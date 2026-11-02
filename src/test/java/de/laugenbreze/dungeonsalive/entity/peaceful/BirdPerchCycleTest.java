package de.laugenbreze.dungeonsalive.entity.peaceful;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigInteger;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class BirdPerchCycleTest {
    private static final UUID GROUP = new UUID(17, 0);
    private static final List<Integer> SIX = List.of(0, 1, 2, 3, 4, 5);

    @Test void flockCruisesTogetherRestsAndDepartsInItsActualSlotOrder() {
        for (int slot : SIX) {
            assertEquals(BirdPerchCycle.Intent.CRUISE, plan(279, slot).intent());
            assertEquals(BirdPerchCycle.Intent.REST, plan(280, slot).intent());
            int departure = 800 + slot * 18;
            assertEquals(BirdPerchCycle.Intent.REST, plan(departure - 1, slot).intent());
            assertEquals(BirdPerchCycle.Intent.DEPART, plan(departure, slot).intent());
            assertEquals(BirdPerchCycle.Intent.DEPART, plan(959, slot).intent());
            assertEquals(BirdPerchCycle.Intent.CRUISE, plan(960, slot).intent());
        }
        assertEquals(BirdPerchCycle.Intent.DEPART, plan(818, 1).intent());
        assertEquals(BirdPerchCycle.Intent.REST, plan(818, 2).intent());
    }

    @Test void sparseLoadedMembersRotateTheHopWithoutDependingOnQueryOrder() {
        List<Integer> loaded = List.of(5, 1, 3);
        for (int cycle = -4; cycle <= 4; cycle++) {
            long tick = cycle * 960L + 600;
            var first = BirdPerchCycle.sample(tick, GROUP, 1, loaded, false);
            var reordered = BirdPerchCycle.sample(tick, GROUP, 5, List.of(3, 5, 1), false);
            assertEquals(first.cycle(), reordered.cycle());
            assertEquals(first.phase(), reordered.phase());
            assertEquals(first.shuffleSlot(), reordered.shuffleSlot());
            assertEquals(List.of(1, 3, 5).get(Math.floorMod(cycle, 3)), first.shuffleSlot());
            assertTrue(first.shuffleWindow());
            assertTrue(loaded.contains(first.shuffleSlot()));
        }
        assertFalse(plan(559, 0).shuffleWindow());
        assertTrue(plan(560, 0).shuffleWindow());
        assertTrue(plan(699, 0).shuffleWindow());
        assertFalse(plan(700, 0).shuffleWindow());
    }

    @Test void rainOrNightRetainsTheGroupCalendarButSuppressesDeparturesAndHops() {
        for (long tick : List.of(-1L, 0L, 600L, 890L, 960L, Long.MAX_VALUE)) {
            var ordinary = BirdPerchCycle.sample(tick, GROUP, 5, SIX, false);
            var shelter = BirdPerchCycle.sample(tick, GROUP, 5, SIX, true);
            assertEquals(BirdPerchCycle.Intent.REST, shelter.intent());
            assertFalse(shelter.shuffleWindow());
            assertEquals(ordinary.phase(), shelter.phase());
            assertEquals(ordinary.cycle(), shelter.cycle());
            assertEquals(ordinary.shuffleSlot(), shelter.shuffleSlot());
        }
        assertEquals(BirdPerchCycle.Intent.DEPART, plan(900, 5).intent());
    }

    @Test void signedAndExtremeWorldTimesKeepTheSameOverflowSafeGroupCalendar() {
        var period = BigInteger.valueOf(960);
        for (long offset : List.of(Long.MIN_VALUE, -961L, -1L, 0L, 959L, Long.MAX_VALUE)) {
            UUID id = new UUID(34, offset);
            for (long tick : List.of(Long.MIN_VALUE, -961L, -960L, -1L, 0L, 959L, 960L, Long.MAX_VALUE)) {
                BigInteger combined = BigInteger.valueOf(tick).add(BigInteger.valueOf(Math.floorMod(offset, 960)));
                BigInteger phase = combined.mod(period);
                long cycle = combined.subtract(phase).divide(period).longValueExact();
                var actual = BirdPerchCycle.sample(tick, id, 0, SIX, false);
                assertEquals(phase.intValueExact(), actual.phase(), "phase for " + tick + "/" + offset);
                assertEquals(cycle, actual.cycle(), "cycle for " + tick + "/" + offset);
            }
        }
    }

    @Test void corruptedRosterCannotInventHopMembersOrExceedTheSixBirdLimit() {
        for (List<Integer> invalid : List.of(List.<Integer>of(), List.of(0, 0), List.of(-1), List.of(6),
                List.of(0, 1, 2, 3, 4, 5, 6))) {
            assertThrows(IllegalArgumentException.class, () -> BirdPerchCycle.sample(600, GROUP, 0, invalid, false));
        }
        assertThrows(IllegalArgumentException.class, () -> BirdPerchCycle.sample(600, GROUP, -1, SIX, false));
        assertThrows(IllegalArgumentException.class, () -> BirdPerchCycle.sample(600, GROUP, 6, SIX, false));
        assertEquals(4, BirdPerchCycle.sample(600, GROUP, 4, List.of(4), false).shuffleSlot());
    }

    private static BirdPerchCycle.Plan plan(long time, int slot) {
        return BirdPerchCycle.sample(time, GROUP, slot, SIX, false);
    }
}
