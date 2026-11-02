package de.laugenbreze.dungeonsalive.entity.peaceful;

import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

/** An individual bird's actual landing progress, retained alongside its existing group identity. */
public final class BirdRoutineState {
    public enum Phase { FLYING, APPROACHING, LANDING, PERCHED, TAKING_OFF, FLEEING }
    Phase phase = Phase.FLYING;
    @Nullable BlockPos perch;
    double surfaceY;
    long restedSince;
    long landingUntil;
    long approachUntil;
    long resumeAfter;
    long searchAfter;
    long shuffledCycle = Long.MIN_VALUE;
    long nextLook;
    boolean guiding;
    @Nullable BlockPos avoidedPerch;
    long avoidUntil;
    int routeFailures;

    public Phase phase() { return phase; }
    @Nullable public BlockPos perch() { return perch; }
    Vec3 feet() { return new Vec3(perch.getX() + .5, surfaceY, perch.getZ() + .5); }

    void clearPerch() { perch = null; guiding = false; restedSince = 0; landingUntil = 0; approachUntil = 0; routeFailures = 0; }
    void clear() {
        clearPerch(); phase = Phase.FLYING; resumeAfter = 0; searchAfter = 0;
        shuffledCycle = Long.MIN_VALUE; nextLook = 0;
        avoidedPerch = null; avoidUntil = 0;
    }
    void save(CompoundTag target, UUID group, String dimension) {
        CompoundTag tag = new CompoundTag();
        tag.putUUID("Group", group); tag.putString("Dimension", dimension); tag.putString("Phase", phase.name());
        if (perch != null) { tag.putLong("Perch", perch.asLong()); tag.putDouble("SurfaceY", surfaceY); }
        tag.putLong("RestedSince", restedSince); tag.putLong("LandingUntil", landingUntil);
        tag.putLong("ApproachUntil", approachUntil);
        if (avoidedPerch != null) { tag.putLong("AvoidedPerch", avoidedPerch.asLong()); tag.putLong("AvoidUntil", avoidUntil); }
        tag.putLong("ResumeAfter", resumeAfter); tag.putLong("SearchAfter", searchAfter); tag.putLong("ShuffledCycle", shuffledCycle);
        target.put("DungeonsBirdRoutine", tag);
    }
    void load(CompoundTag source, UUID group, String dimension, long now) {
        clear();
        CompoundTag tag = source.getCompound("DungeonsBirdRoutine");
        if (!tag.hasUUID("Group") || !group.equals(tag.getUUID("Group")) || !dimension.equals(tag.getString("Dimension"))) return;
        try { phase = Phase.valueOf(tag.getString("Phase")); } catch (IllegalArgumentException ignored) { phase = Phase.FLYING; }
        if (tag.contains("Perch", Tag.TAG_LONG) && Double.isFinite(tag.getDouble("SurfaceY"))) {
            perch = BlockPos.of(tag.getLong("Perch")); surfaceY = tag.getDouble("SurfaceY");
            if (surfaceY <= perch.getY() || surfaceY > perch.getY() + 1) clearPerch();
        }
        restedSince = Math.clamp(tag.getLong("RestedSince"), now - BirdPerchCycle.CYCLE_TICKS, now);
        landingUntil = Math.clamp(tag.getLong("LandingUntil"), now, now + 50);
        approachUntil = Math.clamp(tag.getLong("ApproachUntil"), now, now + 240);
        if (tag.contains("AvoidedPerch", Tag.TAG_LONG)) {
            avoidedPerch = BlockPos.of(tag.getLong("AvoidedPerch")); avoidUntil = Math.clamp(tag.getLong("AvoidUntil"), now, now + 240);
        }
        resumeAfter = Math.clamp(tag.getLong("ResumeAfter"), now, now + 180);
        searchAfter = Math.clamp(tag.getLong("SearchAfter"), now, now + 80);
        shuffledCycle = tag.getLong("ShuffledCycle");
        if (perch == null && phase != Phase.FLEEING) phase = Phase.FLYING;
        // Nothing read from NBT authorizes a landing: the AI rechecks actual loaded
        // support, body collision and position before publishing birdPerched.
    }
}
