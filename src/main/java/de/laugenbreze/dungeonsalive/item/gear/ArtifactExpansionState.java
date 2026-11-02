package de.laugenbreze.dungeonsalive.item.gear;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;

/** Cast quiver charges belong to the server player; item components only mirror the count. */
public final class ArtifactExpansionState {
    public static final int QUIVER_CAPACITY = 7;
    private static final String CHARGES = "DungeonsAliveFlamingQuiverCharges";
    private static final String DISPLAY = "dungeonsalive_flame_charges_display";

    private ArtifactExpansionState() {}

    public static int quiverCharges(Player player) {
        return Math.clamp(player.getPersistentData().getInt(CHARGES), 0, QUIVER_CAPACITY);
    }

    public static void fillQuiver(Player player) { setCharges(player, QUIVER_CAPACITY); }

    public static boolean consumeArrow(Player player) {
        if (player.level().isClientSide || player.isSpectator() || !player.isAlive() || quiverCharges(player) == 0) return false;
        setCharges(player, quiverCharges(player) - 1);
        return true;
    }

    public static void copyOnClone(Player original, Player replacement, boolean death) {
        setCharges(replacement, death ? 0 : quiverCharges(original));
    }

    private static void setCharges(Player player, int amount) {
        if (player.level().isClientSide) return;
        player.getPersistentData().putInt(CHARGES, Math.clamp(amount, 0, QUIVER_CAPACITY));
        for (int slot = 0; slot < player.getInventory().getContainerSize(); slot++) {
            refreshStack(player, player.getInventory().getItem(slot));
        }
    }

    public static void refreshStack(Player player, ItemStack stack) {
        if (player.level().isClientSide || !(stack.getItem() instanceof DungeonsArtifactItem item)
                || item.kind() != ArtifactKind.FLAMING_QUIVER || displayed(stack) == quiverCharges(player)) return;
        CustomData.update(DataComponents.CUSTOM_DATA, stack, tag -> tag.putInt(DISPLAY, quiverCharges(player)));
    }

    public static int displayed(ItemStack stack) {
        return Math.clamp(stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY)
                .copyTag().getInt(DISPLAY), 0, QUIVER_CAPACITY);
    }
}
