/* Original keyed animation authored for Dungeons Alive, 2026. Project MIT license.
 * The viewed Dungeons reference establishes the segmented silhouette, not these keyframes.
 * No original game animation extraction and no renamed Goety clip.
 * Server action timing is bound separately using BEAM_HIT_SECONDS / SLAM_HIT_SECONDS.
 */
package de.laugenbreze.dungeonsalive.client.entity.reference.imported;

import net.minecraft.client.animation.AnimationChannel;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.animation.Keyframe;
import net.minecraft.client.animation.KeyframeAnimations;
import org.joml.Vector3f;

/** Independent segment motion, grounded quadruped gait, crown aperture and collapse. */
public final class OriginalVengefulHeartAnimations {
    private OriginalVengefulHeartAnimations() {}

    public static final float BEAM_HIT_SECONDS = 1.8F;
    public static final float SLAM_HIT_SECONDS = 1.5F;

    public static final AnimationDefinition IDLE = idle();
    public static final AnimationDefinition WALK = walk();
    public static final AnimationDefinition BEAM = beam();
    public static final AnimationDefinition HEAVY_SLAM = slam();
    public static final AnimationDefinition DEATH = death();

    private static AnimationDefinition idle() {
        var b = AnimationDefinition.Builder.withLength(6.0F).looping();
        p(b, "body", k(0,0,0,0), k(1.5F,0,.18F,0), k(3,0,0,0), k(4.5F,0,.12F,0), k(6,0,0,0));
        r(b, "body_middle", k(0,0,0,0), k(1.5F,0,-.8F,0), k(3,0,0,0), k(4.5F,0,.8F,0), k(6,0,0,0));
        r(b, "body_rear", k(0,0,0,0), k(1.5F,0,1.2F,0), k(3,0,0,0), k(4.5F,0,-1.2F,0), k(6,0,0,0));
        r(b, "neck_base", k(0,0,0,0), k(1.5F,.8F,1.3F,.3F), k(3,0,0,0), k(4.5F,-.6F,-1.1F,-.3F), k(6,0,0,0));
        r(b, "neck_1", k(0,0,0,0), k(1.5F,-.7F,-1.8F,-.4F), k(3,0,0,0), k(4.5F,.8F,1.5F,.3F), k(6,0,0,0));
        r(b, "neck_2", k(0,0,0,0), k(1.5F,1.0F,2.0F,.4F), k(3,0,0,0), k(4.5F,-.8F,-1.6F,-.3F), k(6,0,0,0));
        r(b, "neck_3", k(0,0,0,0), k(1.5F,-.9F,-1.6F,-.3F), k(3,0,0,0), k(4.5F,.8F,1.6F,.3F), k(6,0,0,0));
        r(b, "neck_4", k(0,0,0,0), k(1.5F,.8F,1.2F,.2F), k(3,0,0,0), k(4.5F,-.8F,-1.2F,-.2F), k(6,0,0,0));
        r(b, "head", k(0,0,0,0), k(1.5F,-1.6F,-1.6F,0), k(3,0,0,0), k(4.5F,1.3F,1.2F,0), k(6,0,0,0));
        r(b, "jaw", k(0,0,0,0), k(1.5F,3,0,0), k(3,1,0,0), k(4.5F,2.2F,0,0), k(6,0,0,0));
        r(b, "left_horn", k(0,0,0,0), k(1.5F,0,0,-1.4F), k(3,0,0,0), k(4.5F,0,0,.8F), k(6,0,0,0));
        r(b, "right_horn", k(0,0,0,0), k(1.5F,0,0,1.4F), k(3,0,0,0), k(4.5F,0,0,-.8F), k(6,0,0,0));
        for (String flame : new String[]{"left_flame", "right_flame", "crown_flame"}) {
            float delta = flame.equals("right_flame") ? -.10F : .10F;
            s(b, flame, k(0,1,1,1), k(1.5F,1-delta*.3F,1+delta,1), k(3,1,1,1),
                    k(4.5F,1+delta*.4F,1-delta*.6F,1), k(6,1,1,1));
        }
        return b.build();
    }

