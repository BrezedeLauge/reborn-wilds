package de.laugenbreze.dungeonsalive.settlement.inn;

import de.laugenbreze.dungeonsalive.entity.peaceful.DungeonsMerchant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.WeakHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

/** Furniture in an explicitly owner-marked inn, never a global stairs/bed interaction override. */
public final class FurnitureReservations implements InnFurniture {
    static final String LEASE_KEY = "DungeonsInnFurniture";
    private static final int SCAN_STEP = 256, WORLD_SCAN_BUDGET = 1024, REFRESH_TICKS = 200;
    private final Map<ServerLevel, WorldState> worlds = new WeakHashMap<>();

    @Override public Snapshot inspect(ServerLevel level, InnRegistry.Inn inn) {
        if (inn.retired()) return new Snapshot(List.of(), List.of(), true, true);
        if (!dimension(level, inn) || !loaded(level, inn.bounds(), 2)) return new Snapshot(List.of(), List.of(), false, false);
        WorldState world = worlds.computeIfAbsent(level, ignored -> new WorldState());
        long now = level.getGameTime();
        if (world.tick != now) { world.tick = now; world.remaining = WORLD_SCAN_BUDGET; }
        world.scans.keySet().removeIf(id -> InnSavedData.get(level.getServer()).find(id) == null);
        Scan scan = world.scans.get(inn.id());
        if (scan == null || scan.revision != inn.revision() || scan.complete && (now < scan.finished || now - scan.finished >= REFRESH_TICKS)) {
            if (world.scans.size() >= InnRegistry.MAX_INNS && scan == null) return new Snapshot(List.of(), List.of(), true, false);
            Scan previous = scan;
            scan = new Scan(inn.revision());
            if (previous != null && previous.revision == inn.revision() && previous.complete) {
                // A bounded refresh must not revoke an unchanged functional
                // room. Continue revalidating the previous finished snapshot
                // until the replacement scan is complete.
                scan.previousSeats = List.copyOf(previous.seats); scan.previousBeds = List.copyOf(previous.beds);
                scan.previousComplete = true;
            }
            world.scans.put(inn.id(), scan);
        }
        int allowance = Math.min(SCAN_STEP, world.remaining), reads = 0;
        var bounds = inn.bounds();
        while (!scan.complete && reads < allowance) {
            int width = bounds.maxX() - bounds.minX() + 1, depth = bounds.maxZ() - bounds.minZ() + 1;
            int index = scan.cursor++;
            var pos = new BlockPos(bounds.minX() + index % width, bounds.minY() + index / (width * depth), bounds.minZ() + index / width % depth);
            reads++;
            var state = level.getBlockState(pos);
            Kind kind = seatBlock(state) ? Kind.SEAT : state.getBlock() instanceof BedBlock && state.getValue(BedBlock.PART) == BedPart.HEAD ? Kind.BED : null;
            if (kind != null) {
                Spot spot = findSpot(level, inn, pos, kind);
                if (spot != null) offer(kind == Kind.SEAT ? scan.seats : scan.beds, spot, point(inn.entrance()));
            }
            if (scan.cursor >= bounds.volume()) { scan.complete = true; scan.finished = now; }
        }
        world.remaining -= reads;
        // Recheck the bounded candidate set even within a cached scan: removed or
        // occupied furniture cannot authorize a claim from an old UI snapshot.
        List<Spot> seats = (scan.complete || !scan.previousComplete ? scan.seats : scan.previousSeats).stream()
                .filter(spot -> validSpot(level, inn, spot, Kind.SEAT)).toList();
        List<Spot> beds = (scan.complete || !scan.previousComplete ? scan.beds : scan.previousBeds).stream()
                .filter(spot -> validSpot(level, inn, spot, Kind.BED)).toList();
        return new Snapshot(seats, beds, true, scan.complete || scan.previousComplete);
    }

    @Override @Nullable public Lease resume(ServerLevel level, DungeonsMerchant guest, InnRegistry.Inn inn) {
        Lease old = readLease(guest.getPersistentData());
        if (old == null) return null;
        boolean ownSeat = old.kind() == Kind.SEAT && guest.getVehicle() instanceof InnSeatEntity seat && seat.matches(old);
        boolean ownBed = old.kind() == Kind.BED && !guest.isPassenger() && guest.isSleeping()
                && guest.getSleepingPos().filter(old.spot().furniture()::equals).isPresent();
        if (!old.innId().equals(inn.id()) || !authorized(level, guest, old)
                || !guest.isAlive() || guest.isNoAi() || guest.getTradingPlayer() != null || guest.hurtTime > 0
                || !validSpot(level, inn, old.spot(), old.kind())) {
            // A retired/reconfigured inn may still contain a legitimately saved
            // sleeper. Release only this guest's exact saved attachment; never
            // disturb an unrelated mount or another occupant's bed state.
            return !release(level, guest, old) && (ownSeat || ownBed) ? old : null;
        }
        return ownSeat || ownBed ? old : null;
    }

