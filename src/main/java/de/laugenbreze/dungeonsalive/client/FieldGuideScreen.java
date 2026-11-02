package de.laugenbreze.dungeonsalive.client;

import de.laugenbreze.dungeonsalive.client.entity.reference.ReferenceModels;
import de.laugenbreze.dungeonsalive.client.entity.reference.ReferencePreviewState;
import de.laugenbreze.dungeonsalive.entity.AttackKind;
import de.laugenbreze.dungeonsalive.entity.DungeonMob;
import de.laugenbreze.dungeonsalive.entity.MobProfile;
import de.laugenbreze.dungeonsalive.entity.MobProfiles;
import de.laugenbreze.dungeonsalive.entity.ModEntities;
import de.laugenbreze.dungeonsalive.entity.peaceful.DungeonsMerchant;
import de.laugenbreze.dungeonsalive.entity.peaceful.ModPeacefulEntities;
import de.laugenbreze.dungeonsalive.entity.peaceful.PeacefulProfiles;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerType;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

/** Read-only local model viewer: no world entity is added and no server action is requested. */
public final class FieldGuideScreen extends Screen {
    private record Entry(ResourceLocation id, Component title, MobProfile mob,
                         PeacefulProfiles.Profile peaceful, VillagerProfession profession) {}
    private final List<Entry> entries = new ArrayList<>();
    private final List<Button> rowButtons = new ArrayList<>();
    private List<Entry> filtered = List.of();
    private EditBox search;
    private Button motionButton;
    private Button nextActionButton;
    private Entry selected;
    private LivingEntity preview;
    private int page, mode, animationTick, attackIndex;
    private int left, top, boxWidth, boxHeight, listWidth, rows;
    private boolean villagers;
    private int tier;
    private String searchText = "";

    public FieldGuideScreen() { super(Component.translatable("screen.dungeonsalive.guide")); }

    @Override
    protected void init() {
        rowButtons.clear();
        boxWidth = Math.min(width - 12, 660);
        boxHeight = Math.min(height - 12, 420);
        left = (width - boxWidth) / 2;
        top = (height - boxHeight) / 2;
        listWidth = Math.min(216, Math.max(112, boxWidth / 3));
        rows = Math.max(1, (boxHeight - 116) / 24);
        entries.clear();
        if (villagers) {
            BuiltInRegistries.VILLAGER_PROFESSION.entrySet().stream()
                    .filter(e -> e.getKey().location().getNamespace().equals("minecraft")
                            || e.getKey().location().getNamespace().equals("dungeonsalive"))
                    .filter(e -> e.getValue() != VillagerProfession.NONE && e.getValue() != VillagerProfession.NITWIT)
                    .sorted(Comparator.comparing(e -> e.getKey().location().toString()))
                    .forEach(e -> entries.add(new Entry(e.getKey().location(), professionName(e.getKey().location()), null, null, e.getValue())));
        } else {
            for (var mob : MobProfiles.all()) entries.add(new Entry(ResourceLocation.fromNamespaceAndPath("dungeonsalive", mob.id()),
                    Component.translatable("entity.dungeonsalive." + mob.id()), mob, null, null));
            for (var creature : PeacefulProfiles.all()) entries.add(new Entry(ResourceLocation.fromNamespaceAndPath("dungeonsalive", creature.id()),
                    Component.translatable("entity.dungeonsalive." + creature.id()), null, creature, null));
        }
        search = new EditBox(font, left + 10, top + 54, listWidth - 20, 18, Component.translatable("screen.dungeonsalive.search"));
        search.setMaxLength(80);
        search.setHint(Component.translatable("screen.dungeonsalive.search"));
        search.setValue(searchText);
        search.setResponder(value -> { searchText = value; page = 0; applyFilter(); });
        addRenderableWidget(search);
        applyFilter();
        addRenderableWidget(Button.builder(Component.translatable(villagers ? "screen.dungeonsalive.guide.mobs" : "screen.dungeonsalive.guide.villagers"),
                button -> { villagers = !villagers; searchText = ""; page = 0; selected = null; mode = 0; rebuildWidgets(); })
                .bounds(left + boxWidth - 124, top + 9, 114, 20).build(DungeonsButton::new));
        addRenderableWidget(Button.builder(Component.translatable("screen.dungeonsalive.gear.short"),
                button -> minecraft.setScreen(new GearGuideScreen()))
                .bounds(left + boxWidth - 198, top + 9, 68, 20).build(DungeonsButton::new));
        addRenderableWidget(Button.builder(filterName(),
                button -> { tier = (tier + 1) % 6; page = 0; applyFilter(); button.setMessage(filterName()); })
                .bounds(left + 10, top + 31, listWidth - 20, 18).build(DungeonsButton::new)).active = !villagers;
        for (int i = 0; i < rows; i++) {
            final int row = i;
            rowButtons.add(addRenderableWidget(Button.builder(Component.empty(), button -> {
                int index = page * rows + row;
                if (index < filtered.size()) select(filtered.get(index));
            }).bounds(left + 10, top + 79 + i * 24, listWidth - 20, 21).build(DungeonsButton::new)));
        }
        addRenderableWidget(Button.builder(Component.literal("‹"), button -> { if (page > 0) page--; })
                .bounds(left + 10, top + boxHeight - 28, 26, 19).build(DungeonsButton::new));
        addRenderableWidget(Button.builder(Component.literal("›"), button -> { if ((page + 1) * rows < filtered.size()) page++; })
                .bounds(left + listWidth - 36, top + boxHeight - 28, 26, 19).build(DungeonsButton::new));
        int detailX = left + listWidth + 10;
        int detailWidth = boxWidth - listWidth - 20;
        int nextWidth = Math.clamp(detailWidth / 3, 36, 70);
        motionButton = addRenderableWidget(Button.builder(animationName(), button -> {
            mode = (mode + 1) % animationModes(); animationTick = 0; button.setMessage(animationName());
        }).bounds(detailX, top + boxHeight - 51, Math.max(1, detailWidth - nextWidth - 5), 19).build(DungeonsButton::new));
        nextActionButton = addRenderableWidget(Button.builder(nextWidth < 60 ? Component.literal("›")
                : Component.translatable("screen.dungeonsalive.guide.next_action"), button -> {
            attackIndex++; animationTick = 0; mode = 2;
        }).bounds(detailX + detailWidth - nextWidth, top + boxHeight - 51, nextWidth, 19).build(DungeonsButton::new));
        addRenderableWidget(Button.builder(Component.translatable("gui.done"), button -> onClose())
                .bounds(left + boxWidth - 80, top + boxHeight - 28, 70, 19).build(DungeonsButton::new));
        updateControls();
    }

