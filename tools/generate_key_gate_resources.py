#!/usr/bin/env python3
"""Own optional gate geometry/recipes; reference installed Minecraft textures, copy no pixels."""
from pathlib import Path
import argparse
import hashlib
import json

ROOT = Path(__file__).resolve().parents[1]
ASSETS = ROOT / 'src/main/resources/assets/dungeonsalive'
DATA = ROOT / 'src/main/resources/data'
KINDS = {'key_gate': ('gold_block', 'gold_ingot'), 'diamond_key_gate': ('diamond_block', 'diamond')}
CHECK = False


def write(path, value):
    content = json.dumps(value, indent=2, ensure_ascii=False) + '\n'
    if CHECK:
        if not path.is_file() or path.read_text(encoding='utf-8') != content:
            raise SystemExit(f'Generated key-gate resource is stale or missing: {path.relative_to(ROOT)}')
        return
    path.parent.mkdir(parents=True, exist_ok=True)
    path.write_text(content, encoding='utf-8')


def cube(start, end, texture, panel=None):
    width, height, depth = [end[i] - start[i] for i in range(3)]
    faces = {side: {'texture': texture, 'uv': [0, 0, *size]} for side, size in {
        'north': (width, height), 'south': (width, height), 'east': (depth, height),
        'west': (depth, height), 'up': (width, depth), 'down': (width, depth)}.items()}
    if panel:
        for side in ('east', 'west'):
            faces[side] = {'texture': panel, 'uv': [0, 0, 16, 16]}
    return {'from': start, 'to': end, 'faces': faces}


def elements(half, offset=0):
    # Base orientation matches DoorBlock.EAST_SHAPE: x=0..3. The entire slab and
    # original key-shaped ornament rotate together to match the native collision.
    pieces = [cube([0, offset, 0], [3, offset + 16, 16], '#iron', '#' + half)]
    for x0, x1 in ((3, 4), (-1, 0)):
        if half == 'upper':
            pieces += [cube([x0, offset + 8, 6], [x1, offset + 12, 10], '#key'),
                       cube([x0, offset + 3, 7], [x1, offset + 9, 9], '#key'),
                       cube([x0, offset + 3, 9], [x1, offset + 5, 11], '#key')]
        else:
            pieces += [cube([x0, offset + 9, 6], [x1, offset + 12, 10], '#key')]
    return pieces


def add_tag(path, values):
    tag = json.loads(path.read_text()) if path.exists() else {'replace': False, 'values': []}
    for value in values:
        if value not in tag['values']:
            tag['values'].append(value)
    write(path, tag)


def main():
    global CHECK
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--check', action='store_true', help='Verify exact generated contents without writing files.')
    CHECK = parser.parse_args().check
    generated = []
    for ident, (metal, ingredient) in KINDS.items():
        textures = {'particle': 'minecraft:block/iron_block', 'iron': 'minecraft:block/iron_block',
                    'lower': 'minecraft:block/iron_door_bottom', 'upper': 'minecraft:block/iron_door_top',
                    'key': 'minecraft:block/' + metal}
        variants = {}
        rotation = {'east': 0, 'south': 90, 'west': 180, 'north': 270}
        open_plane = {'east': {'left': 'south', 'right': 'north'},
                      'south': {'left': 'west', 'right': 'east'},
                      'west': {'left': 'north', 'right': 'south'},
                      'north': {'left': 'east', 'right': 'west'}}
        for facing in rotation:
            for half in ('lower', 'upper'):
                for hinge in ('left', 'right'):
                    for opened in (False, True):
                        plane = open_plane[facing][hinge] if opened else facing
                        key = f'facing={facing},half={half},hinge={hinge},open={str(opened).lower()}'
                        variants[key] = {'model': f'dungeonsalive:block/{ident}_{half}', 'y': rotation[plane]}
        paths = {}
        paths[ASSETS / 'blockstates' / f'{ident}.json'] = {'variants': variants}
        for half in ('lower', 'upper'):
            paths[ASSETS / 'models/block' / f'{ident}_{half}.json'] = {
                'parent': 'minecraft:block/block', 'render_type': 'minecraft:cutout',
                'textures': textures, 'elements': elements(half)}
        paths[ASSETS / 'models/item' / f'{ident}.json'] = {
            'parent': 'minecraft:block/block', 'render_type': 'minecraft:cutout', 'textures': textures,
            'elements': elements('lower') + elements('upper', 16),
            'display': {'gui': {'rotation': [30, 225, 0], 'translation': [0, -4, 0], 'scale': [.45, .45, .45]},
                        'ground': {'translation': [0, 2, 0], 'scale': [.25, .25, .25]},
                        'fixed': {'rotation': [0, 90, 0], 'translation': [0, -4, 0], 'scale': [.5, .5, .5]}}}
        paths[DATA / 'dungeonsalive/recipe' / f'{ident}.json'] = {
            'type': 'minecraft:crafting_shapeless', 'category': 'redstone',
            'ingredients': [{'item': 'minecraft:iron_door'}, {'item': 'minecraft:' + ingredient},
                            {'item': 'minecraft:tripwire_hook'}],
            'result': {'id': 'dungeonsalive:' + ident, 'count': 1}}
        paths[DATA / 'dungeonsalive/loot_table/blocks' / f'{ident}.json'] = {
            'type': 'minecraft:block', 'pools': [{'rolls': 1, 'entries': [{'type': 'minecraft:item', 'name': 'dungeonsalive:' + ident}],
                'conditions': [{'condition': 'minecraft:survives_explosion'},
                    {'condition': 'minecraft:block_state_property', 'block': 'dungeonsalive:' + ident,
                     'properties': {'half': 'lower'}}]}]}
        paths[DATA / 'dungeonsalive/advancement/recipes' / f'{ident}.json'] = {
            'parent': 'minecraft:recipes/root',
            'criteria': {'has_door': {'trigger': 'minecraft:inventory_changed',
                                     'conditions': {'items': [{'items': ['minecraft:iron_door']}]}},
                         'has_recipe': {'trigger': 'minecraft:recipe_unlocked', 'conditions': {'recipe': 'dungeonsalive:' + ident}}},
            'requirements': [['has_door', 'has_recipe']], 'rewards': {'recipes': ['dungeonsalive:' + ident]}}
        for path, value in paths.items():
            write(path, value)
            generated.append({'path': str(path.relative_to(ROOT)), 'sha256': hashlib.sha256(path.read_bytes()).hexdigest()})
    ids = ['dungeonsalive:' + ident for ident in KINDS]
    for tag in ('doors', 'mineable/pickaxe'):
        add_tag(DATA / 'minecraft/tags/block' / f'{tag}.json', ids)
    add_tag(DATA / 'minecraft/tags/item/doors.json', ids)
    write(ROOT / 'design/key-gate-resources.json', {
        'schema_version': 1, 'generator': 'tools/generate_key_gate_resources.py',
        'author': 'Dungeons Alive project', 'geometry_license': 'MIT',
        'textures': 'References to installed Minecraft resources only; no third-party pixels copied.',
        'scope': 'Two optional player-built Minecraft door adaptations, not imported Dungeons gate models.',
        'native_visual_validation': 'pending', 'generated_files': generated})
    print(f'{"Verified" if CHECK else "Generated"} {len(KINDS)} optional gates; {len(generated)} models, blockstates, recipes, loot and unlock files.')


if __name__ == '__main__':
    main()
