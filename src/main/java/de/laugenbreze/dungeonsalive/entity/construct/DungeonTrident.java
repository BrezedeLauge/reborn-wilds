package de.laugenbreze.dungeonsalive.entity.construct;

import de.laugenbreze.dungeonsalive.entity.DungeonMob;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

/** A real trident flight/renderer, with hostile-spell filtering and no obtainable projectile item. */
public final class DungeonTrident extends ThrownTrident {
    private long expiresAt;
    private float damage = 7;
    public DungeonTrident(EntityType<? extends DungeonTrident> type, Level level) {
        super(type, level);
        pickup = AbstractArrow.Pickup.DISALLOWED;
    }
    @Nullable public static DungeonTrident launch(DungeonMob owner, Vec3 lockedAim, float damage) {
        if (owner.level().isClientSide || !owner.isAlive() || owner.constructReservationCount() >= ConstructService.OWNER_CAP
                || !Double.isFinite(lockedAim.x) || !Double.isFinite(lockedAim.y) || !Double.isFinite(lockedAim.z)
                || owner.distanceToSqr(lockedAim) > 24 * 24) return null;
        DungeonTrident projectile = ModConstructs.TRIDENT.get().create(owner.level());
        if (projectile == null) return null;
        projectile.setOwner(owner);
        projectile.setPos(owner.getX(), owner.getEyeY() - .1, owner.getZ());
        projectile.expiresAt = owner.level().getGameTime() + 120;
        projectile.damage = Float.isFinite(damage) ? Math.clamp(damage, 0, 24) : 0;
        Vec3 aim = lockedAim.subtract(projectile.position());
        projectile.shoot(aim.x, aim.y + aim.horizontalDistance() * .08, aim.z, 1.35f, 0);
        if (!ConstructService.loaded(owner.level(), projectile.getBoundingBox().expandTowards(projectile.getDeltaMovement()).inflate(1))
                || !owner.reserveConstruct(projectile.getUUID(), projectile.expiresAt)) return null;
        if (!owner.level().addFreshEntity(projectile)) { owner.releaseConstruct(projectile.getUUID()); return null; }
        owner.playSound(SoundEvents.DROWNED_SHOOT, 1, 1);
        return projectile;
    }
    @Override public void tick() {
        if (!level().isClientSide && (level().getGameTime() >= expiresAt || !(getOwner() instanceof DungeonMob owner) || !owner.isAlive()
                || distanceToSqr(owner) > 48 * 48 || !ConstructService.loaded(level(), getBoundingBox().expandTowards(getDeltaMovement()).inflate(1)))) {
            discard(); return;
        }
        super.tick();
    }
    @Override protected boolean canHitEntity(Entity entity) {
        if (!super.canHitEntity(entity) || !(getOwner() instanceof DungeonMob owner) || !owner.isAlive()) return false;
        if (entity instanceof DungeonConstruct wall) return wall.blocksMovement();
        return entity instanceof LivingEntity target && ConstructService.mayHarm(owner, target);
    }
    @Override protected void onHitEntity(EntityHitResult hit) {
        if (level().isClientSide) return;
        if (getOwner() instanceof DungeonMob owner && hit.getEntity() instanceof LivingEntity victim
                && ConstructService.mayHarm(owner, victim)) victim.hurt(damageSources().trident(this, owner), damage);
        playSound(SoundEvents.TRIDENT_HIT, 1, 1);
        discard();
    }
    @Override protected boolean tryPickup(Player player) { return false; }
    @Override public void playerTouch(Player player) {}
    @Override public void remove(RemovalReason reason) {
        if (reason.shouldDestroy() && getOwner() instanceof DungeonMob owner) owner.releaseConstruct(getUUID());
        super.remove(reason);
    }
    @Override public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putLong("DungeonsTridentExpires", expiresAt); tag.putFloat("DungeonsTridentDamage", damage);
    }
    @Override public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        expiresAt = Math.min(tag.getLong("DungeonsTridentExpires"), level().getGameTime() + 120);
        damage = Float.isFinite(tag.getFloat("DungeonsTridentDamage")) ? Math.clamp(tag.getFloat("DungeonsTridentDamage"), 0, 24) : 0;
        pickup = AbstractArrow.Pickup.DISALLOWED;
    }
}
