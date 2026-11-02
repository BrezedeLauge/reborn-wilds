package de.laugenbreze.dungeonsalive.client.dev;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import de.laugenbreze.dungeonsalive.DungeonsAlive;
import de.laugenbreze.dungeonsalive.client.InnScreen;
import de.laugenbreze.dungeonsalive.entity.peaceful.DungeonsMerchant;
import de.laugenbreze.dungeonsalive.settlement.inn.InnAnchorBlockEntity;
import de.laugenbreze.dungeonsalive.settlement.inn.InnContent;
import de.laugenbreze.dungeonsalive.settlement.inn.InnMenu;
import de.laugenbreze.dungeonsalive.settlement.inn.InnRegistry;
import de.laugenbreze.dungeonsalive.settlement.inn.InnSavedData;
import de.laugenbreze.dungeonsalive.settlement.inn.InnSeatEntity;
import de.laugenbreze.dungeonsalive.settlement.inn.InnSelection;
import de.laugenbreze.dungeonsalive.settlement.inn.InnService;
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
import net.minecraft.client.Minecraft;
import net.minecraft.client.Screenshot;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.storage.LevelResource;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.event.RenderFrameEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.CommandEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

/** One disposable, explicitly staged inn. Only the normal InnService creates and drives its guest. */
public final class InnShowcaseCapture {
    private static final Path SUFFIX = Path.of("run-prism", "instances", "Dungeons-Alive-Inn-QA", "minecraft");
    private static final String WORLD = "Dungeons Alive Inn Showcase", TOKEN = "isolated-dungeonsalive-inn-prism-qa-v1";
    private static final int MAX_TICKS = 3600, MAX_IMAGES = 180;
    private static final NativeCaptureTerrainReadiness TERRAIN = new NativeCaptureTerrainReadiness();
    private static final List<Map<String, Object>> FRAMES = new ArrayList<>(), SAMPLES = new ArrayList<>(), PREPARATION = new ArrayList<>();
    private static final Map<String, Integer> CLIENT_STATE_FRAMES = new LinkedHashMap<>();
    private static final List<Map<String, Object>> TRANSITIONS = new ArrayList<>(), CAMERAS = new ArrayList<>(), MARKS = new ArrayList<>(), ATTACHMENTS = new ArrayList<>(); // Server only.
    private static volatile Snapshot latest;
    private static volatile boolean stopped;
    private static MinecraftServer server;
    private static UUID ownerId, innId, guestId;
    private static Path output;
    private static String artifactSha, mode = "selection", phase = "NONE";
    private static Camera camera;
    private static BlockPos anchor, stock, privateStock, door, arrival, entrance;
    private static int floor, commands, seatedTicks, sleepingTicks;
    private static long startTick = -1, segmentStart, startNanos, lastSample = -1, lastImage = -100, cameraWaitStarted;
    private static Vec3 previousGuest;
    private static double distance, maximumStep, maximumWalkingStep, lastExitDistance = Double.POSITIVE_INFINITY;
    private static boolean previouslySeated, previouslySleeping;
    private static boolean sawBridge, sawDoor, sawSeat, sawBed, sawMeal, sawLeaving, departed;
    private static boolean started, selectionCaptured, menuRequested, commitSent;
    private static int menuFrames;
    private static String initializedCamera, imageReadyCamera, waitingCamera;
    private static long preparationStarted;

    private InnShowcaseCapture() { }
    public static void register() {
        if (!Boolean.getBoolean("dungeonsalive.innShowcase") || !Boolean.getBoolean("dungeonsalive.isolatedInnQa")) return;
        NeoForge.EVENT_BUS.addListener(InnShowcaseCapture::frame);
        NeoForge.EVENT_BUS.addListener(InnShowcaseCapture::tick);
        NeoForge.EVENT_BUS.addListener(InnShowcaseCapture::command);
    }

