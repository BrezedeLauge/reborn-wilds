# Kreaturen und Kämpfe – implementierter Stand

Stand: **2. November 2026**, Version **0.4.0-alpha**. Plattform: **Minecraft Java 1.21.1 / NeoForge 21.1.265 / Java 21**. Ausgeführte Prüfungen und grafische Befunde gehören nach [VALIDATION.md](VALIDATION.md).

**Weitergeführter Stand: Modbuild bestanden, neue Spielprüfung offen.** Wollkuh-Herden und Geier-/Irrlicht-Fluggruppen haben gemeinsame gespeicherte Mitgliedschaft, begrenzte lokale Bewegung, Nachfolge und endlichen Schadensalarm. Natürliche Habitate sind direkt als Biome-Modifier eingebunden; Haustiere und manuelle/ältere persistente Tiere behalten ihre Regeln. Details: [LIVING_WORLD.md](LIVING_WORLD.md). Zusätzlich bindet `DungeonMob` jede vorbereitete Aktion an die ursprüngliche Ziel-UUID: Zielwechsel, Tod, Entfernung oder Verlust eines gültigen Gegners brechen mit normalem Cooldown ab. Bereits ausgelöste Projektile/Konstrukte behalten ihren eigenen Lebenszyklus. NBT-Reuse setzt keine halbfertige Aktion fort. Diese Änderungen erhöhen keine Figurenanzahl und sind noch im echten Minecraft zu prüfen. Die 97 JUnit-Tests des vollständigen Builds ersetzen diesen Serverlauf nicht; [Buildnachweis](../reports/current-build-validation.json).

## Tatsächlicher Umfang

Die aktuelle Registrierung enthält **131 Figuren**: **78 feindliche** und **53 friedliche** Entities. Unter den Gegnern sind **57 reguläre Profile, neun Minibosse und alle zwölf regulären Dungeons-I-Bossgrundtypen** einschließlich DLCs und Treetop Tangle. Die friedlichen Profile umfassen **zwei Schlüsselgolems, vier Wildtiere, 35 Begleiter, elf Händler und einen befreibaren Zivilisten**. Diese Zahlen zählen auch ausdrücklich registrierte Rüstungs-, Farb- und Begleitervarianten; sie bedeuten nicht 131 vollständig neue Spezies.

Zusätzlich existieren **vier zeitlich begrenzte Konstrukte**: Geomancer Wall, Geomancer Bomb, Ice Chunk und Entangle Vine. Sie sind gesonderte `MISC`-Entities ohne gewöhnliches Gegnerprofil, natürliche Spawns oder Spawn-Eier. Die beiden Projektiltypen Soul Bolt und geworfener Dreizack werden ebenfalls nicht als weitere Kreaturen mitgezählt. Mechaniken, Lebenszeit, Besitz, Deckung und API stehen in [CONSTRUCTS_AND_SWIMMING.md](CONSTRUCTS_AND_SWIMMING.md).

Verbindliche Zuordnung von Registrierung, Katalog, Modell und Implementierungsgrenzen:

- [implementation-status.json](../design/implementation-status.json): gemeinsamer aktueller Laufzeitstatus mit tatsächlich registrierten IDs und noch offenen Katalogeinträgen.
- [implemented-mobs.json](../design/implemented-mobs.json): aus `MobProfiles.java` erzeugte Gegner, Attribute und Aktionsfolgen.
- [peaceful-profiles.json](../design/peaceful-profiles.json): tatsächlich registrierte friedliche Profile.
- [reference-models.json](../design/reference-models.json), [builtin-models.json](../design/builtin-models.json) und [model-delivery-inventory.json](../design/model-delivery-inventory.json): ausgewählte Modellbindungen, native Kompositionen und konkrete Lieferdateien.

