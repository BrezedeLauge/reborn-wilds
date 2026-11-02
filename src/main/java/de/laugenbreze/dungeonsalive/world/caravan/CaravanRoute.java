package de.laugenbreze.dungeonsalive.world.caravan;

/** Persistent route decisions independent of navigation and Minecraft's day-time commands. */
public final class CaravanRoute {
    public static final int REST_TICKS = 200;
    public static final int PANIC_TICKS = 100;
    public enum Action { WAIT, TRAVEL, TRADE, FLEE }
    private boolean towardsB;
    private long restUntil;
    private long alarmUntil;

    public CaravanRoute() { this(true, 0, 0); }
    public CaravanRoute(boolean towardsB, long restUntil, long alarmUntil) {
        this.towardsB = towardsB;
        this.restUntil = restUntil;
        this.alarmUntil = alarmUntil;
    }
    public boolean towardsB() { return towardsB; }
    public long restUntil() { return restUntil; }
    public long alarmUntil() { return alarmUntil; }
    public void hurt(long now) { alarmUntil = Math.max(alarmUntil, now + PANIC_TICKS); }

    public Action decide(long now, long dayTime, boolean trading, boolean raining, double targetDistanceSquared) {
        if (now < alarmUntil) return Action.FLEE;
        if (trading) return Action.TRADE;
        if (Math.floorMod(dayTime, 24_000L) >= 12_000 || raining || now < restUntil) return Action.WAIT;
        if (targetDistanceSquared <= 2.25) {
            towardsB = !towardsB;
            restUntil = now + REST_TICKS;
            return Action.WAIT;
        }
        return Action.TRAVEL;
    }
}
