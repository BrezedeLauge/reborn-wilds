package de.laugenbreze.dungeonsalive.world.caravan;

import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CaravanPolicyTest {
    @Test void arrivalReversesTheRouteOnlyOnceAndActuallyWaitsBeforeTheReturnLeg() {
        var route = new CaravanRoute();
        assertEquals(CaravanRoute.Action.TRAVEL, route.decide(100, 6000, false, false, 100));
        assertEquals(CaravanRoute.Action.WAIT, route.decide(120, 6000, false, false, 1));
        assertFalse(route.towardsB());
        assertEquals(320, route.restUntil());
        assertEquals(CaravanRoute.Action.WAIT, route.decide(319, 6000, false, false, 100));
        assertFalse(route.towardsB());
        assertEquals(CaravanRoute.Action.TRAVEL, route.decide(320, 6000, false, false, 100));
        assertEquals(CaravanRoute.Action.WAIT, route.decide(450, 6000, false, false, 1));
        assertTrue(route.towardsB());
    }

    @Test void realDangerOverridesTradeForExactlyTheAlarmLifetimeThenTheInterruptedLegResumes() {
        var route = new CaravanRoute(false, 0, 0);
        route.hurt(100);
        assertEquals(CaravanRoute.Action.FLEE, route.decide(199, 6000, true, true, 100));
        assertEquals(CaravanRoute.Action.TRADE, route.decide(200, 6000, true, false, 100));
        assertFalse(route.towardsB());
        assertEquals(CaravanRoute.Action.TRAVEL, route.decide(201, 6000, false, false, 100));
        assertEquals(200, route.alarmUntil());
    }

    @Test void laterDamageExtendsTheAlarmButCancelledTimeCannotShortenIt() {
        var route = new CaravanRoute();
        route.hurt(100);
        route.hurt(150);
        route.hurt(90);
        assertEquals(250, route.alarmUntil());
        assertEquals(CaravanRoute.Action.FLEE, route.decide(249, -1, false, true, 1));
        assertEquals(CaravanRoute.Action.WAIT, route.decide(250, -1, false, false, 100));
    }

    @Test void daylightCommandsDoNotResetAbsoluteTradeOrPanicTiming() {
        var route = new CaravanRoute();
        assertEquals(CaravanRoute.Action.WAIT, route.decide(10, -1, false, false, 100));
        assertEquals(CaravanRoute.Action.TRAVEL, route.decide(11, 30_000, false, false, 100));
        assertEquals(CaravanRoute.Action.WAIT, route.decide(12, 6000, false, true, 100));
        assertEquals(CaravanRoute.Action.TRADE, route.decide(13, 18_000, true, true, 100));
        route.hurt(14);
        assertEquals(CaravanRoute.Action.FLEE, route.decide(113, 6000, false, false, 100));
        assertEquals(CaravanRoute.Action.TRAVEL, route.decide(114, 6000, false, false, 100));
    }

    @Test void routeStateSurvivesAReconstructionWithoutRestartingItsPausesOrAlarm() {
        var original = new CaravanRoute();
        original.decide(100, 6000, false, false, 1);
        original.hurt(150);
        var restored = new CaravanRoute(original.towardsB(), original.restUntil(), original.alarmUntil());
        for (long now : new long[]{200, 250, 299, 300, 400})
            assertEquals(original.decide(now, 6000, false, false, 100), restored.decide(now, 6000, false, false, 100));
        assertFalse(restored.towardsB());
    }

    @Test void savedReservationsRemainFullWhenEveryMemberIsUnloaded() {
        var ledger = new CaravanLedger();
        UUID one = UUID.randomUUID(), two = UUID.randomUUID();
        Set<UUID> members = Set.of(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID());
        assertTrue(ledger.reserve(one, members));
        assertTrue(ledger.reserve(two, Set.of(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID())));
        var reloaded = new CaravanLedger();
        ledger.snapshot().forEach((id, ids) -> assertTrue(reloaded.reserve(id, ids)));
        assertEquals(2, reloaded.groups());
        assertFalse(reloaded.reserve(UUID.randomUUID(), Set.of(UUID.randomUUID())));
        assertEquals(members, reloaded.members(one));
    }

    @Test void deathOrClaimReleasesOnlyThatMemberAndTheLastMemberReleasesTheGroup() {
        var ledger = new CaravanLedger();
        UUID group = UUID.randomUUID(), trader = UUID.randomUUID(), left = UUID.randomUUID(), right = UUID.randomUUID();
        assertTrue(ledger.reserve(group, Set.of(trader, left, right)));
        assertTrue(ledger.remove(group, trader));
        assertEquals(1, ledger.groups());
        assertTrue(ledger.contains(group, left));
        assertTrue(ledger.remove(group, left));
        assertEquals(1, ledger.groups());
        assertFalse(ledger.remove(group, left));
        assertTrue(ledger.remove(group, right));
        assertEquals(0, ledger.groups());
    }

    @Test void reservationsAreAtomicBoundedUniqueAndExternallyImmutable() {
        var ledger = new CaravanLedger();
        UUID one = UUID.randomUUID(), member = UUID.randomUUID();
        assertTrue(ledger.reserve(one, Set.of(member)));
        assertTrue(ledger.reserve(one, Set.of(member)));
        assertFalse(ledger.reserve(one, Set.of(UUID.randomUUID())));
        assertFalse(ledger.reserve(UUID.randomUUID(), Set.of(member)));
        assertFalse(ledger.reserve(UUID.randomUUID(), Set.of(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID())));
        assertEquals(1, ledger.groups());
        assertThrows(UnsupportedOperationException.class, () -> ledger.members(one).clear());
        assertThrows(UnsupportedOperationException.class, () -> ledger.snapshot().clear());
        assertTrue(ledger.contains(one, member));
    }
}