Der umfangreichere [Inhaltskatalog](MOB_CATALOG.md) ist ein Recherche- und Ausbauverzeichnis. Eine Katalogzeile, ein vorhandener Vanilla-Verwandter oder eine bereits importierte Hilfsgeometrie ist noch keine registrierte Dungeons-Entity. Die Sift bleiben entsprechend der Nutzerentscheidung ausgeschlossen. Andere offene Dungeons-II- und Ancient-Varianten werden im Statusmanifest geführt und nicht als fertig mitgezählt.

Die vorhandene Spiellogik ist eine **serverseitige Minecraft-Adaption**: frei bauen, abbauen und erkunden bleibt erhalten. Die Modelle sind deutlich weiter ausgearbeitet als die früheren Familienprototypen. Das macht die noch teilweise vereinfachten Originalkämpfe, Spezialfähigkeiten und Begegnungsabläufe nicht automatisch vollständig.

## Modell- und Animationsgrundlage

Alle aktuell registrierten Figuren haben eine ausdrückliche Quellen-/Eigenmodellbindung oder native Komposition. Die früheren gemeinsamen Illager-, Golem-, Pflanzen- und anderen Familienmodelle sind keine zutreffende Beschreibung des aktuellen Lieferbestands. Ihr älterer Code bleibt ein Entwicklungsvorgänger beziehungsweise Fallback für zukünftige ungebundene Profile.

| Grundlage | Was tatsächlich verwendet wird | Was dadurch noch nicht portiert wird |
| --- | --- | --- |
| Goety | Veröffentlichte Java-Geometrie, passende Texturen und vorhandene benannte Keyframeclips; etwa für Redstone Monstrosity, Royal Guard, Necromancer und Enderlings. | Die vollständige Goety-KI, die ursprünglichen Dungeons-Angriffe oder deren Arenen. |
| Dungeons Mobs und DungeonsMobs-Port | Ausgewählte Java- und Bedrock-/Gecko-Geometrien, Texturen und konvertierte Quellclips; per-face UVs bleiben erhalten. | Ein allgemeiner GeckoLib-Loader oder sämtliche Inhalte der Quellmods. |
| ReCrafted Mobs | Ausgewählte CEM-Modelle, Materialien und deklarierte Ausdrücke; passende native Hostbewegungen erhalten ihre tatsächlichen Gelenke. | Proprietäre Dungeons-Keyframes. Ein Warden-Hostclip bleibt eine erkennbare Minecraft-Adaption. |
| Installiertes Minecraft | Native Modellfabriken und `minecraft:`-Texturen für bestimmte Tiere, Hauptleute, Skelettpferd/Reiter sowie die Schleimgeometrie. | Eine zweite Kopie der Minecraft-Assets oder automatisch die KI der entsprechenden Vanilla-Klasse. |
| Eigene Ausarbeitung | Eigenes Vengeful-Heart-Modell und eigene Anpassungen, Gesten, Zeitabbildung und Effekte. Treetop Whisperer ist eine dokumentierte größere, dunkler/violett angepasste Goety-Whisperer-Ableitung. | Eine behauptete Originaldatei aus Minecraft Dungeons oder Actions & Stuff. |

Die **Redstone-Monstrosität verwendet jetzt das vom Nutzer verlangte Goety-Quellenmodell** mit dessen Rig, Texturen und Clips. Das selbst erstellte 0.2.1-Modell samt Generator und Blockbench-Export bleibt ein historischer Bearbeitungsstand; es ist nicht mehr die aktuelle Darstellung. Herkunft, Änderungen und unterschiedliche Lizenzbedingungen stehen in [SOURCE_MODEL_AUDIT.md](SOURCE_MODEL_AUDIT.md) und [THIRD_PARTY_NOTICES.md](../THIRD_PARTY_NOTICES.md).

