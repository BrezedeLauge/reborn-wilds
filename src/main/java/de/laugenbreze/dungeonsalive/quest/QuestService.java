package de.laugenbreze.dungeonsalive.quest;

import com.google.gson.Gson;
import de.laugenbreze.dungeonsalive.DungeonsAlive;
import de.laugenbreze.dungeonsalive.item.ModItems;
import de.laugenbreze.dungeonsalive.network.JournalPayload;
import de.laugenbreze.dungeonsalive.network.JournalView;
import de.laugenbreze.dungeonsalive.quest.core.QuestJournal;
import de.laugenbreze.dungeonsalive.quest.core.QuestStatus;
import de.laugenbreze.dungeonsalive.quest.core.ObjectiveType;
import de.laugenbreze.dungeonsalive.quest.data.QuestDocument;
import de.laugenbreze.dungeonsalive.quest.data.QuestRepository;
import de.laugenbreze.dungeonsalive.quest.data.QuestSavedData;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/** Authoritative server-thread adapter. Clients may request actions, never report progress. */
public final class QuestService {
    private static final Gson GSON = new Gson();
    private static QuestRepository repository = new QuestRepository();
    private static final Map<UUID, CachedJournal> JOURNALS = new HashMap<>();
    private static final Set<UUID> WARNED = new HashSet<>();

    private QuestService() {}

    public static QuestRepository repository() { return repository; }

    public static void resetSession() {
        JOURNALS.clear();
        WARNED.clear();
        repository = new QuestRepository();
    }

    public static void disconnect(ServerPlayer player) {
        JOURNALS.remove(player.getUUID());
        WARNED.remove(player.getUUID());
    }

    public static void login(ServerPlayer player) {
        if (!healthy(player)) return;
        var data = QuestSavedData.get(player.server);
        if (!data.hasReceivedJournal(player.getUUID())) {
            // Mark first on the server thread; joining again cannot issue another journal.
            data.markReceivedJournal(player.getUUID());
            give(player, new ItemStack(ModItems.QUEST_JOURNAL.get()));
            player.sendSystemMessage(Component.translatable("message.dungeonsalive.welcome")
                    .withStyle(ChatFormatting.GOLD));
        }
        sample(player);
        send(player, false);
    }

    public static void tick(ServerPlayer player) {
        if (player.isSpectator() || !healthy(player)) return;
        var cached = cached(player);
        boolean reloaded = cached.needsSync;
        boolean changed = sample(player);
        if (changed || reloaded) send(player, false);
    }

    public static void killed(ServerPlayer player, ResourceLocation entityId) {
        if (player.isSpectator() || !healthy(player)) return;
        QuestJournal journal = cached(player).journal;
        if (journal.recordKill(entityId.toString())) {
            persist(player);
            send(player, false);
        }
    }

    public static void open(ServerPlayer player) {
        if (!healthy(player)) return;
        sample(player);
        send(player, true);
    }

    public static int accept(ServerPlayer player, String questId) {
        if (!healthy(player) || !known(questId, player)) return 0;
        var document = repository.snapshot().documents().get(questId);
        var biomes = player.registryAccess().registryOrThrow(net.minecraft.core.registries.Registries.BIOME);
        for (var objective : document.definition().objectives()) {
            if (objective.type() == ObjectiveType.VISIT_BIOME
                    && !biomes.containsKey(ResourceLocation.parse(objective.target()))) {
                player.sendSystemMessage(Component.translatable("message.dungeonsalive.missing_biome", objective.target()));
                return 0;
            }
        }
        if (!cached(player).journal.accept(questId)) {
            player.sendSystemMessage(Component.translatable("message.dungeonsalive.cannot_accept"));
            send(player, false);
            return 0;
        }
        sample(player);
        persist(player);
        player.sendSystemMessage(Component.translatable("message.dungeonsalive.accepted",
                repository.snapshot().documents().get(questId).title()).withStyle(ChatFormatting.GREEN));
        send(player, false);
        return 1;
    }

    public static int claim(ServerPlayer player, String questId) {
        if (!healthy(player) || !known(questId, player)) return 0;
        // Inventory and world-state objectives are re-evaluated immediately, never trusted from the last UI update.
        sample(player);
        var cached = cached(player);
        QuestDocument document = repository.snapshot().documents().get(questId);
        List<ItemStack> rewards = new ArrayList<>();
        for (QuestDocument.Reward reward : document.rewards()) {
            ResourceLocation id = ResourceLocation.parse(reward.itemId());
            if (!BuiltInRegistries.ITEM.containsKey(id)) {
                player.sendSystemMessage(Component.translatable("message.dungeonsalive.missing_reward", id.toString()));
                return 0;
            }
            rewards.add(new ItemStack(BuiltInRegistries.ITEM.get(id), reward.count()));
        }
        if (!cached.journal.claim(questId)) {
            player.sendSystemMessage(Component.translatable("message.dungeonsalive.cannot_claim"));
            send(player, false);
            return 0;
        }
        // Claims and rewards execute on the same server thread. No replay can pay twice.
        // Normal world saving persists both; a process crash is not a transactional database.
        persist(player);
        for (ItemStack reward : rewards) give(player, reward);
        player.giveExperiencePoints(document.experience());
        player.sendSystemMessage(Component.translatable("message.dungeonsalive.claimed", document.title())
                .withStyle(ChatFormatting.GOLD));
        send(player, false);
        return 1;
    }

