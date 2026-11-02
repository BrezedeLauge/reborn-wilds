#!/usr/bin/env python3
"""Build two original small camps as a separate Minecraft 1.21.1 datapack.

All block layouts are authored here; no original Minecraft/Dungeons structures
are read or copied. The small typed NBT encoder emits vanilla structure format.
"""
from pathlib import Path
import gzip
import hashlib
import json
import struct

ROOT = Path(__file__).resolve().parents[1]
PACK = ROOT / 'examples/datapacks/dungeons_camps'
DATA = PACK / 'data/dungeonsalive'

def tag(kind, value): return (kind, value)
def string(v): return tag(8, v)
def integer(v): return tag(3, v)
def byte(v): return tag(1, v)
def floating(v): return tag(5, v)
def compound(v): return tag(10, v)
def listing(kind, values): return tag(9, (kind, values))

def utf8(value):
    data = value.encode('utf-8')
    return struct.pack('>H', len(data)) + data

def payload(kind, value):
    if kind == 1: return struct.pack('>b', value)
    if kind == 2: return struct.pack('>h', value)
    if kind == 3: return struct.pack('>i', value)
    if kind == 5: return struct.pack('>f', value)
    if kind == 6: return struct.pack('>d', value)
    if kind == 8: return utf8(value)
    if kind == 9:
        subtype, values = value
        return bytes([subtype]) + struct.pack('>i', len(values)) + b''.join(payload(subtype, child) for child in values)
    if kind == 10:
        return b''.join(bytes([t]) + utf8(name) + payload(t, child) for name, (t, child) in value.items()) + b'\0'
    raise ValueError(f'Unsupported NBT tag {kind}')

def write_json(path, value):
    path.parent.mkdir(parents=True, exist_ok=True)
    path.write_text(json.dumps(value, ensure_ascii=False, indent=2) + '\n', encoding='utf-8')

class Camp:
    def __init__(self, name, size):
        self.name, self.size, self.blocks, self.entities = name, size, {}, []
    def block(self, x, y, z, name, **properties):
        assert 0 <= x < self.size[0] and 0 <= y < self.size[1] and 0 <= z < self.size[2]
        self.blocks[x, y, z] = ('minecraft:' + name, tuple(sorted((k, str(v)) for k, v in properties.items())))
    def fill(self, x1, y1, z1, x2, y2, z2, name, **properties):
        for x in range(x1, x2 + 1):
            for y in range(y1, y2 + 1):
                for z in range(z1, z2 + 1): self.block(x, y, z, name, **properties)
    def clearing(self):
        self.fill(0, 0, 0, self.size[0]-1, 0, self.size[2]-1, 'coarse_dirt')
        self.fill(0, 1, 0, self.size[0]-1, self.size[1]-1, self.size[2]-1, 'air')
        for x in range(self.size[0]):
            for z in range(self.size[2]):
                if (x*7+z*11) % 9 == 0: self.block(x, 0, z, 'gravel')
    def npc(self, ident, x, y, z, captive=False):
        data = {'id': string('dungeonsalive:' + ident), 'PersistenceRequired': byte(1),
                'NoAI': byte(0), 'Invulnerable': byte(0), 'Health': floating(16 if captive else 30),
                'Rotation': listing(5, [180., 0.]), 'Motion': listing(6, [0., 0., 0.]),
                'Tags': listing(8, ['dungeonsalive_camp_npc', 'dungeonsalive_camp_' + self.name])}
        if captive: data.update(DungeonsCivilianRescued=byte(0), DungeonsWaiting=byte(1))
        self.entities.append({'pos': listing(6, [float(x), float(y), float(z)]),
                              'blockPos': listing(3, [int(x), int(y), int(z)]), 'nbt': compound(data)})
    def save(self):
        states = sorted(set(self.blocks.values()))
        state_ids = {state: i for i, state in enumerate(states)}
        palette = []
        for name, properties in states:
            data = {'Name': string(name)}
            if properties: data['Properties'] = compound({k: string(v) for k, v in properties})
            palette.append(data)
        blocks = [{'pos': listing(3, list(pos)), 'state': integer(state_ids[value])}
                  for pos, value in sorted(self.blocks.items())]
        nbt = {'DataVersion': integer(3955), 'author': string('Dungeons Alive contributors; original generated layout'),
               'size': listing(3, list(self.size)), 'palette': listing(10, palette),
               'blocks': listing(10, blocks), 'entities': listing(10, self.entities)}
        path = DATA / 'structure/camps' / (self.name + '.nbt')
        path.parent.mkdir(parents=True, exist_ok=True)
        path.write_bytes(gzip.compress(bytes([10]) + utf8('') + payload(10, nbt), mtime=0))
        return {'id': 'dungeonsalive:camps/' + self.name, 'file': str(path.relative_to(ROOT)),
                'size': list(self.size), 'block_records_including_air': len(blocks),
                'palette_entries': len(palette), 'entity_ids': [e['nbt'][1]['id'][1] for e in self.entities],
                'sha256': hashlib.sha256(path.read_bytes()).hexdigest(), 'origin': 'own_generator_authored_layout',
                'no_original_map_or_structure_copied': True}

