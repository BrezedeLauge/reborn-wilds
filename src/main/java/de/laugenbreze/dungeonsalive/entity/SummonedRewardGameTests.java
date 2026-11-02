package de.laugenbreze.dungeonsalive.entity;

import com.mojang.authlib.GameProfile;
import de.laugenbreze.dungeonsalive.DungeonsAlive;
import de.laugenbreze.dungeonsalive.item.gear.ArtifactKind;
import de.laugenbreze.dungeonsalive.item.gear.GearContent;
import de.laugenbreze.dungeonsalive.item.gear.SoulEnergy;
import de.laugenbreze.dungeonsalive.item.gear.WeaponKind;
import de.laugenbreze.dungeonsalive.quest.QuestService;
import de.laugenbreze.dungeonsalive.quest.core.QuestSnapshot;
import de.laugenbreze.dungeonsalive.quest.core.QuestStatus;
import de.laugenbreze.dungeonsalive.quest.data.QuestSavedData;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.util.FakePlayer;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

/** Reward regressions use the real death event path, including persisted summon identity. */
@GameTestHolder(DungeonsAlive.MOD_ID)
@PrefixGameTestTemplate(false)
public final class SummonedRewardGameTests {
    private SummonedRewardGameTests() {}

    @GameTest(template = "gametest_empty", templateNamespace = DungeonsAlive.MOD_ID,
            batch = "summoned_rewards", timeoutTicks = 100)
    public static void actualSummonsDoNotYieldSoulsAfterReloadButHeartstealerStillHeals(GameTestHelper helper) {
        floor(helper);
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setPos(helper.absoluteVec(new Vec3(3.5, 1, 5.5)));
        player.setItemInHand(InteractionHand.OFF_HAND, new ItemStack(GearContent.artifact(ArtifactKind.HARVESTER)));
        player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(GearContent.weapon(WeaponKind.HEARTSTEALER)));
        SoulEnergy.set(player, 0);
        helper.assertTrue(SoulEnergy.carriesSoulArtifact(player), "Fixture must carry a real soul artifact");
        DungeonMob caster = helper.spawnWithNoFreeWill(ModEntities.type("necromancer"), new Vec3(8.5, 1, 5.5));
        var target = helper.spawnWithNoFreeWill(EntityType.IRON_GOLEM, new Vec3(15.5, 1, 5.5));
        helper.runAtTickTime(35, () -> {
            caster.getRandom().setSeed(0L);
            helper.assertTrue(caster.beginAttack(AttackKind.SUMMON, target),
                    "Necromancer must start its real SUMMON action");
        });
        helper.runAtTickTime(78, () -> {
            var children = helper.getLevel().getEntitiesOfClass(DungeonMob.class, caster.getBoundingBox().inflate(6),
                    mob -> mob.isAlive() && mob.isSummoned() && mob.profile().id().equals("vanguard"));
            helper.assertTrue(children.size() >= 2, "Actual SUMMON must supply two separate helper fixtures");
            children.forEach(child -> { child.setNoAi(true); child.setTarget(null); });

            DungeonMob direct = children.get(0);
            player.setHealth(10);
            float expectedHealth = 10 + Math.min(6, direct.getMaxHealth() * .09F);
            kill(helper, direct, player);
            helper.assertTrue(SoulEnergy.get(player) == 0, "A directly summoned helper must not supply souls");
            helper.assertTrue(Math.abs(player.getHealth() - expectedHealth) < .001F,
                    "Heartstealer must retain its normal melee-kill healing against a summoned opponent");

            DungeonMob original = children.get(1);
            CompoundTag saved = new CompoundTag();
            original.addAdditionalSaveData(saved);
            helper.assertTrue(saved.hasUUID("DungeonsSummoner")
                            && saved.getUUID("DungeonsSummoner").equals(caster.getUUID()),
                    "Actual SUMMON must record the caster, rather than a test-created reward flag");
            DungeonMob restored = ModEntities.type("vanguard").create(helper.getLevel());
            helper.assertTrue(restored != null, "Vanguard factory must create the reload fixture");
            restored.setPos(original.position());
            restored.readAdditionalSaveData(saved);
            restored.setNoAi(true);
            original.discard();
            helper.assertTrue(restored.isSummoned() && helper.getLevel().addFreshEntity(restored),
                    "Summoner identity must survive the helper's NBT roundtrip");
            kill(helper, restored, player);
            helper.assertTrue(SoulEnergy.get(player) == 0, "Reloading a summoned helper must not make it a soul source");

            DungeonMob ordinary = helper.spawnWithNoFreeWill(ModEntities.type("vanguard"), new Vec3(5.5, 1, 9.5));
            helper.assertFalse(ordinary.isSummoned(), "Control must be an ordinary mob of the same registered type");
            kill(helper, ordinary, player);
            helper.assertTrue(SoulEnergy.get(player) == 3, "An ordinary Vanguard kill must still supply exactly three souls");
            helper.succeed();
        });
    }

    @GameTest(template = "gametest_empty", templateNamespace = DungeonsAlive.MOD_ID,
            batch = "summoned_rewards", timeoutTicks = 40)
    public static void reloadedSummonCannotCompleteBountyButOrdinaryBossCan(GameTestHelper helper) {
        floor(helper);
        // The death adapter needs a ServerPlayer. No later owner lookup is needed,
        // so this FakePlayer stays out of the level's player list and uses its no-op connection.
        FakePlayer player = new FakePlayer(helper.getLevel(), new GameProfile(UUID.randomUUID(), "summon-quest-qa"));
        player.setGameMode(GameType.SURVIVAL);
        player.setPos(helper.absoluteVec(new Vec3(3.5, 1, 5.5)));
        String prerequisite = "dungeonsalive:a_place_to_return";
        String bounty = "dungeonsalive:encounters/nameless_one";
        QuestSavedData data = QuestSavedData.get(helper.getLevel().getServer());
        QuestSnapshot previous = data.read(player.getUUID());
        try {
            helper.assertTrue(QuestService.repository().snapshot().documents().containsKey(bounty),
                    "Server must load the actual shipped Nameless bounty");
            data.write(player.getUUID(), new QuestSnapshot(Set.of(prerequisite), Set.of(prerequisite), Map.of()));
            QuestService.disconnect(player);
            helper.assertTrue(QuestService.status(player, bounty) == QuestStatus.AVAILABLE,
                    "Claimed prerequisite must unlock the existing bounty");
            helper.assertTrue(QuestService.accept(player, bounty) == 1 && QuestService.status(player, bounty) == QuestStatus.ACTIVE,
                    "Bounty must be explicitly accepted before any death event");

            DungeonMob ordinary = helper.spawnWithNoFreeWill(ModEntities.type("nameless_one"), new Vec3(13.5, 1, 5.5));
            LivingDeathEvent cancelled = new LivingDeathEvent(ordinary, player.damageSources().playerAttack(player));
            cancelled.setCanceled(true);
            NeoForge.EVENT_BUS.post(cancelled);
            assertNoBountyProgress(helper, data, player, bounty, "A cancelled death must not advance the bounty");

            DungeonMob caster = helper.spawnWithNoFreeWill(ModEntities.type("necromancer"), new Vec3(8.5, 1, 5.5));
            DungeonMob marked = ModEntities.type("nameless_one").create(helper.getLevel());
            helper.assertTrue(marked != null, "Nameless factory must create the marked fixture");
            // Deliberately authored NBT fixture: this does not claim the current Necromancer
            // can summon bosses. It tests the same persistent marker against a real kill quest.
            CompoundTag marker = new CompoundTag();
            marked.addAdditionalSaveData(marker);
            marker.putUUID("DungeonsSummoner", caster.getUUID());
            marker.putLong("DungeonsSummonExpiresAt", helper.getLevel().getGameTime() + 1200);
            marked.readAdditionalSaveData(marker);
            CompoundTag saved = new CompoundTag();
            marked.addAdditionalSaveData(saved);
            DungeonMob restored = ModEntities.type("nameless_one").create(helper.getLevel());
            helper.assertTrue(restored != null, "Nameless factory must create the restored fixture");
            restored.readAdditionalSaveData(saved);
            restored.setNoAi(true);
            restored.setPos(helper.absoluteVec(new Vec3(5.5, 1, 9.5)));
            helper.assertTrue(restored.isSummoned() && helper.getLevel().addFreshEntity(restored),
                    "Marked boss fixture must retain summon identity after NBT reload");
            kill(helper, restored, player);
            assertNoBountyProgress(helper, data, player, bounty, "A reloaded summon must not count as the bounty boss");

            helper.assertFalse(ordinary.isSummoned(), "Control boss must not carry summon identity");
            kill(helper, ordinary, player);
            helper.assertTrue(QuestService.status(player, bounty) == QuestStatus.READY,
                    "An ordinary boss of the same registry ID must complete the accepted bounty");
            helper.assertTrue(data.read(player.getUUID()).objectiveProgress().getOrDefault(bounty, Map.of())
                            .getOrDefault("defeat_v1", 0) == 1,
                    "Exactly one eligible boss kill must be persisted");
            helper.assertFalse(data.read(player.getUUID()).claimedQuests().contains(bounty),
                    "Completing the objective must not automatically claim its reward");
        } finally {
            QuestService.disconnect(player);
            data.write(player.getUUID(), previous);
        }
        helper.succeed();
    }

    private static void assertNoBountyProgress(GameTestHelper helper, QuestSavedData data,
                                               FakePlayer player, String bounty, String message) {
        helper.assertTrue(QuestService.status(player, bounty) == QuestStatus.ACTIVE
                        && data.read(player.getUUID()).objectiveProgress().getOrDefault(bounty, Map.of())
                                .getOrDefault("defeat_v1", 0) == 0, message);
    }

    private static void kill(GameTestHelper helper, DungeonMob victim, Player player) {
        helper.assertTrue(victim.hurt(player.damageSources().playerAttack(player), 1000) && !victim.isAlive(),
                "Fixture must die through real player-attributed damage");
    }

    private static void floor(GameTestHelper helper) {
        for (int x = 0; x < 20; x++) for (int z = 0; z < 12; z++)
            helper.setBlock(new BlockPos(x, 0, z), Blocks.STONE);
    }
}
