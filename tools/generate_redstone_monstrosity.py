#!/usr/bin/env python3
"""Original Redstone Monstrosity model source, UV atlas, emission mask and Blockbench project.

The geometry and pixels below were drawn for Dungeons Alive from visual study of
official screenshots. No original game geometry, texture or animation is read.
This script is the editable mesh/texture source; the runtime animation is in
RedstoneMonstrosityModel.java. Only this one boss's dedicated files are written.
"""
from __future__ import annotations
from pathlib import Path
import base64
import hashlib
import json
import math
import random
import struct
import uuid
import zlib

ROOT = Path(__file__).resolve().parents[1]
JAVA = ROOT / 'src/main/java/de/laugenbreze/dungeonsalive/client/entity'
TEXTURES = ROOT / 'src/main/resources/assets/dungeonsalive/textures/entity/redstone_monstrosity'
SIZE = 512
NODES = []
BOXES = []


def node(name, parent, x, y, z):
    NODES.append(dict(name=name, parent=parent, pivot=(x, y, z)))
    return name


def box(parent, name, material, x, y, z, w, h, d):
    assert name not in {b['name'] for b in BOXES}, name
    assert all(isinstance(v, int) and v > 0 for v in (w, h, d)), name
    BOXES.append(dict(parent=parent, name=name, material=material, pos=(x, y, z), size=(w, h, d)))


