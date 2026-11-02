package de.laugenbreze.dungeonsalive.client.entity.reference.imported;

import net.minecraft.client.animation.*;

/** Own MIT captive and rescue gestures on the retained community blacksmith rig. */
public final class OriginalCivilianAnimations {
    private OriginalCivilianAnimations() {}
    private static Keyframe rotation(float t, float x, float y, float z) {
        return new Keyframe(t, KeyframeAnimations.degreeVec(x, y, z), AnimationChannel.Interpolations.CATMULLROM);
    }
    public static final AnimationDefinition CAPTIVE = AnimationDefinition.Builder.withLength(4.0F).looping()
            .addAnimation("__cem_host_head", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    rotation(0, 12, -7, 0), rotation(1, 16, 0, 2), rotation(2, 12, 7, 0),
                    rotation(3, 10, 0, -2), rotation(4, 12, -7, 0)))
            .addAnimation("__cem_host_arms", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    rotation(0, -7, 0, 0), rotation(2, -10, 0, 0), rotation(4, -7, 0, 0))).build();
    public static final AnimationDefinition FREED = AnimationDefinition.Builder.withLength(2.0F)
            .addAnimation("__cem_host_head", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    rotation(0, 12, 0, 0), rotation(.45F, -12, 0, 0), rotation(1, 9, 0, 0), rotation(1.5F, -3, 0, 0), rotation(2, 0, 0, 0)))
            .addAnimation("__cem_host_arms", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    rotation(0, -7, 0, 0), rotation(.6F, -16, 0, 0), rotation(2, 0, 0, 0))).build();
}
