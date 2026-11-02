# Architektur

Stand: **2. November 2026**, **0.4.0-alpha**. Zielplattform: Minecraft Java 1.21.1, NeoForge 21.1.265, Java 21. Der unten ausdrücklich bezeichnete lokale Quellfix folgt auf den ausgelieferten 0.4.0-Stand; er ist keine neue Releaseversion oder erneute Laufzeitabnahme.

## Grundentscheidung

Der Server entscheidet über Questfortschritt, Belohnungen, Handel, Besitz, Seelen, Begegnungen und Kampfwirkungen. Clients stellen diesen Zustand dar. Minecrafts Bauen, Inventar, Bewegung, Kamera und allgemeine Kampfsteuerung bleiben die Grundlage. Der Quellenkatalog ist Entwicklungsinventar; er registriert keine zusätzlichen Inhalte. Die tatsächlichen **131 Figuren** sind **78 Gegner und 53 friedliche Figuren**. **Vier Zauberkonstrukte** werden separat gezählt; aktuelle IDs und Zuordnungen stehen in [implementation-status.json](../design/implementation-status.json).

Alle folgenden Paketpfade liegen unter `src/main/java/de/laugenbreze/dungeonsalive/`:

| Pfad | Verantwortung |
| --- | --- |
| `quest/core/` | Minecraft-unabhängige Definitionen, Abhängigkeiten, Fortschritt, Status und Claim-Sperre |
| `quest/data/` | Begrenztes JSON-Format, Registry-Prüfung, atomarer Katalogwechsel und NBT-Speicherung |
| `quest/QuestService.java` | Adapter für Spieler, Inventar, Biome, Killfortschritt, Belohnungen und Journal-Synchronisierung |
| `quest/QuestEvents.java`, `QuestCommands.java` | Serverereignisse, Befehle und Registry-Export |
| `network/` | Journal-Ansicht, S2C-Payload und Trennung gemeinsamer/Client-Klassen |
| `entity/` | 78 Gegnerprofile, echte Entity-/Spawn-Ei-Registrierung, Attribute, Angriffszeitlinien, KI und SoulBolt |
| `entity/peaceful/` | 53 friedliche Profile, Tiere/Begleiter, eigenständige Händler, befreibarer Schmied und Camp-GameTests |
| `entity/construct/` | Vier zeitlich begrenzte Zauberkonstrukte, Besitzer-/Zielregeln, Platzierung, Deckung und Dreizackprojektil |
| `client/entity/`, `client/entity/reference/` | Ausgewählte Quellenmodelle, native Kompositionen, Materialien, Hand-Layer und synchronisierte Aktionsdarstellung |
| `encounter/` | 21 Siegel, Standortprüfung, serverseitige Erzeugung, Verbrauch und gemeinsamer Cooldown |
| `village/` | Sechs Berufe, Arbeitsplätze, POIs, Handel und Beobachtung tatsächlicher Server-Arbeitszustände |
| `client/village/` | Eigene Dorfbewohnermodelle, 19 Berufsbewegungen, Requisiten, Zustandsinterpolation und Vorschau |
| `client/FieldGuideScreen.java` | Lokale 3D-Vorschau von Kreaturen und Berufen ohne Weltänderung |
| `client/GearGuideScreen.java` | Vorschau der vierzehn Ausrüstungsmodelle mit Beschreibung und Werten |
| `client/JournalScreen.java` | Aufgaben, Fortschritt, Voraussetzungen und explizite Annahme/Abholung |
| `client/dev/` | Echte Client-Aufnahmen in markierten Modell-, Boss-, Feature- und Lager-Entwicklungswelten |
| `ambient/`, `client/ambient/` | Partikelregistrierung und ausschließlich lokale Atmosphäre |
| `item/` | Reisetagebuch und Windhorn |
| `item/gear/` | Acht Artefakte, sechs Waffen, gemeinsame Seelen, Kampf-/Deckungsregeln und Cooldowns |
| `gametest/`, zusätzliche `*GameTests` | Tests im tatsächlich gestarteten NeoForge-Server |

## Quests und Lebenszyklus

Beim Server-Reload lädt `QuestRepository` JSON-Dateien zu einem unveränderlichen `Bundle` aus Katalog, Anzeigedaten und Revision. Erst eine vollständig gültige Zusammenstellung ersetzt den bisherigen Stand. Auch JSON-Syntaxfehler verhindern diesen Austausch. Fehlende optionale Mods deaktivieren ihre Quests und abhängigen Aufgaben nachvollziehbar im Log.

