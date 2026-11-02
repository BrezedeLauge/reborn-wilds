package de.laugenbreze.dungeonsalive.settlement;

import de.laugenbreze.dungeonsalive.DungeonsAlive;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.saveddata.SavedData;
import org.jetbrains.annotations.Nullable;

/** All dimensions share authoritative anchors and reward claims; unloaded residents are never respawned. */
public final class SettlementSavedData extends SavedData {
    public static final String DATA_NAME = DungeonsAlive.MOD_ID + "_settlements";
    private static final Factory<SettlementSavedData> FACTORY = new Factory<>(SettlementSavedData::new, SettlementSavedData::load, null);
    private final SettlementRegistry registry = new SettlementRegistry();
    @Nullable private CompoundTag preserved;

    public static SettlementSavedData get(MinecraftServer server) { return server.overworld().getDataStorage().computeIfAbsent(FACTORY, DATA_NAME); }
    public SettlementRegistry.Decision check(UUID owner, String dimension, int x, int z) {
        return preserved != null ? SettlementRegistry.Decision.BLOCKED : registry.check(owner, dimension, x, z);
    }
    public boolean add(SettlementRegistry.Anchor anchor) {
        if (preserved != null || !registry.add(anchor)) return false;
        setDirty(); return true;
    }
    public boolean claimReward(UUID boss) {
        if (preserved != null || !registry.claimReward(boss)) return false;
        setDirty(); return true;
    }
    public List<SettlementRegistry.Anchor> anchors() { return registry.anchors(); }

    public static SettlementSavedData load(CompoundTag tag, HolderLookup.Provider provider) {
        var data = new SettlementSavedData();
        try {
            if (tag.getInt("Version") != 1) throw new IllegalArgumentException("Unknown settlement format");
            var saved = list(tag, "Anchors", SettlementRegistry.MAX_SETTLEMENTS);
            var claimed = list(tag, "Rewards", SettlementRegistry.MAX_REWARDS);
            List<SettlementRegistry.Anchor> anchors = new ArrayList<>();
            List<UUID> claims = new ArrayList<>();
            for (int i = 0; i < saved.size(); i++) {
                var entry = saved.getCompound(i);
                if (!entry.hasUUID("Id") || !entry.hasUUID("Owner") || ResourceLocation.tryParse(entry.getString("Dimension")) == null
                        || !entry.contains("X", Tag.TAG_INT) || !entry.contains("Y", Tag.TAG_INT) || !entry.contains("Z", Tag.TAG_INT))
                    throw new IllegalArgumentException("Invalid settlement anchor");
                var people = list(entry, "Residents", 2);
                List<UUID> residents = new ArrayList<>();
                for (int j = 0; j < people.size(); j++) {
                    if (!people.getCompound(j).hasUUID("Id")) throw new IllegalArgumentException("Invalid resident");
                    residents.add(people.getCompound(j).getUUID("Id"));
                }
                anchors.add(new SettlementRegistry.Anchor(entry.getUUID("Id"), entry.getUUID("Owner"), entry.getString("Dimension"),
                        entry.getInt("X"), entry.getInt("Y"), entry.getInt("Z"), residents));
            }
            for (int i = 0; i < claimed.size(); i++) {
                if (!claimed.getCompound(i).hasUUID("Id")) throw new IllegalArgumentException("Invalid boss claim");
                claims.add(claimed.getCompound(i).getUUID("Id"));
            }
            if (!data.registry.restore(anchors, claims)) throw new IllegalArgumentException("Invalid bounded settlement history");
        } catch (IllegalArgumentException exception) { data.preserved = tag.copy(); }
        return data;
    }
    private static ListTag list(CompoundTag tag, String key, int maximum) {
        if (!(tag.get(key) instanceof ListTag list) || list.size() > maximum || !list.isEmpty() && list.getElementType() != Tag.TAG_COMPOUND)
            throw new IllegalArgumentException("Invalid settlement list " + key);
        return list;
    }
    @Override public CompoundTag save(CompoundTag tag, HolderLookup.Provider provider) {
        if (preserved != null) return preserved.copy();
        tag.putInt("Version", 1);
        ListTag anchors = new ListTag();
        for (var anchor : registry.anchors()) {
            CompoundTag entry = new CompoundTag();
            entry.putUUID("Id", anchor.id()); entry.putUUID("Owner", anchor.owner()); entry.putString("Dimension", anchor.dimension());
            entry.putInt("X", anchor.x()); entry.putInt("Y", anchor.y()); entry.putInt("Z", anchor.z());
            entry.put("Residents", ids(anchor.residents())); anchors.add(entry);
        }
        tag.put("Anchors", anchors); tag.put("Rewards", ids(registry.rewardClaims())); return tag;
    }
    private static ListTag ids(List<UUID> identities) {
        ListTag result = new ListTag();
        for (var id : identities) { CompoundTag entry = new CompoundTag(); entry.putUUID("Id", id); result.add(entry); }
        return result;
    }
}
