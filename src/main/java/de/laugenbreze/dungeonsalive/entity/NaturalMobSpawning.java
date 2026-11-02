package de.laugenbreze.dungeonsalive.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;

/** Native spawn predicates; only the biome modifier catalog decides which regular types occur naturally. */
public final class NaturalMobSpawning {
    public static void register(RegisterSpawnPlacementsEvent event) {
        for (MobProfile profile : MobProfiles.all()) {
            if (profile.tier() != MobProfile.Tier.REGULAR) continue;
            event.register(ModEntities.type(profile.id()), profile.aquatic() ? SpawnPlacementTypes.IN_WATER : SpawnPlacementTypes.ON_GROUND,
                    Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                    (type, level, reason, pos, random) -> canSpawn(profile, type, level, reason, pos, random),
                    RegisterSpawnPlacementsEvent.Operation.REPLACE);
        }
    }

    private static boolean canSpawn(MobProfile profile, EntityType<DungeonMob> type, ServerLevelAccessor level,
                                    MobSpawnType reason, BlockPos pos, RandomSource random) {
        boolean natural = reason == MobSpawnType.NATURAL || reason == MobSpawnType.CHUNK_GENERATION;
        if (natural && !NaturalMobConfig.NATURAL_HOSTILES.get()) return false;
        AABB body = type.getSpawnAABB(pos.getX() + .5, pos.getY(), pos.getZ() + .5);
        if (!loadedCollisionRegion(level, body) || level.getDifficulty() == Difficulty.PEACEFUL) return false;
        if (profile.aquatic()) {
            return (MobSpawnType.ignoresLightRequirements(reason) || Monster.isDarkEnoughToSpawn(level, pos, random))
                    && waterVolume(level, body) && level.noCollision(body);
        }
        // Standard Overworld darkness would prevent most Nether/End natural entries from ever appearing.
        boolean anyLight = level.getLevel().dimension() == Level.NETHER || level.getLevel().dimension() == Level.END;
        return (anyLight ? Monster.checkAnyLightMonsterSpawnRules(type, level, reason, pos, random)
                : Monster.checkMonsterSpawnRules(type, level, reason, pos, random))
                && level.noCollision(body);
    }

    /** Used by DungeonMob's real obstruction hook: Mob's default rejects every liquid, including water. */
    public static boolean aquaticObstruction(DungeonMob mob, LevelReader level) {
        AABB body = mob.getBoundingBox();
        return mob.profile() != null && mob.profile().aquatic() && loadedCollisionRegion(level, body)
                && waterVolume(level, body) && level.isUnobstructed(mob) && level.noCollision(mob);
    }

    private static boolean loadedCollisionRegion(LevelReader level, AABB box) {
        if (box.getXsize() > 8 || box.getYsize() > 8 || box.getZsize() > 8
                || box.minY <= level.getMinBuildHeight() || box.maxY >= level.getMaxBuildHeight()
                || !level.getWorldBorder().isWithinBounds(box)) return false;
        // Vanilla collision iteration probes a one-block edge around the AABB. Guard that entire region first.
        int minX = (Mth.floor(box.minX - 1.0E-7) - 1) >> 4, maxX = (Mth.floor(box.maxX + 1.0E-7) + 1) >> 4;
        int minZ = (Mth.floor(box.minZ - 1.0E-7) - 1) >> 4, maxZ = (Mth.floor(box.maxZ + 1.0E-7) + 1) >> 4;
        for (int x = minX; x <= maxX; x++) for (int z = minZ; z <= maxZ; z++) {
            if (!level.hasChunkAt(new BlockPos(x << 4, Mth.floor(box.minY), z << 4))) return false;
        }
        return true;
    }

    private static boolean waterVolume(LevelReader level, AABB box) {
        for (BlockPos pos : BlockPos.betweenClosed(Mth.floor(box.minX), Mth.floor(box.minY), Mth.floor(box.minZ),
                Mth.floor(box.maxX - 1.0E-6), Mth.floor(box.maxY - 1.0E-6), Mth.floor(box.maxZ - 1.0E-6))) {
            var fluid = level.getFluidState(pos);
            if (!fluid.is(FluidTags.WATER)
                    || pos.getY() + fluid.getHeight(level, pos) + 1.0E-6 < Math.min(box.maxY, pos.getY() + 1.0)) return false;
        }
        return true;
    }

    private NaturalMobSpawning() {}
}
