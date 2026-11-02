package de.laugenbreze.dungeonsalive.item.gear;

import de.laugenbreze.dungeonsalive.DungeonsAlive;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.AfterBatch;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

/** Actual ammunition, charged components, arrows and impact callbacks; not executed by the offline suite. */
@GameTestHolder(DungeonsAlive.MOD_ID)
@PrefixGameTestTemplate(false)
public final class RangedWeaponGameTests {
    private static final String BATCH = "ranged_gear";
    private static final Map<ServerLevel, Set<Entity>> ACTORS = new IdentityHashMap<>();
    private RangedWeaponGameTests() {}

    @AfterBatch(batch = BATCH)
    public static void cleanup(ServerLevel level) {
        var actors = ACTORS.remove(level);
        if (actors != null) actors.forEach(Entity::discard);
    }

    @GameTest(template = "gametest_empty", templateNamespace = DungeonsAlive.MOD_ID, batch = BATCH, timeoutTicks = 40)
    public static void powerBowConsumesAmmoDamagesTheWeaponAndFiresItsStrongerArrow(GameTestHelper helper) {
        Player player = player(helper);
        var target = track(helper, helper.spawnWithNoFreeWill(EntityType.HUSK, new Vec3(10.5, 1, 5.5)));
        float health = target.getHealth();
        ItemStack stack = new ItemStack(GearContent.ranged(RangedWeaponKind.POWER_BOW));
        AbstractArrow arrow = fireBow(helper, player, stack);
        helper.assertTrue(player.getInventory().countItem(Items.ARROW) == 3 && stack.getDamageValue() == 1,
                "A real power-bow shot must spend one arrow and one durability");
        helper.assertTrue(Math.abs(arrow.getBaseDamage() - 2.7) < .001,
                "The fired ordinary arrow did not receive the power bow's damage multiplier");
        player.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
        helper.runAtTickTime(20, () -> {
            helper.assertTrue(target.getHealth() < health, "The real powered arrow never hit its target after switching held items");
            helper.succeed();
        });
    }

    @GameTest(template = "gametest_empty", templateNamespace = DungeonsAlive.MOD_ID, batch = BATCH, timeoutTicks = 40)
    public static void huntersPromiseCommandsOnlyAnActiveOwnedWolfAndRefundsAtMostOnePaidArrow(GameTestHelper helper) {
        Player player = player(helper);
        var target = track(helper, helper.spawnWithNoFreeWill(EntityType.HUSK, new Vec3(10.5, 1, 5.5)));
        Wolf owned = wolf(helper, player.getUUID(), false, 4.5, 7.5);
        Wolf sitting = wolf(helper, player.getUUID(), true, 6.5, 8.5);
        Wolf foreign = wolf(helper, UUID.randomUUID(), false, 4.5, 9.5);
        AbstractArrow arrow = fireBow(helper, player, new ItemStack(GearContent.ranged(RangedWeaponKind.HUNTERS_PROMISE)));
        boolean[] sawOwnedTarget = {false};
        helper.onEachTick(() -> {
            sawOwnedTarget[0] |= owned.getTarget() == target;
            helper.assertTrue(sitting.getTarget() == null && foreign.getTarget() == null,
                    "Hunter's Promise commanded a sitting or foreign wolf");
        });
        // LegacyRandomSource's seed 0 produces 0 for the next nextInt(5). The
        // mock player is not world-ticked, so this tests the actual success branch.
        player.getRandom().setSeed(0);
        player.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
        helper.runAtTickTime(20, () -> {
            helper.assertTrue(sawOwnedTarget[0],
                    "Hunter's Promise did not preserve ownership and sitting boundaries");
            helper.assertTrue(player.getInventory().countItem(Items.ARROW) == 4
                            && arrow.pickup == AbstractArrow.Pickup.DISALLOWED
                            && arrow.getPersistentData().getBoolean(RangedWeaponEvents.HUNTER_PROCESSED),
                    "A paid arrow was not returned once with physical pickup disabled");
            helper.succeed();
        });
    }

