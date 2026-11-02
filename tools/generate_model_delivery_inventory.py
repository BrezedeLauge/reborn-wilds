#!/usr/bin/env python3
"""Record actual delivered model art and its pinned provenance, without building the mod.

Run after the last model/texture/manifest change:
    python3 tools/generate_model_delivery_inventory.py

This uses the checked-in audits/manifests and actual file bytes, not local research clones.
It fails on unidentified third-party snapshots or textures instead of guessing their origin.
An inventory entry never certifies registration, playability, or graphical acceptance.
"""
from __future__ import annotations

from collections import Counter, defaultdict
import hashlib
import json
from pathlib import Path
import re

ROOT = Path(__file__).resolve().parents[1]
JAVA = ROOT / "src/main/java/de/laugenbreze/dungeonsalive/client/entity/reference/imported"
TEX = ROOT / "src/main/resources/assets/dungeonsalive/textures/entity/reference"
OUT = ROOT / "design/model-delivery-inventory.json"


def read(relative: str):
    return json.loads((ROOT / relative).read_text(encoding="utf-8"))


def digest(path: Path) -> str:
    return hashlib.sha256(path.read_bytes()).hexdigest()


def file_info(path: Path) -> dict:
    return {"file": path.relative_to(ROOT).as_posix(), "sha256": digest(path), "bytes": path.stat().st_size}


def strings(value):
    if isinstance(value, str):
        yield value
    elif isinstance(value, dict):
        for child in value.values():
            yield from strings(child)
    elif isinstance(value, list):
        for child in value:
            yield from strings(child)


