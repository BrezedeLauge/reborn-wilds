package de.laugenbreze.dungeonsalive.entity.peaceful;

import java.util.EnumSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

/** Small, local walks and damage escapes. No block edits, teleportation or population changes. */
final class MerchantRoutineGoal extends Goal {
    private static final int DESTINATION_ATTEMPTS = 12;
    private final DungeonsMerchant merchant;

    MerchantRoutineGoal(DungeonsMerchant merchant) {
        this.merchant = merchant;
        setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override public boolean canUse() {
        return merchant.hasCampRoutine() && merchant.campHome() != null && !merchant.isTrading();
    }
    @Override public boolean canContinueToUse() { return canUse(); }
    @Override public boolean requiresUpdateEveryTick() { return true; }
    @Override public void stop() { merchant.getNavigation().stop(); }

    @Override public void tick() {
        var decision = merchant.routine().decide(merchant.routineObservation());
        if (decision.stopNavigation()) merchant.getNavigation().stop();
        if (!decision.planRoute()) return;
        // Knockback briefly makes vanilla GroundPathNavigation unable to begin any search.
        // Waiting for landing is not a failed search and must not consume most of the escape window.
        if (!merchant.onGround() && !merchant.isInLiquid() && !merchant.isPassenger()) return;
        long now = merchant.level().getGameTime();
        BlockPos target = chooseDestination(decision.activity());
        boolean success = false;
        if (target != null) {
            Path path = merchant.getNavigation().createPath(target, 0);
            if (path != null && path.canReach() && pathStaysLocal(path, decision.activity())) {
                success = merchant.getNavigation().moveTo(path, decision.activity() == MerchantRoutine.Activity.FLEE ? 1.15 : .65);
            }
        }
        merchant.routine().routeAttempted(now, success);
        if (!success) merchant.getNavigation().stop();
    }

    @Nullable private BlockPos chooseDestination(MerchantRoutine.Activity activity) {
        BlockPos home = merchant.campHome();
        if (home == null || !MerchantNavigation.loadedArea(merchant.level(), merchant.blockPosition(), MerchantNavigation.LOADED_RADIUS)) return null;
        if (activity == MerchantRoutine.Activity.RETURN_HOME) {
            // The saved standing point, or its immediately adjacent reachable ground.
            if (safeDestination(merchant, home)) return home;
            for (Direction direction : Direction.Plane.HORIZONTAL) {
                BlockPos adjacent = home.relative(direction);
                if (safeDestination(merchant, adjacent)) return adjacent;
            }
            return null;
        }

        boolean fleeing = activity == MerchantRoutine.Activity.FLEE;
        int radius = fleeing ? MerchantRoutine.FLEE_RADIUS : MerchantRoutine.DAY_RADIUS;
        Vec3 threat = merchant.campThreat();
        double bestScore = fleeing ? merchant.position().distanceToSqr(threat) + 2 : -1;
        BlockPos best = null;
        for (int attempt = 0; attempt < DESTINATION_ATTEMPTS; attempt++) {
            int x = home.getX() + merchant.getRandom().nextInt(radius * 2 + 1) - radius;
            int z = home.getZ() + merchant.getRandom().nextInt(radius * 2 + 1) - radius;
            // Bounded vertical probes handle a little uneven ground without an unbounded height scan.
            for (int dy : new int[]{0, 1, -1, 2, -2}) {
                BlockPos candidate = new BlockPos(x, home.getY() + dy, z);
                if (candidate.distSqr(home) > radius * radius || merchant.distanceToSqr(Vec3.atBottomCenterOf(candidate)) < 4
                        || !safeDestination(merchant, candidate)) continue;
                double score = fleeing ? Vec3.atBottomCenterOf(candidate).distanceToSqr(threat) : 0;
                if (score > bestScore) { bestScore = score; best = candidate; }
                if (!fleeing) return best;
                break;
            }
        }
        return best;
    }

    private boolean pathStaysLocal(Path path, MerchantRoutine.Activity activity) {
        BlockPos home = merchant.campHome();
        if (home == null) return false;
        double limit = activity == MerchantRoutine.Activity.WANDER ? MerchantRoutine.DAY_RADIUS
                : activity == MerchantRoutine.Activity.FLEE ? MerchantRoutine.FLEE_RADIUS
                : Math.max(MerchantRoutine.FLEE_RADIUS, Math.sqrt(merchant.blockPosition().distSqr(home)) + 1);
        // Vanilla can return the still-active path when the requested home is unchanged.
        // Its traversed prefix remains in the node list, but the return radius shrinks as
        // the merchant approaches home. Only the remaining route may veto that progress.
        for (int i = path.getNextNodeIndex(); i < path.getNodeCount(); i++) {
            var node = path.getNode(i);
            BlockPos position = new BlockPos(node.x, node.y, node.z);
            if (position.distSqr(home) > limit * limit || !safeDestination(merchant, position)) return false;
        }
        return true;
    }

    /** Used both before pathfinding and for every accepted path node. Reads only loaded terrain. */
    static boolean safeDestination(DungeonsMerchant merchant, BlockPos position) {
        var level = merchant.level();
        if (position.getY() <= level.getMinBuildHeight() || position.getY() + 2 >= level.getMaxBuildHeight()
                || !level.getWorldBorder().isWithinBounds(position)
                || !MerchantNavigation.loadedArea(level, position, 2)) return false;
        var below = level.getBlockState(position.below());
        if (!below.isFaceSturdy(level, position.below(), Direction.UP) || dangerous(merchant, position.below())) return false;
        if (dangerous(merchant, position) || dangerous(merchant, position.above())) return false;
        AABB standingBox = merchant.getBoundingBox().move(Vec3.atBottomCenterOf(position).subtract(merchant.position()));
        return level.noCollision(merchant, standingBox);
    }

    private static boolean dangerous(DungeonsMerchant merchant, BlockPos position) {
        var state = merchant.level().getBlockState(position);
        return !state.getFluidState().isEmpty() || state.is(Blocks.FIRE) || state.is(Blocks.SOUL_FIRE)
                || state.is(Blocks.MAGMA_BLOCK) || state.is(Blocks.CACTUS) || state.is(Blocks.SWEET_BERRY_BUSH)
                || state.is(Blocks.WITHER_ROSE) || state.is(Blocks.CAMPFIRE) || state.is(Blocks.SOUL_CAMPFIRE)
                || state.is(Blocks.POWDER_SNOW);
    }
}
