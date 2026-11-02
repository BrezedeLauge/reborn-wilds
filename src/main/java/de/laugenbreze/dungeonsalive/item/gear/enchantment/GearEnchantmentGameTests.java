package de.laugenbreze.dungeonsalive.item.gear.enchantment;

import de.laugenbreze.dungeonsalive.DungeonsAlive;
import de.laugenbreze.dungeonsalive.item.gear.ArtifactKind;
import de.laugenbreze.dungeonsalive.item.gear.GearContent;
import de.laugenbreze.dungeonsalive.item.gear.RangedWeaponKind;
import de.laugenbreze.dungeonsalive.item.gear.WeaponKind;
import java.util.function.Consumer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

/** Native registry/components and real damage callbacks; requires the dedicated Minecraft server. */
@GameTestHolder(DungeonsAlive.MOD_ID)
@PrefixGameTestTemplate(false)
public final class GearEnchantmentGameTests {
    private static final String BATCH = "gear_enchantments";
    private GearEnchantmentGameTests() {}

    @GameTest(template = "gametest_empty", templateNamespace = DungeonsAlive.MOD_ID, batch = BATCH, timeoutTicks = 40)
    public static void registryLevelsAndCopyOnlyServicePreserveMetadataAndAllowTheOriginalCombinations(GameTestHelper helper) {
        ItemStack original = weapon(); original.setDamageValue(17);
        original.set(DataComponents.CUSTOM_NAME, Component.literal("Named survival weapon"));
        ItemStack result = original;
        for (var kind : GearEnchantments.definitions()) {
            helper.assertTrue(GearEnchantments.holder(helper.getLevel(), kind).isPresent(), "Native enchantment is missing: " + kind.id);
            for (int rank = 1; rank <= 3; rank++) {
                helper.assertTrue(GearEnchantments.canApply(result, helper.getLevel(), kind), "Valid next native enchantment level was rejected");
                result = GearEnchantments.apply(result, helper.getLevel(), kind);
                helper.assertTrue(GearEnchantments.level(result, helper.getLevel(), kind) == rank, "Native component did not hold the requested next level");
            }
            helper.assertFalse(GearEnchantments.canApply(result, helper.getLevel(), kind), "Service accepted a fourth level");
            helper.assertTrue(GearEnchantments.apply(result, helper.getLevel(), kind).isEmpty(), "Invalid application manufactured an output");
            helper.assertTrue(GearEnchantments.level(original, helper.getLevel(), kind) == 0, "Preview mutated the original inventory stack");
        }
        helper.assertTrue(result.getDamageValue() == 17 && original.get(DataComponents.CUSTOM_NAME).equals(result.get(DataComponents.CUSTOM_NAME)),
                "Enchanting lost original durability or custom name");
        for (ItemStack wrong : new ItemStack[]{new ItemStack(Items.DIAMOND_SWORD), new ItemStack(GearContent.ranged(RangedWeaponKind.POWER_BOW)),
                new ItemStack(GearContent.artifact(ArtifactKind.HARVESTER))})
            helper.assertFalse(GearEnchantments.canApply(wrong, helper.getLevel(), GearEnchantmentKind.RADIANCE), "Melee selection accepted an unsupported item");
        helper.succeed();
    }

    @GameTest(template = "gametest_empty", templateNamespace = DungeonsAlive.MOD_ID, batch = BATCH, timeoutTicks = 40)
    public static void committedAndCriticalModifyOnlyRealHostileDirectMelee(GameTestHelper helper) {
        Player player = player(helper);
        player.setItemInHand(InteractionHand.MAIN_HAND, enchanted(helper, GearEnchantmentKind.COMMITTED, GearEnchantmentKind.CRITICAL_HIT));
        var hostile = helper.spawnWithNoFreeWill(EntityType.SILVERFISH, new Vec3(6.5, 1, 5.5));
        hostile.setHealth(6); // 25% missing health: 1.075 Committed multiplier at III.
        player.getRandom().setSeed(4096); // First legacy nextDouble is below the 15% critical threshold.
        hostile.hurt(player.damageSources().playerAttack(player), 2);
        helper.assertTrue(close(6 - hostile.getHealth(), 4.8375F), "Combined native effects did not apply their single expected melee multiplier");
        var magic = helper.spawnWithNoFreeWill(EntityType.SILVERFISH, new Vec3(9.5, 1, 5.5));
        magic.hurt(player.damageSources().indirectMagic(player, player), 2);
        helper.assertTrue(close(magic.getHealth(), 6), "Secondary magic recursively received melee enchantment multipliers");
        var passive = helper.spawnWithNoFreeWill(EntityType.COW, new Vec3(12.5, 1, 5.5));
        float health = passive.getHealth(); passive.hurt(player.damageSources().playerAttack(player), 2);
        helper.assertTrue(close(health - passive.getHealth(), 2), "Passive targets received enchanted bonus damage");
        helper.succeed();
    }

