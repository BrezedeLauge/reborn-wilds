package de.laugenbreze.dungeonsalive.world.water;

/** Bounded gameplay hydraulics, measured in blocks/tick; not a volumetric fluid solver. */
public final class CurrentStrength {
    private CurrentStrength() {}
    public static double speed(double width, double depth, double slope, double bankDistance) {
        if (!Double.isFinite(width) || !Double.isFinite(depth) || !Double.isFinite(slope)
                || !Double.isFinite(bankDistance) || width <= 0 || depth <= 0) return 0;
        double channel = Math.sqrt(12 / (Math.max(2, width) * Math.sqrt(Math.max(1, depth))));
        double bank = .35 + .65 * Math.clamp(2 * bankDistance / width, 0, 1);
        return Math.clamp(.055 * channel * (1 + Math.clamp(slope, 0, 2) * .85) * bank, .012, .14);
    }
    public static double impulse(double targetSpeed, double downstreamSpeed, int ticks) {
        if (!Double.isFinite(targetSpeed) || !Double.isFinite(downstreamSpeed)) return 0;
        // Let swimmers/paddlers exceed the stream speed; do not erase their steering or vertical motion.
        return Math.clamp((targetSpeed - downstreamSpeed) * .07 * Math.clamp(ticks, 0, 5), 0, .028);
    }
}