    private static void frame(RenderFrameEvent.Post event) {
        var client = Minecraft.getInstance();
        if (stopped || client.level == null || client.player == null || client.getSingleplayerServer() == null) return;
        try {
            if (!started) {
                final Path run;
                try { run = authorize(client); }
                catch (Exception rejected) {
                    stopped = true; DungeonsAlive.LOGGER.error("Inn showcase refused an unrelated run; ordinary client remains untouched", rejected); return;
                }
                started = true; startNanos = System.nanoTime(); server = client.getSingleplayerServer(); ownerId = client.player.getUUID();
                output = run.resolve("inn-captures"); Files.createDirectory(output);
                client.options.fov().set(65); client.options.hideGui = true; client.setScreen(null);
                server.execute(() -> { try { stage(); } catch (Exception failure) { client.execute(() -> fail(client, failure)); } });
                return;
            }
            if (System.nanoTime() - startNanos > 480_000_000_000L) throw new IllegalStateException("Inn capture exceeded eight minutes");
            Snapshot snapshot = latest; if (snapshot == null) return;
            // Preserve the actual server-observed failure. A new camera's mesh
            // wait must not replace it with an unrelated readiness exception.
            if (snapshot.finished() && snapshot.error() != null) {
                stopped = true; writeReport("incomplete", snapshot, snapshot.error()); client.stop(); return;
            }
            Camera view = snapshot.camera();
            if (!view.id().equals(initializedCamera)) {
                if (!view.id().equals(waitingCamera)) { waitingCamera = view.id(); cameraWaitStarted = System.nanoTime(); }
                if (!cameraAcknowledged(client, view)) {
                    if (System.nanoTime() - cameraWaitStarted > 15_000_000_000L) throw new IllegalStateException("Camera packet was not acknowledged: " + view.id());
                    return;
                }
                client.player.setOnGround(false); client.player.setDeltaMovement(Vec3.ZERO);
                client.player.getAbilities().flying = true; client.player.onUpdateAbilities();
                initializedCamera = view.id(); TERRAIN.reset(); preparationStarted = System.nanoTime();
            }
            if (!cameraAcknowledged(client, view)) throw new IllegalStateException("Fixed camera drifted: " + view.id());
            if (!view.id().equals(imageReadyCamera)) {
                if (!TERRAIN.sample(client)) {
                    if (System.nanoTime() - preparationStarted > 120_000_000_000L)
                        throw new IllegalStateException("Inn camera terrain was not ready: " + TERRAIN.snapshot());
                    return; // Server AI and observation keep running; only framebuffer acceptance waits.
                }
                imageReadyCamera = view.id();
                PREPARATION.add(Map.of("camera", view.id(), "tick", snapshot.tick(), "terrain", TERRAIN.snapshot()));
            }
            if (snapshot.mode().equals("selection")) {
                if (!selectionCaptured) {
                    capture(client, snapshot, "owner_room_selection"); selectionCaptured = true;
                }
                if (!menuRequested) {
                    menuRequested = true;
                    server.execute(() -> {
                        try {
                            if (!InnContent.open(owner(), anchor)) throw new IllegalStateException("Actual owner menu refused the marked inn");
                            mode = "menu"; publish(false, false, null);
                        } catch (Exception failure) { client.execute(() -> fail(client, failure)); }
                    });
                }
                return;
            }
            if (snapshot.mode().equals("menu")) {
                if (!(client.screen instanceof InnScreen) || !(client.player.containerMenu instanceof InnMenu menu) || !menu.canCommit()) {
                    if (System.nanoTime() - preparationStarted > 30_000_000_000L) throw new IllegalStateException("Actual InnScreen/commit-ready menu never arrived");
                    return;
                }
                if (!commitSent && ++menuFrames >= 5) {
                    capture(client, snapshot, "actual_owner_inn_menu");
                    client.gameMode.handleInventoryButtonClick(menu.containerId, 0); // Normal client intent, checked by actual server menu.
                    commitSent = true;
                }
                return;
            }
            if (client.screen != null) throw new IllegalStateException("Unexpected screen during the acknowledged runtime camera");
            if (snapshot.tick() != lastSample) {
                lastSample = snapshot.tick(); if (snapshot.tick() % 5 == 0 || snapshot.finished()) SAMPLES.add(snapshot.data());
            }
            if ((snapshot.tick() - lastImage >= 20 || snapshot.finished()) && FRAMES.size() < MAX_IMAGES) {
                Snapshot current = latest;
                if (current != null && !current.camera().equals(view)) return;
                capture(client, snapshot, "unmodified_runtime_frame"); lastImage = snapshot.tick();
            }
            if (snapshot.finished()) {
                boolean views = CLIENT_STATE_FRAMES.getOrDefault("SEATED", 0) >= 2 && CLIENT_STATE_FRAMES.getOrDefault("SLEEPING", 0) >= 2;
                stopped = true;
                writeReport(snapshot.complete() && views ? "captured" : "incomplete", snapshot,
                        snapshot.error() != null ? snapshot.error() : views ? null : "Missing repeated real client seated/sleeping frames");
                client.stop();
            }
        } catch (Exception failure) { fail(client, failure); }
    }

