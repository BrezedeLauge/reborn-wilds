package de.laugenbreze.dungeonsalive.quest.core;

/** Events understood by the quest domain. Minecraft adapters supply registry IDs as strings. */
public enum ObjectiveType {
    KILL_ENTITY,
    VISIT_BIOME,
    COLLECT_ITEM,
    WORLD_STATE
}