    @Override @Nullable public Lease reserve(ServerLevel level, DungeonsMerchant guest, InnRegistry.Inn inn, Kind kind) {
        if (!assigned(level, guest, inn) || !guest.isAlive() || guest.isNoAi() || guest.getTradingPlayer() != null) return null;
        Lease old = readLease(guest.getPersistentData());
        if (old != null) {
            if (old.kind() == kind && authorized(level, guest, old) && validSpot(level, inn, old.spot(), kind)) return old;
            release(level, guest, old);
            if (readLease(guest.getPersistentData()) != null) return null;
        }
        Snapshot snapshot = inspect(level, inn);
        if (!snapshot.loaded() || !snapshot.complete()) return null;
        var choices = kind == Kind.SEAT ? snapshot.seats() : snapshot.beds();
        for (Spot spot : choices) {
            if (kind == Kind.BED && level.getBlockState(spot.furniture()).getValue(BedBlock.OCCUPIED)) continue;
            var lease = new Lease(inn.id(), guest.getUUID(), inn.revision(), kind, spot);
            if (level.getEntitiesOfClass(InnSeatEntity.class, new AABB(spot.furniture()).inflate(2), seat -> seat.matchesFurniture(spot.furniture())).stream().findAny().isPresent()) continue;
            guest.getPersistentData().put(LEASE_KEY, writeLease(lease));
            return lease;
        }
        return null;
    }

    @Override public boolean use(ServerLevel level, DungeonsMerchant guest, Lease lease) {
        if (!authorized(level, guest, lease) || !lease.equals(readLease(guest.getPersistentData()))) return false;
        var inn = InnSavedData.get(level.getServer()).find(lease.innId());
        if (inn == null || !validSpot(level, inn, lease.spot(), lease.kind()) || !guest.isAlive() || guest.isNoAi()
                || guest.getTradingPlayer() != null || guest.getTarget() != null || guest.hurtTime > 0
                || guest.getBbWidth() > .7 || guest.getBbHeight() > 2.1) return false;
        if (lease.kind() == Kind.SEAT && guest.getVehicle() instanceof InnSeatEntity seat && seat.matches(lease)) return true;
        if (lease.kind() == Kind.BED && guest.isSleeping() && guest.getSleepingPos().filter(lease.spot().furniture()::equals).isPresent()) return true;
        if (guest.isPassenger() || guest.isSleeping() || !guest.onGround()
                || guest.position().distanceToSqr(Vec3.atBottomCenterOf(lease.spot().contact())) > 1
                || guest.position().distanceToSqr(Vec3.atCenterOf(lease.spot().furniture())) > 4) return false;
        guest.getNavigation().stop();
        if (lease.kind() == Kind.BED) {
            if (level.getBlockState(lease.spot().furniture()).getValue(BedBlock.OCCUPIED) || otherSleeper(level, guest, lease.spot().furniture())) return false;
            guest.startSleeping(lease.spot().furniture()); // Vanilla's local bed attachment, after real route contact.
            return guest.isSleeping();
        }
        var seat = InnFurnitureContent.SEAT.get().create(level);
        if (seat == null) return false;
        seat.configure(lease);
        if (!level.addFreshEntity(seat)) return false;
        if (!guest.startRiding(seat, true)) { seat.discard(); return false; }
        seat.positionRider(guest);
        return guest.getVehicle() == seat;
    }

