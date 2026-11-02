/* Adapted from Goety 2, Copyright (c) 2023 Polarice3, MIT.
 * Revision: 7de64560f76ae47f1b399bb46f69540511b9871e
 * Original: client/render/model/TropicalSlimeModel.java.
 * Exact source boxes/UVs; own shared ground pivot and matching layer hierarchy.
 * Provenance: design/v04-source-model-additions.json; third_party/goety/LICENSE.txt.
 */
package de.laugenbreze.dungeonsalive.client.entity.reference.imported;

import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

public final class GoetyTropicalSlimeGeometry {
    private GoetyTropicalSlimeGeometry() {}

    public static LayerDefinition createBodyLayer() { return create(false); }
    public static LayerDefinition createOuterBodyLayer() { return create(true); }

    private static LayerDefinition create(boolean outer) {
        MeshDefinition mesh = new MeshDefinition();
        // Moving the pivot to ground level changes no rest-pose vertex. All materials
        // use these same paths so a squash cannot detach the fish or outer surface.
        var slime = mesh.getRoot().addOrReplaceChild("slime", CubeListBuilder.create(), PartPose.offset(0, 24, 0));
        CubeListBuilder inner = CubeListBuilder.create();
        CubeListBuilder shell = CubeListBuilder.create();
        if (outer) {
            shell.texOffs(0, 0).addBox(-4, 16, -4, 8, 8, 8, new CubeDeformation(0));
        } else {
            inner.texOffs(0, 20).addBox(-3, 17, -3, 6, 6, 6, new CubeDeformation(0))
                    .texOffs(24, 16).addBox(-3.3F, 18, -3.5F, 2, 2, 2, new CubeDeformation(0))
                    .texOffs(24, 20).addBox(1.3F, 18, -3.5F, 2, 2, 2, new CubeDeformation(0))
                    .texOffs(24, 24).addBox(0, 21, -3.5F, 1, 1, 1, new CubeDeformation(0));
        }
        slime.addOrReplaceChild("inner", inner, PartPose.offset(0, -24, 0));
        slime.addOrReplaceChild("cube", shell, PartPose.offset(0, -24, 0));
        return LayerDefinition.create(mesh, 32, 32);
    }
}
