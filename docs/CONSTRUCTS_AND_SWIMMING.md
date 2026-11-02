# Temporäre Gegnerkonstrukte und Unterwassersteuerung – 0.4

Diese Erweiterung macht vier bereits importierte Modellvorlagen zu tatsächlichen, servergesteuerten Zauberobjekten. Geometrie, Texturen und vorhandene Erscheinungs-/Verschwindeclips kommen aus den im [Modellmanifest](../design/reference-models.json) und in den [Lizenzhinweisen](../THIRD_PARTY_NOTICES.md) erfassten Community-Quellen. Platzierung, Lebensdauer, Schaden, Zerstörbarkeit und Minecraft-Navigation sind eigene Implementierungen. Das ist kein vollständiger Port der ursprünglichen Dungeons-Kampflogik.

## Registrierungs- und Aufrufweg

Die vier Konstrukte und der feindliche Dreizack gehören zu `entity.construct.ModConstructs`. Sie werden als `MobCategory.MISC` registriert, nicht als weitere gewöhnliche Gegnerprofile. Sie haben keine Spawn-Eier, natürliche Spawngewichte, Beute oder Erfahrungspunkte.

| Entity-ID | Auslösender Gegner / Aktion | Ablauf nach Erzeugung | Wirkung |
| --- | --- | --- | --- |
| `dungeonsalive:geomancer_wall` | Geomancer / `ROOT_SNARE` | 25 Ticks Aufstieg, 120 aktiv, 40 Abstieg | Drei einzelne, zerstörbare Pfeiler neben und hinter dem festgelegten Zielpunkt. Die Seite zum Zaubernden bleibt offen. |
| `dungeonsalive:geomancer_bomb` | Geomancer / `FIRE_FIELD` | 25 Ticks Aufstieg, 35 Zündung, 40 Abstieg | Ein hoher Bombenpfeiler neben dem Ziel; ein einmaliger geschützter Flächentreffer mit 3,2 Blöcken Radius. |
| `dungeonsalive:ice_chunk` | Iceologer / `ICE_FALL` | 40 Ticks Formation, tatsächlicher Fall, 23 Ticks Landung | Entsteht vier Blöcke über der geprüften Bodenposition. Kollision löst höchstens einen Treffer mit 2,4 Blöcken Radius und Verlangsamung aus. |
| `dungeonsalive:entangle_vine` | Whisperer, Wavewhisperer, Treetop Whisperer / `ROOT_SNARE` | 8 Ticks Wachstum, 60 aktiv, 25 Verfall | Ein Anfangstreffer; danach kurze Verlangsamung nur für das festgelegte Opfer innerhalb der Ranke. |
| `dungeonsalive:dungeon_trident` | Armored Trident Drowned / `SOUL_BOLT` | Nativer Dreizackflug, spätestens nach 120 Ticks entfernt | Echter `ThrownTrident`, der passende Gegner trifft und an Gelände oder aktiven Pfeilern aufgehalten wird. Kein aufsammelbarer Gegenstand. |

Ein Tick ist bei unverzögertem Spielbetrieb eine Zwanzigstelsekunde. Die zusätzliche Vorbereitung des Zaubernden läuft davor auf der bestehenden `AttackKind`-Zeitleiste: 32 Ticks für `ROOT_SNARE`, 36 für `FIRE_FIELD` und `ICE_FALL`, 24 für den Dreizackwurf. Beim Dreizack ist der Abwurf an den eigenen Wurfclip mit seinem Kontaktmarker gebunden.

`DungeonMob.beginAttack(...)` bleibt der normale Spieleinstieg. Beim Freigabepunkt ruft `DungeonMob.release(...)` den passenden Konstruktdienst auf. Eine fehlgeschlagene Platzierung erzeugt keinen ersatzweisen, unsichtbaren Flächentreffer.

Für Entwicklungsszenen stehen dieselben geprüften Dienste öffentlich bereit:

