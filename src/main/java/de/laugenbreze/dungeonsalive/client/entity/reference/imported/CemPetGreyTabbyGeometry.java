/*
 * Generated adaptation of ReCrafted Mobs 11.0 by GillsBills and credited contributors.
 * Authored asset license: CC-BY-NC-SA-4.0; https://creativecommons.org/licenses/by-nc-sa/4.0/
 * Source: https://modrinth.com/resourcepack/recrafted-mobs
 * Model: assets/minecraft/optifine/cem/cat/cat.jem
 * Source SHA256: 7c12db1234223418f2ba6a485ddcb1e050f452f26e2f05ee08832c04b02b6327
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

public final class CemPetGreyTabbyGeometry {
    private CemPetGreyTabbyGeometry() {}

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        PartDefinition p0 = root.addOrReplaceChild("__cem_host_head", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p1 = root.addOrReplaceChild("__cem_host_body", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p2 = root.addOrReplaceChild("__cem_host_back_right_leg", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p3 = root.addOrReplaceChild("__cem_host_back_left_leg", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p4 = root.addOrReplaceChild("__cem_host_front_right_leg", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p5 = root.addOrReplaceChild("__cem_host_front_left_leg", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p6 = root.addOrReplaceChild("__cem_host_tail", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p7 = root.addOrReplaceChild("__cem_host_tail2", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p8 = p0.addOrReplaceChild("head", CubeListBuilder.create()
                .texOffs(0, 0).mirror(false)
                .addBox(-2.5F, -11.0F, -12.0F, 5.0F, 4.0F, 5.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(0, 24).mirror(false)
                .addBox(-1.5F, -9.02F, -13.0F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(0, 10).mirror(false)
                .addBox(-2.0F, -12.0F, -9.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(6, 10).mirror(false)
                .addBox(1.0F, -12.0F, -9.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(0.0F, 9.0F, 9.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p9 = p1.addOrReplaceChild("body", CubeListBuilder.create()
                .texOffs(20, 0).mirror(false)
                .addBox(-2.0F, -9.0F, -18.0F, 4.0F, 16.0F, 6.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(0.0F, 12.0F, 10.0F, 1.57079633F, 0.0F, 0.0F));
        PartDefinition p10 = p5.addOrReplaceChild("front_left_leg", CubeListBuilder.create()
                .texOffs(40, 0).mirror(false)
                .addBox(0.1F, -9.9F, -5.0F, 2.0F, 10.0F, 2.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(-1.1F, 9.9F, 5.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p11 = p4.addOrReplaceChild("front_right_leg", CubeListBuilder.create()
                .texOffs(40, 0).mirror(false)
                .addBox(-2.1F, -9.9F, -5.0F, 2.0F, 10.0F, 2.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(1.1F, 9.9F, 5.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p12 = p3.addOrReplaceChild("back_left_leg", CubeListBuilder.create()
                .texOffs(8, 13).mirror(false)
                .addBox(0.1F, -6.0F, 6.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(-1.1F, 6.0F, -5.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p13 = p2.addOrReplaceChild("back_right_leg", CubeListBuilder.create()
                .texOffs(8, 13).mirror(false)
                .addBox(-2.1F, -6.0F, 6.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(1.1F, 6.0F, -5.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p14 = p6.addOrReplaceChild("tail", CubeListBuilder.create()
                .texOffs(0, 15).mirror(false)
                .addBox(-0.5F, -8.5F, 8.0F, 1.0F, 8.0F, 1.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(0.0F, 8.5F, -8.0F, 1.57079633F, 0.0F, 0.0F));
        PartDefinition p15 = p7.addOrReplaceChild("tail2", CubeListBuilder.create()
                .texOffs(4, 15).mirror(false)
                .addBox(-0.5F, -8.5F, 16.0F, 1.0F, 8.0F, 1.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(0.0F, 8.5F, -16.0F, 1.57079633F, 0.0F, 0.0F));
        return LayerDefinition.create(mesh, 64, 32);
    }

    /** Preserve source expression units; static scales are reapplied after every pose reset. */
    public static ReferenceMobDefinition.Builder configure(ReferenceMobDefinition.Builder builder) {
        builder.cemDriver("cat");
        builder.bakedRoot(CemPetGreyTabbyGeometry::bakeRoot);
        builder.hostAlias("head", "__cem_host_head");
        builder.hostAlias("body", "__cem_host_body");
        builder.hostAlias("back_right_leg", "__cem_host_back_right_leg");
        builder.hostAlias("back_left_leg", "__cem_host_back_left_leg");
        builder.hostAlias("front_right_leg", "__cem_host_front_right_leg");
        builder.hostAlias("front_left_leg", "__cem_host_front_left_leg");
        builder.hostAlias("leg1", "__cem_host_back_right_leg");
        builder.hostAlias("leg2", "__cem_host_back_left_leg");
        builder.hostAlias("leg3", "__cem_host_front_right_leg");
        builder.hostAlias("leg4", "__cem_host_front_left_leg");
        builder.hostAlias("tail", "__cem_host_tail");
        builder.hostAlias("tail2", "__cem_host_tail2");
        builder.expression("EMF_body.rx", "0");
        builder.expression("EMF_tail.rx", "0");
        builder.expression("EMF_tail2.rx", "0");
        return builder;
    }

    /** Complete matching rig; installed cubes stay owned by the Minecraft client. */
    public static net.minecraft.client.model.geom.ModelPart bakeRoot() {
        var nativeRoot = LayerDefinition.create(net.minecraft.client.model.OcelotModel.createBodyMesh(CubeDeformation.NONE), 64, 32).bakeRoot();
        var nativeCubes = new java.util.HashMap<String, java.util.List<net.minecraft.client.model.geom.ModelPart.Cube>>();
        nativeRoot.visit(new com.mojang.blaze3d.vertex.PoseStack(),
                (pose, path, index, cube) -> nativeCubes.computeIfAbsent(path, key -> new java.util.ArrayList<>()).add(cube));
        var customCubes = new java.util.HashMap<String, java.util.List<net.minecraft.client.model.geom.ModelPart.Cube>>();
        createBodyLayer().bakeRoot().visit(new com.mojang.blaze3d.vertex.PoseStack(),
                (pose, path, index, cube) -> customCubes.computeIfAbsent(path, key -> new java.util.ArrayList<>()).add(cube));
        var p15Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_tail2/tail2", java.util.List.of()));
        var p15 = new net.minecraft.client.model.geom.ModelPart(p15Cubes, java.util.Map.ofEntries());
        p15.setInitialPose(PartPose.offsetAndRotation(0.0F, 8.5F, -16.0F, 1.57079633F, 0.0F, 0.0F)); p15.resetPose();
        var p14Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_tail/tail", java.util.List.of()));
        var p14 = new net.minecraft.client.model.geom.ModelPart(p14Cubes, java.util.Map.ofEntries());
        p14.setInitialPose(PartPose.offsetAndRotation(0.0F, 8.5F, -8.0F, 1.57079633F, 0.0F, 0.0F)); p14.resetPose();
        var p13Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_back_right_leg/back_right_leg", java.util.List.of()));
        var p13 = new net.minecraft.client.model.geom.ModelPart(p13Cubes, java.util.Map.ofEntries());
        p13.setInitialPose(PartPose.offsetAndRotation(1.1F, 6.0F, -5.0F, 0.0F, 0.0F, 0.0F)); p13.resetPose();
        var p12Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_back_left_leg/back_left_leg", java.util.List.of()));
        var p12 = new net.minecraft.client.model.geom.ModelPart(p12Cubes, java.util.Map.ofEntries());
        p12.setInitialPose(PartPose.offsetAndRotation(-1.1F, 6.0F, -5.0F, 0.0F, 0.0F, 0.0F)); p12.resetPose();
        var p11Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_front_right_leg/front_right_leg", java.util.List.of()));
        var p11 = new net.minecraft.client.model.geom.ModelPart(p11Cubes, java.util.Map.ofEntries());
        p11.setInitialPose(PartPose.offsetAndRotation(1.1F, 9.9F, 5.0F, 0.0F, 0.0F, 0.0F)); p11.resetPose();
        var p10Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_front_left_leg/front_left_leg", java.util.List.of()));
        var p10 = new net.minecraft.client.model.geom.ModelPart(p10Cubes, java.util.Map.ofEntries());
        p10.setInitialPose(PartPose.offsetAndRotation(-1.1F, 9.9F, 5.0F, 0.0F, 0.0F, 0.0F)); p10.resetPose();
        var p9Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/body", java.util.List.of()));
        var p9 = new net.minecraft.client.model.geom.ModelPart(p9Cubes, java.util.Map.ofEntries());
        p9.setInitialPose(PartPose.offsetAndRotation(0.0F, 12.0F, 10.0F, 1.57079633F, 0.0F, 0.0F)); p9.resetPose();
        var p8Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_head/head", java.util.List.of()));
        var p8 = new net.minecraft.client.model.geom.ModelPart(p8Cubes, java.util.Map.ofEntries());
        p8.setInitialPose(PartPose.offsetAndRotation(0.0F, 9.0F, 9.0F, 0.0F, 0.0F, 0.0F)); p8.resetPose();
        var p7Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_tail2", java.util.List.of()));
        var p7 = new net.minecraft.client.model.geom.ModelPart(p7Cubes, java.util.Map.ofEntries(java.util.Map.entry("tail2", p15)));
        p7.setInitialPose(nativeRoot.getChild("tail2").storePose()); p7.resetPose();
        var p6Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_tail", java.util.List.of()));
        var p6 = new net.minecraft.client.model.geom.ModelPart(p6Cubes, java.util.Map.ofEntries(java.util.Map.entry("tail", p14)));
        p6.setInitialPose(nativeRoot.getChild("tail1").storePose()); p6.resetPose();
        var p5Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_front_left_leg", java.util.List.of()));
        var p5 = new net.minecraft.client.model.geom.ModelPart(p5Cubes, java.util.Map.ofEntries(java.util.Map.entry("front_left_leg", p10)));
        p5.setInitialPose(nativeRoot.getChild("left_front_leg").storePose()); p5.resetPose();
        var p4Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_front_right_leg", java.util.List.of()));
        var p4 = new net.minecraft.client.model.geom.ModelPart(p4Cubes, java.util.Map.ofEntries(java.util.Map.entry("front_right_leg", p11)));
        p4.setInitialPose(nativeRoot.getChild("right_front_leg").storePose()); p4.resetPose();
        var p3Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_back_left_leg", java.util.List.of()));
        var p3 = new net.minecraft.client.model.geom.ModelPart(p3Cubes, java.util.Map.ofEntries(java.util.Map.entry("back_left_leg", p12)));
        p3.setInitialPose(nativeRoot.getChild("left_hind_leg").storePose()); p3.resetPose();
        var p2Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_back_right_leg", java.util.List.of()));
        var p2 = new net.minecraft.client.model.geom.ModelPart(p2Cubes, java.util.Map.ofEntries(java.util.Map.entry("back_right_leg", p13)));
        p2.setInitialPose(nativeRoot.getChild("right_hind_leg").storePose()); p2.resetPose();
        var p1Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body", java.util.List.of()));
        var p1 = new net.minecraft.client.model.geom.ModelPart(p1Cubes, java.util.Map.ofEntries(java.util.Map.entry("body", p9)));
        p1.setInitialPose(nativeRoot.getChild("body").storePose()); p1.resetPose();
        var p0Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_head", java.util.List.of()));
        var p0 = new net.minecraft.client.model.geom.ModelPart(p0Cubes, java.util.Map.ofEntries(java.util.Map.entry("head", p8)));
        p0.setInitialPose(nativeRoot.getChild("head").storePose()); p0.resetPose();
        return new net.minecraft.client.model.geom.ModelPart(java.util.List.of(), java.util.Map.ofEntries(java.util.Map.entry("__cem_host_head", p0), java.util.Map.entry("__cem_host_body", p1), java.util.Map.entry("__cem_host_back_right_leg", p2), java.util.Map.entry("__cem_host_back_left_leg", p3), java.util.Map.entry("__cem_host_front_right_leg", p4), java.util.Map.entry("__cem_host_front_left_leg", p5), java.util.Map.entry("__cem_host_tail", p6), java.util.Map.entry("__cem_host_tail2", p7)));
    }
}
