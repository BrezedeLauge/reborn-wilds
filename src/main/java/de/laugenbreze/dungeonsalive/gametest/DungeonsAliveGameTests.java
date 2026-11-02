package de.laugenbreze.dungeonsalive.gametest;

import de.laugenbreze.dungeonsalive.DungeonsAlive;
import de.laugenbreze.dungeonsalive.ambient.AmbientParticles;
import de.laugenbreze.dungeonsalive.item.ModItems;
import de.laugenbreze.dungeonsalive.quest.QuestService;
import de.laugenbreze.dungeonsalive.quest.core.ObjectiveType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

import java.util.Set;

/**
 * Tests real registries, server resource reload, and item behavior in a running NeoForge server.
 * NeoForge discovers this class only when its development GameTest support is enabled.
 */
@GameTestHolder(DungeonsAlive.MOD_ID)
@PrefixGameTestTemplate(false)
public final class DungeonsAliveGameTests {
    private static final String TEMPLATE = "gametest_empty";

    private DungeonsAliveGameTests() {}

    @GameTest(template = TEMPLATE, templateNamespace = DungeonsAlive.MOD_ID, timeoutTicks = 40)
    public static void registeredContentIsAvailable(GameTestHelper helper) {
        helper.assertTrue(BuiltInRegistries.ITEM.get(id("quest_journal")) == ModItems.QUEST_JOURNAL.get(),
                "Quest journal is not registered under dungeonsalive:quest_journal");
        helper.assertTrue(BuiltInRegistries.ITEM.get(id("wind_horn")) == ModItems.WIND_HORN.get(),
                "Wind horn is not registered under dungeonsalive:wind_horn");
        helper.assertTrue(ModItems.QUEST_JOURNAL.get() != Items.AIR && ModItems.WIND_HORN.get() != Items.AIR,
                "Registered mod items must not resolve to AIR");
        helper.assertTrue(BuiltInRegistries.PARTICLE_TYPE.get(id("butterfly")) == AmbientParticles.BUTTERFLY.get(),
                "Butterfly particle registry entry is missing");
        helper.assertTrue(BuiltInRegistries.PARTICLE_TYPE.get(id("firefly")) == AmbientParticles.FIREFLY.get(),
                "Firefly particle registry entry is missing");
        helper.assertTrue(BuiltInRegistries.PARTICLE_TYPE.get(id("spore")) == AmbientParticles.SPORE.get(),
                "Spore particle registry entry is missing");
        helper.succeed();
    }

    @GameTest(template = TEMPLATE, templateNamespace = DungeonsAlive.MOD_ID, timeoutTicks = 40)
    public static void serverLoadsDefaultQuestData(GameTestHelper helper) {
        var bundle = QuestService.repository().snapshot();
        Set<String> required = Set.of("dungeonsalive:small_wonders", "dungeonsalive:woodland_paths",
                "dungeonsalive:restless_nights", "dungeonsalive:a_place_to_return");
        helper.assertTrue(bundle.revision() > 0, "Server resource reload did not publish a quest catalog");
        helper.assertTrue(bundle.documents().keySet().containsAll(required),
                "Default quest data is missing: loaded " + bundle.documents().keySet());
        helper.assertTrue(bundle.catalog().definitions().keySet().equals(bundle.documents().keySet()),
                "Quest metadata and domain definitions disagree after server reload");
        if (ModList.get().isLoaded("enchantwithmob")) {
            ResourceLocation enchanterId = ResourceLocation.parse("enchantwithmob:enchanter");
            helper.assertTrue(BuiltInRegistries.ENTITY_TYPE.containsKey(enchanterId),
                    "Enchant With Mob is loaded but its expected enchanter entity is missing");
            var enchanterQuest = bundle.documents().get("dungeonsalive:the_enchanter");
            if (enchanterQuest != null) {
                helper.assertTrue(enchanterQuest.definition().objectives().stream().anyMatch(objective ->
                                objective.type() == ObjectiveType.KILL_ENTITY
                                        && objective.target().equals(enchanterId.toString())),
                        "Optional Enchanter quest does not target the actual registered enchanter");
            }
            DungeonsAlive.LOGGER.info("GameTest verified optional Enchanter registry; {} quests loaded, Enchanter quest present: {}",
                    bundle.documents().size(), enchanterQuest != null);
        }
        helper.succeed();
    }

