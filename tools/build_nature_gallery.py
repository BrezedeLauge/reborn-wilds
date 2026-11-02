#!/usr/bin/env python3
"""Inspect original native nature frames locally, at their recorded tick intervals.

This gallery neither interpolates frames nor certifies their visual quality. Failed
captures remain visibly failed. PNGs are referenced unchanged, beside their source
manifest; move the gallery and capture directory together if archiving them.
"""
from __future__ import annotations

import argparse
import hashlib
import json
import os
from pathlib import Path
from urllib.parse import quote

ROOT = Path(__file__).resolve().parents[1]


def payload(run: Path, output: Path) -> dict:
    run = run.resolve(strict=True)
    raw = (run / 'manifest.json').read_bytes()
    report = json.loads(raw)
    if not isinstance(report, dict) or not isinstance(report.get('frames'), list):
        raise ValueError('Expected a native nature manifest with frames')
    if not report['frames']:
        raise ValueError('No native frames were recorded; a gallery cannot substitute for a capture')
    frames, files, previous = [], set(), -1
    for frame in report['frames']:
        name = frame.get('file')
        if not isinstance(name, str) or Path(name).name != name or '\\' in name or not name.endswith('.png'):
            raise ValueError('Frame must be a PNG basename inside its capture directory')
        path = (run / name).resolve(strict=True)
        if path.parent != run or name in files:
            raise ValueError('Duplicate frame or path outside its capture directory')
        content = path.read_bytes()
        if not content.startswith(b'\x89PNG\r\n\x1a\n') or hashlib.sha256(content).hexdigest() != frame.get('sha256'):
            raise ValueError(f'Original PNG or hash does not match: {name}')
        snapshot = frame.get('server_snapshot', {})
        tick = snapshot.get('elapsed_tick')
        if type(tick) is not int or tick < previous or snapshot.get('sequence') not in ('context', 'ducks', 'birds'):
            raise ValueError('Frame timing or declared camera sequence is invalid')
        previous = tick
        files.add(name)
        frames.append({**frame, 'image': quote(os.path.relpath(path, output.parent.resolve()), safe='/')})
    return {'status': report.get('status', 'unknown'), 'error': report.get('error'),
            'cycles_complete': report.get('cycles_complete', False),
            'visual_review_performed': report.get('visual_review_performed', False),
            'artifact_sha256': report.get('artifact_sha256'),
            'manifest_sha256': hashlib.sha256(raw).hexdigest(),
            'scope': report.get('scope'), 'frames': frames}


