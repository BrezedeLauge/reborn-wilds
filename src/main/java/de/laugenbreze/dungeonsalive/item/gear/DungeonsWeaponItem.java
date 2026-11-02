package de.laugenbreze.dungeonsalive.item.gear;

import net.minecraft.ChatFormatting;
import de.laugenbreze.dungeonsalive.DungeonsAlive;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.ItemAbility;

import java.util.List;

/** Ordinary Minecraft melee with restrained signature effects and actual source art. */
public final class DungeonsWeaponItem extends SwordItem {
    private static final ResourceLocation REACH = ResourceLocation.fromNamespaceAndPath(DungeonsAlive.MOD_ID, "weapon_entity_reach");
    private static final ResourceLocation KNOCKBACK = ResourceLocation.fromNamespaceAndPath(DungeonsAlive.MOD_ID, "weapon_knockback");
    private final WeaponKind kind;
    public DungeonsWeaponItem(WeaponKind kind, Properties properties) {
        super(Tiers.DIAMOND, properties.durability(kind.durability)
                .attributes(attributes(kind)));
        this.kind = kind;
    }
    public WeaponKind kind() { return kind; }

    private static ItemAttributeModifiers attributes(WeaponKind kind) {
        var result = SwordItem.createAttributes(Tiers.DIAMOND, kind.damage - 4, kind.attacksPerSecond - 4);
        // These are ordinary main-hand components: vanilla synchronizes them and
        // removes them on a hand/slot change. Block reach is intentionally unchanged.
        if (kind.family.entityReachBonus > 0) result = result.withModifierAdded(Attributes.ENTITY_INTERACTION_RANGE,
                new AttributeModifier(REACH, kind.family.entityReachBonus, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND);
        if (kind.family.knockbackBonus > 0) result = result.withModifierAdded(Attributes.ATTACK_KNOCKBACK,
                new AttributeModifier(KNOCKBACK, kind.family.knockbackBonus, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND);
        return result;
    }

    @Override public boolean canPerformAction(ItemStack stack, ItemAbility ability) {
        return ability == ItemAbilities.SWORD_SWEEP ? kind.family.sweeping : super.canPerformAction(stack, ability);
    }

    @Override public void postHurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        super.postHurtEnemy(stack, target, attacker);
        if (!(attacker instanceof Player player) || !(attacker.level() instanceof ServerLevel server)
                || !(target instanceof net.minecraft.world.entity.monster.Enemy)
                || target.isAlliedTo(player) || player.isAlliedTo(target)
                || (target instanceof net.minecraft.world.entity.OwnableEntity pet && pet.getOwnerUUID() != null)
                || player.getCooldowns().isOnCooldown(this)) return;
        switch (kind) {
            case FROST_SCYTHE -> {
                if (ArtifactCombat.hostileTo(player, target)) target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 80, 1), player);
                player.getCooldowns().addCooldown(this, 30);
                server.sendParticles(ParticleTypes.SNOWFLAKE, target.getX(), target.getY() + 1, target.getZ(), 12, .4, .6, .4, .02);
            }
            case SUNS_GRACE -> {
                player.heal(2);
                player.getCooldowns().addCooldown(this, 100);
                server.sendParticles(ParticleTypes.HEART, player.getX(), player.getY() + 1.6, player.getZ(), 3, .3, .2, .3, 0);
            }
            case STORMLANDER, WHIRLWIND -> {
                player.getCooldowns().addCooldown(this, kind == WeaponKind.STORMLANDER ? 80 : 50);
                for (LivingEntity nearby : ArtifactCombat.nearby(player, target.position(), kind == WeaponKind.STORMLANDER ? 3 : 2.5)) {
                    if (nearby != target && player.hasLineOfSight(nearby))
                        nearby.hurt(player.damageSources().indirectMagic(player, player), kind == WeaponKind.STORMLANDER ? 4 : 3);
                }
                server.sendParticles(kind == WeaponKind.STORMLANDER ? ParticleTypes.ELECTRIC_SPARK : ParticleTypes.SWEEP_ATTACK,
                        target.getX(), target.getY() + 1, target.getZ(), 12, 1, .5, 1, .05);
            }
            default -> { }
        }
    }

    @Override public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> lines, TooltipFlag flag) {
        lines.add(Component.translatable("item.dungeonsalive." + kind.id + ".tooltip").withStyle(ChatFormatting.GRAY));
    }
}
