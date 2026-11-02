package de.laugenbreze.dungeonsalive.entity;

import de.laugenbreze.dungeonsalive.DungeonsAlive;
import java.util.HashSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.event.EventHooks;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

/** Actual loaded biome tables and registered predicates; no claim that a natural spawn tick was observed. */
@GameTestHolder(DungeonsAlive.MOD_ID)
@PrefixGameTestTemplate(false)
public final class NaturalMobSpawnGameTests {
    @GameTest(template = "gametest_empty", templateNamespace = DungeonsAlive.MOD_ID,
            batch = "natural_hostiles", timeoutTicks = 20)
    public static void defaultBiomeEntriesAreRegularBoundedAndUseTheOrdinaryMonsterCap(GameTestHelper helper) {
        var biomes = helper.getLevel().registryAccess().registryOrThrow(Registries.BIOME);
        for (String[] entry : new String[][] {
                {"minecraft:forest", "royal_guard"}, {"minecraft:desert", "vanguard"},
                {"minecraft:jungle", "jungle_zombie"}, {"minecraft:frozen_peaks", "iceologer"},
                {"minecraft:snowy_plains", "frozen_zombie"}, {"minecraft:swamp", "wraith"},
                {"minecraft:crimson_forest", "piglin_hunter"}, {"minecraft:end_highlands", "watchling"},
                {"minecraft:ocean", "armored_drowned"}, {"dungeonsalive:amber_grove", "royal_guard"},
                {"dungeonsalive:luminous_marsh", "sunken_skeleton"}, {"dungeonsalive:ember_crags", "vanguard"}}) {
            var biome = biomes.get(ResourceLocation.parse(entry[0]));
            helper.assertTrue(biome != null, "Missing natural-hostile fixture biome " + entry[0]);
            var type = ModEntities.type(entry[1]);
            var spawns = biome.getMobSettings().getMobs(MobCategory.MONSTER).unwrap();
            helper.assertTrue(spawns.stream().filter(spawn -> spawn.type == type).count() == 1,
                    "Missing or duplicated hostile biome entry: " + entry[0] + " / " + entry[1]);
            helper.assertTrue(spawns.stream().filter(spawn -> spawn.type == type)
                            .allMatch(spawn -> spawn.minCount >= 1 && spawn.maxCount <= 2),
                    "A curated pack exceeds its ordinary local size");
        }
        var naturalIds = new HashSet<String>();
        for (var biome : biomes) for (var spawn : biome.getMobSettings().getMobs(MobCategory.MONSTER).unwrap()) {
            for (MobProfile profile : MobProfiles.all()) if (spawn.type == ModEntities.type(profile.id())) {
                helper.assertTrue(profile.tier() == MobProfile.Tier.REGULAR, "A boss/miniboss entered natural biome spawning");
                naturalIds.add(profile.id());
            }
        }
        helper.assertTrue(naturalIds.size() == 20, "The built-in baseline must contain exactly twenty distinct ordinary opponents");
        for (String id : naturalIds) {
            var mob = ModEntities.type(id).create(helper.getLevel());
            helper.assertTrue(mob != null && mob.getType().getCategory() == MobCategory.MONSTER,
                    "A natural opponent bypassed the ordinary monster category");
            helper.assertFalse(mob.isPersistenceRequired(), "Natural regular opponent was made persistent and mob-cap exempt");
            mob.discard();
        }
        helper.succeed();
    }

    @GameTest(template = "gametest_empty", templateNamespace = DungeonsAlive.MOD_ID,
            batch = "natural_hostiles", timeoutTicks = 20)
    public static void masterToggleReachesEveryRegularPlacementAndPeacefulStillRejectsManualPlacementChecks(GameTestHelper helper) {
        var pos = helper.absolutePos(new BlockPos(4, 1, 4));
        helper.setBlock(new BlockPos(4, 0, 4), Blocks.STONE);
        var level = helper.getLevel();
        boolean before = NaturalMobConfig.NATURAL_HOSTILES.get();
        Difficulty difficulty = level.getDifficulty();
        try {
            NaturalMobConfig.NATURAL_HOSTILES.set(false);
            for (MobProfile profile : MobProfiles.all()) {
                if (profile.tier() != MobProfile.Tier.REGULAR) continue;
                var type = ModEntities.type(profile.id());
                helper.assertFalse(SpawnPlacements.checkSpawnRules(type, level, MobSpawnType.NATURAL, pos, level.random),
                        "Natural hostile master switch missed " + profile.id());
                helper.assertTrue(SpawnPlacements.getPlacementType(type)
                                == (profile.aquatic() ? SpawnPlacementTypes.IN_WATER : SpawnPlacementTypes.ON_GROUND),
                        "Wrong registered ground/water placement: " + profile.id());
            }
            level.getServer().setDifficulty(Difficulty.NORMAL, true);
            // In 1.21.1 only TRIAL_SPAWNER ignores light; ordinary SPAWNER still checks darkness.
            helper.assertTrue(SpawnPlacements.checkSpawnRules(ModEntities.type("royal_guard"), level, MobSpawnType.TRIAL_SPAWNER, pos, level.random),
                    "The natural-only master switch incorrectly disabled a safe trial-spawner check");
            level.getServer().setDifficulty(Difficulty.PEACEFUL, true);
            helper.assertFalse(SpawnPlacements.checkSpawnRules(ModEntities.type("royal_guard"), level, MobSpawnType.TRIAL_SPAWNER, pos, level.random),
                    "The registered predicate bypassed Peaceful difficulty");
        } finally {
            level.getServer().setDifficulty(difficulty, true);
            NaturalMobConfig.NATURAL_HOSTILES.set(before);
        }
        helper.succeed();
    }

