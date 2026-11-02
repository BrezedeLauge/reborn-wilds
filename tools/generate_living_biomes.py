#!/usr/bin/env python3
"""Generate original, bounded landscape recipes; vanilla content is referenced, never copied.

Only new chunk decoration changes. The optional Living World preset is registered elsewhere.
--check never writes. --minecraft-jar additionally checks registry references against the
locally installed 1.21.1 resources; it is not a substitute for Minecraft codec/client tests.
"""
from __future__ import annotations

import argparse
import json
from pathlib import Path
from zipfile import ZipFile

ROOT = Path(__file__).resolve().parents[1]
DATA = Path("src/main/resources/data")
MOD = DATA / "dungeonsalive"
PREFIX = "dungeonsalive:living/"
BIOMES = ("amber_grove", "luminous_marsh", "ember_crags")
SOIL = ["minecraft:grass_block", "minecraft:dirt", "minecraft:coarse_dirt",
        "minecraft:podzol", "minecraft:rooted_dirt", "minecraft:moss_block",
        "minecraft:mud", "dungeonsalive:amber_moss", "dungeonsalive:luminous_moss"]
FAMILIES = {
    "woodland": ["#minecraft:is_forest", "#minecraft:is_taiga"],
    "wetland": ["minecraft:swamp", "minecraft:mangrove_swamp"],
    "dryland": ["#minecraft:is_savanna", "#minecraft:is_badlands"],
}
ORE_NAMES = ("dirt gravel granite_upper granite_lower diorite_upper diorite_lower "
             "andesite_upper andesite_lower tuff coal_upper coal_lower iron_upper iron_middle "
             "iron_small gold gold_lower redstone redstone_lower diamond diamond_medium "
             "diamond_large diamond_buried lapis lapis_buried copper").split()


def state(name, **properties):
    out = {"Name": name}
    if properties:
        out["Properties"] = properties
    return out


def provider(name, **properties):
    return {"type": "minecraft:simple_state_provider", "state": state(name, **properties)}


def matching(blocks, offset=None):
    out = {"type": "minecraft:matching_blocks", "blocks": blocks}
    if offset is not None:
        out["offset"] = offset
    return out


def all_of(*predicates):
    return {"type": "minecraft:all_of", "predicates": list(predicates)}


def predicate_filter(predicate):
    return {"type": "minecraft:block_predicate_filter", "predicate": predicate}


def configured(kind, config):
    return {"type": "minecraft:" + kind, "config": config}


def placed(feature, *, count=None, rarity=None, heightmap="MOTION_BLOCKING_NO_LEAVES", offset=0, predicate=None):
    # One attempt unless explicitly given a small count; every outer placement has a biome gate.
    placement = []
    if count is not None:
        placement.append({"type": "minecraft:count", "count": count})
    if rarity is not None:
        placement.append({"type": "minecraft:rarity_filter", "chance": rarity})
    placement += [{"type": "minecraft:in_square"}, {"type": "minecraft:heightmap", "heightmap": heightmap}]
    if offset:
        placement.append({"type": "minecraft:random_offset", "xz_spread": 0, "y_spread": offset})
    if predicate is not None:
        placement.append(predicate_filter(predicate))
    placement.append({"type": "minecraft:biome"})
    return {"feature": feature, "placement": placement}


def disk(block):
    return configured("disk", {
        "half_height": 0, "radius": {"type": "minecraft:uniform", "min_inclusive": 1, "max_inclusive": 2},
        "state_provider": {"fallback": provider(block), "rules": []},
        "target": all_of(matching(SOIL), matching("minecraft:air", [0, 1, 0])),
    })


def deadwood(log):
    # Both origin and every following column cell require air over natural soil.
    # Vanilla BlockColumn checks the following position first, so the origin needs its own filter.
    safe = all_of(matching("minecraft:air"), matching(SOIL, [0, -1, 0]))
    def branch(direction, axis):
        return {"feature": configured("block_column", {
            "direction": direction, "prioritize_tip": False, "allowed_placement": safe,
            "layers": [{"height": {"type": "minecraft:uniform", "min_inclusive": 2, "max_inclusive": 4},
                        "provider": provider(log, axis=axis)}],
        }), "placement": [predicate_filter(safe)]}
    return configured("random_boolean_selector", {"feature_true": branch("east", "x"), "feature_false": branch("south", "z")})


def flowers(blocks, tries=12):
    choices = [{"data": state(block), "weight": 1} for block in blocks]
    return configured("random_patch", {
        "tries": tries, "xz_spread": 3, "y_spread": 2,
        "feature": {"feature": configured("simple_block", {
            "to_place": {"type": "minecraft:weighted_state_provider", "entries": choices},
        }), "placement": [predicate_filter(matching("minecraft:air"))]},
    })


