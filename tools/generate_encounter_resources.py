#!/usr/bin/env python3
"""Rebuild our own seal sprites, recipes and bounty quests from the implemented mob manifest."""
from pathlib import Path
import argparse
import json
import struct
import zlib

ROOT = Path(__file__).resolve().parents[1]
RES = ROOT / 'src/main/resources'
THEMES = {
    'redstone_golem': 'redstone_block', 'endersent': 'ender_pearl',
    'drowned_necromancer': 'heart_of_the_sea', 'wildfire': 'blaze_rod',
    'illusioner': 'lapis_block', 'pillager_raid_captain': 'crossbow',
    'vindicator_raid_captain': 'iron_axe', 'skeleton_horseman': 'bone_block',
    'rampart_captain': 'shield',
    'arch_illager': 'gold_block', 'corrupted_cauldron': 'cauldron',
    'heart_of_ender': 'ender_eye', 'mooshroom_monstrosity': 'red_mushroom_block',
    'nameless_one': 'skeleton_skull', 'redstone_monstrosity': 'redstone_block',
    'jungle_abomination': 'moss_block', 'treetop_whisperer': 'jungle_log',
    'wretched_wraith': 'packed_ice', 'tempest_golem': 'copper_block',
    'ancient_guardian': 'prismarine_crystals', 'vengeful_heart_of_ender': 'dragon_breath'
}


def json_file(path, value):
    path.parent.mkdir(parents=True, exist_ok=True)
    path.write_text(json.dumps(value, ensure_ascii=False, indent=2) + '\n', encoding='utf-8')


def png(path, accent, boss):
    """Small, original pixel seal: gemstone, brass rim and family-coloured inset."""
    size = 32
    color = ((accent >> 16) & 255, (accent >> 8) & 255, accent & 255)
    pixels = []
    for y in range(size):
        line = []
        for x in range(size):
            dx, dy = abs(x - 15.5), abs(y - 15.5)
            diamond = dx + dy
            if diamond > 14: rgba = (0, 0, 0, 0)
            elif diamond > 11: rgba = (209, 164, 75, 255) if boss else (135, 166, 178, 255)
            elif diamond > 8: rgba = (42, 36, 52, 255)
            elif dx < 2 or dy < 2 or (dx < 5 and dy < 5):
                rgba = tuple(min(255, c + (28 if x + y < 30 else 0)) for c in color) + (255,)
            else: rgba = (56, 49, 68, 255)
            line.extend(rgba)
        pixels.append(bytes([0]) + bytes(line))
    def chunk(kind, data):
        return struct.pack('!I', len(data)) + kind + data + struct.pack('!I', zlib.crc32(kind + data) & 0xffffffff)
    payload = b'\x89PNG\r\n\x1a\n' + chunk(b'IHDR', struct.pack('!2I5B', size, size, 8, 6, 0, 0, 0))
    payload += chunk(b'IDAT', zlib.compress(b''.join(pixels), 9)) + chunk(b'IEND', b'')
    path.parent.mkdir(parents=True, exist_ok=True)
    path.write_bytes(payload)


def update_names():
    from generate_mob_resources import profiles
    targets = [mob for mob in profiles() if mob['tier'] in ('BOSS', 'MINIBOSS')]
    for locale, prefix, name in [('de_de', 'Begegnungssiegel: ', 'name_de'), ('en_us', 'Encounter Seal: ', 'name_en')]:
        path = RES / f'assets/dungeonsalive/lang/{locale}.json'
        language = json.loads(path.read_text())
        for mob in targets: language['item.dungeonsalive.' + mob['id'] + '_seal'] = prefix + mob[name]
        json_file(path, language)
    print(f'Merged names for {len(targets)} registered seals without changing recipes or quests.')

def main(names_only=False):
    if names_only:
        update_names()
        return
    mobs = json.loads((ROOT / 'design/implemented-mobs.json').read_text())['mobs']
    targets = [m for m in mobs if m['tier'] != 'REGULAR']
    if {m['id'] for m in targets} != set(THEMES):
        raise SystemExit('Update the explicit thematic ingredients before adding/removing encounter profiles.')
    language = {'de_de': {}, 'en_us': {}}
    for index, mob in enumerate(targets):
        name = mob['id']
        seal = name + '_seal'
        boss = mob['tier'] == 'BOSS'
        png(RES / f'assets/dungeonsalive/textures/item/{seal}.png', mob['accent'], boss)
        json_file(RES / f'assets/dungeonsalive/models/item/{seal}.json', {
            'parent': 'minecraft:item/generated', 'textures': {'layer0': f'dungeonsalive:item/{seal}'}})
        json_file(RES / f'data/dungeonsalive/recipe/{seal}.json', {
            'type': 'minecraft:crafting_shaped', 'category': 'misc', 'pattern': ['AEA', 'ICI', 'ARA'],
            'key': {'A': {'item': 'minecraft:amethyst_shard'}, 'E': {'item': 'minecraft:emerald'},
                    'I': {'item': 'minecraft:diamond' if boss else 'minecraft:iron_ingot'},
                    'C': {'item': 'minecraft:' + THEMES[name]}, 'R': {'item': 'minecraft:redstone'}},
            'result': {'id': 'dungeonsalive:' + seal, 'count': 1}})
        json_file(RES / f'data/dungeonsalive/advancement/recipes/{seal}.json', {
            'parent': 'minecraft:recipes/root',
            'criteria': {'has_ingredient': {'trigger': 'minecraft:inventory_changed',
                'conditions': {'items': [{'items': ['minecraft:amethyst_shard']}]}},
                'has_the_recipe': {'trigger': 'minecraft:recipe_unlocked',
                    'conditions': {'recipe': 'dungeonsalive:' + seal}}},
            'requirements': [['has_ingredient', 'has_the_recipe']],
            'rewards': {'recipes': ['dungeonsalive:' + seal]}})
        json_file(RES / f'data/dungeonsalive/dungeonsalive/quests/encounters/{name}.json', {
            'schema_version': 1, 'title': 'Erinnerung: ' + mob['name_de'],
            'description': 'Eine freiwillige Herausforderung für dein nächstes Abenteuer. '
                           'Stelle das passende Begegnungssiegel her. Schleiche und benutze es auf einer freien, '
                           'ebenen Fläche. Besiege ' + mob['name_de'] + '. Nimm die Aufgabe vor dem Kampf an. '
                           'Das Siegel wird verbraucht; es gibt keine automatisch gebaute Arena.',
            'icon': 'dungeonsalive:' + seal, 'order': 100 + index,
            'prerequisites': ['dungeonsalive:a_place_to_return'],
            'objectives': [{'id': 'defeat_v1', 'type': 'kill_entity', 'target': mob['registry_id'],
                            'amount': 1, 'label': mob['name_de'] + ' besiegen'}],
            'rewards': [{'item': 'minecraft:emerald', 'count': 16 if boss else 8},
                        {'item': 'minecraft:golden_apple', 'count': 2 if boss else 1}],
            'experience': 160 if boss else 80})
        language['de_de']['item.dungeonsalive.' + seal] = 'Begegnungssiegel: ' + mob['name_de']
        language['en_us']['item.dungeonsalive.' + seal] = 'Encounter seal: ' + mob['name_en']
    json_file(ROOT / 'design/encounter-language.json', language)
    update_names()
    print(f'Generated {len(targets)} seal sprites, recipes and voluntary bounty quests.')


if __name__ == '__main__':
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--names-only', action='store_true')
    main(names_only=parser.parse_args().names_only)
