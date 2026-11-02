package de.laugenbreze.dungeonsalive.entity.peaceful;

import java.util.EnumSet;
import java.util.List;
import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

/** Loaded flocks share changing local destinations, formation spacing and quiet places. */
final class WildlifeFlockGoal extends Goal {
    private static final double REJOIN_DISTANCE_SQR = 12 * 12;
    private final DungeonsCreature creature;
    private final WildlifeGroupPolicy routeBudget = new WildlifeGroupPolicy();
    private final BirdPerchController perching;
    @Nullable private WildlifeGroupPolicy.Activity previousActivity;
    @Nullable private Vec3 pauseOrigin;

    WildlifeFlockGoal(DungeonsCreature creature) {
        this.creature = creature;
        this.perching = new BirdPerchController(creature);
        setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override public boolean canUse() {
        return creature.flyingWildlife() && creature.ownerId() == null && !creature.waiting()
                && creature.wildlifeGroupId() != null && creature.wildlifeAnchor() != null;
    }

    @Override public boolean canContinueToUse() { return canUse(); }
    @Override public boolean requiresUpdateEveryTick() { return true; }

    @Override public void stop() {
        creature.getNavigation().stop();
        if (creature.birdWildlife() || creature.birdPerched()) perching.stop();
        previousActivity = null;
        pauseOrigin = null;
    }

    @Override public void tick() {
        long now = creature.level().getGameTime();
        if (creature.birdWildlife() && perching.tick()) return;
        var activity = creature.wildlifeActivity();
        // Bird rest has a real support/landing lifecycle. The original hovering
        // rhythm remains unchanged for wisps and other non-bird flying wildlife.
        if (creature.birdWildlife() && activity != WildlifeGroupPolicy.Activity.FLEE)
            activity = WildlifeGroupPolicy.Activity.WANDER;
        DungeonsCreature leader = creature.wildlifeLeader();
        if (activity != previousActivity) {
            creature.getNavigation().stop();
            pauseOrigin = activity == WildlifeGroupPolicy.Activity.WANDER ? null : leader.position();
        }
        previousActivity = activity;
        if (activity == WildlifeGroupPolicy.Activity.WANDER && leader == creature && creature.wildlifeMembers().stream()
                .anyMatch(member -> member != creature && creature.distanceToSqr(member) > REJOIN_DISTANCE_SQR)) {
            creature.getNavigation().stop();
            return;
        }
        if (!routeBudget.canReplan(now)) return;
        Vec3 destination = destination(now, activity);
        if (destination == null) {
            routeBudget.routeAttempted(now, false);
            creature.getNavigation().stop();
            return;
        }
        if (creature.position().distanceToSqr(destination) < .5) {
            routeBudget.routeAttempted(now, true);
            creature.getNavigation().stop();
            return;
        }
        var path = creature.getNavigation().createPath(BlockPos.containing(destination), 0);
        double speed = activity == WildlifeGroupPolicy.Activity.FLEE ? 1.1
                : Math.clamp(creature.position().distanceTo(destination) / 6, .35, .85);
        boolean success = path != null && path.canReach() && creature.getNavigation().moveTo(path, speed);
        routeBudget.routeAttempted(now, success);
        if (success) creature.getLookControl().setLookAt(destination.x, destination.y, destination.z, 20, 20);
        else creature.getNavigation().stop();
    }

    @Nullable private Vec3 destination(long now, WildlifeGroupPolicy.Activity activity) {
        BlockPos home = creature.wildlifeAnchor();
        UUID group = creature.wildlifeGroupId();
        if (home == null || group == null
                || !WildlifeFlightNavigation.loadedArea(creature.level(), creature.blockPosition(), WildlifeFlightNavigation.LOADED_RADIUS)) return null;
        boolean bird = creature.profile().host().equals("phantom") || creature.profile().host().equals("parrot");
        boolean localHeight = !creature.level().dimensionType().hasSkyLight()
                || creature.profile().id().equals("wisp") && !creature.level().canSeeSky(creature.blockPosition());
        var heightmap = bird ? Heightmap.Types.MOTION_BLOCKING : Heightmap.Types.MOTION_BLOCKING_NO_LEAVES;
        var journey = WildlifeJourney.sample(now, group);
        Vec3 centre = activity == WildlifeGroupPolicy.Activity.WANDER
                ? Vec3.atBottomCenterOf(home).add(journey.x(), 0, journey.z())
                : pauseOrigin == null ? creature.wildlifeLeader().position() : pauseOrigin;
        if (!WildlifeFlightNavigation.loadedArea(creature.level(), BlockPos.containing(centre), 2)) return null;
        // Air-spawned anchors already include their clearance above the canopy.
        // Underground wisps and roofed dimensions never consult the surface roof.
        double baseY = centre.y - (bird ? 1 : 1.8);
        if (!localHeight && activity == WildlifeGroupPolicy.Activity.WANDER) {
            baseY = creature.level().getHeight(heightmap, (int)Math.floor(centre.x), (int)Math.floor(centre.z));
        }
        var anchor = new FlockFlightPattern.Point(centre.x, baseY, centre.z);
        Vec3 threat = creature.wildlifeThreat();
        var threatPoint = new FlockFlightPattern.Point(threat.x, threat.y, threat.z);
        int slot = creature.wildlifeSlot();
        var formationActivity = activity == WildlifeGroupPolicy.Activity.WANDER && !journey.travelling()
                ? WildlifeGroupPolicy.Activity.REST : activity;
        var planned = FlockFlightPattern.sample(now + 20, group, bird, slot, formationActivity, anchor, threatPoint);
        Vec3 target = new Vec3(planned.x(), planned.y(), planned.z());

        List<DungeonsCreature> members = creature.wildlifeMembers();
        DungeonsCreature leader = creature.wildlifeLeader();
        var leaderPlan = FlockFlightPattern.sample(now + 20, group, bird, leader.wildlifeSlot(), formationActivity, anchor, threatPoint);
        if (activity == WildlifeGroupPolicy.Activity.REST) {
            // A loaded leader may occupy a nonzero persistent roster slot. Subtract
            // that slot so resting does not repeatedly push the whole flock sideways.
            target = centre.add(target.subtract(new Vec3(leaderPlan.x(), leaderPlan.y(), leaderPlan.z())));
        } else if (leader != creature && leader.isAlive() && leader.level() == creature.level()
                && creature.distanceToSqr(leader) > REJOIN_DISTANCE_SQR) {
            // Follow the loaded leader's matching formation position until close
            // enough to share the normal course again. No distant UUID lookup occurs.
            target = leader.position().add(target.subtract(new Vec3(leaderPlan.x(), leaderPlan.y(), leaderPlan.z())));
        }
        target = target.add(separation(members, bird ? 2.4 : 1.2));
        Vec3 delta = target.subtract(creature.position());
        if (delta.lengthSqr() > WildlifeJourney.MAX_STEP * WildlifeJourney.MAX_STEP)
            target = creature.position().add(delta.normalize().scale(WildlifeJourney.MAX_STEP));
        BlockPos column = BlockPos.containing(target);
        if (!WildlifeFlightNavigation.loadedArea(creature.level(), column, 2)) return null;
        if (!localHeight) {
            double terrain = creature.level().getHeight(heightmap, column.getX(), column.getZ());
            double minimumHeight = terrain + (formationActivity == WildlifeGroupPolicy.Activity.REST ? 1 : 2);
            target = new Vec3(target.x, Math.max(target.y, minimumHeight), target.z);
        }
        // A cliff or roof beyond the local vertical envelope is not a reason to
        // send a flock through the terrain or to search arbitrary heights.
        if (target.y > home.getY() + 32 || target.y < home.getY() - 16) return null;
        for (double lift : new double[]{0, 1, 2}) {
            Vec3 candidate = target.add(0, lift, 0);
            if (candidate.distanceToSqr(Vec3.atBottomCenterOf(home)) <= WildlifeJourney.MAX_LOCAL_DISTANCE * WildlifeJourney.MAX_LOCAL_DISTANCE
                    && WildlifeFlightNavigation.safeBody(creature, candidate)) return candidate;
        }
        return null;
    }

    private Vec3 separation(List<DungeonsCreature> members, double spacing) {
        Vec3 sum = Vec3.ZERO;
        for (DungeonsCreature other : members) {
            if (other == creature || !other.isAlive() || other.level() != creature.level()) continue;
            Vec3 away = creature.position().subtract(other.position());
            double distance = away.length();
            if (distance >= spacing) continue;
            if (distance < .001) {
                long seed = creature.getUUID().getLeastSignificantBits() ^ other.getUUID().getLeastSignificantBits();
                double angle = Math.floorMod(seed, 360) * Math.PI / 180;
                double sign = creature.getUUID().compareTo(other.getUUID()) < 0 ? -1 : 1;
                away = new Vec3(Math.cos(angle) * sign, 0, Math.sin(angle) * sign);
            } else away = away.scale(1 / distance);
            sum = sum.add(away.scale((spacing - distance) * .8));
        }
        return sum.lengthSqr() > 6.25 ? sum.normalize().scale(2.5) : sum;
    }
}
