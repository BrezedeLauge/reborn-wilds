#!/usr/bin/env python3
"""Convert explicitly audited OptiFine CEM models to Minecraft 1.21.1 model layers.

The converter itself is original MIT-licensed project code. Generated geometry
and retained animation expressions remain adaptations of the source assets and
keep their source license. No archive or executable is copied. Explicitly
approved source PNGs may be combined losslessly into a generated texture atlas.

Usage:
  python3 tools/convert_cem_models.py --source-root /path/to/ReCrafted-Mobs \
      --audit /path/to/recrafted-boss-map.json

This deliberately is not an OptiFine replacement. Unknown hosts and unsupported
rendering features fail with the source bone/box in the error, rather than
silently producing a convincing but incorrect model. The currently audited
bosses use the fully supported standard UV path. JPM inheritance, local pivots,
axis inversion, U mirroring, scale, replace/attach and native host hierarchy are
preserved. Per-face UV/V mirroring use the project's ReferenceMeshCube baker;
multiple audited textures use a deterministic atlas. Installed vanilla textures
remain runtime resources; armor and native sheep fur use separate material
layers with the same animated rig. Native texture-only bundles use the matching
installed model factory. Game textures and model files are never extracted.
"""

from __future__ import annotations

import argparse
import copy
import hashlib
import json
import math
import re
import sys
from dataclasses import asdict, dataclass, field
from pathlib import Path
from types import SimpleNamespace
from typing import Any

from cem_visual_additions import GENERATED_TEXTURES, adapt as adapt_visuals, generated_texture


PROJECT = Path(__file__).resolve().parents[1]
PACKAGE = "de.laugenbreze.dungeonsalive.client.entity.reference.imported"
JAVA_DIR = PROJECT / "src/main/java" / PACKAGE.replace(".", "/")
PREFIX = "__cem_host_"
APPROVED = "reuse_with_CC_BY_NC_SA_attribution_changes_and_sharealike"
LICENSE = "CC-BY-NC-SA-4.0"


class ConversionError(ValueError):
    pass


def vector(value: Any, length: int, context: str, default: float = 0) -> list[float]:
    if value is None:
        return [default] * length
    if not isinstance(value, list) or len(value) != length:
        raise ConversionError(f"{context}: expected {length} numeric components")
    try:
        result = [float(v) for v in value]
    except (ValueError, TypeError) as error:
        raise ConversionError(f"{context}: nonnumeric vector") from error
    if not all(math.isfinite(v) for v in result):
        raise ConversionError(f"{context}: nonfinite vector")
    return result


def scalar(value: Any, context: str) -> float:
    try:
        result = float(value)
    except (ValueError, TypeError) as error:
        raise ConversionError(f"{context}: nonnumeric scalar") from error
    if not math.isfinite(result):
        raise ConversionError(f"{context}: nonfinite scalar")
    return result


def f(value: float) -> str:
    if abs(value) < 1e-10:
        value = 0
    text = f"{value:.9g}"
    return text + ("F" if "." in text or "e" in text.lower() else ".0F")


def quote(value: str) -> str:
    return json.dumps(value, ensure_ascii=True)


@dataclass
class Box:
    position: list[float]
    dimensions: list[float]
    uv: list[float]
    inflate: list[float]
    mirror_u: bool
    mirror_v: bool = False
    face_uv: dict[str, list[float]] | None = None
    texture: str = ""
    texture_size: list[int] = field(default_factory=list)


@dataclass
class Bone:
    name: str
    parent: str | None
    translate: list[float] = field(default_factory=lambda: [0., 0., 0.])
    rotate: list[float] = field(default_factory=lambda: [0., 0., 0.])
    scale: float = 1.
    boxes: list[Box] = field(default_factory=list)
    host: bool = False
    vanilla_path: str | None = None
    retain_vanilla_cubes: bool = False
    source_part: str | None = None
    source_translate: list[float] | None = None
    source_rotate: list[float] | None = None
    invert_axis: str = ""
    native_pose: bool = False


@dataclass
class Conversion:
    runtime_id: str
    class_name: str
    host: str
    bones: list[Bone]
    aliases: dict[str, str]
    expressions: list[dict[str, str]]
    source_file: str | None
    source_sha256: str | None
    texture_size: list[int]
    textures: list[str]
    used_sources: set[str]
    modifications: list[str]
    source_model_count: int
    source_box_count: int
    render_texture_source: str
    atlas: dict[str, Any] | None = None
    materials: list[dict[str, Any]] = field(default_factory=list)
    native_factory: str | None = None
    hidden_bones: list[str] = field(default_factory=list)
    visual_additions: list[dict[str, Any]] = field(default_factory=list)


