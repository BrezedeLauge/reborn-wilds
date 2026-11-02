# Optionale eigene Lager in der Oberwelt

`examples/datapacks/dungeons_camps` enthält zwei kleine, selbst entworfene Lager für Minecraft Java 1.21.1. Sie bestehen aus normalen Minecraft-Blöcken und enthalten jeweils **einen tatsächlich registrierten friedlichen NPC**. Kein Teil einer ursprünglichen Dungeons-Karte wurde kopiert.

| Lager | Größe | Inhalt |
| --- | --- | --- |
| Händlerplatz | 9 × 6 × 9 Blöcke | Gestreiftes Vordach, Holzpodest, leere Fässer, Laterne, Sitzgelegenheiten und `dungeonsalive:village_merchant` mit normalen bestehenden Handelsangeboten. |
| Gefangenenlager | 11 × 6 × 9 Blöcke | Kleines Holz-/Eisenkäfiggebäude, gewöhnliches Zauntor, Laternen und `dungeonsalive:captured_blacksmith` im unbefreiten zivilen Zustand. |

Das Lagerfeuer am Händlerplatz ist bewusst unangezündet; Laternen liefern Licht. Es gibt keine vorgegebenen Boss- oder Gegnerspawns, keine übernommenen Schatzkisten und keine Originalkartengeometrie.

**Weitergeführter Quellstand:** Das bereits vorhandene Tag `dungeonsalive_camp_npc` aktiviert beim Händler den neuen [Lageralltag](LIVING_WORLD.md). Seine Heimat entsteht erst beim tatsächlichen serverseitigen KI-Schritt am platzierten Weltstandort. Die unveränderte Vorlage benötigt dafür keine neuen NBT-Koordinaten. Tageswege, Nacht-/Regenrückkehr und Schadensflucht sind noch im echten Server und Client zu prüfen; die unten dokumentierten bisherigen Fotos und Tests nehmen diese Prüfung nicht vorweg.

## Aktivieren

1. Die passende Dungeons-Alive-Mod installieren.
2. Den gesamten Ordner `examples/datapacks/dungeons_camps` nach `<Welt>/datapacks/dungeons_camps` kopieren.
3. Die Welt beziehungsweise den Server vollständig neu starten. Worldgen-Registries werden beim Weltladen eingelesen.
4. Neue Chunks in passenden Oberweltbiomen erkunden. Bereits erzeugte Landschaft wird nicht nachträglich mit Lagern bebaut.

Geeignet sind Ebenen, Sonnenblumenebenen, Wälder, Birkenwälder und Wiesen. Die Struktur nutzt einen Jigsaw-Startpool mit zwei ausdrücklich begrenzten Einzelvorlagen; Händlerplätze haben Gewicht 3, Gefangenenlager Gewicht 1. `size: 0` bedeutet, dass keine weiteren Gebäudeteile angebaut werden.

Die zufällige Platzierung verwendet `spacing: 48`, `separation: 28`, Häufigkeitsfaktor `0.5` und eine Ausschlusszone um Vanilla-Dorfstrukturkandidaten. Dies sind Strukturplatzierungsparameter, kein garantiertes Lager alle 768 Blöcke. Biome, Höhenkarte, Zufallsentscheidung und weitere Platzierungsbedingungen begrenzen die tatsächlichen Treffer zusätzlich.

Im Gefangenenlager das normale Zauntor öffnen und den Schmied rechtsklicken. Er reagiert auf die serverseitige Befreiung und folgt dem Retter. Derselbe Spieler kann ihn danach warten oder wieder folgen lassen. Die Befreiung öffnet keine Blöcke automatisch und verleiht keine Gegenstände oder Händler-Upgrades.

Für eine eigene Testwelt stehen nach aktiviertem Pack zur Verfügung:

```mcfunction
/locate structure dungeonsalive:dungeons_camp
/place template dungeonsalive:camps/merchant
/place template dungeonsalive:camps/captive
```

Die beiden `/place`-Befehle setzen eine Vorlage direkt am gewählten Ort. Dafür eine freie Stelle in einer Testwelt verwenden; das ist eine ausdrückliche manuelle Platzierung, keine nachträgliche natürliche Weltgenerierung.

## Eigene Vorlagen bearbeiten

```bash
python3 tools/generate_dungeons_camps.py
```

Der Generator enthält alle Blockpositionen, Paletten, Größen, NPC-Daten, biomebezogenen Tags, Template-Pool- und Struktur-JSONs. Er benötigt keine Zusatzbibliothek und liest keine fremden Karten. Die NBT-Dateien verwenden DataVersion **3955**, das Datapack Format **48**. Die installierte 1.21.1-`version.json` und die tatsächlichen `JigsawStructure`-/`SinglePoolElement`-/`StructureTemplate`-Quellen wurden für diesen Stand geprüft. Mojangs Dateien werden nicht mitgeliefert.

Die Vorlagen stehen unter MIT als eigene Projektarbeit. [dungeons-camps.json](../design/dungeons-camps.json) hält Größen, Hashes, Herkunft und Prüfgrenzen fest. Die sichtbaren NPC-Modelle behalten ihre separat dokumentierten Kunstlizenzen; das reine Blocklayout ändert deren Bedingungen nicht.

