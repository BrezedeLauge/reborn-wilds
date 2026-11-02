package de.laugenbreze.dungeonsalive.item.gear;

/** Deliberate Minecraft balancing; values are ticks and whole soul charges. */
public enum ArtifactKind {
    BOOTS_OF_SWIFTNESS("boots_of_swiftness", 200, 0),
    DEATH_CAP_MUSHROOM("death_cap_mushroom", 600, 0),
    IRON_HIDE_AMULET("iron_hide_amulet", 500, 0),
    SHOCK_POWDER("shock_powder", 300, 0),
    GONG_OF_WEAKENING("gong_of_weakening", 400, 0),
    HARVESTER("harvester", 100, 20),
    CORRUPTED_BEACON("corrupted_beacon", 60, 3),
    LIGHTNING_ROD("lightning_rod", 200, 15),
    SOUL_HEALER("soul_healer", 200, 15),
    GHOST_CLOAK("ghost_cloak", 200, 0),
    FLAMING_QUIVER("flaming_quiver", 600, 0);

    public final String id;
    public final int cooldownTicks;
    public final int soulCost;

    ArtifactKind(String id, int cooldownTicks, int soulCost) {
        this.id = id;
        this.cooldownTicks = cooldownTicks;
        this.soulCost = soulCost;
    }
}
