package de.laugenbreze.dungeonsalive.client.entity.reference.imported;

import net.minecraft.client.animation.*;

/** Own fuse and four-leg locomotion poses on the installed CreeperModel. */
public final class InfamousIcyCreeperAnimations {
    private InfamousIcyCreeperAnimations() {}
    public static final AnimationDefinition IDLE=AnimationDefinition.Builder.withLength(3.5F).looping()
            .addAnimation("head",rotation(new float[][]{{0,0,0,0},{1.75F,1.2F,0,0},{3.5F,0,0,0}})).build();
    public static final AnimationDefinition WALK=walk();
    public static final AnimationDefinition FUSE=fuse();
    private static AnimationDefinition walk() {
        var b=AnimationDefinition.Builder.withLength(1.2F).looping();
        for(String bone:new String[]{"right_hind_leg","left_hind_leg","right_front_leg","left_front_leg"}) {
            float sign=bone.equals("right_hind_leg")||bone.equals("left_front_leg")?1:-1;
            b.addAnimation(bone,rotation(new float[][]{{0,35*sign,0,0},{.3F,0,0,0},{.6F,-35*sign,0,0},{.9F,0,0,0},{1.2F,35*sign,0,0}}));
        }
        return b.build();
    }
    private static AnimationDefinition fuse() {
        var b=AnimationDefinition.Builder.withLength(1.7F);
        for(String bone:new String[]{"head","body"})
            b.addAnimation(bone,new AnimationChannel(AnimationChannel.Targets.SCALE,
                    scale(0,1,1,1),scale(.4F,1.025F,.99F,1.025F),scale(.8F,1.055F,.98F,1.055F),
                    scale(1.1F,1.09F,.97F,1.09F),scale(1.25F,1.16F,.95F,1.16F),scale(1.7F,1,1,1)));
        b.addAnimation("head",rotation(new float[][]{{0,0,0,0},{.4F,-2,0,-1},{.8F,-3,0,1},{1.1F,-4,0,-1.5F},{1.25F,-6,0,0},{1.7F,0,0,0}}));
        return b.build();
    }
    private static Keyframe scale(float t,float x,float y,float z) {
        return new Keyframe(t,KeyframeAnimations.scaleVec(x,y,z),AnimationChannel.Interpolations.LINEAR);
    }
    private static AnimationChannel rotation(float[][] values) {
        Keyframe[] k=new Keyframe[values.length];
        for(int i=0;i<values.length;i++)k[i]=new Keyframe(values[i][0],KeyframeAnimations.degreeVec(values[i][1],values[i][2],values[i][3]),AnimationChannel.Interpolations.CATMULLROM);
        return new AnimationChannel(AnimationChannel.Targets.ROTATION,k);
    }
}
