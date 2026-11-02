package de.laugenbreze.dungeonsalive.quest.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import de.laugenbreze.dungeonsalive.quest.core.ObjectiveType;
import de.laugenbreze.dungeonsalive.quest.core.QuestDefinition;
import de.laugenbreze.dungeonsalive.quest.core.QuestObjective;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Strict bounded JSON parsing with no dependency on a running Minecraft registry.
 * Registry existence and optional installed mods are checked by {@link QuestRepository}.
 */
public final class QuestDocumentParser {
    public static final int MAX_QUESTS = 128;
    public static final int MAX_OBJECTIVES = 16;
    public static final int MAX_REWARDS = 16;
    public static final int MAX_ID_LENGTH = 256;
    private static final Pattern RESOURCE_ID = Pattern.compile("[a-z0-9_.-]+:[a-z0-9/._-]+");
    private static final Pattern OBJECTIVE_ID = Pattern.compile("[a-z0-9_.-]{1,64}");
    private static final Pattern MOD_ID = Pattern.compile("[a-z][a-z0-9_]{0,63}");
    private static final Set<String> ROOT_KEYS = Set.of("schema_version", "title", "description", "icon",
            "order", "prerequisites", "objectives", "rewards", "experience", "requires_mods");
    private static final Set<String> OBJECTIVE_KEYS = Set.of("id", "type", "target", "amount", "label");
    private static final Set<String> REWARD_KEYS = Set.of("item", "count");

    private QuestDocumentParser() {}

    public record Parsed(QuestDocument document, List<String> requiresMods) {
        public Parsed {
            requiresMods = List.copyOf(requiresMods);
        }
    }

    public static Parsed parse(String questId, JsonElement json) {
        requireResourceId(questId, "quest ID");
        JsonObject root = object(json, questId);
        checkKeys(root, ROOT_KEYS, questId);
        if (integer(root, "schema_version", null, 1, 1, questId) != 1) {
            throw invalid(questId, "unsupported schema_version");
        }
        String title = string(root, "title", null, 128, false, questId);
        String description = string(root, "description", "", 2048, true, questId);
        String icon = requireResourceId(string(root, "icon", "minecraft:book", MAX_ID_LENGTH, false, questId), "icon");
        int order = integer(root, "order", 0, -1_000_000, 1_000_000, questId);
        int experience = integer(root, "experience", 0, 0, 100_000, questId);

        var prerequisites = new ArrayList<String>();
        JsonArray prerequisiteJson = array(root, "prerequisites", false, MAX_QUESTS, questId);
        for (JsonElement value : prerequisiteJson) {
            prerequisites.add(requireResourceId(primitiveString(value, MAX_ID_LENGTH, false, "prerequisite"), "prerequisite"));
        }

        var requiresMods = new ArrayList<String>();
        var uniqueMods = new HashSet<String>();
        for (JsonElement value : array(root, "requires_mods", false, 16, questId)) {
            String modId = primitiveString(value, 64, false, "requires_mods");
            if (!MOD_ID.matcher(modId).matches() || !uniqueMods.add(modId)) {
                throw invalid(questId, "invalid or duplicate required mod ID: " + modId);
            }
            requiresMods.add(modId);
        }

        var objectives = new ArrayList<QuestObjective>();
        var labels = new LinkedHashMap<String, String>();
        JsonArray objectiveJson = array(root, "objectives", true, MAX_OBJECTIVES, questId);
        if (objectiveJson.isEmpty()) {
            throw invalid(questId, "objectives must not be empty");
        }
        for (JsonElement value : objectiveJson) {
            JsonObject objective = object(value, "objective in " + questId);
            checkKeys(objective, OBJECTIVE_KEYS, questId);
            String id = string(objective, "id", null, 64, false, questId);
            if (!isObjectiveId(id)) {
                throw invalid(questId, "invalid objective ID: " + id);
            }
            ObjectiveType type = switch (string(objective, "type", null, 32, false, questId)) {
                case "kill_entity" -> ObjectiveType.KILL_ENTITY;
                case "visit_biome" -> ObjectiveType.VISIT_BIOME;
                case "collect_item" -> ObjectiveType.COLLECT_ITEM;
                case "world_state" -> ObjectiveType.WORLD_STATE;
                default -> throw invalid(questId, "unsupported objective type");
            };
            String target = requireResourceId(string(objective, "target", null, MAX_ID_LENGTH, false, questId), "target");
            int amount = integer(objective, "amount", 1, 1, 1_000_000, questId);
            String label = string(objective, "label", id, 160, false, questId);
            objectives.add(new QuestObjective(id, type, target, amount));
            labels.put(id, label);
        }

        var rewards = new ArrayList<QuestDocument.Reward>();
        for (JsonElement value : array(root, "rewards", false, MAX_REWARDS, questId)) {
            JsonObject reward = object(value, "reward in " + questId);
            checkKeys(reward, REWARD_KEYS, questId);
            String item = requireResourceId(string(reward, "item", null, MAX_ID_LENGTH, false, questId), "reward item");
            int count = integer(reward, "count", 1, 1, 64, questId);
            rewards.add(new QuestDocument.Reward(item, count));
        }
        return new Parsed(new QuestDocument(new QuestDefinition(questId, prerequisites, objectives),
                title, description, icon, labels, rewards, experience, order), requiresMods);
    }

