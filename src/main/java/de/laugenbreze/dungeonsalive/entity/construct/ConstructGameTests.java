package de.laugenbreze.dungeonsalive.entity.construct;

import de.laugenbreze.dungeonsalive.DungeonsAlive;
import de.laugenbreze.dungeonsalive.entity.AttackKind;
import de.laugenbreze.dungeonsalive.entity.DungeonMob;
import de.laugenbreze.dungeonsalive.entity.ModEntities;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.navigation.AmphibiousPathNavigation;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

/** Risk-focused server checks: temporary geometry, legal victims, absolute lifetime and actual water navigation. */
@GameTestHolder(DungeonsAlive.MOD_ID)
@PrefixGameTestTemplate(false)
public final class ConstructGameTests {
    private ConstructGameTests() {}

    @GameTest(template = "gametest_empty", templateNamespace = DungeonsAlive.MOD_ID, batch = "constructs", timeoutTicks = 70)
    public static void wallPlacementCapCoverAndOwnerLifecycle(GameTestHelper helper) {
        floor(helper);
        DungeonMob caster = helper.spawnWithNoFreeWill(ModEntities.type("geomancer"), new Vec3(2.5, 1, 5.5));
        var target = helper.spawnWithNoFreeWill(EntityType.IRON_GOLEM, new Vec3(16.5, 1, 5.5));
        helper.assertTrue(ConstructService.spawn(caster, target, ConstructKind.GEOMANCER_WALL, target.position(), 4) == null,
                "A pillar was placed through a living occupant");
        for (int y = 1; y <= 4; y++) helper.setBlock(new BlockPos(13, y, 9), Blocks.STONE);
        helper.assertTrue(ConstructService.spawn(caster, target, ConstructKind.GEOMANCER_WALL,
                helper.absoluteVec(new Vec3(13.5, 1, 9.5)), 4) == null, "A pillar occupied a solid player-placeable block");

        List<DungeonConstruct> walls = new ArrayList<>();
        for (Vec3 pos : List.of(new Vec3(9.5, 1, 5.5), new Vec3(5.5, 1, 3.5), new Vec3(7.5, 1, 3.5),
                new Vec3(9.5, 1, 3.5), new Vec3(11.5, 1, 3.5), new Vec3(5.5, 1, 7.5),
                new Vec3(7.5, 1, 7.5), new Vec3(9.5, 1, 7.5))) {
            DungeonConstruct wall = ConstructService.spawn(caster, target, ConstructKind.GEOMANCER_WALL, helper.absoluteVec(pos), 4);
            helper.assertTrue(wall != null, "An unobstructed supported pillar failed to form");
            walls.add(wall);
        }
        helper.assertTrue(caster.constructReservationCount() == ConstructService.OWNER_CAP, "Owner reservations do not count actual constructs");
        helper.assertTrue(ConstructService.spawn(caster, target, ConstructKind.GEOMANCER_WALL,
                helper.absoluteVec(new Vec3(11.5, 1, 7.5)), 4) == null, "Per-owner construct cap was bypassed");
        CompoundTag saved = new CompoundTag();
        caster.addAdditionalSaveData(saved);
        DungeonMob restored = ModEntities.type("geomancer").create(helper.getLevel());
        restored.readAdditionalSaveData(saved);
        helper.assertTrue(restored.constructReservationCount() == ConstructService.OWNER_CAP,
                "Saving/loading the caster forgot reservations for unloaded children");
        helper.assertFalse(walls.getFirst().blocksMovement(), "A forming pillar trapped entities before its appear animation completed");

        helper.runAtTickTime(30, () -> {
            DungeonConstruct wall = walls.getFirst();
            helper.assertTrue(wall.blocksMovement() && wall.canBeCollidedWith(), "A formed wall did not become physical cover");
            helper.assertFalse(ConstructService.clearLine(helper.getLevel(), caster.getEyePosition(), target.getEyePosition(), caster),
                    "The wall failed to stop a combat line of sight");
            wall.discard();
            helper.assertTrue(caster.constructReservationCount() == ConstructService.OWNER_CAP - 1, "Destroyed wall did not release its reservation");
            caster.discard();
        });
        helper.runAtTickTime(35, () -> {
            helper.assertTrue(walls.stream().allMatch(DungeonConstruct::isRemoved), "Construct survived loss of its live owner");
            helper.assertTrue(helper.getBlockState(new BlockPos(13, 2, 9)).is(Blocks.STONE), "Placement altered a protected obstruction");
            helper.succeed();
        });
    }

