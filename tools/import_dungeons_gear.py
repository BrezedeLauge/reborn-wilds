#!/usr/bin/env python3
"""Import a pinned, selected MIT art subset; all later runs work from retained snapshots.

Only texture resource locations are changed in item models. Pixel bytes, element
coordinates, UVs and display transforms stay as authored by the upstream project.
This does not import the foreign mod binary or its gameplay implementation.
"""
from __future__ import annotations

import argparse
import hashlib
import json
from pathlib import Path
import shutil
import subprocess

ROOT = Path(__file__).resolve().parents[1]
REVISION = '9ba0075c5b2f13ef54ec914ad1327a853ecdce6f'
PROJECT = 'https://github.com/Infamous-Misadventures/Dungeons-Gear'
BASE = 'src/main/resources/assets/dungeons_gear'
SNAPSHOTS = ROOT / 'third_party/dungeons_gear/snapshots'
ASSETS = ROOT / 'src/main/resources/assets/dungeonsalive'
DATA = ROOT / 'src/main/resources/data/dungeonsalive'

# id, source id, English/German name, English/German behavior, ingredients.
ARTIFACTS = [
 ('boots_of_swiftness','boots_of_swiftness','Boots of Swiftness','Stiefel der Schnelligkeit',
  'Speed III for 5 seconds.','Schnelligkeit III für 5 Sekunden.', ['feather','sugar','iron_boots','sugar','emerald']),
 ('death_cap_mushroom','death_cap_mushroom','Death Cap Mushroom','Todeshutpilz',
  'Haste IV and Speed II for 9 seconds.','Eile IV und Schnelligkeit II für 9 Sekunden.', ['red_mushroom','brown_mushroom','emerald','sugar','blaze_powder']),
 ('iron_hide_amulet','iron_hide_amulet','Iron Hide Amulet','Eisenhautamulett',
  'Resistance II for 10 seconds.','Resistenz II für 10 Sekunden.', ['iron_block','leather','iron_chestplate','leather','emerald']),
 ('shock_powder','shock_powder','Shock Powder','Schockpulver',
  'Slows and weakens visible nearby hostile mobs for 4 seconds.','Bremst und schwächt sichtbare Gegner im Umkreis für 4 Sekunden.', ['gunpowder','glowstone_dust','amethyst_shard','blaze_powder','emerald']),
 ('gong_of_weakening','gong_of_weakening','Gong of Weakening','Gong der Schwächung',
  'Weakens visible nearby foes. Player damage against marked foes rises by 25% for 7 seconds.',
  'Schwächt sichtbare Gegner. Spielerschaden gegen markierte Gegner steigt 7 Sekunden lang um 25%.', ['gold_ingot','iron_bars','bell','iron_bars','amethyst_shard']),
 ('harvester','harvester','Harvester','Seelenernter',
  'Soul burst: damages visible hostile mobs within 5 blocks.','Seelenstoß: schädigt sichtbare Gegner in 5 Blöcken Umkreis.', ['soul_sand','amethyst_shard','book','ghast_tear','diamond']),
 ('corrupted_beacon','corrupted_beacon','Corrupted Beacon','Verdorbenes Leuchtfeuer',
  'Hold to channel a soul beam up to 24 blocks. Solid cover stops it.',
  'Gedrückt halten: Seelenstrahl bis 24 Blöcke. Feste Deckung stoppt ihn.', ['obsidian','diamond','beacon','amethyst_shard','soul_sand']),
 ('lightning_rod','lightning_rod','Lightning Rod','Blitzstab',
  'Aim at a visible hostile mob to release a lightning burst. No fire or block damage.',
  'Ziele auf einen sichtbaren Gegner für einen Blitzstoß. Ohne Feuer oder Blockschaden.', ['amethyst_shard','gold_ingot','lightning_rod','gold_ingot','diamond']),
 ('soul_healer','soul_healer','Soul Healer','Seelenheiler',
  'Spend 15 souls to restore up to four hearts. No cost at full health.', 'Heilt bis zu vier Herzen für 15 Seelen. Bei voller Gesundheit keine Kosten.', ['soul_sand','gold_ingot','ghast_tear','amethyst_shard','diamond']),
 ('ghost_cloak','ghost_cloak','Ghost Cloak','Geisterumhang',
  'Speed II and Resistance I for 3 seconds. Solid blocks remain solid.', 'Schnelligkeit II und Resistenz I für 3 Sekunden. Feste Blöcke bleiben undurchdringlich.', ['phantom_membrane','ender_pearl','leather','soul_sand','emerald']),
 ('flaming_quiver','flaming_quiver','Flaming Quiver','Flammenköcher',
  'Ignites your next seven arrows. Each Multishot arrow costs one charge. Requires ammunition.', 'Entzündet deine nächsten sieben Pfeile. Jeder Multishot-Pfeil kostet eine Ladung. Benötigt Munition.', ['blaze_powder','leather','arrow','magma_cream','emerald']),
]