```java
ConstructService.spawn(caster, target, ConstructKind.ICE_CHUNK,
        target.position(), damage);
ConstructService.castWalls(caster, target, lockedGround, damage);
ConstructService.castBomb(caster, target, lockedGround, damage);
DungeonTrident.launch(caster, lockedAim, damage);
```

`spawn` und `launch` können `null` zurückgeben. Das ist eine normale Ablehnung durch Platz, Deckung, Lebenszustand, geladene Welt oder Kapazität. Die Funktionen umgehen keine Spielregeln. Ein nacktes `/summon` ohne gültigen Besitzer, Ziel und Lebenszeit erzeugt deshalb kein dauerhaftes oder schädigendes Konstrukt.

## Platz und Deckung

Die Platzierung verändert keine Blöcke. Sie prüft geladene Chunks, Weltgrenze, Bauhöhe, tatsächliche Blockkollision und vier tragende Punkte unter der Grundfläche. Es gibt keine Terrainbereinigung, um einen Pfeiler oder Eisblock passend zu machen. Wo eine Wand, Decke, Flüssigkeit oder fehlender Boden die vorgesehene Form verhindert, entfällt dieses Objekt.

Pfeiler dürfen beim Erzeugen keine lebende Entity überlappen. Vor dem Übergang zur festen Wand wird erneut geprüft: Ist während der Aufstiegsphase jemand hineingelaufen, verschwindet der Pfeiler wieder. Ein aktiver Wandpfeiler ist echte Entity-Kollision. Die gemeinsame Sichtlinienprüfung und die Projektilpfade behandeln ihn als Deckung. Dadurch werden auch Nahkampffreigabe, Strahlen und relevante Flächentreffer an der Wand begrenzt.

Der Eisfall prüft eine gesamte freie Luftsäule. Seine Entity bewegt sich anschließend tatsächlich nach unten und trifft auf die aktuelle Blockkollision. Ein später platzierter Boden oder eine Plattform kann den Fall deshalb abfangen. Der Treffer prüft erneut Deckung. Der Eisblock ersetzt keinen Schnee-, Eis- oder Luftblock.

Eine Ranke bleibt am ursprünglichen Punkt. Sie zieht und teleportiert ihr Opfer nicht. Der Wirkungsabstand beträgt 1,65 Blöcke, der Höhenabstand weniger als 1,5 Blöcke. Eine Verlangsamung wird jeweils nur für zwölf Ticks erneuert; Verlassen, Zerstören oder Entfernen der Ranke löst den Effekt entsprechend schnell. Wavewhisperer-Ranken dürfen unter Wasser stehen; die übrigen hier platzierten Konstrukte verlangen einen flüssigkeitsfreien Platz.

## Opfer, Besitz und Lebensdauer

Neue Konstrukttreffer und Dreizacktreffer verwenden `ConstructService.mayHarm`. Zulässig sind angreifbare Spieler, Eisengolems oder ein tatsächlich feindlich behandelter `Enemy`. Kreativ-/Zuschauerspieler, Verbündete, andere `DungeonMob`-Entities, Konstrukte und passive Tiere, Dorfbewohner oder Haustiere sind ausgeschlossen. Ein manuell gesetztes passives Ziel macht dieses nicht zu einem zulässigen Opfer.

Ein Konstrukt speichert Besitzer- und Ziel-UUID. Beide müssen in derselben bereits geladenen Serverwelt auflösbar und gültig bleiben. Der Besitzer muss leben; der Zielzustand wird vor der Wirkung erneut geprüft. Konstrukte verschwinden, wenn ihr Besitzer oder Ziel nicht mehr auflösbar ist. Es werden dafür keine Chunks oder Dimensionen geladen.

Der Besitzer führt höchstens acht absolute Reservierungen für Konstrukte und feindliche Dreizacke zusammen. Diese UUID-/Ablaufzeit-Liste wird mit dem Besitzer gespeichert. Das Entladen eines Kindes gibt den Platz nicht vorzeitig frei. Eine echte Entfernung gibt die Reservierung frei; abgelaufene Reservierungen werden bereinigt. Zusätzlich erlaubt die Konstrukterzeugung höchstens 32 geladene Konstrukte innerhalb von 32 Blöcken um den Platzierungspunkt. Diese zweite Grenze ist ausdrücklich lokal und umfasst nicht die Dreizacke.

