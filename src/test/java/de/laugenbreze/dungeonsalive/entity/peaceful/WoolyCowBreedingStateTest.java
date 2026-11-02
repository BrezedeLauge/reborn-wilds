package de.laugenbreze.dungeonsalive.entity.peaceful;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class WoolyCowBreedingStateTest {
    @Test void loveNeedsFoodExpiresAndCannotBeRefilledOrUsedDuringPause() {
        var cow = new WoolyCowBreedingState();
        assertFalse(cow.inLove());
        assertTrue(cow.feed());
        assertTrue(cow.inLove());
        assertFalse(cow.feed());
        for (int i = 1; i < WoolyCowBreedingState.LOVE_TICKS; i++) cow.tick();
        assertTrue(cow.inLove());
        cow.tick(); assertFalse(cow.inLove());
        assertTrue(cow.feed());
        cow.bred();
        assertFalse(cow.inLove()); assertFalse(cow.feed());
        for (int i = 1; i < WoolyCowBreedingState.BREEDING_PAUSE_TICKS; i++) cow.tick();
        assertEquals(1, cow.age()); assertFalse(cow.feed());
        cow.tick(); assertEquals(0, cow.age()); assertTrue(cow.feed());
    }

    @Test void calfReallyGrowsAndEachWheatAdvancesOnlyItsRemainingYouth() {
        var calf = new WoolyCowBreedingState(); calf.born();
        assertEquals(-24_000, calf.age()); assertTrue(calf.baby());
        assertTrue(calf.feed()); assertEquals(-21_600, calf.age());
        assertTrue(calf.feed()); assertEquals(-19_440, calf.age());
        assertFalse(calf.inLove());
        for (int i = 1; i < 19_440; i++) calf.tick();
        assertEquals(-1, calf.age()); assertTrue(calf.baby());
        calf.tick(); assertFalse(calf.baby()); assertEquals(0, calf.age());
        assertFalse(calf.inLove(), "Maturing must not implicitly start a breeding window");
        calf.restore(-1, 0); assertTrue(calf.feed());
        assertEquals(0, calf.age()); assertFalse(calf.inLove());
    }

    @Test void reloadKeepsRemainingAgeAndLoveWithoutAdvancingUnloadedTime() {
        var original = new WoolyCowBreedingState(); original.born();
        for (int i = 0; i < 937; i++) original.tick();
        var restored = new WoolyCowBreedingState(); restored.restore(original.age(), original.loveTicks());
        assertEquals(original.age(), restored.age()); assertTrue(restored.baby());
        original.restore(0, 23); restored.restore(original.age(), original.loveTicks());
        assertEquals(23, restored.loveTicks());
        restored.interruptLove(); assertFalse(restored.inLove());
        original.bred(); restored.restore(original.age(), original.loveTicks());
        assertEquals(WoolyCowBreedingState.BREEDING_PAUSE_TICKS, restored.age());
        assertFalse(restored.readyForFood());
    }

    @Test void corruptSaveValuesCannotCreateLongerYouthPauseOrLoveOrLoveForCalves() {
        var cow = new WoolyCowBreedingState();
        cow.restore(Integer.MIN_VALUE, Integer.MAX_VALUE);
        assertEquals(-24_000, cow.age()); assertEquals(0, cow.loveTicks());
        cow.restore(Integer.MAX_VALUE, 600);
        assertEquals(6_000, cow.age()); assertEquals(0, cow.loveTicks());
        cow.restore(0, Integer.MAX_VALUE); assertEquals(600, cow.loveTicks());
        cow.restore(0, -1); assertEquals(0, cow.loveTicks());
    }

    @Test void loadedPopulationAndReservedGroupSlotsBothBoundBirths() {
        assertTrue(WoolyCowBreedingState.roomForBirth(2, 2));
        assertTrue(WoolyCowBreedingState.roomForBirth(23, 5));
        assertFalse(WoolyCowBreedingState.roomForBirth(24, 2));
        assertFalse(WoolyCowBreedingState.roomForBirth(2, 6), "Unloaded reserved members still fill a herd");
        assertFalse(WoolyCowBreedingState.roomForBirth(-1, 2));
        assertFalse(WoolyCowBreedingState.roomForBirth(2, -1));
        assertFalse(WoolyCowBreedingState.roomForBirth(Integer.MAX_VALUE, 1));
    }
}
