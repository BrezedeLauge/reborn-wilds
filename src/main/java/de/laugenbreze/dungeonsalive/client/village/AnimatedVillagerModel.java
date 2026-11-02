package de.laugenbreze.dungeonsalive.client.village;

import com.mojang.blaze3d.vertex.PoseStack;
import de.laugenbreze.dungeonsalive.village.ProfessionGesture;
import de.laugenbreze.dungeonsalive.village.VillageActivity;
import de.laugenbreze.dungeonsalive.village.VillageAnimationConfig;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.client.model.VillagerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.npc.Villager;

/** Own motion and arm geometry on Minecraft's runtime villager model, not an imported animation. */
public final class AnimatedVillagerModel extends VillagerModel<Villager> implements ArmedModel {
    private final ModelPart rightArm, leftArm, body;
    private VillageClient.AnimationFrame frame;

    public AnimatedVillagerModel(ModelPart root) {
        super(root);
        rightArm = root.getChild("right_arm");
        leftArm = root.getChild("left_arm");
        body = root.getChild("body");
    }

    public static LayerDefinition createLayer() {
        var mesh = VillagerModel.createBodyModel();
        var root = mesh.getRoot();
        root.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(44, 22)
                .addBox(-3, -2, -2, 4, 10, 4), PartPose.offset(-5, 3, 0));
        root.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(44, 22).mirror()
                .addBox(-1, -2, -2, 4, 10, 4), PartPose.offset(5, 3, 0));
        return LayerDefinition.create(mesh, 64, 64);
    }

    @Override public void setupAnim(Villager villager, float limbSwing, float limbAmount, float ageInTicks, float headYaw, float headPitch) {
        root().getAllParts().forEach(ModelPart::resetPose);
        super.setupAnim(villager, limbSwing, limbAmount, ageInTicks, headYaw, headPitch);
        root().getChild("arms").visible = false;
        rightArm.visible = leftArm.visible = true;
        frame = VillageClient.frame(villager, ageInTicks);
        float strength = VillageAnimationConfig.INTENSITY.get().floatValue();
        float phase = ageInTicks + (villager.getUUID().getLeastSignificantBits() & 255);
        float breath = Mth.sin(phase * 0.065F);
        rightArm.xRot = Mth.cos(limbSwing * 0.6662F + Mth.PI) * 0.7F * limbAmount;
        leftArm.xRot = Mth.cos(limbSwing * 0.6662F) * 0.7F * limbAmount;
        rightArm.zRot = 0.06F + breath * 0.015F * strength;
        leftArm.zRot = -rightArm.zRot;
        body.zRot = Mth.sin(limbSwing * 0.6662F) * Math.min(limbAmount, 1) * 0.025F * strength;
        body.y = breath * 0.05F * strength;
        if (villager.isSleeping()) return;
        float w = frame.weight() * strength;
        if (frame.mode() == VillageActivity.PANIC) {
            rightArm.xRot = blend(rightArm.xRot, -1.0F + Mth.sin(phase * 0.8F) * 0.35F, w);
            leftArm.xRot = blend(leftArm.xRot, -1.0F - Mth.sin(phase * 0.8F) * 0.35F, w);
            rightArm.zRot += 0.3F * w; leftArm.zRot -= 0.3F * w;
        } else if (frame.mode() == VillageActivity.TRADE) {
            rightArm.xRot = blend(rightArm.xRot, -1.2F + Mth.sin(phase * 0.12F) * 0.08F, w);
            leftArm.xRot = blend(leftArm.xRot, -0.65F, w);
            rightArm.yRot = -0.25F * w; leftArm.yRot = 0.2F * w;
            getHead().xRot += Mth.sin(phase * 0.1F) * 0.04F * w;
        } else if (frame.mode() == VillageActivity.WORK) {
            animateWork(frame.gesture(), frame.time(), w);
        }
        if (villager.hurtTime > 0) body.zRot += Mth.sin(villager.hurtTime * 0.75F) * 0.08F;
    }

    private void animateWork(ProfessionGesture gesture, float t, float w) {
        float slow = Mth.sin(t * 0.15F), beat = Mth.sin(t * 0.32F);
        float r = -0.8F, l = -0.8F, ry = 0, ly = 0, rz = 0.08F, lz = -0.08F, lean = 0;
        switch (gesture) {
            case ARMORER -> { r = -1.15F + beat * 0.65F; l = -0.85F; ly = 0.35F; lean = 0.06F; }
            case TOOLSMITH -> { r = -1.0F + Mth.sin(t * 0.4F) * 0.4F; l = -0.9F; ry = -0.12F; }
            case WEAPONSMITH -> { r = -1.0F + slow * 0.16F; l = -1.05F - slow * 0.16F; ry = -0.3F; ly = 0.3F; lean = 0.09F; }
            case BLACKSMITH -> { r = -1.2F + Mth.sin(t * 0.26F) * 0.8F; l = -0.7F; ly = 0.35F; lean = 0.08F; }
            case BUTCHER -> { r = -1.05F + Mth.sin(t * 0.38F) * 0.4F; l = -0.95F; ly = 0.3F; lean = 0.04F; }
            case CARTOGRAPHER -> { r = -1.0F; l = -0.9F; ry = -0.25F + slow * 0.15F; ly = 0.25F; lean = 0.13F; }
            case CLERIC -> { r = -1.0F + Mth.cos(t * 0.25F) * 0.12F; l = -0.8F; ry = -0.25F + beat * 0.16F; ly = 0.25F; }
            case FARMER -> { r = -0.95F + slow * 0.38F; l = -0.75F - slow * 0.18F; ry = -0.16F; ly = 0.3F; lean = 0.16F; }
            case FISHERMAN -> { r = -0.65F + slow * 0.14F; l = -1.05F; ry = -0.1F; ly = 0.4F; }
            case FLETCHER -> { r = -0.95F + beat * 0.07F; l = -1.0F; ry = -0.4F; ly = 0.45F; lean = 0.08F; }
            case LEATHERWORKER -> { r = -0.9F + beat * 0.15F; l = -0.9F; ry = -0.3F + slow * 0.1F; ly = 0.35F; lean = 0.13F; }
            case LIBRARIAN -> { r = -1.1F + slow * 0.03F; l = -1.1F; ry = -0.5F + Math.max(0, beat) * 0.15F; ly = 0.5F; lean = 0.04F; }
            case MASON -> { r = -1.2F + Mth.sin(t * 0.42F) * 0.34F; l = -1.0F; ry = -0.3F; ly = 0.35F; lean = 0.12F; }
            case SHEPHERD -> { r = -0.95F + slow * 0.13F; l = -0.95F - slow * 0.13F; ry = -0.4F; ly = 0.4F; lean = 0.05F; }
            case VILLAGE_MERCHANT -> { r = -0.8F + slow * 0.2F; l = -0.8F - slow * 0.2F; ry = -0.3F; ly = 0.3F; }
            case LUXURY_MERCHANT -> { r = -1.2F + slow * 0.08F; l = -1.05F; ry = -0.25F; ly = 0.25F; }
            case MYSTERY_MERCHANT -> { r = -1.25F; l = -0.75F; ry = -0.3F + slow * 0.2F; ly = 0.4F; }
            case GIFT_WRAPPER -> { r = -0.95F; l = -0.95F; ry = -0.35F + slow * 0.25F; ly = 0.35F - slow * 0.25F; lean = 0.1F; }
            case ENCHANTSMITH -> { r = -1.2F + slow * 0.12F; l = -1.15F - slow * 0.12F; ry = -0.25F; ly = 0.25F; rz = 0.2F; lz = -0.2F; }
            case NONE -> { return; }
        }
        rightArm.xRot = blend(rightArm.xRot, r, w); leftArm.xRot = blend(leftArm.xRot, l, w);
        rightArm.yRot = ry * w; leftArm.yRot = ly * w;
        rightArm.zRot = blend(rightArm.zRot, rz, w); leftArm.zRot = blend(leftArm.zRot, lz, w);
        body.xRot = lean * w;
        getHead().xRot += (0.18F + lean) * w;
    }

    private static float blend(float from, float to, float weight) { return Mth.lerp(Math.min(weight, 1), from, to); }
    public VillageClient.AnimationFrame animationFrame() { return frame; }
    @Override public void translateToHand(HumanoidArm arm, PoseStack poses) {
        root().translateAndRotate(poses);
        (arm == HumanoidArm.RIGHT ? rightArm : leftArm).translateAndRotate(poses);
    }
}
