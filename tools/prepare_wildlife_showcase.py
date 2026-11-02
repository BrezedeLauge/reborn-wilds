#!/usr/bin/env python3
"""Prepare an isolated, manually staged wildlife scene; never changes the source world.

No capture automation, natural-spawn claim, load function or tick function is installed.
Only the explicitly invoked stage function changes the three documented test fields.
"""
import argparse
import gzip
import hashlib
import io
import json
from pathlib import Path
import shutil
import struct
import tempfile

ROOT = Path(__file__).resolve().parents[1]
SOURCE_RUN = ROOT / "run-gametest"
SOURCE = SOURCE_RUN / "world"
RUN = ROOT / "run-wildlife-showcase"
WORLD = RUN / "saves/wildlife-showcase"
TOKEN = "dungeonsalive-isolated-wildlife-showcase-v1"
MARKER = RUN / "dungeonsalive-wildlife-showcase.txt"
WORLD_MARKER = WORLD / "dungeonsalive-showcase-world.txt"
PACK_NAME = "dungeonsalive_wildlife_showcase"
ENTITY_TAG = "dungeonsalive_wildlife_showcase"
MAX_NBT_BYTES = 64 * 1024 * 1024


def enable_commands(level_dat: bytes) -> bytes:
    """Losslessly patch the decompressed NBT bytes, then recompress; preserve every other tag.

    Unknown/invalid tag types, duplicate Data/allowCommands and wrong boolean types fail
    closed. This does not deserialize/re-encode player, registry, worldgen or mod data.
    """
    with gzip.GzipFile(fileobj=io.BytesIO(level_dat)) as source:
        raw = source.read(MAX_NBT_BYTES + 1)
    if len(raw) > MAX_NBT_BYTES:
        raise ValueError("level.dat exceeds the bounded 64 MiB NBT limit")
    data_count, command_offsets, data_ends = 0, [], []

    def checked(position, size):
        if size < 0 or position < 0 or position + size > len(raw):
            raise ValueError("Truncated or negative-length NBT payload")
        return position + size

    def string(position):
        end = checked(position, 2)
        size = struct.unpack_from(">H", raw, position)[0]
        finish = checked(end, size)
        return raw[end:finish], finish

    def count(position):
        end = checked(position, 4)
        value = struct.unpack_from(">i", raw, position)[0]
        if value < 0:
            raise ValueError("Negative NBT collection length")
        return value, end

    def payload(kind, position, depth, path):
        nonlocal data_count
        if depth > 64:
            raise ValueError("NBT nesting exceeds 64 levels")
        fixed = {1: 1, 2: 2, 3: 4, 4: 8, 5: 4, 6: 8}
        if kind in fixed:
            return checked(position, fixed[kind])
        if kind in (7, 11, 12):
            length, position = count(position)
            return checked(position, length * {7: 1, 11: 4, 12: 8}[kind])
        if kind == 8:
            return string(position)[1]
        if kind == 9:
            end = checked(position, 1)
            child = raw[position]
            length, position = count(end)
            if child > 12 or child == 0 and length:
                raise ValueError("Invalid NBT list element type")
            if child in fixed:
                return checked(position, length * fixed[child])
            if length > len(raw):
                raise ValueError("NBT list cannot fit in its input")
            for _ in range(length):
                position = payload(child, position, depth + 1, None)
            return position
        if kind == 10:
            while True:
                end = checked(position, 1)
                child = raw[position]
                if child == 0:
                    if path == (b"Data",):
                        data_ends.append(position)
                    return end
                name, position = string(end)
                if path == () and name == b"Data":
                    data_count += 1
                    if child != 10:
                        raise ValueError("Root Data must be a compound")
                if path == (b"Data",) and name == b"allowCommands":
                    if child != 1:
                        raise ValueError("Data.allowCommands must be a byte")
                    command_offsets.append(position)
                child_path = None if path is None else path + (name,)
                position = payload(child, position, depth + 1, child_path)
        raise ValueError(f"Unsupported NBT tag type {kind}")

    if not raw or raw[0] != 10:
        raise ValueError("level.dat root must be a compound")
    _, start = string(1)
    if payload(10, start, 0, ()) != len(raw):
        raise ValueError("Unexpected trailing NBT bytes")
    if data_count != 1 or len(data_ends) != 1 or len(command_offsets) > 1:
        raise ValueError("Expected exactly one Data compound and at most one allowCommands")
    if command_offsets:
        offset = command_offsets[0]
        if raw[offset] == 1:
            return level_dat
        patched = raw[:offset] + b"\x01" + raw[offset + 1:]
    else:
        offset = data_ends[0]
        name = b"allowCommands"
        entry = b"\x01" + struct.pack(">H", len(name)) + name + b"\x01"
        patched = raw[:offset] + entry + raw[offset:]
    return gzip.compress(patched, mtime=0)


