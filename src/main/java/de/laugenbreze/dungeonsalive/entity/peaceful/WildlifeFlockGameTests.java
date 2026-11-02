package de.laugenbreze.dungeonsalive.entity.peaceful;

import de.laugenbreze.dungeonsalive.DungeonsAlive;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.AfterBatch;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

/** Server movement/collision regressions; no clock, weather or natural-spawn scheduler changes. */
@GameTestHolder(DungeonsAlive.MOD_ID)
@PrefixGameTestTemplate(false)
public final class WildlifeFlockGameTests {
    private static final String BATCH = "wildlife_flight";
    private static final int TEMPLATE_HEIGHT = 5;
    private static final Map<ServerLevel, Set<ChunkPos>> FIXTURE_CHUNKS = new IdentityHashMap<>();
    private static final Map<ServerLevel, Set<Entity>> FIXTURE_ENTITIES = new IdentityHashMap<>();
    private static final Map<ServerLevel, Map<BlockPos, BlockState>> FIXTURE_BLOCKS = new IdentityHashMap<>();

    private WildlifeFlockGameTests() {}

    @AfterBatch(batch = BATCH)
    public static void releaseFixtureChunks(ServerLevel level) {
        Set<Entity> entities = FIXTURE_ENTITIES.remove(level);
        if (entities != null) entities.forEach(Entity::discard);
        Map<BlockPos, BlockState> blocks = FIXTURE_BLOCKS.remove(level);
        if (blocks != null) blocks.forEach((position, state) -> level.setBlock(position, state, Block.UPDATE_ALL));
        Set<ChunkPos> chunks = FIXTURE_CHUNKS.remove(level);
        if (chunks != null) for (ChunkPos chunk : chunks) level.setChunkForced(chunk.x, chunk.z, false);
    }

    @GameTest(template = "gametest_empty", templateNamespace = DungeonsAlive.MOD_ID,
            batch = BATCH, skyAccess = true, timeoutTicks = 170)
    public static void realVulturePackClimbsTogetherAndAlarmExpires(GameTestHelper helper) {
        arena(helper);
        List<DungeonsCreature> flock = spawnPack(helper, "vulture");
        var attacker = helper.makeMockPlayer(GameType.SURVIVAL);
        attacker.setPos(helper.absoluteVec(new Vec3(2.5, 3, 5.5)));
        Vec3[] starts = flock.stream().map(DungeonsCreature::position).toArray(Vec3[]::new);
        Vec3[] previous = starts.clone();
        boolean[] sawPath = new boolean[flock.size()];
        boolean[] advanced = new boolean[flock.size()];
        boolean[] started = {false};
        helper.runAtTickTime(4, () -> {
            flock.forEach(member -> member.setNoAi(false));
            helper.assertTrue(flock.getFirst().hurt(helper.getLevel().damageSources().playerAttack(attacker), 1), "Pack alarm damage was rejected");
            long alarm = flock.getFirst().wildlifeAlarmUntil();
            for (var member : flock) helper.assertTrue(member.wildlifeAlarmUntil() == alarm,
                    "Accepted damage did not alarm the actual loaded flock");
            started[0] = true;
        });
        helper.onEachTick(() -> {
            if (!started[0]) return;
            for (int i = 0; i < flock.size(); i++) {
                var member = flock.get(i);
                helper.assertTrue(member.isAlive() && member.getTarget() == null, "A flying wildlife member disappeared or retaliated");
                helper.assertTrue(member.position().distanceToSqr(previous[i]) < 2.25, "Flock movement jumped instead of flying");
                previous[i] = member.position();
                sawPath[i] |= !member.getNavigation().isDone();
                if (member.wildlifeActivity() == WildlifeGroupPolicy.Activity.FLEE) {
                    advanced[i] |= member.getX() > starts[i].x + 2 && member.getY() > starts[i].y + 1;
                }
            }
        });
        helper.runAtTickTime(145, () -> {
            UUID group = flock.getFirst().wildlifeGroupId();
            for (int i = 0; i < flock.size(); i++) {
                var member = flock.get(i);
                helper.assertTrue(group != null && group.equals(member.wildlifeGroupId()) && sawPath[i] && advanced[i],
                        "Vulture " + i + " did not share group identity and actual forward/upward flight: " + member.position());
                helper.assertTrue(member.wildlifeActivity() != WildlifeGroupPolicy.Activity.FLEE,
                        "A single damage event left the flock permanently alarmed");
            }
            started[0] = false; // succeed() removes actors before all same-tick callbacks finish.
            helper.succeed();
        });
    }

