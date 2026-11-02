package de.laugenbreze.dungeonsalive.quest.data;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import de.laugenbreze.dungeonsalive.DungeonsAlive;
import de.laugenbreze.dungeonsalive.quest.core.ObjectiveType;
import de.laugenbreze.dungeonsalive.quest.core.QuestCatalog;
import de.laugenbreze.dungeonsalive.quest.core.QuestObjective;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.Items;
import net.neoforged.fml.ModList;

import java.util.ArrayList;
import java.io.Reader;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** Server data-pack quests from data/[namespace]/dungeonsalive/quests/[path].json. */
public final class QuestRepository extends SimpleJsonResourceReloadListener {
    private volatile Bundle current = new Bundle(new QuestCatalog(List.of()), Map.of(), 0);
    private volatile List<String> warnings = List.of();

    public QuestRepository() {
        super(new GsonBuilder().disableHtmlEscaping().create(), DungeonsAlive.MOD_ID + "/quests");
    }

    public record Bundle(QuestCatalog catalog, Map<String, QuestDocument> documents, long revision) {
        public Bundle {
            documents = Collections.unmodifiableMap(new LinkedHashMap<>(documents));
        }
    }

    public Bundle snapshot() {
        return current;
    }

    public List<String> warnings() {
        return warnings;
    }

    /**
     * Vanilla's JSON listener logs and skips malformed resources. Here a malformed quest
     * rejects this entire quest reload so an incomplete catalog cannot replace a valid one.
     */
    @Override
    protected Map<ResourceLocation, JsonElement> prepare(ResourceManager resourceManager, ProfilerFiller profiler) {
        var prepared = new PreparedQuests();
        String directory = DungeonsAlive.MOD_ID + "/quests";
        String prefix = directory + "/";
        try {
            var files = resourceManager.listResources(directory, id -> id.getPath().endsWith(".json"));
            if (files.size() > QuestDocumentParser.MAX_QUESTS) {
                throw new IllegalArgumentException("Too many quest resources: " + files.size());
            }
            for (var entry : files.entrySet()) {
                ResourceLocation file = entry.getKey();
                String path = file.getPath();
                if (!path.startsWith(prefix)) continue;
                ResourceLocation questId = ResourceLocation.fromNamespaceAndPath(file.getNamespace(),
                        path.substring(prefix.length(), path.length() - ".json".length()));
                try (Reader reader = entry.getValue().openAsReader()) {
                    var text = new StringBuilder();
                    char[] buffer = new char[2048];
                    int read;
                    while ((read = reader.read(buffer)) != -1) {
                        if (text.length() + read > 65_536) {
                            throw new IllegalArgumentException("Quest JSON exceeds 65536 characters: " + file);
                        }
                        text.append(buffer, 0, read);
                    }
                    prepared.put(questId, JsonParser.parseString(text.toString()));
                } catch (Exception ex) {
                    throw new IllegalArgumentException("Cannot parse " + file + ": " + ex.getMessage(), ex);
                }
            }
        } catch (Exception ex) {
            prepared.failure = ex.getMessage();
        }
        return prepared;
    }

    private static final class PreparedQuests extends LinkedHashMap<ResourceLocation, JsonElement> {
        private String failure;
    }

