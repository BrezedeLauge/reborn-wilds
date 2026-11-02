#!/usr/bin/env python3
"""Reproduce all original workstation pixels, profession overlays and resource JSON (stdlib only)."""
from pathlib import Path
import json
import struct
import zlib

ROOT = Path(__file__).resolve().parents[1]
RES = ROOT / "src/main/resources"
ASSETS = RES / "assets/dungeonsalive"
DATA = RES / "data/dungeonsalive"
ROLES = [
    ("village_merchant", "merchant_counter", (47, 113, 76), (221, 182, 82), ["emerald"]),
    ("luxury_merchant", "luxury_counter", (122, 52, 82), (235, 192, 80), ["gold_ingot", "amethyst_shard"]),
    ("mystery_merchant", "mystery_counter", (66, 49, 98), (97, 190, 179), ["ender_pearl", "purple_dye"]),
    ("blacksmith", "blacksmith_station", (67, 61, 54), (195, 142, 70), ["iron_block", "coal"]),
    ("gift_wrapper", "gift_wrapping_table", (145, 43, 49), (235, 214, 150), ["paper", "string", "red_dye"]),
    ("enchantsmith", "enchantsmith_table", (68, 43, 119), (105, 202, 224), ["book", "lapis_lazuli", "amethyst_shard"]),
]


def save_json(path, value):
    path.parent.mkdir(parents=True, exist_ok=True)
    path.write_text(json.dumps(value, indent=2, ensure_ascii=False) + "\n", encoding="utf-8")


def save_png(path, width, height, pixels):
    def chunk(kind, data):
        return struct.pack(">I", len(data)) + kind + data + struct.pack(">I", zlib.crc32(kind + data) & 0xFFFFFFFF)
    path.parent.mkdir(parents=True, exist_ok=True)
    raw = b"".join(b"\0" + bytes(c for px in pixels[y * width:(y + 1) * width] for c in px) for y in range(height))
    path.write_bytes(b"\x89PNG\r\n\x1a\n" + chunk(b"IHDR", struct.pack(">IIBBBBB", width, height, 8, 6, 0, 0, 0))
                     + chunk(b"IDAT", zlib.compress(raw, 9)) + chunk(b"IEND", b""))


def shade(color, amount):
    return tuple(max(0, min(255, value + amount)) for value in color) + (255,)


def coat(path, main, trim):
    pixels = [(0, 0, 0, 0)] * 4096
    def rect(x0, y0, x1, y1, color):
        for y in range(y0, y1):
            for x in range(x0, x1):
                pixels[y * 64 + x] = color
    rect(16, 26, 44, 38, shade(main, -8))
    rect(22, 26, 30, 38, shade(main, 0))
    rect(44, 26, 60, 36, shade(main, -5))
    rect(0, 44, 28, 62, shade(main, -10))
    rect(6, 44, 14, 62, shade(main, 0))
    rect(6, 45, 7, 61, shade(trim, -8))
    rect(13, 45, 14, 61, shade(trim, -8))
    rect(7, 52, 13, 54, shade(trim, -4))
    rect(8, 46, 12, 50, shade(trim, 4))
    rect(9, 47, 11, 49, shade(main, 15))
    rect(8, 56, 12, 60, shade(main, -24))
    save_png(path, 64, 64, pixels)
    save_json(path.with_name(path.name + ".mcmeta"), {"villager": {"hat": "none"}})


def cube(start, end, texture):
    return {"from": start, "to": end,
            "faces": {side: {"texture": texture} for side in ("north", "south", "west", "east", "up", "down")}}


def main():
    for role, block, color, trim, ingredients in ROLES:
        coat(ASSETS / f"textures/entity/villager/profession/{role}.png", color, trim)
        cloth = []
        for y in range(16):
            for x in range(16):
                line = x in (1, 14) or y in (1, 14)
                cloth.append(shade(trim if line else color, ((x * 7 + y * 3) % 5) - 2))
        save_png(ASSETS / f"textures/block/{block}_cloth.png", 16, 16, cloth)
        wood = "minecraft:block/dark_oak_planks" if role in ("enchantsmith", "mystery_merchant") else "minecraft:block/spruce_planks"
        elements = [cube([1, 0, 1], [4, 11, 4], "#wood"), cube([12, 0, 1], [15, 11, 4], "#wood"),
                    cube([1, 0, 12], [4, 11, 15], "#wood"), cube([12, 0, 12], [15, 11, 15], "#wood"),
                    cube([0, 10, 0], [16, 12, 16], "#wood"), cube([1, 12, 1], [15, 13, 15], "#cloth")]
        if role == "blacksmith":
            elements += [cube([4, 12, 4], [12, 13, 12], "#metal"), cube([6, 13, 5], [10, 14, 11], "#metal")]
        elif role == "gift_wrapper":
            elements += [cube([5, 12, 5], [11, 14, 11], "#cloth"), cube([7, 13, 4], [9, 14, 12], "#trim")]
        elif role == "enchantsmith":
            elements += [cube([4, 13, 4], [12, 14, 11], "#book")]
        else:
            elements += [cube([3, 13, 5], [6, 14, 9], "#trim"), cube([9, 13, 4], [13, 14, 10], "#cloth")]
        save_json(ASSETS / f"models/block/{block}.json", {
            "parent": "minecraft:block/block", "render_type": "minecraft:cutout",
            "textures": {"wood": wood, "cloth": f"dungeonsalive:block/{block}_cloth", "particle": wood,
                         "metal": "minecraft:block/iron_block", "trim": "minecraft:block/gold_block", "book": "minecraft:block/bookshelf"},
            "elements": elements})
        save_json(ASSETS / f"models/item/{block}.json", {"parent": f"dungeonsalive:block/{block}"})
        save_json(ASSETS / f"blockstates/{block}.json", {"variants": {
            f"facing={direction}": {"model": f"dungeonsalive:block/{block}", "y": rotation}
            for direction, rotation in (("north", 0), ("east", 90), ("south", 180), ("west", 270))}})
        save_json(DATA / f"loot_table/blocks/{block}.json", {"type": "minecraft:block", "pools": [
            {"rolls": 1, "entries": [{"type": "minecraft:item", "name": f"dungeonsalive:{block}"}],
             "conditions": [{"condition": "minecraft:survives_explosion"}]}]})
        save_json(DATA / f"recipe/{block}.json", {"type": "minecraft:crafting_shapeless", "category": "misc",
            "ingredients": [{"item": "minecraft:crafting_table"}] + [{"item": "minecraft:" + item} for item in ingredients],
            "result": {"id": f"dungeonsalive:{block}", "count": 1}})
    save_json(RES / "data/minecraft/tags/point_of_interest_type/acquirable_job_site.json",
              {"replace": False, "values": [f"dungeonsalive:{role}" for role, *_ in ROLES]})
    save_json(RES / "data/minecraft/tags/block/mineable/axe.json",
              {"replace": False, "values": [f"dungeonsalive:{block}" for _, block, *_ in ROLES]})


if __name__ == "__main__":
    main()
