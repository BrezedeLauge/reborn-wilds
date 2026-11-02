package de.laugenbreze.dungeonsalive.entity.peaceful;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.WeakHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.EventHooks;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import org.jetbrains.annotations.Nullable;

/** Server-only, bounded habitat population in existing loaded chunks, independent of vanilla's scarce passive cap. */
public final class WildlifeEncounters {
    private static final Map<ServerLevel, Cursor> CURSORS = new WeakHashMap<>();
    private static final int PROBES = 12;
    private static final int MIN_DISTANCE = 24;
    private static final int RADIUS = 64;
    private static final class Cursor {
        final Map<UUID, Long> nextAttempt = new HashMap<>();
        int player;
    }
    public static void register() { NeoForge.EVENT_BUS.addListener(WildlifeEncounters::tick); }

    static boolean enabled(ServerLevel level) {
        return WildlifeConfig.NATURAL_SPAWNS.get() && WildlifeConfig.EXPLORATION_ENCOUNTERS.get()
                && level.getGameRules().getBoolean(GameRules.RULE_DOMOBSPAWNING);
    }

    private static void tick(LevelTickEvent.Post event) {
        if (!(event.getLevel() instanceof ServerLevel level) || level.getGameTime() % 40 != 0 || !enabled(level)) return;
        var players = level.players();
        if (players.isEmpty()) { CURSORS.remove(level); return; }
        Cursor cursor = CURSORS.computeIfAbsent(level, ignored -> new Cursor());
        cursor.nextAttempt.keySet().removeIf(id -> players.stream().noneMatch(p -> p.getUUID().equals(id)));
        ServerPlayer player = players.get(Math.floorMod(cursor.player++, players.size()));
        if (!player.isAlive() || player.isSpectator() || player.isSleeping()
                || level.getGameTime() < cursor.nextAttempt.getOrDefault(player.getUUID(), Long.MIN_VALUE)) return;
        cursor.nextAttempt.put(player.getUUID(), level.getGameTime() + WildlifeConfig.ENCOUNTER_INTERVAL.get());
        int loaded = 0;
        for (var entity : level.getAllEntities()) {
            if (entity instanceof DungeonsCreature creature && counted(creature) && ++loaded >= WildlifeConfig.LOADED_LIMIT.get()) return;
        }
        var history = HabitatPopulationSavedData.get(level);
        Vec3 view = player.getLookAngle();
        double heading = Math.atan2(view.z, view.x);
        for (int attempt = 0; attempt < PROBES; attempt++) {
            double angle = heading + (level.random.nextDouble() - .5) * (attempt < 8 ? Math.PI : Math.PI * 2);
            double distance = 32 + level.random.nextDouble() * 24;
            int x = Mth.floor(player.getX() + Math.cos(angle) * distance);
            int z = Mth.floor(player.getZ() + Math.sin(angle) * distance);
            if (level.getChunkSource().getChunkNow(x >> 4, z >> 4) == null) continue;
            int surface = level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, x, z);
            boolean underground = !level.dimensionType().hasSkyLight() || player.getY() < surface - 10;
            int y = underground ? player.blockPosition().getY() : surface;
            BlockPos column = new BlockPos(x, y, z);
            if (!WildlifeNavigationSafety.loadedArea(level, column, WildlifeNavigationSafety.LOADED_RADIUS)
                    || !level.getWorldBorder().isWithinBounds(column)) continue;
            long region = ChunkPos.asLong(x >> 6, z >> 6);
            if (!history.budget.available(region, level.getGameTime())) continue;
            String biome = level.getBiome(column).unwrapKey().map(key -> key.location().toString()).orElse("");
            if (underground && level.dimensionType().hasSkyLight()
                    && !biome.endsWith("_caves") && !biome.equals("minecraft:deep_dark")) biome = "minecraft:dripstone_caves";
            var candidates = WildlifeHabitats.forBiome(biome).stream()
                    .filter(population -> !underground || population.layer() != WildlifeHabitats.Layer.CANOPY)
                    .filter(population -> speciesEnabled(population.id()) && active(level, population.id(), underground)).toList();
            if (candidates.isEmpty()) continue;
            var population = candidates.get(level.random.nextInt(candidates.size()));
            BlockPos origin = placement(level, column, population.layer(), underground);
            if (origin == null || Math.abs(origin.getY() - player.getY()) > 80) continue;
            int nearby = level.getEntitiesOfClass(DungeonsCreature.class, new AABB(origin).inflate(RADIUS), WildlifeEncounters::counted).size();
            int desired = population.min() + level.random.nextInt(population.max() - population.min() + 1);
            int count = HabitatPopulationBudget.groupSize(desired, nearby, WildlifeConfig.NEARBY_LIMIT.get(), loaded, WildlifeConfig.LOADED_LIMIT.get());
            if (count == 0) return;
            // Avoid stacking the same animal group on a visible existing group.
            if (!level.getEntitiesOfClass(DungeonsCreature.class, new AABB(origin).inflate(24),
                    other -> counted(other) && other.profile().id().equals(population.id())).isEmpty()) continue;
            if (spawnGroup(level, population, origin, count, underground) >= 2) {
                history.budget.recordSuccess(region, level.getGameTime());
                history.setDirty();
                return;
            }
        }
    }

    static boolean counted(DungeonsCreature creature) {
        return creature.isAlive() && !creature.isRemoved() && creature.ownerId() == null
                && (creature.profile().kind() == PeacefulProfiles.Kind.WILDLIFE || creature.naturalWildlife());
    }

    static boolean speciesEnabled(String id) {
        return switch (id) {
            case "wooly_cow" -> WildlifeConfig.WOOLY_COWS.get();
            case "vulture" -> WildlifeConfig.VULTURES.get();
            case "wisp" -> WildlifeConfig.WISPS.get();
            case "pet_toucan", "pet_golden_parrot", "pet_red_panda" -> WildlifeConfig.JUNGLE_WILDLIFE.get();
            case "pet_raven" -> WildlifeConfig.FOREST_BIRDS.get();
            default -> true;
        };
    }

    private static boolean active(ServerLevel level, String id, boolean underground) {
        if (underground || PeacefulProfiles.get(id).aquatic()) return true;
        boolean night = Math.floorMod(level.getDayTime(), 24_000L) >= 12_000;
        return PeacefulProfiles.get(id).nocturnalWildlife() == night;
    }

    @Nullable static BlockPos placement(ServerLevel level, BlockPos column, WildlifeHabitats.Layer layer, boolean underground) {
        if (!WildlifeNavigationSafety.loadedArea(level, column, 4)) return null;
        if (layer == WildlifeHabitats.Layer.WATER_SURFACE) {
            if (underground) return null;
            int top = level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, column.getX(), column.getZ()) - 1;
            BlockPos pos = new BlockPos(column.getX(), top, column.getZ());
            return DuckHabitat.surfaceWater(level, pos) ? pos : null;
        }
        if (layer == WildlifeHabitats.Layer.WATER) {
            int top = underground ? column.getY() + 4 : level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, column.getX(), column.getZ()) - 1;
            for (int y = top - 2; y >= top - 12 && y > level.getMinBuildHeight() + 1; y--) {
                var pos = new BlockPos(column.getX(), y, column.getZ());
                if (level.getFluidState(pos).is(FluidTags.WATER) && level.getFluidState(pos.above()).is(FluidTags.WATER)
                        && level.getFluidState(pos.below()).is(FluidTags.WATER)) return pos;
            }
            return null;
        }
        if (layer == WildlifeHabitats.Layer.CANOPY && !underground) {
            int top = level.getHeight(Heightmap.Types.MOTION_BLOCKING, column.getX(), column.getZ());
            var floor = new BlockPos(column.getX(), top - 1, column.getZ());
            if (!naturalFloor(level, floor)) return null;
            return floor.above(4);
        }
        int top = underground ? Math.min(column.getY() + 8, level.getMaxBuildHeight() - 4)
                : level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, column.getX(), column.getZ());
        int bottom = underground ? column.getY() - 10 : top - 3;
        for (int y = top; y >= bottom && y > level.getMinBuildHeight(); y--) {
            BlockPos pos = new BlockPos(column.getX(), y, column.getZ());
            if (naturalFloor(level, pos.below()) && level.getBlockState(pos).isAir() && level.getBlockState(pos.above()).isAir())
                return layer == WildlifeHabitats.Layer.LOCAL_AIR ? pos.above(2) : pos;
        }
        return null;
    }

    private static boolean naturalFloor(ServerLevel level, BlockPos pos) {
        var state = level.getBlockState(pos);
        if (!state.getFluidState().isEmpty()) return false;
        return state.is(BlockTags.DIRT) || state.is(BlockTags.SAND) || state.is(BlockTags.LEAVES)
                || state.is(BlockTags.BASE_STONE_OVERWORLD) || state.is(BlockTags.BASE_STONE_NETHER)
                || state.is(net.minecraft.world.level.block.Blocks.GRAVEL) || state.is(net.minecraft.world.level.block.Blocks.SNOW_BLOCK)
                || state.is(net.minecraft.world.level.block.Blocks.SNOW) || state.is(net.minecraft.world.level.block.Blocks.ICE)
                || state.is(net.minecraft.world.level.block.Blocks.PACKED_ICE) || state.is(net.minecraft.world.level.block.Blocks.BLUE_ICE)
                || state.is(net.minecraft.world.level.block.Blocks.END_STONE) || state.is(net.minecraft.world.level.block.Blocks.SOUL_SAND)
                || state.is(net.minecraft.world.level.block.Blocks.SOUL_SOIL) || state.is(net.minecraft.world.level.block.Blocks.MOSS_BLOCK);
    }

    static int spawnGroup(ServerLevel level, WildlifeHabitats.Population population, BlockPos origin, int desired, boolean underground) {
        if (!enabled(level) || !speciesEnabled(population.id()) || desired < 2 || desired > WildlifeGroupPolicy.MAX_MEMBERS) return 0;
        int loaded = 0;
        for (var entity : level.getAllEntities()) if (entity instanceof DungeonsCreature creature && counted(creature)) loaded++;
        int nearby = level.getEntitiesOfClass(DungeonsCreature.class, new AABB(origin).inflate(RADIUS), WildlifeEncounters::counted).size();
        desired = HabitatPopulationBudget.groupSize(desired, nearby, WildlifeConfig.NEARBY_LIMIT.get(), loaded, WildlifeConfig.LOADED_LIMIT.get());
        if (desired == 0) return 0;
        // An adult alone or with just one juvenile is not a successful family encounter.
        if (population.id().equals("pet_duck") && desired < 3) return 0;
        List<DungeonsCreature> added = new ArrayList<>();
        SpawnGroupData group = null;
        for (int slot = 0; slot < desired; slot++) {
            double angle = slot * Math.PI * 2 / desired;
            BlockPos column = origin.offset((int)Math.round(Math.cos(angle) * 4), 0, (int)Math.round(Math.sin(angle) * 4));
            BlockPos pos = placement(level, column, population.layer(), underground);
            if (pos == null || !level.getWorldBorder().isWithinBounds(pos)
                    || !WildlifeNavigationSafety.loadedArea(level, pos, 4)
                    || level.players().stream().anyMatch(p -> !p.isSpectator() && p.distanceToSqr(Vec3.atCenterOf(pos)) < MIN_DISTANCE * MIN_DISTANCE)) continue;
            DungeonsCreature animal = ModPeacefulEntities.creatureType(population.id()).create(level);
            if (animal == null) continue;
            animal.moveTo(pos.getX() + .5, pos.getY() + (population.layer() == WildlifeHabitats.Layer.WATER_SURFACE ? .8 : 0), pos.getZ() + .5, level.random.nextFloat() * 360, 0);
            boolean clear = population.layer() == WildlifeHabitats.Layer.WATER_SURFACE ? DuckHabitat.safe(animal, pos)
                    : population.layer() == WildlifeHabitats.Layer.WATER ? WildlifeSchoolGoal.safeWater(animal, animal.position())
                    : population.layer() == WildlifeHabitats.Layer.GROUND ? WildlifeNavigationSafety.safeGround(animal, pos)
                    : WildlifeFlightNavigation.safeBody(animal, animal.position());
            // Aquatic mobs have an explicit water-volume check; the base Mob obstruction method rejects all liquid.
            if (!clear || !EventHooks.checkSpawnPosition(animal, level, MobSpawnType.NATURAL)) { animal.discard(); continue; }
            var next = EventHooks.finalizeMobSpawn(animal, level, level.getCurrentDifficultyAt(pos), MobSpawnType.NATURAL, group);
            if (!animal.naturalWildlife() || !level.addFreshEntity(animal)) { animal.discard(); continue; }
            group = next;
            added.add(animal);
        }
        if (added.size() < (population.id().equals("pet_duck") ? 3 : 2)) { added.forEach(DungeonsCreature::discard); return 0; }
        return added.size();
    }
    private WildlifeEncounters() {}
}
