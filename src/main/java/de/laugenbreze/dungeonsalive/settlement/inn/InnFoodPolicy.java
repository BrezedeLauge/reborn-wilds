package de.laugenbreze.dungeonsalive.settlement.inn;

import java.util.Set;

/** Ordinary meals without potion effects, valuable golden ingredients or container remainders. */
public final class InnFoodPolicy {
    private static final Set<String> FOOD = Set.of("minecraft:bread", "minecraft:apple", "minecraft:carrot",
            "minecraft:baked_potato", "minecraft:beetroot", "minecraft:cooked_beef", "minecraft:cooked_porkchop",
            "minecraft:cooked_mutton", "minecraft:cooked_chicken", "minecraft:cooked_rabbit",
            "minecraft:cooked_cod", "minecraft:cooked_salmon");
    private InnFoodPolicy() { }
    public static boolean accepts(String itemId) { return FOOD.contains(itemId); }
}
