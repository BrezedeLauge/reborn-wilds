package de.laugenbreze.dungeonsalive.entity.peaceful;

import de.laugenbreze.dungeonsalive.DungeonsAlive;
import java.util.ArrayList;
import java.util.UUID;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.saveddata.SavedData;
import org.jetbrains.annotations.Nullable;

/** One logical-server claim set shared across dimensions; never resolves entities or chunks. */
public final class PiggyBankSavedData extends SavedData {
    private static final Factory<PiggyBankSavedData> FACTORY = new Factory<>(PiggyBankSavedData::new, PiggyBankSavedData::load, null);
    private final PiggyBankClaims claims = new PiggyBankClaims();
    @Nullable private CompoundTag preserved;

    public static PiggyBankSavedData get(MinecraftServer server) {
        return server.overworld().getDataStorage().computeIfAbsent(FACTORY, DungeonsAlive.MOD_ID + "_piggy_bank_claims");
    }
    public boolean claim(UUID identity) {
        if (preserved != null || !claims.claim(identity)) return false;
        setDirty(); return true;
    }
    public static PiggyBankSavedData load(CompoundTag tag, HolderLookup.Provider registries) {
        var data = new PiggyBankSavedData();
        Tag raw = tag.get("Claimed");
        if (tag.getInt("Version") != 1 || !(raw instanceof ListTag saved)
                || !saved.isEmpty() && saved.getElementType() != Tag.TAG_COMPOUND || saved.size() > PiggyBankClaims.MAX_CLAIMS) {
            data.preserved = tag.copy(); return data;
        }
        var ids = new ArrayList<UUID>();
        for (int i = 0; i < saved.size(); i++) {
            CompoundTag entry = saved.getCompound(i);
            if (!entry.hasUUID("Id")) { data.preserved = tag.copy(); return data; }
            ids.add(entry.getUUID("Id"));
        }
        data.claims.restore(ids);
        return data;
    }
    @Override public CompoundTag save(CompoundTag tag, HolderLookup.Provider registries) {
        if (preserved != null) return preserved.copy();
        tag.putInt("Version", 1);
        ListTag saved = new ListTag();
        for (UUID identity : claims.snapshot()) {
            CompoundTag entry = new CompoundTag(); entry.putUUID("Id", identity); saved.add(entry);
        }
        tag.put("Claimed", saved);
        return tag;
    }
}
