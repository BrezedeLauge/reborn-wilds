# Lebendige Umgebung – Stand des Prototyps

Der implementierte erste Baustein bringt kleine, bewegte Lebewesen und Luftpartikel
in bestehende Minecraft-Welten. Er verändert weder Gelände noch Kampf-, Abbau- oder
Bausysteme. Die Darstellung besteht aus **lokalen, zweidimensionalen Partikeln**.
Sie sind keine angreifbaren, zähmbaren oder serverseitig simulierten Mobs.

## Was umgesetzt ist

| Effekt | Verhalten | Umgebung |
| --- | --- | --- |
| Schmetterlinge | Vierstufiger Flügelschlag, drei Farbtöne, schwebende Flugbahn und sanftes Ein-/Ausblenden | Tagsüber in Wald-/Dschungel-Tags, Plains, Sunflower Plains, Meadow und Cherry Grove |
| Glühwürmchen | Kleine Flugbewegungen und pulsierender heller Hinterleib | Nachts in Wald, Dschungel, Sumpf, Mangrovensumpf und Flussbiomen |
| Sporen/Luftpartikel | Seltene, langsam steigende Partikel | Wald-/Dschungelbiome; optional abschaltbar |

Die Helligkeit der Glühwürmchen ist eine Render-Eigenschaft. Sie beleuchten keine
Blöcke und beeinflussen nicht, wo feindliche Mobs spawnen. Alle Pixeltexturen sind
für dieses Projekt neu erzeugte Prototypen; es sind **keine Originalgrafiken aus
Minecraft Dungeons oder Actions & Stuff**.

## Wie man den Effekt sieht

1. Eine Welt in Minecraft Java 1.21.1 mit dem Mod öffnen.
2. In einen Wald oder eine Wiese gehen; Grafikoption »Partikel« auf »Alle« stellen.
3. Tagsüber bei trockenem Wetter einige Sekunden warten: In der Umgebung fliegen
   Schmetterlinge. Blickhöhe knapp über Gras macht sie leichter erkennbar.
4. Im Wald oder Sumpf bis zur Nacht warten. Zum Entwickeln können mit aktivierten
   Cheats `/time set night` und `/weather clear` verwendet werden.
5. Zum gezielten Funktionstest: `/particle dungeonsalive:butterfly ~ ~1 ~` oder
   `/particle dungeonsalive:firefly ~ ~1 ~`. Diese Befehle lösen einen einzelnen
   Partikel aus und umgehen die automatische Biomauswahl.

## Budget und Einstellungen

Die Client-Konfiguration wird als `dungeonsalive-client.toml` registriert.

| Schlüssel unter `[ambience]` | Standard | Bedeutung |
| --- | --- | --- |
| `enabled` | `true` | Automatische Atmosphäre aktiv |
| `butterflies` | `true` | Schmetterlinge |
| `fireflies` | `true` | Glühwürmchen |
| `spores` | `true` | Schwebende Partikel |
| `maxParticles` | `96` | Harte Obergrenze der von diesem Spawner verwalteten Partikel; 0–384 |
| `spawnRadius` | `18` | Abstand für neue Partikel; 4–32 Blöcke |
| `density` | `1.0` | Spawnwahrscheinlichkeit; 0–3 |

Die Minecraft-Einstellung »Verringert« halbiert das Budget. »Minimal« deaktiviert
die automatische Atmosphäre. Alle fünf Client-Ticks werden höchstens vier
Geländespalten betrachtet, und ausschließlich bereits geladene Chunks werden
verwendet. Bei Regen, unter Wasser und in Dimensionen ohne Oberlicht entstehen
keine neuen automatischen Effekte. Bestehende Partikel leben noch kurz weiter.
Weltwechsel und Deaktivierung räumen verwaltete Partikel auf. Eine zusätzliche
Frist verhindert dauerhafte Referenzen, falls ein Ressourcen-Neuladen Partikel
aus Minecrafts Engine entfernt.

Der Grenzwert gilt für den automatischen Spawner. Manuell durch `/particle`
erzeugte Partikel verwenden Minecrafts normalen Partikelmechanismus.

## Architektur und Anschluss

- Common: `AmbientParticles.register(modBus)` registriert nur Partikeltypen.
- Common: `container.registerConfig(ModConfig.Type.CLIENT, AmbientConfig.SPEC)`
  registriert lokale Einstellungen ohne Clientklassen zu laden.
- Ausschließlich physischer Client: `AmbientClient.register(modBus)` registriert
  Provider und den Client-Tick-Listener. Diesen Aufruf niemals auf dem Dedicated
  Server ausführen.
- `AmbientParticle` behandelt Bewegung, Flipbook und Render-Helligkeit.
- Die Texturen lassen sich unverändert reproduzieren:
  `python3 tools/generate_ambient_textures.py` (nur Python-Standardbibliothek).

## Bewusste Grenzen und nächster Ausbau

- Billboard-Flügel statt vollständiger 3D-Modelle; noch kein Landen auf Blumen.
- Keine neuen Geräusche und keine Änderung der Vanilla-Mobanimationen.
- Die Platzwahl orientiert sich an Biomen und Oberflächen, noch nicht an einzelnen
  Blumen, dem Jahreslauf oder einer persistenten Population.
- Es gibt keine Questziele für diese Dekopartikel. Für fangbare oder fotografierbare
  Tiere ist später ein serverseitiger Entity-Typ mit eigener Modellanimation nötig.
- Feinschliff von Sichtbarkeit, Dichte und Flügeltempo braucht eine Prüfung im
  laufenden Client. Ein erfolgreicher Compile ersetzt diese Sichtprüfung nicht.

Für den nächsten Schritt bieten sich zuerst ein eigener Schmetterlings-Entity mit
Lande-/Fluchtverhalten und anschließend Biome-Tags für frei definierbare
Spawngebiete an. Partikel bleiben für entfernte Schwärme sinnvoll.

## Technische Primärquellen

- [NeoForge 1.21.1: Partikel und Provider](https://docs.neoforged.net/docs/1.21.1/resources/client/particles/)
- [NeoForge 1.21.1: Client-Konfiguration](https://docs.neoforged.net/docs/1.21.1/misc/config/)

Die Quellen beschreiben die verwendeten APIs. Verhalten, Parametrisierung,
Texturzeichnungen und Code dieses Moduls wurden für dieses Projekt erstellt.
