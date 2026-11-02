package de.laugenbreze.dungeonsalive.entity;

import de.laugenbreze.dungeonsalive.DungeonsAlive;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public final class ModEntities {
    private static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, DungeonsAlive.MOD_ID);
    private static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(DungeonsAlive.MOD_ID);
    public static final Map<String, DeferredHolder<EntityType<?>, EntityType<DungeonMob>>> TYPES;
    public static final Map<String, DeferredItem<DeferredSpawnEggItem>> EGGS;
    public static final DeferredHolder<EntityType<?>, EntityType<SoulBolt>> SOUL_BOLT = ENTITIES.register("soul_bolt", () ->
            EntityType.Builder.<SoulBolt>of(SoulBolt::new, MobCategory.MISC).sized(.3f, .3f)
                    .clientTrackingRange(8).updateInterval(1).build("dungeonsalive:soul_bolt"));
    static {
        var types = new LinkedHashMap<String, DeferredHolder<EntityType<?>, EntityType<DungeonMob>>>();
        var eggs = new LinkedHashMap<String, DeferredItem<DeferredSpawnEggItem>>();
        for (MobProfile profile : MobProfiles.all()) {
            var type = ENTITIES.register(profile.id(), () -> {
                var builder = EntityType.Builder.<DungeonMob>of(
                                (entityType, level) -> new DungeonMob(entityType, level, profile), MobCategory.MONSTER)
                        .sized(profile.width(), profile.height()).clientTrackingRange(10).updateInterval(2);
                if (profile.family() == MobProfile.Family.WILDFIRE || profile.id().equals("flame_construct")) builder.fireImmune();
                return builder.build(DungeonsAlive.MOD_ID + ":" + profile.id());
            });
            types.put(profile.id(), type);
            eggs.put(profile.id(), ITEMS.register(profile.id() + "_spawn_egg", () ->
                    new DeferredSpawnEggItem(type, profile.color(), profile.accent(), new Item.Properties())));
        }
        TYPES = Collections.unmodifiableMap(types);
        EGGS = Collections.unmodifiableMap(eggs);
    }
    private ModEntities() {}
    public static EntityType<DungeonMob> type(String id) {
        var holder = TYPES.get(id);
        if (holder == null) throw new IllegalArgumentException("Unknown DungeonMob type: " + id);
        return holder.get();
    }
    public static void register(IEventBus bus) {
        ENTITIES.register(bus);
        ITEMS.register(bus);
        bus.addListener(ModEntities::attributes);
        bus.addListener(ModEntities::spawnPlacements);
        bus.addListener(ModEntities::creative);
    }
    private static void attributes(EntityAttributeCreationEvent event) {
        for (MobProfile profile : MobProfiles.all()) event.put(type(profile.id()), DungeonMob.createAttributes(profile).build());
    }
    private static void spawnPlacements(RegisterSpawnPlacementsEvent event) {
        NaturalMobSpawning.register(event);
    }
    private static void creative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.SPAWN_EGGS) EGGS.values().forEach(event::accept);
    }
}
