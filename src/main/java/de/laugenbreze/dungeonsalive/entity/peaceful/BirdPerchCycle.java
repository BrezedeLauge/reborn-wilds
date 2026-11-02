package de.laugenbreze.dungeonsalive.entity.peaceful;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

/** One saved group's shared flight/rest calendar; member slots stagger its departures. */
public final class BirdPerchCycle {
    public static final int CYCLE_TICKS = 960;
    public static final int CRUISE_TICKS = 280;
    public static final int DEPARTURE_TICK = 800;
    public static final int SLOT_DELAY = 18;
    public enum Intent { CRUISE, REST, DEPART }
    public record Plan(long cycle, int phase, Intent intent, int shuffleSlot, boolean shuffleWindow) {}

    public static Plan sample(long gameTime, UUID group, int slot, List<Integer> presentSlots, boolean seekShelter) {
        Objects.requireNonNull(group);
        Objects.requireNonNull(presentSlots);
        if (slot < 0 || slot >= WildlifeGroupPolicy.MAX_MEMBERS || presentSlots.isEmpty()
                || presentSlots.size() > WildlifeGroupPolicy.MAX_MEMBERS
                || presentSlots.stream().anyMatch(value -> value == null || value < 0 || value >= WildlifeGroupPolicy.MAX_MEMBERS)
                || presentSlots.stream().distinct().count() != presentSlots.size())
            throw new IllegalArgumentException("Bird slots must be distinct loaded members of a group of at most six");
        long combined = Math.floorMod(gameTime, CYCLE_TICKS) + Math.floorMod(group.getLeastSignificantBits(), CYCLE_TICKS);
        int phase = (int) (combined % CYCLE_TICKS);
        long cycle = Math.floorDiv(gameTime, CYCLE_TICKS) + combined / CYCLE_TICKS;
        Intent intent = seekShelter ? Intent.REST : phase < CRUISE_TICKS ? Intent.CRUISE
                : phase < DEPARTURE_TICK + slot * SLOT_DELAY ? Intent.REST : Intent.DEPART;
        var ordered = presentSlots.stream().sorted().toList();
        int shuffleSlot = ordered.get((int) Math.floorMod(cycle, ordered.size()));
        return new Plan(cycle, phase, intent, shuffleSlot, !seekShelter && phase >= 560 && phase < 700);
    }

    private BirdPerchCycle() {}
}
