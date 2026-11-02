package de.laugenbreze.dungeonsalive.entity.peaceful;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PiggyBankTreasureStateTest {
    @Test void identityGivesStableBoundedTreasureAcrossFreshInstancesAndRepeatedPreparation() {
        boolean[] choices = new boolean[4];
        for (int i = 0; i < 1000; i++) {
            UUID identity = new UUID(i * 125267L, i - 500L);
            var first = new PiggyBankTreasureState(); first.prepare(identity);
            var copy = new PiggyBankTreasureState(); copy.prepare(identity);
            assertTrue(first.emeralds() >= 6 && first.emeralds() <= 12);
            assertTrue(first.gearIndex() >= 0 && first.gearIndex() < 4);
            choices[first.gearIndex()] = true;
            assertEquals(first.emeralds(), copy.emeralds()); assertEquals(first.gearIndex(), copy.gearIndex());
            first.prepare(UUID.randomUUID());
            assertEquals(copy.emeralds(), first.emeralds()); assertEquals(copy.gearIndex(), first.gearIndex());
        }
        for (boolean observed : choices) assertTrue(observed, "A configured ordinary gear choice was never reachable");
    }

    @Test void savedPlanAndSpentMarkerSurviveReloadAndCorruptNumbersCannotRaiseTheReward() {
        UUID identity = new UUID(20, 30);
        var original = new PiggyBankTreasureState(); original.prepare(identity);
        assertTrue(original.spend()); assertFalse(original.spend());
        var loaded = new PiggyBankTreasureState();
        loaded.restore(identity, original.emeralds(), original.gearIndex(), original.spent());
        assertEquals(original.emeralds(), loaded.emeralds()); assertEquals(original.gearIndex(), loaded.gearIndex());
        assertTrue(loaded.spent()); assertFalse(loaded.spend());
        loaded.restore(identity, Integer.MAX_VALUE, -1, false);
        assertEquals(original.emeralds(), loaded.emeralds()); assertEquals(original.gearIndex(), loaded.gearIndex());
        assertTrue(loaded.spend()); assertFalse(loaded.spend());
    }

    @Test void authoritativeClaimsRejectPredeathNbtReplayIncludingAfterLedgerReload() {
        var ledger = new PiggyBankClaims(); UUID pig = new UUID(5, 7), next = new UUID(5, 8);
        assertTrue(ledger.claim(pig)); assertFalse(ledger.claim(pig));
        var loaded = new PiggyBankClaims(); loaded.restore(ledger.snapshot());
        assertFalse(loaded.claim(pig)); assertTrue(loaded.claim(next)); assertFalse(loaded.claim(next));
        assertThrows(UnsupportedOperationException.class, () -> loaded.snapshot().add(UUID.randomUUID()));
    }

    @Test void fullOrInvalidLedgerNeverForgetsPreviousClaimsToMakeRoom() {
        var ledger = new PiggyBankClaims(); var ids = new ArrayList<UUID>();
        for (int i = 0; i < PiggyBankClaims.MAX_CLAIMS; i++) ids.add(new UUID(1, i));
        ledger.restore(ids);
        assertFalse(ledger.claim(new UUID(2, 1))); assertFalse(ledger.claim(ids.getFirst()));
        assertEquals(PiggyBankClaims.MAX_CLAIMS, ledger.snapshot().size());
        ids.add(new UUID(2, 2)); ledger.restore(ids);
        assertTrue(ledger.blocked()); assertFalse(ledger.claim(new UUID(2, 3)));
        ledger.restore(List.of()); assertTrue(ledger.claim(new UUID(3, 1)));
    }
}