    private static boolean cameraAcknowledged(Minecraft client, Camera view) {
        return client.player.distanceToSqr(view.position()) <= .01
                && Math.abs(Mth.wrapDegrees(client.player.getYRot() - view.yaw())) < .3
                && Math.abs(client.player.getXRot() - view.pitch()) < .3;
    }
    private static void command(CommandEvent event) {
        if (startTick >= 0 && event.getParseResults().getContext().getSource().getServer() == server) commands++;
    }
    private static void tick(ServerTickEvent.Post event) {
        if (!started || stopped || event.getServer() != server || camera == null) return;
        try {
            var level = server.overworld();
            if (mode.equals("menu") && level.getBlockEntity(anchor) instanceof InnAnchorBlockEntity ledger && ledger.innId() != null) {
                innId = ledger.innId(); var inn = InnSavedData.get(server).find(innId);
                if (inn == null || !inn.owner().equals(ownerId) || inn.revision() != 1) throw new IllegalStateException("Committed menu did not bind the actual owner and first revision");
                owner().closeContainer(); startTick = level.getGameTime(); mode = "observing";
                cut("route_in", new Vec3(-.5, floor + 4.0, 14.5), new Vec3(-3.5, floor + 1.2, 5.5));
            }
            if (!mode.equals("observing")) return;
            long elapsed = level.getGameTime() - startTick;
            var inn = InnSavedData.get(server).find(innId);
            if (inn == null) throw new IllegalStateException("Configured inn disappeared during observation");
            if (inn.guest() != null) {
                if (guestId == null) guestId = inn.guest().entity();
                else if (!guestId.equals(inn.guest().entity())) throw new IllegalStateException("Unexpected replacement visitor");
                if (!(level.getEntity(guestId) instanceof DungeonsMerchant guest) || !guest.isAlive() || guest.isNoAi())
                    throw new IllegalStateException("Actual admitted visitor is absent or lacks active AI");
                Vec3 here = guest.position();
                boolean seated = guest.getVehicle() instanceof InnSeatEntity, sleeping = guest.isSleeping();
                if (previousGuest != null) {
                    double step = here.distanceTo(previousGuest); distance += step; maximumStep = Math.max(maximumStep, step);
                    // Real vanilla bed/seat attachments reposition a body only
                    // after arrival. Record that local operation separately;
                    // it is not the navigation teleport this check forbids.
                    boolean sleepChange = sleeping != previouslySleeping;
                    boolean seatChange = seated != previouslySeated;
                    Vec3 attachment = Vec3.atBottomCenterOf(sleepChange ? p(7, 1, 3) : p(4, 1, 3));
                    double radius = sleepChange && !sleeping ? 4 : 2;
                    boolean localAttachment = (sleepChange || seatChange)
                            && previousGuest.distanceToSqr(attachment) <= radius * radius
                            && here.distanceToSqr(attachment) <= radius * radius;
                    if (localAttachment) ATTACHMENTS.add(Map.of("tick", elapsed,
                            "kind", sleepChange ? sleeping ? "vanilla_bed_enter" : "vanilla_bed_exit" : seated ? "seat_mount" : "seat_dismount",
                            "from", point(previousGuest), "to", point(here), "distance", step,
                            "furniture", point(attachment)));
                    else maximumWalkingStep = Math.max(maximumWalkingStep, step);
                }
                previouslySeated = seated; previouslySleeping = sleeping;
                previousGuest = here; lastExitDistance = here.distanceToSqr(Vec3.atBottomCenterOf(arrival));
                sawBridge |= guest.onGround() && here.x >= -6 && here.x < -3 && here.z >= 4 && here.z < 7
                        && level.getBlockState(guest.blockPosition().below()).is(Blocks.OAK_PLANKS);
                String state = inn.guest().phase().name();
                if (!state.equals(phase)) { phase = state; TRANSITIONS.add(Map.of("tick", elapsed, "phase", state, "position", point(here))); }
                sawDoor |= level.getBlockState(door).getValue(DoorBlock.OPEN);
                if (seated) { sawSeat = true; seatedTicks++; }
                if (guest.isSleeping()) { sawBed = true; sleepingTicks++; }
                sawMeal |= food(stock) == 3 && inn.guest().phase() == InnRegistry.GuestPhase.RESTING;
                sawLeaving |= inn.guest().phase() == InnRegistry.GuestPhase.LEAVING;
                if (seated && camera.id().equals("route_in"))
                    cut("seat", new Vec3(4.5, floor + 1.1, 7.5), new Vec3(4.5, floor + 1.5, 3.5));
                if (guest.isSleeping() && !camera.id().equals("bed"))
                    cut("bed", new Vec3(5.0, floor + 1.1, 6.5), new Vec3(7.5, floor + 1.3, 3.5));
                if (sawLeaving && !guest.isSleeping() && !seated && !camera.id().equals("route_out"))
                    cut("route_out", new Vec3(-.5, floor + 4.0, 14.5), new Vec3(-3.5, floor + 1.2, 5.5));
            } else if (guestId != null) {
                departed = sawLeaving && lastExitDistance <= 2.25 && level.getEntity(guestId) == null && inn.nextVisitAfter() > level.getGameTime();
                if (!departed) throw new IllegalStateException("Visitor vanished without its genuine completed exit");
            }
            boolean complete = departed && sawBridge && sawDoor && sawSeat && seatedTicks >= 40 && sawBed && sleepingTicks >= 40 && sawMeal
                    && food(stock) == 3 && food(privateStock) == 8 && distance > 8 && !level.getBlockState(door).getValue(DoorBlock.OPEN);
            String error = commands != 0 ? "Command issued during live observation" : maximumWalkingStep > 1.5 ? "Discontinuous guest walking outside a local furniture attachment" : null;
            if (food(stock) < 3 || food(privateStock) != 8) error = "Visitor consumed extra or unassigned stock";
            if (elapsed >= MAX_TICKS && !complete) error = "Actual inn lifecycle incomplete within bounded observation";
            publish(error != null || complete || elapsed >= MAX_TICKS, complete && error == null, error);
        } catch (Exception failure) { Minecraft.getInstance().execute(() -> fail(Minecraft.getInstance(), failure)); }
    }