    private static AnimationDefinition walk() {
        var b = AnimationDefinition.Builder.withLength(2.0F).looping();
        gait(b, "front_left", false);
        gait(b, "back_right", false);
        gait(b, "front_right", true);
        gait(b, "back_left", true);
        p(b, "body", k(0,0,0,0), k(.5F,0,.35F,0), k(1,0,0,0), k(1.5F,0,.35F,0), k(2,0,0,0));
        r(b, "body", k(0,0,-.9F,-.6F), k(.5F,0,0,0), k(1,0,.9F,.6F), k(1.5F,0,0,0), k(2,0,-.9F,-.6F));
        r(b, "body_middle", k(0,0,2,0), k(.5F,0,0,0), k(1,0,-2,0), k(1.5F,0,0,0), k(2,0,2,0));
        r(b, "body_rear", k(0,0,-3,0), k(.5F,0,0,0), k(1,0,3,0), k(1.5F,0,0,0), k(2,0,-3,0));
        r(b, "neck_base", k(0,1.8F,.7F,.5F), k(.5F,.3F,0,0), k(1,-.6F,-.7F,-.5F), k(1.5F,.3F,0,0), k(2,1.8F,.7F,.5F));
        r(b, "neck_1", k(0,-1.2F,-1.5F,0), k(.5F,0,0,0), k(1,1.2F,1.5F,0), k(1.5F,0,0,0), k(2,-1.2F,-1.5F,0));
        r(b, "neck_2", k(0,.9F,2,0), k(.5F,0,0,0), k(1,-.9F,-2,0), k(1.5F,0,0,0), k(2,.9F,2,0));
        r(b, "neck_3", k(0,-.6F,-1.7F,0), k(.5F,0,0,0), k(1,.6F,1.7F,0), k(1.5F,0,0,0), k(2,-.6F,-1.7F,0));
        r(b, "neck_4", k(0,.5F,1,0), k(.5F,0,0,0), k(1,-.5F,-1,0), k(1.5F,0,0,0), k(2,.5F,1,0));
        r(b, "head", k(0,-1.4F,0,0), k(.5F,.3F,0,0), k(1,.6F,0,0), k(1.5F,.3F,0,0), k(2,-1.4F,0,0));
        r(b, "jaw", k(0,1,0,0), k(.5F,3.2F,0,0), k(1,1,0,0), k(1.5F,3.2F,0,0), k(2,1,0,0));
        return b.build();
    }

    private static void gait(AnimationDefinition.Builder b, String leg, boolean opposite) {
        float sign = opposite ? -1 : 1;
        r(b, leg+"_leg", k(0,14*sign,0,0), k(.5F,0,0,0), k(1,-14*sign,0,0), k(1.5F,0,0,0), k(2,14*sign,0,0));
        r(b, leg+"_foot", k(0,-9*sign,0,0), k(.5F,0,0,0), k(1,9*sign,0,0), k(1.5F,0,0,0), k(2,-9*sign,0,0));
        p(b, leg+"_leg", k(0,0,.18F,0), k(.5F,0,opposite ? 0 : 1.25F,0), k(1,0,.18F,0),
                k(1.5F,0,opposite ? 1.25F : 0,0), k(2,0,.18F,0));
    }

    private static AnimationDefinition beam() {
        var b = AnimationDefinition.Builder.withLength(3.0F);
        r(b,"body_front",k(0,0,0,0),k(.6F,-1,0,0),k(1.4F,-2,0,0),k(1.8F,1,0,0),k(2.35F,1,0,0),k(3,0,0,0));
        r(b,"neck_base",k(0,0,0,0),k(.7F,-3,-5,0),k(1.45F,-4,-8,0),k(1.8F,3,0,0),k(2.4F,3,0,0),k(3,0,0,0));
        r(b,"neck_1",k(0,0,0,0),k(.7F,-2,11,0),k(1.45F,-4,17,0),k(1.8F,2,0,0),k(2.4F,1,0,0),k(3,0,0,0));
        r(b,"neck_2",k(0,0,0,0),k(.7F,-2,-13,0),k(1.45F,-3,-22,0),k(1.8F,1,0,0),k(2.4F,0,0,0),k(3,0,0,0));
        r(b,"neck_3",k(0,0,0,0),k(.7F,-1,14,0),k(1.45F,-2,23,0),k(1.8F,1,0,0),k(2.4F,0,0,0),k(3,0,0,0));
        r(b,"neck_4",k(0,0,0,0),k(.7F,-2,-7,0),k(1.45F,-2,-10,0),k(1.8F,1,0,0),k(2.4F,0,0,0),k(3,0,0,0));
        r(b,"head",k(0,0,0,0),k(.7F,-10,0,0),k(1.45F,-12,0,0),k(1.8F,8,0,0),k(2.4F,8,0,0),k(3,0,0,0));
        r(b,"jaw",k(0,0,0,0),k(.7F,10,0,0),k(1.4F,17,0,0),k(1.8F,24,0,0),k(2.4F,22,0,0),k(3,0,0,0));
        r(b,"left_horn",k(0,0,0,0),k(1.4F,0,-2,-4),k(1.8F,0,0,-2),k(2.4F,0,0,-2),k(3,0,0,0));
        r(b,"right_horn",k(0,0,0,0),k(1.4F,0,2,4),k(1.8F,0,0,2),k(2.4F,0,0,2),k(3,0,0,0));
        for(String flame : new String[]{"left_flame","right_flame","crown_flame"})
            s(b,flame,k(0,1,1,1),k(.7F,1.03F,1.08F,1),k(1.4F,1.08F,1.24F,1),k(1.8F,.9F,.82F,1),k(2.3F,1.05F,1.15F,1),k(3,1,1,1));
        return b.build();
    }

