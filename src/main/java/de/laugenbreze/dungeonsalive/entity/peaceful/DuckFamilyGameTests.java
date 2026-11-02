package de.laugenbreze.dungeonsalive.entity.peaceful;

import de.laugenbreze.dungeonsalive.DungeonsAlive;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.AfterBatch;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

/** Passive real-AI cycles and saved family recovery on a shallow pond; no phase setters or clock edits. */
@GameTestHolder(DungeonsAlive.MOD_ID)
@PrefixGameTestTemplate(false)
public final class DuckFamilyGameTests {
    private static final String CYCLE = "duck_family_cycle", RECOVERY = "duck_family_recovery", POLICY = "duck_family_policy",
            SMALL_POND = "duck_family_small_pond";
    private static final Map<ServerLevel, Set<Entity>> ACTORS = new IdentityHashMap<>();
    private static final Map<ServerLevel, Set<ChunkPos>> CHUNKS = new IdentityHashMap<>();
    private DuckFamilyGameTests() { }
    @AfterBatch(batch = CYCLE) public static void cleanCycle(ServerLevel level) { cleanup(level); }
    @AfterBatch(batch = RECOVERY) public static void cleanRecovery(ServerLevel level) { cleanup(level); }
    @AfterBatch(batch = POLICY) public static void cleanPolicy(ServerLevel level) { cleanup(level); }
    @AfterBatch(batch = SMALL_POND) public static void cleanSmallPond(ServerLevel level) { cleanup(level); }
    private static void cleanup(ServerLevel level) {
        var actors = ACTORS.remove(level); if (actors != null) actors.forEach(Entity::discard);
        var chunks = CHUNKS.remove(level); if (chunks != null) chunks.forEach(pos -> level.setChunkForced(pos.x, pos.z, false));
    }

    @GameTest(template = "gametest_empty", templateNamespace = DungeonsAlive.MOD_ID, batch = CYCLE,
            skyAccess = true, timeoutTicks = 1800)
    public static void passiveFamilyActuallySwimsWalksOntoShoreAndCompletesItsCycle(GameTestHelper helper) {
        pond(helper);
        var family = family(helper);
        var adult = family.getFirst();
        Vec3[] starts = family.stream().map(Entity::position).toArray(Vec3[]::new);
        Vec3[] previous = starts.clone();
        boolean[] paths = new boolean[3], water = new boolean[3], dry = new boolean[3];
        double[] actualSwimming = new double[3];
        var phases = EnumSet.noneOf(DuckFamilyState.Activity.class);
        boolean[] finished = {false};
        helper.onEachTick(() -> {
            if (finished[0]) return;
            phases.add(adult.duckActivity());
            for (int i = 0; i < family.size(); i++) {
                var member = family.get(i);
                helper.assertTrue(member.isAlive() && member.ownerId() == null && member.duckFamily()
                                && member.getTarget() == null && member.isPersistenceRequired(), "Passive family lost a real peaceful member");
                helper.assertTrue(member.position().distanceToSqr(previous[i]) < 1, "A duck teleported instead of swimming/walking");
                if (member.isInWater() && member.duckActivity() == DuckFamilyState.Activity.SWIM)
                    actualSwimming[i] += member.position().subtract(previous[i]).horizontalDistance();
                previous[i] = member.position();
                paths[i] |= !member.getNavigation().isDone();
                water[i] |= member.isInWater();
                dry[i] |= water[i] && !member.isInWater() && member.onGround();
            }
            if (!phases.containsAll(EnumSet.of(DuckFamilyState.Activity.TO_SHORE, DuckFamilyState.Activity.FORAGE,
                    DuckFamilyState.Activity.PREEN, DuckFamilyState.Activity.REST, DuckFamilyState.Activity.TO_WATER))
                    || adult.duckActivity() != DuckFamilyState.Activity.SWIM || !family.stream().allMatch(Entity::isInWater)) return;
            for (int i = 0; i < family.size(); i++) {
                helper.assertTrue(paths[i] && water[i] && dry[i] && family.get(i).distanceToSqr(starts[i]) > 1,
                        "Member " + i + " lacks real own water/shore movement: " + diagnostic(family.get(i)));
                helper.assertTrue(actualSwimming[i] > 3, "Member " + i + " did not really swim a substantial horizontal family route: " + actualSwimming[i]);
                helper.assertTrue(family.get(i).distanceToSqr(adult) < 25, "Family did not regroup after returning to water");
            }
            finished[0] = true; helper.succeed();
        });
        helper.runAtTickTime(1790, () -> helper.assertTrue(finished[0], "Incomplete passive cycle " + phases + " "
                + family.stream().map(DuckFamilyGameTests::diagnostic).toList()));
    }

