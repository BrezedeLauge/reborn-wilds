package de.laugenbreze.dungeonsalive.world.water;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.phys.Vec3;

/** Read-only local channel sample. Never changes water blocks, heightmaps, terrain, or chunk loading. */
public final class WaterCurrentField {
    public record Sample(Vec3 direction, double speed, int width, int depth, boolean falling) {
        public static final Sample STILL = new Sample(Vec3.ZERO, 0, 0, 0, false);
        public Vec3 velocity() { return direction.scale(speed); }
    }
    private static final int RADIUS = 16;
    private WaterCurrentField() {}

    public static Sample sample(Level level, BlockPos origin) {
        // Fluid.getFlow itself inspects neighbours, so check the complete enclosing area first.
        if (!loaded(level, origin, RADIUS + 2) || !water(level, origin)) return Sample.STILL;
        BlockPos surface = origin;
        for (int n = 0; n < 12 && surface.getY() + 1 < level.getMaxBuildHeight() && water(level, surface.above()); n++) surface = surface.above();
        if (water(level, surface.above())) return Sample.STILL;
        var fluid = level.getFluidState(surface);
        Vec3 nativeFlow = fluid.getFlow(level, surface).multiply(1, 0, 1);
        boolean falling = fluid.hasProperty(FlowingFluid.FALLING) && fluid.getValue(FlowingFluid.FALLING);
        int west = extent(level, surface, Direction.WEST), east = extent(level, surface, Direction.EAST);
        int north = extent(level, surface, Direction.NORTH), south = extent(level, surface, Direction.SOUTH);
        int widthX = west + east + 1, widthZ = north + south + 1;
        int depth = depth(level, surface);
        boolean alongZ = widthX < widthZ;
        int width = alongZ ? widthX : widthZ;
        int bankDistance = 1 + Math.min(alongZ ? west : north, alongZ ? east : south);
        Vec3 direction;
        double slope = 0;
        if (nativeFlow.lengthSqr() > .0001) {
            direction = nativeFlow.normalize();
            // A real surface fall takes priority over an inferred river axis.
            slope = falling ? 1.5 : Math.max(0, 1 - fluid.getOwnHeight());
            width = Math.abs(direction.z) > Math.abs(direction.x) ? widthX : widthZ;
            bankDistance = 1 + (Math.abs(direction.z) > Math.abs(direction.x) ? Math.min(west, east) : Math.min(north, south));
        } else {
            // Lakes, oceans, pools and canals do not acquire an arbitrary current.
            // Flat vanilla river sources have no watershed direction: infer an axis only for a clear channel.
            if (!level.getBiome(surface).is(BiomeTags.IS_RIVER)
                    || Math.max(widthX, widthZ) < Math.min(widthX, widthZ) * 1.35 || width >= 29) return Sample.STILL;
            Direction forward = alongZ ? Direction.SOUTH : Direction.EAST;
            Direction backward = forward.getOpposite();
            int reach = Math.min(8, Math.min(alongZ ? north : west, alongZ ? south : east));
            int depthDifference = reach < 2 ? 0 : depth(level, surface.relative(forward, reach)) - depth(level, surface.relative(backward, reach));
            // Deeper downstream bed wins when visible locally. Otherwise a stable positive axis avoids per-block reversals.
            direction = Vec3.atLowerCornerOf((depthDifference < 0 ? backward : forward).getNormal());
            slope = Math.min(.5, Math.abs(depthDifference) / 16.0);
        }
        return new Sample(direction, CurrentStrength.speed(width, depth, slope, bankDistance), width, depth, falling);
    }

    private static int extent(Level level, BlockPos surface, Direction direction) {
        int n = 0;
        while (n < RADIUS && water(level, surface.relative(direction, n + 1))) n++;
        return n;
    }
    private static int depth(Level level, BlockPos surface) {
        int n = 0;
        while (n < 12 && surface.getY() - n >= level.getMinBuildHeight() && water(level, surface.below(n))) n++;
        return n;
    }
    private static boolean water(Level level, BlockPos pos) {
        // Waterlogged solid blocks are banks, not traversable channels.
        var state = level.getBlockState(pos);
        return state.getFluidState().is(FluidTags.WATER) && state.getCollisionShape(level, pos).isEmpty();
    }
    static boolean loaded(Level level, BlockPos pos, int radius) {
        for (int x = (pos.getX() - radius) >> 4; x <= (pos.getX() + radius) >> 4; x++)
            for (int z = (pos.getZ() - radius) >> 4; z <= (pos.getZ() + radius) >> 4; z++)
                if (level.getChunkSource().getChunkNow(x, z) == null) return false;
        return level.getWorldBorder().isWithinBounds(pos);
    }
}