Der Grundbestand umfasst **29 Quests: vier Einstiegsquests, 21 Begegnungsquests und vier Ausrüstungsquests**. Die Begegnungsquests setzen die abgeholte Belohnung von `dungeonsalive:a_place_to_return` voraus. Freischaltung bedeutet Verfügbarkeit; sie nimmt keine Aufgabe automatisch an. Die optionale Enchant-With-Mob-Quest erhöht den Bestand auf 30, sofern ihr Datapack und die Fremdmod aktiv sind.

Beim Login erhält ein Spieler einmalig das Questbuch. Der UUID-basierte Fortschritt kommt aus `QuestSavedData`. Inventar und aktuelles Biom werden alle 20 Server-Ticks geprüft; ein passendes Todesereignis wird unmittelbar verarbeitet. Vor Abholung wird das Inventar erneut geprüft. Sammelziele weisen Besitz nach und verbrauchen keine Gegenstände.

`QuestService` hält pro UUID einen `CachedJournal`. Nach Katalogwechsel erkennt der Vergleich der Bundle-Instanz die neue Generation und stellt das Journal aus dem gespeicherten Snapshot wieder her. Logout und Serverstopp räumen Sitzungscaches auf. Geänderte Snapshots markieren SavedData als geändert.

Die Datei `<Welt>/data/dungeonsalive_quests.dat` gilt gemeinsam für alle Dimensionen. Beschädigte Spielereinträge bleiben quarantänisiert erhalten; unbekannte Dateischemata schützen die ganze Datei vor Überschreiben. Unbekannte Quest-/Zielschlüssel werden beim vorübergehenden Entfernen eines Datapacks erhalten. Bestehende Quest-IDs und ihre Zielbedeutung bleiben erhalten. Details: [QUEST_FORMAT.md](QUEST_FORMAT.md).

Ein Claim setzt seinen Zustand vor der Reward-Ausgabe und verhindert wiederholte Abholungen in derselben Sitzung. Minecraft speichert Weltzustand und Spielerdateien regulär. **Claim, Inventar und Erfahrung bilden keine gemeinsame Datenbanktransaktion:** Ein harter Prozessabbruch kann nicht atomar über diese Dateien abgesichert werden.

## Kampfmobs: Profil, Zustand und Wirkung

`MobProfiles` enthält **78 explizite Profile: 57 reguläre Gegner, neun Minibosse und zwölf Bossgrundtypen**. Ein Profil bestimmt Registry-ID, Namen, Familie, Dimensionen, Kampfwerte, Farben und erlaubte Angriffsfolge. Die Darstellung verwendet die separat ausgewählten Quellen-/Builtin-Bindings. Historische Familienrenderer sind weiterhin als Fallback-Code vorhanden, ersetzen aber nicht die ausgewählten Modelle. Gemeinsame Kampftechnik ist beabsichtigt; die Profile sind keine vollständige Rekonstruktion sämtlicher Originalmechaniken.

`AttackKind` definiert **15 Aktionen** mit Vorbereitung, Erholung, Cooldown und Reichweite. `DungeonMob` synchronisiert aktive Aktion, Startzeit und Kampfphase. Das Clientmodell berechnet die sichtbare Pose aus derselben Zeitleiste; ausschließlich der Server löst die Wirkung einmal aus. Ein bloßer Client-Keyframe verursacht keinen Schaden.

Während einer Aktion bleibt der Zielpunkt für markierte Flächen und gerichtete Angriffe gespeichert. Ziele können ausweichen oder Deckung nutzen. Nahkampf und Flächen prüfen gültige Gegner und Sichtlinie; Strahlen werden durch solide Deckung abgeschnitten. Kreativ-/Zuschauerspieler und Verbündete werden durch den Gegnerfilter ausgeschlossen. Die Mobfamilie bekämpft sich nicht selbst. Der Mod entfernt für Angriffe keine Spielerblöcke und erzeugt kein zerstörendes Feuer.