    @GameTest(template = TEMPLATE, templateNamespace = DungeonsAlive.MOD_ID, timeoutTicks = 40)
    public static void windHornRespectsTargetsRadiusAndCooldown(GameTestHelper helper) {
        prepareFloor(helper);
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        Vec3 origin = helper.absoluteVec(new Vec3(4.5, 1, 5.5));
        player.setPos(origin.x, origin.y, origin.z);
        player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(ModItems.WIND_HORN.get()));

        var closeZombie = helper.spawnWithNoFreeWill(EntityType.ZOMBIE, new Vec3(7.5, 1, 5.5));
        var distantZombie = helper.spawnWithNoFreeWill(EntityType.ZOMBIE, new Vec3(12.5, 1, 5.5));
        var cow = helper.spawnWithNoFreeWill(EntityType.COW, new Vec3(4.5, 1, 8.5));
        closeZombie.setDeltaMovement(Vec3.ZERO);
        distantZombie.setDeltaMovement(Vec3.ZERO);
        cow.setDeltaMovement(Vec3.ZERO);
        float healthBefore = closeZombie.getHealth();

        var horn = ModItems.WIND_HORN.get();
        var firstUse = horn.use(helper.getLevel(), player, InteractionHand.MAIN_HAND);
        helper.assertTrue(firstUse.getResult().consumesAction(), "First server-side horn use did not succeed");
        helper.assertTrue(player.getCooldowns().isOnCooldown(horn), "Horn did not reserve its cooldown");
        helper.assertTrue(closeZombie.hasEffect(MobEffects.MOVEMENT_SLOWDOWN),
                "Nearby hostile mob did not receive the defensive slowing effect");
        Vec3 awayFromPlayer = closeZombie.position().subtract(player.position());
        helper.assertTrue(closeZombie.getDeltaMovement().dot(awayFromPlayer) > 0,
                "Nearby hostile mob was not knocked away from the player");
        helper.assertTrue(closeZombie.getHealth() == healthBefore, "Wind horn should not deal damage");
        helper.assertFalse(distantZombie.hasEffect(MobEffects.MOVEMENT_SLOWDOWN),
                "A hostile mob beyond the horn radius was affected");
        helper.assertTrue(distantZombie.getDeltaMovement().equals(Vec3.ZERO),
                "A hostile mob beyond the horn radius received motion");
        helper.assertFalse(cow.hasEffect(MobEffects.MOVEMENT_SLOWDOWN), "Peaceful animal was affected");
        helper.assertTrue(cow.getDeltaMovement().equals(Vec3.ZERO), "Peaceful animal was knocked back");

        // Remove the first effect to prove that repeated calls cannot reapply it during cooldown.
        closeZombie.removeEffect(MobEffects.MOVEMENT_SLOWDOWN);
        closeZombie.setDeltaMovement(Vec3.ZERO);
        var replay = horn.use(helper.getLevel(), player, InteractionHand.MAIN_HAND);
        helper.assertTrue(replay.getResult() == InteractionResult.FAIL, "Cooldown allowed immediate repeated use");
        helper.assertFalse(closeZombie.hasEffect(MobEffects.MOVEMENT_SLOWDOWN),
                "Repeated use reapplied an effect while the horn was cooling down");
        helper.assertTrue(closeZombie.getDeltaMovement().equals(Vec3.ZERO),
                "Repeated use reapplied knockback while the horn was cooling down");
        helper.succeed();
    }

    private static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(DungeonsAlive.MOD_ID, path);
    }

    private static void prepareFloor(GameTestHelper helper) {
        for (int x = 0; x < 20; x++) {
            for (int z = 0; z < 12; z++) {
                helper.setBlock(new BlockPos(x, 0, z), Blocks.STONE);
            }
        }
    }
}
