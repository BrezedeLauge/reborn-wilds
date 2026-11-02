package de.laugenbreze.dungeonsalive.settlement.inn;

import de.laugenbreze.dungeonsalive.entity.peaceful.DungeonsMerchant;
import de.laugenbreze.dungeonsalive.entity.peaceful.ModPeacefulEntities;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import org.jetbrains.annotations.Nullable;

/** Actual vanilla walking routes, including built bridges; every terrain read is preceded by a loaded-area guard. */
public final class InnRouteValidator {
    public static final int LOADED_RADIUS = 42, MAX_NODES = 128;
    public record Check(boolean loaded, boolean connected, @Nullable BlockPos problem,
                        @Nullable Path path, List<BlockPos> nodes) {
        public Check { nodes = List.copyOf(nodes); if (problem != null) problem = problem.immutable(); }
    }
    private InnRouteValidator() { }

    /** Detached geometry probe. It is never added to the world and cannot perform AI, trades or damage. */
    @Nullable static DungeonsMerchant probe(ServerLevel level, BlockPos start) {
        return probe(level, start, null);
    }
    @Nullable static DungeonsMerchant probe(ServerLevel level, BlockPos start, @Nullable InnRegistry.Inn inn) {
        if (!loadedArea(level, start, LOADED_RADIUS)) return null;
        var probe = ModPeacefulEntities.merchantType("village_merchant").create(level);
        if (probe == null) return null;
        probe.addTag(DungeonsMerchant.CAMP_TAG);
        if (probe.getNavigation() instanceof GroundPathNavigation navigation) navigation.setCanOpenDoors(true);
        probe.setPos(feet(level, start));
        // Only the detached pathfinding probe is seeded with its verified standing
        // contact. A real guest must reach onGround through its actual movement.
        probe.setOnGround(safeStanding(probe, start, inn));
        return probe;
    }

    public static Check inspect(ServerLevel level, InnRegistry.Inn inn, List<BlockPos> previousRoute) {
        BlockPos start = pos(inn.arrival()), target = pos(inn.entrance());
        if (!loadedArea(level, start, LOADED_RADIUS) || !loadedArea(level, target, 2)) return unavailable();
        var probe = probe(level, start, inn);
        if (probe == null) return unavailable();
        BlockPos knownObstacle = null;
        // A formerly verified node supplies an honest broken-bridge location.
        // For a route that has never existed there is no invented first blocker.
        for (BlockPos node : previousRoute) {
            if (!loadedArea(level, node, 2)) return unavailable();
            if (!safeStanding(probe, node, inn)) { knownObstacle = node; break; }
        }
        Check current = plan(probe, target, inn);
        if (!current.connected && current.problem == null && knownObstacle != null)
            return new Check(current.loaded, false, knownObstacle, null, List.of());
        return current;
    }

    public static Check plan(DungeonsMerchant merchant, BlockPos target) {
        return plan(merchant, target, InnService.visitOf(merchant));
    }
    static Check plan(DungeonsMerchant merchant, BlockPos target, @Nullable InnRegistry.Inn inn) {
        Level level = merchant.level();
        if (!loadedArea(level, merchant.blockPosition(), LOADED_RADIUS) || !loadedArea(level, target, 2)) return unavailable();
        if (!safeStanding(merchant, target, inn)) return blocked(target);
        if (!merchant.onGround() && !merchant.isInLiquid() && !merchant.isPassenger()) return blocked(null);
        if (merchant.blockPosition().distSqr(target) > 24 * 24) return blocked(null);
        Path path = merchant.getNavigation().createPath(target, 0);
        if (path == null || !path.canReach() || path.getNodeCount() > MAX_NODES) return blocked(null);
        List<BlockPos> nodes = new ArrayList<>();
        // Vanilla can reuse an active path: traversed nodes do not veto a
        // guest already beyond an obstacle, and do not send it back to the start.
        for (int i = path.getNextNodeIndex(); i < path.getNodeCount(); i++) {
            var node = path.getNode(i); BlockPos point = new BlockPos(node.x, node.y, node.z);
            if (!loadedArea(level, point, 2)) return unavailable();
            if (!safeStanding(merchant, point, inn)) return blocked(point);
            nodes.add(point);
        }
        return new Check(true, true, null, path, nodes);
    }

