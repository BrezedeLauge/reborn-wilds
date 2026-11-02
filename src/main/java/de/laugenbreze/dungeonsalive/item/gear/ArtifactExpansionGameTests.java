package de.laugenbreze.dungeonsalive.item.gear;

import de.laugenbreze.dungeonsalive.DungeonsAlive;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.gametest.framework.AfterBatch;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

/** Server resources, real menu-independent item use and actual vanilla bow projectiles. */
@GameTestHolder(DungeonsAlive.MOD_ID)
@PrefixGameTestTemplate(false)
public final class ArtifactExpansionGameTests {
    private static final String BATCH = "artifact_expansion";
    private static final Map<ServerLevel, List<Entity>> PROJECTILES = new IdentityHashMap<>();

    private ArtifactExpansionGameTests() {}

    @AfterBatch(batch = BATCH)
    public static void removeProjectiles(ServerLevel level) {
        List<Entity> entities = PROJECTILES.remove(level);
        if (entities != null) entities.forEach(Entity::discard);
    }

    @GameTest(template = "gametest_empty", templateNamespace = DungeonsAlive.MOD_ID, batch = BATCH, timeoutTicks = 20)
    public static void soulHealerUsesServerSoulsAndRejectsWasteAndRepeatedUse(GameTestHelper helper) {
        Player owner = player(helper);
        var healer = GearContent.artifact(ArtifactKind.SOUL_HEALER);
        ItemStack stack = new ItemStack(healer);
        owner.setItemInHand(InteractionHand.MAIN_HAND, stack);
        SoulEnergy.set(owner, 30);
        helper.assertTrue(healer.use(helper.getLevel(), owner, InteractionHand.MAIN_HAND).getResult() == InteractionResult.FAIL,
                "Full health should not waste a soul-healer activation");
        helper.assertTrue(SoulEnergy.get(owner) == 30 && !owner.getCooldowns().isOnCooldown(healer),
                "Rejected full-health healing spent souls or started its cooldown");
        owner.setHealth(10);
        SoulEnergy.set(owner, 0);
        CustomData.update(DataComponents.CUSTOM_DATA, stack, tag -> tag.putInt("dungeonsalive_souls_display", 100));
        helper.assertTrue(healer.use(helper.getLevel(), owner, InteractionHand.MAIN_HAND).getResult() == InteractionResult.FAIL
                        && owner.getHealth() == 10, "A forged display financed healing");
        SoulEnergy.set(owner, 30);
        helper.assertTrue(healer.use(helper.getLevel(), owner, InteractionHand.MAIN_HAND).getResult().consumesAction(), "Funded soul healer failed");
        helper.assertTrue(owner.getHealth() == 18 && SoulEnergy.get(owner) == 15 && owner.getCooldowns().isOnCooldown(healer),
                "Soul healer did not heal eight points and charge exactly fifteen souls");
        helper.assertTrue(healer.use(helper.getLevel(), owner, InteractionHand.MAIN_HAND).getResult() == InteractionResult.FAIL
                        && owner.getHealth() == 18 && SoulEnergy.get(owner) == 15, "Repeated use bypassed the healer cooldown");
        helper.succeed();
    }

