# Manueller Spieltest vor der nächsten Veröffentlichung

**Ziel:** Dungeons Alive **0.4.0-alpha**, Minecraft Java **1.21.1**, NeoForge **21.1.265**, Java **21**.

Diese Checkliste beschreibt die vollständige manuelle Abnahme. Nur tatsächlich durchgeführte Schritte abhaken. Die verbindlichen bereits ausgeführten Build-, Server- und Darstellungsprüfungen stehen in [VALIDATION.md](VALIDATION.md). Ein erfolgreicher Modell-Screenshot ist kein vollständiger Kampf-, Animations- oder Mehrspielertest.

Eine separate Testwelt verwenden. Zunächst nur NeoForge und Dungeons Alive laden; optionale Mods und Spawn-Datapacks später getrennt ergänzen. Inventar, Verbrauch, Beute und Cooldowns im Survivalmodus prüfen.

## Zusatz im aktuellen Quellstand: Lagerhändleralltag

Diese neuen Schritte sind **noch offen**. Voraussetzungen und Grenzen: [LIVING_WORLD.md](LIVING_WORLD.md). Die alten Lagerfotos verwenden `NoAI` und erfüllen diese Abnahme nicht.

- [ ] Unveränderte Händlerlager-Vorlage mit aktiver KI platzieren: Heimat entspricht dem wirklichen Weltstandort; der Händler macht kleine Tageswege und kehrt bei Nacht/Regen zurück.
- [ ] Bei offenem Handel hält er an. Ein Treffer beendet seinen Handelsdialog und löst nur eine kurze Flucht aus; danach erfolgt die Heimkehr ohne Angriff auf den Spieler.
- [ ] Ein zweiter echter Spieler sieht dieselbe Bewegung; beide können den Händler nach Beruhigung wieder normal benutzen. Kein fremdes Menü wird durch veraltete Händlerreferenzen geschlossen.
- [ ] Heimat bleibt nach Speichern/Neuladen erhalten. Ein bisheriger markierter Lagerhändler ohne Heimatdaten übernimmt seinen aktuellen Standort; ein normaler unmarkierter Händler erhält keine neue Lagerroutine.
- [ ] Eine versperrte Heimkehr zerstört keine Blöcke und teleportiert nicht. Am Rand geladener Chunks entstehen keine zusätzlichen Chunktickets durch die Routine.
- [ ] Angebote, Kauf/Bezahlung, Angebotsbenutzungen und Schleichen-Rechtsklick zum Journal bleiben funktionsfähig. `NoAI` unterdrückt die neue Bewegung.

## Zusatz im aktuellen Quellstand: Herden und Fluggruppen

- [ ] Neue Welt mit Standardeinstellungen erkunden: Wollkühe in Ebenen/Wiesen, Geier in trockenen Biomen und Irrlichter nachts in Wäldern/Sümpfen natürlich antreffen. Eine gestellte Showcase-Gruppe gilt nicht als natürlicher Fund.
- [ ] Zusammensein, getrennte Folgeplätze, Höhenverlauf, Ruhe und Rückkehr eines örtlich getrennten Mitglieds beobachten; keine ständigen Kollisionen, Oszillation oder Teleportation.
- [ ] Ein tatsächlicher Treffer löst begrenzte gemeinsame Flucht aus. Nach Alarmende beruhigen sich die Tiere; benachbarte andere Gruppen, Haustiere und Händler bleiben getrennt.
- [ ] Leittier entfernen, Mitglied entladen und dieselben Gruppen nach Speichern/Neuladen prüfen. Kein achter/siebter Teilnehmer überfüllt eine Sechsergruppe; vorhandene manuelle Tiere und Haustiere despawnen nicht plötzlich.
- [ ] Neue Hindernisse, verschiedene Höhen, Chunkgrenzen und zwei tatsächliche Clients prüfen. Kein unbekannter Chunk wird für ein Wander-/Flugziel angefordert.
- [ ] Natürliche Spawns global und je Art abschalten und wieder einschalten. Keine bereits vorhandenen Tiere werden dadurch gelöscht; normales Kreaturenlimit und lokale Dichte greifen.
- [ ] Gegner während Windup ihr Ziel wechseln oder verlieren lassen: Vorbereitung endet, kein falscher Nachtreffer/Spawn, normaler Cooldown. Bereits fliegende Geschosse folgen weiterhin ihren eigenen Regeln.

## 1. Installation, Upgrade und grundlegende Bedienung

