package de.laugenbreze.dungeonsalive.entity.peaceful;

import de.laugenbreze.dungeonsalive.DungeonsAlive;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.AfterBatch;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

/** Real entity membership, walking, succession and alarm tests, distinct from pure policy checks. */
@GameTestHolder(DungeonsAlive.MOD_ID)
@PrefixGameTestTemplate(false)
public final class WildlifeHerdGameTests {
    private static final String BATCH = "wildlife_herds";
    private static final Map<ServerLevel, Set<ChunkPos>> FIXTURE_CHUNKS = new IdentityHashMap<>();
    private static final Map<ServerLevel, Set<Entity>> FIXTURE_ACTORS = new IdentityHashMap<>();
    private WildlifeHerdGameTests() {}

    @AfterBatch(batch = BATCH)
    public static void releaseFixtureChunks(ServerLevel level) {
        var actors = FIXTURE_ACTORS.remove(level);
        if (actors != null) for (var actor : actors) actor.discard();
        var chunks = FIXTURE_CHUNKS.remove(level);
        if (chunks != null) for (var chunk : chunks) level.setChunkForced(chunk.x, chunk.z, false);
    }

    @GameTest(template = "gametest_empty", templateNamespace = DungeonsAlive.MOD_ID,
            batch = BATCH, timeoutTicks = 220)
    public static void separatedCowActuallyRejoinsAndLoadedMemberSucceedsDeadLeader(GameTestHelper helper) {
        floor(helper);
        loadFixture(helper);
        List<DungeonsCreature> cows = List.of(spawn(helper, "wooly_cow", 4.5, 5.5),
                spawn(helper, "wooly_cow", 6.5, 5.5), spawn(helper, "wooly_cow", 8.5, 5.5));
        DungeonsCreature[] leader = new DungeonsCreature[1], follower = new DungeonsCreature[1];
        Vec3[] previous = new Vec3[1];
        double[] initialDistance = new double[1];
        boolean[] sawPath = {false}, sawMovement = {false};
        helper.runAtTickTime(25, () -> {
            UUID group = cows.getFirst().wildlifeGroupId();
            helper.assertTrue(group != null && cows.stream().allMatch(cow -> group.equals(cow.wildlifeGroupId())),
                    "Nearby ordinary wooly cows did not form an actual shared group");
            var ordered = cows.stream().sorted(Comparator.comparing(Entity::getUUID)).toList();
            leader[0] = ordered.getFirst(); follower[0] = ordered.getLast();
            // The stationary actors are fixtures. The separated follower retains its real goals.
            for (var cow : ordered) if (cow != follower[0]) { cow.removeFreeWill(); cow.getNavigation().stop(); }
            leader[0].setPos(helper.absoluteVec(new Vec3(6.5, 1, 5.5)));
            ordered.get(1).setPos(helper.absoluteVec(new Vec3(3.5, 1, 8.5)));
            follower[0].setPos(helper.absoluteVec(new Vec3(16.5, 1, 5.5)));
            follower[0].getNavigation().stop();
            previous[0] = follower[0].position();
            initialDistance[0] = follower[0].distanceToSqr(leader[0]);
        });
        helper.onEachTick(() -> {
            if (previous[0] == null) return;
            var current = follower[0].position();
            helper.assertTrue(current.distanceToSqr(previous[0]) < 1, "Herd reunion teleported a follower");
            sawMovement[0] |= current.distanceToSqr(helper.absoluteVec(new Vec3(16.5, 1, 5.5))) > 4;
            sawPath[0] |= !follower[0].getNavigation().isDone();
            previous[0] = current;
            helper.assertTrue(cows.stream().allMatch(cow -> cow.getTarget() == null), "Herd behavior created a combat target");
        });
        helper.succeedWhen(() -> {
            helper.assertTrue(follower[0] != null && sawPath[0] && sawMovement[0]
                            && follower[0].distanceToSqr(leader[0]) < initialDistance[0] / 2,
                    "Separated cow has not walked back toward its actual loaded leader");
            UUID group = follower[0].wildlifeGroupId();
            helper.assertTrue(leader[0].hurt(helper.getLevel().damageSources().generic(), 1000) && !leader[0].isAlive(),
                    "Leader fixture did not actually die");
            var successor = cows.stream().filter(Entity::isAlive).min(Comparator.comparing(Entity::getUUID)).orElseThrow();
            helper.assertTrue(follower[0].wildlifeLeader() == successor && group.equals(successor.wildlifeGroupId()),
                    "A dead leader prevented local deterministic succession");
        });
    }

