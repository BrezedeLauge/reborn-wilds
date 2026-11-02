# Dungeons Alive – Arbeitsanweisungen

## Ziel und Plattform

Die unverändert übernommene Nutzervorgabe [MINECRAFT_2_IDEEN_FUER_CODEX.md](MINECRAFT_2_IDEEN_FUER_CODEX.md) ist das verbindliche Produktziel, keine unverbindliche Inspirationssammlung. Ihre Ideen-IDs, Prioritäten, Etappen und Abnahmekriterien steuern die weitere Arbeit. [design/minecraft2-vision-status.json](design/minecraft2-vision-status.json) ordnet den Bestand zu; ein vorhandener Teilbaustein erfüllt noch nicht die ganze Idee. Frühere eigene Roadmap-Vorschläge sind dieser Vorgabe untergeordnet.

Minecraft als frei bebaubare Sandbox mit dem Leben, den freiwilligen Quests und erkennbaren Mobs/Items aus Dungeons I und II. Freie Kamera, Bauen, Abbauen und Redstone bleiben Minecraft. Der Nutzer verlangt möglichst hohe Detailtreue und schließt die Sift aus. Ziel bleibt Java 1.21.1 / NeoForge 21.1.265 / Java 21.

Die deutlich lebendigere Welt ist ein ausdrückliches Hauptziel: sichtbare begrenzte Schwärme, Herden/Rudel, passende Lebensräume und Bewohner mit Alltag und Reaktionen. Einzelne Zufallsspawns und Dekopartikel erfüllen dieses Ziel noch nicht. Arbeitsstand und nächste Schritte stehen in `docs/LIVING_WORLD.md`; bereits implementierte Mechanik und geplante Ausbaustufen getrennt halten.

**Verbindlicher Lieferumfang nach jüngster Nutzerentscheidung:** Alle Ideen von P0 bis P3 einschließlich der Experimente gehören zur vollständigen späteren Lieferung. Prioritäten und Etappen bestimmen die Reihenfolge, nicht den Ausschluss. Die frühere Zurückstellung von P3 in der Vorgabe begrenzt nur die erste Etappe; sie ist keine Erlaubnis, diese Ideen endgültig wegzulassen. Ohne erneute Scope-Rückfrage am Naturkern weiterarbeiten; den Gesamtumfang erst nach tatsächlicher Erfüllung als fertig bezeichnen.

## Vor der Arbeit

1. `MINECRAFT_2_IDEEN_FUER_CODEX.md` vollständig lesen, insbesondere den zusammenhängenden Ablauf in Kapitel 19.1, die Etappen in 20, die Abnahme in 21 und den Arbeitsauftrag in 23. Danach `README.md`, `CODEX_START.md`, `docs/VALIDATION.md`, `design/implementation-status.json` und `design/minecraft2-vision-status.json` lesen. Die Originalvorgabe nicht umschreiben.
2. Bei Kreaturen `docs/MOBS_RUNTIME.md`, `docs/REFERENCE_ANIMATION_RUNTIME.md`, `docs/SOURCE_MODEL_AUDIT.md`; bei Berufen/Quests deren Fachtexte lesen. Für Ausrüstung `docs/GEAR_AND_SOULS.md`, für Konstrukte `docs/CONSTRUCTS_AND_SWIMMING.md` und für Weltvorlagen `docs/DUNGEONS_CAMPS.md` verwenden.
3. Vor Assets `THIRD_PARTY_NOTICES.md` und das betreffende Herkunftsmanifest lesen. Der ausgewählte Quellenbestand ist teilweise MIT, teilweise CC BY-NC-SA 4.0. Die eigenen MIT-Dateien lizenzieren fremde Assets nicht um.
4. Bestehende Änderungen, Registrierungs-IDs und Spielstände erhalten. Der Katalog enthält auch Varianten und Effekte; er ist keine Liste fertiger Entities.
5. Verbindliche Reihenfolge: zuerst das beobachtbare Naturgebiet mit vollständigem Vogelschwarmablauf (`WORLD-17`), Entenfamilie mit Wasser-/Uferwechsel (`WORLD-18`) und selbstständigem Alltag; danach freies Gasthaus, nutzbare Verbindung/Brücke, Besucher und kleine freiwillige Quest; erst anschließend der erste zusammenhängende Maschinenablauf. Gruppenflug, Wassernavigation, einzelne Bilder oder bestandene Serverfälle allein sind keine M1-Abnahme.