def geometry():
    node('base', 'root', 0, 24, 0)
    node('torso', 'base', 0, -30, 2)
    # The broad furnace body sits above short legs; it is not a stretched humanoid.
    box('torso', 'torso_mass', 'dark_stone', -17, -16, -11, 34, 28, 22)
    box('torso', 'upper_back', 'stone', -15, -18, -9, 30, 5, 20)
    box('torso', 'redstone_back_plate', 'redstone', -15, -19, -9, 30, 2, 20)
    box('torso', 'back_inlay_left', 'stone', -15, -19.15, -8, 5, 1, 5)
    box('torso', 'back_inlay_right', 'pale_stone', 8, -19.15, 3, 7, 1, 7)
    box('torso', 'back_plate_border', 'redstone', -16, -15, 11, 32, 19, 2)
    box('torso', 'back_center_slab', 'stone', -13, -12, 12.2, 26, 13, 1)
    box('torso', 'back_center_seam', 'dark_stone', -.5, -12, 13.25, 1, 13, 1)
    box('torso', 'back_low_armor', 'pale_stone', -15, 5, 10, 30, 5, 3)
    box('torso', 'front_armor', 'stone', -16, -7, -12, 32, 18, 2)
    # Angular conductive bands frame the torso instead of adding a central chest lamp.
    box('torso', 'front_conduit_upper_left', 'conduit', -16, -5, -12.6, 12, 3, 1)
    box('torso', 'front_conduit_upper_right', 'conduit', 4, -5, -12.6, 12, 3, 1)
    box('torso', 'front_conduit_down_left', 'conduit', -16, -2, -12.6, 3, 10, 1)
    box('torso', 'front_conduit_down_right', 'conduit', 13, -2, -12.6, 3, 10, 1)
    box('torso', 'front_conduit_lower', 'conduit', -16, 8, -12.6, 32, 3, 1)
    box('torso', 'front_lower_rim', 'pale_stone', -16.5, 11, -13, 33, 2, 3)
    box('torso', 'left_side_conduit', 'conduit', -17.6, -4, -10, 1, 3, 21)
    box('torso', 'right_side_conduit', 'conduit', 16.6, -4, -10, 1, 3, 21)
    box('torso', 'left_flank_plate', 'runed_stone', -18, 0, -7, 2, 10, 16)
    box('torso', 'right_flank_plate', 'runed_stone', 16, 0, -7, 2, 10, 16)
    box('torso', 'lower_furnace', 'dark_stone', -12, 12, -8, 24, 6, 17)
    box('torso', 'lower_furnace_rim', 'stone', -12.5, 15, -8.5, 25, 3, 18)
    # Two horn brackets turn outwards and then upwards, beside the inset mask.
    node('head', 'torso', 0, -7, -15)
    box('head', 'mask_back', 'dark_stone', -10, -10, -3, 20, 15, 8)
    box('head', 'mask_forehead', 'runed_stone', -9, -10, -6, 18, 10, 4)
    box('head', 'mask_brow_top', 'pale_stone', -9, -10.2, -6.4, 18, 2, 4)
    box('head', 'mask_left_cheek', 'stone', -10, 0, -6, 6, 6, 4)
    box('head', 'mask_right_cheek', 'stone', 4, 0, -6, 6, 6, 4)
    box('head', 'mask_center_nose', 'stone', -4, -1, -6, 8, 3, 3)
    box('head', 'mask_left_lower_rim', 'conduit', -10, 5, -6.6, 6, 1, 1)
    box('head', 'mask_right_lower_rim', 'conduit', 4, 5, -6.6, 6, 1, 1)
    box('head', 'mask_top_redstone_patch', 'redstone', 2, -10.35, -6.5, 7, 1, 7)
    box('head', 'mask_right_redstone_patch', 'redstone', 7, -9, -6.4, 2, 10, 1)
    # Three small pale lights form the canonical shallow triangle.
    for name, x, y, w in [('forehead', -2, -2, 4), ('left', -9, 1, 3), ('right', 6, 1, 3)]:
        box('head', name + '_eye_socket', 'dark_stone', x - 1, y - 1, -7, w + 2, 4, 1)
        box('head', name + '_eye_frame', 'ember_frame', x - .4, y - .4, -7.35, w + 1, 3, 1)
        box('head', name + '_eye_light', 'eye', x, y, -7.55, w, 2, 1)
    box('head', 'mouth_cavity', 'cavity', -7, 2, -3.5, 14, 9, 1)
    box('head', 'mouth_upper_lip', 'pale_stone', -4, 1.4, -6.8, 8, 2, 1)
    for side, sign in [('right', -1), ('left', 1)]:
        node(side + '_horn', 'head', sign * 10, -5, 2)
        box(side + '_horn', side + '_horn_bridge', 'stone', -5 if sign < 0 else 0, -1, -3, 5, 5, 7)
        box(side + '_horn', side + '_horn_upright', 'runed_stone', -5 if sign < 0 else 1, -10, -3, 4, 10, 7)
        box(side + '_horn', side + '_horn_cap', 'pale_stone', -5.3 if sign < 0 else .7, -10.5, -3.3, 5, 2, 8)
    node('jaw', 'head', 0, 6, 1)
    box('jaw', 'jaw_hull', 'dark_stone', -9, 0, -9, 18, 5, 11)
    box('jaw', 'jaw_inner_floor', 'furnace', -7, -.3, -8, 14, 1, 9)
    box('jaw', 'jaw_left_wall', 'stone', -9, -1, -9, 2, 5, 11)
    box('jaw', 'jaw_right_wall', 'stone', 7, -1, -9, 2, 5, 11)
    box('jaw', 'jaw_front_lip', 'stone', -9, 1, -10, 18, 4, 2)
    box('jaw', 'jaw_front_conduit', 'conduit', -7, 1.1, -10.35, 14, 1, 1)
    box('jaw', 'jaw_chin', 'pale_stone', -5, 4, -10, 10, 2, 3)
    # Each arm is a separate shoulder, recessed joint, elbow, gauntlet and knuckle rig.
    for side, sign in [('right', -1), ('left', 1)]:
        node(side + '_arm', 'torso', sign * 23, -12, -1)
        p = side + '_arm'
        box(p, side + '_shoulder_mass', 'runed_stone', -9, -6, -8, 18, 17, 16)
        box(p, side + '_shoulder_upper', 'redstone' if side == 'left' else 'runed_stone', -7, -12, -6, 14, 8, 13)
        box(p, side + '_shoulder_ridge', 'stone', -7.3, -12.3, -6.3, 15, 2, 14)
        box(p, side + '_shoulder_front_plate', 'runed_stone', -9.4, -4, -9, 19, 12, 2)
        box(p, side + '_shoulder_bottom_trim', 'pale_stone', -9.4, 7.5, -9, 19, 2, 3)
        box(p, side + '_upperarm_joint', 'joint', -4.5, 9, -4.5, 9, 10, 9)
        box(p, side + '_upperarm_vein', 'conduit', -1.5, 10, -5, 3, 9, 1)
        box(p, side + '_outer_shoulder_inset', 'redstone', -9.6 if side == 'right' else 8.6, -1, -4, 1, 7, 7)
        node(side + '_forearm', p, 0, 19, 0)
        f = side + '_forearm'
        box(f, side + '_elbow_pivot', 'joint', -5.5, -2, -5.5, 11, 7, 11)
        box(f, side + '_gauntlet', 'stone', -8, 2, -8, 16, 12, 16)
        box(f, side + '_gauntlet_lip', 'pale_stone', -8.4, 1.7, -8.4, 17, 2, 17)
        box(f, side + '_gauntlet_front', 'runed_stone', -7, 4, -8.5, 14, 8, 1)
        node(side + '_hand', f, 0, 13, -.5)
        h = side + '_hand'
        box(h, side + '_palm', 'dark_stone', -7, -1, -7, 14, 8, 14)
        box(h, side + '_hand_back', 'stone', -7.3, -1.2, -7.3, 15, 3, 15)
        for i in range(3):
            box(h, side + '_knuckle_' + str(i), 'stone', -6 + i * 4.5, 4, -9, 3, 4, 5)
            box(h, side + '_finger_seam_' + str(i), 'dark_stone', -5.8 + i * 4.5, 6, -9.2, 3, 1, 1)
        box(h, side + '_thumb', 'dark_stone', -9 if side == 'right' else 7, 1, -4, 2, 5, 6)
    # Short legs remain visually subordinate to the shoulder/fist mass.
    for side, sign in [('right', -1), ('left', 1)]:
        node(side + '_leg', 'base', sign * 8, -13, 4)
        p = side + '_leg'
        box(p, side + '_hip_joint', 'joint', -3.5, -2, -3.5, 7, 5, 7)
        box(p, side + '_shin', 'dark_stone', -4, 1, -4.5, 8, 10, 9)
        box(p, side + '_shin_front', 'runed_dark', -4.5, 1.5, -5.1, 9, 8, 1)
        box(p, side + '_shin_conduit', 'conduit', -.5, 2, -5.5, 1, 7, 1)
        box(p, side + '_foot', 'dark_stone', -5, 10, -8, 10, 3, 14)
        box(p, side + '_toe_cap', 'stone', -5.2, 9.8, -8.3, 11, 2, 4)
        box(p, side + '_toe_left', 'pale_stone', -4, 11.2, -8.5, 3, 1, 1)
        box(p, side + '_toe_right', 'pale_stone', 1, 11.2, -8.5, 3, 1, 1)


