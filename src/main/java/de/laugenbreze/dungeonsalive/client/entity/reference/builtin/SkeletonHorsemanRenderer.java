package de.laugenbreze.dungeonsalive.client.entity.reference.builtin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import de.laugenbreze.dungeonsalive.client.entity.MobRenderers;
import de.laugenbreze.dungeonsalive.entity.DungeonMob;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemDisplayContext;

/** Composite renderer; it never creates a second fake entity or borrows a second entity's AI. */
public final class SkeletonHorsemanRenderer extends MobRenderer<DungeonMob, SkeletonHorsemanModel> {
    private static final ResourceLocation HORSE = ResourceLocation.withDefaultNamespace("textures/entity/horse/horse_skeleton.png");
    private static final ResourceLocation SKELETON = ResourceLocation.withDefaultNamespace("textures/entity/skeleton/skeleton.png");

    public SkeletonHorsemanRenderer(EntityRendererProvider.Context context) {
        super(context, new SkeletonHorsemanModel(context.bakeLayer(MobRenderers.layer("skeleton_horseman")), context.bakeLayer(ModelLayers.SKELETON)), .8F);
        addLayer(new RiderLayer(this, context.getItemInHandRenderer()));
    }

    @Override public ResourceLocation getTextureLocation(DungeonMob mob) { return HORSE; }

    private static final class RiderLayer extends RenderLayer<DungeonMob, SkeletonHorsemanModel> {
        private final ItemInHandRenderer items;
        private RiderLayer(SkeletonHorsemanRenderer renderer, ItemInHandRenderer items) { super(renderer); this.items = items; }
        @Override public void render(PoseStack pose, MultiBufferSource buffers, int light, DungeonMob mob,
                                     float swing, float amount, float partialTick, float age, float yaw, float pitch) {
            if (mob.isInvisible()) return;
            pose.pushPose();
            getParentModel().translateToRider(pose);
            getParentModel().rider().renderToBuffer(pose, buffers.getBuffer(RenderType.entityCutoutNoCull(SKELETON)),
                    light, LivingEntityRenderer.getOverlayCoords(mob, 0), -1);
            var bow = mob.getMainHandItem();
            if (!bow.isEmpty()) {
                pose.pushPose();
                boolean left = mob.getMainArm() == HumanoidArm.LEFT;
                getParentModel().rider().translateToHand(mob.getMainArm(), pose);
                pose.mulPose(Axis.XP.rotationDegrees(-90)); pose.mulPose(Axis.YP.rotationDegrees(180));
                pose.translate((left ? -1 : 1) / 16F, .125F, -.625F);
                items.renderItem(mob, bow, left ? ItemDisplayContext.THIRD_PERSON_LEFT_HAND : ItemDisplayContext.THIRD_PERSON_RIGHT_HAND,
                        left, pose, buffers, light);
                pose.popPose();
            }
            pose.popPose();
        }
    }
}
