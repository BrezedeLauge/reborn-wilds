package de.laugenbreze.dungeonsalive.quest.core;

import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class WorldStateQuestTest {
    private static final String INN = "dungeonsalive:inn_ready", ROUTE = "dungeonsalive:inn_connected";
    private static QuestDefinition quest(String id, List<String> prerequisites, String condition) {
        return new QuestDefinition(id, prerequisites, List.of(new QuestObjective("condition", ObjectiveType.WORLD_STATE, condition, 1)));
    }
    @Test void existingWorldIsRecognizedOnlyAfterDeliberateAcceptanceAndNeverAutoClaims() {
        var q = quest("test:inn", List.of(), INN); var journal = new QuestJournal(new QuestCatalog(List.of(q)));
        assertTrue(journal.activeWorldStateTargets().isEmpty());
        assertFalse(journal.updateWorldState(Map.of(INN, true)));
        assertEquals(QuestStatus.AVAILABLE, journal.status(q.id()));
        assertTrue(journal.accept(q.id()));
        assertEquals(Set.of(INN), journal.activeWorldStateTargets());
        assertEquals(QuestStatus.ACTIVE, journal.status(q.id()));
        assertTrue(journal.updateWorldState(Map.of(INN, true)));
        assertEquals(QuestStatus.READY, journal.status(q.id()));
        assertTrue(journal.snapshot().claimedQuests().isEmpty());
    }
    @Test void brokenBridgeRevokesReadinessAndAClaimFreezesExactlyOnce() {
        var q = quest("test:route", List.of(), ROUTE); var journal = new QuestJournal(new QuestCatalog(List.of(q)));
        journal.accept(q.id()); journal.updateWorldState(Map.of(ROUTE, true));
        journal.updateWorldState(Map.of(ROUTE, false));
        assertEquals(QuestStatus.ACTIVE, journal.status(q.id())); assertFalse(journal.claim(q.id()));
        journal.updateWorldState(Map.of(ROUTE, true)); assertTrue(journal.claim(q.id()));
        assertFalse(journal.claim(q.id())); assertFalse(journal.updateWorldState(Map.of()));
        assertEquals(QuestStatus.CLAIMED, journal.status(q.id())); assertTrue(journal.activeWorldStateTargets().isEmpty());
    }
    @Test void reloadRequiresFreshWorldEvidenceButPreservesClaimAndDoesNotStartNextQuest() {
        var first = quest("test:first", List.of(), INN); var next = quest("test:next", List.of(first.id()), ROUTE);
        var catalog = new QuestCatalog(List.of(first, next)); var journal = new QuestJournal(catalog);
        journal.accept(first.id()); journal.updateWorldState(Map.of(INN, true));
        var restored = new QuestJournal(catalog, journal.snapshot());
        assertEquals(QuestStatus.ACTIVE, restored.status(first.id())); assertFalse(restored.claim(first.id()));
        restored.updateWorldState(Map.of(INN, true)); assertTrue(restored.claim(first.id()));
        restored = new QuestJournal(catalog, restored.snapshot());
        assertEquals(QuestStatus.CLAIMED, restored.status(first.id())); assertFalse(restored.claim(first.id()));
        assertEquals(QuestStatus.AVAILABLE, restored.status(next.id())); assertTrue(restored.activeWorldStateTargets().isEmpty());
    }
    @Test void missingConditionsResetPendingStateAndDoNotAlterOtherObjectiveKindsOrPlayers() {
        var q = new QuestDefinition("test:mixed", List.of(), List.of(new QuestObjective("world", ObjectiveType.WORLD_STATE, INN, 1),
                new QuestObjective("kill", ObjectiveType.KILL_ENTITY, "minecraft:zombie", 2)));
        var catalog = new QuestCatalog(List.of(q)); var alice = new QuestJournal(catalog); var bob = new QuestJournal(catalog);
        alice.accept(q.id()); bob.accept(q.id()); alice.recordKill("minecraft:zombie"); alice.updateWorldState(Map.of(INN, true));
        assertEquals(0, bob.progress(q.id()).get("world"));
        alice.updateWorldState(Map.of()); assertEquals(Map.of("world", 0, "kill", 1), alice.progress(q.id()));
        assertThrows(IllegalArgumentException.class, () -> new QuestObjective("bad", ObjectiveType.WORLD_STATE, INN, 2));
    }
}
