package de.laugenbreze.dungeonsalive.world.keygate;

import com.mojang.serialization.MapCodec;
import de.laugenbreze.dungeonsalive.DungeonsAlive;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

/** Optional player-built gates; no structures or existing doors are replaced. */
public final class KeyGateContent {
    private static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(DungeonsAlive.MOD_ID);
    private static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(DungeonsAlive.MOD_ID);
    private static final DeferredRegister<MapCodec<? extends Block>> BLOCK_TYPES = DeferredRegister.create(Registries.BLOCK_TYPE, DungeonsAlive.MOD_ID);
    private static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, DungeonsAlive.MOD_ID);
    public static final DeferredHolder<MapCodec<? extends Block>, MapCodec<KeyGateBlock>> GATE_CODEC = BLOCK_TYPES.register("key_gate", () -> KeyGateBlock.CODEC);
    public static final DeferredBlock<KeyGateBlock> KEY_GATE = BLOCKS.register("key_gate",
            () -> new KeyGateBlock("key_golem", BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_DOOR)));
    public static final DeferredBlock<KeyGateBlock> DIAMOND_KEY_GATE = BLOCKS.register("diamond_key_gate",
            () -> new KeyGateBlock("diamond_key_golem", BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_DOOR)));
    public static final DeferredItem<KeyGateItem> KEY_GATE_ITEM = ITEMS.register("key_gate",
            () -> new KeyGateItem(KEY_GATE.get(), new Item.Properties()));
    public static final DeferredItem<KeyGateItem> DIAMOND_KEY_GATE_ITEM = ITEMS.register("diamond_key_gate",
            () -> new KeyGateItem(DIAMOND_KEY_GATE.get(), new Item.Properties()));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<KeyGateBlockEntity>> GATE_OWNER = BLOCK_ENTITIES.register("key_gate_owner",
            () -> BlockEntityType.Builder.of(KeyGateBlockEntity::new, KEY_GATE.get(), DIAMOND_KEY_GATE.get()).build(null));

    private KeyGateContent() {}
    public static void register(IEventBus bus) {
        BLOCK_TYPES.register(bus); BLOCKS.register(bus); ITEMS.register(bus); BLOCK_ENTITIES.register(bus);
        bus.addListener(KeyGateContent::creative);
    }
    private static void creative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.FUNCTIONAL_BLOCKS) {
            event.accept(KEY_GATE_ITEM); event.accept(DIAMOND_KEY_GATE_ITEM);
        }
    }
}
