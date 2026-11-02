package de.laugenbreze.dungeonsalive.settlement.inn;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.EntityLeaveLevelEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.event.server.ServerStoppedEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import org.joml.Vector3f;

public final class InnEvents {
    private static final DustParticleOptions PLAN = new DustParticleOptions(new Vector3f(.35F, .9F, .8F), 1F);
    private InnEvents() { }
    public static void register() {
        NeoForge.EVENT_BUS.addListener(InnEvents::start); NeoForge.EVENT_BUS.addListener(InnEvents::stop);
        NeoForge.EVENT_BUS.addListener(InnEvents::tick); NeoForge.EVENT_BUS.addListener(InnEvents::removed);
        NeoForge.EVENT_BUS.addListener(InnEvents::logout); NeoForge.EVENT_BUS.addListener(InnEvents::preview);
    }
    private static void start(ServerStartingEvent event) { InnService.attach(event.getServer(), InnFurnitureContent.FURNITURE); }
    private static void stop(ServerStoppedEvent event) { InnService.detach(event.getServer()); InnSelection.clearAll(); }
    private static void tick(LevelTickEvent.Post event) { if (event.getLevel() instanceof ServerLevel level) InnService.tick(level); }
    private static void removed(EntityLeaveLevelEvent event) { InnService.removed(event.getEntity()); }
    private static void logout(PlayerEvent.PlayerLoggedOutEvent event) { if (event.getEntity() instanceof ServerPlayer player) InnSelection.clear(player); }
    /** At most48 private markers/second while actually holding the tool; never forces chunks or changes blocks. */
    private static void preview(PlayerTickEvent.Post event) {
        if (!(event.getEntity() instanceof ServerPlayer player) || player.tickCount % 20 != 0 || !player.isAlive() || player.isSpectator()
                || !player.getMainHandItem().is(InnContent.MARKER.get()) && !player.getOffhandItem().is(InnContent.MARKER.get())) return;
        var draft = InnSelection.current(player);
        if (draft == null || draft.first() == null || draft.second() == null || player.distanceToSqr(draft.anchor().getCenter()) > 64 * 64) return;
        var bounds = draft.bounds(); var level = player.serverLevel();
        // Four samples per edge: fixed cost even for the largest supported room.
        for (int axis = 0; axis < 3; axis++) for (int sideA = 0; sideA < 2; sideA++) for (int sideB = 0; sideB < 2; sideB++) for (int t = 0; t < 4; t++) {
            double[] min = { bounds.minX(), bounds.minY(), bounds.minZ() }, max = { bounds.maxX() + 1D, bounds.maxY() + 1D, bounds.maxZ() + 1D };
            double[] point = min.clone();
            point[axis] = min[axis] + (max[axis] - min[axis]) * t / 3D;
            point[(axis + 1) % 3] = sideA == 0 ? min[(axis + 1) % 3] : max[(axis + 1) % 3];
            point[(axis + 2) % 3] = sideB == 0 ? min[(axis + 2) % 3] : max[(axis + 2) % 3];
            if (level.hasChunkAt(BlockPos.containing(point[0], point[1], point[2])))
                level.sendParticles(player, PLAN, true, point[0], point[1], point[2], 1, 0, 0, 0, 0);
        }
    }
}
