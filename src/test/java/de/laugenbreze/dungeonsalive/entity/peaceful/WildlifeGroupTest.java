package de.laugenbreze.dungeonsalive.entity.peaceful;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class WildlifeGroupTest {
    private static UUID id(long value) { return new UUID(0, value); }

    private static WildlifeGroup group(long groupId, long... members) {
        var group = new WildlifeGroup(id(groupId));
        for (long member : members) assertTrue(group.tryJoin(id(member)));
        return group;
    }

    @Test
    void unloadingMembersChangesLocalLeadershipButDoesNotCreateNewPopulationSlots() {
        var group = group(100, 1, 2, 3, 4, 5, 6);
        assertEquals(id(1), group.leader(group.members()));
        assertEquals(id(4), group.leader(List.of(id(5), id(4))),
                "The next present member leads without resolving the absent leader");
        assertFalse(group.tryJoin(id(7)), "Chunk unloading does not free a reserved slot");
        assertEquals(6, group.size());
        assertTrue(group.tryJoin(id(1)), "The returning original member is still reserved");
        assertEquals(id(1), group.leader(List.of(id(4), id(1))));
        assertEquals(id(100), group.id());
    }

    @Test
    void leadershipUsesOnlyKnownLivingLocalMembersAndHasNoMissingLeaderDependency() {
        var group = group(100, 4, 2, 3);
        assertNull(group.leader(List.of()));
        assertNull(group.leader(List.of(id(1), id(99))));
        assertEquals(id(3), group.leader(List.of(id(99), id(4), id(3), id(3))));
        assertEquals(id(2), group.leader(List.of(id(4), id(2))));
        assertEquals(List.of(id(2), id(3), id(4)), group.members(),
                "Selecting a temporary local leader must not rewrite persistent membership");
    }

    @Test
    void independentLoadOrderProducesTheSameSlotsAndSnapshotsCannotMutateMembership() {
        var first = group(100, 6, 2, 4);
        var second = group(100, 4, 6, 2);
        assertEquals(first.members(), second.members());
        for (long member : new long[]{2, 4, 6}) assertEquals(first.slot(id(member)), second.slot(id(member)));
        assertEquals(-1, first.slot(id(99)));
        var snapshot = first.members();
        assertThrows(UnsupportedOperationException.class, () -> snapshot.add(id(99)));
        assertTrue(first.tryJoin(id(5)));
        assertEquals(List.of(id(2), id(4), id(6)), snapshot,
                "A previously saved snapshot is not a live view into later group changes");
        assertEquals(List.of(id(2), id(4), id(5), id(6)), first.members());
    }

    @Test
    void mergingPartialCopiesPreservesReservationsAndIsIdempotentAtCapacity() {
        var first = group(100, 1, 2, 3, 4);
        var returningCopy = group(100, 3, 4, 5, 6);
        var beforeOther = returningCopy.members();
        assertTrue(first.merge(returningCopy));
        assertEquals(List.of(id(1), id(2), id(3), id(4), id(5), id(6)), first.members());
        assertEquals(beforeOther, returningCopy.members());
        assertTrue(first.merge(returningCopy));
        assertEquals(6, first.size());
        assertFalse(first.tryJoin(id(7)));
    }

    @Test
    void foreignIdentityAndOversizedUnionsAreRejectedWithoutPartialMembershipChanges() {
        var first = group(100, 1, 2, 3, 4);
        var foreign = group(200, 5);
        var oversized = group(100, 3, 4, 5, 6, 7);
        var firstBefore = first.members();
        var otherBefore = oversized.members();
        assertFalse(first.merge(foreign));
        assertEquals(firstBefore, first.members());
        assertFalse(first.merge(oversized));
        assertEquals(firstBefore, first.members());
        assertEquals(otherBefore, oversized.members());
    }
}
