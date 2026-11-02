package de.laugenbreze.dungeonsalive.settlement.inn;

import de.laugenbreze.dungeonsalive.DungeonsAlive;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.EntityLeaveLevelEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import de.laugenbreze.dungeonsalive.entity.peaceful.DungeonsMerchant;

public final class InnFurnitureContent {
    private static final DeferredRegister<EntityType<?>> TYPES = DeferredRegister.create(Registries.ENTITY_TYPE, DungeonsAlive.MOD_ID);
    public static final DeferredHolder<EntityType<?>, EntityType<InnSeatEntity>> SEAT = TYPES.register("inn_seat",
            () -> EntityType.Builder.<InnSeatEntity>of(InnSeatEntity::new, MobCategory.MISC).sized(.01F, .01F)
                    .clientTrackingRange(8).updateInterval(20).build("dungeonsalive:inn_seat"));
    public static final FurnitureReservations FURNITURE = new FurnitureReservations();
    private InnFurnitureContent() { }
    public static void register(IEventBus bus) { TYPES.register(bus); NeoForge.EVENT_BUS.addListener(InnFurnitureContent::removed); }
    private static void removed(EntityLeaveLevelEvent event) {
        if (!(event.getLevel() instanceof ServerLevel level) || !(event.getEntity() instanceof DungeonsMerchant guest)
                || guest.getRemovalReason() != Entity.RemovalReason.KILLED && guest.getRemovalReason() != Entity.RemovalReason.DISCARDED) return;
        FURNITURE.releaseTerminated(level, guest);
    }
}
