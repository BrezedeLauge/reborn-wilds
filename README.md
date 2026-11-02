# Reborn Wilds

**A living-world adventure mod for Minecraft.**

Watch duck families swim ashore, follow birds between resting places, build an inn that welcomes real visitors, or seek out a Dungeons-inspired encounter. Reborn Wilds adds wildlife, settlements and voluntary adventures while keeping Minecraft's building, mining, exploration and Redstone at its heart.

**Alpha · Minecraft Java 1.21.1 · NeoForge 21.1.265 · Java 21**

The project was previously named **Dungeons Alive**. Its internal mod ID remains `dungeonsalive` to preserve registry IDs and saved data. Existing commands, registry keys and technical filenames retain that identifier.

## What's here

- **Living wildlife.** Small herds and flocks travel, rest and react to danger. Wooly cows can breed and raise calves; natural duck families swim, wait for their young and visit the shore. Bird routines include actual perching and takeoff.
- **Places you build.** Mark a player-built inn, assign its food supplies and provide a usable entrance, seat and bed. A bounded visitor system walks guests along real paths, including suitable bridges and wooden doors. Ownership, reservations and visits persist across saves.
- **A world to explore.** Four custom biomes, three adventure sites, merchant caravans, settlement charters, natural hostile encounters and additional equipment in selected structure chests. Repeated travel leaves paths that can recover, while discovered corruption can be stopped and cleansed.
- **Dungeons-inspired encounters.** 131 registered character profiles: 78 hostile and 53 peaceful, including variants, merchants and companions. Four spell constructs are counted separately. Boss and miniboss fights can be started deliberately with 21 craftable encounter seals.
- **Equipment and progression.** 42 equipment items: 12 artifacts, 14 melee weapons, four ranged weapons and 12 armor pieces across three sets. Shared souls, six enchantments and repair, weapon-upgrade and enchanting services support their own Minecraft balance.
- **Voluntary quests.** 30 bundled, data-driven quests. Accept an adventure when you want it and claim its reward yourself; completing one never automatically starts the next. Inns also work without their optional quest.

These are implemented systems in an evolving alpha, not a complete port of Minecraft Dungeons or completion of the project's full design. Character profiles include variants and roles, not 131 distinct species. See the [implementation inventory](design/implementation-status.json) and [development roadmap](design/minecraft2-vision-status.json) for the finer distinctions.

## Install

1. Create a **Minecraft Java 1.21.1** instance with **NeoForge 21.1.265** and Java 21. Fabric, Forge and Bedrock are not supported targets.
2. Build the mod using the instructions below. Copy the resulting `build/libs/dungeonsalive-0.4.0-alpha.jar` into the instance's `mods` folder. Install the same build on both the client and server.
3. Keep only one version of this mod installed. Back up existing worlds before changing alpha builds.
4. To include the four custom biomes, create a **new world** with the **Reborn Wilds – Living World** preset (`dungeonsalive:living_world`). Existing worlds retain their generator; new structures do not rebuild already generated terrain.

The core mod does not require the upstream content mods or resource packs credited below. Optional integrations and the separate camp datapack are described in [optional setup](docs/OPTIONAL_SETUP.md) and [Dungeons camps](docs/DUNGEONS_CAMPS.md).

## Start exploring

| Control | Action |
| --- | --- |
| **J** | Open the travel journal; accept and claim quests. |
| **B** | Open the creature guide; browse models, animations and equipment. |
| **Right-click a merchant** | Trade or open that merchant's supported service. |
| **Sneak + right-click a merchant** | Open the quest journal. |
| **Sneak + use an encounter seal on the ground** | Request a boss or miniboss encounter in a suitable clear area. |

Key bindings can be changed in Minecraft's controls menu. The guide's previews do not spawn creatures into your world. Encounter seals check space and permissions before consuming an item; they do not build arenas or remove your blocks.

For an inn, craft an **Inn ledger** from a lectern and an emerald, and an **Inn plan** from paper and charcoal. Use the plan on your ledger, mark the room corners, entrance, supplies and arrival point, then confirm at the ledger. The [inn guide](docs/PLAYER_BUILT_INNS.md) explains supported furniture, routes and ownership.

Useful commands:

```text
/dungeonsalive journal
/dungeonsalive status
/dungeonsalive accept dungeonsalive:encounters/redstone_golem
/dungeonsalive claim dungeonsalive:encounters/redstone_golem
```

Acceptance still requires the quest to be available; claiming still requires its server-checked objectives. Operators can inspect registered IDs with `/dungeonsalive registry entities dungeonsalive` and `/dungeonsalive registry items`.

