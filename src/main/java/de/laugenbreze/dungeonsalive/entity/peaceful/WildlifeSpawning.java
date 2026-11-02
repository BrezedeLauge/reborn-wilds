package de.laugenbreze.dungeonsalive.entity.peaceful;

import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;

/** Native spawn placement predicates; biome entries are generated with peaceful resources. */
public final class WildlifeSpawning {
    public static final List<String> NATURAL_TYPES = List.of("wooly_cow", "vulture", "wisp");

    public static void register(RegisterSpawnPlacementsEvent event) {
        for (String id : NATURAL_TYPES) {
            // Fliers are born into clear air above ground and take off through their flight AI.
            // This avoids an unbounded independent air-spawn scheduler or additional mob cap.
            event.register(ModPeacefulEntities.creatureType(id), SpawnPlacementTypes.ON_GROUND,
                    Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                    (type, level, reason, pos, random) -> canSpawn(id, type, level, reason, pos, random),
                    RegisterSpawnPlacementsEvent.Operation.REPLACE);
        }
    }

    static boolean canSpawn(String id, EntityType<DungeonsCreature> type, ServerLevelAccessor level,
                            MobSpawnType reason, BlockPos pos, RandomSource random) {
        boolean natural = reason == MobSpawnType.NATURAL || reason == MobSpawnType.CHUNK_GENERATION;
        if (natural && (!WildlifeConfig.NATURAL_SPAWNS.get() || !enabled(id))) return false;
        if (pos.getY() <= level.getMinBuildHeight() || pos.getY() + 2 >= level.getMaxBuildHeight()
                || !level.hasChunkAt(pos) || !level.getWorldBorder().isWithinBounds(pos)) return false;
        if (!Mob.checkMobSpawnRules(type, level, reason, pos, random)
                || !level.getFluidState(pos).isEmpty() || !level.getFluidState(pos.above()).isEmpty()
                || !level.getBlockState(pos.below()).isFaceSturdy(level, pos.below(), Direction.UP)) return false;
        if (natural) {
            long time = Math.floorMod(level.getLevel().getDayTime(), 24_000L);
            if (id.equals("wisp")) {
                if (time < 12_000) return false;
            } else if (time >= 12_000 || level.getMaxLocalRawBrightness(pos) <= 8) {
                return false;
            }
            if (id.equals("wooly_cow") && !level.getBlockState(pos.below()).is(BlockTags.ANIMALS_SPAWNABLE_ON)) return false;
            // Live natural spawning runs on the server thread. WorldGenRegion may
            // run on a chunk worker: never query its backing server's live entities.
            // Chunk generation keeps Minecraft's own per-pack generation limits.
            if (reason == MobSpawnType.NATURAL && level instanceof ServerLevel server) {
                int nearby = server.getEntitiesOfClass(DungeonsCreature.class, new AABB(pos).inflate(48),
                        WildlifeEncounters::counted).size();
                if (nearby >= WildlifeConfig.NEARBY_LIMIT.get()) return false;
            }
        }
        return true;
    }

    private static boolean enabled(String id) {
        return switch (id) {
            case "wooly_cow" -> WildlifeConfig.WOOLY_COWS.get();
            case "vulture" -> WildlifeConfig.VULTURES.get();
            case "wisp" -> WildlifeConfig.WISPS.get();
            default -> false;
        };
    }

    private WildlifeSpawning() {}
}
