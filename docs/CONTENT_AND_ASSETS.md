# Inhalte, Modelle und Herkunft

Recherche- und Planungsstand: **2. November 2026**. Zielplattform des Projekts: Minecraft Java **1.21.1 / NeoForge / Java 21**.

## Das gewünschte Ergebnis

Die Sandbox bleibt der Mittelpunkt: frei bauen, abbauen, herstellen und erkunden. Dungeons liefert die gestalterische Richtung für erkennbare Kreaturen, Ausrüstung, lebendige Landschaften und kleine Abenteuer. Bestehende Welten sollen weiter sinnvoll spielbar sein. Quests ergänzen das Erkunden; sie sperren weder normales Crafting noch den Zugang zur üblichen Minecraft-Progression.

Dieses Dokument beschreibt die Beschaffung und Umsetzung weiterer Inhalte. Den tatsächlich implementierten Stand und die durchgeführten Tests nennt die Projekt-README. Ein Eintrag in dieser Planung bedeutet noch keine fertige Entity oder ein ausgeliefertes Modell.

## Drei verschiedene Bedeutungen von „Original“

| Gemeinter Wunsch | Technischer Weg | Nachweis vor Integration |
| --- | --- | --- |
| Der erkennbare Gegner oder Gegenstand aus Dungeons | Vorhandenen Mod optional verbinden oder eine eigene Umsetzung schreiben | Spielreferenz, Funktionsbeschreibung, überprüfte Minecraft-Registry-ID oder eigene Implementierung |
| Möglichst ähnliche Bewegung, Silhouette und Wirkung | Eigenes Modell, eigene Textur und passende Animationen anhand von Referenzen erstellen | Bearbeitbare Quelldatei, Urheber/Entstehung, Vergleich im Spiel |
| Die unveränderten Dateien aus dem kommerziellen Spiel | Separater Import wäre nur mit geklärter Nutzungsgrundlage und passender Konvertierung sinnvoll | Konkrete Herkunft, Bedingungen, erlaubter Verwendungszweck und Zielformat |

Das Projektpaket enthält keine extrahierten Dungeons-Spieldateien und keinen kopierten Actions-&-Stuff-Inhalt. Einen fremden Mod als optionale Installation zu unterstützen ist ein eigener Weg: Der Nutzer installiert ihn aus der Quelle des Autors, und unser Questsystem arbeitet mit seinen registrierten Inhalten.

## Ausgewählte Quellen in 0.4.0

Die Alpha verwendet inzwischen konkrete zugeschriebene Community-Modellquellen: Goety 2, Dungeons Mobs und DungeonsMobs-Port unter MIT sowie ReCrafted Mobs V11 unter CC BY-NC-SA 4.0. Die übernommenen Geometrien, Texturen und Animationsquellen sind dateigenau ausgewählt; fremde Modbinaries, Audioarchive und kommerzielle Spielarchive werden nicht pauschal übernommen. Native Minecraft-Modelle werden über die installierte Spielversion angesprochen.

`design/reference-models.json` legt die Laufzeitbindung fest. `design/model-delivery-inventory.json` enthält das tatsächliche Lieferinventar mit SHA-256 und Herkunft; `design/model-source-audit.json` ist dagegen die größere Verfügbarkeitskarte. Eigene Ergänzungen, etwa Vengeful-Geometrie und Treetop-Palettenadaption, sind gesondert bezeichnet. Die 436 Katalogzeilen sind kein Implementierungsnachweis; der aktuelle Stand steht in `design/implementation-status.json`.