def spawn(entity, weight, low=2, high=4):
    return {"type": "minecraft:" + entity, "weight": weight, "minCount": low, "maxCount": high}


def biome(name, *, temperature, downfall, colors, trees, plants, creatures):
    features = [[] for _ in range(11)]
    features[1] = ["minecraft:lake_lava_underground"]
    if name == "luminous_marsh":
        features[1].append(PREFIX + "marsh_pool")
    features[2] = ["minecraft:amethyst_geode"]
    features[3] = ["minecraft:monster_room", "minecraft:monster_room_deep"]
    features[6] = ["minecraft:ore_" + ore for ore in ORE_NAMES] + [
        "minecraft:underwater_magma", "minecraft:disk_sand", "minecraft:disk_clay", "minecraft:disk_gravel"]
    features[8] = ["minecraft:spring_water", "minecraft:spring_lava"]
    # Shared vanilla entries retain their relative order. Every original decoration follows them.
    features[9] = ["minecraft:glow_lichen"] + plants + [
        "minecraft:brown_mushroom_normal", "minecraft:red_mushroom_normal",
        "minecraft:patch_sugar_cane", "minecraft:patch_pumpkin", PREFIX + trees]
    features[10] = ["minecraft:freeze_top_layer"]
    monsters = [spawn("spider", 100, 4, 4), spawn("zombie", 95, 4, 4),
                spawn("zombie_villager", 5, 1, 1), spawn("skeleton", 100, 4, 4),
                spawn("creeper", 100, 4, 4), spawn("slime", 100, 4, 4),
                spawn("enderman", 10, 1, 4), spawn("witch", 5, 1, 1)]
    return {
        "has_precipitation": downfall > 0, "temperature": temperature, "downfall": downfall,
        "effects": {key: int(value, 16) for key, value in colors.items()} | {
            "mood_sound": {"sound": "minecraft:ambient.cave", "tick_delay": 6000, "block_search_extent": 8, "offset": 2.0}},
        "carvers": {"air": ["minecraft:cave", "minecraft:cave_extra_underground", "minecraft:canyon"]},
        "features": features, "spawn_costs": {}, "spawners": {
            "ambient": [spawn("bat", 10, 8, 8)], "axolotls": [], "creature": creatures,
            "misc": [], "monster": monsters, "underground_water_creature": [spawn("glow_squid", 10, 4, 6)],
            "water_ambient": [], "water_creature": [],
        },
    }


