package de.laugenbreze.dungeonsalive.entity.peaceful;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DuckSwimProgressTest {
    @Test void threeSharedSectionsAllowTheOrdinaryWaterPhaseToFinish() {
        var journey = new DuckSwimProgress();
        for (int i = 0; i < DuckSwimProgress.SHARED_SECTIONS - 1; i++) {
            journey.arrivedTogether();
            assertFalse(journey.readyForShore());
        }
        journey.arrivedTogether();
        assertTrue(journey.readyForShore());
        journey.reset();
        assertFalse(journey.readyForShore(), "A new swim must have its own actual shared route");
    }

    @Test void missingOrBlockedWaterSectionsCannotHoldTheFamilyForever() {
        var journey = new DuckSwimProgress();
        for (int i = 0; i < DuckSwimProgress.MAX_ROAM_TICKS - 1; i++) journey.tick();
        assertFalse(journey.readyForShore());
        journey.tick();
        assertTrue(journey.readyForShore());
        for (int i = 0; i < 1_000; i++) journey.tick();
        assertTrue(journey.readyForShore());
        journey.reset();
        assertFalse(journey.readyForShore());
    }
}
