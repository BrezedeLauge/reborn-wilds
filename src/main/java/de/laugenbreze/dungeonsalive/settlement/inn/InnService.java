package de.laugenbreze.dungeonsalive.settlement.inn;

import de.laugenbreze.dungeonsalive.entity.peaceful.DungeonsMerchant;
import de.laugenbreze.dungeonsalive.entity.peaceful.ModPeacefulEntities;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.state.properties.ChestType;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

/** Authoritative room configuration and bounded actual visitor admission; no terrain or inventory manufacture. */
public final class InnService {
    public static final String VISITOR_TAG = "dungeonsalive_inn_visitor", INN_KEY = "DungeonsInn", REVISION_KEY = "DungeonsInnRevision";
    public static final String STOCK_BINDING_KEY = "DungeonsInnStock";
    public enum Code { OK, UNAVAILABLE, NOT_OWNER, OUT_OF_REACH, NO_RIGHTS, INVALID_SELECTION, INVALID_STOCK,
        UNLOADED, STALE_REVISION, BUSY, WORLD_LIMIT, OWNER_LIMIT, OVERLAP, STOCK_ASSIGNED, DATA_BLOCKED }
    public record Result(Code code, @Nullable UUID innId, long revision) { }
    private record RememberedRoute(long revision, List<BlockPos> nodes) { }
    private static final Map<MinecraftServer, InnService> SERVICES = new ConcurrentHashMap<>();
    private final InnFurniture furniture;
    private final Map<UUID, RememberedRoute> routes = new HashMap<>();
    private final Map<UUID, Long> nextAttempt = new HashMap<>();
    private final Map<String, Integer> dimensionCursors = new TreeMap<>();

    private InnService(InnFurniture furniture) { this.furniture = Objects.requireNonNull(furniture); }
    public static void attach(MinecraftServer server, InnFurniture furniture) {
        if (SERVICES.putIfAbsent(server, new InnService(furniture)) != null) throw new IllegalStateException("Inn service already attached");
    }
    public static void detach(MinecraftServer server) { SERVICES.remove(server); }
    @Nullable static InnFurniture furniture(MinecraftServer server) {
        var service = SERVICES.get(server); return service == null ? null : service.furniture;
    }

    public static Result configure(ServerPlayer player, BlockPos anchor, long expectedRevision, InnBounds bounds,
                                   BlockPos entrance, BlockPos stock, BlockPos arrival) {
        var service = SERVICES.get(player.server);
        if (service == null) return new Result(Code.UNAVAILABLE, null, 0);
        ServerLevel level = player.serverLevel();
        if (!InnRouteValidator.loadedArea(level, anchor, 1)) return new Result(Code.UNLOADED, null, 0);
        var blockEntity = level.getBlockEntity(anchor);
        if (!(blockEntity instanceof InnAnchorAccess access) || !player.getUUID().equals(access.ownerId()))
            return new Result(Code.NOT_OWNER, null, 0);
        UUID id = access.innId() == null ? UUID.randomUUID() : access.innId();
        var data = InnSavedData.get(level.getServer()); var old = data.find(id);
        long revision = old == null ? 0 : old.revision();
        if (data.blocked()) return new Result(Code.DATA_BLOCKED, id, revision);
        if (player.position().distanceToSqr(Vec3.atCenterOf(anchor)) > 64) return new Result(Code.OUT_OF_REACH, id, revision);
        if (player.isSpectator() || !player.mayBuild() || !level.mayInteract(player, anchor))
            return new Result(Code.NO_RIGHTS, id, revision);
        if (expectedRevision != revision) return new Result(Code.STALE_REVISION, id, revision);
        if (!loadedBounds(level, bounds) || !InnRouteValidator.loadedArea(level, arrival, 2)) return new Result(Code.UNLOADED, id, revision);
        try {
            // Validate pure geometry before any potentially large selection walk.
            new InnRegistry.Inn(id, player.getUUID(), level.dimension().location().toString(), InnRouteValidator.point(anchor), bounds,
                    InnRouteValidator.point(entrance), InnRouteValidator.point(stock), InnRouteValidator.point(arrival),
                    Math.addExact(revision, 1), null, old == null ? 0 : old.arrivals(), old == null ? 0 : old.nextVisitAfter(), false);
        } catch (IllegalArgumentException | ArithmeticException exception) { return new Result(Code.INVALID_SELECTION, id, revision); }
        if (!legalPosition(level, entrance) || !legalPosition(level, arrival) || !legalPosition(level, stock))
            return new Result(Code.INVALID_SELECTION, id, revision);
        // One explicit configuration transaction, at most 16,384 positions; never a per-tick room scan.
        for (BlockPos position : BlockPos.betweenClosed(bounds.minX(), bounds.minY(), bounds.minZ(), bounds.maxX(), bounds.maxY(), bounds.maxZ()))
            if (!level.mayInteract(player, position)) return new Result(Code.NO_RIGHTS, id, revision);
        if (!level.mayInteract(player, arrival)) return new Result(Code.NO_RIGHTS, id, revision);
        if (stock(level, stock) == null) return new Result(Code.INVALID_STOCK, id, revision);
        if (level.getBlockEntity(stock) instanceof BaseContainerBlockEntity locked && !locked.canOpen(player))
            return new Result(Code.NO_RIGHTS, id, revision);
        var change = data.configure(id, player.getUUID(), level.dimension().location().toString(), InnRouteValidator.point(anchor), bounds,
                InnRouteValidator.point(entrance), InnRouteValidator.point(stock), InnRouteValidator.point(arrival), expectedRevision);
        if (change.decision() != InnRegistry.Decision.OK) return new Result(switch (change.decision()) {
            case NOT_OWNER -> Code.NOT_OWNER;
            case STALE_REVISION -> Code.STALE_REVISION;
            case BUSY -> Code.BUSY;
            case WORLD_LIMIT -> Code.WORLD_LIMIT;
            case OWNER_LIMIT -> Code.OWNER_LIMIT;
            case OVERLAP -> Code.OVERLAP;
            case STOCK_ASSIGNED -> Code.STOCK_ASSIGNED;
            default -> Code.DATA_BLOCKED;
        }, id, revision);
        access.bindInn(id); blockEntity.setChanged();
        // The explicit owner confirmation binds this actual BlockEntity, not
        // merely its coordinates. Replacing it cannot silently assign a new chest.
        var stockEntity = level.getBlockEntity(stock);
        stockEntity.getPersistentData().putUUID(STOCK_BINDING_KEY, id); stockEntity.setChanged();
        service.routes.remove(id); service.nextAttempt.remove(id);
        return new Result(Code.OK, id, change.inn().revision());
    }

