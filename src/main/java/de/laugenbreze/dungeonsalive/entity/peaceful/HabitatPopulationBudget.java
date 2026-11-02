package de.laugenbreze.dungeonsalive.entity.peaceful;

import java.util.HashMap;
import java.util.Map;

/** Bounded regional replenishment history, independent of entities and the Minecraft clock API. */
public final class HabitatPopulationBudget {
    public static final int MAX_REGIONS = 2048;
    public static final int COOLDOWN_TICKS = 1200;
    private final Map<Long, Long> until = new HashMap<>();

    public boolean available(long region, long now) {
        until.entrySet().removeIf(entry -> entry.getValue() <= now);
        return !until.containsKey(region) && until.size() < MAX_REGIONS;
    }

    public boolean recordSuccess(long region, long now) {
        if (!available(region, now)) return false;
        until.put(region, now > Long.MAX_VALUE - COOLDOWN_TICKS ? Long.MAX_VALUE : now + COOLDOWN_TICKS);
        return true;
    }

    public void restore(long region, long deadline) {
        if (until.size() < MAX_REGIONS || until.containsKey(region)) until.merge(region, deadline, Math::max);
    }

    public Map<Long, Long> snapshot() { return Map.copyOf(until); }

    /** Whole groups only. Failed probes and full habitats never consume population slots. */
    public static int groupSize(int requested, int nearby, int nearbyLimit, int loaded, int loadedLimit) {
        if (nearby < 0 || loaded < 0 || nearbyLimit < 0 || loadedLimit < 0) return 0;
        int size = Math.min(Math.min(Math.max(0, requested), WildlifeGroupPolicy.MAX_MEMBERS),
                Math.min(Math.max(0, nearbyLimit - nearby), Math.max(0, loadedLimit - loaded)));
        return size >= 2 ? size : 0;
    }
}
