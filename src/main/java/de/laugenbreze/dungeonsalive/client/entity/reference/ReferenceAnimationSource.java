package de.laugenbreze.dungeonsalive.client.entity.reference;

import de.laugenbreze.dungeonsalive.entity.AttackKind;
import de.laugenbreze.dungeonsalive.entity.DungeonMob;
import net.minecraft.world.entity.Mob;
import org.jetbrains.annotations.Nullable;

/**
 * Client adapter for any real Mob, including passive PathfinderMob implementations.
 * Adapters only read synchronized entity state; they must never produce gameplay effects.
 */
public interface ReferenceAnimationSource<T extends Mob> {
    @Nullable AttackKind action(T entity);
    float actionTicks(T entity, float partialTick);
    @Nullable default Gesture gesture(T entity, float partialTick) { return null; }

    /** An explicitly named noncombat clip, such as a server-synchronized greeting or trade gesture. */
    record Gesture(String clip, float elapsedTicks, float durationTicks, float blendInTicks, float blendOutTicks) {
        public Gesture {
            if (clip == null || clip.isBlank()) throw new IllegalArgumentException("Gesture needs a clip name");
            if (!Float.isFinite(elapsedTicks) || !Float.isFinite(durationTicks) || durationTicks <= 0
                    || !Float.isFinite(blendInTicks) || !Float.isFinite(blendOutTicks) || blendInTicks < 0 || blendOutTicks < 0)
                throw new IllegalArgumentException("Gesture has an invalid animation clock");
        }
        public Gesture(String clip, float elapsedTicks, float durationTicks) { this(clip, elapsedTicks, durationTicks, 3, 4); }
    }

    /** Backwards-compatible DungeonMob adapter; other Mob subclasses receive locomotion only. */
    static <T extends Mob> ReferenceAnimationSource<T> compatible() {
        return new ReferenceAnimationSource<>() {
            @Override public AttackKind action(T entity) { return entity instanceof DungeonMob mob ? mob.activeAttack() : null; }
            @Override public float actionTicks(T entity, float partialTick) { return entity instanceof DungeonMob mob ? mob.animationTick(partialTick) : 0; }
        };
    }
    static <T extends Mob> ReferenceAnimationSource<T> passive() {
        return new ReferenceAnimationSource<>() {
            @Override public AttackKind action(T entity) { return null; }
            @Override public float actionTicks(T entity, float partialTick) { return 0; }
        };
    }
}
