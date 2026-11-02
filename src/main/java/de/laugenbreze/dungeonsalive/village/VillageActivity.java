package de.laugenbreze.dungeonsalive.village;

/** Small pure decision layer: visual work must follow actual server work memory. */
public final class VillageActivity {
    public static final int IDLE = 0, WORK = 1, TRADE = 2, PANIC = 3;
    public static final int WORK_TICKS = 80;
    private VillageActivity() {}

    public record Observation(boolean alive, boolean baby, boolean sleeping, boolean noAi,
                              boolean panic, boolean trading, boolean workActivity, boolean validJobSite,
                              boolean stationary, long lastWorked, long now) {}
    public record State(int mode, long startedAt, int duration) {
        public State {
            if (mode < IDLE || mode > PANIC || duration < 0 || duration > 200) {
                throw new IllegalArgumentException("Invalid villager animation state");
            }
        }
        public static State idle() { return new State(IDLE, 0, 0); }
    }

    public static State select(Observation o) {
        if (!o.alive || o.baby || o.sleeping || o.noAi) return State.idle();
        if (o.panic) return new State(PANIC, o.now, 100);
        if (o.trading) return new State(TRADE, o.now, 100);
        long elapsed = o.now - o.lastWorked;
        if (o.workActivity && o.validJobSite && o.stationary && o.lastWorked >= 0
                && elapsed >= 0 && elapsed < WORK_TICKS) {
            return new State(WORK, o.lastWorked, WORK_TICKS);
        }
        return State.idle();
    }
}
