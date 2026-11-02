package de.laugenbreze.dungeonsalive.entity.peaceful;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.UUID;

import static de.laugenbreze.dungeonsalive.entity.peaceful.WildlifeGroupPolicy.Activity.*;
import static org.junit.jupiter.api.Assertions.*;

class FlockFlightPatternTest {
    private static final UUID FLOCK = UUID.fromString("1b3cfddb-9890-4ff5-b18a-476d5e6083eb");
    private static final FlockFlightPattern.Point HOME = new FlockFlightPattern.Point(-25_000, 83, 31_415);
    private static final FlockFlightPattern.Point THREAT = new FlockFlightPattern.Point(-24_993, 83, 31_420);
    private static final double EPSILON = 1.0e-8;

    private static FlockFlightPattern.Point sample(long tick, boolean vulture, int slot,
                                                   WildlifeGroupPolicy.Activity activity) {
        return FlockFlightPattern.sample(tick, FLOCK, vulture, slot, activity, HOME, THREAT);
    }

    private static double horizontalDistance(FlockFlightPattern.Point a, FlockFlightPattern.Point b) {
        return Math.hypot(a.x() - b.x(), a.z() - b.z());
    }

    private static void assertPointEquals(FlockFlightPattern.Point expected, FlockFlightPattern.Point actual) {
        assertEquals(expected.x(), actual.x(), EPSILON);
        assertEquals(expected.y(), actual.y(), EPSILON);
        assertEquals(expected.z(), actual.z(), EPSILON);
    }

    @Test
    void allSixMembersKeepDistinctFiniteLocalWaypointsAcrossTheWholeFlightCycle() {
        for (boolean vulture : new boolean[]{false, true}) {
            for (var activity : WildlifeGroupPolicy.Activity.values()) {
                for (long tick : new long[]{0, 1, 90, 179, 359, 360, -1, Long.MIN_VALUE, Long.MAX_VALUE}) {
                    var waypoints = new ArrayList<FlockFlightPattern.Point>();
                    for (int slot = 0; slot < 6; slot++) {
                        var point = sample(tick, vulture, slot, activity);
                        assertTrue(Double.isFinite(point.x()) && Double.isFinite(point.y()) && Double.isFinite(point.z()));
                        assertTrue(horizontalDistance(point, HOME) <= FlockFlightPattern.MAX_HORIZONTAL_DISTANCE);
                        assertTrue(point.y() >= HOME.y() + .5 && point.y() <= HOME.y() + 9,
                                "Flight targets must remain within the documented local height band");
                        for (var previous : waypoints) assertTrue(horizontalDistance(previous, point) > .01,
                                "Distinct flock members must not be sent to one identical waypoint");
                        waypoints.add(point);
                    }
                }
            }
        }
    }

    @Test
    void aFullOrbitReturnsToTheSameFormationEvenAcrossNegativeWorldTime() {
        for (boolean vulture : new boolean[]{false, true}) {
            for (long tick : new long[]{-721, -1, 0, 137, Long.MIN_VALUE, Long.MAX_VALUE - 360}) {
                for (int slot = 0; slot < 6; slot++) {
                    assertPointEquals(sample(tick, vulture, slot, WANDER),
                            sample(tick + 360, vulture, slot, WANDER));
                }
            }
        }
    }

    @Test
    void vulturesTravelInPairedWingsBehindTheirLeadersActualCourse() {
        for (long tick : new long[]{0, 41, 190, 329}) {
            var leader = sample(tick, true, 0, WANDER);
            var before = sample(tick - 1, true, 0, WANDER);
            var after = sample(tick + 1, true, 0, WANDER);
            double length = horizontalDistance(before, after);
            double forwardX = (after.x() - before.x()) / length;
            double forwardZ = (after.z() - before.z()) / length;
            double previousBehind = 0;
            for (int leftSlot : new int[]{1, 3}) {
                var left = sample(tick, true, leftSlot, WANDER);
                var right = sample(tick, true, leftSlot + 1, WANDER);
                double leftForward = (left.x() - leader.x()) * forwardX + (left.z() - leader.z()) * forwardZ;
                double rightForward = (right.x() - leader.x()) * forwardX + (right.z() - leader.z()) * forwardZ;
                double leftSide = (left.x() - leader.x()) * forwardZ - (left.z() - leader.z()) * forwardX;
                double rightSide = (right.x() - leader.x()) * forwardZ - (right.z() - leader.z()) * forwardX;
                assertTrue(leftForward < previousBehind, "Each successive wing pair sits farther behind the leader");
                assertEquals(leftForward, rightForward, EPSILON);
                assertTrue(leftSide * rightSide < 0);
                assertEquals(-leftSide, rightSide, EPSILON);
                assertEquals(left.y(), right.y(), EPSILON);
                previousBehind = leftForward;
            }
        }
    }

