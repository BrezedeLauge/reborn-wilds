# Lebendige Welt

**Neuerer Ausbau:** [LIVING_WORLD_EXPANSION.md](LIVING_WORLD_EXPANSION.md) ergänzt die unten dokumentierte Grundlage um alle geeigneten Biome, vier eigene Landschaften, zusätzliche Wildformen, Karawanen, Pfade, Wasserströmung, Entdeckungshinweise, Siedlungen und die Verderbnisfront. Die ältere 97-Test-Buildaussage unten ist historisch; aktuelle Quellen und Prüfungen sind dort gesondert benannt.

Die Welt soll auch zwischen Kämpfen und Quests belebt wirken: Ein Schwarm zieht vorbei, Tiere bleiben als Gruppe zusammen, Bewohner gehen ihrem Alltag nach und reagieren auf ihre Umgebung. Das ist ein ausdrückliches Nutzerziel. Bauen, Abbauen, freie Kamera, Farmen und Redstone bleiben Minecraft. Dungeons I und II liefern Kreaturen und Atmosphäre; die Sift bleibt ausgeschlossen.

## Arbeitsstand und Reihenfolge

| Baustein | Stand | Gewünschtes Verhalten |
| --- | --- | --- |
| Kleine Orte | Zwei optionale Lager vorhanden | Händler und befreibare Bewohner haben einen erkennbaren Platz in der Welt. |
| Händleralltag | Erste Umsetzung im weitergeführten Quellstand; Server-/Clientprüfung ausstehend | Heimat merken, tagsüber kurze Wege, bei Nacht/Regen heimkehren, nach Schaden kurz fliehen und zurückkehren. |
| Natürliche Wildtiere | Vier natürliche Habitatzuordnungen im Quellstand; vier Wildtierprofile insgesamt | Wollkühe in Graslandschaften, Geier in trockenen Biomen und Irrlichter nachts in Wäldern/Feuchtgebieten; normale Kreaturengrenzen. |
| Herden | Wollkuh-Gruppen im Quellstand | Gemeinsame gespeicherte Identität, höchstens sechs Mitglieder, lokale Führung/Nachfolge, Wiederanschluss, Ruhephasen und gemeinsamer Schadensalarm. |
| Wollkuh-Zucht | Im Quellstand, native Abnahme ausstehend | Weizen lockt und startet Paarung; Kälber folgen Eltern und wachsen innerhalb begrenzter Herden heran. |
| Fluggruppen | Geier- und Irrlichtgruppen im Quellstand | Echte Entities mit gemeinsamem lokalem Flugkurs, individuellen Folgeplätzen, Abstand, Höhenverlauf und endlicher Flucht. |
| Weitere Rudel und Wanderungen | Weiterer Ausbau | Zusätzliche Arten, längere Wanderungen und besondere Interaktionen benötigen eigene Umsetzung und Spielabnahme. |
| Bewohner und Begegnungen | Berufe, Handelsangebote, Arbeitsgesten und freiwillige Quests vorhanden | Mehr nachvollziehbarer Tagesablauf und Reaktionen an tatsächlichen Orten; regionale Begegnungen schrittweise ergänzen. |

Die vorhandenen Schmetterlings-, Glühwürmchen- und Sporenpartikel sind Atmosphäre. Die neuen Tiergruppen verwenden tatsächliche registrierte Entities und serverseitige Bewegung. Der vollständige Modbuild mit 97 bestandenen JUnit-Tests ist abgeschlossen; alle hier neu beschriebenen Laufzeitänderungen warten weiterhin auf Minecraft-Server- und Clientprüfung. Die neue JAR liegt unter `build/libs/`; die bisherige JAR unter `dist/` enthält die Ergänzungen nicht. [Buildnachweis](../reports/current-build-validation.json).

## Umgesetzt im Quellstand: natürliche Herden und Fluggruppen

`DungeonsCreature` gruppiert die drei vorhandenen Wildtierprofile `wooly_cow`, `vulture` und `wisp`. Ein natürlicher Spawnverband teilt eine Gruppen-UUID, einen örtlichen Anker und einen auf sechs Plätze begrenzten Mitgliederbestand. Manuell erzeugte Tiere derselben Art schließen sich innerhalb von zwölf Blöcken einer freien Gruppe an. Ein Mitglied belegt seinen Platz auch beim Chunkentladen weiter; ein fehlender oder gestorbener Anführer verhindert die Wahl einer lokalen lebenden Führung nicht. Es werden keine entfernten Anführer nachgeladen.

