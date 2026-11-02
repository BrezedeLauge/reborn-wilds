package de.laugenbreze.dungeonsalive.world.trail;

import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;

/** Actual ground crossings, not standing/jumping in place. All changes are local server decisions. */
public final class NaturalTrails {
    public static final ModConfigSpec SPEC;
    public static final ModConfigSpec.BooleanValue ENABLED;
    public static final ModConfigSpec.IntValue WEAR_STEPS, PATH_STEPS, REGROW_TICKS;
    private static final Set<String> ORIGINALS = Set.of("minecraft:grass_block", "minecraft:podzol", "minecraft:mycelium");
    private static final Map<ServerPlayer, BlockPos> LAST = new WeakHashMap<>();
    static {
        var builder = new ModConfigSpec.Builder();
        ENABLED = builder.comment("Repeated ground crossings wear natural grass/podzol/mycelium into trails; generated trails regrow.",
                "Player-placed soil recorded after this mod is installed, farmland, roads, buildings and ordinary dirt paths are excluded.")
                .define("enabled", true);
        WEAR_STEPS = builder.defineInRange("wearAfterCrossings", 6, 2, 32);
        PATH_STEPS = builder.defineInRange("pathAfterCrossings", 12, 4, 64);
        REGROW_TICKS = builder.comment("Unused paths soften after this many game ticks, then restore original ground after twice this time.",
                "Only loaded chunks are processed; disabling wear still allows recorded trails to recover.")
                .defineInRange("regrowTicks", 12000, 200, 192000);
        SPEC = builder.build();
    }
    static Set<String> originalIds() { return ORIGINALS; }
    public static void register() {
        NeoForge.EVENT_BUS.addListener(NaturalTrails::walk);
        NeoForge.EVENT_BUS.addListener(NaturalTrails::regrow);
        NeoForge.EVENT_BUS.addListener(NaturalTrails::placed);
    }
    private static void walk(EntityTickEvent.Post event) {
        if (!(event.getEntity() instanceof ServerPlayer player) || player.tickCount % 4 != 0) return;
        if (!ENABLED.get() || !player.onGround() || player.isSpectator() || !player.mayBuild()
                || player.getAbilities().flying || player.isPassenger() || player.isShiftKeyDown()) { LAST.remove(player); return; }
        BlockPos pos = player.getOnPos().immutable();
        BlockPos previous = LAST.put(player, pos);
        if (previous == null || previous.equals(pos) || !player.serverLevel().mayInteract(player, pos)) return;
        step(player.serverLevel(), pos);
    }
    static void step(ServerLevel level, BlockPos pos) {
        if (!ENABLED.get() || level.getChunkSource().getChunkNow(pos.getX() >> 4, pos.getZ() >> 4) == null
                || !level.getWorldBorder().isWithinBounds(pos) || !level.isEmptyBlock(pos.above())) return;
        var data = TrailSavedData.get(level);
        long key = pos.asLong();
        if (data.playerGround.contains(key) || data.protectionFull) return;
        BlockState state = level.getBlockState(pos);
        var traffic = data.footprints.get(key);
        if (traffic == null) {
            String id = BuiltInRegistries.BLOCK.getKey(state.getBlock()).toString();
            if (!ORIGINALS.contains(id) || data.footprints.size() >= TrailSavedData.LIMIT) return;
            traffic = new FootTraffic(id, 0, 0, level.getGameTime());
        } else if (!state.is(expected(traffic).getBlock())) { data.forget(key); return; }
        traffic.stepped(level.getGameTime());
        int desired = traffic.desiredStage(WEAR_STEPS.get(), Math.max(WEAR_STEPS.get() + 1, PATH_STEPS.get()));
        if (desired > traffic.stage() && change(level, pos, stageState(traffic, desired))) traffic.setStage(desired);
        data.put(key, traffic);
    }
    private static void regrow(LevelTickEvent.Post event) {
        if (!(event.getLevel() instanceof ServerLevel level) || level.getGameTime() % 20 != 0 || level.players().isEmpty()) return;
        recoverLoaded(level);
    }
    /** Shared server processing, also exercised on actual blocks by the native regression fixtures. */
    static void recoverLoaded(ServerLevel level) {
        var data = TrailSavedData.get(level);
        int count = Math.min(128, data.pending.size());
        for (int index = 0; index < count; index++) {
            long key = data.pending.removeFirst();
            var traffic = data.footprints.get(key);
            if (traffic == null) continue;
            BlockPos pos = BlockPos.of(key);
            if (level.getChunkSource().getChunkNow(pos.getX() >> 4, pos.getZ() >> 4) == null) { data.pending.addLast(key); continue; }
            BlockState current = level.getBlockState(pos);
            if (!current.is(expected(traffic).getBlock())) { data.forget(key); continue; }
            long previousStep = traffic.lastStep();
            int desired = traffic.recoveredStage(level.getGameTime(), REGROW_TICKS.get());
            if (previousStep != traffic.lastStep()) data.setDirty();
            if (desired < traffic.stage() && level.isEmptyBlock(pos.above()) && level.getMaxLocalRawBrightness(pos.above()) >= 4
                    && change(level, pos, stageState(traffic, desired))) { traffic.setStage(desired); data.setDirty(); }
            if (traffic.stage() == 0 && level.getGameTime() - traffic.lastStep() >= (long)REGROW_TICKS.get() * 2) data.forget(key);
            else data.pending.addLast(key);
        }
    }
    private static BlockState expected(FootTraffic traffic) { return stageState(traffic, traffic.stage()); }
    private static BlockState stageState(FootTraffic traffic, int stage) {
        return stage == 2 ? Blocks.DIRT_PATH.defaultBlockState() : stage == 1 ? Blocks.COARSE_DIRT.defaultBlockState()
                : BuiltInRegistries.BLOCK.get(ResourceLocation.parse(traffic.original())).defaultBlockState();
    }
    private static boolean change(ServerLevel level, BlockPos pos, BlockState after) {
        var event = new TrailChangeEvent(level, pos, level.getBlockState(pos), after);
        NeoForge.EVENT_BUS.post(event);
        return !event.isCanceled() && level.setBlock(pos, after, 3);
    }
    private static void placed(BlockEvent.EntityPlaceEvent event) {
        if (event.getLevel() instanceof ServerLevel level && event.getEntity() instanceof ServerPlayer) {
            var data = TrailSavedData.get(level);
            data.forget(event.getPos().asLong());
            if (ORIGINALS.contains(BuiltInRegistries.BLOCK.getKey(event.getPlacedBlock().getBlock()).toString())) data.protect(event.getPos().asLong());
        }
    }
    // Break/tool events can still be cancelled by a later listener. Forget history
    // only after step/recovery observes an actual different block in the world.
    private NaturalTrails() {}
}
