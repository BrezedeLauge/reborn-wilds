# Integrationstests im echten NeoForge-Server

Prüfplan für **0.4.0-alpha**, Minecraft Java **1.21.1**, NeoForge **21.1.265**, Java **21**. Die folgenden Tabellen beschreiben, was ein erfolgreicher Test belegt. Sie nehmen keinen ausstehenden Lauf als bestanden vorweg. **Die tatsächlich akzeptierten Ergebnisse und Gesamtzahlen stehen ausschließlich im aktuellen [Validierungsbericht](VALIDATION.md).**

GameTests laufen mit registrierter Mod und geladenen Serverressourcen in Minecrafts tatsächlichem Server. Sie ergänzen JUnit, den nativen Grafiklauf und die [manuellen Spieltests](MANUAL_TESTS.md). Keine dieser Ebenen ersetzt die anderen.

**Aktueller Stand:** Der vollständige lokale Modbuild mit 97 JUnit-Tests in 23 Klassen besteht. Die neuen GameTests sind dabei kompiliert, aber nicht ausgeführt. Der anschließende `runGameTestServer --offline`-Versuch aus der Agent-Sandbox scheitert vor den Tasks mit `SocketException: Operation not permitted` beim Gradle-`FileLockContentionHandler`. Der erfolgreiche Build und die vorhandenen Abhängigkeiten heben diese Sandbox-Grenze nicht auf. [Buildnachweis](../reports/current-build-validation.json), [aktueller Server-Startversuch](../reports/current-gametest-attempt.txt).

## Bestehende Grundprüfungen

| Klasse / Fälle | Aussage bei Erfolg |
| --- | --- |
| `DungeonsAliveGameTests.registeredContentIsAvailable` | Questbuch, Windhorn und gemeinsame Partikeltypen sind registriert; Common-Klassen laden auf dem Server. |
| `DungeonsAliveGameTests.serverLoadsDefaultQuestData` | Der geladene Questkatalog enthält die stabilen Einstiegsaufgaben und konsistente Definitionen; optionale Daten werden anhand des tatsächlichen Profils bewertet. |
| `DungeonsAliveGameTests.windHornRespectsTargetsRadiusAndCooldown` | Die echte Benutzung beeinflusst ein passendes nahes Ziel, verschont passive/entfernte Ziele, respektiert Cooldown und erzeugt keinen direkten Schaden. |
| `DungeonMobGameTests` | Alle tatsächlichen Profile besitzen Entities, Attribute und Spawn-Eier. Weitere Fälle prüfen Windup/Einmalwirkung, neue Strahldeckung, NBT-Abbruch, Bossphase, verwaiste Geschosse, passive Ziele und die blockschonende Kälteexplosion. |
| `VillageGameTests` | Berufs-/POI-Registrierung und Handelsangebote, echte Arbeitsplatzzuordnung sowie Arbeitszustand nur bei gespeichertem tatsächlichem Arbeitsvorgang. |
| `EncounterGameTests` | Fehlklick und blockierter Platz werden abgelehnt; erfolgreicher Spawn verbraucht genau ein Siegel und erhält Persistenz, gemeinsame Abklingzeit und Begegnungsabstand. |

Diese Prüfungen fragen den aktuellen registrierten Bestand ab. Ältere feste Zahlen aus der 0.2-Prototypphase sind keine gültigen Erwartungen für 0.4.

## Friedliche Figuren, Befreiung und aquatische Begleiter

Die Fälle stehen in `entity/peaceful/PeacefulGameTests.java`. Besitz-/Handelsprüfungen und die neuen Navigationsregressionen verwenden getrennte Batches.

