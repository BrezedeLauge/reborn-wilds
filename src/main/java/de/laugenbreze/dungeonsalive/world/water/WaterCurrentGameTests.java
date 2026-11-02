package de.laugenbreze.dungeonsalive.world.water;

import de.laugenbreze.dungeonsalive.DungeonsAlive;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

@GameTestHolder(DungeonsAlive.MOD_ID)
@PrefixGameTestTemplate(false)
public final class WaterCurrentGameTests {
    @GameTest(template = "gametest_empty", templateNamespace = DungeonsAlive.MOD_ID, batch = "water_currents", timeoutTicks = 20)
    public static void nativeDownhillFlowDrivesTheChannelAndWaterloggedBanksStaySolid(GameTestHelper helper) {
        var level = helper.getLevel();
        var origin = helper.absolutePos(new BlockPos(5, 3, 5));
        // The fixture deliberately provides the enclosing loaded area. Sampling itself must never load it.
        for (int x = (origin.getX() - 18) >> 4; x <= (origin.getX() + 18) >> 4; x++)
            for (int z = (origin.getZ() - 18) >> 4; z <= (origin.getZ() + 18) >> 4; z++) level.getChunk(x, z);
        for (BlockPos pos : BlockPos.betweenClosed(origin.offset(-3, -1, -3), origin.offset(3, -1, 3))) level.setBlock(pos, Blocks.STONE.defaultBlockState(), 2);
        for (BlockPos pos : BlockPos.betweenClosed(origin.offset(-3, 0, -3), origin.offset(3, 0, 3))) level.setBlock(pos, Blocks.STONE.defaultBlockState(), 2);
        level.setBlock(origin, Blocks.WATER.defaultBlockState().setValue(LiquidBlock.LEVEL, 1), 2);
        level.setBlock(origin.west(), Blocks.WATER.defaultBlockState(), 2);
        level.setBlock(origin.east(), Blocks.WATER.defaultBlockState().setValue(LiquidBlock.LEVEL, 5), 2);
        var nativeFlow = level.getFluidState(origin).getFlow(level, origin).multiply(1, 0, 1).normalize();
        var sample = WaterCurrentField.sample(level, origin);
        helper.assertTrue(sample.speed() > 0 && nativeFlow.lengthSqr() > 0 && sample.direction().dot(nativeFlow) > .99,
                "Channel current did not follow the actual vanilla downhill water flow");
        level.setBlock(origin, Blocks.OAK_SLAB.defaultBlockState().setValue(BlockStateProperties.WATERLOGGED, true), 2);
        helper.assertTrue(WaterCurrentField.sample(level, origin).speed() == 0, "Waterlogged solid slab became a traversable stream");
        helper.succeed();
    }
    @GameTest(template = "gametest_empty", templateNamespace = DungeonsAlive.MOD_ID, batch = "water_currents", timeoutTicks = 20)
    public static void unknownChunksAndStillNonRiverPoolsDoNotGetInventedCurrents(GameTestHelper helper) {
        var level = helper.getLevel();
        var origin = helper.absolutePos(new BlockPos(5, 3, 5));
        var unknown = origin.offset(20_000_000, 0, 0);
        helper.assertTrue(level.getChunkSource().getChunkNow(unknown.getX() >> 4, unknown.getZ() >> 4) == null, "Fixture target was loaded");
        helper.assertTrue(WaterCurrentField.sample(level, unknown).speed() == 0, "Unknown terrain produced current");
        helper.assertTrue(level.getChunkSource().getChunkNow(unknown.getX() >> 4, unknown.getZ() >> 4) == null, "Sampling loaded an unknown chunk");
        for (int x = (origin.getX() - 18) >> 4; x <= (origin.getX() + 18) >> 4; x++)
            for (int z = (origin.getZ() - 18) >> 4; z <= (origin.getZ() + 18) >> 4; z++) level.getChunk(x, z);
        helper.assertTrue(WaterCurrentField.loaded(level, origin, 18), "Pool fixture failed to provide the full sampler area");
        for (BlockPos pos : BlockPos.betweenClosed(origin.offset(-3, 0, -3), origin.offset(3, 0, 3))) level.setBlock(pos, Blocks.WATER.defaultBlockState(), 2);
        helper.assertTrue(WaterCurrentField.sample(level, origin).speed() == 0, "A still symmetric pool acquired an invented downstream direction");
        level.setBlock(origin, Blocks.WATER.defaultBlockState().setValue(LiquidBlock.LEVEL, 1), 2);
        level.setBlock(origin.east(), Blocks.WATER.defaultBlockState().setValue(LiquidBlock.LEVEL, 5), 2);
        helper.assertTrue(WaterCurrentField.sample(level, origin).speed() > 0, "Pool test was vacuously still rather than reading loaded fluid geometry");
        helper.succeed();
    }
    private WaterCurrentGameTests() {}
}