def host_rig(host: str) -> tuple[list[Bone], dict[str, str], str]:
    """MC1.21.1 rest poses; geometry stays in the installed vanilla client.

    CEM top parts are children of these real host parts. Omitting a parent (most
    visibly Warden's body at Y=24) changes both geometry and expression behavior.
    """
    bones: list[Bone] = []
    aliases: dict[str, str] = {}

    def add(alias: str, vanilla: str, parent: str | None = None,
            pos=(0., 0., 0.), rot=(0., 0., 0.)) -> None:
        name = PREFIX + alias
        parent_name = PREFIX + parent if parent else None
        parent_path = next((b.vanilla_path for b in bones if b.name == parent_name), "") or ""
        bones.append(Bone(name, parent_name, list(pos), list(rot), host=True,
                          vanilla_path=parent_path + "/" + vanilla, retain_vanilla_cubes=True))
        aliases[alias] = name

    if host == "slime":
        add("body", "cube")
        for name in ["right_eye", "left_eye", "mouth"]:
            add(name, name)
        factory = "net.minecraft.client.model.SlimeModel.createInnerBodyLayer()"
    elif host == "warden":
        add("root", "bone", pos=(0., 24., 0.))
        aliases["body"] = PREFIX + "root"  # OptiFine name; root is also the driver alias.
        add("torso", "body", "root", (0., -21., 0.))
        add("head", "head", "torso", (0., -13., 0.))
        add("right_tendril", "right_tendril", "head", (-8., -12., 0.))
        add("left_tendril", "left_tendril", "head", (8., -12., 0.))
        add("right_ribcage", "right_ribcage", "torso", (-7., -2., -4.))
        add("left_ribcage", "left_ribcage", "torso", (7., -2., -4.))
        add("right_arm", "right_arm", "torso", (-13., -13., 1.))
        add("left_arm", "left_arm", "torso", (13., -13., 1.))
        add("right_leg", "right_leg", "root", (-5.9, -13., 0.))
        add("left_leg", "left_leg", "root", (5.9, -13., 0.))
        # Vanilla's bone is an empty transform, never a missing visible part.
        bones[0].retain_vanilla_cubes = False
        factory = "net.minecraft.client.model.WardenModel.createBodyLayer()"
    elif host == "wither":
        add("body1", "shoulders")
        add("body2", "ribcage", pos=(-2., 6.9, -.5), rot=(.20420352, 0., 0.))
        add("body3", "tail", pos=(-2., 6.9 + math.cos(.20420352) * 10,
                                    -.5 + math.sin(.20420352) * 10), rot=(.83252203, 0., 0.))
        add("head1", "center_head")
        add("head2", "right_head", pos=(-8., 4., 0.))
        add("head3", "left_head", pos=(10., 4., 0.))
        factory = "net.minecraft.client.model.WitherBossModel.createBodyLayer(CubeDeformation.NONE)"
    elif host == "guardian":
        add("body", "head")
        add("eye", "eye", "body", (0., 0., -8.25))
        add("tail1", "tail0", "body")
        add("tail2", "tail1", "tail1", (-1.5, .5, 14.))
        add("tail3", "tail2", "tail2", (.5, .5, 6.))
        rx = [1.75, .25, 0, 0, .5, .5, .5, .5, 1.25, .75, 0, 0]
        ry = [0, 0, 0, 0, .25, 1.75, 1.25, .75, 0, 0, 0, 0]
        rz = [0, 0, .25, 1.75, 0, 0, 0, 0, 0, 0, .75, 1.25]
        px = [0, 0, 8, -8, -8, 8, 8, -8, 0, 0, 8, -8]
        py = [-8, -8, -8, -8, 0, 0, 0, 0, 8, 8, 8, 8]
        pz = [8, -8, 0, 0, -8, -8, 8, 8, 8, -8, 0, 0]
        for i in range(12):
            pulse = 1 + math.cos(i) * .01
            add(f"spine{i+1}", f"spike{i}", "body",
                (px[i] * pulse, 16 + py[i] * pulse, pz[i] * pulse),
                (rx[i] * math.pi, ry[i] * math.pi, rz[i] * math.pi))
        factory = "net.minecraft.client.model.GuardianModel.createBodyLayer()"
    else:
        # Additional hosts use their installed vanilla layer for exact rest poses.
        # This avoids duplicating version-sensitive pivots in the converter. The
        # generated configure() selects bakeRoot(), never the unbound template.
        def native(alias: str, vanilla: str | None = None, parent: str | None = None,
                   empty: bool = False) -> None:
            add(alias, vanilla or alias, parent)
            bones[-1].native_pose = True
            if empty:
                bones[-1].retain_vanilla_cubes = False

        def quadruped() -> None:
            native("head"); native("body")
            for i, name in enumerate(["right_hind_leg", "left_hind_leg", "right_front_leg", "left_front_leg"], 1):
                native(f"leg{i}", name)

        if host == "chicken":
            for name in ["head", "body", "right_leg", "left_leg", "right_wing", "left_wing"]:
                native(name)
            native("bill", "beak"); native("chin", "red_thing")
            factory = "net.minecraft.client.model.ChickenModel.createBodyLayer()"
        elif host in ("cow", "pig", "sheep"):
            quadruped()
            factory = {"cow": "net.minecraft.client.model.CowModel.createBodyLayer()",
                       "pig": "net.minecraft.client.model.PigModel.createBodyLayer(CubeDeformation.NONE)",
                       "sheep": "net.minecraft.client.model.SheepModel.createBodyLayer()"}[host]
        elif host == "cat":
            native("head"); native("body")
            for alias, vanilla in {
                "back_right_leg": "right_hind_leg", "back_left_leg": "left_hind_leg",
                "front_right_leg": "right_front_leg", "front_left_leg": "left_front_leg",
            }.items():
                native(alias, vanilla)
            for index, alias in enumerate(["back_right_leg", "back_left_leg", "front_right_leg", "front_left_leg"], 1):
                aliases[f"leg{index}"] = aliases[alias]
            native("tail", "tail1"); native("tail2")
            factory = "LayerDefinition.create(net.minecraft.client.model.OcelotModel.createBodyMesh(CubeDeformation.NONE), 64, 32)"
        elif host in ("wandering_trader", "vindicator", "evoker"):
            native("head"); native("headwear", "hat", "head")
            aliases["hat"] = PREFIX + "headwear"  # Native name accepted by the audited evoker CEM.
            if host == "wandering_trader":
                native("headwear2", "hat_rim", "headwear")
            native("nose", "nose", "head")
            native("body")
            if host == "wandering_trader":
                native("bodywear", "jacket", "body")
            native("arms"); native("right_leg"); native("left_leg")
            if host != "wandering_trader":
                native("right_arm"); native("left_arm")
            factory = ("LayerDefinition.create(net.minecraft.client.model.VillagerModel.createBodyModel(), 64, 64)"
                       if host == "wandering_trader" else "net.minecraft.client.model.IllagerModel.createBodyLayer()")
        elif host in ("zombie", "husk", "skeleton", "stray"):
            for name in ["head", "body", "right_arm", "left_arm", "right_leg", "left_leg"]:
                native(name)
            native("headwear", "hat")
            factory = ("net.minecraft.client.model.SkeletonModel.createBodyLayer()" if host in ("skeleton", "stray")
                       else "LayerDefinition.create(net.minecraft.client.model.HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F), 64, 64)")
        elif host == "allay":
            native("root", "root", empty=True)
            native("head", "head", "root"); native("body", "body", "root")
            for name in ["left_arm", "right_arm", "left_wing", "right_wing"]:
                native(name, name, "body")
            factory = "net.minecraft.client.model.AllayModel.createBodyLayer()"
        elif host == "phantom":
            native("body"); native("head", "head", "body")
            native("tail", "tail_base", "body"); native("tail2", "tail_tip", "tail")
            for side in ["left", "right"]:
                native(side + "_wing", side + "_wing_base", "body")
                native(side + "_wing_tip", side + "_wing_tip", side + "_wing")
            factory = "net.minecraft.client.model.PhantomModel.createBodyLayer()"
        elif host == "glow_squid":
            native("body")
            for i in range(8):
                native(f"tentacle{i+1}", f"tentacle{i}")
            factory = "net.minecraft.client.model.SquidModel.createBodyLayer()"
        elif host == "parrot":
            for name in ["head", "body", "tail", "left_wing", "right_wing", "left_leg", "right_leg"]:
                native(name)
            factory = "net.minecraft.client.model.ParrotModel.createBodyLayer()"
        elif host == "rabbit":
            for alias, name in {"left_foot": "left_hind_foot", "right_foot": "right_hind_foot",
                                "left_thigh": "left_haunch", "right_thigh": "right_haunch",
                                "left_arm": "left_front_leg", "right_arm": "right_front_leg"}.items():
                native(alias, name)
            for name in ["body", "head", "right_ear", "left_ear", "tail", "nose"]:
                native(name)
            factory = "net.minecraft.client.model.RabbitModel.createBodyLayer()"
        elif host == "fox":
            quadruped(); native("tail", "tail", "body")
            factory = "net.minecraft.client.model.FoxModel.createBodyLayer()"
        elif host == "silverfish":
            for i in range(7):
                native(f"body{i+1}", f"segment{i}")
            for i in range(3):
                native(f"wing{i+1}", f"layer{i}")
            factory = "net.minecraft.client.model.SilverfishModel.createBodyLayer()"
        elif host == "ravager":
            native("neck"); native("head", "head", "neck"); native("jaw", "mouth", "head")
            native("body")
            for i, name in enumerate(["right_hind_leg", "left_hind_leg", "right_front_leg", "left_front_leg"], 1):
                native(f"leg{i}", name)
            factory = "net.minecraft.client.model.RavagerModel.createBodyLayer()"
        else:
            raise ConversionError(f"Unsupported CEM vanilla host: {host}")
    return bones, aliases, factory


