package de.laugenbreze.dungeonsalive.world.trail;

import com.mojang.authlib.GameProfile;
import de.laugenbreze.dungeonsalive.DungeonsAlive;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.gametest.framework.AfterBatch;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.util.FakePlayer;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

/** Real block/item/event/NBT contracts. Old timestamps model saved inactive trails without changing the world's clock. */
@GameTestHolder(DungeonsAlive.MOD_ID)
@PrefixGameTestTemplate(false)
public final class NativeTrailGameTests {
    private static final String BATCH = "natural_trails";
    private static final Map<ServerLevel, CompoundTag> HISTORY = new IdentityHashMap<>();
    private static final Map<ServerLevel, Map<BlockPos, BlockState>> BLOCKS = new IdentityHashMap<>();
    private static Config originalConfig;
    private record Config(boolean enabled, int wear, int path, int regrow) {}
    private NativeTrailGameTests() {}

    @AfterBatch(batch = BATCH)
    public static void cleanup(ServerLevel level) {
        var history = HISTORY.remove(level);
        if (history != null) level.getDataStorage().set("dungeonsalive_trails", TrailSavedData.load(history, level.registryAccess()));
        var blocks = BLOCKS.remove(level);
        if (blocks != null) blocks.forEach((pos, state) -> level.setBlock(pos, state, Block.UPDATE_ALL));
        if (originalConfig != null) {
            NaturalTrails.ENABLED.set(originalConfig.enabled()); NaturalTrails.WEAR_STEPS.set(originalConfig.wear());
            NaturalTrails.PATH_STEPS.set(originalConfig.path()); NaturalTrails.REGROW_TICKS.set(originalConfig.regrow());
            originalConfig = null;
        }
    }

    @GameTest(template = "gametest_empty", templateNamespace = DungeonsAlive.MOD_ID, batch = BATCH, skyAccess = true)
    public static void realCrossingsWearGroundAndRecordedInactivePathsRecoverBothStages(GameTestHelper helper) {
        fixture(helper);
        ServerLevel level = helper.getLevel();
        BlockPos a = block(helper, new BlockPos(4, 0, 4), Blocks.PODZOL.defaultBlockState());
        BlockPos b = block(helper, new BlockPos(5, 0, 4), Blocks.GRASS_BLOCK.defaultBlockState());
        FakePlayer player = player(helper);
        cross(player, a); // First observed standing position is not a crossing.
        for (int i = 0; i < 8; i++) cross(player, a);
        helper.assertFalse(TrailSavedData.get(level).footprints.containsKey(a.asLong()), "Standing still was counted as repeated crossings");
        cross(player, b); cross(player, a);
        helper.assertTrue(level.getBlockState(a).is(Blocks.PODZOL), "One crossing prematurely wore the ground");
        cross(player, b); cross(player, a);
        helper.assertTrue(level.getBlockState(a).is(Blocks.COARSE_DIRT), "Repeated crossings did not create actual worn ground");
        for (int i = 0; i < 2; i++) { cross(player, b); cross(player, a); }
        helper.assertTrue(level.getBlockState(a).is(Blocks.DIRT_PATH), "Repeated crossings did not create an actual dirt path");
        var data = TrailSavedData.get(level);
        data.put(a.asLong(), new FootTraffic("minecraft:podzol", 4, 2, level.getGameTime() - 200));
        // This invokes the same loaded-block processor as the registered level tick.
        NaturalTrails.recoverLoaded(level);
        helper.assertTrue(level.getBlockState(a).is(Blocks.COARSE_DIRT), "First recovery interval did not soften the real path");
        data.put(a.asLong(), new FootTraffic("minecraft:podzol", 4, 1, level.getGameTime() - 400));
        NaturalTrails.ENABLED.set(false); // Disabling new wear must not strand existing paths.
        NaturalTrails.recoverLoaded(level);
        helper.assertTrue(level.getBlockState(a).is(Blocks.PODZOL) && !data.footprints.containsKey(a.asLong()),
                "Inactive generated path did not return to its original podzol and release its sparse history");
        NaturalTrails.ENABLED.set(true);
        helper.succeed();
    }

