package de.laugenbreze.dungeonsalive.item.gear;

import de.laugenbreze.dungeonsalive.DungeonsAlive;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

/** Real item attributes, native Player attacks and equipment changes; no synthetic damage events. */
@GameTestHolder(DungeonsAlive.MOD_ID)
@PrefixGameTestTemplate(false)
public final class WeaponFamilyGameTests {
    private static final String BATCH = "weapon_families";
    private WeaponFamilyGameTests() {}

    @GameTest(template = "gametest_empty", templateNamespace = DungeonsAlive.MOD_ID, batch = BATCH, timeoutTicks = 40)
    public static void mainHandComponentsSetActualDamageSpeedAndReachWithoutChangingBlockReachOrStackingOffhand(GameTestHelper helper) {
        Player player = player(helper);
        double entityRange = player.getAttributeValue(Attributes.ENTITY_INTERACTION_RANGE);
        double blockRange = player.getAttributeValue(Attributes.BLOCK_INTERACTION_RANGE);
        for (var kind : WeaponKind.values()) {
            ItemStack stack = new ItemStack(GearContent.weapon(kind));
            helper.assertTrue(BuiltInRegistries.ITEM.getKey(stack.getItem()).getPath().equals(kind.id)
                            && stack.getMaxStackSize() == 1 && stack.getMaxDamage() == kind.durability,
                    "Weapon family is missing its real registration or durability: " + kind.id);
            equip(player, stack);
            helper.assertTrue(close(player.getAttributeValue(Attributes.ATTACK_DAMAGE), kind.damage)
                            && close(player.getAttributeValue(Attributes.ATTACK_SPEED), kind.attacksPerSecond)
                            && close(player.getAttributeValue(Attributes.ENTITY_INTERACTION_RANGE), entityRange + kind.family.entityReachBonus)
                            && close(player.getAttributeValue(Attributes.BLOCK_INTERACTION_RANGE), blockRange),
                    "Actual main-hand attributes disagree with family balance: " + kind.id);
            helper.assertTrue(stack.canPerformAction(ItemAbilities.SWORD_SWEEP) == kind.family.sweeping,
                    "Native sweep ability disagrees with the weapon's handling");
        }
        equip(player, new ItemStack(GearContent.weapon(WeaponKind.SPEAR)));
        var outsideOrdinaryReach = helper.spawnWithNoFreeWill(EntityType.WITCH, new Vec3(7.25, 1, 5.5));
        helper.assertTrue(player.canInteractWithEntity(outsideOrdinaryReach, 0), "Native interaction range did not admit the longer spear reach");
        player.setItemInHand(InteractionHand.OFF_HAND, new ItemStack(GearContent.weapon(WeaponKind.GLAIVE))); player.tick();
        helper.assertTrue(close(player.getAttributeValue(Attributes.ENTITY_INTERACTION_RANGE), entityRange + 1), "Off-hand polearm stacked its reach");
        equip(player, new ItemStack(GearContent.weapon(WeaponKind.DAGGERS)));
        helper.assertTrue(close(player.getAttributeValue(Attributes.ENTITY_INTERACTION_RANGE), entityRange), "Swapping away from the spear retained reach");
        helper.assertFalse(player.canInteractWithEntity(outsideOrdinaryReach, 0), "Normal reach still admitted a target beyond three blocks");
        equip(player, ItemStack.EMPTY); player.setItemInHand(InteractionHand.OFF_HAND, ItemStack.EMPTY); player.tick();
        helper.assertTrue(close(player.getAttributeValue(Attributes.ATTACK_DAMAGE), 1)
                        && close(player.getAttributeValue(Attributes.ATTACK_SPEED), 4), "Removing weapons retained combat modifiers");
        helper.succeed();
    }

