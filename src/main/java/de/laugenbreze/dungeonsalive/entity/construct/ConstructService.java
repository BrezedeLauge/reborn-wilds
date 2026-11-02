package de.laugenbreze.dungeonsalive.entity.construct;

import de.laugenbreze.dungeonsalive.entity.DungeonMob;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

/** All placement and attack queries are bounded and precede terrain access with a loaded-area check. */
public final class ConstructService {
    public static final int OWNER_CAP = 8;
    public static final int LOCAL_CAP = 32;
    public static final int MAX_RESERVATION_TICKS = 240;
    private ConstructService() {}

    public static boolean mayHarm(DungeonMob owner, LivingEntity victim) {
        // An explicit target cannot turn a passive animal, villager or pet into collateral damage.
        return owner.isAlive() && owner.isOpponent(victim)
                && (victim instanceof Player || victim instanceof IronGolem || victim instanceof Enemy);
    }

    /** Three separate pillars leave the side toward the caster open; obstructed positions are omitted. */
    public static int castWalls(DungeonMob owner, LivingEntity target, Vec3 lockedGround, float damage) {
        Vec3 front = new Vec3(owner.getX() - lockedGround.x, 0, owner.getZ() - lockedGround.z).normalize();
        if (front.lengthSqr() < .01) front = new Vec3(0, 0, -1);
        Vec3 side = new Vec3(-front.z, 0, front.x);
        int count = 0;
        for (Vec3 offset : List.of(front.scale(-2.1), side.scale(2.1), side.scale(-2.1))) {
            if (spawn(owner, target, ConstructKind.GEOMANCER_WALL, lockedGround.add(offset), damage) != null) count++;
        }
        return count;
    }

    /** Bomb pillars form beside the locked target, never through an occupied living body. */
    @Nullable public static DungeonConstruct castBomb(DungeonMob owner, LivingEntity target, Vec3 lockedGround, float damage) {
        Vec3 towardCaster = new Vec3(owner.getX() - lockedGround.x, 0, owner.getZ() - lockedGround.z).normalize();
        if (towardCaster.lengthSqr() < .01) towardCaster = new Vec3(0, 0, -1);
        Vec3 side = new Vec3(-towardCaster.z, 0, towardCaster.x);
        double offset = Math.min(3, target.getBbWidth() * .5 + 1.0);
        for (Vec3 direction : List.of(towardCaster, side, side.scale(-1), towardCaster.scale(-1))) {
            DungeonConstruct bomb = spawn(owner, target, ConstructKind.GEOMANCER_BOMB, lockedGround.add(direction.scale(offset)), damage);
            if (bomb != null) return bomb;
        }
        return null;
    }

    /** Public authoring/showcase hook with the same caps and safety checks as a real spell. */
    @Nullable public static DungeonConstruct spawn(DungeonMob owner, LivingEntity target, ConstructKind kind, Vec3 lockedGround, float damage) {
        if (!(owner.level() instanceof ServerLevel level) || !owner.isAlive() || !mayHarm(owner, target)
                || target.level() != level || !finite(lockedGround) || owner.distanceToSqr(lockedGround) > 24 * 24
                || owner.constructReservationCount() >= OWNER_CAP) return null;
        AABB local = new AABB(lockedGround, lockedGround).inflate(32);
        if (level.getEntitiesOfClass(DungeonConstruct.class, local).size() >= LOCAL_CAP) return null;
        Vec3 ground = findGround(level, owner, kind, lockedGround);
        if (ground == null || !clearLine(level, owner.getEyePosition(), ground.add(0, .5, 0), owner)) return null;
        Vec3 position = ground;
        if (kind == ConstructKind.ICE_CHUNK) {
            position = ground.add(0, 4.0, 0);
            // Keep the lower face above its supporting floor; only widen the swept column horizontally.
            AABB column = box(kind, position).minmax(box(kind, ground)).inflate(.02, 0, .02);
            if (!loaded(level, column) || !level.noBlockCollision(null, column) || level.containsAnyLiquid(column)) return null;
        }
        AABB body = box(kind, position);
        if (!loaded(level, body.inflate(1)) || !level.getWorldBorder().isWithinBounds(body)
                || !level.noBlockCollision(null, body)) return null;
        // Pillars may not rise through any living occupant; neither pushes nor block edits make room.
        if ((kind == ConstructKind.GEOMANCER_WALL || kind == ConstructKind.GEOMANCER_BOMB)
                && occupied(level, body)) return null;
        DungeonConstruct construct = ModConstructs.type(kind).create(level);
        if (construct == null) return null;
        construct.initialize(owner, target, position, damage);
        if (!owner.reserveConstruct(construct.getUUID(), construct.expiresAt())) return null;
        if (!level.addFreshEntity(construct)) {
            owner.releaseConstruct(construct.getUUID());
            return null;
        }
        return construct;
    }

