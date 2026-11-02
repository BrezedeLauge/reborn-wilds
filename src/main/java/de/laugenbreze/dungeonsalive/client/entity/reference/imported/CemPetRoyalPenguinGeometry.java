/*
 * Generated adaptation of ReCrafted Mobs 11.0 by GillsBills and credited contributors.
 * Authored asset license: CC-BY-NC-SA-4.0; https://creativecommons.org/licenses/by-nc-sa/4.0/
 * Source: https://modrinth.com/resourcepack/recrafted-mobs
 * Model: assets/minecraft/optifine/cem/chicken/chicken6.jem
 * Source SHA256: dd03235a29f972d3555322411093864f75ae42719c4ba1319a73b8c55f1daec6
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

public final class CemPetRoyalPenguinGeometry {
    private CemPetRoyalPenguinGeometry() {}

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        PartDefinition p0 = root.addOrReplaceChild("__cem_host_head", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p1 = root.addOrReplaceChild("__cem_host_body", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p2 = root.addOrReplaceChild("__cem_host_right_leg", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p3 = root.addOrReplaceChild("__cem_host_left_leg", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p4 = root.addOrReplaceChild("__cem_host_right_wing", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p5 = root.addOrReplaceChild("__cem_host_left_wing", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p6 = root.addOrReplaceChild("__cem_host_bill", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p7 = root.addOrReplaceChild("__cem_host_chin", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p8 = p0.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 9.0F, 4.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p9 = p1.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 8.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p10 = p9.addOrReplaceChild("penguin", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p11 = p10.addOrReplaceChild("head3", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -20.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p12 = p11.addOrReplaceChild("body_sub_32", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p13 = p10.addOrReplaceChild("body2", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -8.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p14 = p10.addOrReplaceChild("left_wing3", CubeListBuilder.create(), PartPose.offsetAndRotation(5.0F, -18.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p15 = p10.addOrReplaceChild("right_wing3", CubeListBuilder.create(), PartPose.offsetAndRotation(-5.0F, -18.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p16 = p10.addOrReplaceChild("left_leg5", CubeListBuilder.create(), PartPose.offsetAndRotation(1.0F, -5.0F, 1.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p17 = p10.addOrReplaceChild("right_leg5", CubeListBuilder.create(), PartPose.offsetAndRotation(-2.0F, -5.0F, 1.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p18 = p2.addOrReplaceChild("right_leg", CubeListBuilder.create(), PartPose.offsetAndRotation(2.0F, 5.0F, -1.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p19 = p3.addOrReplaceChild("left_leg", CubeListBuilder.create(), PartPose.offsetAndRotation(-1.0F, 5.0F, -1.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p20 = p4.addOrReplaceChild("right_wing", CubeListBuilder.create(), PartPose.offsetAndRotation(4.0F, 11.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p21 = p5.addOrReplaceChild("left_wing", CubeListBuilder.create(), PartPose.offsetAndRotation(-4.0F, 11.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p22 = p6.addOrReplaceChild("bill", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 9.0F, 4.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p23 = p7.addOrReplaceChild("chin", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 9.0F, 4.0F, 0.0F, 0.0F, 0.0F));
        return LayerDefinition.create(mesh, 128, 128);
    }

    /** Preserve source expression units; static scales are reapplied after every pose reset. */
    public static ReferenceMobDefinition.Builder configure(ReferenceMobDefinition.Builder builder) {
        builder.cemDriver("chicken");
        builder.bakedRoot(CemPetRoyalPenguinGeometry::bakeRoot);
        builder.hostAlias("head", "__cem_host_head");
        builder.hostAlias("body", "__cem_host_body");
        builder.hostAlias("right_leg", "__cem_host_right_leg");
        builder.hostAlias("left_leg", "__cem_host_left_leg");
        builder.hostAlias("right_wing", "__cem_host_right_wing");
        builder.hostAlias("left_wing", "__cem_host_left_wing");
        builder.hostAlias("bill", "__cem_host_bill");
        builder.hostAlias("chin", "__cem_host_chin");
        builder.expression("left_wing3.rz", "left_wing.rz/4");
        builder.expression("right_wing3.rz", "right_wing.rz/4");
        builder.expression("penguin.rx", "torad(-90)");
        builder.expression("penguin.tz", "-8");
        builder.expression("penguin.ty", "-8");
        builder.expression("left_leg5.rx", "left_leg.rx");
        builder.expression("right_leg5.rx", "right_leg.rx");
        return builder;
    }

    /** Complete matching rig; installed cubes stay owned by the Minecraft client. */
    public static net.minecraft.client.model.geom.ModelPart bakeRoot() {
        var nativeRoot = net.minecraft.client.model.ChickenModel.createBodyLayer().bakeRoot();
        var nativeCubes = new java.util.HashMap<String, java.util.List<net.minecraft.client.model.geom.ModelPart.Cube>>();
        nativeRoot.visit(new com.mojang.blaze3d.vertex.PoseStack(),
                (pose, path, index, cube) -> nativeCubes.computeIfAbsent(path, key -> new java.util.ArrayList<>()).add(cube));
        var customCubes = new java.util.HashMap<String, java.util.List<net.minecraft.client.model.geom.ModelPart.Cube>>();
        createBodyLayer().bakeRoot().visit(new com.mojang.blaze3d.vertex.PoseStack(),
                (pose, path, index, cube) -> customCubes.computeIfAbsent(path, key -> new java.util.ArrayList<>()).add(cube));
        var p23Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_chin/chin", java.util.List.of()));
        var p23 = new net.minecraft.client.model.geom.ModelPart(p23Cubes, java.util.Map.ofEntries());
        p23.setInitialPose(PartPose.offsetAndRotation(0.0F, 9.0F, 4.0F, 0.0F, 0.0F, 0.0F)); p23.resetPose();
        var p22Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_bill/bill", java.util.List.of()));
        var p22 = new net.minecraft.client.model.geom.ModelPart(p22Cubes, java.util.Map.ofEntries());
        p22.setInitialPose(PartPose.offsetAndRotation(0.0F, 9.0F, 4.0F, 0.0F, 0.0F, 0.0F)); p22.resetPose();
        var p21Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_left_wing/left_wing", java.util.List.of()));
        var p21 = new net.minecraft.client.model.geom.ModelPart(p21Cubes, java.util.Map.ofEntries());
        p21.setInitialPose(PartPose.offsetAndRotation(-4.0F, 11.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p21.resetPose();
        var p20Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_right_wing/right_wing", java.util.List.of()));
        var p20 = new net.minecraft.client.model.geom.ModelPart(p20Cubes, java.util.Map.ofEntries());
        p20.setInitialPose(PartPose.offsetAndRotation(4.0F, 11.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p20.resetPose();
        var p19Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_left_leg/left_leg", java.util.List.of()));
        var p19 = new net.minecraft.client.model.geom.ModelPart(p19Cubes, java.util.Map.ofEntries());
        p19.setInitialPose(PartPose.offsetAndRotation(-1.0F, 5.0F, -1.0F, 0.0F, 0.0F, 0.0F)); p19.resetPose();
        var p18Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_right_leg/right_leg", java.util.List.of()));
        var p18 = new net.minecraft.client.model.geom.ModelPart(p18Cubes, java.util.Map.ofEntries());
        p18.setInitialPose(PartPose.offsetAndRotation(2.0F, 5.0F, -1.0F, 0.0F, 0.0F, 0.0F)); p18.resetPose();
        var p17Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/body/penguin/right_leg5", java.util.List.of()));
        p17Cubes.add(new ReferenceMeshCube(-2.0F, 0.0F, -3.0F, 4.0F, 5.0F, 3.0F, new float[][][] {
                {{2.0F, 0.0F, -3.0F, 0.1953125F, 0.6953125F}, {-2.0F, 0.0F, -3.0F, 0.1640625F, 0.6953125F}, {-2.0F, 5.0F, -3.0F, 0.1640625F, 0.734375F}, {2.0F, 5.0F, -3.0F, 0.1953125F, 0.734375F}},
                {{-2.0F, 0.0F, -3.0F, 0.1640625F, 0.6953125F}, {-2.0F, 0.0F, 0.0F, 0.140625F, 0.6953125F}, {-2.0F, 5.0F, 0.0F, 0.140625F, 0.734375F}, {-2.0F, 5.0F, -3.0F, 0.1640625F, 0.734375F}},
                {{-2.0F, 0.0F, 0.0F, 0.25F, 0.6953125F}, {2.0F, 0.0F, 0.0F, 0.21875F, 0.6953125F}, {2.0F, 5.0F, 0.0F, 0.21875F, 0.734375F}, {-2.0F, 5.0F, 0.0F, 0.25F, 0.734375F}},
                {{2.0F, 0.0F, 0.0F, 0.21875F, 0.6953125F}, {2.0F, 0.0F, -3.0F, 0.1953125F, 0.6953125F}, {2.0F, 5.0F, -3.0F, 0.1953125F, 0.734375F}, {2.0F, 5.0F, 0.0F, 0.21875F, 0.734375F}},
                {{-2.0F, 0.0F, -3.0F, 0.1640625F, 0.6953125F}, {2.0F, 0.0F, -3.0F, 0.1953125F, 0.6953125F}, {2.0F, 0.0F, 0.0F, 0.1953125F, 0.671875F}, {-2.0F, 0.0F, 0.0F, 0.1640625F, 0.671875F}},
                {{-2.0F, 5.0F, 0.0F, 0.1953125F, 0.671875F}, {2.0F, 5.0F, 0.0F, 0.2265625F, 0.671875F}, {2.0F, 5.0F, -3.0F, 0.2265625F, 0.6953125F}, {-2.0F, 5.0F, -3.0F, 0.1953125F, 0.6953125F}}
        }));
        var p17 = new net.minecraft.client.model.geom.ModelPart(p17Cubes, java.util.Map.ofEntries());
        p17.setInitialPose(PartPose.offsetAndRotation(-2.0F, -5.0F, 1.0F, 0.0F, 0.0F, 0.0F)); p17.resetPose();
        var p16Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/body/penguin/left_leg5", java.util.List.of()));
        p16Cubes.add(new ReferenceMeshCube(-1.0F, 0.0F, -3.0F, 4.0F, 5.0F, 3.0F, new float[][][] {
                {{3.0F, 0.0F, -3.0F, 0.1953125F, 0.6953125F}, {-1.0F, 0.0F, -3.0F, 0.1640625F, 0.6953125F}, {-1.0F, 5.0F, -3.0F, 0.1640625F, 0.734375F}, {3.0F, 5.0F, -3.0F, 0.1953125F, 0.734375F}},
                {{-1.0F, 0.0F, -3.0F, 0.1640625F, 0.6953125F}, {-1.0F, 0.0F, 0.0F, 0.140625F, 0.6953125F}, {-1.0F, 5.0F, 0.0F, 0.140625F, 0.734375F}, {-1.0F, 5.0F, -3.0F, 0.1640625F, 0.734375F}},
                {{-1.0F, 0.0F, 0.0F, 0.25F, 0.6953125F}, {3.0F, 0.0F, 0.0F, 0.21875F, 0.6953125F}, {3.0F, 5.0F, 0.0F, 0.21875F, 0.734375F}, {-1.0F, 5.0F, 0.0F, 0.25F, 0.734375F}},
                {{3.0F, 0.0F, 0.0F, 0.21875F, 0.6953125F}, {3.0F, 0.0F, -3.0F, 0.1953125F, 0.6953125F}, {3.0F, 5.0F, -3.0F, 0.1953125F, 0.734375F}, {3.0F, 5.0F, 0.0F, 0.21875F, 0.734375F}},
                {{-1.0F, 0.0F, -3.0F, 0.1640625F, 0.6953125F}, {3.0F, 0.0F, -3.0F, 0.1953125F, 0.6953125F}, {3.0F, 0.0F, 0.0F, 0.1953125F, 0.671875F}, {-1.0F, 0.0F, 0.0F, 0.1640625F, 0.671875F}},
                {{-1.0F, 5.0F, 0.0F, 0.1953125F, 0.671875F}, {3.0F, 5.0F, 0.0F, 0.2265625F, 0.671875F}, {3.0F, 5.0F, -3.0F, 0.2265625F, 0.6953125F}, {-1.0F, 5.0F, -3.0F, 0.1953125F, 0.6953125F}}
        }));
        var p16 = new net.minecraft.client.model.geom.ModelPart(p16Cubes, java.util.Map.ofEntries());
        p16.setInitialPose(PartPose.offsetAndRotation(1.0F, -5.0F, 1.0F, 0.0F, 0.0F, 0.0F)); p16.resetPose();
        var p15Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/body/penguin/right_wing3", java.util.List.of()));
        p15Cubes.add(new ReferenceMeshCube(-1.0F, 0.0F, -2.0F, 1.0F, 11.0F, 5.0F, new float[][][] {
                {{0.0F, 0.0F, -2.0F, 0.40625F, 0.53125F}, {-1.0F, 0.0F, -2.0F, 0.3984375F, 0.53125F}, {-1.0F, 11.0F, -2.0F, 0.3984375F, 0.6171875F}, {0.0F, 11.0F, -2.0F, 0.40625F, 0.6171875F}},
                {{-1.0F, 0.0F, -2.0F, 0.3984375F, 0.53125F}, {-1.0F, 0.0F, 3.0F, 0.359375F, 0.53125F}, {-1.0F, 11.0F, 3.0F, 0.359375F, 0.6171875F}, {-1.0F, 11.0F, -2.0F, 0.3984375F, 0.6171875F}},
                {{-1.0F, 0.0F, 3.0F, 0.453125F, 0.53125F}, {0.0F, 0.0F, 3.0F, 0.4453125F, 0.53125F}, {0.0F, 11.0F, 3.0F, 0.4453125F, 0.6171875F}, {-1.0F, 11.0F, 3.0F, 0.453125F, 0.6171875F}},
                {{0.0F, 0.0F, 3.0F, 0.4453125F, 0.53125F}, {0.0F, 0.0F, -2.0F, 0.40625F, 0.53125F}, {0.0F, 11.0F, -2.0F, 0.40625F, 0.6171875F}, {0.0F, 11.0F, 3.0F, 0.4453125F, 0.6171875F}},
                {{-1.0F, 0.0F, -2.0F, 0.3984375F, 0.53125F}, {0.0F, 0.0F, -2.0F, 0.40625F, 0.53125F}, {0.0F, 0.0F, 3.0F, 0.40625F, 0.4921875F}, {-1.0F, 0.0F, 3.0F, 0.3984375F, 0.4921875F}},
                {{-1.0F, 11.0F, 3.0F, 0.40625F, 0.4921875F}, {0.0F, 11.0F, 3.0F, 0.4140625F, 0.4921875F}, {0.0F, 11.0F, -2.0F, 0.4140625F, 0.53125F}, {-1.0F, 11.0F, -2.0F, 0.40625F, 0.53125F}}
        }));
        var p15 = new net.minecraft.client.model.geom.ModelPart(p15Cubes, java.util.Map.ofEntries());
        p15.setInitialPose(PartPose.offsetAndRotation(-5.0F, -18.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p15.resetPose();
        var p14Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/body/penguin/left_wing3", java.util.List.of()));
        p14Cubes.add(new ReferenceMeshCube(0.0F, 0.0F, -2.0F, 1.0F, 11.0F, 5.0F, new float[][][] {
                {{1.0F, 0.0F, -2.0F, 0.3125F, 0.53125F}, {0.0F, 0.0F, -2.0F, 0.3046875F, 0.53125F}, {0.0F, 11.0F, -2.0F, 0.3046875F, 0.6171875F}, {1.0F, 11.0F, -2.0F, 0.3125F, 0.6171875F}},
                {{0.0F, 0.0F, -2.0F, 0.3046875F, 0.53125F}, {0.0F, 0.0F, 3.0F, 0.265625F, 0.53125F}, {0.0F, 11.0F, 3.0F, 0.265625F, 0.6171875F}, {0.0F, 11.0F, -2.0F, 0.3046875F, 0.6171875F}},
                {{0.0F, 0.0F, 3.0F, 0.359375F, 0.53125F}, {1.0F, 0.0F, 3.0F, 0.3515625F, 0.53125F}, {1.0F, 11.0F, 3.0F, 0.3515625F, 0.6171875F}, {0.0F, 11.0F, 3.0F, 0.359375F, 0.6171875F}},
                {{1.0F, 0.0F, 3.0F, 0.3515625F, 0.53125F}, {1.0F, 0.0F, -2.0F, 0.3125F, 0.53125F}, {1.0F, 11.0F, -2.0F, 0.3125F, 0.6171875F}, {1.0F, 11.0F, 3.0F, 0.3515625F, 0.6171875F}},
                {{0.0F, 0.0F, -2.0F, 0.3046875F, 0.53125F}, {1.0F, 0.0F, -2.0F, 0.3125F, 0.53125F}, {1.0F, 0.0F, 3.0F, 0.3125F, 0.4921875F}, {0.0F, 0.0F, 3.0F, 0.3046875F, 0.4921875F}},
                {{0.0F, 11.0F, 3.0F, 0.3125F, 0.4921875F}, {1.0F, 11.0F, 3.0F, 0.3203125F, 0.4921875F}, {1.0F, 11.0F, -2.0F, 0.3203125F, 0.53125F}, {0.0F, 11.0F, -2.0F, 0.3125F, 0.53125F}}
        }));
        var p14 = new net.minecraft.client.model.geom.ModelPart(p14Cubes, java.util.Map.ofEntries());
        p14.setInitialPose(PartPose.offsetAndRotation(5.0F, -18.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p14.resetPose();
        var p13Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/body/penguin/body2", java.util.List.of()));
        p13Cubes.add(new ReferenceMeshCube(-5.0F, -12.0F, -3.0F, 10.0F, 16.0F, 7.0F, new float[][][] {
                {{5.0F, -12.0F, -3.0F, 0.1328125F, 0.546875F}, {-5.0F, -12.0F, -3.0F, 0.0546875F, 0.546875F}, {-5.0F, 4.0F, -3.0F, 0.0546875F, 0.671875F}, {5.0F, 4.0F, -3.0F, 0.1328125F, 0.671875F}},
                {{-5.0F, -12.0F, -3.0F, 0.0546875F, 0.546875F}, {-5.0F, -12.0F, 4.0F, 0.0F, 0.546875F}, {-5.0F, 4.0F, 4.0F, 0.0F, 0.671875F}, {-5.0F, 4.0F, -3.0F, 0.0546875F, 0.671875F}},
                {{-5.0F, -12.0F, 4.0F, 0.265625F, 0.546875F}, {5.0F, -12.0F, 4.0F, 0.1875F, 0.546875F}, {5.0F, 4.0F, 4.0F, 0.1875F, 0.671875F}, {-5.0F, 4.0F, 4.0F, 0.265625F, 0.671875F}},
                {{5.0F, -12.0F, 4.0F, 0.1875F, 0.546875F}, {5.0F, -12.0F, -3.0F, 0.1328125F, 0.546875F}, {5.0F, 4.0F, -3.0F, 0.1328125F, 0.671875F}, {5.0F, 4.0F, 4.0F, 0.1875F, 0.671875F}},
                {{-5.0F, -12.0F, -3.0F, 0.0546875F, 0.546875F}, {5.0F, -12.0F, -3.0F, 0.1328125F, 0.546875F}, {5.0F, -12.0F, 4.0F, 0.1328125F, 0.4921875F}, {-5.0F, -12.0F, 4.0F, 0.0546875F, 0.4921875F}},
                {{-5.0F, 4.0F, 4.0F, 0.1328125F, 0.4921875F}, {5.0F, 4.0F, 4.0F, 0.2109375F, 0.4921875F}, {5.0F, 4.0F, -3.0F, 0.2109375F, 0.546875F}, {-5.0F, 4.0F, -3.0F, 0.1328125F, 0.546875F}}
        }));
        p13Cubes.add(new ReferenceMeshCube(-2.0F, 2.0F, 4.0F, 4.0F, 2.0F, 4.0F, new float[][][] {
                {{2.0F, 2.0F, 4.0F, 0.328125F, 0.6484375F}, {-2.0F, 2.0F, 4.0F, 0.296875F, 0.6484375F}, {-2.0F, 4.0F, 4.0F, 0.296875F, 0.6640625F}, {2.0F, 4.0F, 4.0F, 0.328125F, 0.6640625F}},
                {{-2.0F, 2.0F, 4.0F, 0.296875F, 0.6484375F}, {-2.0F, 2.0F, 8.0F, 0.265625F, 0.6484375F}, {-2.0F, 4.0F, 8.0F, 0.265625F, 0.6640625F}, {-2.0F, 4.0F, 4.0F, 0.296875F, 0.6640625F}},
                {{-2.0F, 2.0F, 8.0F, 0.390625F, 0.6484375F}, {2.0F, 2.0F, 8.0F, 0.359375F, 0.6484375F}, {2.0F, 4.0F, 8.0F, 0.359375F, 0.6640625F}, {-2.0F, 4.0F, 8.0F, 0.390625F, 0.6640625F}},
                {{2.0F, 2.0F, 8.0F, 0.359375F, 0.6484375F}, {2.0F, 2.0F, 4.0F, 0.328125F, 0.6484375F}, {2.0F, 4.0F, 4.0F, 0.328125F, 0.6640625F}, {2.0F, 4.0F, 8.0F, 0.359375F, 0.6640625F}},
                {{-2.0F, 2.0F, 4.0F, 0.296875F, 0.6484375F}, {2.0F, 2.0F, 4.0F, 0.328125F, 0.6484375F}, {2.0F, 2.0F, 8.0F, 0.328125F, 0.6171875F}, {-2.0F, 2.0F, 8.0F, 0.296875F, 0.6171875F}},
                {{-2.0F, 4.0F, 8.0F, 0.328125F, 0.6171875F}, {2.0F, 4.0F, 8.0F, 0.359375F, 0.6171875F}, {2.0F, 4.0F, 4.0F, 0.359375F, 0.6484375F}, {-2.0F, 4.0F, 4.0F, 0.328125F, 0.6484375F}}
        }));
        var p13 = new net.minecraft.client.model.geom.ModelPart(p13Cubes, java.util.Map.ofEntries());
        p13.setInitialPose(PartPose.offsetAndRotation(0.0F, -8.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p13.resetPose();
        var p12Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/body/penguin/head3/body_sub_32", java.util.List.of()));
        p12Cubes.add(new ReferenceMeshCube(-2.0F, -7.0F, -2.0F, 0.0F, 2.0F, 5.0F, new float[][][] {
                {{-2.0F, -7.0F, -2.0F, 0.25F, 0.515625F}, {-2.0F, -7.0F, -2.0F, 0.25F, 0.515625F}, {-2.0F, -5.0F, -2.0F, 0.25F, 0.53125F}, {-2.0F, -5.0F, -2.0F, 0.25F, 0.53125F}},
                {{-2.0F, -7.0F, -2.0F, 0.25F, 0.515625F}, {-2.0F, -7.0F, 3.0F, 0.2890625F, 0.515625F}, {-2.0F, -5.0F, 3.0F, 0.2890625F, 0.53125F}, {-2.0F, -5.0F, -2.0F, 0.25F, 0.53125F}},
                {{-2.0F, -7.0F, 3.0F, 0.2890625F, 0.515625F}, {-2.0F, -7.0F, 3.0F, 0.2890625F, 0.515625F}, {-2.0F, -5.0F, 3.0F, 0.2890625F, 0.53125F}, {-2.0F, -5.0F, 3.0F, 0.2890625F, 0.53125F}},
                {{-2.0F, -7.0F, 3.0F, 0.2109375F, 0.515625F}, {-2.0F, -7.0F, -2.0F, 0.25F, 0.515625F}, {-2.0F, -5.0F, -2.0F, 0.25F, 0.53125F}, {-2.0F, -5.0F, 3.0F, 0.2109375F, 0.53125F}},
                {{-2.0F, -7.0F, -2.0F, 0.25F, 0.515625F}, {-2.0F, -7.0F, -2.0F, 0.25F, 0.515625F}, {-2.0F, -7.0F, 3.0F, 0.25F, 0.4765625F}, {-2.0F, -7.0F, 3.0F, 0.25F, 0.4765625F}},
                {{-2.0F, -5.0F, 3.0F, 0.25F, 0.4765625F}, {-2.0F, -5.0F, 3.0F, 0.25F, 0.4765625F}, {-2.0F, -5.0F, -2.0F, 0.25F, 0.515625F}, {-2.0F, -5.0F, -2.0F, 0.25F, 0.515625F}}
        }));
        var p12 = new net.minecraft.client.model.geom.ModelPart(p12Cubes, java.util.Map.ofEntries());
        p12.setInitialPose(PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p12.resetPose();
        var p11Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/body/penguin/head3", java.util.List.of()));
        p11Cubes.add(new ReferenceMeshCube(-2.0F, -5.0F, -3.0F, 4.0F, 5.0F, 5.0F, new float[][][] {
                {{2.0F, -5.0F, -3.0F, 0.0703125F, 0.7109375F}, {-2.0F, -5.0F, -3.0F, 0.0390625F, 0.7109375F}, {-2.0F, 0.0F, -3.0F, 0.0390625F, 0.75F}, {2.0F, 0.0F, -3.0F, 0.0703125F, 0.75F}},
                {{-2.0F, -5.0F, -3.0F, 0.0390625F, 0.7109375F}, {-2.0F, -5.0F, 2.0F, 0.0F, 0.7109375F}, {-2.0F, 0.0F, 2.0F, 0.0F, 0.75F}, {-2.0F, 0.0F, -3.0F, 0.0390625F, 0.75F}},
                {{-2.0F, -5.0F, 2.0F, 0.140625F, 0.7109375F}, {2.0F, -5.0F, 2.0F, 0.109375F, 0.7109375F}, {2.0F, 0.0F, 2.0F, 0.109375F, 0.75F}, {-2.0F, 0.0F, 2.0F, 0.140625F, 0.75F}},
                {{2.0F, -5.0F, 2.0F, 0.109375F, 0.7109375F}, {2.0F, -5.0F, -3.0F, 0.0703125F, 0.7109375F}, {2.0F, 0.0F, -3.0F, 0.0703125F, 0.75F}, {2.0F, 0.0F, 2.0F, 0.109375F, 0.75F}},
                {{-2.0F, -5.0F, -3.0F, 0.0390625F, 0.7109375F}, {2.0F, -5.0F, -3.0F, 0.0703125F, 0.7109375F}, {2.0F, -5.0F, 2.0F, 0.0703125F, 0.671875F}, {-2.0F, -5.0F, 2.0F, 0.0390625F, 0.671875F}},
                {{-2.0F, 0.0F, 2.0F, 0.0703125F, 0.671875F}, {2.0F, 0.0F, 2.0F, 0.1015625F, 0.671875F}, {2.0F, 0.0F, -3.0F, 0.1015625F, 0.7109375F}, {-2.0F, 0.0F, -3.0F, 0.0703125F, 0.7109375F}}
        }));
        p11Cubes.add(new ReferenceMeshCube(2.0F, -7.0F, -2.0F, 0.0F, 2.0F, 5.0F, new float[][][] {
                {{2.0F, -7.0F, -2.0F, 0.25F, 0.515625F}, {2.0F, -7.0F, -2.0F, 0.25F, 0.515625F}, {2.0F, -5.0F, -2.0F, 0.25F, 0.53125F}, {2.0F, -5.0F, -2.0F, 0.25F, 0.53125F}},
                {{2.0F, -7.0F, -2.0F, 0.25F, 0.515625F}, {2.0F, -7.0F, 3.0F, 0.2109375F, 0.515625F}, {2.0F, -5.0F, 3.0F, 0.2109375F, 0.53125F}, {2.0F, -5.0F, -2.0F, 0.25F, 0.53125F}},
                {{2.0F, -7.0F, 3.0F, 0.2890625F, 0.515625F}, {2.0F, -7.0F, 3.0F, 0.2890625F, 0.515625F}, {2.0F, -5.0F, 3.0F, 0.2890625F, 0.53125F}, {2.0F, -5.0F, 3.0F, 0.2890625F, 0.53125F}},
                {{2.0F, -7.0F, 3.0F, 0.2890625F, 0.515625F}, {2.0F, -7.0F, -2.0F, 0.25F, 0.515625F}, {2.0F, -5.0F, -2.0F, 0.25F, 0.53125F}, {2.0F, -5.0F, 3.0F, 0.2890625F, 0.53125F}},
                {{2.0F, -7.0F, -2.0F, 0.25F, 0.515625F}, {2.0F, -7.0F, -2.0F, 0.25F, 0.515625F}, {2.0F, -7.0F, 3.0F, 0.25F, 0.4765625F}, {2.0F, -7.0F, 3.0F, 0.25F, 0.4765625F}},
                {{2.0F, -5.0F, 3.0F, 0.25F, 0.4765625F}, {2.0F, -5.0F, 3.0F, 0.25F, 0.4765625F}, {2.0F, -5.0F, -2.0F, 0.25F, 0.515625F}, {2.0F, -5.0F, -2.0F, 0.25F, 0.515625F}}
        }));
        p11Cubes.add(new ReferenceMeshCube(-1.0F, -5.0F, -7.0F, 2.0F, 2.0F, 4.0F, new float[][][] {
                {{1.0F, -5.0F, -7.0F, 0.296875F, 0.6953125F}, {-1.0F, -5.0F, -7.0F, 0.28125F, 0.6953125F}, {-1.0F, -3.0F, -7.0F, 0.28125F, 0.7109375F}, {1.0F, -3.0F, -7.0F, 0.296875F, 0.7109375F}},
                {{-1.0F, -5.0F, -7.0F, 0.28125F, 0.6953125F}, {-1.0F, -5.0F, -3.0F, 0.25F, 0.6953125F}, {-1.0F, -3.0F, -3.0F, 0.25F, 0.7109375F}, {-1.0F, -3.0F, -7.0F, 0.28125F, 0.7109375F}},
                {{-1.0F, -5.0F, -3.0F, 0.34375F, 0.6953125F}, {1.0F, -5.0F, -3.0F, 0.328125F, 0.6953125F}, {1.0F, -3.0F, -3.0F, 0.328125F, 0.7109375F}, {-1.0F, -3.0F, -3.0F, 0.34375F, 0.7109375F}},
                {{1.0F, -5.0F, -3.0F, 0.328125F, 0.6953125F}, {1.0F, -5.0F, -7.0F, 0.296875F, 0.6953125F}, {1.0F, -3.0F, -7.0F, 0.296875F, 0.7109375F}, {1.0F, -3.0F, -3.0F, 0.328125F, 0.7109375F}},
                {{-1.0F, -5.0F, -7.0F, 0.28125F, 0.6953125F}, {1.0F, -5.0F, -7.0F, 0.296875F, 0.6953125F}, {1.0F, -5.0F, -3.0F, 0.296875F, 0.6640625F}, {-1.0F, -5.0F, -3.0F, 0.28125F, 0.6640625F}},
                {{-1.0F, -3.0F, -3.0F, 0.296875F, 0.6640625F}, {1.0F, -3.0F, -3.0F, 0.3125F, 0.6640625F}, {1.0F, -3.0F, -7.0F, 0.3125F, 0.6953125F}, {-1.0F, -3.0F, -7.0F, 0.296875F, 0.6953125F}}
        }));
        var p11 = new net.minecraft.client.model.geom.ModelPart(p11Cubes, java.util.Map.ofEntries(java.util.Map.entry("body_sub_32", p12)));
        p11.setInitialPose(PartPose.offsetAndRotation(0.0F, -20.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p11.resetPose();
        var p10Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/body/penguin", java.util.List.of()));
        var p10 = new net.minecraft.client.model.geom.ModelPart(p10Cubes, java.util.Map.ofEntries(java.util.Map.entry("head3", p11), java.util.Map.entry("body2", p13), java.util.Map.entry("left_wing3", p14), java.util.Map.entry("right_wing3", p15), java.util.Map.entry("left_leg5", p16), java.util.Map.entry("right_leg5", p17)));
        p10.setInitialPose(PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p10.resetPose();
        var p9Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/body", java.util.List.of()));
        var p9 = new net.minecraft.client.model.geom.ModelPart(p9Cubes, java.util.Map.ofEntries(java.util.Map.entry("penguin", p10)));
        p9.setInitialPose(PartPose.offsetAndRotation(0.0F, 8.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p9.resetPose();
        var p8Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_head/head", java.util.List.of()));
        var p8 = new net.minecraft.client.model.geom.ModelPart(p8Cubes, java.util.Map.ofEntries());
        p8.setInitialPose(PartPose.offsetAndRotation(0.0F, 9.0F, 4.0F, 0.0F, 0.0F, 0.0F)); p8.resetPose();
        var p7Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_chin", java.util.List.of()));
        var p7 = new net.minecraft.client.model.geom.ModelPart(p7Cubes, java.util.Map.ofEntries(java.util.Map.entry("chin", p23)));
        p7.setInitialPose(nativeRoot.getChild("red_thing").storePose()); p7.resetPose();
        var p6Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_bill", java.util.List.of()));
        var p6 = new net.minecraft.client.model.geom.ModelPart(p6Cubes, java.util.Map.ofEntries(java.util.Map.entry("bill", p22)));
        p6.setInitialPose(nativeRoot.getChild("beak").storePose()); p6.resetPose();
        var p5Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_left_wing", java.util.List.of()));
        var p5 = new net.minecraft.client.model.geom.ModelPart(p5Cubes, java.util.Map.ofEntries(java.util.Map.entry("left_wing", p21)));
        p5.setInitialPose(nativeRoot.getChild("left_wing").storePose()); p5.resetPose();
        var p4Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_right_wing", java.util.List.of()));
        var p4 = new net.minecraft.client.model.geom.ModelPart(p4Cubes, java.util.Map.ofEntries(java.util.Map.entry("right_wing", p20)));
        p4.setInitialPose(nativeRoot.getChild("right_wing").storePose()); p4.resetPose();
        var p3Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_left_leg", java.util.List.of()));
        var p3 = new net.minecraft.client.model.geom.ModelPart(p3Cubes, java.util.Map.ofEntries(java.util.Map.entry("left_leg", p19)));
        p3.setInitialPose(nativeRoot.getChild("left_leg").storePose()); p3.resetPose();
        var p2Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_right_leg", java.util.List.of()));
        var p2 = new net.minecraft.client.model.geom.ModelPart(p2Cubes, java.util.Map.ofEntries(java.util.Map.entry("right_leg", p18)));
        p2.setInitialPose(nativeRoot.getChild("right_leg").storePose()); p2.resetPose();
        var p1Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body", java.util.List.of()));
        var p1 = new net.minecraft.client.model.geom.ModelPart(p1Cubes, java.util.Map.ofEntries(java.util.Map.entry("body", p9)));
        p1.setInitialPose(nativeRoot.getChild("body").storePose()); p1.resetPose();
        var p0Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_head", java.util.List.of()));
        var p0 = new net.minecraft.client.model.geom.ModelPart(p0Cubes, java.util.Map.ofEntries(java.util.Map.entry("head", p8)));
        p0.setInitialPose(nativeRoot.getChild("head").storePose()); p0.resetPose();
        return new net.minecraft.client.model.geom.ModelPart(java.util.List.of(), java.util.Map.ofEntries(java.util.Map.entry("__cem_host_head", p0), java.util.Map.entry("__cem_host_body", p1), java.util.Map.entry("__cem_host_right_leg", p2), java.util.Map.entry("__cem_host_left_leg", p3), java.util.Map.entry("__cem_host_right_wing", p4), java.util.Map.entry("__cem_host_left_wing", p5), java.util.Map.entry("__cem_host_bill", p6), java.util.Map.entry("__cem_host_chin", p7)));
    }
}
