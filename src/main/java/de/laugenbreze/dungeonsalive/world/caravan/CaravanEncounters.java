package de.laugenbreze.dungeonsalive.world.caravan;

import de.laugenbreze.dungeonsalive.entity.peaceful.DungeonsCreature;
import de.laugenbreze.dungeonsalive.entity.peaceful.DungeonsMerchant;
import de.laugenbreze.dungeonsalive.entity.peaceful.ModPeacefulEntities;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.inventory.MerchantMenu;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.EntityLeaveLevelEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.player.PlayerContainerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.server.ServerStoppedEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;

/** Rare, real server caravans. No chunk tickets, terrain edits, NPC recruitment or teleportation. */
public final class CaravanEncounters {
    private static final Map<Mob, CaravanGoal> GOALS = new HashMap<>();
    private record Trade(ServerPlayer player, MerchantMenu menu) {}
    private static final Map<DungeonsMerchant, Trade> TRADES = new HashMap<>();

    public static void register() {
        NeoForge.EVENT_BUS.addListener(CaravanEncounters::join);
        NeoForge.EVENT_BUS.addListener(CaravanEncounters::leave);
        NeoForge.EVENT_BUS.addListener(CaravanEncounters::damage);
        NeoForge.EVENT_BUS.addListener(CaravanEncounters::open);
        NeoForge.EVENT_BUS.addListener(CaravanEncounters::close);
        NeoForge.EVENT_BUS.addListener(CaravanEncounters::interact);
        NeoForge.EVENT_BUS.addListener(CaravanEncounters::tick);
        NeoForge.EVENT_BUS.addListener(CaravanEncounters::shutdown);
    }
    private static void join(EntityJoinLevelEvent event) {
        if (!(event.getLevel() instanceof ServerLevel level) || !(event.getEntity() instanceof Mob mob) || GOALS.containsKey(mob)) return;
        CaravanMember member = CaravanMember.load(mob);
        if (member == null || !CaravanSavedData.get(level).ledger.contains(member.group, mob.getUUID())) return;
        var goal = new CaravanGoal(mob, member);
        GOALS.put(mob, goal);
        mob.getNavigation().stop();
        mob.goalSelector.addGoal(3, goal);
    }
    private static void leave(EntityLeaveLevelEvent event) {
        if (!(event.getLevel() instanceof ServerLevel level) || !(event.getEntity() instanceof Mob mob)) return;
        var goal = GOALS.remove(mob);
        if (goal != null) {
            mob.goalSelector.removeGoal(goal);
            var reason = mob.getRemovalReason();
            // Unloading keeps the reservation. Cross-dimension removal cannot carry this local route.
            if (reason != null && (reason.shouldDestroy() || reason == Entity.RemovalReason.CHANGED_DIMENSION))
                CaravanSavedData.get(level).release(goal.member.group, mob.getUUID());
        }
        if (mob instanceof DungeonsMerchant merchant) TRADES.remove(merchant);
    }
    private static void damage(LivingDamageEvent.Post event) {
        if (!(event.getEntity() instanceof Mob mob) || !(mob.level() instanceof ServerLevel level)
                || !CaravanMember.eligible(mob) || event.getNewDamage() <= 0) return;
        var hit = GOALS.get(mob);
        if (hit == null) return;
        Vec3 source = event.getSource().getSourcePosition();
        if (source == null || !Double.isFinite(source.x) || !Double.isFinite(source.y) || !Double.isFinite(source.z)) source = mob.position();
        for (UUID id : CaravanSavedData.get(level).ledger.members(hit.member.group)) {
            Entity entity = level.getEntity(id);
            if (!(entity instanceof Mob other) || !other.isAlive() || !CaravanMember.eligible(other) || other.distanceToSqr(mob) > 32 * 32) continue;
            var goal = GOALS.get(other);
            if (goal == null || !goal.member.group.equals(hit.member.group)) continue;
            goal.hurt(source, level.getGameTime());
            if (other instanceof DungeonsMerchant merchant) {
                var trade = TRADES.remove(merchant);
                if (trade != null && trade.player.containerMenu == trade.menu && merchant.getTradingPlayer() == trade.player)
                    trade.player.closeContainer();
                merchant.setTradingPlayer(null);
            }
        }
    }
    private static void open(PlayerContainerEvent.Open event) {
        if (!(event.getEntity() instanceof ServerPlayer player) || !(event.getContainer() instanceof MerchantMenu menu)) return;
        // Inspect only the feature's at-most-two loaded traders, never arbitrary players' menus.
        for (Mob mob : GOALS.keySet()) if (mob instanceof DungeonsMerchant merchant && merchant.level() == player.level()
                && merchant.getTradingPlayer() == player && merchant.distanceToSqr(player) <= 64) TRADES.put(merchant, new Trade(player, menu));
    }
    private static void close(PlayerContainerEvent.Close event) {
        TRADES.entrySet().removeIf(entry -> entry.getValue().player == event.getEntity() && entry.getValue().menu == event.getContainer());
    }
    private static void interact(PlayerInteractEvent.EntityInteract event) {
        if (!(event.getTarget() instanceof DungeonsMerchant merchant) || merchant.level().isClientSide || event.getEntity().isShiftKeyDown()) return;
        var goal = GOALS.get(merchant);
        if (goal != null && merchant.level().getGameTime() < goal.member.route.alarmUntil()) {
            event.setCancellationResult(InteractionResult.FAIL);
            event.setCanceled(true);
        }
    }
    private static void shutdown(ServerStoppedEvent event) { GOALS.clear(); TRADES.clear(); }

