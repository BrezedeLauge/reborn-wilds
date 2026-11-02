package de.laugenbreze.dungeonsalive.client;

import de.laugenbreze.dungeonsalive.merchant.GearUpgrade;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

public final class GearUpgradeClient {
    private GearUpgradeClient() {}
    public static void tooltip(ItemTooltipEvent event) {
        int rank = GearUpgrade.rank(event.getItemStack());
        if (rank > 0) event.getToolTip().add(Component.translatable("tooltip.dungeonsalive.gear_rank", rank, rank * 5)
                .withStyle(ChatFormatting.GOLD));
    }
}