def main() -> None:
    inputs = ["design/reference-models.json", "design/model-source-audit.json",
              "design/bedrock-model-imports.json", "design/bedrock-conversion-report.json",
              "design/cem-model-imports.json", "design/cem-extra-model-imports.json",
              "design/infamous-additions.json", "design/infamous-bindings.json",
              "design/infamous-conversion-report.json"]
    if (ROOT / "design/v04-source-model-additions.json").is_file():
        inputs.append("design/v04-source-model-additions.json")
    docs = {name: read(name) for name in inputs}
    ref = docs[inputs[0]]
    audit = docs[inputs[1]]
    alias = {"recrafted": "recrafted_mobs"}
    audit_sources = {alias.get(s["id"], s["id"]): s for s in audit["sources"]}
    sources = {}
    for sid, source in ref["sources"].items():
        a = audit_sources.get(sid, {})
        sources[sid] = {"name": source["name"], "license": source["license"],
                        "license_file": source["license_path"], "revision": source.get("commit"),
                        "copyright": source["copyright"], "project_url": a.get("url", source.get("repository")),
                        "credits": a.get("credits", [source["copyright"]])}
        for key in ["archive_url", "archive_sha256", "version", "version_id", "author_page", "license_url"]:
            if key in a:
                sources[sid][key] = a[key]
        if sid == "original":
            sources[sid]["credits"] = ["LaugenBreze / Dungeons Alive contributors"]

    by_hash = defaultdict(list)
    by_source_path = {}
    for f in audit["source_files"]:
        sid = alias.get(f["source_project_id"], f["source_project_id"])
        source = sources[sid]
        upstream = {"source_id": sid, "upstream_path": f["path"], "sha256": f["sha256"]}
        if source.get("archive_url"):
            upstream.update(archive_url=source["archive_url"], archive_sha256=source["archive_sha256"],
                            archive_member=f["path"])
        else:
            upstream["source_url"] = f.get("source_url") or f.get("url") or (
                source["project_url"].rstrip("/") + "/blob/" + source["revision"] + "/" + f["path"])
        by_hash[f["sha256"]].append(upstream)
        by_source_path[(sid, f["path"])] = upstream

    def matching(path: Path, sid: str) -> list:
        return [x for x in by_hash.get(digest(path), []) if x["source_id"] == sid]

    def source_file(sid: str, relative: str) -> dict:
        result = by_source_path.get((sid, relative))
        if result is None:
            raise ValueError(f"Unaudited source dependency: {sid}:{relative}")
        return result

    # Actual retained snapshots, including files used as behavior/binding references.
    snapshot_files = []
    for sid in ["goety", "dungeons_mobs", "dungeons_mobs_port", "recrafted_mobs"]:
        base = ROOT / "third_party" / sid
        for path in sorted(base.rglob("*")):
            if not path.is_file():
                continue
            rel = path.relative_to(base).as_posix()
            selected = rel.startswith(("source_models/", "source_animations/", "snapshots/", "source/assets/"))
            if not selected:
                continue
            matches = matching(path, sid)
            if not matches:
                raise ValueError(f"Changed or unaudited source snapshot: {path.relative_to(ROOT)}")
            snapshot_files.append(file_info(path) | {"source_id": sid, "license": sources[sid]["license"],
                                  "modification": "unchanged_source_bytes", "upstream_matches": matches,
                                  "usage": "selected source art or binding reference; not an extra runtime registry claim"})

    # Lossless atlases explicitly describe each input rather than relying on a filename.
    atlas_by_file = {}
    for name in ["design/cem-model-imports.json", "design/cem-extra-model-imports.json"]:
        for model in docs[name]["models"]:
            atlas = model.get("generated_atlas")
            if not atlas:
                continue
            files = [atlas["file"]]
            if atlas.get("emissive_resource"):
                files.append("src/main/resources/assets/" + atlas["emissive_resource"].replace(":", "/"))
            for target in files:
                glow = target.endswith("_e.png")
                dependencies = []
                own_tiles = {name for addition in model.get("original_visual_additions", [])
                             for name in addition.get("generated_textures", [])}
                for tile in atlas["tiles"]:
                    selected = tile.get("emissive_source") if glow else tile["source"]
                    if selected:
                        if selected.startswith("dungeonsalive-generated:"):
                            if selected not in own_tiles:
                                raise ValueError(f"Undeclared own atlas tile: {selected}")
                            dependencies.append({"source_id": "original", "generated_resource": selected,
                                "generator": "tools/convert_cem_models.py",
                                "generator_sha256": digest(ROOT / "tools/convert_cem_models.py"),
                                "modification": "independently authored procedural tile; enclosing adapted atlas retains CC-BY-NC-SA-4.0"})
                        else:
                            dependencies.append(source_file("recrafted_mobs", selected))
                atlas_by_file[target] = {"source_id": "recrafted_mobs", "modification": "lossless_atlas_with_remapped_uv",
                                        "changes": atlas["changes"], "upstream_sources": dependencies,
                                        "atlas_layout": atlas, "conversion_manifest": name}
    for name in ["design/bedrock-model-imports.json", "design/infamous-additions.json"]:
        for asset in docs[name].get("assets", []):
            equipment = asset.get("equipment")
            if equipment:
                atlas_by_file[equipment["atlas_file"]] = {"source_id": asset["source"],
                    "modification": "lossless_costume_atlas_with_bound_armor_geometry", "changes": equipment["changes"],
                    "source_materials": equipment["materials"], "conversion_manifest": name}
        for atlas in docs[name].get("texture_atlases", []):
            atlas_by_file[atlas["file"]] = {"source_id": "dungeons_mobs", "modification": "lossless_atlas_with_remapped_uv",
                "changes": atlas["changes"], "source_materials": atlas["materials"], "conversion_manifest": name}
    for info in atlas_by_file.values():
        if "source_materials" in info:
            info["upstream_sources"] = []
            for material in info["source_materials"]:
                path = ROOT / material["snapshot"]
                if digest(path) != material["sha256"]:
                    raise ValueError(f"Changed atlas source snapshot: {path}")
                info["upstream_sources"].extend(matching(path, info["source_id"]))

    references = defaultdict(set)
    for model in ref["models"]:
        for value in strings(model):
            if value.startswith("dungeonsalive:textures/"):
                references[value].add(model["id"])
        for overlay in model.get("animated_overlays", []):
            for frame in range(overlay["frames"]):
                references[overlay["prefix"] + str(frame) + ".png"].add(model["id"])

    textures = []
    folders = {"infamous": "dungeons_mobs", "cem": "recrafted_mobs"}
    for path in sorted(TEX.rglob("*.png")):
        rel = path.relative_to(ROOT).as_posix()
        sid = folders.get(path.relative_to(TEX).parts[0], path.relative_to(TEX).parts[0])
        record = file_info(path)
        record.update(source_id=sid, license=sources[sid]["license"], credits=sources[sid]["credits"])
        resource = "dungeonsalive:" + path.relative_to(ROOT / "src/main/resources/assets/dungeonsalive").as_posix()
        record.update(resource=resource, referenced_by_model_ids=sorted(references[resource]))
        matches = matching(path, sid)
        if matches:
            record.update(modification="unchanged_source_bytes", upstream_sources=matches)
        elif rel in atlas_by_file:
            record.update(atlas_by_file[rel])
        elif path.name == "treetop_whisperer_adapted.png" and sid == "goety":
            original = next(x for x in ref["models"] if x["id"] == "whisperer")["texture"]
            source_path = ROOT / "src/main/resources/assets" / original.replace(":", "/")
            record.update(modification="documented_hsv_palette_derivative", generator="tools/generate_treetop_palette.py",
                          upstream_sources=matching(source_path, "goety"),
                          changes="Darker greens/outer surface and purple inner palette; source rig retained, runtime size multiplied by 1.5. Exact formula lives in the generator.")
        elif sid == "original" and path.name in ["vengeful_heart.png", "vengeful_heart_glow.png"]:
            record.update(modification="independently_authored_pixel_atlas", generator="tools/generate_original_vengeful.py",
                          upstream_sources=[], changes="Own procedural stone/crack/flame pixels, disjoint face UVs; reference images were viewed but not used as texture inputs.")
        else:
            raise ValueError(f"Unidentified delivered texture: {rel}")
        textures.append(record)

    # Every generated or hand-maintained model/clip gets its own license boundary.
    definitions = {a["key"]: a for a in ref["assets"]}
    for a in docs["design/infamous-bindings.json"]["asset_definitions"]:
        definitions.setdefault(a["key"], a)
    builtin_geometry_classes = set()
    for name in ["design/cem-model-imports.json", "design/cem-extra-model-imports.json"]:
        for model in docs[name]["models"]:
            key = model["class_name"].removesuffix("Geometry")
            sid = "recrafted_mobs" if model.get("source_file") else "original"
            definition = {"key": key, "source": sid, "geometry_class": model["class_name"], "credits": sources[sid]["credits"]}
            if model.get("source_file"):
                definition["model_snapshot"] = "third_party/recrafted_mobs/source/" + model["source_file"]
            else:
                builtin_geometry_classes.add(model["class_name"])
            definitions.setdefault(key, definition)
    detailed_snapshots = defaultdict(set)
    for name in ["design/bedrock-model-imports.json", "design/infamous-additions.json"]:
        for a in docs[name].get("assets", []):
            detailed_snapshots[a["geometry_class"]].add(a["model_snapshot"])
            if a.get("animation_snapshot"):
                detailed_snapshots[a["animation_class"]].add(a["animation_snapshot"])
            for layer in a.get("equipment", {}).get("attachments", []):
                detailed_snapshots[a["geometry_class"]].add(layer["snapshot"])
    for name in ["design/cem-model-imports.json", "design/cem-extra-model-imports.json"]:
        for model in docs[name]["models"]:
            if model.get("source_file"):
                detailed_snapshots[model["class_name"]].add("third_party/recrafted_mobs/source/" + model["source_file"])
    classes = defaultdict(list)
    for asset in definitions.values():
        classes[asset["geometry_class"]].append(asset)
        for owner in set(asset.get("clip_owners", {}).values()):
            classes[owner].append(asset)
    for model in ref["models"]:
        for owner in set(model.get("additional_clip_owners", {}).values()):
            classes[owner].append(definitions[model["asset"]])
    all_model_ids = defaultdict(list)
    for model in ref["models"]:
        all_model_ids[model["asset"]].append(model["id"])
    converted_sources = []
    independent_animation_classes = set(docs.get("design/v04-source-model-additions.json", {})
                                        .get("independent_animation_classes", []))
    for path in sorted(JAVA.glob("*.java")):
        selected = classes.get(path.stem, [])
        if not selected and path.stem not in ["ReferenceMeshCube", "ReferenceModelIndex"]:
            raise ValueError(f"Unmapped delivered model class: {path.name}")
        source_ids = sorted(set(a["source"] for a in selected)) or ["original"]
        if path.stem in builtin_geometry_classes:
            source_ids = ["original"]
        licenses = sorted(set(sources[sid]["license"] for sid in source_ids))
        upstream_snapshots = list(detailed_snapshots[path.stem])
        for asset in selected:
            if asset.get("model_snapshot"):
                upstream_snapshots.append(asset["model_snapshot"])
            upstream_snapshots.extend(a["snapshot"] for a in asset.get("animation_sources", []))
            upstream_snapshots.extend(a["snapshot"] for a in asset.get("helper_methods", []))
        record = file_info(path) | {"source_ids": source_ids, "licenses": licenses,
            "asset_keys": sorted(set(a["key"] for a in selected)),
            "credits": sorted(set(c for a in selected for c in a.get("credits", []))) or sources["original"]["credits"],
            "source_snapshot_paths": sorted(set(upstream_snapshots)),
            "referenced_by_model_ids": sorted(set(m for a in selected for m in all_model_ids[a["key"]])),
            "modification": "native_java_adaptation_or_source_clip_conversion" if selected else "independent_shared_runtime_support"}
        if "recrafted_mobs" in source_ids:
            record["license_scope"] = "Adapted artistic geometry/UV/expressions retain CC-BY-NC-SA-4.0 even when represented in a Java source file. Independent renderer/converter code is separately MIT."
            record["conversion_manifests"] = ["design/cem-model-imports.json", "design/cem-extra-model-imports.json"]
        if path.stem.startswith("Infamous"):
            record["conversion_manifests"] = ["design/infamous-additions.json", "design/infamous-conversion-report.json"]
            record["modification_details"] = "Native wrappers call installed Minecraft factories; selected sack/coral/helmet/shell/armor parts follow their proper bones. Piglin and Icy Creeper action clips are own additions, not original Dungeons keyframes."
        if path.stem.startswith("Bedrock"):
            record["conversion_manifests"] = ["design/bedrock-model-imports.json", "design/bedrock-conversion-report.json"]
        if path.stem.startswith("OriginalVengeful"):
            record.update(modification="independently_authored_geometry_or_animation", generator="tools/generate_original_vengeful.py",
                          source_snapshot_paths=[], reference_images_are_not_texture_inputs=True)
        if path.stem in independent_animation_classes:
            record.update(source_ids=["original"], licenses=["MIT"], credits=sources["original"]["credits"],
                          source_snapshot_paths=[], modification="independently_authored_animation_on_attributed_rig",
                          provenance_manifest="design/v04-source-model-additions.json",
                          license_scope="These independently authored timing/pose clips are MIT. Their separately inventoried community model geometry and skin retain their own source licenses.")
        if path.stem in builtin_geometry_classes:
            record.update(modification="own_wrapper_using_installed_minecraft_factory",
                          credits=sources["original"]["credits"], source_snapshot_paths=[],
                          license_scope="This independent geometry wrapper is MIT. Its separately inventoried ReCrafted skin remains CC-BY-NC-SA-4.0; installed Minecraft game files are not copied.")
        converted_sources.append(record)

    notices = [ROOT / "LICENSE", ROOT / "THIRD_PARTY_NOTICES.md"]
    notices.extend(sorted((ROOT / "third_party").glob("*/LICENSE.txt")))
    notices.extend([ROOT / "third_party/recrafted_mobs/UPSTREAM-LICENSE-NOTICE.txt",
                    ROOT / "third_party/recrafted_mobs/source/CREDITS.md"])
    builtin = sorted(set(value for doc in docs.values() for value in strings(doc)
                         if value.startswith("minecraft:") and " " not in value))
    result = {"schema_version": 1, "generator": "tools/generate_model_delivery_inventory.py",
        "refresh_instruction": "Run again after the final model/texture/manifest merge and before the release build. Hashes describe this file snapshot, not future edits.",
        "scope": "Actual retained source snapshots, delivered reference-model textures and native model/clip Java sources. This is an attribution/integrity inventory, not a count of finished original mobs.",
        "sources": sources, "input_manifests": [file_info(ROOT / name) for name in inputs],
        "license_and_notice_files": [file_info(path) for path in notices],
        "source_snapshots": snapshot_files, "delivered_textures": textures, "model_and_animation_sources": converted_sources,
        "minecraft_resource_references_only": builtin,
        "minecraft_reference_notice": "Installed Minecraft geometry factories/resources are used at runtime; Mojang source archives and game PNGs are not redistributed. Source files under ReCrafted's assets/minecraft namespace are the specifically audited community-authored pack art, not copied vanilla game files.",
        "counts": {"source_snapshots": len(snapshot_files), "delivered_textures": len(textures),
                   "model_and_animation_java_files": len(converted_sources), "currently_indexed_model_bindings": len(ref["models"]),
                   "texture_license_counts": dict(Counter(t["license"] for t in textures))},
        "acceptance_notice": "A referenced binding or generated class is not an acceptance claim. Read actual client/server validation reports for registration, rendering, combat and quality status."}
    OUT.write_text(json.dumps(result, ensure_ascii=False, indent=2) + "\n", encoding="utf-8")
    print(json.dumps(result["counts"], indent=2))
    print("Wrote", OUT.relative_to(ROOT))


if __name__ == "__main__":
    main()