    @GameTest(template = "gametest_empty", templateNamespace = DungeonsAlive.MOD_ID, batch = SMALL_POND,
            skyAccess = true, timeoutTicks = 1600)
    public static void smallPondFamilyUsesItsRealBankAndReturnsWithoutAnImpossibleSwimmingQuota(GameTestHelper helper) {
        pond(helper);
        for (int x = 3; x <= 13; x++) for (int z = 2; z <= 9; z++)
            if (x < 8 || x > 9 || z < 5 || z > 6) helper.setBlock(new BlockPos(x, 1, z), Blocks.GRASS_BLOCK);
        var family = family(helper, List.of(new Vec3(8.5, 1.8, 5.5), new Vec3(9.5, 1.8, 5.5), new Vec3(8.5, 1.8, 6.5)));
        var adult = family.getFirst();
        Vec3[] previous = family.stream().map(Entity::position).toArray(Vec3[]::new);
        boolean[] paths = new boolean[3], water = new boolean[3], dry = new boolean[3];
        var phases = EnumSet.noneOf(DuckFamilyState.Activity.class);
        boolean[] finished = {false};
        helper.onEachTick(() -> {
            if (finished[0]) return;
            phases.add(adult.duckActivity());
            for (int i = 0; i < family.size(); i++) {
                var member = family.get(i);
                helper.assertTrue(member.isAlive() && member.duckFamily() && member.ownerId() == null,
                        "Small pond lost a real family member");
                helper.assertTrue(member.position().distanceToSqr(previous[i]) < 1, "Small pond recovery teleported a duck");
                previous[i] = member.position();
                paths[i] |= !member.getNavigation().isDone();
                water[i] |= member.isInWater();
                dry[i] |= water[i] && !member.isInWater() && member.onGround();
            }
            if (!phases.containsAll(EnumSet.of(DuckFamilyState.Activity.TO_SHORE, DuckFamilyState.Activity.FORAGE,
                    DuckFamilyState.Activity.PREEN, DuckFamilyState.Activity.REST, DuckFamilyState.Activity.TO_WATER))
                    || adult.duckActivity() != DuckFamilyState.Activity.SWIM || !family.stream().allMatch(Entity::isInWater)) return;
            for (int i = 0; i < family.size(); i++) helper.assertTrue(paths[i] && water[i] && dry[i]
                            && family.get(i).distanceToSqr(adult) < 25,
                    "Small pond member did not really use both media and regroup: " + diagnostic(family.get(i)));
            for (int x = 3; x <= 13; x++) for (int z = 2; z <= 9; z++)
                helper.assertTrue(helper.getBlockState(new BlockPos(x, 1, z)).is(
                                x >= 8 && x <= 9 && z >= 5 && z <= 6 ? Blocks.WATER : Blocks.GRASS_BLOCK),
                        "The family modified its limited pond or bank");
            finished[0] = true; helper.succeed();
        });
        helper.runAtTickTime(1590, () -> helper.assertTrue(finished[0], "Small pond cycle never recovered " + phases + " "
                + family.stream().map(DuckFamilyGameTests::diagnostic).toList()));
    }