    private static AnimationDefinition slam() {
        var b = AnimationDefinition.Builder.withLength(2.6F);
        p(b,"body",k(0,0,0,0),k(.6F,0,.6F,0),k(1.25F,0,1.4F,0),k(1.5F,0,-.6F,0),k(1.7F,0,.25F,0),k(2.6F,0,0,0));
        r(b,"body",k(0,0,0,0),k(.6F,-2,0,0),k(1.25F,-4,0,0),k(1.5F,3,0,0),k(1.7F,1,0,0),k(2.6F,0,0,0));
        for(String leg : new String[]{"front_left_leg","front_right_leg"}) {
            r(b,leg,k(0,0,0,0),k(.6F,-6,0,0),k(1.25F,-16,0,0),k(1.5F,1,0,0),k(1.7F,0,0,0),k(2.6F,0,0,0));
            p(b,leg,k(0,0,0,0),k(.6F,0,1,0),k(1.25F,0,3,0),k(1.5F,0,0,0),k(2.6F,0,0,0));
        }
        r(b,"neck_base",k(0,0,0,0),k(.65F,-5,-2,0),k(1.25F,-8,-3,0),k(1.5F,12,0,0),k(1.75F,8,0,0),k(2.6F,0,0,0));
        r(b,"neck_1",k(0,0,0,0),k(.65F,-4,4,0),k(1.25F,-7,5,0),k(1.5F,8,0,0),k(1.75F,4,0,0),k(2.6F,0,0,0));
        r(b,"neck_2",k(0,0,0,0),k(.65F,-3,-4,0),k(1.25F,-5,-6,0),k(1.5F,6,0,0),k(1.75F,3,0,0),k(2.6F,0,0,0));
        r(b,"neck_3",k(0,0,0,0),k(.65F,-2,3,0),k(1.25F,-3,4,0),k(1.5F,4,0,0),k(1.75F,1,0,0),k(2.6F,0,0,0));
        r(b,"neck_4",k(0,0,0,0),k(.65F,-2,-1,0),k(1.25F,-2,-1,0),k(1.5F,2,0,0),k(1.75F,-1,0,0),k(2.6F,0,0,0));
        r(b,"head",k(0,0,0,0),k(.65F,-8,0,0),k(1.25F,-12,0,0),k(1.5F,12,0,0),k(1.75F,-4,0,0),k(2.6F,0,0,0));
        r(b,"jaw",k(0,0,0,0),k(.65F,7,0,0),k(1.25F,15,0,0),k(1.5F,5,0,0),k(1.75F,9,0,0),k(2.6F,0,0,0));
        r(b,"left_horn",k(0,0,0,0),k(1.25F,0,0,-3),k(1.5F,0,0,4),k(1.75F,0,0,-2),k(2.6F,0,0,0));
        r(b,"right_horn",k(0,0,0,0),k(1.25F,0,0,3),k(1.5F,0,0,-4),k(1.75F,0,0,2),k(2.6F,0,0,0));
        return b.build();
    }

