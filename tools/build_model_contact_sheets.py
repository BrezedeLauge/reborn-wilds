#!/usr/bin/env python3
"""Label and arrange unchanged native world screenshots for model review; no synthetic scenery."""
from pathlib import Path
from PIL import Image, ImageDraw, ImageFont
import argparse
import json
import hashlib

ROOT=Path(__file__).resolve().parents[1]

def main():
    parser=argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--run',type=Path,required=True)
    parser.add_argument('--overrides',type=Path,action='append',default=[])
    args=parser.parse_args()
    rows=json.loads((ROOT/'design/implementation-status.json').read_text())['runtime_entities']
    runs=[args.run.resolve()]+[p.resolve() for p in args.overrides]
    paths={}
    for run in runs:
        manifest=json.loads((run/'manifest.json').read_text())
        if not manifest.get('complete'):raise ValueError('Only completed native captures may be presented')
        for frame in manifest['captures']:
            if frame.get('view')=='idle':paths[frame['profile']]=run/frame['file']
    font_path='/usr/share/fonts/truetype/dejavu/DejaVuSans.ttf'
    title=ImageFont.truetype(font_path,42);label=ImageFont.truetype(font_path,23);small=ImageFont.truetype(font_path,19)
    choices=[('boss-gallery.png','DUNGEONS ALIVE  |  12 BOSSGRUNDTYPEN',[r for r in rows if r['category']=='BOSS']),
             ('creature-gallery.png','DUNGEONS ALIVE  |  KREATUREN & BEWOHNER',[next(r for r in rows if r['id']==i) for i in
              ['royal_guard','illusioner','armored_piglin','key_golem','wooly_cow','piggy_bank','pet_red_panda','pet_grey_tabby','pet_spotted_baby_pig','blacksmith','enchantsmith','tower_keeper']])]
    out=ROOT/'reports/native';out.mkdir(parents=True,exist_ok=True);provenance=[]
    for filename,heading,selected in choices:
        canvas=Image.new('RGB',(2000,2020),'#11191d');draw=ImageDraw.Draw(canvas)
        draw.text((30,26),heading,font=title,fill='#e4eedf')
        draw.text((32,86),'Echte Minecraft-Weltaufnahmen · 0.3.0-alpha · Skalierte Übersicht · Gestellte Prüfszene',font=small,fill='#abbcb6')
        for n,row in enumerate(selected):
            path=paths[row['id']];x=20+(n%3)*660;y=135+(n//3)*465
            with Image.open(path) as image:
                if image.size!=(1440,900):raise ValueError('Unexpected native capture dimensions')
                canvas.paste(image.convert('RGB').resize((640,400),Image.Resampling.LANCZOS),(x,y))
            draw.text((x+8,y+411),row['name_de'],font=label,fill='#e5eee7')
            provenance.append(dict(sheet=filename,id=row['id'],source=str(path.relative_to(ROOT)),sha256=hashlib.sha256(path.read_bytes()).hexdigest()))
        canvas.save(out/filename)
    (ROOT/'reports/contact-sheet-provenance.json').write_text(json.dumps(provenance,indent=2,ensure_ascii=False)+'\n')
    print('Wrote two labeled native-frame contact sheets; source PNGs unchanged.')

if __name__=='__main__':main()
