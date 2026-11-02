#!/usr/bin/env python3
"""Reproducible, offline conversion of selected licensed Bedrock/Gecko visual assets.

Keeps arbitrary cube pivots, negative/fractional face UVs and named skeletal rigs.
Numeric keyframes are retained. Supported procedural MoLang/easing channels are
sampled at 60 Hz with all original key times retained; unsupported expressions
raise an error. Ground speed is normalized to one and driven by walk phase at
runtime. This is a documented animation adaptation, not a complete MoLang engine.
"""
from __future__ import annotations
import ast
import argparse
import copy
import bisect
import hashlib
import json
import math
from pathlib import Path
import re

ROOT = Path(__file__).resolve().parents[1]
PKG = 'de.laugenbreze.dungeonsalive.client.entity.reference.imported'
OUT = ROOT / 'src/main/java' / PKG.replace('.', '/')
FACES = {'west':[3,2,0,1], 'east':[4,5,7,6], 'north':[2,4,6,0],
         'south':[5,3,1,7], 'up':[3,5,4,2], 'down':[0,6,7,1]}

def f(x):
    x = float(x)
    if not math.isfinite(x): raise ValueError('Nonfinite number')
    if abs(x) < 1e-9: x = 0
    s = format(x, '.8g')
    return s + ('F' if '.' in s or 'e' in s else '.0F')

def q(x): return json.dumps(x)
def bone_name(x): return 'bedrock_source_root' if x == 'root' else x
def vec(x, default=0):
    if x is None: return [default]*3
    if isinstance(x, (int,float)): return [float(x)]*3
    if not isinstance(x,list) or len(x) != 3: raise ValueError(f'Expected vector: {x}')
    return x

def java_array(value):
    if isinstance(value, list): return '{' + ', '.join(java_array(x) for x in value) + '}'
    return f(value)

def source_json(path, digest):
    p = ROOT / path
    if hashlib.sha256(p.read_bytes()).hexdigest() != digest: raise ValueError('Source hash changed: '+path)
    return json.loads(p.read_text())

