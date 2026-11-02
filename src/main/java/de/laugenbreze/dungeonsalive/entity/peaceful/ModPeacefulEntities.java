package de.laugenbreze.dungeonsalive.entity.peaceful;

import de.laugenbreze.dungeonsalive.DungeonsAlive;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.*;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.registries.*;
import java.util.*;

public final class ModPeacefulEntities {
    private static final DeferredRegister<EntityType<?>> ENTITIES=DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE,DungeonsAlive.MOD_ID);
    private static final DeferredRegister.Items ITEMS=DeferredRegister.createItems(DungeonsAlive.MOD_ID);
    private static final Map<String,DeferredHolder<EntityType<?>,EntityType<DungeonsCreature>>> CREATURES=new LinkedHashMap<>();
    private static final Map<String,DeferredHolder<EntityType<?>,EntityType<DungeonsMerchant>>> MERCHANTS=new LinkedHashMap<>();
    private static final List<DeferredItem<DeferredSpawnEggItem>> EGGS=new ArrayList<>();
    static {
        for(var p:PeacefulProfiles.all()){
            if(p.kind()==PeacefulProfiles.Kind.MERCHANT){
                var type=ENTITIES.register(p.id(),()->EntityType.Builder.<DungeonsMerchant>of((t,l)->new DungeonsMerchant(t,l,p),MobCategory.CREATURE)
                        .sized(p.width(),p.height()).clientTrackingRange(10).updateInterval(2).build(DungeonsAlive.MOD_ID+":"+p.id()));
                MERCHANTS.put(p.id(),type);
                EGGS.add(ITEMS.register(p.id()+"_spawn_egg",()->new DeferredSpawnEggItem(type,p.color(),p.accent(),new Item.Properties())));
            }else{
                var category=p.aquatic()?MobCategory.WATER_CREATURE:MobCategory.CREATURE;
                var type=ENTITIES.register(p.id(),()->EntityType.Builder.<DungeonsCreature>of((t,l)->new DungeonsCreature(t,l,p),category)
                        .sized(p.width(),p.height()).clientTrackingRange(10).updateInterval(2).build(DungeonsAlive.MOD_ID+":"+p.id()));
                CREATURES.put(p.id(),type);
                EGGS.add(ITEMS.register(p.id()+"_spawn_egg",()->new DeferredSpawnEggItem(type,p.color(),p.accent(),new Item.Properties())));
            }
        }
    }
    public static EntityType<DungeonsCreature> creatureType(String id){var h=CREATURES.get(id);if(h==null)throw new IllegalArgumentException(id);return h.get();}
    public static EntityType<DungeonsMerchant> merchantType(String id){var h=MERCHANTS.get(id);if(h==null)throw new IllegalArgumentException(id);return h.get();}
    public static void register(IEventBus bus){ENTITIES.register(bus);ITEMS.register(bus);bus.addListener(ModPeacefulEntities::attributes);bus.addListener(ModPeacefulEntities::creative);bus.addListener(WildlifeSpawning::register);}
    private static void attributes(EntityAttributeCreationEvent event){
        for(var p:PeacefulProfiles.all())if(p.kind()==PeacefulProfiles.Kind.MERCHANT)event.put(merchantType(p.id()),DungeonsMerchant.createAttributes().build());
        else event.put(creatureType(p.id()),DungeonsCreature.createAttributes(p).build());
    }
    private static void creative(BuildCreativeModeTabContentsEvent event){if(event.getTabKey()==CreativeModeTabs.SPAWN_EGGS)EGGS.forEach(event::accept);}
    private ModPeacefulEntities(){}
}
