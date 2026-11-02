package de.laugenbreze.dungeonsalive.settlement.inn;

import com.mojang.authlib.GameProfile;
import de.laugenbreze.dungeonsalive.DungeonsAlive;
import de.laugenbreze.dungeonsalive.entity.peaceful.DungeonsMerchant;
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
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.util.FakePlayer;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;
import org.jetbrains.annotations.Nullable;

/** Draft integration cases; require actual Root registrations and the normal merchant goals/events. */
@GameTestHolder(DungeonsAlive.MOD_ID)
@PrefixGameTestTemplate(false)
public final class InnIntegrationGameTests {
    private static final String CYCLE = "inn_actual_visit", LOSS = "inn_actual_loss";
    private record Clock(long time, boolean daylight) { }
    private static final Map<ServerLevel, Clock> CLOCKS = new IdentityHashMap<>();
    private static final Map<ServerLevel, Set<Entity>> ACTORS = new IdentityHashMap<>();
    private static final Map<ServerLevel, Set<ServerPlayer>> PLAYERS = new IdentityHashMap<>();
    private static final Map<ServerLevel, Set<UUID>> INNS = new IdentityHashMap<>();
    private static final Map<ServerLevel, Set<ChunkPos>> CHUNKS = new IdentityHashMap<>();
    private InnIntegrationGameTests() { }
    record Fixture(FakePlayer owner, BlockPos anchor, BlockPos stock, BlockPos privateStock, BlockPos door,
                   BlockPos entrance, BlockPos arrival, UUID innId) { }

    @AfterBatch(batch = CYCLE) public static void cleanupCycle(ServerLevel level) { cleanup(level); }
    @AfterBatch(batch = LOSS) public static void cleanupLoss(ServerLevel level) { cleanup(level); }
    public static void cleanup(ServerLevel level) {
        try {
            var players = PLAYERS.remove(level);
            if (players != null) players.forEach(player -> level.removePlayerImmediately(player, Entity.RemovalReason.DISCARDED));
            var actors = ACTORS.remove(level); if (actors != null) actors.forEach(Entity::discard);
            var ids = INNS.remove(level);
            if (ids != null) for (UUID id : ids) {
                var data = InnSavedData.get(level.getServer()); var inn = data.find(id);
                if (inn == null) continue;
                if (inn.guest() != null) {
                    if (inn.guest().phase() == InnRegistry.GuestPhase.RESERVED) data.cancelReservation(id, inn.guest().entity());
                    else {
                        if (inn.guest().phase() != InnRegistry.GuestPhase.LEAVING)
                            data.advance(id, inn.guest().entity(), inn.guest().phase(), InnRegistry.GuestPhase.LEAVING, level.getGameTime());
                        data.finishDeparture(id, inn.guest().entity(), level.getGameTime());
                    }
                }
                data.remove(id, inn.owner(), inn.revision());
            }
        } finally {
            var chunks = CHUNKS.remove(level); if (chunks != null) chunks.forEach(chunk -> level.setChunkForced(chunk.x, chunk.z, false));
            var clock = CLOCKS.remove(level);
            if (clock != null) {
                level.setDayTime(clock.time());
                level.getGameRules().getRule(GameRules.RULE_DAYLIGHT).set(clock.daylight(), level.getServer());
            }
        }
    }

