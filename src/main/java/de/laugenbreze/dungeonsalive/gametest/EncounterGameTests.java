package de.laugenbreze.dungeonsalive.gametest;

import de.laugenbreze.dungeonsalive.DungeonsAlive;
import de.laugenbreze.dungeonsalive.encounter.EncounterContent;
import de.laugenbreze.dungeonsalive.entity.DungeonMob;
import de.laugenbreze.dungeonsalive.entity.MobProfile;
import de.laugenbreze.dungeonsalive.entity.MobProfiles;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

@GameTestHolder(DungeonsAlive.MOD_ID)
@PrefixGameTestTemplate(false)
public final class EncounterGameTests {
    private EncounterGameTests() {}

    @GameTest(template = "gametest_empty", templateNamespace = DungeonsAlive.MOD_ID, batch = "encounters", timeoutTicks = 50)
    public static void sealsCheckSpaceConsumeOnceAndPreventCrowdedEncounters(GameTestHelper helper) {
        long expected = MobProfiles.all().stream().filter(profile -> profile.tier() != MobProfile.Tier.REGULAR).count();
        helper.assertTrue(EncounterContent.SEALS.size() == expected, "Every boss/miniboss needs a matching seal");
        for (int x = 0; x < 20; x++) for (int z = 0; z < 12; z++) helper.setBlock(new BlockPos(x, 0, z), Blocks.STONE);
        var player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setPos(helper.absoluteVec(new Vec3(2, 1, 6)));
        var seal = EncounterContent.SEALS.get("redstone_golem").get();
        var stack = new ItemStack(seal, 2);
        player.setItemInHand(InteractionHand.MAIN_HAND, stack);
        BlockPos floor = helper.absolutePos(new BlockPos(10, 0, 6));
        var context = new UseOnContext(player, InteractionHand.MAIN_HAND,
                new BlockHitResult(Vec3.atCenterOf(floor).add(0, .5, 0), Direction.UP, floor, false));
        helper.assertTrue(seal.useOn(context) == InteractionResult.FAIL, "An unintentional click must not start an encounter");
        helper.assertTrue(stack.getCount() == 2, "Rejected click consumed a seal");
        player.setShiftKeyDown(true);
        helper.setBlock(new BlockPos(10, 1, 6), Blocks.STONE);
        helper.assertTrue(seal.useOn(context) == InteractionResult.FAIL, "A blocked site allowed a spawn");
        helper.assertTrue(stack.getCount() == 2, "Blocked site consumed a seal");
        helper.setBlock(new BlockPos(10, 1, 6), Blocks.AIR);
        var result = seal.useOn(context);
        helper.assertTrue(result.consumesAction(), "A clear level site did not start an encounter: " + result);
        helper.assertTrue(stack.getCount() == 1, "A successful encounter did not consume exactly one seal");
        var mobs = helper.getLevel().getEntitiesOfClass(DungeonMob.class, player.getBoundingBox().inflate(16));
        helper.assertTrue(mobs.stream().filter(mob -> mob.profile().id().equals("redstone_golem")).count() == 1,
                "Expected exactly one Redstone Golem");
        helper.assertTrue(mobs.stream().allMatch(DungeonMob::isPersistenceRequired), "Encounter should persist when walking away");
        for (var registered : EncounterContent.SEALS.values()) helper.assertTrue(player.getCooldowns().isOnCooldown(registered.get()),
                "An alternate seal bypasses the shared cooldown");
        helper.assertTrue(seal.useOn(context) == InteractionResult.FAIL && stack.getCount() == 1,
                "Rapid replay duplicated an encounter or consumed another seal");
        player.getCooldowns().removeCooldown(seal);
        // Far enough not to overlap collision, near enough to exercise the encounter spacing rule.
        BlockPos secondFloor = helper.absolutePos(new BlockPos(15, 0, 6));
        var nearby = new UseOnContext(player, InteractionHand.MAIN_HAND,
                new BlockHitResult(Vec3.atCenterOf(secondFloor).add(0, .5, 0), Direction.UP, secondFloor, false));
        helper.assertTrue(seal.useOn(nearby) == InteractionResult.FAIL && stack.getCount() == 1,
                "A second nearby encounter must wait for the first to finish");
        mobs.forEach(DungeonMob::discard);
        helper.succeed();
    }
}
