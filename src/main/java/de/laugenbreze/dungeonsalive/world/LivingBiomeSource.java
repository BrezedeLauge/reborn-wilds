package de.laugenbreze.dungeonsalive.world;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import de.laugenbreze.dungeonsalive.DungeonsAlive;
import java.util.stream.Stream;
import java.util.Optional;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.Climate;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

/** Opt-in world preset: preserve the vanilla climate layout, adding coherent regional variants. */
public final class LivingBiomeSource extends BiomeSource {
    public static final MapCodec<LivingBiomeSource> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            BiomeSource.CODEC.fieldOf("base").forGetter(source -> source.base),
            Biome.CODEC.fieldOf("amber_grove").forGetter(source -> source.amber),
            Biome.CODEC.fieldOf("luminous_marsh").forGetter(source -> source.marsh),
            Biome.CODEC.fieldOf("ember_crags").forGetter(source -> source.crags),
            Biome.CODEC.optionalFieldOf("blighted_thicket").forGetter(source -> source.blight)
    ).apply(instance, LivingBiomeSource::new));
    private static final DeferredRegister<MapCodec<? extends BiomeSource>> TYPES = DeferredRegister.create(Registries.BIOME_SOURCE, DungeonsAlive.MOD_ID);
    static { TYPES.register("living_overworld", () -> CODEC); }
    private final BiomeSource base;
    private final Holder<Biome> amber, marsh, crags;
    private final Optional<Holder<Biome>> blight;
    public LivingBiomeSource(BiomeSource base, Holder<Biome> amber, Holder<Biome> marsh, Holder<Biome> crags, Optional<Holder<Biome>> blight) {
        this.base = base; this.amber = amber; this.marsh = marsh; this.crags = crags;
        this.blight = blight;
    }
    public static void register(IEventBus bus) { TYPES.register(bus); }
    @Override protected MapCodec<? extends BiomeSource> codec() { return CODEC; }
    @Override protected Stream<Holder<Biome>> collectPossibleBiomes() {
        return Stream.concat(base.possibleBiomes().stream(), Stream.concat(Stream.of(amber, marsh, crags), blight.stream()));
    }
    @Override public Holder<Biome> getNoiseBiome(int x, int y, int z, Climate.Sampler sampler) {
        Holder<Biome> original = base.getNoiseBiome(x, y, z, sampler);
        if (y < 0) return original;
        // Climate noise is seed-driven and continuous; no rectangular random biome patches.
        var climate = sampler.sample(x, y, z);
        float variation = Climate.unquantizeCoord(climate.weirdness());
        if (blight.isPresent() && (original.is(Biomes.DARK_FOREST) || original.is(Biomes.FOREST))
                && variation < -.55F && Climate.unquantizeCoord(climate.erosion()) > -.1F) return blight.get();
        if ((original.is(Biomes.FOREST) || original.is(Biomes.BIRCH_FOREST) || original.is(Biomes.FLOWER_FOREST)) && variation > .05F) return amber;
        if ((original.is(Biomes.SWAMP) || original.is(Biomes.MANGROVE_SWAMP)) && variation > -.25F) return marsh;
        if ((original.is(Biomes.SAVANNA_PLATEAU) || original.is(Biomes.WINDSWEPT_SAVANNA)
                || original.is(Biomes.WOODED_BADLANDS) || original.is(Biomes.BADLANDS)) && variation < .25F) return crags;
        return original;
    }
}
