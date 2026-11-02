package de.laugenbreze.dungeonsalive.entity.construct;

/** Existing authored rigs with explicit sandbox lifetimes, not additional wandering species. */
public enum ConstructKind {
    GEOMANCER_WALL("geomancer_wall", .875f, 3.125f, 16, 25, 120, 40),
    GEOMANCER_BOMB("geomancer_bomb", .875f, 3.125f, 8, 25, 35, 40),
    ICE_CHUNK("ice_chunk", 2f, 1.5f, 10, 40, 60, 23),
    ENTANGLE_VINE("entangle_vine", 2f, 1f, 10, 8, 60, 25);

    private final String id;
    private final float width, height, health;
    private final int formTicks, activeTicks, decayTicks;
    ConstructKind(String id, float width, float height, float health, int formTicks, int activeTicks, int decayTicks) {
        this.id = id; this.width = width; this.height = height; this.health = health;
        this.formTicks = formTicks; this.activeTicks = activeTicks; this.decayTicks = decayTicks;
    }
    public String id() { return id; }
    public float width() { return width; }
    public float height() { return height; }
    public float health() { return health; }
    public int formTicks() { return formTicks; }
    public int activeTicks() { return activeTicks; }
    public int decayTicks() { return decayTicks; }
    public int maximumLifetimeTicks() { return formTicks + activeTicks + decayTicks; }
}
