package de.laugenbreze.dungeonsalive.client.entity;

import de.laugenbreze.dungeonsalive.client.entity.reference.ReferenceAnimationSource;
import de.laugenbreze.dungeonsalive.client.entity.reference.ReferenceMobRenderer;
import de.laugenbreze.dungeonsalive.client.entity.reference.ReferenceModels;
import de.laugenbreze.dungeonsalive.entity.AttackKind;
import de.laugenbreze.dungeonsalive.entity.construct.ConstructKind;
import de.laugenbreze.dungeonsalive.entity.construct.DungeonConstruct;
import de.laugenbreze.dungeonsalive.entity.construct.ModConstructs;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ThrownTridentRenderer;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

/** Existing reference layers are registered by MobRenderers; this hook adds only actual entity renderers. */
public final class ConstructRenderers {
    private ConstructRenderers() {}
    private static final ReferenceAnimationSource<DungeonConstruct> SOURCE = new ReferenceAnimationSource<>() {
        @Override public AttackKind action(DungeonConstruct entity) { return null; }
        @Override public float actionTicks(DungeonConstruct entity, float partialTick) { return 0; }
        @Override public Gesture gesture(DungeonConstruct entity, float partialTick) {
            String clip = entity.visualClip();
            return clip == null ? null : new Gesture(clip, entity.phaseTick(partialTick), entity.phaseDurationTicks(), 0, 0);
        }
    };
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        for (ConstructKind kind : ConstructKind.values()) {
            event.registerEntityRenderer(ModConstructs.type(kind), context ->
                    new ReferenceMobRenderer<DungeonConstruct>(context, ReferenceModels.get(kind.id()), SOURCE) {
                        @Override protected float getFlipDegrees(DungeonConstruct entity) { return 0; }
                        @Override protected RenderType getRenderType(DungeonConstruct entity, boolean bodyVisible, boolean translucent, boolean glowing) {
                            // The licensed IceCloud atlas includes alpha-64 formation glows and a
                            // translucent ring. Its source renderer uses this same material pass.
                            if (kind == ConstructKind.ICE_CHUNK && bodyVisible)
                                return RenderType.entityTranslucent(getTextureLocation(entity));
                            return super.getRenderType(entity, bodyVisible, translucent, glowing);
                        }
                    });
        }
        event.registerEntityRenderer(ModConstructs.TRIDENT.get(), ThrownTridentRenderer::new);
    }
}
