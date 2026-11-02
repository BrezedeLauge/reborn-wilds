package de.laugenbreze.dungeonsalive.client.entity.reference.imported;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

/**
 * Installed drowned UV convention with separately deformed skin-shell parts.
 * The paired authored MIT base and outer PNGs occupy x=0 and x=64 in a 128x64 atlas.
 * This preserves real shell depth; simply painting both PNGs on one cube is incorrect.
 */
public final class InfamousArmoredDrownedGeometry {
    private InfamousArmoredDrownedGeometry() {}
    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = HumanoidModel.createMesh(CubeDeformation.NONE, 0);
        PartDefinition root = mesh.getRoot();
        root.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(32, 48)
                .addBox(-1, -2, -2, 4, 12, 4), PartPose.offset(5, 2, 0));
        root.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(16, 48)
                .addBox(-2, 0, -2, 4, 12, 4), PartPose.offset(1.9F, 12, 0));
        CubeDeformation outer = new CubeDeformation(.25F);
        root.getChild("head").addOrReplaceChild("drowned_outer_head", CubeListBuilder.create().texOffs(64, 0)
                .addBox(-4, -8, -4, 8, 8, 8, outer), PartPose.ZERO);
        root.getChild("head").addOrReplaceChild("drowned_outer_hat", CubeListBuilder.create().texOffs(96, 0)
                .addBox(-4, -8, -4, 8, 8, 8, new CubeDeformation(.75F)), PartPose.ZERO);
        root.getChild("body").addOrReplaceChild("drowned_outer_body", CubeListBuilder.create().texOffs(80, 16)
                .addBox(-4, 0, -2, 8, 12, 4, outer), PartPose.ZERO);
        root.getChild("right_arm").addOrReplaceChild("drowned_outer_right_arm", CubeListBuilder.create().texOffs(104, 16)
                .addBox(-3, -2, -2, 4, 12, 4, outer), PartPose.ZERO);
        root.getChild("left_arm").addOrReplaceChild("drowned_outer_left_arm", CubeListBuilder.create().texOffs(96, 48)
                .addBox(-1, -2, -2, 4, 12, 4, outer), PartPose.ZERO);
        root.getChild("right_leg").addOrReplaceChild("drowned_outer_right_leg", CubeListBuilder.create().texOffs(64, 16)
                .addBox(-2, 0, -2, 4, 12, 4, outer), PartPose.ZERO);
        root.getChild("left_leg").addOrReplaceChild("drowned_outer_left_leg", CubeListBuilder.create().texOffs(80, 48)
                .addBox(-2, 0, -2, 4, 12, 4, outer), PartPose.ZERO);
        return LayerDefinition.create(mesh, 128, 64);
    }
}
