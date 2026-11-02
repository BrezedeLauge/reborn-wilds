#!/usr/bin/env python3
"""Write the current release's human-readable report only from completed evidence."""
import json
from pathlib import Path
ROOT=Path(__file__).resolve().parents[1]
def read(p):return json.loads((ROOT/p).read_text())
def main():
 s=read('reports/validation-summary.json'); inv=read('design/model-delivery-inventory.json')['counts']
 if s['version']!='0.4.0-alpha' or s['server_failed'] or s['junit_failed']:raise ValueError('Unaccepted validation summary')
 for n in ('model','feature','camp'):
  r=read('reports/'+n+'-showcase.json')
  if r.get('complete')is not True or r.get('failure'):raise ValueError('Incomplete native evidence')
 text=f'''# Prüfung des ausgelieferten Stands

Stand **2. November 2026, {s['version']}**. Ziel **Minecraft Java 1.21.1 / NeoForge 21.1.265 / Java 21**. Die Tabelle trennt technische Tests, tatsächlich erzeugte native Bilder und die begrenzte visuelle Einzelabnahme. Ein erfolgreicher Screenshot beweist keine vollständige Originaltreue oder ausbalancierten Bosskampf.

## Tatsächlich abgeschlossene Prüfungen

| Bereich | Ausgeführtes Ergebnis | Nachweis und Bedeutung |
| --- | --- | --- |
| JUnit | **{s['junit_tests']} Tests, keine Fehler** | Questkern, Parser, Speicherung, Animationen, Rigs, Materialien, Ausdrücke und neue Pose-Regressionsprüfungen. [test-results.json](../reports/test-results.json) |
| Echter Core-GameTest-Server | **{s['server_gametests']} erforderliche Tests bestanden** | Registrierungen, Quests, Mobaktionen, Deckung, Konstrukte, Trident, Seelen, Artefakte, Waffen, echte Wasser-/Folgebewegung, Berufe, Handel und Siegel. [Serverbericht](../reports/server-checks.txt) |
| Optionales Camp-Profil | **{s['camp_total_tests']} erforderliche Tests bestanden** | Dieselben {s['server_gametests']} Core-Fälle und **zwei zusätzliche** Tests für geladene Strukturregistries, echte Vorlagenplatzierung und NPC-Interaktionen. Insgesamt {s['camp_total_tests']} verschiedene Serverfälle, keine Addition der wiederholten Core-Fälle. |
| Vollständiger Modelllauf | **131 Figuren, {s['current_gallery_frames']} Hauptbilder** | Tatsächlich registrierte Entities im echten Client: Ruhe, Bewegung, ausgewählte Aktion beziehungsweise Händlerbegrüßung oder Befreiung. Vollständig abgeschlossenes [Manifest](../reports/model-showcase.json). |
| Modell-Bildfolgen | **{s['native_animation_sequences']} Folgen aus {s['native_animation_frames']} nativen PNGs** | Gezielte Bewegungen/Aktionen der neu bearbeiteten Figuren. Diese Auswahl ist kein vollständiger Satz sämtlicher Originalanimationen. |
| Ausrüstung und Konstrukte | **{s['feature_main_frames']} Hauptbilder, {s['feature_sequences']} Folgen aus {s['feature_sequence_frames']} PNGs** | Vierzehn echte Item-GUI-Modelle, vier Konstrukte, Beacon-Seitenbeobachtung und zusätzliche echte Egoansicht. [Featuremanifest](../reports/feature-showcase.json). |
| Lager im echten Client | **{s['camp_main_frames']} Hauptbilder** | Unveränderte eigene Datapack-Vorlagen, tatsächliche NPCs, echte Befreiungsinteraktion. Bewusst manuell im Testgebiet platziert. [Campmanifest](../reports/camp-showcase.json). |
| Modell-Lieferinventar | **{inv['source_snapshots']} Quellsnapshots, {inv['delivered_textures']} Texturen, {inv['model_and_animation_java_files']} Modell-/Animations-Javadateien** | Hashes und Attribution; 133 Reference-Bindings und vier Builtin-Kompositionen. [Inventar](../design/model-delivery-inventory.json). |
| Ausrüstungsinventar | **28 unveränderte Quellen, 28 ausgelieferte Kunstdateien** | Vierzehn Modelle und vierzehn PNGs separat erfasst. [Gear-Inventar](../design/gear-source-inventory.json). |
| Offline-Galerie | **131 Figuren, 352 native Ansichten** | Selbständige HTML-Datei mit Suche, Kategorien und Wechsel zwischen Ruhe/Bewegung/Aktion/Begrüßung/Befreiung. Verkleinerte WebPs und ursprüngliche SHA-256. [Galerie](../reports/native-gallery.html). |

Die **131 Figuren** sind **78 Gegner** (57 reguläre, neun Minibosse, zwölf Bossgrundtypen) und **53 friedliche** (35 Begleiter, vier Wildtiere, zwei Schlüsselgolems, elf Händler, ein ziviler Schmied). **Vier Zauberkonstrukte** werden separat gezählt. Die beiden unregistrierten Modellvorlagen `mage` und `imperfect_heart_of_ender` gehören nicht zur Entity-Anzahl. Der aktuelle Abgleich steht in [implementation-status.json](../design/implementation-status.json).

## Konkrete Fehler, die diese Runde gefunden und behoben hat

**Befreiung und Folgen:** Der neue Schmied blieb im ersten vollständigen Modelllauf trotz korrektem Rettungszustand stehen. Ursache war die Kombination aus Minecrafts alternierender Goal-Taktung und einer zusätzlichen Zehn-Tick-Bedingung: bestimmte Entity-IDs konnten niemals den Navigationsstart erreichen. Der Goal erhält nun jeden Tick sein Update; die Bewegung bleibt im begrenzten Intervall. Ein eigener Servertest mit ungerader Entity-ID und neue echte Weltbilder bestätigen tatsächliches Folgen. Die Aufnahmeprüfung wurde nicht abgeschwächt.

**Wassernavigation:** Der Standard-Blickcontroller setzte den zum Aufwärtsschwimmen benötigten Winkel zurück. Die aquatischen Controller behalten ihn während der Wasserbewegung. Gegner und aquatischer Begleiter bestehen tatsächliche Höhen-/Wegtests. Die Drowned-Modelle lesen den wirklichen Schwimmzustand; Wasserbilder zeigen Neigung und Armbewegung, Landansichten wieder die aufrechte Haltung.

**Dreizack:** Ein passender Armclip allein reichte nicht: Minecraft wählt das umgedrehte Wurf-Itemmodell anhand des echten Benutzungszustands. Die Vorbereitung aktiviert diesen synchronisiert, Freigabe und Abbruch beenden ihn. Native Vorher-/Nachherbilder zeigen jetzt die Spitzen vor der Hand. Der Servertest prüft zusätzlich, dass genau ein echtes Projektil entsteht.

**Eis:** Die korrekt importierte Quelle enthält halbtransparente Formationsflächen. Ein Cutout-Material machte sie zuvor deckend magenta. Der Quellen-Translucent-Pass lässt jetzt die blaue Eisstruktur durchscheinen; originaler Atlas, Formationsring und Landungsring bleiben erhalten. Alle vier Konstrukte wurden bis nach ihrer natürlichen Entfernung aufgenommen. Schlussbilder und `removed=true` stimmen überein.

**Arch-Illager und Baby Squid:** Der Stab wurde korrekt ausgerichtet und um eine eigene dokumentierte Orb ergänzt. Der kleine Tintenfisch erhielt acht eigene zweigliedrige Tentakel mit sichtbaren UVs und phasenversetzter Bewegung. Die Quellenbestandteile und eigenen Ergänzungen sind getrennt ausgewiesen. Tagesaufnahmen belegen Form/Farbe; eine besondere Arch-Orb-Nachtabnahme wird daraus nicht abgeleitet.

**Ausrüstung:** Ein zweiter Minecraft-Hintergrundblur machte zunächst das fertig gezeichnete Handbuch unscharf. Die korrigierten nativen Ansichten zeigen scharfe Itemmodelle und Text. Zu große Beacon-Partikel direkt vor der Kamera wurden durch einen schmalen Strahlverlauf ersetzt. Die zusätzliche Egoaufnahme zeigt weiterhin das tatsächliche Ziel. Der erste seitliche Beobachterausschnitt wurde wegen einer fehlenden Kopfrotation der reinen Aufnahmekamera gezielt neu aufgenommen.

**Camp-Aufnahmen:** Der erste Start blieb am normalen Experimentaldialog der markierten Testwelt stehen. Der Vorstartpfad bestätigt nur genau diesen beobachteten Dialog dieser isolierten Einzelwelt. Anschließend wartete die Entity-Prüfung trotz sichtbarer Vorlage und NPC sowie bereiter Kamera bei Aufnahmezähler null. Ein Wechsel der numerischen NPC-ID durch Chunk-Neuladen ist dafür eine mögliche Erklärung, die im damaligen Log nicht nachgewiesen wurde. Die Prüfung benutzt jetzt die exakte stabile UUID plus registrierten Typ; der Spieler befindet sich vor der Vorlagenplatzierung im Zielgebiet. Der korrigierte Lauf schloss alle drei Aufnahmen ab, wobei seine numerischen NPC-IDs stabil blieben. NPCs wurden für das Foto nicht durch beliebige ähnliche Figuren ersetzt.

## Was tatsächlich angesehen wurde

Die vollständigen Capture-Manifeste dokumentieren erzeugte Bilder und serverbezogene Zustände. Die folgenden Berichte führen die **wirklich geöffneten Einzelbilder beziehungsweise ausdrücklich bezeichneten Modell-Ausschnitte**, ihre Hashes, Vorher-/Nachherläufe und verbleibenden Grenzen. Überschneidungen werden nicht zu einer erfundenen Gesamtzahl abgenommener Figuren addiert.

- [Visuelle Korrekturen](../reports/v04-visual-polish-qa.json): Arch-Illager, Baby Squid, Drowned und befreiter Schmied.
- [Neue Inhalte und Lager](../reports/v04-new-content-visual-qa.json): fünf neue Figuren, richtige Waffenhaltung und echte Vorlagen/NPCs.
- [Konstrukte](../reports/v04-construct-visual-qa.json): vier Hauptzustände, Schlussbilder, zusätzliche Eisphasen. Kleine Rankendetails sind in der gemeinsamen Kamera teilweise durch das Ziel verdeckt.
- [Ausrüstung](../reports/v04-gear-visual-qa.json): alle vierzehn tatsächlichen Modelldetails, ausgewählte vollständige GUI-Ansichten und Beacon-Korrekturen. Dies ist keine Abnahme jedes Buttons und jeder Auflösung.

Die unveränderten aktuellen Haupt-/Sequenz-PNGs liegen vollständig im separaten **DungeonsAlive_Native_Captures_v0.4.0.zip**. Das Projektpaket enthält ausgewählte unveränderte PNGs unter `reports/native-v04/`, eine Galerie aller Modellansichten und die neuen GIFs unter `reports/animations/v04/`. [Archivprüfsumme](../reports/native-archive.json) und [GIF-Herkunft](../reports/animations/v04/provenance.json) sichern die Zuordnung. Beschriftete Übersichten skalieren beziehungsweise beschneiden nur die ausdrücklich protokollierten nativen Ansichten; sie sind keine synthetisch erzeugten Spielszenen.

## Bekannte Grenzen

| Bereich | Noch offen oder bewusst begrenzt |
| --- | --- |
| Vollständigkeit | 436 Katalogzeilen umfassen auch Varianten, NPC-Rollen und Effekte; 47 klare Sift-Zeilen bleiben ausgeschlossen. Nicht alle übrigen Originalinhalte, Ancient-Varianten oder Dungeons-II-Kreaturen sind fertig. |
| Bossmechaniken | Vorhandene serverseitige Aktionsbausteine und Konstrukte sind Minecraft-Adaptionen. Tempest-Säulen, vollständige Herz-Phasen, Klonfolgen und Originalarenen fehlen teilweise. |
| Modelltreue | Treetop bleibt eine Farb-/Größenadaption. Vengeful ist ein eigener detaillierter Nachbau; besondere Sequenzen, alle Übergänge und exakte Seitenproportionen benötigen weitere Abnahme. |
| Ausrüstung | Vierzehn neue Gegenstände, nicht der vollständige Originalbestand. Werte/Rezepte sind eigene Balance. Fighter’s Bindings benutzt schnellen normalen Nahkampf, noch keine eigene beidhändige Faustanimation. |
| Bewohner | Normale Warenangebote und echte Befreiung sind vorhanden; ursprüngliche Upgrade-, Geschenk-, Gild-/Unique-Dienste, Schlüssel-Türen und alle Begleiterfähigkeiten sind weiter offen. |
| Lager | Laden der Worldgen-Daten und echte Template-Platzierung/NPCs geprüft. Keine ausgiebige natürliche Suche über verschiedene Weltsamen, Hanglagen und langfristige Häufigkeiten. |
| Gesamtspiel | Kein vollständiger Survival-Durchlauf aller 29 Grundquests / 21 Siegel, kein Mehrspielertest mit zwei realen Clients, kein Langzeitlasttest und keine umfassende Shader-/EMF-Abnahme. |
| Plattform / Ton | Linux/Mesa llvmpipe, OpenGL 4.5. Windows/macOS und Tonwiedergabe wurden in dieser Umgebung nicht geprüft; die Audioausgabe war nicht verfügbar. |

Die Originalpixel/Modelle stammen aus dokumentierten Community-Quellen oder eigenen Anpassungen. CEM-Hostposen und eigene Übergänge sind keine extrahierten Original-Dungeons-Keyframes. Eigener MIT-Code lizenziert CC-BY-NC-SA-Assets nicht um. [THIRD_PARTY_NOTICES.md](../THIRD_PARTY_NOTICES.md) und die vollständigen Lizenzen sind maßgeblich.

## Historische Nachweise und Wiederholung

Die 0.3-Berichte bleiben unter `reports/history/v03/` erhalten. Frühere Redstone-Weltbilder, das damalige friedliche QA und ältere GIFs im Stamm von `reports/animations/` sind historische Belege. `reports/screenshots/` und `render-smoke.json` stammen teilweise aus 0.2.0. Sie werden nicht als erneut durchgeführte 0.4-Abnahme ausgegeben. Die Spezialintegration Enchant With Mob / Bagus Lib wurde in dieser Runde nicht erneut getestet.

```bash
./gradlew build
./gradlew runGameTestServer
python3 tools/prepare_camp_gametest.py
./gradlew runCampGameTestServer
python3 tools/prepare_mob_model_showcase.py
./gradlew runMobModelShowcase -PshowcaseMobs=all -PshowcaseClips=none
python3 tools/prepare_feature_showcase.py
./gradlew runFeatureShowcase
python3 tools/prepare_camp_showcase.py
./gradlew runCampShowcase
```

Ein grafischer Desktop oder virtueller OpenGL-Bildschirm ist für die letzten drei Aufgaben erforderlich. Die Aufnahmewelten sind ausdrücklich markierte Entwicklungskopien. Offline-Profildienstwarnungen verhinderten die lokalen Tests nicht; eine Microsoft-/Mojang-Anmeldung wurde nicht geprüft. Ein Gradle-Prozess mit Exit 0 reicht bei einer Aufnahme nicht: Das jeweilige Manifest muss vollständig und ohne Fehler sein. Der Serverlog enthält auch den historischen anschließenden Camp-Aufnahmefehler; die bestandenen 30/32 Serverfälle und die später abgeschlossenen nativen Manifeste werden getrennt ausgewertet.
'''
 (ROOT/'docs/VALIDATION.md').write_text(text)
 print('Wrote docs/VALIDATION.md from accepted current evidence.')
if __name__=='__main__':main()
