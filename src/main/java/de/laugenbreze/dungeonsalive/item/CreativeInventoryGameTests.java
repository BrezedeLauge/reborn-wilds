package de.laugenbreze.dungeonsalive.item;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import de.laugenbreze.dungeonsalive.DungeonsAlive;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

/** Actual registered creative contents and shipped language resources, independent of the list helper. */
@GameTestHolder(DungeonsAlive.MOD_ID)
@PrefixGameTestTemplate(false)
public final class CreativeInventoryGameTests {
    private CreativeInventoryGameTests() {}

    @GameTest(template = "gametest_empty", templateNamespace = DungeonsAlive.MOD_ID)
    public static void registeredTabContainsEveryRealModItemForOrdinaryAndOperatorPlayers(GameTestHelper helper) {
        ResourceLocation tabId = ResourceLocation.fromNamespaceAndPath(DungeonsAlive.MOD_ID, "dungeons_alive");
        helper.assertTrue(BuiltInRegistries.CREATIVE_MODE_TAB.containsKey(tabId), "The Dungeons Alive creative tab is not registered");
        CreativeModeTab tab = BuiltInRegistries.CREATIVE_MODE_TAB.get(tabId);
        Set<Item> expected = new HashSet<>();
        BuiltInRegistries.ITEM.entrySet().forEach(entry -> {
            if (entry.getKey().location().getNamespace().equals(DungeonsAlive.MOD_ID)) expected.add(entry.getValue());
        });
        helper.assertFalse(expected.isEmpty(), "The actual item registry has no Dungeons Alive items");
        for (boolean operator : new boolean[]{false, true}) {
            tab.buildContents(new CreativeModeTab.ItemDisplayParameters(helper.getLevel().enabledFeatures(), operator,
                    helper.getLevel().registryAccess()));
            List<ItemStack> contents = List.copyOf(tab.getDisplayItems());
            Set<Item> actual = new HashSet<>();
            for (ItemStack stack : contents) {
                helper.assertTrue(stack.getCount() == 1 && !stack.isEmpty(), "A creative entry is empty or has an invalid stack count");
                helper.assertTrue(actual.add(stack.getItem()), "The actual creative tab contains an item more than once");
            }
            helper.assertTrue(actual.equals(expected), "The built creative tab omits a registered mod item or includes unrelated items");
            helper.assertTrue(contents.getFirst().is(ModItems.QUEST_JOURNAL.get()), "The travel journal is not the first discovery entry");
            Set<Item> search = new HashSet<>();
            tab.getSearchTabDisplayItems().forEach(stack -> search.add(stack.getItem()));
            helper.assertTrue(search.equals(expected), "Creative search does not expose every registered Dungeons Alive item");
        }
        helper.succeed();
    }

    @GameTest(template = "gametest_empty", templateNamespace = DungeonsAlive.MOD_ID)
    public static void everyRegisteredItemHasShippedGermanAndEnglishDisplayNames(GameTestHelper helper) throws IOException {
        Set<String> keys = new TreeSet<>();
        int checked = 0;
        for (var entry : BuiltInRegistries.ITEM.entrySet()) {
            if (!entry.getKey().location().getNamespace().equals(DungeonsAlive.MOD_ID)) continue;
            keys.add(entry.getValue().getDefaultInstance().getDescriptionId());
            checked++;
        }
        helper.assertTrue(checked > 0, "Language check saw no actual registered mod items");
        keys.add("itemGroup.dungeonsalive");
        var missing = new TreeMap<String, Set<String>>();
        for (String locale : List.of("de_de", "en_us")) {
            JsonObject language = language(locale);
            Set<String> untranslated = new TreeSet<>();
            for (String key : keys) if (!translated(language, key)) untranslated.add(key);
            if (!untranslated.isEmpty()) missing.put(locale, untranslated);
        }
        helper.assertTrue(missing.isEmpty(), "Missing, empty or untranslated real registry display-name keys by language: " + missing);
        helper.succeed();
    }

    private static boolean translated(JsonObject language, String key) {
        var value = language.get(key);
        return value != null && value.isJsonPrimitive() && value.getAsJsonPrimitive().isString()
                && !value.getAsString().isBlank() && !value.getAsString().equals(key);
    }

    private static JsonObject language(String locale) throws IOException {
        String path = "/assets/dungeonsalive/lang/" + locale + ".json";
        try (var input = CreativeInventoryGameTests.class.getResourceAsStream(path)) {
            if (input == null) throw new AssertionError("Missing shipped language resource " + path);
            try (var reader = new InputStreamReader(input, StandardCharsets.UTF_8)) {
                return JsonParser.parseReader(reader).getAsJsonObject();
            }
        }
    }
}
