package de.laugenbreze.dungeonsalive.entity.peaceful;

import java.util.Set;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

/** Natural schools never ask pathfinding to obtain a missing chunk or leave their water volume. */
final class WildlifeWaterNavigation extends WaterBoundPathNavigation {
    private final DungeonsCreature creature;
    WildlifeWaterNavigation(DungeonsCreature creature, Level level) { super(creature, level); this.creature = creature; }

    @Override @Nullable public Path createPath(BlockPos target, int accuracy) {
        if (!WildlifeNavigationSafety.loadedArea(level, creature.blockPosition(), WildlifeNavigationSafety.LOADED_RADIUS)
                || !WildlifeSchoolGoal.safeWater(creature, Vec3.atCenterOf(target))) return null;
        return super.createPath(target, accuracy);
    }

    @Override @Nullable protected Path createPath(Set<BlockPos> targets, int regionOffset,
                                                  boolean offsetUpward, int accuracy, float followRange) {
        if (targets.isEmpty() || targets.size() > 8 || regionOffset < 0 || regionOffset > 16
                || !Float.isFinite(followRange) || followRange <= 0
                || !WildlifeNavigationSafety.loadedArea(level, creature.blockPosition(), WildlifeNavigationSafety.LOADED_RADIUS)
                || targets.stream().anyMatch(target -> !WildlifeSchoolGoal.safeWater(creature, Vec3.atCenterOf(target)))) return null;
        Path path = super.createPath(targets, regionOffset, offsetUpward, accuracy, Math.min(24, followRange));
        if (path == null || !path.canReach() || path.getNodeCount() > 128) return null;
        Vec3 previous = creature.position();
        for (int index = 0; index < path.getNodeCount(); index++) {
            Vec3 next = path.getEntityPosAtNode(creature, index);
            if (!safeSegment(previous, next)) return null;
            previous = next;
        }
        return path;
    }

    private boolean safeSegment(Vec3 from, Vec3 to) {
        if (from.distanceToSqr(to) > 48 * 48) return false;
        int steps = Math.max(1, (int)Math.ceil(from.distanceTo(to) * 2));
        for (int step = 1; step <= steps; step++)
            if (!WildlifeSchoolGoal.safeWater(creature, from.lerp(to, (double)step / steps))) return false;
        return true;
    }

    @Override protected boolean canMoveDirectly(Vec3 from, Vec3 to) { return safeSegment(from, to); }

    @Override public boolean isStableDestination(BlockPos pos) {
        return WildlifeNavigationSafety.loadedArea(level, pos, 2)
                && WildlifeSchoolGoal.safeWater(creature, Vec3.atCenterOf(pos));
    }

    @Override public void tick() {
        if (!WildlifeNavigationSafety.loadedArea(level, creature.blockPosition(), WildlifeNavigationSafety.LOADED_RADIUS)) { stop(); return; }
        Path path = getPath();
        if (path != null && !path.isDone() && !safeSegment(creature.position(), path.getNextEntityPos(creature))) { stop(); return; }
        super.tick();
        path = getPath();
        if (path != null && !path.isDone() && !safeSegment(creature.position(), path.getNextEntityPos(creature))) stop();
    }
}
