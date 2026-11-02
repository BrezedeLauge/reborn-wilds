# Herkunft und Lizenzen der mitgelieferten Bestandteile

**Dieses Paket enthält Dateien unter unterschiedlichen Lizenzen.** Der eigene Programmcode steht unter MIT. Ausgewählte Community-Modelle und Texturen stehen unter MIT beziehungsweise **CC BY-NC-SA 4.0**. Die Projekt-MIT-Lizenz ersetzt die Bedingungen dieser Kunstdateien nicht. Auch eine aus einem fremden Modell erzeugte Java-Geometrie behält die Herkunft der darin enthaltenen Modellkunst.

Die Liste der **tatsächlich vorhandenen** Quellsnapshots, Texturen und Modell-/Animationsdateien steht mit SHA-256, Quellenpfad, Revision, Attribution und Änderungen in [`design/model-delivery-inventory.json`](design/model-delivery-inventory.json). Aktualisierung nach dem letzten Modell-/Texturmerge und vor dem Release-Build: `python3 tools/generate_model_delivery_inventory.py`. Das Inventar belegt Herkunft und Dateiintegrität; es ist keine Zusage, dass jede Form registriert, vollständig animiert oder grafisch abgenommen ist.

Die JAR führt diese Hinweise unter `META-INF/THIRD_PARTY_NOTICES.md`, vollständige Lizenztexte unter `META-INF/third_party/` und das Inventar unter `META-INF/dungeonsalive/model-provenance.json` mit. Quellsnapshots und Texturen werden dafür nicht ein zweites Mal als Lizenzbeilage hineinkopiert.

## Eigener Projektinhalt — MIT

Eigenständig erstellter Laufzeitcode, Konverter, Build-/Prüfhelfer, Projekttexte, Questdefinitionen, die leere GameTest-Struktur und eigene Kunstdateien unterliegen der MIT-Lizenz im Hauptverzeichnis, © 2026 LaugenBreze. Dazu gehören eigene Partikel-/Windhorn-Texturen, ältere allgemeine Mobprototypen, Berufsoverlays, Werkstationen und Begegnungssiegel. Ihre Herstellung ist in `tools/` und [`design/asset-provenance.json`](design/asset-provenance.json) dokumentiert.

Die drei kurzen Enten-Kontaktlaute werden durch `tools/generate_duck_sounds.py` aus eigenen Oszillator-/Formantfunktionen erzeugt und als räumlich abspielbares Mono-Ogg kodiert. Es werden keine fremden Tonaufnahmen verwendet. [`design/duck-audio-provenance.json`](design/duck-audio-provenance.json) führt die konkreten Dateien und Hashes; die Laute sind eigene synthetische Arbeit unter MIT und kein extrahiertes Dungeons-Audio. Familienverhalten und zustandsgebundene Körperhaltungen sind eigene Laufzeitergänzungen. Die unveränderte ReCrafted-Entengeometrie und ihre Textur behalten separat CC BY-NC-SA 4.0.

Tukan- und Goldpapagei-Kontaktlaute referenzieren den installierten Minecraft-Papageienruf mit eigener Ereigniszuordnung und passenden Untertiteln. Die Flügelschläge beim tatsächlichen Abflug referenzieren ebenfalls ein installiertes Minecraft-Ereignis. Es werden keine Minecraft-Tondateien mitgeliefert oder umlizenziert; diese Zuordnung behauptet weder echte artspezifische Aufnahmen noch Dungeons-Originalaudio. [`design/bird-audio-provenance.json`](design/bird-audio-provenance.json) und `tools/generate_bird_sound_aliases.py` dokumentieren die Zuordnung.

Die eigene Redstone-Monstrosität aus 0.2.1, ihre beiden 512²-Atlanten und ihr Blockbench-Export bleiben eigene Projektarbeit. Die neu eingebundene Goety-Darstellung ist eine gesonderte Quelle und wird nicht als selbst erstelltes Modell ausgegeben. Die in [`docs/REDSTONE_MONSTROSITY.md`](docs/REDSTONE_MONSTROSITY.md) verlinkten offiziellen Bilder wurden betrachtet, aber nicht als Texturen übernommen.

