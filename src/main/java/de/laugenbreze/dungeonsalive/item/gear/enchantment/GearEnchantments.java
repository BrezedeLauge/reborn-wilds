package de.laugenbreze.dungeonsalive.item.gear.enchantment;

import de.laugenbreze.dungeonsalive.DungeonsAlive;
import de.laugenbreze.dungeonsalive.item.gear.DungeonsWeaponItem;
import java.util.List;
import java.util.Optional;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;

/** Datapack-backed registry access and copy-only service API; no client-selected result stack is trusted. */
public final class GearEnchantments {
    private static final List<GearEnchantmentKind> DEFINITIONS = List.of(GearEnchantmentKind.values());
    private GearEnchantments() {}
    public static List<GearEnchantmentKind> definitions() { return DEFINITIONS; }
    public static ResourceKey<Enchantment> key(GearEnchantmentKind kind) {
        return ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.fromNamespaceAndPath(DungeonsAlive.MOD_ID, kind.id));
    }
    public static Optional<Holder.Reference<Enchantment>> holder(ServerLevel level, GearEnchantmentKind kind) {
        return level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).get(key(kind));
    }
    public static int level(ItemStack stack, ServerLevel level, GearEnchantmentKind kind) {
        return holder(level, kind).map(enchantment -> GearEnchantmentRules.level(
                stack.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY).getLevel(enchantment))).orElse(0);
    }
    public static boolean canApply(ItemStack stack, ServerLevel level, GearEnchantmentKind kind) {
        if (stack.isEmpty() || stack.getCount() != 1 || !(stack.getItem() instanceof DungeonsWeaponItem)) return false;
        var registered = holder(level, kind);
        if (registered.isEmpty()) return false;
        var enchantment = registered.get();
        if (!stack.supportsEnchantment(enchantment)) return false;
        var stored = stack.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY);
        int current = stored.getLevel(enchantment);
        if (current < 0 || current >= Math.min(GearEnchantmentKind.MAX_LEVEL, enchantment.value().getMaxLevel())) return false;
        for (var entry : stack.getAllEnchantments(level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT)).entrySet()) {
            if (!entry.getKey().equals(enchantment) && !Enchantment.areCompatible(entry.getKey(), enchantment)) return false;
        }
        return true;
    }
    /** Add the next level to a detached copy, preserving damage, names, upgrades and other components. */
    public static ItemStack apply(ItemStack stack, ServerLevel level, GearEnchantmentKind kind) {
        if (!canApply(stack, level, kind)) return ItemStack.EMPTY;
        ItemStack result = stack.copy();
        result.enchant(holder(level, kind).orElseThrow(), level(stack, level, kind) + 1);
        return result;
    }
    /** Gameplay respects NeoForge's effective enchantments, while menu levels reflect stored components. */
    static int effectiveLevel(ItemStack stack, ServerLevel level, GearEnchantmentKind kind) {
        if (!(stack.getItem() instanceof DungeonsWeaponItem)) return 0;
        return holder(level, kind).filter(stack::supportsEnchantment)
                .map(enchantment -> GearEnchantmentRules.level(stack.getEnchantmentLevel(enchantment))).orElse(0);
    }
}
