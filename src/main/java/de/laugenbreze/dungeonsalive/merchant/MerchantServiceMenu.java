package de.laugenbreze.dungeonsalive.merchant;

import de.laugenbreze.dungeonsalive.entity.peaceful.DungeonsMerchant;
import de.laugenbreze.dungeonsalive.item.gear.DungeonsWeaponItem;
import de.laugenbreze.dungeonsalive.item.gear.enchantment.GearEnchantments;
import java.util.UUID;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.Nullable;

/** Server-owned input/payment transaction; result slot is a display-only copy and is never returned or extracted. */
public final class MerchantServiceMenu extends AbstractContainerMenu {
    public static final int INPUT_SLOT = 0, PAYMENT_SLOT = 1, PREVIEW_SLOT = 2;
    private static final int PLAYER_START = 3, PLAYER_END = 39, COMMIT_BASE = 1000, MAX_REVISION = 30_000;
    private final SimpleContainer inputs = new SimpleContainer(2), preview = new SimpleContainer(1);
    private final SimpleContainerData data = new SimpleContainerData(4);
    private final Inventory inventory;
    private final ServiceKind kind;
    private final int merchantId;
    @Nullable private final DungeonsMerchant merchant;
    @Nullable private final UUID merchantUuid;
    private ItemStack quotedInput = ItemStack.EMPTY, quotedPayment = ItemStack.EMPTY, quotedResult = ItemStack.EMPTY, completedResult = ItemStack.EMPTY;
    private int quotedSelection = -1;
    private boolean changing, removed, exhausted;

