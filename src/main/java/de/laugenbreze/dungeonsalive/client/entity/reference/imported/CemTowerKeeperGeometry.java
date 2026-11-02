/*
 * Generated adaptation of ReCrafted Mobs 11.0 by GillsBills and credited contributors.
 * Authored asset license: CC-BY-NC-SA-4.0; https://creativecommons.org/licenses/by-nc-sa/4.0/
 * Source: https://modrinth.com/resourcepack/recrafted-mobs
 * Model: assets/minecraft/optifine/cem/wandering_trader/wandering_trader10.jem
 * Source SHA256: 54bcffb321fe1df5e325b21f210d97dfcc9fdf01db11d907dd761b67df3a2500
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

public final class CemTowerKeeperGeometry {
    private CemTowerKeeperGeometry() {}

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
        PartDefinition p11 = p4.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 24.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p12 = p11.addOrReplaceChild("TowerKeeper", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p13 = p12.addOrReplaceChild("bottom", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, -3.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p14 = p13.addOrReplaceChild("body3", CubeListBuilder.create()
                .texOffs(0, 84).mirror(false)
                .addBox(-9.0F, -20.0F, -6.0F, 18.0F, 20.0F, 16.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(28, 46).mirror(false)
                .addBox(-7.0F, -20.0F, -7.0F, 0.0F, 20.0F, 2.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(32, 46).mirror(false)
                .addBox(7.0F, -20.0F, -7.0F, 0.0F, 20.0F, 2.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(0, 53).mirror(false)
                .addBox(-7.0F, -20.0F, -5.0F, 14.0F, 15.0F, 0.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(-16, 68).mirror(false)
                .addBox(-9.0F, -20.0F, -6.0F, 18.0F, 0.0F, 16.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(-16, 68).mirror(false)
                .addBox(-9.0F, -6.0F, -6.0F, 18.0F, 0.0F, 16.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p15 = p13.addOrReplaceChild("right_leg3", CubeListBuilder.create()
                .texOffs(68, 106).mirror(false)
                .addBox(-2.0F, 0.0F, -3.0F, 4.0F, 8.0F, 6.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(-4.0F, -8.0F, 3.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p16 = p13.addOrReplaceChild("left_leg3", CubeListBuilder.create(), PartPose.offsetAndRotation(4.0F, -8.0F, 3.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p17 = p16.addOrReplaceChild("body_sub_22", CubeListBuilder.create()
                .texOffs(68, 106).mirror(true)
                .addBox(-2.0F, 0.0F, -3.0F, 4.0F, 8.0F, 6.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.ZERO);
        PartDefinition p18 = p13.addOrReplaceChild("right_arm", CubeListBuilder.create()
                .texOffs(52, 77).mirror(false)
                .addBox(-3.0F, 0.0F, -1.0F, 3.0F, 15.0F, 8.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(-9.0F, -20.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p19 = p13.addOrReplaceChild("left_arm", CubeListBuilder.create(), PartPose.offsetAndRotation(9.0F, -20.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p20 = p19.addOrReplaceChild("body_sub_25", CubeListBuilder.create()
                .texOffs(52, 77).mirror(true)
                .addBox(0.0F, 0.0F, -1.0F, 3.0F, 15.0F, 8.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.ZERO);
        PartDefinition p21 = p12.addOrReplaceChild("middle", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -15.0F, -3.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p22 = p21.addOrReplaceChild("body4", CubeListBuilder.create()
                .texOffs(36, 26).mirror(false)
                .addBox(-9.0F, -20.0F, -6.0F, 18.0F, 15.0F, 16.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(66, 68).mirror(false)
                .addBox(-7.0F, -20.0F, -7.0F, 0.0F, 15.0F, 2.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(70, 68).mirror(false)
                .addBox(7.0F, -20.0F, -7.0F, 0.0F, 15.0F, 2.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(36, 57).mirror(false)
                .addBox(-7.0F, -20.0F, -5.0F, 14.0F, 15.0F, 0.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(-16, 68).mirror(false)
                .addBox(-9.0F, -20.0F, -6.0F, 18.0F, 0.0F, 16.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(-16, 68).mirror(false)
                .addBox(-9.0F, -5.0F, -6.0F, 18.0F, 0.0F, 16.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p23 = p21.addOrReplaceChild("right_arm3", CubeListBuilder.create()
                .texOffs(52, 77).mirror(false)
                .addBox(-3.0F, 0.0F, -1.0F, 3.0F, 15.0F, 8.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(-9.0F, -20.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p24 = p21.addOrReplaceChild("left_arm3", CubeListBuilder.create(), PartPose.offsetAndRotation(9.0F, -20.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p25 = p24.addOrReplaceChild("body_sub_30", CubeListBuilder.create()
                .texOffs(52, 77).mirror(true)
                .addBox(0.0F, 0.0F, -1.0F, 3.0F, 15.0F, 8.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.ZERO);
        PartDefinition p26 = p12.addOrReplaceChild("top", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -30.0F, -3.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p27 = p26.addOrReplaceChild("body5", CubeListBuilder.create()
                .texOffs(0, 120).mirror(false)
                .addBox(-9.0F, -28.0F, -6.0F, 18.0F, 23.0F, 16.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(0, 0).mirror(false)
                .addBox(-9.0F, -38.0F, -6.0F, 18.0F, 10.0F, 16.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(68, 120).mirror(false)
                .addBox(-8.0F, -37.0F, -4.0F, 14.0F, 7.0F, 14.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(74, 68).mirror(false)
                .addBox(-7.0F, -28.0F, -7.0F, 0.0F, 23.0F, 2.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(78, 68).mirror(false)
                .addBox(7.0F, -28.0F, -7.0F, 0.0F, 23.0F, 2.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(0, 30).mirror(false)
                .addBox(-7.0F, -28.0F, -5.0F, 14.0F, 23.0F, 0.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(64, 57).mirror(false)
                .addBox(-2.0F, -26.0F, -7.0F, 4.0F, 7.0F, 2.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(-16, 68).mirror(false)
                .addBox(-9.0F, -5.0F, -6.0F, 18.0F, 0.0F, 16.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p28 = p27.addOrReplaceChild("hat", CubeListBuilder.create()
                .texOffs(82, 7).mirror(false)
                .addBox(-9.0F, 0.0F, -5.0F, 18.0F, 10.0F, 5.0F, new CubeDeformation(-0.01F, -0.01F, -0.01F)), PartPose.offsetAndRotation(0.0F, -38.0F, 10.0F, 0.785398163F, 0.0F, 0.0F));
        PartDefinition p29 = p26.addOrReplaceChild("right_arm4", CubeListBuilder.create()
                .texOffs(104, 26).mirror(false)
                .addBox(-6.0F, -2.0F, -1.0F, 6.0F, 12.0F, 8.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(90, 24).mirror(false)
                .addBox(-6.0F, 10.0F, -1.0F, 3.0F, 2.0F, 8.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(-9.0F, -28.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p30 = p26.addOrReplaceChild("left_arm4", CubeListBuilder.create(), PartPose.offsetAndRotation(9.0F, -28.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p31 = p30.addOrReplaceChild("body_sub_36", CubeListBuilder.create()
                .texOffs(104, 26).mirror(true)
                .addBox(0.0F, -2.0F, -1.0F, 6.0F, 12.0F, 8.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(90, 24).mirror(true)
                .addBox(3.0F, 10.0F, -1.0F, 3.0F, 2.0F, 8.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.ZERO);
        PartDefinition p32 = p26.addOrReplaceChild("cape2", CubeListBuilder.create()
                .texOffs(88, 57).mirror(false)
                .addBox(-9.0F, 0.0F, -2.0F, 18.0F, 26.0F, 2.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(0.0F, -28.0F, 11.0F, 2.96705973F, 0.0F, 3.14159265F));
        PartDefinition p33 = p32.addOrReplaceChild("cape3", CubeListBuilder.create()
                .texOffs(88, 85).mirror(false)
                .addBox(-9.0F, 0.0F, -2.0F, 18.0F, 16.0F, 2.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(0.0F, 26.0F, 0.0F, -0.0436332313F, 0.0F, 0.0F));
        PartDefinition p34 = p33.addOrReplaceChild("capebottom", CubeListBuilder.create()
                .texOffs(88, 103).mirror(false)
                .addBox(-9.0F, 0.0F, -2.0F, 18.0F, 15.0F, 2.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(0.0F, 16.0F, 0.0F, -0.0436332313F, 0.0F, 0.0F));
        PartDefinition p35 = p6.addOrReplaceChild("arms", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 22.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p36 = p7.addOrReplaceChild("right_leg", CubeListBuilder.create(), PartPose.offsetAndRotation(2.0F, 12.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p37 = p8.addOrReplaceChild("left_leg", CubeListBuilder.create(), PartPose.offsetAndRotation(-2.0F, 12.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p38 = p5.addOrReplaceChild("bodywear", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 24.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p39 = p1.addOrReplaceChild("headwear", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 24.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        return LayerDefinition.create(mesh, 256, 256);
    }

    /** Preserve source expression units; static scales are reapplied after every pose reset. */
    public static ReferenceMobDefinition.Builder configure(ReferenceMobDefinition.Builder builder) {
        builder.cemDriver("wandering_trader");
        builder.bakedRoot(CemTowerKeeperGeometry::bakeRoot);
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
        builder.expression("left_arm.rx", "-right_leg.rx");
        builder.expression("right_arm.rx", "right_leg.rx");
        builder.expression("left_leg3.rx", "right_leg.rx");
        builder.expression("right_leg3.rx", "-right_leg.rx");
        builder.expression("left_arm3.rx", "right_leg.rx");
        builder.expression("right_arm3.rx", "-right_leg.rx");
        builder.expression("left_arm4.rx", "-right_leg.rx");
        builder.expression("right_arm4.rx", "right_leg.rx");
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
        var p39Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_head/__cem_host_headwear/headwear", java.util.List.of()));
        var p39 = new net.minecraft.client.model.geom.ModelPart(p39Cubes, java.util.Map.ofEntries());
        p39.setInitialPose(PartPose.offsetAndRotation(0.0F, 24.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p39.resetPose();
        var p38Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/__cem_host_bodywear/bodywear", java.util.List.of()));
        var p38 = new net.minecraft.client.model.geom.ModelPart(p38Cubes, java.util.Map.ofEntries());
        p38.setInitialPose(PartPose.offsetAndRotation(0.0F, 24.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p38.resetPose();
        var p37Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_left_leg/left_leg", java.util.List.of()));
        var p37 = new net.minecraft.client.model.geom.ModelPart(p37Cubes, java.util.Map.ofEntries());
        p37.setInitialPose(PartPose.offsetAndRotation(-2.0F, 12.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p37.resetPose();
        var p36Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_right_leg/right_leg", java.util.List.of()));
        var p36 = new net.minecraft.client.model.geom.ModelPart(p36Cubes, java.util.Map.ofEntries());
        p36.setInitialPose(PartPose.offsetAndRotation(2.0F, 12.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p36.resetPose();
        var p35Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_arms/arms", java.util.List.of()));
        var p35 = new net.minecraft.client.model.geom.ModelPart(p35Cubes, java.util.Map.ofEntries());
        p35.setInitialPose(PartPose.offsetAndRotation(0.0F, 22.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p35.resetPose();
        var p34Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/body/TowerKeeper/top/cape2/cape3/capebottom", java.util.List.of()));
        var p34 = new net.minecraft.client.model.geom.ModelPart(p34Cubes, java.util.Map.ofEntries());
        p34.setInitialPose(PartPose.offsetAndRotation(0.0F, 16.0F, 0.0F, -0.0436332313F, 0.0F, 0.0F)); p34.resetPose();
        var p33Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/body/TowerKeeper/top/cape2/cape3", java.util.List.of()));
        var p33 = new net.minecraft.client.model.geom.ModelPart(p33Cubes, java.util.Map.ofEntries(java.util.Map.entry("capebottom", p34)));
        p33.setInitialPose(PartPose.offsetAndRotation(0.0F, 26.0F, 0.0F, -0.0436332313F, 0.0F, 0.0F)); p33.resetPose();
        var p32Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/body/TowerKeeper/top/cape2", java.util.List.of()));
        var p32 = new net.minecraft.client.model.geom.ModelPart(p32Cubes, java.util.Map.ofEntries(java.util.Map.entry("cape3", p33)));
        p32.setInitialPose(PartPose.offsetAndRotation(0.0F, -28.0F, 11.0F, 2.96705973F, 0.0F, 3.14159265F)); p32.resetPose();
        var p31Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/body/TowerKeeper/top/left_arm4/body_sub_36", java.util.List.of()));
        var p31 = new net.minecraft.client.model.geom.ModelPart(p31Cubes, java.util.Map.ofEntries());
        p31.setInitialPose(PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p31.resetPose();
        var p30Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/body/TowerKeeper/top/left_arm4", java.util.List.of()));
        var p30 = new net.minecraft.client.model.geom.ModelPart(p30Cubes, java.util.Map.ofEntries(java.util.Map.entry("body_sub_36", p31)));
        p30.setInitialPose(PartPose.offsetAndRotation(9.0F, -28.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p30.resetPose();
        var p29Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/body/TowerKeeper/top/right_arm4", java.util.List.of()));
        var p29 = new net.minecraft.client.model.geom.ModelPart(p29Cubes, java.util.Map.ofEntries());
        p29.setInitialPose(PartPose.offsetAndRotation(-9.0F, -28.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p29.resetPose();
        var p28Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/body/TowerKeeper/top/body5/hat", java.util.List.of()));
        var p28 = new net.minecraft.client.model.geom.ModelPart(p28Cubes, java.util.Map.ofEntries());
        p28.setInitialPose(PartPose.offsetAndRotation(0.0F, -38.0F, 10.0F, 0.785398163F, 0.0F, 0.0F)); p28.resetPose();
        var p27Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/body/TowerKeeper/top/body5", java.util.List.of()));
        var p27 = new net.minecraft.client.model.geom.ModelPart(p27Cubes, java.util.Map.ofEntries(java.util.Map.entry("hat", p28)));
        p27.setInitialPose(PartPose.offsetAndRotation(0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F)); p27.resetPose();
        var p26Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/body/TowerKeeper/top", java.util.List.of()));
        var p26 = new net.minecraft.client.model.geom.ModelPart(p26Cubes, java.util.Map.ofEntries(java.util.Map.entry("body5", p27), java.util.Map.entry("right_arm4", p29), java.util.Map.entry("left_arm4", p30), java.util.Map.entry("cape2", p32)));
        p26.setInitialPose(PartPose.offsetAndRotation(0.0F, -30.0F, -3.0F, 0.0F, 0.0F, 0.0F)); p26.resetPose();
        var p25Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/body/TowerKeeper/middle/left_arm3/body_sub_30", java.util.List.of()));
        var p25 = new net.minecraft.client.model.geom.ModelPart(p25Cubes, java.util.Map.ofEntries());
        p25.setInitialPose(PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p25.resetPose();
        var p24Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/body/TowerKeeper/middle/left_arm3", java.util.List.of()));
        var p24 = new net.minecraft.client.model.geom.ModelPart(p24Cubes, java.util.Map.ofEntries(java.util.Map.entry("body_sub_30", p25)));
        p24.setInitialPose(PartPose.offsetAndRotation(9.0F, -20.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p24.resetPose();
        var p23Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/body/TowerKeeper/middle/right_arm3", java.util.List.of()));
        var p23 = new net.minecraft.client.model.geom.ModelPart(p23Cubes, java.util.Map.ofEntries());
        p23.setInitialPose(PartPose.offsetAndRotation(-9.0F, -20.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p23.resetPose();
        var p22Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/body/TowerKeeper/middle/body4", java.util.List.of()));
        var p22 = new net.minecraft.client.model.geom.ModelPart(p22Cubes, java.util.Map.ofEntries());
        p22.setInitialPose(PartPose.offsetAndRotation(0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F)); p22.resetPose();
        var p21Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/body/TowerKeeper/middle", java.util.List.of()));
        var p21 = new net.minecraft.client.model.geom.ModelPart(p21Cubes, java.util.Map.ofEntries(java.util.Map.entry("body4", p22), java.util.Map.entry("right_arm3", p23), java.util.Map.entry("left_arm3", p24)));
        p21.setInitialPose(PartPose.offsetAndRotation(0.0F, -15.0F, -3.0F, 0.0F, 0.0F, 0.0F)); p21.resetPose();
        var p20Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/body/TowerKeeper/bottom/left_arm/body_sub_25", java.util.List.of()));
        var p20 = new net.minecraft.client.model.geom.ModelPart(p20Cubes, java.util.Map.ofEntries());
        p20.setInitialPose(PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p20.resetPose();
        var p19Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/body/TowerKeeper/bottom/left_arm", java.util.List.of()));
        var p19 = new net.minecraft.client.model.geom.ModelPart(p19Cubes, java.util.Map.ofEntries(java.util.Map.entry("body_sub_25", p20)));
        p19.setInitialPose(PartPose.offsetAndRotation(9.0F, -20.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p19.resetPose();
        var p18Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/body/TowerKeeper/bottom/right_arm", java.util.List.of()));
        var p18 = new net.minecraft.client.model.geom.ModelPart(p18Cubes, java.util.Map.ofEntries());
        p18.setInitialPose(PartPose.offsetAndRotation(-9.0F, -20.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p18.resetPose();
        var p17Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/body/TowerKeeper/bottom/left_leg3/body_sub_22", java.util.List.of()));
        var p17 = new net.minecraft.client.model.geom.ModelPart(p17Cubes, java.util.Map.ofEntries());
        p17.setInitialPose(PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p17.resetPose();
        var p16Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/body/TowerKeeper/bottom/left_leg3", java.util.List.of()));
        var p16 = new net.minecraft.client.model.geom.ModelPart(p16Cubes, java.util.Map.ofEntries(java.util.Map.entry("body_sub_22", p17)));
        p16.setInitialPose(PartPose.offsetAndRotation(4.0F, -8.0F, 3.0F, 0.0F, 0.0F, 0.0F)); p16.resetPose();
        var p15Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/body/TowerKeeper/bottom/right_leg3", java.util.List.of()));
        var p15 = new net.minecraft.client.model.geom.ModelPart(p15Cubes, java.util.Map.ofEntries());
        p15.setInitialPose(PartPose.offsetAndRotation(-4.0F, -8.0F, 3.0F, 0.0F, 0.0F, 0.0F)); p15.resetPose();
        var p14Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/body/TowerKeeper/bottom/body3", java.util.List.of()));
        var p14 = new net.minecraft.client.model.geom.ModelPart(p14Cubes, java.util.Map.ofEntries());
        p14.setInitialPose(PartPose.offsetAndRotation(0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F)); p14.resetPose();
        var p13Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/body/TowerKeeper/bottom", java.util.List.of()));
        var p13 = new net.minecraft.client.model.geom.ModelPart(p13Cubes, java.util.Map.ofEntries(java.util.Map.entry("body3", p14), java.util.Map.entry("right_leg3", p15), java.util.Map.entry("left_leg3", p16), java.util.Map.entry("right_arm", p18), java.util.Map.entry("left_arm", p19)));
        p13.setInitialPose(PartPose.offsetAndRotation(0.0F, 0.0F, -3.0F, 0.0F, 0.0F, 0.0F)); p13.resetPose();
        var p12Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/body/TowerKeeper", java.util.List.of()));
        var p12 = new net.minecraft.client.model.geom.ModelPart(p12Cubes, java.util.Map.ofEntries(java.util.Map.entry("bottom", p13), java.util.Map.entry("middle", p21), java.util.Map.entry("top", p26)));
        p12.setInitialPose(PartPose.offsetAndRotation(0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F)); p12.resetPose();
        var p11Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/body", java.util.List.of()));
        var p11 = new net.minecraft.client.model.geom.ModelPart(p11Cubes, java.util.Map.ofEntries(java.util.Map.entry("TowerKeeper", p12)));
        p11.setInitialPose(PartPose.offsetAndRotation(0.0F, 24.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p11.resetPose();
        var p10Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_head/__cem_host_nose/nose", java.util.List.of()));
        var p10 = new net.minecraft.client.model.geom.ModelPart(p10Cubes, java.util.Map.ofEntries());
        p10.setInitialPose(PartPose.offsetAndRotation(0.0F, 26.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p10.resetPose();
        var p9Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_head/head", java.util.List.of()));
        var p9 = new net.minecraft.client.model.geom.ModelPart(p9Cubes, java.util.Map.ofEntries());
        p9.setInitialPose(PartPose.offsetAndRotation(0.0F, 24.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p9.resetPose();
        var p8Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_left_leg", java.util.List.of()));
        var p8 = new net.minecraft.client.model.geom.ModelPart(p8Cubes, java.util.Map.ofEntries(java.util.Map.entry("left_leg", p37)));
        p8.setInitialPose(nativeRoot.getChild("left_leg").storePose()); p8.resetPose();
        var p7Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_right_leg", java.util.List.of()));
        var p7 = new net.minecraft.client.model.geom.ModelPart(p7Cubes, java.util.Map.ofEntries(java.util.Map.entry("right_leg", p36)));
        p7.setInitialPose(nativeRoot.getChild("right_leg").storePose()); p7.resetPose();
        var p6Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_arms", java.util.List.of()));
        var p6 = new net.minecraft.client.model.geom.ModelPart(p6Cubes, java.util.Map.ofEntries(java.util.Map.entry("arms", p35)));
        p6.setInitialPose(nativeRoot.getChild("arms").storePose()); p6.resetPose();
        var p5Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/__cem_host_bodywear", java.util.List.of()));
        var p5 = new net.minecraft.client.model.geom.ModelPart(p5Cubes, java.util.Map.ofEntries(java.util.Map.entry("bodywear", p38)));
        p5.setInitialPose(nativeRoot.getChild("body").getChild("jacket").storePose()); p5.resetPose();
        var p4Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body", java.util.List.of()));
        var p4 = new net.minecraft.client.model.geom.ModelPart(p4Cubes, java.util.Map.ofEntries(java.util.Map.entry("__cem_host_bodywear", p5), java.util.Map.entry("body", p11)));
        p4.setInitialPose(nativeRoot.getChild("body").storePose()); p4.resetPose();
        var p3Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_head/__cem_host_nose", java.util.List.of()));
        var p3 = new net.minecraft.client.model.geom.ModelPart(p3Cubes, java.util.Map.ofEntries(java.util.Map.entry("nose", p10)));
        p3.setInitialPose(nativeRoot.getChild("head").getChild("nose").storePose()); p3.resetPose();
        var p2Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_head/__cem_host_headwear/__cem_host_headwear2", java.util.List.of()));
        p2Cubes.addAll(nativeCubes.getOrDefault("/head/hat/hat_rim", java.util.List.of()));
        var p2 = new net.minecraft.client.model.geom.ModelPart(p2Cubes, java.util.Map.ofEntries());
        p2.setInitialPose(nativeRoot.getChild("head").getChild("hat").getChild("hat_rim").storePose()); p2.resetPose();
        var p1Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_head/__cem_host_headwear", java.util.List.of()));
        var p1 = new net.minecraft.client.model.geom.ModelPart(p1Cubes, java.util.Map.ofEntries(java.util.Map.entry("__cem_host_headwear2", p2), java.util.Map.entry("headwear", p39)));
        p1.setInitialPose(nativeRoot.getChild("head").getChild("hat").storePose()); p1.resetPose();
        var p0Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_head", java.util.List.of()));
        var p0 = new net.minecraft.client.model.geom.ModelPart(p0Cubes, java.util.Map.ofEntries(java.util.Map.entry("__cem_host_headwear", p1), java.util.Map.entry("__cem_host_nose", p3), java.util.Map.entry("head", p9)));
        p0.setInitialPose(nativeRoot.getChild("head").storePose()); p0.resetPose();
        return new net.minecraft.client.model.geom.ModelPart(java.util.List.of(), java.util.Map.ofEntries(java.util.Map.entry("__cem_host_head", p0), java.util.Map.entry("__cem_host_body", p4), java.util.Map.entry("__cem_host_arms", p6), java.util.Map.entry("__cem_host_right_leg", p7), java.util.Map.entry("__cem_host_left_leg", p8)));
    }
}
