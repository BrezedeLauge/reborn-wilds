package de.laugenbreze.dungeonsalive.client.dev;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import de.laugenbreze.dungeonsalive.DungeonsAlive;
import de.laugenbreze.dungeonsalive.entity.peaceful.BirdPerchCycle;
import de.laugenbreze.dungeonsalive.entity.peaceful.DungeonsCreature;
import de.laugenbreze.dungeonsalive.entity.peaceful.ModPeacefulEntities;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HexFormat;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Screenshot;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.storage.LevelResource;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.HitResult;
import net.neoforged.neoforge.client.event.RenderFrameEvent;
import net.neoforged.neoforge.client.event.sound.PlaySoundSourceEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.CommandEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

/** One-shot observation, never ordinary gameplay: real AI in an explicitly staged disposable habitat. */
public final class NativeNatureShowcaseCapture {
    private static final Path SUFFIX = Path.of("run-prism", "instances", "Dungeons-Alive-Nature-QA", "minecraft");
    private static final String TOKEN = "isolated-dungeonsalive-nature-prism-qa-v1";
    private static final String WORLD = "Dungeons Alive Nature Showcase";
    private static final int MAX_TICKS = 3000, MIN_TICKS = 1920, MAX_IMAGES = 200;
    private static final List<AnimalTrace> ANIMALS = new ArrayList<>(); // Server thread only.
    private static final List<Map<String, Object>> FRAMES = new ArrayList<>(); // Render thread only.
    private static final List<Map<String, Object>> SAMPLES = new ArrayList<>(); // Render snapshots only.
    private static final Map<String, Integer> PHASE_FRAMES_IN_VIEW = new LinkedHashMap<>();
    private static final NativeCaptureTerrainReadiness TERRAIN = new NativeCaptureTerrainReadiness();
    private static final List<Map<String, Object>> PREPARATION = new ArrayList<>();
    private static final List<Map<String, Object>> DIAGNOSTICS = new ArrayList<>();
    private static final AtomicInteger DUCK_AUDIO_CHANNELS = new AtomicInteger();
    private static final Set<String> DUCK_AUDIO_VARIANTS = ConcurrentHashMap.newKeySet();
    private static final Map<String, AtomicInteger> NATURE_AUDIO_CHANNELS = new ConcurrentHashMap<>();
    private static Map<String, Object> audioResources = Map.of();
    private static final List<Map<String, Object>> CAMERA_SEGMENTS = new ArrayList<>(); // Server thread only.
    private static MinecraftServer server;
    private static UUID playerId;
    private static UUID focalBird;
    private static Path output;
    private static String artifactSha;
    private static Vec3 camera;
    private static float cameraYaw, cameraPitch;
    private static long startTick, startNanos, lastImageTick = -100, burstUntil;
    private static long lastSampleTick = -1;
    private static long segmentStartTick;
    private static int fixtureFloor;
    private static int commands, lastTransitions;
    private static double maximumPlayerDistance, maximumCameraRotation;
    private static volatile Observation latest;
    private static volatile boolean observing, stopped;
    private static volatile String sequence = "context";
    private static boolean started, contextCaptured;
    private static String initializedCameraSequence;
    private static String waitingCameraSequence;
    private static String imageReadySequence;
    private static int clientCameraInitializations;
    private static boolean observationRequested;
    private static long preparationStarted, preparationDiagnostic;
    private static long cameraWaitStarted;

    private NativeNatureShowcaseCapture() {}

    public static void register() {
        if (!Boolean.getBoolean("dungeonsalive.natureShowcase") || !Boolean.getBoolean("dungeonsalive.isolatedNatureQa")) return;
        NeoForge.EVENT_BUS.addListener(NativeNatureShowcaseCapture::frame);
        NeoForge.EVENT_BUS.addListener(NativeNatureShowcaseCapture::tick);
        NeoForge.EVENT_BUS.addListener(NativeNatureShowcaseCapture::command);
        NeoForge.EVENT_BUS.addListener(NativeNatureShowcaseCapture::soundStarted);
    }

