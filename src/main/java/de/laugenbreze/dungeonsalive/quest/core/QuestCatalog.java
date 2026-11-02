package de.laugenbreze.dungeonsalive.quest.core;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * An immutable, internally consistent set of quests. A reload should validate a new catalog
 * before replacing the old one, then restore each journal from its snapshot.
 */
public final class QuestCatalog {
    private final Map<String, QuestDefinition> definitions;

    public QuestCatalog(Collection<QuestDefinition> quests) {
        Objects.requireNonNull(quests, "quests");
        var byId = new LinkedHashMap<String, QuestDefinition>();
        for (QuestDefinition quest : quests) {
            Objects.requireNonNull(quest, "quest");
            if (byId.putIfAbsent(quest.id(), quest) != null) {
                throw new IllegalArgumentException("Duplicate quest ID: " + quest.id());
            }
        }
        for (QuestDefinition quest : byId.values()) {
            for (String prerequisite : quest.prerequisites()) {
                if (!byId.containsKey(prerequisite)) {
                    throw new IllegalArgumentException("Unknown prerequisite " + prerequisite + " in " + quest.id());
                }
            }
        }
        var visited = new HashSet<String>();
        var visiting = new HashSet<String>();
        for (String id : byId.keySet()) {
            validateAcyclic(id, byId, visited, visiting);
        }
        definitions = Collections.unmodifiableMap(byId);
    }

    public Map<String, QuestDefinition> definitions() {
        return definitions;
    }

    /** Definitions in catalog insertion order, suitable for an adapter's journal view. */
    public Collection<QuestDefinition> quests() {
        return definitions.values();
    }

    public QuestDefinition require(String id) {
        QuestDefinition definition = definitions.get(id);
        if (definition == null) {
            throw new IllegalArgumentException("Unknown quest: " + id);
        }
        return definition;
    }

    private static void validateAcyclic(String id, Map<String, QuestDefinition> byId,
                                        Set<String> visited, Set<String> visiting) {
        if (visited.contains(id)) {
            return;
        }
        if (!visiting.add(id)) {
            throw new IllegalArgumentException("Cyclic quest prerequisites involving: " + id);
        }
        for (String prerequisite : byId.get(id).prerequisites()) {
            validateAcyclic(prerequisite, byId, visited, visiting);
        }
        visiting.remove(id);
        visited.add(id);
    }
}
