/*
 * Generated adaptation of ReCrafted Mobs 11.0 by GillsBills and credited contributors.
 * Authored asset license: CC-BY-NC-SA-4.0; https://creativecommons.org/licenses/by-nc-sa/4.0/
 * Source: https://modrinth.com/resourcepack/recrafted-mobs
 * Model: assets/minecraft/optifine/cem/fox/fox4.jem
 * Source SHA256: 1fc96160bbce48bd8f171762ce7a90ccc66add844925d715ce76de15b976a97a
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

public final class CemPetSkunkGeometry {
    private CemPetSkunkGeometry() {}

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        PartDefinition p0 = root.addOrReplaceChild("__cem_host_head", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p1 = root.addOrReplaceChild("__cem_host_body", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p2 = root.addOrReplaceChild("__cem_host_leg1", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p3 = root.addOrReplaceChild("__cem_host_leg2", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p4 = root.addOrReplaceChild("__cem_host_leg3", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p5 = root.addOrReplaceChild("__cem_host_leg4", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p6 = p1.addOrReplaceChild("__cem_host_tail", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p7 = p0.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offsetAndRotation(1.0F, 7.5F, 3.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p8 = p7.addOrReplaceChild("skunk_head", CubeListBuilder.create()
                .texOffs(0, 0).mirror(false)
                .addBox(-3.0F, -2.5F, -7.0F, 6.0F, 5.0F, 7.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(0, 12).mirror(false)
                .addBox(-2.0F, 0.5F, -9.0F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F, 0.0F, 0.0F))
                .texOffs(0, 5).mirror(false)
                .addBox(-4.0F, -3.5F, -2.0F, 2.0F, 2.0F, 0.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(0.0F, -10.5F, -2.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p9 = p8.addOrReplaceChild("__cem_unnamed_0_0_0", CubeListBuilder.create()
                .texOffs(0, 5).mirror(true)
                .addBox(2.0F, -3.5F, -2.0F, 2.0F, 2.0F, 0.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.ZERO);
        PartDefinition p10 = p8.addOrReplaceChild("closed_eyes", CubeListBuilder.create()
                .texOffs(26, 21).mirror(false)
                .addBox(-3.0F, -11.0F, -10.0F, 6.0F, 1.0F, 0.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(0.0F, 10.5F, 3.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p11 = p8.addOrReplaceChild("open_eyes", CubeListBuilder.create()
                .texOffs(26, 20).mirror(false)
                .addBox(-3.0F, -11.0F, -10.0F, 6.0F, 1.0F, 0.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(0.0F, 10.5F, 3.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p12 = p1.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 16.5F, -3.5F, 0.0F, 0.0F, 0.0F));
        PartDefinition p13 = p12.addOrReplaceChild("body_rotation", CubeListBuilder.create()
                .texOffs(26, 0).mirror(false)
                .addBox(-4.0F, -5.0F, -2.5F, 8.0F, 13.0F, 7.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(0.0F, -7.5F, 3.0F, 1.57079633F, 0.0F, 0.0F));
        PartDefinition p14 = p2.addOrReplaceChild("leg1", CubeListBuilder.create()
                .texOffs(17, 12).mirror(true)
                .addBox(-2.999F, -6.0F, 6.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(5.0F, 6.5F, -7.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p15 = p3.addOrReplaceChild("leg2", CubeListBuilder.create()
                .texOffs(18, 20).mirror(true)
                .addBox(0.999F, -6.0F, 6.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(1.0F, 6.5F, -7.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p16 = p4.addOrReplaceChild("leg3", CubeListBuilder.create()
                .texOffs(18, 20).mirror(false)
                .addBox(-3.001F, -6.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(2.0F, 6.5F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p17 = p5.addOrReplaceChild("leg4", CubeListBuilder.create()
                .texOffs(17, 12).mirror(false)
                .addBox(1.001F, -6.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(-2.0F, 6.5F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p18 = p6.addOrReplaceChild("tail", CubeListBuilder.create(), PartPose.offsetAndRotation(4.0F, 9.0F, -11.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p19 = p18.addOrReplaceChild("tail_rotation", CubeListBuilder.create()
                .texOffs(0, 16).mirror(false)
                .addBox(-2.0F, -2.5F, -1.5F, 4.0F, 11.0F, 5.0F, new CubeDeformation(0.0F, 0.0F, 0.0F)), PartPose.offsetAndRotation(0.0F, -8.5F, 13.5F, 1.57079633F, 0.0F, 0.0F));
        return LayerDefinition.create(mesh, 64, 64);
    }

    /** Preserve source expression units; static scales are reapplied after every pose reset. */
    public static ReferenceMobDefinition.Builder configure(ReferenceMobDefinition.Builder builder) {
        builder.cemDriver("fox");
        builder.bakedRoot(CemPetSkunkGeometry::bakeRoot);
        builder.hostAlias("head", "__cem_host_head");
        builder.hostAlias("body", "__cem_host_body");
        builder.hostAlias("leg1", "__cem_host_leg1");
        builder.hostAlias("leg2", "__cem_host_leg2");
        builder.hostAlias("leg3", "__cem_host_leg3");
        builder.hostAlias("leg4", "__cem_host_leg4");
        builder.hostAlias("tail", "__cem_host_tail");
        builder.expression("head.ty", "if(is_sitting, 9, 12)+if(body.rz==torad(-90), 7, 0)");
        builder.expression("head.tz", "if(is_sitting, 0, -3)");
        builder.expression("open_eyes.visible", "body.rz==torad(0)");
        builder.expression("closed_eyes.visible", "body.rz==torad(-90)");
        builder.expression("skunk_head.tx", "if(body.rz==torad(-90), -1, 0)");
        builder.expression("skunk_head.ty", "if(body.rz==torad(-90), -6, -6)");
        builder.expression("skunk_head.tz", "if(body.rz==torad(-90), -3, -2)");
        builder.expression("leg3.tx", "-2.01");
        builder.expression("leg3.tz", "if(is_sitting, 0, 0)");
        builder.expression("leg4.tx", "2.01");
        builder.expression("leg4.tz", "if(is_sitting, 0, 0)");
        builder.expression("tail.ty", "if(body.rz==torad(-90), 12, 24.5)");
        builder.expression("tail.tz", "if(body.rz==torad(-90), 0.5, 0.5)");
        builder.expression("body_rotation.rx", "0");
        builder.expression("tail_rotation.rx", "0");
        builder.expression("tail.ty", "if(body.rz==torad(-90), 12, 18.5)");
        builder.expression("tail.tz", "if(body.rz==torad(-90), 0.5, -2)");
        return builder;
    }

    /** Complete matching rig; installed cubes stay owned by the Minecraft client. */
    public static net.minecraft.client.model.geom.ModelPart bakeRoot() {
        var nativeRoot = net.minecraft.client.model.FoxModel.createBodyLayer().bakeRoot();
        var nativeCubes = new java.util.HashMap<String, java.util.List<net.minecraft.client.model.geom.ModelPart.Cube>>();
        nativeRoot.visit(new com.mojang.blaze3d.vertex.PoseStack(),
                (pose, path, index, cube) -> nativeCubes.computeIfAbsent(path, key -> new java.util.ArrayList<>()).add(cube));
        var customCubes = new java.util.HashMap<String, java.util.List<net.minecraft.client.model.geom.ModelPart.Cube>>();
        createBodyLayer().bakeRoot().visit(new com.mojang.blaze3d.vertex.PoseStack(),
                (pose, path, index, cube) -> customCubes.computeIfAbsent(path, key -> new java.util.ArrayList<>()).add(cube));
        var p19Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/__cem_host_tail/tail/tail_rotation", java.util.List.of()));
        var p19 = new net.minecraft.client.model.geom.ModelPart(p19Cubes, java.util.Map.ofEntries());
        p19.setInitialPose(PartPose.offsetAndRotation(0.0F, -8.5F, 13.5F, 1.57079633F, 0.0F, 0.0F)); p19.resetPose();
        var p18Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/__cem_host_tail/tail", java.util.List.of()));
        var p18 = new net.minecraft.client.model.geom.ModelPart(p18Cubes, java.util.Map.ofEntries(java.util.Map.entry("tail_rotation", p19)));
        p18.setInitialPose(PartPose.offsetAndRotation(4.0F, 9.0F, -11.0F, 0.0F, 0.0F, 0.0F)); p18.resetPose();
        var p17Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_leg4/leg4", java.util.List.of()));
        var p17 = new net.minecraft.client.model.geom.ModelPart(p17Cubes, java.util.Map.ofEntries());
        p17.setInitialPose(PartPose.offsetAndRotation(-2.0F, 6.5F, 0.0F, 0.0F, 0.0F, 0.0F)); p17.resetPose();
        var p16Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_leg3/leg3", java.util.List.of()));
        var p16 = new net.minecraft.client.model.geom.ModelPart(p16Cubes, java.util.Map.ofEntries());
        p16.setInitialPose(PartPose.offsetAndRotation(2.0F, 6.5F, 0.0F, 0.0F, 0.0F, 0.0F)); p16.resetPose();
        var p15Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_leg2/leg2", java.util.List.of()));
        var p15 = new net.minecraft.client.model.geom.ModelPart(p15Cubes, java.util.Map.ofEntries());
        p15.setInitialPose(PartPose.offsetAndRotation(1.0F, 6.5F, -7.0F, 0.0F, 0.0F, 0.0F)); p15.resetPose();
        var p14Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_leg1/leg1", java.util.List.of()));
        var p14 = new net.minecraft.client.model.geom.ModelPart(p14Cubes, java.util.Map.ofEntries());
        p14.setInitialPose(PartPose.offsetAndRotation(5.0F, 6.5F, -7.0F, 0.0F, 0.0F, 0.0F)); p14.resetPose();
        var p13Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/body/body_rotation", java.util.List.of()));
        var p13 = new net.minecraft.client.model.geom.ModelPart(p13Cubes, java.util.Map.ofEntries());
        p13.setInitialPose(PartPose.offsetAndRotation(0.0F, -7.5F, 3.0F, 1.57079633F, 0.0F, 0.0F)); p13.resetPose();
        var p12Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/body", java.util.List.of()));
        var p12 = new net.minecraft.client.model.geom.ModelPart(p12Cubes, java.util.Map.ofEntries(java.util.Map.entry("body_rotation", p13)));
        p12.setInitialPose(PartPose.offsetAndRotation(0.0F, 16.5F, -3.5F, 0.0F, 0.0F, 0.0F)); p12.resetPose();
        var p11Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_head/head/skunk_head/open_eyes", java.util.List.of()));
        var p11 = new net.minecraft.client.model.geom.ModelPart(p11Cubes, java.util.Map.ofEntries());
        p11.setInitialPose(PartPose.offsetAndRotation(0.0F, 10.5F, 3.0F, 0.0F, 0.0F, 0.0F)); p11.resetPose();
        var p10Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_head/head/skunk_head/closed_eyes", java.util.List.of()));
        var p10 = new net.minecraft.client.model.geom.ModelPart(p10Cubes, java.util.Map.ofEntries());
        p10.setInitialPose(PartPose.offsetAndRotation(0.0F, 10.5F, 3.0F, 0.0F, 0.0F, 0.0F)); p10.resetPose();
        var p9Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_head/head/skunk_head/__cem_unnamed_0_0_0", java.util.List.of()));
        var p9 = new net.minecraft.client.model.geom.ModelPart(p9Cubes, java.util.Map.ofEntries());
        p9.setInitialPose(PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p9.resetPose();
        var p8Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_head/head/skunk_head", java.util.List.of()));
        var p8 = new net.minecraft.client.model.geom.ModelPart(p8Cubes, java.util.Map.ofEntries(java.util.Map.entry("__cem_unnamed_0_0_0", p9), java.util.Map.entry("closed_eyes", p10), java.util.Map.entry("open_eyes", p11)));
        p8.setInitialPose(PartPose.offsetAndRotation(0.0F, -10.5F, -2.0F, 0.0F, 0.0F, 0.0F)); p8.resetPose();
        var p7Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_head/head", java.util.List.of()));
        var p7 = new net.minecraft.client.model.geom.ModelPart(p7Cubes, java.util.Map.ofEntries(java.util.Map.entry("skunk_head", p8)));
        p7.setInitialPose(PartPose.offsetAndRotation(1.0F, 7.5F, 3.0F, 0.0F, 0.0F, 0.0F)); p7.resetPose();
        var p6Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/__cem_host_tail", java.util.List.of()));
        var p6 = new net.minecraft.client.model.geom.ModelPart(p6Cubes, java.util.Map.ofEntries(java.util.Map.entry("tail", p18)));
        p6.setInitialPose(nativeRoot.getChild("body").getChild("tail").storePose()); p6.resetPose();
        var p5Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_leg4", java.util.List.of()));
        var p5 = new net.minecraft.client.model.geom.ModelPart(p5Cubes, java.util.Map.ofEntries(java.util.Map.entry("leg4", p17)));
        p5.setInitialPose(nativeRoot.getChild("left_front_leg").storePose()); p5.resetPose();
        var p4Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_leg3", java.util.List.of()));
        var p4 = new net.minecraft.client.model.geom.ModelPart(p4Cubes, java.util.Map.ofEntries(java.util.Map.entry("leg3", p16)));
        p4.setInitialPose(nativeRoot.getChild("right_front_leg").storePose()); p4.resetPose();
        var p3Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_leg2", java.util.List.of()));
        var p3 = new net.minecraft.client.model.geom.ModelPart(p3Cubes, java.util.Map.ofEntries(java.util.Map.entry("leg2", p15)));
        p3.setInitialPose(nativeRoot.getChild("left_hind_leg").storePose()); p3.resetPose();
        var p2Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_leg1", java.util.List.of()));
        var p2 = new net.minecraft.client.model.geom.ModelPart(p2Cubes, java.util.Map.ofEntries(java.util.Map.entry("leg1", p14)));
        p2.setInitialPose(nativeRoot.getChild("right_hind_leg").storePose()); p2.resetPose();
        var p1Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body", java.util.List.of()));
        var p1 = new net.minecraft.client.model.geom.ModelPart(p1Cubes, java.util.Map.ofEntries(java.util.Map.entry("__cem_host_tail", p6), java.util.Map.entry("body", p12)));
        p1.setInitialPose(nativeRoot.getChild("body").storePose()); p1.resetPose();
        var p0Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_head", java.util.List.of()));
        var p0 = new net.minecraft.client.model.geom.ModelPart(p0Cubes, java.util.Map.ofEntries(java.util.Map.entry("head", p7)));
        p0.setInitialPose(nativeRoot.getChild("head").storePose()); p0.resetPose();
        return new net.minecraft.client.model.geom.ModelPart(java.util.List.of(), java.util.Map.ofEntries(java.util.Map.entry("__cem_host_head", p0), java.util.Map.entry("__cem_host_body", p1), java.util.Map.entry("__cem_host_leg1", p2), java.util.Map.entry("__cem_host_leg2", p3), java.util.Map.entry("__cem_host_leg3", p4), java.util.Map.entry("__cem_host_leg4", p5)));
    }
}
