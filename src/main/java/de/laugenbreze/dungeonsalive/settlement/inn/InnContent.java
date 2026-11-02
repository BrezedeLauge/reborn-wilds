package de.laugenbreze.dungeonsalive.settlement.inn;

import com.mojang.serialization.MapCodec;
import de.laugenbreze.dungeonsalive.DungeonsAlive;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class InnContent {
    private static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(DungeonsAlive.MOD_ID);
    private static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(DungeonsAlive.MOD_ID);
    private static final DeferredRegister<MapCodec<? extends Block>> TYPES = DeferredRegister.create(Registries.BLOCK_TYPE, DungeonsAlive.MOD_ID);
    private static final DeferredRegister<BlockEntityType<?>> ENTITIES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, DungeonsAlive.MOD_ID);
    private static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(Registries.MENU, DungeonsAlive.MOD_ID);
    public static final DeferredHolder<MapCodec<? extends Block>, MapCodec<InnAnchorBlock>> CODEC = TYPES.register("inn_ledger", () -> InnAnchorBlock.CODEC);
    public static final DeferredBlock<InnAnchorBlock> ANCHOR = BLOCKS.register("inn_ledger", () -> new InnAnchorBlock(
            BlockBehaviour.Properties.ofFullCopy(Blocks.LECTERN).pushReaction(PushReaction.BLOCK)));
    public static final DeferredItem<net.minecraft.world.item.BlockItem> ANCHOR_ITEM = ITEMS.registerSimpleBlockItem(ANCHOR);
    public static final DeferredItem<InnMarkerItem> MARKER = ITEMS.register("inn_marker", () -> new InnMarkerItem(new Item.Properties().stacksTo(1)));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<InnAnchorBlockEntity>> ANCHOR_ENTITY = ENTITIES.register("inn_ledger",
            () -> BlockEntityType.Builder.of(InnAnchorBlockEntity::new, ANCHOR.get()).build(null));
    public static final DeferredHolder<MenuType<?>, MenuType<InnMenu>> MENU = MENUS.register("inn_ledger", () -> IMenuTypeExtension.create(InnMenu::new));
    private InnContent() { }
    public static void register(IEventBus bus) { TYPES.register(bus); BLOCKS.register(bus); ITEMS.register(bus); ENTITIES.register(bus); MENUS.register(bus); }
    public static boolean open(ServerPlayer player, BlockPos position) {
        var level = player.serverLevel();
        if (!player.isAlive() || player.isSpectator() || !player.mayBuild() || !level.hasChunkAt(position)
                || player.distanceToSqr(Vec3.atCenterOf(position)) > 64 || !level.mayInteract(player, position)
                || !(level.getBlockEntity(position) instanceof InnAnchorBlockEntity anchor)) return false;
        if (!player.getUUID().equals(anchor.ownerId())) {
            player.displayClientMessage(Component.translatable("inn.dungeonsalive.not_owner"), true); return false;
        }
        var draft = InnSelection.get(player, position); var status = InnService.status(player, position);
        var view = new InnPlanView(position.immutable(), anchor.placementId(), draft == null ? status.revision() : draft.revision(),
                draft == null ? null : draft.token(), draft == null ? null : draft.first(), draft == null ? null : draft.second(),
                draft == null ? null : draft.entrance(), draft == null ? null : draft.stock(), draft == null ? null : draft.arrival());
        return player.openMenu(new SimpleMenuProvider((id, inventory, owner) -> new InnMenu(id, inventory, view),
                Component.translatable("block.dungeonsalive.inn_ledger")), view::write).isPresent();
    }
}