Wollkühe machen gemeinsame kurze Wege, halten sich bei ihrer Gruppe und nähern sich nach einer örtlichen Trennung wieder an. Geier fliegen mit versetzten Folgeplätzen entlang eines gemeinsamen kreisenden Kurses; Irrlichter benutzen kompaktere, getrennte Schwebeplätze. Eine kleine Abstandsregel ergänzt die tatsächliche Flugnavigation. Ziele, ganze Suchbereiche, Körperfreiheit und laufende Flugsegmente werden geprüft, bevor weitergeflogen wird.

Die Gruppen haben aktive und ruhige Phasen: Wollkühe/Geier bei Tag, Irrlichter nachts, Ruhe bei Regen. Ein tatsächlich angenommener Treffer löst für nahe Mitglieder derselben Gruppe einen auf 120 Serverticks begrenzten Alarm aus. Die Tiere fliehen friedlich; Haustiere, Zivilisten und andere Gruppen werden nicht alarmiert. Die Routinen sind lokale Habitatbewegung, noch keine Fernwanderung oder Jagdsimulation. Ruhende Fluggruppen halten eine niedrige Flug-/Schwebeposition; eine eigene Landeanimation wurde nicht ergänzt.

Heimat, Gruppen-ID, Mitglieder und natürlicher Herkunftsmarker bleiben im NBT erhalten. Neue natürliche Tiere nehmen am gewöhnlichen Minecraft-Kreaturenlimit und Despawnverfahren teil. Manuell erzeugte Tiere, frühere persistente Tiere und Haustiere behalten ihren dauerhaften Bestand. Besitz-, Folgen-, Warten- und Befreiungsregeln bleiben getrennt.

### Wollkühe züchten

Weizen lockt unbesessene Wollkühe an. Zwei gefütterte erwachsene Tiere haben bis zu 30 Sekunden Paarungsbereitschaft und benötigen drei Sekunden ruhige gemeinsame Balz. Ein angenommener Treffer oder Gruppenalarm unterbricht sie. Erst ein erfolgreich in der Welt angelegtes Kalb verbraucht die Bereitschaft und setzt beide Eltern für fünf Minuten auf Pause.

Die Geburt prüft freie Gruppenplätze einschließlich reservierter entladener Mitglieder, höchstens 24 geladene Dungeons-Wildtiere im Umkreis von 48 Blöcken und einen sicheren geladenen Standplatz. Das Kalb übernimmt die Gruppenzugehörigkeit, merkt sich beide Eltern und folgt erreichbaren lebenden Eltern durch echte Navigation. Es lädt keine entfernten Eltern nach. Gruppenleitung bevorzugt Erwachsene.

Kälber benötigen 24.000 aktive Serverticks zum Erwachsenwerden; Füttern mit Weizen beschleunigt das Wachstum. Alter, Paarungszeit und Eltern-UUIDs werden gespeichert; alte Tiere ohne diese Daten bleiben erwachsen. Gefütterte Eltern und gezüchtete Kälber werden persistent. Natürliche unberührte Populationen behalten ihre bisherigen Despawnregeln. Bei `NoAI` laufen keine Paarungs-/Wachstumsroutinen. Das Kalb benutzt das vorhandene Modell mit halber Größe, keine neue behauptete Original-Jungtierkunst.

Drei neue Minecraft-GameTests prüfen echte Balz/Geburt, Folgeweg und Speicherung, reservierte Gruppenplätze/lokale Dichte/abgewiesene Entity-Anlage sowie Unterbrechung und Altstand-Kompatibilität. Sie sind geschrieben, aber noch nicht ausgeführt. Fünf reine Zustandsprüfungen laufen im Offline-JUnit-Bestand.

### Natürliche Vorkommen

