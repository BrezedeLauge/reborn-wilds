package de.laugenbreze.dungeonsalive.item.gear;

import de.laugenbreze.dungeonsalive.DungeonsAlive;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingEquipmentChangeEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import org.jetbrains.annotations.Nullable;

/** Full-set rules use equipped server items; copied display data never grants a bonus. */
public final class ArmorEffects {
    public static final String LIFESTEAL_UNTIL = "DungeonsAliveSpiderHealUntil";
    public static final int LIFESTEAL_PAUSE = 10;
    private static final ResourceLocation SPIDER_SPEED = ResourceLocation.fromNamespaceAndPath(DungeonsAlive.MOD_ID, "spider_set_attack_speed");
    private ArmorEffects() {}
    static void register() {
        NeoForge.EVENT_BUS.addListener(ArmorEffects::equipmentChanged);
        NeoForge.EVENT_BUS.addListener(ArmorEffects::incomingDamage);
        NeoForge.EVENT_BUS.addListener(ArmorEffects::damageApplied);
    }
    @Nullable public static ArmorSetKind fullSet(LivingEntity wearer) {
        ArmorSetKind found = null;
        for (var type : ArmorContent.PIECES) {
            var stack = wearer.getItemBySlot(type.getSlot());
            if (!(stack.getItem() instanceof DungeonsArmorItem armor) || armor.getType() != type) return null;
            if (found != null && armor.kind() != found) return null;
            found = armor.kind();
        }
        return found;
    }
    public static int artifactCooldown(Player player, int originalTicks) {
        var set = fullSet(player);
        return set == null ? Math.max(0, originalTicks) : set.artifactCooldown(originalTicks);
    }
    private static void equipmentChanged(LivingEquipmentChangeEvent event) {
        if (event.getEntity() instanceof Player player && !player.level().isClientSide) refreshSetAttributes(player);
    }
    public static void refreshSetAttributes(Player player) {
        if (player.level().isClientSide) return;
        var speed = player.getAttribute(Attributes.ATTACK_SPEED);
        if (speed == null) return;
        var set = fullSet(player);
        boolean active = player.isAlive() && set == ArmorSetKind.SPIDER;
        if (!active) speed.removeModifier(SPIDER_SPEED);
        else if (speed.getModifier(SPIDER_SPEED) == null)
            speed.addTransientModifier(new AttributeModifier(SPIDER_SPEED, set.attackSpeedBonus, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
    }
    private static boolean eligibleMelee(Player player, LivingEntity victim, DamageSource source) {
        return player.isAlive() && !player.isSpectator() && player.level() == victim.level()
                && victim instanceof Enemy && !victim.isAlliedTo(player) && !player.isAlliedTo(victim)
                && !(victim instanceof OwnableEntity pet && pet.getOwnerUUID() != null)
                && source.is(DamageTypes.PLAYER_ATTACK) && source.getDirectEntity() == player;
    }
    private static void incomingDamage(LivingIncomingDamageEvent event) {
        if (event.isCanceled() || event.getEntity().level().isClientSide || !Float.isFinite(event.getAmount()) || event.getAmount() <= 0
                || !(event.getSource().getEntity() instanceof Player player) || !eligibleMelee(player, event.getEntity(), event.getSource())) return;
        var set = fullSet(player);
        if (set != null) event.setAmount(event.getAmount() * set.meleeMultiplier);
    }
    private static void damageApplied(LivingDamageEvent.Post event) {
        if (event.getEntity().level().isClientSide || !(event.getSource().getEntity() instanceof Player player)
                || !eligibleMelee(player, event.getEntity(), event.getSource()) || fullSet(player) != ArmorSetKind.SPIDER) return;
        float healing = ArmorSetKind.SPIDER.lifeSteal(event.getNewDamage());
        long now = player.level().getGameTime();
        long until = player.getPersistentData().getLong(LIFESTEAL_UNTIL);
        if (healing <= 0 || now < until && until - now <= LIFESTEAL_PAUSE) return;
        player.getPersistentData().putLong(LIFESTEAL_UNTIL, now + LIFESTEAL_PAUSE);
        player.heal(healing);
    }
}