    public static QuestStatus status(ServerPlayer player, String questId) {
        return cached(player).journal.status(questId);
    }

    public static boolean healthy(ServerPlayer player) {
        var data = QuestSavedData.get(player.server);
        boolean healthy = !data.isReadOnly() && !data.isQuarantined(player.getUUID());
        if (!healthy && WARNED.add(player.getUUID())) {
            player.sendSystemMessage(Component.translatable("message.dungeonsalive.storage_error")
                    .withStyle(ChatFormatting.RED));
        }
        return healthy;
    }

    private static boolean known(String id, ServerPlayer player) {
        if (repository.snapshot().documents().containsKey(id)) return true;
        player.sendSystemMessage(Component.translatable("message.dungeonsalive.unknown_quest", id));
        return false;
    }

    private static CachedJournal cached(ServerPlayer player) {
        var bundle = repository.snapshot();
        var entry = JOURNALS.get(player.getUUID());
        if (entry == null || entry.bundle != bundle) {
            var snapshot = QuestSavedData.get(player.server).read(player.getUUID());
            entry = new CachedJournal(bundle, new QuestJournal(bundle.catalog(), snapshot));
            JOURNALS.put(player.getUUID(), entry);
        }
        return entry;
    }

    private static boolean sample(ServerPlayer player) {
        var journal = cached(player).journal;
        Map<String, Integer> inventory = new HashMap<>();
        for (int slot = 0; slot < player.getInventory().getContainerSize(); slot++) {
            ItemStack stack = player.getInventory().getItem(slot);
            if (!stack.isEmpty()) {
                inventory.merge(BuiltInRegistries.ITEM.getKey(stack.getItem()).toString(), stack.getCount(), Integer::sum);
            }
        }
        boolean changed = journal.updateInventory(inventory);
        changed |= journal.updateWorldState(WorldStateObjectives.sample(player, journal.activeWorldStateTargets()));
        var biome = player.serverLevel().getBiome(player.blockPosition()).unwrapKey();
        if (biome.isPresent()) changed |= journal.visitBiome(biome.get().location().toString());
        if (changed) persist(player);
        return changed;
    }

    private static void persist(ServerPlayer player) {
        QuestSavedData.get(player.server).write(player.getUUID(), cached(player).journal.snapshot());
    }

    private static void send(ServerPlayer player, boolean open) {
        var cached = cached(player);
        List<JournalView.Entry> entries = cached.bundle.documents().values().stream()
                .sorted(Comparator.comparingInt(QuestDocument::order).thenComparing(d -> d.definition().id()))
                .map(document -> view(document, cached.journal)).toList();
        String json = GSON.toJson(new JournalView(1, entries));
        if (json.length() > JournalPayload.MAX_JSON_CHARS) {
            DungeonsAlive.LOGGER.error("Quest journal exceeds network budget ({} chars); reduce datapack text", json.length());
            if (open) player.sendSystemMessage(Component.translatable("message.dungeonsalive.journal_too_large"));
            return;
        }
        PacketDistributor.sendToPlayer(player, new JournalPayload(open, json));
        cached.needsSync = false;
    }

    private static JournalView.Entry view(QuestDocument document, QuestJournal journal) {
        String id = document.definition().id();
        Map<String, Integer> progress = journal.progress(id);
        var objectives = document.definition().objectives().stream().map(objective -> new JournalView.Objective(
                document.objectiveLabels().getOrDefault(objective.id(), objective.target()),
                progress.getOrDefault(objective.id(), 0), objective.requiredCount())).toList();
        var rewards = document.rewards().stream()
                .map(reward -> new JournalView.Reward(reward.itemId(), reward.count())).toList();
        var prerequisites = document.definition().prerequisites().stream().map(prerequisite -> {
            var other = repository.snapshot().documents().get(prerequisite);
            return other == null ? prerequisite : other.title();
        }).toList();
        return new JournalView.Entry(id, document.title(), document.description(), document.icon(),
                journal.status(id).name(), objectives, rewards, document.experience(), prerequisites);
    }

    private static void give(ServerPlayer player, ItemStack stack) {
        while (!stack.isEmpty()) {
            ItemStack part = stack.split(Math.min(stack.getMaxStackSize(), stack.getCount()));
            if (!player.getInventory().add(part)) player.drop(part, false);
        }
    }

    private static final class CachedJournal {
        final QuestRepository.Bundle bundle;
        final QuestJournal journal;
        boolean needsSync = true;
        CachedJournal(QuestRepository.Bundle bundle, QuestJournal journal) {
            this.bundle = bundle;
            this.journal = journal;
        }
    }
}
