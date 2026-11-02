package de.laugenbreze.dungeonsalive.quest;

import de.laugenbreze.dungeonsalive.settlement.inn.InnService;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

/** Explicit, authoritative world conditions; clients cannot send values or reinterpret older objective IDs. */
public final class WorldStateObjectives {
    public static final Set<String> CONDITIONS = Set.of("dungeonsalive:inn_ready", "dungeonsalive:inn_connected", "dungeonsalive:guest_arrived");
    private WorldStateObjectives() { }
    public static boolean known(ResourceLocation id) { return CONDITIONS.contains(id.toString()); }
    public static Map<String, Boolean> sample(ServerPlayer player, Set<String> activeTargets) {
        var result = new LinkedHashMap<String, Boolean>();
        for (String target : activeTargets) {
            // Unknown data cannot authorize progress, including catalogs saved by an older adapter.
            result.put(target, CONDITIONS.contains(target) && InnService.worldState(player, ResourceLocation.parse(target)));
        }
        return Map.copyOf(result);
    }
}
