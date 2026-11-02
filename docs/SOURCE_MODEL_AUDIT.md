# Quellenprüfung für Mobmodelle, Texturen und Animationen

**Prüfstand: 2. November 2026.** Die vollständige Zuordnung steht in [`design/model-source-audit.json`](../design/model-source-audit.json): **alle 436 bestehenden Katalogzeilen genau einmal**, mit Quellenpaketen, Dateihashes, Lizenztexten, tatsächlichen Texturbindungen, offenen Arbeiten und dem Sift-Ausschluss. Diese Datei beschreibt verfügbare Grundlagen. Sie behauptet weder 436 neu implementierte Entities noch 436 fertige Originalanimationen.

Der Nutzer möchte die Welt von Dungeons in der Minecraft-Sandbox und hat ausdrücklich hochwertige vorhandene Nachbildungen als Quellen gewünscht. Daher werden veröffentlichte, passend lizenzierte Modelle gezielt verwendet. Öffentliche Erreichbarkeit allein gilt nicht als Nutzungserlaubnis. Originale Spieldateien wurden für diesen Schritt nicht extrahiert; offizielle Bilder und öffentlich sichtbare Referenzansichten dienen der Betrachtung.

## Was die Zahlen bedeuten

| Quellenstatus | Dungeons I | Dungeons II | Gesamt | Bedeutung |
|---|---:|---:|---:|---|
| Konkretes Assetpaket zur Adaption vorhanden | 130 | 1 | **131** | Exakt benannte Quelle oder ausdrücklich dokumentierte Ableitung. Noch keine Aussage über vollständige Implementierung oder grafische Abnahme. |
| Minecraft-Modell bereits vorhanden | 86 | 39 | **125** | Vorhandene Vanilla-Renderer, Varianten oder Kompositionen. Nicht als neu portierte Modelle zählen. |
| Genaue Nachbildung/Variante offen | 56 | 77 | **133** | Häufig ist ein Basisrig vorhanden; genaue Palette, Anbauteile, Effekte oder Animationen fehlen weiterhin. |
| Sift ausgeschlossen | 0 | 47 | **47** | Konkrete, begründete Ausschlussliste gemäß Nutzerwunsch. |
| **Katalogzeilen** | **272** | **164** | **436** | Darunter Pets, Varianten, Händlerrollen, Fallen und Effekte. |

Damit bleiben **389 nicht ausgeschlossene Katalogzeilen**, darunter der noch nicht sicher einem Habitat zugeordnete II-Wisp. Auch diese Zahl ist keine Arten- oder Fertigstellungszahl. Die JSON enthält **128 Quellenpakete und 595 deduplizierte Quelldateien**. Ein Paket kann mehrere Formen bedienen; ein Eintrag kann sowohl eine MIT- als auch eine CC-Alternative haben. Der zusätzliche Quellenkandidat `mcd:mage` ist ausdrücklich kein nachträglich eingeschmuggelter 437. Katalogeintrag.

`coverage[].catalog_runtime_id_at_audit` und `catalog_implementation_status_at_audit` geben den vorhandenen Katalogstand wieder. Sie werden durch das Auffinden eines Modells nicht zu einer Implementierungszusage hochgestuft. Den tatsächlich gebauten und geprüften Stand dokumentieren [`VALIDATION.md`](VALIDATION.md), [`MOBS_RUNTIME.md`](MOBS_RUNTIME.md) und die jeweiligen Laufberichte.

## Geprüfte Quellen und Bedingungen

