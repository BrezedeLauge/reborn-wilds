package de.laugenbreze.dungeonsalive.item.gear;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class WeaponFamilyTest {
    @Test void baseFamiliesOfferDifferentCombatTradeoffsWithoutReplacingTheSixSignatureWeapons() {
        assertTrue(WeaponKind.DAGGERS.damage < WeaponKind.SWORD.damage);
        assertTrue(WeaponKind.DAGGERS.attacksPerSecond > WeaponKind.SWORD.attacksPerSecond);
        assertTrue(WeaponKind.HAMMER.damage > WeaponKind.SWORD.damage);
        assertTrue(WeaponKind.HAMMER.attacksPerSecond < WeaponKind.SWORD.attacksPerSecond);
        assertTrue(WeaponKind.CUTLASS.attacksPerSecond > WeaponKind.SWORD.attacksPerSecond);
        assertTrue(WeaponKind.SICKLES.damage < WeaponKind.CUTLASS.damage);
        assertTrue(WeaponKind.SICKLES.attacksPerSecond > WeaponKind.CUTLASS.attacksPerSecond);
        assertEquals(Set.of("heartstealer", "stormlander", "fighters_bindings", "suns_grace", "frost_scythe", "whirlwind"),
                Arrays.stream(WeaponKind.values()).filter(kind -> kind.family == WeaponFamily.SIGNATURE).map(kind -> kind.id).collect(Collectors.toSet()));
        assertEquals(14, Arrays.stream(WeaponKind.values()).map(kind -> kind.id).distinct().count());
    }

    @Test void longWeaponsExchangeSpeedForBoundedReachWhileThrustingWeaponsCannotSweep() {
        for (WeaponKind kind : WeaponKind.values()) {
            assertTrue(kind.damage > 0 && Float.isFinite(kind.attacksPerSecond) && kind.attacksPerSecond > 0 && kind.attacksPerSecond <= 4);
            assertTrue(kind.durability > 0 && kind.family.entityReachBonus >= 0 && kind.family.entityReachBonus <= 1);
            if (kind.family.entityReachBonus > 0) {
                assertTrue(kind.attacksPerSecond < WeaponKind.SWORD.attacksPerSecond);
                assertTrue(kind == WeaponKind.SPEAR || kind == WeaponKind.GLAIVE);
            }
        }
        assertTrue(WeaponKind.GLAIVE.family.sweeping);
        assertFalse(WeaponKind.SPEAR.family.sweeping);
        assertFalse(WeaponKind.DAGGERS.family.sweeping);
        assertFalse(WeaponKind.SOUL_KNIFE.family.sweeping);
        assertTrue(WeaponKind.HAMMER.family.knockbackBonus > 0);
        assertEquals(1, Arrays.stream(WeaponKind.values()).filter(kind -> kind.family.knockbackBonus > 0).count());
    }

    @Test void originalSignatureBalanceStaysIntactAndSoulBonusBelongsOnlyToTheSoulKnife() {
        WeaponKind[] original = {WeaponKind.HEARTSTEALER, WeaponKind.STORMLANDER, WeaponKind.FIGHTERS_BINDINGS,
                WeaponKind.SUNS_GRACE, WeaponKind.FROST_SCYTHE, WeaponKind.WHIRLWIND};
        int[] damage = {9, 10, 5, 7, 8, 9}; float[] speed = {1F, .8F, 3.2F, 1.3F, 1F, 1.1F};
        for (int i = 0; i < original.length; i++) {
            assertEquals(damage[i], original[i].damage); assertEquals(speed[i], original[i].attacksPerSecond);
            assertEquals(1561, original[i].durability); assertTrue(original[i].family.sweeping);
            assertEquals(0, original[i].family.entityReachBonus); assertEquals(0, original[i].family.knockbackBonus);
        }
        assertEquals(Set.of(WeaponKind.SOUL_KNIFE), Arrays.stream(WeaponKind.values()).filter(kind -> kind.family.soulBonus > 0).collect(Collectors.toSet()));
        assertEquals(2, WeaponKind.SOUL_KNIFE.family.soulBonus);
    }
}
