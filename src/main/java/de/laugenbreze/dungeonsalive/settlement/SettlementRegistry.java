package de.laugenbreze.dungeonsalive.settlement;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/** Bounded saved settlement decisions; positions never imply loading a world or an entity. */
public final class SettlementRegistry {
    public static final int MAX_SETTLEMENTS = 64, MAX_PER_OWNER = 3, MIN_SPACING = 96, MAX_REWARDS = 16_384;
    public enum Decision { AVAILABLE, WORLD_LIMIT, OWNER_LIMIT, NEARBY, BLOCKED }
    public record Anchor(UUID id, UUID owner, String dimension, int x, int y, int z, List<UUID> residents) {
        public Anchor {
            Objects.requireNonNull(id); Objects.requireNonNull(owner); Objects.requireNonNull(dimension);
            residents = List.copyOf(residents);
            if (dimension.isBlank() || residents.size() != 2 || new HashSet<>(residents).size() != 2)
                throw new IllegalArgumentException("A settlement needs two distinct residents and a dimension");
        }
    }
    private final java.util.ArrayList<Anchor> anchors = new java.util.ArrayList<>();
    private final Set<UUID> rewards = new HashSet<>();
    private boolean blocked;

    public Decision check(UUID owner, String dimension, int x, int z) {
        if (blocked) return Decision.BLOCKED;
        if (anchors.size() >= MAX_SETTLEMENTS) return Decision.WORLD_LIMIT;
        if (anchors.stream().filter(anchor -> anchor.owner().equals(owner)).count() >= MAX_PER_OWNER) return Decision.OWNER_LIMIT;
        for (var anchor : anchors) {
            double dx = (double)anchor.x() - x, dz = (double)anchor.z() - z;
            if (anchor.dimension().equals(dimension) && dx * dx + dz * dz < MIN_SPACING * MIN_SPACING) return Decision.NEARBY;
        }
        return Decision.AVAILABLE;
    }
    public boolean add(Anchor anchor) {
        if (check(anchor.owner(), anchor.dimension(), anchor.x(), anchor.z()) != Decision.AVAILABLE
                || anchors.stream().anyMatch(existing -> existing.id().equals(anchor.id())
                    || existing.residents().stream().anyMatch(anchor.residents()::contains))) return false;
        anchors.add(anchor); return true;
    }
    public boolean claimReward(UUID boss) { return !blocked && rewards.size() < MAX_REWARDS && rewards.add(Objects.requireNonNull(boss)); }
    public List<Anchor> anchors() { return List.copyOf(anchors); }
    public List<UUID> rewardClaims() { return rewards.stream().sorted().toList(); }
    public boolean restore(List<Anchor> saved, List<UUID> claims) {
        anchors.clear(); rewards.clear(); blocked = false;
        if (saved.size() > MAX_SETTLEMENTS || claims.size() > MAX_REWARDS) { blocked = true; return false; }
        for (var anchor : saved) if (anchor == null || !add(anchor)) { blocked = true; return false; }
        for (UUID claim : claims) if (claim == null || !rewards.add(claim)) { blocked = true; return false; }
        return true;
    }
}
