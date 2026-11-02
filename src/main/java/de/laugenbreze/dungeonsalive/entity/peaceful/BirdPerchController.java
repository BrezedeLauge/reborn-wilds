package de.laugenbreze.dungeonsalive.entity.peaceful;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

/** Bounded terrain-aware part of the flock goal: approach, real landing, rest and departure. */
final class BirdPerchController {
    private static final int SEARCH_BACKOFF = 80;
    private static final double LANDING_DISTANCE_SQR = .3 * .3;
    private final DungeonsCreature bird;
    private final WildlifeGroupPolicy routeBudget = new WildlifeGroupPolicy();
    private record Seat(BlockPos block, double y, double score) {
        Vec3 feet() { return new Vec3(block.getX() + .5, y, block.getZ() + .5); }
    }

    BirdPerchController(DungeonsCreature bird) { this.bird = bird; }

    /** True owns this tick's movement; false lets the existing loaded flock flight run. */
    boolean tick() {
        var state = bird.birdRoutine();
        state.guiding = false;
        long now = bird.level().getGameTime();
        if (!WildlifeFlightNavigation.loadedArea(bird.level(), bird.blockPosition(), WildlifeFlightNavigation.LOADED_RADIUS)) {
            bird.getNavigation().stop();
            return true;
        }
        if (bird.wildlifeAlarmUntil() > now) {
            state.clearPerch(); state.phase = BirdRoutineState.Phase.FLEEING;
            state.resumeAfter = Math.max(state.resumeAfter, bird.wildlifeAlarmUntil() + 80);
            fly();
            return false;
        }
        if (state.phase == BirdRoutineState.Phase.FLEEING) state.phase = BirdRoutineState.Phase.FLYING;
        if (now < state.resumeAfter) { fly(); return false; }
        var members = bird.wildlifeMembers();
        var slots = members.stream().map(DungeonsCreature::wildlifeSlot).distinct().toList();
        if (slots.isEmpty()) slots = List.of(bird.wildlifeSlot());
        boolean shelter = Math.floorMod(bird.level().getDayTime(), 24_000L) >= 12_000 || bird.level().isRaining();
        var plan = BirdPerchCycle.sample(now, bird.wildlifeGroupId(), bird.wildlifeSlot(), slots, shelter);

        if (state.perch != null && !validSavedSeat(state)) abandon(now);
        if (plan.intent() != BirdPerchCycle.Intent.REST) {
            if (state.phase == BirdRoutineState.Phase.PERCHED || state.phase == BirdRoutineState.Phase.LANDING) {
                state.phase = BirdRoutineState.Phase.TAKING_OFF;
                bird.getNavigation().stop();
                BirdSounds.departed(bird);
            }
            if (state.phase == BirdRoutineState.Phase.TAKING_OFF && state.perch != null) {
                fly();
                if (bird.getY() >= state.surfaceY + 1.6) {
                    state.clearPerch(); state.phase = BirdRoutineState.Phase.FLYING;
                    return false;
                }
                moveTo(state.feet().add(0, 3, 0), .7, now);
                return true;
            }
            state.clearPerch(); state.phase = BirdRoutineState.Phase.FLYING;
            fly();
            return false;
        }
        // Returning members rejoin their actual loaded leader before choosing a new
        // shared resting area. No leader UUID lookup can load an absent member.
        var leader = bird.wildlifeLeader();
        if (leader != bird && bird.distanceToSqr(leader) > 12 * 12) {
            state.clearPerch(); state.phase = BirdRoutineState.Phase.FLYING;
            fly();
            return false;
        }
        if (state.perch == null) {
            if (now < state.searchAfter) { fly(); return false; }
            state.searchAfter = now + SEARCH_BACKOFF;
            Seat found = findSeat(leader.position(), null, false, members);
            if (found == null) { fly(); return false; }
            choose(found);
        }
        Vec3 feet = state.feet();
        double horizontal = horizontalDistanceSqr(bird.position(), feet);
        if (bird.onGround() && Math.abs(bird.getY() - state.surfaceY) < .08 && horizontal <= LANDING_DISTANCE_SQR) {
            boolean arrived = state.phase != BirdRoutineState.Phase.PERCHED;
            if (arrived) state.restedSince = now;
            state.phase = BirdRoutineState.Phase.PERCHED;
            bird.setNoGravity(false);
            bird.getNavigation().stop();
            bird.setXRot(0);
            if (now >= state.nextLook) {
                state.nextLook = now + 60 + bird.wildlifeSlot() * 7L;
                Vec3 toward = members.size() > 1 ? members.get((bird.wildlifeSlot() + 1) % members.size()).getEyePosition()
                        : feet.add(Math.cos(now * .02) * 3, bird.getEyeHeight(), Math.sin(now * .02) * 3);
                bird.getLookControl().setLookAt(toward.x, toward.y, toward.z, 15, 15);
            }
            if (plan.shuffleWindow() && plan.shuffleSlot() == bird.wildlifeSlot() && state.shuffledCycle != plan.cycle()
                    && now - state.restedSince >= 100) {
                state.shuffledCycle = plan.cycle();
                Seat nearby = findSeat(feet, state.perch, true, members);
                if (nearby != null) choose(nearby);
            }
            return true;
        }
        bird.setBirdPerched(false);
        if (state.phase == BirdRoutineState.Phase.LANDING) {
            bird.getNavigation().stop();
            bird.setNoGravity(false);
            if (now >= state.landingUntil || bird.getY() < state.surfaceY - .15 || horizontal > .55 * .55) {
                abandon(now);
                fly();
            }
            return true;
        }
        state.phase = BirdRoutineState.Phase.APPROACHING;
        if (now >= state.approachUntil) { rejectSeat(now); fly(); return true; }
        fly();
        // A final short, continuously collision-checked approach avoids treating the
        // navigation node's coarse arrival tolerance as contact with the actual roof.
        Vec3 delta = feet.subtract(bird.position());
        if (horizontal <= .15 * .15 && bird.getDeltaMovement().horizontalDistanceSqr() <= .02 * .02
                && bird.getY() >= state.surfaceY && bird.getY() <= state.surfaceY + 1.1
                && safeSegment(bird.position(), feet.add(0, .01, 0))) {
            state.phase = BirdRoutineState.Phase.LANDING;
            state.landingUntil = now + 50;
            bird.getNavigation().stop();
            bird.setNoGravity(false);
        } else if (delta.lengthSqr() <= 2.5 * 2.5 && safeSegment(bird.position(), feet.add(0, .12, 0))) {
            bird.getNavigation().stop();
            state.guiding = true;
            bird.getMoveControl().setWantedPosition(feet.x, feet.y + .12, feet.z, .35);
            bird.getLookControl().setLookAt(feet.x, feet.y + bird.getEyeHeight(), feet.z, 15, 15);
        } else moveTo(feet.add(0, 1, 0), .65, now);
        return true;
    }

