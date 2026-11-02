package de.laugenbreze.dungeonsalive.settlement.inn;

import de.laugenbreze.dungeonsalive.entity.peaceful.DungeonsMerchant;
import java.util.EnumSet;
import java.util.Objects;
import java.util.function.BooleanSupplier;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import org.jetbrains.annotations.Nullable;

/** One real saved traveller: walk in, use an assigned seat/meal, rest, and walk out. */
public final class InnVisitorGoal extends Goal {
    private static final int MEAL_SEAT_TICKS = 120, REST_TICKS = 200, MAX_ACTIVITY_TICKS = 2_400;
    private final DungeonsMerchant visitor;
    private final BooleanSupplier frightened;
    @Nullable private InnFurniture.Lease lease;
    private long retryAt;
    private int usingTicks;

    /** Merchant registers at priority3; the supplier reads its existing real damage-escape deadline. */
    public InnVisitorGoal(DungeonsMerchant visitor, BooleanSupplier frightened) {
        this.visitor = visitor; this.frightened = Objects.requireNonNull(frightened);
        setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }
    @Override public boolean canUse() {
        if (!(visitor.level() instanceof ServerLevel level) || !visitor.isAlive() || visitor.isNoAi()
                || visitor.isTrading() || frightened.getAsBoolean() || InnService.furniture(level.getServer()) == null) return false;
        var inn = InnService.visitOf(visitor);
        if (inn != null && visitor.getNavigation() instanceof GroundPathNavigation navigation) navigation.setCanOpenDoors(true);
        if (inn != null && lease == null && (visitor.isPassenger() || visitor.isSleeping())) {
            lease = InnService.furniture(level.getServer()).resume(level, visitor, inn);
            usingTicks = 0;
        }
        return inn != null && inn.guest().phase() != InnRegistry.GuestPhase.RESERVED
                && (!visitor.isPassenger() || lease != null);
    }
    @Override public boolean canContinueToUse() { return canUse(); }
    @Override public boolean requiresUpdateEveryTick() { return true; }
    @Override public void stop() { visitor.getNavigation().stop(); release(); }

    @Override public void tick() {
        var level = (ServerLevel)visitor.level();
        var inn = InnService.visitOf(visitor); if (inn == null) { stop(); return; }
        long now = level.getGameTime();
        if (!visitor.getNavigation().isDone() && !InnRouteValidator.remainingRouteSafe(visitor)) {
            visitor.getNavigation().stop(); retryAt = Math.max(retryAt, now + 40);
        }
        if (inn.guest().phase() != InnRegistry.GuestPhase.LEAVING && now - inn.guest().since() >= MAX_ACTIVITY_TICKS) {
            leave(inn, now); return;
        }
        switch (inn.guest().phase()) {
            case TRAVELLING -> {
                BlockPos entrance = InnRouteValidator.pos(inn.entrance());
                if (contact(entrance)) {
                    visitor.getNavigation().stop();
                    InnSavedData.get(level.getServer()).advance(inn.id(), visitor.getUUID(), InnRegistry.GuestPhase.TRAVELLING,
                            InnRegistry.GuestPhase.ARRIVED, now);
                } else move(entrance, now);
            }
            case ARRIVED -> {
                if (use(inn, InnFurniture.Kind.SEAT, now) && ++usingTicks >= MEAL_SEAT_TICKS) {
                    if (!InnService.eatAtReservedSeat(visitor, lease)) leave(inn, now);
                    else release();
                }
            }
            case RESTING -> {
                InnFurniture.Kind kind = level.isNight() ? InnFurniture.Kind.BED : InnFurniture.Kind.SEAT;
                if (use(inn, kind, now) && ++usingTicks >= REST_TICKS) leave(inn, now);
            }
            case LEAVING -> {
                // External retirement can switch a currently seated/sleeping
                // visitor to LEAVING. Keep its lease until a real safe exit exists.
                if (!release()) return;
                BlockPos exit = InnRouteValidator.pos(inn.arrival());
                if (contact(exit)) {
                    release(); visitor.getNavigation().stop();
                    // This is the created visitor's actual reached exit, not a
                    // replacement/despawn inferred from an unloaded UUID.
                    visitor.discard();
                    InnSavedData.get(level.getServer()).finishDeparture(inn.id(), visitor.getUUID(), now);
                } else move(exit, now);
            }
            default -> visitor.getNavigation().stop();
        }
    }

    private boolean use(InnRegistry.Inn inn, InnFurniture.Kind kind, long now) {
        var level = (ServerLevel)visitor.level(); var adapter = InnService.furniture(level.getServer());
        if (adapter == null) return false;
        if (lease != null && (lease.kind() != kind || lease.configRevision() != inn.revision()) && !release()) return false;
        if (lease == null) {
            if (now < retryAt) return false;
            lease = adapter.reserve(level, visitor, inn, kind);
            retryAt = now + (lease == null ? 40 : 1);
            if (lease == null) return false;
        }
        if (adapter.use(level, visitor, lease)) { visitor.getNavigation().stop(); return true; }
        if (!InnRouteValidator.safeStanding(visitor, lease.spot().contact())) { release(); retryAt = now + 40; return false; }
        move(lease.spot().contact(), now);
        return false;
    }
    private void leave(InnRegistry.Inn inn, long now) {
        release(); visitor.getNavigation().stop();
        InnSavedData.get(((ServerLevel)visitor.level()).getServer()).advance(inn.id(), visitor.getUUID(), inn.guest().phase(),
                InnRegistry.GuestPhase.LEAVING, now);
    }
    private boolean release() {
        if (lease != null && visitor.level() instanceof ServerLevel level) {
            var adapter = InnService.furniture(level.getServer());
            if (adapter == null || !adapter.release(level, visitor, lease)) return false;
        }
        lease = null; usingTicks = 0;
        return true;
    }
    private boolean contact(BlockPos point) {
        return InnRouteValidator.safeStanding(visitor, point) && visitor.onGround()
                && visitor.position().distanceToSqr(InnRouteValidator.feet(visitor.level(), point)) < 1.21;
    }
    private void move(BlockPos point, long now) {
        // Vanilla cannot start a grounded path search during a knockback/fall.
        // This wait must not consume a genuine search-failure backoff.
        if (!visitor.onGround() && !visitor.isInLiquid() && !visitor.isPassenger() || now < retryAt) return;
        var route = InnRouteValidator.plan(visitor, point);
        boolean started = route.connected() && route.path() != null && visitor.getNavigation().moveTo(route.path(), .75);
        retryAt = now + (started ? 20 : 40);
        if (!started) visitor.getNavigation().stop();
    }
}
