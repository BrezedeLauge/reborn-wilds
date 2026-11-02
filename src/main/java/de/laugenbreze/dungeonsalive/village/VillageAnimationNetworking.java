package de.laugenbreze.dungeonsalive.village;

import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import java.util.function.Consumer;

/** Safe common-side bridge: no Minecraft client classes loaded by a dedicated server. */
public final class VillageAnimationNetworking {
    private static Consumer<VillageAnimationPayload> receiver = ignored -> {};
    private VillageAnimationNetworking() {}
    public static void register(RegisterPayloadHandlersEvent event) {
        event.registrar("1").playToClient(VillageAnimationPayload.TYPE, VillageAnimationPayload.CODEC,
                (payload, context) -> receiver.accept(payload));
    }
    public static void installClientReceiver(Consumer<VillageAnimationPayload> clientReceiver) {
        receiver = clientReceiver;
    }
}
