package de.laugenbreze.dungeonsalive.entity;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import static de.laugenbreze.dungeonsalive.entity.AttackKind.*;
import static de.laugenbreze.dungeonsalive.entity.MobProfile.Family.*;
import static de.laugenbreze.dungeonsalive.entity.MobProfile.Tier.*;

/** A deliberate playable first pass. Each name has its own registered entity and attack sequence. */
public final class MobProfiles {
    private static final Map<String, MobProfile> PROFILES = new LinkedHashMap<>();
    static {
        add("royal_guard", "Royal Guard", "Königliche Wache", REGULAR, ILLAGER, 38, 6, .24, 8, .75f, 2.1f, 0x626c78, 0xd6b152, GUARD, MELEE, MELEE);
        add("vanguard", "Vanguard", "Vorhut", REGULAR, SKELETON, 30, 5, .24, 5, .7f, 2.0f, 0xa9aa96, 0x846445, GUARD, MELEE);
        add("enchanter", "Enchanter", "Verzauberer", REGULAR, ILLAGER, 28, 3, .23, 0, .7f, 1.95f, 0x813788, 0xf3c464, ENCHANT, SOUL_BOLT);
        add("necromancer", "Necromancer", "Nekromant", REGULAR, SKELETON, 34, 5, .21, 0, .7f, 2.0f, 0x263c5b, 0x66dfdb, SUMMON, SOUL_BOLT, SOUL_BOLT);
        add("geomancer", "Geomancer", "Geomant", REGULAR, ILLAGER, 32, 6, .20, 0, .7f, 1.95f, 0x946b4a, 0x9ecb78, ROOT_SNARE, FIRE_FIELD);
        add("wraith", "Wraith", "Gespenst", REGULAR, WRAITH, 26, 5, .24, 0, .8f, 1.9f, 0x25496c, 0x72ecf5, FIRE_FIELD, TELEPORT);
        add("iceologer", "Iceologer", "Eisologe", REGULAR, ILLAGER, 30, 6, .23, 0, .7f, 1.95f, 0x7199bd, 0xbde8ff, ICE_FALL, SOUL_BOLT);
        add("wind_caller", "Wind Caller", "Windrufer", REGULAR, ILLAGER, 30, 4, .24, 0, .7f, 1.95f, 0xd4c9b5, 0xa8e9e5, STORM, SOUL_BOLT);
        add("whisperer", "Whisperer", "Flüsterer", REGULAR, PLANT, 34, 5, .19, 0, .8f, 2.0f, 0x42764a, 0xd897c8, ROOT_SNARE, VOLLEY);
        add("wavewhisperer", "Wavewhisperer", "Wellenflüsterer", REGULAR, PLANT, 38, 5, .22, 0, .8f, 2.0f, 0x2e8d83, 0xeca46e, ROOT_SNARE, STORM);
        add("leapleaf", "Leapleaf", "Sprungblatt", REGULAR, PLANT, 44, 7, .25, 3, 1.2f, 2.1f, 0x517531, 0xdcbc56, LEAP, HEAVY_SLAM);
        add("watchling", "Watchling", "Wächterling", REGULAR, ENDERLING, 32, 5, .29, 0, .7f, 2.3f, 0x2b223f, 0xc278e3, TELEPORT, MELEE, MELEE);
        add("blastling", "Blastling", "Schussling", REGULAR, ENDERLING, 34, 5, .23, 0, .8f, 2.2f, 0x3a254b, 0xe4a0f1, VOLLEY, SOUL_BOLT);
        add("snareling", "Snareling", "Fesselling", REGULAR, ENDERLING, 30, 4, .25, 0, .8f, 2.0f, 0x51455e, 0xe9d8ed, ROOT_SNARE, MELEE);
        add("armored_vindicator", "Armored Vindicator", "Gepanzerter Diener", REGULAR, ILLAGER, 38, 7, .24, 7, .75f, 2f, 0x65717a, 0xa63b38, MELEE, HEAVY_SLAM);
        add("armored_pillager", "Armored Pillager", "Gepanzerter Plünderer", REGULAR, ILLAGER, 34, 5, .23, 6, .75f, 2f, 0x576878, 0xa69168, VOLLEY, MELEE);
        add("redstone_cube", "Redstone Cube", "Redstone-Würfel", REGULAR, SLIME, 12, 3, .29, 0, .65f, .7f, 0x604747, 0xef4a32, LEAP, MELEE);
        add("conjured_slime", "Conjured Slime", "Beschworener Schleim", REGULAR, SLIME, 12, 3, .27, 0, .65f, .7f, 0x67527b, 0xe4a3ef, LEAP, MELEE);
        add("tropical_slime", "Tropical Slime", "Tropenschleim", REGULAR, SLIME, 30, 5, .22, 0, 1.3f, 1.3f, 0x78bce4, 0xf7bc58, LEAP, MELEE);
        add("diamond_armored_pillager", "Diamond Armored Pillager", "Diamantgepanzerter Plünderer", REGULAR, ILLAGER, 48, 7, .22, 10, .9f, 2.7f, 0x445765, 0x80d8e5, VOLLEY, MELEE);
        add("diamond_armored_vindicator", "Diamond Armored Vindicator", "Diamantgepanzerter Diener", REGULAR, ILLAGER, 54, 9, .24, 10, .9f, 2.7f, 0x48545e, 0x88cbdc, MELEE, HEAVY_SLAM);

        add("squall_golem", "Squall Golem", "Böengolem", REGULAR, GOLEM, 80, 9, .23, 6, 1.4f, 3.0f, 0x8a8876, 0x76c9c1, HEAVY_SLAM, MELEE, STORM);
        add("mountaineer", "Mountaineer", "Bergsteiger", REGULAR, ILLAGER, 30, 6, .25, 2, .7f, 2.0f, 0x85684f, 0xd1b690, MELEE, HEAVY_SLAM);
        add("vindicator_chef", "Vindicator Chef", "Diener-Koch", REGULAR, ILLAGER, 28, 6, .25, 1, .7f, 2.1f, 0xe8e4da, 0x815144, MELEE, MELEE);
        add("sunken_skeleton", "Sunken Skeleton", "Versunkenes Skelett", REGULAR, SKELETON, 24, 5, .23, 2, .65f, 2.0f, 0xa1b8ae, 0xce8e72, VOLLEY, MELEE);
        add("quick_growing_vine", "Quick Growing Vine", "Schnellwachsende Ranke", REGULAR, PLANT, 14, 4, 0, 0, .8f, 1.5f, 0x48673b, 0xbb86ae, ROOT_SNARE);
        add("quick_growing_anemone", "Quick Growing Anemone", "Schnellwachsende Anemone", REGULAR, PLANT, 14, 4, 0, 0, .8f, 1.5f, 0x4d8790, 0xda9c83, ROOT_SNARE);
        add("poison_quill_vine", "Poison Quill Vine", "Giftstachelranke", REGULAR, PLANT, 18, 5, 0, 0, .8f, 1.4f, 0x567543, 0xc57ab0, VOLLEY, ROOT_SNARE);
        add("poison_anemone", "Poison Anemone", "Giftanemone", REGULAR, PLANT, 18, 5, 0, 0, .8f, 1.4f, 0x4a929b, 0xcf8a8b, VOLLEY, ROOT_SNARE);
        add("flame_construct", "Flame Construct", "Flammenkonstrukt", REGULAR, GOLEM, 54, 7, .22, 5, 1.0f, 2.2f, 0x676b75, 0xf3a846, GUARD, FIRE_FIELD, VOLLEY);
        add("tower_guard", "Tower Guard", "Turmwache", REGULAR, ILLAGER, 42, 7, .24, 7, .75f, 2.05f, 0x5a626f, 0xa48aad, GUARD, MELEE);
        add("rampart_captain", "Rampart Captain", "Bollwerk-Hauptmann", MINIBOSS, ILLAGER, 100, 9, .25, 8, .85f, 2.2f, 0x687387, 0xb1a0bf, GUARD, HEAVY_SLAM, MELEE);
        add("tower_wraith", "Tower Wraith", "Turmgespenst", REGULAR, WRAITH, 38, 6, .24, 2, .8f, 2.2f, 0x51486a, 0xba9bde, FIRE_FIELD, TELEPORT);
        add("armored_mountaineer", "Armored Mountaineer", "Gepanzerter Bergsteiger", REGULAR, ILLAGER, 44, 7, .23, 8, .8f, 2.05f, 0x747a83, 0xc4b3a3, MELEE, HEAVY_SLAM);
        add("armored_mossy_skeleton", "Armored Mossy Skeleton", "Gepanzertes Moosskelett", REGULAR, SKELETON, 32, 6, .23, 6, .7f, 2.0f, 0x71876a, 0x9aa4ac, VOLLEY, MELEE);
        add("armored_frozen_zombie", "Armored Frozen Zombie", "Gepanzerter Frostzombie", REGULAR, ZOMBIE, 38, 6, .21, 6, .75f, 2.0f, 0x7399ad, 0x929ea6, MELEE, MELEE);
        add("armored_jungle_zombie", "Armored Jungle Zombie", "Gepanzerter Dschungelzombie", REGULAR, ZOMBIE, 38, 6, .21, 6, .75f, 2.0f, 0x648353, 0x9b9d89, MELEE, MELEE);

        add("frozen_zombie", "Frozen Zombie", "Frostzombie", REGULAR, ZOMBIE, 26, 5, 0.22, 0, 0.65f, 1.95f, 0x779ba9, 0xcbdde2, MELEE);
        add("jungle_zombie", "Jungle Zombie", "Dschungelzombie", REGULAR, ZOMBIE, 26, 5, 0.22, 0, 0.65f, 1.95f, 0x527b40, 0xa8b961, MELEE);
        add("mossy_skeleton", "Mossy Skeleton", "Moosskelett", REGULAR, SKELETON, 26, 5, 0.22, 0, 0.65f, 1.95f, 0x8b9273, 0x547c3e, VOLLEY);
        add("icy_creeper", "Icy Creeper", "Eiscreeper", REGULAR, CREEPER, 20, 8, 0.22, 0, 0.6f, 1.7f, 0x96c1da, 0xe5f6fa, ICY_EXPLOSION);
        add("piglin_hunter", "Piglin Hunter", "Piglin-Jäger", REGULAR, PIGLIN, 24, 6, 0.24, 0, 0.7f, 1.95f, 0xb88473, 0x6f4d39, MELEE, VOLLEY);
        add("zombified_piglin_hunter", "Zombified Piglin Hunter", "Zombifizierter Piglin-Jäger", REGULAR, PIGLIN, 24, 6, 0.24, 0, 0.7f, 1.95f, 0x849063, 0x86615a, MELEE, VOLLEY);
        add("armored_piglin", "Gold Armored Piglin", "Goldgepanzerter Piglin", REGULAR, PIGLIN, 30, 6, 0.24, 7, 0.7f, 1.95f, 0xcbac42, 0x86614e, MELEE);
        add("netherite_armored_piglin", "Netherite Armored Piglin", "Netheritgepanzerter Piglin", REGULAR, PIGLIN, 30, 6, 0.24, 7, 0.7f, 1.95f, 0x51444a, 0x906f62, MELEE);
        add("armored_piglin_hunter", "Gold Armored Piglin Hunter", "Goldgepanzerter Piglin-Jäger", REGULAR, PIGLIN, 30, 6, 0.24, 7, 0.7f, 1.95f, 0xc3a347, 0x72533c, MELEE, VOLLEY);
        add("netherite_armored_piglin_hunter", "Netherite Armored Piglin Hunter", "Netheritgepanzerter Piglin-Jäger", REGULAR, PIGLIN, 30, 6, 0.24, 7, 0.7f, 1.95f, 0x54464f, 0x9b7969, MELEE, VOLLEY);
        add("fungus_thrower", "Fungus Thrower", "Pilzwerfer", REGULAR, PIGLIN, 24, 6, 0.24, 0, 0.7f, 1.95f, 0xab785f, 0x4d9a91, MELEE, VOLLEY);
        add("zombified_fungus_thrower", "Zombified Fungus Thrower", "Zombifizierter Pilzwerfer", REGULAR, PIGLIN, 24, 6, 0.24, 0, 0.7f, 1.95f, 0x79916c, 0x4d9a91, MELEE, VOLLEY);
        add("armored_drowned", "Seaweed Armored Drowned", "Seegrasgepanzerter Ertrunkener", REGULAR, ZOMBIE, 26, 5, 0.22, 7, 0.65f, 1.95f, 0x466d60, 0x78846a, MELEE);
        add("armored_trident_drowned", "Armored Trident Drowned", "Gepanzerter Dreizack-Ertrunkener", REGULAR, ZOMBIE, 30, 6, .22, 7, .65f, 1.95f, 0x466d60, 0x7cadab, SOUL_BOLT, MELEE);
        add("pale_armored_drowned", "Pale Armored Drowned", "Blasser gepanzerter Ertrunkener", REGULAR, ZOMBIE, 26, 5, 0.22, 7, 0.65f, 1.95f, 0x93a59a, 0x5c8989, MELEE);
        add("armored_sunken_skeleton", "Red Coral Armored Sunken Skeleton", "Rotkorallen-Skelett", REGULAR, SKELETON, 26, 5, 0.22, 7, 0.65f, 1.95f, 0xa4aba1, 0xb94e46, VOLLEY);
        add("yellow_coral_armored_sunken_skeleton", "Yellow Coral Armored Sunken Skeleton", "Gelbkorallen-Skelett", REGULAR, SKELETON, 26, 5, 0.22, 7, 0.65f, 1.95f, 0xa7afa3, 0xd3ac4d, VOLLEY);

        add("armored_skeleton", "Armored Skeleton", "Gepanzertes Skelett", REGULAR, SKELETON, 36, 6, .22, 7, .7f, 2.0f, 0xa4a79c, 0x747e8c, VOLLEY, MELEE);
        add("armored_stray", "Armored Stray", "Gepanzerter Eiswanderer", REGULAR, SKELETON, 36, 6, .22, 7, .7f, 2.0f, 0x98b4bb, 0x7e8a9b, VOLLEY, MELEE);
        add("armored_zombie", "Armored Zombie", "Gepanzerter Zombie", REGULAR, ZOMBIE, 36, 6, .22, 7, .7f, 2.0f, 0x729153, 0x7b8390, MELEE, MELEE);
        add("armored_husk", "Armored Husk", "Gepanzerter Wüstenzombie", REGULAR, ZOMBIE, 36, 6, .22, 7, .7f, 2.0f, 0x9a9065, 0x7c8390, MELEE, MELEE);

        add("redstone_golem", "Redstone Golem", "Redstone-Golem", MINIBOSS, GOLEM, 140, 11, .22, 8, 1.8f, 3.0f, 0x796b67, 0xed4838, HEAVY_SLAM, FIRE_FIELD, MELEE);
        add("endersent", "Endersent", "Endersent", MINIBOSS, ENDERLING, 120, 10, .24, 5, 1.4f, 3.2f, 0x252235, 0xe6afef, TELEPORT, HEAVY_SLAM, MELEE);
        add("drowned_necromancer", "Drowned Necromancer", "Ertrunkener Nekromant", MINIBOSS, SKELETON, 100, 7, .22, 3, .9f, 2.2f, 0x376c66, 0x8fe7ce, SUMMON, BEAM, SOUL_BOLT);
        add("wildfire", "Wildfire", "Lauffeuer", MINIBOSS, WILDFIRE, 120, 8, .23, 4, 1.2f, 2.3f, 0x6d3c22, 0xffcc5b, GUARD, VOLLEY, FIRE_FIELD);
        add("illusioner", "Illusioner", "Illusionist", MINIBOSS, ILLAGER, 100, 7, .28, 0, .75f, 2.0f, 0x4d529c, 0xb3d9f5, TELEPORT, VOLLEY, SOUL_BOLT);
        add("pillager_raid_captain", "Pillager Raid Captain", "Plünderer-Hauptmann", MINIBOSS, ILLAGER, 90, 7, .25, 6, .8f, 2.1f, 0x554b4b, 0xd5bd81, VOLLEY, ENCHANT, MELEE);
        add("vindicator_raid_captain", "Vindicator Raid Captain", "Diener-Hauptmann", MINIBOSS, ILLAGER, 100, 9, .27, 7, .8f, 2.1f, 0x4c6370, 0xe1bd73, GUARD, HEAVY_SLAM, MELEE);
        add("skeleton_horseman", "Skeleton Horseman", "Skelettreiter", MINIBOSS, HORSEMAN, 80, 6, .33, 2, 1.2f, 2.8f, 0xbbbba9, 0x706c66, VOLLEY, LEAP, MELEE);

        add("arch_illager", "Arch-Illager", "Erz-Illager", BOSS, ILLAGER, 180, 8, .25, 2, .8f, 1.65f, 0x9b3941, 0xf1c566, SUMMON, VOLLEY, TELEPORT, SOUL_BOLT);
        add("corrupted_cauldron", "Corrupted Cauldron", "Verderbter Kessel", BOSS, CAULDRON, 240, 9, 0, 8, 2.6f, 2.5f, 0x3d3b40, 0xe18cce, SUMMON, FIRE_FIELD, VOLLEY);
        add("heart_of_ender", "Heart of Ender", "Herz des Enders", BOSS, ENDERLING, 260, 11, .21, 4, 2.2f, 3.5f, 0x21182a, 0xd97be9, BEAM, FIRE_FIELD, VOLLEY, TELEPORT);
        add("mooshroom_monstrosity", "Mooshroom Monstrosity", "Mooshroom-Monstrosität", BOSS, GOLEM, 300, 13, .18, 10, 2.5f, 3.6f, 0x665451, 0xe3c7a2, HEAVY_SLAM, VOLLEY, STORM);
        add("nameless_one", "The Nameless One", "Der Namenlose", BOSS, SKELETON, 220, 9, .22, 2, 1.0f, 2.5f, 0x4d5b40, 0x8ee7a0, SUMMON, SOUL_BOLT, VOLLEY, TELEPORT);
        add("redstone_monstrosity", "Redstone Monstrosity", "Redstone-Monstrosität", BOSS, GOLEM, 320, 14, .18, 12, 2.8f, 3.8f, 0x786a61, 0xf34e28, HEAVY_SLAM, SUMMON, FIRE_FIELD);
        add("jungle_abomination", "Jungle Abomination", "Dschungelungeheuer", BOSS, PLANT, 280, 12, .18, 8, 2.5f, 3.8f, 0x3f6338, 0xd99963, HEAVY_SLAM, ROOT_SNARE, SUMMON);
        add("treetop_whisperer", "Treetop Whisperer", "Baumkronenflüsterer", BOSS, PLANT, 220, 9, .19, 4, 2.0f, 3.5f, 0x426d49, 0xe0a2ca, ROOT_SNARE, VOLLEY, SUMMON);
        add("wretched_wraith", "Wretched Wraith", "Elendes Gespenst", BOSS, WRAITH, 220, 9, .26, 2, 1.8f, 3f, 0x507fa3, 0xc0edff, ICE_FALL, VOLLEY, TELEPORT);
        add("tempest_golem", "Tempest Golem", "Sturmgolem", BOSS, GOLEM, 300, 12, .20, 10, 2.4f, 3.6f, 0x968f77, 0x86e1ed, GUARD, STORM, HEAVY_SLAM, BEAM);
        add("ancient_guardian", "Ancient Guardian", "Uralter Wächter", BOSS, GUARDIAN, 260, 10, .23, 8, 2.6f, 2.5f, 0x7b938a, 0xe4bb6a, BEAM, STORM, SUMMON);
        add("vengeful_heart_of_ender", "Vengeful Heart of Ender", "Rachsüchtiges Herz des Enders", BOSS, ENDERLING, 360, 13, .22, 6, 2.8f, 3.8f, 0x2e1f41, 0xec94f6, BEAM, VOLLEY, HEAVY_SLAM, TELEPORT, FIRE_FIELD);
    }
    private MobProfiles() {}
    private static void add(String id, String en, String de, MobProfile.Tier tier, MobProfile.Family family,
                            double hp, double damage, double speed, double armor, float width, float height,
                            int color, int accent, AttackKind... attacks) {
        var profile = new MobProfile(id, en, de, tier, family, hp, damage, speed, armor, width, height, color, accent, List.of(attacks));
        if (PROFILES.putIfAbsent(id, profile) != null) throw new IllegalArgumentException("Duplicate mob: " + id);
    }
    public static List<MobProfile> all() { return List.copyOf(PROFILES.values()); }
    public static MobProfile get(String id) {
        MobProfile result = PROFILES.get(id);
        if (result == null) throw new IllegalArgumentException("Unknown mob profile: " + id);
        return result;
    }
}
