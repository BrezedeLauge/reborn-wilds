package de.laugenbreze.dungeonsalive.item.gear;

import de.laugenbreze.dungeonsalive.DungeonsAlive;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ArmorContent {
    public static final List<ArmorItem.Type> PIECES = List.of(ArmorItem.Type.HELMET, ArmorItem.Type.CHESTPLATE, ArmorItem.Type.LEGGINGS, ArmorItem.Type.BOOTS);
    private static final DeferredRegister<ArmorMaterial> MATERIALS = DeferredRegister.create(Registries.ARMOR_MATERIAL, DungeonsAlive.MOD_ID);
    private static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(DungeonsAlive.MOD_ID);
    private static final Map<ArmorSetKind, Map<ArmorItem.Type, DeferredItem<DungeonsArmorItem>>> ARMOR = new EnumMap<>(ArmorSetKind.class);
    static {
        for (ArmorSetKind kind : ArmorSetKind.values()) {
            DeferredHolder<ArmorMaterial, ArmorMaterial> material = MATERIALS.register(kind.id + "_armor", () -> material(kind));
            var pieces = new EnumMap<ArmorItem.Type, DeferredItem<DungeonsArmorItem>>(ArmorItem.Type.class);
            for (var type : PIECES) pieces.put(type, ITEMS.registerItem(kind.id + "_" + type.getName(),
                    props -> new DungeonsArmorItem(kind, material, type, props),
                    new Item.Properties().durability(type.getDurability(kind.durabilityMultiplier)).rarity(kind == ArmorSetKind.SPIDER ? Rarity.RARE : Rarity.UNCOMMON)));
            ARMOR.put(kind, Map.copyOf(pieces));
        }
    }
    private ArmorContent() {}
    public static DungeonsArmorItem armor(ArmorSetKind kind, ArmorItem.Type type) {
        var item = ARMOR.get(kind).get(type);
        if (item == null) throw new IllegalArgumentException("No humanoid armor piece: " + type);
        return item.get();
    }
    public static void register(IEventBus bus) {
        MATERIALS.register(bus); ITEMS.register(bus);
        bus.addListener(ArmorContent::creative);
        ArmorEffects.register();
    }
    private static ArmorMaterial material(ArmorSetKind kind) {
        return new ArmorMaterial(Map.of(ArmorItem.Type.HELMET, kind.helmetDefense, ArmorItem.Type.CHESTPLATE, kind.chestDefense,
                ArmorItem.Type.LEGGINGS, kind.legDefense, ArmorItem.Type.BOOTS, kind.bootDefense, ArmorItem.Type.BODY, 0),
                15, SoundEvents.ARMOR_EQUIP_LEATHER,
                () -> Ingredient.of(switch (kind) { case WOLF -> Items.LEATHER; case SPIDER -> Items.STRING; case BATTLE_ROBES -> Items.WHITE_WOOL; }),
                List.of(new ArmorMaterial.Layer(ResourceLocation.fromNamespaceAndPath(DungeonsAlive.MOD_ID, kind.id))), 0, 0);
    }
    private static void creative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.COMBAT)
            for (var kind : ArmorSetKind.values()) for (var type : PIECES) event.accept(ARMOR.get(kind).get(type));
    }
}
