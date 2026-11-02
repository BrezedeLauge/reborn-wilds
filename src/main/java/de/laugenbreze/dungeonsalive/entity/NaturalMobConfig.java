package de.laugenbreze.dungeonsalive.entity;

import net.neoforged.neoforge.common.ModConfigSpec;

/** Server-authoritative switch for ordinary natural opponents, independent of boss encounters. */
public final class NaturalMobConfig {
    public static final ModConfigSpec SPEC;
    public static final ModConfigSpec.BooleanValue NATURAL_HOSTILES;

    static {
        var builder = new ModConfigSpec.Builder();
        builder.comment("Curated Dungeons opponents use normal Minecraft MONSTER spawning and caps.",
                "No extra datapack is required. Bosses and minibosses are never added by these biome entries.",
                "Disabling new natural spawns retains existing mobs and manual/siege/summon encounters.")
                .push("hostiles");
        NATURAL_HOSTILES = builder.comment("Allow natural Dungeons opponents in suitable biomes.",
                        "Overworld spawns require normal monster darkness. Nether and End use the native any-light monster rule.",
                        "Aquatic opponents additionally require water around their full spawn body and free collision space.")
                .define("naturalHostiles", true);
        builder.pop();
        SPEC = builder.build();
    }

    private NaturalMobConfig() {}
}
