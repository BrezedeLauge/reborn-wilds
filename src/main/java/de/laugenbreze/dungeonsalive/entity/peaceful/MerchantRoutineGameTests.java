package de.laugenbreze.dungeonsalive.entity.peaceful;

import com.mojang.authlib.GameProfile;
import de.laugenbreze.dungeonsalive.DungeonsAlive;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.OptionalInt;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.AfterBatch;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.MerchantMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.util.FakePlayer;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;
import org.jetbrains.annotations.Nullable;

/** Actual server AI and menu regressions; these fixtures never change time or weather. */
@GameTestHolder(DungeonsAlive.MOD_ID)
@PrefixGameTestTemplate(false)
public final class MerchantRoutineGameTests {
    private static final String BATCH = "merchant_routine";
    private static final Map<ServerLevel, Set<ChunkPos>> FIXTURE_CHUNKS = new IdentityHashMap<>();

    private MerchantRoutineGameTests() {}

    @AfterBatch(batch = BATCH)
    public static void releaseFixtureChunks(ServerLevel level) {
        Set<ChunkPos> chunks = FIXTURE_CHUNKS.remove(level);
        if (chunks != null) for (ChunkPos chunk : chunks) level.setChunkForced(chunk.x, chunk.z, false);
    }

    @GameTest(template = "gametest_empty", templateNamespace = DungeonsAlive.MOD_ID,
            batch = BATCH, timeoutTicks = 30)
    public static void campHomeStartsAfterPlacementAndSurvivesReload(GameTestHelper helper) {
        floor(helper);
        loadNavigationFixture(helper);
        var templateActor = createMerchant(helper, true);
        templateActor.setPos(1.5, 1, 1.5); // A template-relative position, before world placement.
        CompoundTag legacy = new CompoundTag();
        templateActor.saveWithoutId(legacy);
        legacy.remove("UUID");
        helper.assertFalse(legacy.contains("DungeonsCampHome"), "An unplaced merchant already saved a camp home");

        var placed = createMerchant(helper, false);
        placed.load(legacy.copy());
        placed.setPos(helper.absoluteVec(new Vec3(3.5, 1, 3.5)));
        BlockPos placedHome = placed.blockPosition().immutable();
        helper.assertTrue(placed.hasCampRoutine() && placed.campHome() == null,
                "Legacy NBT loading either lost the camp tag or captured a position before AI placement");
        helper.assertTrue(helper.getLevel().addFreshEntity(placed), "Could not add the legacy camp merchant");

        var noAi = createMerchant(helper, false);
        noAi.load(legacy.copy());
        noAi.setNoAi(true);
        noAi.setPos(helper.absoluteVec(new Vec3(8.5, 1, 3.5)));
        helper.assertTrue(helper.getLevel().addFreshEntity(noAi), "Could not add the NoAI camp merchant");
        var ordinary = spawnMerchant(helper, new Vec3(13.5, 1, 3.5), false);
        DungeonsMerchant[] restored = new DungeonsMerchant[1];
        BlockPos[] resumedHome = new BlockPos[1];

        helper.runAtTickTime(4, () -> {
            helper.assertTrue(placedHome.equals(placed.campHome()), "First real AI tick did not record the world placement");
            BlockPos unloadedTarget = null;
            for (int distance : new int[]{512, 1024, 2048, 4096}) {
                BlockPos candidate = placedHome.offset(distance, 0, distance);
                if (!helper.getLevel().hasChunk(candidate.getX() >> 4, candidate.getZ() >> 4)) {
                    unloadedTarget = candidate;
                    break;
                }
            }
            helper.assertTrue(unloadedTarget != null, "No unloaded target chunk was available among the bounded fixture candidates");
            helper.assertTrue(placed.getNavigation().createPath(unloadedTarget, 0) == null,
                    "Camp navigation accepted a target in an unloaded chunk");
            helper.assertFalse(helper.getLevel().hasChunk(unloadedTarget.getX() >> 4, unloadedTarget.getZ() >> 4),
                    "Rejecting a distant route loaded its previously absent target chunk");
            helper.assertTrue(noAi.campHome() == null, "NoAI merchant acquired an active camp home");
            helper.assertTrue(!ordinary.hasCampRoutine() && ordinary.campHome() == null,
                    "An ordinary spawn was silently enrolled into a camp routine");
            CompoundTag saved = new CompoundTag();
            placed.saveWithoutId(saved);
            saved.remove("UUID");
            placed.discard();
            restored[0] = createMerchant(helper, false);
            restored[0].load(saved);
            restored[0].setNoAi(true);
            restored[0].setPos(helper.absoluteVec(new Vec3(16.5, 1, 8.5)));
            helper.assertTrue(helper.getLevel().addFreshEntity(restored[0]), "Could not add the restored camp merchant");
            helper.assertTrue(restored[0].hasCampRoutine() && placedHome.equals(restored[0].campHome()),
                    "Full entity NBT reload lost the tag or rewrote the established home");
            noAi.setPos(helper.absoluteVec(new Vec3(8.5, 1, 7.5)));
            resumedHome[0] = noAi.blockPosition().immutable();
            noAi.setNoAi(false);
        });
        helper.runAtTickTime(9, () -> {
            helper.assertTrue(resumedHome[0].equals(noAi.campHome()), "Enabling AI did not use the final placed position");
            helper.assertTrue(placedHome.equals(restored[0].campHome()), "Reloaded home drifted to the entity's new position");
            helper.succeed();
        });
    }