RANGED = [
 ('power_bow','power_bow','Power Bow','Kraftbogen',
  '+35% arrow base damage with ordinary bow draw timing.', '+35% Pfeil-Basisschaden bei normaler Bogenspannzeit.', ['diamond','string','bow','iron_ingot','emerald']),
 ('hunters_promise','hunters_promise',"Hunter's Promise",'Versprechen des Jägers',
  'Hostile hits direct up to four nearby owned wolves; 20% chance to return a paid normal arrow.', 'Gegnertreffer lenken bis zu vier eigene Wölfe in der Nähe aufs Ziel; 20% Chance auf Rückgabe eines bezahlten normalen Pfeils.', ['bone','string','bow','leather','emerald']),
 ('auto_crossbow','auto_crossbow','Auto Crossbow','Automatische Armbrust',
  'Half the normal loading time. Each shot still requires ammunition.', 'Halbe normale Ladezeit. Jeder Schuss benötigt weiterhin Munition.', ['redstone','iron_ingot','crossbow','string','diamond']),
 ('exploding_crossbow','exploding_crossbow','Exploding Crossbow','Explodierende Armbrust',
  'Arrows burst against visible hostile mobs without destroying blocks.', 'Pfeile detonieren gegen sichtbare Gegner, ohne Blöcke zu zerstören.', ['gunpowder','iron_ingot','crossbow','blaze_powder','diamond']),
]
ARMOR_SETS = [
 ('wolf', 'Wolf', 'Wolfs', 'leather', 'bone'),
 ('spider', 'Spider', 'Spinnen', 'leather', 'spider_eye'),
 ('battle_robes', 'Battle Robes', 'Kampfroben', 'leather', 'lapis_lazuli'),
]
ARMOR = []
for set_id, en, de, material, accent in ARMOR_SETS:
    for piece, piece_en, piece_de in [('helmet','Hood','Kapuze'), ('chestplate','Chestpiece','Bruststück'),
                                      ('leggings','Leggings','Hose'), ('boots','Boots','Stiefel')]:
        ident = set_id + '_' + piece
        ARMOR.append((ident, ident, en + ' ' + piece_en, de + '-' + piece_de,
                      'Wear all four matching pieces to activate the set bonus.',
                      'Trage alle vier passenden Teile für den Setbonus.',
                      [accent, material, 'iron_' + piece, material, 'emerald']))


def item_rows():
    return [('artifact', row) for row in ARTIFACTS] + [('weapon', row) for row in WEAPONS] + [
        ('ranged', row) for row in RANGED] + [('armor', row) for row in ARMOR]