    public static boolean isResourceId(String id) {
        return id != null && id.length() <= MAX_ID_LENGTH && RESOURCE_ID.matcher(id).matches();
    }

    public static boolean isObjectiveId(String id) {
        return id != null && OBJECTIVE_ID.matcher(id).matches();
    }

    private static String requireResourceId(String id, String label) {
        if (!isResourceId(id)) {
            throw new IllegalArgumentException("Invalid " + label + ": " + id + "; use namespace:path");
        }
        return id;
    }

    private static JsonObject object(JsonElement value, String label) {
        if (value == null || !value.isJsonObject()) {
            throw invalid(label, "expected a JSON object");
        }
        return value.getAsJsonObject();
    }

    private static JsonArray array(JsonObject root, String key, boolean required, int max, String label) {
        JsonElement value = root.get(key);
        if (value == null && !required) {
            return new JsonArray();
        }
        if (value == null || !value.isJsonArray() || value.getAsJsonArray().size() > max) {
            throw invalid(label, key + " must be an array with at most " + max + " entries");
        }
        return value.getAsJsonArray();
    }

    private static String string(JsonObject root, String key, String fallback, int max,
                                 boolean allowBlank, String label) {
        JsonElement value = root.get(key);
        if (value == null && fallback != null) {
            return fallback;
        }
        return primitiveString(value, max, allowBlank, label + "." + key);
    }

    private static String primitiveString(JsonElement value, int max, boolean allowBlank, String label) {
        if (!(value instanceof JsonPrimitive primitive) || !primitive.isString()) {
            throw invalid(label, "expected a string");
        }
        String text = primitive.getAsString();
        if (text.length() > max || (!allowBlank && text.isBlank()) || text.indexOf('\0') >= 0) {
            throw invalid(label, "string is blank, contains NUL, or exceeds " + max + " characters");
        }
        return text;
    }

    private static int integer(JsonObject root, String key, Integer fallback, int min, int max, String label) {
        JsonElement value = root.get(key);
        if (value == null && fallback != null) {
            return fallback;
        }
        if (!(value instanceof JsonPrimitive primitive) || !primitive.isNumber()) {
            throw invalid(label, key + " must be an integer");
        }
        try {
            int number = primitive.getAsBigDecimal().intValueExact();
            if (number < min || number > max) {
                throw invalid(label, key + " must be between " + min + " and " + max);
            }
            return number;
        } catch (ArithmeticException | NumberFormatException ex) {
            throw invalid(label, key + " must be an exact integer between " + min + " and " + max);
        }
    }

    private static void checkKeys(JsonObject object, Set<String> allowed, String label) {
        for (String key : object.keySet()) {
            if (!allowed.contains(key)) {
                throw invalid(label, "unknown field: " + key);
            }
        }
    }

    private static IllegalArgumentException invalid(String label, String message) {
        return new IllegalArgumentException(label + ": " + message);
    }
}