    @GameTest(template = "gametest_empty", templateNamespace = DungeonsAlive.MOD_ID, batch = RECOVERY,
            skyAccess = true, timeoutTicks = 2200)
    public static void savedYoungKeepsParentAndRecoversFromAlarmAndChangedWaterEdge(GameTestHelper helper) {
        pond(helper);
        var family = family(helper);
        var adult = family.getFirst();
        boolean[] alarmed = {false}, reloadPending = {false}, reloaded = {false}, sawFlee = {false}, changed = {false}, finished = {false};
        BlockPos[] previousWater = new BlockPos[1];
        Vec3[] afterReload = new Vec3[1];
        helper.onEachTick(() -> {
            if (finished[0]) return;
            if (!changed[0] && adult.duckActivity() == DuckFamilyState.Activity.SWIM && adult.duckDestination() != null)
                previousWater[0] = adult.duckDestination();
            if (!alarmed[0] && adult.duckActivity() == DuckFamilyState.Activity.FORAGE) {
                var attacker = helper.makeMockPlayer(GameType.SURVIVAL);
                attacker.setPos(adult.position().add(1, 0, 1));
                helper.assertTrue(adult.hurt(helper.getLevel().damageSources().playerAttack(attacker), 1), "Family alarm damage was rejected");
                alarmed[0] = true;
            }
            sawFlee[0] |= adult.duckActivity() == DuckFamilyState.Activity.FLEE;
            if (sawFlee[0] && !reloadPending[0] && !reloaded[0]) {
                reloadPending[0] = true;
                var original = family.get(1);
                CompoundTag saved = new CompoundTag(); original.saveWithoutId(saved);
                var uuid = original.getUUID(); var group = original.wildlifeGroupId(); var parent = original.duckParent();
                int youngTicks = original.duckState().youngTicks();
                original.discard();
                helper.runAfterDelay(2, () -> {
                    var restored = ModPeacefulEntities.creatureType("pet_duck").create(helper.getLevel());
                    helper.assertTrue(restored != null, "Missing existing duck registry type");
                    restored.load(saved); track(helper, restored);
                    helper.assertTrue(helper.getLevel().addFreshEntity(restored), "Full same-UUID family reload rejected");
                    helper.assertTrue(uuid.equals(restored.getUUID()) && group.equals(restored.wildlifeGroupId())
                                    && parent.equals(restored.duckParent()) && parent.equals(adult.getUUID())
                                    && restored.duckState().youngTicks() == youngTicks && restored.duckYoung()
                                    && restored.duckState().activity().ordinal() == saved.getCompound("DungeonsDuckFamily").getInt("Activity")
                                    && restored.duckState().ticks() == saved.getCompound("DungeonsDuckFamily").getInt("Ticks")
                                    && restored.duckFamily() && restored.isPersistenceRequired()
                                    && Math.abs(restored.getScale() - .55F) < .001F,
                            "Reload changed family identity, parent, young age, body or persistence");
                    family.set(1, restored); afterReload[0] = restored.position();
                    reloadPending[0] = false; reloaded[0] = true;
                });
            }
            if (reloaded[0] && !changed[0] && adult.duckActivity() == DuckFamilyState.Activity.REST
                    && previousWater[0] != null && family.stream().noneMatch(Entity::isInWater)) {
                // A real changed shore edge after everyone has left the water. The
                // AI must discover another usable entry; no phase/target is forced.
                helper.getLevel().setBlock(previousWater[0], Blocks.STONE.defaultBlockState(), Block.UPDATE_ALL);
                changed[0] = true;
            }
            if (!changed[0] || adult.duckActivity() != DuckFamilyState.Activity.SWIM
                    || !family.stream().allMatch(Entity::isInWater)) return;
            helper.assertTrue(sawFlee[0] && adult.wildlifeAlarmUntil() <= helper.getLevel().getGameTime(), "Alarm did not end");
            helper.assertTrue(family.get(1).distanceToSqr(afterReload[0]) > 1
                            && family.get(1).distanceToSqr(adult) < 25, "Reloaded juvenile did not move and rejoin");
            helper.assertTrue(helper.getLevel().getBlockState(previousWater[0]).is(Blocks.STONE), "Duck restored or destroyed the changed edge");
            helper.assertTrue(family.stream().allMatch(member -> member.getTarget() == null && member.isAlive()), "Family recovery retaliated/lost a member");
            finished[0] = true; helper.succeed();
        });
        helper.runAtTickTime(2190, () -> helper.assertTrue(finished[0], "Recovery incomplete alarm=" + alarmed[0]
                + " reload=" + reloaded[0] + " changed=" + changed[0] + " "
                + family.stream().map(DuckFamilyGameTests::diagnostic).toList()));
    }

