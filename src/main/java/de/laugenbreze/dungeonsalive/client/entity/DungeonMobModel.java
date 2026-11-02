package de.laugenbreze.dungeonsalive.client.entity;

import de.laugenbreze.dungeonsalive.entity.AttackKind;
import de.laugenbreze.dungeonsalive.entity.DungeonMob;
import de.laugenbreze.dungeonsalive.entity.MobProfile;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;

/** Original editable cuboid models. Eleven silhouette families, with per-profile accessories. */
public final class DungeonMobModel extends HierarchicalModel<DungeonMob> {
    private final ModelPart root, head, body, rightArm, leftArm, rightLeg, leftLeg, ornaments, tail;
    public DungeonMobModel(ModelPart root) {
        this.root = root;
        head = root.getChild("head"); body = root.getChild("body");
        rightArm = root.getChild("right_arm"); leftArm = root.getChild("left_arm");
        rightLeg = root.getChild("right_leg"); leftLeg = root.getChild("left_leg");
        ornaments = root.getChild("ornaments"); tail = root.getChild("tail");
    }
    @Override public ModelPart root() { return root; }
    private static PartDefinition cube(PartDefinition parent, String name, int u, int v,
                                       float x, float y, float z, float w, float h, float d,
                                       float px, float py, float pz) {
        return parent.addOrReplaceChild(name, CubeListBuilder.create().texOffs(u, v).addBox(x, y, z, w, h, d), PartPose.offset(px, py, pz));
    }
    public static LayerDefinition create(MobProfile p) {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        for (String part : new String[]{"head", "body", "right_arm", "left_arm", "right_leg", "left_leg", "ornaments", "tail"}) {
            root.addOrReplaceChild(part, CubeListBuilder.create(), PartPose.ZERO);
        }
        switch (p.family()) {
            case ILLAGER, SKELETON, ZOMBIE, PIGLIN -> humanoid(root, p);
            case CREEPER -> throw new IllegalArgumentException("Creepers require their dedicated four-legged reference rig");
            case WRAITH -> wraith(root, p);
            case GOLEM -> golem(root, p);
            case PLANT -> plant(root, p);
            case ENDERLING -> enderling(root, p);
            case CAULDRON -> cauldron(root);
            case GUARDIAN -> guardian(root);
            case WILDFIRE -> wildfire(root);
            case HORSEMAN -> horseman(root);
            case SLIME -> slime(root, p);
        }
        return LayerDefinition.create(mesh, 128, 128);
    }
    private static void humanoid(PartDefinition root, MobProfile p) {
        boolean skeleton = p.family() == MobProfile.Family.SKELETON;
        PartDefinition head = cube(root, "head", 0, 0, -4, -10, -4, 8, 10, 8, 0, 0, 0);
        if (!skeleton) cube(head, "nose", 40, 0, -1, -1, -2, 2, 4, 2, 0, -4, -4);
        cube(root, "body", 0, 32, -4, 0, -2, 8, 12, 4, 0, 0, 0);
        float limb = skeleton ? 2 : 4;
        PartDefinition right = cube(root, "right_arm", 64, 0, -limb, -1, -2, limb, 13, 4, -4, 1, 0);
        PartDefinition left = cube(root, "left_arm", 64, 0, 0, -1, -2, limb, 13, 4, 4, 1, 0);
        cube(root, "right_leg", 64, 32, -1.8f, 0, -2, skeleton ? 2 : 3.6f, 12, 4, -2.1f, 12, 0);
        cube(root, "left_leg", 64, 32, -1.8f, 0, -2, skeleton ? 2 : 3.6f, 12, 4, 2.1f, 12, 0);
        boolean caster = p.attacks().contains(AttackKind.SUMMON) || p.id().equals("enchanter") || p.id().equals("geomancer") || p.id().equals("wind_caller");
        if (caster) {
            cube(root.getChild("body"), "robe", 0, 64, -5, 4, -3, 10, 14, 6, 0, 0, 0);
            PartDefinition staff = cube(right, "staff", 96, 0, -.8f, -9, -.8f, 1.6f, 24, 1.6f, -2, 8, -3);
            cube(staff, "gem", 96, 32, -2.5f, -12, -2.5f, 5, 5, 5, 0, 0, 0);
        }
        if (p.id().equals("enchanter") || p.id().equals("illusioner") || p.id().equals("iceologer")) {
            cube(head, "hood", 32, 64, -4.5f, -11, -4.5f, 9, 3, 9, 0, 0, 0);
            cube(head, "hood_tail", 64, 64, -4.5f, -8, 2, 9, 9, 3, 0, 0, 0);
        }
        if (p.attacks().contains(AttackKind.GUARD)) {
            cube(left, "shield", 80, 64, -1, -4, -2, 7, 12, 2, 0, 6, -3);
            cube(right, "glaive", 96, 0, -.7f, -10, -.7f, 1.4f, 24, 1.4f, -2, 7, -3);
            cube(right, "blade", 96, 32, -2, -12, -1, 4, 7, 2, -2, 7, -3);
        }
        if (p.id().contains("captain")) {
            cube(root.getChild("body"), "banner_pole", 96, 0, -.7f, -24, -.7f, 1.4f, 34, 1.4f, 0, 3, 4);
            cube(root.getChild("body"), "banner", 80, 64, -5, -23, 0, 10, 14, 1, 0, 3, 4);
        }
        if (p.id().equals("royal_guard") || p.id().contains("armored") || p.id().contains("captain")) {
            cube(head, "helmet", 32, 64, -4.5f, -11, -4.5f, 9, 4, 9, 0, 0, 0);
            cube(root.getChild("body"), "cuirass", 0, 96, -4.5f, .5f, -2.8f, 9, 8, 5.6f, 0, 0, 0);
        }
        if (p.boss()) {
            for (int i = 0; i < 3; i++) cube(head, "crown_" + i, 96, 32, -1, -4, -1, 2, 4, 2, -3 + i * 3, -10, -3.5f);
            cube(head, "crown_band", 88, 32, -4.5f, -1.5f, -4.5f, 9, 2, 9, 0, -9, 0);
        }
        if (p.id().contains("pillager") || p.id().equals("illusioner")) {
            cube(right, "crossbow", 96, 32, -5, -1, -4, 10, 2, 4, -1, 8, -2);
        }
    }
    private static void wraith(PartDefinition root, MobProfile p) {
        cube(root, "head", 0, 0, -4, -9, -4, 8, 9, 8, 0, 0, 0);
        PartDefinition body = cube(root, "body", 0, 32, -6, 0, -3, 12, 13, 6, 0, 0, 0);
        cube(body, "lower_cloak", 0, 64, -8, 0, -4, 16, 6, 8, 0, 12, 0);
        cube(root, "right_arm", 64, 0, -4, 0, -2, 4, 13, 4, -5, 2, 0);
        cube(root, "left_arm", 64, 0, 0, 0, -2, 4, 13, 4, 5, 2, 0);
        PartDefinition tail = root.getChild("tail");
        for (int i = 0; i < 5; i++) cube(tail, "tatter_" + i, 64, 32, -.8f, 0, -1.5f, 1.6f, 5 + i % 2 * 2, 3, -6 + i * 3, 17, 0);
        if (p.boss()) {
            cube(root.getChild("head"), "ice_crown", 88, 32, -5, -3, -4, 10, 3, 8, 0, -9, 0);
            for (int i = 0; i < 3; i++) cube(root.getChild("head"), "ice_" + i, 96, 32, -1, -5, -1, 2, 5, 2, -4 + 4 * i, -11, 0);
        }
    }
    private static void golem(PartDefinition root, MobProfile p) {
        PartDefinition body = cube(root, "body", 0, 32, -10, -10, -6, 20, 21, 12, 0, -4, 0);
        PartDefinition head = cube(root, "head", 0, 0, -6, -8, -5, 12, 8, 10, 0, -14, -1);
        cube(root, "right_arm", 64, 0, -9, -3, -5, 9, 26, 10, -10, -9, 0);
        cube(root, "left_arm", 64, 0, 0, -3, -5, 9, 26, 10, 10, -9, 0);
        cube(root, "right_leg", 64, 64, -4, 0, -4, 8, 17, 8, -6, 7, 0);
        cube(root, "left_leg", 64, 64, -4, 0, -4, 8, 17, 8, 6, 7, 0);
        cube(body, "core", 96, 32, -3, -3, -1, 6, 6, 2, 0, 0, -6);
        if (p.id().equals("mooshroom_monstrosity")) {
            cube(head, "left_horn", 96, 32, -1, -5, -1, 2, 6, 2, 7, -6, -2);
            cube(head, "right_horn", 96, 32, -1, -5, -1, 2, 6, 2, -7, -6, -2);
            PartDefinition ornaments = root.getChild("ornaments");
            for (int i = 0; i < 3; i++) {
                cube(ornaments, "stem" + i, 64, 64, -1, -5, -1, 2, 5, 2, -7 + 7 * i, -15, 3);
                cube(ornaments, "cap" + i, 80, 96, -4, -2, -3, 8, 3, 6, -7 + 7 * i, -20, 3);
            }
        } else if (p.id().equals("tempest_golem")) {
            cube(head, "temple_crown", 64, 32, -8, -3, -6, 16, 4, 12, 0, -8, 0);
            cube(root.getChild("right_arm"), "blade", 96, 0, -2, 0, -1, 4, 20, 2, -5, 21, -2);
        } else {
            cube(body, "left_plate", 0, 96, -5, -2, -5, 10, 4, 10, 11, -6, 0);
            cube(body, "right_plate", 0, 96, -5, -2, -5, 10, 4, 10, -11, -6, 0);
        }
    }
    private static void plant(PartDefinition root, MobProfile p) {
        boolean leap = p.id().equals("leapleaf");
        PartDefinition body = cube(root, "body", 0, 32, -6, -6, -4, 12, 17, 8, 0, 1, 0);
        PartDefinition head = cube(root, "head", 0, 0, -5, -9, -5, 10, 9, 10, 0, -5, -1);
        cube(root, "right_arm", 64, 0, -4, -2, -3, 4, 21, 6, -6, -2, 0);
        cube(root, "left_arm", 64, 0, 0, -2, -3, 4, 21, 6, 6, -2, 0);
        cube(root, "right_leg", 64, 64, -3, 0, -3, 6, 13, 6, -4, 11, 0);
        cube(root, "left_leg", 64, 64, -3, 0, -3, 6, 13, 6, 4, 11, 0);
        for (int i = 0; i < 4; i++) {
            PartDefinition leaf = cube(head, "leaf_" + i, 80, 96, -5, -2, -2, 10, 3, 4, 0, -7, 0);
            leaf = head.addOrReplaceChild("leaf_" + i, CubeListBuilder.create().texOffs(80, 96).addBox(2, -1, -2, 9, 2, 4),
                    PartPose.offsetAndRotation(0, -7, 0, -.15f, i * Mth.HALF_PI, leap ? -.4f : .2f));
        }
        if (p.id().equals("jungle_abomination")) {
            cube(body, "broken_wall", 0, 96, -4, -2, 0, 8, 18, 4, 2, -7, 4);
            cube(head, "jaw", 96, 32, -6, -1, -2, 12, 4, 4, 0, -1, -4);
        } else {
            cube(head, "flower_center", 96, 32, -3, -3, -1, 6, 6, 2, 0, -4, -5);
        }
    }
    private static void enderling(PartDefinition root, MobProfile p) {
        boolean heart = p.id().contains("heart");
        cube(root, "body", 0, 32, -5, -6, -3, 10, 13, 6, 0, -3, 0);
        PartDefinition head = cube(root, "head", 0, 0, -5, -8, -4, 10, 8, 8, 0, -9, -1);
        cube(root, "right_arm", 64, 0, -3, -1, -2, 3, 24, 4, -5, -7, 0);
        cube(root, "left_arm", 64, 0, 0, -1, -2, 3, 24, 4, 5, -7, 0);
        cube(root, "right_leg", 64, 64, -1.5f, 0, -2, 3, 20, 4, -3, 4, 0);
        cube(root, "left_leg", 64, 64, -1.5f, 0, -2, 3, 20, 4, 3, 4, 0);
        if (heart) {
            PartDefinition ornaments = root.getChild("ornaments");
            for (int i = 0; i < 4; i++) cube(ornaments, "tendril" + i, 64, 0, -1.3f, 0, -1.3f, 2.6f, 24, 2.6f, -9 + i * 6, -3, 3);
            cube(head, "jaw", 88, 32, -5.5f, -1, -5, 11, 4, 9, 0, 0, 0);
            cube(root.getChild("body"), "ender_core", 96, 32, -3, -3, -1, 6, 6, 2, 0, 0, -3);
            if (p.id().startsWith("vengeful")) cube(head, "crown", 80, 96, -7, -4, -5, 14, 4, 10, 0, -8, 0);
        } else if (p.id().equals("blastling")) {
            cube(root.getChild("right_arm"), "cannon", 96, 32, -3, -3, -7, 6, 6, 9, -1, 16, -1);
        } else if (p.id().equals("snareling")) {
            cube(head, "mandible_l", 96, 32, -1, 0, -1, 2, 6, 2, 4, -1, -4);
            cube(head, "mandible_r", 96, 32, -1, 0, -1, 2, 6, 2, -4, -1, -4);
        }
    }
    private static void cauldron(PartDefinition root) {
        PartDefinition body = cube(root, "body", 0, 32, -12, 0, -12, 24, 4, 24, 0, 16, 0);
        cube(body, "front", 0, 64, -12, -15, -12, 24, 15, 3, 0, 0, 0);
        cube(body, "back", 0, 64, -12, -15, 9, 24, 15, 3, 0, 0, 0);
        cube(body, "left", 0, 64, -12, -15, -9, 3, 15, 18, 0, 0, 0);
        cube(body, "right", 0, 64, 9, -15, -9, 3, 15, 18, 0, 0, 0);
        cube(root, "head", 48, 96, -9, -1, -9, 18, 2, 18, 0, 6, 0);
        cube(root, "right_arm", 64, 0, -3, 0, -2, 3, 10, 4, -12, 5, 0);
        cube(root, "left_arm", 64, 0, 0, 0, -2, 3, 10, 4, 12, 5, 0);
        cube(root, "right_leg", 64, 64, -3, 0, -8, 6, 4, 16, -8, 20, 0);
        cube(root, "left_leg", 64, 64, -3, 0, -8, 6, 4, 16, 8, 20, 0);
    }
    private static void guardian(PartDefinition root) {
        PartDefinition body = cube(root, "body", 0, 32, -10, -9, -9, 20, 18, 18, 0, 12, 0);
        cube(root, "head", 96, 32, -4, -4, -1, 8, 8, 2, 0, 11, -9);
        PartDefinition tail = cube(root, "tail", 64, 0, -3, -3, 0, 6, 6, 14, 0, 12, 8);
        cube(tail, "fin", 80, 96, -.5f, -7, 0, 1, 14, 7, 0, 0, 12);
        PartDefinition spikes = root.getChild("ornaments");
        for (int i = 0; i < 4; i++) {
            cube(spikes, "upper_spike" + i, 96, 0, -1, -7, -1, 2, 7, 2, -7 + i * 4.5f, 3, 0);
            cube(spikes, "lower_spike" + i, 96, 0, -1, 0, -1, 2, 3, 2, -7 + i * 4.5f, 21, 0);
        }
        cube(root, "right_arm", 96, 0, -7, -1, -1, 7, 2, 2, -10, 12, 0);
        cube(root, "left_arm", 96, 0, 0, -1, -1, 7, 2, 2, 10, 12, 0);
    }
    private static void wildfire(PartDefinition root) {
        cube(root, "head", 0, 0, -4, -8, -4, 8, 8, 8, 0, 0, 0);
        cube(root, "body", 0, 32, -4, 0, -4, 8, 12, 8, 0, 0, 0);
        PartDefinition ornaments = root.getChild("ornaments");
        for (int i = 0; i < 4; i++) {
            ornaments.addOrReplaceChild("shield" + i, CubeListBuilder.create().texOffs(80, 64).addBox(-4, -6, -1, 8, 12, 2),
                    PartPose.offsetAndRotation((float)Math.cos(i * Math.PI / 2) * 8, 7, (float)Math.sin(i * Math.PI / 2) * 8, 0, -i * Mth.HALF_PI, 0));
        }
        PartDefinition tail = root.getChild("tail");
        for (int i = 0; i < 4; i++) cube(tail, "flame" + i, 96, 32, -1, 0, -1, 2, 9, 2, -4 + i * 2.7f, 13, 0);
        cube(root.getChild("head"), "crown", 88, 32, -5, -2, -5, 10, 3, 10, 0, -8, 0);
    }
    private static void horseman(PartDefinition root) {
        PartDefinition body = cube(root, "body", 0, 32, -4, -4, -9, 8, 9, 18, 0, 10, 0);
        cube(body, "horse_neck", 64, 0, -2.5f, -12, -3, 5, 12, 6, 0, 0, -7);
        cube(body, "horse_head", 0, 0, -3, -6, -4, 6, 7, 9, 0, -12, -9);
        cube(root, "right_leg", 64, 64, -1.5f, 0, -1.5f, 3, 11, 3, -3, 13, -6);
        cube(root, "left_leg", 64, 64, -1.5f, 0, -1.5f, 3, 11, 3, 3, 13, -6);
        cube(root, "right_arm", 64, 64, -1.5f, 0, -1.5f, 3, 11, 3, -3, 13, 6);
        cube(root, "left_arm", 64, 64, -1.5f, 0, -1.5f, 3, 11, 3, 3, 13, 6);
        PartDefinition rider = cube(root, "head", 0, 0, -3, -7, -3, 6, 7, 6, 0, -2, 1);
        cube(rider, "rider_torso", 0, 64, -3, 0, -1.5f, 6, 8, 3, 0, 0, 0);
        cube(rider, "rider_arm", 64, 0, -2, 0, -1, 2, 9, 2, -3, 0, 0);
        cube(rider, "rider_bow", 96, 32, -.8f, -6, -.8f, 1.6f, 13, 1.6f, -5, 5, -3);
        cube(root, "tail", 64, 0, -1, 0, -1, 2, 10, 2, 0, 10, 9);
    }
    private static void slime(PartDefinition root, MobProfile p) {
        cube(root, "body", 0, 32, -7, -7, -7, 14, 14, 14, 0, 17, 0);
        cube(root, "head", 96, 32, -5, -2, -.5f, 10, 4, 1, 0, 15, -7);
        if (p.id().equals("redstone_cube")) cube(root.getChild("body"), "core", 96, 32, -3, -3, -.5f, 6, 6, 1, 0, 0, -7);
    }
    public static float nativeHeight(MobProfile p) {
        return switch (p.family()) {
            case GOLEM -> p.id().equals("mooshroom_monstrosity") ? 50f / 16 : 48f / 16;
            case PLANT -> 40f / 16;
            case ENDERLING -> 45f / 16;
            case CAULDRON -> 25f / 16;
            case GUARDIAN -> 30f / 16;
            case WILDFIRE -> 35f / 16;
            case HORSEMAN -> 36f / 16;
            case SLIME -> 14f / 16;
            default -> 36f / 16;
        };
    }
    public static float nativeWidth(MobProfile p) {
        return switch (p.family()) {
            case GOLEM -> 38f / 16;
            case PLANT -> 22f / 16;
            case ENDERLING -> p.id().contains("heart") ? 22f / 16 : 16f / 16;
            case CAULDRON -> 30f / 16;
            case GUARDIAN -> 34f / 16;
            case WILDFIRE -> 24f / 16;
            case HORSEMAN -> 12f / 16;
            case SLIME -> 14f / 16;
            default -> 18f / 16;
        };
    }
    @Override public void setupAnim(DungeonMob mob, float limbSwing, float limbAmount, float age, float yaw, float pitch) {
        root.getAllParts().forEach(ModelPart::resetPose);
        MobProfile p = mob.profile();
        float time = age + mob.getId() * 1.73f;
        float breath = Mth.sin(time * .075f);
        head.yRot += yaw * Mth.DEG_TO_RAD * .65f;
        head.xRot += pitch * Mth.DEG_TO_RAD * .55f;
        body.xRot += breath * .015f;
        rightArm.zRot += .04f + breath * .025f;
        leftArm.zRot -= .04f + breath * .025f;
        float walk = limbSwing * (p.family() == MobProfile.Family.GOLEM ? .44f : .65f);
        float amount = Math.min(limbAmount, .8f);
        rightLeg.xRot += Mth.cos(walk) * amount;
        leftLeg.xRot += Mth.cos(walk + Mth.PI) * amount;
        rightArm.xRot += Mth.cos(walk + Mth.PI) * amount * .6f;
        leftArm.xRot += Mth.cos(walk) * amount * .6f;
        root.y += Math.abs(Mth.sin(walk)) * amount * -.6f;
        body.zRot += Mth.sin(walk) * amount * .025f;
        if (p.hovering()) { root.y += Mth.sin(time * .09f) * .8f - .6f; tail.zRot = Mth.sin(time * .1f) * .08f; }
        if (p.family() == MobProfile.Family.GUARDIAN) { tail.yRot = Mth.sin(time * .18f) * .25f; ornaments.y += breath * .4f; }
        if (p.family() == MobProfile.Family.WILDFIRE) { ornaments.yRot = time * .025f; tail.yRot = -time * .04f; }
        if (p.family() == MobProfile.Family.PLANT) { head.zRot += breath * .025f; rightArm.zRot += .1f; leftArm.zRot -= .1f; }
        if (p.family() == MobProfile.Family.CAULDRON) { head.y += breath * .5f; rightLeg.xRot = leftLeg.xRot = 0; }
        if (p.family() == MobProfile.Family.SLIME) { root.yScale = 1 + Mth.sin(time * .2f) * .035f; }
        if (mob.hurtTime > 0) body.zRot += Mth.sin(mob.hurtTime * .8f) * .08f;
        AttackKind attack = mob.activeAttack();
        if (attack == null) return;
        float elapsed = mob.animationTick(age - mob.tickCount);
        float anticipation = smooth(Mth.clamp(elapsed / attack.windup(), 0, 1));
        float recover = elapsed <= attack.windup() ? 0 : smooth(Mth.clamp((elapsed - attack.windup()) / attack.recovery(), 0, 1));
        float hold = anticipation * (1 - recover);
        switch (attack) {
            case MELEE -> {
                rightArm.xRot = elapsed < attack.windup() ? -1.9f * anticipation : .7f * (1 - recover);
                body.yRot = -.3f * hold;
            }
            case HEAVY_SLAM -> {
                rightArm.xRot = leftArm.xRot = elapsed < attack.windup() ? -2.6f * anticipation : .9f * (1 - recover);
                body.xRot = elapsed < attack.windup() ? -.15f * anticipation : .25f * (1 - recover);
                root.y += elapsed < attack.windup() ? -1.2f * anticipation : 1.3f * (1 - recover);
            }
            case GUARD -> { leftArm.xRot = -1.3f * hold; leftArm.yRot = .6f * hold; body.xRot += .08f * hold; }
            case LEAP -> { body.xRot += .3f * hold; rightLeg.xRot = leftLeg.xRot = -.8f * hold; rightArm.xRot = leftArm.xRot = -1f * hold; }
            case TELEPORT -> { rightArm.zRot += .7f * hold; leftArm.zRot -= .7f * hold; root.y -= 2 * hold; }
            case BEAM -> { head.xRot -= .18f * hold; rightArm.xRot = leftArm.xRot = -1.45f * hold; body.xRot -= .08f * hold; }
            case SUMMON, STORM, ENCHANT -> {
                rightArm.xRot = leftArm.xRot = -2f * hold;
                rightArm.zRot = -.35f * hold; leftArm.zRot = .35f * hold;
                head.xRot -= .2f * hold;
            }
            default -> { rightArm.xRot = -1.65f * hold; leftArm.xRot = -1.25f * hold; body.yRot -= .12f * hold; }
        }
    }
    private static float smooth(float t) { return t * t * (3 - 2 * t); }
}
