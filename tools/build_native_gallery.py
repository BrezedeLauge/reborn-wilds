#!/usr/bin/env python3
"""Build one offline HTML gallery from completed native Minecraft capture manifests.

    python3 tools/build_native_gallery.py \
        --run run-mob-model-showcase/captures/<complete-run> \
        --overrides run-mob-model-showcase/captures/<complete-correction-run> \
        --output reports/native-gallery.html

Only main_world_frame entries are included. A correction replaces all views of each
of its profiles; an incomplete run is never silently mixed into a completed one.
The original PNGs remain untouched. Pillow makes at-most-768x480 WebP derivatives;
the HTML embeds them, their provenance, and all UI code without external resources.
Requires Python 3.10+ and Pillow with WebP support (already used by asset tools).
"""
from __future__ import annotations

import argparse
import base64
import hashlib
import io
import json
import re
import sys
from dataclasses import dataclass
from pathlib import Path

from PIL import Image, features

ROOT = Path(__file__).resolve().parents[1]
CAPTURE_KIND = "native_minecraft_world_model_checks"
VIEW_NAMES = {"idle": "idle", "movement": "movement", "action": "action",
              "interaction": "greeting", "greeting": "greeting"}
CATEGORIES = {"BOSS": "boss", "MINIBOSS": "miniboss", "REGULAR": "enemy",
              "KEY_GOLEM": "animal", "WILDLIFE": "animal", "PET": "animal",
              "MERCHANT": "merchant", "CIVILIAN": "civilian"}
GROUP_ORDER = {name: index for index, name in enumerate(("boss", "miniboss", "enemy", "animal", "merchant", "civilian"))}


class GalleryError(ValueError):
    """A source capture cannot support a truthful, complete gallery."""


def sha256(data: bytes) -> str:
    return hashlib.sha256(data).hexdigest()


def read_json(data: bytes, label: str) -> dict:
    def bad_constant(value: str):
        raise GalleryError(f"{label}: non-finite JSON constant {value}")

    try:
        result = json.loads(data, parse_constant=bad_constant)
    except (ValueError, UnicodeError) as exc:
        raise GalleryError(f"{label}: invalid JSON: {exc}") from exc
    if not isinstance(result, dict):
        raise GalleryError(f"{label}: expected a JSON object")
    return result


def display_path(path: Path) -> str:
    try:
        return path.relative_to(ROOT).as_posix()
    except ValueError:
        return path.as_posix()


@dataclass(frozen=True)
class CaptureRun:
    directory: Path
    manifest: dict
    manifest_sha256: str
    profiles: dict[str, dict[str, dict]]

    def provenance(self) -> dict:
        return {"run": self.manifest["run"], "capture_directory": display_path(self.directory),
                "manifest_file": "manifest.json", "manifest_sha256": self.manifest_sha256,
                "complete": True, "capture_kind": CAPTURE_KIND,
                "world": self.manifest.get("world"), "staging": self.manifest.get("staging"),
                "profiles": list(self.profiles), "main_frames": sum(map(len, self.profiles.values()))}


