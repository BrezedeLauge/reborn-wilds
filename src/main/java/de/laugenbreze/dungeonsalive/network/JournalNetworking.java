package de.laugenbreze.dungeonsalive.network;

import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;

public final class JournalNetworking {
    private JournalNetworking() {}
    public static void register(RegisterPayloadHandlersEvent event) {
        event.registrar("1").playToClient(JournalPayload.TYPE, JournalPayload.CODEC,
                (payload, context) -> ClientJournalBridge.receive(payload));
    }
}
