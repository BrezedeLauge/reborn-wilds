package de.laugenbreze.dungeonsalive.client.dev;

import com.google.gson.GsonBuilder;
import de.laugenbreze.dungeonsalive.DungeonsAlive;
import de.laugenbreze.dungeonsalive.entity.AttackKind;
import de.laugenbreze.dungeonsalive.entity.DungeonMob;
import de.laugenbreze.dungeonsalive.entity.ModEntities;
import de.laugenbreze.dungeonsalive.item.ModItems;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Screenshot;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.storage.LevelResource;
import net.minecraft.world.phys.AABB;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.client.event.RenderFrameEvent;
import net.neoforged.neoforge.common.NeoForge;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Explicit development-only, marked-world showcase. Screenshots are native frames
 * of a real server entity in a real built courtyard, viewed through the player.
 * This helper is inactive in production, ordinary runClient and multiplayer.
 */
public final class BossShowcaseCapture {
    private static final String TOKEN = "dungeonsalive-isolated-boss-showcase-v1";
    private static final String RUN_NAME = "run-boss-showcase";
    private static final String WORLD_NAME = "boss-showcase";
    private static final int FLOOR = 80;
    private static final double X = 160.5, Y = FLOOR + 1, Z = 160.5;
    private record Shot(String file, double x, double y, double z,
                        double aimX, double aimY, double aimZ, int fov,
                        boolean hideHud, AttackKind action) {}
    private static final List<Shot> SHOTS = List.of(
            new Shot("01-redstone-monstrosity-hero.png", X + 6.2, Y + 1, Z - 8.8,
                    X, Y + 2.05, Z, 38, true, null),
            new Shot("02-redstone-monstrosity-gameplay.png", X + 3.5, Y, Z - 7.6,
                    X, Y + 1.95, Z, 58, false, null),
            new Shot("03-redstone-monstrosity-detail.png", X + 8.8, Y + 1, Z - 5.3,
                    X, Y + 2.08, Z, 34, true, null),
            new Shot("04-redstone-monstrosity-summon.png", X + 6.2, Y + 1, Z - 8.8,
                    X, Y + 2.05, Z, 38, true, AttackKind.SUMMON),
            new Shot("05-redstone-monstrosity-slam.png", X + 4.2, Y, Z - 9.9,
                    X, Y + 2.9, Z, 54, false, AttackKind.HEAVY_SLAM),
            new Shot("06-night.png", X + 6.2, Y + 1, Z - 8.8,
                    X, Y + 2.05, Z, 38, true, null)
    );
    private static final List<Map<String, Object>> CAPTURES = new ArrayList<>();
    private static boolean verified, setupQueued, setupComplete, poseQueued, poseReady;
    private static boolean actionRequestInFlight, actionArmed, stopped;
    private static boolean nightRequestInFlight, nightSceneReady;
    private static int shotIndex, frames, bossEntityId;
    private static long started, lastStatus;
    private static UUID playerId, bossId, targetId;

    private BossShowcaseCapture() {}

    public static void register() {
        if (FMLEnvironment.production || !Boolean.getBoolean("dungeonsalive.bossShowcase")) return;
        started = System.nanoTime();
        NeoForge.EVENT_BUS.addListener(BossShowcaseCapture::rendered);
    }

