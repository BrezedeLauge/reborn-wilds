package de.laugenbreze.dungeonsalive.client.dev;

import com.google.gson.GsonBuilder;
import de.laugenbreze.dungeonsalive.DungeonsAlive;
import de.laugenbreze.dungeonsalive.client.entity.reference.ReferenceModels;
import de.laugenbreze.dungeonsalive.entity.AttackKind;
import de.laugenbreze.dungeonsalive.entity.DungeonMob;
import de.laugenbreze.dungeonsalive.entity.MobProfiles;
import de.laugenbreze.dungeonsalive.entity.ModEntities;
import de.laugenbreze.dungeonsalive.entity.peaceful.DungeonsCreature;
import de.laugenbreze.dungeonsalive.entity.peaceful.DungeonsMerchant;
import de.laugenbreze.dungeonsalive.entity.peaceful.ModPeacefulEntities;
import de.laugenbreze.dungeonsalive.entity.peaceful.PeacefulProfiles;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Screenshot;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Difficulty;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.LevelResource;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.client.event.RenderFrameEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * Native world-frame checks of every registered profile. Only an explicitly marked,
 * copied development world may be changed. A real integrated server owns all entities,
 * movement and actions; this class never writes a preview action or limb animation.
 */
public final class MobModelShowcaseCapture {
    private static final String TOKEN = "dungeonsalive-isolated-mob-model-showcase-v1";
    private static final String RUN_NAME = "run-mob-model-showcase";
    private static final String WORLD_NAME = "mob-model-showcase";
    private static final int FLOOR = 80;
    private static final double X = 320.5, Y = FLOOR + 1, Z = 320.5;
    private static final int MOVEMENT_TICKS = 38;
    private static final DateTimeFormatter RUN_TIME = DateTimeFormatter.ofPattern("uuuuMMdd-HHmmss-SSS").withZone(ZoneOffset.UTC);
    private static final List<Map<String, Object>> CAPTURES = new ArrayList<>();
    private static final List<Map<String, Object>> CLIPS = new ArrayList<>();

    private enum View { IDLE, MOVEMENT, ACTION, INTERACTION }
    private record Subject(String id, boolean boss, boolean stationary, boolean aquatic, boolean flying,
                           double speed, double width, boolean peaceful, boolean merchant, boolean civilian, List<AttackKind> attacks) {}
    private record Camera(Vec3 feet, Vec3 aim, int fov, double width, double height, double depth) {}
    private record Scene(Subject profile, View view, AttackKind action, Camera camera,
                         UUID uuid, int entityId, Vec3 start, boolean water, boolean clip, boolean continuedCivilian) {}
    private record Signal(UUID uuid, long gameTick, boolean running, int elapsed,
                          boolean actionBegan, boolean actionComplete, Vec3 position, double moved,
                          String interactionResult, boolean tradingMenuOpened, boolean tradingMenuClosed,
                          boolean inWater, boolean swimming, boolean navigationDone, boolean noAi,
                          String pose, boolean usingItem, String useItem, int useItemTicks,
                          boolean rescued, boolean waiting, String keeper, Camera camera) {}

    /** Only accessed on the integrated server thread. */
    private static final class ServerScene {
        final Scene scene;
        final Mob mob;
        final LivingEntity target;
        long startedAt = -1;
        boolean requested, actionBegan, actionComplete, tradingMenuOpened, tradingMenuClosed;
        String interactionResult = "not_requested";
        ServerScene(Scene scene, Mob mob, LivingEntity target) {
            this.scene = scene;
            this.mob = mob;
            this.target = target;
        }
    }

    private static List<Subject> subjects = List.of();
    private static Set<String> clipIds = Set.of();
    private static MinecraftServer integratedServer;
    private static ServerScene serverScene;
    private static volatile Signal signal;
    private static Scene scene;
    private static UUID playerId;
    private static Path output;
    private static String runId;
    private static String failure;
    private static boolean verified, arenaQueued, arenaReady, sceneQueued, sequenceRequested, mainCaptured;
    private static volatile boolean stopped;
    private static boolean serverWater;
    private static int subjectIndex, viewIndex, settledFrames, sequenceFrame;
    private static long started, lastStatus, sceneStarted, lastClipTick = Long.MIN_VALUE;
    private static final List<Map<String, Object>> sceneClipFrames = new ArrayList<>();

    private MobModelShowcaseCapture() {}

    public static void register() {
        if (FMLEnvironment.production || !Boolean.getBoolean("dungeonsalive.modelShowcase")) return;
        started = System.nanoTime();
        NeoForge.EVENT_BUS.addListener(MobModelShowcaseCapture::rendered);
        NeoForge.EVENT_BUS.addListener(MobModelShowcaseCapture::serverTick);
    }

