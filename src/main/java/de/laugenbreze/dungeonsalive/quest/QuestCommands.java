package de.laugenbreze.dungeonsalive.quest;

import com.google.gson.GsonBuilder;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import de.laugenbreze.dungeonsalive.DungeonsAlive;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.LevelResource;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class QuestCommands {
    private QuestCommands() {}
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        var root = Commands.literal("dungeonsalive")
                .executes(context -> { QuestService.open(context.getSource().getPlayerOrException()); return 1; })
                .then(Commands.literal("journal").executes(context -> {
                    QuestService.open(context.getSource().getPlayerOrException()); return 1;
                }))
                .then(Commands.literal("accept")
                        .then(Commands.argument("quest", ResourceLocationArgument.id())
                                .suggests((context, builder) -> SharedSuggestionProvider.suggest(
                                        QuestService.repository().snapshot().documents().keySet(), builder))
                                .executes(context -> QuestService.accept(context.getSource().getPlayerOrException(),
                                        ResourceLocationArgument.getId(context, "quest").toString()))))
                .then(Commands.literal("claim")
                        .then(Commands.argument("quest", ResourceLocationArgument.id())
                                .suggests((context, builder) -> SharedSuggestionProvider.suggest(
                                        QuestService.repository().snapshot().documents().keySet(), builder))
                                .executes(context -> QuestService.claim(context.getSource().getPlayerOrException(),
                                        ResourceLocationArgument.getId(context, "quest").toString()))))
                .then(Commands.literal("status").executes(context -> {
                    context.getSource().sendSuccess(() -> Component.literal("Reborn Wilds: "
                            + QuestService.repository().snapshot().documents().size() + " quests; revision "
                            + QuestService.repository().snapshot().revision()), false);
                    for (String warning : QuestService.repository().warnings()) {
                        context.getSource().sendSuccess(() -> Component.literal(warning), false);
                    }
                    return 1;
                }));
        var registry = Commands.literal("registry").requires(source -> source.hasPermission(2));
        for (String kind : List.of("items", "entities", "biomes")) {
            registry.then(Commands.literal(kind)
                    .executes(context -> exportRegistry(context.getSource(), kind, null))
                    .then(Commands.argument("namespace", StringArgumentType.word())
                            .executes(context -> exportRegistry(context.getSource(), kind,
                                    StringArgumentType.getString(context, "namespace")))));
        }
        root.then(registry);
        dispatcher.register(root);
    }

    private static int exportRegistry(CommandSourceStack source, String kind, String namespace) {
        if (namespace != null && !namespace.matches("[a-z0-9_.-]{1,128}")) {
            source.sendFailure(Component.literal("Invalid namespace. Use a mod ID without : or /"));
            return 0;
        }
        var keys = switch (kind) {
            case "items" -> BuiltInRegistries.ITEM.keySet();
            case "entities" -> BuiltInRegistries.ENTITY_TYPE.keySet();
            case "biomes" -> source.getServer().registryAccess()
                    .registryOrThrow(net.minecraft.core.registries.Registries.BIOME).keySet();
            default -> throw new IllegalArgumentException("Unknown registry: " + kind);
        };
        var ids = keys.stream().filter(id -> namespace == null || id.getNamespace().equals(namespace))
                .map(ResourceLocation::toString).sorted().toList();
        try {
            var directory = source.getServer().getWorldPath(LevelResource.ROOT).resolve("dungeonsalive/registry");
            Files.createDirectories(directory);
            String filename = kind + (namespace == null ? "" : "-" + namespace) + ".json";
            var destination = directory.resolve(filename);
            var temporary = Files.createTempFile(directory, "registry-", ".tmp");
            Map<String, Object> output = new LinkedHashMap<>();
            output.put("schema_version", 1);
            output.put("minecraft_version", "1.21.1");
            output.put("registry", kind);
            output.put("namespace", namespace);
            output.put("ids", ids);
            Files.writeString(temporary, new GsonBuilder().setPrettyPrinting().create().toJson(output), StandardCharsets.UTF_8);
            Files.move(temporary, destination, StandardCopyOption.REPLACE_EXISTING);
            source.sendSuccess(() -> Component.literal(ids.size() + " IDs exported: " + destination.toAbsolutePath()), false);
            return ids.size();
        } catch (IOException exception) {
            DungeonsAlive.LOGGER.error("Could not export registry {}", kind, exception);
            source.sendFailure(Component.literal("Could not write registry export; see server log."));
            return 0;
        }
    }
}
