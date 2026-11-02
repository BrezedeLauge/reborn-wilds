package de.laugenbreze.dungeonsalive.entity.peaceful;

import de.laugenbreze.dungeonsalive.DungeonsAlive;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.*;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.entity.ai.control.*;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.navigation.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.phys.Vec3;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;
import java.util.UUID;
import org.jetbrains.annotations.Nullable;

/** Peaceful wildlife, pets and explicitly distinguished civilians. No combat target goals. */
public final class DungeonsCreature extends PathfinderMob {
    private static final EntityDataAccessor<Boolean> WAITING = SynchedEntityData.defineId(DungeonsCreature.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> RESCUED = SynchedEntityData.defineId(DungeonsCreature.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Long> RESCUE_BEGIN = SynchedEntityData.defineId(DungeonsCreature.class, EntityDataSerializers.LONG);
    private static final EntityDataAccessor<Boolean> WOOLY_BABY = SynchedEntityData.defineId(DungeonsCreature.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> BIRD_PERCHED = SynchedEntityData.defineId(DungeonsCreature.class, EntityDataSerializers.BOOLEAN);
    private static final ResourceLocation WOOLY_BABY_SCALE = ResourceLocation.fromNamespaceAndPath(DungeonsAlive.MOD_ID, "wooly_calf_scale");
    private static final EntityDataAccessor<Boolean> DUCK_FAMILY = SynchedEntityData.defineId(DungeonsCreature.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> DUCK_YOUNG = SynchedEntityData.defineId(DungeonsCreature.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> DUCK_ACTIVITY = SynchedEntityData.defineId(DungeonsCreature.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Long> DUCK_BEGIN = SynchedEntityData.defineId(DungeonsCreature.class, EntityDataSerializers.LONG);
    private static final ResourceLocation DUCK_YOUNG_SCALE = ResourceLocation.fromNamespaceAndPath(DungeonsAlive.MOD_ID, "duck_young_scale");
    private final PeacefulProfiles.Profile profile;
    @Nullable private UUID owner;
    private boolean naturalWildlife;
    private boolean naturalCompanion;
    private boolean usingWildCompanionMovement;
    private final PathNavigation companionNavigation;
    private final MoveControl companionMoveControl;
    private final LookControl companionLookControl;
    @Nullable private WildlifeGroup wildlifeGroup;
    @Nullable private BlockPos wildlifeAnchor;
    private List<DungeonsCreature> nearbyGroup = List.of();
    private long nextGroupScan;
    private long wildlifeAlarmUntil = Long.MIN_VALUE;
    private Vec3 wildlifeThreat = Vec3.ZERO;
    private final WoolyCowBreedingState woolyBreeding = new WoolyCowBreedingState();
    @Nullable private UUID woolyParentA;
    @Nullable private UUID woolyParentB;
    private final PiggyBankTreasureState piggyTreasure = new PiggyBankTreasureState();
    private final BirdRoutineState birdRoutine = new BirdRoutineState();
    private long piggyAlarmUntil = Long.MIN_VALUE;
    @Nullable private BlockPos piggyEscapeOrigin;
    private Vec3 piggyThreat = Vec3.ZERO;
    private final DuckFamilyState duckFamilyState = new DuckFamilyState();
    @Nullable private UUID duckParent;
    @Nullable private BlockPos duckDestination;

    public DungeonsCreature(EntityType<? extends DungeonsCreature> type, Level level, PeacefulProfiles.Profile profile) {
        super(type, level);
        this.profile = profile;
        // Natural wildlife must participate in the ordinary creature cap and despawn
        // lifecycle. Manual creation is made persistent by finalizeSpawn or its first AI tick.
        if (!profile.wildlifeEligible()) setPersistenceRequired();
        if (civilian()) entityData.set(WAITING, true);
        if (flyingWildlife()) {
            navigation = new WildlifeFlightNavigation(this, level);
            moveControl = new WildlifeFlightMoveControl(this);
            lookControl = new LookControl(this) {
                @Override protected boolean resetXRotOnTick() { return navigation.isDone(); }
            };
            setNoGravity(true);
        } else if (profile.flying()) {
            navigation = new FlyingPathNavigation(this, level);
            moveControl = new FlyingMoveControl(this, 20, true);
            setNoGravity(true);
        } else if (profile.aquatic()) {
            navigation = new WaterBoundPathNavigation(this, level);
            moveControl = new SmoothSwimmingMoveControl(this, 85, 10, .15F, .5F, true);
            lookControl = new LookControl(this) {
                // The swimming move controller steers depth through XRot; the ordinary
                // look controller must not erase that angle later in the same AI tick.
                @Override protected boolean resetXRotOnTick() {
                    return !DungeonsCreature.this.isInWater() || navigation.isDone();
                }
            };
        } else if (profile.amphibious()) {
            navigation = new AmphibiousPathNavigation(this, level);
        } else if (groupWildlife() || piggyBank()) {
            navigation = new WildlifeGroundNavigation(this, level);
        }
        companionNavigation = navigation;
        companionMoveControl = moveControl;
        companionLookControl = lookControl;
    }
    public PeacefulProfiles.Profile profile() { return profile; }
    public boolean civilian() { return profile != null && profile.kind() == PeacefulProfiles.Kind.CIVILIAN; }
    public boolean rescued() { return entityData.get(RESCUED); }
    public float rescueGestureTicks(float partialTick) { return (float)(level().getGameTime() - entityData.get(RESCUE_BEGIN)) + partialTick; }
    public boolean waiting() { return entityData.get(WAITING); }
    @Nullable public UUID ownerId() { return owner; }
    /** Natural provenance is explicit; an unowned spawn egg or an old pet is not a wild encounter. */
    public boolean naturalWildlife() { return owner == null && naturalWildlife; }
    public boolean naturalWildBird() { return naturalCompanion && owner == null && profile.wildBirdEligible(); }
    public boolean groupWildlife() {
        return profile != null && owner == null && (naturalCompanion
                || profile.kind() == PeacefulProfiles.Kind.WILDLIFE
                && (profile.id().equals("wooly_cow") || profile.id().equals("vulture") || profile.id().equals("wisp")));
    }
    public boolean flyingWildlife() { return groupWildlife() && profile.flying(); }
    public boolean birdWildlife() { return flyingWildlife() && (profile.id().equals("vulture") || profile.wildBirdEligible()); }
    public boolean birdPerched() { return entityData.get(BIRD_PERCHED); }
    public BirdRoutineState.Phase birdActivity() { return birdRoutine.phase(); }
    @Nullable public BlockPos birdPerch() { return birdRoutine.perch(); }
    BirdRoutineState birdRoutine() { return birdRoutine; }
    void setBirdPerched(boolean perched) { if (!level().isClientSide) entityData.set(BIRD_PERCHED, perched); }
    boolean birdLandingSteering() { return birdWildlife() && birdRoutine.guiding; }
    public boolean aquaticWildlife() { return groupWildlife() && profile.aquatic(); }
    public boolean duck() { return profile != null && profile.id().equals("pet_duck"); }
    public boolean duckFamily() { return entityData.get(DUCK_FAMILY); }
    public boolean duckYoung() { return entityData.get(DUCK_YOUNG); }
    public DuckFamilyState.Activity duckActivity() {
        int activity = entityData.get(DUCK_ACTIVITY);
        return DuckFamilyState.Activity.values()[Math.clamp(activity, 0, DuckFamilyState.Activity.values().length - 1)];
    }
    public float duckActivityTicks(float partialTick) { return (float)(level().getGameTime() - entityData.get(DUCK_BEGIN)) + partialTick; }
    DuckFamilyState duckState() { return duckFamilyState; }
    @Nullable UUID duckParent() { return duckParent; }
    @Nullable BlockPos duckDestination() { return duckDestination; }
    void duckDestination(@Nullable BlockPos pos) { duckDestination = pos == null ? null : pos.immutable(); }
    public boolean piggyBank() { return profile != null && profile.kind() == PeacefulProfiles.Kind.WILDLIFE && profile.id().equals("piggy_bank"); }
    public boolean piggyFleeing() { return piggyBank() && piggyEscapeOrigin != null && level().getGameTime() < piggyAlarmUntil; }
    public boolean piggyTreasureSpent() { return piggyBank() && piggyTreasure.spent(); }
    @Nullable BlockPos piggyEscapeOrigin() { return piggyEscapeOrigin; }
    Vec3 piggyThreat() { return piggyThreat; }
    public boolean woolyCow() { return profile != null && profile.kind() == PeacefulProfiles.Kind.WILDLIFE && profile.id().equals("wooly_cow"); }
    public int woolyAgeTicks() { return woolyCow() ? woolyBreeding.age() : 0; }
    public boolean woolyInLove() { return woolyCow() && woolyBreeding.inLove(); }
    @Override public boolean isBaby() { return entityData.get(WOOLY_BABY) || entityData.get(DUCK_YOUNG); }
    @Override public float getAgeScale() {
        // Our SCALE attribute already halves both the body and the source-model renderer.
        // Do not apply vanilla's additional baby dimension multiplier to this species.
        return woolyCow() || duck() ? 1F : super.getAgeScale();
    }
    boolean calmWoolyCow() {
        return woolyCow() && owner == null && isAlive() && !isRemoved() && !isNoAi() && !waiting()
                && !isPassenger() && !isInWater() && !isInLava()
                && wildlifeAlarmUntil <= level().getGameTime();
    }
    boolean canCourt(DungeonsCreature other) {
        return other != this && other.level() == level() && calmWoolyCow() && other.calmWoolyCow()
                && woolyInLove() && other.woolyInLove();
    }
    boolean isWoolyParent(DungeonsCreature other) {
        return other.woolyCow() && (other.getUUID().equals(woolyParentA) || other.getUUID().equals(woolyParentB));
    }
    @Nullable public UUID wildlifeGroupId() { return wildlifeGroup == null ? null : wildlifeGroup.id(); }
    @Nullable public BlockPos wildlifeAnchor() { return wildlifeAnchor; }
    public int wildlifeSlot() { return wildlifeGroup == null ? 0 : Math.max(0, wildlifeGroup.slot(getUUID())); }
    public long wildlifeAlarmUntil() { return wildlifeAlarmUntil; }
    public Vec3 wildlifeThreat() { return wildlifeThreat; }
    public WildlifeGroupPolicy.Activity wildlifeActivity() {
        if (wildlifeGroup == null || profile == null) return WildlifeGroupPolicy.Activity.REST;
        boolean nocturnal = profile.nocturnalWildlife();
        // Covered canopy still follows the surface clock. Only a dimension without
        // skylight, or an already-loaded fully dark sky column, uses its own active phase.
        boolean darkHabitat = !level().dimensionType().hasSkyLight()
                || level().hasChunkAt(blockPosition()) && !level().canSeeSky(blockPosition())
                    && level().getBrightness(LightLayer.SKY, blockPosition()) == 0;
        long dayTime = darkHabitat ? (nocturnal ? 18_000L : 6_000L) : level().getDayTime();
        return WildlifeGroupPolicy.activity(wildlifeGroup.id(), level().getGameTime(), dayTime,
                        nocturnal, !darkHabitat && level().isRaining(), wildlifeAlarmUntil);
    }
    public List<DungeonsCreature> wildlifeMembers() {
        if (wildlifeGroup == null) return List.of();
        return nearbyGroup.stream().filter(this::availableGroupMember).sorted(Comparator.comparing(Entity::getUUID))
                .limit(WildlifeGroupPolicy.MAX_MEMBERS).toList();
    }
    public DungeonsCreature wildlifeLeader() {
        var members = wildlifeMembers();
        // A newborn's UUID must not turn the whole adult herd into followers of a calf
        // that is itself trying to walk toward its parents.
        if (woolyCow() || duckFamily()) {
            var adult = members.stream().filter(member -> !member.isBaby()).findFirst();
            if (adult.isPresent()) return adult.get();
        }
        return members.isEmpty() ? this : members.getFirst();
    }
    public static AttributeSupplier.Builder createAttributes(PeacefulProfiles.Profile p) {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, p.kind()==PeacefulProfiles.Kind.KEY_GOLEM ? 30 : 16)
                .add(Attributes.MOVEMENT_SPEED,p.speed()).add(Attributes.FLYING_SPEED,.3).add(Attributes.FOLLOW_RANGE,24)
                .add(Attributes.SCALE, 1);
    }
    @Override protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder); builder.define(WAITING,false);
        builder.define(RESCUED,false); builder.define(RESCUE_BEGIN,-1000L);
        builder.define(WOOLY_BABY, false);
        builder.define(BIRD_PERCHED, false);
        builder.define(DUCK_FAMILY, false); builder.define(DUCK_YOUNG, false);
        builder.define(DUCK_ACTIVITY, DuckFamilyState.Activity.REST.ordinal()); builder.define(DUCK_BEGIN, 0L);
    }
    @Override protected void registerGoals() {
        goalSelector.addGoal(0,new FloatGoal(this) {
            @Override public boolean canUse(){return profile!=null && !duckFamily() && !profile.aquatic() && !profile.amphibious() && super.canUse();}
        });
        goalSelector.addGoal(1,new FollowKeeperGoal());
        goalSelector.addGoal(2,new PanicGoal(this,1.25) {
            @Override public boolean canUse(){return civilian() && rescued() && !waiting() && super.canUse();}
        });
        goalSelector.addGoal(3,new WoolyCowSocialGoal(this));
        goalSelector.addGoal(3,new DuckFamilyGoal(this));
        goalSelector.addGoal(2,new PiggyBankEscapeGoal(this));
        goalSelector.addGoal(4,new WildlifeGroundGroupGoal(this));
        goalSelector.addGoal(4,new WildlifeFlockGoal(this));
        goalSelector.addGoal(4,new WildlifeSchoolGoal(this));
        goalSelector.addGoal(4,new WanderGoal());
        goalSelector.addGoal(6,new LookAtPlayerGoal(this,Player.class,6));
        goalSelector.addGoal(7,new RandomLookAroundGoal(this));
        // Deliberately no target selector: pets do not attack players or villagers.
    }
    @Override @Nullable public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                                             MobSpawnType reason, @Nullable SpawnGroupData data) {
        SpawnGroupData inherited = super.finalizeSpawn(level, difficulty, reason, data);
        naturalWildlife = owner == null && profile.wildlifeEligible()
                && (reason == MobSpawnType.NATURAL || reason == MobSpawnType.CHUNK_GENERATION);
        naturalCompanion = naturalWildlife && profile.kind() == PeacefulProfiles.Kind.PET;
        updateWildCompanionMovement();
        if (!naturalWildlife) setPersistenceRequired();
        if (naturalWildlife && groupWildlife()) {
            WildlifeSpawnGroupData group = data instanceof WildlifeSpawnGroupData existing
                    && existing.profileId.equals(profile.id()) && existing.group.size() < WildlifeGroupPolicy.MAX_MEMBERS
                    ? existing : new WildlifeSpawnGroupData(profile.id(), blockPosition());
            if (duck()) {
                if (group.firstDuck == null) group.firstDuck = getUUID();
                else { duckParent = group.firstDuck; duckFamilyState.born(); }
                setPersistenceRequired();
                syncDuckState();
            }
            group.group.tryJoin(getUUID());
            wildlifeGroup = group.group;
            wildlifeAnchor = group.anchor;
            return group;
        }
        return inherited;
    }
    @Override public int getMaxSpawnClusterSize() {
        return piggyBank() ? 1 : groupWildlife() || profile != null && profile.wildlifeEligible() && profile.kind() == PeacefulProfiles.Kind.PET
                ? WildlifeGroupPolicy.MAX_MEMBERS : super.getMaxSpawnClusterSize();
    }
    @Override public boolean checkSpawnObstruction(LevelReader level) {
        if (duck() && level instanceof Level actual && DuckHabitat.surfaceWater(actual, blockPosition()))
            return DuckHabitat.safe(this, blockPosition());
        if (!profile.aquatic()) return super.checkSpawnObstruction(level);
        var box = getBoundingBox();
        if (box.getXsize() > 8 || box.getZsize() > 8) return false;
        int minX = BlockPos.containing(box.minX, box.minY, box.minZ).getX() >> 4;
        int maxX = BlockPos.containing(box.maxX, box.maxY, box.maxZ).getX() >> 4;
        int minZ = BlockPos.containing(box.minX, box.minY, box.minZ).getZ() >> 4;
        int maxZ = BlockPos.containing(box.maxX, box.maxY, box.maxZ).getZ() >> 4;
        for (int x = minX; x <= maxX; x++) for (int z = minZ; z <= maxZ; z++)
            if (!level.hasChunkAt(new BlockPos(x << 4, blockPosition().getY(), z << 4))) return false;
        BlockPos top = BlockPos.containing(getX(), box.maxY - .001, getZ());
        return level.getFluidState(blockPosition()).is(FluidTags.WATER) && level.getFluidState(top).is(FluidTags.WATER)
                && level.isUnobstructed(this) && level.noCollision(this);
    }
    @Override public boolean isPersistenceRequired() {
        // Includes manually created NoAI wildlife, whose first AI hook will never run.
        return owner != null || profile != null && profile.wildlifeEligible() && !naturalWildlife
                || super.isPersistenceRequired();
    }
    @Override public void tick() {
        super.tick();
        if (!level().isClientSide && birdWildlife()) BirdPerchController.afterMovement(this);
    }
    @Override protected void customServerAiStep() {
        super.customServerAiStep();
        if (woolyCow()) {
            woolyBreeding.tick();
            updateWoolyAge();
        }
        if (duck()) { duckFamilyState.tickAge(); syncDuckState(); }
        if (profile.wildlifeEligible() && !naturalWildlife) setPersistenceRequired();
        if (!groupWildlife()) return;
        if (wildlifeGroup == null) joinOrCreateLocalGroup();
        if (level().getGameTime() >= nextGroupScan) refreshLocalGroup();
    }
    private void joinOrCreateLocalGroup() {
        var neighbors = level().getEntitiesOfClass(DungeonsCreature.class,
                getBoundingBox().inflate(WildlifeGroupPolicy.JOIN_RADIUS), other -> other != this && sameWildlifeKind(other)
                        && distanceToSqr(other) <= WildlifeGroupPolicy.JOIN_RADIUS * WildlifeGroupPolicy.JOIN_RADIUS
                        && other.isAlive() && !other.isNoAi() && other.wildlifeGroup != null && other.wildlifeAnchor != null);
        neighbors.sort(Comparator.comparing(other -> other.wildlifeGroup.id()));
        for (var other : neighbors) {
            if (other.wildlifeGroup.tryJoin(getUUID())) {
                wildlifeGroup = other.wildlifeGroup;
                wildlifeAnchor = other.wildlifeAnchor;
                wildlifeAlarmUntil = other.wildlifeAlarmUntil;
                wildlifeThreat = other.wildlifeThreat;
                nextGroupScan = 0;
                return;
            }
        }
        wildlifeGroup = new WildlifeGroup(UUID.randomUUID());
        wildlifeGroup.tryJoin(getUUID());
        wildlifeAnchor = blockPosition().immutable();
    }
    private boolean sameWildlifeKind(DungeonsCreature other) {
        return other.groupWildlife() && other.owner == null && profile.id().equals(other.profile.id());
    }
    private boolean availableGroupMember(DungeonsCreature other) {
        return other.isAlive() && !other.isRemoved() && !other.isNoAi() && other.level() == level()
                && sameWildlifeKind(other) && wildlifeGroup != null && other.wildlifeGroup != null
                && wildlifeGroup.id().equals(other.wildlifeGroup.id()) && wildlifeGroup.slot(other.getUUID()) >= 0
                && distanceToSqr(other) <= WildlifeGroupPolicy.COHESION_RADIUS * WildlifeGroupPolicy.COHESION_RADIUS;
    }
    private void refreshLocalGroup() {
        nextGroupScan = level().getGameTime() + WildlifeGroupPolicy.REPLAN_TICKS;
        if (wildlifeGroup == null) return;
        var neighbors = level().getEntitiesOfClass(DungeonsCreature.class,
                getBoundingBox().inflate(WildlifeGroupPolicy.COHESION_RADIUS), other -> sameWildlifeKind(other)
                        && other.isAlive() && !other.isNoAi() && other.wildlifeGroup != null
                        && wildlifeGroup.id().equals(other.wildlifeGroup.id()));
        neighbors.sort(Comparator.comparing(Entity::getUUID));
        for (var other : neighbors) if (wildlifeGroup.merge(other.wildlifeGroup)) other.wildlifeGroup = wildlifeGroup;
        var present = new ArrayList<DungeonsCreature>();
        if (availableGroupMember(this)) present.add(this);
        for (var other : neighbors) if (other != this && availableGroupMember(other)) present.add(other);
        present.sort(Comparator.comparing(Entity::getUUID));
        nearbyGroup = List.copyOf(present.subList(0, Math.min(present.size(), WildlifeGroupPolicy.MAX_MEMBERS)));
    }
    @Override public boolean hurt(DamageSource source, float amount) {
        float before = getHealth() + getAbsorptionAmount();
        boolean accepted = super.hurt(source, amount);
        if (accepted && !level().isClientSide && piggyBank() && owner == null && isAlive() && !isNoAi()
                && getHealth() + getAbsorptionAmount() < before) {
            piggyAlarmUntil = level().getGameTime() + PiggyBankEscapeGoal.FLEE_TICKS;
            piggyEscapeOrigin = blockPosition().immutable();
            piggyThreat = source.getSourcePosition() == null ? position() : source.getSourcePosition();
            navigation.stop();
        }
        if (accepted && !level().isClientSide && groupWildlife() && !isNoAi() && getHealth() + getAbsorptionAmount() < before) {
            if (wildlifeGroup == null) joinOrCreateLocalGroup();
            refreshLocalGroup();
            long until = level().getGameTime() + WildlifeGroupPolicy.ALARM_TICKS;
            Vec3 threat = source.getSourcePosition();
            wildlifeThreat = threat == null ? position() : threat;
            wildlifeAlarmUntil = until;
            if (birdWildlife()) setBirdPerched(false);
            for (var other : nearbyGroup) {
                if (!availableGroupMember(other)) continue;
                other.wildlifeAlarmUntil = until;
                other.wildlifeThreat = wildlifeThreat;
                if (other.birdWildlife()) other.setBirdPerched(false);
                other.woolyBreeding.interruptLove();
                other.navigation.stop();
            }
            woolyBreeding.interruptLove();
            navigation.stop();
        }
        return accepted;
    }
    @Override public InteractionResult mobInteract(Player player, InteractionHand hand) {
        if (civilian()) return interactWithCivilian(player, hand);
        if (woolyCow() && player.getItemInHand(hand).is(Items.WHEAT)) {
            if (!calmWoolyCow() || player.level() != level() || player.isSpectator() || !player.isAlive()
                    || distanceToSqr(player) > 25 || !player.hasLineOfSight(this)) return InteractionResult.PASS;
            if (level().isClientSide) return InteractionResult.SUCCESS;
            if (!woolyBreeding.feed()) return InteractionResult.PASS;
            if (!player.getAbilities().instabuild) player.getItemInHand(hand).shrink(1);
            // Deliberately cared-for wildlife and its future calves survive ordinary despawn.
            setPersistenceRequired();
            updateWoolyAge();
            navigation.stop();
            playSound(SoundEvents.COW_AMBIENT, .6F, isBaby() ? 1.5F : 1F);
            return InteractionResult.CONSUME;
        }
        if (profile.kind()!=PeacefulProfiles.Kind.PET && profile.kind()!=PeacefulProfiles.Kind.KEY_GOLEM)
            return super.mobInteract(player,hand);
        if (naturalCompanion && (hand != InteractionHand.MAIN_HAND || !isAlive() || !player.isAlive()
                || player.isSpectator() || player.level() != level() || distanceToSqr(player) > 25
                || !player.hasLineOfSight(this))) return InteractionResult.PASS;
        if (!level().isClientSide && (owner==null || owner.equals(player.getUUID()))) {
            if (owner==null) {
                owner=player.getUUID(); entityData.set(WAITING,false);
                if (naturalCompanion) {
                    // Explicit care ends the wild role. The old flock keeps its bounded
                    // reservation, but this animal can no longer lead, alarm or follow it.
                    naturalCompanion=false; naturalWildlife=false;
                    wildlifeGroup=null; wildlifeAnchor=null; nearbyGroup=List.of(); nextGroupScan=0;
                    wildlifeAlarmUntil=Long.MIN_VALUE; wildlifeThreat=Vec3.ZERO;
                    setPersistenceRequired();
                    updateWildCompanionMovement();
                }
            }
            else entityData.set(WAITING,!waiting());
            updateWaitingPose();
            navigation.stop();
            player.displayClientMessage(Component.translatable(waiting()?"message.dungeonsalive.pet_wait":"message.dungeonsalive.pet_follow",getDisplayName()),true);
        }
        return InteractionResult.sidedSuccess(level().isClientSide);
    }
    private InteractionResult interactWithCivilian(Player player, InteractionHand hand) {
        if (hand != InteractionHand.MAIN_HAND || !isAlive() || player.isSpectator()) return InteractionResult.PASS;
        if (!level().isClientSide) {
            // A direct interaction is sufficient in this sandbox adaptation. No cage blocks,
            // trades, inventories, rewards or unrelated player's progress are changed.
            if (distanceToSqr(player) > 25 || !player.hasLineOfSight(this)) return InteractionResult.FAIL;
            if (!rescued()) {
                owner = player.getUUID();
                entityData.set(RESCUED, true);
                entityData.set(WAITING, false);
                entityData.set(RESCUE_BEGIN, level().getGameTime());
                navigation.stop();
                playSound(SoundEvents.VILLAGER_YES, .8F, 1F);
                player.displayClientMessage(Component.translatableWithFallback("message.dungeonsalive.civilian_rescued", "%s ist befreit und folgt dir.", getDisplayName()), true);
            } else if (owner != null && owner.equals(player.getUUID())) {
                entityData.set(WAITING, !waiting());
                navigation.stop();
                player.displayClientMessage(Component.translatableWithFallback(
                        waiting() ? "message.dungeonsalive.civilian_wait" : "message.dungeonsalive.civilian_follow",
                        waiting() ? "%s wartet hier." : "%s folgt dir.", getDisplayName()), true);
            } else {
                player.displayClientMessage(Component.translatableWithFallback("message.dungeonsalive.civilian_other_rescuer", "%s reist bereits mit einem anderen Retter.", getDisplayName()), true);
            }
        }
        return InteractionResult.sidedSuccess(level().isClientSide);
    }
    @Override public boolean canDrownInFluidType(net.neoforged.neoforge.fluids.FluidType type) {
        return duckFamily() || profile!=null && (profile.aquatic() || profile.amphibious()) ? false : super.canDrownInFluidType(type);
    }
    @Override public void travel(Vec3 input) {
        if (duckFamily() && isControlledByLocalInstance() && isInWater()) {
            // Real surface swimming: movement uses the normal server MoveControl;
            // buoyancy affects velocity only, never entity position or water blocks.
            moveRelative(.1F, input);
            BlockPos surface = blockPosition();
            for (int i = 0; i < 3 && WildlifeNavigationSafety.loadedArea(level(), surface, 2)
                    && level().getFluidState(surface.above()).is(FluidTags.WATER); i++) surface = surface.above();
            double lift = DuckHabitat.surfaceWater(level(), surface)
                    ? net.minecraft.util.Mth.clamp((surface.getY() + .8 - getY()) * .18, -.08, .08) : .04;
            // Keep a real JumpControl/water-exit impulse. Replacing Y with zero
            // buoyancy at the waterline traps an airborne swimmer against a bank:
            // ordinary vanilla step-up requires actual ground/falling contact.
            setDeltaMovement(getDeltaMovement().x, Math.max(lift, getDeltaMovement().y), getDeltaMovement().z);
            double previousY = getY();
            move(MoverType.SELF, getDeltaMovement());
            setDeltaMovement(getDeltaMovement().multiply(.8, .8, .8));
            var path = navigation.getPath();
            if (horizontalCollision && path != null && !path.isDone()) {
                BlockPos next = path.getNextNodePos();
                double rise = next.getY() - getY();
                Vec3 motion = getDeltaMovement();
                if (rise > 0 && rise <= maxUpStep() && !DuckHabitat.surfaceWater(level(), next)
                        && DuckHabitat.safe(this, next)
                        && isFree(motion.x, motion.y + .6 - getY() + previousY, motion.z))
                    setDeltaMovement(motion.x, .3, motion.z);
            }
        } else super.travel(input);
    }
    @Override public boolean causeFallDamage(float distance,float multiplier,DamageSource source) {
        return profile!=null && profile.flying() ? false : super.causeFallDamage(distance,multiplier,source);
    }
    @Override public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag); if(owner!=null) tag.putUUID("DungeonsKeeper",owner); tag.putBoolean("DungeonsWaiting",waiting());
        if (birdWildlife() && wildlifeGroup != null) birdRoutine.save(tag, wildlifeGroup.id(), level().dimension().location().toString());
        if (civilian()) tag.putBoolean("DungeonsCivilianRescued", rescued());
        if (piggyBank()) {
            piggyTreasure.prepare(getUUID());
            CompoundTag treasure = new CompoundTag();
            treasure.putInt("Emeralds", piggyTreasure.emeralds()); treasure.putInt("Gear", piggyTreasure.gearIndex());
            treasure.putBoolean("Spent", piggyTreasure.spent()); tag.put("DungeonsPiggyTreasure", treasure);
        }
        if (woolyCow()) {
            CompoundTag cow = new CompoundTag();
            cow.putInt("Age", woolyBreeding.age()); cow.putInt("InLove", woolyBreeding.loveTicks());
            if (woolyParentA != null) cow.putUUID("ParentA", woolyParentA);
            if (woolyParentB != null) cow.putUUID("ParentB", woolyParentB);
            tag.put("DungeonsWoolyCow", cow);
        }
        if (duck() && (duckFamily() || duckParent != null || duckFamilyState.young())) {
            CompoundTag family = new CompoundTag();
            family.putInt("YoungTicks", duckFamilyState.youngTicks());
            family.putInt("Activity", duckFamilyState.activity().ordinal()); family.putInt("Ticks", duckFamilyState.ticks());
            if (duckParent != null) family.putUUID("Parent", duckParent);
            tag.put("DungeonsDuckFamily", family);
        }
        if (profile.wildlifeEligible()) {
            tag.putBoolean("DungeonsNaturalWildlife", naturalWildlife);
            tag.putBoolean("PersistenceRequired", isPersistenceRequired());
            if (profile.kind() == PeacefulProfiles.Kind.PET) tag.putBoolean("DungeonsWildCompanion", naturalCompanion);
        }
        if (wildlifeGroup != null && wildlifeAnchor != null) {
            CompoundTag group = new CompoundTag();
            group.putUUID("Id", wildlifeGroup.id());
            group.putString("Dimension", level().dimension().location().toString());
            group.putInt("X", wildlifeAnchor.getX()); group.putInt("Y", wildlifeAnchor.getY()); group.putInt("Z", wildlifeAnchor.getZ());
            ListTag members = new ListTag();
            for (UUID member : wildlifeGroup.members()) {
                CompoundTag entry = new CompoundTag(); entry.putUUID("Id", member); members.add(entry);
            }
            group.put("Members", members);
            tag.put("DungeonsWildlifeGroup", group);
        }
    }
    @Override public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag); owner=tag.hasUUID("DungeonsKeeper")?tag.getUUID("DungeonsKeeper"):null;
        naturalCompanion = owner == null && profile.kind() == PeacefulProfiles.Kind.PET && profile.wildlifeEligible()
                && tag.getBoolean("DungeonsWildCompanion") && tag.getBoolean("DungeonsNaturalWildlife");
        naturalWildlife = owner == null && (profile.kind() == PeacefulProfiles.Kind.WILDLIFE || naturalCompanion)
                && tag.getBoolean("DungeonsNaturalWildlife");
        if (piggyBank()) {
            CompoundTag treasure = tag.getCompound("DungeonsPiggyTreasure");
            piggyTreasure.restore(getUUID(), treasure.getInt("Emeralds"), treasure.getInt("Gear"), treasure.getBoolean("Spent"));
        }
        piggyAlarmUntil = Long.MIN_VALUE; piggyEscapeOrigin = null; piggyThreat = Vec3.ZERO;
        CompoundTag cow = woolyCow() ? tag.getCompound("DungeonsWoolyCow") : new CompoundTag();
        woolyBreeding.restore(cow.getInt("Age"), cow.getInt("InLove"));
        woolyParentA = cow.hasUUID("ParentA") ? cow.getUUID("ParentA") : null;
        woolyParentB = cow.hasUUID("ParentB") ? cow.getUUID("ParentB") : null;
        updateWoolyAge();
        CompoundTag family = duck() ? tag.getCompound("DungeonsDuckFamily") : new CompoundTag();
        duckFamilyState.restore(family.getInt("YoungTicks"), family.getInt("Activity"), family.getInt("Ticks"));
        duckParent = family.hasUUID("Parent") ? family.getUUID("Parent") : null;
        duckDestination = null;
        syncDuckState();
        entityData.set(DUCK_BEGIN, level().getGameTime() - duckFamilyState.ticks());
        wildlifeGroup = null; wildlifeAnchor = null; nearbyGroup = List.of(); nextGroupScan = 0;
        wildlifeAlarmUntil = Long.MIN_VALUE; wildlifeThreat = Vec3.ZERO;
        if (groupWildlife() && tag.contains("DungeonsWildlifeGroup", Tag.TAG_COMPOUND)) {
            CompoundTag group = tag.getCompound("DungeonsWildlifeGroup");
            if (group.hasUUID("Id") && group.getString("Dimension").equals(level().dimension().location().toString())
                    && group.contains("X", Tag.TAG_INT) && group.contains("Y", Tag.TAG_INT) && group.contains("Z", Tag.TAG_INT)) {
                BlockPos anchor = new BlockPos(group.getInt("X"), group.getInt("Y"), group.getInt("Z"));
                WildlifeGroup restored = new WildlifeGroup(group.getUUID("Id"));
                ListTag members = group.getList("Members", Tag.TAG_COMPOUND);
                for (int i = 0; i < Math.min(members.size(), WildlifeGroupPolicy.MAX_MEMBERS); i++) {
                    CompoundTag entry = members.getCompound(i);
                    if (entry.hasUUID("Id")) restored.tryJoin(entry.getUUID("Id"));
                }
                if (anchor.getY() >= level().getMinBuildHeight() && anchor.getY() < level().getMaxBuildHeight()
                        && level().getWorldBorder().isWithinBounds(anchor) && restored.tryJoin(getUUID())) {
                    wildlifeGroup = restored; wildlifeAnchor = anchor;
                }
            }
        }
        entityData.set(WAITING,tag.getBoolean("DungeonsWaiting"));
        if (civilian()) {
            entityData.set(RESCUED, tag.getBoolean("DungeonsCivilianRescued"));
            // Old/unrescued saves cannot accidentally release a civilian while loading.
            if (!rescued()) { owner = null; entityData.set(WAITING, true); }
            entityData.set(RESCUE_BEGIN, -1000L);
        }
        updateWaitingPose();
        updateWildCompanionMovement();
        birdRoutine.clear();
        setBirdPerched(false);
        if (birdWildlife() && wildlifeGroup != null)
            birdRoutine.load(tag, wildlifeGroup.id(), level().dimension().location().toString(), level().getGameTime());
    }
    private void updateWildCompanionMovement() {
        boolean wild = naturalCompanion && owner == null;
        if (!wild && usingWildCompanionMovement && profile.wildBirdEligible()) {
            birdRoutine.clear(); setBirdPerched(false); setNoGravity(true);
        }
        if (wild == usingWildCompanionMovement) return;
        navigation.stop();
        if (wild && profile.flying()) {
            navigation = new WildlifeFlightNavigation(this, level());
            moveControl = new WildlifeFlightMoveControl(this);
            lookControl = new LookControl(this) {
                @Override protected boolean resetXRotOnTick() { return navigation.isDone(); }
            };
            setNoGravity(true);
        } else if (wild && duck()) {
            navigation = new DuckFamilyNavigation(this, level());
        } else if (wild && profile.aquatic()) {
            navigation = new WildlifeWaterNavigation(this, level());
        } else if (wild) {
            navigation = new WildlifeGroundNavigation(this, level());
        } else {
            navigation = companionNavigation;
            moveControl = companionMoveControl;
            lookControl = companionLookControl;
        }
        navigation.stop();
        usingWildCompanionMovement = wild;
        syncDuckState();
    }
    void syncDuckState() {
        boolean family = duck() && naturalCompanion && owner == null;
        entityData.set(DUCK_FAMILY, family);
        boolean young = duck() && duckFamilyState.young();
        entityData.set(DUCK_YOUNG, young);
        int activity = (family ? duckFamilyState.activity() : DuckFamilyState.Activity.REST).ordinal();
        if (entityData.get(DUCK_ACTIVITY) != activity) {
            entityData.set(DUCK_ACTIVITY, activity);
            entityData.set(DUCK_BEGIN, level().getGameTime() - duckFamilyState.ticks());
        }
        var scale = getAttribute(Attributes.SCALE);
        if (scale == null) return;
        boolean scaled = scale.getModifier(DUCK_YOUNG_SCALE) != null;
        if (young == scaled) return;
        if (young) scale.addTransientModifier(new AttributeModifier(DUCK_YOUNG_SCALE, -.45, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
        else scale.removeModifier(DUCK_YOUNG_SCALE);
        refreshDimensions();
    }
    private void updateWoolyAge() {
        boolean baby = woolyCow() && woolyBreeding.baby();
        entityData.set(WOOLY_BABY, baby);
        var scale = getAttribute(Attributes.SCALE);
        if (scale == null) return;
        boolean scaled = scale.getModifier(WOOLY_BABY_SCALE) != null;
        if (baby == scaled) return;
        if (baby) scale.addTransientModifier(new AttributeModifier(WOOLY_BABY_SCALE, -.5, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
        else scale.removeModifier(WOOLY_BABY_SCALE);
        // SCALE is synchronized by vanilla and changes both the living renderer and physical body.
        // A transient modifier is reconstructed from age on reload, leaving external scale values intact.
        refreshDimensions();
    }
    @Override protected void dropFromLootTable(DamageSource source, boolean recentlyHit) {
        super.dropFromLootTable(source, recentlyHit);
        if (piggyBank()) PiggyBankTreasure.drop(this, source, piggyTreasure);
    }

    boolean tryWoolyBirth(DungeonsCreature mate) {
        if (!(level() instanceof ServerLevel server) || !canCourt(mate) || distanceToSqr(mate) > 4
                || !hasLineOfSight(mate)) return false;
        if (wildlifeGroup == null) joinOrCreateLocalGroup();
        if (mate.wildlifeGroup == null) mate.joinOrCreateLocalGroup();
        // A calf occupies an actual free reservation in either parent's existing herd. Full
        // rosters include unloaded members: breeding cannot silently overflow them or load them.
        DungeonsCreature family = wildlifeGroup.size() < WildlifeGroupPolicy.MAX_MEMBERS ? this : mate;
        WildlifeGroup familyGroup = family.wildlifeGroup;
        int population = server.getEntitiesOfClass(DungeonsCreature.class,
                getBoundingBox().inflate(WoolyCowBreedingState.POPULATION_RADIUS),
                other -> other.isAlive() && !other.isRemoved() && other.profile.kind() == PeacefulProfiles.Kind.WILDLIFE
                        && distanceToSqr(other) <= (double)WoolyCowBreedingState.POPULATION_RADIUS * WoolyCowBreedingState.POPULATION_RADIUS).size();
        if (!WoolyCowBreedingState.roomForBirth(population, familyGroup.size())) return false;
        DungeonsCreature child = ModPeacefulEntities.creatureType("wooly_cow").create(server);
        if (child == null) return false;
        child.woolyBreeding.born();
        child.updateWoolyAge();
        child.woolyParentA = getUUID(); child.woolyParentB = mate.getUUID();
        child.setPersistenceRequired();
        BlockPos birth = woolyBirthPosition(child, mate);
        if (birth == null) { child.discard(); return false; }
        child.setPos(Vec3.atBottomCenterOf(birth));
        child.wildlifeGroup = familyGroup;
        child.wildlifeAnchor = family.wildlifeAnchor;
        // Commit the reservation and parents' pause only after the actual entity was accepted.
        if (!server.addFreshEntity(child)) { child.discard(); return false; }
        if (!familyGroup.tryJoin(child.getUUID())) { child.discard(); return false; }
        woolyBreeding.bred(); mate.woolyBreeding.bred();
        setPersistenceRequired(); mate.setPersistenceRequired();
        navigation.stop(); mate.navigation.stop();
        nextGroupScan = 0; mate.nextGroupScan = 0; child.nextGroupScan = 0;
        return true;
    }

    @Nullable private BlockPos woolyBirthPosition(DungeonsCreature child, DungeonsCreature mate) {
        BlockPos center = BlockPos.containing(position().add(mate.position()).scale(.5));
        for (int radius = 0; radius <= 2; radius++) {
            for (int dx = -radius; dx <= radius; dx++) for (int dz = -radius; dz <= radius; dz++) {
                if (Math.max(Math.abs(dx), Math.abs(dz)) != radius) continue;
                for (int dy : new int[]{0, 1, -1}) {
                    BlockPos candidate = center.offset(dx, dy, dz);
                    if (WildlifeNavigationSafety.safeGround(child, candidate)) return candidate;
                }
            }
        }
        return null;
    }
    @Override @Nullable protected SoundEvent getAmbientSound() { return civilian() ? SoundEvents.VILLAGER_AMBIENT : super.getAmbientSound(); }
    @Override protected SoundEvent getHurtSound(DamageSource source) { return civilian() ? SoundEvents.VILLAGER_HURT : super.getHurtSound(source); }
    @Override protected SoundEvent getDeathSound() { return civilian() ? SoundEvents.VILLAGER_DEATH : super.getDeathSound(); }
    private void updateWaitingPose() {
        boolean canSit=profile!=null && (profile.host().equals("cat") || profile.host().equals("fox"));
        setPose(canSit && waiting() ? Pose.SITTING : Pose.STANDING);
    }
    private final class FollowKeeperGoal extends Goal {
        private Player keeper;
        FollowKeeperGoal(){setFlags(EnumSet.of(Flag.MOVE,Flag.LOOK));}
        @Override public boolean canUse(){
            keeper=owner==null?null:level().getPlayerByUUID(owner);
            return keeper!=null && !keeper.isSpectator() && !waiting() && distanceToSqr(keeper)>9;
        }
        @Override public boolean canContinueToUse(){return keeper!=null && keeper.isAlive() && !waiting() && distanceToSqr(keeper)>4 && distanceToSqr(keeper)<576;}
        // Navigation's ten-tick interval must see both tick parities. The default
        // alternating goal scheduler otherwise starves keepers with an odd entity ID.
        @Override public boolean requiresUpdateEveryTick(){return true;}
        @Override public void tick(){
            if(keeper==null)return;
            getLookControl().setLookAt(keeper,20,20);
            if(tickCount%10==0) navigation.moveTo(keeper,1.1);
        }
        @Override public void stop(){navigation.stop();keeper=null;}
    }
    private final class WanderGoal extends Goal {
        private double x,y,z;
        WanderGoal(){setFlags(EnumSet.of(Flag.MOVE));}
        @Override public boolean canUse(){
            if(profile==null || groupWildlife() || waiting() || random.nextInt(65)!=0) return false;
            x=getX()+(random.nextDouble()-.5)*10;
            z=getZ()+(random.nextDouble()-.5)*10;
            y=getY()+(profile.flying()||profile.aquatic()?(random.nextDouble()-.5)*4:0);
            if(!level().hasChunkAt(net.minecraft.core.BlockPos.containing(x,y,z))) return false;
            if(profile.aquatic() && !level().getFluidState(net.minecraft.core.BlockPos.containing(x,y,z)).is(FluidTags.WATER)) return false;
            if(profile.flying()) y=Math.max(y,level().getHeight(net.minecraft.world.level.levelgen.Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,net.minecraft.util.Mth.floor(x),net.minecraft.util.Mth.floor(z))+1.0);
            return true;
        }
        @Override public void start(){navigation.moveTo(x,y,z,.75);}
        @Override public boolean canContinueToUse(){return !waiting()&&!navigation.isDone();}
        @Override public void stop(){navigation.stop();}
    }
    private static final class WildlifeSpawnGroupData implements SpawnGroupData {
        private final String profileId;
        private final WildlifeGroup group = new WildlifeGroup(UUID.randomUUID());
        private final BlockPos anchor;
        @Nullable private UUID firstDuck;
        private WildlifeSpawnGroupData(String profileId, BlockPos anchor) {
            this.profileId = profileId;
            this.anchor = anchor.immutable();
        }
    }
}