def outputs():
    out = {}
    def resource(registry, name, data):
        out[MOD / "worldgen" / registry / ("living/" + name + ".json")] = data
    def feature(name, data, **placement):
        resource("configured_feature", name, data)
        resource("placed_feature", name, placed(PREFIX + name, **placement))
    ground = all_of(matching("minecraft:air"), matching(SOIL + ["minecraft:stone", "minecraft:red_sand", "minecraft:terracotta"], [0, -1, 0]))
    for name, block, rarity in [("amber_rocks", "minecraft:mossy_cobblestone", 4),
                                ("marsh_rocks", "minecraft:andesite", 6),
                                ("ember_rocks", "minecraft:red_sandstone", 3)]:
        feature(name, configured("forest_rock", {"state": state(block)}), rarity=rarity, predicate=ground)
    for name, block, rarity in [("amber_floor", "dungeonsalive:amber_moss", 2),
                                ("luminous_floor", "dungeonsalive:luminous_moss", 2),
                                ("ember_floor", "minecraft:red_sand", 3)]:
        feature(name, disk(block), rarity=rarity, offset=-1)
    feature("woodland_deadwood", deadwood("minecraft:oak_log"), rarity=6)
    feature("marsh_deadwood", deadwood("minecraft:mangrove_log"), rarity=5)
    feature("dry_deadwood", deadwood("minecraft:acacia_log"), rarity=9)
    feature("amber_flowers", flowers(["minecraft:dandelion", "minecraft:orange_tulip", "minecraft:oxeye_daisy"]), rarity=2)
    feature("marsh_flowers", flowers(["minecraft:blue_orchid", "minecraft:azure_bluet"]), rarity=2)
    feature("dry_scrub", flowers(["minecraft:dead_bush"], tries=6), rarity=3)
    feature("marsh_pool", configured("lake", {"fluid": provider("minecraft:water", level="0"),
                                                "barrier": provider("minecraft:mud")}), rarity=6, heightmap="WORLD_SURFACE_WG")
    feature("marsh_lichen", configured("multiface_growth", {
        "block": "minecraft:glow_lichen", "can_be_placed_on": ["minecraft:stone", "minecraft:andesite", "minecraft:mangrove_log", "minecraft:oak_log"],
        "can_place_on_ceiling": False, "can_place_on_floor": True, "can_place_on_wall": True,
        "chance_of_spreading": 0.15, "search_range": 4,
    }), rarity=2)
    # Tree shape stays Minecraft's genuine oak/birch, swamp/mangrove and acacia geometry.
    resource("placed_feature", "amber_trees", placed("minecraft:trees_birch_and_oak", count=3, heightmap="OCEAN_FLOOR"))
    resource("configured_feature", "marsh_trees", configured("random_selector", {
        "features": [{"chance": 0.2, "feature": "minecraft:mangrove_checked"}],
        "default": {"feature": "minecraft:swamp_oak", "placement": []},
    }))
    resource("placed_feature", "marsh_trees", placed(PREFIX + "marsh_trees", count=1, heightmap="OCEAN_FLOOR"))
    resource("placed_feature", "ember_trees", placed("minecraft:trees_savanna", count=1, heightmap="OCEAN_FLOOR"))
    common = [spawn("sheep", 8), spawn("pig", 8), spawn("chicken", 8), spawn("cow", 6)]
    specs = {
        "amber_grove": biome("amber_grove", temperature=0.65, downfall=0.6, trees="amber_trees",
            colors=dict(fog_color="D8D4C0", sky_color="9BBDD1", water_color="547E9B", water_fog_color="334B58", foliage_color="CEA34E", grass_color="A8B662"),
            plants=["minecraft:flower_default", "minecraft:patch_grass_forest"], creatures=common + [spawn("wolf", 3)]),
        "luminous_marsh": biome("luminous_marsh", temperature=0.8, downfall=0.9, trees="marsh_trees",
            colors=dict(fog_color="9FCFC6", sky_color="7EB9CE", water_color="2B7E85", water_fog_color="184D54", foliage_color="3D876F", grass_color="4B907A"),
            plants=["minecraft:flower_swamp", "minecraft:patch_grass_normal", "minecraft:patch_waterlily"],
            creatures=[spawn("pig", 6), spawn("chicken", 6), spawn("frog", 10, 2, 4)]),
        "ember_crags": biome("ember_crags", temperature=1.4, downfall=0.0, trees="ember_trees",
            colors=dict(fog_color="DDBD9A", sky_color="7EB8D9", water_color="4D9C99", water_fog_color="315F66", foliage_color="A59649", grass_color="B8A35D"),
            plants=["minecraft:flower_warm", "minecraft:patch_grass_normal"], creatures=common + [spawn("armadillo", 8, 2, 3)]),
    }
    decorations = {
        "woodland": ["amber_rocks", "amber_floor", "woodland_deadwood", "amber_flowers"],
        "wetland": ["marsh_rocks", "luminous_floor", "marsh_deadwood", "marsh_flowers", "marsh_lichen"],
        "dryland": ["ember_rocks", "ember_floor", "dry_deadwood", "dry_scrub"],
    }
    for name, family in zip(BIOMES, FAMILIES):
        # Custom and vanilla biomes use the SAME modifier path; features are not applied twice.
        out[MOD / "worldgen/biome" / (name + ".json")] = specs[name]
        out[MOD / "tags/worldgen/biome/living_decoration" / (family + ".json")] = {
            "replace": False, "values": FAMILIES[family] + ["dungeonsalive:" + name]}
        out[MOD / "neoforge/biome_modifier" / ("living_" + family + ".json")] = {
            "type": "neoforge:add_features", "biomes": "#dungeonsalive:living_decoration/" + family,
            "features": [PREFIX + feature for feature in decorations[family]], "step": "vegetal_decoration"}
    out[MOD / "tags/worldgen/biome/living_biomes.json"] = {"replace": False, "values": ["dungeonsalive:" + name for name in BIOMES]}
    for tag, names in {"is_overworld": BIOMES, "is_forest": ["amber_grove"], "is_savanna": ["ember_crags"]}.items():
        out[DATA / "minecraft/tags/worldgen/biome" / (tag + ".json")] = {"replace": False, "values": ["dungeonsalive:" + name for name in names]}
    out[Path("design/living-biomes.json")] = {
        "schema_version": 1, "generator": "tools/generate_living_biomes.py",
        "biomes": list(BIOMES), "world_preset": "dungeonsalive:living_world", "existing_worlds_keep_their_generator": True,
        "source": "Original biome palettes and decoration recipes; installed Minecraft 1.21.1 registry references and codecs only. No copied textures or world archives.",
        "terrain": "The optional preset retains vanilla Overworld noise. Ember Crags names an aesthetic; these JSON biomes do not impose cliff height or a new surface/noise engine.",
        "decoration": decorations,
        "bounds": {"tree_attempts_per_chunk": {"amber_grove": 3, "luminous_marsh": 1, "ember_crags": 1},
                   "ground_disk_radius": [1, 2], "ground_disk_layers": 1, "deadwood_blocks": [2, 4],
                   "flower_patch_attempts": 12, "flower_patch_xz_spread": 3,
                   "lichen_search_range": 4, "marsh_pool_attempt_chance_per_chunk": "1/6",
                   "marsh_pool_shape_limit": "Vanilla LakeFeature local 16 x 8 x 16 cells; not an unbounded water fill",
                   "decoration_placement": "Only new-chunk worldgen, each outer placement biome-filtered; no ticking decorator or player-block mutation"},
        "validation_limit": "Generated JSON/reference checks are not native world-generation or visual approval.",
        "generated_resources": sorted(str(path) for path in out),
    }
    return out


