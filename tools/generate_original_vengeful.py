#!/usr/bin/env python3
"""Own, reference-based Vengeful Heart of Ender art. No imported image pixels.

The Python description is the editable source for the Java rig and pixel atlases.
Animation clips are maintained separately in OriginalVengefulHeartAnimations.java.
Viewed references: official EchoingVoid-header2.jpg and the public Minecraft Wiki
whole-body render. Both are visual references only; neither is loaded by this script.
"""
from __future__ import annotations

from dataclasses import dataclass, field
from pathlib import Path
import hashlib
import json
import math
import random

from PIL import Image, ImageDraw

ROOT = Path(__file__).resolve().parents[1]
JAVA = ROOT / 'src/main/java/de/laugenbreze/dungeonsalive/client/entity/reference/imported'
TEXTURES = ROOT / 'src/main/resources/assets/dungeonsalive/textures/entity/reference/original'
SIZE = 512
DENSITY = 2
PADDING = 2


@dataclass
class Box:
    name: str
    origin: tuple[float, float, float]
    size: tuple[float, float, float]
    material: str
    seed: int
    eyes: bool = False
    plane: bool = False
    uv: list[tuple[int, int, int, int]] = field(default_factory=list)


@dataclass
class Bone:
    name: str
    parent: str | None
    position: tuple[float, float, float] = (0, 0, 0)
    rotation: tuple[float, float, float] = (0, 0, 0)
    boxes: list[Box] = field(default_factory=list)


bones: dict[str, Bone] = {}


def bone(name, parent, position=(0, 0, 0), rotation=(0, 0, 0)):
    assert name not in bones
    bones[name] = Bone(name, parent, position, rotation)
    return name


def box(part, name, xyz, whd, material='shell', seed=0, eyes=False, plane=False):
    bones[part].boxes.append(Box(name, xyz, whd, material, seed, eyes, plane))


# Minecraft model convention: y=24 is the ground, forward is -Z.
bone('vengeful_root', None, (0, 24, 0))
bone('body', 'vengeful_root', (0, -12, 0))
bone('body_front', 'body', (0, -4, -15), (0, -3, 0))
box('body_front', 'front_shell', (-10.5, -7, -10), (21, 12, 20), 'shell', 31, True)
box('body_front', 'front_underplate', (-9, 3.7, -8.5), (18, 2.2, 17), 'shell', 32)
bone('body_middle', 'body', (0, -3.8, 4), (0, 4, 0))
box('body_middle', 'middle_shell', (-9.3, -6, -10), (18.6, 11.5, 20), 'shell', 41, True)
box('body_middle', 'middle_joint', (-8, -3.8, -11), (16, 7, 22), 'goo', 42)
bone('body_rear', 'body_middle', (0, -.5, 18), (0, -6, 0))
box('body_rear', 'rear_shell', (-9, -7.2, -9), (18, 12.5, 18), 'shell', 51, True)
box('body_rear', 'rear_joint', (-7.7, -4.5, -10), (15.4, 8.5, 18), 'goo', 52)
bone('rear_crest', 'body_rear', (0, -6, 5), (0, 6, 0))
box('rear_crest', 'rear_raised_segment', (-7.2, -8.5, -6.7), (14.4, 9, 13.4), 'shell', 55, True)

for label, x, z, sign in [('front_left', 12.5, -19, 1), ('front_right', -12.5, -19, -1),
                           ('back_left', 11.2, 22, 1), ('back_right', -11.2, 22, -1)]:
    bone(label+'_leg', 'body', (x, 0, z), (0, -2*sign, 2*sign))
    box(label+'_leg', label+'_haunch', (-4, -2, -4.5), (8, 6.4, 9), 'leg', 100+len(bones))
    box(label+'_leg', label+'_shin', (-3.2, 3.8, -3.8), (6.4, 4.5, 7.6), 'leg', 120+len(bones))
    bone(label+'_foot', label+'_leg', (.4*sign, 8, -.25), (0, 0, -2*sign))
    box(label+'_foot', label+'_foot_block', (-4.1, 0, -4.8), (8.2, 4, 9.6), 'leg', 140+len(bones))
    box(label+'_foot', label+'_toe_lip', (-3.6, 1.8, -5.1), (7.2, 2.1, 1.2), 'leg', 160+len(bones))

