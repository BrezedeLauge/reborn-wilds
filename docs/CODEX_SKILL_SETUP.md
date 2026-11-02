# Codex: Skills für Dungeons Alive einrichten

**Installations- und Erstellungsauftrag für die nächste Codex-Sitzung.** Ergänzung vom 2. November 2026 für den vorhandenen Modstand 0.4.0-alpha. Die Quellen wurden geprüft; auf dem Rechner des Nutzers wurde durch dieses Dokument noch nichts installiert. Maßgeblich bleiben [AGENTS.md](../AGENTS.md) und [CODEX_START.md](../CODEX_START.md).

**Inzwischen ausgeführter Stand:** [reports/skill-setup.json](../reports/skill-setup.json) erfasst die aktuelle Umgebung. Die drei Projektskills sind unter `skills/<name>/` erstellt und validiert; eine native Screenshot-Entsprechung ist geprüft. Automatische Installation bleibt wegen schreibgeschützter Erkennungsorte blockiert. Bei Fortsetzung auf diesen Quellen und den konkreten Validierungsnachweisen aufbauen. Der folgende Plan bleibt die Spezifikation, nicht die Behauptung eines erneuten Installationsbedarfs für bereits abgedeckte Fähigkeiten.

## 1. Auftrag ausführen

Richte die unten ausgewählten Skills zu Beginn der nächsten Mod-Arbeit ein. Die Nutzeranweisung autorisiert diese passende Einrichtung; frage nicht erneut pauschal, ob Skills installiert werden sollen. Verwende die tatsächlich verfügbaren Installations- und Erstellungsfunktionen deiner Zielumgebung. Eine konkrete fehlende Berechtigung oder ein fehlendes Werkzeug dokumentieren und mit den davon unabhängigen Arbeiten fortfahren.

Ermittle zuerst, welche System-, Nutzer-, Plugin- und Projektskills schon vorhanden sind. Verwende vorhandene passende Skills und vermeide gleichnamige zweite Kopien. Eine Installer-Liste, die nur ihren eigenen Zielordner prüft, ist kein Beweis dafür, dass ein Systemskill fehlt. Bestehende eigene Änderungen an Skills erhalten.

Nutze den angebotenen **Skill Installer** und **Skill Creator**. In Codex CLI/IDE werden sie üblicherweise als `$skill-installer` und `$skill-creator` angesprochen; ihre tatsächlichen Anweisungen und Pfade aus der aktiven Sitzung lesen. Sie werden in der offiziellen Dokumentation als Systemskills geführt. Ein vorhandener `openai-docs`-Skill hilft bei Änderungen am Codex-Setup; Minecraft-APIs weiterhin anhand der passenden NeoForge-/Minecraft-Quellen und der Projektabhängigkeiten prüfen.

## 2. Kleine Auswahl vorhandener Skills

| Priorität | Skill | Konkrete Verwendung und Installationsentscheidung |
| --- | --- | --- |
| Einrichten, falls noch nicht abgedeckt | **`screenshot`** | Native Fenster beziehungsweise Bildschirmbereiche auf macOS, Windows oder Linux aufnehmen. Für manuelle Minecraft-Sichtprüfungen und schwer erreichbare Fensterzustände. Die bestehenden gezielten Minecraft-Aufnahmehelfer des Projekts zuerst verwenden; deren Manifeste liefern mehr Kontext. |
| Bei Arbeiten an der Galerie | **`playwright`** | Suche, Filter, Ansichtswechsel und Darstellung von `reports/native-gallery.html` im Browser prüfen. Erst bei einer entsprechenden Aufgabe installieren, wenn keine passende Browserfähigkeit vorhanden ist. Dieser Skill steuert kein natives Minecraft-Fenster. |
| Bei GitHub-Actions-Aufgaben | **`gh-fix-ci`** | Fehlgeschlagene PR-Prüfungen und Actions-Logs untersuchen, sobald das Projekt tatsächlich mit GitHub und Actions arbeitet. Ohne solche Aufgaben ist die lokale Gradle-/GameTest-Prüfung ausreichend. |

**Quellenstatus:** Der frühere offizielle Katalog `openai/skills` ist inzwischen als veraltet markiert und verweist auf `openai/plugins`. Prüfe daher zuerst vorhandene Fähigkeiten und aktuelle offizielle Angebote. Installiere eine gepflegte Entsprechung nur nach Prüfung ihres konkreten Inhalts und ihrer benötigten Werkzeuge. Eine bloß ähnlich benannte Erweiterung ist kein verifizierter Ersatz.