    @GameTest(template = "gametest_empty", templateNamespace = DungeonsAlive.MOD_ID, batch = BATCH, skyAccess = true)
    public static void playerPlacedSoilAndOrdinaryRoadsAreNotEnrolled(GameTestHelper helper) {
        fixture(helper);
        ServerLevel level = helper.getLevel();
        BlockPos support = block(helper, new BlockPos(9, 0, 4), Blocks.STONE.defaultBlockState());
        BlockPos placed = block(helper, new BlockPos(9, 1, 4), Blocks.AIR.defaultBlockState());
        block(helper, new BlockPos(9, 2, 4), Blocks.AIR.defaultBlockState());
        FakePlayer player = player(helper);
        player.setPos(Vec3.atBottomCenterOf(placed).add(-2, 0, 0));
        ItemStack soil = new ItemStack(Items.GRASS_BLOCK, 2);
        player.setItemInHand(InteractionHand.MAIN_HAND, soil);
        var use = new UseOnContext(player, InteractionHand.MAIN_HAND,
                new BlockHitResult(Vec3.atCenterOf(support).add(0, .5, 0), Direction.UP, support, false));
        helper.assertTrue(soil.useOn(use).consumesAction() && level.getBlockState(placed).is(Blocks.GRASS_BLOCK),
                "The fixture did not place soil through the actual item/placement event pipeline");
        helper.assertTrue(TrailSavedData.get(level).playerGround.contains(placed.asLong()), "Actual player placement was not protected");
        BlockPos road = block(helper, new BlockPos(11, 0, 4), Blocks.DIRT_PATH.defaultBlockState());
        BlockPos farm = block(helper, new BlockPos(12, 0, 4), Blocks.FARMLAND.defaultBlockState());
        for (int i = 0; i < 20; i++) {
            NaturalTrails.step(level, placed); NaturalTrails.step(level, road); NaturalTrails.step(level, farm);
        }
        NaturalTrails.recoverLoaded(level);
        helper.assertTrue(level.getBlockState(placed).is(Blocks.GRASS_BLOCK), "Player-placed soil was worn");
        helper.assertTrue(level.getBlockState(road).is(Blocks.DIRT_PATH) && level.getBlockState(farm).is(Blocks.FARMLAND),
                "An ordinary road or farmland was changed by trail processing");
        helper.assertFalse(TrailSavedData.get(level).footprints.containsKey(road.asLong())
                || TrailSavedData.get(level).footprints.containsKey(farm.asLong()), "Ordinary roads/farmland gained regrowth history");
        helper.succeed();
    }

    @GameTest(template = "gametest_empty", templateNamespace = DungeonsAlive.MOD_ID, batch = BATCH, skyAccess = true)
    public static void canceledWearRegrowthAndActualBlockBreakRetainTheGroundAndHistory(GameTestHelper helper) {
        fixture(helper);
        ServerLevel level = helper.getLevel();
        BlockPos pos = block(helper, new BlockPos(4, 0, 4), Blocks.GRASS_BLOCK.defaultBlockState());
        boolean[] observed = {false};
        Consumer<TrailChangeEvent> veto = event -> {
            if (event.level == level && event.position.equals(pos)) { observed[0] = true; event.setCanceled(true); }
        };
        NeoForge.EVENT_BUS.addListener(EventPriority.LOWEST, veto);
        try {
            for (int i = 0; i < 4; i++) NaturalTrails.step(level, pos);
            helper.assertTrue(observed[0] && level.getBlockState(pos).is(Blocks.GRASS_BLOCK)
                    && TrailSavedData.get(level).footprints.get(pos.asLong()).stage() == 0,
                    "A canceled automatic wear event changed the real ground or its recorded stage");
        } finally { NeoForge.EVENT_BUS.unregister(veto); }
        NaturalTrails.step(level, pos);
        helper.assertTrue(level.getBlockState(pos).is(Blocks.DIRT_PATH), "Allowed wear did not resume after cancellation");

        boolean[] breakObserved = {false};
        Consumer<BlockEvent.BreakEvent> cancelBreak = event -> {
            if (event.getLevel() == level && event.getPos().equals(pos)) { breakObserved[0] = true; event.setCanceled(true); }
        };
        NeoForge.EVENT_BUS.addListener(EventPriority.LOWEST, cancelBreak);
        try {
            FakePlayer player = player(helper); player.setPos(Vec3.atBottomCenterOf(pos.above()));
            helper.assertFalse(player.gameMode.destroyBlock(pos), "Canceled actual player break unexpectedly succeeded");
            helper.assertTrue(breakObserved[0] && level.getBlockState(pos).is(Blocks.DIRT_PATH)
                    && TrailSavedData.get(level).footprints.containsKey(pos.asLong()),
                    "Canceled player breaking erased the real generated path's recovery history");
        } finally { NeoForge.EVENT_BUS.unregister(cancelBreak); }

        TrailSavedData.get(level).put(pos.asLong(), new FootTraffic("minecraft:grass_block", 4, 2, level.getGameTime() - 400));
        observed[0] = false;
        NeoForge.EVENT_BUS.addListener(EventPriority.LOWEST, veto);
        try {
            NaturalTrails.recoverLoaded(level);
            helper.assertTrue(observed[0] && level.getBlockState(pos).is(Blocks.DIRT_PATH)
                    && TrailSavedData.get(level).footprints.get(pos.asLong()).stage() == 2,
                    "Canceled recovery changed the path or consumed its history");
        } finally { NeoForge.EVENT_BUS.unregister(veto); }
        NaturalTrails.recoverLoaded(level);
        helper.assertTrue(level.getBlockState(pos).is(Blocks.GRASS_BLOCK)
                && !TrailSavedData.get(level).footprints.containsKey(pos.asLong()), "Allowed recovery did not resume");
        helper.succeed();
    }

