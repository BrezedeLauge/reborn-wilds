package de.laugenbreze.dungeonsalive.village;

import com.google.common.collect.ImmutableSet;
import de.laugenbreze.dungeonsalive.DungeonsAlive;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;

/** Six real professions: normal villager job acquisition, trades, restocking and persistence. */
public final class VillageContent {
    private static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(DungeonsAlive.MOD_ID);
    private static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(DungeonsAlive.MOD_ID);
    private static final DeferredRegister<PoiType> POIS = DeferredRegister.create(Registries.POINT_OF_INTEREST_TYPE, DungeonsAlive.MOD_ID);
    private static final DeferredRegister<VillagerProfession> PROFESSIONS = DeferredRegister.create(Registries.VILLAGER_PROFESSION, DungeonsAlive.MOD_ID);

    public record MerchantRole(String id, String workstationId, DeferredBlock<MerchantWorkstationBlock> workstation,
                               DeferredItem<BlockItem> item, DeferredHolder<PoiType, PoiType> poi,
                               DeferredHolder<VillagerProfession, VillagerProfession> profession) {}

    public static final MerchantRole VILLAGE_MERCHANT = role("village_merchant", "merchant_counter", SoundEvents.VILLAGER_WORK_CARTOGRAPHER);
    public static final MerchantRole LUXURY_MERCHANT = role("luxury_merchant", "luxury_counter", SoundEvents.VILLAGER_WORK_LEATHERWORKER);
    public static final MerchantRole MYSTERY_MERCHANT = role("mystery_merchant", "mystery_counter", SoundEvents.VILLAGER_WORK_LIBRARIAN);
    public static final MerchantRole BLACKSMITH = role("blacksmith", "blacksmith_station", SoundEvents.VILLAGER_WORK_WEAPONSMITH);
    public static final MerchantRole GIFT_WRAPPER = role("gift_wrapper", "gift_wrapping_table", SoundEvents.VILLAGER_WORK_FLETCHER);
    public static final MerchantRole ENCHANTSMITH = role("enchantsmith", "enchantsmith_table", SoundEvents.VILLAGER_WORK_CLERIC);
    public static final List<MerchantRole> ROLES = List.of(VILLAGE_MERCHANT, LUXURY_MERCHANT, MYSTERY_MERCHANT,
            BLACKSMITH, GIFT_WRAPPER, ENCHANTSMITH);

    private VillageContent() {}

    private static MerchantRole role(String id, String workstationId, SoundEvent workSound) {
        var block = BLOCKS.register(workstationId, () -> new MerchantWorkstationBlock(
                BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS).strength(2.5F).noOcclusion()));
        var item = ITEMS.register(workstationId, () -> new BlockItem(block.get(), new Item.Properties()));
        var poi = POIS.register(id, () -> new PoiType(ImmutableSet.copyOf(block.get().getStateDefinition().getPossibleStates()), 1, 1));
        var profession = PROFESSIONS.register(id, () -> new VillagerProfession(id,
                holder -> holder.is(poi.getKey()), holder -> holder.is(poi.getKey()),
                ImmutableSet.of(), ImmutableSet.of(), workSound));
        return new MerchantRole(id, workstationId, block, item, poi, profession);
    }

    public static void register(IEventBus bus, ModContainer container) {
        BLOCKS.register(bus);
        ITEMS.register(bus);
        POIS.register(bus);
        PROFESSIONS.register(bus);
        bus.addListener(VillageContent::creative);
        bus.addListener(VillageAnimationNetworking::register);
        container.registerConfig(ModConfig.Type.CLIENT, VillageAnimationConfig.SPEC, "dungeonsalive-villagers-client.toml");
        NeoForge.EVENT_BUS.addListener(MerchantTrades::register);
        VillageAnimationEvents.register();
    }

    private static void creative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.FUNCTIONAL_BLOCKS) ROLES.forEach(role -> event.accept(role.item()));
    }
}
