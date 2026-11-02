package de.laugenbreze.dungeonsalive.settlement.inn;

import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class InnRegistryTest {
    @Test void inclusiveRoomBoundsAndArrivalCannotBecomeAnUnboundedOrInternalSpawnSelection() {
        var maximum = new InnBounds(0, 0, 0, 31, 15, 31);
        assertEquals(16_384, maximum.volume()); assertTrue(maximum.contains(31, 15, 31)); assertFalse(maximum.contains(32, 15, 31));
        assertThrows(IllegalArgumentException.class, () -> new InnBounds(0, 0, 0, 32, 15, 31));
        assertThrows(IllegalArgumentException.class, () -> new InnBounds(2, 0, 0, 1, 2, 2));
        assertThrows(IllegalArgumentException.class, () -> new InnBounds(Integer.MIN_VALUE, 0, 0, Integer.MAX_VALUE, 1, 1));
        var inn = fixture(0, UUID.randomUUID());
        assertThrows(IllegalArgumentException.class, () -> new InnRegistry.Inn(inn.id(), inn.owner(), inn.dimension(), inn.anchor(),
                inn.bounds(), inn.entrance(), inn.stock(), new InnBounds.Point(3, 61, 3), 1, null, 0, 0, false));
        assertThrows(IllegalArgumentException.class, () -> new InnRegistry.Inn(inn.id(), inn.owner(), inn.dimension(), inn.anchor(),
                inn.bounds(), inn.entrance(), inn.stock(), new InnBounds.Point(3, 61, -25), 1, null, 0, 0, false));
    }

    @Test void onlyOwnerAndCurrentRevisionCanChangeTheSameAnchor() {
        var registry = new InnRegistry(); var inn = fixture(0, UUID.randomUUID());
        assertEquals(InnRegistry.Decision.OK, configure(registry, inn, 0).decision());
        var impostor = new InnRegistry.Inn(inn.id(), UUID.randomUUID(), inn.dimension(), inn.anchor(), inn.bounds(),
                inn.entrance(), inn.stock(), inn.arrival(), 1, null, 0, 0, false);
        assertEquals(InnRegistry.Decision.NOT_OWNER, configure(registry, impostor, 1).decision());
        assertEquals(InnRegistry.Decision.STALE_REVISION, configure(registry, inn, 0).decision());
        assertEquals(1, registry.get(inn.id()).orElseThrow().revision());
        assertEquals(InnRegistry.Decision.OK, configure(registry, inn, 1).decision());
        assertEquals(2, registry.get(inn.id()).orElseThrow().revision());
        assertEquals(InnRegistry.Decision.NOT_OWNER, registry.remove(inn.id(), impostor.owner(), 2));
        assertEquals(InnRegistry.Decision.STALE_REVISION, registry.remove(inn.id(), inn.owner(), 1));
    }

    @Test void roomAndAssignedStockCannotBelongToTwoInns() {
        var registry = new InnRegistry(); var first = fixture(0, UUID.randomUUID());
        configure(registry, first, 0);
        var sameStock = new InnRegistry.Inn(UUID.randomUUID(), UUID.randomUUID(), first.dimension(),
                new InnBounds.Point(2, 61, 1), first.bounds(), first.entrance(), first.stock(), first.arrival(), 1, null, 0, 0, false);
        assertEquals(InnRegistry.Decision.STOCK_ASSIGNED, configure(registry, sameStock, 0).decision());
        var overlap = new InnRegistry.Inn(UUID.randomUUID(), UUID.randomUUID(), first.dimension(),
                new InnBounds.Point(2, 61, 1), first.bounds(), first.entrance(), new InnBounds.Point(2, 61, 2), first.arrival(), 1, null, 0, 0, false);
        assertEquals(InnRegistry.Decision.OVERLAP, configure(registry, overlap, 0).decision());
        assertEquals(1, registry.all().size());
    }

    @Test void globalOwnerAndPersistentGuestCapsIncludeCurrentlyUnloadedReservations() {
        var ownerLimit = new InnRegistry(); var owner = UUID.randomUUID();
        for (int i = 0; i < InnRegistry.MAX_PER_OWNER; i++) assertEquals(InnRegistry.Decision.OK, configure(ownerLimit, fixture(i, owner), 0).decision());
        assertEquals(InnRegistry.Decision.OWNER_LIMIT, configure(ownerLimit, fixture(9, owner), 0).decision());
        var registry = new InnRegistry();
        for (int i = 0; i < InnRegistry.MAX_INNS; i++) assertEquals(InnRegistry.Decision.OK, configure(registry, fixture(i, UUID.randomUUID()), 0).decision());
        assertEquals(InnRegistry.Decision.WORLD_LIMIT, configure(registry, fixture(90, UUID.randomUUID()), 0).decision());
        var identities = registry.all(); UUID exclusive = UUID.randomUUID();
        assertTrue(registry.reserveGuest(identities.getFirst().id(), exclusive, 1, 10));
        assertFalse(registry.reserveGuest(identities.get(1).id(), exclusive, 1, 10));
        for (int i = 1; i < InnRegistry.MAX_GUESTS; i++) assertTrue(registry.reserveGuest(identities.get(i).id(), UUID.randomUUID(), 1, 10));
        var reloaded = new InnRegistry(); assertTrue(reloaded.restore(registry.all(), registry.arrivalOwners()));
        assertFalse(reloaded.reserveGuest(identities.get(8).id(), UUID.randomUUID(), 1, 50));
        assertEquals(InnRegistry.Decision.BUSY, configure(reloaded, identities.getFirst(), 1).decision());
    }

    @Test void actualGuestLifecycleCountsArrivalOnceAndRetainsDepartureCooldownAcrossReload() {
        var registry = new InnRegistry(); var inn = fixture(0, UUID.randomUUID()); configure(registry, inn, 0);
        UUID guest = UUID.randomUUID(); assertTrue(registry.reserveGuest(inn.id(), guest, 1, 10));
        assertFalse(registry.advanceGuest(inn.id(), guest, InnRegistry.GuestPhase.RESERVED, InnRegistry.GuestPhase.ARRIVED, 11));
        assertTrue(registry.advanceGuest(inn.id(), guest, InnRegistry.GuestPhase.RESERVED, InnRegistry.GuestPhase.TRAVELLING, 11));
        assertFalse(registry.cancelReservation(inn.id(), guest), "An unloaded travelling guest must not become a free slot");
        assertFalse(registry.finishDeparture(inn.id(), guest, 12));
        assertTrue(registry.advanceGuest(inn.id(), guest, InnRegistry.GuestPhase.TRAVELLING, InnRegistry.GuestPhase.ARRIVED, 12));
        assertFalse(registry.advanceGuest(inn.id(), guest, InnRegistry.GuestPhase.TRAVELLING, InnRegistry.GuestPhase.ARRIVED, 13));
        assertTrue(registry.advanceGuest(inn.id(), guest, InnRegistry.GuestPhase.ARRIVED, InnRegistry.GuestPhase.RESTING, 13));
        assertTrue(registry.advanceGuest(inn.id(), guest, InnRegistry.GuestPhase.RESTING, InnRegistry.GuestPhase.LEAVING, 14));
        assertFalse(registry.finishDeparture(inn.id(), UUID.randomUUID(), 15));
        assertTrue(registry.finishDeparture(inn.id(), guest, 15));
        var restored = new InnRegistry(); assertTrue(restored.restore(registry.all(), registry.arrivalOwners()));
        assertEquals(1, restored.get(inn.id()).orElseThrow().arrivals());
        assertFalse(restored.reserveGuest(inn.id(), UUID.randomUUID(), 1, 16));
        assertTrue(restored.reserveGuest(inn.id(), UUID.randomUUID(), 1, 15 + InnRegistry.VISIT_COOLDOWN));
    }

    @Test void duplicateGuestHistoryIsRejectedAsAWholeInsteadOfEnablingReplacementSpawns() {
        var one = fixture(0, UUID.randomUUID()); var two = fixture(1, UUID.randomUUID());
        var guest = new InnRegistry.Guest(UUID.randomUUID(), 1, InnRegistry.GuestPhase.TRAVELLING, 10);
        var first = one.withGuest(guest, 0); var second = two.withGuest(guest, 0);
        var registry = new InnRegistry();
        assertFalse(registry.restore(List.of(first, second), List.of())); assertTrue(registry.blocked()); assertTrue(registry.all().isEmpty());
        assertEquals(InnRegistry.Decision.BLOCKED, configure(registry, fixture(3, UUID.randomUUID()), 0).decision());
    }

    @Test void foodPolicyAcceptsOrdinaryMealsWithoutConsumingValuablesHazardsOrContainerItems() {
        assertTrue(InnFoodPolicy.accepts("minecraft:bread")); assertTrue(InnFoodPolicy.accepts("minecraft:cooked_cod"));
        for (String item : List.of("minecraft:enchanted_golden_apple", "minecraft:golden_carrot", "minecraft:rotten_flesh",
                "minecraft:pufferfish", "minecraft:potion", "minecraft:mushroom_stew", "minecraft:diamond", "dungeonsalive:soul_healer"))
            assertFalse(InnFoodPolicy.accepts(item), item);
    }

    @Test void removedAnchorRetainsItsUnloadedGuestUntilDepartureWithoutBlockingReuseOfTheRoom() {
        var registry = new InnRegistry(); var inn = fixture(0, UUID.randomUUID()); configure(registry, inn, 0);
        UUID guest = UUID.randomUUID(); registry.reserveGuest(inn.id(), guest, 1, 10);
        registry.advanceGuest(inn.id(), guest, InnRegistry.GuestPhase.RESERVED, InnRegistry.GuestPhase.TRAVELLING, 11);
        registry.advanceGuest(inn.id(), guest, InnRegistry.GuestPhase.TRAVELLING, InnRegistry.GuestPhase.ARRIVED, 12);
        assertEquals(InnRegistry.Decision.NOT_OWNER, registry.retire(inn.id(), UUID.randomUUID(), 1, 13));
        assertEquals(InnRegistry.Decision.OK, registry.retire(inn.id(), inn.owner(), 1, 13));
        var old = registry.get(inn.id()).orElseThrow();
        assertTrue(old.retired()); assertEquals(guest, old.guest().entity()); assertEquals(InnRegistry.GuestPhase.LEAVING, old.guest().phase());
        assertFalse(registry.reserveGuest(inn.id(), UUID.randomUUID(), 1, 14));
        var newRoom = fixture(0, inn.owner()); assertEquals(InnRegistry.Decision.OK, configure(registry, newRoom, 0).decision());
        var restored = new InnRegistry(); assertTrue(restored.restore(registry.all().reversed(), registry.arrivalOwners()));
        assertTrue(restored.get(inn.id()).orElseThrow().retired(), "Reload must not release the absent old guest");
        assertTrue(restored.finishDeparture(inn.id(), guest, 20)); assertTrue(restored.get(inn.id()).isEmpty());
        assertTrue(restored.hadArrival(inn.owner()), "Historical real arrival survives removal of the retired room");
        assertTrue(restored.get(newRoom.id()).isPresent());
    }

    @Test void historicalArrivalCapRejectsNewOwnersWithoutForgettingExistingEvidence() {
        var history = java.util.stream.IntStream.range(0, InnRegistry.MAX_ARRIVAL_OWNERS)
                .mapToObj(i -> new UUID(0, i)).toList();
        var newcomer = fixture(0, UUID.randomUUID()); var registry = new InnRegistry();
        assertTrue(registry.restore(List.of(newcomer), history));
        assertFalse(registry.reserveGuest(newcomer.id(), UUID.randomUUID(), 1, 100));
        assertTrue(registry.hadArrival(history.getFirst()));
        var returningOwner = fixture(1, history.getFirst()); configure(registry, returningOwner, 0);
        assertTrue(registry.reserveGuest(returningOwner.id(), UUID.randomUUID(), 1, 100));
        var oversized = new java.util.ArrayList<>(history); oversized.add(UUID.randomUUID());
        assertFalse(new InnRegistry().restore(List.of(), oversized));
    }

    static InnRegistry.Inn fixture(int index, UUID owner) {
        int x = index * 40;
        return new InnRegistry.Inn(UUID.randomUUID(), owner, "minecraft:overworld", new InnBounds.Point(x + 1, 61, 1),
                new InnBounds(x, 60, 0, x + 7, 65, 7), new InnBounds.Point(x + 3, 61, 0),
                new InnBounds.Point(x + 1, 61, 2), new InnBounds.Point(x + 3, 61, -8), 1, null, 0, 0, false);
    }
    static InnRegistry.Change configure(InnRegistry registry, InnRegistry.Inn inn, long revision) {
        return registry.configure(inn.id(), inn.owner(), inn.dimension(), inn.anchor(), inn.bounds(), inn.entrance(), inn.stock(), inn.arrival(), revision);
    }
}
