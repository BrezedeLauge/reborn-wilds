package de.laugenbreze.dungeonsalive.entity.peaceful;

import java.util.List;
import java.util.UUID;

/** Bounded Minecraft balance, deliberately distinct from an original Dungeons drop table. */
public final class PiggyBankTreasureState {
    public static final int MIN_EMERALDS = 6;
    public static final int MAX_EMERALDS = 12;
    public static final List<String> GEAR = List.of("power_bow", "boots_of_swiftness", "iron_hide_amulet", "death_cap_mushroom");
    private boolean planned;
    private boolean spent;
    private int emeralds;
    private int gear;

    public void prepare(UUID identity) {
        if (planned) return;
        long mixed = identity.getMostSignificantBits() ^ Long.rotateLeft(identity.getLeastSignificantBits(), 23);
        mixed = (mixed ^ mixed >>> 30) * 0xbf58476d1ce4e5b9L;
        mixed = (mixed ^ mixed >>> 27) * 0x94d049bb133111ebL;
        mixed ^= mixed >>> 31;
        emeralds = MIN_EMERALDS + (int)Math.floorMod(mixed, MAX_EMERALDS - MIN_EMERALDS + 1);
        gear = (int)Math.floorMod(mixed >>> 12, GEAR.size());
        planned = true;
    }

    public int emeralds() { return emeralds; }
    public int gearIndex() { return gear; }
    public boolean spent() { return spent; }
    public boolean spend() {
        if (!planned || spent) return false;
        spent = true;
        return true;
    }
    public void restore(UUID identity, int savedEmeralds, int savedGear, boolean savedSpent) {
        planned = false; prepare(identity);
        if (savedEmeralds >= MIN_EMERALDS && savedEmeralds <= MAX_EMERALDS
                && savedGear >= 0 && savedGear < GEAR.size()) {
            emeralds = savedEmeralds; gear = savedGear;
        }
        spent = savedSpent;
    }
}
