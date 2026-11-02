package de.laugenbreze.dungeonsalive.client;

import de.laugenbreze.dungeonsalive.item.gear.*;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ArmorItem;

import java.util.ArrayList;
import java.util.List;

/** Local guide renders the very same baked item models used by normal inventories. */
public final class GearGuideScreen extends Screen {
    private record Entry(String id, Item item, ArtifactKind artifact, WeaponKind weapon) {}
    private final List<Entry> entries = new ArrayList<>();
    private final List<Button> rowButtons = new ArrayList<>();
    private enum Category { ARTIFACTS, WEAPONS, RANGED, ARMOR }
    private Category category = Category.ARTIFACTS;
    private int page, left, top, boxWidth, boxHeight, listWidth, rows;
    private Entry selected;

    public GearGuideScreen() { super(Component.translatable("screen.dungeonsalive.gear")); }

    @Override protected void init() {
        boxWidth = Math.min(width - 12, 660);
        boxHeight = Math.min(height - 12, 420);
        left = (width - boxWidth) / 2;
        top = (height - boxHeight) / 2;
        listWidth = Math.min(210, Math.max(125, boxWidth / 3));
        rows = Math.max(1, (boxHeight - 105) / 25);
        rowButtons.clear(); entries.clear();
        switch (category) {
            case ARTIFACTS -> {
                entries.add(new Entry("wind_horn", de.laugenbreze.dungeonsalive.item.ModItems.WIND_HORN.get(), null, null));
                for (ArtifactKind kind : ArtifactKind.values()) entries.add(new Entry(kind.id, GearContent.artifact(kind), kind, null));
            }
            case WEAPONS -> {
                for (WeaponKind kind : WeaponKind.values()) entries.add(new Entry(kind.id, GearContent.weapon(kind), null, kind));
            }
            case RANGED -> {
                for (RangedWeaponKind kind : RangedWeaponKind.values()) entries.add(new Entry(kind.id, GearContent.ranged(kind), null, null));
            }
            case ARMOR -> {
                for (ArmorSetKind kind : ArmorSetKind.values()) for (ArmorItem.Type type : ArmorContent.PIECES)
                    entries.add(new Entry(kind.id + "_" + type.getName(), ArmorContent.armor(kind, type), null, null));
            }
        }
        if (selected == null || entries.stream().noneMatch(entry -> entry.id.equals(selected.id))) selected = entries.getFirst();
        addRenderableWidget(Button.builder(Component.translatable("screen.dungeonsalive.gear." + category.name().toLowerCase(java.util.Locale.ROOT)).append(" ›"),
                button -> { category = Category.values()[(category.ordinal() + 1) % Category.values().length]; page = 0; selected = null; rebuildWidgets(); })
                .bounds(left + 10, top + 34, listWidth - 20, 20).build(DungeonsButton::new));
        for (int i = 0; i < rows; i++) {
            final int row = i;
            rowButtons.add(addRenderableWidget(Button.builder(Component.empty(), button -> {
                int index = page * rows + row;
                if (index < entries.size()) selected = entries.get(index);
            }).bounds(left + 10, top + 61 + i * 25, listWidth - 20, 22).build(DungeonsButton::new)));
        }
        addRenderableWidget(Button.builder(Component.literal("‹"), button -> { if (page > 0) page--; })
                .bounds(left + 10, top + boxHeight - 28, 24, 19).build(DungeonsButton::new));
        addRenderableWidget(Button.builder(Component.literal("›"), button -> { if ((page + 1) * rows < entries.size()) page++; })
                .bounds(left + listWidth - 34, top + boxHeight - 28, 24, 19).build(DungeonsButton::new));
        addRenderableWidget(Button.builder(Component.translatable("screen.dungeonsalive.guide.short"), button -> minecraft.setScreen(new FieldGuideScreen()))
                .bounds(left + boxWidth - 190, top + boxHeight - 28, 105, 19).build(DungeonsButton::new));
        addRenderableWidget(Button.builder(Component.translatable("gui.done"), button -> onClose())
                .bounds(left + boxWidth - 78, top + boxHeight - 28, 68, 19).build(DungeonsButton::new));
    }