- [ ] Neue Instanz startet ohne fehlende Klassen, Ressourcen oder Datenpaketfehler.
- [ ] Beim Upgrade liegt nur `dungeonsalive-0.4.0-alpha.jar` im Mods-Ordner; die alte Mod-JAR ist entfernt.
- [ ] Eine gesicherte Welt einer früheren Alpha laden: Angenommene/abgeschlossene alte Quests und deren Fortschritt bleiben erhalten.
- [ ] Neue Spieler erhalten genau ein Reisetagebuch. Bestehende Spieler bekommen nicht bei jedem Login ein weiteres.
- [ ] **J**, Journal-Rechtsklick und `/dungeonsalive journal` öffnen dieselbe Aufgabenansicht.
- [ ] **B** öffnet das 3D-Handbuch. Beide Tasten sind umbelegbar und verdrängen keine bewusste eigene Tastenbelegung.
- [ ] Deutsch/Englisch prüfen: Namen, Bedienung, Tooltips und Berufe erscheinen lesbar. Die Aufgabenbeschreibungen sind derzeit deutsch.
- [ ] Fortschrittsmeldungen öffnen ein geschlossenes Journal nicht von selbst. Das Journal pausiert die laufende Welt nicht.

## 2. Handbuch und Oberfläche

Das Handbuch zeigt die tatsächlich registrierten feindlichen und friedlichen Figuren, 19 Berufsrollen und den Ausrüstungseinstieg. Den aktuellen Figurenbestand gegen `design/implementation-status.json` und den ausgeführten Stand in `VALIDATION.md` prüfen. Der größere Quellenkatalog ist Entwicklungsdokumentation; seine zusätzlichen Referenzeinträge sind keine weiteren Vorschau-Entities.

- [ ] Nach Namen und Registry-ID suchen; leere Trefferliste, Löschen der Suche und erneute Auswahl prüfen.
- [ ] Vorhandene Gegner-, friedliche und Händlerfilter durchschalten; keine registrierten Einträge bleiben unzugänglich.
- [ ] Zwischen Kreaturen, Berufen und Ausrüstung wechseln; Auswahl, Seitenanzeige und Suche bleiben verständlich.
- [ ] Ruhe-, Lauf- und Aktionsmodus testen. Gegner erhalten ihre tatsächlich gebundenen Aktionen, friedliche Figuren Bewegung beziehungsweise Geste; keine erfundenen Tierangriffe. Bei Berufen eine Arbeitssequenz ansehen.
- [ ] Unterschiedliche individuelle Rigs prüfen, einschließlich Kessel, Wächter, Wildfire, Reiter, Tiere und Schleime. Keine rosa/schwarzen Flächen, fehlenden Teile oder abgeschnittenen UVs.
- [ ] Handbuch öffnen/schließen und Auswahl häufig wechseln: keine echten Weltgegner, Geschosse, Bossleisten, Beute oder Questfortschritte entstehen.
- [ ] 1280×720 oder größer und niedrige GUI-Skalierung: Texte, Knöpfe und Modelle klar.
- [ ] Hohe GUI-Skalierung bis zu einer kleinen logischen Oberfläche: Auswahl, Blättern, Animation und Schließen bleiben erreichbar.
- [ ] Journal bei kleiner Oberfläche: lange Details scrollen, Voraussetzungen/Belohnungen erreichen und zwischen Seiten wechseln.
- [ ] Fenster während geöffneter Menüs ändern; keine überlappenden Schaltflächen, verlorenen Suchfelder oder unscharf überzeichneten Inhalte.
- [ ] Geöffnete Journalansicht aktualisiert Fortschritt, ohne eine andere ausgewählte Aufgabe zu verdrängen.

## 3. Die vier Einstiegsquests vollständig spielen

Eine Aufgabe muss ausdrücklich angenommen werden. Ihre Folgequests werden erst nach abgeholter Belohnung verfügbar.

| Aufgabe / ID | Konkrete Prüfung | Erwartetes Ergebnis |
| --- | --- | --- |
| **Kleine Wunder** — `dungeonsalive:small_wonders` | Annehmen, drei Löwenzahn und drei Mohn bereithalten. Vor Abholung einen Mohn weglegen. | Fehlender Mohn macht das Ziel wieder unerfüllt. Mit allen Blumen: drei Honigflaschen und 20 XP-Punkte. Blumen bleiben erhalten. |
| **Unter grünen Kronen** — `dungeonsalive:woodland_paths` | Nach Kleine Wunder annehmen, `minecraft:forest` besuchen und vier Eichen-Setzlinge halten. | Besuch bleibt gespeichert. Belohnung: zwölf Fackeln, vier Brot und 30 XP-Punkte. Setzlinge bleiben. |
| **Unruhige Nächte** — `dungeonsalive:restless_nights` | Nach Kleine Wunder annehmen, danach selbst fünf Zombies und drei Skelette töten. | Nur passende eigene Kills seit Annahme zählen. Belohnung: Windhorn und 50 XP-Punkte. |
| **Ein Ort zum Bleiben** — `dungeonsalive:a_place_to_return` | Nach Unter grünen Kronen annehmen; 16 Bruchstein, acht Eichenstämme und ein Lagerfeuer halten. | Vier Laternen, sechs Smaragde und 35 XP-Punkte; Baumaterial bleibt. Nach Abholung werden die zugehörigen Begegnungs- und Artefaktaufgaben verfügbar. |

