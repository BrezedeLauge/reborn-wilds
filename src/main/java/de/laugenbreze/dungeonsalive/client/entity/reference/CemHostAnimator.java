package de.laugenbreze.dungeonsalive.client.entity.reference;

import de.laugenbreze.dungeonsalive.entity.AttackKind;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.animation.KeyframeAnimations;
import net.minecraft.client.animation.definitions.WardenAnimation;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.AnimationUtils;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import org.joml.Vector3f;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Supplies the vanilla host-part movements consumed by authored CEM equations.
 * Warden keyframes come from Minecraft itself. Their playback clock is adapted to our actions;
 * The ordinary animal/illager gait equations follow the installed Minecraft models. Entity-only
 * animation counters (flapping, tentacle pulse, rabbit hopping) use deterministic clocks derived
 * from actual movement/water/ground state because our passive entities are independent Mobs.
 */
final class CemHostAnimator {
    private static final float[] SPIKE_X = {0, 0, 8, -8, -8, 8, 8, -8, 0, 0, 8, -8};
    private static final float[] SPIKE_Y = {-8, -8, -8, -8, 0, 0, 0, 0, 8, 8, 8, 8};
    private static final float[] SPIKE_Z = {8, -8, 0, 0, -8, -8, 8, 8, 8, -8, 0, 0};
    private final String id, host;
    private final Map<String, ModelPart> parts;
    private final HostRig rig;
    private final Vector3f vector = new Vector3f();

    CemHostAnimator(String id, String host, ModelPart root, Map<String, ModelPart> parts) {
        this.id = id; this.host = host; this.parts = Map.copyOf(parts);
        List<String> required = switch (host) {
            case "warden" -> List.of("torso", "head", "left_arm", "right_arm", "left_leg", "right_leg", "left_ribcage", "right_ribcage", "left_tendril", "right_tendril");
            case "guardian" -> List.of("body", "eye", "tail1", "tail2", "tail3", "spine1", "spine2", "spine3", "spine4", "spine5", "spine6", "spine7", "spine8", "spine9", "spine10", "spine11", "spine12");
            case "wither" -> List.of("body1", "body2", "body3", "head1", "head2", "head3");
            case "slime" -> List.of("body");
            case "chicken" -> List.of("head", "bill", "chin", "body", "left_leg", "right_leg", "left_wing", "right_wing");
            case "cow", "pig", "sheep", "goat", "turtle" -> List.of("head", "body", "leg1", "leg2", "leg3", "leg4");
            case "cat" -> List.of("head", "body", "leg1", "leg2", "leg3", "leg4", "tail", "tail2");
            case "fox" -> List.of("head", "body", "tail", "leg1", "leg2", "leg3", "leg4");
            case "rabbit" -> List.of("head", "nose", "left_ear", "right_ear", "left_foot", "right_foot", "left_thigh", "right_thigh", "left_arm", "right_arm");
            case "wandering_trader", "villager" -> List.of("head", "body", "arms", "left_leg", "right_leg");
            case "vindicator", "evoker", "illusioner", "pillager" -> List.of("head", "body", "arms", "left_arm", "right_arm", "left_leg", "right_leg");
            case "armored_pillager", "armored_vindicator" -> List.of("head", "body", "left_arm", "right_arm", "left_leg", "right_leg");
            case "skeleton", "stray", "zombie", "husk" -> List.of("head", "headwear", "body", "left_arm", "right_arm", "left_leg", "right_leg");
            case "phantom" -> List.of("body", "head", "left_wing", "left_wing_tip", "right_wing", "right_wing_tip", "tail", "tail2");
            case "allay" -> List.of("root", "body", "head", "left_arm", "right_arm", "left_wing", "right_wing");
            case "glow_squid", "squid" -> List.of("body", "tentacle1", "tentacle2", "tentacle3", "tentacle4", "tentacle5", "tentacle6", "tentacle7", "tentacle8");
            case "parrot" -> List.of("head", "body", "tail", "left_wing", "right_wing", "left_leg", "right_leg");
            case "silverfish" -> List.of("body1", "body2", "body3", "body4", "body5", "body6", "body7", "wing1", "wing2", "wing3");
            case "ravager" -> List.of("head", "body", "neck", "jaw", "leg1", "leg2", "leg3", "leg4");
            default -> throw new IllegalArgumentException(id + ": unsupported CEM host " + host);
        };
        for (String name : required) part(name);
        rig = new HostRig(root);
    }