    @GameTest(template = "gametest_empty", templateNamespace = DungeonsAlive.MOD_ID, batch = BATCH, timeoutTicks = 20)
    public static void ghostCloakHasShortBuffsAndStillCollidesWithPlayerBlocks(GameTestHelper helper) {
        Player owner = player(helper);
        var cloak = GearContent.artifact(ArtifactKind.GHOST_CLOAK);
        owner.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(cloak));
        for (int y = 1; y <= 3; y++) for (int z = 3; z <= 7; z++) helper.setBlock(new BlockPos(5, y, z), Blocks.STONE);
        helper.assertTrue(cloak.use(helper.getLevel(), owner, InteractionHand.MAIN_HAND).getResult().consumesAction(), "Ghost cloak failed");
        var speed = owner.getEffect(MobEffects.MOVEMENT_SPEED);
        var resistance = owner.getEffect(MobEffects.DAMAGE_RESISTANCE);
        helper.assertTrue(speed != null && speed.getDuration() == 60 && speed.getAmplifier() == 1
                        && resistance != null && resistance.getDuration() == 60 && resistance.getAmplifier() == 0,
                "Ghost cloak did not apply its bounded speed/resistance effects");
        helper.assertTrue(cloak.use(helper.getLevel(), owner, InteractionHand.MAIN_HAND).getResult() == InteractionResult.FAIL,
                "Cloak reuse bypassed its cooldown");
        double wallX = helper.absolutePos(new BlockPos(5, 1, 5)).getX();
        owner.move(MoverType.SELF, new Vec3(4, 0, 0));
        helper.assertTrue(owner.getBoundingBox().maxX <= wallX + .001 && !owner.noPhysics,
                "Ghost cloak bypassed the ordinary solid-block collision");
        helper.assertTrue(helper.getBlockState(new BlockPos(5, 1, 5)).is(Blocks.STONE), "Ghost cloak changed a player-built wall");
        helper.succeed();
    }

    @GameTest(template = "gametest_empty", templateNamespace = DungeonsAlive.MOD_ID, batch = BATCH, timeoutTicks = 30)
    public static void flamingQuiverFundsSevenRealArrowsAndNeverRechargesReloadedShots(GameTestHelper helper) {
        Player owner = player(helper);
        // Rain legitimately extinguishes vanilla burning arrows; shelter this
        // resource-accounting fixture without changing the world's weather.
        for (int x = 0; x < 20; x++) for (int z = 0; z < 12; z++) helper.setBlock(new BlockPos(x, 4, z), Blocks.GLASS);
        var quiver = GearContent.artifact(ArtifactKind.FLAMING_QUIVER);
        owner.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(quiver));
        helper.assertTrue(quiver.use(helper.getLevel(), owner, InteractionHand.MAIN_HAND).getResult().consumesAction(), "Flaming quiver failed");
        helper.assertTrue(ArtifactExpansionState.quiverCharges(owner) == 7
                        && quiver.use(helper.getLevel(), owner, InteractionHand.MAIN_HAND).getResult() == InteractionResult.FAIL,
                "Quiver capacity or repeat-use cooldown is wrong");
        owner.getInventory().setItem(8, new ItemStack(Items.ARROW, 16));
        owner.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.BOW));
        Arrow first = shoot(helper, owner);
        Player[] restoredOwner = new Player[1];
        Arrow[] restoredArrow = new Arrow[1];
        Arrow[] legacyArrow = new Arrow[1];
        List<Arrow> subsequent = new ArrayList<>();
        helper.runAtTickTime(2, () -> {
            helper.assertTrue(first.isOnFire() && ArtifactExpansionState.quiverCharges(owner) == 6,
                    "A real accepted bow projectile did not spend exactly one cast charge");
            CompoundTag playerSave = new CompoundTag();
            owner.saveWithoutId(playerSave);
            Player loaded = helper.makeMockPlayer(GameType.SURVIVAL);
            loaded.load(playerSave);
            restoredOwner[0] = loaded;
            helper.assertTrue(ArtifactExpansionState.quiverCharges(loaded) == 6, "Full player NBT reload lost cast quiver charges");
            CompoundTag arrowSave = new CompoundTag();
            first.saveWithoutId(arrowSave);
            first.discard();
            arrowSave.remove("UUID");
            var arrow = EntityType.ARROW.create(helper.getLevel());
            helper.assertTrue(arrow != null, "Arrow factory disappeared");
            arrow.load(arrowSave);
            arrow.setOwner(loaded); // Mock players are unregistered; preserve a resolvable test owner.
            helper.assertTrue(helper.getLevel().addFreshEntity(arrow), "Could not add the NBT-restored projectile");
            track(helper, arrow);
            restoredArrow[0] = arrow;

            var cancelled = EntityType.ARROW.create(helper.getLevel());
            cancelled.setOwner(loaded);
            var cancelledJoin = new EntityJoinLevelEvent(cancelled, helper.getLevel());
            cancelledJoin.setCanceled(true);
            ArtifactExpansionEvents.projectileJoined(cancelledJoin);
            ArtifactExpansionEvents.projectileTick(new EntityTickEvent.Pre(cancelled));
            helper.assertTrue(ArtifactExpansionState.quiverCharges(loaded) == 6 && !cancelled.isOnFire(),
                    "The explicitly cancelled-spawn event fixture spent a quiver charge");
            var legacy = EntityType.ARROW.create(helper.getLevel());
            helper.assertTrue(legacy != null, "Legacy arrow fixture could not be created");
            legacy.setOwner(loaded);
            legacy.setPos(helper.absoluteVec(new Vec3(3.5, 2, 9.5)));
            // Model the documented disk-load callback for an older arrow with no
            // quiver markers, then let its actual server entity tick normally.
            ArtifactExpansionEvents.projectileJoined(new EntityJoinLevelEvent(legacy, helper.getLevel(), true));
            helper.assertTrue(helper.getLevel().addFreshEntity(legacy), "Could not add the old unmarked arrow fixture");
            track(helper, legacy);
            legacyArrow[0] = legacy;
            for (int index = 0; index < 7; index++) subsequent.add(shoot(helper, loaded));
        });
        helper.runAtTickTime(5, () -> {
            Player loaded = restoredOwner[0];
            helper.assertTrue(ArtifactExpansionState.quiverCharges(loaded) == 0,
                    "Seven additional arrows did not exhaust the six remaining charges");
            helper.assertTrue(subsequent.stream().filter(Arrow::isOnFire).count() == 6 && restoredArrow[0].isOnFire(),
                    "Reload rebilled an old arrow or the seventh unfunded new arrow caught fire");
            helper.assertFalse(legacyArrow[0].isOnFire(), "Loading an older unmarked arrow applied a new cast charge retroactively");
            ItemStack forged = new ItemStack(quiver);
            CustomData.update(DataComponents.CUSTOM_DATA, forged, tag -> tag.putInt("dungeonsalive_flame_charges_display", 7));
            loaded.setItemInHand(InteractionHand.MAIN_HAND, forged);
            helper.assertFalse(ArtifactExpansionState.consumeArrow(loaded), "A forged quiver display granted a server charge");
            var livingClone = helper.makeMockPlayer(GameType.SURVIVAL);
            ArtifactExpansionEvents.clonePlayer(new PlayerEvent.Clone(livingClone, owner, false));
            helper.assertTrue(ArtifactExpansionState.quiverCharges(livingClone) == 6, "Non-death player cloning lost cast charges");
            var deathClone = helper.makeMockPlayer(GameType.SURVIVAL);
            ArtifactExpansionEvents.clonePlayer(new PlayerEvent.Clone(deathClone, owner, true));
            helper.assertTrue(ArtifactExpansionState.quiverCharges(deathClone) == 0, "Death duplicated remaining quiver charges");
            helper.succeed();
        });
    }

    private static Player player(GameTestHelper helper) {
        for (int x = 0; x < 20; x++) for (int z = 0; z < 12; z++) helper.setBlock(new BlockPos(x, 0, z), Blocks.STONE);
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setPos(helper.absoluteVec(new Vec3(3.5, 1, 5.5)));
        player.setYRot(-90);
        player.setXRot(0);
        return player;
    }

    private static Arrow shoot(GameTestHelper helper, Player owner) {
        Set<UUID> before = helper.getLevel().getEntitiesOfClass(Arrow.class, owner.getBoundingBox().inflate(4), arrow -> arrow.getOwner() == owner)
                .stream().map(Entity::getUUID).collect(Collectors.toSet());
        ItemStack bow = owner.getMainHandItem();
        helper.assertTrue(bow.getItem() instanceof BowItem, "Projectile fixture is not holding a real bow");
        var item = (BowItem) bow.getItem();
        helper.assertTrue(item.use(helper.getLevel(), owner, InteractionHand.MAIN_HAND).getResult().consumesAction(),
                "Actual vanilla bow use did not begin drawing");
        item.releaseUsing(bow, helper.getLevel(), owner, item.getUseDuration(bow, owner) - 20);
        owner.stopUsingItem();
        List<Arrow> fresh = helper.getLevel().getEntitiesOfClass(Arrow.class, owner.getBoundingBox().inflate(4),
                arrow -> arrow.getOwner() == owner && !before.contains(arrow.getUUID()));
        fresh.forEach(arrow -> track(helper, arrow));
        helper.assertTrue(fresh.size() == 1, "Actual vanilla bow release did not create exactly one new owned arrow");
        return fresh.getFirst();
    }

    private static void track(GameTestHelper helper, Entity entity) {
        PROJECTILES.computeIfAbsent(helper.getLevel(), ignored -> new ArrayList<>()).add(entity);
    }
}