    @GameTest(template = "gametest_empty", templateNamespace = DungeonsAlive.MOD_ID, batch = CYCLE, timeoutTicks = 1800)
    public static void realRegisteredInnVisitorCrossesBridgeAndClosedDoorReloadsItsSeatThenEatsSleepsAndWalksOut(GameTestHelper helper) {
        Fixture fixture = fixture(helper, true);
        var level = helper.getLevel(); var data = InnSavedData.get(level.getServer());
        helper.assertTrue(InnService.status(fixture.owner(), fixture.anchor()).connected(), "Authorized closed wooden inn door rejected a real route");
        helper.assertFalse(level.getBlockState(fixture.door()).getValue(DoorBlock.OPEN), "Read-only route proof opened the world door");
        var stockEntity = level.getBlockEntity(fixture.stock());
        CompoundTag stockSave = stockEntity.saveWithFullMetadata(level.registryAccess());
        var stockReload = BlockEntity.loadStatic(fixture.stock(), level.getBlockState(fixture.stock()), stockSave, level.registryAccess());
        helper.assertTrue(stockReload != null && stockReload.getPersistentData().getUUID(InnService.STOCK_BINDING_KEY).equals(fixture.innId()),
                "Real container NBT lost its explicit stock binding");
        DungeonsMerchant[] guest = {null}; Vec3[] previous = {null}; boolean[] attached = {false};
        boolean[] sawPath = {false}, sawDoor = {false}, sawSeat = {false}, sawBed = {false}, reloading = {false}, reloaded = {false}, finished = {false};
        helper.onEachTick(() -> {
            if (finished[0]) return;
            var inn = data.find(fixture.innId());
            helper.assertTrue(inn != null && !inn.retired(), "Configured inn disappeared during ordinary visit");
            sawDoor[0] |= level.getBlockState(fixture.door()).getValue(DoorBlock.OPEN);
            if (guest[0] == null && inn.guest() != null && level.getEntity(inn.guest().entity()) instanceof DungeonsMerchant actual) {
                guest[0] = actual; track(level, actual); previous[0] = actual.position();
            }
            if (guest[0] == null || reloading[0]) return;
            var actor = guest[0];
            if (!actor.isRemoved()) {
                boolean nowAttached = actor.isPassenger() || actor.isSleeping();
                if (!attached[0] && !nowAttached) helper.assertTrue(actor.position().distanceToSqr(previous[0]) < 1,
                        "Traveller teleported on an ordinary walking segment");
                previous[0] = actor.position(); attached[0] = nowAttached;
                sawPath[0] |= !actor.getNavigation().isDone();
                helper.assertTrue(actor.getTarget() == null && !actor.isNoAi(), "A real visit lost its peaceful AI");
            }
            if (actor.getVehicle() instanceof InnSeatEntity seat) {
                sawSeat[0] = true; track(level, seat);
                if (!reloaded[0]) {
                    var saved = new CompoundTag(); helper.assertTrue(seat.save(saved), "Real seat+passenger save failed");
                    UUID guestId = actor.getUUID(); reloading[0] = true;
                    actor.remove(Entity.RemovalReason.UNLOADED_TO_CHUNK); seat.remove(Entity.RemovalReason.UNLOADED_TO_CHUNK);
                    helper.assertTrue(data.find(fixture.innId()).guest().entity().equals(guestId), "Unload prematurely freed the visitor slot");
                    helper.runAfterDelay(2, () -> {
                        helper.assertTrue(data.find(fixture.innId()).guest().entity().equals(guestId), "Absent guest was replaced during reload gap");
                        var restoredRoot = EntityType.loadEntityRecursive(saved, level, entity -> entity);
                        helper.assertTrue(restoredRoot instanceof InnSeatEntity && level.tryAddFreshEntityWithPassengers(restoredRoot), "Saved root+passenger reload rejected");
                        restoredRoot.getSelfAndPassengers().forEach(entity -> track(level, entity));
                        helper.assertTrue(level.getEntity(guestId) instanceof DungeonsMerchant, "Saved guest identity did not return");
                        guest[0] = (DungeonsMerchant)level.getEntity(guestId);
                        helper.assertTrue(guest[0].getVehicle() == restoredRoot && InnService.visitOf(guest[0]) != null,
                                "Reload lost the actual own passenger/registry binding");
                        previous[0] = guest[0].position(); attached[0] = true;
                        reloaded[0] = true; reloading[0] = false;
                    });
                }
            }
            sawBed[0] |= actor.isSleeping() && actor.getSleepingPos().map(pos -> level.getBlockState(pos).getValue(BedBlock.OCCUPIED)).orElse(false);
            helper.assertTrue(count(level, fixture.privateStock()) == 8, "Guest consumed from the unassigned private neighbour");
            helper.assertTrue(count(level, fixture.stock()) >= 3, "One guest consumed multiple meals across reload");
            if (inn.guest() != null || !actor.isRemoved()) return;
            helper.assertTrue(sawPath[0] && sawDoor[0] && sawSeat[0] && sawBed[0] && reloaded[0],
                    "Visit omitted actual bridge/door/seat-reload/bed interaction");
            helper.assertTrue(count(level, fixture.stock()) == 3 && inn.arrivals() == 1 && data.hadArrival(fixture.owner().getUUID()),
                    "Visit did not consume exactly one assigned meal and record one real arrival");
            helper.assertTrue(actor.position().distanceToSqr(Vec3.atBottomCenterOf(fixture.arrival())) < 2.25,
                    "Guest disappeared before actually reaching its exit");
            helper.assertFalse(level.getBlockState(fixture.door()).getValue(DoorBlock.OPEN), "Guest left its initially closed inn door open");
            helper.assertTrue(inn.nextVisitAfter() > level.getGameTime(), "Departure lost its persistent population pause");
            finished[0] = true; helper.succeed();
        });
        helper.runAtTickTime(1790, () -> helper.assertTrue(finished[0], "Visit incomplete: " + describe(data.find(fixture.innId()), guest[0])));
    }

