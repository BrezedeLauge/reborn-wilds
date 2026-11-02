package de.laugenbreze.dungeonsalive.item.gear;

/** Dungeons forms with explicit native-Minecraft balance and source-model identities. */
public enum WeaponKind {
    HEARTSTEALER("heartstealer", 9, 1.0F, 1561),
    STORMLANDER("stormlander", 10, .8F, 1561),
    FIGHTERS_BINDINGS("fighters_bindings", 5, 3.2F, 1561),
    SUNS_GRACE("suns_grace", 7, 1.3F, 1561),
    FROST_SCYTHE("frost_scythe", 8, 1.0F, 1561),
    WHIRLWIND("whirlwind", 9, 1.1F, 1561),
    SWORD("sword", 7, 1.6F, 1561, WeaponFamily.SWORD),
    CUTLASS("cutlass", 6, 2F, 1561, WeaponFamily.CUTLASS),
    DAGGERS("daggers", 4, 3F, 1561, WeaponFamily.DAGGERS),
    GLAIVE("glaive", 8, 1.1F, 1561, WeaponFamily.GLAIVE),
    SPEAR("spear", 7, 1.2F, 1561, WeaponFamily.SPEAR),
    HAMMER("hammer", 11, .7F, 1561, WeaponFamily.HAMMER),
    SICKLES("sickles", 5, 2.5F, 1561, WeaponFamily.SICKLES),
    SOUL_KNIFE("soul_knife", 9, .8F, 1561, WeaponFamily.SOUL_KNIFE);

    public final String id;
    public final int damage;
    public final float attacksPerSecond;
    public final int durability;
    public final WeaponFamily family;

    WeaponKind(String id, int damage, float attacksPerSecond, int durability) {
        this(id, damage, attacksPerSecond, durability, WeaponFamily.SIGNATURE);
    }
    WeaponKind(String id, int damage, float attacksPerSecond, int durability, WeaponFamily family) {
        this.id = id;
        this.damage = damage;
        this.attacksPerSecond = attacksPerSecond;
        this.durability = durability;
        this.family = family;
    }
}
