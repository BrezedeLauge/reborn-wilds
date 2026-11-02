package de.laugenbreze.dungeonsalive.entity.peaceful;

import de.laugenbreze.dungeonsalive.DungeonsAlive;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

/** Real registry/water/spawn-policy integration, separate from the pure population budget tests. */
@GameTestHolder(DungeonsAlive.MOD_ID)
@PrefixGameTestTemplate(false)
public final class WildlifeEncounterGameTests {
    @GameTest(template = "gametest_empty", templateNamespace = DungeonsAlive.MOD_ID, batch = "wildlife_encounters", timeoutTicks = 20)
    public static void everyVanillaBiomeHasAnExplicitHabitatAndEveryAnimalFactoryExists(GameTestHelper helper) {
        var biomes = helper.getLevel().registryAccess().registryOrThrow(Registries.BIOME);
        biomes.keySet().stream().filter(id -> id.getNamespace().equals("minecraft")).forEach(id ->
                helper.assertTrue(WildlifeHabitats.all().containsKey(id.toString()), "Unmapped vanilla biome " + id));
        WildlifeHabitats.all().forEach((biome, entries) -> entries.forEach(entry -> {
            var creature = ModPeacefulEntities.creatureType(entry.id()).create(helper.getLevel());
            helper.assertTrue(creature != null && creature.profile().wildlifeEligible(), "Invalid wildlife factory " + entry.id());
            helper.assertTrue(entry.min() >= 2 && entry.max() <= WildlifeGroupPolicy.MAX_MEMBERS, "Unbounded pack " + biome);
        }));
        helper.succeed();
    }

    @GameTest(template = "gametest_empty", templateNamespace = DungeonsAlive.MOD_ID, batch = "wildlife_encounters", timeoutTicks = 20)
    public static void explorationRespectsBothTheGlobalWildlifeToggleAndVanillaMobSpawning(GameTestHelper helper) {
        var level = helper.getLevel();
        boolean natural = WildlifeConfig.NATURAL_SPAWNS.get(), encounters = WildlifeConfig.EXPLORATION_ENCOUNTERS.get();
        boolean spawning = level.getGameRules().getBoolean(GameRules.RULE_DOMOBSPAWNING);
        try {
            WildlifeConfig.NATURAL_SPAWNS.set(true); WildlifeConfig.EXPLORATION_ENCOUNTERS.set(true);
            level.getGameRules().getRule(GameRules.RULE_DOMOBSPAWNING).set(true, level.getServer());
            helper.assertTrue(WildlifeEncounters.enabled(level), "Enabled exploration cannot run");
            level.getGameRules().getRule(GameRules.RULE_DOMOBSPAWNING).set(false, level.getServer());
            helper.assertFalse(WildlifeEncounters.enabled(level), "Exploration ignores doMobSpawning");
            level.getGameRules().getRule(GameRules.RULE_DOMOBSPAWNING).set(true, level.getServer());
            WildlifeConfig.NATURAL_SPAWNS.set(false);
            helper.assertFalse(WildlifeEncounters.enabled(level), "Exploration ignores wildlife toggle");
            var group = new WildlifeHabitats.Population("pet_toucan", WildlifeHabitats.Layer.CANOPY, 3, 5);
            helper.assertTrue(WildlifeEncounters.spawnGroup(level, group, helper.absolutePos(new BlockPos(5, 2, 5)), 3, false) == 0,
                    "Disabled spawning still produced a flock");
        } finally {
            WildlifeConfig.NATURAL_SPAWNS.set(natural); WildlifeConfig.EXPLORATION_ENCOUNTERS.set(encounters);
            level.getGameRules().getRule(GameRules.RULE_DOMOBSPAWNING).set(spawning, level.getServer());
        }
        helper.succeed();
    }

    @GameTest(template = "gametest_empty", templateNamespace = DungeonsAlive.MOD_ID, batch = "wildlife_encounters", timeoutTicks = 20)
    public static void aquaticGroupsRequireFullWaterBodiesAndRejectDryOrUnloadedDestinations(GameTestHelper helper) {
        var level = helper.getLevel();
        var relative = new BlockPos(4, 3, 4);
        for (BlockPos pos : BlockPos.betweenClosed(relative.offset(-2, -2, -2), relative.offset(2, 2, 2))) helper.setBlock(pos, Blocks.WATER);
        var squid = ModPeacefulEntities.creatureType("pet_baby_squid").create(level);
        Vec3 wet = helper.absoluteVec(new Vec3(4.5, 3, 4.5));
        squid.setPos(wet);
        helper.assertTrue(WildlifeSchoolGoal.safeWater(squid, wet) && squid.checkSpawnObstruction(level), "A clear water body rejected its aquatic animal");
        helper.setBlock(relative, Blocks.AIR);
        helper.assertFalse(WildlifeSchoolGoal.safeWater(squid, wet), "A school path left the water");
        helper.setBlock(relative, Blocks.STONE);
        helper.assertFalse(WildlifeSchoolGoal.safeWater(squid, wet), "A school path entered solid terrain");
        helper.assertFalse(WildlifeSchoolGoal.safeWater(squid, wet.add(20_000_000, 0, 0)), "Water inspection accepted unknown chunks");
        helper.succeed();
    }

    @GameTest(template = "gametest_empty", templateNamespace = DungeonsAlive.MOD_ID, batch = "wildlife_encounters", timeoutTicks = 20)
    public static void regionCooldownSurvivesSavedDataSerialization(GameTestHelper helper) {
        var data = new HabitatPopulationSavedData();
        helper.assertTrue(data.budget.recordSuccess(42, 100), "Region could not record a real population");
        CompoundTag nbt = data.save(new CompoundTag(), helper.getLevel().registryAccess());
        helper.assertTrue(nbt.getList("Regions", 10).size() == 1
                && nbt.getList("Regions", 10).getCompound(0).getLong("Until") == 1300,
                "Region history did not serialize its bounded cooldown");
        helper.succeed();
    }
    private WildlifeEncounterGameTests() {}
}
