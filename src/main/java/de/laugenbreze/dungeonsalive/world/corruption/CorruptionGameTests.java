package de.laugenbreze.dungeonsalive.world.corruption;

import com.mojang.authlib.GameProfile;
import de.laugenbreze.dungeonsalive.DungeonsAlive;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.gametest.framework.AfterBatch;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.util.FakePlayer;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

/** Actual loaded blocks, real generation feature, player placement, focus use and original-state restoration. */
@GameTestHolder(DungeonsAlive.MOD_ID)
@PrefixGameTestTemplate(false)
public final class CorruptionGameTests {
    private static final Map<ServerLevel, CompoundTag> HISTORY = new IdentityHashMap<>();
    private static final Map<ServerLevel, Set<ChunkPos>> CHUNKS = new IdentityHashMap<>();
    private CorruptionGameTests() {}
    @AfterBatch(batch = "corruption_restore") public static void cleanRestore(ServerLevel level) { cleanup(level); }
    @AfterBatch(batch = "corruption_bounds") public static void cleanBounds(ServerLevel level) { cleanup(level); }
    private static void cleanup(ServerLevel level) {
        var previous = HISTORY.remove(level);
        if (previous != null) level.getServer().overworld().getDataStorage().set(CorruptionSavedData.DATA_NAME,
                CorruptionSavedData.load(previous, level.registryAccess()));
        var chunks = CHUNKS.remove(level);
        if (chunks != null) for (var chunk : chunks) level.setChunkForced(chunk.x, chunk.z, false);
    }

    @GameTest(template = "gametest_empty", templateNamespace = DungeonsAlive.MOD_ID, batch = "corruption_restore", skyAccess = true, timeoutTicks = 30)
    public static void realNaturalFrontSavesRestoresAndLeavesPlayerConstructionUnchanged(GameTestHelper helper) {
        fixture(helper); var level = helper.getLevel(); var centre = helper.absolutePos(new BlockPos(10, 2, 6));
        helper.assertTrue(level.registryAccess().registryOrThrow(Registries.BIOME).get(CorruptionContent.BIOME.location()) != null,
                "The actual blighted biome did not load");
        var feature = level.registryAccess().registryOrThrow(Registries.CONFIGURED_FEATURE)
                .get(ResourceLocation.fromNamespaceAndPath(DungeonsAlive.MOD_ID, "corruption/natural_soil"));
        helper.assertTrue(feature != null, "Missing actual natural-soil generation feature");
        for (int x = -2; x <= 2; x++) for (int z = -2; z <= 2; z++) {
            var marker = centre.offset(x, -1, z);
            helper.assertTrue(feature.place(level, level.getChunkSource().getGenerator(), RandomSource.create(1), marker)
                            && level.getBlockState(marker).is(CorruptionContent.NATURAL_SOIL.get()),
                    "Actual configured feature did not reserve the original dirt beneath grass");
        }
        var data = CorruptionSavedData.get(level.getServer());
        var region = data.discover(level.dimension().location().toString(), centre);
        helper.assertTrue(region != null, "Explicit discovery fixture did not reserve a bounded region");
        CorruptionEvents.advance(level.getServer());
        helper.assertTrue(region.patches() > 0 && region.patches() <= CorruptionFront.CHANGES_PER_PULSE
                        && level.getBlockState(centre).is(CorruptionContent.BLIGHTED_GRASS.get()),
                "A server pulse did not produce a real, bounded natural front");
        CompoundTag saved = data.save(new CompoundTag(), level.registryAccess());
        var restored = CorruptionSavedData.load(saved, level.registryAccess());
        helper.assertFalse(restored.frozen(), "Valid original-state NBT failed to restore");
        level.getServer().overworld().getDataStorage().set(CorruptionSavedData.DATA_NAME, restored);
        region = restored.nearby(level.dimension().location().toString(), centre.getX(), centre.getZ(), 1);
        helper.assertTrue(region != null && region.front.grown() == data.regions.getFirst().front.grown(), "Reload reset growth history");
        var player = new FakePlayer(level, new GameProfile(UUID.randomUUID(), "corruption-test"));
        player.getAbilities().instabuild = false; player.getAbilities().mayBuild = true;
        player.setPos(Vec3.atBottomCenterOf(centre.offset(0, 1, -3)));
        var building = new ItemStack(Items.OAK_PLANKS, 2); player.setItemInHand(InteractionHand.MAIN_HAND, building);
        helper.assertTrue(building.useOn(context(player, centre)).consumesAction() && level.getBlockState(centre.above()).is(Blocks.OAK_PLANKS),
                "The actual player BlockItem placement did not execute");
        helper.assertTrue(restored.protectedAt(level.dimension().location().toString(), centre.getX(), centre.getZ())
                        && level.getBlockState(centre.below()).is(Blocks.DIRT), "Player construction did not permanently invalidate its natural reservation");
        CorruptionEvents.advance(level.getServer());
        int untouched = (int)region.originals.keySet().stream().filter(pos -> !restored.protectedAt(regionDimension(level), pos.getX(), pos.getZ())).count();
        helper.assertTrue(untouched > 0, "Fixture lacks untouched corrupted nature to restore");
        var expected = new java.util.HashMap<>(region.originals);
        var focus = new ItemStack(CorruptionContent.FOCUS.get(), 2); player.setItemInHand(InteractionHand.MAIN_HAND, focus);
        helper.assertTrue(focus.getItem().useOn(context(player, centre)) == InteractionResult.CONSUME && focus.getCount() == 1,
                "A real focus did not stop the actual region and consume exactly once");
        helper.assertTrue(!region.front.canGrow(), "Focus left the region growing while cleanup was pending");
        for (int pulse = 0; pulse < 4; pulse++) CorruptionEvents.advance(level.getServer());
        helper.assertTrue(region.front.stage() == CorruptionFront.Stage.CLEANSED && region.patches() == 0,
                "Loaded original natural states did not finish restoring");
        expected.forEach((position, original) -> {
            if (!restored.protectedAt(regionDimension(level), position.getX(), position.getZ()))
                helper.assertTrue(level.getBlockState(position).equals(original), "Restoration changed the original natural block properties");
        });
        helper.assertTrue(level.getBlockState(centre.above()).is(Blocks.OAK_PLANKS)
                        && level.getBlockState(centre).is(CorruptionContent.BLIGHTED_GRASS.get()),
                "Automatic cleanup modified the player's protected construction column");
        helper.assertTrue(focus.getItem().useOn(context(player, centre)) == InteractionResult.FAIL && focus.getCount() == 1,
                "Reusing a focus on a cleansed region charged or restarted it");
        helper.succeed();
    }

