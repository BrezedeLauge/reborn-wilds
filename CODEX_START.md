# Codex-Übergabe – Reborn Wilds 0.4.0

**Öffentlicher Projektname: Reborn Wilds.** Die internen IDs, Java-Packages, gespeicherten Schlüssel und geschützten QA-Profilnamen bleiben `dungeonsalive` beziehungsweise ihre bisherigen Werte. Aktueller öffentlicher Prüfstand: [docs/VALIDATION.md](docs/VALIDATION.md). Die folgenden Einträge sind lokale historische Arbeitsnachweise; `reports/` und `build/` gehören nicht zum öffentlichen Quellpaket.

## Verbindliches Ziel und nächster zusammenhängender Schritt

Zuerst [MINECRAFT_2_IDEEN_FUER_CODEX.md](MINECRAFT_2_IDEEN_FUER_CODEX.md) vollständig lesen. Diese unveränderte Nutzervorgabe ist verbindlich; insbesondere Kapitel 19.1, 20, 21 und 23 bestimmen Reihenfolge und Abnahme. [design/minecraft2-vision-status.json](design/minecraft2-vision-status.json) enthält alle 223 Ideen mit Originalpriorität, Bestandsbelegen und klaren Lücken. Nicht bewertete Ideen sind ausdrücklich `not_assessed`.

**M1 wird über beobachtbare Natur abgenommen:** `WORLD-17` besitzt jetzt tatsächlichen Vogelanflug, Kollisionskontakt, Ruhe, Sitzwechsel, versetzten Abflug und Alarmreaktion; `WORLD-18` eine natürliche Entenfamilie mit Jungtieren, gemeinsamem Schwimmen und sicheren Uferwechseln zum Fressen, Putzen und Ruhen. Die echten Serverregressionen sind bestanden. **Die grafische Abnahme der vollständigen Abläufe ist noch offen**; einzelne Landschafts-/UI-Bilder ersetzen diese Beobachtung nicht. Zuerst den aktuellen M1-Prüfstand und die gezielte Capture-Korrektur unten beachten, dann den zusammenhängenden weiteren M1-Schritt fortsetzen.

Danach freie Gasthausfunktion, nutzbare Brücke/Alternative, Besucher und kleine freiwillige Quest durchgängig verbinden; erst danach M2 mit Kurbel, Welle, Winde, Wasserantrieb, Redstone und Erklärung. Persistente Aufträge, Materialreservierungen, allgemeine Raumfunktionsprüfung und kooperative Auftragsanerkennung fehlen noch. Vorhandene Registrierung, Händlerzahlung oder Siedlungsanker ersetzen sie nicht. Früher vorgeschlagene eigene Ausbaurichtungen sind diesem Ziel untergeordnet.

**Verbindlicher Lieferumfang nach jüngster Nutzerentscheidung:** Alle Ideen von P0 bis P3 einschließlich der Experimente gehören zur vollständigen späteren Lieferung. Prioritäten und Etappen bestimmen die Reihenfolge, nicht den Ausschluss. Die frühere Zurückstellung von P3 in der Vorgabe begrenzt nur die erste Etappe; sie ist keine Erlaubnis, diese Ideen endgültig wegzulassen. Ohne erneute Scope-Rückfrage am Naturkern weiterarbeiten; den Gesamtumfang erst nach tatsächlicher Erfüllung als fertig bezeichnen.

## Passende Skills und vorhandene Prüfgrundlage

**Aktueller Einrichtungsstand:** [reports/skill-setup.json](reports/skill-setup.json) und [reports/project-skills-validation.json](reports/project-skills-validation.json) erfassen die geprüfte Sitzung. Die drei Skills liegen bereits unter `skills/<name>/` und sind validiert. Die vorhandene native Computer-Use-Funktion deckt Fenster-Screenshots ab. `.agents`/`.codex` und persönliche Skillablagen sind hier schreibgeschützt; eine automatische Installation ist deshalb noch nicht nachgewiesen. Vorhandene Quellen erhalten und bei Bedarf in dieser Sitzung ausdrücklich lesen. Sobald ein zulässiger Erkennungsort verfügbar ist, diese geprüften Ordner installieren und die Erkennung nachweisen; nicht neu erfinden oder bestehende Skills überschreiben.

