/*
 * Generated adaptation of ReCrafted Mobs 11.0 by GillsBills and credited contributors.
 * Authored asset license: CC-BY-NC-SA-4.0; https://creativecommons.org/licenses/by-nc-sa/4.0/
 * Source: https://modrinth.com/resourcepack/recrafted-mobs
 * Model: assets/minecraft/optifine/cem/pig/pig3.jem
 * Source SHA256: a8627c76fde657c331e55d47b71403f71a8698c725453abf7f2ef5e3a46e86d4
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

public final class CemPiggyBankGeometry {
    private CemPiggyBankGeometry() {}

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        PartDefinition p0 = root.addOrReplaceChild("__cem_host_head", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p1 = root.addOrReplaceChild("__cem_host_body", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p2 = root.addOrReplaceChild("__cem_host_leg1", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p3 = root.addOrReplaceChild("__cem_host_leg2", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p4 = root.addOrReplaceChild("__cem_host_leg3", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p5 = root.addOrReplaceChild("__cem_host_leg4", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p6 = p1.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 13.0F, -2.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p7 = p6.addOrReplaceChild("rotation", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -13.0F, 2.0F, 1.57079633F, 0.0F, 0.0F));
        PartDefinition p8 = p7.addOrReplaceChild("body_sub_1", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p9 = p8.addOrReplaceChild("chest", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -1.0F, 1.0F, -1.57079633F, 0.0F, 0.0F));
        PartDefinition p10 = p9.addOrReplaceChild("topchest", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -10.0F, 7.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p11 = p0.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 12.0F, 6.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p12 = p2.addOrReplaceChild("leg1", CubeListBuilder.create(), PartPose.offsetAndRotation(-3.0F, 6.0F, -7.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p13 = p3.addOrReplaceChild("leg2", CubeListBuilder.create(), PartPose.offsetAndRotation(3.0F, 6.0F, -7.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p14 = p4.addOrReplaceChild("leg3", CubeListBuilder.create(), PartPose.offsetAndRotation(-3.0F, 6.0F, 5.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p15 = p5.addOrReplaceChild("leg4", CubeListBuilder.create(), PartPose.offsetAndRotation(3.0F, 6.0F, 5.0F, 0.0F, 0.0F, 0.0F));
        return LayerDefinition.create(mesh, 68, 68);
    }

    /** Preserve source expression units; static scales are reapplied after every pose reset. */
    public static ReferenceMobDefinition.Builder configure(ReferenceMobDefinition.Builder builder) {
        builder.cemDriver("pig");
        builder.bakedRoot(CemPiggyBankGeometry::bakeRoot);
        builder.hostAlias("head", "__cem_host_head");
        builder.hostAlias("body", "__cem_host_body");
        builder.hostAlias("leg1", "__cem_host_leg1");
        builder.hostAlias("leg2", "__cem_host_leg2");
        builder.hostAlias("leg3", "__cem_host_leg3");
        builder.hostAlias("leg4", "__cem_host_leg4");
        builder.expression("rotation.rx", "0");
        builder.expression("chest.sx", "if(is_ridden, 0, 1)");
        builder.expression("chest.sy", "if(is_ridden, 0, 1)");
        builder.expression("chest.sz", "if(is_ridden, 0, 1)");
        return builder;
    }

    /** Complete matching rig; installed cubes stay owned by the Minecraft client. */
    public static net.minecraft.client.model.geom.ModelPart bakeRoot() {
        var nativeRoot = net.minecraft.client.model.PigModel.createBodyLayer(CubeDeformation.NONE).bakeRoot();
        var nativeCubes = new java.util.HashMap<String, java.util.List<net.minecraft.client.model.geom.ModelPart.Cube>>();
        nativeRoot.visit(new com.mojang.blaze3d.vertex.PoseStack(),
                (pose, path, index, cube) -> nativeCubes.computeIfAbsent(path, key -> new java.util.ArrayList<>()).add(cube));
        var customCubes = new java.util.HashMap<String, java.util.List<net.minecraft.client.model.geom.ModelPart.Cube>>();
        createBodyLayer().bakeRoot().visit(new com.mojang.blaze3d.vertex.PoseStack(),
                (pose, path, index, cube) -> customCubes.computeIfAbsent(path, key -> new java.util.ArrayList<>()).add(cube));
        var p15Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_leg4/leg4", java.util.List.of()));
        p15Cubes.add(new ReferenceMeshCube(-5.0F, -6.0F, -7.0F, 4.0F, 6.0F, 4.0F, new float[][][] {
                {{-1.0F, -6.0F, -3.0F, 0.046875F, 0.234375F}, {-5.0F, -6.0F, -3.0F, 0.03125F, 0.234375F}, {-5.0F, -6.0F, -7.0F, 0.03125F, 0.265625F}, {-1.0F, -6.0F, -7.0F, 0.046875F, 0.265625F}},
                {{-1.0F, 0.0F, -7.0F, 0.0625F, 0.265625F}, {-5.0F, 0.0F, -7.0F, 0.046875F, 0.265625F}, {-5.0F, 0.0F, -3.0F, 0.046875F, 0.234375F}, {-1.0F, 0.0F, -3.0F, 0.0625F, 0.234375F}},
                {{-5.0F, -6.0F, -7.0F, 0.03125F, 0.265625F}, {-5.0F, -6.0F, -3.0F, 0.015625F, 0.265625F}, {-5.0F, 0.0F, -3.0F, 0.015625F, 0.3125F}, {-5.0F, 0.0F, -7.0F, 0.03125F, 0.3125F}},
                {{-1.0F, -6.0F, -3.0F, 0.0625F, 0.265625F}, {-1.0F, -6.0F, -7.0F, 0.046875F, 0.265625F}, {-1.0F, 0.0F, -7.0F, 0.046875F, 0.3125F}, {-1.0F, 0.0F, -3.0F, 0.0625F, 0.3125F}},
                {{-1.0F, -6.0F, -7.0F, 0.046875F, 0.265625F}, {-5.0F, -6.0F, -7.0F, 0.03125F, 0.265625F}, {-5.0F, 0.0F, -7.0F, 0.03125F, 0.3125F}, {-1.0F, 0.0F, -7.0F, 0.046875F, 0.3125F}},
                {{-5.0F, -6.0F, -3.0F, 0.078125F, 0.265625F}, {-1.0F, -6.0F, -3.0F, 0.0625F, 0.265625F}, {-1.0F, 0.0F, -3.0F, 0.0625F, 0.3125F}, {-5.0F, 0.0F, -3.0F, 0.078125F, 0.3125F}}
        }));
        var p15 = new net.minecraft.client.model.geom.ModelPart(p15Cubes, java.util.Map.ofEntries());
        p15.setInitialPose(PartPose.offsetAndRotation(3.0F, 6.0F, 5.0F, 0.0F, 0.0F, 0.0F)); p15.resetPose();
        var p14Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_leg3/leg3", java.util.List.of()));
        p14Cubes.add(new ReferenceMeshCube(1.0F, -6.0F, -7.0F, 4.0F, 6.0F, 4.0F, new float[][][] {
                {{5.0F, -6.0F, -3.0F, 0.046875F, 0.15625F}, {1.0F, -6.0F, -3.0F, 0.03125F, 0.15625F}, {1.0F, -6.0F, -7.0F, 0.03125F, 0.1875F}, {5.0F, -6.0F, -7.0F, 0.046875F, 0.1875F}},
                {{5.0F, 0.0F, -7.0F, 0.0625F, 0.1875F}, {1.0F, 0.0F, -7.0F, 0.046875F, 0.1875F}, {1.0F, 0.0F, -3.0F, 0.046875F, 0.15625F}, {5.0F, 0.0F, -3.0F, 0.0625F, 0.15625F}},
                {{1.0F, -6.0F, -7.0F, 0.03125F, 0.1875F}, {1.0F, -6.0F, -3.0F, 0.015625F, 0.1875F}, {1.0F, 0.0F, -3.0F, 0.015625F, 0.234375F}, {1.0F, 0.0F, -7.0F, 0.03125F, 0.234375F}},
                {{5.0F, -6.0F, -3.0F, 0.0625F, 0.1875F}, {5.0F, -6.0F, -7.0F, 0.046875F, 0.1875F}, {5.0F, 0.0F, -7.0F, 0.046875F, 0.234375F}, {5.0F, 0.0F, -3.0F, 0.0625F, 0.234375F}},
                {{5.0F, -6.0F, -7.0F, 0.046875F, 0.1875F}, {1.0F, -6.0F, -7.0F, 0.03125F, 0.1875F}, {1.0F, 0.0F, -7.0F, 0.03125F, 0.234375F}, {5.0F, 0.0F, -7.0F, 0.046875F, 0.234375F}},
                {{1.0F, -6.0F, -3.0F, 0.078125F, 0.1875F}, {5.0F, -6.0F, -3.0F, 0.0625F, 0.1875F}, {5.0F, 0.0F, -3.0F, 0.0625F, 0.234375F}, {1.0F, 0.0F, -3.0F, 0.078125F, 0.234375F}}
        }));
        var p14 = new net.minecraft.client.model.geom.ModelPart(p14Cubes, java.util.Map.ofEntries());
        p14.setInitialPose(PartPose.offsetAndRotation(-3.0F, 6.0F, 5.0F, 0.0F, 0.0F, 0.0F)); p14.resetPose();
        var p13Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_leg2/leg2", java.util.List.of()));
        p13Cubes.add(new ReferenceMeshCube(-5.0F, -6.0F, 5.0F, 4.0F, 6.0F, 4.0F, new float[][][] {
                {{-1.0F, -6.0F, 9.0F, 0.046875F, 0.390625F}, {-5.0F, -6.0F, 9.0F, 0.03125F, 0.390625F}, {-5.0F, -6.0F, 5.0F, 0.03125F, 0.421875F}, {-1.0F, -6.0F, 5.0F, 0.046875F, 0.421875F}},
                {{-1.0F, 0.0F, 5.0F, 0.0625F, 0.421875F}, {-5.0F, 0.0F, 5.0F, 0.046875F, 0.421875F}, {-5.0F, 0.0F, 9.0F, 0.046875F, 0.390625F}, {-1.0F, 0.0F, 9.0F, 0.0625F, 0.390625F}},
                {{-5.0F, -6.0F, 5.0F, 0.03125F, 0.421875F}, {-5.0F, -6.0F, 9.0F, 0.015625F, 0.421875F}, {-5.0F, 0.0F, 9.0F, 0.015625F, 0.46875F}, {-5.0F, 0.0F, 5.0F, 0.03125F, 0.46875F}},
                {{-1.0F, -6.0F, 9.0F, 0.0625F, 0.421875F}, {-1.0F, -6.0F, 5.0F, 0.046875F, 0.421875F}, {-1.0F, 0.0F, 5.0F, 0.046875F, 0.46875F}, {-1.0F, 0.0F, 9.0F, 0.0625F, 0.46875F}},
                {{-1.0F, -6.0F, 5.0F, 0.046875F, 0.421875F}, {-5.0F, -6.0F, 5.0F, 0.03125F, 0.421875F}, {-5.0F, 0.0F, 5.0F, 0.03125F, 0.46875F}, {-1.0F, 0.0F, 5.0F, 0.046875F, 0.46875F}},
                {{-5.0F, -6.0F, 9.0F, 0.078125F, 0.421875F}, {-1.0F, -6.0F, 9.0F, 0.0625F, 0.421875F}, {-1.0F, 0.0F, 9.0F, 0.0625F, 0.46875F}, {-5.0F, 0.0F, 9.0F, 0.078125F, 0.46875F}}
        }));
        var p13 = new net.minecraft.client.model.geom.ModelPart(p13Cubes, java.util.Map.ofEntries());
        p13.setInitialPose(PartPose.offsetAndRotation(3.0F, 6.0F, -7.0F, 0.0F, 0.0F, 0.0F)); p13.resetPose();
        var p12Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_leg1/leg1", java.util.List.of()));
        p12Cubes.add(new ReferenceMeshCube(1.0F, -6.0F, 5.0F, 4.0F, 6.0F, 4.0F, new float[][][] {
                {{5.0F, -6.0F, 9.0F, 0.046875F, 0.3125F}, {1.0F, -6.0F, 9.0F, 0.03125F, 0.3125F}, {1.0F, -6.0F, 5.0F, 0.03125F, 0.34375F}, {5.0F, -6.0F, 5.0F, 0.046875F, 0.34375F}},
                {{5.0F, 0.0F, 5.0F, 0.0625F, 0.34375F}, {1.0F, 0.0F, 5.0F, 0.046875F, 0.34375F}, {1.0F, 0.0F, 9.0F, 0.046875F, 0.3125F}, {5.0F, 0.0F, 9.0F, 0.0625F, 0.3125F}},
                {{1.0F, -6.0F, 5.0F, 0.03125F, 0.34375F}, {1.0F, -6.0F, 9.0F, 0.015625F, 0.34375F}, {1.0F, 0.0F, 9.0F, 0.015625F, 0.390625F}, {1.0F, 0.0F, 5.0F, 0.03125F, 0.390625F}},
                {{5.0F, -6.0F, 9.0F, 0.0625F, 0.34375F}, {5.0F, -6.0F, 5.0F, 0.046875F, 0.34375F}, {5.0F, 0.0F, 5.0F, 0.046875F, 0.390625F}, {5.0F, 0.0F, 9.0F, 0.0625F, 0.390625F}},
                {{5.0F, -6.0F, 5.0F, 0.046875F, 0.34375F}, {1.0F, -6.0F, 5.0F, 0.03125F, 0.34375F}, {1.0F, 0.0F, 5.0F, 0.03125F, 0.390625F}, {5.0F, 0.0F, 5.0F, 0.046875F, 0.390625F}},
                {{1.0F, -6.0F, 9.0F, 0.078125F, 0.34375F}, {5.0F, -6.0F, 9.0F, 0.0625F, 0.34375F}, {5.0F, 0.0F, 9.0F, 0.0625F, 0.390625F}, {1.0F, 0.0F, 9.0F, 0.078125F, 0.390625F}}
        }));
        var p12 = new net.minecraft.client.model.geom.ModelPart(p12Cubes, java.util.Map.ofEntries());
        p12.setInitialPose(PartPose.offsetAndRotation(-3.0F, 6.0F, -7.0F, 0.0F, 0.0F, 0.0F)); p12.resetPose();
        var p11Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_head/head", java.util.List.of()));
        p11Cubes.add(new ReferenceMeshCube(-4.0F, -16.0F, -14.0F, 8.0F, 8.0F, 8.0F, new float[][][] {
                {{4.0F, -16.0F, -6.0F, 0.078125F, 0.03125F}, {-4.0F, -16.0F, -6.0F, 0.046875F, 0.03125F}, {-4.0F, -16.0F, -14.0F, 0.046875F, 0.09375F}, {4.0F, -16.0F, -14.0F, 0.078125F, 0.09375F}},
                {{4.0F, -8.0F, -14.0F, 0.109375F, 0.09375F}, {-4.0F, -8.0F, -14.0F, 0.078125F, 0.09375F}, {-4.0F, -8.0F, -6.0F, 0.078125F, 0.03125F}, {4.0F, -8.0F, -6.0F, 0.109375F, 0.03125F}},
                {{-4.0F, -16.0F, -14.0F, 0.046875F, 0.09375F}, {-4.0F, -16.0F, -6.0F, 0.015625F, 0.09375F}, {-4.0F, -8.0F, -6.0F, 0.015625F, 0.15625F}, {-4.0F, -8.0F, -14.0F, 0.046875F, 0.15625F}},
                {{4.0F, -16.0F, -6.0F, 0.109375F, 0.09375F}, {4.0F, -16.0F, -14.0F, 0.078125F, 0.09375F}, {4.0F, -8.0F, -14.0F, 0.078125F, 0.15625F}, {4.0F, -8.0F, -6.0F, 0.109375F, 0.15625F}},
                {{4.0F, -16.0F, -14.0F, 0.078125F, 0.09375F}, {-4.0F, -16.0F, -14.0F, 0.046875F, 0.09375F}, {-4.0F, -8.0F, -14.0F, 0.046875F, 0.15625F}, {4.0F, -8.0F, -14.0F, 0.078125F, 0.15625F}},
                {{-4.0F, -16.0F, -6.0F, 0.140625F, 0.09375F}, {4.0F, -16.0F, -6.0F, 0.109375F, 0.09375F}, {4.0F, -8.0F, -6.0F, 0.109375F, 0.15625F}, {-4.0F, -8.0F, -6.0F, 0.140625F, 0.15625F}}
        }));
        p11Cubes.add(new ReferenceMeshCube(-2.0F, -12.0F, -15.0F, 4.0F, 3.0F, 1.0F, new float[][][] {
                {{2.0F, -12.0F, -14.0F, 0.10546875F, 0.15625F}, {-2.0F, -12.0F, -14.0F, 0.08984375F, 0.15625F}, {-2.0F, -12.0F, -15.0F, 0.08984375F, 0.1640625F}, {2.0F, -12.0F, -15.0F, 0.10546875F, 0.1640625F}},
                {{2.0F, -9.0F, -15.0F, 0.12109375F, 0.1640625F}, {-2.0F, -9.0F, -15.0F, 0.10546875F, 0.1640625F}, {-2.0F, -9.0F, -14.0F, 0.10546875F, 0.15625F}, {2.0F, -9.0F, -14.0F, 0.12109375F, 0.15625F}},
                {{-2.0F, -12.0F, -15.0F, 0.08984375F, 0.1640625F}, {-2.0F, -12.0F, -14.0F, 0.0859375F, 0.1640625F}, {-2.0F, -9.0F, -14.0F, 0.0859375F, 0.1875F}, {-2.0F, -9.0F, -15.0F, 0.08984375F, 0.1875F}},
                {{2.0F, -12.0F, -14.0F, 0.109375F, 0.1640625F}, {2.0F, -12.0F, -15.0F, 0.10546875F, 0.1640625F}, {2.0F, -9.0F, -15.0F, 0.10546875F, 0.1875F}, {2.0F, -9.0F, -14.0F, 0.109375F, 0.1875F}},
                {{2.0F, -12.0F, -15.0F, 0.10546875F, 0.1640625F}, {-2.0F, -12.0F, -15.0F, 0.08984375F, 0.1640625F}, {-2.0F, -9.0F, -15.0F, 0.08984375F, 0.1875F}, {2.0F, -9.0F, -15.0F, 0.10546875F, 0.1875F}},
                {{-2.0F, -12.0F, -14.0F, 0.125F, 0.1640625F}, {2.0F, -12.0F, -14.0F, 0.109375F, 0.1640625F}, {2.0F, -9.0F, -14.0F, 0.109375F, 0.1875F}, {-2.0F, -9.0F, -14.0F, 0.125F, 0.1875F}}
        }));
        var p11 = new net.minecraft.client.model.geom.ModelPart(p11Cubes, java.util.Map.ofEntries());
        p11.setInitialPose(PartPose.offsetAndRotation(0.0F, 12.0F, 6.0F, 0.0F, 0.0F, 0.0F)); p11.resetPose();
        var p10Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/body/rotation/body_sub_1/chest/topchest", java.util.List.of()));
        p10Cubes.add(new ReferenceMeshCube(-10.0F, -5.0F, -14.0F, 20.0F, 5.0F, 14.0F, new float[][][] {
                {{10.0F, -5.0F, 0.0F, 0.4453125F, 0.21875F}, {-10.0F, -5.0F, 0.0F, 0.3671875F, 0.21875F}, {-10.0F, -5.0F, -14.0F, 0.3671875F, 0.328125F}, {10.0F, -5.0F, -14.0F, 0.4453125F, 0.328125F}},
                {{10.0F, 0.0F, -14.0F, 0.5234375F, 0.328125F}, {-10.0F, 0.0F, -14.0F, 0.4453125F, 0.328125F}, {-10.0F, 0.0F, 0.0F, 0.4453125F, 0.21875F}, {10.0F, 0.0F, 0.0F, 0.5234375F, 0.21875F}},
                {{-10.0F, -5.0F, -14.0F, 0.3671875F, 0.328125F}, {-10.0F, -5.0F, 0.0F, 0.3125F, 0.328125F}, {-10.0F, 0.0F, 0.0F, 0.3125F, 0.3671875F}, {-10.0F, 0.0F, -14.0F, 0.3671875F, 0.3671875F}},
                {{10.0F, -5.0F, 0.0F, 0.5F, 0.328125F}, {10.0F, -5.0F, -14.0F, 0.4453125F, 0.328125F}, {10.0F, 0.0F, -14.0F, 0.4453125F, 0.3671875F}, {10.0F, 0.0F, 0.0F, 0.5F, 0.3671875F}},
                {{10.0F, -5.0F, -14.0F, 0.4453125F, 0.328125F}, {-10.0F, -5.0F, -14.0F, 0.3671875F, 0.328125F}, {-10.0F, 0.0F, -14.0F, 0.3671875F, 0.3671875F}, {10.0F, 0.0F, -14.0F, 0.4453125F, 0.3671875F}},
                {{-10.0F, -5.0F, 0.0F, 0.578125F, 0.328125F}, {10.0F, -5.0F, 0.0F, 0.5F, 0.328125F}, {10.0F, 0.0F, 0.0F, 0.5F, 0.3671875F}, {-10.0F, 0.0F, 0.0F, 0.578125F, 0.3671875F}}
        }));
        p10Cubes.add(new ReferenceMeshCube(-2.0F, -2.0F, -15.0F, 4.0F, 4.0F, -2.0F, new float[][][] {
                {{2.0F, -2.0F, -17.0F, 0.53125F, 0.390625F}, {-2.0F, -2.0F, -17.0F, 0.5F, 0.390625F}, {-2.0F, -2.0F, -15.0F, 0.5F, 0.40625F}, {2.0F, -2.0F, -15.0F, 0.53125F, 0.40625F}},
                {{2.0F, 2.0F, -15.0F, 0.5625F, 0.40625F}, {-2.0F, 2.0F, -15.0F, 0.53125F, 0.40625F}, {-2.0F, 2.0F, -17.0F, 0.53125F, 0.390625F}, {2.0F, 2.0F, -17.0F, 0.5625F, 0.390625F}},
                {{-2.0F, -2.0F, -15.0F, 0.5F, 0.40625F}, {-2.0F, -2.0F, -17.0F, 0.4921875F, 0.40625F}, {-2.0F, 2.0F, -17.0F, 0.4921875F, 0.46875F}, {-2.0F, 2.0F, -15.0F, 0.5F, 0.46875F}},
                {{2.0F, -2.0F, -17.0F, 0.5390625F, 0.40625F}, {2.0F, -2.0F, -15.0F, 0.53125F, 0.40625F}, {2.0F, 2.0F, -15.0F, 0.53125F, 0.46875F}, {2.0F, 2.0F, -17.0F, 0.5390625F, 0.46875F}},
                {{2.0F, -2.0F, -15.0F, 0.53125F, 0.40625F}, {-2.0F, -2.0F, -15.0F, 0.5F, 0.40625F}, {-2.0F, 2.0F, -15.0F, 0.5F, 0.46875F}, {2.0F, 2.0F, -15.0F, 0.53125F, 0.46875F}},
                {{-2.0F, -2.0F, -17.0F, 0.5703125F, 0.40625F}, {2.0F, -2.0F, -17.0F, 0.5390625F, 0.40625F}, {2.0F, 2.0F, -17.0F, 0.5390625F, 0.46875F}, {-2.0F, 2.0F, -17.0F, 0.5703125F, 0.46875F}}
        }));
        p10Cubes.add(new ReferenceMeshCube(-2.0F, -2.0F, -15.0F, 4.0F, 4.0F, 1.0F, new float[][][] {
                {{2.0F, -2.0F, -14.0F, 0.33203125F, 0.3671875F}, {-2.0F, -2.0F, -14.0F, 0.31640625F, 0.3671875F}, {-2.0F, -2.0F, -15.0F, 0.31640625F, 0.375F}, {2.0F, -2.0F, -15.0F, 0.33203125F, 0.375F}},
                {{2.0F, 2.0F, -15.0F, 0.34765625F, 0.375F}, {-2.0F, 2.0F, -15.0F, 0.33203125F, 0.375F}, {-2.0F, 2.0F, -14.0F, 0.33203125F, 0.3671875F}, {2.0F, 2.0F, -14.0F, 0.34765625F, 0.3671875F}},
                {{-2.0F, -2.0F, -15.0F, 0.31640625F, 0.375F}, {-2.0F, -2.0F, -14.0F, 0.3125F, 0.375F}, {-2.0F, 2.0F, -14.0F, 0.3125F, 0.40625F}, {-2.0F, 2.0F, -15.0F, 0.31640625F, 0.40625F}},
                {{2.0F, -2.0F, -14.0F, 0.3359375F, 0.375F}, {2.0F, -2.0F, -15.0F, 0.33203125F, 0.375F}, {2.0F, 2.0F, -15.0F, 0.33203125F, 0.40625F}, {2.0F, 2.0F, -14.0F, 0.3359375F, 0.40625F}},
                {{2.0F, -2.0F, -15.0F, 0.33203125F, 0.375F}, {-2.0F, -2.0F, -15.0F, 0.31640625F, 0.375F}, {-2.0F, 2.0F, -15.0F, 0.31640625F, 0.40625F}, {2.0F, 2.0F, -15.0F, 0.33203125F, 0.40625F}},
                {{-2.0F, -2.0F, -14.0F, 0.3515625F, 0.375F}, {2.0F, -2.0F, -14.0F, 0.3359375F, 0.375F}, {2.0F, 2.0F, -14.0F, 0.3359375F, 0.40625F}, {-2.0F, 2.0F, -14.0F, 0.3515625F, 0.40625F}}
        }));
        var p10 = new net.minecraft.client.model.geom.ModelPart(p10Cubes, java.util.Map.ofEntries());
        p10.setInitialPose(PartPose.offsetAndRotation(0.0F, -10.0F, 7.0F, 0.0F, 0.0F, 0.0F)); p10.resetPose();
        var p9Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/body/rotation/body_sub_1/chest", java.util.List.of()));
        p9Cubes.add(new ReferenceMeshCube(-10.0F, -10.0F, -7.0F, 20.0F, 10.0F, 14.0F, new float[][][] {
                {{10.0F, -10.0F, 7.0F, 0.4453125F, 0.03125F}, {-10.0F, -10.0F, 7.0F, 0.3671875F, 0.03125F}, {-10.0F, -10.0F, -7.0F, 0.3671875F, 0.140625F}, {10.0F, -10.0F, -7.0F, 0.4453125F, 0.140625F}},
                {{10.0F, 0.0F, -7.0F, 0.5234375F, 0.140625F}, {-10.0F, 0.0F, -7.0F, 0.4453125F, 0.140625F}, {-10.0F, 0.0F, 7.0F, 0.4453125F, 0.03125F}, {10.0F, 0.0F, 7.0F, 0.5234375F, 0.03125F}},
                {{-10.0F, -10.0F, -7.0F, 0.3671875F, 0.140625F}, {-10.0F, -10.0F, 7.0F, 0.3125F, 0.140625F}, {-10.0F, 0.0F, 7.0F, 0.3125F, 0.21875F}, {-10.0F, 0.0F, -7.0F, 0.3671875F, 0.21875F}},
                {{10.0F, -10.0F, 7.0F, 0.5F, 0.140625F}, {10.0F, -10.0F, -7.0F, 0.4453125F, 0.140625F}, {10.0F, 0.0F, -7.0F, 0.4453125F, 0.21875F}, {10.0F, 0.0F, 7.0F, 0.5F, 0.21875F}},
                {{10.0F, -10.0F, -7.0F, 0.4453125F, 0.140625F}, {-10.0F, -10.0F, -7.0F, 0.3671875F, 0.140625F}, {-10.0F, 0.0F, -7.0F, 0.3671875F, 0.21875F}, {10.0F, 0.0F, -7.0F, 0.4453125F, 0.21875F}},
                {{-10.0F, -10.0F, 7.0F, 0.578125F, 0.140625F}, {10.0F, -10.0F, 7.0F, 0.5F, 0.140625F}, {10.0F, 0.0F, 7.0F, 0.5F, 0.21875F}, {-10.0F, 0.0F, 7.0F, 0.578125F, 0.21875F}}
        }));
        var p9 = new net.minecraft.client.model.geom.ModelPart(p9Cubes, java.util.Map.ofEntries(java.util.Map.entry("topchest", p10)));
        p9.setInitialPose(PartPose.offsetAndRotation(0.0F, -1.0F, 1.0F, -1.57079633F, 0.0F, 0.0F)); p9.resetPose();
        var p8Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/body/rotation/body_sub_1", java.util.List.of()));
        p8Cubes.add(new ReferenceMeshCube(-5.0F, -10.0F, -7.0F, 10.0F, 16.0F, 8.0F, new float[][][] {
                {{5.0F, -10.0F, 1.0F, 0.1953125F, 0.09375F}, {-5.0F, -10.0F, 1.0F, 0.15625F, 0.09375F}, {-5.0F, -10.0F, -7.0F, 0.15625F, 0.15625F}, {5.0F, -10.0F, -7.0F, 0.1953125F, 0.15625F}},
                {{5.0F, 6.0F, -7.0F, 0.234375F, 0.15625F}, {-5.0F, 6.0F, -7.0F, 0.1953125F, 0.15625F}, {-5.0F, 6.0F, 1.0F, 0.1953125F, 0.09375F}, {5.0F, 6.0F, 1.0F, 0.234375F, 0.09375F}},
                {{-5.0F, -10.0F, -7.0F, 0.15625F, 0.15625F}, {-5.0F, -10.0F, 1.0F, 0.125F, 0.15625F}, {-5.0F, 6.0F, 1.0F, 0.125F, 0.28125F}, {-5.0F, 6.0F, -7.0F, 0.15625F, 0.28125F}},
                {{5.0F, -10.0F, 1.0F, 0.2265625F, 0.15625F}, {5.0F, -10.0F, -7.0F, 0.1953125F, 0.15625F}, {5.0F, 6.0F, -7.0F, 0.1953125F, 0.28125F}, {5.0F, 6.0F, 1.0F, 0.2265625F, 0.28125F}},
                {{5.0F, -10.0F, -7.0F, 0.1953125F, 0.15625F}, {-5.0F, -10.0F, -7.0F, 0.15625F, 0.15625F}, {-5.0F, 6.0F, -7.0F, 0.15625F, 0.28125F}, {5.0F, 6.0F, -7.0F, 0.1953125F, 0.28125F}},
                {{-5.0F, -10.0F, 1.0F, 0.265625F, 0.15625F}, {5.0F, -10.0F, 1.0F, 0.2265625F, 0.15625F}, {5.0F, 6.0F, 1.0F, 0.2265625F, 0.28125F}, {-5.0F, 6.0F, 1.0F, 0.265625F, 0.28125F}}
        }));
        var p8 = new net.minecraft.client.model.geom.ModelPart(p8Cubes, java.util.Map.ofEntries(java.util.Map.entry("chest", p9)));
        p8.setInitialPose(PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p8.resetPose();
        var p7Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/body/rotation", java.util.List.of()));
        var p7 = new net.minecraft.client.model.geom.ModelPart(p7Cubes, java.util.Map.ofEntries(java.util.Map.entry("body_sub_1", p8)));
        p7.setInitialPose(PartPose.offsetAndRotation(0.0F, -13.0F, 2.0F, 1.57079633F, 0.0F, 0.0F)); p7.resetPose();
        var p6Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/body", java.util.List.of()));
        var p6 = new net.minecraft.client.model.geom.ModelPart(p6Cubes, java.util.Map.ofEntries(java.util.Map.entry("rotation", p7)));
        p6.setInitialPose(PartPose.offsetAndRotation(0.0F, 13.0F, -2.0F, 0.0F, 0.0F, 0.0F)); p6.resetPose();
        var p5Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_leg4", java.util.List.of()));
        var p5 = new net.minecraft.client.model.geom.ModelPart(p5Cubes, java.util.Map.ofEntries(java.util.Map.entry("leg4", p15)));
        p5.setInitialPose(nativeRoot.getChild("left_front_leg").storePose()); p5.resetPose();
        var p4Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_leg3", java.util.List.of()));
        var p4 = new net.minecraft.client.model.geom.ModelPart(p4Cubes, java.util.Map.ofEntries(java.util.Map.entry("leg3", p14)));
        p4.setInitialPose(nativeRoot.getChild("right_front_leg").storePose()); p4.resetPose();
        var p3Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_leg2", java.util.List.of()));
        var p3 = new net.minecraft.client.model.geom.ModelPart(p3Cubes, java.util.Map.ofEntries(java.util.Map.entry("leg2", p13)));
        p3.setInitialPose(nativeRoot.getChild("left_hind_leg").storePose()); p3.resetPose();
        var p2Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_leg1", java.util.List.of()));
        var p2 = new net.minecraft.client.model.geom.ModelPart(p2Cubes, java.util.Map.ofEntries(java.util.Map.entry("leg1", p12)));
        p2.setInitialPose(nativeRoot.getChild("right_hind_leg").storePose()); p2.resetPose();
        var p1Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body", java.util.List.of()));
        var p1 = new net.minecraft.client.model.geom.ModelPart(p1Cubes, java.util.Map.ofEntries(java.util.Map.entry("body", p6)));
        p1.setInitialPose(nativeRoot.getChild("body").storePose()); p1.resetPose();
        var p0Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_head", java.util.List.of()));
        var p0 = new net.minecraft.client.model.geom.ModelPart(p0Cubes, java.util.Map.ofEntries(java.util.Map.entry("head", p11)));
        p0.setInitialPose(nativeRoot.getChild("head").storePose()); p0.resetPose();
        return new net.minecraft.client.model.geom.ModelPart(java.util.List.of(), java.util.Map.ofEntries(java.util.Map.entry("__cem_host_head", p0), java.util.Map.entry("__cem_host_body", p1), java.util.Map.entry("__cem_host_leg1", p2), java.util.Map.entry("__cem_host_leg2", p3), java.util.Map.entry("__cem_host_leg3", p4), java.util.Map.entry("__cem_host_leg4", p5)));
    }
}
