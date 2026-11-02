# Dungeons-Ausrüstung und Seelen

Der Quellstand registriert **42 Ausrüstungsitems: zwölf Artefakte einschließlich Windhorn, vierzehn Nahkampfwaffen, vier Fernkampfwaffen und zwölf Rüstungsteile in drei Sets**. Die geprüfte Baseline `011f3b80…` besteht mit **137 JUnit-Fällen und 123 echten nativen GameTests**, einschließlich Ausrüstung, Händlerdiensten, Beschwörungsbelohnungen und Schlüsseltoren. [Artefaktbezogener Nachweis](../reports/living-expansion-validation.json), [Serverlog](../reports/living-world-gametest.log). Die getrennte direkte Java-Test-JAR liegt unter `build/local-check/`; das ist kein neuer Gradle-Release. `build/libs/`, `dist/` und die ursprüngliche Nutzerinstanz sind historische Stände. Die grafische Abnahme des aktuellen Ausbaus bleibt getrennt offen; neue M1-Vogel-/Entenänderungen sind durch diese Baseline nicht geprüft. Dies ist weiterhin kein vollständiger Dungeons-Gegenstandsbestand.

Die Bedienung bleibt Minecraft: Gegenstand in die Hand nehmen, Rechtsklick für Artefakte/Bögen/Armbrüste, gewöhnlicher Angriff für Nahkampfwaffen, normale Rüstungsplätze. **B → Ausrüstung** wechselt zwischen Artefakten, Nahkampf, Fernkampf und Rüstung. Die 41 importierten Ausrüstungsitems besitzen Rezepte und Rezeptfreischaltungen; das bestehende Windhorn-Rezept bleibt erhalten.

## Artefakte

Die folgenden Werte sind bewusst für diese Minecraft-Alpha gewählt. Es sind keine behaupteten exakten Originalwerte aus Dungeons. Ein Herz entspricht zwei Schadenspunkten. Der Server entscheidet über Ziele, Kosten und Abklingzeiten; sichtbare Effekte geben den bestätigten Zustand wieder.

| Registry-Endung | Wirkung | Dauer / Reichweite | Abklingzeit | Seelen |
| --- | --- | --- | --- | --- |
| `boots_of_swiftness` | Schnelligkeit III | 5 Sekunden | 10 Sekunden | keine |
| `death_cap_mushroom` | Eile IV und Schnelligkeit II | 9 Sekunden | 30 Sekunden | keine |
| `iron_hide_amulet` | Resistenz II | 10 Sekunden | 25 Sekunden | keine |
| `shock_powder` | Verlangsamung V und Schwäche II auf sichtbare Gegner | 4 Sekunden, Radius 5 | 15 Sekunden | keine |
| `gong_of_weakening` | Schwäche II, Leuchten und 25 % mehr Schaden durch Spieler | 7 Sekunden, Radius 6 | 20 Sekunden | keine |
| `harvester` | Seelenexplosion mit 12 Magieschaden | Radius 5, sichtbare Gegner | 5 Sekunden | 20 pro Einsatz |
| `corrupted_beacon` | Rechtsklick halten: sichtbarer Seelenstrahl, 6 Magieschaden je Puls | bis 24 Blöcke, Puls alle 10 Ticks | 3 Sekunden nach Beendigung | 3 pro halber Sekunde |
| `lightning_rod` | Anvisierten Gegner und nahe sichtbare Gegner mit 14 Magieschaden treffen | Ziel bis 24 Blöcke, Wirkungsradius 2 | 10 Sekunden | 15 pro Einsatz |
| `soul_healer` | Heilt bis zu 8 Lebenspunkte; bei voller Gesundheit keine Kosten | sofort | 10 Sekunden | 15 pro Einsatz |
| `ghost_cloak` | Schnelligkeit II und Resistenz I | 3 Sekunden | 10 Sekunden | keine |
| `flaming_quiver` | Füllt sieben serverseitige Feuerpfeil-Ladungen auf | nächste sieben Pfeile | 30 Sekunden | keine |

