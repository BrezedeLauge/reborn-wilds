/*
 * Generated adaptation of ReCrafted Mobs 11.0 by GillsBills and credited contributors.
 * Authored asset license: CC-BY-NC-SA-4.0; https://creativecommons.org/licenses/by-nc-sa/4.0/
 * Source: https://modrinth.com/resourcepack/recrafted-mobs
 * Model: assets/minecraft/optifine/cem/glow_squid/glow_squid2.jem
 * Source SHA256: 856162c814be633165a854733d90a777dcd20d2d05116c380da65b5e2922e99c
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

public final class CemPetBabySquidGeometry {
    private CemPetBabySquidGeometry() {}

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        PartDefinition p0 = root.addOrReplaceChild("__cem_host_body", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p1 = root.addOrReplaceChild("__cem_host_tentacle1", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p2 = root.addOrReplaceChild("__cem_host_tentacle2", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p3 = root.addOrReplaceChild("__cem_host_tentacle3", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p4 = root.addOrReplaceChild("__cem_host_tentacle4", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p5 = root.addOrReplaceChild("__cem_host_tentacle5", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p6 = root.addOrReplaceChild("__cem_host_tentacle6", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p7 = root.addOrReplaceChild("__cem_host_tentacle7", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p8 = root.addOrReplaceChild("__cem_host_tentacle8", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p9 = p0.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p10 = p9.addOrReplaceChild("babyghast", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -5.0F, -4.5F, 0.0F, 0.0F, 0.0F));
        PartDefinition p11 = p1.addOrReplaceChild("tentacle1", CubeListBuilder.create(), PartPose.offsetAndRotation(5.0F, -7.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p12 = p2.addOrReplaceChild("tentacle2", CubeListBuilder.create(), PartPose.offsetAndRotation(3.5F, -7.0F, 3.5F, 0.0F, 0.0F, 0.0F));
        PartDefinition p13 = p3.addOrReplaceChild("tentacle3", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -7.0F, 5.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p14 = p4.addOrReplaceChild("tentacle4", CubeListBuilder.create(), PartPose.offsetAndRotation(-3.5F, -7.0F, 3.5F, 0.0F, 0.0F, 0.0F));
        PartDefinition p15 = p5.addOrReplaceChild("tentacle5", CubeListBuilder.create(), PartPose.offsetAndRotation(10.0F, -7.0F, 5.0F, 0.0F, 1.57079633F, 0.0F));
        PartDefinition p16 = p6.addOrReplaceChild("tentacle6", CubeListBuilder.create(), PartPose.offsetAndRotation(-3.5F, -7.0F, -3.5F, 0.0F, 0.0F, 0.0F));
        PartDefinition p17 = p7.addOrReplaceChild("tentacle7", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -7.0F, -1.65F, 0.0F, 3.14159265F, 0.0F));
        PartDefinition p18 = p8.addOrReplaceChild("tentacle8", CubeListBuilder.create(), PartPose.offsetAndRotation(3.5F, -7.0F, -3.5F, 0.0F, 0.0F, 0.0F));
        PartDefinition p19 = p10.addOrReplaceChild("da_squid_arm_1", CubeListBuilder.create(), PartPose.offsetAndRotation(2.25F, 2.75F, 2.0F, 0.261799388F, 1.57079633F, 0.0F));
        PartDefinition p20 = p19.addOrReplaceChild("da_squid_tip_1", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 2.85F, 0.0F, -0.174532925F, 0.0F, 0.0F));
        PartDefinition p21 = p10.addOrReplaceChild("da_squid_arm_2", CubeListBuilder.create(), PartPose.offsetAndRotation(1.59099026F, 2.75F, 3.59099026F, 0.261799388F, 0.785398163F, 0.0F));
        PartDefinition p22 = p21.addOrReplaceChild("da_squid_tip_2", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 2.85F, 0.0F, -0.174532925F, 0.0F, 0.0F));
        PartDefinition p23 = p10.addOrReplaceChild("da_squid_arm_3", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 2.75F, 4.25F, 0.261799388F, 0.0F, 0.0F));
        PartDefinition p24 = p23.addOrReplaceChild("da_squid_tip_3", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 2.85F, 0.0F, -0.174532925F, 0.0F, 0.0F));
        PartDefinition p25 = p10.addOrReplaceChild("da_squid_arm_4", CubeListBuilder.create(), PartPose.offsetAndRotation(-1.59099026F, 2.75F, 3.59099026F, 0.261799388F, -0.785398163F, 0.0F));
        PartDefinition p26 = p25.addOrReplaceChild("da_squid_tip_4", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 2.85F, 0.0F, -0.174532925F, 0.0F, 0.0F));
        PartDefinition p27 = p10.addOrReplaceChild("da_squid_arm_5", CubeListBuilder.create(), PartPose.offsetAndRotation(-2.25F, 2.75F, 2.0F, 0.261799388F, -1.57079633F, 0.0F));
        PartDefinition p28 = p27.addOrReplaceChild("da_squid_tip_5", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 2.85F, 0.0F, -0.174532925F, 0.0F, 0.0F));
        PartDefinition p29 = p10.addOrReplaceChild("da_squid_arm_6", CubeListBuilder.create(), PartPose.offsetAndRotation(-1.59099026F, 2.75F, 0.409009742F, 0.261799388F, -2.35619449F, 0.0F));
        PartDefinition p30 = p29.addOrReplaceChild("da_squid_tip_6", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 2.85F, 0.0F, -0.174532925F, 0.0F, 0.0F));
        PartDefinition p31 = p10.addOrReplaceChild("da_squid_arm_7", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 2.75F, -0.25F, 0.261799388F, -3.14159265F, 0.0F));
        PartDefinition p32 = p31.addOrReplaceChild("da_squid_tip_7", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 2.85F, 0.0F, -0.174532925F, 0.0F, 0.0F));
        PartDefinition p33 = p10.addOrReplaceChild("da_squid_arm_8", CubeListBuilder.create(), PartPose.offsetAndRotation(1.59099026F, 2.75F, 0.409009742F, 0.261799388F, -3.92699082F, 0.0F));
        PartDefinition p34 = p33.addOrReplaceChild("da_squid_tip_8", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 2.85F, 0.0F, -0.174532925F, 0.0F, 0.0F));
        return LayerDefinition.create(mesh, 32, 32);
    }

    /** Preserve source expression units; static scales are reapplied after every pose reset. */
    public static ReferenceMobDefinition.Builder configure(ReferenceMobDefinition.Builder builder) {
        builder.cemDriver("glow_squid");
        builder.bakedRoot(CemPetBabySquidGeometry::bakeRoot);
        builder.hostAlias("body", "__cem_host_body");
        builder.hostAlias("tentacle1", "__cem_host_tentacle1");
        builder.hostAlias("tentacle2", "__cem_host_tentacle2");
        builder.hostAlias("tentacle3", "__cem_host_tentacle3");
        builder.hostAlias("tentacle4", "__cem_host_tentacle4");
        builder.hostAlias("tentacle5", "__cem_host_tentacle5");
        builder.hostAlias("tentacle6", "__cem_host_tentacle6");
        builder.hostAlias("tentacle7", "__cem_host_tentacle7");
        builder.hostAlias("tentacle8", "__cem_host_tentacle8");
        builder.expression("EMF_da_squid_arm_1.rx", "if(is_in_water, torad(24) + sin(age * 0.19 + 0.00000000) * (0.14 + min(limb_speed, 0.4) * 0.5), torad(12) + sin(age * 0.10 + 0.00000000) * 0.06)");
        builder.expression("EMF_da_squid_tip_1.rx", "if(is_in_water, -torad(20) + sin(age * 0.19 + 1.20000000) * 0.22, -torad(12) + sin(age * 0.10 + 1.20000000) * 0.05)");
        builder.expression("EMF_da_squid_arm_2.rx", "if(is_in_water, torad(24) + sin(age * 0.19 + 0.78539816) * (0.14 + min(limb_speed, 0.4) * 0.5), torad(12) + sin(age * 0.10 + 0.78539816) * 0.06)");
        builder.expression("EMF_da_squid_tip_2.rx", "if(is_in_water, -torad(20) + sin(age * 0.19 + 1.98539816) * 0.22, -torad(12) + sin(age * 0.10 + 1.98539816) * 0.05)");
        builder.expression("EMF_da_squid_arm_3.rx", "if(is_in_water, torad(24) + sin(age * 0.19 + 1.57079633) * (0.14 + min(limb_speed, 0.4) * 0.5), torad(12) + sin(age * 0.10 + 1.57079633) * 0.06)");
        builder.expression("EMF_da_squid_tip_3.rx", "if(is_in_water, -torad(20) + sin(age * 0.19 + 2.77079633) * 0.22, -torad(12) + sin(age * 0.10 + 2.77079633) * 0.05)");
        builder.expression("EMF_da_squid_arm_4.rx", "if(is_in_water, torad(24) + sin(age * 0.19 + 2.35619449) * (0.14 + min(limb_speed, 0.4) * 0.5), torad(12) + sin(age * 0.10 + 2.35619449) * 0.06)");
        builder.expression("EMF_da_squid_tip_4.rx", "if(is_in_water, -torad(20) + sin(age * 0.19 + 3.55619449) * 0.22, -torad(12) + sin(age * 0.10 + 3.55619449) * 0.05)");
        builder.expression("EMF_da_squid_arm_5.rx", "if(is_in_water, torad(24) + sin(age * 0.19 + 3.14159265) * (0.14 + min(limb_speed, 0.4) * 0.5), torad(12) + sin(age * 0.10 + 3.14159265) * 0.06)");
        builder.expression("EMF_da_squid_tip_5.rx", "if(is_in_water, -torad(20) + sin(age * 0.19 + 4.34159265) * 0.22, -torad(12) + sin(age * 0.10 + 4.34159265) * 0.05)");
        builder.expression("EMF_da_squid_arm_6.rx", "if(is_in_water, torad(24) + sin(age * 0.19 + 3.92699082) * (0.14 + min(limb_speed, 0.4) * 0.5), torad(12) + sin(age * 0.10 + 3.92699082) * 0.06)");
        builder.expression("EMF_da_squid_tip_6.rx", "if(is_in_water, -torad(20) + sin(age * 0.19 + 5.12699082) * 0.22, -torad(12) + sin(age * 0.10 + 5.12699082) * 0.05)");
        builder.expression("EMF_da_squid_arm_7.rx", "if(is_in_water, torad(24) + sin(age * 0.19 + 4.71238898) * (0.14 + min(limb_speed, 0.4) * 0.5), torad(12) + sin(age * 0.10 + 4.71238898) * 0.06)");
        builder.expression("EMF_da_squid_tip_7.rx", "if(is_in_water, -torad(20) + sin(age * 0.19 + 5.91238898) * 0.22, -torad(12) + sin(age * 0.10 + 5.91238898) * 0.05)");
        builder.expression("EMF_da_squid_arm_8.rx", "if(is_in_water, torad(24) + sin(age * 0.19 + 5.49778714) * (0.14 + min(limb_speed, 0.4) * 0.5), torad(12) + sin(age * 0.10 + 5.49778714) * 0.06)");
        builder.expression("EMF_da_squid_tip_8.rx", "if(is_in_water, -torad(20) + sin(age * 0.19 + 6.69778714) * 0.22, -torad(12) + sin(age * 0.10 + 6.69778714) * 0.05)");
        builder.expression("EMF_babyghast.ty", "if(is_in_water, -5 + sin(age * 0.095) * 0.18, -5)");
        return builder;
    }

    /** Complete matching rig; installed cubes stay owned by the Minecraft client. */
    public static net.minecraft.client.model.geom.ModelPart bakeRoot() {
        var nativeRoot = net.minecraft.client.model.SquidModel.createBodyLayer().bakeRoot();
        var nativeCubes = new java.util.HashMap<String, java.util.List<net.minecraft.client.model.geom.ModelPart.Cube>>();
        nativeRoot.visit(new com.mojang.blaze3d.vertex.PoseStack(),
                (pose, path, index, cube) -> nativeCubes.computeIfAbsent(path, key -> new java.util.ArrayList<>()).add(cube));
        var customCubes = new java.util.HashMap<String, java.util.List<net.minecraft.client.model.geom.ModelPart.Cube>>();
        createBodyLayer().bakeRoot().visit(new com.mojang.blaze3d.vertex.PoseStack(),
                (pose, path, index, cube) -> customCubes.computeIfAbsent(path, key -> new java.util.ArrayList<>()).add(cube));
        var p34Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/body/babyghast/da_squid_arm_8/da_squid_tip_8", java.util.List.of()));
        p34Cubes.add(new ReferenceMeshCube(-0.4F, 0.0F, -0.4F, 0.8F, 2.0F, 0.8F, new float[][][] {
                {{0.4F, 0.0F, 0.4F, 0.3875F, 0.03125F}, {-0.4F, 0.0F, 0.4F, 0.38125F, 0.03125F}, {-0.4F, 0.0F, -0.4F, 0.38125F, 0.0375F}, {0.4F, 0.0F, -0.4F, 0.3875F, 0.0375F}},
                {{0.4F, 2.0F, -0.4F, 0.39375F, 0.0375F}, {-0.4F, 2.0F, -0.4F, 0.3875F, 0.0375F}, {-0.4F, 2.0F, 0.4F, 0.3875F, 0.03125F}, {0.4F, 2.0F, 0.4F, 0.39375F, 0.03125F}},
                {{-0.4F, 0.0F, -0.4F, 0.38125F, 0.0375F}, {-0.4F, 0.0F, 0.4F, 0.375F, 0.0375F}, {-0.4F, 2.0F, 0.4F, 0.375F, 0.053125F}, {-0.4F, 2.0F, -0.4F, 0.38125F, 0.053125F}},
                {{0.4F, 0.0F, 0.4F, 0.39375F, 0.0375F}, {0.4F, 0.0F, -0.4F, 0.3875F, 0.0375F}, {0.4F, 2.0F, -0.4F, 0.3875F, 0.053125F}, {0.4F, 2.0F, 0.4F, 0.39375F, 0.053125F}},
                {{0.4F, 0.0F, -0.4F, 0.3875F, 0.0375F}, {-0.4F, 0.0F, -0.4F, 0.38125F, 0.0375F}, {-0.4F, 2.0F, -0.4F, 0.38125F, 0.053125F}, {0.4F, 2.0F, -0.4F, 0.3875F, 0.053125F}},
                {{-0.4F, 0.0F, 0.4F, 0.4F, 0.0375F}, {0.4F, 0.0F, 0.4F, 0.39375F, 0.0375F}, {0.4F, 2.0F, 0.4F, 0.39375F, 0.053125F}, {-0.4F, 2.0F, 0.4F, 0.4F, 0.053125F}}
        }));
        var p34 = new net.minecraft.client.model.geom.ModelPart(p34Cubes, java.util.Map.ofEntries());
        p34.setInitialPose(PartPose.offsetAndRotation(0.0F, 2.85F, 0.0F, -0.174532925F, 0.0F, 0.0F)); p34.resetPose();
        var p33Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/body/babyghast/da_squid_arm_8", java.util.List.of()));
        p33Cubes.add(new ReferenceMeshCube(-0.5F, 0.0F, -0.5F, 1.0F, 3.0F, 1.0F, new float[][][] {
                {{0.5F, 0.0F, 0.5F, 0.359375F, 0.03125F}, {-0.5F, 0.0F, 0.5F, 0.3515625F, 0.03125F}, {-0.5F, 0.0F, -0.5F, 0.3515625F, 0.0390625F}, {0.5F, 0.0F, -0.5F, 0.359375F, 0.0390625F}},
                {{0.5F, 3.0F, -0.5F, 0.3671875F, 0.0390625F}, {-0.5F, 3.0F, -0.5F, 0.359375F, 0.0390625F}, {-0.5F, 3.0F, 0.5F, 0.359375F, 0.03125F}, {0.5F, 3.0F, 0.5F, 0.3671875F, 0.03125F}},
                {{-0.5F, 0.0F, -0.5F, 0.3515625F, 0.0390625F}, {-0.5F, 0.0F, 0.5F, 0.34375F, 0.0390625F}, {-0.5F, 3.0F, 0.5F, 0.34375F, 0.0625F}, {-0.5F, 3.0F, -0.5F, 0.3515625F, 0.0625F}},
                {{0.5F, 0.0F, 0.5F, 0.3671875F, 0.0390625F}, {0.5F, 0.0F, -0.5F, 0.359375F, 0.0390625F}, {0.5F, 3.0F, -0.5F, 0.359375F, 0.0625F}, {0.5F, 3.0F, 0.5F, 0.3671875F, 0.0625F}},
                {{0.5F, 0.0F, -0.5F, 0.359375F, 0.0390625F}, {-0.5F, 0.0F, -0.5F, 0.3515625F, 0.0390625F}, {-0.5F, 3.0F, -0.5F, 0.3515625F, 0.0625F}, {0.5F, 3.0F, -0.5F, 0.359375F, 0.0625F}},
                {{-0.5F, 0.0F, 0.5F, 0.375F, 0.0390625F}, {0.5F, 0.0F, 0.5F, 0.3671875F, 0.0390625F}, {0.5F, 3.0F, 0.5F, 0.3671875F, 0.0625F}, {-0.5F, 3.0F, 0.5F, 0.375F, 0.0625F}}
        }));
        var p33 = new net.minecraft.client.model.geom.ModelPart(p33Cubes, java.util.Map.ofEntries(java.util.Map.entry("da_squid_tip_8", p34)));
        p33.setInitialPose(PartPose.offsetAndRotation(1.59099026F, 2.75F, 0.409009742F, 0.261799388F, -3.92699082F, 0.0F)); p33.resetPose();
        var p32Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/body/babyghast/da_squid_arm_7/da_squid_tip_7", java.util.List.of()));
        p32Cubes.add(new ReferenceMeshCube(-0.4F, 0.0F, -0.4F, 0.8F, 2.0F, 0.8F, new float[][][] {
                {{0.4F, 0.0F, 0.4F, 0.3875F, 0.03125F}, {-0.4F, 0.0F, 0.4F, 0.38125F, 0.03125F}, {-0.4F, 0.0F, -0.4F, 0.38125F, 0.0375F}, {0.4F, 0.0F, -0.4F, 0.3875F, 0.0375F}},
                {{0.4F, 2.0F, -0.4F, 0.39375F, 0.0375F}, {-0.4F, 2.0F, -0.4F, 0.3875F, 0.0375F}, {-0.4F, 2.0F, 0.4F, 0.3875F, 0.03125F}, {0.4F, 2.0F, 0.4F, 0.39375F, 0.03125F}},
                {{-0.4F, 0.0F, -0.4F, 0.38125F, 0.0375F}, {-0.4F, 0.0F, 0.4F, 0.375F, 0.0375F}, {-0.4F, 2.0F, 0.4F, 0.375F, 0.053125F}, {-0.4F, 2.0F, -0.4F, 0.38125F, 0.053125F}},
                {{0.4F, 0.0F, 0.4F, 0.39375F, 0.0375F}, {0.4F, 0.0F, -0.4F, 0.3875F, 0.0375F}, {0.4F, 2.0F, -0.4F, 0.3875F, 0.053125F}, {0.4F, 2.0F, 0.4F, 0.39375F, 0.053125F}},
                {{0.4F, 0.0F, -0.4F, 0.3875F, 0.0375F}, {-0.4F, 0.0F, -0.4F, 0.38125F, 0.0375F}, {-0.4F, 2.0F, -0.4F, 0.38125F, 0.053125F}, {0.4F, 2.0F, -0.4F, 0.3875F, 0.053125F}},
                {{-0.4F, 0.0F, 0.4F, 0.4F, 0.0375F}, {0.4F, 0.0F, 0.4F, 0.39375F, 0.0375F}, {0.4F, 2.0F, 0.4F, 0.39375F, 0.053125F}, {-0.4F, 2.0F, 0.4F, 0.4F, 0.053125F}}
        }));
        var p32 = new net.minecraft.client.model.geom.ModelPart(p32Cubes, java.util.Map.ofEntries());
        p32.setInitialPose(PartPose.offsetAndRotation(0.0F, 2.85F, 0.0F, -0.174532925F, 0.0F, 0.0F)); p32.resetPose();
        var p31Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/body/babyghast/da_squid_arm_7", java.util.List.of()));
        p31Cubes.add(new ReferenceMeshCube(-0.5F, 0.0F, -0.5F, 1.0F, 3.0F, 1.0F, new float[][][] {
                {{0.5F, 0.0F, 0.5F, 0.359375F, 0.03125F}, {-0.5F, 0.0F, 0.5F, 0.3515625F, 0.03125F}, {-0.5F, 0.0F, -0.5F, 0.3515625F, 0.0390625F}, {0.5F, 0.0F, -0.5F, 0.359375F, 0.0390625F}},
                {{0.5F, 3.0F, -0.5F, 0.3671875F, 0.0390625F}, {-0.5F, 3.0F, -0.5F, 0.359375F, 0.0390625F}, {-0.5F, 3.0F, 0.5F, 0.359375F, 0.03125F}, {0.5F, 3.0F, 0.5F, 0.3671875F, 0.03125F}},
                {{-0.5F, 0.0F, -0.5F, 0.3515625F, 0.0390625F}, {-0.5F, 0.0F, 0.5F, 0.34375F, 0.0390625F}, {-0.5F, 3.0F, 0.5F, 0.34375F, 0.0625F}, {-0.5F, 3.0F, -0.5F, 0.3515625F, 0.0625F}},
                {{0.5F, 0.0F, 0.5F, 0.3671875F, 0.0390625F}, {0.5F, 0.0F, -0.5F, 0.359375F, 0.0390625F}, {0.5F, 3.0F, -0.5F, 0.359375F, 0.0625F}, {0.5F, 3.0F, 0.5F, 0.3671875F, 0.0625F}},
                {{0.5F, 0.0F, -0.5F, 0.359375F, 0.0390625F}, {-0.5F, 0.0F, -0.5F, 0.3515625F, 0.0390625F}, {-0.5F, 3.0F, -0.5F, 0.3515625F, 0.0625F}, {0.5F, 3.0F, -0.5F, 0.359375F, 0.0625F}},
                {{-0.5F, 0.0F, 0.5F, 0.375F, 0.0390625F}, {0.5F, 0.0F, 0.5F, 0.3671875F, 0.0390625F}, {0.5F, 3.0F, 0.5F, 0.3671875F, 0.0625F}, {-0.5F, 3.0F, 0.5F, 0.375F, 0.0625F}}
        }));
        var p31 = new net.minecraft.client.model.geom.ModelPart(p31Cubes, java.util.Map.ofEntries(java.util.Map.entry("da_squid_tip_7", p32)));
        p31.setInitialPose(PartPose.offsetAndRotation(0.0F, 2.75F, -0.25F, 0.261799388F, -3.14159265F, 0.0F)); p31.resetPose();
        var p30Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/body/babyghast/da_squid_arm_6/da_squid_tip_6", java.util.List.of()));
        p30Cubes.add(new ReferenceMeshCube(-0.4F, 0.0F, -0.4F, 0.8F, 2.0F, 0.8F, new float[][][] {
                {{0.4F, 0.0F, 0.4F, 0.3875F, 0.03125F}, {-0.4F, 0.0F, 0.4F, 0.38125F, 0.03125F}, {-0.4F, 0.0F, -0.4F, 0.38125F, 0.0375F}, {0.4F, 0.0F, -0.4F, 0.3875F, 0.0375F}},
                {{0.4F, 2.0F, -0.4F, 0.39375F, 0.0375F}, {-0.4F, 2.0F, -0.4F, 0.3875F, 0.0375F}, {-0.4F, 2.0F, 0.4F, 0.3875F, 0.03125F}, {0.4F, 2.0F, 0.4F, 0.39375F, 0.03125F}},
                {{-0.4F, 0.0F, -0.4F, 0.38125F, 0.0375F}, {-0.4F, 0.0F, 0.4F, 0.375F, 0.0375F}, {-0.4F, 2.0F, 0.4F, 0.375F, 0.053125F}, {-0.4F, 2.0F, -0.4F, 0.38125F, 0.053125F}},
                {{0.4F, 0.0F, 0.4F, 0.39375F, 0.0375F}, {0.4F, 0.0F, -0.4F, 0.3875F, 0.0375F}, {0.4F, 2.0F, -0.4F, 0.3875F, 0.053125F}, {0.4F, 2.0F, 0.4F, 0.39375F, 0.053125F}},
                {{0.4F, 0.0F, -0.4F, 0.3875F, 0.0375F}, {-0.4F, 0.0F, -0.4F, 0.38125F, 0.0375F}, {-0.4F, 2.0F, -0.4F, 0.38125F, 0.053125F}, {0.4F, 2.0F, -0.4F, 0.3875F, 0.053125F}},
                {{-0.4F, 0.0F, 0.4F, 0.4F, 0.0375F}, {0.4F, 0.0F, 0.4F, 0.39375F, 0.0375F}, {0.4F, 2.0F, 0.4F, 0.39375F, 0.053125F}, {-0.4F, 2.0F, 0.4F, 0.4F, 0.053125F}}
        }));
        var p30 = new net.minecraft.client.model.geom.ModelPart(p30Cubes, java.util.Map.ofEntries());
        p30.setInitialPose(PartPose.offsetAndRotation(0.0F, 2.85F, 0.0F, -0.174532925F, 0.0F, 0.0F)); p30.resetPose();
        var p29Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/body/babyghast/da_squid_arm_6", java.util.List.of()));
        p29Cubes.add(new ReferenceMeshCube(-0.5F, 0.0F, -0.5F, 1.0F, 3.0F, 1.0F, new float[][][] {
                {{0.5F, 0.0F, 0.5F, 0.359375F, 0.03125F}, {-0.5F, 0.0F, 0.5F, 0.3515625F, 0.03125F}, {-0.5F, 0.0F, -0.5F, 0.3515625F, 0.0390625F}, {0.5F, 0.0F, -0.5F, 0.359375F, 0.0390625F}},
                {{0.5F, 3.0F, -0.5F, 0.3671875F, 0.0390625F}, {-0.5F, 3.0F, -0.5F, 0.359375F, 0.0390625F}, {-0.5F, 3.0F, 0.5F, 0.359375F, 0.03125F}, {0.5F, 3.0F, 0.5F, 0.3671875F, 0.03125F}},
                {{-0.5F, 0.0F, -0.5F, 0.3515625F, 0.0390625F}, {-0.5F, 0.0F, 0.5F, 0.34375F, 0.0390625F}, {-0.5F, 3.0F, 0.5F, 0.34375F, 0.0625F}, {-0.5F, 3.0F, -0.5F, 0.3515625F, 0.0625F}},
                {{0.5F, 0.0F, 0.5F, 0.3671875F, 0.0390625F}, {0.5F, 0.0F, -0.5F, 0.359375F, 0.0390625F}, {0.5F, 3.0F, -0.5F, 0.359375F, 0.0625F}, {0.5F, 3.0F, 0.5F, 0.3671875F, 0.0625F}},
                {{0.5F, 0.0F, -0.5F, 0.359375F, 0.0390625F}, {-0.5F, 0.0F, -0.5F, 0.3515625F, 0.0390625F}, {-0.5F, 3.0F, -0.5F, 0.3515625F, 0.0625F}, {0.5F, 3.0F, -0.5F, 0.359375F, 0.0625F}},
                {{-0.5F, 0.0F, 0.5F, 0.375F, 0.0390625F}, {0.5F, 0.0F, 0.5F, 0.3671875F, 0.0390625F}, {0.5F, 3.0F, 0.5F, 0.3671875F, 0.0625F}, {-0.5F, 3.0F, 0.5F, 0.375F, 0.0625F}}
        }));
        var p29 = new net.minecraft.client.model.geom.ModelPart(p29Cubes, java.util.Map.ofEntries(java.util.Map.entry("da_squid_tip_6", p30)));
        p29.setInitialPose(PartPose.offsetAndRotation(-1.59099026F, 2.75F, 0.409009742F, 0.261799388F, -2.35619449F, 0.0F)); p29.resetPose();
        var p28Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/body/babyghast/da_squid_arm_5/da_squid_tip_5", java.util.List.of()));
        p28Cubes.add(new ReferenceMeshCube(-0.4F, 0.0F, -0.4F, 0.8F, 2.0F, 0.8F, new float[][][] {
                {{0.4F, 0.0F, 0.4F, 0.3875F, 0.03125F}, {-0.4F, 0.0F, 0.4F, 0.38125F, 0.03125F}, {-0.4F, 0.0F, -0.4F, 0.38125F, 0.0375F}, {0.4F, 0.0F, -0.4F, 0.3875F, 0.0375F}},
                {{0.4F, 2.0F, -0.4F, 0.39375F, 0.0375F}, {-0.4F, 2.0F, -0.4F, 0.3875F, 0.0375F}, {-0.4F, 2.0F, 0.4F, 0.3875F, 0.03125F}, {0.4F, 2.0F, 0.4F, 0.39375F, 0.03125F}},
                {{-0.4F, 0.0F, -0.4F, 0.38125F, 0.0375F}, {-0.4F, 0.0F, 0.4F, 0.375F, 0.0375F}, {-0.4F, 2.0F, 0.4F, 0.375F, 0.053125F}, {-0.4F, 2.0F, -0.4F, 0.38125F, 0.053125F}},
                {{0.4F, 0.0F, 0.4F, 0.39375F, 0.0375F}, {0.4F, 0.0F, -0.4F, 0.3875F, 0.0375F}, {0.4F, 2.0F, -0.4F, 0.3875F, 0.053125F}, {0.4F, 2.0F, 0.4F, 0.39375F, 0.053125F}},
                {{0.4F, 0.0F, -0.4F, 0.3875F, 0.0375F}, {-0.4F, 0.0F, -0.4F, 0.38125F, 0.0375F}, {-0.4F, 2.0F, -0.4F, 0.38125F, 0.053125F}, {0.4F, 2.0F, -0.4F, 0.3875F, 0.053125F}},
                {{-0.4F, 0.0F, 0.4F, 0.4F, 0.0375F}, {0.4F, 0.0F, 0.4F, 0.39375F, 0.0375F}, {0.4F, 2.0F, 0.4F, 0.39375F, 0.053125F}, {-0.4F, 2.0F, 0.4F, 0.4F, 0.053125F}}
        }));
        var p28 = new net.minecraft.client.model.geom.ModelPart(p28Cubes, java.util.Map.ofEntries());
        p28.setInitialPose(PartPose.offsetAndRotation(0.0F, 2.85F, 0.0F, -0.174532925F, 0.0F, 0.0F)); p28.resetPose();
        var p27Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/body/babyghast/da_squid_arm_5", java.util.List.of()));
        p27Cubes.add(new ReferenceMeshCube(-0.5F, 0.0F, -0.5F, 1.0F, 3.0F, 1.0F, new float[][][] {
                {{0.5F, 0.0F, 0.5F, 0.359375F, 0.03125F}, {-0.5F, 0.0F, 0.5F, 0.3515625F, 0.03125F}, {-0.5F, 0.0F, -0.5F, 0.3515625F, 0.0390625F}, {0.5F, 0.0F, -0.5F, 0.359375F, 0.0390625F}},
                {{0.5F, 3.0F, -0.5F, 0.3671875F, 0.0390625F}, {-0.5F, 3.0F, -0.5F, 0.359375F, 0.0390625F}, {-0.5F, 3.0F, 0.5F, 0.359375F, 0.03125F}, {0.5F, 3.0F, 0.5F, 0.3671875F, 0.03125F}},
                {{-0.5F, 0.0F, -0.5F, 0.3515625F, 0.0390625F}, {-0.5F, 0.0F, 0.5F, 0.34375F, 0.0390625F}, {-0.5F, 3.0F, 0.5F, 0.34375F, 0.0625F}, {-0.5F, 3.0F, -0.5F, 0.3515625F, 0.0625F}},
                {{0.5F, 0.0F, 0.5F, 0.3671875F, 0.0390625F}, {0.5F, 0.0F, -0.5F, 0.359375F, 0.0390625F}, {0.5F, 3.0F, -0.5F, 0.359375F, 0.0625F}, {0.5F, 3.0F, 0.5F, 0.3671875F, 0.0625F}},
                {{0.5F, 0.0F, -0.5F, 0.359375F, 0.0390625F}, {-0.5F, 0.0F, -0.5F, 0.3515625F, 0.0390625F}, {-0.5F, 3.0F, -0.5F, 0.3515625F, 0.0625F}, {0.5F, 3.0F, -0.5F, 0.359375F, 0.0625F}},
                {{-0.5F, 0.0F, 0.5F, 0.375F, 0.0390625F}, {0.5F, 0.0F, 0.5F, 0.3671875F, 0.0390625F}, {0.5F, 3.0F, 0.5F, 0.3671875F, 0.0625F}, {-0.5F, 3.0F, 0.5F, 0.375F, 0.0625F}}
        }));
        var p27 = new net.minecraft.client.model.geom.ModelPart(p27Cubes, java.util.Map.ofEntries(java.util.Map.entry("da_squid_tip_5", p28)));
        p27.setInitialPose(PartPose.offsetAndRotation(-2.25F, 2.75F, 2.0F, 0.261799388F, -1.57079633F, 0.0F)); p27.resetPose();
        var p26Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/body/babyghast/da_squid_arm_4/da_squid_tip_4", java.util.List.of()));
        p26Cubes.add(new ReferenceMeshCube(-0.4F, 0.0F, -0.4F, 0.8F, 2.0F, 0.8F, new float[][][] {
                {{0.4F, 0.0F, 0.4F, 0.3875F, 0.03125F}, {-0.4F, 0.0F, 0.4F, 0.38125F, 0.03125F}, {-0.4F, 0.0F, -0.4F, 0.38125F, 0.0375F}, {0.4F, 0.0F, -0.4F, 0.3875F, 0.0375F}},
                {{0.4F, 2.0F, -0.4F, 0.39375F, 0.0375F}, {-0.4F, 2.0F, -0.4F, 0.3875F, 0.0375F}, {-0.4F, 2.0F, 0.4F, 0.3875F, 0.03125F}, {0.4F, 2.0F, 0.4F, 0.39375F, 0.03125F}},
                {{-0.4F, 0.0F, -0.4F, 0.38125F, 0.0375F}, {-0.4F, 0.0F, 0.4F, 0.375F, 0.0375F}, {-0.4F, 2.0F, 0.4F, 0.375F, 0.053125F}, {-0.4F, 2.0F, -0.4F, 0.38125F, 0.053125F}},
                {{0.4F, 0.0F, 0.4F, 0.39375F, 0.0375F}, {0.4F, 0.0F, -0.4F, 0.3875F, 0.0375F}, {0.4F, 2.0F, -0.4F, 0.3875F, 0.053125F}, {0.4F, 2.0F, 0.4F, 0.39375F, 0.053125F}},
                {{0.4F, 0.0F, -0.4F, 0.3875F, 0.0375F}, {-0.4F, 0.0F, -0.4F, 0.38125F, 0.0375F}, {-0.4F, 2.0F, -0.4F, 0.38125F, 0.053125F}, {0.4F, 2.0F, -0.4F, 0.3875F, 0.053125F}},
                {{-0.4F, 0.0F, 0.4F, 0.4F, 0.0375F}, {0.4F, 0.0F, 0.4F, 0.39375F, 0.0375F}, {0.4F, 2.0F, 0.4F, 0.39375F, 0.053125F}, {-0.4F, 2.0F, 0.4F, 0.4F, 0.053125F}}
        }));
        var p26 = new net.minecraft.client.model.geom.ModelPart(p26Cubes, java.util.Map.ofEntries());
        p26.setInitialPose(PartPose.offsetAndRotation(0.0F, 2.85F, 0.0F, -0.174532925F, 0.0F, 0.0F)); p26.resetPose();
        var p25Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/body/babyghast/da_squid_arm_4", java.util.List.of()));
        p25Cubes.add(new ReferenceMeshCube(-0.5F, 0.0F, -0.5F, 1.0F, 3.0F, 1.0F, new float[][][] {
                {{0.5F, 0.0F, 0.5F, 0.359375F, 0.03125F}, {-0.5F, 0.0F, 0.5F, 0.3515625F, 0.03125F}, {-0.5F, 0.0F, -0.5F, 0.3515625F, 0.0390625F}, {0.5F, 0.0F, -0.5F, 0.359375F, 0.0390625F}},
                {{0.5F, 3.0F, -0.5F, 0.3671875F, 0.0390625F}, {-0.5F, 3.0F, -0.5F, 0.359375F, 0.0390625F}, {-0.5F, 3.0F, 0.5F, 0.359375F, 0.03125F}, {0.5F, 3.0F, 0.5F, 0.3671875F, 0.03125F}},
                {{-0.5F, 0.0F, -0.5F, 0.3515625F, 0.0390625F}, {-0.5F, 0.0F, 0.5F, 0.34375F, 0.0390625F}, {-0.5F, 3.0F, 0.5F, 0.34375F, 0.0625F}, {-0.5F, 3.0F, -0.5F, 0.3515625F, 0.0625F}},
                {{0.5F, 0.0F, 0.5F, 0.3671875F, 0.0390625F}, {0.5F, 0.0F, -0.5F, 0.359375F, 0.0390625F}, {0.5F, 3.0F, -0.5F, 0.359375F, 0.0625F}, {0.5F, 3.0F, 0.5F, 0.3671875F, 0.0625F}},
                {{0.5F, 0.0F, -0.5F, 0.359375F, 0.0390625F}, {-0.5F, 0.0F, -0.5F, 0.3515625F, 0.0390625F}, {-0.5F, 3.0F, -0.5F, 0.3515625F, 0.0625F}, {0.5F, 3.0F, -0.5F, 0.359375F, 0.0625F}},
                {{-0.5F, 0.0F, 0.5F, 0.375F, 0.0390625F}, {0.5F, 0.0F, 0.5F, 0.3671875F, 0.0390625F}, {0.5F, 3.0F, 0.5F, 0.3671875F, 0.0625F}, {-0.5F, 3.0F, 0.5F, 0.375F, 0.0625F}}
        }));
        var p25 = new net.minecraft.client.model.geom.ModelPart(p25Cubes, java.util.Map.ofEntries(java.util.Map.entry("da_squid_tip_4", p26)));
        p25.setInitialPose(PartPose.offsetAndRotation(-1.59099026F, 2.75F, 3.59099026F, 0.261799388F, -0.785398163F, 0.0F)); p25.resetPose();
        var p24Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/body/babyghast/da_squid_arm_3/da_squid_tip_3", java.util.List.of()));
        p24Cubes.add(new ReferenceMeshCube(-0.4F, 0.0F, -0.4F, 0.8F, 2.0F, 0.8F, new float[][][] {
                {{0.4F, 0.0F, 0.4F, 0.3875F, 0.03125F}, {-0.4F, 0.0F, 0.4F, 0.38125F, 0.03125F}, {-0.4F, 0.0F, -0.4F, 0.38125F, 0.0375F}, {0.4F, 0.0F, -0.4F, 0.3875F, 0.0375F}},
                {{0.4F, 2.0F, -0.4F, 0.39375F, 0.0375F}, {-0.4F, 2.0F, -0.4F, 0.3875F, 0.0375F}, {-0.4F, 2.0F, 0.4F, 0.3875F, 0.03125F}, {0.4F, 2.0F, 0.4F, 0.39375F, 0.03125F}},
                {{-0.4F, 0.0F, -0.4F, 0.38125F, 0.0375F}, {-0.4F, 0.0F, 0.4F, 0.375F, 0.0375F}, {-0.4F, 2.0F, 0.4F, 0.375F, 0.053125F}, {-0.4F, 2.0F, -0.4F, 0.38125F, 0.053125F}},
                {{0.4F, 0.0F, 0.4F, 0.39375F, 0.0375F}, {0.4F, 0.0F, -0.4F, 0.3875F, 0.0375F}, {0.4F, 2.0F, -0.4F, 0.3875F, 0.053125F}, {0.4F, 2.0F, 0.4F, 0.39375F, 0.053125F}},
                {{0.4F, 0.0F, -0.4F, 0.3875F, 0.0375F}, {-0.4F, 0.0F, -0.4F, 0.38125F, 0.0375F}, {-0.4F, 2.0F, -0.4F, 0.38125F, 0.053125F}, {0.4F, 2.0F, -0.4F, 0.3875F, 0.053125F}},
                {{-0.4F, 0.0F, 0.4F, 0.4F, 0.0375F}, {0.4F, 0.0F, 0.4F, 0.39375F, 0.0375F}, {0.4F, 2.0F, 0.4F, 0.39375F, 0.053125F}, {-0.4F, 2.0F, 0.4F, 0.4F, 0.053125F}}
        }));
        var p24 = new net.minecraft.client.model.geom.ModelPart(p24Cubes, java.util.Map.ofEntries());
        p24.setInitialPose(PartPose.offsetAndRotation(0.0F, 2.85F, 0.0F, -0.174532925F, 0.0F, 0.0F)); p24.resetPose();
        var p23Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/body/babyghast/da_squid_arm_3", java.util.List.of()));
        p23Cubes.add(new ReferenceMeshCube(-0.5F, 0.0F, -0.5F, 1.0F, 3.0F, 1.0F, new float[][][] {
                {{0.5F, 0.0F, 0.5F, 0.359375F, 0.03125F}, {-0.5F, 0.0F, 0.5F, 0.3515625F, 0.03125F}, {-0.5F, 0.0F, -0.5F, 0.3515625F, 0.0390625F}, {0.5F, 0.0F, -0.5F, 0.359375F, 0.0390625F}},
                {{0.5F, 3.0F, -0.5F, 0.3671875F, 0.0390625F}, {-0.5F, 3.0F, -0.5F, 0.359375F, 0.0390625F}, {-0.5F, 3.0F, 0.5F, 0.359375F, 0.03125F}, {0.5F, 3.0F, 0.5F, 0.3671875F, 0.03125F}},
                {{-0.5F, 0.0F, -0.5F, 0.3515625F, 0.0390625F}, {-0.5F, 0.0F, 0.5F, 0.34375F, 0.0390625F}, {-0.5F, 3.0F, 0.5F, 0.34375F, 0.0625F}, {-0.5F, 3.0F, -0.5F, 0.3515625F, 0.0625F}},
                {{0.5F, 0.0F, 0.5F, 0.3671875F, 0.0390625F}, {0.5F, 0.0F, -0.5F, 0.359375F, 0.0390625F}, {0.5F, 3.0F, -0.5F, 0.359375F, 0.0625F}, {0.5F, 3.0F, 0.5F, 0.3671875F, 0.0625F}},
                {{0.5F, 0.0F, -0.5F, 0.359375F, 0.0390625F}, {-0.5F, 0.0F, -0.5F, 0.3515625F, 0.0390625F}, {-0.5F, 3.0F, -0.5F, 0.3515625F, 0.0625F}, {0.5F, 3.0F, -0.5F, 0.359375F, 0.0625F}},
                {{-0.5F, 0.0F, 0.5F, 0.375F, 0.0390625F}, {0.5F, 0.0F, 0.5F, 0.3671875F, 0.0390625F}, {0.5F, 3.0F, 0.5F, 0.3671875F, 0.0625F}, {-0.5F, 3.0F, 0.5F, 0.375F, 0.0625F}}
        }));
        var p23 = new net.minecraft.client.model.geom.ModelPart(p23Cubes, java.util.Map.ofEntries(java.util.Map.entry("da_squid_tip_3", p24)));
        p23.setInitialPose(PartPose.offsetAndRotation(0.0F, 2.75F, 4.25F, 0.261799388F, 0.0F, 0.0F)); p23.resetPose();
        var p22Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/body/babyghast/da_squid_arm_2/da_squid_tip_2", java.util.List.of()));
        p22Cubes.add(new ReferenceMeshCube(-0.4F, 0.0F, -0.4F, 0.8F, 2.0F, 0.8F, new float[][][] {
                {{0.4F, 0.0F, 0.4F, 0.3875F, 0.03125F}, {-0.4F, 0.0F, 0.4F, 0.38125F, 0.03125F}, {-0.4F, 0.0F, -0.4F, 0.38125F, 0.0375F}, {0.4F, 0.0F, -0.4F, 0.3875F, 0.0375F}},
                {{0.4F, 2.0F, -0.4F, 0.39375F, 0.0375F}, {-0.4F, 2.0F, -0.4F, 0.3875F, 0.0375F}, {-0.4F, 2.0F, 0.4F, 0.3875F, 0.03125F}, {0.4F, 2.0F, 0.4F, 0.39375F, 0.03125F}},
                {{-0.4F, 0.0F, -0.4F, 0.38125F, 0.0375F}, {-0.4F, 0.0F, 0.4F, 0.375F, 0.0375F}, {-0.4F, 2.0F, 0.4F, 0.375F, 0.053125F}, {-0.4F, 2.0F, -0.4F, 0.38125F, 0.053125F}},
                {{0.4F, 0.0F, 0.4F, 0.39375F, 0.0375F}, {0.4F, 0.0F, -0.4F, 0.3875F, 0.0375F}, {0.4F, 2.0F, -0.4F, 0.3875F, 0.053125F}, {0.4F, 2.0F, 0.4F, 0.39375F, 0.053125F}},
                {{0.4F, 0.0F, -0.4F, 0.3875F, 0.0375F}, {-0.4F, 0.0F, -0.4F, 0.38125F, 0.0375F}, {-0.4F, 2.0F, -0.4F, 0.38125F, 0.053125F}, {0.4F, 2.0F, -0.4F, 0.3875F, 0.053125F}},
                {{-0.4F, 0.0F, 0.4F, 0.4F, 0.0375F}, {0.4F, 0.0F, 0.4F, 0.39375F, 0.0375F}, {0.4F, 2.0F, 0.4F, 0.39375F, 0.053125F}, {-0.4F, 2.0F, 0.4F, 0.4F, 0.053125F}}
        }));
        var p22 = new net.minecraft.client.model.geom.ModelPart(p22Cubes, java.util.Map.ofEntries());
        p22.setInitialPose(PartPose.offsetAndRotation(0.0F, 2.85F, 0.0F, -0.174532925F, 0.0F, 0.0F)); p22.resetPose();
        var p21Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/body/babyghast/da_squid_arm_2", java.util.List.of()));
        p21Cubes.add(new ReferenceMeshCube(-0.5F, 0.0F, -0.5F, 1.0F, 3.0F, 1.0F, new float[][][] {
                {{0.5F, 0.0F, 0.5F, 0.359375F, 0.03125F}, {-0.5F, 0.0F, 0.5F, 0.3515625F, 0.03125F}, {-0.5F, 0.0F, -0.5F, 0.3515625F, 0.0390625F}, {0.5F, 0.0F, -0.5F, 0.359375F, 0.0390625F}},
                {{0.5F, 3.0F, -0.5F, 0.3671875F, 0.0390625F}, {-0.5F, 3.0F, -0.5F, 0.359375F, 0.0390625F}, {-0.5F, 3.0F, 0.5F, 0.359375F, 0.03125F}, {0.5F, 3.0F, 0.5F, 0.3671875F, 0.03125F}},
                {{-0.5F, 0.0F, -0.5F, 0.3515625F, 0.0390625F}, {-0.5F, 0.0F, 0.5F, 0.34375F, 0.0390625F}, {-0.5F, 3.0F, 0.5F, 0.34375F, 0.0625F}, {-0.5F, 3.0F, -0.5F, 0.3515625F, 0.0625F}},
                {{0.5F, 0.0F, 0.5F, 0.3671875F, 0.0390625F}, {0.5F, 0.0F, -0.5F, 0.359375F, 0.0390625F}, {0.5F, 3.0F, -0.5F, 0.359375F, 0.0625F}, {0.5F, 3.0F, 0.5F, 0.3671875F, 0.0625F}},
                {{0.5F, 0.0F, -0.5F, 0.359375F, 0.0390625F}, {-0.5F, 0.0F, -0.5F, 0.3515625F, 0.0390625F}, {-0.5F, 3.0F, -0.5F, 0.3515625F, 0.0625F}, {0.5F, 3.0F, -0.5F, 0.359375F, 0.0625F}},
                {{-0.5F, 0.0F, 0.5F, 0.375F, 0.0390625F}, {0.5F, 0.0F, 0.5F, 0.3671875F, 0.0390625F}, {0.5F, 3.0F, 0.5F, 0.3671875F, 0.0625F}, {-0.5F, 3.0F, 0.5F, 0.375F, 0.0625F}}
        }));
        var p21 = new net.minecraft.client.model.geom.ModelPart(p21Cubes, java.util.Map.ofEntries(java.util.Map.entry("da_squid_tip_2", p22)));
        p21.setInitialPose(PartPose.offsetAndRotation(1.59099026F, 2.75F, 3.59099026F, 0.261799388F, 0.785398163F, 0.0F)); p21.resetPose();
        var p20Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/body/babyghast/da_squid_arm_1/da_squid_tip_1", java.util.List.of()));
        p20Cubes.add(new ReferenceMeshCube(-0.4F, 0.0F, -0.4F, 0.8F, 2.0F, 0.8F, new float[][][] {
                {{0.4F, 0.0F, 0.4F, 0.3875F, 0.03125F}, {-0.4F, 0.0F, 0.4F, 0.38125F, 0.03125F}, {-0.4F, 0.0F, -0.4F, 0.38125F, 0.0375F}, {0.4F, 0.0F, -0.4F, 0.3875F, 0.0375F}},
                {{0.4F, 2.0F, -0.4F, 0.39375F, 0.0375F}, {-0.4F, 2.0F, -0.4F, 0.3875F, 0.0375F}, {-0.4F, 2.0F, 0.4F, 0.3875F, 0.03125F}, {0.4F, 2.0F, 0.4F, 0.39375F, 0.03125F}},
                {{-0.4F, 0.0F, -0.4F, 0.38125F, 0.0375F}, {-0.4F, 0.0F, 0.4F, 0.375F, 0.0375F}, {-0.4F, 2.0F, 0.4F, 0.375F, 0.053125F}, {-0.4F, 2.0F, -0.4F, 0.38125F, 0.053125F}},
                {{0.4F, 0.0F, 0.4F, 0.39375F, 0.0375F}, {0.4F, 0.0F, -0.4F, 0.3875F, 0.0375F}, {0.4F, 2.0F, -0.4F, 0.3875F, 0.053125F}, {0.4F, 2.0F, 0.4F, 0.39375F, 0.053125F}},
                {{0.4F, 0.0F, -0.4F, 0.3875F, 0.0375F}, {-0.4F, 0.0F, -0.4F, 0.38125F, 0.0375F}, {-0.4F, 2.0F, -0.4F, 0.38125F, 0.053125F}, {0.4F, 2.0F, -0.4F, 0.3875F, 0.053125F}},
                {{-0.4F, 0.0F, 0.4F, 0.4F, 0.0375F}, {0.4F, 0.0F, 0.4F, 0.39375F, 0.0375F}, {0.4F, 2.0F, 0.4F, 0.39375F, 0.053125F}, {-0.4F, 2.0F, 0.4F, 0.4F, 0.053125F}}
        }));
        var p20 = new net.minecraft.client.model.geom.ModelPart(p20Cubes, java.util.Map.ofEntries());
        p20.setInitialPose(PartPose.offsetAndRotation(0.0F, 2.85F, 0.0F, -0.174532925F, 0.0F, 0.0F)); p20.resetPose();
        var p19Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/body/babyghast/da_squid_arm_1", java.util.List.of()));
        p19Cubes.add(new ReferenceMeshCube(-0.5F, 0.0F, -0.5F, 1.0F, 3.0F, 1.0F, new float[][][] {
                {{0.5F, 0.0F, 0.5F, 0.359375F, 0.03125F}, {-0.5F, 0.0F, 0.5F, 0.3515625F, 0.03125F}, {-0.5F, 0.0F, -0.5F, 0.3515625F, 0.0390625F}, {0.5F, 0.0F, -0.5F, 0.359375F, 0.0390625F}},
                {{0.5F, 3.0F, -0.5F, 0.3671875F, 0.0390625F}, {-0.5F, 3.0F, -0.5F, 0.359375F, 0.0390625F}, {-0.5F, 3.0F, 0.5F, 0.359375F, 0.03125F}, {0.5F, 3.0F, 0.5F, 0.3671875F, 0.03125F}},
                {{-0.5F, 0.0F, -0.5F, 0.3515625F, 0.0390625F}, {-0.5F, 0.0F, 0.5F, 0.34375F, 0.0390625F}, {-0.5F, 3.0F, 0.5F, 0.34375F, 0.0625F}, {-0.5F, 3.0F, -0.5F, 0.3515625F, 0.0625F}},
                {{0.5F, 0.0F, 0.5F, 0.3671875F, 0.0390625F}, {0.5F, 0.0F, -0.5F, 0.359375F, 0.0390625F}, {0.5F, 3.0F, -0.5F, 0.359375F, 0.0625F}, {0.5F, 3.0F, 0.5F, 0.3671875F, 0.0625F}},
                {{0.5F, 0.0F, -0.5F, 0.359375F, 0.0390625F}, {-0.5F, 0.0F, -0.5F, 0.3515625F, 0.0390625F}, {-0.5F, 3.0F, -0.5F, 0.3515625F, 0.0625F}, {0.5F, 3.0F, -0.5F, 0.359375F, 0.0625F}},
                {{-0.5F, 0.0F, 0.5F, 0.375F, 0.0390625F}, {0.5F, 0.0F, 0.5F, 0.3671875F, 0.0390625F}, {0.5F, 3.0F, 0.5F, 0.3671875F, 0.0625F}, {-0.5F, 3.0F, 0.5F, 0.375F, 0.0625F}}
        }));
        var p19 = new net.minecraft.client.model.geom.ModelPart(p19Cubes, java.util.Map.ofEntries(java.util.Map.entry("da_squid_tip_1", p20)));
        p19.setInitialPose(PartPose.offsetAndRotation(2.25F, 2.75F, 2.0F, 0.261799388F, 1.57079633F, 0.0F)); p19.resetPose();
        var p18Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_tentacle8/tentacle8", java.util.List.of()));
        var p18 = new net.minecraft.client.model.geom.ModelPart(p18Cubes, java.util.Map.ofEntries());
        p18.setInitialPose(PartPose.offsetAndRotation(3.5F, -7.0F, -3.5F, 0.0F, 0.0F, 0.0F)); p18.resetPose();
        var p17Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_tentacle7/tentacle7", java.util.List.of()));
        var p17 = new net.minecraft.client.model.geom.ModelPart(p17Cubes, java.util.Map.ofEntries());
        p17.setInitialPose(PartPose.offsetAndRotation(0.0F, -7.0F, -1.65F, 0.0F, 3.14159265F, 0.0F)); p17.resetPose();
        var p16Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_tentacle6/tentacle6", java.util.List.of()));
        var p16 = new net.minecraft.client.model.geom.ModelPart(p16Cubes, java.util.Map.ofEntries());
        p16.setInitialPose(PartPose.offsetAndRotation(-3.5F, -7.0F, -3.5F, 0.0F, 0.0F, 0.0F)); p16.resetPose();
        var p15Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_tentacle5/tentacle5", java.util.List.of()));
        var p15 = new net.minecraft.client.model.geom.ModelPart(p15Cubes, java.util.Map.ofEntries());
        p15.setInitialPose(PartPose.offsetAndRotation(10.0F, -7.0F, 5.0F, 0.0F, 1.57079633F, 0.0F)); p15.resetPose();
        var p14Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_tentacle4/tentacle4", java.util.List.of()));
        var p14 = new net.minecraft.client.model.geom.ModelPart(p14Cubes, java.util.Map.ofEntries());
        p14.setInitialPose(PartPose.offsetAndRotation(-3.5F, -7.0F, 3.5F, 0.0F, 0.0F, 0.0F)); p14.resetPose();
        var p13Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_tentacle3/tentacle3", java.util.List.of()));
        var p13 = new net.minecraft.client.model.geom.ModelPart(p13Cubes, java.util.Map.ofEntries());
        p13.setInitialPose(PartPose.offsetAndRotation(0.0F, -7.0F, 5.0F, 0.0F, 0.0F, 0.0F)); p13.resetPose();
        var p12Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_tentacle2/tentacle2", java.util.List.of()));
        var p12 = new net.minecraft.client.model.geom.ModelPart(p12Cubes, java.util.Map.ofEntries());
        p12.setInitialPose(PartPose.offsetAndRotation(3.5F, -7.0F, 3.5F, 0.0F, 0.0F, 0.0F)); p12.resetPose();
        var p11Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_tentacle1/tentacle1", java.util.List.of()));
        var p11 = new net.minecraft.client.model.geom.ModelPart(p11Cubes, java.util.Map.ofEntries());
        p11.setInitialPose(PartPose.offsetAndRotation(5.0F, -7.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p11.resetPose();
        var p10Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/body/babyghast", java.util.List.of()));
        p10Cubes.add(new ReferenceMeshCube(-3.0F, -3.0F, -1.0F, 6.0F, 6.0F, 6.0F, new float[][][] {
                {{3.0F, -3.0F, 5.0F, 0.125F, 0.28125F}, {-3.0F, -3.0F, 5.0F, 0.078125F, 0.28125F}, {-3.0F, -3.0F, -1.0F, 0.078125F, 0.328125F}, {3.0F, -3.0F, -1.0F, 0.125F, 0.328125F}},
                {{3.0F, 3.0F, -1.0F, 0.171875F, 0.328125F}, {-3.0F, 3.0F, -1.0F, 0.125F, 0.328125F}, {-3.0F, 3.0F, 5.0F, 0.125F, 0.28125F}, {3.0F, 3.0F, 5.0F, 0.171875F, 0.28125F}},
                {{-3.0F, -3.0F, -1.0F, 0.078125F, 0.328125F}, {-3.0F, -3.0F, 5.0F, 0.03125F, 0.328125F}, {-3.0F, 3.0F, 5.0F, 0.03125F, 0.375F}, {-3.0F, 3.0F, -1.0F, 0.078125F, 0.375F}},
                {{3.0F, -3.0F, 5.0F, 0.171875F, 0.328125F}, {3.0F, -3.0F, -1.0F, 0.125F, 0.328125F}, {3.0F, 3.0F, -1.0F, 0.125F, 0.375F}, {3.0F, 3.0F, 5.0F, 0.171875F, 0.375F}},
                {{3.0F, -3.0F, -1.0F, 0.125F, 0.328125F}, {-3.0F, -3.0F, -1.0F, 0.078125F, 0.328125F}, {-3.0F, 3.0F, -1.0F, 0.078125F, 0.375F}, {3.0F, 3.0F, -1.0F, 0.125F, 0.375F}},
                {{-3.0F, -3.0F, 5.0F, 0.21875F, 0.328125F}, {3.0F, -3.0F, 5.0F, 0.171875F, 0.328125F}, {3.0F, 3.0F, 5.0F, 0.171875F, 0.375F}, {-3.0F, 3.0F, 5.0F, 0.21875F, 0.375F}}
        }));
        p10Cubes.add(new ReferenceMeshCube(-3.0F, 3.0F, -1.0F, 1.0F, 1.0F, 0.0F, new float[][][] {
                {{-2.0F, 3.0F, -1.0F, 0.0390625F, 0.28125F}, {-3.0F, 3.0F, -1.0F, 0.03125F, 0.28125F}, {-3.0F, 3.0F, -1.0F, 0.03125F, 0.28125F}, {-2.0F, 3.0F, -1.0F, 0.0390625F, 0.28125F}},
                {{-2.0F, 4.0F, -1.0F, 0.046875F, 0.28125F}, {-3.0F, 4.0F, -1.0F, 0.0390625F, 0.28125F}, {-3.0F, 4.0F, -1.0F, 0.0390625F, 0.28125F}, {-2.0F, 4.0F, -1.0F, 0.046875F, 0.28125F}},
                {{-3.0F, 3.0F, -1.0F, 0.03125F, 0.28125F}, {-3.0F, 3.0F, -1.0F, 0.03125F, 0.28125F}, {-3.0F, 4.0F, -1.0F, 0.03125F, 0.2890625F}, {-3.0F, 4.0F, -1.0F, 0.03125F, 0.2890625F}},
                {{-2.0F, 3.0F, -1.0F, 0.0390625F, 0.28125F}, {-2.0F, 3.0F, -1.0F, 0.0390625F, 0.28125F}, {-2.0F, 4.0F, -1.0F, 0.0390625F, 0.2890625F}, {-2.0F, 4.0F, -1.0F, 0.0390625F, 0.2890625F}},
                {{-2.0F, 3.0F, -1.0F, 0.0390625F, 0.28125F}, {-3.0F, 3.0F, -1.0F, 0.03125F, 0.28125F}, {-3.0F, 4.0F, -1.0F, 0.03125F, 0.2890625F}, {-2.0F, 4.0F, -1.0F, 0.0390625F, 0.2890625F}},
                {{-3.0F, 3.0F, -1.0F, 0.046875F, 0.28125F}, {-2.0F, 3.0F, -1.0F, 0.0390625F, 0.28125F}, {-2.0F, 4.0F, -1.0F, 0.0390625F, 0.2890625F}, {-3.0F, 4.0F, -1.0F, 0.046875F, 0.2890625F}}
        }));
        p10Cubes.add(new ReferenceMeshCube(-3.0F, 3.0F, 5.0F, 1.0F, 1.0F, 0.0F, new float[][][] {
                {{-2.0F, 3.0F, 5.0F, 0.0390625F, 0.28125F}, {-3.0F, 3.0F, 5.0F, 0.03125F, 0.28125F}, {-3.0F, 3.0F, 5.0F, 0.03125F, 0.28125F}, {-2.0F, 3.0F, 5.0F, 0.0390625F, 0.28125F}},
                {{-2.0F, 4.0F, 5.0F, 0.046875F, 0.28125F}, {-3.0F, 4.0F, 5.0F, 0.0390625F, 0.28125F}, {-3.0F, 4.0F, 5.0F, 0.0390625F, 0.28125F}, {-2.0F, 4.0F, 5.0F, 0.046875F, 0.28125F}},
                {{-3.0F, 3.0F, 5.0F, 0.03125F, 0.28125F}, {-3.0F, 3.0F, 5.0F, 0.03125F, 0.28125F}, {-3.0F, 4.0F, 5.0F, 0.03125F, 0.2890625F}, {-3.0F, 4.0F, 5.0F, 0.03125F, 0.2890625F}},
                {{-2.0F, 3.0F, 5.0F, 0.0390625F, 0.28125F}, {-2.0F, 3.0F, 5.0F, 0.0390625F, 0.28125F}, {-2.0F, 4.0F, 5.0F, 0.0390625F, 0.2890625F}, {-2.0F, 4.0F, 5.0F, 0.0390625F, 0.2890625F}},
                {{-2.0F, 3.0F, 5.0F, 0.0390625F, 0.28125F}, {-3.0F, 3.0F, 5.0F, 0.03125F, 0.28125F}, {-3.0F, 4.0F, 5.0F, 0.03125F, 0.2890625F}, {-2.0F, 4.0F, 5.0F, 0.0390625F, 0.2890625F}},
                {{-3.0F, 3.0F, 5.0F, 0.046875F, 0.28125F}, {-2.0F, 3.0F, 5.0F, 0.0390625F, 0.28125F}, {-2.0F, 4.0F, 5.0F, 0.0390625F, 0.2890625F}, {-3.0F, 4.0F, 5.0F, 0.046875F, 0.2890625F}}
        }));
        p10Cubes.add(new ReferenceMeshCube(2.0F, 3.0F, -1.0F, 1.0F, 1.0F, 0.0F, new float[][][] {
                {{3.0F, 3.0F, -1.0F, 0.0390625F, 0.28125F}, {2.0F, 3.0F, -1.0F, 0.03125F, 0.28125F}, {2.0F, 3.0F, -1.0F, 0.03125F, 0.28125F}, {3.0F, 3.0F, -1.0F, 0.0390625F, 0.28125F}},
                {{3.0F, 4.0F, -1.0F, 0.046875F, 0.28125F}, {2.0F, 4.0F, -1.0F, 0.0390625F, 0.28125F}, {2.0F, 4.0F, -1.0F, 0.0390625F, 0.28125F}, {3.0F, 4.0F, -1.0F, 0.046875F, 0.28125F}},
                {{2.0F, 3.0F, -1.0F, 0.03125F, 0.28125F}, {2.0F, 3.0F, -1.0F, 0.03125F, 0.28125F}, {2.0F, 4.0F, -1.0F, 0.03125F, 0.2890625F}, {2.0F, 4.0F, -1.0F, 0.03125F, 0.2890625F}},
                {{3.0F, 3.0F, -1.0F, 0.0390625F, 0.28125F}, {3.0F, 3.0F, -1.0F, 0.0390625F, 0.28125F}, {3.0F, 4.0F, -1.0F, 0.0390625F, 0.2890625F}, {3.0F, 4.0F, -1.0F, 0.0390625F, 0.2890625F}},
                {{3.0F, 3.0F, -1.0F, 0.0390625F, 0.28125F}, {2.0F, 3.0F, -1.0F, 0.03125F, 0.28125F}, {2.0F, 4.0F, -1.0F, 0.03125F, 0.2890625F}, {3.0F, 4.0F, -1.0F, 0.0390625F, 0.2890625F}},
                {{2.0F, 3.0F, -1.0F, 0.046875F, 0.28125F}, {3.0F, 3.0F, -1.0F, 0.0390625F, 0.28125F}, {3.0F, 4.0F, -1.0F, 0.0390625F, 0.2890625F}, {2.0F, 4.0F, -1.0F, 0.046875F, 0.2890625F}}
        }));
        p10Cubes.add(new ReferenceMeshCube(3.0F, 3.0F, 0.0F, 0.0F, 1.0F, 1.0F, new float[][][] {
                {{3.0F, 3.0F, 1.0F, 0.0390625F, 0.2734375F}, {3.0F, 3.0F, 1.0F, 0.0390625F, 0.2734375F}, {3.0F, 3.0F, 0.0F, 0.0390625F, 0.28125F}, {3.0F, 3.0F, 0.0F, 0.0390625F, 0.28125F}},
                {{3.0F, 4.0F, 0.0F, 0.0390625F, 0.28125F}, {3.0F, 4.0F, 0.0F, 0.0390625F, 0.28125F}, {3.0F, 4.0F, 1.0F, 0.0390625F, 0.2734375F}, {3.0F, 4.0F, 1.0F, 0.0390625F, 0.2734375F}},
                {{3.0F, 3.0F, 0.0F, 0.0390625F, 0.28125F}, {3.0F, 3.0F, 1.0F, 0.03125F, 0.28125F}, {3.0F, 4.0F, 1.0F, 0.03125F, 0.2890625F}, {3.0F, 4.0F, 0.0F, 0.0390625F, 0.2890625F}},
                {{3.0F, 3.0F, 1.0F, 0.046875F, 0.28125F}, {3.0F, 3.0F, 0.0F, 0.0390625F, 0.28125F}, {3.0F, 4.0F, 0.0F, 0.0390625F, 0.2890625F}, {3.0F, 4.0F, 1.0F, 0.046875F, 0.2890625F}},
                {{3.0F, 3.0F, 0.0F, 0.0390625F, 0.28125F}, {3.0F, 3.0F, 0.0F, 0.0390625F, 0.28125F}, {3.0F, 4.0F, 0.0F, 0.0390625F, 0.2890625F}, {3.0F, 4.0F, 0.0F, 0.0390625F, 0.2890625F}},
                {{3.0F, 3.0F, 1.0F, 0.046875F, 0.28125F}, {3.0F, 3.0F, 1.0F, 0.046875F, 0.28125F}, {3.0F, 4.0F, 1.0F, 0.046875F, 0.2890625F}, {3.0F, 4.0F, 1.0F, 0.046875F, 0.2890625F}}
        }));
        p10Cubes.add(new ReferenceMeshCube(-3.0F, 3.0F, 0.0F, 0.0F, 1.0F, 1.0F, new float[][][] {
                {{-3.0F, 3.0F, 1.0F, 0.0390625F, 0.2734375F}, {-3.0F, 3.0F, 1.0F, 0.0390625F, 0.2734375F}, {-3.0F, 3.0F, 0.0F, 0.0390625F, 0.28125F}, {-3.0F, 3.0F, 0.0F, 0.0390625F, 0.28125F}},
                {{-3.0F, 4.0F, 0.0F, 0.0390625F, 0.28125F}, {-3.0F, 4.0F, 0.0F, 0.0390625F, 0.28125F}, {-3.0F, 4.0F, 1.0F, 0.0390625F, 0.2734375F}, {-3.0F, 4.0F, 1.0F, 0.0390625F, 0.2734375F}},
                {{-3.0F, 3.0F, 0.0F, 0.0390625F, 0.28125F}, {-3.0F, 3.0F, 1.0F, 0.03125F, 0.28125F}, {-3.0F, 4.0F, 1.0F, 0.03125F, 0.2890625F}, {-3.0F, 4.0F, 0.0F, 0.0390625F, 0.2890625F}},
                {{-3.0F, 3.0F, 1.0F, 0.046875F, 0.28125F}, {-3.0F, 3.0F, 0.0F, 0.0390625F, 0.28125F}, {-3.0F, 4.0F, 0.0F, 0.0390625F, 0.2890625F}, {-3.0F, 4.0F, 1.0F, 0.046875F, 0.2890625F}},
                {{-3.0F, 3.0F, 0.0F, 0.0390625F, 0.28125F}, {-3.0F, 3.0F, 0.0F, 0.0390625F, 0.28125F}, {-3.0F, 4.0F, 0.0F, 0.0390625F, 0.2890625F}, {-3.0F, 4.0F, 0.0F, 0.0390625F, 0.2890625F}},
                {{-3.0F, 3.0F, 1.0F, 0.046875F, 0.28125F}, {-3.0F, 3.0F, 1.0F, 0.046875F, 0.28125F}, {-3.0F, 4.0F, 1.0F, 0.046875F, 0.2890625F}, {-3.0F, 4.0F, 1.0F, 0.046875F, 0.2890625F}}
        }));
        p10Cubes.add(new ReferenceMeshCube(-3.0F, 3.0F, 3.0F, 0.0F, 1.0F, 1.0F, new float[][][] {
                {{-3.0F, 3.0F, 4.0F, 0.0390625F, 0.2734375F}, {-3.0F, 3.0F, 4.0F, 0.0390625F, 0.2734375F}, {-3.0F, 3.0F, 3.0F, 0.0390625F, 0.28125F}, {-3.0F, 3.0F, 3.0F, 0.0390625F, 0.28125F}},
                {{-3.0F, 4.0F, 3.0F, 0.0390625F, 0.28125F}, {-3.0F, 4.0F, 3.0F, 0.0390625F, 0.28125F}, {-3.0F, 4.0F, 4.0F, 0.0390625F, 0.2734375F}, {-3.0F, 4.0F, 4.0F, 0.0390625F, 0.2734375F}},
                {{-3.0F, 3.0F, 3.0F, 0.0390625F, 0.28125F}, {-3.0F, 3.0F, 4.0F, 0.03125F, 0.28125F}, {-3.0F, 4.0F, 4.0F, 0.03125F, 0.2890625F}, {-3.0F, 4.0F, 3.0F, 0.0390625F, 0.2890625F}},
                {{-3.0F, 3.0F, 4.0F, 0.046875F, 0.28125F}, {-3.0F, 3.0F, 3.0F, 0.0390625F, 0.28125F}, {-3.0F, 4.0F, 3.0F, 0.0390625F, 0.2890625F}, {-3.0F, 4.0F, 4.0F, 0.046875F, 0.2890625F}},
                {{-3.0F, 3.0F, 3.0F, 0.0390625F, 0.28125F}, {-3.0F, 3.0F, 3.0F, 0.0390625F, 0.28125F}, {-3.0F, 4.0F, 3.0F, 0.0390625F, 0.2890625F}, {-3.0F, 4.0F, 3.0F, 0.0390625F, 0.2890625F}},
                {{-3.0F, 3.0F, 4.0F, 0.046875F, 0.28125F}, {-3.0F, 3.0F, 4.0F, 0.046875F, 0.28125F}, {-3.0F, 4.0F, 4.0F, 0.046875F, 0.2890625F}, {-3.0F, 4.0F, 4.0F, 0.046875F, 0.2890625F}}
        }));
        p10Cubes.add(new ReferenceMeshCube(3.0F, 3.0F, 3.0F, 0.0F, 1.0F, 1.0F, new float[][][] {
                {{3.0F, 3.0F, 4.0F, 0.0390625F, 0.2734375F}, {3.0F, 3.0F, 4.0F, 0.0390625F, 0.2734375F}, {3.0F, 3.0F, 3.0F, 0.0390625F, 0.28125F}, {3.0F, 3.0F, 3.0F, 0.0390625F, 0.28125F}},
                {{3.0F, 4.0F, 3.0F, 0.0390625F, 0.28125F}, {3.0F, 4.0F, 3.0F, 0.0390625F, 0.28125F}, {3.0F, 4.0F, 4.0F, 0.0390625F, 0.2734375F}, {3.0F, 4.0F, 4.0F, 0.0390625F, 0.2734375F}},
                {{3.0F, 3.0F, 3.0F, 0.0390625F, 0.28125F}, {3.0F, 3.0F, 4.0F, 0.03125F, 0.28125F}, {3.0F, 4.0F, 4.0F, 0.03125F, 0.2890625F}, {3.0F, 4.0F, 3.0F, 0.0390625F, 0.2890625F}},
                {{3.0F, 3.0F, 4.0F, 0.046875F, 0.28125F}, {3.0F, 3.0F, 3.0F, 0.0390625F, 0.28125F}, {3.0F, 4.0F, 3.0F, 0.0390625F, 0.2890625F}, {3.0F, 4.0F, 4.0F, 0.046875F, 0.2890625F}},
                {{3.0F, 3.0F, 3.0F, 0.0390625F, 0.28125F}, {3.0F, 3.0F, 3.0F, 0.0390625F, 0.28125F}, {3.0F, 4.0F, 3.0F, 0.0390625F, 0.2890625F}, {3.0F, 4.0F, 3.0F, 0.0390625F, 0.2890625F}},
                {{3.0F, 3.0F, 4.0F, 0.046875F, 0.28125F}, {3.0F, 3.0F, 4.0F, 0.046875F, 0.28125F}, {3.0F, 4.0F, 4.0F, 0.046875F, 0.2890625F}, {3.0F, 4.0F, 4.0F, 0.046875F, 0.2890625F}}
        }));
        p10Cubes.add(new ReferenceMeshCube(2.0F, 3.0F, 5.0F, 1.0F, 1.0F, 0.0F, new float[][][] {
                {{3.0F, 3.0F, 5.0F, 0.0390625F, 0.28125F}, {2.0F, 3.0F, 5.0F, 0.03125F, 0.28125F}, {2.0F, 3.0F, 5.0F, 0.03125F, 0.28125F}, {3.0F, 3.0F, 5.0F, 0.0390625F, 0.28125F}},
                {{3.0F, 4.0F, 5.0F, 0.046875F, 0.28125F}, {2.0F, 4.0F, 5.0F, 0.0390625F, 0.28125F}, {2.0F, 4.0F, 5.0F, 0.0390625F, 0.28125F}, {3.0F, 4.0F, 5.0F, 0.046875F, 0.28125F}},
                {{2.0F, 3.0F, 5.0F, 0.03125F, 0.28125F}, {2.0F, 3.0F, 5.0F, 0.03125F, 0.28125F}, {2.0F, 4.0F, 5.0F, 0.03125F, 0.2890625F}, {2.0F, 4.0F, 5.0F, 0.03125F, 0.2890625F}},
                {{3.0F, 3.0F, 5.0F, 0.0390625F, 0.28125F}, {3.0F, 3.0F, 5.0F, 0.0390625F, 0.28125F}, {3.0F, 4.0F, 5.0F, 0.0390625F, 0.2890625F}, {3.0F, 4.0F, 5.0F, 0.0390625F, 0.2890625F}},
                {{3.0F, 3.0F, 5.0F, 0.0390625F, 0.28125F}, {2.0F, 3.0F, 5.0F, 0.03125F, 0.28125F}, {2.0F, 4.0F, 5.0F, 0.03125F, 0.2890625F}, {3.0F, 4.0F, 5.0F, 0.0390625F, 0.2890625F}},
                {{2.0F, 3.0F, 5.0F, 0.046875F, 0.28125F}, {3.0F, 3.0F, 5.0F, 0.0390625F, 0.28125F}, {3.0F, 4.0F, 5.0F, 0.0390625F, 0.2890625F}, {2.0F, 4.0F, 5.0F, 0.046875F, 0.2890625F}}
        }));
        p10Cubes.add(new ReferenceMeshCube(-0.5F, 3.0F, -1.0F, 1.0F, 1.0F, 0.0F, new float[][][] {
                {{0.5F, 3.0F, -1.0F, 0.0390625F, 0.28125F}, {-0.5F, 3.0F, -1.0F, 0.03125F, 0.28125F}, {-0.5F, 3.0F, -1.0F, 0.03125F, 0.28125F}, {0.5F, 3.0F, -1.0F, 0.0390625F, 0.28125F}},
                {{0.5F, 4.0F, -1.0F, 0.046875F, 0.28125F}, {-0.5F, 4.0F, -1.0F, 0.0390625F, 0.28125F}, {-0.5F, 4.0F, -1.0F, 0.0390625F, 0.28125F}, {0.5F, 4.0F, -1.0F, 0.046875F, 0.28125F}},
                {{-0.5F, 3.0F, -1.0F, 0.03125F, 0.28125F}, {-0.5F, 3.0F, -1.0F, 0.03125F, 0.28125F}, {-0.5F, 4.0F, -1.0F, 0.03125F, 0.2890625F}, {-0.5F, 4.0F, -1.0F, 0.03125F, 0.2890625F}},
                {{0.5F, 3.0F, -1.0F, 0.0390625F, 0.28125F}, {0.5F, 3.0F, -1.0F, 0.0390625F, 0.28125F}, {0.5F, 4.0F, -1.0F, 0.0390625F, 0.2890625F}, {0.5F, 4.0F, -1.0F, 0.0390625F, 0.2890625F}},
                {{0.5F, 3.0F, -1.0F, 0.0390625F, 0.28125F}, {-0.5F, 3.0F, -1.0F, 0.03125F, 0.28125F}, {-0.5F, 4.0F, -1.0F, 0.03125F, 0.2890625F}, {0.5F, 4.0F, -1.0F, 0.0390625F, 0.2890625F}},
                {{-0.5F, 3.0F, -1.0F, 0.046875F, 0.28125F}, {0.5F, 3.0F, -1.0F, 0.0390625F, 0.28125F}, {0.5F, 4.0F, -1.0F, 0.0390625F, 0.2890625F}, {-0.5F, 4.0F, -1.0F, 0.046875F, 0.2890625F}}
        }));
        p10Cubes.add(new ReferenceMeshCube(-0.5F, 3.0F, 5.0F, 1.0F, 1.0F, 0.0F, new float[][][] {
                {{0.5F, 3.0F, 5.0F, 0.0390625F, 0.28125F}, {-0.5F, 3.0F, 5.0F, 0.03125F, 0.28125F}, {-0.5F, 3.0F, 5.0F, 0.03125F, 0.28125F}, {0.5F, 3.0F, 5.0F, 0.0390625F, 0.28125F}},
                {{0.5F, 4.0F, 5.0F, 0.046875F, 0.28125F}, {-0.5F, 4.0F, 5.0F, 0.0390625F, 0.28125F}, {-0.5F, 4.0F, 5.0F, 0.0390625F, 0.28125F}, {0.5F, 4.0F, 5.0F, 0.046875F, 0.28125F}},
                {{-0.5F, 3.0F, 5.0F, 0.03125F, 0.28125F}, {-0.5F, 3.0F, 5.0F, 0.03125F, 0.28125F}, {-0.5F, 4.0F, 5.0F, 0.03125F, 0.2890625F}, {-0.5F, 4.0F, 5.0F, 0.03125F, 0.2890625F}},
                {{0.5F, 3.0F, 5.0F, 0.0390625F, 0.28125F}, {0.5F, 3.0F, 5.0F, 0.0390625F, 0.28125F}, {0.5F, 4.0F, 5.0F, 0.0390625F, 0.2890625F}, {0.5F, 4.0F, 5.0F, 0.0390625F, 0.2890625F}},
                {{0.5F, 3.0F, 5.0F, 0.0390625F, 0.28125F}, {-0.5F, 3.0F, 5.0F, 0.03125F, 0.28125F}, {-0.5F, 4.0F, 5.0F, 0.03125F, 0.2890625F}, {0.5F, 4.0F, 5.0F, 0.0390625F, 0.2890625F}},
                {{-0.5F, 3.0F, 5.0F, 0.046875F, 0.28125F}, {0.5F, 3.0F, 5.0F, 0.0390625F, 0.28125F}, {0.5F, 4.0F, 5.0F, 0.0390625F, 0.2890625F}, {-0.5F, 4.0F, 5.0F, 0.046875F, 0.2890625F}}
        }));
        var p10 = new net.minecraft.client.model.geom.ModelPart(p10Cubes, java.util.Map.ofEntries(java.util.Map.entry("da_squid_arm_1", p19), java.util.Map.entry("da_squid_arm_2", p21), java.util.Map.entry("da_squid_arm_3", p23), java.util.Map.entry("da_squid_arm_4", p25), java.util.Map.entry("da_squid_arm_5", p27), java.util.Map.entry("da_squid_arm_6", p29), java.util.Map.entry("da_squid_arm_7", p31), java.util.Map.entry("da_squid_arm_8", p33)));
        p10.setInitialPose(PartPose.offsetAndRotation(0.0F, -5.0F, -4.5F, 0.0F, 0.0F, 0.0F)); p10.resetPose();
        var p9Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body/body", java.util.List.of()));
        var p9 = new net.minecraft.client.model.geom.ModelPart(p9Cubes, java.util.Map.ofEntries(java.util.Map.entry("babyghast", p10)));
        p9.setInitialPose(PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p9.resetPose();
        var p8Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_tentacle8", java.util.List.of()));
        var p8 = new net.minecraft.client.model.geom.ModelPart(p8Cubes, java.util.Map.ofEntries(java.util.Map.entry("tentacle8", p18)));
        p8.setInitialPose(nativeRoot.getChild("tentacle7").storePose()); p8.resetPose();
        var p7Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_tentacle7", java.util.List.of()));
        var p7 = new net.minecraft.client.model.geom.ModelPart(p7Cubes, java.util.Map.ofEntries(java.util.Map.entry("tentacle7", p17)));
        p7.setInitialPose(nativeRoot.getChild("tentacle6").storePose()); p7.resetPose();
        var p6Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_tentacle6", java.util.List.of()));
        var p6 = new net.minecraft.client.model.geom.ModelPart(p6Cubes, java.util.Map.ofEntries(java.util.Map.entry("tentacle6", p16)));
        p6.setInitialPose(nativeRoot.getChild("tentacle5").storePose()); p6.resetPose();
        var p5Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_tentacle5", java.util.List.of()));
        var p5 = new net.minecraft.client.model.geom.ModelPart(p5Cubes, java.util.Map.ofEntries(java.util.Map.entry("tentacle5", p15)));
        p5.setInitialPose(nativeRoot.getChild("tentacle4").storePose()); p5.resetPose();
        var p4Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_tentacle4", java.util.List.of()));
        var p4 = new net.minecraft.client.model.geom.ModelPart(p4Cubes, java.util.Map.ofEntries(java.util.Map.entry("tentacle4", p14)));
        p4.setInitialPose(nativeRoot.getChild("tentacle3").storePose()); p4.resetPose();
        var p3Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_tentacle3", java.util.List.of()));
        var p3 = new net.minecraft.client.model.geom.ModelPart(p3Cubes, java.util.Map.ofEntries(java.util.Map.entry("tentacle3", p13)));
        p3.setInitialPose(nativeRoot.getChild("tentacle2").storePose()); p3.resetPose();
        var p2Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_tentacle2", java.util.List.of()));
        var p2 = new net.minecraft.client.model.geom.ModelPart(p2Cubes, java.util.Map.ofEntries(java.util.Map.entry("tentacle2", p12)));
        p2.setInitialPose(nativeRoot.getChild("tentacle1").storePose()); p2.resetPose();
        var p1Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_tentacle1", java.util.List.of()));
        var p1 = new net.minecraft.client.model.geom.ModelPart(p1Cubes, java.util.Map.ofEntries(java.util.Map.entry("tentacle1", p11)));
        p1.setInitialPose(nativeRoot.getChild("tentacle0").storePose()); p1.resetPose();
        var p0Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body", java.util.List.of()));
        var p0 = new net.minecraft.client.model.geom.ModelPart(p0Cubes, java.util.Map.ofEntries(java.util.Map.entry("body", p9)));
        p0.setInitialPose(nativeRoot.getChild("body").storePose()); p0.resetPose();
        return new net.minecraft.client.model.geom.ModelPart(java.util.List.of(), java.util.Map.ofEntries(java.util.Map.entry("__cem_host_body", p0), java.util.Map.entry("__cem_host_tentacle1", p1), java.util.Map.entry("__cem_host_tentacle2", p2), java.util.Map.entry("__cem_host_tentacle3", p3), java.util.Map.entry("__cem_host_tentacle4", p4), java.util.Map.entry("__cem_host_tentacle5", p5), java.util.Map.entry("__cem_host_tentacle6", p6), java.util.Map.entry("__cem_host_tentacle7", p7), java.util.Map.entry("__cem_host_tentacle8", p8)));
    }
}
