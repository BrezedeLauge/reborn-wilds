package de.laugenbreze.dungeonsalive.world.trail;

/** Repeated crossings wear ground; unused generated trails always recover, including their final path stage. */
public final class FootTraffic {
    private int steps;
    private int stage;
    private long lastStep;
    private final String original;
    public FootTraffic(String original, int steps, int stage, long lastStep) {
        this.original = original; this.steps = Math.clamp(steps, 0, 64); this.stage = Math.clamp(stage, 0, 2); this.lastStep = lastStep;
    }
    public void stepped(long now) { steps = Math.min(64, steps + 1); lastStep = now; }
    public int desiredStage(int wearThreshold, int pathThreshold) {
        return steps >= pathThreshold ? 2 : steps >= wearThreshold ? 1 : 0;
    }
    public int recoveredStage(long now, int regrowTicks) {
        long elapsed;
        try { elapsed = Math.subtractExact(now, lastStep); }
        catch (ArithmeticException invalidSavedClock) { lastStep = now; return stage; }
        // A future/corrupt saved timestamp or clock rollback starts one normal
        // recovery interval, never an effectively permanent or immediate trail.
        if (elapsed < 0) { lastStep = now; return stage; }
        if (elapsed < regrowTicks) return stage;
        return elapsed >= (long)regrowTicks * 2 ? 0 : Math.min(stage, 1);
    }
    public void setStage(int stage) { this.stage = Math.clamp(stage, 0, 2); }
    public String original() { return original; }
    public int steps() { return steps; }
    public int stage() { return stage; }
    public long lastStep() { return lastStep; }
}