Die technische Laufzeit ist in [REFERENCE_ANIMATION_RUNTIME.md](REFERENCE_ANIMATION_RUNTIME.md) beschrieben. Sie erhält Elternhierarchie, Pivots, UVs, statische Skalen, getrennte Materialien, Handanker und vorhandene Clips. Leuchtmasken, transparente Hüllen und nummerierte Effektframes besitzen eigene Renderpässe. Eine strikte Prüfung lehnt fehlende Bones, Texturen oder unbekannte Ausdruckseingänge ab.

## Reguläre Gegner

Die 57 regulären Profile sind im Gegnermanifest vollständig mit Registry-ID und Werten aufgelistet. Sie umfassen unter anderem Royal Guard, Vanguard, Enchanter, Necromancer, Geomancer, Wraith, Iceologer, Wind Caller, Whisperer/Wavewhisperer, Leapleaf, die drei Enderlings, Redstone Cube, Conjured Slime, Squall Golem, Mountaineer, Vindicator Chef, Flame Construct, Tower Guard/Wraith sowie ausgewählte Pflanzen-, Unterwasser-, Piglin-, Zombie- und Skelettvarianten. Neu in 0.4 sind Tropical Slime, Armored Trident Drowned und die ausdrücklich getrennten Diamond-Rüstungstiers von Pillager und Vindicator.

Dabei sind charakteristische Mechanikgrenzen ausdrücklich zu beachten:

- Geomancer erzeugt jetzt echte, zerstörbare temporäre Pfeiler und einen verzögert zündenden Bombenpfeiler mit den importierten Quellrigs und Erscheinungsclips. Platzprüfung, Besitzergrenze und Deckung sind implementiert; beliebige Originalformationen und sämtliche Dungeons-Kampfabläufe bleiben offen.
- Whisperer, Wavewhisperer und Treetop Whisperer erzeugen für `ROOT_SNARE` eine echte Fesselranke. Sie wächst, verlangsamt kurzzeitig nur ihr festgelegtes Opfer im Wirkungsbereich und verfällt. Sie zieht und teleportiert niemanden. Andere Pflanzenprofile besitzen weiterhin ihre ausdrücklich zugewiesenen Zonen oder Geschosse; nicht alle Original-Gift-/Aufwuchsmechaniken sind vorhanden.
- Iceologer erzeugt einen sichtbaren Eisblock, der nach seiner Formation tatsächlich fällt, mit dem Gelände kollidiert und einmalig Frostschaden auslöst. Wretched Wraith behält dagegen seine bisherige vorbereitete Frostzone; identische Aktionsnamen erzwingen keine identischen Objekte.
- Mobile aquatische Gegner erhalten Land-/Wassernavigation, wirkliche dreidimensionale Wasserbewegung und einen synchronisierten Schwimmzustand. Die Darstellung liest diesen Zustand; bloßes Stehen in Wasser erzwingt keine waagrechte Schwimmpose.
- Armored Trident Drowned führt und wirft einen echten Dreizack mit begrenzter Lebenszeit und ohne Pickup. Diamond Armored Vindicator hält das tatsächlich registrierte Whirlwind-Item; seine Angriffe aktivieren keine zusätzlichen Spielerwaffen-Callbacks. Tropical Slime besitzt transparente Hülle und die aus der Quelle übernommene nummerierte Fischanimation im Inneren.
- Wraith und Wildfire schweben in der Darstellung. Die feindliche Grundnavigation wird dadurch nicht zu frei fliegender oder wanddurchdringender KI.
- Icy Creeper besitzt jetzt einen eigenen gezündeten Frostangriff mit Schaden und Verlangsamung, verschwindet anschließend und zerstört keine Blöcke.
- Rüstungs- und Waffenvarianten erhalten ihre echten Quellengeometrien beziehungsweise tatsächlich ausgerüsteten Minecraft-Gegenstände. Ihre Werte und Angriffsfolgen sind weiterhin Projektbalancing.

Der eigene Verzauberer ist `dungeonsalive:enchanter`. Ein separat installierter Verzauberer einer anderen Mod bleibt eine andere Registry-ID und ein anderes Questziel. Alle folgenden Projekt-IDs verwenden den Namespace `dungeonsalive:`.

