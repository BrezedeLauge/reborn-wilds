package de.laugenbreze.dungeonsalive.settlement.inn;

import de.laugenbreze.dungeonsalive.DungeonsAlive;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.saveddata.SavedData;
import org.jetbrains.annotations.Nullable;

/** Separate versioned file; corrupt/unknown records are retained verbatim and cannot authorize more guests. */
public final class InnSavedData extends SavedData {
    public static final String DATA_NAME = DungeonsAlive.MOD_ID + "_inns";
    private static final Factory<InnSavedData> FACTORY = new Factory<>(InnSavedData::new, InnSavedData::load, null);
    private final InnRegistry registry = new InnRegistry();
    @Nullable private CompoundTag preserved;
    public static InnSavedData get(MinecraftServer server) { return server.overworld().getDataStorage().computeIfAbsent(FACTORY, DATA_NAME); }
    public boolean blocked() { return preserved != null || registry.blocked(); }
    public List<InnRegistry.Inn> all() { return blocked() ? List.of() : registry.all(); }
    @Nullable public InnRegistry.Inn find(UUID id) { return blocked() ? null : registry.get(id).orElse(null); }
    public boolean hadArrival(UUID owner) { return !blocked() && registry.hadArrival(owner); }

    public InnRegistry.Change configure(UUID id, UUID owner, String dimension, InnBounds.Point anchor, InnBounds bounds,
                                        InnBounds.Point entrance, InnBounds.Point stock, InnBounds.Point arrival, long expectedRevision) {
        if (blocked()) return new InnRegistry.Change(InnRegistry.Decision.BLOCKED, null);
        var result = registry.configure(id, owner, dimension, anchor, bounds, entrance, stock, arrival, expectedRevision);
        if (result.decision() == InnRegistry.Decision.OK) setDirty();
        return result;
    }
    public boolean reserve(UUID inn, UUID guest, long revision, long now) { return changed(!blocked() && registry.reserveGuest(inn, guest, revision, now)); }
    public boolean advance(UUID inn, UUID guest, InnRegistry.GuestPhase from, InnRegistry.GuestPhase to, long now) {
        return changed(!blocked() && registry.advanceGuest(inn, guest, from, to, now));
    }
    public boolean cancelReservation(UUID inn, UUID guest) { return changed(!blocked() && registry.cancelReservation(inn, guest)); }
    public boolean finishDeparture(UUID inn, UUID guest, long now) { return changed(!blocked() && registry.finishDeparture(inn, guest, now)); }
    public InnRegistry.Decision remove(UUID inn, UUID owner, long revision) {
        if (blocked()) return InnRegistry.Decision.BLOCKED;
        var result = registry.remove(inn, owner, revision);
        if (result == InnRegistry.Decision.OK) setDirty();
        return result;
    }
    public InnRegistry.Decision retire(UUID inn, UUID owner, long revision, long now) {
        if (blocked()) return InnRegistry.Decision.BLOCKED;
        var result = registry.retire(inn, owner, revision, now);
        if (result == InnRegistry.Decision.OK) setDirty();
        return result;
    }
    private boolean changed(boolean changed) { if (changed) setDirty(); return changed; }

