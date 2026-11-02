/*
 * Generated adaptation of ReCrafted Mobs 11.0 by GillsBills and credited contributors.
 * Authored asset license: CC-BY-NC-SA-4.0; https://creativecommons.org/licenses/by-nc-sa/4.0/
 * Source: https://modrinth.com/resourcepack/recrafted-mobs
 * Model: assets/minecraft/optifine/cem/phantom/phantom2.jem
 * Source SHA256: 483cea28407717ede11c04ea47f476b30c2b1f35204ba0690984b94f96a5b8f5
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

public final class CemVultureGeometry {
    private CemVultureGeometry() {}

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        PartDefinition p0 = root.addOrReplaceChild("__cem_host_body", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p1 = p0.addOrReplaceChild("__cem_host_head", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p2 = p0.addOrReplaceChild("__cem_host_tail", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p3 = p2.addOrReplaceChild("__cem_host_tail2", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p4 = p0.addOrReplaceChild("__cem_host_left_wing", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p5 = p4.addOrReplaceChild("__cem_host_left_wing_tip", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p6 = p0.addOrReplaceChild("__cem_host_right_wing", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p7 = p6.addOrReplaceChild("__cem_host_right_wing_tip", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p8 = p0.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offsetAndRotation(-0.5F, 5.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p9 = p8.addOrReplaceChild("Vulture", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0872664626F, 0.0F, 0.0F));
        PartDefinition p10 = p9.addOrReplaceChild("body2", CubeListBuilder.create()
                .texOffs(0, 8).mirror(false)
                .addBox(-2.0F, -3.0F, -5.0F, 5.0F, 5.0F, 12.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(0, 25).mirror(false)
                .addBox(-2.0F, -3.0F, -5.0F, 5.0F, 5.0F, 3.0F, new CubeDeformation(0.25F, 0.25F, 0.25F)), PartPose.offsetAndRotation(-0.5F, -5.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p11 = p10.addOrReplaceChild("neck", CubeListBuilder.create()
                .texOffs(0, 13).mirror(false)
                .addBox(-1.5F, 0.49824F, -0.87722F, 2.0F, 2.0F, 4.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(24, 29).mirror(false)
                .addBox(-1.5F, -2.50176F, -0.87722F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(1.0F, -0.5F, -8.0F, 0.479965544F, 0.0F, 0.0F));
        PartDefinition p12 = p11.addOrReplaceChild("neck2", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 2.0F, -3.0F, -0.959931089F, 0.0F, 0.0F));
        PartDefinition p13 = p12.addOrReplaceChild("neck3", CubeListBuilder.create()
                .texOffs(22, 8).mirror(false)
                .addBox(-2.5F, -5.0F, -1.0F, 4.0F, 4.0F, 5.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(16, 25).mirror(false)
                .addBox(-2.0F, -4.0F, -3.0F, 3.0F, 3.0F, 2.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(0.0F, -2.0F, -3.0F, 0.479965544F, 0.0F, 0.0F));
        PartDefinition p14 = p10.addOrReplaceChild("right_leg", CubeListBuilder.create()
                .texOffs(0, 0).mirror(false)
                .addBox(-0.5F, 2.0F, 0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(2.0F, 2.0F, 5.0F, 0.523598776F, 0.0F, 0.0F));
        PartDefinition p15 = p14.addOrReplaceChild("__cem_unnamed_0_0_0_1_0", CubeListBuilder.create()
                .texOffs(16, 30).mirror(true)
                .addBox(-1.0F, 0.0F, 0.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(-0.05F, -0.05F, -0.05F)), PartPose.ZERO);
        PartDefinition p16 = p10.addOrReplaceChild("left_leg", CubeListBuilder.create()
                .texOffs(16, 30).mirror(false)
                .addBox(-1.0F, 0.0F, 0.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(-0.05F, -0.05F, -0.05F))
                .texOffs(0, 0).mirror(false)
                .addBox(-0.5F, 2.0F, 0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(-1.0F, 2.0F, 5.0F, 0.567232007F, 0.0F, 0.0F));
        PartDefinition p17 = p10.addOrReplaceChild("tail3", CubeListBuilder.create()
                .texOffs(0, 8).mirror(false)
                .addBox(-1.0F, 0.0F, 0.0F, 3.0F, 0.0F, 5.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(0.0F, -0.5F, 7.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p18 = p9.addOrReplaceChild("left_wing2", CubeListBuilder.create()
                .texOffs(0, 0).mirror(false)
                .addBox(0.0F, 0.0F, 0.0F, 14.0F, 1.0F, 7.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(2.5F, -8.0F, -5.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p19 = p18.addOrReplaceChild("left_wing_tip2", CubeListBuilder.create()
                .texOffs(0, 34).mirror(false)
                .addBox(0.0F, 0.0F, 0.0F, 6.0F, 1.0F, 5.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(14.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p20 = p9.addOrReplaceChild("right_wing2", CubeListBuilder.create(), PartPose.offsetAndRotation(-2.5F, -7.0F, -8.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p21 = p20.addOrReplaceChild("body_sub_10", CubeListBuilder.create()
                .texOffs(0, 0).mirror(true)
                .addBox(-14.0F, 0.0F, 0.0F, 14.0F, 1.0F, 7.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(0.0F, -1.0F, 3.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p22 = p21.addOrReplaceChild("right_wing_tip2", CubeListBuilder.create(), PartPose.offsetAndRotation(-14.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p23 = p22.addOrReplaceChild("body_sub_13", CubeListBuilder.create()
                .texOffs(0, 34).mirror(true)
                .addBox(-6.0F, 0.0F, 0.0F, 6.0F, 1.0F, 5.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.ZERO);
        PartDefinition p24 = p1.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offsetAndRotation(-0.5F, 3.75F, 8.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p25 = p4.addOrReplaceChild("left_wing", CubeListBuilder.create(), PartPose.offsetAndRotation(-2.5F, 7.0F, 8.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p26 = p5.addOrReplaceChild("left_wing_tip", CubeListBuilder.create(), PartPose.offsetAndRotation(-8.5F, 7.0F, 8.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p27 = p6.addOrReplaceChild("right_wing", CubeListBuilder.create(), PartPose.offsetAndRotation(2.5F, 7.0F, 8.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p28 = p7.addOrReplaceChild("right_wing_tip", CubeListBuilder.create(), PartPose.offsetAndRotation(8.5F, 7.0F, 8.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p29 = p2.addOrReplaceChild("tail", CubeListBuilder.create(), PartPose.offsetAndRotation(-0.5F, 7.0F, -1.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p30 = p3.addOrReplaceChild("tail2", CubeListBuilder.create(), PartPose.offsetAndRotation(-0.5F, 6.5F, -7.0F, 0.0F, 0.0F, 0.0F));
        return LayerDefinition.create(mesh, 64, 64);
    }

    /** Preserve source expression units; static scales are reapplied after every pose reset. */
    public static ReferenceMobDefinition.Builder configure(ReferenceMobDefinition.Builder builder) {
        builder.cemDriver("phantom");
        builder.bakedRoot(CemVultureGeometry::bakeRoot);
        builder.hostAlias("body", "__cem_host_body");
        builder.hostAlias("head", "__cem_host_head");
        builder.hostAlias("tail", "__cem_host_tail");
        builder.hostAlias("tail2", "__cem_host_tail2");
        builder.hostAlias("left_wing", "__cem_host_left_wing");
        builder.hostAlias("left_wing_tip", "__cem_host_left_wing_tip");
        builder.hostAlias("right_wing", "__cem_host_right_wing");
        builder.hostAlias("right_wing_tip", "__cem_host_right_wing_tip");
        builder.expression("left_wing2.rz", "left_wing.rz");
        builder.expression("right_wing2.rz", "right_wing.rz");
        builder.expression("left_wing_tip2.rz", "left_wing_tip.rz");
        builder.expression("right_wing_tip2.rz", "right_wing_tip.rz");
        builder.expression("tail3.rx", "tail.rx");
        return builder;
    }

    /** Complete matching rig; installed cubes stay owned by the Minecraft client. */
    public static net.minecraft.client.model.geom.ModelPart bakeRoot() {
        var nativeRoot = net.minecraft.client.model.PhantomModel.createBodyLayer().bakeRoot();
        var nativeCubes = new java.util.HashMap<String, java.util.List<net.minecraft.client.model.geom.ModelPart.Cube>>();
        nativeRoot.visit(new com.mojang.blaze3d.vertex.PoseStack(),
                (pose, path, index, cube) -> nativeCubes.computeIfAbsent(path, key -> new java.util.ArrayList<>()).add(cube));
        var customCubes = new java.util.HashMap<String, java.util.List<net.minecraft.client.model.geom.ModelPart.Cube>>();
        createBodyLayer().bakeRoot().visit(new com.mojang.blaze3d.vertex.PoseStack(),
                (pose, path, index, cube) -> customCubes.computeIfAbsent(path, key -> new java.util.ArrayList<>()).add(cube));
        var p30Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/__cem_host_tail/__cem_host_tail2/tail2", java.util.List.of()));
        var p30 = new net.minecraft.client.model.geom.ModelPart(p30Cubes, java.util.Map.ofEntries());
        p30.setInitialPose(PartPose.offsetAndRotation(-0.5F, 6.5F, -7.0F, 0.0F, 0.0F, 0.0F)); p30.resetPose();
        var p29Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/__cem_host_tail/tail", java.util.List.of()));
        var p29 = new net.minecraft.client.model.geom.ModelPart(p29Cubes, java.util.Map.ofEntries());
        p29.setInitialPose(PartPose.offsetAndRotation(-0.5F, 7.0F, -1.0F, 0.0F, 0.0F, 0.0F)); p29.resetPose();
        var p28Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/__cem_host_right_wing/__cem_host_right_wing_tip/right_wing_tip", java.util.List.of()));
        var p28 = new net.minecraft.client.model.geom.ModelPart(p28Cubes, java.util.Map.ofEntries());
        p28.setInitialPose(PartPose.offsetAndRotation(8.5F, 7.0F, 8.0F, 0.0F, 0.0F, 0.0F)); p28.resetPose();
        var p27Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/__cem_host_right_wing/right_wing", java.util.List.of()));
        var p27 = new net.minecraft.client.model.geom.ModelPart(p27Cubes, java.util.Map.ofEntries());
        p27.setInitialPose(PartPose.offsetAndRotation(2.5F, 7.0F, 8.0F, 0.0F, 0.0F, 0.0F)); p27.resetPose();
        var p26Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/__cem_host_left_wing/__cem_host_left_wing_tip/left_wing_tip", java.util.List.of()));
        var p26 = new net.minecraft.client.model.geom.ModelPart(p26Cubes, java.util.Map.ofEntries());
        p26.setInitialPose(PartPose.offsetAndRotation(-8.5F, 7.0F, 8.0F, 0.0F, 0.0F, 0.0F)); p26.resetPose();
        var p25Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/__cem_host_left_wing/left_wing", java.util.List.of()));
        var p25 = new net.minecraft.client.model.geom.ModelPart(p25Cubes, java.util.Map.ofEntries());
        p25.setInitialPose(PartPose.offsetAndRotation(-2.5F, 7.0F, 8.0F, 0.0F, 0.0F, 0.0F)); p25.resetPose();
        var p24Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/__cem_host_head/head", java.util.List.of()));
        var p24 = new net.minecraft.client.model.geom.ModelPart(p24Cubes, java.util.Map.ofEntries());
        p24.setInitialPose(PartPose.offsetAndRotation(-0.5F, 3.75F, 8.0F, 0.0F, 0.0F, 0.0F)); p24.resetPose();
        var p23Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/body/Vulture/right_wing2/body_sub_10/right_wing_tip2/body_sub_13", java.util.List.of()));
        var p23 = new net.minecraft.client.model.geom.ModelPart(p23Cubes, java.util.Map.ofEntries());
        p23.setInitialPose(PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p23.resetPose();
        var p22Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/body/Vulture/right_wing2/body_sub_10/right_wing_tip2", java.util.List.of()));
        var p22 = new net.minecraft.client.model.geom.ModelPart(p22Cubes, java.util.Map.ofEntries(java.util.Map.entry("body_sub_13", p23)));
        p22.setInitialPose(PartPose.offsetAndRotation(-14.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p22.resetPose();
        var p21Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/body/Vulture/right_wing2/body_sub_10", java.util.List.of()));
        var p21 = new net.minecraft.client.model.geom.ModelPart(p21Cubes, java.util.Map.ofEntries(java.util.Map.entry("right_wing_tip2", p22)));
        p21.setInitialPose(PartPose.offsetAndRotation(0.0F, -1.0F, 3.0F, 0.0F, 0.0F, 0.0F)); p21.resetPose();
        var p20Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/body/Vulture/right_wing2", java.util.List.of()));
        var p20 = new net.minecraft.client.model.geom.ModelPart(p20Cubes, java.util.Map.ofEntries(java.util.Map.entry("body_sub_10", p21)));
        p20.setInitialPose(PartPose.offsetAndRotation(-2.5F, -7.0F, -8.0F, 0.0F, 0.0F, 0.0F)); p20.resetPose();
        var p19Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/body/Vulture/left_wing2/left_wing_tip2", java.util.List.of()));
        var p19 = new net.minecraft.client.model.geom.ModelPart(p19Cubes, java.util.Map.ofEntries());
        p19.setInitialPose(PartPose.offsetAndRotation(14.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p19.resetPose();
        var p18Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/body/Vulture/left_wing2", java.util.List.of()));
        var p18 = new net.minecraft.client.model.geom.ModelPart(p18Cubes, java.util.Map.ofEntries(java.util.Map.entry("left_wing_tip2", p19)));
        p18.setInitialPose(PartPose.offsetAndRotation(2.5F, -8.0F, -5.0F, 0.0F, 0.0F, 0.0F)); p18.resetPose();
        var p17Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/body/Vulture/body2/tail3", java.util.List.of()));
        var p17 = new net.minecraft.client.model.geom.ModelPart(p17Cubes, java.util.Map.ofEntries());
        p17.setInitialPose(PartPose.offsetAndRotation(0.0F, -0.5F, 7.0F, 0.0F, 0.0F, 0.0F)); p17.resetPose();
        var p16Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/body/Vulture/body2/left_leg", java.util.List.of()));
        var p16 = new net.minecraft.client.model.geom.ModelPart(p16Cubes, java.util.Map.ofEntries());
        p16.setInitialPose(PartPose.offsetAndRotation(-1.0F, 2.0F, 5.0F, 0.567232007F, 0.0F, 0.0F)); p16.resetPose();
        var p15Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/body/Vulture/body2/right_leg/__cem_unnamed_0_0_0_1_0", java.util.List.of()));
        var p15 = new net.minecraft.client.model.geom.ModelPart(p15Cubes, java.util.Map.ofEntries());
        p15.setInitialPose(PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p15.resetPose();
        var p14Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/body/Vulture/body2/right_leg", java.util.List.of()));
        var p14 = new net.minecraft.client.model.geom.ModelPart(p14Cubes, java.util.Map.ofEntries(java.util.Map.entry("__cem_unnamed_0_0_0_1_0", p15)));
        p14.setInitialPose(PartPose.offsetAndRotation(2.0F, 2.0F, 5.0F, 0.523598776F, 0.0F, 0.0F)); p14.resetPose();
        var p13Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/body/Vulture/body2/neck/neck2/neck3", java.util.List.of()));
        var p13 = new net.minecraft.client.model.geom.ModelPart(p13Cubes, java.util.Map.ofEntries());
        p13.setInitialPose(PartPose.offsetAndRotation(0.0F, -2.0F, -3.0F, 0.479965544F, 0.0F, 0.0F)); p13.resetPose();
        var p12Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/body/Vulture/body2/neck/neck2", java.util.List.of()));
        var p12 = new net.minecraft.client.model.geom.ModelPart(p12Cubes, java.util.Map.ofEntries(java.util.Map.entry("neck3", p13)));
        p12.setInitialPose(PartPose.offsetAndRotation(0.0F, 2.0F, -3.0F, -0.959931089F, 0.0F, 0.0F)); p12.resetPose();
        var p11Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/body/Vulture/body2/neck", java.util.List.of()));
        var p11 = new net.minecraft.client.model.geom.ModelPart(p11Cubes, java.util.Map.ofEntries(java.util.Map.entry("neck2", p12)));
        p11.setInitialPose(PartPose.offsetAndRotation(1.0F, -0.5F, -8.0F, 0.479965544F, 0.0F, 0.0F)); p11.resetPose();
        var p10Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/body/Vulture/body2", java.util.List.of()));
        var p10 = new net.minecraft.client.model.geom.ModelPart(p10Cubes, java.util.Map.ofEntries(java.util.Map.entry("neck", p11), java.util.Map.entry("right_leg", p14), java.util.Map.entry("left_leg", p16), java.util.Map.entry("tail3", p17)));
        p10.setInitialPose(PartPose.offsetAndRotation(-0.5F, -5.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p10.resetPose();
        var p9Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/body/Vulture", java.util.List.of()));
        var p9 = new net.minecraft.client.model.geom.ModelPart(p9Cubes, java.util.Map.ofEntries(java.util.Map.entry("body2", p10), java.util.Map.entry("left_wing2", p18), java.util.Map.entry("right_wing2", p20)));
        p9.setInitialPose(PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0872664626F, 0.0F, 0.0F)); p9.resetPose();
        var p8Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/body", java.util.List.of()));
        var p8 = new net.minecraft.client.model.geom.ModelPart(p8Cubes, java.util.Map.ofEntries(java.util.Map.entry("Vulture", p9)));
        p8.setInitialPose(PartPose.offsetAndRotation(-0.5F, 5.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p8.resetPose();
        var p7Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/__cem_host_right_wing/__cem_host_right_wing_tip", java.util.List.of()));
        var p7 = new net.minecraft.client.model.geom.ModelPart(p7Cubes, java.util.Map.ofEntries(java.util.Map.entry("right_wing_tip", p28)));
        p7.setInitialPose(nativeRoot.getChild("body").getChild("right_wing_base").getChild("right_wing_tip").storePose()); p7.resetPose();
        var p6Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/__cem_host_right_wing", java.util.List.of()));
        var p6 = new net.minecraft.client.model.geom.ModelPart(p6Cubes, java.util.Map.ofEntries(java.util.Map.entry("__cem_host_right_wing_tip", p7), java.util.Map.entry("right_wing", p27)));
        p6.setInitialPose(nativeRoot.getChild("body").getChild("right_wing_base").storePose()); p6.resetPose();
        var p5Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/__cem_host_left_wing/__cem_host_left_wing_tip", java.util.List.of()));
        var p5 = new net.minecraft.client.model.geom.ModelPart(p5Cubes, java.util.Map.ofEntries(java.util.Map.entry("left_wing_tip", p26)));
        p5.setInitialPose(nativeRoot.getChild("body").getChild("left_wing_base").getChild("left_wing_tip").storePose()); p5.resetPose();
        var p4Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/__cem_host_left_wing", java.util.List.of()));
        var p4 = new net.minecraft.client.model.geom.ModelPart(p4Cubes, java.util.Map.ofEntries(java.util.Map.entry("__cem_host_left_wing_tip", p5), java.util.Map.entry("left_wing", p25)));
        p4.setInitialPose(nativeRoot.getChild("body").getChild("left_wing_base").storePose()); p4.resetPose();
        var p3Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/__cem_host_tail/__cem_host_tail2", java.util.List.of()));
        var p3 = new net.minecraft.client.model.geom.ModelPart(p3Cubes, java.util.Map.ofEntries(java.util.Map.entry("tail2", p30)));
        p3.setInitialPose(nativeRoot.getChild("body").getChild("tail_base").getChild("tail_tip").storePose()); p3.resetPose();
        var p2Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/__cem_host_tail", java.util.List.of()));
        var p2 = new net.minecraft.client.model.geom.ModelPart(p2Cubes, java.util.Map.ofEntries(java.util.Map.entry("__cem_host_tail2", p3), java.util.Map.entry("tail", p29)));
        p2.setInitialPose(nativeRoot.getChild("body").getChild("tail_base").storePose()); p2.resetPose();
        var p1Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/__cem_host_head", java.util.List.of()));
        var p1 = new net.minecraft.client.model.geom.ModelPart(p1Cubes, java.util.Map.ofEntries(java.util.Map.entry("head", p24)));
        p1.setInitialPose(nativeRoot.getChild("body").getChild("head").storePose()); p1.resetPose();
        var p0Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body", java.util.List.of()));
        var p0 = new net.minecraft.client.model.geom.ModelPart(p0Cubes, java.util.Map.ofEntries(java.util.Map.entry("__cem_host_head", p1), java.util.Map.entry("__cem_host_tail", p2), java.util.Map.entry("__cem_host_left_wing", p4), java.util.Map.entry("__cem_host_right_wing", p6), java.util.Map.entry("body", p8)));
        p0.setInitialPose(nativeRoot.getChild("body").storePose()); p0.resetPose();
        return new net.minecraft.client.model.geom.ModelPart(java.util.List.of(), java.util.Map.ofEntries(java.util.Map.entry("__cem_host_body", p0)));
    }
}