- [ ] Kills vor Annahme zählen nicht rückwirkend; freigeschaltete Aufgaben starten nicht automatisch.
- [ ] Wiederholtes Klicken auf Abholen beziehungsweise erneuter Claim-Befehl zahlt nichts doppelt aus.
- [ ] Volles Survivalinventar: nicht untergebrachte Belohnungen liegen nachvollziehbar am Spieler.
- [ ] Zwei Spieler haben unabhängige Annahmen, Zähler und Belohnungen.
- [ ] Besitz-Ziele werden nach Weglegen, Tod oder Wiederverbinden anhand des aktuellen Inventars geprüft.

## 4. Begegnungssiegel und neue Herausforderungen

Die Details und Rezepte stehen in [ENCOUNTERS.md](ENCOUNTERS.md).

- [ ] Nach Abholung von Ein Ort zum Bleiben sind die in den aktuellen Questdaten enthaltenen Herausforderungen verfügbar, weiterhin ausdrücklich anzunehmen.
- [ ] Alle gelieferten Begegnungssiegel haben einen Namen, sichtbare Grafik, Tooltip und funktionierendes Rezept.
- [ ] Redstone-Golem-Siegel im Survival herstellen, Aufgabe `dungeonsalive:encounters/redstone_golem` annehmen, freie Fläche vorbereiten.
- [ ] Ohne Schleichen benutzen: verständlicher Hinweis, kein Spawn und kein Verbrauch.
- [ ] Seitlich auf einen Block klicken, zu enger Raum, unvollständiger Boden, Wasser, Lebewesen im Spawnraum: kein Spawn/Verbrauch.
- [ ] Friedlicher Schwierigkeitsgrad: keine Begegnung.
- [ ] Auf geeigneter Fläche schleichen und auf den Boden klicken: genau ein Gegner, genau ein verbrauchtes Siegel.
- [ ] Sofort auf einen anderen Siegeltyp wechseln: gemeinsame 200-Tick-Abklingzeit verhindert die zweite Aktivierung.
- [ ] Nach Cooldown nahe einer bestehenden Boss-/Minibossbegegnung aktivieren: Abstandspuffer verhindert eng gestapelte Begegnungen.
- [ ] Genügend entfernt eine weitere erlaubte Begegnung aktivieren: funktioniert ohne starren globalen Ein-Boss-Zwang.
- [ ] Der Spawn entfernt keine Blöcke und baut keine Arena. Geplante Spielergebäude bleiben erhalten.
- [ ] Golem selbst besiegen: passendes Ziel erfüllt, acht Smaragde, ein goldener Apfel und 80 XP-Punkte einmalig abholen.
- [ ] Einen anderen Gegner töten: erfüllt diese Aufgabe nicht. Gleiche Aufgabe bleibt nach Claim abgeschlossen.
- [ ] Je eine besonders große, kleine, stationäre und aquatische Boss-/Minibossform am Siegelzugang prüfen; Dimensionen und freier Platz stimmen.

## 5. Kampf und sichtbare Animationen

Jede Fähigkeit braucht eine lesbare Vorbereitung und ein nachvollziehbares wirksames Fenster. Hier geht es um tatsächliche Weltbegegnungen, zusätzlich zur Handbuchansicht.

| Szene | Was sichtbar und spielerisch passen muss |
| --- | --- |
| Royal Guard | Schildhaltung von Angriff unterscheidbar; Frontrichtung, reduzierte Treffer und Nahkampfzeitpunkt passen zusammen |
| Enchanter mit Verbündeten | Verstärkung ist lesbar, begrenzt und unterbrechbar durch das Besiegen des Unterstützers |
| Wraith / Iceologer / Whisperer | Gefahrenfläche wird rechtzeitig markiert; Ausweichen bleibt möglich; Effekt stimmt mit Fläche überein |
| Drowned Necromancer / Heart of Ender | Strahl richtet sich nachvollziehbar aus; neu platzierte solide Deckung schützt |
| Redstone Golem / Monstrosity | Gewicht und Schlagvorbereitung stimmen; Schaden kommt nicht schon in der Ruhepose |
| Leapleaf / Redstone Cube | Sprungbahn, Landung und Flächenwirkung passen zusammen |
| Wildfire / Illusioner | Schild-/Salven-/Teleportationsphasen sind erkennbar, keine unsichtbaren Soforttreffer |
| Summoner | Beschworene Helfer bleiben begrenzt, verlieren nach Ablauf ihre Existenz und liefern keine normale Beute/XP |
| Boss unter halber Gesundheit | Bossleiste und Phase stimmen mit tatsächlicher Gesundheit überein; Tod entfernt die Leiste |

