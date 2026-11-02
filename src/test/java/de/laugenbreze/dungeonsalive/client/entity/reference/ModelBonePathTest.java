package de.laugenbreze.dungeonsalive.client.entity.reference;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import org.joml.Vector3f;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ModelBonePathTest {
    @Test void itemAnchorIncludesAnimatedParentRotationTranslationAndScale() {
        var mesh = new MeshDefinition();
        var torso = mesh.getRoot().addOrReplaceChild("torso", CubeListBuilder.create(), PartPose.offset(16, 0, 0));
        torso.addOrReplaceChild("hand", CubeListBuilder.create(), PartPose.offset(16, 0, 0));
        var root = LayerDefinition.create(mesh, 64, 64).bakeRoot();
        root.getChild("torso").zRot = (float)Math.PI / 2;
        root.getChild("torso").xScale = 2;
        var path = new ModelBonePath(root, "torso/hand", "test hand");
        var pose = new PoseStack();
        path.transform(pose);
        Vector3f anchor = pose.last().pose().transformPosition(new Vector3f());
        assertEquals(1, anchor.x, 1e-6);
        assertEquals(2, anchor.y, 1e-6, "Applying only the hand transform would detach equipment from the animated parent");
        assertEquals(0, anchor.z, 1e-6);
        root.getChild("torso").visible = false;
        assertFalse(path.visible(), "A weapon must disappear with its parent arm");
    }

    @Test void pathsAreStrictAndDoNotSilentlyStripRealRootBones() {
        var mesh = new MeshDefinition();
        mesh.getRoot().addOrReplaceChild("hand", CubeListBuilder.create(), PartPose.ZERO);
        var root = LayerDefinition.create(mesh, 64, 64).bakeRoot();
        assertDoesNotThrow(() -> new ModelBonePath(root, "/hand", "hand"));
        assertThrows(IllegalArgumentException.class, () -> new ModelBonePath(root, "root/hand", "hand"));
        assertThrows(IllegalArgumentException.class, () -> new ModelBonePath(root, "hand/missing", "hand"));
    }
}
