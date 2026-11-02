/* Dungeons Alive animation adapters. Wraith posture follows Goety's procedural
 * WraithModel setupAnim (MIT, copyright 2023 Polarice3), represented here as
 * inspectable keyframe loops. These are adaptations, not Dungeons game clips. */
package de.laugenbreze.dungeonsalive.client.entity.reference.imported;

import net.minecraft.client.animation.*;

public final class ReferenceSupplementAnimations {
    private ReferenceSupplementAnimations() {}
    public static final AnimationDefinition WRAITH_IDLE = AnimationDefinition.Builder.withLength(4F).looping()
        .addAnimation("Ghost", new AnimationChannel(AnimationChannel.Targets.POSITION,
            new Keyframe(0F, KeyframeAnimations.posVec(0, 0, 0), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(1F, KeyframeAnimations.posVec(0, .8F, 0), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(2F, KeyframeAnimations.posVec(0, 0, 0), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(3F, KeyframeAnimations.posVec(0, -.8F, 0), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(4F, KeyframeAnimations.posVec(0, 0, 0), AnimationChannel.Interpolations.CATMULLROM)))
        .addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
            new Keyframe(0F, KeyframeAnimations.degreeVec(-42, -6, 4), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(2F, KeyframeAnimations.degreeVec(-35, -8, 8), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(4F, KeyframeAnimations.degreeVec(-42, -6, 4), AnimationChannel.Interpolations.CATMULLROM)))
        .addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
            new Keyframe(0F, KeyframeAnimations.degreeVec(-35, 6, -8), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(2F, KeyframeAnimations.degreeVec(-42, 8, -4), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(4F, KeyframeAnimations.degreeVec(-35, 6, -8), AnimationChannel.Interpolations.CATMULLROM)))
        .addAnimation("robe", new AnimationChannel(AnimationChannel.Targets.ROTATION,
            new Keyframe(0F, KeyframeAnimations.degreeVec(3, 0, 0), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(2F, KeyframeAnimations.degreeVec(9, 0, 0), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(4F, KeyframeAnimations.degreeVec(3, 0, 0), AnimationChannel.Interpolations.CATMULLROM)))
        .build();
    public static final AnimationDefinition WRAITH_WALK = AnimationDefinition.Builder.withLength(2F).looping()
        .addAnimation("robe", new AnimationChannel(AnimationChannel.Targets.ROTATION,
            new Keyframe(0F, KeyframeAnimations.degreeVec(30, 0, 0), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(1F, KeyframeAnimations.degreeVec(38, 0, 0), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(2F, KeyframeAnimations.degreeVec(30, 0, 0), AnimationChannel.Interpolations.CATMULLROM)))
        .addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
            new Keyframe(0F, KeyframeAnimations.degreeVec(-45, -6, 12), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(1F, KeyframeAnimations.degreeVec(-38, -6, 15), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(2F, KeyframeAnimations.degreeVec(-45, -6, 12), AnimationChannel.Interpolations.CATMULLROM)))
        .addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
            new Keyframe(0F, KeyframeAnimations.degreeVec(-38, 6, -15), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(1F, KeyframeAnimations.degreeVec(-45, 6, -12), AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(2F, KeyframeAnimations.degreeVec(-38, 6, -15), AnimationChannel.Interpolations.CATMULLROM)))
        .build();
}
