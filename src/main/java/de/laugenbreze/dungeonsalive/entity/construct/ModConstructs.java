package de.laugenbreze.dungeonsalive.entity.construct;

import de.laugenbreze.dungeonsalive.DungeonsAlive;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/** Independent registration hook: no ordinary hostile profile, egg, loot or natural spawn. */
public final class ModConstructs {
    private static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(Registries.ENTITY_TYPE, DungeonsAlive.MOD_ID);
    public static final Map<ConstructKind, DeferredHolder<EntityType<?>, EntityType<DungeonConstruct>>> TYPES;
    public static final DeferredHolder<EntityType<?>, EntityType<DungeonTrident>> TRIDENT = ENTITIES.register("dungeon_trident", () ->
            EntityType.Builder.<DungeonTrident>of(DungeonTrident::new, MobCategory.MISC)
                    .sized(.5f, .5f).clientTrackingRange(8).updateInterval(1).build(DungeonsAlive.MOD_ID + ":dungeon_trident"));
    static {
        var types = new EnumMap<ConstructKind, DeferredHolder<EntityType<?>, EntityType<DungeonConstruct>>>(ConstructKind.class);
        for (ConstructKind kind : ConstructKind.values()) {
            types.put(kind, ENTITIES.register(kind.id(), () ->
                    EntityType.Builder.<DungeonConstruct>of((type, level) -> new DungeonConstruct(type, level, kind), MobCategory.MISC)
                            .sized(kind.width(), kind.height()).fireImmune().clientTrackingRange(10).updateInterval(1)
                            .build(DungeonsAlive.MOD_ID + ":" + kind.id())));
        }
        TYPES = Collections.unmodifiableMap(types);
    }
    private ModConstructs() {}
    public static EntityType<DungeonConstruct> type(ConstructKind kind) { return TYPES.get(kind).get(); }
    public static void register(IEventBus bus) {
        ENTITIES.register(bus);
        bus.addListener(ModConstructs::attributes);
    }
    private static void attributes(EntityAttributeCreationEvent event) {
        for (ConstructKind kind : ConstructKind.values()) event.put(type(kind), DungeonConstruct.createAttributes(kind).build());
    }
}