    private static void tick(LevelTickEvent.Post event) {
        if (!(event.getLevel() instanceof ServerLevel level) || level.getGameTime() % 40 != 0) return;
        // Claiming a companion uses its existing interaction. It becomes an ordinary owned pet immediately.
        for (var entry : new ArrayList<>(GOALS.entrySet())) {
            Mob mob = entry.getKey();
            if (mob.level() == level && !CaravanMember.eligible(mob)) detach(level, mob, entry.getValue());
        }
        if (level.dimension() != Level.OVERWORLD || !CaravanConfig.ENABLED.get()
                || !level.getGameRules().getBoolean(GameRules.RULE_DOMOBSPAWNING)
                || Math.floorMod(level.getDayTime(), 24_000L) >= 12_000 || level.isRaining()) return;
        var history = CaravanSavedData.get(level);
        if (history.ledger.groups() >= CaravanLedger.MAX_GROUPS || level.getGameTime() < history.nextAttempt) return;
        var players = level.players().stream().filter(player -> player.isAlive() && !player.isSpectator() && !player.isSleeping()).toList();
        if (players.isEmpty()) return;
        history.nextAttempt = level.getGameTime() + 1_200;
        history.setDirty();
        if (level.random.nextInt(3) != 0) return;
        ServerPlayer player = players.get(level.random.nextInt(players.size()));
        for (int attempt = 0; attempt < 6; attempt++) {
            double angle = level.random.nextDouble() * Math.PI * 2;
            int x = Mth.floor(player.getX() + Math.cos(angle) * 40), z = Mth.floor(player.getZ() + Math.sin(angle) * 40);
            BlockPos column = new BlockPos(x, player.blockPosition().getY(), z);
            if (!CaravanGoal.loadedArea(level, column, CaravanGoal.LOADED_RADIUS)) continue;
            int y = level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, x, z);
            BlockPos a = new BlockPos(x, y, z);
            if (!level.canSeeSky(a) || Math.abs(y - player.getY()) > 12
                    || history.origins.values().stream().anyMatch(origin -> horizontalDistanceSquared(origin, a) < 192 * 192)) continue;
            double heading = level.random.nextDouble() * Math.PI * 2;
            int bx = x + Mth.floor(Math.cos(heading) * 20), bz = z + Mth.floor(Math.sin(heading) * 20);
            BlockPos second = new BlockPos(bx, y, bz);
            if (!CaravanGoal.loadedArea(level, second, CaravanGoal.LOADED_RADIUS)) continue;
            BlockPos b = new BlockPos(bx, level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, bx, bz), bz);
            if (Math.abs(a.getY() - b.getY()) > 3 || !level.canSeeSky(b)) continue;
            if (!spawnGroup(level, a, b).isEmpty()) return;
        }
    }

    /** Real transactional group creation; package visibility allows the native GameTest to exercise it. */
    static List<Mob> spawnGroup(ServerLevel level, BlockPos a, BlockPos b) {
        var history = CaravanSavedData.get(level);
        if (history.ledger.groups() >= CaravanLedger.MAX_GROUPS || a.distSqr(b) < 64 || a.distSqr(b) > 1024
                || !CaravanGoal.loadedArea(level, a, CaravanGoal.LOADED_RADIUS) || !CaravanGoal.loadedArea(level, b, CaravanGoal.LOADED_RADIUS)
                || history.origins.values().stream().anyMatch(origin -> horizontalDistanceSquared(origin, a) < 192 * 192)) return List.of();
        DungeonsMerchant merchant = ModPeacefulEntities.merchantType("village_merchant").create(level);
        DungeonsCreature left = ModPeacefulEntities.creatureType("pet_baby_wooly_cow").create(level);
        DungeonsCreature right = ModPeacefulEntities.creatureType("pet_baby_wooly_cow").create(level);
        if (merchant == null || left == null || right == null) return List.of();
        List<Mob> members = List.of(merchant, left, right);
        List<BlockPos> origins = List.of(a, a.offset(2, 0, 2), a.offset(-2, 0, 2));
        UUID group = UUID.randomUUID();
        for (int index = 0; index < members.size(); index++) {
            Mob member = members.get(index);
            BlockPos spawn = CaravanGoal.findFloor(member, origins.get(index));
            if (spawn == null || !CaravanGoal.safeFloor(member, b)) { members.forEach(Mob::discard); return List.of(); }
            member.setPos(Vec3.atBottomCenterOf(spawn));
            // A freshly constructed entity has not had a physics tick yet. safeFloor just
            // established actual dry solid support; initialize that state before the genuine
            // GroundPathNavigation preflight (which otherwise refuses every ungrounded mob).
            member.setOnGround(true);
            member.finalizeSpawn(level, level.getCurrentDifficultyAt(spawn), MobSpawnType.EVENT, null);
            member.setPersistenceRequired();
            CaravanMember.attach(member, group, merchant.getUUID(), a, b, index);
        }
        var route = merchant.getNavigation().createPath(b, 0);
        if (route == null || !route.canReach()) { members.forEach(Mob::discard); return List.of(); }
        merchant.getNavigation().stop();
        if (!history.ledger.reserve(group, Set.of(merchant.getUUID(), left.getUUID(), right.getUUID()))) {
            members.forEach(Mob::discard); return List.of();
        }
        history.origins.put(group, a.immutable());
        history.setDirty();
        for (Mob member : members) if (!level.addFreshEntity(member)) {
            members.forEach(Mob::discard);
            members.forEach(other -> history.release(group, other.getUUID()));
            return List.of();
        }
        return members;
    }
    private static void detach(ServerLevel level, Mob mob, CaravanGoal goal) {
        mob.goalSelector.removeGoal(goal);
        GOALS.remove(mob);
        mob.removeTag(CaravanMember.TAG);
        mob.getPersistentData().remove(CaravanMember.KEY);
        CaravanSavedData.get(level).release(goal.member.group, mob.getUUID());
        if (mob instanceof DungeonsMerchant merchant) TRADES.remove(merchant);
    }
    private static double horizontalDistanceSquared(BlockPos a, BlockPos b) {
        double x = (double) a.getX() - b.getX(), z = (double) a.getZ() - b.getZ();
        return x * x + z * z;
    }
    private CaravanEncounters() {}
}
