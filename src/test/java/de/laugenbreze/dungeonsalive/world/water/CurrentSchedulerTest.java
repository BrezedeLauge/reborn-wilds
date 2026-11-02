package de.laugenbreze.dungeonsalive.world.water;

import java.util.HashSet;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CurrentSchedulerTest {
    @Test void firstBoatSampleIsUnknownAndFollowingSamplesUseTheirActualTimeDifference() {
        var history = new CurrentMotionHistory();
        assertFalse(history.observe(100, 0, 60, 0).measured());
        var sample = history.observe(105, 1, 60, -.5);
        assertTrue(sample.measured());
        assertEquals(.2, sample.x(), .000001);
        assertEquals(-.1, sample.z(), .000001);
        sample = history.observe(115, 4, 60, -.5);
        assertEquals(.3, sample.x(), .000001);
        assertEquals(0, sample.z());
    }
    @Test void aFastDownstreamPilotedBoatGetsNoAdditionalImpulseDespiteAZeroVanillaServerVelocity() {
        var history = new CurrentMotionHistory();
        history.observe(0, 0, 60, 0);
        var measured = history.observe(5, 2, 60, 0);
        assertTrue(measured.measured());
        assertEquals(0, CurrentStrength.impulse(.08, measured.x(), 5));
        assertTrue(CurrentStrength.impulse(.08, 0, 5) > 0, "Control must expose the original zero-velocity defect");
    }
    @Test void jumpsTeleportsAndLongSamplingGapsResetTheBaselineWithoutInventingAnImpulse() {
        var history = new CurrentMotionHistory();
        history.observe(10, 0, 60, 0);
        assertFalse(history.observe(15, 100, 60, 0).measured());
        assertTrue(history.observe(20, 101, 60, 0).measured());
        assertFalse(history.observe(25, 102, 64, 0).measured());
        assertFalse(history.observe(46, 102, 64, 0).measured());
        assertTrue(history.observe(51, 103, 64, 0).measured());
        assertFalse(history.observe(51, 104, 64, 0).measured());
        assertFalse(history.observe(40, 104, 64, 0).measured());
        assertFalse(history.observe(45, Double.NaN, 64, 0).measured());
        assertFalse(history.observe(50, 105, 64, 0).measured());
    }
    @Test void priorityUsersAreProcessedFirstWhileOrdinaryObjectsKeepAGuaranteedShare() {
        var queue = new CurrentWorkQueue();
        for (int id = 1; id <= 100; id++) queue.offer(id, false);
        for (int id = 1001; id <= 1100; id++) queue.offer(id, true);
        assertEquals(128, queue.queued());
        var selected = queue.drain();
        assertEquals(64, selected.size());
        assertTrue(selected.subList(0, 16).stream().allMatch(id -> id > 1000));
        assertTrue(selected.subList(16, 64).stream().allMatch(id -> id < 1000));
        assertEquals(0, queue.queued());
    }
    @Test void fixedIterationOrderCannotPermanentlyStarveTheTailOfACrowd() {
        var queue = new CurrentWorkQueue();
        var seen = new HashSet<Integer>();
        for (int round = 0; round < 10; round++) {
            for (int id = 1; id <= 500; id++) queue.offer(id, false);
            seen.addAll(queue.drain());
        }
        assertEquals(500, seen.size());
    }
    @Test void controlledCrowdsAlsoRotateAndUnusedNormalCapacityReturnsToPlayers() {
        var queue = new CurrentWorkQueue();
        var seen = new HashSet<Integer>();
        for (int round = 0; round < 5; round++) {
            for (int id = 1; id <= 300; id++) queue.offer(id, true);
            var selection = queue.drain();
            assertEquals(64, selection.size());
            seen.addAll(selection);
        }
        assertEquals(300, seen.size());
    }
    @Test void duplicateAndReclassifiedEntriesNeverConsumeTwoSlotsAndSignedIdWrapIsHandled() {
        var queue = new CurrentWorkQueue();
        queue.offer(5, false); queue.offer(5, false); queue.offer(5, true);
        queue.offer(Integer.MAX_VALUE, false); queue.offer(Integer.MIN_VALUE, false);
        var chosen = queue.drain();
        assertEquals(3, chosen.size());
        assertEquals(3, new HashSet<>(chosen).size());
        assertEquals(5, chosen.getFirst().intValue());
    }
}
