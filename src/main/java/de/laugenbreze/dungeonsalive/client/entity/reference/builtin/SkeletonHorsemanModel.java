package de.laugenbreze.dungeonsalive.client.entity.reference.builtin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import de.laugenbreze.dungeonsalive.entity.AttackKind;
import de.laugenbreze.dungeonsalive.entity.DungeonMob;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HorseModel;
import net.minecraft.client.model.SkeletonModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.animal.horse.SkeletonHorse;
import net.minecraft.world.entity.monster.Skeleton;

/** One gameplay entity, two complete native rigs/materials and a rider attached to the horse's back. */
public final class SkeletonHorsemanModel extends EntityModel<DungeonMob> {
    private final ModelPart horseRoot, riderRoot, body, headParts, tail;
    private final HorseModel<SkeletonHorse> horse;
    private final SkeletonModel<Skeleton> rider;

    public SkeletonHorsemanModel(ModelPart horseRoot, ModelPart riderRoot) {
        this.horseRoot = horseRoot; this.riderRoot = riderRoot;
        horse = new HorseModel<>(horseRoot);
        rider = new SkeletonModel<>(riderRoot);
        body = horseRoot.getChild("body"); headParts = horseRoot.getChild("head_parts"); tail = body.getChild("tail");
        horse.young = rider.young = false;
        rider.riding = true;
    }

    public static LayerDefinition createHorseLayer() {
        return LayerDefinition.create(HorseModel.createBodyMesh(CubeDeformation.NONE), 64, 64);
    }

    public SkeletonModel<Skeleton> rider() { return rider; }

    /** Includes the animated back pivot: the rider stays seated while the horse rears. */
    public void translateToRider(PoseStack pose) {
        horseRoot.translateAndRotate(pose);
        body.translateAndRotate(pose);
        pose.translate(0, -20 / 16F, -5 / 16F);
    }

    @Override public void setupAnim(DungeonMob mob, float swing, float amount, float age, float yaw, float pitch) {
        horseRoot.getAllParts().forEach(ModelPart::resetPose);
        riderRoot.getAllParts().forEach(ModelPart::resetPose);
        for (String name : new String[]{"left_hind_baby_leg", "right_hind_baby_leg", "left_front_baby_leg", "right_front_baby_leg"})
            horseRoot.getChild(name).visible = false;
        body.getChild("saddle").visible = false;
        for (String name : new String[]{"left_saddle_mouth", "right_saddle_mouth", "left_saddle_line", "right_saddle_line", "head_saddle", "mouth_saddle_wrap"})
            headParts.getChild(name).visible = false;

        body.y = 11;
        float headPitch = pitch * Mth.DEG_TO_RAD;
        if (amount > .2F) headPitch += Mth.cos(swing * .8F) * .15F * amount;
        headParts.xRot = Mth.PI / 6 + headPitch;
        headParts.yRot = Mth.clamp(yaw, -20, 20) * Mth.DEG_TO_RAD;
        float walk = Mth.cos((mob.isInWater() ? .2F : 1) * swing * .6662F + Mth.PI);
        ModelPart leftHind = horseRoot.getChild("left_hind_leg"), rightHind = horseRoot.getChild("right_hind_leg");
        ModelPart leftFront = horseRoot.getChild("left_front_leg"), rightFront = horseRoot.getChild("right_front_leg");
        leftHind.xRot = -walk * .5F * amount; rightHind.xRot = walk * .5F * amount;
        leftFront.xRot = walk * .8F * amount; rightFront.xRot = -walk * .8F * amount;
        leftFront.z = rightFront.z = -10;
        tail.xRot = Mth.PI / 6 + amount * .75F;
        tail.y = -5 + amount; tail.z = 2 + amount * 2;

        float partialTick = Mth.clamp(age - mob.tickCount, 0, 1);
        AttackKind action = mob.activeAttack();
        if (action == AttackKind.LEAP) {
            float ticks = mob.animationTick(partialTick);
            float rise = Mth.clamp(ticks / action.windup(), 0, 1);
            float fall = Mth.clamp((action.duration() - ticks) / Math.max(1, action.recovery()), 0, 1);
            float rear = Math.min(rise, fall) * .8F;
            body.xRot = -Mth.PI / 4 * rear;
            headParts.y = Mth.lerp(rear, 4, -4); headParts.z = Mth.lerp(rear, -12, -4);
            headParts.xRot = Mth.lerp(rear, headParts.xRot, Mth.PI / 12 + headPitch);
            leftFront.y = rightFront.y = Mth.lerp(rear, 14, 2);
            leftFront.z = rightFront.z = Mth.lerp(rear, -10, -6);
            leftFront.xRot = Mth.lerp(rear, leftFront.xRot, -Mth.PI / 3);
            rightFront.xRot = Mth.lerp(rear, rightFront.xRot, -Mth.PI / 3);
            leftHind.xRot += Mth.PI / 12 * rear; rightHind.xRot += Mth.PI / 12 * rear;
        }

        rider.head.xRot = pitch * Mth.DEG_TO_RAD; rider.head.yRot = yaw * Mth.DEG_TO_RAD;
        rider.hat.copyFrom(rider.head);
        rider.rightLeg.xRot = rider.leftLeg.xRot = -1.4137167F;
        rider.rightLeg.yRot = Mth.PI / 10; rider.leftLeg.yRot = -Mth.PI / 10;
        rider.rightLeg.zRot = .07853982F; rider.leftLeg.zRot = -.07853982F;
        rider.rightArm.xRot = rider.leftArm.xRot = -Mth.PI / 5;
        if (action == AttackKind.VOLLEY || action == AttackKind.SOUL_BOLT) {
            // Skeleton bow-and-arrow pose, held through the server shot and released in recovery.
            float remaining = (action.duration() - mob.animationTick(partialTick)) / Math.max(1, action.recovery());
            float weight = Mth.clamp(Math.min(mob.animationTick(partialTick) / 3, remaining), 0, 1);
            rider.rightArm.xRot = Mth.lerp(weight, rider.rightArm.xRot, -Mth.HALF_PI + rider.head.xRot);
            rider.rightArm.yRot = (-.1F + rider.head.yRot) * weight;
            rider.leftArm.xRot = Mth.lerp(weight, rider.leftArm.xRot, -Mth.HALF_PI + rider.head.xRot);
            rider.leftArm.yRot = (.1F + rider.head.yRot + .4F) * weight;
        }
    }

    @Override public void renderToBuffer(PoseStack pose, VertexConsumer vertices, int light, int overlay, int color) {
        horse.renderToBuffer(pose, vertices, light, overlay, color);
    }
}
