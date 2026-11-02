package de.laugenbreze.dungeonsalive.client.dev;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.chunk.SectionRenderDispatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ChunkTrackingView;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.status.ChunkStatus;

/** Read-only client readiness probe, called only after an isolated QA run is authorized. */
final class NativeCaptureTerrainReadiness {
    private static Field visibleSectionsField;
    private final StabilityWindow stability = new StabilityWindow();
    private Map<String, Object> snapshot = Map.of();

    void reset() {
        stability.reset(); snapshot = Map.of();
    }

    Map<String, Object> snapshot() { return snapshot; }

    boolean sample(Minecraft client) throws ReflectiveOperationException {
        int distance = client.options.getEffectiveRenderDistance();
        var tracking = ChunkTrackingView.of(client.player.chunkPosition(), distance);
        var expected = new ArrayList<ChunkPos>();
        tracking.forEach(expected::add);
        var missingChunks = new ArrayList<String>();
        for (var chunk : expected) {
            // false is essential: a cache miss must not become the client's empty placeholder.
            if (client.level.getChunkSource().getChunk(chunk.x, chunk.z, ChunkStatus.FULL, false) == null)
                missingChunks.add(chunk.x + "," + chunk.z);
        }

        // 1.21.1 exposes individual section readiness and the dispatcher, but not the
        // occlusion/frustum-selected section list. Inspect that exact list read-only in
        // this authorized QA harness; do not demand every underground/back-facing section.
        // A renamed/inaccessible field fails the capture instead of silently weakening it.
        if (visibleSectionsField == null) {
            visibleSectionsField = LevelRenderer.class.getDeclaredField("visibleSections");
            visibleSectionsField.setAccessible(true);
        }
        Object selected = visibleSectionsField.get(client.levelRenderer);
        if (!(selected instanceof Iterable<?> sections))
            throw new IllegalStateException("Unexpected 1.21.1 visible-section collection");
        var frustum = client.levelRenderer.getFrustum();
        var compiled = new LinkedHashMap<Long, SectionRenderDispatcher.CompiledSection>();
        var pendingSections = new ArrayList<String>();
        int renderable = 0, uncompiled = 0;
        for (Object value : sections) {
            if (!(value instanceof SectionRenderDispatcher.RenderSection section))
                throw new IllegalStateException("Unexpected 1.21.1 visible-section entry");
            BlockPos origin = section.getOrigin();
            if (!tracking.isInViewDistance(origin.getX() >> 4, origin.getZ() >> 4)
                    || frustum == null || !frustum.isVisible(section.getBoundingBox())) continue;
            var state = section.getCompiled();
            compiled.put(origin.asLong(), state);
            boolean hasCompiled = state != SectionRenderDispatcher.CompiledSection.UNCOMPILED
                    && client.levelRenderer.isSectionCompiled(origin);
            if (!hasCompiled) uncompiled++;
            if (!hasCompiled || section.isDirty()) pendingSections.add(origin.toShortString());
            if (hasCompiled && !state.hasNoRenderableLayers()) renderable++;
        }
        var dispatcher = client.levelRenderer.getSectionRenderDispatcher();
        boolean queuesEmpty = client.levelRenderer.hasRenderedAllSections();
        boolean coverageComplete = missingChunks.isEmpty() && uncompiled == 0 && renderable > 0;
        boolean meshesCurrent = pendingSections.isEmpty() && queuesEmpty;
        long now = System.nanoTime();
        // Live corruption/soil changes legitimately replace compiled meshes. Stabilize
        // coverage and actual section POSITIONS; demand three currently quiet frames,
        // rather than requiring the world's block meshes to remain frozen for 1.25s.
        boolean ready = stability.observe(now, compiled.keySet(), coverageComplete, meshesCurrent);
        var state = new LinkedHashMap<String, Object>();
        state.put("scope", "Vanilla rounded chunk tracking view plus actual occlusion/frustum-selected compiled sections; not a visual approval");
        state.put("view_distance", distance); state.put("expected_chunks", expected.size());
        state.put("missing_chunk_count", missingChunks.size());
        state.put("missing_chunks_first_12", missingChunks.stream().limit(12).toList());
        state.put("visible_sections", compiled.size()); state.put("renderable_sections", renderable);
        state.put("pending_section_count", pendingSections.size());
        state.put("uncompiled_section_count", uncompiled);
        state.put("pending_sections_first_12", pendingSections.stream().limit(12).toList());
        state.put("compile_queue", dispatcher.getToBatchCount()); state.put("upload_queue", dispatcher.getToUpload());
        state.put("stable_frames", stability.coverageFrames); state.put("stable_millis", stability.millis(now));
        state.put("current_mesh_quiet_frames", stability.quietFrames); state.put("required_current_mesh_quiet_frames", 3);
        state.put("stability_scope", "Loaded coverage and exact visible section positions; compiled object replacement alone does not reset the window");
        state.put("required_stable_frames", 25); state.put("required_stable_millis", 1250);
        state.put("ready", ready);
        snapshot = Map.copyOf(state);
        return ready;
    }

    /** Pure temporal gate, independently exercised with changing live-mesh schedules. */
    static final class StabilityWindow {
        private Set<Long> positions = Set.of();
        private boolean started;
        private long since;
        private int coverageFrames, quietFrames;

        void reset() {
            positions = Set.of(); started = false; coverageFrames = 0; quietFrames = 0;
        }

        boolean observe(long now, Set<Long> visiblePositions, boolean coverageComplete, boolean meshesCurrent) {
            if (!coverageComplete || visiblePositions.isEmpty()) { reset(); return false; }
            if (!started || !positions.equals(visiblePositions) || now - since < 0) {
                started = true; since = now; positions = Set.copyOf(visiblePositions);
                coverageFrames = 1; quietFrames = meshesCurrent ? 1 : 0;
            } else {
                coverageFrames++;
                quietFrames = meshesCurrent ? quietFrames + 1 : 0;
            }
            return coverageFrames >= 25 && millis(now) >= 1250 && quietFrames >= 3;
        }

        long millis(long now) { return started ? (now - since) / 1_000_000 : 0; }
    }
}