Neue Platzierungen und Wurfziele sind auf 24 Blöcke zum Besitzer begrenzt; ein laufendes Objekt wird spätestens bei mehr als 48 Blöcken Abstand entfernt. Jede Lebenszeit ist ein absoluter Weltzeitpunkt. Ein in einem entladenen Chunk liegendes Objekt erhält beim erneuten Laden keine neue volle Lebensdauer. Eine bereits versäumte Bombenzündung wird nicht nachgeholt; ein geladenes Objekt aus einer früheren Eis-Fallphase verursacht ebenfalls keinen nachträglichen Soforttreffer.

Die maximale Lebenszeit ohne frühere Entfernung beträgt 185 Ticks für Wände, 100 für Bomben, 93 für Ranken und 123 für Eis. Ein Eisblock, der früher landet, endet entsprechend früher. Schaden und Nebenwirkungen werden serverseitig genau einmal ausgelöst beziehungsweise bei der Ranke nur kurzzeitig aufgefrischt. Verhinderter Schaden löst die daran gebundene Rückstoß-/Verlangsamungswirkung nicht aus.

Spieler und Eisengolems können Konstrukte beschädigen. Normale gegnerische Treffer beschädigen ihre eigenen Zauberobjekte nicht. Ein zerstörtes Objekt spielt seine synchronisierte Verschwindephase, ohne parallel die generische Mob-Todanimation einzuschalten. Es erzeugt weder Beute noch Erfahrung. Der Dreizack bleibt auch nach NBT-Laden nicht aufhebbar und erzeugt keinen Waffengegenstand beim Treffer oder Ablauf.

## Client und Animationen

`ConstructRenderers.registerRenderers(...)` wird nach der Installation der `ReferenceModels` aufgerufen. Die vier Konstrukte verwenden die schon vorhandenen Reference-Rigs und Clips; `ThrownTridentRenderer` stellt den wirklichen Dreizack dar. Es werden keine Ersatzkugeln als Dreizack ausgegeben.

Der Eisblock verwendet `RenderType.entityTranslucent`, entsprechend dem gelesenen `IceCloudRenderer` der Quelle. Der originale Atlas enthält halbtransparente magentafarbene Formationshüllen und Ringe neben dem cyan-blauen Eis. Mit einem Cutout-Pass wurden diese Hüllen im ersten tatsächlichen Clientbild fälschlich deckend; die gezielte Materialkorrektur erhält Geometrie, Ringposition und Originalpixel. Der anfängliche Ring liegt am erhöhten Entstehungspunkt, der Landungsring nach dem Fall am Boden. Er wird nicht pauschal als Bodenwarnmarker umgedeutet.

Der Dreizackwerfer beginnt während der tatsächlichen `SOUL_BOLT`-Vorbereitung mit `startUsingItem(MAIN_HAND)`. Das synchronisiert den nativen Minecraft-Prädikatwert für die korrekte Wurfhaltung des gehaltenen Dreizacks. Freigabe, Tod, Entfernung und Laden einer abgebrochenen Aktion beenden den Zustand mit `stopUsingItem()`. `releaseUsingItem()` wird bewusst nicht aufgerufen: Das echte Projektil wird einmalig von der Mob-Aktion erzeugt, ohne einen zweiten Item-Wurf auszulösen.

Der Client liest ausschließlich `DungeonConstruct.phase()`, `phaseTick(...)`, `phaseDurationTicks()` und `visualClip()`. Die Phase und ihr Startzeitpunkt sind synchronisierte Entity-Daten. Die vorhandene `ReferenceAnimationSource.Gesture` spielt den passenden Clip über genau dieses Fenster. Aktive ruhende Objekte benutzen den jeweiligen Source-Idleclip. Der Server erhält durch das Rendern keine Treffer- oder Bewegungsbefehle.

