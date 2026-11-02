package de.laugenbreze.dungeonsalive.entity;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.AABB;

/** Slow, dodgeable magic projectile. It never explodes, ignites or modifies blocks. */
public final class SoulBolt extends Projectile {
    private float damage = 4;
    private int remainingTicks = 100;
    public SoulBolt(EntityType<? extends SoulBolt> type, Level level) { super(type, level); }
    public SoulBolt(Level level, DungeonMob owner, Vec3 direction, float damage) {
        this(ModEntities.SOUL_BOLT.get(), level);
        setOwner(owner);
        setPos(owner.getX(), owner.getY(.65), owner.getZ());
        this.damage = Math.clamp(damage, 0, 64);
        shoot(direction.x, direction.y, direction.z, .65f, 0);
    }
    @Override protected void defineSynchedData(SynchedEntityData.Builder builder) {}
    @Override public void tick() {
        super.tick();
        if (!level().isClientSide) {
            if (--remainingTicks <= 0 || !(getOwner() instanceof DungeonMob owner) || !owner.isAlive()) { discard(); return; }
            AABB swept = getBoundingBox().expandTowards(getDeltaMovement()).inflate(1);
            if (!level().hasChunksAt(BlockPos.containing(swept.minX, swept.minY, swept.minZ), BlockPos.containing(swept.maxX, swept.maxY, swept.maxZ))) {
                discard(); return;
            }
        }
        HitResult hit = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
        if (hit.getType() != HitResult.Type.MISS && !level().isClientSide) {
            if (hit instanceof EntityHitResult entityHit && entityHit.getEntity() instanceof LivingEntity living) {
                living.hurt(damageSources().indirectMagic(this, getOwner()), damage);
            }
            ((ServerLevel) level()).sendParticles(ParticleTypes.SOUL, hit.getLocation().x, hit.getLocation().y, hit.getLocation().z, 6, .1, .1, .1, .02);
            discard();
            return;
        }
        Vec3 next = position().add(getDeltaMovement());
        setPos(next.x, next.y, next.z);
        updateRotation();
        if (level().isClientSide) level().addParticle(ParticleTypes.SOUL_FIRE_FLAME, getX(), getY(), getZ(), 0, 0, 0);
    }
    @Override protected boolean canHitEntity(Entity entity) {
        if (!super.canHitEntity(entity) || entity == getOwner() || entity instanceof DungeonMob) return false;
        if (entity instanceof de.laugenbreze.dungeonsalive.entity.construct.DungeonConstruct construct)
            return getOwner() instanceof DungeonMob owner && owner.isAlive() && construct.blocksMovement();
        return getOwner() instanceof DungeonMob owner && owner.isAlive() && entity instanceof LivingEntity living && owner.isOpponent(living);
    }
    @Override protected void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putFloat("Damage", damage); tag.putInt("RemainingTicks", remainingTicks);
    }
    @Override protected void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        damage = Math.clamp(tag.getFloat("Damage"), 0, 64);
        remainingTicks = Math.clamp(tag.getInt("RemainingTicks"), 0, 100);
    }
}
