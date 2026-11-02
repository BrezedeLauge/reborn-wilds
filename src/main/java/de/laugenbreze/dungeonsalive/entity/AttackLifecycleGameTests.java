package de.laugenbreze.dungeonsalive.entity;

import de.laugenbreze.dungeonsalive.DungeonsAlive;
import de.laugenbreze.dungeonsalive.entity.construct.DungeonTrident;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

/** Actual action timelines: changing/losing opponents cannot redirect or resurrect a pending release. */
@GameTestHolder(DungeonsAlive.MOD_ID)
@PrefixGameTestTemplate(false)
public final class AttackLifecycleGameTests {
    private static final String BATCH = "attack_lifecycle";
    private AttackLifecycleGameTests() {}

    @GameTest(template = "gametest_empty", templateNamespace = DungeonsAlive.MOD_ID, batch = BATCH, timeoutTicks = 110)
    public static void switchingTargetCancelsMeleeAndOnlyANewActionCanHitTheReplacement(GameTestHelper helper) {
        floor(helper);
        var attacker = helper.spawnWithNoFreeWill(ModEntities.type("royal_guard"), new Vec3(5.5, 1, 5.5));
        var first = helper.spawnWithNoFreeWill(EntityType.IRON_GOLEM, new Vec3(7.5, 1, 5.5));
        var replacement = helper.spawnWithNoFreeWill(EntityType.IRON_GOLEM, new Vec3(5.5, 1, 7.5));
        float firstHealth = first.getHealth(), replacementHealth = replacement.getHealth();
        helper.runAtTickTime(35, () -> helper.assertTrue(attacker.beginAttack(AttackKind.MELEE, first), "Melee fixture did not start"));
        helper.runAtTickTime(40, () -> attacker.setTarget(replacement));
        helper.runAtTickTime(43, () -> {
            cancelled(helper, attacker, AttackKind.MELEE);
            helper.assertFalse(attacker.beginAttack(AttackKind.MELEE, replacement), "Target change bypassed the normal cooldown");
        });
        helper.runAtTickTime(60, () -> helper.assertTrue(first.getHealth() == firstHealth && replacement.getHealth() == replacementHealth,
                "The abandoned windup hit its old or replacement opponent"));
        helper.runAtTickTime(80, () -> helper.assertTrue(attacker.beginAttack(AttackKind.MELEE, replacement),
                "Cancellation did not allow a fresh action after its finite cooldown"));
        helper.runAtTickTime(99, () -> {
            helper.assertTrue(first.getHealth() == firstHealth && replacement.getHealth() < replacementHealth,
                    "Fresh melee did not hit only its newly telegraphed opponent");
            helper.succeed();
        });
    }

    @GameTest(template = "gametest_empty", templateNamespace = DungeonsAlive.MOD_ID, batch = BATCH, timeoutTicks = 95)
    public static void deadTargetCancelsSummoningWhileTheUnchangedControlReallySummons(GameTestHelper helper) {
        floor(helper);
        var cancelled = helper.spawnWithNoFreeWill(ModEntities.type("necromancer"), new Vec3(5.5, 1, 3.5));
        var control = helper.spawnWithNoFreeWill(ModEntities.type("necromancer"), new Vec3(5.5, 1, 8.5));
        var doomed = helper.spawnWithNoFreeWill(EntityType.IRON_GOLEM, new Vec3(14.5, 1, 3.5));
        var living = helper.spawnWithNoFreeWill(EntityType.IRON_GOLEM, new Vec3(14.5, 1, 8.5));
        helper.runAtTickTime(35, () -> {
            cancelled.getRandom().setSeed(0L); control.getRandom().setSeed(0L);
            helper.assertTrue(cancelled.beginAttack(AttackKind.SUMMON, doomed) && control.beginAttack(AttackKind.SUMMON, living),
                    "Both real SUMMON timelines must start");
        });
        helper.runAtTickTime(40, () -> helper.assertTrue(doomed.hurt(helper.getLevel().damageSources().generic(), 1000) && !doomed.isAlive(),
                "Target must actually die before the SUMMON release"));
        helper.runAtTickTime(43, () -> cancelled(helper, cancelled, AttackKind.SUMMON));
        helper.runAtTickTime(78, () -> {
            helper.assertTrue(summons(helper, cancelled).isEmpty(), "A dead target still caused the abandoned SUMMON to release");
            var children = summons(helper, control);
            helper.assertTrue(!children.isEmpty() && children.stream().allMatch(child -> child.getTarget() == living),
                    "Unchanged SUMMON did not create actual children assigned to its bound opponent");
            children.forEach(child -> child.setNoAi(true));
            helper.succeed();
        });
    }

