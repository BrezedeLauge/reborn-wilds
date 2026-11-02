# Dorfbewohner, Händler und Arbeitsanimationen

Stand: **2. November 2026**, **0.4.0-alpha**, Minecraft Java **1.21.1** / NeoForge **21.1.265**. Die sechs zusätzlichen Dorfbewohnerberufe und ihre Arbeitsgesten bleiben erhalten. Hinzu kommen elf eigenständige Dungeons-Händler, 41 weitere friedliche Kreaturen und ein befreibarer Schmied. Dieses Dokument beschreibt die Implementierung; verbindliche Testergebnisse stehen in [VALIDATION.md](VALIDATION.md).

## Sechs zusätzliche Berufe

Die Händler sind **echte Berufe normaler Minecraft-Dorfbewohner**, keine umbenannten Standbilder und keine unabhängig gespeicherten NPC-Kopien. Jeder Beruf besitzt einen eigenen herstellbaren Arbeitsplatz, eine registrierte POI und Angebote auf allen fünf Berufsstufen. Minecraft übernimmt Arbeitsplatzsuche, normales Handeln, Aufstieg, Nachfüllen am Arbeitsplatz und Spielstandspeicherung. Vorhandene Vanilla-Berufe und Arbeitsplatzblöcke bleiben erhalten.

| Beruf | Profession-ID | Arbeitsplatz | Herstellung: Werkbank plus … | Beispiele für den Handel |
| --- | --- | --- | --- | --- |
| Dorfhändler | `dungeonsalive:village_merchant` | `merchant_counter` | Smaragd | Brot, Pfeile, Laternen, Leinen, Sattel; Windhorn auf Meisterstufe |
| Luxushändler | `dungeonsalive:luxury_merchant` | `luxury_counter` | Goldbarren, Amethystscherbe | Goldkarotten, Uhr, Namensschild, goldener Apfel, Diamantausrüstung |
| Mysteriöser Händler | `dungeonsalive:mystery_merchant` | `mystery_counter` | Enderperle, violetter Farbstoff | Unterschiedliche Kuriositäten je erzeugtem Angebot, später Windhorn und Diamantwerkzeug |
| Dungeons-Schmied | `dungeonsalive:blacksmith` | `blacksmith_station` | Eisenblock, Kohle | Eisenwerkzeug, Schild, Armbrust, Amboss, Diamantwerkzeug |
| Geschenkverpacker | `dungeonsalive:gift_wrapper` | `gift_wrapping_table` | Papier, Faden, roter Farbstoff | Truhen, Rahmen, Kerzen, Namensschild, Endertruhe, verzierter Krug |
| Verzauberungsschmied | `dungeonsalive:enchantsmith` | `enchantsmith_table` | Buch, Lapislazuli, Amethystscherbe | Bücher und definierte verzauberte Bücher, beispielsweise Haltbarkeit III und Behutsamkeit |

Alle sechs Rezepte sind formlos. Die vollständige ID eines Arbeitsplatzes beginnt ebenfalls mit `dungeonsalive:`. Die Blöcke stehen im Kreativinventar unter funktionalen Blöcken. Ihre niedrigere Tischform ist absichtlich passierbar wie ein Arbeitsplatz, besitzt eine Kollisionsbox und lässt sich wieder abbauen. Sie ersetzen keine bestehenden Weltblöcke automatisch.

Es sind **60 konkrete Angebote** vorgesehen: zwei pro Stufe und Beruf. Kauf- und Verkaufsangebote verwenden die normalen serverseitigen `MerchantOffer`-Transaktionen. Der Preis, die begrenzte Zahl an Nutzungen und die Vorschau werden durch Minecraft dargestellt. Beim mysteriösen Händler entsteht eine zufällige Auswahl beim Erzeugen des Angebots; der Spieler sieht das konkrete Ergebnis **vor** dem Bezahlen. Er kauft keine verdeckte Zufallskiste.

### Originalrolle und spielbare Umsetzung

