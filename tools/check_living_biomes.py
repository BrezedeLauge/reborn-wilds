#!/usr/bin/env python3
"""Read-only reference, bounded-placement and feature-order audit using installed 1.21.1 data.

The graph follows the (step, placed-feature) precedence used by Minecraft FeatureSorter.
It cannot prove that registry codecs load or that generated terrain looks good in the client.
"""
from __future__ import annotations

import argparse
import hashlib
import json
from collections import defaultdict
from pathlib import Path
from zipfile import ZipFile

from generate_living_biomes import BIOMES, MOD, ROOT, outputs, vanilla_references


def audit(jar):
    generated = outputs()
    references = vanilla_references(generated, jar)
    registries = defaultdict(dict)
    kinds = ("worldgen/biome", "worldgen/placed_feature", "worldgen/configured_feature", "tags/worldgen/biome")
    with ZipFile(jar) as archive:
        for path in archive.namelist():
            for kind in kinds:
                prefix = "data/minecraft/" + kind + "/"
                if path.startswith(prefix) and path.endswith(".json"):
                    registries[kind]["minecraft:" + path[len(prefix):-5]] = json.loads(archive.read(path))
    for namespace in (ROOT / "src/main/resources/data").iterdir():
        if not namespace.is_dir(): continue
        for kind in kinds:
            for path in (namespace / kind).rglob("*.json"):
                key = namespace.name + ":" + path.relative_to(namespace / kind).as_posix()[:-5]
                value = json.loads(path.read_text())
                if kind.startswith("tags/") and not value.get("replace") and key in registries[kind]:
                    value = {"values": registries[kind][key]["values"] + value["values"]}
                registries[kind][key] = value

    def require(kind, key):
        if key not in registries[kind]: raise ValueError(f"Unknown {kind} reference: {key}")

    def biome_set(selector, seen=frozenset()):
        if isinstance(selector, list): return set().union(*(biome_set(v, seen) for v in selector))
        if not selector.startswith("#"):
            require("worldgen/biome", selector)
            return {selector}
        key = selector[1:]
        if key in seen: raise ValueError("Cyclic biome tag: " + key)
        require("tags/worldgen/biome", key)
        result = set()
        for entry in registries["tags/worldgen/biome"][key]["values"]:
            if isinstance(entry, dict):
                identifier = entry.get("id")
                if not isinstance(identifier, str) or set(entry) - {"id", "required"}:
                    raise ValueError("Malformed biome-tag entry: " + str(entry))
                kind = "tags/worldgen/biome" if identifier.startswith("#") else "worldgen/biome"
                if not entry.get("required", True) and identifier.removeprefix("#") not in registries[kind]: continue
                entry = identifier
            if not isinstance(entry, str): raise ValueError("Malformed biome-tag entry: " + str(entry))
            result.update(biome_set(entry, seen | {key}))
        return result

    modified = set()
    feature_step_names = ["raw_generation", "lakes", "local_modifications", "underground_structures", "surface_structures",
                          "strongholds", "underground_ores", "underground_decoration", "fluid_springs", "vegetal_decoration", "top_layer_modification"]
    modifier_paths = sorted((ROOT / "src/main/resources/data").glob("*/neoforge/biome_modifier/*.json"))
    for path in modifier_paths:
        modifier = json.loads(path.read_text())
        if modifier.get("type") != "neoforge:add_features": continue
        step = feature_step_names.index(modifier["step"])
        feature_ids = modifier["features"] if isinstance(modifier["features"], list) else [modifier["features"]]
        for key in feature_ids: require("worldgen/placed_feature", key)
        for key in biome_set(modifier["biomes"]):
            steps = registries["worldgen/biome"][key]["features"]
            while len(steps) <= step: steps.append([])
            steps[step].extend(feature_ids)
            modified.add(key)

    graph = defaultdict(set)
    source = defaultdict(set)
    for biome, spec in registries["worldgen/biome"].items():
        ordered = [(step, feature) for step, features in enumerate(spec["features"]) for feature in features]
        for step, feature in ordered:
            require("worldgen/placed_feature", feature)
            source[(step, feature)].add(biome)
            graph[(step, feature)]
        for before, after in zip(ordered, ordered[1:]): graph[before].add(after)
    active, finished = [], set()
    def visit(node):
        if node in finished: return
        if node in active:
            cycle = active[active.index(node):] + [node]
            raise ValueError("Feature order cycle: " + " -> ".join(f"{step}:{feature}" for step, feature in cycle))
        active.append(node)
        for child in sorted(graph[node]): visit(child)
        active.pop()
        finished.add(node)
    for node in sorted(graph): visit(node)

    block_states = set()
    configured_count = placed_count = 0
    def walk(value):
        if isinstance(value, list):
            for child in value: walk(child)
        elif isinstance(value, dict):
            kind = value.get("type")
            if "Name" in value: block_states.add(value["Name"])
            if kind == "minecraft:count" and not (isinstance(value["count"], int) and 1 <= value["count"] <= 3):
                raise ValueError("Unbounded outer feature count")
            if kind == "minecraft:random_patch" and (value["config"]["tries"] > 12 or value["config"]["xz_spread"] > 3):
                raise ValueError("Flower patch exceeds local decoration budget")
            if kind == "minecraft:disk" and (value["config"]["radius"]["max_inclusive"] > 2 or value["config"]["half_height"] != 0):
                raise ValueError("Ground patch exceeds one-layer / two-block-radius budget")
            if kind == "minecraft:block_column":
                config = value["config"]
                if config["direction"] not in ("east", "south") or sum(layer["height"]["max_inclusive"] for layer in config["layers"]) > 4:
                    raise ValueError("Deadwood exceeds four horizontal blocks")
                if config["allowed_placement"]["type"] != "minecraft:all_of": raise ValueError("Deadwood has lost its air-and-soil gate")
            if kind == "minecraft:multiface_growth" and value["config"]["search_range"] > 4:
                raise ValueError("Lichen search exceeds local radius")
            if "feature" in value and isinstance(value["feature"], str):
                require("worldgen/configured_feature" if "placement" in value else "worldgen/placed_feature", value["feature"])
            for child in value.values(): walk(child)
    for path, spec in generated.items():
        if "/worldgen/configured_feature/" in str(path): configured_count += 1
        if "/worldgen/placed_feature/" in str(path):
            placed_count += 1
            if spec["placement"][-1] != {"type": "minecraft:biome"}: raise ValueError("Missing outer biome gate: " + str(path))
        walk(spec)
    files = []
    for path in sorted(generated):
        payload = (ROOT / path).read_bytes()
        expected = (json.dumps(generated[path], indent=2, ensure_ascii=False) + "\n").encode()
        if payload != expected: raise ValueError("Stale generated resource: " + str(path))
        files.append({"path": str(path), "sha256": hashlib.sha256(payload).hexdigest()})
    return {
        "status": "passed_static_reference_bounds_and_order_audit", "minecraft_version": "1.21.1",
        "input": "Locally installed Minecraft resource JAR, read only; no archive copied into the project",
        "vanilla_registry_references": references, "biomes_in_order_graph": len(registries["worldgen/biome"]),
        "feature_order_vertices": len(graph), "feature_order_edges": sum(map(len, graph.values())),
        "feature_order_cycles": 0, "modified_biomes": sorted(modified), "custom_biomes": list(BIOMES),
        "configured_features": configured_count, "placed_features": placed_count,
        "referenced_block_states": sorted(block_states), "generated_files": files,
        "native_codec_load_executed": False, "native_world_generation_executed": False, "visual_review_executed": False,
        "limits": "This static graph/reference audit does not execute Minecraft registries, Feature.place, chunk generation or rendering.",
    }


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--minecraft-jar", type=Path, required=True)
    parser.add_argument("--report", type=Path, help="Optional JSON audit artifact; no generated resources are modified")
    args = parser.parse_args()
    report = audit(args.minecraft_jar)
    if args.report:
        args.report.parent.mkdir(parents=True, exist_ok=True)
        args.report.write_text(json.dumps(report, indent=2) + "\n")
    print(f"Passed: {report['vanilla_registry_references']} registry references, {report['biomes_in_order_graph']} biome feature lists, "
          f"{report['feature_order_vertices']} ordered features, no cycles, {report['configured_features']} bounded configured features")


if __name__ == "__main__":
    main()
