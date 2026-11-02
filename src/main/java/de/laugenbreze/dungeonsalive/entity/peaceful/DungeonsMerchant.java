package de.laugenbreze.dungeonsalive.entity.peaceful;

import de.laugenbreze.dungeonsalive.quest.QuestService;
import de.laugenbreze.dungeonsalive.merchant.MerchantServices;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.syncher.*;
import net.minecraft.server.level.*;
import net.minecraft.sounds.*;
import net.minecraft.world.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.trading.*;
import net.minecraft.world.inventory.MerchantMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

/** Camp merchants with normal Minecraft offers and a synchronized greeting gesture. */
public final class DungeonsMerchant extends AbstractVillager {
    public static final String CAMP_TAG = "dungeonsalive_camp_npc";
    private static final EntityDataAccessor<Long> GREETING = SynchedEntityData.defineId(DungeonsMerchant.class,EntityDataSerializers.LONG);
    private final PeacefulProfiles.Profile profile;
    private long nextRestock;
    @Nullable private BlockPos campHome;
    private MerchantRoutine routine = new MerchantRoutine();
    private Vec3 campThreat = Vec3.ZERO;
    @Nullable private ServerPlayer campCustomer;
    @Nullable private MerchantMenu campTradeMenu;
    public DungeonsMerchant(EntityType<? extends DungeonsMerchant> type,Level level,PeacefulProfiles.Profile profile){
        super(type,level);this.profile=profile;setPersistenceRequired();
    }
    public PeacefulProfiles.Profile profile(){return profile;}
    public boolean hasCampRoutine(){return getTags().contains(CAMP_TAG);}
    @Nullable public BlockPos campHome(){return campHome;}
    public MerchantRoutine.Activity campActivity(){return routine.activity();}
    MerchantRoutine routine(){return routine;}
    Vec3 campThreat(){return campThreat;}
    MerchantRoutine.Observation routineObservation(){
        return new MerchantRoutine.Observation(level().getGameTime(), level().getDayTime(), level().isRaining(),
                isTrading(), campHome == null ? 0 : position().distanceToSqr(Vec3.atBottomCenterOf(campHome)), getNavigation().isDone());
    }
    public static AttributeSupplier.Builder createAttributes(){return Mob.createMobAttributes().add(Attributes.MAX_HEALTH,30).add(Attributes.MOVEMENT_SPEED,.25).add(Attributes.FOLLOW_RANGE,24);}
    @Override protected PathNavigation createNavigation(Level level){return new MerchantNavigation(this,level);}
    @Override protected void defineSynchedData(SynchedEntityData.Builder builder){super.defineSynchedData(builder);builder.define(GREETING,-1000L);}
    @Override protected void registerGoals(){
        goalSelector.addGoal(0,new FloatGoal(this));
        goalSelector.addGoal(1,new TradeWithPlayerGoal(this));
        goalSelector.addGoal(2,new LookAtTradingPlayerGoal(this));
        goalSelector.addGoal(2,new de.laugenbreze.dungeonsalive.settlement.inn.InnDoorGoal(this));
        goalSelector.addGoal(3,new de.laugenbreze.dungeonsalive.settlement.inn.InnVisitorGoal(this,
                () -> routine.frightened(level().getGameTime())));
        goalSelector.addGoal(4,new MerchantRoutineGoal(this));
        goalSelector.addGoal(5,new WaterAvoidingRandomStrollGoal(this,.5){
            @Override public boolean canUse(){return !isTrading() && !hasCampRoutine() && super.canUse();}
            @Override public boolean canContinueToUse(){return !isTrading() && !hasCampRoutine() && super.canContinueToUse();}
        });
        goalSelector.addGoal(6,new LookAtPlayerGoal(this,Player.class,8));
        goalSelector.addGoal(7,new RandomLookAroundGoal(this));
    }
    public void beginGreeting(){if(!level().isClientSide) entityData.set(GREETING,level().getGameTime());}
    public float gestureTick(float partialTick){return (float)(level().getGameTime()-entityData.get(GREETING))+partialTick;}
    @Override public InteractionResult mobInteract(Player player,InteractionHand hand){
        if(hand!=InteractionHand.MAIN_HAND || !isAlive() || isTrading()) return super.mobInteract(player,hand);
        if(player instanceof ServerPlayer server){
            if(hasCampRoutine() && routine.frightened(level().getGameTime()) && !player.isShiftKeyDown()) return InteractionResult.FAIL;
            beginGreeting();getNavigation().stop();
            if(player.isShiftKeyDown()) QuestService.open(server);
            else if(!MerchantServices.open(server,this)) openGoodsTrade(server);
        }
        return InteractionResult.sidedSuccess(level().isClientSide);
    }
    /** Used both by direct interaction and the service menu's explicit goods button. */
    public void openGoodsTrade(ServerPlayer player){
        if(!isAlive() || player.level()!=level() || player.distanceToSqr(this)>64
                || isTrading() && getTradingPlayer()!=player) return;
        var previousMenu=player.containerMenu;
        setTradingPlayer(player);getNavigation().stop();openTradingScreen(player,getDisplayName(),1);
        if(hasCampRoutine() && player.containerMenu!=previousMenu && player.containerMenu instanceof MerchantMenu menu){
            campCustomer=player;campTradeMenu=menu;
        }
    }
    @Override public void setTradingPlayer(@Nullable Player player){
        super.setTradingPlayer(player);
        if(player != campCustomer){campCustomer=null;campTradeMenu=null;}
        if(player != null) getNavigation().stop();
    }
    @Override protected void customServerAiStep(){
        super.customServerAiStep();
        if(!hasCampRoutine()) return;
        // This hook is not run by NoAI entities or field-guide previews. Template
        // placement/rotation and save loading have already supplied the real position.
        if(campHome == null) campHome=blockPosition().immutable();
        if(isTrading()){
            routine.decide(routineObservation());
            getNavigation().stop();
        }
    }
    @Override public boolean hurt(net.minecraft.world.damagesource.DamageSource source,float amount){
        float before=getHealth()+getAbsorptionAmount();
        boolean accepted=super.hurt(source,amount);
        if(accepted && !level().isClientSide && getHealth()+getAbsorptionAmount()<before)
            MerchantServices.closeCustomer(this);
        if(accepted && isAlive() && !level().isClientSide && !isNoAi() && hasCampRoutine()
                && getHealth()+getAbsorptionAmount()<before){
            Vec3 origin=source.getSourcePosition();
            campThreat=origin == null ? position() : origin;
            routine.hurt(level().getGameTime());
            // Close only the exact menu opened by this merchant. A stale customer
            // reference must never close a chest or another merchant's interface.
            if(campCustomer != null && campTradeMenu != null && campCustomer.containerMenu == campTradeMenu) campCustomer.closeContainer();
            setTradingPlayer(null);
            getNavigation().stop();
        }
        return accepted;
    }
    @Override protected void updateTrades(){
        if(profile==null) return;
        switch(profile.id()){
            case "blacksmith" -> {offer(5,Items.IRON_PICKAXE,1);offer(4,Items.IRON_AXE,1);offer(18,Items.ANVIL,1);}
            case "enchantsmith" -> {offer(8,Items.EXPERIENCE_BOTTLE,8);offer(5,Items.LAPIS_LAZULI,16);offer(12,Items.BOOKSHELF,4);}
            case "piglin_merchant" -> {offer(4,Items.GOLD_INGOT,8);offer(7,Items.CRYING_OBSIDIAN,4);offer(3,Items.QUARTZ,16);}
            case "luxury_merchant" -> {offer(24,Items.DIAMOND,1);offer(10,Items.GOLDEN_APPLE,1);offer(8,Items.AMETHYST_SHARD,16);}
            case "gift_wrapper" -> {offer(2,Items.BUNDLE,1);offer(1,Items.PAPER,16);offer(2,Items.CHEST,2);}
            case "powersmith" -> {offer(8,Items.IRON_CHESTPLATE,1);offer(6,Items.SHIELD,1);offer(5,Items.IRON_SWORD,1);}
            case "uniquesmith" -> {offer(9,Items.SPYGLASS,1);offer(7,Items.CROSSBOW,1);offer(4,Items.COMPASS,1);}
            case "gildsmith" -> {offer(8,Items.GOLDEN_CARROT,8);offer(7,Items.GOLD_BLOCK,1);offer(8,Items.GOLDEN_BOOTS,1);}
            case "tower_keeper" -> {offer(3,Items.SCAFFOLDING,32);offer(4,Items.LADDER,32);offer(5,Items.CLOCK,1);}
            case "mystery_merchant" -> {offer(4,Items.ENDER_PEARL,2);offer(4,Items.GLOW_INK_SAC,8);offer(3,Items.RABBIT_FOOT,1);}
            default -> {offer(1,Items.BREAD,6);offer(2,Items.LANTERN,4);offer(3,Items.LEAD,2);}
        }
    }
    private void offer(int emeralds,Item result,int amount){getOffers().add(new MerchantOffer(new ItemCost(Items.EMERALD,emeralds),new ItemStack(result,amount),12,2,.05F));}
    @Override protected void rewardTradeXp(MerchantOffer offer){
        beginGreeting();if(level() instanceof ServerLevel server && offer.shouldRewardExp()) server.addFreshEntity(new ExperienceOrb(server,getX(),getY()+.5,getZ(),3));
    }
    @Override public void tick(){
        super.tick();
        if(!level().isClientSide && tickCount%200==0 && !isTrading() && level().getGameTime()>=nextRestock){
            if(offers!=null) offers.forEach(MerchantOffer::resetUses);
            nextRestock=level().getGameTime()+24000;
        }
    }
    @Override public void addAdditionalSaveData(CompoundTag tag){
        super.addAdditionalSaveData(tag);tag.putLong("DungeonsRestock",nextRestock);
        if(campHome != null){
            CompoundTag home=new CompoundTag();
            home.putInt("X",campHome.getX());home.putInt("Y",campHome.getY());home.putInt("Z",campHome.getZ());
            home.putString("Dimension",level().dimension().location().toString());
            tag.put("DungeonsCampHome",home);
        }
    }
    @Override public void readAdditionalSaveData(CompoundTag tag){
        super.readAdditionalSaveData(tag);nextRestock=tag.getLong("DungeonsRestock");
        campHome=null;routine=new MerchantRoutine();campThreat=Vec3.ZERO;campCustomer=null;campTradeMenu=null;
        if(tag.contains("DungeonsCampHome",Tag.TAG_COMPOUND)){
            CompoundTag home=tag.getCompound("DungeonsCampHome");
            if(home.contains("X",Tag.TAG_INT) && home.contains("Y",Tag.TAG_INT) && home.contains("Z",Tag.TAG_INT)
                    && home.getString("Dimension").equals(level().dimension().location().toString())){
                BlockPos position=new BlockPos(home.getInt("X"),home.getInt("Y"),home.getInt("Z"));
                if(position.getY()>=level().getMinBuildHeight() && position.getY()<level().getMaxBuildHeight()
                        && level().getWorldBorder().isWithinBounds(position)) campHome=position;
            }
        }
    }
    @Override @Nullable public AgeableMob getBreedOffspring(ServerLevel level,AgeableMob partner){return null;}
    @Override protected SoundEvent getAmbientSound(){return SoundEvents.VILLAGER_AMBIENT;}
    @Override protected SoundEvent getHurtSound(net.minecraft.world.damagesource.DamageSource source){return SoundEvents.VILLAGER_HURT;}
    @Override protected SoundEvent getDeathSound(){return SoundEvents.VILLAGER_DEATH;}
}
