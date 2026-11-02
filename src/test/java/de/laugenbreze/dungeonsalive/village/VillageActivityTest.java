package de.laugenbreze.dungeonsalive.village;

import org.junit.jupiter.api.Test;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

class VillageActivityTest {
    private VillageActivity.Observation observation(boolean job, boolean stationary, long worked, long now) {
        return new VillageActivity.Observation(true, false, false, false, false, false, true, job, stationary, worked, now);
    }

    @Test void workNeedsRecentRealWorkAndAnAccessibleOwnedJobSite() {
        assertEquals(VillageActivity.WORK, VillageActivity.select(observation(true, true, 100, 120)).mode());
        assertEquals(VillageActivity.IDLE, VillageActivity.select(observation(false, true, 100, 120)).mode());
        assertEquals(VillageActivity.IDLE, VillageActivity.select(observation(true, false, 100, 120)).mode());
        assertEquals(VillageActivity.IDLE, VillageActivity.select(observation(true, true, -1, 120)).mode());
        assertEquals(VillageActivity.IDLE, VillageActivity.select(observation(true, true, 100, 180)).mode());
        assertEquals(VillageActivity.IDLE, VillageActivity.select(observation(true, true, 130, 120)).mode());
    }

    @Test void panicAndTradingOverrideWorkAndSleepingNeverWorks() {
        assertEquals(VillageActivity.PANIC, VillageActivity.select(new VillageActivity.Observation(
                true, false, false, false, true, true, true, true, true, 100, 120)).mode());
        assertEquals(VillageActivity.TRADE, VillageActivity.select(new VillageActivity.Observation(
                true, false, false, false, false, true, true, true, true, 100, 120)).mode());
        assertEquals(VillageActivity.IDLE, VillageActivity.select(new VillageActivity.Observation(
                true, false, true, false, false, true, true, true, true, 100, 120)).mode());
        assertEquals(VillageActivity.IDLE, VillageActivity.select(new VillageActivity.Observation(
                true, true, false, false, false, false, true, true, true, 100, 120)).mode());
    }

    @Test void professionCoverageUsesExactNamespacedIds() {
        Set<String> vanilla = Set.of("armorer", "butcher", "cartographer", "cleric", "farmer", "fisherman",
                "fletcher", "leatherworker", "librarian", "mason", "shepherd", "toolsmith", "weaponsmith");
        for (String profession : vanilla) assertNotEquals(ProfessionGesture.NONE, ProfessionGesture.forId("minecraft:" + profession));
        assertEquals(19, ProfessionGesture.supported().size());
        assertEquals(ProfessionGesture.NONE, ProfessionGesture.forId("othermod:farmer"));
        assertEquals(ProfessionGesture.NONE, ProfessionGesture.forId("minecraft:nitwit"));
        assertEquals(ProfessionGesture.NONE, ProfessionGesture.forId("minecraft:none"));
    }
}
