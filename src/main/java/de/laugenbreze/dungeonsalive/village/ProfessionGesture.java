package de.laugenbreze.dungeonsalive.village;

import java.util.Map;

/** Stable registry IDs map to distinct motions. Third-party professions fall back to idle/walking. */
public enum ProfessionGesture {
    ARMORER, BUTCHER, CARTOGRAPHER, CLERIC, FARMER, FISHERMAN, FLETCHER, LEATHERWORKER,
    LIBRARIAN, MASON, SHEPHERD, TOOLSMITH, WEAPONSMITH,
    VILLAGE_MERCHANT, LUXURY_MERCHANT, MYSTERY_MERCHANT, BLACKSMITH, GIFT_WRAPPER, ENCHANTSMITH,
    NONE;

    private static final Map<String, ProfessionGesture> BY_ID = Map.ofEntries(
            Map.entry("minecraft:armorer", ARMORER), Map.entry("minecraft:butcher", BUTCHER),
            Map.entry("minecraft:cartographer", CARTOGRAPHER), Map.entry("minecraft:cleric", CLERIC),
            Map.entry("minecraft:farmer", FARMER), Map.entry("minecraft:fisherman", FISHERMAN),
            Map.entry("minecraft:fletcher", FLETCHER), Map.entry("minecraft:leatherworker", LEATHERWORKER),
            Map.entry("minecraft:librarian", LIBRARIAN), Map.entry("minecraft:mason", MASON),
            Map.entry("minecraft:shepherd", SHEPHERD), Map.entry("minecraft:toolsmith", TOOLSMITH),
            Map.entry("minecraft:weaponsmith", WEAPONSMITH),
            Map.entry("dungeonsalive:village_merchant", VILLAGE_MERCHANT),
            Map.entry("dungeonsalive:luxury_merchant", LUXURY_MERCHANT),
            Map.entry("dungeonsalive:mystery_merchant", MYSTERY_MERCHANT),
            Map.entry("dungeonsalive:blacksmith", BLACKSMITH), Map.entry("dungeonsalive:gift_wrapper", GIFT_WRAPPER),
            Map.entry("dungeonsalive:enchantsmith", ENCHANTSMITH));

    public static ProfessionGesture forId(String id) { return BY_ID.getOrDefault(id, NONE); }
    public static Map<String, ProfessionGesture> supported() { return BY_ID; }
}
