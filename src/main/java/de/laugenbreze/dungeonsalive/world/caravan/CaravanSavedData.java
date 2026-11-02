package de.laugenbreze.dungeonsalive.world.caravan;

import java.util.HashSet;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

final class CaravanSavedData extends SavedData {
    final CaravanLedger ledger = new CaravanLedger();
    final java.util.Map<java.util.UUID, net.minecraft.core.BlockPos> origins = new java.util.HashMap<>();
    long nextAttempt;

    static CaravanSavedData get(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(new Factory<>(CaravanSavedData::new, CaravanSavedData::load, null), "dungeonsalive_caravans");
    }
    private static CaravanSavedData load(CompoundTag tag, HolderLookup.Provider registries) {
        var result = new CaravanSavedData();
        result.nextAttempt = tag.getLong("NextAttempt");
        var groups = tag.getList("Groups", Tag.TAG_COMPOUND);
        for (int index = 0; index < Math.min(groups.size(), CaravanLedger.MAX_GROUPS); index++) {
            var group = groups.getCompound(index);
            if (!group.hasUUID("Id")) continue;
            var members = group.getList("Members", Tag.TAG_COMPOUND);
            var ids = new HashSet<java.util.UUID>();
            for (int member = 0; member < Math.min(members.size(), CaravanLedger.MAX_MEMBERS); member++) {
                if (members.getCompound(member).hasUUID("Id")) ids.add(members.getCompound(member).getUUID("Id"));
            }
            if (result.ledger.reserve(group.getUUID("Id"), ids) && group.contains("Origin", Tag.TAG_LONG))
                result.origins.put(group.getUUID("Id"), net.minecraft.core.BlockPos.of(group.getLong("Origin")));
        }
        return result;
    }
    @Override public CompoundTag save(CompoundTag tag, HolderLookup.Provider registries) {
        tag.putLong("NextAttempt", nextAttempt);
        var groups = new ListTag();
        ledger.snapshot().forEach((id, members) -> {
            var group = new CompoundTag();
            group.putUUID("Id", id);
            if (origins.containsKey(id)) group.putLong("Origin", origins.get(id).asLong());
            var ids = new ListTag();
            members.stream().sorted().forEach(member -> { var value = new CompoundTag(); value.putUUID("Id", member); ids.add(value); });
            group.put("Members", ids);
            groups.add(group);
        });
        tag.put("Groups", groups);
        return tag;
    }
    void release(java.util.UUID group, java.util.UUID member) {
        if (ledger.remove(group, member)) {
            if (ledger.members(group).isEmpty()) origins.remove(group);
            setDirty();
        }
    }
}
