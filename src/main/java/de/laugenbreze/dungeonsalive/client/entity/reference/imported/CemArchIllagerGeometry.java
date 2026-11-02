/*
 * Generated adaptation of ReCrafted Mobs 11.0 by GillsBills and credited contributors.
 * Authored asset license: CC-BY-NC-SA-4.0; https://creativecommons.org/licenses/by-nc-sa/4.0/
 * Source: https://modrinth.com/resourcepack/recrafted-mobs
 * Model: assets/minecraft/optifine/cem/wither/wither6.jem
 * Source SHA256: cb8559e107d6c5ece3db867582b2ecd18ec1cceda12f68d2750da153433ae9bd
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

public final class CemArchIllagerGeometry {
    private CemArchIllagerGeometry() {}

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        PartDefinition p0 = root.addOrReplaceChild("__cem_host_body1", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p1 = root.addOrReplaceChild("__cem_host_body2", CubeListBuilder.create(), PartPose.offsetAndRotation(-2.0F, 6.9F, -0.5F, 0.20420352F, 0.0F, 0.0F));
        PartDefinition p2 = root.addOrReplaceChild("__cem_host_body3", CubeListBuilder.create(), PartPose.offsetAndRotation(-2.0F, 16.6922281F, 1.52787293F, 0.83252203F, 0.0F, 0.0F));
        PartDefinition p3 = root.addOrReplaceChild("__cem_host_head1", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p4 = root.addOrReplaceChild("__cem_host_head2", CubeListBuilder.create(), PartPose.offsetAndRotation(-8.0F, 4.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p5 = root.addOrReplaceChild("__cem_host_head3", CubeListBuilder.create(), PartPose.offsetAndRotation(10.0F, 4.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p6 = p0.addOrReplaceChild("arch_illager", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 22.225F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p7 = p6.addOrReplaceChild("head4", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -10.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p8 = p6.addOrReplaceChild("body5", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p9 = p8.addOrReplaceChild("necklace", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p10 = p9.addOrReplaceChild("bone12", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -5.0F, -3.0F, 0.0F, 0.0F, -0.392699082F));
        PartDefinition p11 = p9.addOrReplaceChild("bone13", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -5.0F, -3.0F, 0.0F, 0.0F, 0.392699082F));
        PartDefinition p12 = p11.addOrReplaceChild("body1_sub_79", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p13 = p8.addOrReplaceChild("right_leg2", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p14 = p8.addOrReplaceChild("left_leg2", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p15 = p14.addOrReplaceChild("body1_sub_82", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p16 = p8.addOrReplaceChild("right_arm5", CubeListBuilder.create(), PartPose.offsetAndRotation(-4.0F, -8.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        PartDefinition p17 = p8.addOrReplaceChild("right_arm6", CubeListBuilder.create(), PartPose.offsetAndRotation(4.0F, -8.0F, 0.0F, 0.0F, 0.0F, -0.959931089F));
        PartDefinition p18 = p17.addOrReplaceChild("body1_sub_85", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p19 = p17.addOrReplaceChild("bone14", CubeListBuilder.create(), PartPose.offsetAndRotation(6.65168845F, 0.78236162F, -0.35F, 0.0F, 0.0F, 0.959931089F));
        PartDefinition p20 = p19.addOrReplaceChild("body1_sub_87", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition p21 = p19.addOrReplaceChild("da_arch_orb", CubeListBuilder.create(), PartPose.offsetAndRotation(0.5F, -6.5F, 0.0F, 0.13962634F, 0.785398163F, 0.13962634F));
        return LayerDefinition.create(mesh, 256, 256);
    }

    /** Preserve source expression units; static scales are reapplied after every pose reset. */
    public static ReferenceMobDefinition.Builder configure(ReferenceMobDefinition.Builder builder) {
        builder.cemDriver("wither");
        builder.bakedRoot(CemArchIllagerGeometry::bakeRoot);
        builder.hostAlias("body1", "__cem_host_body1");
        builder.hostAlias("body2", "__cem_host_body2");
        builder.hostAlias("body3", "__cem_host_body3");
        builder.hostAlias("head1", "__cem_host_head1");
        builder.hostAlias("head2", "__cem_host_head2");
        builder.hostAlias("head3", "__cem_host_head3");
        builder.boneScale("arch_illager", 0.5F);
        builder.expression("EMF_da_arch_orb.ry", "torad(45) + age * 0.012");
        builder.expression("EMF_da_arch_orb.ty", "-6.5 + sin(age * 0.09) * 0.12");
        builder.expression("EMF_right_leg2.rx", "sin(limb_swing * 0.6662) * limb_speed * 0.55");
        builder.expression("EMF_left_leg2.rx", "-sin(limb_swing * 0.6662) * limb_speed * 0.55");
        builder.expression("EMF_right_arm5.rx", "-sin(swing_progress * pi) * torad(80) + sin(limb_swing * 0.6662) * limb_speed * 0.25");
        return builder;
    }

    /** Complete matching rig; installed cubes stay owned by the Minecraft client. */
    public static net.minecraft.client.model.geom.ModelPart bakeRoot() {
        var nativeRoot = net.minecraft.client.model.WitherBossModel.createBodyLayer(CubeDeformation.NONE).bakeRoot();
        var nativeCubes = new java.util.HashMap<String, java.util.List<net.minecraft.client.model.geom.ModelPart.Cube>>();
        nativeRoot.visit(new com.mojang.blaze3d.vertex.PoseStack(),
                (pose, path, index, cube) -> nativeCubes.computeIfAbsent(path, key -> new java.util.ArrayList<>()).add(cube));
        var customCubes = new java.util.HashMap<String, java.util.List<net.minecraft.client.model.geom.ModelPart.Cube>>();
        createBodyLayer().bakeRoot().visit(new com.mojang.blaze3d.vertex.PoseStack(),
                (pose, path, index, cube) -> customCubes.computeIfAbsent(path, key -> new java.util.ArrayList<>()).add(cube));
        var p21Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body1/arch_illager/body5/right_arm6/bone14/da_arch_orb", java.util.List.of()));
        p21Cubes.add(new ReferenceMeshCube(-2.5F, -2.5F, -2.5F, 5.0F, 5.0F, 5.0F, new float[][][] {
                {{2.5F, -2.5F, 2.5F, 0.54296875F, 0.0078125F}, {-2.5F, -2.5F, 2.5F, 0.533203125F, 0.0078125F}, {-2.5F, -2.5F, -2.5F, 0.533203125F, 0.017578125F}, {2.5F, -2.5F, -2.5F, 0.54296875F, 0.017578125F}},
                {{2.5F, 2.5F, -2.5F, 0.552734375F, 0.017578125F}, {-2.5F, 2.5F, -2.5F, 0.54296875F, 0.017578125F}, {-2.5F, 2.5F, 2.5F, 0.54296875F, 0.0078125F}, {2.5F, 2.5F, 2.5F, 0.552734375F, 0.0078125F}},
                {{-2.5F, -2.5F, -2.5F, 0.533203125F, 0.017578125F}, {-2.5F, -2.5F, 2.5F, 0.5234375F, 0.017578125F}, {-2.5F, 2.5F, 2.5F, 0.5234375F, 0.02734375F}, {-2.5F, 2.5F, -2.5F, 0.533203125F, 0.02734375F}},
                {{2.5F, -2.5F, 2.5F, 0.552734375F, 0.017578125F}, {2.5F, -2.5F, -2.5F, 0.54296875F, 0.017578125F}, {2.5F, 2.5F, -2.5F, 0.54296875F, 0.02734375F}, {2.5F, 2.5F, 2.5F, 0.552734375F, 0.02734375F}},
                {{2.5F, -2.5F, -2.5F, 0.54296875F, 0.017578125F}, {-2.5F, -2.5F, -2.5F, 0.533203125F, 0.017578125F}, {-2.5F, 2.5F, -2.5F, 0.533203125F, 0.02734375F}, {2.5F, 2.5F, -2.5F, 0.54296875F, 0.02734375F}},
                {{-2.5F, -2.5F, 2.5F, 0.5625F, 0.017578125F}, {2.5F, -2.5F, 2.5F, 0.552734375F, 0.017578125F}, {2.5F, 2.5F, 2.5F, 0.552734375F, 0.02734375F}, {-2.5F, 2.5F, 2.5F, 0.5625F, 0.02734375F}}
        }));
        var p21 = new net.minecraft.client.model.geom.ModelPart(p21Cubes, java.util.Map.ofEntries());
        p21.setInitialPose(PartPose.offsetAndRotation(0.5F, -6.5F, 0.0F, 0.13962634F, 0.785398163F, 0.13962634F)); p21.resetPose();
        var p20Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body1/arch_illager/body5/right_arm6/bone14/body1_sub_87", java.util.List.of()));
        p20Cubes.add(new ReferenceMeshCube(0.0F, -2.0F, -0.5F, 1.0F, 18.0F, 1.0F, new float[][][] {
                {{0.0F, -2.0F, -0.5F, 0.234375F, 0.080078125F}, {1.0F, -2.0F, -0.5F, 0.232421875F, 0.080078125F}, {1.0F, -2.0F, 0.5F, 0.232421875F, 0.078125F}, {0.0F, -2.0F, 0.5F, 0.234375F, 0.078125F}},
                {{0.0F, 16.0F, 0.5F, 0.236328125F, 0.078125F}, {1.0F, 16.0F, 0.5F, 0.234375F, 0.078125F}, {1.0F, 16.0F, -0.5F, 0.234375F, 0.080078125F}, {0.0F, 16.0F, -0.5F, 0.236328125F, 0.080078125F}},
                {{1.0F, 16.0F, -0.5F, 0.232421875F, 0.115234375F}, {1.0F, 16.0F, 0.5F, 0.23046875F, 0.115234375F}, {1.0F, -2.0F, 0.5F, 0.23046875F, 0.080078125F}, {1.0F, -2.0F, -0.5F, 0.232421875F, 0.080078125F}},
                {{0.0F, 16.0F, 0.5F, 0.236328125F, 0.115234375F}, {0.0F, 16.0F, -0.5F, 0.234375F, 0.115234375F}, {0.0F, -2.0F, -0.5F, 0.234375F, 0.080078125F}, {0.0F, -2.0F, 0.5F, 0.236328125F, 0.080078125F}},
                {{0.0F, 16.0F, -0.5F, 0.234375F, 0.115234375F}, {1.0F, 16.0F, -0.5F, 0.232421875F, 0.115234375F}, {1.0F, -2.0F, -0.5F, 0.232421875F, 0.080078125F}, {0.0F, -2.0F, -0.5F, 0.234375F, 0.080078125F}},
                {{1.0F, 16.0F, 0.5F, 0.23828125F, 0.115234375F}, {0.0F, 16.0F, 0.5F, 0.236328125F, 0.115234375F}, {0.0F, -2.0F, 0.5F, 0.236328125F, 0.080078125F}, {1.0F, -2.0F, 0.5F, 0.23828125F, 0.080078125F}}
        }));
        p20Cubes.add(new ReferenceMeshCube(-1.0F, -3.0F, -1.5F, 3.0F, 1.0F, 3.0F, new float[][][] {
                {{-1.0F, -3.0F, -1.5F, 0.25F, 0.083984375F}, {2.0F, -3.0F, -1.5F, 0.244140625F, 0.083984375F}, {2.0F, -3.0F, 1.5F, 0.244140625F, 0.078125F}, {-1.0F, -3.0F, 1.5F, 0.25F, 0.078125F}},
                {{-1.0F, -2.0F, 1.5F, 0.255859375F, 0.078125F}, {2.0F, -2.0F, 1.5F, 0.25F, 0.078125F}, {2.0F, -2.0F, -1.5F, 0.25F, 0.083984375F}, {-1.0F, -2.0F, -1.5F, 0.255859375F, 0.083984375F}},
                {{2.0F, -2.0F, -1.5F, 0.244140625F, 0.0859375F}, {2.0F, -2.0F, 1.5F, 0.23828125F, 0.0859375F}, {2.0F, -3.0F, 1.5F, 0.23828125F, 0.083984375F}, {2.0F, -3.0F, -1.5F, 0.244140625F, 0.083984375F}},
                {{-1.0F, -2.0F, 1.5F, 0.255859375F, 0.0859375F}, {-1.0F, -2.0F, -1.5F, 0.25F, 0.0859375F}, {-1.0F, -3.0F, -1.5F, 0.25F, 0.083984375F}, {-1.0F, -3.0F, 1.5F, 0.255859375F, 0.083984375F}},
                {{-1.0F, -2.0F, -1.5F, 0.25F, 0.0859375F}, {2.0F, -2.0F, -1.5F, 0.244140625F, 0.0859375F}, {2.0F, -3.0F, -1.5F, 0.244140625F, 0.083984375F}, {-1.0F, -3.0F, -1.5F, 0.25F, 0.083984375F}},
                {{2.0F, -2.0F, 1.5F, 0.26171875F, 0.0859375F}, {-1.0F, -2.0F, 1.5F, 0.255859375F, 0.0859375F}, {-1.0F, -3.0F, 1.5F, 0.255859375F, 0.083984375F}, {2.0F, -3.0F, 1.5F, 0.26171875F, 0.083984375F}}
        }));
        var p20 = new net.minecraft.client.model.geom.ModelPart(p20Cubes, java.util.Map.ofEntries());
        p20.setInitialPose(PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p20.resetPose();
        var p19Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body1/arch_illager/body5/right_arm6/bone14", java.util.List.of()));
        var p19 = new net.minecraft.client.model.geom.ModelPart(p19Cubes, java.util.Map.ofEntries(java.util.Map.entry("body1_sub_87", p20), java.util.Map.entry("da_arch_orb", p21)));
        p19.setInitialPose(PartPose.offsetAndRotation(6.65168845F, 0.78236162F, -0.35F, 0.0F, 0.0F, 0.959931089F)); p19.resetPose();
        var p18Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body1/arch_illager/body5/right_arm6/body1_sub_85", java.util.List.of()));
        p18Cubes.add(new ReferenceMeshCube(0.0F, -2.0F, -1.5F, 3.0F, 4.0F, 3.0F, new float[][][] {
                {{0.0F, -2.0F, -1.5F, 0.294921875F, 0.078125F}, {3.0F, -2.0F, -1.5F, 0.2890625F, 0.078125F}, {3.0F, -2.0F, 1.5F, 0.2890625F, 0.072265625F}, {0.0F, -2.0F, 1.5F, 0.294921875F, 0.072265625F}},
                {{0.0F, 2.0F, 1.5F, 0.30078125F, 0.072265625F}, {3.0F, 2.0F, 1.5F, 0.294921875F, 0.072265625F}, {3.0F, 2.0F, -1.5F, 0.294921875F, 0.078125F}, {0.0F, 2.0F, -1.5F, 0.30078125F, 0.078125F}},
                {{3.0F, 2.0F, -1.5F, 0.2890625F, 0.0859375F}, {3.0F, 2.0F, 1.5F, 0.283203125F, 0.0859375F}, {3.0F, -2.0F, 1.5F, 0.283203125F, 0.078125F}, {3.0F, -2.0F, -1.5F, 0.2890625F, 0.078125F}},
                {{0.0F, 2.0F, 1.5F, 0.30078125F, 0.0859375F}, {0.0F, 2.0F, -1.5F, 0.294921875F, 0.0859375F}, {0.0F, -2.0F, -1.5F, 0.294921875F, 0.078125F}, {0.0F, -2.0F, 1.5F, 0.30078125F, 0.078125F}},
                {{0.0F, 2.0F, -1.5F, 0.294921875F, 0.0859375F}, {3.0F, 2.0F, -1.5F, 0.2890625F, 0.0859375F}, {3.0F, -2.0F, -1.5F, 0.2890625F, 0.078125F}, {0.0F, -2.0F, -1.5F, 0.294921875F, 0.078125F}},
                {{3.0F, 2.0F, 1.5F, 0.306640625F, 0.0859375F}, {0.0F, 2.0F, 1.5F, 0.30078125F, 0.0859375F}, {0.0F, -2.0F, 1.5F, 0.30078125F, 0.078125F}, {3.0F, -2.0F, 1.5F, 0.306640625F, 0.078125F}}
        }));
        p18Cubes.add(new ReferenceMeshCube(1.0F, -2.0F, -0.5F, 1.0F, 8.0F, 1.0F, new float[][][] {
                {{1.0F, -2.0F, -0.5F, 0.24609375F, 0.03515625F}, {2.0F, -2.0F, -0.5F, 0.244140625F, 0.03515625F}, {2.0F, -2.0F, 0.5F, 0.244140625F, 0.033203125F}, {1.0F, -2.0F, 0.5F, 0.24609375F, 0.033203125F}},
                {{1.0F, 6.0F, 0.5F, 0.248046875F, 0.033203125F}, {2.0F, 6.0F, 0.5F, 0.24609375F, 0.033203125F}, {2.0F, 6.0F, -0.5F, 0.24609375F, 0.03515625F}, {1.0F, 6.0F, -0.5F, 0.248046875F, 0.03515625F}},
                {{2.0F, 6.0F, -0.5F, 0.244140625F, 0.05078125F}, {2.0F, 6.0F, 0.5F, 0.2421875F, 0.05078125F}, {2.0F, -2.0F, 0.5F, 0.2421875F, 0.03515625F}, {2.0F, -2.0F, -0.5F, 0.244140625F, 0.03515625F}},
                {{1.0F, 6.0F, 0.5F, 0.248046875F, 0.05078125F}, {1.0F, 6.0F, -0.5F, 0.24609375F, 0.05078125F}, {1.0F, -2.0F, -0.5F, 0.24609375F, 0.03515625F}, {1.0F, -2.0F, 0.5F, 0.248046875F, 0.03515625F}},
                {{1.0F, 6.0F, -0.5F, 0.24609375F, 0.05078125F}, {2.0F, 6.0F, -0.5F, 0.244140625F, 0.05078125F}, {2.0F, -2.0F, -0.5F, 0.244140625F, 0.03515625F}, {1.0F, -2.0F, -0.5F, 0.24609375F, 0.03515625F}},
                {{2.0F, 6.0F, 0.5F, 0.25F, 0.05078125F}, {1.0F, 6.0F, 0.5F, 0.248046875F, 0.05078125F}, {1.0F, -2.0F, 0.5F, 0.248046875F, 0.03515625F}, {2.0F, -2.0F, 0.5F, 0.25F, 0.03515625F}}
        }));
        var p18 = new net.minecraft.client.model.geom.ModelPart(p18Cubes, java.util.Map.ofEntries());
        p18.setInitialPose(PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p18.resetPose();
        var p17Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body1/arch_illager/body5/right_arm6", java.util.List.of()));
        var p17 = new net.minecraft.client.model.geom.ModelPart(p17Cubes, java.util.Map.ofEntries(java.util.Map.entry("body1_sub_85", p18), java.util.Map.entry("bone14", p19)));
        p17.setInitialPose(PartPose.offsetAndRotation(4.0F, -8.0F, 0.0F, 0.0F, 0.0F, -0.959931089F)); p17.resetPose();
        var p16Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body1/arch_illager/body5/right_arm5", java.util.List.of()));
        p16Cubes.add(new ReferenceMeshCube(-3.0F, -2.0F, -1.5F, 3.0F, 4.0F, 3.0F, new float[][][] {
                {{0.0F, -2.0F, 1.5F, 0.294921875F, 0.072265625F}, {-3.0F, -2.0F, 1.5F, 0.2890625F, 0.072265625F}, {-3.0F, -2.0F, -1.5F, 0.2890625F, 0.078125F}, {0.0F, -2.0F, -1.5F, 0.294921875F, 0.078125F}},
                {{0.0F, 2.0F, -1.5F, 0.30078125F, 0.078125F}, {-3.0F, 2.0F, -1.5F, 0.294921875F, 0.078125F}, {-3.0F, 2.0F, 1.5F, 0.294921875F, 0.072265625F}, {0.0F, 2.0F, 1.5F, 0.30078125F, 0.072265625F}},
                {{-3.0F, -2.0F, -1.5F, 0.2890625F, 0.078125F}, {-3.0F, -2.0F, 1.5F, 0.283203125F, 0.078125F}, {-3.0F, 2.0F, 1.5F, 0.283203125F, 0.0859375F}, {-3.0F, 2.0F, -1.5F, 0.2890625F, 0.0859375F}},
                {{0.0F, -2.0F, 1.5F, 0.30078125F, 0.078125F}, {0.0F, -2.0F, -1.5F, 0.294921875F, 0.078125F}, {0.0F, 2.0F, -1.5F, 0.294921875F, 0.0859375F}, {0.0F, 2.0F, 1.5F, 0.30078125F, 0.0859375F}},
                {{0.0F, -2.0F, -1.5F, 0.294921875F, 0.078125F}, {-3.0F, -2.0F, -1.5F, 0.2890625F, 0.078125F}, {-3.0F, 2.0F, -1.5F, 0.2890625F, 0.0859375F}, {0.0F, 2.0F, -1.5F, 0.294921875F, 0.0859375F}},
                {{-3.0F, -2.0F, 1.5F, 0.306640625F, 0.078125F}, {0.0F, -2.0F, 1.5F, 0.30078125F, 0.078125F}, {0.0F, 2.0F, 1.5F, 0.30078125F, 0.0859375F}, {-3.0F, 2.0F, 1.5F, 0.306640625F, 0.0859375F}}
        }));
        p16Cubes.add(new ReferenceMeshCube(-2.0F, -2.0F, -0.5F, 1.0F, 8.0F, 1.0F, new float[][][] {
                {{-1.0F, -2.0F, 0.5F, 0.24609375F, 0.033203125F}, {-2.0F, -2.0F, 0.5F, 0.244140625F, 0.033203125F}, {-2.0F, -2.0F, -0.5F, 0.244140625F, 0.03515625F}, {-1.0F, -2.0F, -0.5F, 0.24609375F, 0.03515625F}},
                {{-1.0F, 6.0F, -0.5F, 0.248046875F, 0.03515625F}, {-2.0F, 6.0F, -0.5F, 0.24609375F, 0.03515625F}, {-2.0F, 6.0F, 0.5F, 0.24609375F, 0.033203125F}, {-1.0F, 6.0F, 0.5F, 0.248046875F, 0.033203125F}},
                {{-2.0F, -2.0F, -0.5F, 0.244140625F, 0.03515625F}, {-2.0F, -2.0F, 0.5F, 0.2421875F, 0.03515625F}, {-2.0F, 6.0F, 0.5F, 0.2421875F, 0.05078125F}, {-2.0F, 6.0F, -0.5F, 0.244140625F, 0.05078125F}},
                {{-1.0F, -2.0F, 0.5F, 0.248046875F, 0.03515625F}, {-1.0F, -2.0F, -0.5F, 0.24609375F, 0.03515625F}, {-1.0F, 6.0F, -0.5F, 0.24609375F, 0.05078125F}, {-1.0F, 6.0F, 0.5F, 0.248046875F, 0.05078125F}},
                {{-1.0F, -2.0F, -0.5F, 0.24609375F, 0.03515625F}, {-2.0F, -2.0F, -0.5F, 0.244140625F, 0.03515625F}, {-2.0F, 6.0F, -0.5F, 0.244140625F, 0.05078125F}, {-1.0F, 6.0F, -0.5F, 0.24609375F, 0.05078125F}},
                {{-2.0F, -2.0F, 0.5F, 0.25F, 0.03515625F}, {-1.0F, -2.0F, 0.5F, 0.248046875F, 0.03515625F}, {-1.0F, 6.0F, 0.5F, 0.248046875F, 0.05078125F}, {-2.0F, 6.0F, 0.5F, 0.25F, 0.05078125F}}
        }));
        var p16 = new net.minecraft.client.model.geom.ModelPart(p16Cubes, java.util.Map.ofEntries());
        p16.setInitialPose(PartPose.offsetAndRotation(-4.0F, -8.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p16.resetPose();
        var p15Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body1/arch_illager/body5/left_leg2/body1_sub_82", java.util.List.of()));
        p15Cubes.add(new ReferenceMeshCube(0.0F, -3.0F, -1.5F, 3.0F, 3.0F, 3.0F, new float[][][] {
                {{0.0F, -3.0F, -1.5F, 0.26171875F, 0.0390625F}, {3.0F, -3.0F, -1.5F, 0.255859375F, 0.0390625F}, {3.0F, -3.0F, 1.5F, 0.255859375F, 0.033203125F}, {0.0F, -3.0F, 1.5F, 0.26171875F, 0.033203125F}},
                {{0.0F, 0.0F, 1.5F, 0.267578125F, 0.033203125F}, {3.0F, 0.0F, 1.5F, 0.26171875F, 0.033203125F}, {3.0F, 0.0F, -1.5F, 0.26171875F, 0.0390625F}, {0.0F, 0.0F, -1.5F, 0.267578125F, 0.0390625F}},
                {{3.0F, 0.0F, -1.5F, 0.255859375F, 0.044921875F}, {3.0F, 0.0F, 1.5F, 0.25F, 0.044921875F}, {3.0F, -3.0F, 1.5F, 0.25F, 0.0390625F}, {3.0F, -3.0F, -1.5F, 0.255859375F, 0.0390625F}},
                {{0.0F, 0.0F, 1.5F, 0.267578125F, 0.044921875F}, {0.0F, 0.0F, -1.5F, 0.26171875F, 0.044921875F}, {0.0F, -3.0F, -1.5F, 0.26171875F, 0.0390625F}, {0.0F, -3.0F, 1.5F, 0.267578125F, 0.0390625F}},
                {{0.0F, 0.0F, -1.5F, 0.26171875F, 0.044921875F}, {3.0F, 0.0F, -1.5F, 0.255859375F, 0.044921875F}, {3.0F, -3.0F, -1.5F, 0.255859375F, 0.0390625F}, {0.0F, -3.0F, -1.5F, 0.26171875F, 0.0390625F}},
                {{3.0F, 0.0F, 1.5F, 0.2734375F, 0.044921875F}, {0.0F, 0.0F, 1.5F, 0.267578125F, 0.044921875F}, {0.0F, -3.0F, 1.5F, 0.267578125F, 0.0390625F}, {3.0F, -3.0F, 1.5F, 0.2734375F, 0.0390625F}}
        }));
        p15Cubes.add(new ReferenceMeshCube(1.0F, 0.0F, -0.5F, 1.0F, 3.0F, 1.0F, new float[][][] {
                {{1.0F, 0.0F, -0.5F, 0.234375F, 0.044921875F}, {2.0F, 0.0F, -0.5F, 0.232421875F, 0.044921875F}, {2.0F, 0.0F, 0.5F, 0.232421875F, 0.04296875F}, {1.0F, 0.0F, 0.5F, 0.234375F, 0.04296875F}},
                {{1.0F, 3.0F, 0.5F, 0.236328125F, 0.04296875F}, {2.0F, 3.0F, 0.5F, 0.234375F, 0.04296875F}, {2.0F, 3.0F, -0.5F, 0.234375F, 0.044921875F}, {1.0F, 3.0F, -0.5F, 0.236328125F, 0.044921875F}},
                {{2.0F, 3.0F, -0.5F, 0.232421875F, 0.05078125F}, {2.0F, 3.0F, 0.5F, 0.23046875F, 0.05078125F}, {2.0F, 0.0F, 0.5F, 0.23046875F, 0.044921875F}, {2.0F, 0.0F, -0.5F, 0.232421875F, 0.044921875F}},
                {{1.0F, 3.0F, 0.5F, 0.236328125F, 0.05078125F}, {1.0F, 3.0F, -0.5F, 0.234375F, 0.05078125F}, {1.0F, 0.0F, -0.5F, 0.234375F, 0.044921875F}, {1.0F, 0.0F, 0.5F, 0.236328125F, 0.044921875F}},
                {{1.0F, 3.0F, -0.5F, 0.234375F, 0.05078125F}, {2.0F, 3.0F, -0.5F, 0.232421875F, 0.05078125F}, {2.0F, 0.0F, -0.5F, 0.232421875F, 0.044921875F}, {1.0F, 0.0F, -0.5F, 0.234375F, 0.044921875F}},
                {{2.0F, 3.0F, 0.5F, 0.23828125F, 0.05078125F}, {1.0F, 3.0F, 0.5F, 0.236328125F, 0.05078125F}, {1.0F, 0.0F, 0.5F, 0.236328125F, 0.044921875F}, {2.0F, 0.0F, 0.5F, 0.23828125F, 0.044921875F}}
        }));
        p15Cubes.add(new ReferenceMeshCube(1.0F, 3.0F, -2.5F, 1.0F, 0.0F, 3.0F, new float[][][] {
                {{1.0F, 3.0F, -2.5F, 0.240234375F, 0.048828125F}, {2.0F, 3.0F, -2.5F, 0.23828125F, 0.048828125F}, {2.0F, 3.0F, 0.5F, 0.23828125F, 0.04296875F}, {1.0F, 3.0F, 0.5F, 0.240234375F, 0.04296875F}},
                {{1.0F, 3.0F, 0.5F, 0.2421875F, 0.04296875F}, {2.0F, 3.0F, 0.5F, 0.240234375F, 0.04296875F}, {2.0F, 3.0F, -2.5F, 0.240234375F, 0.048828125F}, {1.0F, 3.0F, -2.5F, 0.2421875F, 0.048828125F}},
                {{2.0F, 3.0F, -2.5F, 0.23828125F, 0.048828125F}, {2.0F, 3.0F, 0.5F, 0.232421875F, 0.048828125F}, {2.0F, 3.0F, 0.5F, 0.232421875F, 0.048828125F}, {2.0F, 3.0F, -2.5F, 0.23828125F, 0.048828125F}},
                {{1.0F, 3.0F, 0.5F, 0.24609375F, 0.048828125F}, {1.0F, 3.0F, -2.5F, 0.240234375F, 0.048828125F}, {1.0F, 3.0F, -2.5F, 0.240234375F, 0.048828125F}, {1.0F, 3.0F, 0.5F, 0.24609375F, 0.048828125F}},
                {{1.0F, 3.0F, -2.5F, 0.240234375F, 0.048828125F}, {2.0F, 3.0F, -2.5F, 0.23828125F, 0.048828125F}, {2.0F, 3.0F, -2.5F, 0.23828125F, 0.048828125F}, {1.0F, 3.0F, -2.5F, 0.240234375F, 0.048828125F}},
                {{2.0F, 3.0F, 0.5F, 0.248046875F, 0.048828125F}, {1.0F, 3.0F, 0.5F, 0.24609375F, 0.048828125F}, {1.0F, 3.0F, 0.5F, 0.24609375F, 0.048828125F}, {2.0F, 3.0F, 0.5F, 0.248046875F, 0.048828125F}}
        }));
        var p15 = new net.minecraft.client.model.geom.ModelPart(p15Cubes, java.util.Map.ofEntries());
        p15.setInitialPose(PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p15.resetPose();
        var p14Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body1/arch_illager/body5/left_leg2", java.util.List.of()));
        var p14 = new net.minecraft.client.model.geom.ModelPart(p14Cubes, java.util.Map.ofEntries(java.util.Map.entry("body1_sub_82", p15)));
        p14.setInitialPose(PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p14.resetPose();
        var p13Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body1/arch_illager/body5/right_leg2", java.util.List.of()));
        p13Cubes.add(new ReferenceMeshCube(-3.0F, -3.0F, -1.5F, 3.0F, 3.0F, 3.0F, new float[][][] {
                {{0.0F, -3.0F, 1.5F, 0.26171875F, 0.033203125F}, {-3.0F, -3.0F, 1.5F, 0.255859375F, 0.033203125F}, {-3.0F, -3.0F, -1.5F, 0.255859375F, 0.0390625F}, {0.0F, -3.0F, -1.5F, 0.26171875F, 0.0390625F}},
                {{0.0F, 0.0F, -1.5F, 0.267578125F, 0.0390625F}, {-3.0F, 0.0F, -1.5F, 0.26171875F, 0.0390625F}, {-3.0F, 0.0F, 1.5F, 0.26171875F, 0.033203125F}, {0.0F, 0.0F, 1.5F, 0.267578125F, 0.033203125F}},
                {{-3.0F, -3.0F, -1.5F, 0.255859375F, 0.0390625F}, {-3.0F, -3.0F, 1.5F, 0.25F, 0.0390625F}, {-3.0F, 0.0F, 1.5F, 0.25F, 0.044921875F}, {-3.0F, 0.0F, -1.5F, 0.255859375F, 0.044921875F}},
                {{0.0F, -3.0F, 1.5F, 0.267578125F, 0.0390625F}, {0.0F, -3.0F, -1.5F, 0.26171875F, 0.0390625F}, {0.0F, 0.0F, -1.5F, 0.26171875F, 0.044921875F}, {0.0F, 0.0F, 1.5F, 0.267578125F, 0.044921875F}},
                {{0.0F, -3.0F, -1.5F, 0.26171875F, 0.0390625F}, {-3.0F, -3.0F, -1.5F, 0.255859375F, 0.0390625F}, {-3.0F, 0.0F, -1.5F, 0.255859375F, 0.044921875F}, {0.0F, 0.0F, -1.5F, 0.26171875F, 0.044921875F}},
                {{-3.0F, -3.0F, 1.5F, 0.2734375F, 0.0390625F}, {0.0F, -3.0F, 1.5F, 0.267578125F, 0.0390625F}, {0.0F, 0.0F, 1.5F, 0.267578125F, 0.044921875F}, {-3.0F, 0.0F, 1.5F, 0.2734375F, 0.044921875F}}
        }));
        p13Cubes.add(new ReferenceMeshCube(-2.0F, 0.0F, -0.5F, 1.0F, 3.0F, 1.0F, new float[][][] {
                {{-1.0F, 0.0F, 0.5F, 0.234375F, 0.04296875F}, {-2.0F, 0.0F, 0.5F, 0.232421875F, 0.04296875F}, {-2.0F, 0.0F, -0.5F, 0.232421875F, 0.044921875F}, {-1.0F, 0.0F, -0.5F, 0.234375F, 0.044921875F}},
                {{-1.0F, 3.0F, -0.5F, 0.236328125F, 0.044921875F}, {-2.0F, 3.0F, -0.5F, 0.234375F, 0.044921875F}, {-2.0F, 3.0F, 0.5F, 0.234375F, 0.04296875F}, {-1.0F, 3.0F, 0.5F, 0.236328125F, 0.04296875F}},
                {{-2.0F, 0.0F, -0.5F, 0.232421875F, 0.044921875F}, {-2.0F, 0.0F, 0.5F, 0.23046875F, 0.044921875F}, {-2.0F, 3.0F, 0.5F, 0.23046875F, 0.05078125F}, {-2.0F, 3.0F, -0.5F, 0.232421875F, 0.05078125F}},
                {{-1.0F, 0.0F, 0.5F, 0.236328125F, 0.044921875F}, {-1.0F, 0.0F, -0.5F, 0.234375F, 0.044921875F}, {-1.0F, 3.0F, -0.5F, 0.234375F, 0.05078125F}, {-1.0F, 3.0F, 0.5F, 0.236328125F, 0.05078125F}},
                {{-1.0F, 0.0F, -0.5F, 0.234375F, 0.044921875F}, {-2.0F, 0.0F, -0.5F, 0.232421875F, 0.044921875F}, {-2.0F, 3.0F, -0.5F, 0.232421875F, 0.05078125F}, {-1.0F, 3.0F, -0.5F, 0.234375F, 0.05078125F}},
                {{-2.0F, 0.0F, 0.5F, 0.23828125F, 0.044921875F}, {-1.0F, 0.0F, 0.5F, 0.236328125F, 0.044921875F}, {-1.0F, 3.0F, 0.5F, 0.236328125F, 0.05078125F}, {-2.0F, 3.0F, 0.5F, 0.23828125F, 0.05078125F}}
        }));
        p13Cubes.add(new ReferenceMeshCube(-2.0F, 3.0F, -2.5F, 1.0F, 0.0F, 3.0F, new float[][][] {
                {{-1.0F, 3.0F, 0.5F, 0.240234375F, 0.04296875F}, {-2.0F, 3.0F, 0.5F, 0.23828125F, 0.04296875F}, {-2.0F, 3.0F, -2.5F, 0.23828125F, 0.048828125F}, {-1.0F, 3.0F, -2.5F, 0.240234375F, 0.048828125F}},
                {{-1.0F, 3.0F, -2.5F, 0.2421875F, 0.048828125F}, {-2.0F, 3.0F, -2.5F, 0.240234375F, 0.048828125F}, {-2.0F, 3.0F, 0.5F, 0.240234375F, 0.04296875F}, {-1.0F, 3.0F, 0.5F, 0.2421875F, 0.04296875F}},
                {{-2.0F, 3.0F, -2.5F, 0.23828125F, 0.048828125F}, {-2.0F, 3.0F, 0.5F, 0.232421875F, 0.048828125F}, {-2.0F, 3.0F, 0.5F, 0.232421875F, 0.048828125F}, {-2.0F, 3.0F, -2.5F, 0.23828125F, 0.048828125F}},
                {{-1.0F, 3.0F, 0.5F, 0.24609375F, 0.048828125F}, {-1.0F, 3.0F, -2.5F, 0.240234375F, 0.048828125F}, {-1.0F, 3.0F, -2.5F, 0.240234375F, 0.048828125F}, {-1.0F, 3.0F, 0.5F, 0.24609375F, 0.048828125F}},
                {{-1.0F, 3.0F, -2.5F, 0.240234375F, 0.048828125F}, {-2.0F, 3.0F, -2.5F, 0.23828125F, 0.048828125F}, {-2.0F, 3.0F, -2.5F, 0.23828125F, 0.048828125F}, {-1.0F, 3.0F, -2.5F, 0.240234375F, 0.048828125F}},
                {{-2.0F, 3.0F, 0.5F, 0.248046875F, 0.048828125F}, {-1.0F, 3.0F, 0.5F, 0.24609375F, 0.048828125F}, {-1.0F, 3.0F, 0.5F, 0.24609375F, 0.048828125F}, {-2.0F, 3.0F, 0.5F, 0.248046875F, 0.048828125F}}
        }));
        var p13 = new net.minecraft.client.model.geom.ModelPart(p13Cubes, java.util.Map.ofEntries());
        p13.setInitialPose(PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p13.resetPose();
        var p12Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body1/arch_illager/body5/necklace/bone13/body1_sub_79", java.util.List.of()));
        p12Cubes.add(new ReferenceMeshCube(-0.5F, -3.5F, 0.975F, 2.0F, 3.0F, 0.0F, new float[][][] {
                {{-0.5F, -3.5F, 0.975F, 0.28125F, 0.0390625F}, {1.5F, -3.5F, 0.975F, 0.275390625F, 0.0390625F}, {1.5F, -3.5F, 0.975F, 0.275390625F, 0.037109375F}, {-0.5F, -3.5F, 0.975F, 0.28125F, 0.037109375F}},
                {{-0.5F, -0.5F, 0.975F, 0.287109375F, 0.037109375F}, {1.5F, -0.5F, 0.975F, 0.28125F, 0.037109375F}, {1.5F, -0.5F, 0.975F, 0.28125F, 0.0390625F}, {-0.5F, -0.5F, 0.975F, 0.287109375F, 0.0390625F}},
                {{1.5F, -0.5F, 0.975F, 0.275390625F, 0.046875F}, {1.5F, -0.5F, 0.975F, 0.2734375F, 0.046875F}, {1.5F, -3.5F, 0.975F, 0.2734375F, 0.0390625F}, {1.5F, -3.5F, 0.975F, 0.275390625F, 0.0390625F}},
                {{-0.5F, -0.5F, 0.975F, 0.283203125F, 0.046875F}, {-0.5F, -0.5F, 0.975F, 0.28125F, 0.046875F}, {-0.5F, -3.5F, 0.975F, 0.28125F, 0.0390625F}, {-0.5F, -3.5F, 0.975F, 0.283203125F, 0.0390625F}},
                {{-0.5F, -0.5F, 0.975F, 0.28125F, 0.046875F}, {1.5F, -0.5F, 0.975F, 0.275390625F, 0.046875F}, {1.5F, -3.5F, 0.975F, 0.275390625F, 0.0390625F}, {-0.5F, -3.5F, 0.975F, 0.28125F, 0.0390625F}},
                {{1.5F, -0.5F, 0.975F, 0.2890625F, 0.046875F}, {-0.5F, -0.5F, 0.975F, 0.283203125F, 0.046875F}, {-0.5F, -3.5F, 0.975F, 0.283203125F, 0.0390625F}, {1.5F, -3.5F, 0.975F, 0.2890625F, 0.0390625F}}
        }));
        var p12 = new net.minecraft.client.model.geom.ModelPart(p12Cubes, java.util.Map.ofEntries());
        p12.setInitialPose(PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p12.resetPose();
        var p11Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body1/arch_illager/body5/necklace/bone13", java.util.List.of()));
        var p11 = new net.minecraft.client.model.geom.ModelPart(p11Cubes, java.util.Map.ofEntries(java.util.Map.entry("body1_sub_79", p12)));
        p11.setInitialPose(PartPose.offsetAndRotation(0.0F, -5.0F, -3.0F, 0.0F, 0.0F, 0.392699082F)); p11.resetPose();
        var p10Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body1/arch_illager/body5/necklace/bone12", java.util.List.of()));
        p10Cubes.add(new ReferenceMeshCube(-1.5F, -3.5F, 0.975F, 2.0F, 3.0F, 0.0F, new float[][][] {
                {{0.5F, -3.5F, 0.975F, 0.28125F, 0.037109375F}, {-1.5F, -3.5F, 0.975F, 0.275390625F, 0.037109375F}, {-1.5F, -3.5F, 0.975F, 0.275390625F, 0.0390625F}, {0.5F, -3.5F, 0.975F, 0.28125F, 0.0390625F}},
                {{0.5F, -0.5F, 0.975F, 0.287109375F, 0.0390625F}, {-1.5F, -0.5F, 0.975F, 0.28125F, 0.0390625F}, {-1.5F, -0.5F, 0.975F, 0.28125F, 0.037109375F}, {0.5F, -0.5F, 0.975F, 0.287109375F, 0.037109375F}},
                {{-1.5F, -3.5F, 0.975F, 0.275390625F, 0.0390625F}, {-1.5F, -3.5F, 0.975F, 0.2734375F, 0.0390625F}, {-1.5F, -0.5F, 0.975F, 0.2734375F, 0.046875F}, {-1.5F, -0.5F, 0.975F, 0.275390625F, 0.046875F}},
                {{0.5F, -3.5F, 0.975F, 0.283203125F, 0.0390625F}, {0.5F, -3.5F, 0.975F, 0.28125F, 0.0390625F}, {0.5F, -0.5F, 0.975F, 0.28125F, 0.046875F}, {0.5F, -0.5F, 0.975F, 0.283203125F, 0.046875F}},
                {{0.5F, -3.5F, 0.975F, 0.28125F, 0.0390625F}, {-1.5F, -3.5F, 0.975F, 0.275390625F, 0.0390625F}, {-1.5F, -0.5F, 0.975F, 0.275390625F, 0.046875F}, {0.5F, -0.5F, 0.975F, 0.28125F, 0.046875F}},
                {{-1.5F, -3.5F, 0.975F, 0.2890625F, 0.0390625F}, {0.5F, -3.5F, 0.975F, 0.283203125F, 0.0390625F}, {0.5F, -0.5F, 0.975F, 0.283203125F, 0.046875F}, {-1.5F, -0.5F, 0.975F, 0.2890625F, 0.046875F}}
        }));
        var p10 = new net.minecraft.client.model.geom.ModelPart(p10Cubes, java.util.Map.ofEntries());
        p10.setInitialPose(PartPose.offsetAndRotation(0.0F, -5.0F, -3.0F, 0.0F, 0.0F, -0.392699082F)); p10.resetPose();
        var p9Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body1/arch_illager/body5/necklace", java.util.List.of()));
        p9Cubes.add(new ReferenceMeshCube(-1.0F, -6.0F, -3.0F, 2.0F, 2.0F, 1.0F, new float[][][] {
                {{1.0F, -6.0F, -2.0F, 0.26953125F, 0.044921875F}, {-1.0F, -6.0F, -2.0F, 0.265625F, 0.044921875F}, {-1.0F, -6.0F, -3.0F, 0.265625F, 0.046875F}, {1.0F, -6.0F, -3.0F, 0.26953125F, 0.046875F}},
                {{1.0F, -4.0F, -3.0F, 0.2734375F, 0.046875F}, {-1.0F, -4.0F, -3.0F, 0.26953125F, 0.046875F}, {-1.0F, -4.0F, -2.0F, 0.26953125F, 0.044921875F}, {1.0F, -4.0F, -2.0F, 0.2734375F, 0.044921875F}},
                {{-1.0F, -6.0F, -3.0F, 0.265625F, 0.046875F}, {-1.0F, -6.0F, -2.0F, 0.263671875F, 0.046875F}, {-1.0F, -4.0F, -2.0F, 0.263671875F, 0.05078125F}, {-1.0F, -4.0F, -3.0F, 0.265625F, 0.05078125F}},
                {{1.0F, -6.0F, -2.0F, 0.271484375F, 0.046875F}, {1.0F, -6.0F, -3.0F, 0.26953125F, 0.046875F}, {1.0F, -4.0F, -3.0F, 0.26953125F, 0.05078125F}, {1.0F, -4.0F, -2.0F, 0.271484375F, 0.05078125F}},
                {{1.0F, -6.0F, -3.0F, 0.26953125F, 0.046875F}, {-1.0F, -6.0F, -3.0F, 0.265625F, 0.046875F}, {-1.0F, -4.0F, -3.0F, 0.265625F, 0.05078125F}, {1.0F, -4.0F, -3.0F, 0.26953125F, 0.05078125F}},
                {{-1.0F, -6.0F, -2.0F, 0.275390625F, 0.046875F}, {1.0F, -6.0F, -2.0F, 0.271484375F, 0.046875F}, {1.0F, -4.0F, -2.0F, 0.271484375F, 0.05078125F}, {-1.0F, -4.0F, -2.0F, 0.275390625F, 0.05078125F}}
        }));
        var p9 = new net.minecraft.client.model.geom.ModelPart(p9Cubes, java.util.Map.ofEntries(java.util.Map.entry("bone12", p10), java.util.Map.entry("bone13", p11)));
        p9.setInitialPose(PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p9.resetPose();
        var p8Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body1/arch_illager/body5", java.util.List.of()));
        p8Cubes.add(new ReferenceMeshCube(-3.0F, -10.0F, -2.0F, 6.0F, 7.0F, 4.0F, new float[][][] {
                {{3.0F, -10.0F, 2.0F, 0.25390625F, 0.052734375F}, {-3.0F, -10.0F, 2.0F, 0.2421875F, 0.052734375F}, {-3.0F, -10.0F, -2.0F, 0.2421875F, 0.060546875F}, {3.0F, -10.0F, -2.0F, 0.25390625F, 0.060546875F}},
                {{3.0F, -3.0F, -2.0F, 0.265625F, 0.060546875F}, {-3.0F, -3.0F, -2.0F, 0.25390625F, 0.060546875F}, {-3.0F, -3.0F, 2.0F, 0.25390625F, 0.052734375F}, {3.0F, -3.0F, 2.0F, 0.265625F, 0.052734375F}},
                {{-3.0F, -10.0F, -2.0F, 0.2421875F, 0.060546875F}, {-3.0F, -10.0F, 2.0F, 0.234375F, 0.060546875F}, {-3.0F, -3.0F, 2.0F, 0.234375F, 0.07421875F}, {-3.0F, -3.0F, -2.0F, 0.2421875F, 0.07421875F}},
                {{3.0F, -10.0F, 2.0F, 0.26171875F, 0.060546875F}, {3.0F, -10.0F, -2.0F, 0.25390625F, 0.060546875F}, {3.0F, -3.0F, -2.0F, 0.25390625F, 0.07421875F}, {3.0F, -3.0F, 2.0F, 0.26171875F, 0.07421875F}},
                {{3.0F, -10.0F, -2.0F, 0.25390625F, 0.060546875F}, {-3.0F, -10.0F, -2.0F, 0.2421875F, 0.060546875F}, {-3.0F, -3.0F, -2.0F, 0.2421875F, 0.07421875F}, {3.0F, -3.0F, -2.0F, 0.25390625F, 0.07421875F}},
                {{-3.0F, -10.0F, 2.0F, 0.2734375F, 0.060546875F}, {3.0F, -10.0F, 2.0F, 0.26171875F, 0.060546875F}, {3.0F, -3.0F, 2.0F, 0.26171875F, 0.07421875F}, {-3.0F, -3.0F, 2.0F, 0.2734375F, 0.07421875F}}
        }));
        p8Cubes.add(new ReferenceMeshCube(-4.05F, -10.55F, -2.05F, 8.1F, 14.1F, 4.1F, new float[][][] {
                {{4.05F, -10.55F, 2.05F, 0.31640625F, 0.037109375F}, {-4.05F, -10.55F, 2.05F, 0.30078125F, 0.037109375F}, {-4.05F, -10.55F, -2.05F, 0.30078125F, 0.044921875F}, {4.05F, -10.55F, -2.05F, 0.31640625F, 0.044921875F}},
                {{4.05F, 3.55F, -2.05F, 0.33203125F, 0.044921875F}, {-4.05F, 3.55F, -2.05F, 0.31640625F, 0.044921875F}, {-4.05F, 3.55F, 2.05F, 0.31640625F, 0.037109375F}, {4.05F, 3.55F, 2.05F, 0.33203125F, 0.037109375F}},
                {{-4.05F, -10.55F, -2.05F, 0.30078125F, 0.044921875F}, {-4.05F, -10.55F, 2.05F, 0.29296875F, 0.044921875F}, {-4.05F, 3.55F, 2.05F, 0.29296875F, 0.072265625F}, {-4.05F, 3.55F, -2.05F, 0.30078125F, 0.072265625F}},
                {{4.05F, -10.55F, 2.05F, 0.32421875F, 0.044921875F}, {4.05F, -10.55F, -2.05F, 0.31640625F, 0.044921875F}, {4.05F, 3.55F, -2.05F, 0.31640625F, 0.072265625F}, {4.05F, 3.55F, 2.05F, 0.32421875F, 0.072265625F}},
                {{4.05F, -10.55F, -2.05F, 0.31640625F, 0.044921875F}, {-4.05F, -10.55F, -2.05F, 0.30078125F, 0.044921875F}, {-4.05F, 3.55F, -2.05F, 0.30078125F, 0.072265625F}, {4.05F, 3.55F, -2.05F, 0.31640625F, 0.072265625F}},
                {{-4.05F, -10.55F, 2.05F, 0.33984375F, 0.044921875F}, {4.05F, -10.55F, 2.05F, 0.32421875F, 0.044921875F}, {4.05F, 3.55F, 2.05F, 0.32421875F, 0.072265625F}, {-4.05F, 3.55F, 2.05F, 0.33984375F, 0.072265625F}}
        }));
        var p8 = new net.minecraft.client.model.geom.ModelPart(p8Cubes, java.util.Map.ofEntries(java.util.Map.entry("necklace", p9), java.util.Map.entry("right_leg2", p13), java.util.Map.entry("left_leg2", p14), java.util.Map.entry("right_arm5", p16), java.util.Map.entry("right_arm6", p17)));
        p8.setInitialPose(PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p8.resetPose();
        var p7Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body1/arch_illager/head4", java.util.List.of()));
        p7Cubes.add(new ReferenceMeshCube(-6.0F, -7.0F, -3.0F, 12.0F, 7.0F, 6.0F, new float[][][] {
                {{6.0F, -7.0F, 3.0F, 0.265625F, 0.0078125F}, {-6.0F, -7.0F, 3.0F, 0.2421875F, 0.0078125F}, {-6.0F, -7.0F, -3.0F, 0.2421875F, 0.01953125F}, {6.0F, -7.0F, -3.0F, 0.265625F, 0.01953125F}},
                {{6.0F, 0.0F, -3.0F, 0.2890625F, 0.01953125F}, {-6.0F, 0.0F, -3.0F, 0.265625F, 0.01953125F}, {-6.0F, 0.0F, 3.0F, 0.265625F, 0.0078125F}, {6.0F, 0.0F, 3.0F, 0.2890625F, 0.0078125F}},
                {{-6.0F, -7.0F, -3.0F, 0.2421875F, 0.01953125F}, {-6.0F, -7.0F, 3.0F, 0.23046875F, 0.01953125F}, {-6.0F, 0.0F, 3.0F, 0.23046875F, 0.033203125F}, {-6.0F, 0.0F, -3.0F, 0.2421875F, 0.033203125F}},
                {{6.0F, -7.0F, 3.0F, 0.27734375F, 0.01953125F}, {6.0F, -7.0F, -3.0F, 0.265625F, 0.01953125F}, {6.0F, 0.0F, -3.0F, 0.265625F, 0.033203125F}, {6.0F, 0.0F, 3.0F, 0.27734375F, 0.033203125F}},
                {{6.0F, -7.0F, -3.0F, 0.265625F, 0.01953125F}, {-6.0F, -7.0F, -3.0F, 0.2421875F, 0.01953125F}, {-6.0F, 0.0F, -3.0F, 0.2421875F, 0.033203125F}, {6.0F, 0.0F, -3.0F, 0.265625F, 0.033203125F}},
                {{-6.0F, -7.0F, 3.0F, 0.30078125F, 0.01953125F}, {6.0F, -7.0F, 3.0F, 0.27734375F, 0.01953125F}, {6.0F, 0.0F, 3.0F, 0.27734375F, 0.033203125F}, {-6.0F, 0.0F, 3.0F, 0.30078125F, 0.033203125F}}
        }));
        p7Cubes.add(new ReferenceMeshCube(-2.0F, -18.0F, -2.0F, 4.0F, 11.0F, 4.0F, new float[][][] {
                {{2.0F, -18.0F, 2.0F, 0.31640625F, 0.0078125F}, {-2.0F, -18.0F, 2.0F, 0.30859375F, 0.0078125F}, {-2.0F, -18.0F, -2.0F, 0.30859375F, 0.015625F}, {2.0F, -18.0F, -2.0F, 0.31640625F, 0.015625F}},
                {{2.0F, -7.0F, -2.0F, 0.32421875F, 0.015625F}, {-2.0F, -7.0F, -2.0F, 0.31640625F, 0.015625F}, {-2.0F, -7.0F, 2.0F, 0.31640625F, 0.0078125F}, {2.0F, -7.0F, 2.0F, 0.32421875F, 0.0078125F}},
                {{-2.0F, -18.0F, -2.0F, 0.30859375F, 0.015625F}, {-2.0F, -18.0F, 2.0F, 0.30078125F, 0.015625F}, {-2.0F, -7.0F, 2.0F, 0.30078125F, 0.037109375F}, {-2.0F, -7.0F, -2.0F, 0.30859375F, 0.037109375F}},
                {{2.0F, -18.0F, 2.0F, 0.32421875F, 0.015625F}, {2.0F, -18.0F, -2.0F, 0.31640625F, 0.015625F}, {2.0F, -7.0F, -2.0F, 0.31640625F, 0.037109375F}, {2.0F, -7.0F, 2.0F, 0.32421875F, 0.037109375F}},
                {{2.0F, -18.0F, -2.0F, 0.31640625F, 0.015625F}, {-2.0F, -18.0F, -2.0F, 0.30859375F, 0.015625F}, {-2.0F, -7.0F, -2.0F, 0.30859375F, 0.037109375F}, {2.0F, -7.0F, -2.0F, 0.31640625F, 0.037109375F}},
                {{-2.0F, -18.0F, 2.0F, 0.33203125F, 0.015625F}, {2.0F, -18.0F, 2.0F, 0.32421875F, 0.015625F}, {2.0F, -7.0F, 2.0F, 0.32421875F, 0.037109375F}, {-2.0F, -7.0F, 2.0F, 0.33203125F, 0.037109375F}}
        }));
        p7Cubes.add(new ReferenceMeshCube(-1.0F, -3.0F, -4.0F, 2.0F, 4.0F, 1.0F, new float[][][] {
                {{1.0F, -3.0F, -3.0F, 0.236328125F, 0.033203125F}, {-1.0F, -3.0F, -3.0F, 0.232421875F, 0.033203125F}, {-1.0F, -3.0F, -4.0F, 0.232421875F, 0.03515625F}, {1.0F, -3.0F, -4.0F, 0.236328125F, 0.03515625F}},
                {{1.0F, 1.0F, -4.0F, 0.240234375F, 0.03515625F}, {-1.0F, 1.0F, -4.0F, 0.236328125F, 0.03515625F}, {-1.0F, 1.0F, -3.0F, 0.236328125F, 0.033203125F}, {1.0F, 1.0F, -3.0F, 0.240234375F, 0.033203125F}},
                {{-1.0F, -3.0F, -4.0F, 0.232421875F, 0.03515625F}, {-1.0F, -3.0F, -3.0F, 0.23046875F, 0.03515625F}, {-1.0F, 1.0F, -3.0F, 0.23046875F, 0.04296875F}, {-1.0F, 1.0F, -4.0F, 0.232421875F, 0.04296875F}},
                {{1.0F, -3.0F, -3.0F, 0.23828125F, 0.03515625F}, {1.0F, -3.0F, -4.0F, 0.236328125F, 0.03515625F}, {1.0F, 1.0F, -4.0F, 0.236328125F, 0.04296875F}, {1.0F, 1.0F, -3.0F, 0.23828125F, 0.04296875F}},
                {{1.0F, -3.0F, -4.0F, 0.236328125F, 0.03515625F}, {-1.0F, -3.0F, -4.0F, 0.232421875F, 0.03515625F}, {-1.0F, 1.0F, -4.0F, 0.232421875F, 0.04296875F}, {1.0F, 1.0F, -4.0F, 0.236328125F, 0.04296875F}},
                {{-1.0F, -3.0F, -3.0F, 0.2421875F, 0.03515625F}, {1.0F, -3.0F, -3.0F, 0.23828125F, 0.03515625F}, {1.0F, 1.0F, -3.0F, 0.23828125F, 0.04296875F}, {-1.0F, 1.0F, -3.0F, 0.2421875F, 0.04296875F}}
        }));
        var p7 = new net.minecraft.client.model.geom.ModelPart(p7Cubes, java.util.Map.ofEntries());
        p7.setInitialPose(PartPose.offsetAndRotation(0.0F, -10.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p7.resetPose();
        var p6Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body1/arch_illager", java.util.List.of()));
        var p6 = new net.minecraft.client.model.geom.ModelPart(p6Cubes, java.util.Map.ofEntries(java.util.Map.entry("head4", p7), java.util.Map.entry("body5", p8)));
        p6.setInitialPose(PartPose.offsetAndRotation(0.0F, 22.225F, 0.0F, 0.0F, 0.0F, 0.0F)); p6.resetPose();
        var p5Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_head3", java.util.List.of()));
        var p5 = new net.minecraft.client.model.geom.ModelPart(p5Cubes, java.util.Map.ofEntries());
        p5.setInitialPose(PartPose.offsetAndRotation(10.0F, 4.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p5.resetPose();
        var p4Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_head2", java.util.List.of()));
        var p4 = new net.minecraft.client.model.geom.ModelPart(p4Cubes, java.util.Map.ofEntries());
        p4.setInitialPose(PartPose.offsetAndRotation(-8.0F, 4.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p4.resetPose();
        var p3Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_head1", java.util.List.of()));
        var p3 = new net.minecraft.client.model.geom.ModelPart(p3Cubes, java.util.Map.ofEntries());
        p3.setInitialPose(PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p3.resetPose();
        var p2Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body3", java.util.List.of()));
        var p2 = new net.minecraft.client.model.geom.ModelPart(p2Cubes, java.util.Map.ofEntries());
        p2.setInitialPose(PartPose.offsetAndRotation(-2.0F, 16.6922281F, 1.52787293F, 0.83252203F, 0.0F, 0.0F)); p2.resetPose();
        var p1Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body2", java.util.List.of()));
        var p1 = new net.minecraft.client.model.geom.ModelPart(p1Cubes, java.util.Map.ofEntries());
        p1.setInitialPose(PartPose.offsetAndRotation(-2.0F, 6.9F, -0.5F, 0.20420352F, 0.0F, 0.0F)); p1.resetPose();
        var p0Cubes = new java.util.ArrayList<>(customCubes.getOrDefault("/__cem_host_body1", java.util.List.of()));
        var p0 = new net.minecraft.client.model.geom.ModelPart(p0Cubes, java.util.Map.ofEntries(java.util.Map.entry("arch_illager", p6)));
        p0.setInitialPose(PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F)); p0.resetPose();
        return new net.minecraft.client.model.geom.ModelPart(java.util.List.of(), java.util.Map.ofEntries(java.util.Map.entry("__cem_host_body1", p0), java.util.Map.entry("__cem_host_body2", p1), java.util.Map.entry("__cem_host_body3", p2), java.util.Map.entry("__cem_host_head1", p3), java.util.Map.entry("__cem_host_head2", p4), java.util.Map.entry("__cem_host_head3", p5)));
    }
}
