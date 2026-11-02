package de.laugenbreze.dungeonsalive.entity;

import de.laugenbreze.dungeonsalive.DungeonsAlive;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

/** Real server registration, telegraph, collision and persistence regression tests. */
@GameTestHolder(DungeonsAlive.MOD_ID)
@PrefixGameTestTemplate(false)
public final class DungeonMobGameTests {
    private DungeonMobGameTests() {}

    @GameTest(template = "gametest_empty", templateNamespace = DungeonsAlive.MOD_ID, batch = "mob_runtime", timeoutTicks = 60)
    public static void allProfilesHaveRealEntitiesAttributesAndEggs(GameTestHelper helper) {
        helper.assertTrue(MobProfiles.all().size() >= 54, "Established hostile profiles must remain registered");
        helper.assertTrue(MobProfiles.all().stream().filter(MobProfile::boss).count() == 12, "Twelve Dungeons-I boss types must be registered");
        for (MobProfile profile : MobProfiles.all()) {
            ResourceLocation id = ResourceLocation.fromNamespaceAndPath(DungeonsAlive.MOD_ID, profile.id());
            helper.assertTrue(BuiltInRegistries.ENTITY_TYPE.containsKey(id), "Missing actual entity type " + id);
            DungeonMob mob = ModEntities.type(profile.id()).create(helper.getLevel());
            helper.assertTrue(mob != null && mob.profile().equals(profile), "Factory selected wrong profile " + id);
            helper.assertTrue(mob.getMaxHealth() == profile.health(), "Wrong maximum health " + id);
            helper.assertTrue(mob.getAttributeValue(Attributes.ATTACK_DAMAGE) == profile.damage(), "Wrong attack attribute " + id);
            helper.assertTrue(BuiltInRegistries.ITEM.containsKey(ResourceLocation.fromNamespaceAndPath(DungeonsAlive.MOD_ID, profile.id() + "_spawn_egg")), "Missing spawn egg " + id);
        }
        helper.succeed();
    }

    @GameTest(template = "gametest_empty", templateNamespace = DungeonsAlive.MOD_ID, batch = "mob_runtime", timeoutTicks = 100)
    public static void meleeHitsAfterWindupExactlyOnceAndHonorsCooldown(GameTestHelper helper) {
        floor(helper);
        DungeonMob guard = helper.spawnWithNoFreeWill(ModEntities.type("royal_guard"), new Vec3(5.5, 1, 5.5));
        var target = helper.spawnWithNoFreeWill(EntityType.IRON_GOLEM, new Vec3(7.5, 1, 5.5));
        float start = target.getHealth();
        helper.runAtTickTime(35, () -> {
            helper.assertTrue(guard.beginAttack(AttackKind.MELEE, target), "Ready mob refused a legal melee action");
            helper.assertTrue(target.getHealth() == start, "Starting the visual windup dealt damage");
            helper.assertFalse(guard.beginAttack(AttackKind.MELEE, target), "Second concurrent action was accepted");
        });
        helper.runAtTickTime(46, () -> helper.assertTrue(target.getHealth() == start, "Hit landed before telegraph completed"));
        helper.runAtTickTime(51, () -> {
            helper.assertTrue(target.getHealth() < start, "No hit landed after the release frame");
            target.getPersistentData().putFloat("ObservedHealth", target.getHealth());
        });
        helper.runAtTickTime(60, () -> {
            helper.assertTrue(target.getHealth() == target.getPersistentData().getFloat("ObservedHealth"), "One animation applied more than one melee hit");
            helper.assertFalse(guard.beginAttack(AttackKind.MELEE, target), "Recovery/cooldown was bypassed");
            helper.succeed();
        });
    }

    @GameTest(template = "gametest_empty", templateNamespace = DungeonsAlive.MOD_ID, batch = "mob_runtime", timeoutTicks = 110)
    public static void chargedBeamStopsAtNewlyPlacedCover(GameTestHelper helper) {
        floor(helper);
        DungeonMob caster = helper.spawnWithNoFreeWill(ModEntities.type("drowned_necromancer"), new Vec3(3.5, 1, 5.5));
        var target = helper.spawnWithNoFreeWill(EntityType.IRON_GOLEM, new Vec3(11.5, 1, 5.5));
        float start = target.getHealth();
        helper.runAtTickTime(35, () -> {
            helper.assertTrue(caster.beginAttack(AttackKind.BEAM, target), "Beam did not start with clear line of sight");
            for (int y = 1; y <= 4; y++) for (int z = 3; z <= 7; z++) helper.setBlock(new BlockPos(7, y, z), Blocks.STONE);
        });
        helper.runAtTickTime(85, () -> {
            helper.assertTrue(target.getHealth() == start, "Beam pierced newly placed solid cover");
            helper.succeed();
        });
    }