    public static InnStatus status(ServerPlayer player, BlockPos anchor) {
        var level = player.serverLevel();
        if (!InnRouteValidator.loadedArea(level, anchor, 1)) return empty(InnStatus.State.UNLOADED);
        if (player.position().distanceToSqr(Vec3.atCenterOf(anchor)) > 64
                || !(level.getBlockEntity(anchor) instanceof InnAnchorAccess access) || !player.getUUID().equals(access.ownerId()))
            return empty(InnStatus.State.NOT_OWNER);
        var service = SERVICES.get(level.getServer());
        if (access.innId() == null) return empty(InnStatus.State.UNCONFIGURED);
        var inn = InnSavedData.get(level.getServer()).find(access.innId());
        if (inn == null) return empty(InnStatus.State.UNCONFIGURED);
        if (!inn.owner().equals(player.getUUID()) || !inn.anchor().equals(InnRouteValidator.point(anchor))) return empty(InnStatus.State.NOT_OWNER);
        return service == null ? new InnStatus(inn.id(), inn.revision(), InnStatus.State.SCANNING, null, 0, 0, 0, false, guestState(level, inn))
                : service.observe(level, inn);
    }

    private InnStatus observe(ServerLevel level, InnRegistry.Inn inn) {
        var guest = guestState(level, inn);
        if (inn.retired()) return report(inn, InnStatus.State.UNCONFIGURED, null, 0, 0, 0, false, guest);
        if (!inn.dimension().equals(level.dimension().location().toString()) || !loadedBounds(level, inn.bounds()))
            return report(inn, InnStatus.State.UNLOADED, null, 0, 0, 0, false, guest);
        // The function anchor must still really exist with the saved owner and identity.
        if (!(level.getBlockEntity(InnRouteValidator.pos(inn.anchor())) instanceof InnAnchorAccess anchor)
                || !inn.owner().equals(anchor.ownerId()) || !inn.id().equals(anchor.innId()))
            return report(inn, InnStatus.State.UNCONFIGURED, InnRouteValidator.pos(inn.anchor()), 0, 0, 0, false, guest);
        var snapshot = furniture.inspect(level, inn);
        var remembered = routes.get(inn.id());
        var route = InnRouteValidator.inspect(level, inn,
                remembered != null && remembered.revision == inn.revision() ? remembered.nodes : List.of());
        if (route.connected()) routes.put(inn.id(), new RememberedRoute(inn.revision(), route.nodes()));
        int seats = snapshot.seats().size(), beds = snapshot.beds().size();
        var inventory = assignedStock(level, inn);
        int food = inventory == null ? 0 : foodCount(inventory);
        InnStatus.State state;
        BlockPos problem = null;
        if (!snapshot.loaded() || !route.loaded()) state = InnStatus.State.UNLOADED;
        else if (!snapshot.complete()) state = InnStatus.State.SCANNING;
        else if (seats == 0) state = InnStatus.State.MISSING_SEAT;
        else if (beds == 0) state = InnStatus.State.MISSING_BED;
        else if (inventory == null) { state = InnStatus.State.MISSING_STOCK; problem = InnRouteValidator.pos(inn.stock()); }
        else if (food == 0) { state = InnStatus.State.NO_FOOD; problem = InnRouteValidator.pos(inn.stock()); }
        else if (!route.connected()) { state = InnStatus.State.BLOCKED_ROUTE; problem = route.problem(); }
        else {
            // Presence alone does not make a chair/bed usable by this real body.
            var probe = InnRouteValidator.probe(level, InnRouteValidator.pos(inn.entrance()), inn);
            boolean seatPath = probe != null && snapshot.seats().stream().anyMatch(spot -> InnRouteValidator.plan(probe, spot.contact(), inn).connected());
            boolean bedPath = probe != null && snapshot.beds().stream().anyMatch(spot -> InnRouteValidator.plan(probe, spot.contact(), inn).connected());
            state = seatPath && bedPath ? InnStatus.State.READY : InnStatus.State.BLOCKED_ROUTE;
        }
        return report(inn, state, problem, seats, beds, food, route.connected(), guest);
    }

