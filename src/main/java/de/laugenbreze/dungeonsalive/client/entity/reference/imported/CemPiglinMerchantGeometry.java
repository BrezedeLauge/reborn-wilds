/*
 * Generated adaptation of ReCrafted Mobs 11.0 by GillsBills and credited contributors.
 * Authored asset license: CC-BY-NC-SA-4.0; https://creativecommons.org/licenses/by-nc-sa/4.0/
 * Source: https://modrinth.com/resourcepack/recrafted-mobs
 * Model: assets/minecraft/optifine/cem/wandering_trader/wandering_trader8.jem
 * Source SHA256: 38a5de4b25714f94eefca4bf5622801fd1ee2898b39133e34e725694e26005b4
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

public final class CemPiglinMerchantGeometry {
    private CemPiglinMerchantGeometry() {}

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
        PartDefinition p10 = p1.addOrReplaceChild("headwear", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 24.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p11 = p3.addOrReplaceChild("nose", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 26.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p12 = p4.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 24.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p13 = p12.addOrReplaceChild("merhcant", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -24.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p14 = p13.addOrReplaceChild("merchant", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 33.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p15 = p14.addOrReplaceChild("backpack", CubeListBuilder.create()
                .texOffs(0, 0).mirror(false)
                .addBox(-13.0F, -16.0F, -6.0F, 21.0F, 15.0F, 14.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(39, 66).mirror(false)
                .addBox(-10.0F, -1.0F, -3.5F, 16.0F, 9.0F, 9.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(0, 29).mirror(false)
                .addBox(-14.5F, -23.0F, 1.0F, 24.0F, 7.0F, 7.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(-3.0F, -23.0F, 13.0F, 0.0F, 3.14159265F, 0.0F));
        PartDefinition p16 = p14.addOrReplaceChild("head2", CubeListBuilder.create()
                .texOffs(0, 70).mirror(false)
                .addBox(-5.0F, -3.0F, -8.0F, 10.0F, 8.0F, 8.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(70, 0).mirror(false)
                .addBox(-6.0F, -4.5F, -9.0F, 12.0F, 8.0F, 11.0F, new CubeDeformation(0.01F, 0.01F, 0.01F))
                .texOffs(86, 24).mirror(false)
                .addBox(-2.0F, 1.0F, -9.0F, 4.0F, 4.0F, 1.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(3, 31).mirror(false)
                .addBox(2.0F, 3.0F, -9.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(0, 29).mirror(false)
                .addBox(-3.0F, 3.0F, -9.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(0.0F, -29.0F, -2.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p17 = p16.addOrReplaceChild("wart", CubeListBuilder.create()
                .texOffs(86, 19).mirror(false)
                .addBox(-7.5F, 0.0F, 0.0F, 5.0F, 5.0F, 0.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(33, 65).mirror(false)
                .addBox(-5.0F, 0.0F, -2.5F, 0.0F, 5.0F, 5.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(28, 65).mirror(false)
                .addBox(5.0F, 0.0F, -2.5F, 0.0F, 5.0F, 5.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(46, 84).mirror(false)
                .addBox(2.5F, 0.0F, 0.0F, 5.0F, 5.0F, 0.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(0.0F, 5.0F, -4.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p18 = p16.addOrReplaceChild("left_ear2", CubeListBuilder.create()
                .texOffs(70, 19).mirror(false)
                .addBox(0.25F, 0.0F, -2.0F, 1.0F, 5.0F, 4.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(56, 10).mirror(false)
                .addBox(-0.75F, 3.0F, -1.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(4.75F, -1.0F, -4.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p19 = p16.addOrReplaceChild("right_ear2", CubeListBuilder.create()
                .texOffs(66, 0).mirror(false)
                .addBox(-0.5F, 0.0F, -2.0F, 1.0F, 5.0F, 4.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(-5.5F, -1.0F, -4.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p20 = p14.addOrReplaceChild("body2", CubeListBuilder.create()
                .texOffs(48, 43).mirror(false)
                .addBox(-8.0F, -27.0F, -3.0F, 16.0F, 15.0F, 8.0F, new CubeDeformation(0.1F, 0.1F, 0.1F))
                .texOffs(54, 33).mirror(false)
                .addBox(-8.0F, -19.5F, -3.0F, 16.0F, 0.0F, 8.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(0, 43).mirror(false)
                .addBox(-8.0F, -33.5F, -3.0F, 16.0F, 19.0F, 8.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.ZERO);
        PartDefinition p21 = p20.addOrReplaceChild("bone", CubeListBuilder.create()
                .texOffs(16, 86).mirror(false)
                .addBox(5.0F, -43.5F, -3.0F, 3.0F, 10.0F, 0.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(80, 19).mirror(false)
                .addBox(-8.0F, -43.5F, -3.0F, 3.0F, 10.0F, 0.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(55, 29).mirror(false)
                .addBox(-15.0F, -45.5F, -3.0F, 30.0F, 2.0F, 2.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(0, 70).mirror(false)
                .addBox(-17.0F, -46.5F, -3.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.ZERO);
        PartDefinition p22 = p21.addOrReplaceChild("body_sub_11", CubeListBuilder.create()
                .texOffs(0, 43).mirror(false)
                .addBox(15.0F, -46.5F, -3.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.ZERO);
        PartDefinition p23 = p21.addOrReplaceChild("wart2", CubeListBuilder.create()
                .texOffs(80, 66).mirror(false)
                .addBox(-7.5F, -0.5F, 0.5F, 5.0F, 5.0F, 0.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(56, 0).mirror(false)
                .addBox(6.5F, -0.5F, 0.5F, 5.0F, 5.0F, 0.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(5.0F, -43.0F, -3.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p24 = p23.addOrReplaceChild("body_sub_13", CubeListBuilder.create()
                .texOffs(33, 65).mirror(false)
                .addBox(-5.0F, -0.5F, -2.0F, 0.0F, 5.0F, 5.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(33, 65).mirror(false)
                .addBox(9.0F, -0.5F, -2.0F, 0.0F, 5.0F, 5.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(0, 8).mirror(false)
                .addBox(-21.0F, -0.5F, 0.5F, 6.0F, 6.0F, 0.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(0, -4).mirror(false)
                .addBox(-18.0F, -0.5F, -2.5F, 0.0F, 6.0F, 6.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.ZERO);
        PartDefinition p25 = p14.addOrReplaceChild("left_arm2", CubeListBuilder.create()
                .texOffs(51, 84).mirror(false)
                .addBox(0.0F, -2.5F, -2.0F, 5.0F, 14.0F, 5.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(8.0F, -31.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p26 = p14.addOrReplaceChild("right_arm2", CubeListBuilder.create(), PartPose.offsetAndRotation(-8.0F, -31.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p27 = p26.addOrReplaceChild("body_sub_16", CubeListBuilder.create()
                .texOffs(31, 84).mirror(true)
                .addBox(-5.0F, -2.5F, -2.0F, 5.0F, 14.0F, 5.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.ZERO);
        PartDefinition p28 = p14.addOrReplaceChild("right_leg2", CubeListBuilder.create()
                .texOffs(0, 86).mirror(false)
                .addBox(-2.0F, 0.5F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(-5.0F, -21.5F, 1.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p29 = p14.addOrReplaceChild("left_leg2", CubeListBuilder.create()
                .texOffs(71, 84).mirror(false)
                .addBox(-2.0F, 0.5F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(5.0F, -21.5F, 1.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p30 = p6.addOrReplaceChild("arms", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 22.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p31 = p7.addOrReplaceChild("right_leg", CubeListBuilder.create(), PartPose.offsetAndRotation(2.0F, 12.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p32 = p8.addOrReplaceChild("left_leg", CubeListBuilder.create(), PartPose.offsetAndRotation(-2.0F, 12.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p33 = p5.addOrReplaceChild("bodywear", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 24.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        return LayerDefinition.create(mesh, 128, 128);
    }

    /** Preserve source expression units; static scales are reapplied after every pose reset. */
    public static ReferenceMobDefinition.Builder configure(ReferenceMobDefinition.Builder builder) {
        builder.cemDriver("wandering_trader");
        builder.bakedRoot(CemPiglinMerchantGeometry::bakeRoot);
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
        builder.expression("left_arm2.rx", "-right_leg.rx");
        builder.expression("right_arm2.rx", "right_leg.rx");
        builder.expression("left_leg2.rx", "right_leg.rx");
        builder.expression("right_leg2.rx", "-right_leg.rx");
        builder.expression("left_ear2.rz", "torad(-25) + 0.0625*sin(age/7)");
        builder.expression("right_ear2.rz", "torad(25) + 0.0625*sin(age/7)");
        builder.expression("head2.rx", "head.rx");
        builder.expression("head2.ry", "head.ry");
        builder.expression("head2.rz", "head.rz");
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
        var p33Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/__cem_host_bodywear/bodywear", java.util.List.of()));
        var p33 = new net.minecraft.client.model.geom.ModelPart(p33Cubes, java.util.Map.ofEntries());
        p33.setInitialPose(PartPose.offsetAndRotation(0.0F, 24.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p33.resetPose();
        var p32Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_left_leg/left_leg", java.util.List.of()));
        var p32 = new net.minecraft.client.model.geom.ModelPart(p32Cubes, java.util.Map.ofEntries());
        p32.setInitialPose(PartPose.offsetAndRotation(-2.0F, 12.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p32.resetPose();
        var p31Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_right_leg/right_leg", java.util.List.of()));
        var p31 = new net.minecraft.client.model.geom.ModelPart(p31Cubes, java.util.Map.ofEntries());
        p31.setInitialPose(PartPose.offsetAndRotation(2.0F, 12.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p31.resetPose();
        var p30Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_arms/arms", java.util.List.of()));
        var p30 = new net.minecraft.client.model.geom.ModelPart(p30Cubes, java.util.Map.ofEntries());
        p30.setInitialPose(PartPose.offsetAndRotation(0.0F, 22.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p30.resetPose();
        var p29Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/body/merhcant/merchant/left_leg2", java.util.List.of()));
        var p29 = new net.minecraft.client.model.geom.ModelPart(p29Cubes, java.util.Map.ofEntries());
        p29.setInitialPose(PartPose.offsetAndRotation(5.0F, -21.5F, 1.0F, 0.0F, 0.0F, 0.0F)); p29.resetPose();
        var p28Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/body/merhcant/merchant/right_leg2", java.util.List.of()));
        var p28 = new net.minecraft.client.model.geom.ModelPart(p28Cubes, java.util.Map.ofEntries());
        p28.setInitialPose(PartPose.offsetAndRotation(-5.0F, -21.5F, 1.0F, 0.0F, 0.0F, 0.0F)); p28.resetPose();
        var p27Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/body/merhcant/merchant/right_arm2/body_sub_16", java.util.List.of()));
        var p27 = new net.minecraft.client.model.geom.ModelPart(p27Cubes, java.util.Map.ofEntries());
        p27.setInitialPose(PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p27.resetPose();
        var p26Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/body/merhcant/merchant/right_arm2", java.util.List.of()));
        var p26 = new net.minecraft.client.model.geom.ModelPart(p26Cubes, java.util.Map.ofEntries(java.util.Map.entry("body_sub_16", p27)));
        p26.setInitialPose(PartPose.offsetAndRotation(-8.0F, -31.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p26.resetPose();
        var p25Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/body/merhcant/merchant/left_arm2", java.util.List.of()));
        var p25 = new net.minecraft.client.model.geom.ModelPart(p25Cubes, java.util.Map.ofEntries());
        p25.setInitialPose(PartPose.offsetAndRotation(8.0F, -31.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p25.resetPose();
        var p24Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/body/merhcant/merchant/body2/bone/wart2/body_sub_13", java.util.List.of()));
        var p24 = new net.minecraft.client.model.geom.ModelPart(p24Cubes, java.util.Map.ofEntries());
        p24.setInitialPose(PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p24.resetPose();
        var p23Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/body/merhcant/merchant/body2/bone/wart2", java.util.List.of()));
        var p23 = new net.minecraft.client.model.geom.ModelPart(p23Cubes, java.util.Map.ofEntries(java.util.Map.entry("body_sub_13", p24)));
        p23.setInitialPose(PartPose.offsetAndRotation(5.0F, -43.0F, -3.0F, 0.0F, 0.0F, 0.0F)); p23.resetPose();
        var p22Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/body/merhcant/merchant/body2/bone/body_sub_11", java.util.List.of()));
        var p22 = new net.minecraft.client.model.geom.ModelPart(p22Cubes, java.util.Map.ofEntries());
        p22.setInitialPose(PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p22.resetPose();
        var p21Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/body/merhcant/merchant/body2/bone", java.util.List.of()));
        var p21 = new net.minecraft.client.model.geom.ModelPart(p21Cubes, java.util.Map.ofEntries(java.util.Map.entry("body_sub_11", p22), java.util.Map.entry("wart2", p23)));
        p21.setInitialPose(PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p21.resetPose();
        var p20Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/body/merhcant/merchant/body2", java.util.List.of()));
        var p20 = new net.minecraft.client.model.geom.ModelPart(p20Cubes, java.util.Map.ofEntries(java.util.Map.entry("bone", p21)));
        p20.setInitialPose(PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p20.resetPose();
        var p19Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/body/merhcant/merchant/head2/right_ear2", java.util.List.of()));
        var p19 = new net.minecraft.client.model.geom.ModelPart(p19Cubes, java.util.Map.ofEntries());
        p19.setInitialPose(PartPose.offsetAndRotation(-5.5F, -1.0F, -4.0F, 0.0F, 0.0F, 0.0F)); p19.resetPose();
        var p18Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/body/merhcant/merchant/head2/left_ear2", java.util.List.of()));
        var p18 = new net.minecraft.client.model.geom.ModelPart(p18Cubes, java.util.Map.ofEntries());
        p18.setInitialPose(PartPose.offsetAndRotation(4.75F, -1.0F, -4.0F, 0.0F, 0.0F, 0.0F)); p18.resetPose();
        var p17Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/body/merhcant/merchant/head2/wart", java.util.List.of()));
        var p17 = new net.minecraft.client.model.geom.ModelPart(p17Cubes, java.util.Map.ofEntries());
        p17.setInitialPose(PartPose.offsetAndRotation(0.0F, 5.0F, -4.0F, 0.0F, 0.0F, 0.0F)); p17.resetPose();
        var p16Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/body/merhcant/merchant/head2", java.util.List.of()));
        var p16 = new net.minecraft.client.model.geom.ModelPart(p16Cubes, java.util.Map.ofEntries(java.util.Map.entry("wart", p17), java.util.Map.entry("left_ear2", p18), java.util.Map.entry("right_ear2", p19)));
        p16.setInitialPose(PartPose.offsetAndRotation(0.0F, -29.0F, -2.0F, 0.0F, 0.0F, 0.0F)); p16.resetPose();
        var p15Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/body/merhcant/merchant/backpack", java.util.List.of()));
        var p15 = new net.minecraft.client.model.geom.ModelPart(p15Cubes, java.util.Map.ofEntries());
        p15.setInitialPose(PartPose.offsetAndRotation(-3.0F, -23.0F, 13.0F, 0.0F, 3.14159265F, 0.0F)); p15.resetPose();
        var p14Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/body/merhcant/merchant", java.util.List.of()));
        var p14 = new net.minecraft.client.model.geom.ModelPart(p14Cubes, java.util.Map.ofEntries(java.util.Map.entry("backpack", p15), java.util.Map.entry("head2", p16), java.util.Map.entry("body2", p20), java.util.Map.entry("left_arm2", p25), java.util.Map.entry("right_arm2", p26), java.util.Map.entry("right_leg2", p28), java.util.Map.entry("left_leg2", p29)));
        p14.setInitialPose(PartPose.offsetAndRotation(0.0F, 33.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p14.resetPose();
        var p13Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/body/merhcant", java.util.List.of()));
        var p13 = new net.minecraft.client.model.geom.ModelPart(p13Cubes, java.util.Map.ofEntries(java.util.Map.entry("merchant", p14)));
        p13.setInitialPose(PartPose.offsetAndRotation(0.0F, -24.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p13.resetPose();
        var p12Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/body", java.util.List.of()));
        var p12 = new net.minecraft.client.model.geom.ModelPart(p12Cubes, java.util.Map.ofEntries(java.util.Map.entry("merhcant", p13)));
        p12.setInitialPose(PartPose.offsetAndRotation(0.0F, 24.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p12.resetPose();
        var p11Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_head/__cem_host_nose/nose", java.util.List.of()));
        var p11 = new net.minecraft.client.model.geom.ModelPart(p11Cubes, java.util.Map.ofEntries());
        p11.setInitialPose(PartPose.offsetAndRotation(0.0F, 26.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p11.resetPose();
        var p10Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_head/__cem_host_headwear/headwear", java.util.List.of()));
        var p10 = new net.minecraft.client.model.geom.ModelPart(p10Cubes, java.util.Map.ofEntries());
        p10.setInitialPose(PartPose.offsetAndRotation(0.0F, 24.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p10.resetPose();
        var p9Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_head/head", java.util.List.of()));
        var p9 = new net.minecraft.client.model.geom.ModelPart(p9Cubes, java.util.Map.ofEntries());
        p9.setInitialPose(PartPose.offsetAndRotation(0.0F, 24.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p9.resetPose();
        var p8Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_left_leg", java.util.List.of()));
        var p8 = new net.minecraft.client.model.geom.ModelPart(p8Cubes, java.util.Map.ofEntries(java.util.Map.entry("left_leg", p32)));
        p8.setInitialPose(nativeRoot.getChild("left_leg").storePose()); p8.resetPose();
        var p7Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_right_leg", java.util.List.of()));
        var p7 = new net.minecraft.client.model.geom.ModelPart(p7Cubes, java.util.Map.ofEntries(java.util.Map.entry("right_leg", p31)));
        p7.setInitialPose(nativeRoot.getChild("right_leg").storePose()); p7.resetPose();
        var p6Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_arms", java.util.List.of()));
        var p6 = new net.minecraft.client.model.geom.ModelPart(p6Cubes, java.util.Map.ofEntries(java.util.Map.entry("arms", p30)));
        p6.setInitialPose(nativeRoot.getChild("arms").storePose()); p6.resetPose();
        var p5Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/__cem_host_bodywear", java.util.List.of()));
        var p5 = new net.minecraft.client.model.geom.ModelPart(p5Cubes, java.util.Map.ofEntries(java.util.Map.entry("bodywear", p33)));
        p5.setInitialPose(nativeRoot.getChild("body").getChild("jacket").storePose()); p5.resetPose();
        var p4Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body", java.util.List.of()));
        var p4 = new net.minecraft.client.model.geom.ModelPart(p4Cubes, java.util.Map.ofEntries(java.util.Map.entry("__cem_host_bodywear", p5), java.util.Map.entry("body", p12)));
        p4.setInitialPose(nativeRoot.getChild("body").storePose()); p4.resetPose();
        var p3Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_head/__cem_host_nose", java.util.List.of()));
        var p3 = new net.minecraft.client.model.geom.ModelPart(p3Cubes, java.util.Map.ofEntries(java.util.Map.entry("nose", p11)));
        p3.setInitialPose(nativeRoot.getChild("head").getChild("nose").storePose()); p3.resetPose();
        var p2Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_head/__cem_host_headwear/__cem_host_headwear2", java.util.List.of()));
        p2Cubes.addAll(nativeCubes.getOrDefault("/head/hat/hat_rim", java.util.List.of()));
        var p2 = new net.minecraft.client.model.geom.ModelPart(p2Cubes, java.util.Map.ofEntries());
        p2.setInitialPose(nativeRoot.getChild("head").getChild("hat").getChild("hat_rim").storePose()); p2.resetPose();
        var p1Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_head/__cem_host_headwear", java.util.List.of()));
        var p1 = new net.minecraft.client.model.geom.ModelPart(p1Cubes, java.util.Map.ofEntries(java.util.Map.entry("__cem_host_headwear2", p2), java.util.Map.entry("headwear", p10)));
        p1.setInitialPose(nativeRoot.getChild("head").getChild("hat").storePose()); p1.resetPose();
        var p0Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_head", java.util.List.of()));
        var p0 = new net.minecraft.client.model.geom.ModelPart(p0Cubes, java.util.Map.ofEntries(java.util.Map.entry("__cem_host_headwear", p1), java.util.Map.entry("__cem_host_nose", p3), java.util.Map.entry("head", p9)));
        p0.setInitialPose(nativeRoot.getChild("head").storePose()); p0.resetPose();
        return new net.minecraft.client.model.geom.ModelPart(java.util.List.of(), java.util.Map.ofEntries(java.util.Map.entry("__cem_host_head", p0), java.util.Map.entry("__cem_host_body", p4), java.util.Map.entry("__cem_host_arms", p6), java.util.Map.entry("__cem_host_right_leg", p7), java.util.Map.entry("__cem_host_left_leg", p8)));
    }
}
