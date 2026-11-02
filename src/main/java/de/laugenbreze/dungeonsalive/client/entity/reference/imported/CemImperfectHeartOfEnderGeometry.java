/*
 * Generated adaptation of ReCrafted Mobs 11.0 by GillsBills and credited contributors.
 * Authored asset license: CC-BY-NC-SA-4.0; https://creativecommons.org/licenses/by-nc-sa/4.0/
 * Source: https://modrinth.com/resourcepack/recrafted-mobs
 * Model: assets/minecraft/optifine/cem/ravager/ravager5.jem
 * Source SHA256: 98ef5997128fd73e59bf3a8650a4736b5af8579cba9fee746939cfef5ae02ed5
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

public final class CemImperfectHeartOfEnderGeometry {
    private CemImperfectHeartOfEnderGeometry() {}

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        PartDefinition p0 = root.addOrReplaceChild("__cem_host_neck", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p1 = p0.addOrReplaceChild("__cem_host_head", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p2 = p1.addOrReplaceChild("__cem_host_jaw", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p3 = root.addOrReplaceChild("__cem_host_body", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p4 = root.addOrReplaceChild("__cem_host_leg1", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p5 = root.addOrReplaceChild("__cem_host_leg2", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p6 = root.addOrReplaceChild("__cem_host_leg3", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p7 = root.addOrReplaceChild("__cem_host_leg4", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p8 = p1.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 14.0F, 10.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p9 = p8.addOrReplaceChild("horns", CubeListBuilder.create(), PartPose.offsetAndRotation(-5.0F, -27.0F, -19.0F, 1.04719755F, 0.0F, 0.0F));
        PartDefinition p10 = p9.addOrReplaceChild("left_horn", CubeListBuilder.create(), PartPose.offsetAndRotation(5.0F, 27.0F, 19.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p11 = p2.addOrReplaceChild("jaw", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 16.0F, 8.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p12 = p3.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 23.0F, -3.5F, 0.0F, 0.0F, 0.0F));
        PartDefinition p13 = p12.addOrReplaceChild("rotation", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -23.0F, 3.5F, 1.57079633F, 0.0F, 0.0F));
        PartDefinition p14 = p13.addOrReplaceChild("Imperfect", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 47.0F, -17.5F, -1.57079633F, 0.0F, 0.0F));
        PartDefinition p15 = p14.addOrReplaceChild("head3", CubeListBuilder.create()
                .texOffs(0, 0).mirror(false)
                .addBox(-15.0F, -7.0F, -21.0F, 26.0F, 15.0F, 21.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(0, 36).mirror(false)
                .addBox(-8.0F, -8.0F, -21.1F, 12.0F, 2.0F, 0.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(2.0F, -10.0F, -68.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p16 = p15.addOrReplaceChild("right_horn", CubeListBuilder.create()
                .texOffs(0, 0).mirror(false)
                .addBox(-26.0F, -27.0F, -9.0F, 9.0F, 9.0F, 0.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(2.0F, 16.0F, -11.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p17 = p15.addOrReplaceChild("left_horn2", CubeListBuilder.create()
                .texOffs(0, 9).mirror(false)
                .addBox(-22.0F, -27.0F, -9.0F, 9.0F, 9.0F, 0.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(33.0F, 16.0F, -11.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p18 = p15.addOrReplaceChild("neck2", CubeListBuilder.create()
                .texOffs(204, 229).mirror(false)
                .addBox(-6.0F, -21.0F, 10.0F, 10.0F, 11.0F, 5.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(-1.0F, 15.0F, -11.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p19 = p14.addOrReplaceChild("body3", CubeListBuilder.create()
                .texOffs(0, 38).mirror(false)
                .addBox(-10.0F, -8.0F, -10.0F, 18.0F, 16.0F, 10.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(1.0F, -10.0F, -55.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p20 = p19.addOrReplaceChild("neck1", CubeListBuilder.create()
                .texOffs(202, 228).mirror(false)
                .addBox(-6.0F, -21.0F, 11.0F, 10.0F, 11.0F, 5.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(0.0F, 15.0F, -12.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p21 = p14.addOrReplaceChild("body4", CubeListBuilder.create()
                .texOffs(0, 64).mirror(false)
                .addBox(-10.0F, -9.0F, -18.0F, 18.0F, 18.0F, 18.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(200, 228).mirror(false)
                .addBox(-8.0F, -7.0F, -16.0F, 14.0F, 14.0F, 14.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(1.0F, -11.0F, -34.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p22 = p21.addOrReplaceChild("front_left_leg", CubeListBuilder.create()
                .texOffs(140, 0).mirror(false)
                .addBox(-3.0F, 0.0F, -5.0F, 11.0F, 20.0F, 12.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(226, 232).mirror(false)
                .addBox(-1.0F, 2.0F, -3.0F, 7.0F, 16.0F, 8.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(13.0F, -3.0F, -10.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p23 = p21.addOrReplaceChild("front_right_leg", CubeListBuilder.create()
                .texOffs(94, 0).mirror(false)
                .addBox(-4.0F, 0.0F, -6.0F, 11.0F, 20.0F, 12.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(-19.0F, -3.0F, -9.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p24 = p21.addOrReplaceChild("neck3", CubeListBuilder.create()
                .texOffs(196, 226).mirror(false)
                .addBox(-6.0F, -20.0F, 8.0F, 10.0F, 10.0F, 5.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(1.0F, 15.0F, -9.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p25 = p14.addOrReplaceChild("body5", CubeListBuilder.create()
                .texOffs(0, 100).mirror(false)
                .addBox(-10.0F, -8.0F, -23.0F, 18.0F, 17.0F, 18.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(0, 136).mirror(false)
                .addBox(-10.0F, -12.0F, -14.0F, 9.0F, 4.0F, 9.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(228, 246).mirror(false)
                .addBox(-9.0F, -11.0F, -13.0F, 7.0F, 3.0F, 7.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(1.0F, -11.0F, -8.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p26 = p25.addOrReplaceChild("neck4", CubeListBuilder.create()
                .texOffs(196, 226).mirror(false)
                .addBox(-6.0F, -20.0F, 12.0F, 10.0F, 10.0F, 5.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(1.0F, 15.0F, -18.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p27 = p14.addOrReplaceChild("body6", CubeListBuilder.create()
                .texOffs(0, 149).mirror(false)
                .addBox(-12.0F, -9.0F, -27.0F, 22.0F, 17.0F, 21.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(170, 218).mirror(false)
                .addBox(-10.0F, -7.0F, -25.0F, 18.0F, 13.0F, 17.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(0, 187).mirror(false)
                .addBox(-12.0F, -15.0F, -27.0F, 11.0F, 6.0F, 21.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(208, 235).mirror(false)
                .addBox(-10.0F, -13.0F, -25.0F, 7.0F, 4.0F, 17.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(1.0F, -10.0F, 17.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p28 = p27.addOrReplaceChild("neck5", CubeListBuilder.create()
                .texOffs(196, 226).mirror(false)
                .addBox(-6.0F, -20.0F, 17.0F, 10.0F, 10.0F, 5.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(1.0F, 14.0F, -24.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p29 = p27.addOrReplaceChild("middle_right_leg", CubeListBuilder.create()
                .texOffs(140, 0).mirror(true)
                .addBox(-5.0F, -1.0F, -5.0F, 11.0F, 20.0F, 12.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(226, 232).mirror(true)
                .addBox(-3.0F, 1.0F, -3.0F, 7.0F, 16.0F, 8.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(-19.0F, -4.0F, -17.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p30 = p27.addOrReplaceChild("middle_left_leg", CubeListBuilder.create()
                .texOffs(94, 32).mirror(false)
                .addBox(-3.0F, -1.0F, -5.0F, 11.0F, 20.0F, 12.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(224, 232).mirror(false)
                .addBox(-1.0F, 1.0F, -3.0F, 8.0F, 16.0F, 8.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(14.0F, -4.0F, -17.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p31 = p14.addOrReplaceChild("body7", CubeListBuilder.create()
                .texOffs(0, 214).mirror(false)
                .addBox(-10.0F, -8.0F, -15.0F, 18.0F, 16.0F, 10.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(2.0F, -11.0F, 29.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p32 = p31.addOrReplaceChild("neck6", CubeListBuilder.create()
                .texOffs(196, 226).mirror(false)
                .addBox(-6.0F, -20.0F, 11.0F, 10.0F, 10.0F, 5.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(0.0F, 15.0F, -17.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p33 = p14.addOrReplaceChild("body8", CubeListBuilder.create()
                .texOffs(72, 64).mirror(false)
                .addBox(-9.0F, -9.0F, -25.0F, 18.0F, 18.0F, 18.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(200, 228).mirror(false)
                .addBox(-7.0F, -7.0F, -23.0F, 14.0F, 14.0F, 14.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(1.0F, -11.0F, 52.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p34 = p33.addOrReplaceChild("neck7", CubeListBuilder.create()
                .texOffs(196, 226).mirror(false)
                .addBox(-6.0F, -20.0F, 10.0F, 10.0F, 10.0F, 5.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(1.0F, 15.0F, -18.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p35 = p14.addOrReplaceChild("body9", CubeListBuilder.create()
                .texOffs(72, 100).mirror(false)
                .addBox(-9.0F, -9.0F, -26.0F, 18.0F, 18.0F, 21.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(190, 223).mirror(false)
                .addBox(-7.0F, -7.0F, -24.0F, 14.0F, 14.0F, 19.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(129, 100).mirror(false)
                .addBox(0.0F, -13.0F, -14.0F, 9.0F, 4.0F, 9.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(228, 245).mirror(false)
                .addBox(1.0F, -12.0F, -13.0F, 7.0F, 3.0F, 7.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(55, 102).mirror(false)
                .addBox(-9.0F, -13.0F, -23.0F, 9.0F, 4.0F, 9.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(1.0F, -11.0F, 74.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p36 = p35.addOrReplaceChild("neck8", CubeListBuilder.create()
                .texOffs(196, 226).mirror(false)
                .addBox(-6.0F, -20.0F, 13.0F, 10.0F, 10.0F, 4.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(1.0F, 15.0F, -18.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p37 = p35.addOrReplaceChild("back_right_leg", CubeListBuilder.create()
                .texOffs(140, 32).mirror(false)
                .addBox(-3.0F, -1.0F, -5.0F, 11.0F, 20.0F, 12.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(226, 232).mirror(false)
                .addBox(-1.0F, 1.0F, -3.0F, 7.0F, 16.0F, 8.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(-19.0F, -3.0F, -19.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p38 = p35.addOrReplaceChild("back_left_leg", CubeListBuilder.create()
                .texOffs(94, 0).mirror(false)
                .addBox(-3.0F, -1.0F, -5.0F, 11.0F, 20.0F, 12.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(14.0F, -3.0F, -19.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p39 = p14.addOrReplaceChild("body10", CubeListBuilder.create()
                .texOffs(86, 139).mirror(false)
                .addBox(-9.0F, -8.0F, -2.0F, 18.0F, 18.0F, 14.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(208, 232).mirror(false)
                .addBox(-7.0F, -6.0F, 0.0F, 14.0F, 14.0F, 10.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(1.0F, -12.0F, 74.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p40 = p4.addOrReplaceChild("leg1", CubeListBuilder.create(), PartPose.offsetAndRotation(8.0F, 37.0F, -21.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p41 = p5.addOrReplaceChild("leg2", CubeListBuilder.create(), PartPose.offsetAndRotation(-8.0F, 37.0F, -21.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p42 = p6.addOrReplaceChild("leg3", CubeListBuilder.create(), PartPose.offsetAndRotation(8.0F, 37.0F, 3.5F, 0.0F, 0.0F, 0.0F));
        PartDefinition p43 = p7.addOrReplaceChild("leg4", CubeListBuilder.create(), PartPose.offsetAndRotation(-8.0F, 37.0F, 3.5F, 0.0F, 0.0F, 0.0F));
        PartDefinition p44 = p0.addOrReplaceChild("neck", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 30.0F, -8.0F, 0.0F, 0.0F, 0.0F));
        return LayerDefinition.create(mesh, 256, 256);
    }

    /** Preserve source expression units; static scales are reapplied after every pose reset. */
    public static ReferenceMobDefinition.Builder configure(ReferenceMobDefinition.Builder builder) {
        builder.cemDriver("ravager");
        builder.bakedRoot(CemImperfectHeartOfEnderGeometry::bakeRoot);
        builder.hostAlias("neck", "__cem_host_neck");
        builder.hostAlias("head", "__cem_host_head");
        builder.hostAlias("jaw", "__cem_host_jaw");
        builder.hostAlias("body", "__cem_host_body");
        builder.hostAlias("leg1", "__cem_host_leg1");
        builder.hostAlias("leg2", "__cem_host_leg2");
        builder.hostAlias("leg3", "__cem_host_leg3");
        builder.hostAlias("leg4", "__cem_host_leg4");
        builder.expression("rotation.rx", "0");
        builder.expression("head3.rx", "head.rx");
        builder.expression("head3.ry", "head.ry");
        builder.expression("head3.rz", "head.rz");
        builder.expression("front_left_leg.rx", "leg1.rx*2");
        builder.expression("front_right_leg.rx", "leg2.rx*2");
        builder.expression("middle_left_leg.rx", "-leg1.rx*2");
        builder.expression("middle_right_leg.rx", "-leg2.rx*2");
        builder.expression("back_left_leg.rx", "leg1.rx*2");
        builder.expression("back_right_leg.rx", "leg2.rx*2");
        return builder;
    }

    /** Complete matching rig; installed cubes stay owned by the Minecraft client. */
    public static net.minecraft.client.model.geom.ModelPart bakeRoot() {
        var nativeRoot = net.minecraft.client.model.RavagerModel.createBodyLayer().bakeRoot();
        var nativeCubes = new java.util.HashMap<String, java.util.List<net.minecraft.client.model.geom.ModelPart.Cube>>();
        nativeRoot.visit(new com.mojang.blaze3d.vertex.PoseStack(),
                (pose, path, index, cube) -> nativeCubes.computeIfAbsent(path, key -> new java.util.ArrayList<>()).add(cube));
        var customCubes = new java.util.HashMap<String, java.util.List<net.minecraft.client.model.geom.ModelPart.Cube>>();
        createBodyLayer().bakeRoot().visit(new com.mojang.blaze3d.vertex.PoseStack(),
                (pose, path, index, cube) -> customCubes.computeIfAbsent(path, key -> new java.util.ArrayList<>()).add(cube));
        var p44Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_neck/neck", java.util.List.of()));
        var p44 = new net.minecraft.client.model.geom.ModelPart(p44Cubes, java.util.Map.ofEntries());
        p44.setInitialPose(PartPose.offsetAndRotation(0.0F, 30.0F, -8.0F, 0.0F, 0.0F, 0.0F)); p44.resetPose();
        var p43Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_leg4/leg4", java.util.List.of()));
        var p43 = new net.minecraft.client.model.geom.ModelPart(p43Cubes, java.util.Map.ofEntries());
        p43.setInitialPose(PartPose.offsetAndRotation(-8.0F, 37.0F, 3.5F, 0.0F, 0.0F, 0.0F)); p43.resetPose();
        var p42Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_leg3/leg3", java.util.List.of()));
        var p42 = new net.minecraft.client.model.geom.ModelPart(p42Cubes, java.util.Map.ofEntries());
        p42.setInitialPose(PartPose.offsetAndRotation(8.0F, 37.0F, 3.5F, 0.0F, 0.0F, 0.0F)); p42.resetPose();
        var p41Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_leg2/leg2", java.util.List.of()));
        var p41 = new net.minecraft.client.model.geom.ModelPart(p41Cubes, java.util.Map.ofEntries());
        p41.setInitialPose(PartPose.offsetAndRotation(-8.0F, 37.0F, -21.0F, 0.0F, 0.0F, 0.0F)); p41.resetPose();
        var p40Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_leg1/leg1", java.util.List.of()));
        var p40 = new net.minecraft.client.model.geom.ModelPart(p40Cubes, java.util.Map.ofEntries());
        p40.setInitialPose(PartPose.offsetAndRotation(8.0F, 37.0F, -21.0F, 0.0F, 0.0F, 0.0F)); p40.resetPose();
        var p39Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/body/rotation/Imperfect/body10", java.util.List.of()));
        var p39 = new net.minecraft.client.model.geom.ModelPart(p39Cubes, java.util.Map.ofEntries());
        p39.setInitialPose(PartPose.offsetAndRotation(1.0F, -12.0F, 74.0F, 0.0F, 0.0F, 0.0F)); p39.resetPose();
        var p38Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/body/rotation/Imperfect/body9/back_left_leg", java.util.List.of()));
        var p38 = new net.minecraft.client.model.geom.ModelPart(p38Cubes, java.util.Map.ofEntries());
        p38.setInitialPose(PartPose.offsetAndRotation(14.0F, -3.0F, -19.0F, 0.0F, 0.0F, 0.0F)); p38.resetPose();
        var p37Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/body/rotation/Imperfect/body9/back_right_leg", java.util.List.of()));
        var p37 = new net.minecraft.client.model.geom.ModelPart(p37Cubes, java.util.Map.ofEntries());
        p37.setInitialPose(PartPose.offsetAndRotation(-19.0F, -3.0F, -19.0F, 0.0F, 0.0F, 0.0F)); p37.resetPose();
        var p36Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/body/rotation/Imperfect/body9/neck8", java.util.List.of()));
        var p36 = new net.minecraft.client.model.geom.ModelPart(p36Cubes, java.util.Map.ofEntries());
        p36.setInitialPose(PartPose.offsetAndRotation(1.0F, 15.0F, -18.0F, 0.0F, 0.0F, 0.0F)); p36.resetPose();
        var p35Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/body/rotation/Imperfect/body9", java.util.List.of()));
        var p35 = new net.minecraft.client.model.geom.ModelPart(p35Cubes, java.util.Map.ofEntries(java.util.Map.entry("neck8", p36), java.util.Map.entry("back_right_leg", p37), java.util.Map.entry("back_left_leg", p38)));
        p35.setInitialPose(PartPose.offsetAndRotation(1.0F, -11.0F, 74.0F, 0.0F, 0.0F, 0.0F)); p35.resetPose();
        var p34Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/body/rotation/Imperfect/body8/neck7", java.util.List.of()));
        var p34 = new net.minecraft.client.model.geom.ModelPart(p34Cubes, java.util.Map.ofEntries());
        p34.setInitialPose(PartPose.offsetAndRotation(1.0F, 15.0F, -18.0F, 0.0F, 0.0F, 0.0F)); p34.resetPose();
        var p33Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/body/rotation/Imperfect/body8", java.util.List.of()));
        var p33 = new net.minecraft.client.model.geom.ModelPart(p33Cubes, java.util.Map.ofEntries(java.util.Map.entry("neck7", p34)));
        p33.setInitialPose(PartPose.offsetAndRotation(1.0F, -11.0F, 52.0F, 0.0F, 0.0F, 0.0F)); p33.resetPose();
        var p32Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/body/rotation/Imperfect/body7/neck6", java.util.List.of()));
        var p32 = new net.minecraft.client.model.geom.ModelPart(p32Cubes, java.util.Map.ofEntries());
        p32.setInitialPose(PartPose.offsetAndRotation(0.0F, 15.0F, -17.0F, 0.0F, 0.0F, 0.0F)); p32.resetPose();
        var p31Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/body/rotation/Imperfect/body7", java.util.List.of()));
        var p31 = new net.minecraft.client.model.geom.ModelPart(p31Cubes, java.util.Map.ofEntries(java.util.Map.entry("neck6", p32)));
        p31.setInitialPose(PartPose.offsetAndRotation(2.0F, -11.0F, 29.0F, 0.0F, 0.0F, 0.0F)); p31.resetPose();
        var p30Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/body/rotation/Imperfect/body6/middle_left_leg", java.util.List.of()));
        var p30 = new net.minecraft.client.model.geom.ModelPart(p30Cubes, java.util.Map.ofEntries());
        p30.setInitialPose(PartPose.offsetAndRotation(14.0F, -4.0F, -17.0F, 0.0F, 0.0F, 0.0F)); p30.resetPose();
        var p29Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/body/rotation/Imperfect/body6/middle_right_leg", java.util.List.of()));
        var p29 = new net.minecraft.client.model.geom.ModelPart(p29Cubes, java.util.Map.ofEntries());
        p29.setInitialPose(PartPose.offsetAndRotation(-19.0F, -4.0F, -17.0F, 0.0F, 0.0F, 0.0F)); p29.resetPose();
        var p28Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/body/rotation/Imperfect/body6/neck5", java.util.List.of()));
        var p28 = new net.minecraft.client.model.geom.ModelPart(p28Cubes, java.util.Map.ofEntries());
        p28.setInitialPose(PartPose.offsetAndRotation(1.0F, 14.0F, -24.0F, 0.0F, 0.0F, 0.0F)); p28.resetPose();
        var p27Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/body/rotation/Imperfect/body6", java.util.List.of()));
        var p27 = new net.minecraft.client.model.geom.ModelPart(p27Cubes, java.util.Map.ofEntries(java.util.Map.entry("neck5", p28), java.util.Map.entry("middle_right_leg", p29), java.util.Map.entry("middle_left_leg", p30)));
        p27.setInitialPose(PartPose.offsetAndRotation(1.0F, -10.0F, 17.0F, 0.0F, 0.0F, 0.0F)); p27.resetPose();
        var p26Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/body/rotation/Imperfect/body5/neck4", java.util.List.of()));
        var p26 = new net.minecraft.client.model.geom.ModelPart(p26Cubes, java.util.Map.ofEntries());
        p26.setInitialPose(PartPose.offsetAndRotation(1.0F, 15.0F, -18.0F, 0.0F, 0.0F, 0.0F)); p26.resetPose();
        var p25Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/body/rotation/Imperfect/body5", java.util.List.of()));
        var p25 = new net.minecraft.client.model.geom.ModelPart(p25Cubes, java.util.Map.ofEntries(java.util.Map.entry("neck4", p26)));
        p25.setInitialPose(PartPose.offsetAndRotation(1.0F, -11.0F, -8.0F, 0.0F, 0.0F, 0.0F)); p25.resetPose();
        var p24Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/body/rotation/Imperfect/body4/neck3", java.util.List.of()));
        var p24 = new net.minecraft.client.model.geom.ModelPart(p24Cubes, java.util.Map.ofEntries());
        p24.setInitialPose(PartPose.offsetAndRotation(1.0F, 15.0F, -9.0F, 0.0F, 0.0F, 0.0F)); p24.resetPose();
        var p23Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/body/rotation/Imperfect/body4/front_right_leg", java.util.List.of()));
        var p23 = new net.minecraft.client.model.geom.ModelPart(p23Cubes, java.util.Map.ofEntries());
        p23.setInitialPose(PartPose.offsetAndRotation(-19.0F, -3.0F, -9.0F, 0.0F, 0.0F, 0.0F)); p23.resetPose();
        var p22Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/body/rotation/Imperfect/body4/front_left_leg", java.util.List.of()));
        var p22 = new net.minecraft.client.model.geom.ModelPart(p22Cubes, java.util.Map.ofEntries());
        p22.setInitialPose(PartPose.offsetAndRotation(13.0F, -3.0F, -10.0F, 0.0F, 0.0F, 0.0F)); p22.resetPose();
        var p21Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/body/rotation/Imperfect/body4", java.util.List.of()));
        var p21 = new net.minecraft.client.model.geom.ModelPart(p21Cubes, java.util.Map.ofEntries(java.util.Map.entry("front_left_leg", p22), java.util.Map.entry("front_right_leg", p23), java.util.Map.entry("neck3", p24)));
        p21.setInitialPose(PartPose.offsetAndRotation(1.0F, -11.0F, -34.0F, 0.0F, 0.0F, 0.0F)); p21.resetPose();
        var p20Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/body/rotation/Imperfect/body3/neck1", java.util.List.of()));
        var p20 = new net.minecraft.client.model.geom.ModelPart(p20Cubes, java.util.Map.ofEntries());
        p20.setInitialPose(PartPose.offsetAndRotation(0.0F, 15.0F, -12.0F, 0.0F, 0.0F, 0.0F)); p20.resetPose();
        var p19Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/body/rotation/Imperfect/body3", java.util.List.of()));
        var p19 = new net.minecraft.client.model.geom.ModelPart(p19Cubes, java.util.Map.ofEntries(java.util.Map.entry("neck1", p20)));
        p19.setInitialPose(PartPose.offsetAndRotation(1.0F, -10.0F, -55.0F, 0.0F, 0.0F, 0.0F)); p19.resetPose();
        var p18Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/body/rotation/Imperfect/head3/neck2", java.util.List.of()));
        var p18 = new net.minecraft.client.model.geom.ModelPart(p18Cubes, java.util.Map.ofEntries());
        p18.setInitialPose(PartPose.offsetAndRotation(-1.0F, 15.0F, -11.0F, 0.0F, 0.0F, 0.0F)); p18.resetPose();
        var p17Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/body/rotation/Imperfect/head3/left_horn2", java.util.List.of()));
        var p17 = new net.minecraft.client.model.geom.ModelPart(p17Cubes, java.util.Map.ofEntries());
        p17.setInitialPose(PartPose.offsetAndRotation(33.0F, 16.0F, -11.0F, 0.0F, 0.0F, 0.0F)); p17.resetPose();
        var p16Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/body/rotation/Imperfect/head3/right_horn", java.util.List.of()));
        var p16 = new net.minecraft.client.model.geom.ModelPart(p16Cubes, java.util.Map.ofEntries());
        p16.setInitialPose(PartPose.offsetAndRotation(2.0F, 16.0F, -11.0F, 0.0F, 0.0F, 0.0F)); p16.resetPose();
        var p15Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/body/rotation/Imperfect/head3", java.util.List.of()));
        var p15 = new net.minecraft.client.model.geom.ModelPart(p15Cubes, java.util.Map.ofEntries(java.util.Map.entry("right_horn", p16), java.util.Map.entry("left_horn2", p17), java.util.Map.entry("neck2", p18)));
        p15.setInitialPose(PartPose.offsetAndRotation(2.0F, -10.0F, -68.0F, 0.0F, 0.0F, 0.0F)); p15.resetPose();
        var p14Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/body/rotation/Imperfect", java.util.List.of()));
        var p14 = new net.minecraft.client.model.geom.ModelPart(p14Cubes, java.util.Map.ofEntries(java.util.Map.entry("head3", p15), java.util.Map.entry("body3", p19), java.util.Map.entry("body4", p21), java.util.Map.entry("body5", p25), java.util.Map.entry("body6", p27), java.util.Map.entry("body7", p31), java.util.Map.entry("body8", p33), java.util.Map.entry("body9", p35), java.util.Map.entry("body10", p39)));
        p14.setInitialPose(PartPose.offsetAndRotation(0.0F, 47.0F, -17.5F, -1.57079633F, 0.0F, 0.0F)); p14.resetPose();
        var p13Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/body/rotation", java.util.List.of()));
        var p13 = new net.minecraft.client.model.geom.ModelPart(p13Cubes, java.util.Map.ofEntries(java.util.Map.entry("Imperfect", p14)));
        p13.setInitialPose(PartPose.offsetAndRotation(0.0F, -23.0F, 3.5F, 1.57079633F, 0.0F, 0.0F)); p13.resetPose();
        var p12Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/body", java.util.List.of()));
        var p12 = new net.minecraft.client.model.geom.ModelPart(p12Cubes, java.util.Map.ofEntries(java.util.Map.entry("rotation", p13)));
        p12.setInitialPose(PartPose.offsetAndRotation(0.0F, 23.0F, -3.5F, 0.0F, 0.0F, 0.0F)); p12.resetPose();
        var p11Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_neck/__cem_host_head/__cem_host_jaw/jaw", java.util.List.of()));
        var p11 = new net.minecraft.client.model.geom.ModelPart(p11Cubes, java.util.Map.ofEntries());
        p11.setInitialPose(PartPose.offsetAndRotation(0.0F, 16.0F, 8.0F, 0.0F, 0.0F, 0.0F)); p11.resetPose();
        var p10Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_neck/__cem_host_head/head/horns/left_horn", java.util.List.of()));
        var p10 = new net.minecraft.client.model.geom.ModelPart(p10Cubes, java.util.Map.ofEntries());
        p10.setInitialPose(PartPose.offsetAndRotation(5.0F, 27.0F, 19.0F, 0.0F, 0.0F, 0.0F)); p10.resetPose();
        var p9Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_neck/__cem_host_head/head/horns", java.util.List.of()));
        var p9 = new net.minecraft.client.model.geom.ModelPart(p9Cubes, java.util.Map.ofEntries(java.util.Map.entry("left_horn", p10)));
        p9.setInitialPose(PartPose.offsetAndRotation(-5.0F, -27.0F, -19.0F, 1.04719755F, 0.0F, 0.0F)); p9.resetPose();
        var p8Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_neck/__cem_host_head/head", java.util.List.of()));
        var p8 = new net.minecraft.client.model.geom.ModelPart(p8Cubes, java.util.Map.ofEntries(java.util.Map.entry("horns", p9)));
        p8.setInitialPose(PartPose.offsetAndRotation(0.0F, 14.0F, 10.0F, 0.0F, 0.0F, 0.0F)); p8.resetPose();
        var p7Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_leg4", java.util.List.of()));
        var p7 = new net.minecraft.client.model.geom.ModelPart(p7Cubes, java.util.Map.ofEntries(java.util.Map.entry("leg4", p43)));
        p7.setInitialPose(nativeRoot.getChild("left_front_leg").storePose()); p7.resetPose();
        var p6Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_leg3", java.util.List.of()));
        var p6 = new net.minecraft.client.model.geom.ModelPart(p6Cubes, java.util.Map.ofEntries(java.util.Map.entry("leg3", p42)));
        p6.setInitialPose(nativeRoot.getChild("right_front_leg").storePose()); p6.resetPose();
        var p5Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_leg2", java.util.List.of()));
        var p5 = new net.minecraft.client.model.geom.ModelPart(p5Cubes, java.util.Map.ofEntries(java.util.Map.entry("leg2", p41)));
        p5.setInitialPose(nativeRoot.getChild("left_hind_leg").storePose()); p5.resetPose();
        var p4Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_leg1", java.util.List.of()));
        var p4 = new net.minecraft.client.model.geom.ModelPart(p4Cubes, java.util.Map.ofEntries(java.util.Map.entry("leg1", p40)));
        p4.setInitialPose(nativeRoot.getChild("right_hind_leg").storePose()); p4.resetPose();
        var p3Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body", java.util.List.of()));
        var p3 = new net.minecraft.client.model.geom.ModelPart(p3Cubes, java.util.Map.ofEntries(java.util.Map.entry("body", p12)));
        p3.setInitialPose(nativeRoot.getChild("body").storePose()); p3.resetPose();
        var p2Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_neck/__cem_host_head/__cem_host_jaw", java.util.List.of()));
        var p2 = new net.minecraft.client.model.geom.ModelPart(p2Cubes, java.util.Map.ofEntries(java.util.Map.entry("jaw", p11)));
        p2.setInitialPose(nativeRoot.getChild("neck").getChild("head").getChild("mouth").storePose()); p2.resetPose();
        var p1Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_neck/__cem_host_head", java.util.List.of()));
        var p1 = new net.minecraft.client.model.geom.ModelPart(p1Cubes, java.util.Map.ofEntries(java.util.Map.entry("__cem_host_jaw", p2), java.util.Map.entry("head", p8)));
        p1.setInitialPose(nativeRoot.getChild("neck").getChild("head").storePose()); p1.resetPose();
        var p0Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_neck", java.util.List.of()));
        var p0 = new net.minecraft.client.model.geom.ModelPart(p0Cubes, java.util.Map.ofEntries(java.util.Map.entry("__cem_host_head", p1), java.util.Map.entry("neck", p44)));
        p0.setInitialPose(nativeRoot.getChild("neck").storePose()); p0.resetPose();
        return new net.minecraft.client.model.geom.ModelPart(java.util.List.of(), java.util.Map.ofEntries(java.util.Map.entry("__cem_host_neck", p0), java.util.Map.entry("__cem_host_body", p3), java.util.Map.entry("__cem_host_leg1", p4), java.util.Map.entry("__cem_host_leg2", p5), java.util.Map.entry("__cem_host_leg3", p6), java.util.Map.entry("__cem_host_leg4", p7)));
    }
}
