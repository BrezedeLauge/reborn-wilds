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
import net.minecraft.world.level.storage.ServerLevelData;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

/** Real entity AI, support collision and event interruption; no activity/position changes after setup. */
@GameTestHolder(DungeonsAlive.MOD_ID)
@PrefixGameTestTemplate(false)
public final class BirdPerchingGameTests {
    private static final String BATCH = "bird_perching";
    private static final Map<ServerLevel, Set<Entity>> ACTORS = new IdentityHashMap<>();
    private static final Map<ServerLevel, Set<ChunkPos>> CHUNKS = new IdentityHashMap<>();
    private static final Map<ServerLevel, Map<BlockPos, BlockState>> BLOCKS = new IdentityHashMap<>();
    private record Conditions(long time, long tick, int clear, int rain, int thunder, boolean raining, boolean thundering) {}
    private static final Map<ServerLevel, Conditions> CONDITIONS = new IdentityHashMap<>();

    @AfterBatch(batch = BATCH)
    public static void cleanup(ServerLevel level) {
        var actors = ACTORS.remove(level); if (actors != null) actors.forEach(Entity::discard);
        var blocks = BLOCKS.remove(level); if (blocks != null) blocks.forEach((pos, state) -> level.setBlock(pos, state, Block.UPDATE_ALL));
        var chunks = CHUNKS.remove(level); if (chunks != null) chunks.forEach(chunk -> level.setChunkForced(chunk.x, chunk.z, false));
        var before = CONDITIONS.remove(level);
        if (before != null) {
            level.setDayTime(before.time + level.getGameTime() - before.tick);
            level.setWeatherParameters(before.clear, before.rain, before.raining, before.thundering);
            ((ServerLevelData)level.getLevelData()).setThunderTime(before.thunder);
        }
    }

    @GameTest(template = "gametest_empty", templateNamespace = DungeonsAlive.MOD_ID, batch = BATCH, skyAccess = true, timeoutTicks = 2400)
    public static void untouchedToucanFlockLandsRestsAndDepartsWhileAPlayerStandsStill(GameTestHelper helper) {
        BlockPos centre = arena(helper, 300);
        var flock = spawn(helper, centre, "pet_toucan");
        var observer = helper.makeMockPlayer(GameType.SURVIVAL);
        observer.setPos(Vec3.atBottomCenterOf(centre.offset(0, 1, 30)));
        Vec3 observerStart = observer.position();
        var watches = new HashMap<UUID, Watch>();
        flock.forEach(bird -> watches.put(bird.getUUID(), new Watch(bird)));
        boolean[] active = {true};
        helper.onEachTick(() -> {
            if (!active[0]) return;
            helper.assertTrue(observer.position().equals(observerStart), "Standing observer was moved to trigger the scene");
            for (var bird : flock) {
                var watch = watches.get(bird.getUUID());
                movement(helper, bird, watch);
                if (bird.birdPerched()) {
                    contact(helper, bird);
                    watch.quietTicks++;
                    if (watch.quietTicks >= 40) watch.rested = true;
                    if (!watch.saved) {
                        var tag = new CompoundTag(); bird.saveWithoutId(tag);
                        var restored = ModPeacefulEntities.creatureType("pet_toucan").create(helper.getLevel());
                        helper.assertTrue(restored != null, "Toucan reload factory missing");
                        restored.load(tag); track(helper.getLevel(), restored);
                        helper.assertTrue(restored.getUUID().equals(bird.getUUID())
                                        && bird.wildlifeGroupId().equals(restored.wildlifeGroupId())
                                        && bird.wildlifeSlot() == restored.wildlifeSlot()
                                        && bird.birdPerch().equals(restored.birdPerch())
                                        && restored.birdActivity() == BirdRoutineState.Phase.PERCHED,
                                "Full NBT lost member identity, slot, actual seat or routine progress");
                        helper.assertFalse(restored.birdPerched(), "Unvalidated NBT invented a new grounded pose");
                        restored.discard(); watch.saved = true;
                    }
                } else watch.quietTicks = 0;
                if (watch.rested && !watch.departed && bird.birdActivity() == BirdRoutineState.Phase.TAKING_OFF) {
                    watch.departed = true; watch.departureTick = helper.getLevel().getGameTime(); watch.departure = bird.position();
                }
                if (watch.departed && bird.birdActivity() == BirdRoutineState.Phase.FLYING && !bird.onGround()
                        && horizontalDistanceSqr(bird.position(), watch.departure) > 4) watch.continued = true;
            }
            if (watches.values().stream().allMatch(watch -> watch.saved && watch.rested && watch.continued)) {
                helper.assertTrue(watches.values().stream().map(watch -> watch.departureTick).distinct().count() == flock.size(),
                        "The entire flock launched in one tick instead of staggered member departures");
                active[0] = false; helper.succeed();
            }
        });
        helper.runAtTickTime(2350, () -> {
            helper.assertTrue(!active[0], "Stationary observation did not see a complete autonomous bird cycle: " + diagnostics(flock, watches));
        });
    }

