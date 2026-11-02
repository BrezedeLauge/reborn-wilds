#!/usr/bin/env python3
"""Stage a separate Prism test instance after matching Java and native QA.

Without --stage this validates and prints the plan without writing anything. Only
the two named version/Java settings files of the old instance are read. No launcher,
account, old mod, old world, or user preferences are copied or modified.
Use --visual-pending for the first native client sighting, explicitly labeled as
unreviewed, or --visual-report for a matching, actually reviewed terrain report.
"""
from __future__ import annotations

import argparse
import configparser
import ctypes
from datetime import datetime, timezone
import gzip
import hashlib
import io
import json
import os
from pathlib import Path
import shutil
import struct
import sys
import tempfile
import uuid

sys.dont_write_bytecode = True
from prepare_living_showcase import TOKEN as SOURCE_TOKEN

ROOT = Path(__file__).resolve().parents[1]
SOURCE_INSTANCE = Path("run-prism/instances/Dungeons-Alive-Creative")
TARGET_INSTANCE = Path("run-prism/instances/Dungeons-Alive-Living-World")
WORLD_NAME = "Dungeons Alive Living World"
INSTANCE_NAME = "Dungeons Alive – Lebendige Welt"
PENDING_INSTANCE_NAME = INSTANCE_NAME + " (Sichttest offen)"
TOKEN = "dungeonsalive-isolated-living-prism-test-v1"
MARKER = "dungeonsalive-living-prism-instance.txt"
SOURCE_MARKER = "dungeonsalive-living-showcase-source.txt"
WORLD_FILES = {"level.dat", SOURCE_MARKER, "preparation.json"}
JAVA_KEYS = ("JavaArchitecture", "JavaPath", "JavaRealArchitecture", "JavaSignature",
             "JavaVendor", "JavaVersion", "OverrideJavaLocation")
OPTIONS = """version:3955
renderDistance:8
simulationDistance:6
guiScale:2
lang:de_de
soundCategory_master:1.0
onboardAccessibility:false
"""


def digest(data: bytes) -> str:
    return hashlib.sha256(data).hexdigest()


def safe_path(root: Path, path: Path) -> Path:
    """Refuse every symlink component, traversal outside the project, and special files."""
    candidate = path if path.is_absolute() else root / path
    if candidate.resolve() != candidate or not candidate.is_relative_to(root):
        raise ValueError(f"Refusing a symlink, non-canonical or external path: {candidate}")
    return candidate


def read_file(root: Path, path: Path) -> bytes:
    path = safe_path(root, path)
    if not path.is_file():
        raise ValueError(f"Missing ordinary file: {path}")
    return path.read_bytes()


