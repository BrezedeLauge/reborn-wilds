#!/usr/bin/env python3
"""Offline conversion of three pinned Dungeons Gear armor meshes onto native humanoid sockets.

The original 96px atlases and explicit UVs are preserved. Wolf's empty boot bones
receive the lower four pixels of its authored leg mesh; clipping interpolates UVs,
so wearing both pieces reconstructs the original external surfaces without overlap.
"""
import hashlib
import json
import math
from pathlib import Path
import struct
from convert_bedrock_models import cube_quads, f, java_array

ROOT = Path(__file__).resolve().parents[1]
SOURCE = ROOT / "third_party/dungeons_gear/snapshots/src/main/resources/assets/dungeons_gear/geo/armor"
OUTPUT = ROOT / "src/main/java/de/laugenbreze/dungeonsalive/client/gear/ArmorGeometry.java"
SETS = ("wolf", "spider", "battle_robes")
# Native default anchors. A child offset preserves the authored rest pose when
# Minecraft copies its animated humanoid anchors into these outer socket parts.
SOCKETS = {"bipedHead": ("head", [0, 0, 0]), "bipedBody": ("body", [0, 0, 0]),
           "bipedRightArm": ("right_arm", [-5, 2, 0]), "bipedLeftArm": ("left_arm", [5, 2, 0]),
           "bipedRightLeg": ("right_leg", [-1.9, 12, 0]), "bipedLeftLeg": ("left_leg", [1.9, 12, 0])}


def clip_leg(quads, lower):
    """Clip local leg Y at 8 without inventing texture pixels or internal caps."""
    result = []
    for quad in quads:
        polygon = []
        for current, previous in zip(quad, quad[-1:] + quad[:-1]):
            inside = current[1] >= 8 if lower else current[1] <= 8
            previous_inside = previous[1] >= 8 if lower else previous[1] <= 8
            if inside != previous_inside:
                t = (8 - previous[1]) / (current[1] - previous[1])
                polygon.append([a + t * (b - a) for a, b in zip(previous, current)])
            if inside:
                polygon.append(current)
        if polygon:
            if len(polygon) != 4:
                raise ValueError("Wolf leg split requires axis-aligned quads")
            result.append(polygon)
    return result


def bounds(quads):
    points = [vertex for face in quads for vertex in face]
    minimum = [min(point[i] for point in points) for i in range(3)]
    maximum = [max(point[i] for point in points) for i in range(3)]
    return minimum + [maximum[i] - minimum[i] for i in range(3)]


