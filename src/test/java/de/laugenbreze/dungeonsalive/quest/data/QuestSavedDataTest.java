package de.laugenbreze.dungeonsalive.quest.data;

import de.laugenbreze.dungeonsalive.quest.core.QuestSnapshot;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class QuestSavedDataTest {
    @Test
    void roundTripKeepsProgressJournalDeliveryAndNoOpWrites() {
        UUID player = UUID.fromString("00000000-0000-0000-0000-000000000001");
        var snapshot = new QuestSnapshot(Set.of("test:active", "test:done"), Set.of("test:done"),
                Map.of("test:active", Map.of("kills", 2)));
        var data = new QuestSavedData();
        assertFalse(data.write(player, QuestSnapshot.empty()));
        assertTrue(data.write(player, snapshot));
        assertFalse(data.write(player, snapshot));
        assertTrue(data.markReceivedJournal(player));
        assertFalse(data.markReceivedJournal(player));
        CompoundTag encoded = data.save(new CompoundTag(), null);
        QuestSavedData restored = QuestSavedData.load(encoded, null);
        assertEquals(snapshot, restored.read(player));
        assertTrue(restored.hasReceivedJournal(player));
        assertFalse(restored.write(player, snapshot));
        assertEquals(encoded, restored.save(new CompoundTag(), null));
    }

    @Test
    void corruptPlayerIsPreservedAndBlockedWhileAnotherPlayerStillWorks() {
        UUID healthy = UUID.fromString("00000000-0000-0000-0000-000000000001");
        UUID corrupt = UUID.fromString("00000000-0000-0000-0000-000000000002");
        var snapshot = new QuestSnapshot(Set.of("test:q"), Set.of(), Map.of("test:q", Map.of("kills", 2)));
        var data = new QuestSavedData();
        data.write(healthy, snapshot);
        data.write(corrupt, snapshot);
        CompoundTag encoded = data.save(new CompoundTag(), null);
        encoded.getList("players", Tag.TAG_COMPOUND).getCompound(1).putString("progress", "broken");
        QuestSavedData restored = QuestSavedData.load(encoded, null);
        assertFalse(restored.isReadOnly());
        assertEquals(snapshot, restored.read(healthy));
        assertTrue(restored.isQuarantined(corrupt));
        assertThrows(IllegalStateException.class, () -> restored.read(corrupt));
        assertThrows(IllegalStateException.class, () -> restored.write(corrupt, QuestSnapshot.empty()));
        CompoundTag resaved = restored.save(new CompoundTag(), null);
        assertEquals(1, resaved.getList("players", Tag.TAG_COMPOUND).size());
        assertEquals("broken", resaved.getList("quarantined_players", Tag.TAG_COMPOUND).getCompound(0).getString("progress"));
        assertTrue(QuestSavedData.load(resaved, null).isQuarantined(corrupt));
    }

    @Test
    void unknownSchemaIsNeverOverwrittenAsAnEmptyCurrentSchema() {
        var future = new CompoundTag();
        future.putInt("schema_version", 999);
        future.putString("future_data", "must survive");
        QuestSavedData data = QuestSavedData.load(future, null);
        assertTrue(data.isReadOnly());
        assertThrows(IllegalStateException.class, () -> data.read(UUID.randomUUID()));
        assertThrows(IllegalStateException.class, () -> data.markReceivedJournal(UUID.randomUUID()));
        assertEquals(future, data.save(new CompoundTag(), null));
    }
}
