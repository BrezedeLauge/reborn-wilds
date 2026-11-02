package de.laugenbreze.dungeonsalive.item.gear.enchantment;

import de.laugenbreze.dungeonsalive.entity.construct.ConstructService;
import java.util.Comparator;
import java.util.List;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;

/** Confirmed direct melee drives effects; secondary magic cannot recursively trigger this handler. */
public final class GearEnchantmentEvents {
    private static final String RADIANCE_UNTIL = "DungeonsRadianceUntil", CHAINS_UNTIL = "DungeonsChainsUntil",
            SWIRLING_UNTIL = "DungeonsSwirlingUntil", LEECHING_SPENT = "DungeonsLeechingSpent";
    private GearEnchantmentEvents() {}
    public static void register() {
        NeoForge.EVENT_BUS.addListener(GearEnchantmentEvents::incoming);
        NeoForge.EVENT_BUS.addListener(GearEnchantmentEvents::applied);
        NeoForge.EVENT_BUS.addListener(EventPriority.LOWEST, GearEnchantmentEvents::death);
    }
    private static boolean hostile(Player owner, LivingEntity victim) {
        return victim instanceof Enemy && !victim.isSpectator() && victim.level() == owner.level()
                && !victim.isAlliedTo(owner) && !owner.isAlliedTo(victim)
                && !(victim instanceof TamableAnimal pet && pet.isTame())
                && !(victim instanceof OwnableEntity pet && pet.getOwnerUUID() != null);
    }
    private static boolean directMelee(Player owner, LivingEntity victim, DamageSource source) {
        return owner.isAlive() && !owner.isSpectator() && hostile(owner, victim)
                && source.is(DamageTypes.PLAYER_ATTACK) && source.getDirectEntity() == owner;
    }
    private static int rank(Player owner, ServerLevel level, GearEnchantmentKind kind) {
        return GearEnchantments.effectiveLevel(owner.getMainHandItem(), level, kind);
    }
    private static void incoming(LivingIncomingDamageEvent event) {
        if (event.isCanceled() || !(event.getEntity().level() instanceof ServerLevel server)
                || !(event.getSource().getEntity() instanceof Player owner)
                || !directMelee(owner, event.getEntity(), event.getSource())
                || !Float.isFinite(event.getAmount()) || event.getAmount() <= 0) return;
        int committed = rank(owner, server, GearEnchantmentKind.COMMITTED), critical = rank(owner, server, GearEnchantmentKind.CRITICAL_HIT);
        float multiplier = GearEnchantmentRules.committedMultiplier(committed, event.getEntity().getHealth(), event.getEntity().getMaxHealth());
        if (critical > 0) multiplier *= GearEnchantmentRules.criticalMultiplier(critical, owner.getRandom().nextDouble());
        event.setAmount((float) Math.min(Float.MAX_VALUE, (double) event.getAmount() * multiplier));
    }
    private static void applied(LivingDamageEvent.Post event) {
        if (!(event.getEntity().level() instanceof ServerLevel server) || !Float.isFinite(event.getNewDamage()) || event.getNewDamage() <= 0
                || !(event.getSource().getEntity() instanceof Player owner) || !directMelee(owner, event.getEntity(), event.getSource())) return;
        LivingEntity victim = event.getEntity();
        long now = server.getGameTime();
        int radiance = rank(owner, server, GearEnchantmentKind.RADIANCE);
        if (radiance > 0 && owner.distanceToSqr(victim) <= 16 && ready(owner, RADIANCE_UNTIL, now, GearEnchantmentRules.RADIANCE_PAUSE)
                && owner.getRandom().nextDouble() < .2) {
            owner.getPersistentData().putLong(RADIANCE_UNTIL, now + GearEnchantmentRules.RADIANCE_PAUSE);
            owner.heal(GearEnchantmentRules.radianceHealing(radiance));
            server.sendParticles(ParticleTypes.HEART, victim.getX(), victim.getY() + 1, victim.getZ(), 5, .7, .4, .7, 0);
        }
        int chains = rank(owner, server, GearEnchantmentKind.CHAINS), swirling = rank(owner, server, GearEnchantmentKind.SWIRLING);
        boolean chainReady = chains > 0 && ready(owner, CHAINS_UNTIL, now, GearEnchantmentRules.CHAINS_PAUSE);
        boolean swirlReady = swirling > 0 && ready(owner, SWIRLING_UNTIL, now, GearEnchantmentRules.SWIRLING_PAUSE);
        if (!chainReady && !swirlReady) return;
        if (chainReady) owner.getPersistentData().putLong(CHAINS_UNTIL, now + GearEnchantmentRules.CHAINS_PAUSE);
        if (swirlReady) owner.getPersistentData().putLong(SWIRLING_UNTIL, now + GearEnchantmentRules.SWIRLING_PAUSE);
        for (LivingEntity nearby : nearby(owner, victim)) {
            if (chainReady) nearby.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN,
                    GearEnchantmentRules.chainsTicks(chains), 3), owner);
            if (swirlReady && nearby != victim) nearby.hurt(owner.damageSources().indirectMagic(owner, owner), GearEnchantmentRules.swirlingDamage(swirling));
        }
        if (swirlReady) server.sendParticles(ParticleTypes.SWEEP_ATTACK, victim.getX(), victim.getY() + .8, victim.getZ(), 4, .8, .2, .8, 0);
    }
    private static boolean ready(Player owner, String key, long now, int pause) {
        return GearEnchantmentRules.cooldownReady(now, owner.getPersistentData().getLong(key), pause);
    }
    private static List<LivingEntity> nearby(Player owner, LivingEntity direct) {
        Vec3 center = direct.getBoundingBox().getCenter();
        AABB area = new AABB(center, center).inflate(GearEnchantmentRules.AREA_RADIUS);
        if (!ConstructService.loaded(owner.level(), area.inflate(1))) return List.of();
        return owner.level().getEntitiesOfClass(LivingEntity.class, area, target -> target.isAlive() && hostile(owner, target)
                        && target.getBoundingBox().getCenter().distanceToSqr(center) <= GearEnchantmentRules.AREA_RADIUS * GearEnchantmentRules.AREA_RADIUS
                        && ConstructService.clearLine(owner.level(), center, target.getBoundingBox().getCenter(), owner))
                .stream().sorted(Comparator.comparingDouble(target -> target.distanceToSqr(center)))
                .limit(GearEnchantmentRules.AREA_CAP).toList();
    }
    private static void death(LivingDeathEvent event) {
        if (event.isCanceled() || !(event.getEntity().level() instanceof ServerLevel server)
                || !(event.getSource().getEntity() instanceof Player owner) || !directMelee(owner, event.getEntity(), event.getSource())
                || event.getEntity().getPersistentData().getBoolean(LEECHING_SPENT)) return;
        int rank = rank(owner, server, GearEnchantmentKind.LEECHING);
        float heal = GearEnchantmentRules.leechingHealing(rank, event.getEntity().getMaxHealth());
        if (heal <= 0) return;
        event.getEntity().getPersistentData().putBoolean(LEECHING_SPENT, true);
        owner.heal(heal);
        server.sendParticles(ParticleTypes.HEART, owner.getX(), owner.getY() + 1.6, owner.getZ(), 3, .3, .2, .3, 0);
    }
}
