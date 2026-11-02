package de.laugenbreze.dungeonsalive.world.corruption;

/** Finite square-spiral scan, filtered to a circular natural front; no Minecraft dependency. */
public final class CorruptionFront {
    public static final int RADIUS = 48, MAX_REGIONS = 8, MAX_PATCHES = 256;
    public static final int CANDIDATES = (RADIUS * 2 + 1) * (RADIUS * 2 + 1);
    public static final int CHECKS_PER_PULSE = 64, CHANGES_PER_PULSE = 4, MAX_PROTECTED = 65_536;
    public enum Stage { GROWING, CLEANSING, CLEANSED }
    public record Offset(int x, int z) { public boolean inCircle() { return x * x + z * z <= RADIUS * RADIUS; } }
    private int cursor, grown;
    private Stage stage = Stage.GROWING;

    public int cursor() { return cursor; }
    public int grown() { return grown; }
    public Stage stage() { return stage; }
    public boolean canGrow() { return stage == Stage.GROWING && cursor < CANDIDATES && grown < MAX_PATCHES; }
    public void scanned() { if (cursor < CANDIDATES) cursor++; }
    public void grew() {
        if (stage != Stage.GROWING || grown >= MAX_PATCHES) throw new IllegalStateException("A bounded front cannot grow further");
        grown++;
    }
    public boolean cleanse() { if (stage != Stage.GROWING) return false; stage = Stage.CLEANSING; return true; }
    public void restoredAll() { if (stage == Stage.CLEANSING) stage = Stage.CLEANSED; }
    public void restore(int cursor, int grown, Stage stage) {
        if (cursor < 0 || cursor > CANDIDATES || grown < 0 || grown > MAX_PATCHES || stage == null)
            throw new IllegalArgumentException("Invalid saved corruption front");
        this.cursor = cursor; this.grown = grown; this.stage = stage;
    }
    public static Offset at(int index) {
        if (index < 0 || index >= CANDIDATES) throw new IllegalArgumentException("Outside bounded corruption spiral");
        if (index == 0) return new Offset(0, 0);
        int ring = (int)Math.ceil((Math.sqrt(index + 1) - 1) / 2), side = 2 * ring;
        int remainder = (side + 1) * (side + 1) - 1 - index;
        if (remainder < side) return new Offset(ring - remainder, -ring);
        if (remainder < side * 2) return new Offset(-ring, -ring + remainder - side);
        if (remainder < side * 3) return new Offset(-ring + remainder - side * 2, ring);
        return new Offset(ring, ring - (remainder - side * 3));
    }
}
