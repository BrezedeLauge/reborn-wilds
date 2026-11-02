package de.laugenbreze.dungeonsalive.item.gear;

import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;

/** One server-owned account per player. Item components are display-only mirrors. */
public final class SoulEnergy {
    public static final int CAPACITY = 100;
    private static final String KEY = "DungeonsAliveSouls";
    private static final String DISPLAY = "dungeonsalive_souls_display";
    private SoulEnergy() {}

    public static int get(Player player) {
        return Math.max(0, Math.min(CAPACITY, player.getPersistentData().getInt(KEY)));
    }

    public static void set(Player player, int amount) {
        if (player.level().isClientSide) return;
        player.getPersistentData().putInt(KEY, Math.max(0, Math.min(CAPACITY, amount)));
        refreshInventory(player);
    }

    public static boolean consume(Player player, int amount) {
        if (player.level().isClientSide || amount < 0) return false;
        if (player.isCreative()) return true;
        if (get(player) < amount) return false;
        set(player, get(player) - amount);
        return true;
    }

    public static boolean carriesSoulArtifact(Player player) {
        for (int slot = 0; slot < player.getInventory().getContainerSize(); slot++) {
            ItemStack stack = player.getInventory().getItem(slot);
            if (stack.getItem() instanceof DungeonsArtifactItem item && item.kind().soulCost > 0) return true;
        }
        return false;
    }

    public static boolean carriesSoulCollector(Player player) {
        if (carriesSoulArtifact(player)) return true;
        for (int slot = 0; slot < player.getInventory().getContainerSize(); slot++) {
            ItemStack stack = player.getInventory().getItem(slot);
            if (stack.getItem() instanceof DungeonsWeaponItem weapon && weapon.kind().family.soulBonus > 0) return true;
        }
        return false;
    }

    public static void earn(Player player, int amount) {
        if (player.level().isClientSide || amount <= 0 || !carriesSoulCollector(player)) return;
        set(player, get(player) + amount);
        player.displayClientMessage(Component.translatable("message.dungeonsalive.souls", get(player), CAPACITY), true);
    }

    public static void refreshInventory(Player player) {
        for (int slot = 0; slot < player.getInventory().getContainerSize(); slot++)
            refreshStack(player, player.getInventory().getItem(slot));
    }

    public static void refreshStack(Player player, ItemStack stack) {
        if (player.level().isClientSide || !(stack.getItem() instanceof DungeonsArtifactItem item)
                || item.kind().soulCost == 0 || displayed(stack) == get(player)) return;
        CustomData.update(DataComponents.CUSTOM_DATA, stack, tag -> tag.putInt(DISPLAY, get(player)));
    }

    public static int displayed(ItemStack stack) {
        return Math.max(0, Math.min(CAPACITY, stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY)
                .copyTag().getInt(DISPLAY)));
    }
}
