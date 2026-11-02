# Weitere Dungeons-Figuren und Befreiungsrolle

## Konkrete Ergänzungen

Dieser Schritt ergänzt **vier Gegnerprofile und eine zivile Rolle**. Die stärkeren Rüstungstiers verwenden vorhandene Quellkunst und bleiben Varianten der bereits katalogisierten Illager. Der gefangene Schmied ist derselbe benannte Charakter in einer eigenen Rolle, kein zusätzlicher Beruf. Die Sift bleibt ausgeschlossen.

| Registry-ID | Quelle und Darstellung | Eigene Funktion / Grenze |
| --- | --- | --- |
| `dungeonsalive:tropical_slime` | Goety-Innenkörper, Augen, äußere Hülle und ursprüngliche Fisch-Animationsfolge | Transparente Hülle und Fischkern folgen derselben Verformung. Eigene Ruhe-, Lauf-, Sprung-, Angriffs- und Todesverformung; ursprüngliches Aufteilen in kleinere Schleime ist nicht Teil dieser Clipadaption. |
| `dungeonsalive:armored_trident_drowned` | Vorhandene geprüfte Seegrasrüstung mit separater äußerer Haut | Tatsächlich gehaltener Dreizack und eigener Wurfclip; der gemeinsame Servercode übernimmt das reale Projektil. Wasserbewegungen und Abnahme stehen im zentralen Prüfstand. |
| `dungeonsalive:diamond_armored_pillager` | Ursprüngliche Diamond-PNG des MIT-Ports auf dem passenden Helm-/Körperrig | Stärkerer Rüstungstier mit Armbrust; kein bloß umbenannter Vanilla-Plünderer. Größe, Lebenspunkte und Schadenswerte sind Sandboxabstimmung. |
| `dungeonsalive:diamond_armored_vindicator` | Ursprüngliche Diamond-PNG auf dem zugehörigen Rig mit gehörntem Helm | Stärkerer Rüstungstier mit dem tatsächlich registrierten Whirlwind-Item in der Hand. Dropchance 0; die Spielerwaffen-Callbacks werden für diesen Mob nicht ausgeführt. Ein vollständiger ursprünglicher Kampf wird damit nicht behauptet. |
| `dungeonsalive:captured_blacksmith` | Bestehendes ReCrafted-Schmiedmodell desselben Charakters | Neuer Typ `CIVILIAN`, mit gespeichertem Befreiungszustand und Retter. Keine normalen Handelsangebote, keine neue Villager-Profession und keine automatische ursprüngliche Upgradefunktion. |

