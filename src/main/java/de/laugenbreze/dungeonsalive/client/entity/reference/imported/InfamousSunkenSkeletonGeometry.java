/* Coral geometry adapted from Dungeons Mobs SunkenSkeletonModel.java, MIT.
 * Copyright (c) 2022 Patrigan and source contributors. See third_party/dungeons_mobs/LICENSE.txt.
 * Source revision f04b11e7b3672776599fd15ad98a61c53f63b182.
 */
package de.laugenbreze.dungeonsalive.client.entity.reference.imported;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

public final class InfamousSunkenSkeletonGeometry {
    private InfamousSunkenSkeletonGeometry() {}
    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = HumanoidModel.createMesh(CubeDeformation.NONE, 0);
        PartDefinition root = mesh.getRoot();
        root.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(0, 16)
                .addBox(-1, -2, -1, 2, 12, 2), PartPose.offset(-5, 2, 0));
        root.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(0, 16).mirror()
                .addBox(-1, -2, -1, 2, 12, 2), PartPose.offset(5, 2, 0));
        root.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(40, 16)
                .addBox(-1, 0, -1, 2, 12, 2), PartPose.offset(-2, 12, 0));
        root.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(40, 16).mirror()
                .addBox(-1, 0, -1, 2, 12, 2), PartPose.offset(2, 12, 0));
        // The authored root-level accessories were not carried by head/leg movement.
        // Preserve their art, attach them correctly and avoid the redundant hat shell.
        root.addOrReplaceChild("hat", CubeListBuilder.create(), PartPose.ZERO);
        root.getChild("head").addOrReplaceChild("head_coral_1", CubeListBuilder.create().texOffs(32, 0)
                .addBox(-4, -8, -4, 8, 8, 8, new CubeDeformation(.5F)), PartPose.ZERO);
        root.getChild("head").addOrReplaceChild("head_coral_2", CubeListBuilder.create().texOffs(44, 10)
                .addBox(-1, -3, 2, 6, 1, 4), PartPose.ZERO);
        root.getChild("right_leg").addOrReplaceChild("right_leg_coral", CubeListBuilder.create().texOffs(52, 1)
                .addBox(-2, 3, -2.1F, 2, 1, 4), PartPose.ZERO);
        return LayerDefinition.create(mesh, 64, 32);
    }
}
