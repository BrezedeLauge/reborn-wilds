#!/usr/bin/env python3
"""Curated, ordinary MONSTER biome entries; the legacy six Wilds IDs intentionally stay the same."""
from __future__ import annotations

import argparse
import json
import re
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
DATA = Path("src/main/resources/data/dungeonsalive")
FAMILIES = {
    "woodland": (["#minecraft:is_forest", "#minecraft:is_taiga", "minecraft:plains", "minecraft:sunflower_plains", "minecraft:meadow"],
                 [("royal_guard", 16, 1, 2), ("enchanter", 10, 1, 1)]),
    "desert": (["minecraft:desert", "#minecraft:is_badlands", "#minecraft:is_savanna"],
               [("vanguard", 16, 1, 2), ("necromancer", 10, 1, 1)]),
    "jungle": (["#minecraft:is_jungle"],
               [("whisperer", 10, 1, 1), ("leapleaf", 10, 1, 1), ("jungle_zombie", 18, 1, 2), ("mossy_skeleton", 14, 1, 2)]),
    "peaks": (["minecraft:frozen_peaks", "minecraft:jagged_peaks", "minecraft:stony_peaks", "minecraft:snowy_slopes"],
              [("iceologer", 12, 1, 1), ("wind_caller", 12, 1, 1)]),
    "swamp": (["minecraft:swamp", "minecraft:mangrove_swamp", "dungeonsalive:luminous_marsh"],
              [("wraith", 14, 1, 1)]),
    # Endermen have vanilla weight 10: copying Overworld weights here would replace most End encounters.
    "outer_end": (["minecraft:end_highlands", "minecraft:end_midlands", "minecraft:end_barrens", "minecraft:small_end_islands"],
                  [("watchling", 3, 1, 2), ("blastling", 2, 1, 1), ("snareling", 2, 1, 1)]),
    "frozen": (["minecraft:snowy_plains", "minecraft:ice_spikes", "minecraft:snowy_taiga", "minecraft:grove", "minecraft:snowy_slopes", "minecraft:frozen_peaks"],
               [("frozen_zombie", 18, 1, 2), ("icy_creeper", 12, 1, 1)]),
    "nether": (["#minecraft:is_nether"], [("piglin_hunter", 18, 1, 2), ("fungus_thrower", 10, 1, 1)]),
    "aquatic": (["#minecraft:is_ocean", "#minecraft:is_river", "minecraft:swamp", "minecraft:mangrove_swamp", "dungeonsalive:luminous_marsh"],
                [("armored_drowned", 14, 1, 2), ("sunken_skeleton", 10, 1, 2)]),
}
LEGACY_IDS = ("woodland", "desert", "jungle", "peaks", "swamp", "outer_end")


