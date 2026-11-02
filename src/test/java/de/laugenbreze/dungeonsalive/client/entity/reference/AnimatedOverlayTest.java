package de.laugenbreze.dungeonsalive.client.entity.reference;

import net.minecraft.resources.ResourceLocation;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AnimatedOverlayTest {
    @Test void sourceSequenceAdvancesInTicksAndWrapsWithoutLosingLongWorldClockPrecision() {
        var layer = new ReferenceMobDefinition.AnimatedOverlay(ResourceLocation.parse("dungeonsalive:textures/fire/"), 31, 1, .5F, true);
        assertEquals("dungeonsalive:textures/fire/30.png", layer.frame(layer.frameIndex(30.999)).toString());
        assertEquals(0, layer.frameIndex(31));
        long later = 31L * 1_000_000_000L;
        assertEquals(12, layer.frameIndex(later + 12.75));
        var slow = new ReferenceMobDefinition.AnimatedOverlay(layer.prefix(), 31, 3, 1, false);
        assertEquals(0, slow.frameIndex(2.999));
        assertEquals(1, slow.frameIndex(3));
        assertThrows(IllegalArgumentException.class, () -> layer.frameIndex(Double.NaN));
        assertThrows(IllegalArgumentException.class, () -> layer.frame(31));
    }
}