**Aktueller gemeinsamer Inn-/QA-Snapshot:** SHA-256 `2566768d4c8e913c19c52bb08505caf995b931d251e082f869fad486ab45f2db`, **457 Haupt-/40 Testquellen, 170/170 JUnit und 138/138 echte NeoForge-GameTests**. [Dauerhafter Prüfbericht](reports/inn-integrated-validation.json). Alle neun neuen Gasthausfälle und die bisherigen Regressionen bestehen; der Bericht hält auch die vorherigen konkreten Fehler und Korrekturen fest. Native-, Nature- und Inn-QA sind mit frischen eigenen Welten und genau dieser JAR vorbereitet. **Der nächste Nachweis ist die tatsächliche Sichtung der drei Aufnahmeläufe**, keine erneute unveränderte Testwiederholung. Die b156-Vorgänger liegen verlustlos unter `build/native-qa-archive/qa-b156-20261102T195134Z/` ([Index](reports/m1-qab156-archive.json)). Nutzerprofile bleiben unverändert; kein neuer Gradle-/Releaseclaim.

**Vorheriger gezielter QA-Korrekturstand:** SHA-256 `b156bfc246f56c0c0529278285bade2a2c8290cb11f4c1cf307f27de154eb8c0`, **426 Haupt-/35 Testquellen, 148/148 JUnit und 129/129 native GameTests**. [Artefaktbezogener Nachweis](reports/m1-capture-readiness-validation.json). Enthalten sind Clientchunk-/Meshbereitschaft, Kamera-Sichtlinien für gestellte Bauwerke, näherer fester Teichblick, konsistente Kamera-/Phasensnapshots und die konkreten GUI-Textkorrekturen. Die neue JAR braucht jetzt ihre tatsächliche erneute Sichtprüfung. Die damaligen eigenen QA-Profile sind inzwischen ebenfalls archiviert; die beendeten 55er Originalprofile liegen verlustlos unter `build/native-qa-archive/qa-55-20261102T191819Z/` ([Datei-/Hashindex](reports/m1-qa55-archive.json)). Keine Nutzerspielwelt wurde geändert.

**Vorheriger M1-Snapshot mit Bildern:** **425 Haupt- und 35 Testquellen**, **148/148 JUnit** und **129/129 echte native GameTests** sind bestanden. Zwei unabhängige Serverläufe bestätigten dieselbe JAR nach einem konkreten UUID-/Phasenfehler; die Wiederholung zählt nicht als 258 verschiedene Tests. [Artefaktbezogener M1-Nachweis](reports/m1-nature-validation.json), [archivierter Java-Bericht](reports/m1-nature-java.json), [Serverlauf 1](reports/m1-nature-gametest-1.json), [Serverlauf 2](reports/m1-nature-gametest-2.json). SHA-256: `55f723625b315bda0c76bb746809abb6c612d9fad8b3eb9fffed673448eaff10`. Die getrennte Test-JAR liegt unter `build/local-check/`; der echte Server nutzt das originale gecachte NeoForge-Rezept. Dies ist kein neuer erfolgreicher Gradle-Task oder Gradle-Release. Der frühere 137/123-Snapshot bleibt in [reports/living-expansion-validation.json](reports/living-expansion-validation.json) erhalten.