    private static void rendered(RenderFrameEvent.Post event) {
        Minecraft client = Minecraft.getInstance();
        if (stopped) return;
        MinecraftServer server = client.getSingleplayerServer();
        if (client.level == null || client.player == null || server == null) {
            if (System.nanoTime() - started > 600_000_000_000L)
                fail(client, new IllegalStateException("No integrated showcase world after ten minutes"));
            return;
        }
        try {
            if (!verified) {
                verifySandbox(client.gameDirectory.toPath(), server);
                playerId = client.player.getUUID();
                subjects = selectSubjects();
                clipIds = parseIds(System.getProperty("dungeonsalive.modelShowcaseClips",
                        "royal_guard,wraith,leapleaf,redstone_monstrosity,skeleton_horseman,heart_of_ender,ancient_guardian"));
                runId = RUN_TIME.format(Instant.now());
                output = client.gameDirectory.toPath().resolve("captures").resolve(runId);
                Files.createDirectories(output);
                verified = true;
                writeReport(client, false);
            }
            if (System.nanoTime() - lastStatus > 10_000_000_000L) {
                lastStatus = System.nanoTime();
                DungeonsAlive.LOGGER.info("Mob model showcase status: {}/{} id={} view={} scene={} frames={} player={} signal={}",
                        subjectIndex + 1, subjects.size(), subjects.get(subjectIndex).id(), viewsFor(subjects.get(subjectIndex)).get(viewIndex),
                        scene != null, settledFrames, client.player.position(), signal);
            }
            if (!arenaQueued) {
                arenaQueued = true;
                server.execute(() -> {
                    try {
                        verifySandbox(client.gameDirectory.toPath(), server);
                        integratedServer = server;
                        buildArena(server);
                        client.execute(() -> arenaReady = true);
                    } catch (Exception exception) { client.execute(() -> fail(client, exception)); }
                });
                return;
            }
            if (!arenaReady) return;
            if (scene == null) {
                if (!sceneQueued) queueScene(client, server);
                return;
            }
            if (System.nanoTime() - sceneStarted > 120_000_000_000L)
                throw new IllegalStateException("Timed out while capturing " + scene.profile().id() + "/" + scene.view());
            if (!(client.level.getEntity(scene.entityId()) instanceof Mob mob) || !mob.getUUID().equals(scene.uuid())) return;
            Signal cameraSignal = signal;
            Camera expectedCamera = cameraSignal != null && cameraSignal.uuid().equals(scene.uuid())
                    ? cameraSignal.camera() : scene.camera();
            if (client.screen != null || client.player.distanceToSqr(expectedCamera.feet()) > .75) return;
            if (++settledFrames < (subjectIndex == 0 && viewIndex == 0 ? 60 : 18)) return;

            if (scene.view() == View.IDLE || scene.view() == View.MOVEMENT && scene.profile().stationary()) {
                captureMain(client, mob);
                finishView(client);
                return;
            }
            if (!sequenceRequested) {
                sequenceRequested = true;
                UUID expected = scene.uuid();
                server.execute(() -> {
                    if (serverScene != null && serverScene.scene.uuid().equals(expected)) {
                        serverScene.requested = true;
                        serverScene.startedAt = server.overworld().getGameTime();
                    }
                });
                return;
            }
            Signal snapshot = signal;
            if (snapshot == null || !snapshot.uuid().equals(scene.uuid()) || !snapshot.running()) return;
            if (scene.view() == View.MOVEMENT) {
                if (scene.clip() && snapshot.elapsed() >= 2 && snapshot.elapsed() <= MOVEMENT_TICKS)
                    captureClipFrame(client, mob, snapshot.gameTick());
                boolean confirmedSwimming = !navigatedWater(scene) || snapshot.swimming() && mob.isSwimming();
                if (!mainCaptured && snapshot.elapsed() >= 16 && confirmedSwimming
                        && mob.walkAnimation.speed() > .015f && snapshot.moved() > .25) {
                    captureMain(client, mob);
                    mainCaptured = true;
                }
                if (snapshot.elapsed() >= MOVEMENT_TICKS && !mainCaptured)
                    throw new IllegalStateException("Missing actual movement/swimming evidence for " + scene.profile().id() + ": " + snapshot);
                if (mainCaptured && (!scene.clip() || snapshot.elapsed() >= MOVEMENT_TICKS)) finishView(client);
                return;
            }

            if (scene.view() == View.INTERACTION) {
                if (scene.profile().civilian()) {
                    if (!(mob instanceof DungeonsCreature civilian)) throw new IllegalStateException("Civilian interaction has the wrong entity type");
                    if (scene.clip() && snapshot.actionBegan()) captureClipFrame(client, mob, snapshot.gameTick());
                    float gestureTick = civilian.rescueGestureTicks(0);
                    if (!mainCaptured && snapshot.rescued() && civilian.rescued() && gestureTick >= 6 && gestureTick < 29) {
                        captureMain(client, mob);
                        mainCaptured = true;
                    }
                    if (snapshot.actionComplete() && !mainCaptured)
                        throw new IllegalStateException("Missed the actual civilian rescue: " + scene.profile().id());
                    if (mainCaptured && (!scene.clip() || snapshot.actionComplete())) finishView(client);
                    return;
                }
                if (!(mob instanceof DungeonsMerchant merchant))
                    throw new IllegalStateException("Interaction view does not contain an actual merchant");
                if (scene.clip() && snapshot.actionBegan()) captureClipFrame(client, mob, snapshot.gameTick());
                float greetingTick = merchant.gestureTick(0);
                if (!mainCaptured && snapshot.tradingMenuClosed() && greetingTick >= 6 && greetingTick < 29) {
                    captureMain(client, mob);
                    mainCaptured = true;
                }
                if (snapshot.actionComplete() && !mainCaptured)
                    throw new IllegalStateException("Missed the synchronized merchant greeting for " + scene.profile().id());
                if (mainCaptured && (!scene.clip() || snapshot.actionComplete())) finishView(client);
                return;
            }

            if (scene.clip() && snapshot.actionBegan()) captureClipFrame(client, mob, snapshot.gameTick());
            if (!(mob instanceof DungeonMob combatMob)) throw new IllegalStateException("Peaceful entity in combat view");
            if (combatMob.activeAttack() == scene.action()) {
                float tick = combatMob.animationTick(0);
                int wanted = scene.action() == AttackKind.GUARD ? scene.action().windup() + 6
                        : Math.max(3, (int)(scene.action().windup() * .72));
                if (!mainCaptured && tick >= wanted && tick < scene.action().duration() - 2) {
                    captureMain(client, mob);
                    mainCaptured = true;
                }
            }
            if (snapshot.actionComplete() && !mainCaptured)
                throw new IllegalStateException("Missed the real action window for " + scene.profile().id());
            if (mainCaptured && (!scene.clip() || snapshot.actionComplete() && combatMob.activeAttack() == null)) finishView(client);
        } catch (Exception exception) { fail(client, exception); }
    }

