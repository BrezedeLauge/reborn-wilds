package de.laugenbreze.dungeonsalive.client.dev;

import com.google.gson.GsonBuilder;
import de.laugenbreze.dungeonsalive.DungeonsAlive;
import de.laugenbreze.dungeonsalive.entity.peaceful.DungeonsCreature;
import de.laugenbreze.dungeonsalive.entity.peaceful.DungeonsMerchant;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Screenshot;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.BackupConfirmScreen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Difficulty;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.storage.LevelResource;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.client.event.RenderFrameEvent;
import net.neoforged.neoforge.common.NeoForge;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HexFormat;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * Photographs the actual optional-pack templates and their real server NPCs.
 * Deliberately placed in an explicitly marked development copy, not evidence of
 * natural world generation. Inactive in production, multiplayer and ordinary saves.
 */
public final class CampShowcaseCapture {
    private static final String TOKEN = "dungeonsalive-isolated-camp-showcase-v1";
    private static final String RUN_NAME = "run-camp-showcase";
    private static final String WORLD_NAME = "camp-showcase";
    private static final int FLOOR = 80;
    private static final BlockPos MERCHANT_ORIGIN = new BlockPos(400, FLOOR, 400);
    private static final BlockPos CAPTIVE_ORIGIN = new BlockPos(424, FLOOR, 400);
    private static final DateTimeFormatter RUN_TIME = DateTimeFormatter.ofPattern("uuuuMMdd-HHmmss-SSS").withZone(ZoneOffset.UTC);

    private record Camp(String template, BlockPos origin, String templateSha256,
                        UUID npcUuid, int npcId, String entityType, int offers) {}
    private record Shot(String file, int camp, Vec3 feet, Vec3 aim, int fov, boolean rescue) {}
    private record ServerState(boolean rescued, boolean waiting, boolean noAi,
                               String interaction, String rescuer, long gameTick) {}
    private static final List<Shot> SHOTS = List.of(
            new Shot("01-merchant-camp.png", 0, new Vec3(410.5, FLOOR + 1, 390.5),
                    new Vec3(404.3, FLOOR + 2.1, 404.5), 48, false),
            new Shot("02-captive-camp.png", 1, new Vec3(433.3, FLOOR + 1, 390.5),
                    new Vec3(429.3, FLOOR + 2.0, 404.8), 48, false),
            new Shot("03-blacksmith-rescued.png", 1, new Vec3(430.5, FLOOR + 1, 396.5),
                    new Vec3(429.5, FLOOR + 2.1, 405.5), 50, true)
    );
    private static final List<Map<String, Object>> CAPTURES = new ArrayList<>();
    private static final Set<UUID> REBOUND_IDS = new HashSet<>();
    private static List<Camp> camps = List.of();
    private static Path output;
    private static String runId, failure;
    private static UUID playerId;
    private static ServerState serverState;
    private static boolean preflightVerified, experimentalDialogConfirmed, verified, setupQueued, setupReady, poseQueued, poseReady, stopped;
    private static int shotIndex, frames;
    private static long started, lastStatus;

    private CampShowcaseCapture() {}

    public static void register() {
        if (FMLEnvironment.production || !Boolean.getBoolean("dungeonsalive.campShowcase")) return;
        started = System.nanoTime();
        NeoForge.EVENT_BUS.addListener(CampShowcaseCapture::rendered);
    }

