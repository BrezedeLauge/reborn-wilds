package de.laugenbreze.dungeonsalive.quest.core;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Deeply immutable persistence boundary. Unknown quest IDs may be retained across temporary
 * datapack removal. Missing/negative saved counts normalize to zero; the journal caps known
 * objectives to their current required count. Minecraft serialization and storage versioning
 * live outside the domain.
 */
public record QuestSnapshot(Set<String> acceptedQuests, Set<String> claimedQuests,
                            Map<String, Map<String, Integer>> objectiveProgress) {
    public QuestSnapshot {
        acceptedQuests = checkedIds(acceptedQuests, "accepted quest ID");
        claimedQuests = checkedIds(claimedQuests, "claimed quest ID");
        Objects.requireNonNull(objectiveProgress, "objectiveProgress");
        var progress = new LinkedHashMap<String, Map<String, Integer>>();
        objectiveProgress.forEach((questId, counts) -> {
            QuestObjective.requireId(questId, "progress quest ID");
            Objects.requireNonNull(counts, "objective counts");
            var copy = new LinkedHashMap<String, Integer>();
            counts.forEach((objectiveId, count) -> {
                QuestObjective.requireId(objectiveId, "progress objective ID");
                copy.put(objectiveId, count == null ? 0 : Math.max(0, count));
            });
            progress.put(questId, Collections.unmodifiableMap(copy));
        });
        objectiveProgress = Collections.unmodifiableMap(progress);
    }

    public static QuestSnapshot empty() {
        return new QuestSnapshot(Set.of(), Set.of(), Map.of());
    }

    private static Set<String> checkedIds(Set<String> ids, String label) {
        Objects.requireNonNull(ids, label);
        for (String id : ids) {
            QuestObjective.requireId(id, label);
        }
        return Set.copyOf(ids);
    }
}