    private static void rendered(RenderFrameEvent.Post event) {
        Minecraft client = Minecraft.getInstance();
        if (stopped) return;
        if (System.nanoTime() - started > 300_000_000_000L) {
            fail(client, new IllegalStateException("Boss showcase did not complete within five minutes"));
            return;
        }
        MinecraftServer server = client.getSingleplayerServer();
        if (client.level == null || client.player == null || server == null) return;
        try {
            if (System.nanoTime() - lastStatus > 5_000_000_000L) {
                lastStatus = System.nanoTime();
                Shot expected = SHOTS.get(Math.min(shotIndex, SHOTS.size() - 1));
                Entity tracked = client.level.getEntity(bossEntityId);
                DungeonsAlive.LOGGER.info(
                        "Boss showcase status: shot={} setup={}/{} pose={}/{} frames={} player={} expected={},{},{} distanceSq={} boss={} uuidMatch={} screen={} action={}/{}",
                        shotIndex, setupQueued, setupComplete, poseQueued, poseReady, frames,
                        client.player.position(), expected.x(), expected.y(), expected.z(),
                        client.player.distanceToSqr(expected.x(), expected.y(), expected.z()),
                        tracked == null ? "not tracked" : tracked.getType(),
                        tracked != null && tracked.getUUID().equals(bossId),
                        client.screen == null ? "none" : client.screen.getClass().getSimpleName(),
                        actionRequestInFlight, actionArmed);
            }
            if (!verified) {
                verifySandbox(client.gameDirectory.toPath(), server);
                verified = true;
            }
            // Let the ordinary first-join terrain screen finish before staging or teleporting.
            // This also keeps a freshly copied world from racing its initial player placement.
            if (!setupQueued && client.screen != null) return;
            if (!setupQueued) {
                setupQueued = true;
                playerId = client.player.getUUID();
                server.execute(() -> {
                    try {
                        verifySandbox(client.gameDirectory.toPath(), server);
                        buildScene(server, playerId);
                        client.execute(() -> setupComplete = true);
                    } catch (Exception exception) {
                        client.execute(() -> fail(client, exception));
                    }
                });
                return;
            }
            if (!setupComplete) return;
            if (!(client.level.getEntity(bossEntityId) instanceof DungeonMob renderedBoss)
                    || !renderedBoss.getUUID().equals(bossId)) return;
            if (shotIndex == 5 && !nightSceneReady) {
                if (!nightRequestInFlight) {
                    nightRequestInFlight = true;
                    server.execute(() -> {
                        try {
                            boolean ready = prepareNight(server);
                            client.execute(() -> {
                                nightSceneReady = ready;
                                nightRequestInFlight = false;
                            });
                        } catch (Exception exception) {
                            client.execute(() -> fail(client, exception));
                        }
                    });
                }
                return;
            }
            if (!poseQueued) {
                poseQueued = true;
                Shot shot = SHOTS.get(shotIndex);
                server.execute(() -> {
                    try {
                        placePlayer(server, shot);
                        client.execute(() -> {
                            client.options.fov().set(shot.fov());
                            client.options.hideGui = shot.hideHud();
                            client.options.bobView().set(false);
                            client.options.setCameraType(CameraType.FIRST_PERSON);
                            client.setScreen(null);
                            client.gui.getChat().clearMessages(false);
                            frames = 0;
                            poseReady = true;
                        });
                    } catch (Exception exception) {
                        client.execute(() -> fail(client, exception));
                    }
                });
                return;
            }
            if (!poseReady || client.screen != null) return;
            Shot shot = SHOTS.get(shotIndex);
            if (client.player.distanceToSqr(shot.x(), shot.y(), shot.z()) > 0.5) return;
            if (shotIndex == 5 && (renderedBoss.activeAttack() != null
                    || client.level.getDayTime() % 24_000 != 14_000)) return;
            if (++frames < (shotIndex == 0 ? 100 : 50)) return;
            if (shot.action() != null) {
                if (!actionArmed && !actionRequestInFlight) {
                    actionRequestInFlight = true;
                    server.execute(() -> {
                        try {
                            boolean began = beginRealAction(server, shot.action());
                            client.execute(() -> {
                                actionArmed = began;
                                actionRequestInFlight = false;
                                if (!began) frames = 30;
                            });
                        } catch (Exception exception) {
                            client.execute(() -> fail(client, exception));
                        }
                    });
                }
                if (!actionArmed || renderedBoss.activeAttack() != shot.action()) return;
                float actionTick = renderedBoss.animationTick(0);
                if (actionTick < 23) return;
                if (actionTick >= shot.action().windup()) {
                    throw new IllegalStateException("Missed real windup capture window for " + shot.action());
                }
            }
            capture(client, renderedBoss, shot);
            shotIndex++;
            poseQueued = false;
            poseReady = false;
            actionArmed = false;
            actionRequestInFlight = false;
            frames = 0;
            if (shotIndex == SHOTS.size()) {
                var report = new LinkedHashMap<String, Object>();
                report.put("capture_kind", "native_minecraft_world_frames");
                report.put("world", WORLD_NAME);
                report.put("entity", "dungeonsalive:redstone_monstrosity");
                report.put("entity_uuid", bossId.toString());
                report.put("staging", "marked development copy; real server blocks/entities; NoAI idle; genuine server actions");
                report.put("captures", CAPTURES);
                Files.writeString(client.gameDirectory.toPath().resolve("screenshots/showcase-capture.json"),
                        new GsonBuilder().setPrettyPrinting().create().toJson(report), StandardCharsets.UTF_8);
                stopped = true;
                DungeonsAlive.LOGGER.info("Boss showcase completed: {} native world captures", CAPTURES.size());
                client.stop();
            }
        } catch (Exception exception) {
            fail(client, exception);
        }
    }

