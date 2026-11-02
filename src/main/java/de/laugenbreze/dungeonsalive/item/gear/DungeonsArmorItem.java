package de.laugenbreze.dungeonsalive.item.gear;

import de.laugenbreze.dungeonsalive.DungeonsAlive;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

/** Ordinary durable armor item; its source-shaped wearable mesh is installed exclusively by the client. */
public final class DungeonsArmorItem extends ArmorItem {
    private final ArmorSetKind kind;
    private final ResourceLocation wornTexture;
    public DungeonsArmorItem(ArmorSetKind kind, Holder<ArmorMaterial> material, Type type, Properties properties) {
        super(material, type, properties);
        this.kind = kind;
        wornTexture = ResourceLocation.fromNamespaceAndPath(DungeonsAlive.MOD_ID, "textures/models/armor/" + kind.id + "_armor.png");
    }
    public ArmorSetKind kind() { return kind; }
    @Override public ResourceLocation getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, ArmorMaterial.Layer layer, boolean innerModel) {
        return wornTexture;
    }
    @Override public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> lines, TooltipFlag flag) {
        super.appendHoverText(stack, context, lines, flag);
        lines.add(Component.translatable("tooltip.dungeonsalive.armor_full_set").withStyle(ChatFormatting.GRAY));
        lines.add(Component.translatable("tooltip.dungeonsalive.armor_set." + kind.id).withStyle(ChatFormatting.DARK_AQUA));
    }
}
