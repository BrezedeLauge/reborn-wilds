#!/usr/bin/env python3
"""Restore the v0.4 selected texture bytes from pinned, retained community snapshots.

No network, external mod binary or game archive is used. The manually maintained
GoetyTropicalSlimeGeometry / Original* clips remain authoritative source files;
this script does not regenerate the central model index or any other model.
"""
from pathlib import Path
import hashlib
import json
import shutil

ROOT = Path(__file__).resolve().parents[1]
MANIFEST = ROOT / 'design/v04-source-model-additions.json'

def sha(path):
    return hashlib.sha256(path.read_bytes()).hexdigest()

def main():
    manifest = json.loads(MANIFEST.read_text(encoding='utf-8'))
    # Validate all inputs before writing anything.
    for source in manifest['source_files']:
        path = ROOT / source['snapshot']
        if sha(path) != source['sha256']:
            raise ValueError(f'Changed pinned model source: {source["snapshot"]}')
    report = []
    for texture in manifest['delivered_textures']:
        src = ROOT / texture['snapshot']
        target = ROOT / texture['file']
        target.parent.mkdir(parents=True, exist_ok=True)
        shutil.copyfile(src, target)
        if sha(target) != texture['sha256']:
            raise ValueError(f'Texture copy differs from source: {texture["file"]}')
        report.append({'file': texture['file'], 'sha256': texture['sha256'], 'bytes': target.stat().st_size})
    path = ROOT / 'reports/v04-model-source-checks.json'
    path.write_text(json.dumps({'source_snapshots_verified': len(manifest['source_files']),
                               'unchanged_texture_copies': report,
                               'runtime_or_visual_acceptance_claim': False}, indent=2) + '\n')
    print(f'Verified {len(manifest["source_files"])} pinned snapshots; restored {len(report)} unchanged texture files.')

if __name__ == '__main__':
    main()