    private ModelPart part(String name) {
        ModelPart part = parts.get(name);
        if (part == null) throw new IllegalArgumentException(id + ": missing CEM host alias '" + name + "'");
        return part;
    }

    void animate(CemFrame frame) {
        switch (host) {
            case "warden" -> warden(frame);
            case "guardian" -> guardian(frame);
            case "wither" -> wither(frame);
            // A stationary furnace should not inherit a slime renderer's whole-body squash.
            case "slime" -> { }
            case "chicken" -> chicken(frame);
            case "cow", "pig", "sheep", "goat" -> quadruped(frame, 1.4F);
            case "cat" -> cat(frame);
            case "turtle" -> turtle(frame);
            case "fox" -> fox(frame);
            case "rabbit" -> rabbit(frame);
            case "wandering_trader", "villager" -> villager(frame);
            case "vindicator", "evoker", "illusioner", "pillager", "armored_pillager", "armored_vindicator" -> illager(frame);
            case "skeleton", "stray", "zombie", "husk" -> humanoid(frame);
            case "phantom" -> phantom(frame);
            case "allay" -> allay(frame);
            case "glow_squid", "squid" -> squid(frame);
            case "parrot" -> parrot(frame);
            case "silverfish" -> silverfish(frame);
            case "ravager" -> ravager(frame);
            default -> throw new AssertionError(host);
        }
    }

    private void warden(CemFrame frame) {
        ModelPart head = part("head"), body = part("torso");
        head.yRot = frame.headYaw() * Mth.DEG_TO_RAD;
        head.xRot = frame.headPitch() * Mth.DEG_TO_RAD;
        float amount = Math.min(.5F, 3 * frame.limbSpeed());
        float phase = frame.limbSwing() * .8662F, cosine = Mth.cos(phase), sine = Mth.sin(phase);
        float bodyAmount = Math.min(.35F, amount);
        head.zRot += .3F * sine * amount;
        head.xRot += 1.2F * Mth.cos(phase + Mth.HALF_PI) * bodyAmount;
        body.zRot = .1F * sine * amount;
        body.xRot = cosine * bodyAmount;
        part("left_leg").xRot = cosine * amount;
        part("right_leg").xRot = Mth.cos(phase + Mth.PI) * amount;
        part("left_arm").xRot = -.8F * cosine * amount;
        part("right_arm").xRot = -.8F * sine * amount;
        part("left_arm").zRot = part("right_arm").zRot = 0;
        float idle = frame.age() * .1F;
        head.zRot += .06F * Mth.cos(idle);
        head.xRot += .06F * Mth.sin(idle);
        body.zRot += .025F * Mth.sin(idle);
        body.xRot += .025F * Mth.cos(idle);
        float tendril = Mth.cos(frame.age() * 2.25F) * Mth.PI * .05F;
        if (parts.containsKey("left_tendril")) part("left_tendril").xRot = tendril;
        if (parts.containsKey("right_tendril")) part("right_tendril").xRot = -tendril;

        AttackKind action = frame.action();
        if (action == null || frame.deathTicks() > 0) return;
        AnimationDefinition animation;
        float impact;
        switch (action) {
            case MELEE, HEAVY_SLAM, LEAP -> { animation = WardenAnimation.WARDEN_ATTACK; impact = .1667F; }
            case BEAM, VOLLEY, SOUL_BOLT -> { animation = WardenAnimation.WARDEN_SONIC_BOOM; impact = 1.7F; }
            case SUMMON, STORM, ROOT_SNARE, FIRE_FIELD -> { animation = WardenAnimation.WARDEN_ROAR; impact = 2; }
            case GUARD -> {
                float weight = ActionClipTiming.weight(frame.actionTicks(), action.windup(), action.recovery(), 3, 4);
                part("right_arm").xRot -= .8F * weight;
                part("left_arm").xRot -= .8F * weight;
                return;
            }
            default -> { return; }
        }
        float seconds = ActionClipTiming.seconds(frame.actionTicks(), action.windup(), action.recovery(), impact, animation.lengthInSeconds());
        float weight = ActionClipTiming.weight(frame.actionTicks(), action.windup(), action.recovery(), 3, 4);
        KeyframeAnimations.animate(rig, animation, Math.round(seconds * 1000), weight, vector);
    }