Bossleisten verfolgen tatsächliche Gesundheit, werden an beobachtende Spieler gebunden und beim Entfernen bereinigt. Die Gesundheitsphasen erhöhen den Druck durch veränderte Werte/Abklingzeiten; speziellere Originalphasen sind weitere Inhaltsarbeit. Beim Speichern bleiben Angriffscursor und Cooldown erhalten. Ein halbfertiger Angriff wird beim Laden abgebrochen und eine Mindestpause gesetzt, damit sein Treffer nicht wiederholt wird.

Beschworene Helfer sind über UUID und absolute Ablaufzeit zugeordnet. Ein Beschwörer reserviert höchstens sechs Helfer gleichzeitig; Wegführen oder Chunkentladung umgeht diese Reservierung nicht. Zusätzlich gilt eine lokale Dichteprüfung. Die Lebensdauer endet nach höchstens 1.200 Welt-Ticks, und Helfer liefern keine normale Beute/Erfahrung. Die Zuordnung und Ablaufzeit werden gespeichert.

**Lokaler Quellfix nach dem ausgelieferten 0.4.0-Stand:** `QuestEvents.death` schließt einen `DungeonMob` mit `isSummoned()` von der Killgutschrift aus. `GearEvents.death` unterdrückt für diese temporären Helfer den Aufruf von `SoulEnergy.earn`; der direkte Heartstealer-Killeffekt bleibt erhalten. Diese Ergänzung ist zum Zeitpunkt dieser Dokumentationsaktualisierung noch nicht durch einen neuen Build-/GameTest-Lauf bestätigt und wird nicht als bereits in der ausgelieferten JAR enthalten ausgegeben.

`SoulBolt` ist ein begrenztes, ausweichbares Projektil. Ohne auflösbaren lebenden Besitzer verwirft es sich auf dem Server und trifft nicht plötzlich friedliche Tiere. Es prüft den überstrichenen geladenen Bereich vor der Kollision. Teleportation, Beschwörung und Strahlprüfung verwenden ebenfalls räumliche Prüfungen; Effekte sollen keine fehlenden Chunks laden.

## Friedliche Figuren, Händler und Befreiung

`PeacefulProfiles` führt **35 Begleiter, vier Wildtiere, zwei Schlüsselgolems, elf Händler und einen zivilen Schmied**. `ModPeacefulEntities` registriert passende Entity-Typen und Spawn-Eier. `DungeonsCreature` trennt die friedlichen Rollen und verwendet abhängig vom Profil Land-, Flug-, Wasser- oder amphibische Navigation. Besitz und Folgen/Warten werden serverseitig entschieden und gespeichert; aquatische Bewegung verwendet wirkliche Navigation statt einer erzwungenen Bildpose.

Im weitergeführten Quellstand ergänzen `WildlifeGroup` und `WildlifeGroupPolicy` eine begrenzte gespeicherte Mitgliedschaft und gemeinsamen Rhythmus für Wollkühe, Geier und Irrlichter. Boden-/Flug-Goals steuern tatsächliche Navigation; `FlockFlightPattern` berechnet nur gemeinsame Zielpunkte und setzt keine Entity-Position. Lokale Teilnehmerabfragen lösen keine entfernten UUIDs auf. `WildlifeSpawning` registriert Prädikate für die generierten Biome-Modifier; seine Live-Dichteprüfung läuft nur beim normalen Server-Spawning, nicht im Generierungs-Thread. Neue natürliche Tiere umgehen nicht mehr durch pauschale Persistenz den Kreaturen-Mobcap. Gruppendaten, natürlicher Herkunftsmarker und bisherige Besitzer-/Persistenzdaten werden getrennt gespeichert. [LIVING_WORLD.md](LIVING_WORLD.md) beschreibt Umfang und noch offene Spielprüfung.

`DungeonsMerchant` ist eine eigenständige `AbstractVillager`-Entity mit tatsächlichen `MerchantOffers`, Restocking und synchronisierter Begrüßung. Rechtsklick öffnet Handel; Schleichen plus Rechtsklick öffnet das Journal. Diese elf Figuren sind von den sechs zusätzlichen Vanilla-Dorfbewohnerberufen getrennt. Ihre normalen Warenangebote sind noch keine Original-Upgrade-, Geschenk-, Gild- oder Unique-Dienste.

