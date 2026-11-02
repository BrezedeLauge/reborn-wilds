package de.laugenbreze.dungeonsalive.world;

import java.util.Map;
import java.util.WeakHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

/** Reports an actual nearby generated structure start; never invokes locate or generates exploration chunks. */
public final class WayfinderStoneBlock extends Block {
    private static final java.util.Set<String> SITES = java.util.Set.of("overgrown_arena", "redstone_mineyard", "sunken_shrine");
    private static final Map<Player, Long> LAST_READ = new WeakHashMap<>();
    public WayfinderStoneBlock(Properties properties) { super(properties); }
    @Override protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        if (!(level instanceof ServerLevel server)) return InteractionResult.SUCCESS;
        long now = level.getGameTime(), previous = LAST_READ.getOrDefault(player, Long.MIN_VALUE);
        if (previous != Long.MIN_VALUE && now >= previous && now - previous < 20) return InteractionResult.CONSUME;
        LAST_READ.put(player, now);
        var clue = nearest(server, pos);
        if (clue == null) player.displayClientMessage(Component.translatable("message.dungeonsalive.waystone.no_traces"), false);
        else {
            int dx = clue.position.getX() - pos.getX(), dz = clue.position.getZ() - pos.getZ();
            int distance = (int)Math.round(Math.sqrt((double)dx * dx + (double)dz * dz));
            if (distance <= 28) player.displayClientMessage(Component.translatable("message.dungeonsalive.waystone.here",
                    Component.translatable("structure.dungeonsalive." + clue.site)), false);
            else player.displayClientMessage(Component.translatable("message.dungeonsalive.waystone.direction",
                    Component.translatable("structure.dungeonsalive." + clue.site),
                    Component.translatable("direction.dungeonsalive." + direction(dx, dz)), distance), false);
        }
        return InteractionResult.CONSUME;
    }
    public record Clue(String site, BlockPos position) {}
    public static Clue nearest(ServerLevel level, BlockPos origin) {
        Clue nearest = null;
        double distance = 192 * 192;
        var registry = level.registryAccess().registryOrThrow(Registries.STRUCTURE);
        // At most 17x17 existing chunk maps, only on deliberate interaction, no entity/world scan each tick.
        for (int x = (origin.getX() >> 4) - 8; x <= (origin.getX() >> 4) + 8; x++) {
            for (int z = (origin.getZ() >> 4) - 8; z <= (origin.getZ() >> 4) + 8; z++) {
                var chunk = level.getChunkSource().getChunkNow(x, z);
                if (chunk == null) continue;
                for (var entry : chunk.getAllStarts().entrySet()) {
                    var id = registry.getKey(entry.getKey());
                    var start = entry.getValue();
                    if (id == null || !id.getNamespace().equals("dungeonsalive") || !SITES.contains(id.getPath()) || !start.isValid()) continue;
                    BlockPos center = start.getBoundingBox().getCenter();
                    double candidate = origin.distSqr(center);
                    if (candidate < distance) { distance = candidate; nearest = new Clue(id.getPath(), center); }
                }
            }
        }
        return nearest;
    }
    static String direction(int x, int z) {
        String eastWest = Math.abs(x) > Math.abs(z) * .42 ? (x > 0 ? "east" : "west") : "";
        String northSouth = Math.abs(z) > Math.abs(x) * .42 ? (z > 0 ? "south" : "north") : "";
        return northSouth + eastWest;
    }
}
