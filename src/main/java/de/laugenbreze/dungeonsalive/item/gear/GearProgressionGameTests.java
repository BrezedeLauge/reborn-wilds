package de.laugenbreze.dungeonsalive.item.gear;

import de.laugenbreze.dungeonsalive.DungeonsAlive;
import de.laugenbreze.dungeonsalive.merchant.GearUpgrade;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.core.component.DataComponents;
import net.minecraft.gametest.framework.AfterBatch;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.GameType;
import net.minecraft.world.phys.Vec3;

import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

@GameTestHolder(DungeonsAlive.MOD_ID)
@PrefixGameTestTemplate(false)
public final class GearProgressionGameTests {
    private static final String BATCH = "gear_progression";
    private static final Map<ServerLevel, List<Entity>> ACTORS = new IdentityHashMap<>();
    private GearProgressionGameTests() {}
    @AfterBatch(batch = BATCH)
    public static void cleanup(ServerLevel level) {
        List<Entity> actors = ACTORS.remove(level);
        if (actors != null) actors.forEach(Entity::discard);
    }

    @GameTest(template = "gametest_empty", templateNamespace = DungeonsAlive.MOD_ID, batch = BATCH, timeoutTicks = 20)
    public static void powerRanksChangeActualMeleeDamageButNotPassiveOrMagicDamage(GameTestHelper helper) {
        Player player = player(helper);
        ItemStack upgraded = maxRank(new ItemStack(GearContent.weapon(WeaponKind.FIGHTERS_BINDINGS)));
        player.setItemInHand(InteractionHand.MAIN_HAND, upgraded);
        var hostile = helper.spawnWithNoFreeWill(EntityType.SILVERFISH, new Vec3(7.5, 1, 5.5));
        var passive = helper.spawnWithNoFreeWill(EntityType.COW, new Vec3(10.5, 1, 5.5));
        var magic = helper.spawnWithNoFreeWill(EntityType.SILVERFISH, new Vec3(13.5, 1, 5.5));
        float before = hostile.getHealth();
        hostile.hurt(player.damageSources().playerAttack(player), 2);
        helper.assertTrue(Math.abs(before - hostile.getHealth() - 2.3F) < .001F, "The real melee event ignored the paid weapon rank");
        before = passive.getHealth(); passive.hurt(player.damageSources().playerAttack(player), 2);
        helper.assertTrue(Math.abs(before - passive.getHealth() - 2) < .001F, "Power ranks gave a special bonus against a passive animal");
        before = magic.getHealth(); magic.hurt(player.damageSources().indirectMagic(player, player), 2);
        helper.assertTrue(Math.abs(before - magic.getHealth() - 2) < .001F, "Weapon ranks recursively amplified an artifact-like source");
        helper.succeed();
    }

    @GameTest(template = "gametest_empty", templateNamespace = DungeonsAlive.MOD_ID, batch = BATCH, timeoutTicks = 20)
    public static void aRealArrowKeepsItsLaunchRankAcrossWeaponSwitchAndSave(GameTestHelper helper) {
        Player player = player(helper);
        ItemStack bow = maxRank(new ItemStack(GearContent.ranged(RangedWeaponKind.POWER_BOW)));
        player.setItemInHand(InteractionHand.MAIN_HAND, bow);
        player.getInventory().setItem(8, new ItemStack(Items.ARROW, 2));
        var item = (DungeonsBowItem) bow.getItem();
        helper.assertTrue(item.use(helper.getLevel(), player, InteractionHand.MAIN_HAND).getResult().consumesAction(), "Upgraded bow could not start drawing");
        item.releaseUsing(bow, helper.getLevel(), player, item.getUseDuration(bow, player) - 20);
        player.stopUsingItem();
        List<AbstractArrow> arrows = helper.getLevel().getEntitiesOfClass(AbstractArrow.class,
                player.getBoundingBox().inflate(4), arrow -> arrow.getOwner() == player);
        ACTORS.computeIfAbsent(helper.getLevel(), ignored -> new ArrayList<>()).addAll(arrows);
        helper.assertTrue(arrows.size() == 1 && player.getInventory().countItem(Items.ARROW) == 1, "Real ranked bow duplicated or omitted ammunition");
        AbstractArrow arrow = arrows.getFirst();
        helper.assertTrue(Math.abs(arrow.getBaseDamage() - 2 * 1.35 * 1.15) < .0001, "Arrow did not snapshot both base weapon and rank multipliers exactly once");
        player.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
        CompoundTag saved = new CompoundTag(); arrow.saveWithoutId(saved);
        var restored = EntityType.ARROW.create(helper.getLevel());
        helper.assertTrue(restored != null, "Missing real arrow factory");
        restored.load(saved);
        helper.assertTrue(Math.abs(restored.getBaseDamage() - arrow.getBaseDamage()) < .0001, "Arrow reload replaced the launch rank with current held equipment");
        helper.succeed();
    }

