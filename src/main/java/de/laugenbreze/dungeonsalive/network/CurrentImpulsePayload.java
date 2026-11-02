package de.laugenbreze.dungeonsalive.network;

import de.laugenbreze.dungeonsalive.DungeonsAlive;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

/** Server-issued horizontal impulse; never accepts player-supplied forces. */
public record CurrentImpulsePayload(int entityId, float x, float z) implements CustomPacketPayload {
    public CurrentImpulsePayload {
        x = Float.isFinite(x) ? Math.clamp(x, -.028F, .028F) : 0;
        z = Float.isFinite(z) ? Math.clamp(z, -.028F, .028F) : 0;
    }
    public static final Type<CurrentImpulsePayload> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(DungeonsAlive.MOD_ID, "water_current"));
    public static final StreamCodec<RegistryFriendlyByteBuf, CurrentImpulsePayload> CODEC = StreamCodec.of(
            (buffer, value) -> { buffer.writeVarInt(value.entityId()); buffer.writeFloat(value.x()); buffer.writeFloat(value.z()); },
            buffer -> new CurrentImpulsePayload(buffer.readVarInt(), buffer.readFloat(), buffer.readFloat()));
    @Override public Type<? extends CustomPacketPayload> type() { return TYPE; }
}
