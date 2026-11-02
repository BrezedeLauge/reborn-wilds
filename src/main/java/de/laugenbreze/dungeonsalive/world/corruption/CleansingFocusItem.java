package de.laugenbreze.dungeonsalive.world.corruption;

import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.phys.Vec3;

/** One successful focus stops a region immediately; loaded natural patches heal over later server pulses. */
public final class CleansingFocusItem extends Item {
    public CleansingFocusItem(Properties properties) { super(properties); }
    @Override public InteractionResult useOn(UseOnContext context) {
        if (context.getLevel().isClientSide) return InteractionResult.SUCCESS;
        if (!(context.getLevel() instanceof ServerLevel level) || !(context.getPlayer() instanceof ServerPlayer player)
                || player.isSpectator() || !player.mayBuild() || context.getItemInHand().isEmpty()
                || !level.hasChunkAt(context.getClickedPos()) || !level.mayInteract(player, context.getClickedPos())
                || player.position().distanceToSqr(Vec3.atCenterOf(context.getClickedPos())) > 64
                || !player.mayUseItemAt(context.getClickedPos(), context.getClickedFace(), context.getItemInHand())) return InteractionResult.FAIL;
        var data = CorruptionSavedData.get(level.getServer());
        var region = data.nearby(level.dimension().location().toString(), context.getClickedPos().getX(), context.getClickedPos().getZ(), CorruptionFront.RADIUS);
        if (region == null || !data.cleanse(region)) {
            player.displayClientMessage(Component.translatable("message.dungeonsalive.corruption.no_front"), true);
            return InteractionResult.FAIL;
        }
        context.getItemInHand().consume(1, player);
        player.displayClientMessage(Component.translatable("message.dungeonsalive.corruption.cleansing"), false);
        level.playSound(null, context.getClickedPos(), SoundEvents.AMETHYST_BLOCK_CHIME, SoundSource.PLAYERS, 1, 1);
        return InteractionResult.CONSUME;
    }
    @Override public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("tooltip.dungeonsalive.cleansing_focus"));
    }
}