    @GameTest(template = "gametest_empty", templateNamespace = DungeonsAlive.MOD_ID, batch = BATCH, timeoutTicks = 40)
    public static void autoCrossbowLoadsAtItsShorterBoundaryAndSpendsAmmoOnlyOnce(GameTestHelper helper) {
        Player player = player(helper);
        ItemStack stack = new ItemStack(GearContent.ranged(RangedWeaponKind.AUTO_CROSSBOW));
        DungeonsCrossbowItem item = (DungeonsCrossbowItem) stack.getItem();
        player.setItemInHand(InteractionHand.MAIN_HAND, stack);
        helper.assertTrue(item.use(helper.getLevel(), player, InteractionHand.MAIN_HAND).getResult().consumesAction(), "Auto crossbow did not start loading");
        int duration = item.chargeDuration(stack, player), total = item.getUseDuration(stack, player);
        helper.assertTrue(duration < CrossbowItem.getChargeDuration(stack, player), "Auto crossbow is no faster than vanilla");
        item.onUseTick(helper.getLevel(), player, stack, total - duration + 1);
        helper.assertTrue(!CrossbowItem.isCharged(stack) && player.getInventory().countItem(Items.ARROW) == 4,
                "Auto loading spent ammo before the actual charge boundary");
        item.onUseTick(helper.getLevel(), player, stack, total - duration);
        item.releaseUsing(stack, helper.getLevel(), player, total - duration);
        helper.assertTrue(CrossbowItem.isCharged(stack) && player.getInventory().countItem(Items.ARROW) == 3,
                "Auto loading failed to store charged ammunition once");
        item.use(helper.getLevel(), player, InteractionHand.MAIN_HAND);
        helper.assertTrue(!CrossbowItem.isCharged(stack) && projectiles(helper, player).size() == 1
                        && stack.getDamageValue() > 0 && player.getInventory().countItem(Items.ARROW) == 3,
                "Loaded auto crossbow did not fire a real arrow with ordinary durability/ammunition accounting");
        helper.succeed();
    }

    @GameTest(template = "gametest_empty", templateNamespace = DungeonsAlive.MOD_ID, batch = BATCH, timeoutTicks = 40)
    public static void explodingCrossbowHitsExposedEnemiesWithoutSplashingAnimalsOrBreakingCover(GameTestHelper helper) {
        explosiveScenario(helper, false);
    }

    @GameTest(template = "gametest_empty", templateNamespace = DungeonsAlive.MOD_ID, batch = BATCH, timeoutTicks = 40)
    public static void upwardExplosiveArrowUsesItsRealContactAboveTheRaisedStoneFloor(GameTestHelper helper) {
        explosiveScenario(helper, true);
    }