    private Component filterName() {
        return switch (tier) {
            case 4 -> Component.translatableWithFallback("screen.dungeonsalive.guide.filter.4", "Friedliche Kreaturen");
            case 5 -> Component.translatableWithFallback("screen.dungeonsalive.guide.filter.5", "Händler");
            default -> Component.translatable("screen.dungeonsalive.guide.filter." + tier);
        };
    }

    private int animationModes() {
        return selected == null ? 1 : selected.peaceful() != null
                && selected.peaceful().kind() != PeacefulProfiles.Kind.MERCHANT ? 2 : 3;
    }

    private Component animationName() {
        if (mode == 2 && selected != null && selected.peaceful() != null)
            return Component.translatableWithFallback("screen.dungeonsalive.guide.motion.greeting", "Begrüßung");
        return Component.translatable("screen.dungeonsalive.guide.motion." + mode);
    }

    private void updateControls() {
        mode = Math.floorMod(mode, animationModes());
        if (motionButton == null || nextActionButton == null) return;
        boolean combat = selected != null && selected.mob() != null;
        nextActionButton.visible = combat;
        nextActionButton.active = combat && selected.mob().attacks().size() > 1;
        int detailWidth = boxWidth - listWidth - 20;
        motionButton.setWidth(combat ? detailWidth - nextActionButton.getWidth() - 5 : detailWidth);
        motionButton.active = selected != null;
        motionButton.setMessage(animationName());
    }

    private void applyFilter() {
        String query = searchText.toLowerCase(Locale.ROOT);
        filtered = entries.stream().filter(entry -> (villagers || tier == 0
                || tier <= 3 && entry.mob() != null && entry.mob().tier().ordinal() == tier - 1
                || tier == 4 && entry.peaceful() != null && entry.peaceful().kind() != PeacefulProfiles.Kind.MERCHANT
                || tier == 5 && entry.peaceful() != null && entry.peaceful().kind() == PeacefulProfiles.Kind.MERCHANT)
                && (entry.id().toString().contains(query) || entry.title().getString().toLowerCase(Locale.ROOT).contains(query))).toList();
        if (selected == null || filtered.stream().noneMatch(entry -> entry.id().equals(selected.id()))) {
            if (filtered.isEmpty()) { selected = null; preview = null; }
            else select(filtered.getFirst());
        }
        page = Math.min(page, Math.max(0, (filtered.size() - 1) / rows));
    }

