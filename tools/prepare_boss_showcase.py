#!/usr/bin/env python3
"""Prepare only the isolated marked world used by ./gradlew runBossShowcase.

The real client/server constructs a forge courtyard and photographs an actual
Redstone Monstrosity entity. No player save is read, edited or replaced.
"""
from pathlib import Path
import shutil

ROOT = Path(__file__).resolve().parents[1]
SOURCE = ROOT / "run-gametest/world"
RUN = ROOT / "run-boss-showcase"
WORLD = RUN / "saves/boss-showcase"
MARKER = RUN / "dungeonsalive-boss-showcase.txt"
WORLD_MARKER = WORLD / "dungeonsalive-showcase-world.txt"
TOKEN = "dungeonsalive-isolated-boss-showcase-v1"


def main() -> None:
    if SOURCE.is_symlink() or RUN.is_symlink() or WORLD.is_symlink():
        raise SystemExit("Refusing a symlinked source, run directory or showcase world.")
    if not (SOURCE / "level.dat").is_file():
        raise SystemExit("Create the isolated test world first: ./gradlew runGameTestServer")
    if RUN.exists() and any(RUN.iterdir()):
        if not MARKER.is_file() or MARKER.read_text(encoding="utf-8").strip() != TOKEN:
            raise SystemExit("Existing run-boss-showcase has no valid developer marker. Move it aside explicitly.")
    if WORLD.exists():
        if not WORLD_MARKER.is_file() or WORLD_MARKER.read_text(encoding="utf-8").strip() != TOKEN:
            raise SystemExit("Existing showcase world has no valid marker; refusing to alter it.")
    RUN.mkdir(parents=True, exist_ok=True)
    MARKER.write_text(TOKEN + "\n", encoding="utf-8")
    if not WORLD.exists():
        shutil.copytree(SOURCE, WORLD, ignore=shutil.ignore_patterns("session.lock"))
    WORLD_MARKER.write_text(TOKEN + "\n", encoding="utf-8")
    (RUN / "options.txt").write_text(
        "version:3955\nonboardAccessibility:false\nrenderDistance:6\nsimulationDistance:5\n"
        "guiScale:3\nfullscreen:false\npauseOnLostFocus:false\nmaxFps:30\n"
        "soundCategory_master:0.0\nlang:de_de\ngamma:0.65\nentityDistanceScaling:1.0\n"
        "bobView:false\nrenderClouds:false\ntutorialStep:none\n",
        encoding="utf-8")
    screenshots = RUN / "screenshots"
    if screenshots.is_dir():
        for name in ("01-redstone-monstrosity-hero.png", "02-redstone-monstrosity-gameplay.png",
                     "03-redstone-monstrosity-detail.png", "04-redstone-monstrosity-summon.png",
                     "05-redstone-monstrosity-slam.png", "06-night.png", "showcase-capture.json"):
            candidate = screenshots / name
            if candidate.is_file() and not candidate.is_symlink():
                candidate.unlink()
    print("Prepared marked developer world: run-boss-showcase/saves/boss-showcase")
    print("Run ./gradlew runBossShowcase on a graphical desktop (native 1920x1080 frames).")
    print("Images and capture metadata appear in run-boss-showcase/screenshots/.")


if __name__ == "__main__":
    main()
