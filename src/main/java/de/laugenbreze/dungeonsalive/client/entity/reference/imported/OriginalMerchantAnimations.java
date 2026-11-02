package de.laugenbreze.dungeonsalive.client.entity.reference.imported;
import net.minecraft.client.animation.*;
/** Original small greeting; source CEM idle/garment motion remains independent. */
public final class OriginalMerchantAnimations {
    public static final AnimationDefinition GREETING=AnimationDefinition.Builder.withLength(1.5F)
        .addAnimation("__cem_host_head",new AnimationChannel(AnimationChannel.Targets.ROTATION,
            new Keyframe(0,KeyframeAnimations.degreeVec(0,0,0),AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(.35F,KeyframeAnimations.degreeVec(12,0,0),AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(.75F,KeyframeAnimations.degreeVec(-4,0,0),AnimationChannel.Interpolations.CATMULLROM),
            new Keyframe(1.5F,KeyframeAnimations.degreeVec(0,0,0),AnimationChannel.Interpolations.CATMULLROM)))
        .build();
}
