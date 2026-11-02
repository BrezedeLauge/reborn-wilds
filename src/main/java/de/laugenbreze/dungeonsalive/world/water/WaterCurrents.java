package de.laugenbreze.dungeonsalive.world.water;

import de.laugenbreze.dungeonsalive.network.ClientCurrentBridge;
import de.laugenbreze.dungeonsalive.network.CurrentImpulsePayload;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.LinkedHashMap;
import java.util.UUID;
import java.lang.ref.WeakReference;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.vehicle.Boat;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import net.neoforged.neoforge.event.server.ServerStoppedEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;

public final class WaterCurrents {
    public static final ModConfigSpec SPEC;
    public static final ModConfigSpec.BooleanValue ENABLED;
    public static final ModConfigSpec.DoubleValue STRENGTH;
    private static final Map<ServerLevel, Budget> BUDGETS = new WeakHashMap<>();
    static {
        var builder = new ModConfigSpec.Builder();
        ENABLED = builder.comment("Local channel currents for swimmers, loose items and boats. Water blocks stay vanilla.").define("enabled", true);
        STRENGTH = builder.defineInRange("strength", 1.0, 0.0, 2.0);
        SPEC = builder.build();
    }
    private WaterCurrents() {}
    public static void register() {
        NeoForge.EVENT_BUS.addListener(WaterCurrents::collect);
        NeoForge.EVENT_BUS.addListener(WaterCurrents::apply);
        NeoForge.EVENT_BUS.addListener(WaterCurrents::shutdown);
    }
    public static void networking(RegisterPayloadHandlersEvent event) {
        event.registrar("1").playToClient(CurrentImpulsePayload.TYPE, CurrentImpulsePayload.CODEC,
                (payload, context) -> context.enqueueWork(() -> ClientCurrentBridge.receive(payload)));
    }
    private static void collect(EntityTickEvent.Post event) {
        Entity entity = event.getEntity();
        if (!(entity.level() instanceof ServerLevel level) || !ENABLED.get() || STRENGTH.get() == 0
                || !eligible(entity) || !wetCenter(level, entity)) return;
        Budget budget = BUDGETS.computeIfAbsent(level, ignored -> new Budget());
        if (level.getGameTime() < budget.nextDue.getOrDefault(entity, Long.MIN_VALUE)) return;
        budget.queue.offer(entity.getId(), controller(entity) != null);
    }
    private static void apply(LevelTickEvent.Post event) {
        if (!(event.getLevel() instanceof ServerLevel level)) return;
        Budget budget = BUDGETS.get(level);
        if (budget == null) return;
        var selected = budget.queue.drain();
        if (!ENABLED.get() || STRENGTH.get() == 0) return;
        for (int id : selected) {
            Entity entity = level.getEntity(id); // Only already loaded entities; no chunk lookup/load.
            if (entity == null || !eligible(entity) || !wetCenter(level, entity)) continue;
            budget.nextDue.put(entity, level.getGameTime() + 5);
            impulse(level, entity, budget);
        }
    }
    private static void impulse(ServerLevel level, Entity entity, Budget budget) {
        BlockPos water = BlockPos.containing(entity.getX(), entity.getBoundingBox().minY + .05, entity.getZ());
        var sample = WaterCurrentField.sample(level, water);
        double downstream = entity.getDeltaMovement().dot(sample.direction());
        ServerPlayer controller = controller(entity);
        if (entity instanceof Boat && controller != null) {
            var history = budget.history(entity);
            var velocity = history.observe(level.getGameTime(), entity.getX(), entity.getY(), entity.getZ());
            if (!velocity.measured()) return;
            downstream = velocity.x() * sample.direction().x + velocity.z() * sample.direction().z;
        }
        if (sample.speed() == 0) return;
        double impulse = CurrentStrength.impulse(sample.speed() * STRENGTH.get(), downstream, 5);
        var delta = sample.direction().scale(impulse);
        if (delta.lengthSqr() < .0000001) return;
        entity.setDeltaMovement(entity.getDeltaMovement().add(delta));
        // Player and piloted boat movement is locally controlled; send an additive impulse, not an old full velocity.
        if (controller != null) PacketDistributor.sendToPlayer(controller, new CurrentImpulsePayload(entity.getId(), (float) delta.x, (float) delta.z));
        else entity.hurtMarked = true;
    }
    private static ServerPlayer controller(Entity entity) {
        return entity instanceof ServerPlayer player ? player : entity.getControllingPassenger() instanceof ServerPlayer player ? player : null;
    }
    private static boolean wetCenter(ServerLevel level, Entity entity) {
        BlockPos pos = BlockPos.containing(entity.getX(), entity.getBoundingBox().minY + .05, entity.getZ());
        return level.hasChunkAt(pos) && level.getFluidState(pos).is(FluidTags.WATER);
    }
    static boolean eligible(Entity entity) {
        if (!entity.isAlive() || entity.isRemoved() || entity.isPassenger() || entity.isSpectator()
                || !(entity instanceof LivingEntity || entity instanceof ItemEntity || entity instanceof Boat)) return false;
        if (entity instanceof ServerPlayer player && (player.getAbilities().flying || player.isSleeping())) return false;
        return entity.isInWater() || entity instanceof Boat;
    }
    private static void shutdown(ServerStoppedEvent event) { BUDGETS.clear(); }
    private record Motion(WeakReference<Entity> entity, CurrentMotionHistory history) {}
    private static final class Budget {
        final CurrentWorkQueue queue = new CurrentWorkQueue();
        final Map<Entity, Long> nextDue = new WeakHashMap<>();
        final Map<UUID, Motion> boatMotion = new LinkedHashMap<>(16, .75f, true);
        CurrentMotionHistory history(Entity entity) {
            Motion existing = boatMotion.get(entity.getUUID());
            if (existing != null && existing.entity.get() == entity) return existing.history;
            if (boatMotion.size() >= 1024) boatMotion.remove(boatMotion.keySet().iterator().next());
            var history = new CurrentMotionHistory();
            boatMotion.put(entity.getUUID(), new Motion(new WeakReference<>(entity), history));
            return history;
        }
    }
}
