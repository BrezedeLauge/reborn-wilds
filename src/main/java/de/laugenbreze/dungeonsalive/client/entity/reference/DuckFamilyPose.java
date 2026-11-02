package de.laugenbreze.dungeonsalive.client.entity.reference;

import de.laugenbreze.dungeonsalive.entity.peaceful.DungeonsCreature;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;

/** Own procedural poses over the unchanged, separately attributed duck rig.
 * Reads synchronized real activities; never chooses or advances a server activity.
 */
final class DuckFamilyPose {
    private static final java.util.Map<DungeonsCreature, Blend> BLENDS = new java.util.WeakHashMap<>();
    private static final class Blend {
        float age, pitch, yaw, height;
        boolean initialized;
        void update(float now, float targetPitch, float targetYaw, float targetHeight) {
            if (!initialized || now < age) { age = now; initialized = true; }
            float amount = 1F - (float)Math.exp(-Mth.clamp(now - age, 0, 2) * .28F);
            pitch = Mth.lerp(amount, pitch, targetPitch);
            yaw = Mth.lerp(amount, yaw, targetYaw);
            height = Mth.lerp(amount, height, targetHeight);
            age = now;
        }
    }
    private DuckFamilyPose() {}

    static void apply(ModelPart root, DungeonsCreature duck, float age) {
        if (!duck.duck() || !duck.duckFamily() || !duck.isAlive() || duck.deathTime > 0) return;
        ModelPart head = root.getChild("__cem_host_head");
        ModelPart bill = root.getChild("__cem_host_bill");
        ModelPart chin = root.getChild("__cem_host_chin");
        ModelPart body = root.getChild("__cem_host_body");
        ModelPart leftLeg = root.getChild("__cem_host_left_leg");
        ModelPart rightLeg = root.getChild("__cem_host_right_leg");
        ModelPart leftWing = root.getChild("__cem_host_left_wing");
        ModelPart rightWing = root.getChild("__cem_host_right_wing");
        // The ordinary chicken host treats any airborne body as flapping. A duck
        // floating on water is supported by the water surface, not in flight.
        if (duck.isInWater()) {
            leftWing.zRot = 0; rightWing.zRot = 0;
            float effort = Mth.clamp((float)duck.getDeltaMovement().horizontalDistance() * 12, .15F, 1F);
            float stagger = (float)Math.floorMod(duck.getUUID().getLeastSignificantBits(), 53) / 9F;
            float paddle = Mth.sin(age * .45F + stagger) * .6F * effort;
            leftLeg.xRot = -.5F + paddle; rightLeg.xRot = -.5F - paddle;
            body.zRot += Mth.sin(age * .12F + stagger) * .025F * effort;
        }
        float partial = Mth.clamp(age - duck.tickCount, 0, 1);
        float activity = duck.duckActivityTicks(partial);
        float blend = Mth.clamp(activity / 12F, 0, 1);
        float personalPhase = Math.floorMod(duck.getUUID().getLeastSignificantBits(), 97) / 97F * Mth.TWO_PI;
        float pitch = 0, yaw = 0, height = 0;
        switch (duck.duckActivity()) {
            case FORAGE -> {
                float peck = Math.max(0, Mth.sin(activity * .2F + personalPhase));
                pitch = (.25F + peck * .8F) * blend;
                height = peck * .7F * blend;
            }
            case PREEN -> {
                yaw = (1.25F + Mth.sin(activity * .13F + personalPhase) * .13F) * blend;
                pitch = .5F * blend;
                rightWing.zRot = -.14F * blend;
            }
            case REST -> {
                yaw = 1.1F * blend; pitch = .2F * blend;
                height = .65F * blend;
                leftLeg.xRot *= 1 - blend; rightLeg.xRot *= 1 - blend;
            }
            case ALERT -> pitch = -.25F * blend;
            default -> { }
        }
        // Head, beak and chin are sibling host pivots in this source rig. Apply
        // identical head transformations so attached face parts never separate.
        Blend pose = BLENDS.computeIfAbsent(duck, ignored -> new Blend());
        pose.update(age, pitch, yaw, height);
        // LookControl may already look at the ground; adding a full peck must
        // not turn the head backwards through the body.
        head.xRot = Mth.clamp(head.xRot + pose.pitch, -.7F, 1.35F);
        head.yRot = Mth.clamp(head.yRot + pose.yaw, -1.5F, 1.5F);
        head.y += pose.height;
        bill.copyFrom(head); chin.copyFrom(head);
    }
}
