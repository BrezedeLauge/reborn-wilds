package de.laugenbreze.dungeonsalive.entity.peaceful;

import de.laugenbreze.dungeonsalive.DungeonsAlive;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

/** Loaded biome modifiers and actual spawn-predicate configuration, without a population scheduler. */
@GameTestHolder(DungeonsAlive.MOD_ID)
@PrefixGameTestTemplate(false)
public final class WildlifeSpawnGameTests {
    private WildlifeSpawnGameTests() {}

    @GameTest(template = "gametest_empty", templateNamespace = DungeonsAlive.MOD_ID,
            batch = "wildlife_spawning", timeoutTicks = 20)
    public static void defaultBiomesContainRealBoundedWildlifePacks(GameTestHelper helper) {
        var biomes = helper.getLevel().registryAccess().registryOrThrow(Registries.BIOME);
        for (String[] pair : new String[][]{{"plains", "wooly_cow"}, {"badlands", "vulture"}, {"forest", "wisp"}}) {
            var biome = biomes.get(ResourceLocation.fromNamespaceAndPath("minecraft", pair[0]));
            helper.assertTrue(biome != null, "Missing fixture biome " + pair[0]);
            var type = ModPeacefulEntities.creatureType(pair[1]);
            var spawns = biome.getMobSettings().getMobs(MobCategory.CREATURE).unwrap();
            helper.assertTrue(spawns.stream().anyMatch(spawn -> spawn.type == type && spawn.minCount == 3 && spawn.maxCount == 5),
                    "Biome modifier did not install the real bounded pack for " + pair[1]);
            // Piggy banks have their own intentionally installed rare spawn modifier. Companion types
            // explicitly eligible for wildlife are now natural; this non-wild calf must still stay out.
            helper.assertFalse(spawns.stream().anyMatch(spawn -> spawn.type == ModPeacefulEntities.creatureType("pet_baby_wooly_cow")),
                    "Habitat accidentally enrolled a companion without natural-wildlife eligibility");
        }
        helper.succeed();
    }

    @GameTest(template = "gametest_empty", templateNamespace = DungeonsAlive.MOD_ID,
            batch = "wildlife_spawning", timeoutTicks = 20)
    public static void disablingNaturalWildlifeReachesEveryRegisteredSpawnPredicate(GameTestHelper helper) {
        var pos = new BlockPos(4, 1, 4);
        helper.setBlock(pos.below(), Blocks.GRASS_BLOCK);
        helper.setBlock(pos, Blocks.AIR);
        helper.setBlock(pos.above(), Blocks.AIR);
        boolean previous = WildlifeConfig.NATURAL_SPAWNS.get();
        try {
            WildlifeConfig.NATURAL_SPAWNS.set(false);
            for (String id : WildlifeSpawning.NATURAL_TYPES) {
                helper.assertFalse(SpawnPlacements.checkSpawnRules(ModPeacefulEntities.creatureType(id), helper.getLevel(),
                                MobSpawnType.NATURAL, helper.absolutePos(pos), helper.getLevel().random),
                        "Natural spawn toggle did not reach the registered predicate for " + id);
            }
        } finally {
            WildlifeConfig.NATURAL_SPAWNS.set(previous);
        }
        helper.succeed();
    }

