#!/usr/bin/env python3
"""Make labelled technical contact sheets exclusively from completed native PNGs."""
import argparse
import hashlib
import json
from pathlib import Path
from PIL import Image, ImageDraw, ImageFont
ROOT=Path(__file__).resolve().parents[1]
FONT='/usr/share/fonts/truetype/dejavu/DejaVuSans.ttf'
BOLD='/usr/share/fonts/truetype/dejavu/DejaVuSans-Bold.ttf'

def main():
    ap=argparse.ArgumentParser(description=__doc__)
    for n in ('model','feature','camp'):ap.add_argument('--'+n+'-run',type=Path,required=True)
    args=ap.parse_args(); runs={n:getattr(args,n+'_run').resolve()for n in ('model','feature','camp')}
    reports={n:json.loads((p/'manifest.json').read_text())for n,p in runs.items()}
    if any(d.get('complete')is not True or d.get('failure')for d in reports.values()):raise ValueError('Only completed captures')
    out=ROOT/'reports/native-v04';out.mkdir(parents=True,exist_ok=True);provenance=[];native_records={}
    def locate(group,frame):
        p=(runs[group]/frame['file']).resolve()
        if not p.is_relative_to(runs[group]) or not p.is_file():raise ValueError('Invalid native board frame path')
        native_records[p]=(group,frame)
        return p
    def path(group,predicate):
        matches=[f for f in reports[group]['captures']if predicate(f)]
        if len(matches)!=1:raise ValueError('Missing or ambiguous native board view')
        return locate(group,matches[0])
    def put(canvas,p,x,y,w,h,crop=None):
        group,frame=native_records[p]
        digest=hashlib.sha256(p.read_bytes()).hexdigest()
        if frame.get('sha256') and frame['sha256']!=digest:raise ValueError('Native board frame hash mismatch')
        original_run=frame.get('source_run',reports[group].get('run',runs[group].name))
        original_manifest=frame.get('source_manifest_sha256')
        if not original_manifest:
            sources=reports[group].get('source_runs',[])
            original_manifest=next((s['manifest_sha256'] for s in sources if s['run']==original_run),None)
            if sources and original_manifest is None:raise ValueError('Unknown original run for native board frame')
            if not sources:original_manifest=hashlib.sha256((runs[group]/'manifest.json').read_bytes()).hexdigest()
        with Image.open(p)as original:
            if crop and (original.size!=(1600,1000) or crop[0]<0 or crop[1]<0 or crop[2]>original.width or crop[3]>original.height):
                raise ValueError('Item-detail crop does not match its actual native GUI dimensions')
            im=original.convert('RGB')
            if crop:im=im.crop(crop)
            im.thumbnail((w,h),Image.Resampling.LANCZOS)
            canvas.paste(im,(x+(w-im.width)//2,y+(h-im.height)//2))
        provenance.append({'sheet':filename,'original_file':p.relative_to(ROOT).as_posix() if p.is_relative_to(ROOT) else str(p),
                           'input_run':runs[group].name,'original_source_run':original_run,
                           'original_source_file':frame.get('source_file',frame['file']),
                           'source_manifest_sha256':original_manifest,
                           'sha256':digest,'crop_pixels':crop,'display_box':[x,y,w,h],
                           'derivation':'Native screenshot only; labelled aspect-preserving resized overview. Cropped item detail only where explicitly recorded.'})
    font=ImageFont.truetype(FONT,23);small=ImageFont.truetype(FONT,19);heading=ImageFont.truetype(BOLD,39)
    subjects=[('tropical_slime','Tropenschleim'),('diamond_armored_pillager','Diamant-Plünderer'),('diamond_armored_vindicator','Diamant-Diener · Whirlwind'),('arch_illager','Arch-Illager · Stab und Orb'),('pet_baby_squid','Baby-Tintenfisch · Tentakel'),('armored_trident_drowned','Dreizack-Ertrunkener · Schwimmen'),('captured_blacksmith','Gefangener Schmied · befreit')]
    frames=[(path('model',lambda f,p=p:f['profile']==p and f['view']==('interaction'if p=='captured_blacksmith'else'movement'if p in ('pet_baby_squid','armored_trident_drowned')else'idle')),label)for p,label in subjects]
    frames +=[(path('camp',lambda f:'01-merchant' in f['file']),'Händlerlager · eigene Blockvorlage'),(path('camp',lambda f:'02-captive' in f['file']),'Gefangenenlager · eigener Ort')]
    filename='v04-neue-inhalte.png';canvas=Image.new('RGB',(1920,1475),'#10191b');d=ImageDraw.Draw(canvas)
    d.text((28,22),'DUNGEONS ALIVE  /  0.4',font=heading,fill='#e6efdf')
    d.text((30,77),'Echte Minecraft-Aufnahmen · verkleinerte vollständige Frames · gestellte Prüfszenen',font=small,fill='#adbfbb')
    for i,(p,label)in enumerate(frames):
        x=20+i%3*633;y=125+i//3*445
        put(canvas,p,x,y,614,384)
        d.text((x+8,y+395),label,font=font,fill='#e4ecdf')
    canvas.save(out/filename)
    filename='v04-ausruestung.png';canvas=Image.new('RGB',(1760,1500),'#10191b');d=ImageDraw.Draw(canvas)
    d.text((28,22),'14 GEGENSTÄNDE  /  ECHTE ITEMMODELLE',font=heading,fill='#e6efdf')
    d.text((30,78),'Native Minecraft-Handbuchansichten · zugeschnittene Modelldetails · eigene Mechanik, Quellenkunst mit Credits',font=small,fill='#adbfbb')
    language=json.loads((ROOT/'src/main/resources/assets/dungeonsalive/lang/de_de.json').read_text())
    gear=[f for f in reports['feature']['captures']if f['kind']=='native_item_gui']
    if len(gear)!=14 or len({f['item'] for f in gear})!=14:raise ValueError('Expected 14 distinct native gear views')
    for i,f in enumerate(gear):
        x=22+(i%4)*435;y=130+(i//4)*330;p=locate('feature',f)
        put(canvas,p,x,y,413,254,(800,205,1215,460))
        label=language['item.dungeonsalive.'+f['item'].split(':')[1]]
        d.text((x+9,y+267),label,font=font,fill='#e4ecdf')
    canvas.save(out/filename)
    (ROOT/'reports/v04-contact-sheet-provenance.json').write_text(json.dumps(provenance,ensure_ascii=False,indent=2)+'\n')
    print('Wrote two labelled native-frame contact sheets, with per-frame crop/hash provenance.')

if __name__=='__main__':main()
