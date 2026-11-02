package de.laugenbreze.dungeonsalive.entity.peaceful;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

/** Only recent regional cooldowns are stored; no references to players or unloaded entities. */
final class HabitatPopulationSavedData extends SavedData {
    final HabitatPopulationBudget budget = new HabitatPopulationBudget();

    static HabitatPopulationSavedData get(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(new Factory<>(HabitatPopulationSavedData::new,
                HabitatPopulationSavedData::load, null), "dungeonsalive_habitat_population");
    }

    private static HabitatPopulationSavedData load(CompoundTag tag, HolderLookup.Provider registries) {
        var data = new HabitatPopulationSavedData();
        var regions = tag.getList("Regions", Tag.TAG_COMPOUND);
        for (int index = 0; index < Math.min(regions.size(), HabitatPopulationBudget.MAX_REGIONS); index++) {
            var region = regions.getCompound(index);
            if (region.contains("Cell", Tag.TAG_LONG) && region.contains("Until", Tag.TAG_LONG))
                data.budget.restore(region.getLong("Cell"), region.getLong("Until"));
        }
        return data;
    }

    @Override public CompoundTag save(CompoundTag tag, HolderLookup.Provider registries) {
        var regions = new ListTag();
        budget.snapshot().entrySet().stream().sorted(java.util.Map.Entry.comparingByKey()).forEach(entry -> {
            var region = new CompoundTag();
            region.putLong("Cell", entry.getKey());
            region.putLong("Until", entry.getValue());
            regions.add(region);
        });
        tag.put("Regions", regions);
        return tag;
    }
}
