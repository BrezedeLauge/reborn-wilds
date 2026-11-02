/*
 * Generated adaptation of ReCrafted Mobs 11.0 by GillsBills and credited contributors.
 * Asset license: CC-BY-NC-SA-4.0; https://creativecommons.org/licenses/by-nc-sa/4.0/
 * Source: https://modrinth.com/resourcepack/recrafted-mobs
 * Model: assets/minecraft/optifine/cem/warden/warden5.jem
 * SHA256: 4576eae51a4521fd2a05a3f35ec4b55555efd39d6dfe4a957a989afe1605e1f3
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

public final class CemTempestGolemGeometry {
    private CemTempestGolemGeometry() {}

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        PartDefinition p0 = root.addOrReplaceChild("__cem_host_root", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 24.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p1 = p0.addOrReplaceChild("__cem_host_torso", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -21.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p2 = p1.addOrReplaceChild("__cem_host_head", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -13.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p3 = p2.addOrReplaceChild("__cem_host_right_tendril", CubeListBuilder.create(), PartPose.offsetAndRotation(-8.0F, -12.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p4 = p2.addOrReplaceChild("__cem_host_left_tendril", CubeListBuilder.create(), PartPose.offsetAndRotation(8.0F, -12.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p5 = p1.addOrReplaceChild("__cem_host_right_ribcage", CubeListBuilder.create(), PartPose.offsetAndRotation(-7.0F, -2.0F, -4.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p6 = p1.addOrReplaceChild("__cem_host_left_ribcage", CubeListBuilder.create(), PartPose.offsetAndRotation(7.0F, -2.0F, -4.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p7 = p1.addOrReplaceChild("__cem_host_right_arm", CubeListBuilder.create(), PartPose.offsetAndRotation(-13.0F, -13.0F, 1.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p8 = p1.addOrReplaceChild("__cem_host_left_arm", CubeListBuilder.create(), PartPose.offsetAndRotation(13.0F, -13.0F, 1.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p9 = p0.addOrReplaceChild("__cem_host_right_leg", CubeListBuilder.create(), PartPose.offsetAndRotation(-5.9F, -13.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p10 = p0.addOrReplaceChild("__cem_host_left_leg", CubeListBuilder.create(), PartPose.offsetAndRotation(5.9F, -13.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p11 = p2.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 34.0F, 1.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p12 = p4.addOrReplaceChild("left_tendril", CubeListBuilder.create(), PartPose.offsetAndRotation(-8.0F, 46.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p13 = p3.addOrReplaceChild("right_tendril", CubeListBuilder.create(), PartPose.offsetAndRotation(8.0F, 46.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p14 = p1.addOrReplaceChild("torso", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 21.0F, 1.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p15 = p14.addOrReplaceChild("monsters", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -21.0F, -1.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p16 = p15.addOrReplaceChild("tempest", CubeListBuilder.create()
                .texOffs(0, 0).mirror(false)
                .addBox(-27.0F, -30.0F, -19.0F, 54.0F, 29.0F, 38.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(0.0F, 22.0F, 1.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p17 = p16.addOrReplaceChild("middle", CubeListBuilder.create()
                .texOffs(0, 68).mirror(false)
                .addBox(-34.0F, -66.0F, -20.0F, 68.0F, 36.0F, 40.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(146, 0).mirror(false)
                .addBox(-11.0F, -59.0F, -26.0F, 22.0F, 22.0F, 6.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.ZERO);
        PartDefinition p18 = p17.addOrReplaceChild("bottom_arms", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p19 = p18.addOrReplaceChild("bottom_left_arm", CubeListBuilder.create()
                .texOffs(312, 0).mirror(true)
                .addBox(0.0F, -5.0F, -4.0F, 32.0F, 9.0F, 8.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(34.0F, -46.0F, -1.0F, 0.0F, 1.13446401F, 0.0F));
        PartDefinition p20 = p19.addOrReplaceChild("bottom_left_arm2", CubeListBuilder.create()
                .texOffs(344, 17).mirror(true)
                .addBox(0.0F, -8.0F, -7.0F, 38.0F, 15.0F, 14.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(288, 116).mirror(true)
                .addBox(0.0F, -16.0F, 7.0F, 48.0F, 31.0F, 4.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(299, 49).mirror(true)
                .addBox(25.0F, -11.0F, -7.0F, 13.0F, 3.0F, 14.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(32.0F, 0.0F, 0.0F, 0.0F, 1.83259571F, 0.0F));
        PartDefinition p21 = p20.addOrReplaceChild("middletop2", CubeListBuilder.create()
                .texOffs(299, 49).mirror(true)
                .addBox(-6.0F, -11.0F, -7.0F, 13.0F, 3.0F, 14.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(32.0F, -1.0F, 0.0F, 0.0F, 0.0F, 3.14159265F));
        PartDefinition p22 = p18.addOrReplaceChild("bottom_right_arm", CubeListBuilder.create()
                .texOffs(312, 0).mirror(false)
                .addBox(-32.0F, -5.0F, -4.0F, 32.0F, 9.0F, 8.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(-34.0F, -46.0F, -1.0F, 0.0F, -1.13446401F, 0.0F));
        PartDefinition p23 = p22.addOrReplaceChild("bottom_right_arm2", CubeListBuilder.create()
                .texOffs(344, 17).mirror(false)
                .addBox(-38.0F, -8.0F, -7.0F, 38.0F, 15.0F, 14.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(346, 73).mirror(false)
                .addBox(-48.0F, -16.0F, 7.0F, 48.0F, 31.0F, 4.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(299, 49).mirror(false)
                .addBox(-38.0F, -11.0F, -7.0F, 13.0F, 3.0F, 14.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(-32.0F, 0.0F, 0.0F, 0.0F, -1.83259571F, 0.0F));
        PartDefinition p24 = p23.addOrReplaceChild("middletop", CubeListBuilder.create()
                .texOffs(299, 49).mirror(false)
                .addBox(-7.0F, -11.0F, -7.0F, 13.0F, 3.0F, 14.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(-32.0F, -1.0F, 0.0F, 0.0F, 0.0F, -3.14159265F));
        PartDefinition p25 = p17.addOrReplaceChild("top2", CubeListBuilder.create()
                .texOffs(0, 145).mirror(false)
                .addBox(-48.0F, -85.0F, -22.0F, 96.0F, 56.0F, 44.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(0, 25).mirror(false)
                .addBox(28.0F, -49.0F, -25.0F, 10.0F, 10.0F, 3.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(0, 25).mirror(false)
                .addBox(-38.0F, -49.0F, -25.0F, 10.0F, 10.0F, 3.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(0, 25).mirror(false)
                .addBox(28.0F, -76.0F, -25.0F, 10.0F, 10.0F, 3.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(0, 25).mirror(false)
                .addBox(-38.0F, -76.0F, -25.0F, 10.0F, 10.0F, 3.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(0.0F, -37.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p26 = p25.addOrReplaceChild("top_arms", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 37.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p27 = p26.addOrReplaceChild("top_right_arm", CubeListBuilder.create()
                .texOffs(288, 151).mirror(false)
                .addBox(-40.0F, -8.0F, -8.0F, 40.0F, 16.0F, 16.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(-48.0F, -97.0F, -1.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p28 = p27.addOrReplaceChild("top_right_arm2", CubeListBuilder.create()
                .texOffs(280, 183).mirror(false)
                .addBox(-48.0F, -12.0F, -10.0F, 48.0F, 24.0F, 20.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(260, 227).mirror(false)
                .addBox(-120.0F, -2.0F, -10.0F, 72.0F, 4.0F, 20.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(240, 245).mirror(false)
                .addBox(-124.0F, -2.0F, -6.0F, 4.0F, 4.0F, 12.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(272, 251).mirror(false)
                .addBox(-128.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(-40.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p29 = p26.addOrReplaceChild("top_left_arm", CubeListBuilder.create()
                .texOffs(288, 151).mirror(true)
                .addBox(0.0F, -8.0F, -8.0F, 40.0F, 16.0F, 16.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(48.0F, -97.0F, -1.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p30 = p29.addOrReplaceChild("top_left_arm2", CubeListBuilder.create()
                .texOffs(280, 183).mirror(true)
                .addBox(0.0F, -12.0F, -10.0F, 48.0F, 24.0F, 20.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(260, 227).mirror(true)
                .addBox(48.0F, -2.0F, -10.0F, 72.0F, 4.0F, 20.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(240, 245).mirror(true)
                .addBox(120.0F, -2.0F, -6.0F, 4.0F, 4.0F, 12.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(272, 251).mirror(true)
                .addBox(124.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(40.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p31 = p25.addOrReplaceChild("head4", CubeListBuilder.create()
                .texOffs(184, 0).mirror(false)
                .addBox(-24.0F, -6.0F, -32.0F, 48.0F, 17.0F, 32.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(176, 77).mirror(false)
                .addBox(-17.0F, -11.0F, -32.0F, 5.0F, 5.0F, 0.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(176, 77).mirror(false)
                .addBox(12.0F, -11.0F, -32.0F, 5.0F, 5.0F, 0.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(0.0F, -82.0F, -22.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p32 = p31.addOrReplaceChild("middle_banner", CubeListBuilder.create()
                .texOffs(236, 139).mirror(false)
                .addBox(-8.0F, -71.0F, -54.0F, 16.0F, 50.0F, 0.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(0.0F, 82.0F, 22.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p33 = p31.addOrReplaceChild("left_banner", CubeListBuilder.create()
                .texOffs(268, 149).mirror(false)
                .addBox(-8.0F, -71.0F, -54.0F, 10.0F, 40.0F, 0.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(19.0F, 82.0F, 30.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p34 = p31.addOrReplaceChild("right_banner", CubeListBuilder.create()
                .texOffs(268, 109).mirror(true)
                .addBox(-2.0F, -71.0F, -54.0F, 10.0F, 40.0F, 0.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(-19.0F, 82.0F, 30.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p35 = p31.addOrReplaceChild("head_top", CubeListBuilder.create()
                .texOffs(176, 51).mirror(false)
                .addBox(-23.0F, -114.0F, -53.0F, 46.0F, 26.0F, 31.0F, new CubeDeformation(-0.01F, -0.01F, -0.01F))
                .texOffs(293, 71).mirror(false)
                .addBox(-4.0F, -117.0F, -56.0F, 8.0F, 8.0F, 37.0F, new CubeDeformation(-0.01F, -0.01F, -0.01F))
                .texOffs(216, 108).mirror(false)
                .addBox(-4.0F, -102.0F, -59.0F, 8.0F, 12.0F, 6.0F, new CubeDeformation(-0.01F, -0.01F, -0.01F))
                .texOffs(176, 75).mirror(false)
                .addBox(11.5F, -103.5F, -53.0F, 2.0F, 2.0F, 0.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(176, 75).mirror(false)
                .addBox(-13.5F, -103.5F, -53.0F, 2.0F, 2.0F, 0.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(0.0F, 82.0F, 22.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p36 = p25.addOrReplaceChild("left_wind", CubeListBuilder.create()
                .texOffs(26, 29).mirror(false)
                .addBox(33.0F, -88.0F, -3.0F, 6.0F, 3.0F, 6.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(0, 6).mirror(false)
                .addBox(34.0F, -103.0F, -2.0F, 4.0F, 15.0F, 4.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.ZERO);
        PartDefinition p37 = p36.addOrReplaceChild("blade1", CubeListBuilder.create()
                .texOffs(16, 11).mirror(false)
                .addBox(-9.0F, -15.0F, 0.0F, 18.0F, 14.0F, 0.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(36.0F, -88.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p38 = p36.addOrReplaceChild("blade2", CubeListBuilder.create()
                .texOffs(16, 11).mirror(false)
                .addBox(-9.0F, -15.0F, 0.0F, 18.0F, 14.0F, 0.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(36.0F, -88.0F, 0.0F, 0.0F, 1.57079633F, 0.0F));
        PartDefinition p39 = p25.addOrReplaceChild("right_wind", CubeListBuilder.create()
                .texOffs(26, 29).mirror(false)
                .addBox(33.0F, -88.0F, -3.0F, 6.0F, 3.0F, 6.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(0, 6).mirror(false)
                .addBox(34.0F, -103.0F, -2.0F, 4.0F, 15.0F, 4.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(-72.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p40 = p39.addOrReplaceChild("blade3", CubeListBuilder.create()
                .texOffs(16, 11).mirror(false)
                .addBox(-9.0F, -15.0F, 0.0F, 18.0F, 14.0F, 0.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(36.0F, -88.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p41 = p39.addOrReplaceChild("blade4", CubeListBuilder.create()
                .texOffs(16, 11).mirror(false)
                .addBox(-9.0F, -15.0F, 0.0F, 18.0F, 14.0F, 0.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(36.0F, -88.0F, 0.0F, 0.0F, 1.57079633F, 0.0F));
        PartDefinition p42 = p6.addOrReplaceChild("left_ribcage", CubeListBuilder.create(), PartPose.offsetAndRotation(-7.0F, 23.0F, 5.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p43 = p5.addOrReplaceChild("right_ribcage", CubeListBuilder.create(), PartPose.offsetAndRotation(7.0F, 23.0F, 5.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p44 = p8.addOrReplaceChild("left_arm", CubeListBuilder.create(), PartPose.offsetAndRotation(-13.0F, 34.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p45 = p7.addOrReplaceChild("right_arm", CubeListBuilder.create(), PartPose.offsetAndRotation(13.0F, 34.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p46 = p10.addOrReplaceChild("left_leg", CubeListBuilder.create(), PartPose.offsetAndRotation(-6.0F, 13.0F, 1.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p47 = p9.addOrReplaceChild("right_leg", CubeListBuilder.create(), PartPose.offsetAndRotation(6.0F, 13.0F, 1.0F, 0.0F, 0.0F, 0.0F));
        return LayerDefinition.create(mesh, 512, 512);
    }

    /** Preserve source expression units; static scales are reapplied after every pose reset. */
    public static ReferenceMobDefinition.Builder configure(ReferenceMobDefinition.Builder builder) {
        builder.cemDriver("warden");
        builder.hostAlias("root", "__cem_host_root");
        builder.hostAlias("body", "__cem_host_root");
        builder.hostAlias("torso", "__cem_host_torso");
        builder.hostAlias("head", "__cem_host_head");
        builder.hostAlias("right_tendril", "__cem_host_right_tendril");
        builder.hostAlias("left_tendril", "__cem_host_left_tendril");
        builder.hostAlias("right_ribcage", "__cem_host_right_ribcage");
        builder.hostAlias("left_ribcage", "__cem_host_left_ribcage");
        builder.hostAlias("right_arm", "__cem_host_right_arm");
        builder.hostAlias("left_arm", "__cem_host_left_arm");
        builder.hostAlias("right_leg", "__cem_host_right_leg");
        builder.hostAlias("left_leg", "__cem_host_left_leg");
        builder.boneScale("tempest", 0.75F);
        builder.expression("head4.rx", "head.rx");
        builder.expression("head4.ry", "head.ry");
        builder.expression("head4.rz", "head.rz");
        builder.expression("blade1.ry", "time/5");
        builder.expression("blade2.ry", "torad(90) + time/5");
        builder.expression("blade3.ry", "-time/5");
        builder.expression("blade4.ry", "torad(90) + -time/5");
        builder.expression("top_right_arm.rx", "right_arm.rx+(torad(-40))");
        builder.expression("top_right_arm.ry", "right_arm.ry+(torad(-20))");
        builder.expression("top_right_arm.rz", "right_arm.rz+(torad(-15))");
        builder.expression("top_left_arm.rx", "left_arm.rx+(torad(-40))");
        builder.expression("top_left_arm.ry", "left_arm.ry+(torad(20))");
        builder.expression("top_left_arm.rz", "left_arm.rz+(torad(15))");
        builder.expression("top_right_arm2.ry", "right_arm.ry+(torad(-40))");
        builder.expression("top_left_arm2.ry", "left_arm.ry+(torad(40))");
        builder.expression("render.shadow_size", "2");
        builder.expression("monsters.rx", "-torso.rx");
        builder.expression("monsters.ry", "-torso.ry");
        builder.expression("monsters.rz", "-torso.rz");
        return builder;
    }
}
