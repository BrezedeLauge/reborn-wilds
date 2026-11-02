package de.laugenbreze.dungeonsalive.world.water;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CurrentStrengthTest {
    @Test void constrictionsAndFallsAccelerateWaterWhileBanksAreCalmer() {
        double broad = CurrentStrength.speed(20, 3, 0, 10);
        double narrow = CurrentStrength.speed(6, 3, 0, 3);
        assertTrue(narrow > broad);
        assertTrue(CurrentStrength.speed(6, 3, 1, 3) > narrow);
        assertTrue(CurrentStrength.speed(20, 3, 0, 1) < broad);
        assertTrue(CurrentStrength.speed(2, 1, 5000, 1000) <= .14);
        assertEquals(0, CurrentStrength.speed(Double.NaN, 1, 1, 1));
    }
    @Test void currentConvergesWithoutStoppingFastSwimmersOrMakingUnboundedImpulses() {
        double speed = 0;
        for (int i = 0; i < 100; i++) speed += CurrentStrength.impulse(.08, speed, 5);
        assertEquals(.08, speed, .00001);
        assertEquals(0, CurrentStrength.impulse(.08, .3, 5));
        assertTrue(CurrentStrength.impulse(.14, -100, 5) <= .028);
        assertEquals(0, CurrentStrength.impulse(1, Double.NaN, 5));
    }
}
