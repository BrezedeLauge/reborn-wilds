package de.laugenbreze.dungeonsalive.client.inn;

import de.laugenbreze.dungeonsalive.settlement.inn.InnFurnitureContent;
import net.minecraft.client.renderer.entity.NoopRenderer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

/** The real furniture block is the visible seat; the saved attachment has no extra mesh or texture. */
public final class InnFurnitureClient {
    private InnFurnitureClient() { }
    public static void register(IEventBus bus) { bus.addListener(InnFurnitureClient::renderers); }
    private static void renderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(InnFurnitureContent.SEAT.get(), NoopRenderer::new);
    }
}
