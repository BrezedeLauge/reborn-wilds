package de.laugenbreze.dungeonsalive.entity.peaceful;

import de.laugenbreze.dungeonsalive.item.gear.ArtifactKind;
import de.laugenbreze.dungeonsalive.item.gear.GearContent;
import de.laugenbreze.dungeonsalive.item.gear.RangedWeaponKind;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameRules;

final class PiggyBankTreasure {
    private PiggyBankTreasure() {}

    /** Called inside vanilla/NeoForge's captured death-loot pass, after accepted LivingDeath. */
    static void drop(DungeonsCreature pig, DamageSource source, PiggyBankTreasureState treasure) {
        if (!(pig.level() instanceof ServerLevel level)) return;
        treasure.prepare(pig.getUUID());
        if (!treasure.spend() || pig.ownerId() != null || !(source.getEntity() instanceof Player player)
                || player.isSpectator() || player.level() != level || !level.getGameRules().getBoolean(GameRules.RULE_DOMOBLOOT)
                || !PiggyBankSavedData.get(level.getServer()).claim(pig.getUUID())) return;
        // spawnAtLocation participates in the enclosing captureDrops list. LivingDropsEvent
        // may still suppress the two real ItemEntities; neither case can retry the claim.
        pig.spawnAtLocation(new ItemStack(Items.EMERALD, treasure.emeralds()));
        pig.spawnAtLocation(new ItemStack(gear(treasure.gearIndex())));
    }

    private static Item gear(int index) {
        return switch (index) {
            case 0 -> GearContent.ranged(RangedWeaponKind.POWER_BOW);
            case 1 -> GearContent.artifact(ArtifactKind.BOOTS_OF_SWIFTNESS);
            case 2 -> GearContent.artifact(ArtifactKind.IRON_HIDE_AMULET);
            case 3 -> GearContent.artifact(ArtifactKind.DEATH_CAP_MUSHROOM);
            default -> throw new IllegalArgumentException("Invalid bounded Piggy Bank gear index: " + index);
        };
    }
}
