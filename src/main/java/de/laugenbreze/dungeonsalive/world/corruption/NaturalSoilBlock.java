package de.laugenbreze.dungeonsalive.world.corruption;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

/** Dirt-compatible reservation; loss of its natural cover discards the marker rather than changing any building. */
public final class NaturalSoilBlock extends Block {
    private static final MapCodec<NaturalSoilBlock> CODEC = simpleCodec(NaturalSoilBlock::new);
    public NaturalSoilBlock(Properties properties) { super(properties); }
    @Override protected MapCodec<? extends NaturalSoilBlock> codec() { return CODEC; }
    @Override protected BlockState updateShape(BlockState state, Direction direction, BlockState neighbor,
                                                LevelAccessor level, BlockPos position, BlockPos neighborPosition) {
        if (direction == Direction.UP && !neighbor.is(Blocks.GRASS_BLOCK) && !neighbor.is(CorruptionContent.BLIGHTED_GRASS.get()))
            return Blocks.DIRT.defaultBlockState();
        return super.updateShape(state, direction, neighbor, level, position, neighborPosition);
    }
}
