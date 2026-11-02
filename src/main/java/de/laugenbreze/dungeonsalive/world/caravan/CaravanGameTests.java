package de.laugenbreze.dungeonsalive.world.caravan;

import com.mojang.authlib.GameProfile;
import de.laugenbreze.dungeonsalive.DungeonsAlive;
import de.laugenbreze.dungeonsalive.entity.peaceful.DungeonsCreature;
import de.laugenbreze.dungeonsalive.entity.peaceful.DungeonsMerchant;
import de.laugenbreze.dungeonsalive.entity.peaceful.ModPeacefulEntities;
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
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.MerchantMenu;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.ServerLevelData;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.util.FakePlayer;
import net.neoforged.neoforge.event.entity.player.PlayerContainerEvent;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;
import org.jetbrains.annotations.Nullable;

/** One isolated, real group lifecycle avoids competing for the intentionally tiny global reservation cap. */
@GameTestHolder(DungeonsAlive.MOD_ID)
@PrefixGameTestTemplate(false)
public final class CaravanGameTests {
    private static final String BATCH = "caravan_lifecycle";
    private static final Map<ServerLevel, Set<ChunkPos>> CHUNKS = new IdentityHashMap<>();
    private static final Map<ServerLevel, Set<Entity>> ACTORS = new IdentityHashMap<>();
    private record Conditions(long time, long tick, int clear, int rain, int thunder, boolean raining, boolean thundering, boolean enabled) {}
    private static final Map<ServerLevel, Conditions> CONDITIONS = new IdentityHashMap<>();

    @AfterBatch(batch = BATCH)
    public static void cleanup(ServerLevel level) {
        var actors = ACTORS.remove(level);
        if (actors != null) actors.forEach(Entity::discard);
        var chunks = CHUNKS.remove(level);
        if (chunks != null) for (var chunk : chunks) level.setChunkForced(chunk.x, chunk.z, false);
        Conditions before = CONDITIONS.remove(level);
        if (before != null) {
            var data = (ServerLevelData) level.getLevelData();
            level.setDayTime(before.time + level.getGameTime() - before.tick);
            level.setWeatherParameters(before.clear, before.rain, before.raining, before.thundering);
            data.setThunderTime(before.thunder);
            CaravanConfig.ENABLED.set(before.enabled);
        }
    }

