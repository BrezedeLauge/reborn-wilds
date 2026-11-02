/*
 * Generated adaptation of ReCrafted Mobs 11.0 by GillsBills and credited contributors.
 * Authored asset license: CC-BY-NC-SA-4.0; https://creativecommons.org/licenses/by-nc-sa/4.0/
 * Source: https://modrinth.com/resourcepack/recrafted-mobs
 * Model: assets/minecraft/optifine/cem/allay/allay4.jem
 * Source SHA256: 83520ffd62c0d5cfcafa1e969a0d7222fd787d7e712f37fced8930d024166ec0
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

public final class CemPetBabyGhastGeometry {
    private CemPetBabyGhastGeometry() {}

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
        PartDefinition p8 = p7.addOrReplaceChild("nether_team", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -6.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p9 = p8.addOrReplaceChild("baby_ghast", CubeListBuilder.create()
                .texOffs(0, 8).mirror(false)
                .addBox(-3.0F, -3.0F, -2.0F, 6.0F, 6.0F, 6.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(0.0F, 2.0F, -1.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p10 = p9.addOrReplaceChild("heart", CubeListBuilder.create()
                .texOffs(2, 0).mirror(false)
                .addBox(-2.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p11 = p9.addOrReplaceChild("front", CubeListBuilder.create()
                .texOffs(0, 13).mirror(false)
                .addBox(2.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(0, 13).mirror(false)
                .addBox(-0.5F, 0.0F, 0.0F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(0, 13).mirror(false)
                .addBox(-3.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(0.0F, 3.0F, -2.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p12 = p9.addOrReplaceChild("back", CubeListBuilder.create()
                .texOffs(0, 13).mirror(false)
                .addBox(2.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(0, 13).mirror(false)
                .addBox(-0.5F, 0.0F, 0.0F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(0, 13).mirror(false)
                .addBox(-3.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(0.0F, 3.0F, 4.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p13 = p9.addOrReplaceChild("right", CubeListBuilder.create()
                .texOffs(0, 12).mirror(false)
                .addBox(0.0F, 0.0F, 2.0F, 0.0F, 1.0F, 1.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(0, 12).mirror(false)
                .addBox(0.0F, 0.0F, -1.0F, 0.0F, 1.0F, 1.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(-3.0F, 3.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p14 = p9.addOrReplaceChild("left", CubeListBuilder.create()
                .texOffs(0, 12).mirror(true)
                .addBox(0.0F, 0.0F, 2.0F, 0.0F, 1.0F, 1.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(0, 12).mirror(true)
                .addBox(0.0F, 0.0F, -1.0F, 0.0F, 1.0F, 1.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(3.0F, 3.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p15 = p2.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 6.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p16 = p4.addOrReplaceChild("right_arm", CubeListBuilder.create(), PartPose.offsetAndRotation(1.75F, 5.5F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p17 = p3.addOrReplaceChild("left_arm", CubeListBuilder.create(), PartPose.offsetAndRotation(-1.75F, 5.5F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p18 = p5.addOrReplaceChild("left_wing", CubeListBuilder.create(), PartPose.offsetAndRotation(-0.5F, 6.0F, -1.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p19 = p6.addOrReplaceChild("right_wing", CubeListBuilder.create(), PartPose.offsetAndRotation(0.5F, 6.0F, -1.0F, 0.0F, 0.0F, 0.0F));
        return LayerDefinition.create(mesh, 32, 32);
    }

    /** Preserve source expression units; static scales are reapplied after every pose reset. */
    public static ReferenceMobDefinition.Builder configure(ReferenceMobDefinition.Builder builder) {
        builder.cemDriver("allay");
        builder.bakedRoot(CemPetBabyGhastGeometry::bakeRoot);
        builder.hostAlias("root", "__cem_host_root");
        builder.hostAlias("head", "__cem_host_head");
        builder.hostAlias("body", "__cem_host_body");
        builder.hostAlias("left_arm", "__cem_host_left_arm");
        builder.hostAlias("right_arm", "__cem_host_right_arm");
        builder.hostAlias("left_wing", "__cem_host_left_wing");
        builder.hostAlias("right_wing", "__cem_host_right_wing");
        builder.expression("nether_team.rx", "-head.rx");
        builder.expression("nether_team.ry", "-head.ry");
        builder.expression("nether_team.rz", "-head.rz");
        builder.expression("body.rx", "0+torad(50)");
        builder.expression("nether_team.ty", "-8");
        builder.expression("body.ty", "-4");
        builder.expression("body.tz", "2");
        builder.expression("front.rx", "torad(-40)+right_arm.rz");
        builder.expression("back.rx", "torad(40)+ -right_arm.rz");
        builder.expression("right.rz", "torad(0)+right_arm.rz");
        builder.expression("left.rz", "torad(0)+ -right_arm.rz");
        builder.expression("heart.sx", "1.1 + sin(time/4)*0.1");
        builder.expression("heart.sy", "1.1 + sin(time/4)*0.1");
        builder.expression("heart.sz", "1.1 + sin(time/4)*0.1");
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
        var p19Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_root/__cem_host_body/__cem_host_right_wing/right_wing", java.util.List.of()));
        var p19 = new net.minecraft.client.model.geom.ModelPart(p19Cubes, java.util.Map.ofEntries());
        p19.setInitialPose(PartPose.offsetAndRotation(0.5F, 6.0F, -1.0F, 0.0F, 0.0F, 0.0F)); p19.resetPose();
        var p18Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_root/__cem_host_body/__cem_host_left_wing/left_wing", java.util.List.of()));
        var p18 = new net.minecraft.client.model.geom.ModelPart(p18Cubes, java.util.Map.ofEntries());
        p18.setInitialPose(PartPose.offsetAndRotation(-0.5F, 6.0F, -1.0F, 0.0F, 0.0F, 0.0F)); p18.resetPose();
        var p17Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_root/__cem_host_body/__cem_host_left_arm/left_arm", java.util.List.of()));
        var p17 = new net.minecraft.client.model.geom.ModelPart(p17Cubes, java.util.Map.ofEntries());
        p17.setInitialPose(PartPose.offsetAndRotation(-1.75F, 5.5F, 0.0F, 0.0F, 0.0F, 0.0F)); p17.resetPose();
        var p16Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_root/__cem_host_body/__cem_host_right_arm/right_arm", java.util.List.of()));
        var p16 = new net.minecraft.client.model.geom.ModelPart(p16Cubes, java.util.Map.ofEntries());
        p16.setInitialPose(PartPose.offsetAndRotation(1.75F, 5.5F, 0.0F, 0.0F, 0.0F, 0.0F)); p16.resetPose();
        var p15Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_root/__cem_host_body/body", java.util.List.of()));
        var p15 = new net.minecraft.client.model.geom.ModelPart(p15Cubes, java.util.Map.ofEntries());
        p15.setInitialPose(PartPose.offsetAndRotation(0.0F, 6.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p15.resetPose();
        var p14Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_root/__cem_host_head/head/nether_team/baby_ghast/left", java.util.List.of()));
        var p14 = new net.minecraft.client.model.geom.ModelPart(p14Cubes, java.util.Map.ofEntries());
        p14.setInitialPose(PartPose.offsetAndRotation(3.0F, 3.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p14.resetPose();
        var p13Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_root/__cem_host_head/head/nether_team/baby_ghast/right", java.util.List.of()));
        var p13 = new net.minecraft.client.model.geom.ModelPart(p13Cubes, java.util.Map.ofEntries());
        p13.setInitialPose(PartPose.offsetAndRotation(-3.0F, 3.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p13.resetPose();
        var p12Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_root/__cem_host_head/head/nether_team/baby_ghast/back", java.util.List.of()));
        var p12 = new net.minecraft.client.model.geom.ModelPart(p12Cubes, java.util.Map.ofEntries());
        p12.setInitialPose(PartPose.offsetAndRotation(0.0F, 3.0F, 4.0F, 0.0F, 0.0F, 0.0F)); p12.resetPose();
        var p11Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_root/__cem_host_head/head/nether_team/baby_ghast/front", java.util.List.of()));
        var p11 = new net.minecraft.client.model.geom.ModelPart(p11Cubes, java.util.Map.ofEntries());
        p11.setInitialPose(PartPose.offsetAndRotation(0.0F, 3.0F, -2.0F, 0.0F, 0.0F, 0.0F)); p11.resetPose();
        var p10Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_root/__cem_host_head/head/nether_team/baby_ghast/heart", java.util.List.of()));
        var p10 = new net.minecraft.client.model.geom.ModelPart(p10Cubes, java.util.Map.ofEntries());
        p10.setInitialPose(PartPose.offsetAndRotation(0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F)); p10.resetPose();
        var p9Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_root/__cem_host_head/head/nether_team/baby_ghast", java.util.List.of()));
        var p9 = new net.minecraft.client.model.geom.ModelPart(p9Cubes, java.util.Map.ofEntries(java.util.Map.entry("heart", p10), java.util.Map.entry("front", p11), java.util.Map.entry("back", p12), java.util.Map.entry("right", p13), java.util.Map.entry("left", p14)));
        p9.setInitialPose(PartPose.offsetAndRotation(0.0F, 2.0F, -1.0F, 0.0F, 0.0F, 0.0F)); p9.resetPose();
        var p8Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_root/__cem_host_head/head/nether_team", java.util.List.of()));
        var p8 = new net.minecraft.client.model.geom.ModelPart(p8Cubes, java.util.Map.ofEntries(java.util.Map.entry("baby_ghast", p9)));
        p8.setInitialPose(PartPose.offsetAndRotation(0.0F, -6.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p8.resetPose();
        var p7Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_root/__cem_host_head/head", java.util.List.of()));
        var p7 = new net.minecraft.client.model.geom.ModelPart(p7Cubes, java.util.Map.ofEntries(java.util.Map.entry("nether_team", p8)));
        p7.setInitialPose(PartPose.offsetAndRotation(0.0F, 6.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p7.resetPose();
        var p6Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_root/__cem_host_body/__cem_host_right_wing", java.util.List.of()));
        var p6 = new net.minecraft.client.model.geom.ModelPart(p6Cubes, java.util.Map.ofEntries(java.util.Map.entry("right_wing", p19)));
        p6.setInitialPose(nativeRoot.getChild("root").getChild("body").getChild("right_wing").storePose()); p6.resetPose();
        var p5Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_root/__cem_host_body/__cem_host_left_wing", java.util.List.of()));
        var p5 = new net.minecraft.client.model.geom.ModelPart(p5Cubes, java.util.Map.ofEntries(java.util.Map.entry("left_wing", p18)));
        p5.setInitialPose(nativeRoot.getChild("root").getChild("body").getChild("left_wing").storePose()); p5.resetPose();
        var p4Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_root/__cem_host_body/__cem_host_right_arm", java.util.List.of()));
        var p4 = new net.minecraft.client.model.geom.ModelPart(p4Cubes, java.util.Map.ofEntries(java.util.Map.entry("right_arm", p16)));
        p4.setInitialPose(nativeRoot.getChild("root").getChild("body").getChild("right_arm").storePose()); p4.resetPose();
        var p3Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_root/__cem_host_body/__cem_host_left_arm", java.util.List.of()));
        var p3 = new net.minecraft.client.model.geom.ModelPart(p3Cubes, java.util.Map.ofEntries(java.util.Map.entry("left_arm", p17)));
        p3.setInitialPose(nativeRoot.getChild("root").getChild("body").getChild("left_arm").storePose()); p3.resetPose();
        var p2Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_root/__cem_host_body", java.util.List.of()));
        var p2 = new net.minecraft.client.model.geom.ModelPart(p2Cubes, java.util.Map.ofEntries(java.util.Map.entry("__cem_host_left_arm", p3), java.util.Map.entry("__cem_host_right_arm", p4), java.util.Map.entry("__cem_host_left_wing", p5), java.util.Map.entry("__cem_host_right_wing", p6), java.util.Map.entry("body", p15)));
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
