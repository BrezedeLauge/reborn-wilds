# Validation and delivery status

Reborn Wilds is an actively developed **0.4.0-alpha** mod for **Minecraft Java 1.21.1 / NeoForge 21.1.265 / Java 21**. This page distinguishes tested snapshots, native visual checks and release builds. It does not claim completion of the full design or a complete Dungeons port.

## Current technical baseline — 2 November 2026

The snapshot before the display-name change to Reborn Wilds passed the following checks against genuine Minecraft and NeoForge dependencies:

| Check | Result | Scope |
| --- | --- | --- |
| Java 21 compilation | 458 main sources and 40 test sources | Full type checking against the mapped Minecraft/NeoForge classes |
| JUnit | **170 passed**, no failures, aborts or skipped tests | Quest state, persistence, animation, ownership and other deterministic rules |
| Native NeoForge GameTest server | **139 passed**, no required or optional failures | Real registries, entities, navigation, combat, inventory, world systems and inns |

Test artifact SHA-256:

```text
90b2c19bc58a03baa4b62a19f9505e32c6d70205fe1dd8e0d51a0b1403cb7f8a
```

The [machine-readable summary](validation/2026-11-02.json) records the artifact and the hashes of the original local evidence. Repeated historical runs are not counted as additional independent tests. This artifact was produced by the direct Java/NeoForge validation tools, **not by a new successful Gradle release build**.

## Reborn Wilds display-name check

After the rename, all **458 main sources and 40 test sources** compiled again and **170/170 JUnit tests passed**. The packaged NeoForge metadata reads `displayName="Reborn Wilds"` and `modId="dungeonsalive"`. Both translation files retain every key, and all **463 data-resource entries remain byte-identical** to the baseline. This check changes presentation strings, not gameplay or save contracts.

The separate direct-test artifact has SHA-256 `d781c943cb9e44298a639eb358429594454e5f4ecb93663d182bcbd558d1025f`. See the [branding check](validation/reborn-wilds-branding.json). The 139-case native server run belongs to the earlier baseline; it was not repeated or reattributed to this new artifact. Native visual confirmation of the new labels remains open.

## Native client checks

Twelve real client views of four custom biomes, three staged adventure sites and the revised interfaces were reviewed on the immediately preceding snapshot (`2566768d…`). They confirmed repaired terrain visibility and the selected interface corrections. The sites were placed deliberately for inspection; those images do not demonstrate their natural discovery across world seeds.

The newer `90b2c19b…` snapshot fixes a bird-navigation corner case and capture accounting for normal bed transitions. Its isolated inn run produced **39 original frames** and observed arrival, eating, sitting, sleeping and departure, with private storage unchanged. The frame sequence still needs its complete visual review. The fresh nature run for this same snapshot has not yet been launched.

Audio channels and resources have been checked, but a listening review or audio recording has not been completed. Automated phase telemetry, rig tests and successful image capture do not establish animation quality by themselves.

## Build status and reproducibility

The normal required commands remain:

```bash
./gradlew build
./gradlew runGameTestServer
```

In the current restricted development environment, Gradle stops before executing tasks when its file-lock contention service attempts to create a socket (`SocketException: Operation not permitted`). The direct fallback tools use dependencies from an earlier successful Gradle setup, compile current sources and run a genuine NeoForge server. They do not replace a successful release build or download a modded game distribution.

The earlier successful Gradle JAR is a different, older source snapshot and is not the current release. No historical JAR from a local cache or launcher is distributed as the current version. A source checkout can be built using Java 21 and the included wrapper in a normal supported environment.

## Open acceptance work

- Complete the latest nature/inn visual checks, including motion and actual audio.
- Exercise equipment in both hands and armor across slots, poses, trims and skin types.
- Test natural world generation across seeds and terrain, with longer survival playthroughs.
- Complete two-client multiplayer, long-duration load and renderer/shader compatibility checks.
- Continue unfinished boss phases, companion abilities, merchant services and the broader design.

Mechanical power and its tutorial remain isolated development drafts and are not included in the public source feature set. Existing registry IDs, saved keys and QA guards remain unchanged by the display-name update.

Raw reports, machine-specific logs, launcher profiles, game worlds and screenshots are intentionally not public; see [local evidence policy](LOCAL_EVIDENCE.md). Provenance and required third-party licenses remain in the repository and packaged mod.