    private static void queueScene(Minecraft client, MinecraftServer server) {
        sceneQueued = true;
        Subject profile = subjects.get(subjectIndex);
        View view = viewsFor(profile).get(viewIndex);
        AttackKind action = characteristicAction(profile);
        Camera camera = cameraFor(profile, view, action);
        boolean water = profile.aquatic() && view == View.MOVEMENT;
        boolean clip = view != View.IDLE && (clipIds.contains("*") || clipIds.contains(profile.id()));
        server.execute(() -> {
            try {
                Scene prepared = createScene(server, profile, view, action, camera, water, clip);
                client.execute(() -> {
                    scene = prepared;
                    settledFrames = sequenceFrame = 0;
                    sequenceRequested = mainCaptured = false;
                    lastClipTick = Long.MIN_VALUE;
                    sceneClipFrames.clear();
                    sceneStarted = System.nanoTime();
                    client.options.fov().set(camera.fov());
                    client.options.hideGui = true;
                    client.options.bobView().set(false);
                    client.options.setCameraType(CameraType.FIRST_PERSON);
                    client.setScreen(null);
                    // clearParticles is private in 1.21.1. The public setLevel
                    // reset clears the same queues while retaining this level.
                    client.particleEngine.setLevel(client.level);
                    client.gui.getChat().clearMessages(false);
                });
            } catch (Exception exception) { client.execute(() -> fail(client, exception)); }
        });
    }

    private static void serverTick(ServerTickEvent.Post event) {
        if (stopped || event.getServer() != integratedServer || serverScene == null) return;
        try {
            ServerScene state = serverScene;
            ServerLevel level = event.getServer().overworld();
            Mob mob = state.mob;
            if (!mob.isAlive()) throw new IllegalStateException("Showcase subject disappeared: " + state.scene.profile().id());
            int elapsed = state.startedAt < 0 ? 0 : (int)(level.getGameTime() - state.startedAt);
            if (state.requested && state.scene.view() == View.MOVEMENT && elapsed < MOVEMENT_TICKS) {
                if (navigatedWater(state.scene)) {
                    mob.setNoAi(false);
                    // Let the entity tick switch to its real WaterBoundPathNavigation first.
                    // A creative camera player is not an enemy and does not hijack this route.
                    if (elapsed >= 2 && elapsed % 4 == 2) {
                        boolean path = mob.getNavigation().moveTo(X, Y + 1.25, Z - 4.5, .7);
                        if (!path && elapsed >= 14) throw new IllegalStateException("No real underwater path for " + state.scene.profile().id());
                    }
                } else if (state.scene.profile().civilian()) {
                    if (!(mob instanceof DungeonsCreature civilian) || !civilian.rescued() || civilian.waiting())
                        throw new IllegalStateException("Civilian following requires an actual prior rescue");
                    mob.setNoAi(false); // Its own FollowKeeperGoal follows the actual camera player.
                } else {
                    double speed = Math.clamp(state.scene.profile().speed() * .32, .045, .105);
                    // Genuine server movement/collision for the remaining staged gait checks.
                    mob.setDeltaMovement(0, mob.getDeltaMovement().y, 0);
                    mob.move(MoverType.SELF, new Vec3(0, 0, -speed));
                    mob.setYRot(180);
                    mob.setYHeadRot(180);
                    mob.yBodyRot = 180;
                }
            } else if (state.requested && state.scene.view() == View.MOVEMENT && elapsed >= MOVEMENT_TICKS) {
                mob.getNavigation().stop();
                mob.setNoAi(true);
                mob.setDeltaMovement(Vec3.ZERO);
            }
            if (state.requested && state.scene.view() == View.ACTION) {
                if (!(mob instanceof DungeonMob combatMob)) throw new IllegalStateException("Peaceful entity received a combat action");
                if (!state.actionBegan && combatMob.attackCooldown() == 0) {
                    state.actionBegan = combatMob.beginAttack(state.scene.action(), state.target);
                    if (state.actionBegan) DungeonsAlive.LOGGER.info("Mob model showcase: {} began real {}", state.scene.profile().id(), state.scene.action());
                }
                if (!state.actionBegan && elapsed > 160)
                    throw new IllegalStateException("No valid action start for " + state.scene.profile().id() + "/" + state.scene.action());
                if (state.actionBegan && combatMob.activeAttack() == null) state.actionComplete = true;
            }
            if (state.requested && state.scene.view() == View.INTERACTION) {
                ServerPlayer player = event.getServer().getPlayerList().getPlayer(playerId);
                if (player == null) throw new IllegalStateException("Showcase player disconnected during interaction");
                if (state.scene.profile().civilian()) {
                    if (!(mob instanceof DungeonsCreature civilian)) throw new IllegalStateException("Expected a real civilian");
                    if (!state.actionBegan) {
                        player.setShiftKeyDown(false);
                        var result = civilian.mobInteract(player, InteractionHand.MAIN_HAND);
                        state.interactionResult = result.toString();
                        state.actionBegan = result.consumesAction() && civilian.rescued()
                                && player.getUUID().equals(civilian.ownerId());
                        if (!state.actionBegan || player.containerMenu != player.inventoryMenu)
                            throw new IllegalStateException("Civilian rescue failed or unexpectedly opened a trading menu");
                    }
                    if (civilian.rescueGestureTicks(0) >= 30) state.actionComplete = true;
                } else if (mob instanceof DungeonsMerchant merchant) {
                    if (!state.actionBegan) {
                        player.setShiftKeyDown(false);
                        var result = merchant.mobInteract(player, InteractionHand.MAIN_HAND);
                        state.interactionResult = result.toString();
                        state.actionBegan = result.consumesAction();
                        state.tradingMenuOpened = player.containerMenu != player.inventoryMenu;
                        if (!state.actionBegan || !state.tradingMenuOpened)
                            throw new IllegalStateException("Real merchant interaction did not open trading: " + state.scene.profile().id());
                    }
                    // Close the actual trade menu after acknowledging it, then
                    // inspect the real synchronized greeting in the world.
                    if (elapsed >= 4 && !state.tradingMenuClosed) {
                        player.closeContainer();
                        merchant.setTradingPlayer(null);
                        state.tradingMenuClosed = true;
                    }
                    if (merchant.gestureTick(0) >= 30) state.actionComplete = true;
                } else throw new IllegalStateException("Interaction subject is neither merchant nor civilian");
            }
            Camera activeCamera = state.scene.camera();
            if (state.requested && state.scene.view() == View.MOVEMENT) {
                Vec3 offset = mob.position().subtract(state.scene.start());
                activeCamera = new Camera(activeCamera.feet().add(offset), activeCamera.aim().add(offset),
                        activeCamera.fov(), activeCamera.width(), activeCamera.height(), activeCamera.depth());
                placePlayer(event.getServer(), activeCamera);
            }
            DungeonsCreature creature = mob instanceof DungeonsCreature value ? value : null;
            signal = new Signal(state.scene.uuid(), level.getGameTime(), state.requested, elapsed,
                    state.actionBegan, state.actionComplete, mob.position(), mob.position().subtract(state.scene.start()).horizontalDistance(),
                    state.interactionResult, state.tradingMenuOpened, state.tradingMenuClosed,
                    mob.isInWater(), mob.isSwimming(), mob.getNavigation().isDone(), mob.isNoAi(), mob.getPose().name(),
                    mob.isUsingItem(), BuiltInRegistries.ITEM.getKey(mob.getUseItem().getItem()).toString(), mob.getTicksUsingItem(),
                    creature != null && creature.rescued(), creature != null && creature.waiting(),
                    creature == null || creature.ownerId() == null ? "none" : creature.ownerId().toString(), activeCamera);
        } catch (Exception exception) { Minecraft.getInstance().execute(() -> fail(Minecraft.getInstance(), exception)); }
    }

