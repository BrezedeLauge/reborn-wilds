package de.laugenbreze.dungeonsalive.item.gear;

/** Native Minecraft handling; no separate dual-wield or original combo animation is implied. */
public enum WeaponFamily {
    SIGNATURE(0, 0, true, 0),
    SWORD(0, 0, true, 0),
    CUTLASS(0, 0, true, 0),
    DAGGERS(0, 0, false, 0),
    GLAIVE(.75, 0, true, 0),
    SPEAR(1, 0, false, 0),
    HAMMER(0, .6, false, 0),
    SICKLES(0, 0, true, 0),
    SOUL_KNIFE(0, 0, false, 2);

    public final double entityReachBonus;
    public final double knockbackBonus;
    public final boolean sweeping;
    public final int soulBonus;

    WeaponFamily(double entityReachBonus, double knockbackBonus, boolean sweeping, int soulBonus) {
        this.entityReachBonus = entityReachBonus;
        this.knockbackBonus = knockbackBonus;
        this.sweeping = sweeping;
        this.soulBonus = soulBonus;
    }
}
