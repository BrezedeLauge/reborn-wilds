package de.laugenbreze.dungeonsalive.world;

import com.mojang.serialization.JsonOps;
import de.laugenbreze.dungeonsalive.DungeonsAlive;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.biome.FixedBiomeSource;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.DensityFunctions;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.presets.WorldPreset;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

/** Actual datapack registry/codecs and Minecraft climate samplers, without generating any terrain. */
@GameTestHolder(DungeonsAlive.MOD_ID)
@PrefixGameTestTemplate(false)
public final class LivingBiomeGameTests {
    private static final Set<String> VARIANTS = Set.of("amber_grove", "luminous_marsh", "ember_crags", "blighted_thicket");
    private LivingBiomeGameTests() {}

    @GameTest(template = "gametest_empty", templateNamespace = DungeonsAlive.MOD_ID, batch = "living_biomes")
    public static void registeredWorldPresetRoundTripsAllDimensionsAndFourRegionalBiomes(GameTestHelper helper) {
        var registries = helper.getLevel().registryAccess();
        WorldPreset preset = preset(registries, "dungeonsalive:living_world");
        var ops = RegistryOps.create(JsonOps.INSTANCE, registries);
        var encoded = WorldPreset.DIRECT_CODEC.encodeStart(ops, preset).result().orElseThrow();
        var decoded = WorldPreset.DIRECT_CODEC.parse(ops, encoded).result().orElseThrow();
        var dimensions = decoded.createWorldDimensions().dimensions();
        helper.assertTrue(dimensions.keySet().equals(Set.of(LevelStem.OVERWORLD, LevelStem.NETHER, LevelStem.END)),
                "Living preset lost vanilla dimensions during real registry-aware codec round-trip");
        BiomeSource source = decoded.overworld().orElseThrow().generator().getBiomeSource();
        helper.assertTrue(source instanceof LivingBiomeSource, "The actual preset did not decode its registered living biome source");
        var possible = source.possibleBiomes();
        for (String name : VARIANTS) helper.assertTrue(possible.contains(biome(registries, name)), "Preset omitted custom biome " + name);
        var vanilla = preset(registries, "minecraft:normal").overworld().orElseThrow().generator().getBiomeSource();
        helper.assertTrue(possible.containsAll(vanilla.possibleBiomes()), "Living preset removed vanilla possible biomes");
        helper.succeed();
    }

    @GameTest(template = "gametest_empty", templateNamespace = DungeonsAlive.MOD_ID, batch = "living_biomes", timeoutTicks = 100)
    public static void realSeededClimateSamplingSurvivesCodecRoundTripAndPreservesUnderground(GameTestHelper helper) {
        var level = helper.getLevel(); var registries = level.registryAccess();
        var generator = (NoiseBasedChunkGenerator) preset(registries, "dungeonsalive:living_world").overworld().orElseThrow().generator();
        BiomeSource source = generator.getBiomeSource();
        var ops = RegistryOps.create(JsonOps.INSTANCE, registries);
        var encoded = BiomeSource.CODEC.encodeStart(ops, source).result().orElseThrow();
        BiomeSource roundTrip = BiomeSource.CODEC.parse(ops, encoded).result().orElseThrow();
        BiomeSource vanilla = preset(registries, "minecraft:normal").overworld().orElseThrow().generator().getBiomeSource();
        var sampler = RandomState.create(generator.generatorSettings().value(), registries.lookupOrThrow(Registries.NOISE), 0x51A7C0DEL).sampler();
        var selected = new HashSet<ResourceLocation>();
        int nativeSelections = 0;
        BlockPos distant = new BlockPos(12_000_000, 80, 12_000_000);
        helper.assertTrue(level.getChunkSource().getChunkNow(distant.getX() >> 4, distant.getZ() >> 4) == null, "Sampling fixture starts in an already loaded distant chunk");
        for (int x = -2048; x <= 2048; x += 128) for (int z = -2048; z <= 2048; z += 128) {
            Holder<Biome> value = source.getNoiseBiome(x, 20, z, sampler);
            helper.assertTrue(value.equals(roundTrip.getNoiseBiome(x, 20, z, sampler)), "Codec round-trip changed seeded climate selection");
            helper.assertTrue(source.possibleBiomes().contains(value), "Climate selected a biome outside the registered possible set");
            helper.assertTrue(source.getNoiseBiome(x, -8, z, sampler).equals(vanilla.getNoiseBiome(x, -8, z, sampler)),
                    "Regional surface replacement altered the vanilla underground biome selection");
            ResourceLocation id = value.unwrapKey().orElseThrow().location();
            if (id.getNamespace().equals(DungeonsAlive.MOD_ID)) selected.add(id); else nativeSelections++;
        }
        helper.assertTrue(!selected.isEmpty() && nativeSelections > 0, "Seeded climate did not exercise both regional and preserved vanilla biomes");
        source.getNoiseBiome(distant.getX() >> 2, 20, distant.getZ() >> 2, sampler);
        helper.assertTrue(level.getChunkSource().getChunkNow(distant.getX() >> 4, distant.getZ() >> 4) == null, "Biome climate sampling generated an unknown chunk");
        helper.succeed();
    }

