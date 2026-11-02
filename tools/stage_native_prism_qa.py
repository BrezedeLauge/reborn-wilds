#!/usr/bin/env python3
"""Prepare one NEW, disposable Prism instance for the native twelve-scene capture.

Requires a current compiled/JUnit-tested artifact and a matching successful native
server report. Copies only version/Java settings and an untouched marked generated
world. Never reads accounts, copies played saves, replaces an instance, or launches
Prism/Minecraft. Without --stage this performs a write-free validation.
"""
from __future__ import annotations

import argparse
import configparser
from datetime import datetime, timezone
import io
import json
from pathlib import Path
import shutil
import sys
import tempfile
import uuid

sys.dont_write_bytecode = True
import stage_living_prism_test as shared

ROOT = Path(__file__).resolve().parents[1]
TARGET = Path("run-prism/instances/Dungeons-Alive-Native-QA")
NAME = "Dungeons Alive – Native QA (Aufnahmen)"
WORLD = "Dungeons Alive Living Showcase"
TOKEN = "isolated-dungeonsalive-native-prism-qa-v1"
RUN_MARKER = "dungeonsalive-native-prism-qa.txt"
FRESH_MARKER = "dungeonsalive-native-prism-qa.fresh"
JAVA_ARGUMENTS = "-Ddungeonsalive.livingShowcase=true -Ddungeonsalive.isolatedPrismQa=true"
OPTIONS = """version:3955
renderDistance:6
simulationDistance:5
guiScale:2
lang:de_de
soundCategory_master:0.0
maxFps:30
onboardAccessibility:false
tutorialStep:none
pauseOnLostFocus:false
"""


def preflight(root: Path, world: Path, java_report: Path, native_report: Path) -> dict:
    target = shared.safe_path(root, root / TARGET)
    if target.exists():
        raise ValueError("Native QA destination already exists; no existing instance is ever replaced")
    for sentinel in ("AGENTS.md", "gradle.properties"):
        shared.read_file(root, Path(sentinel))
    world = shared.safe_path(root, world)
    if world.parent != root / "build/living-showcase-worlds" or not world.name.startswith("world-"):
        raise ValueError("Native QA source must be a freshly generated living-showcase world")
    if not world.is_dir() or {path.name for path in world.iterdir()} != shared.WORLD_FILES:
        raise ValueError("Native QA source must contain exactly the three fresh generated files, without played terrain")
    world_files = {name: shared.read_file(root, world / name) for name in shared.WORLD_FILES}
    if world_files[shared.SOURCE_MARKER] != (shared.SOURCE_TOKEN + "\n").encode():
        raise ValueError("Fresh source marker does not match")
    preparation = json.loads(world_files["preparation.json"])
    if (preparation.get("kind") != "fresh_isolated_developer_world" or preparation.get("preset") != "dungeonsalive:living_world"
            or preparation.get("existing_save_read_or_modified") is not False
            or preparation.get("native_decode_generation_and_rendering_executed") is not False
            or preparation.get("experimental_development_preset_acknowledged") is not True):
        raise ValueError("Unexpected fresh source preparation metadata")
    preset = json.loads(shared.read_file(root, Path("src/main/resources/data/dungeonsalive/worldgen/world_preset/living_world.json")))
    biome_source = preset["dimensions"]["minecraft:overworld"]["generator"]["biome_source"]
    if (biome_source.get("type") != "dungeonsalive:living_overworld"
            or any(biome_source.get(name) != "dungeonsalive:" + name
                   for name in ("amber_grove", "luminous_marsh", "ember_crags", "blighted_thicket"))):
        raise ValueError("Native QA preset must include all four implemented biomes")
    # Reuse the complete typed fresh-NBT/preset validation, but preserve the original
    # bytes and original level name for this distinct QA profile and Java guard.
    shared.renamed_fresh_level(world_files["level.dat"], preset)
    reports, evidence = {}, {}
    for name, path in (("java", java_report), ("native", native_report)):
        data = shared.read_file(root, path)
        reports[name] = json.loads(data)
        evidence[name] = {"path": str(shared.safe_path(root, path)), "sha256": shared.digest(data)}
    jar_path, jar = shared.current_artifact(root, reports["java"])
    sha = shared.digest(jar)
    native = reports["native"]
    if (native.get("status") != "passed" or native.get("exit_code") != 0
            or native.get("game_test_server_started") is not True or native.get("completed_test_count", 0) <= 0
            or native.get("required_failures") != 0 or native.get("optional_failures") != 0
            or native.get("failure_messages") != [] or native.get("artifact", {}).get("sha256") != sha):
        raise ValueError("Native GameTests must have passed on the identical current artifact SHA")
    source = root / shared.SOURCE_INSTANCE
    pack_bytes = shared.read_file(root, source / "mmc-pack.json")
    pack = json.loads(pack_bytes)
    if (pack.get("formatVersion") != 1 or len(pack["components"]) != 3
            or {item["uid"]: item["version"] for item in pack["components"]}
            != {"org.lwjgl3": "3.3.3", "net.minecraft": "1.21.1", "net.neoforged": "21.1.265"}):
        raise ValueError("Unexpected Prism versions")
    settings_bytes = shared.read_file(root, source / "instance.cfg")
    settings = configparser.ConfigParser(interpolation=None)
    settings.optionxform = str
    settings.read_string(settings_bytes.decode())
    general = settings["General"]
    if general.get("JavaVersion", "").split(".")[0] != "21" or general.get("OverrideJavaLocation") != "true":
        raise ValueError("Native QA requires the existing explicit Java 21 location")
    config = configparser.ConfigParser(interpolation=None)
    config.optionxform = str
    config["General"] = {key: general[key] for key in shared.JAVA_KEYS if key in general}
    config["General"].update({"ConfigVersion": "1.3", "InstanceType": "OneSix", "name": NAME,
                              "uuid": uuid.uuid4().hex, "iconKey": "default", "OverrideMemory": "true",
                              "MinMemAlloc": "512", "MaxMemAlloc": "4096", "OverrideMiscellaneous": "true",
                              "OverrideJavaArgs": "true", "JvmArgs": JAVA_ARGUMENTS,
                              "JoinServerOnLaunch": "true", "JoinServerOnLaunchAddress": "", "JoinWorldOnLaunch": WORLD,
                              "OverrideWindow": "true", "LaunchMaximized": "false", "MinecraftWinWidth": "1600", "MinecraftWinHeight": "1000"})
    output = io.StringIO()
    config.write(output, space_around_delimiters=False)
    return {"target": target, "world": world, "world_files": world_files, "jar_path": jar_path, "jar": jar, "sha256": sha,
            "pack": pack_bytes, "config": output.getvalue().encode(), "evidence": evidence,
            "source_settings_hashes": {"mmc-pack.json": shared.digest(pack_bytes), "instance.cfg": shared.digest(settings_bytes)}}