**Nächster offener Nachweis ist die tatsächliche Sichtprüfung.** Der neue Prism-Lauf auf der M1-JAR erzeugte **12 echte PNGs**, alle Hashes wurden geprüft und der automatische Client-/Serverstopp ist protokolliert. [Manifest](reports/m1-living-world-capture.json), [Log](reports/m1-living-world-capture.log). Erste Landschaftsbilder zeigen jedoch unvollständig gelieferte/gerenderte Terrainquadrate: **keine Terrainfreigabe**. Der nächste gezielte Helferfix muss vorhandene Clientchunks und kompilierte Renderabschnitte prüfen, statt nur Renderframes abzuzählen; danach diese Ansichten erneut aufnehmen. Die eigene Vogel-/Enten-Bildfolge und echte Hörprüfung bleiben getrennte Aufgaben. Die technische Aufnahme ist keine visuelle Zustimmung.

Die getrennte Nature-Aufnahme hat inzwischen **114 echte, hashgeprüfte Bilder** über 1920 Weltticks geliefert: alle drei Entenzyklen sowie der vollständige Zyklus des ausgewählten Fokusvogels, mit unbewegter Kamera je Sequenz und ohne Befehle während der Beobachtung. [Manifest](reports/m1-nature-capture.json), [Log](reports/m1-nature-capture.log). Vier Entenrufe, ein Tukanruf und drei Flügelschläge starteten echte Audiokanäle. **Bildsichtung und Hörprüfung bleiben getrennt offen**; die automatische Zyklus-/Kanalmessung erteilt keine grafische Freigabe.

Das spielbare Profil `run-prism/instances/Dungeons-Alive-Living-World` enthält weiterhin den früheren Teststand `3eacf57e…`; Nutzerprofile nicht überschreiben oder ungefragt beenden. Die getrennten Profile `Dungeons-Alive-Native-QA` und `Dungeons-Alive-Nature-QA` enthalten inzwischen den oben genannten Inn-/QA-Hash `2566768d…` und ausdrücklich frisch erzeugte Testwelten; dasselbe gilt für das neue `Dungeons-Alive-Inn-QA`. Die 55er und b156-Vorgänger sind archiviert. Das erste nimmt Landschaften/UI auf, das zweite echte Tierabläufe mit festen Kamerasequenzen. Die frühere erfolglose Native-QA-Probe `011f3b80…` ist verlustlos unter `build/native-qa-archive/` sowie in [ihrem Bericht](reports/living-world-prism-first-attempt.json) erhalten. Neue Sourceänderungen brauchen einen neuen passenden Prüf-/Staging-Snapshot; bestehende Capture-JARs und Welten bleiben unverändert.

Die frühere Gradle-JAR unter `build/libs/` und die gerade benutzte Prism-Creative-Instanz sind weiterhin der ältere Build mit 97 JUnit-Tests. Die Spielwelt/Instanz des Nutzers nicht während seiner Sitzung austauschen oder beenden. `dist/` bleibt der historische Auslieferungsstand. Gradle scheitert in dieser Sandbox vor der Kompilierung am `FileLockContentionHandler` mit `SocketException: Operation not permitted`. Der direkte, dokumentierte Java-/NeoForge-Weg umgeht keinen Netzwerk- oder Berechtigungsfilter und verändert keine Launcher-Konten.

Werkzeuge: `tools/check_java_local.py`, `tools/run_gametest_local.py`, `tools/prepare_living_showcase.py`, `tools/run_client_local.py`, `tools/stage_native_prism_qa.py` und `tools/stage_nature_prism_qa.py` und `tools/stage_inn_prism_qa.py`. Java: `/Applications/Android Studio.app/Contents/jbr/Contents/Home`; Abhängigkeitscache: `/private/tmp/dungeonsalive-gradle-setup-user/caches/modules-2/files-2.1`. Der direkte Clientversuch bleibt als [macOS-GUI-/XPC-Fehlversuch](reports/living-world-direct-client.json) erhalten. Produktions-Capture verlangt zwei ausdrückliche Flags, den exakten eigenen QA-Pfad, tatsächlichen Weltabgleich und verbraucht eine einmalige Fresh-Markierung. Ein abgewiesener Fremdlauf darf weder verändert noch beendet werden. Natürlich erzeugte Biome, gestellte Bossvorlagen und der gestellte Natur-Testteich bleiben im Sichtbericht getrennt.

