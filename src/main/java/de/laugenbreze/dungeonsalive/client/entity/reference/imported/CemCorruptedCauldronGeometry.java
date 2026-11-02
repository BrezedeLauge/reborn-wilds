/*
 * Generated adaptation of ReCrafted Mobs 11.0 by GillsBills and credited contributors.
 * Asset license: CC-BY-NC-SA-4.0; https://creativecommons.org/licenses/by-nc-sa/4.0/
 * Source: https://modrinth.com/resourcepack/recrafted-mobs
 * Model: assets/minecraft/optifine/cem/slime/slime3.jem
 * SHA256: 01473ebb763ecec33a32b1d32a853a3d7002b3cb565627ff704fa2f754edc81c
 * Changes: CEM-to-Java conversion, explicit vanilla host transforms and runtime bindings.
 * Full credits, source hashes and additional changes: design/cem-model-imports.json.
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

public final class CemCorruptedCauldronGeometry {
    private CemCorruptedCauldronGeometry() {}

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        PartDefinition p0 = root.addOrReplaceChild("__cem_host_body", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p1 = root.addOrReplaceChild("__cem_host_right_eye", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p2 = root.addOrReplaceChild("__cem_host_left_eye", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p3 = root.addOrReplaceChild("__cem_host_mouth", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p4 = p0.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 29.5F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p5 = p4.addOrReplaceChild("cauldron", CubeListBuilder.create()
                .texOffs(352, 0).mirror(false)
                .addBox(-16.0F, -19.0F, -16.0F, 32.0F, 19.0F, 32.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(352, 90).mirror(false)
                .addBox(-16.0F, -19.0F, -16.0F, 32.0F, 4.0F, 32.0F, new CubeDeformation(-0.05F, -0.05F, -0.05F))
                .texOffs(352, 51).mirror(false)
                .addBox(-16.0F, -2.0F, -16.0F, 32.0F, 7.0F, 32.0F, new CubeDeformation(-0.15F, -0.15F, -0.15F))
                .texOffs(416, 0).mirror(false)
                .addBox(-16.0F, 4.0F, -16.0F, 32.0F, 0.0F, 32.0F, new CubeDeformation(-0.17F, -0.17F, -0.17F)), PartPose.offsetAndRotation(0.0F, -10.5F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p6 = p5.addOrReplaceChild("bone", CubeListBuilder.create()
                .texOffs(448, 90).mirror(false)
                .addBox(-13.0F, -45.0F, -16.0F, 26.0F, 26.0F, 3.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(448, 51).mirror(false)
                .addBox(-13.0F, -45.0F, -16.0F, 26.0F, 26.0F, 3.0F, new CubeDeformation(-0.05F, -0.05F, -0.05F)), PartPose.offsetAndRotation(0.0F, -6.0F, -32.0F, -1.57079633F, 0.0F, 0.0F));
        PartDefinition p7 = p5.addOrReplaceChild("logs", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p8 = p7.addOrReplaceChild("wood", CubeListBuilder.create()
                .texOffs(352, 0).mirror(false)
                .addBox(-13.0F, -19.0F, -32.0F, 5.0F, 18.0F, 5.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(352, 0).mirror(false)
                .addBox(8.0F, -19.0F, -32.0F, 5.0F, 18.0F, 5.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(352, 51).mirror(false)
                .addBox(-2.5F, -19.0F, -32.0F, 5.0F, 17.0F, 5.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(0.0F, -22.0F, 0.0F, 1.57079633F, 0.0F, 0.0F));
        PartDefinition p9 = p7.addOrReplaceChild("wood2", CubeListBuilder.create()
                .texOffs(352, 0).mirror(false)
                .addBox(-13.0F, -19.0F, -32.0F, 5.0F, 18.0F, 5.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(352, 0).mirror(false)
                .addBox(8.0F, -19.0F, -32.0F, 5.0F, 18.0F, 5.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(352, 51).mirror(false)
                .addBox(-2.5F, -19.0F, -32.0F, 5.0F, 17.0F, 5.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(0.0F, -22.0F, 0.0F, 1.57079633F, -1.57079633F, 0.0F));
        PartDefinition p10 = p7.addOrReplaceChild("wood3", CubeListBuilder.create()
                .texOffs(352, 0).mirror(false)
                .addBox(-13.0F, -19.0F, -32.0F, 5.0F, 18.0F, 5.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(352, 0).mirror(false)
                .addBox(8.0F, -19.0F, -32.0F, 5.0F, 18.0F, 5.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(352, 51).mirror(false)
                .addBox(-2.5F, -19.0F, -32.0F, 5.0F, 17.0F, 5.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(0.0F, -22.0F, 0.0F, 1.57079633F, 1.57079633F, 0.0F));
        PartDefinition p11 = p7.addOrReplaceChild("wood4", CubeListBuilder.create()
                .texOffs(352, 0).mirror(false)
                .addBox(-13.0F, -19.0F, -32.0F, 5.0F, 18.0F, 5.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(352, 0).mirror(false)
                .addBox(8.0F, -19.0F, -32.0F, 5.0F, 18.0F, 5.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(352, 51).mirror(false)
                .addBox(-2.5F, -19.0F, -32.0F, 5.0F, 17.0F, 5.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(0.0F, -22.0F, 0.0F, 1.57079633F, 3.14159265F, 0.0F));
        PartDefinition p12 = p5.addOrReplaceChild("other", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p13 = p12.addOrReplaceChild("fire", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 5.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p14 = p13.addOrReplaceChild("front", CubeListBuilder.create()
                .texOffs(353, 126).mirror(false)
                .addBox(-16.0F, -6.0F, -16.5F, 32.0F, 6.0F, 0.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.ZERO);
        PartDefinition p15 = p13.addOrReplaceChild("front2", CubeListBuilder.create()
                .texOffs(353, 126).mirror(false)
                .addBox(-16.0F, -9.0F, -16.5F, 32.0F, 9.0F, 0.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -1.57079633F, 0.0F));
        PartDefinition p16 = p15.addOrReplaceChild("front3", CubeListBuilder.create()
                .texOffs(353, 126).mirror(false)
                .addBox(-16.0F, -13.0F, -16.5F, 32.0F, 13.0F, 0.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -1.57079633F, 0.0F));
        PartDefinition p17 = p16.addOrReplaceChild("front4", CubeListBuilder.create()
                .texOffs(353, 126).mirror(false)
                .addBox(-16.0F, -9.0F, -16.5F, 32.0F, 9.0F, 0.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -1.57079633F, 0.0F));
        PartDefinition p18 = p12.addOrReplaceChild("spin", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -7.5F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p19 = p18.addOrReplaceChild("spoon", CubeListBuilder.create(), PartPose.offsetAndRotation(13.0F, -33.0F, 9.0F, 2.0943951F, -0.610865238F, 1.57079633F));
        PartDefinition p20 = p19.addOrReplaceChild("bottom", CubeListBuilder.create()
                .texOffs(372, 4).mirror(false)
                .addBox(-1.0F, 1.0F, 0.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(367, 2).mirror(false)
                .addBox(-1.0F, 0.0F, 0.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(0.0F, -2.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p21 = p19.addOrReplaceChild("top", CubeListBuilder.create()
                .texOffs(367, 0).mirror(false)
                .addBox(19.0F, -22.0F, 0.0F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(352, 0).mirror(false)
                .addBox(20.0F, -24.0F, 0.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(352, 73).mirror(false)
                .addBox(21.0F, -31.0F, 0.0F, 9.0F, 9.0F, 1.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(1.0F, -1.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p22 = p19.addOrReplaceChild("rod", CubeListBuilder.create()
                .texOffs(367, 2).mirror(false)
                .addBox(0.0F, -1.0F, 0.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(367, 2).mirror(false)
                .addBox(1.0F, -2.0F, 0.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(367, 2).mirror(false)
                .addBox(3.0F, -4.0F, 0.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(367, 2).mirror(false)
                .addBox(2.0F, -3.0F, 0.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(367, 2).mirror(false)
                .addBox(4.0F, -5.0F, 0.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(367, 2).mirror(false)
                .addBox(5.0F, -6.0F, 0.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(367, 2).mirror(false)
                .addBox(7.0F, -8.0F, 0.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(367, 2).mirror(false)
                .addBox(6.0F, -7.0F, 0.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(367, 2).mirror(false)
                .addBox(8.0F, -9.0F, 0.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(367, 2).mirror(false)
                .addBox(9.0F, -10.0F, 0.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(367, 2).mirror(false)
                .addBox(11.0F, -12.0F, 0.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(367, 2).mirror(false)
                .addBox(10.0F, -11.0F, 0.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(367, 2).mirror(false)
                .addBox(12.0F, -13.0F, 0.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(367, 2).mirror(false)
                .addBox(13.0F, -14.0F, 0.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(367, 2).mirror(false)
                .addBox(14.0F, -15.0F, 0.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(367, 2).mirror(false)
                .addBox(15.0F, -16.0F, 0.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(367, 2).mirror(false)
                .addBox(16.0F, -17.0F, 0.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(367, 2).mirror(false)
                .addBox(17.0F, -18.0F, 0.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(367, 2).mirror(false)
                .addBox(18.0F, -19.0F, 0.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(367, 2).mirror(false)
                .addBox(19.0F, -20.0F, 0.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(0.0F, -2.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p23 = p1.addOrReplaceChild("right_eye", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 24.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p24 = p2.addOrReplaceChild("left_eye", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 24.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p25 = p3.addOrReplaceChild("mouth", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 24.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        return LayerDefinition.create(mesh, 512, 256);
    }

    /** Preserve source expression units; static scales are reapplied after every pose reset. */
    public static ReferenceMobDefinition.Builder configure(ReferenceMobDefinition.Builder builder) {
        builder.cemDriver("slime");
        builder.hostAlias("body", "__cem_host_body");
        builder.hostAlias("right_eye", "__cem_host_right_eye");
        builder.hostAlias("left_eye", "__cem_host_left_eye");
        builder.hostAlias("mouth", "__cem_host_mouth");
        builder.boneScale("cauldron", 0.5F);
        builder.expression("spin.ry", "time/6");
        return builder;
    }
}