    @GameTest(template = "gametest_empty", templateNamespace = DungeonsAlive.MOD_ID, batch = BATCH, timeoutTicks = 280)
    public static void realCaravanWalksTradesFleesPersistsAndLetsThePlayerClaimACompanion(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        var weather = (ServerLevelData) level.getLevelData();
        CONDITIONS.put(level, new Conditions(level.getDayTime(), level.getGameTime(), weather.getClearWeatherTime(), weather.getRainTime(),
                weather.getThunderTime(), level.isRaining(), level.isThundering(), CaravanConfig.ENABLED.get()));
        CaravanConfig.ENABLED.set(false); // Only this explicit fixture, never the unrelated random encounter scheduler.
        level.setDayTime(6000);
        level.setWeatherParameters(0, 6000, false, false);
        for (int x = 0; x < 20; x++) for (int z = 0; z < 12; z++) helper.setBlock(new BlockPos(x, 0, z), Blocks.STONE);
        loadFixture(helper);
        BlockPos a = helper.absolutePos(new BlockPos(3, 1, 5)), b = helper.absolutePos(new BlockPos(16, 1, 5));
        var group = CaravanEncounters.spawnGroup(level, a, b);
        helper.assertTrue(group.size() == 3, "Real bounded transactional caravan creation failed");
        group.forEach(actor -> track(level, actor));
        var trader = (DungeonsMerchant) group.getFirst();
        var companion = (DungeonsCreature) group.get(1);
        // Mob.finalizeSpawn adds a random FOLLOW_RANGE multiplier. Force its real
        // positive case so a base-24 threshold cannot silently freeze half of caravans.
        var bonus = ResourceLocation.withDefaultNamespace("random_spawn_bonus");
        for (var actor : group) {
            var follow = actor.getAttribute(Attributes.FOLLOW_RANGE);
            follow.removeModifier(bonus);
            follow.addPermanentModifier(new AttributeModifier(bonus, .1, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
        }
        helper.assertTrue(trader.getAttributeValue(Attributes.FOLLOW_RANGE) > 24, "Positive native spawn-bonus fixture is missing");
        UUID id = CaravanMember.load(trader).group;
        Vec3 origin = trader.position();
        Vec3[] previous = {origin};
        boolean[] walked = {false}, sawPath = {false}, fleeMoved = {false};
        Vec3[] escape = {null};
        long[] alarm = {0};
        var customer = new MenuPlayer(level);
        customer.setGameMode(GameType.SURVIVAL);
        track(level, customer);
        helper.onEachTick(() -> {
            helper.assertTrue(trader.position().distanceToSqr(previous[0]) < 1, "Caravan route teleported instead of walking");
            previous[0] = trader.position();
            walked[0] |= trader.position().distanceToSqr(origin) > 1;
            sawPath[0] |= !trader.getNavigation().isDone();
            if (escape[0] != null && level.getGameTime() < alarm[0]) fleeMoved[0] |= trader.position().distanceToSqr(escape[0]) > 1;
            helper.assertTrue(group.stream().allMatch(mob -> mob.getTarget() == null), "A caravan member acquired a combat target");
        });
        helper.runAtTickTime(5, () -> {
            var nbt = new CompoundTag();
            trader.saveWithoutId(nbt);
            var restored = ModPeacefulEntities.merchantType("village_merchant").create(level);
            helper.assertTrue(restored != null, "Missing restored merchant factory");
            restored.load(nbt);
            track(level, restored);
            var state = CaravanMember.load(restored);
            helper.assertTrue(state != null && state.group.equals(id) && state.a.equals(a) && state.b.equals(b),
                    "Real full entity NBT lost caravan identity or placed anchors");
            restored.addTag("dungeonsalive_settler");
            helper.assertTrue(CaravanMember.load(restored) == null, "A settled NPC was eligible for caravan recruitment");
            restored.discard();
            BlockPos unloaded = a.offset(4096, 0, 4096);
            helper.assertFalse(level.hasChunkAt(unloaded), "Unloaded-target fixture was already loaded");
            helper.assertTrue(CaravanEncounters.spawnGroup(level, unloaded, unloaded.offset(16, 0, 0)).isEmpty(),
                    "Caravan creation accepted an unloaded route");
            helper.assertFalse(level.hasChunkAt(unloaded), "Rejected caravan creation loaded an unknown chunk");
        });
        helper.runAtTickTime(40, () -> {
            // The .7 walking modifier also drives vanilla's forward input. Allow two
            // planning intervals for a full block of real travel, including AI startup.
            var path = trader.getNavigation().getPath();
            helper.assertTrue(walked[0] && sawPath[0], "The actual trader AI never began its route: origin=" + origin
                    + ", position=" + trader.position() + ", sawPath=" + sawPath[0]
                    + ", motion=" + trader.getDeltaMovement() + ", ground=" + trader.onGround()
                    + ", followRange=" + trader.getAttributeValue(Attributes.FOLLOW_RANGE)
                    + ", collision=" + trader.horizontalCollision + ", path="
                    + (path == null ? "null" : path.getNextNodeIndex() + "/" + path.getNodeCount())
                    + ", daytime=" + level.getDayTime() + ", rain=" + level.isRaining());
            customer.setPos(trader.position().add(0, 0, 1));
            helper.assertTrue(customer.interactOn(trader, InteractionHand.MAIN_HAND).consumesAction(), "Real player interaction did not open caravan trade");
            helper.assertTrue(customer.containerMenu instanceof MerchantMenu && trader.isTrading() && trader.getNavigation().isDone(),
                    "Real caravan goods menu did not interrupt navigation");
            var menu = (MerchantMenu) customer.containerMenu;
            var offer = trader.getOffers().getFirst();
            int uses = offer.getUses();
            menu.getSlot(0).set(offer.getCostA().copy());
            menu.getSlot(1).set(offer.getCostB().copy());
            helper.assertTrue(menu.getSlot(2).hasItem(), "Caravan merchant lost its existing offers");
            menu.clicked(2, 0, ClickType.QUICK_MOVE, customer);
            helper.assertTrue(offer.getUses() == uses + 1, "Actual caravan purchase did not consume an offer");
        });
        helper.runAtTickTime(45, () -> {
            customer.setPos(trader.position().add(-1, 0, 0));
            escape[0] = trader.position();
            helper.assertTrue(trader.hurt(level.damageSources().playerAttack(customer), 1), "Actual damage was not accepted");
            alarm[0] = level.getGameTime() + CaravanRoute.PANIC_TICKS;
            helper.assertTrue(customer.containerMenu == customer.inventoryMenu && !trader.isTrading(), "Damage left the exact caravan trade menu open");
            helper.assertTrue(group.stream().allMatch(mob -> CaravanMember.load(mob).route.alarmUntil() == alarm[0]),
                    "Accepted damage did not propagate one finite alarm to the nearby group");
            helper.assertTrue(customer.interactOn(trader, InteractionHand.MAIN_HAND) == InteractionResult.FAIL,
                    "A normal interaction reopened trade during active danger");
            var saved = new CompoundTag(); trader.saveWithoutId(saved);
            var restored = ModPeacefulEntities.merchantType("village_merchant").create(level);
            helper.assertTrue(restored != null, "Missing reload factory");
            restored.load(saved); track(level, restored);
            helper.assertTrue(CaravanMember.load(restored).route.alarmUntil() == alarm[0], "Reload restarted or erased the persisted alarm timeout");
            restored.discard();
        });
        helper.runAtTickTime(55, () -> {
            // Reproduce a stale reference explicitly: only the real current menu may be closed by damage.
            trader.openGoodsTrade(customer);
            helper.assertTrue(customer.containerMenu instanceof MerchantMenu, "Stale-menu fixture did not create an actual merchant menu");
            var previousMenu = customer.containerMenu;
            var chest = ChestMenu.threeRows(91, customer.getInventory(), new SimpleContainer(27));
            customer.containerMenu = chest;
            trader.invulnerableTime = 0;
            helper.assertTrue(trader.hurt(level.damageSources().playerAttack(customer), 1), "Second accepted damage fixture failed");
            alarm[0] = level.getGameTime() + CaravanRoute.PANIC_TICKS;
            helper.assertTrue(customer.containerMenu == chest, "Caravan panic closed an unrelated chest");
            previousMenu.removed(customer);
            customer.containerMenu = customer.inventoryMenu;
        });
        helper.runAtTickTime(180, () -> {
            helper.assertTrue(fleeMoved[0], "Alarm produced no actual walking response");
            helper.assertTrue(CaravanMember.load(trader).route.alarmUntil() <= level.getGameTime(), "Caravan panic never expired");
            customer.setPos(companion.position().add(0, 0, 1));
            helper.assertTrue(customer.interactOn(companion, InteractionHand.MAIN_HAND).consumesAction()
                    && customer.getUUID().equals(companion.ownerId()), "The existing companion ownership interaction stopped working");
        });
        helper.runAtTickTime(260, () -> {
            helper.assertTrue(trader.isAlive() && CaravanSavedData.get(level).ledger.contains(id, trader.getUUID()), "An active caravan lost its persistent reservation");
            helper.assertFalse(CaravanSavedData.get(level).ledger.contains(id, companion.getUUID()), "Claimed pet still occupies caravan membership");
            helper.assertFalse(companion.getTags().contains(CaravanMember.TAG), "Claimed companion retained caravan control");
            helper.assertTrue(CaravanMember.load(trader).a.equals(a) && CaravanMember.load(trader).b.equals(b), "Walking or danger changed the placed route anchors");
            customer.closeContainer();
            helper.succeed();
        });
    }

    private static void loadFixture(GameTestHelper helper) {
        var level = helper.getLevel();
        var min = helper.absolutePos(new BlockPos(-CaravanGoal.LOADED_RADIUS, 0, -CaravanGoal.LOADED_RADIUS));
        var max = helper.absolutePos(new BlockPos(20 + CaravanGoal.LOADED_RADIUS, 0, 12 + CaravanGoal.LOADED_RADIUS));
        var chunks = CHUNKS.computeIfAbsent(level, ignored -> new HashSet<>());
        for (int x = min.getX() >> 4; x <= max.getX() >> 4; x++) for (int z = min.getZ() >> 4; z <= max.getZ() >> 4; z++) {
            if (level.setChunkForced(x, z, true)) chunks.add(new ChunkPos(x, z));
            level.getChunk(x, z); // Explicit fixture loading, never production behavior.
        }
    }
    private static void track(ServerLevel level, Entity entity) { ACTORS.computeIfAbsent(level, ignored -> new HashSet<>()).add(entity); }
    private static final class MenuPlayer extends FakePlayer {
        private int menuId;
        MenuPlayer(ServerLevel level) { super(level, new GameProfile(UUID.randomUUID(), "caravan-test")); }
        @Override public OptionalInt openMenu(@Nullable MenuProvider provider, @Nullable Consumer<RegistryFriendlyByteBuf> writer) {
            if (provider == null) return OptionalInt.empty();
            if (containerMenu != inventoryMenu) closeContainer();
            var menu = provider.createMenu(++menuId, getInventory(), this);
            if (menu == null) return OptionalInt.empty();
            containerMenu = menu;
            // FakePlayer.openMenu is deliberately a no-op. Keep the actual server menu and the
            // same Open event emitted by ServerPlayer's native menu path, with no network packet.
            NeoForge.EVENT_BUS.post(new PlayerContainerEvent.Open(this, menu));
            return OptionalInt.of(menuId);
        }
    }
    private CaravanGameTests() {}
}
