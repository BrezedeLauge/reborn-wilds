package de.laugenbreze.dungeonsalive.entity.peaceful;

import de.laugenbreze.dungeonsalive.DungeonsAlive;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/** Original synthetic contact calls; the server chooses when a real animal calls. */
public final class DuckSounds {
    private static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(Registries.SOUND_EVENT, DungeonsAlive.MOD_ID);
    public static final DeferredHolder<SoundEvent, SoundEvent> CONTACT = SOUNDS.register("entity.duck.contact",
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(DungeonsAlive.MOD_ID, "entity.duck.contact")));

    public static void register(IEventBus bus) { SOUNDS.register(bus); }
    private DuckSounds() {}
}
