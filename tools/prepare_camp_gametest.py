#!/usr/bin/env python3
"""Install the optional camps pack into a dedicated, marked development test directory."""
from pathlib import Path
import shutil

ROOT = Path(__file__).resolve().parents[1]
RUN = ROOT / 'run-gametest-camps'
MARKER = RUN / '.dungeonsalive-camp-test'

def main():
    if RUN.exists() and any(RUN.iterdir()) and not MARKER.exists():
        raise SystemExit('Refusing to change an existing unmarked camp-test directory.')
    RUN.mkdir(parents=True, exist_ok=True)
    MARKER.write_text('Dedicated Dungeons Alive optional-pack GameTest directory.\n')
    target = RUN/'world/datapacks/dungeons_camps'
    shutil.copytree(ROOT/'examples/datapacks/dungeons_camps', target, dirs_exist_ok=True)
    (RUN/'eula.txt').write_text('eula=true\n')
    (RUN/'server.properties').write_text('level-name=world\nonline-mode=false\nmax-tick-time=60000\n')
    print('Prepared', target.relative_to(ROOT))
    print('Run the dedicated gameTestServer profile with dungeonsalive.test.camps=true.')

if __name__ == '__main__': main()
