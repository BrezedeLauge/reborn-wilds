package de.laugenbreze.dungeonsalive.client.entity.reference.imported;

import net.minecraft.client.model.PiglinModel;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

/**
 * Installed piglin mesh plus the actual outer HumanoidArmorModel head dimensions.
 * Authored MIT skin/helmet textures are losslessly juxtaposed at atlas x=0 / x=64.
 * CustomPiglinRenderer/PiglinHelmetItem source bindings: design/infamous-additions.json.
 */
public final class InfamousArmoredPiglinGeometry {
    private InfamousArmoredPiglinGeometry() {}
    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = PiglinModel.createMesh(CubeDeformation.NONE);
        mesh.getRoot().getChild("head").addOrReplaceChild("piglin_helmet",
                CubeListBuilder.create().texOffs(64, 0)
                        .addBox(-4, -8, -4, 8, 8, 8, new CubeDeformation(1.02F)), PartPose.ZERO);
        return LayerDefinition.create(mesh, 128, 64);
    }
}
