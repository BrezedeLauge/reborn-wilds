package de.laugenbreze.dungeonsalive.item.gear;

/** Four real ranged items; these values are the Minecraft adaptation's balance. */
public enum RangedWeaponKind {
    POWER_BOW("power_bow", false, 768),
    HUNTERS_PROMISE("hunters_promise", false, 768),
    AUTO_CROSSBOW("auto_crossbow", true, 930),
    EXPLODING_CROSSBOW("exploding_crossbow", true, 930);

    public final String id;
    public final boolean crossbow;
    public final int durability;

    RangedWeaponKind(String id, boolean crossbow, int durability) {
        this.id = id;
        this.crossbow = crossbow;
        this.durability = durability;
    }
}