    @Override public boolean release(ServerLevel level, DungeonsMerchant guest, Lease lease) {
        if (guest.level() != level || !guest.getUUID().equals(lease.guestId())) return false;
        Lease saved = readLease(guest.getPersistentData());
        if (saved == null) return true;
        if (!lease.equals(saved)) return false;
        if (!loaded(level, lease.spot().furniture(), 4)) return false; // Never make vanilla search an unknown dismount column.
        if (guest.getVehicle() instanceof InnSeatEntity seat && seat.matches(lease)) {
            if (seat.position().distanceToSqr(Vec3.atBottomCenterOf(lease.spot().contact())) > 4
                    || !clearStanding(level, lease.spot().contact())) return false;
            guest.stopRiding();
            if (guest.getVehicle() == seat) return false; // Respect another mod cancelling a dismount.
            seat.discard();
        }
        if (guest.getSleepingPos().filter(lease.spot().furniture()::equals).isPresent()) {
            if (guest.isPassenger()) return false;
            var bed = level.getBlockState(lease.spot().furniture());
            boolean actualBed = bed.getBlock() instanceof BedBlock;
            Vec3 exit = actualBed ? BedBlock.findStandUpPosition(guest.getType(), level, lease.spot().furniture(),
                    bed.getValue(BedBlock.FACING), guest.getYRot()).orElse(null) : Vec3.atBottomCenterOf(lease.spot().contact());
            if (exit == null || exit.distanceToSqr(Vec3.atBottomCenterOf(lease.spot().furniture())) > 16
                    || !clearStanding(level, BlockPos.containing(exit))
                    || !level.getWorldBorder().isWithinBounds(guest.getDimensions(Pose.STANDING).makeBoundingBox(exit))
                    || level.getBlockCollisions(guest, guest.getDimensions(Pose.STANDING).makeBoundingBox(exit)).iterator().hasNext()) return false;
            if (otherSleeper(level, guest, lease.spot().furniture()) || !actualBed) {
                // Removed/replaced furniture cannot provide vanilla's bed exit.
                // This is the checked adjacent furniture attachment, not a route skip.
                guest.clearSleepingPos(); guest.setPose(Pose.STANDING); guest.setPos(exit);
            } else guest.stopSleeping();
        }
        guest.getPersistentData().remove(LEASE_KEY);
        return true;
    }

    /** Terminal removal has no living body to route out; clear only its exact saved ownership. */
    void releaseTerminated(ServerLevel level, DungeonsMerchant guest) {
        if (guest.level() != level || guest.getRemovalReason() != net.minecraft.world.entity.Entity.RemovalReason.KILLED
                && guest.getRemovalReason() != net.minecraft.world.entity.Entity.RemovalReason.DISCARDED) return;
        Lease lease = readLease(guest.getPersistentData());
        if (lease == null || !guest.getUUID().equals(lease.guestId())) return;
        BlockPos furniture = lease.spot().furniture();
        // Loaded entity lookup does not load neighbouring chunks. Entity's own
        // terminal removal has normally already detached its dead passenger.
        for (InnSeatEntity seat : level.getEntitiesOfClass(InnSeatEntity.class, new AABB(furniture).inflate(2),
                seat -> seat.matches(lease) && seat.getPassengers().stream().allMatch(p -> p == guest))) {
            if (guest.getVehicle() == seat) guest.removeVehicle(); // No stand-up search or position change for a removed entity.
            if (seat.getPassengers().isEmpty()) seat.discard();
        }
        if (lease.kind() == Kind.BED && guest.getSleepingPos().filter(furniture::equals).isPresent()) {
            if (level.hasChunkAt(furniture) && !otherSleeper(level, guest, furniture)) {
                var bed = level.getBlockState(furniture);
                if (bed.getBlock() instanceof BedBlock) {
                    // Update only loaded halves directly; no neighbour search or
                    // unloaded dismount column is needed for terminal cleanup.
                    level.setBlock(furniture, bed.setValue(BedBlock.OCCUPIED, false), 18);
                    BlockPos otherPos = furniture.relative(bed.getValue(BedBlock.PART) == BedPart.HEAD
                            ? bed.getValue(BedBlock.FACING).getOpposite() : bed.getValue(BedBlock.FACING));
                    if (level.hasChunkAt(otherPos)) {
                        var other = level.getBlockState(otherPos);
                        if (other.getBlock() == bed.getBlock() && other.getValue(BedBlock.FACING) == bed.getValue(BedBlock.FACING)
                                && other.getValue(BedBlock.PART) != bed.getValue(BedBlock.PART))
                            level.setBlock(otherPos, other.setValue(BedBlock.OCCUPIED, false), 18);
                    }
                }
            }
            // The entity is already terminally removed. A standing-pose change
            // would enlarge its bounding box and invoke vanilla's collision
            // position search even for a dead body. Only clear its ownership.
            guest.clearSleepingPos();
        }
        guest.getPersistentData().remove(LEASE_KEY);
    }

