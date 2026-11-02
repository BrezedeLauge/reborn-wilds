package de.laugenbreze.dungeonsalive.entity.peaceful;

/** A few shared water sections, with a finite fallback when a pond shrinks or a route is blocked. */
final class DuckSwimProgress {
    static final int SHARED_SECTIONS = 3;
    static final int MAX_ROAM_TICKS = 480;
    private int ticks;
    private int sections;

    void tick() { ticks = Math.min(MAX_ROAM_TICKS, ticks + 1); }
    void arrivedTogether() { sections = Math.min(SHARED_SECTIONS, sections + 1); }
    boolean readyForShore() { return sections >= SHARED_SECTIONS || ticks >= MAX_ROAM_TICKS; }
    void reset() { ticks = 0; sections = 0; }
}