def separate_coplanar_surfaces():
    """Separate overlapped trim surfaces by 1/8 model pixel, before UV generation.

    Overlapping armour is intentional; coincident outward faces are not. Only a
    smaller part with an overlapping same-facing plane is offset, along that
    plane's normal. Butt-jointed faces without positive overlap are untouched.
    This avoids the real shoulder/jaw/back stripes seen in the client capture.
    """
    repairs = 0
    origins = {b['name']: tuple(b['pos']) for b in BOXES}
    for _ in range(100):
        conflict = None
        for i, a in enumerate(BOXES):
            for b in BOXES[i + 1:]:
                if a['parent'] != b['parent']:
                    continue
                for axis in range(3):
                    for side in (0, 1):
                        pa = a['pos'][axis] + side * a['size'][axis]
                        pb = b['pos'][axis] + side * b['size'][axis]
                        if abs(pa - pb) > .00001:
                            continue
                        overlap = [min(a['pos'][j] + a['size'][j], b['pos'][j] + b['size'][j]) - max(a['pos'][j], b['pos'][j])
                                   for j in range(3) if j != axis]
                        if min(overlap) > .00001:
                            moving = b if math.prod(b['size']) <= math.prod(a['size']) else a
                            conflict = (moving, axis, side)
                            break
                    if conflict: break
                if conflict: break
            if conflict: break
        if conflict is None:
            print(f'Geometry validation: {repairs} tiny trim-plane offsets; no coincident overlapping faces within a rig node.')
            return
        moving, axis, side = conflict
        pos = list(moving['pos']); pos[axis] += .125 if side else -.125
        assert abs(pos[axis] - origins[moving['name']][axis]) <= .5, moving['name']
        moving['pos'] = tuple(pos); repairs += 1
    raise ValueError('Unresolved coplanar geometry')