    @Override
    protected synchronized void apply(Map<ResourceLocation, JsonElement> resources,
                                      ResourceManager resourceManager, ProfilerFiller profiler) {
        var notices = new ArrayList<String>();
        try {
            if (resources instanceof PreparedQuests prepared && prepared.failure != null) {
                throw new IllegalArgumentException(prepared.failure);
            }
            if (resources.size() > QuestDocumentParser.MAX_QUESTS) {
                throw new IllegalArgumentException("Too many quests: " + resources.size()
                        + " (maximum " + QuestDocumentParser.MAX_QUESTS + ")");
            }
            var candidates = new LinkedHashMap<String, QuestDocument>();
            var disabled = new HashSet<String>();
            var keys = new ArrayList<>(resources.keySet());
            keys.sort(Comparator.comparing(ResourceLocation::toString));
            for (ResourceLocation location : keys) {
                var parsed = QuestDocumentParser.parse(location.toString(), resources.get(location));
                List<String> absent = parsed.requiresMods().stream()
                        .filter(id -> !ModList.get().isLoaded(id)).toList();
                if (!absent.isEmpty()) {
                    disabled.add(location.toString());
                    notices.add("Skipped " + location + ": required mods not installed: " + absent);
                } else {
                    candidates.put(location.toString(), parsed.document());
                }
            }

            // Disable the dependency closure of optional missing mods. A typo in an unrelated
            // prerequisite is still rejected by QuestCatalog, rather than silently disappearing.
            boolean removed;
            do {
                removed = false;
                var iterator = candidates.entrySet().iterator();
                while (iterator.hasNext()) {
                    var entry = iterator.next();
                    List<String> unavailable = entry.getValue().definition().prerequisites().stream()
                            .filter(disabled::contains).toList();
                    if (!unavailable.isEmpty()) {
                        notices.add("Skipped " + entry.getKey() + ": disabled prerequisites: " + unavailable);
                        disabled.add(entry.getKey());
                        iterator.remove();
                        removed = true;
                    }
                }
            } while (removed);

            var ordered = new LinkedHashMap<String, QuestDocument>();
            candidates.values().stream().sorted(Comparator.comparingInt(QuestDocument::order)
                            .thenComparing(document -> document.definition().id()))
                    .forEach(document -> ordered.put(document.definition().id(), document));
            for (QuestDocument document : ordered.values()) {
                validateRegistries(document);
            }
            var catalog = new QuestCatalog(ordered.values().stream().map(QuestDocument::definition).toList());
            Bundle replacement = new Bundle(catalog, ordered, current.revision() + 1);
            current = replacement;
            warnings = List.copyOf(notices);
            notices.forEach(message -> DungeonsAlive.LOGGER.warn("{}", message));
            DungeonsAlive.LOGGER.info("Loaded {} Reborn Wilds quests (revision {})",
                    ordered.size(), replacement.revision());
        } catch (RuntimeException ex) {
            String rejection = "Quest reload rejected; keeping revision " + current.revision() + ": " + ex.getMessage();
            notices.add(rejection);
            warnings = List.copyOf(notices);
            DungeonsAlive.LOGGER.error("{}", rejection);
        }
    }

    private static void validateRegistries(QuestDocument document) {
        String questId = document.definition().id();
        requireItem(document.icon(), questId + " icon");
        for (QuestDocument.Reward reward : document.rewards()) {
            requireItem(reward.itemId(), questId + " reward");
        }
        for (QuestObjective objective : document.definition().objectives()) {
            ResourceLocation target = requireLocation(objective.target(), questId + "/" + objective.id());
            if (objective.type() == ObjectiveType.COLLECT_ITEM) {
                requireItem(objective.target(), questId + "/" + objective.id());
            } else if (objective.type() == ObjectiveType.KILL_ENTITY
                    && !BuiltInRegistries.ENTITY_TYPE.containsKey(target)) {
                throw new IllegalArgumentException(questId + ": unknown entity target " + target);
            }
            if (objective.type() == ObjectiveType.WORLD_STATE
                    && !de.laugenbreze.dungeonsalive.quest.WorldStateObjectives.known(target))
                throw new IllegalArgumentException(questId + ": unknown world condition " + target);
            // Biomes are dynamic registries. Syntax is checked here; the live server adapter
            // checks availability against its level registry before allowing the quest.
        }
    }

    private static void requireItem(String value, String context) {
        ResourceLocation id = requireLocation(value, context);
        if (!BuiltInRegistries.ITEM.containsKey(id) || BuiltInRegistries.ITEM.get(id) == Items.AIR) {
            throw new IllegalArgumentException(context + ": unknown or unusable item " + value);
        }
    }

    private static ResourceLocation requireLocation(String value, String context) {
        ResourceLocation id = ResourceLocation.tryParse(value);
        if (id == null) {
            throw new IllegalArgumentException(context + ": invalid resource ID " + value);
        }
        return id;
    }
}
