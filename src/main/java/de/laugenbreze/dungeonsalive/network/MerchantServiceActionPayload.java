package de.laugenbreze.dungeonsalive.network;

import de.laugenbreze.dungeonsalive.DungeonsAlive;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

/** A request references a displayed quote; the server owns every item and payment. */
public record MerchantServiceActionPayload(int containerId, int action, int quoteRevision) implements CustomPacketPayload {
    public static final Type<MerchantServiceActionPayload> TYPE = new Type<>(
            ResourceLocation.fromNamespaceAndPath(DungeonsAlive.MOD_ID, "merchant_service_action"));
    public static final StreamCodec<RegistryFriendlyByteBuf, MerchantServiceActionPayload> CODEC = StreamCodec.of(
            (buffer, value) -> { buffer.writeVarInt(value.containerId()); buffer.writeVarInt(value.action()); buffer.writeVarInt(value.quoteRevision()); },
            buffer -> new MerchantServiceActionPayload(buffer.readVarInt(), buffer.readVarInt(), buffer.readVarInt()));
    @Override public Type<? extends CustomPacketPayload> type() { return TYPE; }
}
