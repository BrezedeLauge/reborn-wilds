/*
 * Generated adaptation of ReCrafted Mobs 11.0 by GillsBills and credited contributors.
 * Asset license: CC-BY-NC-SA-4.0; https://creativecommons.org/licenses/by-nc-sa/4.0/
 * Source: https://modrinth.com/resourcepack/recrafted-mobs
 * Model: assets/minecraft/optifine/cem/wither/wither5.jem
 * SHA256: f0e8fbbbd3e1c67cb73587bef8c14996bbdf7c58ee3f3c1f23ee7961c7e444d9
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

public final class CemNamelessOneGeometry {
    private CemNamelessOneGeometry() {}

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        PartDefinition p0 = root.addOrReplaceChild("__cem_host_body1", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p1 = root.addOrReplaceChild("__cem_host_body2", CubeListBuilder.create(), PartPose.offsetAndRotation(-2.0F, 6.9F, -0.5F, 0.20420352F, 0.0F, 0.0F));
        PartDefinition p2 = root.addOrReplaceChild("__cem_host_body3", CubeListBuilder.create(), PartPose.offsetAndRotation(-2.0F, 16.6922281F, 1.52787293F, 0.83252203F, 0.0F, 0.0F));
        PartDefinition p3 = root.addOrReplaceChild("__cem_host_head1", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p4 = root.addOrReplaceChild("__cem_host_head2", CubeListBuilder.create(), PartPose.offsetAndRotation(-8.0F, 4.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p5 = root.addOrReplaceChild("__cem_host_head3", CubeListBuilder.create(), PartPose.offsetAndRotation(10.0F, 4.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p6 = p0.addOrReplaceChild("body1", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 24.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p7 = p6.addOrReplaceChild("necro", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -16.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p8 = p7.addOrReplaceChild("head", CubeListBuilder.create()
                .texOffs(0, 0).mirror(false)
                .addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(32, 0).mirror(false)
                .addBox(-4.0F, -10.75F, -4.0F, 8.0F, 6.0F, 8.0F, new CubeDeformation(0.35F, 0.35F, 0.35F)), PartPose.ZERO);
        PartDefinition p9 = p7.addOrReplaceChild("body", CubeListBuilder.create()
                .texOffs(0, 16).mirror(false)
                .addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(0, 32).mirror(false)
                .addBox(-9.0F, -2.0F, -2.4F, 5.0F, 5.0F, 6.0F, new CubeDeformation(0.1F, 0.1F, 0.1F)), PartPose.ZERO);
        PartDefinition p10 = p9.addOrReplaceChild("body1_sub_14", CubeListBuilder.create()
                .texOffs(40, 14).mirror(true)
                .addBox(-8.0F, -1.0F, -0.9F, 16.0F, 23.0F, 3.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(0, 32).mirror(true)
                .addBox(4.0F, -2.0F, -2.4F, 5.0F, 5.0F, 6.0F, new CubeDeformation(0.1F, 0.1F, 0.1F)), PartPose.ZERO);
        PartDefinition p11 = p7.addOrReplaceChild("left_arm", CubeListBuilder.create()
                .texOffs(32, 16).mirror(true)
                .addBox(9.0F, -2.0F, -1.0F, 2.0F, 12.0F, 2.0F, new CubeDeformation(0.1F, 0.1F, 0.1F)), PartPose.offsetAndRotation(-5.0F, 2.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p12 = p7.addOrReplaceChild("right_arm", CubeListBuilder.create()
                .texOffs(32, 16).mirror(false)
                .addBox(-11.0F, -2.0F, -1.0F, 2.0F, 12.0F, 2.0F, new CubeDeformation(0.1F, 0.1F, 0.1F)), PartPose.offsetAndRotation(5.0F, 2.0F, 0.0F, -1.04719755F, 0.0F, 0.0F));
        PartDefinition p13 = p12.addOrReplaceChild("bone5", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p14 = p13.addOrReplaceChild("body1_sub_18", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p15 = p13.addOrReplaceChild("bone4", CubeListBuilder.create(), PartPose.offsetAndRotation(-9.0F, 9.0F, 5.0F, 1.57079633F, 0.0F, 0.0F));
        PartDefinition p16 = p15.addOrReplaceChild("body1_sub_20", CubeListBuilder.create()
                .texOffs(0, 43).mirror(true)
                .addBox(-1.5F, -10.5F, -0.5F, 1.0F, 26.0F, 1.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(4, 44).mirror(true)
                .addBox(-2.5F, -11.5F, -1.5F, 3.0F, 1.0F, 3.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.ZERO);
        PartDefinition p17 = p15.addOrReplaceChild("bone6", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p18 = p17.addOrReplaceChild("body1_sub_22", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p19 = p17.addOrReplaceChild("bone7", CubeListBuilder.create(), PartPose.offsetAndRotation(-1.0F, -11.0F, 0.0F, 0.0F, 1.57079633F, 0.0F));
        PartDefinition p20 = p19.addOrReplaceChild("body1_sub_24", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p21 = p19.addOrReplaceChild("bone8", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 1.57079633F, 0.0F));
        PartDefinition p22 = p21.addOrReplaceChild("body1_sub_26", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p23 = p21.addOrReplaceChild("bone9", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 1.57079633F, 0.0F));
        PartDefinition p24 = p23.addOrReplaceChild("body1_sub_28", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p25 = p13.addOrReplaceChild("bone11", CubeListBuilder.create(), PartPose.offsetAndRotation(-11.0F, 9.0F, -10.0F, 0.0F, 0.0F, -0.34906585F));
        PartDefinition p26 = p25.addOrReplaceChild("body1_sub_30", CubeListBuilder.create()
                .texOffs(4, 54).mirror(true)
                .addBox(-1.0F, -1.5F, -1.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.2F, 0.2F, 0.2F)), PartPose.ZERO);
        PartDefinition p27 = p13.addOrReplaceChild("bone10", CubeListBuilder.create(), PartPose.offsetAndRotation(-10.0F, 9.0F, -9.0F, 0.0872664626F, 0.0F, -0.392699082F));
        PartDefinition p28 = p27.addOrReplaceChild("body1_sub_32", CubeListBuilder.create()
                .texOffs(4, 48).mirror(true)
                .addBox(-1.5F, -1.5F, -1.25F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.ZERO);
        PartDefinition p29 = p7.addOrReplaceChild("right_leg", CubeListBuilder.create()
                .texOffs(24, 16).mirror(false)
                .addBox(-5.0F, 0.0F, -1.1F, 2.0F, 12.0F, 2.0F, new CubeDeformation(0.1F, 0.1F, 0.1F)), PartPose.offsetAndRotation(2.0F, 12.0F, 0.1F, 0.0F, 0.0F, 0.0F));
        PartDefinition p30 = p29.addOrReplaceChild("body1_sub_34", CubeListBuilder.create()
                .texOffs(24, 30).mirror(true)
                .addBox(-6.0F, -0.1F, -2.1F, 4.0F, 10.0F, 4.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.ZERO);
        PartDefinition p31 = p7.addOrReplaceChild("left_leg", CubeListBuilder.create()
                .texOffs(24, 30).mirror(false)
                .addBox(2.0F, -0.1F, -2.1F, 4.0F, 10.0F, 4.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(-2.0F, 12.0F, 0.1F, 0.0F, 0.0F, 0.0F));
        PartDefinition p32 = p31.addOrReplaceChild("body1_sub_36", CubeListBuilder.create()
                .texOffs(24, 16).mirror(true)
                .addBox(3.0F, 0.0F, -1.1F, 2.0F, 12.0F, 2.0F, new CubeDeformation(0.1F, 0.1F, 0.1F)), PartPose.ZERO);
        PartDefinition p33 = p1.addOrReplaceChild("body2", CubeListBuilder.create(), PartPose.offsetAndRotation(3.0F, 17.1F, 0.5F, 0.0F, 0.0F, 0.0F));
        PartDefinition p34 = p2.addOrReplaceChild("body3", CubeListBuilder.create(), PartPose.offsetAndRotation(3.0F, 7.1F, 0.5F, 0.0F, 0.0F, 0.0F));
        PartDefinition p35 = p3.addOrReplaceChild("head1", CubeListBuilder.create(), PartPose.offsetAndRotation(1.0F, 24.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p36 = p4.addOrReplaceChild("head2", CubeListBuilder.create(), PartPose.offsetAndRotation(9.0F, 20.0F, 1.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p37 = p5.addOrReplaceChild("head3", CubeListBuilder.create(), PartPose.offsetAndRotation(-9.0F, 20.0F, 1.0F, 0.0F, 0.0F, 0.0F));
        return LayerDefinition.create(mesh, 128, 128);
    }

    /** Preserve source expression units; static scales are reapplied after every pose reset. */
    public static ReferenceMobDefinition.Builder configure(ReferenceMobDefinition.Builder builder) {
        builder.cemDriver("wither");
        builder.hostAlias("body1", "__cem_host_body1");
        builder.hostAlias("body2", "__cem_host_body2");
        builder.hostAlias("body3", "__cem_host_body3");
        builder.hostAlias("head1", "__cem_host_head1");
        builder.hostAlias("head2", "__cem_host_head2");
        builder.hostAlias("head3", "__cem_host_head3");
        builder.boneScale("necro", 0.75F);
        builder.expression("right_leg.rx", "-sin(limb_swing)*limb_speed");
        builder.expression("left_leg.rx", "sin(limb_swing)*limb_speed");
        builder.expression("left_arm.rx", "-sin(limb_swing)*limb_speed");
        builder.expression("right_arm.rx", "torad(-60)+sin(limb_swing)*limb_speed");
        builder.expression("head.rx", "head1.rx");
        builder.expression("head.ry", "head1.ry");
        builder.expression("head.rz", "head1.rz");
        return builder;
    }
}
