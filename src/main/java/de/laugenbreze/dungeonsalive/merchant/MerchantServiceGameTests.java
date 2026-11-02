package de.laugenbreze.dungeonsalive.merchant;

import com.mojang.authlib.GameProfile;
import de.laugenbreze.dungeonsalive.DungeonsAlive;
import de.laugenbreze.dungeonsalive.entity.peaceful.DungeonsMerchant;
import de.laugenbreze.dungeonsalive.entity.peaceful.ModPeacefulEntities;
import de.laugenbreze.dungeonsalive.item.gear.GearContent;
import de.laugenbreze.dungeonsalive.item.gear.WeaponKind;
import de.laugenbreze.dungeonsalive.item.gear.enchantment.GearEnchantmentKind;
import de.laugenbreze.dungeonsalive.item.gear.enchantment.GearEnchantments;
import de.laugenbreze.dungeonsalive.network.MerchantServiceActionPayload;
import de.laugenbreze.dungeonsalive.network.MerchantServiceNetworking;
import io.netty.buffer.Unpooled;
import java.util.OptionalInt;
import java.util.UUID;
import java.util.function.Consumer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.MerchantMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.util.FakePlayer;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;
import org.jetbrains.annotations.Nullable;

/** Native menu slots and real commits, without substituting a client-authored result or fabricated damage event. */
@GameTestHolder(DungeonsAlive.MOD_ID)
@PrefixGameTestTemplate(false)
public final class MerchantServiceGameTests {
    private static final String BATCH = "merchant_services";
    private MerchantServiceGameTests() {}

    @GameTest(template = "gametest_empty", templateNamespace = DungeonsAlive.MOD_ID, batch = BATCH, timeoutTicks = 40)
    public static void repairCommitsOncePreservesMetadataAndRejectsAStalePreview(GameTestHelper helper) {
        var player = player(helper); var npc = merchant(helper, "blacksmith");
        var menu = open(helper, player, npc);
        ItemStack original = namedWeapon(helper); original.setDamageValue(700);
        player.getInventory().setItem(0, original.copy()); player.getInventory().setItem(1, new ItemStack(Items.EMERALD, 20));
        helper.assertFalse(menu.quickMoveStack(player, 30).isEmpty(), "Actual player-inventory input did not shift into service");
        helper.assertFalse(menu.quickMoveStack(player, 31).isEmpty(), "Actual emeralds did not shift into payment");
        menu.broadcastChanges();
        helper.assertTrue(menu.status() == ServiceStatus.READY && menu.getSlot(2).getItem().getDamageValue() == 0,
                "Repair quote did not show the actual metadata-preserving output");
        var buffer = new RegistryFriendlyByteBuf(Unpooled.buffer(), helper.getLevel().registryAccess());
        try {
            var payload = new MerchantServiceActionPayload(menu.containerId, 0, 29_999);
            MerchantServiceActionPayload.CODEC.encode(buffer, payload);
            helper.assertTrue(MerchantServiceActionPayload.CODEC.decode(buffer).equals(payload), "Service nonce was truncated by its actual packet codec");
        } finally { buffer.release(); }
        MerchantServiceNetworking.handle(player, new MerchantServiceActionPayload(menu.containerId + 1, 0, menu.quoteRevision()));
        MerchantServiceNetworking.handle(player, new MerchantServiceActionPayload(menu.containerId, 0, 29_999));
        helper.assertTrue(menu.getSlot(0).getItem().getDamageValue() == 700 && menu.getSlot(1).getItem().getCount() == 20,
                "Wrong-window or wrong-nonce network action consumed service inputs");
        int oldRevision = menu.quoteRevision();
        menu.clicked(2, 0, ClickType.PICKUP, player);
        helper.assertTrue(menu.getCarried().isEmpty() && menu.quickMoveStack(player, 2).isEmpty(), "Preview was extractable without payment");
        menu.getSlot(1).set(new ItemStack(Items.EMERALD, 19));
        helper.assertFalse(menu.handleServiceAction(player, 0, oldRevision), "A stale preview revision bought against changed payment state");
        int price = menu.cost(), revision = menu.quoteRevision();
        helper.assertTrue(menu.handleServiceAction(player, 0, revision), "Ready server-side repair did not commit");
        ItemStack expected = original.copy(); expected.setDamageValue(0);
        helper.assertTrue(ItemStack.matches(menu.getSlot(0).getItem(), expected) && menu.getSlot(1).getItem().getCount() == 19 - price,
                "Repair changed unrelated components or charged the wrong emerald count");
        helper.assertTrue(menu.status() == ServiceStatus.COLLECT_RESULT && menu.getSlot(2).getItem().isEmpty(), "Paid result was not isolated from a second quote");
        helper.assertFalse(menu.handleServiceAction(player, 0, revision), "Replayed commit charged twice");
        helper.assertFalse(menu.handleServiceAction(player, 0, menu.quoteRevision()), "Double-click bought another operation before result collection");
        player.closeContainer(); menu.removed(player);
        helper.assertTrue(player.getInventory().countItem(expected.getItem()) == 1 && player.getInventory().countItem(Items.EMERALD) == 19 - price,
                "Close/repeated removal lost or duplicated actual input/payment");
        helper.assertTrue(ItemStack.matches(findWeapon(player), expected) && npc.getTradingPlayer() == null,
                "Returned result lost metadata or retained the customer lock");
        helper.succeed();
    }