    @GameTest(template = "gametest_empty", templateNamespace = DungeonsAlive.MOD_ID, batch = "constructs", timeoutTicks = 180)
    public static void geomancerBombTelegraphsOnceAndHonorsCoverAndPassives(GameTestHelper helper) {
        floor(helper);
        DungeonMob caster = helper.spawnWithNoFreeWill(ModEntities.type("geomancer"), new Vec3(3.5, 1, 5.8));
        var target = helper.spawnWithNoFreeWill(EntityType.IRON_GOLEM, new Vec3(8.5, 1, 5.8));
        var sheltered = helper.spawnWithNoFreeWill(EntityType.IRON_GOLEM, new Vec3(6.8, 1, 2.8));
        var cow = helper.spawnWithNoFreeWill(EntityType.COW, new Vec3(6.8, 1, 7.7));
        float targetBefore = target.getHealth(), coverBefore = sheltered.getHealth(), cowBefore = cow.getHealth();
        for (int x = 5; x <= 8; x++) for (int y = 1; y <= 4; y++) helper.setBlock(new BlockPos(x, y, 4), Blocks.STONE);
        DungeonConstruct[] bomb = new DungeonConstruct[1];
        float[] after = new float[1];
        helper.runAtTickTime(35, () -> helper.assertTrue(caster.beginAttack(AttackKind.FIRE_FIELD, target), "Geomancer refused its bomb action"));
        helper.runAtTickTime(65, () -> {
            helper.assertTrue(owned(helper, caster).isEmpty(), "Bomb appeared before the caster's release frame");
            helper.assertTrue(target.getHealth() == targetBefore, "Cast windup already dealt bomb damage");
        });
        helper.runAtTickTime(78, () -> {
            var objects = owned(helper, caster);
            helper.assertTrue(objects.size() == 1 && objects.getFirst().kind() == ConstructKind.GEOMANCER_BOMB,
                    "Geomancer used the old area pulse instead of a visible bomb pillar");
            bomb[0] = objects.getFirst();
            helper.assertTrue(bomb[0].phase() == DungeonConstruct.Phase.FORMING && bomb[0].getBbHeight() > 3,
                    "Bomb pillar lost its tall source shape or formation phase");
        });
        helper.runAtTickTime(120, () -> helper.assertTrue(target.getHealth() == targetBefore, "Bomb hit before its visible fuse finished"));
        helper.runAtTickTime(138, () -> {
            helper.assertTrue(target.getHealth() < targetBefore, "Armed bomb did not hit its nearby legal opponent");
            helper.assertTrue(sheltered.getHealth() == coverBefore, "Bomb damage pierced newly placed stone cover");
            helper.assertTrue(cow.getHealth() == cowBefore, "Bomb harmed a peaceful bystander");
            helper.assertTrue(bomb[0].phase() == DungeonConstruct.Phase.DECAY, "Released bomb did not begin its disappearance clip");
            after[0] = target.getHealth();
        });
        helper.runAtTickTime(175, () -> {
            helper.assertTrue(target.getHealth() == after[0], "One bomb released damage more than once");
            helper.assertTrue(bomb[0].isRemoved(), "Bomb outlived its absolute lifetime");
            helper.assertTrue(helper.getBlockState(new BlockPos(6, 0, 5)).is(Blocks.STONE)
                    && helper.getBlockState(new BlockPos(6, 2, 4)).is(Blocks.STONE), "Bomb changed player-placeable blocks");
            helper.succeed();
        });
    }