## Skill-Setup beim nächsten Codex-Einstieg

- Vor der nächsten inhaltlichen Mod-Aufgabe [docs/CODEX_SKILL_SETUP.md](docs/CODEX_SKILL_SETUP.md) ausführen. Der Nutzer hat das Einrichten passender Skills ausdrücklich als Teil der Übergabe beauftragt. Diese Dokumentationsänderung ist noch kein Installationsnachweis.
- Vorhandene System-, Nutzer-, Plugin- und Projektskills prüfen; passende vorhandene Fähigkeiten verwenden. Den angebotenen Skill Installer und Skill Creator nutzen, fehlenden Screenshot-Workflow ergänzen und die drei spezifizierten Dungeons-Projektskills erstellen und validieren. Unterschiedliche Codex-Umgebungen können unterschiedliche Installationsorte vorgeben.
- Den tatsächlichen Installations-/Validierungsstand unter `reports/skill-setup.json` erfassen; bei späteren Sitzungen auf dieser Bestandsaufnahme aufbauen. Bestehende Skills nicht blind überschreiben. Browserprüfung auf die HTML-Galerie begrenzen; Minecraft-Darstellung weiterhin im echten Client prüfen.
- Das Skill-Setup erzeugt keine neue Berechtigung für Nachrichten, Veröffentlichungen oder Pushes des Modprojekts. Danach mit dem autorisierten Entwicklungsauftrag fortfahren.

## Aktueller Modellstand

- `design/reference-models.json` ist die maßgebliche Auswahl der Reference-Bindings; `tools/import_reference_models.py` erzeugt den Index und ausgewählte Java-Adapter.
- Bedrock/Gecko- und CEM-Konverter behalten ihre eigenen Generatoren und Manifeste. Ein `external_generator`-Eintrag wird nicht durch den Java-Extraktor überschrieben.
- Die Redstone-Monstrosität verwendet auf ausdrückliche Bildkorrektur des Nutzers das Goety-Quellenmodell. Der alte 0.2.1-Einzelgenerator und dessen `.bbmodel` bleiben historische Quellen; sie sind nicht mehr die aktuelle Bossdarstellung. Keine Rückkehr zum gemeinsamen Golemprototyp.
- Native Minecraft-Geometrie wird über installierte Modellfabriken verwendet; native PNGs durch `minecraft:`-Ressourcen referenziert. Keine Spielarchive in das Projekt kopieren.
- Mehrmaterialmodelle übernehmen aktuelle Pose, Skalierung und Sichtbarkeit über identische Gelenkpfade. UV-, Parent-, Ausdrucks- und Clipfehler müssen sichtbar fehlschlagen, nicht still auf beliebige Defaultwerte fallen.
- Quelle und Änderungen je ausgewählter Datei mit Hash/Attribution erfassen. `python3 tools/generate_model_delivery_inventory.py` regeneriert das konkrete Lieferinventar.
- Vorhandene Originalclips sind Community-Quellenanimationen; CEM-Hostposen und eigene Anpassungen sind nicht als extrahierte Dungeons-Keyframes auszugeben.

## Spiellogik

