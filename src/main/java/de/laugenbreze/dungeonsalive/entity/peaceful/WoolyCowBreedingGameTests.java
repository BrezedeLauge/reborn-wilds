package de.laugenbreze.dungeonsalive.entity.peaceful;

import de.laugenbreze.dungeonsalive.DungeonsAlive;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.AfterBatch;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

/** Real feeding, AI courtship, birth admission and growth; no artificial animal or navigation stubs. */
@GameTestHolder(DungeonsAlive.MOD_ID)
@PrefixGameTestTemplate(false)
public final class WoolyCowBreedingGameTests {
    // Separate batches keep the deliberate density fixture out of the positive birth test.
    private static final String LIFE = "wooly_cow_lifecycle";
    private static final String LIMIT = "wooly_cow_limits";
    private static final String INTERACTION = "wooly_cow_interaction";
    private static final Map<ServerLevel, Set<Entity>> ACTORS = new IdentityHashMap<>();
    private static final Map<ServerLevel, Set<ChunkPos>> CHUNKS = new IdentityHashMap<>();
    private WoolyCowBreedingGameTests() {}

    @AfterBatch(batch = LIFE) public static void cleanLifecycle(ServerLevel level) { cleanup(level); }
    @AfterBatch(batch = LIMIT) public static void cleanLimits(ServerLevel level) { cleanup(level); }
    @AfterBatch(batch = INTERACTION) public static void cleanInteraction(ServerLevel level) { cleanup(level); }
    private static void cleanup(ServerLevel level) {
        var actors = ACTORS.remove(level);
        if (actors != null) for (var actor : actors) actor.discard();
        var chunks = CHUNKS.remove(level);
        if (chunks != null) for (var chunk : chunks) level.setChunkForced(chunk.x, chunk.z, false);
    }

