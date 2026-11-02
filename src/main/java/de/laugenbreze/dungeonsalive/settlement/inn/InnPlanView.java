package de.laugenbreze.dungeonsalive.settlement.inn;

import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import org.jetbrains.annotations.Nullable;

/** Bounded read-only opening snapshot. No room coordinates are accepted from the client. */
public record InnPlanView(BlockPos anchor, UUID placement, long revision, @Nullable UUID token,
                          @Nullable BlockPos first, @Nullable BlockPos second, @Nullable BlockPos entrance,
                          @Nullable BlockPos stock, @Nullable BlockPos arrival) {
    public boolean complete() { return token != null && first != null && second != null && entrance != null && stock != null && arrival != null; }
    public void write(RegistryFriendlyByteBuf buffer) {
        buffer.writeBlockPos(anchor); buffer.writeUUID(placement); buffer.writeLong(revision);
        buffer.writeBoolean(token != null); if (token != null) buffer.writeUUID(token);
        writePos(buffer, first); writePos(buffer, second); writePos(buffer, entrance); writePos(buffer, stock); writePos(buffer, arrival);
    }
    public static InnPlanView read(RegistryFriendlyByteBuf buffer) {
        return new InnPlanView(buffer.readBlockPos(), buffer.readUUID(), buffer.readLong(), buffer.readBoolean() ? buffer.readUUID() : null,
                readPos(buffer), readPos(buffer), readPos(buffer), readPos(buffer), readPos(buffer));
    }
    private static void writePos(RegistryFriendlyByteBuf buffer, @Nullable BlockPos pos) {
        buffer.writeBoolean(pos != null); if (pos != null) buffer.writeBlockPos(pos);
    }
    @Nullable private static BlockPos readPos(RegistryFriendlyByteBuf buffer) { return buffer.readBoolean() ? buffer.readBlockPos() : null; }
}