    private void guardian(CemFrame frame) {
        part("body").xRot = frame.headPitch() * Mth.DEG_TO_RAD;
        part("body").yRot = frame.headYaw() * Mth.DEG_TO_RAD;
        float retract = Mth.clamp(frame.limbSpeed(), 0, 1) * .55F;
        if (frame.action() == AttackKind.BEAM) retract = 0;
        for (int i = 0; i < 12; i++) {
            float distance = 1 + Mth.cos(frame.age() * 1.5F + i) * .01F - retract;
            ModelPart spike = part("spine" + (i + 1));
            spike.x = SPIKE_X[i] * distance;
            spike.y = 16 + SPIKE_Y[i] * distance;
            spike.z = SPIKE_Z[i] * distance;
        }
        part("eye").x = Mth.clamp(frame.headYaw() / 30, -2, 2);
        part("eye").y = frame.headPitch() < 0 ? 0 : 1;
        float tail = frame.age() * (frame.inWater() ? .12F : .35F) + frame.limbSwing() * .35F;
        part("tail1").yRot = Mth.sin(tail) * Mth.PI * .05F;
        part("tail2").yRot = Mth.sin(tail) * Mth.PI * .1F;
        part("tail3").yRot = Mth.sin(tail) * Mth.PI * .15F;
    }

    private void wither(CemFrame frame) {
        float idle = Mth.cos(frame.age() * .1F);
        ModelPart ribs = part("body2"), tail = part("body3");
        ribs.xRot = (.065F + .05F * idle) * Mth.PI;
        tail.setPos(-2, 6.9F + Mth.cos(ribs.xRot) * 10, -.5F + Mth.sin(ribs.xRot) * 10);
        tail.xRot = (.265F + .1F * idle) * Mth.PI;
        part("head1").yRot = frame.headYaw() * Mth.DEG_TO_RAD;
        part("head1").xRot = frame.headPitch() * Mth.DEG_TO_RAD;
        part("head2").yRot = frame.headYaw() * Mth.DEG_TO_RAD;
        part("head3").yRot = frame.headYaw() * Mth.DEG_TO_RAD;
        part("head2").xRot = part("head3").xRot = frame.headPitch() * Mth.DEG_TO_RAD;
    }

    private void look(CemFrame frame) {
        part("head").yRot = frame.headYaw() * Mth.DEG_TO_RAD;
        part("head").xRot = frame.headPitch() * Mth.DEG_TO_RAD;
    }

    private float gait(CemFrame frame, float amplitude) {
        return Mth.cos(frame.limbSwing() * .6662F) * amplitude * frame.limbSpeed();
    }

    private void quadruped(CemFrame frame, float amplitude) {
        look(frame);
        float swing = gait(frame, amplitude);
        part("leg1").xRot = part("leg4").xRot = swing;
        part("leg2").xRot = part("leg3").xRot = -swing;
    }

