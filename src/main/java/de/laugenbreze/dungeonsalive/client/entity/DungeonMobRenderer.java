package de.laugenbreze.dungeonsalive.client.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import de.laugenbreze.dungeonsalive.entity.DungeonMob;
import de.laugenbreze.dungeonsalive.entity.MobProfile;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public final class DungeonMobRenderer extends MobRenderer<DungeonMob, DungeonMobModel> {
    private final ResourceLocation texture;
    public DungeonMobRenderer(EntityRendererProvider.Context context, MobProfile profile) {
        super(context, new DungeonMobModel(context.bakeLayer(MobRenderers.layer(profile.id()))), profile.width() * .45f);
        texture = ResourceLocation.fromNamespaceAndPath("dungeonsalive", "textures/entity/" + profile.id() + ".png");
    }
    @Override public ResourceLocation getTextureLocation(DungeonMob mob) { return texture; }
    @Override protected void scale(DungeonMob mob, PoseStack pose, float partialTick) {
        MobProfile profile = mob.profile();
        float horizontal = profile.width() / DungeonMobModel.nativeWidth(profile);
        float vertical = profile.height() / DungeonMobModel.nativeHeight(profile);
        pose.scale(horizontal, vertical, horizontal);
    }
}
