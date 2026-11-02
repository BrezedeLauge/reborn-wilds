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
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ServerLevelData;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

/** Real navigation over several group phases; fixtures restore their world state even on failure. */
@GameTestHolder(DungeonsAlive.MOD_ID)
@PrefixGameTestTemplate(false)
public final class WildlifeJourneyGameTests {
    private static final String BATCH = "wildlife_journeys";
    private static final String CAVE_BATCH = "wildlife_sheltered_journeys";
    private static final Map<ServerLevel, Set<ChunkPos>> CHUNKS = new IdentityHashMap<>();
    private static final Map<ServerLevel, Set<Entity>> ACTORS = new IdentityHashMap<>();
    private static final Map<ServerLevel, Map<BlockPos, BlockState>> BLOCKS = new IdentityHashMap<>();
    private static final Map<ServerLevel, Weather> WEATHER = new IdentityHashMap<>();
    private record Weather(long day, int clear, int rain, int thunder, boolean raining, boolean thundering,
                           float rainLevel, float thunderLevel) {}
    private WildlifeJourneyGameTests() {}

    @AfterBatch(batch = BATCH)
    public static void cleanupGround(ServerLevel level) { cleanup(level); }
    @AfterBatch(batch = CAVE_BATCH)
    public static void cleanupFlight(ServerLevel level) { cleanup(level); }
    private static void cleanup(ServerLevel level) {
        var actors = ACTORS.remove(level);
        if (actors != null) actors.forEach(Entity::discard);
        var blocks = BLOCKS.remove(level);
        if (blocks != null) blocks.forEach((position, state) -> level.setBlock(position, state, Block.UPDATE_ALL));
        var weather = WEATHER.remove(level);
        if (weather != null) {
            ServerLevelData data = (ServerLevelData)level.getLevelData();
            level.setDayTime(weather.day());
            data.setClearWeatherTime(weather.clear()); data.setRainTime(weather.rain()); data.setThunderTime(weather.thunder());
            data.setRaining(weather.raining()); data.setThundering(weather.thundering());
            level.setRainLevel(weather.rainLevel()); level.setThunderLevel(weather.thunderLevel());
        }
        var chunks = CHUNKS.remove(level);
        if (chunks != null) for (var chunk : chunks) level.setChunkForced(chunk.x, chunk.z, false);
    }

    @GameTest(template = "gametest_empty", templateNamespace = DungeonsAlive.MOD_ID,
            batch = BATCH, skyAccess = true, timeoutTicks = 580)
    public static void herdWalksBeyondOldSpawnCircleTogetherAndActuallyRests(GameTestHelper helper) {
        fixture(helper, 6_000);
        List<DungeonsCreature> herd = pack(helper, "wooly_cow", 3.5, 1, 5.5);
        Vec3[] starts = herd.stream().map(Entity::position).toArray(Vec3[]::new), previous = starts.clone();
        double[] progress = new double[herd.size()];
        boolean[] sawPath = new boolean[herd.size()];
        boolean[] started = {false};
        helper.runAtTickTime(4, () -> {
            assignRoute(helper, herd, new BlockPos(2, 1, 5));
            started[0] = true;
        });
        helper.onEachTick(() -> {
            if (!started[0]) return;
            for (int i = 0; i < herd.size(); i++) {
                var member = herd.get(i);
                helper.assertTrue(member.isAlive() && member.getTarget() == null, "Migration lost a peaceful herd actor");
                helper.assertTrue(member.position().distanceToSqr(previous[i]) < 1, "Migration teleported a herd member");
                previous[i] = member.position();
                progress[i] = Math.max(progress[i], member.position().distanceToSqr(starts[i]));
                sawPath[i] |= !member.getNavigation().isDone();
            }
        });
        helper.runAtTickTime(540, () -> {
            var leader = herd.getFirst();
            helper.assertTrue(progress[0] > 9 * 9 && sawPath[0], "Leader did not actually walk beyond its former tiny spawn circle");
            for (int i = 1; i < herd.size(); i++) {
                helper.assertTrue(sawPath[i] && progress[i] > 4 * 4 && herd.get(i).distanceToSqr(leader) < 8 * 8,
                        "The longer itinerary left a follower behind");
            }
            helper.assertTrue(leader.wildlifeActivity() == WildlifeGroupPolicy.Activity.REST && leader.getNavigation().isDone(),
                    "The moving herd did not stop during its shared quiet interval");
            helper.assertTrue(herd.stream().allMatch(member -> leader.wildlifeGroupId().equals(member.wildlifeGroupId())),
                    "Travel reassigned a persistent group identity");
            // GameTestInfo immediately discards actors on success, then may still
            // execute this tick's onEachTick callback in hash-map iteration order.
            started[0] = false;
            helper.succeed();
        });
    }