Der Nutzer hat ausdrücklich beauftragt, Codex auch zur Installation der für dieses Projekt wichtigen Skills anzuweisen. **Führe deshalb beim nächsten Codex-Einstieg zunächst das [Skill-Setup](docs/CODEX_SKILL_SETUP.md) aus und arbeite anschließend am bestehenden Mod weiter.** Prüfe vorhandene Skills, verwende den angebotenen Skill Installer und Skill Creator und installiere nur fehlende passende Ergänzungen.

Der konkrete Auftrag ist: `screenshot` beziehungsweise eine geprüfte aktuelle Entsprechung ergänzen und die drei in der Anleitung spezifizierten Projektskills **`dungeons-neoforge-runtime`**, **`dungeons-models-animation`** und **`dungeons-native-qa`** erstellen, validieren und in der Zielumgebung verfügbar machen. `playwright` ist für Arbeit an der HTML-Galerie vorgesehen, `gh-fix-ci` erst bei tatsächlichen GitHub-Actions-Aufgaben. Der maschinenlesbare [Setup-Plan](design/codex-skill-plan.json) enthält geprüfte Quellen und den ausdrücklich noch ausstehenden Installationsstatus.

Die Installation dieser passenden Skills ist durch den Nutzerauftrag gedeckt; dafür ist keine erneute allgemeine Bestätigungsrunde nötig. Beachte tatsächliche Berechtigungsgrenzen der Zielumgebung, erhalte vorhandene benutzerdefinierte Skills und dokumentiere den erreichten Zustand. Dieses Übergabepaket selbst behauptet keine Installation auf dem Rechner des Nutzers.

## Nutzerziel und Plattform

Minecraft Java mit den erkennbaren Kreaturen, Bossen, Items, Händlern, Quests und dem Leben von Minecraft Dungeons I und II. Normale Minecraft-Kamera, Bauen, Abbauen, Farmen und Redstone bleiben erhalten. Actions & Stuff ist die Bewegungsreferenz. **Sift-Inhalte sind ausgeschlossen.** Der Nutzer korrigierte die Redstone-Monstrosität ausdrücklich mit einem Goety-Bild; das Goety-basierte Quellenmodell ersetzt den vereinfachten früheren Entwurf.

**Lebendige Welt ist ein Hauptziel:** begrenzte Schwärme, Herden/Rudel, Lebensräume sowie Bewohner mit Alltag und Reaktionen. Der Nutzer hat nach der Bestandsübersicht die Fortsetzung beauftragt. Lagerhändleralltag, Herdenwanderungen, Fluggruppen, Zucht und die ursprüngliche Zielbindung von Gegnerangriffen sind implementiert und besitzen bestandene Serverregressionen. Native Darstellung und längeres Zusammenspiel in einer echten Spielwelt bleiben zu prüfen. [docs/LIVING_WORLD.md](docs/LIVING_WORLD.md) und [aktueller Ausbau](docs/LIVING_WORLD_EXPANSION.md) trennen Umfang und Grenzen.

Beibehalten: **Minecraft 1.21.1 / NeoForge 21.1.265 / Java 21**. Zuerst `README.md`, `AGENTS.md`, `docs/VALIDATION.md` und `design/implementation-status.json` lesen. `./gradlew build`; bei Common-/Registrierungsänderungen auch `./gradlew runGameTestServer`. Grafische Änderungen im tatsächlichen Client prüfen.

## Was wirklich vorhanden ist