- [ ] Ruhe, Gehen, Blickrichtung, Verletzungsreaktion und Tod mehrerer Familien ansehen.
- [ ] Trefferzahl pro einzelner Aktion prüfen; keine Dauerschadensschleife durch wiederholte Renderframes.
- [ ] Lauf- und Aktionsübergänge bei hoher und niedriger Bildrate vergleichen.
- [ ] Kreativ-/Zuschauerspieler und verbündete Teams werden nicht als normale Gegner getroffen.
- [ ] Friedliche Tiere im Geschossweg bleiben geschützt; ein gültiger Gegner dahinter kann trotzdem getroffen werden.
- [ ] Beschwörer oder Projektilbesitzer entfernen/entladen: verwaiste Geschosse werden nicht zu Treffern auf beliebige Tiere.
- [ ] Teleportation an Wänden, Wasser und Chunkgrenzen prüfen; keine Platzierung in Blöcken oder erzwungene Terrainexploration.
- [ ] Einen Boss während der Vorbereitung speichern, regulär neu laden: angefangener Treffer wird nicht nachträglich wiederholt.
- [ ] Zwei Clients sehen dieselben Vorwarnungen, Treffer und Phasen. Latenz beeinflusst Lesbarkeit, aber nicht die Anzahl serverseitiger Wirkungen.

## 6. Neue Berufe und vorhandene Dorfbewohner

Für tatsächliche Arbeit **keine** Dorfbewohner mit `NoAI:1b` verwenden. Ein Handbuchmodell ist kein Nachweis für die Vanilla-Arbeitsplatzsuche.

- [ ] Jede der sechs Stationen herstellen, platzieren, in vier Richtungen drehen und wieder abbauen; passendes Item wird zurückgegeben.
- [ ] Unbeschäftigten erwachsenen Dorfbewohner bei Tag mit erreichbarer Station bereitstellen. Er nimmt den richtigen Beruf an.
- [ ] Eigenen Berufsnamen, Kleidung und Handelsfenster prüfen; keine unübersetzten Registry-Schlüssel.
- [ ] Auf allen fünf Stufen sinnvolle Angebote erreichen. Angezeigte Kosten werden verbraucht, Ergebnis und Erfahrung erscheinen einmal.
- [ ] Beim mysteriösen Händler ist das konkrete Angebot bereits vor dem Bezahlen sichtbar.
- [ ] Ausverkauftes Angebot wird im normalen Arbeitsablauf nachgefüllt; Entfernung des Arbeitsplatzes verhindert normales Restocking.
- [ ] Speichern/neuladen erhält Beruf, Karriere und bestehende Angebote.
- [ ] Die sechs Rollen bieten derzeit normalen Handel: keine nicht vorhandenen Upgrade-/Reroll-/Geschenktransfermenüs erwarten.
- [ ] Bei wechselnder Jobstelle stimmen profession, Kleidung und Bewegung zusammen; Kinder und Nichtsnutze erhalten keine Arbeitsgeste.

### Berufsbewegungen

Alle 13 Vanilla-Berufe und sechs neuen Rollen im Handbuch ansehen, dann mindestens Schmied, Bibliothekar, Kartograf, Fischer und Geschenkverpacker an echten Arbeitsstellen vergleichen. Eine Arbeitssequenz beginnt nur nach tatsächlicher Arbeit; die Vanilla-KI arbeitet nicht ununterbrochen. Gegebenenfalls mehrere Arbeitsintervalle abwarten.

