package de.laugenbreze.dungeonsalive.world.keygate;

import com.mojang.authlib.GameProfile;
import de.laugenbreze.dungeonsalive.DungeonsAlive;
import de.laugenbreze.dungeonsalive.entity.peaceful.DungeonsCreature;
import de.laugenbreze.dungeonsalive.entity.peaceful.ModPeacefulEntities;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.gametest.framework.AfterBatch;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.util.FakePlayer;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

/** Actual placed DoorBlocks, live owned keys, server use and native player mining. Not executed offline. */
@GameTestHolder(DungeonsAlive.MOD_ID)
@PrefixGameTestTemplate(false)
public final class KeyGateGameTests {
    private static final String BATCH = "key_gates";
    private static final Map<ServerLevel, Set<Entity>> ACTORS = new IdentityHashMap<>();
    private KeyGateGameTests() {}
    @AfterBatch(batch = BATCH) public static void cleanup(ServerLevel level) {
        var actors = ACTORS.remove(level);
        if (actors != null) actors.forEach(Entity::discard);
    }

    @GameTest(template = "gametest_empty", templateNamespace = DungeonsAlive.MOD_ID, batch = BATCH, timeoutTicks = 30)
    public static void bothKeysOpenOnlyTheirOwnGateAndRemainAliveThroughRepeatedUse(GameTestHelper helper) {
        floor(helper);
        for (int variant = 0; variant < 2; variant++) {
            KeyGateBlock block = variant == 0 ? KeyGateContent.KEY_GATE.get() : KeyGateContent.DIAMOND_KEY_GATE.get();
            var player = player(helper);
            BlockPos pos = helper.absolutePos(new BlockPos(4 + variant * 9, 1, 6));
            place(helper, player, (BlockItem)block.asItem(), pos);
            helper.assertTrue(owner(helper, pos).ownerId().equals(player.getUUID()), "Placement did not bind the gate to its real placer");
            use(player, block, pos.above()); assertOpen(helper, pos, false);
            var key = key(helper, player, block.requiredGolem(), Vec3.atBottomCenterOf(pos).add(-1, 0, -2));
            float health = key.getHealth();
            use(player, block, pos.above()); assertOpen(helper, pos, true);
            use(player, block, pos); assertOpen(helper, pos, false);
            use(player, block, pos); assertOpen(helper, pos, true);
            helper.assertTrue(key.isAlive() && !key.isRemoved() && key.getHealth() == health
                    && player.getUUID().equals(key.ownerId()), "Using the key consumed, damaged or reassigned the living golem");
            key.discard();
            use(player, block, pos.above()); assertOpen(helper, pos, false);
            use(player, block, pos); assertOpen(helper, pos, false);
        }
        helper.succeed();
    }

    @GameTest(template = "gametest_empty", templateNamespace = DungeonsAlive.MOD_ID, batch = BATCH, timeoutTicks = 30)
    public static void foreignWrongDeadDistantAndOccludedKeysCannotOpenOrTouchNeighborDoors(GameTestHelper helper) {
        floor(helper); var player = player(helper); var stranger = player(helper);
        KeyGateBlock gate = KeyGateContent.KEY_GATE.get();
        BlockPos pos = helper.absolutePos(new BlockPos(7, 1, 6)), adjacent = pos.east(2), vanilla = pos.east(4);
        place(helper, player, KeyGateContent.KEY_GATE_ITEM.get(), pos);
        place(helper, stranger, KeyGateContent.KEY_GATE_ITEM.get(), adjacent);
        place(helper, stranger, (BlockItem)Items.IRON_DOOR, vanilla);
        var vanillaBottom = helper.getLevel().getBlockState(vanilla);
        var vanillaTop = helper.getLevel().getBlockState(vanilla.above());
        Vec3 keyPlace = Vec3.atBottomCenterOf(pos).add(-1, 0, -2);
        var foreign = key(helper, stranger, "key_golem", keyPlace);
        player.setPos(Vec3.atBottomCenterOf(pos).add(0, 0, -2));
        use(player, gate, pos); assertOpen(helper, pos, false); foreign.discard();
        var wrong = key(helper, player, "diamond_key_golem", keyPlace);
        use(player, gate, pos); assertOpen(helper, pos, false); wrong.discard();
        var actual = key(helper, player, "key_golem", keyPlace);
        stranger.setPos(player.position()); use(stranger, gate, pos); assertOpen(helper, pos, false);
        actual.setPos(Vec3.atBottomCenterOf(pos).add(-6, 0, -2));
        use(player, gate, pos); assertOpen(helper, pos, false);
        actual.setPos(keyPlace);
        // The player can see the back of the gate, while stone blocks only the key's ray.
        player.setPos(Vec3.atBottomCenterOf(pos).add(0, 0, 2));
        for (int x = -2; x <= 1; x++) for (int y = 0; y < 3; y++) helper.getLevel().setBlockAndUpdate(pos.offset(x, y, -1), Blocks.STONE.defaultBlockState());
        use(player, gate, pos); assertOpen(helper, pos, false);
        for (int x = -2; x <= 1; x++) for (int y = 0; y < 3; y++) helper.getLevel().setBlockAndUpdate(pos.offset(x, y, -1), Blocks.AIR.defaultBlockState());
        use(player, gate, pos); assertOpen(helper, pos, true); assertOpen(helper, adjacent, false);
        use(player, gate, pos); actual.setHealth(0);
        use(player, gate, pos); assertOpen(helper, pos, false); assertOpen(helper, adjacent, false);
        helper.assertTrue(helper.getLevel().getBlockState(vanilla).equals(vanillaBottom)
                && helper.getLevel().getBlockState(vanilla.above()).equals(vanillaTop), "Key use changed an adjacent ordinary Minecraft door");
        helper.succeed();
    }