    @GameTest(template = "gametest_empty", templateNamespace = DungeonsAlive.MOD_ID, batch = "constructs", timeoutTicks = 60)
    public static void vineTargetsOnlyItsOpponentAndReleasesWhenOwnerDisappears(GameTestHelper helper) {
        floor(helper);
        DungeonMob caster = helper.spawnWithNoFreeWill(ModEntities.type("whisperer"), new Vec3(3.5, 1, 5.5));
        var target = helper.spawnWithNoFreeWill(EntityType.IRON_GOLEM, new Vec3(8.5, 1, 5.5));
        var cow = helper.spawnWithNoFreeWill(EntityType.COW, new Vec3(8.5, 1, 6.8));
        float before = target.getHealth(), passiveBefore = cow.getHealth();
        DungeonConstruct vine = ConstructService.spawn(caster, target, ConstructKind.ENTANGLE_VINE, target.position(), 6);
        helper.assertTrue(vine != null, "A supported vine could not form underneath its target");
        helper.runAtTickTime(5, () -> helper.assertTrue(target.getHealth() == before, "Vine dealt damage before forming"));
        helper.runAtTickTime(16, () -> {
            helper.assertTrue(target.getHealth() < before && target.hasEffect(MobEffects.MOVEMENT_SLOWDOWN), "Active vine failed to snare its target");
            helper.assertTrue(cow.getHealth() == passiveBefore && !cow.hasEffect(MobEffects.MOVEMENT_SLOWDOWN), "Vine affected a passive animal");
            caster.discard();
        });
        helper.runAtTickTime(35, () -> {
            helper.assertTrue(vine.isRemoved(), "Ownerless vine persisted");
            helper.assertFalse(target.hasEffect(MobEffects.MOVEMENT_SLOWDOWN), "A removed vine left its target indefinitely rooted");
            helper.assertTrue(helper.getBlockState(new BlockPos(8, 0, 5)).is(Blocks.STONE), "Vine replaced its supporting floor");
            helper.succeed();
        });
    }

    @GameTest(template = "gametest_empty", templateNamespace = DungeonsAlive.MOD_ID, batch = "constructs", skyAccess = true, timeoutTicks = 110)
    public static void iceFormsFallsHitsOnceAndExpiresWithoutTerrainDamage(GameTestHelper helper) {
        floor(helper);
        DungeonMob caster = helper.spawnWithNoFreeWill(ModEntities.type("iceologer"), new Vec3(3.5, 1, 5.5));
        var target = helper.spawnWithNoFreeWill(EntityType.IRON_GOLEM, new Vec3(8.5, 1, 5.5));
        var sheep = helper.spawnWithNoFreeWill(EntityType.SHEEP, new Vec3(8.5, 1, 7.2));
        float before = target.getHealth(), passiveBefore = sheep.getHealth();
        DungeonConstruct ice = ConstructService.spawn(caster, target, ConstructKind.ICE_CHUNK, target.position(), 6);
        helper.assertTrue(ice != null, "A clear supported air column rejected the ice chunk");
        double startY = ice.getY();
        float[] after = new float[1];
        helper.runAtTickTime(35, () -> {
            helper.assertTrue(ice.phase() == DungeonConstruct.Phase.FORMING && Math.abs(ice.getY() - startY) < .01,
                    "Ice fell before its source formation clip finished");
            helper.assertTrue(target.getHealth() == before, "Floating ice damaged a target before impact");
        });
        helper.runAtTickTime(64, () -> {
            helper.assertTrue(ice.getY() < startY - 2 && ice.phase() == DungeonConstruct.Phase.IMPACT, "Ice did not actually fall and collide with its floor");
            helper.assertTrue(target.getHealth() < before && target.hasEffect(MobEffects.MOVEMENT_SLOWDOWN), "Ice impact failed to damage and slow its opponent");
            helper.assertTrue(sheep.getHealth() == passiveBefore, "Ice impact harmed a peaceful animal");
            after[0] = target.getHealth();
        });
        helper.runAtTickTime(100, () -> {
            helper.assertTrue(ice.isRemoved(), "Impacted ice failed to expire");
            helper.assertTrue(target.getHealth() == after[0], "One ice impact applied repeated damage");
            helper.assertTrue(helper.getBlockState(new BlockPos(8, 0, 5)).is(Blocks.STONE), "Ice destroyed its collision floor");
            helper.succeed();
        });
    }

