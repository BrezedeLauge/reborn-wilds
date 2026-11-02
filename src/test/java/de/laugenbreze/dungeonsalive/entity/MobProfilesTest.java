package de.laugenbreze.dungeonsalive.entity;

import org.junit.jupiter.api.Test;
import java.util.HashSet;
import static org.junit.jupiter.api.Assertions.*;

class MobProfilesTest {
    @Test void everyActionHasAVisibleWindupAndFiniteCooldown() {
        for (AttackKind attack : AttackKind.values()) {
            assertTrue(attack.windup() >= 10, attack.name());
            assertTrue(attack.recovery() > 0 && attack.cooldown() >= 15, attack.name());
            assertTrue(attack.range() <= 24 && attack.duration() < 100, attack.name());
        }
    }
    @Test void profileIdsAreUniqueAndBossCatalogIsExplicit() {
        assertTrue(MobProfiles.all().size() >= 54);
        assertEquals(MobProfiles.all().size(), new HashSet<>(MobProfiles.all().stream().map(MobProfile::id).toList()).size());
        assertEquals(12, MobProfiles.all().stream().filter(MobProfile::boss).count());
        assertTrue(MobProfiles.all().stream().filter(p -> p.tier() == MobProfile.Tier.MINIBOSS).count() >= 8);
        for (String id : new String[]{"redstone_monstrosity", "vengeful_heart_of_ender", "arch_illager", "ancient_guardian"})
            assertTrue(MobProfiles.get(id).boss(), "Established boss tier must not be lost: " + id);
        assertThrows(IllegalArgumentException.class, () -> MobProfiles.get("imaginary_boss"));
    }
}
