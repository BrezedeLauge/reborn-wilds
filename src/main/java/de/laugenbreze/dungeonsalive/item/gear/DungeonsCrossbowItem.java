package de.laugenbreze.dungeonsalive.item.gear;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.ChargedProjectiles;
import net.minecraft.world.level.Level;

/** Uses real charged-projectile components and vanilla firing; auto loading stays per user. */
public final class DungeonsCrossbowItem extends CrossbowItem {
    private final RangedWeaponKind kind;

    public DungeonsCrossbowItem(RangedWeaponKind kind, Properties properties) {
        super(properties.durability(Objects.requireNonNull(kind).durability));
        if (!kind.crossbow) throw new IllegalArgumentException("Bow kind used as a crossbow: " + kind);
        this.kind = kind;
    }

    public RangedWeaponKind kind() { return kind; }

    /** Includes vanilla Quick Charge, with a finite lower bound for this adaptation. */
    public int chargeDuration(ItemStack stack, LivingEntity user) {
        int vanilla = CrossbowItem.getChargeDuration(stack, user);
        return kind == RangedWeaponKind.AUTO_CROSSBOW ? Math.max(3, (vanilla + 1) / 2) : vanilla;
    }

    @Override public int getUseDuration(ItemStack stack, LivingEntity user) {
        return kind == RangedWeaponKind.AUTO_CROSSBOW ? chargeDuration(stack, user) + 3 : super.getUseDuration(stack, user);
    }

    @Override public void onUseTick(Level level, LivingEntity user, ItemStack stack, int remaining) {
        if (kind != RangedWeaponKind.AUTO_CROSSBOW) {
            super.onUseTick(level, user, stack, remaining);
            return;
        }
        int elapsed = getUseDuration(stack, user) - remaining;
        if (elapsed >= chargeDuration(stack, user)) {
            loadAuto(level, user, stack);
            user.stopUsingItem();
        } else if (elapsed == 0 && level instanceof ServerLevel server) {
            server.playSound(null, user.blockPosition(), SoundEvents.CROSSBOW_LOADING_START.value(), SoundSource.PLAYERS, .5F, 1.2F);
        }
    }

    @Override public void releaseUsing(ItemStack stack, Level level, LivingEntity user, int remaining) {
        if (kind != RangedWeaponKind.AUTO_CROSSBOW) {
            super.releaseUsing(stack, level, user, remaining);
        } else if (getUseDuration(stack, user) - remaining >= chargeDuration(stack, user)) {
            loadAuto(level, user, stack);
        }
    }

    private void loadAuto(Level level, LivingEntity user, ItemStack stack) {
        if (!(level instanceof ServerLevel server) || isCharged(stack)) return;
        ItemStack ammunition = user.getProjectile(stack);
        if (ammunition.isEmpty()) return;
        // The shared vanilla draw path applies ammunition use and Multishot rather
        // than manufacturing a separate arrow or bypassing ammo enchantments.
        List<ItemStack> drawn = draw(stack, ammunition, user);
        if (drawn.isEmpty()) return;
        stack.set(DataComponents.CHARGED_PROJECTILES, ChargedProjectiles.of(drawn));
        server.playSound(null, user.blockPosition(), SoundEvents.CROSSBOW_LOADING_END.value(), SoundSource.PLAYERS, 1, 1.2F);
    }

    @Override public Predicate<ItemStack> getSupportedHeldProjectiles() {
        // Explosive arrows have bounded hostile-only splash. Vanilla fireworks
        // have a different damage policy, so this particular weapon accepts arrows.
        return kind == RangedWeaponKind.EXPLODING_CROSSBOW ? getAllSupportedProjectiles() : super.getSupportedHeldProjectiles();
    }

    @Override public AbstractArrow customArrow(AbstractArrow arrow, ItemStack projectileStack, ItemStack weaponStack) {
        RangedWeaponEvents.prepareArrow(arrow, projectileStack, weaponStack, kind);
        return arrow;
    }

    @Override public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> lines, TooltipFlag flag) {
        super.appendHoverText(stack, context, lines, flag);
        lines.add(Component.translatable("item.dungeonsalive." + kind.id + ".tooltip").withStyle(ChatFormatting.GRAY));
    }
}