    @GameTest(template = "gametest_empty", templateNamespace = DungeonsAlive.MOD_ID,
            batch = BATCH, skyAccess = true, timeoutTicks = 150)
    public static void reloadedWispKeepsItsSlotAndActuallyRejoinsLoadedFlock(GameTestHelper helper) {
        arena(helper);
        List<DungeonsCreature> flock = spawnPack(helper, "wisp");
        var original = flock.get(1);
        UUID memberId = original.getUUID();
        UUID groupId = original.wildlifeGroupId();
        int slot = original.wildlifeSlot();
        BlockPos anchor = original.wildlifeAnchor();
        CompoundTag saved = new CompoundTag();
        DungeonsCreature[] restored = new DungeonsCreature[1];
        Vec3 displaced = helper.absoluteVec(new Vec3(17.5, 18, 9.5));
        Vec3 initialCompanionCenter = flock.getFirst().position().add(flock.getLast().position()).scale(.5);
        Vec3 towardCompanions = initialCompanionCenter.subtract(displaced);
        double initialSeparation = towardCompanions.length();
        Vec3 rejoinDirection = towardCompanions.normalize();
        Vec3[] previous = {displaced};
        boolean[] sawPath = {false};
        boolean[] tracking = {false};
        helper.runAtTickTime(2, () -> {
            original.saveWithoutId(saved);
            original.discard();
        });
        helper.runAtTickTime(4, () -> {
            var member = ModPeacefulEntities.creatureType("wisp").create(helper.getLevel());
            helper.assertTrue(member != null, "Wisp factory disappeared");
            member.load(saved);
            member.setPos(displaced); // Initial separation only; all subsequent motion is server AI.
            member.setNoAi(false);
            trackFixture(helper.getLevel(), member);
            helper.assertTrue(helper.getLevel().addFreshEntity(member), "Could not add the same-UUID reloaded wisp");
            helper.assertTrue(memberId.equals(member.getUUID()) && groupId.equals(member.wildlifeGroupId())
                            && slot == member.wildlifeSlot() && anchor.equals(member.wildlifeAnchor()),
                    "Full NBT reload changed the flock identity, member slot or shared anchor");
            restored[0] = member;
            tracking[0] = true;
            flock.set(1, member);
            flock.forEach(actor -> actor.setNoAi(false));
            var attacker = helper.makeMockPlayer(GameType.SURVIVAL);
            attacker.setPos(helper.absoluteVec(new Vec3(2.5, 3, 5.5)));
            // Damage the reloaded member itself so its real damage hook resolves
            // its loaded companions before the first goal tick after restoration.
            helper.assertTrue(member.hurt(helper.getLevel().damageSources().playerAttack(attacker), 1), "Rejoin alarm damage was rejected");
            helper.assertTrue(member.wildlifeAlarmUntil() == flock.getFirst().wildlifeAlarmUntil(),
                    "Reloaded member was not resolved as a living local flock member");
        });
        helper.onEachTick(() -> {
            if (!tracking[0]) return;
            var member = restored[0];
            helper.assertTrue(member.position().distanceToSqr(previous[0]) < 2.25,
                    "Rejoining jumped instead of following a real flight route");
            previous[0] = member.position();
            sawPath[0] |= !member.getNavigation().isDone();
            helper.assertTrue(member.isAlive() && member.getTarget() == null && groupId.equals(member.wildlifeGroupId()),
                    "Rejoining changed the saved peaceful flock membership");
        });
        helper.succeedWhen(() -> {
            // The alarm moves the companions east as well. Westward movement
            // alone is not a rejoin invariant: the separated member must perform
            // its own substantial 3D flight toward the original pack and then
            // regain spacing with the actual, still-moving loaded companions.
            Vec3 travelled = restored[0] == null ? Vec3.ZERO : restored[0].position().subtract(displaced);
            helper.assertTrue(restored[0] != null && sawPath[0]
                            && travelled.lengthSqr() > initialSeparation * initialSeparation / 4
                            && travelled.dot(rejoinDirection) > initialSeparation / 3,
                    "Separated wisp has not performed its own 3D flight toward the pack: travelled="
                            + travelled + ", initial separation=" + initialSeparation + ", saw path=" + sawPath[0]
                            + ", member=" + movementState(restored[0]) + ", leader=" + movementState(flock.getFirst()));
            helper.assertTrue(restored[0].distanceToSqr(flock.getFirst()) < 64,
                    "Reloaded wisp has not regained local flock spacing: member=" + movementState(restored[0])
                            + ", leader=" + movementState(flock.getFirst()));
            helper.assertTrue(slot == restored[0].wildlifeSlot() && anchor.equals(restored[0].wildlifeAnchor()),
                    "Rejoining silently reassigned the member slot or anchor");
            tracking[0] = false; // succeedWhen removes actors before remaining same-tick callbacks.
        });
    }

