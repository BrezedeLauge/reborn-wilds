package de.laugenbreze.dungeonsalive.entity.peaceful;

import org.junit.jupiter.api.Test;

import static de.laugenbreze.dungeonsalive.entity.peaceful.MerchantRoutine.Activity.*;
import static org.junit.jupiter.api.Assertions.*;

/** Sequences of real observations, independent of Minecraft navigation and registries. */
class MerchantRoutineTest {
    private static MerchantRoutine.Decision observe(MerchantRoutine routine, long tick, long dayTime,
                                                     boolean raining, boolean trading,
                                                     double distanceSqr, boolean navigationDone) {
        return routine.decide(new MerchantRoutine.Observation(
                tick, dayTime, raining, trading, distanceSqr, navigationDone));
    }

    @Test
    void crossingTheCampRadiusStartsACompleteReturnRatherThanStoppingAtTheBoundary() {
        var routine = new MerchantRoutine();
        assertEquals(WANDER, observe(routine, 0, 6_000, false, false, 0, true).activity());
        assertEquals(RETURN_HOME, observe(routine, 1, 6_000, false, false, 49, true).activity());
        routine.routeAttempted(1, true);
        assertEquals(RETURN_HOME, observe(routine, 10, 6_000, false, false, 25, false).activity(),
                "An inward step across the daytime radius must not cancel the route to camp");
        assertEquals(RETURN_HOME, observe(routine, 21, 6_000, false, false, 4, false).activity());
        assertEquals(WANDER, observe(routine, 25, 6_000, false, false, 2.25, true).activity());
    }

    @Test
    void eveningReturnSurvivesTheNextDawnUntilTheMerchantActuallyArrives() {
        var routine = new MerchantRoutine();
        assertEquals(WANDER, observe(routine, 0, 11_999, false, false, 16, true).activity());
        assertEquals(RETURN_HOME, observe(routine, 1, 12_000, false, false, 16, true).activity());
        routine.routeAttempted(1, true);
        assertEquals(RETURN_HOME, observe(routine, 2, 24_000, false, false, 9, false).activity(),
                "Changing world time does not teleport a merchant home or finish its return");
        assertEquals(WANDER, observe(routine, 3, 24_000, false, false, 1, true).activity());
        var atNight = observe(routine, 4, 36_000, false, false, 1, false);
        assertEquals(REST, atNight.activity());
        assertTrue(atNight.stopNavigation());
        assertFalse(atNight.planRoute());
    }

    @Test
    void anOpenTradePausesShelterMovementAndClosingItResumesTheRainRoutine() {
        var routine = new MerchantRoutine();
        var trading = observe(routine, 0, 6_000, true, true, 16, false);
        assertEquals(TRADING, trading.activity());
        assertTrue(trading.stopNavigation());
        assertFalse(trading.planRoute());
        assertEquals(RETURN_HOME, observe(routine, 1, 6_000, true, false, 16, true).activity());
        assertEquals(REST, observe(routine, 2, 6_000, true, false, 1, true).activity());
        assertEquals(WANDER, observe(routine, 3, 6_000, false, false, 1, true).activity());
    }

    @Test
    void damageInterruptsTradingThenExpiresIntoAHomeReturnEvenInsideTheDayRadius() {
        var routine = new MerchantRoutine();
        assertEquals(TRADING, observe(routine, 99, 6_000, false, true, 1, false).activity());
        routine.hurt(100);
        assertEquals(FLEE, observe(routine, 100, 6_000, false, true, 1, false).activity(),
                "The entity adapter must be able to interrupt a trade after actual damage");
        assertTrue(routine.frightened(199));
        assertEquals(FLEE, observe(routine, 199, 6_000, false, false, 9, true).activity());
        assertFalse(routine.frightened(200));
        assertEquals(RETURN_HOME, observe(routine, 200, 6_000, false, false, 9, true).activity());
        assertEquals(TRADING, observe(routine, 201, 6_000, false, true, 9, true).activity());
        assertEquals(RETURN_HOME, observe(routine, 202, 6_000, false, false, 9, true).activity(),
                "A peaceful trade can pause, but must not erase, the pending home return");
        assertEquals(WANDER, observe(routine, 203, 6_000, false, false, 1, true).activity());
    }

    @Test
    void aFailedPathKeepsItsRetryBudgetAcrossTradesWeatherAndRepeatedHits() {
        var routine = new MerchantRoutine();
        assertTrue(observe(routine, 0, 6_000, false, false, 0, true).planRoute());
        routine.routeAttempted(0, false);
        assertFalse(observe(routine, 10, 12_000, false, false, 49, true).planRoute());
        assertEquals(TRADING, observe(routine, 20, 12_000, false, true, 49, true).activity());
        routine.hurt(30);
        assertFalse(observe(routine, 30, 12_000, true, false, 49, true).planRoute());
        routine.hurt(79);
        assertFalse(observe(routine, 79, 12_000, true, false, 49, true).planRoute(),
                "Repeated damage must not restart pathfinding every tick");
        assertTrue(observe(routine, 80, 12_000, true, false, 49, true).planRoute());
        routine.routeAttempted(80, false);
        routine.hurt(100);
        assertFalse(observe(routine, 159, 12_000, false, false, 49, true).planRoute());
        assertTrue(observe(routine, 160, 12_000, false, false, 49, true).planRoute());
    }

    @Test
    void normalWanderingIsSparseButAnAlarmCanUseTheNextBoundedReplanSlot() {
        var routine = new MerchantRoutine();
        assertTrue(observe(routine, 0, 6_000, false, false, 0, true).planRoute());
        routine.routeAttempted(0, true);
        assertFalse(observe(routine, 20, 6_000, false, false, 4, true).planRoute());
        assertFalse(observe(routine, 159, 6_000, false, false, 4, true).planRoute());
        assertFalse(observe(routine, 160, 6_000, false, false, 4, false).planRoute(),
                "A still-running wander path does not need a replacement destination");
        assertTrue(observe(routine, 161, 6_000, false, false, 4, true).planRoute());
        routine.routeAttempted(161, true);
        routine.hurt(162);
        assertFalse(observe(routine, 162, 6_000, false, false, 4, true).planRoute());
        assertTrue(observe(routine, 181, 6_000, false, false, 4, true).planRoute(),
                "Danger uses the next 20-tick route slot, not the 160-tick wander interval");
    }

    @Test
    void dayTimeWrapsAcrossDaysAndNegativeTimeCommandsWithoutChangingTheSchedule() {
        var routine = new MerchantRoutine();
        long[] dayTimes = {0, 11_999, 24_000, 35_999, -12_001};
        long[] nightTimes = {12_000, 23_999, 36_000, 47_999, -1, -12_000};
        for (long time : dayTimes) {
            assertEquals(WANDER, observe(routine, 100, time, false, false, 0, true).activity(),
                    "Expected daytime for world time " + time);
        }
        for (long time : nightTimes) {
            assertEquals(REST, observe(routine, 100, time, false, false, 0, true).activity(),
                    "Expected nighttime for world time " + time);
        }
    }
}