def renamed_fresh_level(compressed: bytes, preset: dict) -> bytes:
    """Validate the narrow fresh-world schema and replace only Data.LevelName's bytes.

    This intentionally rejects non-generated saves, duplicate tags, old/played worlds,
    unfamiliar tag types and trailing data. All other decompressed bytes survive intact.
    """
    with gzip.GzipFile(fileobj=io.BytesIO(compressed)) as source:
        raw = source.read(1_048_577)
    if len(raw) > 1_048_576:
        raise ValueError("Fresh level.dat exceeds 1 MiB")
    cursor = 0
    level_name_span = None

    def take(length):
        nonlocal cursor
        if length < 0 or cursor + length > len(raw):
            raise ValueError("Truncated fresh-world NBT")
        result = raw[cursor:cursor + length]
        cursor += length
        return result

    def string():
        return take(struct.unpack(">H", take(2))[0]).decode("utf-8")

    def payload(kind, depth, path):
        nonlocal level_name_span
        if depth > 32:
            raise ValueError("Fresh-world NBT is too deeply nested")
        formats = {1: ">b", 3: ">i", 4: ">q", 5: ">f", 6: ">d"}
        if kind in formats:
            fmt = formats[kind]
            return kind, struct.unpack(fmt, take(struct.calcsize(fmt)))[0]
        if kind == 8:
            start = cursor
            value = string()
            if path == ("Data", "LevelName"):
                level_name_span = (start, cursor)
            return kind, value
        if kind == 9:
            child = take(1)[0]
            length = struct.unpack(">i", take(4))[0]
            if not 0 <= length <= len(raw):
                raise ValueError("Invalid fresh-world list length")
            return kind, [payload(child, depth + 1, None) for _ in range(length)]
        if kind == 10:
            result = {}
            while True:
                child = take(1)[0]
                if child == 0:
                    return kind, result
                name = string()
                if name in result:
                    raise ValueError("Duplicate fresh-world NBT tag")
                result[name] = payload(child, depth + 1, None if path is None else path + (name,))
        raise ValueError(f"Unsupported fresh-world NBT type: {kind}")

    if take(1) != b"\x0a" or string() != "":
        raise ValueError("Expected the generated unnamed compound root")
    root = payload(10, 0, ())[1]
    if cursor != len(raw) or set(root) != {"Data"} or root["Data"][0] != 10:
        raise ValueError("Unexpected fresh-world root or trailing bytes")
    data = root["Data"][1]
    expected = {"DataVersion": (3, 3955), "version": (3, 19133), "GameType": (3, 1),
                "allowCommands": (1, 1), "initialized": (1, 0), "LastPlayed": (4, 0),
                "confirmedExperimentalSettings": (1, 1)}
    if any(data.get(key) != value for key, value in expected.items()) or "Player" in data:
        raise ValueError("Source is not the untouched, confirmed fresh Creative development world")
    if level_name_span is None or data.get("LevelName") != (8, "Dungeons Alive Living Showcase"):
        raise ValueError("Unexpected source world name")

    def untyped(tag):
        kind, value = tag
        if kind == 10:
            return {key: untyped(entry) for key, entry in value.items()}
        if kind == 9:
            return [untyped(entry) for entry in value]
        return value

    worldgen = data.get("WorldGenSettings")
    if not worldgen or worldgen[0] != 10 or untyped(worldgen).get("dimensions") != preset.get("dimensions"):
        raise ValueError("Fresh world's dimensions differ from the currently tested living-world preset")
    start, end = level_name_span
    name = WORLD_NAME.encode("utf-8")
    return gzip.compress(raw[:start] + struct.pack(">H", len(name)) + name + raw[end:], mtime=0)


def current_artifact(root: Path, report: dict) -> tuple[Path, bytes]:
    counts = report.get("junit", {}).get("counts", {})
    if (report.get("status") != "passed" or report.get("junit_exit_code") != 0
            or report.get("compile_main", {}).get("exit_code") != 0
            or report.get("compile_test", {}).get("exit_code") != 0
            or counts.get("succeeded", 0) <= 0 or counts.get("succeeded") != counts.get("found")
            or any(counts.get(key, -1) != 0 for key in ("failed", "aborted", "skipped", "total_failures"))):
        raise ValueError("Current Java compilation and all actual JUnit tests must have passed")
    artifact = report.get("artifact", {})
    if artifact.get("kind") != "local_direct_javac_test_artifact_not_gradle_release":
        raise ValueError("Java report must identify the separate local test artifact explicitly")
    path = safe_path(root, Path(artifact.get("path", "missing")))
    if path.parent != root / "build/local-check" or path.suffix != ".jar":
        raise ValueError("Only the separate build/local-check test artifact may be staged")
    jar = read_file(root, path)
    if digest(jar) != artifact.get("sha256"):
        raise ValueError("Artifact differs from the successful Java report")
    for key in ("sources", "resource_inputs", "packaging_inputs", "build_configuration"):
        entries = report.get(key)
        if not isinstance(entries, list) or not entries:
            raise ValueError(f"Missing verified current input inventory: {key}")
        for entry in entries:
            if digest(read_file(root, Path(entry["path"]))) != entry["sha256"]:
                raise ValueError(f"Project changed after compilation: {entry['path']}")
    for key, folders, glob in (("sources", ("src/main/java", "src/test/java"), "*.java"),
                               ("resource_inputs", ("src/main/resources", "src/test/resources"), "*")):
        current = {path.relative_to(root).as_posix() for folder in folders
                   for path in (root / folder).rglob(glob) if path.is_file()}
        if current != {entry["path"] for entry in report[key]}:
            raise ValueError(f"Project input file set changed after compilation: {key}")
    return path, jar