def merchant_camp():
    c = Camp('merchant', (9, 6, 9)); c.clearing()
    c.fill(1, 0, 2, 5, 0, 6, 'spruce_planks')
    c.fill(3, 0, 0, 4, 0, 3, 'dirt_path')
    for x in (1, 5):
        for z in (2, 6): c.fill(x, 1, z, x, 3, z, 'oak_fence', north='false', south='false', east='false', west='false', waterlogged='false')
    for x in range(1, 6):
        c.fill(x, 4, 2, x, 4, 6, 'orange_wool' if x % 2 else 'white_wool')
    for x in (2, 3, 4): c.block(x, 1, 4, 'barrel', facing='north', open='false')
    c.block(1, 1, 7, 'barrel', facing='up', open='false'); c.block(2, 1, 7, 'hay_block', axis='y')
    c.block(6, 1, 3, 'crafting_table'); c.block(6, 1, 6, 'spruce_stairs', facing='east', half='bottom', shape='straight', waterlogged='false')
    c.block(7, 1, 6, 'campfire', facing='north', lit='false', signal_fire='false', waterlogged='false')
    c.block(8, 1, 6, 'spruce_stairs', facing='west', half='bottom', shape='straight', waterlogged='false')
    c.fill(7, 1, 2, 7, 2, 2, 'oak_fence', north='false', south='false', east='false', west='false', waterlogged='false')
    c.block(7, 3, 2, 'lantern', hanging='false', waterlogged='false')
    c.block(1, 1, 1, 'potted_fern')
    c.npc('village_merchant', 3.5, 1, 5.5)
    return c

def captive_camp():
    c = Camp('captive', (11, 6, 9)); c.clearing()
    c.fill(3, 0, 3, 7, 0, 7, 'spruce_planks')
    c.fill(5, 0, 0, 5, 0, 3, 'gravel')
    for x in range(3, 8):
        for z in range(3, 8):
            if x in (3, 7) and z in (3, 7): c.fill(x, 1, z, x, 3, z, 'stripped_spruce_log', axis='y')
            elif x in (3, 7) or z in (3, 7):
                if (x, z) != (5, 3):
                    c.fill(x, 1, z, x, 3, z, 'iron_bars', north=str(x in (3, 7)).lower(), south=str(x in (3, 7)).lower(),
                           east=str(z in (3, 7)).lower(), west=str(z in (3, 7)).lower(), waterlogged='false')
            c.block(x, 4, z, 'spruce_slab', type='bottom', waterlogged='false')
    c.block(5, 1, 3, 'spruce_fence_gate', facing='north', in_wall='false', open='false', powered='false')
    c.block(2, 1, 2, 'cobblestone'); c.block(2, 2, 2, 'lantern', hanging='false', waterlogged='false')
    c.block(8, 1, 2, 'cobblestone'); c.block(8, 2, 2, 'lantern', hanging='false', waterlogged='false')
    c.block(9, 1, 5, 'crafting_table'); c.block(9, 1, 6, 'barrel', facing='west', open='false')
    c.fill(1, 1, 5, 1, 1, 6, 'brown_carpet'); c.block(1, 1, 7, 'hay_block', axis='y')
    c.block(4, 1, 6, 'hay_block', axis='y')
    c.npc('captured_blacksmith', 5.5, 1, 5.5, captive=True)
    return c

