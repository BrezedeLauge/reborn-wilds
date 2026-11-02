# Mob-Katalog: Dungeons I und Dungeons II

Recherche- und Abgleichstand: **2. November 2026**. Zielplattform des Mods: **Minecraft Java 1.21.1 / NeoForge 21.1.265 / Java 21**.

Der Katalog in [dungeons-mob-catalog.json](../design/dungeons-mob-catalog.json) enthält **436 Referenzeinträge** mit Namen, Spiel, Familie, Kategorie, Quellen, Variantenbeziehung, Animationsbedarf und tatsächlichem Implementierungsstatus. Davon entfallen **272 auf Dungeons I** und **164 auf Dungeons II**. Eine Zeile kann eine Kreatur, Variante, Händlerrolle, Haustierfarbe oder ein Umgebungsobjekt beschreiben. Diese Zahl bezeichnet deshalb weder 436 unterschiedliche Tierarten noch 436 fertig implementierte Mobs.

Die aktuelle Implementierung in **0.4.0** umfasst **131 registrierte Figuren**: 78 feindliche (zwölf Bosse, neun Minibosse, 57 reguläre) und 53 friedliche (35 Begleiter, vier Wildtiere, zwei Schlüsselgolems, elf Händler, ein befreibarer Schmied). Dazu kommen vier getrennt gezählte Zauberkonstrukte und sechs eigene Vanilla-Dorfbewohnerberufe. Die vollständige aktuelle Tabelle steht in [IMPLEMENTED_CREATURES.md](IMPLEMENTED_CREATURES.md), der maschinenlesbare Abgleich in [implementation-status.json](../design/implementation-status.json).

Die ursprünglichen `implementation_status`-/`animation_status`-Felder des recherchierten Katalogs sind der historische Erfassungsstand. **Der aktuelle Laufzeitstatus wird durch `implementation-status.json` ersetzt**, ohne historische Quellenbehauptungen nachträglich umzuschreiben. Er verknüpft 131 registrierte Figuren und vier gesonderte Konstrukte mit insgesamt 129 Katalogzeilen; einige Rüstungspaletten gehören zur gleichen Grundzeile. Modelle, native Prüfaufnahmen und vollständige Originalmechaniken sind getrennte Statusdimensionen.

## Aktueller Quellen- und Sift-Abgleich

Der Nutzer schließt die **Sift-Dimension samt zugehörigen Inhalten** aus. Das betrifft 47 eindeutig zugeordnete Zeilen. `mcd2:wisp` besitzt eine ausdrücklich offene Habitatzuordnung und wird nicht still als ausgeschlossen oder fertig umgesetzt behandelt. Unser `wisp` bezieht sich auf die separate Dungeons-I-Hinweisfigur.

[model-source-audit.json](../design/model-source-audit.json) prüft alle 436 Zeilen: 131 Zuordnungen zu konkreten wiederverwendbaren Quellenpaketen, 125 native Minecraft-Grundlagen, 133 genaue Nachbildungen/Varianten offen und 47 Sift-Ausschlüsse. Diese Quellengruppen sind keine Entity-Zählung. Details: [SOURCE_MODEL_AUDIT.md](SOURCE_MODEL_AUDIT.md).

## Wie die Zahlen zu lesen sind

Die folgenden exklusiven Gruppen ergeben zusammen den Katalogumfang. Ein „Basiseintrag“ ist ein Referenzgrundtyp dieser Arbeitsliste; dieselbe Kreatur in zwei Spielen wird zweimal dokumentiert. Eine zoologische oder vollständige interne Asset-Zählung ist damit nicht gemeint.

| Zählgruppe | Dungeons I | Dungeons II |
| --- | ---: | ---: |
| Basiseinträge für Kreaturen | 99 | 81 |
| Abgeleitete Mobvarianten einschließlich Ancients | 79 | 47 |
| Zusätzliche freundliche Rollen eines bereits aufgeführten Typs | 0 | 2 |
| NPC- und Händlerrollen | 13 | 22 |
| Kosmetische Haustier-Einträge | 49 | 2 |
| Gefahren, Objekte, Effekte und Filmformen | 32 | 10 |
| **Gesamt** | **272** | **164** |