def cube_quads(c, bone, pivot, tw, th):
    o = vec(c.get('origin')); s = vec(c.get('size'))
    inflate = float(c.get('inflate', bone.get('inflate', 0)))
    x0, y0, z0 = o[0]-inflate, -o[1]-s[1]-inflate, o[2]-inflate
    x1, y1, z1 = o[0]+s[0]+inflate, -o[1]+inflate, o[2]+s[2]+inflate
    vertices = [[x1,y1,z0],[x1,y1,z1],[x1,y0,z0],[x1,y0,z1],
                [x0,y0,z0],[x0,y0,z1],[x0,y1,z0],[x0,y1,z1]]
    # Bedrock -> Blockbench -> native Java convention verified against official
    # codecs: pivot=(x,24-y,z), relative rotation=(rx,ry,rz), cubes Y-down.
    for v in vertices:
        v[0] -= pivot[0]; v[1] += pivot[1]; v[2] -= pivot[2]
    uv = c.get('uv', [0,0]); box_uv = isinstance(uv,list)
    mirror = bool(c.get('mirror', bone.get('mirror',False)))
    if box_uv:
        u,v = uv; dx,dy,dz = [math.floor(n) for n in s]
        uv = {'west':{'uv':[u+dz+dx,v+dz],'uv_size':[dz,dy]},
              'east':{'uv':[u,v+dz],'uv_size':[dz,dy]},
              'north':{'uv':[u+dz,v+dz],'uv_size':[dx,dy]},
              'south':{'uv':[u+dz+dx+dz,v+dz],'uv_size':[dx,dy]},
              'up':{'uv':[u+dz,v],'uv_size':[dx,dz]},
              'down':{'uv':[u+dz+dx,v+dz],'uv_size':[dx,-dz]}}
    quads=[]
    for face, entry in uv.items():
        if face not in FACES: raise ValueError('Unknown face '+face)
        direction = face
        if mirror and face in ('west','east'): direction = 'east' if face == 'west' else 'west'
        if mirror and not box_uv and face in ('up','down'): direction = 'down' if face == 'up' else 'up'
        u,v = entry['uv']
        atlas_offset = c.get('_atlas_offset', [0,0])
        u += atlas_offset[0]; v += atlas_offset[1]
        default_size = [s[2] if face in ('west','east') else s[0], s[2] if face in ('up','down') else s[1]]
        du,dv = entry.get('uv_size',default_size)
        us,ue = (u,u+du) if mirror else (u+du,u)
        tex = [[us/tw,v/th],[ue/tw,v/th],[ue/tw,(v+dv)/th],[us/tw,(v+dv)/th]]
        rotation = int(entry.get('uv_rotation',0))
        if rotation % 90: raise ValueError('Unsupported nonquarter UV rotation')
        turns = (rotation//90)%4
        if turns: tex=tex[turns:]+tex[:turns]
        quads.append([vertices[i]+tex[n] for n,i in enumerate(FACES[direction])])
    return [x0-pivot[0],y0+pivot[1],z0-pivot[2],x1-x0,y1-y0,z1-z0],quads

def geometry(asset, raw):
    geo=raw['minecraft:geometry'][0]; bones=geo['bones']; names={x['name'] for x in bones}
    if len(names)!=len(bones): raise ValueError('Duplicate bone')
    nodes={}; cube_count=0
    for b in bones:
        name=bone_name(b['name']); p=vec(b.get('pivot'))
        parent=bone_name(b['parent']) if b.get('parent') else None
        nodes[name]={'parent':parent,'pivot':p,'rotation':vec(b.get('rotation')),'cubes':[]}
    for b in bones:
        name=bone_name(b['name']); p=vec(b.get('pivot'))
        for i,c in enumerate(b.get('cubes',[])):
            if c.get('rotation'):
                target=name+'__cube_'+str(i); cp=vec(c.get('pivot'))
                nodes[target]={'parent':name,'pivot':cp,'rotation':vec(c['rotation']),'cubes':[(c,b,cp)]}
            else: nodes[name]['cubes'].append((c,b,p))
            cube_count+=1
    for name,node in nodes.items():
        if node['parent'] is not None and node['parent'] not in nodes: raise ValueError('Absent parent '+node['parent'])
    tw=geo['description']['texture_width']; th=geo['description']['texture_height']
    cls=asset['geometry_class']; methods=[]
    numbers={name:i for i,name in enumerate(nodes)}
    for name,node in nodes.items():
        parent=node['parent']; pivot=node['pivot']; pp=nodes[parent]['pivot'] if parent else [0,24,0]
        offset=[pivot[0]-pp[0],pp[1]-pivot[1],pivot[2]-pp[2]]
        pose=offset+[math.radians(x) for x in node['rotation']]
        cubes=[]
        for c,b,p in node['cubes']:
            bounds,quads=cube_quads(c,b,p,tw,th)
            cubes.append('new ReferenceMeshCube('+', '.join(f(x) for x in bounds)+', new float[][][]'+java_array(quads)+')')
        children=[n for n,d in nodes.items() if d['parent']==name]
        body=f'    private static ModelPart bone{numbers[name]}() {{\n        var children = new LinkedHashMap<String, ModelPart>();\n'
        for child in children: body+=f'        children.put({q(child)}, bone{numbers[child]}());\n'
        body+='        var part = new ModelPart(List.of('+',\n            '.join(cubes)+'), children);\n'
        body+='        var pose = PartPose.offsetAndRotation('+', '.join(f(x) for x in pose)+');\n'
        body+='        part.setInitialPose(pose); part.loadPose(pose); return part;\n    }\n'
        methods.append(body)
    text=notice(asset)+f'package {PKG};\nimport java.util.*;\nimport net.minecraft.client.model.geom.*;\nimport net.minecraft.client.model.geom.builders.*;\n\npublic final class {cls} {{\n'
    text+=f'    public static LayerDefinition createBodyLayer() {{ return LayerDefinition.create(new MeshDefinition(), {tw}, {th}); }}\n'
    text+='    public static ModelPart bakeRoot() {\n        var children = new LinkedHashMap<String, ModelPart>();\n'
    for n,d in nodes.items():
        if d['parent'] is None: text+=f'        children.put({q(n)}, bone{numbers[n]}());\n'
    text+='        return new ModelPart(List.of(), children);\n    }\n'+'\n'.join(methods)+'}\n'
    (OUT/(cls+'.java')).write_text(text)
    return names, {'bones':len(bones),'cube_pivot_nodes':len(nodes)-len(bones),'cubes':cube_count,'texture_size':[tw,th]}

def notice(asset):
    return ('/* Adapted visual asset, '+asset['license']+'. '+asset['copyright']+'\n'
            ' * Original sources and changes: design/bedrock-model-imports.json; licenses in third_party.\n'
            ' * Generated by tools/convert_bedrock_models.py; source snapshots are retained unchanged. */\n')

def attach_equipment(asset, raw):
    """Place audited costume geometry onto explicitly named animated rig sockets."""
    if not asset.get('equipment'): return raw
    raw=copy.deepcopy(raw); geo=raw['minecraft:geometry'][0]
    nodes={b['name']:b for b in geo['bones']}
    from PIL import Image
    spec=asset['equipment']; size=spec['atlas_size']; atlas=Image.new('RGBA',tuple(size))
    for material in spec['materials']:
        path=ROOT/material['snapshot']
        if hashlib.sha256(path.read_bytes()).hexdigest()!=material['sha256']: raise ValueError('Equipment texture hash changed')
        atlas.paste(Image.open(path).convert('RGBA'),tuple(material['offset']))
    for attachment in spec['attachments']:
        data=source_json(attachment['snapshot'],attachment['sha256'])['minecraft:geometry'][0]
        source={b['name']:b for b in data['bones']}
        for src,target in attachment['bone_map'].items():
            original=source[src]; dest=nodes[target]
            # Source coordinates are in the same authored humanoid frame. Explicit
            # offsets account for 4px armor arm pivots attached to 5px illager arms.
            shift=attachment.get('bone_offsets',{}).get(src,[0,0,0])
            dest['cubes']=copy.deepcopy(original.get('cubes',[]))
            for c in dest['cubes']:
                for key in ('origin','pivot'):
                    if key in c: c[key]=[a+b for a,b in zip(c[key],shift)]
                c['_atlas_offset']=attachment['atlas_offset']
            dest['mirror']=original.get('mirror',False)
    geo['description']['texture_width'],geo['description']['texture_height']=size
    path=ROOT/spec['atlas_file'];path.parent.mkdir(parents=True,exist_ok=True);atlas.save(path)
    return raw

def expression(source,t):
    if isinstance(source,(int,float)): return float(source)
    tree=ast.parse(source.replace('Math.','math.'),mode='eval')
    def visit(n):
        if isinstance(n,ast.Constant) and isinstance(n.value,(int,float)): return n.value
        if isinstance(n,ast.BinOp):
            a,b=visit(n.left),visit(n.right)
            if isinstance(n.op,ast.Add): return a+b
            if isinstance(n.op,ast.Sub): return a-b
            if isinstance(n.op,ast.Mult): return a*b
            if isinstance(n.op,ast.Div): return a/b
        if isinstance(n,ast.UnaryOp):
            if isinstance(n.op,ast.USub): return -visit(n.operand)
            if isinstance(n.op,ast.UAdd): return visit(n.operand)
        if isinstance(n,ast.Attribute) and isinstance(n.value,ast.Name):
            if (n.value.id,n.attr)==('query','anim_time'): return t
            # Explicit sampling adaptation: authored lifetime oscillation is
            # evaluated on the continuous cyclic clip clock, never replaced by 0.
            if (n.value.id,n.attr)==('query','life_time'): return t
            if (n.value.id,n.attr)==('query','ground_speed'): return 1
        if isinstance(n,ast.Call) and isinstance(n.func,ast.Attribute) and isinstance(n.func.value,ast.Name) and n.func.value.id=='math':
            args=[visit(x) for x in n.args]
            if n.func.attr=='cos': return math.cos(math.radians(args[0]))
            if n.func.attr=='sin': return math.sin(math.radians(args[0]))
            if n.func.attr=='clamp': return max(args[1],min(args[2],args[0]))
        raise ValueError('Unsupported MoLang: '+source)
    return visit(tree.body)

def data_vector(v,which='post'):
    if isinstance(v,dict):
        v=v.get(which,v.get('vector',v.get('post',v.get('pre'))))
    return vec(v)

def easing(name,t,args=None):
    if name in (None,'linear'): return t
    if name=='easeInOutSine': return -(math.cos(math.pi*t)-1)/2
    if name=='easeOutSine': return math.sin(math.pi*t/2)
    if name=='easeInSine': return 1-math.cos(math.pi*t/2)
    if name in ('easeInOutQuad','easeInOutCubic','easeInOutQuart'):
        power={'easeInOutQuad':2,'easeInOutCubic':3,'easeInOutQuart':4}[name]
        return (2*t)**power/2 if t<.5 else 1-(-2*t+2)**power/2
    if name=='easeOutBack':
        strength=1.70158*(float(args[0]) if args else 1)
        return 1+(strength+1)*(t-1)**3+strength*(t-1)**2
    if name=='step':
        steps=int(args[0]) if args else 2
        if steps<2: raise ValueError('Step easing needs >=2 steps')
        if t<0: return 0
        if t>(steps-1)/steps: return (steps-1)/steps
        low,high=0,steps-1
        while high-low>1:
            middle=(low+high)//2
            if t>=middle/steps: low=middle
            else: high=middle
        return low/steps
    raise ValueError('Unsupported easing '+str(name))

def channel_frames(channel,length):
    if not isinstance(channel,dict) or any(k in channel for k in ('vector','pre','post')): frames=[(0,channel)]
    else: frames=sorted((float(t),v) for t,v in channel.items())
    times=[x[0] for x in frames]
    encoded=json.dumps(channel)
    sampled=bool(re.search(r'query\.|Math\.|math\.|easing',encoded))
    def evaluate(t):
        idx=bisect.bisect_right(times,t)-1; idx=max(0,idx)
        a,va=frames[idx]
        av=[expression(x,t) for x in data_vector(va)]
        if idx==len(frames)-1: return av
        b,vb=frames[idx+1]; bv=[expression(x,t) for x in data_vector(vb,'pre')]
        k=max(0,min(1,(t-a)/(b-a)))
        k=easing(vb.get('easing') if isinstance(vb,dict) else None,k,vb.get('easingArgs') if isinstance(vb,dict) else None)
        return [x+(y-x)*k for x,y in zip(av,bv)]
    if sampled:
        moments=sorted(set(times+[round(i/60,8) for i in range(int(length*60)+1)]+[length]))
        return [(t,evaluate(t),'LINEAR') for t in moments],True
    out=[]
    for t,v in frames:
        interp='CATMULLROM' if isinstance(v,dict) and v.get('lerp_mode')=='catmullrom' else 'LINEAR'
        if isinstance(v,dict) and 'pre' in v and 'post' in v and v['pre']!=v['post']:
            if t>0: out.append((max(0,t-0.00001),[expression(x,t) for x in data_vector(v,'pre')],interp))
        out.append((t,[expression(x,t) for x in data_vector(v)],interp))
    return out,False

def animations(asset,raw,bones):
    cls=asset['animation_class']; body=[]; methods=[]; owners={}; lengths={}; dropped={}; sampled_channels=0; channel_counter=0
    animations=raw['animations']
    for name,clip in animations.items():
        cname=re.sub('[^A-Za-z0-9_]','_',name).upper()
        if cname in owners: raise ValueError('Duplicate sanitized clip')
        owners[cname]=cls
        length=float(clip.get('animation_length', asset.get('procedural_loop_seconds',7.2)))
        lengths[cname]=length
        cb=f'    public static final AnimationDefinition {cname} = AnimationDefinition.Builder.withLength({f(length)})'
        if clip.get('loop') is True: cb+='.looping()'
        dropped[name]=[]
        for bone,channels in clip.get('bones',{}).items():
            if bone not in bones:
                if bone not in asset.get('discard_unbound_channels',[]): raise ValueError(f'{name}: unapproved absent bone {bone}')
                dropped[name].append(bone); continue
            for kind,values in channels.items():
                if kind not in ('rotation','position','scale'): raise ValueError('Unsupported channel '+kind)
                frames,sampled=channel_frames(values,length); sampled_channels+=int(sampled)
                # Skip empty identity channels, but retain genuine scale-zero visibility.
                identity=[1,1,1] if kind=='scale' else [0,0,0]
                if all(v==identity for _,v,_ in frames): continue
                channel_counter+=1; method=f'channel{channel_counter}'
                func={'rotation':'degreeVec','position':'posVec','scale':'scaleVec'}[kind]
                target=kind.upper()
                # Each channel is separate to avoid JVM's 64KB method limit.
                entries=[f'new Keyframe({f(t)}, KeyframeAnimations.{func}('+', '.join(f(x) for x in v)+f'), AnimationChannel.Interpolations.{i})' for t,v,i in frames]
                methods.append(f'    private static AnimationChannel {method}() {{ return new AnimationChannel(AnimationChannel.Targets.{target},\n            '+',\n            '.join(entries)+'); }\n')
                cb+=f'\n        .addAnimation({q(bone_name(bone))}, {method}())'
        cb+='\n        .build();\n'; body.append(cb)
    text=notice(asset)+f'package {PKG};\nimport net.minecraft.client.animation.*;\npublic final class {cls} {{\n'
    text+='\n'.join(body+methods)+'}\n'
    (OUT/(cls+'.java')).write_text(text)
    return {'clip_owners':owners,'clip_lengths':lengths,'discarded_unbound_channels':dropped,'sampled_channels':sampled_channels,'retained_channels':channel_counter}

def main():
    parser=argparse.ArgumentParser();parser.add_argument('--manifest',default='design/bedrock-model-imports.json');parser.add_argument('--report',default='design/bedrock-conversion-report.json');args=parser.parse_args()
    manifest=json.loads((ROOT/args.manifest).read_text()); results=[]
    OUT.mkdir(parents=True,exist_ok=True)
    for asset in manifest['assets']:
        raw=attach_equipment(asset,source_json(asset['model_snapshot'],asset['model_sha256']))
        bones,stats=geometry(asset,raw)
        stats.update({'key':asset['key'],'geometry_class':asset['geometry_class'],'baked_root':True})
        if asset.get('animation_snapshot'):
            anim=source_json(asset['animation_snapshot'],asset['animation_sha256'])
            stats.update(animations(asset,anim,bones))
        results.append(stats)
    (ROOT/args.report).write_text(json.dumps({'assets':results,'coordinate_reference':'Official Blockbench Bedrock and Java entity codecs; GeckoLib 1.21.1 quad conventions','procedural_sampling_hz':60,'normalized_ground_speed':1,'lifetime_clock_adaptation':'query.life_time is sampled on cyclic clip time; action clips use their own adapted phase, not global entity life phase.'},indent=2)+'\n')
    print(f'Converted {len(results)} Bedrock visual bundles; source hashes, rig bindings and expressions validated.')

if __name__=='__main__': main()
