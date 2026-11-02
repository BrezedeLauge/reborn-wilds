package de.laugenbreze.dungeonsalive.settlement.inn;

import de.laugenbreze.dungeonsalive.entity.peaceful.DungeonsMerchant;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

/** One saved, invisible seat attachment for one assigned inn guest. Never a decorative passenger spawner. */
public final class InnSeatEntity extends Entity {
    @Nullable private InnFurniture.Lease lease;
    public InnSeatEntity(EntityType<? extends InnSeatEntity> type, Level level) {
        super(type, level); noPhysics = true; setNoGravity(true); setInvulnerable(true);
    }
    void configure(InnFurniture.Lease lease) {
        if (this.lease != null || lease.kind() != InnFurniture.Kind.SEAT) throw new IllegalStateException("Seat already bound or wrong use");
        this.lease = lease;
        // Bent villager legs put the hip about .75 above the entity origin. The
        // mount is a local furniture attachment after physical path contact.
        setPos(Vec3.atBottomCenterOf(lease.spot().furniture()).add(0, -.25, 0));
    }
    boolean matches(InnFurniture.Lease expected) { return expected.equals(lease); }
    boolean matchesFurniture(BlockPos pos) { return lease != null && lease.spot().furniture().equals(pos); }
    @Override protected void defineSynchedData(SynchedEntityData.Builder builder) { }
    @Override protected void addAdditionalSaveData(CompoundTag tag) {
        if (lease != null) tag.put(FurnitureReservations.LEASE_KEY, FurnitureReservations.writeLease(lease));
    }
    @Override protected void readAdditionalSaveData(CompoundTag tag) { lease = FurnitureReservations.readLease(tag); }
    @Override public boolean isPickable() { return false; }
    @Override public boolean canBeCollidedWith() { return false; }
    @Override protected boolean canAddPassenger(Entity passenger) {
        return lease != null && passenger instanceof DungeonsMerchant && passenger.getUUID().equals(lease.guestId()) && getPassengers().isEmpty();
    }
    @Override protected Vec3 getPassengerAttachmentPoint(Entity passenger, EntityDimensions dimensions, float scale) {
        return passenger.getVehicleAttachmentPoint(this);
    }
    @Override public Vec3 getDismountLocationForPassenger(LivingEntity passenger) {
        if (!passenger.isAlive() || passenger.isRemoved()) return passenger.position();
        if (lease != null && level() instanceof ServerLevel server
                && position().distanceToSqr(Vec3.atBottomCenterOf(lease.spot().contact())) <= 4
                && FurnitureReservations.clearStanding(server, lease.spot().contact()))
            return Vec3.atBottomCenterOf(lease.spot().contact());
        return passenger.position(); // No unknown-column or distant fallback.
    }
    @Override public void tick() {
        super.tick();
        if (!(level() instanceof ServerLevel server)) return;
        if (lease == null || lease.kind() != InnFurniture.Kind.SEAT) { discard(); return; }
        if (getPassengers().size() > 1 || getPassengers().stream().anyMatch(passenger -> !passenger.getUUID().equals(lease.guestId()))) {
            ejectPassengers(); discard(); return;
        }
        if (!FurnitureReservations.loaded(server, lease.spot().furniture(), 4)) return;
        var inn = InnSavedData.get(server.getServer()).find(lease.innId());
        var loadedGuest = server.getEntity(lease.guestId());
        if (loadedGuest == null && tickCount <= 40) return;
        boolean valid = loadedGuest instanceof DungeonsMerchant guest && guest.isAlive()
                && position().distanceToSqr(Vec3.atBottomCenterOf(lease.spot().furniture()).add(0, -.25, 0)) < .001
                && FurnitureReservations.authorized(server, guest, lease) && inn != null
                && FurnitureReservations.validSpot(server, inn, lease.spot(), InnFurniture.Kind.SEAT)
                && lease.equals(FurnitureReservations.readLease(guest.getPersistentData()));
        if (!valid || loadedGuest instanceof DungeonsMerchant guest && (guest.getTradingPlayer() != null || guest.hurtTime > 0 || guest.isNoAi())) {
            if (loadedGuest instanceof DungeonsMerchant guest && lease.equals(FurnitureReservations.readLease(guest.getPersistentData())))
                InnFurnitureContent.FURNITURE.release(server, guest, lease);
            else { ejectPassengers(); discard(); }
            return;
        }
        // A saved root entity loads its real passenger in the same entity-loading
        // transaction. The short grace period handles that order, not an absent
        // guest lookup or chunk load. An abandoned empty seat cannot accumulate.
        if (getPassengers().isEmpty() && tickCount > 40) discard();
    }
}