    private static void explosiveScenario(GameTestHelper helper, boolean raised) {
        Player player = player(helper);
        int feet = raised ? 3 : 1;
        if (raised) {
            for (int x = 7; x <= 13; x++) for (int z = 2; z <= 9; z++) helper.setBlock(new BlockPos(x, 2, z), Blocks.STONE);
            player.setXRot(-14); // Upward real shot: a foot-based contact would be moved inside the stone floor.
        }
        var direct = track(helper, helper.spawnWithNoFreeWill(EntityType.HUSK, new Vec3(9.5, feet, 5.5)));
        var nearby = track(helper, helper.spawnWithNoFreeWill(EntityType.HUSK, new Vec3(9.5, feet, 6.8)));
        var covered = track(helper, helper.spawnWithNoFreeWill(EntityType.HUSK, new Vec3(9.5, feet, 3.5)));
        var passive = track(helper, helper.spawnWithNoFreeWill(EntityType.COW, new Vec3(10.5, feet, 5.5)));
        float directHealth = direct.getHealth(), nearbyHealth = nearby.getHealth();
        float coveredHealth = covered.getHealth(), passiveHealth = passive.getHealth();
        for (int x = 7; x <= 12; x++) for (int y = 1; y <= 4; y++) helper.setBlock(new BlockPos(x, y, 4), Blocks.STONE);
        ItemStack stack = new ItemStack(GearContent.ranged(RangedWeaponKind.EXPLODING_CROSSBOW));
        DungeonsCrossbowItem item = (DungeonsCrossbowItem) stack.getItem();
        player.setItemInHand(InteractionHand.MAIN_HAND, stack);
        item.use(helper.getLevel(), player, InteractionHand.MAIN_HAND);
        item.releaseUsing(stack, helper.getLevel(), player, item.getUseDuration(stack, player) - item.chargeDuration(stack, player));
        player.stopUsingItem();
        helper.assertTrue(CrossbowItem.isCharged(stack), "Explosive crossbow did not load through the real release callback");
        item.use(helper.getLevel(), player, InteractionHand.MAIN_HAND);
        List<AbstractArrow> arrows = projectiles(helper, player);
        helper.assertTrue(arrows.size() == 1, "Explosive crossbow did not fire one real arrow");
        helper.assertTrue(arrows.getFirst().getDeltaMovement().x > 2,
                "The un-ticked mock player's actual head/view was not aimed down the target lane");
        if (raised) helper.assertTrue(arrows.getFirst().getDeltaMovement().y > .3,
                "The raised-floor regression did not fire an actual upward shot");
        helper.runAtTickTime(20, () -> {
            helper.assertTrue(direct.getHealth() < directHealth && nearby.getHealth() < nearbyHealth,
                    "The real arrow and its hostile splash did not both deal damage: direct=" + direct.getHealth() + "/" + directHealth
                            + ", nearby=" + nearby.getHealth() + "/" + nearbyHealth + ", arrow=" + arrows.getFirst().position()
                            + ", motion=" + arrows.getFirst().getDeltaMovement() + ", removed=" + arrows.getFirst().isRemoved()
                            + ", kind=" + arrows.getFirst().getPersistentData().getString(RangedWeaponEvents.KIND)
                            + ", spent=" + arrows.getFirst().getPersistentData().getBoolean(RangedWeaponEvents.EXPLOSION_SPENT));
            helper.assertTrue(covered.getHealth() == coveredHealth && passive.getHealth() == passiveHealth,
                    "Explosive arrow damaged a covered enemy or passive animal");
            helper.assertTrue(helper.getBlockState(new BlockPos(9, 2, 4)).is(Blocks.STONE)
                            && arrows.getFirst().getPersistentData().getBoolean(RangedWeaponEvents.EXPLOSION_SPENT),
                    "The explosion modified terrain or failed to spend its once-per-arrow effect");
            helper.succeed();
        });
    }

    private static Player player(GameTestHelper helper) {
        for (int x = 0; x < 20; x++) for (int z = 0; z < 12; z++) helper.setBlock(new BlockPos(x, 0, z), Blocks.STONE);
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setPos(helper.absoluteVec(new Vec3(3.5, 1, 5.5)));
        player.setYRot(-90); player.setYHeadRot(-90); player.setXRot(0);
        // Keep ammunition out of selected slot 0: equipping the real weapon
        // must not replace the only arrow stack before vanilla getProjectile.
        player.getInventory().selected = 0;
        player.getInventory().setItem(1, new ItemStack(Items.ARROW, 4));
        return player;
    }

    private static AbstractArrow fireBow(GameTestHelper helper, Player player, ItemStack stack) {
        DungeonsBowItem bow = (DungeonsBowItem) stack.getItem();
        player.setItemInHand(InteractionHand.MAIN_HAND, stack);
        helper.assertTrue(bow.use(helper.getLevel(), player, InteractionHand.MAIN_HAND).getResult().consumesAction(), "Bow did not start drawing");
        bow.releaseUsing(stack, helper.getLevel(), player, bow.getUseDuration(stack, player) - 20);
        player.stopUsingItem();
        var arrows = projectiles(helper, player);
        helper.assertTrue(arrows.size() == 1, "Bow did not create exactly one real arrow");
        return arrows.getFirst();
    }

    private static List<AbstractArrow> projectiles(GameTestHelper helper, Player player) {
        var arrows = helper.getLevel().getEntitiesOfClass(AbstractArrow.class, player.getBoundingBox().inflate(24), arrow -> arrow.getOwner() == player);
        arrows.forEach(arrow -> track(helper, arrow));
        return arrows;
    }

    private static Wolf wolf(GameTestHelper helper, UUID owner, boolean sitting, double x, double z) {
        Wolf wolf = track(helper, helper.spawnWithNoFreeWill(EntityType.WOLF, new Vec3(x, 1, z)));
        wolf.setTame(true, true); wolf.setOwnerUUID(owner); wolf.setOrderedToSit(sitting);
        wolf.setNoAi(false);
        return wolf;
    }

    private static <T extends Entity> T track(GameTestHelper helper, T entity) {
        ACTORS.computeIfAbsent(helper.getLevel(), ignored -> new HashSet<>()).add(entity);
        return entity;
    }
}
