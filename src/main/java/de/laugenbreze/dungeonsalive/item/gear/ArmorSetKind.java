package de.laugenbreze.dungeonsalive.item.gear;

/** Explicit Minecraft balance, inspired by the source sets rather than imported original-game numbers. */
public enum ArmorSetKind {
    WOLF("wolf", 18, 2, 6, 5, 2, 1.20F, 0, 1, 0),
    SPIDER("spider", 15, 2, 5, 4, 1, 1, .25, 1, .06F),
    BATTLE_ROBES("battle_robes", 12, 1, 4, 3, 2, 1.15F, 0, .8, 0);

    public final String id;
    public final int durabilityMultiplier, helmetDefense, chestDefense, legDefense, bootDefense;
    public final float meleeMultiplier, lifeStealFraction;
    public final double attackSpeedBonus, artifactCooldownMultiplier;
    ArmorSetKind(String id, int durabilityMultiplier, int helmetDefense, int chestDefense, int legDefense, int bootDefense,
                 float meleeMultiplier, double attackSpeedBonus, double artifactCooldownMultiplier, float lifeStealFraction) {
        this.id = id; this.durabilityMultiplier = durabilityMultiplier;
        this.helmetDefense = helmetDefense; this.chestDefense = chestDefense;
        this.legDefense = legDefense; this.bootDefense = bootDefense;
        this.meleeMultiplier = meleeMultiplier; this.attackSpeedBonus = attackSpeedBonus;
        this.artifactCooldownMultiplier = artifactCooldownMultiplier; this.lifeStealFraction = lifeStealFraction;
    }
    public int artifactCooldown(int originalTicks) {
        return originalTicks <= 0 ? 0 : Math.max(1, (int)Math.ceil(originalTicks * artifactCooldownMultiplier));
    }
    public float lifeSteal(float healthDamage) {
        return Float.isFinite(healthDamage) && healthDamage > 0 ? Math.min(2, healthDamage * lifeStealFraction) : 0;
    }
}
