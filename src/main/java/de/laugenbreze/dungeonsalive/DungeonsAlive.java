package de.laugenbreze.dungeonsalive;

import com.mojang.logging.LogUtils;
import de.laugenbreze.dungeonsalive.ambient.AmbientConfig;
import de.laugenbreze.dungeonsalive.ambient.AmbientParticles;
import de.laugenbreze.dungeonsalive.item.ModItems;
import de.laugenbreze.dungeonsalive.entity.ModEntities;
import de.laugenbreze.dungeonsalive.encounter.EncounterContent;
import de.laugenbreze.dungeonsalive.village.VillageContent;
import de.laugenbreze.dungeonsalive.network.JournalNetworking;
import de.laugenbreze.dungeonsalive.quest.QuestEvents;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import org.slf4j.Logger;

@Mod(DungeonsAlive.MOD_ID)
public final class DungeonsAlive {
    public static final String MOD_ID = "dungeonsalive";
    public static final Logger LOGGER = LogUtils.getLogger();

    public DungeonsAlive(IEventBus modBus, ModContainer container) {
        ModItems.register(modBus);
        de.laugenbreze.dungeonsalive.world.LivingWorldBlocks.register(modBus);
        de.laugenbreze.dungeonsalive.world.LivingBiomeSource.register(modBus);
        de.laugenbreze.dungeonsalive.world.corruption.CorruptionContent.register(modBus);
        de.laugenbreze.dungeonsalive.loot.ExplorationLoot.register(modBus);
        de.laugenbreze.dungeonsalive.item.DungeonsCreativeTab.register(modBus);
        de.laugenbreze.dungeonsalive.item.gear.GearContent.register(modBus);
        ModEntities.register(modBus);
        de.laugenbreze.dungeonsalive.entity.construct.ModConstructs.register(modBus);
        de.laugenbreze.dungeonsalive.entity.peaceful.ModPeacefulEntities.register(modBus);
        de.laugenbreze.dungeonsalive.entity.peaceful.DuckSounds.register(modBus);
        de.laugenbreze.dungeonsalive.entity.peaceful.BirdSounds.register(modBus);
        de.laugenbreze.dungeonsalive.world.keygate.KeyGateContent.register(modBus);
        modBus.addListener(de.laugenbreze.dungeonsalive.entity.peaceful.PiggyBankSpawning::register);
        EncounterContent.register(modBus);
        VillageContent.register(modBus, container);
        de.laugenbreze.dungeonsalive.merchant.MerchantServices.register(modBus);
        de.laugenbreze.dungeonsalive.settlement.inn.InnContent.register(modBus);
        de.laugenbreze.dungeonsalive.settlement.inn.InnFurnitureContent.register(modBus);
        de.laugenbreze.dungeonsalive.settlement.inn.InnEvents.register();
        net.neoforged.neoforge.common.NeoForge.EVENT_BUS.addListener(
                de.laugenbreze.dungeonsalive.merchant.GearUpgradeEffects::incomingDamage);
        de.laugenbreze.dungeonsalive.item.gear.enchantment.GearEnchantmentEvents.register();
        AmbientParticles.register(modBus);
        container.registerConfig(ModConfig.Type.CLIENT, AmbientConfig.SPEC);
        container.registerConfig(ModConfig.Type.COMMON,
                de.laugenbreze.dungeonsalive.entity.peaceful.WildlifeConfig.SPEC, "dungeonsalive-wildlife-common.toml");
        container.registerConfig(ModConfig.Type.COMMON,
                de.laugenbreze.dungeonsalive.entity.peaceful.PiggyBankConfig.SPEC, "dungeonsalive-piggy-bank-common.toml");
        container.registerConfig(ModConfig.Type.COMMON,
                de.laugenbreze.dungeonsalive.entity.NaturalMobConfig.SPEC, "dungeonsalive-hostiles-common.toml");
        container.registerConfig(ModConfig.Type.COMMON,
                de.laugenbreze.dungeonsalive.world.trail.NaturalTrails.SPEC, "dungeonsalive-trails-common.toml");
        de.laugenbreze.dungeonsalive.world.trail.NaturalTrails.register();
        container.registerConfig(ModConfig.Type.COMMON,
                de.laugenbreze.dungeonsalive.world.water.WaterCurrents.SPEC, "dungeonsalive-water-common.toml");
        de.laugenbreze.dungeonsalive.world.water.WaterCurrents.register();
        modBus.addListener(de.laugenbreze.dungeonsalive.world.water.WaterCurrents::networking);
        container.registerConfig(ModConfig.Type.COMMON,
                de.laugenbreze.dungeonsalive.world.caravan.CaravanConfig.SPEC, "dungeonsalive-caravans-common.toml");
        de.laugenbreze.dungeonsalive.world.caravan.CaravanEncounters.register();
        de.laugenbreze.dungeonsalive.world.corruption.CorruptionEvents.register();
        modBus.addListener(JournalNetworking::register);
        modBus.addListener(de.laugenbreze.dungeonsalive.network.MerchantServiceNetworking::register);
        modBus.addListener(de.laugenbreze.dungeonsalive.item.gear.GearHudEvents::networking);
        de.laugenbreze.dungeonsalive.item.gear.GearHudEvents.register();
        de.laugenbreze.dungeonsalive.entity.peaceful.WildlifeEncounters.register();
        QuestEvents.register();
        de.laugenbreze.dungeonsalive.settlement.SettlementEvents.register(ModItems.SETTLEMENT_CHARTER);
    }
}