class Converter:
    def __init__(self, root: Path, audit: dict[str, Any]):
        self.root = root.resolve()
        self.audit = audit
        self.files = {item["path"]: item for item in audit["source_files"]}
        self.verified: set[str] = set()

    def source(self, relative: str) -> Path:
        path = (self.root / relative).resolve()
        if not path.is_relative_to(self.root):
            raise ConversionError(f"Source path escapes resource pack: {relative}")
        relative = path.relative_to(self.root).as_posix()
        approval = self.files.get(relative)
        if approval is None:
            raise ConversionError(f"File is absent from the approved asset audit: {relative}")
        if relative not in self.verified:
            actual = hashlib.sha256(path.read_bytes()).hexdigest()
            if actual != approval["sha256"]:
                raise ConversionError(f"Audited SHA256 mismatch: {relative}")
            self.verified.add(relative)
        return path

    def resolve_resource(self, name: str, context: str, suffix: str) -> str:
        if not name.endswith(suffix):
            name += suffix
        if name.startswith("assets/"):
            relative = name
        elif ":" in name:
            namespace, name = name.split(":", 1)
            relative = f"assets/{namespace}/{name}"
        elif name.startswith("~/"):
            relative = "assets/minecraft/optifine/" + name[2:]
        elif name.startswith(("textures/", "optifine/")):
            relative = "assets/minecraft/" + name
        else:
            relative = (Path(context).parent / name).as_posix()
        return self.source(relative).relative_to(self.root).as_posix()

    def load_parts(self, document: dict[str, Any], context: str,
                   used: set[str]) -> list[dict[str, Any]]:
        """Resolve earlier baseId definitions and external JPM defaults strictly."""
        definitions: dict[str, dict[str, Any]] = {}
        loading: set[str] = set()

        def resolve(raw: dict[str, Any], source_file: str) -> dict[str, Any]:
            if raw.get("__resolved_cem"):
                return copy.deepcopy(raw)
            node = copy.deepcopy(raw)
            if "texture" in node:
                node["__texture_context"] = source_file
            if "baseId" in node:
                base = definitions.get(str(node["baseId"]))
                if base is None:
                    raise ConversionError(f"{source_file}: baseId must name an earlier part: {node['baseId']}")
                inherited = {k: copy.deepcopy(v) for k, v in base.items() if k != "id"}
                inherited.update(node)
                inherited.pop("__resolved_cem", None)
                node = inherited
            if node.get("model"):
                external = self.resolve_resource(str(node["model"]), source_file, ".jpm")
                if external in loading:
                    raise ConversionError(f"Cyclic JPM reference: {external}")
                loading.add(external)
                used.add(external)
                part = resolve(json.loads(self.source(external).read_text()), external)
                loading.remove(external)
                # An external part supplies defaults; explicit JEM properties win.
                inherited = {k: v for k, v in part.items() if k not in ("part", "attach", "id")}
                inherited.update(node)
                node = inherited
                node.pop("model", None)
            children = list(node.get("submodels", []))
            if node.get("submodel") is not None:
                children.append(node["submodel"])
            node["submodels"] = [resolve(child, source_file) for child in children]
            node.pop("submodel", None)
            node["__resolved_cem"] = True
            if node.get("id"):
                definitions[str(node["id"])] = copy.deepcopy(node)
            return node

        return [resolve(raw, context) for raw in document.get("models", [])]

    def convert(self, entry: dict[str, Any]) -> Conversion:
        if entry.get("model_file") is None:
            return self.convert_native(entry)
        if entry.get("decision", entry.get("license_decision")) != APPROVED or entry.get("license") != LICENSE:
            raise ConversionError(f"No compatible explicit reuse approval for {entry.get('runtime_id')}")
        runtime_id = entry["runtime_id"]
        identifier = runtime_id.split(":", 1)[-1]
        class_name = "Cem" + "".join(part.capitalize() for part in identifier.split("_")) + "Geometry"
        source_file = entry["model_file"]
        if not source_file:
            raise ConversionError("Texture-only bundle uses installed vanilla model, not CEM geometry")
        source_path = self.source(source_file)
        document = json.loads(source_path.read_text())
        folder = source_path.parent.name
        host = {"elder_guardian": "guardian", "sheep_wool": "sheep"}.get(folder, folder)
        bones, aliases, _factory = host_rig(host)
        used = {source_file}
        parts = self.load_parts(document, source_file, used)
        native_layers = [binding for binding in entry.get("texture_bindings", []) if binding["node"].startswith("builtin_")]
        if any(binding["node"] != "builtin_SheepFurModel" for binding in native_layers):
            raise ConversionError("Unknown additional installed model layer")
        source_layers = [binding for binding in entry.get("texture_bindings", []) if binding["node"] == "cem_SheepFurModel"]
        texture_size = [int(v) for v in vector(document.get("textureSize"), 2, source_file + " textureSize")]
        if min(texture_size) <= 0:
            raise ConversionError(f"{source_file}: textureSize must be positive")
        textures = list(entry["texture_files"])
        if identifier == "arch_illager":
            # This authored shared Wither model uses the spawn/invulnerable skin
            # for Archie. The normal Heart skin intentionally contains a solid
            # magenta Archie region; it is unsuitable for the standalone entity.
            spawn_skin = "assets/minecraft/optifine/random/entity/wither/wither_invulnerable6.png"
            if spawn_skin not in textures + entry.get("optional_layer_files", []):
                raise ConversionError("Standalone Archie spawn-phase skin is absent from the approved bundle")
            textures = [spawn_skin]
        if identifier == "heart_of_ender":
            spawn_skin = "assets/minecraft/optifine/random/entity/wither/wither_invulnerable6.png"
            if spawn_skin not in textures + entry.get("optional_layer_files", []):
                raise ConversionError("Visible Archie subtree skin is absent from the approved Heart bundle")
            if spawn_skin not in textures:
                textures.append(spawn_skin)
        for texture in textures:
            self.source(texture)
            used.add(texture)
        root_binding = next((binding for binding in entry.get("texture_bindings", []) if binding["node"] == "root"), {})
        base_texture = root_binding.get("resolved_file", root_binding.get("builtin_reference")) or next((path for path in textures if not path.endswith("_e.png")), None)
        if base_texture is None:
            raise ConversionError(f"{source_file}: no audited base texture")
        # Audit resolution (e.g. a random-entity rule) takes precedence over a JEM
        # default texture; the default may describe a different named variant.
        expressions: list[dict[str, str]] = []
        node_names = {bone.name for bone in bones}
        bindings = {binding["node"]: binding for binding in entry.get("texture_bindings", [])}
        referenced_bones: set[str] = set()

        def collect_references(raw: dict[str, Any]) -> None:
            for group in raw.get("animations", []):
                for target, expression in group.items():
                    referenced_bones.add(target.rsplit(".", 1)[0])
                    referenced_bones.update(re.findall(r"\b([A-Za-z_][\w]*)\.[A-Za-z_][\w]*", str(expression)))
            for child in raw.get("submodels", []):
                collect_references(child)

        for raw in parts:
            collect_references(raw)
        source_node_count = source_box_count = 0
        modifications = [
            "CEM axis conventions converted to Minecraft 1.21.1 local ModelPart poses.",
            "Original custom bone IDs, ordered expressions and uniform bone scales retained.",
            "Empty vanilla host transforms retained under reserved __cem_host_ names.",
            "Audited texture-rule mapping used instead of unverified default texture selection.",
        ]
        if entry.get("model_selection_adaptation"):
            modifications.append(str(entry["model_selection_adaptation"]))
        if entry.get("model_rule", {}).get("selection_kind") == "explicit_normal_species_basis_adaptation_not_unique_upstream_name_rule":
            modifications.append("Explicit normal-species source JEM selected for this pet, with its exact named skin; upstream biome/weighted species variants are not treated as a unique name-based model rule.")
            rule_file = entry["model_rule"]["file"]
            self.source(rule_file)
            used.add(rule_file)

        def add_node(raw: dict[str, Any], parent: str, index: str,
                     inherited_texture: str, inherited_size: list[int]) -> None:
            nonlocal source_node_count, source_box_count
            source_node_count += 1
            name = str(raw.get("id") or "__cem_unnamed_" + index)
            source_name = name
            context = f"{source_file}/{name}"
            if name in node_names:
                if name in referenced_bones:
                    raise ConversionError(f"{context}: duplicate animated rig ID; refusing ambiguous binding")
                name = name + "__occurrence_" + index
                modifications.append(f"Unreferenced duplicate helper ID {source_name} uniquely named {name}; geometry/pose unchanged.")
            node_names.add(name)
            texture = inherited_texture
            if identifier == "heart_of_ender" and source_name == "arch_illager":
                texture = "assets/minecraft/optifine/random/entity/wither/wither_invulnerable6.png"
            if raw.get("texture"):
                binding = bindings.get(source_name)
                if binding is not None:
                    texture = binding.get("resolved_file", binding.get("builtin_reference"))
                    if texture is None:
                        raise ConversionError(f"{context}: audited texture binding has no resolved resource")
                    if not texture.startswith("minecraft:"):
                        self.source(texture)
                else:
                    texture = self.resolve_resource(str(raw["texture"]), raw.get("__texture_context", source_file), ".png")
                if not texture.startswith("minecraft:"):
                    used.add(texture)
            size = [int(v) for v in vector(raw.get("textureSize", inherited_size), 2, context)]
            if min(size) <= 0:
                raise ConversionError(f"{context}: nonpositive local textureSize")
            if raw.get("sprites"):
                raise ConversionError(f"{context}: CEM sprites are not supported by CubeListBuilder")
            if raw.get("attachments"):
                raise ConversionError(f"{context}: attachment render hooks must be bound explicitly")
            invert = str(raw.get("invertAxis", "")).lower()
            if set(invert) - set("xyz"):
                raise ConversionError(f"{context}: invalid invertAxis {invert!r}")
            source_translate = vector(raw.get("translate"), 3, context + " translate")
            source_rotate = vector(raw.get("rotate"), 3, context + " rotate")
            signs = [-1 if axis in invert else 1 for axis in "xyz"]
            translate = [value * sign for value, sign in zip(source_translate, signs)]
            rotate = [math.radians(value * sign) for value, sign in zip(source_rotate, signs)]
            scale = scalar(raw.get("scale", 1), context + " scale")
            if scale <= 0:
                raise ConversionError(f"{context}: scale must be positive")
            mirror = str(raw.get("mirrorTexture", "")).lower()
            if set(mirror) - set("uv"):
                raise ConversionError(f"{context}: invalid mirrorTexture {mirror!r}")
            node = Bone(name, parent, translate, rotate, scale, source_part=raw.get("part"),
                        source_translate=source_translate, source_rotate=source_rotate, invert_axis=invert)
            for i, box in enumerate(raw.get("boxes", [])):
                box_context = f"{context}/box{i}"
                coords = vector(box.get("coordinates"), 6, box_context + " coordinates")
                origin, dimensions = coords[:3], coords[3:]
                if any(v < 0 for v in dimensions):
                    raise ConversionError(f"{box_context}: negative box dimensions")
                origin = [-x-d if sign < 0 else x for x, d, sign in zip(origin, dimensions, signs)]
                uv = vector(box.get("textureOffset"), 2, box_context + " textureOffset")
                faces = None
                if "textureOffset" not in box:
                    faces = {}
                    for key, value in box.items():
                        if key.startswith("uv") and value:
                            if key not in ("uvNorth", "uvSouth", "uvEast", "uvWest", "uvUp", "uvDown"):
                                raise ConversionError(f"{box_context}: legacy face alias {key} needs an explicit mapping")
                            faces[key[2:].lower()] = vector(value, 4, box_context + " " + key)
                    if not faces:
                        raise ConversionError(f"{box_context}: box has neither box UV nor face UV data")
                inflate = (vector(box["sizesAdd"], 3, box_context + " sizesAdd")
                           if "sizesAdd" in box else [scalar(box.get("sizeAdd", 0), box_context)] * 3)
                node.boxes.append(Box(origin, dimensions, uv, inflate, "u" in mirror, "v" in mirror,
                                      faces, texture, size))
                source_box_count += 1
            bones.append(node)
            for group in raw.get("animations", []):
                if not isinstance(group, dict):
                    raise ConversionError(f"{context}: animation group must be an object")
                for target, expression in group.items():
                    source_target, source_expression = str(target), str(expression)
                    # `this` denotes this authored CUSTOM node, even when its ID
                    # also names a vanilla host. EMF uses this explicit namespace
                    # during its lexical preprocessing; a global `this` alias or
                    # replacing it with an unqualified host ID changes the rig.
                    canonical_target = re.sub(r"\bthis(?=\.)", "EMF_" + name, source_target)
                    canonical_expression = re.sub(r"\bthis(?=\.)", "EMF_" + name, source_expression)
                    expressions.append({"target": canonical_target, "expression": canonical_expression,
                                        "source_bone": name, "source_target": source_target,
                                        "source_expression": source_expression})
            for i, child in enumerate(raw.get("submodels", [])):
                add_node(child, name, index + "_" + str(i), texture, size)

        replaced: set[str] = set()
        for i, raw in enumerate(parts):
            part = raw.get("part")
            if part not in aliases:
                raise ConversionError(f"{source_file}: unknown {host} host part {part!r}")
            parent = aliases[part]
            if not bool(raw.get("attach", False)):
                replaced.add(parent)
            add_node(raw, parent, str(i), base_texture, texture_size)
        for bone in bones:
            if bone.name in replaced:
                bone.retain_vanilla_cubes = False

        if identifier == "heart_of_ender":
            modifications.append("Visible Archie subtree uses the audited spawn-phase skin in a separate atlas tile; Heart keeps normal6 and its emissive mask. This corrects the normal skin's solid-magenta Archie region without changing geometry.")

        if identifier == "arch_illager":
            # Both subjects belong to one authored model. This exported variant
            # extracts the actual Archie subtree, preserving its inherited scale.
            root_bone = next((b for b in bones if b.name == "arch_illager"), None)
            if root_bone is None:
                raise ConversionError("Audited shared Arch/Heart model lacks arch_illager subtree")
            by_name = {b.name: b for b in bones}
            ancestry: list[str] = []
            inherited_scale = 1.
            parent = root_bone.parent
            while parent is not None:
                ancestry.append(parent)
                inherited_scale *= by_name[parent].scale
                parent = by_name[parent].parent
            selected = {root_bone.name}
            for bone in bones:
                if bone.parent in selected:
                    selected.add(bone.name)
            bones = [b for b in bones if b.host or b.name in selected]
            for bone in bones:
                if bone.host:
                    bone.retain_vanilla_cubes = False
            root_bone.parent = aliases["body1"]
            root_bone.translate = [0., 0., 0.]
            root_bone.scale *= inherited_scale
            bounds = rest_bounds(bones)
            root_bone.translate[1] = 24. - bounds["max"][1]
            # Source's arch_illager.ty=50 positions it inside the Heart. Applying
            # it to a separated entity would put it below the floor.
            expressions = [expression for expression in expressions
                           if expression["target"].rsplit(".", 1)[0] in selected
                           and expression["target"] not in ("arch_illager.tx", "arch_illager.ty", "arch_illager.tz")]
            modifications.extend([
                "Standalone Arch-Illager selected from original arch_illager subtree; Heart geometry excluded.",
                f"Inherited ancestor uniform scale {inherited_scale:g} retained on extracted root.",
                "Extracted root recentered at entity X/Z and placed with lowest rest vertex at model Y=24.",
                "Heart-specific arch_illager.ty=50 placement expression omitted from standalone entity.",
                "Uses audited wither_invulnerable6.png spawn-phase color skin; normal Heart skin contains a solid magenta Archie region.",
                "Heart-only emissive skin omitted from standalone Archie.",
            ])
        visual_additions = adapt_visuals(identifier, bones, expressions, modifications, Bone, Box)
        actual_textures = sorted({box.texture for bone in bones for box in bone.boxes})
        if not actual_textures:
            raise ConversionError(f"{source_file}: no visible geometry")
        mixed_vanilla = any(texture.startswith("minecraft:") for texture in actual_textures)
        atlas = self.atlas_plan(identifier, actual_textures, textures, bones) if len(actual_textures) > 1 and not mixed_vanilla else None
        if atlas is not None and any(bone.retain_vanilla_cubes for bone in bones):
            raise ConversionError(f"{source_file}: mixed atlas plus unreplaced vanilla cubes needs runtime material binding")
        main_texture = base_texture if base_texture in actual_textures else actual_textures[0]
        materials = []
        if mixed_vanilla:
            for texture in actual_textures:
                if texture != main_texture:
                    materials.append(self.material(identifier, len(materials), texture, "cem_material"))
            modifications.append("Installed minecraft: base textures remain runtime resources; authored armor is drawn as a separate material with copied master bone poses.")
        for binding in native_layers:
            texture = binding["resolved_file"]
            material = self.material(identifier, len(materials), texture, "native_sheep_fur")
            material["native_factory"] = "net.minecraft.client.model.SheepFurModel.createFurLayer()"
            materials.append(material)
            modifications.append("Installed SheepFurModel cubes retained in their original head/body/leg host transforms as a separately textured outer layer.")
        for binding in source_layers:
            layer_file = binding["model_file"]
            layer_texture = binding["resolved_file"]
            if binding.get("model_rule", {}).get("file"):
                rule_file = binding["model_rule"]["file"]
                self.source(rule_file)
                used.add(rule_file)
            layer = self.convert({
                "runtime_id": runtime_id + "__fur_layer", "model_file": layer_file,
                "license": LICENSE, "decision": APPROVED, "texture_files": [layer_texture],
                "texture_bindings": [{"node": "root", "resolved_file": layer_texture}],
            })
            if layer.host != host or layer.materials or layer.atlas:
                raise ConversionError(f"{layer_file}: auxiliary source rig does not match the primary material host")
            if any(bone.retain_vanilla_cubes for bone in layer.bones):
                raise ConversionError(f"{layer_file}: auxiliary rig with retained native cubes needs an explicit material mapping")
            renames = {bone.name: "__cem_layer_fur_" + bone.name for bone in layer.bones if not bone.host}
            if set(renames.values()) & {bone.name for bone in bones}:
                raise ConversionError(f"{layer_file}: auxiliary material bone name collision")
            for source_bone in layer.bones:
                if source_bone.host:
                    continue
                bone = copy.deepcopy(source_bone)
                bone.name = renames[bone.name]
                bone.parent = renames.get(bone.parent, bone.parent)
                bones.append(bone)

            def layer_bone_reference(match: re.Match[str]) -> str:
                name = match.group(1)
                if name.startswith("EMF_") and name[4:] in renames:
                    return "EMF_" + renames[name[4:]]
                if name in renames and name not in layer.aliases:
                    return renames[name]
                return name

            for source_expression in layer.expressions:
                expression = copy.deepcopy(source_expression)
                for field_name in ["target", "expression"]:
                    expression[field_name] = re.sub(r"\b([A-Za-z_][\w]*)(?=\.[A-Za-z_])", layer_bone_reference, expression[field_name])
                expression["source_file"] = layer_file
                expression["auxiliary_material"] = "fur"
                expressions.append(expression)
            material = self.material(identifier, len(materials), layer_texture, "cem_material")
            material.update({"source_model_file": layer_file, "source_model_sha256": layer.source_sha256,
                             "source_texture_size": layer.texture_size, "source_box_count": layer.source_box_count,
                             "bone_namespace": "__cem_layer_fur_", "source_bone_mapping": renames})
            materials.append(material)
            used.update(layer.used_sources)
            source_node_count += layer.source_model_count
            source_box_count += layer.source_box_count
            modifications.append("Original sheep_wool CEM geometry, 64x64 UV layout and ordered expressions retained as a separate fur material; its custom bones are namespaced under __cem_layer_fur_ to avoid collisions with the primary rig.")
        return Conversion(runtime_id, class_name, host, bones, aliases, expressions,
                          source_file, self.files[source_file]["sha256"], texture_size,
                          list(textures), used, modifications, source_node_count, source_box_count,
                          main_texture, atlas, materials, visual_additions=visual_additions)

    def material(self, identifier: str, index: int, texture: str, kind: str) -> dict[str, Any]:
        if texture.startswith("minecraft:"):
            return {"kind": kind, "source": texture, "resource": texture, "file": None}
        self.source(texture)
        filename = identifier + f"_material_{index+1}.png"
        return {"kind": kind, "source": texture,
                "resource": "dungeonsalive:textures/entity/reference/cem/" + filename,
                "file": "src/main/resources/assets/dungeonsalive/textures/entity/reference/cem/" + filename}

    def convert_native(self, entry: dict[str, Any]) -> Conversion:
        if entry.get("license_decision") != "reuse_texture_with_CC_BY_NC_SA_attribution_changes_and_sharealike" or entry.get("license") != LICENSE:
            raise ConversionError("Native wrapper needs explicit texture-only reuse approval")
        identifier = entry["runtime_id"].split(":", 1)[-1]
        native_model = entry["builtin_model"]
        spec = {
            "GoatModel": ("goat", "net.minecraft.client.model.GoatModel.createBodyLayer()", [64, 64]),
            "CatModel": ("cat", "LayerDefinition.create(net.minecraft.client.model.OcelotModel.createBodyMesh(CubeDeformation.NONE), 64, 32)", [64, 32]),
            "PigModel": ("pig", "net.minecraft.client.model.PigModel.createBodyLayer(CubeDeformation.NONE)", [64, 32]),
            "TurtleModel": ("turtle", "net.minecraft.client.model.TurtleModel.createBodyLayer()", [128, 64]),
        }.get(native_model)
        if spec is None:
            raise ConversionError("No native geometry wrapper for " + native_model)
        host, factory, texture_size = spec
        aliases = {"head": "head", "body": "body", "leg1": "right_hind_leg", "leg2": "left_hind_leg",
                   "leg3": "right_front_leg", "leg4": "left_front_leg"}
        bones = [Bone(name, None, host=True, vanilla_path="/" + name, retain_vanilla_cubes=True, native_pose=True)
                 for name in aliases.values()]
        hidden = []
        if host == "goat":
            for name in ["left_horn", "right_horn", "nose"]:
                aliases[name] = name
                bones.append(Bone(name, "head", host=True, vanilla_path="/head/" + name, retain_vanilla_cubes=True, native_pose=True))
            hidden = ["left_horn", "right_horn"]
        elif host == "cat":
            aliases.update({"tail": "tail1", "tail2": "tail2"})
            for name in ["tail1", "tail2"]:
                bones.append(Bone(name, None, host=True, vanilla_path="/" + name, retain_vanilla_cubes=True, native_pose=True))
        elif host == "turtle":
            aliases["egg_belly"] = "egg_belly"
            bones.append(Bone("egg_belly", None, host=True, vanilla_path="/egg_belly", retain_vanilla_cubes=True, native_pose=True))
            hidden = ["egg_belly"]
        textures = list(entry["texture_files"])
        for texture in textures:
            self.source(texture)
        if len(textures) != 1:
            raise ConversionError("Native wrapper currently expects one audited texture")
        return Conversion(entry["runtime_id"], "Cem" + "".join(part.capitalize() for part in identifier.split("_")) + "Geometry",
                          host, bones, aliases, [], None, None,
                          texture_size, textures, set(textures),
                          ["Geometry supplied by the installed Minecraft 1.21.1 model factory; no vanilla model or texture file is copied.",
                           "Audited ReCrafted texture bound to its matching native UV layout.",
                           "Pet scale is a separate runtime binding; this wrapper does not claim source-specific original-game baby proportions."]
                          + (["Baby goat horns hidden."] if host == "goat" else [])
                          + (["Nonpregnant baby turtle egg belly hidden."] if host == "turtle" else []),
                          0, 0, textures[0], native_factory=factory, hidden_bones=hidden)

    def write_materials(self, model: Conversion) -> None:
        for material in model.materials:
            if material["file"] is not None:
                target = PROJECT / material["file"]
                target.parent.mkdir(parents=True, exist_ok=True)
                target.write_bytes(self.source(material["source"]).read_bytes())

    def atlas_plan(self, identifier: str, textures: list[str], approved_textures: list[str], bones: list[Bone]) -> dict[str, Any]:
        padding = 4
        offset = padding
        height = 0
        tiles = []
        for texture in textures:
            with self.texture_image(texture) as source_image:
                width, tile_height = source_image.size
            glow = texture.removesuffix(".png") + "_e.png"
            if glow not in approved_textures and glow not in GENERATED_TEXTURES:
                glow = None
            limits = [0., 0., 1., 1.]
            for bone in bones:
                for box in bone.boxes:
                    if box.texture != texture:
                        continue
                    for quad in box_quads(box, SimpleNamespace(atlas=None)):
                        for vertex in quad:
                            limits = [min(limits[0], vertex[3]), min(limits[1], vertex[4]),
                                      max(limits[2], vertex[3]), max(limits[3], vertex[4])]
            repeat = [math.floor(limits[0]), math.floor(limits[1]), math.ceil(limits[2]), math.ceil(limits[3])]
            tile_width = width * (repeat[2] - repeat[0])
            tile_total_height = tile_height * (repeat[3] - repeat[1])
            tiles.append({"source": texture, "emissive_source": glow,
                          "pixel_origin": "dungeonsalive_original_generated_pixels" if texture in GENERATED_TEXTURES else "audited_source_pixels_unchanged",
                          "x": offset - repeat[0] * width, "y": padding - repeat[1] * tile_height,
                          "width": width, "height": tile_height, "repeat_range": repeat})
            offset += tile_width + padding * 2
            height = max(height, tile_total_height + padding * 2)
        size = [2 ** math.ceil(math.log2(max(1, value))) for value in [offset, height]]
        if max(size) > 4096:
            raise ConversionError(f"{identifier}: audited texture repetition would require an excessive atlas {size}")
        filename = identifier + "_atlas.png"
        resource = "dungeonsalive:textures/entity/reference/cem/" + filename
        return {"size": size, "padding": padding, "tiles": tiles,
                "resource": resource,
                "file": "src/main/resources/assets/dungeonsalive/textures/entity/reference/cem/" + filename,
                "emissive_resource": resource.removesuffix(".png") + "_e.png" if any(t["emissive_source"] for t in tiles) else None,
                "changes": "Lossless assembly of audited source PNGs into separate padded tiles; source repeat wrapping retained; UVs remapped without image resampling. Any explicitly marked dungeonsalive-generated tile is original project artwork, not source pixels."}

    def texture_image(self, texture: str):
        if texture in GENERATED_TEXTURES:
            return generated_texture(texture)
        from PIL import Image
        return Image.open(self.source(texture))

    def write_atlas(self, model: Conversion) -> None:
        if model.atlas is None:
            return
        from PIL import Image

        atlas = model.atlas
        base = Image.new("RGBA", tuple(atlas["size"]), (0, 0, 0, 0))
        glow = Image.new("RGBA", tuple(atlas["size"]), (0, 0, 0, 0))
        for tile in atlas["tiles"]:
            with self.texture_image(tile["source"]) as source_image:
                source_rgba = source_image.convert("RGBA")
                for x in range(tile["repeat_range"][0], tile["repeat_range"][2]):
                    for y in range(tile["repeat_range"][1], tile["repeat_range"][3]):
                        base.paste(source_rgba, (tile["x"] + x * tile["width"], tile["y"] + y * tile["height"]))
            if tile["emissive_source"]:
                with self.texture_image(tile["emissive_source"]) as source_image:
                    if source_image.size != (tile["width"], tile["height"]):
                        raise ConversionError("Emissive/base texture dimensions differ: " + tile["source"])
                    source_rgba = source_image.convert("RGBA")
                    for x in range(tile["repeat_range"][0], tile["repeat_range"][2]):
                        for y in range(tile["repeat_range"][1], tile["repeat_range"][3]):
                            glow.paste(source_rgba, (tile["x"] + x * tile["width"], tile["y"] + y * tile["height"]))
        target = PROJECT / atlas["file"]
        target.parent.mkdir(parents=True, exist_ok=True)
        base.save(target)
        if atlas["emissive_resource"]:
            glow.save(target.with_name(target.stem + "_e.png"))