    @GameTest(template = "gametest_empty", templateNamespace = DungeonsAlive.MOD_ID, batch = "constructs", timeoutTicks = 100)
    public static void realTridentSkipsAnimalsStopsAtWallAndProducesNoPickup(GameTestHelper helper) {
        floor(helper);
        DungeonMob caster = helper.spawnWithNoFreeWill(ModEntities.type("armored_trident_drowned"), new Vec3(3.5, 1, 7.5));
        var target = helper.spawnWithNoFreeWill(EntityType.IRON_GOLEM, new Vec3(12.5, 1, 7.5));
        helper.setBlock(new BlockPos(6, 1, 7), Blocks.STONE);
        var cow = helper.spawnWithNoFreeWill(EntityType.COW, new Vec3(6.5, 2, 7.5));
        DungeonMob blockedCaster = helper.spawnWithNoFreeWill(ModEntities.type("armored_trident_drowned"), new Vec3(3.5, 1, 2.5));
        DungeonMob cancelledCaster = helper.spawnWithNoFreeWill(ModEntities.type("armored_trident_drowned"), new Vec3(3.5, 1, 9.5));
        var sheltered = helper.spawnWithNoFreeWill(EntityType.IRON_GOLEM, new Vec3(12.5, 1, 2.5));
        DungeonConstruct wall = ConstructService.spawn(blockedCaster, sheltered, ConstructKind.GEOMANCER_WALL,
                helper.absoluteVec(new Vec3(8.5, 1, 2.5)), 0);
        helper.assertTrue(wall != null, "Trident cover could not be placed");
        float before = target.getHealth(), passiveBefore = cow.getHealth(), coveredBefore = sheltered.getHealth();
        DungeonTrident orphan = ModConstructs.TRIDENT.get().create(helper.getLevel());
        orphan.setPos(helper.absoluteVec(new Vec3(5, 3, 7.5)));
        orphan.shoot(1, 0, 0, 1, 0);
        helper.getLevel().addFreshEntity(orphan);
        DungeonTrident[] blocked = new DungeonTrident[1];
        helper.runAtTickTime(35, () -> {
            helper.assertTrue(orphan.isRemoved(), "A trident with unresolved owner survived");
            helper.assertTrue(caster.beginAttack(AttackKind.SOUL_BOLT, target), "Drowned did not start its real trident throw");
            helper.assertTrue(caster.isUsingItem() && caster.getUseItem() == caster.getMainHandItem(),
                    "Throw windup did not publish the vanilla held-item throwing state");
            helper.assertTrue(cancelledCaster.beginAttack(AttackKind.SOUL_BOLT, target), "Cancelled-throw fixture could not begin");
            CompoundTag cancelledSave = new CompoundTag(); cancelledCaster.addAdditionalSaveData(cancelledSave);
            cancelledCaster.readAdditionalSaveData(cancelledSave);
            helper.assertTrue(cancelledCaster.activeAttack() == null && !cancelledCaster.isUsingItem(),
                    "Cancelling a persisted half-cast retained the throwing item state");
            blocked[0] = DungeonTrident.launch(blockedCaster, sheltered.getEyePosition(), 6);
            helper.assertTrue(blocked[0] != null && blocked[0].pickup == AbstractArrow.Pickup.DISALLOWED, "Hostile throw created an obtainable item");
            CompoundTag saved = new CompoundTag(); blocked[0].addAdditionalSaveData(saved);
            saved.putByte("pickup", (byte)AbstractArrow.Pickup.ALLOWED.ordinal());
            DungeonTrident restored = ModConstructs.TRIDENT.get().create(helper.getLevel());
            restored.readAdditionalSaveData(saved);
            helper.assertTrue(restored.pickup == AbstractArrow.Pickup.DISALLOWED, "Loading re-enabled hostile trident pickup");
        });
        helper.runAtTickTime(55, () -> {
            helper.assertTrue(target.getHealth() == before, "Trident damaged its opponent before the throw contact frame");
            helper.assertTrue(caster.isUsingItem(), "Drowned left the throwing state before its server contact frame");
        });
        helper.runAtTickTime(60, () -> {
            helper.assertFalse(caster.isUsingItem(), "Actual projectile release did not clear the throwing item state");
            var projectiles = helper.getLevel().getEntitiesOfClass(AbstractArrow.class, caster.getBoundingBox().inflate(20),
                    projectile -> projectile.getOwner() == caster);
            helper.assertTrue(projectiles.size() == 1 && projectiles.getFirst() instanceof DungeonTrident,
                    "One throw created a second item-callback projectile or no real trident");
        });
        helper.runAtTickTime(85, () -> {
            helper.assertTrue(target.getHealth() < before, "Actual thrown trident did not reach its legal opponent");
            helper.assertTrue(cow.getHealth() == passiveBefore, "Thrown trident harmed a passive animal in its trajectory");
            helper.assertTrue(sheltered.getHealth() == coveredBefore && blocked[0].isRemoved(), "Trident crossed an active physical wall");
            helper.assertTrue(caster.constructReservationCount() == 0, "Resolved trident retained its owner reservation");
            helper.assertTrue(cancelledCaster.constructReservationCount() == 0 && !cancelledCaster.isUsingItem(),
                    "Cancelled throw later produced a projectile or restored its item-use state");
            helper.assertTrue(helper.getLevel().getEntitiesOfClass(ItemEntity.class, caster.getBoundingBox().inflate(16)).isEmpty(),
                    "Hostile trident produced a farmable item entity");
            helper.succeed();
        });
    }

