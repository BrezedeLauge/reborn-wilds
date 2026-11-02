package de.laugenbreze.dungeonsalive.entity.peaceful;

import de.laugenbreze.dungeonsalive.DungeonsAlive;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.gametest.framework.GameTestGenerator;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.gametest.framework.TestFunction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtAccounter;
import net.minecraft.nbt.NbtIo;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.phys.AABB;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import net.neoforged.neoforge.gametest.GameTestHolder;

/** Optional pack integration: absent unless the dedicated camp-test flag is explicitly set. */
@GameTestHolder(DungeonsAlive.MOD_ID)
public final class CampGameTests {
    private CampGameTests() {}
    @GameTestGenerator
    public static Collection<TestFunction> campTests() {
        if (!Boolean.getBoolean("dungeonsalive.test.camps")) return List.of();
        return List.of(
                new TestFunction("dungeons_camps", "camps.merchant_template", "dungeonsalive:gametest_empty", 40, 0, true, CampGameTests::merchant),
                new TestFunction("dungeons_camps", "camps.captive_rescue", "dungeonsalive:gametest_empty", 40, 0, true, CampGameTests::captive));
    }
    private static ResourceLocation id(String path) { return ResourceLocation.fromNamespaceAndPath(DungeonsAlive.MOD_ID, path); }

    private static StructureTemplate loadAndValidate(GameTestHelper helper, String name) {
        var access = helper.getLevel().registryAccess();
        helper.assertTrue(access.registryOrThrow(Registries.STRUCTURE).get(id("dungeons_camp")) != null, "Optional Jigsaw structure was not registered");
        helper.assertTrue(access.registryOrThrow(Registries.TEMPLATE_POOL).get(id("camps/start")) != null, "Optional start pool was not registered");
        helper.assertTrue(access.registryOrThrow(Registries.STRUCTURE_SET).get(id("dungeons_camps")) != null, "Optional spread placement was not registered");
        try (var stream = helper.getLevel().getServer().getResourceManager().open(id("structure/camps/" + name + ".nbt"))) {
            CompoundTag data = NbtIo.readCompressed(stream, NbtAccounter.unlimitedHeap());
            helper.assertTrue(data.getInt("DataVersion") == 3955, "Unexpected camp NBT version");
            var palette = data.getList("palette", 10);
            for (int i = 0; i < palette.size(); i++) {
                CompoundTag state = palette.getCompound(i);
                ResourceLocation blockId = ResourceLocation.parse(state.getString("Name"));
                helper.assertTrue(BuiltInRegistries.BLOCK.containsKey(blockId), "Unregistered camp block " + blockId);
                var block = BuiltInRegistries.BLOCK.get(blockId);
                var properties = state.getCompound("Properties");
                for (String key : properties.getAllKeys()) {
                    var property = block.getStateDefinition().getProperty(key);
                    helper.assertTrue(property != null && property.getValue(properties.getString(key)).isPresent(), "Invalid camp state " + blockId + "/" + key);
                }
            }
            var entities = data.getList("entities", 10);
            helper.assertTrue(entities.size() == 1, "Camp must contain exactly one intended NPC");
            CompoundTag npc = entities.getCompound(0).getCompound("nbt");
            ResourceLocation entityId = ResourceLocation.parse(npc.getString("id"));
            helper.assertTrue(BuiltInRegistries.ENTITY_TYPE.containsKey(entityId), "Camp references an unknown NPC " + entityId);
            helper.assertTrue(!npc.contains("UUID") && npc.getBoolean("PersistenceRequired"), "Template must create a fresh persistent NPC identity");
            helper.assertFalse(npc.contains("DungeonsCampHome"), "Template must not bake relative coordinates into a merchant's world home");
        } catch (IOException error) { throw new IllegalStateException("Could not read the optional camp resource", error); }
        return helper.getLevel().getStructureManager().get(id("camps/" + name)).orElseThrow(() -> new IllegalStateException("Camp template did not load: " + name));
    }

    private static AABB place(GameTestHelper helper, StructureTemplate template) {
        BlockPos origin = helper.absolutePos(new BlockPos(1, 1, 1));
        helper.assertTrue(template.placeInWorld(helper.getLevel(), origin, origin,
                new StructurePlaceSettings().setIgnoreEntities(false).setFinalizeEntities(true), helper.getLevel().random, 3), "Camp template placement failed");
        var size = template.getSize();
        return new AABB(origin.getX(), origin.getY(), origin.getZ(),
                origin.getX() + size.getX(), origin.getY() + size.getY(), origin.getZ() + size.getZ()).inflate(.1);
    }