Die Mod-Ressourcen enthalten standardmäßig vier Biome-Modifier; ein zusätzliches Spawn-Datapack ist dafür nicht nötig. Der normale Minecraft-Spawner fordert für die drei Gruppenarten Verbände von drei bis fünf Tieren an; Sparschweine erscheinen einzeln. Gelände, Kreaturenlimit und erfolgreiche Einzelplatzierungen entscheiden, wie viele davon tatsächlich erscheinen.

| Art | Habitate | Zeit |
| --- | --- | --- |
| Wollkuh | Ebenen, Sonnenblumenebenen, Wiesen; geeigneter Grasboden | Tag |
| Geier | Wüsten, Badlands und Savannen | Tag |
| Irrlicht | Wälder und Sümpfe | Abend/Nacht |
| Sparschwein | Ebenen, Wälder und Savannen auf geeignetem Grasboden | Tag, sehr selten |

Die Habitate sind ausdrücklich Sandbox-Adaptionen. `tools/generate_peaceful_resources.py` erzeugt die drei Gruppenhabitate und [wildlife-habitats.json](../design/wildlife-habitats.json); `tools/generate_piggy_bank_resources.py` erzeugt die gesonderten Sparschwein-Ressourcen.

`config/dungeonsalive-wildlife-common.toml` steuert neue natürliche Spawns über `wildlife.naturalSpawns`, `woolyCows`, `vultures` und `wisps`. Abschalten löscht keine vorhandenen Tiere. `nearbyLimit` begrenzt das laufende natürliche Spawnen zusätzlich auf standardmäßig 24 geladene Dungeons-Wildtiere in 48 Blöcken Umgebung. Die anfängliche Chunkgenerierung verwendet Minecrafts Packgrenzen und greift nicht von einem Generierungs-Thread auf lebende Entity-Verzeichnisse zu. Alle Populationen bleiben echte Kreaturen; es gibt keinen zusätzlichen unbegrenzten Umgebungsspawner.

### Sparschwein: seltene Schatzbegegnung

Das bestehende `piggy_bank` bekommt eigene Mechanik bei unverändertem Quellenmodell. Nach einem tatsächlich angenommenen Treffer flieht es sechs Sekunden lang innerhalb von zwölf Blöcken um den Trefferort. Ziele liegen in geladenem, begehbarem Gelände; es greift nicht zurück und zerstört keine Blöcke. Flucht wird beim Neuladen nicht endlos fortgesetzt.

Ein gültiger tödlicher Spielertreffer liefert bei aktivem `doMobLoot` **6–12 Smaragde und genau einen Gegenstand**: Kraftbogen, Stiefel der Schnelligkeit, Eisenhautamulett oder Todeshutpilz. Bloße Treffer erzeugen keine Münzen. Schatzwahl und Verbrauch werden gespeichert. Zusätzlich führt der gemeinsame Server einen dimensionsübergreifenden Bestand bereits belohnter UUIDs; erneutes Laden einer alten Kopie derselben Kreatur erzeugt keinen zweiten Schatz. Der Bestand fasst höchstens 65.536 Ansprüche und verwirft keine alten Einträge: Bei voller oder unlesbarer Tabelle bleiben weitere Schätze gesperrt. Abgebrochene Drops bleiben unterdrückt. Das sind eigene Beutewerte, keine kopierte Originaltabelle.

Natürliche Sparschweine verwenden den normalen Kreaturenspawner, Einzelpacks mit Gewicht 1 und zusätzlich eine Platzierungswahrscheinlichkeit von 1/8. Beim laufenden natürlichen Spawn gelten das gemeinsame Wildtierlimit und höchstens zwei geladene Sparschweine in 48 Blöcken Umgebung. Die erstmalige Chunkgenerierung benutzt die begrenzten Einzelpacks ohne Zugriff auf lebende Entity-Verzeichnisse. `config/dungeonsalive-piggy-bank-common.toml` kann neue Spawns abschalten; der gemeinsame Wildtierschalter gilt ebenfalls. Bestehende Tiere werden dadurch nicht gelöscht. Es gibt keine Sparschwein-Zucht.

### Schlüsselgolem-Tore