def stage(root: Path, plan: dict, revalidate) -> None:
    target = plan["target"]
    shared.safe_path(root, target.parent).mkdir(parents=True, exist_ok=True)
    temporary = Path(tempfile.mkdtemp(prefix=".native-prism-qa-", dir=target.parent))
    try:
        run = temporary / "minecraft"
        world = run / "saves" / WORLD
        world.mkdir(parents=True)
        for name, data in plan["world_files"].items():
            (world / name).write_bytes(data)
        (world / FRESH_MARKER).write_text(TOKEN + "\n")
        (run / RUN_MARKER).write_text(TOKEN + "\n")
        (temporary / "dungeonsalive-native-prism-qa-instance.txt").write_text(TOKEN + "\n")
        (run / "mods").mkdir()
        (run / "mods" / plan["jar_path"].name).write_bytes(plan["jar"])
        (run / "options.txt").write_text(OPTIONS)
        (temporary / "instance.cfg").write_bytes(plan["config"])
        (temporary / "mmc-pack.json").write_bytes(plan["pack"])
        manifest = {"kind": TOKEN, "created_at_utc": datetime.now(timezone.utc).isoformat(), "instance_name": NAME,
                    "quick_play_world": WORLD, "world_source": str(plan["world"]),
                    "source_level_sha256": shared.digest(plan["world_files"]["level.dat"]), "level_bytes_changed": False,
                    "artifact": {"path": str(plan["jar_path"]), "sha256": plan["sha256"]}, "evidence": plan["evidence"],
                    "source_settings_hashes": plan["source_settings_hashes"], "capture_status": "not_started", "visual_review_performed": False,
                    "launcher_opened": False, "source_world_modified": False, "old_instance_modified": False,
                    "scope": "Disposable twelve-scene native capture; three boss-site views are staged templates. Capture is not visual approval.",
                    "java_flags": JAVA_ARGUMENTS.split(), "capture_output": "minecraft/captures/manifest.json"}
        (temporary / "staging.json").write_text(json.dumps(manifest, ensure_ascii=False, indent=2) + "\n")
        fresh = revalidate()
        if (fresh["sha256"] != plan["sha256"] or fresh["evidence"] != plan["evidence"]
                or fresh["world_files"] != plan["world_files"] or fresh["source_settings_hashes"] != plan["source_settings_hashes"]):
            raise ValueError("Native QA staging inputs changed during the copy")
        shared.publish_new_directory(temporary, target)
    finally:
        if temporary.exists():
            shutil.rmtree(temporary)


def main() -> int:
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--world", type=Path, required=True)
    parser.add_argument("--java-report", type=Path, default=Path("reports/local-java-check.json"))
    parser.add_argument("--native-report", type=Path, required=True)
    parser.add_argument("--stage", action="store_true", help="Create only the new disposable native QA instance; never starts a client")
    args = parser.parse_args()
    try:
        validate = lambda: preflight(ROOT, args.world, args.java_report, args.native_report)
        plan = validate()
        if args.stage:
            stage(ROOT, plan, validate)
        print(json.dumps({"status": "staged" if args.stage else "validated_without_writing", "target": str(plan["target"]),
                          "instance_name": NAME, "quick_play_world": WORLD, "artifact_sha256": plan["sha256"],
                          "capture_status": "not_started", "visual_review_performed": False, "launcher_opened": False}, ensure_ascii=False, indent=2))
        return 0
    except (ValueError, OSError, KeyError, TypeError, configparser.Error) as error:
        print(f"Refusing native Prism QA staging: {error}", file=sys.stderr)
        return 1


if __name__ == "__main__":
    raise SystemExit(main())
