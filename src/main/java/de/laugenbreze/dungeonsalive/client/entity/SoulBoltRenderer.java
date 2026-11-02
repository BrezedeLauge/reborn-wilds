package de.laugenbreze.dungeonsalive.client.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import de.laugenbreze.dungeonsalive.entity.SoulBolt;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public final class SoulBoltRenderer extends EntityRenderer<SoulBolt> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath("dungeonsalive", "textures/entity/soul_bolt.png");
    public SoulBoltRenderer(EntityRendererProvider.Context context) { super(context); }
    @Override public ResourceLocation getTextureLocation(SoulBolt bolt) { return TEXTURE; }
    @Override public void render(SoulBolt bolt, float yaw, float partial, PoseStack pose, MultiBufferSource buffers, int light) {
        pose.pushPose();
        pose.mulPose(entityRenderDispatcher.cameraOrientation());
        float pulse = .3f + Mth.sin((bolt.tickCount + partial) * .5f) * .025f;
        pose.scale(pulse, pulse, pulse);
        var vertices = buffers.getBuffer(RenderType.entityTranslucentEmissive(TEXTURE));
        var frame = pose.last();
        vertices.addVertex(frame, -.5f, -.5f, 0).setColor(255, 255, 255, 240).setUv(0, 1).setOverlay(OverlayTexture.NO_OVERLAY).setLight(15728880).setNormal(frame, 0, 0, 1);
        vertices.addVertex(frame, .5f, -.5f, 0).setColor(255, 255, 255, 240).setUv(1, 1).setOverlay(OverlayTexture.NO_OVERLAY).setLight(15728880).setNormal(frame, 0, 0, 1);
        vertices.addVertex(frame, .5f, .5f, 0).setColor(255, 255, 255, 240).setUv(1, 0).setOverlay(OverlayTexture.NO_OVERLAY).setLight(15728880).setNormal(frame, 0, 0, 1);
        vertices.addVertex(frame, -.5f, .5f, 0).setColor(255, 255, 255, 240).setUv(0, 0).setOverlay(OverlayTexture.NO_OVERLAY).setLight(15728880).setNormal(frame, 0, 0, 1);
        pose.popPose();
        super.render(bolt, yaw, partial, pose, buffers, light);
    }
}