    public static InnSavedData load(CompoundTag tag, HolderLookup.Provider provider) {
        var data = new InnSavedData();
        try {
            if (!tag.contains("Version", Tag.TAG_INT) || tag.getInt("Version") != 1
                    || !(tag.get("Inns") instanceof ListTag inns) || inns.size() > InnRegistry.MAX_INNS
                    || !inns.isEmpty() && inns.getElementType() != Tag.TAG_COMPOUND)
                throw new IllegalArgumentException("Unknown or oversized inn history");
            List<InnRegistry.Inn> records = new ArrayList<>();
            for (int i = 0; i < inns.size(); i++) {
                CompoundTag entry = inns.getCompound(i);
                var bounds = entry.getCompound("Bounds");
                InnRegistry.Guest guest = null;
                if (entry.contains("Guest")) {
                    if (!entry.contains("Guest", Tag.TAG_COMPOUND)) throw new IllegalArgumentException("Invalid guest");
                    var g = entry.getCompound("Guest");
                    guest = new InnRegistry.Guest(uuid(g, "Entity"), number(g, "Revision"),
                            InnRegistry.GuestPhase.valueOf(g.getString("Phase")), number(g, "Since"));
                }
                records.add(new InnRegistry.Inn(uuid(entry, "Id"), uuid(entry, "Owner"), entry.getString("Dimension"),
                        point(entry, "Anchor"), new InnBounds(integer(bounds, "MinX"), integer(bounds, "MinY"), integer(bounds, "MinZ"),
                        integer(bounds, "MaxX"), integer(bounds, "MaxY"), integer(bounds, "MaxZ")),
                        point(entry, "Entrance"), point(entry, "Stock"), point(entry, "Arrival"), number(entry, "Revision"),
                        guest, number(entry, "Arrivals"), number(entry, "NextVisitAfter"), bool(entry, "Retired")));
            }
            if (!(tag.get("ArrivalOwners") instanceof ListTag owners) || owners.size() > InnRegistry.MAX_ARRIVAL_OWNERS
                    || !owners.isEmpty() && owners.getElementType() != Tag.TAG_COMPOUND) throw new IllegalArgumentException("Invalid arrival history");
            List<UUID> history = new ArrayList<>();
            for (int i = 0; i < owners.size(); i++) history.add(uuid(owners.getCompound(i), "Id"));
            if (!data.registry.restore(records, history)) throw new IllegalArgumentException("Invalid exclusive inn history");
        } catch (IllegalArgumentException | NullPointerException exception) { data.preserved = tag.copy(); }
        return data;
    }
    private static UUID uuid(CompoundTag tag, String key) {
        if (!tag.hasUUID(key)) throw new IllegalArgumentException("Missing UUID " + key);
        return tag.getUUID(key);
    }
    private static long number(CompoundTag tag, String key) {
        if (!tag.contains(key, Tag.TAG_LONG)) throw new IllegalArgumentException("Missing long " + key);
        return tag.getLong(key);
    }
    private static int integer(CompoundTag tag, String key) {
        if (!tag.contains(key, Tag.TAG_INT)) throw new IllegalArgumentException("Missing int " + key);
        return tag.getInt(key);
    }
    private static boolean bool(CompoundTag tag, String key) {
        if (!tag.contains(key, Tag.TAG_BYTE) || tag.getByte(key) < 0 || tag.getByte(key) > 1)
            throw new IllegalArgumentException("Missing/invalid boolean " + key);
        return tag.getBoolean(key);
    }
    private static InnBounds.Point point(CompoundTag tag, String key) {
        if (!tag.contains(key, Tag.TAG_COMPOUND)) throw new IllegalArgumentException("Missing point " + key);
        var point = tag.getCompound(key);
        return new InnBounds.Point(integer(point, "X"), integer(point, "Y"), integer(point, "Z"));
    }
    @Override public CompoundTag save(CompoundTag tag, HolderLookup.Provider provider) {
        if (preserved != null) return preserved.copy();
        tag.putInt("Version", 1); ListTag records = new ListTag();
        for (var inn : registry.all()) {
            CompoundTag entry = new CompoundTag();
            entry.putUUID("Id", inn.id()); entry.putUUID("Owner", inn.owner()); entry.putString("Dimension", inn.dimension());
            entry.putLong("Revision", inn.revision()); entry.putLong("Arrivals", inn.arrivals()); entry.putLong("NextVisitAfter", inn.nextVisitAfter());
            entry.putBoolean("Retired", inn.retired());
            entry.put("Anchor", point(inn.anchor())); entry.put("Entrance", point(inn.entrance()));
            entry.put("Stock", point(inn.stock())); entry.put("Arrival", point(inn.arrival()));
            var b = inn.bounds(); CompoundTag bounds = new CompoundTag();
            bounds.putInt("MinX", b.minX()); bounds.putInt("MinY", b.minY()); bounds.putInt("MinZ", b.minZ());
            bounds.putInt("MaxX", b.maxX()); bounds.putInt("MaxY", b.maxY()); bounds.putInt("MaxZ", b.maxZ()); entry.put("Bounds", bounds);
            if (inn.guest() != null) {
                var guest = inn.guest(); CompoundTag g = new CompoundTag();
                g.putUUID("Entity", guest.entity()); g.putLong("Revision", guest.revision());
                g.putString("Phase", guest.phase().name()); g.putLong("Since", guest.since()); entry.put("Guest", g);
            }
            records.add(entry);
        }
        tag.put("Inns", records);
        ListTag history = new ListTag();
        for (UUID owner : registry.arrivalOwners()) { CompoundTag entry = new CompoundTag(); entry.putUUID("Id", owner); history.add(entry); }
        tag.put("ArrivalOwners", history); return tag;
    }
    private static CompoundTag point(InnBounds.Point p) {
        CompoundTag tag = new CompoundTag(); tag.putInt("X", p.x()); tag.putInt("Y", p.y()); tag.putInt("Z", p.z()); return tag;
    }
}
