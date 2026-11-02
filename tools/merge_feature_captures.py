#!/usr/bin/env python3
"""Merge completed native feature runs, replacing whole scenes, without changing PNG bytes."""
import argparse
import copy
import hashlib
import json
from pathlib import Path
import shutil

CONSTRUCTS={'geomancer_wall','geomancer_bomb','ice_chunk','entangle_vine'}
SCENE_FILES={subject:{'world/'+subject+'.png'} for subject in CONSTRUCTS}
SCENE_FILES['corrupted_beacon']={'world/corrupted_beacon.png','world/corrupted_beacon-first-person.png'}

def validate_scenes(report):
    """An override is a whole native scene, never a new still with an older clip."""
    captures=report['captures']; clips=report['clips']
    if len({f['file'] for f in captures})!=len(captures) or len({c['subject'] for c in clips})!=len(clips):
        raise ValueError('Duplicate native view or clip in source manifest')
    filenames={f['file'] for f in captures}; subjects={c['subject'] for c in clips}
    if not subjects.issubset(SCENE_FILES): raise ValueError('Unknown native feature scene')
    for subject,expected in SCENE_FILES.items():
        present=filenames & expected
        if present or subject in subjects:
            if present!=expected or subject not in subjects:
                raise ValueError(f'Incomplete scene override: {subject}; main views and sequence must come together')

def main():
    p=argparse.ArgumentParser(description=__doc__)
    p.add_argument('--base',type=Path,required=True)
    p.add_argument('--override',type=Path,action='append',default=[])
    p.add_argument('--output',type=Path,required=True)
    a=p.parse_args();out=a.output.resolve()
    if out.exists():raise SystemExit('Use a fresh merged evidence directory')
    roots=[a.base.resolve()]+[x.resolve()for x in a.override]
    if any(out==r or out.is_relative_to(r)for r in roots):raise SystemExit('Output must not be inside a source run')
    captures={};clips={};sources=[];inputs=[];original_manifests={};report=None
    for base in roots:
        raw=(base/'manifest.json').read_bytes();d=json.loads(raw)
        if d.get('complete')is not True or d.get('failure')or d.get('capture_kind')!='native_minecraft_gear_and_construct_checks':raise ValueError('Only completed native feature runs')
        validate_scenes(d)
        if report is None:report=copy.deepcopy(d)
        source={'run':base.name,'manifest_sha256':hashlib.sha256(raw).hexdigest(),'selection':d.get('selection','all'),'complete':True}
        inputs.append(source)
        for original in d.get('source_runs') or [source]:
            run=original['run']
            if not run or Path(run).name!=run or run in ('.','..') or '\\' in run:
                raise ValueError('Invalid original native run name')
            manifest_file='source-manifests/'+run+'.json'
            blob=(base/manifest_file).read_bytes() if d.get('source_runs') else raw
            if hashlib.sha256(blob).hexdigest()!=original['manifest_sha256']:
                raise ValueError('Original native manifest hash mismatch')
            original_report=json.loads(blob)
            if original_report.get('complete') is not True or original_report.get('failure') or original_report.get('source_runs'):
                raise ValueError('Original manifest must be a completed native run, not another merge')
            existing=next((s for s in sources if s['run']==original['run']),None)
            if existing and existing['manifest_sha256']!=original['manifest_sha256']:
                raise ValueError('Ambiguous original run name with different manifests')
            if not existing:sources.append({**copy.deepcopy(original),'manifest_file':manifest_file})
            original_manifests[manifest_file]=blob
        for f in d['captures']:captures[f['file']]=(base,copy.deepcopy(f))
        for c in d['clips']:clips[c['subject']]=(base,copy.deepcopy(c))
    if len(captures)!=20 or len(clips)!=5:raise ValueError('Expected full current feature set:20main/5sequences')
    out.mkdir(parents=True)
    for file,blob in original_manifests.items():
        target=out/file;target.parent.mkdir(parents=True,exist_ok=True);target.write_bytes(blob)
    def retain(base,frame):
        rel=Path(frame['file']);src=(base/rel).resolve()
        if rel.is_absolute()or '..'in rel.parts or not src.is_relative_to(base):raise ValueError('Escaping source frame')
        digest=hashlib.sha256(src.read_bytes()).hexdigest()
        if frame.get('sha256')!=digest:raise ValueError('Native frame hash mismatch')
        dst=out/rel;dst.parent.mkdir(parents=True,exist_ok=True);shutil.copy2(src,dst)
        frame.setdefault('source_run',base.name);frame.setdefault('source_file',rel.as_posix())
        original=next((s for s in sources if s['run']==frame['source_run']),None)
        if original is None:raise ValueError('Frame refers to an unknown original source run')
        if frame.get('source_manifest_sha256',original['manifest_sha256'])!=original['manifest_sha256']:
            raise ValueError('Frame original-manifest hash mismatch')
        frame['source_manifest_sha256']=original['manifest_sha256']
        frame['copied_from_input_run']=base.name
        return frame
    report['captures']=[retain(base,f)for base,f in captures.values()]
    report['clips']=[]
    for base,clip in clips.values():
        clip['frames']=[retain(base,f)for f in clip['frames']]
        original_runs=sorted({f['source_run'] for f in clip['frames']})
        clip['source_run']=original_runs[0] if len(original_runs)==1 else None
        clip['source_runs']=original_runs;report['clips'].append(clip)
    report.update({'selection':'merged_all','run':out.name,'source_runs':sources,'merge_inputs':inputs,'complete':True,'planned_main_frames':20,
                   'merge_notice':'Later completed scenes replace earlier views/sequences. Every retained native PNG is copied unchanged with original hash and source_run retained. Unchanged original manifests are retained separately; superseded image files are not part of this merged current set.'})
    (out/'manifest.json').write_text(json.dumps(report,ensure_ascii=False,indent=2)+'\n')
    print(out)

if __name__=='__main__':main()