    @GameTest(template = "gametest_empty", templateNamespace = DungeonsAlive.MOD_ID, batch = LOSS, timeoutTicks = 1400)
    public static void foreignConfigurationReplacementStockBrokenBridgeAndUnloadedRetirementNeverCreateAReplacementGuest(GameTestHelper helper) {
        Fixture fixture = fixture(helper, false); var level = helper.getLevel(); var data = InnSavedData.get(level.getServer());
        var inn = data.find(fixture.innId());
        var stranger = new FakePlayer(level, new GameProfile(UUID.randomUUID(), "inn-stranger")); stranger.setGameMode(GameType.SURVIVAL);
        stranger.setPos(fixture.owner().position());
        helper.assertTrue(InnService.configure(stranger, fixture.anchor(), 1, inn.bounds(), fixture.entrance(), fixture.stock(), fixture.arrival()).code()
                        == InnService.Code.NOT_OWNER, "Foreign player reconfigured the actual owned anchor");
        helper.assertTrue(InnService.configure(fixture.owner(), fixture.anchor(), 0, inn.bounds(), fixture.entrance(), fixture.stock(), fixture.arrival()).code()
                        == InnService.Code.STALE_REVISION, "Stale owner request changed the confirmed room");
        var privateDoor = Blocks.OAK_DOOR.defaultBlockState().setValue(DoorBlock.FACING, Direction.EAST).setValue(DoorBlock.OPEN, false);
        helper.setBlock(new BlockPos(7, 2, 5), privateDoor.setValue(DoorBlock.HALF, DoubleBlockHalf.LOWER));
        helper.setBlock(new BlockPos(7, 3, 5), privateDoor.setValue(DoorBlock.HALF, DoubleBlockHalf.UPPER));
        helper.assertFalse(InnDoorGoal.allowed(level, inn, helper.absolutePos(new BlockPos(7, 2, 5))), "Visitor gained rights to an outside private wooden door");
        helper.assertFalse(level.getBlockState(helper.absolutePos(new BlockPos(7, 2, 5))).getValue(DoorBlock.OPEN), "Private door opened during a rights check");
        helper.setBlock(new BlockPos(7, 3, 5), Blocks.AIR); helper.setBlock(new BlockPos(7, 2, 5), Blocks.AIR);
        DungeonsMerchant[] guest = {null}; UUID[] identity = {null}; Vec3[] previous = {null}, reloadStart = {null};
        long[] brokenAt = {-1}; boolean[] pending = {false}, reloaded = {false}, finished = {false};
        helper.onEachTick(() -> {
            if (finished[0]) return;
            var current = data.find(fixture.innId());
            if (guest[0] == null && current != null && current.guest() != null && level.getEntity(current.guest().entity()) instanceof DungeonsMerchant actor) {
                guest[0] = actor; identity[0] = actor.getUUID(); previous[0] = actor.position(); track(level, actor);
            }
            if (guest[0] == null || pending[0]) return;
            var actor = guest[0];
            if (!actor.isRemoved()) {
                helper.assertTrue(actor.position().distanceToSqr(previous[0]) < 1, "Loss recovery teleported the travelling guest");
                previous[0] = actor.position();
            }
            if (brokenAt[0] < 0 && actor.distanceToSqr(Vec3.atBottomCenterOf(fixture.arrival())) > 4) {
                helper.setBlock(new BlockPos(9, 1, 5), Blocks.WATER);
                helper.getLevel().setBlockAndUpdate(fixture.stock(), Blocks.AIR.defaultBlockState());
                helper.getLevel().setBlockAndUpdate(fixture.stock(), Blocks.BARREL.defaultBlockState());
                ((Container)level.getBlockEntity(fixture.stock())).setItem(0, new ItemStack(Items.BREAD, 12));
                helper.assertFalse(level.getBlockEntity(fixture.stock()).getPersistentData().hasUUID(InnService.STOCK_BINDING_KEY), "Replacement inherited private authorization");
                helper.assertTrue(InnService.status(fixture.owner(), fixture.anchor()).state() == InnStatus.State.MISSING_STOCK,
                        "Same-position replacement container was silently authorized");
                brokenAt[0] = level.getGameTime();
            }
            if (brokenAt[0] >= 0 && !reloaded[0] && level.getGameTime() - brokenAt[0] >= 60) {
                helper.assertTrue(actor.getX() < helper.absolutePos(new BlockPos(9, 2, 5)).getX(), "Guest crossed a missing bridge span");
                var saved = new CompoundTag(); helper.assertTrue(actor.save(saved), "Real travelling guest could not save");
                actor.remove(Entity.RemovalReason.UNLOADED_TO_CHUNK); pending[0] = true;
                level.setBlockAndUpdate(fixture.anchor(), Blocks.AIR.defaultBlockState());
                var retired = data.find(fixture.innId());
                helper.assertTrue(retired != null && retired.retired() && retired.guest().entity().equals(identity[0])
                                && retired.guest().phase() == InnRegistry.GuestPhase.LEAVING,
                        "Anchor removal discarded or replaced its absent guest reservation");
                helper.runAfterDelay(2, () -> {
                    var restored = EntityType.loadEntityRecursive(saved, level, entity -> entity);
                    helper.assertTrue(restored instanceof DungeonsMerchant && level.tryAddFreshEntityWithPassengers(restored), "Retired traveller NBT reload rejected");
                    guest[0] = (DungeonsMerchant)restored; track(level, restored);
                    helper.assertTrue(restored.getUUID().equals(identity[0]) && InnService.visitOf(guest[0]) != null, "Removed anchor blocked the saved departure role");
                    previous[0] = restored.position(); reloadStart[0] = restored.position(); pending[0] = false; reloaded[0] = true;
                });
                return;
            }
            if (!reloaded[0] || data.find(fixture.innId()) != null || !guest[0].isRemoved()) return;
            helper.assertTrue(guest[0].distanceToSqr(reloadStart[0]) > 1 && guest[0].position().distanceToSqr(Vec3.atBottomCenterOf(fixture.arrival())) < 2.25,
                    "Retired guest did not actually walk back after reload");
            helper.assertTrue(count(level, fixture.stock()) == 12 && count(level, fixture.privateStock()) == 8,
                    "Loss recovery consumed unauthorized replacement/private food");
            helper.assertTrue(level.getBlockState(fixture.anchor()).isAir() && level.getBlockState(helper.absolutePos(new BlockPos(9, 1, 5))).is(Blocks.WATER),
                    "Visitor rebuilt the removed anchor or bridge");
            finished[0] = true; helper.succeed();
        });
        helper.runAtTickTime(1390, () -> helper.assertTrue(finished[0], "Retirement incomplete: " + describe(data.find(fixture.innId()), guest[0])));
    }

