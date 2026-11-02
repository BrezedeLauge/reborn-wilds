package de.laugenbreze.dungeonsalive.world.caravan;

import net.neoforged.neoforge.common.ModConfigSpec;

public final class CaravanConfig {
    public static final ModConfigSpec SPEC;
    public static final ModConfigSpec.BooleanValue ENABLED;
    static {
        var builder = new ModConfigSpec.Builder();
        ENABLED = builder.comment("Rare Overworld trader caravans in already loaded open terrain.",
                        "At most two persistent groups per dimension, including unloaded groups. Existing groups remain when disabled.",
                        "One trader and two claimable companions travel a short safe route. No teleport, chunk tickets or terrain edits.")
                .define("caravans", true);
        SPEC = builder.build();
    }
    private CaravanConfig() {}
}
