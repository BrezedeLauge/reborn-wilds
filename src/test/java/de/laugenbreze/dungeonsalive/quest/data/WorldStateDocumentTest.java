package de.laugenbreze.dungeonsalive.quest.data;

import com.google.gson.JsonParser;
import de.laugenbreze.dungeonsalive.quest.core.ObjectiveType;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class WorldStateDocumentTest {
    @Test void worldStateIsAnExplicitNewTypeWithoutChangingExistingObjectiveIds() {
        var json = JsonParser.parseString("""
                {"schema_version":1,"title":"Das offene Gasthaus","description":"Ein nutzbarer Raum und Weg.",
                 "icon":"minecraft:oak_sign","prerequisites":[],"objectives":[
                  {"id":"ready","type":"world_state","target":"dungeonsalive:inn_ready","amount":1,"label":"Gasthaus nutzbar"}],
                 "rewards":[{"item":"minecraft:bread","count":2}]}
                """).getAsJsonObject();
        var parsed = QuestDocumentParser.parse("test:inn", json).document();
        assertEquals(ObjectiveType.WORLD_STATE, parsed.definition().objectives().getFirst().type());
        assertEquals("ready", parsed.definition().objectives().getFirst().id());
        json.getAsJsonArray("objectives").get(0).getAsJsonObject().addProperty("amount", 2);
        assertThrows(IllegalArgumentException.class, () -> QuestDocumentParser.parse("test:inn", json));
    }
}
