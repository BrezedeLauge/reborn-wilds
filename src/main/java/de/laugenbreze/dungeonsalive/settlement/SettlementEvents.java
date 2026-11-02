package de.laugenbreze.dungeonsalive.settlement;

import de.laugenbreze.dungeonsalive.entity.DungeonMob;
import java.util.function.Supplier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;

/** Real authorized boss drops, once per stored identity; cancelled deaths never reach this hook. */
public final class SettlementEvents {
    private SettlementEvents() {}
    public static void register(Supplier<? extends Item> charter) {
        NeoForge.EVENT_BUS.addListener(EventPriority.LOWEST, (LivingDropsEvent event) -> reward(event, charter));
    }
    private static void reward(LivingDropsEvent event, Supplier<? extends Item> charter) {
        if (event.isCanceled() || !(event.getEntity() instanceof DungeonMob boss) || !boss.profile().boss() || boss.isSummoned()
                || !(boss.level() instanceof ServerLevel level) || !(event.getSource().getEntity() instanceof ServerPlayer player)
                || player.isSpectator() || boss.isAlliedTo(player) || player.isAlliedTo(boss)
                || !level.getGameRules().getBoolean(GameRules.RULE_DOMOBLOOT)) return;
        if (!SettlementSavedData.get(level.getServer()).claimReward(boss.getUUID())) return;
        var drop = new ItemEntity(level, boss.getX(), boss.getY() + .5, boss.getZ(), new ItemStack(charter.get()));
        drop.setDefaultPickUpDelay();
        event.getDrops().add(drop); // Normal capture/drop cancellation, never an inventory injection.
    }
}
