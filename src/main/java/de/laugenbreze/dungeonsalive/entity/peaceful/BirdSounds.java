package de.laugenbreze.dungeonsalive.entity.peaceful;

import de.laugenbreze.dungeonsalive.DungeonsAlive;
import java.util.Map;
import java.util.WeakHashMap;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/** Quiet event-driven calls; aliases reference installed Minecraft sounds, never copied recordings. */
public final class BirdSounds {
    private static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(Registries.SOUND_EVENT, DungeonsAlive.MOD_ID);
    private static final DeferredHolder<SoundEvent, SoundEvent> TOUCAN = call("entity.toucan.contact");
    private static final DeferredHolder<SoundEvent, SoundEvent> GOLDEN_PARROT = call("entity.golden_parrot.contact");
    // Cosmetic throttling only. Weak entity keys cannot retain unloaded worlds or animals.
    private static final Map<DungeonsCreature, Calls> LAST = new WeakHashMap<>();
    private record Calls(long contact, long flight) { }
    private BirdSounds() { }

    private static DeferredHolder<SoundEvent, SoundEvent> call(String id) {
        return SOUNDS.register(id, () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(DungeonsAlive.MOD_ID, id)));
    }
    public static void register(IEventBus bus) { SOUNDS.register(bus); }

    public static void rested(DungeonsCreature bird) {
        if (!eligible(bird) || !bird.birdPerched() || !bird.onGround() || bird.wildlifeLeader() != bird) return;
        SoundEvent sound = switch (bird.profile().id()) {
            case "pet_toucan" -> TOUCAN.get();
            case "pet_golden_parrot" -> GOLDEN_PARROT.get();
            default -> null; // A raven or vulture does not acquire a parrot voice.
        };
        if (sound == null) return;
        long now = bird.level().getGameTime();
        Calls last = LAST.getOrDefault(bird, new Calls(Long.MIN_VALUE, Long.MIN_VALUE));
        if (last.contact() != Long.MIN_VALUE && now >= last.contact() && now - last.contact() < 180) return;
        LAST.put(bird, new Calls(now, last.flight()));
        bird.playSound(sound, .4F, .97F + Math.floorMod(bird.getUUID().getLeastSignificantBits(), 7) * .01F);
    }

    public static void departed(DungeonsCreature bird) {
        if (!eligible(bird) || bird.birdActivity() != BirdRoutineState.Phase.TAKING_OFF) return;
        long now = bird.level().getGameTime();
        Calls last = LAST.getOrDefault(bird, new Calls(Long.MIN_VALUE, Long.MIN_VALUE));
        if (last.flight() != Long.MIN_VALUE && now >= last.flight() && now - last.flight() < 40) return;
        LAST.put(bird, new Calls(last.contact(), now));
        bird.playSound(SoundEvents.PARROT_FLY, .22F, .95F + bird.wildlifeSlot() * .04F);
    }

    private static boolean eligible(DungeonsCreature bird) {
        return !bird.level().isClientSide && bird.isAlive() && !bird.isNoAi() && bird.birdWildlife()
                && bird.ownerId() == null && bird.wildlifeGroupId() != null;
    }
}