def selected_resources():
    """Exact source -> delivered path map, also used to resolve model dependencies."""
    files = {}
    for kind, row in item_rows():
        ident, original = row[:2]
        generated = 'src/generated/resources/assets/dungeons_gear'
        model = f'{generated if kind == "armor" else BASE}/models/item/{original}.json'
        texture = f'{BASE}/textures/item/{"armor/" if kind == "armor" else ""}{original}.png'
        files[model] = f'models/item/{ident}.json'
        files[texture] = f'textures/item/gear/{ident}.png'
        if kind == 'ranged':
            states = ['pulling_0', 'pulling_1', 'pulling_2']
            if 'crossbow' in ident: states += ['arrow', 'firework']
            for state in states:
                files[f'{BASE}/models/item/{original}_{state}.json'] = f'models/item/{ident}_{state}.json'
                files[f'{BASE}/textures/item/{original}_{state}.png'] = f'textures/item/gear/{ident}_{state}.png'
    for ident, *_ in ARMOR_SETS:
        files[f'{BASE}/textures/models/armor/{ident}_armor.png'] = f'textures/models/armor/{ident}_armor.png'
        # Retained source for the Java model generator, not a foreign runtime dependency.
        files[f'{BASE}/geo/armor/{ident}_armor.geo.json'] = None
    return files


def convert_model(model, resources):
    texture_names = {}; model_names = {}
    for source, destination in resources.items():
        if destination is None: continue
        if '/textures/' in source:
            texture_names['dungeons_gear:' + source.split('/textures/', 1)[1][:-4]] = 'dungeonsalive:' + destination.removeprefix('textures/')[:-4]
        if '/models/' in source and source.endswith('.json'):
            model_names['dungeons_gear:' + source.split('/models/', 1)[1][:-5]] = 'dungeonsalive:' + destination.removeprefix('models/')[:-5]
    for key, value in model.get('textures', {}).items():
        if value.startswith('dungeons_gear:'): model['textures'][key] = texture_names[value]
    parent = model.get('parent', '')
    if parent.startswith('dungeons_gear:'): model['parent'] = model_names[parent]
    elif parent.startswith('item/'): model['parent'] = 'minecraft:' + parent
    for override in model.get('overrides', []):
        if override['model'].startswith('dungeons_gear:'): override['model'] = model_names[override['model']]
    if 'dungeons_gear:' in json.dumps(model): raise ValueError('Unresolved foreign resource in model')
    return model
