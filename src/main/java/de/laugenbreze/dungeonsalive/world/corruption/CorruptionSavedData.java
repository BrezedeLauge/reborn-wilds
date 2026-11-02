package de.laugenbreze.dungeonsalive.world.corruption;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.saveddata.SavedData;
import org.jetbrains.annotations.Nullable;

/** One world-wide bounded front/history; unrecognized data is preserved and never acted upon. */
public final class CorruptionSavedData extends SavedData {
    public static final String DATA_NAME = "dungeonsalive_corruption";
    private static final Factory<CorruptionSavedData> FACTORY = new Factory<>(CorruptionSavedData::new, CorruptionSavedData::load, null);
    public static final class Region {
        public final UUID id;
        public final String dimension;
        public final int x, z;
        public final CorruptionFront front = new CorruptionFront();
        final LinkedHashMap<BlockPos, BlockState> originals = new LinkedHashMap<>();
        Region(UUID id, String dimension, int x, int z) { this.id = id; this.dimension = dimension; this.x = x; this.z = z; }
        public int patches() { return originals.size(); }
        boolean contains(int px, int pz, int radius) {
            double dx = (double)x - px, dz = (double)z - pz; return dx * dx + dz * dz <= radius * radius;
        }
    }
    private record Column(String dimension, int x, int z) {}
    final List<Region> regions = new ArrayList<>();
    private final Set<Column> protectedColumns = new HashSet<>();
    private boolean protectionFull;
    @Nullable private CompoundTag preserved;
    int nextRegion;

    public static CorruptionSavedData get(MinecraftServer server) { return server.overworld().getDataStorage().computeIfAbsent(FACTORY, DATA_NAME); }
    public boolean frozen() { return preserved != null; }
    public boolean growthDisabled() { return frozen() || protectionFull; }
    public boolean protectedAt(String dimension, int x, int z) { return protectionFull || protectedColumns.contains(new Column(dimension, x, z)); }
    public void protect(String dimension, int x, int z) {
        if (frozen()) return;
        if (protectedColumns.size() >= CorruptionFront.MAX_PROTECTED) protectionFull = true;
        else protectedColumns.add(new Column(dimension, x, z));
        setDirty();
    }
    @Nullable public Region nearby(String dimension, int x, int z, int radius) {
        return regions.stream().filter(region -> region.dimension.equals(dimension) && region.contains(x, z, radius)).findFirst().orElse(null);
    }
    @Nullable public Region discover(String dimension, BlockPos position) {
        if (growthDisabled() || regions.size() >= CorruptionFront.MAX_REGIONS
                || nearby(dimension, position.getX(), position.getZ(), CorruptionFront.RADIUS * 2) != null) return null;
        var region = new Region(UUID.randomUUID(), dimension, position.getX(), position.getZ());
        regions.add(region); setDirty(); return region;
    }
    public boolean cleanse(Region region) {
        if (frozen() || !regions.contains(region) || !region.front.cleanse()) return false;
        setDirty(); return true;
    }

