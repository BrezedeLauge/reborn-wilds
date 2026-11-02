package de.laugenbreze.dungeonsalive.entity.peaceful;

import com.mojang.authlib.GameProfile;
import de.laugenbreze.dungeonsalive.DungeonsAlive;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
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
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.util.FakePlayer;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

/** Native spawn provenance, role transitions and save compatibility; not an ambient-spawn simulation. */
@GameTestHolder(DungeonsAlive.MOD_ID)
@PrefixGameTestTemplate(false)
public final class NaturalCompanionGameTests {
    private static final String BATCH = "natural_companions";
    private static final Map<ServerLevel, Set<Entity>> ACTORS = new IdentityHashMap<>();
    private NaturalCompanionGameTests() {}
    @AfterBatch(batch = BATCH) public static void cleanup(ServerLevel level) {
        var actors = ACTORS.remove(level);
        if (actors != null) actors.forEach(Entity::discard);
    }

    @GameTest(template = "gametest_empty", templateNamespace = DungeonsAlive.MOD_ID, batch = BATCH, timeoutTicks = 30)
    public static void optedInNaturalCompanionsShareBoundedPacksAndRestoreWildRoleFromNbt(GameTestHelper helper) {
        floor(helper);
        for (var profile : PeacefulProfiles.all()) {
            if (profile.kind() != PeacefulProfiles.Kind.PET || !profile.wildlifeEligible()) continue;
            boolean savedFamily = profile.id().equals("pet_duck");
            SpawnGroupData pack = null;
            DungeonsCreature first = null;
            for (int index = 0; index < 7; index++) {
                var animal = create(helper, profile.id());
                pack = animal.finalizeSpawn(helper.getLevel(), helper.getLevel().getCurrentDifficultyAt(animal.blockPosition()), MobSpawnType.NATURAL, pack);
                helper.assertTrue(animal.naturalWildlife() && animal.groupWildlife() && animal.isPersistenceRequired() == savedFamily
                                && animal.getMaxSpawnClusterSize() == 6 && animal.ownerId() == null,
                        "Natural companion lost its exact bounded wildlife lifetime: " + profile.id());
                if (savedFamily) helper.assertTrue(animal.duckFamily() && animal.duckYoung() == (index > 0 && index < 6),
                        "Bounded duck pack did not retain its adult/young assignment");
                assertWildNavigation(helper, animal);
                if (first == null) first = animal;
                else helper.assertTrue(first.wildlifeGroupId().equals(animal.wildlifeGroupId()) == (index < 6),
                        "Natural spawn group split too early or admitted a seventh member: " + profile.id());
            }
            CompoundTag saved = save(first);
            helper.assertTrue(saved.getBoolean("DungeonsWildCompanion") && saved.getBoolean("DungeonsNaturalWildlife")
                    && saved.getBoolean("PersistenceRequired") == savedFamily, "Natural companion provenance/lifetime was not explicitly saved");
            var restored = create(helper, profile.id()); restored.load(saved);
            helper.assertTrue(restored.naturalWildlife() && restored.groupWildlife() && restored.isPersistenceRequired() == savedFamily
                            && first.wildlifeGroupId().equals(restored.wildlifeGroupId())
                            && first.wildlifeAnchor().equals(restored.wildlifeAnchor()),
                    "Full NBT reload lost natural lifetime/group binding: " + profile.id());
            assertWildNavigation(helper, restored);
        }
        helper.succeed();
    }