    @GameTest(template = "gametest_empty", templateNamespace = DungeonsAlive.MOD_ID, batch = BATCH, timeoutTicks = 40)
    public static void powersmithPersistsExactlyThreePaidRanksWithoutReplacingTheWeapon(GameTestHelper helper) {
        var player = player(helper); var npc = merchant(helper, "powersmith"); var menu = open(helper, player, npc);
        ItemStack original = namedWeapon(helper); original.setDamageValue(73);
        menu.getSlot(0).set(original.copy());
        for (int rank = 1; rank <= 3; rank++) {
            menu.getSlot(1).set(new ItemStack(Items.EMERALD, 16 * rank));
            helper.assertTrue(menu.cost() == 16 * rank && menu.handleServiceAction(player, 0, menu.quoteRevision()), "Powersmith refused a legal paid rank");
            helper.assertTrue(GearUpgrade.rank(menu.getSlot(0).getItem()) == rank && menu.getSlot(1).getItem().isEmpty(), "Rank or emerald debit was incorrect");
            helper.assertTrue(menu.getSlot(0).getItem().getDamageValue() == 73
                            && original.get(DataComponents.CUSTOM_NAME).equals(menu.getSlot(0).getItem().get(DataComponents.CUSTOM_NAME))
                            && original.get(DataComponents.ENCHANTMENTS).equals(menu.getSlot(0).getItem().get(DataComponents.ENCHANTMENTS)),
                    "Permanent upgrade replaced durability, custom name or native enchantments");
            collectAndReinsert(helper, menu, player);
        }
        menu.getSlot(1).set(new ItemStack(Items.EMERALD, 64));
        helper.assertTrue(menu.status() == ServiceStatus.MAXIMUM && !menu.handleServiceAction(player, 0, menu.quoteRevision()), "Powersmith sold rank four");
        var result = menu.getSlot(0).getItem();
        var restored = ItemStack.parse(helper.getLevel().registryAccess(), result.save(helper.getLevel().registryAccess())).orElseThrow();
        helper.assertTrue(ItemStack.matches(result, restored) && GearUpgrade.rank(restored) == 3 && GearUpgrade.rank(original) == 0,
                "Full item NBT roundtrip lost the permanent rank or the preview mutated the original");
        player.closeContainer();
        helper.assertTrue(player.getInventory().countItem(original.getItem()) == 1 && player.getInventory().countItem(Items.EMERALD) == 64,
                "Rejected rank-four purchase consumed or lost items");
        helper.succeed();
    }