def custom_box(box: Box, model: Conversion) -> bool:
    return bool(model.atlas or box.face_uv is not None or box.mirror_v
                or box.texture_size != model.texture_size or any(int(v) != v for v in box.uv))


def box_quads(box: Box, model: Conversion) -> list[list[list[float]]]:
    """Original implementation of the documented CEM cube/face UV conventions.

    Source axes have already been inverted. OptiFine face UVs address authored
    east/west and up/down faces; those labels differ from Java's positive axes.
    Per-face mode ignores mirrorTexture, matching the CEM reference behavior.
    """
    lo = [p - growth for p, growth in zip(box.position, box.inflate)]
    hi = [p + d + growth for p, d, growth in zip(box.position, box.dimensions, box.inflate)]
    flip = False
    if box.face_uv is None:
        if box.mirror_u:
            lo[0], hi[0] = hi[0], lo[0]
            flip = not flip
        if box.mirror_v:
            lo[1], hi[1] = hi[1], lo[1]
            flip = not flip
    # Corners: near top left/right, near bottom right/left, then the far face.
    vertices = [[lo[0], lo[1], lo[2]], [hi[0], lo[1], lo[2]],
                [hi[0], hi[1], lo[2]], [lo[0], hi[1], lo[2]],
                [lo[0], lo[1], hi[2]], [hi[0], lo[1], hi[2]],
                [hi[0], hi[1], hi[2]], [lo[0], hi[1], hi[2]]]
    if box.face_uv is not None:
        faces = {"up": [0, 1, 5, 4], "down": [7, 6, 2, 3],
                 "west": [5, 1, 2, 6], "east": [0, 4, 7, 3],
                 "north": [1, 0, 3, 2], "south": [4, 5, 6, 7]}
        rectangles = box.face_uv
    else:
        u, v = box.uv
        dx, dy, dz = box.dimensions
        a, b, c, d, e = u + dz, u + dz + dx, u + dz + 2 * dx, u + 2 * dz + dx, u + 2 * dz + 2 * dx
        top, bottom = v + dz, v + dz + dy
        faces = {"down": [5, 4, 0, 1], "up": [2, 3, 7, 6],
                 "west": [0, 4, 7, 3], "east": [5, 1, 2, 6],
                 "north": [1, 0, 3, 2], "south": [4, 5, 6, 7]}
        rectangles = {"down": [a, v, b, top], "up": [b, top, c, v],
                      "west": [u, top, a, bottom], "east": [b, top, d, bottom],
                      "north": [a, top, b, bottom], "south": [d, top, e, bottom]}
    tile = next((item for item in model.atlas["tiles"] if item["source"] == box.texture), None) if model.atlas else None

    def uv_point(u: float, v: float) -> list[float]:
        nu, nv = u / box.texture_size[0], v / box.texture_size[1]
        if tile:
            repeat = tile["repeat_range"]
            if not (repeat[0] - 1e-7 <= nu <= repeat[2] + 1e-7 and repeat[1] - 1e-7 <= nv <= repeat[3] + 1e-7):
                raise ConversionError(f"{model.runtime_id}: UV exceeds the validated repeated source tile")
            return [(tile["x"] + nu * tile["width"]) / model.atlas["size"][0],
                    (tile["y"] + nv * tile["height"]) / model.atlas["size"][1]]
        return [nu, nv]

    result = []
    for face, rectangle in rectangles.items():
        u0, v0, u1, v1 = rectangle
        texture_points = [uv_point(u1, v0), uv_point(u0, v0), uv_point(u0, v1), uv_point(u1, v1)]
        quad = [vertices[index] + point for index, point in zip(faces[face], texture_points)]
        result.append(list(reversed(quad)) if flip else quad)
    return result


