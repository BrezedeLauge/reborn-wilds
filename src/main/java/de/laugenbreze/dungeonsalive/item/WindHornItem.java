package de.laugenbreze.dungeonsalive.item;

import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.List;

/** An original, defensive interpretation of the Dungeons wind-horn idea. */
public final class WindHornItem extends Item {
    public static final double RADIUS = 6.0;
    public static final int COOLDOWN_TICKS = 200;
    public static final int SLOWNESS_TICKS = 60;

    public WindHornItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (player.getCooldowns().isOnCooldown(this)) {
            return InteractionResultHolder.fail(stack);
        }
        if (level.isClientSide) {
            // Movement, status effects, cooldown and feedback originate on the server.
            return InteractionResultHolder.sidedSuccess(stack, true);
        }
        if (!(level instanceof ServerLevel serverLevel)) {
            return InteractionResultHolder.fail(stack);
        }

        // Reserve the use before applying effects. Repeated packets cannot bypass it.
        player.getCooldowns().addCooldown(this,
                de.laugenbreze.dungeonsalive.item.gear.ArmorEffects.artifactCooldown(player, COOLDOWN_TICKS));
        for (LivingEntity target : serverLevel.getEntitiesOfClass(LivingEntity.class,
                player.getBoundingBox().inflate(RADIUS), entity -> canAffect(player, entity))) {
            double towardPlayerX = player.getX() - target.getX();
            double towardPlayerZ = player.getZ() - target.getZ();
            if (towardPlayerX * towardPlayerX + towardPlayerZ * towardPlayerZ < 0.0001) {
                Vec3 look = player.getLookAngle();
                towardPlayerX = -look.x;
                towardPlayerZ = -look.z;
                if (towardPlayerX * towardPlayerX + towardPlayerZ * towardPlayerZ < 0.0001) {
                    towardPlayerX = 1;
                    towardPlayerZ = 0;
                }
            }
            // LivingEntity#knockback subtracts this horizontal direction, pushing
            // the target away from the player. Vanilla knockback resistance applies.
            target.knockback(1.1, towardPlayerX, towardPlayerZ);
            target.hurtMarked = true;
            target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN,
                    SLOWNESS_TICKS, 0), player);
        }

        // Existing Minecraft feedback: the mod ships no copied audio assets.
        serverLevel.playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.RAID_HORN, SoundSource.PLAYERS, 0.65F, 1.35F);
        serverLevel.sendParticles(ParticleTypes.GUST,
                player.getX(), player.getY() + 0.9, player.getZ(), 5, 1.2, 0.2, 1.2, 0);
        serverLevel.sendParticles(ParticleTypes.CLOUD,
                player.getX(), player.getY() + 0.8, player.getZ(), 36, 1.5, 0.2, 1.5, 0.10);
        player.awardStat(Stats.ITEM_USED.get(this));
        return InteractionResultHolder.sidedSuccess(stack, false);
    }

    private static boolean canAffect(Player player, LivingEntity target) {
        if (!(target instanceof Enemy) || !target.isAlive() || target.isSpectator()
                || target.distanceToSqr(player) > RADIUS * RADIUS
                || target.isAlliedTo(player) || player.isAlliedTo(target)
                || !player.hasLineOfSight(target)) {
            return false;
        }
        // Protect owned/tamed mod creatures even when they also implement Enemy.
        if (target instanceof TamableAnimal tameable && tameable.isTame()) {
            return false;
        }
        return !(target instanceof OwnableEntity ownable && ownable.getOwnerUUID() != null);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context,
                               List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, context, tooltip, flag);
        tooltip.add(Component.translatable("item.dungeonsalive.wind_horn.tooltip")
                .withStyle(ChatFormatting.GRAY));
    }
}