    @Nullable private static Vec3 findGround(ServerLevel level, DungeonMob owner, ConstructKind kind, Vec3 requested) {
        int top = (int)Math.floor(requested.y) + 1;
        boolean allowWater = kind == ConstructKind.ENTANGLE_VINE && owner.profile().aquatic();
        for (int y = top; y >= top - 5; y--) {
            Vec3 candidate = new Vec3(requested.x, y + .015, requested.z);
            AABB body = box(kind, candidate);
            if (y <= level.getMinBuildHeight() || body.maxY >= level.getMaxBuildHeight() || !loaded(level, body.inflate(1))) continue;
            boolean support = true;
            double inset = Math.max(.1, kind.width() * .5 - .05);
            for (double x : new double[]{candidate.x - inset, candidate.x + inset}) {
                for (double z : new double[]{candidate.z - inset, candidate.z + inset}) {
                    BlockPos floor = BlockPos.containing(x, y - .05, z);
                    if (!level.getBlockState(floor).isFaceSturdy(level, floor, Direction.UP)) support = false;
                }
            }
            if (!support || !level.noBlockCollision(null, body)) continue;
            if (allowWater) {
                if (level.getFluidState(BlockPos.containing(candidate)).is(FluidTags.LAVA)) continue;
            } else if (level.containsAnyLiquid(body)) continue;
            return candidate;
        }
        return null;
    }

    public static boolean occupied(ServerLevel level, AABB box) {
        return occupied(level, box, null);
    }
    public static boolean occupied(ServerLevel level, AABB box, @Nullable Entity except) {
        return !level.getEntitiesOfClass(LivingEntity.class, box, entity -> entity != except && entity.isAlive()).isEmpty();
    }
    public static AABB box(ConstructKind kind, Vec3 pos) {
        double radius = kind.width() * .5;
        return new AABB(pos.x - radius, pos.y, pos.z - radius, pos.x + radius, pos.y + kind.height(), pos.z + radius);
    }
    public static boolean loaded(Level level, AABB box) {
        return level.hasChunksAt(BlockPos.containing(box.minX, box.minY, box.minZ), BlockPos.containing(box.maxX, box.maxY, box.maxZ));
    }
    public static Vec3 clipWalls(Level level, Vec3 from, Vec3 to, @Nullable Entity except) {
        Vec3 end = to;
        double shortest = from.distanceToSqr(to);
        for (DungeonConstruct wall : level.getEntitiesOfClass(DungeonConstruct.class, new AABB(from, to).inflate(.01),
                entity -> entity != except && entity.blocksMovement())) {
            var intersection = wall.getBoundingBox().clip(from, end);
            if (intersection.isPresent() && from.distanceToSqr(intersection.get()) < shortest) {
                end = intersection.get(); shortest = from.distanceToSqr(end);
            }
        }
        return end;
    }
    public static boolean clearLine(Level level, Vec3 from, Vec3 to, Entity context) {
        if (!loaded(level, new AABB(from, to).inflate(1))) return false;
        HitResult hit = level.clip(new ClipContext(from, to, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, context));
        return hit.getType() == HitResult.Type.MISS && clipWalls(level, from, to, context).distanceToSqr(to) < .0001;
    }
    private static boolean finite(Vec3 vector) { return Double.isFinite(vector.x) && Double.isFinite(vector.y) && Double.isFinite(vector.z); }
}
