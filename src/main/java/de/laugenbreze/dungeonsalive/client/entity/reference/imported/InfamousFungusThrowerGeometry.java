/* Sack geometry adapted from Dungeons Mobs FungusSackModel.java, MIT.
 * Copyright (c) 2022 Patrigan and source contributors. See third_party/dungeons_mobs/LICENSE.txt.
 * Source revision f04b11e7b3672776599fd15ad98a61c53f63b182.
 */
package de.laugenbreze.dungeonsalive.client.entity.reference.imported;

import net.minecraft.client.model.PiglinModel;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

public final class InfamousFungusThrowerGeometry {
    private InfamousFungusThrowerGeometry() {}
    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = PiglinModel.createMesh(CubeDeformation.NONE);
        // Exact authored sack box and setupAnim Y offset. Attach to body so animation
        // carries the actual backpack instead of leaving it floating at the bind pose.
        mesh.getRoot().getChild("body").addOrReplaceChild("fungus_sack",
                CubeListBuilder.create().texOffs(64, 0)
                        .addBox(-4, -4.1111F, -.0556F, 8, 9, 7), PartPose.offset(0, 4.3611F, 0));
        return LayerDefinition.create(mesh, 128, 64);
    }
}
