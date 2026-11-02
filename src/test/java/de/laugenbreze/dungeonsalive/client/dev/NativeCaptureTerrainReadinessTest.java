package de.laugenbreze.dungeonsalive.client.dev;

import java.util.Set;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class NativeCaptureTerrainReadinessTest {
    private static final Set<Long> VIEW = Set.of(12L, 45L, 89L);

    @Test void aLivingSceneCanFinishBetweenRepeatedMeshUpdatesWithoutFreezingBlocks() {
        var window = new NativeCaptureTerrainReadiness.StabilityWindow();
        // A live block system updates every second. A 1.25s no-rebuild rule can
        // never pass; stable delivered coverage plus three quiet frames can.
        for (int frame = 0; frame <= 42; frame++) {
            long millis = frame * 50L;
            boolean current = millis % 1000 >= 150;
            boolean ready = window.observe(millis * 1_000_000, VIEW, true, current);
            if (millis < 1250 || !current) assertFalse(ready);
            if (millis == 1500) assertTrue(ready);
            if (millis == 2000) assertFalse(ready, "An actual pending upload must still block the current capture");
        }
        assertFalse(window.observe(2150_000_000L, VIEW, true, true));
        assertFalse(window.observe(2200_000_000L, VIEW, true, true));
        assertTrue(window.observe(2250_000_000L, VIEW, true, true));
    }

    @Test void missingDeliveryOrNewVisiblePositionsRestartCoverageEvenWithEqualCounts() {
        var window = new NativeCaptureTerrainReadiness.StabilityWindow();
        for (int frame = 0; frame < 40; frame++) window.observe(frame * 50_000_000L, VIEW, true, true);
        assertFalse(window.observe(2000_000_000L, VIEW, false, true));
        assertFalse(window.observe(2050_000_000L, VIEW, true, true));
        var changed = Set.of(12L, 45L, 90L);
        for (int frame = 0; frame < 25; frame++)
            assertFalse(window.observe((2100L + frame * 50) * 1_000_000, changed, true, true));
        assertTrue(window.observe(3350_000_000L, changed, true, true));
    }

    @Test void elapsedTimeAloneAndEmptyViewsNeverAuthorizeAnImage() {
        var window = new NativeCaptureTerrainReadiness.StabilityWindow();
        assertFalse(window.observe(0, VIEW, true, true));
        assertFalse(window.observe(10_000_000_000L, VIEW, true, true), "Two rendered frames are not a stable visual sample");
        assertFalse(window.observe(20_000_000_000L, Set.of(), true, true));
        assertFalse(window.observe(30_000_000_000L, VIEW, true, true), "An empty/unknown view must reset readiness");
    }
}
