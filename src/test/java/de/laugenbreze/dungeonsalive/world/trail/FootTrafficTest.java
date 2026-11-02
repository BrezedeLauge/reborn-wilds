package de.laugenbreze.dungeonsalive.world.trail;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class FootTrafficTest {
    @Test void sparseTrafficStaysGrassAndRepeatedCrossingsCreateTwoWearStages() {
        var path = new FootTraffic("minecraft:grass_block", 0, 0, 0);
        for (int i = 1; i <= 5; i++) { path.stepped(i); assertEquals(0, path.desiredStage(6, 12)); }
        path.stepped(6); assertEquals(1, path.desiredStage(6, 12)); path.setStage(1);
        for (int i = 7; i <= 12; i++) path.stepped(i);
        assertEquals(2, path.desiredStage(6, 12));
    }
    @Test void evenTheFullyWornPathReturnsToItsOriginalGroundAfterInactivity() {
        var path = new FootTraffic("minecraft:podzol", 12, 2, 100);
        assertEquals(2, path.recoveredStage(12099, 12000));
        assertEquals(1, path.recoveredStage(12100, 12000));
        assertEquals(0, path.recoveredStage(24100, 12000));
        assertEquals("minecraft:podzol", path.original());
    }
    @Test void renewedUsePostponesRegrowthAndRestoredHistoryKeepsThatDeadline() {
        var path = new FootTraffic("minecraft:mycelium", 12, 2, 0);
        path.stepped(11999);
        var restored = new FootTraffic(path.original(), path.steps(), path.stage(), path.lastStep());
        assertEquals(2, restored.recoveredStage(12000, 12000));
        assertEquals(1, restored.recoveredStage(23999, 12000));
        assertEquals(0, restored.recoveredStage(35999, 12000));
    }
    @Test void futureSavedTimesAndSubtractionOverflowRebaseToOneNormalGracePeriod() {
        for (long saved : new long[]{Long.MAX_VALUE, Long.MIN_VALUE}) {
            var path = new FootTraffic("minecraft:grass_block", 12, 2, saved);
            assertEquals(2, path.recoveredStage(100, 200));
            assertEquals(100, path.lastStep());
            assertEquals(2, path.recoveredStage(299, 200));
            assertEquals(1, path.recoveredStage(300, 200));
            assertEquals(0, path.recoveredStage(500, 200));
        }
    }
}
