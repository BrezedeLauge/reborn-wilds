package de.laugenbreze.dungeonsalive.settlement.inn;

import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

/** Status-only menu. Food remains in the explicitly selected real chest/barrel. */
public final class InnMenu extends AbstractContainerMenu {
    private final Inventory inventory;
    private final InnPlanView plan;
    private final SimpleContainerData data = new SimpleContainerData(15);
    private boolean removed, committed;
    private long nextRefresh;
    public InnMenu(int id, Inventory inventory, RegistryFriendlyByteBuf extra) { this(id, inventory, InnPlanView.read(extra)); }
    public InnMenu(int id, Inventory inventory, InnPlanView plan) {
        super(InnContent.MENU.get(), id); this.inventory = inventory; this.plan = plan; addDataSlots(data);
    }
    public InnPlanView plan() { return plan; }
    public InnStatus.State state() { return enumAt(InnStatus.State.values(), data.get(0), InnStatus.State.UNCONFIGURED); }
    public InnStatus.GuestState guest() { return enumAt(InnStatus.GuestState.values(), data.get(1), InnStatus.GuestState.NONE); }
    public int seats() { return data.get(2); }
    public int beds() { return data.get(3); }
    public int food() { return data.get(4); }
    public boolean connected() { return data.get(5) != 0; }
    @Nullable public BlockPos problem() { return data.get(6) == 0 ? null : new BlockPos(readInt(7), readInt(9), readInt(11)); }
    public InnService.Code result() { return enumAt(InnService.Code.values(), data.get(13), InnService.Code.UNAVAILABLE); }
    public boolean canCommit() { return data.get(14) != 0 && plan.complete(); }
    private static <T> T enumAt(T[] values, int index, T fallback) { return index >= 0 && index < values.length ? values[index] : fallback; }
    private void writeInt(int offset, int value) { data.set(offset, value & 65535); data.set(offset + 1, (value >>> 16) & 65535); }
    private int readInt(int offset) { return (data.get(offset) & 65535) | (data.get(offset + 1) & 65535) << 16; }
    @Override public boolean stillValid(Player player) {
        if (player.level().isClientSide) return true;
        return !removed && player == inventory.player && player.containerMenu == this && player.isAlive() && !player.isSpectator()
                && player.mayBuild() && player.distanceToSqr(Vec3.atCenterOf(plan.anchor())) <= 64
                && player.level().hasChunkAt(plan.anchor()) && player.level().mayInteract(player, plan.anchor())
                && player.level().getBlockEntity(plan.anchor()) instanceof InnAnchorBlockEntity anchor
                && player.getUUID().equals(anchor.ownerId()) && plan.placement().equals(anchor.placementId());
    }
    @Override public void broadcastChanges() {
        if (inventory.player instanceof ServerPlayer player && stillValid(player) && player.serverLevel().getGameTime() >= nextRefresh) {
            nextRefresh = player.serverLevel().getGameTime() + 20; refresh(player);
        }
        super.broadcastChanges();
    }
    private void refresh(ServerPlayer player) {
        var status = InnService.status(player, plan.anchor());
        data.set(0, status.state().ordinal()); data.set(1, status.guest().ordinal());
        data.set(2, status.seats()); data.set(3, status.beds()); data.set(4, status.foodItems()); data.set(5, status.connected() ? 1 : 0);
        BlockPos problem = status.problem(); data.set(6, problem == null ? 0 : 1);
        if (problem != null) { writeInt(7, problem.getX()); writeInt(9, problem.getY()); writeInt(11, problem.getZ()); }
        var draft = InnSelection.get(player, plan.anchor());
        data.set(14, !committed && plan.complete() && draft != null && draft.complete()
                && draft.token().equals(plan.token()) && draft.revision() == plan.revision() && status.revision() == plan.revision() ? 1 : 0);
    }
    /** The vanilla button packet is only an intent; its immutable menu identity pins the shown plan and revision. */
    @Override public boolean clickMenuButton(Player player, int action) {
        if (!(player instanceof ServerPlayer server) || !stillValid(player) || action < 0 || action > 1) return false;
        if (action == 1) { InnSelection.clear(server); server.closeContainer(); return true; }
        refresh(server);
        if (!canCommit()) { data.set(13, InnService.Code.STALE_REVISION.ordinal()); return false; }
        var draft = InnSelection.get(server, plan.anchor());
        if (draft == null || !draft.token().equals(plan.token())) return false;
        var result = InnService.configure(server, plan.anchor(), plan.revision(), draft.bounds(), draft.entrance(), draft.stock(), draft.arrival());
        data.set(13, result.code().ordinal());
        if (result.code() == InnService.Code.OK) { committed = true; InnSelection.clear(server); }
        refresh(server); broadcastChanges(); return true;
    }
    @Override public void clicked(int slot, int button, ClickType type, Player player) { }
    @Override public ItemStack quickMoveStack(Player player, int index) { return ItemStack.EMPTY; }
    @Override public void removed(Player player) { removed = true; super.removed(player); }
}
