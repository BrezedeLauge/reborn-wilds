package de.laugenbreze.dungeonsalive.settlement.inn;

import java.util.UUID;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class InnSavedDataTest {
    @Test void actualNbtRoundTripRetainsRoomOwnerStockAndExclusiveUnloadedGuest() {
        var inn = InnRegistryTest.fixture(0, UUID.randomUUID()); var data = new InnSavedData();
        configure(data, inn); UUID guest = UUID.randomUUID();
        assertTrue(data.reserve(inn.id(), guest, 1, 100));
        assertTrue(data.advance(inn.id(), guest, InnRegistry.GuestPhase.RESERVED, InnRegistry.GuestPhase.TRAVELLING, 101));
        var encoded = data.save(new CompoundTag(), null); var restored = InnSavedData.load(encoded, null);
        assertFalse(restored.blocked()); assertEquals(data.all(), restored.all());
        assertEquals(guest, restored.find(inn.id()).guest().entity());
        assertFalse(restored.reserve(inn.id(), UUID.randomUUID(), 1, 200));
        assertFalse(restored.cancelReservation(inn.id(), guest));
        assertEquals(InnRegistry.Decision.BUSY, restored.remove(inn.id(), inn.owner(), 1));
        assertEquals(encoded, restored.save(new CompoundTag(), null));
    }

    @Test void unknownSchemaAndMalformedGuestRemainVerbatimAndCannotAuthorizeNewGuests() {
        var future = new CompoundTag(); future.putInt("Version", 999); future.putString("Future", "keep this data");
        var blocked = InnSavedData.load(future, null); assertTrue(blocked.blocked());
        assertEquals(future, blocked.save(new CompoundTag(), null));
        assertFalse(blocked.reserve(UUID.randomUUID(), UUID.randomUUID(), 1, 20));
        var inn = InnRegistryTest.fixture(0, UUID.randomUUID()); var data = new InnSavedData(); configure(data, inn);
        var corrupted = data.save(new CompoundTag(), null);
        corrupted.getList("Inns", Tag.TAG_COMPOUND).getCompound(0).putString("Guest", "broken future reservation");
        var restored = InnSavedData.load(corrupted, null);
        assertTrue(restored.blocked()); assertTrue(restored.all().isEmpty());
        assertEquals(corrupted, restored.save(new CompoundTag(), null));
    }

    @Test void duplicateGuestUuidInActualNbtDoesNotDiscardTheSavedReservationHistory() {
        var first = InnRegistryTest.fixture(0, UUID.randomUUID()); var second = InnRegistryTest.fixture(1, UUID.randomUUID());
        var data = new InnSavedData(); configure(data, first); configure(data, second);
        UUID guest = UUID.randomUUID();
        data.reserve(first.id(), guest, 1, 10); data.reserve(second.id(), UUID.randomUUID(), 1, 10);
        var encoded = data.save(new CompoundTag(), null);
        encoded.getList("Inns", Tag.TAG_COMPOUND).getCompound(1).getCompound("Guest").putUUID("Entity", guest);
        var restored = InnSavedData.load(encoded, null);
        assertTrue(restored.blocked()); assertNull(restored.find(first.id()));
        assertEquals(encoded, restored.save(new CompoundTag(), null));
    }

    @Test void emptyInnCanOnlyBeRemovedByItsOwnerAtTheCurrentRevision() {
        var inn = InnRegistryTest.fixture(0, UUID.randomUUID()); var data = new InnSavedData(); configure(data, inn);
        assertEquals(InnRegistry.Decision.NOT_OWNER, data.remove(inn.id(), UUID.randomUUID(), 1));
        assertEquals(InnRegistry.Decision.STALE_REVISION, data.remove(inn.id(), inn.owner(), 0));
        assertEquals(InnRegistry.Decision.OK, data.remove(inn.id(), inn.owner(), 1));
        assertNull(data.find(inn.id()));
        assertTrue(InnSavedData.load(data.save(new CompoundTag(), null), null).all().isEmpty());
    }

    @Test void retiredGuestAndHistoricalArrivalSurviveRealNbtUntilTheKnownExit() {
        var inn = InnRegistryTest.fixture(0, UUID.randomUUID()); var data = new InnSavedData(); configure(data, inn);
        UUID guest = UUID.randomUUID(); data.reserve(inn.id(), guest, 1, 10);
        data.advance(inn.id(), guest, InnRegistry.GuestPhase.RESERVED, InnRegistry.GuestPhase.TRAVELLING, 11);
        data.advance(inn.id(), guest, InnRegistry.GuestPhase.TRAVELLING, InnRegistry.GuestPhase.ARRIVED, 12);
        assertEquals(InnRegistry.Decision.OK, data.retire(inn.id(), inn.owner(), 1, 13));
        var encoded = data.save(new CompoundTag(), null); var restored = InnSavedData.load(encoded, null);
        assertFalse(restored.blocked()); assertTrue(restored.find(inn.id()).retired());
        assertEquals(guest, restored.find(inn.id()).guest().entity());
        assertEquals(InnRegistry.GuestPhase.LEAVING, restored.find(inn.id()).guest().phase());
        assertFalse(restored.reserve(inn.id(), UUID.randomUUID(), 1, 100));
        assertTrue(restored.finishDeparture(inn.id(), guest, 101)); assertNull(restored.find(inn.id()));
        var afterExit = InnSavedData.load(restored.save(new CompoundTag(), null), null);
        assertTrue(afterExit.hadArrival(inn.owner())); assertTrue(afterExit.all().isEmpty());
    }

    private static void configure(InnSavedData data, InnRegistry.Inn inn) {
        assertEquals(InnRegistry.Decision.OK, data.configure(inn.id(), inn.owner(), inn.dimension(), inn.anchor(), inn.bounds(),
                inn.entrance(), inn.stock(), inn.arrival(), 0).decision());
    }
}
