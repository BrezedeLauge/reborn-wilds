/*
 * Generated adaptation of ReCrafted Mobs 11.0 by GillsBills and credited contributors.
 * Authored asset license: CC-BY-NC-SA-4.0; https://creativecommons.org/licenses/by-nc-sa/4.0/
 * Source: https://modrinth.com/resourcepack/recrafted-mobs
 * Model: assets/minecraft/optifine/cem/sheep/sheep.jem
 * Source SHA256: 945296832dd7d8f1a7f5c10128e89dbfd7f88cc6da7977243c0a3c138131cb87
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

public final class CemPoisonousGreenSheepGeometry {
    private CemPoisonousGreenSheepGeometry() {}

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        PartDefinition p0 = root.addOrReplaceChild("__cem_host_head", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p1 = root.addOrReplaceChild("__cem_host_body", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p2 = root.addOrReplaceChild("__cem_host_leg1", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p3 = root.addOrReplaceChild("__cem_host_leg2", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p4 = root.addOrReplaceChild("__cem_host_leg3", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p5 = root.addOrReplaceChild("__cem_host_leg4", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p6 = p1.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 19.0F, -2.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p7 = p6.addOrReplaceChild("rotation", CubeListBuilder.create()
                .texOffs(28, 8).mirror(false)
                .addBox(-4.0F, -10.0F, -7.0F, 8.0F, 16.0F, 6.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(0.0F, -19.0F, 2.0F, 1.57079633F, 0.0F, 0.0F));
        PartDefinition p8 = p0.addOrReplaceChild("head", CubeListBuilder.create()
                .texOffs(0, 0).mirror(false)
                .addBox(-3.0F, -22.0F, -14.0F, 6.0F, 6.0F, 8.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(0.0F, 18.0F, 8.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p9 = p2.addOrReplaceChild("leg1", CubeListBuilder.create()
                .texOffs(17, 46).mirror(false)
                .addBox(-5.0F, -12.0F, 5.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(3.0F, 12.0F, -7.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p10 = p3.addOrReplaceChild("leg2", CubeListBuilder.create()
                .texOffs(0, 46).mirror(false)
                .addBox(1.0F, -12.0F, 5.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(-3.0F, 12.0F, -7.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p11 = p4.addOrReplaceChild("leg3", CubeListBuilder.create()
                .texOffs(17, 30).mirror(false)
                .addBox(-5.0F, -12.0F, -7.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(3.0F, 12.0F, 5.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p12 = p5.addOrReplaceChild("leg4", CubeListBuilder.create()
                .texOffs(0, 30).mirror(false)
                .addBox(1.0F, -12.0F, -7.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(-3.0F, 12.0F, 5.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p13 = p1.addOrReplaceChild("__cem_layer_fur_body", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 19.0F, -2.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p14 = p13.addOrReplaceChild("__cem_layer_fur_rotation", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -19.0F, 2.0F, 1.57079633F, 0.0F, 0.0F));
        PartDefinition p15 = p0.addOrReplaceChild("__cem_layer_fur_head", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 18.0F, 8.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p16 = p2.addOrReplaceChild("__cem_layer_fur_leg1", CubeListBuilder.create(), PartPose.offsetAndRotation(3.0F, 12.0F, -7.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p17 = p3.addOrReplaceChild("__cem_layer_fur_leg2", CubeListBuilder.create(), PartPose.offsetAndRotation(-3.0F, 12.0F, -7.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p18 = p4.addOrReplaceChild("__cem_layer_fur_leg3", CubeListBuilder.create(), PartPose.offsetAndRotation(3.0F, 12.0F, 5.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p19 = p5.addOrReplaceChild("__cem_layer_fur_leg4", CubeListBuilder.create(), PartPose.offsetAndRotation(-3.0F, 12.0F, 5.0F, 0.0F, 0.0F, 0.0F));
        return LayerDefinition.create(mesh, 64, 64);
    }

    /** Preserve source expression units; static scales are reapplied after every pose reset. */
    public static ReferenceMobDefinition.Builder configure(ReferenceMobDefinition.Builder builder) {
        builder.cemDriver("sheep");
        builder.bakedRoot(CemPoisonousGreenSheepGeometry::bakeRoot);
        builder.hostAlias("head", "__cem_host_head");
        builder.hostAlias("body", "__cem_host_body");
        builder.hostAlias("leg1", "__cem_host_leg1");
        builder.hostAlias("leg2", "__cem_host_leg2");
        builder.hostAlias("leg3", "__cem_host_leg3");
        builder.hostAlias("leg4", "__cem_host_leg4");
        builder.expression("rotation.rx", "0");
        builder.expression("__cem_layer_fur_rotation.rx", "0");
        builder.materialLayer("dungeonsalive:textures/entity/reference/cem/poisonous_green_sheep_material_1.png", CemPoisonousGreenSheepGeometry::bakeMaterial1);
        return builder;
    }

    /** Complete matching rig; installed cubes stay owned by the Minecraft client. */
    public static net.minecraft.client.model.geom.ModelPart bakeRoot() {
        var nativeRoot = net.minecraft.client.model.SheepModel.createBodyLayer().bakeRoot();
        var nativeCubes = new java.util.HashMap<String, java.util.List<net.minecraft.client.model.geom.ModelPart.Cube>>();
        nativeRoot.visit(new com.mojang.blaze3d.vertex.PoseStack(),
                (pose, path, index, cube) -> nativeCubes.computeIfAbsent(path, key -> new java.util.ArrayList<>()).add(cube));
        var customCubes = new java.util.HashMap<String, java.util.List<net.minecraft.client.model.geom.ModelPart.Cube>>();
        createBodyLayer().bakeRoot().visit(new com.mojang.blaze3d.vertex.PoseStack(),
                (pose, path, index, cube) -> customCubes.computeIfAbsent(path, key -> new java.util.ArrayList<>()).add(cube));
        var p19Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_leg4/__cem_layer_fur_leg4", java.util.List.of()));
        var p19 = new net.minecraft.client.model.geom.ModelPart(p19Cubes, java.util.Map.ofEntries());
        p19.setInitialPose(PartPose.offsetAndRotation(-3.0F, 12.0F, 5.0F, 0.0F, 0.0F, 0.0F)); p19.resetPose();
        var p18Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_leg3/__cem_layer_fur_leg3", java.util.List.of()));
        var p18 = new net.minecraft.client.model.geom.ModelPart(p18Cubes, java.util.Map.ofEntries());
        p18.setInitialPose(PartPose.offsetAndRotation(3.0F, 12.0F, 5.0F, 0.0F, 0.0F, 0.0F)); p18.resetPose();
        var p17Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_leg2/__cem_layer_fur_leg2", java.util.List.of()));
        var p17 = new net.minecraft.client.model.geom.ModelPart(p17Cubes, java.util.Map.ofEntries());
        p17.setInitialPose(PartPose.offsetAndRotation(-3.0F, 12.0F, -7.0F, 0.0F, 0.0F, 0.0F)); p17.resetPose();
        var p16Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_leg1/__cem_layer_fur_leg1", java.util.List.of()));
        var p16 = new net.minecraft.client.model.geom.ModelPart(p16Cubes, java.util.Map.ofEntries());
        p16.setInitialPose(PartPose.offsetAndRotation(3.0F, 12.0F, -7.0F, 0.0F, 0.0F, 0.0F)); p16.resetPose();
        var p15Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_head/__cem_layer_fur_head", java.util.List.of()));
        var p15 = new net.minecraft.client.model.geom.ModelPart(p15Cubes, java.util.Map.ofEntries());
        p15.setInitialPose(PartPose.offsetAndRotation(0.0F, 18.0F, 8.0F, 0.0F, 0.0F, 0.0F)); p15.resetPose();
        var p14Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/__cem_layer_fur_body/__cem_layer_fur_rotation", java.util.List.of()));
        var p14 = new net.minecraft.client.model.geom.ModelPart(p14Cubes, java.util.Map.ofEntries());
        p14.setInitialPose(PartPose.offsetAndRotation(0.0F, -19.0F, 2.0F, 1.57079633F, 0.0F, 0.0F)); p14.resetPose();
        var p13Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/__cem_layer_fur_body", java.util.List.of()));
        var p13 = new net.minecraft.client.model.geom.ModelPart(p13Cubes, java.util.Map.ofEntries(java.util.Map.entry("__cem_layer_fur_rotation", p14)));
        p13.setInitialPose(PartPose.offsetAndRotation(0.0F, 19.0F, -2.0F, 0.0F, 0.0F, 0.0F)); p13.resetPose();
        var p12Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_leg4/leg4", java.util.List.of()));
        var p12 = new net.minecraft.client.model.geom.ModelPart(p12Cubes, java.util.Map.ofEntries());
        p12.setInitialPose(PartPose.offsetAndRotation(-3.0F, 12.0F, 5.0F, 0.0F, 0.0F, 0.0F)); p12.resetPose();
        var p11Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_leg3/leg3", java.util.List.of()));
        var p11 = new net.minecraft.client.model.geom.ModelPart(p11Cubes, java.util.Map.ofEntries());
        p11.setInitialPose(PartPose.offsetAndRotation(3.0F, 12.0F, 5.0F, 0.0F, 0.0F, 0.0F)); p11.resetPose();
        var p10Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_leg2/leg2", java.util.List.of()));
        var p10 = new net.minecraft.client.model.geom.ModelPart(p10Cubes, java.util.Map.ofEntries());
        p10.setInitialPose(PartPose.offsetAndRotation(-3.0F, 12.0F, -7.0F, 0.0F, 0.0F, 0.0F)); p10.resetPose();
        var p9Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_leg1/leg1", java.util.List.of()));
        var p9 = new net.minecraft.client.model.geom.ModelPart(p9Cubes, java.util.Map.ofEntries());
        p9.setInitialPose(PartPose.offsetAndRotation(3.0F, 12.0F, -7.0F, 0.0F, 0.0F, 0.0F)); p9.resetPose();
        var p8Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_head/head", java.util.List.of()));
        var p8 = new net.minecraft.client.model.geom.ModelPart(p8Cubes, java.util.Map.ofEntries());
        p8.setInitialPose(PartPose.offsetAndRotation(0.0F, 18.0F, 8.0F, 0.0F, 0.0F, 0.0F)); p8.resetPose();
        var p7Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/body/rotation", java.util.List.of()));
        var p7 = new net.minecraft.client.model.geom.ModelPart(p7Cubes, java.util.Map.ofEntries());
        p7.setInitialPose(PartPose.offsetAndRotation(0.0F, -19.0F, 2.0F, 1.57079633F, 0.0F, 0.0F)); p7.resetPose();
        var p6Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/body", java.util.List.of()));
        var p6 = new net.minecraft.client.model.geom.ModelPart(p6Cubes, java.util.Map.ofEntries(java.util.Map.entry("rotation", p7)));
        p6.setInitialPose(PartPose.offsetAndRotation(0.0F, 19.0F, -2.0F, 0.0F, 0.0F, 0.0F)); p6.resetPose();
        var p5Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_leg4", java.util.List.of()));
        var p5 = new net.minecraft.client.model.geom.ModelPart(p5Cubes, java.util.Map.ofEntries(java.util.Map.entry("leg4", p12), java.util.Map.entry("__cem_layer_fur_leg4", p19)));
        p5.setInitialPose(nativeRoot.getChild("left_front_leg").storePose()); p5.resetPose();
        var p4Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_leg3", java.util.List.of()));
        var p4 = new net.minecraft.client.model.geom.ModelPart(p4Cubes, java.util.Map.ofEntries(java.util.Map.entry("leg3", p11), java.util.Map.entry("__cem_layer_fur_leg3", p18)));
        p4.setInitialPose(nativeRoot.getChild("right_front_leg").storePose()); p4.resetPose();
        var p3Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_leg2", java.util.List.of()));
        var p3 = new net.minecraft.client.model.geom.ModelPart(p3Cubes, java.util.Map.ofEntries(java.util.Map.entry("leg2", p10), java.util.Map.entry("__cem_layer_fur_leg2", p17)));
        p3.setInitialPose(nativeRoot.getChild("left_hind_leg").storePose()); p3.resetPose();
        var p2Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_leg1", java.util.List.of()));
        var p2 = new net.minecraft.client.model.geom.ModelPart(p2Cubes, java.util.Map.ofEntries(java.util.Map.entry("leg1", p9), java.util.Map.entry("__cem_layer_fur_leg1", p16)));
        p2.setInitialPose(nativeRoot.getChild("right_hind_leg").storePose()); p2.resetPose();
        var p1Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body", java.util.List.of()));
        var p1 = new net.minecraft.client.model.geom.ModelPart(p1Cubes, java.util.Map.ofEntries(java.util.Map.entry("body", p6), java.util.Map.entry("__cem_layer_fur_body", p13)));
        p1.setInitialPose(nativeRoot.getChild("body").storePose()); p1.resetPose();
        var p0Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_head", java.util.List.of()));
        var p0 = new net.minecraft.client.model.geom.ModelPart(p0Cubes, java.util.Map.ofEntries(java.util.Map.entry("head", p8), java.util.Map.entry("__cem_layer_fur_head", p15)));
        p0.setInitialPose(nativeRoot.getChild("head").storePose()); p0.resetPose();
        return new net.minecraft.client.model.geom.ModelPart(java.util.List.of(), java.util.Map.ofEntries(java.util.Map.entry("__cem_host_head", p0), java.util.Map.entry("__cem_host_body", p1), java.util.Map.entry("__cem_host_leg1", p2), java.util.Map.entry("__cem_host_leg2", p3), java.util.Map.entry("__cem_host_leg3", p4), java.util.Map.entry("__cem_host_leg4", p5)));
    }

    /** Complete matching rig; installed cubes stay owned by the Minecraft client. */
    public static net.minecraft.client.model.geom.ModelPart bakeMaterial1() {
        var nativeRoot = net.minecraft.client.model.SheepModel.createBodyLayer().bakeRoot();
        var p19Cubes = new java.util.ArrayList<net.minecraft.client.model.geom.ModelPart.Cube>();
        p19Cubes.add(new ReferenceMeshCube(0.5F, -12.5F, -7.5F, 5.0F, 7.0F, 5.0F, new float[][][] {
                {{5.5F, -12.5F, -2.5F, 0.125F, 0.34375F}, {0.5F, -12.5F, -2.5F, 0.0625F, 0.34375F}, {0.5F, -12.5F, -7.5F, 0.0625F, 0.40625F}, {5.5F, -12.5F, -7.5F, 0.125F, 0.40625F}},
                {{5.5F, -5.5F, -7.5F, 0.1875F, 0.40625F}, {0.5F, -5.5F, -7.5F, 0.125F, 0.40625F}, {0.5F, -5.5F, -2.5F, 0.125F, 0.34375F}, {5.5F, -5.5F, -2.5F, 0.1875F, 0.34375F}},
                {{0.5F, -12.5F, -7.5F, 0.0625F, 0.40625F}, {0.5F, -12.5F, -2.5F, 0.0F, 0.40625F}, {0.5F, -5.5F, -2.5F, 0.0F, 0.5F}, {0.5F, -5.5F, -7.5F, 0.0625F, 0.5F}},
                {{5.5F, -12.5F, -2.5F, 0.1875F, 0.40625F}, {5.5F, -12.5F, -7.5F, 0.125F, 0.40625F}, {5.5F, -5.5F, -7.5F, 0.125F, 0.5F}, {5.5F, -5.5F, -2.5F, 0.1875F, 0.5F}},
                {{5.5F, -12.5F, -7.5F, 0.125F, 0.40625F}, {0.5F, -12.5F, -7.5F, 0.0625F, 0.40625F}, {0.5F, -5.5F, -7.5F, 0.0625F, 0.5F}, {5.5F, -5.5F, -7.5F, 0.125F, 0.5F}},
                {{0.5F, -12.5F, -2.5F, 0.25F, 0.40625F}, {5.5F, -12.5F, -2.5F, 0.1875F, 0.40625F}, {5.5F, -5.5F, -2.5F, 0.1875F, 0.5F}, {0.5F, -5.5F, -2.5F, 0.25F, 0.5F}}
        }));
        var p19 = new net.minecraft.client.model.geom.ModelPart(p19Cubes, java.util.Map.ofEntries());
        p19.setInitialPose(PartPose.offsetAndRotation(-3.0F, 12.0F, 5.0F, 0.0F, 0.0F, 0.0F)); p19.resetPose();
        var p18Cubes = new java.util.ArrayList<net.minecraft.client.model.geom.ModelPart.Cube>();
        p18Cubes.add(new ReferenceMeshCube(-5.5F, -12.5F, -7.5F, 5.0F, 7.0F, 5.0F, new float[][][] {
                {{-0.5F, -12.5F, -2.5F, 0.390625F, 0.34375F}, {-5.5F, -12.5F, -2.5F, 0.328125F, 0.34375F}, {-5.5F, -12.5F, -7.5F, 0.328125F, 0.40625F}, {-0.5F, -12.5F, -7.5F, 0.390625F, 0.40625F}},
                {{-0.5F, -5.5F, -7.5F, 0.453125F, 0.40625F}, {-5.5F, -5.5F, -7.5F, 0.390625F, 0.40625F}, {-5.5F, -5.5F, -2.5F, 0.390625F, 0.34375F}, {-0.5F, -5.5F, -2.5F, 0.453125F, 0.34375F}},
                {{-5.5F, -12.5F, -7.5F, 0.328125F, 0.40625F}, {-5.5F, -12.5F, -2.5F, 0.265625F, 0.40625F}, {-5.5F, -5.5F, -2.5F, 0.265625F, 0.5F}, {-5.5F, -5.5F, -7.5F, 0.328125F, 0.5F}},
                {{-0.5F, -12.5F, -2.5F, 0.453125F, 0.40625F}, {-0.5F, -12.5F, -7.5F, 0.390625F, 0.40625F}, {-0.5F, -5.5F, -7.5F, 0.390625F, 0.5F}, {-0.5F, -5.5F, -2.5F, 0.453125F, 0.5F}},
                {{-0.5F, -12.5F, -7.5F, 0.390625F, 0.40625F}, {-5.5F, -12.5F, -7.5F, 0.328125F, 0.40625F}, {-5.5F, -5.5F, -7.5F, 0.328125F, 0.5F}, {-0.5F, -5.5F, -7.5F, 0.390625F, 0.5F}},
                {{-5.5F, -12.5F, -2.5F, 0.515625F, 0.40625F}, {-0.5F, -12.5F, -2.5F, 0.453125F, 0.40625F}, {-0.5F, -5.5F, -2.5F, 0.453125F, 0.5F}, {-5.5F, -5.5F, -2.5F, 0.515625F, 0.5F}}
        }));
        var p18 = new net.minecraft.client.model.geom.ModelPart(p18Cubes, java.util.Map.ofEntries());
        p18.setInitialPose(PartPose.offsetAndRotation(3.0F, 12.0F, 5.0F, 0.0F, 0.0F, 0.0F)); p18.resetPose();
        var p17Cubes = new java.util.ArrayList<net.minecraft.client.model.geom.ModelPart.Cube>();
        p17Cubes.add(new ReferenceMeshCube(0.5F, -12.5F, 4.5F, 5.0F, 7.0F, 5.0F, new float[][][] {
                {{5.5F, -12.5F, 9.5F, 0.125F, 0.5F}, {0.5F, -12.5F, 9.5F, 0.0625F, 0.5F}, {0.5F, -12.5F, 4.5F, 0.0625F, 0.5625F}, {5.5F, -12.5F, 4.5F, 0.125F, 0.5625F}},
                {{5.5F, -5.5F, 4.5F, 0.1875F, 0.5625F}, {0.5F, -5.5F, 4.5F, 0.125F, 0.5625F}, {0.5F, -5.5F, 9.5F, 0.125F, 0.5F}, {5.5F, -5.5F, 9.5F, 0.1875F, 0.5F}},
                {{0.5F, -12.5F, 4.5F, 0.0625F, 0.5625F}, {0.5F, -12.5F, 9.5F, 0.0F, 0.5625F}, {0.5F, -5.5F, 9.5F, 0.0F, 0.65625F}, {0.5F, -5.5F, 4.5F, 0.0625F, 0.65625F}},
                {{5.5F, -12.5F, 9.5F, 0.1875F, 0.5625F}, {5.5F, -12.5F, 4.5F, 0.125F, 0.5625F}, {5.5F, -5.5F, 4.5F, 0.125F, 0.65625F}, {5.5F, -5.5F, 9.5F, 0.1875F, 0.65625F}},
                {{5.5F, -12.5F, 4.5F, 0.125F, 0.5625F}, {0.5F, -12.5F, 4.5F, 0.0625F, 0.5625F}, {0.5F, -5.5F, 4.5F, 0.0625F, 0.65625F}, {5.5F, -5.5F, 4.5F, 0.125F, 0.65625F}},
                {{0.5F, -12.5F, 9.5F, 0.25F, 0.5625F}, {5.5F, -12.5F, 9.5F, 0.1875F, 0.5625F}, {5.5F, -5.5F, 9.5F, 0.1875F, 0.65625F}, {0.5F, -5.5F, 9.5F, 0.25F, 0.65625F}}
        }));
        var p17 = new net.minecraft.client.model.geom.ModelPart(p17Cubes, java.util.Map.ofEntries());
        p17.setInitialPose(PartPose.offsetAndRotation(-3.0F, 12.0F, -7.0F, 0.0F, 0.0F, 0.0F)); p17.resetPose();
        var p16Cubes = new java.util.ArrayList<net.minecraft.client.model.geom.ModelPart.Cube>();
        p16Cubes.add(new ReferenceMeshCube(-5.5F, -12.5F, 4.5F, 5.0F, 7.0F, 5.0F, new float[][][] {
                {{-0.5F, -12.5F, 9.5F, 0.390625F, 0.5F}, {-5.5F, -12.5F, 9.5F, 0.328125F, 0.5F}, {-5.5F, -12.5F, 4.5F, 0.328125F, 0.5625F}, {-0.5F, -12.5F, 4.5F, 0.390625F, 0.5625F}},
                {{-0.5F, -5.5F, 4.5F, 0.453125F, 0.5625F}, {-5.5F, -5.5F, 4.5F, 0.390625F, 0.5625F}, {-5.5F, -5.5F, 9.5F, 0.390625F, 0.5F}, {-0.5F, -5.5F, 9.5F, 0.453125F, 0.5F}},
                {{-5.5F, -12.5F, 4.5F, 0.328125F, 0.5625F}, {-5.5F, -12.5F, 9.5F, 0.265625F, 0.5625F}, {-5.5F, -5.5F, 9.5F, 0.265625F, 0.65625F}, {-5.5F, -5.5F, 4.5F, 0.328125F, 0.65625F}},
                {{-0.5F, -12.5F, 9.5F, 0.453125F, 0.5625F}, {-0.5F, -12.5F, 4.5F, 0.390625F, 0.5625F}, {-0.5F, -5.5F, 4.5F, 0.390625F, 0.65625F}, {-0.5F, -5.5F, 9.5F, 0.453125F, 0.65625F}},
                {{-0.5F, -12.5F, 4.5F, 0.390625F, 0.5625F}, {-5.5F, -12.5F, 4.5F, 0.328125F, 0.5625F}, {-5.5F, -5.5F, 4.5F, 0.328125F, 0.65625F}, {-0.5F, -5.5F, 4.5F, 0.390625F, 0.65625F}},
                {{-5.5F, -12.5F, 9.5F, 0.515625F, 0.5625F}, {-0.5F, -12.5F, 9.5F, 0.453125F, 0.5625F}, {-0.5F, -5.5F, 9.5F, 0.453125F, 0.65625F}, {-5.5F, -5.5F, 9.5F, 0.515625F, 0.65625F}}
        }));
        var p16 = new net.minecraft.client.model.geom.ModelPart(p16Cubes, java.util.Map.ofEntries());
        p16.setInitialPose(PartPose.offsetAndRotation(3.0F, 12.0F, -7.0F, 0.0F, 0.0F, 0.0F)); p16.resetPose();
        var p15Cubes = new java.util.ArrayList<net.minecraft.client.model.geom.ModelPart.Cube>();
        p15Cubes.add(new ReferenceMeshCube(-3.6F, -22.6F, -12.6F, 7.2F, 7.2F, 7.2F, new float[][][] {
                {{3.6F, -22.6F, -5.4F, 0.1875F, 0.0F}, {-3.6F, -22.6F, -5.4F, 0.09375F, 0.0F}, {-3.6F, -22.6F, -12.6F, 0.09375F, 0.09375F}, {3.6F, -22.6F, -12.6F, 0.1875F, 0.09375F}},
                {{3.6F, -15.4F, -12.6F, 0.28125F, 0.09375F}, {-3.6F, -15.4F, -12.6F, 0.1875F, 0.09375F}, {-3.6F, -15.4F, -5.4F, 0.1875F, 0.0F}, {3.6F, -15.4F, -5.4F, 0.28125F, 0.0F}},
                {{-3.6F, -22.6F, -12.6F, 0.09375F, 0.09375F}, {-3.6F, -22.6F, -5.4F, 0.0F, 0.09375F}, {-3.6F, -15.4F, -5.4F, 0.0F, 0.1875F}, {-3.6F, -15.4F, -12.6F, 0.09375F, 0.1875F}},
                {{3.6F, -22.6F, -5.4F, 0.28125F, 0.09375F}, {3.6F, -22.6F, -12.6F, 0.1875F, 0.09375F}, {3.6F, -15.4F, -12.6F, 0.1875F, 0.1875F}, {3.6F, -15.4F, -5.4F, 0.28125F, 0.1875F}},
                {{3.6F, -22.6F, -12.6F, 0.1875F, 0.09375F}, {-3.6F, -22.6F, -12.6F, 0.09375F, 0.09375F}, {-3.6F, -15.4F, -12.6F, 0.09375F, 0.1875F}, {3.6F, -15.4F, -12.6F, 0.1875F, 0.1875F}},
                {{-3.6F, -22.6F, -5.4F, 0.375F, 0.09375F}, {3.6F, -22.6F, -5.4F, 0.28125F, 0.09375F}, {3.6F, -15.4F, -5.4F, 0.28125F, 0.1875F}, {-3.6F, -15.4F, -5.4F, 0.375F, 0.1875F}}
        }));
        var p15 = new net.minecraft.client.model.geom.ModelPart(p15Cubes, java.util.Map.ofEntries());
        p15.setInitialPose(PartPose.offsetAndRotation(0.0F, 18.0F, 8.0F, 0.0F, 0.0F, 0.0F)); p15.resetPose();
        var p14Cubes = new java.util.ArrayList<net.minecraft.client.model.geom.ModelPart.Cube>();
        p14Cubes.add(new ReferenceMeshCube(-5.75F, -11.75F, -8.75F, 11.5F, 19.5F, 9.5F, new float[][][] {
                {{5.75F, -11.75F, 0.75F, 0.59375F, 0.0F}, {-5.75F, -11.75F, 0.75F, 0.46875F, 0.0F}, {-5.75F, -11.75F, -8.75F, 0.46875F, 0.09375F}, {5.75F, -11.75F, -8.75F, 0.59375F, 0.09375F}},
                {{5.75F, 7.75F, -8.75F, 0.71875F, 0.09375F}, {-5.75F, 7.75F, -8.75F, 0.59375F, 0.09375F}, {-5.75F, 7.75F, 0.75F, 0.59375F, 0.0F}, {5.75F, 7.75F, 0.75F, 0.71875F, 0.0F}},
                {{-5.75F, -11.75F, -8.75F, 0.46875F, 0.09375F}, {-5.75F, -11.75F, 0.75F, 0.375F, 0.09375F}, {-5.75F, 7.75F, 0.75F, 0.375F, 0.34375F}, {-5.75F, 7.75F, -8.75F, 0.46875F, 0.34375F}},
                {{5.75F, -11.75F, 0.75F, 0.6875F, 0.09375F}, {5.75F, -11.75F, -8.75F, 0.59375F, 0.09375F}, {5.75F, 7.75F, -8.75F, 0.59375F, 0.34375F}, {5.75F, 7.75F, 0.75F, 0.6875F, 0.34375F}},
                {{5.75F, -11.75F, -8.75F, 0.59375F, 0.09375F}, {-5.75F, -11.75F, -8.75F, 0.46875F, 0.09375F}, {-5.75F, 7.75F, -8.75F, 0.46875F, 0.34375F}, {5.75F, 7.75F, -8.75F, 0.59375F, 0.34375F}},
                {{-5.75F, -11.75F, 0.75F, 0.8125F, 0.09375F}, {5.75F, -11.75F, 0.75F, 0.6875F, 0.09375F}, {5.75F, 7.75F, 0.75F, 0.6875F, 0.34375F}, {-5.75F, 7.75F, 0.75F, 0.8125F, 0.34375F}}
        }));
        var p14 = new net.minecraft.client.model.geom.ModelPart(p14Cubes, java.util.Map.ofEntries());
        p14.setInitialPose(PartPose.offsetAndRotation(0.0F, -19.0F, 2.0F, 1.57079633F, 0.0F, 0.0F)); p14.resetPose();
        var p13Cubes = new java.util.ArrayList<net.minecraft.client.model.geom.ModelPart.Cube>();
        var p13 = new net.minecraft.client.model.geom.ModelPart(p13Cubes, java.util.Map.ofEntries(java.util.Map.entry("__cem_layer_fur_rotation", p14)));
        p13.setInitialPose(PartPose.offsetAndRotation(0.0F, 19.0F, -2.0F, 0.0F, 0.0F, 0.0F)); p13.resetPose();
        var p12Cubes = new java.util.ArrayList<net.minecraft.client.model.geom.ModelPart.Cube>();
        var p12 = new net.minecraft.client.model.geom.ModelPart(p12Cubes, java.util.Map.ofEntries());
        p12.setInitialPose(PartPose.offsetAndRotation(-3.0F, 12.0F, 5.0F, 0.0F, 0.0F, 0.0F)); p12.resetPose();
        var p11Cubes = new java.util.ArrayList<net.minecraft.client.model.geom.ModelPart.Cube>();
        var p11 = new net.minecraft.client.model.geom.ModelPart(p11Cubes, java.util.Map.ofEntries());
        p11.setInitialPose(PartPose.offsetAndRotation(3.0F, 12.0F, 5.0F, 0.0F, 0.0F, 0.0F)); p11.resetPose();
        var p10Cubes = new java.util.ArrayList<net.minecraft.client.model.geom.ModelPart.Cube>();
        var p10 = new net.minecraft.client.model.geom.ModelPart(p10Cubes, java.util.Map.ofEntries());
        p10.setInitialPose(PartPose.offsetAndRotation(-3.0F, 12.0F, -7.0F, 0.0F, 0.0F, 0.0F)); p10.resetPose();
        var p9Cubes = new java.util.ArrayList<net.minecraft.client.model.geom.ModelPart.Cube>();
        var p9 = new net.minecraft.client.model.geom.ModelPart(p9Cubes, java.util.Map.ofEntries());
        p9.setInitialPose(PartPose.offsetAndRotation(3.0F, 12.0F, -7.0F, 0.0F, 0.0F, 0.0F)); p9.resetPose();
        var p8Cubes = new java.util.ArrayList<net.minecraft.client.model.geom.ModelPart.Cube>();
        var p8 = new net.minecraft.client.model.geom.ModelPart(p8Cubes, java.util.Map.ofEntries());
        p8.setInitialPose(PartPose.offsetAndRotation(0.0F, 18.0F, 8.0F, 0.0F, 0.0F, 0.0F)); p8.resetPose();
        var p7Cubes = new java.util.ArrayList<net.minecraft.client.model.geom.ModelPart.Cube>();
        var p7 = new net.minecraft.client.model.geom.ModelPart(p7Cubes, java.util.Map.ofEntries());
        p7.setInitialPose(PartPose.offsetAndRotation(0.0F, -19.0F, 2.0F, 1.57079633F, 0.0F, 0.0F)); p7.resetPose();
        var p6Cubes = new java.util.ArrayList<net.minecraft.client.model.geom.ModelPart.Cube>();
        var p6 = new net.minecraft.client.model.geom.ModelPart(p6Cubes, java.util.Map.ofEntries(java.util.Map.entry("rotation", p7)));
        p6.setInitialPose(PartPose.offsetAndRotation(0.0F, 19.0F, -2.0F, 0.0F, 0.0F, 0.0F)); p6.resetPose();
        var p5Cubes = new java.util.ArrayList<net.minecraft.client.model.geom.ModelPart.Cube>();
        var p5 = new net.minecraft.client.model.geom.ModelPart(p5Cubes, java.util.Map.ofEntries(java.util.Map.entry("leg4", p12), java.util.Map.entry("__cem_layer_fur_leg4", p19)));
        p5.setInitialPose(nativeRoot.getChild("left_front_leg").storePose()); p5.resetPose();
        var p4Cubes = new java.util.ArrayList<net.minecraft.client.model.geom.ModelPart.Cube>();
        var p4 = new net.minecraft.client.model.geom.ModelPart(p4Cubes, java.util.Map.ofEntries(java.util.Map.entry("leg3", p11), java.util.Map.entry("__cem_layer_fur_leg3", p18)));
        p4.setInitialPose(nativeRoot.getChild("right_front_leg").storePose()); p4.resetPose();
        var p3Cubes = new java.util.ArrayList<net.minecraft.client.model.geom.ModelPart.Cube>();
        var p3 = new net.minecraft.client.model.geom.ModelPart(p3Cubes, java.util.Map.ofEntries(java.util.Map.entry("leg2", p10), java.util.Map.entry("__cem_layer_fur_leg2", p17)));
        p3.setInitialPose(nativeRoot.getChild("left_hind_leg").storePose()); p3.resetPose();
        var p2Cubes = new java.util.ArrayList<net.minecraft.client.model.geom.ModelPart.Cube>();
        var p2 = new net.minecraft.client.model.geom.ModelPart(p2Cubes, java.util.Map.ofEntries(java.util.Map.entry("leg1", p9), java.util.Map.entry("__cem_layer_fur_leg1", p16)));
        p2.setInitialPose(nativeRoot.getChild("right_hind_leg").storePose()); p2.resetPose();
        var p1Cubes = new java.util.ArrayList<net.minecraft.client.model.geom.ModelPart.Cube>();
        var p1 = new net.minecraft.client.model.geom.ModelPart(p1Cubes, java.util.Map.ofEntries(java.util.Map.entry("body", p6), java.util.Map.entry("__cem_layer_fur_body", p13)));
        p1.setInitialPose(nativeRoot.getChild("body").storePose()); p1.resetPose();
        var p0Cubes = new java.util.ArrayList<net.minecraft.client.model.geom.ModelPart.Cube>();
        var p0 = new net.minecraft.client.model.geom.ModelPart(p0Cubes, java.util.Map.ofEntries(java.util.Map.entry("head", p8), java.util.Map.entry("__cem_layer_fur_head", p15)));
        p0.setInitialPose(nativeRoot.getChild("head").storePose()); p0.resetPose();
        return new net.minecraft.client.model.geom.ModelPart(java.util.List.of(), java.util.Map.ofEntries(java.util.Map.entry("__cem_host_head", p0), java.util.Map.entry("__cem_host_body", p1), java.util.Map.entry("__cem_host_leg1", p2), java.util.Map.entry("__cem_host_leg2", p3), java.util.Map.entry("__cem_host_leg3", p4), java.util.Map.entry("__cem_host_leg4", p5)));
    }
}