    /** Identical slots/data layout on both sides; entity and service identity come from the server open packet. */
    public MerchantServiceMenu(int id, Inventory inventory, RegistryFriendlyByteBuf extra) {
        this(id, inventory, extra.readVarInt(), readKind(extra), null);
    }
    public MerchantServiceMenu(int id, Inventory inventory, DungeonsMerchant merchant, ServiceKind kind) {
        this(id, inventory, merchant.getId(), kind, merchant);
        if (ServiceKind.forMerchant(merchant.profile().id()) != kind) throw new IllegalArgumentException("Service does not belong to merchant");
    }
    private MerchantServiceMenu(int id, Inventory inventory, int merchantId, ServiceKind kind, @Nullable DungeonsMerchant merchant) {
        super(MerchantServices.MENU.get(), id);
        this.inventory = inventory; this.merchantId = merchantId; this.kind = kind; this.merchant = merchant;
        this.merchantUuid = merchant == null ? null : merchant.getUUID();
        addSlot(new Slot(inputs, 0, 26, 45) {
            @Override public int getMaxStackSize() { return 1; }
            @Override public boolean mayPlace(ItemStack stack) { return acceptsInput(stack); }
        });
        addSlot(new Slot(inputs, 1, 80, 45) {
            @Override public boolean mayPlace(ItemStack stack) { return stack.is(Items.EMERALD); }
        });
        addSlot(new Slot(preview, 0, 134, 45) {
            @Override public boolean mayPlace(ItemStack stack) { return false; }
            @Override public boolean mayPickup(Player player) { return false; }
        });
        for (int row = 0; row < 3; row++) for (int col = 0; col < 9; col++)
            addSlot(new Slot(inventory, col + row * 9 + 9, 8 + col * 18, 145 + row * 18));
        for (int col = 0; col < 9; col++) addSlot(new Slot(inventory, col, 8 + col * 18, 203));
        addDataSlots(data);
        data.set(3, 1);
        inputs.addListener(this::slotsChanged);
    }
    private static ServiceKind readKind(RegistryFriendlyByteBuf buffer) {
        int ordinal = buffer.readVarInt();
        if (ordinal < 0 || ordinal >= ServiceKind.values().length) throw new IllegalArgumentException("Unknown merchant service");
        return ServiceKind.values()[ordinal];
    }
    public ServiceKind serviceKind() { return kind; }
    public int merchantId() { return merchantId; }
    public int cost() { return data.get(0); }
    public ServiceStatus status() {
        int value = data.get(1);
        return value >= 0 && value < ServiceStatus.values().length ? ServiceStatus.values()[value] : ServiceStatus.UNAVAILABLE;
    }
    public int selection() { return data.get(2); }
    public int quoteRevision() { return data.get(3); }
    public int commitButtonId() { return COMMIT_BASE + data.get(3); }
    /** Called by the explicit VarInt C2S payload; vanilla button packets cannot represent our commit token. */
    public boolean handleServiceAction(ServerPlayer player, int action, int revision) {
        if (revision < 1 || revision > MAX_REVISION || revision != quoteRevision()) return false;
        if (action != 0 && action != 1 && !(kind == ServiceKind.ENCHANTSMITH && action >= 10 && action < 10 + GearEnchantments.definitions().size())) return false;
        return clickMenuButton(player, action == 0 ? COMMIT_BASE + revision : action);
    }
    public boolean belongsTo(DungeonsMerchant npc) { return merchant == npc && merchantUuid != null && merchantUuid.equals(npc.getUUID()) && merchant.level() == npc.level(); }
    private boolean acceptsInput(ItemStack stack) {
        return !stack.isEmpty() && switch (kind) {
            case BLACKSMITH -> stack.isDamageableItem();
            case POWERSMITH -> GearUpgrade.supports(stack);
            case ENCHANTSMITH -> stack.getItem() instanceof DungeonsWeaponItem;
        };
    }
    @Override public boolean stillValid(Player player) {
        if (player.level().isClientSide) return true;
        return !removed && !exhausted && player == inventory.player && player.isAlive() && !player.isSpectator() && player.containerMenu == this
                && merchant != null && belongsTo(merchant) && merchant.isAlive() && !merchant.isRemoved()
                && merchant.level() == player.level() && merchant.getTradingPlayer() == player && player.distanceToSqr(merchant) <= 64
                && ServiceKind.forMerchant(merchant.profile().id()) == kind;
    }
    @Override public void slotsChanged(Container container) {
        if (!changing && !removed && !inventory.player.level().isClientSide) refreshQuote();
    }
    @Override public void broadcastChanges() {
        if (!removed && !inventory.player.level().isClientSide) refreshQuote();
        super.broadcastChanges();
    }
    private void refreshQuote() {
        if (!(inventory.player.level() instanceof ServerLevel level)) return;
        ItemStack input = inputs.getItem(0), payment = inputs.getItem(1);
        if (!completedResult.isEmpty() && !ItemStack.matches(input, completedResult)) completedResult = ItemStack.EMPTY;
        int enchantLevel = 0;
        boolean enchantable = false;
        if (kind == ServiceKind.ENCHANTSMITH && selection() >= 0 && selection() < GearEnchantments.definitions().size()) {
            var enchantment = GearEnchantments.definitions().get(selection());
            enchantLevel = GearEnchantments.level(input, level, enchantment);
            enchantable = GearEnchantments.canApply(input, level, enchantment);
        }
        var quote = MerchantServicePolicy.quote(kind, new MerchantServicePolicy.Input(input.getCount(), input.isDamageableItem(), input.getDamageValue(),
                input.getMaxDamage(), GearUpgrade.supports(input), GearUpgrade.rank(input), enchantable, enchantLevel,
                payment.is(Items.EMERALD) ? payment.getCount() : 0, !completedResult.isEmpty()));
        ItemStack result = quote.cost() > 0 ? transform(input, level) : ItemStack.EMPTY;
        if (quote.cost() > 0 && result.isEmpty()) quote = new MerchantServicePolicy.Quote(ServiceStatus.UNSUPPORTED, 0);
        boolean changed = !ItemStack.matches(input, quotedInput) || !ItemStack.matches(payment, quotedPayment)
                || !ItemStack.matches(result, quotedResult) || quotedSelection != selection()
                || cost() != quote.cost() || data.get(1) != quote.status().ordinal();
        if (changed) {
            if (data.get(3) >= MAX_REVISION) exhausted = true;
            else data.set(3, data.get(3) + 1);
            quotedInput = input.copy(); quotedPayment = payment.copy(); quotedResult = result.copy(); quotedSelection = selection();
        }
        data.set(0, exhausted ? 0 : quote.cost());
        data.set(1, (exhausted ? ServiceStatus.UNAVAILABLE : quote.status()).ordinal());
        preview.setItem(0, exhausted ? ItemStack.EMPTY : result);
    }
    private ItemStack transform(ItemStack input, ServerLevel level) {
        if (input.getCount() != 1) return ItemStack.EMPTY;
        return switch (kind) {
            case BLACKSMITH -> {
                if (!input.isDamageableItem() || !input.isDamaged()) yield ItemStack.EMPTY;
                ItemStack result = input.copy(); result.setDamageValue(0); yield result;
            }
            case POWERSMITH -> GearUpgrade.upgrade(input);
            case ENCHANTSMITH -> selection() >= 0 && selection() < GearEnchantments.definitions().size()
                    ? GearEnchantments.apply(input, level, GearEnchantments.definitions().get(selection())) : ItemStack.EMPTY;
        };
    }
    @Override public boolean clickMenuButton(Player player, int buttonId) {
        if (!(player instanceof ServerPlayer server) || !stillValid(player)) return false;
        if (buttonId == 1) {
            server.closeContainer();
            if (merchant != null && merchant.isAlive() && !merchant.isRemoved() && merchant.level() == server.level()
                    && merchant.distanceToSqr(server) <= 64 && merchant.getTradingPlayer() == null) merchant.openGoodsTrade(server);
            return true;
        }
        if (kind == ServiceKind.ENCHANTSMITH && buttonId >= 10 && buttonId < 10 + GearEnchantments.definitions().size()) {
            data.set(2, buttonId - 10); refreshQuote(); broadcastChanges(); return true;
        }
        refreshQuote();
        if (buttonId != commitButtonId() || status() != ServiceStatus.READY || !(server.level() instanceof ServerLevel level)) return false;
        // Recreate from the actual live server stack, never from the display slot or a client packet.
        ItemStack result = transform(inputs.getItem(0), level);
        if (result.isEmpty() || !ItemStack.matches(result, quotedResult) || !ItemStack.matches(inputs.getItem(0), quotedInput)
                || !ItemStack.matches(inputs.getItem(1), quotedPayment) || !inputs.getItem(1).is(Items.EMERALD) || inputs.getItem(1).getCount() < cost()) return false;
        int price = cost();
        changing = true;
        try {
            completedResult = result.copy();
            inputs.setItem(0, result);
            inputs.removeItem(1, price);
        } finally { changing = false; }
        merchant.beginGreeting();
        refreshQuote(); broadcastChanges();
        return true;
    }
    @Override public void clicked(int slotId, int button, ClickType type, Player player) {
        if (!stillValid(player) || slotId == PREVIEW_SLOT || slotId < -999 || slotId >= slots.size()) return;
        super.clicked(slotId, button, type, player);
    }
    @Override public boolean canDragTo(Slot slot) { return slot != slots.get(PREVIEW_SLOT) && super.canDragTo(slot); }
    @Override public boolean canTakeItemForPickAll(ItemStack stack, Slot slot) { return slot != slots.get(PREVIEW_SLOT) && super.canTakeItemForPickAll(stack, slot); }
    @Override public ItemStack quickMoveStack(Player player, int slotIndex) {
        if (!stillValid(player) || slotIndex < 0 || slotIndex >= slots.size() || slotIndex == PREVIEW_SLOT) return ItemStack.EMPTY;
        Slot slot = slots.get(slotIndex);
        if (!slot.hasItem() || !slot.mayPickup(player)) return ItemStack.EMPTY;
        ItemStack stack = slot.getItem(), original = stack.copy();
        if (slotIndex < PLAYER_START) {
            if (!moveItemStackTo(stack, PLAYER_START, PLAYER_END, true)) return ItemStack.EMPTY;
        } else if (stack.is(Items.EMERALD)) {
            if (!moveItemStackTo(stack, PAYMENT_SLOT, PAYMENT_SLOT + 1, false)) return ItemStack.EMPTY;
        } else if (acceptsInput(stack)) {
            if (!moveItemStackTo(stack, INPUT_SLOT, INPUT_SLOT + 1, false)) return ItemStack.EMPTY;
        } else return ItemStack.EMPTY;
        if (stack.isEmpty()) slot.set(ItemStack.EMPTY); else slot.setChanged();
        slot.onTake(player, stack);
        refreshQuote();
        return original;
    }
    @Override public void removed(Player player) {
        if (removed) return;
        removed = true;
        super.removed(player);
        if (!player.level().isClientSide) {
            clearContainer(player, inputs);
            if (merchant != null && merchant.getTradingPlayer() == player) merchant.setTradingPlayer(null);
        }
        preview.clearContent();
        quotedInput = quotedPayment = quotedResult = completedResult = ItemStack.EMPTY;
    }
}
