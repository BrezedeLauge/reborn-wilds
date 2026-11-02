package de.laugenbreze.dungeonsalive.client.ambient;

import de.laugenbreze.dungeonsalive.ambient.AmbientConfig;
import de.laugenbreze.dungeonsalive.ambient.AmbientParticles;
import net.minecraft.client.Minecraft;
import net.minecraft.client.ParticleStatus;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.tags.BiomeTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.common.NeoForge;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/** Physical-client entry point. Never call from the dedicated-server load path. */
public final class AmbientClient {
    private static final List<TrackedParticle> TRACKED = new ArrayList<>();
    private static ClientLevel trackedLevel;
    private static long ticks;

    private AmbientClient() {}

    public static void register(IEventBus modBus) {
        modBus.addListener(AmbientClient::registerProviders);
        NeoForge.EVENT_BUS.addListener(AmbientClient::onClientTick);
    }

    private static void registerProviders(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(AmbientParticles.BUTTERFLY.get(),
                sprites -> new AmbientParticle.Provider(sprites, AmbientParticle.Kind.BUTTERFLY));
        event.registerSpriteSet(AmbientParticles.FIREFLY.get(),
                sprites -> new AmbientParticle.Provider(sprites, AmbientParticle.Kind.FIREFLY));
        event.registerSpriteSet(AmbientParticles.SPORE.get(),
                sprites -> new AmbientParticle.Provider(sprites, AmbientParticle.Kind.SPORE));
    }

