package de.laugenbreze.dungeonsalive.client.entity.reference.imported;

import net.minecraft.client.animation.*;

/** Own MIT squash clips on the audited Goety rig; no original Dungeons keyframes claimed. */
public final class OriginalTropicalSlimeAnimations {
    private OriginalTropicalSlimeAnimations() {}
    private static Keyframe scale(float t, float x, float y, float z) {
        return new Keyframe(t, KeyframeAnimations.scaleVec(x, y, z), AnimationChannel.Interpolations.CATMULLROM);
    }
    public static final AnimationDefinition IDLE = AnimationDefinition.Builder.withLength(2.4F).looping()
            .addAnimation("slime", new AnimationChannel(AnimationChannel.Targets.SCALE,
                    scale(0, 1, 1, 1), scale(1.2F, 1.02F, .96F, 1.02F), scale(2.4F, 1, 1, 1))).build();
    public static final AnimationDefinition WALK = AnimationDefinition.Builder.withLength(1.0F).looping()
            .addAnimation("slime", new AnimationChannel(AnimationChannel.Targets.SCALE,
                    scale(0, 1.1F, .82F, 1.1F), scale(.25F, .92F, 1.18F, .92F),
                    scale(.55F, .96F, 1.08F, .96F), scale(.8F, 1.12F, .8F, 1.12F), scale(1, 1.1F, .82F, 1.1F))).build();
    public static final AnimationDefinition LEAP = AnimationDefinition.Builder.withLength(2.0F)
            .addAnimation("slime", new AnimationChannel(AnimationChannel.Targets.SCALE,
                    scale(0, 1, 1, 1), scale(.9F, 1.2F, .7F, 1.2F), scale(1.2F, .9F, 1.25F, .9F),
                    scale(1.6F, 1.12F, .8F, 1.12F), scale(2, 1, 1, 1))).build();
    public static final AnimationDefinition MELEE = AnimationDefinition.Builder.withLength(1.1F)
            .addAnimation("slime", new AnimationChannel(AnimationChannel.Targets.SCALE,
                    scale(0, 1, 1, 1), scale(.5F, 1.12F, .78F, 1.12F), scale(.7F, .92F, 1.18F, .92F),
                    scale(1.1F, 1, 1, 1))).build();
    public static final AnimationDefinition DEATH = AnimationDefinition.Builder.withLength(1.0F)
            .addAnimation("slime", new AnimationChannel(AnimationChannel.Targets.SCALE,
                    scale(0, 1, 1, 1), scale(.45F, 1.22F, .55F, 1.22F), scale(1, 1.25F, .08F, 1.25F))).build();
}