    private static void frame(RenderFrameEvent.Post event) {
        var client = Minecraft.getInstance();
        if (stopped || client.level == null || client.player == null || client.getSingleplayerServer() == null) return;
        try {
            if (!started) {
                final Path run;
                try { run = authorize(client); }
                catch (Exception rejected) {
                    stopped = true;
                    DungeonsAlive.LOGGER.error("Nature showcase refused an unapproved run/world; client remains untouched", rejected);
                    return;
                }
                started = true; startNanos = System.nanoTime();
                server = client.getSingleplayerServer(); playerId = client.player.getUUID();
                output = run.resolve("nature-captures"); Files.createDirectory(output);
                inspectAudio(client);
                client.options.fov().set(55); client.options.hideGui = true; client.setScreen(null);
                server.execute(() -> {
                    try { stage(); }
                    catch (Exception failure) { client.execute(() -> fail(client, failure)); }
                });
                return;
            }
            if (System.nanoTime() - startNanos > 360_000_000_000L)
                throw new IllegalStateException("Nature observation exceeded its six-minute wall-clock bound");
            Observation observation = latest;
            if (observation == null) return;
            String frameSequence = observation.sequence();
            Vec3 frameCamera = observation.camera();
            if (!frameSequence.equals(sequence)) return;
            if (frameSequence.equals("context") && contextCaptured) return;
            if (!frameSequence.equals(initializedCameraSequence)) {
                if (!frameSequence.equals(waitingCameraSequence)) {
                    waitingCameraSequence = frameSequence; cameraWaitStarted = System.nanoTime();
                }
                if (client.player.distanceToSqr(frameCamera) > .01) {
                    if (Boolean.TRUE.equals(observation.data().get("before_actor_spawn"))
                            && System.nanoTime() - cameraWaitStarted > 15_000_000_000L) {
                        Path path = output.resolve("diagnostic-camera-" + frameSequence + ".png");
                        try (var pixels = Screenshot.takeScreenshot(client.getMainRenderTarget())) { pixels.writeToFile(path); }
                        DIAGNOSTICS.add(Map.of("file", path.getFileName().toString(), "sha256", hash(path),
                                "sequence", frameSequence, "kind", "unaltered_framebuffer_camera_timeout_not_accepted_scene"));
                        throw new IllegalStateException("Preparation camera was not acknowledged within fifteen seconds");
                    }
                    if (observation.tick() - observation.segmentStart() > 40)
                        throw new IllegalStateException("Client never acknowledged the fixed camera without drift");
                    return;
                }
                client.player.setOnGround(false); client.player.setDeltaMovement(Vec3.ZERO);
                client.player.getAbilities().flying = true; client.player.onUpdateAbilities();
                initializedCameraSequence = frameSequence; clientCameraInitializations++;
                TERRAIN.reset(); preparationStarted = System.nanoTime(); preparationDiagnostic = preparationStarted;
                DungeonsAlive.LOGGER.info("Nature showcase client acknowledged {} camera; flight initialized once", frameSequence);
            }
            if (Boolean.TRUE.equals(observation.data().get("before_actor_spawn"))) {
                if (observationRequested) return;
                if (client.player.distanceToSqr(frameCamera) > .01)
                    throw new IllegalStateException("Initialized preparation camera drifted before observation");
                long now = System.nanoTime();
                if (now - preparationStarted > 120_000_000_000L)
                    throw new IllegalStateException("Nature preparation terrain did not become ready: " + TERRAIN.snapshot());
                boolean terrainReady = TERRAIN.sample(client);
                if (now - preparationDiagnostic > 15_000_000_000L) {
                    preparationDiagnostic = now;
                    DungeonsAlive.LOGGER.info("Nature preparation {} terrain: {}", frameSequence, TERRAIN.snapshot());
                    if (DIAGNOSTICS.stream().noneMatch(entry -> frameSequence.equals(entry.get("sequence")))) {
                        Path path = output.resolve("diagnostic-stall-" + frameSequence + ".png");
                        try (var pixels = Screenshot.takeScreenshot(client.getMainRenderTarget())) { pixels.writeToFile(path); }
                        DIAGNOSTICS.add(Map.of("file", path.getFileName().toString(), "sha256", hash(path),
                                "sequence", frameSequence, "kind", "unaltered_framebuffer_diagnostic_not_accepted_scene",
                                "terrain_readiness", TERRAIN.snapshot()));
                        writeReport("preparing", observation, null);
                    }
                }
                if (!terrainReady) return;
                imageReadySequence = frameSequence;
                PREPARATION.add(Map.of("sequence", frameSequence, "client_world_tick", client.level.getGameTime(),
                        "before_actor_spawn", true, "terrain_readiness", TERRAIN.snapshot()));
                if (frameSequence.equals("context")) {
                    capture(client, observation); contextCaptured = true;
                    server.execute(() -> {
                        try {
                            // Pond plus roughly two blocks of bank, in a fixed closer view.
                            changeCamera(server.getPlayerList().getPlayer(playerId),
                                    new Vec3(-5.5, fixtureFloor + 4.5, 10.0), new Vec3(-5.5, fixtureFloor + .5, 0), "ducks");
                            latest = preparationObservation();
                        } catch (Exception failure) { client.execute(() -> fail(client, failure)); }
                    });
                } else {
                    observationRequested = true;
                    server.execute(() -> {
                        try { beginObservation(); }
                        catch (Exception failure) { client.execute(() -> fail(client, failure)); }
                    });
                }
                return;
            }
            if (lastSampleTick != observation.tick()) {
                lastSampleTick = observation.tick();
                if (observation.tick() % 10 == 0 || observation.finished()) SAMPLES.add(observation.data());
            }
            if (observation.transitions() != lastTransitions) {
                burstUntil = observation.tick() + 12; lastTransitions = observation.transitions();
            }
            int interval = observation.tick() < burstUntil ? 4 : 40;
            // A real later camera cut can reveal different meshes. Keep recording
            // every server tick and let AI proceed; only the framebuffer waits.
            boolean imageTerrainReady = frameSequence.equals(imageReadySequence);
            if (!imageTerrainReady && TERRAIN.sample(client)) {
                imageReadySequence = frameSequence; imageTerrainReady = true;
                DungeonsAlive.LOGGER.info("Nature {} image terrain ready: {}", frameSequence, TERRAIN.snapshot());
            }
            if ((observation.tick() - lastImageTick >= interval || observation.finished())
                    && imageTerrainReady && FRAMES.size() < (observation.finished() ? MAX_IMAGES : MAX_IMAGES - 1)) {
                // The camera is positioned once during setup. Never move it to make a failed cycle look complete.
                Observation current = latest;
                if (current != null && (!current.sequence().equals(frameSequence) || !current.camera().equals(frameCamera))) return;
                if (client.player.distanceToSqr(frameCamera) > .01) {
                    if (observation.tick() - observation.segmentStart() > 40) throw new IllegalStateException("The real client camera moved after its declared sequence cut");
                    return; // Wait for the one initial teleport packet; do not capture the old camera.
                }
                capture(client, observation); lastImageTick = observation.tick();
            }
            if (observation.finished()) {
                if (!imageTerrainReady) throw new IllegalStateException("Observation ended before the bird view terrain was ready: " + TERRAIN.snapshot());
                stopped = true;
                writeReport(observation.complete() ? "captured" : "incomplete", observation, observation.error());
                DungeonsAlive.LOGGER.info("Nature showcase completed: {} frames; actual cycles complete={}", FRAMES.size(), observation.complete());
                client.stop();
            }
        } catch (Exception failure) { fail(client, failure); }
    }