    /** Called after the entity's real AI, travel and collision, before publishing its final pose. */
    static void afterMovement(DungeonsCreature bird) {
        var state = bird.birdRoutine();
        if (!bird.isAlive() || bird.isNoAi() || state.phase != BirdRoutineState.Phase.PERCHED) {
            bird.setBirdPerched(false);
            return;
        }
        boolean supported = state.perch != null && bird.onGround() && !bird.isNoGravity()
                && WildlifeFlightNavigation.loadedArea(bird.level(), bird.blockPosition(), 2)
                && Math.abs(bird.getY() - state.surfaceY) < .08
                && horizontalDistanceSqr(bird.position(), state.feet()) <= LANDING_DISTANCE_SQR
                && !bird.level().noBlockCollision(bird, bird.getBoundingBox().move(0, -.025, 0));
        boolean newlyPerched = !bird.birdPerched();
        bird.setBirdPerched(supported);
        if (supported) {
            if (newlyPerched) BirdSounds.rested(bird);
            return;
        }
        // The goal saw the preceding tick's onGround. A push or movement in this
        // tick may have removed contact; never leave that stale pose on clients.
        state.guiding = false;
        if (state.perch == null) state.phase = BirdRoutineState.Phase.FLYING;
        else {
            state.phase = BirdRoutineState.Phase.LANDING;
            state.landingUntil = bird.level().getGameTime() + 50;
        }
    }