HTML = r'''<!doctype html>
<html lang="de"><meta charset="utf-8"><meta name="viewport" content="width=device-width,initial-scale=1">
<meta http-equiv="Content-Security-Policy" content="default-src 'none'; img-src 'self' file:; style-src 'unsafe-inline'; script-src 'unsafe-inline'">
<title>Dungeons Alive · Native Naturaufnahmen</title>
<style>
:root{color-scheme:dark;font:16px/1.5 system-ui;background:#121713;color:#f4efe0}body{max-width:1440px;margin:auto;padding:24px}
h1{font-size:26px;margin:0}p{max-width:95ch}small,.muted{color:#c2c9bf}button,select{font:inherit;color:inherit;background:#29352b;border:1px solid #819476;border-radius:5px;padding:8px 14px}
button:focus-visible,select:focus-visible,input:focus-visible,a:focus-visible{outline:3px solid #f0c97e;outline-offset:3px}button:disabled{opacity:.5}
.controls{display:flex;gap:12px;align-items:center;flex-wrap:wrap;margin:18px 0}input[type=range]{flex:1;min-width:220px;accent-color:#e2bd71}
figure{margin:0;background:#080c09;border:1px solid #52604d}figure img{display:block;width:100%;height:auto}figcaption{padding:10px 14px}
.status{padding:12px;border-left:4px solid #e2bd71;background:#242c22}.failed{border-color:#ef9c8e}table{border-collapse:collapse;width:100%;font-size:14px}th,td{text-align:left;border-bottom:1px solid #394536;padding:8px}th{color:#e2bd71}.scroll{overflow:auto}code{overflow-wrap:anywhere}a{color:#f0d39b}details{margin-top:18px}
</style>
<h1>Native Naturaufnahmen</h1>
<p>Unveränderte Minecraft-Bilder einer ausdrücklich gestellten Testumgebung. Die Wiedergabe verwendet die aufgezeichneten Tickabstände; zwischen den Aufnahmen fehlen Bilder. Sie ist kein kontinuierliches Video und keine automatische Grafikfreigabe.</p>
<p id="status" class="status"></p><p id="scope" class="muted"></p>
<div class="controls"><button id="previous" aria-label="Vorheriges Bild">Zurück</button><button id="play">Abspielen</button><button id="next" aria-label="Nächstes Bild">Weiter</button>
<label>Ansicht <select id="sequence"><option value="all">Alle Aufnahmen</option><option value="context">Hainübersicht</option><option value="ducks">Entenfamilie</option><option value="birds">Vogelrast</option></select></label>
<input id="position" type="range" min="0" value="0" aria-label="Aufgenommenes Bild auswählen"></div>
<figure><a id="original" target="_blank" rel="noopener"><img id="frame" alt="Unveränderte Aufnahme aus dem Minecraft-Client"></a><figcaption id="caption"></figcaption></figure>
<p id="cut" class="muted"></p><div class="scroll"><table><thead><tr><th>Tier / Identität</th><th>Serverzustand</th><th>Kontakt</th><th>Position</th><th>Weg / größter Tickschritt</th><th>Clientzustand / im Bildkegel*</th></tr></thead><tbody id="animals"></tbody></table></div>
<small>*Der geometrische Bildkegel berücksichtigt keine verdeckenden Blöcke. Sichtbarkeit und Bewegung müssen an den Originalbildern beurteilt werden.</small>
<details><summary>Nachweise dieser Aufnahme</summary><p>JAR: <code id="jar"></code></p><p>Quellmanifest: <code id="manifest"></code></p><p>Originalbild: <code id="hash"></code></p><p>Diese Galerie schreibt weder das Aufnahmeprotokoll noch einen Abnahmestatus um.</p></details>
<script id="capture-data" type="application/json">__DATA__</script>
<script>
'use strict';
const data=JSON.parse(document.getElementById('capture-data').textContent), el=id=>document.getElementById(id);
let frames=data.frames,index=0,timer=null,playing=false;
const names={context:'Hainübersicht',ducks:'Entenfamilie',birds:'Vogelrast'};
el('status').textContent=`Aufnahmestatus: ${data.status}. Erfasste Abläufe vollständig: ${data.cycles_complete?'ja':'nein'}. Visuelle Prüfung im Quellmanifest: ${data.visual_review_performed?'ja':'nein'}.`+(data.error?' Fehler: '+data.error:'');
if(data.status!=='captured'||!data.cycles_complete)el('status').classList.add('failed');
el('scope').textContent=data.scope||'';el('jar').textContent=data.artifact_sha256||'nicht angegeben';el('manifest').textContent=data.manifest_sha256;
const number=n=>Number(n).toFixed(2),position=p=>Array.isArray(p)?p.map(number).join(', '):'–';
function stop(){playing=false;clearTimeout(timer);el('play').textContent='Abspielen';}
function show(){
 const f=frames[index],s=f.server_snapshot,prior=index?frames[index-1]:null;
 el('frame').src=f.image;el('original').href=f.image;el('position').max=frames.length-1;el('position').value=index;
 el('caption').textContent=`${names[s.sequence]} · Bild ${index+1}/${frames.length} · Tick ${s.elapsed_tick} (${number(s.elapsed_tick/20)} s Spielzeit) · ${f.file}`;
 el('cut').textContent=prior&&prior.server_snapshot.sequence!==s.sequence?'Geplanter Kameraschnitt; keine durchgehende Kamera.':`Feste Kamera dieser Sequenz: ${position(f.client_camera)}. Das nächste Bild wird erst bei seinem aufgezeichneten Tick angezeigt.`;
 el('hash').textContent=f.sha256;el('previous').disabled=index===0;el('next').disabled=index===frames.length-1;
 el('animals').replaceChildren();
 for(const a of s.animals||[]){
  const client=(f.client_entities||[]).find(c=>c.uuid===a.uuid),row=document.createElement('tr');
  const values=[`${a.species}${a.duck_young?' · Jungtier':''} / ${a.uuid.slice(0,8)}`,a.activity,
   `${a.on_ground?'Boden':'kein Boden'} / ${a.in_water?'Wasser':'trocken'}`,position(a.position),
   `${number(a.distance_travelled)} / ${number(a.maximum_step)}`,
   client?`${client.activity} / ${client.centre_inside_camera_frustum_not_occlusion_test?'ja':'nein'}`:'nicht im Client erfasst'];
  for(const value of values){const cell=document.createElement('td');cell.textContent=value;row.append(cell);}el('animals').append(row);
 }
}
function schedule(){
 if(!playing)return;if(index>=frames.length-1){stop();return;}
 const delay=Math.max(20,(frames[index+1].server_snapshot.elapsed_tick-frames[index].server_snapshot.elapsed_tick)*50);
 timer=setTimeout(()=>{index++;show();schedule();},delay);
}
el('play').onclick=()=>{if(playing){stop();return;}if(index===frames.length-1)index=0;playing=true;el('play').textContent='Pause';show();schedule();};
el('previous').onclick=()=>{stop();index=Math.max(0,index-1);show();};el('next').onclick=()=>{stop();index=Math.min(frames.length-1,index+1);show();};
el('position').oninput=()=>{stop();index=Number(el('position').value);show();};
el('sequence').onchange=()=>{stop();frames=data.frames.filter(f=>el('sequence').value==='all'||f.server_snapshot.sequence===el('sequence').value);index=0;show();};
for(const option of el('sequence').options)if(option.value!=='all'&&!data.frames.some(f=>f.server_snapshot.sequence===option.value))option.disabled=true;
el('frame').onerror=()=>{stop();el('cut').textContent='Originalbild nicht erreichbar. Die Aufnahme darf nicht durch ein Ersatzbild ersetzt werden.';};show();
</script></html>'''


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--run', required=True, type=Path)
    parser.add_argument('--output', type=Path, default=ROOT / 'reports/nature-gallery.html')
    args = parser.parse_args()
    data = payload(args.run, args.output)
    encoded = json.dumps(data, ensure_ascii=False).replace('<', '\\u003c').replace('&', '\\u0026')
    args.output.parent.mkdir(parents=True, exist_ok=True)
    args.output.write_text(HTML.replace('__DATA__', encoded), encoding='utf-8')
    print(json.dumps({'gallery': str(args.output), 'frames': len(data['frames']),
                      'source_status': data['status'], 'visual_approval_added': False}))


if __name__ == '__main__':
    main()