    @GameTest(template = "gametest_empty", templateNamespace = DungeonsAlive.MOD_ID, batch = BATCH, timeoutTicks = 40)
    public static void enchantsmithAddsChosenNativeEnchantmentsAndRejectsOldSelectionTokens(GameTestHelper helper) {
        var player = player(helper); var npc = merchant(helper, "enchantsmith"); var menu = open(helper, player, npc);
        ItemStack original = GearUpgrade.upgrade(namedWeapon(helper)); original.setDamageValue(44);
        menu.getSlot(0).set(original.copy()); menu.getSlot(1).set(new ItemStack(Items.EMERALD, 64));
        int stale = menu.quoteRevision();
        helper.assertTrue(menu.handleServiceAction(player, 11, stale), "Enchantment selection did not change");
        helper.assertFalse(menu.handleServiceAction(player, 0, stale), "Old selection token bought the replacement enchantment");
        for (int index = 0; index < GearEnchantments.definitions().size(); index++) {
            helper.assertTrue(menu.handleServiceAction(player, 10 + index, menu.quoteRevision()), "Known enchantment selection was rejected");
            menu.getSlot(1).set(new ItemStack(Items.EMERALD, 8));
            helper.assertTrue(menu.handleServiceAction(player, 0, menu.quoteRevision()), "Enchantsmith did not commit a registered level-I enchantment");
            var selected = GearEnchantments.definitions().get(index);
            helper.assertTrue(GearEnchantments.level(menu.getSlot(0).getItem(), helper.getLevel(), selected) == 1,
                    "Paid native enchantment component differs from the selected option");
            collectAndReinsert(helper, menu, player);
        }
        menu.handleServiceAction(player, 10, menu.quoteRevision());
        for (int rank = 2; rank <= 3; rank++) {
            menu.getSlot(1).set(new ItemStack(Items.EMERALD, 8 * rank));
            helper.assertTrue(menu.handleServiceAction(player, 0, menu.quoteRevision()), "Enchantment rank II/III was rejected");
            collectAndReinsert(helper, menu, player);
        }
        var result = menu.getSlot(0).getItem();
        helper.assertTrue(menu.status() == ServiceStatus.MAXIMUM && GearEnchantments.level(result, helper.getLevel(), GearEnchantmentKind.RADIANCE) == 3,
                "Enchantsmith did not stop at the defined third level");
        helper.assertTrue(GearUpgrade.rank(result) == 1 && result.getDamageValue() == 44
                        && result.get(DataComponents.CUSTOM_NAME).equals(original.get(DataComponents.CUSTOM_NAME))
                        && result.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getString("ServiceTestMarker").equals("keep"),
                "Enchanting lost existing upgrade, durability, name or unrelated custom data");
        player.closeContainer(); helper.succeed();
    }

    @GameTest(template = "gametest_empty", templateNamespace = DungeonsAlive.MOD_ID, batch = BATCH, timeoutTicks = 40)
    public static void invalidCustomerDistanceDamageAndGoodsSwitchNeverConsumeAHiddenInput(GameTestHelper helper) {
        var player = player(helper); var npc = merchant(helper, "blacksmith"); var menu = open(helper, player, npc);
        var previous = menu;
        menu = open(helper, player, npc);
        helper.assertTrue(menu != previous && menu.stillValid(player), "Reopening the same NPC lost its newly installed customer lock");
        ItemStack original = namedWeapon(helper); original.setDamageValue(20);
        menu.getSlot(0).set(original.copy()); menu.getSlot(1).set(new ItemStack(Items.EMERALD, 12));
        var stranger = player(helper);
        helper.assertFalse(menu.handleServiceAction(stranger, 0, menu.quoteRevision()), "Another player committed this customer's service");
        player.setPos(player.position().add(30, 0, 0));
        helper.assertFalse(menu.stillValid(player) || menu.handleServiceAction(player, 0, menu.quoteRevision()), "Distant player could still buy");
        player.setPos(helper.absoluteVec(new Vec3(3.5, 1, 5.5)));
        helper.assertTrue(npc.hurt(helper.getLevel().damageSources().generic(), 1), "Real merchant damage was not accepted");
        helper.assertTrue(player.containerMenu == player.inventoryMenu && ItemStack.matches(findWeapon(player), original)
                        && player.getInventory().countItem(Items.EMERALD) == 12,
                "Damage did not close only the active service and return unmodified items");
        var next = open(helper, player, npc);
        helper.assertTrue(next.handleServiceAction(player, 1, next.quoteRevision()) && player.containerMenu instanceof MerchantMenu,
                "Explicit goods button did not open the actual existing MerchantOffers menu");
        var goods = player.containerMenu;
        MerchantServices.closeCustomer(npc);
        helper.assertTrue(player.containerMenu == goods && npc.getTradingPlayer() == player, "Service closer disturbed ordinary goods trading");
        player.closeContainer();
        var foreign = ChestMenu.threeRows(91, player.getInventory(), new SimpleContainer(27)); player.containerMenu = foreign;
        npc.setTradingPlayer(player); MerchantServices.closeCustomer(npc);
        helper.assertTrue(player.containerMenu == foreign, "Stale customer reference closed a foreign chest");
        npc.setTradingPlayer(null); player.closeContainer(); helper.succeed();
    }

