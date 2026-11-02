package de.laugenbreze.dungeonsalive.item.gear.enchantment;

/** Finite balance rules; no claim that these are the original game's numeric values. */
public final class GearEnchantmentRules {
    public static final int RADIANCE_PAUSE = 20, CHAINS_PAUSE = 40, SWIRLING_PAUSE = 20;
    public static final double AREA_RADIUS = 3;
    public static final int AREA_CAP = 8;
    private GearEnchantmentRules() {}
    public static int level(int raw) { return Math.clamp(raw, 0, GearEnchantmentKind.MAX_LEVEL); }
    /** A saved deadline may not extend an effect's pause after a clock reset or malformed NBT. */
    public static boolean cooldownReady(long now, long until, int pause) {
        long remaining = until - now;
        return until <= now || remaining < 0 || remaining > pause;
    }
    public static float committedMultiplier(int raw, float health, float maximum) {
        if (!Float.isFinite(health) || !Float.isFinite(maximum) || maximum <= 0) return 1;
        double missing = 1 - Math.clamp((double) health / maximum, 0, 1);
        return (float) (1 + .1 * level(raw) * missing);
    }
    public static float criticalMultiplier(int raw, double roll) {
        int rank = level(raw);
        return rank > 0 && Double.isFinite(roll) && roll >= 0 && roll < .05 * rank ? 1.5F + .25F * rank : 1;
    }
    public static float radianceHealing(int raw) { return level(raw); }
    public static float leechingHealing(int raw, float maximum) {
        int rank = level(raw);
        return Float.isFinite(maximum) && maximum > 0 ? Math.min(2 * rank, maximum * .03F * rank) : 0;
    }
    public static int chainsTicks(int raw) { int rank = level(raw); return rank == 0 ? 0 : 20 + rank * 20; }
    public static float swirlingDamage(int raw) { int rank = level(raw); return rank == 0 ? 0 : 1 + rank; }
}
