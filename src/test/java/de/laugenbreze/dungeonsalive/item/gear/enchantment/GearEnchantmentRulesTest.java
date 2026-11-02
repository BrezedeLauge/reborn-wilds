package de.laugenbreze.dungeonsalive.item.gear.enchantment;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class GearEnchantmentRulesTest {
    @Test void savedCooldownSurvivesNormalTicksButCannotLockEffectsAfterClockRollback() {
        assertFalse(GearEnchantmentRules.cooldownReady(100, 120, 20));
        assertFalse(GearEnchantmentRules.cooldownReady(119, 120, 20));
        assertTrue(GearEnchantmentRules.cooldownReady(120, 120, 20));
        assertTrue(GearEnchantmentRules.cooldownReady(0, 120, 20));
        assertTrue(GearEnchantmentRules.cooldownReady(100, Long.MAX_VALUE, 20));
        assertTrue(GearEnchantmentRules.cooldownReady(Long.MIN_VALUE, Long.MAX_VALUE, 20));
    }
    @Test void woundedTargetsGainCommittedBonusWithoutHealingOrMalformedHealthAmplifyingIt() {
        assertEquals(1, GearEnchantmentRules.committedMultiplier(3, 100, 100));
        assertEquals(1.15F, GearEnchantmentRules.committedMultiplier(3, 50, 100), .00001);
        assertEquals(1.3F, GearEnchantmentRules.committedMultiplier(999, -10, 100), .00001);
        assertEquals(1, GearEnchantmentRules.committedMultiplier(3, Float.NaN, 100));
        assertEquals(1, GearEnchantmentRules.committedMultiplier(3, 10, 0));
        assertEquals(1, GearEnchantmentRules.committedMultiplier(3, 200, 100));
    }
    @Test void criticalRollsRespectEachRanksBoundaryAndCapForgedLevels() {
        for (int rank = 1; rank <= 3; rank++) {
            assertEquals(1.5F + rank * .25F, GearEnchantmentRules.criticalMultiplier(rank, 0));
            assertEquals(1, GearEnchantmentRules.criticalMultiplier(rank, .05 * rank));
            assertEquals(1, GearEnchantmentRules.criticalMultiplier(rank, Double.NaN));
        }
        assertEquals(1, GearEnchantmentRules.criticalMultiplier(0, 0));
        assertEquals(2.25F, GearEnchantmentRules.criticalMultiplier(1000, .1));
    }
    @Test void healingAndAreaEffectsRemainFiniteAgainstBossHealthAndOutOfRangeLevels() {
        assertEquals(1.8F, GearEnchantmentRules.leechingHealing(3, 20), .00001);
        assertEquals(6, GearEnchantmentRules.leechingHealing(3, Float.MAX_VALUE));
        assertEquals(0, GearEnchantmentRules.leechingHealing(3, Float.POSITIVE_INFINITY));
        assertEquals(0, GearEnchantmentRules.leechingHealing(-1, 100));
        assertEquals(3, GearEnchantmentRules.radianceHealing(999));
        assertEquals(80, GearEnchantmentRules.chainsTicks(999));
        assertEquals(4, GearEnchantmentRules.swirlingDamage(999));
        assertEquals(0, GearEnchantmentRules.chainsTicks(0));
        assertEquals(0, GearEnchantmentRules.swirlingDamage(0));
    }
    @Test void everyServiceChoiceHasExactlyThreePaidUpgradeSteps() {
        assertEquals(6, GearEnchantmentKind.values().length);
        for (var kind : GearEnchantmentKind.values()) {
            assertEquals(8, kind.cost(1)); assertEquals(16, kind.cost(2)); assertEquals(24, kind.cost(3));
            assertThrows(IllegalArgumentException.class, () -> kind.cost(0));
            assertThrows(IllegalArgumentException.class, () -> kind.cost(4));
        }
    }
}
