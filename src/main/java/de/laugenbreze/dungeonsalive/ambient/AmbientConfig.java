package de.laugenbreze.dungeonsalive.ambient;

import net.neoforged.neoforge.common.ModConfigSpec;

/** Client preferences, deliberately free of client-class references. */
public final class AmbientConfig {
    public static final ModConfigSpec SPEC;
    public static final ModConfigSpec.BooleanValue ENABLED;
    public static final ModConfigSpec.BooleanValue BUTTERFLIES;
    public static final ModConfigSpec.BooleanValue FIREFLIES;
    public static final ModConfigSpec.BooleanValue SPORES;
    public static final ModConfigSpec.IntValue MAX_PARTICLES;
    public static final ModConfigSpec.IntValue SPAWN_RADIUS;
    public static final ModConfigSpec.DoubleValue DENSITY;
    public static final ModConfigSpec.BooleanValue LEAF_MOTES;
    public static final ModConfigSpec.BooleanValue WATERFALL_MIST;

    static {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();
        builder.comment("Local visual ambience. No world generation or gameplay changes.")
                .push("ambience");
        ENABLED = builder.comment("Enable automatic ambience around this player.")
                .define("enabled", true);
        BUTTERFLIES = builder.comment("Animated butterflies in forests, meadows and plains during daylight.")
                .define("butterflies", true);
        FIREFLIES = builder.comment("Pulsing fireflies in forests, jungles and wetlands after dusk.")
                .define("fireflies", true);
        SPORES = builder.comment("Occasional drifting motes in forests and jungles.")
                .define("spores", true);
        LEAF_MOTES = builder.comment("Small falling fragments beneath actual loaded leaf blocks; installed Minecraft block particles.")
                .define("leafMotes", true);
        WATERFALL_MIST = builder.comment("Small mist/splash particles where falling water meets a water pool.")
                .define("waterfallMist", true);
        MAX_PARTICLES = builder.comment("Maximum particles created by the automatic ambience spawner.",
                        "Minecraft's Decreased setting halves this budget; Minimal disables automatic ambience.")
                .defineInRange("maxParticles", 96, 0, 384);
        SPAWN_RADIUS = builder.comment("Maximum horizontal distance of new ambience, in blocks.")
                .defineInRange("spawnRadius", 18, 4, 32);
        DENSITY = builder.comment("Spawn probability multiplier; 0 disables automatic spawning.",
                        "The hard particle limit remains in effect at every density.")
                .defineInRange("density", 1.0, 0.0, 3.0);
        builder.pop();
        SPEC = builder.build();
    }

    private AmbientConfig() {}
}
