package de.laugenbreze.dungeonsalive.quest.data;

import de.laugenbreze.dungeonsalive.DungeonsAlive;
import de.laugenbreze.dungeonsalive.quest.core.QuestSnapshot;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/**
 * Per-player quest state in the Overworld's data/dungeonsalive_quests.dat.
 * Access belongs to the logical server thread. Corrupt players are quarantined with their
 * original NBT instead of being silently restarted, and unknown schemas are read-only.
 */
public final class QuestSavedData extends SavedData {
    private static final int SCHEMA_VERSION = 1;
    private static final int MAX_SAVED_QUESTS_PER_PLAYER = 4096;
    private static final int MAX_SAVED_OBJECTIVES_PER_QUEST = 128;
    private static final String FILE_NAME = DungeonsAlive.MOD_ID + "_quests";
    private static final Factory<QuestSavedData> FACTORY =
            new Factory<>(QuestSavedData::new, QuestSavedData::load, null);

    private final Map<UUID, PlayerEntry> players = new LinkedHashMap<>();
    private final Set<UUID> quarantinedPlayers = new HashSet<>();
    private final List<CompoundTag> quarantine = new ArrayList<>();
    private CompoundTag preservedRoot = new CompoundTag();
    private CompoundTag readOnlyRoot;

    private record PlayerEntry(QuestSnapshot snapshot, boolean receivedJournal, CompoundTag template) {}

    public QuestSavedData() {}

    public static QuestSavedData get(MinecraftServer server) {
        Objects.requireNonNull(server, "server");
        return server.overworld().getDataStorage().computeIfAbsent(FACTORY, FILE_NAME);
    }

    public boolean isReadOnly() {
        return readOnlyRoot != null;
    }

    public boolean isQuarantined(UUID playerId) {
        return quarantinedPlayers.contains(playerId);
    }

    public QuestSnapshot read(UUID playerId) {
        ensureWritable(playerId);
        PlayerEntry entry = players.get(playerId);
        return entry == null ? QuestSnapshot.empty() : entry.snapshot();
    }

    /** Marks SavedData dirty only when the immutable snapshot has actually changed. */
    public boolean write(UUID playerId, QuestSnapshot snapshot) {
        ensureWritable(playerId);
        Objects.requireNonNull(snapshot, "snapshot");
        PlayerEntry previous = players.get(playerId);
        QuestSnapshot before = previous == null ? QuestSnapshot.empty() : previous.snapshot();
        if (before.equals(snapshot)) {
            return false;
        }
        players.put(playerId, new PlayerEntry(snapshot, previous != null && previous.receivedJournal(),
                previous == null ? new CompoundTag() : previous.template()));
        setDirty();
        return true;
    }

    public boolean hasReceivedJournal(UUID playerId) {
        ensureWritable(playerId);
        PlayerEntry entry = players.get(playerId);
        return entry != null && entry.receivedJournal();
    }

    public boolean markReceivedJournal(UUID playerId) {
        ensureWritable(playerId);
        PlayerEntry previous = players.get(playerId);
        if (previous != null && previous.receivedJournal()) {
            return false;
        }
        players.put(playerId, new PlayerEntry(previous == null ? QuestSnapshot.empty() : previous.snapshot(), true,
                previous == null ? new CompoundTag() : previous.template()));
        setDirty();
        return true;
    }

    private void ensureWritable(UUID playerId) {
        Objects.requireNonNull(playerId, "playerId");
        if (isReadOnly()) {
            throw new IllegalStateException("Reborn Wilds quest data has an unknown or corrupt schema; file is protected");
        }
        if (isQuarantined(playerId)) {
            throw new IllegalStateException("Reborn Wilds quest data for " + playerId + " is quarantined; original NBT is preserved");
        }
    }