    private static Scene createScene(MinecraftServer server, Subject profile, View view, AttackKind action,
                                     Camera camera, boolean water, boolean clip) {
        ServerLevel level = server.overworld();
        ServerPlayer player = server.getPlayerList().getPlayer(playerId);
        if (player == null) throw new IllegalStateException("Showcase player disconnected");
        player.closeContainer();
        player.removeAllEffects();
        player.setGameMode(view == View.ACTION ? GameType.SURVIVAL : GameType.CREATIVE);
        player.getAbilities().flying = view != View.ACTION;
        player.onUpdateAbilities();
        Mob continued = profile.civilian() && view != View.IDLE && serverScene != null
                && serverScene.scene.profile().id().equals(profile.id()) ? serverScene.mob : null;
        serverScene = null;
        signal = null;
        for (Entity entity : level.getEntitiesOfClass(Entity.class,
                new AABB(X - 40, FLOOR - 3, Z - 40, X + 40, FLOOR + 24, Z + 40),
                entity -> !(entity instanceof Player) && entity != continued)) entity.discard();
        if (serverWater != water) {
            configureWaterBay(level, water);
            serverWater = water;
        }
        Mob mob = continued != null ? continued : profile.peaceful()
                ? (profile.merchant() ? ModPeacefulEntities.merchantType(profile.id()).create(level)
                                     : ModPeacefulEntities.creatureType(profile.id()).create(level))
                : ModEntities.type(profile.id()).create(level);
        if (mob == null) throw new IllegalStateException("Could not create " + profile.id());
        double startZ = view == View.MOVEMENT && !profile.stationary() && !profile.civilian() ? Z + 1.25 : Z;
        Vec3 start = continued != null ? mob.position() : new Vec3(X, Y + (profile.flying() || water ? 1.25 : 0), startZ);
        if (continued == null) mob.moveTo(start.x, start.y, start.z, 180, 0);
        mob.setNoAi(true);
        mob.getNavigation().stop();
        mob.setPersistenceRequired();
        mob.setYHeadRot(180);
        mob.yBodyRot = mob.yBodyRotO = mob.yHeadRotO = 180;
        if (continued == null && !level.addFreshEntity(mob)) throw new IllegalStateException("Server rejected " + profile.id());
        placePlayer(server, camera);

        LivingEntity target = player;
        if (view == View.ACTION && camera.feet().distanceTo(start) > action.range() * .93) {
            IronGolem golem = EntityType.IRON_GOLEM.create(level);
            if (golem == null) throw new IllegalStateException("Could not create a real close-range target");
            double separation = Math.min(action.range() * .75, Math.max(2.25, profile.width() * .5 + .95));
            golem.moveTo(X, Y, Z + separation, 180, 0);
            golem.setNoAi(true);
            golem.setInvulnerable(true);
            golem.setPersistenceRequired();
            golem.setSilent(true);
            if (!level.addFreshEntity(golem)) throw new IllegalStateException("Server rejected action target");
            target = golem;
        }
        Scene prepared = new Scene(profile, view, action, camera, mob.getUUID(), mob.getId(), start, water, clip, continued != null);
        serverScene = new ServerScene(prepared, mob, target);
        DungeonsAlive.LOGGER.info("Mob model showcase staged {}/{} as actual entity {}", profile.id(), view, mob.getUUID());
        return prepared;
    }

