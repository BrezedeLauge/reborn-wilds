package de.laugenbreze.dungeonsalive.world.trail;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.ICancellableEvent;

/** Claim/protection integrations can veto automatic wear or regrowth before any block mutation. */
public final class TrailChangeEvent extends Event implements ICancellableEvent {
    public final ServerLevel level;
    public final BlockPos position;
    public final BlockState before, after;
    public TrailChangeEvent(ServerLevel level, BlockPos position, BlockState before, BlockState after) {
        this.level = level; this.position = position.immutable(); this.before = before; this.after = after;
    }
}