def rest_bounds(bones: list[Bone]) -> dict[str, list[float]]:
    """Conservative deformed box bounds, in model pixels before expression evaluation."""
    by_name = {bone.name: bone for bone in bones}
    minimum = [math.inf] * 3
    maximum = [-math.inf] * 3

    def transform(point: list[float], bone: Bone) -> list[float]:
        x, y, z = [v * bone.scale for v in point]
        # Minecraft Quaternionf.rotationZYX means X, then Y, then Z on a vector.
        rx, ry, rz = bone.rotate
        y, z = y * math.cos(rx) - z * math.sin(rx), y * math.sin(rx) + z * math.cos(rx)
        x, z = x * math.cos(ry) + z * math.sin(ry), -x * math.sin(ry) + z * math.cos(ry)
        x, y = x * math.cos(rz) - y * math.sin(rz), x * math.sin(rz) + y * math.cos(rz)
        result = [v + delta for v, delta in zip([x, y, z], bone.translate)]
        return transform(result, by_name[bone.parent]) if bone.parent is not None else result

    for bone in bones:
        for box in bone.boxes:
            lo = [p - g for p, g in zip(box.position, box.inflate)]
            hi = [p + d + g for p, d, g in zip(box.position, box.dimensions, box.inflate)]
            for x in (lo[0], hi[0]):
                for y in (lo[1], hi[1]):
                    for z in (lo[2], hi[2]):
                        values = transform([x, y, z], bone)
                        minimum = [min(a, b) for a, b in zip(minimum, values)]
                        maximum = [max(a, b) for a, b in zip(maximum, values)]
    if math.isinf(minimum[0]):
        return {"min": [0., 0., 0.], "max": [0., 0., 0.]}
    return {"min": [round(v, 8) for v in minimum], "max": [round(v, 8) for v in maximum]}


