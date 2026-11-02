package de.laugenbreze.dungeonsalive.client.village;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.CustomHeadLayer;
import net.minecraft.client.renderer.entity.layers.VillagerProfessionLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.npc.Villager;

/** Keeps vanilla biome clothing, hats, badges, baby scaling and head equipment. */
public final class AnimatedVillagerRenderer extends MobRenderer<Villager, AnimatedVillagerModel> {
    private static final ResourceLocation SKIN = ResourceLocation.withDefaultNamespace("textures/entity/villager/villager.png");
    public AnimatedVillagerRenderer(EntityRendererProvider.Context context) {
        super(context, new AnimatedVillagerModel(context.bakeLayer(VillageClient.LAYER)), 0.5F);
        addLayer(new CustomHeadLayer<>(this, context.getModelSet(), context.getItemInHandRenderer()));
        addLayer(new VillagerProfessionLayer<>(this, context.getResourceManager(), "villager"));
        addLayer(new VillagerWorkItemLayer(this, context.getItemInHandRenderer()));
    }
    @Override public ResourceLocation getTextureLocation(Villager villager) { return SKIN; }
    @Override protected void scale(Villager villager, PoseStack poses, float partialTick) {
        float scale = 0.9375F * villager.getAgeScale();
        poses.scale(scale, scale, scale);
    }
    @Override protected float getShadowRadius(Villager villager) {
        return super.getShadowRadius(villager) * (villager.isBaby() ? 0.5F : 1);
    }
}
