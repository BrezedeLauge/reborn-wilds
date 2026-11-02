package de.laugenbreze.dungeonsalive.merchant;

import de.laugenbreze.dungeonsalive.item.gear.DungeonsBowItem;
import de.laugenbreze.dungeonsalive.item.gear.DungeonsCrossbowItem;
import de.laugenbreze.dungeonsalive.item.gear.DungeonsWeaponItem;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;

/** Permanent three-rank weapon improvement; copies retain damage, names, enchantments and all unrelated components. */
public final class GearUpgrade {
    public static final String KEY = "DungeonsAliveGearUpgrade";
    private GearUpgrade() {}
    public static boolean supports(ItemStack stack) {
        return !stack.isEmpty() && (stack.getItem() instanceof DungeonsWeaponItem
                || stack.getItem() instanceof DungeonsBowItem || stack.getItem() instanceof DungeonsCrossbowItem);
    }
    public static int rank(ItemStack stack) {
        if (!supports(stack)) return 0;
        CompoundTag root = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        if (!root.contains(KEY, Tag.TAG_COMPOUND)) return 0;
        CompoundTag value = root.getCompound(KEY);
        if (value.getInt("Version") != 1 || !value.contains("Rank", Tag.TAG_INT)) return 0;
        int rank = value.getInt("Rank");
        return rank >= 0 && rank <= MerchantServicePolicy.MAX_RANK ? rank : 0;
    }
    public static boolean canUpgrade(ItemStack stack) {
        return stack.getCount() == 1 && supports(stack) && rank(stack) < MerchantServicePolicy.MAX_RANK;
    }
    public static ItemStack upgrade(ItemStack stack) {
        if (!canUpgrade(stack)) return ItemStack.EMPTY;
        ItemStack result = stack.copy();
        int next = rank(stack) + 1;
        CustomData.update(DataComponents.CUSTOM_DATA, result, root -> {
            CompoundTag value = new CompoundTag(); value.putInt("Version", 1); value.putInt("Rank", next);
            root.put(KEY, value);
        });
        return result;
    }
    public static double damageMultiplier(ItemStack stack) { return MerchantServicePolicy.damageMultiplier(rank(stack)); }
}
