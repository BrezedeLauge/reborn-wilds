package de.laugenbreze.dungeonsalive.client.entity.reference.builtin;

import com.mojang.blaze3d.vertex.PoseStack;
import de.laugenbreze.dungeonsalive.client.entity.reference.ModelBonePath;
import de.laugenbreze.dungeonsalive.client.entity.reference.ReferenceMobDefinition;
import de.laugenbreze.dungeonsalive.client.entity.reference.ReferenceMobModel;
import de.laugenbreze.dungeonsalive.client.entity.reference.ReferenceMobRenderer;
import de.laugenbreze.dungeonsalive.entity.DungeonMob;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.layers.CustomHeadLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemDisplayContext;

/** The real illager rig, real equipped weapon and the actual equipped raid banner. */
public final class CaptainMobRenderer extends ReferenceMobRenderer<DungeonMob> {
    public CaptainMobRenderer(EntityRendererProvider.Context context, ReferenceMobDefinition definition) {
        super(context, definition);
        addLayer(new BannerLayer(this, context.getItemInHandRenderer()));
    }

    private static final class BannerLayer extends RenderLayer<DungeonMob, ReferenceMobModel<DungeonMob>> {
        private final ItemInHandRenderer items;
        private final ModelBonePath head;
        private BannerLayer(CaptainMobRenderer renderer, ItemInHandRenderer items) {
            super(renderer); this.items = items;
            head = new ModelBonePath(getParentModel().root(), "head", "captain banner");
        }
        @Override public void render(PoseStack pose, MultiBufferSource buffers, int light, DungeonMob mob,
                                     float swing, float amount, float partialTick, float age, float yaw, float pitch) {
            var banner = mob.getItemBySlot(EquipmentSlot.HEAD);
            if (mob.isInvisible() || banner.isEmpty() || !head.visible()) return;
            pose.pushPose();
            head.transform(pose);
            CustomHeadLayer.translateToHead(pose, false);
            items.renderItem(mob, banner, ItemDisplayContext.HEAD, false, pose, buffers, light);
            pose.popPose();
        }
    }
}