# Five connected but individually pivoted cube segments. Differing widths and yaw
# avoid a straight rectangular tower; glowing connective tissue remains visible.
neck_specs = [
    ('neck_base', 'body_front', (0, -5, -.5), (0, 4, 0), (20, 8.7, 18)),
    ('neck_1', 'neck_base', (.1, -8, .15), (0, -8, -.5), (18.8, 8.7, 17.8)),
    ('neck_2', 'neck_1', (-.3, -8, .2), (0, 10, .8), (19.6, 8.2, 17.3)),
    ('neck_3', 'neck_2', (.3, -7.5, -.15), (0, -8, -.6), (18.1, 7.9, 16.8)),
    ('neck_4', 'neck_3', (-.2, -7, .1), (0, 6, .3), (18.8, 7.4, 17.2)),
]
for i, (name, parent, p, rot, (w,h,d)) in enumerate(neck_specs):
    bone(name, parent, p, rot)
    box(name, 'neck_shell_'+str(i), (-w/2, -h, -d/2), (w,h-.35,d), 'shell', 210+i*17, True)
    box(name, 'neck_joint_'+str(i), (-w*.40, -h-.85, -d*.40), (w*.80,1.8,d*.80), 'goo', 215+i*17)
    # A few angular peeling plates, deliberately asymmetrical and noncoplanar.
    if i in [0,2,4]:
        s=-1 if i==2 else 1
        bone(name+'_peel', name, (s*w*.49,-h*.36,-d*.20), (0,0,-8*s))
        box(name+'_peel','peeling_edge_'+str(i),(-.25,-1.6,-2.2),(.8,3.8,5.5),'shell',230+i)

bone('head', 'neck_4', (0, -7, -.4), (0, -4, 0))
# Open crown/mouth: no solid cube is allowed to fill this negative space.
box('head', 'mouth_core', (-7.5,-7.5,-4.3), (15,5.8,6.5), 'mouth', 301)
box('head', 'back_wall', (-9.2,-8,2), (18.4,7.5,3), 'shell', 302)
box('head', 'left_cheek', (8.3,-8,-8), (3.2,7.5,12.3), 'crown', 303)
box('head', 'right_cheek', (-11.5,-8,-8), (3.2,7.5,12.3), 'crown', 304)
box('head', 'crown_top', (-11.5,-10,-8.4), (23,2.2,13.2), 'crown', 305)
box('head', 'forehead_bar', (-12,-8.3,-9.4), (24,1.6,1.3), 'crown', 306)
box('head', 'portal_top', (-7.8,-10.12,-5.3), (15.6,.12,8.1), 'mouth', 307)
bone('jaw', 'head', (0,-.3,3.5), (2,0,0))
box('jaw', 'jaw_plate', (-11.5,-.8,-13), (23,2.5,15.3), 'crown', 311)
box('jaw', 'jaw_luminous_lip', (-8.5,-1.0,-12.2), (17,.18,11.6), 'goo', 312)
for i, x in enumerate([-7.2,-2.5,2.5,7.2]):
    box('head','upper_fang_'+str(i),(x-.7,-7.2,-8.8),(1.4,1.6,.7),'tooth',320+i)

# Long, thin sideways horns are the distinctive crown silhouette, not thick antlers.
for side, sign in [('left',1),('right',-1)]:
    bone(side+'_horn', 'head', (sign*11.2,-8.6,-7.9), (0,0,sign*-4))
    start=0 if sign==1 else -17
    box(side+'_horn',side+'_horn_bar',(start,-.65,-.7),(17,1.3,1.4),'horn',340+sign)
    bone(side+'_horn_tip', side+'_horn', (sign*16.7,-.15,0), (0,0,sign*-6))
    start=0 if sign==1 else -2.7
    box(side+'_horn_tip',side+'_horn_tip_plate',(start,-.7,-.7),(2.7,1.4,1.4),'horn',346+sign)
    bone(side+'_flame',side+'_horn_tip',(sign*1.0,-.7,0))
    box(side+'_flame',side+'_flame_front',(-3.8,-9.8,-.025),(7.6,10.4,.05),'flame',350+sign,plane=True)
    bone(side+'_flame_cross',side+'_flame',rotation=(0,90,0))
    box(side+'_flame_cross',side+'_flame_cross_plane',(-3.5,-8.8,-.025),(7,9.4,.05),'flame',360+sign,plane=True)
