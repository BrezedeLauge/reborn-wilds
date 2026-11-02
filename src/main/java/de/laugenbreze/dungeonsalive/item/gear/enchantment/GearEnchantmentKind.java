package de.laugenbreze.dungeonsalive.item.gear.enchantment;

/** Stable native enchantment IDs. Prices are this Minecraft adaptation's emerald costs. */
public enum GearEnchantmentKind {
    RADIANCE("radiance"), LEECHING("leeching"), COMMITTED("committed"),
    CRITICAL_HIT("critical_hit"), CHAINS("chains"), SWIRLING("swirling");

    public static final int MAX_LEVEL = 3;
    public final String id;
    GearEnchantmentKind(String id) { this.id = id; }
    public String nameKey() { return "enchantment.dungeonsalive." + id; }
    public int cost(int targetLevel) {
        if (targetLevel < 1 || targetLevel > MAX_LEVEL) throw new IllegalArgumentException("Enchantment level must be I–III");
        return targetLevel * 8;
    }
}