- **131 registrierte Figuren: 78 Gegner und 53 friedliche.** Zwölf Bossgrundtypen, neun Minibosse, 57 reguläre Gegner; 35 Begleiter, vier Wildtiere, zwei Schlüsselgolems, elf Händler und ein befreibarer Schmied. Farb-/Rüstungsvarianten sind keine neuen biologischen Spezies.
- **Vier gesonderte zeitlich begrenzte Konstrukte:** Geomancer-Wand, Bombensäule, Ice Chunk und Entangle Vine. Serverbesitz, Begrenzung, Speicherung, Ablauf, Schaden beziehungsweise Deckung und Abbruch sind implementiert. Sie zählen nicht zu den 131 Figuren.
- Für alle registrierten Figuren konkrete Quellen-/Builtin-Bindings. Java-, Bedrock/Gecko- und CEM-Konverter, Quellsnapshots, Hashes, vollständige Lizenzen, getrennte Materialien, Handpfade und Leuchtmasken. Der gemeinsame alte Familienrenderer darf diese Modelle nicht überschreiben.
- **Ausrüstungsquellstand: zwölf Artefakte einschließlich Windhorn, vierzehn Nahkampfwaffen, vier Fernkampfwaffen und zwölf Rüstungsteile in drei Sets.** Die Ergänzungen kompilieren und ihre aktuellen Serverregressionen bestehen; die neue Clientprüfung bleibt offen. 41 importierte Gegenstände haben veröffentlichte Community-Modelle, eigene Servermechanik und Rezepte; Windhorn besteht separat. Sechs Waffen als zusätzliche Bossbeute. Serverseitiger gemeinsamer Seelenvorrat, Kosten, Cooldowns und Deckungsprüfung. Details in `docs/GEAR_AND_SOULS.md`.
- Sechs Villager-Berufe mit 60 Trades, Arbeitsposen für 19 Rollen, 21 Siegel und **29 Grundquests**. Eigener Windhorn-Bestand und dekorative Insekten bleiben erhalten.
- Friedliche Tiere mit Besitzer-/Folgen-/Warten-Regeln; Händler mit echten MerchantOffers und Begrüßung. Captured Blacksmith hat eine eigene gespeicherte Befreiung samt Retter; kein behaupteter zusätzlicher Upgrade-Dienst.
- Zwei eigene optionale Lager unter `examples/datapacks/dungeons_camps`, normale Minecraft-Blockvorlagen mit tatsächlichen NPCs und sparsamer Jigsaw-Platzierung in neuen Chunks. Zusätzlich optionaler Wilds-Spawn-Datapack.

**B** öffnet das Figurenhandbuch, **B → Ausrüstung** die Itemansichten, **J** das Journal. Die Vorschauen erzeugen keine Weltentities und verändern keine Spielstände. Alle Figuren besitzen Spawn-Eier und echte `/summon`-IDs.

## Zuletzt implementiert, Server geprüft und Sichtprüfung offen

Drei serverseitige Schmiededienste (Reparatur, drei Waffenränge, sechs Verzauberungen) mit Vorschau, bezahltem Ergebnis, Rückgabe und Angebotsrevision. Native Verzauberungen Strahlen, Lebensraub, Hingabe, Kritischer Treffer, Ketten und Wirbel bis III. Besitzerbezogene S2C-Anzeige für Seelen und Flammenköcher. Acht zusätzliche Grundwaffen mit eigenen Werten, Reichweite/Rückstoß/Schwungregeln; Seelenmesser sammelt direkt fünf Seelen, ohne Beschwörungsfarm.

Wollkühe bekommen echte Weizen-Balz, begrenzte Geburten, Elternbindung, Speicherung und Wachstum. Sparschweine erscheinen selten, fliehen und geben einmalige echte Beute mit persistenter UUID-Sperre. Zwei selbst gebaute Schlüsselgolem-Tore prüfen Besitzer, Schlüsselart, Entfernung und Sicht; kein Redstone-Bypass. Die Registrierungen und Generatoren sind in `DungeonsAlive` eingebunden. [reports/progression-status.json](reports/progression-status.json) bleibt der frühere Ausbau-Snapshot. Für den aktuellen Quellstand gilt [reports/living-expansion-validation.json](reports/living-expansion-validation.json): Die realen Serverfälle sind bestanden, ihre Darstellung und das längere Survival-/Mehrspieler-Zusammenspiel bleiben offen.

