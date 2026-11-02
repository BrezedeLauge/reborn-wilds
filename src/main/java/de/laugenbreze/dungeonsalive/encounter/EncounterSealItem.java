package de.laugenbreze.dungeonsalive.encounter;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;

import java.util.List;

/** Own sandbox mechanic; seals are not original Minecraft Dungeons items. */
public final class EncounterSealItem extends Item {
    private final String mobId;
    public EncounterSealItem(String mobId, Properties properties) {
        super(properties);
        this.mobId = mobId;
    }
    public String mobId() { return mobId; }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        if (context.getPlayer() == null) return InteractionResult.FAIL;
        if (!context.getPlayer().isShiftKeyDown()) {
            if (!context.getLevel().isClientSide) context.getPlayer().displayClientMessage(
                    Component.translatable("message.dungeonsalive.seal.sneak"), true);
            return InteractionResult.FAIL;
        }
        if (context.getLevel().isClientSide) return InteractionResult.SUCCESS;
        return EncounterService.useSeal(context, mobId);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> lines, TooltipFlag flag) {
        lines.add(Component.translatable("item.dungeonsalive.seal.tooltip").withStyle(ChatFormatting.GOLD));
        lines.add(Component.translatable("item.dungeonsalive.seal.prototype").withStyle(ChatFormatting.GRAY));
    }
}
