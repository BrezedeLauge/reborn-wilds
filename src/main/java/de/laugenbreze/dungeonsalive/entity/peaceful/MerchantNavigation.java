package de.laugenbreze.dungeonsalive.entity.peaceful;

import java.util.Set;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.Path;
import org.jetbrains.annotations.Nullable;

/** Ground navigation whose camp routes never construct a region across an unloaded chunk. */
final class MerchantNavigation extends GroundPathNavigation {
    // Vanilla's region is centered on the mob: follow range + target-specific offset.
    // Both supported offsets (8 for blocks, 16 for entities) fit inside this bound.
    static final int MAX_FOLLOW_RANGE = 24;
    static final int LOADED_RADIUS = MAX_FOLLOW_RANGE + 16 + 2;
    private final DungeonsMerchant merchant;

    MerchantNavigation(DungeonsMerchant merchant, Level level) {
        super(merchant, level);
        this.merchant = merchant;
    }

    static boolean loadedArea(Level level, BlockPos center, int radius) {
        int minX = (center.getX() - radius) >> 4, maxX = (center.getX() + radius) >> 4;
        int minZ = (center.getZ() - radius) >> 4, maxZ = (center.getZ() + radius) >> 4;
        for (int x = minX; x <= maxX; x++) for (int z = minZ; z <= maxZ; z++) {
            // hasChunk is a non-loading presence check; never use getChunk here.
            if (!level.hasChunk(x, z)) return false;
        }
        return true;
    }

    @Override @Nullable public Path createPath(BlockPos target, int accuracy) {
        // GroundPathNavigation examines the destination column before its base path call.
        if (merchant.hasCampRoutine() && (!loadedArea(level, target, 2)
                || !loadedArea(level, merchant.blockPosition(), LOADED_RADIUS))) return null;
        return super.createPath(target, accuracy);
    }

    @Override @Nullable protected Path createPath(Set<BlockPos> targets, int regionOffset,
                                                  boolean offsetUpward, int accuracy, float followRange) {
        if (!merchant.hasCampRoutine()) return super.createPath(targets, regionOffset, offsetUpward, accuracy, followRange);
        if (regionOffset < 0 || regionOffset > 16 || !Float.isFinite(followRange) || followRange <= 0
                || !loadedArea(level, merchant.blockPosition(), LOADED_RADIUS)
                || targets.stream().anyMatch(target -> !loadedArea(level, target, 2))) return null;
        return super.createPath(targets, regionOffset, offsetUpward, accuracy, Math.min(followRange, MAX_FOLLOW_RANGE));
    }

    @Override public void tick() {
        // A previously valid route may outlive a chunk unloading. This also gates
        // vanilla's automatic path recomputation, not only our initial move request.
        if (merchant.hasCampRoutine() && !loadedArea(level, merchant.blockPosition(), LOADED_RADIUS)) {
            stop();
            return;
        }
        super.tick();
    }
}
