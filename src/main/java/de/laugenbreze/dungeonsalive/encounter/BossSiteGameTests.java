package de.laugenbreze.dungeonsalive.encounter;

import de.laugenbreze.dungeonsalive.DungeonsAlive;
import de.laugenbreze.dungeonsalive.entity.DungeonMob;
import de.laugenbreze.dungeonsalive.entity.MobProfiles;
import java.io.IOException;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtAccounter;
import net.minecraft.nbt.NbtIo;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

/** Uses each real site as the GameTest template, so its complete bounds receive normal cleanup. */
@GameTestHolder(DungeonsAlive.MOD_ID)
@PrefixGameTestTemplate(false)
public final class BossSiteGameTests {
    private BossSiteGameTests() {}

    @GameTest(template = "boss_sites/overgrown_arena", templateNamespace = DungeonsAlive.MOD_ID,
            batch = "boss_sites", skyAccess = true, timeoutTicks = 30)
    public static void overgrownArenaLoadsWithVoluntarySealAndUsableFloor(GameTestHelper helper) {
        validate(helper, "overgrown_arena", "corrupted_cauldron", new BlockPos(10, 0, 10), new BlockPos(10, 1, 18));
    }

    @GameTest(template = "boss_sites/redstone_mineyard", templateNamespace = DungeonsAlive.MOD_ID,
            batch = "boss_sites", skyAccess = true, timeoutTicks = 30)
    public static void redstoneMineyardHasRealRoomForTheMonstrosity(GameTestHelper helper) {
        validate(helper, "redstone_mineyard", "redstone_monstrosity", new BlockPos(11, 0, 10), new BlockPos(19, 1, 15));
    }

    @GameTest(template = "boss_sites/sunken_shrine", templateNamespace = DungeonsAlive.MOD_ID,
            batch = "boss_sites", skyAccess = true, timeoutTicks = 30)
    public static void sunkenShrineKeepsTheEncounterFloorDryAndLootFinite(GameTestHelper helper) {
        validate(helper, "sunken_shrine", "nameless_one", new BlockPos(10, 2, 10), new BlockPos(10, 3, 18));
    }

    private static void validate(GameTestHelper helper, String site, String mob, BlockPos floor, BlockPos chestPosition) {
        var level = helper.getLevel();
        var registry = level.registryAccess();
        helper.assertTrue(registry.registryOrThrow(Registries.STRUCTURE).get(id(site)) != null,
                "The actual Jigsaw structure registry lacks " + site);
        helper.assertTrue(registry.registryOrThrow(Registries.STRUCTURE_SET).get(id(site)) != null,
                "The actual natural structure set is missing " + site);
        helper.assertTrue(registry.registryOrThrow(Registries.TEMPLATE_POOL).get(id("boss_sites/" + site)) != null,
                "The actual start pool is missing " + site);
        try (var stream = level.getServer().getResourceManager().open(id("structure/boss_sites/" + site + ".nbt"))) {
            CompoundTag nbt = NbtIo.readCompressed(stream, NbtAccounter.unlimitedHeap());
            helper.assertTrue(nbt.getInt("DataVersion") == 3955 && nbt.getList("entities", 10).isEmpty(),
                    "Boss site has stale NBT or automatic entity spawns");
            var palette = nbt.getList("palette", 10);
            for (int i = 0; i < palette.size(); i++) {
                var state = palette.getCompound(i);
                var blockId = ResourceLocation.parse(state.getString("Name"));
                helper.assertTrue(BuiltInRegistries.BLOCK.containsKey(blockId), "Unregistered site block: " + blockId);
                var block = BuiltInRegistries.BLOCK.get(blockId);
                var properties = state.getCompound("Properties");
                for (String name : properties.getAllKeys()) {
                    var property = block.getStateDefinition().getProperty(name);
                    helper.assertTrue(property != null && property.getValue(properties.getString(name)).isPresent(),
                            "Invalid site palette state: " + blockId + "/" + name);
                }
            }
        } catch (IOException exception) { throw new IllegalStateException("Could not read boss site " + site, exception); }
        var template = level.getStructureManager().get(id("boss_sites/" + site)).orElseThrow();
        // The helper origin is the control StructureBlock one block below the
        // template (StructureUtils.createStructureBlock uses its default (0,1,0)).
        BlockPos origin = helper.absolutePos(BlockPos.ZERO.above()), size = new BlockPos(template.getSize());
        AABB bounds = new AABB(origin.getX(), origin.getY(), origin.getZ(),
                origin.getX() + size.getX(), origin.getY() + size.getY(), origin.getZ() + size.getZ());
        helper.assertTrue(level.getEntitiesOfClass(DungeonMob.class, bounds).isEmpty(), "Entering a site automatically created an enemy");
        String rejected = EncounterService.validateSite(level, helper.absolutePos(floor.above(2)), MobProfiles.get(mob));
        helper.assertTrue(rejected == null, "The existing seal validator rejected the actual site floor: " + rejected);
        var blockEntity = level.getBlockEntity(helper.absolutePos(chestPosition.above()));
        helper.assertTrue(blockEntity instanceof ChestBlockEntity, "The template did not place its actual loot chest");
        var chest = (ChestBlockEntity)blockEntity;
        helper.assertTrue(chest.getLootTable() != null && chest.getLootTable().location().equals(id("chests/boss_sites/" + site)),
                "Placed chest lost its real loot table reference");
        int seals = 0, count = 0;
        for (int slot = 0; slot < chest.getContainerSize(); slot++) {
            var stack = chest.getItem(slot); // Vanilla server unpack, not a mock loot calculation.
            count += stack.getCount();
            if (stack.is(EncounterContent.SEALS.get(mob).get())) seals += stack.getCount();
        }
        helper.assertTrue(seals == 1 && count >= 6 && count <= 14 && chest.getLootTable() == null,
                "Site loot did not generate one seal and finite provisions exactly once");
        for (int slot = 0; slot < chest.getContainerSize(); slot++) chest.removeItemNoUpdate(slot);
        helper.assertTrue(chest.isEmpty() && chest.getLootTable() == null, "Emptied site chest regenerated loot");
        helper.assertTrue(level.getEntitiesOfClass(DungeonMob.class, bounds).isEmpty(), "Taking the seal automatically started the boss");
        helper.succeed();
    }

    private static ResourceLocation id(String path) { return ResourceLocation.fromNamespaceAndPath(DungeonsAlive.MOD_ID, path); }
}
