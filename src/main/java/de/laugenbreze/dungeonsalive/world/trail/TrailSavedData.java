package de.laugenbreze.dungeonsalive.world.trail;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

/** Sparse, capped history. Only explicitly recorded paths may regrow; ordinary dirt paths are never enrolled. */
final class TrailSavedData extends SavedData {
    static final int LIMIT = 32768;
    final Map<Long, FootTraffic> footprints = new HashMap<>();
    final Set<Long> playerGround = new HashSet<>();
    final ArrayDeque<Long> pending = new ArrayDeque<>();
    boolean protectionFull;
    static TrailSavedData get(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(new Factory<>(TrailSavedData::new, TrailSavedData::load, null), "dungeonsalive_trails");
    }
    void put(long position, FootTraffic traffic) {
        if (!footprints.containsKey(position)) {
            if (footprints.size() >= LIMIT) return;
            if (pending.size() >= LIMIT * 2) { pending.clear(); pending.addAll(footprints.keySet()); }
            pending.addLast(position);
        }
        footprints.put(position, traffic); setDirty();
    }
    void protect(long position) {
        footprints.remove(position);
        if (playerGround.size() >= LIMIT) protectionFull = true;
        else playerGround.add(position);
        setDirty();
    }
    void forget(long position) { if (footprints.remove(position) != null) setDirty(); }
    static TrailSavedData load(CompoundTag tag, HolderLookup.Provider registries) {
        var data = new TrailSavedData();
        var entries = tag.getList("Footprints", Tag.TAG_COMPOUND);
        for (int i = 0; i < Math.min(entries.size(), LIMIT); i++) {
            var entry = entries.getCompound(i);
            String original = entry.getString("Original");
            if (!NaturalTrails.originalIds().contains(original)) continue;
            data.put(entry.getLong("Pos"), new FootTraffic(original, entry.getInt("Steps"), entry.getInt("Stage"), entry.getLong("LastStep")));
        }
        long[] protectedPositions = tag.getLongArray("PlayerGround");
        for (int i = 0; i < Math.min(protectedPositions.length, LIMIT); i++) data.playerGround.add(protectedPositions[i]);
        // Conflicting/corrupt saved entries must never regrow protected player ground.
        data.footprints.keySet().removeAll(data.playerGround);
        data.pending.removeIf(data.playerGround::contains);
        data.protectionFull = tag.getBoolean("ProtectionFull") || protectedPositions.length > LIMIT;
        data.setDirty(false);
        return data;
    }
    @Override public CompoundTag save(CompoundTag tag, HolderLookup.Provider registries) {
        var entries = new ListTag();
        footprints.forEach((pos, traffic) -> {
            var entry = new CompoundTag(); entry.putLong("Pos", pos); entry.putString("Original", traffic.original());
            entry.putInt("Steps", traffic.steps()); entry.putInt("Stage", traffic.stage()); entry.putLong("LastStep", traffic.lastStep()); entries.add(entry);
        });
        tag.put("Footprints", entries); tag.putLongArray("PlayerGround", playerGround.stream().mapToLong(Long::longValue).toArray());
        tag.putBoolean("ProtectionFull", protectionFull); return tag;
    }
}
