package de.laugenbreze.dungeonsalive.entity.peaceful;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** Explicit sandbox ecology, using existing entities. These are not original Dungeons spawn tables. */
public final class WildlifeHabitats {
    public enum Layer { GROUND, CANOPY, WATER, WATER_SURFACE, LOCAL_AIR }
    public record Population(String id, Layer layer, int min, int max) {}
    private static final Map<String, List<Population>> BIOMES = new LinkedHashMap<>();
    private static Population ground(String id) { return new Population(id, Layer.GROUND, 2, 4); }
    private static Population air(String id) { return new Population(id, Layer.CANOPY, 3, 5); }
    private static final Population WISP = new Population("wisp", Layer.LOCAL_AIR, 3, 5);
    private static final Population SQUID = new Population("pet_baby_squid", Layer.WATER, 3, 5);
    private static final Population DUCKS = new Population("pet_duck", Layer.WATER_SURFACE, 3, 5);
    static {
        add("plains sunflower_plains meadow", ground("wooly_cow"), air("pet_raven"), ground("pet_hedgehog"), DUCKS);
        add("forest birch_forest old_growth_birch_forest flower_forest dark_forest cherry_grove",
                air("pet_raven"), ground("pet_hedgehog"), ground("pet_racoon"), WISP, DUCKS);
        add("jungle sparse_jungle bamboo_jungle", air("pet_toucan"), air("pet_golden_parrot"), ground("pet_red_panda"), WISP);
        add("taiga old_growth_pine_taiga old_growth_spruce_taiga snowy_taiga grove",
                air("pet_raven"), ground("pet_dark_baby_goat"), ground("pet_racoon"), WISP);
        add("savanna savanna_plateau windswept_savanna", air("vulture"), ground("wooly_cow"));
        add("desert badlands eroded_badlands wooded_badlands", air("vulture"));
        add("windswept_hills windswept_gravelly_hills windswept_forest stony_peaks jagged_peaks frozen_peaks snowy_slopes",
                ground("pet_dark_baby_goat"), air("pet_raven"));
        add("snowy_plains ice_spikes", ground("pet_royal_penguin"), air("pet_raven"));
        add("swamp mangrove_swamp", ground("pet_racoon"), WISP, SQUID, DUCKS);
        add("river", SQUID, air("pet_raven"), WISP, DUCKS);
        add("frozen_river", SQUID, ground("pet_royal_penguin"));
        add("beach", ground("pet_ruby_baby_turtle"), air("pet_raven"));
        add("snowy_beach stony_shore", ground("pet_royal_penguin"), air("pet_raven"));
        add("ocean deep_ocean cold_ocean deep_cold_ocean lukewarm_ocean deep_lukewarm_ocean warm_ocean frozen_ocean deep_frozen_ocean", SQUID);
        add("mushroom_fields", ground("pet_cluckshroom"), WISP);
        add("lush_caves", ground("pet_mole"), WISP, SQUID);
        add("dripstone_caves deep_dark", ground("pet_mole"), WISP);
        add("nether_wastes crimson_forest warped_forest soul_sand_valley basalt_deltas",
                new Population("pet_baby_ghast", Layer.LOCAL_AIR, 2, 4), ground("pet_zombified_baby_pig"));
        add("the_end end_highlands end_midlands end_barrens small_end_islands", WISP);
        // The technical empty-world biome has no habitat/terrain. Do not manufacture a population in the void.
        add("the_void");
        BIOMES.put("dungeonsalive:amber_grove", BIOMES.get("minecraft:forest"));
        BIOMES.put("dungeonsalive:luminous_marsh", BIOMES.get("minecraft:swamp"));
        BIOMES.put("dungeonsalive:ember_crags", BIOMES.get("minecraft:savanna"));
        BIOMES.put("dungeonsalive:blighted_thicket", List.of(WISP));
    }
    private static void add(String biomes, Population... populations) {
        for (String biome : biomes.split(" ")) {
            if (BIOMES.putIfAbsent("minecraft:" + biome, List.of(populations)) != null)
                throw new IllegalStateException("Duplicate habitat " + biome);
        }
    }
    public static List<Population> forBiome(String id) { return BIOMES.getOrDefault(id, List.of()); }
    public static Map<String, List<Population>> all() { return Map.copyOf(BIOMES); }
    private WildlifeHabitats() {}
}