    @Test
    void restingWispsHoldCompactStableHoverPlacesInsteadOfContinuingTheirOrbit() {
        var leader = sample(0, false, 0, REST);
        for (int slot = 0; slot < 6; slot++) {
            var resting = sample(0, false, slot, REST);
            assertPointEquals(resting, sample(Long.MAX_VALUE, false, slot, REST));
            assertTrue(horizontalDistance(resting, leader) <= 2);
            assertTrue(Math.abs(resting.y() - leader.y()) <= .5);
        }
        assertTrue(horizontalDistance(sample(0, false, 0, WANDER), sample(90, false, 0, WANDER)) > 1,
                "The same flock must still make real progress when its activity becomes WANDER");
    }

    @Test
    void anAlarmPlacesEveryMemberAwayFromTheThreatAndHandlesAnOverlappingThreat() {
        for (boolean vulture : new boolean[]{false, true}) {
            for (var threat : new FlockFlightPattern.Point[]{THREAT,
                    new FlockFlightPattern.Point(HOME.x() - 10, HOME.y(), HOME.z()),
                    new FlockFlightPattern.Point(HOME.x(), HOME.y(), HOME.z() - 10)}) {
                double awayX = HOME.x() - threat.x(), awayZ = HOME.z() - threat.z();
                for (int slot = 0; slot < 6; slot++) {
                    var escape = FlockFlightPattern.sample(100, FLOCK, vulture, slot, FLEE, HOME, threat);
                    assertTrue((escape.x() - HOME.x()) * awayX + (escape.z() - HOME.z()) * awayZ > 0,
                            "Even trailing formation members must move to the safe side of the anchor");
                    assertTrue(horizontalDistance(escape, threat) > horizontalDistance(HOME, threat));
                    var overlapping = FlockFlightPattern.sample(100, FLOCK, vulture, slot, FLEE, HOME, HOME);
                    assertTrue(horizontalDistance(overlapping, HOME) > 1);
                }
            }
        }
    }

    @Test
    void relocatingTheWholeFlockPreservesItsRelativeGeometry() {
        double dx = 1_500, dy = -30, dz = -900;
        var movedHome = new FlockFlightPattern.Point(HOME.x() + dx, HOME.y() + dy, HOME.z() + dz);
        var movedThreat = new FlockFlightPattern.Point(THREAT.x() + dx, THREAT.y() + dy, THREAT.z() + dz);
        for (boolean vulture : new boolean[]{false, true}) {
            for (var activity : WildlifeGroupPolicy.Activity.values()) {
                for (int slot = 0; slot < 6; slot++) {
                    var original = sample(131, vulture, slot, activity);
                    var moved = FlockFlightPattern.sample(131, FLOCK, vulture, slot, activity, movedHome, movedThreat);
                    assertPointEquals(new FlockFlightPattern.Point(original.x() + dx, original.y() + dy, original.z() + dz), moved);
                }
            }
        }
    }

    @Test
    void invalidSlotOrNonfiniteCoordinatesFailInsteadOfProducingAnUnboundedDestination() {
        assertThrows(IllegalArgumentException.class, () -> sample(0, true, -1, WANDER));
        assertThrows(IllegalArgumentException.class, () -> sample(0, true, 6, WANDER));
        assertThrows(IllegalArgumentException.class, () -> new FlockFlightPattern.Point(Double.NaN, 0, 0));
        assertThrows(IllegalArgumentException.class, () -> new FlockFlightPattern.Point(0, Double.POSITIVE_INFINITY, 0));
        assertThrows(IllegalArgumentException.class, () -> new FlockFlightPattern.Point(0, 0, Double.NEGATIVE_INFINITY));
    }
}
