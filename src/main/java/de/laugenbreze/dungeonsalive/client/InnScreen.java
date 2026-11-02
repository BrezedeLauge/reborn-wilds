package de.laugenbreze.dungeonsalive.client;

import de.laugenbreze.dungeonsalive.settlement.inn.InnMenu;
import de.laugenbreze.dungeonsalive.settlement.inn.InnService;
import java.util.Locale;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.Nullable;

/** Server-observed room readiness and an explicit confirmation of the five marked positions. */
public final class InnScreen extends AbstractContainerScreen<InnMenu> {
    private Button save;
    public InnScreen(InnMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title); imageWidth = 310; imageHeight = 230;
    }
    @Override protected void init() {
        super.init();
        save = addRenderableWidget(Button.builder(Component.translatable("screen.dungeonsalive.inn.save"), ignored -> action(0))
                .bounds(leftPos + 10, topPos + 202, 140, 20).build(DungeonsButton::new));
        addRenderableWidget(Button.builder(Component.translatable("screen.dungeonsalive.inn.reset"), ignored -> action(1))
                .bounds(leftPos + 158, topPos + 202, 142, 20).build(DungeonsButton::new));
        save.active = menu.canCommit();
    }
    private void action(int action) {
        if (minecraft != null && minecraft.gameMode != null) {
            save.active = false; minecraft.gameMode.handleInventoryButtonClick(menu.containerId, action);
        }
    }
    @Override protected void containerTick() { super.containerTick(); save.active = menu.canCommit(); }
    @Override protected void renderBg(GuiGraphics graphics, float partial, int mouseX, int mouseY) {
        DungeonsTheme.panel(graphics, leftPos, topPos, imageWidth, imageHeight);
        DungeonsTheme.inset(graphics, leftPos + 9, topPos + 91, 292, 82);
    }
    @Override protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
        line(graphics, title, 10, 10, 290, DungeonsTheme.GOLD);
        line(graphics, Component.translatable("screen.dungeonsalive.inn.status." + menu.state().name().toLowerCase(Locale.ROOT)), 10, 24, 290, DungeonsTheme.SOUL);
        line(graphics, Component.translatable("screen.dungeonsalive.inn.counts", menu.seats(), menu.beds(), menu.food()), 10, 40, 290, DungeonsTheme.TEXT);
        line(graphics, Component.translatable("screen.dungeonsalive.inn.guest." + menu.guest().name().toLowerCase(Locale.ROOT)), 10, 53, 290, DungeonsTheme.MUTED);
        line(graphics, Component.translatable("screen.dungeonsalive.inn.connection." + menu.connected()), 10, 66, 290, DungeonsTheme.MUTED);
        if (menu.problem() != null) line(graphics, Component.translatable("screen.dungeonsalive.inn.problem", position(menu.problem())), 10, 79, 290, DungeonsTheme.GOLD);
        var plan = menu.plan();
        line(graphics, Component.translatable("screen.dungeonsalive.inn.plan"), 15, 97, 280, DungeonsTheme.GOLD);
        planLine(graphics, "first", plan.first(), 111); planLine(graphics, "second", plan.second(), 122);
        planLine(graphics, "entrance", plan.entrance(), 133); planLine(graphics, "stock", plan.stock(), 144); planLine(graphics, "arrival", plan.arrival(), 155);
        Component footer = menu.result() != InnService.Code.OK
                ? Component.translatable("inn.dungeonsalive.result." + menu.result().name().toLowerCase(Locale.ROOT))
                : Component.translatable("screen.dungeonsalive.inn.help");
        int row = 179;
        for (var text : font.split(footer, 286)) {
            if (row > 190) break;
            graphics.drawString(font, text, 12, row, DungeonsTheme.MUTED, false); row += 10;
        }
    }
    private void planLine(GuiGraphics graphics, String key, @Nullable BlockPos pos, int y) {
        line(graphics, Component.translatable("screen.dungeonsalive.inn." + key, position(pos)), 15, y, 280, DungeonsTheme.TEXT);
    }
    private static String position(@Nullable BlockPos pos) { return pos == null ? "—" : pos.getX() + " / " + pos.getY() + " / " + pos.getZ(); }
    private void line(GuiGraphics graphics, Component text, int x, int y, int width, int colour) {
        float scale = Math.min(1F, (float) width / Math.max(1, font.width(text)));
        graphics.pose().pushPose(); graphics.pose().translate(x, y, 0); graphics.pose().scale(scale, scale, 1);
        graphics.drawString(font, text, 0, 0, colour, false); graphics.pose().popPose();
    }
    @Override public void render(GuiGraphics graphics, int mouseX, int mouseY, float partial) {
        super.render(graphics, mouseX, mouseY, partial); renderTooltip(graphics, mouseX, mouseY);
    }
}