Alle IDs beginnen mit `dungeonsalive:`. Der Blitzstab erzeugt Licht-/Funkenpartikel und Donner, keine brandstiftende Vanilla-Blitzentity. Shock Powder ist eine Verlangsamung mit Schwäche, keine vollständige Ausschaltung fremder KI. Eile ist der normale Minecraft-Effekt; der Pilz implementiert kein separates Dungeons-Angriffssystem.

Flächen- und Strahleffekte wählen feindliche, lebende, nicht verbündete Ziele. Spieler, friedliche Tiere und Kreaturen mit einem eingetragenen Besitzer gehören nicht zu diesen Spezialzielen. Blockwände und aktive Geomancer-Wände unterbrechen den Sichtweg. Strahlabfragen prüfen geladene Bereiche und laden keine unbekannten Chunks nach. Der Blitzstab verbraucht ohne gültiges sichtbares Ziel keine Seelen und startet keine Abklingzeit.

Der Beacon endet bei Loslassen, Gegenstandswechsel oder fehlenden Seelen. Der tatsächliche Stop-Callback setzt die Abklingzeit auch bei einem Hotbarwechsel. Eine kopierte Gegenstands-NBT mit erfundenem Anzeigewert kann keinen Seelenvorrat erzeugen.

Geisterumhang verändert weder Kollision noch Blockzugriff. Der Flammenköcher ersetzt keine Munition: jede tatsächlich erzeugte normale oder spektrale Pfeilentity verbraucht eine Ladung, auch einzelne Multishot-Pfeile. Dreizacke und andere Magiegeschosse zählen nicht. Neue Ladungen füllen auf sieben auf; sie addieren sich nicht. Bei Tod verfallen sie, bei anderen Spieler-Klonvorgängen bleiben sie erhalten. Gespeicherte/erneut geladene Pfeile lösen keine erneute Abbuchung aus. Ein Gegenstands-Tooltip ist nur die Anzeige des serverseitigen Vorrats. Pfeile behalten Vanilla-Brandregeln: Wasser/Regen kann sie löschen; direkte Brandpfeilinteraktionen etwa mit TNT bleiben möglich. Nur die eigene Zusatzexplosion der Explosionsarmbrust erzeugt keinen Blockschaden.

## Ein gemeinsamer Seelenvorrat

Der tatsächliche Vorrat gehört zum Spieler, nicht zum einzelnen Gegenstand. Er liegt serverseitig im persistenten Spielerfeld `DungeonsAliveSouls` und ist auf **100** begrenzt. Wer mindestens ein Seelenartefakt oder das Seelenmesser mitführt, erhält **3 Seelen** für einen ihm zugerechneten feindlichen Kill. Friedliche Tiere, verbündete beziehungsweise besessene Kreaturen und abgebrochene Todesereignisse zählen nicht. Der Kreativmodus kann Artefakte ohne Seelenkosten ausprobieren.

Der weitergeführte Quellstand schließt außerdem zeitlich begrenzte Dungeon-Helfer mit gespeichertem Beschwörer von diesem Seelengewinn aus. Normale Gegner derselben Registry-ID zählen weiterhin. Heartstealer heilt auch beim gültigen direkten Nahkampfkill eines solchen feindlichen Helfers; die Änderung betrifft dessen Seelenbelohnung. Die beiden echten `summoned_rewards`-Fälle bestehen in der oben genannten Baseline; `dist/` bleibt der historische Auslieferungsstand. Siehe [aktuellen Prüfstand](VALIDATION.md).

Beim Tod bleibt die abgerundete Hälfte erhalten; andere Spieler-Klonvorgänge übernehmen den bisherigen Vorrat. Ein Anzeigeabzug `dungeonsalive_souls_display` auf den Artefakten wird regelmäßig vom Server erneuert und dient ausschließlich dem Tooltip. Er wird niemals als Zahlungsquelle eingelesen. Beim Gewinnen von Seelen erscheint außerdem eine kurze Statuszeile.

