package de.laugenbreze.dungeonsalive.entity.peaceful;

/**
 * Server-side camp routine and route budget. Minecraft supplies observations and performs
 * movement; this state machine owns priority, damage memory and retry deadlines.
 * Transient routes and alarm deadlines deliberately restart after an entity reload.
 */
public final class MerchantRoutine {
    public static final int DAY_RADIUS = 6;
    public static final int FLEE_RADIUS = 10;
    public static final double HOME_TOLERANCE_SQR = 2.25;
    public static final int FLEE_TICKS = 100;
    public static final int REPLAN_TICKS = 20;
    public static final int FAILURE_BACKOFF_TICKS = 80;
    public static final int WANDER_INTERVAL_TICKS = 160;

    public enum Activity { TRADING, FLEE, RETURN_HOME, REST, WANDER }
    public record Observation(long gameTime, long dayTime, boolean raining, boolean trading,
                              double homeDistanceSqr, boolean navigationDone) {}
    public record Decision(Activity activity, boolean stopNavigation, boolean planRoute) {}

    private Activity activity = Activity.REST;
    private long frightenedUntil = Long.MIN_VALUE;
    private long retryAt = Long.MIN_VALUE;
    private long wanderAt = Long.MIN_VALUE;
    private boolean returnRequired;

    public Activity activity() { return activity; }
    public boolean frightened(long now) { return now < frightenedUntil; }

    public void hurt(long now) {
        frightenedUntil = now + FLEE_TICKS;
        returnRequired = true;
        // Do not clear retryAt: repeated damage must not turn path failure into a busy loop.
    }

    public Decision decide(Observation observation) {
        long now = observation.gameTime();
        boolean home = observation.homeDistanceSqr() <= HOME_TOLERANCE_SQR;
        boolean shelterTime = observation.raining() || Math.floorMod(observation.dayTime(), 24_000L) >= 12_000;
        Activity next;
        if (frightened(now)) {
            next = Activity.FLEE;
        } else if (observation.trading()) {
            next = Activity.TRADING;
        } else {
            if (home) returnRequired = false;
            if (!home && (returnRequired || shelterTime
                    || observation.homeDistanceSqr() > DAY_RADIUS * DAY_RADIUS)) {
                returnRequired = true;
                next = Activity.RETURN_HOME;
            } else {
                next = shelterTime ? Activity.REST : Activity.WANDER;
            }
        }
        boolean changed = next != activity;
        activity = next;
        boolean resting = next == Activity.REST || next == Activity.TRADING;
        boolean plan = !resting && now >= retryAt
                && (next != Activity.WANDER || observation.navigationDone() && now >= wanderAt);
        return new Decision(next, changed || resting, plan);
    }

    /** Called exactly once for each attempted route, including rejected/unloaded targets. */
    public void routeAttempted(long now, boolean success) {
        retryAt = now + (success ? REPLAN_TICKS : FAILURE_BACKOFF_TICKS);
        if (activity == Activity.WANDER) {
            wanderAt = now + (success ? WANDER_INTERVAL_TICKS : FAILURE_BACKOFF_TICKS);
        }
    }
}