def emit_java(model: Conversion, output: Path, audit: dict[str, Any]) -> None:
    bones = model.bones
    variables = {bone.name: f"p{i}" for i, bone in enumerate(bones)}
    paths: dict[str, str] = {}
    for bone in bones:
        paths[bone.name] = (paths.get(bone.parent, "") if bone.parent else "") + "/" + bone.name

    def is_main_box(box: Box) -> bool:
        return not any(material["kind"] == "cem_material" and box.texture == material["source"] for material in model.materials)

    retained = [bone for bone in bones if bone.retain_vanilla_cubes]
    native_poses = any(bone.native_pose for bone in bones)
    mesh_boxes = any(is_main_box(box) and custom_box(box, model) for bone in bones for box in bone.boxes)
    needs_baker = model.native_factory is None and (bool(retained) or native_poses or mesh_boxes)
    source_description = model.source_file or ("installed Minecraft model factory: " + str(model.native_factory))
    lines = [
        "/*",
        f" * Generated adaptation of ReCrafted Mobs {audit['version']} by GillsBills and credited contributors.",
        f" * Authored asset license: {LICENSE}; https://creativecommons.org/licenses/by-nc-sa/4.0/",
        f" * Source: {audit['source_url']}",
        f" * Model: {source_description}",
        f" * Source SHA256: {model.source_sha256 or 'not applicable to installed game geometry'}",
        " * Changes: CEM-to-Java conversion, explicit native host transforms and runtime material bindings.",
        " * Full credits, source hashes and changes: design/cem-model-imports.json and cem-extra-model-imports.json.",
        " * Regenerate with tools/convert_cem_models.py; do not hand-edit generated geometry.",
        " */",
        f"package {PACKAGE};", "",
        "import de.laugenbreze.dungeonsalive.client.entity.reference.ReferenceMobDefinition;",
        "import net.minecraft.client.model.geom.PartPose;",
        "import net.minecraft.client.model.geom.builders.CubeDeformation;",
        "import net.minecraft.client.model.geom.builders.CubeListBuilder;",
        "import net.minecraft.client.model.geom.builders.LayerDefinition;",
        "import net.minecraft.client.model.geom.builders.MeshDefinition;",
        "import net.minecraft.client.model.geom.builders.PartDefinition;", "",
        f"public final class {model.class_name} {{",
        f"    private {model.class_name}() {{}}", "",
        "    public static LayerDefinition createBodyLayer() {",
    ]
    if model.native_factory:
        lines.append(f"        return {model.native_factory};")
    else:
        lines += ["        MeshDefinition mesh = new MeshDefinition();", "        PartDefinition root = mesh.getRoot();"]
        for bone in bones:
            builder = "CubeListBuilder.create()"
            for box in bone.boxes:
                if not is_main_box(box) or custom_box(box, model):
                    continue
                builder += f"\n                .texOffs({int(box.uv[0])}, {int(box.uv[1])}).mirror({str(box.mirror_u).lower()})"
                values = ", ".join(f(v) for v in box.position + box.dimensions)
                inflate = ", ".join(f(v) for v in box.inflate)
                builder += f"\n                .addBox({values}, new CubeDeformation({inflate}))"
            pose = ("PartPose.ZERO" if not any(bone.translate + bone.rotate) else
                    "PartPose.offsetAndRotation(" + ", ".join(f(v) for v in bone.translate + bone.rotate) + ")")
            parent = variables[bone.parent] if bone.parent else "root"
            lines.append(f"        PartDefinition {variables[bone.name]} = {parent}.addOrReplaceChild({quote(bone.name)}, {builder}, {pose});")
        lines.append(f"        return LayerDefinition.create(mesh, {model.texture_size[0]}, {model.texture_size[1]});")
    lines += ["    }", "",
              "    /** Preserve source expression units; static scales are reapplied after every pose reset. */",
              "    public static ReferenceMobDefinition.Builder configure(ReferenceMobDefinition.Builder builder) {",
              f"        builder.cemDriver({quote(model.host)});"]
    if needs_baker:
        lines.append(f"        builder.bakedRoot({model.class_name}::bakeRoot);")
    for alias, node in model.aliases.items():
        lines.append(f"        builder.hostAlias({quote(alias)}, {quote(node)});")
    if model.hidden_bones:
        lines.append("        builder.hiddenBones(" + ", ".join(quote(name) for name in model.hidden_bones) + ");")
    for bone in bones:
        if bone.scale != 1:
            lines.append(f"        builder.boneScale({quote(bone.name)}, {f(bone.scale)});")
    for expression in model.expressions:
        lines.append(f"        builder.expression({quote(expression['target'])}, {quote(expression['expression'])});")
    for index, material in enumerate(model.materials, 1):
        lines.append(f"        builder.materialLayer({quote(material['resource'])}, {model.class_name}::bakeMaterial{index});")
    lines += ["        return builder;", "    }"]

    def append_mesh_box(variable: str, box: Box) -> None:
        lo = [p - growth for p, growth in zip(box.position, box.inflate)]
        dimensions = [d + 2 * growth for d, growth in zip(box.dimensions, box.inflate)]
        values = ", ".join(f(v) for v in lo + dimensions)
        quads = box_quads(box, model)
        encoded = ",\n                ".join("{" + ", ".join("{" + ", ".join(f(v) for v in vertex) + "}" for vertex in quad) + "}" for quad in quads)
        lines.append(f"        {variable}Cubes.add(new ReferenceMeshCube({values}, new float[][][] {{\n                {encoded}\n        }}));")

    def append_baker(method_name: str, material: dict[str, Any] | None = None) -> None:
        # Public ModelPart.visit exposes installed Cube instances without
        # reflection. Every material preserves the entire rig, including empty
        # custom nodes and parents; the renderer copies the master's live pose.
        _host, _alias, factory = host_rig(model.host)
        lines.extend(["", "    /** Complete matching rig; installed cubes stay owned by the Minecraft client. */",
                      f"    public static net.minecraft.client.model.geom.ModelPart {method_name}() {{",
                      f"        var nativeRoot = {factory}.bakeRoot();"])
        if material is None:
            lines.extend([
                "        var nativeCubes = new java.util.HashMap<String, java.util.List<net.minecraft.client.model.geom.ModelPart.Cube>>();",
                "        nativeRoot.visit(new com.mojang.blaze3d.vertex.PoseStack(),",
                "                (pose, path, index, cube) -> nativeCubes.computeIfAbsent(path, key -> new java.util.ArrayList<>()).add(cube));",
                "        var customCubes = new java.util.HashMap<String, java.util.List<net.minecraft.client.model.geom.ModelPart.Cube>>();",
                "        createBodyLayer().bakeRoot().visit(new com.mojang.blaze3d.vertex.PoseStack(),",
                "                (pose, path, index, cube) -> customCubes.computeIfAbsent(path, key -> new java.util.ArrayList<>()).add(cube));"])
        elif material["kind"] == "native_sheep_fur":
            lines.extend([
                "        var furCubes = new java.util.HashMap<String, java.util.List<net.minecraft.client.model.geom.ModelPart.Cube>>();",
                f"        {material['native_factory']}.bakeRoot().visit(new com.mojang.blaze3d.vertex.PoseStack(),",
                "                (pose, path, index, cube) -> furCubes.computeIfAbsent(path, key -> new java.util.ArrayList<>()).add(cube));"])
        for bone in reversed(bones):
            variable = variables[bone.name]
            children = [b for b in bones if b.parent == bone.name]
            if material is None:
                lines.append(f"        var {variable}Cubes = new java.util.ArrayList<>(customCubes.getOrDefault({quote(paths[bone.name])}, java.util.List.of()));")
                for box in bone.boxes:
                    if is_main_box(box) and custom_box(box, model):
                        append_mesh_box(variable, box)
                if bone.retain_vanilla_cubes:
                    lines.append(f"        {variable}Cubes.addAll(nativeCubes.getOrDefault({quote(bone.vanilla_path or '')}, java.util.List.of()));")
            else:
                lines.append(f"        var {variable}Cubes = new java.util.ArrayList<net.minecraft.client.model.geom.ModelPart.Cube>();")
                if material["kind"] == "cem_material":
                    for box in bone.boxes:
                        if box.texture == material["source"]:
                            append_mesh_box(variable, box)
                elif material["kind"] == "native_sheep_fur" and bone.host:
                    lines.append(f"        {variable}Cubes.addAll(furCubes.getOrDefault({quote(bone.vanilla_path or '')}, java.util.List.of()));")
            mapping = "java.util.Map.ofEntries(" + ", ".join(
                f"java.util.Map.entry({quote(child.name)}, {variables[child.name]})" for child in children) + ")"
            lines.append(f"        var {variable} = new net.minecraft.client.model.geom.ModelPart({variable}Cubes, {mapping});")
            pose = "PartPose.offsetAndRotation(" + ", ".join(f(v) for v in bone.translate + bone.rotate) + ")"
            if bone.native_pose:
                pose = "nativeRoot" + "".join(".getChild(" + quote(name) + ")" for name in (bone.vanilla_path or "").split("/") if name) + ".storePose()"
            lines.append(f"        {variable}.setInitialPose({pose}); {variable}.resetPose();")
        children = [b for b in bones if b.parent is None]
        mapping = "java.util.Map.ofEntries(" + ", ".join(
            f"java.util.Map.entry({quote(child.name)}, {variables[child.name]})" for child in children) + ")"
        lines.extend([f"        return new net.minecraft.client.model.geom.ModelPart(java.util.List.of(), {mapping});", "    }"])

    if needs_baker:
        append_baker("bakeRoot")
    for index, material in enumerate(model.materials, 1):
        append_baker(f"bakeMaterial{index}", material)
    lines += ["}", ""]
    output.mkdir(parents=True, exist_ok=True)
    target = output / f"{model.class_name}.java"
    content = "\n".join(lines)
    if not target.exists() or target.read_text() != content:
        target.write_text(content)


