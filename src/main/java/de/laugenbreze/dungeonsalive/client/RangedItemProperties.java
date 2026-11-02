package de.laugenbreze.dungeonsalive.client;

import de.laugenbreze.dungeonsalive.item.gear.DungeonsCrossbowItem;
import de.laugenbreze.dungeonsalive.item.gear.GearContent;
import de.laugenbreze.dungeonsalive.item.gear.RangedWeaponKind;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ChargedProjectiles;

/** Called only from client setup's enqueued work; common registration never loads this class. */
public final class RangedItemProperties {
    private RangedItemProperties() {}

    public static void register() {
        for (RangedWeaponKind kind : RangedWeaponKind.values()) {
            var item = GearContent.ranged(kind);
            ItemProperties.register(item, ResourceLocation.withDefaultNamespace("pull"), (stack, level, user, seed) -> {
                if (user == null || user.getUseItem() != stack || kind.crossbow && CrossbowItem.isCharged(stack)) return 0;
                float duration = kind.crossbow ? ((DungeonsCrossbowItem) item).chargeDuration(stack, user) : 20;
                return Math.min(1, (stack.getUseDuration(user) - user.getUseItemRemainingTicks()) / duration);
            });
            ItemProperties.register(item, ResourceLocation.withDefaultNamespace("pulling"), (stack, level, user, seed) ->
                    user != null && user.isUsingItem() && user.getUseItem() == stack
                            && (!kind.crossbow || !CrossbowItem.isCharged(stack)) ? 1 : 0);
            if (kind.crossbow) {
                ItemProperties.register(item, ResourceLocation.withDefaultNamespace("charged"), (stack, level, user, seed) -> CrossbowItem.isCharged(stack) ? 1 : 0);
                ItemProperties.register(item, ResourceLocation.withDefaultNamespace("firework"), (stack, level, user, seed) ->
                        stack.getOrDefault(DataComponents.CHARGED_PROJECTILES, ChargedProjectiles.EMPTY).contains(Items.FIREWORK_ROCKET) ? 1 : 0);
            }
        }
    }
}
