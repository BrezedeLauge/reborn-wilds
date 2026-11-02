package de.laugenbreze.dungeonsalive.client.entity.reference;

import de.laugenbreze.dungeonsalive.entity.AttackKind;
import de.laugenbreze.dungeonsalive.entity.peaceful.DungeonsCreature;

/** Reads synchronized rescue state; the renderer never frees or claims a civilian. */
public final class CivilianAnimationSource implements ReferenceAnimationSource<DungeonsCreature> {
    public static final CivilianAnimationSource INSTANCE = new CivilianAnimationSource();
    private CivilianAnimationSource() {}
    @Override public AttackKind action(DungeonsCreature mob) { return null; }
    @Override public float actionTicks(DungeonsCreature mob, float partialTick) { return 0; }
    @Override public Gesture gesture(DungeonsCreature mob, float partialTick) {
        if (!mob.civilian()) return null;
        if (!mob.rescued()) return new Gesture("CAPTIVE", (mob.tickCount + partialTick) % 80, 80, 0, 0);
        float elapsed = mob.rescueGestureTicks(partialTick);
        return elapsed >= 0 && elapsed < 40 ? new Gesture("FREED", elapsed, 40, 0, 4) : null;
    }
}
