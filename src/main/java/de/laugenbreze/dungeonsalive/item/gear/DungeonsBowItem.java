package de.laugenbreze.dungeonsalive.item.gear;

import java.util.List;
import java.util.Objects;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

/** Keeps vanilla drawing, ammunition, enchantments, durability and projectile flight. */
public final class DungeonsBowItem extends BowItem {
    private final RangedWeaponKind kind;

    public DungeonsBowItem(RangedWeaponKind kind, Properties properties) {
        super(properties.durability(Objects.requireNonNull(kind).durability));
        if (kind.crossbow) throw new IllegalArgumentException("Crossbow kind used as a bow: " + kind);
        this.kind = kind;
    }

    public RangedWeaponKind kind() { return kind; }

    @Override public AbstractArrow customArrow(AbstractArrow arrow, ItemStack projectileStack, ItemStack weaponStack) {
        RangedWeaponEvents.prepareArrow(arrow, projectileStack, weaponStack, kind);
        return arrow;
    }

    @Override public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> lines, TooltipFlag flag) {
        super.appendHoverText(stack, context, lines, flag);
        lines.add(Component.translatable("item.dungeonsalive." + kind.id + ".tooltip").withStyle(ChatFormatting.GRAY));
    }
}