Mojangs September-Update beschreibt Dorfhändler, Luxus- und Rätselhändler, Geschenkverpacker und Schmied. Der Originalschmied verbessert Ausrüstung, der Geschenkverpacker überträgt Gegenstände an Mitspieler. Der später ergänzte Enchantsmith kann Verzauberungsoptionen neu auswürfeln. Quellen: [Minecraft Dungeons September Update](https://www.minecraft.net/en-us/article/dungeons-september-dev-blog), [New Dungeons DLC and more on September 8](https://www.minecraft.net/tr-tr/article/new-dungeons-dlc-and-more-september-8), [Fauna Faire is open](https://www.minecraft.net/en-us/article/fauna-faire-open).

Die sechs **Minecraft-Sandbox-Berufe** verwenden gewöhnlichen Handel. Davon getrennt bieten die eigenständigen Blacksmith-, Powersmith- und Enchantsmith-Entities inzwischen echte Reparatur, Waffenränge und die gezielte Erhöhung eigener Verzauberungen an. Diese Dienste behalten die Komponenten des eingelegten Stücks und ändern nur die jeweils bezahlte Eigenschaft. Sie sind eigene Minecraft-Adaptionen: ein langfristiger Original-Dungeons-Upgradeauftrag, zufälliges Neuwürfeln vorhandener Verzauberungen und ein Geschenktransfer-Menü zwischen zwei Spielern fehlen weiterhin.

Piglin Merchant, Gildsmith, Powersmith, Uniquesmith und Tower Keeper gehören nicht zu diesen sechs Dorfbewohnerberufen. Sie sind inzwischen zusammen mit sechs weiteren Rollen als eigenständige Händler-Entities implementiert; diese zweite Gruppe wird im nächsten Abschnitt beschrieben. Dungeons-II-NPCs wie Woodcutter, Beekeeper und Wolf Tamer bleiben eigenständige spätere Rollen. Ein Katalogeintrag registriert noch keinen Beruf.

## Elf eigenständige Dungeons-Händler

Zusätzlich zu den Berufen gibt es **elf registrierte `DungeonsMerchant`-Entities** mit ihren eigenen, aus den auditierten ReCrafted-Quellen übernommenen Modellen. Sie erben Minecraft-Handel von `AbstractVillager`, besitzen echte Handelsangebote und bleiben als Entities im Spielstand erhalten. Ein normaler Dorfbewohner kann daneben weiterhin einen der sechs Berufe annehmen. Beispielsweise bezeichnen die Entity-ID und die Berufs-ID `dungeonsalive:blacksmith` Einträge in zwei verschiedenen Minecraft-Registries.

Händler mit `dungeonsalive_camp_npc` erhalten einen [gespeicherten Lageralltag](LIVING_WORLD.md): begrenzte Tageswege, Heimkehr bei Nacht/Regen und kurze Flucht nach akzeptiertem Schaden. Ein aktiver Handel pausiert die normalen Wege; Schaden beendet nur das tatsächlich zu diesem Händler gehörende offene Menü. Unmarkierte Händler bleiben bei ihrer bisherigen KI. Die vier echten `merchant_routine`-Serverfälle bestehen in der geprüften Baseline `011f3b80…`; sie prüfen unter anderem wirkliche Heimkehr, NBT, Handelsunterbrechung und endliche Flucht. [Artefaktbezogener Nachweis](../reports/living-expansion-validation.json). Eine grafische Gesamtprüfung und der verbindliche M1-Gasthaus-/Besucherablauf sind dadurch nicht abgenommen.

| Eigenständiger Händler | Entity-ID | Aktuelle Handelswaren |
| --- | --- | --- |
| Schmied | `dungeonsalive:blacksmith` | Eisenspitzhacke, Eisenaxt, Amboss |
| Geschenkeverpacker | `dungeonsalive:gift_wrapper` | Bündel, Papier, Truhen |
| Mysteriöser Händler | `dungeonsalive:mystery_merchant` | Enderperlen, Leuchttintenbeutel, Hasenpfote |
| Dorfhändler | `dungeonsalive:village_merchant` | Brot, Laternen, Leinen |
| Luxushändler | `dungeonsalive:luxury_merchant` | Diamant, goldener Apfel, Amethystscherben |
| Piglin-Händler | `dungeonsalive:piglin_merchant` | Goldbarren, weinender Obsidian, Quarz |
| Verzauberungsschmied | `dungeonsalive:enchantsmith` | Erfahrungsfläschchen, Lapislazuli, Bücherregale |
| Turmwächter | `dungeonsalive:tower_keeper` | Gerüste, Leitern, Uhr |
| Kraftschmied | `dungeonsalive:powersmith` | Eisenbrustplatte, Schild, Eisenschwert |
| Einzigartigkeitsschmied | `dungeonsalive:uniquesmith` | Fernrohr, Armbrust, Kompass |
| Vergoldungsschmied | `dungeonsalive:gildsmith` | Goldkarotten, Goldblock, Goldstiefel |

Jede dieser Entities bietet derzeit drei konkrete Käufe gegen Smaragde an, zusammen **33 Angebote**. Die Transaktion läuft serverseitig über die gewöhnlichen `MerchantOffer`- und Handelsmenü-Mechanismen: Das Menü zeigt Ergebnis und Preis vor dem Kauf, verbraucht den Preis und zählt die Angebotsnutzungen. Jedes Angebot hat zunächst zwölf Nutzungen. Die Händler füllen den Vorrat regelmäßig nach; nach einer Nachfüllung liegt der nächste Zeitpunkt 24.000 Spielticks später. Eine laufende Unterhaltung wird dabei nicht unterbrochen. Angebote, Nutzungsstände und der nächste Nachfüllzeitpunkt werden gespeichert. Diese eigenständigen Camp-Händler benötigen dafür keine Dorf-POI und besitzen derzeit keine Berufsstufen.

**Rechtsklick mit der Haupthand** öffnet beim Blacksmith, Powersmith und Enchantsmith den jeweiligen Dienst; dessen **Waren**-Aktion führt zu den normalen Angeboten. Andere Rollen öffnen unmittelbar das echte Minecraft-Handelsfenster. **Schleichen + Rechtsklick** öffnet das bestehende Questjournal. Eine Interaktion und ein abgeschlossener Handel starten die synchronisierte, 30 Ticks lange Begrüßungsgeste. Der Server übermittelt ihren Startzeitpunkt; der Client blendet die eigens geschriebene Geste in das jeweilige Quellenmodell ein. Sie erzeugt weder Ware noch eine fingierte Angriffshandlung. Im Feldführer lässt sich dieselbe Geste an einem getrennten Vorschauobjekt ansehen.

Zusätzlich zu den Waren besitzt **Blacksmith** eine bezahlte vollständige Reparatur haltbarer Ausrüstung. **Powersmith** erhöht passende Dungeons-Waffen bis Rang III, je Rang um fünf Prozent Schaden. **Enchantsmith** erhöht eine ausgewählte passende eigene Verzauberung bis III. Das Menü zeigt die konkrete Vorschau und den Smaragdpreis; erst die bestätigte serverseitige Zahlung ändert das eingelegte Stück. Veraltete Angebotsrevisionen und wiederholte Pakete können kein zweites Ergebnis erzeugen. Beim Schließen, Entfernen oder Schaden werden Einlagen zurückgegeben. Fünf native `merchant_services`-Fälle bestehen in derselben Baseline. Werte und genaue Grenzen: [Ausrüstung und Seelen](GEAR_AND_SOULS.md#schmiededienste-und-verzauberungen-im-weitergeführten-quellstand).

Vergoldung, Umwandlung zu einzigartiger Ausrüstung, Dungeons-Turmprogression, Multiplayer-Geschenktransfers und zufälliges Neuwürfeln sind weiterhin offen. Die Berufsvariante des Enchantsmith verkauft unabhängig vom Entity-Dienst verzauberte Bücher. Persistente Werkstattaufträge mit Materialreservierung, sichtbarer Arbeit und späterer Abholung gehören ebenfalls noch nicht zu diesen unmittelbaren Transaktionen.

Dienstimplementierung: [`MerchantServices.java`](../src/main/java/de/laugenbreze/dungeonsalive/merchant/MerchantServices.java), [`MerchantServiceMenu.java`](../src/main/java/de/laugenbreze/dungeonsalive/merchant/MerchantServiceMenu.java), [`MerchantServicePolicy.java`](../src/main/java/de/laugenbreze/dungeonsalive/merchant/MerchantServicePolicy.java).

Implementierung: [`DungeonsMerchant.java`](../src/main/java/de/laugenbreze/dungeonsalive/entity/peaceful/DungeonsMerchant.java), [`PeacefulRenderers.java`](../src/main/java/de/laugenbreze/dungeonsalive/client/entity/PeacefulRenderers.java), [`PeacefulProfiles.java`](../src/main/java/de/laugenbreze/dungeonsalive/entity/peaceful/PeacefulProfiles.java). Herkunft, Änderungen und Lizenz der verwendeten Modelle stehen in [`cem-extra-model-imports.json`](../design/cem-extra-model-imports.json) sowie [`third_party/recrafted_mobs`](../third_party/recrafted_mobs/).

## Befreibarer Schmied

`dungeonsalive:captured_blacksmith` ist eine gesonderte zivile Entity. Ein tatsächlicher Rechtsklick befreit ihn, speichert den Retter und startet eine kurze eigene Geste. Anschließend folgt er diesem Spieler; derselbe Spieler kann zwischen Folgen und Warten wechseln. Andere Spieler können ihn nicht übernehmen. Er verkauft keine zusätzlichen Waren und wird nicht automatisch zu einem Upgrade-Dienst. Im optionalen [Gefangenenlager](DUNGEONS_CAMPS.md) ist ein solcher Schmied enthalten. Die Blöcke des Käfigs werden durch die Befreiung nicht entfernt.

## 35 Begleiter, vier Wildtiere und zwei Schlüsselgolems

Die aktuelle Registrierung enthält **53 friedliche Entity-Typen insgesamt**. Neben den elf Händlern sind das die folgenden 41 Kreaturen und ein ziviler gefangener Schmied; gezählt werden die Einträge in `PeacefulProfiles`, einschließlich bewusst getrennter Varianten.

| Gruppe | Anzahl | Enthaltene Beispiele und Besonderheiten |
| --- | ---: | --- |
| Begleiter (`PET`) | 35 | Kleine Kühe und Schweine, Mini-Abominations und Ministrositäten, Vögel, Igel, Maulwurf, Waschbär, Panda, Stinktier, Kaninchen, Katze, Zicklein, Schildkröte, drei farbige Schafe; auch Wächtergeist und Seelenmagier sind aktuell dieser friedlichen Gruppe zugeordnet |
| Wildtiere (`WILDLIFE`) | 4 | Wollkuh, Sparschwein, Geier, Irrlicht |
| Schlüsselgolems (`KEY_GOLEM`) | 2 | Goldener Schlüsselgolem und Diamantschlüsselgolem |
| Händler (`MERCHANT`) | 11 | Die eigenständigen Händler aus der Tabelle oben |
| Bewohner (`CIVILIAN`) | 1 | Gefangener Schmied mit serverseitiger Befreiung, gespeichertem Retter und Folgen/Warten |

Begleiter und Schlüsselgolems lassen sich durch Interaktion einem Spieler zuordnen. Weitere Interaktionen desselben Spielers schalten zwischen Folgen und Warten um. Besitzer-UUID und Wartezustand überstehen Speichern und Laden; andere Spieler können sie dadurch nicht übernehmen oder umschalten. Katze und auf Fuchsmodellen beruhende Begleiter setzen beim Warten den tatsächlichen Sitzstatus, der ihre Quellen-Sitzposen aktiviert. Andere Kreaturen warten in ihrer vorhandenen Ruhepose. Wildtiere erhalten diese Besitzmechanik nicht.

Alle diese Kreaturen haben eine friedliche KI ohne Kampfzielauswahl. Fliegende Arten benutzen Flugnavigation; der kleine Tintenfisch benutzt Wassernavigation und kann unter Wasser bleiben. Die Rubinschildkröte benutzt amphibische Navigation und ertrinkt nicht. Lauf- und Flugbewegungen werden von den wirklichen Entity-Zuständen gespeist. Die drei farbigen Schafe besitzen zusätzlich die separat texturierte Außenhaut aus dem Quellenmodell `sheep_wool.jem`, einschließlich seiner eigenen 64×64-Texturbelegung und Bewegungsausdrücke. Katze und geflecktes Ferkel verwenden das bewusst gewählte normale Katzen- beziehungsweise Schweine-CEM-Modell des Pakets mit dem passenden Skin; die zufälligen Artenvarianten des ursprünglichen Ressourcenpakets werden dabei nicht übernommen. Zicklein und Rubinschildkröte verwenden passende installierte Minecraft-Modellfabriken mit den auditierten ReCrafted-Skins. Minecraft-Modell- und Grundtexturdateien werden dafür nicht mitgeliefert.

Die Schlüsselgolems bedienen inzwischen zwei eigene herstellbare Schlüsseltore mit serverseitiger Prüfung von Besitzer, Schlüsselart, Entfernung und Sicht. Die fünf nativen `key_gates`-Fälle bestehen in derselben Baseline. Dies ist eine eigene Minecraft-Adaption, kein vollständiger Original-Dungeons-Türport. Die Namen der farbigen Schafe bedeuten gegenwärtig keine zusätzlichen Feuer-, Gift- oder Tempo-Kampffähigkeiten. Wächtergeist und Seelenmagier sind hier friedliche Begleiter mit Quellenmodell; ihnen wird keine ursprüngliche Beschwörungs- oder Kampfmechanik zugeschrieben. Zugang für die Entwicklung bieten registrierte Spawneier im Kreativinventar und `/summon dungeonsalive:<id>`; eine fertig bestückte Dungeons-Kampfwelt entsteht durch diese Registrierung allein nicht.

Implementierung: [`DungeonsCreature.java`](../src/main/java/de/laugenbreze/dungeonsalive/entity/peaceful/DungeonsCreature.java), [`ModPeacefulEntities.java`](../src/main/java/de/laugenbreze/dungeonsalive/entity/peaceful/ModPeacefulEntities.java). Der Feldführer über **B** führt Gegner, friedliche Kreaturen und Händler mit Suche und Gruppenfilter auf. Tiere haben Ruhe- und Bewegungsansichten; Händler zusätzlich die Begrüßung. Die separate Berufsseite behält die Vorschau der **19 Arbeitsgesten aus 13 Vanilla- und sechs zusätzlichen Berufen**.

## Alle 13 regulären Java-Berufe erhalten eine Arbeitsgeste

Die Bewegungen und Modelle sind eigene prozedurale Java-Animationen. Sie verwenden weiche Ein- und Ausblendung, getrennte Arme, eine leichte Gewichtsverlagerung sowie vorhandene Blick- und Laufbewegungen. Die folgenden Gesten laufen beim tatsächlichen Arbeiten am eigenen Arbeitsplatz:

| Vanilla-Beruf | Bewegungsbild | Sichtbare Requisite |
| --- | --- | --- |
| Rüstungsschmied | Kräftiger rhythmischer Bearbeitungsschlag; andere Hand stützt | Eisenbarren |
| Metzger | Kurze Schneid-/Hackbewegung | Eisenaxt als vorläufiges Werkzeug |
| Kartograf | Kleine zeichnende Handbewegung, Blick nach unten | Feder |
| Geistlicher | Kreisende Mischbewegung | Glasflasche |
| Bauer | Sortier-/Erntebewegung am Komposter | Weizen |
| Fischer | Angel/Zubehör prüfen, ruhige Armbewegung | Angel |
| Pfeilmacher | Präzises Bearbeiten eines Pfeils | Pfeil |
| Gerber | Kleine Glätt-/Nähbewegung | Leder |
| Bibliothekar | Buch halten, kleine Blätterbewegung | Buch |
| Steinmetz | Kurzes behauen, leicht vorgeneigt | Eisenspitzhacke als vorläufiges Werkzeug |
| Schäfer | Abwechselnde Handbewegung beim Bearbeiten | Schere |
| Werkzeugschmied | Kürzere, schnellere Arbeitsschläge | Eisenspitzhacke |
| Waffenschmied | Langsames beidarmiges Bearbeiten | Eisenschwert |

Die sechs neuen Berufe besitzen zusätzliche Gesten: Waren sortieren, eine Kostbarkeit präsentieren, eine Kuriosität prüfen, schwere Schmiedeschläge, Papier zusammenlegen sowie über einem Buch arbeiten. Diese Gesten teilen kein einziges unverändertes Bewegungspaar für alle Berufe. Ein eigener Hammer, eine Zange und eine aufwendige Buchgeometrie können die Platzhalter-Requisiten später ersetzen.

Unbeschäftigte Dorfbewohner und Nichtsnutze erhalten die allgemeine ruhige Bewegung und normales Gehen; sie führen keine erfundene Arbeitsgeste aus. Kinder, schlafende und tote Dorfbewohner starten keine Arbeit. Zusätzliche Berufe fremder Mods erhalten allgemeine Bewegung, aber keine anhand eines ähnlich klingenden Namens geratene Berufsanimation.

## Animation folgt tatsächlichem Zustand

Der Client besitzt nicht den vollständigen Vanilla-Dorfbewohner-Brain. Deshalb entscheidet der Server, wann eine Arbeit dargestellt wird. Alle fünf Ticks prüft er bei geladenen Dorfbewohnern:

1. Der Dorfbewohner lebt, ist erwachsen, wach und hat eingeschaltete KI.
2. Seine aktive Tätigkeit ist `WORK`.
3. Seine gespeicherte Arbeitsstelle liegt in derselben Dimension, in einem bereits geladenen Chunk und höchstens 1,8 Blöcke entfernt. Ihre echte POI passt zum Beruf.
4. Seine Bewegungsrate ist klein genug für eine stehende Arbeitsgeste.
5. Vanilla hat in `LAST_WORKED_AT_POI` kürzlich eine tatsächliche Arbeit gespeichert. Ein bloßes Stehen neben einer Werkbank reicht nicht.

Eine solche Arbeitssequenz dauert höchstens 80 Ticks. Handeln und Panik haben Vorrang; Verlassen der Arbeitsstelle, Schlafen oder fehlende Voraussetzungen beenden die Arbeitsdarstellung. Die Animation produziert keine Gegenstände und setzt kein Feld oder Inventar heimlich zurück. Normale Vanilla-Arbeiten, darunter Restocking, werden nicht ersetzt.

Kompakte S2C-Pakete gehen nur an Spieler, die den Dorfbewohner verfolgen, hauptsächlich bei Zustandswechseln. Lange Handels-/Panikzustände bekommen einen begrenzten Auffrischungstakt. UUIDs schützen vor versehentlich wiederverwendeten Entity-Nummern. Der Client begrenzt seinen Cache, blendet Übergänge weich aus und leert alte Zustände beim Weltwechsel. Auf dem Server laden die gemeinsamen Klassen keine Renderer.

### Handbuch-Vorschau

`VillageClient.preview(villager, true)` aktiviert ausschließlich für ein ausdrücklich als Vorschau verwendetes Clientobjekt eine Arbeitsanimation. Sie dient dem 3D-Handbuch; sie gibt einer realen Kreatur weder Beruf noch Gegenstände und meldet keine Arbeit an den Server. `clearPreview` entfernt den Eintrag. Vorschauobjekte dürfen nicht gleichzeitig echte Entities der Welt sein.

## Verhältnis zu Actions & Stuff und Fresh Animations

Actions & Stuff ist die gestalterische Referenz des Nutzers. Es wurden keine Bedrock-Animationen, Modelle oder Texturen daraus importiert. Diese Java-Umsetzung ist ein eigener Prototyp. Ihre Bewegungsqualität muss an echten Spielszenen bewertet werden; der Umfang von Actions & Stuff wird damit nicht als erreicht ausgegeben.

Mit standardmäßiger Einstellung `preferExternalModels=true` übernimmt der Mod **keinen** Vanilla-Villager-Renderer, sobald Entity Model Features beziehungsweise OptiFine installiert ist. Dadurch kann beispielsweise Fresh Animations seinen eigenen Villager-Look bereitstellen. Die sechs Berufe, Stationen und Handelssysteme bleiben dennoch aktiv. Die eigenen Arbeitsanimationen werden in dieser Kombination nicht automatisch in Fresh Animations hineingemischt. Quelle für den externen Modellansatz: [Entity Model Features](https://modrinth.com/mod/entity-model-features).

Die separate Datei `config/dungeonsalive-villagers-client.toml` enthält:

| Option | Standard | Wirkung |
| --- | --- | --- |
| `enabled` | `true` | Eigenen Villager-Renderer verwenden |
| `preferExternalModels` | `true` | Bei EMF/OptiFine den externen Mod zeichnen lassen |
| `workProps` | `true` | Eigene, rein sichtbare Arbeitsgegenstände anzeigen |
| `intensity` | `1.0` | Stärke der zusätzlichen Bewegung zwischen 0 und 1,5 |

Nach Änderungen an `enabled` oder `preferExternalModels` den Client neu starten. Der Rendererwechsel ist bewusst keine unbemerkte Änderung mitten im Spiel. Shader-/Ressourcenpaketkombinationen und andere Mods, die denselben Renderer ersetzen, brauchen weiterhin eine konkrete Kompatibilitätsprüfung.

## Eigene Dateien und Bearbeitung

- `village/VillageContent.java`: Registrierung der Berufe, POIs und Arbeitsplätze.
- `village/MerchantTrades.java`: konkrete Angebote und Preise.
- `village/VillageAnimationEvents.java`: Beobachtung der tatsächlichen Server-KI.
- `village/VillageActivity.java`: unabhängig testbare Zustandsregeln.
- `client/village/AnimatedVillagerModel.java`: eigene Armgeometrie und Bewegungen.
- `client/village/VillagerWorkItemLayer.java`: sichtbare Werkzeuge.
- `tools/generate_village_assets.py`: reproduzierbare eigene Berufskleidung, Tischtexturen, Modelle, Rezepte, Loot und Tags.

Vanilla-Grundtextur, Kleidung der Biome, Hutformen, Karrieremarken und referenzierte Itemgrafiken werden durch Minecraft selbst geladen. Das Paket enthält keine Kopie dieser Spieldateien. Eigene Berufsoverlays und Tischtexturen stehen unter der Projektlizenz.

## Konkrete manuelle Abnahme

Eine Testwelt bei Tag öffnen, einen arbeitslosen erwachsenen Dorfbewohner und einen der sechs Arbeitsplätze bereitstellen. Er muss den Arbeitsplatz regulär annehmen, den richtigen Namen und die eigene Kleidung erhalten und ein gültiges Handelsfenster öffnen. Die Arbeit erst am tatsächlich benutzten Arbeitsplatz beobachten; Bewegung auf dem Weg dorthin soll normales Gehen sein. Kaufen, restocken lassen, speichern und neu laden. Den Arbeitsplatz entfernen und die üblichen Unterschiede zwischen unbenutztem Beruf und bereits gehandeltem Beruf prüfen.

Anschließend mehrere Vanilla-Berufe nebeneinander ansehen: Schmiede dürfen andere Bewegungen als Bibliothekar, Fischer und Kartograf haben. Beim Handeln endet die Arbeitsgeste; bei einer Bedrohung gilt Panik. Schlafen, Kinder, GUI-Skalierung und das Halten vorhandener Handgegenstände gesondert ansehen. Zuletzt mit aktiviertem EMF/Fresh-Animations-Profil prüfen, dass dessen Renderer weiter verwendet wird. Ein Headless-GameTest kann keine optischen Werkzeugpositionen oder Übergänge freigeben.

Für die eigenständigen Händler beispielsweise `/summon dungeonsalive:blacksmith` verwenden. Den Reparaturdienst öffnen und über **Waren** das normale Handelsfenster erreichen, ein sichtbares Angebot mit exakt passender Zahlung kaufen, den verringerten Vorrat nach einem Weltneustart kontrollieren und die echte Begrüßung von einem zweiten Spieler aus beobachten. Schleichen + Rechtsklick muss das Questjournal öffnen. Anschließend einen normalen Dorfbewohner am Schmiede-Arbeitsplatz prüfen, damit beide getrennten Implementierungen nebeneinander funktionieren.

Für Begleiter zuerst Besitz und Warten bei Katze oder rotem Panda prüfen, speichern und neu laden, dann mit einem zweiten Spieler eine unerlaubte Übernahme versuchen. Tintenfisch unter Wasser sowie Schildkröte an Land und im Wasser beobachten. Im Feldführer die Gruppenfilter, Tiere ohne Angriffstaste und Händler mit Begrüßung bei kleiner und großer GUI-Skalierung ansehen. Diese Absätze sind ein Abnahmeplan; sie behaupten keine bereits durchgeführte vollständige visuelle Prüfung aller Varianten.
