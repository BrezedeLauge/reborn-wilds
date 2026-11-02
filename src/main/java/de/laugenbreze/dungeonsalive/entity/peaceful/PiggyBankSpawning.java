package de.laugenbreze.dungeonsalive.entity.peaceful;

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

/** Uses the normal creature spawner, not a repeating extra treasure-entity scheduler. */
public final class PiggyBankSpawning {
    public static void register(RegisterSpawnPlacementsEvent event) {
        event.register(ModPeacefulEntities.creatureType("piggy_bank"), SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, PiggyBankSpawning::canSpawn,
                RegisterSpawnPlacementsEvent.Operation.REPLACE);
    }
    static boolean canSpawn(EntityType<DungeonsCreature> type, ServerLevelAccessor level, MobSpawnType reason,
                            BlockPos pos, RandomSource random) {
        boolean natural = reason == MobSpawnType.NATURAL || reason == MobSpawnType.CHUNK_GENERATION;
        if (natural && (!PiggyBankConfig.NATURAL_SPAWNS.get() || !WildlifeConfig.NATURAL_SPAWNS.get())) return false;
        if (!level.hasChunkAt(pos) || !level.getWorldBorder().isWithinBounds(pos)
                || pos.getY() <= level.getMinBuildHeight() || pos.getY() + 2 >= level.getMaxBuildHeight()) return false;
        if (!Mob.checkMobSpawnRules(type, level, reason, pos, random)
                || !level.getFluidState(pos).isEmpty() || !level.getFluidState(pos.above()).isEmpty()
                || !level.getBlockState(pos.below()).isFaceSturdy(level, pos.below(), Direction.UP)) return false;
        if (!natural) return true;
        if (Math.floorMod(level.getLevel().getDayTime(), 24_000) >= 12_000 || level.getMaxLocalRawBrightness(pos) <= 8
                || !level.getBlockState(pos.below()).is(BlockTags.ANIMALS_SPAWNABLE_ON)) return false;
        if (reason == MobSpawnType.NATURAL && level instanceof ServerLevel server) {
            var wildlife = server.getEntitiesOfClass(DungeonsCreature.class, new AABB(pos).inflate(48),
                    WildlifeEncounters::counted);
            if (wildlife.size() >= WildlifeConfig.NEARBY_LIMIT.get() || wildlife.stream().filter(DungeonsCreature::piggyBank).count() >= 2) return false;
        }
        // WorldGenRegion may run on a chunk worker: its bounded one-animal pack never
        // reads the backing server's live entity collection. The extra rarity applies to both paths.
        return random.nextInt(8) == 0;
    }
    private PiggyBankSpawning() {}
}
