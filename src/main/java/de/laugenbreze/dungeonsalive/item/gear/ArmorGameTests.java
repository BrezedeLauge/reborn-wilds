package de.laugenbreze.dungeonsalive.item.gear;

import de.laugenbreze.dungeonsalive.DungeonsAlive;
import java.util.function.Consumer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

/** Real equipment ticks, damage events and artifact callbacks; rendering needs a separate native client check. */
@GameTestHolder(DungeonsAlive.MOD_ID)
@PrefixGameTestTemplate(false)
public final class ArmorGameTests {
    private ArmorGameTests() {}
    @GameTest(template = "gametest_empty", templateNamespace = DungeonsAlive.MOD_ID, batch = "armor_sets", timeoutTicks = 40)
    public static void allPiecesAreDurableArmorAndOnlyAnActualFullWolfSetBoostsMelee(GameTestHelper helper) {
        Player player = player(helper);
        for (var kind : ArmorSetKind.values()) for (var type : ArmorContent.PIECES) {
            var item = ArmorContent.armor(kind, type);
            helper.assertTrue(BuiltInRegistries.ITEM.getKey(item).getPath().equals(kind.id + "_" + type.getName())
                            && item.getType() == type && item.getDefaultInstance().getMaxDamage() == type.getDurability(kind.durabilityMultiplier),
                    "Armor registration, slot or durability differs from its declared piece");
        }
        equip(player, ArmorSetKind.WOLF);
        var boosted = helper.spawnWithNoFreeWill(EntityType.SILVERFISH, new Vec3(6.5, 1, 3.5));
        float before = boosted.getHealth();
        helper.assertTrue(boosted.hurt(player.damageSources().playerAttack(player), 2), "Actual hostile melee was rejected");
        helper.assertTrue(close(before - boosted.getHealth(), 2.4F), "Full Wolf set did not apply its direct-melee bonus");
        player.setItemSlot(EquipmentSlot.CHEST, new ItemStack(ArmorContent.armor(ArmorSetKind.SPIDER, ArmorItem.Type.CHESTPLATE)));
        helper.assertTrue(ArmorEffects.fullSet(player) == null, "Mixed armor granted a full-set identity");
        var ordinary = helper.spawnWithNoFreeWill(EntityType.SILVERFISH, new Vec3(9.5, 1, 3.5));
        before = ordinary.getHealth(); ordinary.hurt(player.damageSources().playerAttack(player), 2);
        helper.assertTrue(close(before - ordinary.getHealth(), 2), "Mixed set retained the old Wolf damage bonus");
        equip(player, ArmorSetKind.WOLF);
        var spell = helper.spawnWithNoFreeWill(EntityType.SILVERFISH, new Vec3(12.5, 1, 3.5));
        before = spell.getHealth(); spell.hurt(player.damageSources().indirectMagic(player, player), 2);
        helper.assertTrue(close(before - spell.getHealth(), 2), "Melee armor boosted an artifact-like magic source");
        helper.succeed();
    }