    @GameTest(template = "gametest_empty", templateNamespace = DungeonsAlive.MOD_ID, batch = BATCH, timeoutTicks = 95)
    public static void removedTargetStopsTridentWindupButReleasedControlKeepsItsOwnFlight(GameTestHelper helper) {
        floor(helper);
        var cancelled = helper.spawnWithNoFreeWill(ModEntities.type("armored_trident_drowned"), new Vec3(3.5, 1, 3.5));
        var control = helper.spawnWithNoFreeWill(ModEntities.type("armored_trident_drowned"), new Vec3(3.5, 1, 8.5));
        var removed = helper.spawnWithNoFreeWill(EntityType.IRON_GOLEM, new Vec3(16.5, 1, 3.5));
        var target = helper.spawnWithNoFreeWill(EntityType.IRON_GOLEM, new Vec3(16.5, 1, 8.5));
        float health = target.getHealth();
        DungeonTrident[] released = new DungeonTrident[1];
        helper.runAtTickTime(35, () -> {
            helper.assertTrue(cancelled.beginAttack(AttackKind.SOUL_BOLT, removed) && control.beginAttack(AttackKind.SOUL_BOLT, target),
                    "Trident fixtures could not start real throwing actions");
            helper.assertTrue(cancelled.isUsingItem() && control.isUsingItem(), "Throw windup did not publish held-item use");
        });
        helper.runAtTickTime(40, removed::discard);
        helper.runAtTickTime(43, () -> cancelled(helper, cancelled, AttackKind.SOUL_BOLT));
        helper.runAtTickTime(60, () -> {
            helper.assertTrue(tridents(helper, cancelled).isEmpty() && cancelled.constructReservationCount() == 0,
                    "Removed target still produced a trident or reserved a construct slot");
            var projectiles = tridents(helper, control);
            helper.assertTrue(projectiles.size() == 1 && !control.isUsingItem(), "Control did not release exactly one real trident");
            released[0] = projectiles.getFirst();
            control.setTarget(null); // The already-released projectile keeps its existing independent lifetime/collision rules.
            cancelled(helper, control, AttackKind.SOUL_BOLT);
            // A real target selector can do this before the next entity tick. It must
            // not restore the old action after a brief, accepted target deletion.
            control.setTarget(target);
            cancelled(helper, control, AttackKind.SOUL_BOLT);
        });
        helper.runAtTickTime(63, () -> {
            cancelled(helper, control, AttackKind.SOUL_BOLT);
            helper.assertTrue(!released[0].isRemoved(), "Stopping action recovery incorrectly removed an in-flight trident");
        });
        helper.runAtTickTime(85, () -> {
            helper.assertTrue(target.getHealth() < health && released[0].isRemoved() && control.constructReservationCount() == 0,
                    "Released trident lost its normal hit/removal/reservation lifecycle");
            helper.succeed();
        });
    }

    @GameTest(template = "gametest_empty", templateNamespace = DungeonsAlive.MOD_ID, batch = BATCH, timeoutTicks = 100)
    public static void nbtReuseAndCasterDeathClearThePendingActionWithoutReplayingIt(GameTestHelper helper) {
        floor(helper);
        var reused = helper.spawnWithNoFreeWill(ModEntities.type("necromancer"), new Vec3(4.5, 1, 3.5));
        var dying = helper.spawnWithNoFreeWill(ModEntities.type("armored_trident_drowned"), new Vec3(4.5, 1, 8.5));
        var target = helper.spawnWithNoFreeWill(EntityType.IRON_GOLEM, new Vec3(14.5, 1, 5.5));
        helper.runAtTickTime(35, () -> helper.assertTrue(reused.beginAttack(AttackKind.SUMMON, target)
                        && dying.beginAttack(AttackKind.SOUL_BOLT, target), "Persistence/death fixtures did not begin"));
        helper.runAtTickTime(40, () -> {
            CompoundTag saved = new CompoundTag(); reused.addAdditionalSaveData(saved);
            helper.assertTrue(saved.getInt("DungeonsCooldown") >= AttackKind.SUMMON.cooldown(),
                    "Saving a half-cast shortened its ordinary cancellation cooldown");
            reused.readAdditionalSaveData(saved);
            cancelled(helper, reused, AttackKind.SUMMON);
            var restored = ModEntities.type("necromancer").create(helper.getLevel());
            helper.assertTrue(restored != null, "Reload factory missing");
            restored.readAdditionalSaveData(saved);
            helper.assertTrue(restored.activeAttack() == null && restored.attackCooldown() >= AttackKind.SUMMON.cooldown(),
                    "Fresh NBT load resumed a pending release or bypassed cooldown");
            helper.assertTrue(dying.hurt(helper.getLevel().damageSources().generic(), 1000) && !dying.isAlive(),
                    "Caster must actually die during its throwing windup");
            helper.assertTrue(dying.activeAttack() == null && !dying.isUsingItem(), "Caster death retained throwing/action state");
        });
        helper.runAtTickTime(85, () -> {
            helper.assertTrue(summons(helper, reused).isEmpty() && tridents(helper, dying).isEmpty(),
                    "NBT reuse or death replayed an abandoned release");
            helper.succeed();
        });
    }

    private static void cancelled(GameTestHelper helper, DungeonMob mob, AttackKind attack) {
        helper.assertTrue(mob.activeAttack() == null && !mob.isUsingItem()
                        && mob.attackCooldown() > 0 && mob.attackCooldown() <= attack.cooldown(),
                "Invalid target did not end action/item use and start the finite ordinary cooldown: action=" + mob.activeAttack()
                        + ", usingItem=" + mob.isUsingItem() + ", cooldown=" + mob.attackCooldown() + ", target="
                        + (mob.getTarget() == null ? "null" : mob.getTarget().getUUID()) + ", alive=" + mob.isAlive());
    }
    private static List<DungeonTrident> tridents(GameTestHelper helper, DungeonMob owner) {
        return helper.getLevel().getEntitiesOfClass(DungeonTrident.class, owner.getBoundingBox().inflate(24), projectile -> projectile.getOwner() == owner);
    }
    private static List<DungeonMob> summons(GameTestHelper helper, DungeonMob owner) {
        return helper.getLevel().getEntitiesOfClass(DungeonMob.class, owner.getBoundingBox().inflate(24), mob -> {
            if (!mob.isSummoned()) return false;
            CompoundTag saved = new CompoundTag(); mob.addAdditionalSaveData(saved);
            return saved.hasUUID("DungeonsSummoner") && saved.getUUID("DungeonsSummoner").equals(owner.getUUID());
        });
    }
    private static void floor(GameTestHelper helper) {
        for (int x = 0; x < 20; x++) for (int z = 0; z < 12; z++) helper.setBlock(new BlockPos(x, 0, z), Blocks.STONE);
    }
}
