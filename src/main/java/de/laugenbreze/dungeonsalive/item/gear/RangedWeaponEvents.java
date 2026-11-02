package de.laugenbreze.dungeonsalive.item.gear;

import de.laugenbreze.dungeonsalive.entity.construct.ConstructService;
import java.util.Comparator;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.ProjectileImpactEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;

/** Special effects belong to the fired arrow, never the item held at impact time. */
public final class RangedWeaponEvents {
    static final String KIND = "DungeonsRangedKind";
    static final String HUNTER_PROCESSED = "DungeonsHunterProcessed";
    static final String REFUNDABLE = "DungeonsHunterRefundable";
    static final String EXPLOSION_SPENT = "DungeonsExplosionSpent";
    public static final double EXPLOSION_RADIUS = 2.5;
    public static final float EXPLOSION_DAMAGE = 6;
    private RangedWeaponEvents() {}

    public static void register() {
        NeoForge.EVENT_BUS.addListener(RangedWeaponEvents::afterDamage);
        NeoForge.EVENT_BUS.addListener(EventPriority.LOWEST, RangedWeaponEvents::impact);
    }

    static void prepareArrow(AbstractArrow arrow, ItemStack ammunition, ItemStack weapon, RangedWeaponKind kind) {
        if (arrow.level().isClientSide) return;
        var data = arrow.getPersistentData();
        if (data.contains(KIND)) return;
        data.putString(KIND, kind.id);
        arrow.setBaseDamage(arrow.getBaseDamage() * de.laugenbreze.dungeonsalive.merchant.GearUpgrade.damageMultiplier(weapon));
        if (kind == RangedWeaponKind.POWER_BOW) arrow.setBaseDamage(arrow.getBaseDamage() * 1.35);
        if (kind == RangedWeaponKind.HUNTERS_PROMISE) data.putBoolean(REFUNDABLE, ammunition.is(Items.ARROW));
    }

    private static boolean hostile(Player owner, LivingEntity victim) {
        return victim instanceof Enemy && victim != owner && !victim.isSpectator()
                && victim.level() == owner.level() && !victim.isAlliedTo(owner) && !owner.isAlliedTo(victim)
                && !(victim instanceof TamableAnimal pet && pet.isTame())
                && !(victim instanceof OwnableEntity pet && pet.getOwnerUUID() != null);
    }

    private static void afterDamage(LivingDamageEvent.Post event) {
        if (!(event.getEntity().level() instanceof ServerLevel server) || event.getNewDamage() <= 0
                || !(event.getSource().getDirectEntity() instanceof AbstractArrow arrow)
                || !(arrow.getOwner() instanceof Player owner) || !owner.isAlive()
                || !hostile(owner, event.getEntity())) return;
        var data = arrow.getPersistentData();
        if (!data.getString(KIND).equals(RangedWeaponKind.HUNTERS_PROMISE.id) || data.getBoolean(HUNTER_PROCESSED)) return;
        data.putBoolean(HUNTER_PROCESSED, true);
        LivingEntity victim = event.getEntity();
        if (victim.isAlive() && owner.distanceToSqr(victim) <= 24 * 24) {
            server.getEntitiesOfClass(Wolf.class, owner.getBoundingBox().inflate(16), wolf -> wolf.isAlive()
                            && !wolf.isNoAi() && wolf.isTame() && owner.getUUID().equals(wolf.getOwnerUUID())
                            && !wolf.isOrderedToSit() && wolf.distanceToSqr(owner) <= 16 * 16
                            && wolf.distanceToSqr(victim) <= 24 * 24 && wolf.canAttack(victim)
                            && ConstructService.clearLine(server, wolf.getEyePosition(), victim.getEyePosition(), wolf))
                    .stream().sorted(Comparator.comparingDouble(wolf -> wolf.distanceToSqr(victim)))
                    .limit(4).forEach(wolf -> wolf.setTarget(victim));
        }
        if (data.getBoolean(REFUNDABLE) && arrow.pickup == AbstractArrow.Pickup.ALLOWED
                && !owner.hasInfiniteMaterials() && owner.getRandom().nextInt(5) == 0) {
            // Infinity/creative/extra Multishot arrows are not refundable. A
            // piercing arrow cannot be refunded and also picked up afterward.
            arrow.pickup = AbstractArrow.Pickup.DISALLOWED;
            ItemStack refund = new ItemStack(Items.ARROW);
            if (!owner.getInventory().add(refund)) owner.drop(refund, false);
        }
    }

    private static void impact(ProjectileImpactEvent event) {
        if (event.isCanceled() || !(event.getProjectile() instanceof AbstractArrow arrow)
                || !(arrow.level() instanceof ServerLevel server) || !(arrow.getOwner() instanceof Player owner)
                || !owner.isAlive() || owner.level() != server || event.getRayTraceResult().getType() == HitResult.Type.MISS) return;
        var data = arrow.getPersistentData();
        if (!data.getString(KIND).equals(RangedWeaponKind.EXPLODING_CROSSBOW.id) || data.getBoolean(EXPLOSION_SPENT)) return;
        data.putBoolean(EXPLOSION_SPENT, true);
        HitResult hit = event.getRayTraceResult();
        Entity direct = hit instanceof EntityHitResult entityHit ? entityHit.getEntity() : null;
        if (direct instanceof LivingEntity living && !hostile(owner, living)) return;
        // 1.21.1's arrow ProjectileUtil overload returns EntityHitResult(entity),
        // whose location is the entity's feet, not the segment intersection.
        // Reconstruct the same inflated-AABB contact used by vanilla; otherwise
        // an upward shot puts its blast below the floor and falsely hits cover.
        Vec3 contact = direct == null ? hit.getLocation() : direct.getBoundingBox().inflate((double) .3F)
                .clip(arrow.position(), arrow.position().add(arrow.getDeltaMovement())).orElse(arrow.position());
        Vec3 center = contact.subtract(arrow.getDeltaMovement().normalize().scale(.05));
        AABB area = new AABB(center, center).inflate(EXPLOSION_RADIUS);
        if (!ConstructService.loaded(server, area.inflate(1))) return;
        // The direct hit retains vanilla arrow/enchantment damage. Damage it here
        // first and vanilla's hurt immunity could swallow that real arrow hit.
        server.getEntitiesOfClass(LivingEntity.class, area, victim -> victim != direct && victim.isAlive()
                        && hostile(owner, victim) && victim.distanceToSqr(center) <= EXPLOSION_RADIUS * EXPLOSION_RADIUS
                        && ConstructService.clearLine(server, center, victim.getBoundingBox().getCenter(), owner))
                .stream().sorted(Comparator.comparingDouble(victim -> victim.distanceToSqr(center))).limit(16)
                .forEach(victim -> victim.hurt(owner.damageSources().indirectMagic(arrow, owner), EXPLOSION_DAMAGE));
        server.sendParticles(ParticleTypes.EXPLOSION, center.x, center.y, center.z, 1, 0, 0, 0, 0);
        server.playSound(null, center.x, center.y, center.z, SoundEvents.GENERIC_EXPLODE, SoundSource.PLAYERS, .65F, 1.2F);
    }
}
