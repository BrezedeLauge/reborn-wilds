package de.laugenbreze.dungeonsalive.entity.peaceful;

import java.util.EnumSet;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;

/** Real aquatic groups use separated swimming positions and a shared, local drifting course. */
final class WildlifeSchoolGoal extends Goal {
    private final DungeonsCreature creature;
    private final WildlifeGroupPolicy budget = new WildlifeGroupPolicy();
    WildlifeSchoolGoal(DungeonsCreature creature) { this.creature = creature; setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK)); }
    @Override public boolean canUse() {
        return creature.aquaticWildlife() && creature.ownerId() == null && !creature.waiting()
                && creature.isInWater() && creature.wildlifeGroupId() != null && creature.wildlifeAnchor() != null;
    }
    @Override public boolean canContinueToUse() { return canUse(); }
    @Override public boolean requiresUpdateEveryTick() { return true; }
    @Override public void stop() { creature.getNavigation().stop(); }

    @Override public void tick() {
        long now = creature.level().getGameTime();
        if (!budget.canReplan(now)) return;
        if (!WildlifeNavigationSafety.loadedArea(creature.level(), creature.blockPosition(), WildlifeNavigationSafety.LOADED_RADIUS)) {
            creature.getNavigation().stop(); budget.routeAttempted(now, false); return;
        }
        double phase = now / 240.0 + Math.floorMod(creature.wildlifeGroupId().getLeastSignificantBits(), 6283) / 1000.0;
        double slot = creature.wildlifeSlot() * Math.PI * 2 / WildlifeGroupPolicy.MAX_MEMBERS;
        Vec3 target = Vec3.atCenterOf(creature.wildlifeAnchor()).add(Math.cos(phase) * 14 + Math.cos(slot) * 2,
                Math.sin(phase * .7) * 2 + Math.sin(slot), Math.sin(phase) * 14 + Math.sin(slot) * 2);
        var leader = creature.wildlifeLeader();
        if (leader != creature && creature.distanceToSqr(leader) > 64) target = leader.position().add(Math.cos(slot) * 2, 0, Math.sin(slot) * 2);
        boolean fleeing = now < creature.wildlifeAlarmUntil();
        if (fleeing) {
            Vec3 away = creature.position().subtract(creature.wildlifeThreat());
            if (away.lengthSqr() < .001) away = new Vec3(Math.cos(phase), .15, Math.sin(phase));
            target = creature.position().add(away.normalize().scale(8));
        }
        Vec3 delta = target.subtract(creature.position());
        if (delta.lengthSqr() > 144) target = creature.position().add(delta.normalize().scale(12));
        boolean moved = false;
        // Try a small depth adjustment when the shared course meets a bank/uneven seabed.
        for (int depth : new int[]{0, -1, 1, -2, 2}) {
            Vec3 candidate = target.add(0, depth, 0);
            if (!safeWater(creature, candidate)) continue;
            var path = creature.getNavigation().createPath(BlockPos.containing(candidate), 0);
            if (path != null && path.canReach() && creature.getNavigation().moveTo(path, fleeing ? 1.15 : .65)) {
                creature.getLookControl().setLookAt(candidate.x, candidate.y, candidate.z);
                moved = true; break;
            }
        }
        budget.routeAttempted(now, moved);
        if (!moved) creature.getNavigation().stop();
    }

    static boolean safeWater(DungeonsCreature creature, Vec3 feet) {
        if (!Double.isFinite(feet.x) || !Double.isFinite(feet.y) || !Double.isFinite(feet.z)) return false;
        var level = creature.level();
        var box = creature.getBoundingBox().move(feet.subtract(creature.position()));
        var min = BlockPos.containing(box.minX, box.minY, box.minZ);
        var max = BlockPos.containing(box.maxX, box.maxY, box.maxZ);
        if (box.minY <= level.getMinBuildHeight() || box.maxY >= level.getMaxBuildHeight()
                || !level.getWorldBorder().isWithinBounds(min) || !level.getWorldBorder().isWithinBounds(max)
                || !WildlifeNavigationSafety.loadedArea(level, min, 2)
                || !WildlifeNavigationSafety.loadedArea(level, max, 2)) return false;
        for (BlockPos pos : BlockPos.betweenClosed(min, max)) if (!level.getFluidState(pos).is(FluidTags.WATER)) return false;
        return level.noBlockCollision(creature, box);
    }
}