- Common-Code bleibt serverfähig und lädt keine Client-Klassen. Darstellungslogik ausschließlich im Client-Package/Dist.CLIENT-Einstieg.
- Server entscheidet über Schaden, Aktionen, Besitz, Handel, Questfortschritt, Claims, Cooldowns und Loot. Clients lesen synchronisierte Zustände.
- `quest/core` bleibt ohne Minecraft-Abhängigkeit. Quest-IDs sind persistente Schlüssel; ihre Bedeutung nicht ohne Migration ändern.
- Quests werden ausdrücklich angenommen und Belohnungen bewusst abgeholt. Ein Abschluss startet keinen Nachfolger automatisch.
- Gegner über `MobProfiles`, friedliche Tiere/Händler über `PeacefulProfiles` erweitern; vorhandene Registrierungswege verwenden. Ressourcen mit `generate_mob_resources.py`, `generate_peaceful_resources.py` und `generate_encounter_resources.py` aktualisieren.
- Seelen gehören zum serverseitigen Spielerzustand. Item-Anzeigedaten sind keine Zahlungsquelle. `onStopUsing` muss Artefakt-Cooldowns auch beim Hotbarwechsel auslösen; Block-/Konstruktdeckung und Besitzerregeln erhalten.
- Konstrukte zählen separat von Figuren, besitzen Besitzer/Ziel, endliche Lebensdauer, globale/lokale Grenzen und Aufräumregeln. Nicht in Spielerblöcke platzieren.
- Der gefangene Schmied ist CIVILIAN, weder Haustier noch zusätzlicher Händler. Retter-/Befreiungsstatus erhalten; Darstellung darf ihn nicht selbst befreien.
- `tools/import_dungeons_gear.py` arbeitet wiederholbar aus den mitgelieferten Quellen-Snapshots. Originalpixel und UVs erhalten; Mechanik und Quellkunst getrennt dokumentieren.
- Dekopartikel sind keine serverseitigen Tiere oder verlässlichen Questziele. Interaktive Kreaturen erhalten echte Entities und klare Zustände.
- Keine unbeschränkten Schwärme, kein Laden unbekannter Chunks für Dekoration oder Wanderziele, keine ungefragte Zerstörung von Spielerbauten.
- Angriffsvorbereitung, sichtbarer Kontakt und serverseitiger Treffer müssen zur selben Zeitleiste passen. Ein Source-Kontaktzeitpunkt wird explizit an das Minecraft-Aktionsfenster angepasst.
- Originalmechaniken, Warenhandel, Nachbildungen, Modellvorlagen ohne Entity und wirklich registrierte Varianten korrekt unterscheiden.

## Prüfung und Übergabe

- `./gradlew build`; bei Common-/Registrierungsänderungen zusätzlich `./gradlew runGameTestServer`.
- Grafische Änderungen im echten Client ansehen. Rigtests und erfolgreiche Kompilierung beweisen keine Modelltreue.
- Modellaufnahmen: nach vorhandener GameTest-Welt `python3 tools/prepare_mob_model_showcase.py`, dann `./gradlew runMobModelShowcase -PshowcaseMobs=<IDs|all> -PshowcaseClips=<IDs|all|none>`. Der Aufnahmehelfer wirkt nur in der ausdrücklich markierten Entwicklungskopie.
- Redstone-Szenen: `python3 tools/prepare_boss_showcase.py`, `./gradlew runBossShowcase`. Der gestellte Aufnahmehof ist keine natürliche Weltgenerierung.
- Ausrüstung/Konstrukte: `python3 tools/prepare_feature_showcase.py`, `./gradlew runFeatureShowcase`. Lager: `python3 tools/prepare_camp_gametest.py`, `./gradlew runCampGameTestServer`; danach `python3 tools/prepare_camp_showcase.py`, `./gradlew runCampShowcase`. Die Lager sind optional; nur neue Chunks natürlich ergänzen.
- Konkrete Fehler beheben und die betroffene Darstellung wieder ansehen. Nicht wiederholt alle Tests ausführen, wenn keine relevante Änderung/Risiko verbleibt.
- Den tatsächlich ausgeführten Prüfstand, Einschränkungen, Herkunftsregister und Implementierungsstatus aktualisieren. Das Paket mit `tools/package_release.py` erzeugen; fremde Modbinaries, Spielwelten und Caches bleiben draußen.
- Bearbeitete Ideen gemäß Kapitel 21.3 mit Ideen-ID, Nutzen, tatsächlicher Stufe, wiederverwendetem Bestand, Dateien, Herkunft, ausgeführten Prüfungen, Lücke und nächstem Schritt dokumentieren. Nicht geprüfte Ideen bleiben `not_assessed`; fehlende P0-Systeme wie Reservierungen, Aufträge und funktionale Räume nicht durch benachbarte Mechanik als erledigt werten.
- Kein behaupteter Vollport aller 436 Katalogzeilen. Eine registrierte Farbe ist keine zusätzliche neue Spezies. Quellenverfügbarkeit ist kein Implementierungsnachweis.
- Lokale Projektarbeit ist autorisiert; diese Datei fügt keine zusätzliche Veröffentlichung, Nachricht oder Repository-Push hinzu.