Die aufrufbare Vorschau-API bleibt clientlokalen, nicht in der Welt registrierten Preview-Entities vorbehalten. Für Weltaufnahmen müssen echte Konstrukte über die oben genannten Dienste oder die tatsächliche Gegneraktion erzeugt werden.

## Unterwassergegner

Mobile Profile mit `MobProfile.aquatic()` besitzen eine `AmphibiousPathNavigation` für Land und Ufer sowie eine `WaterBoundPathNavigation` für dreidimensionale Wasserwege. Das betrifft unter anderem Drowned- und Sunken-Skeleton-Varianten, Wavewhisperer, Guardians und den Tropical Slime. Stationäre Profile erhalten keine Wandersteuerung.

Im Wasser übernimmt `SmoothSwimmingMoveControl`. Seine Neigungssteuerung wird nicht mehr durch den normalen Blickcontroller jedes Tick zurückgesetzt; auf Land bleibt dessen üblicher Reset erhalten. Ein Wasser-Wanderziel wird nur in einem bereits geladenen, passenden Wasserbereich gesucht und überschreibt keine laufende Navigation. Der automatische FloatGoal ist für diese aquatischen Profile ausgeschaltet, damit sie nicht zwangsläufig an die Oberfläche gedrückt werden.

`isSwimming()` entsteht auf dem Server aus tatsächlichem Wasserzustand, aktiver KI und Wasserziel, laufender Navigation oder echter Bewegung. Es ist kein pauschales `isInWater`-Flag. Bei einem Ziel an Land wird der amphibische Navigationsweg verwendet. Beim Verlassen des Wassers kehren Navigation, Bewegungssteuerung und Pose zum Landzustand zurück. Die CEM-/Renderseite liest diesen echten Schwimmzustand; sie erzeugt ihn nicht.

Für Aufnahmehelfer ist deshalb `NoAI=false` mit einem tatsächlichen Wasserwegpunkt geeignet. Nach dem ersten Eintauchtick kann ein Wegpunkt gesetzt werden: Der anfängliche Wechsel von Land- zu Wassernavigation beendet den alten Pfad. Eine NoAI-Entity erhält allein durch eine manuell gesetzte Pose keinen bestätigten Schwimmzustand.

## Aquatische Begleiter und gerettete Zivilisten

Der aquatische `DungeonsCreature`-Zweig verwendet ebenfalls Wassernavigation und `SmoothSwimmingMoveControl`. Während einer aktiven Wasserroute verhindert sein neutraler Blickcontroller den gleichen unerwünschten Neigungsreset. Der Begleiter erhält dadurch tatsächliche vertikale Steuerung; es wird weder seine Position künstlich versetzt noch ein bloßer Schwimmzustand als Bewegungsnachweis gesetzt. Der aktuelle aquatische Begleiter ist `pet_baby_squid`.

`FollowKeeperGoal` wird für Begleiter und den geretteten Schmied jeden KI-Tick aktualisiert und erneuert seinen Pfad weiterhin nur alle zehn Entity-Ticks. Das ist erforderlich, weil der normale abwechselnde Goal-Takt sonst bei einer ungeraden Entity-ID ausschließlich ungerade Ticks liefern kann: Die frühere Prüfung `tickCount % 10 == 0` wurde dann niemals erreicht. Die neue Prüfung befreit einen wirklichen Schmied mit genau dieser ungünstigen Parität und verlangt seine tatsächliche Annäherung an einen über UUID in der Serverwelt auflösbaren Retter.

## Prüfung und Weiterarbeit

Die sechs gezielten Serverprüfungen stehen in `entity.construct.ConstructGameTests`, Batch `constructs`. Sie prüfen unterstützte und abgelehnte Platzierung, Reservierungsgrenze mit Besitzer-NBT, tatsächliche Pfeilerdeckung, Besitzerverlust, Zündfenster und Einmalwirkung, passive Tiere, feste Deckung, Rankenfreigabe, echten Eisfall und Ablauf, echte Dreizackkollision ohne Pickup sowie getrennte vertikale und horizontale Wassernavigation mit Landrückkehr. Der Dreizacktest verlangt zusätzlich den richtigen Item-Use-Zustand während der Vorbereitung, dessen Ende bei Freigabe und NBT-Abbruch sowie genau ein echtes Projektil.

