package de.laugenbreze.dungeonsalive.client.entity.reference.builtin;

import de.laugenbreze.dungeonsalive.client.entity.reference.ReferenceMobDefinition;
import de.laugenbreze.dungeonsalive.client.entity.reference.VisualBounds;
import net.minecraft.client.model.IllagerModel;
import net.minecraft.client.model.SlimeModel;

import java.util.LinkedHashMap;
import java.util.Map;

/** Native Minecraft geometry/textures are referenced at runtime; no Mojang assets are copied. */
public final class BuiltinMobModels {
    private BuiltinMobModels() {}

    public static Map<String, ReferenceMobDefinition> definitions() {
        Map<String, ReferenceMobDefinition> result = new LinkedHashMap<>();
        result.put("pillager_raid_captain", captain("pillager_raid_captain", "pillager"));
        result.put("vindicator_raid_captain", captain("vindicator_raid_captain", "vindicator"));
        result.put("skeleton_horseman", ReferenceMobDefinition.builder("skeleton_horseman", SkeletonHorsemanModel::createHorseLayer,
                        "minecraft:textures/entity/horse/horse_skeleton.png")
                .shadow(.8F)
                .hiddenBones("left_hind_baby_leg", "right_hind_baby_leg", "left_front_baby_leg", "right_front_baby_leg",
                        "saddle", "left_saddle_mouth", "right_saddle_mouth", "left_saddle_line", "right_saddle_line", "head_saddle", "mouth_saddle_wrap")
                .build());
        result.put("conjured_slime", ReferenceMobDefinition.builder("conjured_slime", SlimeModel::createInnerBodyLayer,
                        ConjuredSlimeRenderer.TEXTURE.toString()).scale(ConjuredSlimeRenderer.SIZE).shadow(.325F).build());
        return Map.copyOf(result);
    }

    private static ReferenceMobDefinition captain(String id, String kind) {
        var builder = ReferenceMobDefinition.builder(id, IllagerModel::createBodyLayer,
                        "minecraft:textures/entity/illager/" + kind + ".png")
                .shadow(.5F).hiddenBones("hat").cemDriver(kind).handItems("right_arm", "left_arm");
        for (String name : new String[]{"head", "body", "arms", "left_arm", "right_arm", "left_leg", "right_leg"})
            builder.hostAlias(name, name);
        return builder.build();
    }

    /** Rest-pose envelope includes the external banner, rider and held equipment. */
    public static VisualBounds compositeBounds(String id) {
        return switch (id) {
            case "pillager_raid_captain", "vindicator_raid_captain" -> new VisualBounds(1.7F, 3.45F, 1.55F, 1.725F);
            case "skeleton_horseman" -> new VisualBounds(1.65F, 2.85F, 2.45F, 1.425F);
            case "conjured_slime" -> new VisualBounds(.65F, .65F, .65F, .325F);
            default -> null;
        };
    }
}
