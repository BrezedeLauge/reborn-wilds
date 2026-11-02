# Optionale Animationen und Dungeons-Enchanter installieren

Dieses Paket enthält einen kleinen Installer für zwei **fest gepinnte** optionale Profile. Er lädt die Dateien direkt vom offiziellen Modrinth-CDN und prüft ihre SHA512-Werte. Die fremden Mods und das Ressourcenpaket sind nicht im Projektarchiv enthalten.

**Prüfstand am 1. November 2026:** API-Metadaten, Abhängigkeiten, vollständige Dateigrößen und SHA512 der Downloads wurden geprüft. Die JAR-Metadaten wurden ebenfalls gelesen. Zusätzlich hat das Enchanter-Profil einen echten Headless-Integrationstest bestanden. Der detaillierte Bericht steht in [VALIDATION.md](VALIDATION.md).

| Profil | Erreichte Prüfung | Noch offen |
| --- | --- | --- |
| Animationen | Downloads, Dateigrößen, SHA512 und kompatible Metadaten geprüft | Grafischer Clientstart und Sichtprüfung der Animationen |
| Enchanter | Headless-Server lädt Enchant With Mob und Bagus Lib; echte Entity-Registry und optionales Questziel bestätigt; alle 13 Server-GameTests bestanden | Grafische Sichtprüfung, vollständiger Enchanter-Kampf und echter Client-Multiplayer |

## Voraussetzung

Einen eigenen Minecraft-Instanzordner für **Java 1.21.1, NeoForge 21.1.265 und Java 21** verwenden. Der Installer benötigt **Python 3.10 oder neuer**. Er installiert keinen Launcher und keinen Modloader. Er startet Minecraft nicht und ändert weder Konten noch EULA- oder Ressourcenpaket-Einstellungen.

Bei Prism Launcher ist das Ziel der **Spielordner** der ausgewählten Instanz, in dem `mods`, `resourcepacks`, `saves` und gegebenenfalls `options.txt` liegen. Es ist nicht der übergeordnete Ordner, der nur die Launcher-Metadaten der Instanz enthält. Der gewählte Spielordner muss bereits existieren.

## Profil 1: Animationen

Enthaltene Dateien:

| Projekt | Feste Version | Modrinth-Version-ID | Ziel |
| --- | --- | --- | --- |
| Fresh Animations | 1.10.4 | `xN57JJts` | `resourcepacks` |
| Entity Model Features | 3.3.12-neoforge-1.21 | `197m3VoU` | `mods` |
| Entity Texture Features | 7.2.6-neoforge-1.21 | `lwNx1nD1` | `mods` |

