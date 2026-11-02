# Gezielte Modellkorrekturen: Erz-Illager, Baby Squid und Schwimmen

Zielplattform: Minecraft Java 1.21.1, NeoForge 21.1.265, Java 21. Diese Änderungen ergänzen vorhandene Modelle. Sie sind keine aus Minecraft Dungeons extrahierten Modelle, Texturen oder Animationsclips.

## Erz-Illager: Stab, Hand und Kugel

Die konkrete Quellenhierarchie lautet `__cem_host_body1/arch_illager/body5/right_arm6/bone14`. Unter `bone14/body1_sub_87` befinden sich Schaft und kleine Auflage. Im ursprünglichen, aus dem gemeinsamen Wither-/Heart-Modell ausgewählten Teil war der Stab um 90 Grad um X gedreht; die eigentliche Kugel war in diesem Unterbaum nicht vorhanden.

Die offizielle Spielansicht in [Search for the Arch-Illager, Part 2](https://www.minecraft.net/en-us/article/search-arch-illager--part-2), [Bild 4](https://www.minecraft.net/content/dam/minecraftnet/games/dungeons/game-characters/Search-Archillager-Part2-Image4.jpg), zeigt den seitlich gehaltenen aufrechten Stab mit einer hellen rosafarbenen, würfelförmigen Kugel. Sie dient als Form- und Farbreferenz. Das Bild wird weder als Textur verwendet noch mit dem Mod als Spielasset ausgeliefert.

Die eigene Anpassung hebt den Stabarm um 55 Grad seitlich an. Der Stab erhält die entgegengesetzte Drehung und verläuft durch den tatsächlichen dünnen Handteil. Seine Quelle, Schaftform und Texturpixel bleiben erhalten. Der neue Knoten `da_arch_orb` trägt einen fünf Modellpixel breiten Würfel über der Auflage. Eine langsame Drehung und eine kleine vertikale Bewegung sind eigene visuelle Ergänzungen. Die leuchtenden Pixel liegen in einer eigenen Kachel und einem eigenen Emissionsatlas. Die Beleuchtung verstärkt sich mit der bereits synchronisierten Serveraktion.

Die Beine reagieren auf die tatsächliche Fortbewegung. Die freie Hand hebt sich innerhalb der synchronisierten Aktion. Diese einfachen Anpassungen sind ausdrücklich keine ursprünglichen Dungeons-Keyframes. Der Kopf bekommt eine begrenzte Blickbewegung über die vorhandene `look`-Bindung.

## Baby Squid: sichtbare gegliederte Tentakel

Die Quelle `glow_squid/glow_squid2.jem` enthält einen kleinen Körper unter dem historisch so benannten Knoten `babyghast`. Die bisherigen winzigen Anhänge sind flache Geometrie, deren UVs transparente Quellpixel treffen. Die leeren nativen Erwachsenen-Tentakel passen zudem nicht an den kleinen Körper.

Die Ergänzung setzt deshalb acht eigene kurze Tentakel direkt unter diesen Körper. Jeder besitzt ein dreipixeliges oberes Segment und eine schmalere zweipixelige Spitze. Ihre getrennten Pivots erlauben einen versetzten Biegeverlauf. Im Wasser läuft eine ruhige Welle; die tatsächliche Bewegung beeinflusst ihre Stärke. Außerhalb des Wassers bleiben nur kleine Bewegungen. Die sichtbaren neuen Pixel sind selbst erzeugte, deckende Petroltöne. Körper, ursprüngliche PNG-Pixel und unveränderte Quellenkopie bleiben erhalten.

Diese neue Form schließt die belegte Sichtbarkeitslücke. Sie behauptet keine neu entdeckten Originaltentakel und keine aus dem Originalspiel übernommenen Schwimm-Keyframes. Die Position und Navigation der wirklichen Entity bestimmt weiterhin der Server.

## Ertrunkene: tatsächlicher Schwimmzustand

Die Bindungsänderung gilt für `armored_drowned`, `pale_armored_drowned` und `armored_trident_drowned`. Der zusätzliche CEM-Eingang `is_swimming` ist nur wahr, wenn der wirkliche Mob sowohl `isInWater()` als auch `isSwimming()` meldet. Er verwendet damit den vorhandenen synchronisierten Minecraft-Zustand.

Während dieses Zustands neigt sich der Körper um 72 Grad. Der Kopf neigt sich entgegen, damit der Blick nach vorne gerichtet bleibt. Arme und Beine wechseln sich beim Paddeln ab. Die äußeren Haut-/Rüstungsteile bleiben Kinder der jeweils bewegten Körperteile. Der separate native `hat`-Knoten folgt der Kopfrotation. Ein gesonderter Wurfclip kann die Arme anschließend übersteuern.

Ein Mob, der nur auf einem Wasserboden steht, behält die normale Haltung. Die lokale Handbuchvorschau erfindet keinen Wasserzustand. Ältere `CemFrame`-Fixtures verwenden für den neuen Eingang ausdrücklich `false`; außerhalb des Wassers bleibt der Eingang auch bei einem veralteten Schwimmflag falsch. Der vorhandene Goety-Nekromant besitzt bereits eigene Wasserclips und wird durch diese drei neuen Bindungen nicht umgebaut.

## Reproduzierbare Dateien und Herkunft

- `tools/cem_visual_additions.py`: eigene Geometrie-/Ausdrucksergänzungen sowie deterministische Originalpixel für die neuen Kacheln.
- `tools/convert_cem_models.py`: bindet ausschließlich diese zwei benannten Ergänzungen in den bestehenden CEM-Konverter ein; andere Modellpfade bleiben unverändert.
- `design/v04-visual-source-audit.json`: gezielter Ausschnitt der bereits geprüften Quellenfreigaben, zwei Modelle und vier genaue Quelldateien mit Hashes.
- `reports/v04-visual-cem-import.json`: Ergebnis des gezielten Laufs aus den mitgelieferten unveränderten Quellenkopien.
- `reports/v04-visual-binding-delta.json`: feldweise Änderungen für die zentrale Referenzbindung; keine vollständige Ersatzdatei für `reference-models.json`.

Die beiden Modelle lassen sich ohne das vollständige fremde Ressourcenpaket neu erzeugen:

```bash
python3 tools/convert_cem_models.py \
  --source-root third_party/recrafted_mobs/source \
  --audit design/v04-visual-source-audit.json \
  --manifest reports/v04-visual-cem-import.json
```

Jede Atlas-Kachel nennt `pixel_origin`. Ursprüngliche ReCrafted-Pixel werden unverändert kopiert, eigene Kacheln als `dungeonsalive_original_generated_pixels` markiert. Die beiden ursprünglichen JEM-Snapshots bleiben SHA256-identisch. Die abgeleiteten Kunstdateien behalten die zugehörige CC-BY-NC-SA-4.0-Herkunft; unabhängiger Konverter-/Laufzeitcode bleibt eigener Projektcode. Die allgemeinen Hinweise und Lizenztexte stehen weiterhin in `THIRD_PARTY_NOTICES.md` und `third_party/recrafted_mobs/`.

## Abnahme

Bereits ausgeführt: beide gezielten Konvertierungen, Python-Syntaxprüfung, vollständiger Pixelvergleich jeder übernommenen Quellenkachel und Hashprüfung der beiden unveränderten JEM-Snapshots. `VisualPolishPoseTest` prüft die tatsächliche Stabachse durch die angehobene Hand sowie die Trennung von Schwimmen und Wasserboden-Idle einschließlich Pose-Reset.

Der zentrale Java-Build und die beiden genannten Regressionen wurden erfolgreich ausgeführt. Die JUnit-XML-Dateien der geprüften zentralen Testausführung enthalten insgesamt 50 Tests ohne Fehler oder übersprungene Tests. Die native Sichtung liegt unter `reports/v04-visual-polish-qa.json`: Der Bericht bewahrt drei frühere Vergleichsbilder, 175 betrachtete Frames des ersten Zwischenstands, 30 Frames des gezielten Nachtests und 83 Frames des abschließenden Gesamtdurchlaufs getrennt auf. Jeder tatsächlich betrachtete native Frame hat einen SHA256 und die zugehörigen erfassten Client-/Serverzustände.

Im echten Tages-Studio sind der aufrechte Stab, der Griff und die helle rosafarbene Kugel sauber sichtbar. Die freie Hand hebt sich während der tatsächlichen Beschwörung. Beim Baby-Squid bleiben die neuen Tentakel verbunden und zeigen über die Sequenz unterschiedliche Biegephasen. Die drei Ertrunkenen neigen den Körper beim tatsächlichen Schwimmen; Haut-/Rüstungsschichten folgen der Bewegung, und spätere Frames bleiben durch die reale Kameranachführung vollständig im Bild. Eine gesonderte Nachtprüfung des Orb-Leuchtpasses ist damit noch nicht behauptet.

Der erste geplante Gesamtdurchlauf `20261102-081618-532` brach bei der nachfolgenden Bewegung des befreiten Schmieds ab: **317 von 352 vorgesehenen Hauptbildern, `complete=false`**. Die vorher erzeugten Bilder sind gültige einzelne Nachweise, aber keine abgeschlossene Gesamtgalerie. Der tatsächliche Rechtsklick hatte den Schmied bereits befreit und denselben Besitzer sowie dieselbe Entity-UUID erhalten; das Folgen blieb bei null Bewegung. Die Quellenprüfung fand einen echten gemeinsamen Pet-/Zivilistenfehler: Das Follow-Goal prüfte `tickCount % 10`, wurde bei ungerader Entity-ID aber ausschließlich auf ungeraden Ticks ausgeführt. Die abschließende `NoAI=true`-Angabe war dagegen der vorgesehene Szenenstopp und nicht die Ursache. Die Laufzeitkorrektur lässt diesen Goal nun jedes Tick arbeiten und behält seinen begrenzten Zehntick-Navigationstakt.

Die Dreizack-Aktionssichtung zeigte außerdem das falsche ruhende Itemmodell in der angehobenen Wurfhand. Vanillas gesondertes Wurfmodell benötigt den tatsächlichen Item-Benutzungszustand; die Armbewegung allein genügt nicht. Die Korrektur setzt diesen tatsächlichen Zustand während der Vorbereitung und beendet ihn wieder beim Abwurf beziehungsweise Abbruch.

Der gezielte Nachlauf `20261102-083518-294` schloss beide betroffenen Figuren mit sechs Hauptbildern und vier Sequenzen ab, `complete=true`. Tatsächlich betrachtet wurden seine sechs Hauptbilder und 24 ausgewählte Sequenzframes. Der befreite Schmied läuft darin mit derselben UUID und unverändertem Besitzer tatsächlich **1,47 Blöcke** bis zur Hauptaufnahme; KI und Navigation sind aktiv, die Beine wechseln sichtbar. Der Dreizack besitzt die richtige Wurfausrichtung. Server und Client melden während des Windups `minecraft:trident` als benutztes Item und am Ende wieder einen beendeten Use-Zustand. Diese 30 Nachtestbilder stehen im Bericht getrennt von den 175 Bildern des Zwischenstands.

Der abschließende Lauf **`20261102-084331-002` ist vollständig abgeschlossen:** 131 Profile, **352 von 352 Hauptbildern**, 13 Sequenzen mit insgesamt 208 Sequenzframes, `complete=true`, kein aufgezeichneter Fehler. Für diese gezielte Abnahme wurden alle 17 Hauptbilder der sechs oben genannten Zielprofile sowie je sechs über die gesamte Dauer verteilte Frames ihrer elf Sequenzen tatsächlich angesehen: **83 neue finale Sichtnachweise**. Die Gesamtaufnahme und der Umfang dieser gezielten visuellen Prüfung sind im Bericht ausdrücklich getrennt.

Alle sechs Zielprofile bestehen diese finale Prüfung. Stab, Kugel und Tentakel bleiben sauber verbunden; die drei Ertrunkenen zeigen mit tatsächlichem Server- und Client-Schwimmflag die geneigte Körperhaltung und intakte Materialschichten. Beim Dreizack melden beide Seiten während der Vorbereitung erneut das benutzte `minecraft:trident`; der letzte Aktionsframe meldet auf beiden Seiten wieder `minecraft:air` und `using_item=false`. Der befreite Schmied bewegt sich bis zur finalen Hauptaufnahme **1,93 Blöcke** über sein wirkliches Follow-Goal, bei aktiver KI und Navigation, mit unveränderter UUID und unverändertem Besitzer. Die historischen Fehlversuche bleiben im Bericht nachvollziehbar, zählen aber nicht zu den 83 final geprüften Frames.

Der markierte `MobModelShowcaseCapture` verwendet für mobile aquatische Gegner jetzt aktive KI und die wirkliche Wassernavigation zu einem Wasserwegpunkt. Er wartet den ersten Entity-Tick ab und verlangt für die Hauptbewegungsaufnahme sowohl den tatsächlichen Server- als auch den synchronisierten Client-Schwimmflag. Ein kreativer Kameraspieler vermeidet ein unbeabsichtigtes feindliches Ziel; Aktionsprüfungen verwenden weiterhin einen gültigen Survivalspieler beziehungsweise echten Golem als Ziel.

Bei Zivilisten bleiben Gefangenschaft, die tatsächliche `mobInteract`-Befreiung und das anschließende `FollowKeeperGoal` an derselben Entity-UUID nachweisbar. Dabei wird kein Handelsfenster erwartet. Die Entwicklungskamera folgt den echten Entitypositionen durch tatsächliche serverseitige Spielerbewegung. Kameraort und -winkel, Pose, Wasserzustand, Navigation und KI-Status werden pro Bild festgehalten. Die ursprünglichen PNGs werden nicht nachträglich umgebaut. Markerprüfung, getrennte Testwelt und Produktionssperre bleiben erhalten.
