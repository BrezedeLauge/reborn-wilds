#!/usr/bin/env python3
"""Prepare an isolated developer world for ./gradlew runRenderSmoke.

Run ./gradlew runGameTestServer first. Never copies or edits a normal player world.
The graphical run captures model prototypes and a real server-fed journal, then exits.
"""
from pathlib import Path
import shutil

ROOT = Path(__file__).resolve().parents[1]
SOURCE = ROOT / 'run-gametest/world'
RUN = ROOT / 'run-render-smoke'
WORLD = RUN / 'saves/render-smoke'
MARKER = RUN / 'dungeonsalive-render-test.txt'


def main():
    if not (SOURCE / 'level.dat').is_file():
        raise SystemExit('Generate the isolated test world first: ./gradlew runGameTestServer')
    if WORLD.exists() and not MARKER.is_file():
        raise SystemExit('Existing render world has no test marker. Move it aside explicitly before continuing.')
    RUN.mkdir(parents=True, exist_ok=True)
    if not WORLD.exists():
        shutil.copytree(SOURCE, WORLD, ignore=shutil.ignore_patterns('session.lock'))
    MARKER.write_text('Isolated Dungeons Alive graphical test world. Not a player save.\n', encoding='utf-8')
    (RUN / 'options.txt').write_text(
        'version:3955\nonboardAccessibility:false\nrenderDistance:2\nsimulationDistance:5\n'
        'guiScale:2\nfullscreen:false\npauseOnLostFocus:false\nmaxFps:30\nsoundCategory_master:0.0\nlang:de_de\n',
        encoding='utf-8')
    print('Ready. Run ./gradlew runRenderSmoke on a graphical desktop; captures appear in run-render-smoke/screenshots/.')


if __name__ == '__main__': main()
