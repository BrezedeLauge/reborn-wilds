package de.laugenbreze.dungeonsalive.entity.construct;

import de.laugenbreze.dungeonsalive.entity.DungeonMob;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

/** A destructible, finite-lived spell object. No wandering AI, loot, player-block mutation or ownerless damage. */
public final class DungeonConstruct extends Mob {
    public enum Phase { FORMING, ACTIVE, FALLING, IMPACT, DECAY }
    private static final EntityDataAccessor<Integer> PHASE = SynchedEntityData.defineId(DungeonConstruct.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Long> PHASE_STARTED = SynchedEntityData.defineId(DungeonConstruct.class, EntityDataSerializers.LONG);
    private final ConstructKind kind;
    @Nullable private UUID ownerId, targetId;
    private long createdAt, expiresAt;
    private float damage;
    private boolean configured, released, snareAccepted, restored;

    public DungeonConstruct(EntityType<? extends DungeonConstruct> type, Level level, ConstructKind kind) {
        super(type, level);
        this.kind = kind;
        this.xpReward = 0;
        setNoAi(true);
        setNoGravity(true);
        setPersistenceRequired();
        setCanPickUpLoot(false);
    }
    public static AttributeSupplier.Builder createAttributes(ConstructKind kind) {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, kind.health()).add(Attributes.MOVEMENT_SPEED, 0)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1).add(Attributes.ARMOR, 0);
    }
    @Override protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(PHASE, Phase.FORMING.ordinal()); builder.define(PHASE_STARTED, 0L);
    }
    @Override protected void registerGoals() {}
    void initialize(DungeonMob owner, LivingEntity target, Vec3 pos, float damage) {
        if (level().isClientSide) throw new IllegalStateException("Construct initialization belongs to the server");
        ownerId = owner.getUUID(); targetId = target.getUUID();
        this.damage = Float.isFinite(damage) ? Math.clamp(damage, 0, 24) : 0;
        createdAt = level().getGameTime(); expiresAt = createdAt + kind.maximumLifetimeTicks();
        configured = true; released = false; snareAccepted = false;
        moveTo(pos.x, pos.y, pos.z, owner.getYRot(), 0);
        setPhase(Phase.FORMING);
    }
    public ConstructKind kind() { return kind; }
    public Phase phase() {
        int index = entityData.get(PHASE);
        return index >= 0 && index < Phase.values().length ? Phase.values()[index] : Phase.DECAY;
    }
    public float phaseTick(float partialTick) {
        return Math.clamp((float)(level().getGameTime() - entityData.get(PHASE_STARTED)) + partialTick, 0, phaseDurationTicks());
    }
    public int phaseDurationTicks() {
        return switch (phase()) {
            case FORMING -> kind.formTicks();
            case ACTIVE -> kind.activeTicks();
            case FALLING -> 20;
            case IMPACT, DECAY -> kind.decayTicks();
        };
    }
    @Nullable public String visualClip() {
        return switch (kind) {
            case GEOMANCER_WALL, GEOMANCER_BOMB -> switch (phase()) {
                case FORMING -> "GEOMANCER_PILLAR_APPEAR";
                case DECAY -> "GEOMANCER_PILLAR_DISAPPEAR";
                default -> null;
            };
            case ENTANGLE_VINE -> switch (phase()) {
                case FORMING -> "VINE_TRAP_SPAWN";
                case DECAY -> "VINE_TRAP_DECAY";
                default -> null;
            };
            case ICE_CHUNK -> switch (phase()) {
                case FORMING -> "ICE_CHUNK_FORM";
                case FALLING -> "ICE_CHUNK_FALL";
                case IMPACT, DECAY -> "ICE_CHUNK_LAND";
                default -> null;
            };
        };
    }
    public long expiresAt() { return expiresAt; }
    public boolean blocksMovement() { return kind == ConstructKind.GEOMANCER_WALL && phase() == Phase.ACTIVE && isAlive(); }
    @Nullable public DungeonMob owner() {
        return level() instanceof ServerLevel server && ownerId != null && server.getEntity(ownerId) instanceof DungeonMob mob ? mob : null;
    }
    @Nullable public LivingEntity intendedTarget() {
        return level() instanceof ServerLevel server && targetId != null && server.getEntity(targetId) instanceof LivingEntity target ? target : null;
    }
    private void setPhase(Phase phase) {
        entityData.set(PHASE, phase.ordinal()); entityData.set(PHASE_STARTED, level().getGameTime());
    }
    private void decay() {
        if (phase() == Phase.DECAY || phase() == Phase.IMPACT) return;
        released = true; setPhase(Phase.DECAY); setDeltaMovement(Vec3.ZERO);
        expiresAt = Math.min(expiresAt, level().getGameTime() + kind.decayTicks());
    }
    @Override public void tick() {
        if (!(level() instanceof ServerLevel server)) { super.tick(); return; }
        DungeonMob caster = owner();
        LivingEntity target = intendedTarget();
        long now = server.getGameTime();
        if (!configured || now >= expiresAt || caster == null || !caster.isAlive()
                || target == null || !ConstructService.mayHarm(caster, target) || distanceToSqr(caster) > 48 * 48
                || !ConstructService.loaded(server, getBoundingBox().inflate(1))) { discard(); return; }
        if (restored) {
            restored = false;
            // Missed armed/falling hazards never replay their hit immediately after a chunk load.
            if (kind == ConstructKind.GEOMANCER_BOMB && now >= createdAt + kind.formTicks() + kind.activeTicks()
                    || kind == ConstructKind.ICE_CHUNK && phase() == Phase.FALLING) decay();
        }
        super.tick();
        if (isRemoved()) return;
        if (!isAlive()) { decay(); return; }
        if (phase() == Phase.FORMING && phaseTick(0) >= kind.formTicks()) {
            if ((kind == ConstructKind.GEOMANCER_WALL || kind == ConstructKind.GEOMANCER_BOMB)
                    && ConstructService.occupied(server, getBoundingBox(), this)) { decay(); return; }
            setPhase(kind == ConstructKind.ICE_CHUNK ? Phase.FALLING : Phase.ACTIVE);
            if (kind == ConstructKind.ENTANGLE_VINE && !released) {
                released = true;
                snareAccepted = inSnareRange(target) && target.hurt(damageSources().indirectMagic(this, caster), damage * .6f);
            }
        }
        switch (phase()) {
            case FORMING -> {
                if (tickCount % 5 == 0) server.sendParticles(kind == ConstructKind.ICE_CHUNK ? ParticleTypes.SNOWFLAKE : ParticleTypes.ENCHANT,
                        getX(), getY(.4), getZ(), 5, getBbWidth() * .4, .4, getBbWidth() * .4, .02);
            }
            case ACTIVE -> {
                if (kind == ConstructKind.GEOMANCER_BOMB) {
                    if (tickCount % 4 == 0) server.sendParticles(ParticleTypes.SMOKE, getX(), getY(.9), getZ(), 4, .2, .2, .2, .02);
                    if (phaseTick(0) >= kind.activeTicks() && !released) {
                        released = true;
                        pulse(caster, 3.2, 1.2f, null);
                        server.sendParticles(ParticleTypes.EXPLOSION, getX(), getY(.4), getZ(), 1, 0, 0, 0, 0);
                        playSound(SoundEvents.GENERIC_EXPLODE.value(), .9f, 1.2f);
                        decay();
                    }
                } else if (kind == ConstructKind.ENTANGLE_VINE && snareAccepted && tickCount % 5 == 0 && inSnareRange(target)) {
                    target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 12, 3), caster);
                }
                if (now >= expiresAt - kind.decayTicks()) decay();
            }
            case FALLING -> {
                Vec3 fall = new Vec3(0, Math.max(-.8, getDeltaMovement().y - .06), 0);
                if (!ConstructService.loaded(server, getBoundingBox().expandTowards(fall).inflate(1))) { discard(); return; }
                setDeltaMovement(fall); move(MoverType.SELF, fall); hasImpulse = true;
                if (onGround() || verticalCollision) {
                    if (!released) {
                        released = true;
                        pulse(caster, 2.4, 1.15f, new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 80, 1));
                        server.sendParticles(ParticleTypes.SNOWFLAKE, getX(), getY(.3), getZ(), 45, 1.2, .6, 1.2, .06);
                        playSound(SoundEvents.GLASS_BREAK, .9f, .7f);
                    }
                    setDeltaMovement(Vec3.ZERO); setPhase(Phase.IMPACT);
                    expiresAt = Math.min(expiresAt, now + kind.decayTicks());
                }
            }
            default -> { }
        }
    }
    private boolean inSnareRange(LivingEntity target) {
        return distanceToSqr(target) <= 1.65 * 1.65 && Math.abs(target.getY() - getY()) < 1.5
                && ConstructService.clearLine(level(), position().add(0, .4, 0), target.getEyePosition(), this);
    }
    private void pulse(DungeonMob caster, double radius, float multiplier, @Nullable MobEffectInstance effect) {
        Vec3 center = position().add(0, .4, 0);
        for (LivingEntity victim : level().getEntitiesOfClass(LivingEntity.class, getBoundingBox().inflate(radius, 1, radius),
                candidate -> ConstructService.mayHarm(caster, candidate))) {
            double dx = victim.getX() - getX(), dz = victim.getZ() - getZ();
            if (dx * dx + dz * dz > radius * radius || Math.abs(victim.getY() - getY()) > 2.5
                    || !ConstructService.clearLine(level(), center, victim.getEyePosition(), this)) continue;
            if (victim.hurt(damageSources().indirectMagic(this, caster), damage * multiplier)) {
                victim.knockback(.65, -dx, -dz);
                if (effect != null) victim.addEffect(new MobEffectInstance(effect), caster);
            }
        }
    }
    @Override public boolean hurt(DamageSource source, float amount) {
        if (phase() == Phase.DECAY || phase() == Phase.IMPACT) return false;
        Entity attacker = source.getEntity();
        if (!(attacker instanceof Player) && !(attacker instanceof IronGolem) && !source.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) return false;
        return super.hurt(source, amount);
    }
    @Override public void die(DamageSource source) { super.die(source); if (!level().isClientSide) decay(); }
    // Do not enter the generic death-animation path: the synchronized disappearance clip owns this interval.
    @Override protected void tickDeath() { deathTime = 0; }
    @Override public boolean canBeCollidedWith() { return blocksMovement(); }
    @Override public boolean isPushable() { return false; }
    @Override public void push(double x, double y, double z) {}
    @Override protected void pushEntities() {}
    @Override public boolean isPushedByFluid() { return false; }
    @Override protected boolean isAffectedByFluids() { return false; }
    @Override public boolean canDrownInFluidType(net.neoforged.neoforge.fluids.FluidType type) { return false; }
    @Override public boolean canFreeze() { return false; }
    @Override public boolean canBeLeashed() { return false; }
    @Override protected boolean canRide(Entity vehicle) { return false; }
    @Override public boolean canUsePortal(boolean allowPassengers) { return false; }
    @Override public boolean canBeAffected(MobEffectInstance effect) { return false; }
    @Override public void travel(Vec3 input) { if (kind != ConstructKind.ICE_CHUNK || phase() != Phase.FALLING) setDeltaMovement(Vec3.ZERO); }
    @Override protected boolean shouldDropLoot() { return false; }
    @Override public boolean shouldDropExperience() { return false; }
    @Override public void remove(RemovalReason reason) {
        DungeonMob caster = owner();
        if (reason.shouldDestroy() && caster != null) caster.releaseConstruct(getUUID());
        super.remove(reason);
    }
    @Override public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        if (ownerId != null) tag.putUUID("DungeonsConstructOwner", ownerId);
        if (targetId != null) tag.putUUID("DungeonsConstructTarget", targetId);
        tag.putLong("DungeonsConstructCreated", createdAt); tag.putLong("DungeonsConstructExpires", expiresAt);
        tag.putInt("DungeonsConstructPhase", phase().ordinal()); tag.putLong("DungeonsConstructPhaseStarted", entityData.get(PHASE_STARTED));
        tag.putFloat("DungeonsConstructDamage", damage); tag.putBoolean("DungeonsConstructReleased", released);
        tag.putBoolean("DungeonsConstructSnareAccepted", snareAccepted);
    }
    @Override public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        ownerId = tag.hasUUID("DungeonsConstructOwner") ? tag.getUUID("DungeonsConstructOwner") : null;
        targetId = tag.hasUUID("DungeonsConstructTarget") ? tag.getUUID("DungeonsConstructTarget") : null;
        long now = level().getGameTime();
        createdAt = Math.min(now, tag.getLong("DungeonsConstructCreated"));
        expiresAt = Math.min(tag.getLong("DungeonsConstructExpires"), now + kind.maximumLifetimeTicks());
        entityData.set(PHASE, Math.clamp(tag.getInt("DungeonsConstructPhase"), 0, Phase.values().length - 1));
        entityData.set(PHASE_STARTED, Math.min(now, tag.getLong("DungeonsConstructPhaseStarted")));
        damage = Float.isFinite(tag.getFloat("DungeonsConstructDamage")) ? Math.clamp(tag.getFloat("DungeonsConstructDamage"), 0, 24) : 0;
        released = tag.getBoolean("DungeonsConstructReleased"); snareAccepted = tag.getBoolean("DungeonsConstructSnareAccepted");
        configured = ownerId != null && targetId != null; restored = true;
        setNoAi(true); setNoGravity(true); setCanPickUpLoot(false);
    }
}