    static Fixture fixture(GameTestHelper helper, boolean night) {
        var level = helper.getLevel();
        if (night) {
            CLOCKS.putIfAbsent(level, new Clock(level.getDayTime(), level.getGameRules().getBoolean(GameRules.RULE_DAYLIGHT)));
            level.getGameRules().getRule(GameRules.RULE_DAYLIGHT).set(false, level.getServer()); level.setDayTime(13_000);
        }
        var min = helper.absolutePos(new BlockPos(-42, 0, -42)); var max = helper.absolutePos(new BlockPos(61, 0, 53));
        var chunks = CHUNKS.computeIfAbsent(level, ignored -> new HashSet<>());
        for (int x = min.getX() >> 4; x <= max.getX() >> 4; x++) for (int z = min.getZ() >> 4; z <= max.getZ() >> 4; z++) {
            if (level.setChunkForced(x, z, true)) chunks.add(new ChunkPos(x, z)); level.getChunk(x, z);
        }
        for (int x = 0; x < 20; x++) for (int z = 0; z < 12; z++) {
            helper.setBlock(new BlockPos(x, 0, z), Blocks.STONE);
            helper.setBlock(new BlockPos(x, 1, z), x >= 8 && x <= 10 && z != 5 ? Blocks.WATER : Blocks.OAK_PLANKS);
            for (int y = 2; y < 5; y++) helper.setBlock(new BlockPos(x, y, z), Blocks.AIR);
        }
        for (int z = 0; z < 12; z++) if (z != 5) for (int y = 2; y < 4; y++) helper.setBlock(new BlockPos(13, y, z), Blocks.STONE);
        var door = Blocks.OAK_DOOR.defaultBlockState().setValue(DoorBlock.FACING, Direction.EAST).setValue(DoorBlock.OPEN, false);
        helper.setBlock(new BlockPos(13, 2, 5), door.setValue(DoorBlock.HALF, DoubleBlockHalf.LOWER));
        helper.setBlock(new BlockPos(13, 3, 5), door.setValue(DoorBlock.HALF, DoubleBlockHalf.UPPER));
        helper.setBlock(new BlockPos(15, 2, 4), Blocks.OAK_SLAB);
        var bed = Blocks.RED_BED.defaultBlockState().setValue(BedBlock.FACING, Direction.NORTH);
        helper.setBlock(new BlockPos(17, 2, 5), bed.setValue(BedBlock.PART, BedPart.FOOT));
        helper.setBlock(new BlockPos(17, 2, 4), bed.setValue(BedBlock.PART, BedPart.HEAD));
        BlockPos stock = helper.absolutePos(new BlockPos(17, 2, 8)), privateStock = helper.absolutePos(new BlockPos(18, 2, 8));
        level.setBlockAndUpdate(stock, Blocks.BARREL.defaultBlockState()); level.setBlockAndUpdate(privateStock, Blocks.BARREL.defaultBlockState());
        ((Container)level.getBlockEntity(stock)).setItem(0, new ItemStack(Items.BREAD, 4));
        ((Container)level.getBlockEntity(privateStock)).setItem(0, new ItemStack(Items.BREAD, 8));
        var owner = new FakePlayer(level, new GameProfile(UUID.randomUUID(), "inn-owner")); owner.setGameMode(GameType.SURVIVAL);
        owner.setPos(helper.absoluteVec(new Vec3(16.5, 2, 10.5))); level.addNewPlayer(owner);
        PLAYERS.computeIfAbsent(level, ignored -> new HashSet<>()).add(owner);
        BlockPos anchor = helper.absolutePos(new BlockPos(16, 2, 8));
        owner.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(InnContent.ANCHOR_ITEM.get()));
        var hit = new BlockHitResult(Vec3.atCenterOf(anchor.below()).add(0, .5, 0), Direction.UP, anchor.below(), false);
        helper.assertTrue(InnContent.ANCHOR_ITEM.get().useOn(new UseOnContext(owner, InteractionHand.MAIN_HAND, hit)).consumesAction(),
                "Actual registered inn BlockItem could not place its owner-bound anchor");
        owner.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
        helper.assertTrue(level.getBlockEntity(anchor) instanceof InnAnchorBlockEntity actual && owner.getUUID().equals(actual.ownerId()), "Real placed anchor lost owner");
        var low = helper.absolutePos(new BlockPos(12, 2, 2)); var high = helper.absolutePos(new BlockPos(19, 4, 9));
        BlockPos entrance = helper.absolutePos(new BlockPos(14, 2, 5)), arrival = helper.absolutePos(new BlockPos(3, 2, 5));
        var result = InnService.configure(owner, anchor, 0, new InnBounds(low.getX(), low.getY(), low.getZ(), high.getX(), high.getY(), high.getZ()), entrance, stock, arrival);
        helper.assertTrue(result.code() == InnService.Code.OK && result.innId() != null && result.revision() == 1,
                "Actual server-authoritative inn configuration failed: " + result.code());
        INNS.computeIfAbsent(level, ignored -> new HashSet<>()).add(result.innId());
        helper.assertTrue(level.getBlockEntity(stock).getPersistentData().getUUID(InnService.STOCK_BINDING_KEY).equals(result.innId())
                        && !level.getBlockEntity(privateStock).getPersistentData().hasUUID(InnService.STOCK_BINDING_KEY),
                "Confirmed stock binding affected an unassigned neighbour");
        helper.assertFalse(level.getBlockState(helper.absolutePos(new BlockPos(13, 2, 5))).getValue(DoorBlock.OPEN), "Fixture door was not initially closed");
        return new Fixture(owner, anchor, stock, privateStock, helper.absolutePos(new BlockPos(13, 2, 5)), entrance, arrival, result.innId());
    }
    private static int count(ServerLevel level, BlockPos pos) { return ((Container)level.getBlockEntity(pos)).getItem(0).getCount(); }
    private static void track(ServerLevel level, Entity actor) { ACTORS.computeIfAbsent(level, ignored -> new HashSet<>()).add(actor); }
    private static String describe(@Nullable InnRegistry.Inn inn, @Nullable DungeonsMerchant guest) {
        return "inn=" + (inn == null ? "gone" : inn.guest()) + ", guest=" + (guest == null ? "missing" : guest.position()
                + ", removed=" + guest.isRemoved() + ", passenger=" + guest.isPassenger() + ", sleeping=" + guest.isSleeping()
                + ", path=" + guest.getNavigation().getPath());
    }
}
