package de.laugenbreze.dungeonsalive.quest.data;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class QuestDocumentParserTest {
    private static JsonObject valid() {
        return JsonParser.parseString("""
                {
                  "schema_version": 1,
                  "title": "Spuren im Wald",
                  "description": "Erkunde den Wald und sammle Beeren.",
                  "icon": "minecraft:compass",
                  "prerequisites": ["dungeonsalive:arrival"],
                  "objectives": [
                    {"id":"forest", "type":"visit_biome", "target":"minecraft:forest", "amount":1, "label":"Wald besuchen"},
                    {"id":"berries", "type":"collect_item", "target":"minecraft:sweet_berries", "amount":8, "label":"Beeren im Inventar"}
                  ],
                  "rewards": [{"item":"minecraft:emerald", "count":2}],
                  "experience": 15,
                  "order": 20,
                  "requires_mods": ["example_mod"]
                }
                """).getAsJsonObject();
    }

    @Test
    void parsesQuestWithoutNeedingMinecraftOrModRegistries() {
        var parsed = QuestDocumentParser.parse("dungeonsalive:forest", valid());
        assertEquals("Spuren im Wald", parsed.document().title());
        assertEquals(2, parsed.document().definition().objectives().size());
        assertEquals("Beeren im Inventar", parsed.document().objectiveLabels().get("berries"));
        assertEquals(2, parsed.document().rewards().getFirst().count());
        assertEquals("example_mod", parsed.requiresMods().getFirst());
        assertEquals(15, parsed.document().experience());
    }

    @Test
    void rejectsUnknownSchemaAndFieldsInsteadOfSilentlyLosingMeaning() {
        var json = valid();
        json.addProperty("schema_version", 2);
        assertThrows(IllegalArgumentException.class, () -> QuestDocumentParser.parse("test:q", json));
        json.addProperty("schema_version", 1);
        json.addProperty("experiance", 50);
        assertThrows(IllegalArgumentException.class, () -> QuestDocumentParser.parse("test:q", json));
    }

    @Test
    void rejectsFractionalStringAndOutOfRangeRewardCounts() {
        var json = valid();
        var reward = json.getAsJsonArray("rewards").get(0).getAsJsonObject();
        reward.addProperty("count", 1.5);
        assertThrows(IllegalArgumentException.class, () -> QuestDocumentParser.parse("test:q", json));
        reward.addProperty("count", "2");
        assertThrows(IllegalArgumentException.class, () -> QuestDocumentParser.parse("test:q", json));
        reward.addProperty("count", 65);
        assertThrows(IllegalArgumentException.class, () -> QuestDocumentParser.parse("test:q", json));
    }

    @Test
    void rejectsBadIdentifiersAndAmbiguousMissingNamespaces() {
        var json = valid();
        json.addProperty("icon", "emerald");
        assertThrows(IllegalArgumentException.class, () -> QuestDocumentParser.parse("test:q", json));
        json.addProperty("icon", "minecraft:emerald");
        assertThrows(IllegalArgumentException.class, () -> QuestDocumentParser.parse("BadNamespace:q", json));
        json.getAsJsonArray("objectives").get(1).getAsJsonObject().addProperty("target", "mod:invalid path");
        assertThrows(IllegalArgumentException.class, () -> QuestDocumentParser.parse("test:q", json));
    }

    @Test
    void rejectsEmptyExcessiveAndDuplicateObjectives() {
        var json = valid();
        var objectives = json.getAsJsonArray("objectives");
        objectives.add(objectives.get(0).deepCopy());
        assertThrows(IllegalArgumentException.class, () -> QuestDocumentParser.parse("test:q", json));
        objectives.remove(2);
        for (int index = 0; index < 15; index++) {
            var copy = objectives.get(0).deepCopy().getAsJsonObject();
            copy.addProperty("id", "extra_" + index);
            objectives.add(copy);
        }
        assertThrows(IllegalArgumentException.class, () -> QuestDocumentParser.parse("test:q", json));
        while (!objectives.isEmpty()) objectives.remove(0);
        assertThrows(IllegalArgumentException.class, () -> QuestDocumentParser.parse("test:q", json));
    }

    @Test
    void boundedTextAndCorrectPrimitiveTypesProtectTheJournalPayload() {
        var json = valid();
        json.addProperty("title", "x".repeat(129));
        assertThrows(IllegalArgumentException.class, () -> QuestDocumentParser.parse("test:q", json));
        json.addProperty("title", true);
        assertThrows(IllegalArgumentException.class, () -> QuestDocumentParser.parse("test:q", json));
        json.addProperty("title", "Hallo");
        json.addProperty("description", "bad\0text");
        assertThrows(IllegalArgumentException.class, () -> QuestDocumentParser.parse("test:q", json));
    }
}
