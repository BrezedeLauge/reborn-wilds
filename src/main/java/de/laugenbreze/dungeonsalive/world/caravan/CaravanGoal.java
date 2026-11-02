package de.laugenbreze.dungeonsalive.world.caravan;

import de.laugenbreze.dungeonsalive.entity.peaceful.DungeonsMerchant;
import java.util.EnumSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

/** Owns MOVE continuously while a member belongs to a caravan, including route pauses. */
final class CaravanGoal extends Goal {
    // Block destinations use PathNavigation's 8-block region offset. 32 also
    // accommodates vanilla's normal positive FOLLOW_RANGE spawn bonus on base 24.
    static final int MAX_FOLLOW_RANGE = 32;
    static final int LOADED_RADIUS = MAX_FOLLOW_RANGE + 8 + 2;
    final Mob mob;
    final CaravanMember member;
    private long nextPlan;
    @Nullable private CaravanRoute.Action lastAction;

    CaravanGoal(Mob mob, CaravanMember member) {
        this.mob = mob; this.member = member;
        setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }
    @Override public boolean canUse() {
        return mob.isAlive() && !mob.isNoAi() && CaravanMember.eligible(mob)
                && CaravanSavedData.get((ServerLevel) mob.level()).ledger.contains(member.group, mob.getUUID());
    }
    @Override public boolean canContinueToUse() { return canUse(); }
    @Override public boolean requiresUpdateEveryTick() { return true; }
    @Override public void start() { mob.getNavigation().stop(); }
    @Override public void stop() { mob.getNavigation().stop(); }

    @Override public void tick() {
        if (!mob.isAlive() || mob.isNoAi() || !CaravanMember.eligible(mob)) { mob.getNavigation().stop(); return; }
        var level = (ServerLevel) mob.level();
        long now = level.getGameTime();
        // This goal runs before vanilla's navigation tick. Inactive/overridden goals call stop().
        // Include the largest native path-region offset and boundary probes, not merely the destination.
        double followRange = mob.getAttributeValue(Attributes.FOLLOW_RANGE);
        if (!Double.isFinite(followRange) || followRange <= 0 || followRange > MAX_FOLLOW_RANGE
                || !loadedArea(level, mob.blockPosition(), LOADED_RADIUS)
                || Math.min(mob.blockPosition().distSqr(member.a), mob.blockPosition().distSqr(member.b)) > 48 * 48) {
            mob.getNavigation().stop(); return;
        }
        var entity = level.getEntity(member.leader); // UUID lookup returns only an already loaded entity.
        DungeonsMerchant leader = entity instanceof DungeonsMerchant candidate && candidate.isAlive() && CaravanMember.eligible(candidate)
                && CaravanSavedData.get(level).ledger.contains(member.group, candidate.getUUID()) ? candidate : null;
        if (leader == null && now >= member.route.alarmUntil()) {
            mob.getNavigation().stop(); return;
        }
        boolean trading = leader != null && leader.isTrading();
        BlockPos destination;
        CaravanRoute.Action action;
        if (member.slot == 0) {
            BlockPos endpoint = member.route.towardsB() ? member.b : member.a;
            action = member.route.decide(now, level.getDayTime(), trading, level.isRaining(), mob.position().distanceToSqr(Vec3.atBottomCenterOf(endpoint)));
            member.save(mob);
            destination = endpoint;
        } else if (leader == null) {
            action = CaravanRoute.Action.FLEE;
            destination = mob.blockPosition(); // The bounded escape target below uses the saved actual threat.
        } else {
            action = now < member.route.alarmUntil() ? CaravanRoute.Action.FLEE
                    : trading ? CaravanRoute.Action.TRADE : CaravanRoute.Action.TRAVEL;
            // An actual slot behind and to either side of the loaded trader, not a forced teleport/leash.
            Vec3 behind = Vec3.directionFromRotation(0, leader.getYRot()).scale(-3);
            Vec3 side = new Vec3(-behind.z, 0, behind.x).normalize().scale(member.slot == 1 ? -1.8 : 1.8);
            destination = BlockPos.containing(leader.position().add(behind).add(side));
            if (action == CaravanRoute.Action.TRAVEL && mob.distanceToSqr(leader) <= 16) {
                mob.getNavigation().stop(); return;
            }
            if (mob.distanceToSqr(leader) > 48 * 48) { mob.getNavigation().stop(); return; }
        }
        if (action != lastAction) {
            mob.getNavigation().stop();
            nextPlan = now;
            lastAction = action;
        }
        if (action == CaravanRoute.Action.TRADE || action == CaravanRoute.Action.WAIT) {
            mob.getNavigation().stop(); return;
        }
        // GroundPathNavigation cannot plan while ordinary knockback has lifted the mob.
        // This temporary physics state is not a failed route and must not consume the
        // 80-tick obstacle backoff (almost the entire finite panic window).
        if (!mob.onGround() && !mob.isInLiquid() && !mob.isPassenger()) return;
        if (now < nextPlan) return;
        nextPlan = now + 20;
        if (action == CaravanRoute.Action.FLEE) {
            Vec3 away = mob.position().subtract(member.threat).multiply(1, 0, 1);
            if (away.lengthSqr() < .01) away = Vec3.directionFromRotation(0, mob.getYRot() + 180);
            destination = BlockPos.containing(mob.position().add(away.normalize().scale(6)));
            if (Math.min(destination.distSqr(member.a), destination.distSqr(member.b)) > 36 * 36) {
                mob.getNavigation().stop(); return;
            }
        }
        BlockPos safe = findFloor(mob, destination);
        if (safe == null) { mob.getNavigation().stop(); nextPlan = now + 80; return; }
        // Use only a local waypoint; each next leg is rechecked against current loaded terrain.
        Vec3 delta = Vec3.atBottomCenterOf(safe).subtract(mob.position());
        if (delta.horizontalDistanceSqr() > 12 * 12) {
            BlockPos waypoint = BlockPos.containing(mob.position().add(delta.normalize().scale(10)));
            safe = findFloor(mob, waypoint);
            if (safe == null) { mob.getNavigation().stop(); nextPlan = now + 80; return; }
        }
        var path = mob.getNavigation().createPath(safe, 0);
        if (path == null || !path.canReach() || !mob.getNavigation().moveTo(path, action == CaravanRoute.Action.FLEE ? 1.15 : .7)) {
            mob.getNavigation().stop(); nextPlan = now + 80; return;
        }
        mob.getLookControl().setLookAt(safe.getX() + .5, safe.getY() + mob.getEyeHeight(), safe.getZ() + .5);
    }

