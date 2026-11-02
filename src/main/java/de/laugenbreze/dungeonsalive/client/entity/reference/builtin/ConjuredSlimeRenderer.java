package de.laugenbreze.dungeonsalive.client.entity.reference.builtin;

import com.mojang.blaze3d.vertex.PoseStack;
import de.laugenbreze.dungeonsalive.entity.AttackKind;
import de.laugenbreze.dungeonsalive.entity.DungeonMob;
import net.minecraft.client.model.SlimeModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.SlimeOuterLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

/** The audited MIT texture on Minecraft's actual inner/transparent-outer slime composition. */
public final class ConjuredSlimeRenderer extends MobRenderer<DungeonMob, SlimeModel<DungeonMob>> {
    public static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath("dungeonsalive",
            "textures/entity/reference/dungeons_mobs_port/conjured_slime.png");
    public static final float SIZE = 1.3F;

    public ConjuredSlimeRenderer(EntityRendererProvider.Context context) {
        super(context, new SlimeModel<>(context.bakeLayer(ModelLayers.SLIME)), .25F * SIZE);
        addLayer(new SlimeOuterLayer<>(this, context.getModelSet()));
    }

    @Override public ResourceLocation getTextureLocation(DungeonMob mob) { return TEXTURE; }

    @Override protected void scale(DungeonMob mob, PoseStack pose, float partialTick) {
        float moving = mob.walkAnimation.speed(partialTick);
        float squash = mob.onGround()
                ? Mth.sin(mob.walkAnimation.position(partialTick) * 1.5F) * .035F * moving
                : Mth.clamp((float)mob.getDeltaMovement().y * .55F, -.12F, .22F);
        AttackKind action = mob.activeAttack();
        if (action == AttackKind.LEAP || action == AttackKind.MELEE) {
            float elapsed = mob.animationTick(partialTick);
            float before = Mth.clamp(elapsed / action.windup(), 0, 1);
            float after = Mth.clamp((elapsed - action.windup()) / Math.max(1, action.recovery()), 0, 1);
            if (action == AttackKind.LEAP) {
                // Compression precedes takeoff; the tall release coincides with server LEAP.
                float anticipation = before < .8F ? -.22F * smooth(before / .8F)
                        : Mth.lerp(smooth((before - .8F) / .2F), -.22F, .25F);
                squash += anticipation * (1 - smooth(after));
            } else squash -= .16F * smooth(before) * (1 - smooth(after));
        }
        if (mob.hurtTime > 0) squash -= Mth.sin(Mth.clamp((mob.hurtTime - partialTick) / 10F, 0, 1) * Mth.PI) * .08F;
        float stretch = 1 + Mth.clamp(squash, -.3F, .4F);
        // Same reciprocal horizontal/vertical deformation as Vanilla SlimeRenderer. Applied before
        // the standard feet translation, it preserves ground contact and affects both materials.
        pose.scale(SIZE / stretch, SIZE * stretch, SIZE / stretch);
    }

    private static float smooth(float value) { return value * value * (3 - 2 * value); }
}
