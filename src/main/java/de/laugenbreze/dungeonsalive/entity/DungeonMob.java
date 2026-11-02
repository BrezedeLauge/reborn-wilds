package de.laugenbreze.dungeonsalive.entity;

import de.laugenbreze.dungeonsalive.entity.construct.ConstructKind;
import de.laugenbreze.dungeonsalive.entity.construct.ConstructService;
import de.laugenbreze.dungeonsalive.entity.construct.DungeonConstruct;
import de.laugenbreze.dungeonsalive.entity.construct.DungeonTrident;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.BossEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.SmoothSwimmingMoveControl;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.AmphibiousPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import javax.annotation.Nullable;

/**
 * Reusable action-based AI. Damage, summons, cooldowns, phases and aim points belong to the server.
 * Geometry and numerical balance are original Minecraft adaptations, not a binary game port.
 */
public final class DungeonMob extends Monster {
    private static final EntityDataAccessor<Integer> ACTION = SynchedEntityData.defineId(DungeonMob.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Long> ACTION_STARTED = SynchedEntityData.defineId(DungeonMob.class, EntityDataSerializers.LONG);
    private static final EntityDataAccessor<Integer> PHASE = SynchedEntityData.defineId(DungeonMob.class, EntityDataSerializers.INT);
    private final MobProfile profile;
    private final ServerBossEvent bossEvent;
    private Vec3 lockedAim = Vec3.ZERO;
    private int attackCooldown = 30;
    private int attackCursor;
    private boolean actionReleased;
    private boolean leapLanded;
    @Nullable private UUID actionTarget;
    @Nullable private UUID summoner;
    private long summonExpiresAt;
    private final Map<UUID, Long> ownedSummons = new LinkedHashMap<>();
    private final Map<UUID, Long> ownedConstructs = new LinkedHashMap<>();
    @Nullable private PathNavigation groundNavigation, waterNavigation;
    @Nullable private MoveControl groundMoveControl, waterMoveControl;

    public DungeonMob(EntityType<? extends DungeonMob> type, Level level, MobProfile profile) {
        super(type, level);
        this.profile = profile;
        this.xpReward = profile.boss() ? 60 : profile.tier() == MobProfile.Tier.MINIBOSS ? 20 : 6;
        this.bossEvent = new ServerBossEvent(getDisplayName(), BossEvent.BossBarColor.PURPLE, BossEvent.BossBarOverlay.PROGRESS);
        var heldItem = switch (profile.id()) {
            case "armored_pillager", "diamond_armored_pillager", "pillager_raid_captain", "piglin_hunter", "zombified_piglin_hunter", "armored_piglin_hunter", "netherite_armored_piglin_hunter" -> net.minecraft.world.item.Items.CROSSBOW;
            case "armored_vindicator", "vindicator_raid_captain", "mountaineer", "armored_mountaineer", "vindicator_chef", "tower_guard", "rampart_captain" -> net.minecraft.world.item.Items.IRON_AXE;
            case "diamond_armored_vindicator" -> de.laugenbreze.dungeonsalive.item.gear.GearContent.weapon(
                    de.laugenbreze.dungeonsalive.item.gear.WeaponKind.WHIRLWIND);
            case "skeleton_horseman", "illusioner", "sunken_skeleton", "armored_sunken_skeleton", "yellow_coral_armored_sunken_skeleton", "mossy_skeleton", "armored_mossy_skeleton", "armored_skeleton", "armored_stray" -> net.minecraft.world.item.Items.BOW;
            case "armored_piglin" -> net.minecraft.world.item.Items.GOLDEN_SWORD;
            case "netherite_armored_piglin" -> net.minecraft.world.item.Items.NETHERITE_SWORD;
            case "armored_trident_drowned" -> net.minecraft.world.item.Items.TRIDENT;
            case "fungus_thrower", "zombified_fungus_thrower" -> net.minecraft.world.item.Items.WARPED_FUNGUS;
            default -> null;
        };
        if (heldItem != null) {
            setItemSlot(net.minecraft.world.entity.EquipmentSlot.MAINHAND, new net.minecraft.world.item.ItemStack(heldItem));
            setDropChance(net.minecraft.world.entity.EquipmentSlot.MAINHAND, 0);
        }
        if (profile.id().equals("pillager_raid_captain") || profile.id().equals("vindicator_raid_captain")) {
            setItemSlot(net.minecraft.world.entity.EquipmentSlot.HEAD, net.minecraft.world.entity.raid.Raid.getLeaderBannerInstance(
                    level.registryAccess().lookupOrThrow(net.minecraft.core.registries.Registries.BANNER_PATTERN)));
            setDropChance(net.minecraft.world.entity.EquipmentSlot.HEAD, 0);
        }
        if (profile.tier() != MobProfile.Tier.REGULAR) setPersistenceRequired();
        if (profile.aquatic() && !profile.stationary()) {
            groundNavigation = new AmphibiousPathNavigation(this, level);
            waterNavigation = new WaterBoundPathNavigation(this, level);
            groundMoveControl = new MoveControl(this);
            waterMoveControl = new SmoothSwimmingMoveControl(this, 85, 10, .9f, 1, false);
            this.navigation = groundNavigation;
            this.moveControl = groundMoveControl;
            this.lookControl = new LookControl(this) {
                // SmoothSwimmingMoveControl steers depth with XRot. The default look controller
                // runs afterward and would reset that steering angle to zero every server tick.
                @Override protected boolean resetXRotOnTick() { return !DungeonMob.this.isSwimming(); }
            };
        }
    }
    public MobProfile profile() { return profile; }
    @Override public boolean canDrownInFluidType(net.neoforged.neoforge.fluids.FluidType type) {
        return profile != null && profile.aquatic() ? false : super.canDrownInFluidType(type);
    }
    public static AttributeSupplier.Builder createAttributes(MobProfile p) {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, p.health())
                .add(Attributes.MOVEMENT_SPEED, p.speed()).add(Attributes.ATTACK_DAMAGE, p.damage())
                .add(Attributes.ARMOR, p.armor()).add(Attributes.FOLLOW_RANGE, p.boss() ? 36 : 24)
                .add(Attributes.KNOCKBACK_RESISTANCE, p.boss() ? .85 : p.tier() == MobProfile.Tier.MINIBOSS ? .6 : .15)
                .add(Attributes.ATTACK_KNOCKBACK, 0).add(Attributes.STEP_HEIGHT, p.width() > 1.5 ? 1 : .6);
    }
    @Override protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(ACTION, -1); builder.define(ACTION_STARTED, 0L); builder.define(PHASE, 1);
    }
    @Override protected void registerGoals() {
        goalSelector.addGoal(0, new FloatGoal(this) {
            @Override public boolean canUse() { return profile != null && !profile.aquatic() && super.canUse(); }
        });
        goalSelector.addGoal(1, new CombatGoal());
        goalSelector.addGoal(5, new AquaticWanderGoal());
        goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, .65) {
            @Override public boolean canUse() { return profile != null && !profile.stationary() && (!profile.aquatic() || !isInWater()) && super.canUse(); }
        });
        goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 10));
        goalSelector.addGoal(7, new RandomLookAroundGoal(this));
        targetSelector.addGoal(1, new HurtByTargetGoal(this, DungeonMob.class));
        targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
        targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
    }
    @Nullable public AttackKind activeAttack() {
        int id = entityData.get(ACTION);
        return id >= 0 && id < AttackKind.values().length ? AttackKind.values()[id] : null;
    }
    public float animationTick(float partialTick) {
        AttackKind attack = activeAttack();
        return attack == null ? 0 : Mth.clamp((float)(level().getGameTime() - entityData.get(ACTION_STARTED)) + partialTick, 0, attack.duration());
    }
    public int combatPhase() { return entityData.get(PHASE); }
    /** Local field-guide pose only; never runs combat code or changes a server entity. */
    public void previewAction(@Nullable AttackKind action, int elapsedTicks) {
        if (!level().isClientSide) return;
        entityData.set(ACTION, action == null ? -1 : action.ordinal());
        entityData.set(ACTION_STARTED, level().getGameTime() - (action == null ? 0 : Math.clamp(elapsedTicks, 0, action.duration())));
    }
    public boolean isSummoned() { return summoner != null; }
    public int attackCooldown() { return attackCooldown; }
    /** Absolute reservations survive unloaded children; the service also imposes a local density cap. */
    public int constructReservationCount() {
        ownedConstructs.entrySet().removeIf(entry -> entry.getValue() <= level().getGameTime());
        return ownedConstructs.size();
    }
    public boolean reserveConstruct(UUID id, long expiresAt) {
        if (level().isClientSide || !isAlive() || constructReservationCount() >= ConstructService.OWNER_CAP
                || expiresAt <= level().getGameTime() || expiresAt > level().getGameTime() + ConstructService.MAX_RESERVATION_TICKS) return false;
        ownedConstructs.put(id, expiresAt); return true;
    }
    public void releaseConstruct(UUID id) { if (!level().isClientSide) ownedConstructs.remove(id); }
    public boolean isOpponent(LivingEntity other) {
        if (other == this || !other.isAlive() || other.isRemoved() || other.level() != level()
                || other instanceof DungeonMob || other instanceof DungeonConstruct || isAlliedTo(other)) return false;
        if (other instanceof Player player) return !player.isCreative() && !player.isSpectator();
        return other == getTarget() || other instanceof IronGolem;
    }
    @Override public void setTarget(@Nullable LivingEntity target) {
        super.setTarget(target);
        // Observe the accepted target after NeoForge's target-change event. A target
        // selector may reacquire the same opponent before our next tick; a cleared
        // or changed target must still end the original telegraph immediately.
        // actionTarget is null during superclass construction and outside an action.
        if (level().isClientSide || actionTarget == null) return;
        AttackKind attack = activeAttack();
        LivingEntity accepted = getTarget();
        if (attack != null && (accepted == null || !actionTarget.equals(accepted.getUUID()) || !isOpponent(accepted))) {
            finishAttack(attack);
        }
    }
    /** Starts a legal attack, also useful to author encounters. Cannot bypass server cooldowns. */
    public boolean beginAttack(AttackKind attack, LivingEntity target) {
        if (level().isClientSide || !isAlive() || activeAttack() != null || attackCooldown > 0
                || !profile.attacks().contains(attack) || !isOpponent(target)
                || distanceToSqr(target) > attack.range() * attack.range() || !hasCombatSight(target)) return false;
        setTarget(target);
        // A target-change event may reject or replace the requested opponent.
        // Never start an action whose telegraph and actual target already disagree.
        if (getTarget() != target || !isOpponent(target)) return false;
        actionTarget = target.getUUID();
        lockedAim = switch (attack) {
            case FIRE_FIELD, ICE_FALL, ROOT_SNARE -> target.position();
            default -> target.getEyePosition();
        };
        actionReleased = false;
        leapLanded = false;
        entityData.set(ACTION_STARTED, level().getGameTime());
        entityData.set(ACTION, attack.ordinal());
        if (attack == AttackKind.SOUL_BOLT && isTridentThrower()
                && getMainHandItem().is(net.minecraft.world.item.Items.TRIDENT)) startUsingItem(InteractionHand.MAIN_HAND);
        navigation.stop();
        playSound(attack == AttackKind.ICY_EXPLOSION ? SoundEvents.CREEPER_PRIMED : attack == AttackKind.HEAVY_SLAM ? SoundEvents.IRON_GOLEM_ATTACK : SoundEvents.EVOKER_PREPARE_ATTACK, .65f, profile.boss() ? .7f : 1.1f);
        return true;
    }
    @Override public void tick() {
        super.tick();
        if (!(level() instanceof ServerLevel server)) return;
        if (!isAlive()) { clearAttackState(); bossEvent.setVisible(false); return; }
        if (summoner != null) {
            Entity owner = server.getEntity(summoner);
            if (server.getGameTime() >= summonExpiresAt || owner != null && !owner.isAlive()) { discard(); return; }
        }
        int phase = profile.boss() && getHealth() <= getMaxHealth() * .5f ? 2 : 1;
        if (profile.id().equals("vengeful_heart_of_ender") && getHealth() <= getMaxHealth() * .25f) phase = 3;
        entityData.set(PHASE, phase);
        if (profile.boss()) {
            bossEvent.setProgress(Mth.clamp(getHealth() / getMaxHealth(), 0, 1));
            bossEvent.setColor(phase >= 2 ? BossEvent.BossBarColor.RED : BossEvent.BossBarColor.PURPLE);
            bossEvent.setName(getDisplayName());
        }
        if (attackCooldown > 0) attackCooldown--;
        AttackKind attack = activeAttack();
        if (attack != null) {
            LivingEntity victim = boundAttackTarget();
            if (victim == null) {
                finishAttack(attack);
            } else {
                int elapsed = (int)animationTick(0);
                if (elapsed < attack.windup()) telegraph(server, attack, elapsed);
                if (!actionReleased && elapsed >= attack.windup()) {
                    actionReleased = true;
                    release(server, attack, victim);
                }
                if (attack == AttackKind.LEAP && actionReleased && !leapLanded && elapsed > attack.windup() + 3
                        && (onGround() || elapsed >= attack.duration() - 1)) {
                    leapLanded = true;
                    damageArea(position(), 2.5, .9f, true, null);
                }
                if (elapsed >= attack.duration()) finishAttack(attack);
            }
        }
        if (profile.stationary()) { navigation.stop(); setDeltaMovement(0, getDeltaMovement().y, 0); }
    }
    @Nullable private LivingEntity boundAttackTarget() {
        LivingEntity target = getTarget();
        return target != null && actionTarget != null && actionTarget.equals(target.getUUID()) && isOpponent(target)
                ? target : null;
    }
    private int normalCooldown(AttackKind attack) {
        return Math.max(15, attack.cooldown() - (combatPhase() - 1) * 8);
    }
    private void finishAttack(AttackKind attack) {
        clearAttackState();
        attackCooldown = normalCooldown(attack);
    }
    private void clearAttackState() {
        stopTridentUse();
        entityData.set(ACTION, -1);
        entityData.set(ACTION_STARTED, 0L);
        actionTarget = null;
        lockedAim = Vec3.ZERO;
        actionReleased = false;
        leapLanded = false;
    }
    private void telegraph(ServerLevel server, AttackKind attack, int elapsed) {
        if (elapsed % 4 != 0) return;
        switch (attack) {
            case FIRE_FIELD, ICE_FALL, ROOT_SNARE -> ring(server, lockedAim.add(0, .12, 0), 2.4,
                    attack == AttackKind.ICE_FALL ? ParticleTypes.SNOWFLAKE : ParticleTypes.ENCHANT);
            case BEAM -> beamParticles(server, beamEnd(), false);
            case STORM, HEAVY_SLAM -> ring(server, position().add(0, .12, 0), attack == AttackKind.STORM ? 7 : slamRadius(), ParticleTypes.ENCHANT);
            case ICY_EXPLOSION -> ring(server, position().add(0, .12, 0), 3.2, ParticleTypes.SNOWFLAKE);
            default -> server.sendParticles(ParticleTypes.ENCHANT, getX(), getY(.7), getZ(), 3, getBbWidth() * .3, .2, getBbWidth() * .3, .02);
        }
    }
    private void release(ServerLevel server, AttackKind attack, LivingEntity victim) {
        if (ConstructService.mayHarm(this, victim)) {
            if (profile.id().equals("geomancer") && attack == AttackKind.ROOT_SNARE) {
                ConstructService.castWalls(this, victim, lockedAim, attackDamage()); return;
            }
            if (profile.id().equals("geomancer") && attack == AttackKind.FIRE_FIELD) {
                ConstructService.castBomb(this, victim, lockedAim, attackDamage()); return;
            }
            if (profile.id().equals("iceologer") && attack == AttackKind.ICE_FALL) {
                ConstructService.spawn(this, victim, ConstructKind.ICE_CHUNK, lockedAim, attackDamage()); return;
            }
            if ((profile.id().equals("whisperer") || profile.id().equals("wavewhisperer") || profile.id().equals("treetop_whisperer"))
                    && attack == AttackKind.ROOT_SNARE) {
                ConstructService.spawn(this, victim, ConstructKind.ENTANGLE_VINE, lockedAim, attackDamage()); return;
            }
        } else if ((profile.id().equals("geomancer") && (attack == AttackKind.ROOT_SNARE || attack == AttackKind.FIRE_FIELD))
                || profile.id().equals("iceologer") && attack == AttackKind.ICE_FALL
                || (profile.id().equals("whisperer") || profile.id().equals("wavewhisperer") || profile.id().equals("treetop_whisperer")) && attack == AttackKind.ROOT_SNARE) {
            return; // Lost/invalid construct target never falls back to an unannounced generic area hit.
        }
        switch (attack) {
            case MELEE -> {
                if (distanceToSqr(victim) <= square(Math.max(2.6, profile.width() + 1.1)) && hasCombatSight(victim)) {
                    if (victim.hurt(damageSources().mobAttack(this), attackDamage())) {
                        victim.knockback(.45, getX() - victim.getX(), getZ() - victim.getZ());
                    }
                }
            }
            case HEAVY_SLAM -> {
                damageArea(position(), slamRadius(), 1.2f, true, null);
                ring(server, position().add(0, .2, 0), slamRadius(), ParticleTypes.CRIT);
                playSound(SoundEvents.IRON_GOLEM_ATTACK, 1f, .6f);
            }
            case SOUL_BOLT -> {
                shootBolt(server, 0, 1f);
                stopTridentUse();
            }
            case VOLLEY -> {
                int count = combatPhase() >= 2 ? 5 : 3;
                for (int i = 0; i < count; i++) shootBolt(server, (i - (count - 1) / 2f) * .16, .65f);
            }
            case FIRE_FIELD -> {
                damageArea(lockedAim, 2.4, 1f, false, new MobEffectInstance(MobEffects.WEAKNESS, 60));
                server.sendParticles(ParticleTypes.SOUL_FIRE_FLAME, lockedAim.x, lockedAim.y + .3, lockedAim.z, 35, 1.5, .3, 1.5, .02);
            }
            case ICE_FALL -> {
                damageArea(lockedAim, 2.4, 1.1f, false, new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 80, 1));
                server.sendParticles(ParticleTypes.SNOWFLAKE, lockedAim.x, lockedAim.y + 1.5, lockedAim.z, 45, 1.3, 1.5, 1.3, .04);
            }
            case ROOT_SNARE -> {
                damageArea(lockedAim, 2.4, .65f, false, new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 80, 3));
                ring(server, lockedAim.add(0, .3, 0), 2.4, ParticleTypes.COMPOSTER);
            }
            case BEAM -> releaseBeam(server);
            case STORM -> {
                damageArea(position(), 7, .8f, true, null);
                ring(server, position().add(0, .6, 0), 7, ParticleTypes.CLOUD);
                playSound(SoundEvents.BREEZE_WIND_CHARGE_BURST.value(), .9f, .8f);
            }
            case SUMMON -> summon(server, victim);
            case TELEPORT -> safeTeleport(server);
            case GUARD -> playSound(SoundEvents.SHIELD_BLOCK, .75f, .8f);
            case ENCHANT -> enchantAllies(server);
            case LEAP -> {
                Vec3 direction = lockedAim.subtract(position());
                Vec3 horizontal = new Vec3(direction.x, 0, direction.z).normalize().scale(.65);
                setDeltaMovement(horizontal.x, .52, horizontal.z);
                hasImpulse = true;
            }
            case ICY_EXPLOSION -> {
                // A cold, telegraphed burst. Keep player-built blocks and passive bystanders intact.
                damageArea(position(), 3.2, 1.4f, true, new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 2));
                server.sendParticles(ParticleTypes.SNOWFLAKE, getX(), getY(.5), getZ(), 80, 1.5, .8, 1.5, .1);
                server.sendParticles(ParticleTypes.EXPLOSION, getX(), getY(.5), getZ(), 1, 0, 0, 0, 0);
                playSound(SoundEvents.GENERIC_EXPLODE.value(), 1.2f, 1.25f);
                discard();
            }
        }
    }
    private static double square(double n) { return n * n; }
    private boolean hasCombatSight(LivingEntity target) {
        return ConstructService.clearLine(level(), getEyePosition(), target.getEyePosition(), this) && hasLineOfSight(target);
    }
    private double slamRadius() { return Math.min(5.5, profile.width() + 1.6); }
    private float attackDamage() { return (float)getAttributeValue(Attributes.ATTACK_DAMAGE) * (combatPhase() >= 2 ? 1.1f : 1); }
    private void damageArea(Vec3 center, double radius, float damageMultiplier, boolean push, @Nullable MobEffectInstance effect) {
        AABB box = new AABB(center.x - radius, center.y - 1, center.z - radius, center.x + radius, center.y + 3, center.z + radius);
        for (LivingEntity other : level().getEntitiesOfClass(LivingEntity.class, box, this::isOpponent)) {
            double dx = other.getX() - center.x, dz = other.getZ() - center.z;
            if (dx * dx + dz * dz > radius * radius || !hasCombatSight(other)
                    || !ConstructService.clearLine(level(), center.add(0, .4, 0), other.getEyePosition(), this)) continue;
            if (other.hurt(damageSources().mobAttack(this), attackDamage() * damageMultiplier)) {
                if (push) other.knockback(.9, -dx, -dz);
                if (effect != null) other.addEffect(new MobEffectInstance(effect), this);
            }
        }
    }
    private void shootBolt(ServerLevel server, double angle, float damageMultiplier) {
        if (isTridentThrower()) {
            DungeonTrident.launch(this, lockedAim, attackDamage() * damageMultiplier);
            return;
        }
        Vec3 from = new Vec3(getX(), getY(.65), getZ());
        Vec3 aim = lockedAim.subtract(from).normalize();
        Vec3 direction = new Vec3(aim.x * Math.cos(angle) - aim.z * Math.sin(angle), aim.y,
                aim.x * Math.sin(angle) + aim.z * Math.cos(angle));
        server.addFreshEntity(new SoulBolt(server, this, direction, attackDamage() * damageMultiplier));
        playSound(SoundEvents.EVOKER_CAST_SPELL, .5f, 1.2f);
    }
    private boolean isTridentThrower() {
        return profile != null && (profile.id().equals("armored_trident_drowned") || profile.id().equals("trident_drowned"));
    }
    private void stopTridentUse() {
        // Item use publishes the vanilla throwing model predicate. The actual projectile
        // belongs to our server action, so never invoke releaseUsingItem/item release callbacks.
        if (isTridentThrower() && isUsingItem()) stopUsingItem();
    }
    private Vec3 beamEnd() {
        Vec3 from = getEyePosition();
        Vec3 delta = lockedAim.subtract(from);
        Vec3 to = from.add(delta.normalize().scale(Math.min(22, delta.length() + 3)));
        if (!loaded(new AABB(from, to).inflate(1))) return from;
        HitResult clip = level().clip(new ClipContext(from, to, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
        return ConstructService.clipWalls(level(), from, clip.getLocation(), this);
    }
    private void releaseBeam(ServerLevel server) {
        Vec3 from = getEyePosition(), to = beamEnd();
        AABB area = new AABB(from, to).inflate(.8);
        for (LivingEntity other : server.getEntitiesOfClass(LivingEntity.class, area, this::isOpponent)) {
            if (other.getBoundingBox().inflate(.45).clip(from, to).isPresent()) other.hurt(damageSources().indirectMagic(this, this), attackDamage() * 1.25f);
        }
        beamParticles(server, to, true);
        playSound(SoundEvents.GUARDIAN_ATTACK, .9f, .7f);
    }
    private void beamParticles(ServerLevel server, Vec3 end, boolean released) {
        Vec3 from = getEyePosition(), vector = end.subtract(from);
        int count = Math.min(36, (int)(vector.length() * (released ? 1.5 : .7)) + 1);
        for (int i = 0; i <= count; i++) {
            Vec3 p = from.add(vector.scale((double)i / count));
            server.sendParticles(released ? ParticleTypes.END_ROD : ParticleTypes.ENCHANT, p.x, p.y, p.z, 1, .025, .025, .025, 0);
        }
    }
    private void summon(ServerLevel server, LivingEntity victim) {
        // Reservation survives chunk unloads; moving old summons away cannot bypass the six-mob cap.
        ownedSummons.entrySet().removeIf(entry -> entry.getValue() <= server.getGameTime()
                || server.getEntity(entry.getKey()) instanceof LivingEntity living && !living.isAlive());
        var local = server.getEntitiesOfClass(DungeonMob.class, getBoundingBox().inflate(32));
        int allowance = Math.min(6 - ownedSummons.size(), 32 - local.size());
        String summonedId = switch (profile.id()) {
            case "arch_illager" -> "royal_guard";
            case "corrupted_cauldron" -> "conjured_slime";
            case "redstone_monstrosity" -> "redstone_cube";
            case "jungle_abomination", "treetop_whisperer" -> "whisperer";
            case "ancient_guardian" -> "wavewhisperer";
            default -> "vanguard";
        };
        for (int i = 0; i < Math.min(3, allowance); i++) {
            for (int attempt = 0; attempt < 6; attempt++) {
                double angle = random.nextDouble() * Math.PI * 2;
                Vec3 desired = position().add(Math.cos(angle) * (profile.width() + 2), 0, Math.sin(angle) * (profile.width() + 2));
                BlockPos floor = BlockPos.containing(desired);
                if (!server.hasChunkAt(floor)) continue;
                DungeonMob mob = ModEntities.type(summonedId).create(server);
                if (mob == null) break;
                mob.moveTo(desired.x, desired.y, desired.z, random.nextFloat() * 360, 0);
                if (!loaded(mob.getBoundingBox().inflate(1)) || !server.noCollision(mob)
                        || !server.getBlockState(floor.below()).isSolidRender(server, floor.below()) && !mob.isInWater()) continue;
                mob.summoner = getUUID(); mob.summonExpiresAt = server.getGameTime() + 1200; mob.xpReward = 0;
                mob.setTarget(victim);
                if (server.addFreshEntity(mob)) {
                    ownedSummons.put(mob.getUUID(), mob.summonExpiresAt);
                    server.sendParticles(ParticleTypes.SOUL, desired.x, desired.y + .7, desired.z, 10, .4, .5, .4, .01);
                    break;
                }
            }
        }
    }
    private void safeTeleport(ServerLevel server) {
        for (int attempt = 0; attempt < 8; attempt++) {
            double angle = random.nextDouble() * Math.PI * 2;
            Vec3 destination = position().add(Math.cos(angle) * 5, 0, Math.sin(angle) * 5);
            BlockPos block = BlockPos.containing(destination);
            if (!server.hasChunkAt(block) || !server.getWorldBorder().isWithinBounds(block)) continue;
            AABB moved = getBoundingBox().move(destination.subtract(position()));
            if (!loaded(moved.inflate(1)) || !server.noCollision(this, moved)
                    || !server.getBlockState(block.below()).isSolidRender(server, block.below()) || server.containsAnyLiquid(moved)) continue;
            server.sendParticles(ParticleTypes.PORTAL, getX(), getY(.5), getZ(), 18, .5, .8, .5, .1);
            teleportTo(destination.x, destination.y, destination.z);
            server.sendParticles(ParticleTypes.PORTAL, getX(), getY(.5), getZ(), 18, .5, .8, .5, .1);
            playSound(SoundEvents.ENDERMAN_TELEPORT, .8f, 1);
            break;
        }
    }
    private void enchantAllies(ServerLevel server) {
        var nearby = server.getEntitiesOfClass(DungeonMob.class, getBoundingBox().inflate(12), mob -> mob != this && mob.isAlive() && hasLineOfSight(mob));
        nearby.sort(java.util.Comparator.comparingDouble(this::distanceToSqr));
        for (DungeonMob ally : nearby.stream().limit(3).toList()) {
            ally.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 120, 0), this);
            ally.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 120, 0), this);
            server.sendParticles(ParticleTypes.ENCHANT, ally.getX(), ally.getY(.7), ally.getZ(), 20, .4, .6, .4, .1);
        }
    }
    private static void ring(ServerLevel server, Vec3 center, double radius, net.minecraft.core.particles.SimpleParticleType particle) {
        for (int i = 0; i < 20; i++) {
            double angle = i * Math.PI * .1;
            server.sendParticles(particle, center.x + Math.cos(angle) * radius, center.y, center.z + Math.sin(angle) * radius, 1, 0, .02, 0, 0);
        }
    }
    private boolean loaded(AABB box) {
        return level().hasChunksAt(BlockPos.containing(box.minX, box.minY, box.minZ), BlockPos.containing(box.maxX, box.maxY, box.maxZ));
    }
    @Override public boolean hurt(DamageSource source, float damage) {
        if (activeAttack() == AttackKind.GUARD && animationTick(0) >= AttackKind.GUARD.windup()
                && !source.is(DamageTypeTags.BYPASSES_ARMOR) && source.getSourcePosition() != null) {
            Vec3 incoming = source.getSourcePosition().subtract(position()).normalize();
            Vec3 forward = getViewVector(1);
            if (incoming.dot(new Vec3(forward.x, 0, forward.z)) > .15) {
                damage *= .25f;
                playSound(SoundEvents.SHIELD_BLOCK, .65f, .9f);
            }
        }
        return super.hurt(source, damage);
    }
    @Override public boolean checkSpawnObstruction(net.minecraft.world.level.LevelReader level) {
        return profile != null && profile.aquatic()
                ? NaturalMobSpawning.aquaticObstruction(this, level) : super.checkSpawnObstruction(level);
    }
    @Override public void travel(Vec3 input) {
        if (profile != null && profile.stationary()) { super.travel(Vec3.ZERO); return; }
        if (profile != null && profile.aquatic() && isInWater() && navigation == waterNavigation && isControlledByLocalInstance()) {
            moveRelative(.08f, input); move(MoverType.SELF, getDeltaMovement()); setDeltaMovement(getDeltaMovement().scale(.9));
        } else super.travel(input);
    }
    @Override public void updateSwimming() {
        if (profile == null || !profile.aquatic() || profile.stationary() || waterNavigation == null) { super.updateSwimming(); return; }
        if (level().isClientSide) return;
        LivingEntity target = getTarget();
        boolean useWater = isInWater() && (target == null || target.isInWater());
        PathNavigation nextNavigation = useWater ? waterNavigation : groundNavigation;
        if (navigation != nextNavigation) { navigation.stop(); navigation = nextNavigation; }
        moveControl = useWater ? waterMoveControl : groundMoveControl;
        boolean swimming = useWater && isEffectiveAi()
                && (target != null && target.isInWater() || !navigation.isDone() || getDeltaMovement().lengthSqr() > .0025);
        setSwimming(swimming);
        if (isAlive() && !isPassenger()) setPose(swimming ? Pose.SWIMMING : Pose.STANDING);
        if (!swimming) setXRot(Mth.rotLerp(.2f, getXRot(), 0));
    }
    @Override public boolean isVisuallySwimming() { return profile != null && profile.aquatic() ? isSwimming() : super.isVisuallySwimming(); }
    @Override public boolean isPushedByFluid() { return !isSwimming() && super.isPushedByFluid(); }
    @Override protected boolean shouldDropLoot() { return !isSummoned() && super.shouldDropLoot(); }
    @Override public boolean shouldDropExperience() { return !isSummoned() && super.shouldDropExperience(); }
    @Override public void startSeenByPlayer(ServerPlayer player) { super.startSeenByPlayer(player); if (profile.boss()) bossEvent.addPlayer(player); }
    @Override public void stopSeenByPlayer(ServerPlayer player) { super.stopSeenByPlayer(player); bossEvent.removePlayer(player); }
    @Override public void die(DamageSource source) {
        if (!level().isClientSide) clearAttackState();
        else stopTridentUse();
        super.die(source);
    }
    @Override public void remove(RemovalReason reason) {
        if (!level().isClientSide) clearAttackState();
        if (bossEvent != null) bossEvent.removeAllPlayers();
        super.remove(reason);
    }
    @Override public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("DungeonsAttackCursor", attackCursor);
        AttackKind active = activeAttack();
        tag.putInt("DungeonsCooldown", active == null ? attackCooldown : Math.max(attackCooldown, normalCooldown(active)));
        if (summoner != null) { tag.putUUID("DungeonsSummoner", summoner); tag.putLong("DungeonsSummonExpiresAt", summonExpiresAt); }
        ListTag children = new ListTag();
        ownedSummons.forEach((id, until) -> {
            CompoundTag child = new CompoundTag(); child.putUUID("Id", id); child.putLong("ExpiresAt", until); children.add(child);
        });
        tag.put("DungeonsOwnedSummons", children);
        ListTag constructs = new ListTag();
        ownedConstructs.forEach((id, until) -> {
            CompoundTag child = new CompoundTag(); child.putUUID("Id", id); child.putLong("ExpiresAt", until); constructs.add(child);
        });
        tag.put("DungeonsOwnedConstructs", constructs);
    }
    @Override public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        attackCursor = Math.max(0, tag.getInt("DungeonsAttackCursor")) % profile.attacks().size();
        attackCooldown = Math.clamp(tag.getInt("DungeonsCooldown"), 30, 200);
        clearAttackState(); // A persisted half-cast is cancelled; damage is never replayed after loading or object reuse.
        if (tag.hasUUID("DungeonsSummoner")) {
            summoner = tag.getUUID("DungeonsSummoner");
            summonExpiresAt = Math.min(tag.getLong("DungeonsSummonExpiresAt"), level().getGameTime() + 1200);
            xpReward = 0;
        }
        ownedSummons.clear();
        ListTag children = tag.getList("DungeonsOwnedSummons", 10);
        for (int i = 0; i < Math.min(6, children.size()); i++) {
            CompoundTag child = children.getCompound(i);
            if (child.hasUUID("Id")) ownedSummons.put(child.getUUID("Id"), Math.min(child.getLong("ExpiresAt"), level().getGameTime() + 1200));
        }
        ownedConstructs.clear();
        ListTag constructs = tag.getList("DungeonsOwnedConstructs", 10);
        for (int i = 0; i < Math.min(ConstructService.OWNER_CAP, constructs.size()); i++) {
            CompoundTag child = constructs.getCompound(i);
            if (child.hasUUID("Id")) ownedConstructs.put(child.getUUID("Id"),
                    Math.min(child.getLong("ExpiresAt"), level().getGameTime() + ConstructService.MAX_RESERVATION_TICKS));
        }
    }
    @Override protected SoundEvent getHurtSound(DamageSource source) {
        if (profile == null) return SoundEvents.HOSTILE_HURT;
        return switch (profile.family()) {
            case ILLAGER -> SoundEvents.VINDICATOR_HURT;
            case ZOMBIE -> SoundEvents.ZOMBIE_HURT;
            case PIGLIN -> profile.id().startsWith("zombified") ? SoundEvents.ZOMBIFIED_PIGLIN_HURT : SoundEvents.PIGLIN_HURT;
            case CREEPER -> SoundEvents.CREEPER_HURT;
            case SKELETON, HORSEMAN -> SoundEvents.SKELETON_HURT;
            case GOLEM -> SoundEvents.IRON_GOLEM_HURT;
            case GUARDIAN -> SoundEvents.GUARDIAN_HURT;
            case WILDFIRE -> SoundEvents.BLAZE_HURT;
            case SLIME -> SoundEvents.SLIME_HURT;
            default -> SoundEvents.ENDERMAN_HURT;
        };
    }
    @Override protected SoundEvent getDeathSound() {
        return profile != null && profile.family() == MobProfile.Family.GOLEM ? SoundEvents.IRON_GOLEM_DEATH : SoundEvents.EVOKER_DEATH;
    }
    private final class AquaticWanderGoal extends Goal {
        private Vec3 destination;
        AquaticWanderGoal() { setFlags(EnumSet.of(Flag.MOVE)); }
        @Override public boolean canUse() {
            if (profile == null || !profile.aquatic() || profile.stationary() || !isInWater()
                    || getTarget() != null || activeAttack() != null || !navigation.isDone() || random.nextInt(45) != 0) return false;
            for (int i = 0; i < 8; i++) {
                Vec3 candidate = position().add((random.nextDouble() - .5) * 8, (random.nextDouble() - .5) * 4, (random.nextDouble() - .5) * 8);
                AABB body = getBoundingBox().move(candidate.subtract(position()));
                if (!loaded(body.inflate(1)) || body.minY <= level().getMinBuildHeight() || body.maxY >= level().getMaxBuildHeight()) continue;
                if (level().getFluidState(BlockPos.containing(candidate)).is(FluidTags.WATER)
                        && level().getFluidState(BlockPos.containing(candidate.add(0, getBbHeight() * .8, 0))).is(FluidTags.WATER)
                        && level().noBlockCollision(DungeonMob.this, body)) { destination = candidate; return true; }
            }
            return false;
        }
        @Override public void start() { navigation.moveTo(destination.x, destination.y, destination.z, .65); }
        @Override public boolean canContinueToUse() { return isInWater() && getTarget() == null && !navigation.isDone() && activeAttack() == null; }
        @Override public void stop() { navigation.stop(); }
    }
    private final class CombatGoal extends Goal {
        CombatGoal() { setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK)); }
        @Override public boolean canUse() { return getTarget() != null && isOpponent(getTarget()); }
        @Override public boolean requiresUpdateEveryTick() { return true; }
        @Override public void stop() { navigation.stop(); }
        @Override public void tick() {
            LivingEntity target = getTarget();
            if (target == null) return;
            if (activeAttack() != null) {
                getLookControl().setLookAt(lockedAim.x, lockedAim.y, lockedAim.z, 25, 25);
                navigation.stop(); return;
            }
            getLookControl().setLookAt(target, 25, 25);
            AttackKind next = profile.attacks().get(attackCursor % profile.attacks().size());
            double range = next.range();
            if (distanceToSqr(target) > square(range * .88) || !hasCombatSight(target)) {
                if (!profile.stationary()) navigation.moveTo(target, 1);
            } else {
                navigation.stop();
                if (beginAttack(next, target)) attackCursor = (attackCursor + 1) % profile.attacks().size();
            }
        }
    }
}
