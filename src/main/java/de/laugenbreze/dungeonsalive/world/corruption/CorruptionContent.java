package de.laugenbreze.dungeonsalive.world.corruption;

import de.laugenbreze.dungeonsalive.DungeonsAlive;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class CorruptionContent {
    public static final ResourceKey<Biome> BIOME = ResourceKey.create(Registries.BIOME, id("blighted_thicket"));
    private static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(DungeonsAlive.MOD_ID);
    private static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(DungeonsAlive.MOD_ID);
    // No BlockItems: these are worldgen/system-owned markers, not obtainable building blocks.
    public static final DeferredBlock<Block> NATURAL_SOIL = BLOCKS.register("reserved_natural_soil",
            () -> new NaturalSoilBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.DIRT)));
    public static final DeferredBlock<Block> BLIGHTED_GRASS = BLOCKS.register("blighted_grass",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.DIRT)));
    public static final DeferredItem<CleansingFocusItem> FOCUS = ITEMS.registerItem("cleansing_focus", CleansingFocusItem::new,
            new Item.Properties().stacksTo(16).rarity(Rarity.UNCOMMON));
    public static void register(IEventBus bus) { BLOCKS.register(bus); ITEMS.register(bus); }
    private static ResourceLocation id(String path) { return ResourceLocation.fromNamespaceAndPath(DungeonsAlive.MOD_ID, path); }
    private CorruptionContent() {}
}
