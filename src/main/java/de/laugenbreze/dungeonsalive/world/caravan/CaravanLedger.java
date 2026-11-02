package de.laugenbreze.dungeonsalive.world.caravan;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/** Bounded, persistent reservations. Unloading is intentionally not a membership mutation. */
public final class CaravanLedger {
    public static final int MAX_GROUPS = 2;
    public static final int MAX_MEMBERS = 3;
    private final Map<UUID, Set<UUID>> groups = new LinkedHashMap<>();

    public boolean reserve(UUID group, Set<UUID> members) {
        if (group == null || members == null || members.isEmpty() || members.size() > MAX_MEMBERS || members.stream().anyMatch(java.util.Objects::isNull)) return false;
        if (groups.containsKey(group)) return groups.get(group).equals(members);
        if (groups.size() >= MAX_GROUPS || groups.values().stream().anyMatch(existing -> existing.stream().anyMatch(members::contains))) return false;
        groups.put(group, new LinkedHashSet<>(members));
        return true;
    }
    public boolean contains(UUID group, UUID member) { return groups.getOrDefault(group, Set.of()).contains(member); }
    public boolean remove(UUID group, UUID member) {
        var members = groups.get(group);
        if (members == null || !members.remove(member)) return false;
        if (members.isEmpty()) groups.remove(group);
        return true;
    }
    public int groups() { return groups.size(); }
    public Set<UUID> members(UUID group) { return Set.copyOf(groups.getOrDefault(group, Set.of())); }
    public Map<UUID, Set<UUID>> snapshot() {
        var result = new LinkedHashMap<UUID, Set<UUID>>();
        groups.forEach((id, members) -> result.put(id, Set.copyOf(members)));
        return java.util.Collections.unmodifiableMap(result);
    }
}
