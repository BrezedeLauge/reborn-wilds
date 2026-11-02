package de.laugenbreze.dungeonsalive.client.entity.reference;

import de.laugenbreze.dungeonsalive.entity.AttackKind;
import net.minecraft.client.animation.AnimationChannel;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.animation.Keyframe;
import net.minecraft.client.animation.KeyframeAnimations;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ReferenceAnimationTest {
    @Test void authoredImpactMapsToEveryServerReleaseAndCannotOverrunClip() {
        for (AttackKind action : AttackKind.values()) {
            assertEquals(.8F, ActionClipTiming.seconds(action.windup(), action.windup(), action.recovery(), .8F, 2), 1e-6);
            assertEquals(0, ActionClipTiming.seconds(-30, action.windup(), action.recovery(), .8F, 2));
            assertEquals(2, ActionClipTiming.seconds(1_000, action.windup(), action.recovery(), .8F, 2));
            assertEquals(1, ActionClipTiming.weight(action.windup(), action.windup(), action.recovery(), 3, 4), 1e-6);
        }
        assertThrows(IllegalArgumentException.class, () -> ActionClipTiming.seconds(1, 14, 8, 3, 2));
        assertThrows(IllegalArgumentException.class, () -> ActionClipTiming.seconds(Float.NaN, 14, 8, 1, 2));
    }

    @Test void actualRigReachesAuthoredContactWithoutLeakingOneEntityPoseIntoAnother() {
        var definition = fixture();
        var root = definition.layer().get().bakeRoot();
        var model = new ReferenceMobModel(root, definition);
        var arm = root.getChild("body").getChild("arm");
        model.poseForFrame(2, .5F, 100, 0, 0, AttackKind.MELEE, AttackKind.MELEE.windup(), 0, false);
        assertEquals(-1.5F, arm.xRot, 1e-5, "Server hit must show the authored contact pose");
        model.poseForFrame(0, 0, 0, 0, 0, null, 0, 0, false);
        assertEquals(.2F, arm.xRot, 1e-5, "Next rendered entity must start from its own idle pose");
        assertEquals(.7F, root.getChild("body").xScale, 1e-5, "Static SCALE survives action resets");
    }

    @Test void enteringAndLeavingActionDoesNotSnapIdleOrWalkingChannels() {
        var definition = fixture();
        var root = definition.layer().get().bakeRoot();
        var model = new ReferenceMobModel(root, definition);
        var body = root.getChild("body");
        var arm = body.getChild("arm");
        var leg = body.getChild("leg");
        model.poseForFrame(3, .4F, 15, 0, 0, null, 0, 0, false);
        float idleArm = arm.xRot, walkingLeg = leg.xRot;
        model.poseForFrame(3, .4F, 15, 0, 0, AttackKind.MELEE, 0, 0, false);
        assertEquals(idleArm, arm.xRot, 1e-6);
        assertEquals(walkingLeg, leg.xRot, 1e-6);
        model.poseForFrame(3, .4F, 15, 0, 0, AttackKind.MELEE, AttackKind.MELEE.duration(), 0, false);
        assertEquals(idleArm, arm.xRot, 1e-6);
        assertEquals(walkingLeg, leg.xRot, 1e-6);
        model.poseForFrame(3, .4F, 15, 0, 0, AttackKind.MELEE, AttackKind.MELEE.windup(), 0, false);
        assertEquals(walkingLeg, leg.xRot, 1e-6, "An arm-only attack must retain the unaffected walk channel");
    }

    @Test void zeroLengthLoopingScaleStaysFiniteForLongRunningAndRepeatedFrames() {
        var definition = fixture();
        var root = definition.layer().get().bakeRoot();
        var model = new ReferenceMobModel(root, definition);
        for (float age : new float[]{0, 1, 10_000, 1_000_000, 0}) {
            model.poseForFrame(0, 0, age, 0, 0, null, 0, 0, false);
            ModelPart body = root.getChild("body");
            assertEquals(.7F, body.xScale, 1e-6);
            assertEquals(.7F, body.yScale, 1e-6);
            assertTrue(Float.isFinite(body.xRot));
            assertTrue(Float.isFinite(body.y));
        }
        VisualBounds bounds = ReferenceModels.bounds("test_reference");
        assertTrue(bounds.width() > 0 && bounds.height() > 0 && Float.isFinite(bounds.centerY()));
    }

    @Test void missingBoneIsAnExplicitAssetErrorInsteadOfSilentlySkippingAnimation() {
        var invalid = ReferenceMobDefinition.builder("missing_bone", ReferenceAnimationTest::layer,
                        "dungeonsalive:textures/entity/test.png")
                .clip("bad", AnimationDefinition.Builder.withLength(1)
                        .addAnimation("misspelled_arm", rotation(0, 0)).build()).idle("bad").build();
        var error = assertThrows(IllegalArgumentException.class,
                () -> new ReferenceMobModel(invalid.layer().get().bakeRoot(), invalid));
        assertTrue(error.getMessage().contains("misspelled_arm"));
    }

    private static ReferenceMobDefinition fixture() {
        var idle = AnimationDefinition.Builder.withLength(1).looping()
                .addAnimation("arm", rotation(.2F, .4F)).build();
        var walk = AnimationDefinition.Builder.withLength(1).looping()
                .addAnimation("leg", rotation(-.5F, .5F)).build();
        var scale = AnimationDefinition.Builder.withLength(0).looping()
                .addAnimation("body", new AnimationChannel(AnimationChannel.Targets.SCALE,
                        new Keyframe(0, KeyframeAnimations.scaleVec(.7F, .7F, .7F), AnimationChannel.Interpolations.LINEAR))).build();
        var strike = AnimationDefinition.Builder.withLength(2)
                .addAnimation("arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                        new Keyframe(0, new org.joml.Vector3f(), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(.8F, new org.joml.Vector3f(-1.5F, 0, 0), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(2, new org.joml.Vector3f(), AnimationChannel.Interpolations.LINEAR))).build();
        return ReferenceMobDefinition.builder("test_reference", ReferenceAnimationTest::layer,
                        "dungeonsalive:textures/entity/test.png")
                .clip("idle", idle).clip("walk", walk).clip("scale", scale).clip("strike", strike)
                .base("scale").idle("idle").walk("walk", 1, 1)
                .action(AttackKind.MELEE, "strike", .8F).build();
    }

    private static AnimationChannel rotation(float start, float end) {
        return new AnimationChannel(AnimationChannel.Targets.ROTATION,
                new Keyframe(0, new org.joml.Vector3f(start, 0, 0), AnimationChannel.Interpolations.LINEAR),
                new Keyframe(1, new org.joml.Vector3f(end, 0, 0), AnimationChannel.Interpolations.LINEAR));
    }

    private static LayerDefinition layer() {
        var mesh = new MeshDefinition();
        var body = mesh.getRoot().addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-4, -12, -3, 8, 12, 6, new CubeDeformation(.25F)), PartPose.offset(0, 24, 0));
        body.addOrReplaceChild("arm", CubeListBuilder.create().texOffs(0, 20).addBox(0, 0, 0, 2, 8, 2), PartPose.ZERO);
        body.addOrReplaceChild("leg", CubeListBuilder.create().texOffs(0, 20).addBox(0, 0, 0, 2, 8, 2), PartPose.ZERO);
        return LayerDefinition.create(mesh, 64, 64);
    }
}
