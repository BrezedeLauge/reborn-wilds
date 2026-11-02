package de.laugenbreze.dungeonsalive.encounter;

import de.laugenbreze.dungeonsalive.DungeonsAlive;
import de.laugenbreze.dungeonsalive.entity.MobProfile;
import de.laugenbreze.dungeonsalive.entity.MobProfiles;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/** Survival entry points for voluntary encounters. Each seal starts exactly one mob. */
public final class EncounterContent {
    private static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(DungeonsAlive.MOD_ID);
    public static final Map<String, DeferredItem<EncounterSealItem>> SEALS;
    static {
        Map<String, DeferredItem<EncounterSealItem>> seals = new LinkedHashMap<>();
        for (MobProfile profile : MobProfiles.all()) {
            if (profile.tier() == MobProfile.Tier.REGULAR) continue;
            seals.put(profile.id(), ITEMS.register(profile.id() + "_seal", () -> new EncounterSealItem(
                    profile.id(), new Item.Properties().stacksTo(16).rarity(
                    profile.boss() ? Rarity.EPIC : Rarity.RARE))));
        }
        SEALS = Collections.unmodifiableMap(seals);
    }
    private EncounterContent() {}

    public static void register(IEventBus bus) {
        ITEMS.register(bus);
        bus.addListener(EncounterContent::creative);
    }

    private static void creative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
            SEALS.values().forEach(event::accept);
        }
    }
}
