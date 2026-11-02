#!/usr/bin/env python3
"""Reproducible original texture atlases, loot, spawn-egg models and profile manifest.

No game files, downloads, Pillow or third-party assets are used. The editable source
of the geometry is DungeonMobModel.java; gameplay is MobProfiles.java.
"""
from pathlib import Path
import argparse
import hashlib
import json
import math
import re
import struct
import zlib

ROOT = Path(__file__).resolve().parents[1]
ASSETS = ROOT / 'src/main/resources/assets/dungeonsalive'
DATA = ROOT / 'src/main/resources/data/dungeonsalive'


def png(path, width, height, pixels):
    def chunk(kind, payload):
        return struct.pack('>I', len(payload)) + kind + payload + struct.pack('>I', zlib.crc32(kind + payload) & 0xffffffff)
    raw = b''.join(b'\0' + bytes(sum(pixels[y * width:(y + 1) * width], [])) for y in range(height))
    path.parent.mkdir(parents=True, exist_ok=True)
    path.write_bytes(b'\x89PNG\r\n\x1a\n' + chunk(b'IHDR', struct.pack('>IIBBBBB', width, height, 8, 6, 0, 0, 0))
                     + chunk(b'IDAT', zlib.compress(raw, 9)) + chunk(b'IEND', b''))


def color(value):
    return [(value >> 16) & 255, (value >> 8) & 255, value & 255]


def mix(a, b, t):
    return [round(x * (1 - t) + y * t) for x, y in zip(a, b)]


def write_json(path, obj):
    path.parent.mkdir(parents=True, exist_ok=True)
    path.write_text(json.dumps(obj, indent=2, ensure_ascii=False) + '\n', encoding='utf-8')


def profiles():
    text = (ROOT / 'src/main/java/de/laugenbreze/dungeonsalive/entity/MobProfiles.java').read_text()
    rows = []
    pattern = re.compile(r'add\("([a-z_]+)", "([^"]+)", "([^"]+)", (\w+), (\w+), ([^;]+)\);')
    for match in pattern.finditer(text):
        ident, en, de, tier, family, values = match.groups()
        fields = [part.strip() for part in values.split(',')]
        health, damage, speed, armor, width, height = [float(part.removesuffix('f')) for part in fields[:6]]
        base, accent = [int(part, 16) for part in fields[6:8]]
        rows.append(dict(id=ident, name_en=en, name_de=de, tier=tier, family=family,
                         health=health, damage=damage, speed=speed, armor=armor, width=width,
                         height=height, color=base, accent=accent, attacks=fields[8:],
                         registry_id='dungeonsalive:' + ident, implementation_status='prototype',
                         model_status='original_cuboid_prototype', animation_status='procedural_prototype_see_validation_report',
                         behavior_status='server_authoritative_archetype_adaptation'))
    # This boss has a dedicated mesh/atlas pipeline; keep its provenance on regeneration.
    for profile in rows:
        if profile['id'] == 'redstone_monstrosity':
            profile.update(model_status='dedicated_reference_model',
                           animation_status='dedicated_server_timed_rig_see_validation',
                           model_source='tools/generate_redstone_monstrosity.py',
                           model_documentation='docs/REDSTONE_MONSTROSITY.md')
    if len(rows) < 38 or len({p['id'] for p in rows}) != len(rows):
        raise ValueError('Expected all established profiles and unique explicit runtime IDs.')
    bindings = {p['id']:p for p in json.loads((ROOT/'design/reference-models.json').read_text())['models']}
    for profile in rows:
        if profile['id'] in bindings:
            profile.update(model_status='authored_reference_adaptation', model_source=bindings[profile['id']]['asset'],
                           model_documentation='design/reference-models.json', animation_status='source_rig_adaptation_see_visual_validation')
        elif profile['id'] in ('pillager_raid_captain','vindicator_raid_captain','skeleton_horseman','conjured_slime'):
            profile.update(model_status='native_minecraft_composition',model_documentation='design/builtin-models.json',animation_status='server_state_driven_native_composition')
    return rows