    @GameTest(template = "gametest_empty", templateNamespace = DungeonsAlive.MOD_ID,
            batch = BATCH, timeoutTicks = 420)
    public static void displacedCampMerchantWalksHomeWithoutTeleport(GameTestHelper helper) {
        floor(helper);
        loadNavigationFixture(helper);
        var merchant = spawnMerchant(helper, new Vec3(2.5, 1, 5.5), true);
        BlockPos home = merchant.blockPosition().immutable();
        Vec3[] previous = new Vec3[1];
        boolean[] sawPath = {false};
        boolean[] sawMovement = {false};
        boolean[] returnPathStarted = {false};
        Vec3 displaced = helper.absoluteVec(new Vec3(17.5, 1, 5.5));
        helper.runAtTickTime(4, () -> {
            helper.assertTrue(home.equals(merchant.campHome()) && !merchant.isNoAi(), "Camp AI did not initialize");
            // Initial displacement is fixture setup. All motion after this point must come from AI.
            merchant.setPos(displaced);
            previous[0] = displaced;
        });
        helper.onEachTick(() -> {
            if (previous[0] == null) return;
            Vec3 current = merchant.position();
            helper.assertTrue(current.distanceToSqr(previous[0]) < 1, "Return home jumped instead of walking");
            previous[0] = current;
            sawPath[0] |= !merchant.getNavigation().isDone();
            sawMovement[0] |= current.distanceToSqr(displaced) > 4;
            if (merchant.campActivity() == MerchantRoutine.Activity.RETURN_HOME && !merchant.getNavigation().isDone())
                returnPathStarted[0] = true;
            if (returnPathStarted[0]
                    && current.distanceToSqr(Vec3.atBottomCenterOf(home)) > MerchantRoutine.HOME_TOLERANCE_SQR) {
                // The flat, clear fixture has no new obstacle or unload. Revalidating a
                // cached path must not reject its already-traversed distant start nodes.
                helper.assertFalse(merchant.getNavigation().isDone(),
                        "A clear active homeward route was interrupted before arrival: " + diagnostic(merchant));
            }
            helper.assertTrue(merchant.getTarget() == null, "Camp return acquired a combat target");
        });
        helper.succeedWhen(() -> {
            helper.assertTrue(previous[0] != null && sawPath[0] && returnPathStarted[0] && sawMovement[0],
                    "No actual navigation toward home yet");
            helper.assertTrue(merchant.isAlive() && home.equals(merchant.campHome()), "Walking changed the saved home");
            helper.assertTrue(merchant.position().distanceToSqr(Vec3.atBottomCenterOf(home)) <= MerchantRoutine.HOME_TOLERANCE_SQR,
                    "Displaced merchant has not walked back home: " + diagnostic(merchant));
        });
    }

