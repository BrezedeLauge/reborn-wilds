package de.laugenbreze.dungeonsalive.quest.core;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * One player's quest state, confined to the logical server thread by the adapter.
 * No quest starts automatically. Prerequisites unlock only after explicit reward claim.
 * Kill/discovery objectives remember events after acceptance; collection and world-state
 * objectives reflect current authoritative observations. Call {@link #updateInventory(Map)}
 * and {@link #updateWorldState(Map)} immediately before {@link #claim(String)}.
 *
 * <p>A successful claim changes state exactly once but does not issue rewards. Persist the
 * resulting snapshot and issue the adapter's validated rewards on the server thread. External
 * reward delivery and crash consistency are intentionally outside this domain's boundary.</p>
 */
public final class QuestJournal {
    private final QuestCatalog catalog;
    private final Set<String> accepted;
    private final Set<String> claimed;
    private final Map<String, Map<String, Integer>> counts;

    public QuestJournal(QuestCatalog catalog) {
        this(catalog, QuestSnapshot.empty());
    }

    public QuestJournal(QuestCatalog catalog, QuestSnapshot snapshot) {
        this.catalog = Objects.requireNonNull(catalog, "catalog");
        Objects.requireNonNull(snapshot, "snapshot");
        accepted = new HashSet<>(snapshot.acceptedQuests());
        claimed = new HashSet<>(snapshot.claimedQuests());
        accepted.addAll(claimed);
        counts = new LinkedHashMap<>();
        snapshot.objectiveProgress().forEach((id, progress) -> counts.put(id, new LinkedHashMap<>(progress)));
        for (QuestDefinition quest : catalog.definitions().values()) {
            Map<String, Integer> existing = counts.get(quest.id());
            if (existing == null) {
                continue;
            }
            for (QuestObjective objective : quest.objectives()) {
                int previous = existing.getOrDefault(objective.id(), 0);
                // Saved live observations are not proof of the current world/inventory after reload.
                int restored = (objective.type() == ObjectiveType.COLLECT_ITEM || objective.type() == ObjectiveType.WORLD_STATE) && !claimed.contains(quest.id())
                        ? 0 : Math.min(previous, objective.requiredCount());
                existing.put(objective.id(), restored);
            }
        }
    }

    public QuestStatus status(String questId) {
        QuestDefinition quest = catalog.require(questId);
        if (claimed.contains(questId)) {
            return QuestStatus.CLAIMED;
        }
        if (accepted.contains(questId)) {
            return objectivesComplete(quest) ? QuestStatus.READY : QuestStatus.ACTIVE;
        }
        return claimed.containsAll(quest.prerequisites()) ? QuestStatus.AVAILABLE : QuestStatus.LOCKED;
    }

    /** Returns false for locked or already accepted/claimed quests without changing progress. */
    public boolean accept(String questId) {
        if (status(questId) != QuestStatus.AVAILABLE) {
            return false;
        }
        accepted.add(questId);
        var initial = new LinkedHashMap<String, Integer>();
        for (QuestObjective objective : catalog.require(questId).objectives()) {
            initial.put(objective.id(), 0);
        }
        counts.put(questId, initial);
        return true;
    }

    /** Returns true exactly once when every objective of an accepted quest is complete. */
    public boolean claim(String questId) {
        if (status(questId) != QuestStatus.READY) {
            return false;
        }
        return claimed.add(questId);
    }

    public boolean recordKill(String entityId) {
        return recordKill(entityId, 1);
    }

    /** One event can advance several accepted quests; counters are capped without integer overflow. */
    public boolean recordKill(String entityId, int amount) {
        QuestObjective.requireId(entityId, "entity ID");
        if (amount < 1) {
            throw new IllegalArgumentException("Kill amount must be positive");
        }
        boolean changed = false;
        for (QuestDefinition quest : catalog.definitions().values()) {
            if (!isTracking(quest.id())) {
                continue;
            }
            for (QuestObjective objective : quest.objectives()) {
                if (objective.type() == ObjectiveType.KILL_ENTITY && objective.target().equals(entityId)) {
                    int old = count(quest.id(), objective.id());
                    int next = (int) Math.min((long) objective.requiredCount(), (long) old + amount);
                    changed |= setCount(quest.id(), objective.id(), next);
                }
            }
        }
        return changed;
    }

    /** Discovery is idempotent, so sampling the same biome each tick cannot farm progress. */
    public boolean visitBiome(String biomeId) {
        QuestObjective.requireId(biomeId, "biome ID");
        boolean changed = false;
        for (QuestDefinition quest : catalog.definitions().values()) {
            if (!isTracking(quest.id())) {
                continue;
            }
            for (QuestObjective objective : quest.objectives()) {
                if (objective.type() == ObjectiveType.VISIT_BIOME && objective.target().equals(biomeId)) {
                    changed |= setCount(quest.id(), objective.id(), 1);
                }
            }
        }
        return changed;
    }

    /**
     * Replaces collection progress with current counts. Missing IDs mean zero. This does not
     * accumulate pickups, so dropping/recollecting the same item cannot satisfy a larger goal.
     * Items are only checked; reward claim does not remove them.
     */
    public boolean updateInventory(Map<String, Integer> inventory) {
        Objects.requireNonNull(inventory, "inventory");
        inventory.forEach((id, amount) -> {
            QuestObjective.requireId(id, "item ID");
            if (amount == null || amount < 0) {
                throw new IllegalArgumentException("Inventory counts must not be negative: " + id);
            }
        });
        boolean changed = false;
        for (QuestDefinition quest : catalog.definitions().values()) {
            if (!isTracking(quest.id())) {
                continue;
            }
            for (QuestObjective objective : quest.objectives()) {
                if (objective.type() == ObjectiveType.COLLECT_ITEM) {
                    int next = Math.min(objective.requiredCount(), inventory.getOrDefault(objective.target(), 0));
                    changed |= setCount(quest.id(), objective.id(), next);
                }
            }
        }
        return changed;
    }

    /** Replaces live world conditions; absent keys are false. Nothing is accepted or claimed implicitly. */
    public boolean updateWorldState(Map<String, Boolean> conditions) {
        Objects.requireNonNull(conditions, "conditions");
        conditions.forEach((id, value) -> {
            QuestObjective.requireId(id, "world condition ID");
            Objects.requireNonNull(value, "world condition value");
        });
        boolean changed = false;
        for (QuestDefinition quest : catalog.definitions().values()) {
            if (!isTracking(quest.id())) continue;
            for (QuestObjective objective : quest.objectives()) {
                if (objective.type() == ObjectiveType.WORLD_STATE)
                    changed |= setCount(quest.id(), objective.id(), Boolean.TRUE.equals(conditions.get(objective.target())) ? 1 : 0);
            }
        }
        return changed;
    }

    public Set<String> activeWorldStateTargets() {
        var targets = new HashSet<String>();
        for (QuestDefinition quest : catalog.definitions().values()) {
            if (!isTracking(quest.id())) continue;
            for (QuestObjective objective : quest.objectives())
                if (objective.type() == ObjectiveType.WORLD_STATE) targets.add(objective.target());
        }
        return Set.copyOf(targets);
    }

    /** Current known objective IDs in declaration order, including zeros. */
    public Map<String, Integer> progress(String questId) {
        var result = new LinkedHashMap<String, Integer>();
        for (QuestObjective objective : catalog.require(questId).objectives()) {
            result.put(objective.id(), count(questId, objective.id()));
        }
        return Collections.unmodifiableMap(result);
    }

    public QuestSnapshot snapshot() {
        return new QuestSnapshot(accepted, claimed, counts);
    }

    private boolean isTracking(String questId) {
        return accepted.contains(questId) && !claimed.contains(questId);
    }

    private boolean objectivesComplete(QuestDefinition quest) {
        for (QuestObjective objective : quest.objectives()) {
            if (count(quest.id(), objective.id()) < objective.requiredCount()) {
                return false;
            }
        }
        return true;
    }

    private int count(String questId, String objectiveId) {
        Map<String, Integer> progress = counts.get(questId);
        return progress == null ? 0 : progress.getOrDefault(objectiveId, 0);
    }

    private boolean setCount(String questId, String objectiveId, int next) {
        if (count(questId, objectiveId) == next) {
            return false;
        }
        counts.computeIfAbsent(questId, ignored -> new LinkedHashMap<>()).put(objectiveId, next);
        return true;
    }
}