    /** Exact opt-in WORLD_STATE keys; each live condition is reobserved at claim time. */
    public static boolean worldState(ServerPlayer player, ResourceLocation condition) {
        if (!condition.getNamespace().equals("dungeonsalive")) return false;
        String kind = condition.getPath();
        if (!List.of("inn_ready", "inn_connected", "guest_arrived").contains(kind)) return false;
        var service = SERVICES.get(player.server); if (service == null) return false;
        if (kind.equals("guest_arrived")) return InnSavedData.get(player.server).hadArrival(player.getUUID());
        for (var inn : InnSavedData.get(player.server).all()) {
            if (!inn.owner().equals(player.getUUID())) continue;
            var level = player.server.getLevel(ResourceKey.create(Registries.DIMENSION, ResourceLocation.parse(inn.dimension())));
            if (level == null) continue;
            var status = service.observe(level, inn);
            if (kind.equals("inn_ready") && status.state() == InnStatus.State.READY
                    || kind.equals("inn_connected") && status.connected()) return true;
        }
        return false;
    }

    /** Called by a real level tick hook: at most two records per100ticks, never loads an absent chunk. */
    public static void tick(ServerLevel level) {
        var service = SERVICES.get(level.getServer());
        if (service == null || Math.floorMod(level.getGameTime(), 100) != 0) return;
        var allInns = InnSavedData.get(level.getServer()).all();
        service.routes.keySet().removeIf(id -> allInns.stream().noneMatch(inn -> inn.id().equals(id)));
        service.nextAttempt.keySet().removeIf(id -> allInns.stream().noneMatch(inn -> inn.id().equals(id)));
        service.dimensionCursors.keySet().removeIf(dimension -> allInns.stream().noneMatch(inn -> inn.dimension().equals(dimension)));
        // Caps include all dimensions and unloaded reservations. Selection advances
        // only this dimension's bounded cursor: ticks of another world cannot
        // continually consume the records this level was supposed to inspect.
        if (allInns.stream().filter(inn -> inn.guest() != null).count() >= InnRegistry.MAX_GUESTS) return;
        String dimension = level.dimension().location().toString();
        var inns = allInns.stream().filter(inn -> inn.dimension().equals(dimension)).toList();
        if (inns.isEmpty()) return;
        int cursor = Math.floorMod(service.dimensionCursors.getOrDefault(dimension, 0), inns.size());
        for (int i = 0; i < Math.min(2, inns.size()); i++) {
            var inn = inns.get(cursor);
            cursor = (cursor + 1) % inns.size();
            if (inn.retired() || inn.guest() != null
                    || level.getGameTime() < inn.nextVisitAfter() || level.getGameTime() < service.nextAttempt.getOrDefault(inn.id(), 0L)
                    || level.players().stream().noneMatch(player -> !player.isSpectator()
                        && player.distanceToSqr(Vec3.atCenterOf(InnRouteValidator.pos(inn.anchor()))) < 48 * 48)) continue;
            service.nextAttempt.put(inn.id(), level.getGameTime() + 200);
            if (service.observe(level, inn).state() == InnStatus.State.READY) service.admit(level, inn);
        }
        service.dimensionCursors.put(dimension, cursor);
    }