Eine kleine HUD-Anzeige zeigt den serverseitigen Seelenvorrat, solange ein Seelensammler mitgeführt wird. Flammenköcher-Ladungen bleiben auch nach dem Wegstecken sichtbar, solange welche vorhanden sind. Der Server überträgt Änderungen höchstens alle fünf Ticks ausschließlich an den Besitzer. Menüs, F1, Tod und Zuschauermodus blenden die Anzeige aus. Sie nimmt keine Zahlungsdaten vom Client entgegen. Frei schwebende Original-Seelenobjekte sind noch nicht umgesetzt.

## Waffen

Alle vierzehn Waffen sind reguläre Minecraft-Nahkampfgegenstände auf Diamantbasis mit **1561 Haltbarkeit**, normaler Angriffsaufladung und den üblichen Schwert-/Reparatur-/Verzauberungsregeln. Die Werte in der Tabelle sind der angezeigte Gesamtschaden und die Angriffe pro Sekunde. Zusatzeffekte besitzen eigene serverseitige Pausen.

| Gegenstand | Schaden | Tempo | Eigene charakteristische Wirkung |
| --- | ---: | ---: | --- |
| Schwert | 7 | 1,6 | Normaler Minecraft-Schwungangriff. |
| Entermesser | 6 | 2,0 | Schnellerer Schwungangriff. |
| Dolche | 4 | 3,0 | Schnelle Einzelangriffe ohne Schwung. |
| Gleve | 8 | 1,1 | +0,75 Blöcke Entity-Reichweite, Schwungangriff. |
| Speer | 7 | 1,2 | +1 Block Entity-Reichweite, Einzelangriffe. |
| Großhammer | 11 | 0,7 | +0,6 Angriffsrückstoß, Einzelangriffe. |
| Sicheln | 5 | 2,5 | Schnelle Schwungangriffe. |
| Seelenmesser | 9 | 0,8 | Ermöglicht Seelensammeln ohne Artefakt; gültige direkte Nahkampfkills geben insgesamt 5 statt 3 Seelen. |
| Heartstealer | 9 | 1,0 | Ein feindlicher direkter Nahkampfkill heilt 9 % der maximalen Lebenspunkte des besiegten Gegners, höchstens 6 Lebenspunkte. |
| Stormlander | 10 | 0,8 | Sichtbare weitere Gegner im Radius 3 erhalten 4 Magieschaden; frühestens alle 4 Sekunden. |
| Fighter’s Bindings | 5 | 3,2 | Sehr schneller normaler Minecraft-Nahkampf. Noch keine eigene beidhändige Faustanimation. |
| Sun’s Grace | 7 | 1,3 | Ein feindlicher Nahkampftreffer heilt den Spieler um 2 Lebenspunkte; frühestens alle 5 Sekunden. |
| Frost Scythe | 8 | 1,0 | Verlangsamung II für 4 Sekunden; Auslösung frühestens alle 1,5 Sekunden. |
| Whirlwind | 9 | 1,1 | Sichtbare weitere Gegner im Radius 2,5 erhalten 3 Magieschaden; frühestens alle 2,5 Sekunden. |

Die Reichweiten- und Rückstoßattribute gelten ausschließlich in der Haupthand; sie erweitern keine Blockinteraktion. Dolche/Sicheln verwenden die vorhandene einzelne Handdarstellung, keine neue beidhändige Animation. Das Seelenmesser erhält bei indirekten Kills keinen Bonus und weder für beschworene Helfer noch friedliche/verbündete Ziele Seelen. Alle Werte sind eigene Minecraft-Balance.

Die besonderen Flächen-/Heilwirkungen gelten nicht für passive oder besessene Ziele. Der gewöhnliche direkte Schwerttreffer folgt weiterhin den normalen Minecraft-Kampfregeln. Der Diamond Armored Vindicator führt das tatsächliche Whirlwind-Item; sein Handgegenstand hat Dropchance null. Daraus entsteht keine zusätzliche kostenlose Waffe neben der regulären Beutetabelle.