    @GameTest(template = "gametest_empty", templateNamespace = DungeonsAlive.MOD_ID, batch = LIFE, timeoutTicks = 200)
    public static void actualWheatCourtshipProducesOnePersistentCalfAndReloadedCalfGrows(GameTestHelper helper) {
        floor(helper); loadNavigation(helper);
        SpawnGroupData[] naturalPack = new SpawnGroupData[1];
        var first = spawn(helper, "wooly_cow", 4.5, 5.5, true, naturalPack);
        var second = spawn(helper, "wooly_cow", 10.5, 5.5, true, naturalPack);
        Player feeder = helper.makeMockPlayer(GameType.SURVIVAL);
        Vec3 firstStart = first.position(), secondStart = second.position();
        Vec3[] previous = {first.position(), second.position()};
        boolean[] sawPath = {false}, sawMovement = {false};
        DungeonsCreature[] following = new DungeonsCreature[1];
        Vec3[] previousCalf = new Vec3[1];
        double[] initialParentDistance = new double[1];
        boolean[] sawParentPath = {false};
        DungeonsCreature[] restored = new DungeonsCreature[1];
        helper.onEachTick(() -> {
            trackChildren(helper, first);
            if (restored[0] != null) return;
            if (following[0] != null) {
                sawParentPath[0] |= !following[0].getNavigation().isDone();
                helper.assertTrue(following[0].position().distanceToSqr(previousCalf[0]) < 1,
                        "A separated calf teleported toward its parent");
                previousCalf[0] = following[0].position();
                return;
            }
            sawPath[0] |= !first.getNavigation().isDone() || !second.getNavigation().isDone();
            sawMovement[0] |= first.distanceToSqr(firstStart) > 1 || second.distanceToSqr(secondStart) > 1;
            helper.assertTrue(first.position().distanceToSqr(previous[0]) < 1
                            && second.position().distanceToSqr(previous[1]) < 1,
                    "Courtship teleported a parent instead of walking");
            previous[0] = first.position(); previous[1] = second.position();
        });
        helper.runAtTickTime(4, () -> {
            helper.assertTrue(!first.isPersistenceRequired() && !second.isPersistenceRequired(),
                    "Natural parent fixtures were already forced persistent before care");
            feed(helper, first, feeder); feed(helper, second, feeder);
            helper.assertTrue(first.woolyInLove() && second.woolyInLove()
                            && first.isPersistenceRequired() && second.isPersistenceRequired(),
                    "Actual wheat use failed to start love or retain cared-for natural animals");
        });
        helper.runAtTickTime(120, () -> {
            var children = children(first);
            helper.assertTrue(children.size() == 1, "The real courtship has not produced its one calf");
            following[0] = children.getFirst(); track(helper, following[0]);
            // Fixed parents and one deliberate fixture separation isolate the calf's actual
            // parent-following goal. Subsequent movement must come from ordinary navigation.
            first.removeFreeWill(); second.removeFreeWill();
            first.getNavigation().stop(); second.getNavigation().stop();
            first.setPos(helper.absoluteVec(new Vec3(6.5, 1, 5.5)));
            second.setPos(helper.absoluteVec(new Vec3(8.5, 1, 5.5)));
            first.setDeltaMovement(Vec3.ZERO); second.setDeltaMovement(Vec3.ZERO);
            following[0].getNavigation().stop();
            following[0].setPos(helper.absoluteVec(new Vec3(16.5, 1, 5.5)));
            following[0].setDeltaMovement(Vec3.ZERO);
            // A freshly placed calf is briefly not grounded. Ordinary physics
            // must land it, then navigation must proceed without a false
            // sixty-tick failed-search penalty. No movement is injected after this.
            following[0].setOnGround(false);
            previousCalf[0] = following[0].position();
            initialParentDistance[0] = following[0].distanceToSqr(second);
        });
        helper.runAtTickTime(160, () -> {
            var children = children(first);
            helper.assertTrue(sawPath[0] && sawMovement[0] && children.size() == 1,
                    "The real goals did not approach and produce exactly one calf");
            helper.assertTrue(sawParentPath[0] && following[0].distanceToSqr(second) < initialParentDistance[0] / 2,
                    "The separated calf did not actually walk back toward a saved parent: saw path=" + sawParentPath[0]
                            + ", initial distance squared=" + initialParentDistance[0] + ", calf=" + movementState(following[0])
                            + ", first=" + movementState(first) + ", second=" + movementState(second));
            var calf = children.getFirst(); track(helper, calf);
            helper.assertTrue(calf.getType() == first.getType() && calf.isBaby() && calf.woolyAgeTicks() < 0
                            && calf.isPersistenceRequired() && calf.isWoolyParent(first) && calf.isWoolyParent(second)
                            && first.wildlifeGroupId().equals(calf.wildlifeGroupId())
                            && first.wildlifeAnchor().equals(calf.wildlifeAnchor()),
                    "Birth lost the real species, juvenile age, persistence, parents or herd binding");
            helper.assertTrue(Math.abs(calf.getBbWidth() - first.getBbWidth() * .5F) < .001F
                            && Math.abs(calf.getBbHeight() - first.getBbHeight() * .5F) < .001F
                            && Math.abs(calf.getScale() - .5F) < .001F
                            && !calf.wildlifeLeader().isBaby(), "Calf body/scale or adult herd leadership is wrong");
            helper.assertTrue(first.woolyAgeTicks() > 0 && second.woolyAgeTicks() > 0
                            && !first.woolyInLove() && !second.woolyInLove(), "A successful birth did not pause both parents");
            rejectFeed(helper, first, feeder);
            int youngBeforeFood = calf.woolyAgeTicks();
            feed(helper, calf, feeder);
            helper.assertTrue(calf.woolyAgeTicks() > youngBeforeFood && calf.isBaby() && !calf.woolyInLove(),
                    "Calf feeding did not advance growth independently of breeding");
            CompoundTag saved = save(calf);
            int savedAge = calf.woolyAgeTicks(); UUID identity = calf.getUUID(); UUID group = calf.wildlifeGroupId();
            calf.discard();
            restored[0] = ModPeacefulEntities.creatureType("wooly_cow").create(helper.getLevel());
            helper.assertTrue(restored[0] != null, "Missing real cow type during full NBT reload");
            track(helper, restored[0]); restored[0].load(saved);
            helper.assertTrue(helper.getLevel().addFreshEntity(restored[0]), "Calf full NBT reload was rejected");
            helper.assertTrue(identity.equals(restored[0].getUUID()) && group.equals(restored[0].wildlifeGroupId())
                            && restored[0].woolyAgeTicks() == savedAge && restored[0].isBaby()
                            && restored[0].isWoolyParent(first) && restored[0].isWoolyParent(second)
                            && Math.abs(restored[0].getScale() - .5F) < .001F && restored[0].isPersistenceRequired(),
                    "Full save/load lost age, scale, identity, family or persistence");
            // An explicitly near-mature saved fixture tests the real server transition without
            // requiring a twenty-minute test. The full original age was checked immediately above.
            CompoundTag nearMature = save(restored[0]);
            nearMature.getCompound("DungeonsWoolyCow").putInt("Age", -2);
            restored[0].readAdditionalSaveData(nearMature);
        });
        helper.runAtTickTime(170, () -> {
            helper.assertTrue(restored[0] != null && !restored[0].isBaby() && restored[0].woolyAgeTicks() == 0
                            && Math.abs(restored[0].getScale() - 1F) < .001F
                            && Math.abs(restored[0].getBbWidth() - first.getBbWidth()) < .001F
                            && children(first).size() == 1 && !restored[0].woolyInLove(),
                    "Actual age ticks did not restore adult dimensions exactly once");
            helper.succeed();
        });
    }

