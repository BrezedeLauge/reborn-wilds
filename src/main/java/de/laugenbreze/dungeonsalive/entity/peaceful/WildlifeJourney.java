package de.laugenbreze.dungeonsalive.entity.peaceful;

import java.util.Objects;
import java.util.UUID;

/** Shared local itinerary derived from the already-persistent group identity and world clock. */
public final class WildlifeJourney {
    public static final int LEG_ACTIVE_TICKS = 600;
    public static final int STAY_ACTIVE_TICKS = 200;
    public static final int ROUTE_TICKS = 4 * 4 * WildlifeGroupPolicy.CYCLE_TICKS;
    public static final double MAX_CENTRE_DISTANCE = 34;
    public static final double MAX_LOCAL_DISTANCE = 64;
    public static final double MAX_STEP = 12;
    private static final double[][] STATIONS = {{0, 0}, {30, 8}, {-6, 32}, {-28, -12}};

    public record Stop(double x, double z, boolean travelling, int destination) {}

    private WildlifeJourney() {}

    public static Stop sample(long gameTime, UUID group) {
        Objects.requireNonNull(group);
        // Modulo before adding avoids overflow even for extreme saved game times. The
        // phase matches WildlifeGroupPolicy, so the course freezes throughout its rest ticks.
        long clock = (Math.floorMod(gameTime, ROUTE_TICKS) + Math.floorMod(group.getLeastSignificantBits(), ROUTE_TICKS)) % ROUTE_TICKS;
        long active = clock / WildlifeGroupPolicy.CYCLE_TICKS * (WildlifeGroupPolicy.CYCLE_TICKS - WildlifeGroupPolicy.REST_TICKS)
                + Math.max(0, clock % WildlifeGroupPolicy.CYCLE_TICKS - WildlifeGroupPolicy.REST_TICKS);
        int segment = (int)(active / (LEG_ACTIVE_TICKS + STAY_ACTIVE_TICKS));
        int phase = (int)(active % (LEG_ACTIVE_TICKS + STAY_ACTIVE_TICKS));
        int next = (segment + 1) % STATIONS.length;
        double fraction = Math.min(1, (double)phase / LEG_ACTIVE_TICKS);
        fraction = fraction * fraction * (3 - 2 * fraction);
        double x = STATIONS[segment][0] + (STATIONS[next][0] - STATIONS[segment][0]) * fraction;
        double z = STATIONS[segment][1] + (STATIONS[next][1] - STATIONS[segment][1]) * fraction;
        double angle = Math.floorMod(group.getMostSignificantBits(), 6_283L) / 1000.0;
        return new Stop(x * Math.cos(angle) - z * Math.sin(angle), x * Math.sin(angle) + z * Math.cos(angle),
                phase < LEG_ACTIVE_TICKS, next);
    }
}