    private void chicken(CemFrame frame) {
        look(frame);
        for (String face : List.of("bill", "chin")) {
            part(face).xRot = part("head").xRot;
            part(face).yRot = part("head").yRot;
        }
        part("right_leg").xRot = gait(frame, 1.4F);
        part("left_leg").xRot = -part("right_leg").xRot;
        // Vanilla ChickenRenderer supplies a flap counter, not raw age. Reconstruct a bounded flap
        // only while airborne; assigning age directly makes grounded key golems spin their arms.
        float flap = frame.onGround() || frame.sitting() ? 0 : (1 + Mth.sin(frame.age() * 1.8F)) * .75F;
        part("right_wing").zRot = flap;
        part("left_wing").zRot = -flap;
    }

    private void villager(CemFrame frame) {
        look(frame);
        part("head").zRot = 0;
        part("right_leg").xRot = gait(frame, .7F);
        part("left_leg").xRot = -part("right_leg").xRot;
        part("right_leg").yRot = part("left_leg").yRot = 0;
        if (frame.riding()) {
            part("right_leg").xRot = part("left_leg").xRot = -1.35F;
            part("right_leg").yRot = .18F; part("left_leg").yRot = -.18F;
        }
        // Trading/greeting gestures are bound authored clips. Do not invent an unhappy counter.
    }

    private void phantom(CemFrame frame) {
        if (id.equals("vulture") && frame.sitting()) {
            // Own grounded resting pose; the source mesh/UVs and wing expressions
            // are retained. Folded host joints also drive its authored wing copies.
            part("left_wing").zRot = .9F; part("left_wing_tip").zRot = -1.35F;
            part("right_wing").zRot = -.9F; part("right_wing_tip").zRot = 1.35F;
            part("tail").xRot = part("tail2").xRot = -.1F;
            return;
        }
        float phase = frame.age() * 7.448451F * Mth.DEG_TO_RAD;
        float flap = Mth.cos(phase) * 16 * Mth.DEG_TO_RAD;
        part("left_wing").zRot = part("left_wing_tip").zRot = flap;
        part("right_wing").zRot = part("right_wing_tip").zRot = -flap;
        part("tail").xRot = part("tail2").xRot = -(5 + Mth.cos(phase * 2) * 5) * Mth.DEG_TO_RAD;
    }

    private void allay(CemFrame frame) {
        look(frame);
        float moving = Mth.clamp(frame.limbSpeed() / .3F, 0, 1), idle = 1 - moving;
        float wing = Mth.cos(frame.age() * 20 * Mth.DEG_TO_RAD + frame.limbSwing()) * Mth.PI * .15F + frame.limbSpeed();
        part("right_wing").xRot = part("left_wing").xRot = .43633232F * idle;
        part("right_wing").yRot = -Mth.PI / 4 + wing;
        part("left_wing").yRot = Mth.PI / 4 - wing;
        part("body").xRot = moving * Mth.PI / 4;
        float phase = frame.age() * 9 * Mth.DEG_TO_RAD;
        part("root").y += Mth.cos(phase) * .25F * idle;
        float arm = .43633232F - Mth.cos(phase + Mth.PI * 1.5F) * Mth.PI * .075F * idle;
        part("left_arm").zRot = -arm;
        part("right_arm").zRot = arm;
    }

    private void squid(CemFrame frame) {
        // Same two-stage stroke shape as Squid.aiStep; a local visual clock replaces its private
        // random tentacle timer. No world movement or water state is manufactured by this driver.
        float phase = Mth.positiveModulo(frame.age() * .15F + frame.limbSwing() * .1F, Mth.TWO_PI);
        float angle;
        if (frame.inWater()) {
            float fraction = phase / Mth.PI;
            angle = phase < Mth.PI ? Mth.sin(fraction * fraction * Mth.PI) * Mth.PI * .25F : 0;
        } else angle = Math.abs(Mth.sin(phase)) * Mth.PI * .25F;
        for (int i = 1; i <= 8; i++) part("tentacle" + i).xRot = angle;
    }

