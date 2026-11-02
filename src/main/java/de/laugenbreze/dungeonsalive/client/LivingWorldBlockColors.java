package de.laugenbreze.dungeonsalive.client;

import de.laugenbreze.dungeonsalive.world.LivingWorldBlocks;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;

/** Own material tints over installed Minecraft texture references; no copied game textures. */
final class LivingWorldBlockColors {
    static void register(IEventBus bus) { bus.addListener(LivingWorldBlockColors::blocks); bus.addListener(LivingWorldBlockColors::items); }
    private static void blocks(RegisterColorHandlersEvent.Block event) {
        event.register((state, level, pos, tint) -> 0xE0BC58, LivingWorldBlocks.AMBER_MOSS.get());
        event.register((state, level, pos, tint) -> 0x6BEBD1, LivingWorldBlocks.LUMINOUS_MOSS.get());
        event.register((state, level, pos, tint) -> 0xB4C2B9, LivingWorldBlocks.ANCIENT_BRICKS.get());
        event.register((state, level, pos, tint) -> 0xC9B780, LivingWorldBlocks.CARVED_ANCIENT_BRICKS.get());
    }
    private static void items(RegisterColorHandlersEvent.Item event) {
        event.register((stack, tint) -> 0xE0BC58, LivingWorldBlocks.AMBER_MOSS_ITEM.get());
        event.register((stack, tint) -> 0x6BEBD1, LivingWorldBlocks.LUMINOUS_MOSS_ITEM.get());
        event.register((stack, tint) -> 0xB4C2B9, LivingWorldBlocks.ANCIENT_BRICKS_ITEM.get());
        event.register((stack, tint) -> 0xC9B780, LivingWorldBlocks.CARVED_ANCIENT_BRICKS_ITEM.get());
    }
    private LivingWorldBlockColors() {}
}