| Test | Konkrete Aussage bei Erfolg |
| --- | --- |
| `peacefulMobsDoNotRetaliateAndAquaticPetDoesNotDrown` | Friedliche Registrierungen bleiben ohne feindliches Spielerziel; das geprüfte aquatische Haustier erhält keinen Ertrinkungsschaden. Das allein belegt noch keine Wasserwegfindung. |
| `merchantTradePaymentAndUseCountersSurviveNbt` | Ein tatsächliches Angebot nimmt die passenden Kosten, erhöht seinen Benutzungszähler und bleibt einschließlich Handelszustand beim NBT-Laden erhalten. |
| `companionOwnerAndWaitingSurviveReloadAndRejectOtherPlayers` | Besitzer und Warten/Folgen bleiben gespeichert; ein anderer Spieler übernimmt den Begleiter nicht. Dieser Fall ersetzt keinen tatsächlichen Bewegungsnachweis. |
| `aquaticCompanionNavigatesUpwardWithoutForcedMotion` | Ein echter Baby-Squid erzeugt bei aktiver KI einen freien aufwärts führenden Wasserpfad und gewinnt danach messbar Höhe. Kein erzwungenes `move` oder Teleportieren ersetzt die Navigation; kein Kampfziel entsteht. |
| `rescuedBlacksmithFollowsWithOddEntityId` | Ein tatsächlicher Zivilist mit **ungerader Entity-ID** wird über `mobInteract` befreit, findet seinen im Serverlevel registrierten Retter und nähert sich ihm messbar durch seinen Follow-Goal. Befreiung und friedlicher Zustand bleiben erhalten. |

Die letzten beiden Fälle sichern konkret gefundene Fehler ab. Der normale Blickcontroller kann die Neigung einer Wasserbewegungssteuerung zurücksetzen und damit Höhenbewegung verhindern. Außerdem darf ein Follow-Goal mit eigenem Zehntick-Takt nicht durch Minecrafts nach `(tickCount + entityId)` versetzte Goal-Ausführung dauerhaft ausgehungert werden. Ein Test, der nur `rescued=true` oder eine Animation prüft, würde den beobachteten Bewegungsstillstand nicht erkennen.

Für das Besitzer-Folgen muss der Retter wirklich über seine UUID im Serverlevel auflösbar sein. Die Regression benutzt dafür einen registrierten `FakePlayer`; sie behauptet keine echte Netzwerkverbindung. Gewöhnliche `makeMockPlayer`-Fixtures reichen für unmittelbare Item-/Interaktionsaufrufe, aber nicht automatisch für die spätere Besitzerauflösung durch eine KI.

## Konstrukte, Dreizack und Unterwassergegner

`entity/construct/ConstructGameTests.java`, Batch `constructs`, prüft tatsächliche registrierte Objekte. Die Mechanik steht in [CONSTRUCTS_AND_SWIMMING.md](CONSTRUCTS_AND_SWIMMING.md).

| Test | Konkrete Aussage bei Erfolg |
| --- | --- |
| `wallPlacementCapCoverAndOwnerLifecycle` | Gültige/ungültige Platzierung, begrenzte Besitzerreservierungen einschließlich NBT, aktive Pfeilerdeckung und Entfernen nach Besitzerverlust. |
| `geomancerBombTelegraphsOnceAndHonorsCoverAndPassives` | Vor der Zündung kein Bombenschaden; danach genau die erlaubte Wirkung unter Beachtung tatsächlicher Deckung und passiver Tiere. |
| `vineTargetsOnlyItsOpponentAndReleasesWhenOwnerDisappears` | Die Ranke wirkt nur auf ihr tatsächliches Ziel und gibt es bei Besitzerverlust wieder frei. |
| `iceFormsFallsHitsOnceAndExpiresWithoutTerrainDamage` | Geprüfte freie Luftsäule, wirklicher Fall, einmaliger Landungstreffer, Ablauf und unverändertes Terrain. |
| `realTridentSkipsAnimalsStopsAtWallAndProducesNoPickup` | Tatsächlicher Dreizackflug, passive Tiere, Wandkollision und kein Pickup; zusätzlich echter Item-Benutzungszustand beim Windup, sauberer NBT-Abbruch und exakt ein Projektil am Freigabepunkt. |
| `underwaterNavigationChangesDepthAndRestoresLandState` | Ein echter Wasserpfad gewinnt zuerst Höhe und bewegt sich danach horizontal; Schwimmflag, Steuerung und Navigation kehren beim Landübergang zurück. |

Für Eisfall und tiefe Becken ist `skyAccess=true` erforderlich, wenn die Szene über die flache Strukturvorlage hinausragt. Sonst kann die GameTest-Barriere selbst die beabsichtigte freie Luft-/Wassersäule blockieren. Das ist eine Fixture-Frage, kein Grund, die Platzprüfung des Spiels zu umgehen.

Die Dreizackprüfung muss den tatsächlichen Benutzungszustand abdecken: Vanillas `trident_throwing`-Itemmodell wird nicht allein durch einen angehobenen Arm gewählt. Der echte Mob muss den gehaltenen Dreizack benutzen und diesen Zustand nach Freigabe/Abbruch wieder beenden. Der grafische Nachweis bleibt trotz erfolgreichem Servertest erforderlich.