    private void parrot(CemFrame frame) {
        look(frame);
        ModelPart body = part("body"), tail = part("tail"), leftWing = part("left_wing"), rightWing = part("right_wing");
        ModelPart leftLeg = part("left_leg"), rightLeg = part("right_leg");
        body.xRot = .4937F;
        leftWing.xRot = rightWing.xRot = -.6981F;
        leftWing.yRot = rightWing.yRot = -Mth.PI;
        leftLeg.xRot = rightLeg.xRot = -.0299F;
        if (frame.sitting()) {
            part("head").y = 17.59F; body.y = 18.4F;
            tail.xRot = 1.5388988F; tail.y = 22.97F;
            leftWing.zRot = -.0873F; rightWing.zRot = .0873F;
            leftWing.y = rightWing.y = 18.84F;
            leftLeg.y = rightLeg.y = 23;
            leftLeg.xRot++; rightLeg.xRot++;
            return;
        }
        float flap = frame.onGround() ? 0 : (1 + Mth.sin(frame.age() * 1.6F)) * .5F;
        float bob = flap * .3F;
        if (frame.onGround()) {
            leftLeg.xRot += gait(frame, 1.4F);
            rightLeg.xRot -= gait(frame, 1.4F);
        } else leftLeg.xRot = rightLeg.xRot = -.0299F + Mth.TWO_PI / 9;
        part("head").y = 15.69F + bob; body.y = 16.5F + bob;
        tail.y = 21.07F + bob; tail.xRot = 1.015F + gait(frame, .3F);
        leftWing.y = rightWing.y = 16.94F + bob;
        leftWing.zRot = -.0873F - flap; rightWing.zRot = .0873F + flap;
        leftLeg.y = rightLeg.y = 22 + bob;
    }

    private void rabbit(CemFrame frame) {
        look(frame);
        for (String name : List.of("nose", "right_ear", "left_ear")) {
            part(name).xRot = part("head").xRot;
            part(name).yRot = part("head").yRot;
        }
        part("right_ear").yRot -= Mth.PI / 12;
        part("left_ear").yRot += Mth.PI / 12;
        float hop = frame.sitting() ? 0 : Math.max(0, Mth.sin(frame.limbSwing() * .6662F)) * Mth.clamp(frame.limbSpeed() * 2, 0, 1);
        if (!frame.onGround()) hop = Math.max(hop, .75F);
        part("left_thigh").xRot = part("right_thigh").xRot = (hop * 50 - 21) * Mth.DEG_TO_RAD;
        part("left_foot").xRot = part("right_foot").xRot = hop * 50 * Mth.DEG_TO_RAD;
        part("left_arm").xRot = part("right_arm").xRot = (-hop * 40 - 11) * Mth.DEG_TO_RAD;
    }

    private void cat(CemFrame frame) {
        look(frame);
        ModelPart body = part("body"), head = part("head"), tail = part("tail"), tip = part("tail2");
        body.setPos(0, 12, -10); body.xRot = Mth.HALF_PI;
        head.y = 15; head.z = -9;
        tail.y = 15; tail.z = 8; tail.xRot = .9F;
        tip.y = 20; tip.z = 14;
        part("leg1").y = part("leg2").y = 18; part("leg1").z = part("leg2").z = 5;
        part("leg3").y = part("leg4").y = 14.1F; part("leg3").z = part("leg4").z = -5;
        float swing = gait(frame, 1);
        part("leg1").xRot = part("leg4").xRot = -swing;
        part("leg2").xRot = part("leg3").xRot = swing;
        tip.xRot = 1.7278761F + Mth.PI / 4 * Mth.cos(frame.limbSwing()) * frame.limbSpeed();
        if (!frame.sitting()) return;
        // CatModel.prepareMobModel sitting pose, on the same OcelotModel native hierarchy.
        body.xRot = Mth.PI / 4; body.y -= 4; body.z += 5;
        head.y -= 3.3F; head.z++;
        tail.y += 8; tail.z -= 2; tail.xRot = 1.7278761F;
        tip.y += 2; tip.z -= .8F; tip.xRot = 2.670354F;
        part("leg1").xRot = part("leg2").xRot = -Mth.HALF_PI;
        part("leg1").y = part("leg2").y = 21; part("leg1").z = part("leg2").z = 1;
        part("leg3").xRot = part("leg4").xRot = -Mth.PI / 20;
        part("leg3").y = part("leg4").y = 16.1F; part("leg3").z = part("leg4").z = -7;
    }

