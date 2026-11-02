#!/usr/bin/env python3
"""Generate rare singleton Piggy Bank habitats and the explicit runtime-balance manifest.

Treasure is emitted inside the captured entity death-loot hook, not from the
generic peaceful loot table, so generate_peaceful_resources.py cannot duplicate it.
"""
from __future__ import annotations
import argparse
import json
from pathlib import Path
import re

ROOT = Path(__file__).resolve().parents[1]
JAVA = "src/main/java/de/laugenbreze/dungeonsalive/entity/peaceful"
BIOMES = ["plains", "sunflower_plains", "forest", "flower_forest", "savanna"]

def resources(root: Path) -> dict[str, dict]:
    profiles = (root / JAVA / "PeacefulProfiles.java").read_text()
    if not re.search(r'add\("piggy_bank",[^\n]+Kind\.WILDLIFE', profiles):
        raise ValueError("Piggy Bank must remain an existing WILDLIFE registry profile")
    state = (root / JAVA / "PiggyBankTreasureState.java").read_text()
    gear_match = re.search(r'GEAR\s*=\s*List\.of\(([^;]+)\);', state)
    if gear_match is None:
        raise ValueError("Missing authoritative bounded gear choices")
    gear = re.findall(r'"([a-z_]+)"', gear_match.group(1))
    if gear != ["power_bow", "boots_of_swiftness", "iron_hide_amulet", "death_cap_mushroom"]:
        raise ValueError("Review changed treasure choices before regenerating balance")
    counts = {name: int(re.search(rf'{name}\s*=\s*(\d+)', state).group(1)) for name in ("MIN_EMERALDS", "MAX_EMERALDS")}
    if counts != {"MIN_EMERALDS": 6, "MAX_EMERALDS": 12}:
        raise ValueError("Review changed treasure quantities before regenerating balance")
    base = "src/main/resources/data/dungeonsalive"
    return {
        f"{base}/tags/worldgen/biome/has_treasure/piggy_bank.json": {
            "replace": False, "values": [f"minecraft:{biome}" for biome in BIOMES]},
        f"{base}/neoforge/biome_modifier/treasure_piggy_bank.json": {
            "type": "neoforge:add_spawns", "biomes": "#dungeonsalive:has_treasure/piggy_bank",
            "spawners": [{"type": "dungeonsalive:piggy_bank", "weight": 1, "minCount": 1, "maxCount": 1}]},
        "design/piggy-bank-behavior.json": {
            "schema_version": 1, "source": "tools/generate_piggy_bank_resources.py",
            "status": "source_implemented_runtime_validation_pending", "registry_id": "dungeonsalive:piggy_bank",
            "mechanic": "minecraft_adaptation_not_original_drop_table",
            "configuration": "dungeonsalive-piggy-bank-common.toml",
            "default_enabled": True, "spawn_weight": 1, "requested_pack_size": 1,
            "extra_placement_chance": "1/8", "schedule": "day",
            "biomes": [f"minecraft:{biome}" for biome in BIOMES],
            "live_local_pig_limit": 2, "live_local_radius": 48,
            "flee_ticks_after_damage": 120, "flee_radius": 12,
            "treasure": {"emerald_min": counts["MIN_EMERALDS"], "emerald_max": counts["MAX_EMERALDS"],
                         "equipment_count": 1, "equipment_choices": [f"dungeonsalive:{item}" for item in gear]},
            "claim_ledger_capacity": 65536,
            "notes": [
                "Loot requires a final player-attributed damage source, accepted death and doMobLoot; no damage-tick coins.",
                "Saved entity identity determines a stable bounded plan. Entity state and world claims prevent ordinary reload and same-UUID replay payouts.",
                "A full or malformed world claim ledger refuses new rewards and never evicts prior UUIDs.",
                "ItemEntities enter NeoForge's captured LivingDrops pass; canceling that pass consumes the claim without spawning items.",
                "Unknown chunks are not loaded. Live natural spawning shares the wildlife configuration limit and adds a two-pig limit.",
                "Chunk generation keeps the singleton pack and rarity but never queries live entities from a generation thread.",
                "The Piggy Bank is wildlife, not a pet or a herd member. Existing pets and summoned companion profiles do not receive treasure.",
                "Native compilation, GameTests and client review remain required; natural distribution is not established by generated JSON."
            ]
        }
    }

def main() -> None:
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--check", action="store_true")
    args = parser.parse_args()
    outputs = resources(ROOT)
    for name, content in outputs.items():
        path = ROOT / name
        rendered = json.dumps(content, ensure_ascii=False, indent=2) + "\n"
        if args.check:
            if not path.is_file() or path.read_text() != rendered:
                raise SystemExit(f"Missing or stale Piggy Bank resource: {name}")
        else:
            path.parent.mkdir(parents=True, exist_ok=True); path.write_text(rendered)
    print(f"{'Checked' if args.check else 'Generated'} {len(outputs)} Piggy Bank resources.")

if __name__ == "__main__":
    main()