def load_run(directory: Path, registry: dict[str, dict]) -> CaptureRun:
    directory = directory.expanduser().resolve()
    manifest_path = directory / "manifest.json"
    try:
        raw = manifest_path.read_bytes()
    except OSError as exc:
        raise GalleryError(f"Cannot read {manifest_path}: {exc}") from exc
    data = read_json(raw, str(manifest_path))
    if data.get("complete") is not True or data.get("failure"):
        raise GalleryError(f"{manifest_path}: capture run is incomplete or failed")
    if data.get("capture_kind") != CAPTURE_KIND:
        raise GalleryError(f"{manifest_path}: not a native world model capture manifest")
    if not isinstance(data.get("run"), str) or not data["run"]:
        raise GalleryError(f"{manifest_path}: missing run ID")
    declared = data.get("profiles")
    if not isinstance(declared, list) or not declared or any(
            not isinstance(item, str) or not re.fullmatch(r"[a-z0-9_]+", item) for item in declared):
        raise GalleryError(f"{manifest_path}: invalid declared profiles")
    if len(set(declared)) != len(declared):
        raise GalleryError(f"{manifest_path}: duplicate declared profiles")
    unknown = set(declared) - registry.keys()
    if unknown:
        raise GalleryError(f"{manifest_path}: profiles missing from current status manifest: {sorted(unknown)}")
    captures = data.get("captures")
    if not isinstance(captures, list) or any(not isinstance(frame, dict) for frame in captures):
        raise GalleryError(f"{manifest_path}: invalid captures list")
    main = [frame for frame in captures if frame.get("kind") == "main_world_frame"]
    planned = data.get("planned_main_frames")
    if type(planned) is not int or planned < 1 or len(main) != planned:
        raise GalleryError(f"{manifest_path}: {len(main)} actual main frames, expected {planned!r}")
    profiles: dict[str, dict[str, dict]] = {key: {} for key in declared}
    files: set[str] = set()
    for frame in main:
        profile = frame.get("profile")
        if profile not in profiles:
            raise GalleryError(f"{manifest_path}: undeclared frame profile {profile!r}")
        if frame.get("registry_id") != registry[profile]["registry_id"]:
            raise GalleryError(f"{manifest_path}: registry ID does not match {profile}")
        view = VIEW_NAMES.get(frame.get("view"))
        if registry[profile]["category"] == "CIVILIAN" and frame.get("view") == "interaction":
            view = "rescue"
        if view is None or view in profiles[profile]:
            raise GalleryError(f"{manifest_path}: unknown or duplicate view {profile}/{frame.get('view')}")
        relative = frame.get("file")
        if not isinstance(relative, str) or not relative or "\\" in relative:
            raise GalleryError(f"{manifest_path}: invalid screenshot path {relative!r}")
        path = Path(relative)
        if path.is_absolute() or ".." in path.parts or path.suffix.lower() != ".png":
            raise GalleryError(f"{manifest_path}: screenshot must be a relative PNG within its run: {relative}")
        resolved = (directory / path).resolve()
        if not resolved.is_relative_to(directory) or not resolved.is_file():
            raise GalleryError(f"{manifest_path}: missing or escaping screenshot: {relative}")
        if relative in files:
            raise GalleryError(f"{manifest_path}: duplicate screenshot file {relative}")
        files.add(relative)
        profiles[profile][view] = frame
    for profile, views in profiles.items():
        category = registry[profile]["category"]
        expected = {"idle", "movement"}
        if category in ("BOSS", "MINIBOSS", "REGULAR"):
            expected.add("action")
        elif category == "MERCHANT":
            expected.add("greeting")
        elif category == "CIVILIAN":
            expected.add("rescue")
        if set(views) != expected:
            raise GalleryError(f"{manifest_path}: {profile} has {sorted(views)}, expected {sorted(expected)}")
    return CaptureRun(directory, data, sha256(raw), profiles)


def encode_frame(run: CaptureRun, frame: dict) -> dict:
    path = (run.directory / frame["file"]).resolve()
    try:
        original = path.read_bytes()
        with Image.open(io.BytesIO(original)) as source:
            if source.format != "PNG":
                raise GalleryError(f"{path}: native screenshot must be PNG")
            original_size = source.size
            if frame.get("resolution") != list(original_size):
                raise GalleryError(f"{path}: PNG dimensions differ from manifest resolution")
            derivative = source.convert("RGB")
            derivative.thumbnail((768, 480), Image.Resampling.LANCZOS)
            buffer = io.BytesIO()
            derivative.save(buffer, format="WEBP", quality=90, method=6)
    except OSError as exc:
        raise GalleryError(f"Cannot decode screenshot {path}: {exc}") from exc
    original_hash = sha256(original)
    declared_hash = frame.get("sha256") or frame.get("original_sha256")
    if declared_hash is not None and declared_hash != original_hash:
        raise GalleryError(f"{path}: PNG hash does not match its manifest")
    webp = buffer.getvalue()
    return {"image": "data:image/webp;base64," + base64.b64encode(webp).decode("ascii"),
            "width": derivative.width, "height": derivative.height,
            "original_width": original_size[0], "original_height": original_size[1],
            "run": run.manifest["run"], "manifest_sha256": run.manifest_sha256,
            "original_file": frame["file"], "original_sha256": original_hash,
            "derivative_sha256": sha256(webp),
            "derivation": "Full frame, no crop or retouch; aspect-preserving Lanczos downscale to at most 768x480; WebP quality 90.",
            "capture": frame}


