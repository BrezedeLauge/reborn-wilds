package de.laugenbreze.dungeonsalive.client.entity.reference.imported;

import net.minecraft.client.model.SkeletonModel;
import net.minecraft.client.model.geom.builders.LayerDefinition;

/** Uses the installed skeleton factory with its actual thin limbs and 64x32 UV layout. */
public final class InfamousMossySkeletonGeometry {
    private InfamousMossySkeletonGeometry() {}
    public static LayerDefinition createBodyLayer() { return SkeletonModel.createBodyLayer(); }
}
