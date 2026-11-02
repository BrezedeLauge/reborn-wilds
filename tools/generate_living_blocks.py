#!/usr/bin/env python3
"""Own buildable material definitions. Minecraft textures are references, never copied pixels."""
import json
import hashlib
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
RES = ROOT / "src/main/resources"
MATERIALS = {
    "amber_moss": ("Bernsteinmoos", "Amber Moss", "moss_block", "hoe"),
    "luminous_moss": ("Leuchtmoos", "Luminous Moss", "moss_block", "hoe"),
    "ancient_bricks": ("Uralte Steinziegel", "Ancient Bricks", "stone_bricks", "pickaxe"),
    "carved_ancient_bricks": ("Gemeißelte uralte Steinziegel", "Carved Ancient Bricks", "chiseled_stone_bricks", "pickaxe"),
    "wayfinder_stone": ("Alter Wegstein", "Ancient Waystone", "lodestone_side", "pickaxe"),
}

def write(path, value):
    path.parent.mkdir(parents=True, exist_ok=True)
    path.write_text(json.dumps(value, ensure_ascii=False, indent=2) + "\n")

def main():
    for key, (de, en, texture, tool) in MATERIALS.items():
        write(RES/f"assets/dungeonsalive/blockstates/{key}.json", {"variants": {"": {"model": f"dungeonsalive:block/{key}"}}})
        faces = {side: {"texture": "#all", "cullface": side, "tintindex": 0} for side in ("down", "up", "north", "south", "west", "east")}
        if key == "wayfinder_stone":
            for face in faces.values(): face.pop("tintindex")
        write(RES/f"assets/dungeonsalive/models/block/{key}.json", {"parent": "minecraft:block/block", "textures": {"all": f"minecraft:block/{texture}", "particle": "#all"}, "elements": [{"from": [0, 0, 0], "to": [16, 16, 16], "faces": faces}]})
        write(RES/f"assets/dungeonsalive/models/item/{key}.json", {"parent": f"dungeonsalive:block/{key}"})
        write(RES/f"data/dungeonsalive/loot_table/blocks/{key}.json", {"type": "minecraft:block", "pools": [{"rolls": 1, "conditions": [{"condition": "minecraft:survives_explosion"}], "entries": [{"type": "minecraft:item", "name": f"dungeonsalive:{key}"}]}]})
    recipes = {
        "amber_moss": (["minecraft:moss_block", "minecraft:yellow_dye"], 2),
        "luminous_moss": (["minecraft:moss_block", "minecraft:glow_ink_sac"], 2),
        "ancient_bricks": (["minecraft:stone_bricks", "minecraft:mossy_stone_bricks"], 2),
        "carved_ancient_bricks": (["dungeonsalive:ancient_bricks", "minecraft:amethyst_shard"], 1),
        "wayfinder_stone": (["dungeonsalive:carved_ancient_bricks", "minecraft:compass", "minecraft:glow_ink_sac"], 1),
    }
    for key, (ingredients, count) in recipes.items():
        write(RES/f"data/dungeonsalive/recipe/{key}.json", {"type": "minecraft:crafting_shapeless", "category": "building", "ingredients": [{"item": item} for item in ingredients], "result": {"id": f"dungeonsalive:{key}", "count": count}})
    for tool in ("hoe", "pickaxe"):
        path = RES/f"data/minecraft/tags/block/mineable/{tool}.json"
        data = json.loads(path.read_text()) if path.exists() else {"replace": False, "values": []}
        data["values"] = sorted(set(data["values"]) | {f"dungeonsalive:{key}" for key, spec in MATERIALS.items() if spec[3] == tool})
        write(path, data)
    path = RES / "data/minecraft/tags/block/dirt.json"
    data = json.loads(path.read_text()) if path.exists() else {"replace": False, "values": []}
    data["values"] = sorted(set(data["values"]) | {"dungeonsalive:amber_moss", "dungeonsalive:luminous_moss", "#dungeonsalive:corruption_soil"})
    write(path, data)
    for language, name_index in (("de_de", 0), ("en_us", 1)):
        path = RES/f"assets/dungeonsalive/lang/{language}.json"
        data = json.loads(path.read_text())
        data["itemGroup.dungeonsalive"] = "Reborn Wilds"
        for key, names in MATERIALS.items(): data[f"block.dungeonsalive.{key}"] = names[name_index]
        write(path, data)
    files = []
    for key in MATERIALS:
        for relative in (f"assets/dungeonsalive/blockstates/{key}.json", f"assets/dungeonsalive/models/block/{key}.json",
                         f"assets/dungeonsalive/models/item/{key}.json", f"data/dungeonsalive/loot_table/blocks/{key}.json",
                         f"data/dungeonsalive/recipe/{key}.json"):
            path = RES / relative
            files.append({"path": str(path.relative_to(ROOT)), "sha256": hashlib.sha256(path.read_bytes()).hexdigest(), "source": "own_definition_with_installed_minecraft_texture_reference"})
    write(ROOT/"design/living-blocks.json", {"generator": "tools/generate_living_blocks.py", "authorship": "Own MIT definitions, model quads and tint choices. Installed Minecraft textures referenced by ID; no copied game PNGs.", "materials": {key: {"de": v[0], "en": v[1], "texture_reference": f"minecraft:block/{v[2]}"} for key, v in MATERIALS.items()}, "delivered_files": files, "native_visual_review": False})

if __name__ == "__main__": main()
