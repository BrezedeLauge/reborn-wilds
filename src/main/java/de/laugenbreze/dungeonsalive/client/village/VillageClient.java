package de.laugenbreze.dungeonsalive.client.village;

import de.laugenbreze.dungeonsalive.DungeonsAlive;
import de.laugenbreze.dungeonsalive.village.ProfessionGesture;
import de.laugenbreze.dungeonsalive.village.VillageActivity;
import de.laugenbreze.dungeonsalive.village.VillageAnimationConfig;
import de.laugenbreze.dungeonsalive.village.VillageAnimationNetworking;
import de.laugenbreze.dungeonsalive.village.VillageAnimationPayload;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.npc.Villager;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.common.NeoForge;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.WeakHashMap;

public final class VillageClient {
    public static final ModelLayerLocation LAYER = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath(DungeonsAlive.MOD_ID, "animated_villager"), "main");
    private static final Map<UUID, TimedState> STATES = new HashMap<>();
    private static final Map<Villager, Boolean> PREVIEWS = new WeakHashMap<>();
    private static Object observedLevel;
    private static boolean rendererInstalled;

    private record TimedState(int mode, double startedAt, double endAt) {}
    public record AnimationFrame(int mode, float time, float weight, ProfessionGesture gesture) {}
    private VillageClient() {}

    public static void register(IEventBus bus) {
        bus.addListener(VillageClient::layers);
        bus.addListener(VillageClient::renderers);
        NeoForge.EVENT_BUS.addListener(VillageClient::tick);
        NeoForge.EVENT_BUS.addListener(VillageClient::logout);
        VillageAnimationNetworking.installClientReceiver(VillageClient::receive);
    }

    private static void layers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(LAYER, AnimatedVillagerModel::createLayer);
    }

    private static void renderers(EntityRenderersEvent.RegisterRenderers event) {
        boolean external = ModList.get().isLoaded("entity_model_features") || ModList.get().isLoaded("optifine");
        rendererInstalled = VillageAnimationConfig.ENABLED.get()
                && !(VillageAnimationConfig.PREFER_EXTERNAL_MODELS.get() && external);
        if (rendererInstalled) event.registerEntityRenderer(EntityType.VILLAGER, AnimatedVillagerRenderer::new);
        else DungeonsAlive.LOGGER.info("Reborn Wilds villager rendering disabled or delegated to external model mod");
    }

    public static boolean rendererInstalled() { return rendererInstalled; }

    private static void receive(VillageAnimationPayload payload) {
        var level = Minecraft.getInstance().level;
        if (level == null) return;
        if (observedLevel != level) {
            STATES.clear(); PREVIEWS.clear(); observedLevel = level;
        }
        var entity = level.getEntity(payload.entityId());
        // A spawn packet may still be pending. Store by UUID, never by a reusable integer ID.
        if (entity != null && !entity.getUUID().equals(payload.uuid())) return;
        long now = level.getGameTime();
        if (payload.mode() == VillageActivity.IDLE) {
            var old = STATES.get(payload.uuid());
            if (old != null) STATES.put(payload.uuid(), new TimedState(old.mode, old.startedAt, Math.min(old.endAt, now + 8)));
        } else if (payload.startedAt() <= now + 200 && payload.startedAt() + payload.duration() > now - 20) {
            var old = STATES.get(payload.uuid());
            double start = old != null && old.mode == payload.mode() && payload.mode() != VillageActivity.WORK
                    ? old.startedAt : payload.startedAt();
            if (STATES.size() >= 2048 && !STATES.containsKey(payload.uuid())) return;
            STATES.put(payload.uuid(), new TimedState(payload.mode(), start, payload.startedAt() + payload.duration()));
        }
    }

    public static AnimationFrame frame(Villager villager, float ageInTicks) {
        ProfessionGesture gesture = ProfessionGesture.forId(BuiltInRegistries.VILLAGER_PROFESSION
                .getKey(villager.getVillagerData().getProfession()).toString());
        Boolean preview = PREVIEWS.get(villager);
        if (preview != null) return new AnimationFrame(preview ? VillageActivity.WORK : VillageActivity.IDLE,
                ageInTicks, preview ? 1 : 0, gesture);
        if (villager.isSleeping() || villager.isBaby() || !villager.isAlive()) {
            return new AnimationFrame(VillageActivity.IDLE, ageInTicks, 0, gesture);
        }
        TimedState state = STATES.get(villager.getUUID());
        if (state == null) return new AnimationFrame(VillageActivity.IDLE, ageInTicks, 0, gesture);
        double now = villager.level().getGameTime() + Mth.clamp(ageInTicks - villager.tickCount, 0, 1);
        float entrance = Mth.clamp((float) (now - state.startedAt) / 8, 0, 1);
        float exit = Mth.clamp((float) (state.endAt - now) / 8, 0, 1);
        float weight = smooth(Math.min(entrance, exit));
        return new AnimationFrame(state.mode, (float) (now - state.startedAt), weight, gesture);
    }

    /** Explicit visual-demo flag only; never use this for entities that belong to a live world. */
    public static void preview(Villager villager, boolean work) { PREVIEWS.put(villager, work); }
    public static void clearPreview(Villager villager) { PREVIEWS.remove(villager); }

    private static float smooth(float f) { return f * f * (3 - 2 * f); }
    private static void tick(ClientTickEvent.Post event) {
        var level = Minecraft.getInstance().level;
        if (level != observedLevel) {
            STATES.clear(); PREVIEWS.clear(); observedLevel = level;
        } else if (level != null && level.getGameTime() % 100 == 0) {
            STATES.values().removeIf(state -> state.endAt < level.getGameTime() - 20);
        }
    }
    private static void logout(ClientPlayerNetworkEvent.LoggingOut event) {
        STATES.clear(); PREVIEWS.clear(); observedLevel = null;
    }
}