## Neun Minibosse

| ID | Aktionsfolge in dieser Fassung | Darstellung / konkrete Grenze |
| --- | --- | --- |
| `redstone_golem` | Schwerer Schlag → Flächenpuls → Nahkampf | Quellenrig mit eigenen Serveraktionen. |
| `endersent` | Teleport → schwerer Schlag → Nahkampf | Quellenmodell und zugeordnete Quellclips; Teleport bleibt die sichere Minecraft-Adaption. |
| `drowned_necromancer` | Beschwörung → Strahl → Seelengeschoss | Tatsächlicher ertrunkener Quellhumanoid mit Schwimmclips; keine frühere Skelettfamilie mehr. |
| `wildfire` | Schildhaltung → Salve → Flächenpuls | Quellenmodell mit Schildkranz; vier unabhängig zerstörbare Schild-Entities sind noch nicht umgesetzt. |
| `illusioner` | Teleport → Salve → Seelengeschoss | Passender Bedrock-Illager-Rig und vorhandene Quellclips, echter Bogen am Handanker; keine vollständige Klonbegegnung. |
| `pillager_raid_captain` | Salve → Verstärkung → Nahkampf | Nativer Illager, tatsächlich gehaltene Armbrust und ausgerüstetes Hauptmannbanner. |
| `vindicator_raid_captain` | Schildhaltung → schwerer Schlag → Nahkampf | Nativer Illager, tatsächliche Axt und Banner. |
| `rampart_captain` | Schildhaltung → schwerer Schlag → Nahkampf | Ausdrückliche Quellenmodellbindung und eigener registrierter Miniboss. |
| `skeleton_horseman` | Salve → Sprung → Nahkampf | Nativer Pferderig mit eigenem nativem Skelettreiter und Bogen; weiterhin eine gemeinsame Gameplay-Entity. |

Die beiden Raid Captains starten keinen erzwungenen Vanilla-Raid. Beim Skelettreiter sind Absteigen, getrennte Gesundheit und Zweientitäten-Reit-KI offen. Native Vanilla-Endermen, Evoker, Ghasts und Elder Guardians werden durch ihre bloße Existenz nicht als zusätzliche fertige Dungeons-Minibosse dieses Projekts gezählt.

## Alle zwölf Dungeons-I-Bossgrundtypen

Die Tabelle beschreibt die **aktuelle Darstellung und Serverfolge**, nicht einen vollständig rekonstruierten Originalkampf. Lebenspunkte und andere Balancingwerte stehen im erzeugten Gegnermanifest.

| Boss-ID | Aktuelle Modellgrundlage | Aktionsfolge in dieser Fassung |
| --- | --- | --- |
| `arch_illager` | ReCrafted-CEM, Arch-Illager-Unterbaum | Königliche Wachen beschwören → Salve → Teleport → Seelengeschoss |
| `corrupted_cauldron` | ReCrafted-CEM-Kessel | Schleime beschwören → markierter Flächenpuls → Salve; unbeweglich |
| `heart_of_ender` | ReCrafted-CEM, Ender-Unterbaum | Strahl → Flächenpuls → Salve → Teleport |
| `mooshroom_monstrosity` | DungeonsMobs-Port-Rig und passende Pilztextur | Schwerer Schlag → Salve → großräumiger Stoß |
| `nameless_one` | ReCrafted-CEM | Vorhuten beschwören → Seelengeschoss → Salve → Teleport |
| `redstone_monstrosity` | Goety-Modell, Texturen und Keyframeclips | Schwerer Schlag → Redstone-Würfel beschwören → Flächenpuls |
| `jungle_abomination` | ReCrafted-CEM mit Warden-Host | Schwerer Schlag → Fesselzone → Flüsterer beschwören |
| `treetop_whisperer` | Größere, dunkel/violett angepasste Goety-Whisperer-Ableitung | Temporäre Fesselranke → Salve → Flüsterer beschwören |
| `wretched_wraith` | ReCrafted-CEM | Frostzone → Salve → Teleport |
| `tempest_golem` | ReCrafted-CEM mit Warden-Host | Schildhaltung → Sturmstoß → schwerer Schlag → Strahl |
| `ancient_guardian` | ReCrafted-CEM mit Guardian-Host | Strahl → Stoß → Wellenflüsterer beschwören; aquatische Bewegung |
| `vengeful_heart_of_ender` | Eigenes ausgearbeitetes Modell und eigene Clips | Strahl → Salve → schwerer Schlag → Teleport → Flächenpuls |

