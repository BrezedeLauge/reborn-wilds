package de.laugenbreze.dungeonsalive.client.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import de.laugenbreze.dungeonsalive.entity.DungeonMob;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;

/** Dedicated renderer preserves the broad anatomy with uniform scaling and its own light mask. */
public final class RedstoneMonstrosityRenderer extends MobRenderer<DungeonMob, RedstoneMonstrosityModel> {
    public static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath("dungeonsalive", "textures/entity/redstone_monstrosity/stone.png");
    public static final ResourceLocation GLOW = ResourceLocation.fromNamespaceAndPath("dungeonsalive", "textures/entity/redstone_monstrosity/glow.png");

    public RedstoneMonstrosityRenderer(EntityRendererProvider.Context context) {
        super(context, new RedstoneMonstrosityModel(context.bakeLayer(MobRenderers.layer("redstone_monstrosity"))), 1.7F);
        addLayer(new FurnaceGlow(this));
    }
    @Override public ResourceLocation getTextureLocation(DungeonMob mob) { return TEXTURE; }
    @Override protected void scale(DungeonMob mob, PoseStack pose, float partialTick) {
        float scale = mob.profile().height() / RedstoneMonstrosityModel.NATIVE_HEIGHT;
        pose.scale(scale, scale, scale);
    }

    private static final class FurnaceGlow extends RenderLayer<DungeonMob, RedstoneMonstrosityModel> {
        FurnaceGlow(RenderLayerParent<DungeonMob, RedstoneMonstrosityModel> parent) { super(parent); }
        @Override public void render(PoseStack pose, MultiBufferSource buffers, int light, DungeonMob mob,
                                     float limbSwing, float limbAmount, float partial, float age, float yaw, float pitch) {
            if (mob.isInvisible()) return;
            int intensity = (int)(255 * RedstoneMonstrosityModel.glowStrength(mob, age));
            // The 1.21.1 translucent-emissive shader still mixes directional light.
            // Vanilla's eyes pass is genuinely unlit; its additive blend needs RGB modulation.
            getParentModel().renderToBuffer(pose, buffers.getBuffer(RenderType.eyes(GLOW)),
                    LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, FastColor.ARGB32.color(255, intensity, intensity, intensity));
        }
    }
}
