package de.laugenbreze.dungeonsalive.entity.peaceful;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/**
 * A bounded, persistent membership reservation, independent of loaded entities.
 * Unloaded members keep their slots; local leadership never resolves or loads them.
 */
public final class WildlifeGroup {
    private final UUID id;
    private final Set<UUID> members = new HashSet<>();

    public WildlifeGroup(UUID id) { this.id = Objects.requireNonNull(id); }
    public UUID id() { return id; }
    public int size() { return members.size(); }
    public List<UUID> members() { return members.stream().sorted().toList(); }

    public boolean tryJoin(UUID member) {
        Objects.requireNonNull(member);
        if (members.contains(member)) return true;
        return members.size() < WildlifeGroupPolicy.MAX_MEMBERS && members.add(member);
    }

    /** Atomic union: an inconsistent or oversized roster cannot replace a valid one. */
    public boolean merge(WildlifeGroup other) {
        Objects.requireNonNull(other);
        if (!id.equals(other.id)) return false;
        Set<UUID> combined = new HashSet<>(members);
        combined.addAll(other.members);
        if (combined.size() > WildlifeGroupPolicy.MAX_MEMBERS) return false;
        members.addAll(combined);
        return true;
    }

    public int slot(UUID member) { return members().indexOf(member); }

    /** Null means no known member is currently present; no absent leader blocks succession. */
    public UUID leader(Collection<UUID> loadedAlive) {
        return loadedAlive.stream().filter(members::contains).min(UUID::compareTo).orElse(null);
    }
}
