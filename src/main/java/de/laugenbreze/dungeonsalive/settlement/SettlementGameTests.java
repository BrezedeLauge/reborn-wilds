package de.laugenbreze.dungeonsalive.settlement;

import com.mojang.authlib.GameProfile;
import de.laugenbreze.dungeonsalive.DungeonsAlive;
import de.laugenbreze.dungeonsalive.entity.DungeonMob;
import de.laugenbreze.dungeonsalive.entity.ModEntities;
import de.laugenbreze.dungeonsalive.entity.peaceful.DungeonsMerchant;
import de.laugenbreze.dungeonsalive.entity.peaceful.ModPeacefulEntities;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.gametest.framework.AfterBatch;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.util.FakePlayer;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

@GameTestHolder(DungeonsAlive.MOD_ID)
@PrefixGameTestTemplate(false)
public final class SettlementGameTests {
    private static final Map<ServerLevel, CompoundTag> HISTORY = new IdentityHashMap<>();
    private static final Map<ServerLevel, Set<Entity>> ACTORS = new IdentityHashMap<>();
    private static final Map<ServerLevel, Set<ChunkPos>> CHUNKS = new IdentityHashMap<>();
    private SettlementGameTests() {}

    @AfterBatch(batch = "settlement_placement") public static void cleanPlacement(ServerLevel level) { cleanup(level); }
    @AfterBatch(batch = "settlement_rewards") public static void cleanRewards(ServerLevel level) { cleanup(level); }
    private static void cleanup(ServerLevel level) {
        var actors = ACTORS.remove(level); if (actors != null) actors.forEach(Entity::discard);
        var history = HISTORY.remove(level);
        if (history != null) level.getServer().overworld().getDataStorage().set(SettlementSavedData.DATA_NAME,
                SettlementSavedData.load(history, level.registryAccess()));
        var chunks = CHUNKS.remove(level);
        if (chunks != null) for (var chunk : chunks) level.setChunkForced(chunk.x, chunk.z, false);
    }

