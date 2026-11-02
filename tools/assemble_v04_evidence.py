#!/usr/bin/env python3
"""Archive completed real Minecraft capture evidence and encode its recorded frames.

No rendering, synthesized frames, retouching, or claims of exhaustive visual QA.
Original full-resolution frames are retained in a separate optional evidence ZIP;
selected unchanged PNGs and rescaled GIF sequences are in the source deliverable.
"""
from __future__ import annotations
import argparse
import copy
from datetime import datetime, timezone
import hashlib
import json
from pathlib import Path
import re
import shutil
import xml.etree.ElementTree as ET
import zipfile
from PIL import Image

ROOT = Path(__file__).resolve().parents[1]
VERSION = '0.4.0-alpha'
SELECTED = {'tropical_slime', 'armored_trident_drowned', 'diamond_armored_pillager',
            'diamond_armored_vindicator', 'captured_blacksmith', 'arch_illager',
            'pet_baby_squid', 'armored_drowned', 'pale_armored_drowned', 'redstone_monstrosity'}

def sha(path): return hashlib.sha256(path.read_bytes()).hexdigest()
def write(path, value):
    path.parent.mkdir(parents=True, exist_ok=True)
    path.write_text(json.dumps(value, ensure_ascii=False, indent=2)+'\n')

def load(directory):
    directory=directory.resolve()
    report=json.loads((directory/'manifest.json').read_text())
    if report.get('complete') is not True or report.get('failure'):
        raise ValueError(f'Incomplete or failed capture: {directory}')
    return directory, report

def frame_path(base, frame):
    path=(base/frame['file']).resolve()
    if not path.is_relative_to(base) or not path.is_file(): raise ValueError('Invalid native frame path')
    if frame.get('sha256') and sha(path)!=frame['sha256']: raise ValueError(f'Native hash mismatch: {path}')
    return path

def encode(base, clips, folder, prefix):
    results=[]
    folder.mkdir(parents=True, exist_ok=True)
    for clip in clips:
        frames_meta=clip['frames']
        if len(frames_meta)<2: raise ValueError('Sequence has fewer than two actual frames')
        # A client world clock can repeat while later server snapshots are
        # already displayed/interpolated. Prefer the recorded server clock,
        # never invent a duration for a duplicate client tick.
        timing_key = ('server_state_tick' if all('server_state_tick' in f for f in frames_meta)
                      else 'server_tick' if all('server_tick' in f for f in frames_meta)
                      else 'game_tick')
        subject=clip.get('subject') or (clip['profile']+'-'+clip['view'])
        images=[]; durations=[]; origins=[]
        for i, frame in enumerate(frames_meta):
            p=frame_path(base,frame)
            origins.append({'source_run':frame.get('source_run',clip.get('source_run',base.name)),
                            'source_file':frame.get('source_file',frame['file']),
                            'source_manifest_sha256':frame.get('source_manifest_sha256'),
                            'sha256':sha(p),'game_tick':frame.get('game_tick',frame.get('server_tick'))})
            with Image.open(p) as original:
                im=original.convert('RGB'); im.thumbnail((720, 720),Image.Resampling.LANCZOS); images.append(im)
            if i+1<len(frames_meta):
                after=frames_meta[i+1]
                delta=after[timing_key]-frame[timing_key]
                if delta<=0: raise ValueError(f'Non-increasing native ticks: {subject}')
                durations.append(round(delta*50))
            else: durations.append(250)
        target=folder/(prefix+subject+'.gif')
        images[0].save(target,save_all=True,append_images=images[1:],duration=durations,loop=0,disposal=2,optimize=False)
        for im in images:im.close()
        source_runs=sorted({f['source_run'] for f in origins})
        results.append({'subject':subject,'file':target.relative_to(ROOT).as_posix(), 'sha256':sha(target),
                        'native_source_frames':len(frames_meta),'duration_ms':sum(durations),
                        'source_run':source_runs[0] if len(source_runs)==1 else None,
                        'source_runs':source_runs,'input_directory':base.relative_to(ROOT).as_posix(),
                        'source_frames':origins,'timing_field':timing_key,
                        'timing':'Recorded server snapshot tick deltas at 50ms per tick; final frame held for 250ms.',
                        'source':'Aspect-preserving resized encoding of actual native PNGs, without synthetic motion.'})
    return results