    @GameTest(template = "gametest_empty", templateNamespace = DungeonsAlive.MOD_ID,
            batch = CAVE_BATCH, skyAccess = true, timeoutTicks = 160)
    public static void shelteredWispsActuallyFlyBelowTheirRoofInsteadOfSeekingTheSurface(GameTestHelper helper) {
        fixture(helper, 18_000);
        for (int x = 0; x < 20; x++) for (int z = 0; z < 12; z++) {
            BlockPos position = helper.absolutePos(new BlockPos(x, 6, z));
            BLOCKS.computeIfAbsent(helper.getLevel(), ignored -> new HashMap<>()).putIfAbsent(position, helper.getLevel().getBlockState(position));
            helper.getLevel().setBlock(position, Blocks.STONE.defaultBlockState(), Block.UPDATE_ALL);
        }
        List<DungeonsCreature> flock = pack(helper, "wisp", 7.5, 2, 5.5);
        Vec3[] starts = flock.stream().map(Entity::position).toArray(Vec3[]::new), previous = starts.clone();
        boolean[] moved = new boolean[flock.size()], path = new boolean[flock.size()];
        boolean[] started = {false};
        boolean[] roofVerified = {false}, finished = {false};
        helper.runAtTickTime(4, () -> {
            for (var member : flock) {
                BlockPos roof = new BlockPos(member.blockPosition().getX(), helper.absolutePos(new BlockPos(0, 6, 0)).getY(), member.blockPosition().getZ());
                helper.assertTrue(helper.getLevel().getBlockState(roof).is(Blocks.STONE), "The local-height fixture is missing its actual roof block");
            }
            roofVerified[0] = true;
        });
        helper.onEachTick(() -> {
            if (finished[0]) return;
            if (!started[0]) {
                // canSeeSky reads asynchronous SKY light, not the physical roof.
                // Wait for this newly built fixture to receive its real light update.
                if (!roofVerified[0] || flock.stream().anyMatch(member -> helper.getLevel().canSeeSky(member.blockPosition()))) return;
                assignRoute(helper, flock, new BlockPos(7, 2, 5));
                started[0] = true;
            }
            for (int i = 0; i < flock.size(); i++) {
                var member = flock.get(i);
                helper.assertTrue(member.isAlive() && member.getY() < helper.absolutePos(new BlockPos(0, 6, 0)).getY(),
                        "Sheltered flock tried to reach the heightmap surface above its roof");
                helper.assertTrue(member.position().distanceToSqr(previous[i]) < 2.25, "Sheltered flock teleported");
                previous[i] = member.position();
                moved[i] |= member.position().distanceToSqr(starts[i]) > 1;
                path[i] |= !member.getNavigation().isDone();
            }
        });
        helper.runAtTickTime(100, () -> helper.assertTrue(started[0],
                "Roof blocks exist, but the bounded fixture wait did not receive updated skylight"));
        helper.succeedWhen(() -> {
            helper.assertTrue(started[0], "Flock fixture has not started");
            for (int i = 0; i < flock.size(); i++) helper.assertTrue(moved[i] && path[i],
                    "Wisp " + i + " has not flown along an actual local path under the roof");
            finished[0] = true; // Remaining same-tick callbacks must not restart or inspect removed actors.
        });
    }

    private static List<DungeonsCreature> pack(GameTestHelper helper, String id, double x, double y, double z) {
        List<DungeonsCreature> pack = new ArrayList<>();
        long high = UUID.randomUUID().getMostSignificantBits();
        for (int i = 0; i < 3; i++) {
            var member = ModPeacefulEntities.creatureType(id).create(helper.getLevel());
            helper.assertTrue(member != null, "Missing wildlife factory " + id);
            member.setUUID(new UUID(high, i));
            member.setPos(helper.absoluteVec(new Vec3(x, y, z + (i - 1) * 1.7)));
            member.setPersistenceRequired(); member.setNoAi(true);
            ACTORS.computeIfAbsent(helper.getLevel(), ignored -> new HashSet<>()).add(member);
            helper.assertTrue(helper.getLevel().addFreshEntity(member), "Could not place the explicit wildlife fixture");
            pack.add(member);
        }
        return pack;
    }

    private static void assignRoute(GameTestHelper helper, List<DungeonsCreature> pack, BlockPos localAnchor) {
        UUID id = new UUID(0, Math.floorMod(100 - helper.getLevel().getGameTime(), WildlifeJourney.ROUTE_TICKS));
        BlockPos anchor = helper.absolutePos(localAnchor);
        CompoundTag group = new CompoundTag(); group.putUUID("Id", id);
        group.putString("Dimension", helper.getLevel().dimension().location().toString());
        group.putInt("X", anchor.getX()); group.putInt("Y", anchor.getY()); group.putInt("Z", anchor.getZ());
        ListTag members = new ListTag();
        for (var member : pack) { CompoundTag tag = new CompoundTag(); tag.putUUID("Id", member.getUUID()); members.add(tag); }
        group.put("Members", members);
        for (var member : pack) {
            CompoundTag saved = new CompoundTag(); member.addAdditionalSaveData(saved);
            saved.put("DungeonsWildlifeGroup", group.copy()); member.readAdditionalSaveData(saved);
            member.setNoAi(false);
            helper.assertTrue(id.equals(member.wildlifeGroupId()) && anchor.equals(member.wildlifeAnchor()), "Saved group fixture was rejected");
        }
    }

    private static void fixture(GameTestHelper helper, long dayTime) {
        ServerLevel level = helper.getLevel();
        WEATHER.computeIfAbsent(level, ignored -> {
            ServerLevelData data = (ServerLevelData)level.getLevelData();
            return new Weather(level.getDayTime(), data.getClearWeatherTime(), data.getRainTime(), data.getThunderTime(),
                    data.isRaining(), data.isThundering(), level.getRainLevel(1), level.getThunderLevel(1));
        });
        level.setDayTime(dayTime); level.setWeatherParameters(10_000, 10_000, false, false);
        level.setRainLevel(0); level.setThunderLevel(0);
        for (int x = 0; x < 20; x++) for (int z = 0; z < 12; z++) helper.setBlock(new BlockPos(x, 0, z), Blocks.STONE);
        int radius = WildlifeNavigationSafety.LOADED_RADIUS;
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