Als reproduzierbare Ausweichquelle wurden diese weiterhin erreichbaren Ordner geprüft:

| Name | Repository | Geprüfte Revision | Vollständiger Skill-Ordner |
| --- | --- | --- | --- |
| `screenshot` | `openai/skills` | `49f948faa9258a0c61caceaf225e179651397431` | `skills/.curated/screenshot` |
| `playwright` | `openai/skills` | dieselbe Revision | `skills/.curated/playwright` |
| `gh-fix-ci` | `openai/skills` | dieselbe Revision | `skills/.curated/gh-fix-ci` |

Verwende bei dieser Quelle die geprüfte Revision und installiere den **vollständigen Ordner mit Helfern und Lizenz**, nicht nur die einzelne `SKILL.md`. Der geprüfte Installer unterstützt `--repo`, `--path` und `--ref`; ermittle seinen tatsächlichen Skriptpfad aus dem in deiner Sitzung angebotenen Skill. Keine Pfade aus der früheren ChatGPT-Ausführungsumgebung übernehmen. Prüfe nach dem Installieren die Sichtbarkeit im Skill-Verzeichnis beziehungsweise aktualisiere dessen Ansicht.

Die [maschinenlesbare Auswahl](../design/codex-skill-plan.json) enthält die Quellen-URLs und SHA-256-Werte der geprüften Skillbeschreibungen. Diese Werte belegen den untersuchten Quellenstand, keine ausgeführte Installation und keine Prüfung sämtlicher Helferskripte. Bei einer neueren Quelle deren Inhalt erneut prüfen und die tatsächlich gewählte Revision dokumentieren.

## 3. Drei passende Projektskills erstellen

Diese drei Namen bezeichnen **neu zu erstellende Projektskills**. Sie sind keine Behauptung, dass bereits gleichnamige öffentliche Pakete existieren. Erstelle sie mit dem verfügbaren Skill Creator aus den vorhandenen Projektdateien, validiere sie und speichere sie entsprechend der Skillverwaltung der Zielumgebung dauerhaft.

Für gewöhnliche lokale Codex-Projekte dokumentiert OpenAI `.agents/skills/<name>/SKILL.md` als Projektort. Falls die konkrete Umgebung eine verwaltete persönliche Skillablage vorgibt, verwende deren verbindlichen Ablauf. Prüfe jeweils die tatsächliche Erkennung; ein Ordner in einem beliebigen temporären Verzeichnis genügt nicht. Bei projektlokalen Skills den bestehenden Paketierer später so ergänzen, dass ausschließlich die beabsichtigten Skillordner mitgeliefert werden; er nimmt `.agents/skills` bisher nicht automatisch auf.

### `dungeons-neoforge-runtime`

**Aktivieren bei:** Änderungen an Entities, Gegneraktionen, KI, Navigation, Konstrukten, Items, Quests, Besitz, Speicherung, Handel, Registrierungen und Client-/Serversynchronisation dieses Mods.

**Verbindlicher Ablauf:**

1. Projektversion und betroffene Registrierung beziehungsweise Zustandsmaschine aus dem vorhandenen Code lesen; Java 21 / Minecraft 1.21.1 / NeoForge 21.1.265 beibehalten, solange kein Versionswechsel beauftragt ist.
2. Serverzustand, Clientdarstellung und Persistenz getrennt verfolgen. Registry- und Quest-IDs erhalten; den reinen Questkern ohne Minecraft-Abhängigkeiten lassen.
3. Vorhandene Generatoren und Mechaniken erweitern. Bei Änderungen an Besitz, Schaden, Seelen und Abklingzeiten die bestehenden Serverprüfungen erhalten.
4. Bei einem Fehler Ursache und beobachtbare Auswirkung verbinden, dann eine gezielte Korrektur und eine sinnvolle Regression prüfen. Für reine Textänderungen keine Spieltests erfinden.
5. Passende JUnit-/GameTests und den Build ausführen, soweit die Änderung dies erfordert. Den tatsächlichen Prüfstand dokumentieren; bei Common-/Registrierungsänderungen den echten Serverlauf einbeziehen.

**Referenzen im Projekt:** [Architektur](ARCHITECTURE.md), [Mob-Laufzeit](MOBS_RUNTIME.md), [Quests](QUEST_FORMAT.md), [Ausrüstung/Seelen](GEAR_AND_SOULS.md), [Konstrukte/Schwimmen](CONSTRUCTS_AND_SWIMMING.md) und [GameTests](GAMETESTS.md). Die jeweils passenden Texte referenzieren, nicht alle in den Skill kopieren.