    @GameTest(template = "gametest_empty", templateNamespace = DungeonsAlive.MOD_ID, batch = LIMIT, timeoutTicks = 20)
    public static void savedReservationsDensityAndRejectedEntityJoinBoundRealBirths(GameTestHelper helper) {
        floor(helper);
        var first = spawn(helper, "wooly_cow", 6.5, 5.5, false);
        var second = spawn(helper, "wooly_cow", 8.3, 5.5, false);
        first.removeFreeWill(); second.removeFreeWill();
        Player feeder = helper.makeMockPlayer(GameType.SURVIVAL);
        helper.runAtTickTime(4, () -> {
            feed(helper, first, feeder); feed(helper, second, feeder);
            CompoundTag freeFirst = save(first), freeSecond = save(second);
            CompoundTag fullFirst = freeFirst.copy(), fullSecond = freeSecond.copy();
            var roster = fullFirst.getCompound("DungeonsWildlifeGroup").getList("Members", Tag.TAG_COMPOUND);
            while (roster.size() < WildlifeGroupPolicy.MAX_MEMBERS) {
                CompoundTag absent = new CompoundTag(); absent.putUUID("Id", UUID.randomUUID()); roster.add(absent);
            }
            fullSecond.put("DungeonsWildlifeGroup", fullFirst.getCompound("DungeonsWildlifeGroup").copy());
            first.readAdditionalSaveData(fullFirst); second.readAdditionalSaveData(fullSecond);
            helper.assertFalse(first.tryWoolyBirth(second), "Unloaded saved members were ignored when the herd was full");
            helper.assertTrue(children(first).isEmpty() && first.woolyInLove() && first.woolyAgeTicks() == 0,
                    "A rejected full-herd birth created a calf or spent the parent state");
            first.readAdditionalSaveData(freeFirst); second.readAdditionalSaveData(freeSecond);
            int alreadyPresent = helper.getLevel().getEntitiesOfClass(DungeonsCreature.class,
                    first.getBoundingBox().inflate(48), cow -> cow.isAlive()
                            && cow.profile().kind() == PeacefulProfiles.Kind.WILDLIFE && cow.distanceToSqr(first) <= 48 * 48).size();
            helper.assertTrue(alreadyPresent < WoolyCowBreedingState.LOCAL_POPULATION_LIMIT,
                    "Unrelated wildlife already filled this test's population fixture");
            List<DungeonsCreature> density = new ArrayList<>();
            for (int i = alreadyPresent; i < WoolyCowBreedingState.LOCAL_POPULATION_LIMIT; i++) {
                var extra = spawn(helper, "piggy_bank", 16.5, 9.5, false); extra.setNoAi(true); density.add(extra);
            }
            helper.assertFalse(first.tryWoolyBirth(second), "Loaded wildlife density did not prevent a real birth");
            density.forEach(Entity::discard);
            AtomicInteger canceled = new AtomicInteger();
            Consumer<EntityJoinLevelEvent> reject = event -> {
                if (event.getEntity() instanceof DungeonsCreature child && child.isBaby() && child.isWoolyParent(first)) {
                    canceled.incrementAndGet(); event.setCanceled(true);
                }
            };
            NeoForge.EVENT_BUS.addListener(EventPriority.HIGHEST, reject);
            try {
                helper.assertFalse(first.tryWoolyBirth(second), "Canceled EntityJoin still completed a birth");
            } finally { NeoForge.EVENT_BUS.unregister(reject); }
            helper.assertTrue(canceled.get() == 1 && children(first).isEmpty() && first.woolyInLove() && second.woolyInLove()
                            && save(first).getCompound("DungeonsWildlifeGroup").getList("Members", Tag.TAG_COMPOUND).size() == 2,
                    "Rejected spawning spent a herd reservation or breeding state");
            helper.assertTrue(first.tryWoolyBirth(second), "A valid actual birth remained blocked after removing the rejection");
            trackChildren(helper, first);
            helper.assertTrue(children(first).size() == 1 && first.woolyAgeTicks() == WoolyCowBreedingState.BREEDING_PAUSE_TICKS
                            && second.woolyAgeTicks() == WoolyCowBreedingState.BREEDING_PAUSE_TICKS
                            && !first.tryWoolyBirth(second), "One successful birth was duplicated or failed to pause both parents");
            helper.succeed();
        });
    }

