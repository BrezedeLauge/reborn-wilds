package de.laugenbreze.dungeonsalive.world;

import de.laugenbreze.dungeonsalive.DungeonsAlive;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

/** Buildable landscape materials; no spreading, player-terrain replacement or hidden tick work. */
public final class LivingWorldBlocks {
    private static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(DungeonsAlive.MOD_ID);
    private static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(DungeonsAlive.MOD_ID);
    public static final DeferredBlock<Block> AMBER_MOSS = BLOCKS.register("amber_moss",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.MOSS_BLOCK)));
    public static final DeferredBlock<Block> LUMINOUS_MOSS = BLOCKS.register("luminous_moss",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.MOSS_BLOCK).lightLevel(state -> 6)));
    public static final DeferredBlock<Block> ANCIENT_BRICKS = BLOCKS.register("ancient_bricks",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_BRICKS)));
    public static final DeferredBlock<Block> CARVED_ANCIENT_BRICKS = BLOCKS.register("carved_ancient_bricks",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.CHISELED_STONE_BRICKS)));
    public static final DeferredBlock<Block> WAYFINDER_STONE = BLOCKS.register("wayfinder_stone",
            () -> new WayfinderStoneBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.LODESTONE).lightLevel(state -> 3)));
    public static final DeferredItem<BlockItem> AMBER_MOSS_ITEM = ITEMS.registerSimpleBlockItem(AMBER_MOSS);
    public static final DeferredItem<BlockItem> LUMINOUS_MOSS_ITEM = ITEMS.registerSimpleBlockItem(LUMINOUS_MOSS);
    public static final DeferredItem<BlockItem> ANCIENT_BRICKS_ITEM = ITEMS.registerSimpleBlockItem(ANCIENT_BRICKS);
    public static final DeferredItem<BlockItem> CARVED_ANCIENT_BRICKS_ITEM = ITEMS.registerSimpleBlockItem(CARVED_ANCIENT_BRICKS);
    public static final DeferredItem<BlockItem> WAYFINDER_STONE_ITEM = ITEMS.registerSimpleBlockItem(WAYFINDER_STONE);
    public static void register(IEventBus bus) { BLOCKS.register(bus); ITEMS.register(bus); }
    private LivingWorldBlocks() {}
}
