/*
 * Generated adaptation of ReCrafted Mobs 11.0 by GillsBills and credited contributors.
 * Asset license: CC-BY-NC-SA-4.0; https://creativecommons.org/licenses/by-nc-sa/4.0/
 * Source: https://modrinth.com/resourcepack/recrafted-mobs
 * Model: assets/minecraft/optifine/cem/wither/wither6.jem
 * SHA256: cb8559e107d6c5ece3db867582b2ecd18ec1cceda12f68d2750da153433ae9bd
 * Changes: CEM-to-Java conversion, explicit vanilla host transforms and runtime bindings.
 * Full credits, source hashes and additional changes: design/cem-model-imports.json.
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

public final class CemHeartOfEnderGeometry {
    private CemHeartOfEnderGeometry() {}

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        PartDefinition p0 = root.addOrReplaceChild("__cem_host_body1", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p1 = root.addOrReplaceChild("__cem_host_body2", CubeListBuilder.create(), PartPose.offsetAndRotation(-2.0F, 6.9F, -0.5F, 0.20420352F, 0.0F, 0.0F));
        PartDefinition p2 = root.addOrReplaceChild("__cem_host_body3", CubeListBuilder.create(), PartPose.offsetAndRotation(-2.0F, 16.6922281F, 1.52787293F, 0.83252203F, 0.0F, 0.0F));
        PartDefinition p3 = root.addOrReplaceChild("__cem_host_head1", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p4 = root.addOrReplaceChild("__cem_host_head2", CubeListBuilder.create(), PartPose.offsetAndRotation(-8.0F, 4.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p5 = root.addOrReplaceChild("__cem_host_head3", CubeListBuilder.create(), PartPose.offsetAndRotation(10.0F, 4.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p6 = p0.addOrReplaceChild("body1", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 24.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p7 = p6.addOrReplaceChild("ender", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p8 = p7.addOrReplaceChild("inner_body", CubeListBuilder.create(), PartPose.offsetAndRotation(10.0F, -24.0F, -11.5F, 0.0F, 0.0F, 0.0F));
        PartDefinition p9 = p8.addOrReplaceChild("top_body", CubeListBuilder.create(), PartPose.offsetAndRotation(-10.0F, 19.0F, 10.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p10 = p9.addOrReplaceChild("ender2", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -19.0F, -10.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p11 = p10.addOrReplaceChild("head5", CubeListBuilder.create(), PartPose.offsetAndRotation(-0.5F, -18.0F, 2.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p12 = p10.addOrReplaceChild("left_arm4", CubeListBuilder.create(), PartPose.offsetAndRotation(10.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p13 = p12.addOrReplaceChild("arm13", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -11.0F, 11.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p14 = p13.addOrReplaceChild("arm14", CubeListBuilder.create(), PartPose.offsetAndRotation(35.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p15 = p12.addOrReplaceChild("arm15", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 7.0F, 11.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p16 = p15.addOrReplaceChild("arm16", CubeListBuilder.create(), PartPose.offsetAndRotation(35.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p17 = p10.addOrReplaceChild("right_arm4", CubeListBuilder.create(), PartPose.offsetAndRotation(-10.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p18 = p17.addOrReplaceChild("arm19", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -11.0F, 11.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p19 = p18.addOrReplaceChild("body1_sub_3", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p20 = p18.addOrReplaceChild("arm20", CubeListBuilder.create(), PartPose.offsetAndRotation(-35.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p21 = p20.addOrReplaceChild("body1_sub_4", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p22 = p17.addOrReplaceChild("arm21", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 7.0F, 11.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p23 = p22.addOrReplaceChild("body1_sub_5", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p24 = p22.addOrReplaceChild("arm22", CubeListBuilder.create(), PartPose.offsetAndRotation(-35.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p25 = p24.addOrReplaceChild("body1_sub_6", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p26 = p8.addOrReplaceChild("left_arm3", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p27 = p26.addOrReplaceChild("arm5", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 25.0F, 11.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p28 = p27.addOrReplaceChild("arm6", CubeListBuilder.create(), PartPose.offsetAndRotation(35.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p29 = p8.addOrReplaceChild("right_arm3", CubeListBuilder.create(), PartPose.offsetAndRotation(-20.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p30 = p29.addOrReplaceChild("arm11", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 25.0F, 11.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p31 = p30.addOrReplaceChild("body1_sub_67", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p32 = p30.addOrReplaceChild("arm12", CubeListBuilder.create(), PartPose.offsetAndRotation(-35.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p33 = p32.addOrReplaceChild("body1_sub_69", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p34 = p8.addOrReplaceChild("arch_illager", CubeListBuilder.create(), PartPose.offsetAndRotation(-10.0F, 54.0F, 11.5F, 0.0F, 0.0F, 0.0F));
        PartDefinition p35 = p34.addOrReplaceChild("head4", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -10.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p36 = p34.addOrReplaceChild("body5", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p37 = p36.addOrReplaceChild("necklace", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p38 = p37.addOrReplaceChild("bone12", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -5.0F, -3.0F, 0.0F, 0.0F, -0.392699082F));
        PartDefinition p39 = p37.addOrReplaceChild("bone13", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -5.0F, -3.0F, 0.0F, 0.0F, 0.392699082F));
        PartDefinition p40 = p39.addOrReplaceChild("body1_sub_79", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p41 = p36.addOrReplaceChild("right_leg2", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p42 = p36.addOrReplaceChild("left_leg2", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p43 = p42.addOrReplaceChild("body1_sub_82", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p44 = p36.addOrReplaceChild("right_arm5", CubeListBuilder.create(), PartPose.offsetAndRotation(-4.0F, -8.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p45 = p36.addOrReplaceChild("right_arm6", CubeListBuilder.create(), PartPose.offsetAndRotation(4.0F, -8.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p46 = p45.addOrReplaceChild("body1_sub_85", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p47 = p45.addOrReplaceChild("bone14", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 5.0F, -5.0F, 1.57079633F, 0.0F, 0.0F));
        PartDefinition p48 = p47.addOrReplaceChild("body1_sub_87", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p49 = p1.addOrReplaceChild("body2", CubeListBuilder.create(), PartPose.offsetAndRotation(3.0F, 17.1F, 0.5F, 0.0F, 0.0F, 0.0F));
        PartDefinition p50 = p2.addOrReplaceChild("body3", CubeListBuilder.create(), PartPose.offsetAndRotation(3.0F, 7.1F, 0.5F, 0.0F, 0.0F, 0.0F));
        PartDefinition p51 = p3.addOrReplaceChild("head1", CubeListBuilder.create(), PartPose.offsetAndRotation(1.0F, 24.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p52 = p4.addOrReplaceChild("head2", CubeListBuilder.create(), PartPose.offsetAndRotation(9.0F, 20.0F, 1.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p53 = p5.addOrReplaceChild("head3", CubeListBuilder.create(), PartPose.offsetAndRotation(-9.0F, 20.0F, 1.0F, 0.0F, 0.0F, 0.0F));
        return LayerDefinition.create(mesh, 256, 256);
    }

    /** Preserve source expression units; static scales are reapplied after every pose reset. */
    public static ReferenceMobDefinition.Builder configure(ReferenceMobDefinition.Builder builder) {
        builder.cemDriver("wither");
        builder.bakedRoot(CemHeartOfEnderGeometry::bakeRoot);
        builder.hostAlias("body1", "__cem_host_body1");
        builder.hostAlias("body2", "__cem_host_body2");
        builder.hostAlias("body3", "__cem_host_body3");
        builder.hostAlias("head1", "__cem_host_head1");
        builder.hostAlias("head2", "__cem_host_head2");
        builder.hostAlias("head3", "__cem_host_head3");
        builder.boneScale("ender", 0.5F);
        builder.expression("top_body.tx", "-10");
        builder.expression("top_body.ty", "20");
        builder.expression("ender.ty", "-15");
        builder.expression("arch_illager.ty", "50");
        builder.expression("top_body.tz", "10");
        builder.expression("head5.rx", "head1.rx + torad(7.5) + -sin(torad(12.5*3) + time/42) *0.20");
        builder.expression("head5.ry", "head1.ry");
        builder.expression("head5.rz", "head1.rz");
        builder.expression("arm13.ry", "torad(12.5) + -sin(torad(12.5*3) + time/42) *0.20");
        builder.expression("arm14.ry", "torad(12.5) + -sin(torad(12.5*3) + time/42) *0.20");
        builder.expression("arm15.ry", "torad(15) + -sin(torad(12.5*3) + time/42) *0.30");
        builder.expression("arm16.ry", "torad(15) + -sin(torad(12.5*3) + time/42) *0.30");
        builder.expression("arm5.ry", "torad(17.5) + -sin(torad(12.5*3) + time/42) *0.35");
        builder.expression("arm6.ry", "torad(17.5) + -sin(torad(12.5*3) + time/42) *0.35");
        builder.expression("arm19.ry", "torad(-12.5) + sin(torad(12.5*3) + time/42) *0.20");
        builder.expression("arm20.ry", "torad(-12.5) + sin(torad(12.5*3) + time/42) *0.20");
        builder.expression("arm21.ry", "torad(-15) + sin(torad(12.5*3) + time/42) *0.30");
        builder.expression("arm22.ry", "torad(-15) + sin(torad(12.5*3) + time/42) *0.30");
        builder.expression("arm11.ry", "torad(-17.5) + sin(torad(12.5*3) + time/42) *0.35");
        builder.expression("arm12.ry", "torad(-17.5) + sin(torad(12.5*3) + time/42) *0.35");
        builder.expression("top_body.rx", "torad(12.5) + sin(torad(12.5*3) + time/42) *0.20");
        builder.expression("ender.rx", "torad(0) +sin(torad(12.5*3) + time/42) *0.10");
        return builder;
    }

    /** Attach=true/unreplaced parts retain installed vanilla cubes without copying game assets. */
    public static net.minecraft.client.model.geom.ModelPart bakeRoot() {
        var nativeCubes = new java.util.HashMap<String, java.util.List<net.minecraft.client.model.geom.ModelPart.Cube>>();
        var nativeRoot = net.minecraft.client.model.WitherBossModel.createBodyLayer(CubeDeformation.NONE).bakeRoot();
        nativeRoot.visit(new com.mojang.blaze3d.vertex.PoseStack(),
                (pose, path, index, cube) -> nativeCubes.computeIfAbsent(path, key -> new java.util.ArrayList<>()).add(cube));
        var customCubes = new java.util.HashMap<String, java.util.List<net.minecraft.client.model.geom.ModelPart.Cube>>();
        createBodyLayer().bakeRoot().visit(new com.mojang.blaze3d.vertex.PoseStack(),
                (pose, path, index, cube) -> customCubes.computeIfAbsent(path, key -> new java.util.ArrayList<>()).add(cube));
        var p53Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_head3/head3", java.util.List.of()));
        var p53 = new net.minecraft.client.model.geom.ModelPart(p53Cubes, java.util.Map.ofEntries());
        p53.setInitialPose(PartPose.offsetAndRotation(-9.0F, 20.0F, 1.0F, 0.0F, 0.0F, 0.0F)); p53.resetPose();
        var p52Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_head2/head2", java.util.List.of()));
        var p52 = new net.minecraft.client.model.geom.ModelPart(p52Cubes, java.util.Map.ofEntries());
        p52.setInitialPose(PartPose.offsetAndRotation(9.0F, 20.0F, 1.0F, 0.0F, 0.0F, 0.0F)); p52.resetPose();
        var p51Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_head1/head1", java.util.List.of()));
        var p51 = new net.minecraft.client.model.geom.ModelPart(p51Cubes, java.util.Map.ofEntries());
        p51.setInitialPose(PartPose.offsetAndRotation(1.0F, 24.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p51.resetPose();
        var p50Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body3/body3", java.util.List.of()));
        var p50 = new net.minecraft.client.model.geom.ModelPart(p50Cubes, java.util.Map.ofEntries());
        p50.setInitialPose(PartPose.offsetAndRotation(3.0F, 7.1F, 0.5F, 0.0F, 0.0F, 0.0F)); p50.resetPose();
        var p49Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body2/body2", java.util.List.of()));
        var p49 = new net.minecraft.client.model.geom.ModelPart(p49Cubes, java.util.Map.ofEntries());
        p49.setInitialPose(PartPose.offsetAndRotation(3.0F, 17.1F, 0.5F, 0.0F, 0.0F, 0.0F)); p49.resetPose();
        var p48Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body1/body1/ender/inner_body/arch_illager/body5/right_arm6/bone14/body1_sub_87", java.util.List.of()));
        p48Cubes.add(new ReferenceMeshCube(0.0F, -2.0F, -0.5F, 1.0F, 18.0F, 1.0F, new float[][][] {
                {{0.0F, -2.0F, -0.5F, 0.375F, 0.080078125F}, {1.0F, -2.0F, -0.5F, 0.374023438F, 0.080078125F}, {1.0F, -2.0F, 0.5F, 0.374023438F, 0.078125F}, {0.0F, -2.0F, 0.5F, 0.375F, 0.078125F}},
                {{0.0F, 16.0F, 0.5F, 0.375976562F, 0.078125F}, {1.0F, 16.0F, 0.5F, 0.375F, 0.078125F}, {1.0F, 16.0F, -0.5F, 0.375F, 0.080078125F}, {0.0F, 16.0F, -0.5F, 0.375976562F, 0.080078125F}},
                {{1.0F, 16.0F, -0.5F, 0.374023438F, 0.115234375F}, {1.0F, 16.0F, 0.5F, 0.373046875F, 0.115234375F}, {1.0F, -2.0F, 0.5F, 0.373046875F, 0.080078125F}, {1.0F, -2.0F, -0.5F, 0.374023438F, 0.080078125F}},
                {{0.0F, 16.0F, 0.5F, 0.375976562F, 0.115234375F}, {0.0F, 16.0F, -0.5F, 0.375F, 0.115234375F}, {0.0F, -2.0F, -0.5F, 0.375F, 0.080078125F}, {0.0F, -2.0F, 0.5F, 0.375976562F, 0.080078125F}},
                {{0.0F, 16.0F, -0.5F, 0.375F, 0.115234375F}, {1.0F, 16.0F, -0.5F, 0.374023438F, 0.115234375F}, {1.0F, -2.0F, -0.5F, 0.374023438F, 0.080078125F}, {0.0F, -2.0F, -0.5F, 0.375F, 0.080078125F}},
                {{1.0F, 16.0F, 0.5F, 0.376953125F, 0.115234375F}, {0.0F, 16.0F, 0.5F, 0.375976562F, 0.115234375F}, {0.0F, -2.0F, 0.5F, 0.375976562F, 0.080078125F}, {1.0F, -2.0F, 0.5F, 0.376953125F, 0.080078125F}}
        }));
        p48Cubes.add(new ReferenceMeshCube(-1.0F, -3.0F, -1.5F, 3.0F, 1.0F, 3.0F, new float[][][] {
                {{-1.0F, -3.0F, -1.5F, 0.3828125F, 0.083984375F}, {2.0F, -3.0F, -1.5F, 0.379882812F, 0.083984375F}, {2.0F, -3.0F, 1.5F, 0.379882812F, 0.078125F}, {-1.0F, -3.0F, 1.5F, 0.3828125F, 0.078125F}},
                {{-1.0F, -2.0F, 1.5F, 0.385742188F, 0.078125F}, {2.0F, -2.0F, 1.5F, 0.3828125F, 0.078125F}, {2.0F, -2.0F, -1.5F, 0.3828125F, 0.083984375F}, {-1.0F, -2.0F, -1.5F, 0.385742188F, 0.083984375F}},
                {{2.0F, -2.0F, -1.5F, 0.379882812F, 0.0859375F}, {2.0F, -2.0F, 1.5F, 0.376953125F, 0.0859375F}, {2.0F, -3.0F, 1.5F, 0.376953125F, 0.083984375F}, {2.0F, -3.0F, -1.5F, 0.379882812F, 0.083984375F}},
                {{-1.0F, -2.0F, 1.5F, 0.385742188F, 0.0859375F}, {-1.0F, -2.0F, -1.5F, 0.3828125F, 0.0859375F}, {-1.0F, -3.0F, -1.5F, 0.3828125F, 0.083984375F}, {-1.0F, -3.0F, 1.5F, 0.385742188F, 0.083984375F}},
                {{-1.0F, -2.0F, -1.5F, 0.3828125F, 0.0859375F}, {2.0F, -2.0F, -1.5F, 0.379882812F, 0.0859375F}, {2.0F, -3.0F, -1.5F, 0.379882812F, 0.083984375F}, {-1.0F, -3.0F, -1.5F, 0.3828125F, 0.083984375F}},
                {{2.0F, -2.0F, 1.5F, 0.388671875F, 0.0859375F}, {-1.0F, -2.0F, 1.5F, 0.385742188F, 0.0859375F}, {-1.0F, -3.0F, 1.5F, 0.385742188F, 0.083984375F}, {2.0F, -3.0F, 1.5F, 0.388671875F, 0.083984375F}}
        }));
        var p48 = new net.minecraft.client.model.geom.ModelPart(p48Cubes, java.util.Map.ofEntries());
        p48.setInitialPose(PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p48.resetPose();
        var p47Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body1/body1/ender/inner_body/arch_illager/body5/right_arm6/bone14", java.util.List.of()));
        var p47 = new net.minecraft.client.model.geom.ModelPart(p47Cubes, java.util.Map.ofEntries(java.util.Map.entry("body1_sub_87", p48)));
        p47.setInitialPose(PartPose.offsetAndRotation(0.0F, 5.0F, -5.0F, 1.57079633F, 0.0F, 0.0F)); p47.resetPose();
        var p46Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body1/body1/ender/inner_body/arch_illager/body5/right_arm6/body1_sub_85", java.util.List.of()));
        p46Cubes.add(new ReferenceMeshCube(0.0F, -2.0F, -1.5F, 3.0F, 4.0F, 3.0F, new float[][][] {
                {{0.0F, -2.0F, -1.5F, 0.405273438F, 0.078125F}, {3.0F, -2.0F, -1.5F, 0.40234375F, 0.078125F}, {3.0F, -2.0F, 1.5F, 0.40234375F, 0.072265625F}, {0.0F, -2.0F, 1.5F, 0.405273438F, 0.072265625F}},
                {{0.0F, 2.0F, 1.5F, 0.408203125F, 0.072265625F}, {3.0F, 2.0F, 1.5F, 0.405273438F, 0.072265625F}, {3.0F, 2.0F, -1.5F, 0.405273438F, 0.078125F}, {0.0F, 2.0F, -1.5F, 0.408203125F, 0.078125F}},
                {{3.0F, 2.0F, -1.5F, 0.40234375F, 0.0859375F}, {3.0F, 2.0F, 1.5F, 0.399414062F, 0.0859375F}, {3.0F, -2.0F, 1.5F, 0.399414062F, 0.078125F}, {3.0F, -2.0F, -1.5F, 0.40234375F, 0.078125F}},
                {{0.0F, 2.0F, 1.5F, 0.408203125F, 0.0859375F}, {0.0F, 2.0F, -1.5F, 0.405273438F, 0.0859375F}, {0.0F, -2.0F, -1.5F, 0.405273438F, 0.078125F}, {0.0F, -2.0F, 1.5F, 0.408203125F, 0.078125F}},
                {{0.0F, 2.0F, -1.5F, 0.405273438F, 0.0859375F}, {3.0F, 2.0F, -1.5F, 0.40234375F, 0.0859375F}, {3.0F, -2.0F, -1.5F, 0.40234375F, 0.078125F}, {0.0F, -2.0F, -1.5F, 0.405273438F, 0.078125F}},
                {{3.0F, 2.0F, 1.5F, 0.411132812F, 0.0859375F}, {0.0F, 2.0F, 1.5F, 0.408203125F, 0.0859375F}, {0.0F, -2.0F, 1.5F, 0.408203125F, 0.078125F}, {3.0F, -2.0F, 1.5F, 0.411132812F, 0.078125F}}
        }));
        p46Cubes.add(new ReferenceMeshCube(1.0F, -2.0F, -0.5F, 1.0F, 8.0F, 1.0F, new float[][][] {
                {{1.0F, -2.0F, -0.5F, 0.380859375F, 0.03515625F}, {2.0F, -2.0F, -0.5F, 0.379882812F, 0.03515625F}, {2.0F, -2.0F, 0.5F, 0.379882812F, 0.033203125F}, {1.0F, -2.0F, 0.5F, 0.380859375F, 0.033203125F}},
                {{1.0F, 6.0F, 0.5F, 0.381835938F, 0.033203125F}, {2.0F, 6.0F, 0.5F, 0.380859375F, 0.033203125F}, {2.0F, 6.0F, -0.5F, 0.380859375F, 0.03515625F}, {1.0F, 6.0F, -0.5F, 0.381835938F, 0.03515625F}},
                {{2.0F, 6.0F, -0.5F, 0.379882812F, 0.05078125F}, {2.0F, 6.0F, 0.5F, 0.37890625F, 0.05078125F}, {2.0F, -2.0F, 0.5F, 0.37890625F, 0.03515625F}, {2.0F, -2.0F, -0.5F, 0.379882812F, 0.03515625F}},
                {{1.0F, 6.0F, 0.5F, 0.381835938F, 0.05078125F}, {1.0F, 6.0F, -0.5F, 0.380859375F, 0.05078125F}, {1.0F, -2.0F, -0.5F, 0.380859375F, 0.03515625F}, {1.0F, -2.0F, 0.5F, 0.381835938F, 0.03515625F}},
                {{1.0F, 6.0F, -0.5F, 0.380859375F, 0.05078125F}, {2.0F, 6.0F, -0.5F, 0.379882812F, 0.05078125F}, {2.0F, -2.0F, -0.5F, 0.379882812F, 0.03515625F}, {1.0F, -2.0F, -0.5F, 0.380859375F, 0.03515625F}},
                {{2.0F, 6.0F, 0.5F, 0.3828125F, 0.05078125F}, {1.0F, 6.0F, 0.5F, 0.381835938F, 0.05078125F}, {1.0F, -2.0F, 0.5F, 0.381835938F, 0.03515625F}, {2.0F, -2.0F, 0.5F, 0.3828125F, 0.03515625F}}
        }));
        var p46 = new net.minecraft.client.model.geom.ModelPart(p46Cubes, java.util.Map.ofEntries());
        p46.setInitialPose(PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p46.resetPose();
        var p45Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body1/body1/ender/inner_body/arch_illager/body5/right_arm6", java.util.List.of()));
        var p45 = new net.minecraft.client.model.geom.ModelPart(p45Cubes, java.util.Map.ofEntries(java.util.Map.entry("body1_sub_85", p46), java.util.Map.entry("bone14", p47)));
        p45.setInitialPose(PartPose.offsetAndRotation(4.0F, -8.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p45.resetPose();
        var p44Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body1/body1/ender/inner_body/arch_illager/body5/right_arm5", java.util.List.of()));
        p44Cubes.add(new ReferenceMeshCube(-3.0F, -2.0F, -1.5F, 3.0F, 4.0F, 3.0F, new float[][][] {
                {{0.0F, -2.0F, 1.5F, 0.405273438F, 0.072265625F}, {-3.0F, -2.0F, 1.5F, 0.40234375F, 0.072265625F}, {-3.0F, -2.0F, -1.5F, 0.40234375F, 0.078125F}, {0.0F, -2.0F, -1.5F, 0.405273438F, 0.078125F}},
                {{0.0F, 2.0F, -1.5F, 0.408203125F, 0.078125F}, {-3.0F, 2.0F, -1.5F, 0.405273438F, 0.078125F}, {-3.0F, 2.0F, 1.5F, 0.405273438F, 0.072265625F}, {0.0F, 2.0F, 1.5F, 0.408203125F, 0.072265625F}},
                {{-3.0F, -2.0F, -1.5F, 0.40234375F, 0.078125F}, {-3.0F, -2.0F, 1.5F, 0.399414062F, 0.078125F}, {-3.0F, 2.0F, 1.5F, 0.399414062F, 0.0859375F}, {-3.0F, 2.0F, -1.5F, 0.40234375F, 0.0859375F}},
                {{0.0F, -2.0F, 1.5F, 0.408203125F, 0.078125F}, {0.0F, -2.0F, -1.5F, 0.405273438F, 0.078125F}, {0.0F, 2.0F, -1.5F, 0.405273438F, 0.0859375F}, {0.0F, 2.0F, 1.5F, 0.408203125F, 0.0859375F}},
                {{0.0F, -2.0F, -1.5F, 0.405273438F, 0.078125F}, {-3.0F, -2.0F, -1.5F, 0.40234375F, 0.078125F}, {-3.0F, 2.0F, -1.5F, 0.40234375F, 0.0859375F}, {0.0F, 2.0F, -1.5F, 0.405273438F, 0.0859375F}},
                {{-3.0F, -2.0F, 1.5F, 0.411132812F, 0.078125F}, {0.0F, -2.0F, 1.5F, 0.408203125F, 0.078125F}, {0.0F, 2.0F, 1.5F, 0.408203125F, 0.0859375F}, {-3.0F, 2.0F, 1.5F, 0.411132812F, 0.0859375F}}
        }));
        p44Cubes.add(new ReferenceMeshCube(-2.0F, -2.0F, -0.5F, 1.0F, 8.0F, 1.0F, new float[][][] {
                {{-1.0F, -2.0F, 0.5F, 0.380859375F, 0.033203125F}, {-2.0F, -2.0F, 0.5F, 0.379882812F, 0.033203125F}, {-2.0F, -2.0F, -0.5F, 0.379882812F, 0.03515625F}, {-1.0F, -2.0F, -0.5F, 0.380859375F, 0.03515625F}},
                {{-1.0F, 6.0F, -0.5F, 0.381835938F, 0.03515625F}, {-2.0F, 6.0F, -0.5F, 0.380859375F, 0.03515625F}, {-2.0F, 6.0F, 0.5F, 0.380859375F, 0.033203125F}, {-1.0F, 6.0F, 0.5F, 0.381835938F, 0.033203125F}},
                {{-2.0F, -2.0F, -0.5F, 0.379882812F, 0.03515625F}, {-2.0F, -2.0F, 0.5F, 0.37890625F, 0.03515625F}, {-2.0F, 6.0F, 0.5F, 0.37890625F, 0.05078125F}, {-2.0F, 6.0F, -0.5F, 0.379882812F, 0.05078125F}},
                {{-1.0F, -2.0F, 0.5F, 0.381835938F, 0.03515625F}, {-1.0F, -2.0F, -0.5F, 0.380859375F, 0.03515625F}, {-1.0F, 6.0F, -0.5F, 0.380859375F, 0.05078125F}, {-1.0F, 6.0F, 0.5F, 0.381835938F, 0.05078125F}},
                {{-1.0F, -2.0F, -0.5F, 0.380859375F, 0.03515625F}, {-2.0F, -2.0F, -0.5F, 0.379882812F, 0.03515625F}, {-2.0F, 6.0F, -0.5F, 0.379882812F, 0.05078125F}, {-1.0F, 6.0F, -0.5F, 0.380859375F, 0.05078125F}},
                {{-2.0F, -2.0F, 0.5F, 0.3828125F, 0.03515625F}, {-1.0F, -2.0F, 0.5F, 0.381835938F, 0.03515625F}, {-1.0F, 6.0F, 0.5F, 0.381835938F, 0.05078125F}, {-2.0F, 6.0F, 0.5F, 0.3828125F, 0.05078125F}}
        }));
        var p44 = new net.minecraft.client.model.geom.ModelPart(p44Cubes, java.util.Map.ofEntries());
        p44.setInitialPose(PartPose.offsetAndRotation(-4.0F, -8.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p44.resetPose();
        var p43Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body1/body1/ender/inner_body/arch_illager/body5/left_leg2/body1_sub_82", java.util.List.of()));
        p43Cubes.add(new ReferenceMeshCube(0.0F, -3.0F, -1.5F, 3.0F, 3.0F, 3.0F, new float[][][] {
                {{0.0F, -3.0F, -1.5F, 0.388671875F, 0.0390625F}, {3.0F, -3.0F, -1.5F, 0.385742188F, 0.0390625F}, {3.0F, -3.0F, 1.5F, 0.385742188F, 0.033203125F}, {0.0F, -3.0F, 1.5F, 0.388671875F, 0.033203125F}},
                {{0.0F, 0.0F, 1.5F, 0.391601562F, 0.033203125F}, {3.0F, 0.0F, 1.5F, 0.388671875F, 0.033203125F}, {3.0F, 0.0F, -1.5F, 0.388671875F, 0.0390625F}, {0.0F, 0.0F, -1.5F, 0.391601562F, 0.0390625F}},
                {{3.0F, 0.0F, -1.5F, 0.385742188F, 0.044921875F}, {3.0F, 0.0F, 1.5F, 0.3828125F, 0.044921875F}, {3.0F, -3.0F, 1.5F, 0.3828125F, 0.0390625F}, {3.0F, -3.0F, -1.5F, 0.385742188F, 0.0390625F}},
                {{0.0F, 0.0F, 1.5F, 0.391601562F, 0.044921875F}, {0.0F, 0.0F, -1.5F, 0.388671875F, 0.044921875F}, {0.0F, -3.0F, -1.5F, 0.388671875F, 0.0390625F}, {0.0F, -3.0F, 1.5F, 0.391601562F, 0.0390625F}},
                {{0.0F, 0.0F, -1.5F, 0.388671875F, 0.044921875F}, {3.0F, 0.0F, -1.5F, 0.385742188F, 0.044921875F}, {3.0F, -3.0F, -1.5F, 0.385742188F, 0.0390625F}, {0.0F, -3.0F, -1.5F, 0.388671875F, 0.0390625F}},
                {{3.0F, 0.0F, 1.5F, 0.39453125F, 0.044921875F}, {0.0F, 0.0F, 1.5F, 0.391601562F, 0.044921875F}, {0.0F, -3.0F, 1.5F, 0.391601562F, 0.0390625F}, {3.0F, -3.0F, 1.5F, 0.39453125F, 0.0390625F}}
        }));
        p43Cubes.add(new ReferenceMeshCube(1.0F, 0.0F, -0.5F, 1.0F, 3.0F, 1.0F, new float[][][] {
                {{1.0F, 0.0F, -0.5F, 0.375F, 0.044921875F}, {2.0F, 0.0F, -0.5F, 0.374023438F, 0.044921875F}, {2.0F, 0.0F, 0.5F, 0.374023438F, 0.04296875F}, {1.0F, 0.0F, 0.5F, 0.375F, 0.04296875F}},
                {{1.0F, 3.0F, 0.5F, 0.375976562F, 0.04296875F}, {2.0F, 3.0F, 0.5F, 0.375F, 0.04296875F}, {2.0F, 3.0F, -0.5F, 0.375F, 0.044921875F}, {1.0F, 3.0F, -0.5F, 0.375976562F, 0.044921875F}},
                {{2.0F, 3.0F, -0.5F, 0.374023438F, 0.05078125F}, {2.0F, 3.0F, 0.5F, 0.373046875F, 0.05078125F}, {2.0F, 0.0F, 0.5F, 0.373046875F, 0.044921875F}, {2.0F, 0.0F, -0.5F, 0.374023438F, 0.044921875F}},
                {{1.0F, 3.0F, 0.5F, 0.375976562F, 0.05078125F}, {1.0F, 3.0F, -0.5F, 0.375F, 0.05078125F}, {1.0F, 0.0F, -0.5F, 0.375F, 0.044921875F}, {1.0F, 0.0F, 0.5F, 0.375976562F, 0.044921875F}},
                {{1.0F, 3.0F, -0.5F, 0.375F, 0.05078125F}, {2.0F, 3.0F, -0.5F, 0.374023438F, 0.05078125F}, {2.0F, 0.0F, -0.5F, 0.374023438F, 0.044921875F}, {1.0F, 0.0F, -0.5F, 0.375F, 0.044921875F}},
                {{2.0F, 3.0F, 0.5F, 0.376953125F, 0.05078125F}, {1.0F, 3.0F, 0.5F, 0.375976562F, 0.05078125F}, {1.0F, 0.0F, 0.5F, 0.375976562F, 0.044921875F}, {2.0F, 0.0F, 0.5F, 0.376953125F, 0.044921875F}}
        }));
        p43Cubes.add(new ReferenceMeshCube(1.0F, 3.0F, -2.5F, 1.0F, 0.0F, 3.0F, new float[][][] {
                {{1.0F, 3.0F, -2.5F, 0.377929688F, 0.048828125F}, {2.0F, 3.0F, -2.5F, 0.376953125F, 0.048828125F}, {2.0F, 3.0F, 0.5F, 0.376953125F, 0.04296875F}, {1.0F, 3.0F, 0.5F, 0.377929688F, 0.04296875F}},
                {{1.0F, 3.0F, 0.5F, 0.37890625F, 0.04296875F}, {2.0F, 3.0F, 0.5F, 0.377929688F, 0.04296875F}, {2.0F, 3.0F, -2.5F, 0.377929688F, 0.048828125F}, {1.0F, 3.0F, -2.5F, 0.37890625F, 0.048828125F}},
                {{2.0F, 3.0F, -2.5F, 0.376953125F, 0.048828125F}, {2.0F, 3.0F, 0.5F, 0.374023438F, 0.048828125F}, {2.0F, 3.0F, 0.5F, 0.374023438F, 0.048828125F}, {2.0F, 3.0F, -2.5F, 0.376953125F, 0.048828125F}},
                {{1.0F, 3.0F, 0.5F, 0.380859375F, 0.048828125F}, {1.0F, 3.0F, -2.5F, 0.377929688F, 0.048828125F}, {1.0F, 3.0F, -2.5F, 0.377929688F, 0.048828125F}, {1.0F, 3.0F, 0.5F, 0.380859375F, 0.048828125F}},
                {{1.0F, 3.0F, -2.5F, 0.377929688F, 0.048828125F}, {2.0F, 3.0F, -2.5F, 0.376953125F, 0.048828125F}, {2.0F, 3.0F, -2.5F, 0.376953125F, 0.048828125F}, {1.0F, 3.0F, -2.5F, 0.377929688F, 0.048828125F}},
                {{2.0F, 3.0F, 0.5F, 0.381835938F, 0.048828125F}, {1.0F, 3.0F, 0.5F, 0.380859375F, 0.048828125F}, {1.0F, 3.0F, 0.5F, 0.380859375F, 0.048828125F}, {2.0F, 3.0F, 0.5F, 0.381835938F, 0.048828125F}}
        }));
        var p43 = new net.minecraft.client.model.geom.ModelPart(p43Cubes, java.util.Map.ofEntries());
        p43.setInitialPose(PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p43.resetPose();
        var p42Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body1/body1/ender/inner_body/arch_illager/body5/left_leg2", java.util.List.of()));
        var p42 = new net.minecraft.client.model.geom.ModelPart(p42Cubes, java.util.Map.ofEntries(java.util.Map.entry("body1_sub_82", p43)));
        p42.setInitialPose(PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p42.resetPose();
        var p41Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body1/body1/ender/inner_body/arch_illager/body5/right_leg2", java.util.List.of()));
        p41Cubes.add(new ReferenceMeshCube(-3.0F, -3.0F, -1.5F, 3.0F, 3.0F, 3.0F, new float[][][] {
                {{0.0F, -3.0F, 1.5F, 0.388671875F, 0.033203125F}, {-3.0F, -3.0F, 1.5F, 0.385742188F, 0.033203125F}, {-3.0F, -3.0F, -1.5F, 0.385742188F, 0.0390625F}, {0.0F, -3.0F, -1.5F, 0.388671875F, 0.0390625F}},
                {{0.0F, 0.0F, -1.5F, 0.391601562F, 0.0390625F}, {-3.0F, 0.0F, -1.5F, 0.388671875F, 0.0390625F}, {-3.0F, 0.0F, 1.5F, 0.388671875F, 0.033203125F}, {0.0F, 0.0F, 1.5F, 0.391601562F, 0.033203125F}},
                {{-3.0F, -3.0F, -1.5F, 0.385742188F, 0.0390625F}, {-3.0F, -3.0F, 1.5F, 0.3828125F, 0.0390625F}, {-3.0F, 0.0F, 1.5F, 0.3828125F, 0.044921875F}, {-3.0F, 0.0F, -1.5F, 0.385742188F, 0.044921875F}},
                {{0.0F, -3.0F, 1.5F, 0.391601562F, 0.0390625F}, {0.0F, -3.0F, -1.5F, 0.388671875F, 0.0390625F}, {0.0F, 0.0F, -1.5F, 0.388671875F, 0.044921875F}, {0.0F, 0.0F, 1.5F, 0.391601562F, 0.044921875F}},
                {{0.0F, -3.0F, -1.5F, 0.388671875F, 0.0390625F}, {-3.0F, -3.0F, -1.5F, 0.385742188F, 0.0390625F}, {-3.0F, 0.0F, -1.5F, 0.385742188F, 0.044921875F}, {0.0F, 0.0F, -1.5F, 0.388671875F, 0.044921875F}},
                {{-3.0F, -3.0F, 1.5F, 0.39453125F, 0.0390625F}, {0.0F, -3.0F, 1.5F, 0.391601562F, 0.0390625F}, {0.0F, 0.0F, 1.5F, 0.391601562F, 0.044921875F}, {-3.0F, 0.0F, 1.5F, 0.39453125F, 0.044921875F}}
        }));
        p41Cubes.add(new ReferenceMeshCube(-2.0F, 0.0F, -0.5F, 1.0F, 3.0F, 1.0F, new float[][][] {
                {{-1.0F, 0.0F, 0.5F, 0.375F, 0.04296875F}, {-2.0F, 0.0F, 0.5F, 0.374023438F, 0.04296875F}, {-2.0F, 0.0F, -0.5F, 0.374023438F, 0.044921875F}, {-1.0F, 0.0F, -0.5F, 0.375F, 0.044921875F}},
                {{-1.0F, 3.0F, -0.5F, 0.375976562F, 0.044921875F}, {-2.0F, 3.0F, -0.5F, 0.375F, 0.044921875F}, {-2.0F, 3.0F, 0.5F, 0.375F, 0.04296875F}, {-1.0F, 3.0F, 0.5F, 0.375976562F, 0.04296875F}},
                {{-2.0F, 0.0F, -0.5F, 0.374023438F, 0.044921875F}, {-2.0F, 0.0F, 0.5F, 0.373046875F, 0.044921875F}, {-2.0F, 3.0F, 0.5F, 0.373046875F, 0.05078125F}, {-2.0F, 3.0F, -0.5F, 0.374023438F, 0.05078125F}},
                {{-1.0F, 0.0F, 0.5F, 0.375976562F, 0.044921875F}, {-1.0F, 0.0F, -0.5F, 0.375F, 0.044921875F}, {-1.0F, 3.0F, -0.5F, 0.375F, 0.05078125F}, {-1.0F, 3.0F, 0.5F, 0.375976562F, 0.05078125F}},
                {{-1.0F, 0.0F, -0.5F, 0.375F, 0.044921875F}, {-2.0F, 0.0F, -0.5F, 0.374023438F, 0.044921875F}, {-2.0F, 3.0F, -0.5F, 0.374023438F, 0.05078125F}, {-1.0F, 3.0F, -0.5F, 0.375F, 0.05078125F}},
                {{-2.0F, 0.0F, 0.5F, 0.376953125F, 0.044921875F}, {-1.0F, 0.0F, 0.5F, 0.375976562F, 0.044921875F}, {-1.0F, 3.0F, 0.5F, 0.375976562F, 0.05078125F}, {-2.0F, 3.0F, 0.5F, 0.376953125F, 0.05078125F}}
        }));
        p41Cubes.add(new ReferenceMeshCube(-2.0F, 3.0F, -2.5F, 1.0F, 0.0F, 3.0F, new float[][][] {
                {{-1.0F, 3.0F, 0.5F, 0.377929688F, 0.04296875F}, {-2.0F, 3.0F, 0.5F, 0.376953125F, 0.04296875F}, {-2.0F, 3.0F, -2.5F, 0.376953125F, 0.048828125F}, {-1.0F, 3.0F, -2.5F, 0.377929688F, 0.048828125F}},
                {{-1.0F, 3.0F, -2.5F, 0.37890625F, 0.048828125F}, {-2.0F, 3.0F, -2.5F, 0.377929688F, 0.048828125F}, {-2.0F, 3.0F, 0.5F, 0.377929688F, 0.04296875F}, {-1.0F, 3.0F, 0.5F, 0.37890625F, 0.04296875F}},
                {{-2.0F, 3.0F, -2.5F, 0.376953125F, 0.048828125F}, {-2.0F, 3.0F, 0.5F, 0.374023438F, 0.048828125F}, {-2.0F, 3.0F, 0.5F, 0.374023438F, 0.048828125F}, {-2.0F, 3.0F, -2.5F, 0.376953125F, 0.048828125F}},
                {{-1.0F, 3.0F, 0.5F, 0.380859375F, 0.048828125F}, {-1.0F, 3.0F, -2.5F, 0.377929688F, 0.048828125F}, {-1.0F, 3.0F, -2.5F, 0.377929688F, 0.048828125F}, {-1.0F, 3.0F, 0.5F, 0.380859375F, 0.048828125F}},
                {{-1.0F, 3.0F, -2.5F, 0.377929688F, 0.048828125F}, {-2.0F, 3.0F, -2.5F, 0.376953125F, 0.048828125F}, {-2.0F, 3.0F, -2.5F, 0.376953125F, 0.048828125F}, {-1.0F, 3.0F, -2.5F, 0.377929688F, 0.048828125F}},
                {{-2.0F, 3.0F, 0.5F, 0.381835938F, 0.048828125F}, {-1.0F, 3.0F, 0.5F, 0.380859375F, 0.048828125F}, {-1.0F, 3.0F, 0.5F, 0.380859375F, 0.048828125F}, {-2.0F, 3.0F, 0.5F, 0.381835938F, 0.048828125F}}
        }));
        var p41 = new net.minecraft.client.model.geom.ModelPart(p41Cubes, java.util.Map.ofEntries());
        p41.setInitialPose(PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p41.resetPose();
        var p40Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body1/body1/ender/inner_body/arch_illager/body5/necklace/bone13/body1_sub_79", java.util.List.of()));
        p40Cubes.add(new ReferenceMeshCube(-0.5F, -3.5F, 0.975F, 2.0F, 3.0F, 0.0F, new float[][][] {
                {{-0.5F, -3.5F, 0.975F, 0.3984375F, 0.0390625F}, {1.5F, -3.5F, 0.975F, 0.395507812F, 0.0390625F}, {1.5F, -3.5F, 0.975F, 0.395507812F, 0.037109375F}, {-0.5F, -3.5F, 0.975F, 0.3984375F, 0.037109375F}},
                {{-0.5F, -0.5F, 0.975F, 0.401367188F, 0.037109375F}, {1.5F, -0.5F, 0.975F, 0.3984375F, 0.037109375F}, {1.5F, -0.5F, 0.975F, 0.3984375F, 0.0390625F}, {-0.5F, -0.5F, 0.975F, 0.401367188F, 0.0390625F}},
                {{1.5F, -0.5F, 0.975F, 0.395507812F, 0.046875F}, {1.5F, -0.5F, 0.975F, 0.39453125F, 0.046875F}, {1.5F, -3.5F, 0.975F, 0.39453125F, 0.0390625F}, {1.5F, -3.5F, 0.975F, 0.395507812F, 0.0390625F}},
                {{-0.5F, -0.5F, 0.975F, 0.399414062F, 0.046875F}, {-0.5F, -0.5F, 0.975F, 0.3984375F, 0.046875F}, {-0.5F, -3.5F, 0.975F, 0.3984375F, 0.0390625F}, {-0.5F, -3.5F, 0.975F, 0.399414062F, 0.0390625F}},
                {{-0.5F, -0.5F, 0.975F, 0.3984375F, 0.046875F}, {1.5F, -0.5F, 0.975F, 0.395507812F, 0.046875F}, {1.5F, -3.5F, 0.975F, 0.395507812F, 0.0390625F}, {-0.5F, -3.5F, 0.975F, 0.3984375F, 0.0390625F}},
                {{1.5F, -0.5F, 0.975F, 0.40234375F, 0.046875F}, {-0.5F, -0.5F, 0.975F, 0.399414062F, 0.046875F}, {-0.5F, -3.5F, 0.975F, 0.399414062F, 0.0390625F}, {1.5F, -3.5F, 0.975F, 0.40234375F, 0.0390625F}}
        }));
        var p40 = new net.minecraft.client.model.geom.ModelPart(p40Cubes, java.util.Map.ofEntries());
        p40.setInitialPose(PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p40.resetPose();
        var p39Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body1/body1/ender/inner_body/arch_illager/body5/necklace/bone13", java.util.List.of()));
        var p39 = new net.minecraft.client.model.geom.ModelPart(p39Cubes, java.util.Map.ofEntries(java.util.Map.entry("body1_sub_79", p40)));
        p39.setInitialPose(PartPose.offsetAndRotation(0.0F, -5.0F, -3.0F, 0.0F, 0.0F, 0.392699082F)); p39.resetPose();
        var p38Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body1/body1/ender/inner_body/arch_illager/body5/necklace/bone12", java.util.List.of()));
        p38Cubes.add(new ReferenceMeshCube(-1.5F, -3.5F, 0.975F, 2.0F, 3.0F, 0.0F, new float[][][] {
                {{0.5F, -3.5F, 0.975F, 0.3984375F, 0.037109375F}, {-1.5F, -3.5F, 0.975F, 0.395507812F, 0.037109375F}, {-1.5F, -3.5F, 0.975F, 0.395507812F, 0.0390625F}, {0.5F, -3.5F, 0.975F, 0.3984375F, 0.0390625F}},
                {{0.5F, -0.5F, 0.975F, 0.401367188F, 0.0390625F}, {-1.5F, -0.5F, 0.975F, 0.3984375F, 0.0390625F}, {-1.5F, -0.5F, 0.975F, 0.3984375F, 0.037109375F}, {0.5F, -0.5F, 0.975F, 0.401367188F, 0.037109375F}},
                {{-1.5F, -3.5F, 0.975F, 0.395507812F, 0.0390625F}, {-1.5F, -3.5F, 0.975F, 0.39453125F, 0.0390625F}, {-1.5F, -0.5F, 0.975F, 0.39453125F, 0.046875F}, {-1.5F, -0.5F, 0.975F, 0.395507812F, 0.046875F}},
                {{0.5F, -3.5F, 0.975F, 0.399414062F, 0.0390625F}, {0.5F, -3.5F, 0.975F, 0.3984375F, 0.0390625F}, {0.5F, -0.5F, 0.975F, 0.3984375F, 0.046875F}, {0.5F, -0.5F, 0.975F, 0.399414062F, 0.046875F}},
                {{0.5F, -3.5F, 0.975F, 0.3984375F, 0.0390625F}, {-1.5F, -3.5F, 0.975F, 0.395507812F, 0.0390625F}, {-1.5F, -0.5F, 0.975F, 0.395507812F, 0.046875F}, {0.5F, -0.5F, 0.975F, 0.3984375F, 0.046875F}},
                {{-1.5F, -3.5F, 0.975F, 0.40234375F, 0.0390625F}, {0.5F, -3.5F, 0.975F, 0.399414062F, 0.0390625F}, {0.5F, -0.5F, 0.975F, 0.399414062F, 0.046875F}, {-1.5F, -0.5F, 0.975F, 0.40234375F, 0.046875F}}
        }));
        var p38 = new net.minecraft.client.model.geom.ModelPart(p38Cubes, java.util.Map.ofEntries());
        p38.setInitialPose(PartPose.offsetAndRotation(0.0F, -5.0F, -3.0F, 0.0F, 0.0F, -0.392699082F)); p38.resetPose();
        var p37Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body1/body1/ender/inner_body/arch_illager/body5/necklace", java.util.List.of()));
        p37Cubes.add(new ReferenceMeshCube(-1.0F, -6.0F, -3.0F, 2.0F, 2.0F, 1.0F, new float[][][] {
                {{1.0F, -6.0F, -2.0F, 0.392578125F, 0.044921875F}, {-1.0F, -6.0F, -2.0F, 0.390625F, 0.044921875F}, {-1.0F, -6.0F, -3.0F, 0.390625F, 0.046875F}, {1.0F, -6.0F, -3.0F, 0.392578125F, 0.046875F}},
                {{1.0F, -4.0F, -3.0F, 0.39453125F, 0.046875F}, {-1.0F, -4.0F, -3.0F, 0.392578125F, 0.046875F}, {-1.0F, -4.0F, -2.0F, 0.392578125F, 0.044921875F}, {1.0F, -4.0F, -2.0F, 0.39453125F, 0.044921875F}},
                {{-1.0F, -6.0F, -3.0F, 0.390625F, 0.046875F}, {-1.0F, -6.0F, -2.0F, 0.389648438F, 0.046875F}, {-1.0F, -4.0F, -2.0F, 0.389648438F, 0.05078125F}, {-1.0F, -4.0F, -3.0F, 0.390625F, 0.05078125F}},
                {{1.0F, -6.0F, -2.0F, 0.393554688F, 0.046875F}, {1.0F, -6.0F, -3.0F, 0.392578125F, 0.046875F}, {1.0F, -4.0F, -3.0F, 0.392578125F, 0.05078125F}, {1.0F, -4.0F, -2.0F, 0.393554688F, 0.05078125F}},
                {{1.0F, -6.0F, -3.0F, 0.392578125F, 0.046875F}, {-1.0F, -6.0F, -3.0F, 0.390625F, 0.046875F}, {-1.0F, -4.0F, -3.0F, 0.390625F, 0.05078125F}, {1.0F, -4.0F, -3.0F, 0.392578125F, 0.05078125F}},
                {{-1.0F, -6.0F, -2.0F, 0.395507812F, 0.046875F}, {1.0F, -6.0F, -2.0F, 0.393554688F, 0.046875F}, {1.0F, -4.0F, -2.0F, 0.393554688F, 0.05078125F}, {-1.0F, -4.0F, -2.0F, 0.395507812F, 0.05078125F}}
        }));
        var p37 = new net.minecraft.client.model.geom.ModelPart(p37Cubes, java.util.Map.ofEntries(java.util.Map.entry("bone12", p38), java.util.Map.entry("bone13", p39)));
        p37.setInitialPose(PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p37.resetPose();
        var p36Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body1/body1/ender/inner_body/arch_illager/body5", java.util.List.of()));
        p36Cubes.add(new ReferenceMeshCube(-3.0F, -10.0F, -2.0F, 6.0F, 7.0F, 4.0F, new float[][][] {
                {{3.0F, -10.0F, 2.0F, 0.384765625F, 0.052734375F}, {-3.0F, -10.0F, 2.0F, 0.37890625F, 0.052734375F}, {-3.0F, -10.0F, -2.0F, 0.37890625F, 0.060546875F}, {3.0F, -10.0F, -2.0F, 0.384765625F, 0.060546875F}},
                {{3.0F, -3.0F, -2.0F, 0.390625F, 0.060546875F}, {-3.0F, -3.0F, -2.0F, 0.384765625F, 0.060546875F}, {-3.0F, -3.0F, 2.0F, 0.384765625F, 0.052734375F}, {3.0F, -3.0F, 2.0F, 0.390625F, 0.052734375F}},
                {{-3.0F, -10.0F, -2.0F, 0.37890625F, 0.060546875F}, {-3.0F, -10.0F, 2.0F, 0.375F, 0.060546875F}, {-3.0F, -3.0F, 2.0F, 0.375F, 0.07421875F}, {-3.0F, -3.0F, -2.0F, 0.37890625F, 0.07421875F}},
                {{3.0F, -10.0F, 2.0F, 0.388671875F, 0.060546875F}, {3.0F, -10.0F, -2.0F, 0.384765625F, 0.060546875F}, {3.0F, -3.0F, -2.0F, 0.384765625F, 0.07421875F}, {3.0F, -3.0F, 2.0F, 0.388671875F, 0.07421875F}},
                {{3.0F, -10.0F, -2.0F, 0.384765625F, 0.060546875F}, {-3.0F, -10.0F, -2.0F, 0.37890625F, 0.060546875F}, {-3.0F, -3.0F, -2.0F, 0.37890625F, 0.07421875F}, {3.0F, -3.0F, -2.0F, 0.384765625F, 0.07421875F}},
                {{-3.0F, -10.0F, 2.0F, 0.39453125F, 0.060546875F}, {3.0F, -10.0F, 2.0F, 0.388671875F, 0.060546875F}, {3.0F, -3.0F, 2.0F, 0.388671875F, 0.07421875F}, {-3.0F, -3.0F, 2.0F, 0.39453125F, 0.07421875F}}
        }));
        p36Cubes.add(new ReferenceMeshCube(-4.05F, -10.55F, -2.05F, 8.1F, 14.1F, 4.1F, new float[][][] {
                {{4.05F, -10.55F, 2.05F, 0.416015625F, 0.037109375F}, {-4.05F, -10.55F, 2.05F, 0.408203125F, 0.037109375F}, {-4.05F, -10.55F, -2.05F, 0.408203125F, 0.044921875F}, {4.05F, -10.55F, -2.05F, 0.416015625F, 0.044921875F}},
                {{4.05F, 3.55F, -2.05F, 0.423828125F, 0.044921875F}, {-4.05F, 3.55F, -2.05F, 0.416015625F, 0.044921875F}, {-4.05F, 3.55F, 2.05F, 0.416015625F, 0.037109375F}, {4.05F, 3.55F, 2.05F, 0.423828125F, 0.037109375F}},
                {{-4.05F, -10.55F, -2.05F, 0.408203125F, 0.044921875F}, {-4.05F, -10.55F, 2.05F, 0.404296875F, 0.044921875F}, {-4.05F, 3.55F, 2.05F, 0.404296875F, 0.072265625F}, {-4.05F, 3.55F, -2.05F, 0.408203125F, 0.072265625F}},
                {{4.05F, -10.55F, 2.05F, 0.419921875F, 0.044921875F}, {4.05F, -10.55F, -2.05F, 0.416015625F, 0.044921875F}, {4.05F, 3.55F, -2.05F, 0.416015625F, 0.072265625F}, {4.05F, 3.55F, 2.05F, 0.419921875F, 0.072265625F}},
                {{4.05F, -10.55F, -2.05F, 0.416015625F, 0.044921875F}, {-4.05F, -10.55F, -2.05F, 0.408203125F, 0.044921875F}, {-4.05F, 3.55F, -2.05F, 0.408203125F, 0.072265625F}, {4.05F, 3.55F, -2.05F, 0.416015625F, 0.072265625F}},
                {{-4.05F, -10.55F, 2.05F, 0.427734375F, 0.044921875F}, {4.05F, -10.55F, 2.05F, 0.419921875F, 0.044921875F}, {4.05F, 3.55F, 2.05F, 0.419921875F, 0.072265625F}, {-4.05F, 3.55F, 2.05F, 0.427734375F, 0.072265625F}}
        }));
        var p36 = new net.minecraft.client.model.geom.ModelPart(p36Cubes, java.util.Map.ofEntries(java.util.Map.entry("necklace", p37), java.util.Map.entry("right_leg2", p41), java.util.Map.entry("left_leg2", p42), java.util.Map.entry("right_arm5", p44), java.util.Map.entry("right_arm6", p45)));
        p36.setInitialPose(PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p36.resetPose();
        var p35Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body1/body1/ender/inner_body/arch_illager/head4", java.util.List.of()));
        p35Cubes.add(new ReferenceMeshCube(-6.0F, -7.0F, -3.0F, 12.0F, 7.0F, 6.0F, new float[][][] {
                {{6.0F, -7.0F, 3.0F, 0.390625F, 0.0078125F}, {-6.0F, -7.0F, 3.0F, 0.37890625F, 0.0078125F}, {-6.0F, -7.0F, -3.0F, 0.37890625F, 0.01953125F}, {6.0F, -7.0F, -3.0F, 0.390625F, 0.01953125F}},
                {{6.0F, 0.0F, -3.0F, 0.40234375F, 0.01953125F}, {-6.0F, 0.0F, -3.0F, 0.390625F, 0.01953125F}, {-6.0F, 0.0F, 3.0F, 0.390625F, 0.0078125F}, {6.0F, 0.0F, 3.0F, 0.40234375F, 0.0078125F}},
                {{-6.0F, -7.0F, -3.0F, 0.37890625F, 0.01953125F}, {-6.0F, -7.0F, 3.0F, 0.373046875F, 0.01953125F}, {-6.0F, 0.0F, 3.0F, 0.373046875F, 0.033203125F}, {-6.0F, 0.0F, -3.0F, 0.37890625F, 0.033203125F}},
                {{6.0F, -7.0F, 3.0F, 0.396484375F, 0.01953125F}, {6.0F, -7.0F, -3.0F, 0.390625F, 0.01953125F}, {6.0F, 0.0F, -3.0F, 0.390625F, 0.033203125F}, {6.0F, 0.0F, 3.0F, 0.396484375F, 0.033203125F}},
                {{6.0F, -7.0F, -3.0F, 0.390625F, 0.01953125F}, {-6.0F, -7.0F, -3.0F, 0.37890625F, 0.01953125F}, {-6.0F, 0.0F, -3.0F, 0.37890625F, 0.033203125F}, {6.0F, 0.0F, -3.0F, 0.390625F, 0.033203125F}},
                {{-6.0F, -7.0F, 3.0F, 0.408203125F, 0.01953125F}, {6.0F, -7.0F, 3.0F, 0.396484375F, 0.01953125F}, {6.0F, 0.0F, 3.0F, 0.396484375F, 0.033203125F}, {-6.0F, 0.0F, 3.0F, 0.408203125F, 0.033203125F}}
        }));
        p35Cubes.add(new ReferenceMeshCube(-2.0F, -18.0F, -2.0F, 4.0F, 11.0F, 4.0F, new float[][][] {
                {{2.0F, -18.0F, 2.0F, 0.416015625F, 0.0078125F}, {-2.0F, -18.0F, 2.0F, 0.412109375F, 0.0078125F}, {-2.0F, -18.0F, -2.0F, 0.412109375F, 0.015625F}, {2.0F, -18.0F, -2.0F, 0.416015625F, 0.015625F}},
                {{2.0F, -7.0F, -2.0F, 0.419921875F, 0.015625F}, {-2.0F, -7.0F, -2.0F, 0.416015625F, 0.015625F}, {-2.0F, -7.0F, 2.0F, 0.416015625F, 0.0078125F}, {2.0F, -7.0F, 2.0F, 0.419921875F, 0.0078125F}},
                {{-2.0F, -18.0F, -2.0F, 0.412109375F, 0.015625F}, {-2.0F, -18.0F, 2.0F, 0.408203125F, 0.015625F}, {-2.0F, -7.0F, 2.0F, 0.408203125F, 0.037109375F}, {-2.0F, -7.0F, -2.0F, 0.412109375F, 0.037109375F}},
                {{2.0F, -18.0F, 2.0F, 0.419921875F, 0.015625F}, {2.0F, -18.0F, -2.0F, 0.416015625F, 0.015625F}, {2.0F, -7.0F, -2.0F, 0.416015625F, 0.037109375F}, {2.0F, -7.0F, 2.0F, 0.419921875F, 0.037109375F}},
                {{2.0F, -18.0F, -2.0F, 0.416015625F, 0.015625F}, {-2.0F, -18.0F, -2.0F, 0.412109375F, 0.015625F}, {-2.0F, -7.0F, -2.0F, 0.412109375F, 0.037109375F}, {2.0F, -7.0F, -2.0F, 0.416015625F, 0.037109375F}},
                {{-2.0F, -18.0F, 2.0F, 0.423828125F, 0.015625F}, {2.0F, -18.0F, 2.0F, 0.419921875F, 0.015625F}, {2.0F, -7.0F, 2.0F, 0.419921875F, 0.037109375F}, {-2.0F, -7.0F, 2.0F, 0.423828125F, 0.037109375F}}
        }));
        p35Cubes.add(new ReferenceMeshCube(-1.0F, -3.0F, -4.0F, 2.0F, 4.0F, 1.0F, new float[][][] {
                {{1.0F, -3.0F, -3.0F, 0.375976562F, 0.033203125F}, {-1.0F, -3.0F, -3.0F, 0.374023438F, 0.033203125F}, {-1.0F, -3.0F, -4.0F, 0.374023438F, 0.03515625F}, {1.0F, -3.0F, -4.0F, 0.375976562F, 0.03515625F}},
                {{1.0F, 1.0F, -4.0F, 0.377929688F, 0.03515625F}, {-1.0F, 1.0F, -4.0F, 0.375976562F, 0.03515625F}, {-1.0F, 1.0F, -3.0F, 0.375976562F, 0.033203125F}, {1.0F, 1.0F, -3.0F, 0.377929688F, 0.033203125F}},
                {{-1.0F, -3.0F, -4.0F, 0.374023438F, 0.03515625F}, {-1.0F, -3.0F, -3.0F, 0.373046875F, 0.03515625F}, {-1.0F, 1.0F, -3.0F, 0.373046875F, 0.04296875F}, {-1.0F, 1.0F, -4.0F, 0.374023438F, 0.04296875F}},
                {{1.0F, -3.0F, -3.0F, 0.376953125F, 0.03515625F}, {1.0F, -3.0F, -4.0F, 0.375976562F, 0.03515625F}, {1.0F, 1.0F, -4.0F, 0.375976562F, 0.04296875F}, {1.0F, 1.0F, -3.0F, 0.376953125F, 0.04296875F}},
                {{1.0F, -3.0F, -4.0F, 0.375976562F, 0.03515625F}, {-1.0F, -3.0F, -4.0F, 0.374023438F, 0.03515625F}, {-1.0F, 1.0F, -4.0F, 0.374023438F, 0.04296875F}, {1.0F, 1.0F, -4.0F, 0.375976562F, 0.04296875F}},
                {{-1.0F, -3.0F, -3.0F, 0.37890625F, 0.03515625F}, {1.0F, -3.0F, -3.0F, 0.376953125F, 0.03515625F}, {1.0F, 1.0F, -3.0F, 0.376953125F, 0.04296875F}, {-1.0F, 1.0F, -3.0F, 0.37890625F, 0.04296875F}}
        }));
        var p35 = new net.minecraft.client.model.geom.ModelPart(p35Cubes, java.util.Map.ofEntries());
        p35.setInitialPose(PartPose.offsetAndRotation(0.0F, -10.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p35.resetPose();
        var p34Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body1/body1/ender/inner_body/arch_illager", java.util.List.of()));
        var p34 = new net.minecraft.client.model.geom.ModelPart(p34Cubes, java.util.Map.ofEntries(java.util.Map.entry("head4", p35), java.util.Map.entry("body5", p36)));
        p34.setInitialPose(PartPose.offsetAndRotation(-10.0F, 54.0F, 11.5F, 0.0F, 0.0F, 0.0F)); p34.resetPose();
        var p33Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body1/body1/ender/inner_body/right_arm3/arm11/arm12/body1_sub_69", java.util.List.of()));
        p33Cubes.add(new ReferenceMeshCube(-35.5F, -5.0F, -5.0F, 35.0F, 10.0F, 10.0F, new float[][][] {
                {{-35.5F, -5.0F, -5.0F, 0.0478515625F, 0.27734375F}, {-0.5F, -5.0F, -5.0F, 0.013671875F, 0.27734375F}, {-0.5F, -5.0F, 5.0F, 0.013671875F, 0.2578125F}, {-35.5F, -5.0F, 5.0F, 0.0478515625F, 0.2578125F}},
                {{-35.5F, 5.0F, 5.0F, 0.08203125F, 0.2578125F}, {-0.5F, 5.0F, 5.0F, 0.0478515625F, 0.2578125F}, {-0.5F, 5.0F, -5.0F, 0.0478515625F, 0.27734375F}, {-35.5F, 5.0F, -5.0F, 0.08203125F, 0.27734375F}},
                {{-0.5F, 5.0F, -5.0F, 0.013671875F, 0.296875F}, {-0.5F, 5.0F, 5.0F, 0.00390625F, 0.296875F}, {-0.5F, -5.0F, 5.0F, 0.00390625F, 0.27734375F}, {-0.5F, -5.0F, -5.0F, 0.013671875F, 0.27734375F}},
                {{-35.5F, 5.0F, 5.0F, 0.0576171875F, 0.296875F}, {-35.5F, 5.0F, -5.0F, 0.0478515625F, 0.296875F}, {-35.5F, -5.0F, -5.0F, 0.0478515625F, 0.27734375F}, {-35.5F, -5.0F, 5.0F, 0.0576171875F, 0.27734375F}},
                {{-35.5F, 5.0F, -5.0F, 0.0478515625F, 0.296875F}, {-0.5F, 5.0F, -5.0F, 0.013671875F, 0.296875F}, {-0.5F, -5.0F, -5.0F, 0.013671875F, 0.27734375F}, {-35.5F, -5.0F, -5.0F, 0.0478515625F, 0.27734375F}},
                {{-0.5F, 5.0F, 5.0F, 0.091796875F, 0.296875F}, {-35.5F, 5.0F, 5.0F, 0.0576171875F, 0.296875F}, {-35.5F, -5.0F, 5.0F, 0.0576171875F, 0.27734375F}, {-0.5F, -5.0F, 5.0F, 0.091796875F, 0.27734375F}}
        }));
        var p33 = new net.minecraft.client.model.geom.ModelPart(p33Cubes, java.util.Map.ofEntries());
        p33.setInitialPose(PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p33.resetPose();
        var p32Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body1/body1/ender/inner_body/right_arm3/arm11/arm12", java.util.List.of()));
        var p32 = new net.minecraft.client.model.geom.ModelPart(p32Cubes, java.util.Map.ofEntries(java.util.Map.entry("body1_sub_69", p33)));
        p32.setInitialPose(PartPose.offsetAndRotation(-35.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p32.resetPose();
        var p31Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body1/body1/ender/inner_body/right_arm3/arm11/body1_sub_67", java.util.List.of()));
        p31Cubes.add(new ReferenceMeshCube(-35.5F, -5.0F, -5.0F, 35.0F, 10.0F, 10.0F, new float[][][] {
                {{-35.5F, -5.0F, -5.0F, 0.0478515625F, 0.27734375F}, {-0.5F, -5.0F, -5.0F, 0.013671875F, 0.27734375F}, {-0.5F, -5.0F, 5.0F, 0.013671875F, 0.2578125F}, {-35.5F, -5.0F, 5.0F, 0.0478515625F, 0.2578125F}},
                {{-35.5F, 5.0F, 5.0F, 0.08203125F, 0.2578125F}, {-0.5F, 5.0F, 5.0F, 0.0478515625F, 0.2578125F}, {-0.5F, 5.0F, -5.0F, 0.0478515625F, 0.27734375F}, {-35.5F, 5.0F, -5.0F, 0.08203125F, 0.27734375F}},
                {{-0.5F, 5.0F, -5.0F, 0.013671875F, 0.296875F}, {-0.5F, 5.0F, 5.0F, 0.00390625F, 0.296875F}, {-0.5F, -5.0F, 5.0F, 0.00390625F, 0.27734375F}, {-0.5F, -5.0F, -5.0F, 0.013671875F, 0.27734375F}},
                {{-35.5F, 5.0F, 5.0F, 0.0576171875F, 0.296875F}, {-35.5F, 5.0F, -5.0F, 0.0478515625F, 0.296875F}, {-35.5F, -5.0F, -5.0F, 0.0478515625F, 0.27734375F}, {-35.5F, -5.0F, 5.0F, 0.0576171875F, 0.27734375F}},
                {{-35.5F, 5.0F, -5.0F, 0.0478515625F, 0.296875F}, {-0.5F, 5.0F, -5.0F, 0.013671875F, 0.296875F}, {-0.5F, -5.0F, -5.0F, 0.013671875F, 0.27734375F}, {-35.5F, -5.0F, -5.0F, 0.0478515625F, 0.27734375F}},
                {{-0.5F, 5.0F, 5.0F, 0.091796875F, 0.296875F}, {-35.5F, 5.0F, 5.0F, 0.0576171875F, 0.296875F}, {-35.5F, -5.0F, 5.0F, 0.0576171875F, 0.27734375F}, {-0.5F, -5.0F, 5.0F, 0.091796875F, 0.27734375F}}
        }));
        var p31 = new net.minecraft.client.model.geom.ModelPart(p31Cubes, java.util.Map.ofEntries());
        p31.setInitialPose(PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p31.resetPose();
        var p30Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body1/body1/ender/inner_body/right_arm3/arm11", java.util.List.of()));
        var p30 = new net.minecraft.client.model.geom.ModelPart(p30Cubes, java.util.Map.ofEntries(java.util.Map.entry("body1_sub_67", p31), java.util.Map.entry("arm12", p32)));
        p30.setInitialPose(PartPose.offsetAndRotation(0.0F, 25.0F, 11.0F, 0.0F, 0.0F, 0.0F)); p30.resetPose();
        var p29Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body1/body1/ender/inner_body/right_arm3", java.util.List.of()));
        var p29 = new net.minecraft.client.model.geom.ModelPart(p29Cubes, java.util.Map.ofEntries(java.util.Map.entry("arm11", p30)));
        p29.setInitialPose(PartPose.offsetAndRotation(-20.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p29.resetPose();
        var p28Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body1/body1/ender/inner_body/left_arm3/arm5/arm6", java.util.List.of()));
        p28Cubes.add(new ReferenceMeshCube(0.5F, -5.0F, -5.0F, 35.0F, 10.0F, 10.0F, new float[][][] {
                {{35.5F, -5.0F, 5.0F, 0.0478515625F, 0.2578125F}, {0.5F, -5.0F, 5.0F, 0.013671875F, 0.2578125F}, {0.5F, -5.0F, -5.0F, 0.013671875F, 0.27734375F}, {35.5F, -5.0F, -5.0F, 0.0478515625F, 0.27734375F}},
                {{35.5F, 5.0F, -5.0F, 0.08203125F, 0.27734375F}, {0.5F, 5.0F, -5.0F, 0.0478515625F, 0.27734375F}, {0.5F, 5.0F, 5.0F, 0.0478515625F, 0.2578125F}, {35.5F, 5.0F, 5.0F, 0.08203125F, 0.2578125F}},
                {{0.5F, -5.0F, -5.0F, 0.013671875F, 0.27734375F}, {0.5F, -5.0F, 5.0F, 0.00390625F, 0.27734375F}, {0.5F, 5.0F, 5.0F, 0.00390625F, 0.296875F}, {0.5F, 5.0F, -5.0F, 0.013671875F, 0.296875F}},
                {{35.5F, -5.0F, 5.0F, 0.0576171875F, 0.27734375F}, {35.5F, -5.0F, -5.0F, 0.0478515625F, 0.27734375F}, {35.5F, 5.0F, -5.0F, 0.0478515625F, 0.296875F}, {35.5F, 5.0F, 5.0F, 0.0576171875F, 0.296875F}},
                {{35.5F, -5.0F, -5.0F, 0.0478515625F, 0.27734375F}, {0.5F, -5.0F, -5.0F, 0.013671875F, 0.27734375F}, {0.5F, 5.0F, -5.0F, 0.013671875F, 0.296875F}, {35.5F, 5.0F, -5.0F, 0.0478515625F, 0.296875F}},
                {{0.5F, -5.0F, 5.0F, 0.091796875F, 0.27734375F}, {35.5F, -5.0F, 5.0F, 0.0576171875F, 0.27734375F}, {35.5F, 5.0F, 5.0F, 0.0576171875F, 0.296875F}, {0.5F, 5.0F, 5.0F, 0.091796875F, 0.296875F}}
        }));
        var p28 = new net.minecraft.client.model.geom.ModelPart(p28Cubes, java.util.Map.ofEntries());
        p28.setInitialPose(PartPose.offsetAndRotation(35.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p28.resetPose();
        var p27Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body1/body1/ender/inner_body/left_arm3/arm5", java.util.List.of()));
        p27Cubes.add(new ReferenceMeshCube(0.5F, -5.0F, -5.0F, 35.0F, 10.0F, 10.0F, new float[][][] {
                {{35.5F, -5.0F, 5.0F, 0.0478515625F, 0.2578125F}, {0.5F, -5.0F, 5.0F, 0.013671875F, 0.2578125F}, {0.5F, -5.0F, -5.0F, 0.013671875F, 0.27734375F}, {35.5F, -5.0F, -5.0F, 0.0478515625F, 0.27734375F}},
                {{35.5F, 5.0F, -5.0F, 0.08203125F, 0.27734375F}, {0.5F, 5.0F, -5.0F, 0.0478515625F, 0.27734375F}, {0.5F, 5.0F, 5.0F, 0.0478515625F, 0.2578125F}, {35.5F, 5.0F, 5.0F, 0.08203125F, 0.2578125F}},
                {{0.5F, -5.0F, -5.0F, 0.013671875F, 0.27734375F}, {0.5F, -5.0F, 5.0F, 0.00390625F, 0.27734375F}, {0.5F, 5.0F, 5.0F, 0.00390625F, 0.296875F}, {0.5F, 5.0F, -5.0F, 0.013671875F, 0.296875F}},
                {{35.5F, -5.0F, 5.0F, 0.0576171875F, 0.27734375F}, {35.5F, -5.0F, -5.0F, 0.0478515625F, 0.27734375F}, {35.5F, 5.0F, -5.0F, 0.0478515625F, 0.296875F}, {35.5F, 5.0F, 5.0F, 0.0576171875F, 0.296875F}},
                {{35.5F, -5.0F, -5.0F, 0.0478515625F, 0.27734375F}, {0.5F, -5.0F, -5.0F, 0.013671875F, 0.27734375F}, {0.5F, 5.0F, -5.0F, 0.013671875F, 0.296875F}, {35.5F, 5.0F, -5.0F, 0.0478515625F, 0.296875F}},
                {{0.5F, -5.0F, 5.0F, 0.091796875F, 0.27734375F}, {35.5F, -5.0F, 5.0F, 0.0576171875F, 0.27734375F}, {35.5F, 5.0F, 5.0F, 0.0576171875F, 0.296875F}, {0.5F, 5.0F, 5.0F, 0.091796875F, 0.296875F}}
        }));
        var p27 = new net.minecraft.client.model.geom.ModelPart(p27Cubes, java.util.Map.ofEntries(java.util.Map.entry("arm6", p28)));
        p27.setInitialPose(PartPose.offsetAndRotation(0.0F, 25.0F, 11.0F, 0.0F, 0.0F, 0.0F)); p27.resetPose();
        var p26Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body1/body1/ender/inner_body/left_arm3", java.util.List.of()));
        var p26 = new net.minecraft.client.model.geom.ModelPart(p26Cubes, java.util.Map.ofEntries(java.util.Map.entry("arm5", p27)));
        p26.setInitialPose(PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p26.resetPose();
        var p25Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body1/body1/ender/inner_body/top_body/ender2/right_arm4/arm21/arm22/body1_sub_6", java.util.List.of()));
        p25Cubes.add(new ReferenceMeshCube(-35.5F, -5.0F, -5.0F, 35.0F, 10.0F, 10.0F, new float[][][] {
                {{-35.5F, -5.0F, -5.0F, 0.0478515625F, 0.27734375F}, {-0.5F, -5.0F, -5.0F, 0.013671875F, 0.27734375F}, {-0.5F, -5.0F, 5.0F, 0.013671875F, 0.2578125F}, {-35.5F, -5.0F, 5.0F, 0.0478515625F, 0.2578125F}},
                {{-35.5F, 5.0F, 5.0F, 0.08203125F, 0.2578125F}, {-0.5F, 5.0F, 5.0F, 0.0478515625F, 0.2578125F}, {-0.5F, 5.0F, -5.0F, 0.0478515625F, 0.27734375F}, {-35.5F, 5.0F, -5.0F, 0.08203125F, 0.27734375F}},
                {{-0.5F, 5.0F, -5.0F, 0.013671875F, 0.296875F}, {-0.5F, 5.0F, 5.0F, 0.00390625F, 0.296875F}, {-0.5F, -5.0F, 5.0F, 0.00390625F, 0.27734375F}, {-0.5F, -5.0F, -5.0F, 0.013671875F, 0.27734375F}},
                {{-35.5F, 5.0F, 5.0F, 0.0576171875F, 0.296875F}, {-35.5F, 5.0F, -5.0F, 0.0478515625F, 0.296875F}, {-35.5F, -5.0F, -5.0F, 0.0478515625F, 0.27734375F}, {-35.5F, -5.0F, 5.0F, 0.0576171875F, 0.27734375F}},
                {{-35.5F, 5.0F, -5.0F, 0.0478515625F, 0.296875F}, {-0.5F, 5.0F, -5.0F, 0.013671875F, 0.296875F}, {-0.5F, -5.0F, -5.0F, 0.013671875F, 0.27734375F}, {-35.5F, -5.0F, -5.0F, 0.0478515625F, 0.27734375F}},
                {{-0.5F, 5.0F, 5.0F, 0.091796875F, 0.296875F}, {-35.5F, 5.0F, 5.0F, 0.0576171875F, 0.296875F}, {-35.5F, -5.0F, 5.0F, 0.0576171875F, 0.27734375F}, {-0.5F, -5.0F, 5.0F, 0.091796875F, 0.27734375F}}
        }));
        var p25 = new net.minecraft.client.model.geom.ModelPart(p25Cubes, java.util.Map.ofEntries());
        p25.setInitialPose(PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p25.resetPose();
        var p24Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body1/body1/ender/inner_body/top_body/ender2/right_arm4/arm21/arm22", java.util.List.of()));
        var p24 = new net.minecraft.client.model.geom.ModelPart(p24Cubes, java.util.Map.ofEntries(java.util.Map.entry("body1_sub_6", p25)));
        p24.setInitialPose(PartPose.offsetAndRotation(-35.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p24.resetPose();
        var p23Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body1/body1/ender/inner_body/top_body/ender2/right_arm4/arm21/body1_sub_5", java.util.List.of()));
        p23Cubes.add(new ReferenceMeshCube(-35.5F, -5.0F, -5.0F, 35.0F, 10.0F, 10.0F, new float[][][] {
                {{-35.5F, -5.0F, -5.0F, 0.0478515625F, 0.27734375F}, {-0.5F, -5.0F, -5.0F, 0.013671875F, 0.27734375F}, {-0.5F, -5.0F, 5.0F, 0.013671875F, 0.2578125F}, {-35.5F, -5.0F, 5.0F, 0.0478515625F, 0.2578125F}},
                {{-35.5F, 5.0F, 5.0F, 0.08203125F, 0.2578125F}, {-0.5F, 5.0F, 5.0F, 0.0478515625F, 0.2578125F}, {-0.5F, 5.0F, -5.0F, 0.0478515625F, 0.27734375F}, {-35.5F, 5.0F, -5.0F, 0.08203125F, 0.27734375F}},
                {{-0.5F, 5.0F, -5.0F, 0.013671875F, 0.296875F}, {-0.5F, 5.0F, 5.0F, 0.00390625F, 0.296875F}, {-0.5F, -5.0F, 5.0F, 0.00390625F, 0.27734375F}, {-0.5F, -5.0F, -5.0F, 0.013671875F, 0.27734375F}},
                {{-35.5F, 5.0F, 5.0F, 0.0576171875F, 0.296875F}, {-35.5F, 5.0F, -5.0F, 0.0478515625F, 0.296875F}, {-35.5F, -5.0F, -5.0F, 0.0478515625F, 0.27734375F}, {-35.5F, -5.0F, 5.0F, 0.0576171875F, 0.27734375F}},
                {{-35.5F, 5.0F, -5.0F, 0.0478515625F, 0.296875F}, {-0.5F, 5.0F, -5.0F, 0.013671875F, 0.296875F}, {-0.5F, -5.0F, -5.0F, 0.013671875F, 0.27734375F}, {-35.5F, -5.0F, -5.0F, 0.0478515625F, 0.27734375F}},
                {{-0.5F, 5.0F, 5.0F, 0.091796875F, 0.296875F}, {-35.5F, 5.0F, 5.0F, 0.0576171875F, 0.296875F}, {-35.5F, -5.0F, 5.0F, 0.0576171875F, 0.27734375F}, {-0.5F, -5.0F, 5.0F, 0.091796875F, 0.27734375F}}
        }));
        var p23 = new net.minecraft.client.model.geom.ModelPart(p23Cubes, java.util.Map.ofEntries());
        p23.setInitialPose(PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p23.resetPose();
        var p22Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body1/body1/ender/inner_body/top_body/ender2/right_arm4/arm21", java.util.List.of()));
        var p22 = new net.minecraft.client.model.geom.ModelPart(p22Cubes, java.util.Map.ofEntries(java.util.Map.entry("body1_sub_5", p23), java.util.Map.entry("arm22", p24)));
        p22.setInitialPose(PartPose.offsetAndRotation(0.0F, 7.0F, 11.0F, 0.0F, 0.0F, 0.0F)); p22.resetPose();
        var p21Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body1/body1/ender/inner_body/top_body/ender2/right_arm4/arm19/arm20/body1_sub_4", java.util.List.of()));
        p21Cubes.add(new ReferenceMeshCube(-35.5F, -5.0F, -5.0F, 35.0F, 10.0F, 10.0F, new float[][][] {
                {{-35.5F, -5.0F, -5.0F, 0.0478515625F, 0.27734375F}, {-0.5F, -5.0F, -5.0F, 0.013671875F, 0.27734375F}, {-0.5F, -5.0F, 5.0F, 0.013671875F, 0.2578125F}, {-35.5F, -5.0F, 5.0F, 0.0478515625F, 0.2578125F}},
                {{-35.5F, 5.0F, 5.0F, 0.08203125F, 0.2578125F}, {-0.5F, 5.0F, 5.0F, 0.0478515625F, 0.2578125F}, {-0.5F, 5.0F, -5.0F, 0.0478515625F, 0.27734375F}, {-35.5F, 5.0F, -5.0F, 0.08203125F, 0.27734375F}},
                {{-0.5F, 5.0F, -5.0F, 0.013671875F, 0.296875F}, {-0.5F, 5.0F, 5.0F, 0.00390625F, 0.296875F}, {-0.5F, -5.0F, 5.0F, 0.00390625F, 0.27734375F}, {-0.5F, -5.0F, -5.0F, 0.013671875F, 0.27734375F}},
                {{-35.5F, 5.0F, 5.0F, 0.0576171875F, 0.296875F}, {-35.5F, 5.0F, -5.0F, 0.0478515625F, 0.296875F}, {-35.5F, -5.0F, -5.0F, 0.0478515625F, 0.27734375F}, {-35.5F, -5.0F, 5.0F, 0.0576171875F, 0.27734375F}},
                {{-35.5F, 5.0F, -5.0F, 0.0478515625F, 0.296875F}, {-0.5F, 5.0F, -5.0F, 0.013671875F, 0.296875F}, {-0.5F, -5.0F, -5.0F, 0.013671875F, 0.27734375F}, {-35.5F, -5.0F, -5.0F, 0.0478515625F, 0.27734375F}},
                {{-0.5F, 5.0F, 5.0F, 0.091796875F, 0.296875F}, {-35.5F, 5.0F, 5.0F, 0.0576171875F, 0.296875F}, {-35.5F, -5.0F, 5.0F, 0.0576171875F, 0.27734375F}, {-0.5F, -5.0F, 5.0F, 0.091796875F, 0.27734375F}}
        }));
        var p21 = new net.minecraft.client.model.geom.ModelPart(p21Cubes, java.util.Map.ofEntries());
        p21.setInitialPose(PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p21.resetPose();
        var p20Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body1/body1/ender/inner_body/top_body/ender2/right_arm4/arm19/arm20", java.util.List.of()));
        var p20 = new net.minecraft.client.model.geom.ModelPart(p20Cubes, java.util.Map.ofEntries(java.util.Map.entry("body1_sub_4", p21)));
        p20.setInitialPose(PartPose.offsetAndRotation(-35.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p20.resetPose();
        var p19Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body1/body1/ender/inner_body/top_body/ender2/right_arm4/arm19/body1_sub_3", java.util.List.of()));
        p19Cubes.add(new ReferenceMeshCube(-35.5F, -5.0F, -5.0F, 35.0F, 10.0F, 10.0F, new float[][][] {
                {{-35.5F, -5.0F, -5.0F, 0.0478515625F, 0.27734375F}, {-0.5F, -5.0F, -5.0F, 0.013671875F, 0.27734375F}, {-0.5F, -5.0F, 5.0F, 0.013671875F, 0.2578125F}, {-35.5F, -5.0F, 5.0F, 0.0478515625F, 0.2578125F}},
                {{-35.5F, 5.0F, 5.0F, 0.08203125F, 0.2578125F}, {-0.5F, 5.0F, 5.0F, 0.0478515625F, 0.2578125F}, {-0.5F, 5.0F, -5.0F, 0.0478515625F, 0.27734375F}, {-35.5F, 5.0F, -5.0F, 0.08203125F, 0.27734375F}},
                {{-0.5F, 5.0F, -5.0F, 0.013671875F, 0.296875F}, {-0.5F, 5.0F, 5.0F, 0.00390625F, 0.296875F}, {-0.5F, -5.0F, 5.0F, 0.00390625F, 0.27734375F}, {-0.5F, -5.0F, -5.0F, 0.013671875F, 0.27734375F}},
                {{-35.5F, 5.0F, 5.0F, 0.0576171875F, 0.296875F}, {-35.5F, 5.0F, -5.0F, 0.0478515625F, 0.296875F}, {-35.5F, -5.0F, -5.0F, 0.0478515625F, 0.27734375F}, {-35.5F, -5.0F, 5.0F, 0.0576171875F, 0.27734375F}},
                {{-35.5F, 5.0F, -5.0F, 0.0478515625F, 0.296875F}, {-0.5F, 5.0F, -5.0F, 0.013671875F, 0.296875F}, {-0.5F, -5.0F, -5.0F, 0.013671875F, 0.27734375F}, {-35.5F, -5.0F, -5.0F, 0.0478515625F, 0.27734375F}},
                {{-0.5F, 5.0F, 5.0F, 0.091796875F, 0.296875F}, {-35.5F, 5.0F, 5.0F, 0.0576171875F, 0.296875F}, {-35.5F, -5.0F, 5.0F, 0.0576171875F, 0.27734375F}, {-0.5F, -5.0F, 5.0F, 0.091796875F, 0.27734375F}}
        }));
        var p19 = new net.minecraft.client.model.geom.ModelPart(p19Cubes, java.util.Map.ofEntries());
        p19.setInitialPose(PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p19.resetPose();
        var p18Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body1/body1/ender/inner_body/top_body/ender2/right_arm4/arm19", java.util.List.of()));
        var p18 = new net.minecraft.client.model.geom.ModelPart(p18Cubes, java.util.Map.ofEntries(java.util.Map.entry("body1_sub_3", p19), java.util.Map.entry("arm20", p20)));
        p18.setInitialPose(PartPose.offsetAndRotation(0.0F, -11.0F, 11.0F, 0.0F, 0.0F, 0.0F)); p18.resetPose();
        var p17Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body1/body1/ender/inner_body/top_body/ender2/right_arm4", java.util.List.of()));
        var p17 = new net.minecraft.client.model.geom.ModelPart(p17Cubes, java.util.Map.ofEntries(java.util.Map.entry("arm19", p18), java.util.Map.entry("arm21", p22)));
        p17.setInitialPose(PartPose.offsetAndRotation(-10.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p17.resetPose();
        var p16Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body1/body1/ender/inner_body/top_body/ender2/left_arm4/arm15/arm16", java.util.List.of()));
        p16Cubes.add(new ReferenceMeshCube(0.5F, -5.0F, -5.0F, 35.0F, 10.0F, 10.0F, new float[][][] {
                {{35.5F, -5.0F, 5.0F, 0.0478515625F, 0.2578125F}, {0.5F, -5.0F, 5.0F, 0.013671875F, 0.2578125F}, {0.5F, -5.0F, -5.0F, 0.013671875F, 0.27734375F}, {35.5F, -5.0F, -5.0F, 0.0478515625F, 0.27734375F}},
                {{35.5F, 5.0F, -5.0F, 0.08203125F, 0.27734375F}, {0.5F, 5.0F, -5.0F, 0.0478515625F, 0.27734375F}, {0.5F, 5.0F, 5.0F, 0.0478515625F, 0.2578125F}, {35.5F, 5.0F, 5.0F, 0.08203125F, 0.2578125F}},
                {{0.5F, -5.0F, -5.0F, 0.013671875F, 0.27734375F}, {0.5F, -5.0F, 5.0F, 0.00390625F, 0.27734375F}, {0.5F, 5.0F, 5.0F, 0.00390625F, 0.296875F}, {0.5F, 5.0F, -5.0F, 0.013671875F, 0.296875F}},
                {{35.5F, -5.0F, 5.0F, 0.0576171875F, 0.27734375F}, {35.5F, -5.0F, -5.0F, 0.0478515625F, 0.27734375F}, {35.5F, 5.0F, -5.0F, 0.0478515625F, 0.296875F}, {35.5F, 5.0F, 5.0F, 0.0576171875F, 0.296875F}},
                {{35.5F, -5.0F, -5.0F, 0.0478515625F, 0.27734375F}, {0.5F, -5.0F, -5.0F, 0.013671875F, 0.27734375F}, {0.5F, 5.0F, -5.0F, 0.013671875F, 0.296875F}, {35.5F, 5.0F, -5.0F, 0.0478515625F, 0.296875F}},
                {{0.5F, -5.0F, 5.0F, 0.091796875F, 0.27734375F}, {35.5F, -5.0F, 5.0F, 0.0576171875F, 0.27734375F}, {35.5F, 5.0F, 5.0F, 0.0576171875F, 0.296875F}, {0.5F, 5.0F, 5.0F, 0.091796875F, 0.296875F}}
        }));
        var p16 = new net.minecraft.client.model.geom.ModelPart(p16Cubes, java.util.Map.ofEntries());
        p16.setInitialPose(PartPose.offsetAndRotation(35.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p16.resetPose();
        var p15Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body1/body1/ender/inner_body/top_body/ender2/left_arm4/arm15", java.util.List.of()));
        p15Cubes.add(new ReferenceMeshCube(0.5F, -5.0F, -5.0F, 35.0F, 10.0F, 10.0F, new float[][][] {
                {{35.5F, -5.0F, 5.0F, 0.0478515625F, 0.2578125F}, {0.5F, -5.0F, 5.0F, 0.013671875F, 0.2578125F}, {0.5F, -5.0F, -5.0F, 0.013671875F, 0.27734375F}, {35.5F, -5.0F, -5.0F, 0.0478515625F, 0.27734375F}},
                {{35.5F, 5.0F, -5.0F, 0.08203125F, 0.27734375F}, {0.5F, 5.0F, -5.0F, 0.0478515625F, 0.27734375F}, {0.5F, 5.0F, 5.0F, 0.0478515625F, 0.2578125F}, {35.5F, 5.0F, 5.0F, 0.08203125F, 0.2578125F}},
                {{0.5F, -5.0F, -5.0F, 0.013671875F, 0.27734375F}, {0.5F, -5.0F, 5.0F, 0.00390625F, 0.27734375F}, {0.5F, 5.0F, 5.0F, 0.00390625F, 0.296875F}, {0.5F, 5.0F, -5.0F, 0.013671875F, 0.296875F}},
                {{35.5F, -5.0F, 5.0F, 0.0576171875F, 0.27734375F}, {35.5F, -5.0F, -5.0F, 0.0478515625F, 0.27734375F}, {35.5F, 5.0F, -5.0F, 0.0478515625F, 0.296875F}, {35.5F, 5.0F, 5.0F, 0.0576171875F, 0.296875F}},
                {{35.5F, -5.0F, -5.0F, 0.0478515625F, 0.27734375F}, {0.5F, -5.0F, -5.0F, 0.013671875F, 0.27734375F}, {0.5F, 5.0F, -5.0F, 0.013671875F, 0.296875F}, {35.5F, 5.0F, -5.0F, 0.0478515625F, 0.296875F}},
                {{0.5F, -5.0F, 5.0F, 0.091796875F, 0.27734375F}, {35.5F, -5.0F, 5.0F, 0.0576171875F, 0.27734375F}, {35.5F, 5.0F, 5.0F, 0.0576171875F, 0.296875F}, {0.5F, 5.0F, 5.0F, 0.091796875F, 0.296875F}}
        }));
        var p15 = new net.minecraft.client.model.geom.ModelPart(p15Cubes, java.util.Map.ofEntries(java.util.Map.entry("arm16", p16)));
        p15.setInitialPose(PartPose.offsetAndRotation(0.0F, 7.0F, 11.0F, 0.0F, 0.0F, 0.0F)); p15.resetPose();
        var p14Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body1/body1/ender/inner_body/top_body/ender2/left_arm4/arm13/arm14", java.util.List.of()));
        p14Cubes.add(new ReferenceMeshCube(0.5F, -5.0F, -5.0F, 35.0F, 10.0F, 10.0F, new float[][][] {
                {{35.5F, -5.0F, 5.0F, 0.0478515625F, 0.2578125F}, {0.5F, -5.0F, 5.0F, 0.013671875F, 0.2578125F}, {0.5F, -5.0F, -5.0F, 0.013671875F, 0.27734375F}, {35.5F, -5.0F, -5.0F, 0.0478515625F, 0.27734375F}},
                {{35.5F, 5.0F, -5.0F, 0.08203125F, 0.27734375F}, {0.5F, 5.0F, -5.0F, 0.0478515625F, 0.27734375F}, {0.5F, 5.0F, 5.0F, 0.0478515625F, 0.2578125F}, {35.5F, 5.0F, 5.0F, 0.08203125F, 0.2578125F}},
                {{0.5F, -5.0F, -5.0F, 0.013671875F, 0.27734375F}, {0.5F, -5.0F, 5.0F, 0.00390625F, 0.27734375F}, {0.5F, 5.0F, 5.0F, 0.00390625F, 0.296875F}, {0.5F, 5.0F, -5.0F, 0.013671875F, 0.296875F}},
                {{35.5F, -5.0F, 5.0F, 0.0576171875F, 0.27734375F}, {35.5F, -5.0F, -5.0F, 0.0478515625F, 0.27734375F}, {35.5F, 5.0F, -5.0F, 0.0478515625F, 0.296875F}, {35.5F, 5.0F, 5.0F, 0.0576171875F, 0.296875F}},
                {{35.5F, -5.0F, -5.0F, 0.0478515625F, 0.27734375F}, {0.5F, -5.0F, -5.0F, 0.013671875F, 0.27734375F}, {0.5F, 5.0F, -5.0F, 0.013671875F, 0.296875F}, {35.5F, 5.0F, -5.0F, 0.0478515625F, 0.296875F}},
                {{0.5F, -5.0F, 5.0F, 0.091796875F, 0.27734375F}, {35.5F, -5.0F, 5.0F, 0.0576171875F, 0.27734375F}, {35.5F, 5.0F, 5.0F, 0.0576171875F, 0.296875F}, {0.5F, 5.0F, 5.0F, 0.091796875F, 0.296875F}}
        }));
        var p14 = new net.minecraft.client.model.geom.ModelPart(p14Cubes, java.util.Map.ofEntries());
        p14.setInitialPose(PartPose.offsetAndRotation(35.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p14.resetPose();
        var p13Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body1/body1/ender/inner_body/top_body/ender2/left_arm4/arm13", java.util.List.of()));
        p13Cubes.add(new ReferenceMeshCube(0.5F, -5.0F, -5.0F, 35.0F, 10.0F, 10.0F, new float[][][] {
                {{35.5F, -5.0F, 5.0F, 0.0478515625F, 0.2578125F}, {0.5F, -5.0F, 5.0F, 0.013671875F, 0.2578125F}, {0.5F, -5.0F, -5.0F, 0.013671875F, 0.27734375F}, {35.5F, -5.0F, -5.0F, 0.0478515625F, 0.27734375F}},
                {{35.5F, 5.0F, -5.0F, 0.08203125F, 0.27734375F}, {0.5F, 5.0F, -5.0F, 0.0478515625F, 0.27734375F}, {0.5F, 5.0F, 5.0F, 0.0478515625F, 0.2578125F}, {35.5F, 5.0F, 5.0F, 0.08203125F, 0.2578125F}},
                {{0.5F, -5.0F, -5.0F, 0.013671875F, 0.27734375F}, {0.5F, -5.0F, 5.0F, 0.00390625F, 0.27734375F}, {0.5F, 5.0F, 5.0F, 0.00390625F, 0.296875F}, {0.5F, 5.0F, -5.0F, 0.013671875F, 0.296875F}},
                {{35.5F, -5.0F, 5.0F, 0.0576171875F, 0.27734375F}, {35.5F, -5.0F, -5.0F, 0.0478515625F, 0.27734375F}, {35.5F, 5.0F, -5.0F, 0.0478515625F, 0.296875F}, {35.5F, 5.0F, 5.0F, 0.0576171875F, 0.296875F}},
                {{35.5F, -5.0F, -5.0F, 0.0478515625F, 0.27734375F}, {0.5F, -5.0F, -5.0F, 0.013671875F, 0.27734375F}, {0.5F, 5.0F, -5.0F, 0.013671875F, 0.296875F}, {35.5F, 5.0F, -5.0F, 0.0478515625F, 0.296875F}},
                {{0.5F, -5.0F, 5.0F, 0.091796875F, 0.27734375F}, {35.5F, -5.0F, 5.0F, 0.0576171875F, 0.27734375F}, {35.5F, 5.0F, 5.0F, 0.0576171875F, 0.296875F}, {0.5F, 5.0F, 5.0F, 0.091796875F, 0.296875F}}
        }));
        var p13 = new net.minecraft.client.model.geom.ModelPart(p13Cubes, java.util.Map.ofEntries(java.util.Map.entry("arm14", p14)));
        p13.setInitialPose(PartPose.offsetAndRotation(0.0F, -11.0F, 11.0F, 0.0F, 0.0F, 0.0F)); p13.resetPose();
        var p12Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body1/body1/ender/inner_body/top_body/ender2/left_arm4", java.util.List.of()));
        var p12 = new net.minecraft.client.model.geom.ModelPart(p12Cubes, java.util.Map.ofEntries(java.util.Map.entry("arm13", p13), java.util.Map.entry("arm15", p15)));
        p12.setInitialPose(PartPose.offsetAndRotation(10.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p12.resetPose();
        var p11Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body1/body1/ender/inner_body/top_body/ender2/head5", java.util.List.of()));
        p11Cubes.add(new ReferenceMeshCube(-11.5F, -27.0F, -11.0F, 23.0F, 23.0F, 22.0F, new float[][][] {
                {{11.5F, -27.0F, 11.0F, 0.0478515625F, 0.0078125F}, {-11.5F, -27.0F, 11.0F, 0.025390625F, 0.0078125F}, {-11.5F, -27.0F, -11.0F, 0.025390625F, 0.05078125F}, {11.5F, -27.0F, -11.0F, 0.0478515625F, 0.05078125F}},
                {{11.5F, -4.0F, -11.0F, 0.0703125F, 0.05078125F}, {-11.5F, -4.0F, -11.0F, 0.0478515625F, 0.05078125F}, {-11.5F, -4.0F, 11.0F, 0.0478515625F, 0.0078125F}, {11.5F, -4.0F, 11.0F, 0.0703125F, 0.0078125F}},
                {{-11.5F, -27.0F, -11.0F, 0.025390625F, 0.05078125F}, {-11.5F, -27.0F, 11.0F, 0.00390625F, 0.05078125F}, {-11.5F, -4.0F, 11.0F, 0.00390625F, 0.095703125F}, {-11.5F, -4.0F, -11.0F, 0.025390625F, 0.095703125F}},
                {{11.5F, -27.0F, 11.0F, 0.0693359375F, 0.05078125F}, {11.5F, -27.0F, -11.0F, 0.0478515625F, 0.05078125F}, {11.5F, -4.0F, -11.0F, 0.0478515625F, 0.095703125F}, {11.5F, -4.0F, 11.0F, 0.0693359375F, 0.095703125F}},
                {{11.5F, -27.0F, -11.0F, 0.0478515625F, 0.05078125F}, {-11.5F, -27.0F, -11.0F, 0.025390625F, 0.05078125F}, {-11.5F, -4.0F, -11.0F, 0.025390625F, 0.095703125F}, {11.5F, -4.0F, -11.0F, 0.0478515625F, 0.095703125F}},
                {{-11.5F, -27.0F, 11.0F, 0.091796875F, 0.05078125F}, {11.5F, -27.0F, 11.0F, 0.0693359375F, 0.05078125F}, {11.5F, -4.0F, 11.0F, 0.0693359375F, 0.095703125F}, {-11.5F, -4.0F, 11.0F, 0.091796875F, 0.095703125F}}
        }));
        p11Cubes.add(new ReferenceMeshCube(-11.5F, -4.0F, -11.0F, 23.0F, 4.0F, 22.0F, new float[][][] {
                {{11.5F, -4.0F, 11.0F, 0.0478515625F, 0.095703125F}, {-11.5F, -4.0F, 11.0F, 0.025390625F, 0.095703125F}, {-11.5F, -4.0F, -11.0F, 0.025390625F, 0.138671875F}, {11.5F, -4.0F, -11.0F, 0.0478515625F, 0.138671875F}},
                {{11.5F, 0.0F, -11.0F, 0.0703125F, 0.138671875F}, {-11.5F, 0.0F, -11.0F, 0.0478515625F, 0.138671875F}, {-11.5F, 0.0F, 11.0F, 0.0478515625F, 0.095703125F}, {11.5F, 0.0F, 11.0F, 0.0703125F, 0.095703125F}},
                {{-11.5F, -4.0F, -11.0F, 0.025390625F, 0.138671875F}, {-11.5F, -4.0F, 11.0F, 0.00390625F, 0.138671875F}, {-11.5F, 0.0F, 11.0F, 0.00390625F, 0.146484375F}, {-11.5F, 0.0F, -11.0F, 0.025390625F, 0.146484375F}},
                {{11.5F, -4.0F, 11.0F, 0.0693359375F, 0.138671875F}, {11.5F, -4.0F, -11.0F, 0.0478515625F, 0.138671875F}, {11.5F, 0.0F, -11.0F, 0.0478515625F, 0.146484375F}, {11.5F, 0.0F, 11.0F, 0.0693359375F, 0.146484375F}},
                {{11.5F, -4.0F, -11.0F, 0.0478515625F, 0.138671875F}, {-11.5F, -4.0F, -11.0F, 0.025390625F, 0.138671875F}, {-11.5F, 0.0F, -11.0F, 0.025390625F, 0.146484375F}, {11.5F, 0.0F, -11.0F, 0.0478515625F, 0.146484375F}},
                {{-11.5F, -4.0F, 11.0F, 0.091796875F, 0.138671875F}, {11.5F, -4.0F, 11.0F, 0.0693359375F, 0.138671875F}, {11.5F, 0.0F, 11.0F, 0.0693359375F, 0.146484375F}, {-11.5F, 0.0F, 11.0F, 0.091796875F, 0.146484375F}}
        }));
        p11Cubes.add(new ReferenceMeshCube(-11.51F, -0.01F, -11.01F, 23.02F, 0.02F, 22.02F, new float[][][] {
                {{11.51F, -0.01F, 11.01F, 0.0927734375F, 0.095703125F}, {-11.51F, -0.01F, 11.01F, 0.0703125F, 0.095703125F}, {-11.51F, -0.01F, -11.01F, 0.0703125F, 0.138671875F}, {11.51F, -0.01F, -11.01F, 0.0927734375F, 0.138671875F}},
                {{11.51F, 0.01F, -11.01F, 0.115234375F, 0.138671875F}, {-11.51F, 0.01F, -11.01F, 0.0927734375F, 0.138671875F}, {-11.51F, 0.01F, 11.01F, 0.0927734375F, 0.095703125F}, {11.51F, 0.01F, 11.01F, 0.115234375F, 0.095703125F}},
                {{-11.51F, -0.01F, -11.01F, 0.0703125F, 0.138671875F}, {-11.51F, -0.01F, 11.01F, 0.048828125F, 0.138671875F}, {-11.51F, 0.01F, 11.01F, 0.048828125F, 0.138671875F}, {-11.51F, 0.01F, -11.01F, 0.0703125F, 0.138671875F}},
                {{11.51F, -0.01F, 11.01F, 0.114257812F, 0.138671875F}, {11.51F, -0.01F, -11.01F, 0.0927734375F, 0.138671875F}, {11.51F, 0.01F, -11.01F, 0.0927734375F, 0.138671875F}, {11.51F, 0.01F, 11.01F, 0.114257812F, 0.138671875F}},
                {{11.51F, -0.01F, -11.01F, 0.0927734375F, 0.138671875F}, {-11.51F, -0.01F, -11.01F, 0.0703125F, 0.138671875F}, {-11.51F, 0.01F, -11.01F, 0.0703125F, 0.138671875F}, {11.51F, 0.01F, -11.01F, 0.0927734375F, 0.138671875F}},
                {{-11.51F, -0.01F, 11.01F, 0.13671875F, 0.138671875F}, {11.51F, -0.01F, 11.01F, 0.114257812F, 0.138671875F}, {11.51F, 0.01F, 11.01F, 0.114257812F, 0.138671875F}, {-11.51F, 0.01F, 11.01F, 0.13671875F, 0.138671875F}}
        }));
        var p11 = new net.minecraft.client.model.geom.ModelPart(p11Cubes, java.util.Map.ofEntries());
        p11.setInitialPose(PartPose.offsetAndRotation(-0.5F, -18.0F, 2.0F, 0.0F, 0.0F, 0.0F)); p11.resetPose();
        var p10Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body1/body1/ender/inner_body/top_body/ender2", java.util.List.of()));
        var p10 = new net.minecraft.client.model.geom.ModelPart(p10Cubes, java.util.Map.ofEntries(java.util.Map.entry("head5", p11), java.util.Map.entry("left_arm4", p12), java.util.Map.entry("right_arm4", p17)));
        p10.setInitialPose(PartPose.offsetAndRotation(0.0F, -19.0F, -10.0F, 0.0F, 0.0F, 0.0F)); p10.resetPose();
        var p9Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body1/body1/ender/inner_body/top_body", java.util.List.of()));
        p9Cubes.add(new ReferenceMeshCube(-11.5F, -37.0F, -10.0F, 22.0F, 37.0F, 20.0F, new float[][][] {
                {{10.5F, -37.0F, 10.0F, 0.044921875F, 0.146484375F}, {-11.5F, -37.0F, 10.0F, 0.0234375F, 0.146484375F}, {-11.5F, -37.0F, -10.0F, 0.0234375F, 0.185546875F}, {10.5F, -37.0F, -10.0F, 0.044921875F, 0.185546875F}},
                {{10.5F, 0.0F, -10.0F, 0.06640625F, 0.185546875F}, {-11.5F, 0.0F, -10.0F, 0.044921875F, 0.185546875F}, {-11.5F, 0.0F, 10.0F, 0.044921875F, 0.146484375F}, {10.5F, 0.0F, 10.0F, 0.06640625F, 0.146484375F}},
                {{-11.5F, -37.0F, -10.0F, 0.0234375F, 0.185546875F}, {-11.5F, -37.0F, 10.0F, 0.00390625F, 0.185546875F}, {-11.5F, 0.0F, 10.0F, 0.00390625F, 0.2578125F}, {-11.5F, 0.0F, -10.0F, 0.0234375F, 0.2578125F}},
                {{10.5F, -37.0F, 10.0F, 0.064453125F, 0.185546875F}, {10.5F, -37.0F, -10.0F, 0.044921875F, 0.185546875F}, {10.5F, 0.0F, -10.0F, 0.044921875F, 0.2578125F}, {10.5F, 0.0F, 10.0F, 0.064453125F, 0.2578125F}},
                {{10.5F, -37.0F, -10.0F, 0.044921875F, 0.185546875F}, {-11.5F, -37.0F, -10.0F, 0.0234375F, 0.185546875F}, {-11.5F, 0.0F, -10.0F, 0.0234375F, 0.2578125F}, {10.5F, 0.0F, -10.0F, 0.044921875F, 0.2578125F}},
                {{-11.5F, -37.0F, 10.0F, 0.0859375F, 0.185546875F}, {10.5F, -37.0F, 10.0F, 0.064453125F, 0.185546875F}, {10.5F, 0.0F, 10.0F, 0.064453125F, 0.2578125F}, {-11.5F, 0.0F, 10.0F, 0.0859375F, 0.2578125F}}
        }));
        var p9 = new net.minecraft.client.model.geom.ModelPart(p9Cubes, java.util.Map.ofEntries(java.util.Map.entry("ender2", p10)));
        p9.setInitialPose(PartPose.offsetAndRotation(-10.0F, 19.0F, 10.0F, 0.0F, 0.0F, 0.0F)); p9.resetPose();
        var p8Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body1/body1/ender/inner_body", java.util.List.of()));
        p8Cubes.add(new ReferenceMeshCube(-21.5F, 19.0F, 0.0F, 22.0F, 37.0F, 20.0F, new float[][][] {
                {{-21.5F, 19.0F, 0.0F, 0.126953125F, 0.185546875F}, {0.5F, 19.0F, 0.0F, 0.10546875F, 0.185546875F}, {0.5F, 19.0F, 20.0F, 0.10546875F, 0.146484375F}, {-21.5F, 19.0F, 20.0F, 0.126953125F, 0.146484375F}},
                {{-21.5F, 56.0F, 20.0F, 0.1484375F, 0.146484375F}, {0.5F, 56.0F, 20.0F, 0.126953125F, 0.146484375F}, {0.5F, 56.0F, 0.0F, 0.126953125F, 0.185546875F}, {-21.5F, 56.0F, 0.0F, 0.1484375F, 0.185546875F}},
                {{0.5F, 56.0F, 0.0F, 0.10546875F, 0.2578125F}, {0.5F, 56.0F, 20.0F, 0.0859375F, 0.2578125F}, {0.5F, 19.0F, 20.0F, 0.0859375F, 0.185546875F}, {0.5F, 19.0F, 0.0F, 0.10546875F, 0.185546875F}},
                {{-21.5F, 56.0F, 20.0F, 0.146484375F, 0.2578125F}, {-21.5F, 56.0F, 0.0F, 0.126953125F, 0.2578125F}, {-21.5F, 19.0F, 0.0F, 0.126953125F, 0.185546875F}, {-21.5F, 19.0F, 20.0F, 0.146484375F, 0.185546875F}},
                {{-21.5F, 56.0F, 0.0F, 0.126953125F, 0.2578125F}, {0.5F, 56.0F, 0.0F, 0.10546875F, 0.2578125F}, {0.5F, 19.0F, 0.0F, 0.10546875F, 0.185546875F}, {-21.5F, 19.0F, 0.0F, 0.126953125F, 0.185546875F}},
                {{0.5F, 56.0F, 20.0F, 0.16796875F, 0.2578125F}, {-21.5F, 56.0F, 20.0F, 0.146484375F, 0.2578125F}, {-21.5F, 19.0F, 20.0F, 0.146484375F, 0.185546875F}, {0.5F, 19.0F, 20.0F, 0.16796875F, 0.185546875F}}
        }));
        var p8 = new net.minecraft.client.model.geom.ModelPart(p8Cubes, java.util.Map.ofEntries(java.util.Map.entry("top_body", p9), java.util.Map.entry("left_arm3", p26), java.util.Map.entry("right_arm3", p29), java.util.Map.entry("arch_illager", p34)));
        p8.setInitialPose(PartPose.offsetAndRotation(10.0F, -24.0F, -11.5F, 0.0F, 0.0F, 0.0F)); p8.resetPose();
        var p7Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body1/body1/ender", java.util.List.of()));
        var p7 = new net.minecraft.client.model.geom.ModelPart(p7Cubes, java.util.Map.ofEntries(java.util.Map.entry("inner_body", p8)));
        p7.setInitialPose(PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p7.resetPose();
        var p6Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body1/body1", java.util.List.of()));
        var p6 = new net.minecraft.client.model.geom.ModelPart(p6Cubes, java.util.Map.ofEntries(java.util.Map.entry("ender", p7)));
        p6.setInitialPose(PartPose.offsetAndRotation(0.0F, 24.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p6.resetPose();
        var p5Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_head3", java.util.List.of()));
        var p5 = new net.minecraft.client.model.geom.ModelPart(p5Cubes, java.util.Map.ofEntries(java.util.Map.entry("head3", p53)));
        p5.setInitialPose(PartPose.offsetAndRotation(10.0F, 4.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p5.resetPose();
        var p4Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_head2", java.util.List.of()));
        var p4 = new net.minecraft.client.model.geom.ModelPart(p4Cubes, java.util.Map.ofEntries(java.util.Map.entry("head2", p52)));
        p4.setInitialPose(PartPose.offsetAndRotation(-8.0F, 4.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p4.resetPose();
        var p3Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_head1", java.util.List.of()));
        var p3 = new net.minecraft.client.model.geom.ModelPart(p3Cubes, java.util.Map.ofEntries(java.util.Map.entry("head1", p51)));
        p3.setInitialPose(PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p3.resetPose();
        var p2Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body3", java.util.List.of()));
        var p2 = new net.minecraft.client.model.geom.ModelPart(p2Cubes, java.util.Map.ofEntries(java.util.Map.entry("body3", p50)));
        p2.setInitialPose(PartPose.offsetAndRotation(-2.0F, 16.6922281F, 1.52787293F, 0.83252203F, 0.0F, 0.0F)); p2.resetPose();
        var p1Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body2", java.util.List.of()));
        var p1 = new net.minecraft.client.model.geom.ModelPart(p1Cubes, java.util.Map.ofEntries(java.util.Map.entry("body2", p49)));
        p1.setInitialPose(PartPose.offsetAndRotation(-2.0F, 6.9F, -0.5F, 0.20420352F, 0.0F, 0.0F)); p1.resetPose();
        var p0Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body1", java.util.List.of()));
        var p0 = new net.minecraft.client.model.geom.ModelPart(p0Cubes, java.util.Map.ofEntries(java.util.Map.entry("body1", p6)));
        p0.setInitialPose(PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p0.resetPose();
        return new net.minecraft.client.model.geom.ModelPart(java.util.List.of(), java.util.Map.ofEntries(java.util.Map.entry("__cem_host_body1", p0), java.util.Map.entry("__cem_host_body2", p1), java.util.Map.entry("__cem_host_body3", p2), java.util.Map.entry("__cem_host_head1", p3), java.util.Map.entry("__cem_host_head2", p4), java.util.Map.entry("__cem_host_head3", p5)));
    }
}
