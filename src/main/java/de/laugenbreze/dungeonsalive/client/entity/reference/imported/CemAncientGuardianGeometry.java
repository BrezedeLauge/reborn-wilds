/*
 * Generated adaptation of ReCrafted Mobs 11.0 by GillsBills and credited contributors.
 * Asset license: CC-BY-NC-SA-4.0; https://creativecommons.org/licenses/by-nc-sa/4.0/
 * Source: https://modrinth.com/resourcepack/recrafted-mobs
 * Model: assets/minecraft/optifine/cem/elder_guardian/elder_guardian2.jem
 * SHA256: f83e59ef3784ccab42721aed5c4a7d55c85b35a3b0ecd238751267e7de76a02e
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

public final class CemAncientGuardianGeometry {
    private CemAncientGuardianGeometry() {}

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        PartDefinition p0 = root.addOrReplaceChild("__cem_host_body", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p1 = p0.addOrReplaceChild("__cem_host_eye", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, -8.25F, 0.0F, 0.0F, 0.0F));
        PartDefinition p2 = p0.addOrReplaceChild("__cem_host_tail1", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p3 = p2.addOrReplaceChild("__cem_host_tail2", CubeListBuilder.create(), PartPose.offsetAndRotation(-1.5F, 0.5F, 14.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p4 = p3.addOrReplaceChild("__cem_host_tail3", CubeListBuilder.create(), PartPose.offsetAndRotation(0.5F, 0.5F, 6.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p5 = p0.addOrReplaceChild("__cem_host_spine1", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 7.92F, 8.08F, 5.49778714F, 0.0F, 0.0F));
        PartDefinition p6 = p0.addOrReplaceChild("__cem_host_spine2", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 7.95677582F, -8.04322418F, 0.785398163F, 0.0F, 0.0F));
        PartDefinition p7 = p0.addOrReplaceChild("__cem_host_spine3", CubeListBuilder.create(), PartPose.offsetAndRotation(7.96670825F, 8.03329175F, 0.0F, 0.0F, 0.0F, 0.785398163F));
        PartDefinition p8 = p0.addOrReplaceChild("__cem_host_spine4", CubeListBuilder.create(), PartPose.offsetAndRotation(-7.9208006F, 8.0791994F, 0.0F, 0.0F, 0.0F, 5.49778714F));
        PartDefinition p9 = p0.addOrReplaceChild("__cem_host_spine5", CubeListBuilder.create(), PartPose.offsetAndRotation(-7.94770851F, 16.0F, -7.94770851F, 1.57079633F, 0.785398163F, 0.0F));
        PartDefinition p10 = p0.addOrReplaceChild("__cem_host_spine6", CubeListBuilder.create(), PartPose.offsetAndRotation(8.02269297F, 16.0F, -8.02269297F, 1.57079633F, 5.49778714F, 0.0F));
        PartDefinition p11 = p0.addOrReplaceChild("__cem_host_spine7", CubeListBuilder.create(), PartPose.offsetAndRotation(8.07681362F, 16.0F, 8.07681362F, 1.57079633F, 3.92699082F, 0.0F));
        PartDefinition p12 = p0.addOrReplaceChild("__cem_host_spine8", CubeListBuilder.create(), PartPose.offsetAndRotation(-8.06031218F, 16.0F, 8.06031218F, 1.57079633F, 2.35619449F, 0.0F));
        PartDefinition p13 = p0.addOrReplaceChild("__cem_host_spine9", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 23.98836F, 7.98836F, 3.92699082F, 0.0F, 0.0F));
        PartDefinition p14 = p0.addOrReplaceChild("__cem_host_spine10", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 23.9271096F, -7.92710958F, 2.35619449F, 0.0F, 0.0F));
        PartDefinition p15 = p0.addOrReplaceChild("__cem_host_spine11", CubeListBuilder.create(), PartPose.offsetAndRotation(7.93287428F, 23.9328743F, 0.0F, 0.0F, 0.0F, 2.35619449F));
        PartDefinition p16 = p0.addOrReplaceChild("__cem_host_spine12", CubeListBuilder.create(), PartPose.offsetAndRotation(-8.00035406F, 24.0003541F, 0.0F, 0.0F, 0.0F, 3.92699082F));
        PartDefinition p17 = p0.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 24.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p18 = p17.addOrReplaceChild("body_flipped", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p19 = p18.addOrReplaceChild("body_sub_1", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p20 = p17.addOrReplaceChild("Guardian", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p21 = p20.addOrReplaceChild("body2", CubeListBuilder.create()
                .texOffs(246, 252).mirror(false)
                .addBox(-31.0F, -36.0F, -48.0F, 72.0F, 72.0F, 12.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(78, 0).mirror(false)
                .addBox(-31.0F, 36.0F, -36.0F, 72.0F, 12.0F, 72.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(294, 27).mirror(false)
                .addBox(5.0F, -84.0F, -22.0F, 0.0F, 38.0F, 57.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(78, 84).mirror(false)
                .addBox(1.0F, -63.0F, -34.0F, 8.0F, 15.0F, 8.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(294, 122).mirror(false)
                .addBox(-17.0F, -7.0F, -51.0F, 45.0F, 22.0F, 3.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(-5.0F, -48.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p22 = p21.addOrReplaceChild("left_body", CubeListBuilder.create()
                .texOffs(78, 252).mirror(false)
                .addBox(-24.0F, -37.0F, -6.0F, 72.0F, 72.0F, 12.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(47.0F, 1.0F, -12.0F, 0.0F, -1.57079633F, 0.0F));
        PartDefinition p23 = p21.addOrReplaceChild("right_body", CubeListBuilder.create()
                .texOffs(78, 168).mirror(false)
                .addBox(-171.0F, -37.0F, -6.0F, 72.0F, 72.0F, 12.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(-37.0F, 1.0F, -135.0F, 0.0F, 1.57079633F, 0.0F));
        PartDefinition p24 = p21.addOrReplaceChild("top_body", CubeListBuilder.create()
                .texOffs(78, 84).mirror(false)
                .addBox(-37.0F, -6.0F, -36.0F, 72.0F, 12.0F, 72.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(6.0F, -42.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p25 = p21.addOrReplaceChild("back_body", CubeListBuilder.create()
                .texOffs(246, 168).mirror(false)
                .addBox(-36.0F, 4.0F, 4.0F, 72.0F, 72.0F, 12.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(5.0F, -40.0F, 52.0F, 0.0F, 3.14159265F, 0.0F));
        PartDefinition p26 = p21.addOrReplaceChild("eye2", CubeListBuilder.create()
                .texOffs(110, 84).mirror(false)
                .addBox(-8.5F, -8.0F, -0.25F, 17.0F, 16.0F, 0.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(5.0F, 4.0F, -51.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p27 = p21.addOrReplaceChild("spines", CubeListBuilder.create(), PartPose.offsetAndRotation(5.0F, -42.0F, -42.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p28 = p27.addOrReplaceChild("top_spines", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 42.0F, 42.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p29 = p28.addOrReplaceChild("spine_1", CubeListBuilder.create()
                .texOffs(78, 0).mirror(true)
                .addBox(-6.0F, -18.0F, -6.0F, 12.0F, 54.0F, 12.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(0.0F, -42.0F, -42.0F, -0.785398163F, 3.14159265F, 0.0F));
        PartDefinition p30 = p28.addOrReplaceChild("spine_2", CubeListBuilder.create()
                .texOffs(78, 0).mirror(false)
                .addBox(-6.0F, -18.0F, -6.0F, 12.0F, 54.0F, 12.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(0.0F, -42.0F, 42.0F, -0.785398163F, 0.0F, 0.0F));
        PartDefinition p31 = p28.addOrReplaceChild("spine3and4", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -42.0F, 0.0F, 0.0F, -1.57079633F, 0.0F));
        PartDefinition p32 = p31.addOrReplaceChild("spine_3", CubeListBuilder.create()
                .texOffs(78, 0).mirror(true)
                .addBox(-6.0F, -18.0F, -6.0F, 12.0F, 54.0F, 12.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -42.0F, -0.785398163F, 3.14159265F, 0.0F));
        PartDefinition p33 = p31.addOrReplaceChild("spine_4", CubeListBuilder.create()
                .texOffs(78, 0).mirror(false)
                .addBox(-6.0F, -18.0F, -6.0F, 12.0F, 54.0F, 12.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 42.0F, -0.785398163F, 0.0F, 0.0F));
        PartDefinition p34 = p27.addOrReplaceChild("middle_spines", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 42.0F, 42.0F, 0.0F, 0.0F, 1.57079633F));
        PartDefinition p35 = p34.addOrReplaceChild("spine_5", CubeListBuilder.create()
                .texOffs(78, 0).mirror(false)
                .addBox(-6.0F, -18.0F, -6.0F, 12.0F, 54.0F, 12.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(0.0F, 42.0F, -42.0F, 2.35619449F, 0.0F, 0.0F));
        PartDefinition p36 = p34.addOrReplaceChild("spine_6", CubeListBuilder.create()
                .texOffs(78, 0).mirror(true)
                .addBox(-6.0F, -18.0F, -6.0F, 12.0F, 54.0F, 12.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(0.0F, -42.0F, -42.0F, -0.785398163F, 3.14159265F, 0.0F));
        PartDefinition p37 = p34.addOrReplaceChild("spines7and8", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 3.14159265F, 0.0F, 0.0F));
        PartDefinition p38 = p37.addOrReplaceChild("spine_7", CubeListBuilder.create()
                .texOffs(78, 0).mirror(false)
                .addBox(-6.0F, -18.0F, -6.0F, 12.0F, 54.0F, 12.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(0.0F, 42.0F, -42.0F, 2.35619449F, 0.0F, 0.0F));
        PartDefinition p39 = p37.addOrReplaceChild("spine_8", CubeListBuilder.create()
                .texOffs(78, 0).mirror(true)
                .addBox(-6.0F, -18.0F, -6.0F, 12.0F, 54.0F, 12.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(0.0F, -42.0F, -42.0F, -0.785398163F, 3.14159265F, 0.0F));
        PartDefinition p40 = p27.addOrReplaceChild("bottom_spines", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 42.0F, 42.0F, 3.14159265F, 0.0F, 0.0F));
        PartDefinition p41 = p40.addOrReplaceChild("spine_9", CubeListBuilder.create()
                .texOffs(78, 0).mirror(true)
                .addBox(-6.0F, -18.0F, -6.0F, 12.0F, 54.0F, 12.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(0.0F, -42.0F, -42.0F, -0.785398163F, 3.14159265F, 0.0F));
        PartDefinition p42 = p40.addOrReplaceChild("spine_10", CubeListBuilder.create()
                .texOffs(78, 0).mirror(false)
                .addBox(-6.0F, -18.0F, -6.0F, 12.0F, 54.0F, 12.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(0.0F, -42.0F, 42.0F, -0.785398163F, 0.0F, 0.0F));
        PartDefinition p43 = p40.addOrReplaceChild("spine11and12", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -42.0F, 0.0F, 0.0F, -1.57079633F, 0.0F));
        PartDefinition p44 = p43.addOrReplaceChild("spine_11", CubeListBuilder.create()
                .texOffs(78, 0).mirror(true)
                .addBox(-6.0F, -18.0F, -6.0F, 12.0F, 54.0F, 12.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -42.0F, -0.785398163F, 3.14159265F, 0.0F));
        PartDefinition p45 = p43.addOrReplaceChild("spine_12", CubeListBuilder.create()
                .texOffs(78, 0).mirror(false)
                .addBox(-6.0F, -18.0F, -6.0F, 12.0F, 54.0F, 12.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 42.0F, -0.785398163F, 0.0F, 0.0F));
        PartDefinition p46 = p21.addOrReplaceChild("tail", CubeListBuilder.create()
                .texOffs(294, 0).mirror(false)
                .addBox(-36.0F, -12.0F, -12.0F, 42.0F, 24.0F, 24.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(5.0F, 0.0F, 48.0F, 0.0F, 1.57079633F, 0.0F));
        PartDefinition p47 = p46.addOrReplaceChild("tail4", CubeListBuilder.create()
                .texOffs(366, 48).mirror(false)
                .addBox(-32.0F, -9.0F, -9.0F, 42.0F, 18.0F, 18.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(110, 100).mirror(false)
                .addBox(-18.0F, -25.0F, 0.0F, 16.0F, 16.0F, 0.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(-36.0F, 3.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p48 = p47.addOrReplaceChild("tail5", CubeListBuilder.create()
                .texOffs(402, 0).mirror(false)
                .addBox(-36.0F, -6.0F, -6.0F, 36.0F, 12.0F, 12.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(402, 122).mirror(false)
                .addBox(-73.0F, -27.0F, 0.0F, 55.0F, 54.0F, 0.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(-26.0F, 3.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p49 = p1.addOrReplaceChild("eye", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 23.5F, 8.25F, 0.0F, 0.0F, 0.0F));
        PartDefinition p50 = p2.addOrReplaceChild("tail1", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 24.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p51 = p3.addOrReplaceChild("tail2", CubeListBuilder.create(), PartPose.offsetAndRotation(1.5F, 23.5F, -14.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p52 = p4.addOrReplaceChild("tail3", CubeListBuilder.create(), PartPose.offsetAndRotation(1.0F, 23.0F, -20.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p53 = p5.addOrReplaceChild("spine1", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 12.5F, -7.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p54 = p53.addOrReplaceChild("spine1_rotation", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -15.0F, 7.0F, -0.785398163F, 0.0F, 0.0F));
        PartDefinition p55 = p6.addOrReplaceChild("spine2", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 12.5F, 7.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p56 = p55.addOrReplaceChild("spine2_rotation", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -15.0F, -7.0F, 0.785398163F, 0.0F, 0.0F));
        PartDefinition p57 = p7.addOrReplaceChild("spine3", CubeListBuilder.create(), PartPose.offsetAndRotation(-7.0F, 12.5F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p58 = p57.addOrReplaceChild("spine3_rotation", CubeListBuilder.create(), PartPose.offsetAndRotation(7.0F, -15.0F, 0.0F, 0.0F, 0.0F, 0.785398163F));
        PartDefinition p59 = p8.addOrReplaceChild("spine4", CubeListBuilder.create(), PartPose.offsetAndRotation(7.0F, 12.5F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p60 = p59.addOrReplaceChild("spine4_rotation", CubeListBuilder.create(), PartPose.offsetAndRotation(-7.0F, -15.0F, 0.0F, 0.0F, 0.0F, -0.785398163F));
        PartDefinition p61 = p9.addOrReplaceChild("spine5", CubeListBuilder.create(), PartPose.offsetAndRotation(7.0F, 5.5F, 7.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p62 = p61.addOrReplaceChild("spine5_rotation", CubeListBuilder.create(), PartPose.offsetAndRotation(-7.0F, -8.0F, -7.0F, 1.57079633F, 0.785398163F, 0.0F));
        PartDefinition p63 = p10.addOrReplaceChild("spine6", CubeListBuilder.create(), PartPose.offsetAndRotation(-7.0F, 5.5F, 7.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p64 = p63.addOrReplaceChild("spine6_rotation", CubeListBuilder.create(), PartPose.offsetAndRotation(7.0F, -8.0F, -7.0F, 1.57079633F, -0.785398163F, 0.0F));
        PartDefinition p65 = p11.addOrReplaceChild("spine7", CubeListBuilder.create(), PartPose.offsetAndRotation(-7.0F, 5.5F, -7.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p66 = p65.addOrReplaceChild("spine7_rotation", CubeListBuilder.create(), PartPose.offsetAndRotation(7.0F, -8.0F, 7.0F, -1.57079633F, 0.785398163F, 0.0F));
        PartDefinition p67 = p12.addOrReplaceChild("spine8", CubeListBuilder.create(), PartPose.offsetAndRotation(7.0F, 5.5F, -7.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p68 = p67.addOrReplaceChild("spine8_rotation", CubeListBuilder.create(), PartPose.offsetAndRotation(-7.0F, -8.0F, 7.0F, -1.57079633F, -0.785398163F, 0.0F));
        PartDefinition p69 = p13.addOrReplaceChild("spine9", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -1.5F, -7.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p70 = p69.addOrReplaceChild("spine9_rotation", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -1.0F, 7.0F, -2.35619449F, 0.0F, 0.0F));
        PartDefinition p71 = p14.addOrReplaceChild("spine10", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -1.5F, 7.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p72 = p71.addOrReplaceChild("spine10_rotation", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -1.0F, -7.0F, 2.35619449F, 0.0F, 0.0F));
        PartDefinition p73 = p15.addOrReplaceChild("spine11", CubeListBuilder.create(), PartPose.offsetAndRotation(-7.0F, -1.5F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p74 = p73.addOrReplaceChild("spine11_rotation", CubeListBuilder.create(), PartPose.offsetAndRotation(7.0F, -1.0F, 0.0F, 0.0F, 0.0F, 2.35619449F));
        PartDefinition p75 = p16.addOrReplaceChild("spine12", CubeListBuilder.create(), PartPose.offsetAndRotation(7.0F, -1.5F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p76 = p75.addOrReplaceChild("spine12_rotation", CubeListBuilder.create(), PartPose.offsetAndRotation(-7.0F, -1.0F, 0.0F, 0.0F, 0.0F, -2.35619449F));
        return LayerDefinition.create(mesh, 512, 512);
    }

    /** Preserve source expression units; static scales are reapplied after every pose reset. */
    public static ReferenceMobDefinition.Builder configure(ReferenceMobDefinition.Builder builder) {
        builder.cemDriver("guardian");
        builder.hostAlias("body", "__cem_host_body");
        builder.hostAlias("eye", "__cem_host_eye");
        builder.hostAlias("tail1", "__cem_host_tail1");
        builder.hostAlias("tail2", "__cem_host_tail2");
        builder.hostAlias("tail3", "__cem_host_tail3");
        builder.hostAlias("spine1", "__cem_host_spine1");
        builder.hostAlias("spine2", "__cem_host_spine2");
        builder.hostAlias("spine3", "__cem_host_spine3");
        builder.hostAlias("spine4", "__cem_host_spine4");
        builder.hostAlias("spine5", "__cem_host_spine5");
        builder.hostAlias("spine6", "__cem_host_spine6");
        builder.hostAlias("spine7", "__cem_host_spine7");
        builder.hostAlias("spine8", "__cem_host_spine8");
        builder.hostAlias("spine9", "__cem_host_spine9");
        builder.hostAlias("spine10", "__cem_host_spine10");
        builder.hostAlias("spine11", "__cem_host_spine11");
        builder.hostAlias("spine12", "__cem_host_spine12");
        builder.boneScale("Guardian", 0.25F);
        builder.expression("Guardian.ty", "3");
        builder.expression("eye2.tx", "eye.tx*8+5");
        builder.expression("eye2.ty", "eye.ty*4+2");
        builder.expression("tail.ry", "tail1.ry/2+torad(90)");
        builder.expression("tail4.ry", "tail2.ry/2");
        builder.expression("tail5.ry", "tail3.ry/2");
        builder.expression("spine_1.ty", "spine2.ty*4-88");
        builder.expression("spine_1.tz", "spine2.tz*4-24");
        builder.expression("spine_2.ty", "spine1.ty*4-88");
        builder.expression("spine_2.tz", "spine1.tz*4+24");
        builder.expression("spine_3.ty", "spine2.ty*4-46");
        builder.expression("spine_3.tz", "spine2.tz*4-24");
        builder.expression("spine_4.ty", "spine1.ty*4-46");
        builder.expression("spine_4.tz", "spine1.tz*4+24");
        builder.expression("spine_5.ty", "-spine1.ty*4+88");
        builder.expression("spine_5.tz", "-spine1.tz*4-24");
        builder.expression("spine_6.ty", "spine2.ty*4-88");
        builder.expression("spine_6.tz", "spine2.tz*4-24");
        builder.expression("spine_7.ty", "-spine1.ty*4+88");
        builder.expression("spine_7.tz", "-spine1.tz*4-24");
        builder.expression("spine_8.ty", "spine2.ty*4-88");
        builder.expression("spine_8.tz", "spine2.tz*4-24");
        builder.expression("spine_9.ty", "spine2.ty*4-88");
        builder.expression("spine_9.tz", "spine2.tz*4-24");
        builder.expression("spine_10.ty", "spine1.ty*4-88");
        builder.expression("spine_10.tz", "spine1.tz*4+24");
        builder.expression("spine_11.ty", "spine2.ty*4-46");
        builder.expression("spine_11.tz", "spine2.tz*4-24");
        builder.expression("spine_12.ty", "spine1.ty*4-46");
        builder.expression("spine_12.tz", "spine1.tz*4+24");
        builder.expression("spine1_rotation.rx", "0");
        builder.expression("spine2_rotation.rx", "0");
        builder.expression("spine3_rotation.rz", "0");
        builder.expression("spine4_rotation.rz", "0");
        builder.expression("spine5_rotation.rx", "0");
        builder.expression("spine5_rotation.ry", "0");
        builder.expression("spine6_rotation.rx", "0");
        builder.expression("spine6_rotation.ry", "0");
        builder.expression("spine7_rotation.rx", "0");
        builder.expression("spine7_rotation.ry", "0");
        builder.expression("spine8_rotation.rx", "0");
        builder.expression("spine8_rotation.ry", "0");
        builder.expression("spine9_rotation.rx", "0");
        builder.expression("spine10_rotation.rx", "0");
        builder.expression("spine11_rotation.rz", "0");
        builder.expression("spine12_rotation.rz", "0");
        return builder;
    }
}
