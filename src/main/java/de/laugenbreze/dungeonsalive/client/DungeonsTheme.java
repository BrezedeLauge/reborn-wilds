package de.laugenbreze.dungeonsalive.client;

import net.minecraft.client.gui.GuiGraphics;

/** Own pixel geometry: carved dark stone, brass corners and restrained soul-blue accents. */
final class DungeonsTheme {
    static final int GOLD = 0xFFD7B46A, TEXT = 0xFFF1E6CD, MUTED = 0xFFA8ACB3;
    static final int STONE = 0xFF24252E, INSET = 0xFF14171F, EDGE = 0xFF4B4954, SOUL = 0xFF73D7CA;
    static void panel(GuiGraphics g, int x, int y, int width, int height) {
        g.fill(x + 3, y + 4, x + width + 5, y + height + 6, 0xA0000000);
        g.fill(x - 2, y - 2, x + width + 2, y + height + 2, 0xFF111218);
        g.fill(x - 1, y - 1, x + width + 1, y + height + 1, EDGE);
        g.fillGradient(x, y, x + width, y + height, 0xFF31313B, STONE);
        g.fill(x + 7, y + 32, x + width - 7, y + 33, 0xFF766246);
        for (int side = 0; side < 2; side++) for (int bottom = 0; bottom < 2; bottom++) {
            int cx = x + (side == 0 ? 2 : width - 10), cy = y + (bottom == 0 ? 2 : height - 10);
            g.fill(cx, cy, cx + 8, cy + 2, GOLD);
            g.fill(cx, cy, cx + 2, cy + 8, GOLD);
            g.fill(cx + 3, cy + 3, cx + 5, cy + 5, 0xFF9F7E45);
        }
    }
    static void inset(GuiGraphics g, int x, int y, int width, int height) {
        g.fill(x - 1, y - 1, x + width + 1, y + height + 1, 0xFF0D1016);
        g.fillGradient(x, y, x + width, y + height, INSET, 0xFF202430);
        g.fill(x, y + height, x + width, y + height + 1, EDGE);
    }
    static void slot(GuiGraphics g, int x, int y) {
        g.fill(x - 1, y - 1, x + 17, y + 17, 0xFF0B0E14);
        g.fill(x, y, x + 16, y + 16, INSET);
        g.fill(x - 1, y + 17, x + 17, y + 18, 0xFF65616A);
        g.fill(x + 17, y - 1, x + 18, y + 18, 0xFF65616A);
    }
    private DungeonsTheme() {}
}