    private static void rendered(RenderFrameEvent.Post event) {
        Minecraft client = Minecraft.getInstance();
        if (stopped) return;
        try {
            if (!preflightVerified) {
                verifyRunDirectory(client.gameDirectory.toPath());
                runId = RUN_TIME.format(Instant.now());
                output = client.gameDirectory.toPath().resolve("captures").resolve(runId);
                Files.createDirectories(output);
                preflightVerified = true;
                writeReport(client, false);
            }
            if (System.nanoTime() - started > 300_000_000_000L)
                throw new IllegalStateException("Camp showcase did not complete within five minutes");
            MinecraftServer server = client.getSingleplayerServer();
            if (server == null || client.level == null || client.player == null) {
                confirmMarkedExperimentalWorld(client);
                return;
            }
            if (!verified) {
                verifySandbox(client.gameDirectory.toPath(), server);
                playerId = client.player.getUUID();
                verified = true;
                writeReport(client, false);
            }
            if (System.nanoTime() - lastStatus > 5_000_000_000L) {
                lastStatus = System.nanoTime();
                Camp expected = camps.isEmpty() ? null : camps.get(SHOTS.get(shotIndex).camp());
                Entity bySpawnId = expected == null ? null : client.level.getEntity(expected.npcId());
                Mob byUuid = expected == null ? null : findClientNpc(client, expected);
                DungeonsAlive.LOGGER.info("Camp showcase: shot={} setup={}/{} pose={}/{} frames={} player={} screen={} expectedUuid={} spawnId={} trackedAtSpawnId={} matchedByUuid={}",
                        shotIndex, setupQueued, setupReady, poseQueued, poseReady, frames, client.player.position(),
                        client.screen == null ? "none" : client.screen.getClass().getSimpleName(),
                        expected == null ? "not staged" : expected.npcUuid(), expected == null ? -1 : expected.npcId(),
                        bySpawnId == null ? "none" : bySpawnId.getUUID(), byUuid == null ? "none" : byUuid.getId());
            }
            if (!setupQueued && client.screen != null) return;
            if (!setupQueued) {
                setupQueued = true;
                server.execute(() -> {
                    try {
                        verifySandbox(client.gameDirectory.toPath(), server);
                        List<Camp> placed = buildScene(server);
                        client.execute(() -> { camps = placed; setupReady = true; });
                    } catch (Exception error) { client.execute(() -> fail(client, error)); }
                });
                return;
            }
            if (!setupReady) return;
            Shot shot = SHOTS.get(shotIndex);
            Camp camp = camps.get(shot.camp());
            // Teleport before waiting for entity tracking: the initial copied-world
            // spawn may lie outside the server's tracking distance from this camp.
            if (!poseQueued) {
                poseQueued = true;
                server.execute(() -> {
                    try {
                        ServerState state = prepareShot(server, camp, shot);
                        client.execute(() -> {
                            serverState = state;
                            client.options.fov().set(shot.fov());
                            client.options.hideGui = true;
                            client.options.bobView().set(false);
                            client.options.setCameraType(CameraType.FIRST_PERSON);
                            client.setScreen(null);
                            client.gui.getChat().clearMessages(false);
                            frames = 0;
                            poseReady = true;
                        });
                    } catch (Exception error) { client.execute(() -> fail(client, error)); }
                });
                return;
            }
            if (!poseReady || client.screen != null || client.player.distanceToSqr(shot.feet()) > .5) return;
            Mob mob = findClientNpc(client, camp);
            if (mob == null) return;
            if (mob instanceof DungeonsCreature civilian && civilian.rescued() != shot.rescue()) return;
            if (++frames < (shot.rescue() ? 12 : shotIndex == 0 ? 90 : 50)) return;
            capture(client, camp, mob, shot);
            shotIndex++;
            poseQueued = false;
            poseReady = false;
            frames = 0;
            boolean complete = shotIndex == SHOTS.size();
            writeReport(client, complete);
            if (complete) {
                stopped = true;
                DungeonsAlive.LOGGER.info("Camp showcase completed: {} native template/NPC frames", CAPTURES.size());
                client.stop();
            }
        } catch (Exception error) { fail(client, error); }
    }

