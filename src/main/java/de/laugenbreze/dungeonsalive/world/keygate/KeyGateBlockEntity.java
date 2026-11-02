package de.laugenbreze.dungeonsalive.world.keygate;

import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

/** Only the lower half stores ownership. Breaking/replacing the gate creates a new placement. */
public final class KeyGateBlockEntity extends BlockEntity {
    @Nullable private UUID owner;
    public KeyGateBlockEntity(BlockPos position, BlockState state) { super(KeyGateContent.GATE_OWNER.get(), position, state); }
    @Nullable public UUID ownerId() { return owner; }
    void placedBy(@Nullable UUID player) { owner = player; setChanged(); }
    @Override protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        if (owner != null) tag.putUUID("DungeonsGateOwner", owner);
    }
    @Override protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        owner = tag.hasUUID("DungeonsGateOwner") ? tag.getUUID("DungeonsGateOwner") : null;
    }
}
