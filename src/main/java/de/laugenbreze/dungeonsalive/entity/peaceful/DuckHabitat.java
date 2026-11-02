package de.laugenbreze.dungeonsalive.entity.peaceful;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

/** Small loaded surface-water/shore pairs. No heightmap query or block modification. */
final class DuckHabitat {
    record Places(BlockPos water, BlockPos shore) { }
    private DuckHabitat() { }

    static boolean surfaceWater(Level level, BlockPos pos) {
        return pos.getY() > level.getMinBuildHeight() && pos.getY() + 2 < level.getMaxBuildHeight()
                && level.getWorldBorder().isWithinBounds(pos)
                && WildlifeNavigationSafety.loadedArea(level, pos, 2)
                && level.getFluidState(pos).is(FluidTags.WATER)
                && level.getFluidState(pos.above()).isEmpty();
    }

    static Vec3 waterPosition(BlockPos pos) { return new Vec3(pos.getX() + .5, pos.getY() + .8, pos.getZ() + .5); }

    static boolean safe(DungeonsCreature duck, BlockPos pos) {
        Level level = duck.level();
        if (pos.getY() <= level.getMinBuildHeight() || pos.getY() + 2 >= level.getMaxBuildHeight()
                || !level.getWorldBorder().isWithinBounds(pos)
                || !WildlifeNavigationSafety.loadedArea(level, pos, 2)) return false;
        boolean water = surfaceWater(level, pos);
        if (!water && (!level.getFluidState(pos).isEmpty() || !level.getFluidState(pos.above()).isEmpty()
                || !level.getBlockState(pos.below()).isFaceSturdy(level, pos.below(), Direction.UP))) return false;
        for (int y = -1; y <= 1; y++) {
            var state = level.getBlockState(pos.above(y));
            if (state.is(Blocks.FIRE) || state.is(Blocks.SOUL_FIRE) || state.is(Blocks.MAGMA_BLOCK)
                    || state.is(Blocks.CAMPFIRE) || state.is(Blocks.SOUL_CAMPFIRE) || state.is(Blocks.CACTUS)
                    || state.is(Blocks.POWDER_SNOW) || state.is(Blocks.SWEET_BERRY_BUSH)
                    || state.is(Blocks.WITHER_ROSE) || state.getFluidState().is(FluidTags.LAVA)) return false;
        }
        Vec3 destination = water ? waterPosition(pos) : Vec3.atBottomCenterOf(pos);
        var box = duck.getBoundingBox().move(destination.subtract(duck.position()));
        // Family members may share a landing area; their ordinary entity collision
        // separates them. Terrain alone decides whether this is a valid destination.
        return level.getWorldBorder().isWithinBounds(box)
                && !level.getBlockCollisions(duck, box).iterator().hasNext();
    }

    @Nullable static Places find(DungeonsCreature duck, @Nullable BlockPos rejectedShore) {
        BlockPos center = duck.blockPosition();
        BlockPos anchor = duck.wildlifeAnchor() == null ? center : duck.wildlifeAnchor();
        Places nearest = null;
        double best = Double.POSITIVE_INFINITY;
        // At most 25*25*5 columns, only when a route needs a new habitat pair.
        for (int dx = -12; dx <= 12; dx++) for (int dz = -12; dz <= 12; dz++) {
            if (dx * dx + dz * dz > 144) continue;
            for (int dy = -2; dy <= 2; dy++) {
                BlockPos water = center.offset(dx, dy, dz);
                if (water.distSqr(anchor) > 24 * 24 || !surfaceWater(duck.level(), water) || !safe(duck, water)) continue;
                for (Direction side : Direction.Plane.HORIZONTAL) for (int rise = 0; rise <= 1; rise++) {
                    BlockPos shore = water.relative(side).above(rise);
                    if (shore.equals(rejectedShore) || surfaceWater(duck.level(), shore) || !safe(duck, shore)) continue;
                    double distance = duck.position().distanceToSqr(waterPosition(water));
                    if (distance < best) { nearest = new Places(water, shore); best = distance; }
                }
            }
        }
        return nearest;
    }
}
