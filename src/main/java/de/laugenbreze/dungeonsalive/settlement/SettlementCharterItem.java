package de.laugenbreze.dungeonsalive.settlement;

import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;

public final class SettlementCharterItem extends Item {
    public SettlementCharterItem(Properties properties) { super(properties); }
    @Override public InteractionResult useOn(UseOnContext context) {
        return context.getLevel().isClientSide ? InteractionResult.SUCCESS : SettlementService.found(context);
    }
    @Override public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("tooltip.dungeonsalive.settlement_charter"));
    }
}
