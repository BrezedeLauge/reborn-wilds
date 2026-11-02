/*
 * Generated adaptation of ReCrafted Mobs 11.0 by GillsBills and credited contributors.
 * Authored asset license: CC-BY-NC-SA-4.0; https://creativecommons.org/licenses/by-nc-sa/4.0/
 * Source: https://modrinth.com/resourcepack/recrafted-mobs
 * Model: assets/minecraft/optifine/cem/wandering_trader/wandering_trader5.jem
 * Source SHA256: cee1cb8a21bdd489940073ee51a1f693fb746237a051c7cda57d3f718c2fecd5
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

public final class CemVillageMerchantGeometry {
    private CemVillageMerchantGeometry() {}

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        PartDefinition p0 = root.addOrReplaceChild("__cem_host_head", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p1 = p0.addOrReplaceChild("__cem_host_headwear", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p2 = p1.addOrReplaceChild("__cem_host_headwear2", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p3 = p0.addOrReplaceChild("__cem_host_nose", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p4 = root.addOrReplaceChild("__cem_host_body", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p5 = p4.addOrReplaceChild("__cem_host_bodywear", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p6 = root.addOrReplaceChild("__cem_host_arms", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p7 = root.addOrReplaceChild("__cem_host_right_leg", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p8 = root.addOrReplaceChild("__cem_host_left_leg", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p9 = p0.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 24.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p10 = p3.addOrReplaceChild("nose", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 26.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p11 = p1.addOrReplaceChild("headwear", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 24.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p12 = p11.addOrReplaceChild("flerp", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -33.4F, 4.5F, 0.610865238F, 0.0F, 0.0F));
        PartDefinition p13 = p2.addOrReplaceChild("headwear2", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 24.0F, 0.0F, -1.57079633F, 0.0F, 0.0F));
        PartDefinition p14 = p4.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 24.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p15 = p5.addOrReplaceChild("bodywear", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 24.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p16 = p6.addOrReplaceChild("arms", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 21.05F, 1.05F, -0.750491578F, 0.0F, 0.0F));
        PartDefinition p17 = p16.addOrReplaceChild("mirrored", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p18 = p16.addOrReplaceChild("glass", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -21.05F, -1.05F, 0.0F, 0.0F, 0.0F));
        PartDefinition p19 = p7.addOrReplaceChild("right_leg", CubeListBuilder.create(), PartPose.offsetAndRotation(2.0F, 12.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p20 = p8.addOrReplaceChild("left_leg", CubeListBuilder.create(), PartPose.offsetAndRotation(-2.0F, 12.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        return LayerDefinition.create(mesh, 64, 64);
    }

    /** Preserve source expression units; static scales are reapplied after every pose reset. */
    public static ReferenceMobDefinition.Builder configure(ReferenceMobDefinition.Builder builder) {
        builder.cemDriver("wandering_trader");
        builder.bakedRoot(CemVillageMerchantGeometry::bakeRoot);
        builder.hostAlias("head", "__cem_host_head");
        builder.hostAlias("headwear", "__cem_host_headwear");
        builder.hostAlias("hat", "__cem_host_headwear");
        builder.hostAlias("headwear2", "__cem_host_headwear2");
        builder.hostAlias("nose", "__cem_host_nose");
        builder.hostAlias("body", "__cem_host_body");
        builder.hostAlias("bodywear", "__cem_host_bodywear");
        builder.hostAlias("arms", "__cem_host_arms");
        builder.hostAlias("right_leg", "__cem_host_right_leg");
        builder.hostAlias("left_leg", "__cem_host_left_leg");
        builder.expression("EMF_headwear2.rx", "0");
        builder.expression("EMF_arms.rx", "0");
        return builder;
    }

    /** Complete matching rig; installed cubes stay owned by the Minecraft client. */
    public static net.minecraft.client.model.geom.ModelPart bakeRoot() {
        var nativeRoot = LayerDefinition.create(net.minecraft.client.model.VillagerModel.createBodyModel(), 64, 64).bakeRoot();
        var nativeCubes = new java.util.HashMap<String, java.util.List<net.minecraft.client.model.geom.ModelPart.Cube>>();
        nativeRoot.visit(new com.mojang.blaze3d.vertex.PoseStack(),
                (pose, path, index, cube) -> nativeCubes.computeIfAbsent(path, key -> new java.util.ArrayList<>()).add(cube));
        var customCubes = new java.util.HashMap<String, java.util.List<net.minecraft.client.model.geom.ModelPart.Cube>>();
        createBodyLayer().bakeRoot().visit(new com.mojang.blaze3d.vertex.PoseStack(),
                (pose, path, index, cube) -> customCubes.computeIfAbsent(path, key -> new java.util.ArrayList<>()).add(cube));
        var p20Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_left_leg/left_leg", java.util.List.of()));
        p20Cubes.add(new ReferenceMeshCube(0.0F, -12.0F, -2.0F, 4.0F, 12.0F, 4.0F, new float[][][] {
                {{4.0F, -12.0F, 2.0F, 0.296875F, 0.203125F}, {0.0F, -12.0F, 2.0F, 0.28125F, 0.203125F}, {0.0F, -12.0F, -2.0F, 0.28125F, 0.234375F}, {4.0F, -12.0F, -2.0F, 0.296875F, 0.234375F}},
                {{4.0F, 0.0F, -2.0F, 0.3125F, 0.234375F}, {0.0F, 0.0F, -2.0F, 0.296875F, 0.234375F}, {0.0F, 0.0F, 2.0F, 0.296875F, 0.203125F}, {4.0F, 0.0F, 2.0F, 0.3125F, 0.203125F}},
                {{0.0F, -12.0F, -2.0F, 0.28125F, 0.234375F}, {0.0F, -12.0F, 2.0F, 0.265625F, 0.234375F}, {0.0F, 0.0F, 2.0F, 0.265625F, 0.328125F}, {0.0F, 0.0F, -2.0F, 0.28125F, 0.328125F}},
                {{4.0F, -12.0F, 2.0F, 0.3125F, 0.234375F}, {4.0F, -12.0F, -2.0F, 0.296875F, 0.234375F}, {4.0F, 0.0F, -2.0F, 0.296875F, 0.328125F}, {4.0F, 0.0F, 2.0F, 0.3125F, 0.328125F}},
                {{4.0F, -12.0F, -2.0F, 0.296875F, 0.234375F}, {0.0F, -12.0F, -2.0F, 0.28125F, 0.234375F}, {0.0F, 0.0F, -2.0F, 0.28125F, 0.328125F}, {4.0F, 0.0F, -2.0F, 0.296875F, 0.328125F}},
                {{0.0F, -12.0F, 2.0F, 0.328125F, 0.234375F}, {4.0F, -12.0F, 2.0F, 0.3125F, 0.234375F}, {4.0F, 0.0F, 2.0F, 0.3125F, 0.328125F}, {0.0F, 0.0F, 2.0F, 0.328125F, 0.328125F}}
        }));
        var p20 = new net.minecraft.client.model.geom.ModelPart(p20Cubes, java.util.Map.ofEntries());
        p20.setInitialPose(PartPose.offsetAndRotation(-2.0F, 12.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p20.resetPose();
        var p19Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_right_leg/right_leg", java.util.List.of()));
        p19Cubes.add(new ReferenceMeshCube(-4.0F, -12.0F, -2.0F, 4.0F, 12.0F, 4.0F, new float[][][] {
                {{0.0F, -12.0F, 2.0F, 0.296875F, 0.203125F}, {-4.0F, -12.0F, 2.0F, 0.28125F, 0.203125F}, {-4.0F, -12.0F, -2.0F, 0.28125F, 0.234375F}, {0.0F, -12.0F, -2.0F, 0.296875F, 0.234375F}},
                {{0.0F, 0.0F, -2.0F, 0.3125F, 0.234375F}, {-4.0F, 0.0F, -2.0F, 0.296875F, 0.234375F}, {-4.0F, 0.0F, 2.0F, 0.296875F, 0.203125F}, {0.0F, 0.0F, 2.0F, 0.3125F, 0.203125F}},
                {{-4.0F, -12.0F, -2.0F, 0.28125F, 0.234375F}, {-4.0F, -12.0F, 2.0F, 0.265625F, 0.234375F}, {-4.0F, 0.0F, 2.0F, 0.265625F, 0.328125F}, {-4.0F, 0.0F, -2.0F, 0.28125F, 0.328125F}},
                {{0.0F, -12.0F, 2.0F, 0.3125F, 0.234375F}, {0.0F, -12.0F, -2.0F, 0.296875F, 0.234375F}, {0.0F, 0.0F, -2.0F, 0.296875F, 0.328125F}, {0.0F, 0.0F, 2.0F, 0.3125F, 0.328125F}},
                {{0.0F, -12.0F, -2.0F, 0.296875F, 0.234375F}, {-4.0F, -12.0F, -2.0F, 0.28125F, 0.234375F}, {-4.0F, 0.0F, -2.0F, 0.28125F, 0.328125F}, {0.0F, 0.0F, -2.0F, 0.296875F, 0.328125F}},
                {{-4.0F, -12.0F, 2.0F, 0.328125F, 0.234375F}, {0.0F, -12.0F, 2.0F, 0.3125F, 0.234375F}, {0.0F, 0.0F, 2.0F, 0.3125F, 0.328125F}, {-4.0F, 0.0F, 2.0F, 0.328125F, 0.328125F}}
        }));
        var p19 = new net.minecraft.client.model.geom.ModelPart(p19Cubes, java.util.Map.ofEntries());
        p19.setInitialPose(PartPose.offsetAndRotation(2.0F, 12.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p19.resetPose();
        var p18Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_arms/arms/glass", java.util.List.of()));
        p18Cubes.add(new ReferenceMeshCube(-5.0F, 3.0F, -11.5F, 10.0F, 2.0F, 10.0F, new float[][][] {
                {{5.0F, 3.0F, -1.5F, 0.625F, 0.03125F}, {-5.0F, 3.0F, -1.5F, 0.5859375F, 0.03125F}, {-5.0F, 3.0F, -11.5F, 0.5859375F, 0.109375F}, {5.0F, 3.0F, -11.5F, 0.625F, 0.109375F}},
                {{5.0F, 5.0F, -11.5F, 0.6640625F, 0.109375F}, {-5.0F, 5.0F, -11.5F, 0.625F, 0.109375F}, {-5.0F, 5.0F, -1.5F, 0.625F, 0.03125F}, {5.0F, 5.0F, -1.5F, 0.6640625F, 0.03125F}},
                {{-5.0F, 3.0F, -11.5F, 0.5859375F, 0.109375F}, {-5.0F, 3.0F, -1.5F, 0.546875F, 0.109375F}, {-5.0F, 5.0F, -1.5F, 0.546875F, 0.125F}, {-5.0F, 5.0F, -11.5F, 0.5859375F, 0.125F}},
                {{5.0F, 3.0F, -1.5F, 0.6640625F, 0.109375F}, {5.0F, 3.0F, -11.5F, 0.625F, 0.109375F}, {5.0F, 5.0F, -11.5F, 0.625F, 0.125F}, {5.0F, 5.0F, -1.5F, 0.6640625F, 0.125F}},
                {{5.0F, 3.0F, -11.5F, 0.625F, 0.109375F}, {-5.0F, 3.0F, -11.5F, 0.5859375F, 0.109375F}, {-5.0F, 5.0F, -11.5F, 0.5859375F, 0.125F}, {5.0F, 5.0F, -11.5F, 0.625F, 0.125F}},
                {{-5.0F, 3.0F, -1.5F, 0.703125F, 0.109375F}, {5.0F, 3.0F, -1.5F, 0.6640625F, 0.109375F}, {5.0F, 5.0F, -1.5F, 0.6640625F, 0.125F}, {-5.0F, 5.0F, -1.5F, 0.703125F, 0.125F}}
        }));
        var p18 = new net.minecraft.client.model.geom.ModelPart(p18Cubes, java.util.Map.ofEntries());
        p18.setInitialPose(PartPose.offsetAndRotation(0.0F, -21.05F, -1.05F, 0.0F, 0.0F, 0.0F)); p18.resetPose();
        var p17Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_arms/arms/mirrored", java.util.List.of()));
        p17Cubes.add(new ReferenceMeshCube(4.0F, -23.05F, -3.05F, 4.0F, 8.0F, 4.0F, new float[][][] {
                {{4.0F, -23.05F, -3.05F, 0.46875F, 0.234375F}, {8.0F, -23.05F, -3.05F, 0.453125F, 0.234375F}, {8.0F, -23.05F, 0.95F, 0.453125F, 0.203125F}, {4.0F, -23.05F, 0.95F, 0.46875F, 0.203125F}},
                {{4.0F, -15.05F, 0.95F, 0.484375F, 0.203125F}, {8.0F, -15.05F, 0.95F, 0.46875F, 0.203125F}, {8.0F, -15.05F, -3.05F, 0.46875F, 0.234375F}, {4.0F, -15.05F, -3.05F, 0.484375F, 0.234375F}},
                {{8.0F, -15.05F, -3.05F, 0.453125F, 0.296875F}, {8.0F, -15.05F, 0.95F, 0.4375F, 0.296875F}, {8.0F, -23.05F, 0.95F, 0.4375F, 0.234375F}, {8.0F, -23.05F, -3.05F, 0.453125F, 0.234375F}},
                {{4.0F, -15.05F, 0.95F, 0.484375F, 0.296875F}, {4.0F, -15.05F, -3.05F, 0.46875F, 0.296875F}, {4.0F, -23.05F, -3.05F, 0.46875F, 0.234375F}, {4.0F, -23.05F, 0.95F, 0.484375F, 0.234375F}},
                {{4.0F, -15.05F, -3.05F, 0.46875F, 0.296875F}, {8.0F, -15.05F, -3.05F, 0.453125F, 0.296875F}, {8.0F, -23.05F, -3.05F, 0.453125F, 0.234375F}, {4.0F, -23.05F, -3.05F, 0.46875F, 0.234375F}},
                {{8.0F, -15.05F, 0.95F, 0.5F, 0.296875F}, {4.0F, -15.05F, 0.95F, 0.484375F, 0.296875F}, {4.0F, -23.05F, 0.95F, 0.484375F, 0.234375F}, {8.0F, -23.05F, 0.95F, 0.5F, 0.234375F}}
        }));
        var p17 = new net.minecraft.client.model.geom.ModelPart(p17Cubes, java.util.Map.ofEntries());
        p17.setInitialPose(PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p17.resetPose();
        var p16Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_arms/arms", java.util.List.of()));
        p16Cubes.add(new ReferenceMeshCube(-4.0F, -19.05F, -3.05F, 8.0F, 4.0F, 4.0F, new float[][][] {
                {{4.0F, -19.05F, 0.95F, 0.46875F, 0.328125F}, {-4.0F, -19.05F, 0.95F, 0.4375F, 0.328125F}, {-4.0F, -19.05F, -3.05F, 0.4375F, 0.359375F}, {4.0F, -19.05F, -3.05F, 0.46875F, 0.359375F}},
                {{4.0F, -15.05F, -3.05F, 0.5F, 0.359375F}, {-4.0F, -15.05F, -3.05F, 0.46875F, 0.359375F}, {-4.0F, -15.05F, 0.95F, 0.46875F, 0.328125F}, {4.0F, -15.05F, 0.95F, 0.5F, 0.328125F}},
                {{-4.0F, -19.05F, -3.05F, 0.4375F, 0.359375F}, {-4.0F, -19.05F, 0.95F, 0.421875F, 0.359375F}, {-4.0F, -15.05F, 0.95F, 0.421875F, 0.390625F}, {-4.0F, -15.05F, -3.05F, 0.4375F, 0.390625F}},
                {{4.0F, -19.05F, 0.95F, 0.484375F, 0.359375F}, {4.0F, -19.05F, -3.05F, 0.46875F, 0.359375F}, {4.0F, -15.05F, -3.05F, 0.46875F, 0.390625F}, {4.0F, -15.05F, 0.95F, 0.484375F, 0.390625F}},
                {{4.0F, -19.05F, -3.05F, 0.46875F, 0.359375F}, {-4.0F, -19.05F, -3.05F, 0.4375F, 0.359375F}, {-4.0F, -15.05F, -3.05F, 0.4375F, 0.390625F}, {4.0F, -15.05F, -3.05F, 0.46875F, 0.390625F}},
                {{-4.0F, -19.05F, 0.95F, 0.515625F, 0.359375F}, {4.0F, -19.05F, 0.95F, 0.484375F, 0.359375F}, {4.0F, -15.05F, 0.95F, 0.484375F, 0.390625F}, {-4.0F, -15.05F, 0.95F, 0.515625F, 0.390625F}}
        }));
        p16Cubes.add(new ReferenceMeshCube(-8.0F, -23.05F, -3.05F, 4.0F, 8.0F, 4.0F, new float[][][] {
                {{-4.0F, -23.05F, 0.95F, 0.46875F, 0.203125F}, {-8.0F, -23.05F, 0.95F, 0.453125F, 0.203125F}, {-8.0F, -23.05F, -3.05F, 0.453125F, 0.234375F}, {-4.0F, -23.05F, -3.05F, 0.46875F, 0.234375F}},
                {{-4.0F, -15.05F, -3.05F, 0.484375F, 0.234375F}, {-8.0F, -15.05F, -3.05F, 0.46875F, 0.234375F}, {-8.0F, -15.05F, 0.95F, 0.46875F, 0.203125F}, {-4.0F, -15.05F, 0.95F, 0.484375F, 0.203125F}},
                {{-8.0F, -23.05F, -3.05F, 0.453125F, 0.234375F}, {-8.0F, -23.05F, 0.95F, 0.4375F, 0.234375F}, {-8.0F, -15.05F, 0.95F, 0.4375F, 0.296875F}, {-8.0F, -15.05F, -3.05F, 0.453125F, 0.296875F}},
                {{-4.0F, -23.05F, 0.95F, 0.484375F, 0.234375F}, {-4.0F, -23.05F, -3.05F, 0.46875F, 0.234375F}, {-4.0F, -15.05F, -3.05F, 0.46875F, 0.296875F}, {-4.0F, -15.05F, 0.95F, 0.484375F, 0.296875F}},
                {{-4.0F, -23.05F, -3.05F, 0.46875F, 0.234375F}, {-8.0F, -23.05F, -3.05F, 0.453125F, 0.234375F}, {-8.0F, -15.05F, -3.05F, 0.453125F, 0.296875F}, {-4.0F, -15.05F, -3.05F, 0.46875F, 0.296875F}},
                {{-8.0F, -23.05F, 0.95F, 0.5F, 0.234375F}, {-4.0F, -23.05F, 0.95F, 0.484375F, 0.234375F}, {-4.0F, -15.05F, 0.95F, 0.484375F, 0.296875F}, {-8.0F, -15.05F, 0.95F, 0.5F, 0.296875F}}
        }));
        var p16 = new net.minecraft.client.model.geom.ModelPart(p16Cubes, java.util.Map.ofEntries(java.util.Map.entry("mirrored", p17), java.util.Map.entry("glass", p18)));
        p16.setInitialPose(PartPose.offsetAndRotation(0.0F, 21.05F, 1.05F, -0.750491578F, 0.0F, 0.0F)); p16.resetPose();
        var p15Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/__cem_host_bodywear/bodywear", java.util.List.of()));
        p15Cubes.add(new ReferenceMeshCube(-4.5F, -24.5F, -3.5F, 9.0F, 21.0F, 7.0F, new float[][][] {
                {{4.5F, -24.5F, 3.5F, 0.3203125F, 0.328125F}, {-4.5F, -24.5F, 3.5F, 0.2890625F, 0.328125F}, {-4.5F, -24.5F, -3.5F, 0.2890625F, 0.375F}, {4.5F, -24.5F, -3.5F, 0.3203125F, 0.375F}},
                {{4.5F, -3.5F, -3.5F, 0.3515625F, 0.375F}, {-4.5F, -3.5F, -3.5F, 0.3203125F, 0.375F}, {-4.5F, -3.5F, 3.5F, 0.3203125F, 0.328125F}, {4.5F, -3.5F, 3.5F, 0.3515625F, 0.328125F}},
                {{-4.5F, -24.5F, -3.5F, 0.2890625F, 0.375F}, {-4.5F, -24.5F, 3.5F, 0.265625F, 0.375F}, {-4.5F, -3.5F, 3.5F, 0.265625F, 0.53125F}, {-4.5F, -3.5F, -3.5F, 0.2890625F, 0.53125F}},
                {{4.5F, -24.5F, 3.5F, 0.34375F, 0.375F}, {4.5F, -24.5F, -3.5F, 0.3203125F, 0.375F}, {4.5F, -3.5F, -3.5F, 0.3203125F, 0.53125F}, {4.5F, -3.5F, 3.5F, 0.34375F, 0.53125F}},
                {{4.5F, -24.5F, -3.5F, 0.3203125F, 0.375F}, {-4.5F, -24.5F, -3.5F, 0.2890625F, 0.375F}, {-4.5F, -3.5F, -3.5F, 0.2890625F, 0.53125F}, {4.5F, -3.5F, -3.5F, 0.3203125F, 0.53125F}},
                {{-4.5F, -24.5F, 3.5F, 0.375F, 0.375F}, {4.5F, -24.5F, 3.5F, 0.34375F, 0.375F}, {4.5F, -3.5F, 3.5F, 0.34375F, 0.53125F}, {-4.5F, -3.5F, 3.5F, 0.375F, 0.53125F}}
        }));
        var p15 = new net.minecraft.client.model.geom.ModelPart(p15Cubes, java.util.Map.ofEntries());
        p15.setInitialPose(PartPose.offsetAndRotation(0.0F, 24.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p15.resetPose();
        var p14Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/body", java.util.List.of()));
        p14Cubes.add(new ReferenceMeshCube(-4.0F, -24.0F, -3.0F, 8.0F, 12.0F, 6.0F, new float[][][] {
                {{4.0F, -24.0F, 3.0F, 0.3828125F, 0.1875F}, {-4.0F, -24.0F, 3.0F, 0.3515625F, 0.1875F}, {-4.0F, -24.0F, -3.0F, 0.3515625F, 0.234375F}, {4.0F, -24.0F, -3.0F, 0.3828125F, 0.234375F}},
                {{4.0F, -12.0F, -3.0F, 0.4140625F, 0.234375F}, {-4.0F, -12.0F, -3.0F, 0.3828125F, 0.234375F}, {-4.0F, -12.0F, 3.0F, 0.3828125F, 0.1875F}, {4.0F, -12.0F, 3.0F, 0.4140625F, 0.1875F}},
                {{-4.0F, -24.0F, -3.0F, 0.3515625F, 0.234375F}, {-4.0F, -24.0F, 3.0F, 0.328125F, 0.234375F}, {-4.0F, -12.0F, 3.0F, 0.328125F, 0.328125F}, {-4.0F, -12.0F, -3.0F, 0.3515625F, 0.328125F}},
                {{4.0F, -24.0F, 3.0F, 0.40625F, 0.234375F}, {4.0F, -24.0F, -3.0F, 0.3828125F, 0.234375F}, {4.0F, -12.0F, -3.0F, 0.3828125F, 0.328125F}, {4.0F, -12.0F, 3.0F, 0.40625F, 0.328125F}},
                {{4.0F, -24.0F, -3.0F, 0.3828125F, 0.234375F}, {-4.0F, -24.0F, -3.0F, 0.3515625F, 0.234375F}, {-4.0F, -12.0F, -3.0F, 0.3515625F, 0.328125F}, {4.0F, -12.0F, -3.0F, 0.3828125F, 0.328125F}},
                {{-4.0F, -24.0F, 3.0F, 0.4375F, 0.234375F}, {4.0F, -24.0F, 3.0F, 0.40625F, 0.234375F}, {4.0F, -12.0F, 3.0F, 0.40625F, 0.328125F}, {-4.0F, -12.0F, 3.0F, 0.4375F, 0.328125F}}
        }));
        var p14 = new net.minecraft.client.model.geom.ModelPart(p14Cubes, java.util.Map.ofEntries());
        p14.setInitialPose(PartPose.offsetAndRotation(0.0F, 24.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p14.resetPose();
        var p13Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_head/__cem_host_headwear/__cem_host_headwear2/headwear2", java.util.List.of()));
        var p13 = new net.minecraft.client.model.geom.ModelPart(p13Cubes, java.util.Map.ofEntries());
        p13.setInitialPose(PartPose.offsetAndRotation(0.0F, 24.0F, 0.0F, -1.57079633F, 0.0F, 0.0F)); p13.resetPose();
        var p12Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_head/__cem_host_headwear/headwear/flerp", java.util.List.of()));
        p12Cubes.add(new ReferenceMeshCube(-1.0F, 0.0F, 0.0F, 3.0F, 0.0F, 4.0F, new float[][][] {
                {{2.0F, 0.0F, 4.0F, 0.27734375F, 0.03125F}, {-1.0F, 0.0F, 4.0F, 0.265625F, 0.03125F}, {-1.0F, 0.0F, 0.0F, 0.265625F, 0.0625F}, {2.0F, 0.0F, 0.0F, 0.27734375F, 0.0625F}},
                {{2.0F, 0.0F, 0.0F, 0.2890625F, 0.0625F}, {-1.0F, 0.0F, 0.0F, 0.27734375F, 0.0625F}, {-1.0F, 0.0F, 4.0F, 0.27734375F, 0.03125F}, {2.0F, 0.0F, 4.0F, 0.2890625F, 0.03125F}},
                {{-1.0F, 0.0F, 0.0F, 0.265625F, 0.0625F}, {-1.0F, 0.0F, 4.0F, 0.25F, 0.0625F}, {-1.0F, 0.0F, 4.0F, 0.25F, 0.0625F}, {-1.0F, 0.0F, 0.0F, 0.265625F, 0.0625F}},
                {{2.0F, 0.0F, 4.0F, 0.29296875F, 0.0625F}, {2.0F, 0.0F, 0.0F, 0.27734375F, 0.0625F}, {2.0F, 0.0F, 0.0F, 0.27734375F, 0.0625F}, {2.0F, 0.0F, 4.0F, 0.29296875F, 0.0625F}},
                {{2.0F, 0.0F, 0.0F, 0.27734375F, 0.0625F}, {-1.0F, 0.0F, 0.0F, 0.265625F, 0.0625F}, {-1.0F, 0.0F, 0.0F, 0.265625F, 0.0625F}, {2.0F, 0.0F, 0.0F, 0.27734375F, 0.0625F}},
                {{-1.0F, 0.0F, 4.0F, 0.3046875F, 0.0625F}, {2.0F, 0.0F, 4.0F, 0.29296875F, 0.0625F}, {2.0F, 0.0F, 4.0F, 0.29296875F, 0.0625F}, {-1.0F, 0.0F, 4.0F, 0.3046875F, 0.0625F}}
        }));
        var p12 = new net.minecraft.client.model.geom.ModelPart(p12Cubes, java.util.Map.ofEntries());
        p12.setInitialPose(PartPose.offsetAndRotation(0.0F, -33.4F, 4.5F, 0.610865238F, 0.0F, 0.0F)); p12.resetPose();
        var p11Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_head/__cem_host_headwear/headwear", java.util.List.of()));
        p11Cubes.add(new ReferenceMeshCube(-4.51F, -34.51F, -4.51F, 9.02F, 11.02F, 9.02F, new float[][][] {
                {{4.51F, -34.51F, 4.51F, 0.453125F, 0.03125F}, {-4.51F, -34.51F, 4.51F, 0.421875F, 0.03125F}, {-4.51F, -34.51F, -4.51F, 0.421875F, 0.09375F}, {4.51F, -34.51F, -4.51F, 0.453125F, 0.09375F}},
                {{4.51F, -23.49F, -4.51F, 0.484375F, 0.09375F}, {-4.51F, -23.49F, -4.51F, 0.453125F, 0.09375F}, {-4.51F, -23.49F, 4.51F, 0.453125F, 0.03125F}, {4.51F, -23.49F, 4.51F, 0.484375F, 0.03125F}},
                {{-4.51F, -34.51F, -4.51F, 0.421875F, 0.09375F}, {-4.51F, -34.51F, 4.51F, 0.390625F, 0.09375F}, {-4.51F, -23.49F, 4.51F, 0.390625F, 0.171875F}, {-4.51F, -23.49F, -4.51F, 0.421875F, 0.171875F}},
                {{4.51F, -34.51F, 4.51F, 0.484375F, 0.09375F}, {4.51F, -34.51F, -4.51F, 0.453125F, 0.09375F}, {4.51F, -23.49F, -4.51F, 0.453125F, 0.171875F}, {4.51F, -23.49F, 4.51F, 0.484375F, 0.171875F}},
                {{4.51F, -34.51F, -4.51F, 0.453125F, 0.09375F}, {-4.51F, -34.51F, -4.51F, 0.421875F, 0.09375F}, {-4.51F, -23.49F, -4.51F, 0.421875F, 0.171875F}, {4.51F, -23.49F, -4.51F, 0.453125F, 0.171875F}},
                {{-4.51F, -34.51F, 4.51F, 0.515625F, 0.09375F}, {4.51F, -34.51F, 4.51F, 0.484375F, 0.09375F}, {4.51F, -23.49F, 4.51F, 0.484375F, 0.171875F}, {-4.51F, -23.49F, 4.51F, 0.515625F, 0.171875F}}
        }));
        var p11 = new net.minecraft.client.model.geom.ModelPart(p11Cubes, java.util.Map.ofEntries(java.util.Map.entry("flerp", p12)));
        p11.setInitialPose(PartPose.offsetAndRotation(0.0F, 24.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p11.resetPose();
        var p10Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_head/__cem_host_nose/nose", java.util.List.of()));
        p10Cubes.add(new ReferenceMeshCube(-1.0F, -27.0F, -6.0F, 2.0F, 4.0F, 2.0F, new float[][][] {
                {{1.0F, -27.0F, -4.0F, 0.375F, 0.03125F}, {-1.0F, -27.0F, -4.0F, 0.3671875F, 0.03125F}, {-1.0F, -27.0F, -6.0F, 0.3671875F, 0.046875F}, {1.0F, -27.0F, -6.0F, 0.375F, 0.046875F}},
                {{1.0F, -23.0F, -6.0F, 0.3828125F, 0.046875F}, {-1.0F, -23.0F, -6.0F, 0.375F, 0.046875F}, {-1.0F, -23.0F, -4.0F, 0.375F, 0.03125F}, {1.0F, -23.0F, -4.0F, 0.3828125F, 0.03125F}},
                {{-1.0F, -27.0F, -6.0F, 0.3671875F, 0.046875F}, {-1.0F, -27.0F, -4.0F, 0.359375F, 0.046875F}, {-1.0F, -23.0F, -4.0F, 0.359375F, 0.078125F}, {-1.0F, -23.0F, -6.0F, 0.3671875F, 0.078125F}},
                {{1.0F, -27.0F, -4.0F, 0.3828125F, 0.046875F}, {1.0F, -27.0F, -6.0F, 0.375F, 0.046875F}, {1.0F, -23.0F, -6.0F, 0.375F, 0.078125F}, {1.0F, -23.0F, -4.0F, 0.3828125F, 0.078125F}},
                {{1.0F, -27.0F, -6.0F, 0.375F, 0.046875F}, {-1.0F, -27.0F, -6.0F, 0.3671875F, 0.046875F}, {-1.0F, -23.0F, -6.0F, 0.3671875F, 0.078125F}, {1.0F, -23.0F, -6.0F, 0.375F, 0.078125F}},
                {{-1.0F, -27.0F, -4.0F, 0.390625F, 0.046875F}, {1.0F, -27.0F, -4.0F, 0.3828125F, 0.046875F}, {1.0F, -23.0F, -4.0F, 0.3828125F, 0.078125F}, {-1.0F, -23.0F, -4.0F, 0.390625F, 0.078125F}}
        }));
        var p10 = new net.minecraft.client.model.geom.ModelPart(p10Cubes, java.util.Map.ofEntries());
        p10.setInitialPose(PartPose.offsetAndRotation(0.0F, 26.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p10.resetPose();
        var p9Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_head/head", java.util.List.of()));
        p9Cubes.add(new ReferenceMeshCube(-4.0F, -34.0F, -4.0F, 8.0F, 10.0F, 8.0F, new float[][][] {
                {{4.0F, -34.0F, 4.0F, 0.328125F, 0.03125F}, {-4.0F, -34.0F, 4.0F, 0.296875F, 0.03125F}, {-4.0F, -34.0F, -4.0F, 0.296875F, 0.09375F}, {4.0F, -34.0F, -4.0F, 0.328125F, 0.09375F}},
                {{4.0F, -24.0F, -4.0F, 0.359375F, 0.09375F}, {-4.0F, -24.0F, -4.0F, 0.328125F, 0.09375F}, {-4.0F, -24.0F, 4.0F, 0.328125F, 0.03125F}, {4.0F, -24.0F, 4.0F, 0.359375F, 0.03125F}},
                {{-4.0F, -34.0F, -4.0F, 0.296875F, 0.09375F}, {-4.0F, -34.0F, 4.0F, 0.265625F, 0.09375F}, {-4.0F, -24.0F, 4.0F, 0.265625F, 0.171875F}, {-4.0F, -24.0F, -4.0F, 0.296875F, 0.171875F}},
                {{4.0F, -34.0F, 4.0F, 0.359375F, 0.09375F}, {4.0F, -34.0F, -4.0F, 0.328125F, 0.09375F}, {4.0F, -24.0F, -4.0F, 0.328125F, 0.171875F}, {4.0F, -24.0F, 4.0F, 0.359375F, 0.171875F}},
                {{4.0F, -34.0F, -4.0F, 0.328125F, 0.09375F}, {-4.0F, -34.0F, -4.0F, 0.296875F, 0.09375F}, {-4.0F, -24.0F, -4.0F, 0.296875F, 0.171875F}, {4.0F, -24.0F, -4.0F, 0.328125F, 0.171875F}},
                {{-4.0F, -34.0F, 4.0F, 0.390625F, 0.09375F}, {4.0F, -34.0F, 4.0F, 0.359375F, 0.09375F}, {4.0F, -24.0F, 4.0F, 0.359375F, 0.171875F}, {-4.0F, -24.0F, 4.0F, 0.390625F, 0.171875F}}
        }));
        var p9 = new net.minecraft.client.model.geom.ModelPart(p9Cubes, java.util.Map.ofEntries());
        p9.setInitialPose(PartPose.offsetAndRotation(0.0F, 24.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p9.resetPose();
        var p8Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_left_leg", java.util.List.of()));
        var p8 = new net.minecraft.client.model.geom.ModelPart(p8Cubes, java.util.Map.ofEntries(java.util.Map.entry("left_leg", p20)));
        p8.setInitialPose(nativeRoot.getChild("left_leg").storePose()); p8.resetPose();
        var p7Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_right_leg", java.util.List.of()));
        var p7 = new net.minecraft.client.model.geom.ModelPart(p7Cubes, java.util.Map.ofEntries(java.util.Map.entry("right_leg", p19)));
        p7.setInitialPose(nativeRoot.getChild("right_leg").storePose()); p7.resetPose();
        var p6Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_arms", java.util.List.of()));
        var p6 = new net.minecraft.client.model.geom.ModelPart(p6Cubes, java.util.Map.ofEntries(java.util.Map.entry("arms", p16)));
        p6.setInitialPose(nativeRoot.getChild("arms").storePose()); p6.resetPose();
        var p5Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/__cem_host_bodywear", java.util.List.of()));
        var p5 = new net.minecraft.client.model.geom.ModelPart(p5Cubes, java.util.Map.ofEntries(java.util.Map.entry("bodywear", p15)));
        p5.setInitialPose(nativeRoot.getChild("body").getChild("jacket").storePose()); p5.resetPose();
        var p4Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body", java.util.List.of()));
        var p4 = new net.minecraft.client.model.geom.ModelPart(p4Cubes, java.util.Map.ofEntries(java.util.Map.entry("__cem_host_bodywear", p5), java.util.Map.entry("body", p14)));
        p4.setInitialPose(nativeRoot.getChild("body").storePose()); p4.resetPose();
        var p3Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_head/__cem_host_nose", java.util.List.of()));
        var p3 = new net.minecraft.client.model.geom.ModelPart(p3Cubes, java.util.Map.ofEntries(java.util.Map.entry("nose", p10)));
        p3.setInitialPose(nativeRoot.getChild("head").getChild("nose").storePose()); p3.resetPose();
        var p2Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_head/__cem_host_headwear/__cem_host_headwear2", java.util.List.of()));
        var p2 = new net.minecraft.client.model.geom.ModelPart(p2Cubes, java.util.Map.ofEntries(java.util.Map.entry("headwear2", p13)));
        p2.setInitialPose(nativeRoot.getChild("head").getChild("hat").getChild("hat_rim").storePose()); p2.resetPose();
        var p1Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_head/__cem_host_headwear", java.util.List.of()));
        var p1 = new net.minecraft.client.model.geom.ModelPart(p1Cubes, java.util.Map.ofEntries(java.util.Map.entry("__cem_host_headwear2", p2), java.util.Map.entry("headwear", p11)));
        p1.setInitialPose(nativeRoot.getChild("head").getChild("hat").storePose()); p1.resetPose();
        var p0Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_head", java.util.List.of()));
        var p0 = new net.minecraft.client.model.geom.ModelPart(p0Cubes, java.util.Map.ofEntries(java.util.Map.entry("__cem_host_headwear", p1), java.util.Map.entry("__cem_host_nose", p3), java.util.Map.entry("head", p9)));
        p0.setInitialPose(nativeRoot.getChild("head").storePose()); p0.resetPose();
        return new net.minecraft.client.model.geom.ModelPart(java.util.List.of(), java.util.Map.ofEntries(java.util.Map.entry("__cem_host_head", p0), java.util.Map.entry("__cem_host_body", p4), java.util.Map.entry("__cem_host_arms", p6), java.util.Map.entry("__cem_host_right_leg", p7), java.util.Map.entry("__cem_host_left_leg", p8)));
    }
}