Die separate Kategorie `boss`, `miniboss`, `hostile`, `passive`, `npc`, `summon`, `ancient` oder `variant` beschreibt die Rolle. `entity_kind` erklärt, ob die Zeile wirklich eine Kreatur oder etwa ein Partikel, eine Falle oder eine Händlerfigur meint. `count_class` entspricht der obigen Zählgruppe. `base_entry` verknüpft eine Variante mit ihrem Grundtyp.

Insbesondere sind Geierschatten, Strider-Umgebungslaute und Wisps keine zusätzlichen fertig animierten Tiere. Dungeons-II-Trill und -Wisp stehen im Wiki ausdrücklich bei mobähnlichen Partikeln. Die Mod besitzt gegenwärtig einen eigenen dekorativen Firefly-Partikel; daraus entsteht kein serverseitiger Glühwürmchen-Mob und kein verlässliches Questziel. Quellen: [I-Mobübersicht](https://minecraft.wiki/w/Dungeons%3AMob), [Klassifikationsübersicht](https://minecraft.fandom.com/wiki/Minecraft_Dungeons%3AMob/editcopy), [II-Mobübersicht](https://minecraft.wiki/w/Dungeons_II%3AMob).

## Alle zwölf dokumentierten Bossbegegnungen aus Dungeons I

Die Bossliste folgt der spielbaren Begegnungskategorie des [Minecraft Wiki](https://minecraft.wiki/w/Dungeons%3ABoss). Interne Blueprint-Markierungen oder beschworene Angriffsobjekte können andere Summen ergeben und werden hier nicht als zusätzliche Hauptbosse gezählt. Jeder der folgenden zwölf Typen besitzt inzwischen ein eigenes registriertes Profil, aber noch keine vollständig originalgetreue Begegnung.

| Originalname | Inhaltsbereich | Laufzeit-ID |
| --- | --- | --- |
| Arch-Illager | Hauptspiel | `dungeonsalive:arch_illager` |
| Corrupted Cauldron | Hauptspiel | `dungeonsalive:corrupted_cauldron` |
| Heart of Ender | Hauptspiel | `dungeonsalive:heart_of_ender` |
| Mooshroom Monstrosity | Hauptspiel | `dungeonsalive:mooshroom_monstrosity` |
| Nameless One | Hauptspiel | `dungeonsalive:nameless_one` |
| Redstone Monstrosity | Hauptspiel | `dungeonsalive:redstone_monstrosity` |
| Jungle Abomination | Jungle Awakens | `dungeonsalive:jungle_abomination` |
| Wretched Wraith | Creeping Winter | `dungeonsalive:wretched_wraith` |
| Tempest Golem | Howling Peaks | `dungeonsalive:tempest_golem` |
| Ancient Guardian | Hidden Depths | `dungeonsalive:ancient_guardian` |
| Vengeful Heart of Ender | Echoing Void | `dungeonsalive:vengeful_heart_of_ender` |
| Treetop Whisperer | Fauna Faire / Treetop Tangle | `dungeonsalive:treetop_whisperer` |

**Treetop Tangle ist eine kostenlose Fauna-Faire-Mission.** Die Dschungelgestaltung macht den Treetop Whisperer nicht automatisch zu einem kostenpflichtigen Jungle-Awakens-Inhalt. Mojangs Missionsbeschreibung bestätigt den Boss und beschreibt Ranken, Gift sowie Leapleaf-Begleiter. Quelle: [Treetop Tangle](https://www.minecraft.net/en-us/article/treetop-tangle).

Tempest Golem braucht für eine näher am Original liegende Umsetzung seine stationäre Säulenbegegnung; der gegenwärtige Prototyp bewegt sich. Ancient Guardian benötigt eine ausgearbeitete Unterwasserbegegnung. Die Ender-Herzen besitzen nun individuelle Körperformen; ihre vollständigen Originalphasen bleiben offen. Die bisherige Familiengeometrie ist inzwischen durch ausgewählte Reference-/Builtin-Modelle ersetzt. Die Angriffe teilen weiterhin serverseitige Bausteine; ein vollständiger Originalport aller individuellen Phasen ist damit nicht abgeschlossen. Die konkreten Modell- und Phasenziele stehen in [ANIMATION_PRODUCTION.md](ANIMATION_PRODUCTION.md).

## Zwölf Miniboss-/Powerful-Typen aus Dungeons I

| Typ | Stand in diesem Projekt |
| --- | --- |
| Drowned Necromancer | Eigener Prototyp; Drowned-Anatomie und Unterwasserbewegung noch offen |
| Elder Guardian | Vanilla-Gegenstück `minecraft:elder_guardian`; keine eigene Dungeons-Begegnung |
| Enderman | Vanilla-Gegenstück `minecraft:enderman`; keine eigene Dungeons-Begegnung |
| Endersent | Eigener Prototyp |
| Evoker | Vanilla-Gegenstück `minecraft:evoker`; keine eigene Dungeons-Begegnung |
| Ghast | Vanilla-Gegenstück `minecraft:ghast`; keine eigene Dungeons-Begegnung |
| Illusioner | Eigener Prototyp trotz vorhandenem Vanilla-Grundtyp |
| Pillager Raid Captain | Eigener Prototyp; Captain-Variante des Pillagers |
| Redstone Golem | Eigener Prototyp |
| Skeleton Horseman | Eigener zusammengesetzter Modellprototyp |
| Vindicator Raid Captain | Eigener Prototyp; Captain-Variante des Vindicators |
| Wildfire | Eigener Prototyp; individuelle Schildzerstörung und Originalphasen offen |

Quelle der Einordnung: [Mobübersicht](https://minecraft.wiki/w/Dungeons%3AMob), [Powerful-Mobs](https://minecraft.fandom.com/wiki/Minecraft_Dungeons%3APowerful), [Wildfire](https://minecraft.wiki/w/Dungeons%3AWildfire). Mojang beschreibt die Einführung der Raid Captains im [Hidden-Depths-Update](https://www.minecraft.net/en-us/article/hidden-depths-dlc-coming-to-dungeons). Die beiden Captain-Rollen zählen als Minibossvarianten; aus einem Banner entsteht keine zusätzliche Spezies. Schwere Gegner wie Leapleaf oder Squall Golem werden ebenfalls nicht allein wegen ihrer Größe zu Hauptbossen erklärt.

## Alle 26 benannten Ancient-Varianten

Ancients sind besondere Varianten mit eigener Begegnung, Dienern und Modifikatoren. Ihre Aufnahme in diese Tabelle erzeugt noch keine Ancient-Hunt-Mechanik. Der allgemeine Mechanismus wird von [Mojang](https://www.minecraft.net/en-us/article/dungeons-dev-blog---adjusting-endgame) beschrieben; Namen und Zuordnungen stammen aus dem [Ancient-Index](https://minecraft.wiki/w/Dungeons%3AAncient) und den im JSON pro Eintrag verlinkten Typseiten.

| Ancient | Grundtyp |
| --- | --- |
| ????? | Mooshroom |
| Abominable Weaver | Spider |
| Abyssal Eye | Guardian |
| Ancient Terror | Wraith |
| Barrage | Skeleton |
| Cursed Presence | Vanguard |
| First Enchanter | Enchanter |
| Frostwarden | Geomancer |
| Grim Guardian | Zombie |
| Haunted Caller | Necromancer |
| Oozing Menace | Slime |
| Pestilent Conjurer | Witch |
| Scuttling Torment | Silverfish |
| Solemn Giant | Pillager |
| The Seeking Flame | Vex |
| The Swarm | Snareling |
| The Tiny Scourge | Baby Zombie |
| The Tower | Chicken Jockey Tower |
| The Unending | Blaze Spawner |
| Thundering Growth | Leapleaf |
| Unbreakable One | Redstone Golem |
| Unstoppable Tusk | Hoglin |
| Vengeful Mariner | Trident Drowned |
| Vigilant Scoundrel | Royal Guard |
| Watcher of the End | Watchling |
| Windbeard | Goat |

Die fünf Fragezeichen sind der dokumentierte Name des Mooshroom-Ancient. Dafür verwendet der Katalog den stabilen Schlüssel `mcd:five_question_marks`. Windbeard gehört zur Ziege; The Unending zum Blaze Spawner. Diese Zuordnungen lassen sich nicht zuverlässig aus den Namen erraten.

## Normale Mobs, Begleiter und lebendige Welt

Die vollständigen Familien stehen im JSON: Illager mit Ausrüstung, Untote, Spinnen, Nether-Gegner, Enderlings, Pflanzengegner, Konstrukte, Wasserwesen, neutrale Tiere, beschworene Verbündete und kosmetische Begleiter. Erfasst sind Hauptspiel, sechs DLC-Inventare, Tower, saisonale Ergänzungen, Captain-/Ancient-Varianten und ausdrücklich getrennte Angriffshelfer. `Leapleaf` ist der benutzte Spielname; ein internes oder fremdes Mod-ID-Fragment `leaper` wird nicht zum zusätzlichen Mob.

Die drei Enchanted-Sheep-Farben besitzen eigene Fähigkeitsrollen. Die sechs benannten Endersent-Augen sind verknüpfte Varianten. Armored-Typen erhalten einen eigenen Referenzeintrag, ihre einzelnen Rüstungsstufen werden vor einer endgültigen Modellproduktion noch pro Typ geprüft. Eine Multiplikation aller Rüstungen, Verzauberungen, Größen und Schwierigkeitsgrade würde den Umfang künstlich aufblasen.

Für eine lebendige Sandbox müssen auch Ruhe, Orientierung, Fressen/Schnüffeln, Flucht, Landen und Tageszeitverhalten implementiert werden. Diese Zustände sind bereits als eigene Produktionsziele im Katalog eingetragen. Noch fehlende Tiere werden dadurch nicht als vorhanden ausgegeben. Eine spätere Region sollte passende, begrenzte Populationen erhalten; für Vögel und interaktive Insekten reichen Dekopartikel allein nicht aus.

## Händler und Dorfbewohner

Dungeons I enthält sieben Lagerhändler: Blacksmith, Enchantsmith, Gift Wrapper, Luxury Merchant, Mystery Merchant, Piglin Merchant und Village Merchant. Die drei Tower-Händler Gildsmith, Powersmith und Uniquesmith bleiben gesonderte Rollen. Villager, Tower Keeper und Captured Blacksmith ergänzen die NPC-Einträge. Entfernte Händlerformen wie Wandering Trader und Trader Llama sind außerhalb des aktiven Inventars. Quellen stehen an den jeweiligen JSON-Zeilen; die Händlerüberarbeitung beschreibt [Mojangs September-Beitrag](https://www.minecraft.net/en-us/article/dungeons-september-dev-blog).

Sechs der Lagerrollen sind als echte zusätzliche Vanilla-Villager-Professionen implementiert. Sie verwenden normale Minecraft-Angebote und eigene Arbeitsposen. Upgrade-Einlagerung, Verzauberungs-Neuwürfeln und Gegenstandstransfer zwischen Spielern fehlen weiterhin. Piglin Merchant und Tower-Händler sind keine der sechs neuen Professionen. Die Details sowie alle **13 Vanilla-Berufsanimationen** stehen in [VILLAGERS_AND_ANIMATIONS.md](VILLAGERS_AND_ANIMATIONS.md).

## Dungeons II: eigener, belegter Bestand

Die aktuelle [Bossliste](https://minecraft.wiki/w/Dungeons_II%3ABoss) nennt elf Typen: Copper Monstrosity, Monarch, Monstrous Opus, Mosstrosity, Prime Enchanter, Redstone Monstrosity, Sculk Monstrosity, Soul Corrupted Witch, Storm Generator, Supreme Evoker und Twisted Warden. Als sechs Minibosse sind Dartback, Enderman, Evoker, Harmonizer, Warden und Witch erfasst. **Für diese II-Begegnungen existieren in diesem Projekt noch keine eigenen Implementierungen.** Ein gleichnamiger I-Prototyp belegt keine fertige II-Version.

Mojangs [Startbeitrag](https://www.minecraft.net/en-us/article/minecraft-dungeons-ii-is-live) bestätigt Prime Enchanter, Supreme Evoker und Grand Illusioner als High Council sowie die Sift-Dimension. Grand Illusioner ist im Katalog eine belegte Antagonisten-/Filmrolle; daraus wird kein zusätzlicher spielbarer Boss abgeleitet. Monarch Echo ist ein beschworener Klon. Storm Generator I–III sind Varianten eines Grundtyps. Quellen: [Monarch](https://minecraft.wiki/w/Dungeons_II%3AMonarch), [Storm Generator](https://minecraft.wiki/w/Dungeons_II%3AStorm_Generator), [II-Entityübersicht](https://minecraft.wiki/w/Template%3ANavbox_Dungeons_II_entities).

Der II-Bestand enthält außerdem Sifter, Sculker, Sift-Tiere, Soul-Corrupted-Varianten, sechs Begleitertypen sowie Händler-/Dorfbewohnerrollen wie Beekeeper, Candlemaker, Caretaker, Woodcutter und Wolf Tamer. Soul Corrupted Mucked ist in der lokalen Howling-Woods-Liste belegt, fehlt aber im allgemeinen Corrupted-Index; diese Abweichung steht ausdrücklich an der Zeile. Als ungenutzt gekennzeichnete Typen wie Charred, Scorched, Cheesy Cube und Soul Corrupted Necromancer werden nicht als aktive Spielinhalte ausgegeben. Quellen: [Sift](https://minecraft.wiki/w/Dungeons_II%3AThe_Sift), [Soul Corrupted](https://minecraft.wiki/w/Dungeons_II%3ASoul_Corrupted), [Howling Woods](https://minecraft.wiki/w/Dungeons_II%3AHowling_Woods), [II-Mobübersicht](https://minecraft.wiki/w/Dungeons_II%3AMob).

## Quellenstatus und sichere Weiterarbeit

`primary_confirmed` bedeutet, dass mindestens Name oder Rolle durch einen verlinkten Erstveröffentlichungsbeleg gestützt werden. Das zertifiziert nicht automatisch jedes Modellmaß oder jede Angriffssequenz. `secondary_catalogued` bezeichnet die Community-Wiki-Referenz und verlangt vor einer originalnahen Detailumsetzung weitere Einzelreferenzen oder eigene Spielbeobachtung. Die Animationslisten sind **unsere Entwurfsanforderungen**, keine extrahierten Animationsdateien.

`mcd:` und `mcd2:` sind Dokumentationsschlüssel. Nur `runtime_id` beziehungsweise `profession_id` verweist bei implementierten Prototypen auf echte IDs dieses Projekts. `vanilla_equivalent` benennt einen tatsächlich passenden Minecraft-Grundtyp; ein Necromancer wird nicht durch ein beliebiges Skelett als „fertig“ markiert. Der Status `vanilla_counterpart` bedeutet allein, dass ein Grundtyp vorhanden ist. Über beide Spiele gibt es 80 solche Referenzzeilen; sie sind keine 80 zusätzlich programmierten Entities.

Die Liste ist ein breites, quellenbezogenes Produktionsinventar. Sie ist kein verifizierter Export sämtlicher Originalregistrierungen oder kommerzieller Dateien. Ausgeschlossen sind Arcade-exklusive Inhalte, ungenutzte/beta Inhalte, reine Romanfiguren, Heldenskins und gewöhnliche Projektile. Offen bleiben genaue Rüstungsstufen, einige Farb-/Größenvarianten, neu hinzukommende II-Inhalte und die vollständige Abnahme von Originaltreue und Animationsqualität. Der aktuelle native Modellprüfstand und historische Berufs-Vorschauprüfungen sind getrennt dokumentiert in [VALIDATION.md](VALIDATION.md). Eigenständige fremde Mods können weitere Inhalte bereitstellen; der konkrete Versions- und Registry-Abgleich steht in [DUNGEONS_CONTENT_AUDIT.md](DUNGEONS_CONTENT_AUDIT.md).
