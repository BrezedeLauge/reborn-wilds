package de.laugenbreze.dungeonsalive.network;

import java.util.function.Consumer;

public final class ClientGearHudBridge {
    private static Consumer<GearHudPayload> receiver = ignored -> {};
    private ClientGearHudBridge() {}
    public static void install(Consumer<GearHudPayload> handler) { receiver = handler; }
    public static void receive(GearHudPayload payload) { receiver.accept(payload); }
}
