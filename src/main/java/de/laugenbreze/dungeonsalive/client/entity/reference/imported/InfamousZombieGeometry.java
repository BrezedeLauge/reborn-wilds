package de.laugenbreze.dungeonsalive.client.entity.reference.imported;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;

/** Installed Minecraft zombie geometry; authored MIT Frozen/Jungle textures are separate. */
public final class InfamousZombieGeometry {
    private InfamousZombieGeometry() {}
    public static LayerDefinition createBodyLayer() {
        return LayerDefinition.create(HumanoidModel.createMesh(CubeDeformation.NONE, 0), 64, 64);
    }
}
