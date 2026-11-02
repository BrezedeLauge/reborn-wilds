package de.laugenbreze.dungeonsalive.loot;

import com.mojang.serialization.MapCodec;
import de.laugenbreze.dungeonsalive.DungeonsAlive;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

/** Additive server loot generation; no chest-open or inventory-replacement hooks. */
public final class ExplorationLoot {
    private static final DeferredRegister<MapCodec<? extends IGlobalLootModifier>> MODIFIERS =
            DeferredRegister.create(NeoForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, DungeonsAlive.MOD_ID);
    static { MODIFIERS.register("exploration_gear", () -> ExplorationGearModifier.CODEC); }
    private ExplorationLoot() {}
    public static void register(IEventBus bus) { MODIFIERS.register(bus); }
}
