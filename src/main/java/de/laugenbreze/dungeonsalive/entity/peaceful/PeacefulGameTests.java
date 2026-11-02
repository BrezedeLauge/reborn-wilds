package de.laugenbreze.dungeonsalive.entity.peaceful;

import com.mojang.authlib.GameProfile;
import de.laugenbreze.dungeonsalive.DungeonsAlive;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;
import net.neoforged.neoforge.common.util.FakePlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/** Server-side regressions for peaceful behavior, actual trades and companion persistence. */
@GameTestHolder(DungeonsAlive.MOD_ID)
@PrefixGameTestTemplate(false)
public final class PeacefulGameTests {
    private PeacefulGameTests() {}

    @GameTest(template = "gametest_empty", templateNamespace = DungeonsAlive.MOD_ID,
            batch = "peaceful_entities", timeoutTicks = 70)
    public static void peacefulMobsDoNotRetaliateAndAquaticPetDoesNotDrown(GameTestHelper helper) {
        floor(helper);
        for (var profile : PeacefulProfiles.all()) {
            Mob registered = profile.kind() == PeacefulProfiles.Kind.MERCHANT
                    ? ModPeacefulEntities.merchantType(profile.id()).create(helper.getLevel())
                    : ModPeacefulEntities.creatureType(profile.id()).create(helper.getLevel());
            helper.assertTrue(registered != null, "Missing peaceful factory: " + profile.id());
            helper.assertFalse(registered instanceof Enemy, "Peaceful content is registered as an enemy: " + profile.id());
        }

        // A real server damage event records this survival player as the last
        // attacker. Any accidentally added retaliation goal would acquire them.
        var player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setPos(helper.absoluteVec(new Vec3(5.5, 1, 6.5)));
        List<Mob> peaceful = new ArrayList<>();
        int index = 0;
        for (String id : List.of("wooly_cow", "key_golem", "pet_redstone_ministrosity")) {
            var mob = ModPeacefulEntities.creatureType(id).create(helper.getLevel());
            mob.setPos(helper.absoluteVec(new Vec3(3.5 + index++ * 2, 1, 4.5)));
            mob.setPersistenceRequired();
            helper.assertTrue(helper.getLevel().addFreshEntity(mob), "Could not spawn peaceful actor " + id);
            helper.assertTrue(mob.hurt(helper.getLevel().damageSources().playerAttack(player), 1), "Player damage setup failed");
            peaceful.add(mob);
        }
        var merchant = ModPeacefulEntities.merchantType("blacksmith").create(helper.getLevel());
        merchant.setPos(helper.absoluteVec(new Vec3(9.5, 1, 4.5)));
        helper.assertTrue(helper.getLevel().addFreshEntity(merchant), "Could not spawn merchant");
        helper.assertTrue(merchant.hurt(helper.getLevel().damageSources().playerAttack(player), 1), "Merchant damage setup failed");
        peaceful.add(merchant);

        for (int x = 12; x <= 16; x++) for (int z = 3; z <= 7; z++) for (int y = 1; y <= 4; y++)
            helper.setBlock(new BlockPos(x, y, z), x == 12 || x == 16 || z == 3 || z == 7 || y == 4 ? Blocks.GLASS : Blocks.WATER);
        var squid = helper.spawnWithNoFreeWill(ModPeacefulEntities.creatureType("pet_baby_squid"), new Vec3(14.5, 2, 5.5));
        squid.setNoGravity(true); // Keep the breathing test submerged; navigation is a separate behavior.
        squid.setAirSupply(0);
        float startingHealth = squid.getHealth();
        helper.runAtTickTime(45, () -> {
            for (Mob mob : peaceful) {
                helper.assertTrue(mob.isAlive(), "Peaceful test actor disappeared");
                helper.assertTrue(mob.getTarget() == null, "Peaceful mob acquired a combat target after player damage");
            }
            helper.assertTrue(squid.isEyeInFluid(FluidTags.WATER), "Aquatic test actor was not actually submerged");
            helper.assertTrue(squid.isAlive() && squid.getHealth() == startingHealth, "Aquatic pet took drowning damage with zero initial air");
            helper.succeed();
        });
    }

