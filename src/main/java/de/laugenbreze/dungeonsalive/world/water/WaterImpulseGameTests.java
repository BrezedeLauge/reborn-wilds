package de.laugenbreze.dungeonsalive.world.water;

import de.laugenbreze.dungeonsalive.DungeonsAlive;
import de.laugenbreze.dungeonsalive.network.CurrentImpulsePayload;
import io.netty.buffer.Unpooled;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.function.Consumer;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.AfterBatch;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

/** Observe the actual registered event scheduler separately from vanilla's own fluid push. */
@GameTestHolder(DungeonsAlive.MOD_ID)
@PrefixGameTestTemplate(false)
public final class WaterImpulseGameTests {
    private static final String BATCH = "water_current_impulses";
    private record Fixture(ItemEntity item, Consumer<EntityTickEvent.Post> before, Consumer<LevelTickEvent.Post> after, boolean enabled, double strength) {}
    private static final Map<ServerLevel, Fixture> FIXTURES = new IdentityHashMap<>();

    @AfterBatch(batch = BATCH)
    public static void cleanup(ServerLevel level) {
        var fixture = FIXTURES.remove(level);
        if (fixture == null) return;
        NeoForge.EVENT_BUS.unregister(fixture.before);
        NeoForge.EVENT_BUS.unregister(fixture.after);
        fixture.item.discard();
        WaterCurrents.ENABLED.set(fixture.enabled);
        WaterCurrents.STRENGTH.set(fixture.strength);
    }

    @GameTest(template = "gametest_empty", templateNamespace = DungeonsAlive.MOD_ID, batch = BATCH, timeoutTicks = 30)
    public static void realWetItemReceivesAnAdditionalBoundedServerImpulseFromTheRegisteredScheduler(GameTestHelper helper) {
        var level = helper.getLevel();
        var origin = helper.absolutePos(new BlockPos(5, 3, 5));
        for (int x = (origin.getX() - 18) >> 4; x <= (origin.getX() + 18) >> 4; x++)
            for (int z = (origin.getZ() - 18) >> 4; z <= (origin.getZ() + 18) >> 4; z++) level.getChunk(x, z);
        for (BlockPos pos : BlockPos.betweenClosed(origin.offset(-3, -1, -3), origin.offset(3, 0, 3)))
            level.setBlock(pos, Blocks.STONE.defaultBlockState(), 2);
        level.setBlock(origin, Blocks.WATER.defaultBlockState().setValue(LiquidBlock.LEVEL, 1), 2);
        level.setBlock(origin.west(), Blocks.WATER.defaultBlockState(), 2);
        level.setBlock(origin.east(), Blocks.WATER.defaultBlockState().setValue(LiquidBlock.LEVEL, 5), 2);
        helper.assertTrue(WaterCurrentField.sample(level, origin).speed() > 0, "Real loaded water fixture has no measurable native downhill flow");
        var item = new ItemEntity(level, origin.getX() + .5, origin.getY() + .1, origin.getZ() + .5, new ItemStack(Items.STONE));
        item.setNoGravity(true);
        item.setDeltaMovement(Vec3.ZERO);
        Vec3[] beforeVelocity = {null}, direction = {Vec3.ZERO};
        long[] observedTick = {Long.MIN_VALUE};
        boolean[] received = {false}, invalidImpulse = {false};
        Consumer<EntityTickEvent.Post> before = event -> {
            if (event.getEntity() != item) return;
            beforeVelocity[0] = item.getDeltaMovement();
            observedTick[0] = level.getGameTime();
            direction[0] = WaterCurrentField.sample(level, BlockPos.containing(item.getX(), item.getY() + .05, item.getZ())).direction();
        };
        Consumer<LevelTickEvent.Post> after = event -> {
            if (event.getLevel() != level || beforeVelocity[0] == null || observedTick[0] != level.getGameTime()) return;
            Vec3 added = item.getDeltaMovement().subtract(beforeVelocity[0]);
            if (added.lengthSqr() < 1.0E-8) return;
            invalidImpulse[0] |= added.dot(direction[0]) <= 0 || added.length() > .028001 || Math.abs(added.y) >= 1.0E-8;
            received[0] = true;
        };
        FIXTURES.put(level, new Fixture(item, before, after, WaterCurrents.ENABLED.get(), WaterCurrents.STRENGTH.get()));
        WaterCurrents.ENABLED.set(true); WaterCurrents.STRENGTH.set(1.0);
        NeoForge.EVENT_BUS.addListener(EventPriority.LOWEST, before);
        NeoForge.EVENT_BUS.addListener(EventPriority.LOWEST, after);
        helper.assertTrue(level.addFreshEntity(item), "Could not add real wet item fixture");
        // Original network codec round trip, without pretending this headless test has a client receiver.
        var buffer = new RegistryFriendlyByteBuf(Unpooled.buffer(), level.registryAccess());
        try {
            var outgoing = new CurrentImpulsePayload(item.getId(), .015F, -.01F);
            CurrentImpulsePayload.CODEC.encode(buffer, outgoing);
            helper.assertTrue(CurrentImpulsePayload.CODEC.decode(buffer).equals(outgoing), "Actual S2C payload codec lost its controlled entity/impulse");
        } finally { buffer.release(); }
        helper.succeedWhen(() -> {
            helper.assertFalse(invalidImpulse[0], "Registered scheduler changed vertical motion or exceeded its bounded downstream impulse");
            helper.assertTrue(received[0], "No additional server impulse was observed through the real registered event scheduler");
        });
    }
    private WaterImpulseGameTests() {}
}