    @GameTest(template = "gametest_empty", templateNamespace = DungeonsAlive.MOD_ID, batch = BATCH, timeoutTicks = 30)
    public static void realOwnershipInteractionEndsWildRoleAndRestoresTheExactCompanionNavigation(GameTestHelper helper) {
        floor(helper);
        var owner = new FakePlayer(helper.getLevel(), new GameProfile(UUID.randomUUID(), "natural-owner")); owner.setGameMode(GameType.SURVIVAL);
        var stranger = new FakePlayer(helper.getLevel(), new GameProfile(UUID.randomUUID(), "natural-stranger")); stranger.setGameMode(GameType.SURVIVAL);
        for (var profile : PeacefulProfiles.all()) {
            if (profile.kind() != PeacefulProfiles.Kind.PET || !profile.wildlifeEligible()) continue;
            var animal = create(helper, profile.id());
            PathNavigation originalNavigation = animal.getNavigation();
            animal.finalizeSpawn(helper.getLevel(), helper.getLevel().getCurrentDifficultyAt(animal.blockPosition()), MobSpawnType.NATURAL, null);
            animal.setNoAi(true);
            helper.assertTrue(helper.getLevel().addFreshEntity(animal), "Could not add actual companion fixture");
            owner.setPos(animal.position().add(0, 0, 1.5)); stranger.setPos(owner.position());
            animal.mobInteract(owner, InteractionHand.MAIN_HAND);
            helper.assertTrue(owner.getUUID().equals(animal.ownerId()) && animal.isPersistenceRequired()
                            && !animal.naturalWildlife() && !animal.naturalWildBird() && !animal.groupWildlife()
                            && animal.wildlifeGroupId() == null && animal.getNavigation() == originalNavigation
                            && !animal.waiting() && animal.isNoAi(),
                    "Care did not restore the ordinary persistent companion without altering NoAI: " + profile.id());
            animal.mobInteract(stranger, InteractionHand.MAIN_HAND);
            helper.assertTrue(owner.getUUID().equals(animal.ownerId()) && !animal.waiting(), "A stranger stole or toggled the companion");
            animal.mobInteract(owner, InteractionHand.MAIN_HAND);
            helper.assertTrue(animal.waiting(), "The existing owner wait interaction stopped working");
            CompoundTag saved = save(animal);
            helper.assertFalse(saved.getBoolean("DungeonsWildCompanion"), "A tamed companion kept its natural-role marker");
            var restored = create(helper, profile.id()); restored.load(saved);
            helper.assertTrue(owner.getUUID().equals(restored.ownerId()) && restored.waiting() && restored.isPersistenceRequired()
                            && !restored.groupWildlife() && !restored.naturalWildlife(), "Tamed ownership/wait state did not survive reload");
            animal.discard();
        }
        helper.succeed();
    }

    @GameTest(template = "gametest_empty", templateNamespace = DungeonsAlive.MOD_ID, batch = BATCH, timeoutTicks = 30)
    public static void spawnEggsLegacyPetsAndOwnedSavesNeverBecomeWildByEligibilityAlone(GameTestHelper helper) {
        floor(helper);
        for (var profile : PeacefulProfiles.all()) {
            if (profile.kind() != PeacefulProfiles.Kind.PET || !profile.wildlifeEligible()) continue;
            var manual = create(helper, profile.id()); manual.setNoAi(true);
            manual.finalizeSpawn(helper.getLevel(), helper.getLevel().getCurrentDifficultyAt(manual.blockPosition()), MobSpawnType.SPAWN_EGG, null);
            helper.assertTrue(manual.isPersistenceRequired() && !manual.groupWildlife() && !manual.naturalWildlife(),
                    "A spawn egg accidentally enrolled an ordinary pet: " + profile.id());
            CompoundTag legacy = save(manual);
            legacy.remove("DungeonsWildCompanion"); legacy.remove("DungeonsNaturalWildlife");
            var restored = create(helper, profile.id()); restored.load(legacy);
            helper.assertTrue(restored.isPersistenceRequired() && restored.isNoAi() && !restored.groupWildlife(), "An old unowned pet became wild");
            UUID owner = UUID.randomUUID();
            legacy.putUUID("DungeonsKeeper", owner); legacy.putBoolean("DungeonsWaiting", true);
            legacy.putBoolean("DungeonsWildCompanion", true); legacy.putBoolean("DungeonsNaturalWildlife", true);
            restored.load(legacy);
            helper.assertTrue(owner.equals(restored.ownerId()) && restored.waiting() && restored.isPersistenceRequired()
                            && !restored.groupWildlife() && !restored.naturalWildlife(), "Natural markers overrode a saved owner");
            legacy.remove("DungeonsKeeper"); legacy.remove("DungeonsWildCompanion");
            restored.load(legacy);
            helper.assertFalse(restored.groupWildlife(), "Generic natural-origin NBT alone enabled a pet's wild role");
        }
        var excluded = create(helper, "pet_baby_wooly_cow");
        excluded.finalizeSpawn(helper.getLevel(), helper.getLevel().getCurrentDifficultyAt(excluded.blockPosition()), MobSpawnType.NATURAL, null);
        helper.assertTrue(excluded.isPersistenceRequired() && !excluded.groupWildlife() && !excluded.naturalWildlife(),
                "An unrelated baby companion was enrolled by a natural spawn reason alone");
        helper.succeed();
    }

