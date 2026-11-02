package de.laugenbreze.dungeonsalive.settlement;

import de.laugenbreze.dungeonsalive.entity.peaceful.DungeonsMerchant;
import de.laugenbreze.dungeonsalive.entity.peaceful.ModPeacefulEntities;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

/** A synchronous two-resident placement transaction; no construction, teleport, lookup or replenishment loop. */
public final class SettlementService {
    public static final String SETTLER_TAG = "dungeonsalive_settler";
    public static final String SETTLEMENT_KEY = "DungeonsSettlement", OWNER_KEY = "DungeonsSettlementOwner";
    public static final int LOCAL_MERCHANT_LIMIT = 6;
    private static final int[][] SPOTS = {{-3, 0}, {3, 0}, {0, -3}, {0, 3}, {-3, -3}, {3, 3}, {-3, 3}, {3, -3}};
    private SettlementService() {}

    public static InteractionResult found(UseOnContext context) {
        if (!(context.getLevel() instanceof ServerLevel level) || !(context.getPlayer() instanceof ServerPlayer player)) return InteractionResult.FAIL;
        BlockPos floor = context.getClickedPos(), anchor = floor.above();
        if (context.getItemInHand().isEmpty() || !(context.getItemInHand().getItem() instanceof SettlementCharterItem)
                || context.getClickedFace() != Direction.UP || player.isSpectator() || !player.mayBuild()
                || player.position().distanceToSqr(Vec3.atCenterOf(floor)) > 64
                || !level.mayInteract(player, floor) || !player.mayUseItemAt(anchor, Direction.UP, context.getItemInHand()))
            return fail(player, "rights");
        if (!loaded(level, anchor, 6) || !level.getWorldBorder().isWithinBounds(anchor)
                || !level.getBlockState(floor).isFaceSturdy(level, floor, Direction.UP)) return fail(player, "space");
        var saved = SettlementSavedData.get(level.getServer());
        var decision = saved.check(player.getUUID(), level.dimension().location().toString(), anchor.getX(), anchor.getZ());
        if (decision != SettlementRegistry.Decision.AVAILABLE) return fail(player, decision.name().toLowerCase(Locale.ROOT));
        if (level.getEntitiesOfClass(DungeonsMerchant.class, new AABB(anchor.getX() - 32, anchor.getY() - 16, anchor.getZ() - 32,
                anchor.getX() + 33, anchor.getY() + 17, anchor.getZ() + 33), LivingEntity::isAlive).size() + 2 > LOCAL_MERCHANT_LIMIT)
            return fail(player, "crowded");
        UUID identity = UUID.randomUUID();
        List<DungeonsMerchant> residents = new ArrayList<>();
        for (String role : List.of("village_merchant", "blacksmith")) {
            var resident = ModPeacefulEntities.merchantType(role).create(level);
            if (resident == null) return fail(player, "space");
            boolean placed = false;
            for (int[] offset : SPOTS) {
                BlockPos position = anchor.offset(offset[0], 0, offset[1]);
                resident.setPos(Vec3.atBottomCenterOf(position));
                if (safeSpot(level, player, resident, position, context) && residents.stream()
                        .noneMatch(other -> other.getBoundingBox().inflate(.5).intersects(resident.getBoundingBox()))) {
                    placed = true; break;
                }
            }
            if (!placed) return fail(player, "space");
            resident.addTag(DungeonsMerchant.CAMP_TAG); resident.addTag(SETTLER_TAG); resident.setPersistenceRequired();
            resident.getPersistentData().putUUID(SETTLEMENT_KEY, identity);
            resident.getPersistentData().putUUID(OWNER_KEY, player.getUUID());
            residents.add(resident);
        }
        List<DungeonsMerchant> admitted = new ArrayList<>();
        for (var resident : residents) {
            if (!level.addFreshEntity(resident)) { admitted.forEach(DungeonsMerchant::discard); return fail(player, "space"); }
            admitted.add(resident);
        }
        if (admitted.stream().anyMatch(resident -> !resident.isAlive() || resident.isRemoved())) {
            admitted.forEach(DungeonsMerchant::discard); return fail(player, "space");
        }
        var record = new SettlementRegistry.Anchor(identity, player.getUUID(), level.dimension().location().toString(),
                anchor.getX(), anchor.getY(), anchor.getZ(), residents.stream().map(DungeonsMerchant::getUUID).toList());
        if (!saved.add(record)) { admitted.forEach(DungeonsMerchant::discard); return fail(player, "nearby"); }
        var item = context.getItemInHand().getItem();
        context.getItemInHand().consume(1, player);
        player.awardStat(Stats.ITEM_USED.get(item));
        player.displayClientMessage(Component.translatable("message.dungeonsalive.settlement.founded"), false);
        level.playSound(null, anchor, SoundEvents.BELL_BLOCK, SoundSource.PLAYERS, .7f, 1.2f);
        return InteractionResult.CONSUME;
    }

    private static boolean safeSpot(ServerLevel level, ServerPlayer player, DungeonsMerchant resident, BlockPos position, UseOnContext context) {
        if (!loaded(level, position, 2) || position.getY() <= level.getMinBuildHeight()
                || position.getY() + resident.getBbHeight() >= level.getMaxBuildHeight()
                || !level.getWorldBorder().isWithinBounds(resident.getBoundingBox())
                || !level.mayInteract(player, position.below()) || !player.mayUseItemAt(position, Direction.UP, context.getItemInHand())) return false;
        var support = level.getBlockState(position.below());
        if (!support.isFaceSturdy(level, position.below(), Direction.UP)) return false;
        for (int y = -1; y <= Math.ceil(resident.getBbHeight()); y++) {
            var state = level.getBlockState(position.above(y));
            if (!state.getFluidState().isEmpty() || state.is(Blocks.MAGMA_BLOCK) || state.is(Blocks.CACTUS)
                    || state.is(Blocks.FIRE) || state.is(Blocks.SOUL_FIRE) || state.is(Blocks.CAMPFIRE) || state.is(Blocks.SOUL_CAMPFIRE)
                    || state.is(Blocks.WITHER_ROSE) || state.is(Blocks.SWEET_BERRY_BUSH) || state.is(Blocks.POWDER_SNOW)) return false;
        }
        return level.noCollision(resident, resident.getBoundingBox()) && !level.containsAnyLiquid(resident.getBoundingBox())
                && level.getEntitiesOfClass(LivingEntity.class, resident.getBoundingBox(), LivingEntity::isAlive).isEmpty();
    }

    private static boolean loaded(ServerLevel level, BlockPos position, int radius) {
        for (int x = (position.getX() - radius) >> 4; x <= (position.getX() + radius) >> 4; x++)
            for (int z = (position.getZ() - radius) >> 4; z <= (position.getZ() + radius) >> 4; z++)
                if (!level.hasChunk(x, z)) return false;
        return true;
    }
    private static InteractionResult fail(ServerPlayer player, String reason) {
        player.displayClientMessage(Component.translatable("message.dungeonsalive.settlement." + reason), true);
        return InteractionResult.FAIL;
    }
}