def manifest_entry(model: Conversion, output: Path, audit: dict[str, Any]) -> dict[str, Any]:
    return {
        "runtime_id": model.runtime_id,
        "id": model.runtime_id.split(":", 1)[-1],
        "class_name": model.class_name,
        "class": PACKAGE + "." + model.class_name,
        "java_file": str((output / (model.class_name + ".java")).relative_to(PROJECT)),
        "source_file": model.source_file,
        "source_sha256": model.source_sha256,
        "license": LICENSE,
        "vanilla_host": model.host,
        "texture_size": model.texture_size,
        "texture_files": model.textures,
        "render_texture_source": model.render_texture_source,
        "render_texture_resource": model.render_texture_source if model.render_texture_source.startswith("minecraft:") else None,
        "generated_atlas": model.atlas,
        "material_layers": model.materials,
        "native_model_factory": model.native_factory,
        "hidden_bones": model.hidden_bones,
        "requires_custom_baker": model.native_factory is None and (
            any(bone.native_pose or bone.retain_vanilla_cubes for bone in model.bones)
            or any(custom_box(box, model) for bone in model.bones for box in bone.boxes)),
        "host_aliases": model.aliases,
        "static_scales": {bone.name: bone.scale for bone in model.bones if bone.scale != 1},
        "expressions": model.expressions,
        "bones": [asdict(bone) for bone in model.bones],
        "source_model_count": None if model.native_factory else model.source_model_count,
        "source_box_count": None if model.native_factory else model.source_box_count,
        "output_box_count": None if model.native_factory else sum(len(bone.boxes) for bone in model.bones),
        "native_geometry_count_note": "Installed game factory: counted by runtime rig validation, not copied into source data." if model.native_factory else None,
        "rest_bounds_pixels_before_expressions": None if any(bone.native_pose for bone in model.bones) else rest_bounds(model.bones),
        "rest_host_pose_source": "installed_MC1.21.1_layer_storePose" if any(bone.native_pose for bone in model.bones) else "MC1.21.1_numeric_rest_pose",
        "modifications": model.modifications,
        "original_visual_additions": model.visual_additions,
        "used_sources": sorted(model.used_sources),
        "native_cube_retention": [bone.name for bone in model.bones if bone.retain_vanilla_cubes],
        "validation_status": "converted_and_hash_checked; requires_compile_and_native_client_review",
    }