    static boolean authorized(ServerLevel level, DungeonsMerchant guest, Lease lease) {
        var inn = InnSavedData.get(level.getServer()).find(lease.innId());
        return inn != null && assigned(level, guest, inn) && inn.revision() == lease.configRevision()
                && guest.getUUID().equals(lease.guestId()) && contains(inn.bounds(), lease.spot().furniture())
                && contains(inn.bounds(), lease.spot().contact());
    }
    private static boolean assigned(ServerLevel level, DungeonsMerchant guest, InnRegistry.Inn inn) {
        var current = InnSavedData.get(level.getServer()).find(inn.id());
        return dimension(level, inn) && guest.level() == level && level.getEntity(guest.getUUID()) == guest
                && inn.guest() != null && inn.guest().entity().equals(guest.getUUID()) && inn.guest().revision() == inn.revision()
                && current != null && !current.retired() && current.revision() == inn.revision() && current.guest() != null
                && current.guest().entity().equals(guest.getUUID())
                && (current.guest().phase() == InnRegistry.GuestPhase.ARRIVED || current.guest().phase() == InnRegistry.GuestPhase.RESTING);
    }
    private static boolean dimension(ServerLevel level, InnRegistry.Inn inn) { return level.dimension().location().toString().equals(inn.dimension()); }
    private static boolean otherSleeper(ServerLevel level, DungeonsMerchant guest, BlockPos bed) {
        return level.getEntitiesOfClass(LivingEntity.class, new AABB(bed).inflate(2), other -> other != guest
                && other.isSleeping() && other.getSleepingPos().filter(bed::equals).isPresent()).size() > 0;
    }

