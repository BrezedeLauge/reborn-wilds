/*
 * Generated adaptation of ReCrafted Mobs 11.0 by GillsBills and credited contributors.
 * Asset license: CC-BY-NC-SA-4.0; https://creativecommons.org/licenses/by-nc-sa/4.0/
 * Source: https://modrinth.com/resourcepack/recrafted-mobs
 * Model: assets/minecraft/optifine/cem/wither/wither3.jem
 * SHA256: e911059e6b01bee843ce7acc712f4759de9b7a2830a5c3e79043ec8feea5acf4
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

public final class CemWretchedWraithGeometry {
    private CemWretchedWraithGeometry() {}

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
        PartDefinition p7 = p6.addOrReplaceChild("wrethed", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -16.0F, -1.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p8 = p7.addOrReplaceChild("head", CubeListBuilder.create()
                .texOffs(48, 75).mirror(false)
                .addBox(-7.0F, -17.0F, -5.0F, 14.0F, 1.0F, 13.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(78, 83).mirror(false)
                .addBox(-7.0F, -24.0F, -1.0F, 0.0F, 7.0F, 6.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(70, 101).mirror(false)
                .addBox(-4.0F, -31.0F, -0.5F, 0.0F, 7.0F, 5.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(80, 101).mirror(false)
                .addBox(4.0F, -31.0F, -0.5F, 0.0F, 7.0F, 5.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(82, 92).mirror(false)
                .addBox(8.0F, -28.0F, -2.0F, 0.0F, 4.0F, 4.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(90, 92).mirror(false)
                .addBox(-8.0F, -28.0F, -2.0F, 0.0F, 4.0F, 4.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(65, 111).mirror(false)
                .addBox(4.0F, -19.0F, 5.0F, 0.0F, 2.0F, 2.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(69, 111).mirror(false)
                .addBox(-4.0F, -19.0F, 5.0F, 0.0F, 2.0F, 2.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(90, 83).mirror(false)
                .addBox(7.0F, -24.0F, -1.0F, 0.0F, 7.0F, 6.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(0, 27).mirror(false)
                .addBox(-7.0F, -16.0F, -5.0F, 14.0F, 16.0F, 13.0F, new CubeDeformation(-0.01F, -0.01F, -0.01F))
                .texOffs(0, 0).mirror(false)
                .addBox(-6.0F, -16.0F, -4.0F, 12.0F, 14.0F, 13.0F, new CubeDeformation(-1.0F, -1.0F, -1.0F)), PartPose.ZERO);
        PartDefinition p9 = p8.addOrReplaceChild("crown", CubeListBuilder.create(), PartPose.offsetAndRotation(1.0F, -6.0F, -4.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p10 = p9.addOrReplaceChild("shard", CubeListBuilder.create(), PartPose.offsetAndRotation(-2.0F, -21.0F, 3.0F, 0.785398163F, 0.0F, 0.0F));
        PartDefinition p11 = p10.addOrReplaceChild("bone", CubeListBuilder.create()
                .texOffs(37, 0).mirror(false)
                .addBox(-1.555F, -2.5F, -3.0F, 6.0F, 6.0F, 6.0F, new CubeDeformation(-1.0F, -1.0F, -1.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.785398163F, 0.0F));
        PartDefinition p12 = p8.addOrReplaceChild("crown2", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -15.0F, -5.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p13 = p12.addOrReplaceChild("bone2", CubeListBuilder.create()
                .texOffs(57, 110).mirror(false)
                .addBox(-2.0F, -8.0F, 0.0F, 4.0F, 8.0F, 0.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0436332313F, 0.0F, 0.0F));
        PartDefinition p14 = p12.addOrReplaceChild("bone3", CubeListBuilder.create()
                .texOffs(57, 106).mirror(false)
                .addBox(-4.0F, -6.0F, 1.0F, 2.0F, 4.0F, 0.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(61, 106).mirror(false)
                .addBox(2.0F, -6.0F, 1.0F, 2.0F, 4.0F, 0.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(57, 89).mirror(false)
                .addBox(5.0F, -12.0F, 2.0F, 3.0F, 10.0F, 0.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(63, 89).mirror(false)
                .addBox(-8.0F, -12.0F, 2.0F, 3.0F, 10.0F, 0.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(57, 99).mirror(false)
                .addBox(-9.0F, -9.0F, 7.0F, 6.0F, 7.0F, 0.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(70, 99).mirror(false)
                .addBox(3.0F, -9.0F, 7.0F, 6.0F, 7.0F, 0.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.ZERO);
        PartDefinition p15 = p7.addOrReplaceChild("bodycold", CubeListBuilder.create()
                .texOffs(54, 3).mirror(false)
                .addBox(-7.0F, -0.02F, -2.0F, 14.0F, 29.0F, 9.0F, new CubeDeformation(-0.01F, -0.01F, -0.01F))
                .texOffs(100, 0).mirror(false)
                .addBox(-1.0F, -2.0F, 3.0F, 2.0F, 24.0F, 2.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(0, 56).mirror(false)
                .addBox(-6.0F, 2.0F, -1.0F, 12.0F, 6.0F, 6.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.ZERO);
        PartDefinition p16 = p7.addOrReplaceChild("right_arm2", CubeListBuilder.create()
                .texOffs(54, 54).mirror(false)
                .addBox(-6.0F, 0.0F, -1.0F, 6.0F, 14.0F, 7.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(54, 41).mirror(false)
                .addBox(-6.0F, -6.0F, -1.0F, 6.0F, 6.0F, 7.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(100, 0).mirror(false)
                .addBox(-4.0F, 0.0F, 0.0F, 2.0F, 24.0F, 2.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(0, 69).mirror(false)
                .addBox(-5.0F, 19.0F, -1.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(16, 68).mirror(false)
                .addBox(-6.0F, 24.0F, -2.0F, 6.0F, 7.0F, 4.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(27, 70).mirror(false)
                .addBox(-6.0F, 24.0F, 2.0F, 6.0F, 7.0F, 9.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(0, 78).mirror(false)
                .addBox(-5.0F, 19.0F, 3.0F, 4.0F, 5.0F, 6.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(-7.0F, 0.0F, 0.0F, -0.34906585F, 0.0F, 0.0F));
        PartDefinition p17 = p7.addOrReplaceChild("left_arm2", CubeListBuilder.create()
                .texOffs(80, 54).mirror(false)
                .addBox(0.0F, 0.0F, -1.0F, 6.0F, 14.0F, 7.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(80, 41).mirror(false)
                .addBox(0.0F, -6.0F, -1.0F, 6.0F, 6.0F, 7.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(100, 0).mirror(false)
                .addBox(2.0F, 0.0F, 0.0F, 2.0F, 24.0F, 2.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(0, 90).mirror(false)
                .addBox(1.0F, 19.0F, -1.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(0, 99).mirror(false)
                .addBox(0.0F, 24.0F, -2.0F, 6.0F, 7.0F, 4.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(27, 88).mirror(false)
                .addBox(0.0F, 24.0F, 2.0F, 6.0F, 7.0F, 9.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(16, 86).mirror(false)
                .addBox(1.0F, 19.0F, 3.0F, 4.0F, 5.0F, 6.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(7.0F, 0.0F, 0.0F, -0.34906585F, 0.0F, 0.0F));
        PartDefinition p18 = p1.addOrReplaceChild("body2", CubeListBuilder.create(), PartPose.offsetAndRotation(3.0F, 17.1F, 0.5F, 0.0F, 0.0F, 0.0F));
        PartDefinition p19 = p2.addOrReplaceChild("body3", CubeListBuilder.create(), PartPose.offsetAndRotation(3.0F, 7.1F, 0.5F, 0.0F, 0.0F, 0.0F));
        PartDefinition p20 = p3.addOrReplaceChild("head1", CubeListBuilder.create(), PartPose.offsetAndRotation(1.0F, 24.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p21 = p4.addOrReplaceChild("head2", CubeListBuilder.create(), PartPose.offsetAndRotation(9.0F, 20.0F, 1.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p22 = p5.addOrReplaceChild("head3", CubeListBuilder.create(), PartPose.offsetAndRotation(-9.0F, 20.0F, 1.0F, 0.0F, 0.0F, 0.0F));
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
        builder.boneScale("wrethed", 0.5F);
        builder.expression("head.rx", "head1.rx");
        builder.expression("head.ry", "head1.ry");
        builder.expression("head.rz", "head1.rz");
        builder.expression("bodycold.rx", "torad(12.5) + sin(torad(12.5*3) + time/42) *0.20");
        builder.expression("right_arm2.rx", "torad(-10) + -(limb_speed+limb_speed*sin(limb_swing/60)) + 0.1*sin(age/10)");
        builder.expression("left_arm2.rx", "torad(-10) + -(limb_speed+limb_speed*sin(limb_swing/55)) + 0.1*sin(age/9.5)");
        return builder;
    }
}
