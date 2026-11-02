package de.laugenbreze.dungeonsalive.client;

import de.laugenbreze.dungeonsalive.network.JournalView;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.item.ItemStack;

import java.util.List;

/** Compact journal with a paged quest list and independently scrollable details. */
public final class JournalScreen extends Screen {
    private JournalView view;
    private String selected;
    private int page;
    private int detailScroll;
    private int maxDetailScroll;
    private int x, y, w, h, listWidth, rows;
    private static final int CREAM = 0xFFE6DDC8;
    private static final int GOLD = 0xFFCDAE63;
    private static final int MUTED = 0xFFA0AF9C;

    public JournalScreen(JournalView view) {
        super(Component.translatable("screen.dungeonsalive.journal"));
        this.view = view;
        if (!view.quests().isEmpty()) selected = view.quests().getFirst().id();
    }

    public void update(JournalView updated) {
        this.view = updated;
        if (view.quests().stream().noneMatch(entry -> entry.id().equals(selected))) {
            selected = view.quests().isEmpty() ? null : view.quests().getFirst().id();
            detailScroll = 0;
        }
        rebuildWidgets();
    }

    @Override
    protected void init() {
        w = Math.min(640, width - 16);
        h = Math.min(356, height - 16);
        x = (width - w) / 2;
        y = (height - h) / 2;
        listWidth = Math.max(105, Math.min(190, w / 3));
        rows = Math.max(1, (h - 100) / 32);
        page = Math.max(0, Math.min(page, Math.max(0, (view.quests().size() - 1) / rows)));
        int first = page * rows;
        for (int row = 0; row < rows && first + row < view.quests().size(); row++) {
            JournalView.Entry entry = view.quests().get(first + row);
            Component label = Component.literal(entry.title())
                    .withStyle(entry.id().equals(selected) ? ChatFormatting.GOLD : ChatFormatting.WHITE);
            addRenderableWidget(Button.builder(label, button -> {
                selected = entry.id(); detailScroll = 0; rebuildWidgets();
            }).bounds(x + 10, y + 51 + row * 32, listWidth - 20, 20).build(DungeonsButton::new));
        }
        Button previous = addRenderableWidget(Button.builder(Component.literal("<"), button -> {
            page--; rebuildWidgets();
        }).bounds(x + 10, y + h - 32, 26, 20).build(DungeonsButton::new));
        previous.active = page > 0;
        Button next = addRenderableWidget(Button.builder(Component.literal(">"), button -> {
            page++; rebuildWidgets();
        }).bounds(x + listWidth - 36, y + h - 32, 26, 20).build(DungeonsButton::new));
        next.active = first + rows < view.quests().size();

        var entry = selectedEntry();
        int rightX = x + listWidth + 14;
        if (entry != null && (entry.status().equals("AVAILABLE") || entry.status().equals("READY"))) {
            boolean ready = entry.status().equals("READY");
            addRenderableWidget(Button.builder(Component.translatable(ready ? "screen.dungeonsalive.claim" : "screen.dungeonsalive.accept"),
                    button -> {
                        ResourceLocation id = ResourceLocation.tryParse(entry.id());
                        if (id != null && minecraft != null && minecraft.getConnection() != null) {
                            button.active = false;
                            minecraft.getConnection().sendCommand("dungeonsalive " + (ready ? "claim " : "accept ") + id);
                        }
                    }).bounds(rightX, y + h - 32, Math.max(70, w - listWidth - 100), 20).build(DungeonsButton::new));
        }
        addRenderableWidget(Button.builder(Component.translatable("gui.done"), button -> onClose())
                .bounds(x + w - 76, y + h - 32, 64, 20).build(DungeonsButton::new));
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        graphics.fill(0, 0, width, height, 0xA0101717);
        DungeonsTheme.panel(graphics, x, y, w, h);
        DungeonsTheme.inset(graphics, x + listWidth, y + 43, w - listWidth - 9, h - 83);
        graphics.fill(x + 10, y + 39, x + w - 10, y + 40, 0x806D825B);
        graphics.drawString(font, title, x + 13, y + 11, GOLD, false);
        graphics.drawString(font, Component.translatable("screen.dungeonsalive.subtitle"), x + 13, y + 25, MUTED, false);

        int first = page * rows;
        for (int row = 0; row < rows && first + row < view.quests().size(); row++) {
            var entry = view.quests().get(first + row);
            graphics.drawString(font, statusText(entry.status()), x + 13, y + 73 + row * 32,
                    statusColor(entry.status()), false);
        }
        graphics.drawCenteredString(font, (page + 1) + " / " + Math.max(1, (view.quests().size() + rows - 1) / rows),
                x + listWidth / 2, y + h - 26, MUTED);

        var entry = selectedEntry();
        if (entry != null) renderDetails(graphics, entry);
        else graphics.drawString(font, Component.translatable("screen.dungeonsalive.empty"), x + listWidth + 14, y + 60, CREAM, false);
        // NeoForge 1.21.1 Screen.render applies a blur; our complete panel is already drawn.
        for (var widget : renderables) widget.render(graphics, mouseX, mouseY, partialTick);
    }