    @GameTest(template = "gametest_empty", templateNamespace = DungeonsAlive.MOD_ID,
            batch = BATCH, timeoutTicks = 30)
    public static void realTradeMenuClosesOnDamageButForeignMenuStaysOpen(GameTestHelper helper) {
        floor(helper);
        loadNavigationFixture(helper);
        var merchant = spawnMerchant(helper, new Vec3(4.5, 1, 5.5), true);
        var staleMerchant = spawnMerchant(helper, new Vec3(12.5, 1, 5.5), true);
        helper.runAtTickTime(4, () -> {
            // FakePlayer normally refuses openMenu. This fixture creates the provider's
            // actual server menu, while retaining its no-op network connection.
            var customer = new MenuPlayer(helper.getLevel());
            MerchantMenu staleMenu = null;
            try {
                var path = merchant.getNavigation().createPath(helper.absolutePos(new BlockPos(8, 1, 5)), 0);
                helper.assertTrue(path != null && path.canReach() && merchant.getNavigation().moveTo(path, .65)
                                && !merchant.getNavigation().isDone(), "Trade fixture did not start a real walkable route");
                customer.setPos(merchant.position().add(0, 0, 1));
                helper.assertTrue(merchant.mobInteract(customer, InteractionHand.MAIN_HAND).consumesAction(), "Merchant interaction failed");
                helper.assertTrue(merchant.getNavigation().isDone(), "Opening trade did not stop the merchant's active route");
                helper.assertTrue(customer.containerMenu instanceof MerchantMenu && merchant.getTradingPlayer() == customer,
                        "Interaction did not open this merchant's actual server trade menu");
                var menu = (MerchantMenu) customer.containerMenu;
                var offer = merchant.getOffers().getFirst();
                int uses = offer.getUses();
                ItemStack result = offer.getResult().copy();
                menu.getSlot(0).set(offer.getCostA().copy());
                menu.getSlot(1).set(offer.getCostB().copy());
                helper.assertTrue(menu.getSlot(2).hasItem(), "Real payment slots did not produce a trade result");
                menu.clicked(2, 0, ClickType.PICKUP, customer);
                helper.assertTrue(offer.getUses() == uses + 1 && menu.getSlot(0).getItem().isEmpty(),
                        "Taking the result did not consume payment and one use through the real menu");
                helper.assertTrue(ItemStack.isSameItemSameComponents(menu.getCarried(), result)
                                && menu.getCarried().getCount() == result.getCount(), "Actual trade returned the wrong result");
                helper.assertTrue(merchant.hurt(helper.getLevel().damageSources().playerAttack(customer), 1), "Trade damage was not accepted");
                helper.assertTrue(customer.containerMenu == customer.inventoryMenu && merchant.getTradingPlayer() == null,
                        "Accepted damage left this merchant's own menu or trading relationship open");

                customer.setPos(staleMerchant.position().add(0, 0, 1));
                staleMerchant.mobInteract(customer, InteractionHand.MAIN_HAND);
                helper.assertTrue(customer.containerMenu instanceof MerchantMenu && staleMerchant.getTradingPlayer() == customer,
                        "Second merchant never established the stale-menu fixture");
                staleMenu = (MerchantMenu) customer.containerMenu;
                var foreign = ChestMenu.threeRows(77, customer.getInventory(), new SimpleContainer(27));
                // Deliberately preserve the old merchant relationship while another actual
                // menu replaces it: normal menu switching would already clear that relation.
                customer.containerMenu = foreign;
                helper.assertTrue(staleMerchant.hurt(helper.getLevel().damageSources().playerAttack(customer), 1), "Stale trade damage was not accepted");
                helper.assertTrue(customer.containerMenu == foreign && staleMerchant.getTradingPlayer() == null,
                        "Damage closed a foreign menu or retained the stale customer");
            } finally {
                customer.closeContainer();
                if (staleMenu != null) staleMenu.removed(customer);
                merchant.setTradingPlayer(null);
                staleMerchant.setTradingPlayer(null);
            }
            helper.succeed();
        });
    }

    @GameTest(template = "gametest_empty", templateNamespace = DungeonsAlive.MOD_ID,
            batch = BATCH, timeoutTicks = 320)
    public static void damagedCampMerchantFleesPeacefullyThenReturnsHome(GameTestHelper helper) {
        floor(helper);
        loadNavigationFixture(helper);
        var merchant = spawnMerchant(helper, new Vec3(10.5, 1, 5.5), true);
        // Start the first actual AI tick already frightened, so an earlier random
        // daytime stroll cannot consume this test's finite escape route budget.
        merchant.setNoAi(true);
        BlockPos home = merchant.blockPosition().immutable();
        var attacker = helper.makeMockPlayer(GameType.SURVIVAL);
        attacker.setPos(helper.absoluteVec(new Vec3(8.5, 1, 5.5)));
        float attackerHealth = attacker.getHealth();
        long[] hurtAt = {Long.MIN_VALUE};
        Vec3[] escapeStart = new Vec3[1];
        boolean[] sawFlee = {false};
        boolean[] sawEscapeMovement = {false};
        helper.runAtTickTime(4, () -> {
            helper.assertTrue(merchant.campHome() == null, "NoAI fixture unexpectedly initialized a camp home");
            merchant.setNoAi(false);
            merchant.getRandom().setSeed(73L);
            escapeStart[0] = merchant.position();
            helper.assertTrue(merchant.hurt(helper.getLevel().damageSources().playerAttack(attacker), 1), "Player damage was not accepted");
            hurtAt[0] = helper.getLevel().getGameTime();
        });
        helper.onEachTick(() -> {
            if (escapeStart[0] == null) return;
            helper.assertTrue(merchant.isAlive() && merchant.getTarget() == null && attacker.getHealth() == attackerHealth,
                    "Fleeing merchant disappeared or retaliated against its attacker");
            if (merchant.campActivity() == MerchantRoutine.Activity.FLEE) {
                sawFlee[0] = true;
                sawEscapeMovement[0] |= helper.getLevel().getGameTime() - hurtAt[0] > 10
                        && !merchant.getNavigation().isDone()
                        && merchant.position().distanceToSqr(escapeStart[0]) > 4
                        && merchant.distanceToSqr(attacker) > escapeStart[0].distanceToSqr(attacker.position()) + 2;
            }
        });
        helper.succeedWhen(() -> {
            helper.assertTrue(hurtAt[0] != Long.MIN_VALUE
                            && helper.getLevel().getGameTime() - hurtAt[0] > MerchantRoutine.FLEE_TICKS + 20,
                    "Finite escape window has not elapsed");
            helper.assertTrue(sawFlee[0] && sawEscapeMovement[0], "Damage did not cause actual movement away from the attacker: " + diagnostic(merchant));
            helper.assertTrue(merchant.campActivity() != MerchantRoutine.Activity.FLEE, "Damage left the merchant permanently fleeing");
            helper.assertTrue(home.equals(merchant.campHome())
                            && merchant.position().distanceToSqr(Vec3.atBottomCenterOf(home)) <= MerchantRoutine.HOME_TOLERANCE_SQR,
                    "Merchant did not return to its preserved home after the finite escape");
        });
    }

