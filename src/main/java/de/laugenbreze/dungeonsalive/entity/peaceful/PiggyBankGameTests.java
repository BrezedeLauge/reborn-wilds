package de.laugenbreze.dungeonsalive.entity.peaceful;

import de.laugenbreze.dungeonsalive.DungeonsAlive;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.gametest.framework.AfterBatch;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

@GameTestHolder(DungeonsAlive.MOD_ID)
@PrefixGameTestTemplate(false)
public final class PiggyBankGameTests {
    private static final Map<ServerLevel, Set<Entity>> ACTORS = new IdentityHashMap<>();
    private static final Map<ServerLevel, Set<ChunkPos>> CHUNKS = new IdentityHashMap<>();
    private PiggyBankGameTests() {}
    @AfterBatch(batch = "piggy_escape") public static void cleanEscape(ServerLevel level) { clean(level); }
    @AfterBatch(batch = "piggy_treasure") public static void cleanTreasure(ServerLevel level) { clean(level); }
    @AfterBatch(batch = "piggy_rules") public static void cleanRules(ServerLevel level) { clean(level); }
    @AfterBatch(batch = "piggy_spawns") public static void cleanSpawns(ServerLevel level) { clean(level); }
    private static void clean(ServerLevel level) {
        var actors = ACTORS.remove(level); if (actors != null) actors.forEach(Entity::discard);
        var chunks = CHUNKS.remove(level); if (chunks != null) for (var chunk : chunks) level.setChunkForced(chunk.x, chunk.z, false);
    }

    @GameTest(template = "gametest_empty", templateNamespace = DungeonsAlive.MOD_ID, batch = "piggy_escape", timeoutTicks = 160)
    public static void actualDamageMakesARealPigFleeWithoutCoinsOrTeleportAndAlarmExpires(GameTestHelper helper) {
        floor(helper); loadNavigation(helper);
        var pig = spawn(helper, "piggy_bank", 8.5, 5.5); pig.setNoAi(false);
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        Vec3[] start = new Vec3[1], previous = new Vec3[1];
        boolean[] path = {false}, moved = {false};
        helper.runAtTickTime(4, () -> {
            player.setPos(pig.position().add(-2, 0, 0));
            start[0] = pig.position(); previous[0] = pig.position();
            helper.assertTrue(pig.hurt(player.damageSources().playerAttack(player), 1) && pig.piggyFleeing(),
                    "Accepted damage did not begin the real Piggy Bank escape");
            helper.assertTrue(!pig.groupWildlife() && pig.wildlifeGroupId() == null && pig.ownerId() == null,
                    "Treasure pig became a forced herd member or pet");
        });
        helper.onEachTick(() -> {
            if (previous[0] == null) return;
            helper.assertTrue(pig.position().distanceToSqr(previous[0]) < 1, "Treasure escape teleported the pig");
            if (pig.piggyFleeing()) {
                path[0] |= !pig.getNavigation().isDone(); moved[0] |= pig.distanceToSqr(start[0]) > 4;
                helper.assertTrue(pig.blockPosition().distSqr(pig.piggyEscapeOrigin()) <= 14 * 14,
                        "The bounded escape ran outside its local origin");
            }
            previous[0] = pig.position();
            helper.assertTrue(drops(helper, pig).isEmpty() && !pig.piggyTreasureSpent() && pig.getTarget() == null,
                    "A living damaged pig produced treasure or a combat target");
        });
        helper.runAtTickTime(135, () -> {
            helper.assertTrue(path[0] && moved[0] && !pig.piggyFleeing(), "Real local escape did not move or failed to expire");
            CompoundTag saved = save(pig); CompoundTag plan = saved.getCompound("DungeonsPiggyTreasure").copy();
            pig.readAdditionalSaveData(saved);
            helper.assertTrue(!pig.piggyFleeing() && plan.equals(save(pig).getCompound("DungeonsPiggyTreasure")),
                    "Reload resumed an old flight or rerolled the retained treasure");
            helper.succeed();
        });
    }