    /** Used only by the explicitly enabled native screenshot harness. */
    public void selectForCapture(String id) {
        Category next = java.util.Arrays.stream(WeaponKind.values()).anyMatch(kind -> kind.id.equals(id)) ? Category.WEAPONS
                : java.util.Arrays.stream(RangedWeaponKind.values()).anyMatch(kind -> kind.id.equals(id)) ? Category.RANGED
                : java.util.Arrays.stream(ArmorSetKind.values()).anyMatch(kind -> id.startsWith(kind.id + "_")) ? Category.ARMOR : Category.ARTIFACTS;
        if (category != next) { category = next; selected = null; page = 0; rebuildWidgets(); }
        selected = entries.stream().filter(entry -> entry.id.equals(id)).findFirst().orElseThrow();
        page = entries.indexOf(selected) / rows;
    }

    @Override public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(graphics, mouseX, mouseY, partialTick);
        DungeonsTheme.panel(graphics, left, top, boxWidth, boxHeight);
        graphics.fill(left + listWidth, top + 32, left + listWidth + 1, top + boxHeight - 36, 0xFF43584B);
        graphics.drawString(font, title, left + 12, top + 13, 0xF0E9C9, false);
        for (int row = 0; row < rowButtons.size(); row++) {
            Button button = rowButtons.get(row);
            int index = page * rows + row;
            button.visible = index < entries.size();
            if (button.visible) {
                Entry entry = entries.get(index);
                String name = Component.translatable("item.dungeonsalive." + entry.id).getString();
                button.setMessage(Component.literal(font.plainSubstrByWidth(name, listWidth - 30)));
                button.active = selected != entry;
            }
        }
        graphics.drawCenteredString(font, (page + 1) + " / " + Math.max(1, (entries.size() + rows - 1) / rows),
                left + listWidth / 2, top + boxHeight - 22, 0xB5C4B8);
        if (selected != null) {
            int x = left + listWidth + 14;
            int usable = boxWidth - listWidth - 28;
            int center = x + usable / 2;
            graphics.drawCenteredString(font, Component.translatable("item.dungeonsalive." + selected.id), center, top + 39, 0xFFE0A1);
            int modelHeight = Math.min(126, Math.max(60, boxHeight / 3));
            int scale = Math.max(2, Math.min(7, modelHeight / 16));
            int modelY = top + 61;
            DungeonsTheme.inset(graphics, x, modelY, usable, modelHeight);
            graphics.pose().pushPose();
            graphics.pose().translate(center - 8 * scale, modelY + (modelHeight - 16 * scale) / 2F, 0);
            graphics.pose().scale(scale, scale, 1);
            graphics.renderItem(new ItemStack(selected.item), 0, 0);
            graphics.pose().popPose();
            int textY = modelY + modelHeight + 10;
            List<Component> paragraphs = new ArrayList<>();
            paragraphs.add(Component.translatable("item.dungeonsalive." + selected.id + ".tooltip"));
            if (selected.item instanceof DungeonsArmorItem armor)
                paragraphs.add(Component.translatable("tooltip.dungeonsalive.armor_set." + armor.kind().id));
            if (selected.artifact != null) {
                paragraphs.add(Component.translatable("tooltip.dungeonsalive.artifact_cooldown", selected.artifact.cooldownTicks / 20));
                if (selected.artifact.soulCost > 0) paragraphs.add(Component.translatable(
                        selected.artifact == ArtifactKind.CORRUPTED_BEACON ? "tooltip.dungeonsalive.beacon_cost"
                                : "tooltip.dungeonsalive.artifact_cost", selected.artifact.soulCost));
            }
            paragraphs.add(Component.translatable("screen.dungeonsalive.gear.recipe"));
            for (Component paragraph : paragraphs) {
                for (var line : font.split(paragraph, usable)) {
                    if (textY + 9 > top + boxHeight - 38) break;
                    graphics.drawString(font, line, x, textY, 0xD1DCD3, false);
                    textY += 11;
                }
                textY += 5;
            }
        }
        // Screen.render applies the 1.21 background blur again. Draw the widgets
        // directly so the panel, text and actual item model remain sharp.
        for (var widget : renderables) widget.render(graphics, mouseX, mouseY, partialTick);
    }

    @Override public boolean isPauseScreen() { return false; }
}
