package de.laugenbreze.dungeonsalive.network;

import java.util.function.Consumer;

public final class ClientCurrentBridge {
    private static Consumer<CurrentImpulsePayload> receiver = ignored -> {};
    private ClientCurrentBridge() {}
    public static void install(Consumer<CurrentImpulsePayload> value) { receiver = value; }
    public static void receive(CurrentImpulsePayload payload) { receiver.accept(payload); }
}
