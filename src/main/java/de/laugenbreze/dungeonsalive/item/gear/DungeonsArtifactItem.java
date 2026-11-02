package de.laugenbreze.dungeonsalive.item.gear;

import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.List;

/** Native Minecraft artifact behavior; selected Dungeons Gear art is credited separately. */
public final class DungeonsArtifactItem extends Item {
    private final ArtifactKind kind;
    public DungeonsArtifactItem(ArtifactKind kind, Properties properties) { super(properties); this.kind = kind; }
    public ArtifactKind kind() { return kind; }

    @Override public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (player.getCooldowns().isOnCooldown(this) || player.isSpectator()) return InteractionResultHolder.fail(stack);
        if (level.isClientSide) {
            if (kind == ArtifactKind.CORRUPTED_BEACON) player.startUsingItem(hand);
            return InteractionResultHolder.sidedSuccess(stack, true);
        }
        if (!(level instanceof ServerLevel server)) return InteractionResultHolder.fail(stack);
        if (kind == ArtifactKind.SOUL_HEALER && (!player.isAlive() || player.getHealth() >= player.getMaxHealth())) {
            player.displayClientMessage(Component.translatable("message.dungeonsalive.soul_healer_full"), true);
            return InteractionResultHolder.fail(stack);
        }
        Vec3 end = kind == ArtifactKind.LIGHTNING_ROD ? ArtifactCombat.rayEnd(player, 24) : player.position();
        List<LivingEntity> selected = kind == ArtifactKind.LIGHTNING_ROD
                ? ArtifactCombat.inRay(player, end, .5) : List.of();
        if (kind == ArtifactKind.LIGHTNING_ROD && selected.isEmpty()) {
            player.displayClientMessage(Component.translatable("message.dungeonsalive.artifact_no_target"), true);
            return InteractionResultHolder.fail(stack);
        }
        if (kind == ArtifactKind.CORRUPTED_BEACON) {
            if (!player.isCreative() && SoulEnergy.get(player) < kind.soulCost) return insufficient(player, stack);
            player.startUsingItem(hand);
            return InteractionResultHolder.consume(stack);
        }
        if (kind.soulCost > 0 && !SoulEnergy.consume(player, kind.soulCost)) return insufficient(player, stack);
        player.getCooldowns().addCooldown(this, ArmorEffects.artifactCooldown(player, kind.cooldownTicks));
        switch (kind) {
            case BOOTS_OF_SWIFTNESS -> player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 100, 2));
            case DEATH_CAP_MUSHROOM -> {
                player.addEffect(new MobEffectInstance(MobEffects.DIG_SPEED, 180, 3));
                player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 180, 1));
            }
            case IRON_HIDE_AMULET -> player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 200, 1));
            case SOUL_HEALER -> player.heal(8);
            case GHOST_CLOAK -> {
                // A short escape buff preserves Minecraft collision and player-built walls.
                player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 60, 1));
                player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 60, 0));
            }
            case FLAMING_QUIVER -> {
                ArtifactExpansionState.fillQuiver(player);
                player.displayClientMessage(Component.translatable("message.dungeonsalive.flaming_quiver_loaded",
                        ArtifactExpansionState.QUIVER_CAPACITY), true);
            }
            case SHOCK_POWDER -> {
                for (LivingEntity target : ArtifactCombat.nearby(player, player.position(), 5)) {
                    target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 80, 4), player);
                    target.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 80, 1), player);
                }
            }
            case GONG_OF_WEAKENING -> {
                for (LivingEntity target : ArtifactCombat.nearby(player, player.position(), 6)) {
                    target.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 140, 1), player);
                    target.addEffect(new MobEffectInstance(MobEffects.GLOWING, 140, 0), player);
                    target.getPersistentData().putLong(GearEvents.GONG_UNTIL, level.getGameTime() + 140);
                }
            }
            case HARVESTER -> {
                for (LivingEntity target : ArtifactCombat.nearby(player, player.position(), 5))
                    target.hurt(player.damageSources().indirectMagic(player, player), 12);
            }
            case LIGHTNING_ROD -> {
                LivingEntity primary = selected.getFirst();
                for (LivingEntity target : ArtifactCombat.nearby(player, primary.position(), 2)) {
                    if (ArtifactCombat.clearLine(player, player.getEyePosition(), target.getBoundingBox().getCenter()))
                        target.hurt(player.damageSources().indirectMagic(player, player), 14);
                }
                Vec3 center = primary.getBoundingBox().getCenter();
                for (int i = 0; i < 16; i++) server.sendParticles(ParticleTypes.ELECTRIC_SPARK,
                        center.x, center.y + i * .35, center.z, 2, .12, .05, .12, .03);
                server.playSound(null, primary.blockPosition(), SoundEvents.LIGHTNING_BOLT_THUNDER, SoundSource.PLAYERS, .35F, 1.4F);
            }
            default -> { }
        }
        server.sendParticles(kind == ArtifactKind.HARVESTER ? ParticleTypes.SOUL : ParticleTypes.ENCHANT,
                player.getX(), player.getY() + 1, player.getZ(), 28, 1.3, .5, 1.3, .05);
        server.playSound(null, player.blockPosition(), kind == ArtifactKind.GONG_OF_WEAKENING
                ? SoundEvents.BELL_BLOCK : SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.PLAYERS, .7F, .9F);
        player.awardStat(Stats.ITEM_USED.get(this));
        return InteractionResultHolder.sidedSuccess(stack, false);
    }

    private InteractionResultHolder<ItemStack> insufficient(Player player, ItemStack stack) {
        player.displayClientMessage(Component.translatable("message.dungeonsalive.artifact_souls_needed", kind.soulCost, SoulEnergy.get(player)), true);
        return InteractionResultHolder.fail(stack);
    }

    @Override public int getUseDuration(ItemStack stack, LivingEntity user) { return kind == ArtifactKind.CORRUPTED_BEACON ? 72000 : 0; }
    @Override public UseAnim getUseAnimation(ItemStack stack) { return kind == ArtifactKind.CORRUPTED_BEACON ? UseAnim.BOW : UseAnim.NONE; }

    @Override public void onUseTick(Level level, LivingEntity user, ItemStack stack, int remaining) {
        if (kind != ArtifactKind.CORRUPTED_BEACON || !(user instanceof Player player) || !(level instanceof ServerLevel server)) return;
        int elapsed = getUseDuration(stack, user) - remaining;
        if (elapsed % 10 == 0) {
            if (!SoulEnergy.consume(player, kind.soulCost)) {
                player.stopUsingItem();
                return;
            }
            Vec3 end = ArtifactCombat.rayEnd(player, 24);
            for (LivingEntity target : ArtifactCombat.inRay(player, end, .3))
                target.hurt(player.damageSources().indirectMagic(player, player), 6);
            if (elapsed % 20 == 0) server.playSound(null, player.blockPosition(), SoundEvents.BEACON_AMBIENT, SoundSource.PLAYERS, .35F, 1.7F);
        }
        if (elapsed % 2 == 0) {
            // A full-sized soul flame directly in front of the first-person
            // camera obscures aiming. Keep the same server ray, but draw a
            // narrow luminous trail starting beyond the hand.
            Vec3 start = player.getEyePosition().add(player.getLookAngle().scale(1.25));
            Vec3 end = ArtifactCombat.rayEnd(player, 24);
            int points = Math.min(48, Math.max(1, (int) (start.distanceTo(end) * 2)));
            for (int i = 0; i <= points; i++) {
                Vec3 at = start.lerp(end, i / (double) points);
                server.sendParticles(new DustParticleOptions(new org.joml.Vector3f(.25F, .8F, 1F), .45F),
                        at.x, at.y, at.z, 1, .015, .015, .015, 0);
            }
        }
    }

    @Override public void onStopUsing(ItemStack stack, LivingEntity user, int remaining) {
        if (!user.level().isClientSide && kind == ArtifactKind.CORRUPTED_BEACON && user instanceof Player player) {
            player.getCooldowns().addCooldown(this, ArmorEffects.artifactCooldown(player, kind.cooldownTicks));
            player.awardStat(Stats.ITEM_USED.get(this));
        }
    }

    @Override public void inventoryTick(ItemStack stack, Level level, Entity holder, int slot, boolean selected) {
        if (!level.isClientSide && holder instanceof Player player && player.tickCount % 20 == 0) {
            SoulEnergy.refreshStack(player, stack);
            ArtifactExpansionState.refreshStack(player, stack);
        }
    }

    @Override public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> lines, TooltipFlag flag) {
        lines.add(Component.translatable("item.dungeonsalive." + kind.id + ".tooltip").withStyle(ChatFormatting.GRAY));
        lines.add(Component.translatable("tooltip.dungeonsalive.artifact_cooldown", kind.cooldownTicks / 20).withStyle(ChatFormatting.DARK_GRAY));
        if (kind == ArtifactKind.FLAMING_QUIVER) {
            lines.add(Component.translatable("tooltip.dungeonsalive.flaming_quiver_charges",
                    ArtifactExpansionState.displayed(stack), ArtifactExpansionState.QUIVER_CAPACITY).withStyle(ChatFormatting.GOLD));
        }
        if (kind.soulCost > 0) {
            lines.add(Component.translatable("tooltip.dungeonsalive.artifact_souls", SoulEnergy.displayed(stack), SoulEnergy.CAPACITY).withStyle(ChatFormatting.AQUA));
            lines.add(Component.translatable(kind == ArtifactKind.CORRUPTED_BEACON ? "tooltip.dungeonsalive.beacon_cost"
                    : "tooltip.dungeonsalive.artifact_cost", kind.soulCost).withStyle(ChatFormatting.DARK_AQUA));
        }
    }
}
