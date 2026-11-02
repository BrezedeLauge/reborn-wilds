package de.laugenbreze.dungeonsalive.quest.core;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class QuestJournalTest {
    private static QuestObjective kill(String id, int amount) {
        return new QuestObjective(id, ObjectiveType.KILL_ENTITY, "minecraft:zombie", amount);
    }

    private static QuestDefinition quest(String id, List<String> prerequisites, QuestObjective... objectives) {
        return new QuestDefinition(id, prerequisites, List.of(objectives));
    }

    @Test
    void onlyAcceptedQuestsReceiveEventsAndEveryAcceptedMatchingQuestCanProgress() {
        var first = quest("test:first", List.of(), kill("kills", 3));
        var second = quest("test:second", List.of(), kill("kills", 2));
        var third = quest("test:third", List.of(), kill("kills", 1));
        var journal = new QuestJournal(new QuestCatalog(List.of(first, second, third)));
        assertFalse(journal.recordKill("minecraft:zombie"));
        assertTrue(journal.accept(first.id()));
        assertTrue(journal.accept(second.id()));
        assertTrue(journal.recordKill("minecraft:zombie"));
        assertEquals(1, journal.progress(first.id()).get("kills"));
        assertEquals(1, journal.progress(second.id()).get("kills"));
        assertEquals(0, journal.progress(third.id()).get("kills"));
        assertEquals(QuestStatus.AVAILABLE, journal.status(third.id()));
        assertFalse(journal.recordKill("minecraft:skeleton"));
    }

    @Test
    void prerequisiteMustBeClaimedAndSuccessorNeverAutoStarts() {
        var first = quest("test:first", List.of(), kill("kills", 1));
        var next = quest("test:next", List.of(first.id()), kill("kills", 1));
        var journal = new QuestJournal(new QuestCatalog(List.of(first, next)));
        assertEquals(QuestStatus.LOCKED, journal.status(next.id()));
        assertFalse(journal.accept(next.id()));
        assertTrue(journal.accept(first.id()));
        journal.recordKill("minecraft:zombie");
        assertEquals(QuestStatus.READY, journal.status(first.id()));
        assertEquals(QuestStatus.LOCKED, journal.status(next.id()));
        assertTrue(journal.claim(first.id()));
        assertEquals(QuestStatus.AVAILABLE, journal.status(next.id()));
        assertFalse(journal.recordKill("minecraft:zombie"));
        assertEquals(0, journal.progress(next.id()).get("kills"));
        assertTrue(journal.accept(next.id()));
    }

    @Test
    void allObjectivesMustBeCompleteAndClaimHappensExactlyOnce() {
        var definition = quest("test:mixed", List.of(), kill("kills", 2),
                new QuestObjective("forest", ObjectiveType.VISIT_BIOME, "minecraft:forest", 1));
        var journal = new QuestJournal(new QuestCatalog(List.of(definition)));
        assertFalse(journal.claim(definition.id()));
        journal.accept(definition.id());
        journal.recordKill("minecraft:zombie", 2);
        assertEquals(QuestStatus.ACTIVE, journal.status(definition.id()));
        assertFalse(journal.claim(definition.id()));
        assertTrue(journal.visitBiome("minecraft:forest"));
        assertFalse(journal.visitBiome("minecraft:forest"));
        assertEquals(QuestStatus.READY, journal.status(definition.id()));
        assertTrue(journal.claim(definition.id()));
        assertFalse(journal.claim(definition.id()));
        assertFalse(journal.accept(definition.id()));
        assertEquals(QuestStatus.CLAIMED, journal.status(definition.id()));
        assertFalse(journal.recordKill("minecraft:zombie"));
    }

    @Test
    void collectionIsInventorySnapshotAndCanLoseReadinessBeforeClaim() {
        var definition = quest("test:collect", List.of(),
                new QuestObjective("berries", ObjectiveType.COLLECT_ITEM, "minecraft:sweet_berries", 8));
        var journal = new QuestJournal(new QuestCatalog(List.of(definition)));
        journal.accept(definition.id());
        assertTrue(journal.updateInventory(Map.of("minecraft:sweet_berries", 4)));
        assertFalse(journal.updateInventory(Map.of("minecraft:sweet_berries", 4)));
        assertEquals(4, journal.progress(definition.id()).get("berries"));
        journal.updateInventory(Map.of());
        journal.updateInventory(Map.of("minecraft:sweet_berries", 4));
        assertEquals(QuestStatus.ACTIVE, journal.status(definition.id()));
        journal.updateInventory(Map.of("minecraft:sweet_berries", 64));
        assertEquals(8, journal.progress(definition.id()).get("berries"));
        assertEquals(QuestStatus.READY, journal.status(definition.id()));
        journal.updateInventory(Map.of("minecraft:sweet_berries", 7));
        assertFalse(journal.claim(definition.id()));
        assertEquals(QuestStatus.ACTIVE, journal.status(definition.id()));
        journal.updateInventory(Map.of("minecraft:sweet_berries", 8));
        assertTrue(journal.claim(definition.id()));
        assertFalse(journal.updateInventory(Map.of()));
        assertEquals(QuestStatus.CLAIMED, journal.status(definition.id()));
    }

    @Test
    void persistenceKeepsClaimGuardAndResetsUnverifiedCollectionSnapshots() {
        var done = quest("test:done", List.of(), kill("kills", 1));
        var pending = quest("test:pending", List.of(done.id()), kill("kills", 3),
                new QuestObjective("apple", ObjectiveType.COLLECT_ITEM, "minecraft:apple", 2));
        var catalog = new QuestCatalog(List.of(done, pending));
        var journal = new QuestJournal(catalog);
        journal.accept(done.id());
        journal.recordKill("minecraft:zombie");
        journal.claim(done.id());
        journal.accept(pending.id());
        journal.recordKill("minecraft:zombie", 2);
        journal.updateInventory(Map.of("minecraft:apple", 2));
        var restored = new QuestJournal(catalog, journal.snapshot());
        assertFalse(restored.claim(done.id()));
        assertEquals(QuestStatus.CLAIMED, restored.status(done.id()));
        assertEquals(2, restored.progress(pending.id()).get("kills"));
        assertEquals(0, restored.progress(pending.id()).get("apple"));
        restored.recordKill("minecraft:zombie");
        assertFalse(restored.claim(pending.id()));
        restored.updateInventory(Map.of("minecraft:apple", 2));
        assertTrue(restored.claim(pending.id()));
    }

    @Test
    void snapshotsAndDefinitionsAreDefensivelyCopied() {
        var accepted = new HashSet<>(Set.of("test:q"));
        var counts = new HashMap<>(Map.of("kills", 1));
        var snapshot = new QuestSnapshot(accepted, Set.of(), Map.of("test:q", counts));
        accepted.clear();
        counts.put("kills", 42);
        assertEquals(Set.of("test:q"), snapshot.acceptedQuests());
        assertEquals(1, snapshot.objectiveProgress().get("test:q").get("kills"));
        assertThrows(UnsupportedOperationException.class,
                () -> snapshot.objectiveProgress().get("test:q").put("kills", 99));
        var objectives = new ArrayList<>(List.of(kill("kills", 3)));
        var definition = new QuestDefinition("test:q", List.of(), objectives);
        objectives.clear();
        assertEquals(1, definition.objectives().size());
    }

    @Test
    void snapshotPreservesTemporarilyMissingQuestsAndUsesObjectiveIdsAfterReorder() {
        var original = quest("test:q", List.of(), kill("first", 4), kill("second", 7));
        var journal = new QuestJournal(new QuestCatalog(List.of(original)));
        journal.accept(original.id());
        journal.recordKill("minecraft:zombie", 3);
        var missing = new QuestJournal(new QuestCatalog(List.of()), journal.snapshot());
        assertTrue(missing.snapshot().acceptedQuests().contains(original.id()));
        var reordered = quest("test:q", List.of(), kill("second", 7), kill("first", 2));
        var restored = new QuestJournal(new QuestCatalog(List.of(reordered)), missing.snapshot());
        assertEquals(3, restored.progress(original.id()).get("second"));
        assertEquals(2, restored.progress(original.id()).get("first"));
    }

    @Test
    void repeatedAcceptCannotResetProgressAndLargeKillAmountCannotOverflow() {
        var definition = quest("test:q", List.of(), kill("kills", Integer.MAX_VALUE));
        var journal = new QuestJournal(new QuestCatalog(List.of(definition)));
        journal.accept(definition.id());
        journal.recordKill("minecraft:zombie", 2);
        assertFalse(journal.accept(definition.id()));
        assertEquals(2, journal.progress(definition.id()).get("kills"));
        journal.recordKill("minecraft:zombie", Integer.MAX_VALUE);
        assertEquals(Integer.MAX_VALUE, journal.progress(definition.id()).get("kills"));
        assertEquals(QuestStatus.READY, journal.status(definition.id()));
    }

    @Test
    void invalidGraphAndDefinitionsAreRejectedBeforeBecomingAnActiveCatalog() {
        assertThrows(IllegalArgumentException.class, () -> quest("test:empty", List.of()));
        assertThrows(IllegalArgumentException.class, () -> kill("zero", 0));
        assertThrows(IllegalArgumentException.class, () ->
                new QuestObjective("visit", ObjectiveType.VISIT_BIOME, "minecraft:forest", 2));
        assertThrows(IllegalArgumentException.class, () ->
                quest("test:duplicate", List.of(), kill("same", 1), kill("same", 2)));
        var a = quest("test:a", List.of("test:b"), kill("kills", 1));
        var b = quest("test:b", List.of("test:a"), kill("kills", 1));
        assertThrows(IllegalArgumentException.class, () -> new QuestCatalog(List.of(a)));
        assertThrows(IllegalArgumentException.class, () -> new QuestCatalog(List.of(a, b)));
        var valid = quest("test:q", List.of(), kill("kills", 1));
        assertThrows(IllegalArgumentException.class, () -> new QuestCatalog(List.of(valid, valid)));
    }

    @Test
    void invalidEventsAndUnknownQuestOperationsCannotChangeState() {
        var definition = quest("test:q", List.of(), kill("kills", 2));
        var journal = new QuestJournal(new QuestCatalog(List.of(definition)));
        journal.accept(definition.id());
        var before = journal.snapshot();
        assertThrows(IllegalArgumentException.class, () -> journal.recordKill("minecraft:zombie", -1));
        assertThrows(IllegalArgumentException.class, () -> journal.updateInventory(Map.of("minecraft:apple", -2)));
        assertThrows(IllegalArgumentException.class, () -> journal.accept("test:unknown"));
        assertThrows(IllegalArgumentException.class, () -> journal.claim("test:unknown"));
        assertThrows(IllegalArgumentException.class, () -> journal.visitBiome(" "));
        assertEquals(before, journal.snapshot());
    }

    @Test
    void differentPlayersNeverShareProgressThroughTheCatalog() {
        var definition = quest("test:q", List.of(), kill("kills", 1));
        var catalog = new QuestCatalog(List.of(definition));
        var alice = new QuestJournal(catalog);
        var bob = new QuestJournal(catalog);
        alice.accept(definition.id());
        bob.accept(definition.id());
        alice.recordKill("minecraft:zombie");
        alice.claim(definition.id());
        assertEquals(QuestStatus.CLAIMED, alice.status(definition.id()));
        assertEquals(QuestStatus.ACTIVE, bob.status(definition.id()));
        assertEquals(0, bob.progress(definition.id()).get("kills"));
    }

    @Test
    void corruptSavedCountsAreClampedAndReloadNeverClaimsReadyQuests() {
        var definition = quest("test:q", List.of(), kill("negative", 3), kill("tooLarge", 5));
        var snapshot = new QuestSnapshot(Set.of(definition.id()), Set.of(),
                Map.of(definition.id(), Map.of("negative", -10, "tooLarge", Integer.MAX_VALUE)));
        var journal = new QuestJournal(new QuestCatalog(List.of(definition)), snapshot);
        assertEquals(0, journal.progress(definition.id()).get("negative"));
        assertEquals(5, journal.progress(definition.id()).get("tooLarge"));
        assertEquals(QuestStatus.ACTIVE, journal.status(definition.id()));
        journal.recordKill("minecraft:zombie", 3);
        var restored = new QuestJournal(new QuestCatalog(List.of(definition)), journal.snapshot());
        assertEquals(QuestStatus.READY, restored.status(definition.id()));
        assertTrue(restored.snapshot().claimedQuests().isEmpty());
        assertTrue(restored.claim(definition.id()));
    }
}
