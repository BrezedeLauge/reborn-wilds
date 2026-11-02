#!/usr/bin/env python3
"""Prepare the isolated copy for native optional-camp screenshots.

Copies only the dedicated camp GameTest world, including its datapack. The client
helper places the actual templates at a documented photography site; screenshots
do not claim that this site was discovered through natural world generation.
"""
from pathlib import Path
import shutil

ROOT = Path(__file__).resolve().parents[1]
SOURCE_RUN = ROOT / "run-gametest-camps"
SOURCE = SOURCE_RUN / "world"
SOURCE_PACK = SOURCE / "datapacks/dungeons_camps"
RUN = ROOT / "run-camp-showcase"
WORLD = RUN / "saves/camp-showcase"
MARKER = RUN / "dungeonsalive-camp-showcase.txt"
WORLD_MARKER = WORLD / "dungeonsalive-showcase-world.txt"
TOKEN = "dungeonsalive-isolated-camp-showcase-v1"


def main() -> None:
    paths = (SOURCE_RUN, SOURCE, SOURCE / "datapacks", SOURCE_PACK,
             RUN, RUN / "saves", WORLD, WORLD / "datapacks", WORLD / "datapacks/dungeons_camps")
    if any(path.is_symlink() for path in paths):
        raise SystemExit("Refusing symlinked source, run, world or datapack paths.")
    if not (SOURCE_RUN / ".dungeonsalive-camp-test").is_file() or not (SOURCE / "level.dat").is_file():
        raise SystemExit("First prepare the optional camp GameTest pack and run ./gradlew runCampGameTestServer.")
    if not (SOURCE_PACK / "pack.mcmeta").is_file():
        raise SystemExit("The dedicated camp test world has no optional dungeons_camps pack.")
    if any(path.is_symlink() for path in SOURCE.rglob("*")):
        raise SystemExit("The isolated source world contains a symlink; refusing to follow it.")
    if RUN.exists() and any(RUN.iterdir()):
        if not MARKER.is_file() or MARKER.read_text(encoding="utf-8").strip() != TOKEN:
            raise SystemExit("Existing run-camp-showcase is unmarked; move it aside explicitly.")
    if WORLD.exists():
        if not WORLD_MARKER.is_file() or WORLD_MARKER.read_text(encoding="utf-8").strip() != TOKEN:
            raise SystemExit("Existing camp showcase world is unmarked; refusing to alter it.")
    if WORLD.parent.exists() and any(path.is_dir() and path != WORLD for path in WORLD.parent.iterdir()):
        raise SystemExit("Camp showcase preflight requires this dedicated run to contain only its marked camp-showcase save.")
    RUN.mkdir(parents=True, exist_ok=True)
    MARKER.write_text(TOKEN + "\n", encoding="utf-8")
    if not WORLD.exists():
        shutil.copytree(SOURCE, WORLD, ignore=shutil.ignore_patterns("session.lock"))
    else:
        # Refresh only the explicitly owned optional pack, retaining prior shots and
        # the marked test world. Runtime uses this pack's actual template resources.
        shutil.copytree(SOURCE_PACK, WORLD / "datapacks/dungeons_camps", dirs_exist_ok=True)
    WORLD_MARKER.write_text(TOKEN + "\n", encoding="utf-8")
    (RUN / "options.txt").write_text(
        "version:3955\nonboardAccessibility:false\nrenderDistance:6\nsimulationDistance:5\n"
        "guiScale:2\nfullscreen:false\npauseOnLostFocus:false\nmaxFps:30\n"
        "soundCategory_master:0.0\nlang:de_de\ngamma:0.65\nentityDistanceScaling:1.5\n"
        "bobView:false\nrenderClouds:false\ntutorialStep:none\n",
        encoding="utf-8",
    )
    print("Prepared marked developer world: run-camp-showcase/saves/camp-showcase")
    print("Run ./gradlew runCampShowcase on the graphical desktop.")
    print("Required dev property: dungeonsalive.campShowcase=true; quickPlaySingleplayer=camp-showcase.")
    print("Three native frames + manifest: run-camp-showcase/captures/<UTC timestamp>/.")
    print("Existing captures remain; latest-capture.json points to the newest manifest.")
    print("The dev helper confirms only the known experimental-world dialog for this single marked copy; no global warning option is changed.")


if __name__ == "__main__":
    main()
