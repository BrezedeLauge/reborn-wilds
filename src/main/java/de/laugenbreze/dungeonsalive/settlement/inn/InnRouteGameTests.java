package de.laugenbreze.dungeonsalive.settlement.inn;

import de.laugenbreze.dungeonsalive.DungeonsAlive;
import de.laugenbreze.dungeonsalive.entity.peaceful.DungeonsMerchant;
import de.laugenbreze.dungeonsalive.entity.peaceful.ModPeacefulEntities;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.AfterBatch;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

/** Draft native regression: real bridge traversal, then an honest broken-route diagnosis. Not yet executed. */
@GameTestHolder(DungeonsAlive.MOD_ID)
@PrefixGameTestTemplate(false)
public final class InnRouteGameTests {
    private static final String BATCH = "inn_draft_route";
    private static final Map<ServerLevel, Set<Entity>> ACTORS = new IdentityHashMap<>();
    private static final Map<ServerLevel, Set<ChunkPos>> CHUNKS = new IdentityHashMap<>();
    private InnRouteGameTests() { }
    @AfterBatch(batch = BATCH) public static void cleanup(ServerLevel level) {
        var actors = ACTORS.remove(level); if (actors != null) actors.forEach(Entity::discard);
        var chunks = CHUNKS.remove(level); if (chunks != null) chunks.forEach(chunk -> level.setChunkForced(chunk.x, chunk.z, false));
    }

    @GameTest(template = "gametest_empty", templateNamespace = DungeonsAlive.MOD_ID, batch = BATCH, timeoutTicks = 420)
    public static void actualMerchantCrossesBuiltBridgeThenRemovedSpanIsDiagnosedWithoutLoadingUnknownChunks(GameTestHelper helper) {
        for (int x = 0; x < 20; x++) for (int z = 0; z < 12; z++) {
            helper.setBlock(new BlockPos(x, 0, z), Blocks.STONE);
            helper.setBlock(new BlockPos(x, 1, z), x >= 8 && x <= 10 && (z < 5 || z > 6) ? Blocks.WATER : Blocks.OAK_PLANKS);
            for (int y = 2; y < 5; y++) helper.setBlock(new BlockPos(x, y, z), Blocks.AIR);
        }
        var level = helper.getLevel(); forceSearchChunks(helper);
        var merchant = ModPeacefulEntities.merchantType("village_merchant").create(level);
        helper.assertTrue(merchant != null, "Missing real existing merchant type");
        merchant.setPos(helper.absoluteVec(new Vec3(3.5, 2, 5.5)));
        merchant.addTag(DungeonsMerchant.CAMP_TAG); merchant.removeFreeWill();
        ACTORS.computeIfAbsent(level, ignored -> new HashSet<>()).add(merchant);
        helper.assertTrue(level.addFreshEntity(merchant), "Actual bridge actor admission failed");
        var min = helper.absolutePos(new BlockPos(12, 1, 3)); var max = helper.absolutePos(new BlockPos(19, 4, 9));
        var inn = new InnRegistry.Inn(UUID.randomUUID(), UUID.randomUUID(), level.dimension().location().toString(),
                InnRouteValidator.point(helper.absolutePos(new BlockPos(16, 2, 4))),
                new InnBounds(min.getX(), min.getY(), min.getZ(), max.getX(), max.getY(), max.getZ()),
                InnRouteValidator.point(helper.absolutePos(new BlockPos(15, 2, 5))),
                InnRouteValidator.point(helper.absolutePos(new BlockPos(17, 2, 5))),
                InnRouteValidator.point(helper.absolutePos(new BlockPos(3, 2, 5))), 1, null, 0, 0, false);
        var checked = new InnRouteValidator.Check[1]; Vec3[] previous = {merchant.position()};
        boolean[] moving = {false}, started = {false}, finished = {false};
        helper.runAtTickTime(4, () -> {
            checked[0] = InnRouteValidator.inspect(level, inn, List.of());
            helper.assertTrue(checked[0].loaded() && checked[0].connected(), "The actual built bridge was not walkable");
            var actual = InnRouteValidator.plan(merchant, InnRouteValidator.pos(inn.entrance()));
            helper.assertTrue(actual.connected() && actual.path() != null && merchant.getNavigation().moveTo(actual.path(), .75),
                    "Real merchant could not start the verified bridge path");
            started[0] = true;
        });
        helper.onEachTick(() -> {
            if (finished[0] || !started[0]) return;
            helper.assertTrue(merchant.isAlive() && merchant.position().distanceToSqr(previous[0]) < 1, "Bridge actor died or teleported");
            moving[0] |= !merchant.getNavigation().isDone(); previous[0] = merchant.position();
            if (!merchant.onGround() || merchant.position().distanceToSqr(Vec3.atBottomCenterOf(InnRouteValidator.pos(inn.entrance()))) > 1.21) return;
            helper.assertTrue(moving[0] && merchant.distanceToSqr(helper.absoluteVec(new Vec3(3.5, 2, 5.5))) > 100,
                    "No actual substantial bridge traversal");
            helper.setBlock(new BlockPos(9, 1, 5), Blocks.WATER); helper.setBlock(new BlockPos(9, 1, 6), Blocks.WATER);
            var broken = InnRouteValidator.inspect(level, inn, checked[0].nodes());
            helper.assertTrue(broken.loaded() && !broken.connected() && broken.problem() != null
                            && broken.problem().getX() == helper.absolutePos(new BlockPos(9, 2, 5)).getX(),
                    "Removed known bridge span has no honest current failure position");
            BlockPos absent = null;
            for (int offset : new int[]{512, 1024, 2048}) {
                var candidate = merchant.blockPosition().offset(offset, 0, offset);
                if (!level.hasChunk(candidate.getX() >> 4, candidate.getZ() >> 4)) { absent = candidate; break; }
            }
            helper.assertTrue(absent != null, "No unloaded bounded negative target available");
            helper.assertFalse(InnRouteValidator.plan(merchant, absent).loaded(), "Unknown route target was accepted");
            helper.assertFalse(level.hasChunk(absent.getX() >> 4, absent.getZ() >> 4), "Route rejection loaded a chunk");
            finished[0] = true; helper.succeed();
        });
        helper.runAtTickTime(410, () -> helper.assertTrue(finished[0], "Actual bridge crossing did not finish: " + merchant.position()));
    }

    private static void forceSearchChunks(GameTestHelper helper) {
        var level = helper.getLevel(); int radius = InnRouteValidator.LOADED_RADIUS;
        var min = helper.absolutePos(new BlockPos(-radius, 0, -radius)); var max = helper.absolutePos(new BlockPos(19 + radius, 0, 11 + radius));
        var added = CHUNKS.computeIfAbsent(level, ignored -> new HashSet<>());
        for (int x = min.getX() >> 4; x <= max.getX() >> 4; x++) for (int z = min.getZ() >> 4; z <= max.getZ() >> 4; z++) {
            if (level.setChunkForced(x, z, true)) added.add(new ChunkPos(x, z)); level.getChunk(x, z);
        }
    }
}
