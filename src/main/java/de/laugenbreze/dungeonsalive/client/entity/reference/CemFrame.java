package de.laugenbreze.dungeonsalive.client.entity.reference;

import de.laugenbreze.dungeonsalive.entity.AttackKind;
import org.jetbrains.annotations.Nullable;

/** All varying inputs to a CEM pose. Rendering one entity cannot retain another entity's variables. */
record CemFrame(float age, float worldTime, float limbSwing, float limbSpeed, float headYaw, float headPitch,
                float hurtTicks, float deathTicks, float health, float maxHealth,
                boolean inWater, boolean onGround, boolean aggressive, @Nullable AttackKind action, float actionTicks,
                @Nullable ReferenceAnimationSource.Gesture gesture, boolean ridden, boolean sitting, boolean riding, boolean baby,
                boolean swimming) {
    /** Existing source fixtures and detached land previews do not invent a swimming state. */
    CemFrame(float age, float worldTime, float limbSwing, float limbSpeed, float headYaw, float headPitch,
             float hurtTicks, float deathTicks, float health, float maxHealth,
             boolean inWater, boolean onGround, boolean aggressive, @Nullable AttackKind action, float actionTicks,
             @Nullable ReferenceAnimationSource.Gesture gesture, boolean ridden, boolean sitting, boolean riding, boolean baby) {
        this(age, worldTime, limbSwing, limbSpeed, headYaw, headPitch, hurtTicks, deathTicks, health, maxHealth,
                inWater, onGround, aggressive, action, actionTicks, gesture, ridden, sitting, riding, baby, false);
    }
    static CemFrame rest() { return new CemFrame(0, 0, 0, 0, 0, 0, 0, 0, 1, 1, false, true, false, null, 0, null, false, false, false, false); }
    double input(String name) {
        return switch (name) {
            case "age" -> age; case "time" -> worldTime;
            case "limb_swing" -> limbSwing; case "limb_speed" -> limbSpeed;
            case "head_yaw" -> headYaw; case "head_pitch" -> headPitch;
            case "hurt_time" -> hurtTicks; case "death_time" -> deathTicks;
            case "health" -> health; case "max_health" -> maxHealth;
            case "swing_progress" -> action == null ? 0 : Math.max(0, Math.min(1, actionTicks / action.duration()));
            case "is_in_water" -> inWater ? 1 : 0; case "is_on_ground" -> onGround ? 1 : 0;
            case "is_swimming" -> inWater && swimming ? 1 : 0;
            case "is_alive" -> health > 0 && deathTicks == 0 ? 1 : 0;
            case "is_aggressive" -> aggressive ? 1 : 0;
            case "is_ridden" -> ridden ? 1 : 0; case "is_riding" -> riding ? 1 : 0;
            case "is_sitting" -> sitting ? 1 : 0; case "is_child" -> baby ? 1 : 0;
            default -> throw new IllegalArgumentException("Unsupported CEM input '" + name + "'");
        };
    }
    static boolean supports(String name) {
        return switch (name) {
            case "age", "time", "limb_swing", "limb_speed", "head_yaw", "head_pitch", "hurt_time", "death_time",
                 "health", "max_health", "swing_progress", "is_in_water", "is_swimming", "is_on_ground", "is_alive", "is_aggressive",
                 "is_ridden", "is_riding", "is_sitting", "is_child" -> true;
            default -> false;
        };
    }
}