    void hurt(Vec3 source, long now) {
        member.threat = source;
        member.route.hurt(now);
        member.save(mob);
        nextPlan = now;
        mob.getNavigation().stop();
    }
    static boolean loadedArea(Level level, BlockPos center, int radius) {
        for (int x = (center.getX() - radius) >> 4; x <= (center.getX() + radius) >> 4; x++)
            for (int z = (center.getZ() - radius) >> 4; z <= (center.getZ() + radius) >> 4; z++) if (!level.hasChunk(x, z)) return false;
        return true;
    }
    @Nullable static BlockPos findFloor(Mob mob, BlockPos destination) {
        for (int offset : new int[]{0, 1, -1, 2, -2}) {
            BlockPos pos = destination.above(offset);
            if (safeFloor(mob, pos)) return pos;
        }
        return null;
    }
    static boolean safeFloor(Mob mob, BlockPos pos) {
        Level level = mob.level();
        if (pos.getY() <= level.getMinBuildHeight() || pos.getY() + mob.getBbHeight() + 1 >= level.getMaxBuildHeight()
                || !level.getWorldBorder().isWithinBounds(pos) || !loadedArea(level, pos, 2)
                || !level.getBlockState(pos.below()).isFaceSturdy(level, pos.below(), Direction.UP)) return false;
        for (int y = -1; y <= Mth.ceil(mob.getBbHeight()); y++) {
            var block = level.getBlockState(pos.above(y));
            if (!block.getFluidState().isEmpty() || block.is(Blocks.FIRE) || block.is(Blocks.SOUL_FIRE)
                    || block.is(Blocks.CACTUS) || block.is(Blocks.MAGMA_BLOCK) || block.is(Blocks.SWEET_BERRY_BUSH)
                    || block.is(Blocks.WITHER_ROSE) || block.is(Blocks.CAMPFIRE) || block.is(Blocks.SOUL_CAMPFIRE)
                    || block.is(Blocks.POWDER_SNOW)) return false;
        }
        var box = mob.getBoundingBox().move(Vec3.atBottomCenterOf(pos).subtract(mob.position()));
        return level.noCollision(mob, box);
    }
}