## Artefakte, Seelen und Waffen

`item/gear/GearGameTests.java` verwendet separate Batches für Cooldowns, Ziele, Strahl, Zauberwanddeckung und Waffen. Vollständige aktuelle Werte stehen in [GEAR_AND_SOULS.md](GEAR_AND_SOULS.md).

| Test | Konkrete Aussage bei Erfolg |
| --- | --- |
| `artifactCooldownAndServerSoulAccount` | Artefaktwirkung entsteht auf dem Server; direkte Wiederholung scheitert. Manipulierte Item-Anzeigedaten liefern keine Seelen, das Spielerkonto ist begrenzt und eine akzeptierte Harvester-Benutzung zahlt genau einmal. |
| `gongRespectsCoverAndPassiveCreatures` | Der Gong erreicht ein offenes feindliches Ziel, respektiert solide Deckung und passive Tiere; die zusätzliche Verwundbarkeit hat ein begrenztes serverseitiges Ende. |
| `beamStopsAtBlocksAndChargesSouls` | Beacon-Puls trifft ein offenes passendes Ziel, schützt Tier und Ziel hinter Deckung, zerstört keine Blöcke und bezahlt einen einzelnen Puls. Seelenerschöpfung stoppt die Wirkung und setzt Cooldown. |
| `spellWallsAndHotbarStopCannotBypassBeamRules` | Ein tatsächlich aktiver Geomancer-Pfeiler deckt gegen den Artefaktstrahl. Benutzungsende wie bei einem Hotbarwechsel aktiviert die Sperre; ein abgewiesener sofortiger Neustart verbraucht keine weiteren Seelen. |
| `signatureWeaponsAndSoulEarningStayBounded` | Begrenzte Waffenheilung respektiert ihre Pause; ein zugerechneter feindlicher Kill liefert Seelen, ein passiver Kill nicht. |

Diese gezielten Fälle sind keine vollständige Survival-Balance- oder PvP-Abnahme sämtlicher Items. Echte Eingaben, zweiter Client, Item-/Handdarstellung, Seelenverlust beim Tod, Rezeptbeschaffung und längere Spielabläufe gehören weiterhin in den manuellen Plan.

## Neue Beschwörungsregressionen im weitergeführten Quellstand

`entity/SummonedRewardGameTests.java`, Batch `summoned_rewards`, ergänzt zwei Fälle. **Noch nicht im Server ausgeführt:** Sie erhöhen nicht die bestandenen Testzahlen des bisherigen Auslieferungsberichts.

| Test | Konkrete Aussage bei Erfolg |
| --- | --- |
| `actualSummonsDoNotYieldSoulsAfterReloadButHeartstealerStillHeals` | Eine echte Necromancer-Beschwörung erzeugt Helfer. Direkter und über NBT wiederhergestellter Helfer geben keine Seelen; ein normaler Vanguard desselben Typs gibt drei. Heartstealer-Heilung bleibt beim direkten Helfer-Kill erhalten. |
| `reloadedSummonCannotCompleteBountyButOrdinaryBossCan` | Ein abgebrochenes Todesereignis und eine ausdrücklich künstlich mit Beschwörermarker versehene, wieder geladene Bossfixture zählen nicht für die angenommene Nameless-Aufgabe. Ein normaler Boss derselben Registry-ID zählt genau einmal; die Belohnung wird nicht automatisch abgeholt. |

Die Bossfixture ist eine Prüfung des gespeicherten Identitätsmerkmals, keine neu implementierte Bossbeschwörung. Ein FakePlayer liefert die benötigte ServerPlayer-Schadensquelle; sein Questzustand und Sitzungscache werden anschließend aufgeräumt. Die Quelländerung betrifft Todesereignis-Adapter, nicht die Minecraft-unabhängige Questzustandsmaschine.

## Neue Lagerhändlerroutinen im weitergeführten Quellstand

`entity/peaceful/MerchantRoutineGameTests.java`, Batch `merchant_routine`, ergänzt vier Fälle. **Noch nicht im Minecraft-Server ausgeführt.** Die sieben separat bestandenen JUnit-Ablauftests für `MerchantRoutine` prüfen die Entscheidungsregeln, nicht diese Entity-/Navigationsintegration.