def build_payload(base: CaptureRun, corrections: list[CaptureRun], registry: dict[str, dict], status: dict) -> dict:
    selected = {key: base for key in base.profiles}
    seen_run_ids = {base.manifest["run"]}
    for correction in corrections:
        if correction.manifest["run"] in seen_run_ids:
            raise GalleryError(f"Duplicate run ID: {correction.manifest['run']}")
        seen_run_ids.add(correction.manifest["run"])
        extra = correction.profiles.keys() - selected.keys()
        if extra:
            raise GalleryError(f"Correction contains profiles absent from base run: {sorted(extra)}")
        for key in correction.profiles:
            selected[key] = correction
    figures = []
    for key, run in selected.items():
        entity = registry[key]
        group = CATEGORIES.get(entity["category"])
        if group is None:
            raise GalleryError(f"Unknown status category for {key}: {entity['category']}")
        figures.append({"id": key, "registry_id": entity["registry_id"],
                        "name_de": entity.get("name_de", key), "name_en": entity.get("name_en", key),
                        "category": entity["category"], "group": group,
                        "visual_source": entity.get("visual_source", "documented_in_source_audit"),
                        "corrected": run is not base,
                        "frames": {view: encode_frame(run, frame) for view, frame in run.profiles[key].items()}})
    figures.sort(key=lambda item: (GROUP_ORDER[item["group"]], item["name_de"].casefold(), item["id"]))
    return {"schema_version": 1, "version": status.get("version", ""),
            "capture_notice": "Actual native Minecraft world frames in an explicitly staged developer test scene. Scaled WebP views, not original-resolution PNGs; no claim of natural spawning, autonomous combat, or complete visual acceptance.",
            "runs": [run.provenance() for run in [base, *corrections]],
            "override_policy": "Later completed correction runs replace all views for their profiles.",
            "figure_count": len(figures), "frame_count": sum(len(item["frames"]) for item in figures),
            "figures": figures}