- [ ] Unterschiedliche Rollen haben unterschiedliche Bewegungen und passende Requisiten.
- [ ] Werkzeuge sitzen an der richtigen Hand, schweben nicht im Gesicht und stecken nicht dauerhaft im Tisch.
- [ ] Blick richtet sich sinnvoll zur tatsächlichen Arbeitsstelle; Arme/Bekleidung sind aus mehreren Blickwinkeln korrekt.
- [ ] Gehen zur Station zeigt normales Gehen, nicht schon die volle stehende Arbeitsgeste.
- [ ] Stehen neben einem fremden oder fehlenden Arbeitsplatz erzeugt keine erfundene Arbeit.
- [ ] Handelsfenster öffnen: Arbeitsgeste weicht der Handelsbewegung.
- [ ] Gefahr/Panik und Schlaf unterbrechen Arbeit nachvollziehbar.
- [ ] Nach Verlassen der Station blendet Arbeit aus; keine endlose Schleife nach Chunk- oder Weltwechsel.
- [ ] Arbeitsrequisiten erzeugen keine Gegenstände im Inventar oder als Beute.
- [ ] Konfiguration `workProps`/Intensität prüfen. Renderer-Einstellungen `enabled` und `preferExternalModels` nach Clientneustart prüfen.

## 7. Optionales Spawn-Datapack

`examples/datapacks/dungeons_wilds` in die Testwelt kopieren und neu starten. Die Spawngewichte sind keine festen Zeitintervalle.

- [ ] Pack wird erkannt; keine unbekannten Entity-/Biommodifikator-IDs.
- [ ] In Wald, Sumpf, Wüste, Dschungel, Bergen und äußerem End passende reguläre Gegner bei zulässigen Spawnbedingungen finden.
- [ ] Neu-/nachgeladene Bereiche und reguläre Moblimits berücksichtigen; ein leerer Bereich nach wenigen Sekunden ist kein Fehlbeweis.
- [ ] Keine natürlichen Bosse oder Minibosse entstehen durch dieses Pack.
- [ ] Gegnerdichte und Mischung mit Vanilla über längere Spielzeit beurteilen; niedrige Gewichte bei Bedarf anhand echter Beobachtungen anpassen.
- [ ] Pack entfernen und neu starten: keine neuen Pack-Spawns; bereits vorhandene Entities bleiben erwartungsgemäß bestehen.

## 8. Atmosphäre und Windhorn

- [ ] Tagsüber bei klarem Wetter auf Wiese/im Wald: Schmetterlinge mit Flügelschlag, vereinzelt Sporen.
- [ ] Nachts im Wald/Sumpf: pulsierende Glühwürmchen, keine behauptete echte Blocklichtquelle.
- [ ] Regen verhindert neue automatische Insekten; vorhandene dürfen ihre begrenzte Lebensdauer beenden.
- [ ] Höhle, Nether und Unterwasserbereich erhalten keine automatischen Oberflächeninsekten.
- [ ] Partikeloption **Verringert** senkt Dichte, **Minimal** stoppt/entfernt verwaltete Effekte; Weltwechsel räumt auf.
- [ ] Windhorn-Rezept aus Ziegenhorn, Kupferbarren, Feder und Leder funktioniert.
- [ ] Naher Feind wird zurückgestoßen und kurz verlangsamt; Ton/Partikel passen. Kein direkter Schaden.
- [ ] Geschlossene Wand, Abstand über sechs Blöcke, friedliche Tiere, Spieler und Verbündete bleiben geschützt.
- [ ] Zweites Windhorn umgeht die gemeinsame 200-Tick-Itemsperre nicht.

## 9. Persistenz, Mehrspieler und optionale Mods

- [ ] Teilweise erfüllte Aufgabe nach Reconnect/normalem Serverneustart fortsetzen; kein neues Startjournal.
- [ ] Abgeholte Aufgabe bleibt abgeschlossen; Sammelziele prüfen das tatsächliche neue Inventar.
- [ ] `/reload` erhält gültigen Fortschritt; fehlerhafte Questdaten ersetzen nicht still den letzten gültigen Katalog.
- [ ] Dedicated Server mit zwei echten Clients starten: keine Client-/Rendererklassen fehlen auf dem Server.
- [ ] Zwei Spieler aktivieren, handeln und kämpfen parallel, ohne den Fortschritt des anderen versehentlich zu übernehmen.
- [ ] Optional Enchant With Mob + Bagus Lib und dessen Questpack laden: eigener `dungeonsalive:enchanter` und fremder `enchantwithmob:enchanter` bleiben getrennt.
- [ ] Ohne optionale Fremdmod werden geschützte Integrationsquests übersprungen; der aktuelle Grundbestand bleibt geladen.
- [ ] Fresh Animations + EMF + ETF separat laden: externer Villager-Renderer bleibt bei Standardeinstellung aktiv; neue Berufe und Handel funktionieren weiterhin.
- [ ] Keine Behauptung, dass eigene Arbeitsbewegungen automatisch in das externe Modell gemischt würden. Handbuchanzeige bei externem Renderer entsprechend bewerten.
- [ ] Shader und weitere Modellmods erst nach Grundabnahme ergänzen und konkrete Kombination dokumentieren.

