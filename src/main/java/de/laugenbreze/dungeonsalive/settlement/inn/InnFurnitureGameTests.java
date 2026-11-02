package de.laugenbreze.dungeonsalive.settlement.inn;

import de.laugenbreze.dungeonsalive.DungeonsAlive;
import de.laugenbreze.dungeonsalive.entity.peaceful.DungeonsMerchant;
import de.laugenbreze.dungeonsalive.entity.peaceful.ModPeacefulEntities;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.gametest.framework.AfterBatch;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

/** Real path/contact, persistence and ownership regressions for the registered furniture adapter. */
@GameTestHolder(DungeonsAlive.MOD_ID)
@PrefixGameTestTemplate(false)
public final class InnFurnitureGameTests {
    private static final String BATCH = "inn_furniture";
    private static final Map<ServerLevel, Set<Entity>> ACTORS = new IdentityHashMap<>();
    private static final Map<ServerLevel, Set<ChunkPos>> CHUNKS = new IdentityHashMap<>();
    private static final Map<ServerLevel, Set<UUID>> INNS = new IdentityHashMap<>();
    private InnFurnitureGameTests() { }
    @AfterBatch(batch = BATCH) public static void cleanup(ServerLevel level) {
        var actors = ACTORS.remove(level); if (actors != null) actors.forEach(Entity::discard);
        var records = INNS.remove(level);
        if (records != null) for (UUID id : records) {
            var saved = InnSavedData.get(level.getServer()); var inn = saved.find(id);
            if (inn != null && inn.guest() != null) {
                var g = inn.guest();
                if (g.phase() == InnRegistry.GuestPhase.RESERVED) saved.cancelReservation(id, g.entity());
                else {
                    if (g.phase() != InnRegistry.GuestPhase.LEAVING) saved.advance(id, g.entity(), g.phase(), InnRegistry.GuestPhase.LEAVING, level.getGameTime());
                    saved.finishDeparture(id, g.entity(), level.getGameTime());
                }
            }
            if (inn != null) saved.remove(id, inn.owner(), inn.revision());
        }
        var chunks = CHUNKS.remove(level); if (chunks != null) chunks.forEach(chunk -> level.setChunkForced(chunk.x, chunk.z, false));
    }
    @GameTest(template = "gametest_empty", templateNamespace = DungeonsAlive.MOD_ID, batch = BATCH, timeoutTicks = 360)
    public static void actualPathContactSeatsOnlyAssignedGuestAndRemovalReleasesItsSavedMount(GameTestHelper helper) {
        Fixture fixture = setup(helper); var furniture = InnFurnitureContent.FURNITURE;
        InnFurniture.Lease[] lease = {null}; long[] seatedAt = {-1}; boolean[] finished = {false}, removed = {false};
        helper.onEachTick(() -> {
            if (finished[0]) return;
            var snapshot = furniture.inspect(helper.getLevel(), fixture.inn);
            if (!snapshot.complete()) return;
            if (lease[0] == null) {
                lease[0] = furniture.reserve(helper.getLevel(), fixture.guest, fixture.inn, InnFurniture.Kind.SEAT);
                helper.assertTrue(lease[0] != null, "No bounded seat reservation");
                helper.assertFalse(furniture.use(helper.getLevel(), fixture.guest, lease[0]), "Distant guest was seated without reaching the furniture");
                helper.assertFalse(furniture.use(helper.getLevel(), fixture.stranger, lease[0]), "An unassigned merchant used somebody else's reservation");
                walkToContact(helper, fixture.guest, lease[0]);
            }
            if (!removed[0] && furniture.use(helper.getLevel(), fixture.guest, lease[0]) && seatedAt[0] < 0) {
                helper.assertTrue(fixture.guest.getVehicle() instanceof InnSeatEntity, "Seat use did not create a real passenger attachment");
                var seat = (InnSeatEntity)fixture.guest.getVehicle(); track(helper, seat);
                var saved = new CompoundTag(); helper.assertTrue(seat.save(saved), "Saved seat refused persistence");
                helper.assertTrue(saved.getList("Passengers", net.minecraft.nbt.Tag.TAG_COMPOUND).size() == 1,
                        "Saving the mount would lose its real guest passenger");
                var restored = InnFurnitureContent.SEAT.get().create(helper.getLevel());
                helper.assertTrue(restored != null, "Seat factory missing"); restored.load(saved); track(helper, restored);
                helper.assertTrue(restored.matches(lease[0]), "Seat NBT lost its inn/guest/revision binding"); restored.discard();
                seatedAt[0] = helper.getLevel().getGameTime();
            }
            if (!removed[0] && seatedAt[0] >= 0 && helper.getLevel().getGameTime() - seatedAt[0] >= 20) {
                helper.assertTrue(lease[0].equals(furniture.resume(helper.getLevel(), fixture.guest, fixture.inn)),
                        "The actual owned passenger did not resume its saved furniture lease");
                helper.getLevel().setBlockAndUpdate(lease[0].spot().contact(), Blocks.STONE.defaultBlockState());
                helper.assertFalse(furniture.release(helper.getLevel(), fixture.guest, lease[0]),
                        "Release dismounted into a newly blocked exit");
                helper.assertTrue(fixture.guest.isPassenger() && lease[0].equals(FurnitureReservations.readLease(fixture.guest.getPersistentData())),
                        "A blocked dismount lost the actual mount or its retryable lease");
                helper.getLevel().setBlockAndUpdate(lease[0].spot().contact(), Blocks.AIR.defaultBlockState());
                helper.getLevel().setBlockAndUpdate(lease[0].spot().furniture(), Blocks.AIR.defaultBlockState()); removed[0] = true;
            }
            if (removed[0] && !fixture.guest.isPassenger()) {
                helper.assertTrue(fixture.guest.isAlive() && fixture.stranger.isAlive(), "Furniture cleanup removed a merchant");
                helper.assertTrue(FurnitureReservations.readLease(fixture.guest.getPersistentData()) == null, "Removed bench retained its own lease");
                finished[0] = true; helper.succeed();
            }
        });
        helper.runAtTickTime(350, () -> helper.assertTrue(finished[0], "Seat contact/lifecycle incomplete: " + describe(fixture.guest, lease[0])));
    }
    @GameTest(template = "gametest_empty", templateNamespace = DungeonsAlive.MOD_ID, batch = BATCH, timeoutTicks = 360)
    public static void realBedSleepOwnsOccupiedStateAndReleaseWakesWithoutRemovingTheBed(GameTestHelper helper) {
        Fixture fixture = setup(helper); var furniture = InnFurnitureContent.FURNITURE;
        InnFurniture.Lease[] lease = {null}; long[] asleepAt = {-1}; boolean[] finished = {false};
        helper.onEachTick(() -> {
            if (finished[0]) return;
            var snapshot = furniture.inspect(helper.getLevel(), fixture.inn);
            if (!snapshot.complete()) return;
            if (lease[0] == null) {
                lease[0] = furniture.reserve(helper.getLevel(), fixture.guest, fixture.inn, InnFurniture.Kind.BED);
                helper.assertTrue(lease[0] != null, "No actual bed reservation");
                helper.assertFalse(furniture.use(helper.getLevel(), fixture.guest, lease[0]), "Distant guest slept before route contact");
                walkToContact(helper, fixture.guest, lease[0]);
            }
            if (furniture.use(helper.getLevel(), fixture.guest, lease[0]) && asleepAt[0] < 0) {
                helper.assertTrue(fixture.guest.isSleeping() && helper.getLevel().getBlockState(lease[0].spot().furniture()).getValue(BedBlock.OCCUPIED),
                        "Actual vanilla sleeping/occupied state missing");
                asleepAt[0] = helper.getLevel().getGameTime();
            }
            if (asleepAt[0] >= 0 && helper.getLevel().getGameTime() - asleepAt[0] >= 20) {
                helper.assertTrue(lease[0].equals(furniture.resume(helper.getLevel(), fixture.guest, fixture.inn)), "Owned actual sleep did not resume");
                helper.assertTrue(furniture.release(helper.getLevel(), fixture.guest, lease[0]), "A free safe bed exit was rejected");
                helper.assertFalse(fixture.guest.isSleeping(), "Release left guest asleep");
                var state = helper.getLevel().getBlockState(lease[0].spot().furniture());
                helper.assertTrue(state.is(Blocks.RED_BED) && !state.getValue(BedBlock.OCCUPIED), "Release destroyed the bed or kept it occupied");
                helper.assertTrue(fixture.guest.isAlive() && FurnitureReservations.readLease(fixture.guest.getPersistentData()) == null, "Release lost guest or kept stale lease");
                finished[0] = true; helper.succeed();
            }
        });
        helper.runAtTickTime(350, () -> helper.assertTrue(finished[0], "Sleep/release incomplete: " + describe(fixture.guest, lease[0])));
    }