def pack():
    x = y = row = 2
    for b in sorted(BOXES, key=lambda b: (-(b['size'][1] + b['size'][2]), -(2 * (b['size'][0] + b['size'][2])), b['name'])):
        w, h, d = b['size']; tw, th = 2 * (w + d), h + d
        if x + tw + 2 > SIZE:
            y += row + 3; x = 2; row = 0
        if y + th + 2 > SIZE:
            raise ValueError('Texture atlas too small')
        b['uv'] = (x, y); b['uv_rect'] = (x, y, x + tw, y + th)
        x += tw + 3; row = max(row, th)
    for i, a in enumerate(BOXES):
        ax, ay, az, aw = a['uv_rect']
        assert 0 <= ax < az < SIZE and 0 <= ay < aw < SIZE
        for b in BOXES[i+1:]:
            bx, by, bz, bw = b['uv_rect']
            assert not (ax < bz and bx < az and ay < bw and by < aw), (a['name'], b['name'])


PALETTE = {
    'stone': (132, 136, 140), 'pale_stone': (155, 160, 161), 'runed_stone': (139, 143, 146),
    'dark_stone': (69, 73, 77), 'runed_dark': (76, 79, 82), 'joint': (42, 44, 48),
    'redstone': (100, 14, 10), 'conduit': (160, 29, 15), 'ember_frame': (214, 60, 13),
    'eye': (255, 235, 165), 'furnace': (146, 20, 9), 'cavity': (10, 8, 9),
}


def rgb(value, delta=0):
    return [max(0, min(255, int(c + delta))) for c in value]


def faces(b):
    u, v = b['uv']; w, h, d = b['size']
    return dict(up=(u + d, v, w, d), down=(u + d + w, v, w, d),
                east=(u, v + d, d, h), north=(u + d, v + d, w, h),
                west=(u + d + w, v + d, d, h), south=(u + d + w + d, v + d, w, h))