Der weitergeführte Quellstand ergänzt für markierte Lagerhändler eine gespeicherte Heimat und einen eigenen KI-Goal. `MerchantRoutine` entscheidet ohne Minecraft-Abhängigkeit über Handel, begrenzte Flucht, vollständige Heimkehr, Ruhe und Tageswege sowie die Pausen zwischen Routenversuchen. Der Entity-Adapter liefert Weltbeobachtungen, verwaltet Heimat/Handelsmenü und führt echte Navigation in geladenen Bereichen aus. Dieser neue Stand ist noch nicht im Server oder Client abgenommen; Details und Grenzen stehen in [LIVING_WORLD.md](LIVING_WORLD.md).

Der `captured_blacksmith` ist CIVILIAN und öffnet kein Handelsfenster. Die Serverinteraktion prüft unter anderem Nähe und Sichtlinie, speichert bei der Befreiung die Retter-UUID und erlaubt anschließend nur diesem Spieler das Umschalten zwischen Folgen und Warten. `DungeonsKeeper`, `DungeonsWaiting` und `DungeonsCivilianRescued` erhalten den Zustand beim Laden. Der Renderer liest den synchronisierten Zustand; eine Darstellung befreit die Figur nicht selbst.

## Konstrukte und Ausrüstung

`DungeonConstruct` stellt Geomancer-Wand, Bombensäule, Ice Chunk und Entangle Vine als eigene begrenzte Entities dar. `ConstructService` prüft geladene Bereiche, Boden, Platz, Weltgrenze, Sichtlinie und zulässige Ziele vor der Erzeugung. Es gelten acht Reservierungen pro Besitzer sowie eine lokale Grenze von 32 Konstrukten. Besitzer-/Ziel-UUID, Phase und absolute Ablaufzeit werden gespeichert. Verlust eines gültigen Besitzers/Ziels und Ablauf beenden das Konstrukt; bereits verpasste Bomben-/Falltreffer werden nach dem Laden nicht sofort nachgeholt. Wände können Bewegung und Strahlen decken, ohne Spielerblöcke zu ersetzen. Der begrenzte `DungeonTrident` ist ein zusätzliches Projektil, keine fünfte Konstruktart oder neue Figur. Details: [CONSTRUCTS_AND_SWIMMING.md](CONSTRUCTS_AND_SWIMMING.md).

`GearContent` registriert acht `DungeonsArtifactItem`- und sechs `DungeonsWeaponItem`-Typen. Ihre vierzehn Quellenmodelle/-texturen, Rezepte und Bossbeute sind von der eigenen Minecraft-Mechanik getrennt. `SoulEnergy` verwaltet einen gemeinsamen serverseitigen Spielervorrat von höchstens 100 Seelen; Item-Komponenten spiegeln ihn nur für die Anzeige. Gültige feindliche Kills bringen bei mitgeführtem Seelenartefakt drei Seelen. Artefakte prüfen Kosten, Cooldowns, Ziele und Block-/Konstruktdeckung. Der Beacon benutzt den echten Benutzungszustand und `onStopUsing`, damit auch Hotbarwechsel oder Abbruch den Cooldown auslösen. Details und Balance: [GEAR_AND_SOULS.md](GEAR_AND_SOULS.md).

## Quellenmodelle und Animationslaufzeit

`MobRenderers` installiert den generierten `ReferenceModelIndex` und die nativen Kompositionen aus `BuiltinMobModels`; friedliche Renderer nutzen dieselben ausgewählten Modellverträge. `design/reference-models.json` ist die maßgebliche Auswahl. Java-, Bedrock/Gecko- und CEM-Konverter erzeugen ihre Adapter getrennt; `external_generator` schützt die extern erzeugten Klassen vor dem Java-Extraktor. Die Goety-Redstone-Monstrosität ist die aktive Bossdarstellung; das eigene Modell aus 0.2.1 bleibt historische Quelle.

`ReferenceMobModel`, Material-/Hand-Layer und benannte Gelenkpfade erhalten aktuelle Pose, Skalierung, Sichtbarkeit und die getrennten Texturen. Fehlende Bindungen oder Texturen sollen sichtbar benannt fehlschlagen. `ActionClipTiming` legt den belegten Quellkontakt an den serverseitigen Trefferzeitpunkt; CEM-Hostbewegungen und eigene Übergänge sind keine extrahierten Original-Dungeons-Keyframes. Native Minecraft-Geometrie wird über Modellfabriken und Spieltexturen über `minecraft:` referenziert. Die konkrete Herkunft einschließlich MIT-/CC-BY-NC-SA-Anteile steht in [THIRD_PARTY_NOTICES.md](../THIRD_PARTY_NOTICES.md) und `design/model-delivery-inventory.json`. Details: [REFERENCE_ANIMATION_RUNTIME.md](REFERENCE_ANIMATION_RUNTIME.md).