| Test | Konkrete Aussage bei Erfolg |
| --- | --- |
| `campHomeStartsAfterPlacementAndSurvivesReload` | Alte markierte Entity-Daten ohne Heimat übernehmen erst beim echten KI-Tick ihre endgültige Weltposition. Volles Entity-NBT erhält Tag/Heimat; `NoAI` verzögert die Initialisierung, und ein unmarkierter Händler erhält keine Routine. Ein ungeladenes Fernziel wird abgelehnt und bleibt ungeladen. |
| `displacedCampMerchantWalksHomeWithoutTeleport` | Ein zunächst ausdrücklich versetzter Händler läuft anschließend über seine tatsächliche KI wieder nach Hause. Pfad, messbare Bewegung, begrenzter Weg pro Tick und unveränderte Heimat werden geprüft. |
| `realTradeMenuClosesOnDamageButForeignMenuStaysOpen` | Die Händlerinteraktion stoppt einen tatsächlich gestarteten Pfad und erstellt ein echtes serverseitiges `MerchantMenu`. Ein Kauf verbraucht Zahlung und Angebotsbenutzung; Schaden schließt das eigene Menü, lässt aber eine absichtlich hergestellte veraltete Referenz auf ein inzwischen fremdes Menü unangetastet. |
| `damagedCampMerchantFleesPeacefullyThenReturnsHome` | Akzeptierter Spielerschaden löst messbare Bewegung vom Angreifer weg aus, ohne Gegenschaden oder Angriffsziel. Das Fluchtfenster endet, und der Händler kehrt zu seiner gespeicherten Heimat zurück. |

Die Fixtures laden ihren örtlichen Navigationsbereich **ausdrücklich im Test** und entfernen ausschließlich die von ihnen hinzugefügten erzwungenen Chunks am Batchende. Der negative Fernzielfall prüft eine abgewiesene Zielanfrage und den anschließend weiterhin fehlenden Chunk; er prüft keinen fehlenden Rand des Suchbereichs und kein Entladen während des Laufens. Tageszeit und Wetter bleiben unverändert; deren Entscheidungsregeln stehen in den reinen JUnit-Fällen. Der Menü-FakePlayer erstellt den tatsächlichen Servercontainer, hat aber keine echte Clientverbindung. Sichtbares Fenster, zwei tatsächliche Spieler und Wegfindung an Entladegrenzen bleiben manuelle Prüfungen.

## Neue natürliche Tiergruppen und Angriffsabbrüche

Die folgenden **13 neuen Serverfälle sind noch nicht ausgeführt**. Sie ergänzen die bisherigen Beschwörungs- und Händlertests, ohne historische Testergebnisse zu erhöhen.

| Klasse / Anzahl | Konkrete Aussage bei Erfolg |
| --- | --- |
| `WildlifeSpawnGameTests` / 4 | Geladene Biome enthalten die echten Packeinträge; registrierte Spawnprädikate erlauben passende Tag-/Nacht-/Bodenfälle und beachten globale/artspezifische Schalter sowie lokale Dichte. Natürliche SpawnGroupData teilt Gruppen bis sechs und trennt das siebte Tier; NBT erhält Identität und Mobcap-Eignung, alte/manuelle Tiere und Haustiere bleiben persistent. |
| `WildlifeHerdGameTests` / 2 | Ein getrenntes Tier läuft tatsächlich zurück; nach echtem Leittiertod übernimmt ein geladenes Mitglied. Sieben Tiere bilden begrenzte Gruppen, Besitz/Pets/Zivilisten bleiben ausgeschlossen, Alarm bleibt lokal und läuft ab, vollständiges Entity-NBT erhält die Gruppe. |
| `WildlifeFlockGameTests` / 3 | Echte Geier steigen während eines gemeinsamen Schadensalarms; ein neu geladenes versetztes Irrlicht behält Gruppe/Platz und fliegt zurück. Ein aktiver Flugpfad stoppt vor einem neu eingesetzten Hindernis und lehnt ein ungeladenes Fernziel ohne Nachladen ab. |
| `AttackLifecycleGameTests` / 4 | Zielwechsel beendet Nahkampfvorbereitung; Zieltod verhindert eine Beschwörung, während die unveränderte Kontrolle tatsächlich beschwört. Ein entferntes Ziel beendet Trident-Windup, ein bereits ausgelöstes Kontrollprojektil behält seinen eigenen Flug. NBT-Reuse und Angreifertod räumen Aktionen ohne Wiederholung oder verkürzten Cooldown auf. |