    private void select(Entry entry) {
        if (preview instanceof Villager villager) de.laugenbreze.dungeonsalive.client.village.VillageClient.clearPreview(villager);
        if (preview instanceof Mob mob) ReferencePreviewState.clear(mob);
        selected = entry;
        animationTick = 0;
        attackIndex = 0;
        if (minecraft == null || minecraft.level == null) { preview = null; return; }
        if (entry.mob() != null) {
            preview = ModEntities.type(entry.mob().id()).create(minecraft.level);
        } else if (entry.peaceful() != null) {
            preview = entry.peaceful().kind() == PeacefulProfiles.Kind.MERCHANT
                    ? ModPeacefulEntities.merchantType(entry.peaceful().id()).create(minecraft.level)
                    : ModPeacefulEntities.creatureType(entry.peaceful().id()).create(minecraft.level);
        } else {
            Villager villager = EntityType.VILLAGER.create(minecraft.level);
            if (villager != null) {
                villager.setVillagerData(villager.getVillagerData().setType(VillagerType.PLAINS).setProfession(entry.profession()).setLevel(2));
                villager.setNoAi(true);
            }
            preview = villager;
        }
        if (preview != null) {
            // These detached previews represent an ordinary ground stance;
            // flying/water creatures keep their appropriate airborne stance.
            preview.setOnGround(entry.peaceful() == null
                    || !entry.peaceful().flying() && !entry.peaceful().aquatic());
        }
        updateControls();
    }

