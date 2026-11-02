package de.laugenbreze.dungeonsalive.item.gear;

import de.laugenbreze.dungeonsalive.network.ClientGearHudBridge;
import de.laugenbreze.dungeonsalive.network.GearHudPayload;
import java.util.Map;
import java.util.WeakHashMap;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.server.ServerStoppedEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;

/** Only changed values go to their owning client; the cache is never a resource account. */
public final class GearHudEvents {
    private static final Map<ServerPlayer, GearHudPayload> SENT = new WeakHashMap<>();
    private GearHudEvents() {}
    public static void register() {
        NeoForge.EVENT_BUS.addListener(GearHudEvents::tick);
        NeoForge.EVENT_BUS.addListener(GearHudEvents::logout);
        NeoForge.EVENT_BUS.addListener(GearHudEvents::stop);
    }
    public static void networking(RegisterPayloadHandlersEvent event) {
        event.registrar("1").playToClient(GearHudPayload.TYPE, GearHudPayload.CODEC,
                (payload, context) -> context.enqueueWork(() -> ClientGearHudBridge.receive(payload)));
    }
    public static GearHudPayload snapshot(Player player) {
        boolean quiver = ArtifactExpansionState.quiverCharges(player) > 0;
        for (int slot = 0; !quiver && slot < player.getInventory().getContainerSize(); slot++) {
            var stack = player.getInventory().getItem(slot);
            quiver = stack.getItem() instanceof DungeonsArtifactItem artifact && artifact.kind() == ArtifactKind.FLAMING_QUIVER;
        }
        return new GearHudPayload(SoulEnergy.get(player), ArtifactExpansionState.quiverCharges(player),
                SoulEnergy.carriesSoulCollector(player), quiver);
    }
    private static void tick(PlayerTickEvent.Post event) {
        if (!(event.getEntity() instanceof ServerPlayer player) || player.tickCount % 5 != 0) return;
        var payload = snapshot(player);
        if (!payload.equals(SENT.get(player))) {
            PacketDistributor.sendToPlayer(player, payload);
            SENT.put(player, payload);
        }
    }
    private static void logout(PlayerEvent.PlayerLoggedOutEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) SENT.remove(player);
    }
    private static void stop(ServerStoppedEvent event) { SENT.clear(); }
}
