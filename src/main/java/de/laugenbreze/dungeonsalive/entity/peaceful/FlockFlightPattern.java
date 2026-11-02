package de.laugenbreze.dungeonsalive.entity.peaceful;

import java.util.Objects;
import java.util.UUID;

/** Shared local flight geometry. Coordinates describe waypoints, never forced entity motion. */
public final class FlockFlightPattern {
    public static final int MAX_MEMBERS = 6;
    public static final int ORBIT_TICKS = 360;
    public static final double MAX_HORIZONTAL_DISTANCE = 22;

    public record Point(double x, double y, double z) {
        public Point {
            if (!Double.isFinite(x) || !Double.isFinite(y) || !Double.isFinite(z)) {
                throw new IllegalArgumentException("Flight coordinates must be finite");
            }
        }
    }

    private FlockFlightPattern() {}

    public static Point sample(long gameTime, UUID flockId, boolean vulture, int slot,
                               WildlifeGroupPolicy.Activity activity, Point anchor, Point threat) {
        Objects.requireNonNull(flockId);
        Objects.requireNonNull(activity);
        Objects.requireNonNull(anchor);
        Objects.requireNonNull(threat);
        if (slot < 0 || slot >= MAX_MEMBERS) throw new IllegalArgumentException("A flock has at most six slots");
        double seedAngle = Math.floorMod(flockId.getLeastSignificantBits(), ORBIT_TICKS) * (Math.PI * 2 / ORBIT_TICKS);
        double angle = seedAngle + Math.floorMod(gameTime, ORBIT_TICKS) * (Math.PI * 2 / ORBIT_TICKS);
        double x = anchor.x(), z = anchor.z();
        double y = anchor.y() + (vulture ? 1 : 1.8);
        double forwardX = -Math.sin(seedAngle), forwardZ = Math.cos(seedAngle);
        if (activity == WildlifeGroupPolicy.Activity.WANDER) {
            double radius = vulture ? 7 : 4;
            x += Math.cos(angle) * radius;
            z += Math.sin(angle) * radius;
            y += vulture ? 4 + Math.sin(angle * 2) * 1.4 : 1.2 + Math.sin(angle * 2) * .6;
            forwardX = -Math.sin(angle);
            forwardZ = Math.cos(angle);
        } else if (activity == WildlifeGroupPolicy.Activity.FLEE) {
            double dx = anchor.x() - threat.x(), dz = anchor.z() - threat.z();
            double length = Math.hypot(dx, dz);
            forwardX = length > .001 ? dx / length : Math.cos(seedAngle);
            forwardZ = length > .001 ? dz / length : Math.sin(seedAngle);
            x += forwardX * 12;
            z += forwardZ * 12;
            y += vulture ? 7 : 4;
        }

        if (slot > 0 && vulture) {
            int row = (slot + 1) / 2;
            double side = (slot & 1) == 0 ? 1 : -1;
            x += -forwardX * row * 2.2 + forwardZ * side * row * 1.8;
            z += -forwardZ * row * 2.2 - forwardX * side * row * 1.8;
            y += row * .25;
        } else if (slot > 0) {
            double offsetAngle = seedAngle + (slot - 1) * (Math.PI * 2 / (MAX_MEMBERS - 1));
            // Wisps hold compact, distinct hovering places around the shared course.
            x += Math.cos(offsetAngle) * 1.8;
            z += Math.sin(offsetAngle) * 1.8;
            y += Math.sin(offsetAngle) * .45;
        }
        return new Point(x, y, z);
    }
}