    @GameTest(template = "gametest_empty", templateNamespace = DungeonsAlive.MOD_ID, batch = POLICY,
            skyAccess = true, timeoutTicks = 30)
    public static void naturalFamilyAdmissionIsBoundedAndManualPetsAndUnknownChunksStayUnchanged(GameTestHelper helper) {
        pond(helper);
        var level = helper.getLevel();
        var manual = ModPeacefulEntities.creatureType("pet_duck").create(level);
        helper.assertTrue(manual != null, "Existing duck factory is missing");
        manual.setPos(helper.absoluteVec(new Vec3(17.5, 2, 5.5)));
        manual.finalizeSpawn(level, level.getCurrentDifficultyAt(manual.blockPosition()), MobSpawnType.SPAWN_EGG, null);
        track(helper, manual); helper.assertTrue(level.addFreshEntity(manual), "Could not add manual pet");
        helper.runAtTickTime(4, () -> {
            helper.assertFalse(manual.duckFamily() || manual.duckYoung() || manual.naturalWildlife(), "Manual duck became a natural family");
            var player = helper.makeMockPlayer(GameType.SURVIVAL); player.setPos(manual.position().add(1, 0, 0));
            manual.mobInteract(player, InteractionHand.MAIN_HAND);
            helper.assertTrue(player.getUUID().equals(manual.ownerId()) && !manual.duckFamily(), "Existing manual duck ownership changed");
            boolean natural = WildlifeConfig.NATURAL_SPAWNS.get(), encounters = WildlifeConfig.EXPLORATION_ENCOUNTERS.get();
            boolean spawning = level.getGameRules().getBoolean(GameRules.RULE_DOMOBSPAWNING);
            try {
                WildlifeConfig.NATURAL_SPAWNS.set(true); WildlifeConfig.EXPLORATION_ENCOUNTERS.set(true);
                level.getGameRules().getRule(GameRules.RULE_DOMOBSPAWNING).set(true, level.getServer());
                var population = new WildlifeHabitats.Population("pet_duck", WildlifeHabitats.Layer.WATER_SURFACE, 3, 5);
                BlockPos center = helper.absolutePos(new BlockPos(8, 1, 5));
                helper.assertTrue(WildlifeEncounters.spawnGroup(level, population, center, 2, false) == 0,
                        "A reserved capacity of two produced an incomplete family");
                helper.assertTrue(WildlifeEncounters.spawnGroup(level, population, center, 3, false) == 3,
                        "A real surface-water family could not be admitted");
                var members = level.getEntitiesOfClass(DungeonsCreature.class, new AABB(center).inflate(12), DungeonsCreature::duckFamily);
                members.forEach(member -> track(helper, member));
                helper.assertTrue(members.size() == 3 && members.stream().filter(DungeonsCreature::duckYoung).count() == 2
                        && members.stream().allMatch(Entity::isAlive), "Admission did not create one adult and two juveniles");
                var adult = members.stream().filter(member -> !member.duckYoung()).findFirst().orElseThrow();
                helper.assertTrue(members.stream().allMatch(member -> !member.duckYoung() || adult.getUUID().equals(member.duckParent())),
                        "Spawned juveniles lack the actual adult parent");
                BlockPos absent = null;
                for (int distance : new int[]{512, 1024, 2048, 4096}) {
                    BlockPos candidate = center.offset(distance, 0, distance);
                    if (!level.hasChunk(candidate.getX() >> 4, candidate.getZ() >> 4)) { absent = candidate; break; }
                }
                helper.assertTrue(absent != null, "No unloaded candidate for a bounded negative path test");
                helper.assertTrue(adult.getNavigation().createPath(absent, 0) == null, "Family accepted an unknown destination");
                helper.assertFalse(level.hasChunk(absent.getX() >> 4, absent.getZ() >> 4), "Rejected duck route loaded its target chunk");
                // Natural provenance can outlive missing/rejected group metadata.
                // A real first AI tick must rebuild it before the swim sampler uses a UUID.
                CompoundTag withoutGroup = new CompoundTag();
                members.stream().filter(DungeonsCreature::duckYoung).findFirst().orElseThrow().saveWithoutId(withoutGroup);
                withoutGroup.remove("DungeonsWildlifeGroup");
                withoutGroup.putUUID("UUID", java.util.UUID.randomUUID());
                var recovering = ModPeacefulEntities.creatureType("pet_duck").create(level);
                helper.assertTrue(recovering != null, "Missing reload factory");
                recovering.load(withoutGroup); track(helper, recovering);
                helper.assertTrue(recovering.duckFamily() && recovering.wildlifeGroupId() == null,
                        "Missing-group fixture did not preserve natural provenance separately");
                helper.assertTrue(level.addFreshEntity(recovering), "Could not admit the missing-group reload fixture");
                helper.runAfterDelay(4, () -> {
                    helper.assertTrue(recovering.isAlive() && !recovering.isNoAi() && recovering.wildlifeGroupId() != null
                                    && recovering.wildlifeAnchor() != null && recovering.duckYoung()
                                    && adult.getUUID().equals(recovering.duckParent()),
                            "Actual AI failed to reconstruct missing group data while retaining its saved parent");
                    helper.succeed();
                });
            } finally {
                WildlifeConfig.NATURAL_SPAWNS.set(natural); WildlifeConfig.EXPLORATION_ENCOUNTERS.set(encounters);
                level.getGameRules().getRule(GameRules.RULE_DOMOBSPAWNING).set(spawning, level.getServer());
            }
        });
    }

