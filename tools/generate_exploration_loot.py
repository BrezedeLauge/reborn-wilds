#!/usr/bin/env python3
"""Append bounded Dungeons gear to selected real vanilla treasure tables through NeoForge GLMs."""
from pathlib import Path
import json

ROOT = Path(__file__).resolve().parents[1]
RES = ROOT / 'src/main/resources/data'
ORDINARY_TABLES = ['simple_dungeon', 'abandoned_mineshaft', 'desert_pyramid', 'jungle_temple',
                   'pillager_outpost', 'buried_treasure', 'nether_bridge']
ENDGAME_TABLES = ['woodland_mansion', 'ancient_city', 'bastion_treasure', 'bastion_bridge',
                 'bastion_hoglin_stable', 'bastion_other', 'end_city_treasure']
GEAR = ['sword', 'cutlass', 'daggers', 'glaive', 'spear', 'hammer', 'sickles', 'soul_knife',
        'power_bow', 'auto_crossbow', 'boots_of_swiftness', 'death_cap_mushroom', 'iron_hide_amulet',
        'shock_powder', 'harvester', 'soul_healer', 'ghost_cloak', 'flaming_quiver']
GEAR += [armor + '_' + piece for armor in ['wolf', 'spider', 'battle_robes']
         for piece in ['helmet', 'chestplate', 'leggings', 'boots']]
RARE = ['heartstealer', 'stormlander', 'suns_grace', 'frost_scythe', 'whirlwind', 'fighters_bindings']


def write(path, value):
    path.parent.mkdir(parents=True, exist_ok=True)
    path.write_text(json.dumps(value, indent=2) + '\n')


def main():
    assert len(GEAR) <= 32 and not set(GEAR) & set(RARE)
    assert not set(ORDINARY_TABLES) & set(ENDGAME_TABLES)
    for item in GEAR + RARE:
        recipe = RES / 'dungeonsalive/recipe' / (item + '.json')
        assert recipe.is_file(), 'Unknown ordinary registered gear recipe: ' + item
        assert json.loads(recipe.read_text())['result']['id'] == 'dungeonsalive:' + item
    entries = []
    for suffix, tables, chance, rare in [('ordinary', ORDINARY_TABLES, .30, False), ('endgame', ENDGAME_TABLES, .40, True)]:
        name = 'exploration_gear_' + suffix
        entries.append('dungeonsalive:' + name)
        modifier = {'type': 'dungeonsalive:exploration_gear', 'conditions': [{'condition': 'minecraft:any_of',
            'terms': [{'condition': 'neoforge:loot_table_id', 'loot_table_id': 'minecraft:chests/' + table} for table in tables]}],
            'chance': chance, 'items': ['dungeonsalive:' + item for item in GEAR]}
        if rare:
            modifier.update(rare_chance=.05, rare_items=['dungeonsalive:' + item for item in RARE])
        write(RES / 'dungeonsalive/loot_modifiers' / (name + '.json'), modifier)
    write(RES / 'neoforge/loot_modifiers/global_loot_modifiers.json', {'replace': False, 'entries': entries})
    print(f'Generated additive gear loot for {len(ORDINARY_TABLES) + len(ENDGAME_TABLES)} vanilla chest tables.')


if __name__ == '__main__':
    main()
