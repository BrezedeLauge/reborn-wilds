package de.laugenbreze.dungeonsalive.client.entity.reference.imported;

import net.minecraft.client.animation.*;

/**
 * Own action timing for the installed PiglinModel rig, not claimed as original Dungeons clips.
 * Sleeves/pants/jacket receive matching channels, including during blended attacks. This avoids
 * leaving root-level clothing behind while the actual arms move or fire the held crossbow.
 */
public final class InfamousPiglinAnimations {
    private InfamousPiglinAnimations() {}
    public static final AnimationDefinition IDLE = idle(false);
    public static final AnimationDefinition HUNTER_IDLE = idle(true);
    public static final AnimationDefinition WALK = walk(false);
    public static final AnimationDefinition HUNTER_WALK = walk(true);
    public static final AnimationDefinition MELEE = melee();
    public static final AnimationDefinition CROSSBOW = crossbow();
    public static final AnimationDefinition THROW = throwFungus();

    private static AnimationDefinition idle(boolean hunter) {
        var b=AnimationDefinition.Builder.withLength(4).looping();
        if(hunter) {
            r(b,"right_arm",k(0,-80,-18,0),k(2,-78,-18,1),k(4,-80,-18,0));
            r(b,"left_arm",k(0,-76,34,0),k(2,-74,34,-1),k(4,-76,34,0));
        } else {
            r(b,"right_arm",k(0,-4,0,3),k(2,-1,0,5),k(4,-4,0,3));
            r(b,"left_arm",k(0,1,0,-4),k(2,-2,0,-2),k(4,1,0,-4));
        }
        return b.build();
    }
    private static AnimationDefinition walk(boolean hunter) {
        var b=AnimationDefinition.Builder.withLength(1.2F).looping();
        r(b,"left_leg",k(0,30,0,0),k(.3F,0,0,0),k(.6F,-30,0,0),k(.9F,0,0,0),k(1.2F,30,0,0));
        r(b,"right_leg",k(0,-30,0,0),k(.3F,0,0,0),k(.6F,30,0,0),k(.9F,0,0,0),k(1.2F,-30,0,0));
        if(hunter) {
            r(b,"right_arm",k(0,-80,-18,0),k(.3F,-77,-18,1),k(.6F,-80,-18,0),k(.9F,-77,-18,1),k(1.2F,-80,-18,0));
            r(b,"left_arm",k(0,-76,34,0),k(.3F,-73,34,-1),k(.6F,-76,34,0),k(.9F,-73,34,-1),k(1.2F,-76,34,0));
        } else {
            r(b,"right_arm",k(0,20,0,3),k(.3F,0,0,3),k(.6F,-20,0,3),k(.9F,0,0,3),k(1.2F,20,0,3));
            r(b,"left_arm",k(0,-20,0,-3),k(.3F,0,0,-3),k(.6F,20,0,-3),k(.9F,0,0,-3),k(1.2F,-20,0,-3));
        }
        return b.build();
    }
    private static AnimationDefinition melee() {
        var b=AnimationDefinition.Builder.withLength(1.2F);
        r(b,"right_arm",k(0,-5,0,3),k(.25F,-85,-8,8),k(.55F,-145,-12,10),k(.7F,22,7,0),k(.82F,15,4,1),k(1.2F,-5,0,3));
        r(b,"left_arm",k(0,0,0,-3),k(.55F,-20,0,-15),k(.7F,-8,0,-6),k(1.2F,0,0,-3));
        r(b,"body",k(0,0,0,0),k(.55F,-4,8,0),k(.7F,4,-9,0),k(.85F,2,-5,0),k(1.2F,0,0,0));
        return b.build();
    }
    private static AnimationDefinition crossbow() {
        var b=AnimationDefinition.Builder.withLength(1.65F);
        r(b,"right_arm",k(0,-78,-18,0),k(.35F,-68,-17,0),k(.72F,-78,-18,0),k(1,-90,-18,0),k(1.1F,-101,-20,0),k(1.3F,-86,-18,0),k(1.65F,-80,-18,0));
        r(b,"left_arm",k(0,-76,34,0),k(.35F,-55,14,-3),k(.72F,-75,34,0),k(1,-82,34,0),k(1.1F,-93,36,0),k(1.3F,-80,34,0),k(1.65F,-76,34,0));
        r(b,"head",k(0,0,0,0),k(.72F,4,0,0),k(1,4,0,0),k(1.1F,1,0,0),k(1.65F,0,0,0));
        return b.build();
    }
    private static AnimationDefinition throwFungus() {
        var b=AnimationDefinition.Builder.withLength(1.5F);
        r(b,"right_arm",k(0,-4,0,3),k(.25F,-80,5,8),k(.7F,-175,12,5),k(.9F,-35,-5,0),k(1.1F,-15,0,2),k(1.5F,-4,0,3));
        r(b,"left_arm",k(0,1,0,-4),k(.7F,-25,-5,-12),k(.9F,-10,0,-7),k(1.5F,1,0,-4));
        r(b,"body",k(0,0,0,0),k(.7F,-4,10,0),k(.9F,5,-8,0),k(1.15F,2,-3,0),k(1.5F,0,0,0));
        return b.build();
    }
    private static float[] k(float t,float x,float y,float z) {return new float[]{t,x,y,z};}
    private static void r(AnimationDefinition.Builder b,String bone,float[]... values) {
        Keyframe[] frames=new Keyframe[values.length];
        for(int i=0;i<values.length;i++) {
            float[] v=values[i];
            frames[i]=new Keyframe(v[0],KeyframeAnimations.degreeVec(v[1],v[2],v[3]),AnimationChannel.Interpolations.CATMULLROM);
        }
        AnimationChannel channel=new AnimationChannel(AnimationChannel.Targets.ROTATION,frames);
        b.addAnimation(bone,channel);
        String clothing=switch(bone) {
            case "right_arm" -> "right_sleeve";
            case "left_arm" -> "left_sleeve";
            case "right_leg" -> "right_pants";
            case "left_leg" -> "left_pants";
            case "body" -> "jacket";
            default -> null;
        };
        if(clothing!=null)b.addAnimation(clothing,channel);
    }
}
