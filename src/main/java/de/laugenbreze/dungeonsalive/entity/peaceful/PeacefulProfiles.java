package de.laugenbreze.dungeonsalive.entity.peaceful;

import java.util.*;

/** Explicit peaceful entities; visual source coverage is audited separately. */
public final class PeacefulProfiles {
    private static final Set<String> NATURAL_COMPANION_TYPES = Set.of(
            "pet_toucan", "pet_raven", "pet_red_panda", "pet_golden_parrot", "pet_hedgehog", "pet_racoon",
            "pet_royal_penguin", "pet_cluckshroom", "pet_dark_baby_goat", "pet_baby_ghast",
            "pet_zombified_baby_pig", "pet_baby_squid", "pet_ruby_baby_turtle", "pet_mole", "pet_duck");
    public enum Kind { PET, WILDLIFE, KEY_GOLEM, MERCHANT, CIVILIAN }
    public record Profile(String id,String nameEn,String nameDe,Kind kind,String host,float width,float height,double speed,int color,int accent) {
        public boolean flying(){return Set.of("allay","parrot","phantom").contains(host);}
        public boolean aquatic(){return host.equals("glow_squid");}
        public boolean amphibious(){return host.equals("turtle");}
        /** Eligibility alone never turns a saved/manual companion into wildlife. */
        public boolean wildlifeEligible(){return kind==Kind.WILDLIFE || kind==Kind.PET && NATURAL_COMPANION_TYPES.contains(id);}
        public boolean wildBirdEligible(){return kind==Kind.PET && Set.of("pet_toucan","pet_raven","pet_golden_parrot").contains(id);}
        public boolean nocturnalWildlife(){return Set.of("wisp","pet_racoon","pet_hedgehog","pet_mole").contains(id);}
    }
    private static final Map<String,Profile> VALUES = new LinkedHashMap<>();
    static {
        add("key_golem", "Key Golem", "Schlüsselgolem", Kind.KEY_GOLEM, "chicken", 0.55F, 0.9F, 0.24, 0x7d7f77, 0xcda86c);
        add("diamond_key_golem", "Diamond Key Golem", "Diamantschlüsselgolem", Kind.KEY_GOLEM, "chicken", 0.55F, 0.9F, 0.24, 0x7d7f77, 0xcda86c);
        add("wooly_cow", "Wooly Cow", "Wollkuh", Kind.WILDLIFE, "cow", 1.1F, 1.4F, 0.24, 0x7d7f77, 0xcda86c);
        add("piggy_bank", "Piggy Bank", "Sparschwein", Kind.WILDLIFE, "pig", 0.95F, 1.0F, 0.24, 0x7d7f77, 0xcda86c);
        add("vulture", "Vulture", "Geier", Kind.WILDLIFE, "phantom", 1.3F, 0.6F, 0.24, 0x7d7f77, 0xcda86c);
        add("wisp", "Wisp", "Irrlicht", Kind.WILDLIFE, "allay", 0.5F, 0.7F, 0.24, 0x7d7f77, 0xcda86c);
        add("pet_baby_dairy_cow", "Baby Dairy Cow", "Kleine Milchkuh", Kind.PET, "cow", 0.7F, 0.8F, 0.24, 0x7d7f77, 0xcda86c);
        add("pet_baby_ghast", "Baby Ghast", "Kleiner Ghast", Kind.PET, "allay", 0.5F, 0.7F, 0.24, 0x7d7f77, 0xcda86c);
        add("pet_baby_moobloom", "Baby Moobloom", "Kleine Blumenkuh", Kind.PET, "cow", 0.7F, 0.8F, 0.24, 0x7d7f77, 0xcda86c);
        add("pet_baby_piebald_pig", "Baby Piebald Pig", "Kleines geschecktes Schwein", Kind.PET, "pig", 0.6F, 0.65F, 0.24, 0x7d7f77, 0xcda86c);
        add("pet_baby_sooty_pig", "Baby Sooty Pig", "Kleines rußiges Schwein", Kind.PET, "pig", 0.6F, 0.65F, 0.24, 0x7d7f77, 0xcda86c);
        add("pet_baby_squid", "Baby Squid", "Kleiner Tintenfisch", Kind.PET, "glow_squid", 0.6F, 0.6F, 0.24, 0x7d7f77, 0xcda86c);
        add("pet_baby_wooly_cow", "Baby Wooly Cow", "Kleine Wollkuh", Kind.PET, "cow", 0.7F, 0.8F, 0.24, 0x7d7f77, 0xcda86c);
        add("pet_cluckshroom", "Cluckshroom", "Pilzhuhn", Kind.PET, "chicken", 0.5F, 0.65F, 0.24, 0x7d7f77, 0xcda86c);
        add("pet_duck", "Duck", "Ente", Kind.PET, "chicken", 0.5F, 0.65F, 0.24, 0x7d7f77, 0xcda86c);
        add("pet_fancy_chicken", "Fancy Chicken", "Schmuckhuhn", Kind.PET, "chicken", 0.5F, 0.65F, 0.24, 0x7d7f77, 0xcda86c);
        add("pet_glowing_mini_abomination", "Glowing Mini Abomination", "Leuchtendes Mini-Dschungelungeheuer", Kind.PET, "chicken", 0.5F, 0.65F, 0.24, 0x7d7f77, 0xcda86c);
        add("pet_glowing_ministrosity", "Glowing Ministrosity", "Leuchtende Ministrosität", Kind.PET, "chicken", 0.5F, 0.65F, 0.24, 0x7d7f77, 0xcda86c);
        add("pet_golden_parrot", "Golden Parrot", "Goldpapagei", Kind.PET, "parrot", 0.4F, 0.65F, 0.24, 0x7d7f77, 0xcda86c);
        add("pet_hedgehog", "Hedgehog", "Igel", Kind.PET, "rabbit", 0.45F, 0.5F, 0.24, 0x7d7f77, 0xcda86c);
        add("pet_mini_abomination", "Mini Abomination", "Mini-Dschungelungeheuer", Kind.PET, "chicken", 0.5F, 0.65F, 0.24, 0x7d7f77, 0xcda86c);
        add("pet_mole", "Mole", "Maulwurf", Kind.PET, "silverfish", 0.5F, 0.4F, 0.24, 0x7d7f77, 0xcda86c);
        add("pet_racoon", "Racoon", "Waschbär", Kind.PET, "fox", 0.65F, 0.55F, 0.24, 0x7d7f77, 0xcda86c);
        add("pet_raven", "Raven", "Rabe", Kind.PET, "parrot", 0.4F, 0.65F, 0.24, 0x7d7f77, 0xcda86c);
        add("pet_royal_penguin", "Royal Penguin", "Königspinguin", Kind.PET, "chicken", 0.5F, 0.65F, 0.24, 0x7d7f77, 0xcda86c);
        add("pet_red_panda", "Red Panda", "Roter Panda", Kind.PET, "fox", 0.65F, 0.55F, 0.24, 0x7d7f77, 0xcda86c);
        add("pet_redstone_ministrosity", "Redstone Ministrosity", "Redstone-Ministrosität", Kind.PET, "chicken", 0.5F, 0.65F, 0.24, 0x7d7f77, 0xcda86c);
        add("pet_skunk", "Skunk", "Stinktier", Kind.PET, "fox", 0.65F, 0.55F, 0.24, 0x7d7f77, 0xcda86c);
        add("pet_stone_ministrosity", "Stone Ministrosity", "Stein-Ministrosität", Kind.PET, "chicken", 0.5F, 0.65F, 0.24, 0x7d7f77, 0xcda86c);
        add("pet_toucan", "Toucan", "Tukan", Kind.PET, "parrot", 0.4F, 0.65F, 0.24, 0x7d7f77, 0xcda86c);
        add("pet_vested_rabbit", "Vested Rabbit", "Kaninchen mit Weste", Kind.PET, "rabbit", 0.45F, 0.5F, 0.24, 0x7d7f77, 0xcda86c);
        add("pet_zombified_baby_pig", "Zombified Baby Pig", "Kleines Zombieschwein", Kind.PET, "pig", 0.6F, 0.65F, 0.24, 0x7d7f77, 0xcda86c);
        add("speedy_blue_sheep", "Speedy Blue Sheep", "Flinkes blaues Schaf", Kind.PET, "sheep", 0.9F, 1.3F, 0.24, 0x4775b6, 0x90b4ed);
        add("fire_red_sheep", "Fire Red Sheep", "Feuerrotes Schaf", Kind.PET, "sheep", 0.9F, 1.3F, 0.24, 0xb64d36, 0xefc065);
        add("poisonous_green_sheep", "Poisonous Green Sheep", "Giftgrünes Schaf", Kind.PET, "sheep", 0.9F, 1.3F, 0.24, 0x699a43, 0xc5e57a);
        add("pet_dark_baby_goat", "Dark Baby Goat", "Dunkles Zicklein", Kind.PET, "goat", 0.5F, 0.65F, 0.24, 0x54463f, 0xaaa18d);
        add("pet_grey_tabby", "Grey Tabby", "Graue Tigerkatze", Kind.PET, "cat", 0.5F, 0.5F, 0.24, 0x8e8c88, 0xc6bfb6);
        add("pet_spotted_baby_pig", "Spotted Baby Pig", "Geflecktes Ferkel", Kind.PET, "pig", 0.55F, 0.6F, 0.24, 0xca998b, 0x7f6857);
        add("pet_ruby_baby_turtle", "Ruby Baby Turtle", "Rubinschildkröte", Kind.PET, "turtle", 0.6F, 0.35F, 0.24, 0xb25158, 0xa6c976);
        add("blacksmith", "Blacksmith", "Schmied", Kind.MERCHANT, "wandering_trader", 0.6F, 1.95F, 0.24, 0x7d7f77, 0xcda86c);
        add("captured_blacksmith", "Captured Blacksmith", "Gefangener Schmied", Kind.CIVILIAN, "wandering_trader", 0.6F, 1.95F, 0.24, 0x736a62, 0xbda18c);
        add("gift_wrapper", "Gift Wrapper", "Geschenkeverpacker", Kind.MERCHANT, "wandering_trader", 0.6F, 1.95F, 0.24, 0x7d7f77, 0xcda86c);
        add("mystery_merchant", "Mystery Merchant", "Mysteriöser Händler", Kind.MERCHANT, "wandering_trader", 0.6F, 1.95F, 0.24, 0x7d7f77, 0xcda86c);
        add("village_merchant", "Village Merchant", "Dorfhändler", Kind.MERCHANT, "wandering_trader", 0.6F, 1.95F, 0.24, 0x7d7f77, 0xcda86c);
        add("luxury_merchant", "Luxury Merchant", "Luxushändler", Kind.MERCHANT, "wandering_trader", 0.6F, 1.95F, 0.24, 0x7d7f77, 0xcda86c);
        add("piglin_merchant", "Piglin Merchant", "Piglin-Händler", Kind.MERCHANT, "wandering_trader", 0.6F, 1.95F, 0.24, 0x7d7f77, 0xcda86c);
        add("enchantsmith", "Enchantsmith", "Verzauberungsschmied", Kind.MERCHANT, "wandering_trader", 0.6F, 1.95F, 0.24, 0x7d7f77, 0xcda86c);
        add("tower_keeper", "Tower Keeper", "Turmwächter", Kind.MERCHANT, "wandering_trader", 0.6F, 1.95F, 0.24, 0x7d7f77, 0xcda86c);
        add("powersmith", "Powersmith", "Kraftschmied", Kind.MERCHANT, "wandering_trader", 0.6F, 1.95F, 0.24, 0x7d7f77, 0xcda86c);
        add("uniquesmith", "Uniquesmith", "Einzigartigkeitsschmied", Kind.MERCHANT, "wandering_trader", 0.6F, 1.95F, 0.24, 0x7d7f77, 0xcda86c);
        add("gildsmith", "Gildsmith", "Vergoldungsschmied", Kind.MERCHANT, "wandering_trader", 0.6F, 1.95F, 0.24, 0x7d7f77, 0xcda86c);
        add("summoned_guardian_vex", "Summoned Guardian Vex", "Wächtergeist", Kind.PET, "allay", 0.5F, 0.7F, 0.24, 0x7d7f77, 0xcda86c);
        add("summoned_soul_wizard", "Summoned Soul Wizard", "Seelenmagier", Kind.PET, "allay", 0.5F, 0.7F, 0.24, 0x7d7f77, 0xcda86c);
    }
    private static void add(String id,String en,String de,Kind kind,String host,float width,float height,double speed,int color,int accent){if(VALUES.putIfAbsent(id,new Profile(id,en,de,kind,host,width,height,speed,color,accent))!=null)throw new IllegalArgumentException("Duplicate peaceful entity "+id);}
    public static List<Profile> all(){return List.copyOf(VALUES.values());}
    public static Profile get(String id){var p=VALUES.get(id);if(p==null)throw new IllegalArgumentException("Unknown peaceful entity "+id);return p;}
    private PeacefulProfiles(){}
}