## 10. Gefangenen befreien und tatsächliches Folgen prüfen

In einer freien Testfläche `/summon dungeonsalive:captured_blacksmith` verwenden oder den Schmied in einem tatsächlich erzeugten optionalen Lager aufsuchen. Keine `NoAI`-Entity für den Folgetest verwenden. Der separate `blacksmith` ist ein Händler; der `captured_blacksmith` ist ein Zivilist mit Befreiungszustand.

- [ ] Vor der Befreiung: wartende, gebeugte Haltung; kein feindliches Spielerziel und kein gewöhnliches Handelsfenster.
- [ ] In Reichweite ohne verdeckende Wand mit der Haupthand rechtsklicken: Bestätigung, sichtbare Befreiungsgeste und Zuordnung zum tatsächlichen Retter. Kein Gegenstand, Questfortschritt oder Handelsangebot wird dabei als angebliche Originalbelohnung erfunden.
- [ ] Danach mindestens sechs Blöcke weggehen: Derselbe Schmied bewegt sich mit eigener Navigation tatsächlich zum Retter. Die Darstellung allein, ein geändertes Flag oder ein bloßes Drehen reicht nicht als Erfolg.
- [ ] Dies mit mehreren frisch gespawnten Exemplaren wiederholen. Zusätzlich die automatische Regression mit ungerader Entity-ID aus `GAMETESTS.md` ausführen; sie deckt den konkret gefundenen KI-Taktfehler ab.
- [ ] Erneuter Rechtsklick des Retters schaltet Warten ein; weiteres Weggehen bewegt den Wartenden nicht. Nächster Rechtsklick erlaubt wieder Folgen.
- [ ] Speichern, Server regulär neu starten und als derselbe Spieler verbinden: Befreiung, Retter und gewählter Wartezustand bleiben erhalten. Keine zweite Befreiungsbelohnung oder neue Entity-Identität entsteht.
- [ ] Ein zweiter Spieler kann den bereits zugeordneten Schmied nicht übernehmen oder dessen Wartezustand umschalten. Beide Clients sehen denselben Zustand.
- [ ] Mit einem normalen Haustier ebenfalls Warten/Folgen und Reconnect prüfen, weil beide denselben Follow-Goal verwenden.

## 11. Aquatische Begleiter und tatsächliches Schwimmen

Ein freies, mindestens sieben Blöcke tiefes Becken mit festem Boden und erreichbarem Ufer vorbereiten. `pet_baby_squid` ist aquatisch; `pet_ruby_baby_turtle` ist amphibisch. Für echten Navigationsnachweis dürfen weder Teleportation noch erzwungene Bewegungswerte den Mob verschieben.

- [ ] Baby-Squid per Rechtsklick einem Besitzer zuordnen. Seitlich **und getrennt davon deutlich höher im Wasser** wegschwimmen: Der Begleiter folgt durch tatsächliche Wasserbewegung und gewinnt Höhe. Ein nur horizontaler Weg ist kein Höhenbewegungsnachweis.
- [ ] Den Wasserbegleiter längere Zeit beobachten: kein Ertrinkungsschaden, kein Spieler-Angriffsziel und kein ungewollter Zwang zur Oberfläche. Warten stoppt das Folgen; erneutes Freigeben setzt es fort.
- [ ] Beim Baby-Squid sind verbundene Tentakelsegmente und unterschiedliche Biegephasen sichtbar. Keine durchsichtigen Lücken oder losgelösten Spitzen; Körper und Originaltextur bleiben erkennbar.
- [ ] Rubinschildkröte an Land und im Wasser prüfen: passende Fortbewegung und erreichbarer Uferweg, ohne sie als ausschließlich wassergebundenen Squid zu behandeln.
- [ ] `armored_drowned`, `pale_armored_drowned` und `armored_trident_drowned` in Wasser mit echter KI bewegen lassen: geneigter Schwimmkörper und wechselnde Armbewegung; äußere Haut und Rüstung folgen mit.
- [ ] Einen aquatischen Gegner ruhig auf dem Wasserboden stehen lassen: bloßer Wasserkontakt kippt ihn nicht in die aktive Schwimmpose. Nach Verlassen des Wassers normale Landpose und Navigation.
- [ ] Dieselben Szenen aus einem seitlichen und einem schräg frontalen Blickwinkel betrachten; Kopf, Arme, Beine und gehaltene Gegenstände dürfen nicht durch unpassende Elternrotationen auseinanderfallen.

### Dreizackhaltung und Abwurf

