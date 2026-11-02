package de.laugenbreze.dungeonsalive.village;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.schedule.Activity;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.server.ServerStoppedEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.Map;
import java.util.WeakHashMap;

/** Observes vanilla AI; does not change jobs, move items or simulate crafting. */
public final class VillageAnimationEvents {
    private static final Map<Villager, VillageActivity.State> LAST_SENT = new WeakHashMap<>();
    private VillageAnimationEvents() {}

    public static void register() {
        NeoForge.EVENT_BUS.addListener(VillageAnimationEvents::tick);
        NeoForge.EVENT_BUS.addListener(VillageAnimationEvents::tracking);
        NeoForge.EVENT_BUS.addListener(VillageAnimationEvents::stopped);
    }

    private static void tick(EntityTickEvent.Post event) {
        if (!(event.getEntity() instanceof Villager villager) || !(villager.level() instanceof ServerLevel)
                || (villager.tickCount + villager.getId()) % 5 != 0) return;
        VillageActivity.State next = observe(villager);
        VillageActivity.State previous = LAST_SENT.get(villager);
        if (previous != null && previous.mode() == next.mode()) {
            if (next.mode() == VillageActivity.IDLE) return;
            if (next.mode() == VillageActivity.WORK && next.startedAt() == previous.startedAt()) return;
            if (next.mode() != VillageActivity.WORK && next.startedAt() - previous.startedAt() < 60) return;
        }
        if (previous == null && next.mode() == VillageActivity.IDLE) return;
        LAST_SENT.put(villager, next);
        PacketDistributor.sendToPlayersTrackingEntity(villager, payload(villager, next));
    }

    private static void tracking(PlayerEvent.StartTracking event) {
        if (event.getTarget() instanceof Villager villager && event.getEntity() instanceof ServerPlayer player) {
            VillageActivity.State state = observe(villager);
            if (state.mode() != VillageActivity.IDLE) PacketDistributor.sendToPlayer(player, payload(villager, state));
        }
    }

    /** Exposed for server GameTests; reads only already-loaded block data. */
    public static VillageActivity.State observe(Villager villager) {
        if (!(villager.level() instanceof ServerLevel level)) return VillageActivity.State.idle();
        var brain = villager.getBrain();
        var job = brain.getMemory(MemoryModuleType.JOB_SITE);
        boolean validJob = job.filter(pos -> pos.dimension().equals(level.dimension())
                        && level.hasChunkAt(pos.pos()) && pos.pos().closerToCenterThan(villager.position(), 1.8)
                        && level.getPoiManager().getType(pos.pos())
                            .filter(villager.getVillagerData().getProfession().heldJobSite()).isPresent())
                .isPresent();
        return VillageActivity.select(new VillageActivity.Observation(villager.isAlive(), villager.isBaby(),
                villager.isSleeping(), villager.isNoAi(), brain.isActive(Activity.PANIC), villager.isTrading(),
                brain.isActive(Activity.WORK), validJob, villager.getDeltaMovement().horizontalDistanceSqr() < 0.0025,
                brain.getMemory(MemoryModuleType.LAST_WORKED_AT_POI).orElse(-1L), level.getGameTime()));
    }

    private static VillageAnimationPayload payload(Villager villager, VillageActivity.State state) {
        return new VillageAnimationPayload(villager.getId(), villager.getUUID(), state.mode(), state.startedAt(), state.duration());
    }
    private static void stopped(ServerStoppedEvent event) { LAST_SENT.clear(); }
}