Hinzu kommen `PeacefulGameTests.aquaticCompanionNavigatesUpwardWithoutForcedMotion` und `PeacefulGameTests.rescuedBlacksmithFollowsWithOddEntityId`, Batch `companion_navigation`. Beide prüfen echte Weltbewegung, nicht nur das Setzen eines Ziel- oder Animationsflags.

Alle acht haben im zentralen Korrekturlauf vom 2. November 2026 bestanden. Das tatsächlich gelesene Protokoll `v04-corrected-validation.log` meldet um 11:34:47 alle **30** erforderlichen Serverprüfungen und um 11:35:01 alle **32** einschließlich der Lagerprüfungen als bestanden. Maßgeblich für das ausgelieferte Paket und spätere Änderungen bleibt der aktuelle [Validierungsbericht](VALIDATION.md). Die tatsächlichen Bildsichtungen, Hashes, gefundenen Fehler und noch offenen Nachprüfungen stehen im [Konstrukt-Sichtbericht](../reports/v04-construct-visual-qa.json). Erfolgreiche Serverprüfungen ersetzen die gesonderte Sichtung echter Clientaufnahmen nicht.

Die korrigierte native Featurefolge `20261102-113549` wurde für alle vier Hauptzustände und Schlussbilder sowie Eis-Formation, Fall und Aufprall tatsächlich angesehen. Der deckende Magentafehler ist behoben; die vollständigen Folgen und ihre `removed=true`-Metadaten bestätigen die natürliche Entfernung aller vier Konstrukte. Der Bericht enthält insgesamt 19 ausgewählte, gegen ihre Manifesthashes geprüfte Frames aus Erst- und Korrekturlauf. Die kleine Ranke ist in der gemeinsamen Kamera teilweise vom Golem verdeckt; eine lückenlose Nahansicht aller Animationen und Zielkörper wird nicht behauptet.

Die vier Konstrukte sind bewusst eine begrenzte erste Umsetzung. Vollständige Dungeons-Bossarenen, beliebige Formationstypen, alle Spezialschutzphasen und identische Original-Schadenswerte sind damit nicht implementiert. Neue Konstruktarten sollten denselben Besitz-, Lebenszeit-, Opfer- und Platzierungsvertrag benutzen.

### Ausrüstungsnachtrag: Diamant-Vindicator

Der `diamond_armored_vindicator` trägt jetzt den registrierten `Whirlwind`-Gegenstand mit Dropchance null. Die Gegnerangriffe aktivieren keine Spieler-Waffen-Callbacks. Die +8 Angriffsattribut-Modifikation dieses Items entspricht der zuvor ausgerüsteten Eisen-Axt; die eigene Mob-Aktionszeitleiste bleibt bestehen.

Diese Zuordnung ist eine ausdrückliche Anpassung anhand der beschriebenen dritten Dungeons-Vindicator-Variante mit Hörnerhelm und Whirlwind: [Vindicator](https://minecraft.wiki/w/Dungeons%3AVindicator), [Whirlwind](https://minecraft.wiki/w/Dungeons%3AWhirlwind). Sie wird nicht als unverändert übernommene Ausrüstungslogik des Modell-Repositories ausgegeben: Die tatsächlich gelesene `DungeonsMobs-Port`-Datei `src/main/java/net/firefoxsalesman/dungeonsmobs/entity/illagers/ArmoredVindicatorEntity.java` rüstet pauschal `dungeonsgear:double_axe` aus und enthält keine Diamond-/Whirlwind-Abzweigung. Die ältere `Dungeons-Mobs`-Datei `src/main/java/com/infamous/dungeons_mobs/compat/DungeonsGearCompat.java` besitzt ebenfalls keine solche Bindung.
