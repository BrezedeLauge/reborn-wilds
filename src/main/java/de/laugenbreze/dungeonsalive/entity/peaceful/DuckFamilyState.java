package de.laugenbreze.dungeonsalive.entity.peaceful;

/** Saved local family activity. Time advances only while the family can perform its real activity. */
public final class DuckFamilyState {
    public enum Activity { SWIM, TO_SHORE, FORAGE, PREEN, REST, TO_WATER, ALERT, FLEE }
    public static final int YOUNG_TICKS = 24_000;
    public static final int SWIM_TICKS = 120;
    public static final int FORAGE_TICKS = 80;
    public static final int PREEN_TICKS = 60;
    public static final int REST_TICKS = 140;
    public static final int ALERT_TICKS = 12;
    private Activity activity = Activity.SWIM;
    private int ticks;
    private int youngTicks;

    public Activity activity() { return activity; }
    public int ticks() { return ticks; }
    public int youngTicks() { return youngTicks; }
    public boolean young() { return youngTicks > 0; }
    public void born() { youngTicks = YOUNG_TICKS; }
    public void tickAge() { if (youngTicks > 0) youngTicks--; }

    public void restore(int savedYoungTicks, int savedActivity, int savedTicks) {
        youngTicks = Math.clamp(savedYoungTicks, 0, YOUNG_TICKS);
        activity = savedActivity >= 0 && savedActivity < Activity.values().length
                ? Activity.values()[savedActivity] : Activity.SWIM;
        ticks = Math.clamp(savedTicks, 0, 24_000);
    }

    public void change(Activity next) {
        if (activity == next) return;
        activity = next;
        ticks = 0;
    }

    public void follow(DuckFamilyState leader) {
        activity = leader.activity;
        ticks = leader.ticks;
    }

    /** No clock, player, reward or forced stage transition is necessary for the ordinary cycle. */
    public void tick(boolean arrived, boolean familyTogether, boolean inWater, boolean alarm) {
        if (alarm && activity != Activity.ALERT && activity != Activity.FLEE) change(Activity.ALERT);
        if (activity == Activity.ALERT) {
            if (++ticks >= ALERT_TICKS) change(Activity.FLEE);
            return;
        }
        if (activity == Activity.FLEE) {
            if (!alarm) change(inWater ? Activity.SWIM : Activity.TO_WATER);
            else ticks = Math.min(24_000, ticks + 1);
            return;
        }
        // A straggler or an inaccessible destination cannot be turned into a completed activity.
        if (!familyTogether) return;
        switch (activity) {
            case SWIM -> { if (inWater && ++ticks >= SWIM_TICKS) change(Activity.TO_SHORE); }
            case TO_SHORE -> { if (arrived && !inWater) change(Activity.FORAGE); }
            case FORAGE -> { if (arrived && !inWater && ++ticks >= FORAGE_TICKS) change(Activity.PREEN); }
            case PREEN -> { if (arrived && !inWater && ++ticks >= PREEN_TICKS) change(Activity.REST); }
            case REST -> { if (arrived && !inWater && ++ticks >= REST_TICKS) change(Activity.TO_WATER); }
            case TO_WATER -> { if (arrived && inWater) change(Activity.SWIM); }
            default -> { }
        }
    }
}