    @GameTest(template = "gametest_empty", templateNamespace = DungeonsAlive.MOD_ID, batch = "living_biomes")
    public static void nativeClimateSamplerCanReachEachRegionWithoutReplacingUnrelatedBiomeFamilies(GameTestHelper helper) {
        var registries = helper.getLevel().registryAccess();
        List<ResourceKey<Biome>> bases = List.of(Biomes.FOREST, Biomes.SWAMP, Biomes.SAVANNA_PLATEAU, Biomes.DARK_FOREST);
        List<String> targets = List.of("amber_grove", "luminous_marsh", "ember_crags", "blighted_thicket");
        double[] variations = {.7, 0, 0, -.8};
        for (int i = 0; i < bases.size(); i++) {
            Holder<Biome> base = registries.registryOrThrow(Registries.BIOME).getHolderOrThrow(bases.get(i));
            var source = source(registries, base);
            var sampler = climate(variations[i]);
            helper.assertTrue(source.getNoiseBiome(7, 20, -11, sampler).equals(biome(registries, targets.get(i))),
                    "Actual climate sampler could not reach " + targets.get(i));
            helper.assertTrue(source.getNoiseBiome(7, -1, -11, sampler).equals(base), "Regional source replaced an underground base biome");
        }
        Holder<Biome> ocean = registries.registryOrThrow(Registries.BIOME).getHolderOrThrow(Biomes.OCEAN);
        helper.assertTrue(source(registries, ocean).getNoiseBiome(0, 20, 0, climate(.7)).equals(ocean), "Regional source replaced an unrelated ocean biome");
        helper.succeed();
    }

    private static WorldPreset preset(RegistryAccess registries, String id) {
        return registries.registryOrThrow(Registries.WORLD_PRESET).getHolderOrThrow(
                ResourceKey.create(Registries.WORLD_PRESET, ResourceLocation.parse(id))).value();
    }
    private static Holder<Biome> biome(RegistryAccess registries, String id) {
        return registries.registryOrThrow(Registries.BIOME).getHolderOrThrow(
                ResourceKey.create(Registries.BIOME, ResourceLocation.fromNamespaceAndPath(DungeonsAlive.MOD_ID, id)));
    }
    private static LivingBiomeSource source(RegistryAccess registries, Holder<Biome> base) {
        return new LivingBiomeSource(new FixedBiomeSource(base), biome(registries, "amber_grove"), biome(registries, "luminous_marsh"),
                biome(registries, "ember_crags"), Optional.of(biome(registries, "blighted_thicket")));
    }
    private static Climate.Sampler climate(double variation) {
        var zero = DensityFunctions.constant(0);
        return new Climate.Sampler(zero, zero, zero, DensityFunctions.constant(.2), zero, DensityFunctions.constant(variation), List.of());
    }
}