def draw_atlas():
    pixels = [[0, 0, 0, 0] for _ in range(SIZE * SIZE)]
    glow = [[0, 0, 0, 0] for _ in range(SIZE * SIZE)]
    for b in BOXES:
        material = b['material']; base = PALETTE[material]
        for face, (u, v, w, h) in faces(b).items():
            seed = int.from_bytes(hashlib.sha256((b['name'] + ':' + face).encode()).digest()[:4], 'big')
            rng = random.Random(seed)
            def put(x, y, c, emission=None):
                if 0 <= x < w and 0 <= y < h:
                    at = (v + y) * SIZE + u + x
                    pixels[at] = list(c) + [255]
                    if emission is not None: glow[at] = list(emission) + [255]
            for yy in range(h):
                for xx in range(w):
                    cluster = ((xx // 3 * 41 + yy // 3 * 19 + seed) % 9) - 4
                    small = ((xx * 11 + yy * 7 + seed) % 3) - 1
                    tint = cluster + small + (5 if face == 'up' else -4 if face == 'down' else 0)
                    if material in ('redstone', 'furnace'):
                        tint = (cluster * 1.4) + (5 if xx < 2 or yy < 2 else 0)
                    put(xx, yy, rgb(base, tint))
            if material in ('stone', 'pale_stone', 'runed_stone', 'dark_stone', 'runed_dark', 'joint'):
                # Controlled, broad pixel clusters and hand-like stepped fault lines.
                if w >= 4 and h >= 4:
                    for xx in range(w):
                        put(xx, 0, rgb(base, 16)); put(xx, h - 1, rgb(base, -19))
                    for yy in range(1, h - 1):
                        put(0, yy, rgb(base, 7)); put(w - 1, yy, rgb(base, -12))
                if w >= 8 and h >= 7 and material != 'joint':
                    for _ in range(max(1, (w * h) // 180)):
                        x, y = rng.randrange(1, max(2, w - 2)), rng.randrange(1, max(2, h - 2))
                        for step in range(rng.randint(3, 7)):
                            put(x, y, rgb(base, -33)); put(x - 1, y, rgb(base, -14))
                            if step % 2: x += rng.choice((-1, 1))
                            else: y += 1
                if material.startswith('runed') and w >= 7 and h >= 6:
                    cx, cy = rng.randrange(2, w - 2), rng.randrange(2, h - 2)
                    for yy in range(cy-2, cy+2):
                        for xx in range(cx-2, cx+2):
                            put(xx, yy, (74, 30, 26))
                    for yy in range(cy-1, cy+1):
                        for xx in range(cx-1, cx+1): put(xx, yy, (166, 33, 15), (82, 13, 7))
                    put(cx, cy, (212, 57, 18), (114, 23, 8))
            elif material == 'redstone':
                for yy in range(h):
                    for xx in range(w):
                        edge = min(xx, yy, w - 1 - xx, h - 1 - yy)
                        if edge == 0: put(xx, yy, (65, 12, 10))
                        elif edge == 1: put(xx, yy, (193, 39, 15), (61, 9, 4))
                        elif edge == 2 and min(w, h) > 5: put(xx, yy, (143, 22, 10))
                if min(w, h) >= 8:
                    for _ in range(max(1, w * h // 130)):
                        x, y = rng.randrange(3, w - 3), rng.randrange(3, h - 3)
                        for xx in range(x, min(w-2, x+3)): put(xx, y, (135, 29, 13))
            elif material == 'conduit':
                horizontal = w >= h
                for yy in range(h):
                    for xx in range(w):
                        t = yy if horizontal else xx; span = h if horizontal else w
                        c = (102, 15, 11) if t in (0, span-1) and span > 2 else (196, 43, 17)
                        em = (70, 13, 5) if c[0] > 150 else None
                        put(xx, yy, c, em)
            elif material == 'ember_frame':
                for yy in range(h):
                    for xx in range(w): put(xx, yy, (220, 64, 15), (139, 36, 5))
            elif material == 'eye':
                for yy in range(h):
                    for xx in range(w):
                        c = (255, 243, 196) if face in ('north', 'up') else (255, 215, 127)
                        put(xx, yy, c, c)
            elif material == 'furnace':
                for yy in range(h):
                    for xx in range(w):
                        edge = min(xx, yy, w - 1 - xx, h - 1 - yy)
                        c = (74, 9, 6) if edge == 0 else (176 + (xx * 13 + yy * 7) % 31, 27 + (yy % 3)*10, 8)
                        if face == 'up' and (xx in (w//3, w//3+1, 2*w//3) or yy == h//2): c = (243, 66, 12)
                        put(xx, yy, c, [int(c[0]*.72), int(c[1]*.72), int(c[2]*.72)])
            # Extrude the outer edge of each island by one texel to avoid mip seams.
        x0, y0, x1, y1 = b['uv_rect']
        for xx in range(x0, x1):
            for dest, src in [((y0-1)*SIZE+xx, y0*SIZE+xx), (y1*SIZE+xx, (y1-1)*SIZE+xx)]:
                pixels[dest] = pixels[src].copy(); glow[dest] = glow[src].copy()
        for yy in range(y0-1, y1+1):
            for dest, src in [(yy*SIZE+x0-1, yy*SIZE+x0), (yy*SIZE+x1, yy*SIZE+x1-1)]:
                pixels[dest] = pixels[src].copy(); glow[dest] = glow[src].copy()
    return pixels, glow


def png(path, pixels):
    def chunk(kind, data):
        return struct.pack('>I', len(data)) + kind + data + struct.pack('>I', zlib.crc32(kind + data) & 0xffffffff)
    raw = b''.join(b'\0' + bytes(sum(pixels[y*SIZE:(y+1)*SIZE], [])) for y in range(SIZE))
    data = b'\x89PNG\r\n\x1a\n' + chunk(b'IHDR', struct.pack('>IIBBBBB', SIZE, SIZE, 8, 6, 0, 0, 0)) + chunk(b'IDAT', zlib.compress(raw, 9)) + chunk(b'IEND', b'')
    path.parent.mkdir(parents=True, exist_ok=True); path.write_bytes(data)
    return data


def jfloat(n):
    return f'{float(n):g}F'


def java_geometry():
    lines = ['package de.laugenbreze.dungeonsalive.client.entity;', '',
             'import net.minecraft.client.model.geom.PartPose;',
             'import net.minecraft.client.model.geom.builders.CubeListBuilder;',
             'import net.minecraft.client.model.geom.builders.LayerDefinition;',
             'import net.minecraft.client.model.geom.builders.MeshDefinition;',
             'import net.minecraft.client.model.geom.builders.PartDefinition;', '',
             '/** Original mesh generated by tools/generate_redstone_monstrosity.py. All UV islands are disjoint. */',
             'public final class RedstoneMonstrosityGeometry {', '    private RedstoneMonstrosityGeometry() {}',
             '    public static LayerDefinition createLayer() {', '        MeshDefinition mesh = new MeshDefinition();',
             '        PartDefinition root = mesh.getRoot();']
    for n in NODES:
        lines.append(f'        PartDefinition {n["name"]} = {n["parent"]}.addOrReplaceChild("{n["name"]}", CubeListBuilder.create(), PartPose.offset({", ".join(map(jfloat,n["pivot"]))}));')
    for b in BOXES:
        u, v = b['uv']; values = b['pos'] + b['size']
        lines.append(f'        {b["parent"]}.addOrReplaceChild("{b["name"]}", CubeListBuilder.create().texOffs({u}, {v}).addBox({", ".join(map(jfloat,values))}), PartPose.ZERO);')
    lines += [f'        return LayerDefinition.create(mesh, {SIZE}, {SIZE});', '    }', '}', '']
    JAVA.mkdir(parents=True, exist_ok=True)
    (JAVA / 'RedstoneMonstrosityGeometry.java').write_text('\n'.join(lines), encoding='utf-8')


def blockbench(png_data):
    ids = {name: str(uuid.uuid5(uuid.NAMESPACE_URL, 'dungeonsalive:redstone_monstrosity/' + name)) for name in ['root'] + [n['name'] for n in NODES] + [b['name'] for b in BOXES]}
    positions = {'root': (0, 0, 0)}
    for n in NODES: positions[n['name']] = tuple(a+b for a,b in zip(positions[n['parent']], n['pivot']))
    elements = []
    for b in BOXES:
        px, py, pz = positions[b['parent']]; x,y,z=b['pos'];w,h,d=b['size']
        f = {name: {'uv': [u,v,u+fw,v+fh], 'texture': 0} for name,(u,v,fw,fh) in faces(b).items()}
        elements.append({'name': b['name'], 'type':'cube', 'uuid':ids[b['name']], 'box_uv':False,
                         'from':[px+x,24-(py+y+h),pz+z], 'to':[px+x+w,24-(py+y),pz+z+d],
                         'origin':[px,24-py,pz], 'rotation':[0,0,0], 'faces':f})
    def group(n):
        x,y,z=positions[n['name']]
        return {'name':n['name'], 'origin':[x,24-y,z], 'rotation':[0,0,0], 'uuid':ids[n['name']],
                'children':[group(c) for c in NODES if c['parent']==n['name']] + [ids[b['name']] for b in BOXES if b['parent']==n['name']]}
    out = {'meta':{'format_version':'4.10','model_format':'free','box_uv':False}, 'name':'Dungeons Alive – Redstone Monstrosity',
           'model_identifier':'dungeonsalive:redstone_monstrosity', 'visible_box':[5,4,0], 'resolution':{'width':SIZE,'height':SIZE},
           'elements':elements, 'outliner':[group(n) for n in NODES if n['parent']=='root'],
           'textures':[{'name':'stone.png','id':'0','uuid':str(uuid.uuid5(uuid.NAMESPACE_URL,'dungeonsalive:redstone_monstrosity/stone')),
                        'mode':'bitmap','saved':False,'source':'data:image/png;base64,'+base64.b64encode(png_data).decode()}],
           'animations':[]}
    dest=ROOT/'design/models/redstone_monstrosity.bbmodel';dest.parent.mkdir(parents=True,exist_ok=True)
    dest.write_text(json.dumps(out,ensure_ascii=False,indent=2)+'\n',encoding='utf-8')


def main():
    geometry();separate_coplanar_surfaces();pack(); stone,glow=draw_atlas()
    stone_data=png(TEXTURES/'stone.png',stone);png(TEXTURES/'glow.png',glow)
    java_geometry();blockbench(stone_data)
    print(f'Generated {len(BOXES)} original cuboids, {len(NODES)} rig nodes, two {SIZE}x{SIZE} atlases, Java mesh and editable Blockbench model.')
    print('UV validation: all islands disjoint, within atlas, with padding; no external image input.')


if __name__=='__main__':main()
