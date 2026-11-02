package de.laugenbreze.dungeonsalive.client.entity;
import de.laugenbreze.dungeonsalive.entity.AttackKind;
import de.laugenbreze.dungeonsalive.entity.peaceful.*;
import de.laugenbreze.dungeonsalive.client.entity.reference.*;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

public final class PeacefulRenderers {
    private static final ReferenceAnimationSource<DungeonsMerchant> MERCHANT_ANIMATION=new ReferenceAnimationSource<>(){
        @Override public AttackKind action(DungeonsMerchant mob){return null;}
        @Override public float actionTicks(DungeonsMerchant mob,float partialTick){return 0;}
        @Override public Gesture gesture(DungeonsMerchant mob,float partialTick){
            float tick=mob.gestureTick(partialTick);return tick>=0 && tick<=30?new Gesture("GREETING",tick,30):null;
        }
    };
    public static void register(IEventBus bus){bus.addListener(PeacefulRenderers::renderers);}
    private static void renderers(EntityRenderersEvent.RegisterRenderers event){
        for(var p:PeacefulProfiles.all()){
            var def=ReferenceModels.get(p.id());
            if(def==null)throw new IllegalStateException("Peaceful entity has no reviewed visual: "+p.id());
            if(p.kind()==PeacefulProfiles.Kind.MERCHANT)event.registerEntityRenderer(ModPeacefulEntities.merchantType(p.id()),context->new ReferenceMobRenderer<>(context,def,MERCHANT_ANIMATION));
            else if(p.kind()==PeacefulProfiles.Kind.CIVILIAN)event.registerEntityRenderer(ModPeacefulEntities.creatureType(p.id()),context->new ReferenceMobRenderer<>(context,def,CivilianAnimationSource.INSTANCE));
            else event.registerEntityRenderer(ModPeacefulEntities.creatureType(p.id()),context->new ReferenceMobRenderer<>(context,def,ReferenceAnimationSource.passive()));
        }
    }
    private PeacefulRenderers(){}
}
