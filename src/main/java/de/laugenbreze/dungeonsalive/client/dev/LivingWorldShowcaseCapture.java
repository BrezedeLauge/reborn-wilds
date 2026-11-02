package de.laugenbreze.dungeonsalive.client.dev;

import com.google.gson.GsonBuilder;
import de.laugenbreze.dungeonsalive.DungeonsAlive;
import de.laugenbreze.dungeonsalive.client.FieldGuideScreen;
import de.laugenbreze.dungeonsalive.client.GearGuideScreen;
import de.laugenbreze.dungeonsalive.client.JournalScreen;
import de.laugenbreze.dungeonsalive.client.MerchantServiceScreen;
import de.laugenbreze.dungeonsalive.entity.peaceful.ModPeacefulEntities;
import de.laugenbreze.dungeonsalive.merchant.MerchantServices;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Screenshot;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.storage.LevelResource;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.HitResult;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.client.event.RenderFrameEvent;
import net.neoforged.neoforge.common.NeoForge;

/** Explicit isolated capture; production additionally requires both flags and the one-shot Prism QA profile. */
public final class LivingWorldShowcaseCapture {
    private static final Path PRISM_QA_SUFFIX = Path.of("run-prism", "instances", "Dungeons-Alive-Native-QA", "minecraft");
    private static final String PRISM_QA_TOKEN = "isolated-dungeonsalive-native-prism-qa-v1";
    private static final String QA_WORLD = "Dungeons Alive Living Showcase";
    private static final List<String> BIOMES = List.of("amber_grove", "luminous_marsh", "ember_crags", "blighted_thicket");
    private static final List<String> SITES = List.of("overgrown_arena", "redstone_mineyard", "sunken_shrine");
    private static final Map<String, BlockPos> LOCATIONS = new LinkedHashMap<>();
    private static final List<Map<String, Object>> CAPTURES = new ArrayList<>();
    private static final List<Map<String, Object>> DIAGNOSTICS = new ArrayList<>();
    private static MinecraftServer server;
    private static UUID playerId;
    private static Path output;
    private static volatile boolean ready;
    private static boolean started, staged, stopped;
    private static boolean cameraInitialized;
    private static int clientCameraInitializations;
    private static int scene, frames;
    private static Vec3 camera;
    private static Map<String, Object> metadata = Map.of();
    private static volatile String phase = "not_started";
    private static volatile long phaseStarted;
    private static long runStarted, lastDiagnostic;
    private static final NativeCaptureTerrainReadiness TERRAIN = new NativeCaptureTerrainReadiness();
    private LivingWorldShowcaseCapture() {}

