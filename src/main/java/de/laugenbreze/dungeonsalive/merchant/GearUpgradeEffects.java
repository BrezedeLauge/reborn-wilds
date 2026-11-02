package de.laugenbreze.dungeonsalive.merchant;

import de.laugenbreze.dungeonsalive.item.gear.ArtifactCombat;
import de.laugenbreze.dungeonsalive.item.gear.DungeonsWeaponItem;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;

/** The actual server-held weapon determines the melee rank; projectiles snapshot at launch. */
public final class GearUpgradeEffects {
    private GearUpgradeEffects() {}
    public static void incomingDamage(LivingIncomingDamageEvent event) {
        if (event.isCanceled() || event.getEntity().level().isClientSide
                || !(event.getSource().getEntity() instanceof Player player)
                || event.getSource().getDirectEntity() != player || !event.getSource().is(DamageTypes.PLAYER_ATTACK)
                || !(player.getMainHandItem().getItem() instanceof DungeonsWeaponItem)
                || !ArtifactCombat.hostileTo(player, event.getEntity())
                || !Float.isFinite(event.getAmount()) || event.getAmount() <= 0) return;
        event.setAmount((float) Math.min(Float.MAX_VALUE,
                (double) event.getAmount() * GearUpgrade.damageMultiplier(player.getMainHandItem())));
    }
}