## Maßgebliche Quellen und Werkzeuge

| Aufgabe | Dateien |
| --- | --- |
| Ausgewählte Figurenmodelle | `design/reference-models.json`, `tools/import_reference_models.py` |
| Java-Quellenmodelle | ausgewählte Snapshots in `third_party/goety/` und `third_party/dungeons_mobs_port/` |
| Bedrock/Gecko | `design/bedrock-model-imports.json`, `design/infamous-additions.json`, `tools/convert_bedrock_models.py` |
| CEM / Materialien | `design/cem-model-imports.json`, `design/cem-extra-model-imports.json`, `tools/convert_cem_models.py` |
| 0.4-Figuren | `design/v04-source-model-additions.json`, `tools/prepare_v04_model_assets.py`, `docs/V04_MOB_ADDITIONS.md` |
| Orb und Tentakel | `design/v04-visual-source-audit.json`, `tools/cem_visual_additions.py`, `docs/V04_VISUAL_POLISH.md` |
| Eigene Vengeful-Nachbildung | `tools/generate_original_vengeful.py`, `OriginalVengefulHeart*` |
| Treetop-Palette | `tools/generate_treetop_palette.py`, ausdrücklich als Adaption bezeichnet |
| Native Kompositionen | `client/entity/reference/builtin/`, `design/builtin-models.json` |
| Ausrüstungsquellen | `tools/import_dungeons_gear.py`, `tools/generate_gear_armor_models.py`, `design/gear-source-inventory.json`, `design/gear-armor-models.json`, `third_party/dungeons_gear/` |
| Eigene Spielmechanik | `entity/construct/`, `item/gear/`, `entity/peaceful/` |
| Eigene Lager | `tools/generate_dungeons_camps.py`, `docs/DUNGEONS_CAMPS.md` |
| Gesamtinventar und Bedingungen | `design/model-delivery-inventory.json`, `THIRD_PARTY_NOTICES.md` |

`tools/generate_redstone_monstrosity.py` und die alte `.bbmodel` sind **historischer Stand 0.2.1**. Sie erzeugen nicht den ausgewählten Goety-Boss. Keine Rückkehr zum gemeinsamen Golemprototyp.

## 0.4-Korrekturen erhalten

Arch-Illager: gerichteter Stab und eigene klar gekennzeichnete Orb-Ergänzung. Baby Squid: acht eigene zweigliedrige Tentakel samt sichtbaren UV-Flächen. Drowned: durch echten synchronisierten Schwimmzustand gesteuerte Pose und räumliche Wassernavigation. Der erforderliche Aufwärtswinkel darf nicht wieder durch den Standard-LookControl zurückgesetzt werden. Auf dem Land müssen die normalen Zustände zurückkehren. Die entsprechenden Sichtungsgrenzen stehen in den QA-Berichten, einschließlich der Unterscheidung zwischen Tagesfarbe und Nachtleuchten.

Beacon: der Stop-Callback muss auch bei Hotbarwechsel und Seelenmangel auslösen. Item-Anzeigedaten dürfen nie als Seelenguthaben gelten. Zielauswahl prüft dünne Blockdeckung und Geomancer-Wände; eine vergrößerte Strahltoleranz darf keinen verdeckten Zielkörper freigeben. Passive und besessene Kreaturen sind keine Artefaktziele.

## Wiederholbare Prüfung

```bash
./gradlew build
./gradlew runGameTestServer
python3 tools/prepare_camp_gametest.py
./gradlew runCampGameTestServer
```