    public static void register() {
        if (!Boolean.getBoolean("dungeonsalive.livingShowcase")
                || FMLEnvironment.production && !Boolean.getBoolean("dungeonsalive.isolatedPrismQa")) return;
        NeoForge.EVENT_BUS.addListener(LivingWorldShowcaseCapture::frame);
    }
    private static void frame(RenderFrameEvent.Post event) {
        var client = Minecraft.getInstance();
        if (stopped || client.level == null || client.player == null || client.getSingleplayerServer() == null) return;
        try {
            if (!started) {
                Path run;
                try { run = authorizedRun(client); }
                catch (Exception rejected) {
                    // Refusing capture must never stop or alter an unrelated client session.
                    stopped = true;
                    DungeonsAlive.LOGGER.error("Living showcase refused an unapproved run/world; client remains untouched", rejected);
                    return;
                }
                server = client.getSingleplayerServer(); playerId = client.player.getUUID(); started = true;
                runStarted = System.nanoTime(); lastDiagnostic = runStarted; step("find_biomes");
                output = run.resolve("captures"); Files.createDirectories(output);
                client.options.fov().set(60);
                server.execute(() -> {
                    try { findBiomes(server.overworld()); client.execute(() -> { step("biomes_ready"); ready = true; }); }
                    catch (Exception failure) { client.execute(() -> fail(client, failure)); }
                });
                return;
            }
            long now = System.nanoTime();
            if (now - lastDiagnostic >= 15_000_000_000L) {
                lastDiagnostic = now;
                DungeonsAlive.LOGGER.info("Living showcase progress: {}", diagnostic(client));
            }
            if (DIAGNOSTICS.isEmpty() && now - phaseStarted >= 15_000_000_000L) {
                Path path = output.resolve(String.format("diagnostic-stall-scene-%02d.png", scene));
                try (var pixels = Screenshot.takeScreenshot(client.getMainRenderTarget())) { pixels.writeToFile(path); }
                DIAGNOSTICS.add(Map.of("file", path.getFileName().toString(), "sha256",
                        java.util.HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256").digest(Files.readAllBytes(path))),
                        "kind", "unaltered_framebuffer_at_wait_timeout_diagnostic_not_accepted_scene", "state", diagnostic(client)));
                writeReport("waiting", null);
                DungeonsAlive.LOGGER.info("Living showcase saved diagnostic framebuffer {}", path.getFileName());
            }
            if (now - phaseStarted > 120_000_000_000L || now - runStarted > 600_000_000_000L)
                throw new IllegalStateException("Bounded capture wait expired: " + diagnostic(client));
            if (!ready) return;
            if (scene >= 12) { finish(client); return; }
            if (!staged) {
                staged = true; frames = 0;
                client.setScreen(null); client.options.hideGui = scene < 7;
                if (scene < 7) {
                    cameraInitialized = false;
                    TERRAIN.reset();
                    step("stage_world_" + scene);
                    ready = false;
                    int selected = scene;
                    double aspect = (double) client.getWindow().getWidth() / client.getWindow().getHeight();
                    server.execute(() -> {
                        try { stageWorld(selected, aspect); client.execute(() -> { step("await_camera_" + selected); ready = true; }); }
                        catch (Exception failure) { client.execute(() -> fail(client, failure)); }
                    });
                } else if (scene == 7) {
                    step("field_guide");
                    var guide = new FieldGuideScreen(); client.setScreen(guide); guide.showMob("redstone_monstrosity");
                    metadata = Map.of("kind", "native_field_guide");
                } else if (scene == 8) {
                    step("gear_guide");
                    client.setScreen(new GearGuideScreen()); metadata = Map.of("kind", "native_gear_guide");
                } else if (scene == 9) {
                    step("quest_journal");
                    client.getConnection().sendCommand("dungeonsalive journal"); metadata = Map.of("kind", "native_server_quest_journal");
                } else if (scene == 10) {
                    step("creative_inventory");
                    var creative = new CreativeModeInventoryScreen(client.player, client.player.connection.enabledFeatures(), true);
                    client.setScreen(creative);
                    // Development-only explicit tab selection, using the actual 1.21.1 inventory implementation.
                    var select = CreativeModeInventoryScreen.class.getDeclaredMethod("selectTab", CreativeModeTab.class);
                    select.setAccessible(true);
                    select.invoke(creative, BuiltInRegistries.CREATIVE_MODE_TAB.get(id("dungeons_alive")));
                    metadata = Map.of("kind", "native_creative_mod_tab");
                } else {
                    step("merchant_service");
                    metadata = Map.of("kind", "native_server_merchant_service");
                    server.execute(() -> {
                        try {
                            var player = server.getPlayerList().getPlayer(playerId);
                            var merchant = ModPeacefulEntities.merchantType("blacksmith").create(player.serverLevel());
                            merchant.setPos(player.position().add(2, 0, 0)); merchant.setNoGravity(true); merchant.setNoAi(true);
                            if (!player.serverLevel().addFreshEntity(merchant) || !MerchantServices.open(player, merchant))
                                throw new IllegalStateException("Native service fixture failed to open");
                        } catch (Exception failure) { client.execute(() -> fail(client, failure)); }
                    });
                }
                return;
            }
            if (scene < 7) {
                if (!cameraInitialized) {
                    // A teleport packet does not clear LocalPlayer.onGround. Initialize
                    // only this acknowledged camera cut, never chase or reposition a drifting camera.
                    if (client.player.distanceToSqr(camera) > .01) return;
                    client.player.setOnGround(false); client.player.setDeltaMovement(Vec3.ZERO);
                    client.player.getAbilities().flying = true; client.player.onUpdateAbilities();
                    cameraInitialized = true; clientCameraInitializations++;
                    DungeonsAlive.LOGGER.info("Living showcase client acknowledged camera {} at {}; flight initialized once", scene, client.player.position());
                    step("await_client_terrain_" + scene);
                } else if (client.player.distanceToSqr(camera) > .04)
                    throw new IllegalStateException("Acknowledged camera drifted after its one-time initialization: " + diagnostic(client));
            }
            frames++;
            if (scene < 7) {
                // The former frame delay could photograph an incomplete chunk delivery.
                // A quiet compiler queue alone also says nothing about missing chunks.
                if (!TERRAIN.sample(client) || frames < 110) return;
            } else if (frames < 40) return;
            if (scene == 9 && !(client.screen instanceof JournalScreen) || scene == 11 && !(client.screen instanceof MerchantServiceScreen)) {
                if (frames < 300) return;
                throw new IllegalStateException("Did not receive the actual server-backed screen for scene " + scene);
            }
            String name = scene < 4 ? BIOMES.get(scene) : scene < 7 ? SITES.get(scene - 4) : List.of("field-guide", "gear-guide", "journal", "creative-tab", "merchant-service").get(scene - 7);
            Path path = output.resolve(String.format("%02d-%s.png", scene + 1, name));
            try (var pixels = Screenshot.takeScreenshot(client.getMainRenderTarget())) { pixels.writeToFile(path); }
            var record = new LinkedHashMap<String, Object>(metadata);
            if (scene < 7) record.put("client_terrain_readiness", TERRAIN.snapshot());
            record.put("file", path.getFileName().toString());
            record.put("sha256", java.util.HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256").digest(Files.readAllBytes(path))));
            CAPTURES.add(record); writeReport("capturing", null);
            DungeonsAlive.LOGGER.info("Living showcase captured {}", name);
            scene++; staged = false;
        } catch (Exception failure) { fail(client, failure); }
    }

    private static Path authorizedRun(Minecraft client) throws Exception {
        Path run = unlinkedRealPath(client.gameDirectory.toPath());
        if (Boolean.getBoolean("dungeonsalive.isolatedPrismQa")) {
            if (!Boolean.getBoolean("dungeonsalive.livingShowcase") || !run.endsWith(PRISM_QA_SUFFIX))
                throw new IllegalStateException("Production capture requires both explicit flags and the fixed native QA profile");
            Path project = run;
            for (int index = 0; index < PRISM_QA_SUFFIX.getNameCount(); index++) project = project.getParent();
            if (project == null || !Files.isRegularFile(unlinkedRealPath(project.resolve("AGENTS.md")))
                    || !Files.isRegularFile(unlinkedRealPath(project.resolve("gradle.properties"))))
                throw new IllegalStateException("Native QA profile is not inside the marked source project");
            requireMarker(run.resolve("dungeonsalive-native-prism-qa.txt"), PRISM_QA_TOKEN);
            Path expectedWorld = unlinkedRealPath(run.resolve("saves").resolve(QA_WORLD));
            var integrated = client.getSingleplayerServer();
            if (!expectedWorld.equals(unlinkedRealPath(integrated.getWorldPath(LevelResource.ROOT)))
                    || !QA_WORLD.equals(integrated.getWorldData().getLevelName())
                    || !Files.isRegularFile(unlinkedRealPath(expectedWorld.resolve("level.dat"))))
                throw new IllegalStateException("Native QA capture refuses a different world or save name");
            requireMarker(expectedWorld.resolve("dungeonsalive-living-showcase-source.txt"), "isolated-living-showcase-world-v1");
            Path fresh = expectedWorld.resolve("dungeonsalive-native-prism-qa.fresh");
            requireMarker(fresh, PRISM_QA_TOKEN);
            Path used = expectedWorld.resolve("dungeonsalive-native-prism-qa.used");
            if (Files.exists(used, java.nio.file.LinkOption.NOFOLLOW_LINKS)
                    || Files.exists(run.resolve("captures"), java.nio.file.LinkOption.NOFOLLOW_LINKS))
                throw new IllegalStateException("Native QA world has already been captured or claimed");
            // Consume only this explicitly generated world's one-shot marker before any
            // teleport, chunk generation, template placement, UI change or client shutdown.
            Files.move(fresh, used);
            return run;
        }
        if (FMLEnvironment.production || !run.getParent().getFileName().toString().equals("local-client")
                || !run.getParent().getParent().getFileName().toString().equals("build"))
            throw new IllegalStateException("Living showcase requires its isolated development run");
        requireMarker(run.resolve("dungeonsalive-local-client.txt"), "isolated-direct-neoforge-client-v1");
        return run;
    }

    private static Path unlinkedRealPath(Path path) throws Exception {
        Path normalized = path.toAbsolutePath().normalize();
        for (Path current = normalized; current != null; current = current.getParent()) {
            if (Files.isSymbolicLink(current)) throw new IllegalStateException("QA capture refuses symlinked paths or parents: " + current);
        }
        if (!normalized.equals(normalized.toRealPath())) throw new IllegalStateException("QA path does not resolve exactly: " + normalized);
        return normalized;
    }

    private static void requireMarker(Path path, String token) throws Exception {
        Path checked = unlinkedRealPath(path);
        if (!Files.isRegularFile(checked) || !Files.readString(checked).equals(token + "\n"))
            throw new IllegalStateException("QA capture marker is missing or does not match: " + path);
    }

    private static void findBiomes(ServerLevel level) {
        var source = level.getChunkSource().getGenerator().getBiomeSource();
        if (!(source instanceof de.laugenbreze.dungeonsalive.world.LivingBiomeSource))
            throw new IllegalStateException("Showcase world did not decode its actual living-world generator");
        var sampler = level.getChunkSource().randomState().sampler();
        // A bounded climate-only search; chunks are generated later only for the explicit capture destinations.
        for (int ring = 0; ring <= 64 && LOCATIONS.size() < BIOMES.size(); ring++) {
            if (ring % 8 == 0) step("find_biomes_ring_" + ring + "_found_" + LOCATIONS.keySet());
            for (int x = -ring; x <= ring; x++) for (int z = -ring; z <= ring; z++) {
                if (Math.max(Math.abs(x), Math.abs(z)) != ring) continue;
                var biome = source.getNoiseBiome(x * 32, 16, z * 32, sampler);
                var key = biome.unwrapKey().orElseThrow().location();
                if (key.getNamespace().equals(DungeonsAlive.MOD_ID) && BIOMES.contains(key.getPath())) LOCATIONS.putIfAbsent(key.getPath(), new BlockPos(x * 128, 64, z * 128));
            }
        }
        if (LOCATIONS.size() != BIOMES.size()) throw new IllegalStateException("Bounded biome search missed requested regions: " + LOCATIONS.keySet());
        DungeonsAlive.LOGGER.info("Living showcase found all biome destinations: {}", LOCATIONS);
    }
    private static void stageWorld(int selected, double aspect) {
        var level = server.overworld(); var player = server.getPlayerList().getPlayer(playerId);
        player.setGameMode(GameType.CREATIVE);
        BlockPos target = selected < 4 ? LOCATIONS.get(BIOMES.get(selected)) : new BlockPos(1000 + (selected - 4) * 128, 64, 1000);
        for (int x = (target.getX() >> 4) - 2; x <= (target.getX() >> 4) + 2; x++) {
            step("load_scene_" + selected + "_chunk_column_" + x);
            for (int z = (target.getZ() >> 4) - 2; z <= (target.getZ() >> 4) + 2; z++) level.getChunk(x, z);
        }
        int ground = level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, target.getX(), target.getZ());
        if (selected < 4) {
            camera = new Vec3(target.getX() + 20, ground + 25, target.getZ() + 28);
            aim(player, level, camera, new Vec3(target.getX(), ground + 3, target.getZ()));
            metadata = Map.of("kind", "native_natural_biome_generation", "requested_biome", BIOMES.get(selected),
                    "ground_biome", level.getBiome(new BlockPos(target.getX(), ground, target.getZ())).unwrapKey().orElseThrow().location().toString(),
                    "target", List.of(target.getX(), ground, target.getZ()), "blocks_staged", false);
        } else {
            var template = level.getStructureManager().get(id("boss_sites/" + SITES.get(selected - 4))).orElseThrow();
            var origin = new BlockPos(target.getX(), ground, target.getZ());
            template.placeInWorld(level, origin, origin, new StructurePlaceSettings(), level.random, 2);
            var view = templateCamera(level, player, origin, template.getSize(), aspect, SITES.get(selected - 4));
            camera = view.position();
            aim(player, level, camera, view.target());
            metadata = Map.of("kind", "native_staged_structure_template", "structure", SITES.get(selected - 4),
                    "natural_discovery_claimed", false, "camera_selection", view.evidence());
        }
    }