    private void renderDetails(GuiGraphics graphics, JournalView.Entry entry) {
        int left = x + listWidth + 14;
        int top = y + 51;
        int bottom = y + h - 47;
        int textWidth = w - listWidth - 30;
        graphics.enableScissor(left - 1, top, x + w - 11, bottom);
        int cursor = top - detailScroll;
        var icon = item(entry.icon());
        if (!icon.isEmpty()) graphics.renderItem(icon, left, cursor);
        cursor = drawWrapped(graphics, Component.literal(entry.title()), left + 23, cursor + 3, textWidth - 23, GOLD) + 13;
        cursor = drawWrapped(graphics, Component.literal(entry.description()), left, cursor, textWidth, CREAM) + 14;
        cursor = drawWrapped(graphics, Component.translatable("screen.dungeonsalive.objectives"), left, cursor, textWidth, GOLD) + 5;
        for (JournalView.Objective objective : entry.objectives()) {
            int color = objective.current() >= objective.required() ? 0xFF91D38A : CREAM;
            cursor = drawWrapped(graphics, Component.literal(objective.current() + " / " + objective.required()
                    + "  " + objective.label()), left, cursor, textWidth, color) + 5;
            int filled = objective.required() <= 0 ? textWidth
                    : (int)Math.clamp((double)objective.current() / objective.required() * textWidth, 0, textWidth);
            graphics.fill(left, cursor, left + textWidth, cursor + 3, 0xFF363A46);
            graphics.fill(left, cursor, left + filled, cursor + 3, objective.current() >= objective.required() ? DungeonsTheme.GOLD : DungeonsTheme.SOUL);
            cursor += 8;
        }
        cursor += 8;
        cursor = drawWrapped(graphics, Component.translatable("screen.dungeonsalive.rewards"), left, cursor, textWidth, GOLD) + 6;
        for (JournalView.Reward reward : entry.rewards()) {
            ItemStack stack = item(reward.itemId());
            graphics.renderItem(stack, left, cursor);
            cursor = Math.max(cursor + 20, drawWrapped(graphics, Component.literal(reward.count() + " × ")
                    .append(stack.getHoverName()), left + 23, cursor + 4, textWidth - 23, CREAM) + 5);
        }
        if (entry.experience() > 0) {
            cursor = drawWrapped(graphics, Component.translatable("screen.dungeonsalive.xp", entry.experience()),
                    left, cursor + 3, textWidth, MUTED) + 8;
        }
        if (entry.status().equals("LOCKED")) {
            cursor = drawWrapped(graphics, Component.translatable("screen.dungeonsalive.requires",
                    String.join(", ", entry.prerequisites())), left, cursor + 8, textWidth, MUTED) + 8;
        }
        cursor = drawWrapped(graphics, statusText(entry.status()), left, cursor + 10, textWidth, statusColor(entry.status())) + 4;
        graphics.disableScissor();
        maxDetailScroll = Math.max(0, cursor + detailScroll - bottom);
        detailScroll = Math.min(detailScroll, maxDetailScroll);
        if (maxDetailScroll > 0) {
            int viewport = bottom - top;
            int thumb = Math.max(14, viewport * viewport / (viewport + maxDetailScroll));
            int offset = (viewport - thumb) * detailScroll / maxDetailScroll;
            graphics.fill(x + w - 6, top + offset, x + w - 4, top + offset + thumb, GOLD);
        }
    }

    private int drawWrapped(GuiGraphics graphics, Component text, int left, int top, int availableWidth, int color) {
        for (FormattedCharSequence line : font.split(text, Math.max(20, availableWidth))) {
            graphics.drawString(font, line, left, top, color, false);
            top += 11;
        }
        return top;
    }

    private ItemStack item(String id) {
        ResourceLocation location = ResourceLocation.tryParse(id);
        return location == null || !BuiltInRegistries.ITEM.containsKey(location)
                ? ItemStack.EMPTY : new ItemStack(BuiltInRegistries.ITEM.get(location));
    }

    private JournalView.Entry selectedEntry() {
        return view.quests().stream().filter(entry -> entry.id().equals(selected)).findFirst().orElse(null);
    }

    private Component statusText(String status) {
        return Component.translatable("quest.status.dungeonsalive." + status.toLowerCase(java.util.Locale.ROOT));
    }
    private int statusColor(String status) {
        return switch (status) {
            case "READY" -> GOLD;
            case "CLAIMED" -> 0xFF91D38A;
            case "ACTIVE" -> CREAM;
            default -> MUTED;
        };
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontal, double vertical) {
        if (mouseX >= x + listWidth && mouseX <= x + w && mouseY >= y + 44 && mouseY <= y + h - 40) {
            detailScroll = Math.max(0, Math.min(maxDetailScroll, detailScroll - (int) (vertical * 18)));
            return true;
        }
        return super.mouseScrolled(mouseX, mouseY, horizontal, vertical);
    }

    @Override
    public boolean isPauseScreen() { return false; }
}
