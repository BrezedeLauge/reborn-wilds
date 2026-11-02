package de.laugenbreze.dungeonsalive.item.gear;

import de.laugenbreze.dungeonsalive.DungeonsAlive;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.EnumMap;
import java.util.Map;

public final class GearContent {
    private static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(DungeonsAlive.MOD_ID);
    private static final EnumMap<ArtifactKind, DeferredItem<DungeonsArtifactItem>> ARTIFACTS = new EnumMap<>(ArtifactKind.class);
    private static final EnumMap<WeaponKind, DeferredItem<DungeonsWeaponItem>> WEAPONS = new EnumMap<>(WeaponKind.class);
    private static final EnumMap<RangedWeaponKind, DeferredItem<? extends Item>> RANGED = new EnumMap<>(RangedWeaponKind.class);
    static {
        for (ArtifactKind kind : ArtifactKind.values()) ARTIFACTS.put(kind,
                ITEMS.registerItem(kind.id, props -> new DungeonsArtifactItem(kind, props), new Item.Properties().stacksTo(1).rarity(Rarity.RARE)));
        for (WeaponKind kind : WeaponKind.values()) WEAPONS.put(kind,
                ITEMS.registerItem(kind.id, props -> new DungeonsWeaponItem(kind, props), new Item.Properties().rarity(Rarity.RARE)));
        for (RangedWeaponKind kind : RangedWeaponKind.values()) RANGED.put(kind,
                ITEMS.registerItem(kind.id, props -> kind == RangedWeaponKind.AUTO_CROSSBOW || kind == RangedWeaponKind.EXPLODING_CROSSBOW
                        ? new DungeonsCrossbowItem(kind, props) : new DungeonsBowItem(kind, props),
                        new Item.Properties().durability(kind.durability).rarity(Rarity.RARE)));
    }
    private GearContent() {}
    public static DungeonsArtifactItem artifact(ArtifactKind kind) { return ARTIFACTS.get(kind).get(); }
    public static DungeonsWeaponItem weapon(WeaponKind kind) { return WEAPONS.get(kind).get(); }
    public static Map<ArtifactKind, DeferredItem<DungeonsArtifactItem>> artifacts() { return Map.copyOf(ARTIFACTS); }
    public static Map<WeaponKind, DeferredItem<DungeonsWeaponItem>> weapons() { return Map.copyOf(WEAPONS); }
    public static Item ranged(RangedWeaponKind kind) { return RANGED.get(kind).get(); }
    public static void register(IEventBus bus) {
        ITEMS.register(bus);
        ArmorContent.register(bus);
        RangedWeaponEvents.register();
        ArtifactExpansionEvents.register();
        bus.addListener(GearContent::creative);
        NeoForge.EVENT_BUS.addListener(GearEvents::incomingDamage);
        NeoForge.EVENT_BUS.addListener(net.neoforged.bus.api.EventPriority.LOWEST, GearEvents::death);
        NeoForge.EVENT_BUS.addListener(GearEvents::clonePlayer);
    }
    private static void creative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.COMBAT) {
            ARTIFACTS.values().forEach(event::accept);
            WEAPONS.values().forEach(event::accept);
            RANGED.values().forEach(event::accept);
        }
        if (event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) ARTIFACTS.values().forEach(event::accept);
    }
}
