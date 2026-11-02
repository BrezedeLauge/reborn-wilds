package de.laugenbreze.dungeonsalive.world.corruption;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.level.PistonEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

/** Natural ground changes only: fixed work budgets, authenticated worldgen soil and permanent edit protection. */
public final class CorruptionEvents {
    private CorruptionEvents() {}
    public static void register() {
        NeoForge.EVENT_BUS.addListener(CorruptionEvents::tick);
        NeoForge.EVENT_BUS.addListener(EventPriority.HIGHEST, true, CorruptionEvents::placed);
        NeoForge.EVENT_BUS.addListener(EventPriority.HIGHEST, true, CorruptionEvents::broken);
        NeoForge.EVENT_BUS.addListener(EventPriority.HIGHEST, true, CorruptionEvents::tool);
        NeoForge.EVENT_BUS.addListener(EventPriority.HIGHEST, true, CorruptionEvents::interacted);
        NeoForge.EVENT_BUS.addListener(EventPriority.HIGHEST, true, CorruptionEvents::piston);
    }
    private static void tick(ServerTickEvent.Post event) {
        if (event.getServer().getTickCount() % 20 != 0) return;
        discover(event.getServer());
        advance(event.getServer());
    }
    private static void discover(MinecraftServer server) {
        var players = server.getPlayerList().getPlayers();
        if (players.isEmpty()) return;
        // At most four player columns per pulse, round-robin even on large servers.
        int start = Math.floorMod(server.getTickCount() / 20 * 4, players.size());
        for (int i = 0; i < Math.min(4, players.size()); i++) {
            var player = players.get((start + i) % players.size());
            var level = player.serverLevel(); var pos = player.blockPosition();
            if (player.isSpectator() || !level.hasChunkAt(pos) || !level.getBiome(pos).is(CorruptionContent.BIOME)) continue;
            var data = CorruptionSavedData.get(server);
            var region = data.discover(level.dimension().location().toString(), pos);
            if (region != null) {
                player.displayClientMessage(Component.translatable("message.dungeonsalive.corruption.discovered"), false);
            }
        }
    }

