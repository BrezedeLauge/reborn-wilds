package de.laugenbreze.dungeonsalive.entity.peaceful;

import java.util.UUID;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class WildlifeJourneyTest {
    private static final UUID GROUP = new UUID(0, 0);
    private static final double EPSILON = 1.0e-9;

    @Test void sharedClockIsPeriodicStableAfterReloadAndFiniteAtExtremeTimes() {
        for (long tick : new long[]{Long.MIN_VALUE, -5_001, -1, 0, 713, Long.MAX_VALUE - WildlifeJourney.ROUTE_TICKS}) {
            UUID restored = UUID.fromString(GROUP.toString());
            var stop = WildlifeJourney.sample(tick, GROUP);
            assertEquals(stop, WildlifeJourney.sample(tick, restored));
            assertEquals(stop, WildlifeJourney.sample(tick + WildlifeJourney.ROUTE_TICKS, restored));
            assertTrue(Double.isFinite(stop.x()) && Double.isFinite(stop.z()));
        }
        assertThrows(NullPointerException.class, () -> WildlifeJourney.sample(0, null));
    }

    @Test void routeReachesSeveralDistantPlacesWithoutLeavingItsLocalBound() {
        for (long seed : new long[]{0, 49, Long.MIN_VALUE, Long.MAX_VALUE}) {
            UUID group = new UUID(seed, seed);
            double maximumDistance = 0;
            for (int tick = 0; tick < WildlifeJourney.ROUTE_TICKS; tick++) {
                var point = WildlifeJourney.sample(tick, group);
                double distance = Math.hypot(point.x(), point.z());
                maximumDistance = Math.max(maximumDistance, distance);
                assertTrue(distance <= WildlifeJourney.MAX_CENTRE_DISTANCE);
                var next = WildlifeJourney.sample(tick + 1, group);
                assertTrue(Math.hypot(next.x() - point.x(), next.z() - point.z()) < .15,
                        "A shared destination must advance gradually, including the route wrap");
            }
            assertTrue(maximumDistance > 30, "Local migration must reach beyond the previous eight-block wander circle");
        }
        var east = WildlifeJourney.sample(1_000, GROUP);
        var north = WildlifeJourney.sample(2_200, GROUP);
        var west = WildlifeJourney.sample(3_400, GROUP);
        assertEquals(30, east.x(), EPSILON); assertEquals(8, east.z(), EPSILON);
        assertEquals(-6, north.x(), EPSILON); assertEquals(32, north.z(), EPSILON);
        assertEquals(-28, west.x(), EPSILON); assertEquals(-12, west.z(), EPSILON);
    }

    @Test void routeFreezesDuringTheExistingGroupRestPhaseAndStaysAtEachDestination() {
        for (long seed : new long[]{0, 127, -831}) {
            UUID group = new UUID(seed, seed);
            for (int tick = 0; tick < WildlifeJourney.ROUTE_TICKS; tick++) {
                var activity = WildlifeGroupPolicy.activity(group, tick, 6_000, false, false, Long.MIN_VALUE);
                if (activity == WildlifeGroupPolicy.Activity.REST) {
                    assertEquals(WildlifeJourney.sample(tick, group), WildlifeJourney.sample(tick + 1, group));
                }
            }
        }
        var arrived = WildlifeJourney.sample(900, GROUP);
        assertFalse(arrived.travelling());
        assertEquals(arrived, WildlifeJourney.sample(1_199, GROUP));
        assertTrue(WildlifeJourney.sample(1_300, GROUP).travelling());
    }

    @Test void groupRotationChangesDirectionButPreservesRouteShapeAndDistance() {
        for (int tick = 0; tick < WildlifeJourney.ROUTE_TICKS; tick += 17) {
            var first = WildlifeJourney.sample(tick, GROUP);
            var rotated = WildlifeJourney.sample(tick, new UUID(1_571, 0));
            assertEquals(Math.hypot(first.x(), first.z()), Math.hypot(rotated.x(), rotated.z()), EPSILON);
            assertEquals(first.destination(), rotated.destination());
            assertEquals(first.travelling(), rotated.travelling());
        }
        assertTrue(WildlifeJourney.sample(1_000, new UUID(1_571, 0)).z() > 29);
    }
}
