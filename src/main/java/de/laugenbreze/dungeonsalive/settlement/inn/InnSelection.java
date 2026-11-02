package de.laugenbreze.dungeonsalive.settlement.inn;

import java.util.Map;
import java.util.UUID;
import java.util.WeakHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

/** One expiring server-owned plan per online player; the tool carries no authoritative data. */
public final class InnSelection {
    private static final Map<ServerPlayer, Draft> PLANS = new WeakHashMap<>();
    private static final long LIFETIME = 24_000;
    public record Draft(UUID token, ResourceLocation dimension, BlockPos anchor, UUID placement, long revision, long expires,
                        @Nullable BlockPos first, @Nullable BlockPos second, @Nullable BlockPos entrance,
                        @Nullable BlockPos stock, @Nullable BlockPos arrival) {
        public int step() { return first == null ? 0 : second == null ? 1 : entrance == null ? 2 : stock == null ? 3 : arrival == null ? 4 : 5; }
        public boolean complete() { return step() == 5; }
        public InnBounds bounds() {
            if (first == null || second == null) throw new IllegalStateException("Incomplete room selection");
            return new InnBounds(Math.min(first.getX(), second.getX()), Math.min(first.getY(), second.getY()), Math.min(first.getZ(), second.getZ()),
                    Math.max(first.getX(), second.getX()), Math.max(first.getY(), second.getY()), Math.max(first.getZ(), second.getZ()));
        }
    }
    private InnSelection() { }
    @Nullable public static Draft current(ServerPlayer player) {
        Draft plan = PLANS.get(player); return plan == null ? null : get(player, plan.anchor);
    }
    @Nullable public static Draft get(ServerPlayer player, BlockPos anchor) {
        Draft plan = PLANS.get(player);
        if (plan == null) return null;
        var level = player.serverLevel();
        if (level.getGameTime() > plan.expires || !plan.dimension.equals(level.dimension().location())
                || !level.hasChunkAt(plan.anchor) || !(level.getBlockEntity(plan.anchor) instanceof InnAnchorBlockEntity block)
                || !player.getUUID().equals(block.ownerId()) || !plan.placement.equals(block.placementId())) {
            PLANS.remove(player); return null;
        }
        return plan.anchor.equals(anchor) ? plan : null;
    }
    public static void clear(ServerPlayer player) { PLANS.remove(player); }
    public static void clearAll() { PLANS.clear(); }
    public static void mark(ServerPlayer player, BlockPos clicked, Direction face) {
        var level = player.serverLevel();
        if (!player.isAlive() || player.isSpectator() || !player.mayBuild() || !level.hasChunkAt(clicked)
                || !level.mayInteract(player, clicked) || player.getEyePosition().distanceToSqr(Vec3.atCenterOf(clicked)) > 64) return;
        if (level.getBlockEntity(clicked) instanceof InnAnchorBlockEntity block) {
            if (!player.getUUID().equals(block.ownerId())) { message(player, "not_owner"); return; }
            var status = InnService.status(player, clicked);
            Draft previous = get(player, clicked);
            if (previous != null && previous.complete() && !player.isShiftKeyDown()) { InnContent.open(player, clicked); return; }
            PLANS.put(player, new Draft(UUID.randomUUID(), level.dimension().location(), clicked.immutable(), block.placementId(),
                    status.revision(), level.getGameTime() + LIFETIME, null, null, null, null, null));
            message(player, "step.0"); return;
        }
        Draft stored = PLANS.get(player);
        Draft plan = stored == null ? null : get(player, stored.anchor);
        if (plan == null) { message(player, "bind_first"); return; }
        if (plan.complete()) { message(player, "return_to_ledger"); return; }
        BlockPos selected = (plan.step() == 2 || plan.step() == 4) ? clicked.relative(face) : clicked;
        selected = selected.immutable();
        if (selected.distSqr(plan.anchor) > 64 * 64 || !level.hasChunkAt(selected)
                || !level.getWorldBorder().isWithinBounds(selected) || level.isOutsideBuildHeight(selected)
                || !level.mayInteract(player, selected)) { message(player, "invalid_selection"); return; }
        Draft next = new Draft(UUID.randomUUID(), plan.dimension, plan.anchor, plan.placement, plan.revision, level.getGameTime() + LIFETIME,
                plan.step() == 0 ? selected : plan.first, plan.step() == 1 ? selected : plan.second,
                plan.step() == 2 ? selected : plan.entrance, plan.step() == 3 ? selected : plan.stock,
                plan.step() == 4 ? selected : plan.arrival);
        if (next.second != null) try { next.bounds(); } catch (IllegalArgumentException invalid) { message(player, "bounds_limit"); return; }
        PLANS.put(player, next);
        message(player, "step." + next.step());
    }
    private static void message(ServerPlayer player, String key) {
        player.displayClientMessage(Component.translatable("inn.dungeonsalive." + key), false);
    }
}