    private static Camera cameraFor(Subject profile, View view, AttackKind action) {
        var bounds = ReferenceModels.bounds(profile.id());
        double width = Math.max(.2, bounds.width());
        double height = Math.max(.2, bounds.height());
        double depth = Math.max(.2, bounds.depth());
        double centerY = bounds.centerY();
        if (!Double.isFinite(centerY)) centerY = height * .5;
        if (profile.flying()) centerY += 1.25;
        boolean water = profile.aquatic() && view == View.MOVEMENT;
        if (water && !profile.peaceful()) {
            // A real swim pose trades vertical extent for forward/back extent.
            depth = Math.max(depth, height * 1.25);
            width = Math.max(width, height * .65);
        }
        if (view == View.ACTION && action == AttackKind.HEAVY_SLAM) {
            height *= 1.65;
            centerY += bounds.height() * .23;
            width *= 1.2;
        } else if (view == View.ACTION && action == AttackKind.LEAP) {
            height += 2.5;
            centerY += .8;
        }
        double distance = Math.max(3.5, height * 1.5 + Math.max(width, depth) * .7);
        // Keep the actual camera inside the water bay when geometry permits.
        // An eleven-block minimum made small creatures unreadable behind the glass front.
        if (view == View.ACTION && action == AttackKind.LEAP) distance = Math.max(8, distance);
        if (view == View.INTERACTION) distance = Math.min(distance, 2.8);
        if (distance > 31) throw new IllegalArgumentException("Model exceeds the isolated studio size: " + profile.id());
        double startOffset = view == View.MOVEMENT && !profile.stationary() && !profile.civilian() ? 1.25 : 0;
        double waterRise = water ? 1.25 : 0;
        Vec3 feet = new Vec3(X + distance * .48, Y + waterRise, Z + startOffset - distance * .8772684879784524);
        Vec3 eye = feet.add(0, 1.62, 0);
        Vec3 aim = new Vec3(X, Y + waterRise + centerY, Z + startOffset);
        Vec3 forward = aim.subtract(eye).normalize();
        Vec3 right = forward.cross(new Vec3(0, 1, 0)).normalize();
        Vec3 up = right.cross(forward).normalize();
        double tanHalfFov = 0;
        // Fit all eight corners in the actual 16:10 viewport, with a generous border.
        for (double dx : new double[]{-width * .5, width * .5})
            for (double dy : new double[]{-height * .5, height * .5})
                for (double dz : new double[]{-depth * .5, depth * .5}) {
                    Vec3 corner = aim.add(dx, dy, dz).subtract(eye);
                    double forwardDistance = Math.max(.25, corner.dot(forward));
                    tanHalfFov = Math.max(tanHalfFov, Math.max(Math.abs(corner.dot(up)), Math.abs(corner.dot(right)) / 1.6) / forwardDistance);
                }
        int fov = (int)Math.ceil(Math.toDegrees(2 * Math.atan(tanHalfFov / .76)));
        if (view == View.ACTION && action == AttackKind.LEAP) fov = Math.max(50, fov);
        return new Camera(feet, aim, Math.clamp(fov, 30, 75), width, height, depth);
    }

    private static AttackKind characteristicAction(Subject profile) {
        if (profile.peaceful()) return null;
        if (profile.boss()) for (AttackKind preferred : List.of(AttackKind.HEAVY_SLAM, AttackKind.SUMMON, AttackKind.BEAM))
            if (profile.attacks().contains(preferred)) return preferred;
        return profile.attacks().getFirst();
    }

    private static List<View> viewsFor(Subject profile) {
        return profile.civilian() ? List.of(View.IDLE, View.INTERACTION, View.MOVEMENT)
                : profile.merchant() ? List.of(View.IDLE, View.MOVEMENT, View.INTERACTION)
                : profile.peaceful() ? List.of(View.IDLE, View.MOVEMENT)
                : List.of(View.IDLE, View.MOVEMENT, View.ACTION);
    }

    private static List<Subject> selectSubjects() {
        Set<String> selected = parseIds(System.getProperty("dungeonsalive.modelShowcaseIds", ""));
        List<Subject> all = new ArrayList<>();
        MobProfiles.all().forEach(profile -> all.add(new Subject(profile.id(), profile.boss(), profile.stationary(),
                profile.aquatic(), false, profile.speed(), profile.width(), false, false, false, profile.attacks())));
        PeacefulProfiles.all().forEach(profile -> all.add(new Subject(profile.id(), false, false,
                profile.aquatic(), profile.flying(), profile.speed(), profile.width(), true,
                profile.kind() == PeacefulProfiles.Kind.MERCHANT, profile.kind() == PeacefulProfiles.Kind.CIVILIAN, List.of())));
        if (selected.isEmpty() || selected.contains("*")) return all;
        Set<String> unknown = new LinkedHashSet<>(selected);
        all.forEach(profile -> unknown.remove(profile.id()));
        unknown.removeAll(Set.of("peaceful", "hostile", "merchants", "creatures", "civilians"));
        if (!unknown.isEmpty()) throw new IllegalArgumentException("Unknown showcase profile IDs: " + unknown);
        List<Subject> result = all.stream().filter(profile -> selected.contains(profile.id())
                || selected.contains("peaceful") && profile.peaceful()
                || selected.contains("hostile") && !profile.peaceful()
                || selected.contains("merchants") && profile.merchant()
                || selected.contains("civilians") && profile.civilian()
                || selected.contains("creatures") && profile.peaceful() && !profile.merchant() && !profile.civilian()).toList();
        if (result.isEmpty()) throw new IllegalArgumentException("No registered models match showcase selection: " + selected);
        return result;
    }

