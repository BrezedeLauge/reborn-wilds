package de.laugenbreze.dungeonsalive.ambient;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/** Registry metadata only. This class is safe to load on a dedicated server. */
public final class AmbientParticles {
    private static final DeferredRegister<ParticleType<?>> PARTICLES =
            DeferredRegister.create(BuiltInRegistries.PARTICLE_TYPE, "dungeonsalive");

    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> BUTTERFLY =
            PARTICLES.register("butterfly", () -> new SimpleParticleType(false));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> FIREFLY =
            PARTICLES.register("firefly", () -> new SimpleParticleType(false));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> SPORE =
            PARTICLES.register("spore", () -> new SimpleParticleType(false));

    private AmbientParticles() {}

    public static void register(IEventBus modBus) {
        PARTICLES.register(modBus);
    }
}