    private static void verifySandbox(Path directory, MinecraftServer server) throws Exception {
        Path run = directory.toAbsolutePath().normalize();
        Path expectedWorld = run.resolve("saves").resolve(WORLD_NAME).normalize();
        Path actualWorld = server.getWorldPath(LevelResource.ROOT).toAbsolutePath().normalize();
        if (!RUN_NAME.equals(run.getFileName().toString()) || Files.isSymbolicLink(run)
                || Files.isSymbolicLink(expectedWorld) || !actualWorld.equals(expectedWorld)
                || !TOKEN.equals(Files.readString(run.resolve("dungeonsalive-boss-showcase.txt")).trim())
                || !TOKEN.equals(Files.readString(expectedWorld.resolve("dungeonsalive-showcase-world.txt")).trim())) {
            throw new IllegalStateException("Refusing to edit an unmarked or unexpected world for boss showcase");
        }
    }

    private static void buildScene(MinecraftServer server, UUID uuid) {
        ServerLevel level = server.overworld();
        ServerPlayer player = server.getPlayerList().getPlayer(uuid);
        if (player == null) throw new IllegalStateException("Showcase has no real integrated-server player");
        server.setDifficulty(Difficulty.NORMAL, true);
        level.getGameRules().getRule(GameRules.RULE_DOMOBSPAWNING).set(false, server);
        level.getGameRules().getRule(GameRules.RULE_DAYLIGHT).set(false, server);
        level.getGameRules().getRule(GameRules.RULE_WEATHER_CYCLE).set(false, server);
        level.getGameRules().getRule(GameRules.RULE_MOBGRIEFING).set(false, server);
        level.setDayTime(10_500);
        level.setWeatherParameters(60_000, 0, false, false);

        // Deliberate construction in this marked copied world, never runtime worldgen.
        for (int cx = 8; cx <= 11; cx++) for (int cz = 8; cz <= 11; cz++) level.getChunk(cx, cz);
        for (Entity entity : level.getEntitiesOfClass(Entity.class,
                new AABB(X - 25, FLOOR - 4, Z - 25, X + 25, FLOOR + 20, Z + 25),
                entity -> !(entity instanceof Player))) entity.discard();
        for (int x = -19; x <= 19; x++) for (int z = -18; z <= 18; z++) {
            for (int y = FLOOR + 1; y <= FLOOR + 15; y++) put(level, x, y, z, Blocks.AIR);
            for (int y = FLOOR - 2; y < FLOOR; y++) put(level, x, y, z, Blocks.DEEPSLATE_BRICKS);
            int hash = Math.floorMod(x * 47 + z * 23 + x * z * 7, 17);
            Block floor = hash == 0 ? Blocks.CRACKED_DEEPSLATE_BRICKS
                    : hash < 4 ? Blocks.DEEPSLATE_TILES : Blocks.POLISHED_DEEPSLATE;
            if (Math.max(Math.abs(x), Math.abs(z)) == 7) floor = Blocks.CHISELED_STONE_BRICKS;
            if (Math.abs(x) == 17 || Math.abs(z) == 16) floor = Blocks.POLISHED_BLACKSTONE_BRICKS;
            put(level, x, FLOOR, z, floor);
        }
        // Quiet, dark forge facade behind the boss; the furnace light stays at the sides.
        for (int x = -16; x <= 16; x++) for (int y = 1; y <= 8; y++) {
            boolean opening = Math.abs(x) <= 3 && y <= (Math.abs(x) == 3 ? 5 : 6);
            put(level, x, FLOOR + y, 11, opening ? Blocks.AIR
                    : Math.floorMod(x * 13 + y * 7, 11) == 0 ? Blocks.CRACKED_DEEPSLATE_BRICKS : Blocks.DEEPSLATE_BRICKS);
            put(level, x, FLOOR + y, 12, opening ? Blocks.AIR : Blocks.DEEPSLATE_BRICKS);
            if (opening) put(level, x, FLOOR + y, 14, Blocks.POLISHED_BLACKSTONE_BRICKS);
        }
        // Close the recessed gate chamber on every side, including oblique views.
        for (int x = -4; x <= 4; x++) for (int y = 1; y <= 8; y++)
            put(level, x, FLOOR + y, 14, Blocks.POLISHED_BLACKSTONE_BRICKS);
        for (int z = 12; z <= 14; z++) {
            for (int y = 1; y <= 8; y++) for (int x : new int[]{-4, 4})
                put(level, x, FLOOR + y, z, Blocks.POLISHED_BLACKSTONE_BRICKS);
            for (int x = -4; x <= 4; x++) put(level, x, FLOOR + 8, z, Blocks.POLISHED_BLACKSTONE_BRICKS);
        }
        for (int x = -4; x <= 4; x++) {
            put(level, x, FLOOR + 7, 10, Blocks.CHISELED_POLISHED_BLACKSTONE);
            put(level, x, FLOOR + 8, 10, Blocks.POLISHED_DEEPSLATE);
        }
        for (int x : new int[]{-16, -11, -5, 5, 11, 16}) {
            for (int y = 1; y <= 9; y++) {
                put(level, x, FLOOR + y, 10, y == 9 ? Blocks.CHISELED_STONE_BRICKS : Blocks.POLISHED_BASALT);
                if (x == -5 || x == 5) put(level, x, FLOOR + y, 9, Blocks.POLISHED_BASALT);
            }
        }
        BlockState bars = Blocks.IRON_BARS.defaultBlockState()
                .setValue(BlockStateProperties.EAST, true).setValue(BlockStateProperties.WEST, true);
        for (int x = -2; x <= 2; x++) for (int y = 1; y <= 6; y++) put(level, x, FLOOR + y, 12, bars);
        // Side walls and buttresses make the courtyard read as a place, not a backdrop.
        for (int side : new int[]{-1, 1}) {
            for (int z = -11; z <= 12; z++) for (int y = 1; y <= 3; y++)
                put(level, side * 17, FLOOR + y, z, y == 3 ? Blocks.POLISHED_DEEPSLATE : Blocks.DEEPSLATE_BRICKS);
            for (int z : new int[]{-10, -3, 4, 11}) for (int y = 1; y <= 5; y++)
                put(level, side * 16, FLOOR + y, z, y == 5 ? Blocks.CHISELED_STONE_BRICKS : Blocks.POLISHED_BASALT);
            // Two real recessed lava channels; bordered and away from the camera/player.
            for (int z = 2; z <= 8; z++) {
                put(level, side * 13, FLOOR, z, Blocks.LAVA);
                put(level, side * 12, FLOOR, z, Blocks.POLISHED_BLACKSTONE_BRICKS);
                put(level, side * 14, FLOOR, z, Blocks.POLISHED_BLACKSTONE_BRICKS);
                if ((z & 1) == 0) put(level, side * 13, FLOOR + 1, z, Blocks.IRON_BARS);
            }
            furnace(level, side * 9, 9);
            brazier(level, side * 8, -3);
            bush(level, side * 13, -7);
            bush(level, side * 14, -1);
            put(level, side * 10, FLOOR + 1, -9, Blocks.MOSSY_COBBLESTONE);
            put(level, side * 11, FLOOR, -9, Blocks.MOSS_BLOCK);
            put(level, side * 11, FLOOR + 1, -9, Blocks.FERN);
        }
        for (int x : new int[]{-7, 7}) {
            for (int y = 5; y <= 8; y++) put(level, x, FLOOR + y, 9, Blocks.CHAIN);
            put(level, x, FLOOR + 4, 9, Blocks.LANTERN.defaultBlockState()
                    .setValue(BlockStateProperties.HANGING, true));
        }
        // Actual stone camera ledges give raised shots a grounded first-person viewpoint.
        for (Shot shot : SHOTS) if (shot.y() > Y) {
            int rx = (int)Math.floor(shot.x() - Math.floor(X));
            int rz = (int)Math.floor(shot.z() - Math.floor(Z));
            for (int dx = -1; dx <= 1; dx++) for (int dz = -1; dz <= 1; dz++)
                put(level, rx + dx, FLOOR + 1, rz + dz, Blocks.POLISHED_DEEPSLATE);
        }

        player.setGameMode(GameType.SURVIVAL);
        player.setInvulnerable(true);
        player.setHealth(player.getMaxHealth());
        player.getFoodData().setFoodLevel(20);
        player.getInventory().clearContent();
        player.getInventory().setItem(0, new ItemStack(Items.IRON_SWORD));
        player.getInventory().setItem(1, new ItemStack(ModItems.WIND_HORN.get()));
        player.getInventory().setItem(2, new ItemStack(Items.BREAD, 12));
        player.getInventory().setItem(3, new ItemStack(Items.IRON_PICKAXE));
        player.getInventory().setItem(4, new ItemStack(ModItems.QUEST_JOURNAL.get()));
        player.getInventory().selected = 0;
        player.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.IRON_HELMET));
        player.setItemSlot(EquipmentSlot.CHEST, new ItemStack(Items.IRON_CHESTPLATE));
        player.setItemSlot(EquipmentSlot.LEGS, new ItemStack(Items.IRON_LEGGINGS));
        player.setItemSlot(EquipmentSlot.FEET, new ItemStack(Items.IRON_BOOTS));

        DungeonMob boss = ModEntities.type("redstone_monstrosity").create(level);
        if (boss == null) throw new IllegalStateException("Could not create actual boss entity");
        boss.moveTo(X, Y, Z, 180, 0);
        boss.setNoAi(true);
        boss.setPersistenceRequired();
        boss.setYHeadRot(180);
        boss.yBodyRot = boss.yBodyRotO = boss.yHeadRotO = 180;
        if (!level.addFreshEntity(boss)) throw new IllegalStateException("Server rejected the showcase boss");
        bossId = boss.getUUID();
        bossEntityId = boss.getId();
        placePlayer(server, SHOTS.getFirst());
        DungeonsAlive.LOGGER.info("Boss showcase built in marked world; entity UUID {} at {},{},{}", bossId, X, Y, Z);
    }

    private static void furnace(ServerLevel level, int x, int z) {
        for (int dx = -1; dx <= 1; dx++) for (int y = 1; y <= 4; y++)
            put(level, x + dx, FLOOR + y, z + 1, Blocks.POLISHED_BLACKSTONE_BRICKS);
        for (int y = 5; y <= 10; y++) put(level, x, FLOOR + y, z + 1, Blocks.POLISHED_BASALT);
        put(level, x, FLOOR + 1, z, Blocks.BLAST_FURNACE.defaultBlockState()
                .setValue(AbstractFurnaceBlock.FACING, Direction.NORTH).setValue(AbstractFurnaceBlock.LIT, true));
        put(level, x, FLOOR + 2, z, Blocks.SHROOMLIGHT);
        put(level, x, FLOOR + 3, z, Blocks.CHISELED_POLISHED_BLACKSTONE);
        put(level, x - 1, FLOOR + 1, z - 1, Blocks.ANVIL);
        put(level, x + 1, FLOOR + 1, z - 1, Blocks.SMITHING_TABLE);
    }

    private static void brazier(ServerLevel level, int x, int z) {
        put(level, x, FLOOR + 1, z, Blocks.CHISELED_POLISHED_BLACKSTONE);
        put(level, x, FLOOR + 2, z, Blocks.CAMPFIRE);
        for (int dx : new int[]{-1, 1}) put(level, x + dx, FLOOR + 1, z, Blocks.POLISHED_BLACKSTONE_BRICK_SLAB);
    }

    private static void bush(ServerLevel level, int x, int z) {
        BlockState leaves = Blocks.AZALEA_LEAVES.defaultBlockState().setValue(BlockStateProperties.PERSISTENT, true);
        for (int dx = -1; dx <= 1; dx++) for (int dz = -1; dz <= 1; dz++) {
            put(level, x + dx, FLOOR, z + dz, Blocks.MOSS_BLOCK);
            if (Math.abs(dx) + Math.abs(dz) < 2) put(level, x + dx, FLOOR + 1, z + dz, leaves);
        }
        put(level, x, FLOOR + 2, z, Blocks.FLOWERING_AZALEA_LEAVES.defaultBlockState()
                .setValue(BlockStateProperties.PERSISTENT, true));
    }

    private static void put(ServerLevel level, int x, int y, int z, Block block) {
        put(level, x, y, z, block.defaultBlockState());
    }

    private static void put(ServerLevel level, int x, int y, int z, BlockState state) {
        level.setBlock(new BlockPos((int)Math.floor(X) + x, y, (int)Math.floor(Z) + z), state, Block.UPDATE_CLIENTS);
    }

    private static void placePlayer(MinecraftServer server, Shot shot) {
        ServerPlayer player = server.getPlayerList().getPlayer(playerId);
        if (player == null) throw new IllegalStateException("Showcase player disconnected");
        double dx = shot.aimX() - shot.x(), dz = shot.aimZ() - shot.z();
        double dy = shot.aimY() - (shot.y() + player.getEyeHeight());
        float yaw = (float)Math.toDegrees(Math.atan2(-dx, dz));
        float pitch = (float)-Math.toDegrees(Math.atan2(dy, Math.sqrt(dx * dx + dz * dz)));
        player.teleportTo(server.overworld(), shot.x(), shot.y(), shot.z(), yaw, pitch);
        player.setDeltaMovement(0, 0, 0);
        player.setOnGround(true);
    }

    private static boolean beginRealAction(MinecraftServer server, AttackKind action) {
        ServerLevel level = server.overworld();
        Entity entity = level.getEntity(bossId);
        if (!(entity instanceof DungeonMob boss)) throw new IllegalStateException("Showcase boss is gone");
        if (boss.activeAttack() != null || boss.attackCooldown() > 0) return false;
        IronGolem target = targetId != null && level.getEntity(targetId) instanceof IronGolem golem ? golem : null;
        if (target == null) {
            target = EntityType.IRON_GOLEM.create(level);
            if (target == null) throw new IllegalStateException("Could not create real action target");
            target.setNoAi(true);
            target.setInvulnerable(true);
            target.setPersistenceRequired();
            target.setSilent(true);
            if (!level.addFreshEntity(target)) throw new IllegalStateException("Server rejected action target");
            targetId = target.getUUID();
        }
        target.teleportTo(action == AttackKind.SUMMON ? X - 14 : X, Y,
                action == AttackKind.SUMMON ? Z - 1 : Z + 4);
        target.setDeltaMovement(0, 0, 0);
        boolean result = boss.beginAttack(action, target);
        if (result) DungeonsAlive.LOGGER.info("Boss showcase began actual server action {}", action);
        return result;
    }

    private static boolean prepareNight(MinecraftServer server) {
        ServerLevel level = server.overworld();
        Entity entity = level.getEntity(bossId);
        if (!(entity instanceof DungeonMob boss)) throw new IllegalStateException("Showcase boss is gone");
        if (boss.activeAttack() != null) return false;
        for (DungeonMob summon : level.getEntitiesOfClass(DungeonMob.class,
                boss.getBoundingBox().inflate(24), mob -> mob != boss && mob.isSummoned())) summon.discard();
        if (targetId != null && level.getEntity(targetId) instanceof IronGolem target) target.discard();
        level.setDayTime(14_000);
        // Ordinary block lighting: two low lantern plinths, with no render/light override.
        for (int x : new int[]{-5, 5}) {
            put(level, x, FLOOR + 1, 1, Blocks.POLISHED_BLACKSTONE_BRICKS);
            put(level, x, FLOOR + 2, 1, Blocks.CHISELED_POLISHED_BLACKSTONE);
            put(level, x, FLOOR + 3, 1, Blocks.LANTERN);
        }
        DungeonsAlive.LOGGER.info("Boss showcase prepared real night at 14000 after completed action; summons removed");
        return true;
    }

    private static void capture(Minecraft client, DungeonMob boss, Shot shot) throws Exception {
        Path destination = client.gameDirectory.toPath().resolve("screenshots");
        Files.createDirectories(destination);
        try (var pixels = Screenshot.takeScreenshot(client.getMainRenderTarget())) {
            pixels.writeToFile(destination.resolve(shot.file()));
        }
        var metadata = new LinkedHashMap<String, Object>();
        metadata.put("file", shot.file());
        metadata.put("resolution", List.of(client.getWindow().getWidth(), client.getWindow().getHeight()));
        var camera = client.gameRenderer.getMainCamera().getPosition();
        metadata.put("camera", List.of(camera.x, camera.y, camera.z));
        metadata.put("planned_player_position", List.of(shot.x(), shot.y(), shot.z()));
        metadata.put("field_of_view", shot.fov());
        metadata.put("world_day_time", client.level.getDayTime());
        metadata.put("native_hud", !shot.hideHud());
        metadata.put("entity_id", boss.getId());
        metadata.put("entity_uuid", boss.getUUID().toString());
        metadata.put("server_action", boss.activeAttack() == null ? "idle" : boss.activeAttack().name());
        metadata.put("animation_tick", boss.animationTick(0));
        CAPTURES.add(metadata);
        DungeonsAlive.LOGGER.info("Boss showcase captured actual world frame {}", shot.file());
    }

    private static void fail(Minecraft client, Exception exception) {
        if (stopped) return;
        stopped = true;
        DungeonsAlive.LOGGER.error("Boss showcase stopped without fabricating a frame", exception);
        client.stop();
    }
}
