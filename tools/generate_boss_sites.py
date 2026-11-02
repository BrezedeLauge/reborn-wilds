#!/usr/bin/env python3
"""Generate three original, naturally placed encounter sites. Entering never starts a boss.

Only own block layouts and ordinary loot tables are emitted; no game maps/assets
are copied. All structures use one finite Jigsaw start piece in new chunks.
"""
from pathlib import Path
import gzip
import hashlib
import json

from generate_dungeons_camps import compound, integer, listing, payload, string, utf8, write_json

ROOT = Path(__file__).resolve().parents[1]
DATA = ROOT / 'src/main/resources/data/dungeonsalive'


class Site:
    def __init__(self, name, size, seal, usable_floor, chest):
        self.name, self.size, self.seal = name, size, seal
        self.usable_floor, self.chest = usable_floor, chest
        self.blocks, self.block_entities = {}, {}

    def block(self, x, y, z, name, **properties):
        assert all(0 <= value < bound for value, bound in zip((x, y, z), self.size)), (self.name, x, y, z)
        self.blocks[x, y, z] = (name if ':' in name else 'minecraft:' + name,
                               tuple(sorted((key, str(value)) for key, value in properties.items())))

    def fill(self, x1, y1, z1, x2, y2, z2, name, **properties):
        for x in range(x1, x2 + 1):
            for y in range(y1, y2 + 1):
                for z in range(z1, z2 + 1):
                    self.block(x, y, z, name, **properties)

    def clear(self, floor):
        self.fill(0, 0, 0, self.size[0] - 1, 0, self.size[2] - 1, floor)
        self.fill(1, 1, 1, self.size[0] - 2, self.size[1] - 1, self.size[2] - 2, 'air')

    def treasure(self, facing):
        self.block(*self.chest, 'chest', facing=facing, type='single', waterlogged='false')
        self.block_entities[self.chest] = {'id': string('minecraft:chest'),
            'LootTable': string('dungeonsalive:chests/boss_sites/' + self.name)}

    def approach(self):
        """A broken narrow track and cairns belong to this actual site, not unrelated fake quest markers."""
        old_end = self.size[2]
        self.size = (self.size[0], self.size[1], old_end + 16)
        center = self.size[0] // 2
        for step in range(16):
            x, z = center + (step // 5) % 2, old_end + step
            for dx in (-1, 0, 1):
                self.block(x + dx, 0, z, 'coarse_dirt' if (step + dx) % 3 else 'mossy_cobblestone')
                self.block(x + dx, 1, z, 'air')
                self.block(x + dx, 2, z, 'air')
            if step in (5, 12):
                self.block(x + 3, 0, z, 'mossy_cobblestone')
                self.block(x + 3, 1, z, 'dungeonsalive:wayfinder_stone' if step == 12 else 'cobblestone_wall',
                           **({} if step == 12 else {'up': 'true', 'north': 'none', 'east': 'none', 'south': 'none', 'west': 'none', 'waterlogged': 'false'}))

    def validate(self):
        # Every seal gets a dry, level 9x9 floor and five blocks of explicit air.
        # The existing encounter validator needs the entire widened boss footprint.
        cx, floor, cz = self.usable_floor
        for x in range(cx - 4, cx + 5):
            for z in range(cz - 4, cz + 5):
                assert self.blocks[x, floor, z][0] not in {'minecraft:air', 'minecraft:water'}
                for y in range(floor + 1, floor + 6):
                    assert self.blocks[x, y, z][0] == 'minecraft:air', (self.name, x, y, z)
        assert len(self.block_entities) == 1 and self.chest in self.block_entities
        assert all(state[0] not in {'minecraft:spawner', 'minecraft:trial_spawner', 'minecraft:jigsaw',
                                    'minecraft:command_block'} for state in self.blocks.values())
        recipe = DATA / ('recipe/' + self.seal + '.json')
        assert recipe.is_file(), 'Unknown existing encounter seal: ' + self.seal
        assert json.loads(recipe.read_text())['result']['id'] == 'dungeonsalive:' + self.seal

    def save(self):
        self.validate()
        states = sorted(set(self.blocks.values()))
        ids = {state: i for i, state in enumerate(states)}
        palette = []
        for name, properties in states:
            state = {'Name': string(name)}
            if properties:
                state['Properties'] = compound({key: string(value) for key, value in properties})
            palette.append(state)
        blocks = []
        for position, state in sorted(self.blocks.items()):
            block = {'pos': listing(3, list(position)), 'state': integer(ids[state])}
            if position in self.block_entities:
                block['nbt'] = compound(self.block_entities[position])
            blocks.append(block)
        nbt = {'DataVersion': integer(3955), 'author': string('Dungeons Alive contributors; original block layout'),
               'size': listing(3, list(self.size)), 'palette': listing(10, palette),
               'blocks': listing(10, blocks), 'entities': listing(10, [])}
        path = DATA / 'structure/boss_sites' / (self.name + '.nbt')
        path.parent.mkdir(parents=True, exist_ok=True)
        path.write_bytes(gzip.compress(bytes([10]) + utf8('') + payload(10, nbt), mtime=0))
        return {'id': 'dungeonsalive:boss_sites/' + self.name, 'file': str(path.relative_to(ROOT)),
                'size': list(self.size), 'blocks_including_air': len(blocks), 'palette_entries': len(palette),
                'sha256': hashlib.sha256(path.read_bytes()).hexdigest(), 'entities': [],
                'seal': 'dungeonsalive:' + self.seal, 'usable_floor': list(self.usable_floor),
                'clear_space': [9, 5, 9], 'chest': list(self.chest)}


def overgrown_arena():
    site = Site('overgrown_arena', (21, 10, 21), 'corrupted_cauldron_seal', (10, 0, 10), (10, 1, 18))
    site.clear('mossy_stone_bricks')
    for x in range(21):
        for z in range(21):
            if (x * 7 + z * 13) % 5 == 0:
                site.block(x, 0, z, 'dungeonsalive:ancient_bricks')
    # Open centre, broken octagonal enclosure and broad entrances.
    for x in range(2, 19):
        for z in range(2, 19):
            distance = max(abs(x - 10), abs(z - 10))
            if distance == 8 and not (8 <= x <= 12 or 8 <= z <= 12):
                height = 2 + (x * 3 + z) % 3
                site.fill(x, 1, z, x, height, z, 'mossy_stone_bricks')
                if (x + z) % 4 == 0:
                    site.block(x, height, z, 'dungeonsalive:amber_moss')
    for x in (3, 17):
        for z in (3, 17):
            site.fill(x, 1, z, x, 5, z, 'dungeonsalive:ancient_bricks')
            site.block(x, 4, z, 'dungeonsalive:carved_ancient_bricks')
            site.block(x, 6, z, 'moss_block')
            site.block(x, 7, z, 'azalea')
    for x in (3, 4, 16, 17):
        site.block(x, 1, 6, 'moss_carpet')
        site.block(x, 0, 15, 'moss_block')
        site.block(x, 1, 15, 'fern')
    site.fill(8, 0, 17, 12, 0, 19, 'dungeonsalive:carved_ancient_bricks')
    site.block(8, 1, 18, 'cauldron')
    site.block(12, 1, 18, 'potted_azalea_bush')
    for z in (1, 2, 18, 19):
        site.fill(9, 0, z, 11, 0, z, 'dungeonsalive:ancient_bricks')
    site.treasure('north')
    return site


def redstone_mineyard():
    site = Site('redstone_mineyard', (23, 12, 21), 'redstone_monstrosity_seal', (11, 0, 10), (19, 1, 15))
    site.clear('stone')
    for x in range(23):
        for z in range(21):
            if (x * 11 + z * 3) % 5 < 2:
                site.block(x, 0, z, 'cobbled_deepslate')
    site.fill(7, 0, 6, 15, 0, 14, 'polished_deepslate')
    for x in (4, 18):
        site.fill(x, 1, 2, x, 6, 2, 'stripped_spruce_log', axis='y')
        site.block(x, 5, 2, 'redstone_block')
    site.fill(4, 7, 2, 18, 7, 2, 'stripped_spruce_log', axis='x')
    for x in (5, 17):
        site.block(x, 6, 2, 'lantern', hanging='true', waterlogged='false')
    # A roofless broken mine facade, not a tunnel excavated into arbitrary terrain.
    site.fill(2, 1, 18, 20, 4, 19, 'deepslate_bricks')
    site.fill(8, 1, 18, 14, 4, 19, 'air')
    site.fill(7, 1, 18, 7, 6, 18, 'polished_basalt', axis='y')
    site.fill(15, 1, 18, 15, 5, 18, 'polished_basalt', axis='y')
    site.fill(7, 6, 18, 12, 6, 18, 'cracked_deepslate_bricks')
    for z in range(3, 18):
        site.block(3, 1, z, 'rail', shape='north_south', waterlogged='false')
    for x, z in ((2, 5), (19, 4), (20, 13), (4, 17)):
        site.block(x, 1, z, 'redstone_ore')
        site.block(x, 2, z, 'chain', axis='y', waterlogged='false')
    site.block(19, 1, 14, 'barrel', facing='up', open='false')
    site.block(20, 1, 15, 'anvil', facing='north')
    site.block(2, 1, 16, 'blast_furnace', facing='east', lit='false')
    site.treasure('west')
    return site


def sunken_shrine():
    site = Site('sunken_shrine', (21, 10, 21), 'nameless_one_seal', (10, 2, 10), (10, 3, 18))
    site.clear('mud_bricks')
    site.fill(2, 1, 2, 18, 1, 18, 'water', level='0')
    site.fill(5, 1, 5, 15, 2, 15, 'dungeonsalive:ancient_bricks')
    for z in range(21):
        site.fill(9, 1, z, 11, 2, z, 'mossy_stone_bricks')
    for x in (3, 17):
        for z in (3, 17):
            site.fill(x, 1, z, x, 5, z, 'mossy_stone_bricks')
            site.block(x, 4, z, 'dungeonsalive:carved_ancient_bricks')
            site.block(x, 6, z, 'oxidized_cut_copper')
            site.block(x, 7, z, 'soul_lantern', hanging='false', waterlogged='false')
    site.fill(7, 1, 17, 13, 2, 19, 'dungeonsalive:ancient_bricks')
    for x in (7, 13):
        site.block(x, 3, 18, 'dungeonsalive:carved_ancient_bricks')
        site.block(x, 4, 18, 'soul_lantern', hanging='false', waterlogged='false')
    for x, z in ((2, 8), (18, 7), (4, 14), (16, 11)):
        site.block(x, 0, z, 'dungeonsalive:luminous_moss')
        site.block(x, 2, z, 'lily_pad')
    site.treasure('north')
    return site


BIOMES = {
    'overgrown_arena': ['#minecraft:is_forest', '#minecraft:is_jungle', {'id': 'dungeonsalive:amber_grove', 'required': False}],
    'redstone_mineyard': ['minecraft:badlands', 'minecraft:wooded_badlands', 'minecraft:eroded_badlands',
                         'minecraft:stony_peaks', 'minecraft:jagged_peaks', 'minecraft:windswept_gravelly_hills',
                         {'id': 'dungeonsalive:ember_crags', 'required': False}],
    'sunken_shrine': ['minecraft:swamp', 'minecraft:mangrove_swamp', {'id': 'dungeonsalive:luminous_marsh', 'required': False}],
}


def loot(site):
    def item(name, count=None):
        entry = {'type': 'minecraft:item', 'name': name}
        if count:
            entry['functions'] = [{'function': 'minecraft:set_count', 'count': {
                'type': 'minecraft:uniform', 'min': count[0], 'max': count[1]}}]
        return entry
    material = {'overgrown_arena': 'minecraft:emerald', 'redstone_mineyard': 'minecraft:redstone',
                'sunken_shrine': 'minecraft:lapis_lazuli'}[site.name]
    return {'type': 'minecraft:chest', 'pools': [
        {'rolls': 1, 'entries': [item('dungeonsalive:' + site.seal)]},
        {'rolls': 1, 'entries': [item(material, (3, 8))]},
        {'rolls': 1, 'entries': [item('minecraft:bread', (2, 5))]},
    ]}


def main():
    sites = [overgrown_arena(), redstone_mineyard(), sunken_shrine()]
    templates = []
    for index, site in enumerate(sites):
        site.approach()
        templates.append(site.save())
        write_json(DATA / ('loot_table/chests/boss_sites/' + site.name + '.json'), loot(site))
        write_json(DATA / ('tags/worldgen/biome/has_structure/' + site.name + '.json'),
                   {'replace': False, 'values': BIOMES[site.name]})
        write_json(DATA / ('worldgen/template_pool/boss_sites/' + site.name + '.json'), {
            'fallback': 'minecraft:empty', 'elements': [{'weight': 1, 'element': {
                'element_type': 'minecraft:single_pool_element', 'location': 'dungeonsalive:boss_sites/' + site.name,
                'processors': {'processors': []}, 'projection': 'rigid'}}]})
        write_json(DATA / ('worldgen/structure/' + site.name + '.json'), {
            'type': 'minecraft:jigsaw', 'biomes': '#dungeonsalive:has_structure/' + site.name,
            'start_pool': 'dungeonsalive:boss_sites/' + site.name, 'size': 0, 'start_height': {'absolute': 0},
            'project_start_to_heightmap': 'WORLD_SURFACE_WG', 'max_distance_from_center': 32,
            'use_expansion_hack': False, 'step': 'surface_structures', 'terrain_adaptation': 'beard_thin',
            'spawn_overrides': {}, 'liquid_settings': 'ignore_waterlogging'})
        write_json(DATA / ('worldgen/structure_set/' + site.name + '.json'), {
            'structures': [{'structure': 'dungeonsalive:' + site.name, 'weight': 1}],
            'placement': {'type': 'minecraft:random_spread', 'spacing': 48 + index * 8, 'separation': 28 + index * 4,
                          'salt': 84624901 + index * 137, 'frequency': .6,
                          'exclusion_zone': {'other_set': 'minecraft:villages', 'chunk_count': 6}}})
    write_json(ROOT / 'design/boss-sites.json', {
        'schema_version': 1, 'generator': 'tools/generate_boss_sites.py', 'license': 'MIT',
        'origin': 'Own original block layouts; no original mission maps or external assets copied.',
        'data_version': 3955, 'templates': templates,
        'generation': 'Default mod worldgen, only new chunks; finite single-piece Jigsaw sites with beard_thin terrain adaptation.',
        'encounters': 'No saved mobs/spawners/commands. One ordinary loot chest contains one existing encounter seal; explicit use remains required.',
        'discovery': 'Each real site includes a 16-block narrow weathered approach and two cairns. The waystone reads only nearby existing loaded structure starts; it never locates or generates chunks.',
        'source_schema': ['Minecraft 1.21.1 JigsawStructure, JigsawPlacement, StructureTemplate, SinglePoolElement',
                          'Existing original generate_dungeons_camps.py typed NBT encoder'],
        'validation_limits': ['Generator checks dimensions, bounded palettes, dry 9x5x9 encounter space and registered seal recipes.',
                              'Actual registry loading, loot and collision checks are separate BossSiteGameTests.',
                              'Natural frequency, varied terrain placement and visual appearance require native exploration.'],
    })
    print('Generated three original boss sites; no automatic boss spawns: ' + ', '.join(site.name for site in sites))


if __name__ == '__main__':
    main()
