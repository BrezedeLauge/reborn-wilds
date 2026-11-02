package de.laugenbreze.dungeonsalive.entity.peaceful;

import java.util.Set;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.navigation.AmphibiousPathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.AmphibiousNodeEvaluator;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.level.pathfinder.PathFinder;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.level.pathfinder.PathfindingContext;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

/** Amphibious routes stay at the surface and guard the entire vanilla search cache before it is read. */
final class DuckFamilyNavigation extends AmphibiousPathNavigation {
    private final DungeonsCreature duck;
    DuckFamilyNavigation(DungeonsCreature duck, Level level) { super(duck, level); this.duck = duck; }

    @Override protected PathFinder createPathFinder(int visitedNodes) {
        nodeEvaluator = new AmphibiousNodeEvaluator(true) {
            @Override public Node getStart() {
                // Floating feet sit close to the water's top. Vanilla's +.5
                // amphibian offset can otherwise start in the air above it.
                return mob.isInWater() ? getStartNode(mob.blockPosition()) : super.getStart();
            }
            @Override public PathType getPathType(PathfindingContext context, int x, int y, int z) {
                PathType type = super.getPathType(context, x, y, z);
                if ((type == PathType.WATER || type == PathType.WATER_BORDER)
                        && context.getPathTypeFromState(x, y + 1, z) == PathType.WATER) return PathType.BLOCKED;
                return type;
            }
        };
        nodeEvaluator.setCanPassDoors(false);
        return new PathFinder(nodeEvaluator, visitedNodes);
    }

    @Override @Nullable public Path createPath(BlockPos target, int accuracy) {
        return allowed(target) ? super.createPath(target, accuracy) : null;
    }

    private boolean allowed(BlockPos target) {
        BlockPos anchor = duck.wildlifeAnchor() == null ? duck.blockPosition() : duck.wildlifeAnchor();
        return target.distSqr(anchor) <= 24 * 24
                && WildlifeNavigationSafety.loadedArea(level, duck.blockPosition(), WildlifeNavigationSafety.LOADED_RADIUS)
                && DuckHabitat.safe(duck, target);
    }

    @Override @Nullable protected Path createPath(Set<BlockPos> targets, int regionOffset,
                                                  boolean offsetUpward, int accuracy, float followRange) {
        if (targets.isEmpty() || targets.size() > 8 || regionOffset < 0 || regionOffset > 16
                || !Float.isFinite(followRange) || followRange <= 0 || targets.stream().anyMatch(pos -> !allowed(pos))) return null;
        Path path = super.createPath(targets, regionOffset, offsetUpward, accuracy, Math.min(24, followRange));
        if (path == null || !path.canReach() || path.getNodeCount() > 128) return null;
        return remainingSafe(path) ? path : null;
    }

    private boolean remainingSafe(Path path) {
        for (int i = path.getNextNodeIndex(); i < path.getNodeCount(); i++)
            if (!DuckHabitat.safe(duck, path.getNodePos(i))) return false;
        return true;
    }

    @Override protected boolean canMoveDirectly(Vec3 from, Vec3 to) { return false; }
    @Override public boolean isStableDestination(BlockPos pos) { return DuckHabitat.safe(duck, pos); }

    @Override public void tick() {
        Path path = getPath();
        if (!WildlifeNavigationSafety.loadedArea(level, duck.blockPosition(), WildlifeNavigationSafety.LOADED_RADIUS)
                || path != null && !path.isDone() && !remainingSafe(path)) { stop(); return; }
        super.tick();
    }
}
