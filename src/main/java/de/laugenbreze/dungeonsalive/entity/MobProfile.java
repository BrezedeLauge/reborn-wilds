package de.laugenbreze.dungeonsalive.entity;

import java.util.List;

/** Explicit Minecraft balancing; visual provenance is recorded separately in the model manifests. */
public record MobProfile(String id, String nameEn, String nameDe, Tier tier, Family family,
                         double health, double damage, double speed, double armor,
                         float width, float height, int color, int accent, List<AttackKind> attacks) {
    public enum Tier { REGULAR, MINIBOSS, BOSS }
    public enum Family { ILLAGER, SKELETON, WRAITH, GOLEM, PLANT, ENDERLING, CAULDRON, GUARDIAN, WILDFIRE, HORSEMAN, SLIME, ZOMBIE, PIGLIN, CREEPER }
    public MobProfile {
        attacks = List.copyOf(attacks);
        if (!id.matches("[a-z0-9_]+") || health <= 0 || damage < 0 || width <= 0 || height <= 0 || attacks.isEmpty()) {
            throw new IllegalArgumentException("Invalid mob profile: " + id);
        }
    }
    public boolean boss() { return tier == Tier.BOSS; }
    public boolean stationary() { return family == Family.CAULDRON || speed == 0; }
    public boolean aquatic() { return family == Family.GUARDIAN || id.contains("drowned") || id.equals("tropical_slime") || id.equals("wavewhisperer") || id.contains("sunken_skeleton") || id.endsWith("anemone"); }
    public boolean hovering() { return family == Family.WRAITH || family == Family.WILDFIRE; }
}
