package de.laugenbreze.dungeonsalive.world.keygate;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import de.laugenbreze.dungeonsalive.entity.peaceful.DungeonsCreature;
import de.laugenbreze.dungeonsalive.entity.peaceful.PeacefulProfiles;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

/** Vanilla two-block door lifecycle, with an explicit server-owned key interaction. */
public final class KeyGateBlock extends DoorBlock implements EntityBlock {
    public static final double KEY_RADIUS = 4;
    public static final MapCodec<KeyGateBlock> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.STRING.fieldOf("key_golem").forGetter((KeyGateBlock block) -> block.requiredGolem),
            propertiesCodec()).apply(instance, KeyGateBlock::new));
    private final String requiredGolem;

    public KeyGateBlock(String requiredGolem, Properties properties) {
        super(BlockSetType.IRON, properties);
        if (!requiredGolem.equals("key_golem") && !requiredGolem.equals("diamond_key_golem"))
            throw new IllegalArgumentException("Unknown gate key: " + requiredGolem);
        this.requiredGolem = requiredGolem;
    }
    @Override public MapCodec<KeyGateBlock> codec() { return CODEC; }
    public String requiredGolem() { return requiredGolem; }
    @Override @Nullable public BlockEntity newBlockEntity(BlockPos position, BlockState state) {
        return state.getValue(HALF) == DoubleBlockHalf.LOWER ? new KeyGateBlockEntity(position, state) : null;
    }
    @Override @Nullable public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockPos position = context.getClickedPos();
        // Vanilla chooses a hinge from adjacent blocks; do not ask it to inspect an
        // unloaded neighbour at the edge of the currently available world.
        if (!loadedRectangle(context.getLevel(), position.getX() - 1, position.getX() + 1,
                position.getZ() - 1, position.getZ() + 1)) return null;
        BlockState state = super.getStateForPlacement(context);
        // Placing against an already-powered block must not create an unlocked gate.
        return state == null ? null : state.setValue(OPEN, false).setValue(POWERED, false);
    }
    @Override public void setPlacedBy(Level level, BlockPos position, BlockState state,
                                       @Nullable LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(level, position, state, placer, stack);
        if (!level.isClientSide && level.getBlockEntity(position) instanceof KeyGateBlockEntity gate)
            gate.placedBy(placer instanceof Player player ? player.getUUID() : null);
    }
    @Override protected void neighborChanged(BlockState state, Level level, BlockPos position,
                                               Block neighbor, BlockPos neighborPosition, boolean movedByPiston) {
        // DoorBlock's neighbor callback only switches redstone power. Structural support and
        // matching upper/lower-half removal remain in the inherited updateShape lifecycle.
    }
    @Override protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos position,
                                                          Player player, BlockHitResult hit) {
        if (level.isClientSide) return InteractionResult.SUCCESS;
        BlockPos lower = lowerPosition(state, position);
        String rejection = rejection(level, lower, player, true);
        if (rejection != null) {
            player.displayClientMessage(Component.translatable("key_gate.dungeonsalive." + rejection), true);
            return InteractionResult.CONSUME;
        }
        BlockState current = level.getBlockState(lower);
        super.setOpen(player, level, current, lower, !current.getValue(OPEN));
        return InteractionResult.CONSUME;
    }
    @Override public void setOpen(@Nullable Entity actor, Level level, BlockState state, BlockPos position, boolean open) {
        // Do not expose DoorBlock's unvalidated mutator to ordinary mob door goals.
        if (!level.isClientSide && actor instanceof Player player) {
            BlockPos lower = lowerPosition(state, position);
            if (rejection(level, lower, player, open) == null)
                super.setOpen(player, level, level.getBlockState(lower), lower, open);
        }
    }
    private static BlockPos lowerPosition(BlockState state, BlockPos position) {
        return state.getValue(HALF) == DoubleBlockHalf.UPPER ? position.below() : position;
    }
    @Nullable private String rejection(Level level, BlockPos lower, Player player, boolean openingRequested) {
        if (!level.hasChunkAt(lower) || !level.hasChunkAt(lower.above()) || player.level() != level
                || !player.isAlive() || player.isSpectator() || player.distanceToSqr(Vec3.atCenterOf(lower)) > 36
                || !level.mayInteract(player, lower)) return "unavailable";
        BlockState bottom = level.getBlockState(lower), upper = level.getBlockState(lower.above());
        if (!bottom.is(this) || bottom.getValue(HALF) != DoubleBlockHalf.LOWER
                || !upper.is(this) || upper.getValue(HALF) != DoubleBlockHalf.UPPER
                || !(level.getBlockEntity(lower) instanceof KeyGateBlockEntity gate)) return "unavailable";
        if (gate.ownerId() == null) return "unassigned";
        if (!gate.ownerId().equals(player.getUUID())) return "not_owner";
        if (!visibleGate(level, player, lower)) return "unavailable";
        // Owners can close their own gate after the key has left. Reopening needs the key again.
        if (!openingRequested || bottom.getValue(OPEN)) return null;
        Vec3 center = Vec3.atBottomCenterOf(lower);
        for (DungeonsCreature key : level.getEntitiesOfClass(DungeonsCreature.class, new AABB(lower).inflate(KEY_RADIUS),
                candidate -> candidate.isAlive() && !candidate.isRemoved() && candidate.level() == level
                        && candidate.profile().kind() == PeacefulProfiles.Kind.KEY_GOLEM
                        && candidate.profile().id().equals(requiredGolem) && player.getUUID().equals(candidate.ownerId())
                        && candidate.distanceToSqr(center) <= KEY_RADIUS * KEY_RADIUS)) {
            if (visibleGate(level, key, lower)) return null;
        }
        return "requires_" + requiredGolem;
    }
    private static boolean visibleGate(Level level, LivingEntity viewer, BlockPos lower) {
        Vec3 from = viewer.getEyePosition(), target = Vec3.atCenterOf(lower).add(0, .5, 0);
        // Clip only after checking the whole short ray's horizontal chunk rectangle. Entity
        // searches inspect loaded sections; neither queries nor this ray may force a chunk.
        int minX = BlockPos.containing(Math.min(from.x, target.x), 0, 0).getX() - 1;
        int maxX = BlockPos.containing(Math.max(from.x, target.x), 0, 0).getX() + 1;
        int minZ = BlockPos.containing(0, 0, Math.min(from.z, target.z)).getZ() - 1;
        int maxZ = BlockPos.containing(0, 0, Math.max(from.z, target.z)).getZ() + 1;
        if (!loadedRectangle(level, minX, maxX, minZ, maxZ)) return false;
        BlockHitResult hit = level.clip(new ClipContext(from, target, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, viewer));
        return hit.getType() == HitResult.Type.MISS || hit.getBlockPos().equals(lower) || hit.getBlockPos().equals(lower.above());
    }
    private static boolean loadedRectangle(Level level, int minBlockX, int maxBlockX, int minBlockZ, int maxBlockZ) {
        int minX = minBlockX >> 4, maxX = maxBlockX >> 4, minZ = minBlockZ >> 4, maxZ = maxBlockZ >> 4;
        if (maxX - minX > 1 || maxZ - minZ > 1) return false;
        for (int x = minX; x <= maxX; x++) for (int z = minZ; z <= maxZ; z++) if (!level.hasChunk(x, z)) return false;
        return true;
    }
}
