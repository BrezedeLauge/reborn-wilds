package de.laugenbreze.dungeonsalive.world.water;

/** Accepted server positions, not the zeroed server velocity of a player-controlled boat. */
public final class CurrentMotionHistory {
    public record Velocity(double x, double z, boolean measured) {
        public static final Velocity UNKNOWN = new Velocity(0, 0, false);
    }
    private long tick = Long.MIN_VALUE;
    private double x, y, z;

    public Velocity observe(long now, double nextX, double nextY, double nextZ) {
        if (!Double.isFinite(nextX) || !Double.isFinite(nextY) || !Double.isFinite(nextZ)) {
            tick = Long.MIN_VALUE;
            return Velocity.UNKNOWN;
        }
        long elapsed = tick == Long.MIN_VALUE ? 0 : now - tick;
        double dx = nextX - x, dy = nextY - y, dz = nextZ - z;
        tick = now; x = nextX; y = nextY; z = nextZ;
        // Restart after first observation, duplicate/backward time, a gap or a discontinuity.
        // This is deliberately conservative: rejecting a jump skips one impulse, never moves a boat back.
        if (elapsed <= 0 || elapsed > 20 || Math.abs(dy) > 2
                || dx * dx + dy * dy + dz * dz > 1.25 * 1.25 * elapsed * elapsed) return Velocity.UNKNOWN;
        return new Velocity(dx / elapsed, dz / elapsed, true);
    }
}