**Kleine Validierungsaufgabe:** An einem vorhandenen befreibaren NPC nachvollziehen, wo Retter, Folgen/Warten und Speicherung entschieden werden; die passenden vorhandenen Tests benennen. Erst bei tatsächlicher Codeänderung neue Laufzeitergebnisse behaupten.

### `dungeons-models-animation`

**Aktivieren bei:** Quellenvergleich, Modellimport, Texturen, UVs, Gelenkhierarchie, Materialien, Handhaltung, Clips, Zustandsübergängen und visueller Detailtreue der Dungeons-Figuren.

**Verbindlicher Ablauf:**

1. Quellenmodell, genaue Revision, Lizenz und Attribution aus den vorhandenen Manifesten bestimmen. Die vom Nutzer vorgegebene Goety-Redstone-Monstrosität erhalten; Sift bleibt ausgeschlossen.
2. Den passenden bestehenden Java-, Bedrock/Gecko- oder CEM-Importweg verwenden. Automatisch erzeugte Dateien über ihren Generator ändern. Eine Blockbench-Bearbeitung nur mit tatsächlich vorhandener kompatibler Datei und geeigneter Exportstrecke planen.
3. Proportionen, UV-Größe, Drehpunkte, Gelenkpfade, Handpfade, transparente Flächen und Leuchtmasken prüfen. Sichtbaren Kontakt zeitlich mit der tatsächlichen Serveraktion verbinden.
4. Quellbestandteile, eigene Ergänzungen und fehlende Originalreferenzen unterscheiden. Bildgenerierung kann eigene Konzeptgrafiken unterstützen; sie liefert keinen Nachweis für ein korrektes Rig oder einen echten Ingame-Zustand.
5. Lieferinventar und Herkunftsangaben aktualisieren. Bei sichtbaren Änderungen an `dungeons-native-qa` übergeben und die betreffenden Modelle im Client ansehen.

**Referenzen im Projekt:** [Quellenprüfung](SOURCE_MODEL_AUDIT.md), [Animationslaufzeit](REFERENCE_ANIMATION_RUNTIME.md), [Animationsausbau](ANIMATION_PRODUCTION.md), [visuelle Korrekturen](V04_VISUAL_POLISH.md), [Referenzbindungen](../design/reference-models.json) und [Lizenzen/Credits](../THIRD_PARTY_NOTICES.md).

**Kleine Validierungsaufgabe:** Für ein bestehendes Modell den aktiven Binding-Eintrag, seine Textur, den zuständigen Generator und die Quellenlizenz auffinden. Einen alten historischen Generator korrekt vom aktuellen Modell unterscheiden.

### `dungeons-native-qa`

**Aktivieren bei:** Grafischen Änderungen, Animationen, Transparenzfehlern, Handhaltung, Ingame-Nachweisen, Capture-Fehlern und der Vorbereitung einer Mod-Übergabe.

**Verbindlicher Ablauf:**

1. Die relevante markierte Entwicklungswelt und den vorhandenen Aufnahmehelfer auswählen. Betriebssystem, JDK, grafischen Bildschirm und tatsächliche OpenGL-Fähigkeit prüfen; die im alten Bericht verwendeten Linux-Pfade nicht auf einen Mac oder Windows-PC übertragen.
2. Modell-, Boss-, Feature- oder Camp-Aufgabe gezielt ausführen. Für Modelländerungen zuerst betroffene IDs auswählen; einen vollständigen Serienlauf nur für eine konkrete verbleibende Prüfanforderung starten.
3. Neben dem Prozessende das zugehörige Capture-Manifest auf Vollständigkeit, Fehler, richtige Entity-IDs und die benötigten Client-/Serverzustände prüfen.
4. Die relevanten Originalbilder tatsächlich öffnen und über mehrere Zeitpunkte beurteilen. Standbilder, produzierte Sequenzen und visuell geprüfte Frames korrekt unterscheiden.
5. Fehler korrigieren und die betroffene Szene erneut prüfen. Aufnahmebedingungen dokumentieren, Original-PNGs erhalten und Ableitungen wie GIFs auf diese zurückführen. Echte Entwicklungsaufnahmen als gestellt kennzeichnen.
6. Für die Übergabe erforderliche Prüfberichte und Inventare aktualisieren; Paketprüfung und Paketierer nutzen. Frühere erfolgreiche Tests als historischen Nachweis führen, nicht als gerade erneut ausgeführten Test.

