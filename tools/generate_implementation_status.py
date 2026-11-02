#!/usr/bin/env python3
"""Join actual registrations to the historical reference catalog without claiming missing variants."""
from pathlib import Path
from collections import Counter
import argparse
import json
import re
from generate_mob_resources import profiles

ROOT=Path(__file__).resolve().parents[1]

def read(name):
    return json.loads((ROOT/name).read_text())

def write(name, data):
    (ROOT/name).write_text(json.dumps(data, indent=2, ensure_ascii=False)+'\n')

def main(status_only=False):
    version=next(line.split('=',1)[1].strip() for line in (ROOT/'gradle.properties').read_text().splitlines() if line.startswith('mod_version='))
    hostile=profiles()
    peaceful=read('design/peaceful-profiles.json')['profiles']
    catalog=read('design/dungeons-mob-catalog.json')['entries']
    audit=read('design/model-source-audit.json')
    reference=read('design/reference-models.json')
    bindings={m['id']:m for m in reference['models']}
    assets={a['key']:a for a in reference['assets']}
    catalog_by_id={e['id']:e for e in catalog}
    source_by_id={e['catalog_id']:e for e in audit['coverage']}
    excluded={e['catalog_id'] for e in audit['coverage'] if e['coverage_status']=='excluded_sift'}
    evidence_path=ROOT/'reports/model-showcase.json'
    evidence=read('reports/model-showcase.json') if evidence_path.is_file() else {}
    captures=evidence.get('captures',[])
    builtins={'pillager_raid_captain','vindicator_raid_captain','skeleton_horseman','conjured_slime'}
    rows=[]
    for p in hostile+peaceful:
        ident=p['id']; model=bindings.get(ident)
        if model is None and ident not in builtins:raise ValueError('Registered figure missing visual binding: '+ident)
        explicit=model.get('catalog_id') if model else None
        candidates=[explicit] if explicit in catalog_by_id else [prefix+ident for prefix in ('mcd:','mcd2:') if prefix+ident in catalog_by_id]
        # The same canonical species can appear in both games. Do not claim both versions from one skin.
        matches=candidates[:1]
        if any(x in excluded for x in matches):raise ValueError('Excluded Sift entry registered: '+ident)
        source=assets[model['asset']]['source'] if model else 'installed_minecraft_composition'
        category=p.get('tier',p.get('kind'))
        frames=[c for c in captures if c['profile']==ident]
        rows.append(dict(id=ident,registry_id='dungeonsalive:'+ident,name_en=p['name_en'],name_de=p['name_de'],
            category=category,catalog_ids=matches,catalog_mapping='explicit_or_same_canonical_id' if matches else 'unresolved_no_guessed_match',
            registered=True,model_binding=model['asset'] if model else 'builtin:'+ident,visual_source=source,
            model_status='source_or_own_adaptation_present',
            animation_status='server_driven_source_or_host_adaptation; individual_original_sequences_not_all_reproduced',
            gameplay_status='minecraft_adaptation; source_specific_mechanics_partial',
            native_views=[c['view'] for c in frames],native_capture_run=evidence.get('run') if frames else None,
            native_visual_review='see reports/native-qa-*.json; capture alone is not visual approval'))
    construct_source=(ROOT/'src/main/java/de/laugenbreze/dungeonsalive/entity/construct/ConstructKind.java').read_text()
    construct_ids=re.findall(r'^\s*[A-Z_]+\("([a-z_]+)"',construct_source,re.M)
    constructs=[]
    for ident in construct_ids:
        model=bindings[ident]
        catalog_id='mcd:'+ident
        if catalog_id not in catalog_by_id:raise ValueError('Unmapped spell construct: '+ident)
        constructs.append(dict(id=ident,registry_id='dungeonsalive:'+ident,category='CONSTRUCT',registered=True,
            catalog_ids=[catalog_id],model_binding=model['asset'],visual_source=assets[model['asset']]['source'],
            gameplay_status='server_owned_timed_destructible_spell_construct',
            source_notes='No spawn egg, natural spawn or ordinary creature count; actual phase/lifetime/owner rules.'))
    coverage=[]
    for e in catalog:
        matched=[r['registry_id'] for r in rows+constructs if e['id'] in r['catalog_ids']]
        source=source_by_id[e['id']]['coverage_status']
        state='excluded_sift' if e['id'] in excluded else 'registered_adaptation' if matched else 'unimplemented_reference'
        coverage.append(dict(catalog_id=e['id'],canonical_name=e['canonical_name'],scope_and_runtime_status=state,
            runtime_ids=matched,source_availability=source,
            note='Base-rig availability, vanilla counterparts and source reuse do not complete a named Ancient/armor/palette variant.'))
    counts=dict(registered_figures=len(rows),hostile=len(hostile),peaceful=len(peaceful),
        hostile_tiers=dict(Counter(p['tier'] for p in hostile)),peaceful_kinds=dict(Counter(p['kind'] for p in peaceful)),
        reference_bindings=len(bindings),builtin_bindings=len(builtins),
        registered_spell_constructs=len(constructs),
        unregistered_model_templates=sorted(set(bindings)-{r['id'] for r in rows+constructs}),
        catalog_rows=len(catalog),excluded_sift=len(excluded),
        catalog_rows_with_registered_adaptation=sum(bool(c['runtime_ids']) for c in coverage),
        runtime_rows_without_exact_catalog_mapping=sum(not r['catalog_ids'] for r in rows))
    status=dict(schema_version=2,version=version,
        status_notice='Actual registration and selected visuals; no claim of a complete original Dungeons port.',
        historical_catalog_notice='Original catalog implementation fields are the earlier research snapshot; this join is the current runtime authority.',
        counts=counts,runtime_entities=rows,runtime_constructs=constructs,catalog_coverage=coverage)
    gear=read('design/gear-source-inventory.json')
    gear_package=ROOT/'src/main/java/de/laugenbreze/dungeonsalive/item/gear'
    for name, kind in [('ArtifactKind','artifact'),('WeaponKind','weapon'),('RangedWeaponKind','ranged')]:
        registered=set(re.findall(r'^\s*[A-Z_]+\("([a-z_]+)"', (gear_package/(name+'.java')).read_text(), re.M))
        if registered!={row['id'] for row in gear['items'] if row['kind']==kind}:
            raise ValueError('Gear source inventory differs from registration enum: '+name)
    armor_sets=set(re.findall(r'^\s*[A-Z_]+\("([a-z_]+)"', (gear_package/'ArmorSetKind.java').read_text(), re.M))
    if {set_id+'_'+piece for set_id in armor_sets for piece in ('helmet','chestplate','leggings','boots')}!={row['id'] for row in gear['items'] if row['kind']=='armor'}:
        raise ValueError('Armor source inventory differs from registered humanoid sets')
    build_report_path='reports/current-build-validation.json'
    build_report=read(build_report_path) if (ROOT/build_report_path).is_file() else None
    expansion_path='reports/living-expansion-validation.json'
    expansion=read(expansion_path) if (ROOT/expansion_path).is_file() else None
    # A retained JAR of the same version is not evidence for later source changes.
    build_passed=bool(not expansion and build_report and build_report['version']==version and build_report['build']['status']=='passed')
    expansion_server_passed=bool(expansion and expansion.get('java_compile_status')=='passed'
        and expansion.get('junit_passed',0)>0 and expansion.get('junit_passed')==expansion.get('junit_total')
        and expansion.get('native_tests_total',0)>0
        and expansion.get('native_tests_passed')==expansion.get('native_tests_total')
        and expansion.get('native_tests_failed')==0 and expansion.get('native_optional_failures')==0)
    status['gear']={'status':('registered_direct_java_and_native_server_passed_visual_validation_pending' if expansion_server_passed
                              else 'registered_build_passed_native_server_and_visual_validation_pending' if build_passed
                              else 'registered_source_pending_build_server_and_native_client_validation'),
        'counts':{'items':len(gear['items'])+1,'artifacts':gear['counts']['artifacts']+1,
                  'melee_weapons':gear['counts']['weapons'],'ranged_weapons':gear['counts']['ranged_weapons'],
                  'armor_sets':len(armor_sets),'armor_pieces':gear['counts']['armor_pieces']},
        'runtime_items':[{'id':row['registry_id'],'kind':row['kind']} for row in gear['items']]+[{'id':'dungeonsalive:wind_horn','kind':'artifact'}],
        'source_inventory':'design/gear-source-inventory.json',
        'scope_notice':'Selected Minecraft adaptations, not all original-game items. Armor counts four separate wearable items per set; 436-row creature catalog does not measure item completeness.'}
    enchantments=re.findall(r'^\s*[A-Z_]+\("([a-z_]+)"',
        (gear_package/'enchantment/GearEnchantmentKind.java').read_text(), re.M)
    status['progression']={
        'status':('implemented_direct_java_and_native_server_passed_visual_validation_pending' if expansion_server_passed
                  else 'implemented_build_passed_native_server_and_visual_validation_pending' if build_passed
                  else 'implemented_source_pending_minecraft_build_server_and_native_client_validation'),
        'merchant_services':['blacksmith_repair','powersmith_three_weapon_ranks','enchantsmith_six_enchantments'],
        'native_enchantments':['dungeonsalive:'+ident for ident in enchantments],
        'enchantment_max_level':3,
        'resource_hud':'owner_only_server_snapshots_souls_and_flame_charges',
        'key_gate_blocks':['dungeonsalive:key_gate','dungeonsalive:diamond_key_gate'],
        'key_gate_scope':'player_built_owner_bound_matching_owned_golem_within_four_blocks_and_line_of_sight',
        'living_world':['bounded_wooly_cow_breeding_growth_parent_following','rare_piggy_bank_spawn_flee_and_single_treasure'],
        'validation_report':'reports/progression-status.json',
        'gear_specification':'docs/GEAR_AND_SOULS.md',
        'world_specification':'docs/LIVING_WORLD.md'}
    # Keep working source readiness separate from retained release/capture evidence.
    development_path='reports/development-status.json'
    if (ROOT/development_path).is_file():
        status['development_status']={'report':development_path,'status':read(development_path)['status']}
    if build_report and build_report['version']==version:
        junit=build_report['junit']
        status['validation']={
            'report':build_report_path,
            'verified_at_utc':build_report['verified_at_utc'],
            'build':{key:build_report['build'][key] for key in ('status','command','log','artifact')},
            'junit':{key:junit[key] for key in ('status','tests','passed','suite_count','failures','errors','skipped')},
            'game_test_server':{key:build_report['game_test_server'][key]
                                for key in ('status','command','log','reason','tests_executed')},
            'native_client':{
                'status_authority':'reports/creative-playtest.json',
                'scope':'Client launch and Creative-world progress are tracked separately; a running client is not native visual approval.'},
            'scope_notice':'This records the checked build artifact. Regenerating the inventory performs no build, JUnit, Minecraft GameTest or visual QA; older offline/release test counts are not added.'}
    if expansion:
        if 'validation' in status: status['historical_gradle_validation']=status.pop('validation')
        status['validation']={'report':expansion_path, **expansion}
        status['living_world_expansion']={
            'specification':'docs/LIVING_WORLD_EXPANSION.md',
            'biomes':['amber_grove','luminous_marsh','ember_crags','blighted_thicket'],
            'world_preset':'dungeonsalive:living_world',
            'existing_world_generator_preserved':True,
            'buildable_blocks':['amber_moss','luminous_moss','ancient_bricks','carved_ancient_bricks','wayfinder_stone'],
            'system_owned_soil_blocks':['reserved_natural_soil','blighted_grass'],
            'natural_hostile_species':20,'wild_companion_profiles':14,
            'boss_sites':['overgrown_arena','redstone_mineyard','sunken_shrine'],
            'features':['bounded_habitat_groups','loaded_chunk_caravans','settlement_charters','additive_chest_gear',
                        'foot_traffic_paths_and_regrowth','local_current_approximation','discovery_activated_natural_corruption_front',
                        'cleansing_focus','loaded_structure_waystones','complete_creative_inventory','own_dungeons_style_ui'],
            'counts_notice':'Existing species are reused; no new species is claimed for wild status, grouped behavior or biome assignments.'}
    write('design/implementation-status.json',status)
    if status_only:
        print('Updated implementation-status.json only; build/JUnit evidence is separate from native server and visual QA.')
        return
    sections=['BOSS','MINIBOSS','REGULAR','MERCHANT','CIVILIAN','KEY_GOLEM','WILDLIFE','PET']
    lines=[f'# Tatsächlich registrierte Kreaturen – {version}','',
        f"{len(rows)} echte Typen: {len(hostile)} Gegner und {len(peaceful)} friedliche Figuren. Farben/Rüstungen werden als Varianten gezählt, nicht als zusätzliche Spezies.",'',
        'Die Tabelle wird aus Java-Profilen und der aktiven Modellauswahl erzeugt. Sie bescheinigt eine Registrierung und ein konkretes Modell; vollständige Originalmechaniken und jede einzelne Animationssequenz bleiben gesonderte Qualitätsziele. Prüfstand: [VALIDATION.md](VALIDATION.md).','']
    for group in sections:
        selected=[r for r in rows if r['category']==group]
        lines += [f'## {group} ({len(selected)})','', '| Name | Registry-ID | Visuelle Quelle |', '| --- | --- | --- |']
        lines += [f"| {r['name_de']} | `dungeonsalive:{r['id']}` | {r['visual_source']} |" for r in selected]
        lines += ['']
    lines += [f'## Zeitlich begrenzte Zauberkonstrukte ({len(constructs)})','',
        'Diese registrierten MISC-Entities sind gesondert von den Kreaturen gezählt. Sie haben Besitzer, Ziele, feste Lebensdauern, Begrenzungen und Deckungsregeln.','',
        '| Registry-ID | Modellquelle |','| --- | --- |']
    lines += [f"| `{r['registry_id']}` | {r['visual_source']} |" for r in constructs]
    lines += ['', '## Vorlagen ohne eigene Entity','',
        ', '.join('`'+x+'`' for x in counts['unregistered_model_templates']), '',
        'Diese Modellvorlagen zählen nicht zu den registrierten Kreaturen. Mage ist eine Community-Mod-Vorlage; Imperfect Heart ist eine Szenenform.','']
    (ROOT/'docs/IMPLEMENTED_CREATURES.md').write_text('\n'.join(lines))
    write('design/animation-coverage.json',dict(schema_version=3,reviewed_on='2026-11-02',version=version,
        status_authority='design/implementation-status.json',runtime_api='docs/REFERENCE_ANIMATION_RUNTIME.md',
        counts=counts,source_semantics=['Selected community-authored clips are retained with documented coordinate/time adaptation.',
        'CEM models use their expressions and installed Minecraft host poses; these are not original Dungeons keyframe exports.',
        'Own Vengeful geometry and Treetop palette adaptation are explicitly identified.',
        'Capture and structural rig validation do not certify all original attacks, transitions, death, swimming or multiplayer behavior.'],
        actual_profiles=[{k:r[k] for k in ('id','category','visual_source','model_binding','animation_status','native_views')} for r in rows],
        villager_profession_animations=19))
    print(json.dumps(counts,indent=2))

if __name__=='__main__':
    parser=argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--status-only',action='store_true',help='Regenerate only implementation-status.json, preserving the creature document and animation coverage.')
    main(status_only=parser.parse_args().status_only)