    @GameTest(template = "gametest_empty", templateNamespace = DungeonsAlive.MOD_ID,
            batch = "natural_hostiles", timeoutTicks = 120)
    public static void aquaticNaturalPlacementUsesFullWaterCollisionAndTheRealObstructionHook(GameTestHelper helper) {
        // Enclosed, three-deep water leaves a deterministic dark interior after the light engine updates.
        for (int x = 2; x <= 8; x++) for (int z = 2; z <= 8; z++) for (int y = 0; y <= 4; y++) {
            helper.setBlock(new BlockPos(x, y, z), x == 2 || x == 8 || z == 2 || z == 8 || y == 0 || y == 4
                    ? Blocks.STONE : Blocks.WATER);
        }
        boolean[] checked = {false};
        helper.onEachTick(() -> {
            if (checked[0]) return;
            var level = helper.getLevel();
            var relative = new BlockPos(5, 1, 5);
            var pos = helper.absolutePos(relative);
            // Lighting propagates asynchronously. Wait for the real sealed-box
            // prerequisite, not an assumed tick count or a lucky random spawn
            // result. Once genuinely dark, every original assertion runs once.
            if (level.getBrightness(net.minecraft.world.level.LightLayer.SKY, pos) != 0
                    || level.getBrightness(net.minecraft.world.level.LightLayer.BLOCK, pos) != 0) return;
            checked[0] = true;
            var type = ModEntities.type("armored_drowned");
            var mob = type.create(level);
            helper.assertTrue(mob != null, "Missing aquatic hostile factory");
            mob.setPos(pos.getX() + .5, pos.getY(), pos.getZ() + .5);
            boolean enabled = NaturalMobConfig.NATURAL_HOSTILES.get();
            Difficulty difficulty = level.getDifficulty();
            try {
                level.getServer().setDifficulty(Difficulty.NORMAL, true);
                NaturalMobConfig.NATURAL_HOSTILES.set(true);
                helper.assertTrue(SpawnPlacements.isSpawnPositionOk(type, level, pos), "Registered IN_WATER placement rejected real water");
                helper.assertTrue(SpawnPlacements.checkSpawnRules(type, level, MobSpawnType.NATURAL, pos, level.random),
                        "Actual natural predicate rejected the fully submerged dark fixture");
                helper.assertTrue(EventHooks.checkSpawnPosition(mob, level, MobSpawnType.NATURAL),
                        "Actual MobSpawnEvent/obstruction pipeline still rejects all liquids");
                helper.setBlock(relative.above(), Blocks.AIR);
                helper.assertFalse(SpawnPlacements.checkSpawnRules(type, level, MobSpawnType.NATURAL, pos, level.random),
                        "Aquatic predicate accepted a dry upper body");
                helper.assertFalse(mob.checkSpawnObstruction(level), "Actual obstruction hook accepted a dry upper body");
                helper.setBlock(relative.above(), Blocks.WATER);
                helper.setBlock(relative, Blocks.OAK_FENCE.defaultBlockState().setValue(BlockStateProperties.WATERLOGGED, true));
                helper.assertFalse(SpawnPlacements.checkSpawnRules(type, level, MobSpawnType.NATURAL, pos, level.random),
                        "Waterlogged solid collision passed the aquatic predicate");
                helper.assertFalse(mob.checkSpawnObstruction(level), "Waterlogged fence passed actual entity obstruction");
            } finally {
                mob.discard();
                NaturalMobConfig.NATURAL_HOSTILES.set(enabled);
                level.getServer().setDifficulty(difficulty, true);
            }
            helper.succeed();
        });
        helper.runAtTickTime(110, () -> {
            var level = helper.getLevel(); var pos = helper.absolutePos(new BlockPos(5, 1, 5));
            helper.assertTrue(checked[0], "Sealed aquatic fixture lighting did not finish: sky="
                    + level.getBrightness(net.minecraft.world.level.LightLayer.SKY, pos) + ",block="
                    + level.getBrightness(net.minecraft.world.level.LightLayer.BLOCK, pos));
        });
    }

    private NaturalMobSpawnGameTests() {}
}
