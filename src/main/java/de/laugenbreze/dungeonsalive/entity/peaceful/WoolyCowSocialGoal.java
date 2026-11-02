package de.laugenbreze.dungeonsalive.entity.peaceful;

import java.util.Comparator;
import java.util.EnumSet;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.Nullable;

/** Real wheat following, courtship and calf following; damage lets the herd's escape goal take over. */
final class WoolyCowSocialGoal extends Goal {
    private enum Action { COURT, TEMPT, FOLLOW_PARENT }
    private final DungeonsCreature cow;
    private final WildlifeGroupPolicy pathBudget = new WildlifeGroupPolicy();
    @Nullable private LivingEntity companion;
    private Action action = Action.TEMPT;
    private int closeTicks;

    WoolyCowSocialGoal(DungeonsCreature cow) {
        this.cow = cow;
        setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override public boolean canUse() {
        if (!cow.calmWoolyCow()) return false;
        if (cow.woolyInLove()) {
            companion = cow.level().getEntitiesOfClass(DungeonsCreature.class,
                    cow.getBoundingBox().inflate(WoolyCowBreedingState.MATE_RADIUS),
                    other -> other != cow && cow.canCourt(other)
                            && cow.distanceToSqr(other) <= WoolyCowBreedingState.MATE_RADIUS * WoolyCowBreedingState.MATE_RADIUS)
                    .stream().min(Comparator.comparingDouble(cow::distanceToSqr)).orElse(null);
            if (companion != null) { action = Action.COURT; return true; }
        }
        companion = cow.level().players().stream().filter(this::tempts)
                .filter(player -> cow.distanceToSqr(player) <= WoolyCowBreedingState.TEMPT_RADIUS * WoolyCowBreedingState.TEMPT_RADIUS)
                .min(Comparator.comparingDouble(cow::distanceToSqr)).orElse(null);
        if (companion != null) { action = Action.TEMPT; return true; }
        if (cow.isBaby()) {
            companion = cow.level().getEntitiesOfClass(DungeonsCreature.class,
                    cow.getBoundingBox().inflate(WoolyCowBreedingState.PARENT_RADIUS),
                    other -> cow.isWoolyParent(other) && other.calmWoolyCow() && !other.isBaby()
                            && cow.distanceToSqr(other) <= WoolyCowBreedingState.PARENT_RADIUS * WoolyCowBreedingState.PARENT_RADIUS)
                    .stream().min(Comparator.comparingDouble(cow::distanceToSqr)).orElse(null);
            if (companion != null && cow.distanceToSqr(companion) > 9) { action = Action.FOLLOW_PARENT; return true; }
        }
        companion = null;
        return false;
    }

    @Override public boolean canContinueToUse() {
        if (!cow.calmWoolyCow() || companion == null || !companion.isAlive() || companion.isRemoved()
                || companion.level() != cow.level()) return false;
        return switch (action) {
            case COURT -> companion instanceof DungeonsCreature other && cow.canCourt(other)
                    && cow.distanceToSqr(other) <= 144;
            case TEMPT -> companion instanceof Player player && tempts(player) && cow.distanceToSqr(player) <= 144
                    && !cow.woolyInLove();
            case FOLLOW_PARENT -> cow.isBaby() && companion instanceof DungeonsCreature other
                    && cow.isWoolyParent(other) && other.calmWoolyCow() && !other.isBaby()
                    && cow.distanceToSqr(other) > 4
                    && cow.distanceToSqr(other) <= WoolyCowBreedingState.PARENT_RADIUS * WoolyCowBreedingState.PARENT_RADIUS;
        };
    }

    @Override public boolean requiresUpdateEveryTick() { return true; }
    @Override public void start() { closeTicks = 0; cow.getNavigation().stop(); }
    @Override public void stop() { cow.getNavigation().stop(); companion = null; closeTicks = 0; }

    @Override public void tick() {
        if (companion == null) return;
        cow.getLookControl().setLookAt(companion, 20, 20);
        double stopDistance = action == Action.TEMPT ? 6.25 : 4;
        if (cow.distanceToSqr(companion) <= stopDistance) {
            cow.getNavigation().stop();
            if (action == Action.COURT && companion instanceof DungeonsCreature mate && cow.hasLineOfSight(mate)) {
                if (++closeTicks >= WoolyCowBreedingState.COURTSHIP_TICKS) {
                    cow.tryWoolyBirth(mate);
                    // Failed placement/cap checks are retried only after another full courtship.
                    closeTicks = 0;
                }
            } else closeTicks = 0;
            return;
        }
        closeTicks = 0;
        // GroundPathNavigation cannot attempt a route while an ordinary walker
        // is airborne. Birth, knockback and stepping down must not consume the
        // failure backoff before a path search was possible at all.
        if (!cow.onGround() && !cow.isInLiquid() && !cow.isPassenger()) return;
        long now = cow.level().getGameTime();
        if (!pathBudget.canReplan(now)) return;
        // WildlifeGroundNavigation guards the target and the complete vanilla search cache.
        var path = cow.getNavigation().createPath(companion.blockPosition(), 0);
        boolean accepted = path != null && path.canReach() && cow.getNavigation().moveTo(path, 1.0);
        pathBudget.routeAttempted(now, accepted);
        if (!accepted) cow.getNavigation().stop();
    }

    private boolean tempts(Player player) {
        return player.isAlive() && !player.isSpectator() && player.level() == cow.level()
                && (player.getMainHandItem().is(Items.WHEAT) || player.getOffhandItem().is(Items.WHEAT));
    }
}
