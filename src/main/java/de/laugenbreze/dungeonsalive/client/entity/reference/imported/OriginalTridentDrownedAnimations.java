package de.laugenbreze.dungeonsalive.client.entity.reference.imported;

import net.minecraft.client.animation.*;

/** Own MIT overhead throwing motion; a real server trident leaves at the 1.2-second source marker. */
public final class OriginalTridentDrownedAnimations {
    private OriginalTridentDrownedAnimations() {}
    private static Keyframe r(float t, float x, float y, float z) {
        return new Keyframe(t, KeyframeAnimations.degreeVec(x, y, z), AnimationChannel.Interpolations.CATMULLROM);
    }
    public static final AnimationDefinition THROW = AnimationDefinition.Builder.withLength(1.7F)
            .addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    r(0, -10, 0, 3), r(.35F, -90, 0, 8), r(.9F, -185, 8, 5),
                    r(1.05F, -195, 5, 3), r(1.2F, -70, -8, 0), r(1.4F, -20, 0, 3), r(1.7F, -10, 0, 3)))
            .addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    r(0, -8, 0, -3), r(.9F, -30, -10, -12), r(1.2F, -12, 4, -6), r(1.7F, -8, 0, -3)))
            .build();
}
