package de.laugenbreze.dungeonsalive.item.gear;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.Comparator;
import java.util.List;

/** Shared target rules: bounded loaded world, solid cover, no friendly area damage. */
public final class ArtifactCombat {
    private ArtifactCombat() {}

    public static boolean hostileTo(Player owner, LivingEntity target) {
        return target instanceof Enemy && target.isAlive() && !target.isSpectator()
                && !target.isAlliedTo(owner) && !owner.isAlliedTo(target)
                && !(target instanceof TamableAnimal pet && pet.isTame())
                && !(target instanceof OwnableEntity pet && pet.getOwnerUUID() != null);
    }

    public static boolean clearLine(Player player, Vec3 start, Vec3 end) {
        return de.laugenbreze.dungeonsalive.entity.construct.ConstructService.clearLine(player.level(), start, end, player);
    }

    public static List<LivingEntity> nearby(Player owner, Vec3 center, double radius) {
        return owner.level().getEntitiesOfClass(LivingEntity.class,
                new AABB(center, center).inflate(radius), target -> hostileTo(owner, target)
                        && target.position().distanceToSqr(center) <= radius * radius
                        && clearLine(owner, center.add(0, .65, 0), target.getBoundingBox().getCenter()))
                .stream().sorted(Comparator.comparingDouble(target -> target.position().distanceToSqr(center)))
                .limit(24).toList();
    }

    public static Vec3 rayEnd(Player player, double range) {
        Vec3 start = player.getEyePosition();
        Vec3 end = start;
        for (double distance = .5; distance <= range; distance += .5) {
            Vec3 next = start.add(player.getLookAngle().scale(distance));
            if (!player.level().hasChunksAt(BlockPos.containing(start), BlockPos.containing(next))) break;
            end = next;
        }
        Vec3 blockEnd = player.level().clip(new ClipContext(start, end, ClipContext.Block.COLLIDER,
                ClipContext.Fluid.NONE, player)).getLocation();
        return de.laugenbreze.dungeonsalive.entity.construct.ConstructService.clipWalls(player.level(), start, blockEnd, player);
    }

    public static List<LivingEntity> inRay(Player player, Vec3 end, double width) {
        Vec3 start = player.getEyePosition();
        return player.level().getEntitiesOfClass(LivingEntity.class, new AABB(start, end).inflate(width),
                        target -> hostileTo(player, target)
                                && target.getBoundingBox().inflate(width).clip(start, end).isPresent()
                                && clearLine(player, start, target.getBoundingBox().getCenter()))
                .stream().sorted(Comparator.comparingDouble(player::distanceToSqr)).limit(8).toList();
    }
}