    public static CorruptionSavedData load(CompoundTag tag, HolderLookup.Provider lookup) {
        var data = new CorruptionSavedData();
        try {
            if (tag.getInt("Version") != 1) throw new IllegalArgumentException("Unknown corruption format");
            var saved = list(tag, "Regions", CorruptionFront.MAX_REGIONS);
            Set<UUID> ids = new HashSet<>();
            for (int i = 0; i < saved.size(); i++) {
                var entry = saved.getCompound(i);
                if (!entry.hasUUID("Id") || !ids.add(entry.getUUID("Id")) || ResourceLocation.tryParse(entry.getString("Dimension")) == null
                        || !entry.contains("X", Tag.TAG_INT) || !entry.contains("Z", Tag.TAG_INT)
                        || Math.abs((long)entry.getInt("X")) > 30_000_000 || Math.abs((long)entry.getInt("Z")) > 30_000_000)
                    throw new IllegalArgumentException("Invalid corruption anchor");
                var region = new Region(entry.getUUID("Id"), entry.getString("Dimension"), entry.getInt("X"), entry.getInt("Z"));
                region.front.restore(entry.getInt("Cursor"), entry.getInt("Grown"), CorruptionFront.Stage.valueOf(entry.getString("Stage")));
                var patches = list(entry, "Originals", CorruptionFront.MAX_PATCHES);
                for (int j = 0; j < patches.size(); j++) {
                    var patch = patches.getCompound(j);
                    if (!patch.contains("Pos", Tag.TAG_LONG)) throw new IllegalArgumentException("Invalid patch position");
                    BlockPos position = BlockPos.of(patch.getLong("Pos"));
                    if (!patch.getCompound("State").getString("Name").equals("minecraft:grass_block"))
                        throw new IllegalArgumentException("Original is not reserved natural grass");
                    var state = NbtUtils.readBlockState(lookup.lookupOrThrow(Registries.BLOCK), patch.getCompound("State"));
                    if (!state.is(Blocks.GRASS_BLOCK) || !NbtUtils.writeBlockState(state).equals(patch.getCompound("State"))
                            || !region.contains(position.getX(), position.getZ(), CorruptionFront.RADIUS)
                            || region.originals.put(position, state) != null) throw new IllegalArgumentException("Invalid original natural state");
                }
                if (region.patches() > region.front.grown() || region.front.stage() == CorruptionFront.Stage.CLEANSED && region.patches() > 0)
                    throw new IllegalArgumentException("Inconsistent front history");
                if (data.nearby(region.dimension, region.x, region.z, CorruptionFront.RADIUS * 2) != null)
                    throw new IllegalArgumentException("Overlapping corruption anchors");
                data.regions.add(region);
            }
            var columns = list(tag, "Protected", CorruptionFront.MAX_PROTECTED);
            for (int i = 0; i < columns.size(); i++) {
                var entry = columns.getCompound(i);
                if (ResourceLocation.tryParse(entry.getString("Dimension")) == null || !entry.contains("X", Tag.TAG_INT) || !entry.contains("Z", Tag.TAG_INT))
                    throw new IllegalArgumentException("Invalid protected column");
                data.protectedColumns.add(new Column(entry.getString("Dimension"), entry.getInt("X"), entry.getInt("Z")));
            }
            data.protectionFull = tag.getBoolean("ProtectionFull");
        } catch (IllegalArgumentException exception) { data.preserved = tag.copy(); }
        return data;
    }
    private static ListTag list(CompoundTag tag, String key, int maximum) {
        if (!(tag.get(key) instanceof ListTag list) || list.size() > maximum || !list.isEmpty() && list.getElementType() != Tag.TAG_COMPOUND)
            throw new IllegalArgumentException("Invalid corruption list " + key);
        return list;
    }
    @Override public CompoundTag save(CompoundTag tag, HolderLookup.Provider lookup) {
        if (preserved != null) return preserved.copy();
        tag.putInt("Version", 1); tag.putBoolean("ProtectionFull", protectionFull);
        ListTag saved = new ListTag();
        for (var region : regions) {
            var entry = new CompoundTag(); entry.putUUID("Id", region.id); entry.putString("Dimension", region.dimension);
            entry.putInt("X", region.x); entry.putInt("Z", region.z); entry.putInt("Cursor", region.front.cursor());
            entry.putInt("Grown", region.front.grown()); entry.putString("Stage", region.front.stage().name());
            ListTag originals = new ListTag();
            region.originals.forEach((position, state) -> {
                var patch = new CompoundTag(); patch.putLong("Pos", position.asLong()); patch.put("State", NbtUtils.writeBlockState(state)); originals.add(patch);
            });
            entry.put("Originals", originals); saved.add(entry);
        }
        tag.put("Regions", saved); ListTag columns = new ListTag();
        protectedColumns.stream().sorted(java.util.Comparator.comparing(Column::dimension).thenComparingInt(Column::x).thenComparingInt(Column::z)).forEach(column -> {
            var entry = new CompoundTag(); entry.putString("Dimension", column.dimension()); entry.putInt("X", column.x()); entry.putInt("Z", column.z()); columns.add(entry);
        });
        tag.put("Protected", columns); return tag;
    }
}