    private static AnimationDefinition death() {
        var b=AnimationDefinition.Builder.withLength(3.5F);
        r(b,"body",k(0,0,0,0),k(.45F,2,0,3),k(.85F,-2,0,-3),k(1.35F,0,0,5),k(2.3F,3,0,12),k(3.5F,4,0,16));
        p(b,"body",k(0,0,0,0),k(.85F,0,.3F,0),k(1.35F,0,-1.3F,0),k(2.3F,0,-4.5F,0),k(3.5F,0,-5.5F,0));
        String[] neck={"neck_base","neck_1","neck_2","neck_3","neck_4"};
        for(int i=0;i<neck.length;i++) {
            float sign=i%2==0 ? 1 : -1;
            r(b,neck[i],k(0,0,0,0),k(.45F,2*sign,5*sign,1.5F*sign),k(.85F,-2*sign,-8*sign,-2*sign),
                    k(1.35F,3*sign,14*sign,4),k(2.3F,7+i*2,28*sign,8+i*3),k(3.5F,12+i*3,45*sign,14+i*4));
            p(b,neck[i],k(0,0,0,0),k(.85F,0,.4F,0),k(1.35F,.25F*sign,.7F,0),
                    k(2.3F,(i+1)*.9F*sign,1.2F,0),k(3.5F,(i+1)*1.6F*sign,-2,0));
            s(b,neck[i],k(0,1,1,1),k(2.3F,1,1,1),k(3.1F,.75F,.75F,.75F),k(3.5F,.05F,.05F,.05F));
        }
        r(b,"head",k(0,0,0,0),k(.45F,-5,0,0),k(.85F,3,0,-4),k(1.35F,-8,0,4),k(2.3F,20,-15,22),k(3.5F,35,-30,35));
        r(b,"jaw",k(0,0,0,0),k(.45F,10,0,0),k(.85F,18,0,0),k(1.35F,25,0,0),k(2.3F,30,0,0),k(3.5F,35,0,0));
        r(b,"body_rear",k(0,0,0,0),k(1.35F,0,12,-6),k(2.3F,2,22,-10),k(3.5F,5,35,-15));
        for(String leg:new String[]{"front_left_leg","front_right_leg","back_left_leg","back_right_leg"}) {
            float side=leg.contains("left")?1:-1;
            r(b,leg,k(0,0,0,0),k(.85F,0,0,5*side),k(1.35F,-5,0,12*side),k(2.3F,-12,0,25*side),k(3.5F,-15,0,32*side));
        }
        for(String flame:new String[]{"left_flame","right_flame","crown_flame"})
            s(b,flame,k(0,1,1,1),k(.45F,1.1F,1.2F,1),k(.85F,.9F,.8F,1),k(1.35F,1.1F,1.25F,1),k(2.3F,.6F,.5F,1),k(3.5F,.02F,.02F,.02F));
        s(b,"body",k(0,1,1,1),k(2.8F,1,1,1),k(3.5F,.1F,.1F,.1F));
        return b.build();
    }

    private static float[] k(float time,float x,float y,float z) { return new float[]{time,x,y,z}; }
    private static void r(AnimationDefinition.Builder b,String name,float[]... frames) {
        channel(b,name,AnimationChannel.Targets.ROTATION,frames,0);
    }
    private static void p(AnimationDefinition.Builder b,String name,float[]... frames) {
        channel(b,name,AnimationChannel.Targets.POSITION,frames,1);
    }
    private static void s(AnimationDefinition.Builder b,String name,float[]... frames) {
        channel(b,name,AnimationChannel.Targets.SCALE,frames,2);
    }
    private static void channel(AnimationDefinition.Builder b,String name,AnimationChannel.Target target,float[][] frames,int type) {
        Keyframe[] result=new Keyframe[frames.length];
        for(int i=0;i<frames.length;i++) {
            float[] f=frames[i];
            Vector3f value=switch(type) {
                case 0 -> KeyframeAnimations.degreeVec(f[1],f[2],f[3]);
                case 1 -> KeyframeAnimations.posVec(f[1],f[2],f[3]);
                default -> KeyframeAnimations.scaleVec(f[1],f[2],f[3]);
            };
            result[i]=new Keyframe(f[0],value,AnimationChannel.Interpolations.CATMULLROM);
        }
        b.addAnimation(name,new AnimationChannel(target,result));
    }
}
