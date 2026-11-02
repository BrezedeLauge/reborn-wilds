package de.laugenbreze.dungeonsalive.network;

import de.laugenbreze.dungeonsalive.DungeonsAlive;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

/** Display only: never registered as a client-to-server payment or inventory message. */
public record GearHudPayload(int souls, int flameCharges, boolean showSouls, boolean showFlames) implements CustomPacketPayload {
    public GearHudPayload {
        souls = Math.clamp(souls, 0, 100);
        flameCharges = Math.clamp(flameCharges, 0, 7);
    }
    public static final Type<GearHudPayload> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(DungeonsAlive.MOD_ID, "gear_hud"));
    public static final StreamCodec<RegistryFriendlyByteBuf, GearHudPayload> CODEC = StreamCodec.of(
            (buffer, value) -> { buffer.writeVarInt(value.souls()); buffer.writeVarInt(value.flameCharges()); buffer.writeBoolean(value.showSouls()); buffer.writeBoolean(value.showFlames()); },
            buffer -> new GearHudPayload(buffer.readVarInt(), buffer.readVarInt(), buffer.readBoolean(), buffer.readBoolean()));
    @Override public Type<? extends CustomPacketPayload> type() { return TYPE; }
}
