package de.laugenbreze.dungeonsalive.client.entity.reference;

import com.mojang.blaze3d.vertex.PoseStack;
import de.laugenbreze.dungeonsalive.client.entity.reference.imported.CemArchIllagerGeometry;
import de.laugenbreze.dungeonsalive.client.entity.reference.imported.ReferenceModelIndex;
import net.minecraft.client.model.geom.ModelPart;
import org.joml.Vector3f;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/** Regression checks for the two concrete visible failures, not visual-quality claims. */
class VisualPolishPoseTest {
    @Test void archStaffIsVerticalThroughItsRealRaisedHandAndRetainsAnOrb() {
        var definition = CemArchIllagerGeometry.configure(ReferenceMobDefinition.builder("arch_illager",
                CemArchIllagerGeometry::createBodyLayer,
                "dungeonsalive:textures/entity/reference/cem/arch_illager_atlas.png")).build();
        var model = model(definition);
        model.poseForFrame(CemFrame.rest());
        var stack = new PoseStack();
        ModelPart current = model.root();
        current.translateAndRotate(stack);
        for (String node : new String[]{"__cem_host_body1", "arch_illager", "body5", "right_arm6"}) {
            current = current.getChild(node);
            current.translateAndRotate(stack);
        }
        Vector3f hand = stack.last().pose().transformPosition(new Vector3f(1.5F / 16, 5F / 16, 0));
        var staff = current.getChild("bone14");
        staff.translateAndRotate(stack);
        Vector3f axis = stack.last().pose().transformDirection(new Vector3f(0, 1, 0)).normalize();
        assertEquals(0, axis.x, 1e-5, "Raised arm must counter-rotate the shaft instead of tipping it sideways");
        assertEquals(0, axis.z, 1e-5, "The old source's 90 degree pitch made the shaft horizontal");
        assertTrue(axis.y > .999F);
        Vector3f shaft = stack.last().pose().transformPosition(new Vector3f(.5F / 16, 0, 0));
        assertTrue(Math.hypot(hand.x - shaft.x, hand.z - shaft.z) < .025,
                "The vertical shaft must pass through the actual small authored hand");
        assertTrue(staff.hasChild("da_arch_orb"), "The explicit own orb must be attached to the staff rig");
    }

    @Test void actualDrownedBindingSwimsOnlyWithTheSynchronizedWaterStateAndResetsForBottomIdle() {
        var definition = ReferenceModelIndex.create().get("armored_drowned");
        assertNotNull(definition);
        var model = model(definition);
        model.poseForFrame(frame(true, false));
        assertEquals(0, model.root().xRot, 1e-6, "Standing on a water floor must not be treated as swimming");
        model.poseForFrame(frame(true, true));
        assertTrue(model.root().xRot > 1, "Actual swimming must visibly change the whole-body silhouette");
        assertTrue(model.root().getChild("head").xRot < -.5,
                "The head must counter-tilt so the submerged swimmer looks forward");
        model.poseForFrame(frame(true, false));
        assertEquals(0, model.root().xRot, 1e-6, "Another idle water entity must not inherit the preceding swimmer's pose");
        assertEquals(0, model.root().y, 1e-6);
        model.poseForFrame(frame(false, true));
        assertEquals(0, model.root().xRot, 1e-6, "A stale swimming flag outside water cannot create a fake swim pose");
    }

    private static CemFrame frame(boolean water, boolean swimming) {
        return new CemFrame(20, 20, 5, .32F, 0, 0, 0, 0, 20, 20,
                water, !swimming, false, null, 0, null, false, false, false, false, swimming);
    }

    private static ReferenceMobModel<?> model(ReferenceMobDefinition definition) {
        var root = definition.bakedRoot() != null ? definition.bakedRoot().get() : definition.layer().get().bakeRoot();
        return new ReferenceMobModel<>(root, definition);
    }
}