    @Nullable private static Spot findSpot(ServerLevel level, InnRegistry.Inn inn, BlockPos furniture, Kind kind) {
        var state = level.getBlockState(furniture);
        if (kind == Kind.BED) {
            var foot = furniture.relative(state.getValue(BedBlock.FACING).getOpposite());
            if (!contains(inn.bounds(), foot)) return null;
        }
        for (Direction side : Direction.Plane.HORIZONTAL) {
            BlockPos contact = furniture.relative(side);
            if (contains(inn.bounds(), contact) && clearStanding(level, contact)) {
                Spot spot = new Spot(furniture, contact);
                if (validSpot(level, inn, spot, kind)) return spot;
            }
        }
        return null;
    }
    static boolean validSpot(ServerLevel level, InnRegistry.Inn inn, Spot spot, Kind kind) {
        if (!contains(inn.bounds(), spot.furniture()) || !contains(inn.bounds(), spot.contact())
                || !contains(inn.bounds(), spot.furniture().above()) || !contains(inn.bounds(), spot.contact().above())
                || !loaded(level, spot.furniture(), 2) || !clearStanding(level, spot.contact())) return false;
        var state = level.getBlockState(spot.furniture());
        if (kind == Kind.SEAT) {
            if (!seatBlock(state)) return false;
            Vec3 hip = Vec3.atBottomCenterOf(spot.furniture()).add(0, .5, 0);
            var body = new AABB(hip.x - .3, hip.y + .025, hip.z - .3, hip.x + .3, hip.y + 1.25, hip.z + .3);
            return level.getWorldBorder().isWithinBounds(body) && !level.getBlockCollisions(null, body).iterator().hasNext() && !level.containsAnyLiquid(body);
        }
        if (!(state.getBlock() instanceof BedBlock) || state.getValue(BedBlock.PART) != BedPart.HEAD || !state.getFluidState().isEmpty()) return false;
        if (state.getValue(BedBlock.OCCUPIED)) {
            var registered = inn.guest() == null ? null : level.getEntity(inn.guest().entity());
            if (!(registered instanceof DungeonsMerchant sleeper)
                    || sleeper.getSleepingPos().filter(spot.furniture()::equals).isEmpty()) return false;
        }
        BlockPos foot = spot.furniture().relative(state.getValue(BedBlock.FACING).getOpposite());
        var other = level.getBlockState(foot);
        return contains(inn.bounds(), foot) && contains(inn.bounds(), foot.above())
                && other.getBlock() == state.getBlock() && other.getValue(BedBlock.PART) == BedPart.FOOT
                && other.getValue(BedBlock.FACING) == state.getValue(BedBlock.FACING)
                && level.isEmptyBlock(spot.furniture().above()) && level.isEmptyBlock(foot.above());
    }
    private static boolean seatBlock(net.minecraft.world.level.block.state.BlockState state) {
        // A low wooden slab is an unambiguous bench surface; ordinary stairs and
        // terrain do not become chairs merely because somebody right-clicks them.
        return state.is(BlockTags.WOODEN_SLABS) && state.getBlock() instanceof SlabBlock
                && state.getValue(SlabBlock.TYPE) == SlabType.BOTTOM && state.getFluidState().isEmpty();
    }
    static boolean clearStanding(ServerLevel level, BlockPos pos) {
        if (!loaded(level, pos, 2) || pos.getY() <= level.getMinBuildHeight() || pos.getY() + 2 >= level.getMaxBuildHeight()
                || !level.getBlockState(pos.below()).isFaceSturdy(level, pos.below(), Direction.UP)) return false;
        for (int y = -1; y <= 2; y++) {
            var state = level.getBlockState(pos.above(y));
            if (!state.getFluidState().isEmpty() || state.is(Blocks.MAGMA_BLOCK) || state.is(Blocks.FIRE) || state.is(Blocks.SOUL_FIRE)
                    || state.is(Blocks.CACTUS) || state.is(Blocks.CAMPFIRE) || state.is(Blocks.SOUL_CAMPFIRE)
                    || state.is(Blocks.WITHER_ROSE) || state.is(Blocks.SWEET_BERRY_BUSH) || state.is(Blocks.POWDER_SNOW)) return false;
        }
        Vec3 feet = Vec3.atBottomCenterOf(pos);
        var body = new AABB(feet.x - .3, feet.y, feet.z - .3, feet.x + .3, feet.y + 1.95, feet.z + .3);
        return level.getWorldBorder().isWithinBounds(body) && !level.getBlockCollisions(null, body).iterator().hasNext();
    }
    static boolean loaded(ServerLevel level, BlockPos pos, int radius) {
        for (int x = (pos.getX() - radius) >> 4; x <= (pos.getX() + radius) >> 4; x++)
            for (int z = (pos.getZ() - radius) >> 4; z <= (pos.getZ() + radius) >> 4; z++) if (!level.hasChunk(x, z)) return false;
        return true;
    }
    private static boolean loaded(ServerLevel level, InnBounds b, int radius) {
        for (int x = (b.minX() - radius) >> 4; x <= (b.maxX() + radius) >> 4; x++)
            for (int z = (b.minZ() - radius) >> 4; z <= (b.maxZ() + radius) >> 4; z++) if (!level.hasChunk(x, z)) return false;
        return b.minY() > level.getMinBuildHeight() && b.maxY() + 2 < level.getMaxBuildHeight();
    }
    private static boolean contains(InnBounds b, BlockPos p) { return b.contains(p.getX(), p.getY(), p.getZ()); }
    private static BlockPos point(InnBounds.Point p) { return new BlockPos(p.x(), p.y(), p.z()); }
    private static void offer(List<Spot> choices, Spot spot, BlockPos entrance) {
        choices.add(spot); choices.sort(Comparator.comparingDouble(value -> value.contact().distSqr(entrance)));
        if (choices.size() > 8) choices.removeLast();
    }
    static CompoundTag writeLease(Lease lease) {
        var tag = new CompoundTag(); tag.putUUID("Inn", lease.innId()); tag.putUUID("Guest", lease.guestId()); tag.putLong("Revision", lease.configRevision());
        tag.putString("Kind", lease.kind().name()); tag.putLong("Furniture", lease.spot().furniture().asLong()); tag.putLong("Contact", lease.spot().contact().asLong()); return tag;
    }
    @Nullable static Lease readLease(CompoundTag parent) {
        if (!parent.contains(LEASE_KEY, Tag.TAG_COMPOUND)) return null;
        CompoundTag tag = parent.getCompound(LEASE_KEY);
        if (!tag.hasUUID("Inn") || !tag.hasUUID("Guest") || !tag.contains("Revision", Tag.TAG_LONG)
                || !tag.contains("Furniture", Tag.TAG_LONG) || !tag.contains("Contact", Tag.TAG_LONG)) return null;
        try { return new Lease(tag.getUUID("Inn"), tag.getUUID("Guest"), tag.getLong("Revision"), Kind.valueOf(tag.getString("Kind")),
                new Spot(BlockPos.of(tag.getLong("Furniture")), BlockPos.of(tag.getLong("Contact")))); }
        catch (IllegalArgumentException exception) { return null; }
    }
    private static final class WorldState {
        long tick = Long.MIN_VALUE; int remaining;
        final Map<UUID, Scan> scans = new HashMap<>();
    }
    private static final class Scan {
        final long revision; int cursor; long finished; boolean complete, previousComplete;
        final List<Spot> seats = new ArrayList<>(), beds = new ArrayList<>();
        List<Spot> previousSeats = List.of(), previousBeds = List.of();
        Scan(long revision) { this.revision = revision; }
    }
}
