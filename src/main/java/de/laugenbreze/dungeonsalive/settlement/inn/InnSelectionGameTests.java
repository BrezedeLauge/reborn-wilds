package de.laugenbreze.dungeonsalive.settlement.inn;

import com.mojang.authlib.GameProfile;
import de.laugenbreze.dungeonsalive.DungeonsAlive;
import io.netty.buffer.Unpooled;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.OptionalInt;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.gametest.framework.AfterBatch;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.common.util.FakePlayer;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;
import org.jetbrains.annotations.Nullable;

/** Real registered block/menu/BlockEntity and server selection transactions, including stale placement protection. */
@GameTestHolder(DungeonsAlive.MOD_ID)
@PrefixGameTestTemplate(false)
public final class InnSelectionGameTests {
    private static final String BATCH = "inn_selection";
    private static final Map<ServerLevel, Set<ChunkPos>> CHUNKS = new IdentityHashMap<>();
    private static final Map<ServerLevel, Set<UUID>> INNS = new IdentityHashMap<>();
    private InnSelectionGameTests() { }
    @AfterBatch(batch = BATCH) public static void cleanup(ServerLevel level) {
        var ids = INNS.remove(level);
        if (ids != null) for (var id : ids) {
            var data = InnSavedData.get(level.getServer()); var inn = data.find(id);
            if (inn != null) data.remove(id, inn.owner(), inn.revision());
        }
        var chunks = CHUNKS.remove(level); if (chunks != null) chunks.forEach(p -> level.setChunkForced(p.x, p.z, false));
    }
    @GameTest(template = "gametest_empty", templateNamespace = DungeonsAlive.MOD_ID, batch = BATCH, timeoutTicks = 40)
    public static void actualOwnerPlanCommitsOnceAndItsReadOnlyOpeningSnapshotRoundTrips(GameTestHelper helper) {
        var owner = setup(helper); select(helper, owner);
        BlockPos pos = helper.absolutePos(new BlockPos(3, 1, 3)); owner.setPos(pos.getCenter().add(0, 0, 2));
        helper.assertTrue(InnContent.open(owner, pos) && owner.containerMenu instanceof InnMenu, "Registered inn provider did not open its real menu");
        var menu = (InnMenu)owner.containerMenu; menu.broadcastChanges();
        helper.assertTrue(menu.canCommit(), "Complete server-authored plan could not be confirmed");
        var buffer = new RegistryFriendlyByteBuf(Unpooled.buffer(), helper.getLevel().registryAccess());
        try { menu.plan().write(buffer); helper.assertTrue(InnPlanView.read(buffer).equals(menu.plan()), "Menu opening snapshot lost placement, token or full coordinates"); }
        finally { buffer.release(); }
        var stranger = new MenuPlayer(helper.getLevel()); stranger.setGameMode(GameType.SURVIVAL); stranger.setPos(owner.position());
        helper.assertFalse(menu.clickMenuButton(stranger, 0), "Another player committed the owner's current plan");
        helper.assertTrue(menu.clickMenuButton(owner, 0) && menu.result() == InnService.Code.OK, "Valid real owner transaction failed");
        var anchor = (InnAnchorBlockEntity)helper.getLevel().getBlockEntity(pos);
        helper.assertTrue(anchor != null && anchor.innId() != null, "Commit did not bind the actual anchor");
        INNS.computeIfAbsent(helper.getLevel(), ignored -> new HashSet<>()).add(anchor.innId());
        var saved = InnSavedData.get(helper.getLevel().getServer()).find(anchor.innId());
        helper.assertTrue(saved != null && saved.revision() == 1 && saved.owner().equals(owner.getUUID()), "Server registry has wrong owner/revision");
        helper.assertFalse(menu.clickMenuButton(owner, 0), "Replayed confirmation created a second configuration");
        helper.assertTrue(InnSavedData.get(helper.getLevel().getServer()).find(anchor.innId()).revision() == 1, "Replay changed the persistent revision");
        var tag = anchor.saveWithFullMetadata(helper.getLevel().registryAccess());
        var restored = BlockEntity.loadStatic(pos, anchor.getBlockState(), tag, helper.getLevel().registryAccess());
        helper.assertTrue(restored instanceof InnAnchorBlockEntity copy && copy.placementId().equals(anchor.placementId())
                && copy.ownerId().equals(owner.getUUID()) && copy.innId().equals(anchor.innId()), "Full anchor NBT lost owner, placement or inn binding");
        owner.closeContainer(); InnSelection.clear(owner); helper.succeed();
    }
    @GameTest(template = "gametest_empty", templateNamespace = DungeonsAlive.MOD_ID, batch = BATCH, timeoutTicks = 40)
    public static void discardedSelectionAndReplacementAnchorInvalidateTheAlreadyOpenMenu(GameTestHelper helper) {
        var owner = setup(helper); select(helper, owner);
        BlockPos pos = helper.absolutePos(new BlockPos(3, 1, 3)); owner.setPos(pos.getCenter().add(0, 0, 2));
        helper.assertTrue(InnContent.open(owner, pos), "Owner could not open inn menu"); var menu = (InnMenu)owner.containerMenu;
        InnSelection.clear(owner);
        helper.assertFalse(menu.clickMenuButton(owner, 0), "A discarded selection was accepted through an old window");
        helper.assertTrue(((InnAnchorBlockEntity)helper.getLevel().getBlockEntity(pos)).innId() == null, "Rejected plan mutated the anchor");
        helper.getLevel().setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
        helper.getLevel().setBlockAndUpdate(pos, InnContent.ANCHOR.get().defaultBlockState());
        var replacement = (InnAnchorBlockEntity)helper.getLevel().getBlockEntity(pos); replacement.placedBy(owner.getUUID());
        helper.assertFalse(menu.stillValid(owner) || menu.clickMenuButton(owner, 0), "Same-position replacement inherited old window authority");
        owner.closeContainer();
        var stranger = new MenuPlayer(helper.getLevel()); stranger.setGameMode(GameType.SURVIVAL); stranger.setPos(owner.position());
        helper.assertFalse(InnContent.open(stranger, pos), "A stranger opened owner configuration");
        InnSelection.mark(stranger, pos, Direction.UP);
        helper.assertTrue(InnSelection.current(stranger) == null && replacement.innId() == null, "Foreign marker bound somebody else's anchor");
        helper.succeed();
    }
    private static MenuPlayer setup(GameTestHelper helper) {
        var level = helper.getLevel(); var chunks = CHUNKS.computeIfAbsent(level, ignored -> new HashSet<>());
        var centre = helper.absolutePos(new BlockPos(5, 0, 3));
        for (int x = (centre.getX() - 48) >> 4; x <= (centre.getX() + 48) >> 4; x++)
            for (int z = (centre.getZ() - 48) >> 4; z <= (centre.getZ() + 48) >> 4; z++) {
                if (level.setChunkForced(x, z, true)) chunks.add(new ChunkPos(x, z)); level.getChunk(x, z);
            }
        for (int x = 0; x <= 12; x++) for (int z = -7; z <= 10; z++) helper.setBlock(new BlockPos(x, 0, z), Blocks.STONE);
        helper.setBlock(new BlockPos(9, 4, 8), Blocks.OAK_PLANKS);
        helper.setBlock(new BlockPos(7, 1, 7), Blocks.CHEST);
        helper.setBlock(new BlockPos(3, 1, 3), InnContent.ANCHOR.get());
        var player = new MenuPlayer(level); player.setGameMode(GameType.SURVIVAL);
        var anchor = (InnAnchorBlockEntity)level.getBlockEntity(helper.absolutePos(new BlockPos(3, 1, 3))); anchor.placedBy(player.getUUID());
        return player;
    }
    private static void select(GameTestHelper helper, MenuPlayer player) {
        for (BlockPos point : new BlockPos[] {new BlockPos(3,1,3), new BlockPos(2,0,2), new BlockPos(9,4,8),
                new BlockPos(5,0,2), new BlockPos(7,1,7), new BlockPos(5,0,-5)}) {
            BlockPos absolute = helper.absolutePos(point); player.setPos(absolute.getCenter().add(0, 1, 1));
            InnSelection.mark(player, absolute, Direction.UP);
        }
        helper.assertTrue(InnSelection.current(player) != null && InnSelection.current(player).complete(), "Actual server marking sequence failed");
    }
    /** FakePlayer's default openMenu is intentionally inert; instantiate the real MenuProvider without a client. */
    private static final class MenuPlayer extends FakePlayer {
        private int next;
        private MenuPlayer(ServerLevel level) { super(level, new GameProfile(UUID.randomUUID(), "inn-plan-test")); }
        @Override public OptionalInt openMenu(@Nullable MenuProvider provider, @Nullable Consumer<RegistryFriendlyByteBuf> extra) {
            if (provider == null) return OptionalInt.empty();
            if (containerMenu != inventoryMenu) closeContainer();
            int id = ++next; var menu = provider.createMenu(id, getInventory(), this);
            if (menu == null) return OptionalInt.empty(); containerMenu = menu; return OptionalInt.of(id);
        }
    }
}
