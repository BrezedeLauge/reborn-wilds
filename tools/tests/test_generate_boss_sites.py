import json
from pathlib import Path
import sys
import unittest

ROOT = Path(__file__).resolve().parents[2]
sys.path.insert(0, str(ROOT / 'tools'))
import generate_boss_sites as sites


class BossSiteGeneratorTest(unittest.TestCase):
    def test_original_sites_keep_bounded_open_space_for_the_existing_seals(self):
        for site in (sites.overgrown_arena(), sites.redstone_mineyard(), sites.sunken_shrine()):
            with self.subTest(site=site.name):
                site.approach()
                site.validate()
                self.assertLessEqual(max(site.size), 37)
                self.assertEqual(sum(state[0] == 'dungeonsalive:wayfinder_stone' for state in site.blocks.values()), 1)
                self.assertEqual(len(site.block_entities), 1)
                self.assertTrue(all(0 <= value < bound for position in site.blocks
                                    for value, bound in zip(position, site.size)))

    def test_each_chest_has_one_existing_seal_and_bounded_provisions(self):
        for site in (sites.overgrown_arena(), sites.redstone_mineyard(), sites.sunken_shrine()):
            with self.subTest(site=site.name):
                loot = sites.loot(site)
                self.assertEqual(loot['type'], 'minecraft:chest')
                self.assertEqual(len(loot['pools']), 3)
                self.assertTrue(all(pool['rolls'] == 1 and len(pool['entries']) == 1 for pool in loot['pools']))
                self.assertEqual(loot['pools'][0]['entries'][0]['name'], 'dungeonsalive:' + site.seal)
                self.assertNotIn('functions', loot['pools'][0]['entries'][0])
                self.assertEqual(site.block_entities[site.chest]['LootTable'][1], 'dungeonsalive:chests/boss_sites/' + site.name)

    def test_generated_worldgen_is_finite_new_chunk_jigsaw_without_spawn_overrides(self):
        for name in sites.BIOMES:
            structure = json.loads((sites.DATA / f'worldgen/structure/{name}.json').read_text())
            self.assertEqual(structure['type'], 'minecraft:jigsaw')
            self.assertEqual(structure['size'], 0)
            self.assertEqual(structure['spawn_overrides'], {})
            self.assertEqual(structure['terrain_adaptation'], 'beard_thin')
            pool = json.loads((sites.DATA / f'worldgen/template_pool/boss_sites/{name}.json').read_text())
            self.assertEqual(pool['fallback'], 'minecraft:empty')
            self.assertEqual(len(pool['elements']), 1)
            placement = json.loads((sites.DATA / f'worldgen/structure_set/{name}.json').read_text())['placement']
            self.assertLess(placement['separation'], placement['spacing'])
            self.assertEqual(placement['exclusion_zone']['other_set'], 'minecraft:villages')


if __name__ == '__main__':
    unittest.main()