    @GameTest(template = "gametest_empty", templateNamespace = DungeonsAlive.MOD_ID, batch = INTERACTION, timeoutTicks = 160)
    public static void damageInterruptsLoveWhileOldSavesNoAiAndOwnedPetsKeepTheirRules(GameTestHelper helper) {
        floor(helper);
        var cow = spawn(helper, "wooly_cow", 4.5, 5.5, false); cow.removeFreeWill();
        var pausedCalf = spawn(helper, "wooly_cow", 8.5, 5.5, false);
        CompoundTag young = save(pausedCalf), state = new CompoundTag(); state.putInt("Age", -10);
        young.put("DungeonsWoolyCow", state); pausedCalf.readAdditionalSaveData(young); pausedCalf.setNoAi(true);
        var owned = spawn(helper, "wooly_cow", 12.5, 5.5, false);
        var pet = spawn(helper, "pet_baby_wooly_cow", 16.5, 5.5, false);
        UUID keeper = UUID.randomUUID();
        for (var actor : List.of(owned, pet)) {
            CompoundTag saved = save(actor); saved.putUUID("DungeonsKeeper", keeper); actor.readAdditionalSaveData(saved);
        }
        Player feeder = helper.makeMockPlayer(GameType.SURVIVAL);
        helper.runAtTickTime(4, () -> {
            CompoundTag oldSave = save(cow); oldSave.remove("DungeonsWoolyCow"); cow.readAdditionalSaveData(oldSave);
            helper.assertTrue(!cow.isBaby() && cow.woolyAgeTicks() == 0 && Math.abs(cow.getScale() - 1) < .001,
                    "An old save without breeding data changed into a calf");
            rejectFeed(helper, pausedCalf, feeder); rejectFeed(helper, owned, feeder); rejectFeed(helper, pet, feeder);
            helper.assertTrue(keeper.equals(owned.ownerId()) && keeper.equals(pet.ownerId()) && !pet.woolyCow()
                            && !save(pet).contains("DungeonsWoolyCow") && pausedCalf.woolyAgeTicks() == -10,
                    "Feeding changed owner, pet identity, NoAI age or unrelated saved data");
            feed(helper, cow, feeder); rejectFeed(helper, cow, feeder);
            helper.assertTrue(cow.hurt(helper.getLevel().damageSources().generic(), 1), "Alarm fixture did not apply real damage");
            helper.assertTrue(!cow.woolyInLove() && cow.wildlifeActivity() == WildlifeGroupPolicy.Activity.FLEE,
                    "Accepted damage did not interrupt courtship for the actual herd alarm");
            rejectFeed(helper, cow, feeder);
        });
        helper.runAtTickTime(130, () -> {
            helper.assertTrue(!cow.woolyInLove() && cow.wildlifeActivity() != WildlifeGroupPolicy.Activity.FLEE
                            && pausedCalf.isBaby() && pausedCalf.woolyAgeTicks() == -10,
                    "Love restarted on its own, alarm did not expire, or NoAI aged the calf");
            feed(helper, cow, feeder);
            pausedCalf.setNoAi(false);
        });
        helper.runAtTickTime(145, () -> {
            helper.assertTrue(cow.woolyInLove() && !pausedCalf.isBaby() && pausedCalf.woolyAgeTicks() == 0
                            && Math.abs(pausedCalf.getScale() - 1) < .001,
                    "Restored active AI did not finish growth or the adult could not be fed after alarm");
            helper.succeed();
        });
    }

