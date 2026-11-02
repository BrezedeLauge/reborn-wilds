package de.laugenbreze.dungeonsalive.item.gear;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.entity.projectile.SpectralArrow;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

/** New arrows consume their cast charge only after an accepted spawn, before their first movement. */
public final class ArtifactExpansionEvents {
    private static final String PENDING = "DungeonsAliveQuiverPending";
    private static final String PROCESSED = "DungeonsAliveQuiverProcessed";

    private ArtifactExpansionEvents() {}

    public static void register() {
        NeoForge.EVENT_BUS.addListener(EventPriority.LOWEST, ArtifactExpansionEvents::projectileJoined);
        NeoForge.EVENT_BUS.addListener(EventPriority.LOWEST, ArtifactExpansionEvents::projectileTick);
        NeoForge.EVENT_BUS.addListener(ArtifactExpansionEvents::clonePlayer);
    }

    public static void projectileJoined(EntityJoinLevelEvent event) {
        if (event.isCanceled() || event.getLevel().isClientSide || !supportedArrow(event.getEntity())) return;
        var data = event.getEntity().getPersistentData();
        if (event.loadedFromDisk()) {
            data.remove(PENDING);
            data.putBoolean(PROCESSED, true);
        } else if (event.getEntity().tickCount == 0 && !data.getBoolean(PROCESSED)) {
            // No owner/world lookup in join callbacks: their chunks may not yet be FULL.
            // A cancelled spawn never reaches the server tick and spends no charge.
            data.putBoolean(PENDING, true);
        }
    }

    public static void projectileTick(EntityTickEvent.Pre event) {
        if (event.isCanceled() || event.getEntity().level().isClientSide || !supportedArrow(event.getEntity())) return;
        var arrow = (AbstractArrow) event.getEntity();
        var data = arrow.getPersistentData();
        if (!data.getBoolean(PENDING) || data.getBoolean(PROCESSED)) return;
        data.remove(PENDING);
        data.putBoolean(PROCESSED, true);
        if (arrow.getOwner() instanceof Player player && player.level() == arrow.level()
                && ArtifactExpansionState.consumeArrow(player)) {
            arrow.setRemainingFireTicks(Math.max(arrow.getRemainingFireTicks(), 100));
        }
    }

    public static void clonePlayer(PlayerEvent.Clone event) {
        ArtifactExpansionState.copyOnClone(event.getOriginal(), event.getEntity(), event.isWasDeath());
    }

    private static boolean supportedArrow(Entity entity) {
        // Tridents and arbitrary magic projectiles are not flaming-quiver ammunition.
        return entity instanceof Arrow || entity instanceof SpectralArrow;
    }
}