## Freiwillige Begegnungen

Jedes der **21 Miniboss-/Bossprofile** besitzt ein `EncounterSealItem`. Es ist eine eigene Minecraft-Sandbox-Mechanik, keine Behauptung über Originalitems aus Dungeons.

Die Benutzung verlangt Schleichen und einen Klick auf die Oberseite eines geeigneten Bodens. `EncounterService` prüft serverseitig Schwierigkeit, Baurechte, geladene Bereiche, Welt-/Baugrenzen, genügend feste Unterstützung, Kollisionen, Flüssigkeiten und andere Lebewesen im Spawnraum. Weitere nicht reguläre Dungeons-Gegner sind im um 24 Blöcke erweiterten Bereich ausgeschlossen.

Erst nach erfolgreicher tatsächlicher Entity-Erzeugung wird im Survivalmodus ein Siegel verbraucht. Alle 21 Siegel erhalten gemeinsam 200 Ticks Cooldown für diesen Spieler. Ein Fehler verbraucht kein Siegel. Der Mod baut keine Arena und verschiebt oder löscht keine vorhandenen Blöcke. Die 21 JSON-Aufgaben verwenden die registrierten Boss-/Miniboss-IDs; sie sind normale Aufgaben im bestehenden Questkern. Details: [ENCOUNTERS.md](ENCOUNTERS.md).

## Berufe und servergesteuerte Arbeitsanimationen

Die sechs zusätzlichen Dungeons-Berufe sind `VillagerProfession`-Einträge mit eigenen `PoiType`-Einträgen und Arbeitsplatzblöcken. Alle vier horizontalen Blockzustände einer Station gehören zu ihrer POI. Das Vanilla-Tag `minecraft:acquirable_job_site` macht sie für gewöhnliche arbeitslose Dorfbewohner zugänglich. Vanilla behält Berufswahl, Handel, Levelaufstieg, Restocking und Speicherung.

`MerchantTrades` ergänzt zwei Angebote je Stufe und Beruf, insgesamt 60. Normale `MerchantOffer`-Transaktionen verbrauchen die angezeigten Kosten. Zufällige Kuriositäten werden beim Erzeugen des Angebots ausgewählt und anschließend vor dem Kauf sichtbar angezeigt. Originaldienste wie Upgrade, Geschenktransfer und Enchantment-Reroll sind noch nicht implementiert.

`VillageAnimationEvents` beobachtet geladene Server-Dorfbewohner im Fünf-Tick-Raster. Arbeit benötigt einen erwachsenen wachen Dorfbewohner, aktive Arbeit, eine passende nahe geladene Job-POI, geringe Bewegung und einen kürzlichen `LAST_WORKED_AT_POI`-Eintrag von Vanilla. Eine Arbeitssequenz dauert höchstens 80 Ticks. Panik und Handel haben Vorrang.

Ein kleiner S2C-Payload übermittelt Modus, UUID und Zeitbereich an Tracking-Clients. Der Client führt keine erfundene Brain-Simulation aus. Er begrenzt seinen Cache, glättet Anfang/Ende und bereinigt Zustände beim Weltwechsel. Sichtbare Werkzeuge sind ausschließlich Requisiten, keine neuen Inventargegenstände.

Der eigene Renderer enthält Bewegungen für 13 Vanilla-Berufe plus sechs Händler. Vanilla-Biomkleidung, Hüte, Berufsmarken und Kopfausrüstung werden über die vorhandenen Ebenen weiter dargestellt. Bei installiertem EMF tritt er standardmäßig zurück. Die separate Clientkonfiguration liegt in `dungeonsalive-villagers-client.toml`. Details: [VILLAGERS_AND_ANIMATIONS.md](VILLAGERS_AND_ANIMATIONS.md).

## Clientoberflächen und Netzwerkgrenzen

Journaltaste **J** und Schaltflächen verwenden normale Serverbefehle. `JournalPayload` überträgt ausschließlich Server → Client: Öffnen-Flag und JSON für `JournalView`. Zusätzlich gilt eine Grenze von 196.608 Zeichen. Einzeltextgrenzen allein garantieren nicht, dass jede beliebig große Kombination in ein Journal passt. Der Quellenkatalog wird nicht als riesiger Journal-Payload verschickt.

