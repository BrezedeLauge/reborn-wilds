package de.laugenbreze.dungeonsalive.item.gear;

import de.laugenbreze.dungeonsalive.entity.DungeonMob;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.damagesource.DamageTypes;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

public final class GearEvents {
    public static final String GONG_UNTIL = "DungeonsAliveGongUntil";
    private GearEvents() {}

    public static void incomingDamage(LivingIncomingDamageEvent event) {
        if (event.getEntity().level().isClientSide || !(event.getEntity() instanceof Enemy)
                || !(event.getSource().getEntity() instanceof Player player)
                || !ArtifactCombat.hostileTo(player, event.getEntity())) return;
        long until = event.getEntity().getPersistentData().getLong(GONG_UNTIL);
        if (until > event.getEntity().level().getGameTime()) event.setAmount(event.getAmount() * 1.25F);
    }

    public static void death(LivingDeathEvent event) {
        if (event.isCanceled() || event.getEntity().level().isClientSide || !(event.getEntity() instanceof Enemy)
                || !(event.getSource().getEntity() instanceof Player player)
                || event.getEntity().isAlliedTo(player) || player.isAlliedTo(event.getEntity())
                || (event.getEntity() instanceof net.minecraft.world.entity.OwnableEntity pet && pet.getOwnerUUID() != null)) return;
        // Like loot and XP, soul rewards cannot be farmed from temporary reinforcements.
        // Direct-kill weapon effects still apply to these real combat opponents.
        boolean directMelee = event.getSource().is(DamageTypes.PLAYER_ATTACK) && event.getSource().getDirectEntity() == player;
        WeaponKind weapon = player.getMainHandItem().getItem() instanceof DungeonsWeaponItem held ? held.kind() : null;
        if (!(event.getEntity() instanceof DungeonMob mob && mob.isSummoned()))
            SoulEnergy.earn(player, 3 + (directMelee && weapon != null ? weapon.family.soulBonus : 0));
        if (directMelee && weapon == WeaponKind.HEARTSTEALER)
            player.heal(Math.min(6, event.getEntity().getMaxHealth() * .09F));
    }

    public static void clonePlayer(PlayerEvent.Clone event) {
        int previous = SoulEnergy.get(event.getOriginal());
        SoulEnergy.set(event.getEntity(), event.isWasDeath() ? previous / 2 : previous);
    }
}