    @GameTest(template = "gametest_empty", templateNamespace = DungeonsAlive.MOD_ID,
            batch = BATCH, skyAccess = true, timeoutTicks = 85)
    public static void flightStopsAtNewCollisionAndNeverLoadsRejectedTarget(GameTestHelper helper) {
        arena(helper);
        var wisp = ModPeacefulEntities.creatureType("wisp").create(helper.getLevel());
        helper.assertTrue(wisp != null, "Wisp factory disappeared");
        // Existing saved waiting state suppresses autonomous group/wander goals;
        // NoAI stays false so actual server navigation and movement still execute.
        CompoundTag waiting = new CompoundTag();
        waiting.putBoolean("DungeonsWaiting", true);
        wisp.readAdditionalSaveData(waiting);
        wisp.setPos(helper.absoluteVec(new Vec3(3.5, 3, 5.5)));
        trackFixture(helper.getLevel(), wisp);
        helper.assertTrue(helper.getLevel().addFreshEntity(wisp), "Could not add the flight navigation actor");
        double startX = wisp.getX();
        int[] wallX = {0};
        helper.runAtTickTime(4, () -> {
            helper.assertTrue(!wisp.isNoAi() && wisp.getNavigation() instanceof WildlifeFlightNavigation,
                    "Fixture did not install real wildlife flight navigation");
            BlockPos absent = null;
            for (int distance : new int[]{512, 1024, 2048, 4096}) {
                BlockPos candidate = wisp.blockPosition().offset(distance, 0, distance);
                if (!helper.getLevel().hasChunk(candidate.getX() >> 4, candidate.getZ() >> 4)) { absent = candidate; break; }
            }
            helper.assertTrue(absent != null, "No unloaded target among the bounded candidate positions");
            helper.assertTrue(wisp.getNavigation().createPath(absent, 0) == null, "Flight accepted an unloaded target");
            helper.assertFalse(helper.getLevel().hasChunk(absent.getX() >> 4, absent.getZ() >> 4),
                    "Rejected flight route loaded its target chunk");
            var path = wisp.getNavigation().createPath(helper.absolutePos(new BlockPos(15, 3, 5)), 0);
            helper.assertTrue(path != null && path.canReach() && wisp.getNavigation().moveTo(path, .8),
                    "Open-air navigation did not accept an actual traversable path");
        });
        helper.onEachTick(() -> {
            // Insert the obstacle while the real path is moving. A fast flyer can
            // already finish this short route before a fixed later fixture tick.
            if (wallX[0] != 0 || wisp.getX() <= startX + .25 || wisp.getNavigation().isDone()) return;
            wallX[0] = (int) Math.floor(wisp.getX()) + 2;
            BlockPos min = helper.absolutePos(new BlockPos(0, 1, 0));
            BlockPos max = helper.absolutePos(new BlockPos(19, 7, 11));
            // Suppress update notifications to isolate the running-path collision
            // guard; the blocks themselves are real and read on subsequent AI ticks.
            for (int y = min.getY(); y <= max.getY(); y++) for (int z = min.getZ(); z <= max.getZ(); z++) {
                BlockPos position = new BlockPos(wallX[0], y, z);
                rememberBlock(helper.getLevel(), position);
                helper.getLevel().setBlock(position, Blocks.STONE.defaultBlockState(), Block.UPDATE_KNOWN_SHAPE);
            }
        });
        helper.runAtTickTime(65, () -> {
            helper.assertTrue(wallX[0] != 0, "Flight fixture never showed real movement on an active path: " + wisp.position()
                    + ", navigation done=" + wisp.getNavigation().isDone());
            helper.assertTrue(wisp.isAlive() && wisp.getNavigation().isDone() && wisp.getX() < wallX[0],
                    "Active flight failed to stop before the newly blocked route");
            helper.assertTrue(helper.getLevel().getBlockState(new BlockPos(wallX[0], wisp.blockPosition().getY(), wisp.blockPosition().getZ())).is(Blocks.STONE),
                    "Flight changed the collision wall rather than stopping");
            helper.assertTrue(wisp.getTarget() == null, "Collision handling introduced a combat target");
            helper.succeed();
        });
    }

