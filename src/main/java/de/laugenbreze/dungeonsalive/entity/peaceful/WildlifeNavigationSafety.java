package de.laugenbreze.dungeonsalive.entity.peaceful;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;

/** Bounded, non-loading terrain checks shared by wildlife movement. */
final class WildlifeNavigationSafety {
    static final int MAX_FOLLOW_RANGE = 24;
    static final int LOADED_RADIUS = MAX_FOLLOW_RANGE + 16 + 2;
    private WildlifeNavigationSafety() {}

    static boolean loadedArea(Level level, BlockPos center, int radius) {
        if (radius < 0 || radius > 64) return false;
        for (int x = (center.getX() - radius) >> 4; x <= (center.getX() + radius) >> 4; x++) {
            for (int z = (center.getZ() - radius) >> 4; z <= (center.getZ() + radius) >> 4; z++) {
                if (!level.hasChunk(x, z)) return false;
            }
        }
        return true;
    }

    static boolean safeGround(DungeonsCreature creature, BlockPos position) {
        var level = creature.level();
        int margin = Math.max(2, (int)Math.ceil(creature.getBbWidth() / 2) + 1);
        if (margin > 8 || position.getY() <= level.getMinBuildHeight()
                || position.getY() + creature.getBbHeight() >= level.getMaxBuildHeight()
                || !level.getWorldBorder().isWithinBounds(position) || !loadedArea(level, position, margin)) return false;
        var floor = level.getBlockState(position.below());
        if (!floor.isFaceSturdy(level, position.below(), Direction.UP) || dangerous(level, position.below())) return false;
        for (int y = 0; y <= Math.ceil(creature.getBbHeight()); y++) if (dangerous(level, position.above(y))) return false;
        var box = creature.getBoundingBox().move(Vec3.atBottomCenterOf(position).subtract(creature.position()));
        return level.noCollision(creature, box);
    }

    private static boolean dangerous(Level level, BlockPos position) {
        var state = level.getBlockState(position);
        return !state.getFluidState().isEmpty() || state.is(Blocks.FIRE) || state.is(Blocks.SOUL_FIRE)
                || state.is(Blocks.MAGMA_BLOCK) || state.is(Blocks.CACTUS) || state.is(Blocks.SWEET_BERRY_BUSH)
                || state.is(Blocks.WITHER_ROSE) || state.is(Blocks.CAMPFIRE) || state.is(Blocks.SOUL_CAMPFIRE)
                || state.is(Blocks.POWDER_SNOW);
    }
}
