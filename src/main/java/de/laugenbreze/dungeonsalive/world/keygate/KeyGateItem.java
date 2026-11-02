package de.laugenbreze.dungeonsalive.world.keygate;

import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

public final class KeyGateItem extends BlockItem {
    private final String key;
    public KeyGateItem(KeyGateBlock block, Properties properties) {
        super(block, properties); key = block.requiredGolem();
    }
    @Override public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> lines, TooltipFlag flag) {
        super.appendHoverText(stack, context, lines, flag);
        lines.add(Component.translatable("tooltip.dungeonsalive.key_gate", Component.translatable("entity.dungeonsalive." + key)));
    }
}