## Build and run

Use a **JDK 21**. The Gradle wrapper is included; the first build downloads development dependencies.

```bash
# macOS / Linux
chmod +x gradlew
./gradlew build
./gradlew runGameTestServer
./gradlew runClient
```

```powershell
# Windows PowerShell
.\gradlew.bat build
.\gradlew.bat runGameTestServer
.\gradlew.bat runClient
```

The build output is `build/libs/dungeonsalive-0.4.0-alpha.jar`. Local worlds, launcher profiles, dependency caches and development captures are not part of the public source distribution.

## Status and limits

The **2 November 2026** source snapshot passed a complete Java/Minecraft/NeoForge typecheck, **170 JUnit tests** and **139 native GameTests**. The subsequent display-name update also passed all 170 JUnit tests and preserved registry/data keys. The 139 server cases refer to the preceding gameplay snapshot. These results come from the documented direct Java/NeoForge validation route, not a newly completed Gradle release. See [validation](docs/VALIDATION.md) for the exact snapshot, methods and outstanding checks.

Native screenshots and telemetry are supporting evidence, not blanket visual approval. The latest inn capture produced 39 real frames; its visual review is still pending. A fresh nature capture for that same snapshot has not yet been run.

Important boundaries of this alpha:

- Boss phases, companion abilities and original Dungeons services remain incomplete. Models and animations combine attributed community work with original adaptations; they are not an official Dungeons asset export.
- The broader building, cooperation and progression design is still in development. Mechanical power networks and the mechanics tutorial are currently isolated drafts, **not included features**.
- Natural encounters respect local limits and loaded chunks. Bosses do not appear through the regular natural-hostile spawn lists. Existing generated chest inventories are not refilled.
- Water currents are a local approximation, not a full fluid-volume simulation. Corruption spreads through recorded natural ground; it does not rewrite existing biome IDs or intentionally overwrite player construction.
- A comprehensive survival playthrough, two-client multiplayer acceptance, long-term load testing and broad renderer/shader compatibility testing remain open.

## Documentation

Much of the detailed development documentation is currently in German.

- [Equipment, souls and merchant services](docs/GEAR_AND_SOULS.md)
- [Player-built inns](docs/PLAYER_BUILT_INNS.md)
- [Living-world systems](docs/LIVING_WORLD_EXPANSION.md)
- [Encounters and seals](docs/ENCOUNTERS.md)
- [Creatures and runtime behavior](docs/MOBS_RUNTIME.md)
- [Quest data format](docs/QUEST_FORMAT.md)
- [Validation and known limitations](docs/VALIDATION.md)

## Credits and licenses

**This repository contains work under different licenses.** Original runtime code, tools and original artwork are MIT-licensed. Selected community artwork retains its upstream license, including **CC BY-NC-SA 4.0** for ReCrafted material. Generated Java files containing adapted artistic geometry can also retain that artwork's license; the project's MIT license does not override it.

| Upstream project | Attribution and license |
| --- | --- |
| [Goety 2](https://github.com/Polarice3/Goety-2) | Polarice3 and credited contributors · MIT |
| [Dungeons Mobs](https://github.com/Infamous-Misadventures/Dungeons-Mobs) | Patrigan and credited contributors · MIT |
| [Dungeons Mobs Port](https://github.com/FirefoxSalesman/DungeonsMobs) | Original authors, FirefoxSalesman and credited artists · MIT |
| [Dungeons Gear](https://github.com/Infamous-Misadventures/Dungeons-Gear) | Patrigan, the_infamous_1 and contributors · MIT |
| [ReCrafted Mobs](https://modrinth.com/resourcepack/recrafted-mobs) | GillsBills and the pack's credited contributors · CC BY-NC-SA 4.0 |

Preserve the relevant attribution, modification notices and license terms when sharing or adapting these files. ReCrafted artwork retains its noncommercial and share-alike conditions. Full credits, pinned sources and changes are recorded in [THIRD_PARTY_NOTICES.md](THIRD_PARTY_NOTICES.md), the [model provenance inventory](design/model-delivery-inventory.json), the [equipment inventory](design/gear-source-inventory.json) and the license texts under `third_party/`. The project's own license is in [LICENSE](LICENSE).

Vanilla Minecraft resources are referenced through the installed game rather than bundled as game assets. Optional third-party mod binaries are not included.

**Reborn Wilds is an unofficial fan project, not an official Minecraft product. It is not approved by or associated with Mojang or Microsoft.**
