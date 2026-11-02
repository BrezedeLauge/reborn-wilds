package de.laugenbreze.dungeonsalive.client.entity;

import de.laugenbreze.dungeonsalive.entity.MobProfiles;
import de.laugenbreze.dungeonsalive.entity.ModEntities;
import de.laugenbreze.dungeonsalive.client.entity.reference.ReferenceModels;
import de.laugenbreze.dungeonsalive.client.entity.reference.ReferenceMobRenderer;
import de.laugenbreze.dungeonsalive.client.entity.reference.imported.ReferenceModelIndex;
import de.laugenbreze.dungeonsalive.client.entity.reference.builtin.BuiltinMobModels;
import de.laugenbreze.dungeonsalive.client.entity.reference.builtin.CaptainMobRenderer;
import de.laugenbreze.dungeonsalive.client.entity.reference.builtin.SkeletonHorsemanRenderer;
import de.laugenbreze.dungeonsalive.client.entity.reference.builtin.ConjuredSlimeRenderer;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

import java.util.LinkedHashMap;

/** Called only from the Dist.CLIENT entry point. */
public final class MobRenderers {
    private MobRenderers() {}
    public static ModelLayerLocation layer(String id) {
        return new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath("dungeonsalive", id), "main");
    }
    public static void register(IEventBus bus) {
        var models = new LinkedHashMap<>(ReferenceModelIndex.create());
        var builtins = BuiltinMobModels.definitions();
        for (var entry : builtins.entrySet()) {
            if (models.putIfAbsent(entry.getKey(), entry.getValue()) != null)
                throw new IllegalArgumentException("Duplicate native composite model " + entry.getKey());
        }
        ReferenceModels.install(models);
        for (String id : builtins.keySet())
            ReferenceModels.registerCompositeBounds(id, BuiltinMobModels.compositeBounds(id));
        bus.addListener(MobRenderers::layers);
        bus.addListener(MobRenderers::renderers);
    }
    private static void layers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        for (var reference : ReferenceModels.all().values())
            event.registerLayerDefinition(layer(reference.id()), reference.layer());
        for (var profile : MobProfiles.all()) {
            var reference = ReferenceModels.get(profile.id());
            if (reference != null) continue;
            if (profile.id().equals("redstone_monstrosity")) event.registerLayerDefinition(layer(profile.id()), RedstoneMonstrosityGeometry::createLayer);
            else event.registerLayerDefinition(layer(profile.id()), () -> DungeonMobModel.create(profile));
        }
    }
    private static void renderers(EntityRenderersEvent.RegisterRenderers event) {
        for (var profile : MobProfiles.all()) {
            var reference = ReferenceModels.get(profile.id());
            if (profile.id().equals("skeleton_horseman")) event.registerEntityRenderer(ModEntities.type(profile.id()), SkeletonHorsemanRenderer::new);
            else if (profile.id().equals("conjured_slime")) event.registerEntityRenderer(ModEntities.type(profile.id()), ConjuredSlimeRenderer::new);
            else if (profile.id().equals("tropical_slime")) event.registerEntityRenderer(ModEntities.type(profile.id()), context -> new de.laugenbreze.dungeonsalive.client.entity.reference.TropicalSlimeRenderer(context, reference));
            else if (profile.id().equals("pillager_raid_captain") || profile.id().equals("vindicator_raid_captain"))
                event.registerEntityRenderer(ModEntities.type(profile.id()), context -> new CaptainMobRenderer(context, reference));
            else if (reference != null) event.registerEntityRenderer(ModEntities.type(profile.id()), context -> new ReferenceMobRenderer<>(context, reference));
            else if (profile.id().equals("redstone_monstrosity")) event.registerEntityRenderer(ModEntities.type(profile.id()), RedstoneMonstrosityRenderer::new);
            else event.registerEntityRenderer(ModEntities.type(profile.id()), context -> new DungeonMobRenderer(context, profile));
        }
        event.registerEntityRenderer(ModEntities.SOUL_BOLT.get(), SoulBoltRenderer::new);
    }
}
