/* Adapted Dungeons Gear visual assets: MIT, Copyright (c) 2022 Patrigan.
 * Generated from retained pinned Geo snapshots by tools/generate_gear_armor_models.py.
 * Original meshes/UVs and the explicit Wolf slot split: design/gear-armor-models.json. */
package de.laugenbreze.dungeonsalive.client.gear;
import de.laugenbreze.dungeonsalive.item.gear.ArmorSetKind;
import de.laugenbreze.dungeonsalive.client.entity.reference.imported.ReferenceMeshCube;
import java.util.*;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;

public final class ArmorGeometry {
    private ArmorGeometry() {}
    public static ModelPart bakeRoot(ArmorSetKind kind) {
        return switch (kind) { case WOLF -> wolf(); case SPIDER -> spider(); case BATTLE_ROBES -> battle_robes(); };
    }
    private static ModelPart socket(String name, ModelPart child, float x, float y, float z) {
        var part = new ModelPart(List.of(), Map.of(name, child));
        var pose = PartPose.offset(x, y, z);
        part.setInitialPose(pose); part.loadPose(pose); return part;
    }
    private static ModelPart wolf0() {
        var children = new LinkedHashMap<String, ModelPart>();
        children.put("armorHead", wolf1());
        var part = new ModelPart(List.of(), children);
        var pose = PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
        part.setInitialPose(pose); part.loadPose(pose); return part;
    }
    private static ModelPart wolf1() {
        var children = new LinkedHashMap<String, ModelPart>();
        
        var part = new ModelPart(List.of(new ReferenceMeshCube(-5.75F, -10.25F, -5.75F, 11.5F, 11.5F, 11.5F, new float[][][]{{{5.75F, -10.25F, 5.75F, 0.3125F, 0.15625F}, {5.75F, -10.25F, -5.75F, 0.20833333F, 0.15625F}, {5.75F, 1.25F, -5.75F, 0.20833333F, 0.3125F}, {5.75F, 1.25F, 5.75F, 0.3125F, 0.3125F}}, {{-5.75F, -10.25F, -5.75F, 0.10416667F, 0.15625F}, {-5.75F, -10.25F, 5.75F, 0.0F, 0.15625F}, {-5.75F, 1.25F, 5.75F, 0.0F, 0.3125F}, {-5.75F, 1.25F, -5.75F, 0.10416667F, 0.3125F}}, {{5.75F, -10.25F, -5.75F, 0.20833333F, 0.15625F}, {-5.75F, -10.25F, -5.75F, 0.10416667F, 0.15625F}, {-5.75F, 1.25F, -5.75F, 0.10416667F, 0.3125F}, {5.75F, 1.25F, -5.75F, 0.20833333F, 0.3125F}}, {{-5.75F, -10.25F, 5.75F, 0.41666667F, 0.15625F}, {5.75F, -10.25F, 5.75F, 0.3125F, 0.15625F}, {5.75F, 1.25F, 5.75F, 0.3125F, 0.3125F}, {-5.75F, 1.25F, 5.75F, 0.41666667F, 0.3125F}}, {{5.75F, -10.25F, 5.75F, 0.20833333F, 0.0F}, {-5.75F, -10.25F, 5.75F, 0.10416667F, 0.0F}, {-5.75F, -10.25F, -5.75F, 0.10416667F, 0.15625F}, {5.75F, -10.25F, -5.75F, 0.20833333F, 0.15625F}}, {{5.75F, 1.25F, -5.75F, 0.3125F, 0.15625F}, {-5.75F, 1.25F, -5.75F, 0.20833333F, 0.15625F}, {-5.75F, 1.25F, 5.75F, 0.20833333F, 0.0F}, {5.75F, 1.25F, 5.75F, 0.3125F, 0.0F}}}),
            new ReferenceMeshCube(-2.25F, -7.25F, -9.0F, 4.5F, 3.5F, 4.5F, new float[][][]{{{2.25F, -7.25F, -4.5F, 0.45833333F, 0.0625F}, {2.25F, -7.25F, -9.0F, 0.41666667F, 0.0625F}, {2.25F, -3.75F, -9.0F, 0.41666667F, 0.109375F}, {2.25F, -3.75F, -4.5F, 0.45833333F, 0.109375F}}, {{-2.25F, -7.25F, -9.0F, 0.375F, 0.0625F}, {-2.25F, -7.25F, -4.5F, 0.33333333F, 0.0625F}, {-2.25F, -3.75F, -4.5F, 0.33333333F, 0.109375F}, {-2.25F, -3.75F, -9.0F, 0.375F, 0.109375F}}, {{2.25F, -7.25F, -9.0F, 0.41666667F, 0.0625F}, {-2.25F, -7.25F, -9.0F, 0.375F, 0.0625F}, {-2.25F, -3.75F, -9.0F, 0.375F, 0.109375F}, {2.25F, -3.75F, -9.0F, 0.41666667F, 0.109375F}}, {{-2.25F, -7.25F, -4.5F, 0.5F, 0.0625F}, {2.25F, -7.25F, -4.5F, 0.45833333F, 0.0625F}, {2.25F, -3.75F, -4.5F, 0.45833333F, 0.109375F}, {-2.25F, -3.75F, -4.5F, 0.5F, 0.109375F}}, {{2.25F, -7.25F, -4.5F, 0.41666667F, 0.0F}, {-2.25F, -7.25F, -4.5F, 0.375F, 0.0F}, {-2.25F, -7.25F, -9.0F, 0.375F, 0.0625F}, {2.25F, -7.25F, -9.0F, 0.41666667F, 0.0625F}}, {{2.25F, -3.75F, -9.0F, 0.45833333F, 0.0625F}, {-2.25F, -3.75F, -9.0F, 0.41666667F, 0.0625F}, {-2.25F, -3.75F, -4.5F, 0.41666667F, 0.0F}, {2.25F, -3.75F, -4.5F, 0.45833333F, 0.0F}}}),
            new ReferenceMeshCube(1.25F, -13.75F, -0.25F, 3.5F, 3.5F, 2.5F, new float[][][]{{{4.75F, -13.75F, 2.25F, 0.072916667F, 0.03125F}, {4.75F, -13.75F, -0.25F, 0.052083333F, 0.03125F}, {4.75F, -10.25F, -0.25F, 0.052083333F, 0.078125F}, {4.75F, -10.25F, 2.25F, 0.072916667F, 0.078125F}}, {{1.25F, -13.75F, -0.25F, 0.020833333F, 0.03125F}, {1.25F, -13.75F, 2.25F, 0.0F, 0.03125F}, {1.25F, -10.25F, 2.25F, 0.0F, 0.078125F}, {1.25F, -10.25F, -0.25F, 0.020833333F, 0.078125F}}, {{4.75F, -13.75F, -0.25F, 0.052083333F, 0.03125F}, {1.25F, -13.75F, -0.25F, 0.020833333F, 0.03125F}, {1.25F, -10.25F, -0.25F, 0.020833333F, 0.078125F}, {4.75F, -10.25F, -0.25F, 0.052083333F, 0.078125F}}, {{1.25F, -13.75F, 2.25F, 0.10416667F, 0.03125F}, {4.75F, -13.75F, 2.25F, 0.072916667F, 0.03125F}, {4.75F, -10.25F, 2.25F, 0.072916667F, 0.078125F}, {1.25F, -10.25F, 2.25F, 0.10416667F, 0.078125F}}, {{4.75F, -13.75F, 2.25F, 0.052083333F, 0.0F}, {1.25F, -13.75F, 2.25F, 0.020833333F, 0.0F}, {1.25F, -13.75F, -0.25F, 0.020833333F, 0.03125F}, {4.75F, -13.75F, -0.25F, 0.052083333F, 0.03125F}}, {{4.75F, -10.25F, -0.25F, 0.083333333F, 0.03125F}, {1.25F, -10.25F, -0.25F, 0.052083333F, 0.03125F}, {1.25F, -10.25F, 2.25F, 0.052083333F, 0.0F}, {4.75F, -10.25F, 2.25F, 0.083333333F, 0.0F}}}),
            new ReferenceMeshCube(-4.75F, -13.75F, -0.25F, 3.5F, 3.5F, 2.5F, new float[][][]{{{-1.25F, -13.75F, 2.25F, 0.072916667F, 0.03125F}, {-1.25F, -13.75F, -0.25F, 0.052083333F, 0.03125F}, {-1.25F, -10.25F, -0.25F, 0.052083333F, 0.078125F}, {-1.25F, -10.25F, 2.25F, 0.072916667F, 0.078125F}}, {{-4.75F, -13.75F, -0.25F, 0.020833333F, 0.03125F}, {-4.75F, -13.75F, 2.25F, 0.0F, 0.03125F}, {-4.75F, -10.25F, 2.25F, 0.0F, 0.078125F}, {-4.75F, -10.25F, -0.25F, 0.020833333F, 0.078125F}}, {{-1.25F, -13.75F, -0.25F, 0.052083333F, 0.03125F}, {-4.75F, -13.75F, -0.25F, 0.020833333F, 0.03125F}, {-4.75F, -10.25F, -0.25F, 0.020833333F, 0.078125F}, {-1.25F, -10.25F, -0.25F, 0.052083333F, 0.078125F}}, {{-4.75F, -13.75F, 2.25F, 0.10416667F, 0.03125F}, {-1.25F, -13.75F, 2.25F, 0.072916667F, 0.03125F}, {-1.25F, -10.25F, 2.25F, 0.072916667F, 0.078125F}, {-4.75F, -10.25F, 2.25F, 0.10416667F, 0.078125F}}, {{-1.25F, -13.75F, 2.25F, 0.052083333F, 0.0F}, {-4.75F, -13.75F, 2.25F, 0.020833333F, 0.0F}, {-4.75F, -13.75F, -0.25F, 0.020833333F, 0.03125F}, {-1.25F, -13.75F, -0.25F, 0.052083333F, 0.03125F}}, {{-1.25F, -10.25F, -0.25F, 0.083333333F, 0.03125F}, {-4.75F, -10.25F, -0.25F, 0.052083333F, 0.03125F}, {-4.75F, -10.25F, 2.25F, 0.052083333F, 0.0F}, {-1.25F, -10.25F, 2.25F, 0.083333333F, 0.0F}}})), children);
        var pose = PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
        part.setInitialPose(pose); part.loadPose(pose); return part;
    }
    private static ModelPart wolf2() {
        var children = new LinkedHashMap<String, ModelPart>();
        children.put("armorBody", wolf3());
        var part = new ModelPart(List.of(), children);
        var pose = PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
        part.setInitialPose(pose); part.loadPose(pose); return part;
    }
    private static ModelPart wolf3() {
        var children = new LinkedHashMap<String, ModelPart>();
        
        var part = new ModelPart(List.of(new ReferenceMeshCube(-4.75F, -0.75F, -2.75F, 9.5F, 13.5F, 5.5F, new float[][][]{{{4.75F, -0.75F, 2.75F, 0.33333333F, 0.375F}, {4.75F, -0.75F, -2.75F, 0.29166667F, 0.375F}, {4.75F, 12.75F, -2.75F, 0.29166667F, 0.5625F}, {4.75F, 12.75F, 2.75F, 0.33333333F, 0.5625F}}, {{-4.75F, -0.75F, -2.75F, 0.20833333F, 0.375F}, {-4.75F, -0.75F, 2.75F, 0.16666667F, 0.375F}, {-4.75F, 12.75F, 2.75F, 0.16666667F, 0.5625F}, {-4.75F, 12.75F, -2.75F, 0.20833333F, 0.5625F}}, {{4.75F, -0.75F, -2.75F, 0.29166667F, 0.375F}, {-4.75F, -0.75F, -2.75F, 0.20833333F, 0.375F}, {-4.75F, 12.75F, -2.75F, 0.20833333F, 0.5625F}, {4.75F, 12.75F, -2.75F, 0.29166667F, 0.5625F}}, {{-4.75F, -0.75F, 2.75F, 0.41666667F, 0.375F}, {4.75F, -0.75F, 2.75F, 0.33333333F, 0.375F}, {4.75F, 12.75F, 2.75F, 0.33333333F, 0.5625F}, {-4.75F, 12.75F, 2.75F, 0.41666667F, 0.5625F}}, {{4.75F, -0.75F, 2.75F, 0.29166667F, 0.3125F}, {-4.75F, -0.75F, 2.75F, 0.20833333F, 0.3125F}, {-4.75F, -0.75F, -2.75F, 0.20833333F, 0.375F}, {4.75F, -0.75F, -2.75F, 0.29166667F, 0.375F}}, {{4.75F, 12.75F, -2.75F, 0.375F, 0.375F}, {-4.75F, 12.75F, -2.75F, 0.29166667F, 0.375F}, {-4.75F, 12.75F, 2.75F, 0.29166667F, 0.3125F}, {4.75F, 12.75F, 2.75F, 0.375F, 0.3125F}}})), children);
        var pose = PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
        part.setInitialPose(pose); part.loadPose(pose); return part;
    }
    private static ModelPart wolf4() {
        var children = new LinkedHashMap<String, ModelPart>();
        children.put("armorRightArm", wolf5());
        var part = new ModelPart(List.of(), children);
        var pose = PartPose.offsetAndRotation(1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
        part.setInitialPose(pose); part.loadPose(pose); return part;
    }
    private static ModelPart wolf5() {
        var children = new LinkedHashMap<String, ModelPart>();
        
        var part = new ModelPart(List.of(new ReferenceMeshCube(-4.75F, -2.75F, -2.75F, 5.5F, 13.5F, 5.5F, new float[][][]{{{-4.75F, -2.75F, -2.75F, 0.5F, 0.375F}, {-4.75F, -2.75F, 2.75F, 0.54166667F, 0.375F}, {-4.75F, 10.75F, 2.75F, 0.54166667F, 0.5625F}, {-4.75F, 10.75F, -2.75F, 0.5F, 0.5625F}}, {{0.75F, -2.75F, 2.75F, 0.41666667F, 0.375F}, {0.75F, -2.75F, -2.75F, 0.45833333F, 0.375F}, {0.75F, 10.75F, -2.75F, 0.45833333F, 0.5625F}, {0.75F, 10.75F, 2.75F, 0.41666667F, 0.5625F}}, {{0.75F, -2.75F, -2.75F, 0.45833333F, 0.375F}, {-4.75F, -2.75F, -2.75F, 0.5F, 0.375F}, {-4.75F, 10.75F, -2.75F, 0.5F, 0.5625F}, {0.75F, 10.75F, -2.75F, 0.45833333F, 0.5625F}}, {{-4.75F, -2.75F, 2.75F, 0.54166667F, 0.375F}, {0.75F, -2.75F, 2.75F, 0.58333333F, 0.375F}, {0.75F, 10.75F, 2.75F, 0.58333333F, 0.5625F}, {-4.75F, 10.75F, 2.75F, 0.54166667F, 0.5625F}}, {{0.75F, -2.75F, 2.75F, 0.45833333F, 0.3125F}, {-4.75F, -2.75F, 2.75F, 0.5F, 0.3125F}, {-4.75F, -2.75F, -2.75F, 0.5F, 0.375F}, {0.75F, -2.75F, -2.75F, 0.45833333F, 0.375F}}, {{0.75F, 10.75F, -2.75F, 0.5F, 0.375F}, {-4.75F, 10.75F, -2.75F, 0.54166667F, 0.375F}, {-4.75F, 10.75F, 2.75F, 0.54166667F, 0.3125F}, {0.75F, 10.75F, 2.75F, 0.5F, 0.3125F}}})), children);
        var pose = PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
        part.setInitialPose(pose); part.loadPose(pose); return part;
    }
    private static ModelPart wolf6() {
        var children = new LinkedHashMap<String, ModelPart>();
        children.put("armorLeftArm", wolf7());
        var part = new ModelPart(List.of(), children);
        var pose = PartPose.offsetAndRotation(-1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
        part.setInitialPose(pose); part.loadPose(pose); return part;
    }
    private static ModelPart wolf7() {
        var children = new LinkedHashMap<String, ModelPart>();
        
        var part = new ModelPart(List.of(new ReferenceMeshCube(-0.75F, -2.75F, -2.75F, 5.5F, 13.5F, 5.5F, new float[][][]{{{4.75F, -2.75F, 2.75F, 0.54166667F, 0.375F}, {4.75F, -2.75F, -2.75F, 0.5F, 0.375F}, {4.75F, 10.75F, -2.75F, 0.5F, 0.5625F}, {4.75F, 10.75F, 2.75F, 0.54166667F, 0.5625F}}, {{-0.75F, -2.75F, -2.75F, 0.45833333F, 0.375F}, {-0.75F, -2.75F, 2.75F, 0.41666667F, 0.375F}, {-0.75F, 10.75F, 2.75F, 0.41666667F, 0.5625F}, {-0.75F, 10.75F, -2.75F, 0.45833333F, 0.5625F}}, {{4.75F, -2.75F, -2.75F, 0.5F, 0.375F}, {-0.75F, -2.75F, -2.75F, 0.45833333F, 0.375F}, {-0.75F, 10.75F, -2.75F, 0.45833333F, 0.5625F}, {4.75F, 10.75F, -2.75F, 0.5F, 0.5625F}}, {{-0.75F, -2.75F, 2.75F, 0.58333333F, 0.375F}, {4.75F, -2.75F, 2.75F, 0.54166667F, 0.375F}, {4.75F, 10.75F, 2.75F, 0.54166667F, 0.5625F}, {-0.75F, 10.75F, 2.75F, 0.58333333F, 0.5625F}}, {{4.75F, -2.75F, 2.75F, 0.5F, 0.3125F}, {-0.75F, -2.75F, 2.75F, 0.45833333F, 0.3125F}, {-0.75F, -2.75F, -2.75F, 0.45833333F, 0.375F}, {4.75F, -2.75F, -2.75F, 0.5F, 0.375F}}, {{4.75F, 10.75F, -2.75F, 0.54166667F, 0.375F}, {-0.75F, 10.75F, -2.75F, 0.5F, 0.375F}, {-0.75F, 10.75F, 2.75F, 0.5F, 0.3125F}, {4.75F, 10.75F, 2.75F, 0.54166667F, 0.3125F}}})), children);
        var pose = PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
        part.setInitialPose(pose); part.loadPose(pose); return part;
    }
    private static ModelPart wolf8() {
        var children = new LinkedHashMap<String, ModelPart>();
        children.put("armorLeftLeg", wolf9());
        children.put("armorLeftBoot", wolf10());
        var part = new ModelPart(List.of(), children);
        var pose = PartPose.offsetAndRotation(0.1F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
        part.setInitialPose(pose); part.loadPose(pose); return part;
    }
    private static ModelPart wolf9() {
        var children = new LinkedHashMap<String, ModelPart>();
        
        var part = new ModelPart(List.of(new ReferenceMeshCube(-2.75F, -0.75F, -2.75F, 5.5F, 8.75F, 5.5F, new float[][][]{{{-2.75F, 8.0F, -2.75F, 0.083333333F, 0.49652778F}, {-2.75F, -0.75F, -2.75F, 0.083333333F, 0.375F}, {-2.75F, -0.75F, 2.75F, 0.125F, 0.375F}, {-2.75F, 8.0F, 2.75F, 0.125F, 0.49652778F}}, {{2.75F, 8.0F, 2.75F, 0.0F, 0.49652778F}, {2.75F, -0.75F, 2.75F, 0.0F, 0.375F}, {2.75F, -0.75F, -2.75F, 0.041666667F, 0.375F}, {2.75F, 8.0F, -2.75F, 0.041666667F, 0.49652778F}}, {{2.75F, 8.0F, -2.75F, 0.041666667F, 0.49652778F}, {2.75F, -0.75F, -2.75F, 0.041666667F, 0.375F}, {-2.75F, -0.75F, -2.75F, 0.083333333F, 0.375F}, {-2.75F, 8.0F, -2.75F, 0.083333333F, 0.49652778F}}, {{-2.75F, 8.0F, 2.75F, 0.125F, 0.49652778F}, {-2.75F, -0.75F, 2.75F, 0.125F, 0.375F}, {2.75F, -0.75F, 2.75F, 0.16666667F, 0.375F}, {2.75F, 8.0F, 2.75F, 0.16666667F, 0.49652778F}}, {{2.75F, -0.75F, 2.75F, 0.041666667F, 0.3125F}, {-2.75F, -0.75F, 2.75F, 0.083333333F, 0.3125F}, {-2.75F, -0.75F, -2.75F, 0.083333333F, 0.375F}, {2.75F, -0.75F, -2.75F, 0.041666667F, 0.375F}}})), children);
        var pose = PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
        part.setInitialPose(pose); part.loadPose(pose); return part;
    }
    private static ModelPart wolf10() {
        var children = new LinkedHashMap<String, ModelPart>();
        
        var part = new ModelPart(List.of(new ReferenceMeshCube(-2.75F, 8.0F, -2.75F, 5.5F, 4.75F, 5.5F, new float[][][]{{{-2.75F, 8.0F, -2.75F, 0.083333333F, 0.49652778F}, {-2.75F, 8.0F, 2.75F, 0.125F, 0.49652778F}, {-2.75F, 12.75F, 2.75F, 0.125F, 0.5625F}, {-2.75F, 12.75F, -2.75F, 0.083333333F, 0.5625F}}, {{2.75F, 8.0F, 2.75F, 0.0F, 0.49652778F}, {2.75F, 8.0F, -2.75F, 0.041666667F, 0.49652778F}, {2.75F, 12.75F, -2.75F, 0.041666667F, 0.5625F}, {2.75F, 12.75F, 2.75F, 0.0F, 0.5625F}}, {{2.75F, 8.0F, -2.75F, 0.041666667F, 0.49652778F}, {-2.75F, 8.0F, -2.75F, 0.083333333F, 0.49652778F}, {-2.75F, 12.75F, -2.75F, 0.083333333F, 0.5625F}, {2.75F, 12.75F, -2.75F, 0.041666667F, 0.5625F}}, {{-2.75F, 8.0F, 2.75F, 0.125F, 0.49652778F}, {2.75F, 8.0F, 2.75F, 0.16666667F, 0.49652778F}, {2.75F, 12.75F, 2.75F, 0.16666667F, 0.5625F}, {-2.75F, 12.75F, 2.75F, 0.125F, 0.5625F}}, {{2.75F, 12.75F, -2.75F, 0.083333333F, 0.375F}, {-2.75F, 12.75F, -2.75F, 0.125F, 0.375F}, {-2.75F, 12.75F, 2.75F, 0.125F, 0.3125F}, {2.75F, 12.75F, 2.75F, 0.083333333F, 0.3125F}}})), children);
        var pose = PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
        part.setInitialPose(pose); part.loadPose(pose); return part;
    }
    private static ModelPart wolf11() {
        var children = new LinkedHashMap<String, ModelPart>();
        children.put("armorRightLeg", wolf12());
        children.put("armorRightBoot", wolf13());
        var part = new ModelPart(List.of(), children);
        var pose = PartPose.offsetAndRotation(-0.1F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
        part.setInitialPose(pose); part.loadPose(pose); return part;
    }
    private static ModelPart wolf12() {
        var children = new LinkedHashMap<String, ModelPart>();
        
        var part = new ModelPart(List.of(new ReferenceMeshCube(-2.75F, -0.75F, -2.75F, 5.5F, 8.75F, 5.5F, new float[][][]{{{2.75F, 8.0F, 2.75F, 0.125F, 0.49652778F}, {2.75F, -0.75F, 2.75F, 0.125F, 0.375F}, {2.75F, -0.75F, -2.75F, 0.083333333F, 0.375F}, {2.75F, 8.0F, -2.75F, 0.083333333F, 0.49652778F}}, {{-2.75F, 8.0F, -2.75F, 0.041666667F, 0.49652778F}, {-2.75F, -0.75F, -2.75F, 0.041666667F, 0.375F}, {-2.75F, -0.75F, 2.75F, 0.0F, 0.375F}, {-2.75F, 8.0F, 2.75F, 0.0F, 0.49652778F}}, {{2.75F, 8.0F, -2.75F, 0.083333333F, 0.49652778F}, {2.75F, -0.75F, -2.75F, 0.083333333F, 0.375F}, {-2.75F, -0.75F, -2.75F, 0.041666667F, 0.375F}, {-2.75F, 8.0F, -2.75F, 0.041666667F, 0.49652778F}}, {{-2.75F, 8.0F, 2.75F, 0.16666667F, 0.49652778F}, {-2.75F, -0.75F, 2.75F, 0.16666667F, 0.375F}, {2.75F, -0.75F, 2.75F, 0.125F, 0.375F}, {2.75F, 8.0F, 2.75F, 0.125F, 0.49652778F}}, {{2.75F, -0.75F, 2.75F, 0.083333333F, 0.3125F}, {-2.75F, -0.75F, 2.75F, 0.041666667F, 0.3125F}, {-2.75F, -0.75F, -2.75F, 0.041666667F, 0.375F}, {2.75F, -0.75F, -2.75F, 0.083333333F, 0.375F}}})), children);
        var pose = PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
        part.setInitialPose(pose); part.loadPose(pose); return part;
    }
    private static ModelPart wolf13() {
        var children = new LinkedHashMap<String, ModelPart>();
        
        var part = new ModelPart(List.of(new ReferenceMeshCube(-2.75F, 8.0F, -2.75F, 5.5F, 4.75F, 5.5F, new float[][][]{{{2.75F, 8.0F, 2.75F, 0.125F, 0.49652778F}, {2.75F, 8.0F, -2.75F, 0.083333333F, 0.49652778F}, {2.75F, 12.75F, -2.75F, 0.083333333F, 0.5625F}, {2.75F, 12.75F, 2.75F, 0.125F, 0.5625F}}, {{-2.75F, 8.0F, -2.75F, 0.041666667F, 0.49652778F}, {-2.75F, 8.0F, 2.75F, 0.0F, 0.49652778F}, {-2.75F, 12.75F, 2.75F, 0.0F, 0.5625F}, {-2.75F, 12.75F, -2.75F, 0.041666667F, 0.5625F}}, {{2.75F, 8.0F, -2.75F, 0.083333333F, 0.49652778F}, {-2.75F, 8.0F, -2.75F, 0.041666667F, 0.49652778F}, {-2.75F, 12.75F, -2.75F, 0.041666667F, 0.5625F}, {2.75F, 12.75F, -2.75F, 0.083333333F, 0.5625F}}, {{-2.75F, 8.0F, 2.75F, 0.16666667F, 0.49652778F}, {2.75F, 8.0F, 2.75F, 0.125F, 0.49652778F}, {2.75F, 12.75F, 2.75F, 0.125F, 0.5625F}, {-2.75F, 12.75F, 2.75F, 0.16666667F, 0.5625F}}, {{2.75F, 12.75F, -2.75F, 0.125F, 0.375F}, {-2.75F, 12.75F, -2.75F, 0.083333333F, 0.375F}, {-2.75F, 12.75F, 2.75F, 0.083333333F, 0.3125F}, {2.75F, 12.75F, 2.75F, 0.125F, 0.3125F}}})), children);
        var pose = PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
        part.setInitialPose(pose); part.loadPose(pose); return part;
    }
    private static ModelPart wolf() {
        var root = new LinkedHashMap<String, ModelPart>();
        root.put("head", socket("bipedHead", wolf0(), 0.0F, 0.0F, 0.0F));
        root.put("body", socket("bipedBody", wolf2(), 0.0F, 0.0F, 0.0F));
        root.put("right_arm", socket("bipedRightArm", wolf4(), -5.0F, 2.0F, 0.0F));
        root.put("left_arm", socket("bipedLeftArm", wolf6(), 5.0F, 2.0F, 0.0F));
        root.put("right_leg", socket("bipedRightLeg", wolf11(), -1.9F, 12.0F, 0.0F));
        root.put("left_leg", socket("bipedLeftLeg", wolf8(), 1.9F, 12.0F, 0.0F));
        root.put("hat", new ModelPart(List.of(), Map.of()));
        return new ModelPart(List.of(), root);
    }
    private static ModelPart spider0() {
        var children = new LinkedHashMap<String, ModelPart>();
        children.put("armorHead", spider1());
        var part = new ModelPart(List.of(), children);
        var pose = PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
        part.setInitialPose(pose); part.loadPose(pose); return part;
    }
    private static ModelPart spider1() {
        var children = new LinkedHashMap<String, ModelPart>();
        children.put("hoodback", spider2());
        var part = new ModelPart(List.of(new ReferenceMeshCube(-5.0F, -9.0F, -5.0F, 10.0F, 10.0F, 10.0F, new float[][][]{{{5.0F, -9.0F, 5.0F, 0.25F, 0.125F}, {5.0F, -9.0F, -5.0F, 0.16666667F, 0.125F}, {5.0F, 1.0F, -5.0F, 0.16666667F, 0.25F}, {5.0F, 1.0F, 5.0F, 0.25F, 0.25F}}, {{-5.0F, -9.0F, -5.0F, 0.083333333F, 0.125F}, {-5.0F, -9.0F, 5.0F, 0.0F, 0.125F}, {-5.0F, 1.0F, 5.0F, 0.0F, 0.25F}, {-5.0F, 1.0F, -5.0F, 0.083333333F, 0.25F}}, {{5.0F, -9.0F, -5.0F, 0.16666667F, 0.125F}, {-5.0F, -9.0F, -5.0F, 0.083333333F, 0.125F}, {-5.0F, 1.0F, -5.0F, 0.083333333F, 0.25F}, {5.0F, 1.0F, -5.0F, 0.16666667F, 0.25F}}, {{-5.0F, -9.0F, 5.0F, 0.33333333F, 0.125F}, {5.0F, -9.0F, 5.0F, 0.25F, 0.125F}, {5.0F, 1.0F, 5.0F, 0.25F, 0.25F}, {-5.0F, 1.0F, 5.0F, 0.33333333F, 0.25F}}, {{5.0F, -9.0F, 5.0F, 0.16666667F, 0.0F}, {-5.0F, -9.0F, 5.0F, 0.083333333F, 0.0F}, {-5.0F, -9.0F, -5.0F, 0.083333333F, 0.125F}, {5.0F, -9.0F, -5.0F, 0.16666667F, 0.125F}}, {{5.0F, 1.0F, -5.0F, 0.25F, 0.125F}, {-5.0F, 1.0F, -5.0F, 0.16666667F, 0.125F}, {-5.0F, 1.0F, 5.0F, 0.16666667F, 0.0F}, {5.0F, 1.0F, 5.0F, 0.25F, 0.0F}}}),
            new ReferenceMeshCube(-4.75F, -8.75F, -4.75F, 9.5F, 9.5F, 9.5F, new float[][][]{{{4.75F, -8.75F, 4.75F, 0.58333333F, 0.125F}, {4.75F, -8.75F, -4.75F, 0.5F, 0.125F}, {4.75F, 0.75F, -4.75F, 0.5F, 0.25F}, {4.75F, 0.75F, 4.75F, 0.58333333F, 0.25F}}, {{-4.75F, -8.75F, -4.75F, 0.41666667F, 0.125F}, {-4.75F, -8.75F, 4.75F, 0.33333333F, 0.125F}, {-4.75F, 0.75F, 4.75F, 0.33333333F, 0.25F}, {-4.75F, 0.75F, -4.75F, 0.41666667F, 0.25F}}, {{4.75F, -8.75F, -4.75F, 0.5F, 0.125F}, {-4.75F, -8.75F, -4.75F, 0.41666667F, 0.125F}, {-4.75F, 0.75F, -4.75F, 0.41666667F, 0.25F}, {4.75F, 0.75F, -4.75F, 0.5F, 0.25F}}, {{-4.75F, -8.75F, 4.75F, 0.66666667F, 0.125F}, {4.75F, -8.75F, 4.75F, 0.58333333F, 0.125F}, {4.75F, 0.75F, 4.75F, 0.58333333F, 0.25F}, {-4.75F, 0.75F, 4.75F, 0.66666667F, 0.25F}}, {{4.75F, -8.75F, 4.75F, 0.5F, 0.0F}, {-4.75F, -8.75F, 4.75F, 0.41666667F, 0.0F}, {-4.75F, -8.75F, -4.75F, 0.41666667F, 0.125F}, {4.75F, -8.75F, -4.75F, 0.5F, 0.125F}}, {{4.75F, 0.75F, -4.75F, 0.58333333F, 0.125F}, {-4.75F, 0.75F, -4.75F, 0.5F, 0.125F}, {-4.75F, 0.75F, 4.75F, 0.5F, 0.0F}, {4.75F, 0.75F, 4.75F, 0.58333333F, 0.0F}}})), children);
        var pose = PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
        part.setInitialPose(pose); part.loadPose(pose); return part;
    }
    private static ModelPart spider2() {
        var children = new LinkedHashMap<String, ModelPart>();
        
        var part = new ModelPart(List.of(new ReferenceMeshCube(-8.9F, -2.9F, -0.9F, 9.8F, 3.8F, 3.8F, new float[][][]{{{0.9F, -2.9F, 2.9F, 0.70833333F, 0.046875F}, {0.9F, -2.9F, -0.9F, 0.6875F, 0.046875F}, {0.9F, 0.9F, -0.9F, 0.6875F, 0.078125F}, {0.9F, 0.9F, 2.9F, 0.70833333F, 0.078125F}}, {{-8.9F, -2.9F, -0.9F, 0.60416667F, 0.046875F}, {-8.9F, -2.9F, 2.9F, 0.58333333F, 0.046875F}, {-8.9F, 0.9F, 2.9F, 0.58333333F, 0.078125F}, {-8.9F, 0.9F, -0.9F, 0.60416667F, 0.078125F}}, {{0.9F, -2.9F, -0.9F, 0.6875F, 0.046875F}, {-8.9F, -2.9F, -0.9F, 0.60416667F, 0.046875F}, {-8.9F, 0.9F, -0.9F, 0.60416667F, 0.078125F}, {0.9F, 0.9F, -0.9F, 0.6875F, 0.078125F}}, {{-8.9F, -2.9F, 2.9F, 0.79166667F, 0.046875F}, {0.9F, -2.9F, 2.9F, 0.70833333F, 0.046875F}, {0.9F, 0.9F, 2.9F, 0.70833333F, 0.078125F}, {-8.9F, 0.9F, 2.9F, 0.79166667F, 0.078125F}}, {{0.9F, -2.9F, 2.9F, 0.6875F, 0.015625F}, {-8.9F, -2.9F, 2.9F, 0.60416667F, 0.015625F}, {-8.9F, -2.9F, -0.9F, 0.60416667F, 0.046875F}, {0.9F, -2.9F, -0.9F, 0.6875F, 0.046875F}}, {{0.9F, 0.9F, -0.9F, 0.77083333F, 0.046875F}, {-8.9F, 0.9F, -0.9F, 0.6875F, 0.046875F}, {-8.9F, 0.9F, 2.9F, 0.6875F, 0.015625F}, {0.9F, 0.9F, 2.9F, 0.77083333F, 0.015625F}}})), children);
        var pose = PartPose.offsetAndRotation(4.0F, -6.0F, 4.75F, -0.34906585F, 0.0F, 0.0F);
        part.setInitialPose(pose); part.loadPose(pose); return part;
    }
    private static ModelPart spider3() {
        var children = new LinkedHashMap<String, ModelPart>();
        children.put("armorBody", spider4());
        var part = new ModelPart(List.of(), children);
        var pose = PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
        part.setInitialPose(pose); part.loadPose(pose); return part;
    }
    private static ModelPart spider4() {
        var children = new LinkedHashMap<String, ModelPart>();
        
        var part = new ModelPart(List.of(new ReferenceMeshCube(-4.76F, -0.76F, -2.76F, 9.52F, 13.52F, 5.52F, new float[][][]{{{4.76F, -0.76F, 2.76F, 0.33333333F, 0.3125F}, {4.76F, -0.76F, -2.76F, 0.29166667F, 0.3125F}, {4.76F, 12.76F, -2.76F, 0.29166667F, 0.5F}, {4.76F, 12.76F, 2.76F, 0.33333333F, 0.5F}}, {{-4.76F, -0.76F, -2.76F, 0.20833333F, 0.3125F}, {-4.76F, -0.76F, 2.76F, 0.16666667F, 0.3125F}, {-4.76F, 12.76F, 2.76F, 0.16666667F, 0.5F}, {-4.76F, 12.76F, -2.76F, 0.20833333F, 0.5F}}, {{4.76F, -0.76F, -2.76F, 0.29166667F, 0.3125F}, {-4.76F, -0.76F, -2.76F, 0.20833333F, 0.3125F}, {-4.76F, 12.76F, -2.76F, 0.20833333F, 0.5F}, {4.76F, 12.76F, -2.76F, 0.29166667F, 0.5F}}, {{-4.76F, -0.76F, 2.76F, 0.41666667F, 0.3125F}, {4.76F, -0.76F, 2.76F, 0.33333333F, 0.3125F}, {4.76F, 12.76F, 2.76F, 0.33333333F, 0.5F}, {-4.76F, 12.76F, 2.76F, 0.41666667F, 0.5F}}, {{4.76F, -0.76F, 2.76F, 0.29166667F, 0.25F}, {-4.76F, -0.76F, 2.76F, 0.20833333F, 0.25F}, {-4.76F, -0.76F, -2.76F, 0.20833333F, 0.3125F}, {4.76F, -0.76F, -2.76F, 0.29166667F, 0.3125F}}, {{4.76F, 12.76F, -2.76F, 0.375F, 0.3125F}, {-4.76F, 12.76F, -2.76F, 0.29166667F, 0.3125F}, {-4.76F, 12.76F, 2.76F, 0.29166667F, 0.25F}, {4.76F, 12.76F, 2.76F, 0.375F, 0.25F}}})), children);
        var pose = PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
        part.setInitialPose(pose); part.loadPose(pose); return part;
    }
    private static ModelPart spider5() {
        var children = new LinkedHashMap<String, ModelPart>();
        children.put("armorRightArm", spider6());
        var part = new ModelPart(List.of(), children);
        var pose = PartPose.offsetAndRotation(1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
        part.setInitialPose(pose); part.loadPose(pose); return part;
    }
    private static ModelPart spider6() {
        var children = new LinkedHashMap<String, ModelPart>();
        
        var part = new ModelPart(List.of(new ReferenceMeshCube(-4.75F, -2.75F, -2.75F, 5.5F, 13.5F, 5.5F, new float[][][]{{{-4.75F, -2.75F, -2.75F, 0.5F, 0.3125F}, {-4.75F, -2.75F, 2.75F, 0.54166667F, 0.3125F}, {-4.75F, 10.75F, 2.75F, 0.54166667F, 0.5F}, {-4.75F, 10.75F, -2.75F, 0.5F, 0.5F}}, {{0.75F, -2.75F, 2.75F, 0.41666667F, 0.3125F}, {0.75F, -2.75F, -2.75F, 0.45833333F, 0.3125F}, {0.75F, 10.75F, -2.75F, 0.45833333F, 0.5F}, {0.75F, 10.75F, 2.75F, 0.41666667F, 0.5F}}, {{0.75F, -2.75F, -2.75F, 0.45833333F, 0.3125F}, {-4.75F, -2.75F, -2.75F, 0.5F, 0.3125F}, {-4.75F, 10.75F, -2.75F, 0.5F, 0.5F}, {0.75F, 10.75F, -2.75F, 0.45833333F, 0.5F}}, {{-4.75F, -2.75F, 2.75F, 0.54166667F, 0.3125F}, {0.75F, -2.75F, 2.75F, 0.58333333F, 0.3125F}, {0.75F, 10.75F, 2.75F, 0.58333333F, 0.5F}, {-4.75F, 10.75F, 2.75F, 0.54166667F, 0.5F}}, {{0.75F, -2.75F, 2.75F, 0.45833333F, 0.25F}, {-4.75F, -2.75F, 2.75F, 0.5F, 0.25F}, {-4.75F, -2.75F, -2.75F, 0.5F, 0.3125F}, {0.75F, -2.75F, -2.75F, 0.45833333F, 0.3125F}}, {{0.75F, 10.75F, -2.75F, 0.5F, 0.3125F}, {-4.75F, 10.75F, -2.75F, 0.54166667F, 0.3125F}, {-4.75F, 10.75F, 2.75F, 0.54166667F, 0.25F}, {0.75F, 10.75F, 2.75F, 0.5F, 0.25F}}})), children);
        var pose = PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
        part.setInitialPose(pose); part.loadPose(pose); return part;
    }
    private static ModelPart spider7() {
        var children = new LinkedHashMap<String, ModelPart>();
        children.put("armorLeftArm", spider8());
        var part = new ModelPart(List.of(), children);
        var pose = PartPose.offsetAndRotation(-1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
        part.setInitialPose(pose); part.loadPose(pose); return part;
    }
    private static ModelPart spider8() {
        var children = new LinkedHashMap<String, ModelPart>();
        
        var part = new ModelPart(List.of(new ReferenceMeshCube(-0.75F, -2.75F, -2.75F, 5.5F, 13.5F, 5.5F, new float[][][]{{{4.75F, -2.75F, 2.75F, 0.54166667F, 0.3125F}, {4.75F, -2.75F, -2.75F, 0.5F, 0.3125F}, {4.75F, 10.75F, -2.75F, 0.5F, 0.5F}, {4.75F, 10.75F, 2.75F, 0.54166667F, 0.5F}}, {{-0.75F, -2.75F, -2.75F, 0.45833333F, 0.3125F}, {-0.75F, -2.75F, 2.75F, 0.41666667F, 0.3125F}, {-0.75F, 10.75F, 2.75F, 0.41666667F, 0.5F}, {-0.75F, 10.75F, -2.75F, 0.45833333F, 0.5F}}, {{4.75F, -2.75F, -2.75F, 0.5F, 0.3125F}, {-0.75F, -2.75F, -2.75F, 0.45833333F, 0.3125F}, {-0.75F, 10.75F, -2.75F, 0.45833333F, 0.5F}, {4.75F, 10.75F, -2.75F, 0.5F, 0.5F}}, {{-0.75F, -2.75F, 2.75F, 0.58333333F, 0.3125F}, {4.75F, -2.75F, 2.75F, 0.54166667F, 0.3125F}, {4.75F, 10.75F, 2.75F, 0.54166667F, 0.5F}, {-0.75F, 10.75F, 2.75F, 0.58333333F, 0.5F}}, {{4.75F, -2.75F, 2.75F, 0.5F, 0.25F}, {-0.75F, -2.75F, 2.75F, 0.45833333F, 0.25F}, {-0.75F, -2.75F, -2.75F, 0.45833333F, 0.3125F}, {4.75F, -2.75F, -2.75F, 0.5F, 0.3125F}}, {{4.75F, 10.75F, -2.75F, 0.54166667F, 0.3125F}, {-0.75F, 10.75F, -2.75F, 0.5F, 0.3125F}, {-0.75F, 10.75F, 2.75F, 0.5F, 0.25F}, {4.75F, 10.75F, 2.75F, 0.54166667F, 0.25F}}})), children);
        var pose = PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
        part.setInitialPose(pose); part.loadPose(pose); return part;
    }
    private static ModelPart spider9() {
        var children = new LinkedHashMap<String, ModelPart>();
        children.put("armorLeftLeg", spider10());
        children.put("armorLeftBoot", spider11());
        var part = new ModelPart(List.of(), children);
        var pose = PartPose.offsetAndRotation(0.1F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
        part.setInitialPose(pose); part.loadPose(pose); return part;
    }
    private static ModelPart spider10() {
        var children = new LinkedHashMap<String, ModelPart>();
        
        var part = new ModelPart(List.of(new ReferenceMeshCube(-2.75F, -0.75F, -2.75F, 5.5F, 13.5F, 5.5F, new float[][][]{{{-2.75F, -0.75F, -2.75F, 0.66666667F, 0.3125F}, {-2.75F, -0.75F, 2.75F, 0.70833333F, 0.3125F}, {-2.75F, 12.75F, 2.75F, 0.70833333F, 0.5F}, {-2.75F, 12.75F, -2.75F, 0.66666667F, 0.5F}}, {{2.75F, -0.75F, 2.75F, 0.58333333F, 0.3125F}, {2.75F, -0.75F, -2.75F, 0.625F, 0.3125F}, {2.75F, 12.75F, -2.75F, 0.625F, 0.5F}, {2.75F, 12.75F, 2.75F, 0.58333333F, 0.5F}}, {{2.75F, -0.75F, -2.75F, 0.625F, 0.3125F}, {-2.75F, -0.75F, -2.75F, 0.66666667F, 0.3125F}, {-2.75F, 12.75F, -2.75F, 0.66666667F, 0.5F}, {2.75F, 12.75F, -2.75F, 0.625F, 0.5F}}, {{-2.75F, -0.75F, 2.75F, 0.70833333F, 0.3125F}, {2.75F, -0.75F, 2.75F, 0.75F, 0.3125F}, {2.75F, 12.75F, 2.75F, 0.75F, 0.5F}, {-2.75F, 12.75F, 2.75F, 0.70833333F, 0.5F}}, {{2.75F, -0.75F, 2.75F, 0.625F, 0.25F}, {-2.75F, -0.75F, 2.75F, 0.66666667F, 0.25F}, {-2.75F, -0.75F, -2.75F, 0.66666667F, 0.3125F}, {2.75F, -0.75F, -2.75F, 0.625F, 0.3125F}}, {{2.75F, 12.75F, -2.75F, 0.66666667F, 0.3125F}, {-2.75F, 12.75F, -2.75F, 0.70833333F, 0.3125F}, {-2.75F, 12.75F, 2.75F, 0.70833333F, 0.25F}, {2.75F, 12.75F, 2.75F, 0.66666667F, 0.25F}}})), children);
        var pose = PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
        part.setInitialPose(pose); part.loadPose(pose); return part;
    }
    private static ModelPart spider11() {
        var children = new LinkedHashMap<String, ModelPart>();
        
        var part = new ModelPart(List.of(new ReferenceMeshCube(-2.5F, -0.5F, -2.5F, 5.0F, 13.0F, 5.0F, new float[][][]{{{-2.5F, -0.5F, -2.5F, 0.083333333F, 0.3125F}, {-2.5F, -0.5F, 2.5F, 0.125F, 0.3125F}, {-2.5F, 12.5F, 2.5F, 0.125F, 0.5F}, {-2.5F, 12.5F, -2.5F, 0.083333333F, 0.5F}}, {{2.5F, -0.5F, 2.5F, 0.0F, 0.3125F}, {2.5F, -0.5F, -2.5F, 0.041666667F, 0.3125F}, {2.5F, 12.5F, -2.5F, 0.041666667F, 0.5F}, {2.5F, 12.5F, 2.5F, 0.0F, 0.5F}}, {{2.5F, -0.5F, -2.5F, 0.041666667F, 0.3125F}, {-2.5F, -0.5F, -2.5F, 0.083333333F, 0.3125F}, {-2.5F, 12.5F, -2.5F, 0.083333333F, 0.5F}, {2.5F, 12.5F, -2.5F, 0.041666667F, 0.5F}}, {{-2.5F, -0.5F, 2.5F, 0.125F, 0.3125F}, {2.5F, -0.5F, 2.5F, 0.16666667F, 0.3125F}, {2.5F, 12.5F, 2.5F, 0.16666667F, 0.5F}, {-2.5F, 12.5F, 2.5F, 0.125F, 0.5F}}, {{2.5F, -0.5F, 2.5F, 0.041666667F, 0.25F}, {-2.5F, -0.5F, 2.5F, 0.083333333F, 0.25F}, {-2.5F, -0.5F, -2.5F, 0.083333333F, 0.3125F}, {2.5F, -0.5F, -2.5F, 0.041666667F, 0.3125F}}, {{2.5F, 12.5F, -2.5F, 0.083333333F, 0.3125F}, {-2.5F, 12.5F, -2.5F, 0.125F, 0.3125F}, {-2.5F, 12.5F, 2.5F, 0.125F, 0.25F}, {2.5F, 12.5F, 2.5F, 0.083333333F, 0.25F}}})), children);
        var pose = PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
        part.setInitialPose(pose); part.loadPose(pose); return part;
    }
    private static ModelPart spider12() {
        var children = new LinkedHashMap<String, ModelPart>();
        children.put("armorRightLeg", spider13());
        children.put("armorRightBoot", spider14());
        var part = new ModelPart(List.of(), children);
        var pose = PartPose.offsetAndRotation(-0.1F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
        part.setInitialPose(pose); part.loadPose(pose); return part;
    }
    private static ModelPart spider13() {
        var children = new LinkedHashMap<String, ModelPart>();
        
        var part = new ModelPart(List.of(new ReferenceMeshCube(-2.75F, -0.75F, -2.75F, 5.5F, 13.5F, 5.5F, new float[][][]{{{2.75F, -0.75F, 2.75F, 0.70833333F, 0.3125F}, {2.75F, -0.75F, -2.75F, 0.66666667F, 0.3125F}, {2.75F, 12.75F, -2.75F, 0.66666667F, 0.5F}, {2.75F, 12.75F, 2.75F, 0.70833333F, 0.5F}}, {{-2.75F, -0.75F, -2.75F, 0.625F, 0.3125F}, {-2.75F, -0.75F, 2.75F, 0.58333333F, 0.3125F}, {-2.75F, 12.75F, 2.75F, 0.58333333F, 0.5F}, {-2.75F, 12.75F, -2.75F, 0.625F, 0.5F}}, {{2.75F, -0.75F, -2.75F, 0.66666667F, 0.3125F}, {-2.75F, -0.75F, -2.75F, 0.625F, 0.3125F}, {-2.75F, 12.75F, -2.75F, 0.625F, 0.5F}, {2.75F, 12.75F, -2.75F, 0.66666667F, 0.5F}}, {{-2.75F, -0.75F, 2.75F, 0.75F, 0.3125F}, {2.75F, -0.75F, 2.75F, 0.70833333F, 0.3125F}, {2.75F, 12.75F, 2.75F, 0.70833333F, 0.5F}, {-2.75F, 12.75F, 2.75F, 0.75F, 0.5F}}, {{2.75F, -0.75F, 2.75F, 0.66666667F, 0.25F}, {-2.75F, -0.75F, 2.75F, 0.625F, 0.25F}, {-2.75F, -0.75F, -2.75F, 0.625F, 0.3125F}, {2.75F, -0.75F, -2.75F, 0.66666667F, 0.3125F}}, {{2.75F, 12.75F, -2.75F, 0.70833333F, 0.3125F}, {-2.75F, 12.75F, -2.75F, 0.66666667F, 0.3125F}, {-2.75F, 12.75F, 2.75F, 0.66666667F, 0.25F}, {2.75F, 12.75F, 2.75F, 0.70833333F, 0.25F}}})), children);
        var pose = PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
        part.setInitialPose(pose); part.loadPose(pose); return part;
    }
    private static ModelPart spider14() {
        var children = new LinkedHashMap<String, ModelPart>();
        
        var part = new ModelPart(List.of(new ReferenceMeshCube(-2.5F, -0.5F, -2.5F, 5.0F, 13.0F, 5.0F, new float[][][]{{{2.5F, -0.5F, 2.5F, 0.125F, 0.3125F}, {2.5F, -0.5F, -2.5F, 0.083333333F, 0.3125F}, {2.5F, 12.5F, -2.5F, 0.083333333F, 0.5F}, {2.5F, 12.5F, 2.5F, 0.125F, 0.5F}}, {{-2.5F, -0.5F, -2.5F, 0.041666667F, 0.3125F}, {-2.5F, -0.5F, 2.5F, 0.0F, 0.3125F}, {-2.5F, 12.5F, 2.5F, 0.0F, 0.5F}, {-2.5F, 12.5F, -2.5F, 0.041666667F, 0.5F}}, {{2.5F, -0.5F, -2.5F, 0.083333333F, 0.3125F}, {-2.5F, -0.5F, -2.5F, 0.041666667F, 0.3125F}, {-2.5F, 12.5F, -2.5F, 0.041666667F, 0.5F}, {2.5F, 12.5F, -2.5F, 0.083333333F, 0.5F}}, {{-2.5F, -0.5F, 2.5F, 0.16666667F, 0.3125F}, {2.5F, -0.5F, 2.5F, 0.125F, 0.3125F}, {2.5F, 12.5F, 2.5F, 0.125F, 0.5F}, {-2.5F, 12.5F, 2.5F, 0.16666667F, 0.5F}}, {{2.5F, -0.5F, 2.5F, 0.083333333F, 0.25F}, {-2.5F, -0.5F, 2.5F, 0.041666667F, 0.25F}, {-2.5F, -0.5F, -2.5F, 0.041666667F, 0.3125F}, {2.5F, -0.5F, -2.5F, 0.083333333F, 0.3125F}}, {{2.5F, 12.5F, -2.5F, 0.125F, 0.3125F}, {-2.5F, 12.5F, -2.5F, 0.083333333F, 0.3125F}, {-2.5F, 12.5F, 2.5F, 0.083333333F, 0.25F}, {2.5F, 12.5F, 2.5F, 0.125F, 0.25F}}})), children);
        var pose = PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
        part.setInitialPose(pose); part.loadPose(pose); return part;
    }
    private static ModelPart spider() {
        var root = new LinkedHashMap<String, ModelPart>();
        root.put("head", socket("bipedHead", spider0(), 0.0F, 0.0F, 0.0F));
        root.put("body", socket("bipedBody", spider3(), 0.0F, 0.0F, 0.0F));
        root.put("right_arm", socket("bipedRightArm", spider5(), -5.0F, 2.0F, 0.0F));
        root.put("left_arm", socket("bipedLeftArm", spider7(), 5.0F, 2.0F, 0.0F));
        root.put("right_leg", socket("bipedRightLeg", spider12(), -1.9F, 12.0F, 0.0F));
        root.put("left_leg", socket("bipedLeftLeg", spider9(), 1.9F, 12.0F, 0.0F));
        root.put("hat", new ModelPart(List.of(), Map.of()));
        return new ModelPart(List.of(), root);
    }
    private static ModelPart battle_robes0() {
        var children = new LinkedHashMap<String, ModelPart>();
        children.put("armorHead", battle_robes1());
        var part = new ModelPart(List.of(), children);
        var pose = PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
        part.setInitialPose(pose); part.loadPose(pose); return part;
    }
    private static ModelPart battle_robes1() {
        var children = new LinkedHashMap<String, ModelPart>();
        
        var part = new ModelPart(List.of(new ReferenceMeshCube(-5.0F, -10.0F, -5.0F, 10.0F, 12.0F, 10.0F, new float[][][]{{{5.0F, -10.0F, 5.0F, 0.25F, 0.12121212F}, {5.0F, -10.0F, -5.0F, 0.16666667F, 0.12121212F}, {5.0F, 2.0F, -5.0F, 0.16666667F, 0.27272727F}, {5.0F, 2.0F, 5.0F, 0.25F, 0.27272727F}}, {{-5.0F, -10.0F, -5.0F, 0.083333333F, 0.12121212F}, {-5.0F, -10.0F, 5.0F, 0.0F, 0.12121212F}, {-5.0F, 2.0F, 5.0F, 0.0F, 0.27272727F}, {-5.0F, 2.0F, -5.0F, 0.083333333F, 0.27272727F}}, {{5.0F, -10.0F, -5.0F, 0.16666667F, 0.12121212F}, {-5.0F, -10.0F, -5.0F, 0.083333333F, 0.12121212F}, {-5.0F, 2.0F, -5.0F, 0.083333333F, 0.27272727F}, {5.0F, 2.0F, -5.0F, 0.16666667F, 0.27272727F}}, {{-5.0F, -10.0F, 5.0F, 0.33333333F, 0.12121212F}, {5.0F, -10.0F, 5.0F, 0.25F, 0.12121212F}, {5.0F, 2.0F, 5.0F, 0.25F, 0.27272727F}, {-5.0F, 2.0F, 5.0F, 0.33333333F, 0.27272727F}}, {{5.0F, -10.0F, 5.0F, 0.16666667F, 0.0F}, {-5.0F, -10.0F, 5.0F, 0.083333333F, 0.0F}, {-5.0F, -10.0F, -5.0F, 0.083333333F, 0.12121212F}, {5.0F, -10.0F, -5.0F, 0.16666667F, 0.12121212F}}, {{5.0F, 2.0F, -5.0F, 0.25F, 0.12121212F}, {-5.0F, 2.0F, -5.0F, 0.16666667F, 0.12121212F}, {-5.0F, 2.0F, 5.0F, 0.16666667F, 0.0F}, {5.0F, 2.0F, 5.0F, 0.25F, 0.0F}}})), children);
        var pose = PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
        part.setInitialPose(pose); part.loadPose(pose); return part;
    }
    private static ModelPart battle_robes2() {
        var children = new LinkedHashMap<String, ModelPart>();
        children.put("armorBody", battle_robes3());
        var part = new ModelPart(List.of(), children);
        var pose = PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
        part.setInitialPose(pose); part.loadPose(pose); return part;
    }
    private static ModelPart battle_robes3() {
        var children = new LinkedHashMap<String, ModelPart>();
        
        var part = new ModelPart(List.of(new ReferenceMeshCube(-4.75F, -0.75F, -2.75F, 9.5F, 13.5F, 5.5F, new float[][][]{{{4.75F, -0.75F, 2.75F, 0.33333333F, 0.33333333F}, {4.75F, -0.75F, -2.75F, 0.29166667F, 0.33333333F}, {4.75F, 12.75F, -2.75F, 0.29166667F, 0.51515152F}, {4.75F, 12.75F, 2.75F, 0.33333333F, 0.51515152F}}, {{-4.75F, -0.75F, -2.75F, 0.20833333F, 0.33333333F}, {-4.75F, -0.75F, 2.75F, 0.16666667F, 0.33333333F}, {-4.75F, 12.75F, 2.75F, 0.16666667F, 0.51515152F}, {-4.75F, 12.75F, -2.75F, 0.20833333F, 0.51515152F}}, {{4.75F, -0.75F, -2.75F, 0.29166667F, 0.33333333F}, {-4.75F, -0.75F, -2.75F, 0.20833333F, 0.33333333F}, {-4.75F, 12.75F, -2.75F, 0.20833333F, 0.51515152F}, {4.75F, 12.75F, -2.75F, 0.29166667F, 0.51515152F}}, {{-4.75F, -0.75F, 2.75F, 0.41666667F, 0.33333333F}, {4.75F, -0.75F, 2.75F, 0.33333333F, 0.33333333F}, {4.75F, 12.75F, 2.75F, 0.33333333F, 0.51515152F}, {-4.75F, 12.75F, 2.75F, 0.41666667F, 0.51515152F}}, {{4.75F, -0.75F, 2.75F, 0.29166667F, 0.27272727F}, {-4.75F, -0.75F, 2.75F, 0.20833333F, 0.27272727F}, {-4.75F, -0.75F, -2.75F, 0.20833333F, 0.33333333F}, {4.75F, -0.75F, -2.75F, 0.29166667F, 0.33333333F}}, {{4.75F, 12.75F, -2.75F, 0.375F, 0.33333333F}, {-4.75F, 12.75F, -2.75F, 0.29166667F, 0.33333333F}, {-4.75F, 12.75F, 2.75F, 0.29166667F, 0.27272727F}, {4.75F, 12.75F, 2.75F, 0.375F, 0.27272727F}}}),
            new ReferenceMeshCube(-4.75F, 12.75F, -2.75F, 9.5F, 5.5F, 5.5F, new float[][][]{{{4.75F, 12.75F, 2.75F, 0.75F, 0.060606061F}, {4.75F, 12.75F, -2.75F, 0.70833333F, 0.060606061F}, {4.75F, 18.25F, -2.75F, 0.70833333F, 0.12121212F}, {4.75F, 18.25F, 2.75F, 0.75F, 0.12121212F}}, {{-4.75F, 12.75F, -2.75F, 0.625F, 0.060606061F}, {-4.75F, 12.75F, 2.75F, 0.58333333F, 0.060606061F}, {-4.75F, 18.25F, 2.75F, 0.58333333F, 0.12121212F}, {-4.75F, 18.25F, -2.75F, 0.625F, 0.12121212F}}, {{4.75F, 12.75F, -2.75F, 0.70833333F, 0.060606061F}, {-4.75F, 12.75F, -2.75F, 0.625F, 0.060606061F}, {-4.75F, 18.25F, -2.75F, 0.625F, 0.12121212F}, {4.75F, 18.25F, -2.75F, 0.70833333F, 0.12121212F}}, {{-4.75F, 12.75F, 2.75F, 0.83333333F, 0.060606061F}, {4.75F, 12.75F, 2.75F, 0.75F, 0.060606061F}, {4.75F, 18.25F, 2.75F, 0.75F, 0.12121212F}, {-4.75F, 18.25F, 2.75F, 0.83333333F, 0.12121212F}}, {{4.75F, 12.75F, 2.75F, 0.70833333F, 0.0F}, {-4.75F, 12.75F, 2.75F, 0.625F, 0.0F}, {-4.75F, 12.75F, -2.75F, 0.625F, 0.060606061F}, {4.75F, 12.75F, -2.75F, 0.70833333F, 0.060606061F}}, {{4.75F, 18.25F, -2.75F, 0.79166667F, 0.060606061F}, {-4.75F, 18.25F, -2.75F, 0.70833333F, 0.060606061F}, {-4.75F, 18.25F, 2.75F, 0.70833333F, 0.0F}, {4.75F, 18.25F, 2.75F, 0.79166667F, 0.0F}}})), children);
        var pose = PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
        part.setInitialPose(pose); part.loadPose(pose); return part;
    }
    private static ModelPart battle_robes4() {
        var children = new LinkedHashMap<String, ModelPart>();
        children.put("armorRightArm", battle_robes5());
        var part = new ModelPart(List.of(), children);
        var pose = PartPose.offsetAndRotation(1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
        part.setInitialPose(pose); part.loadPose(pose); return part;
    }
    private static ModelPart battle_robes5() {
        var children = new LinkedHashMap<String, ModelPart>();
        
        var part = new ModelPart(List.of(new ReferenceMeshCube(-5.5F, -3.0F, -3.0F, 6.0F, 14.0F, 6.0F, new float[][][]{{{0.5F, -3.0F, 3.0F, 0.54166667F, 0.33333333F}, {0.5F, -3.0F, -3.0F, 0.5F, 0.33333333F}, {0.5F, 11.0F, -3.0F, 0.5F, 0.51515152F}, {0.5F, 11.0F, 3.0F, 0.54166667F, 0.51515152F}}, {{-5.5F, -3.0F, -3.0F, 0.45833333F, 0.33333333F}, {-5.5F, -3.0F, 3.0F, 0.41666667F, 0.33333333F}, {-5.5F, 11.0F, 3.0F, 0.41666667F, 0.51515152F}, {-5.5F, 11.0F, -3.0F, 0.45833333F, 0.51515152F}}, {{0.5F, -3.0F, -3.0F, 0.5F, 0.33333333F}, {-5.5F, -3.0F, -3.0F, 0.45833333F, 0.33333333F}, {-5.5F, 11.0F, -3.0F, 0.45833333F, 0.51515152F}, {0.5F, 11.0F, -3.0F, 0.5F, 0.51515152F}}, {{-5.5F, -3.0F, 3.0F, 0.58333333F, 0.33333333F}, {0.5F, -3.0F, 3.0F, 0.54166667F, 0.33333333F}, {0.5F, 11.0F, 3.0F, 0.54166667F, 0.51515152F}, {-5.5F, 11.0F, 3.0F, 0.58333333F, 0.51515152F}}, {{0.5F, -3.0F, 3.0F, 0.5F, 0.27272727F}, {-5.5F, -3.0F, 3.0F, 0.45833333F, 0.27272727F}, {-5.5F, -3.0F, -3.0F, 0.45833333F, 0.33333333F}, {0.5F, -3.0F, -3.0F, 0.5F, 0.33333333F}}, {{0.5F, 11.0F, -3.0F, 0.54166667F, 0.33333333F}, {-5.5F, 11.0F, -3.0F, 0.5F, 0.33333333F}, {-5.5F, 11.0F, 3.0F, 0.5F, 0.27272727F}, {0.5F, 11.0F, 3.0F, 0.54166667F, 0.27272727F}}}),
            new ReferenceMeshCube(-4.5F, -2.5F, -2.5F, 5.0F, 13.0F, 5.0F, new float[][][]{{{0.5F, -2.5F, 2.5F, 0.95833333F, 0.060606061F}, {0.5F, -2.5F, -2.5F, 0.91666667F, 0.060606061F}, {0.5F, 10.5F, -2.5F, 0.91666667F, 0.24242424F}, {0.5F, 10.5F, 2.5F, 0.95833333F, 0.24242424F}}, {{-4.5F, -2.5F, -2.5F, 0.875F, 0.060606061F}, {-4.5F, -2.5F, 2.5F, 0.83333333F, 0.060606061F}, {-4.5F, 10.5F, 2.5F, 0.83333333F, 0.24242424F}, {-4.5F, 10.5F, -2.5F, 0.875F, 0.24242424F}}, {{0.5F, -2.5F, -2.5F, 0.91666667F, 0.060606061F}, {-4.5F, -2.5F, -2.5F, 0.875F, 0.060606061F}, {-4.5F, 10.5F, -2.5F, 0.875F, 0.24242424F}, {0.5F, 10.5F, -2.5F, 0.91666667F, 0.24242424F}}, {{-4.5F, -2.5F, 2.5F, 1.0F, 0.060606061F}, {0.5F, -2.5F, 2.5F, 0.95833333F, 0.060606061F}, {0.5F, 10.5F, 2.5F, 0.95833333F, 0.24242424F}, {-4.5F, 10.5F, 2.5F, 1.0F, 0.24242424F}}, {{0.5F, -2.5F, 2.5F, 0.91666667F, 0.0F}, {-4.5F, -2.5F, 2.5F, 0.875F, 0.0F}, {-4.5F, -2.5F, -2.5F, 0.875F, 0.060606061F}, {0.5F, -2.5F, -2.5F, 0.91666667F, 0.060606061F}}, {{0.5F, 10.5F, -2.5F, 0.95833333F, 0.060606061F}, {-4.5F, 10.5F, -2.5F, 0.91666667F, 0.060606061F}, {-4.5F, 10.5F, 2.5F, 0.91666667F, 0.0F}, {0.5F, 10.5F, 2.5F, 0.95833333F, 0.0F}}})), children);
        var pose = PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
        part.setInitialPose(pose); part.loadPose(pose); return part;
    }
    private static ModelPart battle_robes6() {
        var children = new LinkedHashMap<String, ModelPart>();
        children.put("armorLeftArm", battle_robes7());
        var part = new ModelPart(List.of(), children);
        var pose = PartPose.offsetAndRotation(-1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
        part.setInitialPose(pose); part.loadPose(pose); return part;
    }
    private static ModelPart battle_robes7() {
        var children = new LinkedHashMap<String, ModelPart>();
        
        var part = new ModelPart(List.of(new ReferenceMeshCube(-0.5F, -3.0F, -3.0F, 6.0F, 14.0F, 6.0F, new float[][][]{{{-0.5F, -3.0F, -3.0F, 0.5F, 0.33333333F}, {-0.5F, -3.0F, 3.0F, 0.54166667F, 0.33333333F}, {-0.5F, 11.0F, 3.0F, 0.54166667F, 0.51515152F}, {-0.5F, 11.0F, -3.0F, 0.5F, 0.51515152F}}, {{5.5F, -3.0F, 3.0F, 0.41666667F, 0.33333333F}, {5.5F, -3.0F, -3.0F, 0.45833333F, 0.33333333F}, {5.5F, 11.0F, -3.0F, 0.45833333F, 0.51515152F}, {5.5F, 11.0F, 3.0F, 0.41666667F, 0.51515152F}}, {{5.5F, -3.0F, -3.0F, 0.45833333F, 0.33333333F}, {-0.5F, -3.0F, -3.0F, 0.5F, 0.33333333F}, {-0.5F, 11.0F, -3.0F, 0.5F, 0.51515152F}, {5.5F, 11.0F, -3.0F, 0.45833333F, 0.51515152F}}, {{-0.5F, -3.0F, 3.0F, 0.54166667F, 0.33333333F}, {5.5F, -3.0F, 3.0F, 0.58333333F, 0.33333333F}, {5.5F, 11.0F, 3.0F, 0.58333333F, 0.51515152F}, {-0.5F, 11.0F, 3.0F, 0.54166667F, 0.51515152F}}, {{5.5F, -3.0F, 3.0F, 0.45833333F, 0.27272727F}, {-0.5F, -3.0F, 3.0F, 0.5F, 0.27272727F}, {-0.5F, -3.0F, -3.0F, 0.5F, 0.33333333F}, {5.5F, -3.0F, -3.0F, 0.45833333F, 0.33333333F}}, {{5.5F, 11.0F, -3.0F, 0.5F, 0.33333333F}, {-0.5F, 11.0F, -3.0F, 0.54166667F, 0.33333333F}, {-0.5F, 11.0F, 3.0F, 0.54166667F, 0.27272727F}, {5.5F, 11.0F, 3.0F, 0.5F, 0.27272727F}}}),
            new ReferenceMeshCube(-0.5F, -2.5F, -2.5F, 5.0F, 13.0F, 5.0F, new float[][][]{{{-0.5F, -2.5F, -2.5F, 0.91666667F, 0.060606061F}, {-0.5F, -2.5F, 2.5F, 0.95833333F, 0.060606061F}, {-0.5F, 10.5F, 2.5F, 0.95833333F, 0.24242424F}, {-0.5F, 10.5F, -2.5F, 0.91666667F, 0.24242424F}}, {{4.5F, -2.5F, 2.5F, 0.83333333F, 0.060606061F}, {4.5F, -2.5F, -2.5F, 0.875F, 0.060606061F}, {4.5F, 10.5F, -2.5F, 0.875F, 0.24242424F}, {4.5F, 10.5F, 2.5F, 0.83333333F, 0.24242424F}}, {{4.5F, -2.5F, -2.5F, 0.875F, 0.060606061F}, {-0.5F, -2.5F, -2.5F, 0.91666667F, 0.060606061F}, {-0.5F, 10.5F, -2.5F, 0.91666667F, 0.24242424F}, {4.5F, 10.5F, -2.5F, 0.875F, 0.24242424F}}, {{-0.5F, -2.5F, 2.5F, 0.95833333F, 0.060606061F}, {4.5F, -2.5F, 2.5F, 1.0F, 0.060606061F}, {4.5F, 10.5F, 2.5F, 1.0F, 0.24242424F}, {-0.5F, 10.5F, 2.5F, 0.95833333F, 0.24242424F}}, {{4.5F, -2.5F, 2.5F, 0.875F, 0.0F}, {-0.5F, -2.5F, 2.5F, 0.91666667F, 0.0F}, {-0.5F, -2.5F, -2.5F, 0.91666667F, 0.060606061F}, {4.5F, -2.5F, -2.5F, 0.875F, 0.060606061F}}, {{4.5F, 10.5F, -2.5F, 0.91666667F, 0.060606061F}, {-0.5F, 10.5F, -2.5F, 0.95833333F, 0.060606061F}, {-0.5F, 10.5F, 2.5F, 0.95833333F, 0.0F}, {4.5F, 10.5F, 2.5F, 0.91666667F, 0.0F}}})), children);
        var pose = PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
        part.setInitialPose(pose); part.loadPose(pose); return part;
    }
    private static ModelPart battle_robes8() {
        var children = new LinkedHashMap<String, ModelPart>();
        children.put("armorLeftLeg", battle_robes9());
        children.put("armorLeftBoot", battle_robes10());
        var part = new ModelPart(List.of(), children);
        var pose = PartPose.offsetAndRotation(0.1F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
        part.setInitialPose(pose); part.loadPose(pose); return part;
    }
    private static ModelPart battle_robes9() {
        var children = new LinkedHashMap<String, ModelPart>();
        
        var part = new ModelPart(List.of(new ReferenceMeshCube(-2.5F, -0.5F, -2.5F, 5.0F, 13.0F, 5.0F, new float[][][]{{{-2.5F, -0.5F, -2.5F, 0.66666667F, 0.33333333F}, {-2.5F, -0.5F, 2.5F, 0.70833333F, 0.33333333F}, {-2.5F, 12.5F, 2.5F, 0.70833333F, 0.51515152F}, {-2.5F, 12.5F, -2.5F, 0.66666667F, 0.51515152F}}, {{2.5F, -0.5F, 2.5F, 0.58333333F, 0.33333333F}, {2.5F, -0.5F, -2.5F, 0.625F, 0.33333333F}, {2.5F, 12.5F, -2.5F, 0.625F, 0.51515152F}, {2.5F, 12.5F, 2.5F, 0.58333333F, 0.51515152F}}, {{2.5F, -0.5F, -2.5F, 0.625F, 0.33333333F}, {-2.5F, -0.5F, -2.5F, 0.66666667F, 0.33333333F}, {-2.5F, 12.5F, -2.5F, 0.66666667F, 0.51515152F}, {2.5F, 12.5F, -2.5F, 0.625F, 0.51515152F}}, {{-2.5F, -0.5F, 2.5F, 0.70833333F, 0.33333333F}, {2.5F, -0.5F, 2.5F, 0.75F, 0.33333333F}, {2.5F, 12.5F, 2.5F, 0.75F, 0.51515152F}, {-2.5F, 12.5F, 2.5F, 0.70833333F, 0.51515152F}}, {{2.5F, -0.5F, 2.5F, 0.625F, 0.27272727F}, {-2.5F, -0.5F, 2.5F, 0.66666667F, 0.27272727F}, {-2.5F, -0.5F, -2.5F, 0.66666667F, 0.33333333F}, {2.5F, -0.5F, -2.5F, 0.625F, 0.33333333F}}, {{2.5F, 12.5F, -2.5F, 0.66666667F, 0.33333333F}, {-2.5F, 12.5F, -2.5F, 0.70833333F, 0.33333333F}, {-2.5F, 12.5F, 2.5F, 0.70833333F, 0.27272727F}, {2.5F, 12.5F, 2.5F, 0.66666667F, 0.27272727F}}})), children);
        var pose = PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
        part.setInitialPose(pose); part.loadPose(pose); return part;
    }
    private static ModelPart battle_robes10() {
        var children = new LinkedHashMap<String, ModelPart>();
        
        var part = new ModelPart(List.of(new ReferenceMeshCube(-2.75F, -0.75F, -2.75F, 5.5F, 13.5F, 5.5F, new float[][][]{{{-2.75F, -0.75F, -2.75F, 0.083333333F, 0.33333333F}, {-2.75F, -0.75F, 2.75F, 0.125F, 0.33333333F}, {-2.75F, 12.75F, 2.75F, 0.125F, 0.51515152F}, {-2.75F, 12.75F, -2.75F, 0.083333333F, 0.51515152F}}, {{2.75F, -0.75F, 2.75F, 0.0F, 0.33333333F}, {2.75F, -0.75F, -2.75F, 0.041666667F, 0.33333333F}, {2.75F, 12.75F, -2.75F, 0.041666667F, 0.51515152F}, {2.75F, 12.75F, 2.75F, 0.0F, 0.51515152F}}, {{2.75F, -0.75F, -2.75F, 0.041666667F, 0.33333333F}, {-2.75F, -0.75F, -2.75F, 0.083333333F, 0.33333333F}, {-2.75F, 12.75F, -2.75F, 0.083333333F, 0.51515152F}, {2.75F, 12.75F, -2.75F, 0.041666667F, 0.51515152F}}, {{-2.75F, -0.75F, 2.75F, 0.125F, 0.33333333F}, {2.75F, -0.75F, 2.75F, 0.16666667F, 0.33333333F}, {2.75F, 12.75F, 2.75F, 0.16666667F, 0.51515152F}, {-2.75F, 12.75F, 2.75F, 0.125F, 0.51515152F}}, {{2.75F, -0.75F, 2.75F, 0.041666667F, 0.27272727F}, {-2.75F, -0.75F, 2.75F, 0.083333333F, 0.27272727F}, {-2.75F, -0.75F, -2.75F, 0.083333333F, 0.33333333F}, {2.75F, -0.75F, -2.75F, 0.041666667F, 0.33333333F}}, {{2.75F, 12.75F, -2.75F, 0.083333333F, 0.33333333F}, {-2.75F, 12.75F, -2.75F, 0.125F, 0.33333333F}, {-2.75F, 12.75F, 2.75F, 0.125F, 0.27272727F}, {2.75F, 12.75F, 2.75F, 0.083333333F, 0.27272727F}}})), children);
        var pose = PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
        part.setInitialPose(pose); part.loadPose(pose); return part;
    }
    private static ModelPart battle_robes11() {
        var children = new LinkedHashMap<String, ModelPart>();
        children.put("armorRightLeg", battle_robes12());
        children.put("armorRightBoot", battle_robes13());
        var part = new ModelPart(List.of(), children);
        var pose = PartPose.offsetAndRotation(-0.1F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
        part.setInitialPose(pose); part.loadPose(pose); return part;
    }
    private static ModelPart battle_robes12() {
        var children = new LinkedHashMap<String, ModelPart>();
        
        var part = new ModelPart(List.of(new ReferenceMeshCube(-2.5F, -0.5F, -2.5F, 5.0F, 13.0F, 5.0F, new float[][][]{{{2.5F, -0.5F, 2.5F, 0.70833333F, 0.33333333F}, {2.5F, -0.5F, -2.5F, 0.66666667F, 0.33333333F}, {2.5F, 12.5F, -2.5F, 0.66666667F, 0.51515152F}, {2.5F, 12.5F, 2.5F, 0.70833333F, 0.51515152F}}, {{-2.5F, -0.5F, -2.5F, 0.625F, 0.33333333F}, {-2.5F, -0.5F, 2.5F, 0.58333333F, 0.33333333F}, {-2.5F, 12.5F, 2.5F, 0.58333333F, 0.51515152F}, {-2.5F, 12.5F, -2.5F, 0.625F, 0.51515152F}}, {{2.5F, -0.5F, -2.5F, 0.66666667F, 0.33333333F}, {-2.5F, -0.5F, -2.5F, 0.625F, 0.33333333F}, {-2.5F, 12.5F, -2.5F, 0.625F, 0.51515152F}, {2.5F, 12.5F, -2.5F, 0.66666667F, 0.51515152F}}, {{-2.5F, -0.5F, 2.5F, 0.75F, 0.33333333F}, {2.5F, -0.5F, 2.5F, 0.70833333F, 0.33333333F}, {2.5F, 12.5F, 2.5F, 0.70833333F, 0.51515152F}, {-2.5F, 12.5F, 2.5F, 0.75F, 0.51515152F}}, {{2.5F, -0.5F, 2.5F, 0.66666667F, 0.27272727F}, {-2.5F, -0.5F, 2.5F, 0.625F, 0.27272727F}, {-2.5F, -0.5F, -2.5F, 0.625F, 0.33333333F}, {2.5F, -0.5F, -2.5F, 0.66666667F, 0.33333333F}}, {{2.5F, 12.5F, -2.5F, 0.70833333F, 0.33333333F}, {-2.5F, 12.5F, -2.5F, 0.66666667F, 0.33333333F}, {-2.5F, 12.5F, 2.5F, 0.66666667F, 0.27272727F}, {2.5F, 12.5F, 2.5F, 0.70833333F, 0.27272727F}}})), children);
        var pose = PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
        part.setInitialPose(pose); part.loadPose(pose); return part;
    }
    private static ModelPart battle_robes13() {
        var children = new LinkedHashMap<String, ModelPart>();
        
        var part = new ModelPart(List.of(new ReferenceMeshCube(-2.75F, -0.75F, -2.75F, 5.5F, 13.5F, 5.5F, new float[][][]{{{2.75F, -0.75F, 2.75F, 0.125F, 0.33333333F}, {2.75F, -0.75F, -2.75F, 0.083333333F, 0.33333333F}, {2.75F, 12.75F, -2.75F, 0.083333333F, 0.51515152F}, {2.75F, 12.75F, 2.75F, 0.125F, 0.51515152F}}, {{-2.75F, -0.75F, -2.75F, 0.041666667F, 0.33333333F}, {-2.75F, -0.75F, 2.75F, 0.0F, 0.33333333F}, {-2.75F, 12.75F, 2.75F, 0.0F, 0.51515152F}, {-2.75F, 12.75F, -2.75F, 0.041666667F, 0.51515152F}}, {{2.75F, -0.75F, -2.75F, 0.083333333F, 0.33333333F}, {-2.75F, -0.75F, -2.75F, 0.041666667F, 0.33333333F}, {-2.75F, 12.75F, -2.75F, 0.041666667F, 0.51515152F}, {2.75F, 12.75F, -2.75F, 0.083333333F, 0.51515152F}}, {{-2.75F, -0.75F, 2.75F, 0.16666667F, 0.33333333F}, {2.75F, -0.75F, 2.75F, 0.125F, 0.33333333F}, {2.75F, 12.75F, 2.75F, 0.125F, 0.51515152F}, {-2.75F, 12.75F, 2.75F, 0.16666667F, 0.51515152F}}, {{2.75F, -0.75F, 2.75F, 0.083333333F, 0.27272727F}, {-2.75F, -0.75F, 2.75F, 0.041666667F, 0.27272727F}, {-2.75F, -0.75F, -2.75F, 0.041666667F, 0.33333333F}, {2.75F, -0.75F, -2.75F, 0.083333333F, 0.33333333F}}, {{2.75F, 12.75F, -2.75F, 0.125F, 0.33333333F}, {-2.75F, 12.75F, -2.75F, 0.083333333F, 0.33333333F}, {-2.75F, 12.75F, 2.75F, 0.083333333F, 0.27272727F}, {2.75F, 12.75F, 2.75F, 0.125F, 0.27272727F}}})), children);
        var pose = PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
        part.setInitialPose(pose); part.loadPose(pose); return part;
    }
    private static ModelPart battle_robes() {
        var root = new LinkedHashMap<String, ModelPart>();
        root.put("head", socket("bipedHead", battle_robes0(), 0.0F, 0.0F, 0.0F));
        root.put("body", socket("bipedBody", battle_robes2(), 0.0F, 0.0F, 0.0F));
        root.put("right_arm", socket("bipedRightArm", battle_robes4(), -5.0F, 2.0F, 0.0F));
        root.put("left_arm", socket("bipedLeftArm", battle_robes6(), 5.0F, 2.0F, 0.0F));
        root.put("right_leg", socket("bipedRightLeg", battle_robes11(), -1.9F, 12.0F, 0.0F));
        root.put("left_leg", socket("bipedLeftLeg", battle_robes8(), 1.9F, 12.0F, 0.0F));
        root.put("hat", new ModelPart(List.of(), Map.of()));
        return new ModelPart(List.of(), root);
    }
}