    private void turtle(CemFrame frame) {
        look(frame);
        float wave = Mth.cos(frame.limbSwing() * .6662F * .6F) * .5F * frame.limbSpeed();
        part("leg1").xRot = wave; part("leg2").xRot = -wave;
        part("leg3").zRot = -wave; part("leg4").zRot = wave;
        part("leg3").xRot = part("leg4").xRot = 0;
        for (int i = 1; i <= 4; i++) part("leg" + i).yRot = 0;
        if (!frame.inWater() && frame.onGround()) {
            float crawl = Mth.cos(frame.limbSwing() * 5) * frame.limbSpeed();
            part("leg3").yRot = -crawl * 8; part("leg4").yRot = crawl * 8;
            part("leg3").zRot = part("leg4").zRot = 0;
            part("leg1").yRot = -crawl * 3; part("leg2").yRot = crawl * 3;
            part("leg1").xRot = part("leg2").xRot = 0;
        }
        // The imported native profile is a baby companion, not an egg-bearing Turtle entity.
        if (parts.containsKey("egg_belly")) part("egg_belly").visible = false;
    }

    private void fox(CemFrame frame) {
        quadruped(frame, 1.4F);
        part("body").xRot = Mth.HALF_PI;
        part("tail").xRot = -.05235988F;
        if (!frame.sitting()) return;
        part("body").xRot = Mth.PI / 6;
        part("body").setPos(0, 9, -3);
        part("tail").xRot = Mth.PI / 4;
        part("tail").setPos(-4, 15, -2);
        part("head").setPos(-1, frame.baby() ? 13 : 10, frame.baby() ? -3.75F : -.25F);
        part("leg1").xRot = part("leg2").xRot = -Mth.PI * 5 / 12;
        part("leg1").setPos(-5, 21.5F, 6.75F); part("leg2").setPos(-1, 21.5F, 6.75F);
        part("leg3").xRot = part("leg4").xRot = -Mth.PI / 12;
    }

    private void silverfish(CemFrame frame) {
        for (int i = 0; i < 7; i++) {
            ModelPart body = part("body" + (i + 1));
            body.yRot = Mth.cos(frame.age() * .9F + i * .15F * Mth.PI) * Mth.PI * .05F * (1 + Math.abs(i - 2));
            body.x = Mth.sin(frame.age() * .9F + i * .15F * Mth.PI) * Mth.PI * .2F * Math.abs(i - 2);
        }
        part("wing1").yRot = part("body3").yRot;
        part("wing2").yRot = part("body5").yRot; part("wing2").x = part("body5").x;
        part("wing3").yRot = part("body2").yRot; part("wing3").x = part("body2").x;
    }

    private void humanoidGait(CemFrame frame, float legAmplitude) {
        look(frame);
        part("right_arm").xRot = -gait(frame, 1);
        part("left_arm").xRot = gait(frame, 1);
        part("right_leg").xRot = gait(frame, legAmplitude);
        part("left_leg").xRot = -gait(frame, legAmplitude);
        for (String name : List.of("right_arm", "left_arm", "right_leg", "left_leg")) {
            part(name).yRot = part(name).zRot = 0;
        }
        if (frame.riding()) {
            part("right_arm").xRot = part("left_arm").xRot = -Mth.PI / 5;
            part("right_leg").xRot = part("left_leg").xRot = -1.4137167F;
            part("right_leg").yRot = Mth.PI / 10; part("left_leg").yRot = -Mth.PI / 10;
            part("right_leg").zRot = .07853982F; part("left_leg").zRot = -.07853982F;
        }
    }