Fresh Animations 1.10.4 wurde bewusst festgehalten; der Installer wählt auch bei neueren Veröffentlichungen immer diese Version. Der Ressourcenpaket-Eintrag der API nennt selbst keine Pflichtabhängigkeiten. EMF und ETF wurden aufgrund der [Autorendokumentation](https://modrinth.com/resourcepack/fresh-animations) explizit in das Profil aufgenommen. EMFs interne Mod-Metadaten verlangen ETF ab 7.2.0; die gepinnte Version 7.2.6 erfüllt das.

Vom Projektverzeichnis aus zunächst prüfen:

```bash
python3 tools/install_optional.py --profile animations --instance "/absoluter/pfad/zur/testinstanz" --dry-run
```

Anschließend tatsächlich installieren:

```bash
python3 tools/install_optional.py --profile animations --instance "/absoluter/pfad/zur/testinstanz"
```

In Minecraft danach **Optionen → Ressourcenpakete → Fresh Animations aktivieren**. EMF und ETF gehören auf den Client. Für einen Dedicated Server ist dieses Animationsprofil nicht vorgesehen.

## Profil 2: Dungeons-Enchanter

| Projekt | Feste Version | Modrinth-Version-ID | Ziel |
| --- | --- | --- | --- |
| Enchant With Mob | 1.21.1-21.7.0 | `BxJSXnXY` | `mods` |
| Bagus Lib | 1.21.1-13.27.0 | `VPif9tbA` | `mods` |

Der Enchanter-Mod verlangt laut seinem internen `META-INF/neoforge.mods.toml` **NeoForge mindestens 21.1.228** und Bagus Lib mindestens `1.21.1-13.3.0`. Die Projektbasis 21.1.265 und die gepinnte Bagus-Lib-Datei erfüllen diese deklarierten Bereiche. Die API meldet für Bagus Lib keine weitere erforderliche Mod-Abhängigkeit.

```bash
python3 tools/install_optional.py --profile enchanter --instance "/absoluter/pfad/zur/testinstanz" --dry-run
python3 tools/install_optional.py --profile enchanter --instance "/absoluter/pfad/zur/testinstanz"
```

Für Multiplayer denselben Inhalt in den Client- und Serverinstanzen installieren und anschließend den gemeinsamen Start prüfen. Der Installer verbindet sich mit keinem Server und kopiert keine Dateien an fremde Rechner.

### Nachgewiesene Ressourcen-ID

Das offizielle, SHA512-geprüfte JAR `enchantwithmob-1.21.1-21.7.0.jar` enthält:

- `assets/enchantwithmob/lang/en_us.json`: `entity.enchantwithmob.enchanter` mit Anzeigename `Enchanter`.
- `item.enchantwithmob.enchanter_spawn_egg` mit passendem Anzeigenamen.
- `data/enchantwithmob/loot_table/entities/enchanter.json`.

Diese Autorenressourcen belegen die vorgesehenen Namen **`enchantwithmob:enchanter`** und **`enchantwithmob:enchanter_spawn_egg`**. Anschließend hat `./gradlew runIntegrationGameTest` die tatsächliche Entity **`enchantwithmob:enchanter`** in der geladenen Registry und als Ziel der optionalen Quest bestätigt. Der Headless-Server hat 25 Dungeons-Alive-Quests einschließlich des optionalen Datapacks geladen. Der Lauf belegt das Laden und die Verknüpfung; ein vollständiger Kampf und die Darstellung im Client bleiben gesonderte Abnahmen.

## Verhalten bei bereits vorhandenen Dateien

Identische Dateien werden anhand der Prüfsumme erkannt und beibehalten. Eine Datei mit demselben Namen und anderem Inhalt führt zum Abbruch. Bekannte anders benannte Versionen derselben Mod werden ebenfalls gemeldet. Der Installer entfernt oder aktualisiert sie nicht automatisch.

Downloads werden zunächst als temporäre `.part`-Dateien im vorgesehenen Unterordner gespeichert. Erst nach Größen- und Hashprüfung werden sie atomisch sichtbar gemacht. Damit auf Unix-Systemen selbst bei einer zwischenzeitlich neu angelegten Zieldatei nichts überschrieben wird, erfolgt die Veröffentlichung über einen atomischen Hardlink und anschließendes Entfernen des temporären Namens. Das benötigt ein Dateisystem mit Hardlinks, beispielsweise APFS, NTFS oder ext4. Auf einem ungeeigneten Dateisystem bricht das Skript ab, statt auf eine ungeschützte Kopie auszuweichen.

`--dry-run` lädt nichts herunter und erstellt keine Verzeichnisse. Eine tatsächliche Installation schreibt ausschließlich nach `mods` und `resourcepacks` innerhalb des ausdrücklich gewählten Instanzordners. Symlink-Zielordner werden abgelehnt. Der vorhandene Minecraft-/NeoForge-Stand wird nicht automatisch aus unterschiedlichen Launcherformaten ermittelt; die oben genannte Instanzbasis muss vorab ausgewählt sein.

## Quellen und Reproduzierbarkeit

Das vollständige Manifest liegt unter [`profiles/optional-mods.json`](../profiles/optional-mods.json). Es enthält für jede Datei Quelle, Projekt- und Versions-ID, Originaldateiname, Größe, SHA512, deklarierte Versionen und Abhängigkeiten. Zur Laufzeit nutzt der Installer ausschließlich diese festen Daten. Er fragt weder „latest“ ab noch ersetzt er eine nicht mehr verfügbare Datei durch eine andere Version.

Die primären API-Nachweise:

- [Fresh Animations xN57JJts](https://api.modrinth.com/v2/version/xN57JJts)
- [EMF 197m3VoU](https://api.modrinth.com/v2/version/197m3VoU)
- [ETF lwNx1nD1](https://api.modrinth.com/v2/version/lwNx1nD1)
- [Enchant With Mob BxJSXnXY](https://api.modrinth.com/v2/version/BxJSXnXY)
- [Bagus Lib VPif9tbA](https://api.modrinth.com/v2/version/VPif9tbA)

Weitere Kandidaten, Quellen und Einschränkungen stehen in [INTEGRATIONS.md](INTEGRATIONS.md).

## Prüfung des Installers

Beide Profile wurden in einer temporären, anschließend entfernten Testinstanz tatsächlich heruntergeladen und installiert. Größe und SHA512 aller fünf Dateien stimmten überein. Wiederholte Installation behielt identische Dateien bei. Zusätzlich geprüft wurden ein schreibfreier Dry-run, Ablehnung fremder Downloadhosts, falscher Loader, unvollständiger Abhängigkeiten und unzulässiger Dateinamen sowie der Erhalt einer absichtlich veränderten lokalen Datei. Eine erst beim abschließenden Anlegen auftretende Dateikollision wurde ebenfalls ohne Überschreiben abgefangen.

Die isolierten Installerprüfungen haben keine Mods ausgeführt. In einem gesonderten echten Headless-Integrationstest wurden danach Enchant With Mob **1.21.1-21.7.0** und Bagus Lib **1.21.1-13.27.0** geladen. ModList, echte Enchanter-Registry und die optionale Questverknüpfung wurden bestätigt; alle 13 Server-GameTests bestanden (Stand 0.2.0; 25 geladene Quests). Der Animationsstack wurde noch nicht grafisch gestartet. Grafische Sichtprüfung, vollständiger Enchanter-Kampf und echter Client-Multiplayer bleiben offen; siehe [VALIDATION.md](VALIDATION.md).

## Dorfbewohner in 0.2

Die eigenen 19 Berufsbewegungen verwenden einen eigenen Villager-Renderer. Bei installiertem EMF bevorzugt `dungeonsalive-villagers-client.toml` standardmäßig das externe Modell. Fresh Animations und unsere Arbeitsposen werden nicht automatisch kombiniert. Prüfe beide Darstellungswege getrennt. Die eigenen neuen Dungeon-Mobs verwenden ihre eigenen Renderer und benötigen Fresh Animations nicht.
