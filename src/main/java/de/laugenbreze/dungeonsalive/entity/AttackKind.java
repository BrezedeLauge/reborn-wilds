package de.laugenbreze.dungeonsalive.entity;

/** Server-authoritative action timelines, in game ticks. The client reads the same timeline. */
public enum AttackKind {
    MELEE(14, 8, 18, 3.2),
    HEAVY_SLAM(30, 14, 28, 5.5),
    SOUL_BOLT(24, 10, 28, 18),
    VOLLEY(26, 12, 36, 20),
    FIRE_FIELD(36, 16, 42, 16),
    ICE_FALL(36, 14, 40, 16),
    ROOT_SNARE(32, 12, 38, 16),
    BEAM(44, 16, 38, 22),
    STORM(42, 18, 50, 8),
    SUMMON(38, 14, 90, 18),
    TELEPORT(20, 12, 30, 18),
    GUARD(10, 38, 35, 14),
    ENCHANT(30, 12, 70, 16),
    LEAP(24, 16, 30, 10),
    ICY_EXPLOSION(25, 5, 60, 3.2);

    private final int windup, recovery, cooldown;
    private final double range;
    AttackKind(int windup, int recovery, int cooldown, double range) {
        this.windup = windup;
        this.recovery = recovery;
        this.cooldown = cooldown;
        this.range = range;
    }
    public int windup() { return windup; }
    public int recovery() { return recovery; }
    public int duration() { return windup + recovery; }
    public int cooldown() { return cooldown; }
    public double range() { return range; }
}
