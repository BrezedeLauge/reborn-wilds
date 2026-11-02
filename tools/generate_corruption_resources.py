#!/usr/bin/env python3
"""Own blighted woodland and strictly worldgen-authenticated natural-soil reservations.

No original maps or PNGs copied. Existing terrain, biome tags and language files
are not rewritten. Native textures and vanilla configured features are referenced.
"""
from pathlib import Path
import json
from generate_living_biomes import biome, placed, all_of, matching

ROOT = Path(__file__).resolve().parents[1]
DATA = ROOT / 'src/main/resources/data'
ASSETS = ROOT / 'src/main/resources/assets/dungeonsalive'


def write(path, data):
    path.parent.mkdir(parents=True, exist_ok=True)
    path.write_text(json.dumps(data, indent=2) + '\n')


def main():
    thicket = biome('blighted_thicket', temperature=.7, downfall=.85, trees='unused',
        colors=dict(fog_color='72617D', sky_color='7F8099', water_color='4F5D57', water_fog_color='283C36',
                    foliage_color='514C36', grass_color='6C6542'),
        plants=['minecraft:patch_grass_forest'], creatures=[])
    thicket['features'][9][-1] = 'dungeonsalive:corruption/thicket_trees'
    write(DATA / 'dungeonsalive/worldgen/biome/blighted_thicket.json', thicket)
    write(DATA / 'dungeonsalive/worldgen/placed_feature/corruption/thicket_trees.json',
          placed('minecraft:trees_birch_and_oak', count=4, heightmap='OCEAN_FLOOR'))
    write(DATA / 'dungeonsalive/worldgen/configured_feature/corruption/natural_soil.json', {
        'type': 'minecraft:replace_single_block', 'config': {'targets': [{
            'target': {'predicate_type': 'minecraft:block_match', 'block': 'minecraft:dirt'},
            'state': {'Name': 'dungeonsalive:reserved_natural_soil'}}]}})
    write(DATA / 'dungeonsalive/worldgen/placed_feature/corruption/natural_soil.json',
          placed('dungeonsalive:corruption/natural_soil', count=16, offset=-2,
                 predicate=all_of(matching('minecraft:dirt'), matching('minecraft:grass_block', [0, 1, 0]))))
    write(DATA / 'dungeonsalive/tags/worldgen/biome/corruption_soil.json', {'replace': False, 'values': [
        '#minecraft:is_overworld', *[{'id': 'dungeonsalive:' + name, 'required': False}
        for name in ['amber_grove', 'luminous_marsh', 'ember_crags', 'blighted_thicket']]]})
    write(DATA / 'dungeonsalive/neoforge/biome_modifier/corruption_soil.json', {
        'type': 'neoforge:add_features', 'biomes': '#dungeonsalive:corruption_soil',
        'features': ['dungeonsalive:corruption/natural_soil'], 'step': 'top_layer_modification'})
    # Own filenames only; no shared vanilla tag file is overwritten.
    write(DATA / 'dungeonsalive/tags/block/corruption_soil.json', {'replace': False,
        'values': ['dungeonsalive:reserved_natural_soil', 'dungeonsalive:blighted_grass']})
    write(DATA / 'minecraft/tags/block/mineable/shovel.json', {'replace': False,
        'values': ['#dungeonsalive:corruption_soil']})
    for block, top, side in [('reserved_natural_soil', 'dirt', 'dirt'), ('blighted_grass', 'mycelium_top', 'mycelium_side')]:
        write(ASSETS / f'blockstates/{block}.json', {'variants': {'': {'model': 'dungeonsalive:block/' + block}}})
        write(ASSETS / f'models/block/{block}.json', {'parent': 'minecraft:block/cube_bottom_top',
              'textures': {'top': 'minecraft:block/' + top, 'bottom': 'minecraft:block/dirt', 'side': 'minecraft:block/' + side}})
        write(DATA / f'dungeonsalive/loot_table/blocks/{block}.json', {'type': 'minecraft:block', 'pools': [
            {'rolls': 1, 'conditions': [{'condition': 'minecraft:survives_explosion'}],
             'entries': [{'type': 'minecraft:item', 'name': 'minecraft:dirt'}]}]})
    write(ASSETS / 'models/item/cleansing_focus.json', {'parent': 'minecraft:item/generated',
        'textures': {'layer0': 'minecraft:item/amethyst_shard'}})
    write(DATA / 'dungeonsalive/recipe/cleansing_focus.json', {'type': 'minecraft:crafting_shaped',
        'category': 'misc', 'pattern': [' A ', 'AGA', ' I '], 'key': {
            'A': {'item': 'minecraft:amethyst_shard'}, 'G': {'item': 'minecraft:gold_ingot'}, 'I': {'item': 'minecraft:iron_ingot'}},
        'result': {'id': 'dungeonsalive:cleansing_focus', 'count': 1}})
    write(ROOT / 'design/corruption.json', {
        'schema_version': 1, 'generator': 'tools/generate_corruption_resources.py', 'biome': 'dungeonsalive:blighted_thicket',
        'origin': 'Own rules, palette and placement; native Minecraft texture/feature references, no copied PNGs.',
        'behavior': 'Discovery starts a bounded saved natural-ground front. No runtime biome-ID rewrite.',
        'nature_authentication': 'Only new-chunk dirt reserved directly beneath grass by a generation feature can spread. Player edits invalidate columns.',
        'limits': {'regions_per_world': 8, 'radius_blocks': 48, 'patches_per_region': 256, 'patches_world': 2048,
                   'candidate_checks_per_20_ticks': 64, 'block_changes_per_20_ticks': 4, 'protected_columns': 65536},
        'cleanup': 'Focus stops a whole region; recorded original grass states restore only in loaded, still-owned natural cells.',
        'limits_note': 'A full protection ledger disables further growth. Cleansed anchors remain remembered; old chunks without reservations are untouched.',
        'native_validation': 'Registry loading, worldgen ordering, discovery, front rendering and multiplayer protection require native server/client checks.',
    })
    print('Generated blighted_thicket and bounded corruption resources; no existing world or shared tag files changed.')


if __name__ == '__main__':
    main()
