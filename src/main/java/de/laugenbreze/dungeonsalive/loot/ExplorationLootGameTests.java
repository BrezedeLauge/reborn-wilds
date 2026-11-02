package de.laugenbreze.dungeonsalive.loot;

import de.laugenbreze.dungeonsalive.DungeonsAlive;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

/** Actual registered vanilla tables and NeoForge's loaded modifier chain, with fixed server seeds. */
@GameTestHolder(DungeonsAlive.MOD_ID)
@PrefixGameTestTemplate(false)
public final class ExplorationLootGameTests {
    private static final List<String> ORDINARY = List.of("simple_dungeon", "abandoned_mineshaft", "desert_pyramid", "jungle_temple",
            "pillager_outpost", "buried_treasure", "nether_bridge");
    private static final List<String> ENDGAME = List.of("woodland_mansion", "ancient_city", "bastion_treasure", "bastion_bridge",
            "bastion_hoglin_stable", "bastion_other", "end_city_treasure");
    private static final Set<String> RARE = Set.of("heartstealer", "stormlander", "suns_grace", "frost_scythe", "whirlwind", "fighters_bindings");
    private ExplorationLootGameTests() {}

    @GameTest(template = "gametest_empty", templateNamespace = DungeonsAlive.MOD_ID, batch = "exploration_loot", timeoutTicks = 60)
    public static void realTreasureTablesKeepEveryVanillaStackAndAppendAtMostOneGearItem(GameTestHelper helper) {
        List<String> tables = new ArrayList<>(ORDINARY); tables.addAll(ENDGAME);
        var params = params(helper);
        for (String name : tables) {
            var table = table(helper, name);
            int awarded = 0, unchanged = 0;
            for (long seed = 1; seed <= 64; seed++) {
                var original = vanilla(table, params, seed);
                var actual = table.getRandomItems(params, seed);
                assertOriginalPrefix(helper, original, actual, name);
                helper.assertTrue(actual.size() >= original.size() && actual.size() <= original.size() + 1,
                        "A treasure roll added more than one item in " + name);
                if (actual.size() == original.size()) { unchanged++; continue; }
                awarded++;
                var bonus = actual.getLast();
                var itemId = BuiltInRegistries.ITEM.getKey(bonus.getItem());
                helper.assertTrue(itemId.getNamespace().equals(DungeonsAlive.MOD_ID) && bonus.getCount() == 1,
                        "The added treasure was not exactly one actual registered Dungeons item");
                helper.assertTrue(!ORDINARY.contains(name) || !RARE.contains(itemId.getPath()),
                        "An ordinary chest awarded an endgame unique");
            }
            helper.assertTrue(awarded > 0 && unchanged > 0, "The actual loaded modifier was absent or unconditional for " + name);
        }
        helper.succeed();
    }

    @GameTest(template = "gametest_empty", templateNamespace = DungeonsAlive.MOD_ID, batch = "exploration_loot", timeoutTicks = 60)
    public static void unlistedTablesStayUnchangedAndEndgameCanReallyYieldAUnique(GameTestHelper helper) {
        var params = params(helper);
        for (String name : List.of("village/village_weaponsmith", "shipwreck_supply", "stronghold_corridor")) {
            var table = table(helper, name);
            for (long seed = 1; seed <= 16; seed++) {
                var original = vanilla(table, params, seed);
                var actual = table.getRandomItems(params, seed);
                helper.assertTrue(actual.size() == original.size(), "An unlisted table received exploration gear: " + name);
                assertOriginalPrefix(helper, original, actual, name);
            }
        }
        var endCity = table(helper, "end_city_treasure");
        boolean foundUnique = false;
        for (long seed = 1; seed <= 1_024 && !foundUnique; seed++) {
            var original = vanilla(endCity, params, seed);
            var actual = endCity.getRandomItems(params, seed);
            if (actual.size() == original.size() + 1) {
                var id = BuiltInRegistries.ITEM.getKey(actual.getLast().getItem());
                foundUnique = id.getNamespace().equals(DungeonsAlive.MOD_ID) && RARE.contains(id.getPath());
            }
        }
        helper.assertTrue(foundUnique, "No actual endgame unique was produced by the fixed bounded seed sequence");
        helper.succeed();
    }

    private static LootParams params(GameTestHelper helper) {
        return new LootParams.Builder(helper.getLevel()).withParameter(LootContextParams.ORIGIN,
                helper.absoluteVec(new Vec3(5, 1, 5))).create(LootContextParamSets.CHEST);
    }

    private static LootTable table(GameTestHelper helper, String name) {
        var key = ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.withDefaultNamespace("chests/" + name));
        var table = helper.getLevel().getServer().reloadableRegistries().getLootTable(key);
        helper.assertTrue(table != LootTable.EMPTY, "The actual vanilla loot table did not load: " + name);
        return table;
    }

    @SuppressWarnings("deprecation")
    private static List<ItemStack> vanilla(LootTable table, LootParams params, long seed) {
        List<ItemStack> original = new ArrayList<>();
        var context = new LootContext.Builder(params).withOptionalRandomSeed(seed).create(Optional.empty());
        // Raw is intentional only for the baseline: actual rolls above use the
        // normal API, which invokes the registered Global Loot Modifier chain.
        table.getRandomItemsRaw(context, LootTable.createStackSplitter(context.getLevel(), original::add));
        return original;
    }

    private static void assertOriginalPrefix(GameTestHelper helper, List<ItemStack> original, List<ItemStack> actual, String name) {
        helper.assertTrue(actual.size() >= original.size(), "Exploration loot removed vanilla entries: " + name);
        for (int i = 0; i < original.size(); i++) helper.assertTrue(ItemStack.matches(original.get(i), actual.get(i)),
                "Exploration loot changed an existing vanilla stack or component: " + name + " slot " + i);
    }
}