    @GameTest(template = "gametest_empty", templateNamespace = DungeonsAlive.MOD_ID,
            batch = "peaceful_entities", timeoutTicks = 40)
    public static void merchantTradePaymentAndUseCountersSurviveNbt(GameTestHelper helper) {
        for (var profile : PeacefulProfiles.all()) if (profile.kind() == PeacefulProfiles.Kind.MERCHANT) {
            var merchant = ModPeacefulEntities.merchantType(profile.id()).create(helper.getLevel());
            helper.assertTrue(merchant != null && !merchant.getOffers().isEmpty(), "Merchant has no actual offers: " + profile.id());
            for (var offer : merchant.getOffers()) helper.assertFalse(offer.getResult().isEmpty(), "Merchant offers an empty result: " + profile.id());
        }
        var merchant = ModPeacefulEntities.merchantType("blacksmith").create(helper.getLevel());
        var offer = merchant.getOffers().getFirst();
        ItemStack displayedResult = offer.getResult().copy();
        ItemStack displayedCost = offer.getCostA().copy();
        ItemStack payment = displayedCost.copy();
        ItemStack second = offer.getCostB().copy();
        helper.assertTrue(offer.take(payment, second), "Real MerchantOffer rejected its displayed payment");
        helper.assertTrue(payment.isEmpty() && second.isEmpty(), "MerchantOffer failed to consume the displayed payment");
        helper.assertFalse(offer.take(payment, second), "Already-consumed payment was accepted twice");
        merchant.notifyTrade(offer);
        helper.assertTrue(offer.getUses() > 0, "Completing a real trade did not consume offer stock");

        CompoundTag saved = new CompoundTag();
        merchant.addAdditionalSaveData(saved);
        var restored = ModPeacefulEntities.merchantType("blacksmith").create(helper.getLevel());
        restored.readAdditionalSaveData(saved);
        helper.assertTrue(restored.getOffers().size() == merchant.getOffers().size(), "Reload replaced saved offers with defaults");
        var restoredOffer = restored.getOffers().getFirst();
        helper.assertTrue(restoredOffer.getUses() == offer.getUses(), "Reload reset the merchant's used stock");
        helper.assertTrue(ItemStack.isSameItemSameComponents(restoredOffer.getResult(), displayedResult)
                && restoredOffer.getResult().getCount() == displayedResult.getCount(), "Saved trade result changed");
        helper.assertTrue(ItemStack.isSameItemSameComponents(restoredOffer.getCostA(), displayedCost)
                && restoredOffer.getCostA().getCount() == displayedCost.getCount(), "Saved trade price changed");
        int originalUses = offer.getUses();
        restoredOffer.increaseUses();
        helper.assertTrue(offer.getUses() == originalUses, "Restored offer shares mutable state with the original merchant");
        helper.succeed();
    }

    @GameTest(template = "gametest_empty", templateNamespace = DungeonsAlive.MOD_ID,
            batch = "peaceful_entities", timeoutTicks = 40)
    public static void companionOwnerAndWaitingSurviveReloadAndRejectOtherPlayers(GameTestHelper helper) {
        var owner = helper.makeMockPlayer(GameType.SURVIVAL);
        var otherPlayer = helper.makeMockPlayer(GameType.SURVIVAL);
        var pet = ModPeacefulEntities.creatureType("pet_redstone_ministrosity").create(helper.getLevel());
        helper.assertTrue(pet.mobInteract(owner, InteractionHand.MAIN_HAND).consumesAction(), "First owner interaction was rejected");
        helper.assertTrue(owner.getUUID().equals(pet.ownerId()) && !pet.waiting(), "First interaction did not adopt the companion");
        pet.mobInteract(owner, InteractionHand.MAIN_HAND);
        helper.assertTrue(pet.waiting(), "Owner could not tell the companion to wait");

        CompoundTag saved = new CompoundTag();
        pet.addAdditionalSaveData(saved);
        var restored = ModPeacefulEntities.creatureType("pet_redstone_ministrosity").create(helper.getLevel());
        restored.readAdditionalSaveData(saved);
        helper.assertTrue(owner.getUUID().equals(restored.ownerId()) && restored.waiting(), "Owner/waiting state was lost after NBT reload");
        restored.mobInteract(otherPlayer, InteractionHand.MAIN_HAND);
        helper.assertTrue(owner.getUUID().equals(restored.ownerId()) && restored.waiting(), "Another player changed a claimed companion's owner or waiting state");
        restored.mobInteract(owner, InteractionHand.MAIN_HAND);
        helper.assertTrue(owner.getUUID().equals(restored.ownerId()) && !restored.waiting(), "Saved owner could not resume following after reload");
        helper.succeed();
    }