## Schmiededienste und Verzauberungen im weitergeführten Quellstand

Blacksmith, Powersmith und Enchantsmith öffnen beim gewöhnlichen Rechtsklick ihren eigenen Dienst. Über **Waren** bleiben ihre bisherigen MerchantOffers zugänglich; Schleichen öffnet weiterhin das freiwillige Questjournal. Der gefangene Schmied bleibt ein eigener befreibarer Zivilist.

Links wird genau ein Ausrüstungsteil eingelegt, mittig die angezeigte Menge Smaragde. Rechts erscheint eine nicht entnehmbare Vorschau. Erst **Bezahlen** führt die Änderung aus; das fertige Stück liegt danach links und muss entnommen werden. Name, Haltbarkeit, Verzauberungen und sonstige Komponenten bleiben erhalten, soweit der gewählte Dienst sie nicht gezielt ändert. Bei Schließen, Entfernung oder Schaden werden eingelegte Gegenstände zurückgegeben, bei vollem Inventar am Spieler fallengelassen. Vorschauen sind keine zusätzlichen Gegenstände.

| Dienst | Wirkung | Preis |
| --- | --- | --- |
| Blacksmith | Beschädigte haltbare Ausrüstung vollständig reparieren. | 2 + aufgerundeter beschädigter Anteil × 10 Smaragde. |
| Powersmith | Dungeons-Nahkampfwaffen, Bögen und Armbrüste bis Rang III, je +5 % Waffenschaden. | Nächster Rang: 16 / 32 / 48 Smaragde. |
| Enchantsmith | Eine der sechs folgenden passenden Verzauberungen um eine Stufe bis III erhöhen. | Nächste Stufe: 8 / 16 / 24 Smaragde. |

Der Server prüft die konkrete Menüinstanz, Spieler, Händler, Entfernung, Eingabekomponenten, Preis und Angebotsrevision. Wiederholte oder veraltete Netzpakete können kein zweites Ergebnis kaufen. Ein Pfeil übernimmt den bezahlten Waffenrang beim Abschuss; ein späterer Waffenwechsel ändert ihn nicht. Rangschaden betrifft keine zusätzlichen Artefakt-/Magieschäden.

Die sechs Verzauberungen sind native 1.21.1-Registry-Einträge mit echten ItemEnchantments, Büchern, Amboss- und Zaubertischanbindung. Sie gelten für die vierzehn Dungeons-Nahkampfwaffen; ihre Servereffekte sind eigene Adaptionen. Vanilla-Kompatibilitätsregeln bleiben erhalten.

| Verzauberung | Stufe I / II / III |
| --- | --- |
| Strahlen (`radiance`) | 20 % Chance auf 1 / 2 / 3 Lebenspunkte Heilung nach einem feindlichen Nahkampftreffer; eine Sekunde Pause. |
| Lebensraub (`leeching`) | Direkter feindlicher Nahkampfkill heilt 3 / 6 / 9 % der maximalen Gegnergesundheit, höchstens 2 / 4 / 6 Lebenspunkte. |
| Hingabe (`committed`) | Mit sinkender Zielgesundheit bis zu 10 / 20 / 30 % mehr Nahkampfschaden. |
| Kritischer Treffer (`critical_hit`) | 5 / 10 / 15 % Chance auf 1,75 / 2 / 2,25-fachen Nahkampfschaden. |
| Ketten (`chains`) | Verlangsamung IV für 2 / 3 / 4 Sekunden; höchstens acht sichtbare Gegner in drei Blöcken Umkreis; zwei Sekunden Pause. |
| Wirbel (`swirling`) | 2 / 3 / 4 Magieschaden gegen höchstens acht sichtbare weitere Gegner in drei Blöcken Umkreis; eine Sekunde Pause. |