    private static void command(CommandEvent event) {
        if (observing && event.getParseResults().getContext().getSource().getServer() == server) commands++;
    }

    private static void soundStarted(PlaySoundSourceEvent event) {
        if (!started || stopped || !natureSounds().contains(event.getSound().getLocation())) return;
        NATURE_AUDIO_CHANNELS.computeIfAbsent(event.getSound().getLocation().toString(), ignored -> new AtomicInteger()).incrementAndGet();
        if (!event.getSound().getLocation().equals(duckSound())) return;
        DUCK_AUDIO_CHANNELS.incrementAndGet();
        DUCK_AUDIO_VARIANTS.add(event.getSound().getSound().getLocation().toString());
    }

    private static ResourceLocation duckSound() {
        return ResourceLocation.fromNamespaceAndPath(DungeonsAlive.MOD_ID, "entity.duck.contact");
    }
    private static List<ResourceLocation> natureSounds() {
        return List.of(duckSound(), ResourceLocation.fromNamespaceAndPath(DungeonsAlive.MOD_ID, "entity.toucan.contact"),
                ResourceLocation.fromNamespaceAndPath(DungeonsAlive.MOD_ID, "entity.golden_parrot.contact"),
                ResourceLocation.fromNamespaceAndPath("minecraft", "entity.parrot.fly"));
    }
    private static void inspectAudio(Minecraft client) throws Exception {
        var resolved = client.getSoundManager().getSoundEvent(duckSound());
        if (!BuiltInRegistries.SOUND_EVENT.containsKey(duckSound()) || resolved == null || resolved.getWeight() <= 0)
            throw new IllegalStateException("The native client did not register/resolve the duck contact sound");
        var resources = new LinkedHashMap<String, String>();
        var events = new LinkedHashMap<String, Integer>();
        for (var event : natureSounds()) {
            var sound = client.getSoundManager().getSoundEvent(event);
            if (!BuiltInRegistries.SOUND_EVENT.containsKey(event) || sound == null || sound.getWeight() <= 0)
                throw new IllegalStateException("The native client did not register/resolve nature sound " + event);
            events.put(event.toString(), sound.getWeight());
        }
        for (int index = 1; index <= 3; index++) {
            var id = ResourceLocation.fromNamespaceAndPath(DungeonsAlive.MOD_ID, "sounds/wildlife/duck_contact_" + index + ".ogg");
            try (var stream = client.getResourceManager().getResource(id).orElseThrow().open()) {
                resources.put(id.toString(), HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256").digest(stream.readAllBytes())));
            }
        }
        audioResources = Map.of("event", duckSound().toString(), "registered_and_resolved", true,
                "resolved_nature_events", Map.copyOf(events),
                "resolved_weight", resolved.getWeight(), "loaded_resource_sha256", Map.copyOf(resources),
                "master_volume", client.options.getSoundSourceVolume(SoundSource.MASTER),
                "neutral_volume", client.options.getSoundSourceVolume(SoundSource.NEUTRAL));
    }

    private static void tick(ServerTickEvent.Post event) {
        if (!observing || stopped || event.getServer() != server) return;
        try {
            var level = server.overworld();
            long elapsed = level.getGameTime() - startTick;
            var player = server.getPlayerList().getPlayer(playerId);
            if (player == null || player.serverLevel() != level) throw new IllegalStateException("Observation player left the fixture");
            maximumPlayerDistance = Math.max(maximumPlayerDistance, player.position().distanceTo(camera));
            maximumCameraRotation = Math.max(maximumCameraRotation,
                    Math.max(Math.abs(net.minecraft.util.Mth.wrapDegrees(player.getYRot() - cameraYaw)), Math.abs(player.getXRot() - cameraPitch)));
            for (AnimalTrace trace : ANIMALS) trace.observe(level, elapsed);
            boolean ducksComplete = ANIMALS.stream().filter(trace -> !trace.bird).allMatch(trace -> trace.complete);
            if (sequence.equals("ducks") && ducksComplete) {
                for (AnimalTrace trace : ANIMALS) {
                    if (!trace.bird || !(level.getEntity(trace.uuid) instanceof DungeonsCreature bird)
                            || !bird.birdPerched() || !bird.onGround() || bird.birdPerch() == null) continue;
                    Vec3 branch = Vec3.atBottomCenterOf(bird.birdPerch()).add(0, 1, 0);
                    Vec3 vantage = birdCamera(level, player, branch);
                    focalBird = trace.uuid; trace.beginBirdDetail(branch);
                    changeCamera(player, vantage, branch.add(0, .7, 0), "birds");
                    break;
                }
            }
            boolean complete = ducksComplete && focalBird != null && ANIMALS.stream()
                    .filter(trace -> trace.uuid.equals(focalBird)).allMatch(trace -> trace.complete);
            String error = commands != 0 ? "A command was issued during the observation" : maximumPlayerDistance > .1
                    ? "Player moved after the one-time setup" : maximumCameraRotation > .2 ? "Camera rotated during observation" : null;
            if (error == null) error = ANIMALS.stream().map(trace -> trace.error).filter(value -> value != null).findFirst().orElse(null);
            if (error == null && elapsed >= MAX_TICKS && !complete) error = "Not every real bird/duck cycle completed within 3000 world ticks";
            boolean finished = error != null || elapsed >= MAX_TICKS || complete && elapsed >= MIN_TICKS;
            List<Map<String, Object>> animals = ANIMALS.stream().map(AnimalTrace::snapshot).toList();
            var data = new LinkedHashMap<String, Object>();
            data.put("world_tick", level.getGameTime()); data.put("elapsed_tick", elapsed);
            data.put("animals", animals); data.put("player_position", point(player.position()));
            data.put("sequence", sequence); data.put("camera_segments", cameraSegments(elapsed));
            if (focalBird != null) data.put("focal_bird_uuid", focalBird.toString());
            data.put("maximum_player_displacement", maximumPlayerDistance); data.put("maximum_camera_rotation", maximumCameraRotation);
            data.put("commands_during_observation", commands);
            int transitions = ANIMALS.stream().filter(trace -> !trace.bird).findFirst().orElseThrow().transitions.size()
                    + ANIMALS.stream().filter(trace -> trace.bird && (focalBird == null || trace.uuid.equals(focalBird)))
                    .findFirst().orElseThrow().transitions.size();
            latest = new Observation(elapsed, transitions, sequence, camera, segmentStartTick,
                    finished, complete && error == null, error, Map.copyOf(data));
            if (finished) observing = false;
        } catch (Exception failure) {
            observing = false;
            Minecraft.getInstance().execute(() -> fail(Minecraft.getInstance(), failure));
        }
    }

    private static void stage() {
        ServerLevel level = server.overworld();
        ServerPlayer player = server.getPlayerList().getPlayer(playerId);
        if (player == null) throw new IllegalStateException("Missing actual integrated player");
        // Explicit fixture generation in the freshly claimed QA world, not wildlife's runtime chunk loading.
        for (int x = -7; x <= 7; x++) for (int z = -7; z <= 7; z++) {
            level.getChunk(x, z); level.setChunkForced(x, z, true);
        }
        int floor = Math.min(level.getMaxBuildHeight() - 24,
                level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, 0, 0) + 5);
        fixtureFloor = floor;
        for (int x = -18; x <= 18; x++) for (int z = -14; z <= 15; z++) {
            for (int y = floor + 1; y <= floor + 16; y++) level.setBlock(new BlockPos(x, y, z), Blocks.AIR.defaultBlockState(), 2);
            level.setBlock(new BlockPos(x, floor - 1, z), Blocks.DIRT.defaultBlockState(), 2);
            level.setBlock(new BlockPos(x, floor, z), Blocks.GRASS_BLOCK.defaultBlockState(), 2);
        }
        // One-block water, broad dry banks. The edge is one ordinary step above the water block.
        for (int x = -9; x <= -2; x++) for (int z = -4; z <= 4; z++)
            level.setBlock(new BlockPos(x, floor, z), Blocks.WATER.defaultBlockState(), 3);
        for (BlockPos trunk : List.of(new BlockPos(5, floor, -2), new BlockPos(8, floor, 3))) {
            tree(level, trunk);
        }
        // The normal saved-group itinerary ranges beyond the pond. Real perches
        // are distributed through a staged grove; no route or phase is restricted.
        for (int x = -40; x <= 40; x += 10) for (int z = -40; z <= 40; z += 10) {
            if (Math.abs(x) <= 10 && Math.abs(z) <= 10) continue;
            int terrain = level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, x, z);
            tree(level, new BlockPos(x, Math.min(level.getMaxBuildHeight() - 10, terrain), z));
        }
        level.getGameRules().getRule(GameRules.RULE_DOMOBSPAWNING).set(false, server);
        level.getGameRules().getRule(GameRules.RULE_DAYLIGHT).set(false, server);
        level.getGameRules().getRule(GameRules.RULE_WEATHER_CYCLE).set(false, server);
        level.setDayTime(6000); level.setWeatherParameters(100000, 0, false, false);
        player.setGameMode(GameType.CREATIVE);
        startTick = level.getGameTime();
        changeCamera(player, new Vec3(0, floor + 29, 47), new Vec3(0, floor + 3, 0), "context");
        latest = preparationObservation();
        DungeonsAlive.LOGGER.info("Nature habitat prepared; waiting for actual client terrain before spawning observation actors");
    }

    private static Observation preparationObservation() {
        return new Observation(0, 0, sequence, camera, segmentStartTick, false, false, null, Map.of("world_tick", server.overworld().getGameTime(),
                "elapsed_tick", 0, "sequence", sequence, "animals", List.of(), "before_actor_spawn", true,
                "player_position", point(server.getPlayerList().getPlayer(playerId).position()),
                "camera_segments", List.of(), "commands_during_observation", 0));
    }

    private static void beginObservation() {
        ServerLevel level = server.overworld();
        int floor = fixtureFloor;
        startTick = level.getGameTime(); segmentStartTick = 0;
        CAMERA_SEGMENTS.clear(); maximumPlayerDistance = 0; maximumCameraRotation = 0;
        // Both setup views have rendered before any animal exists. From this point
        // every world tick is observed; no pause, AI suppression or phase selection.
        spawnGroup(level, "pet_toucan", List.of(new Vec3(4.5, floor + 7, -.5),
                new Vec3(6.5, floor + 7, -.5), new Vec3(8.5, floor + 7, -.5)), true);
        spawnGroup(level, "pet_duck", List.of(new Vec3(-6.5, floor + .8, -.5),
                new Vec3(-6.5, floor + .8, .5), new Vec3(-5.5, floor + .8, .5)), false);
        latest = null; observing = true;
        DungeonsAlive.LOGGER.info("Nature showcase observation began: six real AI entities, fixed camera, staged pond and perches");
    }

    private static void tree(ServerLevel level, BlockPos trunk) {
        for (int y = 1; y <= 3; y++) level.setBlock(trunk.above(y), Blocks.OAK_LOG.defaultBlockState(), 2);
        for (int x = -2; x <= 2; x++) for (int z = -2; z <= 2; z++)
            level.setBlock(trunk.offset(x, 4, z), Blocks.OAK_LEAVES.defaultBlockState()
                    .setValue(BlockStateProperties.PERSISTENT, true), 2);
    }

    private static Vec3 birdCamera(ServerLevel level, ServerPlayer player, Vec3 branch) {
        for (int lift : new int[]{2, 4, 6}) for (Vec3 offset : List.of(new Vec3(0, lift, 12), new Vec3(12, lift, 0),
                new Vec3(-12, lift, 0), new Vec3(0, lift, -12))) {
            Vec3 candidate = branch.add(offset);
            if (level.noBlockCollision(player, player.getBoundingBox().move(candidate.subtract(player.position())))
                    && level.clip(new ClipContext(candidate.add(0, player.getEyeHeight(), 0), branch.add(0, .7, 0),
                    ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, player)).getType() == HitResult.Type.MISS) return candidate;
        }
        throw new IllegalStateException("No unobstructed stationary camera around the actually used branch");
    }

    private static void changeCamera(ServerPlayer player, Vec3 from, Vec3 target, String nextSequence) {
        if (player == null) throw new IllegalStateException("Camera player left the observation");
        long elapsed = player.level().getGameTime() - startTick;
        if (camera != null) CAMERA_SEGMENTS.add(cameraSegment(elapsed));
        camera = from; maximumPlayerDistance = 0; maximumCameraRotation = 0; segmentStartTick = elapsed;
        Vec3 delta = target.subtract(from.add(0, player.getEyeHeight(), 0));
        cameraYaw = (float) Math.toDegrees(Math.atan2(-delta.x, delta.z));
        cameraPitch = (float) -Math.toDegrees(Math.atan2(delta.y, Math.hypot(delta.x, delta.z)));
        player.teleportTo(player.serverLevel(), from.x, from.y, from.z, cameraYaw, cameraPitch);
        player.setOnGround(false); player.setDeltaMovement(Vec3.ZERO);
        player.getAbilities().flying = true; player.onUpdateAbilities();
        sequence = nextSequence;
        DungeonsAlive.LOGGER.info("Nature showcase stationary sequence {} begins at tick {}", nextSequence, elapsed);
    }

    private static Map<String, Object> cameraSegment(long elapsed) {
        return Map.of("sequence", sequence, "from_tick", segmentStartTick, "to_tick", elapsed,
                "position", point(camera), "yaw", cameraYaw, "pitch", cameraPitch,
                "maximum_player_displacement", maximumPlayerDistance, "maximum_camera_rotation", maximumCameraRotation);
    }
    private static List<Map<String, Object>> cameraSegments(long elapsed) {
        var segments = new ArrayList<>(CAMERA_SEGMENTS); segments.add(cameraSegment(elapsed)); return List.copyOf(segments);
    }

    private static void spawnGroup(ServerLevel level, String id, List<Vec3> positions, boolean bird) {
        SpawnGroupData group = null;
        for (Vec3 position : positions) {
            var animal = ModPeacefulEntities.creatureType(id).create(level);
            if (animal == null) throw new IllegalStateException("Missing creature " + id);
            animal.setPos(position);
            group = animal.finalizeSpawn(level, level.getCurrentDifficultyAt(animal.blockPosition()), MobSpawnType.NATURAL, group);
            // These six declared observation actors must survive ordinary distance
            // despawn after the fixed-camera cut. Their actual AI remains unchanged.
            animal.setPersistenceRequired();
            if (animal.isNoAi() || !animal.naturalWildlife() || !level.addFreshEntity(animal))
                throw new IllegalStateException("Actual natural-provenance fixture failed for " + id);
            ANIMALS.add(new AnimalTrace(animal, id, bird));
        }
    }

    private static void capture(Minecraft client, Observation observation) throws Exception {
        Path path = output.resolve(String.format("%04d-tick-%04d.png", FRAMES.size(), observation.tick()));
        try (var image = Screenshot.takeScreenshot(client.getMainRenderTarget())) { image.writeToFile(path); }
        List<Map<String, Object>> clientAnimals = new ArrayList<>();
        for (Map<String, Object> entry : observation.animals()) {
            UUID uuid = UUID.fromString((String) entry.get("uuid"));
            for (var entity : client.level.entitiesForRendering()) if (entity.getUUID().equals(uuid) && entity instanceof DungeonsCreature creature) {
                // The six-stage bird routine is authoritative server state. The client
                // receives its contact flag, not the unsynchronized server state object.
                String activity = Boolean.TRUE.equals(entry.get("bird"))
                        ? (creature.birdPerched() ? "PERCHED" : "NOT_PERCHED") : creature.duckActivity().name();
                String serverActivity = (String) entry.get("activity");
                boolean inView = centreInsideView(client, creature);
                if (inView) PHASE_FRAMES_IN_VIEW.merge(uuid + "/" + serverActivity, 1, Integer::sum);
                clientAnimals.add(Map.of("uuid", uuid.toString(), "position", point(creature.position()), "activity", activity,
                        "server_activity_at_snapshot", serverActivity,
                        "bird_perched", creature.birdPerched(), "duck_young", creature.duckYoung(),
                        "centre_inside_camera_frustum_not_occlusion_test", inView));
                break;
            }
        }
        var frame = new LinkedHashMap<String, Object>();
        frame.put("file", path.getFileName().toString()); frame.put("sha256", hash(path));
        frame.put("server_snapshot", observation.data()); frame.put("client_world_tick", client.level.getGameTime());
        frame.put("client_entities", clientAnimals); frame.put("client_camera", point(client.player.position()));
        frame.put("client_yaw", client.player.getYRot()); frame.put("client_pitch", client.player.getXRot());
        frame.put("requested_camera_from_same_server_snapshot", point(observation.camera()));
        frame.put("client_terrain_readiness", TERRAIN.snapshot());
        FRAMES.add(frame);
        if (FRAMES.size() % 20 == 0) writeReport("observing", observation, null);
    }

    private static boolean centreInsideView(Minecraft client, DungeonsCreature animal) {
        Vec3 forward = client.player.getLookAngle().normalize();
        Vec3 right = forward.cross(new Vec3(0, 1, 0)).normalize();
        Vec3 up = right.cross(forward).normalize();
        Vec3 delta = animal.position().add(0, animal.getBbHeight() / 2.0, 0).subtract(client.player.getEyePosition());
        double depth = delta.dot(forward), halfHeight = Math.tan(Math.toRadians(55 / 2.0)) * depth;
        double aspect = (double) client.getWindow().getWidth() / Math.max(1, client.getWindow().getHeight());
        return depth > 0 && Math.abs(delta.dot(up)) <= halfHeight && Math.abs(delta.dot(right)) <= halfHeight * aspect;
    }

    private static Path authorize(Minecraft client) throws Exception {
        if (!Boolean.getBoolean("dungeonsalive.natureShowcase") || !Boolean.getBoolean("dungeonsalive.isolatedNatureQa"))
            throw new IllegalStateException("Both explicit nature QA flags are required");
        Path run = realUnlinked(client.gameDirectory.toPath());
        if (!run.endsWith(SUFFIX)) throw new IllegalStateException("Nature capture refuses any other instance");
        Path project = run;
        for (int i = 0; i < SUFFIX.getNameCount(); i++) project = project.getParent();
        if (project == null || !Files.isRegularFile(realUnlinked(project.resolve("AGENTS.md")))
                || !Files.isRegularFile(realUnlinked(project.resolve("gradle.properties"))))
            throw new IllegalStateException("Nature QA must remain inside the marked project");
        marker(run.resolve("dungeonsalive-nature-prism-qa.txt"), TOKEN);
        Path world = realUnlinked(run.resolve("saves").resolve(WORLD));
        var integrated = client.getSingleplayerServer();
        if (!world.equals(realUnlinked(integrated.getWorldPath(LevelResource.ROOT)))
                || !WORLD.equals(integrated.getWorldData().getLevelName()) || !Files.isRegularFile(realUnlinked(world.resolve("level.dat"))))
            throw new IllegalStateException("The actual opened world is not the isolated nature fixture");
        marker(world.resolve("dungeonsalive-living-showcase-source.txt"), "isolated-living-showcase-world-v1");
        Path fresh = world.resolve("dungeonsalive-nature-prism-qa.fresh"); marker(fresh, TOKEN);
        Path used = world.resolve("dungeonsalive-nature-prism-qa.used");
        if (Files.exists(used, LinkOption.NOFOLLOW_LINKS) || Files.exists(run.resolve("nature-captures"), LinkOption.NOFOLLOW_LINKS))
            throw new IllegalStateException("Nature QA world was already claimed");
        var staging = JsonParser.parseString(Files.readString(realUnlinked(run.getParent().resolve("staging.json")))).getAsJsonObject();
        String expected = staging.getAsJsonObject("artifact").get("sha256").getAsString();
        try (var files = Files.list(realUnlinked(run.resolve("mods")))) {
            List<Path> jars = files.filter(path -> path.getFileName().toString().endsWith(".jar")).toList();
            if (jars.size() != 1 || !expected.equals(hash(realUnlinked(jars.getFirst()))))
                throw new IllegalStateException("Nature QA artifact differs from its tested staging manifest");
        }
        artifactSha = expected;
        // No mutation, including options or client.stop(), is allowed before this one-shot claim.
        Files.move(fresh, used);
        return run;
    }

    private static Path realUnlinked(Path path) throws Exception {
        Path normalized = path.toAbsolutePath().normalize();
        for (Path p = normalized; p != null; p = p.getParent()) if (Files.isSymbolicLink(p))
            throw new IllegalStateException("Nature QA refuses symlinked paths: " + p);
        if (!normalized.equals(normalized.toRealPath())) throw new IllegalStateException("Nature QA path must resolve exactly");
        return normalized;
    }
    private static void marker(Path path, String value) throws Exception {
        Path checked = realUnlinked(path);
        if (!Files.isRegularFile(checked) || !Files.readString(checked).equals(value + "\n"))
            throw new IllegalStateException("Missing or incorrect nature QA marker: " + path);
    }
    private static String hash(Path path) throws Exception {
        return HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256").digest(Files.readAllBytes(path)));
    }
    private static List<Double> point(Vec3 point) { return List.of(point.x, point.y, point.z); }

    private static void writeReport(String status, Observation observation, String error) throws Exception {
        var report = new LinkedHashMap<String, Object>();
        report.put("status", status); report.put("artifact_sha256", artifactSha);
        report.put("scope", "One wide context image, a fixed duck camera, then one fixed camera at the first actually observed bird perch. No tracking camera or AI manipulation.");
        report.put("natural_generation_claimed", false); report.put("initial_spawn_reason", "NATURAL fixture");
        report.put("fixture_actors_persistent_from_setup", true);
        report.put("fixture_persistence_scope", "Six staged actors only: ordinary distance-based despawn cannot interrupt a fixed-camera lifecycle observation. No AI, phase, route or world wildlife policy changes.");
        report.put("harness_activity_forced_after_setup", false); report.put("harness_entity_teleports_after_setup", 0);
        report.put("planned_camera_cuts", List.of("context to duck pond", "duck pond to actual observed bird branch"));
        report.put("client_camera_initializations", clientCameraInitializations);
        report.put("pre_observation_terrain_readiness", PREPARATION);
        report.put("diagnostic_frames", DIAGNOSTICS);
        report.put("context_capture_before_actor_spawn", true);
        report.put("terrain_gate_scope", "Before actors spawn, plus one image-only readiness gate at the later bird camera cut; AI and server observation never pause.");
        report.put("bird_detail_requirement", "Actual rest, calendar departure, flight, and subsequent real landing; the camera stays at the first branch even if the next landing is out of frame.");
        report.put("maximum_world_ticks", MAX_TICKS); report.put("maximum_images", MAX_IMAGES);
        report.put("server_phase_frame_counts_with_client_entity_inside_camera_frustum", PHASE_FRAMES_IN_VIEW);
        report.put("audio_resources", audioResources); report.put("actual_duck_audio_channel_starts", DUCK_AUDIO_CHANNELS.get());
        report.put("actual_duck_audio_variants_started", DUCK_AUDIO_VARIANTS.stream().sorted().toList());
        var played = new LinkedHashMap<String, Integer>();
        NATURE_AUDIO_CHANNELS.forEach((name, count) -> played.put(name, count.get()));
        report.put("actual_nature_audio_channel_starts", played);
        report.put("audio_hearing_review_performed", false); report.put("audio_recording_captured", false);
        var client = Minecraft.getInstance();
        var clientState = new LinkedHashMap<String, Object>();
        clientState.put("sequence", sequence);
        clientState.put("screen", client.screen == null ? "none" : client.screen.getClass().getSimpleName());
        clientState.put("terrain_readiness", TERRAIN.snapshot());
        if (client.player != null) {
            clientState.put("position", point(client.player.position())); clientState.put("flying", client.player.getAbilities().flying);
            clientState.put("on_ground", client.player.onGround());
        }
        if (camera != null) clientState.put("requested_camera_position", point(camera));
        report.put("capture_client_state", clientState);
        report.put("visibility_limit", "Frustum counts are geometric only, not an occlusion or visual-quality approval. Off-screen stages remain disclosed in server telemetry.");
        report.put("visual_review_performed", false); report.put("frames", FRAMES); report.put("samples", SAMPLES);
        if (observation != null) { report.put("final_observation", observation.data()); report.put("cycles_complete", observation.complete()); }
        if (error != null) report.put("error", error);
        Files.writeString(output.resolve("manifest.json"), new GsonBuilder().setPrettyPrinting().create().toJson(report));
    }
    private static void fail(Minecraft client, Exception failure) {
        stopped = true; observing = false;
        DungeonsAlive.LOGGER.error("Nature showcase failed", failure);
        try { if (output != null) writeReport("failed", latest, failure.toString()); } catch (Exception ignored) { }
        // Only reached after successful authorize(); rejected unrelated sessions return above.
        if (started) client.stop();
    }

    private record Observation(long tick, int transitions, String sequence, Vec3 camera, long segmentStart,
                               boolean finished, boolean complete, String error, Map<String, Object> data) {
        @SuppressWarnings("unchecked") List<Map<String, Object>> animals() { return (List<Map<String, Object>>) data.get("animals"); }
    }

    private static final class AnimalTrace {
        private final UUID uuid;
        private final DungeonsCreature observedActor;
        private final String species;
        private final boolean bird;
        private final List<Map<String, Object>> transitions = new ArrayList<>();
        private Vec3 previous, restPosition;
        private String phase = "unobserved", error;
        private int progress, restTicks;
        private double distance, maximumStep, horizontalSwimmingDistance;
        private boolean complete, birdDetail, previouslySwimming;
        private Map<String, Object> current = Map.of();

        AnimalTrace(DungeonsCreature animal, String species, boolean bird) {
            uuid = animal.getUUID(); observedActor = animal; this.species = species; this.bird = bird; previous = animal.position();
        }
        void beginBirdDetail(Vec3 branch) {
            birdDetail = true; progress = 0; restTicks = 0; complete = false; restPosition = branch;
        }
        void observe(ServerLevel level, long tick) {
            if (!(level.getEntity(uuid) instanceof DungeonsCreature animal) || !animal.isAlive() || animal.isNoAi()) {
                var diagnostic = new LinkedHashMap<String, Object>();
                diagnostic.put("observed_failure_tick", tick);
                diagnostic.put("loaded_lookup_present", level.getEntity(uuid) != null);
                diagnostic.put("last_reference_removed", observedActor.isRemoved());
                diagnostic.put("removal_reason", observedActor.getRemovalReason() == null ? "none" : observedActor.getRemovalReason().name());
                diagnostic.put("alive", observedActor.isAlive()); diagnostic.put("no_ai", observedActor.isNoAi());
                diagnostic.put("health", observedActor.getHealth());
                diagnostic.put("persistence_required", observedActor.isPersistenceRequired());
                diagnostic.put("custom_persistence", observedActor.requiresCustomPersistence());
                diagnostic.put("no_action_ticks", observedActor.getNoActionTime());
                diagnostic.put("last_position", point(observedActor.position()));
                diagnostic.put("last_chunk_loaded", level.hasChunkAt(observedActor.blockPosition()));
                var nearest = level.getNearestPlayer(observedActor, -1);
                if (nearest != null) diagnostic.put("nearest_player_distance", nearest.distanceTo(observedActor));
                diagnostic.put("cause_limit", "RemovalReason records the actual entity removal category; DISCARDED alone does not prove a particular despawn call site.");
                var last = new LinkedHashMap<String, Object>(current); last.put("loss_diagnostic", Map.copyOf(diagnostic)); current = Map.copyOf(last);
                error = "Tracked AI entity disappeared or stopped: " + uuid + "; removal=" + diagnostic.get("removal_reason"); return;
            }
            if (!Double.isFinite(animal.getX()) || !Double.isFinite(animal.getY()) || !Double.isFinite(animal.getZ()))
                throw new IllegalStateException("Tracked entity developed a non-finite position: " + uuid);
            double step = animal.position().distanceTo(previous); distance += step; maximumStep = Math.max(maximumStep, step);
            if (step > 1.5) error = "Discontinuous motion exceeded 1.5 blocks/tick: " + uuid;
            boolean swimming = !bird && animal.isInWater() && animal.duckActivity().name().equals("SWIM");
            if (swimming && previouslySwimming)
                horizontalSwimmingDistance += Math.hypot(animal.getX() - previous.x, animal.getZ() - previous.z);
            previouslySwimming = swimming;
            previous = animal.position();
            String actual = bird ? animal.birdActivity().name() : animal.duckActivity().name();
            if (!actual.equals(phase)) {
                phase = actual; restTicks = 0;
                transitions.add(Map.of("tick", tick, "activity", phase, "position", point(previous), "on_ground", animal.onGround(),
                        "in_water", animal.isInWater(), "bird_perched", animal.birdPerched()));
            }
            if (bird && birdDetail) {
                if (progress == 0 && phase.equals("PERCHED") && animal.onGround() && animal.birdPerched() && ++restTicks >= 20) progress = 1;
                if (progress == 1 && phase.equals("TAKING_OFF")) {
                    var slots = animal.wildlifeMembers().stream().map(DungeonsCreature::wildlifeSlot).distinct().toList();
                    if (slots.isEmpty()) slots = List.of(animal.wildlifeSlot());
                    var calendar = BirdPerchCycle.sample(level.getGameTime(), animal.wildlifeGroupId(), animal.wildlifeSlot(), slots, false);
                    // A little on-branch shuffle is not the group's actual calendar departure.
                    if (calendar.intent() != BirdPerchCycle.Intent.REST) progress = 2;
                }
                if (progress == 2 && (phase.equals("FLYING") || phase.equals("APPROACHING"))
                        && !animal.onGround() && previous.distanceToSqr(restPosition) > 2.25) progress = 3;
                if (progress == 3 && phase.equals("LANDING")) progress = 4;
                if (progress >= 3 && phase.equals("PERCHED") && animal.onGround() && animal.birdPerched()) complete = true;
            } else {
                if (bird) { snapshotAnimal(animal); return; }
                if (progress == 0 && phase.equals("SWIM") && animal.isInWater()) progress = 1;
                if (progress == 1 && phase.equals("TO_SHORE")) progress = 2;
                if (progress == 2 && phase.equals("FORAGE") && animal.onGround() && !animal.isInWater()) progress = 3;
                if (progress == 3 && phase.equals("PREEN") && animal.onGround() && !animal.isInWater()) progress = 4;
                if (progress == 4 && phase.equals("REST") && animal.onGround() && !animal.isInWater() && ++restTicks >= 40) progress = 5;
                if (progress == 5 && phase.equals("TO_WATER")) progress = 6;
                if (progress == 6 && phase.equals("SWIM") && animal.isInWater() && horizontalSwimmingDistance > 3) complete = true;
            }
            snapshotAnimal(animal);
        }
        private void snapshotAnimal(DungeonsCreature animal) {
            var record = new LinkedHashMap<String, Object>();
            record.put("uuid", uuid.toString()); record.put("species", species); record.put("bird", bird); record.put("position", point(previous));
            record.put("activity", phase); record.put("on_ground", animal.onGround()); record.put("in_water", animal.isInWater());
            record.put("bird_perched", animal.birdPerched()); record.put("duck_young", animal.duckYoung());
            record.put("has_live_path", !animal.getNavigation().isDone()); record.put("distance_travelled", distance);
            var path = animal.getNavigation().getPath();
            if (path != null) record.put("path_progress", Map.of("next_index", path.getNextNodeIndex(), "node_count", path.getNodeCount(),
                    "next_node", path.isDone() ? List.of() : List.of(path.getNextNodePos().getX(), path.getNextNodePos().getY(), path.getNextNodePos().getZ())));
            record.put("persistence_required", animal.isPersistenceRequired());
            record.put("horizontal_swim_distance_in_actual_water", horizontalSwimmingDistance);
            record.put("maximum_step", maximumStep); record.put("observed_sequence_progress", progress);
            record.put("cycle_complete", complete); record.put("transitions", List.copyOf(transitions));
            record.put("focal_bird_detail", birdDetail);
            if (animal.wildlifeGroupId() != null) record.put("group_uuid", animal.wildlifeGroupId().toString());
            record.put("slot", animal.wildlifeSlot());
            if (animal.birdPerch() != null) record.put("perch_block", List.of(animal.birdPerch().getX(), animal.birdPerch().getY(), animal.birdPerch().getZ()));
            current = Map.copyOf(record);
        }
        Map<String, Object> snapshot() { return current; }
    }
}
