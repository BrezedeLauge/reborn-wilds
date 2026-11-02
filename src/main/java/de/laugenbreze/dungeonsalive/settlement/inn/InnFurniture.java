package de.laugenbreze.dungeonsalive.settlement.inn;

import de.laugenbreze.dungeonsalive.entity.peaceful.DungeonsMerchant;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import org.jetbrains.annotations.Nullable;

/** Contract for actual furniture scanning/reservation. There is intentionally no permissive default adapter. */
public interface InnFurniture {
    enum Kind { SEAT, BED }
    record Spot(BlockPos furniture, BlockPos contact) {
        public Spot { furniture = furniture.immutable(); contact = contact.immutable(); }
    }
    record Snapshot(List<Spot> seats, List<Spot> beds, boolean loaded, boolean complete) {
        public Snapshot {
            seats = List.copyOf(seats); beds = List.copyOf(beds);
            if (seats.size() > 8 || beds.size() > 8) throw new IllegalArgumentException("Unbounded furniture scan");
        }
    }
    record Lease(UUID innId, UUID guestId, long configRevision, Kind kind, Spot spot) {
        public Lease {
            Objects.requireNonNull(innId); Objects.requireNonNull(guestId); Objects.requireNonNull(kind); Objects.requireNonNull(spot);
            if (configRevision < 1) throw new IllegalArgumentException("Invalid furniture revision");
        }
    }
    Snapshot inspect(ServerLevel level, InnRegistry.Inn inn);
    @Nullable Lease resume(ServerLevel level, DungeonsMerchant guest, InnRegistry.Inn inn);
    @Nullable Lease reserve(ServerLevel level, DungeonsMerchant guest, InnRegistry.Inn inn, Kind kind);
    boolean use(ServerLevel level, DungeonsMerchant guest, Lease lease);
    /** False retains the own lease while a safe loaded dismount/wakeup is unavailable. */
    boolean release(ServerLevel level, DungeonsMerchant guest, Lease lease);
}
