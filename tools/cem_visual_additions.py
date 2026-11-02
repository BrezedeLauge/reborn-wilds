"""Explicit original visual additions to two attributed ReCrafted adaptations.

The small textures in this module are drawn from numeric colors and UV layouts,
not sampled from screenshots or extracted game assets. Source PNG pixels and
snapshots are preserved. Composite adapted assets retain CC BY-NC-SA 4.0.
"""

from __future__ import annotations

import math
from typing import Any


ORB = "dungeonsalive-generated:arch_orb.png"
ORB_GLOW = "dungeonsalive-generated:arch_orb_e.png"
TENTACLES = "dungeonsalive-generated:baby_squid_tentacles.png"
GENERATED_TEXTURES = frozenset((ORB, ORB_GLOW, TENTACLES))


def generated_texture(name: str):
    """Return an original deterministic pixel tile; never mutate a source image."""
    from PIL import Image

    if name not in GENERATED_TEXTURES:
        raise ValueError(f"Unknown original visual tile: {name}")
    if name in (ORB, ORB_GLOW):
        image = Image.new("RGBA", (32, 32), (0, 0, 0, 0))
        # Six 5x5 faces in the normal Java cube net. The official reference is a
        # luminous rose-colored cube, not a downloaded/extracted texture.
        faces = [(5, 0), (10, 0), (0, 5), (5, 5), (10, 5), (15, 5)]
        for face, (u, v) in enumerate(faces):
            for y in range(5):
                for x in range(5):
                    edge = min(x, y, 4 - x, 4 - y) == 0
                    highlight = (x, y) in ((1, 1), (2, 1), (1, 2))
                    color = (167, 37, 83, 255) if edge else (224, 74, 127, 255)
                    if highlight:
                        color = (255, 166, 201, 255)
                    if face == 1:
                        color = tuple(int(c * .78) for c in color[:3]) + (255,)
                    if name == ORB_GLOW:
                        color = (247, 102, 159, 255) if not edge else (124, 30, 65, 255)
                        if highlight:
                            color = (255, 191, 216, 255)
                    image.putpixel((u + x, v + y), color)
        return image
    image = Image.new("RGBA", (16, 16), (19, 65, 82, 255))
    # Independently drawn opaque teal bands. A filled tile also gives fractional
    # distal-tip UVs solid neighboring pixels at the exact source-scale size.
    for y in range(16):
        for x in range(16):
            band = (x + y * 2) % 7
            color = (35, 100, 116, 255) if band < 2 else (19, 65, 82, 255)
            if y % 5 == 3:
                color = (65, 135, 142, 255)
            image.putpixel((x, y), color)
    return image