def generate():
    methods, branches, inventory = [], [], []
    delivery = json.loads((ROOT / "design/gear-source-inventory.json").read_text())
    if delivery["source"]["revision"] != "9ba0075c5b2f13ef54ec914ad1327a853ecdce6f":
        raise ValueError("Unexpected armor source revision")
    expected_hashes = {entry["file"]: entry["sha256"] for entry in delivery["source_files"]}
    for set_id in SETS:
        path = SOURCE / (set_id + "_armor.geo.json")
        if hashlib.sha256(path.read_bytes()).hexdigest() != expected_hashes.get(str(path.relative_to(ROOT))):
            raise ValueError("Armor source differs from the pinned inventory: " + set_id)
        raw = json.loads(path.read_text())
        if len(raw["minecraft:geometry"]) != 1:
            raise ValueError("Expected one armor geometry")
        geo = raw["minecraft:geometry"][0]
        bones = {bone["name"]: bone for bone in geo["bones"]}
        if len(bones) != len(geo["bones"]) or set(bones) - set(SOCKETS) - {
                "armorHead", "armorBody", "armorRightArm", "armorLeftArm", "armorRightLeg", "armorLeftLeg", "armorRightBoot", "armorLeftBoot", "hoodback"}:
            raise ValueError("Unexpected or duplicate armor bone")
        if {name for name, bone in bones.items() if "parent" not in bone} != set(SOCKETS):
            raise ValueError("Unexpected armor root sockets")
        tw, th = geo["description"]["texture_width"], geo["description"]["texture_height"]
        texture = ROOT / ("src/main/resources/assets/dungeonsalive/textures/models/armor/" + set_id + "_armor.png")
        png = texture.read_bytes()
        if png[:8] != b"\x89PNG\r\n\x1a\n" or struct.unpack(">II", png[16:24]) != (tw, th):
            raise ValueError("Armor texture dimensions do not match the actual source UV atlas")
        cubes = {}
        for name, bone in bones.items():
            for cube in bone.get("cubes", []):
                if cube.get("rotation") or cube.get("pivot"):
                    raise ValueError("Rotated cube needs explicit conversion support")
                _, quads = cube_quads(cube, bone, bone["pivot"], tw, th)
                cubes.setdefault(name, []).append(quads)
        if set_id == "wolf":
            for side in ("Left", "Right"):
                leg, boot = "armor" + side + "Leg", "armor" + side + "Boot"
                if cubes.get(boot) or len(cubes.get(leg, [])) != 1:
                    raise ValueError("Wolf source boot/leg structure changed")
                original = cubes[leg][0]
                cubes[leg] = [clip_leg(original, False)]
                cubes[boot] = [clip_leg(original, True)]
        numbers = {name: i for i, name in enumerate(bones)}
        for name, bone in bones.items():
            parent = bone.get("parent")
            if parent and parent not in bones:
                raise ValueError("Missing armor parent")
            pivot = bone["pivot"]
            if parent:
                pp = bones[parent]["pivot"]
                offset = [pivot[0] - pp[0], pp[1] - pivot[1], pivot[2] - pp[2]]
            else:
                native = SOCKETS[name][1]
                offset = [pivot[0] - native[0], 24 - pivot[1] - native[1], pivot[2] - native[2]]
            rotation = bone.get("rotation", [0, 0, 0])
            pose = offset + [math.radians(n) for n in rotation]
            calls = [f'children.put("{child}", {set_id}{numbers[child]}());'
                     for child, candidate in bones.items() if candidate.get("parent") == name]
            meshes = ["new ReferenceMeshCube(" + ", ".join(f(n) for n in bounds(quads)) +
                      ", new float[][][]" + java_array(quads) + ")" for quads in cubes.get(name, [])]
            methods.append(f'    private static ModelPart {set_id}{numbers[name]}() {{\n'
                           '        var children = new LinkedHashMap<String, ModelPart>();\n        ' + "\n        ".join(calls) +
                           '\n        var part = new ModelPart(List.of(' + ",\n            ".join(meshes) + '), children);\n'
                           '        var pose = PartPose.offsetAndRotation(' + ", ".join(f(n) for n in pose) + ');\n'
                           '        part.setInitialPose(pose); part.loadPose(pose); return part;\n    }')
        lines = []
        for source_name, (native_name, offset) in SOCKETS.items():
            lines.append(f'root.put("{native_name}", socket("{source_name}", {set_id}{numbers[source_name]}(), '
                         + ", ".join(f(n) for n in offset) + '));')
        methods.append(f'    private static ModelPart {set_id}() {{\n        var root = new LinkedHashMap<String, ModelPart>();\n        '
                       + "\n        ".join(lines) + '\n        root.put("hat", new ModelPart(List.of(), Map.of()));\n'
                       '        return new ModelPart(List.of(), root);\n    }')
        branches.append(f'case {set_id.upper()} -> {set_id}();')
        inventory.append({"id": set_id, "source": str(path.relative_to(ROOT)), "sha256": hashlib.sha256(path.read_bytes()).hexdigest(),
                          "atlas_size": [tw, th], "source_bones": len(bones), "source_cubes": sum(len(b.get("cubes", [])) for b in bones.values()),
                          "adaptation": "Native humanoid socket offsets retain source rest placement, pivots, rotations and explicit UVs. " +
                          ("Empty boot bones receive lower four pixels of source leg mesh through proportional UV clipping at local Y=8; complementary leggings reconstruct the complete original surface. Open clothing seam, no duplicated faces or fabricated pixels." if set_id == "wolf" else "Authored leg and boot meshes remain separate equipment slots.")})
    text = '''/* Adapted Dungeons Gear visual assets: MIT, Copyright (c) 2022 Patrigan.
 * Generated from retained pinned Geo snapshots by tools/generate_gear_armor_models.py.
 * Original meshes/UVs and the explicit Wolf slot split: design/gear-armor-models.json. */
package de.laugenbreze.dungeonsalive.client.gear;
import de.laugenbreze.dungeonsalive.item.gear.ArmorSetKind;
import de.laugenbreze.dungeonsalive.client.entity.reference.imported.ReferenceMeshCube;
import java.util.*;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;

public final class ArmorGeometry {
    private ArmorGeometry() {}
    public static ModelPart bakeRoot(ArmorSetKind kind) {
        return switch (kind) { ''' + " ".join(branches) + ''' };
    }
    private static ModelPart socket(String name, ModelPart child, float x, float y, float z) {
        var part = new ModelPart(List.of(), Map.of(name, child));
        var pose = PartPose.offset(x, y, z);
        part.setInitialPose(pose); part.loadPose(pose); return part;
    }
''' + "\n".join(methods) + "\n}\n"
    OUTPUT.parent.mkdir(parents=True, exist_ok=True)
    OUTPUT.write_text(text)
    manifest = {"source_repository": "Infamous-Misadventures/Dungeons-Gear", "revision": "9ba0075c5b2f13ef54ec914ad1327a853ecdce6f",
                "license": "MIT", "models": inventory, "generated_java": str(OUTPUT.relative_to(ROOT)), "generated_sha256": hashlib.sha256(text.encode()).hexdigest(),
                "validation": "Reproducible geometry conversion only; no native Minecraft visual acceptance is implied."}
    (ROOT / "design/gear-armor-models.json").write_text(json.dumps(manifest, indent=2) + "\n")
    print("Generated three source-shaped armor meshes and design/gear-armor-models.json")


if __name__ == "__main__":
    generate()