    @GameTest(template = "gametest_empty", templateNamespace = DungeonsAlive.MOD_ID, batch = BATCH, skyAccess = true, timeoutTicks = 3200)
    public static void perchedVulturesFleeRegroupAndReplaceARemovedSeatWithoutTeleport(GameTestHelper helper) {
        BlockPos centre = arena(helper, 700);
        var flock = spawn(helper, centre, "vulture");
        var attacker = helper.makeMockPlayer(GameType.SURVIVAL);
        attacker.setPos(Vec3.atBottomCenterOf(centre.offset(-20, 1, 0)));
        var watches = new HashMap<UUID, Watch>();
        flock.forEach(bird -> watches.put(bird.getUUID(), new Watch(bird)));
        boolean[] active = {true}, disturbed = {false}, removedSeat = {false};
        long[] alarmUntil = {0};
        int[] seatRemovedAtEntityTick = {-1};
        BlockPos[] removed = {null};
        DungeonsCreature chosen = flock.getFirst();
        helper.onEachTick(() -> {
            if (!active[0]) return;
            for (var bird : flock) {
                var watch = watches.get(bird.getUUID()); movement(helper, bird, watch);
                // The test itself can remove support between two entity ticks.
                // onGround and the published pose still describe the preceding
                // physics result until this actual bird has ticked again.
                boolean beforePostRemovalTick = removedSeat[0] && bird == chosen
                        && bird.tickCount <= seatRemovedAtEntityTick[0];
                if (removedSeat[0] && bird == chosen && !beforePostRemovalTick)
                    helper.assertFalse(bird.birdPerched() && removed[0].equals(bird.birdPerch()),
                            "A real post-removal entity tick republished the removed seat: " + diagnostics(flock, watches));
                if (bird.birdPerched() && !beforePostRemovalTick) { contact(helper, bird); watch.quietTicks++; }
                else watch.quietTicks = 0;
                if (disturbed[0] && bird.birdActivity() == BirdRoutineState.Phase.FLEEING
                        && bird.position().distanceToSqr(watch.departure) > 1) watch.fled = true;
            }
            if (!disturbed[0] && watches.values().stream().allMatch(watch -> watch.quietTicks >= 10)) {
                flock.forEach(bird -> watches.get(bird.getUUID()).departure = bird.position());
                helper.assertTrue(chosen.hurt(helper.getLevel().damageSources().playerAttack(attacker), 1), "Actual bird disturbance damage was rejected");
                alarmUntil[0] = chosen.wildlifeAlarmUntil(); disturbed[0] = true;
                helper.assertTrue(flock.stream().allMatch(bird -> bird.wildlifeAlarmUntil() == alarmUntil[0]), "Perched members did not share a finite actual alarm");
            }
            if (disturbed[0] && !removedSeat[0] && helper.getLevel().getGameTime() > alarmUntil[0] + 80
                    && watches.values().stream().allMatch(watch -> watch.fled) && chosen.birdPerched()) {
                removed[0] = chosen.birdPerch();
                seatRemovedAtEntityTick[0] = chosen.tickCount;
                helper.getLevel().setBlock(removed[0], Blocks.AIR.defaultBlockState(), Block.UPDATE_ALL);
                removedSeat[0] = true;
            }
            if (removedSeat[0] && chosen.birdPerched() && !removed[0].equals(chosen.birdPerch())
                    && flock.stream().allMatch(bird -> bird.birdPerched() && bird.wildlifeAlarmUntil() <= helper.getLevel().getGameTime())) {
                helper.assertTrue(helper.getLevel().getBlockState(removed[0]).isAir(), "Bird repaired or changed a removed player roof block");
                active[0] = false; helper.succeed();
            }
        });
        helper.runAtTickTime(3150, () -> helper.assertTrue(!active[0], "Interrupted flock did not physically flee, regroup and revalidate its seat: disturbed="
                + disturbed[0] + ", removed=" + removedSeat[0] + ", " + diagnostics(flock, watches)));
    }

