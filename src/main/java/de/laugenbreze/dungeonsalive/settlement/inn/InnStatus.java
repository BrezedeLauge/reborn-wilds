package de.laugenbreze.dungeonsalive.settlement.inn;

import java.util.UUID;
import net.minecraft.core.BlockPos;
import org.jetbrains.annotations.Nullable;

/** Small server-observed menu DTO; no inventory contents or invented obstacle position. */
public record InnStatus(@Nullable UUID innId, long revision, State state, @Nullable BlockPos problem,
                        int seats, int beds, int foodItems, boolean connected, GuestState guest) {
    public enum State { UNCONFIGURED, SCANNING, READY, UNLOADED, MISSING_SEAT, MISSING_BED, MISSING_STOCK, NO_FOOD, BLOCKED_ROUTE, NOT_OWNER }
    public enum GuestState { NONE, RESERVED, TRAVELLING, ARRIVED, RESTING, LEAVING, UNLOADED }
    public InnStatus {
        if (revision < 0 || seats < 0 || seats > 8 || beds < 0 || beds > 8 || foodItems < 0 || foodItems > 64)
            throw new IllegalArgumentException("Invalid bounded inn status");
        if (problem != null) problem = problem.immutable();
    }
}
