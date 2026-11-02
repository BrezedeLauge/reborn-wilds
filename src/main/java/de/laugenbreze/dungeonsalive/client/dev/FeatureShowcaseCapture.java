package de.laugenbreze.dungeonsalive.client.dev;

import com.google.gson.GsonBuilder;
import de.laugenbreze.dungeonsalive.DungeonsAlive;
import de.laugenbreze.dungeonsalive.client.GearGuideScreen;
import de.laugenbreze.dungeonsalive.entity.DungeonMob;
import de.laugenbreze.dungeonsalive.entity.ModEntities;
import de.laugenbreze.dungeonsalive.entity.construct.*;
import de.laugenbreze.dungeonsalive.item.gear.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Screenshot;
import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.client.event.RenderFrameEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.util.*;

/** Explicit isolated developer scene: native item GUI, actual timed constructs and a real held beacon. */
public final class FeatureShowcaseCapture {
    private static final String TOKEN = "dungeonsalive-isolated-feature-showcase-v1";
    private static final double X = 480.5, Y = 91, Z = 480.5;
    private static final Vec3 CAMERA = new Vec3(X + 8, Y + 2, Z - 11);
    private static final Vec3 BEAM_ORIGIN = new Vec3(X - 6, Y, Z - 2);
    private static final List<String> GEAR = new ArrayList<>();
    private static final List<Integer> SCENES = new ArrayList<>();
    private static final List<Map<String, Object>> CAPTURES = new ArrayList<>();
    private static final List<Map<String, Object>> CLIPS = new ArrayList<>();
    private static MinecraftServer server;
    private static Path output;
    private static UUID playerId;
    private static GearGuideScreen guide;
    private static boolean started, ready, queued, stopped, mainCaptured, beamFirstPersonStaged;
    private static int index, frames;
    private static long stageStarted, lastFrameTick = -1;
    private static volatile long worldTick;
    private static DungeonConstruct construct;
    private static LivingEntity beamTarget;
    private static Entity observer;
    private static final List<Map<String, Object>> currentClip = new ArrayList<>();
    private FeatureShowcaseCapture() {}

    public static void register() {
        if (FMLEnvironment.production || !Boolean.getBoolean("dungeonsalive.featureShowcase")) return;
        NeoForge.EVENT_BUS.addListener(FeatureShowcaseCapture::frame);
        NeoForge.EVENT_BUS.addListener(FeatureShowcaseCapture::tick);
    }
    private static void tick(ServerTickEvent.Post event) {
        if (event.getServer() == server) worldTick = event.getServer().overworld().getGameTime();
    }

