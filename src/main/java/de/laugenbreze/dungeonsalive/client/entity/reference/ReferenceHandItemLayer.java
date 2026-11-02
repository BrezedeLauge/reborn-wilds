package de.laugenbreze.dungeonsalive.client.entity.reference;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

/** Renders real entity equipment at the source rig's hands, retaining all ancestor transforms. */
public final class ReferenceHandItemLayer<T extends Mob> extends RenderLayer<T, ReferenceMobModel<T>> {
    private final ItemInHandRenderer items;
    private final @Nullable ModelBonePath right, left;
    private final boolean palmAnchor;

    public ReferenceHandItemLayer(RenderLayerParent<T, ReferenceMobModel<T>> parent, ItemInHandRenderer items) {
        super(parent);
        this.items = items;
        var binding = getParentModel().definition().handItems();
        if (binding == null) throw new IllegalArgumentException("Held-item layer without a rig binding");
        String label = getParentModel().definition().id() + " held items";
        right = binding.right() == null ? null : new ModelBonePath(getParentModel().root(), binding.right(), label);
        left = binding.left() == null ? null : new ModelBonePath(getParentModel().root(), binding.left(), label);
        palmAnchor = binding.palmAnchor();
    }

    @Override public void render(PoseStack pose, MultiBufferSource buffers, int light, T mob,
                                 float limbSwing, float limbAmount, float partialTick, float age, float yaw, float pitch) {
        if (mob.isInvisible()) return;
        boolean mainRight = mob.getMainArm() == HumanoidArm.RIGHT;
        renderHand(pose, buffers, light, mob, mainRight ? mob.getMainHandItem() : mob.getOffhandItem(), false, right);
        renderHand(pose, buffers, light, mob, mainRight ? mob.getOffhandItem() : mob.getMainHandItem(), true, left);
    }

    private void renderHand(PoseStack pose, MultiBufferSource buffers, int light, T mob,
                            ItemStack stack, boolean leftHand, @Nullable ModelBonePath path) {
        if (stack.isEmpty() || path == null || !path.visible()) return;
        pose.pushPose();
        path.transform(pose);
        pose.mulPose(Axis.XP.rotationDegrees(-90));
        pose.mulPose(Axis.YP.rotationDegrees(180));
        // Vanilla shoulder anchors include the 10px forearm reach. A source palm bone has already
        // performed that translation and must not receive it a second time.
        pose.translate((leftHand ? -1 : 1) / 16F, .125F, palmAnchor ? 0 : -.625F);
        items.renderItem(mob, stack, leftHand ? ItemDisplayContext.THIRD_PERSON_LEFT_HAND : ItemDisplayContext.THIRD_PERSON_RIGHT_HAND,
                leftHand, pose, buffers, light);
        pose.popPose();
    }
}