    @GameTest(template = "gametest_empty", templateNamespace = DungeonsAlive.MOD_ID, batch = BATCH, timeoutTicks = 360)
    public static void terminalSleepingGuestClearsOnlyItsBedEvenWhenTheLivingExitIsBlocked(GameTestHelper helper) {
        Fixture fixture = setup(helper); var furniture = InnFurnitureContent.FURNITURE;
        InnFurniture.Lease[] lease = {null}; boolean[] killed = {false}, finished = {false};
        helper.onEachTick(() -> {
            if (finished[0]) return;
            var snapshot = furniture.inspect(helper.getLevel(), fixture.inn);
            if (!snapshot.complete()) return;
            if (lease[0] == null) {
                lease[0] = furniture.reserve(helper.getLevel(), fixture.guest, fixture.inn, InnFurniture.Kind.BED);
                helper.assertTrue(lease[0] != null, "No actual bed reservation");
                walkToContact(helper, fixture.guest, lease[0]);
            }
            if (!killed[0] && furniture.use(helper.getLevel(), fixture.guest, lease[0])) {
                BlockPos bed = lease[0].spot().furniture();
                // Enclose every vanilla stand-up candidate. A living guest must
                // wait; a genuinely terminal removal must release ownership.
                for (int x = -2; x <= 2; x++) for (int z = -2; z <= 2; z++) {
                    BlockPos column = bed.offset(x, 0, z);
                    if (!(helper.getLevel().getBlockState(column).getBlock() instanceof BedBlock))
                        helper.getLevel().setBlockAndUpdate(column, Blocks.STONE.defaultBlockState());
                    helper.getLevel().setBlockAndUpdate(column.above(), Blocks.STONE.defaultBlockState());
                }
                helper.assertFalse(furniture.release(helper.getLevel(), fixture.guest, lease[0]), "Living sleeper escaped a blocked exit");
                helper.assertTrue(fixture.guest.isSleeping(), "Blocked live cleanup lost sleeping state");
                // Normal LivingEntity.hurt already wakes a sleeper before death.
                // Exercise the actual terminal-removal hook while ownership is
                // still present, rather than falsely crediting vanilla wakeup.
                Vec3 sleepingPosition = fixture.guest.position();
                fixture.guest.setHealth(0); fixture.guest.remove(Entity.RemovalReason.KILLED);
                helper.assertTrue(fixture.guest.position().equals(sleepingPosition), "Terminal cleanup moved the removed body to a blocked fallback");
                killed[0] = true;
            }
            if (killed[0] && fixture.guest.isRemoved()) {
                var bed = helper.getLevel().getBlockState(lease[0].spot().furniture());
                helper.assertTrue(bed.is(Blocks.RED_BED) && !bed.getValue(BedBlock.OCCUPIED), "Terminal cleanup retained occupied bed or removed furniture");
                helper.assertTrue(FurnitureReservations.readLease(fixture.guest.getPersistentData()) == null, "Terminal guest kept its furniture lease");
                helper.assertTrue(fixture.stranger.isAlive(), "Terminal cleanup affected an unrelated merchant");
                finished[0] = true; helper.succeed();
            }
        });
        helper.runAtTickTime(350, () -> helper.assertTrue(finished[0], "Terminal bed lifecycle incomplete: " + describe(fixture.guest, lease[0])));
    }
    private static void walkToContact(GameTestHelper helper, DungeonsMerchant guest, InnFurniture.Lease lease) {
        // Vanilla moveTo(x,y,z,speed) requests accuracy1, allowing the path to
        // finish in the neighbouring block. Furniture requires the real exact
        // standing contact, just as the production visitor's accuracy0 route.
        var path = guest.getNavigation().createPath(lease.spot().contact(), 0);
        helper.assertTrue(path != null && path.canReach() && guest.getNavigation().moveTo(path, .8),
                "Actual exact-contact path failed: " + describe(guest, lease));
    }
    private static String describe(DungeonsMerchant guest, InnFurniture.Lease lease) {
        var path = guest.getNavigation().getPath();
        return "position=" + guest.position() + ",ground=" + guest.onGround() + ",noAI=" + guest.isNoAi()
                + ",passenger=" + guest.isPassenger() + ",sleeping=" + guest.isSleeping()
                + ",path=" + (path == null ? "null" : path.getNextNodeIndex() + "/" + path.getNodeCount())
                + ",lease=" + lease + ",contactDistanceSquared=" + (lease == null ? -1
                : guest.position().distanceToSqr(Vec3.atBottomCenterOf(lease.spot().contact())));
    }
    private record Fixture(InnRegistry.Inn inn, DungeonsMerchant guest, DungeonsMerchant stranger) { }
    private static Fixture setup(GameTestHelper helper) {
        var level = helper.getLevel();
        var min = helper.absolutePos(new BlockPos(-48, 0, -48)); var max = helper.absolutePos(new BlockPos(68, 0, 60));
        var chunks = CHUNKS.computeIfAbsent(level, ignored -> new HashSet<>());
        for (int x = min.getX() >> 4; x <= max.getX() >> 4; x++) for (int z = min.getZ() >> 4; z <= max.getZ() >> 4; z++) {
            if (level.setChunkForced(x, z, true)) chunks.add(new ChunkPos(x, z)); level.getChunk(x, z);
        }
        for (int x = 0; x < 20; x++) for (int z = 0; z < 12; z++) helper.setBlock(new BlockPos(x, 0, z), Blocks.STONE);
        helper.setBlock(new BlockPos(5, 1, 4), Blocks.OAK_SLAB);
        helper.setBlock(new BlockPos(9, 1, 5), Blocks.RED_BED.defaultBlockState().setValue(BedBlock.PART, BedPart.FOOT).setValue(BedBlock.FACING, Direction.NORTH));
        helper.setBlock(new BlockPos(9, 1, 4), Blocks.RED_BED.defaultBlockState().setValue(BedBlock.PART, BedPart.HEAD).setValue(BedBlock.FACING, Direction.NORTH));
        var guest = merchant(helper, new Vec3(1.5, 1, 2.5)); var stranger = merchant(helper, new Vec3(16.5, 1, 9.5));
        UUID id = UUID.randomUUID(), owner = UUID.randomUUID();
        BlockPos low = helper.absolutePos(new BlockPos(1, 1, 1)), high = helper.absolutePos(new BlockPos(18, 3, 10));
        var saved = InnSavedData.get(level.getServer());
        var result = saved.configure(id, owner, level.dimension().location().toString(), point(helper, 3, 1, 3),
                new InnBounds(low.getX(), low.getY(), low.getZ(), high.getX(), high.getY(), high.getZ()),
                point(helper, 1, 1, 2), point(helper, 3, 1, 6), point(helper, -5, 1, 2), 0);
        helper.assertTrue(result.decision() == InnRegistry.Decision.OK, "Could not reserve isolated marked inn fixture");
        INNS.computeIfAbsent(level, ignored -> new HashSet<>()).add(id);
        long now = level.getGameTime(); helper.assertTrue(saved.reserve(id, guest.getUUID(), 1, now), "Guest reservation missing");
        saved.advance(id, guest.getUUID(), InnRegistry.GuestPhase.RESERVED, InnRegistry.GuestPhase.TRAVELLING, now);
        saved.advance(id, guest.getUUID(), InnRegistry.GuestPhase.TRAVELLING, InnRegistry.GuestPhase.ARRIVED, now);
        return new Fixture(saved.find(id), guest, stranger);
    }
    private static InnBounds.Point point(GameTestHelper helper, int x, int y, int z) {
        var p = helper.absolutePos(new BlockPos(x, y, z)); return new InnBounds.Point(p.getX(), p.getY(), p.getZ());
    }
    private static DungeonsMerchant merchant(GameTestHelper helper, Vec3 position) {
        var merchant = ModPeacefulEntities.merchantType("village_merchant").create(helper.getLevel());
        helper.assertTrue(merchant != null, "Merchant factory missing"); merchant.setPos(helper.absoluteVec(position)); merchant.removeFreeWill();
        merchant.setPersistenceRequired(); track(helper, merchant); helper.assertTrue(helper.getLevel().addFreshEntity(merchant), "Could not spawn actual merchant"); return merchant;
    }
    private static void track(GameTestHelper helper, Entity entity) { ACTORS.computeIfAbsent(helper.getLevel(), ignored -> new HashSet<>()).add(entity); }
}
