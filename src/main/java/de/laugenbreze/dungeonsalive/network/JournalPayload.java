package de.laugenbreze.dungeonsalive.network;

import de.laugenbreze.dungeonsalive.DungeonsAlive;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record JournalPayload(boolean open, String json) implements CustomPacketPayload {
    // Total view size is checked by QuestService in addition to per-definition limits.
    // UTF-8 bytes remain below the 1 MiB S2C payload ceiling.
    public static final int MAX_JSON_CHARS = 196_608;
    public static final Type<JournalPayload> TYPE = new Type<>(
            ResourceLocation.fromNamespaceAndPath(DungeonsAlive.MOD_ID, "journal"));
    public static final StreamCodec<RegistryFriendlyByteBuf, JournalPayload> CODEC = StreamCodec.of(
            (buffer, value) -> { buffer.writeBoolean(value.open()); buffer.writeUtf(value.json(), MAX_JSON_CHARS); },
            buffer -> new JournalPayload(buffer.readBoolean(), buffer.readUtf(MAX_JSON_CHARS)));

    @Override
    public Type<? extends CustomPacketPayload> type() { return TYPE; }
}
