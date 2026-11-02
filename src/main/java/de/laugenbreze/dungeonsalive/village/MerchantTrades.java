package de.laugenbreze.dungeonsalive.village;

import de.laugenbreze.dungeonsalive.item.ModItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.trading.MerchantOffer;
import net.neoforged.neoforge.event.village.VillagerTradesEvent;

import java.util.Optional;

/** Sandbox reinterpretations. No fake item-upgrade or enchant-reroll service. */
public final class MerchantTrades {
    private MerchantTrades() {}

    public static void register(VillagerTradesEvent event) {
        if (event.getType() == VillageContent.VILLAGE_MERCHANT.profession().get()) {
            add(event, 1, buy(Items.WHEAT, 20, 2), sell(Items.BREAD, 2, 6, 1));
            add(event, 2, buy(Items.LEATHER, 8, 10), sell(Items.ARROW, 3, 16, 5));
            add(event, 3, sell(Items.LANTERN, 3, 2, 10), sell(Items.CAMPFIRE, 4, 2, 10));
            add(event, 4, sell(Items.COMPASS, 5, 1, 15), sell(Items.LEAD, 6, 2, 15));
            add(event, 5, sell(Items.SADDLE, 18, 1, 30), sell(ModItems.WIND_HORN.get(), 24, 1, 30));
        } else if (event.getType() == VillageContent.LUXURY_MERCHANT.profession().get()) {
            add(event, 1, buy(Items.GOLD_INGOT, 6, 2), sell(Items.GOLDEN_CARROT, 5, 3, 1));
            add(event, 2, sell(Items.CLOCK, 8, 1, 5), sell(Items.CANDLE, 4, 8, 5));
            add(event, 3, sell(Items.AMETHYST_BLOCK, 10, 4, 10), sell(Items.ENDER_PEARL, 8, 1, 10));
            add(event, 4, sell(Items.GOLDEN_APPLE, 24, 1, 15), sell(Items.NAME_TAG, 20, 1, 15));
            add(event, 5, sell(Items.DIAMOND_HELMET, 32, 1, 30), sell(Items.DIAMOND_SWORD, 36, 1, 30));
        } else if (event.getType() == VillageContent.MYSTERY_MERCHANT.profession().get()) {
            add(event, 1, buy(Items.AMETHYST_SHARD, 12, 2), choice(5, 1, 1, Items.SPYGLASS, Items.COMPASS, Items.CLOCK));
            add(event, 2, choice(6, 4, 5, Items.PRISMARINE_SHARD, Items.GLOW_INK_SAC, Items.GLOWSTONE_DUST), sell(Items.ENDER_PEARL, 8, 1, 5));
            add(event, 3, choice(12, 1, 10, Items.GOAT_HORN, Items.NAME_TAG, Items.SADDLE), sell(Items.ECHO_SHARD, 16, 1, 10));
            add(event, 4, choice(18, 1, 15, Items.GOLDEN_APPLE, Items.NAUTILUS_SHELL, Items.PHANTOM_MEMBRANE), sell(Items.OBSIDIAN, 10, 4, 15));
            add(event, 5, sell(ModItems.WIND_HORN.get(), 28, 1, 30), choice(24, 1, 30, Items.DIAMOND_AXE, Items.DIAMOND_PICKAXE, Items.DIAMOND_SHOVEL));
        } else if (event.getType() == VillageContent.BLACKSMITH.profession().get()) {
            add(event, 1, buy(Items.COAL, 18, 2), sell(Items.IRON_AXE, 6, 1, 1));
            add(event, 2, buy(Items.IRON_INGOT, 5, 10), sell(Items.SHIELD, 8, 1, 5));
            add(event, 3, sell(Items.IRON_CHESTPLATE, 12, 1, 10), sell(Items.CROSSBOW, 12, 1, 10));
            add(event, 4, sell(Items.ANVIL, 24, 1, 15), sell(Items.DIAMOND_SHOVEL, 18, 1, 15));
            add(event, 5, sell(Items.DIAMOND_PICKAXE, 36, 1, 30), sell(Items.DIAMOND_BOOTS, 32, 1, 30));
        } else if (event.getType() == VillageContent.GIFT_WRAPPER.profession().get()) {
            add(event, 1, buy(Items.PAPER, 24, 2), sell(Items.CHEST, 2, 2, 1));
            add(event, 2, buy(Items.STRING, 18, 10), sell(Items.ITEM_FRAME, 3, 4, 5));
            add(event, 3, sell(Items.FLOWER_POT, 3, 4, 10), sell(Items.CANDLE, 4, 8, 10));
            add(event, 4, sell(Items.NAME_TAG, 18, 1, 15), sell(Items.GLOW_ITEM_FRAME, 6, 2, 15));
            add(event, 5, sell(Items.ENDER_CHEST, 24, 1, 30), sell(Items.DECORATED_POT, 10, 2, 30));
        } else if (event.getType() == VillageContent.ENCHANTSMITH.profession().get()) {
            add(event, 1, buy(Items.LAPIS_LAZULI, 24, 2), sell(Items.BOOK, 3, 1, 1));
            add(event, 2, book(Enchantments.UNBREAKING, 1, 14, 5), book(Enchantments.EFFICIENCY, 1, 12, 5));
            add(event, 3, book(Enchantments.PROTECTION, 2, 20, 10), book(Enchantments.SHARPNESS, 2, 20, 10));
            add(event, 4, book(Enchantments.POWER, 3, 26, 15), book(Enchantments.FORTUNE, 2, 28, 15));
            add(event, 5, book(Enchantments.UNBREAKING, 3, 32, 30), book(Enchantments.SILK_TOUCH, 1, 32, 30));
        }
    }

    private static void add(VillagerTradesEvent event, int level, VillagerTrades.ItemListing... trades) {
        for (var trade : trades) event.getTrades().get(level).add(trade);
    }
    private static VillagerTrades.ItemListing buy(Item item, int count, int xp) {
        return (merchant, random) -> new MerchantOffer(new ItemCost(item, count), new ItemStack(Items.EMERALD), 12, xp, 0.05F);
    }
    private static VillagerTrades.ItemListing sell(Item item, int cost, int count, int xp) {
        return (merchant, random) -> new MerchantOffer(new ItemCost(Items.EMERALD, cost), new ItemStack(item, count), 8, xp, 0.05F);
    }
    /** Randomness runs once when an offer is created; the exact output is visible before paying. */
    private static VillagerTrades.ItemListing choice(int cost, int count, int xp, Item... choices) {
        return (merchant, random) -> new MerchantOffer(new ItemCost(Items.EMERALD, cost),
                new ItemStack(choices[random.nextInt(choices.length)], count), 4, xp, 0.05F);
    }
    private static VillagerTrades.ItemListing book(ResourceKey<Enchantment> enchantment, int level, int emeralds, int xp) {
        return (merchant, random) -> new MerchantOffer(new ItemCost(Items.EMERALD, emeralds), Optional.of(new ItemCost(Items.BOOK)),
                EnchantedBookItem.createForEnchantment(new EnchantmentInstance(
                        merchant.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(enchantment), level)), 4, xp, 0.2F);
    }
}