LANG = {
    "de_de": {"biome.dungeonsalive.amber_grove": "Bernsteinhain", "biome.dungeonsalive.luminous_marsh": "Nebelbruch", "biome.dungeonsalive.ember_crags": "Glutklippen"},
    "en_us": {"biome.dungeonsalive.amber_grove": "Amber Grove", "biome.dungeonsalive.luminous_marsh": "Luminous Marsh", "biome.dungeonsalive.ember_crags": "Ember Crags"},
}


def vanilla_references(generated, jar):
    """Resolve registry-resource references, including nested configured/placed features.

    Block/property/feature-type decoding is left to the actual game's registry codecs.
    """
    checked = set()
    with ZipFile(jar) as archive:
        names = set(archive.namelist())
        def reference(registry, value):
            if not isinstance(value, str) or not value.startswith("minecraft:"):
                return
            path = "data/minecraft/" + registry + "/" + value.split(":", 1)[1] + ".json"
            if path not in names:
                raise ValueError("Unknown Minecraft registry resource: " + path)
            checked.add(path)
        def walk(value):
            if isinstance(value, list):
                for child in value: walk(child)
            elif isinstance(value, dict):
                if "feature" in value:
                    reference("worldgen/configured_feature" if "placement" in value else "worldgen/placed_feature", value["feature"])
                for key in ("default", "feature_true", "feature_false"):
                    reference("worldgen/placed_feature", value.get(key))
                for child in value.values(): walk(child)
        for path, data in generated.items():
            walk(data)
            if "/worldgen/biome/" in str(path) and "/tags/" not in str(path):
                for step in data["features"]:
                    for feature in step: reference("worldgen/placed_feature", feature)
                for carvers in data["carvers"].values():
                    for carver in carvers: reference("worldgen/configured_carver", carver)
            if "/tags/worldgen/biome/" in str(path):
                for entry in data["values"]:
                    if entry.startswith("#minecraft:"): reference("tags/worldgen/biome", entry[1:])
                    else: reference("worldgen/biome", entry)
    return len(checked)


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--check", action="store_true", help="Compare generated files and language keys without writing")
    parser.add_argument("--minecraft-jar", type=Path, help="Installed Minecraft 1.21.1 client/resource jar, read only")
    args = parser.parse_args()
    generated = outputs()
    if args.minecraft_jar:
        print(f"Verified {vanilla_references(generated, args.minecraft_jar)} vanilla registry references")
    stale = []
    for path, data in generated.items():
        target = ROOT / path
        payload = json.dumps(data, indent=2, ensure_ascii=False) + "\n"
        if args.check:
            if not target.exists() or target.read_text() != payload: stale.append(str(path))
        else:
            target.parent.mkdir(parents=True, exist_ok=True)
            target.write_text(payload)
    for locale, keys in LANG.items():
        path = ROOT / "src/main/resources/assets/dungeonsalive/lang" / (locale + ".json")
        data = json.loads(path.read_text())
        if args.check:
            if any(data.get(key) != value for key, value in keys.items()): stale.append(str(path.relative_to(ROOT)))
        else:
            # Only these three owned keys change; read the current document immediately before its atomic replacement.
            data.update(keys)
            temporary = path.with_suffix(".living-biomes.tmp")
            temporary.write_text(json.dumps(data, indent=2, ensure_ascii=False) + "\n")
            temporary.replace(path)
    if stale:
        raise SystemExit("Stale living biome outputs:\n" + "\n".join(stale))
    print(f"{'Checked' if args.check else 'Generated'} {len(generated)} landscape resources/manifests and 6 language keys")


if __name__ == "__main__":
    main()
