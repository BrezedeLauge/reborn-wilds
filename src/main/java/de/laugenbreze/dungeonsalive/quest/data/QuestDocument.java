package de.laugenbreze.dungeonsalive.quest.data;

import de.laugenbreze.dungeonsalive.quest.core.QuestDefinition;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/** Immutable quest definition plus presentation and reward data. All text is literal text. */
public record QuestDocument(QuestDefinition definition, String title, String description, String icon,
                            Map<String, String> objectiveLabels, List<Reward> rewards,
                            int experience, int order) {
    public QuestDocument {
        Objects.requireNonNull(definition, "definition");
        Objects.requireNonNull(title, "title");
        Objects.requireNonNull(description, "description");
        Objects.requireNonNull(icon, "icon");
        objectiveLabels = Map.copyOf(objectiveLabels);
        rewards = List.copyOf(rewards);
        if (experience < 0) {
            throw new IllegalArgumentException("Quest experience must not be negative");
        }
    }

    public record Reward(String itemId, int count) {
        public Reward {
            Objects.requireNonNull(itemId, "itemId");
            if (itemId.isBlank() || count < 1 || count > 64) {
                throw new IllegalArgumentException("Reward needs an item ID and a count between 1 and 64");
            }
        }
    }
}