def main():
    write_json(PACK/'pack.mcmeta', {'pack': {'pack_format': 48, 'description': 'Reborn Wilds: seltene eigene Händler- und Gefangenenlager (optional; neue Chunks).'}})
    write_json(DATA/'tags/worldgen/biome/has_structure/dungeons_camps.json', {'replace': False, 'values': [
        'minecraft:plains', 'minecraft:sunflower_plains', 'minecraft:forest', 'minecraft:birch_forest', 'minecraft:meadow']})
    write_json(DATA/'worldgen/template_pool/camps/start.json', {'fallback': 'minecraft:empty', 'elements': [
        {'weight': weight, 'element': {'element_type': 'minecraft:single_pool_element', 'location': 'dungeonsalive:camps/'+name,
                                     'processors': {'processors': []}, 'projection': 'rigid'}}
        for name, weight in [('merchant', 3), ('captive', 1)]]})
    write_json(DATA/'worldgen/structure/dungeons_camp.json', {
        'type': 'minecraft:jigsaw', 'biomes': '#dungeonsalive:has_structure/dungeons_camps',
        'start_pool': 'dungeonsalive:camps/start', 'size': 0, 'start_height': {'absolute': 0},
        'project_start_to_heightmap': 'WORLD_SURFACE_WG', 'max_distance_from_center': 32,
        'use_expansion_hack': False, 'step': 'surface_structures', 'terrain_adaptation': 'beard_thin', 'spawn_overrides': {}})
    write_json(DATA/'worldgen/structure_set/dungeons_camps.json', {
        'structures': [{'structure': 'dungeonsalive:dungeons_camp', 'weight': 1}],
        'placement': {'type': 'minecraft:random_spread', 'spacing': 48, 'separation': 28, 'salt': 84623719,
                      'frequency': .5, 'exclusion_zone': {'other_set': 'minecraft:villages', 'chunk_count': 8}}})
    templates = [merchant_camp().save(), captive_camp().save()]
    report = {'schema_version': 1, 'generator': 'tools/generate_dungeons_camps.py', 'asset_license': 'MIT',
              'data_version': 3955, 'pack_format': 48, 'templates': templates,
              'runtime_status': 'generated; actual datapack registry loading/template placement require separate CampGameTests',
              'scope': 'Optional new-chunk world generation. One friendly NPC per camp. No forced hostile mobs or bosses.',
              'primary_schema_verification': {'minecraft': '1.21.1', 'neoforge': '21.1.265',
                  'installed_data': ['version.json', 'data/minecraft/worldgen/structure/pillager_outpost.json', 'data/minecraft/worldgen/structure_set/pillager_outposts.json'],
                  'installed_source_classes': ['JigsawStructure', 'SinglePoolElement', 'RandomSpreadStructurePlacement', 'StructureTemplate', 'GameTestServer', 'Main'],
                  'game_files_redistributed': False},
              'limitations': ['One-piece Jigsaw start templates; no expanding dungeon network.', 'NPC rescue does not open physical gates; players open the ordinary gate themselves.',
                              'No full original mission layout, map, chest loot or blacksmith upgrade service.', 'Spacing/frequency are generation settings, not a guaranteed real-world interval.']}
    write_json(ROOT/'design/dungeons-camps.json', report)
    print('Generated original camps:', ', '.join(t['id'] for t in templates))

if __name__ == '__main__': main()
