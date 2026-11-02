package de.laugenbreze.dungeonsalive.client.dev;

import de.laugenbreze.dungeonsalive.DungeonsAlive;
import de.laugenbreze.dungeonsalive.client.FieldGuideScreen;
import de.laugenbreze.dungeonsalive.entity.MobProfiles;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Screenshot;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.client.event.RenderFrameEvent;
import net.neoforged.neoforge.common.NeoForge;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/** Explicit developer-only render exercise, inactive in normal builds and multiplayer. */
public final class RenderSmokeCapture {
    private record Subject(String kind, String id) {}
    private static final List<Subject> SUBJECTS = new ArrayList<>();
    private static FieldGuideScreen guide;
    private static int index, frame;
    private static boolean stopped;
    private RenderSmokeCapture() {}

    public static void register() {
        if (FMLEnvironment.production || !Boolean.getBoolean("dungeonsalive.renderSmoke")) return;
        NeoForge.EVENT_BUS.addListener(RenderSmokeCapture::rendered);
    }

    private static void rendered(RenderFrameEvent.Post event) {
        Minecraft client = Minecraft.getInstance();
        if (stopped || client.level == null || client.player == null || client.getSingleplayerServer() == null) return;
        if (!client.gameDirectory.toPath().toAbsolutePath().normalize().getFileName().toString().equals("run-render-smoke")) return;
        if (guide == null) {
            MobProfiles.all().forEach(mob -> SUBJECTS.add(new Subject("mob", mob.id())));
            for (String id : List.of("royal_guard", "redstone_golem", "necromancer", "wildfire", "heart_of_ender"))
                SUBJECTS.add(new Subject("action", id));
            BuiltInRegistries.VILLAGER_PROFESSION.entrySet().stream()
                    .filter(entry -> entry.getValue() != VillagerProfession.NONE && entry.getValue() != VillagerProfession.NITWIT)
                    .filter(entry -> List.of("minecraft", "dungeonsalive").contains(entry.getKey().location().getNamespace()))
                    .forEach(entry -> SUBJECTS.add(new Subject("profession", entry.getKey().location().toString())));
            SUBJECTS.add(new Subject("journal", "quests"));
            guide = new FieldGuideScreen();
            client.setScreen(guide);
        }
        if (frame == 0) {
            Subject subject = SUBJECTS.get(index);
            if (subject.kind.equals("mob")) guide.showMob(subject.id);
            else if (subject.kind.equals("action")) guide.showMobAction(subject.id, 0);
            else if (subject.kind.equals("profession")) guide.showProfession(ResourceLocation.parse(subject.id));
            else {
                client.setScreen(null);
                client.getConnection().sendCommand("dungeonsalive journal");
            }
        }
        // Several rendered frames catch invalid geometry, layers, textures and GUI rendering.
        if (++frame < 12) return;
        Subject subject = SUBJECTS.get(index);
        if (subject.kind.equals("journal") && !(client.screen instanceof de.laugenbreze.dungeonsalive.client.JournalScreen)) {
            if (frame < 180) return;
            stopped = true;
            DungeonsAlive.LOGGER.error("Render smoke did not receive an authoritative journal response");
            client.stop();
            return;
        }
        Path destination = client.gameDirectory.toPath().resolve("screenshots");
        try {
            Files.createDirectories(destination);
            String file = String.format("%03d-%s-%s.png", index, subject.kind, subject.id.replace(':', '_'));
            try (var pixels = Screenshot.takeScreenshot(client.getMainRenderTarget())) {
                pixels.writeToFile(destination.resolve(file));
            }
            DungeonsAlive.LOGGER.info("Render smoke captured {}", file);
        } catch (Exception exception) {
            stopped = true;
            DungeonsAlive.LOGGER.error("Render smoke capture failed", exception);
            client.stop();
            return;
        }
        index++;
        frame = 0;
        if (index == SUBJECTS.size()) {
            stopped = true;
            DungeonsAlive.LOGGER.info("Render smoke completed: {} real model screenshots", index);
            client.stop();
        }
    }
}
