#!/usr/bin/env python3
"""Verify the deliverable's structure and provenance without rerunning gameplay tests.

The report deliberately separates archive integrity from the actual client/server
and visual evidence in docs/VALIDATION.md. It is not a claim of complete Dungeons
gameplay or a substitute for native visual review.
"""
from __future__ import annotations

import ast
from datetime import datetime, timezone
import hashlib
import json
from pathlib import Path
import re
import sys
import zipfile

from PIL import Image

ROOT = Path(__file__).resolve().parents[1]
FOLDERS = ('src', 'gradle', 'docs', 'tools', 'profiles', 'examples', 'design',
           'schemas', 'reports', 'third_party')


def main() -> None:
    errors: list[str] = []
    counts: dict[str, object] = {}
    version = next(line.split('=', 1)[1].strip()
                   for line in (ROOT / 'gradle.properties').read_text().splitlines()
                   if line.startswith('mod_version='))
    files = sorted(path for folder in FOLDERS for path in (ROOT / folder).rglob('*')
                   if path.is_file() and '__pycache__' not in path.parts)

    def check(condition: bool, message: str) -> None:
        if not condition:
            errors.append(message)

    def digest(path: Path) -> str:
        return hashlib.sha256(path.read_bytes()).hexdigest()

    def read_json(relative: str):
        return json.loads((ROOT / relative).read_text(encoding='utf-8'))

    parsed = 0
    for path in files:
        if path.suffix == '.json':
            try:
                json.loads(path.read_text(encoding='utf-8'))
                parsed += 1
            except (ValueError, UnicodeError) as exc:
                errors.append(f'JSON {path.relative_to(ROOT)}: {exc}')
    counts['json_parsed'] = parsed

    decoded = 0
    for path in files:
        if path.suffix.lower() == '.png':
            try:
                with Image.open(path) as bitmap:
                    bitmap.verify()
                decoded += 1
            except (OSError, ValueError) as exc:
                errors.append(f'PNG {path.relative_to(ROOT)}: {exc}')
    counts['png_decoded'] = decoded

    scripts = list((ROOT / 'tools').glob('*.py'))
    for path in scripts:
        try:
            ast.parse(path.read_text(encoding='utf-8'), filename=str(path))
        except SyntaxError as exc:
            errors.append(f'Python {path.name}: {exc}')
    counts['python_tools_syntax_checked'] = len(scripts)

    links = 0
    docs = list(ROOT.glob('*.md')) + list((ROOT / 'docs').rglob('*.md'))
    for path in docs:
        for raw in re.findall(r'\[[^\]\n]*\]\(([^)\n]+)\)', path.read_text(encoding='utf-8')):
            target = raw.split('#', 1)[0]
            if not target or re.match(r'[a-zA-Z][a-zA-Z0-9+.-]*:', target):
                continue
            resolved = (path.parent / target).resolve()
            check(resolved.exists(), f'Missing local link in {path.relative_to(ROOT)}: {raw}')
            check(resolved.is_relative_to(ROOT), f'Link outside deliverable in {path.relative_to(ROOT)}: {raw}')
            links += 1
    counts['local_markdown_links_checked'] = links

    inventory = read_json('design/model-delivery-inventory.json')
    verified = 0
    for group in ('input_manifests', 'license_and_notice_files', 'source_snapshots',
                  'delivered_textures', 'model_and_animation_sources'):
        for entry in inventory[group]:
            path = ROOT / entry['file']
            check(path.is_file(), f'Missing inventoried file: {entry["file"]}')
            if path.is_file():
                check(path.stat().st_size == entry['bytes'], f'Stale inventory size: {entry["file"]}')
                check(digest(path) == entry['sha256'], f'Stale inventory hash: {entry["file"]}')
                verified += 1
    counts['provenance_file_hashes_checked'] = verified
    counts['model_inventory'] = inventory['counts']

    implementation = read_json('design/implementation-status.json')
    current = implementation['counts']
    check(current['registered_figures'] == 131, 'Unexpected registered-figure count')
    check(current['hostile'] == 78 and current['peaceful'] == 53, 'Unexpected entity categories')
    check(current['runtime_rows_without_exact_catalog_mapping'] == 0, 'Unmapped runtime entities')
    check(current['excluded_sift'] == 47, 'Sift exclusion count changed')
    check(current['registered_spell_constructs'] == 4, 'Missing separately registered constructs')
    counts['implementation'] = current

    evidence = read_json('reports/validation-summary.json')
    unit = read_json('reports/test-results.json')
    check(unit['total_tests'] == 50 and unit['failures'] == 0 and unit['errors'] == 0 and unit['skipped'] == 0,
          'JUnit evidence differs from the accepted build')
    check(evidence['version'] == version and unit['version'] == version, 'Stale validation version')
    check(evidence['server_gametests'] == 30 and evidence['server_failed'] == 0,
          'GameTest summary differs from the accepted run')
    check(evidence['camp_additional_tests'] == 2
          and evidence['camp_total_tests'] == 32,
          'Camp tests must count two additional distinct cases, not repeat the core count')
    server_log = (ROOT / 'reports/server-validation-log.txt').read_text()
    passed_counts = [int(value) for value in re.findall(r'All (\d+) required tests passed', server_log)]
    check(passed_counts == [evidence['server_gametests'], evidence['camp_total_tests']]
          and 'BUILD SUCCESSFUL' in server_log, 'Server summary does not match retained successful log')
    counts['reported_junit_tests'] = unit['total_tests']
    counts['reported_server_gametests'] = evidence['server_gametests']
    counts['reported_distinct_server_tests_including_camps'] = evidence['camp_total_tests']

    captures = read_json('reports/model-showcase.json')
    check(captures.get('complete') is True and not captures.get('failure'), 'Incomplete model capture')
    check(len(captures['profiles']) == 131 and len(captures['captures']) == 352,
          'Current gallery manifest count mismatch')
    check(set(captures['profiles']) == {entry['id'] for entry in implementation['runtime_entities']},
          'Native gallery does not cover exactly the registered figure set')
    check({entry['profile'] for entry in captures['captures']} == set(captures['profiles'])
          and len({(entry['profile'], entry['view']) for entry in captures['captures']}) == 352,
          'Native gallery has missing figures or duplicate profile/view entries')
    counts['current_native_main_frames'] = len(captures['captures'])
    counts['native_animation_sequences'] = len(captures['clips'])
    counts['native_animation_frames'] = sum(len(clip['frames']) for clip in captures['clips'])
    check(counts['native_animation_sequences'] == evidence['native_animation_sequences'], 'Clip count mismatch')
    check(counts['native_animation_frames'] == evidence['native_animation_frames'], 'Clip frame count mismatch')

    gear_inventory = read_json('design/gear-source-inventory.json')
    gear_hashes = 0
    for group in ('source_files', 'delivered_files'):
        for entry in gear_inventory[group]:
            path = ROOT / entry['file']
            check(path.is_file() and digest(path) == entry['sha256'], f'Gear provenance mismatch: {entry["file"]}')
            gear_hashes += 1
    check(gear_inventory['counts']['artifacts'] == 8 and gear_inventory['counts']['weapons'] == 6,
          'Unexpected gear selection')
    counts['gear_provenance_hashes_checked'] = gear_hashes

    actual_hashes = 0
    for name in ('model-showcase', 'feature-showcase', 'camp-showcase'):
        recorded = read_json('reports/' + name + '.json')
        check(recorded.get('complete') is True and not recorded.get('failure'), f'Incomplete {name}')
        input_manifest = ROOT / recorded['archive_input_manifest_path']
        check(input_manifest.is_file() and digest(input_manifest) == recorded['input_manifest_sha256'],
              f'Unchanged input-manifest hash mismatch: {name}')
        original_sources = {source['run']: source for source in recorded.get('source_runs', [])}
        for source in original_sources.values():
            path = ROOT / source['archive_manifest_path']
            check(path.is_file() and digest(path) == source['manifest_sha256'],
                  f'Unchanged original native manifest hash mismatch: {source["run"]}')
        if name == 'feature-showcase':
            check(len(recorded['captures']) == 20 and len(recorded.get('clips', [])) == 5
                  and evidence['feature_main_frames'] == 20 and evidence['feature_sequences'] == 5,
                  'Feature evidence must contain 20 main views and five sequences')
            check(len({entry['file'] for entry in recorded['captures']}) == 20
                  and len({clip['subject'] for clip in recorded.get('clips', [])}) == 5,
                  'Feature evidence contains duplicate main views or sequence subjects')
            check(sum(len(clip['frames']) for clip in recorded['clips']) == evidence['feature_sequence_frames'],
                  'Feature sequence-frame count differs from summary')
        elif name == 'camp-showcase':
            check(len(recorded['captures']) == 3 and len({entry['file'] for entry in recorded['captures']}) == 3
                  and evidence['camp_main_frames'] == 3, 'Camp evidence must contain three distinct views')
        frames = recorded['captures'] + [frame for clip in recorded.get('clips', []) for frame in clip['frames']]
        for frame in frames:
            if original_sources:
                original = original_sources.get(frame.get('source_run'))
                check(original is not None and frame.get('source_manifest_sha256') == original['manifest_sha256'],
                      f'Frame origin does not match a retained original native manifest: {frame["file"]}')
            if 'archive_path' in frame:
                path = ROOT / frame['archive_path']
                check(path.is_file() and digest(path) == frame['sha256'], f'Native archive bytes changed: {frame["archive_path"]}')
                actual_hashes += 1
    counts['archived_current_native_png_hashes_checked'] = actual_hashes
    boss = read_json('reports/boss-showcase.json')
    for entry in boss['captures']:
        path = ROOT / entry['archive_path']
        check(path.is_file() and digest(path) == entry['sha256'],
              f'Boss capture bytes differ: {entry["file"]}')
    counts['native_boss_capture_hashes_checked'] = len(boss['captures'])
    gif_paths = sorted((ROOT / 'reports' / 'animations' / 'v04').glob('*.gif'))
    check(len(gif_paths) == evidence['native_animation_sequences'] + evidence['feature_sequences'], 'Missing current GIF sequence')
    gif_provenance = read_json('reports/animations/v04/provenance.json')
    check({entry['file'] for entry in gif_provenance['clips']} == {path.relative_to(ROOT).as_posix() for path in gif_paths},
          'GIF files differ from their source-provenance manifest')
    for entry in gif_provenance['clips']:
        path = ROOT / entry['file']
        check(path.is_file() and digest(path) == entry['sha256'], f'GIF derivative hash mismatch: {entry["file"]}')
        check(len(entry['source_frames']) == entry['native_source_frames']
              and sorted({frame['source_run'] for frame in entry['source_frames']}) == entry['source_runs'],
              f'GIF original-run provenance is inconsistent: {entry["file"]}')
    for path in gif_paths:
        with Image.open(path) as animation:
            check(animation.n_frames > 1, f'GIF is not animated: {path.name}')
    counts['delivered_animated_gifs'] = len(gif_paths)

    jar = ROOT / 'build' / 'libs' / f'dungeonsalive-{version}.jar'
    with zipfile.ZipFile(jar) as archive:
        check(archive.testzip() is None, 'JAR archive corruption')
        resources = [p for p in (ROOT / 'src/main/resources').rglob('*') if p.is_file()]
        names = set(archive.namelist())
        for path in resources:
            name = path.relative_to(ROOT / 'src/main/resources').as_posix()
            check(name in names and archive.read(name) == path.read_bytes(),
                  f'JAR resource missing or stale: {name}')
        counts['jar_resources_identical_to_source'] = len(resources)
        main_java = list((ROOT / 'src/main/java').rglob('*.java'))
        for path in main_java:
            name = path.relative_to(ROOT / 'src/main/java').with_suffix('.class').as_posix()
            check(name in names, f'Compiled class missing: {name}')
        counts['main_java_files_with_compiled_classes'] = len(main_java)
        for local, bundled in (
            ('LICENSE', 'META-INF/LICENSE-Dungeons-Alive.txt'),
            ('THIRD_PARTY_NOTICES.md', 'META-INF/THIRD_PARTY_NOTICES.md'),
            ('design/model-delivery-inventory.json', 'META-INF/dungeonsalive/model-provenance.json'),
            ('third_party/goety/LICENSE.txt', 'META-INF/third_party/goety/LICENSE.txt'),
            ('third_party/dungeons_mobs/LICENSE.txt', 'META-INF/third_party/dungeons_mobs/LICENSE.txt'),
            ('third_party/dungeons_mobs_port/LICENSE.txt', 'META-INF/third_party/dungeons_mobs_port/LICENSE.txt'),
            ('third_party/recrafted_mobs/LICENSE.txt', 'META-INF/third_party/recrafted_mobs/LICENSE.txt'),
            ('third_party/dungeons_gear/LICENSE.txt', 'META-INF/third_party/dungeons_gear/LICENSE.txt'),
            ('design/gear-source-inventory.json', 'META-INF/dungeonsalive/gear-provenance.json'),
        ):
            check(bundled in names and archive.read(bundled) == (ROOT / local).read_bytes(),
                  f'JAR attribution or inventory missing/stale: {bundled}')
        counts['jar_zip_integrity'] = 'passed'

    report = {
        'version': version,
        'checked_on': datetime.now(timezone.utc).strftime('%Y-%m-%d'),
        'status': 'passed' if not errors else 'failed',
        'checks': counts,
        'jar': {'path': f'dist/{jar.name}', 'bytes': jar.stat().st_size, 'sha256': digest(jar)},
        'errors': errors,
        'limitations': ('Structural packaging and provenance verification only. JUnit and server '
                        'tests are reported from their retained completed runs. Native views and '
                        'clips do not establish full original boss gameplay, all original animation '
                        'sequences, two-client multiplayer or exhaustive visual acceptance. Specific '
                        'remaining defects and source limitations are listed in docs/VALIDATION.md.'),
    }
    (ROOT / 'reports/release-checks.json').write_text(json.dumps(report, indent=2, ensure_ascii=False) + '\n', encoding='utf-8')
    print(json.dumps({'status': report['status'], 'checks': counts, 'errors': errors}, ensure_ascii=False))
    if errors:
        sys.exit(1)


if __name__ == '__main__':
    main()
