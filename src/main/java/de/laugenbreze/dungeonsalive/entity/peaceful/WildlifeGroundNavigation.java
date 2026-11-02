package de.laugenbreze.dungeonsalive.entity.peaceful;

import java.util.Set;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.Path;
import org.jetbrains.annotations.Nullable;

/** Includes vanilla's whole search cache and its automatic replans in the loaded-area guard. */
final class WildlifeGroundNavigation extends GroundPathNavigation {
    WildlifeGroundNavigation(DungeonsCreature creature, Level level) { super(creature, level); }

    @Override @Nullable public Path createPath(BlockPos target, int accuracy) {
        if (!WildlifeNavigationSafety.loadedArea(level, target, 2)
                || !WildlifeNavigationSafety.loadedArea(level, mob.blockPosition(), WildlifeNavigationSafety.LOADED_RADIUS)) return null;
        return super.createPath(target, accuracy);
    }
    @Override @Nullable protected Path createPath(Set<BlockPos> targets, int regionOffset,
                                                  boolean offsetUpward, int accuracy, float followRange) {
        if (regionOffset < 0 || regionOffset > 16 || !Float.isFinite(followRange) || followRange <= 0
                || !WildlifeNavigationSafety.loadedArea(level, mob.blockPosition(), WildlifeNavigationSafety.LOADED_RADIUS)
                || targets.stream().anyMatch(target -> !WildlifeNavigationSafety.loadedArea(level, target, 2))) return null;
        return super.createPath(targets, regionOffset, offsetUpward, accuracy,
                Math.min(followRange, WildlifeNavigationSafety.MAX_FOLLOW_RANGE));
    }
    @Override public void tick() {
        if (!WildlifeNavigationSafety.loadedArea(level, mob.blockPosition(), WildlifeNavigationSafety.LOADED_RADIUS)) {
            stop(); return;
        }
        super.tick();
    }
}
