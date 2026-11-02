package de.laugenbreze.dungeonsalive.client.entity.reference;

import com.mojang.blaze3d.vertex.PoseStack;
import de.laugenbreze.dungeonsalive.client.entity.reference.imported.GoetyTropicalSlimeGeometry;
import de.laugenbreze.dungeonsalive.entity.DungeonMob;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;

/** Own renderer preserving Goety's inner/fish/transparent-shell composition and fish frame sequence. */
public final class TropicalSlimeRenderer extends ReferenceMobRenderer<DungeonMob> {
    public static final ResourceLocation TEXTURE = resource("tropical_slime.png");
    private static ResourceLocation resource(String name) {
        return ResourceLocation.fromNamespaceAndPath("dungeonsalive", "textures/entity/reference/goety/tropical_slime/" + name);
    }
    public TropicalSlimeRenderer(EntityRendererProvider.Context context, ReferenceMobDefinition definition) {
        super(context, definition);
        addLayer(new FishAndShellLayer(this));
        float size = .5F * definition.scale();
        shadowRadius = size * .25F;
        ReferenceModels.registerCompositeBounds(definition.id(), new VisualBounds(size, size, size, size * .5F));
    }

    private static final class FishAndShellLayer extends RenderLayer<DungeonMob, ReferenceMobModel<DungeonMob>> {
        private final ModelPart fish = GoetyTropicalSlimeGeometry.createBodyLayer().bakeRoot();
        private final ModelPart shell = GoetyTropicalSlimeGeometry.createOuterBodyLayer().bakeRoot();
        private final ModelPoseCopy fishPose;
        private final ModelPoseCopy shellPose;
        private final ResourceLocation[] frames = new ResourceLocation[47];
        private FishAndShellLayer(RenderLayerParent<DungeonMob, ReferenceMobModel<DungeonMob>> parent) {
            super(parent);
            fishPose = new ModelPoseCopy(getParentModel().root(), fish, "tropical_slime/fish");
            shellPose = new ModelPoseCopy(getParentModel().root(), shell, "tropical_slime/shell");
            for (int i = 0; i < frames.length; i++) {
                frames[i] = resource("fishes/" + (i + 1) + ".png");
                if (Minecraft.getInstance().getResourceManager().getResource(frames[i]).isEmpty())
                    throw new IllegalArgumentException("Missing tropical fish frame " + frames[i]);
            }
        }
        @Override public void render(PoseStack pose, MultiBufferSource buffers, int light, DungeonMob mob,
                                     float limbSwing, float limbAmount, float partialTick, float age, float yaw, float pitch) {
            boolean outline = mob.isInvisible() && Minecraft.getInstance().shouldEntityAppearGlowing(mob);
            if (mob.isInvisible() && !outline) return;
            fishPose.copy();
            shellPose.copy();
            int slot = Math.floorMod((int)Math.floor(age), 48);
            // Upstream maps 0..46 to files 1..47. Counter 47 falls back to file 1,
            // followed by counter 0/file 1. These are animation frames, not skins.
            ResourceLocation texture = frames[slot == 47 ? 0 : slot];
            int overlay = LivingEntityRenderer.getOverlayCoords(mob, 0);
            if (!mob.isInvisible()) fish.render(pose, buffers.getBuffer(RenderType.entityCutoutNoCull(texture)), light, overlay, -1);
            shell.render(pose, buffers.getBuffer(outline ? RenderType.outline(TEXTURE) : RenderType.entityTranslucent(TEXTURE)), light, overlay, -1);
        }
    }
}
