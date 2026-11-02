#!/usr/bin/env python3
"""Derive peaceful entity inventory, egg models, empty loot and names from actual Java profiles."""
from pathlib import Path
import argparse
import json
import re

ROOT = Path(__file__).resolve().parents[1]

# These are deliberate sandbox habitats, not claims about original Dungeons spawn tables.
WILDLIFE_HABITATS = {
    'wooly_cow': dict(weight=6, minCount=3, maxCount=5, schedule='day', biomes=[
        'plains', 'sunflower_plains', 'meadow']),
    'vulture': dict(weight=4, minCount=3, maxCount=5, schedule='day', biomes=[
        'desert', 'badlands', 'eroded_badlands', 'wooded_badlands',
        'savanna', 'savanna_plateau', 'windswept_savanna']),
    'wisp': dict(weight=5, minCount=3, maxCount=5, schedule='dusk_and_night', biomes=[
        'forest', 'flower_forest', 'birch_forest', 'old_growth_birch_forest',
        'dark_forest', 'swamp', 'mangrove_swamp']),
}

def write(path, value):
    path.parent.mkdir(parents=True, exist_ok=True)
    path.write_text(json.dumps(value, indent=2, ensure_ascii=False) + '\n')

def main(profiles_only=False):
    source = (ROOT/'src/main/java/de/laugenbreze/dungeonsalive/entity/peaceful/PeacefulProfiles.java').read_text()
    companion_set = re.search(r'NATURAL_COMPANION_TYPES\s*=\s*Set\.of\((.*?)\);', source, re.S)
    if companion_set is None:
        raise ValueError('Explicit natural-companion eligibility list missing')
    natural_companions = set(re.findall(r'"([a-z_]+)"', companion_set.group(1)))
    pattern = r'add\("([^"]+)", "([^"]+)", "([^"]+)", Kind\.(\w+), "([^"]+)", ([0-9.]+)F, ([0-9.]+)F, ([0-9.]+), (0x[a-f0-9]+), (0x[a-f0-9]+)\);'
    profiles = []
    for row in re.findall(pattern, source):
        ident, en, de, kind, host, width, height, speed, color, accent = row
        profiles.append(dict(id=ident, registry_id='dungeonsalive:'+ident, name_en=en, name_de=de,
            kind=kind, host=host, width=float(width), height=float(height), speed=float(speed),
            color=int(color, 16), accent=int(accent, 16),
            wildlife_eligible=kind == 'WILDLIFE' or kind == 'PET' and ident in natural_companions,
            natural_companion_eligible=kind == 'PET' and ident in natural_companions))
    if len(profiles) < 45 or len({p['id'] for p in profiles}) != len(profiles):
        raise ValueError('Established peaceful entities missing or duplicate registry IDs')
    if not natural_companions or not natural_companions <= {p['id'] for p in profiles if p['kind'] == 'PET'}:
        raise ValueError('Natural-companion eligibility references a missing or non-PET registration')
    write(ROOT/'design/peaceful-profiles.json', {'profiles':profiles,
        'notes':['Generated from PeacefulProfiles.java; these are registered entities, not merely catalog references.',
                 'Merchant services and pet abilities are Minecraft adaptations; see docs/MOBS_RUNTIME.md.',
                 'Eligible companion types remain PET registrations. Only explicit natural-origin markers enable wild groups; saved/manual/owned companions retain their pet role.']})
    if profiles_only:
        print(f'Generated {len(profiles)} peaceful profiles with {len(natural_companions)} opt-in natural companion types; other resources untouched.')
        return
    assets = ROOT/'src/main/resources/assets/dungeonsalive'
    for locale in ('en_us', 'de_de'):
        path = assets/'lang'/f'{locale}.json'
        language = json.loads(path.read_text())
        for p in profiles:
            name=p['name_en' if locale=='en_us' else 'name_de']
            language['entity.dungeonsalive.'+p['id']]=name
            language['item.dungeonsalive.'+p['id']+'_spawn_egg']=name+(' Spawn Egg' if locale=='en_us' else '-Spawn-Ei')
        write(path, language)
    for p in profiles:
        write(assets/'models/item'/f"{p['id']}_spawn_egg.json", {'parent':'minecraft:item/template_spawn_egg'})
        write(ROOT/'src/main/resources/data/dungeonsalive/loot_table/entities'/f"{p['id']}.json", {'type':'minecraft:entity','pools':[]})
    generate_wildlife_habitats(profiles)
    print(f'Generated {len(profiles)} peaceful profiles, names, eggs and loot resources.')

def generate_wildlife_habitats(profiles):
    wildlife = {p['id'] for p in profiles if p['kind'] == 'WILDLIFE'}
    if not set(WILDLIFE_HABITATS) <= wildlife:
        raise ValueError('Natural habitat references an unregistered wildlife profile')
    entries = []
    for ident, settings in WILDLIFE_HABITATS.items():
        if not 1 <= settings['minCount'] <= settings['maxCount'] <= 6:
            raise ValueError(f'Unbounded natural wildlife group: {ident}')
        tag = f'has_wildlife/{ident}'
        biome_ids = ['minecraft:' + biome for biome in settings['biomes']]
        write(ROOT/'src/main/resources/data/dungeonsalive/tags/worldgen/biome'/f'{tag}.json',
              {'replace':False, 'values':biome_ids})
        modifier_path = f'src/main/resources/data/dungeonsalive/neoforge/biome_modifier/wildlife_{ident}.json'
        write(ROOT/modifier_path, {
            'type':'neoforge:add_spawns', 'biomes':'#dungeonsalive:' + tag,
            'spawners':[{'type':'dungeonsalive:' + ident,
                         **{key:settings[key] for key in ('weight','minCount','maxCount')}}]})
        entries.append(dict(id=ident, biome_tag='dungeonsalive:' + tag,
                            biomes=biome_ids, schedule=settings['schedule'],
                            spawn_weight=settings['weight'], requested_pack_min=settings['minCount'],
                            requested_pack_max=settings['maxCount'], modifier=modifier_path))
    write(ROOT/'design/wildlife-habitats.json', {
        'schema_version':1,
        'source':'tools/generate_peaceful_resources.py',
        'status':'source_implemented_runtime_validation_pending',
        'default_enabled':True,
        'configuration':'dungeonsalive-wildlife-common.toml',
        'spawn_category':'creature',
        'habitats':entries,
        'notes':[
            'Minecraft adaptations, not extracted original spawn tables; Wisp habitat remains an explicit adaptation.',
            'Normal creature caps, spawn placement and collision checks apply. Requested pack size is not a guaranteed successful spawn count.',
            'Piggy Bank uses separate rare single-spawn rules in tools/generate_piggy_bank_resources.py and design/piggy-bank-behavior.json. Pets, key golems and merchants receive no natural wildlife biome entries.',
            'Biome modifiers add spawn opportunities without replacing terrain, structures or existing entities.'
        ]})

if __name__ == '__main__':
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--profiles-only', action='store_true', help='Update profile eligibility inventory without writing language, loot or habitat resources.')
    main(profiles_only=parser.parse_args().profiles_only)