def main():
    ap=argparse.ArgumentParser(description=__doc__)
    ap.add_argument('--model-run',type=Path,required=True)
    ap.add_argument('--feature-run',type=Path,required=True)
    ap.add_argument('--camp-run',type=Path,required=True)
    ap.add_argument('--server-log',type=Path,required=True)
    args=ap.parse_args()
    runs=[('model',*load(args.model_run)),('feature',*load(args.feature_run)),('camp',*load(args.camp_run))]
    model=runs[0][2];feature=runs[1][2];camp=runs[2][2]
    if len(model['profiles'])!=131 or len(model['captures'])!=352: raise ValueError('Expected complete 131-figure run')
    if len(set(model['profiles']))!=131 or {f['profile'] for f in model['captures']}!=set(model['profiles']) \
            or len({(f['profile'],f['view']) for f in model['captures']})!=352:
        raise ValueError('Model run contains duplicate/missing profile views')
    if len(feature['captures'])!=20 or len(feature['clips'])!=5: raise ValueError('Expected complete gear/construct/beam run')
    if len(camp['captures'])!=3: raise ValueError('Expected three completed native camp views')
    evidence_zip=ROOT.parent/'DungeonsAlive_Native_Captures_v0.4.0.zip'
    archived=[];gif_meta=[]
    with zipfile.ZipFile(evidence_zip,'w',zipfile.ZIP_DEFLATED,compresslevel=6) as archive:
        for name,base,raw in runs:
            copied=copy.deepcopy(raw);copied['version']=VERSION
            input_manifest=(base/'manifest.json').read_bytes()
            copied['input_manifest_sha256']=hashlib.sha256(input_manifest).hexdigest()
            copied['native_archive_input_manifest']=name+'/input-manifest.json'
            archive.writestr(copied['native_archive_input_manifest'],input_manifest)
            input_copy=ROOT/'reports/native-v04'/name/'input-manifest.json'
            input_copy.parent.mkdir(parents=True,exist_ok=True);input_copy.write_bytes(input_manifest)
            copied['archive_input_manifest_path']=input_copy.relative_to(ROOT).as_posix()
            copied['source_run_directory']=base.relative_to(ROOT).as_posix()
            copied['source_run_directory_kind']='merged_evidence' if raw.get('source_runs') else 'native_capture'
            for original in copied.get('source_runs',[]):
                manifest_path=(base/original['manifest_file']).resolve()
                if not manifest_path.is_relative_to(base):raise ValueError('Escaping original manifest path')
                blob=manifest_path.read_bytes()
                if hashlib.sha256(blob).hexdigest()!=original['manifest_sha256']:raise ValueError('Original source manifest hash mismatch')
                original['native_archive_manifest_path']=name+'/'+original['manifest_file']
                archive.writestr(original['native_archive_manifest_path'],blob)
                destination=ROOT/'reports/native-v04'/name/original['manifest_file']
                destination.parent.mkdir(parents=True,exist_ok=True);destination.write_bytes(blob)
                original['archive_manifest_path']=destination.relative_to(ROOT).as_posix()
            copied['native_archive']=evidence_zip.name
            copied['raw_png_notice']='All native main/sequence PNGs are in the separate capture ZIP. Selected unchanged main PNGs have archive_path in this source package; the offline gallery contains scaled derivatives of every model view.'
            all_frames=copied['captures']+[f for c in copied.get('clips',[])for f in c['frames']]
            used=set()
            for frame in all_frames:
                p=frame_path(base,frame);frame['sha256']=sha(p)
                frame.setdefault('source_run',raw.get('run',base.name))
                frame.setdefault('source_file',frame['file'])
                frame.setdefault('source_manifest_sha256',copied['input_manifest_sha256'])
                entry=name+'/'+frame['file'];frame['native_archive_path']=entry
                if entry not in used:archive.write(p,entry);used.add(entry)
            for frame in copied['captures']:
                if name!='model' or frame.get('profile') in SELECTED:
                    destination=ROOT/'reports'/'native-v04'/name/frame['file']
                    destination.parent.mkdir(parents=True,exist_ok=True)
                    shutil.copy2(base/frame['file'],destination)
                    frame['archive_path']=destination.relative_to(ROOT).as_posix()
                    archived.append(frame['archive_path'])
            write(ROOT/'reports'/(name+'-showcase.json'),copied)
            archive.writestr(name+'/manifest.json',json.dumps(copied,ensure_ascii=False,indent=2)+'\n')
            gif_meta+=encode(base,copied.get('clips',[]),ROOT/'reports/animations/v04','' if name=='model' else name+'-')
        archive.writestr('README.txt', 'Dungeons Alive 0.4.0 — actual Minecraft 1.21.1 development-client captures.\nStaged worlds; not natural-spawn or exhaustive visual/gameplay acceptance.\nAll PNGs are unchanged retained native frames. Metadata contains state, original paths and hashes.\ninput-manifest.json retains each accepted input unchanged; feature/source-manifests retains the separate original native manifests when scenes were merged. Superseded images from replaced scenes are not included in the current merged set.\nSelected visual QA and technical validation are in the separate full source package.\n')
    write(ROOT/'reports/animations/v04/provenance.json',{'version':VERSION,'clips':gif_meta})
    suites=[]
    for xml in sorted((ROOT/'build/test-results/test').glob('TEST-*.xml')):
        root=ET.parse(xml).getroot()
        suites.append({k:int(root.attrib[k]) if k in ('tests','failures','errors','skipped') else root.attrib[k]
                       for k in ('name','tests','failures','errors','skipped','timestamp')})
    unit={'version':VERSION,'source':'Actual final Gradle JUnit XML','total_tests':sum(x['tests']for x in suites),
          'failures':sum(x['failures']for x in suites),'errors':sum(x['errors']for x in suites),
          'skipped':sum(x['skipped']for x in suites),'suites':suites}
    if unit['total_tests']!=50 or unit['failures'] or unit['errors'] or unit['skipped']:raise ValueError('JUnit gate failed')
    write(ROOT/'reports/test-results.json',unit)
    log=args.server_log.read_text();passed=[int(n)for n in re.findall(r'All (\d+) required tests passed',log)]
    if passed!=[30,32] or 'BUILD SUCCESSFUL' not in log:raise ValueError('Server log is not the completed 30-core / 32-including-camps run')
    shutil.copy2(args.server_log,ROOT/'reports/server-validation-log.txt')
    (ROOT/'reports/server-checks.txt').write_text(f'Dungeons Alive {VERSION}\nActual completed command: ./gradlew build runGameTestServer runCampGameTestServer\nCore: {passed[0]} required GameTests passed.\nCamps: {passed[1]} required GameTests passed = same {passed[0]} core + 2 additional camp tests.\nDistinct required cases: {passed[1]}, not {sum(passed)}.\nNo failed required test in accepted run. Complete log: server-validation-log.txt\n')
    summary={'version':VERSION,'updated_utc':datetime.now(timezone.utc).isoformat(),
             'junit_tests':unit['total_tests'],'junit_failed':0,'server_gametests':passed[0],'server_failed':0,
             'camp_total_tests':passed[1],'camp_additional_tests':2,'registered_figures':131,'registered_spell_constructs':4,
             'current_gallery_frames':len(model['captures']),'native_animation_sequences':len(model['clips']),
             'native_animation_frames':sum(len(c['frames'])for c in model['clips']),
             'feature_main_frames':len(feature['captures']),'feature_sequences':len(feature['clips']),
             'feature_sequence_frames':sum(len(c['frames'])for c in feature['clips']),
             'camp_main_frames':len(camp['captures']),'selected_unchanged_current_pngs':len(archived),
             'historical_boss_showcase_frames':6,
             'environment':{'minecraft':'1.21.1','neoforge':'21.1.265','java':'21.0.13+9-LTS','renderer':'Mesa llvmpipe OpenGL 4.5','os':'Linux x86_64'},
             'evidence':['test-results.json','server-validation-log.txt','model-showcase.json','feature-showcase.json','camp-showcase.json','v04-visual-polish-qa.json','v04-new-content-visual-qa.json','v04-construct-visual-qa.json','v04-gear-visual-qa.json'],
             'limits':['Native captures are staged and do not imply full visual approval of every pose.','No two-real-client, long-term survival, all-seed camp generation or complete original boss-mechanic acceptance.','See docs/VALIDATION.md for actual viewed frames and remaining limits.']}
    write(ROOT/'reports/validation-summary.json',summary)
    write(ROOT/'reports/native-archive.json',{'file':evidence_zip.name,'bytes':evidence_zip.stat().st_size,'sha256':sha(evidence_zip),'contents':'All unchanged PNGs and manifests from the accepted model, feature and camp evidence sets. A merged feature set retains each original native source run separately.'})
    print(json.dumps(summary,ensure_ascii=False));print('Native evidence ZIP:',evidence_zip, evidence_zip.stat().st_size)

if __name__=='__main__':main()
