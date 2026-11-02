/*
 * Generated adaptation of ReCrafted Mobs 11.0 by GillsBills and credited contributors.
 * Authored asset license: CC-BY-NC-SA-4.0; https://creativecommons.org/licenses/by-nc-sa/4.0/
 * Source: https://modrinth.com/resourcepack/recrafted-mobs
 * Model: installed Minecraft model factory: net.minecraft.client.model.TurtleModel.createBodyLayer()
 * Source SHA256: not applicable to installed game geometry
 * Changes: CEM-to-Java conversion, explicit native host transforms and runtime material bindings.
 * Full credits, source hashes and changes: design/cem-model-imports.json and cem-extra-model-imports.json.
 * Regenerate with tools/convert_cem_models.py; do not hand-edit generated geometry.
 */
package de.laugenbreze.dungeonsalive.client.entity.reference.imported;

import de.laugenbreze.dungeonsalive.client.entity.reference.ReferenceMobDefinition;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

public final class CemPetRubyBabyTurtleGeometry {
    private CemPetRubyBabyTurtleGeometry() {}

    public static LayerDefinition createBodyLayer() {
        return net.minecraft.client.model.TurtleModel.createBodyLayer();
    }

    /** Preserve source expression units; static scales are reapplied after every pose reset. */
    public static ReferenceMobDefinition.Builder configure(ReferenceMobDefinition.Builder builder) {
        builder.cemDriver("turtle");
        builder.hostAlias("head", "head");
        builder.hostAlias("body", "body");
        builder.hostAlias("leg1", "right_hind_leg");
        builder.hostAlias("leg2", "left_hind_leg");
        builder.hostAlias("leg3", "right_front_leg");
        builder.hostAlias("leg4", "left_front_leg");
        builder.hostAlias("egg_belly", "egg_belly");
        builder.hiddenBones("egg_belly");
        return builder;
    }
}
