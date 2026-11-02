package de.laugenbreze.dungeonsalive.entity.peaceful;

import java.util.Set;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

/** Loaded-area flight paths; neither target inspection nor replanning may load terrain. */
final class WildlifeFlightNavigation extends FlyingPathNavigation {
    static final int MAX_FOLLOW_RANGE = 24;
    static final int LOADED_RADIUS = MAX_FOLLOW_RANGE + 16 + 2;
    private static final int MAX_PATH_NODES = 128;
    private final DungeonsCreature creature;

    WildlifeFlightNavigation(DungeonsCreature creature, Level level) {
        super(creature, level);
        this.creature = creature;
        setCanFloat(false);
    }

    static boolean loadedArea(Level level, BlockPos center, int radius) {
        for (int x = (center.getX() - radius) >> 4; x <= (center.getX() + radius) >> 4; x++) {
            for (int z = (center.getZ() - radius) >> 4; z <= (center.getZ() + radius) >> 4; z++) {
                if (!level.hasChunk(x, z)) return false;
            }
        }
        return true;
    }

    static boolean safeBody(DungeonsCreature creature, Vec3 feet) {
        Level level = creature.level();
        if (!Double.isFinite(feet.x) || !Double.isFinite(feet.y) || !Double.isFinite(feet.z)) return false;
        AABB box = creature.getBoundingBox().move(feet.subtract(creature.position()));
        BlockPos min = BlockPos.containing(box.minX, box.minY, box.minZ);
        BlockPos max = BlockPos.containing(box.maxX, box.maxY, box.maxZ);
        if (box.minY < level.getMinBuildHeight() || box.maxY >= level.getMaxBuildHeight()
                || !level.getWorldBorder().isWithinBounds(min) || !level.getWorldBorder().isWithinBounds(max)
                || !level.hasChunksAt(min.offset(-1, 0, -1), max.offset(1, 0, 1))) return false;
        // Member spacing is handled by the goal. Terrain checks must not turn a
        // temporarily close flockmate into an impassable permanent world obstacle.
        return level.noBlockCollision(creature, box) && !level.containsAnyLiquid(box);
    }

    private boolean safeSegment(Vec3 from, Vec3 to) {
        double distance = from.distanceTo(to);
        if (distance > MAX_FOLLOW_RANGE * 2) return false;
        int steps = Math.max(1, (int) Math.ceil(distance * 2));
        for (int step = 1; step <= steps; step++) {
            if (!safeBody(creature, from.lerp(to, (double) step / steps))) return false;
        }
        return true;
    }

    @Override @Nullable public Path createPath(BlockPos target, int accuracy) {
        if (!loadedArea(level, creature.blockPosition(), LOADED_RADIUS)
                || !safeBody(creature, Vec3.atBottomCenterOf(target))) return null;
        return super.createPath(target, accuracy);
    }

    @Override @Nullable protected Path createPath(Set<BlockPos> targets, int regionOffset,
                                                  boolean offsetUpward, int accuracy, float followRange) {
        if (targets.isEmpty() || targets.size() > 8 || regionOffset < 0 || regionOffset > 16
                || !Float.isFinite(followRange) || followRange <= 0
                || !loadedArea(level, creature.blockPosition(), LOADED_RADIUS)
                || targets.stream().anyMatch(target -> !safeBody(creature, Vec3.atBottomCenterOf(target)))) return null;
        Path path = super.createPath(targets, regionOffset, offsetUpward, accuracy, Math.min(followRange, MAX_FOLLOW_RANGE));
        if (path == null || !path.canReach() || path.getNodeCount() > MAX_PATH_NODES) return null;
        Vec3 previous = creature.position();
        // Vanilla may return the existing path when the target is unchanged.
        // The traversed prefix is no longer a route from the bird's current body.
        for (int i = path.getNextNodeIndex(); i < path.getNodeCount(); i++) {
            Vec3 point = path.getEntityPosAtNode(creature, i);
            if (!safeSegment(previous, point)) return null;
            previous = point;
        }
        return path;
    }

    @Override protected void followThePath() {
        Path current = getPath();
        if (current == null || current.isDone()) return;
        Vec3 position = creature.position();
        Vec3 waypoint = current.getNextEntityPos(creature);
        this.maxDistanceToWaypoint = creature.getBbWidth() > .75F ? creature.getBbWidth() / 2
                : .75F - creature.getBbWidth() / 2;
        boolean reached = Math.abs(position.x - waypoint.x) <= maxDistanceToWaypoint
                && Math.abs(position.z - waypoint.z) <= maxDistanceToWaypoint
                && Math.abs(position.y - waypoint.y) < 1;
        int following = current.getNextNodeIndex() + 1;
        // Vanilla can skip a nearby corner before the next tick's safety check.
        // In particular, descending past a shelf must first clear its real edge:
        // a ray to the old waypoint is not a body-sweep to the new waypoint.
        if (reached) {
            if (following >= current.getNodeCount()) {
                current.advance();
            } else {
                Vec3 nextWaypoint = current.getEntityPosAtNode(creature, following);
                // A newly obstructed node cannot become reachable by finishing
                // this corner. Discard it now instead of waiting for the stuck
                // timeout at the still-clear previous waypoint.
                if (!safeBody(creature, nextWaypoint)) {
                    stop();
                    return;
                }
                if (safeSegment(position, nextWaypoint)) current.advance();
            }
        }
        // Keep Vanilla's bounded stuck/timeout lifecycle without its early corner cut.
        doStuckDetection(position);
    }

    @Override public void tick() {
        if (!loadedArea(level, creature.blockPosition(), LOADED_RADIUS)) {
            stop();
            return;
        }
        Path current = getPath();
        if (current != null && !current.isDone()
                && !safeSegment(creature.position(), current.getNextEntityPos(creature))) {
            stop();
            return;
        }
        super.tick();
    }
}