    private void admit(ServerLevel level, InnRegistry.Inn inn) {
        BlockPos origin = InnRouteValidator.pos(inn.arrival());
        if (!InnRouteValidator.loadedArea(level, origin, InnRouteValidator.LOADED_RADIUS)) return;
        if (level.getEntitiesOfClass(DungeonsMerchant.class, new AABB(origin).inflate(32, 16, 32), LivingEntity::isAlive).size() >= 6) return;
        var visitor = ModPeacefulEntities.merchantType("village_merchant").create(level); if (visitor == null) return;
        visitor.setPos(InnRouteValidator.feet(level, origin));
        if (!InnRouteValidator.safeStanding(visitor, origin) || !level.noCollision(visitor, visitor.getBoundingBox())) return;
        visitor.finalizeSpawn(level, level.getCurrentDifficultyAt(origin), MobSpawnType.EVENT, null);
        visitor.addTag(DungeonsMerchant.CAMP_TAG); visitor.addTag(VISITOR_TAG); visitor.setPersistenceRequired();
        visitor.getPersistentData().putUUID(INN_KEY, inn.id()); visitor.getPersistentData().putLong(REVISION_KEY, inn.revision());
        var data = InnSavedData.get(level.getServer());
        if (!data.reserve(inn.id(), visitor.getUUID(), inn.revision(), level.getGameTime())) return;
        if (!level.addFreshEntity(visitor) || !visitor.isAlive() || visitor.isRemoved()
                || !data.advance(inn.id(), visitor.getUUID(), InnRegistry.GuestPhase.RESERVED, InnRegistry.GuestPhase.TRAVELLING, level.getGameTime())) {
            visitor.discard(); data.cancelReservation(inn.id(), visitor.getUUID());
        }
    }

    @Nullable public static InnRegistry.Inn visitOf(DungeonsMerchant visitor) {
        if (!(visitor.level() instanceof ServerLevel level) || !visitor.getTags().contains(VISITOR_TAG)
                || !visitor.getPersistentData().hasUUID(INN_KEY)) return null;
        var inn = InnSavedData.get(level.getServer()).find(visitor.getPersistentData().getUUID(INN_KEY));
        return inn != null && inn.guest() != null && inn.guest().entity().equals(visitor.getUUID())
                && inn.revision() == visitor.getPersistentData().getLong(REVISION_KEY)
                && inn.dimension().equals(level.dimension().location().toString()) ? inn : null;
    }

    static boolean eatAtReservedSeat(DungeonsMerchant guest, InnFurniture.Lease lease) {
        if (!(guest.level() instanceof ServerLevel level)) return false;
        var inn = visitOf(guest);
        var adapter = furniture(level.getServer());
        if (inn == null || inn.guest().phase() != InnRegistry.GuestPhase.ARRIVED || adapter == null
                || lease.kind() != InnFurniture.Kind.SEAT || !lease.innId().equals(inn.id())
                || !lease.guestId().equals(guest.getUUID()) || lease.configRevision() != inn.revision()
                || !adapter.use(level, guest, lease)) return false;
        var inventory = assignedStock(level, inn); if (inventory == null) return false;
        for (int slot = 0; slot < inventory.getContainerSize(); slot++) {
            var meal = inventory.getItem(slot);
            if (meal.isEmpty() || !InnFoodPolicy.accepts(BuiltInRegistries.ITEM.getKey(meal.getItem()).toString())) continue;
            // Both mutations execute synchronously on the authoritative server.
            // RESTING is the saved consumed-meal marker; reload cannot consume again.
            if (!InnSavedData.get(level.getServer()).advance(inn.id(), guest.getUUID(), InnRegistry.GuestPhase.ARRIVED,
                    InnRegistry.GuestPhase.RESTING, level.getGameTime())) return false;
            inventory.removeItem(slot, 1); inventory.setChanged(); return true;
        }
        return false;
    }

    /** EntityLeaveLevel hook calls this only for a known terminal removal, never for chunk unload. */
    public static void removed(Entity entity) {
        if (!(entity instanceof DungeonsMerchant guest) || !(guest.level() instanceof ServerLevel level)
                || entity.getRemovalReason() != Entity.RemovalReason.KILLED && entity.getRemovalReason() != Entity.RemovalReason.DISCARDED) return;
        var inn = visitOf(guest); if (inn == null) return;
        var data = InnSavedData.get(level.getServer());
        if (inn.guest().phase() == InnRegistry.GuestPhase.RESERVED) { data.cancelReservation(inn.id(), guest.getUUID()); return; }
        if (inn.guest().phase() != InnRegistry.GuestPhase.LEAVING)
            data.advance(inn.id(), guest.getUUID(), inn.guest().phase(), InnRegistry.GuestPhase.LEAVING, level.getGameTime());
        data.finishDeparture(inn.id(), guest.getUUID(), level.getGameTime());
    }