HTML = r'''<!doctype html>
<html lang="de"><head><meta charset="utf-8"><meta name="viewport" content="width=device-width,initial-scale=1">
<meta http-equiv="Content-Security-Policy" content="default-src 'none'; img-src data:; style-src 'unsafe-inline'; script-src 'unsafe-inline'; connect-src 'none'; base-uri 'none'; form-action 'none'">
<title>Dungeons Alive – echte Spielaufnahmen</title>
<style>
:root{color-scheme:dark;--bg:#101719;--panel:#182225;--edge:#344348;--text:#e9f0eb;--muted:#afbeb9;--accent:#c7ea91;--warm:#f5c085}*{box-sizing:border-box}body{margin:0;background:var(--bg);color:var(--text);font:16px/1.5 system-ui,-apple-system,BlinkMacSystemFont,"Segoe UI",sans-serif}button,input,select{font:inherit}button,select,input{border:1px solid var(--edge);border-radius:8px;background:#202c2f;color:var(--text)}button,select{cursor:pointer}button:hover,select:hover{border-color:var(--accent)}button:focus-visible,input:focus-visible,select:focus-visible,summary:focus-visible,a:focus-visible{outline:3px solid var(--accent);outline-offset:3px}header,main,footer{width:min(1440px,calc(100% - 48px));margin:auto}header{padding:44px 0 24px}.eyebrow{font-size:12px;letter-spacing:.14em;font-weight:750;color:var(--accent)}h1{font-size:clamp(30px,4vw,48px);letter-spacing:-.035em;line-height:1.12;margin:13px 0 14px}header p{max-width:1000px;color:var(--muted);margin:8px 0}strong{color:var(--text)}.notice{padding:13px 17px;border-left:3px solid var(--warm);background:#222824;border-radius:0 8px 8px 0}.stats{display:flex;gap:22px;flex-wrap:wrap;padding:18px 0 4px;color:var(--muted)}.stats b{font-size:22px;color:var(--text);margin-right:6px}.controls{position:sticky;top:0;z-index:2;background:#101719f5;border-top:1px solid var(--edge);border-bottom:1px solid var(--edge);padding:16px 0;backdrop-filter:blur(10px)}.filters{display:flex;gap:12px;flex-wrap:wrap}.search{flex:1;min-width:210px}.search input{width:100%;padding:11px 13px}select{padding:11px 13px;min-width:185px}.viewbar{display:flex;gap:7px;align-items:center;flex-wrap:wrap;margin-top:13px}.viewbar button{padding:7px 13px}.viewbar button[aria-pressed=true]{color:#172015;background:var(--accent);border-color:var(--accent);font-weight:700}.viewbar span{color:var(--muted);font-size:13px;margin-right:8px}.result{color:var(--muted);font-size:14px;margin:18px 0}.grid{display:grid;grid-template-columns:repeat(auto-fill,minmax(300px,1fr));gap:20px;padding-bottom:30px}.card{background:var(--panel);border:1px solid var(--edge);border-radius:12px;overflow:hidden}.shot{border:0;padding:0;border-radius:0;background:#0b1113;width:100%;display:block;position:relative}.shot img{display:block;width:100%;height:auto;aspect-ratio:8/5;object-fit:contain}.expand{position:absolute;right:10px;bottom:10px;background:#0d151ddd;border:1px solid #657876;border-radius:6px;padding:4px 8px;font-size:12px}.body{padding:15px 16px 17px}.chips{display:flex;gap:6px;flex-wrap:wrap;margin-bottom:9px}.chip{font-size:11px;letter-spacing:.025em;border:1px solid #4b605a;border-radius:5px;padding:2px 7px;color:var(--accent)}.corrected{border-color:#7b6550;color:var(--warm)}.card h2{font-size:19px;line-height:1.25;margin:0 0 5px}.name-en{font-size:13px;color:var(--muted);margin:0 0 9px}.registry{font:12px/1.5 ui-monospace,SFMono-Regular,Consolas,monospace;overflow-wrap:anywhere;color:#acbeb8}.caption{font-size:13px;margin:9px 0 0;color:var(--muted)}.empty{padding:45px;border:1px dashed var(--edge);text-align:center;color:var(--muted);border-radius:10px;grid-column:1/-1}.sr{position:absolute;width:1px;height:1px;overflow:hidden;clip:rect(0,0,0,0)}footer{padding:20px 0 40px;border-top:1px solid var(--edge);font-size:13px;color:var(--muted)}footer details{margin-top:14px}summary{cursor:pointer}pre{white-space:pre-wrap;overflow-wrap:anywhere;font-size:12px;background:#0d1416;border:1px solid var(--edge);border-radius:7px;padding:14px;max-height:390px;overflow:auto}dialog{background:#142023;color:var(--text);border:1px solid #5c746c;border-radius:13px;padding:0;width:min(1150px,96vw);max-height:96vh;box-shadow:0 24px 100px #0009}dialog::backdrop{background:#020608d9}.modal-head{display:flex;gap:15px;align-items:center;justify-content:space-between;padding:16px 20px;border-bottom:1px solid var(--edge)}.modal-head h2{margin:0;font-size:21px}.close{padding:7px 12px}.modal-shot{display:block;width:100%;max-height:65vh;object-fit:contain;background:#080e10}.modal-body{padding:15px 20px 22px}.modal-body p{font-size:14px;color:var(--muted)}.modal-nav{display:flex;gap:8px;flex-wrap:wrap;align-items:center}.modal-nav button{padding:7px 12px}.modal-nav button[aria-pressed=true]{background:var(--accent);color:#172015}.download{color:var(--accent);font-size:14px;margin-left:auto}dialog details{font-size:14px}.tagline{font-size:12px;color:var(--warm)}@media(max-width:600px){header,main,footer{width:calc(100% - 28px)}header{padding-top:26px}.grid{grid-template-columns:1fr}.controls{position:static}select{width:100%}.modal-head{padding:12px}.modal-body{padding:12px}.download{margin:5px 0}}
</style></head><body>
<header><div class="eyebrow">DUNGEONS ALIVE · MINECRAFT JAVA 1.21.1</div><h1>Kreaturen im Spiel</h1>
<p>Durchsuche die tatsächlichen Weltaufnahmen nach Name, Registry-ID und Figurengruppe. Die Ansichten zeigen Ruhe, Bewegung, eine ausgewählte Serveraktion oder die Händlerbegrüßung.</p>
<p class="notice"><strong>Echte Minecraft-Frames aus einer gestellten Prüfszene.</strong> Alle Bilder sind unverzerrte, verkleinerte WebP-Ansichten bis 768 × 480 Pixel. Sie belegen weder natürliche Spawns noch einen vollständigen Originalkampf oder eine abgeschlossene Qualitätsabnahme. Die Vollansicht verwendet ebenfalls das verkleinerte Bild.</p>
<div class="stats" id="stats"></div></header>
<main><section class="controls" aria-label="Galerie filtern"><div class="filters"><label class="search"><span class="sr">Nach Namen oder ID suchen</span><input id="search" type="search" placeholder="Name oder Registry-ID suchen …" autocomplete="off"></label><label><span class="sr">Figurengruppe</span><select id="category"><option value="all">Alle Figuren</option></select></label></div><div class="viewbar" id="views" role="group" aria-label="Aufnahmesicht"><span>Ansicht</span></div></section>
<p class="result" id="result" role="status" aria-live="polite"></p><section class="grid" id="gallery" aria-label="Spielaufnahmen"></section></main>
<footer><strong>Herkunft bleibt nachprüfbar.</strong> Jede Ansicht enthält ihre Lauf-ID, den Original-PNG-Pfad und SHA-256 der Originaldatei. Korrekturläufe ersetzen den vollständigen Sichtensatz der betroffenen Figur. Die Quellenmodelle behalten ihre Bedingungen gemäß THIRD_PARTY_NOTICES.md; diese Galerie benötigt keine Internetverbindung.<details><summary>Aufnahmeläufe und Manifest-Prüfsummen</summary><pre id="runs"></pre></details><p id="version"></p></footer>
<dialog id="detail" aria-labelledby="detail-title"><div class="modal-head"><div><div class="tagline">SKALIERTE VOLLANSICHT · ECHTER MINECRAFT-FRAME</div><h2 id="detail-title"></h2></div><button class="close" id="close" type="button" aria-label="Vollansicht schließen">Schließen ×</button></div><img class="modal-shot" id="detail-image" alt=""><div class="modal-body"><div class="modal-nav" id="detail-views"></div><p id="detail-caption"></p><details><summary>Originaldatei, Prüfsummen und Aufnahmemetadaten</summary><pre id="detail-provenance"></pre></details></div></dialog>
<script id="gallery-data" type="application/json">__GALLERY_DATA__</script>
<script>
'use strict';
const data=JSON.parse(document.getElementById('gallery-data').textContent);
const $=id=>document.getElementById(id);
const groups={boss:'Bosse',miniboss:'Minibosse',enemy:'Gegner',animal:'Tiere & Begleiter',merchant:'Händler',civilian:'Bewohner'};
const views={idle:'Ruhe',movement:'Bewegung',action:'Aktion',greeting:'Begrüßung',rescue:'Befreiung'};
const kinds={KEY_GOLEM:'Schlüsselgolem',WILDLIFE:'Wildtier',PET:'Begleiter',MERCHANT:'Händler',CIVILIAN:'Bewohner',BOSS:'Boss',MINIBOSS:'Miniboss',REGULAR:'Gegner'};
let selectedView='idle',activeFigure=null,activeView=null;
const normal=value=>String(value).normalize('NFD').replace(/[\u0300-\u036f]/g,'').toLocaleLowerCase('de');
function node(tag,cls,text){const n=document.createElement(tag);if(cls)n.className=cls;if(text!==undefined)n.textContent=text;return n;}
for(const [n,label] of [[data.figure_count,'Figuren'],[data.frame_count,'Weltansichten'],[data.runs.length,'abgeschlossene Läufe']]){const s=node('span');s.append(node('b','',n),document.createTextNode(label));$('stats').append(s);}
for(const [key,label] of Object.entries(groups)){const count=data.figures.filter(f=>f.group===key).length;const option=node('option','',label+' ('+count+')');option.value=key;$('category').append(option);}
for(const [key,label] of Object.entries(views)){const b=node('button','',label);b.type='button';b.dataset.view=key;b.addEventListener('click',()=>{selectedView=key;render();});$('views').append(b);}
function caption(frame,view){const c=frame.capture;if(view==='movement'&&c.stationary_profile)return 'Stationäres Profil · keine Fortbewegung';if(view==='action')return 'Serveraktion '+c.server_action+' · Tick '+Number(c.action_tick||0).toFixed(1);if(view==='greeting')return 'Begrüßung nach echter Händlerinteraktion';if(view==='rescue')return 'Befreiung nach tatsächlichem Rechtsklick';return views[view]+' · Welt-Tick '+c.game_tick;}
function render(){
 const query=normal($('search').value),group=$('category').value;
 const matching=data.figures.filter(f=>(group==='all'||group===f.group)&&normal(f.name_de+' '+f.name_en+' '+f.registry_id).includes(query));
 const visible=matching.filter(f=>f.frames[selectedView]);
 for(const b of $('views').querySelectorAll('button'))b.setAttribute('aria-pressed',String(b.dataset.view===selectedView));
 $('result').textContent=visible.length+' von '+data.figure_count+' Figuren · '+views[selectedView]+(matching.length>visible.length?' · '+(matching.length-visible.length)+' passende Figuren besitzen diese Ansicht nicht.':'');
 const fragment=document.createDocumentFragment();
 for(const figure of visible){
  const frame=figure.frames[selectedView],card=node('article','card'),button=node('button','shot');button.type='button';button.setAttribute('aria-label',figure.name_de+' – '+views[selectedView]+' in skalierter Vollansicht öffnen');
  const img=node('img');img.src=frame.image;img.alt=figure.name_de+' in der Minecraft-Prüfszene, '+views[selectedView];img.width=frame.width;img.height=frame.height;img.loading='lazy';img.decoding='async';button.append(img,node('span','expand','Vollansicht ↗'));button.addEventListener('click',()=>openDetail(figure,selectedView));
  const body=node('div','body'),chips=node('div','chips');chips.append(node('span','chip',kinds[figure.category]||figure.category));if(figure.corrected)chips.append(node('span','chip corrected','Korrekturlauf'));
  body.append(chips,node('h2','',figure.name_de),node('p','name-en',figure.name_en),node('div','registry',figure.registry_id),node('p','caption',caption(frame,selectedView)));card.append(button,body);fragment.append(card);
 }
 if(!visible.length)fragment.append(node('div','empty','Keine passende Aufnahme. Suche, Figurengruppe oder Ansicht ändern.'));
 $('gallery').replaceChildren(fragment);
}
function openDetail(figure,view){activeFigure=figure;activeView=view;paintDetail();if(!$('detail').open)$('detail').showModal();}
function paintDetail(){
 const frame=activeFigure.frames[activeView];$('detail-title').textContent=activeFigure.name_de+' · '+views[activeView];$('detail-image').src=frame.image;$('detail-image').alt=activeFigure.name_de+' – '+views[activeView]+' in der echten Minecraft-Prüfszene';
 $('detail-caption').textContent=caption(frame,activeView)+' · Anzeige '+frame.width+' × '+frame.height+' · Original-PNG '+frame.original_width+' × '+frame.original_height+' · Aufnahmelauf '+frame.run;
 const controls=document.createDocumentFragment();for(const [key,label] of Object.entries(views)){if(!activeFigure.frames[key])continue;const b=node('button','',label);b.type='button';b.setAttribute('aria-pressed',String(key===activeView));b.addEventListener('click',()=>{activeView=key;paintDetail();});controls.append(b);}
 const link=node('a','download','Verkleinertes WebP speichern');link.href=frame.image;link.download=activeFigure.id+'-'+activeView+'.webp';controls.append(link);$('detail-views').replaceChildren(controls);
 const {image,...provenance}=frame;$('detail-provenance').textContent=JSON.stringify({profile:activeFigure.registry_id,visual_source:activeFigure.visual_source,corrected:activeFigure.corrected,...provenance},null,2);
}
$('search').addEventListener('input',render);$('category').addEventListener('change',render);$('close').addEventListener('click',()=>$('detail').close());$('detail').addEventListener('click',e=>{if(e.target===$('detail')){const box=$('detail').getBoundingClientRect();if(e.clientX<box.left||e.clientX>box.right||e.clientY<box.top||e.clientY>box.bottom)$('detail').close();}});
$('detail').addEventListener('keydown',e=>{if((e.key==='ArrowRight'||e.key==='ArrowLeft')&&activeFigure){const available=Object.keys(views).filter(v=>activeFigure.frames[v]);const delta=e.key==='ArrowRight'?1:-1;activeView=available[(available.indexOf(activeView)+delta+available.length)%available.length];paintDetail();e.preventDefault();}});
$('runs').textContent=JSON.stringify(data.runs,null,2);$('version').textContent='Dungeons Alive '+data.version+' · Offline-Galerie · Hauptframes vollständig abgeschlossener Aufnahmeläufe';render();
</script></body></html>'''


