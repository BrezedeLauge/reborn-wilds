package de.laugenbreze.dungeonsalive.network;

import java.util.function.Consumer;

/** No client classes here: this bridge is safe to load on a dedicated server. */
public final class ClientJournalBridge {
    private static Consumer<JournalPayload> receiver = ignored -> {};
    private ClientJournalBridge() {}
    public static void install(Consumer<JournalPayload> handler) { receiver = handler; }
    public static void receive(JournalPayload payload) { receiver.accept(payload); }
}