    @GameTest(template = "gametest_empty", templateNamespace = DungeonsAlive.MOD_ID, batch = "armor_sets", timeoutTicks = 40)
    public static void spiderUsesEquipmentEventsAndOnlyAppliedHostileMeleeHealsWithABoundedPause(GameTestHelper helper) {
        Player player = player(helper);
        double baseSpeed = player.getAttributeValue(Attributes.ATTACK_SPEED);
        equip(player, ArmorSetKind.SPIDER);
        // The lightweight GameTest player is not in the level. Tick its real Player
        // implementation explicitly so vanilla equipment detection dispatches the event.
        player.tick(); player.tick();
        helper.assertTrue(Math.abs(player.getAttributeValue(Attributes.ATTACK_SPEED) - baseSpeed * 1.25) < .00001,
                "Real equipment detection did not install exactly one Spider speed modifier");
        player.setHealth(10);
        var rejected = helper.spawnWithNoFreeWill(EntityType.SILVERFISH, new Vec3(6.5, 1, 3.5));
        Consumer<LivingIncomingDamageEvent> cancel = event -> { if (event.getEntity() == rejected) event.setCanceled(true); };
        NeoForge.EVENT_BUS.addListener(EventPriority.HIGHEST, cancel);
        try {
            rejected.hurt(player.damageSources().playerAttack(player), 2);
            helper.assertTrue(player.getHealth() == 10, "Cancelled damage granted life steal");
        } finally { NeoForge.EVENT_BUS.unregister(cancel); }
        var passive = helper.spawnWithNoFreeWill(EntityType.COW, new Vec3(9.5, 1, 3.5));
        passive.hurt(player.damageSources().playerAttack(player), 2);
        helper.assertTrue(player.getHealth() == 10, "Passive-animal damage granted life steal");
        var first = helper.spawnWithNoFreeWill(EntityType.SILVERFISH, new Vec3(6.5, 1, 7.5));
        first.hurt(player.damageSources().playerAttack(player), 2);
        helper.assertTrue(close(player.getHealth(), 10.12F), "Accepted hostile damage did not heal exactly six percent");
        var second = helper.spawnWithNoFreeWill(EntityType.SILVERFISH, new Vec3(9.5, 1, 7.5));
        second.hurt(player.damageSources().playerAttack(player), 2);
        helper.assertTrue(close(player.getHealth(), 10.12F), "Same-tick multi-hit bypassed the life-steal pause");
        helper.runAfterDelay(11, () -> {
            var next = helper.spawnWithNoFreeWill(EntityType.SILVERFISH, new Vec3(12.5, 1, 7.5));
            next.hurt(player.damageSources().playerAttack(player), 2);
            helper.assertTrue(close(player.getHealth(), 10.24F), "Finite life-steal pause did not expire");
            player.setItemSlot(EquipmentSlot.FEET, ItemStack.EMPTY); player.tick();
            helper.assertTrue(ArmorEffects.fullSet(player) == null
                            && Math.abs(player.getAttributeValue(Attributes.ATTACK_SPEED) - baseSpeed) < .00001,
                    "Removing one piece retained the full-set speed modifier");
            helper.succeed();
        });
    }

    @GameTest(template = "gametest_empty", templateNamespace = DungeonsAlive.MOD_ID, batch = "armor_sets", timeoutTicks = 40)
    public static void robesShortenActualArtifactUseAndBeaconStopCooldownsOnlyWhileComplete(GameTestHelper helper) {
        Player player = player(helper);
        equip(player, ArmorSetKind.BATTLE_ROBES);
        var boots = GearContent.artifact(ArtifactKind.BOOTS_OF_SWIFTNESS);
        player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(boots));
        helper.assertTrue(boots.use(helper.getLevel(), player, InteractionHand.MAIN_HAND).getResult().consumesAction(), "Boots artifact did not activate");
        for (int i = 0; i < 159; i++) player.getCooldowns().tick();
        helper.assertTrue(player.getCooldowns().isOnCooldown(boots), "Robes removed a cooldown too early");
        player.getCooldowns().tick();
        helper.assertFalse(player.getCooldowns().isOnCooldown(boots), "Real artifact cooldown was not shortened from 200 to 160 ticks");
        player.setItemSlot(EquipmentSlot.HEAD, ItemStack.EMPTY);
        boots.use(helper.getLevel(), player, InteractionHand.MAIN_HAND);
        for (int i = 0; i < 160; i++) player.getCooldowns().tick();
        helper.assertTrue(player.getCooldowns().isOnCooldown(boots), "Incomplete robes retained reduced artifact cooldown");
        equip(player, ArmorSetKind.BATTLE_ROBES);
        var beacon = GearContent.artifact(ArtifactKind.CORRUPTED_BEACON);
        SoulEnergy.set(player, 10);
        player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(beacon));
        beacon.use(helper.getLevel(), player, InteractionHand.MAIN_HAND);
        helper.assertTrue(player.isUsingItem(), "Beacon did not begin channeling");
        player.stopUsingItem();
        for (int i = 0; i < 47; i++) player.getCooldowns().tick();
        helper.assertTrue(player.getCooldowns().isOnCooldown(beacon), "Beacon stop lost its bounded cooldown");
        player.getCooldowns().tick();
        helper.assertFalse(player.getCooldowns().isOnCooldown(beacon), "Beacon stop callback ignored the current complete robe set");
        helper.succeed();
    }

    private static Player player(GameTestHelper helper) {
        for (int x = 0; x < 20; x++) for (int z = 0; z < 12; z++) helper.setBlock(new BlockPos(x, 0, z), Blocks.STONE);
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setPos(helper.absoluteVec(new Vec3(3.5, 1, 5.5)));
        player.getFoodData().setFoodLevel(1);
        return player;
    }
    private static void equip(Player player, ArmorSetKind kind) {
        for (var type : ArmorContent.PIECES) player.setItemSlot(type.getSlot(), new ItemStack(ArmorContent.armor(kind, type)));
    }
    private static boolean close(float a, float b) { return Math.abs(a - b) < .0001F; }
}