    @GameTest(template = "gametest_empty", templateNamespace = DungeonsAlive.MOD_ID,
            batch = "wildlife_spawning", timeoutTicks = 30)
    public static void realSpawnRulesAllowSuitableAnimalsAndRejectWrongTimeTerrainAndDensity(GameTestHelper helper) {
        var relative = new BlockPos(4, 1, 4);
        helper.setBlock(relative.below(), Blocks.GRASS_BLOCK);
        helper.setBlock(relative, Blocks.AIR);
        helper.setBlock(relative.above(), Blocks.AIR);
        // Stable light makes these schedule assertions independent of a previous
        // tick's cached sky-darkness value when time is temporarily changed below.
        helper.setBlock(relative.offset(0, 0, 3), Blocks.GLOWSTONE);
        helper.runAtTickTime(4, () -> {
            var level = helper.getLevel();
            var pos = helper.absolutePos(relative);
            long originalTime = level.getDayTime();
            boolean enabled = WildlifeConfig.NATURAL_SPAWNS.get();
            boolean cows = WildlifeConfig.WOOLY_COWS.get();
            boolean vultures = WildlifeConfig.VULTURES.get();
            boolean wisps = WildlifeConfig.WISPS.get();
            int originalLimit = WildlifeConfig.NEARBY_LIMIT.get();
            List<DungeonsCreature> densityFixture = new ArrayList<>();
            try {
                WildlifeConfig.NATURAL_SPAWNS.set(true);
                WildlifeConfig.WOOLY_COWS.set(true);
                WildlifeConfig.VULTURES.set(true);
                WildlifeConfig.WISPS.set(true);
                WildlifeConfig.NEARBY_LIMIT.set(64);
                level.setDayTime(6_000);
                helper.assertTrue(allowed(helper, "wooly_cow", pos), "Suitable daytime grass rejected every cow spawn");
                helper.assertTrue(allowed(helper, "vulture", pos), "Suitable daytime ground rejected every vulture spawn");
                helper.assertFalse(allowed(helper, "wisp", pos), "Nocturnal wisps spawned during the day");
                WildlifeConfig.WOOLY_COWS.set(false);
                helper.assertFalse(allowed(helper, "wooly_cow", pos), "Cow-specific switch did not suppress a suitable spawn");
                WildlifeConfig.WOOLY_COWS.set(true);
                WildlifeConfig.VULTURES.set(false);
                helper.assertFalse(allowed(helper, "vulture", pos), "Vulture-specific switch did not suppress a suitable spawn");
                WildlifeConfig.VULTURES.set(true);
                helper.setBlock(relative.below(), Blocks.STONE);
                helper.assertFalse(allowed(helper, "wooly_cow", pos), "Cow habitat accepted bare stone instead of animal grass");
                helper.assertTrue(allowed(helper, "vulture", pos), "Dry-ground flyer incorrectly required cow grass");
                helper.setBlock(relative.below(), Blocks.GRASS_BLOCK);
                level.setDayTime(18_000);
                helper.assertTrue(allowed(helper, "wisp", pos), "Suitable nocturnal habitat rejected every wisp spawn");
                helper.assertFalse(allowed(helper, "wooly_cow", pos) || allowed(helper, "vulture", pos), "Day-active wildlife spawned at night");
                WildlifeConfig.WISPS.set(false);
                helper.assertFalse(allowed(helper, "wisp", pos), "Wisp-specific switch did not suppress a suitable spawn");
                WildlifeConfig.WISPS.set(true);
                level.setDayTime(6_000);
                int baseline = level.getEntitiesOfClass(DungeonsCreature.class, new AABB(pos).inflate(48),
                        creature -> creature.isAlive() && creature.profile().kind() == PeacefulProfiles.Kind.WILDLIFE).size();
                helper.assertTrue(baseline <= 58, "Too many unrelated wildlife fixtures for the bounded density test");
                WildlifeConfig.NEARBY_LIMIT.set(baseline + 6);
                helper.assertTrue(allowed(helper, "wooly_cow", pos), "Spawn was rejected before the local limit was reached");
                for (int i = 0; i < 6; i++) {
                    var cow = ModPeacefulEntities.creatureType("wooly_cow").create(level);
                    cow.setNoAi(true);
                    cow.setPos(helper.absoluteVec(new Vec3(9.5 + i % 3 * 2, 1, 3.5 + i / 3 * 2)));
                    densityFixture.add(cow);
                    helper.assertTrue(level.addFreshEntity(cow), "Could not add the local-density fixture");
                }
                helper.assertFalse(allowed(helper, "wooly_cow", pos), "Live natural spawning ignored the full local wildlife limit");
            } finally {
                densityFixture.forEach(DungeonsCreature::discard);
                level.setDayTime(originalTime);
                WildlifeConfig.NATURAL_SPAWNS.set(enabled);
                WildlifeConfig.WOOLY_COWS.set(cows);
                WildlifeConfig.VULTURES.set(vultures);
                WildlifeConfig.WISPS.set(wisps);
                WildlifeConfig.NEARBY_LIMIT.set(originalLimit);
            }
            helper.succeed();
        });
    }