    private static void stage() {
        ServerLevel level = server.overworld(); ServerPlayer owner = owner();
        // Explicit finite scene construction only in the freshly claimed development world.
        for (int x = -6; x <= 6; x++) for (int z = -6; z <= 6; z++) { level.getChunk(x, z); level.setChunkForced(x, z, true); }
        floor = Math.min(level.getMaxBuildHeight() - 14, level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, 0, 0) + 4);
        for (int x = -16; x <= 13; x++) for (int z = -4; z <= 18; z++) {
            for (int y = 1; y <= 10; y++) level.setBlock(p(x, y, z), Blocks.AIR.defaultBlockState(), 2);
            level.setBlock(p(x, -1, z), Blocks.STONE.defaultBlockState(), 2);
            level.setBlock(p(x, 0, z), Blocks.GRASS_BLOCK.defaultBlockState(), 2);
        }
        for (int x = -6; x <= -4; x++) for (int z = -4; z <= 18; z++) level.setBlock(p(x, 0, z), Blocks.WATER.defaultBlockState(), 3);
        for (int x = -11; x <= 9; x++) for (int z = 4; z <= 6; z++) level.setBlock(p(x, 0, z), Blocks.OAK_PLANKS.defaultBlockState(), 3);
        for (int x = 0; x <= 9; x++) for (int z = 0; z <= 10; z++) {
            level.setBlock(p(x, 0, z), Blocks.OAK_PLANKS.defaultBlockState(), 2);
            level.setBlock(p(x, 4, z), Blocks.OAK_SLAB.defaultBlockState(), 2);
            if (x == 0 || x == 9 || z == 0 || z == 10) for (int y = 1; y <= 3; y++)
                level.setBlock(p(x, y, z), y == 2 && ((x == 9 && z >= 3 && z <= 7) || (z == 0 && x >= 3 && x <= 7))
                        ? Blocks.GLASS_PANE.defaultBlockState() : Blocks.STONE_BRICKS.defaultBlockState(), 3);
        }
        door = p(0, 1, 5);
        var doorState = Blocks.OAK_DOOR.defaultBlockState().setValue(DoorBlock.FACING, Direction.EAST).setValue(DoorBlock.OPEN, false);
        level.setBlock(door, doorState.setValue(DoorBlock.HALF, DoubleBlockHalf.LOWER), 3);
        level.setBlock(door.above(), doorState.setValue(DoorBlock.HALF, DoubleBlockHalf.UPPER), 3);
        level.setBlock(p(4, 1, 3), Blocks.OAK_SLAB.defaultBlockState(), 3);
        var bed = Blocks.RED_BED.defaultBlockState().setValue(BedBlock.FACING, Direction.NORTH);
        level.setBlock(p(7, 1, 4), bed.setValue(BedBlock.PART, BedPart.FOOT), 3);
        level.setBlock(p(7, 1, 3), bed.setValue(BedBlock.PART, BedPart.HEAD), 3);
        for (BlockPos lantern : List.of(p(2, 1, 1), p(7, 1, 1), p(2, 1, 9), p(8, 1, 9), p(-2, 1, 3), p(-8, 1, 3)))
            level.setBlock(lantern, Blocks.LANTERN.defaultBlockState(), 3);
        anchor = p(6, 1, 7); stock = p(7, 1, 7); privateStock = p(8, 1, 7); entrance = p(1, 1, 5); arrival = p(-10, 1, 5);
        level.setBlock(stock, Blocks.BARREL.defaultBlockState(), 3); level.setBlock(privateStock, Blocks.BARREL.defaultBlockState(), 3);
        ((Container)level.getBlockEntity(stock)).setItem(0, new ItemStack(Items.BREAD, 4));
        ((Container)level.getBlockEntity(privateStock)).setItem(0, new ItemStack(Items.BREAD, 8));
        level.getGameRules().getRule(GameRules.RULE_DOMOBSPAWNING).set(false, server);
        level.getGameRules().getRule(GameRules.RULE_DAYLIGHT).set(false, server);
        level.getGameRules().getRule(GameRules.RULE_WEATHER_CYCLE).set(false, server);
        level.setDayTime(13_000); level.setWeatherParameters(100000, 0, false, false);
        owner.setGameMode(GameType.CREATIVE);
        setupPosition(owner, anchor.offset(-2, 0, 1));
        owner.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(InnContent.ANCHOR_ITEM.get()));
        var anchorHit = new BlockHitResult(Vec3.atCenterOf(anchor.below()).add(0, .5, 0), Direction.UP, anchor.below(), false);
        if (!InnContent.ANCHOR_ITEM.get().useOn(new UseOnContext(owner, InteractionHand.MAIN_HAND, anchorHit)).consumesAction()
                || !(level.getBlockEntity(anchor) instanceof InnAnchorBlockEntity placed) || !ownerId.equals(placed.ownerId()))
            throw new IllegalStateException("Actual owner BlockItem placement failed");
        owner.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(InnContent.MARKER.get()));
        for (BlockPos clicked : List.of(anchor, p(0, 1, 0), p(9, 3, 10), entrance.below(), stock, arrival.below())) {
            setupPosition(owner, clicked.above(4));
            var hit = new BlockHitResult(Vec3.atCenterOf(clicked).add(0, .5, 0), Direction.UP, clicked, false);
            InnContent.MARKER.get().onItemUseFirst(owner.getMainHandItem(), new UseOnContext(owner, InteractionHand.MAIN_HAND, hit));
            var plan = InnSelection.current(owner);
            MARKS.add(Map.of("clicked", List.of(clicked.getX(), clicked.getY(), clicked.getZ()), "actual_step", plan == null ? -1 : plan.step()));
        }
        var selection = InnSelection.current(owner);
        if (selection == null || !selection.complete()) throw new IllegalStateException("Actual owner marker sequence was not complete");
        // The owner remains inside the server menu's real eight-block interaction limit.
        cut("owner_selection", new Vec3(3.5, floor + 1.1, 8.5), new Vec3(6.5, floor + 1.5, 7.5));
        publish(false, false, null);
    }

    private static void setupPosition(ServerPlayer player, BlockPos pos) {
        player.teleportTo(server.overworld(), pos.getX() + .5, pos.getY(), pos.getZ() + .5, 0, 0);
        player.getAbilities().flying = true; player.onUpdateAbilities(); player.setDeltaMovement(Vec3.ZERO);
    }
    private static BlockPos p(int x, int y, int z) { return new BlockPos(x, floor + y, z); }
    private static ServerPlayer owner() {
        var player = server.getPlayerList().getPlayer(ownerId);
        if (player == null || player.serverLevel() != server.overworld()) throw new IllegalStateException("Actual integrated owner left the inn fixture");
        return player;
    }
    private static void cut(String id, Vec3 position, Vec3 target) {
        var player = owner(); var level = server.overworld();
        if (!level.noBlockCollision(player, player.getBoundingBox().move(position.subtract(player.position()))))
            throw new IllegalStateException("Planned camera intersects the built inn: " + id);
        Vec3 delta = target.subtract(position.add(0, player.getEyeHeight(), 0));
        float yaw = (float)Math.toDegrees(Math.atan2(-delta.x, delta.z));
        float pitch = (float)-Math.toDegrees(Math.atan2(delta.y, Math.hypot(delta.x, delta.z)));
        segmentStart = startTick < 0 ? 0 : level.getGameTime() - startTick;
        camera = new Camera(id, position, yaw, pitch);
        CAMERAS.add(Map.of("id", id, "tick", segmentStart, "position", point(position), "yaw", yaw, "pitch", pitch));
        player.teleportTo(level, position.x, position.y, position.z, yaw, pitch);
        player.getAbilities().flying = true; player.onUpdateAbilities(); player.setOnGround(false); player.setDeltaMovement(Vec3.ZERO);
    }
    private static int food(BlockPos pos) {
        if (!(server.overworld().getBlockEntity(pos) instanceof Container inventory)) return -1;
        int count = 0; for (int slot = 0; slot < inventory.getContainerSize(); slot++) if (inventory.getItem(slot).is(Items.BREAD)) count += inventory.getItem(slot).getCount();
        return count;
    }
    private static void publish(boolean finished, boolean complete, String error) {
        var level = server.overworld(); long tick = startTick < 0 ? 0 : level.getGameTime() - startTick;
        var data = new LinkedHashMap<String, Object>();
        data.put("world_tick", level.getGameTime()); data.put("elapsed_tick", tick); data.put("mode", mode); data.put("camera", camera.id());
        data.put("owner_position", point(owner().position())); data.put("selected_stock_bread", food(stock)); data.put("private_stock_bread", food(privateStock));
        data.put("stock_inventory", inventory(stock)); data.put("private_inventory", inventory(privateStock));
        data.put("door_open", level.getBlockState(door).getValue(DoorBlock.OPEN));
        data.put("marker_actions", List.copyOf(MARKS)); data.put("camera_cuts", List.copyOf(CAMERAS)); data.put("phase_transitions", List.copyOf(TRANSITIONS));
        data.put("seated_ticks", seatedTicks); data.put("sleeping_ticks", sleepingTicks); data.put("guest_distance", distance); data.put("maximum_guest_step", maximumStep);
        data.put("maximum_walking_step", maximumWalkingStep); data.put("local_furniture_attachments", List.copyOf(ATTACHMENTS));
        data.put("commands_during_observation", commands); data.put("actual_departure", departed); data.put("actual_bridge_contact", sawBridge);
        if (innId != null) {
            var inn = InnSavedData.get(server).find(innId); data.put("inn_id", innId.toString());
            if (inn != null) data.put("registry", Map.of("revision", inn.revision(), "phase", inn.guest() == null ? "NONE" : inn.guest().phase().name(),
                    "owner", inn.owner().toString(), "arrivals", inn.arrivals(), "retired", inn.retired(), "next_visit_after", inn.nextVisitAfter()));
        }
        if (guestId != null) {
            data.put("guest_uuid", guestId.toString());
            if (level.getEntity(guestId) instanceof DungeonsMerchant guest) data.put("guest", Map.of("position", point(guest.position()),
                    "entity_type", BuiltInRegistries.ENTITY_TYPE.getKey(guest.getType()).toString(), "on_ground", guest.onGround(), "no_ai", guest.isNoAi(),
                    "seated", guest.getVehicle() instanceof InnSeatEntity, "sleeping", guest.isSleeping(), "trading", guest.isTrading()));
        }
        latest = new Snapshot(tick, mode, camera, finished, complete, error, Map.copyOf(data));
    }
    private static List<Map<String, Object>> inventory(BlockPos pos) {
        if (!(server.overworld().getBlockEntity(pos) instanceof Container inventory)) return List.of();
        var contents = new ArrayList<Map<String, Object>>();
        for (int slot = 0; slot < inventory.getContainerSize(); slot++) {
            var item = inventory.getItem(slot); if (!item.isEmpty()) contents.add(Map.of("slot", slot,
                    "item", BuiltInRegistries.ITEM.getKey(item.getItem()).toString(), "count", item.getCount()));
        }
        return List.copyOf(contents);
    }

    private static void capture(Minecraft client, Snapshot snapshot, String kind) throws Exception {
        Path file = output.resolve(String.format("%03d-%s-tick-%04d.png", FRAMES.size(), snapshot.camera().id(), snapshot.tick()));
        try (var pixels = Screenshot.takeScreenshot(client.getMainRenderTarget())) { pixels.writeToFile(file); }
        var frame = new LinkedHashMap<String, Object>();
        frame.put("file", file.getFileName().toString()); frame.put("sha256", hash(file)); frame.put("kind", kind); frame.put("server_snapshot", snapshot.data());
        frame.put("client_camera", point(client.player.position())); frame.put("client_yaw", client.player.getYRot()); frame.put("client_pitch", client.player.getXRot());
        frame.put("screen", client.screen == null ? "none" : client.screen.getClass().getName()); frame.put("terrain_readiness", TERRAIN.snapshot());
        if (client.player.containerMenu instanceof InnMenu menu) frame.put("actual_client_menu", Map.of("container_id", menu.containerId,
                "can_commit", menu.canCommit(), "state", menu.state().name(), "plan_complete", menu.plan().complete(), "revision", menu.plan().revision()));
        String tracked = (String)snapshot.data().get("guest_uuid");
        if (tracked != null) for (var entity : client.level.entitiesForRendering()) if (entity.getUUID().toString().equals(tracked) && entity instanceof DungeonsMerchant guest) {
            String state = guest.isSleeping() ? "SLEEPING" : guest.getVehicle() instanceof InnSeatEntity ? "SEATED" : "WALKING";
            boolean inView = inView(client, guest);
            frame.put("client_guest", Map.of("uuid", tracked, "position", point(guest.position()), "actual_synced_state", state,
                    "centre_inside_frustum_not_occlusion_test", inView));
            if (inView) CLIENT_STATE_FRAMES.merge(state, 1, Integer::sum);
        }
        FRAMES.add(Map.copyOf(frame)); if (FRAMES.size() % 10 == 0) writeReport("observing", snapshot, null);
    }
    private static boolean inView(Minecraft client, DungeonsMerchant guest) {
        Vec3 forward = client.player.getLookAngle().normalize(), right = forward.cross(new Vec3(0, 1, 0)).normalize(), up = right.cross(forward).normalize();
        Vec3 delta = guest.position().add(0, guest.getBbHeight() / 2, 0).subtract(client.player.getEyePosition());
        double depth = delta.dot(forward), halfHeight = Math.tan(Math.toRadians(65 / 2.0)) * depth;
        double aspect = (double)client.getWindow().getWidth() / Math.max(1, client.getWindow().getHeight());
        return depth > 0 && Math.abs(delta.dot(up)) <= halfHeight && Math.abs(delta.dot(right)) <= halfHeight * aspect;
    }
    private static void writeReport(String status, Snapshot snapshot, String error) throws Exception {
        var report = new LinkedHashMap<String, Object>();
        report.put("status", status); report.put("artifact_sha256", artifactSha); report.put("scope", "Explicitly staged isolated development inn; real owner selection/menu commit and unmodified InnService visitor AI.");
        report.put("visual_review_performed", false); report.put("natural_generation_claimed", false);
        report.put("guest_spawned_by_harness", false); report.put("guest_phases_set_by_harness", false); report.put("guest_teleports_by_harness", 0);
        report.put("setup_time", 13000); report.put("daylight_cycle_during_observation", false);
        report.put("planned_camera_cuts", List.of("owner_selection/menu", "route_in", "seat after genuine mount", "bed after genuine sleep", "route_out after genuine departure"));
        report.put("maximum_world_ticks", MAX_TICKS); report.put("maximum_images", MAX_IMAGES); report.put("technical_terrain_readiness", PREPARATION);
        report.put("actual_client_state_frames_inside_frustum", CLIENT_STATE_FRAMES); report.put("frames", FRAMES); report.put("samples", SAMPLES);
        report.put("visibility_limit", "Frustum and synchronized state evidence do not prove occlusion-free composition, model quality or visual approval.");
        if (snapshot != null) report.put("final_observation", snapshot.data()); if (error != null) report.put("error", error);
        Files.writeString(output.resolve("manifest.json"), new GsonBuilder().setPrettyPrinting().create().toJson(report));
    }
    private static Path authorize(Minecraft client) throws Exception {
        if (!Boolean.getBoolean("dungeonsalive.innShowcase") || !Boolean.getBoolean("dungeonsalive.isolatedInnQa")) throw new IllegalStateException("Both explicit inn QA flags are required");
        Path run = realUnlinked(client.gameDirectory.toPath()); if (!run.endsWith(SUFFIX)) throw new IllegalStateException("Inn capture refuses any other instance");
        Path project = run; for (int i = 0; i < SUFFIX.getNameCount(); i++) project = project.getParent();
        if (project == null || !Files.isRegularFile(realUnlinked(project.resolve("AGENTS.md")))
                || !Files.isRegularFile(realUnlinked(project.resolve("gradle.properties")))) throw new IllegalStateException("Inn QA must remain in the actual project");
        marker(run.resolve("dungeonsalive-inn-prism-qa.txt"), TOKEN);
        Path world = realUnlinked(run.resolve("saves").resolve(WORLD)); var integrated = client.getSingleplayerServer();
        if (!world.equals(realUnlinked(integrated.getWorldPath(LevelResource.ROOT))) || !WORLD.equals(integrated.getWorldData().getLevelName()))
            throw new IllegalStateException("Opened world differs from the isolated inn world");
        marker(world.resolve("dungeonsalive-living-showcase-source.txt"), "isolated-living-showcase-world-v1");
        Path fresh = world.resolve("dungeonsalive-inn-prism-qa.fresh"); marker(fresh, TOKEN);
        Path used = world.resolve("dungeonsalive-inn-prism-qa.used");
        if (Files.exists(used, LinkOption.NOFOLLOW_LINKS) || Files.exists(run.resolve("inn-captures"), LinkOption.NOFOLLOW_LINKS)) throw new IllegalStateException("Inn QA world already used");
        var staging = JsonParser.parseString(Files.readString(realUnlinked(run.getParent().resolve("staging.json")))).getAsJsonObject();
        if (!TOKEN.equals(staging.get("kind").getAsString())) throw new IllegalStateException("Wrong instance staging kind");
        String expected = staging.getAsJsonObject("artifact").get("sha256").getAsString();
        try (var files = Files.list(realUnlinked(run.resolve("mods")))) {
            var jars = files.filter(path -> path.getFileName().toString().endsWith(".jar")).toList();
            if (jars.size() != 1 || !hash(realUnlinked(jars.getFirst())).equals(expected)) throw new IllegalStateException("Inn artifact does not match its native-tested staging report");
        }
        artifactSha = expected; Files.move(fresh, used); return run;
    }
    private static Path realUnlinked(Path path) throws Exception {
        Path normalized = path.toAbsolutePath().normalize();
        for (Path p = normalized; p != null; p = p.getParent()) if (Files.isSymbolicLink(p)) throw new IllegalStateException("Inn QA refuses symlinked path: " + p);
        if (!normalized.equals(normalized.toRealPath())) throw new IllegalStateException("Inn path must resolve exactly"); return normalized;
    }
    private static void marker(Path path, String token) throws Exception {
        Path checked = realUnlinked(path); if (!Files.isRegularFile(checked) || !Files.readString(checked).equals(token + "\n")) throw new IllegalStateException("Missing exact inn QA marker: " + path);
    }
    private static String hash(Path path) throws Exception { return HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256").digest(Files.readAllBytes(path))); }
    private static List<Double> point(Vec3 p) { return List.of(p.x, p.y, p.z); }
    private static void fail(Minecraft client, Exception failure) {
        stopped = true; DungeonsAlive.LOGGER.error("Inn showcase failed", failure);
        try { if (output != null) writeReport("failed", latest, failure.toString()); } catch (Exception ignored) { }
        if (started) client.stop(); // Authorization failures return before started; ordinary sessions are never stopped.
    }
    private record Camera(String id, Vec3 position, float yaw, float pitch) { }
    private record Snapshot(long tick, String mode, Camera camera, boolean finished, boolean complete, String error, Map<String, Object> data) { }
}