Gemeinsame Bridge-Klassen enthalten nur Empfänger ohne direkte Minecraft-Clientklassen. Der physische Clienteinstieg installiert die Darstellung. Dedicated Server benötigen keine Renderer.

Das Handbuch auf **B** erstellt ausschließlich lokale Vorschauobjekte. Es ruft deren normale KI-Ticks nicht auf, fügt sie keiner Welt hinzu und sendet keinen Spawnauftrag an den Server. Für die Ansicht werden nur visuelle Tick-/Lauf-/Aktionswerte fortgeschrieben. Eine ausdrücklich markierte Villager-Vorschau darf eine Arbeitssequenz zeigen, ohne zu behaupten, dass ein echter Bewohner gerade arbeitet.

**B → Ausrüstung** öffnet `GearGuideScreen` mit den vierzehn tatsächlich registrierten Itemmodellen sowie Beschreibung und Werten. Diese GUI-Vorschau erzeugt weder Gegenstände im Inventar noch Artefaktwirkungen.

Schmetterlinge, Glühwürmchen und Sporen bleiben lokale animierte Partikel. Auswahl und Häufigkeit berücksichtigen Tageszeit, Biom, Regen und Grafikoptionen. Es werden nur geladene Bereiche betrachtet. Die Standardgrenze beträgt 96 automatisch erzeugte Partikel; Weltwechsel räumen lokale Referenzen auf. Diese Dekoration ist keine interaktive Tierwelt.

## Optionale Inhalte und Tests

`examples/datapacks/dungeons_wilds` ergänzt natürliche reguläre Gegner über NeoForge-Biommodifikatoren; es wird nicht automatisch aktiviert. Sechs Biomgruppen enthalten zwölf unterschiedliche reguläre IDs mit niedrigen Gewichten. Boss-/Minibossspawns gehören nicht zu diesem Pack. Normale Monster-Spawnregeln, Moblimits und Lichtbedingungen gelten weiterhin.

`examples/datapacks/dungeons_camps` ergänzt optional einen Händlerplatz und ein Gefangenenlager über eigene Jigsaw-/Strukturvorlagen. Sie bestehen aus gewöhnlichen Minecraft-Blöcken und enthalten tatsächliche Händler-/Schmied-Entities. Das Pack wirkt bei natürlicher Generierung nur in neuen geeigneten Chunks. `tools/generate_dungeons_camps.py` erzeugt die Vorlagen; `CampGameTests` prüft im gesonderten Camp-Profil geladene Registries, echte Vorlagenplatzierung und Interaktionen. Gestellte Client-Aufnahmen dieser Vorlagen belegen keine Suche über natürliche Weltsamen oder Hanglagen. Details: [DUNGEONS_CAMPS.md](DUNGEONS_CAMPS.md).

Eigener `dungeonsalive:enchanter` und optionaler `enchantwithmob:enchanter` bleiben getrennt. Der Fremdmod-Installer pinnt Versionen und Prüfsummen, statt beliebige aktuelle Dateien zu kombinieren. Katalog, Quellcode und getestete optionale Profile sind unterschiedliche Nachweise.

JUnit prüft Questkern, Parser, Persistenz, Animations-/Rig-/Materialverträge und zusätzliche Zustandsregeln. Server-GameTests prüfen reale Registrierungen, Handel, Vanilla-Berufsübernahme, Arbeitsbeobachtung, Kampfframes, Deckung, Projektile, Laden, Begegnungen, Konstrukte, Seelen, Ausrüstung sowie echte Folge-/Wasserbewegung. Eigenständige Batches trennen verschiedene Testszenarien. Das Camp-Profil wiederholt Core-Fälle und ergänzt zwei eigene Fälle; wiederholte Fälle werden nicht als zusätzliche Tests gezählt. Die tatsächlich ausgeführten Läufe des Auslieferungsstands stehen in [VALIDATION.md](VALIDATION.md); die weitere visuelle und Mehrspielerabnahme in [MANUAL_TESTS.md](MANUAL_TESTS.md). Diese Dokumentationsaktualisierung behauptet keinen erneuten Build, Server- oder Clientlauf.
