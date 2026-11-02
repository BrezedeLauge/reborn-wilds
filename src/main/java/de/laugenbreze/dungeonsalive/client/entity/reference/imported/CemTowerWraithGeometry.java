/*
 * Generated adaptation of ReCrafted Mobs 11.0 by GillsBills and credited contributors.
 * Authored asset license: CC-BY-NC-SA-4.0; https://creativecommons.org/licenses/by-nc-sa/4.0/
 * Source: https://modrinth.com/resourcepack/recrafted-mobs
 * Model: assets/minecraft/optifine/cem/evoker/evoker8.jem
 * Source SHA256: 22d997e678596324bbfaf7b66253d1a780cf8f5a4c069aca925ad4b14a96b1a9
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

public final class CemTowerWraithGeometry {
    private CemTowerWraithGeometry() {}

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        PartDefinition p0 = root.addOrReplaceChild("__cem_host_head", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p1 = p0.addOrReplaceChild("__cem_host_headwear", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p2 = p0.addOrReplaceChild("__cem_host_nose", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p3 = root.addOrReplaceChild("__cem_host_body", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p4 = root.addOrReplaceChild("__cem_host_arms", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p5 = root.addOrReplaceChild("__cem_host_right_leg", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p6 = root.addOrReplaceChild("__cem_host_left_leg", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p7 = root.addOrReplaceChild("__cem_host_right_arm", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p8 = root.addOrReplaceChild("__cem_host_left_arm", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p9 = p0.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 24.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p10 = p1.addOrReplaceChild("hat", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 24.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p11 = p2.addOrReplaceChild("nose", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 26.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p12 = p3.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 24.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p13 = p12.addOrReplaceChild("wraith", CubeListBuilder.create()
                .texOffs(54, 30).mirror(false)
                .addBox(-1.5F, -18.0F, 3.0F, 1.0F, 12.0F, 1.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(1.0F, -6.0F, -2.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p14 = p13.addOrReplaceChild("face", CubeListBuilder.create()
                .texOffs(0, 0).mirror(false)
                .addBox(-4.0F, -11.0F, -4.0F, 6.0F, 7.0F, 6.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(0, 40).mirror(false)
                .addBox(-5.0F, -12.0F, -4.5F, 8.0F, 12.0F, 8.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(0, 32).mirror(false)
                .addBox(-4.0F, -3.0F, -5.05F, 6.0F, 3.0F, 5.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(0.0F, -14.0F, 3.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p15 = p14.addOrReplaceChild("towerthing", CubeListBuilder.create()
                .texOffs(24, 0).mirror(false)
                .addBox(-5.0F, 0.0F, 0.0F, 8.0F, 2.0F, 9.0F, new CubeDeformation(-0.01F, -0.01F, -0.01F)), PartPose.offsetAndRotation(0.0F, -12.0F, 3.5F, -0.872664626F, 0.0F, 0.0F));
        PartDefinition p16 = p14.addOrReplaceChild("Cape", CubeListBuilder.create()
                .texOffs(32, 37).mirror(false)
                .addBox(-6.0F, 0.0F, -1.0F, 10.0F, 20.0F, 1.0F, new CubeDeformation(-0.01F, -0.01F, -0.01F)), PartPose.offsetAndRotation(0.0F, 0.0F, 2.5F, 0.392699082F, 0.0F, 0.0F));
        PartDefinition p17 = p13.addOrReplaceChild("waist", CubeListBuilder.create()
                .texOffs(18, 14).mirror(false)
                .addBox(-5.0F, -3.0F, 0.5F, 8.0F, 19.0F, 4.0F, new CubeDeformation(-0.01F, -0.01F, -0.01F)), PartPose.offsetAndRotation(0.0F, -14.0F, 0.0F, 0.218166156F, 0.0F, 0.0F));
        PartDefinition p18 = p5.addOrReplaceChild("right_leg", CubeListBuilder.create(), PartPose.offsetAndRotation(2.0F, 12.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p19 = p6.addOrReplaceChild("left_leg", CubeListBuilder.create(), PartPose.offsetAndRotation(-2.0F, 12.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p20 = p7.addOrReplaceChild("right_arm", CubeListBuilder.create(), PartPose.offsetAndRotation(5.0F, 21.0F, 2.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p21 = p20.addOrReplaceChild("rightarm4", CubeListBuilder.create()
                .texOffs(54, 30).mirror(false)
                .addBox(-2.5F, -0.5F, -0.5F, 1.0F, 12.0F, 1.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(2, 14).mirror(false)
                .addBox(-4.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(-4.0F, -22.0F, 0.5F, 0.0F, 0.0F, 0.0F));
        PartDefinition p22 = p8.addOrReplaceChild("left_arm", CubeListBuilder.create(), PartPose.offsetAndRotation(-5.0F, 22.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p23 = p22.addOrReplaceChild("leftarm4", CubeListBuilder.create()
                .texOffs(54, 30).mirror(false)
                .addBox(1.5F, -0.5F, -0.5F, 1.0F, 12.0F, 1.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(42, 14).mirror(false)
                .addBox(0.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(4.0F, -22.0F, 0.5F, 0.0F, 0.0F, 0.0F));
        PartDefinition p24 = p4.addOrReplaceChild("arms", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 22.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p25 = p24.addOrReplaceChild("bone32", CubeListBuilder.create(), PartPose.offsetAndRotation(1.0F, -7.0F, 1.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p26 = p25.addOrReplaceChild("leftarm", CubeListBuilder.create()
                .texOffs(54, 30).mirror(false)
                .addBox(1.5F, -0.5F, -0.5F, 1.0F, 12.0F, 1.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(42, 14).mirror(false)
                .addBox(0.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(3.0F, -16.0F, 0.5F, 0.0F, 0.0F, 0.0F));
        PartDefinition p27 = p25.addOrReplaceChild("rightarm", CubeListBuilder.create()
                .texOffs(54, 30).mirror(false)
                .addBox(-2.5F, -0.5F, -0.5F, 1.0F, 12.0F, 1.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(2, 14).mirror(false)
                .addBox(-4.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(-5.0F, -16.0F, 0.5F, 0.0F, 0.0F, 0.0F));
        return LayerDefinition.create(mesh, 64, 64);
    }

    /** Preserve source expression units; static scales are reapplied after every pose reset. */
    public static ReferenceMobDefinition.Builder configure(ReferenceMobDefinition.Builder builder) {
        builder.cemDriver("evoker");
        builder.bakedRoot(CemTowerWraithGeometry::bakeRoot);
        builder.hostAlias("head", "__cem_host_head");
        builder.hostAlias("headwear", "__cem_host_headwear");
        builder.hostAlias("hat", "__cem_host_headwear");
        builder.hostAlias("nose", "__cem_host_nose");
        builder.hostAlias("body", "__cem_host_body");
        builder.hostAlias("arms", "__cem_host_arms");
        builder.hostAlias("right_leg", "__cem_host_right_leg");
        builder.hostAlias("left_leg", "__cem_host_left_leg");
        builder.hostAlias("right_arm", "__cem_host_right_arm");
        builder.hostAlias("left_arm", "__cem_host_left_arm");
        builder.expression("face.rx", "head.rx");
        builder.expression("face.ry", "head.ry");
        builder.expression("face.rz", "head.rz");
        builder.expression("waist.rx", "torad(12.5) + sin(torad(12.5*3) + time/42) *0.20");
        builder.expression("rightarm.rx", "torad(42.5) + right_arm.rx / 1.5");
        builder.expression("leftarm.rx", "torad(42.5) + left_arm.rx / 1.5");
        return builder;
    }

    /** Complete matching rig; installed cubes stay owned by the Minecraft client. */
    public static net.minecraft.client.model.geom.ModelPart bakeRoot() {
        var nativeRoot = net.minecraft.client.model.IllagerModel.createBodyLayer().bakeRoot();
        var nativeCubes = new java.util.HashMap<String, java.util.List<net.minecraft.client.model.geom.ModelPart.Cube>>();
        nativeRoot.visit(new com.mojang.blaze3d.vertex.PoseStack(),
                (pose, path, index, cube) -> nativeCubes.computeIfAbsent(path, key -> new java.util.ArrayList<>()).add(cube));
        var customCubes = new java.util.HashMap<String, java.util.List<net.minecraft.client.model.geom.ModelPart.Cube>>();
        createBodyLayer().bakeRoot().visit(new com.mojang.blaze3d.vertex.PoseStack(),
                (pose, path, index, cube) -> customCubes.computeIfAbsent(path, key -> new java.util.ArrayList<>()).add(cube));
        var p27Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_arms/arms/bone32/rightarm", java.util.List.of()));
        var p27 = new net.minecraft.client.model.geom.ModelPart(p27Cubes, java.util.Map.ofEntries());
        p27.setInitialPose(PartPose.offsetAndRotation(-5.0F, -16.0F, 0.5F, 0.0F, 0.0F, 0.0F)); p27.resetPose();
        var p26Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_arms/arms/bone32/leftarm", java.util.List.of()));
        var p26 = new net.minecraft.client.model.geom.ModelPart(p26Cubes, java.util.Map.ofEntries());
        p26.setInitialPose(PartPose.offsetAndRotation(3.0F, -16.0F, 0.5F, 0.0F, 0.0F, 0.0F)); p26.resetPose();
        var p25Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_arms/arms/bone32", java.util.List.of()));
        var p25 = new net.minecraft.client.model.geom.ModelPart(p25Cubes, java.util.Map.ofEntries(java.util.Map.entry("leftarm", p26), java.util.Map.entry("rightarm", p27)));
        p25.setInitialPose(PartPose.offsetAndRotation(1.0F, -7.0F, 1.0F, 0.0F, 0.0F, 0.0F)); p25.resetPose();
        var p24Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_arms/arms", java.util.List.of()));
        var p24 = new net.minecraft.client.model.geom.ModelPart(p24Cubes, java.util.Map.ofEntries(java.util.Map.entry("bone32", p25)));
        p24.setInitialPose(PartPose.offsetAndRotation(0.0F, 22.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p24.resetPose();
        var p23Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_left_arm/left_arm/leftarm4", java.util.List.of()));
        var p23 = new net.minecraft.client.model.geom.ModelPart(p23Cubes, java.util.Map.ofEntries());
        p23.setInitialPose(PartPose.offsetAndRotation(4.0F, -22.0F, 0.5F, 0.0F, 0.0F, 0.0F)); p23.resetPose();
        var p22Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_left_arm/left_arm", java.util.List.of()));
        var p22 = new net.minecraft.client.model.geom.ModelPart(p22Cubes, java.util.Map.ofEntries(java.util.Map.entry("leftarm4", p23)));
        p22.setInitialPose(PartPose.offsetAndRotation(-5.0F, 22.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p22.resetPose();
        var p21Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_right_arm/right_arm/rightarm4", java.util.List.of()));
        var p21 = new net.minecraft.client.model.geom.ModelPart(p21Cubes, java.util.Map.ofEntries());
        p21.setInitialPose(PartPose.offsetAndRotation(-4.0F, -22.0F, 0.5F, 0.0F, 0.0F, 0.0F)); p21.resetPose();
        var p20Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_right_arm/right_arm", java.util.List.of()));
        var p20 = new net.minecraft.client.model.geom.ModelPart(p20Cubes, java.util.Map.ofEntries(java.util.Map.entry("rightarm4", p21)));
        p20.setInitialPose(PartPose.offsetAndRotation(5.0F, 21.0F, 2.0F, 0.0F, 0.0F, 0.0F)); p20.resetPose();
        var p19Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_left_leg/left_leg", java.util.List.of()));
        var p19 = new net.minecraft.client.model.geom.ModelPart(p19Cubes, java.util.Map.ofEntries());
        p19.setInitialPose(PartPose.offsetAndRotation(-2.0F, 12.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p19.resetPose();
        var p18Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_right_leg/right_leg", java.util.List.of()));
        var p18 = new net.minecraft.client.model.geom.ModelPart(p18Cubes, java.util.Map.ofEntries());
        p18.setInitialPose(PartPose.offsetAndRotation(2.0F, 12.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p18.resetPose();
        var p17Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/body/wraith/waist", java.util.List.of()));
        var p17 = new net.minecraft.client.model.geom.ModelPart(p17Cubes, java.util.Map.ofEntries());
        p17.setInitialPose(PartPose.offsetAndRotation(0.0F, -14.0F, 0.0F, 0.218166156F, 0.0F, 0.0F)); p17.resetPose();
        var p16Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/body/wraith/face/Cape", java.util.List.of()));
        var p16 = new net.minecraft.client.model.geom.ModelPart(p16Cubes, java.util.Map.ofEntries());
        p16.setInitialPose(PartPose.offsetAndRotation(0.0F, 0.0F, 2.5F, 0.392699082F, 0.0F, 0.0F)); p16.resetPose();
        var p15Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/body/wraith/face/towerthing", java.util.List.of()));
        var p15 = new net.minecraft.client.model.geom.ModelPart(p15Cubes, java.util.Map.ofEntries());
        p15.setInitialPose(PartPose.offsetAndRotation(0.0F, -12.0F, 3.5F, -0.872664626F, 0.0F, 0.0F)); p15.resetPose();
        var p14Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/body/wraith/face", java.util.List.of()));
        var p14 = new net.minecraft.client.model.geom.ModelPart(p14Cubes, java.util.Map.ofEntries(java.util.Map.entry("towerthing", p15), java.util.Map.entry("Cape", p16)));
        p14.setInitialPose(PartPose.offsetAndRotation(0.0F, -14.0F, 3.0F, 0.0F, 0.0F, 0.0F)); p14.resetPose();
        var p13Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/body/wraith", java.util.List.of()));
        var p13 = new net.minecraft.client.model.geom.ModelPart(p13Cubes, java.util.Map.ofEntries(java.util.Map.entry("face", p14), java.util.Map.entry("waist", p17)));
        p13.setInitialPose(PartPose.offsetAndRotation(1.0F, -6.0F, -2.0F, 0.0F, 0.0F, 0.0F)); p13.resetPose();
        var p12Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/body", java.util.List.of()));
        var p12 = new net.minecraft.client.model.geom.ModelPart(p12Cubes, java.util.Map.ofEntries(java.util.Map.entry("wraith", p13)));
        p12.setInitialPose(PartPose.offsetAndRotation(0.0F, 24.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p12.resetPose();
        var p11Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_head/__cem_host_nose/nose", java.util.List.of()));
        var p11 = new net.minecraft.client.model.geom.ModelPart(p11Cubes, java.util.Map.ofEntries());
        p11.setInitialPose(PartPose.offsetAndRotation(0.0F, 26.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p11.resetPose();
        var p10Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_head/__cem_host_headwear/hat", java.util.List.of()));
        var p10 = new net.minecraft.client.model.geom.ModelPart(p10Cubes, java.util.Map.ofEntries());
        p10.setInitialPose(PartPose.offsetAndRotation(0.0F, 24.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p10.resetPose();
        var p9Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_head/head", java.util.List.of()));
        var p9 = new net.minecraft.client.model.geom.ModelPart(p9Cubes, java.util.Map.ofEntries());
        p9.setInitialPose(PartPose.offsetAndRotation(0.0F, 24.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p9.resetPose();
        var p8Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_left_arm", java.util.List.of()));
        var p8 = new net.minecraft.client.model.geom.ModelPart(p8Cubes, java.util.Map.ofEntries(java.util.Map.entry("left_arm", p22)));
        p8.setInitialPose(nativeRoot.getChild("left_arm").storePose()); p8.resetPose();
        var p7Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_right_arm", java.util.List.of()));
        var p7 = new net.minecraft.client.model.geom.ModelPart(p7Cubes, java.util.Map.ofEntries(java.util.Map.entry("right_arm", p20)));
        p7.setInitialPose(nativeRoot.getChild("right_arm").storePose()); p7.resetPose();
        var p6Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_left_leg", java.util.List.of()));
        var p6 = new net.minecraft.client.model.geom.ModelPart(p6Cubes, java.util.Map.ofEntries(java.util.Map.entry("left_leg", p19)));
        p6.setInitialPose(nativeRoot.getChild("left_leg").storePose()); p6.resetPose();
        var p5Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_right_leg", java.util.List.of()));
        var p5 = new net.minecraft.client.model.geom.ModelPart(p5Cubes, java.util.Map.ofEntries(java.util.Map.entry("right_leg", p18)));
        p5.setInitialPose(nativeRoot.getChild("right_leg").storePose()); p5.resetPose();
        var p4Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_arms", java.util.List.of()));
        var p4 = new net.minecraft.client.model.geom.ModelPart(p4Cubes, java.util.Map.ofEntries(java.util.Map.entry("arms", p24)));
        p4.setInitialPose(nativeRoot.getChild("arms").storePose()); p4.resetPose();
        var p3Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body", java.util.List.of()));
        var p3 = new net.minecraft.client.model.geom.ModelPart(p3Cubes, java.util.Map.ofEntries(java.util.Map.entry("body", p12)));
        p3.setInitialPose(nativeRoot.getChild("body").storePose()); p3.resetPose();
        var p2Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_head/__cem_host_nose", java.util.List.of()));
        var p2 = new net.minecraft.client.model.geom.ModelPart(p2Cubes, java.util.Map.ofEntries(java.util.Map.entry("nose", p11)));
        p2.setInitialPose(nativeRoot.getChild("head").getChild("nose").storePose()); p2.resetPose();
        var p1Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_head/__cem_host_headwear", java.util.List.of()));
        var p1 = new net.minecraft.client.model.geom.ModelPart(p1Cubes, java.util.Map.ofEntries(java.util.Map.entry("hat", p10)));
        p1.setInitialPose(nativeRoot.getChild("head").getChild("hat").storePose()); p1.resetPose();
        var p0Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_head", java.util.List.of()));
        var p0 = new net.minecraft.client.model.geom.ModelPart(p0Cubes, java.util.Map.ofEntries(java.util.Map.entry("__cem_host_headwear", p1), java.util.Map.entry("__cem_host_nose", p2), java.util.Map.entry("head", p9)));
        p0.setInitialPose(nativeRoot.getChild("head").storePose()); p0.resetPose();
        return new net.minecraft.client.model.geom.ModelPart(java.util.List.of(), java.util.Map.ofEntries(java.util.Map.entry("__cem_host_head", p0), java.util.Map.entry("__cem_host_body", p3), java.util.Map.entry("__cem_host_arms", p4), java.util.Map.entry("__cem_host_right_leg", p5), java.util.Map.entry("__cem_host_left_leg", p6), java.util.Map.entry("__cem_host_right_arm", p7), java.util.Map.entry("__cem_host_left_arm", p8)));
    }
}