    @GameTest(template = "gametest_empty", templateNamespace = DungeonsAlive.MOD_ID, batch = BATCH, timeoutTicks = 20)
    public static void hudReadsServerAccountsAndKeepsActiveQuiverVisibleAfterStowing(GameTestHelper helper) {
        Player player = player(helper);
        ItemStack artifact = new ItemStack(GearContent.artifact(ArtifactKind.SOUL_HEALER));
        player.setItemInHand(InteractionHand.MAIN_HAND, artifact);
        SoulEnergy.set(player, 20);
        ArtifactExpansionState.fillQuiver(player);
        CustomData.update(DataComponents.CUSTOM_DATA, artifact, tag -> {
            tag.putInt("dungeonsalive_souls_display", 100);
            tag.putInt("dungeonsalive_flame_charges_display", 99);
        });
        var display = GearHudEvents.snapshot(player);
        helper.assertTrue(display.souls() == 20 && display.flameCharges() == 7 && display.showSouls() && display.showFlames(),
                "A forged item tooltip affected the server HUD snapshot");
        player.getInventory().clearContent(); ArtifactExpansionState.consumeArrow(player);
        display = GearHudEvents.snapshot(player);
        helper.assertTrue(!display.showSouls() && display.showFlames() && display.flameCharges() == 6,
                "Stowing gear hid cast charges or displayed a soul artifact that is no longer carried");
        var replacement = helper.makeMockPlayer(GameType.SURVIVAL);
        ArtifactExpansionState.copyOnClone(player, replacement, true);
        display = GearHudEvents.snapshot(replacement);
        helper.assertTrue(!display.showFlames() && !display.showSouls(), "Dead-player state left the resource HUD active without gear");
        helper.succeed();
    }

    @GameTest(template = "gametest_empty", templateNamespace = DungeonsAlive.MOD_ID, batch = BATCH, timeoutTicks = 20)
    public static void soulKnifeCollectsRealSoulsWithoutAnArtifactButCannotFarmSummons(GameTestHelper helper) {
        Player player = player(helper);
        player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(GearContent.weapon(WeaponKind.SOUL_KNIFE)));
        SoulEnergy.set(player, 0);
        helper.assertTrue(!SoulEnergy.carriesSoulArtifact(player) && GearHudEvents.snapshot(player).showSouls(),
                "The soul knife did not act as its own server-account collector");
        var enemy = helper.spawnWithNoFreeWill(EntityType.SILVERFISH, new Vec3(7.5, 1, 5.5));
        enemy.hurt(player.damageSources().playerAttack(player), 1000);
        helper.assertTrue(SoulEnergy.get(player) == 5, "Direct soul-knife kill must supply 3 ordinary plus 2 bonus souls");
        enemy = helper.spawnWithNoFreeWill(EntityType.SILVERFISH, new Vec3(10.5, 1, 5.5));
        enemy.hurt(player.damageSources().indirectMagic(player, player), 1000);
        helper.assertTrue(SoulEnergy.get(player) == 8, "An indirect kill received the direct-weapon bonus");
        var temporary = helper.spawnWithNoFreeWill(de.laugenbreze.dungeonsalive.entity.ModEntities.type("frozen_zombie"),
                new Vec3(13.5, 1, 5.5));
        CompoundTag saved = new CompoundTag(); temporary.saveWithoutId(saved);
        saved.putUUID("DungeonsSummoner", java.util.UUID.randomUUID());
        saved.putLong("DungeonsSummonExpiresAt", helper.getLevel().getGameTime() + 600);
        temporary.load(saved);
        helper.assertTrue(temporary.isSummoned(), "Fixture did not preserve its reinforcement state");
        temporary.hurt(player.damageSources().playerAttack(player), 1000);
        var cow = helper.spawnWithNoFreeWill(EntityType.COW, new Vec3(16.5, 1, 5.5));
        cow.hurt(player.damageSources().playerAttack(player), 1000);
        helper.assertTrue(SoulEnergy.get(player) == 8, "A summon or passive animal supplied soul-knife rewards");
        SoulEnergy.set(player, 99);
        enemy = helper.spawnWithNoFreeWill(EntityType.SILVERFISH, new Vec3(19.5, 1, 5.5));
        enemy.hurt(player.damageSources().playerAttack(player), 1000);
        helper.assertTrue(SoulEnergy.get(player) == 100, "The soul-knife bonus bypassed the player account cap");
        helper.succeed();
    }

    private static Player player(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setPos(helper.absoluteVec(new Vec3(3.5, 1, 5.5)));
        player.setYRot(-90); player.setXRot(0);
        return player;
    }
    private static ItemStack maxRank(ItemStack stack) {
        return GearUpgrade.upgrade(GearUpgrade.upgrade(GearUpgrade.upgrade(stack)));
    }
}