WEAPONS = [
 ('sword','sword','Sword','Schwert',
  '7 attack damage; 1.6 attacks per second. Supports Minecraft sweep attacks.',
  '7 Angriffsschaden; 1,6 Angriffe pro Sekunde. Unterstützt Minecrafts Schwungangriff.', ['iron_ingot','iron_ingot','iron_sword','stick','emerald']),
 ('cutlass','cutlass','Cutlass','Entermesser',
  '6 attack damage; 2 attacks per second. Supports Minecraft sweep attacks.',
  '6 Angriffsschaden; 2 Angriffe pro Sekunde. Unterstützt Minecrafts Schwungangriff.', ['iron_ingot','gold_ingot','iron_sword','stick','emerald']),
 ('daggers','dagger','Daggers','Dolche',
  '4 attack damage; 3 attacks per second. Quick single-target attacks.',
  '4 Angriffsschaden; 3 Angriffe pro Sekunde. Schnelle Einzelangriffe.', ['flint','iron_ingot','iron_sword','iron_ingot','emerald']),
 ('glaive','glaive','Glaive','Gleve',
  '8 attack damage; 1.1 attacks per second; +0.75 blocks of entity reach. Supports sweep attacks.',
  '8 Angriffsschaden; 1,1 Angriffe pro Sekunde; +0,75 Blöcke Entity-Reichweite. Unterstützt Schwungangriffe.', ['iron_ingot','stick','iron_sword','stick','emerald']),
 ('spear','spear','Spear','Speer',
  '7 attack damage; 1.2 attacks per second; +1 block of entity reach.',
  '7 Angriffsschaden; 1,2 Angriffe pro Sekunde; +1 Block Entity-Reichweite.', ['iron_ingot','stick','flint','stick','emerald']),
 ('hammer','great_hammer','Great Hammer','Großhammer',
  '11 attack damage; 0.7 attacks per second; +0.6 attack knockback.',
  '11 Angriffsschaden; 0,7 Angriffe pro Sekunde; +0,6 Angriffsrückstoß.', ['iron_block','iron_ingot','iron_axe','stick','emerald']),
 ('sickles','sickle','Sickles','Sicheln',
  '5 attack damage; 2.5 attacks per second. Supports Minecraft sweep attacks.',
  '5 Angriffsschaden; 2,5 Angriffe pro Sekunde. Unterstützt Minecrafts Schwungangriff.', ['iron_ingot','flint','iron_hoe','stick','emerald']),
 ('soul_knife','soul_knife','Soul Knife','Seelenmesser',
  '9 attack damage; 0.8 attacks per second. Collects souls; direct hostile kills yield two extra souls.',
  '9 Angriffsschaden; 0,8 Angriffe pro Sekunde. Sammelt Seelen; direkte Gegnertötungen geben zwei zusätzliche Seelen.', ['soul_sand','iron_ingot','iron_sword','amethyst_shard','emerald']),
 ('heartstealer','heartstealer','Heartstealer','Herzensbrecher',
  'Melee kills heal 9% of the defeated hostile mob\'s maximum health, up to 3 hearts.',
  'Nahkampfsiege heilen 9% der maximalen Gegnergesundheit, höchstens 3 Herzen.', ['diamond','iron_block','diamond_sword','iron_block','netherite_scrap']),
 ('stormlander','stormlander','Stormlander','Sturmländer',
  'A melee hit shocks nearby visible hostile mobs. Effect cooldown: 4 seconds.',
  'Nahkampftreffer schocken sichtbare Gegner in der Nähe. Effektpause: 4 Sekunden.', ['lightning_rod','iron_block','diamond_axe','iron_block','diamond']),
 ('fighters_bindings','fighters_binding',"Fighter's Bindings",'Kämpferfesseln',
  'Fast melee attacks: 3.2 attacks per second. Uses ordinary Minecraft attack timing.',
  'Schneller Nahkampf: 3,2 Angriffe pro Sekunde. Nutzt Minecrafts Schlagrhythmus.', ['diamond','leather','iron_block','leather','emerald']),
 ('suns_grace','suns_grace',"Sun's Grace",'Sonnengnade',
  'Hits against hostile mobs heal one heart. Effect cooldown: 5 seconds.',
  'Treffer gegen Gegner heilen ein Herz. Effektpause: 5 Sekunden.', ['gold_block','glowstone_dust','diamond_axe','glowstone_dust','diamond']),
 ('frost_scythe','frost_scythe','Frost Scythe','Frostsense',
  'Slows struck hostile mobs for 4 seconds. Effect cooldown: 1.5 seconds.',
  'Bremst getroffene Gegner für 4 Sekunden. Effektpause: 1,5 Sekunden.', ['blue_ice','diamond','iron_hoe','diamond','amethyst_shard']),
 ('whirlwind','whirlwind','Whirlwind','Wirbelwind',
  'Melee hits sweep through nearby visible hostile mobs. Effect cooldown: 2.5 seconds.',
  'Nahkampftreffer erfassen weitere sichtbare Gegner in der Nähe. Effektpause: 2,5 Sekunden.', ['diamond','iron_block','diamond_axe','iron_block','feather']),
]


def write(path: Path, data) -> None:
    path.parent.mkdir(parents=True, exist_ok=True)
    path.write_text(json.dumps(data, ensure_ascii=False, indent=2) + '\n', encoding='utf-8')


def info(path: Path) -> dict:
    return {'file': path.relative_to(ROOT).as_posix(), 'bytes': path.stat().st_size,
            'sha256': hashlib.sha256(path.read_bytes()).hexdigest()}


