package de.laugenbreze.dungeonsalive.entity.peaceful;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static de.laugenbreze.dungeonsalive.entity.peaceful.WildlifeGroupPolicy.Activity.*;
import static org.junit.jupiter.api.Assertions.*;

class WildlifeGroupPolicyTest {
    private static final UUID GROUP = UUID.fromString("03ce395f-0515-45e0-886a-33d93d08fb91");

    private static WildlifeGroupPolicy.Activity activity(UUID id, long gameTime, long dayTime,
                                                         boolean nocturnal, boolean raining, long alarmUntil) {
        return WildlifeGroupPolicy.activity(id, gameTime, dayTime, nocturnal, raining, alarmUntil);
    }

    @Test
    void activeGroupsGetACompleteSharedRestAndWanderCycleAtAnyClockOffset() {
        for (long start : new long[]{0, 731, -491, Long.MAX_VALUE - 300}) {
            int resting = 0, wandering = 0;
            for (int offset = 0; offset < 300; offset++) {
                var state = activity(GROUP, start + offset, 6_000, false, false, Long.MIN_VALUE);
                if (state == REST) resting++;
                if (state == WANDER) wandering++;
            }
            assertEquals(100, resting, "Rest share must survive clock offset " + start);
            assertEquals(200, wandering, "Wander share must survive clock offset " + start);
        }
    }

    @Test
    void daytimeAndNocturnalAnimalsKeepOppositeSchedulesAcrossTimeCommands() {
        long[] days = {0, 11_999, 24_000, 35_999, -12_001};
        long[] nights = {12_000, 23_999, 36_000, 47_999, -1};
        for (long time : days) {
            for (int tick = 0; tick < 300; tick++) {
                assertEquals(REST, activity(GROUP, tick, time, true, false, Long.MIN_VALUE),
                        "A nocturnal group must rest during world day " + time);
            }
        }
        for (long time : nights) {
            for (int tick = 0; tick < 300; tick++) {
                assertEquals(REST, activity(GROUP, tick, time, false, false, Long.MIN_VALUE),
                        "A daytime group must rest during world night " + time);
            }
        }
        long nocturnalWander = 0;
        for (int tick = 0; tick < 300; tick++) {
            if (activity(GROUP, tick, 18_000, true, false, Long.MIN_VALUE) == WANDER) nocturnalWander++;
        }
        assertEquals(200, nocturnalWander);
    }

    @Test
    void aStormPausesTheCurrentRhythmAndDangerOverridesBothWeatherAndSleep() {
        long movingTick = -1;
        for (int tick = 0; tick < 300; tick++) {
            if (activity(GROUP, tick, 6_000, false, false, Long.MIN_VALUE) == WANDER) {
                movingTick = tick;
                break;
            }
        }
        assertTrue(movingTick >= 0);
        assertEquals(REST, activity(GROUP, movingTick, 6_000, false, true, Long.MIN_VALUE));
        assertEquals(WANDER, activity(GROUP, movingTick, 6_000, false, false, Long.MIN_VALUE),
                "Clearing weather resumes the shared world-clock phase, not a new private cycle");
        long expires = movingTick + WildlifeGroupPolicy.ALARM_TICKS;
        assertEquals(FLEE, activity(GROUP, movingTick, 18_000, false, true, expires));
        assertEquals(FLEE, activity(GROUP, expires - 1, 18_000, false, true, expires));
        assertEquals(REST, activity(GROUP, expires, 18_000, false, true, expires));
    }

    @Test
    void failedRoutesBackOffAndSuccessfulRoutesStillHaveABoundedReplanRate() {
        var policy = new WildlifeGroupPolicy();
        assertTrue(policy.canReplan(0));
        policy.routeAttempted(0, false);
        for (int tick = 0; tick < 60; tick++) assertFalse(policy.canReplan(tick));
        assertTrue(policy.canReplan(60));
        assertTrue(policy.canReplan(60), "Checking the budget alone must not spend it");
        policy.routeAttempted(60, true);
        assertFalse(policy.canReplan(79));
        assertTrue(policy.canReplan(80));
        policy.routeAttempted(80, false);
        assertFalse(policy.canReplan(139));
        assertTrue(policy.canReplan(140));
    }

    @Test
    void differentGroupsHaveStaggeredRhythmsRatherThanAllWalkingOnTheSameTick() {
        UUID first = new UUID(0, 1), second = new UUID(0, 101);
        boolean observedDifferentStates = false;
        for (int tick = 0; tick < 300; tick++) {
            if (activity(first, tick, 6_000, false, false, Long.MIN_VALUE)
                    != activity(second, tick, 6_000, false, false, Long.MIN_VALUE)) {
                observedDifferentStates = true;
            }
        }
        assertTrue(observedDifferentStates);
    }
}