Alle Bosse besitzen eine serverseitige Lebensleiste. Unter 50 % Leben beginnt eine zweite Tempostufe: Die Leistenfarbe wechselt, Aktionspausen werden kürzer und Salven breiter. Vengeful Heart erhält unter 25 % eine weitere Tempostufe. Die Vorbereitung und der Freigabezeitpunkt bleiben dabei unverändert. Mehrere vollständige Originalkörperformen, Arenenzustände und Original-Phasenfolgen sind dadurch nicht automatisch vorhanden.

Tempest Golem besitzt insbesondere noch keine vollständige stationäre Zwei-Säulen-/Verwundbarkeitsbegegnung. Nameless One besitzt noch nicht seinen kompletten Klonablauf. Treetop verwendet in der aktuellen Folge Flüsterer als Helfer; die präzise Originalbegleiter- und Giftmechanik ist weiterhin ein Ausbaupunkt. Diese Grenzen bleiben auch bei wesentlich besseren Modellen bestehen.

## Friedliche Kreaturen, Begleiter und Händler

`DungeonsCreature` ist eine echte `PathfinderMob`-Entity ohne die feindliche Zielsuche von `DungeonMob`. Besitz, Warten und Navigation werden auf dem Server verwaltet und gespeichert. Begleiter und Schlüsselgolems können einem Spieler zugeordnet werden und folgen ihm innerhalb der vorhandenen Navigation. Eine weitere Interaktion schaltet zwischen Folgen und Warten um. Bei den unterstützten Katzen-/Fuchshosts setzt Warten zusätzlich den tatsächlichen Sitzzustand; andere Körperformen bleiben ohne erfundene Sitzpose stehen.

Fliegende Profile verwenden Flugnavigation, aquatische Profile Wassernavigation. Die eigenen Wanderzielabfragen prüfen geladene Chunks, bevor sie Terrain oder Flüssigkeit abfragen. Diese Kreaturen sind echte gespeicherte Welt-Entities; Umgebungsfunken und Partikel sind weiterhin nur ergänzende Dekoration. Der weitergeführte Quellstand ergänzt begrenzte Herden/Fluggruppen, Wollkuh-Zucht, seltene Sparschwein-Schätze und zwei selbst gebaute Schlüsselgolem-Tore; Umfang und ausstehende native Prüfung stehen in [LIVING_WORLD.md](LIVING_WORLD.md). Weitergehende Populationsplanung und sämtliche Dungeons-Begleiterfähigkeiten sind damit noch nicht abgeschlossen. Die Tore bilden noch keine vollständige Original-Dungeonmechanik.

Die Unterwassersteuerung des aquatischen Begleiters erhält ihren tatsächlichen Neigungswinkel; ein eigener Servertest verlangt einen Höhengewinn auf einer Wasserroute. Die gemeinsame Folge-KI sieht beide Tick-Paritäten, damit ihr Zehn-Tick-Pfadintervall auch bei ungerader Entity-ID erreicht wird. Eine zweite Regression befreit einen solchen Schmied und prüft seine wirkliche Annäherung an den registrierten Retter.