bone('crown_flame','head',(0,-10.2,1.0))
box('crown_flame','crown_fire',(-7,-7.7,-.025),(14,8,.05),'flame',370,plane=True)
bone('crown_flame_cross','crown_flame',rotation=(0,90,0))
box('crown_flame_cross','crown_fire_cross',(-4.8,-6.5,-.025),(9.6,6.8,.05),'flame',371,plane=True)

atlas=Image.new('RGBA',(SIZE,SIZE),(0,0,0,0))
glow=Image.new('RGBA',(SIZE,SIZE),(0,0,0,0))
cursor_x=cursor_y=PADDING; shelf_height=0
face_rectangles=[]


def alloc(w,h):
    global cursor_x,cursor_y,shelf_height
    if cursor_x+w+PADDING>SIZE:
        cursor_x=PADDING;cursor_y+=shelf_height+PADDING;shelf_height=0
    if cursor_y+h+PADDING>SIZE:raise RuntimeError('Own UV atlas overflow')
    r=(cursor_x,cursor_y,w,h);cursor_x+=w+PADDING;shelf_height=max(shelf_height,h)
    face_rectangles.append(r)
    return r


def col(base,delta):
    return tuple(max(0,min(255,c+delta)) for c in base)+(255,)


def paint(w,h,material,seed,face,eyes):
    rng=random.Random(seed*97+face*271)
    im=Image.new('RGBA',(w,h),(0,0,0,0));em=Image.new('RGBA',(w,h),(0,0,0,0))
    draw=ImageDraw.Draw(im);ed=ImageDraw.Draw(em)
    if material=='flame':
        # Hand-authored stepped silhouette parameterized by deterministic tongues.
        for x in range(w):
            q=x/max(1,w-1); center=1-abs(q*2-1)
            tip=int(h*(.48-.37*center))+rng.randrange(max(1,h//5))
            if (x//2)%5==0:tip=max(0,tip-h//4)
            for y in range(tip,h):
                edge=min(x,w-x-1,y-tip)
                inner=(y-tip)/max(1,h-tip)
                c=(125,12,205) if edge<1 else ((230,45,255) if inner<.4 else (255,135,255))
                if inner>.65 and .2<q<.8:c=(255,235,255)
                draw.point((x,y),fill=c+(255,));ed.point((x,y),fill=c+(255,))
        return im,em
    palettes={'shell':(24,9,34),'leg':(24,16,35),'horn':(22,6,34),'crown':(27,9,39),
              'goo':(179,34,217),'mouth':(208,69,244),'tooth':(241,174,255)}
    base=palettes[material];step=2
    for y in range(0,h,step):
        for x in range(0,w,step):
            delta=rng.choice([-7,-5,-3,-1,0,0,1,2,4,6])
            draw.rectangle((x,y,min(w-1,x+step-1),min(h-1,y+step-1)),fill=col(base,delta))
    if material in ['shell','crown','horn']:
        # Broken plate rims, dark recesses and stepped surface grain.
        for x in range(w):
            if rng.random()>.22:draw.point((x,0),fill=(43,19,55,255))
        for y in range(h):
            if rng.random()>.35:draw.point((0,y),fill=(36,14,46,255))
        draw.line((0,h-1,w-1,h-1),fill=(10,3,17,255),width=1)
        for _ in range(max(1,w*h//90)):
            x=rng.randrange(w);y=rng.randrange(h)
            ww=rng.randint(1,max(1,w//8));hh=rng.randint(1,2)
            draw.rectangle((x,y,min(w-1,x+ww),min(h-1,y+hh)),fill=rng.choice([(12,4,21,255),(34,13,43,255),(40,16,49,255)]))
    if material=='leg':
        # Dense irregular obsidian pits distinguish legs from the peeled body plates.
        for _ in range(max(4,w*h//15)):
            x=rng.randrange(w);y=rng.randrange(h)
            ww=rng.randint(1,3);hh=rng.randint(1,4)
            draw.rectangle((x,y,min(w-1,x+ww),min(h-1,y+hh)),fill=rng.choice([(4,2,10,255),(8,3,14,255),(38,20,46,255)]))
    if material=='shell' and w>=14 and h>=10:
        # One large missing-skin patch, with angular branching rather than random pink noise.
        side=-1 if seed%2 else 1
        cx=int(w*(.22 if side<0 else .73));cy=int(h*(.63 if eyes else .43))
        rw=max(3,w//6);rh=max(3,h//4)
        variant=(seed+face)%5
        patterns=[
            [(-1,-.5),(-.5,-.5),(-.5,-1),(.33,-1),(.33,-.33),(1,-.33),(1,.33),(.5,.33),(.5,1),(-.33,1),(-.33,.33),(-1,.33)],
            [(-1,-1),(-.2,-1),(-.2,-.3),(.7,-.3),(.7,.2),(1,.2),(1,.6),(.1,.6),(.1,1),(-.5,1),(-.5,.25),(-1,.25)],
            [(-1,-.3),(-.6,-.3),(-.6,-.8),(.4,-.8),(.4,-1),(1,-1),(1,-.2),(.5,-.2),(.5,.4),(-.1,.4),(-.1,1),(-.5,1),(-.5,.2),(-1,.2)],
            [(-1,-1),(-.2,-1),(-.2,-.6),(.6,-.6),(.6,-.2),(1,-.2),(1,.3),(.2,.3),(.2,1),(-.2,1),(-.2,.2),(-1,.2)],
            [(-1,-.6),(-.5,-.6),(-.5,-1),(.2,-1),(.2,-.5),(.8,-.5),(.8,.4),(.4,.4),(.4,1),(-.2,1),(-.2,.4),(-1,.4)],
        ]
        polygon=[(int(cx+x*rw),int(cy+y*rh))for x,y in patterns[variant]]
        draw.polygon(polygon,fill=(202,48,237,255));ed.polygon(polygon,fill=(173,38,210,255))
        path=[(cx-rw+1,cy),(cx-rw//3,cy),(cx-rw//3,cy-rh+1),(cx+rw//3-1,cy-rh+1)]
        draw.line(path,fill=(255,180,255,255),width=2);ed.line(path,fill=(255,137,250,255),width=2)
        # A narrow dripping fissure joins the main open patch.
        drop=[(cx+rw//3,cy+rh//2),(cx+rw//3, min(h-1,cy+rh+2)),(cx+rw//3+2,min(h-1,cy+rh+2))]
        draw.line(drop,fill=(237,90,252,255),width=2);ed.line(drop,fill=(208,64,239,255),width=2)
        # Rectangular eyes are not stamped onto every face: fronts and alternating sides.
        if eyes and (face==0 or (face in [2,3] and seed%3==face%3)):
            ey=max(2,int(h*.27));ew=max(2,int(w*.115));eh=max(2,int(h*.11));mid=w//2
            for ex in [mid-ew-2,mid+2]:
                draw.rectangle((ex-1,ey-1,ex+ew,ey+eh),fill=(54,13,67,255))
                draw.rectangle((ex,ey,ex+ew-1,ey+eh-1),fill=(255,210,255,255))
                ed.rectangle((ex,ey,ex+ew-1,ey+eh-1),fill=(248,153,255,255))
                draw.line((ex,ey,ex+ew-1,ey),fill=(255,246,255,255));ed.line((ex,ey,ex+ew-1,ey),fill=(255,218,255,255))
    if material in ['goo','mouth','tooth']:
        for y in range(h):
            for x in range(w):
                c=im.getpixel((x,y));ed.point((x,y),fill=c)
        if material=='mouth':
            # Bright inner void aperture, dark edges read as actual depth in the open jaw.
            for y in range(h):
                for x in range(w):
                    border=min(x,w-x-1,y,h-y-1)
                    c=(103,9,147,255)if border<1 else ((242,95,255,255)if border<3 else (255,231,255,255))
                    draw.point((x,y),fill=c);ed.point((x,y),fill=c)
        elif material=='goo':
            for y in range(1,h,4):
                draw.line((0,y,w-1,y),fill=(246,143,255,255));ed.line((0,y,w-1,y),fill=(222,93,246,255))
    return im,em


face_tasks=[]
for b in bones.values():
    for q in b.boxes:
        w,h,d=q.size
        dimensions=[(w,h),(w,h),(d,h),(d,h),(w,d),(w,d)]
        if q.plane:dimensions=dimensions[:2]
        q.uv=[None]*len(dimensions)
        for face,(fw,fh) in enumerate(dimensions):
            tw=max(2,math.ceil(fw*DENSITY));th=max(2,math.ceil(fh*DENSITY))
            face_tasks.append((q,face,tw,th))
# Height-sorted shelves keep the many narrow crown/joint faces from wasting rows.
for q,face,tw,th in sorted(face_tasks,key=lambda task:(-task[3],-task[2])):
    rect=alloc(tw,th);q.uv[face]=rect
    diffuse,emissive=paint(tw,th,q.material,q.seed,face,q.eyes)
    atlas.paste(diffuse,rect[:2]);glow.paste(emissive,rect[:2])
TEXTURES.mkdir(parents=True,exist_ok=True)
atlas.save(TEXTURES/'vengeful_heart.png');glow.save(TEXTURES/'vengeful_heart_glow.png')


def f(v):
    if abs(v)<.0000001:v=0
    return f'{v:.6f}'.rstrip('0').rstrip('.')+'.0F' if float(v).is_integer() else f'{v:.6f}'.rstrip('0')+'F'


childmap={k:[]for k in bones}
for b in bones.values():
    if b.parent:childmap[b.parent].append(b.name)
header='''/* Original reference-based art created for Dungeons Alive, 2026. Project MIT license.
 * No image pixels, game model vertices or animation clips copied from Minecraft Dungeons.
 * Visual references and scope: docs/SOURCE_MODEL_AUDIT.md.
 * Geometry and pixel atlases generated by tools/generate_original_vengeful.py.
 * Do not overwrite with a renamed Goety/CEM rig; edit the own generator instead.
 */
package de.laugenbreze.dungeonsalive.client.entity.reference.imported;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;

public final class OriginalVengefulHeartGeometry {
    private OriginalVengefulHeartGeometry() {}
    public static final int TEXTURE_SIZE = 512;
    public static LayerDefinition createBodyLayer() {
        return LayerDefinition.create(new MeshDefinition(), TEXTURE_SIZE, TEXTURE_SIZE);
    }
    public static ModelPart bakeRoot() {
        var children = new LinkedHashMap<String, ModelPart>();
        children.put("vengeful_root", vengeful_root());
        return new ModelPart(List.of(), children);
    }
'''
code=[header]
for b in bones.values():
    code.append(f'    private static ModelPart {b.name}() {{\n        var children = new LinkedHashMap<String, ModelPart>();')
    for child in childmap[b.name]:code.append(f'        children.put("{child}", {child}());')
    code.append('        var cubes = new ArrayList<ModelPart.Cube>();')
    for q in b.boxes:
        uv='new int[][] {'+', '.join('{'+','.join(str(v)for v in r)+'}'for r in q.uv)+'}'
        code.append('        cubes.add(cube('+', '.join(f(v)for v in (*q.origin,*q.size))+', '+uv+')); // '+q.name)
    code.append('        var part = new ModelPart(cubes, children);')
    code.append('        var pose = PartPose.offsetAndRotation('+', '.join(f(v)for v in (*b.position,*(math.radians(v)for v in b.rotation)))+');')
    code.append('        part.setInitialPose(pose); part.loadPose(pose); return part;\n    }\n')
code.append('''    private static ModelPart.Cube cube(float x, float y, float z, float w, float h, float d, int[][] uv) {
        float a=x+w, b=y+h, c=z+d;
        float[][][] vertices = {
            {{x,y,z},{x,b,z},{a,b,z},{a,y,z}},
            {{a,y,c},{a,b,c},{x,b,c},{x,y,c}},
            {{x,y,c},{x,b,c},{x,b,z},{x,y,z}},
            {{a,y,z},{a,b,z},{a,b,c},{a,y,c}},
            {{x,y,c},{x,y,z},{a,y,z},{a,y,c}},
            {{x,b,z},{x,b,c},{a,b,c},{a,b,z}}
        };
        float[][][] faces = new float[uv.length][4][5];
        for (int face=0; face<uv.length; face++) {
            int[] r=uv[face];
            float u0=(r[0]+.01F)/TEXTURE_SIZE, v0=(r[1]+.01F)/TEXTURE_SIZE;
            float u1=(r[0]+r[2]-.01F)/TEXTURE_SIZE, v1=(r[1]+r[3]-.01F)/TEXTURE_SIZE;
            float[][] coords={{u0,v0},{u0,v1},{u1,v1},{u1,v0}};
            for (int v=0; v<4; v++) {
                System.arraycopy(vertices[face][v],0,faces[face][v],0,3);
                faces[face][v][3]=coords[v][0]; faces[face][v][4]=coords[v][1];
            }
        }
        return new ReferenceMeshCube(x,y,z,w,h,d,faces);
    }
}
''')
(JAVA/'OriginalVengefulHeartGeometry.java').write_text('\n'.join(code))

# Deterministic bind-pose bounds and UV-space checks. These are geometry checks,
# not a substitute for viewing the rendered creature and its actual animations.
def matmul(a,b):
    return [[sum(a[i][k]*b[k][j]for k in range(4))for j in range(4)]for i in range(4)]


def transform(b):
    rx,ry,rz=map(math.radians,b.rotation)
    c,s=math.cos,math.sin
    mx=[[1,0,0,0],[0,c(rx),-s(rx),0],[0,s(rx),c(rx),0],[0,0,0,1]]
    my=[[c(ry),0,s(ry),0],[0,1,0,0],[-s(ry),0,c(ry),0],[0,0,0,1]]
    mz=[[c(rz),-s(rz),0,0],[s(rz),c(rz),0,0],[0,0,1,0],[0,0,0,1]]
    result=matmul(matmul(mz,my),mx)
    for i in range(3):result[i][3]=b.position[i]
    return matmul(matrices[b.parent],result)if b.parent else result


matrices={};points=[]
for b in bones.values():
    matrix=transform(b);matrices[b.name]=matrix
    for q in b.boxes:
        for dx in [0,q.size[0]]:
            for dy in [0,q.size[1]]:
                for dz in [0,q.size[2]]:
                    p=[q.origin[0]+dx,q.origin[1]+dy,q.origin[2]+dz,1]
                    points.append([sum(matrix[i][k]*p[k]for k in range(4))for i in range(3)])
mins=[min(p[i]for p in points)for i in range(3)]
maxs=[max(p[i]for p in points)for i in range(3)]
for i,(x,y,w,h)in enumerate(face_rectangles):
    assert 0<=x and 0<=y and x+w<=SIZE and y+h<=SIZE
    for xx,yy,ww,hh in face_rectangles[:i]:
        assert x+w<=xx or xx+ww<=x or y+h<=yy or yy+hh<=y,'UV rectangles overlap'
print(json.dumps({'own_bones':len(bones),'own_boxes':sum(len(b.boxes)for b in bones.values()),'atlas_size':[SIZE,SIZE],
                  'atlas_last_used_y':cursor_y+shelf_height,'uv_rectangles':len(face_rectangles),'bind_min_pixels':mins,'bind_max_pixels':maxs,
                  'raw_height_blocks':(maxs[1]-mins[1])/16,'recommended_scale_for_3_8_blocks':3.8/((maxs[1]-mins[1])/16),
                  'pixel_authorship':'All pixels generated from the authored patterns above; no input images.'},indent=2))