    @GameTest(template = "gametest_empty", templateNamespace = DungeonsAlive.MOD_ID, batch = BATCH, timeoutTicks = 40)
    public static void actualSwordAttackSweepsButActualSpearThrustLeavesTheSideTargetUntouched(GameTestHelper helper) {
        Player sword = player(helper);
        var directSword = helper.spawnWithNoFreeWill(EntityType.WITCH, new Vec3(5.5, 1, 5.5));
        var sideSword = helper.spawnWithNoFreeWill(EntityType.WITCH, new Vec3(5.5, 1, 6.2));
        charge(sword, new ItemStack(GearContent.weapon(WeaponKind.SWORD)));
        float swordHealth = directSword.getHealth(), sideHealth = sideSword.getHealth();
        sword.attack(directSword);
        helper.assertTrue(close(swordHealth - directSword.getHealth(), WeaponKind.SWORD.damage)
                        && sideSword.getHealth() < sideHealth && sword.getMainHandItem().getDamageValue() == 1,
                "A fully charged real sword attack failed its direct damage, sweep or durability use");
        Player spear = player(helper); spear.setPos(helper.absoluteVec(new Vec3(12.5, 1, 5.5)));
        var directSpear = helper.spawnWithNoFreeWill(EntityType.WITCH, new Vec3(14.5, 1, 5.5));
        var sideSpear = helper.spawnWithNoFreeWill(EntityType.WITCH, new Vec3(14.5, 1, 6.2));
        charge(spear, new ItemStack(GearContent.weapon(WeaponKind.SPEAR)));
        float spearHealth = directSpear.getHealth(), untouched = sideSpear.getHealth();
        spear.attack(directSpear);
        helper.assertTrue(close(spearHealth - directSpear.getHealth(), WeaponKind.SPEAR.damage)
                        && sideSpear.getHealth() == untouched && spear.getMainHandItem().getDamageValue() == 1,
                "A real spear attack swept a side target or bypassed native damage/durability");
        helper.succeed();
    }

    @GameTest(template = "gametest_empty", templateNamespace = DungeonsAlive.MOD_ID, batch = BATCH, timeoutTicks = 40)
    public static void hammerUsesItsRealKnockbackAttributeAndRemovingItRestoresThePlayer(GameTestHelper helper) {
        Player player = player(helper);
        double ordinary = player.getAttributeValue(Attributes.ATTACK_KNOCKBACK);
        charge(player, new ItemStack(GearContent.weapon(WeaponKind.HAMMER)));
        helper.assertTrue(close(player.getAttributeValue(Attributes.ATTACK_KNOCKBACK), ordinary + .6), "Hammer did not equip its real knockback component");
        var target = helper.spawnWithNoFreeWill(EntityType.WITCH, new Vec3(5.5, 1, 5.5));
        float health = target.getHealth(); player.attack(target);
        helper.assertTrue(close(health - target.getHealth(), WeaponKind.HAMMER.damage)
                        && target.getDeltaMovement().horizontalDistanceSqr() > 0 && player.getMainHandItem().getDamageValue() == 1,
                "A real hammer attack failed its native hit, recoil or durability use");
        equip(player, new ItemStack(GearContent.weapon(WeaponKind.CUTLASS)));
        helper.assertTrue(close(player.getAttributeValue(Attributes.ATTACK_KNOCKBACK), ordinary), "Hammer knockback leaked into another weapon");
        helper.succeed();
    }

    private static Player player(GameTestHelper helper) {
        for (int x = 0; x < 20; x++) for (int z = 0; z < 12; z++) helper.setBlock(new BlockPos(x, 0, z), Blocks.STONE);
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setPos(helper.absoluteVec(new Vec3(3.5, 1, 5.5)));
        return player;
    }
    private static void equip(Player player, ItemStack stack) {
        player.setItemInHand(InteractionHand.MAIN_HAND, stack); player.tick(); player.tick();
    }
    private static void charge(Player player, ItemStack stack) {
        equip(player, stack);
        for (int tick = 0; tick < 40; tick++) player.tick();
        player.setOnGround(true); player.setSprinting(false); player.setDeltaMovement(Vec3.ZERO);
    }
    private static boolean close(double actual, double expected) { return Math.abs(actual - expected) < .00001; }
}
