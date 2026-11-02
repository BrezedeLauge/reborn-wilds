# Redstone-Monstrosität – aktuelle Referenzadaption

Stand **0.3.0-alpha, 2. November 2026**. Die Bildkorrektur des Nutzers zeigt eine Goety-Darstellung der Redstone Monstrosity. Das aktive Modell verwendet deshalb genau die entsprechende veröffentlichte Goety-Geometrie und ihre Textur-/Animationsquellen. Der frühere eigene 0.2.1-Entwurf wurde als historische Quelle erhalten, ist aber nicht mehr das ausgewählte Laufzeitmodell.

## Körper und Oberfläche

Die Quelle besitzt **29 Quader, 26 Modellteile und zehn benannte Clips**. Entscheidend sind ihre Proportionen und UVs: breite, geschichtete Steinschultern, rote Fassung des Rückens, kompakter zentraler Kopf, kleine helle Gesichtselemente, lange schwere Arme, ausgeformte Finger und getrennte Beinsegmente. Das Modell wird einheitlich mit Faktor **0,63** skaliert; die eigene Kollisionsbox bleibt 2,8 × 3,8 Blöcke.

Die Quellgrundtextur besitzt **512 × 256 Pixel**. Separate Active-, Eyes- und Lines-Schichten erhalten beleuchtete Details und die kleinen hellen Augen. Normal beleuchteter Stein und selbstleuchtende Markierungen werden getrennt gerendert. Die Dateien liegen unter `textures/entity/reference/goety/servants/redstone_monstrosity/`.

## Animationen und Kontaktpunkte

| Clip | Verwendung |
| --- | --- |
| IDLE / WALK | Ruhe und tatsächliche Bewegung |
| SMASH | HEAVY_SLAM; dokumentierter Quellkontakt bei 1,70 Sekunden |
| SUMMON | SUMMON; dokumentierte Würfelfreigabe bei 2,00 Sekunden |
| SPIT | FIRE_FIELD-Adaption; Quellfreigabe bei 0,85 Sekunden |
| DEATH | Sterbesequenz |
| ACTIVATE, TO_SIT, TO_STAND, SIT | Im Assetbestand erhalten; nicht als eigene gegenwärtige Bossmechanik behauptet |

Der Server verwendet weiterhin die eigenen Minecraft-Aktionslängen. Der Renderer bildet die Quellzeit vor und nach dem ausdrücklich angegebenen Kontaktpunkt auf Vorbereitung und Erholung dieser Aktion ab. Damit fällt der sichtbare Kontakt auf den tatsächlichen Serverauslöser. Diese Zeitabbildung ist keine unveränderte Kopie der kompletten Goety- oder Dungeons-Kampflogik.

## Bearbeiten und reproduzieren

- Auswahl: `design/reference-models.json`, ID `redstone_monstrosity`.
- Original-Snapshot: `third_party/goety/source_models/RedstoneMonstrosityModel.java`.
- Animation-Snapshot: zugehörige Datei unter `third_party/goety/source_animations/`.
- Ausgabe: `GoetyRedstoneMonstrosityGeometry.java`, `GoetyRedstoneMonstrosityAnimations.java`.
- Neu erzeugen: `python3 tools/import_reference_models.py`.
- `tools/generate_redstone_monstrosity.py`, `RedstoneMonstrosityModel.java` und die alte `.bbmodel` erzeugen ausschließlich den historischen eigenen Vorgänger. Sie ersetzen die aktive Binding-Auswahl nicht.

```bash
python3 tools/prepare_boss_showcase.py
./gradlew runBossShowcase
```

Die Galerie enthält echte Client-Framebuffer-Aufnahmen einer serverseitigen Entity in einer eigens gestellten Entwicklungswelt. Den tatsächlich ausgeführten Stand und die ausgewählten Bilder nennt [VALIDATION.md](VALIDATION.md). Eine einzelne schöne Pose beweist keine vollständig abgenommene Kampfsequenz.

## Herkunft und verbleibende Mechanik

Quelle: [Goety 2, Polarice3](https://github.com/Polarice3/Goety-2), Revision `7de64560f76ae47f1b399bb46f69540511b9871e`, MIT. Vollständige Lizenz und Attribution befinden sich in `third_party/goety/`, mit zusätzlichem Lieferinventar in `design/model-delivery-inventory.json`.

Noch offen sind insbesondere die vollständige ursprüngliche Arena-/Bombenmechanik, alle Bewegungsübergänge in langen Kämpfen und eine Mehrspielerabnahme mit mehreren realen Clients. Der Boss hat bereits Leben, Bossleiste, vorbereitete serverseitige Aktionen, Würfelbeschwörung und eigene Minecraft-Beute; diese gemeinsame Kampfadaption wird nicht als vollständiger Originalkampf bezeichnet.
