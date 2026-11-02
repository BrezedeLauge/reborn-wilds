package de.laugenbreze.dungeonsalive.village;

import de.laugenbreze.dungeonsalive.DungeonsAlive;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.tags.PoiTypeTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.behavior.AssignProfessionFromJobSite;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.village.poi.PoiTypes;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

@GameTestHolder(DungeonsAlive.MOD_ID)
@PrefixGameTestTemplate(false)
public final class VillageGameTests {
    private VillageGameTests() {}

    @GameTest(batch = "villagers", template = "gametest_empty", templateNamespace = DungeonsAlive.MOD_ID, timeoutTicks = 40)
    public static void allMerchantJobsAndTradesAreRealRegistrations(GameTestHelper helper) {
        for (var role : VillageContent.ROLES) {
            helper.assertTrue(BuiltInRegistries.VILLAGER_PROFESSION.get(role.profession().getId()) == role.profession().get(), "Missing profession " + role.id());
            for (var state : role.workstation().get().getStateDefinition().getPossibleStates()) {
                var poi = PoiTypes.forState(state).orElseThrow();
                helper.assertTrue(poi.is(role.poi().getKey()), "Wrong workstation POI " + role.id());
                helper.assertTrue(poi.is(PoiTypeTags.ACQUIRABLE_JOB_SITE), "Job is not acquirable " + role.id());
                helper.assertTrue(role.profession().get().heldJobSite().test(poi), "Profession rejects own workstation");
            }
            var offersByLevel = VillagerTrades.TRADES.get(role.profession().get());
            helper.assertTrue(offersByLevel != null && offersByLevel.size() == 5, "Missing career levels " + role.id());
            for (int careerLevel = 1; careerLevel <= 5; careerLevel++) {
                var villager = EntityType.VILLAGER.create(helper.getLevel());
                villager.setVillagerData(villager.getVillagerData().setProfession(role.profession().get()).setLevel(careerLevel));
                var offers = villager.getOffers();
                helper.assertTrue(offers.size() >= 2, "Career level has fewer than two trades " + role.id());
                for (var offer : offers) {
                    helper.assertFalse(offer.getResult().isEmpty(), "Empty merchant result " + role.id());
                    ItemStack payment = offer.getCostA().copy();
                    ItemStack second = offer.getCostB().copy();
                    helper.assertTrue(offer.take(payment, second), "Displayed merchant payment was rejected");
                    helper.assertTrue(payment.isEmpty() && second.isEmpty(), "Trade did not consume its displayed cost");
                    helper.assertFalse(offer.take(payment, second), "Trade can reuse the already consumed payment");
                }
            }
        }
        helper.succeed();
    }

    @GameTest(batch = "villagers", template = "gametest_empty", templateNamespace = DungeonsAlive.MOD_ID, timeoutTicks = 60)
    public static void vanillaProfessionAssignmentAcceptsMerchantWorkstation(GameTestHelper helper) {
        prepareFloor(helper);
        var role = VillageContent.ENCHANTSMITH;
        BlockPos pos = new BlockPos(5, 1, 5);
        helper.setBlock(pos, role.workstation().get());
        Villager villager = helper.spawnWithNoFreeWill(EntityType.VILLAGER, new Vec3(5.5, 1, 4.4));
        villager.setVillagerData(villager.getVillagerData().setProfession(VillagerProfession.NONE));
        helper.runAfterDelay(3, () -> {
            var actualPos = helper.absolutePos(pos);
            helper.assertTrue(helper.getLevel().getPoiManager().getType(actualPos).isPresent(), "Placed workstation has no world POI");
            villager.getBrain().setMemory(MemoryModuleType.POTENTIAL_JOB_SITE, GlobalPos.of(helper.getLevel().dimension(), actualPos));
            boolean started = AssignProfessionFromJobSite.create().tryStart(helper.getLevel(), villager, helper.getLevel().getGameTime());
            helper.assertTrue(started, "Vanilla assignment behavior did not run");
            helper.assertTrue(villager.getVillagerData().getProfession() == role.profession().get(), "Vanilla AI did not assign Enchantsmith");
            helper.assertTrue(villager.getBrain().getMemory(MemoryModuleType.JOB_SITE).orElseThrow().pos().equals(actualPos), "Job site memory was not adopted");
            helper.succeed();
        });
    }

    @GameTest(batch = "villagers", template = "gametest_empty", templateNamespace = DungeonsAlive.MOD_ID, timeoutTicks = 60)
    public static void workAnimationRequiresActualWorkMemoryAndWorkstation(GameTestHelper helper) {
        prepareFloor(helper);
        var role = VillageContent.BLACKSMITH;
        BlockPos pos = new BlockPos(5, 1, 5);
        helper.setBlock(pos, role.workstation().get());
        Villager villager = helper.spawnWithNoFreeWill(EntityType.VILLAGER, new Vec3(5.5, 1, 4.4));
        helper.runAfterDelay(3, () -> {
            villager.setVillagerData(villager.getVillagerData().setProfession(role.profession().get()));
            villager.setNoAi(false);
            villager.getBrain().setMemory(MemoryModuleType.JOB_SITE, GlobalPos.of(helper.getLevel().dimension(), helper.absolutePos(pos)));
            villager.refreshBrain(helper.getLevel());
            villager.getBrain().setActiveActivityIfPossible(Activity.WORK);
            villager.setDeltaMovement(Vec3.ZERO);
            helper.assertTrue(VillageAnimationEvents.observe(villager).mode() == VillageActivity.IDLE, "Standing at a station must not invent work");
            villager.getBrain().setMemory(MemoryModuleType.LAST_WORKED_AT_POI, helper.getLevel().getGameTime());
            helper.assertTrue(VillageAnimationEvents.observe(villager).mode() == VillageActivity.WORK, "Actual recent work did not trigger animation");
            villager.setPos(villager.getX() + 5, villager.getY(), villager.getZ());
            helper.assertTrue(VillageAnimationEvents.observe(villager).mode() == VillageActivity.IDLE, "Work animation continued far from the job site");
            helper.succeed();
        });
    }

    private static void prepareFloor(GameTestHelper helper) {
        for (int x = 0; x < 20; x++) for (int z = 0; z < 12; z++) helper.setBlock(new BlockPos(x, 0, z), Blocks.STONE);
    }
}