    @GameTest(template = "gametest_empty", templateNamespace = DungeonsAlive.MOD_ID, batch = BATCH, timeoutTicks = 40)
    public static void closingWithAFullInventoryReturnsOnlyRealInputAndPaymentAsDrops(GameTestHelper helper) {
        var player = player(helper); var npc = merchant(helper, "blacksmith"); var menu = open(helper, player, npc);
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) player.getInventory().setItem(i, new ItemStack(Items.STONE, 64));
        ItemStack original = namedWeapon(helper); original.setDamageValue(40);
        menu.getSlot(0).set(original.copy()); menu.getSlot(1).set(new ItemStack(Items.EMERALD, 9));
        helper.assertFalse(menu.getSlot(2).getItem().isEmpty(), "Full-inventory case needs a real preview to detect accidental preview duplication");
        player.closeContainer(); menu.removed(player);
        var drops = helper.getLevel().getEntitiesOfClass(ItemEntity.class, player.getBoundingBox().inflate(5));
        int weapons = drops.stream().filter(drop -> drop.getItem().is(original.getItem())).mapToInt(drop -> drop.getItem().getCount()).sum();
        int emeralds = drops.stream().filter(drop -> drop.getItem().is(Items.EMERALD)).mapToInt(drop -> drop.getItem().getCount()).sum();
        helper.assertTrue(weapons == 1 && emeralds == 9 && drops.stream().anyMatch(drop -> ItemStack.matches(drop.getItem(), original)),
                "Full inventory close lost input/payment, created a repaired preview, or returned items twice");
        drops.forEach(ItemEntity::discard); helper.succeed();
    }

    private static void collectAndReinsert(GameTestHelper helper, MerchantServiceMenu menu, MenuPlayer player) {
        menu.clicked(0, 0, ClickType.PICKUP, player);
        helper.assertTrue(menu.getSlot(0).getItem().isEmpty() && !menu.getCarried().isEmpty(), "Paid result could not be collected normally");
        menu.clicked(0, 0, ClickType.PICKUP, player);
        helper.assertTrue(menu.getCarried().isEmpty() && !menu.getSlot(0).getItem().isEmpty(), "Collected input could not be deliberately resubmitted");
    }
    private static MenuPlayer player(GameTestHelper helper) {
        for (int x = 0; x < 20; x++) for (int z = 0; z < 12; z++) helper.setBlock(new BlockPos(x, 0, z), Blocks.STONE);
        var player = new MenuPlayer(helper.getLevel()); player.setGameMode(GameType.SURVIVAL);
        player.setPos(helper.absoluteVec(new Vec3(3.5, 1, 5.5))); return player;
    }
    private static DungeonsMerchant merchant(GameTestHelper helper, String id) {
        return helper.spawnWithNoFreeWill(ModPeacefulEntities.merchantType(id), new Vec3(5.5, 1, 5.5));
    }
    private static MerchantServiceMenu open(GameTestHelper helper, MenuPlayer player, DungeonsMerchant merchant) {
        helper.assertTrue(MerchantServices.open(player, merchant) && player.containerMenu instanceof MerchantServiceMenu, "Real service provider did not create its registered server menu");
        var menu = (MerchantServiceMenu)player.containerMenu; menu.broadcastChanges(); return menu;
    }
    private static ItemStack namedWeapon(GameTestHelper helper) {
        var stack = new ItemStack(GearContent.weapon(WeaponKind.HEARTSTEALER));
        stack.set(DataComponents.CUSTOM_NAME, Component.literal("Keep this exact weapon"));
        CustomData.update(DataComponents.CUSTOM_DATA, stack, tag -> tag.putString("ServiceTestMarker", "keep"));
        stack.enchant(helper.getLevel().registryAccess().lookupOrThrow(Registries.ENCHANTMENT).get(Enchantments.UNBREAKING).orElseThrow(), 2);
        return stack;
    }
    private static ItemStack findWeapon(MenuPlayer player) {
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            var item = player.getInventory().getItem(i); if (item.is(GearContent.weapon(WeaponKind.HEARTSTEALER))) return item;
        }
        return ItemStack.EMPTY;
    }
    /** FakePlayer's normal openMenu is a no-op. This fixture creates the actual provider menu and uses its safe no-op network connection. */
    private static final class MenuPlayer extends FakePlayer {
        private int nextMenuId;
        private MenuPlayer(ServerLevel level) { super(level, new GameProfile(UUID.randomUUID(), "service-test")); }
        @Override public OptionalInt openMenu(@Nullable MenuProvider provider, @Nullable Consumer<RegistryFriendlyByteBuf> extraDataWriter) {
            if (provider == null) return OptionalInt.empty();
            if (containerMenu != inventoryMenu) closeContainer();
            int id = ++nextMenuId;
            var menu = provider.createMenu(id, getInventory(), this);
            if (menu == null) return OptionalInt.empty();
            containerMenu = menu; return OptionalInt.of(id);
        }
    }
}