    private static Set<String> parseIds(String value) {
        return new LinkedHashSet<>(Arrays.stream(value.split(",")).map(String::trim)
                .filter(id -> !id.isEmpty()).map(id -> id.equals("all") ? "*" : id).toList());
    }

    private static boolean navigatedWater(Scene current) {
        return current.view() == View.MOVEMENT && current.water() && !current.profile().peaceful()
                && !current.profile().stationary();
    }

    private static String movementControl(Scene current) {
        if (current.view() != View.MOVEMENT || current.profile().stationary()) return "NoAI staged entity";
        if (navigatedWater(current)) return "actual_server_water_navigation; NoAI=false; no forced entity displacement";
        if (current.profile().civilian()) return "actual_FollowKeeperGoal after real rescue; same entity UUID; no forced entity displacement";
        return "server_MoverType_SELF; client gait derived from networked displacement";
    }

    private static void buildArena(MinecraftServer server) {
        ServerLevel level = server.overworld();
        server.setDifficulty(Difficulty.NORMAL, true);
        level.getGameRules().getRule(GameRules.RULE_DOMOBSPAWNING).set(false, server);
        level.getGameRules().getRule(GameRules.RULE_DAYLIGHT).set(false, server);
        level.getGameRules().getRule(GameRules.RULE_WEATHER_CYCLE).set(false, server);
        level.getGameRules().getRule(GameRules.RULE_MOBGRIEFING).set(false, server);
        level.getGameRules().getRule(GameRules.RULE_ANNOUNCE_ADVANCEMENTS).set(false, server);
        level.setDayTime(6000);
        level.setWeatherParameters(60_000, 0, false, false);
        for (int cx = 17; cx <= 22; cx++) for (int cz = 17; cz <= 22; cz++) level.getChunk(cx, cz);
        for (int x = -34; x <= 34; x++) for (int z = -34; z <= 23; z++) {
            for (int y = FLOOR + 1; y <= FLOOR + 16; y++) put(level, x, y, z, Blocks.AIR);
            put(level, x, FLOOR - 1, z, Blocks.STONE_BRICKS);
            put(level, x, FLOOR, z, (Math.floorMod(x, 6) == 0 || Math.floorMod(z, 6) == 0)
                    ? Blocks.POLISHED_ANDESITE : Blocks.SMOOTH_STONE);
        }
        for (int x = -30; x <= 30; x++) for (int y = 1; y <= 13; y++)
            put(level, x, FLOOR + y, 18, y == 1 || y == 12 ? Blocks.POLISHED_DEEPSLATE : Blocks.LIGHT_GRAY_CONCRETE);
        for (int x : new int[]{-24, -16, -8, 8, 16, 24}) for (int y = 1; y <= 13; y++)
            put(level, x, FLOOR + y, 17, Blocks.GRAY_CONCRETE);
        for (int side : new int[]{-1, 1}) for (int z = -13; z <= 18; z++) for (int y = 1; y <= 8; y++)
            put(level, side * 30, FLOOR + y, z, Blocks.LIGHT_GRAY_CONCRETE);
        for (int x : new int[]{-12, 12}) {
            put(level, x, FLOOR + 1, 9, Blocks.CHISELED_STONE_BRICKS);
            put(level, x, FLOOR + 2, 9, Blocks.LANTERN);
        }
        ServerPlayer player = server.getPlayerList().getPlayer(playerId);
        if (player == null) throw new IllegalStateException("No integrated-server player");
        player.setGameMode(GameType.SURVIVAL);
        player.setInvulnerable(true);
        player.setHealth(player.getMaxHealth());
        player.removeAllEffects();
        player.getFoodData().setFoodLevel(20);
        player.getInventory().clearContent();
        serverWater = false;
        DungeonsAlive.LOGGER.info("Mob model showcase built marked testing studio; no natural world generation claimed");
    }

    private static void configureWaterBay(ServerLevel level, boolean water) {
        for (int x = -8; x <= 8; x++) for (int z = -8; z <= 8; z++) {
            put(level, x, FLOOR, z, water && Math.floorMod(x + z, 5) == 0 ? Blocks.SEA_LANTERN : Blocks.SMOOTH_STONE);
            for (int y = 1; y <= 9; y++) {
                Block block = !water ? Blocks.AIR : Math.abs(x) == 8 || Math.abs(z) == 8 ? Blocks.GLASS : y <= 7 ? Blocks.WATER : Blocks.AIR;
                put(level, x, FLOOR + y, z, block);
            }
        }
    }

    private static void put(ServerLevel level, int x, int y, int z, Block block) {
        level.setBlock(new BlockPos((int)Math.floor(X) + x, y, (int)Math.floor(Z) + z), block.defaultBlockState(), Block.UPDATE_CLIENTS);
    }