def preflight(root: Path, world: Path, java_report: Path, native_report: Path,
              visual_report: Path | None, *, visual_pending: bool = False) -> dict:
    if (visual_report is None) != visual_pending:
        raise ValueError("Choose exactly one explicit visual mode: a reviewed report or visual-pending")
    target = safe_path(root, root / TARGET_INSTANCE)
    if target.exists():
        raise ValueError("Destination already exists; no existing instance, marked or unmarked, is overwritten")
    world = safe_path(root, world)
    if world.parent != root / "build/living-showcase-worlds" or not world.name.startswith("world-"):
        raise ValueError("Source must be a fresh generated build/living-showcase-worlds/world-* directory")
    if not world.is_dir() or {path.name for path in world.iterdir()} != WORLD_FILES:
        raise ValueError("Source world contains unexpected files or generated terrain; create a fresh source")
    world_files = {name: read_file(root, world / name) for name in WORLD_FILES}
    if world_files[SOURCE_MARKER].decode() != SOURCE_TOKEN + "\n":
        raise ValueError("Source development-world marker is absent or wrong")
    preparation = json.loads(world_files["preparation.json"])
    if (preparation.get("kind") != "fresh_isolated_developer_world"
            or preparation.get("preset") != "dungeonsalive:living_world"
            or preparation.get("existing_save_read_or_modified") is not False
            or preparation.get("native_decode_generation_and_rendering_executed") is not False
            or preparation.get("experimental_development_preset_acknowledged") is not True):
        raise ValueError("Unexpected source-world preparation metadata")
    preset = json.loads(read_file(root, Path("src/main/resources/data/dungeonsalive/worldgen/world_preset/living_world.json")))
    biome_source = preset["dimensions"]["minecraft:overworld"]["generator"]["biome_source"]
    if (biome_source.get("type") != "dungeonsalive:living_overworld"
            or any(biome_source.get(name) != "dungeonsalive:" + name
                   for name in ("amber_grove", "luminous_marsh", "ember_crags", "blighted_thicket"))):
        raise ValueError("Living-world preset must include all four implemented custom biomes")
    staged_level = renamed_fresh_level(world_files["level.dat"], preset)
    evidence = {}
    reports = {}
    report_paths = [("java", java_report), ("native", native_report)]
    if not visual_pending:
        report_paths.append(("visual", visual_report))
    for name, path in report_paths:
        raw = read_file(root, path)
        reports[name] = json.loads(raw)
        evidence[name] = {"path": str(safe_path(root, path)), "sha256": digest(raw)}
    jar_path, jar = current_artifact(root, reports["java"])
    sha = digest(jar)
    native = reports["native"]
    if (native.get("status") != "passed" or native.get("exit_code") != 0
            or native.get("game_test_server_started") is not True or native.get("completed_test_count", 0) <= 0
            or native.get("required_failures") != 0 or native.get("optional_failures") != 0
            or native.get("failure_messages") != [] or native.get("artifact", {}).get("sha256") != sha):
        raise ValueError("Native GameTests must have passed on the identical artifact SHA")
    if visual_pending:
        evidence["visual"] = {"status": "pending", "scope": "isolated_living_world_client",
                              "reason": "Separate fresh test instance prepared for its first actual native client sighting; no visual QA claimed"}
    else:
        visual = reports["visual"]
        if (visual.get("status") != "passed" or visual.get("scope") != "isolated_living_world_client"
                or visual.get("artifact", {}).get("sha256") != sha
                or visual.get("world_preset") != "dungeonsalive:living_world"
                or visual.get("checks", {}).get("native_world_loaded") is not True
                or visual.get("checks", {}).get("living_world_terrain_rendered") is not True
                or not visual.get("screenshots")):
            raise ValueError("Matching scoped native-client terrain QA is required")
        for screenshot in visual["screenshots"]:
            if screenshot.get("reviewed") is not True or digest(read_file(root, Path(screenshot["path"]))) != screenshot.get("sha256"):
                raise ValueError("Visual evidence must exist, match its hash, and have actually been reviewed")
        evidence["visual"]["status"] = "passed"
    source_settings = root / SOURCE_INSTANCE
    pack_bytes = read_file(root, source_settings / "mmc-pack.json")
    pack = json.loads(pack_bytes)
    components = {entry["uid"]: entry["version"] for entry in pack["components"]}
    if (pack.get("formatVersion") != 1 or components != {"org.lwjgl3": "3.3.3", "net.minecraft": "1.21.1", "net.neoforged": "21.1.265"}
            or len(pack["components"]) != 3):
        raise ValueError("Unexpected Prism component versions; review before staging")
    config_bytes = read_file(root, source_settings / "instance.cfg")
    source = configparser.ConfigParser(interpolation=None)
    source.optionxform = str
    source.read_string(config_bytes.decode())
    general = source["General"]
    if general.get("JavaVersion", "").split(".")[0] != "21" or general.get("OverrideJavaLocation") != "true":
        raise ValueError("Source instance must specify its working Java 21 location")
    config = configparser.ConfigParser(interpolation=None)
    config.optionxform = str
    config["General"] = {key: general[key] for key in JAVA_KEYS if key in general}
    instance_name = PENDING_INSTANCE_NAME if visual_pending else INSTANCE_NAME
    config["General"].update({"ConfigVersion": "1.3", "InstanceType": "OneSix", "name": instance_name,
                              "uuid": uuid.uuid4().hex, "iconKey": "default", "OverrideMemory": "true",
                              "MinMemAlloc": "512", "MaxMemAlloc": "4096", "OverrideMiscellaneous": "true",
                              "JoinServerOnLaunch": "true", "JoinServerOnLaunchAddress": "", "JoinWorldOnLaunch": WORLD_NAME,
                              "OverrideWindow": "true", "LaunchMaximized": "false", "MinecraftWinWidth": "1600", "MinecraftWinHeight": "1000"})
    text = io.StringIO()
    config.write(text, space_around_delimiters=False)
    return {"target": target, "world": world, "world_files": world_files, "staged_level": staged_level,
            "jar_path": jar_path, "jar": jar, "sha256": sha, "config": text.getvalue().encode(), "pack": pack_bytes,
            "instance_name": instance_name, "visual_status": "pending" if visual_pending else "passed",
            "evidence": evidence, "source_settings_hashes": {"mmc-pack.json": digest(pack_bytes), "instance.cfg": digest(config_bytes)}}