`key_gate` und `diamond_key_gate` sind zwei selbst herstellbare, zweiblockhohe Tore. Die Rezepte kombinieren eine Eisentür, Goldbarren beziehungsweise Diamant und einen Haken. Beim Platzieren merkt sich die untere Hälfte den Erbauer. Nur dieser Spieler öffnet das Tor mit einem lebenden eigenen passenden Schlüsselgolem innerhalb von vier Blöcken und mit freier Sicht. Der Golem wird nicht verbraucht. Schließen darf der Besitzer auch ohne anwesenden Golem. Redstone umgeht die Schlüsselprüfung nicht.

Die Tore bleiben wie normale Minecraft-Blöcke abbaubar; sie ersetzen kein Grundstücksschutzsystem. Abbau, obere/untere Hälfte und Creative-Verhalten benutzen den Tür-Lebenszyklus. Besitzerlose, etwa per Struktur oder Befehl erzeugte Tore lassen sich erst nach eigenem Neuplatzieren benutzen. Die eigenen Torquader referenzieren installierte Minecraft-Texturen, keine neu kopierten Spiel-PNGs. Natürliche Lager und bestehende Türen werden nicht ersetzt. Fünf neue GameTests prüfen Platzierung, Besitzer, Schlüsselart, Sicht, Entfernung, Redstone, Speicherung und die Abbau-/Dropfälle; native Ausführung und Modellansicht stehen aus.

### Tatsächliche Prüfung

17 neue reine JUnit-Fälle prüfen Gruppenlimit, gespeicherte Mitgliedschaft, Nachfolge, Zusammenführen von NBT-Beständen, Tages-/Nacht-/Alarmphasen und Flugformationen. Sie gehören auch zum erfolgreichen vollständigen Build mit 97 JUnit-Tests; frühere Teilmengen werden nicht addiert. Die neuen Spawn-, Herden- und Flug-GameTests sind angelegt, aber noch nicht ausgeführt. Der aktuelle Agent-Versuch scheitert bereits beim Gradle-Start am gesperrten Sandbox-Socket, obwohl die Build-Abhängigkeiten jetzt vorhanden sind. [Prüfstand](VALIDATION.md). Diese Regeln und mathematischen Kurven sind kein Nachweis einer bereits angesehenen Minecraft-Flugszene.

### Echte KI in einer getrennten Entwicklungskopie ansehen

Nach einem erfolgreichen GameTest-Serverlauf:

```bash
python3 tools/prepare_wildlife_showcase.py --dry-run
python3 tools/prepare_wildlife_showcase.py
./gradlew runWildlifeShowcase
```

Der Vorbereiter kopiert ausschließlich `run-gametest/world` nach `run-wildlife-showcase/saves/wildlife-showcase`. Markierungen und Symlinkprüfungen schützen unbekannte Verzeichnisse. Nur in der Kopie wird `Data.allowCommands` mit gesicherter Originaldatei aktiviert; die übrigen entpackten NBT-Bytes bleiben erhalten. Ein Hashmanifest enthält die genaue Bühne und Bedienbefehle. Die Quelle bleibt unverändert.

Im Client `/reload`, `/gamemode spectator @s` und `/execute in minecraft:overworld run tp @s 48 86 -32 0 15` verwenden, dann nach Laden der Umgebung `/function dungeonsalive_wildlife_showcase:stage`. Falls die Funktion noch unbekannt ist, zuerst `/datapack enable "file/dungeonsalive_wildlife_showcase"` ausführen. Die ausdrücklich aufgerufene Funktion baut drei festgelegte Testfelder und setzt je vier echte Wollkühe, Geier und Irrlichter sowie einen Lagerhändler mit aktiver KI ein. Sie verändert ausschließlich die Entwicklungskopie und entfernt nur zuvor für diese Szene markierte Entities.

Die Funktionen `dungeonsalive_wildlife_showcase:day`, `:night` und `:alarm` erlauben Tageswechsel und tatsächlichen Schaden; vor dem Alarm einige Sekunden auf die Gruppierung warten. Für Händlerinteraktionen zum Survivalmodus wechseln. Wiederholte Alarme verursachen wirklichen Schaden, deshalb bei Bedarf neu mit `:stage` aufstellen. Die Szene besitzt keine automatischen Load-/Tick-Funktionen und keine künstlich gesetzte Bewegung. F2-Aufnahmen oder native Fensteraufnahmen müssen den tatsächlichen Client zeigen; es werden keine Bilder automatisch als bestanden ausgegeben.

