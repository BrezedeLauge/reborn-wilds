package de.laugenbreze.dungeonsalive.client;

import de.laugenbreze.dungeonsalive.DungeonsAlive;
import de.laugenbreze.dungeonsalive.network.ClientGearHudBridge;
import de.laugenbreze.dungeonsalive.network.GearHudPayload;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;

/** Compact owned-resource display; hidden with F1, menus, death and spectator mode. */
public final class GearHud {
    private static GearHudPayload state;
    private GearHud() {}
    public static void register(IEventBus bus) {
        ClientGearHudBridge.install(payload -> state = payload);
        bus.addListener(GearHud::layers);
    }
    public static void clear() { state = null; }
    private static void layers(RegisterGuiLayersEvent event) {
        event.registerAboveAll(ResourceLocation.fromNamespaceAndPath(DungeonsAlive.MOD_ID, "gear_resources"),
                (graphics, delta) -> render(graphics));
    }
    private static void render(GuiGraphics graphics) {
        Minecraft client = Minecraft.getInstance();
        if (state == null || client.player == null || client.level == null || client.screen != null || client.options.hideGui
                || !client.player.isAlive() || client.player.isSpectator() || (!state.showSouls() && !state.showFlames())) return;
        int width = client.getWindow().getGuiScaledWidth(), height = client.getWindow().getGuiScaledHeight();
        // Away from hearts/armor/hotbar, boss bars and the right-hand potion effects.
        int x = 8, y = Math.max(8, height - 72);
        int panelWidth = Math.min(112, Math.max(72, width / 3));
        int panelHeight = state.showSouls() && state.showFlames() ? 32 : 20;
        DungeonsTheme.inset(graphics, x - 3, y - 3, panelWidth + 6, panelHeight + 3);
        graphics.fill(x - 4, y - 3, x - 2, y + panelHeight, DungeonsTheme.GOLD);
        if (state.showSouls()) {
            graphics.drawString(client.font, Component.translatable("hud.dungeonsalive.souls", state.souls(), 100), x, y, 0xFF9BEFE4, true);
            graphics.fill(x, y + 11, x + panelWidth, y + 14, 0xFF31453F);
            graphics.fill(x, y + 11, x + panelWidth * state.souls() / 100, y + 14, 0xFF73D7CA);
            y += 18;
        }
        if (state.showFlames()) graphics.drawString(client.font,
                Component.translatable("hud.dungeonsalive.flames", state.flameCharges(), 7), x, y, 0xFFFFC078, true);
    }
}
