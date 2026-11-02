package de.laugenbreze.dungeonsalive.world.corruption;

import java.util.HashSet;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CorruptionFrontTest {
    @Test void spiralVisitsEachBoundedCellOnceInAnOutwardFront() {
        var visited = new HashSet<CorruptionFront.Offset>(); int previousRing = 0, circular = 0;
        for (int i = 0; i < CorruptionFront.CANDIDATES; i++) {
            var position = CorruptionFront.at(i);
            assertTrue(visited.add(position), "A cursor must not repeatedly convert the same cell");
            int ring = Math.max(Math.abs(position.x()), Math.abs(position.z()));
            assertTrue(ring >= previousRing && ring <= CorruptionFront.RADIUS); previousRing = ring;
            if (position.inCircle()) { circular++; assertTrue(position.x() * position.x() + position.z() * position.z() <= 48 * 48); }
        }
        assertEquals(CorruptionFront.CANDIDATES, visited.size()); assertTrue(circular > 7_000 && circular < visited.size());
        assertThrows(IllegalArgumentException.class, () -> CorruptionFront.at(-1));
        assertThrows(IllegalArgumentException.class, () -> CorruptionFront.at(CorruptionFront.CANDIDATES));
    }
    @Test void growthAndWorkCapsCannotBeResetByRemovingAnOldPatch() {
        var front = new CorruptionFront();
        for (int i = 0; i < CorruptionFront.MAX_PATCHES; i++) { assertTrue(front.canGrow()); front.scanned(); front.grew(); }
        assertFalse(front.canGrow()); assertEquals(256, front.grown());
        assertThrows(IllegalStateException.class, front::grew);
        assertEquals(2_048, CorruptionFront.MAX_REGIONS * CorruptionFront.MAX_PATCHES);
        assertEquals(64, CorruptionFront.CHECKS_PER_PULSE); assertEquals(4, CorruptionFront.CHANGES_PER_PULSE);
    }
    @Test void focusStopsImmediatelyAndCompletedCleansingNeverReactivates() {
        var front = new CorruptionFront(); front.scanned(); front.grew();
        assertTrue(front.cleanse()); assertFalse(front.canGrow()); assertFalse(front.cleanse());
        assertThrows(IllegalStateException.class, front::grew);
        front.restoredAll(); assertEquals(CorruptionFront.Stage.CLEANSED, front.stage()); assertFalse(front.cleanse());
        assertEquals(1, front.grown(), "Historical growth remains bounded even after nature is restored");
    }
    @Test void saveReloadRetainsExactCursorAndRejectsUnboundedHistory() {
        var original = new CorruptionFront();
        for (int i = 0; i < 71; i++) original.scanned();
        original.grew(); original.grew();
        var restored = new CorruptionFront(); restored.restore(original.cursor(), original.grown(), original.stage());
        assertEquals(CorruptionFront.at(original.cursor()), CorruptionFront.at(restored.cursor()));
        assertEquals(original.grown(), restored.grown()); assertTrue(restored.canGrow());
        assertThrows(IllegalArgumentException.class, () -> restored.restore(CorruptionFront.CANDIDATES + 1, 0, CorruptionFront.Stage.GROWING));
        assertThrows(IllegalArgumentException.class, () -> restored.restore(0, 257, CorruptionFront.Stage.GROWING));
        assertThrows(IllegalArgumentException.class, () -> restored.restore(0, 0, null));
    }
}