def main() -> None:
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--source', type=Path, help='Initial upstream clone at the pinned commit')
    args = parser.parse_args()
    resources = selected_resources()
    selected = sorted(resources)
    if args.source:
        revision = subprocess.check_output(['git', '-C', str(args.source), 'rev-parse', 'HEAD'], text=True).strip()
        if revision != REVISION:
            raise SystemExit(f'Unexpected source revision {revision}; expected {REVISION}')
        for relative in selected:
            source = args.source / relative
            if not source.is_file(): raise SystemExit(f'Missing selected source: {relative}')
            destination = SNAPSHOTS / relative
            destination.parent.mkdir(parents=True, exist_ok=True)
            shutil.copyfile(source, destination)
        shutil.copyfile(args.source / 'LICENSE.txt', ROOT / 'third_party/dungeons_gear/LICENSE.txt')

    source_files = []
    for relative in selected:
        path = SNAPSHOTS / relative
        if not path.is_file(): raise SystemExit(f'Missing snapshot: {path}; run once with --source')
        source_files.append(info(path) | {'source_path': relative, 'source_url': f'{PROJECT}/blob/{REVISION}/{relative}',
                                         'modification': 'unchanged_source_bytes'})
    delivered = []
    for relative, destination in resources.items():
        if destination is None: continue
        source = SNAPSHOTS / relative
        target = ASSETS / destination
        if relative.endswith('.json'):
            write(target, convert_model(json.loads(source.read_text()), resources))
            modification = 'resource_locations_only; geometry_uvs_display_unchanged'
        else:
            target.parent.mkdir(parents=True, exist_ok=True)
            shutil.copyfile(source, target)
            modification = 'unchanged_source_pixels'
        delivered.append(info(target) | {'source_path': relative, 'modification': modification})
    entries = []
    languages = {locale: json.loads((ASSETS / f'lang/{locale}.json').read_text()) for locale in ['en_us','de_de']}
    for kind, row in item_rows():
        ident, original, english, german, en_tip, de_tip, ingredients = row
        model = json.loads((ASSETS / f'models/item/{ident}.json').read_text())
        for locale, name, tooltip in [('en_us', english, en_tip), ('de_de', german, de_tip)]:
            languages[locale]['item.dungeonsalive.' + ident] = name
            languages[locale]['item.dungeonsalive.' + ident + '.tooltip'] = tooltip
        recipe = {'type': 'minecraft:crafting_shaped', 'category': 'equipment', 'pattern': [' A ', 'BCD', ' E '],
                  'key': {letter: {'item': 'minecraft:' + item} for letter, item in zip('ABCDE', ingredients)},
                  'result': {'id': 'dungeonsalive:' + ident, 'count': 1}}
        write(DATA / f'recipe/{ident}.json', recipe)
        write(DATA / f'advancement/recipes/{ident}.json', {
            'parent': 'minecraft:recipes/root',
            'criteria': {'material': {'trigger': 'minecraft:inventory_changed',
                                      'conditions': {'items': [{'items': ['minecraft:' + ingredients[2]]}]}},
                         'has_recipe': {'trigger': 'minecraft:recipe_unlocked',
                                        'conditions': {'recipe': 'dungeonsalive:' + ident}}},
            'requirements': [['material', 'has_recipe']], 'rewards': {'recipes': ['dungeonsalive:' + ident]}})
        entries.append({'id': ident, 'registry_id': 'dungeonsalive:' + ident, 'source_id': original,
                        'name_en': english, 'name_de': german,
                        'kind': kind,
                        'model_elements': len(model.get('elements', [])),
                        'rendering': 'source_3d_elements' if model.get('elements') else 'source_handheld_sprite',
                        'behavior': 'own_native_minecraft_adaptation', 'recipe': 'own_sandbox_recipe'})
    shared = {
        'message.dungeonsalive.soul_healer_full': ('Your health is already full.', 'Deine Gesundheit ist bereits vollständig.'),
        'message.dungeonsalive.flaming_quiver_loaded': ('Flaming quiver: %s arrows charged.', 'Flammenköcher: %s Pfeile aufgeladen.'),
        'tooltip.dungeonsalive.flaming_quiver_charges': ('Flame charges: %s / %s', 'Feuerladungen: %s / %s'),
        'tooltip.dungeonsalive.armor_full_set': ('Set bonus with four matching pieces:', 'Setbonus bei vier passenden Teilen:'),
        'tooltip.dungeonsalive.armor_set.wolf': ('+20% melee damage against hostile mobs.', '+20% Nahkampfschaden gegen Gegner.'),
        'tooltip.dungeonsalive.armor_set.spider': ('+25% attack speed; heals 6% of melee damage against hostile mobs (max. 1 heart per 0.5 seconds).',
                                                '+25% Angriffstempo; heilt 6% des Nahkampfschadens gegen Gegner (max. 1 Herz je 0,5 Sekunden).'),
        'tooltip.dungeonsalive.armor_set.battle_robes': ('+15% melee damage against hostile mobs; artifact cooldowns reduced by 20%.',
                                                      '+15% Nahkampfschaden gegen Gegner; Artefakt-Abklingzeiten um 20% verkürzt.'),
        'message.dungeonsalive.souls': ('Souls: %s / %s', 'Seelen: %s / %s'),
        'message.dungeonsalive.artifact_no_target': ('Aim at a visible hostile mob.', 'Ziele auf einen sichtbaren Gegner.'),
        'message.dungeonsalive.artifact_souls_needed': ('Requires %s souls; you have %s.', 'Benötigt %s Seelen; du hast %s.'),
        'tooltip.dungeonsalive.artifact_cooldown': ('Cooldown: %s seconds', 'Abklingzeit: %s Sekunden'),
        'tooltip.dungeonsalive.artifact_souls': ('Your soul reserve: %s / %s', 'Dein Seelenvorrat: %s / %s'),
        'tooltip.dungeonsalive.artifact_cost': ('Cost: %s souls per use', 'Kosten: %s Seelen pro Einsatz'),
        'tooltip.dungeonsalive.beacon_cost': ('Cost: %s souls every half second', 'Kosten: %s Seelen je halbe Sekunde'),
        'screen.dungeonsalive.gear': ('Equipment', 'Ausrüstung'),
        'screen.dungeonsalive.gear.short': ('Gear', 'Ausrüstung'),
        'screen.dungeonsalive.gear.artifacts': ('Artifacts', 'Artefakte'),
        'screen.dungeonsalive.gear.weapons': ('Weapons', 'Waffen'),
        'screen.dungeonsalive.gear.ranged': ('Ranged weapons', 'Fernkampfwaffen'),
        'screen.dungeonsalive.gear.armor': ('Armor', 'Rüstungen'),
        'screen.dungeonsalive.gear.detail': ('Select an item to inspect its model and abilities.', 'Wähle einen Gegenstand für Modell und Fähigkeiten.'),
        'screen.dungeonsalive.gear.souls': ('Soul artifacts collect 3 souls per hostile kill. Capacity 100; death keeps half.',
                                         'Seelenartefakte sammeln 3 Seelen pro besiegtem Gegner. Vorrat 100; bei Tod bleibt die Hälfte.'),
        'screen.dungeonsalive.gear.recipe': ('Crafting recipes are available in the recipe book.', 'Herstellungsrezepte stehen im Rezeptbuch.'),
    }
    for key, values in shared.items():
        for locale, value in zip(['en_us', 'de_de'], values): languages[locale][key] = value
    for locale, data in languages.items(): write(ASSETS / f'lang/{locale}.json', data)

    # Vanilla 1.21 enchantments select supported items through tags, not Java inheritance.
    tags = {'swords': [r[0] for r in WEAPONS],
            'bows': [r[0] for r in RANGED if 'crossbow' not in r[0]],
            'crossbows': [r[0] for r in RANGED if 'crossbow' in r[0]],
            'enchantable/bow': [r[0] for r in RANGED if 'crossbow' not in r[0]],
            'enchantable/crossbow': [r[0] for r in RANGED if 'crossbow' in r[0]],
            'enchantable/durability': [r[0] for r in WEAPONS + RANGED + ARMOR]}
    for slot, suffix in [('head', 'helmet'), ('chest', 'chestplate'), ('leg', 'leggings'), ('foot', 'boots')]:
        values = [r[0] for r in ARMOR if r[0].endswith('_' + suffix)]
        tags[slot + '_armor'] = values
        tags['enchantable/' + slot + '_armor'] = values
    tags['enchantable/armor'] = [r[0] for r in ARMOR]
    tags['enchantable/equippable'] = [r[0] for r in ARMOR]
    for tag, ids in tags.items():
        write(DATA.parent / f'minecraft/tags/item/{tag}.json', {'replace': False, 'values': ['dungeonsalive:' + ident for ident in ids]})

    # New optional branch; existing quest identifiers and objective meanings are preserved.
    quests = [
        ('relic_supplies', 'Relikte des Weges', 'Sammle Materialien für die ersten Artefakte. Deine Vorräte bleiben erhalten.',
         'boots_of_swiftness', 'a_place_to_return', [('feather',8,'Federn im Inventar'),('sugar',8,'Zucker im Inventar'),('emerald',4,'Smaragde im Inventar')], 40),
        ('relic_soulcraft', 'Die Sprache der Seelen', 'Erkunde den Nether und bringe Seelensand mit. Der Seelenernter lädt sich anschließend durch besiegte Gegner auf.',
         'harvester', 'relic_supplies', [('soul_sand',8,'Seelensand im Inventar'),('amethyst_shard',8,'Amethystscherben im Inventar')], 65),
        ('relic_ironhide', 'Standhalten', 'Bereite dich auf schwere Begegnungen vor. Die Materialien werden nur geprüft und bleiben bei dir.',
         'iron_hide_amulet', 'relic_supplies', [('iron_ingot',24,'Eisenbarren im Inventar'),('leather',8,'Leder im Inventar')], 50),
        ('relic_storm', 'Ein Funke im Sturm', 'Baue einen Blitzableiter und sammle die letzten Werkstoffe für den Blitzstab.',
         'lightning_rod', 'relic_soulcraft', [('lightning_rod',1,'Blitzableiter im Inventar'),('diamond',2,'Diamanten im Inventar')], 80),
    ]
    for offset, (ident, title, description, reward, prerequisite, goals, xp) in enumerate(quests):
        write(DATA / f'dungeonsalive/quests/{ident}.json', {'schema_version':1,'title':title,'description':description,
            'icon':'dungeonsalive:'+reward,'order':500+offset*10,'prerequisites':['dungeonsalive:'+prerequisite],
            'objectives':[{'id':item,'type':'collect_item','target':'minecraft:'+item,'amount':amount,'label':label}
                          for item,amount,label in goals],
            'rewards':[{'item':'dungeonsalive:'+reward,'count':1}], 'experience':xp})
    write(ROOT / 'design/gear-source-inventory.json', {
        'schema_version':1, 'source':{'id':'dungeons_gear','project':PROJECT,'revision':REVISION,'license':'MIT',
        'copyright':'Copyright (c) 2022 Patrigan','credits':['Patrigan','the_infamous_1 and Dungeons Gear contributors'],
        'license_file':'third_party/dungeons_gear/LICENSE.txt'},
        'source_files':source_files, 'delivered_files':delivered, 'items':entries,
        'counts':{'artifacts':len(ARTIFACTS),'weapons':len(WEAPONS),'ranged_weapons':len(RANGED),
                  'armor_sets':len(ARMOR_SETS),'armor_pieces':len(ARMOR),
                  'source_files':len(source_files),'delivered_files':len(delivered),'new_quests':len(quests)},
        'notice':'Published community art, not claimed to be original game-extracted files. Native behavior, recipes, cooldowns and soul accounting are this project\'s adaptation.'})
    print(json.dumps({'items':len(entries),'snapshots':len(source_files),'models_and_textures':len(delivered),'quests':len(quests)}))


if __name__ == '__main__': main()