Die **elf Händler** sind `blacksmith`, `gift_wrapper`, `mystery_merchant`, `village_merchant`, `luxury_merchant`, `piglin_merchant`, `enchantsmith`, `tower_keeper`, `powersmith`, `uniquesmith` und `gildsmith`. Sie verwenden `DungeonsMerchant`, eine echte `AbstractVillager`-Handelsentity. Normale Interaktion öffnet das tatsächliche Handelsfenster; Blacksmith, Powersmith und Enchantsmith öffnen im weitergeführten Quellstand stattdessen ihren eigenen [Ausrüstungsdienst](GEAR_AND_SOULS.md), dessen Warenknopf weiterhin zum normalen Handel führt. Interaktion mit Schleichen öffnet den vorhandenen Questzugang. Eine serverseitige Begrüßungsgeste begleitet diese Interaktion beziehungsweise einen Handel. Angebote und Wiederauffüllung werden serverseitig gespeichert und verwaltet.

Die Angebote sind passende Minecraft-Waren. Drei eigene Dienste für Reparatur, begrenzte Waffenaufwertung und sechs Verzauberungen sind im neuen Quellstand implementiert. Vergoldung, beliebiger Eigenschaftsneuwurf, Geschenkübertragung und komplette Towerdienste bleiben weitere Ausbaupunkte. Die elf Dungeons-Händler sind außerdem von den Vanilla-Dorfbewohnerberufen und den eigenen Dorfberufen zu unterscheiden; deren Arbeit und Animationen stehen in [VILLAGERS_AND_ANIMATIONS.md](VILLAGERS_AND_ANIMATIONS.md).

Der zusätzliche `captured_blacksmith` gehört zur Rolle `CIVILIAN`. Er wartet zunächst gefangen und kann durch eine tatsächliche Serverinteraktion befreit werden; danach folgt er dem Retter. Diese Interaktion öffnet kein Handelsfenster und vergibt keinen Gegenstandsbonus. Er ist eine gesonderte befreibare Figur und kein zwölfter Händler. Vorbereitete Lager und ihr geschützter Platzierungsweg werden in [DUNGEONS_CAMPS.md](DUNGEONS_CAMPS.md) beschrieben.

## Serveraktionen und sichtbare Zeitpunkte

`AttackKind` besitzt **15 Aktionsarten**. Die synchronisierte Aktions-ID und Startzeit verbinden Server und Darstellung; der Client verschickt keine Treffernachricht. Ein zugeordneter Quellclip wird so zeitlich abgebildet, dass sein ausdrücklich festgelegter Kontakt genau auf dem Serverfreigabetick liegt. Danach blendet die Pose in die tatsächliche Ruhe-/Bewegungshaltung zurück. Eine Quelle ohne passenden Originalclip erhält eine dokumentierte Adaption, keine erfundene Herkunftsbehauptung.

| Aktion | Vorbereitung | Erholung | Anschließender Cooldown | Tatsächlicher Effekt |
| --- | ---: | ---: | ---: | --- |
| `MELEE` | 14 | 8 | 18 | Nahkampfschaden und angenommener Rückstoß |
| `HEAVY_SLAM` | 30 | 14 | 28 | Vorbereiteter schwerer Bereichstreffer |
| `SOUL_BOLT` | 24 | 10 | 28 | Eigenes Magiegeschoss; beim Armored Trident Drowned tatsächlicher Dreizackwurf |
| `VOLLEY` | 26 | 12 | 36 | Gestreute Geschosse |
| `FIRE_FIELD` | 36 | 16 | 42 | Beim Geomancer Erzeugung des Bombenpfeilers; sonst markierter einmaliger Seelenfeuerpuls |
| `ICE_FALL` | 36 | 14 | 40 | Beim Iceologer Erzeugung des fallenden Eisblocks; sonst Frostzone mit Verlangsamung |
| `ROOT_SNARE` | 32 | 12 | 38 | Geomancer: Pfeilerformation; Whisperer-Arten: temporäre Fesselranke; übrige zugeordnete Profile: vorbereitete Fesselzone |
| `BEAM` | 44 | 16 | 38 | Vorgewarnter einzelner Strahltreffer mit Blockprüfung |
| `STORM` | 42 | 18 | 50 | Großer Stoß im Nahbereich |
| `SUMMON` | 38 | 14 | 90 | Bis zu drei Helfer innerhalb der Bestandsgrenzen |
| `TELEPORT` | 20 | 12 | 30 | Geprüfter kurzer Ortswechsel |
| `GUARD` | 10 | 38 | 35 | Aktive Frontschutzphase |
| `ENCHANT` | 30 | 12 | 70 | Kurzzeitige Verstärkung von bis zu drei Verbündeten |
| `LEAP` | 24 | 16 | 30 | Tatsächliche Sprungbewegung mit späterem Landungsstoß |
| `ICY_EXPLOSION` | 25 | 5 | 60 | Frostschaden und Verlangsamung im 3,2-Block-Bereich, danach verschwindet der Angreifer |

