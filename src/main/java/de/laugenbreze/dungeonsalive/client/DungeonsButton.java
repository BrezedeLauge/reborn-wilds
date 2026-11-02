package de.laugenbreze.dungeonsalive.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;

/** Retains vanilla press, focus and narration; long labels stay still and expose their full tooltip. */
final class DungeonsButton extends Button {
    private Tooltip automaticTooltip;
    private String automaticLabel;
    DungeonsButton(Button.Builder builder) { super(builder); }
    @Override protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        int x = getX(), y = getY(), right = x + getWidth(), bottom = y + getHeight();
        boolean focus = isHoveredOrFocused();
        int edge = !active ? 0xFF42434A : focus ? DungeonsTheme.GOLD : 0xFF76717C;
        graphics.fill(x, y, right, bottom, 0xFF101219);
        graphics.fill(x + 1, y + 1, right - 1, bottom - 2, edge);
        graphics.fillGradient(x + 2, y + 2, right - 2, bottom - 3,
                !active ? 0xFF303039 : focus ? 0xFF64523C : 0xFF41434F,
                !active ? 0xFF262630 : focus ? 0xFF443829 : 0xFF292D38);
        graphics.fill(x + 2, bottom - 2, right - 2, bottom - 1, 0xFF080B10);
        if (focus && active) graphics.fill(x + 3, y + 4, x + 5, bottom - 5, DungeonsTheme.GOLD);
        var font = Minecraft.getInstance().font;
        int color = !active ? 0xFF85838C : focus ? 0xFFFFE8AE : DungeonsTheme.TEXT;
        int available = Math.max(0, getWidth() - 14);
        if (font.width(getMessage()) > available) {
            String full = getMessage().getString();
            if (getTooltip() == null || getTooltip() == automaticTooltip) {
                if (automaticTooltip == null || !full.equals(automaticLabel)) {
                    automaticTooltip = Tooltip.create(getMessage()); automaticLabel = full;
                }
                setTooltip(automaticTooltip);
            }
            Component label = Component.literal(font.plainSubstrByWidth(full,
                    Math.max(0, available - font.width("…"))) + "…").withStyle(getMessage().getStyle());
            graphics.drawString(font, label, x + 7, y + (getHeight() - font.lineHeight) / 2, color);
        } else {
            if (getTooltip() == automaticTooltip) setTooltip(null);
            automaticTooltip = null; automaticLabel = null;
            graphics.drawCenteredString(font, getMessage(), x + getWidth() / 2,
                    y + (getHeight() - font.lineHeight) / 2, color);
        }
    }
}
