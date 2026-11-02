package de.laugenbreze.dungeonsalive.client.entity.reference;

import de.laugenbreze.dungeonsalive.entity.AttackKind;
import net.minecraft.client.animation.AnimationChannel;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.animation.Keyframe;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

/** Explicit bindings between a licensed source rig and our server-authoritative actions. */
public record ReferenceMobDefinition(
        String id, Supplier<LayerDefinition> layer, ResourceLocation texture,
        float scale, float shadow, Map<String, AnimationDefinition> clips,
        List<String> baseClips, @Nullable String idleClip, @Nullable WalkBinding walk, @Nullable SwimBinding swim,
        Map<AttackKind, ActionBinding> actions, @Nullable String deathClip,
        @Nullable LookBinding look, @Nullable HumanoidGait humanoidGait,
        List<TextureLayer> textureLayers, List<String> hiddenBones, List<BoneMotion> boneMotions,
        Map<String, BoneScale> boneScales, List<ExpressionBinding> expressions,
        @Nullable String cemDriver, Map<String, String> hostAliases, @Nullable Supplier<ModelPart> bakedRoot,
        @Nullable HandItems handItems, List<AnimatedOverlay> animatedOverlays, List<MaterialLayer> materialLayers) {

    public record WalkBinding(String clip, float speed, float amplitude) {}
    public record SwimBinding(String idleClip, String moveClip, float speed, float amplitude) {}
    public record LookBinding(String bone, float maxYawDegrees, float maxPitchDegrees) {}
    public record HumanoidGait(String leftLeg, String rightLeg, String leftArm, String rightArm,
                               float speed, float amplitude) {}
    public record TextureLayer(ResourceLocation texture, boolean emissive, boolean fullBright,
                               float idleStrength, float pulseAmplitude, float attackBoost) {}
    public enum Axis { X, Y, Z }
    /** Spin speed is degrees/sec; oscillator speed is cycles/sec and amplitude is degrees. */
    public record BoneMotion(String bone, Axis axis, float speed, float amplitude, float phaseDegrees, boolean spin) {}
    public record BoneScale(float x, float y, float z) {}
    public record ExpressionBinding(String target, String expression) {}
    /** Additional material geometry with exactly the master's complete named bone hierarchy. */
    public record MaterialLayer(ResourceLocation texture, Supplier<ModelPart> bakedRoot) {
        public MaterialLayer {
            Objects.requireNonNull(texture, "Material texture");
            Objects.requireNonNull(bakedRoot, "Material geometry");
        }
    }
    /** Paths start immediately below the baked root; a literal root bone is never stripped. */
    public record HandItems(@Nullable String right, @Nullable String left, boolean palmAnchor) {
        public HandItems {
            if (right == null && left == null) throw new IllegalArgumentException("Held items need at least one hand path");
            if (right != null && right.isBlank() || left != null && left.isBlank())
                throw new IllegalArgumentException("Held-item bone paths must not be blank");
        }
    }
    /** Sequential source PNGs named prefix + frame index + .png, starting at zero. */
    public record AnimatedOverlay(ResourceLocation prefix, int frameCount, int ticksPerFrame, float alpha, boolean fullBright) {
        public AnimatedOverlay {
            Objects.requireNonNull(prefix, "Animated overlay prefix");
            if (frameCount < 1 || frameCount > 1024 || ticksPerFrame < 1)
                throw new IllegalArgumentException("Invalid animated overlay frame count or clock");
            if (!Float.isFinite(alpha) || alpha < 0 || alpha > 1)
                throw new IllegalArgumentException("Animated overlay alpha must be between zero and one");
        }
        public ResourceLocation frame(int index) {
            if (index < 0 || index >= frameCount) throw new IllegalArgumentException("Overlay frame outside its sequence");
            return prefix.withPath(prefix.getPath() + index + ".png");
        }
        public int frameIndex(double time) {
            if (!Double.isFinite(time)) throw new IllegalArgumentException("Nonfinite overlay clock");
            return (int)Math.floorMod((long)Math.floor(time / ticksPerFrame), frameCount);
        }
    }

    public record ActionBinding(String clip, float hitSeconds, float blendInTicks, float blendOutTicks) {
        public ActionBinding {
            Objects.requireNonNull(clip, "Action clip");
            finiteNonNegative(hitSeconds, "Source impact time");
            finiteNonNegative(blendInTicks, "Blend-in time");
            finiteNonNegative(blendOutTicks, "Blend-out time");
        }
    }

    public ReferenceMobDefinition {
        if (id == null || !id.matches("[a-z0-9_]+")) throw new IllegalArgumentException("Invalid model id: " + id);
        Objects.requireNonNull(layer, "Model geometry");
        Objects.requireNonNull(texture, "Model texture");
        finitePositive(scale, "Uniform scale");
        if (shadow != -1) finiteNonNegative(shadow, "Shadow size");
        clips = Map.copyOf(clips);
        baseClips = List.copyOf(baseClips);
        actions = Map.copyOf(actions);
        textureLayers = List.copyOf(textureLayers);
        hiddenBones = List.copyOf(hiddenBones);
        boneMotions = List.copyOf(boneMotions);
        boneScales = Map.copyOf(boneScales);
        expressions = List.copyOf(expressions);
        hostAliases = Map.copyOf(hostAliases);
        animatedOverlays = List.copyOf(animatedOverlays);
        materialLayers = List.copyOf(materialLayers);
        for (var entry : clips.entrySet()) validateClip(id, entry.getKey(), entry.getValue());
        for (String clip : baseClips) requireClip(id, clips, clip);
        if (idleClip != null) requireClip(id, clips, idleClip);
        if (walk != null) {
            requireClip(id, clips, walk.clip());
            finitePositive(walk.speed(), "Walk speed");
            finitePositive(walk.amplitude(), "Walk amplitude");
        }
        if (swim != null) {
            requireClip(id, clips, swim.idleClip());
            requireClip(id, clips, swim.moveClip());
            finitePositive(swim.speed(), "Swim speed");
            finitePositive(swim.amplitude(), "Swim amplitude");
        }
        if (deathClip != null) requireClip(id, clips, deathClip);
        for (var entry : actions.entrySet()) {
            AnimationDefinition clip = requireClip(id, clips, entry.getValue().clip());
            finitePositive(clip.lengthInSeconds(), "Action clip duration");
            if (entry.getValue().hitSeconds() > clip.lengthInSeconds())
                throw new IllegalArgumentException(id + "/" + entry.getKey() + ": impact exceeds clip length");
        }
        if (look != null) {
            finiteNonNegative(look.maxYawDegrees(), "Head yaw limit");
            finiteNonNegative(look.maxPitchDegrees(), "Head pitch limit");
        }
        if (humanoidGait != null) {
            finitePositive(humanoidGait.speed(), "Gait speed");
            finitePositive(humanoidGait.amplitude(), "Gait amplitude");
        }
        for (TextureLayer textureLayer : textureLayers) {
            Objects.requireNonNull(textureLayer.texture(), "Overlay texture");
            finiteNonNegative(textureLayer.idleStrength(), "Glow strength");
            finiteNonNegative(textureLayer.pulseAmplitude(), "Glow pulse");
            finiteNonNegative(textureLayer.attackBoost(), "Attack glow");
        }
        for (BoneMotion motion : boneMotions) {
            Objects.requireNonNull(motion.bone(), "Procedural bone");
            Objects.requireNonNull(motion.axis(), "Procedural axis");
            if (!Float.isFinite(motion.speed()) || !Float.isFinite(motion.amplitude()) || !Float.isFinite(motion.phaseDegrees()))
                throw new IllegalArgumentException(id + ": nonfinite procedural motion");
        }
        for (BoneScale value : boneScales.values()) {
            finitePositive(value.x(), "Bone X scale"); finitePositive(value.y(), "Bone Y scale"); finitePositive(value.z(), "Bone Z scale");
        }
        if (cemDriver != null && !List.of("warden", "guardian", "wither", "slime", "chicken", "cow", "pig", "sheep", "goat", "cat", "turtle", "fox", "rabbit",
                "wandering_trader", "villager", "vindicator", "evoker", "illusioner", "pillager", "armored_pillager", "armored_vindicator", "skeleton", "stray", "zombie", "husk",
                "phantom", "allay", "glow_squid", "squid", "parrot", "silverfish", "ravager").contains(cemDriver))
            throw new IllegalArgumentException(id + ": unsupported CEM host '" + cemDriver + "'");
    }

    public static Builder builder(String id, Supplier<LayerDefinition> layer, String textureResource) {
        return new Builder(id, layer, ResourceLocation.parse(textureResource));
    }

    private static AnimationDefinition requireClip(String id, Map<String, AnimationDefinition> clips, String name) {
        AnimationDefinition clip = clips.get(name);
        if (clip == null) throw new IllegalArgumentException(id + ": missing animation '" + name + "'");
        return clip;
    }

    private static void validateClip(String id, String name, AnimationDefinition clip) {
        Objects.requireNonNull(name, "Clip name");
        Objects.requireNonNull(clip, "Animation clip");
        finiteNonNegative(clip.lengthInSeconds(), id + "/" + name + " length");
        for (var bone : clip.boneAnimations().entrySet()) {
            if (bone.getKey().isBlank()) throw new IllegalArgumentException(id + "/" + name + ": empty bone name");
            for (AnimationChannel channel : bone.getValue()) {
                if (channel.keyframes().length == 0)
                    throw new IllegalArgumentException(id + "/" + name + "/" + bone.getKey() + ": empty channel");
                float previous = -1;
                for (Keyframe keyframe : channel.keyframes()) {
                    if (!Float.isFinite(keyframe.timestamp()) || keyframe.timestamp() < 0 || keyframe.timestamp() < previous)
                        throw new IllegalArgumentException(id + "/" + name + ": unordered or invalid keyframe time");
                    if (!keyframe.target().isFinite())
                        throw new IllegalArgumentException(id + "/" + name + ": nonfinite keyframe vector");
                    Objects.requireNonNull(keyframe.interpolation(), "Keyframe interpolation");
                    previous = keyframe.timestamp();
                }
            }
        }
    }

    private static void finitePositive(float value, String label) {
        if (!Float.isFinite(value) || value <= 0) throw new IllegalArgumentException(label + " must be finite and positive");
    }
    private static void finiteNonNegative(float value, String label) {
        if (!Float.isFinite(value) || value < 0) throw new IllegalArgumentException(label + " must be finite and nonnegative");
    }

    public static final class Builder {
        private final String id;
        private final Supplier<LayerDefinition> layer;
        private final ResourceLocation texture;
        private float scale = 1, shadow = -1;
        private final Map<String, AnimationDefinition> clips = new LinkedHashMap<>();
        private final List<String> baseClips = new ArrayList<>();
        private final Map<AttackKind, ActionBinding> actions = new EnumMap<>(AttackKind.class);
        private final List<TextureLayer> textureLayers = new ArrayList<>();
        private final List<String> hiddenBones = new ArrayList<>();
        private final List<BoneMotion> boneMotions = new ArrayList<>();
        private final Map<String, BoneScale> boneScales = new LinkedHashMap<>();
        private final List<ExpressionBinding> expressions = new ArrayList<>();
        private final Map<String, String> hostAliases = new LinkedHashMap<>();
        private String idleClip, deathClip;
        private WalkBinding walk;
        private SwimBinding swim;
        private LookBinding look;
        private HumanoidGait humanoidGait;
        private String cemDriver;
        private Supplier<ModelPart> bakedRoot;
        private HandItems handItems;
        private final List<AnimatedOverlay> animatedOverlays = new ArrayList<>();
        private final List<MaterialLayer> materialLayers = new ArrayList<>();

        private Builder(String id, Supplier<LayerDefinition> layer, ResourceLocation texture) {
            this.id = id; this.layer = layer; this.texture = texture;
        }
        public Builder scale(float value) { scale = value; return this; }
        public Builder shadow(float value) { shadow = value; return this; }
        public Builder bakedRoot(Supplier<ModelPart> factory) { bakedRoot = Objects.requireNonNull(factory); return this; }
        public Builder handItems(String rightPath, String leftPath) { return handItems(rightPath, leftPath, false); }
        public Builder handItems(String rightPath, String leftPath, boolean palmAnchor) {
            handItems = new HandItems(rightPath, leftPath, palmAnchor); return this;
        }
        public Builder animatedOverlay(String prefix, int frameCount, int ticksPerFrame, float alpha, boolean fullBright) {
            animatedOverlays.add(new AnimatedOverlay(ResourceLocation.parse(prefix), frameCount, ticksPerFrame, alpha, fullBright));
            return this;
        }
        public Builder materialLayer(String texture, Supplier<ModelPart> bakedRoot) {
            materialLayers.add(new MaterialLayer(ResourceLocation.parse(texture), bakedRoot)); return this;
        }
        public Builder clip(String name, AnimationDefinition clip) {
            if (clips.putIfAbsent(name, clip) != null) throw new IllegalArgumentException(id + ": duplicate clip " + name);
            return this;
        }
        public Builder base(String clip) { baseClips.add(clip); return this; }
        public Builder idle(String clip) { idleClip = clip; return this; }
        public Builder walk(String clip, float speed, float amplitude) {
            walk = new WalkBinding(clip, speed, amplitude); return this;
        }
        public Builder swim(String idleClip, String moveClip, float speed, float amplitude) {
            swim = new SwimBinding(idleClip, moveClip, speed, amplitude); return this;
        }
        public Builder action(AttackKind action, String clip, float sourceHitSeconds) {
            return action(action, clip, sourceHitSeconds, 3, 4);
        }
        public Builder action(AttackKind action, String clip, float sourceHitSeconds, float blendInTicks, float blendOutTicks) {
            if (actions.putIfAbsent(action, new ActionBinding(clip, sourceHitSeconds, blendInTicks, blendOutTicks)) != null)
                throw new IllegalArgumentException(id + ": duplicate action " + action);
            return this;
        }
        public Builder death(String clip) { deathClip = clip; return this; }
        public Builder look(String bone, float maxYawDegrees, float maxPitchDegrees) {
            look = new LookBinding(bone, maxYawDegrees, maxPitchDegrees); return this;
        }
        public Builder humanoidGait(String leftLeg, String rightLeg, String leftArm, String rightArm, float speed, float amplitude) {
            humanoidGait = new HumanoidGait(leftLeg, rightLeg, leftArm, rightArm, speed, amplitude); return this;
        }
        public Builder emissive(String resource, float idleStrength, float pulseAmplitude, float attackBoost) {
            textureLayers.add(new TextureLayer(ResourceLocation.parse(resource), true, true, idleStrength, pulseAmplitude, attackBoost));
            return this;
        }
        public Builder overlay(String resource, boolean fullBright) {
            textureLayers.add(new TextureLayer(ResourceLocation.parse(resource), false, fullBright, 1, 0, 0)); return this;
        }
        public Builder hiddenBones(String... names) { hiddenBones.addAll(List.of(names)); return this; }
        public Builder boneScale(String bone, float uniformScale) { return boneScale(bone, uniformScale, uniformScale, uniformScale); }
        public Builder boneScale(String bone, float x, float y, float z) {
            if (boneScales.putIfAbsent(bone, new BoneScale(x, y, z)) != null)
                throw new IllegalArgumentException(id + ": duplicate static bone scale " + bone);
            return this;
        }
        public Builder expression(String target, String sourceExpression) {
            expressions.add(new ExpressionBinding(target, sourceExpression)); return this;
        }
        public Builder cemDriver(String host) { cemDriver = host.toLowerCase(Locale.ROOT); return this; }
        public Builder hostAlias(String sourceName, String rigNode) {
            if (hostAliases.putIfAbsent(sourceName, rigNode) != null)
                throw new IllegalArgumentException(id + ": duplicate host alias " + sourceName);
            return this;
        }
        public Builder spin(String bone, String axis, float degreesPerSecond) {
            boneMotions.add(new BoneMotion(bone, Axis.valueOf(axis.toUpperCase(Locale.ROOT)), degreesPerSecond, 0, 0, true));
            return this;
        }
        public Builder oscillate(String bone, String axis, float amplitudeDegrees, float cyclesPerSecond, float phaseDegrees) {
            boneMotions.add(new BoneMotion(bone, Axis.valueOf(axis.toUpperCase(Locale.ROOT)), cyclesPerSecond, amplitudeDegrees, phaseDegrees, false));
            return this;
        }
        public ReferenceMobDefinition build() {
            return new ReferenceMobDefinition(id, layer, texture, scale, shadow, clips, baseClips, idleClip, walk, swim,
                    actions, deathClip, look, humanoidGait, textureLayers, hiddenBones, boneMotions, boneScales, expressions, cemDriver, hostAliases, bakedRoot, handItems, animatedOverlays, materialLayers);
        }
    }
}