Alle Zeiten sind Spielticks; regulär sind das 20 Ticks pro Sekunde. Die bei `ICY_EXPLOSION` deklarierten Erholungs-/Cooldownwerte werden nach dem eigenen Verschwinden nicht mehr ausgespielt. Der Frostangriff erzeugt Partikel und Geräusch, jedoch keine blockzerstörende Weltexplosion.

Bei Konstrukten ist der Freigabetick des Zaubernden der Erzeugungszeitpunkt. Formation, Zündung, Fall oder Verfall laufen anschließend auf der eigenen synchronisierten Objektzeitleiste. Der Bombenschaden entsteht daher erst nach der zusätzlichen sichtbaren Zündung, der Eisschaden erst bei wirklicher Kollision. Diese Trennung verhindert einen unsichtbaren frühen Treffer während einer noch laufenden Erscheinungsanimation.

Ein beim Speichern laufender Angriff wird nach dem Laden beendet. Eine Mindestpause verhindert einen unmittelbar wiederholten Treffer. Ein lokaler Feldführer kann ausgesuchte Posen und Händlergesten zeigen, ohne eine Weltentity hinzuzufügen oder einen serverseitigen Angriff auszulösen. Seine lokale Animationsuhr läuft auch bei pausiertem Singleplayer weiter.

## Regeln für die frei bebaubare Welt