    @GameTest(template = "gametest_empty", templateNamespace = DungeonsAlive.MOD_ID, batch = "settlement_placement", timeoutTicks = 30)
    public static void charterPlacesTwoRealResidentsAtomicallyWithSavedOwnerAndCampHome(GameTestHelper helper) {
        fixture(helper);
        var player = player(helper); var charter = charter(helper);
        var stack = new ItemStack(charter, 2); player.setItemInHand(InteractionHand.MAIN_HAND, stack);
        BlockPos floor = helper.absolutePos(new BlockPos(10, 0, 6));
        var use = context(player, floor);
        player.getAbilities().mayBuild = false;
        helper.assertTrue(charter.useOn(use) == InteractionResult.FAIL && stack.getCount() == 2,
                "A player without build permission founded a settlement");
        player.getAbilities().mayBuild = true;
        Consumer<EntityJoinLevelEvent> rejectSecond = event -> {
            if (event.getEntity() instanceof DungeonsMerchant merchant && merchant.getTags().contains(SettlementService.SETTLER_TAG)) {
                track(helper, merchant);
                if (merchant.profile().id().equals("blacksmith")) event.setCanceled(true);
            }
        };
        NeoForge.EVENT_BUS.addListener(EventPriority.HIGHEST, rejectSecond);
        try { helper.assertTrue(charter.useOn(use) == InteractionResult.FAIL, "Rejected second NPC still committed a settlement"); }
        finally { NeoForge.EVENT_BUS.unregister(rejectSecond); }
        helper.assertTrue(stack.getCount() == 2 && settlers(helper).isEmpty()
                        && SettlementSavedData.get(helper.getLevel().getServer()).anchors().isEmpty(),
                "Failed placement left a resident, consumed a charter or saved an incomplete anchor");
        helper.assertTrue(charter.useOn(use).consumesAction() && stack.getCount() == 1, "Safe placement did not consume exactly one charter");
        var residents = settlers(helper); residents.forEach(actor -> track(helper, actor));
        helper.assertTrue(residents.size() == 2 && residents.stream().allMatch(resident -> resident.hasCampRoutine() && resident.isPersistenceRequired()),
                "Founding did not place two actual persistent camp merchants");
        var record = SettlementSavedData.get(helper.getLevel().getServer()).anchors().getFirst();
        helper.assertTrue(record.owner().equals(player.getUUID()) && record.residents().containsAll(residents.stream().map(Entity::getUUID).toList()),
                "Settlement history lacks its real owner or resident identities");
        var stranger = player(helper); stranger.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(charter, 1));
        helper.assertTrue(charter.useOn(context(stranger, floor)) == InteractionResult.FAIL && stranger.getMainHandItem().getCount() == 1,
                "Another player overlapped or took over the existing settlement");
        helper.runAtTickTime(4, () -> {
            for (var resident : residents) {
                helper.assertTrue(resident.campHome() != null && resident.getPersistentData().getUUID(SettlementService.OWNER_KEY).equals(player.getUUID())
                                && resident.getPersistentData().getUUID(SettlementService.SETTLEMENT_KEY).equals(record.id()),
                        "Real AI failed to initialize the camp home or lost founding metadata");
            }
            var original = residents.getFirst(); BlockPos home = original.campHome();
            CompoundTag saved = new CompoundTag(); original.saveWithoutId(saved); original.discard();
            var restored = ModPeacefulEntities.merchantType(original.profile().id()).create(helper.getLevel());
            helper.assertTrue(restored != null, "Missing resident reload factory"); restored.load(saved); track(helper, restored);
            helper.assertTrue(helper.getLevel().addFreshEntity(restored) && home.equals(restored.campHome())
                            && restored.getPersistentData().getUUID(SettlementService.OWNER_KEY).equals(player.getUUID()),
                    "Full resident NBT reload lost home or ownership");
            var data = SettlementSavedData.get(helper.getLevel().getServer());
            var copy = SettlementSavedData.load(data.save(new CompoundTag(), helper.getLevel().registryAccess()), helper.getLevel().registryAccess());
            helper.assertTrue(copy.anchors().equals(data.anchors()) && copy.check(stranger.getUUID(), record.dimension(), record.x(), record.z())
                            == SettlementRegistry.Decision.NEARBY, "Reload allowed an unloaded settlement reservation to be overwritten");
            helper.succeed();
        });
    }

    @GameTest(template = "gametest_empty", templateNamespace = DungeonsAlive.MOD_ID, batch = "settlement_rewards", timeoutTicks = 30)
    public static void realPlayerBossDeathGivesOneCharterAndReplayCancellationOrSummonsGiveNone(GameTestHelper helper) {
        fixture(helper); var player = player(helper); var charter = charter(helper);
        var canceled = boss(helper);
        Consumer<LivingDeathEvent> cancel = event -> { if (event.getEntity() == canceled) event.setCanceled(true); };
        NeoForge.EVENT_BUS.addListener(EventPriority.HIGHEST, cancel);
        try { canceled.hurt(player.damageSources().playerAttack(player), 10_000); }
        finally { NeoForge.EVENT_BUS.unregister(cancel); }
        helper.assertTrue(charters(helper) == 0, "Canceled boss death rewarded a charter"); canceled.discard();
        var real = boss(helper); CompoundTag original = new CompoundTag(); real.saveWithoutId(original);
        helper.assertTrue(real.hurt(player.damageSources().playerAttack(player), 10_000) && !real.isAlive(), "Actual player boss kill failed");
        helper.assertTrue(charters(helper) == 1, "Actual boss kill did not produce exactly one real charter ItemEntity");
        real.discard();
        var replay = ModEntities.type("redstone_monstrosity").create(helper.getLevel());
        helper.assertTrue(replay != null, "Missing boss reload factory"); replay.load(original); track(helper, replay);
        helper.assertTrue(helper.getLevel().addFreshEntity(replay), "Could not reload the same boss identity");
        replay.hurt(player.damageSources().playerAttack(player), 10_000);
        helper.assertTrue(charters(helper) == 1, "Reloading the same boss identity duplicated its charter"); replay.discard();
        var summoned = boss(helper); CompoundTag summon = new CompoundTag(); summoned.addAdditionalSaveData(summon);
        summon.putUUID("DungeonsSummoner", UUID.randomUUID()); summon.putLong("DungeonsSummonExpiresAt", helper.getLevel().getGameTime() + 100);
        summoned.readAdditionalSaveData(summon);
        helper.assertTrue(summoned.isSummoned(), "Helper fixture lost its existing summoned marker");
        summoned.hurt(player.damageSources().playerAttack(player), 10_000); summoned.discard();
        var environmental = boss(helper); environmental.hurt(helper.getLevel().damageSources().generic(), 10_000); environmental.discard();
        var disabled = boss(helper); var rule = helper.getLevel().getGameRules().getRule(GameRules.RULE_DOMOBLOOT); boolean previous = rule.get();
        try { rule.set(false, helper.getLevel().getServer()); disabled.hurt(player.damageSources().playerAttack(player), 10_000); }
        finally { rule.set(previous, helper.getLevel().getServer()); }
        helper.assertTrue(charters(helper) == 1, "Summon/environment/mobLoot=false produced an extra charter");
        helper.assertTrue(charter instanceof SettlementCharterItem, "Reward is not the actual actionable charter");
        helper.succeed();
    }

    private static java.util.List<DungeonsMerchant> settlers(GameTestHelper helper) {
        return helper.getLevel().getEntitiesOfClass(DungeonsMerchant.class, bounds(helper), entity -> entity.getTags().contains(SettlementService.SETTLER_TAG));
    }
    private static long charters(GameTestHelper helper) {
        var drops = helper.getLevel().getEntitiesOfClass(ItemEntity.class, bounds(helper));
        drops.forEach(drop -> track(helper, drop));
        return drops.stream().filter(drop -> drop.getItem().getItem() instanceof SettlementCharterItem).mapToLong(drop -> drop.getItem().getCount()).sum();
    }
    private static AABB bounds(GameTestHelper helper) {
        Vec3 origin = helper.absoluteVec(Vec3.ZERO); return new AABB(origin.x, origin.y, origin.z, origin.x + 20, origin.y + 10, origin.z + 12);
    }
    private static SettlementCharterItem charter(GameTestHelper helper) {
        var item = BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath(DungeonsAlive.MOD_ID, "settlement_charter"));
        helper.assertTrue(item instanceof SettlementCharterItem, "Missing actual charter registration"); return (SettlementCharterItem)item;
    }
    private static FakePlayer player(GameTestHelper helper) {
        var player = new FakePlayer(helper.getLevel(), new GameProfile(UUID.randomUUID(), "settlement-test"));
        player.getAbilities().instabuild = false; player.getAbilities().mayBuild = true;
        player.setPos(helper.absoluteVec(new Vec3(10.5, 1, 3.5))); return player;
    }
    private static UseOnContext context(FakePlayer player, BlockPos floor) {
        return new UseOnContext(player, InteractionHand.MAIN_HAND,
                new BlockHitResult(Vec3.atCenterOf(floor).add(0, .5, 0), Direction.UP, floor, false));
    }
    private static DungeonMob boss(GameTestHelper helper) {
        var boss = helper.spawnWithNoFreeWill(ModEntities.type("redstone_monstrosity"), new Vec3(10.5, 1, 6.5));
        boss.setNoAi(true); track(helper, boss); return boss;
    }
    private static void track(GameTestHelper helper, Entity entity) { ACTORS.computeIfAbsent(helper.getLevel(), ignored -> new HashSet<>()).add(entity); }
    private static void fixture(GameTestHelper helper) {
        var level = helper.getLevel();
        HISTORY.putIfAbsent(level, SettlementSavedData.get(level.getServer()).save(new CompoundTag(), level.registryAccess()));
        level.getServer().overworld().getDataStorage().set(SettlementSavedData.DATA_NAME, new SettlementSavedData());
        for (int x = 0; x < 20; x++) for (int z = 0; z < 12; z++) helper.setBlock(new BlockPos(x, 0, z), Blocks.STONE);
        var added = CHUNKS.computeIfAbsent(level, ignored -> new HashSet<>());
        BlockPos min = helper.absolutePos(new BlockPos(-8, 0, -8)), max = helper.absolutePos(new BlockPos(27, 0, 19));
        for (int x = min.getX() >> 4; x <= max.getX() >> 4; x++) for (int z = min.getZ() >> 4; z <= max.getZ() >> 4; z++) {
            if (level.setChunkForced(x, z, true)) added.add(new ChunkPos(x, z)); level.getChunk(x, z);
        }
    }
}