    private static List<DungeonsCreature> family(GameTestHelper helper) {
        return family(helper, List.of(new Vec3(8.5, 1.8, 4.5), new Vec3(8.5, 1.8, 5.5), new Vec3(8.5, 1.8, 6.5)));
    }

    private static List<DungeonsCreature> family(GameTestHelper helper, List<Vec3> positions) {
        var family = new ArrayList<DungeonsCreature>(); SpawnGroupData group = null;
        for (Vec3 position : positions) {
            var duck = ModPeacefulEntities.creatureType("pet_duck").create(helper.getLevel());
            helper.assertTrue(duck != null, "Missing actual duck registry type");
            duck.setPos(helper.absoluteVec(position));
            group = duck.finalizeSpawn(helper.getLevel(), helper.getLevel().getCurrentDifficultyAt(duck.blockPosition()), MobSpawnType.NATURAL, group);
            track(helper, duck); helper.assertTrue(helper.getLevel().addFreshEntity(duck), "Natural family fixture was rejected");
            family.add(duck);
        }
        helper.assertTrue(!family.getFirst().duckYoung() && family.get(1).duckYoung() && family.get(2).duckYoung(), "Family age assignment is wrong");
        helper.assertTrue(Math.abs(family.get(1).getBbWidth() - family.getFirst().getBbWidth() * .55F) < .001F,
                "Juvenile body was not scaled exactly once");
        return family;
    }

    private static void pond(GameTestHelper helper) {
        // Entire geometry is inside the 20x5x12 template cleanup area.
        for (int x = 0; x < 20; x++) for (int z = 0; z < 12; z++) {
            helper.setBlock(new BlockPos(x, 0, z), Blocks.STONE);
            helper.setBlock(new BlockPos(x, 1, z), x >= 3 && x <= 13 && z >= 2 && z <= 9 ? Blocks.WATER : Blocks.GRASS_BLOCK);
            for (int y = 2; y < 5; y++) helper.setBlock(new BlockPos(x, y, z), Blocks.AIR);
        }
        var level = helper.getLevel(); int radius = WildlifeNavigationSafety.LOADED_RADIUS;
        BlockPos min = helper.absolutePos(new BlockPos(-radius, 0, -radius));
        BlockPos max = helper.absolutePos(new BlockPos(19 + radius, 0, 11 + radius));
        var added = CHUNKS.computeIfAbsent(level, ignored -> new HashSet<>());
        for (int x = min.getX() >> 4; x <= max.getX() >> 4; x++) for (int z = min.getZ() >> 4; z <= max.getZ() >> 4; z++) {
            if (level.setChunkForced(x, z, true)) added.add(new ChunkPos(x, z)); level.getChunk(x, z);
        }
    }
    private static void track(GameTestHelper helper, Entity entity) { ACTORS.computeIfAbsent(helper.getLevel(), ignored -> new HashSet<>()).add(entity); }
    private static String diagnostic(DungeonsCreature duck) {
        var path = duck.getNavigation().getPath();
        return duck.position() + " activity=" + duck.duckActivity() + " young=" + duck.duckYoung() + " wet=" + duck.isInWater()
                + " ground=" + duck.onGround() + " destination=" + duck.duckDestination()
                + " path=" + (path == null ? "null" : path.getNextNodeIndex() + "/" + path.getNodeCount() + " -> " + path.getTarget());
    }
}
