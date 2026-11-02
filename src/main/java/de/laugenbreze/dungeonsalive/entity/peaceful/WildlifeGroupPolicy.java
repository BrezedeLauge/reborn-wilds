package de.laugenbreze.dungeonsalive.entity.peaceful;

import java.util.UUID;

/** Shared group rhythm and per-actor path budget; all times are absolute server ticks. */
public final class WildlifeGroupPolicy {
    public static final int MAX_MEMBERS = 6;
    public static final int JOIN_RADIUS = 12;
    public static final int COHESION_RADIUS = 24;
    public static final int ALARM_TICKS = 120;
    public static final int REPLAN_TICKS = 20;
    public static final int FAILURE_BACKOFF_TICKS = 60;
    public static final int REST_TICKS = 100;
    public static final int CYCLE_TICKS = 300;

    public enum Activity { REST, WANDER, FLEE }
    private long nextRouteAt = Long.MIN_VALUE;

    public static Activity activity(UUID groupId, long gameTime, long dayTime,
                                    boolean nocturnal, boolean raining, long alarmUntil) {
        if (gameTime < alarmUntil) return Activity.FLEE;
        boolean night = Math.floorMod(dayTime, 24_000L) >= 12_000;
        if (raining || nocturnal != night) return Activity.REST;
        long phase = Math.floorMod(Math.floorMod(gameTime, CYCLE_TICKS)
                + Math.floorMod(groupId.getLeastSignificantBits(), CYCLE_TICKS), CYCLE_TICKS);
        return phase < REST_TICKS ? Activity.REST : Activity.WANDER;
    }

    public boolean canReplan(long now) { return now >= nextRouteAt; }
    public void routeAttempted(long now, boolean success) {
        nextRouteAt = now + (success ? REPLAN_TICKS : FAILURE_BACKOFF_TICKS);
    }
}