    @GameTest(template = "gametest_empty", templateNamespace = DungeonsAlive.MOD_ID, batch = BATCH, timeoutTicks = 40)
    public static void radianceAndLeechingRequireAppliedDamageAndUncancelledDeathAndKeepTheirBounds(GameTestHelper helper) {
        Player player = player(helper); player.setHealth(10);
        player.setItemInHand(InteractionHand.MAIN_HAND, enchanted(helper, GearEnchantmentKind.RADIANCE, GearEnchantmentKind.LEECHING));
        var rejected = helper.spawnWithNoFreeWill(EntityType.SILVERFISH, new Vec3(6.5, 1, 5.5));
        Consumer<LivingIncomingDamageEvent> cancelDamage = event -> { if (event.getEntity() == rejected) event.setCanceled(true); };
        NeoForge.EVENT_BUS.addListener(EventPriority.HIGHEST, cancelDamage);
        try {
            rejected.hurt(player.damageSources().playerAttack(player), 2);
            helper.assertTrue(player.getHealth() == 10, "Cancelled melee granted Radiance healing");
        } finally { NeoForge.EVENT_BUS.unregister(cancelDamage); }
        var victim = helper.spawnWithNoFreeWill(EntityType.SILVERFISH, new Vec3(6.5, 1, 6.5));
        player.getRandom().setSeed(4096); victim.hurt(player.damageSources().playerAttack(player), 1);
        helper.assertTrue(close(player.getHealth(), 13), "A successful Radiance III roll did not heal three health");
        player.getRandom().setSeed(4096); victim.hurt(player.damageSources().playerAttack(player), 100);
        helper.assertTrue(close(player.getHealth(), 13.72F), "A direct hostile kill did not grant capped Leeching, or repeated Radiance bypassed its pause");
        var protectedDeath = helper.spawnWithNoFreeWill(EntityType.SILVERFISH, new Vec3(6.5, 1, 3.5));
        Consumer<LivingDeathEvent> cancelDeath = event -> { if (event.getEntity() == protectedDeath) event.setCanceled(true); };
        NeoForge.EVENT_BUS.addListener(EventPriority.HIGHEST, cancelDeath);
        try {
            protectedDeath.hurt(player.damageSources().playerAttack(player), 100);
            helper.assertTrue(close(player.getHealth(), 13.72F), "Cancelled death granted Leeching healing");
        } finally { NeoForge.EVENT_BUS.unregister(cancelDeath); }
        helper.runAfterDelay(21, () -> {
            var next = helper.spawnWithNoFreeWill(EntityType.SILVERFISH, new Vec3(6.5, 1, 8));
            player.getRandom().setSeed(4096); next.hurt(player.damageSources().playerAttack(player), 1);
            helper.assertTrue(close(player.getHealth(), 16.72F), "Radiance's finite pause did not expire");
            helper.succeed();
        });
    }

    @GameTest(template = "gametest_empty", templateNamespace = DungeonsAlive.MOD_ID, batch = BATCH, timeoutTicks = 40)
    public static void chainsAndSwirlingRespectCoverPassiveTargetsAndNonrecursiveAreaDamage(GameTestHelper helper) {
        Player player = player(helper);
        player.setItemInHand(InteractionHand.MAIN_HAND, enchanted(helper, GearEnchantmentKind.CHAINS, GearEnchantmentKind.SWIRLING));
        var direct = helper.spawnWithNoFreeWill(EntityType.SILVERFISH, new Vec3(6.5, 1, 5.5));
        var nearby = helper.spawnWithNoFreeWill(EntityType.SILVERFISH, new Vec3(7.5, 1, 6.5));
        var covered = helper.spawnWithNoFreeWill(EntityType.SILVERFISH, new Vec3(6.5, 1, 3.5));
        var passive = helper.spawnWithNoFreeWill(EntityType.COW, new Vec3(7.5, 1, 5.5));
        float passiveHealth = passive.getHealth();
        for (int x = 5; x <= 9; x++) for (int y = 1; y <= 3; y++) helper.setBlock(new BlockPos(x, y, 4), Blocks.STONE);
        direct.hurt(player.damageSources().playerAttack(player), 2);
        helper.assertTrue(close(direct.getHealth(), 6) && close(nearby.getHealth(), 4), "Swirling missed its side target or recursively hit the original target");
        helper.assertTrue(direct.hasEffect(MobEffects.MOVEMENT_SLOWDOWN) && nearby.hasEffect(MobEffects.MOVEMENT_SLOWDOWN), "Chains did not restrain visible living enemies");
        helper.assertTrue(close(covered.getHealth(), 8) && !covered.hasEffect(MobEffects.MOVEMENT_SLOWDOWN)
                        && passive.getHealth() == passiveHealth && !passive.hasEffect(MobEffects.MOVEMENT_SLOWDOWN),
                "Area enchantments crossed solid cover or affected a passive creature");
        var followup = helper.spawnWithNoFreeWill(EntityType.SILVERFISH, new Vec3(6.5, 1, 7));
        followup.hurt(player.damageSources().playerAttack(player), 2);
        helper.assertTrue(close(nearby.getHealth(), 4) && !followup.hasEffect(MobEffects.MOVEMENT_SLOWDOWN), "Same-tick melee bypassed finite area-effect pauses");
        helper.assertTrue(helper.getBlockState(new BlockPos(6, 2, 4)).is(Blocks.STONE), "Enchantment damaged the cover block");
        helper.succeed();
    }

    private static ItemStack weapon() { return new ItemStack(GearContent.weapon(WeaponKind.FIGHTERS_BINDINGS)); }
    private static ItemStack enchanted(GameTestHelper helper, GearEnchantmentKind... kinds) {
        ItemStack stack = weapon();
        for (var kind : kinds) for (int rank = 0; rank < 3; rank++) {
            stack = GearEnchantments.apply(stack, helper.getLevel(), kind);
            helper.assertFalse(stack.isEmpty(), "Native fixture enchantment could not be applied: " + kind.id);
        }
        return stack;
    }
    private static Player player(GameTestHelper helper) {
        for (int x = 0; x < 20; x++) for (int z = 0; z < 12; z++) helper.setBlock(new BlockPos(x, 0, z), Blocks.STONE);
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setPos(helper.absoluteVec(new Vec3(3.5, 1, 5.5)));
        player.getFoodData().setFoodLevel(1);
        return player;
    }
    private static boolean close(float actual, float expected) { return Math.abs(actual - expected) < .0001F; }
}
