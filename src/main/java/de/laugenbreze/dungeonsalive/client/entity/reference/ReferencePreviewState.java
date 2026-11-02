package de.laugenbreze.dungeonsalive.client.entity.reference;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.WeakHashMap;

/** Local guide previews only. Real client-world entities can never acquire a fabricated gesture. */
public final class ReferencePreviewState {
    private static final Map<Mob, ReferenceAnimationSource.Gesture> gestures = new WeakHashMap<>();
    private ReferencePreviewState() {}

    public static void setGesture(Mob entity, String clip, float elapsedTicks, float durationTicks) {
        if (!isLocalPreview(entity)) throw new IllegalArgumentException("Gesture preview requires a detached client-only entity");
        gestures.put(entity, new ReferenceAnimationSource.Gesture(clip, elapsedTicks, durationTicks));
    }

    public static @Nullable ReferenceAnimationSource.Gesture gesture(Mob entity, float partialTick) {
        if (!isLocalPreview(entity)) return null;
        var value = gestures.get(entity);
        return value == null ? null : new ReferenceAnimationSource.Gesture(value.clip(),
                value.elapsedTicks() + Mth.clamp(partialTick, 0, 1), value.durationTicks(), value.blendInTicks(), value.blendOutTicks());
    }

    public static void clear(Mob entity) { gestures.remove(entity); }

    static boolean isLocalPreview(Mob entity) {
        return entity.level().isClientSide && entity.level().getEntity(entity.getId()) != entity;
    }
}