    private static void merchant(GameTestHelper helper) {
        StructureTemplate template = loadAndValidate(helper, "merchant");
        helper.assertTrue(template.getSize().getX() == 9 && template.getSize().getZ() == 9, "Merchant template dimensions changed");
        AABB box = place(helper, template);
        var placed = helper.getLevel().getEntitiesOfClass(DungeonsMerchant.class, box, m -> m.getTags().contains("dungeonsalive_camp_merchant"));
        helper.assertTrue(placed.size() == 1, "Template did not immediately create exactly one camp merchant");
        BlockPos placedHome = placed.getFirst().blockPosition().immutable();
        var placedId = placed.getFirst().getUUID();
        helper.assertTrue(placed.getFirst().campHome() == null, "Home must wait for the first actual server AI tick");
        helper.runAtTickTime(2, () -> {
            var merchants = helper.getLevel().getEntitiesOfClass(DungeonsMerchant.class, box, m -> m.getTags().contains("dungeonsalive_camp_merchant"));
            helper.assertTrue(merchants.size() == 1, "Template did not spawn exactly one real camp merchant");
            var merchant = merchants.getFirst();
            helper.assertTrue(merchant.getType() == ModPeacefulEntities.merchantType("village_merchant"), "Wrong merchant type");
            helper.assertTrue(!merchant.getOffers().isEmpty() && merchant.isPersistenceRequired(), "Camp merchant lacks actual trades/persistence");
            helper.assertTrue(merchant.getUUID().equals(placedId) && merchant.hasCampRoutine(), "Placed merchant identity or camp routine tag changed");
            helper.assertTrue(placedHome.equals(merchant.campHome()), "Camp home did not use the actual placed world coordinates");
            helper.succeed();
        });
    }

    private static void captive(GameTestHelper helper) {
        StructureTemplate template = loadAndValidate(helper, "captive");
        helper.assertTrue(template.getSize().getX() == 11 && template.getSize().getZ() == 9, "Captive template dimensions changed");
        AABB box = place(helper, template);
        helper.runAtTickTime(2, () -> {
            var civilians = helper.getLevel().getEntitiesOfClass(DungeonsCreature.class, box, m -> m.getTags().contains("dungeonsalive_camp_captive"));
            helper.assertTrue(civilians.size() == 1, "Template did not spawn exactly one real captive");
            var civilian = civilians.getFirst();
            helper.assertTrue(civilian.civilian() && !civilian.rescued() && civilian.waiting() && civilian.ownerId() == null, "Captive did not load in its restrained civilian state");
            var rescuer = helper.makeMockPlayer(GameType.SURVIVAL);
            rescuer.setPos(civilian.position().add(0, 0, -1));
            helper.assertTrue(civilian.mobInteract(rescuer, InteractionHand.MAIN_HAND).consumesAction(), "Real rescue interaction failed");
            helper.assertTrue(civilian.rescued() && !civilian.waiting() && rescuer.getUUID().equals(civilian.ownerId()), "Rescue did not assign a following civilian");
            civilian.mobInteract(rescuer, InteractionHand.MAIN_HAND);
            CompoundTag saved = new CompoundTag(); civilian.addAdditionalSaveData(saved);
            var restored = ModPeacefulEntities.creatureType("captured_blacksmith").create(helper.getLevel());
            restored.setPos(civilian.position()); restored.readAdditionalSaveData(saved);
            helper.assertTrue(restored.rescued() && restored.waiting() && rescuer.getUUID().equals(restored.ownerId()), "Civilian rescue/wait/retainer state did not survive reload");
            var other = helper.makeMockPlayer(GameType.SURVIVAL); other.setPos(civilian.position().add(0, 0, -1));
            restored.mobInteract(other, InteractionHand.MAIN_HAND);
            helper.assertTrue(restored.waiting() && rescuer.getUUID().equals(restored.ownerId()), "Another player took over an established rescue");
            restored.mobInteract(rescuer, InteractionHand.MAIN_HAND);
            helper.assertFalse(restored.waiting(), "Saved rescuer could not resume following");
            helper.assertTrue(civilian.getTarget() == null, "Rescued civilian received a combat target");
            helper.succeed();
        });
    }
}
