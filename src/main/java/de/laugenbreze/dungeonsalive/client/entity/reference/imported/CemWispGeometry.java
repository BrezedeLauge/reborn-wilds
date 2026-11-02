/*
 * Generated adaptation of ReCrafted Mobs 11.0 by GillsBills and credited contributors.
 * Authored asset license: CC-BY-NC-SA-4.0; https://creativecommons.org/licenses/by-nc-sa/4.0/
 * Source: https://modrinth.com/resourcepack/recrafted-mobs
 * Model: assets/minecraft/optifine/cem/allay/allay5.jem
 * Source SHA256: de370922c46e2329cd1091faa643169c75f1ee76484cb1c07c8b2951d48854c6
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

public final class CemWispGeometry {
    private CemWispGeometry() {}

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
        PartDefinition p8 = p7.addOrReplaceChild("wisp", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -6.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p9 = p8.addOrReplaceChild("side_right", CubeListBuilder.create(), PartPose.offsetAndRotation(-4.0F, 2.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p10 = p9.addOrReplaceChild("bone", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, -0.5F, 0.0F, 0.0F, -0.567232007F));
        PartDefinition p11 = p9.addOrReplaceChild("bone2", CubeListBuilder.create(), PartPose.offsetAndRotation(-2.0F, 1.0F, -0.5F, 0.0F, 0.0F, -1.22173048F));
        PartDefinition p12 = p8.addOrReplaceChild("side_left", CubeListBuilder.create(), PartPose.offsetAndRotation(4.0F, 2.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p13 = p12.addOrReplaceChild("bone3", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, -0.5F, 0.0F, 0.0F, 0.436332313F));
        PartDefinition p14 = p13.addOrReplaceChild("head_sub_7", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p15 = p12.addOrReplaceChild("bone4", CubeListBuilder.create(), PartPose.offsetAndRotation(1.0F, 2.0F, -0.5F, 0.0F, 0.0F, 1.43989663F));
        PartDefinition p16 = p15.addOrReplaceChild("head_sub_9", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p17 = p2.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 6.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p18 = p4.addOrReplaceChild("right_arm", CubeListBuilder.create(), PartPose.offsetAndRotation(1.75F, 5.5F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p19 = p3.addOrReplaceChild("left_arm", CubeListBuilder.create(), PartPose.offsetAndRotation(-1.75F, 5.5F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p20 = p5.addOrReplaceChild("left_wing", CubeListBuilder.create(), PartPose.offsetAndRotation(-0.5F, 6.0F, -1.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p21 = p6.addOrReplaceChild("right_wing", CubeListBuilder.create(), PartPose.offsetAndRotation(0.5F, 6.0F, -1.0F, 0.0F, 0.0F, 0.0F));
        return LayerDefinition.create(mesh, 32, 32);
    }

    /** Preserve source expression units; static scales are reapplied after every pose reset. */
    public static ReferenceMobDefinition.Builder configure(ReferenceMobDefinition.Builder builder) {
        builder.cemDriver("allay");
        builder.bakedRoot(CemWispGeometry::bakeRoot);
        builder.hostAlias("root", "__cem_host_root");
        builder.hostAlias("head", "__cem_host_head");
        builder.hostAlias("body", "__cem_host_body");
        builder.hostAlias("left_arm", "__cem_host_left_arm");
        builder.hostAlias("right_arm", "__cem_host_right_arm");
        builder.hostAlias("left_wing", "__cem_host_left_wing");
        builder.hostAlias("right_wing", "__cem_host_right_wing");
        builder.expression("wisp.ty", "-7");
        builder.expression("wisp.rx", "head.rx-head.rx");
        builder.expression("wisp.ry", "head.ry-head.ry");
        builder.expression("wisp.rz", "head.rz-head.rz");
        builder.expression("wisp.sx", "0.65");
        builder.expression("wisp.sy", "0.65");
        builder.expression("wisp.sz", "0.65");
        builder.expression("body.rx", "body.rx-body.rx");
        builder.expression("body.ry", "body.ry-body.ry");
        builder.expression("body.rz", "body.rz-body.rz");
        builder.expression("body.sx", "0.90");
        builder.expression("body.sy", "0.90");
        builder.expression("body.sz", "0.90");
        builder.expression("right_arm.tx", "right_arm.tx-right_arm.tx");
        builder.expression("right_arm.ty", "right_arm.ty-right_arm.ty");
        builder.expression("right_arm.tz", "right_arm.tz-right_arm.tz");
        builder.expression("right_arm.rx", "right_arm.rx-right_arm.rx+torad(-90)");
        builder.expression("right_arm.ry", "right_arm.ry-right_arm.ry");
        builder.expression("right_arm.rz", "right_arm.rz-right_arm.rz");
        builder.expression("body.ty", "0");
        builder.expression("body.tz", "3.5");
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
        var p21Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_root/__cem_host_body/__cem_host_right_wing/right_wing", java.util.List.of()));
        var p21 = new net.minecraft.client.model.geom.ModelPart(p21Cubes, java.util.Map.ofEntries());
        p21.setInitialPose(PartPose.offsetAndRotation(0.5F, 6.0F, -1.0F, 0.0F, 0.0F, 0.0F)); p21.resetPose();
        var p20Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_root/__cem_host_body/__cem_host_left_wing/left_wing", java.util.List.of()));
        var p20 = new net.minecraft.client.model.geom.ModelPart(p20Cubes, java.util.Map.ofEntries());
        p20.setInitialPose(PartPose.offsetAndRotation(-0.5F, 6.0F, -1.0F, 0.0F, 0.0F, 0.0F)); p20.resetPose();
        var p19Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_root/__cem_host_body/__cem_host_left_arm/left_arm", java.util.List.of()));
        var p19 = new net.minecraft.client.model.geom.ModelPart(p19Cubes, java.util.Map.ofEntries());
        p19.setInitialPose(PartPose.offsetAndRotation(-1.75F, 5.5F, 0.0F, 0.0F, 0.0F, 0.0F)); p19.resetPose();
        var p18Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_root/__cem_host_body/__cem_host_right_arm/right_arm", java.util.List.of()));
        var p18 = new net.minecraft.client.model.geom.ModelPart(p18Cubes, java.util.Map.ofEntries());
        p18.setInitialPose(PartPose.offsetAndRotation(1.75F, 5.5F, 0.0F, 0.0F, 0.0F, 0.0F)); p18.resetPose();
        var p17Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_root/__cem_host_body/body", java.util.List.of()));
        var p17 = new net.minecraft.client.model.geom.ModelPart(p17Cubes, java.util.Map.ofEntries());
        p17.setInitialPose(PartPose.offsetAndRotation(0.0F, 6.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p17.resetPose();
        var p16Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_root/__cem_host_head/head/wisp/side_left/bone4/head_sub_9", java.util.List.of()));
        p16Cubes.add(new ReferenceMeshCube(0.0F, 2.5F, 0.0F, 4.0F, 1.0F, 0.0F, new float[][][] {
                {{0.0F, 2.5F, 0.0F, 0.0625F, 0.125F}, {4.0F, 2.5F, 0.0F, 0.0F, 0.125F}, {4.0F, 2.5F, 0.0F, 0.0F, 0.125F}, {0.0F, 2.5F, 0.0F, 0.0625F, 0.125F}},
                {{0.0F, 3.5F, 0.0F, 0.125F, 0.125F}, {4.0F, 3.5F, 0.0F, 0.0625F, 0.125F}, {4.0F, 3.5F, 0.0F, 0.0625F, 0.125F}, {0.0F, 3.5F, 0.0F, 0.125F, 0.125F}},
                {{4.0F, 3.5F, 0.0F, 0.0F, 0.140625F}, {4.0F, 3.5F, 0.0F, 0.0F, 0.140625F}, {4.0F, 2.5F, 0.0F, 0.0F, 0.125F}, {4.0F, 2.5F, 0.0F, 0.0F, 0.125F}},
                {{0.0F, 3.5F, 0.0F, 0.0625F, 0.140625F}, {0.0F, 3.5F, 0.0F, 0.0625F, 0.140625F}, {0.0F, 2.5F, 0.0F, 0.0625F, 0.125F}, {0.0F, 2.5F, 0.0F, 0.0625F, 0.125F}},
                {{0.0F, 3.5F, 0.0F, 0.0625F, 0.140625F}, {4.0F, 3.5F, 0.0F, 0.0F, 0.140625F}, {4.0F, 2.5F, 0.0F, 0.0F, 0.125F}, {0.0F, 2.5F, 0.0F, 0.0625F, 0.125F}},
                {{4.0F, 3.5F, 0.0F, 0.125F, 0.140625F}, {0.0F, 3.5F, 0.0F, 0.0625F, 0.140625F}, {0.0F, 2.5F, 0.0F, 0.0625F, 0.125F}, {4.0F, 2.5F, 0.0F, 0.125F, 0.125F}}
        }));
        var p16 = new net.minecraft.client.model.geom.ModelPart(p16Cubes, java.util.Map.ofEntries());
        p16.setInitialPose(PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p16.resetPose();
        var p15Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_root/__cem_host_head/head/wisp/side_left/bone4", java.util.List.of()));
        var p15 = new net.minecraft.client.model.geom.ModelPart(p15Cubes, java.util.Map.ofEntries(java.util.Map.entry("head_sub_9", p16)));
        p15.setInitialPose(PartPose.offsetAndRotation(1.0F, 2.0F, -0.5F, 0.0F, 0.0F, 1.43989663F)); p15.resetPose();
        var p14Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_root/__cem_host_head/head/wisp/side_left/bone3/head_sub_7", java.util.List.of()));
        p14Cubes.add(new ReferenceMeshCube(0.0F, -1.0F, 0.0F, 4.0F, 1.0F, 0.0F, new float[][][] {
                {{0.0F, -1.0F, 0.0F, 0.0625F, 0.09375F}, {4.0F, -1.0F, 0.0F, 0.0F, 0.09375F}, {4.0F, -1.0F, 0.0F, 0.0F, 0.09375F}, {0.0F, -1.0F, 0.0F, 0.0625F, 0.09375F}},
                {{0.0F, 0.0F, 0.0F, 0.125F, 0.09375F}, {4.0F, 0.0F, 0.0F, 0.0625F, 0.09375F}, {4.0F, 0.0F, 0.0F, 0.0625F, 0.09375F}, {0.0F, 0.0F, 0.0F, 0.125F, 0.09375F}},
                {{4.0F, 0.0F, 0.0F, 0.0F, 0.109375F}, {4.0F, 0.0F, 0.0F, 0.0F, 0.109375F}, {4.0F, -1.0F, 0.0F, 0.0F, 0.09375F}, {4.0F, -1.0F, 0.0F, 0.0F, 0.09375F}},
                {{0.0F, 0.0F, 0.0F, 0.0625F, 0.109375F}, {0.0F, 0.0F, 0.0F, 0.0625F, 0.109375F}, {0.0F, -1.0F, 0.0F, 0.0625F, 0.09375F}, {0.0F, -1.0F, 0.0F, 0.0625F, 0.09375F}},
                {{0.0F, 0.0F, 0.0F, 0.0625F, 0.109375F}, {4.0F, 0.0F, 0.0F, 0.0F, 0.109375F}, {4.0F, -1.0F, 0.0F, 0.0F, 0.09375F}, {0.0F, -1.0F, 0.0F, 0.0625F, 0.09375F}},
                {{4.0F, 0.0F, 0.0F, 0.125F, 0.109375F}, {0.0F, 0.0F, 0.0F, 0.0625F, 0.109375F}, {0.0F, -1.0F, 0.0F, 0.0625F, 0.09375F}, {4.0F, -1.0F, 0.0F, 0.125F, 0.09375F}}
        }));
        var p14 = new net.minecraft.client.model.geom.ModelPart(p14Cubes, java.util.Map.ofEntries());
        p14.setInitialPose(PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p14.resetPose();
        var p13Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_root/__cem_host_head/head/wisp/side_left/bone3", java.util.List.of()));
        var p13 = new net.minecraft.client.model.geom.ModelPart(p13Cubes, java.util.Map.ofEntries(java.util.Map.entry("head_sub_7", p14)));
        p13.setInitialPose(PartPose.offsetAndRotation(0.0F, 0.0F, -0.5F, 0.0F, 0.0F, 0.436332313F)); p13.resetPose();
        var p12Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_root/__cem_host_head/head/wisp/side_left", java.util.List.of()));
        var p12 = new net.minecraft.client.model.geom.ModelPart(p12Cubes, java.util.Map.ofEntries(java.util.Map.entry("bone3", p13), java.util.Map.entry("bone4", p15)));
        p12.setInitialPose(PartPose.offsetAndRotation(4.0F, 2.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p12.resetPose();
        var p11Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_root/__cem_host_head/head/wisp/side_right/bone2", java.util.List.of()));
        p11Cubes.add(new ReferenceMeshCube(-4.0F, 3.5F, 0.0F, 4.0F, 1.0F, 0.0F, new float[][][] {
                {{0.0F, 3.5F, 0.0F, 0.0625F, 0.125F}, {-4.0F, 3.5F, 0.0F, 0.0F, 0.125F}, {-4.0F, 3.5F, 0.0F, 0.0F, 0.125F}, {0.0F, 3.5F, 0.0F, 0.0625F, 0.125F}},
                {{0.0F, 4.5F, 0.0F, 0.125F, 0.125F}, {-4.0F, 4.5F, 0.0F, 0.0625F, 0.125F}, {-4.0F, 4.5F, 0.0F, 0.0625F, 0.125F}, {0.0F, 4.5F, 0.0F, 0.125F, 0.125F}},
                {{-4.0F, 3.5F, 0.0F, 0.0F, 0.125F}, {-4.0F, 3.5F, 0.0F, 0.0F, 0.125F}, {-4.0F, 4.5F, 0.0F, 0.0F, 0.140625F}, {-4.0F, 4.5F, 0.0F, 0.0F, 0.140625F}},
                {{0.0F, 3.5F, 0.0F, 0.0625F, 0.125F}, {0.0F, 3.5F, 0.0F, 0.0625F, 0.125F}, {0.0F, 4.5F, 0.0F, 0.0625F, 0.140625F}, {0.0F, 4.5F, 0.0F, 0.0625F, 0.140625F}},
                {{0.0F, 3.5F, 0.0F, 0.0625F, 0.125F}, {-4.0F, 3.5F, 0.0F, 0.0F, 0.125F}, {-4.0F, 4.5F, 0.0F, 0.0F, 0.140625F}, {0.0F, 4.5F, 0.0F, 0.0625F, 0.140625F}},
                {{-4.0F, 3.5F, 0.0F, 0.125F, 0.125F}, {0.0F, 3.5F, 0.0F, 0.0625F, 0.125F}, {0.0F, 4.5F, 0.0F, 0.0625F, 0.140625F}, {-4.0F, 4.5F, 0.0F, 0.125F, 0.140625F}}
        }));
        var p11 = new net.minecraft.client.model.geom.ModelPart(p11Cubes, java.util.Map.ofEntries());
        p11.setInitialPose(PartPose.offsetAndRotation(-2.0F, 1.0F, -0.5F, 0.0F, 0.0F, -1.22173048F)); p11.resetPose();
        var p10Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_root/__cem_host_head/head/wisp/side_right/bone", java.util.List.of()));
        p10Cubes.add(new ReferenceMeshCube(-4.0F, -1.0F, 0.0F, 4.0F, 1.0F, 0.0F, new float[][][] {
                {{0.0F, -1.0F, 0.0F, 0.0625F, 0.09375F}, {-4.0F, -1.0F, 0.0F, 0.0F, 0.09375F}, {-4.0F, -1.0F, 0.0F, 0.0F, 0.09375F}, {0.0F, -1.0F, 0.0F, 0.0625F, 0.09375F}},
                {{0.0F, 0.0F, 0.0F, 0.125F, 0.09375F}, {-4.0F, 0.0F, 0.0F, 0.0625F, 0.09375F}, {-4.0F, 0.0F, 0.0F, 0.0625F, 0.09375F}, {0.0F, 0.0F, 0.0F, 0.125F, 0.09375F}},
                {{-4.0F, -1.0F, 0.0F, 0.0F, 0.09375F}, {-4.0F, -1.0F, 0.0F, 0.0F, 0.09375F}, {-4.0F, 0.0F, 0.0F, 0.0F, 0.109375F}, {-4.0F, 0.0F, 0.0F, 0.0F, 0.109375F}},
                {{0.0F, -1.0F, 0.0F, 0.0625F, 0.09375F}, {0.0F, -1.0F, 0.0F, 0.0625F, 0.09375F}, {0.0F, 0.0F, 0.0F, 0.0625F, 0.109375F}, {0.0F, 0.0F, 0.0F, 0.0625F, 0.109375F}},
                {{0.0F, -1.0F, 0.0F, 0.0625F, 0.09375F}, {-4.0F, -1.0F, 0.0F, 0.0F, 0.09375F}, {-4.0F, 0.0F, 0.0F, 0.0F, 0.109375F}, {0.0F, 0.0F, 0.0F, 0.0625F, 0.109375F}},
                {{-4.0F, -1.0F, 0.0F, 0.125F, 0.09375F}, {0.0F, -1.0F, 0.0F, 0.0625F, 0.09375F}, {0.0F, 0.0F, 0.0F, 0.0625F, 0.109375F}, {-4.0F, 0.0F, 0.0F, 0.125F, 0.109375F}}
        }));
        var p10 = new net.minecraft.client.model.geom.ModelPart(p10Cubes, java.util.Map.ofEntries());
        p10.setInitialPose(PartPose.offsetAndRotation(0.0F, 0.0F, -0.5F, 0.0F, 0.0F, -0.567232007F)); p10.resetPose();
        var p9Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_root/__cem_host_head/head/wisp/side_right", java.util.List.of()));
        var p9 = new net.minecraft.client.model.geom.ModelPart(p9Cubes, java.util.Map.ofEntries(java.util.Map.entry("bone", p10), java.util.Map.entry("bone2", p11)));
        p9.setInitialPose(PartPose.offsetAndRotation(-4.0F, 2.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p9.resetPose();
        var p8Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_root/__cem_host_head/head/wisp", java.util.List.of()));
        p8Cubes.add(new ReferenceMeshCube(-4.5F, -4.0F, -5.0F, 9.0F, 9.0F, 9.0F, new float[][][] {
                {{4.5F, -4.0F, 4.0F, 0.28125F, 0.0F}, {-4.5F, -4.0F, 4.0F, 0.140625F, 0.0F}, {-4.5F, -4.0F, -5.0F, 0.140625F, 0.140625F}, {4.5F, -4.0F, -5.0F, 0.28125F, 0.140625F}},
                {{4.5F, 5.0F, -5.0F, 0.421875F, 0.140625F}, {-4.5F, 5.0F, -5.0F, 0.28125F, 0.140625F}, {-4.5F, 5.0F, 4.0F, 0.28125F, 0.0F}, {4.5F, 5.0F, 4.0F, 0.421875F, 0.0F}},
                {{-4.5F, -4.0F, -5.0F, 0.140625F, 0.140625F}, {-4.5F, -4.0F, 4.0F, 0.0F, 0.140625F}, {-4.5F, 5.0F, 4.0F, 0.0F, 0.28125F}, {-4.5F, 5.0F, -5.0F, 0.140625F, 0.28125F}},
                {{4.5F, -4.0F, 4.0F, 0.421875F, 0.140625F}, {4.5F, -4.0F, -5.0F, 0.28125F, 0.140625F}, {4.5F, 5.0F, -5.0F, 0.28125F, 0.28125F}, {4.5F, 5.0F, 4.0F, 0.421875F, 0.28125F}},
                {{4.5F, -4.0F, -5.0F, 0.28125F, 0.140625F}, {-4.5F, -4.0F, -5.0F, 0.140625F, 0.140625F}, {-4.5F, 5.0F, -5.0F, 0.140625F, 0.28125F}, {4.5F, 5.0F, -5.0F, 0.28125F, 0.28125F}},
                {{-4.5F, -4.0F, 4.0F, 0.5625F, 0.140625F}, {4.5F, -4.0F, 4.0F, 0.421875F, 0.140625F}, {4.5F, 5.0F, 4.0F, 0.421875F, 0.28125F}, {-4.5F, 5.0F, 4.0F, 0.5625F, 0.28125F}}
        }));
        var p8 = new net.minecraft.client.model.geom.ModelPart(p8Cubes, java.util.Map.ofEntries(java.util.Map.entry("side_right", p9), java.util.Map.entry("side_left", p12)));
        p8.setInitialPose(PartPose.offsetAndRotation(0.0F, -6.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p8.resetPose();
        var p7Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_root/__cem_host_head/head", java.util.List.of()));
        var p7 = new net.minecraft.client.model.geom.ModelPart(p7Cubes, java.util.Map.ofEntries(java.util.Map.entry("wisp", p8)));
        p7.setInitialPose(PartPose.offsetAndRotation(0.0F, 6.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p7.resetPose();
        var p6Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_root/__cem_host_body/__cem_host_right_wing", java.util.List.of()));
        var p6 = new net.minecraft.client.model.geom.ModelPart(p6Cubes, java.util.Map.ofEntries(java.util.Map.entry("right_wing", p21)));
        p6.setInitialPose(nativeRoot.getChild("root").getChild("body").getChild("right_wing").storePose()); p6.resetPose();
        var p5Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_root/__cem_host_body/__cem_host_left_wing", java.util.List.of()));
        var p5 = new net.minecraft.client.model.geom.ModelPart(p5Cubes, java.util.Map.ofEntries(java.util.Map.entry("left_wing", p20)));
        p5.setInitialPose(nativeRoot.getChild("root").getChild("body").getChild("left_wing").storePose()); p5.resetPose();
        var p4Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_root/__cem_host_body/__cem_host_right_arm", java.util.List.of()));
        var p4 = new net.minecraft.client.model.geom.ModelPart(p4Cubes, java.util.Map.ofEntries(java.util.Map.entry("right_arm", p18)));
        p4.setInitialPose(nativeRoot.getChild("root").getChild("body").getChild("right_arm").storePose()); p4.resetPose();
        var p3Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_root/__cem_host_body/__cem_host_left_arm", java.util.List.of()));
        var p3 = new net.minecraft.client.model.geom.ModelPart(p3Cubes, java.util.Map.ofEntries(java.util.Map.entry("left_arm", p19)));
        p3.setInitialPose(nativeRoot.getChild("root").getChild("body").getChild("left_arm").storePose()); p3.resetPose();
        var p2Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_root/__cem_host_body", java.util.List.of()));
        var p2 = new net.minecraft.client.model.geom.ModelPart(p2Cubes, java.util.Map.ofEntries(java.util.Map.entry("__cem_host_left_arm", p3), java.util.Map.entry("__cem_host_right_arm", p4), java.util.Map.entry("__cem_host_left_wing", p5), java.util.Map.entry("__cem_host_right_wing", p6), java.util.Map.entry("body", p17)));
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
