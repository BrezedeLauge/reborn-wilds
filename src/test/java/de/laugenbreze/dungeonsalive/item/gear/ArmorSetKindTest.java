package de.laugenbreze.dungeonsalive.item.gear;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ArmorSetKindTest {
    @Test void robesShortenTheActualCooldownButNeverRemoveAPositiveCooldown() {
        assertEquals(480, ArmorSetKind.BATTLE_ROBES.artifactCooldown(600));
        assertEquals(49, ArmorSetKind.BATTLE_ROBES.artifactCooldown(61));
        assertEquals(1, ArmorSetKind.BATTLE_ROBES.artifactCooldown(1));
        assertEquals(0, ArmorSetKind.BATTLE_ROBES.artifactCooldown(0));
        assertEquals(600, ArmorSetKind.WOLF.artifactCooldown(600));
        assertEquals(600, ArmorSetKind.SPIDER.artifactCooldown(600));
    }
    @Test void spiderHealingUsesPositiveFiniteHealthDamageAndHasAHardPerHitCap() {
        assertEquals(.6F, ArmorSetKind.SPIDER.lifeSteal(10), .00001F);
        assertEquals(2, ArmorSetKind.SPIDER.lifeSteal(1000));
        assertEquals(0, ArmorSetKind.SPIDER.lifeSteal(-10));
        assertEquals(0, ArmorSetKind.SPIDER.lifeSteal(Float.NaN));
        assertEquals(0, ArmorSetKind.SPIDER.lifeSteal(Float.POSITIVE_INFINITY));
        assertEquals(0, ArmorSetKind.WOLF.lifeSteal(10));
        assertEquals(0, ArmorSetKind.BATTLE_ROBES.lifeSteal(10));
    }
}