def functions():
    commands = [
        f"kill @e[tag={ENTITY_TAG}]",
        "gamerule doMobSpawning false", "gamerule doDaylightCycle false", "gamerule doWeatherCycle false",
        "weather clear", "time set 1000",
    ]
    for center in (0, 48, 96):
        commands += [f"fill {center - 12} 80 -12 {center + 12} 110 12 air",
                     f"fill {center - 12} 80 -12 {center + 12} 80 12 grass_block"]
    for mob, center in (("wooly_cow", 0), ("vulture", 48), ("wisp", 96)):
        for dx, dz in ((-3, -3), (3, -3), (-3, 3), (3, 3)):
            commands.append(f'summon dungeonsalive:{mob} {center + dx + .5} 81 {dz + .5} '
                            f'{{Tags:["{ENTITY_TAG}"],PersistenceRequired:1b}}')
    commands.append('summon dungeonsalive:village_merchant 0.5 81 8.5 '
                    f'{{Tags:["{ENTITY_TAG}","dungeonsalive_camp_npc"],PersistenceRequired:1b}}')
    commands += [f'tellraw @a {{"text":"[Wildlife showcase] Manually staged scene; not a natural-spawn test. Wait for AI grouping before alarm.","color":"yellow"}}']
    alarm = [f'damage @e[type=dungeonsalive:{mob},tag={ENTITY_TAG},limit=1,sort=arbitrary] 1 minecraft:generic by @s'
             for mob in ("wooly_cow", "vulture", "wisp", "village_merchant")]
    bodies = {"stage": commands, "day": ["weather clear", "time set 1000"],
              "night": ["weather clear", "time set 14000"], "alarm": alarm}
    return {name: "# Manual isolated-copy command; no load/tick hook.\n" +
            "\n".join("execute in minecraft:overworld run " + command for command in body) + "\n"
            for name, body in bodies.items()}


def preflight():
    for path in (SOURCE_RUN, SOURCE, RUN, RUN / "saves", WORLD):
        if path.is_symlink():
            raise ValueError(f"Refusing symlinked path: {path}")
        if path.exists() and not path.is_dir():
            raise ValueError(f"Expected directory: {path}")
    if not (SOURCE / "level.dat").is_file():
        raise ValueError("Missing run-gametest/world/level.dat; first run ./gradlew runGameTestServer.")
    for directory in (SOURCE, RUN):
        if directory.exists() and any(path.is_symlink() for path in directory.rglob("*")):
            raise ValueError(f"Refusing symlinks anywhere in {directory}")
    for directory, marker in ((RUN, MARKER), (WORLD, WORLD_MARKER)):
        if directory.exists() and (not marker.is_file() or marker.read_text(encoding="utf-8").strip() != TOKEN):
            raise ValueError(f"Existing directory is unmarked; refusing to alter {directory}")
    if WORLD.parent.exists() and any(path.is_dir() and path != WORLD for path in WORLD.parent.iterdir()):
        raise ValueError("Dedicated showcase run must contain only its marked wildlife-showcase save")
    pack = (WORLD if WORLD.exists() else SOURCE) / "datapacks" / PACK_NAME
    if pack.exists() and (not (pack / "showcase-marker.txt").is_file()
                          or (pack / "showcase-marker.txt").read_text(encoding="utf-8").strip() != TOKEN):
        raise ValueError("Existing wildlife showcase datapack is unmarked")
    allowed = {"pack.mcmeta", "showcase-marker.txt", *[f"data/{PACK_NAME}/function/{name}.mcfunction" for name in functions()]}
    if pack.exists() and any(path.is_file() and path.relative_to(pack).as_posix() not in allowed for path in pack.rglob("*")):
        raise ValueError("Existing showcase pack contains unknown files; refusing possible load/tick hooks")


