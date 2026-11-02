/*
 * Generated adaptation of ReCrafted Mobs 11.0 by GillsBills and credited contributors.
 * Authored asset license: CC-BY-NC-SA-4.0; https://creativecommons.org/licenses/by-nc-sa/4.0/
 * Source: https://modrinth.com/resourcepack/recrafted-mobs
 * Model: assets/minecraft/optifine/cem/zombie/zombie2.jem
 * Source SHA256: b7b6fe82ee2f85cfa651764045b56a81f334614e0585e7cec32c18efab373abf
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

public final class CemArmoredZombieGeometry {
    private CemArmoredZombieGeometry() {}

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        PartDefinition p0 = root.addOrReplaceChild("__cem_host_head", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p1 = root.addOrReplaceChild("__cem_host_body", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p2 = root.addOrReplaceChild("__cem_host_right_arm", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p3 = root.addOrReplaceChild("__cem_host_left_arm", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p4 = root.addOrReplaceChild("__cem_host_right_leg", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p5 = root.addOrReplaceChild("__cem_host_left_leg", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p6 = root.addOrReplaceChild("__cem_host_headwear", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p7 = p0.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 24.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p8 = p7.addOrReplaceChild("head3", CubeListBuilder.create(), PartPose.offsetAndRotation(8.0F, -25.0F, -8.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p9 = p6.addOrReplaceChild("headwear", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 24.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p10 = p9.addOrReplaceChild("head_armored", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -24.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p11 = p1.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 24.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p12 = p11.addOrReplaceChild("body_armor", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -24.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p13 = p3.addOrReplaceChild("left_arm", CubeListBuilder.create(), PartPose.offsetAndRotation(-5.0F, 22.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p14 = p13.addOrReplaceChild("left_arm_armor", CubeListBuilder.create(), PartPose.offsetAndRotation(5.0F, -22.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p15 = p2.addOrReplaceChild("right_arm", CubeListBuilder.create(), PartPose.offsetAndRotation(5.0F, 22.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p16 = p15.addOrReplaceChild("right_arm_armor", CubeListBuilder.create(), PartPose.offsetAndRotation(-5.0F, -22.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p17 = p5.addOrReplaceChild("left_leg", CubeListBuilder.create(), PartPose.offsetAndRotation(-1.9F, 12.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p18 = p4.addOrReplaceChild("right_leg", CubeListBuilder.create(), PartPose.offsetAndRotation(1.9F, 12.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        return LayerDefinition.create(mesh, 128, 128);
    }

    /** Preserve source expression units; static scales are reapplied after every pose reset. */
    public static ReferenceMobDefinition.Builder configure(ReferenceMobDefinition.Builder builder) {
        builder.cemDriver("zombie");
        builder.bakedRoot(CemArmoredZombieGeometry::bakeRoot);
        builder.hostAlias("head", "__cem_host_head");
        builder.hostAlias("body", "__cem_host_body");
        builder.hostAlias("right_arm", "__cem_host_right_arm");
        builder.hostAlias("left_arm", "__cem_host_left_arm");
        builder.hostAlias("right_leg", "__cem_host_right_leg");
        builder.hostAlias("left_leg", "__cem_host_left_leg");
        builder.hostAlias("headwear", "__cem_host_headwear");
        builder.materialLayer("dungeonsalive:textures/entity/reference/cem/armored_zombie_material_1.png", CemArmoredZombieGeometry::bakeMaterial1);
        return builder;
    }

    /** Complete matching rig; installed cubes stay owned by the Minecraft client. */
    public static net.minecraft.client.model.geom.ModelPart bakeRoot() {
        var nativeRoot = LayerDefinition.create(net.minecraft.client.model.HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F), 64, 64).bakeRoot();
        var nativeCubes = new java.util.HashMap<String, java.util.List<net.minecraft.client.model.geom.ModelPart.Cube>>();
        nativeRoot.visit(new com.mojang.blaze3d.vertex.PoseStack(),
                (pose, path, index, cube) -> nativeCubes.computeIfAbsent(path, key -> new java.util.ArrayList<>()).add(cube));
        var customCubes = new java.util.HashMap<String, java.util.List<net.minecraft.client.model.geom.ModelPart.Cube>>();
        createBodyLayer().bakeRoot().visit(new com.mojang.blaze3d.vertex.PoseStack(),
                (pose, path, index, cube) -> customCubes.computeIfAbsent(path, key -> new java.util.ArrayList<>()).add(cube));
        var p18Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_right_leg/right_leg", java.util.List.of()));
        p18Cubes.add(new ReferenceMeshCube(-4.0F, -12.0F, -2.0F, 4.0F, 12.0F, 4.0F, new float[][][] {
                {{0.0F, -12.0F, 2.0F, 0.125F, 0.25F}, {-4.0F, -12.0F, 2.0F, 0.0625F, 0.25F}, {-4.0F, -12.0F, -2.0F, 0.0625F, 0.3125F}, {0.0F, -12.0F, -2.0F, 0.125F, 0.3125F}},
                {{0.0F, 0.0F, -2.0F, 0.1875F, 0.3125F}, {-4.0F, 0.0F, -2.0F, 0.125F, 0.3125F}, {-4.0F, 0.0F, 2.0F, 0.125F, 0.25F}, {0.0F, 0.0F, 2.0F, 0.1875F, 0.25F}},
                {{-4.0F, -12.0F, -2.0F, 0.0625F, 0.3125F}, {-4.0F, -12.0F, 2.0F, 0.0F, 0.3125F}, {-4.0F, 0.0F, 2.0F, 0.0F, 0.5F}, {-4.0F, 0.0F, -2.0F, 0.0625F, 0.5F}},
                {{0.0F, -12.0F, 2.0F, 0.1875F, 0.3125F}, {0.0F, -12.0F, -2.0F, 0.125F, 0.3125F}, {0.0F, 0.0F, -2.0F, 0.125F, 0.5F}, {0.0F, 0.0F, 2.0F, 0.1875F, 0.5F}},
                {{0.0F, -12.0F, -2.0F, 0.125F, 0.3125F}, {-4.0F, -12.0F, -2.0F, 0.0625F, 0.3125F}, {-4.0F, 0.0F, -2.0F, 0.0625F, 0.5F}, {0.0F, 0.0F, -2.0F, 0.125F, 0.5F}},
                {{-4.0F, -12.0F, 2.0F, 0.25F, 0.3125F}, {0.0F, -12.0F, 2.0F, 0.1875F, 0.3125F}, {0.0F, 0.0F, 2.0F, 0.1875F, 0.5F}, {-4.0F, 0.0F, 2.0F, 0.25F, 0.5F}}
        }));
        p18Cubes.add(new ReferenceMeshCube(-4.25F, -12.25F, -2.25F, 4.5F, 12.5F, 4.5F, new float[][][] {
                {{0.25F, -12.25F, 2.25F, 0.125F, 0.5F}, {-4.25F, -12.25F, 2.25F, 0.0625F, 0.5F}, {-4.25F, -12.25F, -2.25F, 0.0625F, 0.5625F}, {0.25F, -12.25F, -2.25F, 0.125F, 0.5625F}},
                {{0.25F, 0.25F, -2.25F, 0.1875F, 0.5625F}, {-4.25F, 0.25F, -2.25F, 0.125F, 0.5625F}, {-4.25F, 0.25F, 2.25F, 0.125F, 0.5F}, {0.25F, 0.25F, 2.25F, 0.1875F, 0.5F}},
                {{-4.25F, -12.25F, -2.25F, 0.0625F, 0.5625F}, {-4.25F, -12.25F, 2.25F, 0.0F, 0.5625F}, {-4.25F, 0.25F, 2.25F, 0.0F, 0.75F}, {-4.25F, 0.25F, -2.25F, 0.0625F, 0.75F}},
                {{0.25F, -12.25F, 2.25F, 0.1875F, 0.5625F}, {0.25F, -12.25F, -2.25F, 0.125F, 0.5625F}, {0.25F, 0.25F, -2.25F, 0.125F, 0.75F}, {0.25F, 0.25F, 2.25F, 0.1875F, 0.75F}},
                {{0.25F, -12.25F, -2.25F, 0.125F, 0.5625F}, {-4.25F, -12.25F, -2.25F, 0.0625F, 0.5625F}, {-4.25F, 0.25F, -2.25F, 0.0625F, 0.75F}, {0.25F, 0.25F, -2.25F, 0.125F, 0.75F}},
                {{-4.25F, -12.25F, 2.25F, 0.25F, 0.5625F}, {0.25F, -12.25F, 2.25F, 0.1875F, 0.5625F}, {0.25F, 0.25F, 2.25F, 0.1875F, 0.75F}, {-4.25F, 0.25F, 2.25F, 0.25F, 0.75F}}
        }));
        var p18 = new net.minecraft.client.model.geom.ModelPart(p18Cubes, java.util.Map.ofEntries());
        p18.setInitialPose(PartPose.offsetAndRotation(1.9F, 12.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p18.resetPose();
        var p17Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_left_leg/left_leg", java.util.List.of()));
        p17Cubes.add(new ReferenceMeshCube(0.0F, -12.0F, -2.0F, 4.0F, 12.0F, 4.0F, new float[][][] {
                {{0.0F, -12.0F, -2.0F, 0.125F, 0.3125F}, {4.0F, -12.0F, -2.0F, 0.0625F, 0.3125F}, {4.0F, -12.0F, 2.0F, 0.0625F, 0.25F}, {0.0F, -12.0F, 2.0F, 0.125F, 0.25F}},
                {{0.0F, 0.0F, 2.0F, 0.1875F, 0.25F}, {4.0F, 0.0F, 2.0F, 0.125F, 0.25F}, {4.0F, 0.0F, -2.0F, 0.125F, 0.3125F}, {0.0F, 0.0F, -2.0F, 0.1875F, 0.3125F}},
                {{4.0F, 0.0F, -2.0F, 0.0625F, 0.5F}, {4.0F, 0.0F, 2.0F, 0.0F, 0.5F}, {4.0F, -12.0F, 2.0F, 0.0F, 0.3125F}, {4.0F, -12.0F, -2.0F, 0.0625F, 0.3125F}},
                {{0.0F, 0.0F, 2.0F, 0.1875F, 0.5F}, {0.0F, 0.0F, -2.0F, 0.125F, 0.5F}, {0.0F, -12.0F, -2.0F, 0.125F, 0.3125F}, {0.0F, -12.0F, 2.0F, 0.1875F, 0.3125F}},
                {{0.0F, 0.0F, -2.0F, 0.125F, 0.5F}, {4.0F, 0.0F, -2.0F, 0.0625F, 0.5F}, {4.0F, -12.0F, -2.0F, 0.0625F, 0.3125F}, {0.0F, -12.0F, -2.0F, 0.125F, 0.3125F}},
                {{4.0F, 0.0F, 2.0F, 0.25F, 0.5F}, {0.0F, 0.0F, 2.0F, 0.1875F, 0.5F}, {0.0F, -12.0F, 2.0F, 0.1875F, 0.3125F}, {4.0F, -12.0F, 2.0F, 0.25F, 0.3125F}}
        }));
        p17Cubes.add(new ReferenceMeshCube(-0.25F, -12.25F, -2.25F, 4.5F, 12.5F, 4.5F, new float[][][] {
                {{-0.25F, -12.25F, -2.25F, 0.125F, 0.8125F}, {4.25F, -12.25F, -2.25F, 0.0625F, 0.8125F}, {4.25F, -12.25F, 2.25F, 0.0625F, 0.75F}, {-0.25F, -12.25F, 2.25F, 0.125F, 0.75F}},
                {{-0.25F, 0.25F, 2.25F, 0.1875F, 0.75F}, {4.25F, 0.25F, 2.25F, 0.125F, 0.75F}, {4.25F, 0.25F, -2.25F, 0.125F, 0.8125F}, {-0.25F, 0.25F, -2.25F, 0.1875F, 0.8125F}},
                {{4.25F, 0.25F, -2.25F, 0.0625F, 1.0F}, {4.25F, 0.25F, 2.25F, 0.0F, 1.0F}, {4.25F, -12.25F, 2.25F, 0.0F, 0.8125F}, {4.25F, -12.25F, -2.25F, 0.0625F, 0.8125F}},
                {{-0.25F, 0.25F, 2.25F, 0.1875F, 1.0F}, {-0.25F, 0.25F, -2.25F, 0.125F, 1.0F}, {-0.25F, -12.25F, -2.25F, 0.125F, 0.8125F}, {-0.25F, -12.25F, 2.25F, 0.1875F, 0.8125F}},
                {{-0.25F, 0.25F, -2.25F, 0.125F, 1.0F}, {4.25F, 0.25F, -2.25F, 0.0625F, 1.0F}, {4.25F, -12.25F, -2.25F, 0.0625F, 0.8125F}, {-0.25F, -12.25F, -2.25F, 0.125F, 0.8125F}},
                {{4.25F, 0.25F, 2.25F, 0.25F, 1.0F}, {-0.25F, 0.25F, 2.25F, 0.1875F, 1.0F}, {-0.25F, -12.25F, 2.25F, 0.1875F, 0.8125F}, {4.25F, -12.25F, 2.25F, 0.25F, 0.8125F}}
        }));
        var p17 = new net.minecraft.client.model.geom.ModelPart(p17Cubes, java.util.Map.ofEntries());
        p17.setInitialPose(PartPose.offsetAndRotation(-1.9F, 12.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p17.resetPose();
        var p16Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_right_arm/right_arm/right_arm_armor", java.util.List.of()));
        var p16 = new net.minecraft.client.model.geom.ModelPart(p16Cubes, java.util.Map.ofEntries());
        p16.setInitialPose(PartPose.offsetAndRotation(-5.0F, -22.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p16.resetPose();
        var p15Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_right_arm/right_arm", java.util.List.of()));
        p15Cubes.add(new ReferenceMeshCube(-8.0F, -24.0F, -2.0F, 4.0F, 12.0F, 4.0F, new float[][][] {
                {{-4.0F, -24.0F, 2.0F, 0.75F, 0.25F}, {-8.0F, -24.0F, 2.0F, 0.6875F, 0.25F}, {-8.0F, -24.0F, -2.0F, 0.6875F, 0.3125F}, {-4.0F, -24.0F, -2.0F, 0.75F, 0.3125F}},
                {{-4.0F, -12.0F, -2.0F, 0.8125F, 0.3125F}, {-8.0F, -12.0F, -2.0F, 0.75F, 0.3125F}, {-8.0F, -12.0F, 2.0F, 0.75F, 0.25F}, {-4.0F, -12.0F, 2.0F, 0.8125F, 0.25F}},
                {{-8.0F, -24.0F, -2.0F, 0.6875F, 0.3125F}, {-8.0F, -24.0F, 2.0F, 0.625F, 0.3125F}, {-8.0F, -12.0F, 2.0F, 0.625F, 0.5F}, {-8.0F, -12.0F, -2.0F, 0.6875F, 0.5F}},
                {{-4.0F, -24.0F, 2.0F, 0.8125F, 0.3125F}, {-4.0F, -24.0F, -2.0F, 0.75F, 0.3125F}, {-4.0F, -12.0F, -2.0F, 0.75F, 0.5F}, {-4.0F, -12.0F, 2.0F, 0.8125F, 0.5F}},
                {{-4.0F, -24.0F, -2.0F, 0.75F, 0.3125F}, {-8.0F, -24.0F, -2.0F, 0.6875F, 0.3125F}, {-8.0F, -12.0F, -2.0F, 0.6875F, 0.5F}, {-4.0F, -12.0F, -2.0F, 0.75F, 0.5F}},
                {{-8.0F, -24.0F, 2.0F, 0.875F, 0.3125F}, {-4.0F, -24.0F, 2.0F, 0.8125F, 0.3125F}, {-4.0F, -12.0F, 2.0F, 0.8125F, 0.5F}, {-8.0F, -12.0F, 2.0F, 0.875F, 0.5F}}
        }));
        p15Cubes.add(new ReferenceMeshCube(-8.25F, -24.25F, -2.25F, 4.5F, 12.5F, 4.5F, new float[][][] {
                {{-3.75F, -24.25F, 2.25F, 0.875F, 0.75F}, {-8.25F, -24.25F, 2.25F, 0.8125F, 0.75F}, {-8.25F, -24.25F, -2.25F, 0.8125F, 0.8125F}, {-3.75F, -24.25F, -2.25F, 0.875F, 0.8125F}},
                {{-3.75F, -11.75F, -2.25F, 0.9375F, 0.8125F}, {-8.25F, -11.75F, -2.25F, 0.875F, 0.8125F}, {-8.25F, -11.75F, 2.25F, 0.875F, 0.75F}, {-3.75F, -11.75F, 2.25F, 0.9375F, 0.75F}},
                {{-8.25F, -24.25F, -2.25F, 0.8125F, 0.8125F}, {-8.25F, -24.25F, 2.25F, 0.75F, 0.8125F}, {-8.25F, -11.75F, 2.25F, 0.75F, 1.0F}, {-8.25F, -11.75F, -2.25F, 0.8125F, 1.0F}},
                {{-3.75F, -24.25F, 2.25F, 0.9375F, 0.8125F}, {-3.75F, -24.25F, -2.25F, 0.875F, 0.8125F}, {-3.75F, -11.75F, -2.25F, 0.875F, 1.0F}, {-3.75F, -11.75F, 2.25F, 0.9375F, 1.0F}},
                {{-3.75F, -24.25F, -2.25F, 0.875F, 0.8125F}, {-8.25F, -24.25F, -2.25F, 0.8125F, 0.8125F}, {-8.25F, -11.75F, -2.25F, 0.8125F, 1.0F}, {-3.75F, -11.75F, -2.25F, 0.875F, 1.0F}},
                {{-8.25F, -24.25F, 2.25F, 1.0F, 0.8125F}, {-3.75F, -24.25F, 2.25F, 0.9375F, 0.8125F}, {-3.75F, -11.75F, 2.25F, 0.9375F, 1.0F}, {-8.25F, -11.75F, 2.25F, 1.0F, 1.0F}}
        }));
        var p15 = new net.minecraft.client.model.geom.ModelPart(p15Cubes, java.util.Map.ofEntries(java.util.Map.entry("right_arm_armor", p16)));
        p15.setInitialPose(PartPose.offsetAndRotation(5.0F, 22.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p15.resetPose();
        var p14Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_left_arm/left_arm/left_arm_armor", java.util.List.of()));
        var p14 = new net.minecraft.client.model.geom.ModelPart(p14Cubes, java.util.Map.ofEntries());
        p14.setInitialPose(PartPose.offsetAndRotation(5.0F, -22.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p14.resetPose();
        var p13Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_left_arm/left_arm", java.util.List.of()));
        p13Cubes.add(new ReferenceMeshCube(4.0F, -24.0F, -2.0F, 4.0F, 12.0F, 4.0F, new float[][][] {
                {{4.0F, -24.0F, -2.0F, 0.75F, 0.3125F}, {8.0F, -24.0F, -2.0F, 0.6875F, 0.3125F}, {8.0F, -24.0F, 2.0F, 0.6875F, 0.25F}, {4.0F, -24.0F, 2.0F, 0.75F, 0.25F}},
                {{4.0F, -12.0F, 2.0F, 0.8125F, 0.25F}, {8.0F, -12.0F, 2.0F, 0.75F, 0.25F}, {8.0F, -12.0F, -2.0F, 0.75F, 0.3125F}, {4.0F, -12.0F, -2.0F, 0.8125F, 0.3125F}},
                {{8.0F, -12.0F, -2.0F, 0.6875F, 0.5F}, {8.0F, -12.0F, 2.0F, 0.625F, 0.5F}, {8.0F, -24.0F, 2.0F, 0.625F, 0.3125F}, {8.0F, -24.0F, -2.0F, 0.6875F, 0.3125F}},
                {{4.0F, -12.0F, 2.0F, 0.8125F, 0.5F}, {4.0F, -12.0F, -2.0F, 0.75F, 0.5F}, {4.0F, -24.0F, -2.0F, 0.75F, 0.3125F}, {4.0F, -24.0F, 2.0F, 0.8125F, 0.3125F}},
                {{4.0F, -12.0F, -2.0F, 0.75F, 0.5F}, {8.0F, -12.0F, -2.0F, 0.6875F, 0.5F}, {8.0F, -24.0F, -2.0F, 0.6875F, 0.3125F}, {4.0F, -24.0F, -2.0F, 0.75F, 0.3125F}},
                {{8.0F, -12.0F, 2.0F, 0.875F, 0.5F}, {4.0F, -12.0F, 2.0F, 0.8125F, 0.5F}, {4.0F, -24.0F, 2.0F, 0.8125F, 0.3125F}, {8.0F, -24.0F, 2.0F, 0.875F, 0.3125F}}
        }));
        p13Cubes.add(new ReferenceMeshCube(3.75F, -24.25F, -2.25F, 4.5F, 12.5F, 4.5F, new float[][][] {
                {{3.75F, -24.25F, -2.25F, 0.75F, 0.5625F}, {8.25F, -24.25F, -2.25F, 0.6875F, 0.5625F}, {8.25F, -24.25F, 2.25F, 0.6875F, 0.5F}, {3.75F, -24.25F, 2.25F, 0.75F, 0.5F}},
                {{3.75F, -11.75F, 2.25F, 0.8125F, 0.5F}, {8.25F, -11.75F, 2.25F, 0.75F, 0.5F}, {8.25F, -11.75F, -2.25F, 0.75F, 0.5625F}, {3.75F, -11.75F, -2.25F, 0.8125F, 0.5625F}},
                {{8.25F, -11.75F, -2.25F, 0.6875F, 0.75F}, {8.25F, -11.75F, 2.25F, 0.625F, 0.75F}, {8.25F, -24.25F, 2.25F, 0.625F, 0.5625F}, {8.25F, -24.25F, -2.25F, 0.6875F, 0.5625F}},
                {{3.75F, -11.75F, 2.25F, 0.8125F, 0.75F}, {3.75F, -11.75F, -2.25F, 0.75F, 0.75F}, {3.75F, -24.25F, -2.25F, 0.75F, 0.5625F}, {3.75F, -24.25F, 2.25F, 0.8125F, 0.5625F}},
                {{3.75F, -11.75F, -2.25F, 0.75F, 0.75F}, {8.25F, -11.75F, -2.25F, 0.6875F, 0.75F}, {8.25F, -24.25F, -2.25F, 0.6875F, 0.5625F}, {3.75F, -24.25F, -2.25F, 0.75F, 0.5625F}},
                {{8.25F, -11.75F, 2.25F, 0.875F, 0.75F}, {3.75F, -11.75F, 2.25F, 0.8125F, 0.75F}, {3.75F, -24.25F, 2.25F, 0.8125F, 0.5625F}, {8.25F, -24.25F, 2.25F, 0.875F, 0.5625F}}
        }));
        var p13 = new net.minecraft.client.model.geom.ModelPart(p13Cubes, java.util.Map.ofEntries(java.util.Map.entry("left_arm_armor", p14)));
        p13.setInitialPose(PartPose.offsetAndRotation(-5.0F, 22.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p13.resetPose();
        var p12Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/body/body_armor", java.util.List.of()));
        var p12 = new net.minecraft.client.model.geom.ModelPart(p12Cubes, java.util.Map.ofEntries());
        p12.setInitialPose(PartPose.offsetAndRotation(0.0F, -24.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p12.resetPose();
        var p11Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/body", java.util.List.of()));
        p11Cubes.add(new ReferenceMeshCube(-4.25F, -24.25F, -2.25F, 8.5F, 12.5F, 4.5F, new float[][][] {
                {{4.25F, -24.25F, 2.25F, 0.4375F, 0.5F}, {-4.25F, -24.25F, 2.25F, 0.3125F, 0.5F}, {-4.25F, -24.25F, -2.25F, 0.3125F, 0.5625F}, {4.25F, -24.25F, -2.25F, 0.4375F, 0.5625F}},
                {{4.25F, -11.75F, -2.25F, 0.5625F, 0.5625F}, {-4.25F, -11.75F, -2.25F, 0.4375F, 0.5625F}, {-4.25F, -11.75F, 2.25F, 0.4375F, 0.5F}, {4.25F, -11.75F, 2.25F, 0.5625F, 0.5F}},
                {{-4.25F, -24.25F, -2.25F, 0.3125F, 0.5625F}, {-4.25F, -24.25F, 2.25F, 0.25F, 0.5625F}, {-4.25F, -11.75F, 2.25F, 0.25F, 0.75F}, {-4.25F, -11.75F, -2.25F, 0.3125F, 0.75F}},
                {{4.25F, -24.25F, 2.25F, 0.5F, 0.5625F}, {4.25F, -24.25F, -2.25F, 0.4375F, 0.5625F}, {4.25F, -11.75F, -2.25F, 0.4375F, 0.75F}, {4.25F, -11.75F, 2.25F, 0.5F, 0.75F}},
                {{4.25F, -24.25F, -2.25F, 0.4375F, 0.5625F}, {-4.25F, -24.25F, -2.25F, 0.3125F, 0.5625F}, {-4.25F, -11.75F, -2.25F, 0.3125F, 0.75F}, {4.25F, -11.75F, -2.25F, 0.4375F, 0.75F}},
                {{-4.25F, -24.25F, 2.25F, 0.625F, 0.5625F}, {4.25F, -24.25F, 2.25F, 0.5F, 0.5625F}, {4.25F, -11.75F, 2.25F, 0.5F, 0.75F}, {-4.25F, -11.75F, 2.25F, 0.625F, 0.75F}}
        }));
        p11Cubes.add(new ReferenceMeshCube(-4.0F, -24.0F, -2.0F, 8.0F, 12.0F, 4.0F, new float[][][] {
                {{4.0F, -24.0F, 2.0F, 0.4375F, 0.25F}, {-4.0F, -24.0F, 2.0F, 0.3125F, 0.25F}, {-4.0F, -24.0F, -2.0F, 0.3125F, 0.3125F}, {4.0F, -24.0F, -2.0F, 0.4375F, 0.3125F}},
                {{4.0F, -12.0F, -2.0F, 0.5625F, 0.3125F}, {-4.0F, -12.0F, -2.0F, 0.4375F, 0.3125F}, {-4.0F, -12.0F, 2.0F, 0.4375F, 0.25F}, {4.0F, -12.0F, 2.0F, 0.5625F, 0.25F}},
                {{-4.0F, -24.0F, -2.0F, 0.3125F, 0.3125F}, {-4.0F, -24.0F, 2.0F, 0.25F, 0.3125F}, {-4.0F, -12.0F, 2.0F, 0.25F, 0.5F}, {-4.0F, -12.0F, -2.0F, 0.3125F, 0.5F}},
                {{4.0F, -24.0F, 2.0F, 0.5F, 0.3125F}, {4.0F, -24.0F, -2.0F, 0.4375F, 0.3125F}, {4.0F, -12.0F, -2.0F, 0.4375F, 0.5F}, {4.0F, -12.0F, 2.0F, 0.5F, 0.5F}},
                {{4.0F, -24.0F, -2.0F, 0.4375F, 0.3125F}, {-4.0F, -24.0F, -2.0F, 0.3125F, 0.3125F}, {-4.0F, -12.0F, -2.0F, 0.3125F, 0.5F}, {4.0F, -12.0F, -2.0F, 0.4375F, 0.5F}},
                {{-4.0F, -24.0F, 2.0F, 0.625F, 0.3125F}, {4.0F, -24.0F, 2.0F, 0.5F, 0.3125F}, {4.0F, -12.0F, 2.0F, 0.5F, 0.5F}, {-4.0F, -12.0F, 2.0F, 0.625F, 0.5F}}
        }));
        var p11 = new net.minecraft.client.model.geom.ModelPart(p11Cubes, java.util.Map.ofEntries(java.util.Map.entry("body_armor", p12)));
        p11.setInitialPose(PartPose.offsetAndRotation(0.0F, 24.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p11.resetPose();
        var p10Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_headwear/headwear/head_armored", java.util.List.of()));
        var p10 = new net.minecraft.client.model.geom.ModelPart(p10Cubes, java.util.Map.ofEntries());
        p10.setInitialPose(PartPose.offsetAndRotation(0.0F, -24.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p10.resetPose();
        var p9Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_headwear/headwear", java.util.List.of()));
        p9Cubes.add(new ReferenceMeshCube(-4.25F, -32.25F, -4.25F, 8.5F, 8.5F, 8.5F, new float[][][] {
                {{4.25F, -32.25F, 4.25F, 0.75F, 0.0F}, {-4.25F, -32.25F, 4.25F, 0.625F, 0.0F}, {-4.25F, -32.25F, -4.25F, 0.625F, 0.125F}, {4.25F, -32.25F, -4.25F, 0.75F, 0.125F}},
                {{4.25F, -23.75F, -4.25F, 0.875F, 0.125F}, {-4.25F, -23.75F, -4.25F, 0.75F, 0.125F}, {-4.25F, -23.75F, 4.25F, 0.75F, 0.0F}, {4.25F, -23.75F, 4.25F, 0.875F, 0.0F}},
                {{-4.25F, -32.25F, -4.25F, 0.625F, 0.125F}, {-4.25F, -32.25F, 4.25F, 0.5F, 0.125F}, {-4.25F, -23.75F, 4.25F, 0.5F, 0.25F}, {-4.25F, -23.75F, -4.25F, 0.625F, 0.25F}},
                {{4.25F, -32.25F, 4.25F, 0.875F, 0.125F}, {4.25F, -32.25F, -4.25F, 0.75F, 0.125F}, {4.25F, -23.75F, -4.25F, 0.75F, 0.25F}, {4.25F, -23.75F, 4.25F, 0.875F, 0.25F}},
                {{4.25F, -32.25F, -4.25F, 0.75F, 0.125F}, {-4.25F, -32.25F, -4.25F, 0.625F, 0.125F}, {-4.25F, -23.75F, -4.25F, 0.625F, 0.25F}, {4.25F, -23.75F, -4.25F, 0.75F, 0.25F}},
                {{-4.25F, -32.25F, 4.25F, 1.0F, 0.125F}, {4.25F, -32.25F, 4.25F, 0.875F, 0.125F}, {4.25F, -23.75F, 4.25F, 0.875F, 0.25F}, {-4.25F, -23.75F, 4.25F, 1.0F, 0.25F}}
        }));
        var p9 = new net.minecraft.client.model.geom.ModelPart(p9Cubes, java.util.Map.ofEntries(java.util.Map.entry("head_armored", p10)));
        p9.setInitialPose(PartPose.offsetAndRotation(0.0F, 24.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p9.resetPose();
        var p8Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_head/head/head3", java.util.List.of()));
        var p8 = new net.minecraft.client.model.geom.ModelPart(p8Cubes, java.util.Map.ofEntries());
        p8.setInitialPose(PartPose.offsetAndRotation(8.0F, -25.0F, -8.0F, 0.0F, 0.0F, 0.0F)); p8.resetPose();
        var p7Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_head/head", java.util.List.of()));
        p7Cubes.add(new ReferenceMeshCube(-4.0F, -32.0F, -4.0F, 8.0F, 8.0F, 8.0F, new float[][][] {
                {{4.0F, -32.0F, 4.0F, 0.25F, 0.0F}, {-4.0F, -32.0F, 4.0F, 0.125F, 0.0F}, {-4.0F, -32.0F, -4.0F, 0.125F, 0.125F}, {4.0F, -32.0F, -4.0F, 0.25F, 0.125F}},
                {{4.0F, -24.0F, -4.0F, 0.375F, 0.125F}, {-4.0F, -24.0F, -4.0F, 0.25F, 0.125F}, {-4.0F, -24.0F, 4.0F, 0.25F, 0.0F}, {4.0F, -24.0F, 4.0F, 0.375F, 0.0F}},
                {{-4.0F, -32.0F, -4.0F, 0.125F, 0.125F}, {-4.0F, -32.0F, 4.0F, 0.0F, 0.125F}, {-4.0F, -24.0F, 4.0F, 0.0F, 0.25F}, {-4.0F, -24.0F, -4.0F, 0.125F, 0.25F}},
                {{4.0F, -32.0F, 4.0F, 0.375F, 0.125F}, {4.0F, -32.0F, -4.0F, 0.25F, 0.125F}, {4.0F, -24.0F, -4.0F, 0.25F, 0.25F}, {4.0F, -24.0F, 4.0F, 0.375F, 0.25F}},
                {{4.0F, -32.0F, -4.0F, 0.25F, 0.125F}, {-4.0F, -32.0F, -4.0F, 0.125F, 0.125F}, {-4.0F, -24.0F, -4.0F, 0.125F, 0.25F}, {4.0F, -24.0F, -4.0F, 0.25F, 0.25F}},
                {{-4.0F, -32.0F, 4.0F, 0.5F, 0.125F}, {4.0F, -32.0F, 4.0F, 0.375F, 0.125F}, {4.0F, -24.0F, 4.0F, 0.375F, 0.25F}, {-4.0F, -24.0F, 4.0F, 0.5F, 0.25F}}
        }));
        var p7 = new net.minecraft.client.model.geom.ModelPart(p7Cubes, java.util.Map.ofEntries(java.util.Map.entry("head3", p8)));
        p7.setInitialPose(PartPose.offsetAndRotation(0.0F, 24.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p7.resetPose();
        var p6Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_headwear", java.util.List.of()));
        var p6 = new net.minecraft.client.model.geom.ModelPart(p6Cubes, java.util.Map.ofEntries(java.util.Map.entry("headwear", p9)));
        p6.setInitialPose(nativeRoot.getChild("hat").storePose()); p6.resetPose();
        var p5Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_left_leg", java.util.List.of()));
        var p5 = new net.minecraft.client.model.geom.ModelPart(p5Cubes, java.util.Map.ofEntries(java.util.Map.entry("left_leg", p17)));
        p5.setInitialPose(nativeRoot.getChild("left_leg").storePose()); p5.resetPose();
        var p4Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_right_leg", java.util.List.of()));
        var p4 = new net.minecraft.client.model.geom.ModelPart(p4Cubes, java.util.Map.ofEntries(java.util.Map.entry("right_leg", p18)));
        p4.setInitialPose(nativeRoot.getChild("right_leg").storePose()); p4.resetPose();
        var p3Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_left_arm", java.util.List.of()));
        var p3 = new net.minecraft.client.model.geom.ModelPart(p3Cubes, java.util.Map.ofEntries(java.util.Map.entry("left_arm", p13)));
        p3.setInitialPose(nativeRoot.getChild("left_arm").storePose()); p3.resetPose();
        var p2Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_right_arm", java.util.List.of()));
        var p2 = new net.minecraft.client.model.geom.ModelPart(p2Cubes, java.util.Map.ofEntries(java.util.Map.entry("right_arm", p15)));
        p2.setInitialPose(nativeRoot.getChild("right_arm").storePose()); p2.resetPose();
        var p1Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body", java.util.List.of()));
        var p1 = new net.minecraft.client.model.geom.ModelPart(p1Cubes, java.util.Map.ofEntries(java.util.Map.entry("body", p11)));
        p1.setInitialPose(nativeRoot.getChild("body").storePose()); p1.resetPose();
        var p0Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_head", java.util.List.of()));
        var p0 = new net.minecraft.client.model.geom.ModelPart(p0Cubes, java.util.Map.ofEntries(java.util.Map.entry("head", p7)));
        p0.setInitialPose(nativeRoot.getChild("head").storePose()); p0.resetPose();
        return new net.minecraft.client.model.geom.ModelPart(java.util.List.of(), java.util.Map.ofEntries(java.util.Map.entry("__cem_host_head", p0), java.util.Map.entry("__cem_host_body", p1), java.util.Map.entry("__cem_host_right_arm", p2), java.util.Map.entry("__cem_host_left_arm", p3), java.util.Map.entry("__cem_host_right_leg", p4), java.util.Map.entry("__cem_host_left_leg", p5), java.util.Map.entry("__cem_host_headwear", p6)));
    }

    /** Complete matching rig; installed cubes stay owned by the Minecraft client. */
    public static net.minecraft.client.model.geom.ModelPart bakeMaterial1() {
        var nativeRoot = LayerDefinition.create(net.minecraft.client.model.HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F), 64, 64).bakeRoot();
        var p18Cubes = new java.util.ArrayList<net.minecraft.client.model.geom.ModelPart.Cube>();
        var p18 = new net.minecraft.client.model.geom.ModelPart(p18Cubes, java.util.Map.ofEntries());
        p18.setInitialPose(PartPose.offsetAndRotation(1.9F, 12.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p18.resetPose();
        var p17Cubes = new java.util.ArrayList<net.minecraft.client.model.geom.ModelPart.Cube>();
        var p17 = new net.minecraft.client.model.geom.ModelPart(p17Cubes, java.util.Map.ofEntries());
        p17.setInitialPose(PartPose.offsetAndRotation(-1.9F, 12.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p17.resetPose();
        var p16Cubes = new java.util.ArrayList<net.minecraft.client.model.geom.ModelPart.Cube>();
        p16Cubes.add(new ReferenceMeshCube(-3.3F, -2.3F, -2.8F, 4.6F, 10.6F, 5.6F, new float[][][] {
                {{1.3F, -2.3F, 2.8F, 0.5625F, 0.1875F}, {-3.3F, -2.3F, 2.8F, 0.5F, 0.1875F}, {-3.3F, -2.3F, -2.8F, 0.5F, 0.265625F}, {1.3F, -2.3F, -2.8F, 0.5625F, 0.265625F}},
                {{1.3F, 8.3F, -2.8F, 0.625F, 0.265625F}, {-3.3F, 8.3F, -2.8F, 0.5625F, 0.265625F}, {-3.3F, 8.3F, 2.8F, 0.5625F, 0.1875F}, {1.3F, 8.3F, 2.8F, 0.625F, 0.1875F}},
                {{-3.3F, -2.3F, -2.8F, 0.5F, 0.265625F}, {-3.3F, -2.3F, 2.8F, 0.421875F, 0.265625F}, {-3.3F, 8.3F, 2.8F, 0.421875F, 0.421875F}, {-3.3F, 8.3F, -2.8F, 0.5F, 0.421875F}},
                {{1.3F, -2.3F, 2.8F, 0.640625F, 0.265625F}, {1.3F, -2.3F, -2.8F, 0.5625F, 0.265625F}, {1.3F, 8.3F, -2.8F, 0.5625F, 0.421875F}, {1.3F, 8.3F, 2.8F, 0.640625F, 0.421875F}},
                {{1.3F, -2.3F, -2.8F, 0.5625F, 0.265625F}, {-3.3F, -2.3F, -2.8F, 0.5F, 0.265625F}, {-3.3F, 8.3F, -2.8F, 0.5F, 0.421875F}, {1.3F, 8.3F, -2.8F, 0.5625F, 0.421875F}},
                {{-3.3F, -2.3F, 2.8F, 0.703125F, 0.265625F}, {1.3F, -2.3F, 2.8F, 0.640625F, 0.265625F}, {1.3F, 8.3F, 2.8F, 0.640625F, 0.421875F}, {-3.3F, 8.3F, 2.8F, 0.703125F, 0.421875F}}
        }));
        var p16 = new net.minecraft.client.model.geom.ModelPart(p16Cubes, java.util.Map.ofEntries());
        p16.setInitialPose(PartPose.offsetAndRotation(-5.0F, -22.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p16.resetPose();
        var p15Cubes = new java.util.ArrayList<net.minecraft.client.model.geom.ModelPart.Cube>();
        var p15 = new net.minecraft.client.model.geom.ModelPart(p15Cubes, java.util.Map.ofEntries(java.util.Map.entry("right_arm_armor", p16)));
        p15.setInitialPose(PartPose.offsetAndRotation(5.0F, 22.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p15.resetPose();
        var p14Cubes = new java.util.ArrayList<net.minecraft.client.model.geom.ModelPart.Cube>();
        p14Cubes.add(new ReferenceMeshCube(-1.3F, -2.3F, -2.8F, 4.6F, 10.6F, 5.6F, new float[][][] {
                {{-1.3F, -2.3F, -2.8F, 0.5625F, 0.265625F}, {3.3F, -2.3F, -2.8F, 0.5F, 0.265625F}, {3.3F, -2.3F, 2.8F, 0.5F, 0.1875F}, {-1.3F, -2.3F, 2.8F, 0.5625F, 0.1875F}},
                {{-1.3F, 8.3F, 2.8F, 0.625F, 0.1875F}, {3.3F, 8.3F, 2.8F, 0.5625F, 0.1875F}, {3.3F, 8.3F, -2.8F, 0.5625F, 0.265625F}, {-1.3F, 8.3F, -2.8F, 0.625F, 0.265625F}},
                {{3.3F, 8.3F, -2.8F, 0.5F, 0.421875F}, {3.3F, 8.3F, 2.8F, 0.421875F, 0.421875F}, {3.3F, -2.3F, 2.8F, 0.421875F, 0.265625F}, {3.3F, -2.3F, -2.8F, 0.5F, 0.265625F}},
                {{-1.3F, 8.3F, 2.8F, 0.640625F, 0.421875F}, {-1.3F, 8.3F, -2.8F, 0.5625F, 0.421875F}, {-1.3F, -2.3F, -2.8F, 0.5625F, 0.265625F}, {-1.3F, -2.3F, 2.8F, 0.640625F, 0.265625F}},
                {{-1.3F, 8.3F, -2.8F, 0.5625F, 0.421875F}, {3.3F, 8.3F, -2.8F, 0.5F, 0.421875F}, {3.3F, -2.3F, -2.8F, 0.5F, 0.265625F}, {-1.3F, -2.3F, -2.8F, 0.5625F, 0.265625F}},
                {{3.3F, 8.3F, 2.8F, 0.703125F, 0.421875F}, {-1.3F, 8.3F, 2.8F, 0.640625F, 0.421875F}, {-1.3F, -2.3F, 2.8F, 0.640625F, 0.265625F}, {3.3F, -2.3F, 2.8F, 0.703125F, 0.265625F}}
        }));
        var p14 = new net.minecraft.client.model.geom.ModelPart(p14Cubes, java.util.Map.ofEntries());
        p14.setInitialPose(PartPose.offsetAndRotation(5.0F, -22.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p14.resetPose();
        var p13Cubes = new java.util.ArrayList<net.minecraft.client.model.geom.ModelPart.Cube>();
        var p13 = new net.minecraft.client.model.geom.ModelPart(p13Cubes, java.util.Map.ofEntries(java.util.Map.entry("left_arm_armor", p14)));
        p13.setInitialPose(PartPose.offsetAndRotation(-5.0F, 22.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p13.resetPose();
        var p12Cubes = new java.util.ArrayList<net.minecraft.client.model.geom.ModelPart.Cube>();
        p12Cubes.add(new ReferenceMeshCube(-4.3F, -0.3F, -2.3F, 8.6F, 12.6F, 4.6F, new float[][][] {
                {{4.3F, -0.3F, 2.3F, 0.8125F, 0.0F}, {-4.3F, -0.3F, 2.3F, 0.6875F, 0.0F}, {-4.3F, -0.3F, -2.3F, 0.6875F, 0.0625F}, {4.3F, -0.3F, -2.3F, 0.8125F, 0.0625F}},
                {{4.3F, 12.3F, -2.3F, 0.9375F, 0.0625F}, {-4.3F, 12.3F, -2.3F, 0.8125F, 0.0625F}, {-4.3F, 12.3F, 2.3F, 0.8125F, 0.0F}, {4.3F, 12.3F, 2.3F, 0.9375F, 0.0F}},
                {{-4.3F, -0.3F, -2.3F, 0.6875F, 0.0625F}, {-4.3F, -0.3F, 2.3F, 0.625F, 0.0625F}, {-4.3F, 12.3F, 2.3F, 0.625F, 0.25F}, {-4.3F, 12.3F, -2.3F, 0.6875F, 0.25F}},
                {{4.3F, -0.3F, 2.3F, 0.875F, 0.0625F}, {4.3F, -0.3F, -2.3F, 0.8125F, 0.0625F}, {4.3F, 12.3F, -2.3F, 0.8125F, 0.25F}, {4.3F, 12.3F, 2.3F, 0.875F, 0.25F}},
                {{4.3F, -0.3F, -2.3F, 0.8125F, 0.0625F}, {-4.3F, -0.3F, -2.3F, 0.6875F, 0.0625F}, {-4.3F, 12.3F, -2.3F, 0.6875F, 0.25F}, {4.3F, 12.3F, -2.3F, 0.8125F, 0.25F}},
                {{-4.3F, -0.3F, 2.3F, 1.0F, 0.0625F}, {4.3F, -0.3F, 2.3F, 0.875F, 0.0625F}, {4.3F, 12.3F, 2.3F, 0.875F, 0.25F}, {-4.3F, 12.3F, 2.3F, 1.0F, 0.25F}}
        }));
        p12Cubes.add(new ReferenceMeshCube(0.2F, 1.9F, -3.4F, 4.2F, 3.2F, 1.2F, new float[][][] {
                {{4.4F, 1.9F, -2.2F, 0.078125F, 0.25F}, {0.2F, 1.9F, -2.2F, 0.015625F, 0.25F}, {0.2F, 1.9F, -3.4F, 0.015625F, 0.265625F}, {4.4F, 1.9F, -3.4F, 0.078125F, 0.265625F}},
                {{4.4F, 5.1F, -3.4F, 0.140625F, 0.265625F}, {0.2F, 5.1F, -3.4F, 0.078125F, 0.265625F}, {0.2F, 5.1F, -2.2F, 0.078125F, 0.25F}, {4.4F, 5.1F, -2.2F, 0.140625F, 0.25F}},
                {{0.2F, 1.9F, -3.4F, 0.015625F, 0.265625F}, {0.2F, 1.9F, -2.2F, 0.0F, 0.265625F}, {0.2F, 5.1F, -2.2F, 0.0F, 0.3125F}, {0.2F, 5.1F, -3.4F, 0.015625F, 0.3125F}},
                {{4.4F, 1.9F, -2.2F, 0.09375F, 0.265625F}, {4.4F, 1.9F, -3.4F, 0.078125F, 0.265625F}, {4.4F, 5.1F, -3.4F, 0.078125F, 0.3125F}, {4.4F, 5.1F, -2.2F, 0.09375F, 0.3125F}},
                {{4.4F, 1.9F, -3.4F, 0.078125F, 0.265625F}, {0.2F, 1.9F, -3.4F, 0.015625F, 0.265625F}, {0.2F, 5.1F, -3.4F, 0.015625F, 0.3125F}, {4.4F, 5.1F, -3.4F, 0.078125F, 0.3125F}},
                {{0.2F, 1.9F, -2.2F, 0.15625F, 0.265625F}, {4.4F, 1.9F, -2.2F, 0.09375F, 0.265625F}, {4.4F, 5.1F, -2.2F, 0.09375F, 0.3125F}, {0.2F, 5.1F, -2.2F, 0.15625F, 0.3125F}}
        }));
        p12Cubes.add(new ReferenceMeshCube(-4.4F, 1.9F, -3.4F, 4.2F, 3.2F, 1.2F, new float[][][] {
                {{-0.2F, 1.9F, -2.2F, 0.265625F, 0.25F}, {-4.4F, 1.9F, -2.2F, 0.203125F, 0.25F}, {-4.4F, 1.9F, -3.4F, 0.203125F, 0.265625F}, {-0.2F, 1.9F, -3.4F, 0.265625F, 0.265625F}},
                {{-0.2F, 5.1F, -3.4F, 0.328125F, 0.265625F}, {-4.4F, 5.1F, -3.4F, 0.265625F, 0.265625F}, {-4.4F, 5.1F, -2.2F, 0.265625F, 0.25F}, {-0.2F, 5.1F, -2.2F, 0.328125F, 0.25F}},
                {{-4.4F, 1.9F, -3.4F, 0.203125F, 0.265625F}, {-4.4F, 1.9F, -2.2F, 0.1875F, 0.265625F}, {-4.4F, 5.1F, -2.2F, 0.1875F, 0.3125F}, {-4.4F, 5.1F, -3.4F, 0.203125F, 0.3125F}},
                {{-0.2F, 1.9F, -2.2F, 0.28125F, 0.265625F}, {-0.2F, 1.9F, -3.4F, 0.265625F, 0.265625F}, {-0.2F, 5.1F, -3.4F, 0.265625F, 0.3125F}, {-0.2F, 5.1F, -2.2F, 0.28125F, 0.3125F}},
                {{-0.2F, 1.9F, -3.4F, 0.265625F, 0.265625F}, {-4.4F, 1.9F, -3.4F, 0.203125F, 0.265625F}, {-4.4F, 5.1F, -3.4F, 0.203125F, 0.3125F}, {-0.2F, 5.1F, -3.4F, 0.265625F, 0.3125F}},
                {{-4.4F, 1.9F, -2.2F, 0.34375F, 0.265625F}, {-0.2F, 1.9F, -2.2F, 0.28125F, 0.265625F}, {-0.2F, 5.1F, -2.2F, 0.28125F, 0.3125F}, {-4.4F, 5.1F, -2.2F, 0.34375F, 0.3125F}}
        }));
        var p12 = new net.minecraft.client.model.geom.ModelPart(p12Cubes, java.util.Map.ofEntries());
        p12.setInitialPose(PartPose.offsetAndRotation(0.0F, -24.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p12.resetPose();
        var p11Cubes = new java.util.ArrayList<net.minecraft.client.model.geom.ModelPart.Cube>();
        var p11 = new net.minecraft.client.model.geom.ModelPart(p11Cubes, java.util.Map.ofEntries(java.util.Map.entry("body_armor", p12)));
        p11.setInitialPose(PartPose.offsetAndRotation(0.0F, 24.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p11.resetPose();
        var p10Cubes = new java.util.ArrayList<net.minecraft.client.model.geom.ModelPart.Cube>();
        p10Cubes.add(new ReferenceMeshCube(-4.3F, -8.3F, -4.3F, 8.6F, 8.6F, 8.6F, new float[][][] {
                {{4.3F, -8.3F, 4.3F, 0.25F, 0.0F}, {-4.3F, -8.3F, 4.3F, 0.125F, 0.0F}, {-4.3F, -8.3F, -4.3F, 0.125F, 0.125F}, {4.3F, -8.3F, -4.3F, 0.25F, 0.125F}},
                {{4.3F, 0.3F, -4.3F, 0.375F, 0.125F}, {-4.3F, 0.3F, -4.3F, 0.25F, 0.125F}, {-4.3F, 0.3F, 4.3F, 0.25F, 0.0F}, {4.3F, 0.3F, 4.3F, 0.375F, 0.0F}},
                {{-4.3F, -8.3F, -4.3F, 0.125F, 0.125F}, {-4.3F, -8.3F, 4.3F, 0.0F, 0.125F}, {-4.3F, 0.3F, 4.3F, 0.0F, 0.25F}, {-4.3F, 0.3F, -4.3F, 0.125F, 0.25F}},
                {{4.3F, -8.3F, 4.3F, 0.375F, 0.125F}, {4.3F, -8.3F, -4.3F, 0.25F, 0.125F}, {4.3F, 0.3F, -4.3F, 0.25F, 0.25F}, {4.3F, 0.3F, 4.3F, 0.375F, 0.25F}},
                {{4.3F, -8.3F, -4.3F, 0.25F, 0.125F}, {-4.3F, -8.3F, -4.3F, 0.125F, 0.125F}, {-4.3F, 0.3F, -4.3F, 0.125F, 0.25F}, {4.3F, 0.3F, -4.3F, 0.25F, 0.25F}},
                {{-4.3F, -8.3F, 4.3F, 0.5F, 0.125F}, {4.3F, -8.3F, 4.3F, 0.375F, 0.125F}, {4.3F, 0.3F, 4.3F, 0.375F, 0.25F}, {-4.3F, 0.3F, 4.3F, 0.5F, 0.25F}}
        }));
        var p10 = new net.minecraft.client.model.geom.ModelPart(p10Cubes, java.util.Map.ofEntries());
        p10.setInitialPose(PartPose.offsetAndRotation(0.0F, -24.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p10.resetPose();
        var p9Cubes = new java.util.ArrayList<net.minecraft.client.model.geom.ModelPart.Cube>();
        var p9 = new net.minecraft.client.model.geom.ModelPart(p9Cubes, java.util.Map.ofEntries(java.util.Map.entry("head_armored", p10)));
        p9.setInitialPose(PartPose.offsetAndRotation(0.0F, 24.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p9.resetPose();
        var p8Cubes = new java.util.ArrayList<net.minecraft.client.model.geom.ModelPart.Cube>();
        var p8 = new net.minecraft.client.model.geom.ModelPart(p8Cubes, java.util.Map.ofEntries());
        p8.setInitialPose(PartPose.offsetAndRotation(8.0F, -25.0F, -8.0F, 0.0F, 0.0F, 0.0F)); p8.resetPose();
        var p7Cubes = new java.util.ArrayList<net.minecraft.client.model.geom.ModelPart.Cube>();
        var p7 = new net.minecraft.client.model.geom.ModelPart(p7Cubes, java.util.Map.ofEntries(java.util.Map.entry("head3", p8)));
        p7.setInitialPose(PartPose.offsetAndRotation(0.0F, 24.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p7.resetPose();
        var p6Cubes = new java.util.ArrayList<net.minecraft.client.model.geom.ModelPart.Cube>();
        var p6 = new net.minecraft.client.model.geom.ModelPart(p6Cubes, java.util.Map.ofEntries(java.util.Map.entry("headwear", p9)));
        p6.setInitialPose(nativeRoot.getChild("hat").storePose()); p6.resetPose();
        var p5Cubes = new java.util.ArrayList<net.minecraft.client.model.geom.ModelPart.Cube>();
        var p5 = new net.minecraft.client.model.geom.ModelPart(p5Cubes, java.util.Map.ofEntries(java.util.Map.entry("left_leg", p17)));
        p5.setInitialPose(nativeRoot.getChild("left_leg").storePose()); p5.resetPose();
        var p4Cubes = new java.util.ArrayList<net.minecraft.client.model.geom.ModelPart.Cube>();
        var p4 = new net.minecraft.client.model.geom.ModelPart(p4Cubes, java.util.Map.ofEntries(java.util.Map.entry("right_leg", p18)));
        p4.setInitialPose(nativeRoot.getChild("right_leg").storePose()); p4.resetPose();
        var p3Cubes = new java.util.ArrayList<net.minecraft.client.model.geom.ModelPart.Cube>();
        var p3 = new net.minecraft.client.model.geom.ModelPart(p3Cubes, java.util.Map.ofEntries(java.util.Map.entry("left_arm", p13)));
        p3.setInitialPose(nativeRoot.getChild("left_arm").storePose()); p3.resetPose();
        var p2Cubes = new java.util.ArrayList<net.minecraft.client.model.geom.ModelPart.Cube>();
        var p2 = new net.minecraft.client.model.geom.ModelPart(p2Cubes, java.util.Map.ofEntries(java.util.Map.entry("right_arm", p15)));
        p2.setInitialPose(nativeRoot.getChild("right_arm").storePose()); p2.resetPose();
        var p1Cubes = new java.util.ArrayList<net.minecraft.client.model.geom.ModelPart.Cube>();
        var p1 = new net.minecraft.client.model.geom.ModelPart(p1Cubes, java.util.Map.ofEntries(java.util.Map.entry("body", p11)));
        p1.setInitialPose(nativeRoot.getChild("body").storePose()); p1.resetPose();
        var p0Cubes = new java.util.ArrayList<net.minecraft.client.model.geom.ModelPart.Cube>();
        var p0 = new net.minecraft.client.model.geom.ModelPart(p0Cubes, java.util.Map.ofEntries(java.util.Map.entry("head", p7)));
        p0.setInitialPose(nativeRoot.getChild("head").storePose()); p0.resetPose();
        return new net.minecraft.client.model.geom.ModelPart(java.util.List.of(), java.util.Map.ofEntries(java.util.Map.entry("__cem_host_head", p0), java.util.Map.entry("__cem_host_body", p1), java.util.Map.entry("__cem_host_right_arm", p2), java.util.Map.entry("__cem_host_left_arm", p3), java.util.Map.entry("__cem_host_right_leg", p4), java.util.Map.entry("__cem_host_left_leg", p5), java.util.Map.entry("__cem_host_headwear", p6)));
    }
}