    static void advance(MinecraftServer server) {
        var data = CorruptionSavedData.get(server);
        if (data.frozen() || data.regions.isEmpty()) return;
        int changes = 0;
        for (int checked = 0; checked < CorruptionFront.CHECKS_PER_PULSE && changes < CorruptionFront.CHANGES_PER_PULSE; checked++) {
            var region = data.regions.get(Math.floorMod(data.nextRegion++, data.regions.size()));
            var level = server.getLevel(ResourceKey.create(Registries.DIMENSION, ResourceLocation.parse(region.dimension)));
            if (level == null) continue; // No dimension creation or chunk lookup on behalf of a front.
            if (region.front.stage() == CorruptionFront.Stage.CLEANSING) {
                if (region.originals.isEmpty()) { region.front.restoredAll(); data.setDirty(); continue; }
                var entry = region.originals.entrySet().iterator().next();
                var position = entry.getKey(); var original = entry.getValue();
                // Rotate blocked/unloaded entries so another loaded patch can heal.
                region.originals.remove(position); region.originals.put(position, original);
                if (!loaded(level, position, 1) || !level.getWorldBorder().isWithinBounds(position)) continue;
                if (data.protectedAt(region.dimension, position.getX(), position.getZ())
                        || !level.getBlockState(position).is(CorruptionContent.BLIGHTED_GRASS.get())
                        || !level.getBlockState(position.below()).is(CorruptionContent.NATURAL_SOIL.get())) {
                    region.originals.remove(position); data.setDirty(); continue;
                }
                if (change(level, position, original)) { region.originals.remove(position); changes++; data.setDirty(); }
                continue;
            }
            if (data.growthDisabled() || !region.front.canGrow()) continue;
            var offset = CorruptionFront.at(region.front.cursor());
            int x = region.x + offset.x(), z = region.z + offset.z();
            if (!offset.inCircle() || data.protectedAt(region.dimension, x, z)) { region.front.scanned(); data.setDirty(); continue; }
            BlockPos column = new BlockPos(x, level.getMinBuildHeight(), z);
            if (!loaded(level, column, 1)) continue; // Preserve the cursor; resume only after ordinary chunk loading.
            region.front.scanned(); data.setDirty();
            int y = level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, x, z) - 1;
            BlockPos position = new BlockPos(x, y, z);
            if (y <= level.getMinBuildHeight() || y + 1 >= level.getMaxBuildHeight() || !level.getWorldBorder().isWithinBounds(position)
                    || !level.getBlockState(position.below()).is(CorruptionContent.NATURAL_SOIL.get())
                    || !level.getBlockState(position).is(Blocks.GRASS_BLOCK) || !level.isEmptyBlock(position.above())
                    || level.getBlockEntity(position) != null) continue;
            BlockState original = level.getBlockState(position);
            if (change(level, position, CorruptionContent.BLIGHTED_GRASS.get().defaultBlockState())) {
                region.originals.put(position.immutable(), original); region.front.grew(); changes++; data.setDirty();
            }
        }
    }

    private static boolean change(ServerLevel level, BlockPos position, BlockState after) {
        var event = new CorruptionChangeEvent(level, position, level.getBlockState(position), after);
        NeoForge.EVENT_BUS.post(event);
        return !event.isCanceled() && level.setBlock(position, after, Block.UPDATE_ALL);
    }
    static boolean loaded(ServerLevel level, BlockPos position, int radius) {
        for (int x = (position.getX() - radius) >> 4; x <= (position.getX() + radius) >> 4; x++)
            for (int z = (position.getZ() - radius) >> 4; z <= (position.getZ() + radius) >> 4; z++)
                if (!level.hasChunk(x, z)) return false;
        return true;
    }
    private static void placed(BlockEvent.EntityPlaceEvent event) {
        if (!(event.getLevel() instanceof ServerLevel level)) return;
        protect(level, event.getPos());
        if (event instanceof BlockEvent.EntityMultiPlaceEvent multi)
            for (var snapshot : multi.getReplacedBlockSnapshots()) protect(level, snapshot.getPos());
    }
    private static void broken(BlockEvent.BreakEvent event) {
        if (event.getLevel() instanceof ServerLevel level) protect(level, event.getPos());
    }
    private static void tool(BlockEvent.BlockToolModificationEvent event) {
        if (!event.isSimulated() && event.getLevel() instanceof ServerLevel level) protect(level, event.getPos());
    }
    private static void interacted(PlayerInteractEvent.RightClickBlock event) {
        if (event.getLevel() instanceof ServerLevel level && !(event.getItemStack().getItem() instanceof CleansingFocusItem)) {
            protect(level, event.getPos());
            if (event.getFace() != null) protect(level, event.getPos().relative(event.getFace()));
        }
    }
    private static void protect(ServerLevel level, BlockPos position) {
        var data = CorruptionSavedData.get(level.getServer()); String dimension = level.dimension().location().toString();
        // Protect whole columns, so a roof or player-grown plant also protects the natural floor beneath it.
        for (int dx = -1; dx <= 1; dx++) for (int dz = -1; dz <= 1; dz++) {
            data.protect(dimension, position.getX() + dx, position.getZ() + dz);
            for (int dy = -2; dy <= 0; dy++) {
                var candidate = position.offset(dx, dy, dz);
                if (loaded(level, candidate, 0) && level.getBlockState(candidate).is(CorruptionContent.NATURAL_SOIL.get()))
                    level.setBlock(candidate, Blocks.DIRT.defaultBlockState(), Block.UPDATE_ALL);
            }
        }
    }
    private static void piston(PistonEvent.Pre event) {
        if (!(event.getLevel() instanceof ServerLevel level)) return;
        if (!loaded(level, event.getPos(), 14)) {
            // A vanilla piston affects at most twelve moved blocks. Protect the bounded
            // envelope without asking its resolver to inspect missing neighbor chunks.
            var data = CorruptionSavedData.get(level.getServer());
            for (int x = -13; x <= 13; x++) for (int z = -13; z <= 13; z++)
                data.protect(level.dimension().location().toString(), event.getPos().getX() + x, event.getPos().getZ() + z);
            return;
        }
        var resolver = event.getStructureHelper();
        if (resolver == null || !resolver.resolve()) return;
        for (var position : resolver.getToPush()) {
            protect(level, position); protect(level, position.relative(event.getDirection())); protect(level, position.relative(event.getDirection().getOpposite()));
        }
        for (var position : resolver.getToDestroy()) protect(level, position);
    }
}
