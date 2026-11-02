/*
 * Generated adaptation of ReCrafted Mobs 11.0 by GillsBills and credited contributors.
 * Authored asset license: CC-BY-NC-SA-4.0; https://creativecommons.org/licenses/by-nc-sa/4.0/
 * Source: https://modrinth.com/resourcepack/recrafted-mobs
 * Model: assets/minecraft/optifine/cem/chicken/chicken5.jem
 * Source SHA256: cb27c85fc11c452139f285ed0ee78492228484757803caa0a799cf4c372f8a64
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

public final class CemPetGlowingMiniAbominationGeometry {
    private CemPetGlowingMiniAbominationGeometry() {}

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
        PartDefinition p10 = p9.addOrReplaceChild("mini", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p11 = p10.addOrReplaceChild("headred", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -4.0F, 1.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p12 = p11.addOrReplaceChild("bone14", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -4.0F, 2.5F, 0.0F, 0.0F, 0.0F));
        PartDefinition p13 = p10.addOrReplaceChild("right_leg3", CubeListBuilder.create(), PartPose.offsetAndRotation(-2.0F, -4.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p14 = p10.addOrReplaceChild("left_leg3", CubeListBuilder.create(), PartPose.offsetAndRotation(2.0F, -4.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p15 = p10.addOrReplaceChild("right_hand", CubeListBuilder.create(), PartPose.offsetAndRotation(-3.5F, -7.5F, 0.0F, 0.0F, 0.0F, 1.57079633F));
        PartDefinition p16 = p15.addOrReplaceChild("body_sub_29", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p17 = p15.addOrReplaceChild("leaf", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.57079633F));
        PartDefinition p18 = p10.addOrReplaceChild("left_hand", CubeListBuilder.create(), PartPose.offsetAndRotation(3.5F, -7.5F, 0.0F, 0.0F, 0.0F, -1.57079633F));
        PartDefinition p19 = p10.addOrReplaceChild("headwood", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -4.0F, 1.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p20 = p19.addOrReplaceChild("bone15", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -1.0F, 2.5F, 0.0F, 0.0F, 0.0F));
        PartDefinition p21 = p10.addOrReplaceChild("right_leg4", CubeListBuilder.create(), PartPose.offsetAndRotation(-2.0F, -2.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p22 = p10.addOrReplaceChild("left_leg4", CubeListBuilder.create(), PartPose.offsetAndRotation(2.0F, -2.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p23 = p2.addOrReplaceChild("right_leg", CubeListBuilder.create(), PartPose.offsetAndRotation(2.0F, 5.0F, -1.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p24 = p3.addOrReplaceChild("left_leg", CubeListBuilder.create(), PartPose.offsetAndRotation(-1.0F, 5.0F, -1.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p25 = p4.addOrReplaceChild("right_wing", CubeListBuilder.create(), PartPose.offsetAndRotation(4.0F, 11.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p26 = p5.addOrReplaceChild("left_wing", CubeListBuilder.create(), PartPose.offsetAndRotation(-4.0F, 11.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p27 = p6.addOrReplaceChild("bill", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 9.0F, 4.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p28 = p7.addOrReplaceChild("chin", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 9.0F, 4.0F, 0.0F, 0.0F, 0.0F));
        return LayerDefinition.create(mesh, 64, 64);
    }

    /** Preserve source expression units; static scales are reapplied after every pose reset. */
    public static ReferenceMobDefinition.Builder configure(ReferenceMobDefinition.Builder builder) {
        builder.cemDriver("chicken");
        builder.bakedRoot(CemPetGlowingMiniAbominationGeometry::bakeRoot);
        builder.hostAlias("head", "__cem_host_head");
        builder.hostAlias("body", "__cem_host_body");
        builder.hostAlias("right_leg", "__cem_host_right_leg");
        builder.hostAlias("left_leg", "__cem_host_left_leg");
        builder.hostAlias("right_wing", "__cem_host_right_wing");
        builder.hostAlias("left_wing", "__cem_host_left_wing");
        builder.hostAlias("bill", "__cem_host_bill");
        builder.hostAlias("chin", "__cem_host_chin");
        builder.expression("mini.rx", "torad(-90)");
        builder.expression("mini.tz", "-8");
        builder.expression("mini.ty", "-8");
        builder.expression("left_leg3.rx", "left_leg.rx");
        builder.expression("right_leg3.rx", "right_leg.rx");
        builder.expression("left_leg4.rx", "left_leg.rx");
        builder.expression("right_leg4.rx", "right_leg.rx");
        builder.expression("bone14.rx", "-torad(10) + sin(torad(12.5*3) + time/5) *0.10 + if(limb_speed<=0.2, 0, -sin(torad(12.5*5) + time/15) *0.10 +torad(-20))");
        builder.expression("right_hand.rz", "torad(70) + -sin(torad(12.5*3) + time/5) *0.25 + if(limb_speed<=0.2, 0, torad(40))+ if(limb_speed<=0.1, 0, torad(20))+ if(limb_speed<=0.05, 0, sin(torad(12.5*3) + time/5) *0.25 + torad(10))");
        builder.expression("left_hand.rz", "torad(-70) + sin(torad(12.5*3) + time/5) *0.25 + if(limb_speed<=0.2, 0, torad(-40))+ if(limb_speed<=0.1, 0, torad(-20))+ if(limb_speed<=0.05, 0, -sin(torad(12.5*3) + time/5) *0.25 + torad(-10))");
        builder.expression("right_hand.rx", "if(limb_speed<=0.2, 0, (limb_speed+limb_speed*sin(limb_swing/11110) +torad(-10)))");
        builder.expression("left_hand.rx", "(limb_speed+limb_speed*sin(limb_swing/11150)+torad(-10))");
        builder.expression("bone15.rx", "-torad(10) + sin(torad(12.5*3) + time/5) *0.10 + if(limb_speed<=0.2, 0, -sin(torad(12.5*3) + time/5) *0.10 +torad(-20))");
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
        var p28Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_chin/chin", java.util.List.of()));
        var p28 = new net.minecraft.client.model.geom.ModelPart(p28Cubes, java.util.Map.ofEntries());
        p28.setInitialPose(PartPose.offsetAndRotation(0.0F, 9.0F, 4.0F, 0.0F, 0.0F, 0.0F)); p28.resetPose();
        var p27Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_bill/bill", java.util.List.of()));
        var p27 = new net.minecraft.client.model.geom.ModelPart(p27Cubes, java.util.Map.ofEntries());
        p27.setInitialPose(PartPose.offsetAndRotation(0.0F, 9.0F, 4.0F, 0.0F, 0.0F, 0.0F)); p27.resetPose();
        var p26Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_left_wing/left_wing", java.util.List.of()));
        var p26 = new net.minecraft.client.model.geom.ModelPart(p26Cubes, java.util.Map.ofEntries());
        p26.setInitialPose(PartPose.offsetAndRotation(-4.0F, 11.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p26.resetPose();
        var p25Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_right_wing/right_wing", java.util.List.of()));
        var p25 = new net.minecraft.client.model.geom.ModelPart(p25Cubes, java.util.Map.ofEntries());
        p25.setInitialPose(PartPose.offsetAndRotation(4.0F, 11.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p25.resetPose();
        var p24Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_left_leg/left_leg", java.util.List.of()));
        var p24 = new net.minecraft.client.model.geom.ModelPart(p24Cubes, java.util.Map.ofEntries());
        p24.setInitialPose(PartPose.offsetAndRotation(-1.0F, 5.0F, -1.0F, 0.0F, 0.0F, 0.0F)); p24.resetPose();
        var p23Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_right_leg/right_leg", java.util.List.of()));
        var p23 = new net.minecraft.client.model.geom.ModelPart(p23Cubes, java.util.Map.ofEntries());
        p23.setInitialPose(PartPose.offsetAndRotation(2.0F, 5.0F, -1.0F, 0.0F, 0.0F, 0.0F)); p23.resetPose();
        var p22Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/body/mini/left_leg4", java.util.List.of()));
        p22Cubes.add(new ReferenceMeshCube(-1.5F, 0.0F, -2.0F, 3.0F, 2.0F, 4.0F, new float[][][] {
                {{1.5F, 0.0F, -2.0F, 0.6875F, 0.96875F}, {-1.5F, 0.0F, -2.0F, 0.640625F, 0.96875F}, {-1.5F, 2.0F, -2.0F, 0.640625F, 1.0F}, {1.5F, 2.0F, -2.0F, 0.6875F, 1.0F}},
                {{-1.5F, 0.0F, -2.0F, 0.640625F, 0.96875F}, {-1.5F, 0.0F, 2.0F, 0.578125F, 0.96875F}, {-1.5F, 2.0F, 2.0F, 0.578125F, 1.0F}, {-1.5F, 2.0F, -2.0F, 0.640625F, 1.0F}},
                {{-1.5F, 0.0F, 2.0F, 0.796875F, 0.96875F}, {1.5F, 0.0F, 2.0F, 0.75F, 0.96875F}, {1.5F, 2.0F, 2.0F, 0.75F, 1.0F}, {-1.5F, 2.0F, 2.0F, 0.796875F, 1.0F}},
                {{1.5F, 0.0F, 2.0F, 0.75F, 0.96875F}, {1.5F, 0.0F, -2.0F, 0.6875F, 0.96875F}, {1.5F, 2.0F, -2.0F, 0.6875F, 1.0F}, {1.5F, 2.0F, 2.0F, 0.75F, 1.0F}},
                {{-1.5F, 0.0F, -2.0F, 0.640625F, 0.96875F}, {1.5F, 0.0F, -2.0F, 0.6875F, 0.96875F}, {1.5F, 0.0F, 2.0F, 0.6875F, 0.90625F}, {-1.5F, 0.0F, 2.0F, 0.640625F, 0.90625F}},
                {{-1.5F, 2.0F, 2.0F, 0.6875F, 0.90625F}, {1.5F, 2.0F, 2.0F, 0.734375F, 0.90625F}, {1.5F, 2.0F, -2.0F, 0.734375F, 0.96875F}, {-1.5F, 2.0F, -2.0F, 0.6875F, 0.96875F}}
        }));
        var p22 = new net.minecraft.client.model.geom.ModelPart(p22Cubes, java.util.Map.ofEntries());
        p22.setInitialPose(PartPose.offsetAndRotation(2.0F, -2.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p22.resetPose();
        var p21Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/body/mini/right_leg4", java.util.List.of()));
        p21Cubes.add(new ReferenceMeshCube(-1.5F, 0.0F, -2.0F, 3.0F, 2.0F, 4.0F, new float[][][] {
                {{1.5F, 0.0F, -2.0F, 0.484375F, 0.96875F}, {-1.5F, 0.0F, -2.0F, 0.4375F, 0.96875F}, {-1.5F, 2.0F, -2.0F, 0.4375F, 1.0F}, {1.5F, 2.0F, -2.0F, 0.484375F, 1.0F}},
                {{-1.5F, 0.0F, -2.0F, 0.4375F, 0.96875F}, {-1.5F, 0.0F, 2.0F, 0.375F, 0.96875F}, {-1.5F, 2.0F, 2.0F, 0.375F, 1.0F}, {-1.5F, 2.0F, -2.0F, 0.4375F, 1.0F}},
                {{-1.5F, 0.0F, 2.0F, 0.59375F, 0.96875F}, {1.5F, 0.0F, 2.0F, 0.546875F, 0.96875F}, {1.5F, 2.0F, 2.0F, 0.546875F, 1.0F}, {-1.5F, 2.0F, 2.0F, 0.59375F, 1.0F}},
                {{1.5F, 0.0F, 2.0F, 0.546875F, 0.96875F}, {1.5F, 0.0F, -2.0F, 0.484375F, 0.96875F}, {1.5F, 2.0F, -2.0F, 0.484375F, 1.0F}, {1.5F, 2.0F, 2.0F, 0.546875F, 1.0F}},
                {{-1.5F, 0.0F, -2.0F, 0.4375F, 0.96875F}, {1.5F, 0.0F, -2.0F, 0.484375F, 0.96875F}, {1.5F, 0.0F, 2.0F, 0.484375F, 0.90625F}, {-1.5F, 0.0F, 2.0F, 0.4375F, 0.90625F}},
                {{-1.5F, 2.0F, 2.0F, 0.484375F, 0.90625F}, {1.5F, 2.0F, 2.0F, 0.53125F, 0.90625F}, {1.5F, 2.0F, -2.0F, 0.53125F, 0.96875F}, {-1.5F, 2.0F, -2.0F, 0.484375F, 0.96875F}}
        }));
        var p21 = new net.minecraft.client.model.geom.ModelPart(p21Cubes, java.util.Map.ofEntries());
        p21.setInitialPose(PartPose.offsetAndRotation(-2.0F, -2.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p21.resetPose();
        var p20Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/body/mini/headwood/bone15", java.util.List.of()));
        p20Cubes.add(new ReferenceMeshCube(-3.5F, -9.0F, -7.0F, 7.0F, 9.0F, 7.0F, new float[][][] {
                {{3.5F, -9.0F, -7.0F, 0.21875F, 0.640625F}, {-3.5F, -9.0F, -7.0F, 0.109375F, 0.640625F}, {-3.5F, 0.0F, -7.0F, 0.109375F, 0.78125F}, {3.5F, 0.0F, -7.0F, 0.21875F, 0.78125F}},
                {{-3.5F, -9.0F, -7.0F, 0.109375F, 0.640625F}, {-3.5F, -9.0F, 0.0F, 0.0F, 0.640625F}, {-3.5F, 0.0F, 0.0F, 0.0F, 0.78125F}, {-3.5F, 0.0F, -7.0F, 0.109375F, 0.78125F}},
                {{-3.5F, -9.0F, 0.0F, 0.4375F, 0.640625F}, {3.5F, -9.0F, 0.0F, 0.328125F, 0.640625F}, {3.5F, 0.0F, 0.0F, 0.328125F, 0.78125F}, {-3.5F, 0.0F, 0.0F, 0.4375F, 0.78125F}},
                {{3.5F, -9.0F, 0.0F, 0.328125F, 0.640625F}, {3.5F, -9.0F, -7.0F, 0.21875F, 0.640625F}, {3.5F, 0.0F, -7.0F, 0.21875F, 0.78125F}, {3.5F, 0.0F, 0.0F, 0.328125F, 0.78125F}},
                {{-3.5F, -9.0F, -7.0F, 0.109375F, 0.640625F}, {3.5F, -9.0F, -7.0F, 0.21875F, 0.640625F}, {3.5F, -9.0F, 0.0F, 0.21875F, 0.53125F}, {-3.5F, -9.0F, 0.0F, 0.109375F, 0.53125F}},
                {{-3.5F, 0.0F, 0.0F, 0.21875F, 0.53125F}, {3.5F, 0.0F, 0.0F, 0.328125F, 0.53125F}, {3.5F, 0.0F, -7.0F, 0.328125F, 0.640625F}, {-3.5F, 0.0F, -7.0F, 0.21875F, 0.640625F}}
        }));
        p20Cubes.add(new ReferenceMeshCube(1.0F, -11.0F, -7.05F, 5.0F, 5.0F, 0.0F, new float[][][] {
                {{6.0F, -11.0F, -7.05F, 0.40625F, 0.78125F}, {1.0F, -11.0F, -7.05F, 0.328125F, 0.78125F}, {1.0F, -6.0F, -7.05F, 0.328125F, 0.859375F}, {6.0F, -6.0F, -7.05F, 0.40625F, 0.859375F}},
                {{1.0F, -11.0F, -7.05F, 0.328125F, 0.78125F}, {1.0F, -11.0F, -7.05F, 0.328125F, 0.78125F}, {1.0F, -6.0F, -7.05F, 0.328125F, 0.859375F}, {1.0F, -6.0F, -7.05F, 0.328125F, 0.859375F}},
                {{1.0F, -11.0F, -7.05F, 0.484375F, 0.78125F}, {6.0F, -11.0F, -7.05F, 0.40625F, 0.78125F}, {6.0F, -6.0F, -7.05F, 0.40625F, 0.859375F}, {1.0F, -6.0F, -7.05F, 0.484375F, 0.859375F}},
                {{6.0F, -11.0F, -7.05F, 0.40625F, 0.78125F}, {6.0F, -11.0F, -7.05F, 0.40625F, 0.78125F}, {6.0F, -6.0F, -7.05F, 0.40625F, 0.859375F}, {6.0F, -6.0F, -7.05F, 0.40625F, 0.859375F}},
                {{1.0F, -11.0F, -7.05F, 0.328125F, 0.78125F}, {6.0F, -11.0F, -7.05F, 0.40625F, 0.78125F}, {6.0F, -11.0F, -7.05F, 0.40625F, 0.78125F}, {1.0F, -11.0F, -7.05F, 0.328125F, 0.78125F}},
                {{1.0F, -6.0F, -7.05F, 0.40625F, 0.78125F}, {6.0F, -6.0F, -7.05F, 0.484375F, 0.78125F}, {6.0F, -6.0F, -7.05F, 0.484375F, 0.78125F}, {1.0F, -6.0F, -7.05F, 0.40625F, 0.78125F}}
        }));
        p20Cubes.add(new ReferenceMeshCube(-3.5F, -6.0F, -7.0F, 7.0F, 0.0F, 7.0F, new float[][][] {
                {{3.5F, -6.0F, -7.0F, 0.4375F, 0.640625F}, {-3.5F, -6.0F, -7.0F, 0.328125F, 0.640625F}, {-3.5F, -6.0F, -7.0F, 0.328125F, 0.640625F}, {3.5F, -6.0F, -7.0F, 0.4375F, 0.640625F}},
                {{-3.5F, -6.0F, -7.0F, 0.328125F, 0.640625F}, {-3.5F, -6.0F, 0.0F, 0.21875F, 0.640625F}, {-3.5F, -6.0F, 0.0F, 0.21875F, 0.640625F}, {-3.5F, -6.0F, -7.0F, 0.328125F, 0.640625F}},
                {{-3.5F, -6.0F, 0.0F, 0.65625F, 0.640625F}, {3.5F, -6.0F, 0.0F, 0.546875F, 0.640625F}, {3.5F, -6.0F, 0.0F, 0.546875F, 0.640625F}, {-3.5F, -6.0F, 0.0F, 0.65625F, 0.640625F}},
                {{3.5F, -6.0F, 0.0F, 0.546875F, 0.640625F}, {3.5F, -6.0F, -7.0F, 0.4375F, 0.640625F}, {3.5F, -6.0F, -7.0F, 0.4375F, 0.640625F}, {3.5F, -6.0F, 0.0F, 0.546875F, 0.640625F}},
                {{-3.5F, -6.0F, -7.0F, 0.328125F, 0.640625F}, {3.5F, -6.0F, -7.0F, 0.4375F, 0.640625F}, {3.5F, -6.0F, 0.0F, 0.4375F, 0.53125F}, {-3.5F, -6.0F, 0.0F, 0.328125F, 0.53125F}},
                {{-3.5F, -6.0F, 0.0F, 0.4375F, 0.53125F}, {3.5F, -6.0F, 0.0F, 0.546875F, 0.53125F}, {3.5F, -6.0F, -7.0F, 0.546875F, 0.640625F}, {-3.5F, -6.0F, -7.0F, 0.4375F, 0.640625F}}
        }));
        p20Cubes.add(new ReferenceMeshCube(-3.5F, -1.0F, -7.0F, 7.0F, 0.0F, 7.0F, new float[][][] {
                {{3.5F, -1.0F, -7.0F, 0.59375F, 0.90625F}, {-3.5F, -1.0F, -7.0F, 0.484375F, 0.90625F}, {-3.5F, -1.0F, -7.0F, 0.484375F, 0.90625F}, {3.5F, -1.0F, -7.0F, 0.59375F, 0.90625F}},
                {{-3.5F, -1.0F, -7.0F, 0.484375F, 0.90625F}, {-3.5F, -1.0F, 0.0F, 0.375F, 0.90625F}, {-3.5F, -1.0F, 0.0F, 0.375F, 0.90625F}, {-3.5F, -1.0F, -7.0F, 0.484375F, 0.90625F}},
                {{-3.5F, -1.0F, 0.0F, 0.8125F, 0.90625F}, {3.5F, -1.0F, 0.0F, 0.703125F, 0.90625F}, {3.5F, -1.0F, 0.0F, 0.703125F, 0.90625F}, {-3.5F, -1.0F, 0.0F, 0.8125F, 0.90625F}},
                {{3.5F, -1.0F, 0.0F, 0.703125F, 0.90625F}, {3.5F, -1.0F, -7.0F, 0.59375F, 0.90625F}, {3.5F, -1.0F, -7.0F, 0.59375F, 0.90625F}, {3.5F, -1.0F, 0.0F, 0.703125F, 0.90625F}},
                {{-3.5F, -1.0F, -7.0F, 0.484375F, 0.90625F}, {3.5F, -1.0F, -7.0F, 0.59375F, 0.90625F}, {3.5F, -1.0F, 0.0F, 0.59375F, 0.796875F}, {-3.5F, -1.0F, 0.0F, 0.484375F, 0.796875F}},
                {{-3.5F, -1.0F, 0.0F, 0.59375F, 0.796875F}, {3.5F, -1.0F, 0.0F, 0.703125F, 0.796875F}, {3.5F, -1.0F, -7.0F, 0.703125F, 0.90625F}, {-3.5F, -1.0F, -7.0F, 0.59375F, 0.90625F}}
        }));
        var p20 = new net.minecraft.client.model.geom.ModelPart(p20Cubes, java.util.Map.ofEntries());
        p20.setInitialPose(PartPose.offsetAndRotation(0.0F, -1.0F, 2.5F, 0.0F, 0.0F, 0.0F)); p20.resetPose();
        var p19Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/body/mini/headwood", java.util.List.of()));
        p19Cubes.add(new ReferenceMeshCube(-3.5F, -1.0F, -4.5F, 7.0F, 3.0F, 7.0F, new float[][][] {
                {{3.5F, -1.0F, -4.5F, 0.21875F, 0.890625F}, {-3.5F, -1.0F, -4.5F, 0.109375F, 0.890625F}, {-3.5F, 2.0F, -4.5F, 0.109375F, 0.9375F}, {3.5F, 2.0F, -4.5F, 0.21875F, 0.9375F}},
                {{-3.5F, -1.0F, -4.5F, 0.109375F, 0.890625F}, {-3.5F, -1.0F, 2.5F, 0.0F, 0.890625F}, {-3.5F, 2.0F, 2.5F, 0.0F, 0.9375F}, {-3.5F, 2.0F, -4.5F, 0.109375F, 0.9375F}},
                {{-3.5F, -1.0F, 2.5F, 0.4375F, 0.890625F}, {3.5F, -1.0F, 2.5F, 0.328125F, 0.890625F}, {3.5F, 2.0F, 2.5F, 0.328125F, 0.9375F}, {-3.5F, 2.0F, 2.5F, 0.4375F, 0.9375F}},
                {{3.5F, -1.0F, 2.5F, 0.328125F, 0.890625F}, {3.5F, -1.0F, -4.5F, 0.21875F, 0.890625F}, {3.5F, 2.0F, -4.5F, 0.21875F, 0.9375F}, {3.5F, 2.0F, 2.5F, 0.328125F, 0.9375F}},
                {{-3.5F, -1.0F, -4.5F, 0.109375F, 0.890625F}, {3.5F, -1.0F, -4.5F, 0.21875F, 0.890625F}, {3.5F, -1.0F, 2.5F, 0.21875F, 0.78125F}, {-3.5F, -1.0F, 2.5F, 0.109375F, 0.78125F}},
                {{-3.5F, 2.0F, 2.5F, 0.21875F, 0.78125F}, {3.5F, 2.0F, 2.5F, 0.328125F, 0.78125F}, {3.5F, 2.0F, -4.5F, 0.328125F, 0.890625F}, {-3.5F, 2.0F, -4.5F, 0.21875F, 0.890625F}}
        }));
        p19Cubes.add(new ReferenceMeshCube(-3.5F, 0.0F, -4.5F, 7.0F, 0.0F, 7.0F, new float[][][] {
                {{3.5F, 0.0F, -4.5F, 0.59375F, 0.796875F}, {-3.5F, 0.0F, -4.5F, 0.484375F, 0.796875F}, {-3.5F, 0.0F, -4.5F, 0.484375F, 0.796875F}, {3.5F, 0.0F, -4.5F, 0.59375F, 0.796875F}},
                {{-3.5F, 0.0F, -4.5F, 0.484375F, 0.796875F}, {-3.5F, 0.0F, 2.5F, 0.375F, 0.796875F}, {-3.5F, 0.0F, 2.5F, 0.375F, 0.796875F}, {-3.5F, 0.0F, -4.5F, 0.484375F, 0.796875F}},
                {{-3.5F, 0.0F, 2.5F, 0.8125F, 0.796875F}, {3.5F, 0.0F, 2.5F, 0.703125F, 0.796875F}, {3.5F, 0.0F, 2.5F, 0.703125F, 0.796875F}, {-3.5F, 0.0F, 2.5F, 0.8125F, 0.796875F}},
                {{3.5F, 0.0F, 2.5F, 0.703125F, 0.796875F}, {3.5F, 0.0F, -4.5F, 0.59375F, 0.796875F}, {3.5F, 0.0F, -4.5F, 0.59375F, 0.796875F}, {3.5F, 0.0F, 2.5F, 0.703125F, 0.796875F}},
                {{-3.5F, 0.0F, -4.5F, 0.484375F, 0.796875F}, {3.5F, 0.0F, -4.5F, 0.59375F, 0.796875F}, {3.5F, 0.0F, 2.5F, 0.59375F, 0.6875F}, {-3.5F, 0.0F, 2.5F, 0.484375F, 0.6875F}},
                {{-3.5F, 0.0F, 2.5F, 0.59375F, 0.6875F}, {3.5F, 0.0F, 2.5F, 0.703125F, 0.6875F}, {3.5F, 0.0F, -4.5F, 0.703125F, 0.796875F}, {-3.5F, 0.0F, -4.5F, 0.59375F, 0.796875F}}
        }));
        var p19 = new net.minecraft.client.model.geom.ModelPart(p19Cubes, java.util.Map.ofEntries(java.util.Map.entry("bone15", p20)));
        p19.setInitialPose(PartPose.offsetAndRotation(0.0F, -4.0F, 1.0F, 0.0F, 0.0F, 0.0F)); p19.resetPose();
        var p18Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/body/mini/left_hand", java.util.List.of()));
        p18Cubes.add(new ReferenceMeshCube(-1.5F, 0.0F, -1.5F, 3.0F, 5.0F, 4.0F, new float[][][] {
                {{1.5F, 0.0F, -1.5F, 0.546875F, 0.328125F}, {-1.5F, 0.0F, -1.5F, 0.5F, 0.328125F}, {-1.5F, 5.0F, -1.5F, 0.5F, 0.40625F}, {1.5F, 5.0F, -1.5F, 0.546875F, 0.40625F}},
                {{-1.5F, 0.0F, -1.5F, 0.5F, 0.328125F}, {-1.5F, 0.0F, 2.5F, 0.4375F, 0.328125F}, {-1.5F, 5.0F, 2.5F, 0.4375F, 0.40625F}, {-1.5F, 5.0F, -1.5F, 0.5F, 0.40625F}},
                {{-1.5F, 0.0F, 2.5F, 0.65625F, 0.328125F}, {1.5F, 0.0F, 2.5F, 0.609375F, 0.328125F}, {1.5F, 5.0F, 2.5F, 0.609375F, 0.40625F}, {-1.5F, 5.0F, 2.5F, 0.65625F, 0.40625F}},
                {{1.5F, 0.0F, 2.5F, 0.609375F, 0.328125F}, {1.5F, 0.0F, -1.5F, 0.546875F, 0.328125F}, {1.5F, 5.0F, -1.5F, 0.546875F, 0.40625F}, {1.5F, 5.0F, 2.5F, 0.609375F, 0.40625F}},
                {{-1.5F, 0.0F, -1.5F, 0.5F, 0.328125F}, {1.5F, 0.0F, -1.5F, 0.546875F, 0.328125F}, {1.5F, 0.0F, 2.5F, 0.546875F, 0.265625F}, {-1.5F, 0.0F, 2.5F, 0.5F, 0.265625F}},
                {{-1.5F, 5.0F, 2.5F, 0.546875F, 0.265625F}, {1.5F, 5.0F, 2.5F, 0.59375F, 0.265625F}, {1.5F, 5.0F, -1.5F, 0.59375F, 0.328125F}, {-1.5F, 5.0F, -1.5F, 0.546875F, 0.328125F}}
        }));
        p18Cubes.add(new ReferenceMeshCube(-1.5F, 5.0F, -1.5F, 3.0F, 1.0F, 4.0F, new float[][][] {
                {{1.5F, 5.0F, -1.5F, 0.546875F, 0.40625F}, {-1.5F, 5.0F, -1.5F, 0.5F, 0.40625F}, {-1.5F, 6.0F, -1.5F, 0.5F, 0.421875F}, {1.5F, 6.0F, -1.5F, 0.546875F, 0.421875F}},
                {{-1.5F, 5.0F, -1.5F, 0.5F, 0.40625F}, {-1.5F, 5.0F, 2.5F, 0.4375F, 0.40625F}, {-1.5F, 6.0F, 2.5F, 0.4375F, 0.421875F}, {-1.5F, 6.0F, -1.5F, 0.5F, 0.421875F}},
                {{-1.5F, 5.0F, 2.5F, 0.65625F, 0.40625F}, {1.5F, 5.0F, 2.5F, 0.609375F, 0.40625F}, {1.5F, 6.0F, 2.5F, 0.609375F, 0.421875F}, {-1.5F, 6.0F, 2.5F, 0.65625F, 0.421875F}},
                {{1.5F, 5.0F, 2.5F, 0.609375F, 0.40625F}, {1.5F, 5.0F, -1.5F, 0.546875F, 0.40625F}, {1.5F, 6.0F, -1.5F, 0.546875F, 0.421875F}, {1.5F, 6.0F, 2.5F, 0.609375F, 0.421875F}},
                {{-1.5F, 5.0F, -1.5F, 0.65625F, 0.328125F}, {1.5F, 5.0F, -1.5F, 0.703125F, 0.328125F}, {1.5F, 5.0F, 2.5F, 0.703125F, 0.265625F}, {-1.5F, 5.0F, 2.5F, 0.65625F, 0.265625F}},
                {{-1.5F, 6.0F, 2.5F, 0.65625F, 0.265625F}, {1.5F, 6.0F, 2.5F, 0.703125F, 0.265625F}, {1.5F, 6.0F, -1.5F, 0.703125F, 0.328125F}, {-1.5F, 6.0F, -1.5F, 0.65625F, 0.328125F}}
        }));
        var p18 = new net.minecraft.client.model.geom.ModelPart(p18Cubes, java.util.Map.ofEntries());
        p18.setInitialPose(PartPose.offsetAndRotation(3.5F, -7.5F, 0.0F, 0.0F, 0.0F, -1.57079633F)); p18.resetPose();
        var p17Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/body/mini/right_hand/leaf", java.util.List.of()));
        p17Cubes.add(new ReferenceMeshCube(6.0F, 0.0F, 2.5F, 4.0F, 4.0F, 0.0F, new float[][][] {
                {{10.0F, 0.0F, 2.5F, 0.0625F, 0.578125F}, {6.0F, 0.0F, 2.5F, 0.0F, 0.578125F}, {6.0F, 4.0F, 2.5F, 0.0F, 0.640625F}, {10.0F, 4.0F, 2.5F, 0.0625F, 0.640625F}},
                {{6.0F, 0.0F, 2.5F, 0.0F, 0.578125F}, {6.0F, 0.0F, 2.5F, 0.0F, 0.578125F}, {6.0F, 4.0F, 2.5F, 0.0F, 0.640625F}, {6.0F, 4.0F, 2.5F, 0.0F, 0.640625F}},
                {{6.0F, 0.0F, 2.5F, 0.125F, 0.578125F}, {10.0F, 0.0F, 2.5F, 0.0625F, 0.578125F}, {10.0F, 4.0F, 2.5F, 0.0625F, 0.640625F}, {6.0F, 4.0F, 2.5F, 0.125F, 0.640625F}},
                {{10.0F, 0.0F, 2.5F, 0.0625F, 0.578125F}, {10.0F, 0.0F, 2.5F, 0.0625F, 0.578125F}, {10.0F, 4.0F, 2.5F, 0.0625F, 0.640625F}, {10.0F, 4.0F, 2.5F, 0.0625F, 0.640625F}},
                {{6.0F, 0.0F, 2.5F, 0.0F, 0.578125F}, {10.0F, 0.0F, 2.5F, 0.0625F, 0.578125F}, {10.0F, 0.0F, 2.5F, 0.0625F, 0.578125F}, {6.0F, 0.0F, 2.5F, 0.0F, 0.578125F}},
                {{6.0F, 4.0F, 2.5F, 0.0625F, 0.578125F}, {10.0F, 4.0F, 2.5F, 0.125F, 0.578125F}, {10.0F, 4.0F, 2.5F, 0.125F, 0.578125F}, {6.0F, 4.0F, 2.5F, 0.0625F, 0.578125F}}
        }));
        var p17 = new net.minecraft.client.model.geom.ModelPart(p17Cubes, java.util.Map.ofEntries());
        p17.setInitialPose(PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.57079633F)); p17.resetPose();
        var p16Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/body/mini/right_hand/body_sub_29", java.util.List.of()));
        p16Cubes.add(new ReferenceMeshCube(-1.5F, 0.0F, -1.5F, 3.0F, 5.0F, 4.0F, new float[][][] {
                {{1.5F, 0.0F, -1.5F, 0.5F, 0.171875F}, {-1.5F, 0.0F, -1.5F, 0.546875F, 0.171875F}, {-1.5F, 5.0F, -1.5F, 0.546875F, 0.25F}, {1.5F, 5.0F, -1.5F, 0.5F, 0.25F}},
                {{-1.5F, 0.0F, -1.5F, 0.546875F, 0.171875F}, {-1.5F, 0.0F, 2.5F, 0.609375F, 0.171875F}, {-1.5F, 5.0F, 2.5F, 0.609375F, 0.25F}, {-1.5F, 5.0F, -1.5F, 0.546875F, 0.25F}},
                {{-1.5F, 0.0F, 2.5F, 0.609375F, 0.171875F}, {1.5F, 0.0F, 2.5F, 0.65625F, 0.171875F}, {1.5F, 5.0F, 2.5F, 0.65625F, 0.25F}, {-1.5F, 5.0F, 2.5F, 0.609375F, 0.25F}},
                {{1.5F, 0.0F, 2.5F, 0.4375F, 0.171875F}, {1.5F, 0.0F, -1.5F, 0.5F, 0.171875F}, {1.5F, 5.0F, -1.5F, 0.5F, 0.25F}, {1.5F, 5.0F, 2.5F, 0.4375F, 0.25F}},
                {{-1.5F, 0.0F, -1.5F, 0.546875F, 0.171875F}, {1.5F, 0.0F, -1.5F, 0.5F, 0.171875F}, {1.5F, 0.0F, 2.5F, 0.5F, 0.109375F}, {-1.5F, 0.0F, 2.5F, 0.546875F, 0.109375F}},
                {{-1.5F, 5.0F, 2.5F, 0.59375F, 0.109375F}, {1.5F, 5.0F, 2.5F, 0.546875F, 0.109375F}, {1.5F, 5.0F, -1.5F, 0.546875F, 0.171875F}, {-1.5F, 5.0F, -1.5F, 0.59375F, 0.171875F}}
        }));
        p16Cubes.add(new ReferenceMeshCube(-1.5F, 5.0F, -1.5F, 3.0F, 1.0F, 4.0F, new float[][][] {
                {{1.5F, 5.0F, -1.5F, 0.5F, 0.25F}, {-1.5F, 5.0F, -1.5F, 0.546875F, 0.25F}, {-1.5F, 6.0F, -1.5F, 0.546875F, 0.265625F}, {1.5F, 6.0F, -1.5F, 0.5F, 0.265625F}},
                {{-1.5F, 5.0F, -1.5F, 0.546875F, 0.25F}, {-1.5F, 5.0F, 2.5F, 0.609375F, 0.25F}, {-1.5F, 6.0F, 2.5F, 0.609375F, 0.265625F}, {-1.5F, 6.0F, -1.5F, 0.546875F, 0.265625F}},
                {{-1.5F, 5.0F, 2.5F, 0.609375F, 0.25F}, {1.5F, 5.0F, 2.5F, 0.65625F, 0.25F}, {1.5F, 6.0F, 2.5F, 0.65625F, 0.265625F}, {-1.5F, 6.0F, 2.5F, 0.609375F, 0.265625F}},
                {{1.5F, 5.0F, 2.5F, 0.4375F, 0.25F}, {1.5F, 5.0F, -1.5F, 0.5F, 0.25F}, {1.5F, 6.0F, -1.5F, 0.5F, 0.265625F}, {1.5F, 6.0F, 2.5F, 0.4375F, 0.265625F}},
                {{-1.5F, 5.0F, -1.5F, 0.703125F, 0.328125F}, {1.5F, 5.0F, -1.5F, 0.65625F, 0.328125F}, {1.5F, 5.0F, 2.5F, 0.65625F, 0.265625F}, {-1.5F, 5.0F, 2.5F, 0.703125F, 0.265625F}},
                {{-1.5F, 6.0F, 2.5F, 0.703125F, 0.265625F}, {1.5F, 6.0F, 2.5F, 0.65625F, 0.265625F}, {1.5F, 6.0F, -1.5F, 0.65625F, 0.328125F}, {-1.5F, 6.0F, -1.5F, 0.703125F, 0.328125F}}
        }));
        var p16 = new net.minecraft.client.model.geom.ModelPart(p16Cubes, java.util.Map.ofEntries());
        p16.setInitialPose(PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p16.resetPose();
        var p15Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/body/mini/right_hand", java.util.List.of()));
        var p15 = new net.minecraft.client.model.geom.ModelPart(p15Cubes, java.util.Map.ofEntries(java.util.Map.entry("body_sub_29", p16), java.util.Map.entry("leaf", p17)));
        p15.setInitialPose(PartPose.offsetAndRotation(-3.5F, -7.5F, 0.0F, 0.0F, 0.0F, 1.57079633F)); p15.resetPose();
        var p14Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/body/mini/left_leg3", java.util.List.of()));
        p14Cubes.add(new ReferenceMeshCube(-1.5F, 0.0F, -2.0F, 3.0F, 4.0F, 4.0F, new float[][][] {
                {{1.5F, 0.0F, -2.0F, 0.328125F, 0.46875F}, {-1.5F, 0.0F, -2.0F, 0.28125F, 0.46875F}, {-1.5F, 4.0F, -2.0F, 0.28125F, 0.53125F}, {1.5F, 4.0F, -2.0F, 0.328125F, 0.53125F}},
                {{-1.5F, 0.0F, -2.0F, 0.28125F, 0.46875F}, {-1.5F, 0.0F, 2.0F, 0.21875F, 0.46875F}, {-1.5F, 4.0F, 2.0F, 0.21875F, 0.53125F}, {-1.5F, 4.0F, -2.0F, 0.28125F, 0.53125F}},
                {{-1.5F, 0.0F, 2.0F, 0.4375F, 0.46875F}, {1.5F, 0.0F, 2.0F, 0.390625F, 0.46875F}, {1.5F, 4.0F, 2.0F, 0.390625F, 0.53125F}, {-1.5F, 4.0F, 2.0F, 0.4375F, 0.53125F}},
                {{1.5F, 0.0F, 2.0F, 0.390625F, 0.46875F}, {1.5F, 0.0F, -2.0F, 0.328125F, 0.46875F}, {1.5F, 4.0F, -2.0F, 0.328125F, 0.53125F}, {1.5F, 4.0F, 2.0F, 0.390625F, 0.53125F}},
                {{-1.5F, 0.0F, -2.0F, 0.28125F, 0.46875F}, {1.5F, 0.0F, -2.0F, 0.328125F, 0.46875F}, {1.5F, 0.0F, 2.0F, 0.328125F, 0.40625F}, {-1.5F, 0.0F, 2.0F, 0.28125F, 0.40625F}},
                {{-1.5F, 4.0F, 2.0F, 0.328125F, 0.40625F}, {1.5F, 4.0F, 2.0F, 0.375F, 0.40625F}, {1.5F, 4.0F, -2.0F, 0.375F, 0.46875F}, {-1.5F, 4.0F, -2.0F, 0.328125F, 0.46875F}}
        }));
        var p14 = new net.minecraft.client.model.geom.ModelPart(p14Cubes, java.util.Map.ofEntries());
        p14.setInitialPose(PartPose.offsetAndRotation(2.0F, -4.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p14.resetPose();
        var p13Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/body/mini/right_leg3", java.util.List.of()));
        p13Cubes.add(new ReferenceMeshCube(-1.5F, 0.0F, -2.0F, 3.0F, 4.0F, 4.0F, new float[][][] {
                {{1.5F, 0.0F, -2.0F, 0.109375F, 0.46875F}, {-1.5F, 0.0F, -2.0F, 0.0625F, 0.46875F}, {-1.5F, 4.0F, -2.0F, 0.0625F, 0.53125F}, {1.5F, 4.0F, -2.0F, 0.109375F, 0.53125F}},
                {{-1.5F, 0.0F, -2.0F, 0.0625F, 0.46875F}, {-1.5F, 0.0F, 2.0F, 0.0F, 0.46875F}, {-1.5F, 4.0F, 2.0F, 0.0F, 0.53125F}, {-1.5F, 4.0F, -2.0F, 0.0625F, 0.53125F}},
                {{-1.5F, 0.0F, 2.0F, 0.21875F, 0.46875F}, {1.5F, 0.0F, 2.0F, 0.171875F, 0.46875F}, {1.5F, 4.0F, 2.0F, 0.171875F, 0.53125F}, {-1.5F, 4.0F, 2.0F, 0.21875F, 0.53125F}},
                {{1.5F, 0.0F, 2.0F, 0.171875F, 0.46875F}, {1.5F, 0.0F, -2.0F, 0.109375F, 0.46875F}, {1.5F, 4.0F, -2.0F, 0.109375F, 0.53125F}, {1.5F, 4.0F, 2.0F, 0.171875F, 0.53125F}},
                {{-1.5F, 0.0F, -2.0F, 0.0625F, 0.46875F}, {1.5F, 0.0F, -2.0F, 0.109375F, 0.46875F}, {1.5F, 0.0F, 2.0F, 0.109375F, 0.40625F}, {-1.5F, 0.0F, 2.0F, 0.0625F, 0.40625F}},
                {{-1.5F, 4.0F, 2.0F, 0.109375F, 0.40625F}, {1.5F, 4.0F, 2.0F, 0.15625F, 0.40625F}, {1.5F, 4.0F, -2.0F, 0.15625F, 0.46875F}, {-1.5F, 4.0F, -2.0F, 0.109375F, 0.46875F}}
        }));
        var p13 = new net.minecraft.client.model.geom.ModelPart(p13Cubes, java.util.Map.ofEntries());
        p13.setInitialPose(PartPose.offsetAndRotation(-2.0F, -4.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p13.resetPose();
        var p12Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/body/mini/headred/bone14", java.util.List.of()));
        p12Cubes.add(new ReferenceMeshCube(-3.5F, -6.0F, -7.0F, 7.0F, 6.0F, 7.0F, new float[][][] {
                {{3.5F, -6.0F, -7.0F, 0.21875F, 0.109375F}, {-3.5F, -6.0F, -7.0F, 0.109375F, 0.109375F}, {-3.5F, 0.0F, -7.0F, 0.109375F, 0.203125F}, {3.5F, 0.0F, -7.0F, 0.21875F, 0.203125F}},
                {{-3.5F, -6.0F, -7.0F, 0.109375F, 0.109375F}, {-3.5F, -6.0F, 0.0F, 0.0F, 0.109375F}, {-3.5F, 0.0F, 0.0F, 0.0F, 0.203125F}, {-3.5F, 0.0F, -7.0F, 0.109375F, 0.203125F}},
                {{-3.5F, -6.0F, 0.0F, 0.4375F, 0.109375F}, {3.5F, -6.0F, 0.0F, 0.328125F, 0.109375F}, {3.5F, 0.0F, 0.0F, 0.328125F, 0.203125F}, {-3.5F, 0.0F, 0.0F, 0.4375F, 0.203125F}},
                {{3.5F, -6.0F, 0.0F, 0.328125F, 0.109375F}, {3.5F, -6.0F, -7.0F, 0.21875F, 0.109375F}, {3.5F, 0.0F, -7.0F, 0.21875F, 0.203125F}, {3.5F, 0.0F, 0.0F, 0.328125F, 0.203125F}},
                {{-3.5F, -6.0F, -7.0F, 0.109375F, 0.109375F}, {3.5F, -6.0F, -7.0F, 0.21875F, 0.109375F}, {3.5F, -6.0F, 0.0F, 0.21875F, 0.0F}, {-3.5F, -6.0F, 0.0F, 0.109375F, 0.0F}},
                {{-3.5F, 0.0F, 0.0F, 0.21875F, 0.0F}, {3.5F, 0.0F, 0.0F, 0.328125F, 0.0F}, {3.5F, 0.0F, -7.0F, 0.328125F, 0.109375F}, {-3.5F, 0.0F, -7.0F, 0.21875F, 0.109375F}}
        }));
        p12Cubes.add(new ReferenceMeshCube(-1.0F, -4.0F, -7.05F, 2.0F, 1.0F, 0.0F, new float[][][] {
                {{1.0F, -4.0F, -7.05F, 0.078125F, 0.09375F}, {-1.0F, -4.0F, -7.05F, 0.046875F, 0.09375F}, {-1.0F, -3.0F, -7.05F, 0.046875F, 0.109375F}, {1.0F, -3.0F, -7.05F, 0.078125F, 0.109375F}},
                {{-1.0F, -4.0F, -7.05F, 0.046875F, 0.09375F}, {-1.0F, -4.0F, -7.05F, 0.046875F, 0.09375F}, {-1.0F, -3.0F, -7.05F, 0.046875F, 0.109375F}, {-1.0F, -3.0F, -7.05F, 0.046875F, 0.109375F}},
                {{-1.0F, -4.0F, -7.05F, 0.109375F, 0.09375F}, {1.0F, -4.0F, -7.05F, 0.078125F, 0.09375F}, {1.0F, -3.0F, -7.05F, 0.078125F, 0.109375F}, {-1.0F, -3.0F, -7.05F, 0.109375F, 0.109375F}},
                {{1.0F, -4.0F, -7.05F, 0.078125F, 0.09375F}, {1.0F, -4.0F, -7.05F, 0.078125F, 0.09375F}, {1.0F, -3.0F, -7.05F, 0.078125F, 0.109375F}, {1.0F, -3.0F, -7.05F, 0.078125F, 0.109375F}},
                {{-1.0F, -4.0F, -7.05F, 0.046875F, 0.09375F}, {1.0F, -4.0F, -7.05F, 0.078125F, 0.09375F}, {1.0F, -4.0F, -7.05F, 0.078125F, 0.09375F}, {-1.0F, -4.0F, -7.05F, 0.046875F, 0.09375F}},
                {{-1.0F, -3.0F, -7.05F, 0.078125F, 0.09375F}, {1.0F, -3.0F, -7.05F, 0.109375F, 0.09375F}, {1.0F, -3.0F, -7.05F, 0.109375F, 0.09375F}, {-1.0F, -3.0F, -7.05F, 0.078125F, 0.09375F}}
        }));
        p12Cubes.add(new ReferenceMeshCube(-3.5F, -2.0F, -7.05F, 1.0F, 1.0F, 0.0F, new float[][][] {
                {{-2.5F, -2.0F, -7.05F, 0.078125F, 0.09375F}, {-3.5F, -2.0F, -7.05F, 0.0625F, 0.09375F}, {-3.5F, -1.0F, -7.05F, 0.0625F, 0.109375F}, {-2.5F, -1.0F, -7.05F, 0.078125F, 0.109375F}},
                {{-3.5F, -2.0F, -7.05F, 0.0625F, 0.09375F}, {-3.5F, -2.0F, -7.05F, 0.0625F, 0.09375F}, {-3.5F, -1.0F, -7.05F, 0.0625F, 0.109375F}, {-3.5F, -1.0F, -7.05F, 0.0625F, 0.109375F}},
                {{-3.5F, -2.0F, -7.05F, 0.09375F, 0.09375F}, {-2.5F, -2.0F, -7.05F, 0.078125F, 0.09375F}, {-2.5F, -1.0F, -7.05F, 0.078125F, 0.109375F}, {-3.5F, -1.0F, -7.05F, 0.09375F, 0.109375F}},
                {{-2.5F, -2.0F, -7.05F, 0.078125F, 0.09375F}, {-2.5F, -2.0F, -7.05F, 0.078125F, 0.09375F}, {-2.5F, -1.0F, -7.05F, 0.078125F, 0.109375F}, {-2.5F, -1.0F, -7.05F, 0.078125F, 0.109375F}},
                {{-3.5F, -2.0F, -7.05F, 0.0625F, 0.09375F}, {-2.5F, -2.0F, -7.05F, 0.078125F, 0.09375F}, {-2.5F, -2.0F, -7.05F, 0.078125F, 0.09375F}, {-3.5F, -2.0F, -7.05F, 0.0625F, 0.09375F}},
                {{-3.5F, -1.0F, -7.05F, 0.078125F, 0.09375F}, {-2.5F, -1.0F, -7.05F, 0.09375F, 0.09375F}, {-2.5F, -1.0F, -7.05F, 0.09375F, 0.09375F}, {-3.5F, -1.0F, -7.05F, 0.078125F, 0.09375F}}
        }));
        p12Cubes.add(new ReferenceMeshCube(2.5F, -2.0F, -7.05F, 1.0F, 1.0F, 0.0F, new float[][][] {
                {{3.5F, -2.0F, -7.05F, 0.078125F, 0.09375F}, {2.5F, -2.0F, -7.05F, 0.0625F, 0.09375F}, {2.5F, -1.0F, -7.05F, 0.0625F, 0.109375F}, {3.5F, -1.0F, -7.05F, 0.078125F, 0.109375F}},
                {{2.5F, -2.0F, -7.05F, 0.0625F, 0.09375F}, {2.5F, -2.0F, -7.05F, 0.0625F, 0.09375F}, {2.5F, -1.0F, -7.05F, 0.0625F, 0.109375F}, {2.5F, -1.0F, -7.05F, 0.0625F, 0.109375F}},
                {{2.5F, -2.0F, -7.05F, 0.09375F, 0.09375F}, {3.5F, -2.0F, -7.05F, 0.078125F, 0.09375F}, {3.5F, -1.0F, -7.05F, 0.078125F, 0.109375F}, {2.5F, -1.0F, -7.05F, 0.09375F, 0.109375F}},
                {{3.5F, -2.0F, -7.05F, 0.078125F, 0.09375F}, {3.5F, -2.0F, -7.05F, 0.078125F, 0.09375F}, {3.5F, -1.0F, -7.05F, 0.078125F, 0.109375F}, {3.5F, -1.0F, -7.05F, 0.078125F, 0.109375F}},
                {{2.5F, -2.0F, -7.05F, 0.0625F, 0.09375F}, {3.5F, -2.0F, -7.05F, 0.078125F, 0.09375F}, {3.5F, -2.0F, -7.05F, 0.078125F, 0.09375F}, {2.5F, -2.0F, -7.05F, 0.0625F, 0.09375F}},
                {{2.5F, -1.0F, -7.05F, 0.078125F, 0.09375F}, {3.5F, -1.0F, -7.05F, 0.09375F, 0.09375F}, {3.5F, -1.0F, -7.05F, 0.09375F, 0.09375F}, {2.5F, -1.0F, -7.05F, 0.078125F, 0.09375F}}
        }));
        p12Cubes.add(new ReferenceMeshCube(-3.5F, -3.0F, -7.0F, 7.0F, 0.0F, 7.0F, new float[][][] {
                {{3.5F, -3.0F, -7.0F, 0.4375F, 0.109375F}, {-3.5F, -3.0F, -7.0F, 0.328125F, 0.109375F}, {-3.5F, -3.0F, -7.0F, 0.328125F, 0.109375F}, {3.5F, -3.0F, -7.0F, 0.4375F, 0.109375F}},
                {{-3.5F, -3.0F, -7.0F, 0.328125F, 0.109375F}, {-3.5F, -3.0F, 0.0F, 0.21875F, 0.109375F}, {-3.5F, -3.0F, 0.0F, 0.21875F, 0.109375F}, {-3.5F, -3.0F, -7.0F, 0.328125F, 0.109375F}},
                {{-3.5F, -3.0F, 0.0F, 0.65625F, 0.109375F}, {3.5F, -3.0F, 0.0F, 0.546875F, 0.109375F}, {3.5F, -3.0F, 0.0F, 0.546875F, 0.109375F}, {-3.5F, -3.0F, 0.0F, 0.65625F, 0.109375F}},
                {{3.5F, -3.0F, 0.0F, 0.546875F, 0.109375F}, {3.5F, -3.0F, -7.0F, 0.4375F, 0.109375F}, {3.5F, -3.0F, -7.0F, 0.4375F, 0.109375F}, {3.5F, -3.0F, 0.0F, 0.546875F, 0.109375F}},
                {{-3.5F, -3.0F, -7.0F, 0.328125F, 0.109375F}, {3.5F, -3.0F, -7.0F, 0.4375F, 0.109375F}, {3.5F, -3.0F, 0.0F, 0.4375F, 0.0F}, {-3.5F, -3.0F, 0.0F, 0.328125F, 0.0F}},
                {{-3.5F, -3.0F, 0.0F, 0.4375F, 0.0F}, {3.5F, -3.0F, 0.0F, 0.546875F, 0.0F}, {3.5F, -3.0F, -7.0F, 0.546875F, 0.109375F}, {-3.5F, -3.0F, -7.0F, 0.4375F, 0.109375F}}
        }));
        var p12 = new net.minecraft.client.model.geom.ModelPart(p12Cubes, java.util.Map.ofEntries());
        p12.setInitialPose(PartPose.offsetAndRotation(0.0F, -4.0F, 2.5F, 0.0F, 0.0F, 0.0F)); p12.resetPose();
        var p11Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/body/mini/headred", java.util.List.of()));
        p11Cubes.add(new ReferenceMeshCube(-3.5F, -6.0F, -4.5F, 7.0F, 6.0F, 7.0F, new float[][][] {
                {{3.5F, -6.0F, -4.5F, 0.21875F, 0.3125F}, {-3.5F, -6.0F, -4.5F, 0.109375F, 0.3125F}, {-3.5F, 0.0F, -4.5F, 0.109375F, 0.40625F}, {3.5F, 0.0F, -4.5F, 0.21875F, 0.40625F}},
                {{-3.5F, -6.0F, -4.5F, 0.109375F, 0.3125F}, {-3.5F, -6.0F, 2.5F, 0.0F, 0.3125F}, {-3.5F, 0.0F, 2.5F, 0.0F, 0.40625F}, {-3.5F, 0.0F, -4.5F, 0.109375F, 0.40625F}},
                {{-3.5F, -6.0F, 2.5F, 0.4375F, 0.3125F}, {3.5F, -6.0F, 2.5F, 0.328125F, 0.3125F}, {3.5F, 0.0F, 2.5F, 0.328125F, 0.40625F}, {-3.5F, 0.0F, 2.5F, 0.4375F, 0.40625F}},
                {{3.5F, -6.0F, 2.5F, 0.328125F, 0.3125F}, {3.5F, -6.0F, -4.5F, 0.21875F, 0.3125F}, {3.5F, 0.0F, -4.5F, 0.21875F, 0.40625F}, {3.5F, 0.0F, 2.5F, 0.328125F, 0.40625F}},
                {{-3.5F, -6.0F, -4.5F, 0.109375F, 0.3125F}, {3.5F, -6.0F, -4.5F, 0.21875F, 0.3125F}, {3.5F, -6.0F, 2.5F, 0.21875F, 0.203125F}, {-3.5F, -6.0F, 2.5F, 0.109375F, 0.203125F}},
                {{-3.5F, 0.0F, 2.5F, 0.21875F, 0.203125F}, {3.5F, 0.0F, 2.5F, 0.328125F, 0.203125F}, {3.5F, 0.0F, -4.5F, 0.328125F, 0.3125F}, {-3.5F, 0.0F, -4.5F, 0.21875F, 0.3125F}}
        }));
        p11Cubes.add(new ReferenceMeshCube(-3.5F, -3.0F, -4.5F, 7.0F, 0.0F, 7.0F, new float[][][] {
                {{3.5F, -3.0F, -4.5F, 0.4375F, 0.109375F}, {-3.5F, -3.0F, -4.5F, 0.328125F, 0.109375F}, {-3.5F, -3.0F, -4.5F, 0.328125F, 0.109375F}, {3.5F, -3.0F, -4.5F, 0.4375F, 0.109375F}},
                {{-3.5F, -3.0F, -4.5F, 0.328125F, 0.109375F}, {-3.5F, -3.0F, 2.5F, 0.21875F, 0.109375F}, {-3.5F, -3.0F, 2.5F, 0.21875F, 0.109375F}, {-3.5F, -3.0F, -4.5F, 0.328125F, 0.109375F}},
                {{-3.5F, -3.0F, 2.5F, 0.65625F, 0.109375F}, {3.5F, -3.0F, 2.5F, 0.546875F, 0.109375F}, {3.5F, -3.0F, 2.5F, 0.546875F, 0.109375F}, {-3.5F, -3.0F, 2.5F, 0.65625F, 0.109375F}},
                {{3.5F, -3.0F, 2.5F, 0.546875F, 0.109375F}, {3.5F, -3.0F, -4.5F, 0.4375F, 0.109375F}, {3.5F, -3.0F, -4.5F, 0.4375F, 0.109375F}, {3.5F, -3.0F, 2.5F, 0.546875F, 0.109375F}},
                {{-3.5F, -3.0F, -4.5F, 0.328125F, 0.109375F}, {3.5F, -3.0F, -4.5F, 0.4375F, 0.109375F}, {3.5F, -3.0F, 2.5F, 0.4375F, 0.0F}, {-3.5F, -3.0F, 2.5F, 0.328125F, 0.0F}},
                {{-3.5F, -3.0F, 2.5F, 0.4375F, 0.0F}, {3.5F, -3.0F, 2.5F, 0.546875F, 0.0F}, {3.5F, -3.0F, -4.5F, 0.546875F, 0.109375F}, {-3.5F, -3.0F, -4.5F, 0.4375F, 0.109375F}}
        }));
        var p11 = new net.minecraft.client.model.geom.ModelPart(p11Cubes, java.util.Map.ofEntries(java.util.Map.entry("bone14", p12)));
        p11.setInitialPose(PartPose.offsetAndRotation(0.0F, -4.0F, 1.0F, 0.0F, 0.0F, 0.0F)); p11.resetPose();
        var p10Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/body/mini", java.util.List.of()));
        var p10 = new net.minecraft.client.model.geom.ModelPart(p10Cubes, java.util.Map.ofEntries(java.util.Map.entry("headred", p11), java.util.Map.entry("right_leg3", p13), java.util.Map.entry("left_leg3", p14), java.util.Map.entry("right_hand", p15), java.util.Map.entry("left_hand", p18), java.util.Map.entry("headwood", p19), java.util.Map.entry("right_leg4", p21), java.util.Map.entry("left_leg4", p22)));
        p10.setInitialPose(PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p10.resetPose();
        var p9Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/body", java.util.List.of()));
        var p9 = new net.minecraft.client.model.geom.ModelPart(p9Cubes, java.util.Map.ofEntries(java.util.Map.entry("mini", p10)));
        p9.setInitialPose(PartPose.offsetAndRotation(0.0F, 8.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p9.resetPose();
        var p8Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_head/head", java.util.List.of()));
        var p8 = new net.minecraft.client.model.geom.ModelPart(p8Cubes, java.util.Map.ofEntries());
        p8.setInitialPose(PartPose.offsetAndRotation(0.0F, 9.0F, 4.0F, 0.0F, 0.0F, 0.0F)); p8.resetPose();
        var p7Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_chin", java.util.List.of()));
        var p7 = new net.minecraft.client.model.geom.ModelPart(p7Cubes, java.util.Map.ofEntries(java.util.Map.entry("chin", p28)));
        p7.setInitialPose(nativeRoot.getChild("red_thing").storePose()); p7.resetPose();
        var p6Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_bill", java.util.List.of()));
        var p6 = new net.minecraft.client.model.geom.ModelPart(p6Cubes, java.util.Map.ofEntries(java.util.Map.entry("bill", p27)));
        p6.setInitialPose(nativeRoot.getChild("beak").storePose()); p6.resetPose();
        var p5Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_left_wing", java.util.List.of()));
        var p5 = new net.minecraft.client.model.geom.ModelPart(p5Cubes, java.util.Map.ofEntries(java.util.Map.entry("left_wing", p26)));
        p5.setInitialPose(nativeRoot.getChild("left_wing").storePose()); p5.resetPose();
        var p4Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_right_wing", java.util.List.of()));
        var p4 = new net.minecraft.client.model.geom.ModelPart(p4Cubes, java.util.Map.ofEntries(java.util.Map.entry("right_wing", p25)));
        p4.setInitialPose(nativeRoot.getChild("right_wing").storePose()); p4.resetPose();
        var p3Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_left_leg", java.util.List.of()));
        var p3 = new net.minecraft.client.model.geom.ModelPart(p3Cubes, java.util.Map.ofEntries(java.util.Map.entry("left_leg", p24)));
        p3.setInitialPose(nativeRoot.getChild("left_leg").storePose()); p3.resetPose();
        var p2Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_right_leg", java.util.List.of()));
        var p2 = new net.minecraft.client.model.geom.ModelPart(p2Cubes, java.util.Map.ofEntries(java.util.Map.entry("right_leg", p23)));
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
