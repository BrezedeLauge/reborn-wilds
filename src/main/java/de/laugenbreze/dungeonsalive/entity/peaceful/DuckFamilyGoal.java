package de.laugenbreze.dungeonsalive.entity.peaceful;

import java.util.EnumSet;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

/** Autonomous family life on real surface water and reachable dry banks. No artificial movement. */
final class DuckFamilyGoal extends Goal {
    private final DungeonsCreature duck;
    @Nullable private DuckHabitat.Places habitat;
    @Nullable private BlockPos rejectedShore;
    private long nextHabitatScan;
    private long nextRoute;
    private int failedRoutes;
    @Nullable private BlockPos swimTarget;
    private final DuckSwimProgress swimProgress = new DuckSwimProgress();
    private int swimLeg;

    DuckFamilyGoal(DungeonsCreature duck) { this.duck = duck; setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK)); }
    @Override public boolean canUse() {
        return duck.duckFamily() && duck.wildlifeGroupId() != null && duck.wildlifeAnchor() != null
                && duck.ownerId() == null && !duck.waiting() && !duck.isNoAi();
    }
    @Override public boolean canContinueToUse() { return canUse(); }
    @Override public boolean requiresUpdateEveryTick() { return true; }
    @Override public void stop() { duck.getNavigation().stop(); duck.duckDestination(null); }

    @Override public void tick() {
        var family = duck.wildlifeMembers().stream().filter(DungeonsCreature::duckFamily).toList();
        var leader = duck.wildlifeLeader();
        if (duck.duckYoung()) {
            // Only locally loaded members are queried. An absent parent is never
            // resolved through the world/chunk loader or silently replaced by a pet.
            leader = family.stream().filter(member -> !member.duckYoung()
                    && member.getUUID().equals(duck.duckParent())).findFirst().orElse(leader);
            // If the adult dies, is adopted, or leaves the loaded local family,
            // surviving young retain their parent UUID and follow one stable
            // local member. They can still flee and use the shore; no new adult
            // is created and no absent parent chunk is loaded.
        }
        if (leader != duck) { follow(leader); return; }
        long now = duck.level().getGameTime();
        boolean alarm = now < duck.wildlifeAlarmUntil();
        var state = duck.duckState();
        var before = state.activity();
        if (before == DuckFamilyState.Activity.SWIM) swimProgress.tick();
        boolean landActivity = land(before);
        boolean nearby = family.stream().allMatch(member -> member.distanceToSqr(duck) <= 16);
        boolean waterActivity = before == DuckFamilyState.Activity.SWIM || before == DuckFamilyState.Activity.TO_WATER;
        boolean together = nearby && family.stream().allMatch(member -> (!landActivity || !member.isInWater() && member.onGround())
                && (!waterActivity || member.isInWater()));
        BlockPos destination = duck.duckDestination();
        boolean arrived = destination != null && duck.position().distanceToSqr(position(destination)) < 2.25
                && (DuckHabitat.surfaceWater(duck.level(), destination) ? duck.isInWater() : duck.onGround());
        state.tick(arrived, together && (before != DuckFamilyState.Activity.SWIM || swimProgress.readyForShore()), duck.isInWater(), alarm);
        duck.syncDuckState();
        if (before != state.activity()) {
            duck.getNavigation().stop(); duck.duckDestination(null); nextRoute = now;
            swimTarget = null;
            if (state.activity() == DuckFamilyState.Activity.SWIM) swimProgress.reset();
            if (state.activity() == DuckFamilyState.Activity.SWIM || state.activity() == DuckFamilyState.Activity.REST)
                duck.playSound(DuckSounds.CONTACT.get(), .25F, .9F);
        }
        if (state.activity() == DuckFamilyState.Activity.ALERT) { duck.getNavigation().stop(); return; }
        if (state.activity() == DuckFamilyState.Activity.FLEE) { flee(now); return; }
        if (!nearby) { duck.getNavigation().stop(); return; }

        if (habitat != null && (!DuckHabitat.surfaceWater(duck.level(), habitat.water())
                || !DuckHabitat.safe(duck, habitat.water()) || !DuckHabitat.safe(duck, habitat.shore()))) {
            rejectedShore = habitat.shore(); habitat = null; nextHabitatScan = now;
            duck.getNavigation().stop(); duck.duckDestination(null);
            state.change(duck.isInWater() ? DuckFamilyState.Activity.SWIM : DuckFamilyState.Activity.TO_WATER);
            duck.syncDuckState();
        }
        if (habitat == null && now >= nextHabitatScan) {
            habitat = DuckHabitat.find(duck, rejectedShore);
            nextHabitatScan = now + 60;
        }
        if (habitat == null) { duck.getNavigation().stop(); return; }
        switch (state.activity()) {
            case SWIM -> swim(now, family);
            case TO_WATER -> move(habitat.water(), .85, now);
            case TO_SHORE -> move(habitat.shore(), .85, now);
            case FORAGE, PREEN, REST -> {
                if (duck.isInWater() || !arrived) move(habitat.shore(), .8, now);
                else {
                    duck.getNavigation().stop();
                    if (state.activity() == DuckFamilyState.Activity.FORAGE)
                        duck.getLookControl().setLookAt(duck.getX() + .35, duck.getY() - .25, duck.getZ(), 20, 30);
                }
            }
            default -> { }
        }
    }

    private void swim(long now, java.util.List<DungeonsCreature> family) {
        if (!duck.isInWater()) { move(habitat.water(), .85, now); return; }
        if (swimTarget != null && (!DuckHabitat.surfaceWater(duck.level(), swimTarget)
                || !DuckHabitat.safe(duck, swimTarget))) {
            swimTarget = null; swimLeg++; nextRoute = now;
        }
        if (swimTarget != null && duck.position().distanceToSqr(position(swimTarget)) < .8) {
            // Everyone follows this same short water corridor. Waiting at its end
            // prevents a youngster from standing inside the adult's entire orbit.
            Vec3 rendezvous = position(swimTarget);
            duck.getNavigation().stop();
            if (!family.stream().allMatch(member -> member.isInWater()
                    && member.position().distanceToSqr(rendezvous) < 1.44)) return;
            swimProgress.arrivedTogether();
            swimTarget = null; swimLeg++; nextRoute = now;
        }
        if (swimTarget == null) {
            duck.getNavigation().stop();
            if (now < nextRoute) return;
            swimTarget = waterWaypoint();
            if (swimTarget == null) { nextRoute = now + 40; return; }
        }
        boolean attempted = now >= nextRoute;
        if (!move(swimTarget, .85, now) && attempted) { swimTarget = null; swimLeg++; }
    }

    @Nullable private BlockPos waterWaypoint() {
        BlockPos center = duck.blockPosition();
        BlockPos anchor = duck.wildlifeAnchor() == null ? center : duck.wildlifeAnchor();
        double angle = swimLeg * 2.399963229728653 + Math.floorMod(duck.wildlifeGroupId().getLeastSignificantBits(), 64) * .1;
        BlockPos best = null, shortSection = null;
        double bestScore = Double.NEGATIVE_INFINITY, shortScore = Double.NEGATIVE_INFINITY;
        // Nearby points are chosen from actual surface water, not a fictitious
        // orbit through banks. A failed route rotates the next bounded choice.
        for (int dx = -5; dx <= 5; dx++) for (int dz = -5; dz <= 5; dz++) {
            int distance = dx * dx + dz * dz;
            if (distance == 0 || distance > 25) continue;
            for (int dy : new int[]{0, 1, -1}) {
                BlockPos candidate = center.offset(dx, dy, dz);
                if (candidate.distSqr(anchor) > 24 * 24 || !DuckHabitat.surfaceWater(duck.level(), candidate)
                        || !DuckHabitat.safe(duck, candidate)) continue;
                double score = (dx * Math.cos(angle) + dz * Math.sin(angle)) / Math.sqrt(distance)
                        - Math.abs(Math.sqrt(distance) - 3) * .15;
                if (distance >= 4 && score > bestScore) { best = candidate; bestScore = score; }
                else if (distance < 4 && score > shortScore) { shortSection = candidate; shortScore = score; }
            }
        }
        // A 2x2 pond has no two-block leg. Its real shorter sections still work;
        // a reduced/blocked pool also has a finite shore fallback in swimProgress.
        return best == null ? shortSection : best;
    }

    private void follow(DungeonsCreature leader) {
        if (land(leader.duckActivity()) && (duck.isInWater() || !duck.onGround()))
            duck.duckState().change(DuckFamilyState.Activity.TO_SHORE);
        else duck.duckState().follow(leader.duckState());
        duck.syncDuckState();
        duck.getLookControl().setLookAt(leader, 25, 25);
        long now = duck.level().getGameTime();
        if (duck.duckActivity() == DuckFamilyState.Activity.ALERT) { duck.getNavigation().stop(); return; }
        BlockPos sharedWater = leader.duckDestination();
        if (leader.duckActivity() == DuckFamilyState.Activity.SWIM && sharedWater != null
                && DuckHabitat.surfaceWater(duck.level(), sharedWater) && DuckHabitat.safe(duck, sharedWater)) {
            move(sharedWater, duck.distanceToSqr(leader) > 9 ? 1.15 : .95, now);
            return;
        }
        boolean needBank = !leader.isInWater() && (land(leader.duckActivity())
                || leader.duckActivity() == DuckFamilyState.Activity.TO_SHORE);
        if (duck.distanceToSqr(leader) < 2.25 && (!needBank || !duck.isInWater())
                && (!leader.isInWater() || duck.isInWater())) { duck.getNavigation().stop(); return; }
        if (now < nextRoute) return;
        BlockPos target = leader.blockPosition();
        if (needBank) {
            BlockPos chosen = null;
            double best = Double.POSITIVE_INFINITY;
            for (int dx = -2; dx <= 2; dx++) for (int dz = -2; dz <= 2; dz++) for (int dy = -1; dy <= 1; dy++) {
                BlockPos candidate = target.offset(dx, dy, dz);
                if (DuckHabitat.surfaceWater(duck.level(), candidate) || !DuckHabitat.safe(duck, candidate)) continue;
                double score = duck.position().distanceToSqr(Vec3.atBottomCenterOf(candidate))
                        + candidate.distSqr(target) * .3;
                if (score < best) { chosen = candidate; best = score; }
            }
            if (chosen != null) target = chosen;
        } else if (leader.isInWater()) {
            Vec3 heading = leader.getDeltaMovement().multiply(1, 0, 1);
            if (heading.lengthSqr() < .001) heading = leader.getLookAngle().multiply(1, 0, 1);
            Vec3 trailing = leader.position().subtract(heading.normalize().scale(.6 + duck.wildlifeSlot() * .4));
            BlockPos candidate = BlockPos.containing(trailing);
            if (DuckHabitat.surfaceWater(duck.level(), candidate) && DuckHabitat.safe(duck, candidate)) target = candidate;
        }
        move(target, duck.distanceToSqr(leader) > 9 ? 1.15 : .95, now);
    }

    private void flee(long now) {
        if (now < nextRoute) return;
        Vec3 away = duck.position().subtract(duck.wildlifeThreat()).multiply(1, 0, 1);
        if (away.lengthSqr() < .01) away = new Vec3(1, 0, 0);
        away = away.normalize();
        for (int distance : new int[]{5, 3, 2}) for (int turn : new int[]{0, 1, -1, 2, -2}) {
            double angle = turn * Math.PI / 4;
            Vec3 direction = new Vec3(away.x * Math.cos(angle) - away.z * Math.sin(angle), 0,
                    away.x * Math.sin(angle) + away.z * Math.cos(angle));
            BlockPos target = BlockPos.containing(duck.position().add(direction.scale(distance)));
            for (int rise : new int[]{0, 1, -1}) {
                BlockPos candidate = target.above(rise);
                if (DuckHabitat.safe(duck, candidate) && move(candidate, 1.2, now)) return;
            }
        }
        nextRoute = now + 20;
    }

    private boolean move(BlockPos destination, double speed, long now) {
        duck.duckDestination(destination);
        boolean water = DuckHabitat.surfaceWater(duck.level(), destination);
        if (duck.isInWater() == water && duck.position().distanceToSqr(position(destination)) < (water ? .64 : .16)) {
            duck.getNavigation().stop(); return true;
        }
        if (now < nextRoute) return !duck.getNavigation().isDone();
        var path = duck.getNavigation().createPath(destination, 0);
        boolean accepted = path != null && path.canReach() && duck.getNavigation().moveTo(path, speed);
        nextRoute = now + (accepted ? 20 : 40);
        if (accepted) failedRoutes = 0;
        else {
            duck.getNavigation().stop();
            if (++failedRoutes >= 2 && habitat != null) {
                rejectedShore = habitat.shore(); habitat = null; nextHabitatScan = now + 20; failedRoutes = 0;
            }
        }
        return accepted;
    }

    private Vec3 position(BlockPos pos) {
        return DuckHabitat.surfaceWater(duck.level(), pos) ? DuckHabitat.waterPosition(pos) : Vec3.atBottomCenterOf(pos);
    }
    private static boolean land(DuckFamilyState.Activity phase) {
        return phase == DuckFamilyState.Activity.FORAGE || phase == DuckFamilyState.Activity.PREEN || phase == DuckFamilyState.Activity.REST;
    }
}
