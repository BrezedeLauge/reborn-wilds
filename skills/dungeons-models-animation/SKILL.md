---
name: dungeons-models-animation
description: Bearbeite Quellenmodelle, Texturen, Rigs und Animationen der Dungeons-Alive-Figuren über die vorhandenen Java-, Bedrock/Gecko- und CEM-Importwege.
---

# Dungeons Alive: Modelle und Animationen

Pfade sind relativ zum **Dungeons-Alive-Projektroot** mit `AGENTS.md`, `CODEX_START.md` und `gradle.properties`, nicht zur Skillinstallation. Projektvorgaben sowie `docs/SOURCE_MODEL_AUDIT.md`, `docs/REFERENCE_ANIMATION_RUNTIME.md` und die betroffenen Herkunftsmanifeste vor Änderungen lesen.

## Quelle und Importweg bestimmen

- Das aktive Binding aus `design/reference-models.json` mit Registrierung und Lieferinventar abgleichen. Quellenverfügbarkeit, Modellvorlage und registrierte Entity sind unterschiedliche Zustände. Sift bleibt ausgeschlossen.
- Revision, konkrete Dateien, Hashes, Lizenz und Attribution aus `THIRD_PARTY_NOTICES.md` und den Manifesten bestimmen. Eigene MIT-Dateien lizenzieren fremde Kunst nicht um. Community-Nachbildungen und eigene Ergänzungen nicht als Original-Dungeons-Export bezeichnen.
- Die ausgewählte **Goety-Redstone-Monstrosität** erhalten. `tools/generate_redstone_monstrosity.py` und die frühere Blockbench-Datei sind historische 0.2.1-Quellen, nicht die aktuelle Bossdarstellung.
- Generierte Adapter über ihren Generator ändern. `external_generator`-Einträge nicht durch den Java-Extraktor überschreiben. Eine Blockbench-Strecke nur verwenden, wenn kompatible Quelldatei und Exportweg tatsächlich vorhanden sind.

| Bestand | Auswahl und Generator |
| --- | --- |
| Java-Modelle/-Clips | `design/reference-models.json`, `tools/import_reference_models.py` |
| Bedrock/Gecko | `design/bedrock-model-imports.json`, `design/infamous-additions.json`, `tools/convert_bedrock_models.py` |
| CEM/JEM/JPM | `design/cem-model-imports.json`, `design/cem-extra-model-imports.json`, `tools/convert_cem_models.py` |
| Native Minecraft-Kompositionen | `design/builtin-models.json`, `client/entity/reference/builtin/` unter dem Java-Paket |
| Ausrüstung | `design/gear-source-inventory.json`, `tools/import_dungeons_gear.py`, `docs/GEAR_AND_SOULS.md` |

## Darstellung prüfen

Proportionen, Atlasgröße/UVs, Drehpunkte, Eltern-/Gelenkpfade, Handpfade, Transparenz und Leuchtmasken prüfen. Mehrmaterialmodelle müssen Pose, Skalierung und Sichtbarkeit über identische Gelenkpfade übernehmen. Ungebundene UV-, Parent-, Ausdrucks- oder Clipbezüge sollen benannt fehlschlagen, statt beliebige Defaults zu benutzen. Native Geometriefabriken und `minecraft:`-Texturen referenzieren; keine Spielarchive kopieren.

Bei Aktionen den belegten Quellenkontakt explizit an das Minecraft-Aktionsfenster anpassen; Rendering entscheidet keinen Schaden. CEM-Hostposen, Übergänge und eigene Clips als solche kennzeichnen. Für Ausbau `docs/ANIMATION_PRODUCTION.md`, bei betroffenen Figuren zusätzlich `docs/V04_VISUAL_POLISH.md` lesen. Bildgenerierung ersetzt weder Rigprüfung noch Ingame-Beleg.

## Ausliefern

Geänderte Quellen und Ableitungen samt Hash/Attribution dokumentieren und `python3 tools/generate_model_delivery_inventory.py` ausführen. Strukturelle Tests und Build prüfen den Vertrag; sichtbare Änderungen zusätzlich im echten Client anhand gezielter Aufnahmen prüfen. Dafür `dungeons-native-qa` verwenden, falls verfügbar, sonst den Ablauf in `docs/VALIDATION.md`. Verbleibende Abweichungen und fehlende Originalreferenzen ausdrücklich festhalten.
