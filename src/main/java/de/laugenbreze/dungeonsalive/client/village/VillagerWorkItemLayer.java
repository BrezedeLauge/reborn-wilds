package de.laugenbreze.dungeonsalive.client.village;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import de.laugenbreze.dungeonsalive.village.ProfessionGesture;
import de.laugenbreze.dungeonsalive.village.VillageActivity;
import de.laugenbreze.dungeonsalive.village.VillageAnimationConfig;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.EnumMap;
import java.util.Map;

/** Render-only tools. Never inserted into inventory, saved, dropped or used to generate resources. */
public final class VillagerWorkItemLayer extends RenderLayer<Villager, AnimatedVillagerModel> {
    private final ItemInHandRenderer items;
    private final Map<ProfessionGesture, ItemStack> props = new EnumMap<>(ProfessionGesture.class);
    public VillagerWorkItemLayer(RenderLayerParent<Villager, AnimatedVillagerModel> parent, ItemInHandRenderer items) {
        super(parent);
        this.items = items;
        prop(ProfessionGesture.ARMORER, Items.IRON_INGOT);
        prop(ProfessionGesture.TOOLSMITH, Items.IRON_PICKAXE);
        prop(ProfessionGesture.WEAPONSMITH, Items.IRON_SWORD);
        prop(ProfessionGesture.BLACKSMITH, Items.IRON_AXE);
        prop(ProfessionGesture.BUTCHER, Items.IRON_AXE);
        prop(ProfessionGesture.CARTOGRAPHER, Items.FEATHER);
        prop(ProfessionGesture.CLERIC, Items.GLASS_BOTTLE);
        prop(ProfessionGesture.FARMER, Items.WHEAT);
        prop(ProfessionGesture.FISHERMAN, Items.FISHING_ROD);
        prop(ProfessionGesture.FLETCHER, Items.ARROW);
        prop(ProfessionGesture.LEATHERWORKER, Items.LEATHER);
        prop(ProfessionGesture.LIBRARIAN, Items.BOOK);
        prop(ProfessionGesture.MASON, Items.IRON_PICKAXE);
        prop(ProfessionGesture.SHEPHERD, Items.SHEARS);
        prop(ProfessionGesture.VILLAGE_MERCHANT, Items.EMERALD);
        prop(ProfessionGesture.LUXURY_MERCHANT, Items.DIAMOND);
        prop(ProfessionGesture.MYSTERY_MERCHANT, Items.AMETHYST_SHARD);
        prop(ProfessionGesture.GIFT_WRAPPER, Items.PAPER);
        prop(ProfessionGesture.ENCHANTSMITH, Items.ENCHANTED_BOOK);
    }
    private void prop(ProfessionGesture gesture, Item item) { props.put(gesture, new ItemStack(item)); }

    @Override public void render(PoseStack poses, MultiBufferSource buffers, int light, Villager villager,
                                 float limbSwing, float limbAmount, float partialTick, float ageInTicks, float headYaw, float headPitch) {
        if (villager.isInvisible() || villager.isSleeping()) return;
        var frame = getParentModel().animationFrame();
        ItemStack right = villager.getMainHandItem();
        ItemStack left = villager.getOffhandItem();
        if (right.isEmpty() && !villager.isBaby() && VillageAnimationConfig.WORK_PROPS.get() && frame != null
                && frame.mode() == VillageActivity.WORK && frame.weight() > 0.4F) {
            right = props.getOrDefault(frame.gesture(), ItemStack.EMPTY);
        }
        renderHand(poses, buffers, light, villager, right, HumanoidArm.RIGHT);
        renderHand(poses, buffers, light, villager, left, HumanoidArm.LEFT);
    }

    private void renderHand(PoseStack poses, MultiBufferSource buffers, int light, Villager villager, ItemStack stack, HumanoidArm arm) {
        if (stack.isEmpty()) return;
        poses.pushPose();
        getParentModel().translateToHand(arm, poses);
        poses.translate(0, 0.5, 0);
        poses.mulPose(Axis.XP.rotationDegrees(-90));
        poses.mulPose(Axis.YP.rotationDegrees(180));
        poses.translate(0, 0.03, -0.10);
        boolean left = arm == HumanoidArm.LEFT;
        items.renderItem(villager, stack, left ? ItemDisplayContext.THIRD_PERSON_LEFT_HAND : ItemDisplayContext.THIRD_PERSON_RIGHT_HAND,
                left, poses, buffers, light);
        poses.popPose();
    }
}