| Quelle | Festgehaltener Stand | Lizenz und konkrete Verwendung |
|---|---|---|
| [Goety 2 – Polarice3](https://github.com/Polarice3/Goety-2) | Branch `1.20`, Commit `7de64560f76ae47f1b399bb46f69540511b9871e` | **MIT, © 2023 Polarice3.** Ausgewählte Java-Rigs, eigene Texturen und Keyframes. Künstlercredits zusätzlich erhalten. |
| [Dungeons Mobs – Infamous Misadventures](https://github.com/Infamous-Misadventures/Dungeons-Mobs) | Branch `1.19`, Commit `f04b11e7b3672776599fd15ad98a61c53f63b182` | **MIT, © 2022 Patrigan.** Ausgewählte Gecko-/Java-Modelle, Animationen und Variantenpaletten. |
| [Dungeons Mobs Port – FirefoxSalesman](https://github.com/FirefoxSalesman/DungeonsMobs) | `master`, Commit `cca789ab3ce910853517d65494efd76508255a1d` | **MIT, © 2022 Patrigan.** Unter anderem die Mooshroom-Monstrosität und echte Rüstungsteile. Credits für Port und ursprüngliche Autoren erhalten. |
| [ReCrafted Mobs – GillsBills](https://modrinth.com/resourcepack/recrafted-mobs) | Version **11.0**, Versions-ID `nUAnxMZE`, 24. Mai 2025 | **CC BY-NC-SA 4.0.** Gezielte CEM-/Texturübernahmen mit Namensregeln, Credits, Änderungsvermerk, nichtkommerzieller Beschränkung und gleicher Lizenz für die adaptierten Kunstdateien. |

ReCrafted-Archiv SHA-256: `74901b18716b8efc57368a3b2d47a43207416925da6f6f5e492758c94f2cec2c`. Der vollständige [CC-Lizenztext](https://creativecommons.org/licenses/by-nc-sa/4.0/legalcode.en), die MIT-Texte und die ursprüngliche ReCrafted-Lizenznotiz sind in der maschinenlesbaren Prüfung enthalten. Sie müssen außerdem im tatsächlich ausgelieferten Attributions-/Lizenzpaket erhalten bleiben. Die eigenen unabhängigen Laufzeitdateien werden dadurch nicht pauschal umetikettiert; die übernommenen oder bearbeiteten Kunstdateien erhalten ihre jeweilige Herkunftslizenz.

Die ReCrafted-Notiz bittet zusätzlich darum, vor Änderungen zu fragen. Für diese gezielte Verwendung stützt sich das Projekt auf die dort ausdrücklich veröffentlichte CC-Lizenz. Diese Entscheidung ist dokumentiert, statt die Bitte zu verschweigen oder die Dateien als MIT auszugeben. Die [Autorseite auf CurseForge](https://www.curseforge.com/minecraft/texture-packs/recrafted-mobs) und die Modrinth-Seite erklären, dass beigesteuerte Assets mit Erlaubnis verwendet werden. Ein öffentliches ursprüngliches Bearbeitungsprotokoll für jedes einzelne Pixel liegt nicht vor. Im geprüften Paket und den ausgewählten Dateien wurde keine gegenteilige ausdrückliche Erklärung gefunden, dass diese sieben Bossmodelle aus dem Dungeons-Spielarchiv extrahiert wurden.

### Credits, die bei der Übernahme erhalten bleiben

- **Goety:** Polarice3; für Royal Guard/Blackguard ausdrücklich **night.lxrd**. Die übrigen auf der [Autorseite](https://www.curseforge.com/minecraft/mc-mods/goety) genannten Beiträge bleiben relevant, wenn weitere Dateien übernommen werden. Einzelne fremde Bibliotheksdateien im Gesamtprojekt besitzen zusätzliche Hinweise; dieses Audit ist keine Freigabe für einen pauschalen Code- oder Audioimport.
- **Dungeons Mobs:** Patrigan und die im ausgewählten Dateiverlauf genannten Autoren; zahlreiche Modell-/Animationsüberarbeitungen von **Alexander’s Fun And Games**, weitere Beiträge unter anderem von Thelnfamous1, Mike Garay, L_Ender und baguchi. Die JSON speichert die jüngsten ausgewählten Dateicommit-Autoren und Links.
- **Dungeons Mobs Port:** Patrigan, Carson Holscher / FirefoxSalesman; **Pasticcino** für Redstone-/Mooshroom-Monstrosity-Kunst sowie die zu weiteren übernommenen Modellen passenden Credits von Andrejulio und McFella. Grundlage sind Repository und [Projektcredits des Autors](https://www.curseforge.com/minecraft/mc-mods/dungeons-mobs-port).
- **ReCrafted:** GillsBills sowie Cave Fox, Kekkman, PinkuRinku, RavenVLemon, Ewanhowell5195, creepermax123 und MickeyJoeAlpha gemäß mitgeliefertem Creditsabschnitt. Die genaue Einzelautorschaft jedes ausgewählten Bosses wird im Pack nicht gesondert angegeben; keine erfundenen individuellen Zuschreibungen ergänzen.

## Die großen Bosslücken schließen

| Ziel | Konkrete Grundlage | Erforderliche Besonderheit |
|---|---|---|
| Redstone Monstrosity | Goety `RedstoneMonstrosityModel.java`, `RedstoneMonstrosityAnimations.java`, zugehörige Texturen | Authentisch ausgearbeitete Goety-Nachbildung, zehn benannte Clips; kein umbenannter Neo-Monstrosity-Rig. |
| Mooshroom Monstrosity | Port `redstone_monstrosity.v2.geo.json`, `redstone_monstrosity.v2.animation.json`, `mooshroom_monstrosity_full.png`, Augenlayer | Geteiltes Monstrosity-Rig, echte Mooshroom-Palette; 512 × 256, 14 Quellclips einschließlich Mooshroom-Slam. |
| Corrupted Cauldron | ReCrafted `slime/slime3.jem` | Eigenes Kessel-/Holzrig, `textures/entity/slime/corrupted_cauldron.png` und Emission. |
| Ancient Guardian | ReCrafted `elder_guardian/elder_guardian2.jem` | Zugehörige Ancient-Palette, kein lediglich vergrößerter Vanilla-Elder. |
| Jungle Abomination | ReCrafted `warden/warden4.jem` | Eigenes Pflanzenrig, zugehörige `jungle_abomination.png` und Emission. |
| Tempest Golem | ReCrafted `warden/warden5.jem` | Eigene Wind-/Golemform, zugehörige `tempest_golem.png` und Emission. |
| Wretched Wraith | ReCrafted `wither/wither3.jem` | `optifine/random/entity/wither/wither3.png` und Emission; Bindung kommt aus der Random-Entity-Regel. |
| Nameless One | ReCrafted `wither/wither5.jem` | `wither5.png` und Emission; eigenes Skelett-Zaubererrig. |
| Heart of Ender | ReCrafted `wither/wither6.jem` | `ender`-Teilbaum und normale `wither6.png`; Quelle enthält außerdem den Arch-Illager. |
| Arch-Illager | Teilbaum `arch_illager` derselben CEM-Quelle | **Standalone zwingend `wither_invulnerable6.png`, ohne `wither6_e`!** Der normale Atlas enthält in Archies UV-Bereich magentafarbene Platzhalter. Der Quellpack verwendet hier offenbar die Wither-Spawnphase als Archiephase. |
| Treetop Whisperer | Goety Whisperer als ausdrücklich dokumentierte Ableitung | Größerer Whisperer: 1,5-fache Größe, dunklere Außenpalette, violettes Inneres; gesonderter Bosskampf bleibt offen. |
| Vengeful Heart of Ender | Eigene Nachbildung anhand tatsächlich betrachteter Referenzen | Eigener Generator und eigene Pixelatlanten vorhanden: 34 Rigknoten, 59 Quader/Flächen, vierbeiniger segmentierter Unterkörper, hoher Vorderhals und breite Flammenkrone; fünf eigene Clips. Die genaue grafische und vollständige Kampf-Abnahme folgt dem integrierten Clientstand. |

Die sieben ReCrafted-JEM-Dateien enthalten ihre Untermodelle unmittelbar; keine unaufgelösten JPM-Verweise. Ihr Manifest enthält Rigknoten, Elternbeziehungen, Transformationsangaben und CEM-Animationsausdrücke. Das sind für Minecraft geschriebene Ausdrücke, keine aus dem Originalspiel exportierten Keyframeclips. Ein erfolgreicher Geometrieimport allein erhält deshalb nicht automatisch das ursprüngliche Kampftiming.

**Die Arch-Texturkorrektur entstand durch einen tatsächlichen Clientbefund.** Dieser Fall zeigt, warum das Lesen eines Dateinamens keine vollständige visuelle Abnahme ersetzt. Auch beim Illusioner ergaben `geo_illager.geo.json` und Basis-PNG allein nur eine braune Grundansicht. Die Ursache ist inzwischen konkret aufgelöst: `DungeonsIllusionerEntity` rüstet ausdrücklich `ILLUSIONER_ARMOR` aus. Deshalb gehören `geo/armor/illusioner_armor.geo.json` und `textures/models/armor/illusioner_armor.png` zur benötigten Quelle. Die unveränderten Texturpixel wurden zusammen mit der Haut in einen Atlas gesetzt; Kopf, Körper, Arme, Beine und Füße sind an die passenden ursprünglichen Rigsockel gebunden. Die 15 Quellclips bleiben erhalten, für die bewaffnete Darstellung werden die passenden `*_uncrossed`-Clips und echten Hand-/Itembindungen verwendet. Die komplette Quelldateizuordnung ist damit geklärt; die native Darstellung und Kampfzustände müssen weiterhin anhand des tatsächlichen integrierten Clientlaufs beurteilt werden.

### Zusätzliche Infamous-Varianten und Konstrukte

[`design/infamous-additions.json`](../design/infamous-additions.json) hält die konkrete Adaption der 18 zusätzlichen Quellenpakete fest. Daraus entstehen **22 visuelle Bindings**, einschließlich vier zusätzlicher vorhandener Palettenvarianten, und **13 Geometrieklassen**. Fünf Gecko-Modelle wurden mit unveränderten Quellclips konvertiert: Mage, Geomancer Bomb, Geomancer Wall, Ice Chunk und Entangle Vine. Der Konvertierungsbericht enthält die erhaltenen Kanäle und die ausdrücklich dokumentierte zyklische Abbildung von `query.life_time`; kein ungebundener Quellkanal wurde stillschweigend verworfen. Die Umwandlung verwendet die geprüften Easing-Funktionen einschließlich `easeOutBack` und `step`.

Für Frozen/Jungle Zombie, Mossy Skeleton, Icy Creeper und die Piglin-Varianten werden die installierten Minecraft-Geometriefabriken verwendet. Die ausgewählten MIT-Texturen bleiben unverändert oder werden für mehrere Ebenen pixelgetreu nebeneinander in einen Atlas gesetzt. Gold-/Netherite-Piglins besitzen ihre passenden separaten Helme, Fungus Thrower den tatsächlich modellierten Pilzsack, Armored Drowned seine äußere geometrische Hautschicht. Sunken Skeleton behält die Korallenteile; sie folgen nun den zugehörigen Kopf-/Beinknoten. Beim Mage wurde die echte `MAGE_ARMOR` samt Umhang aufgelöst, damit nicht erneut nur eine unvollständige Grundhaut erscheint.

Die nativen Piglin-Posen für Laufen, Armbrust, Nahkampf und Pilzwurf sowie die Creeper-Zündbewegung sind eigene Ergänzungen für die Minecraft-Sandbox. Sie werden nicht als Original-Keyframes aus Dungeons ausgegeben. Die Armbrusthalterung berücksichtigt beide Arme und mitbewegte Kleidung. Der Icy Creeper benötigt einen eigenen Frost-/Explosionszustand; sein Manifest liefert deshalb keine irreführende Feuerfeldbindung. Die Konstrukte sind als Fallen beziehungsweise beschworene Hilfsobjekte eingeordnet. Ihre Modellverfügbarkeit macht sie nicht automatisch zu frei umherwandernden Gegnern. Die geprüften Quellen und Bindings sind Vorstufen; Registrierung, Spielverhalten und native Abnahme stehen separat in der Laufzeitdokumentation.

Für Treetop: [Mojangs Beschreibung](https://www.minecraft.net/en-us/article/treetop-tangle) und die ergänzend als sekundär gekennzeichnete [Treetop-Taxonomie](https://minecraft.fandom.com/wiki/Minecraft_Dungeons%3ATreetop_Whisperer). Für Vengeful wurden [offizielles Echoing-Void-Keyart](https://www.minecraft.net/content/dam/minecraftnet/games/dungeons/key-art/EchoingVoid-header2.jpg) und eine [sekundäre Ganzkörperansicht](https://minecraft.wiki/images/MCD_Vengeful_Heart_of_Ender.png) tatsächlich betrachtet. Beides sind Referenzen, keine zu kopierenden Texturatlanten. Der [offizielle Release-Trailer](https://youtu.be/NMA4gEPbN-4) ist auf der [Mojang-Veröffentlichungsseite](https://www.minecraft.net/en-us/article/echoing-void-has-arrived) verlinkt.

## Friedliche Welt, Pets, Händler und Varianten

Die Prüfung beschränkt sich ausdrücklich nicht auf 38 Kampfprofile. Das zusätzliche ReCrafted-Inventar enthält 64 kuratierte Zuordnungen mit 170 deduplizierten Dateien. Namensregeln wurden wirklich ausgewertet, statt ähnlich klingende Dateinamen automatisch gleichzusetzen.

| Gruppe | Konkrete Quellenbindung | Verbleibende Arbeit |
|---|---|---|
| Golden / Diamond Key Golem | `chicken/chicken4.jem`; `textures/entity/chicken_key.png` bzw. `optifine/random/entity/chicken_key2.png` | Tragen, Flucht, Türkontakt und das Zielsystem. Fake-Diamond ist gesondert offen. |
| Wooly Cow | `cow/cow5.jem` + **`optifine/random/entity/cow/wooly_cow2.png`** | Tierverhalten, Skalierung und Interaktionen. Die unsuffixed Palette gehört zur Umbra Cow. |
| Piggy Bank | `pig/pig3.jem` + `textures/entity/pig/chest_pig.png` **und** `pig_chest_closed.png` | Körper und Kistensubmodell benötigen verschiedene Texturen; Loot-/Fluchtverhalten. |
| Vulture, Wisp | `phantom/phantom2.jem`, `allay/allay5.jem` | Flug-/Schwebebewegung an die tatsächliche Entity koppeln; keine Partikel zu Tieren umdeklarieren. |
| Ministrosities / Mini Abomination | ReCrafted `chicken/chicken5.jem` mit fünf geprüften Paletten; alternativ Goety Ministrosity-Rig mit Idle/Walk/Attack/Sit | Unterschiedliche Petpaletten erhalten, Originalverhalten und Größen prüfen. |
| Kleine Tiere | Geprüfte ReCrafted-Pakete für unter anderem Mole, Hedgehog, Racoon, Raven, Red Panda, Skunk, Toucan, Royal Penguin, Duck und Vested Rabbit | Nicht jeden Pet-Atlas mit einem beliebigen generischen Tier kombinieren; Babyproportionen und Begleitverhalten prüfen. |

**Konkret korrigierte Schaf-/Pet-Bindungen:** Die drei Enchanted-Sheep-Farben benötigen neben dem Körper das eigene `sheep_wool/sheep_wool.jem` mit **64 × 64**-UV-Layout. Die Modellregel nennt alle drei Enchanted-Namen ausdrücklich; die Fur-Regeln wählen Blau `sheep_fur8.png`, Grün `sheep_fur9.png`, Rot `sheep_fur10.png`. Eine zuvor vorgesehene Vanilla-64-×-32-Fellschicht wäre nicht gleichwertig. Beide Quellmodelle und die Fell-Expressions sind im Audit enthalten.

Bei **Spotted Baby Pig** und **Grey Tabby** ist die Hautauswahl namentlich belegt, die Modellwahl dagegen nicht eindeutig: Die ursprünglichen Modellregeln enthalten Kontext-/Zufallszweige. Das Audit wählt deshalb ausdrücklich das normale Pack-Rig `pig/pig.jem` (64 × 64) beziehungsweise `cat/cat.jem` (64 × 32) als passende Tierbasis. Das ist eine dokumentierte Adaptionsentscheidung, keine erfundene Namensregel. Gerade beim Spotted-Pig-Atlas würde ein ungeprüftes Vanilla-64-×-32-Modell falsche UVs erzeugen. Goat und Turtle besitzen dagegen keine entsprechenden Pack-JEMs und bleiben echte Vanilla-Modellreferenzen.
| Händler / Turmrollen | Elf unterschiedliche ReCrafted-Händlerrigs von Blacksmith bis Gildsmith | Mehrere Zubehörlayer beachten; vorhandene Villager-Professionsprototypen werden damit nicht automatisch zu Originaldiensten. |
| Rüstungsvarianten | MIT-Modelle für Armored Pillager/Vindicator; ReCrafted-Rigs und geprüfte Körper-/Rüstungspaletten für weitere Zombies/Skelette/Strays/Mountaineers | Rüstungstiers, Waffenhand und tatsächliche Pose erhalten. Beim Armored Frozen Zombie und Mossy Skeleton behebt die explizite Body-Bindung unvollständige Quellnamensregeln. |
| Pflanzen / Unterwasserhindernisse | Goety QuickGrowingVine/PoisonQuillVine einschließlich expliziter Kelp-/Anemone-Rendererbindungen | Rig, Burst/Hold/Burrow/Shoot-Clips und korrekte Unterwasserpaletten statt bloßer Namensübertragung. |
| Biomine / Tropical Slime | Goety BioMineModel + 19 Frames; TropicalSlimeModel + innerer Fischlayer | Beim Tropenschleim sind die 47 Fisch-PNG **Animationsframes**, keine 47 Paletten. Der tatsächliche Quellzähler durchläuft 48 Ticks und fällt im letzten Slot auf Frame 1 zurück. Transparenter Außenkörper und innere Fischfolge werden getrennt mit gemeinsamer Pose gezeichnet. |

Für normale Kuh, Schwein, Huhn, Wolf, Zombie, Skelett, Evoker und andere echte Minecraft-Gegenstücke dürfen die installierten Modelle weiterverwendet werden. Dasselbe gilt für sinnvolle Zusammensetzungen wie Skeleton Horseman oder Chicken Jockey. Das ist eine passende Sandbox-Grundlage, jedoch keine neue Dungeons-Portierung und keine Zusage, dass Alter, Farbe, Angriffsablauf oder Begleiterverhalten schon stimmen.

**Alle 26 Ancient-Einträge bleiben einzeln sichtbar.** Sie besitzen bekannte Grundformen; die Prüfung verknüpft diese mit vorhandenen Rigs oder Vanilla-Gegenstücken. Noch ungeprüfte Ancient-Größen, Effekte, Rüstungen und verzauberte Bewegungs-/Kampfzustände werden nicht durch 26 bloße Aliasnamen als fertig ausgegeben. Das gilt ebenso für die sechs Endersent-Augenformen.

Die verbleibenden speziellen Dungeons-I-Optiklücken sind neben der noch zu prüfenden eigenen Vengeful-Nachbildung unter anderem Clam-Formen, Sentries/Fallen, exakte Ferret-/Emperor-Penguin-/Phantom-Familiar-Petvarianten, Armored End Vindicator, Arch-Visage, bestimmte Kulissenobjekte und Effekte. Für generisches „Ferret“ und „King Penguin“ gibt es ReCrafted-Kandidaten; ohne Farb-/Artabgleich sind das keine gesicherten Cinnamon-, Sable- oder Emperor-Zuordnungen.

## Sift ist eine Dimension, keine pauschale II-Sperre

Die 47 ausgeschlossenen IDs stehen vollständig unter `sift_scope.excluded_ids`, jeweils mit Begründung und Quellen pro Zeile. Dazu gehören die Sift-eigenen Sifter-/Sculker- und anderen Spezies, ihre Begleiterrollen, entsprechende korrumpierte Varianten sowie Sculk Monstrosity / Sculk Cube. Das Habitat ist anhand der offiziellen [Dungeons-II-Beschreibung](https://www.minecraft.net/en-us/about-dungeons-ii) und ausdrücklich als sekundär gekennzeichneter Einzel-/Dimensionstaxonomie abgegrenzt.

Die Interpretation lautet: **Sift-eigene Spezies auslassen**, auch wenn eine solche Spezies zeitweise durch ein dimensionsübergreifendes Ereignis außerhalb ihrer Herkunft vorkommen kann. Deshalb ist ein Echo Golem am Portal kein Anlass, ihn stillschweigend wieder einzuschließen. Dieser interpretierte Randfall steht in der JSON.

Beibehalten werden Overworld- oder dimensionsübergreifende Vanilla-/Illager-Familien einschließlich ihrer Soul-corrupted-Varianten. Prime Enchanter, Supreme Evoker, Grand Illusioner, Twisted Warden, Monstrous Opus, Flame Construct und Twisted Chicken sind nicht aufgrund ihres II-Ursprungs ausgeschlossen. **Flame Construct** besitzt eine konkrete Goety-Modell-/Animationsquelle; die [Autorenveröffentlichung 2.5.59.0](https://www.curseforge.com/minecraft/mc-mods/goety/files/9153454) nennt den Zugang ausdrücklich.

Für die übrigen neuen II-Formen ist eine passende I-/Vanilla-Familie häufig ein brauchbarer Anfang. Die konkrete II-Palette, Rüstung, korrumpierte Oberfläche oder besondere Silhouette muss aber sichtbar verglichen werden. Die Quellenkarte weist solche Grundlagen als Grundlagen aus. `mcd2:wisp` bleibt hinsichtlich Habitat offen; keine unbewiesene Sift-Zuordnung erzwingen.

## Nicht ausgewählte Quellen

**Dungeons Bosses**, **Dungeons Content** und weitere betrachtete ARR-Projekte wurden nicht als frei kopierbare Assetlieferanten behandelt. Das gilt ebenso für die restriktive Timefall-Lizenz des alten Chronosacaria-Repositories. Das MIT-Projekt `pigsteel/MCDungeonsMobs` enthält lediglich eine kleine Vorlage ohne die gesuchten Modelle. `Extended-Universe-Mobs` besitzt eigene CC-BY-NC-SA-Nachbildungen, schloss aber keine der wichtigsten exakten Bosslücken dieser Auswahl.

Eine Vengeful-Blockdisplay-Vorlage aus 1.467 Spielerköpfen liefert keinen gegliederten Entity-Rig und wurde nicht als Ersatz übernommen. Der Actions-and-Stuff-Stil ist ein Qualitätswunsch; dessen Assets wurden nicht kopiert. Sounds und Musik gehören nicht zu dieser gezielten Modellprüfung und sind durch sie nicht zur Übernahme freigegeben.

## Konkrete Arbeit mit der JSON

1. In `coverage` die **Katalog-ID** suchen. Das sind Recherche-IDs, keine versprochenen Registry-IDs.
2. `coverage_status`, `remaining_work` und bei Varianten die `base_candidate_catalog_ids` lesen. Ein Grundkörper darf nicht versehentlich als exakte Variante ausgegeben werden.
3. `source_bundle_ids` beziehungsweise `available_base_bundle_ids` auflösen. Das Paket enthält Modell-, Textur-, Animations- und Referenzpfade; CEM-Pakete außerdem Knoten-/Texturbindungen und Quellregeln.
4. Über `source_project_id` den festgehaltenen Commit oder das Archiv plus Lizenz/Credits bestimmen. `source_files` enthält SHA-256 und bei Gitquellen die Datei-URL und ausgewählte Historie.
5. Nur benötigte Dateien adaptieren und Attribution/Änderungen mitliefern. Vanilla-Ressourcen als installierte Referenzen verwenden. Ein Multi-Textur-Rig nicht versehentlich auf einen einzigen Atlas reduzieren.
6. Die Adaption in der echten laufenden Minecraft-Welt prüfen: UVs, Transparenz/Emission, Pose, Bodenkontakt, Maßstab und zum Serverzustand passende Animation. Erst dieses Ergebnis als tatsächlichen Fortschritt in Runtime-/Abnahmedokumentation eintragen.

Die lokalen Referenzklone und Downloads liegen außerhalb des Lieferprojekts unter `build-tools/reference-mobs`. Sie sind Recherche-/Konvertierungsmaterial, kein zusätzlich mitzulieferndes Modarchiv. Das Audit bleibt durch exakte Quellen, Revisionen, Hashes und Pfade nachvollziehbar, auch wenn dieser lokale Arbeitsordner später neu aufgebaut wird.