    @GameTest(template = "gametest_empty", templateNamespace = DungeonsAlive.MOD_ID, batch = BATCH, skyAccess = true)
    public static void savedClockRecoveryProtectedConflictAndUnloadedHistoryRemainBounded(GameTestHelper helper) {
        fixture(helper);
        ServerLevel level = helper.getLevel();
        BlockPos clock = block(helper, new BlockPos(4, 0, 4), Blocks.DIRT_PATH.defaultBlockState());
        BlockPos protectedGround = block(helper, new BlockPos(6, 0, 4), Blocks.DIRT_PATH.defaultBlockState());
        BlockPos unloaded = new BlockPos(12_000_000, 100, 12_000_000);
        helper.assertTrue(level.getChunkSource().getChunkNow(unloaded.getX() >> 4, unloaded.getZ() >> 4) == null,
                "The deliberately distant chunk was already loaded before the test");
        var data = TrailSavedData.get(level);
        data.put(clock.asLong(), new FootTraffic("minecraft:podzol", 4, 2, Long.MAX_VALUE));
        data.put(protectedGround.asLong(), new FootTraffic("minecraft:mycelium", 4, 2, level.getGameTime() - 400));
        data.put(unloaded.asLong(), new FootTraffic("minecraft:grass_block", 4, 2, level.getGameTime() - 400));
        // Deliberately contradictory saved data: protection wins over a generated-path record.
        data.playerGround.add(protectedGround.asLong());
        CompoundTag saved = data.save(new CompoundTag(), level.registryAccess());
        var restored = TrailSavedData.load(saved, level.registryAccess());
        level.getDataStorage().set("dungeonsalive_trails", restored);
        helper.assertTrue(restored.playerGround.contains(protectedGround.asLong())
                && !restored.footprints.containsKey(protectedGround.asLong())
                && !restored.pending.contains(protectedGround.asLong()), "NBT load retained a trail that conflicts with player protection");
        NaturalTrails.step(level, unloaded);
        NaturalTrails.recoverLoaded(level);
        helper.assertTrue(level.getChunkSource().getChunkNow(unloaded.getX() >> 4, unloaded.getZ() >> 4) == null
                && restored.footprints.get(unloaded.asLong()).stage() == 2
                && restored.pending.contains(unloaded.asLong()), "Trail work loaded an unknown chunk or discarded its pending saved history");
        helper.assertTrue(restored.isDirty() && restored.footprints.get(clock.asLong()).lastStep() == level.getGameTime()
                && level.getBlockState(clock).is(Blocks.DIRT_PATH), "A future saved clock was not rebased with a full recovery grace period");
        var secondLoad = TrailSavedData.load(restored.save(new CompoundTag(), level.registryAccess()), level.registryAccess());
        helper.assertTrue(secondLoad.footprints.get(clock.asLong()).lastStep() == level.getGameTime()
                && secondLoad.footprints.get(clock.asLong()).original().equals("minecraft:podzol"), "Corrected clock or original ground was lost across NBT round-trip");
        helper.assertTrue(level.getBlockState(protectedGround).is(Blocks.DIRT_PATH), "Conflicting saved history regrew protected ground");
        helper.succeed();
    }

    private static void fixture(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        if (!HISTORY.containsKey(level)) {
            HISTORY.put(level, TrailSavedData.get(level).save(new CompoundTag(), level.registryAccess()));
            level.getDataStorage().set("dungeonsalive_trails", new TrailSavedData());
        }
        if (originalConfig == null) originalConfig = new Config(NaturalTrails.ENABLED.get(), NaturalTrails.WEAR_STEPS.get(),
                NaturalTrails.PATH_STEPS.get(), NaturalTrails.REGROW_TICKS.get());
        NaturalTrails.ENABLED.set(true); NaturalTrails.WEAR_STEPS.set(2); NaturalTrails.PATH_STEPS.set(4); NaturalTrails.REGROW_TICKS.set(200);
    }
    private static BlockPos block(GameTestHelper helper, BlockPos local, BlockState state) {
        ServerLevel level = helper.getLevel(); BlockPos pos = helper.absolutePos(local);
        BLOCKS.computeIfAbsent(level, ignored -> new HashMap<>()).putIfAbsent(pos.immutable(), level.getBlockState(pos));
        level.setBlock(pos, state, Block.UPDATE_ALL);
        return pos;
    }
    private static FakePlayer player(GameTestHelper helper) {
        var player = new FakePlayer(helper.getLevel(), new GameProfile(UUID.randomUUID(), "trail-test"));
        player.setGameMode(GameType.SURVIVAL); player.getAbilities().mayBuild = true;
        player.getAbilities().instabuild = false; player.getAbilities().flying = false;
        return player; // No level/player-list admission, sockets or player save are needed for these item/tick hooks.
    }
    private static void cross(FakePlayer player, BlockPos floor) {
        player.setPos(Vec3.atBottomCenterOf(floor.above())); player.setOnGround(true); player.tickCount = 4;
        NeoForge.EVENT_BUS.post(new EntityTickEvent.Post(player));
    }
}
