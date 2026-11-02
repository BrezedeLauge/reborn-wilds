package de.laugenbreze.dungeonsalive.settlement.inn;

import de.laugenbreze.dungeonsalive.entity.peaceful.DungeonsMerchant;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.ai.goal.OpenDoorGoal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import org.jetbrains.annotations.Nullable;

/** Vanilla door interaction, restricted to the current visitor's explicitly owned inn area. */
public final class InnDoorGoal extends OpenDoorGoal {
    private final DungeonsMerchant visitor;
    @Nullable private Block openedDoor;
    public InnDoorGoal(DungeonsMerchant visitor) { super(visitor, true); this.visitor = visitor; }
    @Override public boolean canUse() {
        var inn = InnService.visitOf(visitor);
        return inn != null && visitor.isAlive() && !visitor.isNoAi() && !visitor.isTrading()
                && InnRouteValidator.loadedArea(visitor.level(), visitor.blockPosition(), InnRouteValidator.LOADED_RADIUS)
                && super.canUse() && allowed(visitor.level(), inn, doorPos)
                && !visitor.level().getBlockState(doorPos).getValue(DoorBlock.OPEN);
    }
    @Override public boolean canContinueToUse() {
        var inn = InnService.visitOf(visitor);
        return inn != null && allowed(visitor.level(), inn, doorPos) && super.canContinueToUse();
    }
    @Override protected void setOpen(boolean open) {
        var inn = InnService.visitOf(visitor);
        if (inn == null || !allowed(visitor.level(), inn, doorPos)) return;
        BlockState state = visitor.level().getBlockState(doorPos);
        if (open) openedDoor = state.getBlock();
        else if (openedDoor != state.getBlock() || state.getValue(DoorBlock.POWERED)) return;
        super.setOpen(open);
        if (!open) openedDoor = null;
    }
    static boolean allowed(Level level, @Nullable InnRegistry.Inn inn, BlockPos door) {
        if (inn == null || !(level instanceof ServerLevel server) || !InnRouteValidator.loadedArea(level, door, 2)
                || !inn.dimension().equals(level.dimension().location().toString())) return false;
        BlockState state = level.getBlockState(door);
        if (!(state.getBlock() instanceof DoorBlock) || !state.is(BlockTags.WOODEN_DOORS)
                || !BuiltInRegistries.BLOCK.getKey(state.getBlock()).getNamespace().equals("minecraft")
                || !DoorBlock.isWoodenDoor(level, door)) return false;
        BlockPos lower = state.getValue(DoorBlock.HALF) == DoubleBlockHalf.LOWER ? door : door.below();
        if (!inn.bounds().contains(InnRouteValidator.point(lower)) || !inn.bounds().contains(InnRouteValidator.point(lower.above()))) return false;
        // A retired inn may have been replaced by somebody else's marked room.
        // Its departing visitor does not gain rights to that new owner's door.
        return InnSavedData.get(server.getServer()).all().stream().noneMatch(other -> !other.retired()
                && !other.id().equals(inn.id()) && !other.owner().equals(inn.owner())
                && other.dimension().equals(inn.dimension()) && other.bounds().contains(InnRouteValidator.point(lower)));
    }
}