- [ ] Der gepanzerte Dreizack-Ertrunkene hält den tatsächlichen Dreizack im Idle. Beim aktiven Wurf startet der tatsächliche Benutzungszustand und damit Vanillas gesondertes Wurfmodell; die Spitzen zeigen in eine zur Ausholbewegung passende Richtung.
- [ ] Der sichtbare Abwurf fällt mit dem tatsächlichen Serverprojektileffekt zusammen: genau ein Dreizack, kein vorzeitiger Treffer während des Windups und kein zweites Projektil aus der Item-Benutzungslogik.
- [ ] Nach Aktion, Abbruch oder normalem Neuladen endet der Benutzungszustand. Kein hängendes Wurfmodell, nachgeholter Sofortwurf oder aufsammelbarer Gratis-Dreizack.
- [ ] Der native Aufnahmebericht muss den Wurf mit `server_using_item`/`client_using_item`, der tatsächlichen Item-ID und den Benutzungsticks belegen. Ein lokal gedrehtes Item oder eine reine Handbuchpose ersetzt diesen Zustandsnachweis nicht.

## 12. Echte Konstrukte und Deckung

Die Auslöser und Lebenszeiten stehen in [CONSTRUCTS_AND_SWIMMING.md](CONSTRUCTS_AND_SWIMMING.md). Geomancer, Iceologer und Whisperer in einer getrennten Survival-Testszene tatsächlich angreifen lassen. Ein nacktes `/summon` des Konstrukts ohne gültigen Besitzer/Ziel ist kein gleichwertiger Mechaniktest.

- [ ] Geomancer-Wände wachsen sichtbar auf, bilden anschließend echte Kollision/Deckung und verschwinden wieder. Die Seite zum Zaubernden bleibt gemäß aktueller Formation offen.
- [ ] Während des Wachstums in eine vorgesehene Pfeilerposition laufen: keine nachträgliche feste Wand innerhalb des Spielers. Blockierte/ungestützte Plätze erzeugen keinen unsichtbaren Ersatztreffer.
- [ ] Aktiver Pfeiler schützt vor passendem gegnerischem Geschoss, Strahl und dem eigenen Corrupted Beacon. Wand zerstören und anschließend wieder frei schießen.
- [ ] Bombenpfeiler: Vorbereitung bleibt schadensfrei; die sichtbare Zündung löst nur einmal Schaden aus. Weglaufen oder neu gesetzte Deckung schützen; Tiere neben dem Ziel bleiben unversehrt.
- [ ] Iceologer: Eis formt sich über einem geprüften freien Bereich, fällt wirklich, landet auf aktueller Kollision und trifft höchstens einmal. Eine nachträglich gesetzte Plattform fängt den Fall ab.
- [ ] Ranke: nur das echte Ziel am ursprünglichen Ort erhält den vorgesehenen Effekt. Verlassen, Zerstören oder Besitzerverlust beendet die kurze Verlangsamung; kein Heranziehen/Teleportieren des Opfers.
- [ ] Nach Entfernen des Zaubernden beziehungsweise nicht mehr geladenem gültigem Ziel verschwinden zugehörige Objekte. Wiederholte Aktionen bleiben an die begrenzte Anzahl und Lebenszeit gebunden.
- [ ] Während einer Vorbereitung speichern/entladen und später regulär laden: abgelaufene Explosionen oder Eis-Treffer werden nicht als Sofortschaden nachgeholt. Keine Konstruktbeute, XP oder Terrainzerstörung.
- [ ] Zwei verbundene Clients sehen dieselben Phasen und Kollisionen; eine geringe Bildrate vervielfacht keinen Schaden.

## 13. Artefakte, Seelen und neue Ausrüstung

Alle Werte, IDs und Rezepte stehen in [GEAR_AND_SOULS.md](GEAR_AND_SOULS.md). Im Survivalmodus und mit zwei getrennten Spielern prüfen. Kreativtests belegen die Seelenkosten nicht, weil Kreativbenutzung bewusst kostenlos ist.