Friedliche Tiere, Spieler und besessene/verbündete Wesen lösen diese Spezialeffekte nicht aus. Sekundärschaden löst keine neue Effektkette aus. Die fünf nativen Händlerdienst-Fälle und die Verzauberungsregressionen bestehen in der genannten Baseline. Die grafische Abnahme von Menü/HUD und das Zusammenspiel mit zwei realen Clients sind dadurch nicht nachgewiesen.

## Fernkampf

Alle vier Waffen verwenden echte Minecraft-Pfeile, Munitionsverbrauch und Haltbarkeit. Bögen haben 768, Armbrüste 930 Haltbarkeit. Vanilla-Verzauberungen werden über die entsprechenden 1.21-Itemtags unterstützt. Die besonderen Effekte gehören zum abgeschossenen Pfeil, sodass ein Hotbarwechsel sie nicht austauscht.

| Gegenstand | Eigene Minecraft-Adaption |
| --- | --- |
| Kraftbogen (`power_bow`) | 35 % mehr Pfeil-Basisschaden, normale Bogenspannzeit. |
| Versprechen des Jägers (`hunters_promise`) | Ein gültiger Gegnertreffer kann bis zu vier eigene aktive Vanilla-Wölfe in der Nähe auf das Ziel ansetzen. 20 % Chance auf Rückgabe eines bezahlten normalen Pfeils; keine Infinity-/Creative-/Mehrfachrückgabe. Friedliche Dungeons-Begleiter erhalten dadurch keine neue Kampf-KI. |
| Automatische Armbrust (`auto_crossbow`) | Halbe Vanilla-Ladezeit, aufgerundet und mindestens drei Ticks; Quick Charge bleibt berücksichtigt. Ein Schuss erfordert weiterhin Munition. |
| Explodierende Armbrust (`exploding_crossbow`) | Einmaliger Zusatzstoß: bis zu 16 sichtbare weitere Gegner im Radius 2,5 erhalten 6 Magieschaden. Der direkte Pfeiltreffer bleibt Vanilla. Keine Blockexplosion; diese Armbrust akzeptiert keine Feuerwerksmunition. |

Bogenmodelle enthalten drei Spannstufen, Armbrüste zusätzlich geladenen Pfeil-/Feuerwerkszustand aus den Quellen. Die Feuerwerksvorlage der explodierenden Armbrust bleibt eine erhaltene Quellenvariante; diese Munition ist in ihrer Mechanik gesperrt.

## Tragbare Rüstung

Jedes Set besteht aus Helm, Bruststück, Hose und Stiefeln. **Nur vier passende angelegte Teile aktivieren den Setbonus.** Im Inventar gehaltene Teile oder kopierte Anzeigedaten reichen nicht. Die Zahlen sind eigene Minecraft-Balance.

| Set | Rüstung Helm/Brust/Hose/Stiefel | Reparatur | Vollständiger Setbonus |
| --- | --- | --- | --- |
| Wolf (`wolf_*`) | 2 / 6 / 5 / 2 | Leder | 20 % mehr direkter Nahkampfschaden gegen Gegner. |
| Spinne (`spider_*`) | 2 / 5 / 4 / 1 | Faden | 25 % mehr Angriffstempo; 6 % des angewandten Nahkampfschadens gegen Gegner heilen, maximal zwei Lebenspunkte je zehn Ticks. |
| Kampfroben (`battle_robes_*`) | 1 / 4 / 3 / 2 | Weiße Wolle | 15 % mehr direkter Nahkampfschaden gegen Gegner; 20 % kürzere Artefakt-Abklingzeit, einschließlich Windhorn und beendetem Beacon. |

Heilung und Schadenboni schützen passive, verbündete und besessene Ziele. Die Rüstungsmodelle übernehmen die konkreten Community-Gecko-Quader, UVs und Atlanten in einen eigenen Minecraft-Humanoid-Adapter. Wolf hat in der Quelle leere Stiefelknoten; für einzeln tragbare Stiefel wird seine Beingeometrie am Knöchel aufgeteilt. Diese Slot-Anpassung ist dokumentiert und benötigt noch echte Clientprüfung. Vanilla-Körperposen werden übernommen; Original-Dungeons-Spieleranimationen sind damit nicht implementiert.

