# Freiwillige Begegnungen und das Kreaturenhandbuch

Stand: **2. November 2026**, **0.3.0-alpha**. Die Siegel und Kopfgelder sind eigene Sandbox-Mechaniken dieses Mods, keine extrahierten Originalgegenstände oder Originalmissionen.

## Eine Begegnung spielen

1. Im Reisetagebuch mit **J** die Aufgaben bis **Ein Ort zum Bleiben** abschließen und die Belohnung abholen. Danach erscheinen 21 freiwillige Kopfgelder. Sie starten nicht von selbst.
2. Gewünschtes Kopfgeld vor dem Kampf ausdrücklich annehmen.
3. Passendes Begegnungssiegel herstellen. Die Rezepte werden nach Erwerb eines Amethystsplitters im Rezeptbuch freigeschaltet.
4. Eine freie, ebene Fläche wählen. **Schleichen und mit dem Siegel auf die Oberseite eines festen Blocks rechtsklicken.** Es entsteht genau der im Namen genannte Gegner. Es wird keine Arena gebaut.
5. Nach dem Sieg die Belohnung im Journal selbst abholen. Kills vor Annahme zählen nicht, Beschwörungen werden nicht als alternative Bosskills gewertet.

Der Kampf verwendet normale Minecraft-Bewegung und das normale Inventar. Jeder Spieler führt seinen eigenen Questfortschritt. Die Quests geben acht bzw. 16 Smaragde, einen bzw. zwei goldene Äpfel und 80 bzw. 160 Erfahrungspunkte für Miniboss bzw. Boss. Das sind erste Balancingwerte, keine Original-Dungeons-Statistiken.

## Rezept für jedes Siegel

In der Werkbank diese Anordnung verwenden:

| | | |
| --- | --- | --- |
| Amethystsplitter | Smaragd | Amethystsplitter |
| Metall / Diamant | Themengegenstand | Metall / Diamant |
| Amethystsplitter | Redstone-Staub | Amethystsplitter |

**Miniboss:** zwei Eisenbarren. **Boss:** zwei Diamanten. Jeweils vier Amethystsplitter, ein Smaragd, ein Redstone-Staub und der passende Themengegenstand.

| Gegner | Art | Themengegenstand | Siegel-ID |
| --- | --- | --- | --- |
| Redstone Golem | Miniboss | Redstone-Block | `dungeonsalive:redstone_golem_seal` |
| Endersent | Miniboss | Enderperle | `dungeonsalive:endersent_seal` |
| Drowned Necromancer | Miniboss | Herz des Meeres | `dungeonsalive:drowned_necromancer_seal` |
| Wildfire | Miniboss | Lohenrute | `dungeonsalive:wildfire_seal` |
| Illusioner | Miniboss | Lapislazuli-Block | `dungeonsalive:illusioner_seal` |
| Pillager Raid Captain | Miniboss | Armbrust | `dungeonsalive:pillager_raid_captain_seal` |
| Vindicator Raid Captain | Miniboss | Eisenaxt | `dungeonsalive:vindicator_raid_captain_seal` |
| Skeleton Horseman | Miniboss | Knochenblock | `dungeonsalive:skeleton_horseman_seal` |
| Rampart Captain | Miniboss | Schild | `dungeonsalive:rampart_captain_seal` |
| Arch-Illager | Boss | Goldblock | `dungeonsalive:arch_illager_seal` |
| Corrupted Cauldron | Boss | Kessel | `dungeonsalive:corrupted_cauldron_seal` |
| Heart of Ender | Boss | Enderauge | `dungeonsalive:heart_of_ender_seal` |
| Mooshroom Monstrosity | Boss | Roter Pilzblock | `dungeonsalive:mooshroom_monstrosity_seal` |
| The Nameless One | Boss | Skelettschädel | `dungeonsalive:nameless_one_seal` |
| Redstone Monstrosity | Boss | Redstone-Block | `dungeonsalive:redstone_monstrosity_seal` |
| Jungle Abomination | Boss | Moosblock | `dungeonsalive:jungle_abomination_seal` |
| Treetop Whisperer | Boss | Tropenbaumstamm | `dungeonsalive:treetop_whisperer_seal` |
| Wretched Wraith | Boss | Packeis | `dungeonsalive:wretched_wraith_seal` |
| Tempest Golem | Boss | Kupferblock | `dungeonsalive:tempest_golem_seal` |
| Ancient Guardian | Boss | Prismarinkristalle | `dungeonsalive:ancient_guardian_seal` |
| Vengeful Heart of Ender | Boss | Drachenatem | `dungeonsalive:vengeful_heart_of_ender_seal` |

## Grenzen beim Benutzen