def main(argv: list[str] | None = None) -> int:
    parser = argparse.ArgumentParser(description=__doc__, formatter_class=argparse.RawDescriptionHelpFormatter)
    parser.add_argument("--run", type=Path, required=True, help="Completed capture directory containing manifest.json")
    parser.add_argument("--overrides", type=Path, action="append", default=[], help="Completed correction run; repeatable, later run wins per profile")
    parser.add_argument("--output", type=Path, default=ROOT / "reports/native-gallery.html", help="Self-contained offline HTML destination")
    args = parser.parse_args(argv)
    try:
        if not features.check("webp"):
            raise GalleryError("Pillow was built without WebP support")
        status = read_json((ROOT / "design/implementation-status.json").read_bytes(), "implementation-status.json")
        registry = {entry["id"]: entry for entry in status["runtime_entities"] if entry.get("registered") is True}
        base = load_run(args.run, registry)
        corrections = [load_run(path, registry) for path in args.overrides]
        payload = build_payload(base, corrections, registry, status)
        # JSON is data, never interpolated into executable JavaScript or HTML attributes.
        serialized = json.dumps(payload, ensure_ascii=False, separators=(",", ":"), allow_nan=False)
        serialized = serialized.replace("&", "\\u0026").replace("<", "\\u003c").replace(">", "\\u003e").replace("\u2028", "\\u2028").replace("\u2029", "\\u2029")
        output = args.output.expanduser().resolve()
        output.parent.mkdir(parents=True, exist_ok=True)
        temporary = output.with_suffix(output.suffix + ".tmp")
        temporary.write_text(HTML.replace("__GALLERY_DATA__", serialized), encoding="utf-8")
        temporary.replace(output)
        print(f"Wrote {output}: {payload['figure_count']} figures, {payload['frame_count']} native main frames, {output.stat().st_size:,} bytes.")
        return 0
    except (GalleryError, OSError, KeyError, TypeError) as exc:
        print(f"Gallery not written: {exc}", file=sys.stderr)
        return 2


if __name__ == "__main__":
    raise SystemExit(main())