## Beschaffung und Quests

Alle 41 importierten Ausrüstungsgegenstände lassen sich herstellen. Geformte Rezepte und zugehörige Freischaltungen liegen unter `src/main/resources/data/dungeonsalive/recipe/` und `advancement/recipes/`. Die konkret ausgelieferten Zutaten stehen in diesen JSON-Dateien und sind zusätzlich im Importgenerator nachvollziehbar. Das Rezeptbuch ist der Einstieg im Spiel.

Sechs Waffen sind außerdem besondere Bossbeute, jeweils mit **35 % Chance bei einem dem Spieler zugerechneten Kill**:

| Boss | Zusätzliche mögliche Waffe |
| --- | --- |
| Redstone-Monstrosität | Heartstealer |
| Tempest Golem | Stormlander |
| The Nameless One | Sun’s Grace |
| Wretched Wraith | Frost Scythe |
| Jungle Abomination | Whirlwind |
| Mooshroom-Monstrosität | Fighter’s Bindings |

Vier zusätzliche Grundquests werden nach dem abgeholten Abschluss von **Ein Ort zum Bleiben** freigeschaltet: `relic_supplies`, `relic_soulcraft`, `relic_ironhide` und `relic_storm`. Sie führen zu Stiefeln, Harvester, Amulett beziehungsweise Blitzstab. Die Sammelziele prüfen den Vorrat, nehmen die Materialien aber nicht weg. Annahme und Belohnungsabholung bleiben ausdrücklich getrennt; nichts startet automatisch. Mit diesen vier Aufgaben enthält das Basispaket **29 Quests**, mit dem gesonderten Enchanter-Integrationspack 30.

## Konkrete Herkunft und Weiterarbeit

Die Kunst stammt aus dem veröffentlichten MIT-Projekt [Dungeons Gear](https://github.com/Infamous-Misadventures/Dungeons-Gear), fixiert auf Revision `9ba0075c5b2f13ef54ec914ad1327a853ecdce6f`. 120 konkrete Quelldateien sind als unveränderte Snapshots erhalten: Itemmodelle samt Fernkampfzuständen, passende PNGs sowie drei getragene Rüstungs-Geometrien und Atlanten. Der Itemimport liefert 117 Modell-/Texturdateien; der getrennte Rüstungsgenerator erzeugt den Java-Adapter. Itempixel, UVs, Display-Transformationen und Itemquader bleiben erhalten. Der Itemimport ändert Ressourcen-Namensräume beziehungsweise die lokalen IDs `fighters_bindings`, `daggers`, `sickles` und `hammer`. Die Rüstungsanpassungen stehen gesondert im Generatorinventar.

- Herkunft, Dateigrößen und SHA-256: [gear-source-inventory.json](../design/gear-source-inventory.json).
- Vollständige Lizenz: [LICENSE.txt](../third_party/dungeons_gear/LICENSE.txt).
- Wiederholbarer Import aus den mitgelieferten Snapshots: `python3 tools/import_dungeons_gear.py`; Rüstungsadapter: `python3 tools/generate_gear_armor_models.py`.
- Quellen-/Ressourcenprüfung: `python3 tools/validate_gear_resources.py`; [Prüfbericht](../reports/gear-expansion-resources.json).
- Eigene Mechanik: `item/gear/`, insbesondere `DungeonsArtifactItem`, `DungeonsWeaponItem`, `SoulEnergy` und `ArtifactCombat`.
- Eigene Darstellung im Handbuch: `client/GearGuideScreen.java`.

Diese Auswahl übernimmt veröffentlichte Community-Kunst mit Credits. Sie ist kein vollständiger Port der Fremdmod und kein Export aus dem Originalspiel. Die Mechanik und Rezeptbalance sind eigene Implementierungen. [VALIDATION.md](VALIDATION.md) trennt tatsächliche Serverprüfungen und native Bilder von noch offenen Survival-/Mehrspielerfragen.