    @GameTest(template = "gametest_empty", templateNamespace = DungeonsAlive.MOD_ID, batch = BATCH, timeoutTicks = 30)
    public static void savedLowerOwnershipSurvivesReloadAndUnassignedGateCannotBeClaimedByUse(GameTestHelper helper) {
        floor(helper); var player = player(helper); var stranger = player(helper);
        var gate = KeyGateContent.DIAMOND_KEY_GATE.get();
        BlockPos pos = helper.absolutePos(new BlockPos(7, 1, 6));
        place(helper, player, KeyGateContent.DIAMOND_KEY_GATE_ITEM.get(), pos);
        helper.assertTrue(helper.getLevel().getBlockEntity(pos.above()) == null, "Upper gate created a second independent owner");
        CompoundTag saved = owner(helper, pos).saveWithFullMetadata(helper.getLevel().registryAccess());
        var restored = new KeyGateBlockEntity(pos, helper.getLevel().getBlockState(pos));
        restored.loadWithComponents(saved, helper.getLevel().registryAccess());
        helper.getLevel().setBlockEntity(restored);
        helper.assertTrue(player.getUUID().equals(owner(helper, pos).ownerId()), "Owner UUID did not survive full block-entity NBT");
        key(helper, player, "diamond_key_golem", Vec3.atBottomCenterOf(pos).add(-1, 0, -2));
        stranger.setPos(player.position()); use(stranger, gate, pos); assertOpen(helper, pos, false);
        use(player, gate, pos); assertOpen(helper, pos, true); use(player, gate, pos);
        saved.remove("DungeonsGateOwner"); restored.loadWithComponents(saved, helper.getLevel().registryAccess());
        use(player, gate, pos); assertOpen(helper, pos, false);
        helper.assertTrue(restored.ownerId() == null, "Right-click silently claimed an unassigned/template gate");
        helper.succeed();
    }

