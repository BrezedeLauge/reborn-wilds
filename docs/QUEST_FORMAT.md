# Questformat und Fortschritt

Stand: 1. November 2026. Implementierung: `quest/data/QuestDocumentParser.java`, `QuestRepository.java`, `QuestSavedData.java` und `quest/core/`.

## Dateien

Ein Datapack für Minecraft 1.21.1 enthält `pack.mcmeta` mit `pack_format: 48`. Beispiel:

```json
{"pack":{"pack_format":48,"description":"Meine Dungeons-Alive-Quests"}}
```

Die Quest liegt unter **`data/<namespace>/dungeonsalive/quests/<pfad>.json`**. Daraus ergibt sich die ID `<namespace>:<pfad>`. Für unsere Mod lautet ein vollständiger Pfad `data/dungeonsalive/dungeonsalive/quests/small_wonders.json`; das doppelte `dungeonsalive` bezeichnet Namespace und Ressourcenverzeichnis.

Vollständiges eingebautes Beispiel:

```json
{
  "schema_version": 1,
  "title": "Kleine Wunder",
  "description": "Zwischen Blumen und Gräsern steckt mehr Leben, als es zunächst scheint. Nimm dir Zeit für einen Spaziergang und sammle ein paar Blüten. Die Blumen bleiben nach dem Abschluss in deinem Inventar.",
  "icon": "minecraft:poppy",
  "order": 10,
  "prerequisites": [],
  "objectives": [
    {"id":"dandelions","type":"collect_item","target":"minecraft:dandelion","amount":3,"label":"Löwenzahn im Inventar"},
    {"id":"poppies","type":"collect_item","target":"minecraft:poppy","amount":3,"label":"Mohn im Inventar"}
  ],
  "rewards": [{"item":"minecraft:honey_bottle","count":3}],
  "experience": 20
}
```

Titel, Beschreibung und Labels sind Klartext. `icon`, `target`, Reward-Items und Voraussetzungen benötigen explizite `namespace:path`-IDs. Die Objective-ID ist ein stabiler lokaler Bezeichner, kein Anzeigetext. `order` steuert die Reihenfolge; bei Gleichstand wird nach Quest-ID sortiert.

## Ziele und Zustände

| Typ | Bedeutung |
| --- | --- |
| `collect_item` | Aktueller Besitz im Spielerinventar; gleiche Gegenstände in mehreren Slots werden summiert. Truheninhalte zählen nicht. |
| `visit_biome` | Aufenthalt im angegebenen Biom nach Annahme; `amount` muss 1 sein. Wiederholte Prüfungen zählen keinen neuen Besuch. |
| `kill_entity` | Passendes Todesereignis mit einem `ServerPlayer` als verantwortlicher DamageSource-Entity. Wolf-/Begleiter-Kills werden noch nicht dem Besitzer zugerechnet. |

`LOCKED` wird nach Abholung aller Vorgänger zu `AVAILABLE`. Erst **Annehmen** aktiviert die Quest (`ACTIVE`). Sind alle Ziele erfüllt, folgt `READY`; erst **Abholen** macht daraus dauerhaft `CLAIMED`. Eine freigeschaltete Folgequest beginnt nicht automatisch.

Sammelziele prüfen Besitz und verbrauchen keine Gegenstände. Weglegen kann `READY` wieder zu `ACTIVE` ändern. Erneutes Aufsammeln erhöht keinen kumulativen Zähler. Vor einem Claim prüft der Server den aktuellen Bestand. Ziele zählen erst nach Annahme; historische Kills werden nicht nachgetragen.

Im weitergeführten Quellstand ignoriert der Todesereignis-Adapter zeitlich begrenzte Dungeon-Helfer mit gespeichertem Beschwörer (`DungeonMob.isSummoned()`). Das gilt auch nach NBT-Laden. Die Registry-ID allein genügt für diese Helfer nicht als Killnachweis. Normale Gegner, vorhandene Quest-/Ziel-IDs und gespeicherte Fortschritte bleiben erhalten; früher gutgeschriebene Kills werden nicht zurückgenommen. Der Zusatz ist noch nicht in der ausgelieferten JAR und wartet auf den neuen Servertest; siehe [VALIDATION.md](VALIDATION.md).

`prerequisites` nennt Vorgänger-IDs. Unbekannte Voraussetzungen, doppelte IDs und Zyklen verhindern den neuen Katalog. Optionales `requires_mods`, beispielsweise `["enchantwithmob"]`, deaktiviert eine Quest bei fehlendem Mod; ihre abhängigen Quests werden ebenfalls übersprungen. Das Beispiel `examples/datapacks/enchanter` ergänzt `dungeonsalive:the_enchanter` mit Ziel `enchantwithmob:enchanter`. Es erzeugt selbst keinen Gegner.

## Grenzen und Validierung

| Inhalt | Grenze |
| --- | --- |
| Katalog / JSON-Datei | 128 Quests / 65.536 Zeichen |
| Objectives / Rewards | Jeweils 16 pro Quest |
| Objective-Soll / Reward-Anzahl | 1–1.000.000 / 1–64 je Reward |
| Titel / Beschreibung / Label | 128 / 2.048 / 160 Zeichen |
| Resource-ID / Objective-ID | 256 / 64 Zeichen |
| Voraussetzungen / erforderliche Mods | 128 / 16 Einträge |
| Erfahrung / Reihenfolge | 0–100.000 / −1.000.000 bis 1.000.000 |

Schema 1 weist unbekannte Felder, falsche Datentypen und ungültige Zahlen zurück. Item- und Entity-IDs werden gegen die geladenen Registries geprüft. Biome werden syntaktisch geprüft und bei Questannahme gegen die aktuelle Biom-Registry validiert.

## Reload und bestehende Welten

Nach Datapack-Änderungen als Operator `/reload` ausführen. `/dungeonsalive status` zeigt Kataloggröße, Revision und Warnungen. Ein ungültiger Reload erhält den letzten gültigen Katalog.

UUID-Fortschritte liegen in `<Welt>/data/dungeonsalive_quests.dat`. Quest- und Objective-IDs müssen für dieselbe Bedeutung stabil bleiben. Umordnen ist möglich; Zähler werden auf aktuelle Sollwerte begrenzt. Vorübergehend entfernte IDs bleiben gespeichert. Umbenennungen werden **nicht** automatisch migriert und können wie neue Aufgaben wirken. Für ein inhaltlich anderes Ziel nicht unbemerkt dieselbe Objective-ID wiederverwenden. Ein geändertes Reward-Schema zahlt bereits abgeholte Quests nicht erneut aus.

Der tatsächliche Teststand einschließlich optionaler Mods steht in [VALIDATION.md](VALIDATION.md).