Der zweite Serverlauf wiederholt die Kerntests und ergänzt zwei bedingte Lagerprüfungen. Diese wiederholten Kerntests nicht als zusätzliche unabhängige Testfälle zählen. Den tatsächlich ausgeführten Stand aus `docs/VALIDATION.md` übernehmen, keine alten Zähler in neue Berichte kopieren.

Für Aufnahmen ist ein grafischer Desktop beziehungsweise OpenGL-fähiger virtueller Bildschirm erforderlich:

```bash
python3 tools/prepare_mob_model_showcase.py
./gradlew runMobModelShowcase -PshowcaseMobs=all -PshowcaseClips=none
# Gezielte echte Bildfolge:
./gradlew runMobModelShowcase -PshowcaseMobs=arch_illager,pet_baby_squid -PshowcaseClips=all
python3 tools/prepare_mob_model_showcase.py --assemble-clips
python3 tools/prepare_feature_showcase.py
./gradlew runFeatureShowcase
python3 tools/prepare_camp_showcase.py
./gradlew runCampShowcase
```

Die Helfer schreiben ausschließlich in markierte Entwicklungskopien. Sie stellen Prüfwelten und Ziele bewusst auf. Erfolg bedeutet vollständiges Manifest ohne Fehler; Gradles Prozessende allein beweist keine abgeschlossene Aufnahme. Native PNGs, Sequenzen, Galerie und tatsächliche Einzelbewertungen bleiben getrennte Nachweise.

## Nächste zusammenhängende Arbeit

1. **Originalkämpfe vertiefen.** Die vorhandenen 15 Aktionsarten und neuen Konstrukte sind eine funktionierende Minecraft-Adaption. Redstone-Bomben/Arena, Tempest-Säulen, Nameless-Klone und Herz-Phasen müssen jeweils als durchgängiger, begrenzter Ablauf mit Tod, Zielverlust und Speichern ausgearbeitet werden.
2. **Fehlende Figuren einzeln ergänzen.** `design/model-source-audit.json` umfasst 436 Referenzzeilen mit 47 Sift-Ausschlüssen. Die echte Laufzeitzuordnung steht in `implementation-status.json`. Ancient-Varianten und offene Dungeons-II-Inhalte weiter recherchieren. `mage` ist ein Community-Mod-Kandidat; `imperfect_heart_of_ender` eine Szenenform. Beide sind keine zusätzlich behaupteten Originalbosse.
3. **Animationen gezielt verfeinern.** Zusätzliche Originalreferenzen für Treetop-Silhouette, Vengeful-Seitenansichten, individuelle Tode und Übergänge heranziehen. Fighter’s Bindings braucht noch eine eigene beidhändige Faustdarstellung. CEM-Hostbewegungen sind keine extrahierten Dungeons-Keyframes.
4. **Bewohner und Welt vertiefen.** Natürliche Lagerplatzierung an verschiedenen Hängen und Weltsamen prüfen, begrenzte Habitate entwickeln, Schlüssel-Türen und besondere Begleiterfähigkeiten ergänzen. Upgrade-/Geschenkdienste benötigen eigene sichere Inventarabläufe; Warenhandel ersetzt diese nicht.
5. **Ausrüstung erweitern und balancieren.** Weitere konkrete Modelle mit verwendbarer Herkunft auswählen; vorhandene IDs, Seele-Regeln und Questfortschritte erhalten. Waffenbalance in echtem Survival und mit zwei realen Clients prüfen.

Eigener Code und ausgewählte Fremdquellen stehen unter MIT; ReCrafted-Assets und ihre Ableitungen behalten CC BY-NC-SA 4.0. Das komplette Assetpaket ist daher nicht pauschal MIT. Keine Actions-&-Stuff-Archive und keine kopierten Minecraft-Spielarchive erforderlich. Bei weiterer Übergabe das Herkunftsinventar regenerieren, final bauen, `tools/verify_release.py` ausführen und mit `tools/package_release.py` paketieren.