    @GameTest(template = "gametest_empty", templateNamespace = DungeonsAlive.MOD_ID, batch = "piggy_treasure", timeoutTicks = 20)
    public static void actualPlayerDeathDropsOneBoundedTreasureAndPredeathNbtReplayCannotPayAgain(GameTestHelper helper) {
        floor(helper);
        var pig = spawn(helper, "piggy_bank", 7.5, 5.5);
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        CompoundTag beforeDeath = save(pig);
        int expectedEmeralds = beforeDeath.getCompound("DungeonsPiggyTreasure").getInt("Emeralds");
        int expectedGear = beforeDeath.getCompound("DungeonsPiggyTreasure").getInt("Gear");
        helper.assertTrue(pig.hurt(player.damageSources().playerAttack(player), 1000) && pig.piggyTreasureSpent(),
                "An actual player death did not consume the treasure state");
        var loot = drops(helper, pig);
        helper.assertTrue(loot.size() == 2 && loot.stream().filter(item -> item.getItem().is(Items.EMERALD))
                        .mapToInt(item -> item.getItem().getCount()).sum() == expectedEmeralds
                        && expectedEmeralds >= 6 && expectedEmeralds <= 12,
                "Treasure did not create exactly one bounded emerald stack plus one gear entity");
        String gearId = PiggyBankTreasureState.GEAR.get(expectedGear);
        helper.assertTrue(loot.stream().anyMatch(item -> item.getItem().getCount() == 1
                        && BuiltInRegistries.ITEM.getKey(item.getItem().getItem()).equals(ResourceLocation.fromNamespaceAndPath(DungeonsAlive.MOD_ID, gearId))),
                "The saved ordinary equipment choice was not the actual dropped item");
        loot.forEach(Entity::discard);
        var ledger = PiggyBankSavedData.get(helper.getLevel().getServer());
        CompoundTag ledgerNbt = ledger.save(new CompoundTag(), helper.getLevel().registryAccess());
        var reloadedLedger = PiggyBankSavedData.load(ledgerNbt, helper.getLevel().registryAccess());
        helper.assertFalse(reloadedLedger.claim(pig.getUUID()), "SavedData reload forgot the paid entity identity");
        UUID identity = pig.getUUID(); pig.discard();
        var replay = ModPeacefulEntities.creatureType("piggy_bank").create(helper.getLevel());
        helper.assertTrue(replay != null, "Missing actual Piggy Bank type for NBT replay");
        track(helper, replay); replay.load(beforeDeath);
        helper.assertTrue(helper.getLevel().addFreshEntity(replay) && identity.equals(replay.getUUID()) && !replay.piggyTreasureSpent(),
                "The pre-death NBT replay fixture did not restore the same unpaid entity snapshot");
        replay.hurt(player.damageSources().playerAttack(player), 1000);
        helper.assertTrue(replay.piggyTreasureSpent() && drops(helper, replay).isEmpty(), "Old entity NBT bypassed the authoritative claim ledger");
        CompoundTag malformed = new CompoundTag(); malformed.putInt("Version", 1);
        ListTag wrongType = new ListTag(); wrongType.add(StringTag.valueOf("not-a-uuid-entry")); malformed.put("Claimed", wrongType);
        var blocked = PiggyBankSavedData.load(malformed, helper.getLevel().registryAccess());
        helper.assertFalse(blocked.claim(UUID.randomUUID()), "Malformed claim history was silently treated as an empty ledger");
        helper.assertTrue(blocked.save(new CompoundTag(), helper.getLevel().registryAccess()).equals(malformed),
                "Unknown claim data was overwritten instead of preserved");
        helper.succeed();
    }

