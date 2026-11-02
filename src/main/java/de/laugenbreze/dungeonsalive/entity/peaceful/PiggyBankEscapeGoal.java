package de.laugenbreze.dungeonsalive.entity.peaceful;

import java.util.EnumSet;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

/** A treasure animal flees locally after damage; no teleport, block changes, allies or retaliation. */
final class PiggyBankEscapeGoal extends Goal {
    static final int FLEE_TICKS = 120;
    static final int FLEE_RADIUS = 12;
    private final DungeonsCreature pig;
    private final WildlifeGroupPolicy paths = new WildlifeGroupPolicy();

    PiggyBankEscapeGoal(DungeonsCreature pig) { this.pig = pig; setFlags(EnumSet.of(Flag.MOVE)); }
    @Override public boolean canUse() {
        return pig.piggyBank() && pig.ownerId() == null && pig.isAlive() && !pig.isNoAi() && !pig.waiting() && pig.piggyFleeing();
    }
    @Override public boolean canContinueToUse() { return canUse(); }
    @Override public boolean requiresUpdateEveryTick() { return true; }
    @Override public void start() { pig.getNavigation().stop(); }
    @Override public void stop() { pig.getNavigation().stop(); }
    @Override public void tick() {
        long now = pig.level().getGameTime();
        if (!paths.canReplan(now)) return;
        BlockPos target = escapeDestination();
        boolean moved = false;
        if (target != null) {
            var path = pig.getNavigation().createPath(target, 0);
            if (path != null && path.canReach()) {
                boolean safe = true;
                for (int i = 0; i < path.getNodeCount() && safe; i++) {
                    var node = path.getNode(i); BlockPos pos = new BlockPos(node.x, node.y, node.z);
                    safe = pos.distSqr(pig.piggyEscapeOrigin()) <= FLEE_RADIUS * FLEE_RADIUS
                            && WildlifeNavigationSafety.safeGround(pig, pos);
                }
                if (safe) moved = pig.getNavigation().moveTo(path, 1.3);
            }
        }
        paths.routeAttempted(now, moved);
        if (!moved) pig.getNavigation().stop();
    }

    @Nullable private BlockPos escapeDestination() {
        BlockPos origin = pig.piggyEscapeOrigin();
        if (origin == null || !WildlifeNavigationSafety.loadedArea(pig.level(), pig.blockPosition(), WildlifeNavigationSafety.LOADED_RADIUS)) return null;
        Vec3 away = pig.position().subtract(pig.piggyThreat());
        double angle = away.horizontalDistanceSqr() < .001 ? Math.toRadians(pig.getYRot()) : Math.atan2(away.z, away.x);
        BlockPos best = null; double bestScore = pig.position().distanceToSqr(pig.piggyThreat()) + 1;
        for (int distance : new int[]{10, 8, 6, 4}) for (double turn : new double[]{0, -.6, .6, -1.2, 1.2}) {
            BlockPos base = BlockPos.containing(Vec3.atBottomCenterOf(origin).add(Math.cos(angle + turn) * distance, 0, Math.sin(angle + turn) * distance));
            for (int dy : new int[]{0, 1, -1, 2, -2}) {
                BlockPos candidate = base.offset(0, dy, 0);
                if (candidate.distSqr(origin) > FLEE_RADIUS * FLEE_RADIUS || !WildlifeNavigationSafety.safeGround(pig, candidate)) continue;
                double score = Vec3.atBottomCenterOf(candidate).distanceToSqr(pig.piggyThreat());
                if (score > bestScore) { best = candidate; bestScore = score; }
                break;
            }
        }
        return best;
    }
}