    private void humanoid(CemFrame frame) {
        humanoidGait(frame, 1.4F);
        ModelPart left = part("left_arm"), right = part("right_arm");
        boolean undead = host.equals("zombie") || host.equals("husk");
        if (undead) AnimationUtils.animateZombieArms(left, right, frame.aggressive(), 0, frame.age());
        else AnimationUtils.bobArms(right, left, frame.age());
        poseAction(frame, !undead);
        part("headwear").xRot = part("head").xRot;
        part("headwear").yRot = part("head").yRot;
        part("headwear").zRot = part("head").zRot;
    }

    private void illager(CemFrame frame) {
        humanoidGait(frame, .7F);
        boolean crossed = !host.startsWith("armored_") && !host.equals("pillager")
                && frame.action() == null && !frame.aggressive() && !frame.riding();
        if (parts.containsKey("arms")) part("arms").visible = crossed;
        part("left_arm").visible = part("right_arm").visible = !crossed;
        if (host.endsWith("pillager") && !crossed)
            AnimationUtils.animateCrossbowHold(part("right_arm"), part("left_arm"), part("head"), true);
        poseAction(frame, host.equals("illusioner"));
    }

    private void poseAction(CemFrame frame, boolean bow) {
        AttackKind action = frame.action();
        if (action == null || frame.deathTicks() > 0) return;
        ModelPart right = part("right_arm"), left = part("left_arm");
        float weight = ActionClipTiming.weight(frame.actionTicks(), action.windup(), action.recovery(), 3, 4);
        switch (action) {
            case MELEE, HEAVY_SLAM, LEAP -> {
                // Explicit anticipatory pose, fast downswing ending at the actual server hit, then
                // recovery. A generic Vanilla attackTime sine would peak at a different tick.
                float before = Mth.clamp(frame.actionTicks() / Math.max(1, action.windup()), 0, 1);
                float angle = before < .78F ? Mth.lerp(smooth(before / .78F), -.3F, -2.5F)
                        : Mth.lerp(smooth((before - .78F) / .22F), -2.5F, .45F);
                if (frame.actionTicks() > action.windup()) angle = Mth.lerp(
                        smooth((frame.actionTicks() - action.windup()) / Math.max(1, action.recovery())), .45F, -.3F);
                right.xRot = Mth.lerp(weight, right.xRot, angle);
                left.xRot = Mth.lerp(weight, left.xRot, action == AttackKind.HEAVY_SLAM ? angle : -.25F);
                right.yRot = Mth.lerp(weight, right.yRot, Mth.PI / 20);
                left.yRot = Mth.lerp(weight, left.yRot, -Mth.PI / 20);
            }
            case SOUL_BOLT, VOLLEY, BEAM -> {
                if (host.endsWith("pillager")) {
                    float rx = right.xRot, ry = right.yRot, lx = left.xRot, ly = left.yRot;
                    AnimationUtils.animateCrossbowHold(right, left, part("head"), true);
                    right.xRot = Mth.lerp(weight, rx, right.xRot); right.yRot = Mth.lerp(weight, ry, right.yRot);
                    left.xRot = Mth.lerp(weight, lx, left.xRot); left.yRot = Mth.lerp(weight, ly, left.yRot);
                } else if (bow) {
                    right.yRot = Mth.lerp(weight, right.yRot, -.1F + part("head").yRot);
                    right.xRot = Mth.lerp(weight, right.xRot, -Mth.HALF_PI + part("head").xRot);
                    left.xRot = Mth.lerp(weight, left.xRot, -.9424779F + part("head").xRot);
                    left.yRot = Mth.lerp(weight, left.yRot, part("head").yRot - .4F);
                    left.zRot = Mth.lerp(weight, left.zRot, Mth.HALF_PI);
                } else spell(frame, weight);
            }
            case SUMMON, ENCHANT, FIRE_FIELD, ICE_FALL, ROOT_SNARE, STORM, TELEPORT -> spell(frame, weight);
            case GUARD -> {
                right.xRot = Mth.lerp(weight, right.xRot, -1.3F); right.yRot = Mth.lerp(weight, right.yRot, -.6F);
                left.xRot = Mth.lerp(weight, left.xRot, -1.3F); left.yRot = Mth.lerp(weight, left.yRot, .6F);
            }
        }
    }