def adapt(identifier: str, bones: list[Any], expressions: list[dict[str, Any]],
          modifications: list[str], Bone: type, Box: type) -> list[dict[str, Any]]:
    """Modify converted data only; the exact audited input files are read-only."""
    if identifier not in ("arch_illager", "pet_baby_squid"):
        return []
    by_name = {bone.name: bone for bone in bones}
    added_bones: list[str] = []

    def expression(target: str, value: str, why: str) -> None:
        expressions.append({"target": target, "expression": value,
                            "source_bone": target.split(".", 1)[0].removeprefix("EMF_"),
                            "origin": "dungeonsalive_original_visual_adaptation",
                            "reason": why})

    def box(position, dimensions, texture, size, uv=(0., 0.)):
        return Box(list(position), list(dimensions), list(uv), [0., 0., 0.], False,
                   texture=texture, texture_size=list(size))

    if identifier == "arch_illager":
        staff = by_name["bone14"]
        arm = by_name["right_arm6"]
        # Confirm the documented attachment before changing the pose. A source
        # hierarchy change must not silently rotate some unrelated future bone.
        if staff.parent != arm.name or arm.parent != "body5":
            raise ValueError("Arch staff no longer uses body5/right_arm6/bone14")
        angle = math.radians(55)
        arm.rotate = [0., 0., -angle]
        # The thin authored hand is at arm-local (1.5,5,0). Counter-rotate the
        # staff to world vertical and put the shaft through that actual hand.
        grip_x = 1.5 * math.cos(angle) + 5 * math.sin(angle)
        delta_x = grip_x - .5
        delta_y = -5.
        staff.translate = [delta_x * math.cos(angle) - delta_y * math.sin(angle),
                           delta_x * math.sin(angle) + delta_y * math.cos(angle), -.35]
        staff.rotate = [0., 0., angle]
        orb = Bone("da_arch_orb", staff.name, [.5, -6.5, 0.],
                   [math.radians(8), math.radians(45), math.radians(8)],
                   boxes=[box((-2.5, -2.5, -2.5), (5., 5., 5.), ORB, (32, 32))])
        bones.append(orb)
        added_bones.append(orb.name)
        expression("EMF_da_arch_orb.ry", "torad(45) + age * 0.012",
                   "Own slow rotation of the newly modeled luminous cube")
        expression("EMF_da_arch_orb.ty", "-6.5 + sin(age * 0.09) * 0.12",
                   "Own restrained floating motion above the authored staff cup")
        expression("EMF_right_leg2.rx", "sin(limb_swing * 0.6662) * limb_speed * 0.55",
                   "Own walking adaptation driven by actual networked movement")
        expression("EMF_left_leg2.rx", "-sin(limb_swing * 0.6662) * limb_speed * 0.55",
                   "Opposite leg follows the same actual locomotion signal")
        expression("EMF_right_arm5.rx", "-sin(swing_progress * pi) * torad(80) + sin(limb_swing * 0.6662) * limb_speed * 0.25",
                   "Own free-hand spell gesture driven by the synchronized server action window; staff arm stays upright")
        modifications.extend([
            "Own visual correction at arch_illager/body5/right_arm6/bone14: raised grip and counter-rotated vertical staff; original snapshot and staff UV pixels unchanged.",
            "Own five-pixel rose orb, slow floating/turning motion and dedicated original emissive tile added; not an extracted Minecraft Dungeons mesh, texture or keyframe.",
            "Visual shape/color reference only: official Search for the Arch-Illager, Part 2, Image4; reference image is not packaged as a texture.",
            "Own leg movement and free-hand spell gesture use actual locomotion and synchronized server action progress.",
        ])
        return [{"kind": "original_visual_addition", "bones": added_bones, "box_count": 1,
                 "generated_textures": [ORB, ORB_GLOW],
                 "changed_rest_poses": [arm.name, staff.name],
                 "reference_url": "https://www.minecraft.net/en-us/article/search-arch-illager--part-2",
                 "reference_image_url": "https://www.minecraft.net/content/dam/minecraftnet/games/dungeons/game-characters/Search-Archillager-Part2-Image4.jpg",
                 "original_source_pixels_preserved": True}]

    body = by_name["babyghast"]
    if body.parent != "body" or not any(b.dimensions == [6., 6., 6.] for b in body.boxes):
        raise ValueError("Baby Squid body no longer matches the inspected six-pixel source cube")
    for i in range(8):
        azimuth = i * math.tau / 8
        name = f"da_squid_arm_{i + 1}"
        tip_name = f"da_squid_tip_{i + 1}"
        # All appendages belong to the authored small body. Reusing native adult
        # tentacle pivots would place them around a much larger invisible body.
        arm = Bone(name, body.name, [math.cos(azimuth) * 2.25, 2.75, 2 + math.sin(azimuth) * 2.25],
                   [math.radians(15), math.pi / 2 - azimuth, 0.],
                   boxes=[box((-.5, 0., -.5), (1., 3., 1.), TENTACLES, (16, 16))])
        tip = Bone(tip_name, name, [0., 2.85, 0.], [math.radians(-10), 0., 0.],
                   boxes=[box((-.4, 0., -.4), (.8, 2., .8), TENTACLES, (16, 16), (4., 0.))])
        bones.extend((arm, tip))
        added_bones.extend((name, tip_name))
        phase = i * math.pi / 4
        expression(f"EMF_{name}.rx",
                   f"if(is_in_water, torad(24) + sin(age * 0.19 + {phase:.8f}) * (0.14 + min(limb_speed, 0.4) * 0.5), torad(12) + sin(age * 0.10 + {phase:.8f}) * 0.06)",
                   "Own water pulse on a short, visible proximal tentacle; actual movement controls amplitude")
        expression(f"EMF_{tip_name}.rx",
                   f"if(is_in_water, -torad(20) + sin(age * 0.19 + {phase + 1.2:.8f}) * 0.22, -torad(12) + sin(age * 0.10 + {phase + 1.2:.8f}) * 0.05)",
                   "Own delayed distal bend, without gameplay motion or an invented original clip")
    expression("EMF_babyghast.ty", "if(is_in_water, -5 + sin(age * 0.095) * 0.18, -5)",
               "Own small underwater body pulse; entity position remains server-owned")
    modifications.extend([
        "Own eight short two-segment opaque tentacles added under the inspected babyghast source-body node; empty adult-host parts and transparent original planes remain source-authentic.",
        "Original source body geometry and PNG pixels preserved; new tentacle pixels are independently generated teal bands in a separate atlas tile.",
        "Own water/idle tentacle waves use actual water contact, entity age and networked limb speed; no original Dungeons animation extraction or autonomous swim AI is claimed.",
    ])
    return [{"kind": "original_visual_addition", "bones": added_bones, "box_count": 16,
             "generated_textures": [TENTACLES], "changed_rest_poses": [],
             "original_source_pixels_preserved": True,
             "purpose": "Visible articulated appendages replacing the prior visual gap without rewriting the audited source"}]