    @GameTest(template = "gametest_empty", templateNamespace = DungeonsAlive.MOD_ID,
            batch = "companion_navigation", skyAccess = true, timeoutTicks = 90)
    public static void aquaticCompanionNavigatesUpwardWithoutForcedMotion(GameTestHelper helper) {
        floor(helper);
        for (int y = 1; y <= 7; y++) for (int x = 0; x < 20; x++) for (int z = 0; z < 12; z++)
            helper.setBlock(new BlockPos(x, y, z), x == 0 || x == 19 || z == 0 || z == 11 ? Blocks.STONE : y <= 6 ? Blocks.WATER : Blocks.AIR);
        var squid = helper.spawnWithNoFreeWill(ModPeacefulEntities.creatureType("pet_baby_squid"), new Vec3(5.5, 1.5, 5.5));
        double initialY = squid.getY();
        helper.runAtTickTime(4, () -> {
            helper.assertTrue(squid.isInWater() && !squid.isNoAi() && squid.getNavigation() instanceof WaterBoundPathNavigation,
                    "Aquatic companion did not enter real, AI-controlled water navigation");
            Vec3 waypoint = helper.absoluteVec(new Vec3(5.5, 4.5, 5.5));
            helper.assertTrue(squid.getNavigation().moveTo(waypoint.x, waypoint.y, waypoint.z, 1),
                    "Aquatic companion could not create an unobstructed upward path");
        });
        helper.runAtTickTime(65, () -> {
            helper.assertTrue(squid.isInWater() && squid.getY() > initialY + .4,
                    "Companion navigation failed to gain actual depth without forced motion: startY=" + initialY
                            + ", actual=" + squid.position() + ", delta=" + squid.getDeltaMovement()
                            + ", pathDone=" + squid.getNavigation().isDone());
            helper.assertTrue(squid.getTarget() == null, "Peaceful water navigation introduced a combat target");
            helper.succeed();
        });
    }

    @GameTest(template = "gametest_empty", templateNamespace = DungeonsAlive.MOD_ID,
            batch = "companion_navigation", timeoutTicks = 80)
    public static void rescuedBlacksmithFollowsWithOddEntityId(GameTestHelper helper) {
        floor(helper);
        // A real level-registered server player is required: the follow goal resolves
        // its keeper by UUID. FakePlayer's no-op connection avoids a network fixture.
        var rescuer = new FakePlayer(helper.getLevel(), new GameProfile(UUID.randomUUID(), "rescue-follow-test"));
        rescuer.setGameMode(GameType.CREATIVE);
        rescuer.setPos(helper.absoluteVec(new Vec3(5.5, 1, 5.5)));
        helper.getLevel().addNewPlayer(rescuer);
        DungeonsCreature candidate = ModPeacefulEntities.creatureType("captured_blacksmith").create(helper.getLevel());
        if ((candidate.getId() & 1) == 0) candidate = ModPeacefulEntities.creatureType("captured_blacksmith").create(helper.getLevel());
        DungeonsCreature civilian = candidate;
        civilian.setPos(helper.absoluteVec(new Vec3(3.5, 1, 5.5)));
        helper.assertTrue((civilian.getId() & 1) == 1 && helper.getLevel().addFreshEntity(civilian),
                "Follow regression did not create the previously starved odd-ID civilian");
        helper.assertTrue(helper.getLevel().getPlayerByUUID(rescuer.getUUID()) == rescuer,
                "Follow regression's keeper was not registered in its server level");
        double initialX = civilian.getX();
        helper.runAtTickTime(2, () -> {
            helper.assertTrue(civilian.mobInteract(rescuer, InteractionHand.MAIN_HAND).consumesAction(), "Rescuer could not free the captive");
            helper.assertTrue(civilian.rescued() && !civilian.waiting() && !civilian.isNoAi(),
                    "Rescue failed to permit actual following AI");
            rescuer.setPos(helper.absoluteVec(new Vec3(13.5, 1, 5.5)));
        });
        helper.runAtTickTime(15, () -> helper.assertFalse(civilian.getNavigation().isDone(),
                "Odd-ID follow goal never issued its navigation request"));
        helper.runAtTickTime(55, () -> {
            // Remove the test player before assertions so a failure cannot leave a
            // registered keeper behind in the subsequent server-test batches.
            double distanceToKeeper = civilian.distanceToSqr(rescuer);
            helper.getLevel().removePlayerImmediately(rescuer, Entity.RemovalReason.DISCARDED);
            helper.assertTrue(civilian.getX() > initialX + 1 && distanceToKeeper < 81,
                    "Freed odd-ID blacksmith did not actually approach its rescuer: " + civilian.position());
            helper.assertTrue(civilian.rescued() && !civilian.waiting() && civilian.getTarget() == null,
                    "Following changed the rescued civilian's peaceful state");
            helper.succeed();
        });
    }

    private static void floor(GameTestHelper helper) {
        for (int x = 0; x < 20; x++) for (int z = 0; z < 12; z++) helper.setBlock(new BlockPos(x, 0, z), Blocks.STONE);
    }
}
