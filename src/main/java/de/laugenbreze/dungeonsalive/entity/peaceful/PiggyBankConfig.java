package de.laugenbreze.dungeonsalive.entity.peaceful;

import net.neoforged.neoforge.common.ModConfigSpec;

public final class PiggyBankConfig {
    public static final ModConfigSpec SPEC;
    public static final ModConfigSpec.BooleanValue NATURAL_SPAWNS;
    static {
        var builder = new ModConfigSpec.Builder();
        builder.comment("Rare single treasure pigs. Disabling new spawns keeps existing animals and their treasure.",
                "The general wildlife naturalSpawns toggle and loaded wildlife limit also apply.")
                .push("piggyBank");
        NATURAL_SPAWNS = builder.define("naturalSpawns", true);
        builder.pop(); SPEC = builder.build();
    }
    private PiggyBankConfig() {}
}