## Getrennter tatsächlicher Servertest

```bash
python3 tools/generate_dungeons_camps.py
python3 tools/prepare_camp_gametest.py
./gradlew runCampGameTestServer
```

Die separate Laufkonfiguration verwendet `run-gametest-camps`, `neoforge.enabledGameTestNamespaces=dungeonsalive` und `dungeonsalive.test.camps=true`. Der Vorbereiter installiert ausschließlich in einem eigens markierten Entwicklungsverzeichnis nach `world/datapacks/dungeons_camps`. Bestehende unmarkierte Verzeichnisse werden abgewiesen.

Ein erfundenes `--dataPackDir`-Argument wird nicht verwendet: Die tatsächlich geprüfte Server-Startimplementierung lädt das Pack-Repository aus dem Weltordner; der GameTest-Server aktiviert alle darin verfügbaren Packs. `CampGameTests` registriert über `@GameTestGenerator` **nur bei gesetztem Testflag** zwei erforderliche Tests. Der gewöhnliche Testlauf braucht das optionale Pack nicht.

Der erste Test prüft tatsächliche Struktur-/Pool-/Strukturset-Registries, NBT-Version, Blockpaletten und Zustandseigenschaften und platziert das Händlerlager über `StructureTemplate.placeInWorld`. Danach muss ein echter persistenter Händler mit Handelsangeboten vorhanden sein. Der zweite Test platziert das Gefangenenlager und prüft Befreiung, Folgen/Warten, Speicherung und Ablehnung einer Übernahme durch einen anderen Spieler.

**Tatsächlich ausgeführt am 2. November 2026:** Der zentrale Lauf `runCampGameTestServer` bestand alle **32 erforderlichen Tests**, darunter die 30 gemeinsamen Servertests und beide zusätzlichen Lagerprüfungen `camps.merchant_template` und `camps.captive_rescue`. Damit sind das Laden des optionalen Packs, die tatsächliche Template-Platzierung und die geprüften NPC-Interaktionen belegt. Zusätzlich wurden die drei unten beschriebenen nativen Lageransichten aufgenommen und einzeln visuell geprüft.

Der aktuelle Ausführungsstand und die native Lageransicht stehen in [VALIDATION.md](VALIDATION.md). Ein erfolgreicher Generatorlauf allein beweist weder erfolgreiches Laden des Datapacks noch natürliche Häufigkeit oder gelungene Platzierung an jedem Hang. Eine längerfristige Erkundung verschiedener Weltsamen und die vollständige natürliche Landschaftsabnahme bleiben gesonderte Arbeit.

## Native Lageransichten reproduzieren

Nach dem erfolgreichen optionalen Servertest:

```bash
python3 tools/prepare_camp_showcase.py
./gradlew runCampShowcase
```

Der Vorbereiter kopiert `run-gametest-camps/world` samt aktiviertem Datapack nach `run-camp-showcase/saves/camp-showcase`. Der Client-Helfer platziert dort die tatsächlichen unveränderten Packvorlagen auf einer ebenen Fotofläche. Er akzeptiert ausschließlich die markierte Entwicklungskopie. Falls deren erster Start den bekannten Experimentaldialog zeigt, wird dessen normaler Bestätigungsknopf gezielt für diese einzige Testwelt verwendet.

Der vollständig abgeschlossene Lauf `20261102-084241-374` erzeugte **drei native PNG mit 1440 × 900 Pixeln**. Alle drei wurden tatsächlich angesehen:

- [Händlerlager](../reports/v04-new-content-native/camps/20261102-084241-374/01-merchant-camp.png): vollständiger Stand und echter Händler, mit drei serverseitig vorhandenen Handelsangeboten.
- [Gefangenenlager](../reports/v04-new-content-native/camps/20261102-084241-374/02-captive-camp.png): geschlossenes normales Zauntor, unbefreiter wartender Schmied.
- [Nach der Befreiung](../reports/v04-new-content-native/camps/20261102-084241-374/03-blacksmith-rescued.png): dasselbe tatsächliche NPC-Exemplar nach erfolgreicher Haupt-Hand-Interaktion; die synchronisierte Befreiungsreaktion ist sichtbar.

Die Übersicht ist eine ausdrücklich gestellte Testszene. Für das dritte Foto wurde das gewöhnliche Tor separat geöffnet; die NPCs werden für die Bildkomposition mit `NoAI` festgehalten. Der natürliche Datapack-Spawn besitzt diesen Fotozustand nicht. Tatsächliches Folgen nach Befreiung wurde gesondert im Servertest und im Modellaufnahme-Helfer geprüft. Die Fotos allein belegen keinen natürlich generierten Fund und keinen Laufweg durch das Lager.

Das [originale Aufnahmeprotokoll](../reports/v04-new-content-native/camps/20261102-084241-374/manifest.json) enthält Template- und Bildhashes, UUIDs, tatsächliche Kamerapositionen und Server-/Clientzustände. Der [Sichtprüfbericht](../reports/v04-new-content-visual-qa.json) hält auch die beiden vorherigen Aufnahmefehler und deren erfolgreiche Korrekturen fest. Die gelieferten PNG sind unveränderte Kopien der Minecraft-Framebuffer-Aufnahmen.