    /** Called by the actual anchor's onRemove only on a block-type change, not a property update. */
    public static void anchorRemoved(ServerLevel level, BlockPos position, @Nullable UUID innId, UUID owner) {
        if (innId == null) return;
        var data = InnSavedData.get(level.getServer()); var inn = data.find(innId);
        if (inn == null || !inn.owner().equals(owner) || !inn.dimension().equals(level.dimension().location().toString())
                || !inn.anchor().equals(InnRouteValidator.point(position))) return;
        data.retire(innId, owner, inn.revision(), level.getGameTime());
        var service = SERVICES.get(level.getServer());
        if (service != null) { service.routes.remove(innId); service.nextAttempt.remove(innId); }
        // visitOf/VisitorGoal deliberately do not require the removed anchor:
        // its existing guest must retain a real path out, including after reload.
    }

    @Nullable private static Container stock(ServerLevel level, BlockPos pos) {
        if (!InnRouteValidator.loadedArea(level, pos, 1)) return null;
        var state = level.getBlockState(pos);
        boolean allowed = state.is(Blocks.BARREL) || state.is(Blocks.CHEST) && state.getValue(ChestBlock.TYPE) == ChestType.SINGLE;
        return allowed && level.getBlockEntity(pos) instanceof Container inventory && inventory.getContainerSize() <= 54 ? inventory : null;
    }
    @Nullable private static Container assignedStock(ServerLevel level, InnRegistry.Inn inn) {
        BlockPos position = InnRouteValidator.pos(inn.stock());
        Container inventory = stock(level, position); if (inventory == null) return null;
        var binding = level.getBlockEntity(position).getPersistentData();
        return binding.hasUUID(STOCK_BINDING_KEY) && inn.id().equals(binding.getUUID(STOCK_BINDING_KEY)) ? inventory : null;
    }
    private static int foodCount(Container inventory) {
        int count = 0;
        for (int slot = 0; slot < inventory.getContainerSize() && count < 64; slot++) {
            var stack = inventory.getItem(slot);
            if (!stack.isEmpty() && InnFoodPolicy.accepts(BuiltInRegistries.ITEM.getKey(stack.getItem()).toString())) count = Math.min(64, count + stack.getCount());
        }
        return count;
    }
    private static boolean loadedBounds(ServerLevel level, InnBounds bounds) {
        if (bounds.minY() <= level.getMinBuildHeight() || bounds.maxY() >= level.getMaxBuildHeight()) return false;
        for (int x = bounds.minX() >> 4; x <= bounds.maxX() >> 4; x++)
            for (int z = bounds.minZ() >> 4; z <= bounds.maxZ() >> 4; z++) if (!level.hasChunk(x, z)) return false;
        return level.getWorldBorder().isWithinBounds(new AABB(bounds.minX(), bounds.minY(), bounds.minZ(),
                bounds.maxX() + 1, bounds.maxY() + 1, bounds.maxZ() + 1));
    }
    private static boolean legalPosition(ServerLevel level, BlockPos position) {
        return position.getY() > level.getMinBuildHeight() && position.getY() + 2 < level.getMaxBuildHeight()
                && level.getWorldBorder().isWithinBounds(position);
    }
    private static InnStatus.GuestState guestState(ServerLevel level, InnRegistry.Inn inn) {
        if (inn.guest() == null) return InnStatus.GuestState.NONE;
        if (inn.guest().phase() == InnRegistry.GuestPhase.RESERVED) return InnStatus.GuestState.RESERVED;
        // getEntity(UUID) consults the loaded entity manager; it cannot load a chunk.
        var entity = level.getEntity(inn.guest().entity());
        return entity == null ? InnStatus.GuestState.UNLOADED : InnStatus.GuestState.valueOf(inn.guest().phase().name());
    }
    private static InnStatus empty(InnStatus.State state) { return new InnStatus(null, 0, state, null, 0, 0, 0, false, InnStatus.GuestState.NONE); }
    private static InnStatus report(InnRegistry.Inn inn, InnStatus.State state, @Nullable BlockPos problem,
                                    int seats, int beds, int food, boolean connected, InnStatus.GuestState guest) {
        return new InnStatus(inn.id(), inn.revision(), state, problem, seats, beds, food, connected, guest);
    }
}
