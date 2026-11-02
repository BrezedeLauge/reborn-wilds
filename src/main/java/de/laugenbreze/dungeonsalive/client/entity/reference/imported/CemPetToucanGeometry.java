/*
 * Generated adaptation of ReCrafted Mobs 11.0 by GillsBills and credited contributors.
 * Authored asset license: CC-BY-NC-SA-4.0; https://creativecommons.org/licenses/by-nc-sa/4.0/
 * Source: https://modrinth.com/resourcepack/recrafted-mobs
 * Model: assets/minecraft/optifine/cem/parrot/parrot3.jem
 * Source SHA256: fdbf8485cf64ac547da230bb2a57598a64f70bb8a2fb78dd0c599fef0ca89a32
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

public final class CemPetToucanGeometry {
    private CemPetToucanGeometry() {}

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        PartDefinition p0 = root.addOrReplaceChild("__cem_host_head", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p1 = root.addOrReplaceChild("__cem_host_body", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p2 = root.addOrReplaceChild("__cem_host_tail", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p3 = root.addOrReplaceChild("__cem_host_left_wing", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p4 = root.addOrReplaceChild("__cem_host_right_wing", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p5 = root.addOrReplaceChild("__cem_host_left_leg", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p6 = root.addOrReplaceChild("__cem_host_right_leg", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p7 = p2.addOrReplaceChild("tail", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 2.9F, -1.2F, 0.0F, 0.0F, 0.0F));
        PartDefinition p8 = p6.addOrReplaceChild("right_leg", CubeListBuilder.create(), PartPose.offsetAndRotation(1.0F, 2.0F, 1.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p9 = p5.addOrReplaceChild("left_leg", CubeListBuilder.create(), PartPose.offsetAndRotation(-1.0F, 2.0F, 1.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p10 = p4.addOrReplaceChild("right_wing", CubeListBuilder.create(), PartPose.offsetAndRotation(1.5F, 7.1F, 0.8F, 0.0F, 0.0F, 0.0F));
        PartDefinition p11 = p10.addOrReplaceChild("right_wing_rotation2", CubeListBuilder.create(), PartPose.offsetAndRotation(-1.5F, -4.6F, -0.8F, 0.0F, 3.14159265F, 0.0F));
        PartDefinition p12 = p3.addOrReplaceChild("left_wing", CubeListBuilder.create(), PartPose.offsetAndRotation(-1.5F, 7.1F, 0.8F, 0.0F, 0.0F, 0.0F));
        PartDefinition p13 = p12.addOrReplaceChild("left_wing_rotation2", CubeListBuilder.create(), PartPose.offsetAndRotation(1.5F, -4.6F, -0.8F, 0.0F, 3.14159265F, 0.0F));
        PartDefinition p14 = p1.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 7.5F, 1.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p15 = p14.addOrReplaceChild("Toucan", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -8.0F, -2.5F, 0.0F, 0.0F, 0.0F));
        PartDefinition p16 = p15.addOrReplaceChild("head2", CubeListBuilder.create()
                .texOffs(12, 0).mirror(false)
                .addBox(-1.5F, -4.0F, 0.0F, 3.0F, 4.0F, 2.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(0, 0).mirror(false)
                .addBox(-1.5F, -4.0F, -6.0F, 3.0F, 3.0F, 6.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(0, 0).mirror(false)
                .addBox(0.0F, -5.0F, 1.0F, 0.0F, 2.0F, 2.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.5F, 0.0F, 0.0F, 0.0F));
        PartDefinition p17 = p15.addOrReplaceChild("body2", CubeListBuilder.create()
                .texOffs(0, 9).mirror(false)
                .addBox(-2.5F, 0.0F, 0.0F, 5.0F, 6.0F, 3.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.ZERO);
        PartDefinition p18 = p17.addOrReplaceChild("tail2", CubeListBuilder.create()
                .texOffs(21, 19).mirror(false)
                .addBox(-1.5F, -1.0F, -1.0F, 3.0F, 4.0F, 1.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(21, 14).mirror(false)
                .addBox(-2.5F, 3.0F, 0.0F, 4.0F, 5.0F, 0.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(0.0F, 6.0F, 3.0F, 0.305432619F, 0.0F, 0.0F));
        PartDefinition p19 = p15.addOrReplaceChild("right_leg2", CubeListBuilder.create()
                .texOffs(0, 0).mirror(false)
                .addBox(-0.5F, 0.0F, 0.5F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(18, 6).mirror(false)
                .addBox(-3.5F, 2.0F, -1.5F, 2.0F, 0.0F, 3.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(1.0F, 6.0F, 1.5F, 0.0F, 0.0F, 0.0F));
        PartDefinition p20 = p15.addOrReplaceChild("left_leg2", CubeListBuilder.create()
                .texOffs(0, 0).mirror(false)
                .addBox(-0.5F, 0.0F, 0.5F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(-1.0F, 6.0F, 1.5F, 0.0F, 0.0F, 0.0F));
        PartDefinition p21 = p20.addOrReplaceChild("body_sub_6", CubeListBuilder.create()
                .texOffs(18, 6).mirror(true)
                .addBox(1.5F, 2.0F, -1.5F, 2.0F, 0.0F, 3.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.ZERO);
        PartDefinition p22 = p15.addOrReplaceChild("left_arm", CubeListBuilder.create()
                .texOffs(0, 13).mirror(false)
                .addBox(-0.5F, 0.0F, -1.5F, 0.0F, 7.0F, 5.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(16, 6).mirror(false)
                .addBox(-0.5F, 0.0F, -1.5F, 1.0F, 5.0F, 3.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(3.0F, 0.5F, 1.5F, 0.0F, 0.0F, 0.0F));
        PartDefinition p23 = p15.addOrReplaceChild("right_arm", CubeListBuilder.create()
                .texOffs(13, 15).mirror(false)
                .addBox(-0.5F, 0.0F, -1.5F, 1.0F, 5.0F, 3.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(-3.0F, 0.5F, 1.5F, 0.0F, 0.0F, 0.0F));
        PartDefinition p24 = p23.addOrReplaceChild("body_sub_9", CubeListBuilder.create()
                .texOffs(0, 13).mirror(true)
                .addBox(0.5F, 0.0F, -1.5F, 0.0F, 7.0F, 5.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.ZERO);
        PartDefinition p25 = p0.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 8.0F, 0.5F, 0.0F, 0.0F, 0.0F));
        return LayerDefinition.create(mesh, 32, 32);
    }

    /** Preserve source expression units; static scales are reapplied after every pose reset. */
    public static ReferenceMobDefinition.Builder configure(ReferenceMobDefinition.Builder builder) {
        builder.cemDriver("parrot");
        builder.bakedRoot(CemPetToucanGeometry::bakeRoot);
        builder.hostAlias("head", "__cem_host_head");
        builder.hostAlias("body", "__cem_host_body");
        builder.hostAlias("tail", "__cem_host_tail");
        builder.hostAlias("left_wing", "__cem_host_left_wing");
        builder.hostAlias("right_wing", "__cem_host_right_wing");
        builder.hostAlias("left_leg", "__cem_host_left_leg");
        builder.hostAlias("right_leg", "__cem_host_right_leg");
        builder.expression("head2.rx", "head.rx + torad(-27.5)");
        builder.expression("head2.ry", "head.ry");
        builder.expression("head2.rz", "head.rz");
        builder.expression("left_arm.rx", "left_wing.rx");
        builder.expression("right_arm.rx", "right_wing.rx");
        builder.expression("left_arm.ry", "left_wing.ry");
        builder.expression("right_arm.ry", "right_wing.ry");
        builder.expression("left_arm.rz", "left_wing.rz");
        builder.expression("right_arm.rz", "right_wing.rz");
        return builder;
    }

    /** Complete matching rig; installed cubes stay owned by the Minecraft client. */
    public static net.minecraft.client.model.geom.ModelPart bakeRoot() {
        var nativeRoot = net.minecraft.client.model.ParrotModel.createBodyLayer().bakeRoot();
        var nativeCubes = new java.util.HashMap<String, java.util.List<net.minecraft.client.model.geom.ModelPart.Cube>>();
        nativeRoot.visit(new com.mojang.blaze3d.vertex.PoseStack(),
                (pose, path, index, cube) -> nativeCubes.computeIfAbsent(path, key -> new java.util.ArrayList<>()).add(cube));
        var customCubes = new java.util.HashMap<String, java.util.List<net.minecraft.client.model.geom.ModelPart.Cube>>();
        createBodyLayer().bakeRoot().visit(new com.mojang.blaze3d.vertex.PoseStack(),
                (pose, path, index, cube) -> customCubes.computeIfAbsent(path, key -> new java.util.ArrayList<>()).add(cube));
        var p25Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_head/head", java.util.List.of()));
        var p25 = new net.minecraft.client.model.geom.ModelPart(p25Cubes, java.util.Map.ofEntries());
        p25.setInitialPose(PartPose.offsetAndRotation(0.0F, 8.0F, 0.5F, 0.0F, 0.0F, 0.0F)); p25.resetPose();
        var p24Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/body/Toucan/right_arm/body_sub_9", java.util.List.of()));
        var p24 = new net.minecraft.client.model.geom.ModelPart(p24Cubes, java.util.Map.ofEntries());
        p24.setInitialPose(PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p24.resetPose();
        var p23Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/body/Toucan/right_arm", java.util.List.of()));
        var p23 = new net.minecraft.client.model.geom.ModelPart(p23Cubes, java.util.Map.ofEntries(java.util.Map.entry("body_sub_9", p24)));
        p23.setInitialPose(PartPose.offsetAndRotation(-3.0F, 0.5F, 1.5F, 0.0F, 0.0F, 0.0F)); p23.resetPose();
        var p22Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/body/Toucan/left_arm", java.util.List.of()));
        var p22 = new net.minecraft.client.model.geom.ModelPart(p22Cubes, java.util.Map.ofEntries());
        p22.setInitialPose(PartPose.offsetAndRotation(3.0F, 0.5F, 1.5F, 0.0F, 0.0F, 0.0F)); p22.resetPose();
        var p21Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/body/Toucan/left_leg2/body_sub_6", java.util.List.of()));
        var p21 = new net.minecraft.client.model.geom.ModelPart(p21Cubes, java.util.Map.ofEntries());
        p21.setInitialPose(PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p21.resetPose();
        var p20Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/body/Toucan/left_leg2", java.util.List.of()));
        var p20 = new net.minecraft.client.model.geom.ModelPart(p20Cubes, java.util.Map.ofEntries(java.util.Map.entry("body_sub_6", p21)));
        p20.setInitialPose(PartPose.offsetAndRotation(-1.0F, 6.0F, 1.5F, 0.0F, 0.0F, 0.0F)); p20.resetPose();
        var p19Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/body/Toucan/right_leg2", java.util.List.of()));
        var p19 = new net.minecraft.client.model.geom.ModelPart(p19Cubes, java.util.Map.ofEntries());
        p19.setInitialPose(PartPose.offsetAndRotation(1.0F, 6.0F, 1.5F, 0.0F, 0.0F, 0.0F)); p19.resetPose();
        var p18Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/body/Toucan/body2/tail2", java.util.List.of()));
        var p18 = new net.minecraft.client.model.geom.ModelPart(p18Cubes, java.util.Map.ofEntries());
        p18.setInitialPose(PartPose.offsetAndRotation(0.0F, 6.0F, 3.0F, 0.305432619F, 0.0F, 0.0F)); p18.resetPose();
        var p17Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/body/Toucan/body2", java.util.List.of()));
        var p17 = new net.minecraft.client.model.geom.ModelPart(p17Cubes, java.util.Map.ofEntries(java.util.Map.entry("tail2", p18)));
        p17.setInitialPose(PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p17.resetPose();
        var p16Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/body/Toucan/head2", java.util.List.of()));
        var p16 = new net.minecraft.client.model.geom.ModelPart(p16Cubes, java.util.Map.ofEntries());
        p16.setInitialPose(PartPose.offsetAndRotation(0.0F, 0.0F, 0.5F, 0.0F, 0.0F, 0.0F)); p16.resetPose();
        var p15Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/body/Toucan", java.util.List.of()));
        var p15 = new net.minecraft.client.model.geom.ModelPart(p15Cubes, java.util.Map.ofEntries(java.util.Map.entry("head2", p16), java.util.Map.entry("body2", p17), java.util.Map.entry("right_leg2", p19), java.util.Map.entry("left_leg2", p20), java.util.Map.entry("left_arm", p22), java.util.Map.entry("right_arm", p23)));
        p15.setInitialPose(PartPose.offsetAndRotation(0.0F, -8.0F, -2.5F, 0.0F, 0.0F, 0.0F)); p15.resetPose();
        var p14Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/body", java.util.List.of()));
        var p14 = new net.minecraft.client.model.geom.ModelPart(p14Cubes, java.util.Map.ofEntries(java.util.Map.entry("Toucan", p15)));
        p14.setInitialPose(PartPose.offsetAndRotation(0.0F, 7.5F, 1.0F, 0.0F, 0.0F, 0.0F)); p14.resetPose();
        var p13Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_left_wing/left_wing/left_wing_rotation2", java.util.List.of()));
        var p13 = new net.minecraft.client.model.geom.ModelPart(p13Cubes, java.util.Map.ofEntries());
        p13.setInitialPose(PartPose.offsetAndRotation(1.5F, -4.6F, -0.8F, 0.0F, 3.14159265F, 0.0F)); p13.resetPose();
        var p12Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_left_wing/left_wing", java.util.List.of()));
        var p12 = new net.minecraft.client.model.geom.ModelPart(p12Cubes, java.util.Map.ofEntries(java.util.Map.entry("left_wing_rotation2", p13)));
        p12.setInitialPose(PartPose.offsetAndRotation(-1.5F, 7.1F, 0.8F, 0.0F, 0.0F, 0.0F)); p12.resetPose();
        var p11Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_right_wing/right_wing/right_wing_rotation2", java.util.List.of()));
        var p11 = new net.minecraft.client.model.geom.ModelPart(p11Cubes, java.util.Map.ofEntries());
        p11.setInitialPose(PartPose.offsetAndRotation(-1.5F, -4.6F, -0.8F, 0.0F, 3.14159265F, 0.0F)); p11.resetPose();
        var p10Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_right_wing/right_wing", java.util.List.of()));
        var p10 = new net.minecraft.client.model.geom.ModelPart(p10Cubes, java.util.Map.ofEntries(java.util.Map.entry("right_wing_rotation2", p11)));
        p10.setInitialPose(PartPose.offsetAndRotation(1.5F, 7.1F, 0.8F, 0.0F, 0.0F, 0.0F)); p10.resetPose();
        var p9Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_left_leg/left_leg", java.util.List.of()));
        var p9 = new net.minecraft.client.model.geom.ModelPart(p9Cubes, java.util.Map.ofEntries());
        p9.setInitialPose(PartPose.offsetAndRotation(-1.0F, 2.0F, 1.0F, 0.0F, 0.0F, 0.0F)); p9.resetPose();
        var p8Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_right_leg/right_leg", java.util.List.of()));
        var p8 = new net.minecraft.client.model.geom.ModelPart(p8Cubes, java.util.Map.ofEntries());
        p8.setInitialPose(PartPose.offsetAndRotation(1.0F, 2.0F, 1.0F, 0.0F, 0.0F, 0.0F)); p8.resetPose();
        var p7Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_tail/tail", java.util.List.of()));
        var p7 = new net.minecraft.client.model.geom.ModelPart(p7Cubes, java.util.Map.ofEntries());
        p7.setInitialPose(PartPose.offsetAndRotation(0.0F, 2.9F, -1.2F, 0.0F, 0.0F, 0.0F)); p7.resetPose();
        var p6Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_right_leg", java.util.List.of()));
        var p6 = new net.minecraft.client.model.geom.ModelPart(p6Cubes, java.util.Map.ofEntries(java.util.Map.entry("right_leg", p8)));
        p6.setInitialPose(nativeRoot.getChild("right_leg").storePose()); p6.resetPose();
        var p5Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_left_leg", java.util.List.of()));
        var p5 = new net.minecraft.client.model.geom.ModelPart(p5Cubes, java.util.Map.ofEntries(java.util.Map.entry("left_leg", p9)));
        p5.setInitialPose(nativeRoot.getChild("left_leg").storePose()); p5.resetPose();
        var p4Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_right_wing", java.util.List.of()));
        var p4 = new net.minecraft.client.model.geom.ModelPart(p4Cubes, java.util.Map.ofEntries(java.util.Map.entry("right_wing", p10)));
        p4.setInitialPose(nativeRoot.getChild("right_wing").storePose()); p4.resetPose();
        var p3Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_left_wing", java.util.List.of()));
        var p3 = new net.minecraft.client.model.geom.ModelPart(p3Cubes, java.util.Map.ofEntries(java.util.Map.entry("left_wing", p12)));
        p3.setInitialPose(nativeRoot.getChild("left_wing").storePose()); p3.resetPose();
        var p2Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_tail", java.util.List.of()));
        var p2 = new net.minecraft.client.model.geom.ModelPart(p2Cubes, java.util.Map.ofEntries(java.util.Map.entry("tail", p7)));
        p2.setInitialPose(nativeRoot.getChild("tail").storePose()); p2.resetPose();
        var p1Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body", java.util.List.of()));
        var p1 = new net.minecraft.client.model.geom.ModelPart(p1Cubes, java.util.Map.ofEntries(java.util.Map.entry("body", p14)));
        p1.setInitialPose(nativeRoot.getChild("body").storePose()); p1.resetPose();
        var p0Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_head", java.util.List.of()));
        var p0 = new net.minecraft.client.model.geom.ModelPart(p0Cubes, java.util.Map.ofEntries(java.util.Map.entry("head", p25)));
        p0.setInitialPose(nativeRoot.getChild("head").storePose()); p0.resetPose();
        return new net.minecraft.client.model.geom.ModelPart(java.util.List.of(), java.util.Map.ofEntries(java.util.Map.entry("__cem_host_head", p0), java.util.Map.entry("__cem_host_body", p1), java.util.Map.entry("__cem_host_tail", p2), java.util.Map.entry("__cem_host_left_wing", p3), java.util.Map.entry("__cem_host_right_wing", p4), java.util.Map.entry("__cem_host_left_leg", p5), java.util.Map.entry("__cem_host_right_leg", p6)));
    }
}
