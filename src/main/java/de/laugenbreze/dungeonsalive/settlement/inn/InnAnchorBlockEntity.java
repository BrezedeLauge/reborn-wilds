package de.laugenbreze.dungeonsalive.settlement.inn;

import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

/** Ownership belongs to the real placement, never an item tag or client configuration packet. */
public final class InnAnchorBlockEntity extends BlockEntity implements InnAnchorAccess {
    @Nullable private UUID owner, inn;
    private UUID placement = UUID.randomUUID();
    public InnAnchorBlockEntity(BlockPos pos, BlockState state) { super(InnContent.ANCHOR_ENTITY.get(), pos, state); }
    @Override @Nullable public UUID ownerId() { return owner; }
    @Override @Nullable public UUID innId() { return inn; }
    public UUID placementId() { return placement; }
    void placedBy(@Nullable UUID player) { owner = player; inn = null; placement = UUID.randomUUID(); setChanged(); }
    @Override public void bindInn(UUID id) { inn = java.util.Objects.requireNonNull(id); setChanged(); }
    @Override protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        if (owner != null) tag.putUUID("Owner", owner);
        if (inn != null) tag.putUUID("Inn", inn);
        tag.putUUID("Placement", placement);
    }
    @Override protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        owner = tag.hasUUID("Owner") ? tag.getUUID("Owner") : null;
        inn = tag.hasUUID("Inn") ? tag.getUUID("Inn") : null;
        placement = tag.hasUUID("Placement") ? tag.getUUID("Placement") : UUID.randomUUID();
    }
}
