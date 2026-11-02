package de.laugenbreze.dungeonsalive.client.entity.reference;

import de.laugenbreze.dungeonsalive.entity.AttackKind;
import net.minecraft.client.animation.AnimationChannel;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.animation.KeyframeAnimations;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.animal.Fox;
import org.joml.Vector3f;

import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Plays authored vanilla keyframe clips on their original named rig. No server effects live here.
 * Each frame starts from the bind pose, so the renderer can safely be shared by many entities.
 */
public final class ReferenceMobModel<T extends Mob> extends HierarchicalModel<T> {
    private static final int POSITION = 1, ROTATION = 2, SCALE = 4;
    private final ModelPart root;
    private final ReferenceMobDefinition definition;
    private final List<ModelPart> parts;
    private final Map<String, Optional<ModelPart>> boneCache = new HashMap<>();
    private final Map<String, AnimationDefinition> singleShotClips = new HashMap<>();
    private final Map<String, int[]> channelMasks = new HashMap<>();
    private final Vector3f vector = new Vector3f();
    private final float[] locomotionPose;
    private final CemPoseProgram cemProgram;
    private final ReferenceAnimationSource<T> animationSource;

    public ReferenceMobModel(ModelPart root, ReferenceMobDefinition definition) {
        this(root, definition, ReferenceAnimationSource.compatible());
    }

    public ReferenceMobModel(ModelPart root, ReferenceMobDefinition definition, ReferenceAnimationSource<T> animationSource) {
        this.root = root;
        this.definition = definition;
        this.animationSource = java.util.Objects.requireNonNull(animationSource);
        parts = root.getAllParts().toList();
        locomotionPose = new float[parts.size() * 9];
        validateRig();
        cemProgram = definition.expressions().isEmpty() && definition.cemDriver() == null ? null
                : new CemPoseProgram(definition, root, this::requireBone);
        for (String name : definition.hiddenBones()) requireBone(name).visible = false;
        reset();
        applyBases(0);
        if (definition.idleClip() != null) apply(definition.idleClip(), 0, 1, false);
        if (cemProgram != null) {
            cemProgram.animateHosts(CemFrame.rest());
            cemProgram.apply(CemFrame.rest());
        }
        ReferenceModels.recordBounds(definition.id(), ReferenceModels.measure(root, definition));
        reset();
    }

    @Override public ModelPart root() { return root; }
    public ReferenceMobDefinition definition() { return definition; }
    public float expressionShadowSize() { return cemProgram == null ? -1 : cemProgram.shadowSize(); }
    public ReferenceAnimationSource<T> animationSource() { return animationSource; }

    @Override public Optional<ModelPart> getAnyDescendantWithName(String name) {
        return boneCache.computeIfAbsent(name, key -> super.getAnyDescendantWithName(key));
    }

    private ModelPart requireBone(String name) {
        return getAnyDescendantWithName(name).orElseThrow(() ->
                new IllegalArgumentException(definition.id() + ": animation references absent bone '" + name + "'"));
    }

