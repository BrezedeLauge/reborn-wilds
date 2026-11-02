package de.laugenbreze.dungeonsalive.entity.peaceful;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DuckFamilyStateTest {
    @Test void completeCycleRequiresRealWaterDryArrivalAndTogetherness() {
        var state = new DuckFamilyState();
        tick(state, DuckFamilyState.SWIM_TICKS, true, true, true, false);
        assertEquals(DuckFamilyState.Activity.TO_SHORE, state.activity());
        tick(state, 500, true, true, true, false);
        assertEquals(DuckFamilyState.Activity.TO_SHORE, state.activity(), "A point inside water is not a dry shore");
        state.tick(true, true, false, false);
        assertEquals(DuckFamilyState.Activity.FORAGE, state.activity());
        tick(state, DuckFamilyState.FORAGE_TICKS, true, true, false, false);
        assertEquals(DuckFamilyState.Activity.PREEN, state.activity());
        tick(state, DuckFamilyState.PREEN_TICKS, true, true, false, false);
        assertEquals(DuckFamilyState.Activity.REST, state.activity());
        tick(state, DuckFamilyState.REST_TICKS, true, true, false, false);
        assertEquals(DuckFamilyState.Activity.TO_WATER, state.activity());
        state.tick(true, true, false, false);
        assertEquals(DuckFamilyState.Activity.TO_WATER, state.activity());
        state.tick(true, true, true, false);
        assertEquals(DuckFamilyState.Activity.SWIM, state.activity());
    }

    @Test void stragglerAndBlockedDestinationCannotBeCountedAsFinishedActivity() {
        var state = new DuckFamilyState();
        tick(state, 300, true, false, true, false);
        assertEquals(0, state.ticks());
        tick(state, DuckFamilyState.SWIM_TICKS, true, true, true, false);
        tick(state, 300, false, true, false, false);
        assertEquals(DuckFamilyState.Activity.TO_SHORE, state.activity());
        state.tick(true, true, false, false);
        tick(state, 300, true, false, false, false);
        assertEquals(DuckFamilyState.Activity.FORAGE, state.activity());
        assertEquals(0, state.ticks());
    }

    @Test void oneAlarmInterruptsRestAndRecoveryResumesInTheActualMedium() {
        var state = new DuckFamilyState();
        state.restore(0, DuckFamilyState.Activity.REST.ordinal(), 37);
        state.tick(true, true, false, true);
        assertEquals(DuckFamilyState.Activity.ALERT, state.activity());
        tick(state, DuckFamilyState.ALERT_TICKS - 1, false, false, false, true);
        assertEquals(DuckFamilyState.Activity.FLEE, state.activity());
        tick(state, 30, false, false, false, true);
        assertEquals(DuckFamilyState.Activity.FLEE, state.activity());
        state.tick(false, false, false, false);
        assertEquals(DuckFamilyState.Activity.TO_WATER, state.activity());
        state.tick(true, true, true, false);
        assertEquals(DuckFamilyState.Activity.SWIM, state.activity());
    }

    @Test void savedJuvenileAndPhaseResumeWithoutAdvancingUnloadedTime() {
        var original = new DuckFamilyState(); original.born();
        for (int i = 0; i < 37; i++) original.tickAge();
        original.restore(original.youngTicks(), DuckFamilyState.Activity.PREEN.ordinal(), 21);
        var restored = new DuckFamilyState();
        restored.restore(original.youngTicks(), original.activity().ordinal(), original.ticks());
        assertTrue(restored.young()); assertEquals(DuckFamilyState.YOUNG_TICKS - 37, restored.youngTicks());
        assertEquals(DuckFamilyState.Activity.PREEN, restored.activity()); assertEquals(21, restored.ticks());
        restored.restore(1, -5, -100); restored.tickAge();
        assertFalse(restored.young()); assertEquals(DuckFamilyState.Activity.SWIM, restored.activity()); assertEquals(0, restored.ticks());
        restored.restore(Integer.MAX_VALUE, 999, Integer.MAX_VALUE);
        assertEquals(DuckFamilyState.YOUNG_TICKS, restored.youngTicks()); assertEquals(24_000, restored.ticks());
        var adult = new DuckFamilyState(); restored.follow(adult);
        assertTrue(restored.young(), "Copying a parent's activity must not overwrite a child's age");
    }

    private static void tick(DuckFamilyState state, int count, boolean arrived, boolean together, boolean water, boolean alarm) {
        for (int i = 0; i < count; i++) state.tick(arrived, together, water, alarm);
    }
}