**Referenzen im Projekt:** [Prüfbericht](VALIDATION.md), [manuelle Prüfungen](MANUAL_TESTS.md), [GameTests](GAMETESTS.md), [prepare_mob_model_showcase.py](../tools/prepare_mob_model_showcase.py), [prepare_feature_showcase.py](../tools/prepare_feature_showcase.py), [prepare_camp_showcase.py](../tools/prepare_camp_showcase.py), [verify_release.py](../tools/verify_release.py) und [package_release.py](../tools/package_release.py).

**Kleine Validierungsaufgabe:** Ein vorhandenes Capture-Manifest lesen, eine dazugehörige Originalaufnahme öffnen und die Grenzen der Aussage nennen. Das als Prüfung eines vorhandenen Belegs melden, nicht als neu gestarteten Minecraft-Lauf.

## 4. Einrichtung nachweisen und weiterarbeiten

Validiere die drei erstellten Skills mit dem vorgesehenen Validator ihres Skill Creators. Prüfe danach mit den kleinen Aufgaben, ob Namen, Referenzen, Trigger und Abläufe in dieser Zielumgebung funktionieren. Vorhandene Regeln des Projekts bleiben maßgeblich; die Skills bündeln deren Anwendung und ersetzen sie nicht.

Schreibe den erreichten Zustand nach `reports/skill-setup.json`: Umgebung, Datum, Skillname, Herkunft und Revision beziehungsweise eigene Projektquelle, tatsächlicher Installationsort, Status (`already_available`, `installed`, `created_and_validated`, `deferred_optional` oder `blocked`), ausgeführte Validierung sowie gegebenenfalls konkreter Blocker. Eine installierte Anleitung ohne nutzbares Aufnahmeprogramm ist noch keine erfolgreich geprüfte Screenshot-Funktion. Für blockierte grafische Prüfungen keine Ersatzbilder als Ingame-Belege ausgeben.

Danach den nächsten in [CODEX_START.md](../CODEX_START.md) beschriebenen Entwicklungsabschnitt bearbeiten und die eingerichteten Skills bei den passenden Aufgaben verwenden. Keine neue Veröffentlichung, Nachricht, kostenpflichtige Generierung oder Freigabe des Modprojekts allein aus diesem Setup-Auftrag ableiten.

## Direkt verwendbarer Startauftrag an Codex

```text
Arbeite an Dungeons Alive aus diesem Projektordner weiter.
Lies zuerst AGENTS.md, CODEX_START.md und docs/CODEX_SKILL_SETUP.md.

Richte die dort ausgewählten passenden Skills ein: Prüfe vorhandene Skills,
verwende deinen Skill Installer und Skill Creator, ergänze bei Bedarf den
Screenshot-Workflow und erstelle die drei spezifizierten Dungeons-Projektskills.
Die passende Installation und Erstellung sind von mir ausdrücklich beauftragt;
frage dafür nicht erneut pauschal nach. Erhalte vorhandene eigene Skills und
halte dich an die tatsächlichen Berechtigungen deiner Umgebung.

Validiere die Einrichtung, dokumentiere ihren echten Status in
reports/skill-setup.json und beginne anschließend mit dem nächsten sinnvollen
Entwicklungsabschnitt aus der Übergabe. Erhalte die Quellenmodelle, den
Minecraft-Sandboxcharakter und den Sift-Ausschluss. Grafische Änderungen mit
echten Minecraft-Aufnahmen prüfen. Berichte konkrete Ergebnisse und offene
Punkte; behaupte keine Installation oder Prüfung, die du nicht ausgeführt hast.
```

## Geprüfte Primärquellen

- [OpenAI: Build skills](https://learn.chatgpt.com/docs/build-skills) – Systemskills, Erstellen, lokale Erkennung und Installation.
- [OpenAI Skills: Status und Verweis auf den neuen Katalog](https://github.com/openai/skills/blob/49f948faa9258a0c61caceaf225e179651397431/README.md).
- [Aktueller offizieller Plugin-Katalog](https://github.com/openai/plugins) – vor einer neuen Installation auf passende aktuelle Angebote prüfen.
- [Screenshot: geprüfter Skill-Ordner](https://github.com/openai/skills/tree/49f948faa9258a0c61caceaf225e179651397431/skills/.curated/screenshot).
- [Playwright: geprüfter Skill-Ordner](https://github.com/openai/skills/tree/49f948faa9258a0c61caceaf225e179651397431/skills/.curated/playwright).
- [GitHub Fix CI: geprüfter Skill-Ordner](https://github.com/openai/skills/tree/49f948faa9258a0c61caceaf225e179651397431/skills/.curated/gh-fix-ci).
