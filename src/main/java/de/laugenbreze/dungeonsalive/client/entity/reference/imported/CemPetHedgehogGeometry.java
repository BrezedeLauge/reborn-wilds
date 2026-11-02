/*
 * Generated adaptation of ReCrafted Mobs 11.0 by GillsBills and credited contributors.
 * Authored asset license: CC-BY-NC-SA-4.0; https://creativecommons.org/licenses/by-nc-sa/4.0/
 * Source: https://modrinth.com/resourcepack/recrafted-mobs
 * Model: assets/minecraft/optifine/cem/rabbit/rabbit6.jem
 * Source SHA256: f48186f6a672046b614e3b8f8995dd64d66dadba4fdd422d2825ea4f00d73247
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

public final class CemPetHedgehogGeometry {
    private CemPetHedgehogGeometry() {}

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        PartDefinition p0 = root.addOrReplaceChild("__cem_host_left_foot", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p1 = root.addOrReplaceChild("__cem_host_right_foot", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p2 = root.addOrReplaceChild("__cem_host_left_thigh", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p3 = root.addOrReplaceChild("__cem_host_right_thigh", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p4 = root.addOrReplaceChild("__cem_host_left_arm", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p5 = root.addOrReplaceChild("__cem_host_right_arm", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p6 = root.addOrReplaceChild("__cem_host_body", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p7 = root.addOrReplaceChild("__cem_host_head", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p8 = root.addOrReplaceChild("__cem_host_right_ear", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p9 = root.addOrReplaceChild("__cem_host_left_ear", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p10 = root.addOrReplaceChild("__cem_host_tail", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p11 = root.addOrReplaceChild("__cem_host_nose", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p12 = p7.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 7.5F, 3.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p13 = p11.addOrReplaceChild("nose", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 7.5F, 3.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p14 = p9.addOrReplaceChild("left_ear", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 7.5F, 3.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p15 = p8.addOrReplaceChild("right_ear", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 7.5F, 3.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p16 = p6.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 8.0F, -7.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p17 = p16.addOrReplaceChild("hedgehog", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p18 = p17.addOrReplaceChild("head4", CubeListBuilder.create()
                .texOffs(15, 3).mirror(false)
                .addBox(-3.5F, -7.0F, -2.0F, 2.0F, 2.0F, 0.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(15, 3).mirror(false)
                .addBox(1.5F, -7.0F, -2.0F, 2.0F, 2.0F, 0.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(5, 0).mirror(false)
                .addBox(-1.5F, -3.0F, -4.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(16, 3).mirror(false)
                .addBox(-0.5F, -2.0F, -4.0F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.ZERO);
        PartDefinition p19 = p18.addOrReplaceChild("body4", CubeListBuilder.create()
                .texOffs(0, 2).mirror(false)
                .addBox(-2.5F, -10.0F, -5.0F, 5.0F, 8.0F, 5.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(0.0F, -2.0F, -5.0F, -1.57079633F, 0.0F, 0.0F));
        PartDefinition p20 = p17.addOrReplaceChild("legs", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p21 = p20.addOrReplaceChild("right_leg1", CubeListBuilder.create()
                .texOffs(15, 5).mirror(false)
                .addBox(-0.5F, 0.0F, 0.0F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(-2.0F, -2.0F, -2.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p22 = p20.addOrReplaceChild("left_leg1", CubeListBuilder.create()
                .texOffs(15, 5).mirror(true)
                .addBox(-0.5F, 0.0F, 0.0F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(2.0F, -2.0F, -2.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p23 = p20.addOrReplaceChild("right_leg2", CubeListBuilder.create()
                .texOffs(15, 5).mirror(false)
                .addBox(-0.5F, 0.0F, 0.0F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(-2.0F, -2.0F, 4.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p24 = p20.addOrReplaceChild("left_leg2", CubeListBuilder.create()
                .texOffs(15, 5).mirror(true)
                .addBox(-0.5F, 0.0F, 0.0F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(2.0F, -2.0F, 4.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p25 = p4.addOrReplaceChild("left_arm", CubeListBuilder.create(), PartPose.offsetAndRotation(-3.0F, 7.0F, 3.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p26 = p5.addOrReplaceChild("right_arm", CubeListBuilder.create(), PartPose.offsetAndRotation(3.0F, 7.0F, 3.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p27 = p2.addOrReplaceChild("left_thigh", CubeListBuilder.create(), PartPose.offsetAndRotation(-3.0F, 8.0F, -2.5F, 0.0F, 0.0F, 0.0F));
        PartDefinition p28 = p3.addOrReplaceChild("right_thigh", CubeListBuilder.create(), PartPose.offsetAndRotation(3.0F, 8.0F, -2.5F, 0.0F, 0.0F, 0.0F));
        PartDefinition p29 = p0.addOrReplaceChild("left_foot", CubeListBuilder.create(), PartPose.offsetAndRotation(-3.0F, 9.5F, -4.2F, 0.0F, 0.0F, 0.0F));
        PartDefinition p30 = p1.addOrReplaceChild("right_foot", CubeListBuilder.create(), PartPose.offsetAndRotation(3.0F, 9.5F, -4.2F, 0.0F, 0.0F, 0.0F));
        PartDefinition p31 = p10.addOrReplaceChild("tail", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 6.75F, -6.5F, 0.0F, 0.0F, 0.0F));
        return LayerDefinition.create(mesh, 64, 32);
    }

    /** Preserve source expression units; static scales are reapplied after every pose reset. */
    public static ReferenceMobDefinition.Builder configure(ReferenceMobDefinition.Builder builder) {
        builder.cemDriver("rabbit");
        builder.bakedRoot(CemPetHedgehogGeometry::bakeRoot);
        builder.hostAlias("left_foot", "__cem_host_left_foot");
        builder.hostAlias("right_foot", "__cem_host_right_foot");
        builder.hostAlias("left_thigh", "__cem_host_left_thigh");
        builder.hostAlias("right_thigh", "__cem_host_right_thigh");
        builder.hostAlias("left_arm", "__cem_host_left_arm");
        builder.hostAlias("right_arm", "__cem_host_right_arm");
        builder.hostAlias("body", "__cem_host_body");
        builder.hostAlias("head", "__cem_host_head");
        builder.hostAlias("right_ear", "__cem_host_right_ear");
        builder.hostAlias("left_ear", "__cem_host_left_ear");
        builder.hostAlias("tail", "__cem_host_tail");
        builder.hostAlias("nose", "__cem_host_nose");
        builder.expression("hedgehog.rx", "torad(20)");
        builder.expression("right_leg1.rx", "right_arm.rx");
        builder.expression("left_leg1.rx", "left_arm.rx");
        builder.expression("right_leg2.rx", "right_thigh.rx");
        builder.expression("left_leg2.rx", "left_thigh.rx");
        return builder;
    }

    /** Complete matching rig; installed cubes stay owned by the Minecraft client. */
    public static net.minecraft.client.model.geom.ModelPart bakeRoot() {
        var nativeRoot = net.minecraft.client.model.RabbitModel.createBodyLayer().bakeRoot();
        var nativeCubes = new java.util.HashMap<String, java.util.List<net.minecraft.client.model.geom.ModelPart.Cube>>();
        nativeRoot.visit(new com.mojang.blaze3d.vertex.PoseStack(),
                (pose, path, index, cube) -> nativeCubes.computeIfAbsent(path, key -> new java.util.ArrayList<>()).add(cube));
        var customCubes = new java.util.HashMap<String, java.util.List<net.minecraft.client.model.geom.ModelPart.Cube>>();
        createBodyLayer().bakeRoot().visit(new com.mojang.blaze3d.vertex.PoseStack(),
                (pose, path, index, cube) -> customCubes.computeIfAbsent(path, key -> new java.util.ArrayList<>()).add(cube));
        var p31Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_tail/tail", java.util.List.of()));
        var p31 = new net.minecraft.client.model.geom.ModelPart(p31Cubes, java.util.Map.ofEntries());
        p31.setInitialPose(PartPose.offsetAndRotation(0.0F, 6.75F, -6.5F, 0.0F, 0.0F, 0.0F)); p31.resetPose();
        var p30Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_right_foot/right_foot", java.util.List.of()));
        var p30 = new net.minecraft.client.model.geom.ModelPart(p30Cubes, java.util.Map.ofEntries());
        p30.setInitialPose(PartPose.offsetAndRotation(3.0F, 9.5F, -4.2F, 0.0F, 0.0F, 0.0F)); p30.resetPose();
        var p29Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_left_foot/left_foot", java.util.List.of()));
        var p29 = new net.minecraft.client.model.geom.ModelPart(p29Cubes, java.util.Map.ofEntries());
        p29.setInitialPose(PartPose.offsetAndRotation(-3.0F, 9.5F, -4.2F, 0.0F, 0.0F, 0.0F)); p29.resetPose();
        var p28Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_right_thigh/right_thigh", java.util.List.of()));
        var p28 = new net.minecraft.client.model.geom.ModelPart(p28Cubes, java.util.Map.ofEntries());
        p28.setInitialPose(PartPose.offsetAndRotation(3.0F, 8.0F, -2.5F, 0.0F, 0.0F, 0.0F)); p28.resetPose();
        var p27Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_left_thigh/left_thigh", java.util.List.of()));
        var p27 = new net.minecraft.client.model.geom.ModelPart(p27Cubes, java.util.Map.ofEntries());
        p27.setInitialPose(PartPose.offsetAndRotation(-3.0F, 8.0F, -2.5F, 0.0F, 0.0F, 0.0F)); p27.resetPose();
        var p26Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_right_arm/right_arm", java.util.List.of()));
        var p26 = new net.minecraft.client.model.geom.ModelPart(p26Cubes, java.util.Map.ofEntries());
        p26.setInitialPose(PartPose.offsetAndRotation(3.0F, 7.0F, 3.0F, 0.0F, 0.0F, 0.0F)); p26.resetPose();
        var p25Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_left_arm/left_arm", java.util.List.of()));
        var p25 = new net.minecraft.client.model.geom.ModelPart(p25Cubes, java.util.Map.ofEntries());
        p25.setInitialPose(PartPose.offsetAndRotation(-3.0F, 7.0F, 3.0F, 0.0F, 0.0F, 0.0F)); p25.resetPose();
        var p24Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/body/hedgehog/legs/left_leg2", java.util.List.of()));
        var p24 = new net.minecraft.client.model.geom.ModelPart(p24Cubes, java.util.Map.ofEntries());
        p24.setInitialPose(PartPose.offsetAndRotation(2.0F, -2.0F, 4.0F, 0.0F, 0.0F, 0.0F)); p24.resetPose();
        var p23Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/body/hedgehog/legs/right_leg2", java.util.List.of()));
        var p23 = new net.minecraft.client.model.geom.ModelPart(p23Cubes, java.util.Map.ofEntries());
        p23.setInitialPose(PartPose.offsetAndRotation(-2.0F, -2.0F, 4.0F, 0.0F, 0.0F, 0.0F)); p23.resetPose();
        var p22Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/body/hedgehog/legs/left_leg1", java.util.List.of()));
        var p22 = new net.minecraft.client.model.geom.ModelPart(p22Cubes, java.util.Map.ofEntries());
        p22.setInitialPose(PartPose.offsetAndRotation(2.0F, -2.0F, -2.0F, 0.0F, 0.0F, 0.0F)); p22.resetPose();
        var p21Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/body/hedgehog/legs/right_leg1", java.util.List.of()));
        var p21 = new net.minecraft.client.model.geom.ModelPart(p21Cubes, java.util.Map.ofEntries());
        p21.setInitialPose(PartPose.offsetAndRotation(-2.0F, -2.0F, -2.0F, 0.0F, 0.0F, 0.0F)); p21.resetPose();
        var p20Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/body/hedgehog/legs", java.util.List.of()));
        var p20 = new net.minecraft.client.model.geom.ModelPart(p20Cubes, java.util.Map.ofEntries(java.util.Map.entry("right_leg1", p21), java.util.Map.entry("left_leg1", p22), java.util.Map.entry("right_leg2", p23), java.util.Map.entry("left_leg2", p24)));
        p20.setInitialPose(PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p20.resetPose();
        var p19Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/body/hedgehog/head4/body4", java.util.List.of()));
        var p19 = new net.minecraft.client.model.geom.ModelPart(p19Cubes, java.util.Map.ofEntries());
        p19.setInitialPose(PartPose.offsetAndRotation(0.0F, -2.0F, -5.0F, -1.57079633F, 0.0F, 0.0F)); p19.resetPose();
        var p18Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/body/hedgehog/head4", java.util.List.of()));
        var p18 = new net.minecraft.client.model.geom.ModelPart(p18Cubes, java.util.Map.ofEntries(java.util.Map.entry("body4", p19)));
        p18.setInitialPose(PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p18.resetPose();
        var p17Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/body/hedgehog", java.util.List.of()));
        var p17 = new net.minecraft.client.model.geom.ModelPart(p17Cubes, java.util.Map.ofEntries(java.util.Map.entry("head4", p18), java.util.Map.entry("legs", p20)));
        p17.setInitialPose(PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p17.resetPose();
        var p16Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/body", java.util.List.of()));
        var p16 = new net.minecraft.client.model.geom.ModelPart(p16Cubes, java.util.Map.ofEntries(java.util.Map.entry("hedgehog", p17)));
        p16.setInitialPose(PartPose.offsetAndRotation(0.0F, 8.0F, -7.0F, 0.0F, 0.0F, 0.0F)); p16.resetPose();
        var p15Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_right_ear/right_ear", java.util.List.of()));
        var p15 = new net.minecraft.client.model.geom.ModelPart(p15Cubes, java.util.Map.ofEntries());
        p15.setInitialPose(PartPose.offsetAndRotation(0.0F, 7.5F, 3.0F, 0.0F, 0.0F, 0.0F)); p15.resetPose();
        var p14Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_left_ear/left_ear", java.util.List.of()));
        var p14 = new net.minecraft.client.model.geom.ModelPart(p14Cubes, java.util.Map.ofEntries());
        p14.setInitialPose(PartPose.offsetAndRotation(0.0F, 7.5F, 3.0F, 0.0F, 0.0F, 0.0F)); p14.resetPose();
        var p13Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_nose/nose", java.util.List.of()));
        var p13 = new net.minecraft.client.model.geom.ModelPart(p13Cubes, java.util.Map.ofEntries());
        p13.setInitialPose(PartPose.offsetAndRotation(0.0F, 7.5F, 3.0F, 0.0F, 0.0F, 0.0F)); p13.resetPose();
        var p12Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_head/head", java.util.List.of()));
        var p12 = new net.minecraft.client.model.geom.ModelPart(p12Cubes, java.util.Map.ofEntries());
        p12.setInitialPose(PartPose.offsetAndRotation(0.0F, 7.5F, 3.0F, 0.0F, 0.0F, 0.0F)); p12.resetPose();
        var p11Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_nose", java.util.List.of()));
        var p11 = new net.minecraft.client.model.geom.ModelPart(p11Cubes, java.util.Map.ofEntries(java.util.Map.entry("nose", p13)));
        p11.setInitialPose(nativeRoot.getChild("nose").storePose()); p11.resetPose();
        var p10Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_tail", java.util.List.of()));
        var p10 = new net.minecraft.client.model.geom.ModelPart(p10Cubes, java.util.Map.ofEntries(java.util.Map.entry("tail", p31)));
        p10.setInitialPose(nativeRoot.getChild("tail").storePose()); p10.resetPose();
        var p9Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_left_ear", java.util.List.of()));
        var p9 = new net.minecraft.client.model.geom.ModelPart(p9Cubes, java.util.Map.ofEntries(java.util.Map.entry("left_ear", p14)));
        p9.setInitialPose(nativeRoot.getChild("left_ear").storePose()); p9.resetPose();
        var p8Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_right_ear", java.util.List.of()));
        var p8 = new net.minecraft.client.model.geom.ModelPart(p8Cubes, java.util.Map.ofEntries(java.util.Map.entry("right_ear", p15)));
        p8.setInitialPose(nativeRoot.getChild("right_ear").storePose()); p8.resetPose();
        var p7Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_head", java.util.List.of()));
        var p7 = new net.minecraft.client.model.geom.ModelPart(p7Cubes, java.util.Map.ofEntries(java.util.Map.entry("head", p12)));
        p7.setInitialPose(nativeRoot.getChild("head").storePose()); p7.resetPose();
        var p6Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body", java.util.List.of()));
        var p6 = new net.minecraft.client.model.geom.ModelPart(p6Cubes, java.util.Map.ofEntries(java.util.Map.entry("body", p16)));
        p6.setInitialPose(nativeRoot.getChild("body").storePose()); p6.resetPose();
        var p5Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_right_arm", java.util.List.of()));
        var p5 = new net.minecraft.client.model.geom.ModelPart(p5Cubes, java.util.Map.ofEntries(java.util.Map.entry("right_arm", p26)));
        p5.setInitialPose(nativeRoot.getChild("right_front_leg").storePose()); p5.resetPose();
        var p4Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_left_arm", java.util.List.of()));
        var p4 = new net.minecraft.client.model.geom.ModelPart(p4Cubes, java.util.Map.ofEntries(java.util.Map.entry("left_arm", p25)));
        p4.setInitialPose(nativeRoot.getChild("left_front_leg").storePose()); p4.resetPose();
        var p3Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_right_thigh", java.util.List.of()));
        var p3 = new net.minecraft.client.model.geom.ModelPart(p3Cubes, java.util.Map.ofEntries(java.util.Map.entry("right_thigh", p28)));
        p3.setInitialPose(nativeRoot.getChild("right_haunch").storePose()); p3.resetPose();
        var p2Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_left_thigh", java.util.List.of()));
        var p2 = new net.minecraft.client.model.geom.ModelPart(p2Cubes, java.util.Map.ofEntries(java.util.Map.entry("left_thigh", p27)));
        p2.setInitialPose(nativeRoot.getChild("left_haunch").storePose()); p2.resetPose();
        var p1Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_right_foot", java.util.List.of()));
        var p1 = new net.minecraft.client.model.geom.ModelPart(p1Cubes, java.util.Map.ofEntries(java.util.Map.entry("right_foot", p30)));
        p1.setInitialPose(nativeRoot.getChild("right_hind_foot").storePose()); p1.resetPose();
        var p0Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_left_foot", java.util.List.of()));
        var p0 = new net.minecraft.client.model.geom.ModelPart(p0Cubes, java.util.Map.ofEntries(java.util.Map.entry("left_foot", p29)));
        p0.setInitialPose(nativeRoot.getChild("left_hind_foot").storePose()); p0.resetPose();
        return new net.minecraft.client.model.geom.ModelPart(java.util.List.of(), java.util.Map.ofEntries(java.util.Map.entry("__cem_host_left_foot", p0), java.util.Map.entry("__cem_host_right_foot", p1), java.util.Map.entry("__cem_host_left_thigh", p2), java.util.Map.entry("__cem_host_right_thigh", p3), java.util.Map.entry("__cem_host_left_arm", p4), java.util.Map.entry("__cem_host_right_arm", p5), java.util.Map.entry("__cem_host_body", p6), java.util.Map.entry("__cem_host_head", p7), java.util.Map.entry("__cem_host_right_ear", p8), java.util.Map.entry("__cem_host_left_ear", p9), java.util.Map.entry("__cem_host_tail", p10), java.util.Map.entry("__cem_host_nose", p11)));
    }
}