    private void validateRig() {
        Map<ModelPart, Integer> indices = new IdentityHashMap<>();
        for (int i = 0; i < parts.size(); i++) indices.put(parts.get(i), i);
        for (var entry : definition.clips().entrySet()) {
            var clip = entry.getValue();
            singleShotClips.put(entry.getKey(), new AnimationDefinition(clip.lengthInSeconds(), false, clip.boneAnimations()));
            int[] masks = new int[parts.size()];
            for (var bone : clip.boneAnimations().entrySet()) {
                ModelPart target = requireBone(bone.getKey());
                for (AnimationChannel channel : bone.getValue()) {
                    int bit;
                    if (channel.target() == AnimationChannel.Targets.POSITION) bit = POSITION;
                    else if (channel.target() == AnimationChannel.Targets.ROTATION) bit = ROTATION;
                    else if (channel.target() == AnimationChannel.Targets.SCALE) bit = SCALE;
                    else throw new IllegalArgumentException(definition.id() + "/" + entry.getKey() + ": unsupported custom animation target");
                    masks[indices.get(target)] |= bit;
                }
            }
            channelMasks.put(entry.getKey(), masks);
        }
        if (definition.look() != null) requireBone(definition.look().bone());
        if (definition.humanoidGait() != null) {
            var gait = definition.humanoidGait();
            requireBone(gait.leftLeg()); requireBone(gait.rightLeg());
            requireBone(gait.leftArm()); requireBone(gait.rightArm());
        }
        for (var motion : definition.boneMotions()) requireBone(motion.bone());
        for (String bone : definition.boneScales().keySet()) requireBone(bone);
        if (definition.handItems() != null) {
            var hands = definition.handItems();
            if (hands.right() != null) new ModelBonePath(root, hands.right(), definition.id());
            if (hands.left() != null) new ModelBonePath(root, hands.left(), definition.id());
        }
    }

    @Override public void setupAnim(T mob, float limbSwing, float limbAmount, float age, float yaw, float pitch) {
        float partialTick = Mth.clamp(age - mob.tickCount, 0, 1);
        var previewGesture = ReferencePreviewState.gesture(mob, partialTick);
        // A detached guide preview continues animating while its singleplayer level is paused.
        // Real entities retain the original CEM world-time clock.
        float worldTime = ReferencePreviewState.isLocalPreview(mob) ? age : (mob.level().getGameTime() % 24_000) + partialTick;
        poseForFrame(new CemFrame(age, worldTime, limbSwing, limbAmount, yaw, pitch,
                Math.max(0, mob.hurtTime - partialTick), mob.deathTime > 0 ? mob.deathTime + partialTick : 0,
                mob.getHealth(), mob.getMaxHealth(), mob.isInWater(), mob.onGround(), mob.isAggressive(), animationSource.action(mob),
                animationSource.actionTicks(mob, partialTick), previewGesture != null ? previewGesture : animationSource.gesture(mob, partialTick), mob.isVehicle(),
                mob.getPose() == Pose.SITTING || mob instanceof TamableAnimal animal && animal.isInSittingPose() || mob instanceof Fox fox && fox.isSitting()
                        || mob instanceof de.laugenbreze.dungeonsalive.entity.peaceful.DungeonsCreature bird && bird.birdPerched(),
                mob.isPassenger(), mob.isBaby(), mob.isInWater() && mob.isSwimming()));
        if (definition.id().equals("pet_duck")
                && mob instanceof de.laugenbreze.dungeonsalive.entity.peaceful.DungeonsCreature duck)
            DuckFamilyPose.apply(root, duck, age);
    }

    /** Package-visible deterministic frame evaluation also used by rig/timing regression tests. */
    void poseForFrame(float limbSwing, float limbAmount, float age, float yaw, float pitch,
                      AttackKind action, float actionTicks, float deathTicks, boolean inWater) {
        poseForFrame(new CemFrame(age, age, limbSwing, limbAmount, yaw, pitch, 0, deathTicks,
                deathTicks > 0 ? 0 : 1, 1, inWater, true, action != null, action, actionTicks, null, false, false, false, false));
    }

