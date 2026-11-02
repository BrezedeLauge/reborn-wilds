package de.laugenbreze.dungeonsalive.entity.peaceful;

import net.neoforged.neoforge.common.ModConfigSpec;

/** Server decisions for natural wildlife; never changes existing pet ownership or terrain. */
public final class WildlifeConfig {
    public static final ModConfigSpec SPEC;
    public static final ModConfigSpec.BooleanValue NATURAL_SPAWNS;
    public static final ModConfigSpec.BooleanValue WOOLY_COWS;
    public static final ModConfigSpec.BooleanValue VULTURES;
    public static final ModConfigSpec.BooleanValue WISPS;
    public static final ModConfigSpec.IntValue NEARBY_LIMIT;
    public static final ModConfigSpec.BooleanValue EXPLORATION_ENCOUNTERS;
    public static final ModConfigSpec.BooleanValue JUNGLE_WILDLIFE;
    public static final ModConfigSpec.BooleanValue FOREST_BIRDS;
    public static final ModConfigSpec.IntValue ENCOUNTER_INTERVAL;
    public static final ModConfigSpec.IntValue LOADED_LIMIT;

    static {
        var builder = new ModConfigSpec.Builder();
        builder.comment("Natural wildlife in suitable biomes. Decisions use the server's settings.",
                "Existing creatures are retained when spawning is disabled; group AI remains active.",
                "Normal Minecraft creature caps and collision checks still apply.").push("wildlife");
        NATURAL_SPAWNS = builder.comment("Allow new natural herds and flocks. No extra datapack is required.")
                .define("naturalSpawns", true);
        WOOLY_COWS = builder.comment("Wooly cow herds in grassy plains and meadows.").define("woolyCows", true);
        VULTURES = builder.comment("Vulture flight groups in deserts, badlands and savannas.").define("vultures", true);
        WISPS = builder.comment("Wisp groups in forests and wetlands after dusk. This habitat is a sandbox adaptation.")
                .define("wisps", true);
        NEARBY_LIMIT = builder.comment("Additional limit on loaded Dungeons wildlife within 48 blocks of live natural spawning.",
                        "This counts wildlife, including persistent wildlife, but not pets or merchants.",
                        "Initial chunk generation keeps Minecraft's pack limits and never queries live entity sections.")
                .defineInRange("nearbyLimit", 24, 6, 64);
        EXPLORATION_ENCOUNTERS = builder.comment("Populate suitable, already loaded habitats during exploration, including existing chunks.",
                        "Uses its own strictly bounded wildlife budget instead of waiting for the vanilla passive-animal cap.",
                        "Respects naturalSpawns, doMobSpawning, per-area cooldowns, collision, and individual species switches.")
                .define("explorationEncounters", true);
        JUNGLE_WILDLIFE = builder.comment("Unowned toucan flocks and red panda groups in jungles; existing pets keep their ownership.")
                .define("jungleWildlife", true);
        FOREST_BIRDS = builder.comment("Unowned raven flocks in forests, using the existing raven model.")
                .define("forestBirds", true);
        ENCOUNTER_INTERVAL = builder.comment("Minimum ticks between habitat-population attempts for one player.",
                        "At most one player is processed per 40 server ticks; crowded habitats are left alone.")
                .defineInRange("encounterInterval", 200, 100, 2400);
        LOADED_LIMIT = builder.comment("Hard cap on loaded unowned Dungeons wildlife for exploration encounters per dimension.",
                        "The nearbyLimit additionally applies within 64 blocks. No animals are deleted to meet a cap.")
                .defineInRange("loadedLimit", 96, 12, 192);
        builder.pop();
        SPEC = builder.build();
    }

    private WildlifeConfig() {}
}
