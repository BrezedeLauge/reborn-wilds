package de.laugenbreze.dungeonsalive.entity.peaceful;

import java.util.EnumSet;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

/** Ground groups share a local itinerary, quiet intervals and finite escapes; they never retaliate. */
final class WildlifeGroundGroupGoal extends Goal {
    private static final int ESCAPE_RADIUS = 12;
    private final DungeonsCreature creature;
    private final WildlifeGroupPolicy pathBudget = new WildlifeGroupPolicy();
    private WildlifeGroupPolicy.Activity previous = WildlifeGroupPolicy.Activity.REST;
    @Nullable private Vec3 escapeOrigin;

    WildlifeGroundGroupGoal(DungeonsCreature creature) {
        this.creature = creature;
        setFlags(EnumSet.of(Flag.MOVE));
    }
    @Override public boolean canUse() {
        return creature.groupWildlife() && !creature.flyingWildlife() && !creature.aquaticWildlife() && !creature.waiting()
                && creature.ownerId() == null && creature.wildlifeGroupId() != null && creature.wildlifeAnchor() != null;
    }
    @Override public boolean canContinueToUse() { return canUse(); }
    @Override public boolean requiresUpdateEveryTick() { return true; }
    @Override public void stop() { creature.getNavigation().stop(); escapeOrigin = null; }

    @Override public void tick() {
        var activity = creature.wildlifeActivity();
        DungeonsCreature leader = creature.wildlifeLeader();
        if (activity != previous) {
            creature.getNavigation().stop();
            escapeOrigin = activity == WildlifeGroupPolicy.Activity.FLEE ? leader.position() : null;
        }
        if (activity == WildlifeGroupPolicy.Activity.FLEE && escapeOrigin == null) escapeOrigin = leader.position();
        previous = activity;
        boolean regroup = leader != creature && creature.distanceToSqr(leader) > 16;
        if (activity == WildlifeGroupPolicy.Activity.REST && !regroup) { creature.getNavigation().stop(); return; }
        if (activity != WildlifeGroupPolicy.Activity.FLEE && leader == creature && creature.wildlifeMembers().stream()
                .anyMatch(member -> member != creature && creature.distanceToSqr(member) > 12 * 12)) {
            // Keep a visible loaded straggler in the local membership radius instead of
            // continually walking away until it can no longer find its leader.
            creature.getNavigation().stop(); return;
        }
        long now = creature.level().getGameTime();
        if (!pathBudget.canReplan(now)) return;
        Vec3 desired = desiredPosition(activity, leader, now);
        if (creature.position().distanceToSqr(desired) <= 2.25) { creature.getNavigation().stop(); return; }
        Vec3 delta = desired.subtract(creature.position());
        if (delta.horizontalDistanceSqr() > WildlifeJourney.MAX_STEP * WildlifeJourney.MAX_STEP) {
            double scale = WildlifeJourney.MAX_STEP / Math.sqrt(delta.horizontalDistanceSqr());
            desired = creature.position().add(delta.x * scale, 0, delta.z * scale);
        }
        BlockPos target = groundNear(desired);
        boolean success = false;
        if (target != null) {
            var path = creature.getNavigation().createPath(target, 0);
            if (path != null && path.canReach()) {
                boolean safe = true;
                BlockPos anchor = creature.wildlifeAnchor();
                double limit = Math.max(WildlifeJourney.MAX_LOCAL_DISTANCE, Math.sqrt(creature.blockPosition().distSqr(anchor)) + 1);
                for (int i = 0; i < path.getNodeCount() && safe; i++) {
                    var node = path.getNode(i);
                    var position = new BlockPos(node.x, node.y, node.z);
                    safe = position.distSqr(anchor) <= limit * limit && WildlifeNavigationSafety.safeGround(creature, position);
                }
                if (safe) success = creature.getNavigation().moveTo(path, activity == WildlifeGroupPolicy.Activity.FLEE ? 1.25 : .75);
            }
        }
        pathBudget.routeAttempted(now, success);
        if (!success) creature.getNavigation().stop();
    }

    private Vec3 desiredPosition(WildlifeGroupPolicy.Activity activity, DungeonsCreature leader, long now) {
        Vec3 anchor = Vec3.atBottomCenterOf(creature.wildlifeAnchor());
        double seed = Math.floorMod(creature.wildlifeGroupId().getMostSignificantBits(), 6_283L) / 1000.0;
        double slotAngle = seed + creature.wildlifeSlot() * Math.PI * 2 / WildlifeGroupPolicy.MAX_MEMBERS;
        if (activity == WildlifeGroupPolicy.Activity.FLEE) {
            Vec3 away = leader.position().subtract(creature.wildlifeThreat());
            double length = Math.sqrt(away.x * away.x + away.z * away.z);
            double x = length < .01 ? Math.cos(seed) : away.x / length;
            double z = length < .01 ? Math.sin(seed) : away.z / length;
            return (escapeOrigin == null ? leader.position() : escapeOrigin).add(x * (ESCAPE_RADIUS - 2) + Math.cos(slotAngle) * 1.5, 0,
                    z * (ESCAPE_RADIUS - 2) + Math.sin(slotAngle) * 1.5);
        }
        if (leader != creature) return leader.position().add(Math.cos(slotAngle) * 2, 0, Math.sin(slotAngle) * 2);
        var stop = WildlifeJourney.sample(now, creature.wildlifeGroupId());
        return anchor.add(stop.x(), 0, stop.z());
    }

    @Nullable private BlockPos groundNear(Vec3 desired) {
        if (!WildlifeNavigationSafety.loadedArea(creature.level(), creature.blockPosition(), WildlifeNavigationSafety.LOADED_RADIUS)) return null;
        BlockPos base = BlockPos.containing(desired.x, creature.getY(), desired.z);
        for (int offset = 0; offset < 5; offset++) {
            int dx = offset == 1 ? 1 : offset == 2 ? -1 : 0;
            int dz = offset == 3 ? 1 : offset == 4 ? -1 : 0;
            BlockPos column = base.offset(dx, 0, dz);
            if (!WildlifeNavigationSafety.loadedArea(creature.level(), column, 2)) continue;
            // A short local step can follow slopes. Never snap a cave animal onto the surface.
            int surface = creature.level().getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, column.getX(), column.getZ());
            int y = Math.abs(surface - creature.getY()) <= 4 ? surface : base.getY();
            for (int dy : new int[]{0, 1, -1, 2, -2, 3, -3}) {
                BlockPos candidate = new BlockPos(column.getX(), y + dy, column.getZ());
                if (candidate.distSqr(creature.wildlifeAnchor()) <= WildlifeJourney.MAX_LOCAL_DISTANCE * WildlifeJourney.MAX_LOCAL_DISTANCE
                        && WildlifeNavigationSafety.safeGround(creature, candidate)) return candidate;
            }
        }
        return null;
    }
}