    void poseForFrame(CemFrame frame) {
        float limbSwing = frame.limbSwing(), limbAmount = frame.limbSpeed(), age = frame.age();
        float yaw = frame.headYaw(), pitch = frame.headPitch(), deathTicks = frame.deathTicks(), actionTicks = frame.actionTicks();
        AttackKind action = frame.action();
        reset();
        long lifeMillis = Math.max(0, (long)(age * 50));
        applyBases(lifeMillis);
        if (cemProgram != null) cemProgram.animateHosts(frame);
        var swim = definition.swim();
        if (swim != null && frame.inWater()) {
            float moving = Mth.clamp(limbAmount * swim.amplitude(), 0, 1);
            apply(swim.idleClip(), lifeMillis, 1 - moving, false);
            apply(swim.moveClip(), (long)(limbSwing * 50 * swim.speed()), moving, false);
        } else {
            var walk = definition.walk();
            float moving = walk != null ? Mth.clamp(limbAmount * walk.amplitude(), 0, 1) : 0;
            if (definition.idleClip() != null) apply(definition.idleClip(), lifeMillis, 1 - moving, false);
            if (walk != null) apply(walk.clip(), (long)(limbSwing * 50 * walk.speed()), moving, false);
            else if (definition.humanoidGait() != null) gait(limbSwing, limbAmount);
        }
        if (definition.look() != null) {
            var look = definition.look();
            var head = requireBone(look.bone());
            head.yRot += Mth.clamp(yaw, -look.maxYawDegrees(), look.maxYawDegrees()) * Mth.DEG_TO_RAD;
            head.xRot += Mth.clamp(pitch, -look.maxPitchDegrees(), look.maxPitchDegrees()) * Mth.DEG_TO_RAD;
        }
        if (cemProgram != null) cemProgram.apply(frame);

        if (deathTicks > 0 && definition.deathClip() != null) {
            String name = definition.deathClip();
            float length = definition.clips().get(name).lengthInSeconds();
            // Common mobs currently disappear after twenty death ticks. Fit the authored collapse
            // into that existing server lifetime, rather than restarting a looping clip or hiding it.
            float t = Mth.clamp(deathTicks / 20, 0, 1);
            blendClip(name, length * t, Mth.clamp(t * 8, 0, 1), lifeMillis);
            applyBoneMotions(age, deathTicks);
            return;
        }
        var binding = action == null ? null : definition.actions().get(action);
        if (binding != null) {
            float length = definition.clips().get(binding.clip()).lengthInSeconds();
            float seconds = ActionClipTiming.seconds(actionTicks, action.windup(), action.recovery(), binding.hitSeconds(), length);
            float weight = ActionClipTiming.weight(actionTicks, action.windup(), action.recovery(), binding.blendInTicks(), binding.blendOutTicks());
            blendClip(binding.clip(), seconds, weight, lifeMillis);
        } else if (frame.gesture() != null && deathTicks <= 0) {
            var gesture = frame.gesture();
            AnimationDefinition clip = definition.clips().get(gesture.clip());
            if (clip == null) throw new IllegalArgumentException(definition.id() + ": gesture refers to missing clip '" + gesture.clip() + "'");
            float progress = Mth.clamp(gesture.elapsedTicks() / gesture.durationTicks(), 0, 1);
            float entrance = gesture.blendInTicks() == 0 ? 1 : Mth.clamp(gesture.elapsedTicks() / gesture.blendInTicks(), 0, 1);
            float exit = gesture.blendOutTicks() == 0 ? 1 : Mth.clamp((gesture.durationTicks() - gesture.elapsedTicks()) / gesture.blendOutTicks(), 0, 1);
            entrance = entrance * entrance * (3 - 2 * entrance);
            exit = exit * exit * (3 - 2 * exit);
            blendClip(gesture.clip(), progress * clip.lengthInSeconds(), entrance * exit, lifeMillis);
        }
        applyBoneMotions(age, deathTicks);
    }

    private void applyBoneMotions(float age, float deathTicks) {
        float lifeSeconds = age / 20;
        float strength = deathTicks > 0 ? Mth.clamp(1 - deathTicks / 20, 0, 1) : 1;
        for (var motion : definition.boneMotions()) {
            float degrees = motion.spin() ? lifeSeconds * motion.speed()
                    : Mth.sin(lifeSeconds * motion.speed() * Mth.TWO_PI + motion.phaseDegrees() * Mth.DEG_TO_RAD) * motion.amplitude();
            float radians = degrees * Mth.DEG_TO_RAD * strength;
            ModelPart bone = requireBone(motion.bone());
            switch (motion.axis()) {
                case X -> bone.xRot += radians;
                case Y -> bone.yRot += radians;
                case Z -> bone.zRot += radians;
            }
        }
    }