    private static void placePlayer(MinecraftServer server, Camera camera) {
        ServerPlayer player = server.getPlayerList().getPlayer(playerId);
        if (player == null) throw new IllegalStateException("Showcase player disconnected");
        Vec3 look = camera.aim().subtract(camera.feet().add(0, player.getEyeHeight(), 0));
        float yaw = (float)Math.toDegrees(Math.atan2(-look.x, look.z));
        float pitch = (float)-Math.toDegrees(Math.atan2(look.y, look.horizontalDistance()));
        player.teleportTo(server.overworld(), camera.feet().x, camera.feet().y, camera.feet().z, yaw, pitch);
        player.setDeltaMovement(Vec3.ZERO);
        player.setOnGround(true);
    }

    private static void verifySandbox(Path directory, MinecraftServer server) throws Exception {
        Path run = directory.toAbsolutePath().normalize();
        Path expectedWorld = run.resolve("saves").resolve(WORLD_NAME).normalize();
        if (!RUN_NAME.equals(run.getFileName().toString()) || Files.isSymbolicLink(run)
                || Files.isSymbolicLink(expectedWorld)
                || !server.getWorldPath(LevelResource.ROOT).toAbsolutePath().normalize().equals(expectedWorld)
                || !TOKEN.equals(Files.readString(run.resolve("dungeonsalive-mob-model-showcase.txt")).trim())
                || !TOKEN.equals(Files.readString(expectedWorld.resolve("dungeonsalive-showcase-world.txt")).trim()))
            throw new IllegalStateException("Refusing to modify an unmarked or unexpected mob-model showcase world");
    }

    private static void captureMain(Minecraft client, Mob mob) throws Exception {
        String label = switch (scene.view()) {
            case IDLE -> "01-idle";
            case MOVEMENT -> scene.profile().civilian() ? "03-following" : scene.profile().stationary() ? "02-stationary" : "02-movement";
            case ACTION -> "03-" + scene.action().name().toLowerCase(java.util.Locale.ROOT);
            case INTERACTION -> scene.profile().civilian() ? "02-interaction-rescue" : "03-interaction-greeting";
        };
        String relative = scene.profile().id() + "/" + label + ".png";
        nativeFrame(client, relative);
        Map<String, Object> metadata = frameMetadata(client, mob, relative);
        metadata.put("kind", "main_world_frame");
        metadata.put("stationary_profile", scene.profile().stationary());
        metadata.put("medium", scene.water() ? "real_water_bay" : "open_air_studio");
        metadata.put("movement_control", movementControl(scene));
        metadata.put("visual_frame_bounds", List.of(scene.camera().width(), scene.camera().height(), scene.camera().depth()));
        CAPTURES.add(metadata);
        writeReport(client, false);
        DungeonsAlive.LOGGER.info("Mob model showcase captured native frame {}", relative);
    }

    private static void captureClipFrame(Minecraft client, Mob mob, long tick) throws Exception {
        if (lastClipTick != Long.MIN_VALUE && tick - lastClipTick < 2) return;
        lastClipTick = tick;
        String relative = scene.profile().id() + "/" + scene.view().name().toLowerCase(java.util.Locale.ROOT)
                + "-frames/" + String.format(java.util.Locale.ROOT, "%04d.png", sequenceFrame++);
        nativeFrame(client, relative);
        sceneClipFrames.add(frameMetadata(client, mob, relative));
    }

    private static Map<String, Object> frameMetadata(Minecraft client, Mob mob, String file) {
        Map<String, Object> metadata = new LinkedHashMap<>();
        Vec3 camera = client.gameRenderer.getMainCamera().getPosition();
        metadata.put("file", file);
        metadata.put("profile", scene.profile().id());
        metadata.put("registry_id", BuiltInRegistries.ENTITY_TYPE.getKey(mob.getType()).toString());
        metadata.put("view", scene.view().name().toLowerCase(java.util.Locale.ROOT));
        metadata.put("entity_uuid", mob.getUUID().toString());
        metadata.put("entity_position", List.of(mob.getX(), mob.getY(), mob.getZ()));
        metadata.put("camera", List.of(camera.x, camera.y, camera.z));
        metadata.put("camera_yaw", client.gameRenderer.getMainCamera().getYRot());
        metadata.put("camera_pitch", client.gameRenderer.getMainCamera().getXRot());
        metadata.put("camera_control", scene.view() == View.MOVEMENT ? "actual_server_player_tracks_entity_displacement" : "fixed_actual_player_view");
        metadata.put("fov", scene.camera().fov());
        metadata.put("resolution", List.of(client.getWindow().getWidth(), client.getWindow().getHeight()));
        metadata.put("world_dimension", client.level.dimension().location().toString());
        metadata.put("game_tick", client.level.getGameTime());
        metadata.put("world_day_time", client.level.getDayTime());
        metadata.put("walk_speed", mob.walkAnimation.speed());
        metadata.put("walk_phase", mob.walkAnimation.position());
        metadata.put("peaceful", scene.profile().peaceful());
        metadata.put("merchant", scene.profile().merchant());
        metadata.put("civilian", scene.profile().civilian());
        metadata.put("continued_civilian_entity", scene.continuedCivilian());
        metadata.put("client_in_water", mob.isInWater());
        metadata.put("client_is_swimming", mob.isSwimming());
        metadata.put("client_pose", mob.getPose().name());
        metadata.put("client_using_item", mob.isUsingItem());
        metadata.put("client_use_item", BuiltInRegistries.ITEM.getKey(mob.getUseItem().getItem()).toString());
        metadata.put("client_use_item_ticks", mob.getTicksUsingItem());
        metadata.put("movement_control", movementControl(scene));
        if (mob instanceof DungeonMob combatMob) {
            metadata.put("server_action", combatMob.activeAttack() == null ? "idle" : combatMob.activeAttack().name());
            metadata.put("action_tick", combatMob.animationTick(0));
        } else if (mob instanceof DungeonsMerchant merchant) {
            float tick = merchant.gestureTick(0);
            metadata.put("server_action", tick >= 0 && tick < 30 ? "GREETING" : "idle");
            metadata.put("gesture_tick", tick);
        } else if (mob instanceof DungeonsCreature creature && creature.civilian()) {
            metadata.put("server_action", creature.rescued() ? "rescued" : "captive");
            metadata.put("client_rescued", creature.rescued());
            metadata.put("client_waiting", creature.waiting());
            metadata.put("rescue_gesture_tick", creature.rescueGestureTicks(0));
        } else metadata.put("server_action", "peaceful_no_combat_action");
        Signal snapshot = signal;
        if (snapshot != null && snapshot.uuid().equals(scene.uuid())) {
            metadata.put("server_state_tick", snapshot.gameTick());
            metadata.put("server_in_water", snapshot.inWater());
            metadata.put("server_is_swimming", snapshot.swimming());
            metadata.put("server_navigation_done", snapshot.navigationDone());
            metadata.put("server_no_ai", snapshot.noAi());
            metadata.put("server_pose", snapshot.pose());
            metadata.put("server_using_item", snapshot.usingItem());
            metadata.put("server_use_item", snapshot.useItem());
            metadata.put("server_use_item_ticks", snapshot.useItemTicks());
            metadata.put("server_position", List.of(snapshot.position().x, snapshot.position().y, snapshot.position().z));
            if (scene.profile().civilian()) {
                metadata.put("server_rescued", snapshot.rescued());
                metadata.put("server_waiting", snapshot.waiting());
                metadata.put("server_keeper_uuid", snapshot.keeper());
            }
            if (scene.view() == View.INTERACTION) {
                metadata.put("interaction_trigger", scene.profile().civilian()
                        ? "actual_server_civilian.mobInteract(MAIN_HAND)" : "actual_server_merchant.mobInteract(MAIN_HAND)");
                metadata.put("interaction_result", snapshot.interactionResult());
                metadata.put("trading_menu_opened", snapshot.tradingMenuOpened());
                if (scene.profile().merchant()) metadata.put("trading_menu_closed_before_world_frame", snapshot.tradingMenuClosed());
            }
        }
        metadata.put("native_hud", !client.options.hideGui);
        return metadata;
    }

