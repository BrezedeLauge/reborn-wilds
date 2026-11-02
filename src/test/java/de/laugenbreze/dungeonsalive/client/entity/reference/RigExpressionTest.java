package de.laugenbreze.dungeonsalive.client.entity.reference;

import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class RigExpressionTest {
    @Test void authoredCemOscillationAndNestedNegativeExpressionsKeepRadianSemantics() {
        var expression = RigExpression.compile("torad(120) + sin(torad(13.5*3) + time/45) *0.15");
        double actual = expression.evaluate(name -> {
            assertEquals("time", name); return 100;
        });
        assertEquals(Math.toRadians(120) + Math.sin(Math.toRadians(40.5) + 100.0 / 45) * .15, actual, 1e-12);
        var negative = RigExpression.compile("torad(-10) + -(limb_speed+limb_speed*sin(limb_swing/60)) + 0.1*sin(age/10)");
        var inputs = Map.of("limb_speed", .4, "limb_swing", 20.0, "age", 40.0);
        assertEquals(Math.toRadians(-10) - (.4 + .4 * Math.sin(20.0 / 60)) + .1 * Math.sin(4),
                negative.evaluate(inputs::get), 1e-12);
    }

    @Test void expressionErrorsAreExplicitAndConditionalsDoNotEvaluateDeadBranches() {
        assertThrows(IllegalArgumentException.class, () -> RigExpression.compile("mystery(time)"));
        assertThrows(IllegalArgumentException.class, () -> RigExpression.compile("sin(1,2)"));
        assertThrows(IllegalArgumentException.class, () -> RigExpression.compile("1 +"));
        assertThrows(IllegalArgumentException.class, () -> RigExpression.compile("1/0").evaluate(name -> 0));
        assertThrows(IllegalArgumentException.class, () -> RigExpression.compile("(".repeat(80) + "1" + ")".repeat(80)));
        assertEquals(3, RigExpression.compile("if(1 < 2, 3, 1/0)").evaluate(name -> 0));
        assertEquals(2, RigExpression.compile("0 ? 1/0 : max(1,2)").evaluate(name -> 0));
    }

    @Test void cemHostAliasWinsOverWrapperAndAssignmentsKeepTheirAuthoredOrder() {
        var definition = ReferenceMobDefinition.builder("test_cem", RigExpressionTest::layer,
                        "dungeonsalive:textures/entity/test.png")
                .hostAlias("head", "__cem_host_head")
                .boneScale("decor", .5F)
                .expression("head.rx", "torad(30)")
                .expression("decor.rx", "head.rx / 2")
                .expression("decor.ty", "decor.rx * 10")
                .expression("render.shadow_size", "3")
                .build();
        var root = definition.layer().get().bakeRoot();
        var model = new ReferenceMobModel(root, definition);
        for (int i = 0; i < 2; i++) {
            model.poseForFrame(0, 0, i * 100, 0, 0, null, 0, 0, false);
            assertEquals(Math.toRadians(30), root.getChild("__cem_host_head").xRot, 1e-6);
            assertEquals(0, root.getChild("head").xRot, "A same-named CEM wrapper must not receive host rotation a second time");
            assertEquals(Math.toRadians(15), root.getChild("decor").xRot, 1e-6);
            assertEquals(Math.toRadians(15) * 10, root.getChild("decor").y, 1e-6);
            assertEquals(.5F, root.getChild("decor").xScale, 1e-6, "Static CEM scale must survive resetPose");
            assertEquals(3, model.expressionShadowSize());
        }
    }

    @Test void unknownCemVariableCannotBecomeAnUnnoticedZero() {
        var definition = ReferenceMobDefinition.builder("bad_expression", RigExpressionTest::layer,
                        "dungeonsalive:textures/entity/test.png")
                .expression("decor.rx", "query.unknown + 1").build();
        var failure = assertThrows(IllegalArgumentException.class,
                () -> new ReferenceMobModel(definition.layer().get().bakeRoot(), definition));
        assertTrue(failure.getMessage().contains("query"));
    }

    @Test void explicitEmfCustomNamespaceBypassesAConflictingVanillaHostAlias() {
        var definition = ReferenceMobDefinition.builder("test_this_scope", RigExpressionTest::layer,
                        "dungeonsalive:textures/entity/test.png")
                .hostAlias("head", "__cem_host_head")
                .expression("head.rx", "torad(30)")
                .expression("EMF_head.rx", "torad(80)")
                .expression("decor.rx", "EMF_head.rx - head.rx")
                .build();
        var root = definition.layer().get().bakeRoot();
        var model = new ReferenceMobModel<>(root, definition);
        model.poseForFrame(0, 0, 0, 0, 0, null, 0, 0, false);
        assertEquals(Math.toRadians(30), root.getChild("__cem_host_head").xRot, 1e-6);
        assertEquals(Math.toRadians(80), root.getChild("head").xRot, 1e-6);
        assertEquals(Math.toRadians(50), root.getChild("decor").xRot, 1e-6,
                "Lexical this must refer to the exact source model, not its Vanilla host");
    }

    private static LayerDefinition layer() {
        var mesh = new MeshDefinition();
        mesh.getRoot().addOrReplaceChild("__cem_host_head", CubeListBuilder.create(), PartPose.ZERO);
        mesh.getRoot().addOrReplaceChild("head", CubeListBuilder.create(), PartPose.ZERO);
        mesh.getRoot().addOrReplaceChild("decor", CubeListBuilder.create().texOffs(0, 0).addBox(-4, -8, -4, 8, 8, 8), PartPose.offset(0, 24, 0));
        return LayerDefinition.create(mesh, 64, 64);
    }
}