    private record TemplateView(Vec3 position, Vec3 target, Map<String, Object> evidence) {}

    private static TemplateView templateCamera(ServerLevel level, ServerPlayer player, BlockPos origin, Vec3i size,
                                             double aspect, String site) {
        // Sample actual upper surfaces in the placed volume. No foliage or surrounding
        // terrain is removed to manufacture a clear view of this deliberately staged site.
        var targets = new ArrayList<Vec3>();
        for (int ix = 0; ix < 5; ix++) for (int iz = 0; iz < 5; iz++) {
            int x = origin.getX() + (int) ((ix + .5) * size.getX() / 5);
            int z = origin.getZ() + (int) ((iz + .5) * size.getZ() / 5);
            for (int y = origin.getY() + size.getY() - 1; y >= origin.getY(); y--) {
                var block = new BlockPos(x, y, z);
                if (!level.getBlockState(block).isAir()) { targets.add(new Vec3(x + .5, y + 1.02, z + .5)); break; }
            }
        }
        if (targets.isEmpty()) throw new IllegalStateException("Placed template has no camera reference surfaces");
        // These are the actual fixtures' usable boss floor, broad entrance and
        // provision chest from generate_boss_sites.py, not a requirement that all
        // columns/roof surfaces of an internally occluded structure be visible.
        int floor = site.equals("sunken_shrine") ? 2 : 0;
        var landmarks = new LinkedHashMap<String, Vec3>();
        landmarks.put("usable_boss_floor", new Vec3(origin.getX() + size.getX() / 2.0, origin.getY() + floor + 1.1, origin.getZ() + 10.5));
        landmarks.put("entrance", new Vec3(origin.getX() + size.getX() / 2.0, origin.getY() + floor + 2, origin.getZ() + 20));
        landmarks.put("provision_chest", site.equals("redstone_mineyard")
                ? new Vec3(origin.getX() + 19.5, origin.getY() + 2.02, origin.getZ() + 15.5)
                : new Vec3(origin.getX() + 10.5, origin.getY() + floor + 2.02, origin.getZ() + 18.5));
        Vec3 target = landmarks.values().stream().reduce(Vec3.ZERO, Vec3::add).scale(1.0 / landmarks.size());
        Vec3 best = null; int bestVisible = -1, candidates = 0, bestLandmarks = -1;
        List<String> bestLandmarkNames = List.of(); double bestDistance = Double.MAX_VALUE;
        for (int lift : new int[]{3, 5, 8, 14, 24, 38}) for (double radius : new double[]{8, 12, 18, 24, 32, 0}) {
            for (int direction = 0; direction < (radius == 0 ? 1 : 8); direction++) {
                double angle = direction * Math.PI / 4;
                Vec3 candidate = new Vec3(target.x + Math.cos(angle) * radius,
                        origin.getY() + floor + lift, target.z + Math.sin(angle) * radius);
                if (!loadedCameraVolume(level, candidate, origin, size)
                        || !level.noBlockCollision(player, player.getBoundingBox().move(candidate.subtract(player.position())))) continue;
                Vec3 eye = candidate.add(0, player.getEyeHeight(), 0);
                candidates++;
                var visibleLandmarks = new ArrayList<String>();
                for (var landmark : landmarks.entrySet()) if (pointInView(eye, target, landmark.getValue(), aspect)
                        && level.clip(new ClipContext(eye, landmark.getValue(), ClipContext.Block.OUTLINE,
                        ClipContext.Fluid.NONE, player)).getType() == HitResult.Type.MISS) visibleLandmarks.add(landmark.getKey());
                int visible = 0;
                for (Vec3 surface : targets) if (pointInView(eye, target, surface, aspect) && level.clip(new ClipContext(eye, surface,
                        ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, player)).getType() == HitResult.Type.MISS) visible++;
                double range = eye.distanceTo(target);
                if (visibleLandmarks.size() > bestLandmarks || visibleLandmarks.size() == bestLandmarks
                        && (visible > bestVisible || visible == bestVisible && range < bestDistance)) {
                    best = candidate; bestLandmarks = visibleLandmarks.size(); bestLandmarkNames = List.copyOf(visibleLandmarks);
                    bestVisible = visible; bestDistance = range;
                }
            }
        }
        if (best == null || bestLandmarks == 0)
            throw new IllegalStateException("No template landmark has a clear loaded-only camera view; candidates=" + candidates);
        return new TemplateView(best, target, Map.of("method", "loaded-only sight rays; prioritize actual boss floor, entrance and provision chest, including low interior views",
                "candidates", candidates, "visible_surface_samples", bestVisible, "surface_samples", targets.size(),
                "visible_landmarks", bestLandmarkNames, "view_scope", "Focused structure view, not a guarantee that every column or rear surface is visible",
                "template_bounds_inside_camera", templateFits(best.add(0, player.getEyeHeight(), 0), target, origin, size, aspect),
                "surrounding_blocks_removed", false, "position", List.of(best.x, best.y, best.z)));
    }

