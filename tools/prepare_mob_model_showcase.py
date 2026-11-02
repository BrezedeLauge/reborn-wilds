#!/usr/bin/env python3
"""Prepare the marked real-world model studio, or encode its native frame sequences.

The Java developer helper is disabled in production and unmarked worlds. Each run
keeps its own timestamped captures; preparing another run preserves earlier images.
GIF encoding only packages the recorded Minecraft frames and their timing.
"""
from __future__ import annotations

import argparse
import json
from pathlib import Path
import shutil

ROOT = Path(__file__).resolve().parents[1]
SOURCE = ROOT / "run-gametest/world"
RUN = ROOT / "run-mob-model-showcase"
WORLD = RUN / "saves/mob-model-showcase"
MARKER = RUN / "dungeonsalive-mob-model-showcase.txt"
WORLD_MARKER = WORLD / "dungeonsalive-showcase-world.txt"
TOKEN = "dungeonsalive-isolated-mob-model-showcase-v1"


def prepare() -> None:
    if any(path.is_symlink() for path in (SOURCE, RUN, RUN / "saves", WORLD)):
        raise SystemExit("Refusing a symlinked test source, run directory or world path.")
    if not (SOURCE / "level.dat").is_file():
        raise SystemExit("Create the isolated GameTest source first: ./gradlew runGameTestServer")
    if RUN.exists() and any(RUN.iterdir()):
        if not MARKER.is_file() or MARKER.read_text(encoding="utf-8").strip() != TOKEN:
            raise SystemExit("Existing run-mob-model-showcase has no valid developer marker. Move it aside explicitly.")
    if WORLD.exists():
        if not WORLD_MARKER.is_file() or WORLD_MARKER.read_text(encoding="utf-8").strip() != TOKEN:
            raise SystemExit("Existing studio world is not marked; refusing to change it.")
    RUN.mkdir(parents=True, exist_ok=True)
    MARKER.write_text(TOKEN + "\n", encoding="utf-8")
    if not WORLD.exists():
        shutil.copytree(SOURCE, WORLD, ignore=shutil.ignore_patterns("session.lock"))
    WORLD_MARKER.write_text(TOKEN + "\n", encoding="utf-8")
    (RUN / "options.txt").write_text(
        "version:3955\nonboardAccessibility:false\nrenderDistance:6\nsimulationDistance:5\n"
        "guiScale:2\nfullscreen:false\npauseOnLostFocus:false\nmaxFps:30\n"
        "soundCategory_master:0.0\nlang:de_de\ngamma:0.6\nentityDistanceScaling:1.5\n"
        "bobView:false\nrenderClouds:false\ntutorialStep:none\n",
        encoding="utf-8",
    )
    print("Prepared marked studio: run-mob-model-showcase/saves/mob-model-showcase")
    print("Run ./gradlew runMobModelShowcase on a graphical desktop (1440x900 native frames).")
    print("Optional filter: -PshowcaseMobs=royal_guard,redstone_monstrosity")
    print("Peaceful/model groups: -PshowcaseMobs=peaceful, creatures, merchants, or hostile; IDs may be mixed with groups.")
    print("Creatures receive idle/movement views; merchants also perform a real server trading interaction and greeting.")
    print("Optional animation selection: -PshowcaseClips=all, or a comma-separated list; 'none' disables sequences.")
    print("Existing timestamped runs are retained. latest-capture.json points to the newest manifest.")
    print("After capture: python3 tools/prepare_mob_model_showcase.py --assemble-clips")


def latest_manifest() -> Path:
    if not MARKER.is_file() or MARKER.read_text(encoding="utf-8").strip() != TOKEN:
        raise SystemExit("No marked model showcase run exists.")
    pointer = RUN / "latest-capture.json"
    if not pointer.is_file():
        raise SystemExit("No capture manifest yet; run runMobModelShowcase first.")
    relative = json.loads(pointer.read_text(encoding="utf-8"))["manifest"]
    path = (RUN / relative).resolve()
    if not path.is_relative_to((RUN / "captures").resolve()):
        raise SystemExit("Manifest path escapes the marked capture directory.")
    return path


def encode_clips(manifest_path: Path | None, max_width: int) -> None:
    try:
        from PIL import Image
    except ImportError as exc:
        raise SystemExit("Encoding native frame sequences requires Pillow: python3 -m pip install Pillow") from exc
    manifest_path = manifest_path.resolve() if manifest_path else latest_manifest()
    report = json.loads(manifest_path.read_text(encoding="utf-8"))
    if report.get("capture_kind") != "native_minecraft_world_model_checks":
        raise SystemExit("This is not a native model-showcase manifest.")
    base = manifest_path.parent
    movie_dir = base / "animations"
    movie_dir.mkdir(exist_ok=True)
    encoded = []
    for clip in report.get("clips", []):
        frames_meta = clip.get("frames", [])
        if len(frames_meta) < 2:
            continue
        frames = []
        durations = []
        for index, frame in enumerate(frames_meta):
            path = (base / frame["file"]).resolve()
            if not path.is_relative_to(base):
                raise SystemExit("A recorded frame path escapes its run directory.")
            with Image.open(path) as original:
                picture = original.convert("RGB")
                if picture.width > max_width:
                    picture = picture.resize((max_width, round(picture.height * max_width / picture.width)), Image.Resampling.LANCZOS)
                frames.append(picture)
            if index + 1 < len(frames_meta):
                tick_delta = frames_meta[index + 1]["game_tick"] - frame["game_tick"]
                durations.append(max(50, min(1000, round(tick_delta * 50))))
            else:
                durations.append(250)
        destination = movie_dir / f"{clip['profile']}-{clip['view']}.gif"
        frames[0].save(destination, save_all=True, append_images=frames[1:], duration=durations,
                       loop=0, disposal=2, optimize=False)
        encoded.append({
            "file": str(destination.relative_to(base)), "profile": clip["profile"], "view": clip["view"],
            "native_source_frames": len(frames), "duration_ms": sum(durations),
            "source": "GIF encoding/resizing of recorded native Minecraft frames; no synthesized motion",
        })
        for frame in frames:
            frame.close()
        print(destination.relative_to(ROOT) if destination.is_relative_to(ROOT) else destination)
    (base / "encoded-clips.json").write_text(json.dumps({"source_manifest": "manifest.json", "clips": encoded}, indent=2) + "\n", encoding="utf-8")
    print(f"Encoded {len(encoded)} native frame sequences; source PNGs are retained.")


def main() -> None:
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--assemble-clips", action="store_true", help="Encode the most recent real capture sequences as GIFs; do not prepare a world.")
    parser.add_argument("--manifest", type=Path, help="Specific prior run manifest to encode.")
    parser.add_argument("--max-width", type=int, default=720, help="GIF display width; native source PNGs retain full resolution.")
    args = parser.parse_args()
    if args.max_width < 160 or args.max_width > 1920:
        parser.error("--max-width must be between 160 and 1920")
    if args.manifest and not args.assemble_clips:
        parser.error("--manifest is used with --assemble-clips")
    if args.assemble_clips:
        encode_clips(args.manifest, args.max_width)
    else:
        prepare()


if __name__ == "__main__":
    main()
