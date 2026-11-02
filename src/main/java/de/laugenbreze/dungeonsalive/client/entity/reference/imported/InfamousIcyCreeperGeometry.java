package de.laugenbreze.dungeonsalive.client.entity.reference.imported;

import net.minecraft.client.model.CreeperModel;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;

/** Uses the installed creeper rig; no vanilla texture is duplicated in the asset package. */
public final class InfamousIcyCreeperGeometry {
    private InfamousIcyCreeperGeometry() {}
    public static LayerDefinition createBodyLayer() { return CreeperModel.createBodyLayer(CubeDeformation.NONE); }
}