    private static void feed(GameTestHelper helper, DungeonsCreature cow, Player player) {
        player.setPos(cow.position().add(0, 0, 2));
        player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.WHEAT, 2));
        cow.mobInteract(player, InteractionHand.MAIN_HAND);
        helper.assertTrue(player.getMainHandItem().getCount() == 1, "Actual valid wheat interaction did not consume exactly one item");
    }
    private static void rejectFeed(GameTestHelper helper, DungeonsCreature cow, Player player) {
        player.setPos(cow.position().add(0, 0, 2));
        player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.WHEAT, 2));
        cow.mobInteract(player, InteractionHand.MAIN_HAND);
        helper.assertTrue(player.getMainHandItem().getCount() == 2, "Invalid/cooldown/owner wheat interaction consumed an item");
    }
    private static CompoundTag save(DungeonsCreature cow) { CompoundTag data = new CompoundTag(); cow.saveWithoutId(data); return data; }
    private static List<DungeonsCreature> children(DungeonsCreature parent) {
        return parent.level().getEntitiesOfClass(DungeonsCreature.class, parent.getBoundingBox().inflate(48),
                child -> child.isAlive() && child.isWoolyParent(parent));
    }
    private static void trackChildren(GameTestHelper helper, DungeonsCreature parent) {
        for (var child : children(parent)) track(helper, child);
    }
    private static void track(GameTestHelper helper, Entity actor) {
        ACTORS.computeIfAbsent(helper.getLevel(), ignored -> new HashSet<>()).add(actor);
    }
    private static String movementState(DungeonsCreature creature) {
        var path = creature.getNavigation().getPath();
        return creature.position() + ", motion=" + creature.getDeltaMovement() + ", calm=" + creature.calmWoolyCow()
                + ", ground=" + creature.onGround() + ", collision=" + creature.horizontalCollision
                + ", done=" + creature.getNavigation().isDone() + ", path="
                + (path == null ? "null" : path.getNextNodeIndex() + "/" + path.getNodeCount() + " to " + path.getTarget()
                + (path.isDone() ? "" : " next " + path.getNextNodePos()));
    }
    private static DungeonsCreature spawn(GameTestHelper helper, String id, double x, double z, boolean natural) {
        return spawn(helper, id, x, z, natural, null);
    }
    private static DungeonsCreature spawn(GameTestHelper helper, String id, double x, double z, boolean natural,
                                           SpawnGroupData[] pack) {
        var cow = ModPeacefulEntities.creatureType(id).create(helper.getLevel());
        helper.assertTrue(cow != null, "Missing actual creature factory: " + id);
        track(helper, cow); cow.setPos(helper.absoluteVec(new Vec3(x, 1, z)));
        if (natural) {
            SpawnGroupData group = cow.finalizeSpawn(helper.getLevel(), helper.getLevel().getCurrentDifficultyAt(cow.blockPosition()),
                    MobSpawnType.NATURAL, pack == null ? null : pack[0]);
            if (pack != null) pack[0] = group;
        }
        helper.assertTrue(helper.getLevel().addFreshEntity(cow), "Actual creature fixture could not join the level");
        return cow;
    }
    private static void floor(GameTestHelper helper) {
        for (int x = 0; x < 20; x++) for (int z = 0; z < 12; z++) helper.setBlock(new BlockPos(x, 0, z), Blocks.STONE);
    }
    private static void loadNavigation(GameTestHelper helper) {
        var level = helper.getLevel(); int radius = WildlifeNavigationSafety.LOADED_RADIUS;
        BlockPos min = helper.absolutePos(new BlockPos(-radius, 0, -radius));
        BlockPos max = helper.absolutePos(new BlockPos(19 + radius, 0, 11 + radius));
        var added = CHUNKS.computeIfAbsent(level, ignored -> new HashSet<>());
        for (int x = Math.min(min.getX(), max.getX()) >> 4; x <= Math.max(min.getX(), max.getX()) >> 4; x++)
            for (int z = Math.min(min.getZ(), max.getZ()) >> 4; z <= Math.max(min.getZ(), max.getZ()) >> 4; z++) {
                if (level.setChunkForced(x, z, true)) added.add(new ChunkPos(x, z));
                level.getChunk(x, z);
            }
    }
}