Die neue **Vengeful Heart of Ender**-Nachbildung ist eigene Arbeit: `tools/generate_original_vengeful.py`, `OriginalVengefulHeartGeometry.java`, `OriginalVengefulHeartAnimations.java` und die beiden `textures/entity/reference/original/vengeful_heart*.png`. Der Generator erzeugt Geometrie, UVs, Stein-/Riss-/Flammenpixel und Animationen ohne Bilddateien als Eingabe. Betrachtete Referenzen waren [Mojangs Echoing-Void-Artwork](https://www.minecraft.net/content/dam/minecraftnet/games/dungeons/key-art/EchoingVoid-header2.jpg) und eine sekundäre [Wiki-Renderansicht](https://minecraft.wiki/images/MCD_Vengeful_Heart_of_Ender.png). Diese Bilder sind keine mitgelieferten Texturvorlagen.

Eigene Piglin-Aktionsclips, Creeper-Zündbewegungen und technische Wrapper um installierte Minecraft-Geometriefabriken bleiben eigene Ergänzungen. Sie sind keine extrahierten Dungeons-Keyframes. Unabhängige Render-/Konverterlogik wird durch die Verwendung separat lizenzierter Kunstdateien nicht pauschal umetikettiert.

Die später ergänzten `OriginalTropicalSlimeAnimations`, `OriginalTridentDrownedAnimations` und `OriginalCivilianAnimations` sind eigene MIT-Clips: Squash, über Kopf vorbereiteter Dreizackwurf und zivile Gefangenen-/Befreiungsgesten. Die vorhandenen fremden Modellformen und Texturen behalten ihre jeweiligen Lizenzen. Die Befreiungslogik und der spezialisierte Tropenschleimrenderer sind eigene Laufzeitadaptionen.

## Goety 2 — MIT

- **Quelle:** [Polarice3/Goety-2](https://github.com/Polarice3/Goety-2), Branch `1.20`, [Commit `7de64560f76ae47f1b399bb46f69540511b9871e`](https://github.com/Polarice3/Goety-2/tree/7de64560f76ae47f1b399bb46f69540511b9871e).
- **Autorenseite:** [Goety auf CurseForge](https://www.curseforge.com/minecraft/mc-mods/goety).
- **Lizenz:** MIT, **Copyright (c) 2023 Polarice3**. Vollständiger Text: [`third_party/goety/LICENSE.txt`](third_party/goety/LICENSE.txt).
- **Weitere Kunstcredits:** **night.lxrd** für Royal Guard/Blackguard; die konkrete Royal-Guard-Zuordnung bleibt im Modellmanifest erhalten. Weitere Credits folgen den festgehaltenen Quellen, ohne erfundene Einzelautorschaft.

Ausgewählt sind Modellgeometrie, Texturen/Leuchtmasken und Animationsdefinitionen für zahlreiche Illager-, Untoten-, Pflanzen-, Enderling- und Golemformen. Spätere Ergänzungen umfassen unter anderem Squall Golem, Mountaineer, Vindicator Chef, Sunken Skeleton, Quick Growing Vine, Poison Quill Vine und Flame Construct mit Effektframes. Mehrere Renderbindungen können dasselbe Rig teilen; das ist keine entsprechende Anzahl neuer Originalmodelle.

Unveränderte Java-Vorlagen liegen in `third_party/goety/source_models/` und `source_animations/`. Adaptionen liegen unter `client/entity/reference/imported/Goety*`, Texturen unter `textures/entity/reference/goety/`. Änderungen betreffen Paket-/Klassennamen, native Renderanbindung, Anschluss an Minecraft-Serverzustände und dokumentierte Adapter. Die **Treetop-Whisperer-Palette** ist eine benannte Ableitung der Whisperer-Textur mit dunkleren Außenfarben und violettem Inneren; die Formel steht in `tools/generate_treetop_palette.py`. Diese Ableitung und Größenanpassung sind kein vollständiger Treetop-Kampfport.

Dateirevisionen, direkte Quellenlinks und Hashes stehen im Lieferinventar und in [`design/reference-models.json`](design/reference-models.json). Für Flame Construct nennt außerdem die [Autorenveröffentlichung 2.5.59.0](https://www.curseforge.com/minecraft/mc-mods/goety/files/9153454) den Zugang. Übernommen werden nur ausgewählte Kunstdateien; Goety selbst, übrige Assets, Sounds und Mod-Binary werden nicht gebündelt.

**Tropical Slime:** Zusätzlich ausgewählt sind das tatsächliche innere und äußere `TropicalSlimeModel`, die feindliche Haut `tropical_slime.png` und die **47 animierten Fischframes**. Der Quellrenderer, Texturindex und die relevante Entity bleiben als unveränderte MIT-Snapshots für den Bewegungs-/Materialbeleg erhalten. Die Frames sind keine Farbvarianten: Der Quellzähler nutzt einen 48-Tick-Zyklus mit Rückfall auf Frame 1 im letzten Slot. Datei 48 sowie Goetys eigene Diener-/Geheimhaut werden nicht ausgewählt. Die Geometrie behält Quader und UVs; ein eigener gemeinsamer Bodenpivot ermöglicht dieselbe Verformung von Kern, Fischen und transparenter Hülle. Herkunft, SHA256 und genaue Änderungen stehen in [`design/v04-source-model-additions.json`](design/v04-source-model-additions.json). `tools/prepare_v04_model_assets.py` stellt die ausgewählten Texturen aus den geprüften Snapshots wieder her.

## Dungeons Mobs — MIT

- **Quelle:** [Infamous-Misadventures/Dungeons-Mobs](https://github.com/Infamous-Misadventures/Dungeons-Mobs), Branch `1.19`, [Commit `f04b11e7b3672776599fd15ad98a61c53f63b182`](https://github.com/Infamous-Misadventures/Dungeons-Mobs/tree/f04b11e7b3672776599fd15ad98a61c53f63b182).
- **Lizenz:** MIT, **Copyright (c) 2022 Patrigan**. Vollständiger Text: [`third_party/dungeons_mobs/LICENSE.txt`](third_party/dungeons_mobs/LICENSE.txt).
- **Credits:** Patrigan und die jeweiligen Dateiautoren. Die geprüfte Historie nennt zahlreiche Nachbildungen/Animationen von **Alexander’s Fun And Games**, weitere Beiträge unter anderem von **Thelnfamous1**, **Mike Garay**, **L_Ender** und **baguchi**. Die Quellenprüfung hält ausgewählte Dateicommit-Autoren fest; diese Sammelnennung behauptet keine alleinige Autorschaft an jeder Datei.

Ausgewählt sind Illusioner, zusätzliche Zombie-, Skelett-, Creeper-, Piglin- und Drowned-Paletten, Korallen-/Sackteile und Gecko-Rigs/-Clips für Mage und Konstrukte. Die **echten zusätzlichen Illusioner- und Mage-Rüstungen** sind Bestandteil: Die ursprünglichen Entities rüsten `ILLUSIONER_ARMOR` beziehungsweise `MAGE_ARMOR` aus. Daher gehören ihre Armor-Geometrien und PNGs zu den benötigten Quellen; sie werden an die ursprünglichen Rigsockel gebunden.

Snapshots liegen in `third_party/dungeons_mobs/snapshots/`. Adaptionen heißen `BedrockIllusioner*` beziehungsweise `Infamous*`; Texturen liegen in `textures/entity/reference/dungeons_mobs/` und `textures/entity/reference/infamous/`. Quellpixel bleiben unverändert oder werden mit dokumentierten Offsets **verlustfrei** in Haut-/Rüstungs-/Sack-/Außenhaut-Atlanten gesetzt. Modelle und Kurven werden nativ konvertiert; Knochen-, Material-, Easing-Bindungen und die bewusst zyklische Abbildung von `query.life_time` stehen in den Konvertierungsmanifesten. Eigene native Bewegungen sind als eigene Ergänzungen gekennzeichnet.

Relevante Manifeste: [`design/bedrock-model-imports.json`](design/bedrock-model-imports.json), [`design/infamous-additions.json`](design/infamous-additions.json), [`design/infamous-bindings.json`](design/infamous-bindings.json) und zugehörige Konvertierungsberichte. Der zusätzliche Quellenkandidat Mage erweitert nicht stillschweigend die recherchierte Liste ursprünglicher Dungeons-Arten.

## Dungeons Mobs Port — MIT

- **Quelle:** [FirefoxSalesman/DungeonsMobs](https://github.com/FirefoxSalesman/DungeonsMobs), [Commit `cca789ab3ce910853517d65494efd76508255a1d`](https://github.com/FirefoxSalesman/DungeonsMobs/tree/cca789ab3ce910853517d65494efd76508255a1d).
- **Autorenseite:** [Dungeons Mobs Port auf CurseForge](https://www.curseforge.com/minecraft/mc-mods/dungeons-mobs-port).
- **Lizenz:** MIT, **Copyright (c) 2022 Patrigan**. Vollständiger Text: [`third_party/dungeons_mobs_port/LICENSE.txt`](third_party/dungeons_mobs_port/LICENSE.txt).
- **Credits:** Patrigan und ursprüngliche Autoren; **Carson Holscher / FirefoxSalesman** für den Port; **Pasticcino** für Redstone-/Mooshroom-Monstrosity-Kunst. Die Projektcredits nennen außerdem **Andrejulio** und **McFella** für weitere Kunst. Individuelle Zuschreibungen folgen den festgehaltenen Quellen.

Ausgewählt sind das detaillierte Monstrosity-Rig mit tatsächlicher Mooshroom-Palette, Augenlayer und Clips sowie Armored-Pillager-/Armored-Vindicator-Modelle mit Rüstungsgeometrie. Vorlagen liegen unter `third_party/dungeons_mobs_port/`; Adaptionen heißen `BedrockMooshroomMonstrosity*` und `PortArmored*`; Texturen liegen unter `textures/entity/reference/dungeons_mobs_port/`. Dies ist gezielte Kunstadaption. Die Fremdmod und ihr vollständiger Spielcode werden nicht gebündelt.

Die beiden ebenfalls veröffentlichten `armored_pillager_diamond.png` und `armored_vindicator_diamond.png` werden unverändert auf ihren passenden **64 × 106** großen Quellatlanten verwendet. Das sind explizite stärkere Rüstungstiers der schon katalogisierten Illager, keine erfundenen Spezies und keine automatisch neu eingefärbten Vanilla-Häute. Besonders der Vindicator nutzt den zugehörigen gehörnten Helm. Größen und Werte sind als Sandboxanpassung dokumentiert. Der Diamond-Vindicator trägt inzwischen das tatsächlich registrierte Whirlwind-Item über seinen Hand-Layer, mit Dropchance 0 und ohne Spielerwaffen-Callbacks; die eigene Gegenstandsadaption ist von der hier zugeschriebenen Rüstungs- und Körperkunst zu unterscheiden.

## ReCrafted Mobs 11.0 — CC BY-NC-SA 4.0

- **Projekt:** [ReCrafted Mobs von GillsBills](https://modrinth.com/resourcepack/recrafted-mobs), Version **11.0**, Versions-ID **`nUAnxMZE`**, veröffentlicht am 24. Mai 2025.
- **Konkretes Quellarchiv:** [ReCrafted Mobs — V.11.0.zip](https://cdn.modrinth.com/data/e1btfmyH/versions/nUAnxMZE/ReCrafted%20Mobs%20-%20V.11.0.zip).
- **Archiv-SHA-256:** `74901b18716b8efc57368a3b2d47a43207416925da6f6f5e492758c94f2cec2c`.
- **Lizenz:** [Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International](https://creativecommons.org/licenses/by-nc-sa/4.0/). Vollständiger Text: [`third_party/recrafted_mobs/LICENSE.txt`](third_party/recrafted_mobs/LICENSE.txt); ursprüngliche Packnotiz: [`UPSTREAM-LICENSE-NOTICE.txt`](third_party/recrafted_mobs/UPSTREAM-LICENSE-NOTICE.txt).
- **Attribution:** **GillsBills**, mit **Cave Fox, Kekkman, PinkuRinku, RavenVLemon, Ewanhowell5195, creepermax123 und MickeyJoeAlpha** gemäß Packcredits. Einzelautorschaft jedes Pixels beziehungsweise Bosses wird im Pack nicht gesondert angegeben.

Die Auswahl umfasst CEM-Modelle, Texturen, Leuchtmasken und Quellregeln für ausgewählte Bosse, Tiere/Pets, Händlerrollen und Varianten. Darunter sind Cauldron, Ancient Guardian, Jungle Abomination, Tempest Golem, Wretched Wraith, Nameless One, Arch-Illager/Heart of Ender, Key Golems, Piggy Bank und Wooly Cow. Das Packarchiv wird nicht mitgeliefert. Ausgewählte unveränderte Vorlagen liegen in `third_party/recrafted_mobs/source/assets/`; ihre Dateiliste mit Hashes steht zusätzlich in `third_party/recrafted_mobs/source/IMPORT_MANIFEST.json`.

**Die Kunstadaptionen behalten CC BY-NC-SA 4.0.** Das betrifft übernommene PNGs, daraus zusammengestellte Atlanten und aus JEMs übertragene künstlerische Geometrie samt UVs/Animationsexpressionen in `Cem*Geometry.java`. Weitergabe und Bearbeitung müssen Attribution, Änderungsvermerk, nichtkommerzielle Nutzung und die gleiche Lizenz für adaptierte Kunst berücksichtigen. Die MIT-Lizenz des eigenen Codes erlaubt keine abweichende kommerzielle Nutzung dieser ReCrafted-Bestandteile. Rein eigene Wrapper, die nur installierte Vanilla-Geometriefabriken aufrufen, sind im Inventar getrennt gekennzeichnet; ihre separat verwendete ReCrafted-Haut bleibt unter CC BY-NC-SA.

**Änderungen:** CEM-Hierarchien werden in native Rigknoten übertragen; Vanilla-Elterntransformationen, Skalierung, Spiegelungen, UVs und unterstützte Ausdrücke werden explizit gebunden. Mehrere Texturen werden bei Bedarf verlustfrei in gepolsterte Atlanten gesetzt und ihre UVs verschoben. Die Vorlagen bleiben unverändert erhalten. Der Standalone-Arch-Illager verwendet den passenden Unterbaum und **`wither_invulnerable6.png`**: Der normale `wither6.png` enthält in Archies UV-Bereich magentafarbene Platzhalter. Heart of Ender behält seine normalen Materialien. Die Quellformeln sind CEM-Animationen für Minecraft, keine aus Dungeons exportierten Keyframes.

Die Packnotiz bittet zusätzlich darum, vor Veränderungen nachzufragen. Diese Bitte ist dokumentiert; das Projekt stützt die gezielte Adaption auf die ausdrücklich veröffentlichte CC-Lizenz und behauptet keine zusätzliche individuelle Genehmigung. Die [Autorseite auf CurseForge](https://www.curseforge.com/minecraft/texture-packs/recrafted-mobs) erläutert die Verwendung beigesteuerter Assets mit Erlaubnis. In den geprüften Dateien wurde keine gegenteilige ausdrückliche Originalspiel-Extraktionserklärung gefunden; ein vollständiger Bearbeitungsverlauf jedes Pixels liegt nicht vor.

Genaue Änderungen und Materialien stehen in [`design/cem-model-imports.json`](design/cem-model-imports.json), [`design/cem-extra-model-imports.json`](design/cem-extra-model-imports.json) und im Lieferinventar. Konvertierung bedeutet keine pauschale grafische oder spielmechanische Fertigmeldung.

Der gefangene Schmied verwendet die bereits zugeschriebene Blacksmith-Geometrie und Haut desselben Dungeons-Charakters. Seine zivile Befreiungsrolle und eigenen Gesten sind getrennte Implementierungsarbeit; sie machen aus dem Modell weder eine neue Spezies noch einen weiteren ursprünglichen Händlerdienst. Die übernommenen ReCrafted-Kunstdateien bleiben dabei CC BY-NC-SA 4.0.

## Minecraft-Ressourcen werden nur referenziert

Das Questjournal verweist beispielsweise auf `minecraft:item/writable_book`. Vanilla-Modelle, Klänge, Handgegenstände, Partikel, Dorfbewohnerkleidung und Arbeitsutensilien werden durch die reguläre Installation aufgelöst. Native Wrapper rufen vorhandene Geometriefabriken auf. Mojangs Entwicklungs-JAR, dekompilierte Spielquellen, Original-Texturen und andere Spiel-Binaries werden nicht als Assetpaket weitergegeben.

`assets/minecraft/` innerhalb der **ReCrafted-Quellsnapshots** bezeichnet den Namespace des dortigen Resourcepacks. Ausgewählt sind konkret geprüfte Community-Kunstdateien dieses Packs, keine aus der Spielinstallation kopierten Vanilla-PNGs. Aufnahmen unter `reports/` zeigen tatsächliche Entwicklungsclients in gekennzeichneten Testszenen; sie sind keine eigenständigen Spiel-Assetlieferungen.

## Dungeons Gear — MIT (Artefakte und Waffen ab 0.4.0)

- **Quelle:** [Infamous-Misadventures/Dungeons-Gear](https://github.com/Infamous-Misadventures/Dungeons-Gear), Commit `9ba0075c5b2f13ef54ec914ad1327a853ecdce6f`.
- **Lizenz:** MIT, **Copyright (c) 2022 Patrigan**. Vollständiger Text: [third_party/dungeons_gear/LICENSE.txt](third_party/dungeons_gear/LICENSE.txt).
- **Credits:** Patrigan, the_infamous_1 und die Dungeons-Gear-Beitragenden; die Quelldateien nennen keine belastbare gesonderte Einzelautorschaft für alle ausgewählten Kunstdateien.

Im weitergeführten Quellstand sind 120 konkrete Dateien ausgewählt: Modelle und PNGs für elf Artefakte, vierzehn Nahkampfwaffen, vier Fernkampfwaffen samt Spann-/Ladezuständen sowie zwölf Rüstungsteile mit drei getragenen Gecko-Modellen und Texturatlanten. Die unveränderten Vorlagen liegen unter `third_party/dungeons_gear/snapshots/`; [design/gear-source-inventory.json](design/gear-source-inventory.json) enthält Dateihashes, direkte gepinnte Quellenlinks, die 117 gelieferten Modell-/Texturdateien und ihre Änderungen. `tools/import_dungeons_gear.py` erzeugt sie wieder aus diesen Vorlagen. Der ältere 0.4.0-JAR enthält weiterhin die ursprüngliche Auswahl von vierzehn Items.

**Änderungen an der Itemkunst:** ausschließlich Resource-Locations beziehungsweise die Registry-Namen `fighters_binding` → `fighters_bindings`, `dagger` → `daggers`, `sickle` → `sickles` und `great_hammer` → `hammer`. Itemquader, UVs, Pixel und GUI-/Handtransformationen bleiben unverändert. **Getragene Rüstung:** `tools/generate_gear_armor_models.py` bindet die Quell-Geometrie an native Humanoid-Gelenke. Die Wolf-Beingeometrie wird mit proportionalen UV-Ausschnitten in Hosen-/Stiefelslot aufgeteilt, weil die ursprünglichen Stiefelknoten leer sind. Konkrete Anpassungen, Atlanten und generierter Java-Hash: [design/gear-armor-models.json](design/gear-armor-models.json). Das sind veröffentlichte Community-Assets; sie werden weder als eigene Kunst noch als belegte Extraktion aus Originalspielarchiven ausgegeben. Ein vollständiger individueller Bearbeitungsverlauf aller Quellpixel ist nicht verfügbar.

**Eigene Umsetzung:** Servereffekte, Ziel-/Deckungsregeln, gemeinsame Spieler-Seelenverwaltung, Minecraft-Schadenswerte, Abklingzeiten, Rezepte, Questzweig, Bossbeute und Handbuch sind eine eigene Minecraft-Adaption. Es wird kein fremder Modcode oder Mod-Binary als Laufzeitabhängigkeit eingebunden. Die JAR enthält diese Lizenz und das Gear-Herkunftsmanifest separat unter `META-INF/`.

## Gradle Wrapper und Projektbasis

- `gradlew`, `gradlew.bat` und `gradle/wrapper/gradle-wrapper.jar` stammen aus dem [offiziellen NeoForge-MDK für 1.21.1 mit ModDevGradle](https://github.com/NeoForgeMDKs/MDK-1.21.1-ModDevGradle).
- Der Wrapper steht unter Apache License 2.0. Vollständiger Text: [`third_party/gradle/LICENSE.txt`](third_party/gradle/LICENSE.txt). Copyright-Hinweise in den Startskripten bleiben erhalten.
- Die Gradle-9.2.1-Distribution lädt erst der Wrapper. Ihre SHA-256 ist in `gradle-wrapper.properties` festgelegt; Quelle: [offizielle Gradle-Prüfsumme](https://services.gradle.org/distributions/gradle-9.2.1-bin.zip.sha256).
- NeoForge, ModDevGradle, JUnit und weitere Werkzeuge werden als externe Abhängigkeiten geladen. Ihre Binaries liegen nicht in der Projekt-ZIP, abgesehen vom aufgeführten Gradle Wrapper. Minecraft-Entwicklungsdateien und Mojang-Mappings bleiben außerhalb des Lieferpakets.

## Nur verlinkte optionale Projekte

Diese Projekte bleiben externe Downloadprofile; ihre Dateien werden nicht durch die gezielte Modelladaption mitgeliefert:

| Projekt | Autorenseite |
|---|---|
| Fresh Animations | https://modrinth.com/resourcepack/fresh-animations |
| Entity Model Features | https://modrinth.com/mod/entity-model-features |
| Entity Texture Features | https://modrinth.com/mod/entitytexturefeatures |
| Enchant With Mob | https://modrinth.com/mod/enchantwithmob |
| Bagus Lib | https://modrinth.com/mod/bagus-lib |

Versions-IDs, Prüfsummen und Quellen stehen in `profiles/optional-mods.json`; ihre eigenen Bedingungen bleiben bestehen. Weitere Befunde stehen in [`docs/INTEGRATIONS.md`](docs/INTEGRATIONS.md).

## Eigene Gasthausfunktion und native Darstellung

Gasthauspult, Auswahlwerkzeug, serverseitige Raumerkennung, Besucherablauf, Möbelbindung, Menü und freiwillige Gasthausquest sind eigener Projektcode beziehungsweise eigene Daten und Texte. Das Pult referenziert das installierte Modell `minecraft:block/lectern`; der Plan referenziert `minecraft:item/map`. Dafür wurden keine Minecraft-Modelldateien oder Bilddateien kopiert. Sitzbank und Bett bleiben echte, vom Spieler gesetzte Vanilla-Blöcke. Der wiederverwendete Dorfhändler behält seine bereits dokumentierte Community-Modellherkunft; die zusätzliche Sitzpose ist eine eigene Host-Anpassung, keine extrahierte Dungeons-Animation. Konkrete Ressourcen und Hashes: [design/inn-content-provenance.json](design/inn-content-provenance.json).

## Dungeons-Bezug und Grenzen der Auswahl

Minecraft, Minecraft Dungeons und Minecraft Dungeons II gehören ihren jeweiligen Rechteinhabern. Dungeons Alive ist ein unabhängiges Fanprojekt. Für diese Integration wurden keine Originalspielarchive von Dungeons I/II oder Actions & Stuff dekompiliert oder daraus Modelle, Texturen, Audiodateien oder Programmbinaries extrahiert. Community-Nachbildungen werden mit ihren konkreten Quellen ausgewiesen. Originalnamen machen daraus keine Original-Assetkopien. Actions & Stuff dient als genannter Qualitätswunsch; dessen Assets wurden nicht übernommen.

Die gesonderte Dungeons-Content-Untersuchung steht in [`docs/DUNGEONS_CONTENT_AUDIT.md`](docs/DUNGEONS_CONTENT_AUDIT.md). Diese Fremdmod und ihre Ressourcen sind nicht Bestandteil der Lieferung; ihre Registry-Liste ist eine Kompatibilitäts-/Inhaltsreferenz. Weitere Details: [`docs/SOURCE_MODEL_AUDIT.md`](docs/SOURCE_MODEL_AUDIT.md), [`design/model-source-audit.json`](design/model-source-audit.json), [`docs/CONTENT_AND_ASSETS.md`](docs/CONTENT_AND_ASSETS.md).
