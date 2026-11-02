package de.laugenbreze.dungeonsalive.client.gear;

import de.laugenbreze.dungeonsalive.item.gear.ArmorContent;
import de.laugenbreze.dungeonsalive.item.gear.ArmorSetKind;
import java.util.EnumMap;
import java.util.Map;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;

/** Loaded only from the existing Dist.CLIENT entry point. */
public final class ArmorClient {
    private ArmorClient() {}
    public static void register(IEventBus bus) { bus.addListener(ArmorClient::extensions); }
    private static void extensions(RegisterClientExtensionsEvent event) {
        for (var kind : ArmorSetKind.values()) {
            IClientItemExtensions extension = new IClientItemExtensions() {
                private final Map<EquipmentSlot, ArmorModel> models = new EnumMap<>(EquipmentSlot.class);
                @Override public HumanoidModel<?> getHumanoidArmorModel(LivingEntity wearer, ItemStack stack, EquipmentSlot slot, HumanoidModel<?> original) {
                    var model = models.computeIfAbsent(slot, ignored -> new ArmorModel(kind));
                    model.selectSlot(slot);
                    // NeoForge's default getGenericArmorModel copies the animated native
                    // humanoid poses and visible parent slots after this hook returns.
                    return model;
                }
            };
            for (var type : ArmorContent.PIECES) event.registerItem(extension, ArmorContent.armor(kind, type));
        }
    }
}
