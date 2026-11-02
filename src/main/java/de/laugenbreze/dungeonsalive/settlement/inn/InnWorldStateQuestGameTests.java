package de.laugenbreze.dungeonsalive.settlement.inn;

import de.laugenbreze.dungeonsalive.DungeonsAlive;
import de.laugenbreze.dungeonsalive.quest.QuestService;
import de.laugenbreze.dungeonsalive.quest.core.QuestStatus;
import de.laugenbreze.dungeonsalive.quest.data.QuestSavedData;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Set;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.AfterBatch;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

/** Real inn -> WORLD_STATE -> explicit reward path; no fake world-condition provider. */
@GameTestHolder(DungeonsAlive.MOD_ID)
@PrefixGameTestTemplate(false)
public final class InnWorldStateQuestGameTests {
    private static final String BATCH = "inn_world_state", QUEST = "dungeonsalive:a_welcoming_inn";
    private static final ResourceLocation CONNECTED = ResourceLocation.fromNamespaceAndPath(DungeonsAlive.MOD_ID, "inn_connected");
    private static final Map<ServerLevel, Set<ServerPlayer>> PLAYERS = new IdentityHashMap<>();
    private InnWorldStateQuestGameTests() { }
    @AfterBatch(batch = BATCH) public static void cleanup(ServerLevel level) {
        var players = PLAYERS.remove(level); if (players != null) players.forEach(QuestService::disconnect);
        InnIntegrationGameTests.cleanup(level);
    }
    @GameTest(template = "gametest_empty", templateNamespace = DungeonsAlive.MOD_ID, batch = BATCH, timeoutTicks = 1_800)
    public static void actualInnBridgeAndGuestRevalidateAtClaimAndPayExactlyOnceAfterReload(GameTestHelper helper) {
        var fixture = InnIntegrationGameTests.fixture(helper, false);
        ServerPlayer owner = fixture.owner();
        PLAYERS.computeIfAbsent(helper.getLevel(), ignored -> new HashSet<>()).add(owner);
        helper.assertTrue(QuestService.repository().snapshot().documents().containsKey(QUEST), "Optional real inn quest was not loaded");
        QuestService.tick(owner);
        helper.assertTrue(QuestService.status(owner, QUEST) == QuestStatus.AVAILABLE, "Existing construction accepted the quest automatically");
        int lanterns = count(owner, Items.LANTERN), emeralds = count(owner, Items.EMERALD), experience = owner.totalExperience;
        helper.assertTrue(QuestService.accept(owner, QUEST) == 1, "Explicit quest acceptance failed");
        boolean[] bridgeTested = {false}, done = {false};
        helper.onEachTick(() -> {
            if (done[0]) return;
            // This calls the real authoritative sampler; the native visitor,
            // route and furniture systems produce every world fact themselves.
            QuestService.tick(owner);
            if (QuestService.status(owner, QUEST) != QuestStatus.READY) return;
            helper.assertTrue(InnSavedData.get(helper.getLevel().getServer()).hadArrival(owner.getUUID()),
                    "Quest became ready without a real saved guest arrival");
            if (!bridgeTested[0]) {
                helper.getLevel().setBlockAndUpdate(helper.absolutePos(new BlockPos(9, 1, 5)), Blocks.WATER.defaultBlockState());
                helper.assertFalse(InnService.worldState(owner, CONNECTED), "Removed actual bridge still reported a live connection");
                helper.assertTrue(QuestService.claim(owner, QUEST) == 0, "Claim trusted the old ready UI after the bridge was removed");
                helper.assertTrue(QuestService.status(owner, QUEST) == QuestStatus.ACTIVE, "Broken connection did not revoke unclaimed readiness");
                helper.assertTrue(count(owner, Items.LANTERN) == lanterns && count(owner, Items.EMERALD) == emeralds
                        && owner.totalExperience == experience, "Rejected claim paid a partial reward");
                helper.getLevel().setBlockAndUpdate(helper.absolutePos(new BlockPos(9, 1, 5)), Blocks.OAK_PLANKS.defaultBlockState());
                bridgeTested[0] = true;
                return;
            }
            helper.assertTrue(QuestService.claim(owner, QUEST) == 1, "Rebuilt genuine connection did not permit the explicit claim");
            helper.assertTrue(count(owner, Items.LANTERN) == lanterns + 4 && count(owner, Items.EMERALD) == emeralds + 6
                    && owner.totalExperience == experience + 40, "Actual reward items/experience did not match the declared quest");
            var saved = QuestSavedData.get(helper.getLevel().getServer());
            CompoundTag encoded = saved.save(new CompoundTag(), helper.getLevel().registryAccess());
            var restored = QuestSavedData.load(encoded, helper.getLevel().registryAccess());
            helper.assertTrue(restored.read(owner.getUUID()).claimedQuests().contains(QUEST), "Real quest NBT lost its persisted claim guard");
            QuestService.disconnect(owner); // Rebuild the actual adapter cache from its SavedData, not an injected ready journal.
            helper.assertTrue(QuestService.status(owner, QUEST) == QuestStatus.CLAIMED && QuestService.claim(owner, QUEST) == 0,
                    "Recreated server journal paid a previously claimed reward");
            helper.assertTrue(count(owner, Items.LANTERN) == lanterns + 4 && count(owner, Items.EMERALD) == emeralds + 6
                    && owner.totalExperience == experience + 40, "Replay changed real rewards");
            done[0] = true; helper.succeed();
        });
    }
    private static int count(ServerPlayer player, Item item) {
        int total = 0;
        for (int slot = 0; slot < player.getInventory().getContainerSize(); slot++) {
            var stack = player.getInventory().getItem(slot); if (stack.is(item)) total += stack.getCount();
        }
        return total;
    }
}