    static boolean remainingRouteSafe(DungeonsMerchant merchant) {
        var path = merchant.getNavigation().getPath();
        if (path == null || !loadedArea(merchant.level(), merchant.blockPosition(), LOADED_RADIUS)) return false;
        for (int i = path.getNextNodeIndex(); i < path.getNodeCount(); i++) {
            var node = path.getNode(i);
            if (!safeStanding(merchant, new BlockPos(node.x, node.y, node.z))) return false;
        }
        return true;
    }
    public static boolean safeStanding(DungeonsMerchant merchant, BlockPos point) {
        return safeStanding(merchant, point, InnService.visitOf(merchant));
    }
    private static boolean safeStanding(DungeonsMerchant merchant, BlockPos point, @Nullable InnRegistry.Inn inn) {
        Level level = merchant.level();
        if (!loadedArea(level, point, 2) || point.getY() <= level.getMinBuildHeight()
                || point.getY() + merchant.getBbHeight() >= level.getMaxBuildHeight()
                || !level.getWorldBorder().isWithinBounds(point)) return false;
        for (int y = -1; y <= 2; y++) {
            var block = level.getBlockState(point.above(y));
            if (!block.getFluidState().isEmpty() || block.is(Blocks.FIRE) || block.is(Blocks.SOUL_FIRE)
                    || block.is(Blocks.MAGMA_BLOCK) || block.is(Blocks.CACTUS) || block.is(Blocks.CAMPFIRE)
                    || block.is(Blocks.SOUL_CAMPFIRE) || block.is(Blocks.POWDER_SNOW)
                    || block.is(Blocks.SWEET_BERRY_BUSH) || block.is(Blocks.WITHER_ROSE)) return false;
        }
        var body = merchant.getBoundingBox().move(feet(level, point).subtract(merchant.position()));
        var footContact = new AABB(body.minX, body.minY - .025, body.minZ, body.maxX, body.minY + .0001, body.maxZ);
        if (!level.getWorldBorder().isWithinBounds(body)
                || !level.getBlockCollisions(merchant, footContact).iterator().hasNext()) return false;
        if (!InnDoorGoal.allowed(level, inn, point)) return !level.getBlockCollisions(merchant, body).iterator().hasNext();
        // Only the authorized wooden door is evaluated in its open state. The
        // world is unchanged; walls, furniture and all foreign doors still collide.
        var bodyShape = Shapes.create(body);
        for (BlockPos block : BlockPos.betweenClosed(BlockPos.containing(body.minX - 1, body.minY - 1, body.minZ - 1),
                BlockPos.containing(body.maxX + 1, body.maxY + 1, body.maxZ + 1))) {
            var state = level.getBlockState(block);
            if (InnDoorGoal.allowed(level, inn, block)) state = state.setValue(DoorBlock.OPEN, true);
            var shape = state.getCollisionShape(level, block, CollisionContext.of(merchant)).move(block.getX(), block.getY(), block.getZ());
            if (Shapes.joinIsNotEmpty(shape, bodyShape, BooleanOp.AND)) return false;
        }
        return true;
    }
    static Vec3 feet(Level level, BlockPos point) {
        return new Vec3(point.getX() + .5, WalkNodeEvaluator.getFloorLevel(level, point), point.getZ() + .5);
    }
    public static boolean loadedArea(Level level, BlockPos center, int radius) {
        for (int x = (center.getX() - radius) >> 4; x <= (center.getX() + radius) >> 4; x++)
            for (int z = (center.getZ() - radius) >> 4; z <= (center.getZ() + radius) >> 4; z++)
                if (!level.hasChunk(x, z)) return false;
        return true;
    }
    static BlockPos pos(InnBounds.Point point) { return new BlockPos(point.x(), point.y(), point.z()); }
    static InnBounds.Point point(BlockPos position) { return new InnBounds.Point(position.getX(), position.getY(), position.getZ()); }
    private static Check unavailable() { return new Check(false, false, null, null, List.of()); }
    private static Check blocked(@Nullable BlockPos problem) { return new Check(true, false, problem, null, List.of()); }
}