Die Tier-Bewegungsfixtures laden ihren benötigten Bereich ausdrücklich. Am Batchende werden alle eigenen Tiere unabhängig von ihrem Aufenthaltsort entfernt; zusätzliche Flugfläche/Wandblöcke außerhalb der nur fünf Blöcke hohen Grundvorlage werden wiederhergestellt, bevor eigene Chunkfreigaben erfolgen. Der positive Spawnregeltest ändert Tageszeit und Konfiguration nur innerhalb eines synchronen `try/finally` und stellt sie wieder her. Er prüft die tatsächlichen Prädikate, keinen langfristigen natürlichen Populationslauf. Vorgegebene Bewegungsszenen bleiben von natürlichem Auftreten in verschiedenen Weltsamen zu unterscheiden.

Die 17 zusätzlichen Offline-JUnit-Fälle prüfen Gruppen- und Flugregeln ohne Minecraft. Ihr bestandener Java-21-Lauf belegt weder diese 13 Serverfälle noch tatsächliche Flugdarstellung.

## Optionale Lagerprüfung

`entity/peaceful/CampGameTests.java` liefert seine Fälle nur, wenn `dungeonsalive.test.camps=true` ausdrücklich gesetzt ist. Ohne das optionale Lagerpack werden keine dafür nicht vorhandenen Ressourcen still vorausgesetzt.

| Generierter Test | Aussage bei Erfolg |
| --- | --- |
| `camps.merchant_template` | Struktur, Pool und Platzierungsregistrierung laden; NBT-Palette/Blockstates stimmen. Die tatsächliche Vorlage erzeugt genau einen persistenten vorgesehenen Händler mit echten Angeboten. Neu im weitergeführten Quellstand: keine vorgebackene Heimat im Template; derselbe NPC übernimmt erst nach Platzierung seine tatsächlichen Weltkoordinaten. Dieser Zusatz ist noch nicht ausgeführt. |
| `camps.captive_rescue` | Die Vorlage erzeugt genau einen anfangs unbefreiten wartenden Zivilisten. Tatsächliche Befreiung, Besitzer, Wartezustand, NBT-Rückkehr und Abweisung eines fremden Spielers funktionieren. |

Der Lager-Befreiungstest prüft den Interaktions-/Speichervertrag. Erst die separate ungerade-ID-Folgeregression beziehungsweise eine echte Weltaufnahme beweist anschließend tatsächliches Gehen. Erfolgreiche Template-Platzierung ist außerdem noch kein Nachweis natürlich entstandener Lager in neu generierten Chunks.

## Start und getrennte Instanzen

Mit Java 21 im Projektverzeichnis:

```bash
./gradlew build
./gradlew runGameTestServer
```

Die NeoForge-Konfiguration verwendet `type = 'gameTestServer'` und `neoforge.enabledGameTestNamespaces=dungeonsalive`; ihr Arbeitsverzeichnis ist `run-gametest`. `@GameTestHolder("dungeonsalive")` und `@PrefixGameTestTemplate(false)` registrieren die Tests ohne zusätzliche Mod-Bus-Anmeldung.

Für das ausdrücklich optionale Lagerprofil:

```bash
python3 tools/prepare_camp_gametest.py
./gradlew runCampGameTestServer
```

Der Vorbereiter arbeitet ausschließlich im markierten `run-gametest-camps`, kopiert das Lager-Datapack hinein und schreibt die Entwicklungsservereinstellungen einschließlich `eula=true`. Ein bereits befüllter unmarkierter Zielordner wird abgelehnt. Dieses Profil läuft getrennt vom Grundprofil; dieselben erneut ausgeführten Kernfälle zählen nicht als zusätzliche unabhängige Tests.

`./gradlew runIntegrationGameTest` bleibt das getrennte Profil für Enchant With Mob/Bagus Lib und die vorgesehenen Integrations-Datapacks. Konfiguration und tatsächlich erzielte Ergebnisse stehen im Validierungsbericht; eine bloße vorhandene Run-Konfiguration ist kein ausgeführter Nachweis.

## Testvorlage und API-Grundlage

`src/main/resources/data/dungeonsalive/structure/gametest_empty.nbt` ist eine eigene leere Struktur von 20 × 5 × 12 mit `DataVersion=3955`. Die Fälle bauen ihre nötigen Böden, Becken und Hindernisse selbst. Keine fremde Spielwelt und keine Dungeons-Assets bilden die Testvorlage.

