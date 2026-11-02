package de.laugenbreze.dungeonsalive.entity.peaceful;

import de.laugenbreze.dungeonsalive.DungeonsAlive;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Set;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.AfterBatch;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

/** A real low branch behind a high shelf; the bird must physically clear the edge before descending. */
@GameTestHolder(DungeonsAlive.MOD_ID)
@PrefixGameTestTemplate(false)
public final class WildlifeFlightNavigationGameTests {
    private static final String BATCH = "wildlife_flight_corners";
    private static final Map<ServerLevel, Set<Entity>> ACTORS = new IdentityHashMap<>();
    private static final Map<ServerLevel, Set<ChunkPos>> CHUNKS = new IdentityHashMap<>();
    private static final Map<ServerLevel, Map<BlockPos, BlockState>> BLOCKS = new IdentityHashMap<>();
    private record Clock(long time, long tick) { }
    private static final Map<ServerLevel, Clock> CLOCKS = new IdentityHashMap<>();

    @AfterBatch(batch = BATCH)
    public static void cleanup(ServerLevel level) {
        var actors = ACTORS.remove(level); if (actors != null) actors.forEach(Entity::discard);
        var blocks = BLOCKS.remove(level); if (blocks != null) blocks.forEach((pos, state) -> level.setBlock(pos, state, Block.UPDATE_ALL));
        var chunks = CHUNKS.remove(level); if (chunks != null) chunks.forEach(chunk -> level.setChunkForced(chunk.x, chunk.z, false));
        var clock = CLOCKS.remove(level); if (clock != null) level.setDayTime(clock.time + level.getGameTime() - clock.tick);
    }

    @GameTest(template = "gametest_empty", templateNamespace = DungeonsAlive.MOD_ID,
            batch = BATCH, skyAccess = true, timeoutTicks = 650)
    public static void naturalToucanActuallyClearsAHighShelfAndLandsOnItsLowerBranch(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        BlockPos origin = helper.absolutePos(new BlockPos(1200, 0, 1200));
        fixture(level, origin);
        var bird = ModPeacefulEntities.creatureType("pet_toucan").create(level);
        helper.assertTrue(bird != null, "Actual toucan type missing");
        Vec3 start = Vec3.atBottomCenterOf(origin.offset(4, 11, 5));
        bird.setPos(start);
        bird.finalizeSpawn(level, level.getCurrentDifficultyAt(bird.blockPosition()), MobSpawnType.NATURAL, null);
        bird.setPersistenceRequired(); // Fixture lifetime only; real unmodified bird AI and calendar.
        ACTORS.computeIfAbsent(level, ignored -> new HashSet<>()).add(bird);
        helper.assertTrue(level.addFreshEntity(bird), "Real natural bird admission failed");
        Vec3[] previous = {start};
        boolean[] active = {true}, sawLivePath = {false}, descendedBeyondEdge = {false};
        int[] supportTicks = {0};
        helper.onEachTick(() -> {
            if (!active[0]) return;
            helper.assertTrue(bird.isAlive() && !bird.isNoAi() && bird.ownerId() == null && bird.getTarget() == null,
                    "The lower-branch fixture lost its real peaceful AI actor");
            helper.assertTrue(bird.position().distanceToSqr(previous[0]) < .75 * .75, "Flight route teleported over the shelf");
            previous[0] = bird.position();
            helper.assertTrue(level.noBlockCollision(bird, bird.getBoundingBox().deflate(.001)), "Bird body entered the shelf or branch");
            sawLivePath[0] |= !bird.getNavigation().isDone();
            if (bird.getY() < origin.getY() + 8.9 && bird.getBoundingBox().minX >= origin.getX() + 7)
                descendedBeyondEdge[0] = true;
            if (bird.birdPerched()) {
                helper.assertTrue(bird.birdPerch() != null && bird.birdPerch().getY() == origin.getY() + 2
                                && level.getBlockState(bird.birdPerch()).is(Blocks.OAK_LEAVES)
                                && bird.onGround() && !bird.isNoGravity()
                                && !level.noBlockCollision(bird, bird.getBoundingBox().move(0, -.025, 0)),
                        "Perched pose did not represent real contact with the lower branch: " + diagnostic(bird));
                supportTicks[0]++;
            } else supportTicks[0] = 0;
            if (supportTicks[0] >= 20) {
                helper.assertTrue(sawLivePath[0] && descendedBeyondEdge[0] && start.y - bird.getY() > 7,
                        "The bird did not actually follow a descending route around the high shelf");
                helper.assertTrue(level.getBlockState(origin.offset(6, 8, 5)).is(Blocks.GRASS_BLOCK), "Bird changed the obstructing shelf");
                active[0] = false; helper.succeed();
            }
        });
        helper.runAtTickTime(620, () -> helper.assertTrue(!active[0],
                "The real bird did not reach its lower branch: pathSeen=" + sawLivePath[0] + ", edgeCleared=" + descendedBeyondEdge[0]
                        + ", supportTicks=" + supportTicks[0] + ", " + diagnostic(bird)));
    }

    private static void fixture(ServerLevel level, BlockPos origin) {
        CLOCKS.computeIfAbsent(level, ignored -> new Clock(level.getDayTime(), level.getGameTime()));
        level.setDayTime(18_000); // Actual shelter calendar seeks a perch; no AI state setter.
        var chunks = CHUNKS.computeIfAbsent(level, ignored -> new HashSet<>());
        for (int x = (origin.getX() - 64) >> 4; x <= (origin.getX() + 80) >> 4; x++)
            for (int z = (origin.getZ() - 64) >> 4; z <= (origin.getZ() + 80) >> 4; z++) {
                if (level.setChunkForced(x, z, true)) chunks.add(new ChunkPos(x, z));
                level.getChunk(x, z); // Explicit fixture only; production navigation never loads.
            }
        for (int x = -8; x <= 14; x++) for (int z = -8; z <= 14; z++) {
            put(level, origin.offset(x, 0, z), Blocks.GRASS_BLOCK.defaultBlockState());
            for (int y = 1; y <= 16; y++) put(level, origin.offset(x, y, z), Blocks.AIR.defaultBlockState());
            if (x <= 6) {
                put(level, origin.offset(x, 7, z), Blocks.DIRT.defaultBlockState());
                put(level, origin.offset(x, 8, z), Blocks.GRASS_BLOCK.defaultBlockState());
            }
        }
        // Grass/dirt are deliberately not bird perches; the reachable lower canopy is the real choice.
        for (int x = 9; x <= 13; x++) for (int z = 2; z <= 8; z++)
            put(level, origin.offset(x, 2, z), Blocks.OAK_LEAVES.defaultBlockState().setValue(LeavesBlock.PERSISTENT, true));
    }
    private static void put(ServerLevel level, BlockPos position, BlockState state) {
        BLOCKS.computeIfAbsent(level, ignored -> new HashMap<>()).putIfAbsent(position.immutable(), level.getBlockState(position));
        level.setBlock(position, state, Block.UPDATE_KNOWN_SHAPE);
    }
    private static String diagnostic(DungeonsCreature bird) {
        var path = bird.getNavigation().getPath();
        return "position=" + bird.position() + ", phase=" + bird.birdActivity() + ", perch=" + bird.birdPerch()
                + ", motion=" + bird.getDeltaMovement() + ", path=" + (path == null ? "null" : path.getNextNodeIndex() + "/" + path.getNodeCount()
                + (path.isDone() ? " done" : " next=" + path.getNextNodePos()));
    }
    private WildlifeFlightNavigationGameTests() { }
}
