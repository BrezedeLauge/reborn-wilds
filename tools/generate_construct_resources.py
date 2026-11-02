#!/usr/bin/env python3
"""Names for the registered finite-lived spell constructs; deliberately no spawn eggs."""
from pathlib import Path
import json
ROOT=Path(__file__).resolve().parents[1]
NAMES={
    'geomancer_wall':('Geomancer Wall','Geomantenpfeiler'),
    'geomancer_bomb':('Geomancer Bomb','Geomantenbombe'),
    'ice_chunk':('Ice Chunk','Eisblock'),
    'entangle_vine':('Entangle Vine','Fesselranke'),
    'dungeon_trident':('Thrown Trident','Geworfener Dreizack'),
}
for index,locale in enumerate(['en_us','de_de']):
    path=ROOT/f'src/main/resources/assets/dungeonsalive/lang/{locale}.json'
    data=json.loads(path.read_text())
    data.update({'entity.dungeonsalive.'+key:value[index] for key,value in NAMES.items()})
    path.write_text(json.dumps(data,ensure_ascii=False,indent=2)+'\n')
print('Generated names for four spell constructs and their trident projectile; no extra creature count.')
