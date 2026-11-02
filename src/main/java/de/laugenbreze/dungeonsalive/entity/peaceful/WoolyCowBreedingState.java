package de.laugenbreze.dungeonsalive.entity.peaceful;

/** Loaded server-tick lifecycle. Unloaded time never produces offspring or skips a breeding pause. */
public final class WoolyCowBreedingState {
    public static final int GROWTH_TICKS = 24_000;
    public static final int BREEDING_PAUSE_TICKS = 6_000;
    public static final int LOVE_TICKS = 600;
    public static final int COURTSHIP_TICKS = 60;
    public static final int LOCAL_POPULATION_LIMIT = 24;
    public static final int POPULATION_RADIUS = 48;
    public static final int MATE_RADIUS = 8;
    public static final int TEMPT_RADIUS = 10;
    public static final int PARENT_RADIUS = 16;
    private int age;
    private int love;

    public int age() { return age; }
    public int loveTicks() { return love; }
    public boolean baby() { return age < 0; }
    public boolean inLove() { return age == 0 && love > 0; }
    public boolean readyForFood() { return age < 0 || age == 0 && love == 0; }

    public void tick() {
        if (age < 0) age++;
        else if (age > 0) age--;
        if (age != 0) love = 0;
        else if (love > 0) love--;
    }

    /** Wheat advances a calf by a tenth of its remaining growth, or starts an adult's love window. */
    public boolean feed() {
        if (age < 0) {
            age = Math.min(0, age + Math.max(1, -age / 10));
            return true;
        }
        if (!readyForFood()) return false;
        love = LOVE_TICKS;
        return true;
    }

    public void born() { age = -GROWTH_TICKS; love = 0; }
    public void bred() { age = BREEDING_PAUSE_TICKS; love = 0; }
    public void interruptLove() { love = 0; }

    public void restore(int savedAge, int savedLove) {
        age = Math.clamp(savedAge, -GROWTH_TICKS, BREEDING_PAUSE_TICKS);
        love = age == 0 ? Math.clamp(savedLove, 0, LOVE_TICKS) : 0;
    }

    public static boolean roomForBirth(int loadedWildlife, int reservedGroupMembers) {
        return loadedWildlife >= 2 && loadedWildlife < LOCAL_POPULATION_LIMIT
                && reservedGroupMembers >= 1 && reservedGroupMembers < WildlifeGroupPolicy.MAX_MEMBERS;
    }
}
