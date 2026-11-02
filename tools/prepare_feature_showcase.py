#!/usr/bin/env python3
"""Prepare only a marked, isolated copy for actual item and construct client captures."""
from pathlib import Path
import shutil

ROOT = Path(__file__).resolve().parents[1]
SOURCE = ROOT / 'run-gametest/world'
RUN = ROOT / 'run-feature-showcase'
WORLD = RUN / 'saves/feature-showcase'
TOKEN = 'dungeonsalive-isolated-feature-showcase-v1'
MARKER = RUN / 'dungeonsalive-feature-showcase.txt'


def main():
    if not (SOURCE/'level.dat').is_file(): raise SystemExit('Run ./gradlew runGameTestServer first.')
    if any(p.is_symlink() for p in [SOURCE,RUN,WORLD]): raise SystemExit('Refusing symlinked development paths.')
    if RUN.exists() and any(RUN.iterdir()) and (not MARKER.is_file() or MARKER.read_text().strip()!=TOKEN):
        raise SystemExit('Existing feature directory is not a marked isolated world.')
    RUN.mkdir(exist_ok=True)
    MARKER.write_text(TOKEN+'\n')
    if not WORLD.exists():
        WORLD.parent.mkdir(exist_ok=True)
        shutil.copytree(SOURCE,WORLD,ignore=shutil.ignore_patterns('session.lock'))
    (RUN/'options.txt').write_text('version:3955\nonboardAccessibility:false\nrenderDistance:5\nsimulationDistance:5\nguiScale:2\nfullscreen:false\npauseOnLostFocus:false\nmaxFps:30\nsoundCategory_master:0.0\nlang:de_de\ngamma:0.65\nbobView:false\nrenderClouds:false\ntutorialStep:none\n')
    print('Prepared marked run-feature-showcase; use ./gradlew runFeatureShowcase.')


if __name__=='__main__':main()