    private static void nativeFrame(Minecraft client, String relative) throws Exception {
        Path file = output.resolve(relative);
        Files.createDirectories(file.getParent());
        try (var pixels = Screenshot.takeScreenshot(client.getMainRenderTarget())) { pixels.writeToFile(file); }
    }

    private static void finishView(Minecraft client) throws Exception {
        if (!sceneClipFrames.isEmpty()) {
            Map<String, Object> clip = new LinkedHashMap<>();
            clip.put("profile", scene.profile().id());
            clip.put("view", scene.view().name().toLowerCase(java.util.Locale.ROOT));
            clip.put("source", "unmodified native Minecraft world frames; actual server motion/action");
            clip.put("frames", List.copyOf(sceneClipFrames));
            CLIPS.add(clip);
        }
        viewIndex++;
        if (viewIndex == viewsFor(scene.profile()).size()) {
            subjectIndex++;
            viewIndex = 0;
        }
        if (subjectIndex == subjects.size()) {
            writeReport(client, true);
            stopped = true;
            DungeonsAlive.LOGGER.info("Mob model showcase completed: {} profiles, {} main world frames, {} native frame sequences",
                    subjects.size(), CAPTURES.size(), CLIPS.size());
            client.stop();
            return;
        }
        writeReport(client, false);
        scene = null;
        sceneQueued = false;
    }

    private static void writeReport(Minecraft client, boolean complete) throws Exception {
        if (output == null) return;
        Map<String, Object> report = new LinkedHashMap<>();
        report.put("capture_kind", "native_minecraft_world_model_checks");
        report.put("run", runId);
        report.put("complete", complete);
        report.put("world", WORLD_NAME);
        report.put("staging", "explicit marked developer studio; real server entities, with one preserved civilian across captivity/rescue/following; not natural worldgen or an autonomous combat playthrough");
        report.put("profiles", subjects.stream().map(Subject::id).toList());
        report.put("planned_main_frames", subjects.stream().mapToInt(profile -> viewsFor(profile).size()).sum());
        report.put("peaceful_views", "creatures: idle plus real movement; merchants: real trading interaction/greeting; civilians: same entity captive, actual rescue interaction without trade UI, actual following");
        report.put("captures", CAPTURES);
        report.put("clips", CLIPS);
        if (failure != null) report.put("failure", failure);
        var gson = new GsonBuilder().setPrettyPrinting().create();
        Files.writeString(output.resolve("manifest.json"), gson.toJson(report), StandardCharsets.UTF_8);
        Files.writeString(client.gameDirectory.toPath().resolve("latest-capture.json"), gson.toJson(Map.of(
                "run", runId, "manifest", "captures/" + runId + "/manifest.json", "complete", complete)), StandardCharsets.UTF_8);
    }

    private static void fail(Minecraft client, Exception exception) {
        if (stopped) return;
        stopped = true;
        failure = exception.toString();
        DungeonsAlive.LOGGER.error("Mob model showcase stopped; incomplete frames remain explicitly incomplete", exception);
        try { writeReport(client, false); }
        catch (Exception reportException) { DungeonsAlive.LOGGER.error("Could not save partial showcase report", reportException); }
        client.stop();
    }
}