    void stop() {
        bird.birdRoutine().guiding = false;
        bird.setBirdPerched(false);
        if (bird.profile() != null && bird.profile().flying()) bird.setNoGravity(true);
    }
    private void fly() { bird.setBirdPerched(false); bird.setNoGravity(true); }
    private void abandon(long now) {
        var state = bird.birdRoutine();
        state.clearPerch(); state.phase = BirdRoutineState.Phase.FLYING;
        state.searchAfter = now + SEARCH_BACKOFF;
        bird.getNavigation().stop(); bird.setBirdPerched(false);
    }
    private void choose(Seat seat) {
        var state = bird.birdRoutine();
        state.perch = seat.block; state.surfaceY = seat.y;
        state.phase = BirdRoutineState.Phase.APPROACHING; state.guiding = false;
        state.approachUntil = bird.level().getGameTime() + 240;
        state.routeFailures = 0;
        bird.getNavigation().stop(); fly();
    }
    private boolean validSavedSeat(BirdRoutineState state) {
        if (bird.wildlifeAnchor() == null || state.perch.distSqr(bird.wildlifeAnchor()) > WildlifeJourney.MAX_LOCAL_DISTANCE * WildlifeJourney.MAX_LOCAL_DISTANCE
                || state.perch.distToCenterSqr(bird.position()) > 32 * 32) return false;
        Seat actual = seatAt(state.perch, 0);
        return actual != null && Math.abs(actual.y - state.surfaceY) < .001;
    }
    private void moveTo(Vec3 target, double speed, long now) {
        if (!routeBudget.canReplan(now)) return;
        boolean valid = WildlifeFlightNavigation.safeBody(bird, target);
        var path = valid ? bird.getNavigation().createPath(BlockPos.containing(target), 0) : null;
        boolean success = path != null && path.canReach() && bird.getNavigation().moveTo(path, speed);
        routeBudget.routeAttempted(now, success);
        if (success) bird.birdRoutine().routeFailures = 0;
        else {
            bird.getNavigation().stop();
            if (++bird.birdRoutine().routeFailures >= 2) rejectSeat(now);
        }
    }
    private void rejectSeat(long now) {
        var state = bird.birdRoutine();
        state.avoidedPerch = state.perch; state.avoidUntil = now + 240;
        abandon(now);
    }
    private boolean safeSegment(Vec3 from, Vec3 to) {
        if (from.distanceToSqr(to) > 9) return false;
        int steps = Math.max(1, (int)Math.ceil(from.distanceTo(to) * 4));
        for (int step = 1; step <= steps; step++)
            if (!WildlifeFlightNavigation.safeBody(bird, from.lerp(to, (double)step / steps))) return false;
        return true;
    }
    @Nullable private Seat findSeat(Vec3 centre, @Nullable BlockPos previous, boolean shuffle, List<DungeonsCreature> members) {
        var candidates = new ArrayList<Seat>();
        double angle = bird.wildlifeSlot() * Math.PI / 3;
        Vec3 preference = centre.add(Math.cos(angle) * 3, 0, Math.sin(angle) * 3);
        BlockPos origin = BlockPos.containing(centre);
        // Exactly 49 local columns, at most once per 80 ticks. No arbitrary-height or
        // unloaded-column scans; the heightmap only identifies an already loaded top.
        for (int dx = -6; dx <= 6; dx += 2) for (int dz = -6; dz <= 6; dz += 2) {
            BlockPos column = origin.offset(dx, 0, dz);
            if (!WildlifeFlightNavigation.loadedArea(bird.level(), column, 2)) continue;
            int y = bird.level().getHeight(Heightmap.Types.MOTION_BLOCKING, column.getX(), column.getZ()) - 1;
            BlockPos support = new BlockPos(column.getX(), y, column.getZ());
            if (Math.abs(y + 1 - centre.y) > 12 || support.equals(previous)
                    || support.equals(bird.birdRoutine().avoidedPerch) && bird.level().getGameTime() < bird.birdRoutine().avoidUntil
                    || bird.wildlifeAnchor() == null || support.distSqr(bird.wildlifeAnchor()) > WildlifeJourney.MAX_LOCAL_DISTANCE * WildlifeJourney.MAX_LOCAL_DISTANCE
                    || shuffle && horizontalDistanceSqr(Vec3.atCenterOf(support), centre) > 16) continue;
            Seat candidate = seatAt(support, horizontalDistanceSqr(Vec3.atCenterOf(support), preference));
            if (candidate == null || members.stream().anyMatch(other -> other != bird && other.birdPerch() != null
                    && horizontalDistanceSqr(candidate.feet(), other.birdRoutine().feet())
                    < Math.pow((bird.getBbWidth() + other.getBbWidth()) * .5 + .6, 2))) continue;
            candidates.add(candidate);
        }
        candidates.sort(Comparator.comparingDouble(Seat::score).thenComparingLong(seat -> seat.block.asLong()));
        return candidates.isEmpty() ? null : candidates.getFirst();
    }
    @Nullable private Seat seatAt(BlockPos support, double score) {
        var level = bird.level();
        if (!WildlifeFlightNavigation.loadedArea(level, support, 2) || !level.getWorldBorder().isWithinBounds(support)
                || support.getY() <= level.getMinBuildHeight() || support.getY() + 3 >= level.getMaxBuildHeight()) return null;
        var block = level.getBlockState(support);
        if (!block.getFluidState().isEmpty() || block.is(BlockTags.DIRT) || block.is(BlockTags.SAND)
                || block.is(Blocks.MAGMA_BLOCK) || block.is(Blocks.CACTUS) || block.is(Blocks.CAMPFIRE)
                || block.is(Blocks.SOUL_CAMPFIRE) || block.is(Blocks.POWDER_SNOW)) return null;
        var shape = block.getCollisionShape(level, support);
        if (shape.isEmpty()) return null;
        double top = shape.max(net.minecraft.core.Direction.Axis.Y);
        if (top <= 0 || top > 1 || shape.toAabbs().stream().noneMatch(box -> Math.abs(box.maxY - top) < .001
                && box.minX <= .001 && box.maxX >= .999 && box.minZ <= .001 && box.maxZ >= .999)) return null;
        Vec3 feet = new Vec3(support.getX() + .5, support.getY() + top, support.getZ() + .5);
        for (int dy = 1; dy <= Math.ceil(bird.getBbHeight()) + 1; dy++) {
            var above = level.getBlockState(support.above(dy));
            if (above.is(Blocks.FIRE) || above.is(Blocks.SOUL_FIRE) || above.is(Blocks.WITHER_ROSE)) return null;
        }
        return WildlifeFlightNavigation.safeBody(bird, feet.add(0, .001, 0)) ? new Seat(support.immutable(), feet.y, score) : null;
    }
    private static double horizontalDistanceSqr(Vec3 a, Vec3 b) {
        double x = a.x - b.x, z = a.z - b.z;
        return x * x + z * z;
    }
}