    @GameTest(template = "gametest_empty", templateNamespace = DungeonsAlive.MOD_ID, batch = "piggy_rules", timeoutTicks = 20)
    public static void deathCancellationLootRuleNonplayersOwnershipAndOtherProfilesNeverMintTreasure(GameTestHelper helper) {
        floor(helper); Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        var canceled = spawn(helper, "piggy_bank", 7.5, 5.5);
        Consumer<LivingDeathEvent> death = event -> { if (event.getEntity() == canceled) event.setCanceled(true); };
        NeoForge.EVENT_BUS.addListener(EventPriority.HIGHEST, death);
        try { canceled.hurt(player.damageSources().playerAttack(player), 1000); }
        finally { NeoForge.EVENT_BUS.unregister(death); }
        helper.assertTrue(!canceled.piggyTreasureSpent() && drops(helper, canceled).isEmpty(), "Canceled death minted or consumed treasure");
        CompoundTag afterCancellation = PiggyBankSavedData.get(helper.getLevel().getServer())
                .save(new CompoundTag(), helper.getLevel().registryAccess());
        var claimCopy = PiggyBankSavedData.load(afterCancellation, helper.getLevel().registryAccess());
        helper.assertTrue(claimCopy.claim(canceled.getUUID()), "Canceled death incorrectly entered the authoritative claim history");
        canceled.discard();
        var environmental = spawn(helper, "piggy_bank", 7.5, 5.5);
        environmental.hurt(helper.getLevel().damageSources().generic(), 1000);
        helper.assertTrue(drops(helper, environmental).isEmpty(), "An environmental kill minted player treasure");
        var disabled = spawn(helper, "piggy_bank", 7.5, 5.5);
        var rule = helper.getLevel().getGameRules().getRule(GameRules.RULE_DOMOBLOOT); boolean previous = rule.get();
        try { rule.set(false, helper.getLevel().getServer()); disabled.hurt(player.damageSources().playerAttack(player), 1000); }
        finally { rule.set(previous, helper.getLevel().getServer()); }
        helper.assertTrue(drops(helper, disabled).isEmpty(), "doMobLoot=false still produced treasure");
        var owned = spawn(helper, "piggy_bank", 7.5, 5.5);
        CompoundTag ownerData = save(owned); ownerData.putUUID("DungeonsKeeper", UUID.randomUUID()); owned.readAdditionalSaveData(ownerData);
        owned.hurt(player.damageSources().playerAttack(player), 1000);
        for (String id : List.of("wooly_cow", "pet_baby_wooly_cow", "summoned_guardian_vex", "captured_blacksmith")) {
            var other = spawn(helper, id, 7.5, 5.5); other.hurt(player.damageSources().playerAttack(player), 1000);
        }
        helper.assertTrue(drops(helper, owned).isEmpty(), "Owned wildlife, pet, summoned companion or civilian received Piggy Bank treasure");
        var dropCanceled = spawn(helper, "piggy_bank", 7.5, 5.5);
        Consumer<LivingDropsEvent> cancelDrops = event -> { if (event.getEntity() == dropCanceled) event.setCanceled(true); };
        NeoForge.EVENT_BUS.addListener(EventPriority.HIGHEST, cancelDrops);
        try { dropCanceled.hurt(player.damageSources().playerAttack(player), 1000); }
        finally { NeoForge.EVENT_BUS.unregister(cancelDrops); }
        helper.assertTrue(dropCanceled.piggyTreasureSpent() && drops(helper, dropCanceled).isEmpty(),
                "Treasure bypassed the actual LivingDrops cancellation pass");
        CompoundTag afterDrops = PiggyBankSavedData.get(helper.getLevel().getServer())
                .save(new CompoundTag(), helper.getLevel().registryAccess());
        helper.assertFalse(PiggyBankSavedData.load(afterDrops, helper.getLevel().registryAccess()).claim(dropCanceled.getUUID()),
                "Canceled item drops reopened the already consumed world claim");
        helper.succeed();
    }