def digest(data):
    return hashlib.sha256(data).hexdigest()


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--dry-run", action="store_true", help="Validate paths and NBT, print the planned commands, write nothing")
    args = parser.parse_args()
    try:
        preflight()
        original = ((WORLD if WORLD.exists() else SOURCE) / "level.dat").read_bytes()
        patched = enable_commands(original)
        commands = ["/reload", "/gamemode spectator @s", "/execute in minecraft:overworld run tp @s 48 86 -32 0 15",
                    *[f"/function {PACK_NAME}:{name}" for name in ("stage", "day", "night", "alarm")]]
        if args.dry_run:
            print("Preflight passed; no files changed.\n" + "\n".join(commands))
            return
        RUN.mkdir(parents=True, exist_ok=True)
        MARKER.write_text(TOKEN + "\n", encoding="utf-8")
        if not WORLD.exists():
            shutil.copytree(SOURCE, WORLD, ignore=shutil.ignore_patterns("session.lock"))
        WORLD_MARKER.write_text(TOKEN + "\n", encoding="utf-8")
        backup = WORLD / "level.dat.before-wildlife-showcase"
        if not backup.exists():
            backup.write_bytes(original)
        with tempfile.NamedTemporaryFile(dir=WORLD, prefix=".wildlife-level-", delete=False) as target:
            temporary = Path(target.name)
            target.write(patched)
        temporary.replace(WORLD / "level.dat")
        pack = WORLD / "datapacks" / PACK_NAME
        output = {"pack.mcmeta": json.dumps({"pack": {"pack_format": 48, "description": "Manual isolated wildlife showcase; staged, not natural generation"}}, indent=2) + "\n",
                  "showcase-marker.txt": TOKEN + "\n"}
        output.update({f"data/{PACK_NAME}/function/{name}.mcfunction": body for name, body in functions().items()})
        for relative, contents in output.items():
            path = pack / relative
            path.parent.mkdir(parents=True, exist_ok=True)
            path.write_text(contents, encoding="utf-8")
        (RUN / "options.txt").write_text(
            "version:3955\nonboardAccessibility:false\nrenderDistance:6\nsimulationDistance:5\nguiScale:2\nfullscreen:false\n"
            "pauseOnLostFocus:false\nmaxFps:30\nsoundCategory_master:0.0\nlang:de_de\ngamma:0.65\n"
            "entityDistanceScaling:1.5\nbobView:false\nrenderClouds:false\ntutorialStep:none\n", encoding="utf-8")
        manifest = {"token": TOKEN, "source": str(SOURCE.relative_to(ROOT)), "world": str(WORLD.relative_to(ROOT)),
                    "warning": "Manually staged development scene, not evidence of natural spawns or native visual QA. No automatic captures or tick/load hooks.",
                    "instructions": f'Launch ./gradlew runWildlifeShowcase. Run commands as the player. Enter the first three, wait for all nearby chunks to load, then stage. If the function is unknown, use /datapack enable "file/{PACK_NAME}". Wait a few seconds before alarm. Alarm causes real damage; restage for repeated checks. Use survival near the merchant to test trading.',
                    "commands": commands, "fields": [{"min": [x - 12, 80, -12], "max": [x + 12, 110, 12]} for x in (0, 48, 96)],
                    "cleanup_entity_tag": ENTITY_TAG, "source_level_sha256": digest((SOURCE / "level.dat").read_bytes()),
                    "backup_level_sha256": digest(backup.read_bytes()), "patched_level_sha256": digest(patched),
                    "files_sha256": {str((pack / path).relative_to(RUN)): digest(contents.encode("utf-8")) for path, contents in output.items()}}
        (RUN / "wildlife-showcase-manifest.json").write_text(json.dumps(manifest, indent=2) + "\n", encoding="utf-8")
        print("Prepared marked run-wildlife-showcase/saves/wildlife-showcase. Source world unchanged.")
        print(manifest["instructions"] + "\n" + "\n".join(commands))
        print("Scene boundaries, warnings and hashes: run-wildlife-showcase/wildlife-showcase-manifest.json")
    except (OSError, ValueError, EOFError, struct.error) as error:
        raise SystemExit(f"Wildlife showcase refused: {error}") from error


if __name__ == "__main__":
    main()
