package de.laugenbreze.dungeonsalive.item;

import de.laugenbreze.dungeonsalive.DungeonsAlive;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModItems {
    private static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(DungeonsAlive.MOD_ID);
    public static final DeferredItem<QuestJournalItem> QUEST_JOURNAL = ITEMS.registerItem(
            "quest_journal", QuestJournalItem::new, new Item.Properties().stacksTo(1));
    public static final DeferredItem<WindHornItem> WIND_HORN = ITEMS.registerItem(
            "wind_horn", WindHornItem::new, new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON));
    public static final DeferredItem<de.laugenbreze.dungeonsalive.settlement.SettlementCharterItem> SETTLEMENT_CHARTER = ITEMS.registerItem(
            "settlement_charter", de.laugenbreze.dungeonsalive.settlement.SettlementCharterItem::new,
            new Item.Properties().stacksTo(16).rarity(Rarity.RARE));

    private ModItems() {}

    public static void register(IEventBus bus) {
        ITEMS.register(bus);
        bus.addListener(ModItems::addToCreativeTab);
    }

    private static void addToCreativeTab(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
            event.accept(QUEST_JOURNAL);
            event.accept(WIND_HORN);
            event.accept(SETTLEMENT_CHARTER);
        }
    }
}
