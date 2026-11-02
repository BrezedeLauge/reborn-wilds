/*
 * Generated adaptation of ReCrafted Mobs 11.0 by GillsBills and credited contributors.
 * Authored asset license: CC-BY-NC-SA-4.0; https://creativecommons.org/licenses/by-nc-sa/4.0/
 * Source: https://modrinth.com/resourcepack/recrafted-mobs
 * Model: assets/minecraft/optifine/cem/allay/allay3.jem
 * Source SHA256: 1177f90b42b738a53cade803245be5e0f5eb889d3578c14c71f320b22a299b9b
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

public final class CemSummonedGuardianVexGeometry {
    private CemSummonedGuardianVexGeometry() {}

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        PartDefinition p0 = root.addOrReplaceChild("__cem_host_root", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p1 = p0.addOrReplaceChild("__cem_host_head", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p2 = p0.addOrReplaceChild("__cem_host_body", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p3 = p2.addOrReplaceChild("__cem_host_left_arm", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p4 = p2.addOrReplaceChild("__cem_host_right_arm", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p5 = p2.addOrReplaceChild("__cem_host_left_wing", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p6 = p2.addOrReplaceChild("__cem_host_right_wing", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p7 = p1.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 6.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p8 = p7.addOrReplaceChild("others", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -6.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p9 = p8.addOrReplaceChild("Guardian", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -5.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p10 = p9.addOrReplaceChild("head2", CubeListBuilder.create()
                .texOffs(0, 48).mirror(false)
                .addBox(-4.5F, -8.0F, -4.0F, 9.0F, 7.0F, 7.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(32, 50).mirror(false)
                .addBox(2.5F, -12.0F, -1.0F, 4.0F, 6.0F, 0.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p11 = p10.addOrReplaceChild("body_sub_59", CubeListBuilder.create()
                .texOffs(32, 50).mirror(true)
                .addBox(-6.5F, -12.0F, -1.0F, 4.0F, 6.0F, 0.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.ZERO);
        PartDefinition p12 = p9.addOrReplaceChild("body3", CubeListBuilder.create()
                .texOffs(17, 63).mirror(false)
                .addBox(-3.0F, 0.0F, -2.0F, 6.0F, 7.0F, 4.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.ZERO);
        PartDefinition p13 = p12.addOrReplaceChild("right_wing2", CubeListBuilder.create()
                .texOffs(0, 95).mirror(false)
                .addBox(-23.0F, 1.0F, 0.0F, 23.0F, 12.0F, 0.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 2.0F, 0.0F, 0.218166156F, 0.0436332313F));
        PartDefinition p14 = p12.addOrReplaceChild("left_wing2", CubeListBuilder.create()
                .texOffs(0, 95).mirror(true)
                .addBox(0.0F, 0.0F, 0.0F, 23.0F, 12.0F, 0.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(0.0F, 1.0F, 2.0F, 0.0F, -0.218166156F, -0.0436332313F));
        PartDefinition p15 = p12.addOrReplaceChild("right_wing3", CubeListBuilder.create()
                .texOffs(0, 95).mirror(false)
                .addBox(-23.0F, 1.0F, 0.0F, 23.0F, 12.0F, 0.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(1.0F, -1.0F, 2.0F, 0.0F, 0.523598776F, 0.392699082F));
        PartDefinition p16 = p12.addOrReplaceChild("left_wing3", CubeListBuilder.create()
                .texOffs(0, 95).mirror(true)
                .addBox(0.0F, 0.0F, 0.0F, 23.0F, 12.0F, 0.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(-1.0F, 0.0F, 2.0F, 0.0F, -0.523598776F, -0.392699082F));
        PartDefinition p17 = p12.addOrReplaceChild("right_arm2", CubeListBuilder.create()
                .texOffs(28, 77).mirror(false)
                .addBox(-2.0F, -1.0F, -1.0F, 3.0F, 8.0F, 3.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(-4.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p18 = p12.addOrReplaceChild("left_arm2", CubeListBuilder.create()
                .texOffs(57, 74).mirror(false)
                .addBox(-1.0F, -1.0F, -1.0F, 3.0F, 8.0F, 3.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(4.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p19 = p12.addOrReplaceChild("right_leg", CubeListBuilder.create(), PartPose.offsetAndRotation(-1.5F, 7.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p20 = p19.addOrReplaceChild("left_leg2", CubeListBuilder.create()
                .texOffs(14, 77).mirror(false)
                .addBox(-3.0F, 0.0F, -2.0F, 4.0F, 7.0F, 3.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(-0.5F, 0.0F, 1.0F, 0.0F, -1.57079633F, 0.0F));
        PartDefinition p21 = p12.addOrReplaceChild("left_leg", CubeListBuilder.create(), PartPose.offsetAndRotation(1.5F, 7.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p22 = p21.addOrReplaceChild("right_leg2", CubeListBuilder.create()
                .texOffs(43, 74).mirror(false)
                .addBox(-3.0F, 0.0F, -2.0F, 4.0F, 7.0F, 3.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(-0.5F, 0.0F, 1.0F, 0.0F, -1.57079633F, 0.0F));
        PartDefinition p23 = p2.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 6.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p24 = p4.addOrReplaceChild("right_arm", CubeListBuilder.create(), PartPose.offsetAndRotation(1.75F, 5.5F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p25 = p3.addOrReplaceChild("left_arm", CubeListBuilder.create(), PartPose.offsetAndRotation(-1.75F, 5.5F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p26 = p5.addOrReplaceChild("left_wing", CubeListBuilder.create(), PartPose.offsetAndRotation(-0.5F, 6.0F, -1.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p27 = p6.addOrReplaceChild("right_wing", CubeListBuilder.create(), PartPose.offsetAndRotation(0.5F, 6.0F, -1.0F, 0.0F, 0.0F, 0.0F));
        return LayerDefinition.create(mesh, 128, 128);
    }

    /** Preserve source expression units; static scales are reapplied after every pose reset. */
    public static ReferenceMobDefinition.Builder configure(ReferenceMobDefinition.Builder builder) {
        builder.cemDriver("allay");
        builder.bakedRoot(CemSummonedGuardianVexGeometry::bakeRoot);
        builder.hostAlias("root", "__cem_host_root");
        builder.hostAlias("head", "__cem_host_head");
        builder.hostAlias("body", "__cem_host_body");
        builder.hostAlias("left_arm", "__cem_host_left_arm");
        builder.hostAlias("right_arm", "__cem_host_right_arm");
        builder.hostAlias("left_wing", "__cem_host_left_wing");
        builder.hostAlias("right_wing", "__cem_host_right_wing");
        builder.expression("others.rx", "-head.rx");
        builder.expression("others.ry", "-head.ry");
        builder.expression("others.rz", "-head.rz");
        builder.expression("Guardian.ty", "0");
        builder.expression("body3.rx", "body.rx");
        builder.expression("head2.rx", "head.rx");
        builder.expression("head2.ry", "head.ry");
        builder.expression("head2.rz", "head.rz");
        builder.expression("right_wing2.ry", "-right_wing.ry");
        builder.expression("left_wing2.ry", "-left_wing.ry");
        builder.expression("right_wing3.ry", "-right_wing.ry*1.2");
        builder.expression("left_wing3.ry", "-left_wing.ry*1.2");
        builder.expression("right_arm2.rx", "right_arm.rx");
        builder.expression("right_arm2.ry", "right_arm.ry");
        builder.expression("right_arm2.rz", "right_arm.rz");
        builder.expression("left_arm2.rx", "left_arm.rx");
        builder.expression("left_arm2.ry", "left_arm.ry");
        builder.expression("left_arm2.rz", "left_arm.rz");
        return builder;
    }

    /** Complete matching rig; installed cubes stay owned by the Minecraft client. */
    public static net.minecraft.client.model.geom.ModelPart bakeRoot() {
        var nativeRoot = net.minecraft.client.model.AllayModel.createBodyLayer().bakeRoot();
        var nativeCubes = new java.util.HashMap<String, java.util.List<net.minecraft.client.model.geom.ModelPart.Cube>>();
        nativeRoot.visit(new com.mojang.blaze3d.vertex.PoseStack(),
                (pose, path, index, cube) -> nativeCubes.computeIfAbsent(path, key -> new java.util.ArrayList<>()).add(cube));
        var customCubes = new java.util.HashMap<String, java.util.List<net.minecraft.client.model.geom.ModelPart.Cube>>();
        createBodyLayer().bakeRoot().visit(new com.mojang.blaze3d.vertex.PoseStack(),
                (pose, path, index, cube) -> customCubes.computeIfAbsent(path, key -> new java.util.ArrayList<>()).add(cube));
        var p27Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_root/__cem_host_body/__cem_host_right_wing/right_wing", java.util.List.of()));
        var p27 = new net.minecraft.client.model.geom.ModelPart(p27Cubes, java.util.Map.ofEntries());
        p27.setInitialPose(PartPose.offsetAndRotation(0.5F, 6.0F, -1.0F, 0.0F, 0.0F, 0.0F)); p27.resetPose();
        var p26Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_root/__cem_host_body/__cem_host_left_wing/left_wing", java.util.List.of()));
        var p26 = new net.minecraft.client.model.geom.ModelPart(p26Cubes, java.util.Map.ofEntries());
        p26.setInitialPose(PartPose.offsetAndRotation(-0.5F, 6.0F, -1.0F, 0.0F, 0.0F, 0.0F)); p26.resetPose();
        var p25Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_root/__cem_host_body/__cem_host_left_arm/left_arm", java.util.List.of()));
        var p25 = new net.minecraft.client.model.geom.ModelPart(p25Cubes, java.util.Map.ofEntries());
        p25.setInitialPose(PartPose.offsetAndRotation(-1.75F, 5.5F, 0.0F, 0.0F, 0.0F, 0.0F)); p25.resetPose();
        var p24Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_root/__cem_host_body/__cem_host_right_arm/right_arm", java.util.List.of()));
        var p24 = new net.minecraft.client.model.geom.ModelPart(p24Cubes, java.util.Map.ofEntries());
        p24.setInitialPose(PartPose.offsetAndRotation(1.75F, 5.5F, 0.0F, 0.0F, 0.0F, 0.0F)); p24.resetPose();
        var p23Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_root/__cem_host_body/body", java.util.List.of()));
        var p23 = new net.minecraft.client.model.geom.ModelPart(p23Cubes, java.util.Map.ofEntries());
        p23.setInitialPose(PartPose.offsetAndRotation(0.0F, 6.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p23.resetPose();
        var p22Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_root/__cem_host_head/head/others/Guardian/body3/left_leg/right_leg2", java.util.List.of()));
        var p22 = new net.minecraft.client.model.geom.ModelPart(p22Cubes, java.util.Map.ofEntries());
        p22.setInitialPose(PartPose.offsetAndRotation(-0.5F, 0.0F, 1.0F, 0.0F, -1.57079633F, 0.0F)); p22.resetPose();
        var p21Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_root/__cem_host_head/head/others/Guardian/body3/left_leg", java.util.List.of()));
        var p21 = new net.minecraft.client.model.geom.ModelPart(p21Cubes, java.util.Map.ofEntries(java.util.Map.entry("right_leg2", p22)));
        p21.setInitialPose(PartPose.offsetAndRotation(1.5F, 7.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p21.resetPose();
        var p20Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_root/__cem_host_head/head/others/Guardian/body3/right_leg/left_leg2", java.util.List.of()));
        var p20 = new net.minecraft.client.model.geom.ModelPart(p20Cubes, java.util.Map.ofEntries());
        p20.setInitialPose(PartPose.offsetAndRotation(-0.5F, 0.0F, 1.0F, 0.0F, -1.57079633F, 0.0F)); p20.resetPose();
        var p19Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_root/__cem_host_head/head/others/Guardian/body3/right_leg", java.util.List.of()));
        var p19 = new net.minecraft.client.model.geom.ModelPart(p19Cubes, java.util.Map.ofEntries(java.util.Map.entry("left_leg2", p20)));
        p19.setInitialPose(PartPose.offsetAndRotation(-1.5F, 7.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p19.resetPose();
        var p18Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_root/__cem_host_head/head/others/Guardian/body3/left_arm2", java.util.List.of()));
        var p18 = new net.minecraft.client.model.geom.ModelPart(p18Cubes, java.util.Map.ofEntries());
        p18.setInitialPose(PartPose.offsetAndRotation(4.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p18.resetPose();
        var p17Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_root/__cem_host_head/head/others/Guardian/body3/right_arm2", java.util.List.of()));
        var p17 = new net.minecraft.client.model.geom.ModelPart(p17Cubes, java.util.Map.ofEntries());
        p17.setInitialPose(PartPose.offsetAndRotation(-4.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p17.resetPose();
        var p16Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_root/__cem_host_head/head/others/Guardian/body3/left_wing3", java.util.List.of()));
        var p16 = new net.minecraft.client.model.geom.ModelPart(p16Cubes, java.util.Map.ofEntries());
        p16.setInitialPose(PartPose.offsetAndRotation(-1.0F, 0.0F, 2.0F, 0.0F, -0.523598776F, -0.392699082F)); p16.resetPose();
        var p15Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_root/__cem_host_head/head/others/Guardian/body3/right_wing3", java.util.List.of()));
        var p15 = new net.minecraft.client.model.geom.ModelPart(p15Cubes, java.util.Map.ofEntries());
        p15.setInitialPose(PartPose.offsetAndRotation(1.0F, -1.0F, 2.0F, 0.0F, 0.523598776F, 0.392699082F)); p15.resetPose();
        var p14Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_root/__cem_host_head/head/others/Guardian/body3/left_wing2", java.util.List.of()));
        var p14 = new net.minecraft.client.model.geom.ModelPart(p14Cubes, java.util.Map.ofEntries());
        p14.setInitialPose(PartPose.offsetAndRotation(0.0F, 1.0F, 2.0F, 0.0F, -0.218166156F, -0.0436332313F)); p14.resetPose();
        var p13Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_root/__cem_host_head/head/others/Guardian/body3/right_wing2", java.util.List.of()));
        var p13 = new net.minecraft.client.model.geom.ModelPart(p13Cubes, java.util.Map.ofEntries());
        p13.setInitialPose(PartPose.offsetAndRotation(0.0F, 0.0F, 2.0F, 0.0F, 0.218166156F, 0.0436332313F)); p13.resetPose();
        var p12Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_root/__cem_host_head/head/others/Guardian/body3", java.util.List.of()));
        var p12 = new net.minecraft.client.model.geom.ModelPart(p12Cubes, java.util.Map.ofEntries(java.util.Map.entry("right_wing2", p13), java.util.Map.entry("left_wing2", p14), java.util.Map.entry("right_wing3", p15), java.util.Map.entry("left_wing3", p16), java.util.Map.entry("right_arm2", p17), java.util.Map.entry("left_arm2", p18), java.util.Map.entry("right_leg", p19), java.util.Map.entry("left_leg", p21)));
        p12.setInitialPose(PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p12.resetPose();
        var p11Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_root/__cem_host_head/head/others/Guardian/head2/body_sub_59", java.util.List.of()));
        var p11 = new net.minecraft.client.model.geom.ModelPart(p11Cubes, java.util.Map.ofEntries());
        p11.setInitialPose(PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p11.resetPose();
        var p10Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_root/__cem_host_head/head/others/Guardian/head2", java.util.List.of()));
        var p10 = new net.minecraft.client.model.geom.ModelPart(p10Cubes, java.util.Map.ofEntries(java.util.Map.entry("body_sub_59", p11)));
        p10.setInitialPose(PartPose.offsetAndRotation(0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p10.resetPose();
        var p9Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_root/__cem_host_head/head/others/Guardian", java.util.List.of()));
        var p9 = new net.minecraft.client.model.geom.ModelPart(p9Cubes, java.util.Map.ofEntries(java.util.Map.entry("head2", p10), java.util.Map.entry("body3", p12)));
        p9.setInitialPose(PartPose.offsetAndRotation(0.0F, -5.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p9.resetPose();
        var p8Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_root/__cem_host_head/head/others", java.util.List.of()));
        var p8 = new net.minecraft.client.model.geom.ModelPart(p8Cubes, java.util.Map.ofEntries(java.util.Map.entry("Guardian", p9)));
        p8.setInitialPose(PartPose.offsetAndRotation(0.0F, -6.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p8.resetPose();
        var p7Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_root/__cem_host_head/head", java.util.List.of()));
        var p7 = new net.minecraft.client.model.geom.ModelPart(p7Cubes, java.util.Map.ofEntries(java.util.Map.entry("others", p8)));
        p7.setInitialPose(PartPose.offsetAndRotation(0.0F, 6.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p7.resetPose();
        var p6Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_root/__cem_host_body/__cem_host_right_wing", java.util.List.of()));
        var p6 = new net.minecraft.client.model.geom.ModelPart(p6Cubes, java.util.Map.ofEntries(java.util.Map.entry("right_wing", p27)));
        p6.setInitialPose(nativeRoot.getChild("root").getChild("body").getChild("right_wing").storePose()); p6.resetPose();
        var p5Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_root/__cem_host_body/__cem_host_left_wing", java.util.List.of()));
        var p5 = new net.minecraft.client.model.geom.ModelPart(p5Cubes, java.util.Map.ofEntries(java.util.Map.entry("left_wing", p26)));
        p5.setInitialPose(nativeRoot.getChild("root").getChild("body").getChild("left_wing").storePose()); p5.resetPose();
        var p4Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_root/__cem_host_body/__cem_host_right_arm", java.util.List.of()));
        var p4 = new net.minecraft.client.model.geom.ModelPart(p4Cubes, java.util.Map.ofEntries(java.util.Map.entry("right_arm", p24)));
        p4.setInitialPose(nativeRoot.getChild("root").getChild("body").getChild("right_arm").storePose()); p4.resetPose();
        var p3Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_root/__cem_host_body/__cem_host_left_arm", java.util.List.of()));
        var p3 = new net.minecraft.client.model.geom.ModelPart(p3Cubes, java.util.Map.ofEntries(java.util.Map.entry("left_arm", p25)));
        p3.setInitialPose(nativeRoot.getChild("root").getChild("body").getChild("left_arm").storePose()); p3.resetPose();
        var p2Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_root/__cem_host_body", java.util.List.of()));
        var p2 = new net.minecraft.client.model.geom.ModelPart(p2Cubes, java.util.Map.ofEntries(java.util.Map.entry("__cem_host_left_arm", p3), java.util.Map.entry("__cem_host_right_arm", p4), java.util.Map.entry("__cem_host_left_wing", p5), java.util.Map.entry("__cem_host_right_wing", p6), java.util.Map.entry("body", p23)));
        p2.setInitialPose(nativeRoot.getChild("root").getChild("body").storePose()); p2.resetPose();
        var p1Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_root/__cem_host_head", java.util.List.of()));
        var p1 = new net.minecraft.client.model.geom.ModelPart(p1Cubes, java.util.Map.ofEntries(java.util.Map.entry("head", p7)));
        p1.setInitialPose(nativeRoot.getChild("root").getChild("head").storePose()); p1.resetPose();
        var p0Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_root", java.util.List.of()));
        var p0 = new net.minecraft.client.model.geom.ModelPart(p0Cubes, java.util.Map.ofEntries(java.util.Map.entry("__cem_host_head", p1), java.util.Map.entry("__cem_host_body", p2)));
        p0.setInitialPose(nativeRoot.getChild("root").storePose()); p0.resetPose();
        return new net.minecraft.client.model.geom.ModelPart(java.util.List.of(), java.util.Map.ofEntries(java.util.Map.entry("__cem_host_root", p0)));
    }
}
