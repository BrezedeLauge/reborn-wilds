package de.laugenbreze.dungeonsalive.network;

import java.util.List;

/** Server-generated display data. Never accepted as a source of player progress. */
public record JournalView(int schemaVersion, List<Entry> quests) {
    public record Entry(String id, String title, String description, String icon, String status,
                        List<Objective> objectives, List<Reward> rewards, int experience,
                        List<String> prerequisites) {}
    public record Objective(String label, int current, int required) {}
    public record Reward(String itemId, int count) {}
}