    @Override
    public void tick() {
        animationTick++;
        if (preview != null) {
            // Advance visual state only. Calling entity.tick() would incorrectly run world behavior here.
            preview.tickCount++;
            // Vanilla turtles multiply the ground-flipper amplitude by eight;
            // use their slow actual ground gait instead of a humanoid stride.
            float stride = selected.peaceful() != null && selected.peaceful().host().equals("turtle") ? .08f : .65f;
            preview.walkAnimation.update(mode == 1 ? stride : 0, .3f);
            if (preview instanceof DungeonMob mob) {
                AttackKind action = mode == 2 ? selected.mob().attacks().get(Math.floorMod(attackIndex, selected.mob().attacks().size())) : null;
                mob.previewAction(action, action == null ? 0 : animationTick % (action.duration() + 30));
            }
            if (preview instanceof Villager villager) {
                de.laugenbreze.dungeonsalive.client.village.VillageClient.preview(villager, mode == 2);
            }
            if (preview instanceof DungeonsMerchant merchant) {
                if (mode == 2) ReferencePreviewState.setGesture(merchant, "GREETING", animationTick % 60, 30);
                else ReferencePreviewState.clear(merchant);
            }
        }
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        updateControls();
        renderBackground(graphics, mouseX, mouseY, delta);
        DungeonsTheme.panel(graphics, left, top, boxWidth, boxHeight);
        graphics.fill(left + listWidth, top + 31, left + listWidth + 1, top + boxHeight - 7, 0xff52684d);
        graphics.drawString(font, title, left + 11, top + 14, 0xffe2c77c, false);
        // Rows are stable widgets; update their labels without rebuilding focus or the search field.
        int row = 0;
        for (var button : rowButtons) {
                int index = page * rows + row++;
                button.visible = index < filtered.size();
                if (button.visible) {
                    Entry entry = filtered.get(index);
                    String label = font.plainSubstrByWidth(entry.title().getString(), listWidth - 32);
                    button.setMessage(Component.literal(label));
                    button.active = selected == null || !entry.id().equals(selected.id());
                }
        }
        graphics.drawCenteredString(font, Component.literal((filtered.isEmpty() ? 0 : page + 1) + "/"
                + Math.max(1, (filtered.size() + rows - 1) / rows)), left + listWidth / 2, top + boxHeight - 22, 0xffc6d5be);
        int x = left + listWidth + 12;
        int detailWidth = boxWidth - listWidth - 24;
        if (selected != null && preview != null) {
            int titleY = top + 36;
            for (var line : font.split(selected.title(), detailWidth)) {
                graphics.drawString(font, line, x, titleY, 0xffe6cf90, false);
                titleY += 10;
                if (titleY > top + 55) break;
            }
            int modelTop = titleY + 3;
            int modelBottom = top + boxHeight - 116;
            if (modelBottom - modelTop >= 25) {
                double modelHeight = preview.getBbHeight();
                double modelWidth = Math.max(.8, preview.getBbWidth());
                double modelDepth = modelWidth;
                double centerY = modelHeight * .5;
                if (selected.profession() == null) {
                    var bounds = ReferenceModels.bounds(selected.id().getPath());
                    modelHeight = Math.max(.15, bounds.height());
                    modelWidth = Math.max(.15, bounds.width());
                    modelDepth = Math.max(.15, bounds.depth());
                    centerY = bounds.centerY();
                }
                if (mode == 2 && selected.mob() != null) {
                    modelHeight *= 1.35;
                    modelWidth *= 1.2;
                    centerY += modelHeight * .06;
                }
                // Native rigs may extend far beyond their collision hitbox. Fit
                // the transformed model bounds and account for the small yaw.
                double projectedWidth = modelWidth * .97 + modelDepth * .25;
                double projectedHeight = modelHeight + modelDepth * .025;
                int scale = (int)Math.clamp(Math.min((modelBottom - modelTop - 8) / projectedHeight,
                        (detailWidth - 12) / projectedWidth) * .87, 4, 256);
                float centerOffset = (float)((centerY - preview.getBbHeight() * .5) / preview.getScale());
                InventoryScreen.renderEntityInInventoryFollowsAngle(graphics, x, modelTop, x + detailWidth, modelBottom,
                        scale, centerOffset, .35f, .05f, preview);
            }
            int infoY = top + boxHeight - 110;
            if (selected.mob() != null) {
                graphics.drawString(font, Component.translatable("screen.dungeonsalive.guide.stats",
                        (int) selected.mob().health(), (int) selected.mob().damage()), x, infoY, 0xffc6d5be, false);
                if (mode == 2) {
                    var action = selected.mob().attacks().get(Math.floorMod(attackIndex, selected.mob().attacks().size()));
                    graphics.drawWordWrap(font, Component.translatable("screen.dungeonsalive.guide.action", action.name().toLowerCase(Locale.ROOT)),
                            x, infoY + 13, detailWidth, 0xffa5d6cc);
                } else graphics.drawWordWrap(font, Component.translatableWithFallback("screen.dungeonsalive.guide.model_preview",
                        "Wähle eine Animation, um die Bewegungen dieser Kreatur anzusehen."), x, infoY + 13, detailWidth, 0xffc6d5be);
            } else if (selected.peaceful() != null) {
                graphics.drawWordWrap(font, selected.peaceful().kind() == PeacefulProfiles.Kind.MERCHANT
                        ? Component.translatableWithFallback("screen.dungeonsalive.guide.merchant", "Rechtsklick: Handel · Schleichen + Rechtsklick: Quests")
                        : selected.peaceful().kind() == PeacefulProfiles.Kind.CIVILIAN
                        ? Component.translatableWithFallback("screen.dungeonsalive.guide.civilian", "Rechtsklick: Befreien · danach Folgen oder Warten")
                        : Component.translatableWithFallback("screen.dungeonsalive.guide.peaceful", "Friedlich · Begleiter und Tiere ohne Angriffe"),
                        x, infoY, detailWidth, 0xffc6d5be);
            } else {
                graphics.drawWordWrap(font, Component.translatable(de.laugenbreze.dungeonsalive.client.village.VillageClient.rendererInstalled()
                        ? "screen.dungeonsalive.guide.work_preview" : "screen.dungeonsalive.guide.external_model"), x, infoY, detailWidth, 0xffc6d5be);
            }
            if (minecraft.options.advancedItemTooltips)
                graphics.drawString(font, font.plainSubstrByWidth(selected.id().toString(), detailWidth), x, top + boxHeight - 64, 0xff9baa91, false);
        } else graphics.drawWordWrap(font, Component.translatable("screen.dungeonsalive.guide.empty"), x, top + 60, detailWidth, 0xffc6d5be);
        // Screen.render() applies the background blur in 1.21.1; doing that here would blur our panel and model.
        for (var widget : renderables) widget.render(graphics, mouseX, mouseY, delta);
    }

    /** Used by the isolated render-smoke run, never by a remote server. */
    public void showMob(String id) {
        villagers = false;
        tier = 0;
        searchText = "";
        rebuildWidgets();
        entries.stream().filter(entry -> entry.profession() == null && entry.id().getPath().equals(id)).findFirst().ifPresent(this::select);
        mode = 0;
    }

    public void showMobAction(String id, int actionIndex) {
        showMob(id);
        mode = animationModes() == 3 ? 2 : 1;
        attackIndex = actionIndex;
    }

    public void showProfession(ResourceLocation id) {
        villagers = true;
        searchText = "";
        rebuildWidgets();
        entries.stream().filter(entry -> entry.id().equals(id)).findFirst().ifPresent(this::select);
        mode = 2;
    }

    @Override
    public void removed() {
        if (preview instanceof Villager villager) de.laugenbreze.dungeonsalive.client.village.VillageClient.clearPreview(villager);
        if (preview instanceof Mob mob) ReferencePreviewState.clear(mob);
        super.removed();
    }

    private static Component professionName(ResourceLocation id) {
        return Component.translatable("entity.minecraft.villager." + (id.getNamespace().equals("minecraft") ? id.getPath() : id.getNamespace() + "." + id.getPath()));
    }
}
