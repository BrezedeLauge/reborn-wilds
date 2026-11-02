package de.laugenbreze.dungeonsalive.client.entity.reference;

import de.laugenbreze.dungeonsalive.entity.AttackKind;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CemHostAnimatorTest {
    @Test void cowUsesOpposedDiagonalPairsAndStopsWithoutRetainedLegMotion() {
        var model = model("cow", "body", "head", "leg1", "leg2", "leg3", "leg4");
        model.poseForFrame(frame(8, .7F, true, false, null, 0));
        float first = bone(model, "leg1").xRot;
        assertTrue(Math.abs(first) > .1F, "A moving animal must have an actual gait");
        assertEquals(first, bone(model, "leg4").xRot, 1e-6);
        assertEquals(-first, bone(model, "leg2").xRot, 1e-6);
        assertEquals(-first, bone(model, "leg3").xRot, 1e-6);
        model.poseForFrame(frame(8, 0, true, false, null, 0));
        for (int i = 1; i <= 4; i++) assertEquals(0, bone(model, "leg" + i).xRot, 1e-6);
    }

    @Test void chickenUsesBoundedAirborneFlappingRatherThanAgeAsRotation() {
        var model = model("chicken", "body", "head", "bill", "chin", "left_leg", "right_leg", "left_wing", "right_wing");
        model.poseForFrame(frame(10000, 0, false, false, null, 0));
        float flap = bone(model, "right_wing").zRot;
        assertTrue(flap >= 0 && flap <= 1.5F);
        assertEquals(-flap, bone(model, "left_wing").zRot, 1e-6);
        model.poseForFrame(frame(10000, 0, true, false, null, 0));
        assertEquals(0, bone(model, "right_wing").zRot, 1e-6);
        assertEquals(bone(model, "head").xRot, bone(model, "bill").xRot, 1e-6);
    }

    @Test void sittingFoxAndParrotResetBeforeRenderingTheNextStandingEntity() {
        var fox = model("fox", "body", "head", "tail", "leg1", "leg2", "leg3", "leg4");
        fox.poseForFrame(frame(20, 0, true, true, null, 0));
        assertEquals(9, bone(fox, "body").y, 1e-6);
        assertEquals(21.5F, bone(fox, "leg1").y, 1e-6);
        fox.poseForFrame(frame(20, 0, true, false, null, 0));
        assertEquals(0, bone(fox, "body").y, 1e-6, "The fixture's standing bind pose must be restored");
        var parrot = model("parrot", "body", "head", "tail", "left_leg", "right_leg", "left_wing", "right_wing");
        parrot.poseForFrame(frame(20, 0, true, true, null, 0));
        assertEquals(23, bone(parrot, "left_leg").y, 1e-6);
        parrot.poseForFrame(frame(20, 0, true, false, null, 0));
        assertEquals(22, bone(parrot, "left_leg").y, 1e-6);
        assertEquals(-.0299F, bone(parrot, "left_leg").xRot, 1e-6);
    }

    @Test void illagerCastingSwitchesArmGeometryAndReturnsToFoldedIdle() {
        var model = model("evoker", "body", "head", "arms", "left_arm", "right_arm", "left_leg", "right_leg");
        model.poseForFrame(frame(25, 0, true, false, AttackKind.SUMMON, AttackKind.SUMMON.windup()));
        assertFalse(bone(model, "arms").visible, "Folded and raised arms must not be rendered together");
        assertTrue(bone(model, "right_arm").visible);
        assertTrue(bone(model, "right_arm").zRot > 2);
        model.poseForFrame(frame(25, 0, true, false, null, 0));
        assertTrue(bone(model, "arms").visible);
        assertFalse(bone(model, "right_arm").visible, "ModelPart.resetPose alone does not reset visible");
        assertFalse(bone(model, "left_arm").visible);
    }

    @Test void extraCemRidingAndSittingInputsComeFromFrameRatherThanAConstantFallback() {
        var expression = RigExpression.compile("if(is_ridden, torad(90), if(is_sitting, torad(30), 0))");
        CemFrame resting = frame(0, 0, true, false, null, 0);
        CemFrame sitting = frame(0, 0, true, true, null, 0);
        CemFrame ridden = new CemFrame(0, 0, 0, 0, 0, 0, 0, 0, 1, 1, false, true,
                false, null, 0, null, true, false, false, false);
        assertEquals(0, expression.evaluate(resting::input));
        assertEquals(Math.PI / 6, expression.evaluate(sitting::input), 1e-6);
        assertEquals(Math.PI / 2, expression.evaluate(ridden::input), 1e-6);
    }

    @Test void armoredPillagerHasVisibleSeparateArmsWithoutInventingAnAbsentFoldedArmBone() {
        var model = model("armored_pillager", "body", "head", "left_arm", "right_arm", "left_leg", "right_leg");
        model.poseForFrame(frame(40, 0, true, false, null, 0));
        assertTrue(bone(model, "left_arm").visible);
        assertTrue(bone(model, "right_arm").visible);
        assertTrue(bone(model, "right_arm").xRot < -1, "The actual crossbow must be held by the separate arms");
    }

    private static CemFrame frame(float age, float moving, boolean grounded, boolean sitting, AttackKind action, float elapsed) {
        return new CemFrame(age, age, age * .4F, moving, 15, 10, 0, 0, 1, 1, false, grounded,
                action != null, action, elapsed, null, false, sitting, false, false);
    }

    private static ReferenceMobModel<?> model(String host, String... aliases) {
        var mesh = new MeshDefinition();
        for (String name : aliases) mesh.getRoot().addOrReplaceChild("host_" + name,
                CubeListBuilder.create().texOffs(0, 0).addBox(-1, -1, -1, 2, 2, 2), PartPose.ZERO);
        var layer = LayerDefinition.create(mesh, 64, 64);
        var builder = ReferenceMobDefinition.builder("test_host_" + host, () -> layer, "dungeonsalive:textures/entity/test.png")
                .cemDriver(host);
        for (String name : aliases) builder.hostAlias(name, "host_" + name);
        var definition = builder.build();
        return new ReferenceMobModel<>(layer.bakeRoot(), definition);
    }

    private static ModelPart bone(ReferenceMobModel<?> model, String alias) {
        return model.root().getChild("host_" + alias);
    }
}