    private static void onClientTick(ClientTickEvent.Post event) {
        Minecraft minecraft = Minecraft.getInstance();
        ClientLevel level = minecraft.level;
        if (level != trackedLevel) {
            clearTracked();
            trackedLevel = level;
            ticks = 0;
        }
        if (level == null || minecraft.player == null || minecraft.isPaused()) {
            return;
        }
        ++ticks;

        ParticleStatus particleSetting = minecraft.options.particles().get();
        int limit = AmbientConfig.MAX_PARTICLES.get();
        if (particleSetting == ParticleStatus.DECREASED) {
            limit /= 2;
        }
        if (!AmbientConfig.ENABLED.get() || particleSetting == ParticleStatus.MINIMAL
                || limit == 0 || AmbientConfig.DENSITY.get() == 0) {
            clearTracked();
            return;
        }

        // Hard deadline also releases references after a resource reload drops
        // particles from the engine without necessarily calling Particle.remove().
        for (Iterator<TrackedParticle> iterator = TRACKED.iterator(); iterator.hasNext();) {
            TrackedParticle entry = iterator.next();
            if (!entry.particle().isAlive() || ticks >= entry.deadline()) {
                entry.particle().remove();
                iterator.remove();
            }
        }
        while (TRACKED.size() > limit) {
            TRACKED.remove(TRACKED.size() - 1).particle().remove();
        }
        if (ticks % 5 == 0 && TRACKED.size() < limit) environmentDetails(minecraft, level, limit);
        if (ticks % 5 != 0 || TRACKED.size() >= limit || !level.dimensionType().hasSkyLight()
                || level.isRaining() || minecraft.player.isUnderWater()) {
            return;
        }

        RandomSource random = level.random;
        long time = Math.floorMod(level.getDayTime(), 24_000);
        boolean night = time >= 12_500 && time <= 23_500;
        int radius = AmbientConfig.SPAWN_RADIUS.get();
        BlockPos playerPos = minecraft.player.blockPosition();

        // At most four column probes every five client ticks. Only loaded chunks
        // are considered; this never asks the server to generate new terrain.
        for (int attempt = 0; attempt < 4 && TRACKED.size() < limit; attempt++) {
            float angle = random.nextFloat() * Mth.TWO_PI;
            float distance = 3 + random.nextFloat() * (radius - 3);
            int x = Mth.floor(playerPos.getX() + Mth.cos(angle) * distance);
            int z = Mth.floor(playerPos.getZ() + Mth.sin(angle) * distance);
            if (!level.hasChunkAt(new BlockPos(x, playerPos.getY(), z))) {
                continue;
            }
            int groundY = level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, x, z);
            if (Math.abs(groundY - minecraft.player.getY()) > 8) {
                continue;
            }
            double y = groundY + 0.4 + random.nextDouble() * 1.4;
            BlockPos position = BlockPos.containing(x + 0.5, y, z + 0.5);
            if (!level.getFluidState(position).isEmpty()
                    || !level.getBlockState(position).getCollisionShape(level, position).isEmpty()
                    || level.getBrightness(LightLayer.SKY, position) < 5) {
                continue;
            }

            Holder<Biome> biome = level.getBiome(position);
            boolean woodland = biome.is(BiomeTags.IS_FOREST) || biome.is(BiomeTags.IS_JUNGLE);
            boolean wetland = biome.is(Biomes.SWAMP) || biome.is(Biomes.MANGROVE_SWAMP)
                    || biome.is(BiomeTags.IS_RIVER);
            boolean meadow = biome.is(Biomes.PLAINS) || biome.is(Biomes.SUNFLOWER_PLAINS)
                    || biome.is(Biomes.MEADOW) || biome.is(Biomes.CHERRY_GROVE);

            SimpleParticleType type = null;
            double chance = 0;
            if (night && (woodland || wetland) && AmbientConfig.FIREFLIES.get()) {
                type = AmbientParticles.FIREFLY.get();
                chance = wetland ? 0.48 : 0.30;
            } else if (!night && (woodland || meadow) && AmbientConfig.BUTTERFLIES.get()) {
                type = AmbientParticles.BUTTERFLY.get();
                chance = meadow ? 0.30 : 0.22;
            }
            if (woodland && AmbientConfig.SPORES.get() && random.nextFloat() < 0.14F) {
                type = AmbientParticles.SPORE.get();
                chance = 0.6;
            }
            if (type != null && random.nextDouble() < chance * AmbientConfig.DENSITY.get()) {
                Particle particle = minecraft.particleEngine.createParticle(type,
                        x + random.nextDouble(), y, z + random.nextDouble(), 0, 0, 0);
                if (particle != null) {
                    TRACKED.add(new TrackedParticle(particle, ticks + 400));
                }
            }
        }
    }

    private static void clearTracked() {
        for (TrackedParticle entry : TRACKED) {
            entry.particle().remove();
        }
        TRACKED.clear();
    }

    private static void environmentDetails(Minecraft client, ClientLevel level, int limit) {
        // Two bounded local probes; shares the existing hard budget and vanilla particle preferences.
        BlockPos player = client.player.blockPosition();
        for (int probe = 0; probe < 2 && TRACKED.size() < limit; probe++) {
            BlockPos pos = player.offset(level.random.nextInt(21) - 10, level.random.nextInt(15) - 3, level.random.nextInt(21) - 10);
            if (!level.hasChunkAt(pos) || pos.getY() <= level.getMinBuildHeight() || pos.getY() + 2 >= level.getMaxBuildHeight()) continue;
            Particle particle = null;
            var state = level.getBlockState(pos);
            if (AmbientConfig.LEAF_MOTES.get() && state.is(BlockTags.LEAVES) && level.isEmptyBlock(pos.below())
                    && level.random.nextDouble() < .4 * AmbientConfig.DENSITY.get()) {
                particle = client.particleEngine.createParticle(new BlockParticleOption(ParticleTypes.FALLING_DUST, state),
                        pos.getX() + level.random.nextDouble(), pos.getY() - .05, pos.getZ() + level.random.nextDouble(), .02, -.02, .01);
            } else if (AmbientConfig.WATERFALL_MIST.get()) {
                var fluid = state.getFluidState();
                if (fluid.is(FluidTags.WATER) && fluid.hasProperty(FlowingFluid.FALLING) && fluid.getValue(FlowingFluid.FALLING)
                        && level.getFluidState(pos.above()).is(FluidTags.WATER) && level.getFluidState(pos.below()).isSource()
                        && level.getFluidState(pos.below()).is(FluidTags.WATER)) {
                    // Only open sides, never mist inside stone or a concealed water pipe.
                    for (var direction : net.minecraft.core.Direction.Plane.HORIZONTAL) {
                        BlockPos edge = pos.relative(direction);
                        if (level.hasChunkAt(edge) && level.isEmptyBlock(edge)) {
                            particle = client.particleEngine.createParticle(ParticleTypes.CLOUD,
                                    edge.getX() + .5, edge.getY() + .2, edge.getZ() + .5,
                                    direction.getStepX() * .025, .025, direction.getStepZ() * .025);
                            if (particle != null) particle.scale(.25F);
                            break;
                        }
                    }
                }
            }
            if (particle != null) TRACKED.add(new TrackedParticle(particle, ticks + 100));
        }
        if (ticks % 10 != 0 || TRACKED.size() >= limit) return;
        // Ripples read the same local channel geometry; the server alone applies movement.
        BlockPos column = player.offset(level.random.nextInt(17) - 8, 0, level.random.nextInt(17) - 8);
        if (!level.hasChunkAt(column)) return;
        int surfaceY = level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, column.getX(), column.getZ()) - 1;
        if (Math.abs(surfaceY - player.getY()) > 10) return;
        BlockPos water = new BlockPos(column.getX(), surfaceY, column.getZ());
        var current = de.laugenbreze.dungeonsalive.world.water.WaterCurrentField.sample(level, water);
        if (current.speed() > 0 && level.random.nextDouble() < AmbientConfig.DENSITY.get()) {
            var velocity = current.velocity();
            Particle ripple = client.particleEngine.createParticle(ParticleTypes.FISHING,
                    water.getX() + level.random.nextDouble(), surfaceY + .93, water.getZ() + level.random.nextDouble(),
                    velocity.x, .002, velocity.z);
            if (ripple != null) TRACKED.add(new TrackedParticle(ripple, ticks + 40));
        }
    }

    private record TrackedParticle(Particle particle, long deadline) {}
}