    public static QuestSavedData load(CompoundTag tag, HolderLookup.Provider registries) {
        var data = new QuestSavedData();
        data.preservedRoot = tag.copy();
        if (!tag.contains("schema_version", Tag.TAG_INT) || tag.getInt("schema_version") != SCHEMA_VERSION) {
            data.readOnlyRoot = tag.copy();
            DungeonsAlive.LOGGER.error("Quest saved data schema is unknown or missing. The file will remain read-only; no progress is reset.");
            return data;
        }
        ListTag playerTags;
        ListTag oldQuarantine;
        try {
            playerTags = list(tag, "players", Tag.TAG_COMPOUND, true);
            oldQuarantine = list(tag, "quarantined_players", Tag.TAG_COMPOUND, false);
        } catch (IllegalArgumentException ex) {
            data.readOnlyRoot = tag.copy();
            DungeonsAlive.LOGGER.error("Quest saved data is protected because its root is corrupt: {}", ex.getMessage());
            return data;
        }

        for (int index = 0; index < oldQuarantine.size(); index++) {
            CompoundTag raw = oldQuarantine.getCompound(index).copy();
            data.quarantine.add(raw);
            UUID playerId = optionalUuid(raw);
            if (playerId != null) data.quarantinedPlayers.add(playerId);
        }
        for (int index = 0; index < playerTags.size(); index++) {
            CompoundTag raw = playerTags.getCompound(index);
            UUID playerId = optionalUuid(raw);
            try {
                if (playerId == null) {
                    throw new IllegalArgumentException("missing or invalid UUID");
                }
                if (data.quarantinedPlayers.contains(playerId)) {
                    throw new IllegalArgumentException("UUID also occurs in quarantined data");
                }
                if (data.players.containsKey(playerId)) {
                    PlayerEntry duplicate = data.players.remove(playerId);
                    data.quarantine.add(duplicate.template().copy());
                    throw new IllegalArgumentException("duplicate player UUID");
                }
                PlayerEntry entry = parsePlayer(raw, playerId);
                data.players.put(playerId, entry);
            } catch (RuntimeException ex) {
                data.quarantine.add(raw.copy());
                if (playerId != null) data.quarantinedPlayers.add(playerId);
                DungeonsAlive.LOGGER.error("Quarantined quest entry for {}: {}. Original NBT is retained, other players are unaffected.",
                        playerId == null ? "unknown UUID" : playerId, ex.getMessage());
            }
        }
        return data;
    }

    private static PlayerEntry parsePlayer(CompoundTag raw, UUID playerId) {
        Set<String> accepted = questIds(raw, "accepted");
        Set<String> claimed = questIds(raw, "claimed");
        ListTag progressTags = list(raw, "progress", Tag.TAG_COMPOUND, true);
        if (progressTags.size() > MAX_SAVED_QUESTS_PER_PLAYER) {
            throw new IllegalArgumentException("too many saved quest entries");
        }
        var progress = new LinkedHashMap<String, Map<String, Integer>>();
        for (int index = 0; index < progressTags.size(); index++) {
            CompoundTag questTag = progressTags.getCompound(index);
            if (!questTag.contains("quest", Tag.TAG_STRING)) {
                throw new IllegalArgumentException("progress entry is missing quest ID");
            }
            String questId = questTag.getString("quest");
            if (!QuestDocumentParser.isResourceId(questId) || progress.containsKey(questId)) {
                throw new IllegalArgumentException("invalid or duplicate progress quest ID: " + questId);
            }
            if (!questTag.contains("objectives", Tag.TAG_COMPOUND)) {
                throw new IllegalArgumentException("progress entry is missing objective counts");
            }
            CompoundTag countsTag = questTag.getCompound("objectives");
            if (countsTag.getAllKeys().size() > MAX_SAVED_OBJECTIVES_PER_QUEST) {
                throw new IllegalArgumentException("too many saved objectives for " + questId);
            }
            var counts = new LinkedHashMap<String, Integer>();
            for (String objectiveId : countsTag.getAllKeys()) {
                if (!QuestDocumentParser.isObjectiveId(objectiveId) || !countsTag.contains(objectiveId, Tag.TAG_INT)) {
                    throw new IllegalArgumentException("invalid saved objective: " + objectiveId);
                }
                int count = countsTag.getInt(objectiveId);
                if (count < 0) {
                    DungeonsAlive.LOGGER.warn("Clamped negative quest count to zero for {}/{}/{}", playerId, questId, objectiveId);
                }
                counts.put(objectiveId, Math.max(0, count));
            }
            progress.put(questId, counts);
        }
        if (raw.contains("intro_seen") && !raw.contains("intro_seen", Tag.TAG_BYTE)) {
            throw new IllegalArgumentException("intro_seen must be a boolean tag");
        }
        return new PlayerEntry(new QuestSnapshot(accepted, claimed, progress), raw.getBoolean("intro_seen"), raw.copy());
    }