    private static DungeonsMerchant createMerchant(GameTestHelper helper, boolean camp) {
        var merchant = ModPeacefulEntities.merchantType("village_merchant").create(helper.getLevel());
        helper.assertTrue(merchant != null, "Missing village merchant factory");
        if (camp) merchant.addTag(DungeonsMerchant.CAMP_TAG);
        return merchant;
    }

    private static String diagnostic(DungeonsMerchant merchant) {
        var path = merchant.getNavigation().getPath();
        return "position=" + merchant.position() + ", home=" + merchant.campHome() + ", activity=" + merchant.campActivity()
                + ", done=" + merchant.getNavigation().isDone() + ", motion=" + merchant.getDeltaMovement()
                + ", ground=" + merchant.onGround() + ", collision=" + merchant.horizontalCollision
                + ", path=" + (path == null ? "none" : path.getNextNodeIndex() + "/" + path.getNodeCount()
                + " to " + path.getTarget() + (path.isDone() ? "" : " next " + path.getNextNodePos()));
    }

    private static DungeonsMerchant spawnMerchant(GameTestHelper helper, Vec3 relative, boolean camp) {
        var merchant = createMerchant(helper, camp);
        merchant.setPos(helper.absoluteVec(relative));
        helper.assertTrue(helper.getLevel().addFreshEntity(merchant), "Could not spawn merchant");
        return merchant;
    }

    private static void floor(GameTestHelper helper) {
        for (int x = 0; x < 20; x++) for (int z = 0; z < 12; z++) helper.setBlock(new BlockPos(x, 0, z), Blocks.STONE);
    }

    private static void loadNavigationFixture(GameTestHelper helper) {
        // Explicit fixture loading supplies the whole vanilla path cache. It does not
        // claim that production AI may load chunks. AfterBatch also runs after failures.
        ServerLevel level = helper.getLevel();
        int radius = MerchantNavigation.LOADED_RADIUS;
        BlockPos min = helper.absolutePos(new BlockPos(-radius, 0, -radius));
        BlockPos max = helper.absolutePos(new BlockPos(19 + radius, 0, 11 + radius));
        Set<ChunkPos> added = FIXTURE_CHUNKS.computeIfAbsent(level, ignored -> new HashSet<>());
        for (int x = Math.min(min.getX(), max.getX()) >> 4; x <= Math.max(min.getX(), max.getX()) >> 4; x++) {
            for (int z = Math.min(min.getZ(), max.getZ()) >> 4; z <= Math.max(min.getZ(), max.getZ()) >> 4; z++) {
                if (level.setChunkForced(x, z, true)) added.add(new ChunkPos(x, z));
                level.getChunk(x, z);
            }
        }
    }

    private static final class MenuPlayer extends FakePlayer {
        private int nextMenuId;

        private MenuPlayer(ServerLevel level) {
            super(level, new GameProfile(UUID.randomUUID(), "camp-trade-test"));
        }

        @Override public OptionalInt openMenu(@Nullable MenuProvider provider,
                                               @Nullable Consumer<RegistryFriendlyByteBuf> extraDataWriter) {
            if (provider == null) return OptionalInt.empty();
            if (containerMenu != inventoryMenu) closeContainer();
            int id = ++nextMenuId;
            var menu = provider.createMenu(id, getInventory(), this);
            if (menu == null) return OptionalInt.empty();
            containerMenu = menu;
            return OptionalInt.of(id);
        }
    }
}