def publish_new_directory(source: Path, target: Path) -> None:
    """Atomic no-replace rename, including an existing empty unmarked destination."""
    libc = ctypes.CDLL(None, use_errno=True)
    if sys.platform == "darwin":
        operation = libc.renamex_np
        operation.argtypes = (ctypes.c_char_p, ctypes.c_char_p, ctypes.c_uint)
        result = operation(os.fsencode(source), os.fsencode(target), 0x00000004)  # RENAME_EXCL
    elif sys.platform.startswith("linux") and hasattr(libc, "renameat2"):
        operation = libc.renameat2
        operation.argtypes = (ctypes.c_int, ctypes.c_char_p, ctypes.c_int, ctypes.c_char_p, ctypes.c_uint)
        result = operation(-100, os.fsencode(source), -100, os.fsencode(target), 1)  # AT_FDCWD / RENAME_NOREPLACE
    else:
        raise ValueError("Atomic exclusive directory publication is unavailable on this platform")
    if result:
        error = ctypes.get_errno()
        raise OSError(error, os.strerror(error), str(target))


def stage(root: Path, plan: dict, revalidate) -> None:
    target = plan["target"]
    safe_path(root, target.parent).mkdir(parents=True, exist_ok=True)
    temporary = Path(tempfile.mkdtemp(prefix=".living-prism-stage-", dir=target.parent))
    try:
        world = temporary / "minecraft/saves" / WORLD_NAME
        world.mkdir(parents=True)
        for name, data in plan["world_files"].items():
            (world / name).write_bytes(data)
        (world / "level.dat.source").write_bytes(plan["world_files"]["level.dat"])
        (world / "level.dat").write_bytes(plan["staged_level"])
        (world / "dungeonsalive-living-prism-world.txt").write_text(TOKEN + "\n")
        mods = temporary / "minecraft/mods"
        mods.mkdir()
        (mods / plan["jar_path"].name).write_bytes(plan["jar"])
        (temporary / "instance.cfg").write_bytes(plan["config"])
        (temporary / "mmc-pack.json").write_bytes(plan["pack"])
        (temporary / "minecraft/options.txt").write_text(OPTIONS)
        (temporary / MARKER).write_text(TOKEN + "\n")
        manifest = {"kind": TOKEN, "created_at_utc": datetime.now(timezone.utc).isoformat(),
                    "instance_name": plan["instance_name"], "quick_play_world": WORLD_NAME,
                    "visual_status": plan["visual_status"],
                    "world_source": str(plan["world"]), "source_level_sha256": digest(plan["world_files"]["level.dat"]),
                    "staged_level_sha256": digest(plan["staged_level"]), "artifact": {"path": str(plan["jar_path"]), "sha256": plan["sha256"]},
                    "evidence": plan["evidence"], "source_settings_hashes": plan["source_settings_hashes"],
                    "source_world_modified": False, "old_instance_modified": False, "launcher_opened": False,
                    "scope": ("Fresh Creative test instance for its first native client sighting; visual QA is pending"
                              if plan["visual_status"] == "pending" else
                              "Fresh playable Creative test world; reviewed terrain scope only, no full-mod visual-fidelity claim")}
        (temporary / "staging.json").write_text(json.dumps(manifest, ensure_ascii=False, indent=2) + "\n")
        fresh = revalidate()
        if (fresh["sha256"] != plan["sha256"] or fresh["evidence"] != plan["evidence"]
                or fresh["world_files"] != plan["world_files"] or fresh["source_settings_hashes"] != plan["source_settings_hashes"]):
            raise ValueError("Staging inputs changed during the copy; refusing publication")
        publish_new_directory(temporary, target)
    finally:
        if temporary.exists():
            shutil.rmtree(temporary)


