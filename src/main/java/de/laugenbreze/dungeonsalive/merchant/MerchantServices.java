package de.laugenbreze.dungeonsalive.merchant;

import de.laugenbreze.dungeonsalive.DungeonsAlive;
import de.laugenbreze.dungeonsalive.entity.peaceful.DungeonsMerchant;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class MerchantServices {
    private static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(Registries.MENU, DungeonsAlive.MOD_ID);
    public static final DeferredHolder<MenuType<?>, MenuType<MerchantServiceMenu>> MENU =
            MENUS.register("merchant_service", () -> IMenuTypeExtension.create(MerchantServiceMenu::new));
    private MerchantServices() {}
    public static void register(IEventBus bus) { MENUS.register(bus); }
    /** False means this role has no service; a recognized but unavailable role never falls through to another purchase. */
    public static boolean open(ServerPlayer player, DungeonsMerchant merchant) {
        var kind = ServiceKind.forMerchant(merchant.profile().id());
        if (kind == null) return false;
        if (!player.isAlive() || player.isSpectator() || !merchant.isAlive() || merchant.isRemoved() || player.level() != merchant.level()
                || player.distanceToSqr(merchant) > 64 || merchant.getTradingPlayer() != null && merchant.getTradingPlayer() != player) return true;
        // Closing a previous menu may clear this very NPC's customer lock. Do it
        // before installing the new lock, including explicit reopening by a server caller.
        if (player.containerMenu != player.inventoryMenu) player.closeContainer();
        if (!player.isAlive() || !merchant.isAlive() || merchant.isRemoved() || player.level() != merchant.level()
                || player.distanceToSqr(merchant) > 64 || merchant.getTradingPlayer() != null && merchant.getTradingPlayer() != player) return true;
        merchant.setTradingPlayer(player);
        var opened = player.openMenu(new SimpleMenuProvider((id, inventory, owner) -> new MerchantServiceMenu(id, inventory, merchant, kind),
                merchant.getDisplayName()), buffer -> { buffer.writeVarInt(merchant.getId()); buffer.writeVarInt(kind.ordinal()); });
        if (opened.isEmpty() && merchant.getTradingPlayer() == player) merchant.setTradingPlayer(null);
        return true;
    }
    /** Never closes a chest, ordinary trade, or another NPC's service menu. */
    public static void closeCustomer(DungeonsMerchant merchant) {
        if (merchant.getTradingPlayer() instanceof ServerPlayer customer
                && customer.containerMenu instanceof MerchantServiceMenu menu && menu.belongsTo(merchant)) customer.closeContainer();
    }
}
