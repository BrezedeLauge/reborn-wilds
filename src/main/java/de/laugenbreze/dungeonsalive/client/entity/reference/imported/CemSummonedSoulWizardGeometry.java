/*
 * Generated adaptation of ReCrafted Mobs 11.0 by GillsBills and credited contributors.
 * Authored asset license: CC-BY-NC-SA-4.0; https://creativecommons.org/licenses/by-nc-sa/4.0/
 * Source: https://modrinth.com/resourcepack/recrafted-mobs
 * Model: assets/minecraft/optifine/cem/allay/allay2.jem
 * Source SHA256: 5fd2d0ab1d7627a9a43e501bf4edf689d17eeff820a33f14009273a8b9c220d8
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

public final class CemSummonedSoulWizardGeometry {
    private CemSummonedSoulWizardGeometry() {}

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
        PartDefinition p9 = p8.addOrReplaceChild("soul", CubeListBuilder.create(), PartPose.offsetAndRotation(3.0F, 1.0F, -2.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p10 = p9.addOrReplaceChild("head3", CubeListBuilder.create()
                .texOffs(64, 0).mirror(false)
                .addBox(-5.0F, -10.0F, 0.0F, 5.0F, 6.0F, 5.0F, new CubeDeformation(-0.05F, -0.05F, -0.05F)), PartPose.ZERO);
        PartDefinition p11 = p10.addOrReplaceChild("eyes", CubeListBuilder.create()
                .texOffs(64, 0).mirror(false)
                .addBox(-4.0F, -7.0F, 0.0F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(64, 0).mirror(false)
                .addBox(-2.0F, -7.0F, 0.0F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -0.05F, 0.0F, 0.0F, 0.0F));
        PartDefinition p12 = p9.addOrReplaceChild("hat", CubeListBuilder.create()
                .texOffs(64, 37).mirror(false)
                .addBox(0.0F, -2.0F, -3.0F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(-4.0F, -11.85F, 4.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p13 = p12.addOrReplaceChild("hat2", CubeListBuilder.create()
                .texOffs(84, 0).mirror(false)
                .addBox(-6.0F, 0.0F, 0.0F, 9.0F, 6.0F, 0.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(3.0F, 0.0F, 0.0F, 0.497418837F, 0.0F, 0.0F));
        PartDefinition p14 = p12.addOrReplaceChild("hat3", CubeListBuilder.create()
                .texOffs(64, 21).mirror(false)
                .addBox(-3.0F, 0.0F, 0.0F, 9.0F, 6.0F, 0.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -3.0F, -0.497418837F, 0.0F, 0.0F));
        PartDefinition p15 = p12.addOrReplaceChild("hat4", CubeListBuilder.create()
                .texOffs(84, 15).mirror(false)
                .addBox(-3.0F, 0.0F, 0.0F, 9.0F, 6.0F, 0.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 1.57079633F, 0.479965544F));
        PartDefinition p16 = p12.addOrReplaceChild("hat5", CubeListBuilder.create()
                .texOffs(82, 21).mirror(false)
                .addBox(-3.0F, 0.0F, 0.0F, 9.0F, 6.0F, 0.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(3.0F, 0.0F, -3.0F, 0.0F, -1.57079633F, -0.497418837F));
        PartDefinition p17 = p12.addOrReplaceChild("bone5", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p18 = p9.addOrReplaceChild("body2", CubeListBuilder.create()
                .texOffs(64, 0).mirror(false)
                .addBox(-5.0F, 1.0F, -2.0F, 5.0F, 4.0F, 5.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(0.0F, -5.0F, 2.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p19 = p18.addOrReplaceChild("bodybo", CubeListBuilder.create()
                .texOffs(64, 11).mirror(false)
                .addBox(-2.5F, -2.5F, -2.5F, 5.0F, 5.0F, 5.0F, new CubeDeformation(0.1F, 0.1F, 0.1F)), PartPose.offsetAndRotation(-2.5F, 2.5F, 0.5F, 0.0F, 1.57079633F, 0.0F));
        PartDefinition p20 = p18.addOrReplaceChild("belowwasit", CubeListBuilder.create()
                .texOffs(88, 27).mirror(false)
                .addBox(-4.6F, -0.1F, 1.5F, 4.0F, 5.0F, 3.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(0.1F, 5.1F, -2.5F, 0.0F, 0.0F, 0.0F));
        PartDefinition p21 = p20.addOrReplaceChild("frontsirk", CubeListBuilder.create()
                .texOffs(100, 35).mirror(false)
                .addBox(0.0F, 0.0F, 0.4F, 6.0F, 5.0F, 0.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(-5.6F, 0.0F, 0.0F, -0.0872664626F, 0.0F, 0.0F));
        PartDefinition p22 = p20.addOrReplaceChild("bone7", CubeListBuilder.create()
                .texOffs(88, 35).mirror(false)
                .addBox(0.0F, 0.0F, 0.0F, 6.0F, 5.0F, 0.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(-5.2F, 0.0F, 6.0F, 0.0F, 1.57079633F, 0.0872664626F));
        PartDefinition p23 = p20.addOrReplaceChild("backsirk", CubeListBuilder.create()
                .texOffs(99, 10).mirror(false)
                .addBox(-6.0F, 0.0F, 0.0F, 6.0F, 5.0F, 0.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(0.4F, 0.0F, 5.6F, 0.0872664626F, 0.0F, 0.0F));
        PartDefinition p24 = p20.addOrReplaceChild("bone9", CubeListBuilder.create()
                .texOffs(100, 21).mirror(false)
                .addBox(0.0F, 0.0F, 0.0F, 6.0F, 5.0F, 0.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -1.57079633F, -0.0872664626F));
        PartDefinition p25 = p18.addOrReplaceChild("bow", CubeListBuilder.create(), PartPose.offsetAndRotation(-1.5F, 3.0F, 3.2F, 0.0F, 0.0F, 0.0F));
        PartDefinition p26 = p25.addOrReplaceChild("bone10", CubeListBuilder.create()
                .texOffs(88, 40).mirror(false)
                .addBox(-1.5F, -4.75F, 0.0F, 3.0F, 7.0F, 0.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(-1.0F, 0.5F, 0.05F, -0.0436332313F, 0.0F, -1.57079633F));
        PartDefinition p27 = p25.addOrReplaceChild("bone11", CubeListBuilder.create()
                .texOffs(102, 26).mirror(false)
                .addBox(-1.5F, -4.75F, 0.0F, 3.0F, 7.0F, 0.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(-1.0F, 0.5F, 0.05F, -0.0436332313F, 0.0F, 1.57079633F));
        PartDefinition p28 = p25.addOrReplaceChild("bone14", CubeListBuilder.create()
                .texOffs(64, 10).mirror(false)
                .addBox(0.0F, -5.0F, 0.0F, 0.0F, 5.0F, 1.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(-1.0F, 0.0F, 0.0F, -1.57079633F, 0.0F, 0.0F));
        PartDefinition p29 = p25.addOrReplaceChild("bone15", CubeListBuilder.create()
                .texOffs(76, 37).mirror(false)
                .addBox(-1.5F, -4.5F, 0.0F, 3.0F, 7.0F, 0.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(0.5F, -1.0F, 0.05F, 0.0F, 0.0F, 0.785398163F));
        PartDefinition p30 = p25.addOrReplaceChild("bone16", CubeListBuilder.create()
                .texOffs(82, 37).mirror(false)
                .addBox(-1.5F, -4.5F, 0.0F, 3.0F, 7.0F, 0.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(-2.5F, -1.0F, 0.05F, 0.0F, 0.0F, -0.785398163F));
        PartDefinition p31 = p18.addOrReplaceChild("leftarm", CubeListBuilder.create()
                .texOffs(76, 27).mirror(false)
                .addBox(-0.5F, -1.0F, -2.0F, 2.0F, 6.0F, 4.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(0.5F, 1.0F, 1.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p32 = p31.addOrReplaceChild("bone13", CubeListBuilder.create()
                .texOffs(94, 8).mirror(false)
                .addBox(0.0F, -1.0F, -1.0F, 8.0F, 1.0F, 1.0F, new CubeDeformation(-0.05F, -0.05F, -0.05F)), PartPose.offsetAndRotation(-0.5F, -1.0F, 0.0F, 0.0F, 0.0F, 1.57079633F));
        PartDefinition p33 = p18.addOrReplaceChild("rightarm", CubeListBuilder.create()
                .texOffs(64, 27).mirror(false)
                .addBox(-1.0F, -1.0F, -2.0F, 2.0F, 6.0F, 4.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(-6.0F, 1.0F, 1.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p34 = p33.addOrReplaceChild("bone12", CubeListBuilder.create()
                .texOffs(94, 6).mirror(false)
                .addBox(-8.0F, -1.0F, -1.0F, 8.0F, 1.0F, 1.0F, new CubeDeformation(-0.05F, -0.05F, -0.05F)), PartPose.offsetAndRotation(1.0F, -1.0F, 0.0F, 0.0F, 0.0F, -1.57079633F));
        PartDefinition p35 = p2.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 6.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p36 = p4.addOrReplaceChild("right_arm", CubeListBuilder.create(), PartPose.offsetAndRotation(1.75F, 5.5F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p37 = p3.addOrReplaceChild("left_arm", CubeListBuilder.create(), PartPose.offsetAndRotation(-1.75F, 5.5F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p38 = p5.addOrReplaceChild("left_wing", CubeListBuilder.create(), PartPose.offsetAndRotation(-0.5F, 6.0F, -1.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p39 = p6.addOrReplaceChild("right_wing", CubeListBuilder.create(), PartPose.offsetAndRotation(0.5F, 6.0F, -1.0F, 0.0F, 0.0F, 0.0F));
        return LayerDefinition.create(mesh, 128, 128);
    }

    /** Preserve source expression units; static scales are reapplied after every pose reset. */
    public static ReferenceMobDefinition.Builder configure(ReferenceMobDefinition.Builder builder) {
        builder.cemDriver("allay");
        builder.bakedRoot(CemSummonedSoulWizardGeometry::bakeRoot);
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
        builder.expression("soul.tx", "2.5");
        builder.expression("soul.ty", "3.5");
        builder.expression("body2.rx", "body.rx");
        builder.expression("head3.rx", "head.rx");
        builder.expression("head3.ry", "head.ry");
        builder.expression("head3.rz", "head.rz");
        builder.expression("rightarm.rx", "right_arm.rx");
        builder.expression("rightarm.ry", "right_arm.ry");
        builder.expression("rightarm.rz", "right_arm.rz");
        builder.expression("leftarm.rx", "left_arm.rx");
        builder.expression("leftarm.ry", "left_arm.ry");
        builder.expression("leftarm.rz", "left_arm.rz");
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
        var p39Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_root/__cem_host_body/__cem_host_right_wing/right_wing", java.util.List.of()));
        var p39 = new net.minecraft.client.model.geom.ModelPart(p39Cubes, java.util.Map.ofEntries());
        p39.setInitialPose(PartPose.offsetAndRotation(0.5F, 6.0F, -1.0F, 0.0F, 0.0F, 0.0F)); p39.resetPose();
        var p38Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_root/__cem_host_body/__cem_host_left_wing/left_wing", java.util.List.of()));
        var p38 = new net.minecraft.client.model.geom.ModelPart(p38Cubes, java.util.Map.ofEntries());
        p38.setInitialPose(PartPose.offsetAndRotation(-0.5F, 6.0F, -1.0F, 0.0F, 0.0F, 0.0F)); p38.resetPose();
        var p37Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_root/__cem_host_body/__cem_host_left_arm/left_arm", java.util.List.of()));
        var p37 = new net.minecraft.client.model.geom.ModelPart(p37Cubes, java.util.Map.ofEntries());
        p37.setInitialPose(PartPose.offsetAndRotation(-1.75F, 5.5F, 0.0F, 0.0F, 0.0F, 0.0F)); p37.resetPose();
        var p36Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_root/__cem_host_body/__cem_host_right_arm/right_arm", java.util.List.of()));
        var p36 = new net.minecraft.client.model.geom.ModelPart(p36Cubes, java.util.Map.ofEntries());
        p36.setInitialPose(PartPose.offsetAndRotation(1.75F, 5.5F, 0.0F, 0.0F, 0.0F, 0.0F)); p36.resetPose();
        var p35Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_root/__cem_host_body/body", java.util.List.of()));
        var p35 = new net.minecraft.client.model.geom.ModelPart(p35Cubes, java.util.Map.ofEntries());
        p35.setInitialPose(PartPose.offsetAndRotation(0.0F, 6.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p35.resetPose();
        var p34Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_root/__cem_host_head/head/others/soul/body2/rightarm/bone12", java.util.List.of()));
        var p34 = new net.minecraft.client.model.geom.ModelPart(p34Cubes, java.util.Map.ofEntries());
        p34.setInitialPose(PartPose.offsetAndRotation(1.0F, -1.0F, 0.0F, 0.0F, 0.0F, -1.57079633F)); p34.resetPose();
        var p33Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_root/__cem_host_head/head/others/soul/body2/rightarm", java.util.List.of()));
        var p33 = new net.minecraft.client.model.geom.ModelPart(p33Cubes, java.util.Map.ofEntries(java.util.Map.entry("bone12", p34)));
        p33.setInitialPose(PartPose.offsetAndRotation(-6.0F, 1.0F, 1.0F, 0.0F, 0.0F, 0.0F)); p33.resetPose();
        var p32Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_root/__cem_host_head/head/others/soul/body2/leftarm/bone13", java.util.List.of()));
        var p32 = new net.minecraft.client.model.geom.ModelPart(p32Cubes, java.util.Map.ofEntries());
        p32.setInitialPose(PartPose.offsetAndRotation(-0.5F, -1.0F, 0.0F, 0.0F, 0.0F, 1.57079633F)); p32.resetPose();
        var p31Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_root/__cem_host_head/head/others/soul/body2/leftarm", java.util.List.of()));
        var p31 = new net.minecraft.client.model.geom.ModelPart(p31Cubes, java.util.Map.ofEntries(java.util.Map.entry("bone13", p32)));
        p31.setInitialPose(PartPose.offsetAndRotation(0.5F, 1.0F, 1.0F, 0.0F, 0.0F, 0.0F)); p31.resetPose();
        var p30Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_root/__cem_host_head/head/others/soul/body2/bow/bone16", java.util.List.of()));
        var p30 = new net.minecraft.client.model.geom.ModelPart(p30Cubes, java.util.Map.ofEntries());
        p30.setInitialPose(PartPose.offsetAndRotation(-2.5F, -1.0F, 0.05F, 0.0F, 0.0F, -0.785398163F)); p30.resetPose();
        var p29Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_root/__cem_host_head/head/others/soul/body2/bow/bone15", java.util.List.of()));
        var p29 = new net.minecraft.client.model.geom.ModelPart(p29Cubes, java.util.Map.ofEntries());
        p29.setInitialPose(PartPose.offsetAndRotation(0.5F, -1.0F, 0.05F, 0.0F, 0.0F, 0.785398163F)); p29.resetPose();
        var p28Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_root/__cem_host_head/head/others/soul/body2/bow/bone14", java.util.List.of()));
        var p28 = new net.minecraft.client.model.geom.ModelPart(p28Cubes, java.util.Map.ofEntries());
        p28.setInitialPose(PartPose.offsetAndRotation(-1.0F, 0.0F, 0.0F, -1.57079633F, 0.0F, 0.0F)); p28.resetPose();
        var p27Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_root/__cem_host_head/head/others/soul/body2/bow/bone11", java.util.List.of()));
        var p27 = new net.minecraft.client.model.geom.ModelPart(p27Cubes, java.util.Map.ofEntries());
        p27.setInitialPose(PartPose.offsetAndRotation(-1.0F, 0.5F, 0.05F, -0.0436332313F, 0.0F, 1.57079633F)); p27.resetPose();
        var p26Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_root/__cem_host_head/head/others/soul/body2/bow/bone10", java.util.List.of()));
        var p26 = new net.minecraft.client.model.geom.ModelPart(p26Cubes, java.util.Map.ofEntries());
        p26.setInitialPose(PartPose.offsetAndRotation(-1.0F, 0.5F, 0.05F, -0.0436332313F, 0.0F, -1.57079633F)); p26.resetPose();
        var p25Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_root/__cem_host_head/head/others/soul/body2/bow", java.util.List.of()));
        var p25 = new net.minecraft.client.model.geom.ModelPart(p25Cubes, java.util.Map.ofEntries(java.util.Map.entry("bone10", p26), java.util.Map.entry("bone11", p27), java.util.Map.entry("bone14", p28), java.util.Map.entry("bone15", p29), java.util.Map.entry("bone16", p30)));
        p25.setInitialPose(PartPose.offsetAndRotation(-1.5F, 3.0F, 3.2F, 0.0F, 0.0F, 0.0F)); p25.resetPose();
        var p24Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_root/__cem_host_head/head/others/soul/body2/belowwasit/bone9", java.util.List.of()));
        var p24 = new net.minecraft.client.model.geom.ModelPart(p24Cubes, java.util.Map.ofEntries());
        p24.setInitialPose(PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -1.57079633F, -0.0872664626F)); p24.resetPose();
        var p23Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_root/__cem_host_head/head/others/soul/body2/belowwasit/backsirk", java.util.List.of()));
        var p23 = new net.minecraft.client.model.geom.ModelPart(p23Cubes, java.util.Map.ofEntries());
        p23.setInitialPose(PartPose.offsetAndRotation(0.4F, 0.0F, 5.6F, 0.0872664626F, 0.0F, 0.0F)); p23.resetPose();
        var p22Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_root/__cem_host_head/head/others/soul/body2/belowwasit/bone7", java.util.List.of()));
        var p22 = new net.minecraft.client.model.geom.ModelPart(p22Cubes, java.util.Map.ofEntries());
        p22.setInitialPose(PartPose.offsetAndRotation(-5.2F, 0.0F, 6.0F, 0.0F, 1.57079633F, 0.0872664626F)); p22.resetPose();
        var p21Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_root/__cem_host_head/head/others/soul/body2/belowwasit/frontsirk", java.util.List.of()));
        var p21 = new net.minecraft.client.model.geom.ModelPart(p21Cubes, java.util.Map.ofEntries());
        p21.setInitialPose(PartPose.offsetAndRotation(-5.6F, 0.0F, 0.0F, -0.0872664626F, 0.0F, 0.0F)); p21.resetPose();
        var p20Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_root/__cem_host_head/head/others/soul/body2/belowwasit", java.util.List.of()));
        var p20 = new net.minecraft.client.model.geom.ModelPart(p20Cubes, java.util.Map.ofEntries(java.util.Map.entry("frontsirk", p21), java.util.Map.entry("bone7", p22), java.util.Map.entry("backsirk", p23), java.util.Map.entry("bone9", p24)));
        p20.setInitialPose(PartPose.offsetAndRotation(0.1F, 5.1F, -2.5F, 0.0F, 0.0F, 0.0F)); p20.resetPose();
        var p19Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_root/__cem_host_head/head/others/soul/body2/bodybo", java.util.List.of()));
        var p19 = new net.minecraft.client.model.geom.ModelPart(p19Cubes, java.util.Map.ofEntries());
        p19.setInitialPose(PartPose.offsetAndRotation(-2.5F, 2.5F, 0.5F, 0.0F, 1.57079633F, 0.0F)); p19.resetPose();
        var p18Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_root/__cem_host_head/head/others/soul/body2", java.util.List.of()));
        var p18 = new net.minecraft.client.model.geom.ModelPart(p18Cubes, java.util.Map.ofEntries(java.util.Map.entry("bodybo", p19), java.util.Map.entry("belowwasit", p20), java.util.Map.entry("bow", p25), java.util.Map.entry("leftarm", p31), java.util.Map.entry("rightarm", p33)));
        p18.setInitialPose(PartPose.offsetAndRotation(0.0F, -5.0F, 2.0F, 0.0F, 0.0F, 0.0F)); p18.resetPose();
        var p17Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_root/__cem_host_head/head/others/soul/hat/bone5", java.util.List.of()));
        var p17 = new net.minecraft.client.model.geom.ModelPart(p17Cubes, java.util.Map.ofEntries());
        p17.setInitialPose(PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p17.resetPose();
        var p16Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_root/__cem_host_head/head/others/soul/hat/hat5", java.util.List.of()));
        var p16 = new net.minecraft.client.model.geom.ModelPart(p16Cubes, java.util.Map.ofEntries());
        p16.setInitialPose(PartPose.offsetAndRotation(3.0F, 0.0F, -3.0F, 0.0F, -1.57079633F, -0.497418837F)); p16.resetPose();
        var p15Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_root/__cem_host_head/head/others/soul/hat/hat4", java.util.List.of()));
        var p15 = new net.minecraft.client.model.geom.ModelPart(p15Cubes, java.util.Map.ofEntries());
        p15.setInitialPose(PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 1.57079633F, 0.479965544F)); p15.resetPose();
        var p14Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_root/__cem_host_head/head/others/soul/hat/hat3", java.util.List.of()));
        var p14 = new net.minecraft.client.model.geom.ModelPart(p14Cubes, java.util.Map.ofEntries());
        p14.setInitialPose(PartPose.offsetAndRotation(0.0F, 0.0F, -3.0F, -0.497418837F, 0.0F, 0.0F)); p14.resetPose();
        var p13Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_root/__cem_host_head/head/others/soul/hat/hat2", java.util.List.of()));
        var p13 = new net.minecraft.client.model.geom.ModelPart(p13Cubes, java.util.Map.ofEntries());
        p13.setInitialPose(PartPose.offsetAndRotation(3.0F, 0.0F, 0.0F, 0.497418837F, 0.0F, 0.0F)); p13.resetPose();
        var p12Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_root/__cem_host_head/head/others/soul/hat", java.util.List.of()));
        var p12 = new net.minecraft.client.model.geom.ModelPart(p12Cubes, java.util.Map.ofEntries(java.util.Map.entry("hat2", p13), java.util.Map.entry("hat3", p14), java.util.Map.entry("hat4", p15), java.util.Map.entry("hat5", p16), java.util.Map.entry("bone5", p17)));
        p12.setInitialPose(PartPose.offsetAndRotation(-4.0F, -11.85F, 4.0F, 0.0F, 0.0F, 0.0F)); p12.resetPose();
        var p11Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_root/__cem_host_head/head/others/soul/head3/eyes", java.util.List.of()));
        var p11 = new net.minecraft.client.model.geom.ModelPart(p11Cubes, java.util.Map.ofEntries());
        p11.setInitialPose(PartPose.offsetAndRotation(0.0F, 0.0F, -0.05F, 0.0F, 0.0F, 0.0F)); p11.resetPose();
        var p10Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_root/__cem_host_head/head/others/soul/head3", java.util.List.of()));
        var p10 = new net.minecraft.client.model.geom.ModelPart(p10Cubes, java.util.Map.ofEntries(java.util.Map.entry("eyes", p11)));
        p10.setInitialPose(PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p10.resetPose();
        var p9Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_root/__cem_host_head/head/others/soul", java.util.List.of()));
        var p9 = new net.minecraft.client.model.geom.ModelPart(p9Cubes, java.util.Map.ofEntries(java.util.Map.entry("head3", p10), java.util.Map.entry("hat", p12), java.util.Map.entry("body2", p18)));
        p9.setInitialPose(PartPose.offsetAndRotation(3.0F, 1.0F, -2.0F, 0.0F, 0.0F, 0.0F)); p9.resetPose();
        var p8Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_root/__cem_host_head/head/others", java.util.List.of()));
        var p8 = new net.minecraft.client.model.geom.ModelPart(p8Cubes, java.util.Map.ofEntries(java.util.Map.entry("soul", p9)));
        p8.setInitialPose(PartPose.offsetAndRotation(0.0F, -6.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p8.resetPose();
        var p7Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_root/__cem_host_head/head", java.util.List.of()));
        var p7 = new net.minecraft.client.model.geom.ModelPart(p7Cubes, java.util.Map.ofEntries(java.util.Map.entry("others", p8)));
        p7.setInitialPose(PartPose.offsetAndRotation(0.0F, 6.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p7.resetPose();
        var p6Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_root/__cem_host_body/__cem_host_right_wing", java.util.List.of()));
        var p6 = new net.minecraft.client.model.geom.ModelPart(p6Cubes, java.util.Map.ofEntries(java.util.Map.entry("right_wing", p39)));
        p6.setInitialPose(nativeRoot.getChild("root").getChild("body").getChild("right_wing").storePose()); p6.resetPose();
        var p5Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_root/__cem_host_body/__cem_host_left_wing", java.util.List.of()));
        var p5 = new net.minecraft.client.model.geom.ModelPart(p5Cubes, java.util.Map.ofEntries(java.util.Map.entry("left_wing", p38)));
        p5.setInitialPose(nativeRoot.getChild("root").getChild("body").getChild("left_wing").storePose()); p5.resetPose();
        var p4Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_root/__cem_host_body/__cem_host_right_arm", java.util.List.of()));
        var p4 = new net.minecraft.client.model.geom.ModelPart(p4Cubes, java.util.Map.ofEntries(java.util.Map.entry("right_arm", p36)));
        p4.setInitialPose(nativeRoot.getChild("root").getChild("body").getChild("right_arm").storePose()); p4.resetPose();
        var p3Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_root/__cem_host_body/__cem_host_left_arm", java.util.List.of()));
        var p3 = new net.minecraft.client.model.geom.ModelPart(p3Cubes, java.util.Map.ofEntries(java.util.Map.entry("left_arm", p37)));
        p3.setInitialPose(nativeRoot.getChild("root").getChild("body").getChild("left_arm").storePose()); p3.resetPose();
        var p2Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_root/__cem_host_body", java.util.List.of()));
        var p2 = new net.minecraft.client.model.geom.ModelPart(p2Cubes, java.util.Map.ofEntries(java.util.Map.entry("__cem_host_left_arm", p3), java.util.Map.entry("__cem_host_right_arm", p4), java.util.Map.entry("__cem_host_left_wing", p5), java.util.Map.entry("__cem_host_right_wing", p6), java.util.Map.entry("body", p35)));
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
