#!/usr/bin/env python3
"""Generate the six native 1.21.1 enchantments and their narrowly scoped item tags.

The JSON effects are intentionally empty: registered server event handlers perform
the bounded effects. Native definitions supply levels, compatibility, enchanting
table selection, books/anvils and real ItemEnchantments components.
"""
from __future__ import annotations
import argparse
import json
from pathlib import Path
import re

ROOT = Path(__file__).resolve().parents[1]
BASE = "src/main/java/de/laugenbreze/dungeonsalive/item/gear"
EXPECTED = {"radiance", "leeching", "committed", "critical_hit", "chains", "swirling"}

def enum_ids(path: Path) -> list[str]:
    return re.findall(r'\b[A-Z][A-Z_]+\("([a-z_]+)"', path.read_text())

def resources(root: Path) -> dict[str, dict]:
    kinds = enum_ids(root / BASE / "enchantment/GearEnchantmentKind.java")
    melee = enum_ids(root / BASE / "WeaponKind.java")
    ranged = enum_ids(root / BASE / "RangedWeaponKind.java")
    if set(kinds) != EXPECTED or len(kinds) != 6 or len(melee) != 14 or len(ranged) != 4:
        raise ValueError("Enchantments or supported weapon registrations changed; review the explicit scope")
    data = "src/main/resources/data"
    result = {}
    for ident in kinds:
        result[f"{data}/dungeonsalive/enchantment/{ident}.json"] = {
            "description": {"translate": f"enchantment.dungeonsalive.{ident}"},
            "supported_items": "#dungeonsalive:enchantable/dungeons_melee",
            "weight": 4, "max_level": 3,
            "min_cost": {"base": 10, "per_level_above_first": 10},
            "max_cost": {"base": 30, "per_level_above_first": 10},
            "anvil_cost": 4, "slots": ["mainhand"], "effects": {},
        }
    for category, ids in (("dungeons_melee", melee), ("dungeons_ranged", ranged)):
        result[f"{data}/dungeonsalive/tags/item/enchantable/{category}.json"] = {
            "replace": False, "values": [f"dungeonsalive:{ident}" for ident in ids]}
    result[f"{data}/minecraft/tags/enchantment/in_enchanting_table.json"] = {
        "replace": False, "values": [f"dungeonsalive:{ident}" for ident in kinds]}
    return result

def main() -> None:
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--check", action="store_true", help="Verify generated content without changing files")
    args = parser.parse_args()
    outputs = resources(ROOT)
    for name, content in outputs.items():
        path = ROOT / name
        text = json.dumps(content, ensure_ascii=False, indent=2) + "\n"
        if args.check:
            if not path.is_file() or path.read_text() != text:
                raise SystemExit(f"Generated enchantment resource is missing or stale: {name}")
        else:
            path.parent.mkdir(parents=True, exist_ok=True)
            path.write_text(text)
    print(f"{'Checked' if args.check else 'Generated'} {len(outputs)} resources: 6 enchantments, 2 weapon tags, 1 enchanting-table tag.")

if __name__ == "__main__":
    main()
