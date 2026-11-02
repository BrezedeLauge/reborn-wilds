/*
 * Generated adaptation of ReCrafted Mobs 11.0 by GillsBills and credited contributors.
 * Authored asset license: CC-BY-NC-SA-4.0; https://creativecommons.org/licenses/by-nc-sa/4.0/
 * Source: https://modrinth.com/resourcepack/recrafted-mobs
 * Model: assets/minecraft/optifine/cem/silverfish/silverfish2.jem
 * Source SHA256: 93dae6f8f1c05187788a2040b1cb5e9eb439d41a1b6a5fbb57501c180416b247
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

public final class CemPetMoleGeometry {
    private CemPetMoleGeometry() {}

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        PartDefinition p0 = root.addOrReplaceChild("__cem_host_body1", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p1 = root.addOrReplaceChild("__cem_host_body2", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p2 = root.addOrReplaceChild("__cem_host_body3", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p3 = root.addOrReplaceChild("__cem_host_body4", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p4 = root.addOrReplaceChild("__cem_host_body5", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p5 = root.addOrReplaceChild("__cem_host_body6", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p6 = root.addOrReplaceChild("__cem_host_body7", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p7 = root.addOrReplaceChild("__cem_host_wing1", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p8 = root.addOrReplaceChild("__cem_host_wing2", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p9 = root.addOrReplaceChild("__cem_host_wing3", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p10 = p0.addOrReplaceChild("body1", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 2.0F, 3.5F, 0.0F, 0.0F, 0.0F));
        PartDefinition p11 = p1.addOrReplaceChild("body2", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 3.0F, 1.5F, 0.0F, 0.0F, 0.0F));
        PartDefinition p12 = p2.addOrReplaceChild("body3", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 4.0F, -1.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p13 = p12.addOrReplaceChild("mole", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p14 = p13.addOrReplaceChild("ro", CubeListBuilder.create()
                .texOffs(0, 0).mirror(false)
                .addBox(-9.0F, -5.0F, 0.0F, 9.0F, 5.0F, 6.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(-3.0F, 0.0F, -4.0F, 0.0F, 1.57079633F, 0.0F));
        PartDefinition p15 = p14.addOrReplaceChild("head", CubeListBuilder.create()
                .texOffs(9, 11).mirror(false)
                .addBox(-1.5F, -1.5F, -2.0F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(0.0F, -1.0F, 3.0F, 0.0F, -1.57079633F, 0.0F));
        PartDefinition p16 = p14.addOrReplaceChild("right_arm", CubeListBuilder.create(), PartPose.offsetAndRotation(-1.0F, -3.0F, 0.0F, 0.0F, -1.57079633F, 0.0F));
        PartDefinition p17 = p16.addOrReplaceChild("right_arm2", CubeListBuilder.create()
                .texOffs(0, 14).mirror(false)
                .addBox(-4.0F, -3.0F, 0.0F, 4.0F, 5.0F, 0.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p18 = p14.addOrReplaceChild("tail", CubeListBuilder.create()
                .texOffs(0, 0).mirror(false)
                .addBox(0.0F, -4.0F, -1.0F, 0.0F, 4.0F, 2.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(-9.0F, 0.0F, 3.0F, -3.14159265F, 0.0F, 1.57079633F));
        PartDefinition p19 = p14.addOrReplaceChild("right_armback2", CubeListBuilder.create(), PartPose.offsetAndRotation(-8.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p20 = p19.addOrReplaceChild("right_armback", CubeListBuilder.create(), PartPose.offsetAndRotation(2.0F, 0.0F, 0.0F, 0.0F, 1.57079633F, 0.0F));
        PartDefinition p21 = p20.addOrReplaceChild("body3_sub_8", CubeListBuilder.create()
                .texOffs(0, 11).mirror(true)
                .addBox(0.0F, 0.0F, -3.0F, 3.0F, 0.0F, 3.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.ZERO);
        PartDefinition p22 = p13.addOrReplaceChild("left_armback2", CubeListBuilder.create(), PartPose.offsetAndRotation(3.0F, 0.0F, 4.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p23 = p22.addOrReplaceChild("left_armback", CubeListBuilder.create()
                .texOffs(0, 11).mirror(false)
                .addBox(-3.0F, 0.0F, -3.0F, 3.0F, 0.0F, 3.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -2.0F, 3.14159265F, 0.0F, 3.14159265F));
        PartDefinition p24 = p13.addOrReplaceChild("left_arm", CubeListBuilder.create(), PartPose.offsetAndRotation(3.0F, -3.0F, -3.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p25 = p24.addOrReplaceChild("left_arm2", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p26 = p25.addOrReplaceChild("body3_sub_13", CubeListBuilder.create()
                .texOffs(0, 14).mirror(true)
                .addBox(0.0F, -3.0F, 0.0F, 4.0F, 5.0F, 0.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.ZERO);
        PartDefinition p27 = p3.addOrReplaceChild("body4", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 3.0F, -4.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p28 = p4.addOrReplaceChild("body5", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 2.0F, -7.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p29 = p5.addOrReplaceChild("body6", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 1.0F, -9.5F, 0.0F, 0.0F, 0.0F));
        PartDefinition p30 = p6.addOrReplaceChild("body7", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 1.0F, -11.5F, 0.0F, 0.0F, 0.0F));
        PartDefinition p31 = p7.addOrReplaceChild("wing1", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 8.0F, -1.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p32 = p8.addOrReplaceChild("wing2", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 4.0F, -7.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p33 = p9.addOrReplaceChild("wing3", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 5.0F, 1.5F, 0.0F, 0.0F, 0.0F));
        return LayerDefinition.create(mesh, 32, 32);
    }

    /** Preserve source expression units; static scales are reapplied after every pose reset. */
    public static ReferenceMobDefinition.Builder configure(ReferenceMobDefinition.Builder builder) {
        builder.cemDriver("silverfish");
        builder.bakedRoot(CemPetMoleGeometry::bakeRoot);
        builder.hostAlias("body1", "__cem_host_body1");
        builder.hostAlias("body2", "__cem_host_body2");
        builder.hostAlias("body3", "__cem_host_body3");
        builder.hostAlias("body4", "__cem_host_body4");
        builder.hostAlias("body5", "__cem_host_body5");
        builder.hostAlias("body6", "__cem_host_body6");
        builder.hostAlias("body7", "__cem_host_body7");
        builder.hostAlias("wing1", "__cem_host_wing1");
        builder.hostAlias("wing2", "__cem_host_wing2");
        builder.hostAlias("wing3", "__cem_host_wing3");
        builder.expression("mole.rx", "-body3.rx");
        builder.expression("mole.ry", "-body3.ry");
        builder.expression("mole.rz", "-body3.rz");
        builder.expression("head.ry", "body1.ry/2 + torad(-90)");
        builder.expression("left_arm.rx", "torad(-40)");
        builder.expression("right_arm.rz", "torad(-40)");
        builder.expression("right_arm.ry", "torad(-90)");
        builder.expression("right_arm2.ry", "sin(limb_swing)*0.5");
        builder.expression("left_arm2.ry", "sin(limb_swing)*0.5");
        builder.expression("right_armback2.ry", "-sin(limb_swing)*0.5");
        builder.expression("left_armback2.ry", "-sin(limb_swing)*0.5");
        builder.expression("right_armback.ry", "torad(90)");
        builder.expression("left_armback.rz", "torad(180)");
        builder.expression("right_armback.rz", "torad(45)");
        builder.expression("left_armback.rx", "torad(140)");
        return builder;
    }

    /** Complete matching rig; installed cubes stay owned by the Minecraft client. */
    public static net.minecraft.client.model.geom.ModelPart bakeRoot() {
        var nativeRoot = net.minecraft.client.model.SilverfishModel.createBodyLayer().bakeRoot();
        var nativeCubes = new java.util.HashMap<String, java.util.List<net.minecraft.client.model.geom.ModelPart.Cube>>();
        nativeRoot.visit(new com.mojang.blaze3d.vertex.PoseStack(),
                (pose, path, index, cube) -> nativeCubes.computeIfAbsent(path, key -> new java.util.ArrayList<>()).add(cube));
        var customCubes = new java.util.HashMap<String, java.util.List<net.minecraft.client.model.geom.ModelPart.Cube>>();
        createBodyLayer().bakeRoot().visit(new com.mojang.blaze3d.vertex.PoseStack(),
                (pose, path, index, cube) -> customCubes.computeIfAbsent(path, key -> new java.util.ArrayList<>()).add(cube));
        var p33Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_wing3/wing3", java.util.List.of()));
        var p33 = new net.minecraft.client.model.geom.ModelPart(p33Cubes, java.util.Map.ofEntries());
        p33.setInitialPose(PartPose.offsetAndRotation(0.0F, 5.0F, 1.5F, 0.0F, 0.0F, 0.0F)); p33.resetPose();
        var p32Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_wing2/wing2", java.util.List.of()));
        var p32 = new net.minecraft.client.model.geom.ModelPart(p32Cubes, java.util.Map.ofEntries());
        p32.setInitialPose(PartPose.offsetAndRotation(0.0F, 4.0F, -7.0F, 0.0F, 0.0F, 0.0F)); p32.resetPose();
        var p31Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_wing1/wing1", java.util.List.of()));
        var p31 = new net.minecraft.client.model.geom.ModelPart(p31Cubes, java.util.Map.ofEntries());
        p31.setInitialPose(PartPose.offsetAndRotation(0.0F, 8.0F, -1.0F, 0.0F, 0.0F, 0.0F)); p31.resetPose();
        var p30Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body7/body7", java.util.List.of()));
        var p30 = new net.minecraft.client.model.geom.ModelPart(p30Cubes, java.util.Map.ofEntries());
        p30.setInitialPose(PartPose.offsetAndRotation(0.0F, 1.0F, -11.5F, 0.0F, 0.0F, 0.0F)); p30.resetPose();
        var p29Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body6/body6", java.util.List.of()));
        var p29 = new net.minecraft.client.model.geom.ModelPart(p29Cubes, java.util.Map.ofEntries());
        p29.setInitialPose(PartPose.offsetAndRotation(0.0F, 1.0F, -9.5F, 0.0F, 0.0F, 0.0F)); p29.resetPose();
        var p28Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body5/body5", java.util.List.of()));
        var p28 = new net.minecraft.client.model.geom.ModelPart(p28Cubes, java.util.Map.ofEntries());
        p28.setInitialPose(PartPose.offsetAndRotation(0.0F, 2.0F, -7.0F, 0.0F, 0.0F, 0.0F)); p28.resetPose();
        var p27Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body4/body4", java.util.List.of()));
        var p27 = new net.minecraft.client.model.geom.ModelPart(p27Cubes, java.util.Map.ofEntries());
        p27.setInitialPose(PartPose.offsetAndRotation(0.0F, 3.0F, -4.0F, 0.0F, 0.0F, 0.0F)); p27.resetPose();
        var p26Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body3/body3/mole/left_arm/left_arm2/body3_sub_13", java.util.List.of()));
        var p26 = new net.minecraft.client.model.geom.ModelPart(p26Cubes, java.util.Map.ofEntries());
        p26.setInitialPose(PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p26.resetPose();
        var p25Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body3/body3/mole/left_arm/left_arm2", java.util.List.of()));
        var p25 = new net.minecraft.client.model.geom.ModelPart(p25Cubes, java.util.Map.ofEntries(java.util.Map.entry("body3_sub_13", p26)));
        p25.setInitialPose(PartPose.offsetAndRotation(0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p25.resetPose();
        var p24Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body3/body3/mole/left_arm", java.util.List.of()));
        var p24 = new net.minecraft.client.model.geom.ModelPart(p24Cubes, java.util.Map.ofEntries(java.util.Map.entry("left_arm2", p25)));
        p24.setInitialPose(PartPose.offsetAndRotation(3.0F, -3.0F, -3.0F, 0.0F, 0.0F, 0.0F)); p24.resetPose();
        var p23Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body3/body3/mole/left_armback2/left_armback", java.util.List.of()));
        var p23 = new net.minecraft.client.model.geom.ModelPart(p23Cubes, java.util.Map.ofEntries());
        p23.setInitialPose(PartPose.offsetAndRotation(0.0F, 0.0F, -2.0F, 3.14159265F, 0.0F, 3.14159265F)); p23.resetPose();
        var p22Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body3/body3/mole/left_armback2", java.util.List.of()));
        var p22 = new net.minecraft.client.model.geom.ModelPart(p22Cubes, java.util.Map.ofEntries(java.util.Map.entry("left_armback", p23)));
        p22.setInitialPose(PartPose.offsetAndRotation(3.0F, 0.0F, 4.0F, 0.0F, 0.0F, 0.0F)); p22.resetPose();
        var p21Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body3/body3/mole/ro/right_armback2/right_armback/body3_sub_8", java.util.List.of()));
        var p21 = new net.minecraft.client.model.geom.ModelPart(p21Cubes, java.util.Map.ofEntries());
        p21.setInitialPose(PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p21.resetPose();
        var p20Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body3/body3/mole/ro/right_armback2/right_armback", java.util.List.of()));
        var p20 = new net.minecraft.client.model.geom.ModelPart(p20Cubes, java.util.Map.ofEntries(java.util.Map.entry("body3_sub_8", p21)));
        p20.setInitialPose(PartPose.offsetAndRotation(2.0F, 0.0F, 0.0F, 0.0F, 1.57079633F, 0.0F)); p20.resetPose();
        var p19Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body3/body3/mole/ro/right_armback2", java.util.List.of()));
        var p19 = new net.minecraft.client.model.geom.ModelPart(p19Cubes, java.util.Map.ofEntries(java.util.Map.entry("right_armback", p20)));
        p19.setInitialPose(PartPose.offsetAndRotation(-8.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p19.resetPose();
        var p18Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body3/body3/mole/ro/tail", java.util.List.of()));
        var p18 = new net.minecraft.client.model.geom.ModelPart(p18Cubes, java.util.Map.ofEntries());
        p18.setInitialPose(PartPose.offsetAndRotation(-9.0F, 0.0F, 3.0F, -3.14159265F, 0.0F, 1.57079633F)); p18.resetPose();
        var p17Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body3/body3/mole/ro/right_arm/right_arm2", java.util.List.of()));
        var p17 = new net.minecraft.client.model.geom.ModelPart(p17Cubes, java.util.Map.ofEntries());
        p17.setInitialPose(PartPose.offsetAndRotation(0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p17.resetPose();
        var p16Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body3/body3/mole/ro/right_arm", java.util.List.of()));
        var p16 = new net.minecraft.client.model.geom.ModelPart(p16Cubes, java.util.Map.ofEntries(java.util.Map.entry("right_arm2", p17)));
        p16.setInitialPose(PartPose.offsetAndRotation(-1.0F, -3.0F, 0.0F, 0.0F, -1.57079633F, 0.0F)); p16.resetPose();
        var p15Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body3/body3/mole/ro/head", java.util.List.of()));
        var p15 = new net.minecraft.client.model.geom.ModelPart(p15Cubes, java.util.Map.ofEntries());
        p15.setInitialPose(PartPose.offsetAndRotation(0.0F, -1.0F, 3.0F, 0.0F, -1.57079633F, 0.0F)); p15.resetPose();
        var p14Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body3/body3/mole/ro", java.util.List.of()));
        var p14 = new net.minecraft.client.model.geom.ModelPart(p14Cubes, java.util.Map.ofEntries(java.util.Map.entry("head", p15), java.util.Map.entry("right_arm", p16), java.util.Map.entry("tail", p18), java.util.Map.entry("right_armback2", p19)));
        p14.setInitialPose(PartPose.offsetAndRotation(-3.0F, 0.0F, -4.0F, 0.0F, 1.57079633F, 0.0F)); p14.resetPose();
        var p13Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body3/body3/mole", java.util.List.of()));
        var p13 = new net.minecraft.client.model.geom.ModelPart(p13Cubes, java.util.Map.ofEntries(java.util.Map.entry("ro", p14), java.util.Map.entry("left_armback2", p22), java.util.Map.entry("left_arm", p24)));
        p13.setInitialPose(PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p13.resetPose();
        var p12Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body3/body3", java.util.List.of()));
        var p12 = new net.minecraft.client.model.geom.ModelPart(p12Cubes, java.util.Map.ofEntries(java.util.Map.entry("mole", p13)));
        p12.setInitialPose(PartPose.offsetAndRotation(0.0F, 4.0F, -1.0F, 0.0F, 0.0F, 0.0F)); p12.resetPose();
        var p11Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body2/body2", java.util.List.of()));
        var p11 = new net.minecraft.client.model.geom.ModelPart(p11Cubes, java.util.Map.ofEntries());
        p11.setInitialPose(PartPose.offsetAndRotation(0.0F, 3.0F, 1.5F, 0.0F, 0.0F, 0.0F)); p11.resetPose();
        var p10Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body1/body1", java.util.List.of()));
        var p10 = new net.minecraft.client.model.geom.ModelPart(p10Cubes, java.util.Map.ofEntries());
        p10.setInitialPose(PartPose.offsetAndRotation(0.0F, 2.0F, 3.5F, 0.0F, 0.0F, 0.0F)); p10.resetPose();
        var p9Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_wing3", java.util.List.of()));
        var p9 = new net.minecraft.client.model.geom.ModelPart(p9Cubes, java.util.Map.ofEntries(java.util.Map.entry("wing3", p33)));
        p9.setInitialPose(nativeRoot.getChild("layer2").storePose()); p9.resetPose();
        var p8Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_wing2", java.util.List.of()));
        var p8 = new net.minecraft.client.model.geom.ModelPart(p8Cubes, java.util.Map.ofEntries(java.util.Map.entry("wing2", p32)));
        p8.setInitialPose(nativeRoot.getChild("layer1").storePose()); p8.resetPose();
        var p7Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_wing1", java.util.List.of()));
        var p7 = new net.minecraft.client.model.geom.ModelPart(p7Cubes, java.util.Map.ofEntries(java.util.Map.entry("wing1", p31)));
        p7.setInitialPose(nativeRoot.getChild("layer0").storePose()); p7.resetPose();
        var p6Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body7", java.util.List.of()));
        var p6 = new net.minecraft.client.model.geom.ModelPart(p6Cubes, java.util.Map.ofEntries(java.util.Map.entry("body7", p30)));
        p6.setInitialPose(nativeRoot.getChild("segment6").storePose()); p6.resetPose();
        var p5Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body6", java.util.List.of()));
        var p5 = new net.minecraft.client.model.geom.ModelPart(p5Cubes, java.util.Map.ofEntries(java.util.Map.entry("body6", p29)));
        p5.setInitialPose(nativeRoot.getChild("segment5").storePose()); p5.resetPose();
        var p4Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body5", java.util.List.of()));
        var p4 = new net.minecraft.client.model.geom.ModelPart(p4Cubes, java.util.Map.ofEntries(java.util.Map.entry("body5", p28)));
        p4.setInitialPose(nativeRoot.getChild("segment4").storePose()); p4.resetPose();
        var p3Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body4", java.util.List.of()));
        var p3 = new net.minecraft.client.model.geom.ModelPart(p3Cubes, java.util.Map.ofEntries(java.util.Map.entry("body4", p27)));
        p3.setInitialPose(nativeRoot.getChild("segment3").storePose()); p3.resetPose();
        var p2Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body3", java.util.List.of()));
        var p2 = new net.minecraft.client.model.geom.ModelPart(p2Cubes, java.util.Map.ofEntries(java.util.Map.entry("body3", p12)));
        p2.setInitialPose(nativeRoot.getChild("segment2").storePose()); p2.resetPose();
        var p1Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body2", java.util.List.of()));
        var p1 = new net.minecraft.client.model.geom.ModelPart(p1Cubes, java.util.Map.ofEntries(java.util.Map.entry("body2", p11)));
        p1.setInitialPose(nativeRoot.getChild("segment1").storePose()); p1.resetPose();
        var p0Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body1", java.util.List.of()));
        var p0 = new net.minecraft.client.model.geom.ModelPart(p0Cubes, java.util.Map.ofEntries(java.util.Map.entry("body1", p10)));
        p0.setInitialPose(nativeRoot.getChild("segment0").storePose()); p0.resetPose();
        return new net.minecraft.client.model.geom.ModelPart(java.util.List.of(), java.util.Map.ofEntries(java.util.Map.entry("__cem_host_body1", p0), java.util.Map.entry("__cem_host_body2", p1), java.util.Map.entry("__cem_host_body3", p2), java.util.Map.entry("__cem_host_body4", p3), java.util.Map.entry("__cem_host_body5", p4), java.util.Map.entry("__cem_host_body6", p5), java.util.Map.entry("__cem_host_body7", p6), java.util.Map.entry("__cem_host_wing1", p7), java.util.Map.entry("__cem_host_wing2", p8), java.util.Map.entry("__cem_host_wing3", p9)));
    }
}