    private static Mob findClientNpc(Minecraft client, Camp camp) {
        // Numeric network IDs can change after a chunk unload/reload between
        // template placement and player arrival. The real server UUID is stable.
        // Keep exact identity and type checks; never substitute another merchant.
        for (Entity entity : client.level.entitiesForRendering()) {
            if (!entity.getUUID().equals(camp.npcUuid())) continue;
            if (!(entity instanceof Mob mob) || !BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType()).toString().equals(camp.entityType()))
                throw new IllegalStateException("The captured camp UUID has an unexpected entity type");
            if (entity.getId() != camp.npcId() && REBOUND_IDS.add(camp.npcUuid()))
                DungeonsAlive.LOGGER.info("Camp showcase retained exact NPC UUID {} after numeric entity id changed {} -> {}",
                        camp.npcUuid(), camp.npcId(), entity.getId());
            return mob;
        }
        return null;
    }

    private static Path verifyRunDirectory(Path directory) throws Exception {
        Path run = directory.toAbsolutePath().normalize();
        Path expected = run.resolve("saves").resolve(WORLD_NAME).normalize();
        if (!RUN_NAME.equals(run.getFileName().toString()) || Files.isSymbolicLink(run)
                || Files.isSymbolicLink(run.resolve("saves")) || Files.isSymbolicLink(expected)
                || !TOKEN.equals(Files.readString(run.resolve("dungeonsalive-camp-showcase.txt")).trim())
                || !TOKEN.equals(Files.readString(expected.resolve("dungeonsalive-showcase-world.txt")).trim())
                || !Files.isRegularFile(expected.resolve("level.dat"))
                || !Files.isRegularFile(expected.resolve("datapacks/dungeons_camps/pack.mcmeta"))) {
            throw new IllegalStateException("Refusing to alter an unmarked camp showcase world or missing optional pack");
        }
        // Before an integrated server exists, the experimental warning does not
        // expose its level ID. Require this isolated run to contain only our one
        // explicitly marked save, so no other world's warning can be accepted.
        try (var saves = Files.list(run.resolve("saves"))) {
            if (saves.anyMatch(path -> Files.isDirectory(path) && !path.toAbsolutePath().normalize().equals(expected)))
                throw new IllegalStateException("Camp preflight refuses a run containing another saved world");
        }
        return expected;
    }

    private static void verifySandbox(Path directory, MinecraftServer server) throws Exception {
        Path expected = verifyRunDirectory(directory);
        Path actual = server.getWorldPath(LevelResource.ROOT).toAbsolutePath().normalize();
        if (!actual.equals(expected)) throw new IllegalStateException("Integrated server opened an unexpected camp showcase world");
    }

    private static void confirmMarkedExperimentalWorld(Minecraft client) throws Exception {
        if (experimentalDialogConfirmed || !(client.screen instanceof BackupConfirmScreen screen)) return;
        verifyRunDirectory(client.gameDirectory.toPath());
        // Verified against 1.21.1 WorldOpenFlows.askForBackup and BackupConfirmScreen.
        // Use the ordinary button callback for only this specific experimental
        // dialog. Never suppress datapack failures, version warnings or global settings.
        String expectedTitle = Component.translatable("selectWorld.backupQuestion.experimental").getString();
        if (!screen.getTitle().getString().equals(expectedTitle))
            throw new IllegalStateException("Camp preflight will not accept a different backup/version warning: " + screen.getTitle().getString());
        String proceedText = Component.translatable("selectWorld.backupJoinSkipButton").getString();
        var buttons = screen.children().stream().filter(child -> child instanceof Button)
                .map(child -> (Button) child)
                .filter(button -> button.active && button.visible && button.getMessage().getString().equals(proceedText)).toList();
        if (buttons.size() != 1) throw new IllegalStateException("Expected exactly one known experimental-world Continue button");
        experimentalDialogConfirmed = true;
        DungeonsAlive.LOGGER.info("Camp preflight: confirming the observed experimental warning only for the marked {} development copy", WORLD_NAME);
        buttons.getFirst().onPress();
        writeReport(client, false);
    }

    private static List<Camp> buildScene(MinecraftServer server) throws Exception {
        ServerLevel level = server.overworld();
        ServerPlayer player = player(server);
        server.setDifficulty(Difficulty.NORMAL, true);
        level.getGameRules().getRule(GameRules.RULE_DOMOBSPAWNING).set(false, server);
        level.getGameRules().getRule(GameRules.RULE_DAYLIGHT).set(false, server);
        level.getGameRules().getRule(GameRules.RULE_WEATHER_CYCLE).set(false, server);
        level.getGameRules().getRule(GameRules.RULE_MOBGRIEFING).set(false, server);
        level.setDayTime(6000);
        level.setWeatherParameters(60_000, 0, false, false);
        // Hold the destination with the real player's normal chunk tickets before
        // creating template NPCs; previously the first teleport happened a later tick.
        Shot first = SHOTS.getFirst();
        player.teleportTo(level, first.feet().x, first.feet().y, first.feet().z, 0, 0);
        player.setDeltaMovement(Vec3.ZERO);
        // A bounded, deliberately level photography site. The NBT templates are
        // loaded unchanged from the enabled pack, including their actual entities.
        for (int cx = 23; cx <= 28; cx++) for (int cz = 23; cz <= 27; cz++) level.getChunk(cx, cz);
        AABB site = new AABB(375, FLOOR - 2, 375, 451, FLOOR + 12, 431);
        for (Entity entity : level.getEntitiesOfClass(Entity.class, site, e -> !(e instanceof Player))) entity.discard();
        for (int x = 375; x <= 450; x++) for (int z = 375; z <= 430; z++) {
            level.setBlock(new BlockPos(x, FLOOR - 1, z), Blocks.DIRT.defaultBlockState(), Block.UPDATE_CLIENTS);
            level.setBlock(new BlockPos(x, FLOOR, z), Blocks.GRASS_BLOCK.defaultBlockState(), Block.UPDATE_CLIENTS);
            for (int y = FLOOR + 1; y <= FLOOR + 10; y++)
                level.setBlock(new BlockPos(x, y, z), Blocks.AIR.defaultBlockState(), Block.UPDATE_CLIENTS);
        }
        Camp merchant = placeCamp(level, "merchant", MERCHANT_ORIGIN, "village_merchant", "dungeonsalive_camp_merchant");
        Camp captive = placeCamp(level, "captive", CAPTIVE_ORIGIN, "captured_blacksmith", "dungeonsalive_camp_captive");
        player.setGameMode(GameType.SURVIVAL);
        player.setInvulnerable(true);
        player.setHealth(player.getMaxHealth());
        player.getFoodData().setFoodLevel(20);
        player.getInventory().clearContent();
        player.closeContainer();
        return List.of(merchant, captive);
    }

    private static Camp placeCamp(ServerLevel level, String name, BlockPos origin, String entityId, String tag) throws Exception {
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(DungeonsAlive.MOD_ID, "camps/" + name);
        var template = level.getStructureManager().get(id).orElseThrow(() -> new IllegalStateException("Missing enabled camp template " + id));
        String hash;
        try (var stream = level.getServer().getResourceManager().open(
                ResourceLocation.fromNamespaceAndPath(DungeonsAlive.MOD_ID, "structure/camps/" + name + ".nbt"))) {
            hash = sha256(stream.readAllBytes());
        }
        if (!template.placeInWorld(level, origin, origin,
                new StructurePlaceSettings().setIgnoreEntities(false).setFinalizeEntities(true), level.random, 3))
            throw new IllegalStateException("Could not place actual camp template " + id);
        var size = template.getSize();
        AABB bounds = new AABB(origin.getX(), origin.getY(), origin.getZ(),
                origin.getX() + size.getX(), origin.getY() + size.getY(), origin.getZ() + size.getZ()).inflate(.1);
        var npcs = level.getEntitiesOfClass(Mob.class, bounds, mob -> mob.getTags().contains(tag));
        if (npcs.size() != 1) throw new IllegalStateException("Expected exactly one source-template NPC in " + id + ", got " + npcs.size());
        Mob npc = npcs.getFirst();
        String type = BuiltInRegistries.ENTITY_TYPE.getKey(npc.getType()).toString();
        if (!(DungeonsAlive.MOD_ID + ":" + entityId).equals(type)) throw new IllegalStateException("Wrong template NPC " + type);
        int offers = npc instanceof DungeonsMerchant merchant ? merchant.getOffers().size() : 0;
        if (npc instanceof DungeonsMerchant && offers == 0) throw new IllegalStateException("Camp merchant has no actual trades");
        if (npc instanceof DungeonsCreature civilian && (!civilian.civilian() || civilian.rescued() || !civilian.waiting()))
            throw new IllegalStateException("Captured NPC did not load its unrescued waiting state");
        npc.setNoAi(true); // Explicit photographic staging; not the natural camp NPC's behavior.
        npc.setPersistenceRequired();
        npc.setYRot(180);
        npc.setYBodyRot(180);
        npc.setYHeadRot(180);
        return new Camp(id.toString(), origin, hash, npc.getUUID(), npc.getId(), type, offers);
    }

    private static ServerState prepareShot(MinecraftServer server, Camp camp, Shot shot) {
        ServerLevel level = server.overworld();
        ServerPlayer player = player(server);
        if (!(level.getEntity(camp.npcUuid()) instanceof Mob npc)) throw new IllegalStateException("Camp NPC disappeared");
        String interaction = "not_requested";
        if (shot.rescue()) {
            if (!(npc instanceof DungeonsCreature civilian) || !civilian.civilian() || civilian.rescued())
                throw new IllegalStateException("Rescue shot requires the original unrescued civilian");
            // Opening the ordinary gate is a disclosed staging action, independent
            // of rescue. The actual mobInteract call must pass the real range/LOS checks.
            BlockPos gate = camp.origin().offset(5, 1, 3);
            var state = level.getBlockState(gate);
            if (!state.is(Blocks.SPRUCE_FENCE_GATE)) throw new IllegalStateException("The template's ordinary gate is missing");
            level.setBlock(gate, state.setValue(BlockStateProperties.OPEN, true), 3);
            player.teleportTo(level, civilian.getX(), civilian.getY(), civilian.getZ() - 1, 0, 0);
            var result = civilian.mobInteract(player, InteractionHand.MAIN_HAND);
            interaction = result.toString();
            if (!result.consumesAction() || !civilian.rescued() || civilian.waiting() || !player.getUUID().equals(civilian.ownerId()))
                throw new IllegalStateException("Genuine server rescue interaction failed");
        }
        double dx = shot.aim().x - shot.feet().x, dz = shot.aim().z - shot.feet().z;
        double dy = shot.aim().y - (shot.feet().y + player.getEyeHeight());
        float yaw = (float)Math.toDegrees(Math.atan2(-dx, dz));
        float pitch = (float)-Math.toDegrees(Math.atan2(dy, Math.sqrt(dx * dx + dz * dz)));
        player.teleportTo(level, shot.feet().x, shot.feet().y, shot.feet().z, yaw, pitch);
        player.setDeltaMovement(Vec3.ZERO);
        player.setOnGround(true);
        DungeonsCreature civilian = npc instanceof DungeonsCreature c ? c : null;
        return new ServerState(civilian != null && civilian.rescued(), civilian != null && civilian.waiting(),
                npc.isNoAi(), interaction, civilian != null && civilian.ownerId() != null ? civilian.ownerId().toString() : "none", level.getGameTime());
    }

    private static ServerPlayer player(MinecraftServer server) {
        ServerPlayer player = server.getPlayerList().getPlayer(playerId);
        if (player == null) throw new IllegalStateException("Camp showcase requires the real integrated-server player");
        return player;
    }

    private static void capture(Minecraft client, Camp camp, Mob npc, Shot shot) throws Exception {
        Path image = output.resolve(shot.file());
        try (var pixels = Screenshot.takeScreenshot(client.getMainRenderTarget())) { pixels.writeToFile(image); }
        var record = new LinkedHashMap<String, Object>();
        record.put("file", shot.file());
        record.put("sha256", sha256(Files.readAllBytes(image)));
        record.put("resolution", List.of(client.getWindow().getWidth(), client.getWindow().getHeight()));
        record.put("template", camp.template());
        record.put("template_nbt_sha256", camp.templateSha256());
        record.put("template_origin", List.of(camp.origin().getX(), camp.origin().getY(), camp.origin().getZ()));
        record.put("entity", camp.entityType());
        record.put("entity_uuid", npc.getUUID().toString());
        record.put("entity_id", npc.getId());
        record.put("entity_id_at_template_spawn", camp.npcId());
        record.put("identity_gate", "exact original server UUID and registered entity type; transient numeric IDs may be rebound after chunk reload");
        record.put("entity_position", List.of(npc.getX(), npc.getY(), npc.getZ()));
        record.put("merchant_offer_count", camp.offers());
        var camera = client.gameRenderer.getMainCamera().getPosition();
        record.put("actual_camera", List.of(camera.x, camera.y, camera.z));
        record.put("field_of_view", shot.fov());
        record.put("world_game_tick", client.level.getGameTime());
        record.put("server_state_at_pose", serverState);
        record.put("gate_staged_open", shot.rescue());
        if (npc instanceof DungeonsCreature civilian) {
            record.put("client_synced_rescued", civilian.rescued());
            record.put("client_synced_waiting", civilian.waiting());
            record.put("rescue_gesture_tick", civilian.rescueGestureTicks(0));
        }
        CAPTURES.add(record);
        DungeonsAlive.LOGGER.info("Camp showcase captured native frame {} with real {}", shot.file(), camp.entityType());
    }

    private static void writeReport(Minecraft client, boolean complete) throws Exception {
        var report = new LinkedHashMap<String, Object>();
        report.put("capture_kind", "native_minecraft_world_camps");
        report.put("complete", complete);
        report.put("run_id", runId);
        report.put("world", WORLD_NAME);
        report.put("source_world", "run-gametest-camps/world, copied with its optional datapack");
        report.put("experimental_world_dialog_confirmed", experimentalDialogConfirmed);
        report.put("experimental_confirmation_scope", "Only the known experimental BackupConfirmScreen callback in this marked, single-save development directory; no global warning setting changed");
        report.put("world_generation_evidence", false);
        report.put("staging", List.of("Explicitly marked development copy only", "Manually placed, unchanged pack templates on a level grass photography site",
                "Real template-spawned server NPCs, held with NoAI for framing", "Third frame: ordinary gate staged open, genuine main-hand server rescue interaction",
                "Native framebuffer PNGs; no compositing, generated scene image or shader pack"));
        report.put("captures", CAPTURES);
        if (failure != null) report.put("failure", failure);
        var gson = new GsonBuilder().setPrettyPrinting().create();
        Files.writeString(output.resolve("manifest.json"), gson.toJson(report) + "\n", StandardCharsets.UTF_8);
        Files.writeString(client.gameDirectory.toPath().resolve("latest-capture.json"),
                gson.toJson(Map.of("manifest", "captures/" + runId + "/manifest.json", "complete", complete)) + "\n", StandardCharsets.UTF_8);
    }

    private static String sha256(byte[] bytes) throws Exception {
        return HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256").digest(bytes));
    }

    private static void fail(Minecraft client, Exception error) {
        if (stopped) return;
        stopped = true;
        failure = error.toString();
        DungeonsAlive.LOGGER.error("Camp showcase stopped without fabricating a frame", error);
        if (output != null) try { writeReport(client, false); }
        catch (Exception reportError) { DungeonsAlive.LOGGER.error("Could not write camp failure metadata", reportError); }
        client.stop();
    }
}