def main() -> int:
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--world", type=Path, required=True, help="Untouched marked world from prepare_living_showcase.py")
    parser.add_argument("--java-report", type=Path, default=Path("reports/local-java-check.json"))
    parser.add_argument("--native-report", type=Path, required=True)
    visual_mode = parser.add_mutually_exclusive_group(required=True)
    visual_mode.add_argument("--visual-report", type=Path, help="Matching reviewed native-client terrain report")
    visual_mode.add_argument("--visual-pending", action="store_true",
                             help="Prepare the separate first-sighting profile labeled 'Sichttest offen'; visual QA remains pending")
    parser.add_argument("--stage", action="store_true", help="Publish the NEW instance after every evidence gate passes; never launches it")
    args = parser.parse_args()
    try:
        validate = lambda: preflight(ROOT, args.world, args.java_report, args.native_report, args.visual_report,
                                     visual_pending=args.visual_pending)
        plan = validate()
        if args.stage:
            stage(ROOT, plan, validate)
        print(json.dumps({"status": "staged" if args.stage else "validated_without_writing", "target": str(plan["target"]),
                          "instance_name": plan["instance_name"], "visual_status": plan["visual_status"],
                          "quick_play_world": WORLD_NAME, "artifact_sha256": plan["sha256"],
                          "launcher_opened": False}, ensure_ascii=False, indent=2))
        return 0
    except (ValueError, OSError, KeyError, TypeError, configparser.Error) as error:
        print(f"Refusing Prism staging: {error}", file=sys.stderr)
        return 1


if __name__ == "__main__":
    raise SystemExit(main())