Die verwendeten `GameTestHolder`, `PrefixGameTestTemplate`, `GameTestHooks`, `GameTest`, `GameTestHelper` sowie die KI-/Itemzustandsverträge wurden an den installierten Minecraft-1.21.1-/NeoForge-21.1.265-Quellen geprüft. Diese Version besitzt kein `EmptyTemplate`-Attribut an `@GameTest`; deshalb bleibt die kleine eigene NBT-Vorlage erforderlich.

## Ergänzende echte Clientprüfung

Nach einer vorhandenen GameTest-Welt lässt sich die isolierte Modellprüfung vorbereiten und auf die zwei konkreten Regressionen begrenzen:

```bash
python3 tools/prepare_mob_model_showcase.py
./gradlew runMobModelShowcase \
  -PshowcaseMobs=captured_blacksmith,armored_trident_drowned \
  -PshowcaseClips=captured_blacksmith,armored_trident_drowned
```

Das benötigt einen grafischen Client. Der Aufnahmehelfer ist in Produktion deaktiviert, verlangt eine ausdrückliche JVM-Property, den separaten Run-Ordner und übereinstimmende Weltmarker. Serverentitäten, Spieleransicht und native Screenshot-API erzeugen die PNGs; der Test setzt keine lokale Vorschau als echte Weltentity ein.

Die folgenden Gates dürfen nicht für ein hübsches Bild entfernt werden:

- Aquatischer Gegner: tatsächliche Wasserwegfindung bei aktiver KI; Server und Client melden beim Hauptbewegungsbild Wasser, Schwimmen und `SWIMMING`.
- Zivilist: dieselbe UUID bleibt über Gefangenschaft, echten Rechtsklick und nachfolgendes tatsächliches Folgen erhalten. Kein erwartetes Handelsfenster und kein erzwungenes Verschieben als Ersatz.
- Dreizack: tatsächlicher Serverangriff plus `server_using_item`/`client_using_item`, konkrete Use-Item-ID und Benutzungsticks; nach Aktion darf der Use-Zustand nicht hängen bleiben.
- Kamera: tatsächliche Spielerposition/Rotation wird dokumentiert; Kameranachführung verändert nicht nachträglich die PNGs.
- Laufende oder abgebrochene Serie: `complete=false` bleibt sichtbar. Einzelne gültige Aufnahmen belegen ihre konkrete Szene, aber keine abgeschlossene Gesamtgalerie.

`runRenderSmoke` bleibt ein älterer Menü-/Renderprüfweg. Historische Prototypzahlen oder damalige Screenshotmengen beschreiben nicht den heutigen vollständigen Figurenbestand. Für aktuelle Modelltreue, Hände, Leuchten und Wasserpose zählt die gezielte native Sichtung samt dokumentierten Einschränkungen.


## Neuer Ausrüstungsquellstand – noch nicht ausgeführt

`ArtifactExpansionGameTests` ergänzt drei Serverfälle: echte Seelenzahlung ohne Tooltip-Vertrauen/Leerlaufverbrauch, kurzer Geisterumhang mit unveränderter Blockkollision und sieben serverseitige Feuerpfeil-Ladungen einschließlich realer Vanilla-Pfeile, NBT-Reload und Spielerklonen.

`RangedWeaponGameTests` ergänzt vier Fälle: tatsächlicher Kraftbogen-Abschuss mit Munition/Haltbarkeit, begrenzte Befehle an eigene aktive Wölfe samt einmaliger Pfeilrückgabe, Auto-Armbrust-Ladegrenze mit einmaliger Zahlung sowie echte Explosionspfeile gegen sichtbare Gegner unter Erhaltung von Tieren und Deckung.

`ArmorGameTests` ergänzt drei Fälle: zwölf tatsächlich registrierte haltbare Rüstungsteile, vollständige/gemischte Sets und echter Nahkampfschaden; Ausrüstungsereignisse, angewandter Schaden, Lebensraubpause und Entkleiden; reale Artefaktnutzung samt Beacon-Stop und verkürzten Cooldowns. Diese zehn neuen Tests sind kompiliert, aber keine ausgeführten Server-Prüfnachweise. Die zwei reinen `ArmorSetKindTest`-Fälle bestehen auch im aktuellen vollständigen JUnit-Lauf mit 97 Tests. Der Minecraft-Serverlauf bleibt wegen der oben dokumentierten Sandbox-Socketgrenze offen.