    private static Set<String> questIds(CompoundTag raw, String key) {
        ListTag values = list(raw, key, Tag.TAG_STRING, true);
        if (values.size() > MAX_SAVED_QUESTS_PER_PLAYER) {
            throw new IllegalArgumentException("too many IDs in " + key);
        }
        var result = new HashSet<String>();
        for (int index = 0; index < values.size(); index++) {
            String id = values.getString(index);
            if (!QuestDocumentParser.isResourceId(id)) {
                throw new IllegalArgumentException("invalid quest ID in " + key + ": " + id);
            }
            result.add(id);
        }
        return result;
    }

    private static ListTag list(CompoundTag tag, String key, int elementType, boolean required) {
        Tag raw = tag.get(key);
        if (raw == null && !required) {
            return new ListTag();
        }
        if (!(raw instanceof ListTag result)
                || (!result.isEmpty() && result.getElementType() != elementType)) {
            throw new IllegalArgumentException("missing or invalid NBT list: " + key);
        }
        return result;
    }

    private static UUID optionalUuid(CompoundTag raw) {
        if (!raw.contains("uuid", Tag.TAG_STRING)) return null;
        String text = raw.getString("uuid");
        try {
            UUID playerId = UUID.fromString(text);
            return playerId.toString().equalsIgnoreCase(text) ? playerId : null;
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }

    @Override
    public CompoundTag save(CompoundTag tag, HolderLookup.Provider registries) {
        if (readOnlyRoot != null) {
            return tag.merge(readOnlyRoot.copy());
        }
        tag.merge(preservedRoot.copy());
        tag.putInt("schema_version", SCHEMA_VERSION);
        var playerTags = new ListTag();
        players.entrySet().stream().sorted(Map.Entry.comparingByKey()).forEach(entry -> {
            CompoundTag raw = entry.getValue().template().copy();
            QuestSnapshot snapshot = entry.getValue().snapshot();
            raw.putString("uuid", entry.getKey().toString());
            raw.putBoolean("intro_seen", entry.getValue().receivedJournal());
            raw.put("accepted", strings(snapshot.acceptedQuests()));
            raw.put("claimed", strings(snapshot.claimedQuests()));
            var progress = new ListTag();
            snapshot.objectiveProgress().entrySet().stream().sorted(Map.Entry.comparingByKey()).forEach(quest -> {
                var questTag = new CompoundTag();
                questTag.putString("quest", quest.getKey());
                var countsTag = new CompoundTag();
                quest.getValue().entrySet().stream().sorted(Map.Entry.comparingByKey())
                        .forEach(count -> countsTag.putInt(count.getKey(), count.getValue()));
                questTag.put("objectives", countsTag);
                progress.add(questTag);
            });
            raw.put("progress", progress);
            playerTags.add(raw);
        });
        tag.put("players", playerTags);
        var quarantined = new ListTag();
        quarantine.forEach(raw -> quarantined.add(raw.copy()));
        tag.put("quarantined_players", quarantined);
        return tag;
    }

    private static ListTag strings(Set<String> values) {
        var list = new ListTag();
        values.stream().sorted().forEach(value -> list.add(StringTag.valueOf(value)));
        return list;
    }
}