- **Keine eigene Blockzerstörung durch diese Aktionen:** Flächenzauber, Geschosse, Sprünge, Teleports und Frostexplosion platzieren kein Feuer und verändern keine Landschaftsblöcke.
- **Ausweichbare Zielstellen:** Flächenziele und Strahlrichtung werden bei Aktionsbeginn festgelegt. Der Strahl prüft beim Treffer erneut feste Deckung.
- **Begrenzte Beschwörung:** Höchstens sechs reservierte, lebende beziehungsweise noch nicht abgelaufene Helfer je Beschwörer. Gespeicherte UUID-Reservierungen verhindern, dass Wegführen oder Chunkentladen die Grenze umgeht. Zusätzlich gilt ein lokaler Grenzwert von 32 Dungeon-Mobs im abgefragten 32-Block-Umfeld.
- **Endliche Helfer:** Die absolute Frist beträgt 1.200 Welt-Spielticks. Abgelaufene Helfer verschwinden auch nach erneutem Laden; beim geladenen, toten Beschwörer ebenfalls. Sie liefern keine Beute oder Erfahrung. Im weitergeführten Quellstand werden solche durch `DungeonsSummoner` markierten Helfer zusätzlich von Seelengewinn und Quest-Killfortschritt ausgeschlossen; der gespeicherte Marker gilt auch nach dem Laden. Heartstealer-Heilung bleibt als direkter Kampfeffekt erhalten. Dieser Zusatz ist noch nicht im ausgelieferten JAR; ausstehende Serverprüfung siehe [VALIDATION.md](VALIDATION.md).
- **Endliche Konstrukte:** Höchstens acht gespeicherte Reservierungen für Konstrukte/Dreizacke je Besitzer sowie ein eigener lokaler Grenzwert von 32 Konstrukten. Auflösbarer lebender Besitzer, gültiges Ziel, absolute Lebenszeit und geladene Kollisionsbereiche werden erneut geprüft. Versäumte Zündungen werden nach dem Laden nicht als Soforttreffer nachgeholt.
- **Geladene Kollisionsbereiche:** Zusätzliche Geschoss-, Strahl-, Teleport- und Beschwörungsabfragen prüfen die betroffenen Chunks vor eigenen Terrainabfragen. Die normale geerbte Minecraft-Navigation bleibt davon getrennt.
- **Definierte Ziele:** Die bisherige Magie und Flächenwirkungen greifen regulär Spieler, Golems und das ausdrückliche Kampftarget an; eigene Dungeon-Mobs bleiben verbündet. Die neuen Konstrukte und Dreizacke schließen passive Tiere, Dorfbewohner und Haustiere auch bei manuell gesetztem Ziel aus. Ein Geschoss ohne auflösbaren lebenden eigenen Beschwörer wird verworfen.
- **Normale Schadensregeln:** Minecraft-Schadensquellen erlauben Rüstung, Widerstand und Ereignisbehandlung. Nahkampfrückstoß folgt nur einem angenommenen Schadensaufruf.
- **Manuelle Bossbegegnungen:** Der Grundmod verteilt diese Bosse nicht zufällig in bestehenden Welten. Begegnungssiegel, Spawn-Eier und `/summon` erlauben gezielte Begegnungen; Regeln und Rezepte stehen in der README.

Beispiele zum Prüfen in einer eigenen Testwelt:

```mcfunction
/summon dungeonsalive:royal_guard ~ ~ ~
/summon dungeonsalive:redstone_monstrosity ~ ~ ~
/summon dungeonsalive:key_golem ~ ~ ~
/summon dungeonsalive:blacksmith ~ ~ ~
```

## Prüfgrenzen und Weiterarbeit

Quellmodell, echte Registrierung, funktionierende Aktion und grafische Abnahme bleiben getrennte Nachweise. Ein bestandener Rigtest prüft Hierarchie, UV-/Texturbindung, gültige Pose und Zeitabbildung. Er beweist weder vollständige Originaltreue noch überzeugenden Bodenkontakt, korrekt ausgerichtete Waffen oder den gesamten Bosskampf. Für diese Punkte werden echte Weltansichten und Bewegungs-/Aktionsfolgen im Client geprüft.

Die maßgeblichen aktuellen Ergebnisse stehen in [VALIDATION.md](VALIDATION.md), der [Animationsmatrix](../design/animation-coverage.json) und den dort verknüpften Aufnahme-/Reviewberichten. Die neuen Common-Mechaniken einschließlich aller sechs Konstrukt-/Wassertests und der beiden Begleiter-Navigationsregressionen haben im zentralen Korrekturlauf bestanden. Die grafische Abnahme der 0.4-Fassung wird gesondert anhand tatsächlicher Aufnahmen dokumentiert. Während ein Aufnahmelauf noch läuft, darf seine Vollständigkeit nicht aus der Zahl registrierter Figuren abgeleitet werden.

Für Änderungen: Gegnerprofile in `entity/MobProfiles.java`, friedliche Profile in `entity/peaceful/PeacefulProfiles.java`, Serveraktionen in `entity/DungeonMob.java` und `entity/AttackKind.java`, Darstellungsbindungen in den Importmanifesten pflegen. Aktuelle Renderer stehen unter `client/entity/reference`; neue Clientfunktionen gehören nicht in Common-Klassen. Konkrete nächste Animations- und Mechanikschritte stehen in [ANIMATION_PRODUCTION.md](ANIMATION_PRODUCTION.md).
