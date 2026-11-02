package de.laugenbreze.dungeonsalive.quest.core;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;

/** Immutable gameplay definition. Presentation text and rewards are owned by the adapter. */
public record QuestDefinition(String id, List<String> prerequisites, List<QuestObjective> objectives) {
    public QuestDefinition {
        QuestObjective.requireId(id, "quest ID");
        prerequisites = List.copyOf(Objects.requireNonNull(prerequisites, "prerequisites"));
        objectives = List.copyOf(Objects.requireNonNull(objectives, "objectives"));
        if (objectives.isEmpty()) {
            throw new IllegalArgumentException("Quest must have at least one objective: " + id);
        }
        var prerequisiteIds = new HashSet<String>();
        for (String prerequisite : prerequisites) {
            QuestObjective.requireId(prerequisite, "prerequisite ID");
            if (prerequisite.equals(id)) {
                throw new IllegalArgumentException("Quest cannot depend on itself: " + id);
            }
            if (!prerequisiteIds.add(prerequisite)) {
                throw new IllegalArgumentException("Duplicate prerequisite " + prerequisite + " in " + id);
            }
        }
        var objectiveIds = new HashSet<String>();
        for (QuestObjective objective : objectives) {
            if (!objectiveIds.add(objective.id())) {
                throw new IllegalArgumentException("Duplicate objective ID " + objective.id() + " in " + id);
            }
        }
    }
}