def outputs():
    source = (ROOT / "src/main/java/de/laugenbreze/dungeonsalive/entity/MobProfiles.java").read_text()
    tiers = dict(re.findall(r'add\("([a-z0-9_]+)",\s*"[^"]+",\s*"[^"]+",\s*(REGULAR|MINIBOSS|BOSS)', source))
    identifiers = [entry[0] for _, entries in FAMILIES.values() for entry in entries]
    if len(identifiers) != 20 or len(set(identifiers)) != 20:
        raise ValueError("The curated catalog must contain exactly 20 distinct ordinary opponents")
    for identifier in identifiers:
        if tiers.get(identifier) != "REGULAR": raise ValueError("Natural hostile is not a registered REGULAR profile: " + identifier)
    out = {}
    for family, (biomes, entries) in FAMILIES.items():
        if any(not (1 <= low <= high <= 2 and 1 <= weight <= 18) for _, weight, low, high in entries):
            raise ValueError("Unbounded pack or excessive spawn weight in " + family)
        out[DATA / "tags/worldgen/biome/has_natural_hostiles" / (family + ".json")] = {"replace": False, "values": biomes}
        # These exact six old names override, rather than combine with, an already installed Wilds pack.
        out[DATA / "neoforge/biome_modifier" / (family + ".json")] = {
            "type": "neoforge:add_spawns", "biomes": "#dungeonsalive:has_natural_hostiles/" + family,
            "spawners": [{"type": "dungeonsalive:" + identifier, "weight": weight, "minCount": low, "maxCount": high}
                         for identifier, weight, low, high in entries],
        }
    out[Path("design/natural-hostiles.json")] = {
        "schema_version": 1, "generator": "tools/generate_natural_hostiles.py",
        "default_enabled": True, "config": "dungeonsalive-hostiles-common.toml", "config_key": "hostiles.naturalHostiles",
        "spawn_system": "Native Minecraft MONSTER spawning and category caps; no independent hostile scheduler",
        "distinct_regular_types": len(identifiers), "bosses_or_minibosses": 0,
        "rules": {"overworld": "Native darkness and non-Peaceful difficulty", "nether_and_end": "Native any-light monster rule and non-Peaceful difficulty",
                  "aquatic": "Full body water volume, water surface height, free collision, registered IN_WATER placement; native Overworld darkness",
                  "pack_size": "1–2, stronger casters individually", "loaded_region": "Body and complete one-block collision edge checked before block reads",
                  "disabling": "Stops new NATURAL/CHUNK_GENERATION Dungeons opponents; retains existing mobs, eggs and encounter mechanics"},
        "families": {family: {"biomes": biomes, "spawns": [{"id": identifier, "weight": weight, "min": low, "max": high} for identifier, weight, low, high in entries]}
                     for family, (biomes, entries) in FAMILIES.items()},
        "legacy_pack": {"path": "examples/datapacks/dungeons_wilds", "same_modifier_ids": list(LEGACY_IDS),
                        "behavior": "Optional legacy low-density overrides for six families, not required for natural spawning. Identical resource IDs prevent duplicate entries. New frozen/nether/aquatic entries remain built in."},
        "validation_limit": "Generated profiles and resource checks do not prove that a natural spawn occurred in a running world.",
    }
    return out


LEGACY_README = """# Optionaler Wilds-Legacy-Override

Der aktuelle Mod verteilt bereits 20 ausgewählte reguläre Gegner über natürliche Biome.
Dieses ältere Pack ist **nicht mehr erforderlich**, damit Dungeons-Gegner natürlich erscheinen.

Wer bewusst die früheren sehr niedrigen Gewichte möchte, kann dieses Pack weiterverwenden.
Seine sechs Ressourcen-IDs (`woodland`, `desert`, `jungle`, `peaks`, `swamp`, `outer_end`)
sind identisch mit den eingebauten BiomeModifier-IDs. Minecraft ersetzt diese Einträge;
es addiert nicht einen zweiten Satz derselben Gegner. Das Pack behält außerdem seine
früheren engeren Biomelisten. Die neuen Familien `frozen`, `nether` und `aquatic` bleiben
von diesem Legacy-Override unberührt.

Der gemeinsame Serverschalter `[hostiles] naturalHostiles` in
`config/dungeonsalive-hostiles-common.toml` blockiert neue natürliche Dungeons-Gegner
auch bei aktiviertem Legacy-Pack. Bestehende Gegner und manuelle Begegnungen bleiben.
Bosse und Minibosse gehören weder zum eingebauten Naturbestand noch zu diesem Pack.

Die Dateien dieses Packs sind historische Konfigurationsvorlagen; sie sind kein Nachweis
für einen ausgeführten Minecraft-Spawn- oder Sichttest.
"""


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--check", action="store_true", help="Compare owned output files without writing")
    args = parser.parse_args()
    serialized = {path: json.dumps(data, indent=2, ensure_ascii=False) + "\n" for path, data in outputs().items()}
    serialized[Path("examples/datapacks/dungeons_wilds/README.md")] = LEGACY_README
    stale = []
    for path, text in serialized.items():
        target = ROOT / path
        if args.check:
            if not target.exists() or target.read_text() != text: stale.append(str(path))
        else:
            target.parent.mkdir(parents=True, exist_ok=True)
            target.write_text(text)
    if stale: raise SystemExit("Stale natural-hostile resources:\n" + "\n".join(stale))
    print(f"{'Checked' if args.check else 'Generated'} 9 biome families / 20 distinct REGULAR types / 0 bosses; legacy overrides documented")


if __name__ == "__main__":
    main()
