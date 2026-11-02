package de.laugenbreze.dungeonsalive.client;

import com.google.gson.Gson;
import com.mojang.blaze3d.platform.InputConstants;
import de.laugenbreze.dungeonsalive.DungeonsAlive;
import de.laugenbreze.dungeonsalive.client.ambient.AmbientClient;
import de.laugenbreze.dungeonsalive.client.entity.MobRenderers;
import de.laugenbreze.dungeonsalive.client.village.VillageClient;
import de.laugenbreze.dungeonsalive.client.gear.ArmorClient;
import de.laugenbreze.dungeonsalive.network.ClientJournalBridge;
import de.laugenbreze.dungeonsalive.network.JournalPayload;
import de.laugenbreze.dungeonsalive.network.JournalView;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.common.NeoForge;
import org.lwjgl.glfw.GLFW;

@Mod(value = DungeonsAlive.MOD_ID, dist = Dist.CLIENT)
public final class DungeonsAliveClient {
    private static final Gson GSON = new Gson();
    private static final KeyMapping JOURNAL_KEY = new KeyMapping("key.dungeonsalive.journal",
            InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_J, "key.categories.dungeonsalive");
    private static final KeyMapping GUIDE_KEY = new KeyMapping("key.dungeonsalive.guide",
            InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_B, "key.categories.dungeonsalive");

    public DungeonsAliveClient(IEventBus bus, ModContainer container) {
        AmbientClient.register(bus);
        MobRenderers.register(bus);
        bus.addListener(de.laugenbreze.dungeonsalive.client.entity.ConstructRenderers::registerRenderers);
        de.laugenbreze.dungeonsalive.client.entity.PeacefulRenderers.register(bus);
        VillageClient.register(bus);
        ArmorClient.register(bus);
        GearHud.register(bus);
        LivingWorldBlockColors.register(bus);
        de.laugenbreze.dungeonsalive.client.inn.InnFurnitureClient.register(bus);
        bus.addListener(DungeonsAliveClient::setupGear);
        bus.addListener(DungeonsAliveClient::serviceScreens);
        NeoForge.EVENT_BUS.addListener(GearUpgradeClient::tooltip);
        de.laugenbreze.dungeonsalive.client.dev.RenderSmokeCapture.register();
        de.laugenbreze.dungeonsalive.client.dev.BossShowcaseCapture.register();
        de.laugenbreze.dungeonsalive.client.dev.MobModelShowcaseCapture.register();
        de.laugenbreze.dungeonsalive.client.dev.FeatureShowcaseCapture.register();
        de.laugenbreze.dungeonsalive.client.dev.CampShowcaseCapture.register();
        de.laugenbreze.dungeonsalive.client.dev.LivingWorldShowcaseCapture.register();
        de.laugenbreze.dungeonsalive.client.dev.NativeNatureShowcaseCapture.register();
        de.laugenbreze.dungeonsalive.client.dev.InnShowcaseCapture.register();
        bus.addListener(DungeonsAliveClient::keys);
        NeoForge.EVENT_BUS.addListener(DungeonsAliveClient::tick);
        NeoForge.EVENT_BUS.addListener(DungeonsAliveClient::logout);
        ClientJournalBridge.install(DungeonsAliveClient::receive);
        de.laugenbreze.dungeonsalive.network.ClientCurrentBridge.install(payload -> {
            var client = Minecraft.getInstance();
            if (client.level == null || client.player == null) return;
            var entity = client.level.getEntity(payload.entityId());
            if (entity == null || (!entity.isInWater() && !(entity instanceof net.minecraft.world.entity.vehicle.Boat))) return;
            if (entity == client.player && !client.player.isSpectator() && !client.player.getAbilities().flying
                    || entity.getControllingPassenger() == client.player) {
                entity.setDeltaMovement(entity.getDeltaMovement().add(payload.x(), 0, payload.z()));
            }
        });
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    private static void setupGear(net.neoforged.fml.event.lifecycle.FMLClientSetupEvent event) {
        event.enqueueWork(RangedItemProperties::register);
    }
    private static void serviceScreens(net.neoforged.neoforge.client.event.RegisterMenuScreensEvent event) {
        event.register(de.laugenbreze.dungeonsalive.merchant.MerchantServices.MENU.get(), MerchantServiceScreen::new);
        event.register(de.laugenbreze.dungeonsalive.settlement.inn.InnContent.MENU.get(), InnScreen::new);
    }

    private static void keys(RegisterKeyMappingsEvent event) {
        event.register(JOURNAL_KEY);
        event.register(GUIDE_KEY);
    }

    private static void tick(ClientTickEvent.Post event) {
        while (GUIDE_KEY.consumeClick()) {
            Minecraft client = Minecraft.getInstance();
            if (client.player != null && client.level != null && client.screen == null) {
                client.setScreen(new FieldGuideScreen());
            }
        }
        while (JOURNAL_KEY.consumeClick()) {
            Minecraft client = Minecraft.getInstance();
            if (client.player != null && client.getConnection() != null && client.screen == null) {
                client.getConnection().sendCommand("dungeonsalive journal");
            }
        }
    }

    private static void logout(ClientPlayerNetworkEvent.LoggingOut event) {
        GearHud.clear();
        if (Minecraft.getInstance().screen instanceof JournalScreen || Minecraft.getInstance().screen instanceof FieldGuideScreen
                || Minecraft.getInstance().screen instanceof GearGuideScreen)
            Minecraft.getInstance().setScreen(null);
    }

    private static void receive(JournalPayload payload) {
        Minecraft client = Minecraft.getInstance();
        try {
            JournalView view = GSON.fromJson(payload.json(), JournalView.class);
            if (view == null || view.schemaVersion() != 1 || view.quests() == null || view.quests().size() > 128) {
                throw new IllegalArgumentException("Unsupported journal payload");
            }
            if (client.screen instanceof JournalScreen screen) {
                screen.update(view);
            } else if (payload.open() && client.level != null) {
                client.setScreen(new JournalScreen(view));
            }
        } catch (RuntimeException exception) {
            DungeonsAlive.LOGGER.error("Could not display the quest journal", exception);
        }
    }
}