    @GameTest(template = "gametest_empty", templateNamespace = DungeonsAlive.MOD_ID, batch = "piggy_spawns", timeoutTicks = 20)
    public static void registeredSingletonHabitatRespectsBothSwitchesDensityAndUnloadedTargets(GameTestHelper helper) {
        floor(helper); BlockPos relative = new BlockPos(5, 1, 5), pos = helper.absolutePos(relative);
        helper.setBlock(relative.below(), Blocks.GRASS_BLOCK); helper.setBlock(relative.offset(0, 0, 3), Blocks.GLOWSTONE);
        // Give the placed block light time to propagate before changing time or checking placement.
        helper.runAtTickTime(4, () -> {
            var level = helper.getLevel(); var type = ModPeacefulEntities.creatureType("piggy_bank");
            var plains = level.registryAccess().registryOrThrow(Registries.BIOME).get(ResourceLocation.withDefaultNamespace("plains"));
            helper.assertTrue(plains != null && plains.getMobSettings().getMobs(MobCategory.CREATURE).unwrap().stream()
                            .anyMatch(entry -> entry.type == type && entry.minCount == 1 && entry.maxCount == 1),
                    "Generated biome modifier did not install the actual singleton Piggy Bank");
            long time = level.getDayTime(); boolean wildlife = WildlifeConfig.NATURAL_SPAWNS.get(), pigs = PiggyBankConfig.NATURAL_SPAWNS.get();
            int limit = WildlifeConfig.NEARBY_LIMIT.get();
            try {
                level.setDayTime(6000); WildlifeConfig.NATURAL_SPAWNS.set(true); PiggyBankConfig.NATURAL_SPAWNS.set(true); WildlifeConfig.NEARBY_LIMIT.set(64);
                var random = RandomSource.create(73); boolean accepted = false;
                for (int i = 0; i < 256; i++) accepted |= SpawnPlacements.checkSpawnRules(type, level, MobSpawnType.NATURAL, pos, random);
                helper.assertTrue(accepted, "Suitable daytime grass never admitted the real rare placement predicate");
                PiggyBankConfig.NATURAL_SPAWNS.set(false);
                helper.assertFalse(PiggyBankSpawning.canSpawn(type, level, MobSpawnType.NATURAL, pos, random), "Pig-specific switch was ignored");
                PiggyBankConfig.NATURAL_SPAWNS.set(true); WildlifeConfig.NATURAL_SPAWNS.set(false);
                helper.assertFalse(PiggyBankSpawning.canSpawn(type, level, MobSpawnType.NATURAL, pos, random), "Global wildlife switch was ignored");
                WildlifeConfig.NATURAL_SPAWNS.set(true);
                spawn(helper, "piggy_bank", 9.5, 5.5); spawn(helper, "piggy_bank", 12.5, 5.5);
                for (int i = 0; i < 64; i++) helper.assertFalse(PiggyBankSpawning.canSpawn(type, level, MobSpawnType.NATURAL, pos, random),
                        "Two local pigs did not suppress another natural treasure spawn");
                BlockPos unloaded = null;
                for (int distance : new int[]{4096, 8192, 16384}) {
                    BlockPos candidate = pos.offset(distance, 0, distance);
                    if (!level.hasChunkAt(candidate)) { unloaded = candidate; break; }
                }
                helper.assertTrue(unloaded != null, "No unloaded target was available for the non-loading fixture");
                helper.assertFalse(PiggyBankSpawning.canSpawn(type, level, MobSpawnType.NATURAL, unloaded, random), "Unloaded terrain was accepted");
                helper.assertFalse(level.hasChunkAt(unloaded), "Placement validation loaded its previously unknown target chunk");
            } finally {
                level.setDayTime(time); WildlifeConfig.NATURAL_SPAWNS.set(wildlife); PiggyBankConfig.NATURAL_SPAWNS.set(pigs); WildlifeConfig.NEARBY_LIMIT.set(limit);
            }
            helper.succeed();
        });
    }

    private static DungeonsCreature spawn(GameTestHelper helper, String id, double x, double z) {
        var entity = ModPeacefulEntities.creatureType(id).create(helper.getLevel());
        helper.assertTrue(entity != null, "Missing real creature fixture: " + id);
        entity.setNoAi(true); entity.setPos(helper.absoluteVec(new Vec3(x, 1, z))); track(helper, entity);
        helper.assertTrue(helper.getLevel().addFreshEntity(entity), "Creature fixture could not join the level"); return entity;
    }
    private static CompoundTag save(DungeonsCreature entity) { var tag = new CompoundTag(); entity.saveWithoutId(tag); return tag; }
    private static void track(GameTestHelper helper, Entity entity) { ACTORS.computeIfAbsent(helper.getLevel(), ignored -> new HashSet<>()).add(entity); }
    private static List<ItemEntity> drops(GameTestHelper helper, DungeonsCreature entity) {
        var result = helper.getLevel().getEntitiesOfClass(ItemEntity.class, entity.getBoundingBox().inflate(16));
        result.forEach(item -> track(helper, item)); return result;
    }
    private static void floor(GameTestHelper helper) { for (int x = 0; x < 20; x++) for (int z = 0; z < 12; z++) helper.setBlock(new BlockPos(x, 0, z), Blocks.STONE); }
    private static void loadNavigation(GameTestHelper helper) {
        var level = helper.getLevel(); int radius = WildlifeNavigationSafety.LOADED_RADIUS;
        BlockPos min = helper.absolutePos(new BlockPos(-radius, 0, -radius)), max = helper.absolutePos(new BlockPos(19 + radius, 0, 11 + radius));
        var chunks = CHUNKS.computeIfAbsent(level, ignored -> new HashSet<>());
        for (int x = Math.min(min.getX(), max.getX()) >> 4; x <= Math.max(min.getX(), max.getX()) >> 4; x++)
            for (int z = Math.min(min.getZ(), max.getZ()) >> 4; z <= Math.max(min.getZ(), max.getZ()) >> 4; z++) {
                if (level.setChunkForced(x, z, true)) chunks.add(new ChunkPos(x, z)); level.getChunk(x, z);
            }
    }
}