def main() -> int:
    parser = argparse.ArgumentParser(description=__doc__, formatter_class=argparse.RawDescriptionHelpFormatter)
    parser.add_argument("--source-root", type=Path, required=True, help="Extracted audited resource pack; never bundled")
    parser.add_argument("--audit", type=Path, required=True, help="Explicit per-file/per-model reuse audit")
    parser.add_argument("--output-dir", type=Path, default=JAVA_DIR)
    parser.add_argument("--manifest", type=Path, default=PROJECT / "design/cem-model-imports.json")
    parser.add_argument("--ids", help="Optional comma-separated runtime identifiers (with or without namespace)")
    parser.add_argument("--skip-unsupported", action="store_true", help="Export supported bundles and record explicit unsupported reasons")
    args = parser.parse_args()
    audit = json.loads(args.audit.read_text())
    if "bundles" in audit:
        audit = dict(audit)
        audit["models"] = [dict(entry, runtime_id="dungeonsalive:" + entry["catalog_id"].split(":", 1)[-1])
                           for entry in audit["bundles"]]
        audit["source_project"] = audit["source"]
        audit["credits"] = audit["attribution_credits"]
        audit.setdefault("origin_review", "See per-bundle audit decisions; only explicitly listed source files are used.")
    converter = Converter(args.source_root, audit)
    selected = None if not args.ids else {identifier.split(":", 1)[-1] for identifier in args.ids.split(",")}
    entries = [entry for entry in audit["models"] if selected is None or entry["runtime_id"].split(":", 1)[-1] in selected]
    if not entries:
        raise ConversionError("No explicitly approved models selected")
    # Complete validation before touching generated outputs.
    models = []
    unsupported = []
    for entry in entries:
        try:
            model = converter.convert(entry)
            # Validate every custom face/atlas coordinate before generating files.
            for bone in model.bones:
                for box in bone.boxes:
                    if custom_box(box, model):
                        box_quads(box, model)
            models.append(model)
        except ConversionError as error:
            if not args.skip_unsupported:
                raise
            unsupported.append({"runtime_id": entry["runtime_id"], "source_file": entry["model_file"], "reason": str(error)})
    for model in models:
        emit_java(model, args.output_dir, audit)
        converter.write_atlas(model)
        converter.write_materials(model)
    manifest = {
        "schema_version": 1,
        "generator": "tools/convert_cem_models.py",
        "target": {"minecraft": "1.21.1", "neoforge": "21.1.265", "java": 21},
        "source_project": audit["source_project"],
        "source_url": audit["source_url"],
        "source_version": audit["version"],
        "source_version_id": audit["version_id"],
        "source_archive_sha256": audit["archive_sha256"],
        "asset_license": LICENSE,
        "credits": audit["credits"],
        "origin_review": audit["origin_review"],
        "no_original_game_keyframes_claimed": True,
        "texture_assets_copied_by_this_tool": any(model.atlas or any(layer["file"] for layer in model.materials) for model in models),
        "transform_contract": {
            "units": "model pixels; Minecraft ModelPart rotation radians",
            "hierarchy": "vanilla host hierarchy -> original top CEM node -> original child nodes",
            "invert_axis": "negate matching translation/rotation axis; box origin becomes -origin-size",
            "rotation_order": "T * Rz * Ry * Rx * S",
            "scale": "static runtime bone scale; reapplied after every resetPose, never baked into coordinates",
            "resolver": "host aliases precede unqualified custom IDs; EMF_<id> explicitly resolves the custom node, including lexical this expansion",
        },
        "models": [manifest_entry(model, args.output_dir, audit) for model in models],
        "unsupported": unsupported,
        "verified_source_files": [converter.files[path] for path in sorted(converter.verified)],
    }
    args.manifest.parent.mkdir(parents=True, exist_ok=True)
    args.manifest.write_text(json.dumps(manifest, ensure_ascii=False, indent=2) + "\n")
    for model in models:
        print(f"{model.runtime_id}: {len(model.bones)} rig nodes, "
              f"{sum(len(b.boxes) for b in model.bones)} boxes, "
              f"{len(model.expressions)} ordered expressions -> {model.class_name}")
    print(f"Manifest: {args.manifest}")
    for item in unsupported:
        print(f"UNSUPPORTED {item['runtime_id']}: {item['reason']}")
    return 0


if __name__ == "__main__":
    try:
        raise SystemExit(main())
    except (ConversionError, OSError, json.JSONDecodeError) as error:
        print(f"CEM conversion stopped: {error}", file=sys.stderr)
        raise SystemExit(2)