    private static void frame(RenderFrameEvent.Post event) {
        Minecraft client = Minecraft.getInstance();
        if (stopped || client.level == null || client.player == null || client.getSingleplayerServer() == null) return;
        try {
            if (!started) {
                Path directory = client.gameDirectory.toPath().toAbsolutePath().normalize();
                if (!directory.getFileName().toString().equals("run-feature-showcase")
                        || !Files.readString(directory.resolve("dungeonsalive-feature-showcase.txt")).trim().equals(TOKEN))
                    throw new IllegalStateException("Feature showcase requires the marked isolated development world");
                output = directory.resolve("captures").resolve(java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss")));
                Files.createDirectories(output);
                String selection = System.getProperty("dungeonsalive.featureShowcaseSelection", "all");
                if (!Set.of("all", "gear", "constructs", "beacon").contains(selection))
                    throw new IllegalArgumentException("Unknown feature showcase selection: " + selection);
                if (selection.equals("all") || selection.equals("gear")) {
                    GEAR.add("wind_horn");
                    Arrays.stream(ArtifactKind.values()).forEach(kind -> GEAR.add(kind.id));
                    Arrays.stream(WeaponKind.values()).forEach(kind -> GEAR.add(kind.id));
                    Arrays.stream(RangedWeaponKind.values()).forEach(kind -> GEAR.add(kind.id));
                    for (ArmorSetKind kind : ArmorSetKind.values()) for (var type : ArmorContent.PIECES)
                        GEAR.add(kind.id + "_" + type.getName());
                }
                if (selection.equals("all") || selection.equals("constructs"))
                    for (int i = 0; i < ConstructKind.values().length; i++) SCENES.add(i);
                if (selection.equals("all") || selection.equals("beacon")) SCENES.add(ConstructKind.values().length);
                server = client.getSingleplayerServer(); playerId = client.player.getUUID(); started = true;
                server.execute(() -> {
                    try { build(server); client.execute(() -> ready = true); }
                    catch (Exception ex) { client.execute(() -> fail(client, ex)); }
                });
                return;
            }
            if (!ready) return;
            if (index < GEAR.size()) {
                if (guide == null) { guide = new GearGuideScreen(); client.setScreen(guide); }
                if (frames == 0) guide.selectForCapture(GEAR.get(index));
                if (++frames < 18) return;
                capture(client, String.format("gear/%02d-%s.png", index + 1, GEAR.get(index)), "native_item_gui", false);
                index++; frames = 0;
                return;
            }
            int sceneOrdinal = index - GEAR.size();
            if (sceneOrdinal >= SCENES.size()) { finish(client); return; }
            int sceneIndex = SCENES.get(sceneOrdinal);
            if (!queued) {
                queued = true; ready = false; mainCaptured = false; lastFrameTick = -1; currentClip.clear();
                beamFirstPersonStaged = false;
                client.setCameraEntity(client.player); observer = null;
                client.setScreen(null); client.options.hideGui = true; client.options.fov().set(44);
                client.particleEngine.setLevel(client.level);
                server.execute(() -> {
                    try { stage(server, sceneIndex); client.execute(() -> ready = true); }
                    catch (Exception ex) { client.execute(() -> fail(client, ex)); }
                });
                return;
            }
            long elapsed = worldTick - stageStarted;
            boolean beacon = sceneIndex == ConstructKind.values().length;
            if (client.player.distanceToSqr(beacon ? BEAM_ORIGIN : CAMERA) > 1) return;
            if (beacon && observer == null && !beamFirstPersonStaged) {
                // Detached observer camera only; the real server player still
                // holds, aims and uses the actual artifact at BEAM_ORIGIN.
                observer = EntityType.ARMOR_STAND.create(client.level);
                if (observer == null) throw new IllegalStateException("Missing local observer camera");
                Vec3 delta = new Vec3(X - 2, Y + 1.2, Z).subtract(CAMERA.add(0, observer.getEyeHeight(), 0));
                float yaw = (float) Math.toDegrees(Math.atan2(-delta.x, delta.z));
                float pitch = (float) -Math.toDegrees(Math.atan2(delta.y, Math.sqrt(delta.x * delta.x + delta.z * delta.z)));
                observer.moveTo(CAMERA.x, CAMERA.y, CAMERA.z, yaw, pitch);
                if (observer instanceof LivingEntity living) {
                    living.setYHeadRot(yaw); living.yHeadRotO = yaw;
                    living.setYBodyRot(yaw); living.yBodyRotO = yaw;
                }
                observer.setOldPosAndRot(); client.setCameraEntity(observer);
                return;
            }
            String subject = beacon ? "corrupted_beacon" : ConstructKind.values()[sceneIndex].id();
            int sequenceEnd = beacon ? 80 : ConstructKind.values()[sceneIndex].maximumLifetimeTicks() + 8;
            if (elapsed >= 4 && elapsed <= sequenceEnd && worldTick != lastFrameTick && elapsed % 4 == 0) {
                lastFrameTick = worldTick;
                Map<String, Object> record = capture(client, "sequences/" + subject + "/" + String.format("%04d", elapsed) + ".png", "native_world_sequence", true);
                currentClip.add(record);
            }
            int contact = beacon ? 38 : switch (ConstructKind.values()[sceneIndex]) {
                case GEOMANCER_WALL -> 35;
                case GEOMANCER_BOMB -> 43;
                case ENTANGLE_VINE -> 18;
                case ICE_CHUNK -> 30;
            };
            if (!mainCaptured && elapsed >= contact) {
                if (!beacon && (construct == null || construct.isRemoved())) throw new IllegalStateException("Construct expired before its native main view: " + subject);
                capture(client, "world/" + subject + ".png", "native_world_frame", false);
                mainCaptured = true;
            }
            if (elapsed >= sequenceEnd + 4) {
                if (beacon && !beamFirstPersonStaged) {
                    beamFirstPersonStaged = true; observer = null;
                    client.setCameraEntity(client.player); return;
                }
                if (beacon && elapsed < sequenceEnd + 8) return;
                if (beacon) capture(client, "world/corrupted_beacon-first-person.png", "native_world_first_person_check", false);
                CLIPS.add(Map.of("subject", subject, "frames", List.copyOf(currentClip), "timing", "actual_server_ticks"));
                index++; queued = false; frames = 0;
            }
        } catch (Exception exception) { fail(client, exception); }
    }

    private static void build(MinecraftServer server) {
        ServerLevel level = server.overworld();
        level.setDayTime(6000);
        level.setWeatherParameters(60000, 0, false, false);
        level.getGameRules().getRule(GameRules.RULE_DOMOBSPAWNING).set(false, server);
        level.getGameRules().getRule(GameRules.RULE_DAYLIGHT).set(false, server);
        for (int x = -18; x <= 18; x++) for (int z = -16; z <= 18; z++) {
            for (int y = 0; y <= 13; y++) level.setBlock(BlockPos.containing(X + x, Y + y, Z + z), Blocks.AIR.defaultBlockState(), 3);
            level.setBlock(BlockPos.containing(X + x, Y - 1, Z + z), (Math.floorMod(x + z, 4) == 0 ? Blocks.POLISHED_ANDESITE : Blocks.STONE_BRICKS).defaultBlockState(), 3);
        }
        for (int x = -18; x <= 18; x++) for (int y = 0; y <= 9; y++)
            level.setBlock(BlockPos.containing(X + x, Y + y, Z + 14), (x % 6 == 0 ? Blocks.DEEPSLATE_TILES : Blocks.LIGHT_GRAY_CONCRETE).defaultBlockState(), 3);
        ServerPlayer player = server.getPlayerList().getPlayer(playerId);
        if (player == null) throw new IllegalStateException("Missing showcase player");
        player.setGameMode(GameType.SURVIVAL); player.setInvulnerable(true); player.setNoGravity(true);
        player.getFoodData().setFoodLevel(20); player.getInventory().clearContent();
        placeCamera(player, level);
    }

    private static void stage(MinecraftServer server, int sceneIndex) {
        ServerLevel level = server.overworld();
        ServerPlayer player = server.getPlayerList().getPlayer(playerId);
        if (player == null) throw new IllegalStateException("Missing showcase player");
        player.stopUsingItem(); player.getInventory().clearContent();
        for (Entity entity : level.getEntitiesOfClass(Entity.class, new AABB(X - 25, Y - 5, Z - 25, X + 25, Y + 20, Z + 25), entity -> !(entity instanceof Player))) entity.discard();
        construct = null; beamTarget = null;
        placeCamera(player, level);
        if (sceneIndex == ConstructKind.values().length) {
            var zombie = EntityType.HUSK.create(level);
            if (zombie == null) throw new IllegalStateException("Could not create actual beam target");
            zombie.moveTo(X, Y, Z, 180, 0); zombie.setNoAi(true); zombie.setPersistenceRequired();
            zombie.getAttribute(net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH).setBaseValue(200);
            zombie.setHealth(200); level.addFreshEntity(zombie); beamTarget = zombie;
            player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(GearContent.artifact(ArtifactKind.CORRUPTED_BEACON)));
            Vec3 delta = zombie.getBoundingBox().getCenter().subtract(BEAM_ORIGIN.add(0, player.getEyeHeight(), 0));
            float yaw = (float) Math.toDegrees(Math.atan2(-delta.x, delta.z));
            float pitch = (float) -Math.toDegrees(Math.atan2(delta.y, Math.sqrt(delta.x * delta.x + delta.z * delta.z)));
            player.teleportTo(level, BEAM_ORIGIN.x, BEAM_ORIGIN.y, BEAM_ORIGIN.z, yaw, pitch);
            SoulEnergy.set(player, 100);
            GearContent.artifact(ArtifactKind.CORRUPTED_BEACON).use(level, player, InteractionHand.MAIN_HAND);
        } else {
            ConstructKind kind = ConstructKind.values()[sceneIndex];
            String casterId = kind == ConstructKind.ICE_CHUNK ? "iceologer" : kind == ConstructKind.ENTANGLE_VINE ? "whisperer" : "geomancer";
            DungeonMob caster = ModEntities.type(casterId).create(level);
            var target = EntityType.IRON_GOLEM.create(level);
            if (caster == null || target == null) throw new IllegalStateException("Could not create construct actors");
            caster.moveTo(X - 3.5, Y, Z + 3, 180, 0); caster.setNoAi(true); caster.setPersistenceRequired(); level.addFreshEntity(caster);
            target.moveTo(X, Y, Z + 2.4, 180, 0); target.setNoAi(true); target.setInvulnerable(true); target.setPersistenceRequired(); level.addFreshEntity(target);
            construct = ConstructService.spawn(caster, target, kind,
                    kind == ConstructKind.ENTANGLE_VINE || kind == ConstructKind.ICE_CHUNK ? target.position() : new Vec3(X, Y, Z), 8);
            if (construct == null) throw new IllegalStateException("Safe construct placement rejected " + kind);
        }
        stageStarted = level.getGameTime();
        DungeonsAlive.LOGGER.info("Feature showcase staged actual server scene {}", sceneIndex);
    }