Diese Bühne ist ein Bewegungs-/Interaktionstest, kein Nachweis natürlicher Spawnverteilung. Ihr Vorbereiter hat acht Daten-/Dateisystemprüfungen bestanden; die Vorbereitung im Projekt wird aktuell wegen fehlender GameTest-Welt korrekt abgewiesen. [Prüfbericht](../reports/wildlife-showcase-preparer-checks.json). Die bestehenden Einzelmodellaufnahmen verwenden teilweise `NoAI` und künstliche Bewegung und ersetzen diese Gruppenprüfung nicht.

## Erste Umsetzung: Alltag der Lagerhändler

Die Routine gilt für `DungeonsMerchant` mit dem bestehenden Tag `dungeonsalive_camp_npc`. Die Händlerlager-Vorlage enthält dieses Tag bereits. Ein alter Lagerhändler ohne gespeicherte Heimat übernimmt beim ersten tatsächlichen serverseitigen KI-Schritt seinen aktuellen Weltstandort. Dadurch werden keine relativen Koordinaten aus der Strukturvorlage als Heimat gespeichert. Händler ohne Tag behalten ihr bisheriges Verhalten; der gefangene Schmied ist weiterhin ein gesonderter Zivilist.

- Tagsüber unternimmt der Händler kurze Wege in seinem Heimatbereich.
- Nachts oder bei globalem Regen kehrt er zur Heimatposition zurück und ruht dort. Dies ist kein Bett-, Dachsuch- oder Innenraumsystem.
- Ein laufender Handel hält ihn an. Akzeptierter, nicht tödlicher Schaden hat Vorrang: Der eigene Handelsdialog wird beendet, der Händler flieht zeitlich und räumlich begrenzt und versucht danach zurückzukehren.
- Heimat und Dimension bleiben im NBT erhalten. Flucht und laufende Wege sind vorübergehend. `NoAI` unterdrückt die Routine auch in den vorhandenen gestellten Fotowelten.
- Navigation benutzt ausschließlich bereits geladene Bereiche, begrenzt Wiederholungen und verändert keine Blöcke. Ein versperrter Rückweg führt zu späteren Versuchen, nicht zu Teleportation oder dem Aufbrechen einer Spielerwand.

Die Regeln sind eigene Minecraft-Adaptionen. Sie sind keine Behauptung eines originalgetreu rekonstruierten Dungeons-Tagesablaufs. Der genaue aktuelle Prüfstand steht in [development-status.json](../reports/development-status.json) und [VALIDATION.md](VALIDATION.md). Die bisherige JAR unter `dist/` enthält diese neue Routine noch nicht.

## Abnahme im tatsächlichen Spiel

In einer gesonderten Testwelt das optionale Lagerpack aktivieren und die unveränderte Händler-Vorlage platzieren. Den Händler mit aktiver KI beobachten: kurze Tageswege; Rückkehr nach Zeitwechsel beziehungsweise Regen; Stillstand beim Handel; Menüabbruch und endliche Flucht nach Treffer. Nach Speichern/Neuladen muss die gleiche Heimat gelten. Auch einen versperrten Rückweg, einen Rand geladener Chunks und zwei tatsächliche Spieler prüfen. Handel, Angebote und Questjournal müssen weiter funktionieren.

Die vorhandenen Lagerfotos halten ihre NPCs mit `NoAI` fest. Sie belegen weder die neue Routine noch tatsächliche Wege. Neue Serverfälle prüfen die Integration; reine JUnit-Regeln belegen keine Minecraft-Wegfindung. Eine neue Veröffentlichung folgt erst nach den erforderlichen Build-, Server- und Clientprüfungen.

## Grenzen für den weiteren Ausbau

Weitere Gruppenarten bauen auf eigener Identität, festen Größenlimits, lokalen Zielen und den Speicher-/Entladeregeln auf. Keine unbekannten Chunks für Wanderungen laden. Interaktive Tiere sind serverseitige Entities; rein dekorative Effekte dürfen keine Kämpfe, Beute oder Quests vortäuschen. Natürliche Population und Haustierbesitz bleiben getrennt. Spielerbauten werden weder zerstört noch durch ungefragte Lager ersetzt.
