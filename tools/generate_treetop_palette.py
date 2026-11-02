#!/usr/bin/env python3
"""Explicit MIT-derived variant palette; never replaces the source Whisperer pixels."""
import colorsys,json,hashlib
from pathlib import Path
from PIL import Image
ROOT=Path(__file__).resolve().parents[1]
manifest=json.loads((ROOT/'design/reference-models.json').read_text())
ref=next(x for x in manifest['models'] if x['id']=='whisperer')['texture']
source=ROOT/'src/main/resources/assets'/ref.replace(':','/')
im=Image.open(source).convert('RGBA');out=[]
for r,g,b,a in im.getdata():
    h,s,v=colorsys.rgb_to_hsv(r/255,g/255,b/255)
    if .18<h<.48: s=min(1,s*1.05);v*=.69
    elif .68<h<.98 and s>.2: h=.77;s=min(1,s*1.12);v*=.83
    else: v*=.86
    rgb=colorsys.hsv_to_rgb(h,s,v);out.append(tuple(round(c*255) for c in rgb)+(a,))
im.putdata(out)
dest=ROOT/'src/main/resources/assets/dungeonsalive/textures/entity/reference/goety/treetop_whisperer_adapted.png';im.save(dest)
print('Generated documented Treetop palette from source SHA256',hashlib.sha256(source.read_bytes()).hexdigest())