    private void gait(float limbSwing, float limbAmount) {
        var gait = definition.humanoidGait();
        float wave = Mth.cos(limbSwing * gait.speed()) * limbAmount * gait.amplitude();
        requireBone(gait.leftLeg()).xRot += wave;
        requireBone(gait.rightLeg()).xRot -= wave;
        requireBone(gait.leftArm()).xRot -= wave * .7F;
        requireBone(gait.rightArm()).xRot += wave * .7F;
    }

    private void blendClip(String clip, float seconds, float weight, long lifeMillis) {
        if (weight <= 0) return;
        capture();
        reset();
        applyBases(lifeMillis);
        apply(clip, Math.round(seconds * 1000), 1, true);
        int[] masks = channelMasks.get(clip);
        for (int i = 0; i < parts.size(); i++) {
            ModelPart part = parts.get(i);
            int offset = i * 9;
            float positionWeight = (masks[i] & POSITION) != 0 ? weight : 0;
            float rotationWeight = (masks[i] & ROTATION) != 0 ? weight : 0;
            float scaleWeight = (masks[i] & SCALE) != 0 ? weight : 0;
            part.x = Mth.lerp(positionWeight, locomotionPose[offset], part.x);
            part.y = Mth.lerp(positionWeight, locomotionPose[offset + 1], part.y);
            part.z = Mth.lerp(positionWeight, locomotionPose[offset + 2], part.z);
            // Source Euler angles intentionally include full turns. Do not wrap to the shortest arc.
            part.xRot = Mth.lerp(rotationWeight, locomotionPose[offset + 3], part.xRot);
            part.yRot = Mth.lerp(rotationWeight, locomotionPose[offset + 4], part.yRot);
            part.zRot = Mth.lerp(rotationWeight, locomotionPose[offset + 5], part.zRot);
            part.xScale = Mth.lerp(scaleWeight, locomotionPose[offset + 6], part.xScale);
            part.yScale = Mth.lerp(scaleWeight, locomotionPose[offset + 7], part.yScale);
            part.zScale = Mth.lerp(scaleWeight, locomotionPose[offset + 8], part.zScale);
        }
    }

    private void capture() {
        for (int i = 0; i < parts.size(); i++) {
            ModelPart part = parts.get(i);
            int offset = i * 9;
            locomotionPose[offset] = part.x; locomotionPose[offset + 1] = part.y; locomotionPose[offset + 2] = part.z;
            locomotionPose[offset + 3] = part.xRot; locomotionPose[offset + 4] = part.yRot; locomotionPose[offset + 5] = part.zRot;
            locomotionPose[offset + 6] = part.xScale; locomotionPose[offset + 7] = part.yScale; locomotionPose[offset + 8] = part.zScale;
        }
    }
    private void reset() {
        parts.forEach(ModelPart::resetPose);
        definition.boneScales().forEach((name, scale) -> {
            ModelPart bone = requireBone(name);
            bone.xScale = scale.x(); bone.yScale = scale.y(); bone.zScale = scale.z();
        });
    }
    private void applyBases(long millis) { for (String name : definition.baseClips()) apply(name, millis, 1, false); }
    private void apply(String name, long millis, float weight, boolean singleShot) {
        AnimationDefinition clip = definition.clips().get(name);
        // Several source rigs have static zero-length SCALE clips marked looping. Avoid modulo zero.
        if (clip.lengthInSeconds() == 0) { singleShot = true; millis = 0; }
        KeyframeAnimations.animate(this, singleShot ? singleShotClips.get(name) : clip, Math.max(0, millis), Math.max(0, weight), vector);
    }
}