    @GameTest(template = "gametest_empty", templateNamespace = DungeonsAlive.MOD_ID,
            batch = BATCH, timeoutTicks = 180)
    public static void groupCapOwnerIsolationAlarmAndFullNbtSurviveRealEntityLifecycle(GameTestHelper helper) {
        floor(helper);
        List<DungeonsCreature> cows = new ArrayList<>();
        for (int i = 0; i < 7; i++) cows.add(spawn(helper, "wooly_cow", 3.5 + i * 2, 5.5));
        var pet = spawn(helper, "pet_baby_wooly_cow", 5.5, 9.5);
        var civilian = spawn(helper, "captured_blacksmith", 7.5, 9.5);
        var owned = spawn(helper, "wooly_cow", 9.5, 9.5);
        CompoundTag ownerData = new CompoundTag(); owned.addAdditionalSaveData(ownerData);
        UUID keeper = UUID.randomUUID(); ownerData.putUUID("DungeonsKeeper", keeper); owned.readAdditionalSaveData(ownerData);
        List<DungeonsCreature> alarmed = new ArrayList<>();
        DungeonsCreature[] outsider = new DungeonsCreature[1], restored = new DungeonsCreature[1];
        long[] deadline = {Long.MIN_VALUE};
        helper.runAtTickTime(25, () -> {
            var groups = cows.stream().collect(Collectors.groupingBy(DungeonsCreature::wildlifeGroupId));
            helper.assertTrue(groups.size() == 2 && groups.values().stream().anyMatch(group -> group.size() == 6),
                    "Seven local cows must reserve one six-member group and a separate group");
            var group = groups.values().stream().filter(members -> members.size() == 6).findFirst().orElseThrow();
            outsider[0] = groups.values().stream().filter(members -> members.size() == 1).findFirst().orElseThrow().getFirst();
            helper.assertTrue(!pet.groupWildlife() && !civilian.groupWildlife() && !owned.groupWildlife()
                            && pet.wildlifeGroupId() == null && civilian.wildlifeGroupId() == null && owned.wildlifeGroupId() == null
                            && keeper.equals(owned.ownerId()), "Group adoption changed a pet, civilian or existing owner");
            var victim = group.getFirst();
            var attacker = helper.makeMockPlayer(GameType.SURVIVAL); attacker.setPos(victim.position().add(-2, 0, 0));
            helper.assertTrue(victim.hurt(helper.getLevel().damageSources().playerAttack(attacker), 1), "Group alarm damage was not accepted");
            deadline[0] = helper.getLevel().getGameTime() + WildlifeGroupPolicy.ALARM_TICKS;
            helper.assertTrue(group.stream().allMatch(cow -> cow.wildlifeAlarmUntil() == deadline[0])
                            && outsider[0].wildlifeAlarmUntil() < helper.getLevel().getGameTime()
                            && pet.wildlifeAlarmUntil() < helper.getLevel().getGameTime(), "Alarm escaped its local actual group");
            UUID id = victim.getUUID(), groupId = victim.wildlifeGroupId(); int slot = victim.wildlifeSlot();
            BlockPos anchor = victim.wildlifeAnchor();
            CompoundTag saved = new CompoundTag(); victim.saveWithoutId(saved); victim.discard();
            restored[0] = ModPeacefulEntities.creatureType("wooly_cow").create(helper.getLevel());
            helper.assertTrue(restored[0] != null, "Missing wooly cow factory for NBT reload");
            track(helper, restored[0]);
            restored[0].load(saved);
            helper.assertTrue(helper.getLevel().addFreshEntity(restored[0]), "Full NBT group fixture could not be reloaded");
            helper.assertTrue(id.equals(restored[0].getUUID()) && groupId.equals(restored[0].wildlifeGroupId())
                            && slot == restored[0].wildlifeSlot() && anchor.equals(restored[0].wildlifeAnchor())
                            && restored[0].isPersistenceRequired(), "Reload lost group identity, slot, anchor or manual persistence");
            alarmed.addAll(group.stream().filter(cow -> cow != victim).toList());
        });
        helper.runAtTickTime(155, () -> {
            helper.assertTrue(helper.getLevel().getGameTime() >= deadline[0]
                            && alarmed.stream().allMatch(cow -> cow.wildlifeActivity() != WildlifeGroupPolicy.Activity.FLEE),
                    "A damage alarm did not expire at its finite server-time deadline");
            helper.assertTrue(restored[0].getTarget() == null && outsider[0].getTarget() == null && pet.ownerId() == null
                            && !civilian.rescued() && civilian.waiting(), "Group lifecycle changed unrelated interaction/combat state");
            helper.succeed();
        });
    }

    private static DungeonsCreature spawn(GameTestHelper helper, String id, double x, double z) {
        var creature = ModPeacefulEntities.creatureType(id).create(helper.getLevel());
        helper.assertTrue(creature != null, "Missing creature factory: " + id);
        track(helper, creature);
        creature.setPos(helper.absoluteVec(new Vec3(x, 1, z)));
        helper.assertTrue(helper.getLevel().addFreshEntity(creature), "Could not spawn " + id);
        return creature;
    }
    private static void track(GameTestHelper helper, Entity actor) {
        FIXTURE_ACTORS.computeIfAbsent(helper.getLevel(), ignored -> new HashSet<>()).add(actor);
    }
    private static void floor(GameTestHelper helper) {
        for (int x = 0; x < 20; x++) for (int z = 0; z < 12; z++) helper.setBlock(new BlockPos(x, 0, z), Blocks.STONE);
    }
    private static void loadFixture(GameTestHelper helper) {
        var level = helper.getLevel(); int radius = WildlifeNavigationSafety.LOADED_RADIUS;
        BlockPos min = helper.absolutePos(new BlockPos(-radius, 0, -radius));
        BlockPos max = helper.absolutePos(new BlockPos(19 + radius, 0, 11 + radius));
        var added = FIXTURE_CHUNKS.computeIfAbsent(level, ignored -> new HashSet<>());
        for (int x = Math.min(min.getX(), max.getX()) >> 4; x <= Math.max(min.getX(), max.getX()) >> 4; x++)
            for (int z = Math.min(min.getZ(), max.getZ()) >> 4; z <= Math.max(min.getZ(), max.getZ()) >> 4; z++) {
                if (level.setChunkForced(x, z, true)) added.add(new ChunkPos(x, z));
                level.getChunk(x, z);
            }
    }
}
