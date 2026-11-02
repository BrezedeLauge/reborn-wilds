#!/usr/bin/env python3
"""Check retained source art, actual dependencies and crafting/guide coverage.

This is resource validation, not a Minecraft build or a visual-fidelity approval.
"""
from __future__ import annotations

from copy import deepcopy
import hashlib
import json
from pathlib import Path
import re
import struct

import import_dungeons_gear as gear


def main():
    resources = gear.selected_resources()
    inventory = json.loads((gear.ROOT / 'design/gear-source-inventory.json').read_text())
    source_records = {row['source_path']: row for row in inventory['source_files']}
    delivered_records = {row['source_path']: row for row in inventory['delivered_files']}
    checked = []
    for source, destination in resources.items():
        original = gear.SNAPSHOTS / source
        raw = original.read_bytes()
        assert hashlib.sha256(raw).hexdigest() == source_records[source]['sha256'], source
        if destination is None:
            checked.append({'source': source, 'status': 'retained_generator_input'})
            continue
        target = gear.ASSETS / destination
        actual = target.read_bytes()
        assert hashlib.sha256(actual).hexdigest() == delivered_records[source]['sha256'], destination
        if source.endswith('.png'):
            assert actual == raw, destination
            assert actual[:8] == b'\x89PNG\r\n\x1a\n', destination
            width, height = struct.unpack('>II', actual[16:24])
            assert 0 < width <= 4096 and 0 < height <= 4096, destination
        else:
            source_model, model = json.loads(raw), json.loads(actual)
            assert model == gear.convert_model(deepcopy(source_model), resources), destination
            for field in ('elements', 'display', 'texture_size', 'gui_light'):
                assert model.get(field) == source_model.get(field), (destination, field)
            references = [(model.get('parent', ''), 'models', '.json')]
            references += [(v, 'textures', '.png') for v in model.get('textures', {}).values()]
            references += [(o['model'], 'models', '.json') for o in model.get('overrides', [])]
            for location, folder, extension in references:
                if not location or location.startswith(('#', 'minecraft:')): continue
                assert location.startswith('dungeonsalive:'), (destination, location)
                assert (gear.ASSETS / folder / (location.split(':', 1)[1] + extension)).is_file(), (destination, location)
        checked.append({'source': source, 'delivered': destination, 'status': 'passed'})
    languages = {locale: json.loads((gear.ASSETS / f'lang/{locale}.json').read_text()) for locale in ('en_us', 'de_de')}
    item_ids = [row[0] for _, row in gear.item_rows()]
    assert len(item_ids) == len(set(item_ids)), 'Duplicate registration ID in resource catalog'
    for ident in item_ids:
        recipe = json.loads((gear.DATA / f'recipe/{ident}.json').read_text())
        assert recipe['result'] == {'id': 'dungeonsalive:' + ident, 'count': 1}, ident
        assert len(recipe['pattern']) == 3 and set('ABCDE') == set(recipe['key']), ident
        advancement = json.loads((gear.DATA / f'advancement/recipes/{ident}.json').read_text())
        assert advancement['rewards']['recipes'] == ['dungeonsalive:' + ident], ident
        for locale, language in languages.items():
            assert language['item.dungeonsalive.' + ident], (ident, locale)
            assert language['item.dungeonsalive.' + ident + '.tooltip'], (ident, locale)
    # Registration enums remain authoritative; source availability does not register an item.
    java = gear.ROOT / 'src/main/java/de/laugenbreze/dungeonsalive/item/gear'
    for filename, rows in [('ArtifactKind.java', gear.ARTIFACTS), ('WeaponKind.java', gear.WEAPONS), ('RangedWeaponKind.java', gear.RANGED)]:
        ids = re.findall(r'^\s*[A-Z_]+\("([a-z_]+)"', (java / filename).read_text(), re.M)
        assert set(ids) == {row[0] for row in rows}, (filename, ids)
    armor_ids = re.findall(r'^\s*[A-Z_]+\("([a-z_]+)"', (java / 'ArmorSetKind.java').read_text(), re.M)
    assert set(armor_ids) == {row[0] for row in gear.ARMOR_SETS}, armor_ids
    report = {'status': 'passed', 'scope': 'resource_and_registration_source_validation_only',
              'minecraft_build_executed': False, 'native_visual_review': False,
              'source_files': len(checked), 'delivered_files': len(delivered_records),
              'gear_items_in_this_import': len(item_ids), 'additional_wind_horn': 1,
              'recipes_and_bilingual_entries_checked': len(item_ids), 'files': checked}
    gear.write(gear.ROOT / 'reports/gear-expansion-resources.json', report)
    print(json.dumps({key: value for key, value in report.items() if key != 'files'}))


if __name__ == '__main__': main()
