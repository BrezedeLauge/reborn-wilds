package de.laugenbreze.dungeonsalive.item;

import de.laugenbreze.dungeonsalive.DungeonsAlive;
import java.util.Comparator;
import java.util.List;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

/** One complete mod inventory, derived from the real item registry so new items cannot be forgotten. */
public final class DungeonsCreativeTab {
    private static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, DungeonsAlive.MOD_ID);
    static {
        TABS.register("dungeons_alive", () -> CreativeModeTab.builder()
                .title(Component.translatable("itemGroup.dungeonsalive"))
                .icon(() -> ModItems.QUEST_JOURNAL.get().getDefaultInstance())
                .displayItems((parameters, output) -> allItems().forEach(output::accept)).build());
    }
    public static List<Item> allItems() {
        return BuiltInRegistries.ITEM.stream().filter(item -> BuiltInRegistries.ITEM.getKey(item).getNamespace().equals(DungeonsAlive.MOD_ID))
                .sorted(Comparator.comparingInt(DungeonsCreativeTab::category)
                        .thenComparing(item -> BuiltInRegistries.ITEM.getKey(item).getPath())).toList();
    }
    private static int category(Item item) {
        if (item == ModItems.QUEST_JOURNAL.get()) return 0;
        if (item instanceof BlockItem) return 1;
        if (item instanceof SpawnEggItem) return 4;
        if (BuiltInRegistries.ITEM.getKey(item).getPath().endsWith("_seal")) return 3;
        return 2;
    }
    public static void register(IEventBus bus) { TABS.register(bus); }
    private DungeonsCreativeTab() {}
}
