package de.laugenbreze.dungeonsalive.settlement;

import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SettlementRegistryTest {
    private static SettlementRegistry.Anchor anchor(UUID owner, String dimension, int x) {
        return new SettlementRegistry.Anchor(UUID.randomUUID(), owner, dimension, x, 65, 0,
                List.of(UUID.randomUUID(), UUID.randomUUID()));
    }
    @Test void ownerAndGlobalCapsApplyAcrossDimensionsWithoutDeletingOldAnchors() {
        var registry = new SettlementRegistry(); UUID owner = UUID.randomUUID();
        for (int i = 0; i < 3; i++) assertTrue(registry.add(anchor(owner, "test:dimension_" + i, 0)));
        assertEquals(SettlementRegistry.Decision.OWNER_LIMIT, registry.check(owner, "test:another", 2_000, 0));
        for (int i = 3; i < SettlementRegistry.MAX_SETTLEMENTS; i++) assertTrue(registry.add(anchor(UUID.randomUUID(), "minecraft:overworld", i * 100)));
        assertEquals(SettlementRegistry.Decision.WORLD_LIMIT, registry.check(UUID.randomUUID(), "minecraft:the_end", 0, 0));
        assertEquals(SettlementRegistry.MAX_SETTLEMENTS, registry.anchors().size());
    }
    @Test void foreignPlayersCannotOverlapOrReplaceAnExistingSettlement() {
        var registry = new SettlementRegistry(); var first = anchor(UUID.randomUUID(), "minecraft:overworld", 0);
        assertTrue(registry.add(first));
        assertEquals(SettlementRegistry.Decision.NEARBY, registry.check(UUID.randomUUID(), "minecraft:overworld", 95, 0));
        assertEquals(SettlementRegistry.Decision.AVAILABLE, registry.check(UUID.randomUUID(), "minecraft:overworld", 96, 0));
        assertEquals(SettlementRegistry.Decision.AVAILABLE, registry.check(UUID.randomUUID(), "minecraft:the_nether", 0, 0));
        assertFalse(registry.add(new SettlementRegistry.Anchor(first.id(), UUID.randomUUID(), "minecraft:the_end", 1_000, 65, 0,
                List.of(UUID.randomUUID(), UUID.randomUUID()))));
        assertEquals(List.of(first), registry.anchors());
    }
    @Test void reloadKeepsExactlyOnceBossClaimsAndPermanentResidentReservations() {
        var registry = new SettlementRegistry(); UUID boss = UUID.randomUUID();
        var settlement = anchor(UUID.randomUUID(), "minecraft:overworld", 0);
        assertTrue(registry.claimReward(boss)); assertTrue(registry.add(settlement));
        var reload = new SettlementRegistry(); assertTrue(reload.restore(registry.anchors(), registry.rewardClaims()));
        assertFalse(reload.claimReward(boss));
        assertEquals(settlement.residents(), reload.anchors().getFirst().residents());
        assertFalse(reload.add(new SettlementRegistry.Anchor(UUID.randomUUID(), UUID.randomUUID(), "minecraft:the_end", 0, 65, 0,
                settlement.residents())));
    }
    @Test void invalidHistoryFailsClosedInsteadOfAllowingReplayOrUnboundedRecruitment() {
        UUID boss = UUID.randomUUID(); var registry = new SettlementRegistry();
        assertFalse(registry.restore(List.of(), List.of(boss, boss)));
        assertFalse(registry.claimReward(UUID.randomUUID()));
        assertEquals(SettlementRegistry.Decision.BLOCKED, registry.check(UUID.randomUUID(), "minecraft:overworld", 0, 0));
        UUID resident = UUID.randomUUID();
        assertThrows(IllegalArgumentException.class, () -> new SettlementRegistry.Anchor(UUID.randomUUID(), UUID.randomUUID(),
                "minecraft:overworld", 0, 64, 0, List.of(resident, resident)));
    }
}