    @GameTest(template = "gametest_empty", templateNamespace = DungeonsAlive.MOD_ID, batch = BATCH, timeoutTicks = 30)
    public static void nativeMiningEitherHalfDropsOneGateAndCreativeDropsNone(GameTestHelper helper) {
        floor(helper); var player = player(helper);
        BlockPos pos = helper.absolutePos(new BlockPos(6, 1, 6)), adjacent = pos.east(2);
        place(helper, player, KeyGateContent.KEY_GATE_ITEM.get(), adjacent);
        for (int attempt = 0; attempt < 3; attempt++) {
            place(helper, player, KeyGateContent.KEY_GATE_ITEM.get(), pos);
            player.setGameMode(attempt == 2 ? GameType.CREATIVE : GameType.SURVIVAL);
            player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.DIAMOND_PICKAXE));
            BlockPos broken = attempt == 0 ? pos.above() : pos;
            helper.assertTrue(player.gameMode.destroyBlock(broken), "Real server player mining did not remove a gate half");
            helper.assertTrue(helper.getLevel().getBlockState(pos).isAir() && helper.getLevel().getBlockState(pos.above()).isAir()
                            && helper.getLevel().getBlockEntity(pos) == null,
                    "Mining left an orphan door half or owner block entity");
            var drops = helper.getLevel().getEntitiesOfClass(ItemEntity.class, new AABB(pos).inflate(2),
                    drop -> drop.getItem().is(KeyGateContent.KEY_GATE_ITEM.get()));
            drops.forEach(drop -> track(helper, drop));
            int count = drops.stream().mapToInt(drop -> drop.getItem().getCount()).sum();
            helper.assertTrue(count == (attempt == 2 ? 0 : 1), "Two-half lifecycle duplicated a gate drop or lost the survival drop");
            drops.forEach(Entity::discard); assertOpen(helper, adjacent, false);
        }
        helper.succeed();
    }

    @GameTest(template = "gametest_empty", templateNamespace = DungeonsAlive.MOD_ID, batch = BATCH, timeoutTicks = 30)
    public static void powerDoesNotBypassKeyAndInvalidRemoteUseDoesNotLoadAChunk(GameTestHelper helper) {
        floor(helper); var player = player(helper); var gate = KeyGateContent.KEY_GATE.get();
        BlockPos pos = helper.absolutePos(new BlockPos(7, 1, 6));
        helper.getLevel().setBlockAndUpdate(pos.east(), Blocks.REDSTONE_BLOCK.defaultBlockState());
        place(helper, player, KeyGateContent.KEY_GATE_ITEM.get(), pos); assertOpen(helper, pos, false);
        helper.getLevel().setBlockAndUpdate(pos.east(), Blocks.AIR.defaultBlockState());
        helper.getLevel().setBlockAndUpdate(pos.east(), Blocks.REDSTONE_BLOCK.defaultBlockState());
        assertOpen(helper, pos, false);
        key(helper, player, "key_golem", Vec3.atBottomCenterOf(pos).add(-1, 0, -2));
        use(player, gate, pos); assertOpen(helper, pos, true);
        helper.getLevel().setBlockAndUpdate(pos.east(), Blocks.AIR.defaultBlockState());
        assertOpen(helper, pos, true); use(player, gate, pos); assertOpen(helper, pos, false);
        BlockPos remote = new BlockPos(1_000_008, pos.getY(), 1_000_008);
        helper.assertFalse(helper.getLevel().hasChunkAt(remote), "Unloaded-chunk fixture is unexpectedly already loaded");
        // Reject a stale/invalid interaction before touching an unknown position. Do not
        // construct a BlockPlaceContext here: vanilla itself reads its clicked world block.
        gate.useWithoutItem(gate.defaultBlockState(), helper.getLevel(), remote, player,
                new BlockHitResult(Vec3.atCenterOf(remote), Direction.NORTH, remote, false));
        helper.assertFalse(helper.getLevel().hasChunkAt(remote), "An invalid gate interaction loaded an unknown chunk");
        helper.succeed();
    }

    private static FakePlayer player(GameTestHelper helper) {
        var player = new FakePlayer(helper.getLevel(), new GameProfile(UUID.randomUUID(), "key-gate-test"));
        player.setGameMode(GameType.SURVIVAL); return player;
    }
    private static void place(GameTestHelper helper, FakePlayer player, BlockItem item, BlockPos absolute) {
        player.setPos(Vec3.atBottomCenterOf(absolute).add(0, 0, -2)); player.setYRot(0);
        player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(item));
        var hit = new BlockHitResult(Vec3.atCenterOf(absolute.below()).add(0, .5, 0), Direction.UP, absolute.below(), false);
        helper.assertTrue(item.useOn(new UseOnContext(player, InteractionHand.MAIN_HAND, hit)).consumesAction(), "Real door BlockItem placement failed");
        player.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
        helper.assertTrue(helper.getLevel().getBlockState(absolute).is(item.getBlock())
                && helper.getLevel().getBlockState(absolute.above()).is(item.getBlock()), "BlockItem did not place both door halves");
    }
    private static void use(FakePlayer player, KeyGateBlock gate, BlockPos absolute) {
        gate.useWithoutItem(player.level().getBlockState(absolute), player.level(), absolute, player,
                new BlockHitResult(Vec3.atCenterOf(absolute), Direction.NORTH, absolute, false));
    }
    private static DungeonsCreature key(GameTestHelper helper, FakePlayer owner, String type, Vec3 absolute) {
        var key = ModPeacefulEntities.creatureType(type).create(helper.getLevel());
        helper.assertTrue(key != null, "Missing actual key golem registration");
        track(helper, key); key.setPos(absolute); key.setNoAi(true);
        helper.assertTrue(helper.getLevel().addFreshEntity(key), "Actual key golem could not enter the world");
        key.mobInteract(owner, InteractionHand.MAIN_HAND);
        helper.assertTrue(owner.getUUID().equals(key.ownerId()), "Existing live interaction did not assign golem ownership");
        return key;
    }
    private static KeyGateBlockEntity owner(GameTestHelper helper, BlockPos absolute) {
        helper.assertTrue(helper.getLevel().getBlockEntity(absolute) instanceof KeyGateBlockEntity, "Gate has no real lower owner block entity");
        return (KeyGateBlockEntity)helper.getLevel().getBlockEntity(absolute);
    }
    private static void assertOpen(GameTestHelper helper, BlockPos lower, boolean expected) {
        var bottom = helper.getLevel().getBlockState(lower); var top = helper.getLevel().getBlockState(lower.above());
        helper.assertTrue(bottom.getBlock() instanceof KeyGateBlock && top.getBlock() == bottom.getBlock()
                        && bottom.getValue(DoorBlock.HALF) == DoubleBlockHalf.LOWER && top.getValue(DoorBlock.HALF) == DoubleBlockHalf.UPPER
                        && bottom.getValue(DoorBlock.OPEN) == expected && top.getValue(DoorBlock.OPEN) == expected,
                "Expected both halves to be " + (expected ? "open" : "closed"));
    }
    private static void floor(GameTestHelper helper) {
        for (int x = 0; x < 20; x++) for (int z = 0; z < 12; z++) helper.setBlock(new BlockPos(x, 0, z), Blocks.STONE);
    }
    private static void track(GameTestHelper helper, Entity entity) {
        ACTORS.computeIfAbsent(helper.getLevel(), ignored -> new HashSet<>()).add(entity);
    }
}
