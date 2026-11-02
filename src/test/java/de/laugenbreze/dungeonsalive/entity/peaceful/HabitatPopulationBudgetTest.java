package de.laugenbreze.dungeonsalive.entity.peaceful;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class HabitatPopulationBudgetTest {
    @Test void neighboringPlayersCannotReplenishTheSameRegionBeforeItsSavedCooldown() {
        var first = new HabitatPopulationBudget();
        assertTrue(first.available(91, 100));
        assertTrue(first.recordSuccess(91, 100));
        assertFalse(first.recordSuccess(91, 101));
        var loaded = new HabitatPopulationBudget();
        first.snapshot().forEach(loaded::restore);
        assertFalse(loaded.available(91, 1299));
        assertTrue(loaded.available(91, 1300));
    }
    @Test void inspectingFailedLocationsDoesNotConsumeHistoryOrPopulation() {
        var budget = new HabitatPopulationBudget();
        for (int index = 0; index < 10_000; index++) assertTrue(budget.available(index, 100));
        assertTrue(budget.snapshot().isEmpty());
        assertEquals(0, HabitatPopulationBudget.groupSize(5, 23, 24, 90, 96));
        assertEquals(0, HabitatPopulationBudget.groupSize(5, 0, 24, 96, 96));
        assertEquals(2, HabitatPopulationBudget.groupSize(5, 22, 24, 90, 96));
        assertEquals(3, HabitatPopulationBudget.groupSize(5, 0, 24, 93, 96));
        assertEquals(6, HabitatPopulationBudget.groupSize(300, 0, 24, 0, 96));
    }
    @Test void manyTravelersCannotMakeHistoryUnboundedAndExpiredRegionsAreReclaimed() {
        var budget = new HabitatPopulationBudget();
        for (int index = 0; index < HabitatPopulationBudget.MAX_REGIONS; index++) assertTrue(budget.recordSuccess(index, 0));
        assertFalse(budget.recordSuccess(99999, 10));
        assertEquals(HabitatPopulationBudget.MAX_REGIONS, budget.snapshot().size());
        assertTrue(budget.recordSuccess(99999, 1200));
        assertEquals(1, budget.snapshot().size());
    }
    @Test void allRealVanillaBiomesHaveExplicitBoundedEcologyAndTheVoidStaysEmpty() {
        assertEquals(68, WildlifeHabitats.all().size());
        WildlifeHabitats.all().forEach((biome, entries) -> {
            if (biome.equals("minecraft:the_void")) { assertTrue(entries.isEmpty()); return; }
            assertFalse(entries.isEmpty(), biome);
            entries.forEach(entry -> {
                assertTrue(entry.min() >= 2 && entry.max() <= WildlifeGroupPolicy.MAX_MEMBERS, biome);
                assertTrue(entry.min() <= entry.max());
                assertFalse(entry.id().contains("summoned") || entry.id().contains("key_golem"));
            });
        });
        assertTrue(WildlifeHabitats.forBiome("minecraft:jungle").stream().anyMatch(p -> p.layer() == WildlifeHabitats.Layer.CANOPY));
        assertTrue(WildlifeHabitats.forBiome("minecraft:jungle").stream().anyMatch(p -> p.layer() == WildlifeHabitats.Layer.GROUND));
        assertTrue(WildlifeHabitats.forBiome("minecraft:deep_ocean").stream().allMatch(p -> p.layer() == WildlifeHabitats.Layer.WATER));
    }
}