Die mitgelieferten ReCrafted-Assets und ihre Anpassungen behalten CC BY-NC-SA 4.0. Die Bedingungen stehen vollständig in `third_party/recrafted_mobs/LICENSE.txt` und den JAR-Notices. Eigener MIT-Code ändert daran nichts. Quelle: [ReCrafted Mobs](https://modrinth.com/resourcepack/recrafted-mobs).

Die optionale eigenständige Integration von Enchant With Mob bleibt erhalten; sie besitzt eigene Registry-IDs und eine zusätzliche Quest. Sie ist für die neuen integrierten Modelle nicht erforderlich. Ebenso benötigen diese Modelle weder GeckoLib noch ein installiertes EMF-/ReCrafted-Ressourcenpaket: die ausgewählten Formen sind in den eigenen Renderer adaptiert. Details und bisherige Prüfgrenzen optionaler Fremdmods stehen in [INTEGRATIONS.md](INTEGRATIONS.md).

Die neue Ausrüstung verwendet zusätzlich vierzehn ausgewählte Itemmodelle und vierzehn unveränderte PNGs aus Dungeons Gear, Revision `9ba0075c5b2f13ef54ec914ad1327a853ecdce6f`, MIT. Herkunft und SHA-256 stehen separat in [gear-source-inventory.json](../design/gear-source-inventory.json). Die Spielmechanik, Rezeptbalance und Seelenregeln sind eigene Arbeit; die Fremdmod wird nicht als vollständiges Binärpaket gebündelt.

## Quellcode und Assets getrennt erfassen

Die älteren Projekte **Dungeons Mobs** und **Dungeons Gear** haben öffentliche Repositories mit MIT-Lizenz. Ihre veröffentlichten Grundlagen beziehen sich auf Forge 1.19.2. Daraus folgt weder eine fertige NeoForge-Portierung noch automatisch ein überprüfter Herkunftsnachweis für jede Grafik und jeden Sound. Quellen: [Dungeons Mobs – Lizenz](https://github.com/Infamous-Misadventures/Dungeons-Mobs/blob/1.19/LICENSE.txt), [Dungeons Gear – Lizenz](https://github.com/Infamous-Misadventures/Dungeons-Gear/blob/1.19/LICENSE.txt), [Mobs-README](https://github.com/Infamous-Misadventures/Dungeons-Mobs).

Für jede übernommene Datei muss deshalb nachvollziehbar bleiben, woher sie stammt. Für die inzwischen ausgewählten Modell- und Animationsquellen werden die zutreffenden Lizenz- und Urhebervermerke mitgeführt. Texturen, Geometrie, Animationen, Musik und Effekte erhalten zusätzlich ihre eigenen Herkunftseinträge. Die [Minecraft Usage Guidelines](https://www.minecraft.net/en-us/usage-guidelines) beschreiben Mods als eigene Erweiterungen und unterscheiden sie von einer Weitergabe wesentlicher Teile des Spiels. Das ist ein relevanter Ausgangspunkt; dieses Projekt erteilt keine pauschale Freigabe zum Kopieren von Spieldateien.

### Herkunftsregister für neue Dateien

Das Projekt führt das maschinenlesbare Herkunftsregister `design/asset-provenance.json`. Der historische eigene Assetbestand steht dort; der maßgebliche neuere Fremdmodellbestand steht zusätzlich in `design/model-delivery-inventory.json`. Bei weiteren Übernahmen beide Zuständigkeiten beachten. Das folgende Schema ist eine **Planungsvorlage**, kein vorhandener Importmechanismus:

```json
{
  "asset_id": "example_entity_idle",
  "project_path": "assets/dungeonsalive/animations/example.animation.json",
  "kind": "animation",
  "origin": "created_for_this_project",
  "author": "Name eintragen",
  "source_url": null,
  "source_revision": null,
  "license": "explizit eintragen",
  "attribution": "",
  "modified": false,
  "reviewed_on": "YYYY-MM-DD"
}
```

Erlaubte Herkunftskategorien für die Arbeitsorganisation:

- **created_for_this_project:** im Projekt erstelltes Material; bearbeitbare Quelle aufbewahren.
- **licensed_dependency:** fremde Laufzeitabhängigkeit; das Paket enthält nur Referenz und Installationsanleitung.
- **licensed_reuse:** konkrete Dateien werden unter dokumentierten Bedingungen übernommen.
- **pending_review:** Herkunft oder Nutzungsgrundlage ist noch offen; vorerst keine Aufnahme in das auslieferbare JAR.

„Im Internet gefunden“, „aus einem kostenlosen Mod“ und „per KI nachgebaut“ sind keine vollständigen Herkunftsangaben.

## Was Dekompilieren praktisch leisten würde

Eine dekompilierte Methode oder extrahierte Geometrie ergibt noch keinen Minecraft-Mod. Für eine Entity werden weiterhin Registrierung, Verhalten, Kollisionsbox, Spawnregeln, Speicherung, Netzwerkzustand, Rendering, Sound und Loot benötigt. Ein Artefakt braucht außerdem die passende Interaktion und ein Balancing für Minecrafts Inventar und Kampfregeln.

Für dieses Projekt ist zunächst kein Dungeons-Decompiler erforderlich. Verhalten kann aus beobachtetem Spielverhalten beschrieben werden; bestehende autorenseitige Mods liefern gegebenenfalls eine lauffähige optionale Umsetzung. Für eigene Kreaturen sind nachvollziehbar erstellte Modelle und Animationen der wartbare Ansatz. Ein späterer Import von vom Nutzer bereitgestellten Dateien wäre ein gesonderter Arbeitsschritt mit konkreten Formaten, Dateien und Herkunftsangaben.

## Produktionsablauf für einen eigenen Mob

1. **Referenzblatt:** Silhouette, Größe, Materialien, Bewegungscharakter, eindeutige Angriffsphasen und Klangwirkung beschreiben. Gameplay-Zahlen ausdrücklich für Minecraft neu festlegen.
2. **Einfaches Modell:** Bearbeitbare Blockbench-Datei mit sauberen Pivotpunkten, benannten Bones und sparsamer Geometrie anlegen. Erst das Grundmodell im Spiel prüfen.
3. **Textur:** Eigene UV-Belegung und zum übrigen Spiel passende Pixeldichte. Emission nur dort, wo sie die Lesbarkeit verbessert.
4. **Bewegung:** `idle`, `walk`, gegebenenfalls `run`, `look`, `attack` oder `cast`, `hurt` und `death` als klar getrennte Zustände. Bei kleinen Flugtieren zusätzlich Flügelschlag, Gleitphase, Landung und Aufschrecken.
5. **Spielverhalten:** Serverzustand bestimmt Ziel, Treffer, Cooldown und Tod. Clientanimation zeigt diesen Zustand an. Schaden darf nicht von einem nur lokal ausgeführten Animations-Keyframe abhängen.
6. **Weltbezug:** Habitat, Tageszeit, Wetter, Gruppenverhalten, maximale Dichte und Abstand zu Spielern festlegen. Bossmechaniken erhalten eine bewusste Aktivierung.
7. **Ein vollständiges Abenteuer:** Ort, Quest, Gegner, Fundstück und Belohnung zusammen testen. Erst danach Varianten hinzufügen.

Für aufwendigere eigene Modelle ist **GeckoLib 4** ein möglicher späterer Animationsbaustein. Auf der Autorenseite ist 4.9.3 für NeoForge 1.21.1 gelistet; die entsprechende Dokumentation beschreibt synchronisierte Animationen. Der Starterkern muss dafür nicht nachträglich vollständig umgebaut werden. Quellen: [Versionen](https://modrinth.com/mod/geckolib/versions), [GeckoLib-4-Dokumentation](https://github.com/bernie-g/geckolib/wiki/Geckolib-Items-%28Geckolib4%29).

## Actions & Stuff als Qualitätsreferenz

Der Nutzer nennt Actions & Stuff als Referenz für die gewünschte Lebendigkeit. Für die Java-Entwicklung übersetzen wir das in überprüfbare Ziele: weiche Übergänge, sichtbare Gewichtsverlagerung, Blickbewegung, Reaktion auf die Umgebung und präzises Timing. Es wird keine Bedrock-Datei in ein Java-Ressourcenpaket umbenannt.

**Fresh Animations mit EMF und ETF** ist eine optionale Ergänzung für vorhandene Vanilla-Mobs. Eigene neue Dungeons-Entities brauchen weiterhin ihre eigenen Renderer und Animationen. Das Ressourcenpaket bringt keine allgemeine automatische Animation für jeden beliebigen neuen Mob mit. Quelle und Installationsplan stehen in [INTEGRATIONS.md](INTEGRATIONS.md).

## Erste Abnahme eines Inhaltsimports

Eine Integration ist erst als umgesetzt zu markieren, wenn diese Ergebnisse vorliegen:

- Exakte Modversion, Abhängigkeiten und Loader sind festgehalten.
- Entity-/Item-IDs wurden im tatsächlich gestarteten Profil festgestellt.
- Die Quest ist ohne OP-Rechte spielbar und kann nur einmal belohnt werden.
- Entfernen der optionalen Mod verursacht keinen Startabsturz; betroffene Quests werden klar als nicht verfügbar behandelt.
- Visuelles Verhalten wurde mit und ohne optionale Animationspakete angesehen.
- Für Multiplayer wurden zwei Clients, ein Dedicated Server und ein Neustart geprüft.

Diese Punkte sind Entwicklungsabnahmen mit getrenntem Testumfang. Beim Enchanter-Profil sind das Laden von Enchant With Mob und Bagus Lib auf einem echten Headless-Server, die reale Entity-Registry und das optionale Questziel bereits geprüft; alle 13 Server-GameTests bestanden (0.2.0, 25 Quests). Der Animationsstack ist hinsichtlich Downloads, Prüfsummen und Metadaten geprüft. Grafische Sichtprüfung, vollständiger Enchanter-Kampf und echter Client-Multiplayer stehen noch aus. Details: [VALIDATION.md](VALIDATION.md).
