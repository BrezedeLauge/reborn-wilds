package de.laugenbreze.dungeonsalive.quest;

import de.laugenbreze.dungeonsalive.entity.DungeonMob;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.server.ServerStoppedEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

public final class QuestEvents {
    private QuestEvents() {}
    public static void register() {
        NeoForge.EVENT_BUS.addListener(QuestEvents::reload);
        NeoForge.EVENT_BUS.addListener(QuestEvents::commands);
        NeoForge.EVENT_BUS.addListener(QuestEvents::login);
        NeoForge.EVENT_BUS.addListener(QuestEvents::logout);
        NeoForge.EVENT_BUS.addListener(QuestEvents::tick);
        NeoForge.EVENT_BUS.addListener(EventPriority.LOWEST, QuestEvents::death);
        NeoForge.EVENT_BUS.addListener(QuestEvents::stopped);
    }
    private static void reload(AddReloadListenerEvent event) { event.addListener(QuestService.repository()); }
    private static void commands(RegisterCommandsEvent event) { QuestCommands.register(event.getDispatcher()); }
    private static void login(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) QuestService.login(player);
    }
    private static void logout(PlayerEvent.PlayerLoggedOutEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) QuestService.disconnect(player);
    }
    private static void tick(PlayerTickEvent.Post event) {
        if (event.getEntity() instanceof ServerPlayer player && player.tickCount % 20 == 0) QuestService.tick(player);
    }
    private static void death(LivingDeathEvent event) {
        // Temporary reinforcements are part of their summoner's encounter, not new quest kills.
        if (event.getEntity() instanceof DungeonMob mob && mob.isSummoned()) return;
        if (!event.isCanceled() && event.getSource().getEntity() instanceof ServerPlayer player) {
            QuestService.killed(player, BuiltInRegistries.ENTITY_TYPE.getKey(event.getEntity().getType()));
        }
    }
    private static void stopped(ServerStoppedEvent event) { QuestService.resetSession(); }
}