    private static List<DungeonsCreature> spawnPack(GameTestHelper helper, String id) {
        List<DungeonsCreature> result = new ArrayList<>();
        UUID seed = UUID.randomUUID();
        SpawnGroupData group = null;
        for (int i = 0; i < 3; i++) {
            var member = ModPeacefulEntities.creatureType(id).create(helper.getLevel());
            helper.assertTrue(member != null, "Missing flock factory " + id);
            member.setUUID(new UUID(seed.getMostSignificantBits(), (seed.getLeastSignificantBits() & ~7L) + i));
            member.setPos(helper.absoluteVec(new Vec3(4.5, 3, i == 0 ? 5.5 : i == 1 ? 7.5 : 3.5)));
            group = member.finalizeSpawn(helper.getLevel(), helper.getLevel().getCurrentDifficultyAt(member.blockPosition()), MobSpawnType.NATURAL, group);
            member.setPersistenceRequired(); // Test actors remain available even without real connected players.
            member.setNoAi(true);
            trackFixture(helper.getLevel(), member);
            helper.assertTrue(helper.getLevel().addFreshEntity(member), "Could not spawn explicit flock fixture");
            result.add(member);
        }
        return result;
    }

    private static void arena(GameTestHelper helper) {
        for (int x = 0; x < 20; x++) for (int z = 0; z < 12; z++) {
            helper.setBlock(new BlockPos(x, 0, z), Blocks.STONE);
            for (int y = 1; y <= 22; y++) {
                BlockPos position = new BlockPos(x, y, z);
                // gametest_empty is only 20 x 5 x 12. Preserve the extra flight
                // volume ourselves because it lies outside template cleanup.
                if (y >= TEMPLATE_HEIGHT) rememberBlock(helper.getLevel(), helper.absolutePos(position));
                helper.setBlock(position, Blocks.AIR);
            }
        }
        ServerLevel level = helper.getLevel();
        int radius = WildlifeFlightNavigation.LOADED_RADIUS;
        BlockPos min = helper.absolutePos(new BlockPos(-radius, 0, -radius));
        BlockPos max = helper.absolutePos(new BlockPos(19 + radius, 0, 11 + radius));
        Set<ChunkPos> added = FIXTURE_CHUNKS.computeIfAbsent(level, ignored -> new HashSet<>());
        for (int x = Math.min(min.getX(), max.getX()) >> 4; x <= Math.max(min.getX(), max.getX()) >> 4; x++) {
            for (int z = Math.min(min.getZ(), max.getZ()) >> 4; z <= Math.max(min.getZ(), max.getZ()) >> 4; z++) {
                if (level.setChunkForced(x, z, true)) added.add(new ChunkPos(x, z));
                level.getChunk(x, z);
            }
        }
    }

    private static void trackFixture(ServerLevel level, Entity entity) {
        FIXTURE_ENTITIES.computeIfAbsent(level, ignored -> new HashSet<>()).add(entity);
    }

    private static String movementState(DungeonsCreature creature) {
        if (creature == null) return "not restored";
        var path = creature.getNavigation().getPath();
        return creature.position() + ", motion=" + creature.getDeltaMovement()
                + ", activity=" + creature.wildlifeActivity() + ", members=" + creature.wildlifeMembers().size()
                + ", done=" + creature.getNavigation().isDone() + ", path="
                + (path == null ? "null" : path.getNextNodeIndex() + "/" + path.getNodeCount() + " to " + path.getTarget()
                + (path.isDone() ? "" : " next " + path.getNextNodePos()));
    }

    private static void rememberBlock(ServerLevel level, BlockPos position) {
        FIXTURE_BLOCKS.computeIfAbsent(level, ignored -> new HashMap<>())
                .putIfAbsent(position.immutable(), level.getBlockState(position));
    }
}