Die beiden Rüstungstiers sind im ursprünglichen Dungeons-Katalog zusammengefasste Varianten; ihre zusätzlichen Registry-IDs erzeugen keine neuen biologischen Arten. Vergleichsquellen für die Erscheinung: [Dungeons-Pillager](https://minecraft.wiki/w/Dungeons%3APillager) und [Dungeons-Vindicator](https://minecraft.wiki/w/Dungeons%3AVindicator), jeweils ausdrücklich sekundäre Taxonomie. Die verwendeten Modelle und Pixel stammen aus dem [gepinnten DungeonsMobs-Port](https://github.com/FirefoxSalesman/DungeonsMobs/tree/cca789ab3ce910853517d65494efd76508255a1d), nicht aus diesen Wiki-Bildern.

## Tropenschleim: wichtige Material- und Animationsdetails

`GoetyTropicalSlimeGeometry` erhält die Quader und UVs von `TropicalSlimeModel.java`. Ein gemeinsamer Pivot am Boden verändert die Ruhegeometrie nicht; er ermöglicht eine zusammenhängende Verformung. Der spezialisierte `TropicalSlimeRenderer` zeichnet Innenkörper und Fische mit Cutout sowie den äußeren Wasserkörper transparent. Alle drei Teile besitzen dieselbe vollständige Gelenkhierarchie und kopieren dieselbe Pose.

Die **47 Fisch-PNG sind Animationsframes**, keine 47 Farbvarianten oder zusätzliche Mobs. Das zeigt die tatsächliche Kombination aus `TropicalSlimeTextures` und `TropicalSlimeServant.tick()`: Die Tabelle enthält die Dateien 1 bis 47, der Zähler durchläuft 0 bis 47; Slot 47 fällt auf Frame 1 zurück, gefolgt von erneut Frame 1 bei Slot 0. Der eigene Renderer erhält diesen 48-Tick-Ablauf. Die unbenutzte Quelldatei 48 sowie Goetys abweichende Diener- und Geheimhaut wurden nicht ausgewählt. Die Analyse korrigiert den früher zu ungenauen Begriff „Palette“ im Quellenkommentar.

Die eigentlichen Sprung-/Squashclips sind eigene Animationen auf dem zugeschriebenen Rig. Sprungfreigabe und Nahkampfrelease beziehen sich auf die bestehenden serverseitigen Zeitfenster. Eine eigene Todesverformung ersetzt keinen vollständigen ursprünglichen Lebenszyklus.

## Befreiung des Schmieds

Der Zivilist beginnt unbefreit und wartet. Eine gültige Haupt-Hand-Interaktion in Reichweite und Sichtlinie befreit ihn serverseitig, weist den Retter zu und startet eine kurze sichtbare Reaktion. Anschließend folgt er dem Retter; erneuter Rechtsklick dieses Spielers schaltet zwischen Folgen und Warten um. Ein anderer Spieler kann die bestehende Zuordnung nicht übernehmen.

Befreiungszustand, Retter und Warten/Folgen werden gespeichert. Ein unbefreiter Spielstand bleibt nach Laden unbefreit; die einmalige Befreiungsgeste startet durch Laden nicht erneut. Der NPC erhält keine Monster-Ziel-KI. Bei dieser Interaktion entstehen weder Gratisgegenstände noch Handelsangebote oder ungefragte Questbelohnungen.

Das ist eine eigene Sandboxadaption der Gefangenenrolle. Ein körperliches Zauntor wird weiterhin normal vom Spieler geöffnet. Eine vollständige originale Mission, automatische Umwandlung in einen Lagerhändler oder Kartenübernahme ist nicht enthalten. Das optionale [Lagerpaket](DUNGEONS_CAMPS.md) kann eine tatsächlich gebaute Umgebung dafür bereitstellen.

## Tatsächlich geprüfter Stand

Der zentrale Prüfstand vom 2. November 2026 umfasst einen erfolgreichen Build, **30 erforderliche Core-GameTests** und **32 erforderliche Tests im optionalen Lagerlauf** einschließlich der beiden Lagertests. Der abgeschlossene echte Clientlauf `20261102-084331-002` erzeugte 352 Hauptbilder für 131 registrierte Figuren sowie 13 kurze native Sequenzen mit 208 weiteren Frames.

Für diese Ergänzungen wurden **15 Hauptbilder selbst angesehen**: je Ruhe, Bewegung und Aktion beziehungsweise Befreiung/Folgen für die fünf Figuren. Dazu kommen drei persönlich geprüfte Lagerbilder. Der [eigene Sichtungsbericht](../reports/v04-new-content-visual-qa.json) nennt jedes tatsächlich geöffnete Bild, SHA256, Zustand und Befund. Die [unveränderten finalen Modellbilder](../reports/v04-new-content-native/full-run/20261102-084331-002/) und die [drei Lagerbilder](../reports/v04-new-content-native/camps/20261102-084241-374/) sind separat erhalten. Die übrigen Aufnahmen und vollständigen Sequenzen werden in diesem Bericht nicht als persönlich angesehen ausgegeben.

Zwei konkrete Fehler wurden dabei behoben und erneut im Client kontrolliert: Der Dreizack nutzt während der Ausholphase nun den tatsächlichen synchronisierten Item-Use-Zustand und zeigt die Spitzen korrekt vor der angehobenen Hand. Der Aufnahmehelfer schaltet beim befreiten Schmied die KI wirklich ein; dieselbe gespeicherte Entity folgt ihrem Retter. Transparenter Tropenschleim samt Fischkern, beide Diamantrüstungen und die getrennte Seegras-Außenhaut blieben in den geprüften Bildern zusammenhängend. Es bestehen keine offenen blockierenden Darstellungsbefunde innerhalb dieser 18 abschließend geprüften Bilder.

Die Prüfung ersetzt keine vollständige Abnahme sämtlicher Animationsframes oder ursprünglicher Kämpfe. Die Lageraufnahmen zeigen ausdrücklich platzierte eigene Vorlagen in einer markierten Entwicklungswelt; natürliche Platzierung über mehrere Seeds und die langfristige Spielbalance sind damit nicht geprüft. Die früheren fehlgeschlagenen Aufnahmeläufe bleiben als Diagnose dokumentiert und werden nicht als vollständiger Lauf gewertet.

## Bearbeiten und reproduzieren

- [v04-source-model-additions.json](../design/v04-source-model-additions.json): Revisionen, Quellenpfade, SHA256, 54 ausgewählte Snapshots und 50 unveränderte Texturdateien.
- [v04-new-model-binding-delta.json](../reports/v04-new-model-binding-delta.json): fünf konkrete Bindings, Spezialrenderer-/NPC-Adapter und vorgeschlagene Prüfansichten.
- `tools/prepare_v04_model_assets.py`: stellt die ausgewählten PNG aus den mitgelieferten, geprüften Snapshots wieder her. Netzwerk und externe Research-Checkouts sind dafür nicht nötig.
- `GoetyTropicalSlimeGeometry` und `OriginalTropicalSlimeAnimations`, `OriginalTridentDrownedAnimations`, `OriginalCivilianAnimations`: bewusst gepflegte Java-Quellen; ein zentraler Import darf sie nicht durch eine allgemeine Familiengeometrie ersetzen.
- [THIRD_PARTY_NOTICES.md](../THIRD_PARTY_NOTICES.md): Goety/Port unter MIT, Schmiedkunst unter CC BY-NC-SA 4.0; eigene neue Clips separat MIT.

Die Quellprüfung und unveränderte Texturkopie sind keine grafische Abnahme. Den tatsächlich ausgeführten Build, Serverlauf und die nativen Aufnahmen dokumentiert [VALIDATION.md](VALIDATION.md). Das Imperfect Heart of Ender bleibt eine Filmszenenreferenz; diesem Schritt wurde dafür kein erfundener zusätzlicher Bosskampf hinzugefügt.