def atlas(p):
    base, accent = color(p['color']), color(p['accent'])
    seed = int.from_bytes(hashlib.sha256(p['id'].encode()).digest()[:4], 'big')
    pixels = []
    for y in range(128):
        for x in range(128):
            noise = ((x * 271 + y * 419 + seed + (x // 3) * (y // 3) * 17) % 13 - 6)
            shade = [max(0, min(255, c + noise)) for c in base]
            if x >= 96 and y < 64:
                shade = mix(accent, [255, 255, 255], .18 if (x + y) % 7 == 0 else 0)
            elif y >= 96:
                shade = mix(base, accent, .3 if (x // 4 + y // 4) % 2 else .17)
            elif y >= 64 and x >= 80:
                shade = mix(base, accent, .55)
            elif x % 16 == 0 or y % 16 == 0:
                shade = mix(base, [0, 0, 0], .22)
            pixels.append(shade + [255])
    def rect(x, y, w, h, rgb):
        for yy in range(y, min(128, y + h)):
            for xx in range(x, min(128, x + w)):
                pixels[yy * 128 + xx] = list(rgb) + [255]
    # Front of a conventional 8x10x8 head. Wider prototypes have separate facet details.
    face = [139, 151, 144] if p['family'] == 'ILLAGER' else base
    if p['family'] in ('SKELETON', 'HORSEMAN'):
        face = [195, 192, 166]
    rect(8, 8, 8, 10, face)
    rect(8, 10, 3, 2, [22, 26, 29]); rect(13, 10, 3, 2, [22, 26, 29])
    rect(9, 10, 1, 1, accent); rect(14, 10, 1, 1, accent)
    rect(10, 15, 4, 1, mix(face, [0, 0, 0], .55))
    if p['family'] == 'GOLEM':
        rect(10, 10, 4, 2, accent); rect(18, 10, 4, 2, accent)
    if p['id'] == 'mooshroom_monstrosity':
        rect(80, 96, 32, 20, [147, 48, 45])
        for xx, yy in [(81, 98), (88, 104), (100, 97), (108, 110), (92, 113)]:
            rect(xx, yy, 3, 3, [237, 218, 174])
    if p['family'] == 'ENDERLING':
        rect(8, 10, 4, 2, accent); rect(15, 10, 4, 2, accent)
    png(ASSETS / 'textures/entity' / (p['id'] + '.png'), 128, 128, pixels)


def update_names(rows):
    """Merge canonical runtime names without regenerating any referenced model or texture."""
    language = {'en_us': {}, 'de_de': {}}
    for p in rows:
        for locale, name in [('en_us', p['name_en']), ('de_de', p['name_de'])]:
            language[locale]['entity.dungeonsalive.' + p['id']] = name
            language[locale]['item.dungeonsalive.' + p['id'] + '_spawn_egg'] = name + (' Spawn Egg' if locale == 'en_us' else '-Spawn-Ei')
    for locale, names in language.items():
        path = ASSETS / 'lang' / (locale + '.json')
        merged = json.loads(path.read_text())
        merged.update(names)
        write_json(path, merged)
    write_json(ROOT / 'design/mob-language.json', language)

def main(names_only=False):
    rows = profiles()
    if names_only:
        update_names(rows)
        print(f'Merged {len(rows)} actual entity and spawn-egg names in German and English; no model, texture or loot changes.')
        return
    language = {'en_us': {}, 'de_de': {}}
    for p in rows:
        ident = p['id']
        atlas(p)
        write_json(ASSETS / 'models/item' / (ident + '_spawn_egg.json'), {'parent': 'minecraft:item/template_spawn_egg'})
        for locale, name in [('en_us', p['name_en']), ('de_de', p['name_de'])]:
            language[locale]['entity.dungeonsalive.' + ident] = name
            language[locale]['item.dungeonsalive.' + ident + '_spawn_egg'] = name + (' Spawn Egg' if locale == 'en_us' else '-Spawn-Ei')
        common = {'ILLAGER': 'emerald', 'SKELETON': 'bone', 'WRAITH': 'phantom_membrane',
                  'GOLEM': 'redstone', 'PLANT': 'vine', 'ENDERLING': 'ender_pearl',
                  'CAULDRON': 'iron_ingot', 'GUARDIAN': 'prismarine_shard', 'WILDFIRE': 'blaze_rod',
                  'HORSEMAN': 'bone', 'SLIME': 'slime_ball', 'ZOMBIE': 'rotten_flesh',
                  'PIGLIN': 'gold_nugget', 'CREEPER': 'gunpowder'}[p['family']]
        amount = {'REGULAR': (0, 2), 'MINIBOSS': (2, 4), 'BOSS': (5, 10)}[p['tier']]
        pool = {'rolls': 1, 'conditions': [{'condition': 'minecraft:killed_by_player'}], 'entries': [{
            'type': 'minecraft:item', 'name': 'minecraft:' + common,
            'functions': [{'function': 'minecraft:set_count', 'count': {'type': 'minecraft:uniform', 'min': amount[0], 'max': amount[1]}}]}]}
        pools = [pool]
        if p['tier'] == 'BOSS':
            pools.append({'rolls': 1, 'conditions': [{'condition': 'minecraft:killed_by_player'}], 'entries': [{
                'type': 'minecraft:item', 'name': 'minecraft:emerald',
                'functions': [{'function': 'minecraft:set_count', 'count': {'type': 'minecraft:uniform', 'min': 6, 'max': 12}}]}]})
        signature_gear = {'redstone_monstrosity': 'heartstealer', 'tempest_golem': 'stormlander',
                          'nameless_one': 'suns_grace', 'wretched_wraith': 'frost_scythe',
                          'jungle_abomination': 'whirlwind', 'mooshroom_monstrosity': 'fighters_bindings'}
        if ident in signature_gear:
            pools.append({'rolls': 1, 'conditions': [{'condition': 'minecraft:killed_by_player'},
                           {'condition': 'minecraft:random_chance', 'chance': 0.35}],
                          'entries': [{'type': 'minecraft:item', 'name': 'dungeonsalive:' + signature_gear[ident]}]})
        write_json(DATA / 'loot_table/entities' / (ident + '.json'), {'type': 'minecraft:entity', 'pools': pools})
    orb = []
    for y in range(16):
        for x in range(16):
            distance = math.hypot(x - 7.5, y - 7.5)
            opacity = 0 if distance > 7 else int(255 * (1 - distance / 8) ** .5)
            orb.append([150 + int(max(0, 80 - distance * 10)), 239, 250, opacity])
    png(ASSETS / 'textures/entity/soul_bolt.png', 16, 16, orb)
    write_json(ROOT / 'design/implemented-mobs.json', {'schema_version': 1, 'generated_from': 'MobProfiles.java',
                'status': 'playable_prototypes_see_validation_report', 'mobs': rows})
    write_json(ROOT / 'design/mob-language.json', language)
    update_names(rows)
    write_json(ROOT / 'src/main/resources/data/minecraft/tags/entity_type/can_breathe_under_water.json',
               {'replace': False, 'values': ['dungeonsalive:ancient_guardian', 'dungeonsalive:drowned_necromancer', 'dungeonsalive:wavewhisperer']})
    print(f'Generated {len(rows)} own atlases, eggs and loot tables; profile and language manifests.')


if __name__ == '__main__':
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--names-only', action='store_true', help='Merge registered names into shipped languages without touching models or textures')
    main(names_only=parser.parse_args().names_only)
