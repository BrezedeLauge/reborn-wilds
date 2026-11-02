package de.laugenbreze.dungeonsalive.village;

import de.laugenbreze.dungeonsalive.DungeonsAlive;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import java.util.UUID;

/** Server-to-client only. UUID protects against stale entity-ID reuse. */
public record VillageAnimationPayload(int entityId, UUID uuid, int mode, long startedAt, int duration)
        implements CustomPacketPayload {
    public static final Type<VillageAnimationPayload> TYPE = new Type<>(
            ResourceLocation.fromNamespaceAndPath(DungeonsAlive.MOD_ID, "villager_animation"));
    public static final StreamCodec<RegistryFriendlyByteBuf, VillageAnimationPayload> CODEC = StreamCodec.of(
            (b, p) -> {
                b.writeVarInt(p.entityId); b.writeUUID(p.uuid); b.writeByte(p.mode);
                b.writeVarLong(p.startedAt); b.writeVarInt(p.duration);
            }, b -> new VillageAnimationPayload(b.readVarInt(), b.readUUID(), b.readUnsignedByte(), b.readVarLong(), b.readVarInt()));
    public VillageAnimationPayload {
        if (entityId < 0 || uuid == null || mode < VillageActivity.IDLE || mode > VillageActivity.PANIC
                || duration < 0 || duration > 200 || startedAt < 0) throw new IllegalArgumentException("Invalid villager animation packet");
    }
    @Override public Type<? extends CustomPacketPayload> type() { return TYPE; }
}