    private static boolean loadedCameraVolume(ServerLevel level, Vec3 candidate, BlockPos origin, Vec3i size) {
        int minX = Math.min(origin.getX(), (int)Math.floor(candidate.x) - 1) >> 4;
        int maxX = Math.max(origin.getX() + size.getX(), (int)Math.floor(candidate.x) + 1) >> 4;
        int minZ = Math.min(origin.getZ(), (int)Math.floor(candidate.z) - 1) >> 4;
        int maxZ = Math.max(origin.getZ() + size.getZ(), (int)Math.floor(candidate.z) + 1) >> 4;
        for (int x = minX; x <= maxX; x++) for (int z = minZ; z <= maxZ; z++)
            if (!level.hasChunk(x, z)) return false;
        return true;
    }

    private static boolean templateFits(Vec3 eye, Vec3 target, BlockPos origin, Vec3i size, double aspect) {
        for (int x : new int[]{0, size.getX()}) for (int y : new int[]{0, size.getY()}) for (int z : new int[]{0, size.getZ()})
            if (!pointInView(eye, target, new Vec3(origin.getX() + x, origin.getY() + y, origin.getZ() + z), aspect)) return false;
        return true;
    }

    private static boolean pointInView(Vec3 eye, Vec3 target, Vec3 absolute, double aspect) {
        Vec3 forward = target.subtract(eye).normalize();
        Vec3 right = forward.cross(new Vec3(0, 1, 0)).normalize();
        // An exactly vertical shot still has a well-defined horizontal basis.
        if (right.lengthSqr() < .1) right = new Vec3(1, 0, 0);
        Vec3 up = right.cross(forward).normalize();
        Vec3 point = absolute.subtract(eye);
        double depth = point.dot(forward), halfHeight = Math.tan(Math.toRadians(30)) * depth * .94;
        return depth > 0 && Math.abs(point.dot(up)) <= halfHeight && Math.abs(point.dot(right)) <= halfHeight * aspect;
    }
    private static void aim(ServerPlayer player, ServerLevel level, Vec3 from, Vec3 target) {
        Vec3 delta = target.subtract(from.add(0, player.getEyeHeight(), 0));
        float yaw = (float)Math.toDegrees(Math.atan2(-delta.x, delta.z));
        float pitch = (float)-Math.toDegrees(Math.atan2(delta.y, Math.sqrt(delta.x * delta.x + delta.z * delta.z)));
        player.teleportTo(level, from.x, from.y, from.z, yaw, pitch);
        player.setOnGround(false);
        player.setDeltaMovement(Vec3.ZERO);
        // Send flight only after the actual teleport. During slow chunk preparation
        // LocalPlayer would otherwise cancel an earlier flight packet while still on the old ground.
        player.getAbilities().flying = true; player.onUpdateAbilities();
        DungeonsAlive.LOGGER.info("Living showcase camera requested {} looking at {}; server player {}, flying={}, dimension={}",
                from, target, player.position(), player.getAbilities().flying, level.dimension().location());
    }
    private static void step(String next) {
        phase = next; phaseStarted = System.nanoTime();
        DungeonsAlive.LOGGER.info("Living showcase step: {}", next);
    }
    private static Map<String, Object> diagnostic(Minecraft client) {
        var state = new LinkedHashMap<String, Object>();
        state.put("phase", phase); state.put("scene", scene); state.put("ready", ready); state.put("staged", staged);
        state.put("render_frames_waited", frames); state.put("phase_seconds", (System.nanoTime() - phaseStarted) / 1_000_000_000L);
        state.put("camera_initialized", cameraInitialized); state.put("client_camera_initializations", clientCameraInitializations);
        state.put("client_terrain_readiness", TERRAIN.snapshot());
        state.put("screen", client.screen == null ? "none" : client.screen.getClass().getSimpleName());
        if (client.player != null) {
            state.put("client_position", client.player.position().toString()); state.put("client_flying", client.player.getAbilities().flying);
            state.put("client_on_ground", client.player.onGround());
            if (camera != null) state.put("camera_distance_squared", client.player.distanceToSqr(camera));
        }
        if (camera != null) state.put("camera_target_position", camera.toString());
        return state;
    }
    private static void writeReport(String status, String error) throws Exception {
        var report = new LinkedHashMap<String, Object>();
        report.put("status", status); report.put("captures", CAPTURES);
        report.put("diagnostic_frames", DIAGNOSTICS);
        report.put("visual_review_performed", false);
        report.put("last_progress", diagnostic(Minecraft.getInstance()));
        report.put("scope", "Real native client rendering; four naturally generated biome views, three explicitly staged templates, five actual screens. Capture alone is not visual approval.");
        if (error != null) report.put("error", error);
        Files.writeString(output.resolve("manifest.json"), new GsonBuilder().setPrettyPrinting().create().toJson(report));
    }
    private static void finish(Minecraft client) throws Exception {
        stopped = true; writeReport("captured", null);
        DungeonsAlive.LOGGER.info("Living showcase completed: {} native captures", CAPTURES.size()); client.stop();
    }
    private static void fail(Minecraft client, Exception error) {
        stopped = true; DungeonsAlive.LOGGER.error("Living showcase failed", error);
        try { if (output != null) writeReport("failed", error.toString()); } catch (Exception ignored) { }
        client.stop();
    }
    private static ResourceLocation id(String path) { return ResourceLocation.fromNamespaceAndPath(DungeonsAlive.MOD_ID, path); }
}