    @GameTest(template = "gametest_empty", templateNamespace = DungeonsAlive.MOD_ID, batch = "mob_runtime", timeoutTicks = 70)
    public static void reloadCancelsHalfCastAndBossHealthSelectsPhase(GameTestHelper helper) {
        floor(helper);
        DungeonMob boss = helper.spawnWithNoFreeWill(ModEntities.type("heart_of_ender"), new Vec3(4.5, 1, 5.5));
        var target = helper.spawnWithNoFreeWill(EntityType.IRON_GOLEM, new Vec3(12.5, 1, 5.5));
        helper.runAtTickTime(35, () -> {
            helper.assertTrue(boss.beginAttack(AttackKind.BEAM, target), "Boss did not begin beam");
            CompoundTag saved = new CompoundTag();
            boss.addAdditionalSaveData(saved);
            DungeonMob restored = ModEntities.type("heart_of_ender").create(helper.getLevel());
            restored.readAdditionalSaveData(saved);
            helper.assertTrue(restored.activeAttack() == null, "Loading replays a half-finished damaging attack");
            helper.assertTrue(restored.attackCooldown() >= 30, "Loading bypasses recovery");
            boss.setHealth(boss.getMaxHealth() * .4f);
        });
        helper.runAtTickTime(38, () -> {
            helper.assertTrue(boss.combatPhase() == 2, "Boss did not enter its second phase below half health");
            helper.succeed();
        });
    }
    @GameTest(template = "gametest_empty", templateNamespace = DungeonsAlive.MOD_ID, batch = "mob_runtime", timeoutTicks = 40)
    public static void orphanedSpellDisappearsWithoutHarmingPeacefulAnimals(GameTestHelper helper) {
        floor(helper);
        var cow = helper.spawnWithNoFreeWill(EntityType.COW, new Vec3(6.5, 1, 5.5));
        float health = cow.getHealth();
        SoulBolt orphan = ModEntities.SOUL_BOLT.get().create(helper.getLevel());
        orphan.setPos(helper.absoluteVec(new Vec3(5.5, 1.8, 5.5)));
        orphan.shoot(1, 0, 0, .65f, 0);
        helper.getLevel().addFreshEntity(orphan);
        helper.runAfterDelay(4, () -> {
            helper.assertTrue(orphan.isRemoved(), "Unresolved-owner spell kept moving after load");
            helper.assertTrue(cow.getHealth() == health, "An orphaned spell harmed a peaceful animal");
            helper.succeed();
        });
    }
    @GameTest(template = "gametest_empty", templateNamespace = DungeonsAlive.MOD_ID, batch = "mob_runtime", timeoutTicks = 50)
    public static void spellSkipsPeacefulAnimalAndHitsCombatTarget(GameTestHelper helper) {
        floor(helper);
        DungeonMob caster = helper.spawnWithNoFreeWill(ModEntities.type("blastling"), new Vec3(3.5, 1, 5.5));
        var cow = helper.spawnWithNoFreeWill(EntityType.COW, new Vec3(6.5, 1, 5.5));
        var golem = helper.spawnWithNoFreeWill(EntityType.IRON_GOLEM, new Vec3(9.5, 1, 5.5));
        float cowHealth = cow.getHealth(), targetHealth = golem.getHealth();
        caster.setTarget(golem);
        SoulBolt bolt = new SoulBolt(helper.getLevel(), caster, new Vec3(1, 0, 0), 5);
        helper.getLevel().addFreshEntity(bolt);
        helper.runAfterDelay(20, () -> {
            helper.assertTrue(cow.getHealth() == cowHealth, "Spell hit a passive animal instead of filtering it");
            helper.assertTrue(golem.getHealth() < targetHealth, "Spell failed to reach its legal opponent");
            helper.assertTrue(bolt.isRemoved(), "Spell was not discarded after collision");
            helper.succeed();
        });
    }
    @GameTest(template = "gametest_empty", templateNamespace = DungeonsAlive.MOD_ID, batch = "mob_runtime", timeoutTicks = 90)
    public static void icyCreeperTelegraphsColdBurstWithoutBreakingBlocks(GameTestHelper helper) {
        floor(helper);
        DungeonMob creeper = helper.spawnWithNoFreeWill(ModEntities.type("icy_creeper"), new Vec3(5.5, 1, 5.5));
        var target = helper.spawnWithNoFreeWill(EntityType.IRON_GOLEM, new Vec3(7.0, 1, 5.5));
        var sheep = helper.spawnWithNoFreeWill(EntityType.SHEEP, new Vec3(5.5, 1, 7.0));
        float before = target.getHealth(), passiveBefore = sheep.getHealth();
        helper.runAtTickTime(35, () -> helper.assertTrue(creeper.beginAttack(AttackKind.ICY_EXPLOSION, target), "Cold fuse did not begin"));
        helper.runAtTickTime(56, () -> {
            helper.assertTrue(target.getHealth() == before, "Cold burst dealt damage before the fuse completed");
            helper.assertFalse(creeper.isRemoved(), "Creeper vanished before its fuse finished");
        });
        helper.runAtTickTime(65, () -> {
            helper.assertTrue(creeper.isRemoved(), "Creeper survived its own completed explosion");
            helper.assertTrue(target.getHealth() < before, "Cold burst missed its nearby combat target");
            helper.assertTrue(target.hasEffect(net.minecraft.world.effect.MobEffects.MOVEMENT_SLOWDOWN), "Cold burst did not slow its target");
            helper.assertTrue(sheep.getHealth() == passiveBefore, "Cold burst harmed a passive bystander");
            helper.assertTrue(helper.getBlockState(new BlockPos(5, 0, 5)).is(Blocks.STONE), "Cold burst destroyed a player-placeable block");
            helper.succeed();
        });
    }
    private static void floor(GameTestHelper helper) {
        for (int x = 0; x < 20; x++) for (int z = 0; z < 12; z++) helper.setBlock(new BlockPos(x, 0, z), Blocks.STONE);
    }
}
