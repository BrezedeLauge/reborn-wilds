/*
 * Generated adaptation of ReCrafted Mobs 11.0 by GillsBills and credited contributors.
 * Asset license: CC-BY-NC-SA-4.0; https://creativecommons.org/licenses/by-nc-sa/4.0/
 * Source: https://modrinth.com/resourcepack/recrafted-mobs
 * Model: assets/minecraft/optifine/cem/warden/warden4.jem
 * SHA256: 1f272b07b5c2477415eb966526bcfa45ee4583ef654582050a98012b6d957b15
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

public final class CemJungleAbominationGeometry {
    private CemJungleAbominationGeometry() {}

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
        PartDefinition p16 = p15.addOrReplaceChild("groot", CubeListBuilder.create(), PartPose.offsetAndRotation(-2.0F, 21.0F, 1.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p17 = p16.addOrReplaceChild("spine", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -43.0F, 2.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p18 = p17.addOrReplaceChild("body2", CubeListBuilder.create()
                .texOffs(0, 0).mirror(false)
                .addBox(-26.0F, -46.0F, -10.0F, 53.0F, 48.0F, 21.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(132, 132).mirror(false)
                .addBox(-25.0F, -45.0F, -9.0F, 51.0F, 46.0F, 19.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(250, 2).mirror(false)
                .addBox(-13.5F, -19.0F, -2.0F, 32.0F, 16.0F, 16.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(0, 307).mirror(false)
                .addBox(-13.5F, -35.0F, 1.0F, 16.0F, 16.0F, 16.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(290, 215).mirror(false)
                .addBox(-29.5F, -51.0F, 6.0F, 16.0F, 32.0F, 16.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(271, 99).mirror(false)
                .addBox(-13.5F, -67.0F, 6.0F, 32.0F, 32.0F, 16.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(0.0F, 15.0F, -2.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p19 = p18.addOrReplaceChild("neck", CubeListBuilder.create()
                .texOffs(250, 0).mirror(false)
                .addBox(-33.75F, -27.0F, -10.0F, 66.0F, 2.0F, 0.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(250, 0).mirror(false)
                .addBox(-33.75F, -27.0F, 11.0F, 66.0F, 2.0F, 0.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(0, 69).mirror(false)
                .addBox(-33.75F, -25.0F, -10.0F, 66.0F, 18.0F, 21.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(-0.25F, -39.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p20 = p19.addOrReplaceChild("head3", CubeListBuilder.create()
                .texOffs(0, 185).mirror(false)
                .addBox(-17.0F, -58.0F, -34.0F, 34.0F, 34.0F, 34.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(148, 0).mirror(false)
                .addBox(-17.0F, -24.0F, -34.0F, 34.0F, 31.0F, 34.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(204, 197).mirror(false)
                .addBox(-17.0F, -8.1F, -34.0F, 34.0F, 0.0F, 34.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(102, 199).mirror(false)
                .addBox(-17.0F, -8.1F, -12.0F, 34.0F, 16.0F, 0.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(-1.75F, -7.0F, -1.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p21 = p20.addOrReplaceChild("leftEye", CubeListBuilder.create()
                .texOffs(0, 2).mirror(false)
                .addBox(-4.0F, -2.0F, 0.0F, 8.0F, 2.0F, 0.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(9.0F, -13.0F, -34.1F, 0.0F, 0.0F, 0.0F));
        PartDefinition p22 = p20.addOrReplaceChild("rightEye", CubeListBuilder.create()
                .texOffs(0, 0).mirror(false)
                .addBox(-5.0F, -2.0F, 0.0F, 8.0F, 2.0F, 0.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(-8.0F, -13.0F, -34.1F, 0.0F, 0.0F, 0.0F));
        PartDefinition p23 = p20.addOrReplaceChild("jaw", CubeListBuilder.create()
                .texOffs(136, 197).mirror(false)
                .addBox(-17.0F, -1.0F, -34.0F, 34.0F, 10.0F, 34.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(194, 65).mirror(false)
                .addBox(-17.0F, 6.9F, -34.0F, 34.0F, 0.0F, 34.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p24 = p18.addOrReplaceChild("groot_right_arm", CubeListBuilder.create()
                .texOffs(127, 16).mirror(false)
                .addBox(-22.0F, -12.0F, -13.0F, 22.0F, 2.0F, 0.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(102, 193).mirror(false)
                .addBox(-22.0F, -12.0F, -13.0F, 0.0F, 2.0F, 24.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(127, 16).mirror(false)
                .addBox(-22.0F, -12.0F, 11.0F, 22.0F, 2.0F, 0.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(102, 191).mirror(false)
                .addBox(0.0F, -12.0F, -13.0F, 0.0F, 2.0F, 24.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(272, 147).mirror(false)
                .addBox(-22.0F, -10.0F, -13.0F, 22.0F, 20.0F, 24.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(-35.0F, -50.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p25 = p24.addOrReplaceChild("elbow", CubeListBuilder.create()
                .texOffs(180, 294).mirror(false)
                .addBox(-8.0F, 0.0F, -8.0F, 16.0F, 44.0F, 16.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(-11.0F, 10.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p26 = p25.addOrReplaceChild("wrist", CubeListBuilder.create()
                .texOffs(296, 34).mirror(false)
                .addBox(-6.0F, 0.0F, -6.0F, 12.0F, 39.0F, 12.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(0.0F, 44.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p27 = p26.addOrReplaceChild("hand", CubeListBuilder.create()
                .texOffs(64, 307).mirror(false)
                .addBox(-4.0F, 0.0F, -4.0F, 8.0F, 31.0F, 8.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(0.0F, 39.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p28 = p18.addOrReplaceChild("groot_left_arm", CubeListBuilder.create()
                .texOffs(286, 263).mirror(false)
                .addBox(0.0F, -16.0F, -17.0F, 33.0F, 2.0F, 0.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(102, 165).mirror(false)
                .addBox(0.0F, -16.0F, -17.0F, 0.0F, 2.0F, 32.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(286, 263).mirror(false)
                .addBox(0.0F, -16.0F, 16.0F, 33.0F, 2.0F, 0.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(102, 165).mirror(false)
                .addBox(33.0F, -16.0F, -17.0F, 0.0F, 2.0F, 32.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(0, 108).mirror(false)
                .addBox(0.0F, -14.0F, -17.0F, 33.0F, 44.0F, 33.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(32.0F, -52.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p29 = p28.addOrReplaceChild("elbow2", CubeListBuilder.create()
                .texOffs(131, 65).mirror(false)
                .addBox(-15.0F, 0.0F, -26.0F, 27.0F, 24.0F, 43.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(18.0F, 30.0F, -3.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p30 = p29.addOrReplaceChild("statue", CubeListBuilder.create()
                .texOffs(0, 253).mirror(false)
                .addBox(-12.0F, -17.0F, -24.0F, 24.0F, 30.0F, 24.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(72, 261).mirror(false)
                .addBox(-9.0F, -2.0F, -26.0F, 18.0F, 3.0F, 2.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(0, 185).mirror(false)
                .addBox(-3.0F, 4.0F, -28.0F, 6.0F, 12.0F, 8.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(244, 294).mirror(false)
                .addBox(-10.0F, 13.0F, -18.0F, 20.0F, 24.0F, 12.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(186, 241).mirror(false)
                .addBox(3.0F, 29.0F, -32.0F, 8.0F, 8.0F, 17.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(-1.0F, 12.0F, -26.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p31 = p30.addOrReplaceChild("arms", CubeListBuilder.create()
                .texOffs(99, 108).mirror(false)
                .addBox(-14.0F, -10.0F, -4.0F, 8.0F, 16.0F, 8.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(306, 199).mirror(false)
                .addBox(-6.0F, -2.0F, -4.0F, 12.0F, 8.0F, 8.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(0, 108).mirror(false)
                .addBox(6.0F, -10.0F, -4.0F, 8.0F, 16.0F, 8.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(0.0F, 21.0F, -19.0F, -1.04719755F, 0.0F, 0.0F));
        PartDefinition p32 = p18.addOrReplaceChild("flower", CubeListBuilder.create()
                .texOffs(96, 294).mirror(false)
                .addBox(-21.0F, -21.5F, -0.5F, 42.0F, 42.0F, 0.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(219, 235).mirror(false)
                .addBox(-3.0F, -9.0F, -7.0F, 0.0F, 17.0F, 6.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(186, 235).mirror(false)
                .addBox(1.0F, -13.0F, -7.0F, 0.0F, 17.0F, 6.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(153, 59).mirror(false)
                .addBox(5.0F, -9.0F, -7.0F, 0.0F, 17.0F, 6.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(0, 63).mirror(false)
                .addBox(1.0F, -5.0F, -7.0F, 0.0F, 17.0F, 6.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(-1.0F, -16.0F, -10.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p33 = p32.addOrReplaceChild("petal1", CubeListBuilder.create()
                .texOffs(244, 330).mirror(false)
                .addBox(-7.0F, -19.0F, -1.4F, 14.0F, 19.0F, 0.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(-10.0F, 0.0F, 0.0F, 2.0943951F, 0.0F, -1.57079633F));
        PartDefinition p34 = p32.addOrReplaceChild("petal2", CubeListBuilder.create()
                .texOffs(272, 330).mirror(false)
                .addBox(-7.0F, -19.0F, -1.4F, 14.0F, 19.0F, 0.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(13.0F, 0.0F, 0.0F, 2.0943951F, 0.0F, 1.57079633F));
        PartDefinition p35 = p32.addOrReplaceChild("petal3", CubeListBuilder.create()
                .texOffs(272, 330).mirror(true)
                .addBox(-7.0F, -20.0F, -0.4F, 14.0F, 19.0F, 0.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(1.0F, -11.0F, -1.0F, 2.0943951F, 0.0F, 0.0F));
        PartDefinition p36 = p32.addOrReplaceChild("petal4", CubeListBuilder.create()
                .texOffs(311, 265).mirror(false)
                .addBox(-7.0F, 0.0F, -0.5F, 14.0F, 19.0F, 0.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(1.0F, 10.0F, -1.0F, -2.0943951F, 0.0F, 0.0F));
        PartDefinition p37 = p17.addOrReplaceChild("pelvis", CubeListBuilder.create(), PartPose.offsetAndRotation(1.0F, 15.0F, -2.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p38 = p37.addOrReplaceChild("groot_right_leg", CubeListBuilder.create()
                .texOffs(211, 241).mirror(false)
                .addBox(-11.5F, 0.0F, -12.5F, 25.0F, 28.0F, 25.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(-27.5F, 0.0F, 0.5F, 0.0F, 0.0F, 0.0F));
        PartDefinition p39 = p38.addOrReplaceChild("right_toes", CubeListBuilder.create(), PartPose.offsetAndRotation(27.5F, 0.0F, -0.5F, 0.0F, 0.0F, 0.0F));
        PartDefinition p40 = p39.addOrReplaceChild("right_leg_right_toe", CubeListBuilder.create()
                .texOffs(300, 329).mirror(true)
                .addBox(-9.0F, 0.0F, -8.0F, 25.0F, 0.0F, 8.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(-39.0F, 25.0F, 4.0F, 0.479965544F, 1.57079633F, 0.0F));
        PartDefinition p41 = p39.addOrReplaceChild("right_leg_left_toe", CubeListBuilder.create()
                .texOffs(300, 337).mirror(false)
                .addBox(-16.0F, 0.0F, -8.0F, 25.0F, 0.0F, 8.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(-14.0F, 25.0F, 4.0F, 0.479965544F, -1.57079633F, 0.0F));
        PartDefinition p42 = p39.addOrReplaceChild("right_leg_front_toe", CubeListBuilder.create()
                .texOffs(300, 321).mirror(false)
                .addBox(-12.0F, 1.0F, -7.0F, 25.0F, 0.0F, 8.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(-27.0F, 24.0F, -13.0F, 0.479965544F, 0.0F, 0.0F));
        PartDefinition p43 = p39.addOrReplaceChild("right_leg_back_toe", CubeListBuilder.create()
                .texOffs(300, 345).mirror(false)
                .addBox(-12.0F, 0.0F, 0.0F, 25.0F, 0.0F, 8.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(-27.0F, 25.0F, 13.0F, -0.479965544F, 0.0F, 0.0F));
        PartDefinition p44 = p37.addOrReplaceChild("groot_left_leg", CubeListBuilder.create()
                .texOffs(111, 241).mirror(false)
                .addBox(-10.0F, 0.0F, -12.0F, 25.0F, 28.0F, 25.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(26.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p45 = p44.addOrReplaceChild("left_toes", CubeListBuilder.create(), PartPose.offsetAndRotation(-24.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p46 = p45.addOrReplaceChild("left_leg_right_toe", CubeListBuilder.create()
                .texOffs(303, 297).mirror(false)
                .addBox(-16.0F, 0.0F, -8.0F, 25.0F, 0.0F, 8.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(39.0F, 25.0F, 4.0F, 0.479965544F, -1.57079633F, 0.0F));
        PartDefinition p47 = p45.addOrReplaceChild("left_leg_left_toe", CubeListBuilder.create()
                .texOffs(303, 305).mirror(true)
                .addBox(-9.0F, 0.0F, -8.0F, 25.0F, 0.0F, 8.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(14.0F, 25.0F, 4.0F, 0.479965544F, 1.57079633F, 0.0F));
        PartDefinition p48 = p45.addOrReplaceChild("left_leg_front_toe", CubeListBuilder.create()
                .texOffs(303, 289).mirror(true)
                .addBox(-13.0F, 1.0F, -7.0F, 25.0F, 0.0F, 8.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(27.0F, 24.0F, -13.0F, 0.479965544F, 0.0F, 0.0F));
        PartDefinition p49 = p45.addOrReplaceChild("left_leg_back_toe", CubeListBuilder.create()
                .texOffs(303, 313).mirror(true)
                .addBox(-13.0F, 0.0F, 0.0F, 25.0F, 0.0F, 8.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(27.0F, 25.0F, 13.0F, -0.479965544F, 0.0F, 0.0F));
        PartDefinition p50 = p6.addOrReplaceChild("left_ribcage", CubeListBuilder.create(), PartPose.offsetAndRotation(-7.0F, 23.0F, 5.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p51 = p5.addOrReplaceChild("right_ribcage", CubeListBuilder.create(), PartPose.offsetAndRotation(7.0F, 23.0F, 5.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p52 = p8.addOrReplaceChild("left_arm", CubeListBuilder.create(), PartPose.offsetAndRotation(-13.0F, 34.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p53 = p7.addOrReplaceChild("right_arm", CubeListBuilder.create(), PartPose.offsetAndRotation(13.0F, 34.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p54 = p10.addOrReplaceChild("left_leg", CubeListBuilder.create(), PartPose.offsetAndRotation(-6.0F, 13.0F, 1.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p55 = p9.addOrReplaceChild("right_leg", CubeListBuilder.create(), PartPose.offsetAndRotation(6.0F, 13.0F, 1.0F, 0.0F, 0.0F, 0.0F));
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
        builder.boneScale("groot", 0.85F);
        builder.expression("head3.rx", "head.rx/2");
        builder.expression("groot_right_leg.rx", "right_leg.rx");
        builder.expression("groot_right_leg.ry", "right_leg.ry");
        builder.expression("groot_right_leg.rz", "right_leg.rz");
        builder.expression("groot_left_leg.rx", "left_leg.rx");
        builder.expression("groot_left_leg.ry", "left_leg.ry");
        builder.expression("groot_left_leg.rz", "left_leg.rz");
        builder.expression("groot_right_arm.rx", "(torad(12.5))");
        builder.expression("elbow.rx", "(torad(17.5))");
        builder.expression("wrist.rx", "(torad(27.5))");
        builder.expression("hand.rx", "(torad(22.5))");
        builder.expression("groot_right_arm.ry", "right_arm.ry");
        builder.expression("groot_right_arm.rz", "right_arm.rz");
        builder.expression("groot_left_arm.rx", "left_arm.rx");
        builder.expression("groot_left_arm.ry", "left_arm.ry");
        builder.expression("groot_left_arm.rz", "left_arm.rz");
        builder.expression("body2.rx", "torso.rx");
        builder.expression("body2.ry", "torso.ry");
        builder.expression("body2.rz", "torso.rz");
        builder.expression("petal1.rx", "torad(120) + sin(torad(13.5*3) + time/45) *0.15");
        builder.expression("petal2.rx", "torad(120) + sin(torad(12.5*3) + time/42) *0.10");
        builder.expression("petal3.rx", "torad(120) + sin(torad(13.5*3) + time/45) *0.15");
        builder.expression("petal4.rx", "torad(-120) + sin(torad(12.5*3) + time/42) *0.10");
        builder.expression("render.shadow_size", "3");
        builder.expression("monsters.rx", "-torso.rx");
        builder.expression("monsters.ry", "-torso.ry");
        builder.expression("monsters.rz", "-torso.rz");
        return builder;
    }
}