    @GameTest(template = "gametest_empty", templateNamespace = DungeonsAlive.MOD_ID, batch = BATCH, timeoutTicks = 30)
    public static void aquaticCompanionSpawnRequiresRealWaterAndFreeBodySpace(GameTestHelper helper) {
        floor(helper);
        for (int x = 3; x <= 9; x++) for (int z = 2; z <= 8; z++) for (int y = 1; y <= 3; y++)
            if (x == 3 || x == 9 || z == 2 || z == 8) helper.setBlock(new BlockPos(x, y, z), Blocks.GLASS);
        for (int x = 4; x <= 8; x++) for (int z = 3; z <= 7; z++) for (int y = 1; y <= 3; y++)
            helper.setBlock(new BlockPos(x, y, z), Blocks.WATER);
        var squid = create(helper, "pet_baby_squid");
        squid.setPos(helper.absoluteVec(new Vec3(6.5, 1.2, 5.5)));
        helper.assertTrue(squid.checkSpawnObstruction(helper.getLevel()), "A real water-filled free body was rejected by the ordinary land-mob liquid check");
        squid.setPos(helper.absoluteVec(new Vec3(12.5, 1.2, 5.5)));
        helper.assertFalse(squid.checkSpawnObstruction(helper.getLevel()), "An aquatic companion spawned on dry land");
        squid.setPos(helper.absoluteVec(new Vec3(6.5, 3.7, 5.5)));
        helper.assertFalse(squid.checkSpawnObstruction(helper.getLevel()), "Water at the feet alone admitted a dry head");
        squid.setPos(helper.absoluteVec(new Vec3(6.5, 1.2, 5.5)));
        helper.setBlock(new BlockPos(6, 1, 5), Blocks.OAK_FENCE.defaultBlockState().setValue(BlockStateProperties.WATERLOGGED, true));
        helper.assertFalse(squid.checkSpawnObstruction(helper.getLevel()), "Waterlogged collision admitted an obstructed aquatic body");
        helper.succeed();
    }

    private static void assertWildNavigation(GameTestHelper helper, DungeonsCreature animal) {
        boolean correct = animal.duck() ? animal.duckFamily() && animal.getNavigation() instanceof DuckFamilyNavigation
                : animal.profile().aquatic() ? animal.getNavigation() instanceof WildlifeWaterNavigation
                : animal.profile().flying() ? animal.getNavigation() instanceof WildlifeFlightNavigation
                : animal.getNavigation() instanceof WildlifeGroundNavigation;
        helper.assertTrue(correct && animal.aquaticWildlife() == animal.profile().aquatic(), "Natural role installed the wrong movement subsystem: " + animal.profile().id());
    }
    private static DungeonsCreature create(GameTestHelper helper, String id) {
        var animal = ModPeacefulEntities.creatureType(id).create(helper.getLevel());
        helper.assertTrue(animal != null, "Missing actual companion factory: " + id);
        ACTORS.computeIfAbsent(helper.getLevel(), ignored -> new HashSet<>()).add(animal);
        animal.setPos(helper.absoluteVec(new Vec3(7.5, 1, 5.5)));
        return animal;
    }
    private static CompoundTag save(DungeonsCreature animal) {
        CompoundTag saved = new CompoundTag(); animal.saveWithoutId(saved); return saved;
    }
    private static void floor(GameTestHelper helper) {
        for (int x = 0; x < 20; x++) for (int z = 0; z < 12; z++) helper.setBlock(new BlockPos(x, 0, z), Blocks.STONE);
    }
}
