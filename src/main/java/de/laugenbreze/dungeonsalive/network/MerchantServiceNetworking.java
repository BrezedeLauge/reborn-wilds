package de.laugenbreze.dungeonsalive.network;

import de.laugenbreze.dungeonsalive.merchant.MerchantServiceMenu;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;

public final class MerchantServiceNetworking {
    private MerchantServiceNetworking() {}
    public static void register(RegisterPayloadHandlersEvent event) {
        event.registrar("1").playToServer(MerchantServiceActionPayload.TYPE, MerchantServiceActionPayload.CODEC,
                (payload, context) -> context.enqueueWork(() -> {
                    if (context.player() instanceof ServerPlayer player) handle(player, payload);
                }));
    }
    public static void handle(ServerPlayer player, MerchantServiceActionPayload payload) {
        if (payload.containerId() < 0 || payload.quoteRevision() < 1 || payload.quoteRevision() > 30_000
                || !(payload.action() == 0 || payload.action() == 1 || payload.action() >= 10 && payload.action() < 16)
                || !(player.containerMenu instanceof MerchantServiceMenu menu) || menu.containerId != payload.containerId()
                || !menu.stillValid(player)) return;
        menu.handleServiceAction(player, payload.action(), payload.quoteRevision());
    }
}
