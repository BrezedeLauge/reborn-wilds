package de.laugenbreze.dungeonsalive.quest.core;

import java.util.Objects;

/**
 * An objective has a stable ID within its quest so saved counts survive objective reordering.
 * Registry ID syntax belongs to the Minecraft adapter; the domain only requires nonblank IDs.
 * A biome objective records discovery, not the number of ticks spent inside the biome.
 */
public record QuestObjective(String id, ObjectiveType type, String target, int requiredCount) {
    public QuestObjective {
        requireId(id, "objective ID");
        Objects.requireNonNull(type, "type");
        requireId(target, "objective target");
        if (requiredCount < 1) {
            throw new IllegalArgumentException("Objective requiredCount must be at least 1: " + id);
        }
        if ((type == ObjectiveType.VISIT_BIOME || type == ObjectiveType.WORLD_STATE) && requiredCount != 1) {
            throw new IllegalArgumentException("Discovery/world-state objectives require count 1: " + id);
        }
    }

    static String requireId(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
        return value;
    }
}