    private void spell(CemFrame frame, float weight) {
        ModelPart right = part("right_arm"), left = part("left_arm");
        float sway = Mth.cos(frame.age() * .6662F) * .25F;
        right.xRot = Mth.lerp(weight, right.xRot, sway); left.xRot = Mth.lerp(weight, left.xRot, sway);
        right.zRot = Mth.lerp(weight, right.zRot, Mth.PI * .75F);
        left.zRot = Mth.lerp(weight, left.zRot, -Mth.PI * .75F);
        right.yRot = Mth.lerp(weight, right.yRot, 0); left.yRot = Mth.lerp(weight, left.yRot, 0);
    }

    private void ravager(CemFrame frame) {
        quadruped(frame, .4F);
        ModelPart neck = part("neck"), jaw = part("jaw");
        neck.setPos(0, -7, 5.5F); neck.xRot = 0;
        jaw.xRot = Mth.PI * .01F;
        AttackKind action = frame.action();
        if (action == null || frame.deathTicks() > 0) return;
        float weight = ActionClipTiming.weight(frame.actionTicks(), action.windup(), action.recovery(), 3, 4);
        float windup = Mth.clamp(frame.actionTicks() / Math.max(1, action.windup()), 0, 1);
        if (action == AttackKind.MELEE || action == AttackKind.HEAVY_SLAM || action == AttackKind.LEAP) {
            float reach = windup < .75F ? -1.5F * smooth(windup / .75F)
                    : Mth.lerp(smooth((windup - .75F) / .25F), -1.5F, 9);
            if (frame.actionTicks() > action.windup()) reach = 9 * (1 - smooth(
                    (frame.actionTicks() - action.windup()) / Math.max(1, action.recovery())));
            neck.z -= reach * weight;
            jaw.xRot = Mth.lerp(weight, jaw.xRot, Mth.PI * .4F * Math.max(.15F, 1 - windup * .6F));
        } else jaw.xRot = Mth.lerp(weight, jaw.xRot, Mth.HALF_PI * smooth(windup));
    }

    private static float smooth(float value) {
        float t = Mth.clamp(value, 0, 1);
        return t * t * (3 - 2 * t);
    }

    private final class HostRig extends HierarchicalModel<Mob> {
        private final ModelPart root;
        private HostRig(ModelPart root) { this.root = root; }
        @Override public ModelPart root() { return root; }
        @Override public Optional<ModelPart> getAnyDescendantWithName(String name) {
            String alias = switch (name) {
                case "body" -> "torso";
                case "bone" -> "root";
                // The shipped WardenAnimation clips retain ear names while WardenModel's actual
                // geometry names these same head appendages tendrils. Bind them explicitly.
                case "left_ear" -> "left_tendril";
                case "right_ear" -> "right_tendril";
                default -> name;
            };
            ModelPart part = parts.get(alias);
            if (part == null) throw new IllegalArgumentException(id + ": vanilla host animation references missing alias '" + alias + "'");
            return Optional.of(part);
        }
        @Override public void setupAnim(Mob entity, float swing, float amount, float age, float yaw, float pitch) { }
    }
}