    private static void placeCamera(ServerPlayer player, ServerLevel level) {
        Vec3 aim = new Vec3(X, Y + 1.25, Z);
        Vec3 delta = aim.subtract(CAMERA.add(0, player.getEyeHeight(), 0));
        float yaw = (float) Math.toDegrees(Math.atan2(-delta.x, delta.z));
        float pitch = (float) -Math.toDegrees(Math.atan2(delta.y, Math.sqrt(delta.x * delta.x + delta.z * delta.z)));
        player.teleportTo(level, CAMERA.x, CAMERA.y, CAMERA.z, yaw, pitch);
    }

    private static Map<String, Object> capture(Minecraft client, String relative, String kind, boolean sequence) throws Exception {
        Path path = output.resolve(relative); Files.createDirectories(path.getParent());
        try (var pixels = Screenshot.takeScreenshot(client.getMainRenderTarget())) { pixels.writeToFile(path); }
        Map<String, Object> record = new LinkedHashMap<>();
        record.put("file", relative); record.put("kind", kind); record.put("server_tick", worldTick);
        record.put("elapsed", worldTick - stageStarted);
        record.put("sha256", HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256").digest(Files.readAllBytes(path))));
        record.put("resolution", List.of(client.getWindow().getWidth(), client.getWindow().getHeight()));
        if (index < GEAR.size()) record.put("item", "dungeonsalive:" + GEAR.get(index));
        else {
            var camera = client.gameRenderer.getMainCamera().getPosition();
            record.put("camera", List.of(camera.x, camera.y, camera.z));
            if (construct != null) { record.put("entity_uuid", construct.getUUID().toString()); record.put("construct", construct.kind().id()); record.put("phase", construct.phase().name()); record.put("removed", construct.isRemoved()); }
            if (beamTarget != null) {
                record.put("target_uuid", beamTarget.getUUID().toString()); record.put("target_health", beamTarget.getHealth());
                record.put("camera_control", observer == null ? "actual_player_first_person" : "detached_observer_of_actual_server_player_use");
                record.put("user_uuid", playerId.toString());
            }
        }
        if (!sequence) CAPTURES.add(record);
        return record;
    }
    private static void finish(Minecraft client) throws Exception {
        Map<String, Object> report = new LinkedHashMap<>();
        report.put("capture_kind", "native_minecraft_gear_and_construct_checks"); report.put("complete", true);
        report.put("selection", System.getProperty("dungeonsalive.featureShowcaseSelection", "all"));
        report.put("planned_main_frames", GEAR.size() + SCENES.size() + (SCENES.contains(ConstructKind.values().length) ? 1 : 0));
        report.put("staging", "marked isolated developer scene; actual baked item GUI, server-owned timed constructs and held artifact use");
        report.put("captures", CAPTURES); report.put("clips", CLIPS);
        Files.writeString(output.resolve("manifest.json"), new GsonBuilder().setPrettyPrinting().create().toJson(report));
        Files.writeString(client.gameDirectory.toPath().resolve("latest-capture.json"), "{\"manifest\":\"captures/" + output.getFileName() + "/manifest.json\"}");
        stopped = true; DungeonsAlive.LOGGER.info("Feature showcase completed: {} native main views and {} server sequences", CAPTURES.size(), CLIPS.size()); client.stop();
    }
    private static void fail(Minecraft client, Exception exception) {
        stopped = true; DungeonsAlive.LOGGER.error("Feature showcase failed", exception); client.stop();
    }
}