    private static boolean allowed(GameTestHelper helper, String id, BlockPos pos) {
        return SpawnPlacements.checkSpawnRules(ModPeacefulEntities.creatureType(id), helper.getLevel(),
                MobSpawnType.NATURAL, pos, helper.getLevel().random);
    }

    @GameTest(template = "gametest_empty", templateNamespace = DungeonsAlive.MOD_ID,
            batch = "wildlife_spawning", timeoutTicks = 20)
    public static void naturalPacksKeepIdentityAndRemainSubjectToCreatureCaps(GameTestHelper helper) {
        var level = helper.getLevel();
        SpawnGroupData data = null;
        DungeonsCreature first = null;
        for (int index = 0; index < 7; index++) {
            var cow = ModPeacefulEntities.creatureType("wooly_cow").create(level);
            helper.assertTrue(cow != null, "Wooly cow factory is missing");
            cow.setPos(helper.absoluteVec(new Vec3(2.5 + index * 2, 1, 3.5)));
            data = cow.finalizeSpawn(level, level.getCurrentDifficultyAt(cow.blockPosition()), MobSpawnType.NATURAL, data);
            helper.assertFalse(cow.isPersistenceRequired(), "A natural animal would bypass the ordinary creature mob cap");
            helper.assertTrue(cow.wildlifeGroupId() != null && cow.wildlifeAnchor() != null, "Natural pack received no shared identity/home");
            if (first == null) first = cow;
            else if (index < 6) helper.assertTrue(first.wildlifeGroupId().equals(cow.wildlifeGroupId()), "One natural pack split before its bound");
            else helper.assertFalse(first.wildlifeGroupId().equals(cow.wildlifeGroupId()), "Seventh member overfilled a six-member group");
        }
        CompoundTag saved = new CompoundTag();
        first.saveWithoutId(saved);
        var restored = ModPeacefulEntities.creatureType("wooly_cow").create(level);
        restored.load(saved.copy());
        helper.assertTrue(first.wildlifeGroupId().equals(restored.wildlifeGroupId())
                        && first.wildlifeAnchor().equals(restored.wildlifeAnchor()) && !restored.isPersistenceRequired(),
                "Reload changed natural group identity, anchor or mob-cap eligibility");

        CompoundTag legacy = saved.copy();
        legacy.remove("DungeonsNaturalWildlife");
        legacy.remove("DungeonsWildlifeGroup");
        legacy.putBoolean("PersistenceRequired", true);
        restored.load(legacy);
        helper.assertTrue(restored.isPersistenceRequired(), "An older persistent wildlife save became despawnable");
        var manual = ModPeacefulEntities.creatureType("wooly_cow").create(level);
        manual.finalizeSpawn(level, level.getCurrentDifficultyAt(helper.absolutePos(new BlockPos(3, 1, 3))), MobSpawnType.SPAWN_EGG, null);
        helper.assertTrue(manual.isPersistenceRequired(), "A manually created wildlife actor lost its persistence");
        var pet = ModPeacefulEntities.creatureType("pet_baby_wooly_cow").create(level);
        helper.assertTrue(pet.isPersistenceRequired() && !pet.groupWildlife(), "Pet was enrolled into natural wildlife lifetime/group rules");
        helper.succeed();
    }
}