    @GameTest(template = "gametest_empty", templateNamespace = DungeonsAlive.MOD_ID, batch = "corruption_bounds", skyAccess = true, timeoutTicks = 30)
    public static void unreservedGroundAndUnknownChunksRemainUntouchedAndRegionCountIsBounded(GameTestHelper helper) {
        fixture(helper); var level = helper.getLevel(); var data = CorruptionSavedData.get(level.getServer());
        var centre = helper.absolutePos(new BlockPos(10, 2, 6));
        var local = data.discover(regionDimension(level), centre);
        helper.assertTrue(local != null, "Could not activate the explicitly placed region fixture");
        CorruptionEvents.advance(level.getServer());
        helper.assertTrue(local.patches() == 0 && level.getBlockState(centre).is(Blocks.GRASS_BLOCK),
                "Unreserved ordinary/player ground was guessed to be natural and changed");
        BlockPos missing = null;
        for (int distance : new int[]{512, 1024, 2048, 4096}) {
            var candidate = centre.offset(distance, 0, distance);
            if (!level.hasChunk(candidate.getX() >> 4, candidate.getZ() >> 4)) { missing = candidate; break; }
        }
        helper.assertTrue(missing != null, "No unloaded test candidate");
        var remote = data.discover(regionDimension(level), missing);
        helper.assertTrue(remote != null, "Could not construct the unloaded-front save fixture");
        CorruptionEvents.advance(level.getServer());
        helper.assertTrue(remote.front.cursor() == 0 && remote.patches() == 0
                        && !level.hasChunk(missing.getX() >> 4, missing.getZ() >> 4),
                "A front loaded an unknown chunk or skipped the saved resume cursor");
        for (int i = 2; i < CorruptionFront.MAX_REGIONS; i++)
            helper.assertTrue(data.discover(regionDimension(level), centre.offset(i * 10_000, 0, 0)) != null, "Region budget was consumed too early");
        helper.assertTrue(data.discover(regionDimension(level), centre.offset(200_000, 0, 0)) == null,
                "Discovery exceeded the hard world region limit");
        CompoundTag unknown = new CompoundTag(); unknown.putInt("Version", 999); unknown.putString("Unknown", "preserve");
        var frozen = CorruptionSavedData.load(unknown, level.registryAccess());
        helper.assertTrue(frozen.growthDisabled() && frozen.save(new CompoundTag(), level.registryAccess()).equals(unknown),
                "Unknown history was acted upon or overwritten");
        helper.succeed();
    }

    private static String regionDimension(ServerLevel level) { return level.dimension().location().toString(); }
    private static UseOnContext context(FakePlayer player, BlockPos floor) {
        return new UseOnContext(player, InteractionHand.MAIN_HAND,
                new BlockHitResult(Vec3.atCenterOf(floor).add(0, .5, 0), Direction.UP, floor, false));
    }
    private static void fixture(GameTestHelper helper) {
        var level = helper.getLevel();
        HISTORY.putIfAbsent(level, CorruptionSavedData.get(level.getServer()).save(new CompoundTag(), level.registryAccess()));
        level.getServer().overworld().getDataStorage().set(CorruptionSavedData.DATA_NAME, new CorruptionSavedData());
        for (int x = 0; x < 20; x++) for (int z = 0; z < 12; z++) {
            helper.setBlock(new BlockPos(x, 0, z), Blocks.STONE);
            helper.setBlock(new BlockPos(x, 1, z), Blocks.DIRT);
            helper.setBlock(new BlockPos(x, 2, z), Blocks.GRASS_BLOCK);
        }
        var added = CHUNKS.computeIfAbsent(level, ignored -> new HashSet<>());
        BlockPos min = helper.absolutePos(new BlockPos(-8, 0, -8)), max = helper.absolutePos(new BlockPos(27, 0, 19));
        for (int x = min.getX() >> 4; x <= max.getX() >> 4; x++) for (int z = min.getZ() >> 4; z <= max.getZ() >> 4; z++) {
            if (level.setChunkForced(x, z, true)) added.add(new ChunkPos(x, z)); level.getChunk(x, z);
        }
    }
}
