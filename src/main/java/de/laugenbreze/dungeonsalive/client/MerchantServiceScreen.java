package de.laugenbreze.dungeonsalive.client;

import de.laugenbreze.dungeonsalive.merchant.MerchantServiceMenu;
import de.laugenbreze.dungeonsalive.merchant.ServiceKind;
import de.laugenbreze.dungeonsalive.merchant.ServiceStatus;
import de.laugenbreze.dungeonsalive.item.gear.enchantment.GearEnchantmentKind;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/** Ordinary server container: item/payment slots, read-only preview, explicit purchase. */
public final class MerchantServiceScreen extends AbstractContainerScreen<MerchantServiceMenu> {
    private static final int GOLD = 0xFFE1BD78, CREAM = 0xFFE6DDC8, MUTED = 0xFFAFBBA9;
    private final List<Button> enchantments = new ArrayList<>();
    private Button commit;

    public MerchantServiceScreen(MerchantServiceMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        imageWidth = 176; imageHeight = 227;
        inventoryLabelY = 134;
    }

    @Override protected void init() {
        super.init();
        enchantments.clear();
        GearEnchantmentKind[] kinds = GearEnchantmentKind.values();
        for (int i = 0; i < kinds.length; i++) {
            final int index = i;
            Component name = Component.translatable(kinds[i].nameKey());
            Button button = Button.builder(Component.literal(font.plainSubstrByWidth(name.getString(), 47)),
                    ignored -> send(10 + index)).bounds(leftPos + 8 + (i % 3) * 54, topPos + 77 + (i / 3) * 19, 52, 18)
                    .tooltip(Tooltip.create(name.copy().append("\n").append(Component.translatable(kinds[i].nameKey() + ".description")))).build(DungeonsButton::new);
            enchantments.add(addRenderableWidget(button));
        }
        commit = addRenderableWidget(Button.builder(Component.translatable("screen.dungeonsalive.service.confirm"),
                        ignored -> { commit.active = false; send(0); })
                .bounds(leftPos + 8, topPos + 115, 98, 18).build(DungeonsButton::new));
        addRenderableWidget(Button.builder(Component.translatable("screen.dungeonsalive.service.goods"), ignored -> send(1))
                .bounds(leftPos + 110, topPos + 115, 58, 18).build(DungeonsButton::new));
    }

    private void send(int action) {
        if (minecraft != null && minecraft.getConnection() != null)
            net.neoforged.neoforge.network.PacketDistributor.sendToServer(
                    new de.laugenbreze.dungeonsalive.network.MerchantServiceActionPayload(menu.containerId, action, menu.quoteRevision()));
    }

    @Override protected void containerTick() {
        super.containerTick();
        commit.active = menu.status() == ServiceStatus.READY;
        for (int i = 0; i < enchantments.size(); i++) {
            Button button = enchantments.get(i);
            button.visible = menu.serviceKind() == ServiceKind.ENCHANTSMITH;
            button.active = menu.status() != ServiceStatus.COLLECT_RESULT && menu.selection() != i;
        }
    }

    @Override protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        int x = leftPos, y = topPos;
        DungeonsTheme.panel(graphics, x, y, imageWidth, imageHeight);
        DungeonsTheme.inset(graphics, x + 6, y + 28, 164, 44);
        for (var slot : menu.slots) {
            DungeonsTheme.slot(graphics, x + slot.x, y + slot.y);
        }
        graphics.drawCenteredString(font, "+", x + 61, y + 49, MUTED);
        graphics.drawCenteredString(font, "→", x + 115, y + 49, GOLD);
    }

    @Override protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
        graphics.drawString(font, font.plainSubstrByWidth(title.getString(), 160), 8, 6, GOLD, false);
        graphics.drawString(font, Component.translatable("screen.dungeonsalive.service." + menu.serviceKind().name().toLowerCase(Locale.ROOT)), 8, 16, CREAM, false);
        smallLabel(graphics, Component.translatable("screen.dungeonsalive.service.item"), 34);
        smallLabel(graphics, Component.translatable("screen.dungeonsalive.service.price", menu.cost()), 88);
        smallLabel(graphics, Component.translatable("screen.dungeonsalive.service.preview"), 142);
        Component status = Component.translatable("screen.dungeonsalive.service.status." + menu.status().name().toLowerCase(Locale.ROOT));
        graphics.drawCenteredString(font, font.plainSubstrByWidth(status.getString(), 162), 88, 65,
                menu.status() == ServiceStatus.READY ? 0xFFB1D28B : CREAM);
        if (menu.serviceKind() != ServiceKind.ENCHANTSMITH) {
            Component description = Component.translatable("screen.dungeonsalive.service." + menu.serviceKind().name().toLowerCase(Locale.ROOT) + ".description");
            int y = 79;
            for (var line : font.split(description, 158)) {
                if (y > 102) break;
                graphics.drawString(font, line, 9, y, MUTED, false); y += 10;
            }
        }
        graphics.drawString(font, playerInventoryTitle, 8, inventoryLabelY, MUTED, false);
    }

    private void smallLabel(GuiGraphics graphics, Component label, int center) {
        graphics.pose().pushPose();
        graphics.pose().translate(center, 32, 0);
        graphics.pose().scale(.75F, .75F, 1);
        graphics.drawCenteredString(font, label, 0, 0, MUTED);
        graphics.pose().popPose();
    }

    @Override public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        super.render(graphics, mouseX, mouseY, partialTick);
        renderTooltip(graphics, mouseX, mouseY);
    }
}
