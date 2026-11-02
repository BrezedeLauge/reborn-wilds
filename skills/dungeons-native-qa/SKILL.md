---
name: dungeons-native-qa
description: Prüfe Dungeons-Alive-Darstellung im echten Minecraft-Client, untersuche Aufnahmefehler und sichere nachvollziehbare Bildbelege für Änderungen und Übergaben.
---

# Dungeons Alive: echte Client-Prüfung

Alle Pfade beziehen sich auf den **Dungeons-Alive-Projektroot** mit `AGENTS.md`, `CODEX_START.md` und `gradle.properties`, nicht auf den Skillordner. `docs/VALIDATION.md`, `docs/MANUAL_TESTS.md` und bei Servervoraussetzungen `docs/GAMETESTS.md` lesen. Vorhandene Aufnahmen und neue Testläufe getrennt führen.

## Passende Entwicklungsaufnahme auswählen

Betriebssystem, JDK 21, grafischen Bildschirm und tatsächliche OpenGL-Fähigkeit prüfen. Keine Linux-Pfade aus historischen Berichten auf macOS/Windows übernehmen. Die Helfer arbeiten nur in ausdrücklich markierten Entwicklungskopien; vorhandene Spielstände erhalten.

| Szene | Vorbereitung und Gradle-Lauf |
| --- | --- |
| Figuren | Nach vorhandener GameTest-Welt `python3 tools/prepare_mob_model_showcase.py`; `./gradlew runMobModelShowcase -PshowcaseMobs=<betroffene IDs> -PshowcaseClips=<IDs\|all\|none>` |
| Redstone-Boss | `python3 tools/prepare_boss_showcase.py`; `./gradlew runBossShowcase` |
| Ausrüstung/Konstrukte | `python3 tools/prepare_feature_showcase.py`; `./gradlew runFeatureShowcase` |
| Lager | `python3 tools/prepare_camp_gametest.py`; `./gradlew runCampGameTestServer`; danach `python3 tools/prepare_camp_showcase.py`; `./gradlew runCampShowcase` |

Zuerst die betroffenen IDs/Zustände prüfen. Einen vollständigen Serienlauf nur aus einer konkreten verbleibenden Prüfanforderung ableiten. Browserautomatisierung dient der HTML-Galerie, nicht dem Nachweis nativer Minecraft-Darstellung. Für ergänzende manuelle Fensteraufnahmen die vorhandene native Computer-Use-Screenshot-API nach ihrer aktiven Tool-Dokumentation verwenden; die Projekt-Capture-Helfer mit ihren Manifesten bleiben der erste Weg. Keine Shell-/AppleScript-Umwege zur UI-Steuerung. Ist diese native API nutzbar, deckt sie den ergänzenden Screenshot-Workflow ohne separaten `screenshot`-Skill ab. Eine Aufnahme eines bestehenden PNGs im Bildbetrachter prüft den Screenshot-Weg, nicht einen neuen Minecraft-Lauf.

## Belege beurteilen

- Neben dem Prozessende das zugehörige Manifest auf `complete`, Fehler, richtige Registry-IDs/UUIDs und die benötigten tatsächlichen Client-/Serverzustände prüfen. Gradle-Exit 0 allein beweist keine vollständige Aufnahme.
- Relevante unveränderte Original-PNGs wirklich öffnen. Bewegung und Aktionen über mehrere Zeitpunkte beurteilen; für Transparenz, Handhaltung oder Leuchten passende Kameras und Lichtbedingungen verwenden. Einzelbilder, erzeugte Sequenzen und tatsächlich visuell geprüfte Frames getrennt zählen.
- Gefundene Fehler korrigieren und die betroffene Szene erneut ansehen. Originalbilder erhalten, Hashes/Quelllauf festhalten und GIFs/Übersichten nachvollziehbar daraus ableiten. Gestellte Aufnahmehöfe und manuell platzierte Lager als solche kennzeichnen; sie belegen keine natürliche Weltgenerierung.
- Ein vorhandenes Bild darf als vorhandener Beleg geprüft werden, auch wenn ein neuer Clientlauf blockiert ist. Dabei weder einen neuen Lauf noch die vollständige Modelltreue behaupten. Keine synthetischen Ersatzbilder als Ingame-Belege verwenden.

## Übergabe

Tatsächlich ausgeführten Prüfstand, visuell geprüfte Auswahl, Einschränkungen, Herkunftsinventare und Implementierungsstatus aktualisieren. Bei einem Release `tools/verify_release.py` und `tools/package_release.py` nach ihren vorhandenen Aufrufen verwenden. Fremde Modbinaries, Spielwelten und Caches bleiben draußen. Wiederholte Core-Tests im Camp-Profil nicht als zusätzliche unabhängige Testfälle zählen. Erfolgreiche Rigs/Builds beweisen weder Originaltreue noch einen vollständigen Survival-/Mehrspielertest.