    private static void movement(GameTestHelper helper, DungeonsCreature bird, Watch watch) {
        helper.assertTrue(bird.isAlive() && bird.getTarget() == null && bird.ownerId() == null, "Bird disappeared, attacked or changed ownership");
        helper.assertTrue(watch.group.equals(bird.wildlifeGroupId()), "Routine replaced the saved flock identity");
        helper.assertTrue(bird.position().distanceToSqr(watch.previous) < 2.25, "Bird routine teleported instead of using movement physics");
        watch.previous = bird.position();
    }
    private static void contact(GameTestHelper helper, DungeonsCreature bird) {
        helper.assertTrue(bird.birdPerch() != null && bird.onGround() && !bird.isNoGravity()
                        && !bird.level().noBlockCollision(bird, bird.getBoundingBox().move(0, -.025, 0)),
                "Perched state was published without actual supporting collision: " + bird.position()
                        + ",slot=" + bird.wildlifeSlot() + ",phase=" + bird.birdActivity() + ",ground=" + bird.onGround()
                        + ",gravity=" + !bird.isNoGravity() + ",seat=" + bird.birdPerch() + ",entityTick=" + bird.tickCount);
        helper.assertTrue(bird.level().getBlockState(bird.birdPerch()).is(Blocks.OAK_LEAVES), "Fixture did not use a real canopy support");
    }
    private static List<DungeonsCreature> spawn(GameTestHelper helper, BlockPos centre, String type) {
        var result = new ArrayList<DungeonsCreature>();
        SpawnGroupData group = null;
        for (int slot = 0; slot < 3; slot++) {
            var bird = ModPeacefulEntities.creatureType(type).create(helper.getLevel());
            helper.assertTrue(bird != null, "Bird factory missing: " + type);
            bird.setPos(Vec3.atBottomCenterOf(centre.offset(slot * 2 - 2, 8, 0)));
            group = bird.finalizeSpawn(helper.getLevel(), helper.getLevel().getCurrentDifficultyAt(bird.blockPosition()), MobSpawnType.NATURAL, group);
            bird.setPersistenceRequired(); // Keep fixture actors independent of unrelated distance-based despawn.
            track(helper.getLevel(), bird);
            helper.assertTrue(helper.getLevel().addFreshEntity(bird), "Could not add the real natural bird");
            result.add(bird);
        }
        return result;
    }
    private static BlockPos arena(GameTestHelper helper, int offset) {
        ServerLevel level = helper.getLevel();
        var data = (ServerLevelData)level.getLevelData();
        CONDITIONS.computeIfAbsent(level, ignored -> new Conditions(level.getDayTime(), level.getGameTime(), data.getClearWeatherTime(), data.getRainTime(),
                data.getThunderTime(), level.isRaining(), level.isThundering()));
        level.setDayTime(6000); level.setWeatherParameters(0, 6000, false, false);
        BlockPos centre = helper.absolutePos(new BlockPos(offset, 0, offset));
        var chunks = CHUNKS.computeIfAbsent(level, ignored -> new HashSet<>());
        for (int x = (centre.getX() - 100) >> 4; x <= (centre.getX() + 100) >> 4; x++)
            for (int z = (centre.getZ() - 100) >> 4; z <= (centre.getZ() + 100) >> 4; z++) {
                if (level.setChunkForced(x, z, true)) chunks.add(new ChunkPos(x, z));
                level.getChunk(x, z); // Explicit isolated fixture loading, never a production search.
            }
        var before = BLOCKS.computeIfAbsent(level, ignored -> new HashMap<>());
        // A broad, flat canopy isolates actual support/flight physics from rare
        // terrain shape selection; all changed blocks lie outside other test bounds.
        for (int x = -48; x <= 48; x++) for (int z = -48; z <= 48; z++) {
            BlockPos pos = centre.offset(x, 4, z);
            before.putIfAbsent(pos, level.getBlockState(pos));
            level.setBlock(pos, Blocks.OAK_LEAVES.defaultBlockState().setValue(net.minecraft.world.level.block.LeavesBlock.PERSISTENT, true), Block.UPDATE_KNOWN_SHAPE);
        }
        return centre;
    }
    private static void track(ServerLevel level, Entity actor) { ACTORS.computeIfAbsent(level, ignored -> new HashSet<>()).add(actor); }
    private static double horizontalDistanceSqr(Vec3 a, Vec3 b) { double x = a.x - b.x, z = a.z - b.z; return x*x + z*z; }
    private static String diagnostics(List<DungeonsCreature> flock, Map<UUID, Watch> watches) {
        return flock.stream().map(bird -> { var watch = watches.get(bird.getUUID()); var path = bird.getNavigation().getPath();
            return bird.wildlifeSlot() + ":" + bird.birdActivity() + "@" + bird.position() + ",seat=" + bird.birdPerch()
                    + ",ground=" + bird.onGround() + ",motion=" + bird.getDeltaMovement() + ",quiet=" + watch.quietTicks
                    + ",rested=" + watch.rested + ",departed=" + watch.departed + ",continued=" + watch.continued + ",fled=" + watch.fled
                    + ",path=" + (path == null ? "null" : path.getNextNodeIndex() + "/" + path.getNodeCount()); }).toList().toString();
    }
    private static final class Watch {
        final UUID group;
        Vec3 previous, departure;
        int quietTicks;
        long departureTick;
        boolean saved, rested, departed, continued, fled;
        Watch(DungeonsCreature bird) { group = bird.wildlifeGroupId(); previous = bird.position(); departure = previous; }
    }
    private BirdPerchingGameTests() {}
}