    @GameTest(template = "gametest_empty", templateNamespace = DungeonsAlive.MOD_ID, batch = "constructs", skyAccess = true, timeoutTicks = 100)
    public static void underwaterNavigationChangesDepthAndRestoresLandState(GameTestHelper helper) {
        floor(helper);
        for (int y = 1; y <= 7; y++) for (int x = 0; x < 20; x++) for (int z = 0; z < 12; z++) {
            helper.setBlock(new BlockPos(x, y, z), x == 0 || x == 19 || z == 0 || z == 11 ? Blocks.STONE : y <= 6 ? Blocks.WATER : Blocks.AIR);
        }
        DungeonMob swimmer = helper.spawnWithNoFreeWill(ModEntities.type("armored_trident_drowned"), new Vec3(4.5, 1.5, 5.5));
        swimmer.setPersistenceRequired();
        double initialY = swimmer.getY(), initialX = swimmer.getX();
        helper.runAtTickTime(4, () -> {
            helper.assertTrue(swimmer.isInWater() && swimmer.getNavigation() instanceof WaterBoundPathNavigation,
                    "Entering deep water did not select water navigation");
            Vec3 waypoint = helper.absoluteVec(new Vec3(4.5, 4, 5.5));
            helper.assertTrue(swimmer.getNavigation().moveTo(waypoint.x, waypoint.y, waypoint.z, 1), "Water navigator failed to produce a clear three-dimensional path");
        });
        helper.runAtTickTime(35, () -> {
            helper.assertTrue(swimmer.isInWater() && swimmer.getY() > initialY + .15,
                    "A vertical water path did not change depth: startY=" + initialY + ", actual=" + swimmer.position()
                            + ", delta=" + swimmer.getDeltaMovement() + ", target=" + swimmer.getTarget());
            Vec3 waypoint = helper.absoluteVec(new Vec3(14.5, 4, 5.5));
            helper.assertTrue(swimmer.getNavigation().moveTo(waypoint.x, waypoint.y, waypoint.z, 1), "Water navigator rejected the horizontal route");
        });
        helper.runAtTickTime(70, () -> {
            helper.assertTrue(swimmer.isSwimming() && swimmer.isInWater(), "Navigating swimmer did not publish the real swimming state");
            helper.assertTrue(swimmer.getX() > initialX + .2, "Horizontal water navigation did not move the entity: startX="
                    + initialX + ", actual=" + swimmer.position() + ", delta=" + swimmer.getDeltaMovement());
            swimmer.getNavigation().stop(); swimmer.setTarget(null); swimmer.setNoAi(true); swimmer.setNoGravity(true);
            for (int x = 3; x <= 6; x++) for (int z = 4; z <= 6; z++) helper.setBlock(new BlockPos(x, 7, z), Blocks.STONE);
            swimmer.moveTo(helper.absoluteVec(new Vec3(4.5, 8, 5.5))); swimmer.setDeltaMovement(Vec3.ZERO);
        });
        helper.runAtTickTime(75, () -> {
            helper.assertFalse(swimmer.isInWater() || swimmer.isSwimming(), "Leaving water retained a fake swimming pose");
            helper.assertTrue(swimmer.getNavigation() instanceof AmphibiousPathNavigation, "Leaving water failed to restore land navigation");
            helper.succeed();
        });
    }

    private static List<DungeonConstruct> owned(GameTestHelper helper, DungeonMob owner) {
        return helper.getLevel().getEntitiesOfClass(DungeonConstruct.class, owner.getBoundingBox().inflate(24), entity -> entity.owner() == owner);
    }
    private static void floor(GameTestHelper helper) {
        for (int x = 0; x < 20; x++) for (int z = 0; z < 12; z++) helper.setBlock(new BlockPos(x, 0, z), Blocks.STONE);
    }
}
