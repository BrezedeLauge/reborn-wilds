package de.laugenbreze.dungeonsalive.client.entity.reference;

import com.mojang.blaze3d.vertex.PoseStack;
import de.laugenbreze.dungeonsalive.client.entity.MobRenderers;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;

/** Original rig proportions are retained by one uniform scale, with independent material layers. */
public class ReferenceMobRenderer<T extends Mob> extends MobRenderer<T, ReferenceMobModel<T>> {
    private final ReferenceMobDefinition definition;

    public ReferenceMobRenderer(EntityRendererProvider.Context context, ReferenceMobDefinition definition) {
        this(context, definition, ReferenceAnimationSource.compatible());
    }
    public ReferenceMobRenderer(EntityRendererProvider.Context context, ReferenceMobDefinition definition, ReferenceAnimationSource<T> source) {
        super(context, new ReferenceMobModel<T>(definition.bakedRoot() != null ? definition.bakedRoot().get()
                        : context.bakeLayer(MobRenderers.layer(definition.id())), definition, source),
                definition.shadow() >= 0 ? definition.shadow() : .5F);
        this.definition = definition;
        if (definition.shadow() < 0) shadowRadius = ReferenceModels.bounds(definition.id()).width() * .25F;
        requireTexture(definition.texture());
        for (var material : definition.materialLayers()) {
            requireTexture(material.texture());
            addLayer(new GeometryMaterialLayer<>(this, material));
        }
        for (var textureLayer : definition.textureLayers()) {
            requireTexture(textureLayer.texture());
            addLayer(new MaterialLayer<>(this, textureLayer));
        }
        for (var animated : definition.animatedOverlays()) {
            for (int i = 0; i < animated.frameCount(); i++) requireTexture(animated.frame(i));
            addLayer(new AnimatedMaterialLayer<>(this, animated));
        }
        if (definition.handItems() != null) addLayer(new ReferenceHandItemLayer<>(this, context.getItemInHandRenderer()));
    }

    private void requireTexture(ResourceLocation resource) {
        if (Minecraft.getInstance().getResourceManager().getResource(resource).isEmpty())
            throw new IllegalArgumentException(definition.id() + ": missing model texture " + resource);
    }

    @Override public ResourceLocation getTextureLocation(T mob) { return definition.texture(); }
    @Override protected void scale(T mob, PoseStack pose, float partialTick) {
        pose.scale(definition.scale(), definition.scale(), definition.scale());
    }
    @Override protected float getFlipDegrees(T mob) {
        return definition.deathClip() == null ? super.getFlipDegrees(mob) : 0;
    }
    @Override protected float getShadowRadius(T mob) {
        float dynamic = model.expressionShadowSize();
        return dynamic >= 0 ? dynamic : super.getShadowRadius(mob);
    }

    private static final class MaterialLayer<T extends Mob> extends RenderLayer<T, ReferenceMobModel<T>> {
        private final ReferenceMobDefinition.TextureLayer layer;
        private MaterialLayer(RenderLayerParent<T, ReferenceMobModel<T>> parent, ReferenceMobDefinition.TextureLayer layer) {
            super(parent); this.layer = layer;
        }
        @Override public void render(PoseStack pose, MultiBufferSource buffers, int light, T mob,
                                     float limbSwing, float limbAmount, float partialTick, float age, float yaw, float pitch) {
            if (mob.isInvisible()) return;
            if (layer.emissive()) {
                float intensity = layer.idleStrength() + Mth.sin(age * .065F) * layer.pulseAmplitude();
                var source = getParentModel().animationSource();
                var action = source.action(mob);
                if (action != null) {
                    float anticipation = Mth.clamp(source.actionTicks(mob, partialTick) / action.windup(), 0, 1);
                    intensity += anticipation * layer.attackBoost();
                }
                if (mob.deathTime > 0) intensity *= Mth.clamp(1 - (mob.deathTime + partialTick) / 20F, 0, 1);
                int value = Math.round(255 * Mth.clamp(intensity, 0, 1));
                if (value == 0) return;
                // Vanilla eyes uses an unlit shader and additive RGB; alpha does not dim this pass.
                getParentModel().renderToBuffer(pose, buffers.getBuffer(RenderType.eyes(layer.texture())),
                        LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, FastColor.ARGB32.color(255, value, value, value));
            } else {
                getParentModel().renderToBuffer(pose, buffers.getBuffer(RenderType.entityCutoutNoCull(layer.texture())),
                        layer.fullBright() ? LightTexture.FULL_BRIGHT : light, OverlayTexture.NO_OVERLAY, -1);
            }
        }
    }

    private static final class GeometryMaterialLayer<T extends Mob> extends RenderLayer<T, ReferenceMobModel<T>> {
        private final ResourceLocation texture;
        private final ModelPart root;
        private final ModelPoseCopy poseCopy;
        private GeometryMaterialLayer(RenderLayerParent<T, ReferenceMobModel<T>> parent, ReferenceMobDefinition.MaterialLayer material) {
            super(parent); texture = material.texture(); root = material.bakedRoot().get();
            poseCopy = new ModelPoseCopy(getParentModel().root(), root, getParentModel().definition().id() + "/" + texture);
        }
        @Override public void render(PoseStack pose, MultiBufferSource buffers, int light, T mob,
                                     float limbSwing, float limbAmount, float partialTick, float age, float yaw, float pitch) {
            if (mob.isInvisible()) return;
            poseCopy.copy();
            root.render(pose, buffers.getBuffer(RenderType.entityCutoutNoCull(texture)), light,
                    LivingEntityRenderer.getOverlayCoords(mob, 0), -1);
        }
    }

    private static final class AnimatedMaterialLayer<T extends Mob> extends RenderLayer<T, ReferenceMobModel<T>> {
        private final ReferenceMobDefinition.AnimatedOverlay layer;
        private final ResourceLocation[] frames;
        private AnimatedMaterialLayer(RenderLayerParent<T, ReferenceMobModel<T>> parent, ReferenceMobDefinition.AnimatedOverlay layer) {
            super(parent); this.layer = layer;
            frames = new ResourceLocation[layer.frameCount()];
            for (int i = 0; i < frames.length; i++) frames[i] = layer.frame(i);
        }
        @Override public void render(PoseStack pose, MultiBufferSource buffers, int light, T mob,
                                     float limbSwing, float limbAmount, float partialTick, float age, float yaw, float pitch) {
            if (mob.isInvisible() || mob.isDeadOrDying() || layer.alpha() == 0) return;
            double clock = ReferencePreviewState.isLocalPreview(mob) ? age : mob.level().getGameTime() + (double)partialTick;
            ResourceLocation texture = frames[layer.frameIndex(clock)];
            // Goety's source fire material uses this shader/transparency/write-mask combination.
            // Unlike the additive eye material, this pass preserves texture alpha and layer alpha.
            RenderType renderType = layer.fullBright() ? RenderType.entityTranslucentEmissive(texture) : RenderType.entityTranslucent(texture);
            getParentModel().renderToBuffer(pose, buffers.getBuffer(renderType), layer.fullBright() ? LightTexture.FULL_BRIGHT : light,
                    OverlayTexture.NO_OVERLAY, FastColor.ARGB32.color(Math.round(layer.alpha() * 255), 255, 255, 255));
        }
    }
}