- [ ] Alle neuen Items über Rezepte und **B → Ausrüstung** aufrufen; GUI-, Boden- und Handdarstellung besitzen die richtige Textur und Ausrichtung.
- [ ] Stiefel, Pilz und Amulett: tatsächliche Effekte, sichtbare Dauer und Abklingzeit stimmen. Zweiter Stack oder Hotbarwechsel umgehen die gemeinsame Sperre desselben Itemtyps nicht.
- [ ] Shock Powder und Gong: sichtbarer Feind wird betroffen; geschlossene Wand, verbündete/besessene Kreatur, Spieler und friedliches Tier werden durch den Spezialeffekt geschützt. Die Gong-Verwundbarkeit endet wieder.
- [ ] Mit einem Seelenartefakt im Inventar zugerechnete feindliche Kills ausführen: gemeinsamer begrenzter Spieler-Seelenvorrat wächst. Passive/verbündete Kills erzeugen keine Seelen. Andere Spieler erhalten nicht versehentlich dieselbe Gutschrift.
- [ ] Zwischen zwei Seelenartefakten und zwei Stacks wechseln: derselbe tatsächliche Vorrat bleibt maßgeblich. Eine kopierte Tooltip-Anzeige darf keine zusätzliche Kaufkraft erzeugen.
- [ ] Harvester mit zu wenig und ausreichend Seelen aktivieren: ohne Deckung passende Feinde, einmalige Kosten, keine Kosten für abgewiesene sofortige Wiederholung und kein Schaden an Terrain/Tieren.
- [ ] Corrupted Beacon halten: bestätigte Pulse kosten jeweils Seelen und treffen nur sichtbare passende Ziele. Glas-/Blockdeckung und aktive Geomancer-Wand begrenzen den Strahl.
- [ ] Beacon während des Haltens loslassen, Hotbar wechseln und bis zur Seelenerschöpfung halten: Strahl endet jeweils; erneute Aktivierung bleibt während der Abklingzeit blockiert. Zwei Clients sehen keinen fortlaufenden Strahl nach Serverende.
- [ ] Blitzstab ohne gültiges sichtbares Ziel benutzen: kein Verbrauch/kein Cooldown. Mit Ziel passende Wirkung und Kosten; kein echtes Blitzfeuer und keine Blockzerstörung.
- [ ] Mit Seelen sterben, respawnen und regulär neu verbinden: gespeicherter Vorrat entspricht der dokumentierten Verlustregel und wird nicht durch Itemkopien wiederhergestellt.
- [ ] Besondere Waffen im normalen aufgeladenen Nahkampf testen: sichtbare Treffer, Haltbarkeit, begrenzte Heil-/Flächeneffekte und deren separate Pausen. Der normale Direkttreffer und der geschützte Spezialeffekt sind getrennt zu bewerten.
- [ ] Die vier Artefaktaufgaben nach abgeholtem **Ein Ort zum Bleiben** ausdrücklich annehmen und abschließen; Besitz-Ziele bleiben Inventarprüfungen und verbrauchen das Material nicht. Belohnungen nur einmal abholen.

Bei Fehlern Versionen, Reproduktionsschritte und `latest.log` festhalten; bei Darstellung zusätzlich Screenshot oder kurze Aufnahme. Ein offener Punkt wird erst nach tatsächlichem Test als bestanden markiert.


## Neue Ausrüstung – Client-/Survivalabnahme offen

1. Drei Rüstungssets herstellen und jedes Teil einzeln sowie als Vollset anlegen; Wolf-Stiefel-/Hosensaum, Spider-Kapuze, Robenatlas, normale/schmale Spielerarme und Verzauberungsglanz in Ego-/Außenansicht prüfen. Auch gemischte Sets, Trim, Elytra und Ressourcen-Neuladen ansehen.
2. Wolf-/Roben-Schaden und Spider-Tempo/Lebensraub gegen Gegner prüfen. Ein Teil ausziehen, sterben, neu verbinden und Welt laden: kein übriggebliebener Setbonus. Friedliche/besessene Ziele geben keine Spezialheilung.
3. Jeden Bogen spannen und jeden Armbrust-Ladezustand ansehen; eigene Vanilla-Verzauberungen, Quick Charge, Multishot, Infinity, Spektral-/Trankpfeile, Pfeilmangel und Creative prüfen. Automatische Armbrust lädt genau einmal je bezahlter Ladung; explodierende Armbrust erhält Spielerbauten.
4. Hunter-Befehle nur an eigene aktive Vanilla-Wölfe; fremde/sitzende Wölfe bleiben unberührt. Rückgabe erzeugt keine zweite aufhebbare Pfeilkopie. Extra-Multishot- und Infinity-Pfeile nicht erstatten.
5. Seelenheiler bei voller/fehlender Gesundheit sowie ohne Seelen; Geisterumhang gegen selbst gebaute Wände; Flammenköcher mit sieben Pfeilen, Multishot, Ladeabbruch, Tod/Reload und zwei echten Clients testen. Tooltipdaten ändern keine Konten.
6. B → Ausrüstung: vier Kategorien, 34 Gegenstände, Seitenwechsel und kleine Fenstergrößen. `runFeatureShowcase` erzeugt nur Itemansichten; getragene Sets zusätzlich im echten Client ansehen.
