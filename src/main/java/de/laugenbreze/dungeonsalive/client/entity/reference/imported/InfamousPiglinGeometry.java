package de.laugenbreze.dungeonsalive.client.entity.reference.imported;

import net.minecraft.client.model.PiglinModel;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;

/** Installed piglin geometry includes the true snout, tusks, ears and clothing parts. */
public final class InfamousPiglinGeometry {
    private InfamousPiglinGeometry() {}
    public static LayerDefinition createBodyLayer() {
        return LayerDefinition.create(PiglinModel.createMesh(CubeDeformation.NONE), 64, 64);
    }
}
