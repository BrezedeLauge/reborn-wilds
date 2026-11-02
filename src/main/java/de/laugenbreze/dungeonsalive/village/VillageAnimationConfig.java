package de.laugenbreze.dungeonsalive.village;

import net.neoforged.neoforge.common.ModConfigSpec;

/** Registered as its own client config; no client-only class references. */
public final class VillageAnimationConfig {
    public static final ModConfigSpec SPEC;
    public static final ModConfigSpec.BooleanValue ENABLED;
    public static final ModConfigSpec.BooleanValue PREFER_EXTERNAL_MODELS;
    public static final ModConfigSpec.BooleanValue WORK_PROPS;
    public static final ModConfigSpec.DoubleValue INTENSITY;
    static {
        var builder = new ModConfigSpec.Builder();
        builder.push("villagers");
        ENABLED = builder.comment("Enable Reborn Wilds's villager renderer. Restart the client after changing.")
                .define("enabled", true);
        PREFER_EXTERNAL_MODELS = builder.comment("Leave the villager renderer to Entity Model Features / OptiFine when installed.",
                        "Keep enabled for Fresh Animations. Restart the client after changing.")
                .define("preferExternalModels", true);
        WORK_PROPS = builder.comment("Render visual tools during actual server-reported work.",
                        "These visual props are not items in the villager's inventory.")
                .define("workProps", true);
        INTENSITY = builder.comment("Strength of extra body motion. Vanilla look and locomotion remain.")
                .defineInRange("intensity", 1.0, 0.0, 1.5);
        builder.pop();
        SPEC = builder.build();
    }
    private VillageAnimationConfig() {}
}
