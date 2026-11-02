package de.laugenbreze.dungeonsalive.item.gear;

import de.laugenbreze.dungeonsalive.DungeonsAlive;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

@GameTestHolder(DungeonsAlive.MOD_ID)
@PrefixGameTestTemplate(false)
public final class GearGameTests {
    private GearGameTests() {}

    private static Player player(GameTestHelper helper) {
        for (int x = 0; x <= 14; x++) for (int z = 0; z <= 14; z++)
            helper.setBlock(new BlockPos(x, 0, z), Blocks.STONE);
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        Vec3 at = helper.absoluteVec(new Vec3(3.5, 1, 5.5));
        player.setPos(at.x, at.y, at.z);
        player.setYRot(-90); player.setXRot(0);
        return player;
    }

    @GameTest(template="gametest_empty", templateNamespace=DungeonsAlive.MOD_ID, timeoutTicks=40, batch="gear_cooldowns")
    public static void artifactCooldownAndServerSoulAccount(GameTestHelper helper) {
        Player owner = player(helper);
        var boots = GearContent.artifact(ArtifactKind.BOOTS_OF_SWIFTNESS);
        owner.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(boots));
        helper.assertTrue(boots.use(helper.getLevel(), owner, InteractionHand.MAIN_HAND).getResult().consumesAction(), "Boots must activate");
        helper.assertTrue(owner.hasEffect(MobEffects.MOVEMENT_SPEED), "Boots must apply speed on the server");
        helper.assertTrue(boots.use(helper.getLevel(), owner, InteractionHand.MAIN_HAND).getResult() == InteractionResult.FAIL,
                "Repeated use must respect the item cooldown");
        var harvester = GearContent.artifact(ArtifactKind.HARVESTER);
        ItemStack forged = new ItemStack(harvester);
        CustomData.update(DataComponents.CUSTOM_DATA, forged, tag -> tag.putInt("dungeonsalive_souls_display", 100));
        owner.setItemInHand(InteractionHand.MAIN_HAND, forged);
        helper.assertTrue(harvester.use(helper.getLevel(), owner, InteractionHand.MAIN_HAND).getResult() == InteractionResult.FAIL,
                "An item display component must never grant server-owned souls");
        SoulEnergy.set(owner, 200);
        helper.assertTrue(SoulEnergy.get(owner) == 100, "Soul account must be capped");
        helper.assertTrue(harvester.use(helper.getLevel(), owner, InteractionHand.MAIN_HAND).getResult().consumesAction(), "Funded harvester must activate");
        helper.assertTrue(SoulEnergy.get(owner) == 80, "Harvester must charge exactly 20 souls once");
        harvester.use(helper.getLevel(), owner, InteractionHand.MAIN_HAND);
        helper.assertTrue(SoulEnergy.get(owner) == 80, "Rejected repeat must not charge souls");
        helper.succeed();
    }

    @GameTest(template="gametest_empty", templateNamespace=DungeonsAlive.MOD_ID, timeoutTicks=40, batch="gear_targets")
    public static void gongRespectsCoverAndPassiveCreatures(GameTestHelper helper) {
        Player owner = player(helper);
        var open = helper.spawnWithNoFreeWill(EntityType.ZOMBIE, new Vec3(5.5, 1, 5.5));
        var covered = helper.spawnWithNoFreeWill(EntityType.ZOMBIE, new Vec3(3.5, 1, 9.5));
        var cow = helper.spawnWithNoFreeWill(EntityType.COW, new Vec3(3.5, 1, 3.5));
        for (int x=1;x<=6;x++) for (int y=1;y<=4;y++) helper.setBlock(new BlockPos(x,y,7),Blocks.STONE);
        var gong = GearContent.artifact(ArtifactKind.GONG_OF_WEAKENING);
        owner.setItemInHand(InteractionHand.MAIN_HAND,new ItemStack(gong));
        gong.use(helper.getLevel(),owner,InteractionHand.MAIN_HAND);
        helper.assertTrue(open.hasEffect(MobEffects.WEAKNESS),"Visible hostile must be marked");
        helper.assertTrue(!covered.hasEffect(MobEffects.WEAKNESS),"Solid cover must stop the gong");
        helper.assertTrue(!cow.hasEffect(MobEffects.WEAKNESS),"Passive creatures must be untouched");
        helper.assertTrue(open.getPersistentData().getLong(GearEvents.GONG_UNTIL)>helper.getLevel().getGameTime(),"Vulnerability requires a bounded server expiry");
        helper.succeed();
    }

    @GameTest(template="gametest_empty", templateNamespace=DungeonsAlive.MOD_ID, timeoutTicks=40, batch="gear_beam")
    public static void beamStopsAtBlocksAndChargesSouls(GameTestHelper helper) {
        Player owner=player(helper);
        var front=helper.spawnWithNoFreeWill(EntityType.ZOMBIE,new Vec3(6.5,1,5.5));
        var behind=helper.spawnWithNoFreeWill(EntityType.ZOMBIE,new Vec3(10.0,1,5.5));
        var cow=helper.spawnWithNoFreeWill(EntityType.COW,new Vec3(7.5,1,5.5));
        for(int y=1;y<=4;y++) helper.setBlock(new BlockPos(9,y,5),Blocks.GLASS_PANE);
        var beam=GearContent.artifact(ArtifactKind.CORRUPTED_BEACON);
        ItemStack stack=new ItemStack(beam);
        owner.setItemInHand(InteractionHand.MAIN_HAND,stack);
        SoulEnergy.set(owner,12);
        beam.use(helper.getLevel(),owner,InteractionHand.MAIN_HAND);
        float before=front.getHealth(), blocked=behind.getHealth(), passive=cow.getHealth();
        beam.onUseTick(helper.getLevel(),owner,stack,72000);
        helper.assertTrue(front.getHealth()<before,"Beam must damage an exposed hostile");
        helper.assertTrue(behind.getHealth()==blocked,"Beam must stop at solid cover");
        helper.assertTrue(cow.getHealth()==passive,"Beam must not hit passive creatures");
        helper.assertTrue(SoulEnergy.get(owner)==9,"A beam pulse costs exactly three souls");
        helper.assertTrue(helper.getBlockState(new BlockPos(9,2,5)).is(Blocks.GLASS_PANE),"Beam must not break blocks");
        SoulEnergy.set(owner,0);
        float exhausted=front.getHealth();
        beam.onUseTick(helper.getLevel(),owner,stack,71990);
        helper.assertTrue(front.getHealth()==exhausted && owner.getCooldowns().isOnCooldown(beam),"Exhausted beam must stop and enter cooldown");
        helper.succeed();
    }

    @GameTest(template="gametest_empty", templateNamespace=DungeonsAlive.MOD_ID, timeoutTicks=80, batch="gear_spell_cover")
    public static void spellWallsAndHotbarStopCannotBypassBeamRules(GameTestHelper helper) {
        Player owner=player(helper);
        var caster=de.laugenbreze.dungeonsalive.entity.ModEntities.type("geomancer").create(helper.getLevel());
        Vec3 at=helper.absoluteVec(new Vec3(8.5,1,10.5));
        caster.moveTo(at.x,at.y,at.z);caster.setNoAi(true);helper.getLevel().addFreshEntity(caster);
        var wallTarget=helper.spawnWithNoFreeWill(EntityType.IRON_GOLEM,new Vec3(12.5,1,10.5));
        var behind=helper.spawnWithNoFreeWill(EntityType.ZOMBIE,new Vec3(11.5,1,5.5));
        var wall=de.laugenbreze.dungeonsalive.entity.construct.ConstructService.spawn(caster,wallTarget,
                de.laugenbreze.dungeonsalive.entity.construct.ConstructKind.GEOMANCER_WALL,
                helper.absoluteVec(new Vec3(8.5,1,5.5)),0);
        helper.assertTrue(wall!=null,"Test spell wall must be placed on the supported floor");
        helper.runAtTickTime(30,()->{
            helper.assertTrue(wall.blocksMovement(),"Wall must be in its actual active phase");
            var beam=GearContent.artifact(ArtifactKind.CORRUPTED_BEACON);
            ItemStack stack=new ItemStack(beam);owner.setItemInHand(InteractionHand.MAIN_HAND,stack);SoulEnergy.set(owner,30);
            beam.use(helper.getLevel(),owner,InteractionHand.MAIN_HAND);
            float health=behind.getHealth();beam.onUseTick(helper.getLevel(),owner,stack,72000);
            helper.assertTrue(behind.getHealth()==health,"A server-owned spell wall must stop an artifact beam");
            owner.stopUsingItem();
            helper.assertTrue(owner.getCooldowns().isOnCooldown(beam),"Stopping use as on a hotbar change must apply the beam cooldown");
            helper.assertTrue(beam.use(helper.getLevel(),owner,InteractionHand.MAIN_HAND).getResult()==InteractionResult.FAIL,
                    "A new use cannot restart the first pulse during cooldown");
            helper.assertTrue(SoulEnergy.get(owner)==27,"Rejected restart must not charge another pulse");
            helper.succeed();
        });
    }

    @GameTest(template="gametest_empty", templateNamespace=DungeonsAlive.MOD_ID, timeoutTicks=40, batch="gear_weapons")
    public static void signatureWeaponsAndSoulEarningStayBounded(GameTestHelper helper) {
        Player owner=player(helper);
        owner.setHealth(10);
        var sun=GearContent.weapon(WeaponKind.SUNS_GRACE);
        ItemStack stack=new ItemStack(sun);
        owner.setItemInHand(InteractionHand.MAIN_HAND,stack);
        var enemy=helper.spawnWithNoFreeWill(EntityType.ZOMBIE,new Vec3(5.5,1,5.5));
        sun.postHurtEnemy(stack,enemy,owner);
        helper.assertTrue(owner.getHealth()==12,"Sun's Grace heals one heart on a hostile hit");
        sun.postHurtEnemy(stack,enemy,owner);
        helper.assertTrue(owner.getHealth()==12,"Rapid repeated callbacks cannot bypass the effect cooldown");
        owner.setItemInHand(InteractionHand.MAIN_HAND,new ItemStack(GearContent.artifact(ArtifactKind.HARVESTER)));
        SoulEnergy.set(owner,0);
        enemy.hurt(owner.damageSources().playerAttack(owner),1000);
        helper.assertTrue(SoulEnergy.get(owner)==3,"A credited hostile kill supplies three souls");
        var cow=helper.spawnWithNoFreeWill(EntityType.COW,new Vec3(5.5,1,8.5));
        cow.hurt(owner.damageSources().playerAttack(owner),1000);
        helper.assertTrue(SoulEnergy.get(owner)==3,"Passive kills never supply souls");
        helper.succeed();
    }
}