Der Server prüft Boden, freien Raum einschließlich anderer Lebewesen, Flüssigkeiten, Weltrand und bereits geladene Chunks. Spieler müssen an dieser Stelle interagieren und bauen dürfen. Auf Friedlich wird die Beschwörung abgewiesen. Bei Ablehnung wird kein Siegel verbraucht. Nach einem erfolgreichen Einsatz gilt eine gemeinsame Abklingzeit von zehn Sekunden für alle Siegel. Ein weiterer Boss oder Miniboss innerhalb von ungefähr 24 Blöcken sperrt eine neue Siegelbegegnung; dabei wird ein vergrößerter Begrenzungskasten verwendet, kein exakter Kugelradius.

Durch Siegel erzeugte Gegner werden persistent: Weggehen beendet den Kampf nicht und erzeugt keine Rückerstattung. Normales Speichern gilt. Diese Regeln gelten für Siegel; Operator-`/summon` und Kreativ-Spawn-Eier sind weiterhin Entwicklungswerkzeuge und erzwingen dieselben Einschränkungen nicht.

Die eigenen Angriffe erzeugen keine Blockexplosionen, Feuerblöcke oder durch Magie gebauten Säulen. Vorhandene Minecraft-Risiken wie Sturz, Lava oder normale andere Gegner bleiben bestehen. Ein gut lesbarer Prototyp ist noch keine ausbalancierte Bossarena.

## Handbuch mit 3D-Vorschau

**B** öffnet das Kreaturenhandbuch. Suche und Filter zeigen die tatsächlich registrierten 38 eigenen Gegner; der zweite Bereich zeigt die 13 regulären Minecraft-Berufe und sechs zusätzlichen Berufe. Die Ansicht kann Ruhe, Gehen und Aktionen zeigen. Bei Gegnern schaltet **Nächste** durch ihre vorhandenen Aktionen; bei Berufen zeigt **Aktion** die Arbeitsbewegung.

Die Figuren in diesem Menü sind ausschließlich lokale Vorschauen. Sie werden keiner Welt hinzugefügt, handeln nicht und verursachen keinen Schaden. Für Arbeitsvorschauen ist der eigene Dorfbewohner-Renderer nötig. Bei installiertem EMF bevorzugt die Konfiguration standardmäßig dessen Modelle; die eigenen Arbeitsanimationen werden dann nicht automatisch in das Fremdmodell gemischt.

Die Ansicht zeigt nur tatsächlich implementierte Inhalte. Der viel größere Referenzkatalog einschließlich Dungeons II steht in [MOB_CATALOG.md](MOB_CATALOG.md); er ist eine Entwicklungsliste, kein zweites Verzeichnis angeblich fertiger Entities.

## Seltene natürliche Begegnungen: optionales Datenpaket

Den Ordner `examples/datapacks/dungeons_wilds` in `<Welt>/datapacks` kopieren und die Welt bzw. den Server neu starten. NeoForge wendet Biommodifikatoren beim Weltladen an; für diese Änderung nicht auf `/reload` allein verlassen.

| Biome | Gegner | Gewicht / Gruppe |
| --- | --- | --- |
| Wald, dunkler Wald, Birkenwald | Royal Guard; Enchanter | 4 / 1–2; 1 / 1 |
| Sumpf, Mangrovensumpf | Wraith | 3 / 1 |
| Wüste, Tafelberge | Vanguard; Necromancer | 4 / 1–2; 1 / 1 |
| Dschungel, Bambusdschungel, lichter Dschungel | Whisperer; Leapleaf | 3 / 1; 1 / 1 |
| Gefrorene Gipfel, zerklüftete Gipfel, verschneite Hänge | Iceologer; Wind Caller | 3 / 1; 2 / 1 |
| End-Hochland, End-Mittelland | Watchling; Blastling; Snareling | 3 / 1–2; 2 / 1; 2 / 1 |

Das Gewicht ist ein relativer Eintrag in der Monster-Spawnliste, keine Prozentwahrscheinlichkeit pro Sekunde. Es gelten die normalen Monster-Licht-/Schwierigkeitsregeln, der Monster-Cap und registrierte Platzierungsbedingungen. Das Paket erzeugt **keine natürlichen Bosse oder Minibosse** und baut keine Strukturen. Es enthält zwölf der 18 regulären Prototypen; die übrigen bleiben über Spawn-Eier und `/summon` zugänglich.

Technische Grundlage: [NeoForge 1.21.1: Biome Modifiers](https://docs.neoforged.net/docs/1.21.1/worldgen/biomemodifier/). Die Eigenschaften des konkreten Pakets ergeben sich aus seinen sechs `neoforge:add_spawns`-JSONs.

## Prüfbereich

`EncounterGameTests` prüft tatsächliche Registrierung, fehlendes Schleichen, blockierte Fläche ohne Verbrauch, genau einen erfolgreichen Spawn, genau einen verbrauchten Gegenstand, Persistenzmarkierung, gemeinsamen Cooldown und Ablehnung eines zweiten nahen Gegners. Die Übersicht über Build, Integrationsprofil und verbleibende Grenzen steht in [VALIDATION.md](VALIDATION.md).
