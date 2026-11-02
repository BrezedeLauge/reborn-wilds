# Animationen: aktueller Bestand und nächste Produktionsschritte

Stand: **2. November 2026**, Version **0.4.0-alpha**. Die maßgebliche Auswahl ist [reference-models.json](../design/reference-models.json); Registrierung und offene Inhalte stehen in [implementation-status.json](../design/implementation-status.json). Die erzeugte [Animationsmatrix](../design/animation-coverage.json) verbindet die aktuellen Profile mit ihrer Darstellung und den Prüfnachweisen. Die Laufzeit-API ist in [REFERENCE_ANIMATION_RUNTIME.md](REFERENCE_ANIMATION_RUNTIME.md) erklärt.

## Was bereits vorhanden ist

**131 registrierte Figuren** besitzen jetzt eine ausdrückliche Quellen-/Eigenmodellbindung oder native Komposition: 78 Gegner und 53 friedliche Kreaturen/Händler/Zivilisten. Hinzu kommen **vier gesonderte, endliche Konstrukte** mit bestehenden Quellrigs und synchronisierten Phasen; sie zählen nicht als weitere Spezies. Die frühere Beschreibung von 37 gemeinsamen Familienmodellen neben einem Einzelboss trifft auf diese Fassung nicht mehr zu. Insbesondere verwendet Redstone Monstrosity auf ausdrücklichen Nutzerwunsch den veröffentlichten Goety-Rig, seine passenden Texturen und vorhandenen Keyframeclips; der eigene 0.2.1-Boss bleibt historisch erhalten.

Die Animationen stammen aus vier unterscheidbaren Verfahren:

| Verfahren | Vorhandene Umsetzung | Geltende Aussage |
| --- | --- | --- |
| Quellclips | Vorhandene Goety-`AnimationDefinition`-Clips und ausdrücklich konvertierte Bedrock-/Gecko-Clips aus den geprüften Dungeons-Mobs-Projekten. | Die veröffentlichten Community-Clips werden mit ihrem tatsächlichen Rig verwendet und zeitlich an diese Mod angepasst. |
| CEM-Host und Ausdrücke | Erhaltene CEM-Hierarchie, Originalausdrücke des ausgewählten Ressourcenpakets und passende Minecraft-Hostbewegungen. | Geometrie und Ausdrücke stammen aus der Quelle; manche Kampfbewegung bleibt ein eigener Vanilla-Hostadapter. |
| Native Kompositionen | Minecraft-Illager mit echtem Banner/Waffen, Skelettpferd mit animiertem Skelettreiter, innerer/äußerer Schleimkörper und passende native Tiermodelle. | Die installierten Minecraft-Rigs werden verwendet; ihre komplette Entity-KI wird dadurch nicht kopiert. |
| Eigene Ausarbeitung | Vengeful-Heart-Modell und Clips, Treetop-Farb-/Größenanpassung, Händlergesten, bestimmte Quelllücken sowie die gemeinsame Zeitabbildung. | Diese Änderungen sind als eigene beziehungsweise abgeleitete Arbeit gekennzeichnet. |

Treetop Whisperer verwendet die passende Whisperer-Grundgestalt in angepasster Größe und dunkel/violettem Erscheinungsbild; er wird nicht als unabhängig gefundenes Original-Bossasset ausgegeben. Das eigenständige Vengeful-Heart-Modell ist ebenfalls kein extrahiertes Original. Quellen, ausgewählte Dateien, Änderungen und Lizenzen stehen in [SOURCE_MODEL_AUDIT.md](SOURCE_MODEL_AUDIT.md), [THIRD_PARTY_NOTICES.md](../THIRD_PARTY_NOTICES.md) und den Importmanifesten.

Der Modellbestand schließt Gehen, ruhige Grundbewegung, Blickführung, passende Schwimm- oder Flugbewegungen, benannte Aktionen, mehrere Materialien, Handgegenstände und Leucht-/Effektpässe ein. Nicht jede Figur besitzt bereits einen eigenen vollständigen Satz aller denkbaren Intro-, Arbeits-, Schadens-, Flucht-, Schlaf- und Todessequenzen. Ein vorhandener Clip oder ein erfolgreiches Renderbild ist keine vollständige Animationsabnahme.

## Der gewünschte Bewegungseindruck

Actions & Stuff bleibt die gestalterische Referenz für lesbare Vorbereitung, Gewicht, weiche Übergänge und Tätigkeiten, die zum Weltzustand passen. Das Projekt übernimmt keine Dateien dieses Bedrock-Pakets. Die ausgewählten anderen Quellen behalten ihre eigenen Attributionen und Bedingungen; „eigene Adaption“ hebt diese Bedingungen nicht auf.

Jede Bewegung soll einen erkennbaren Anlass haben. Eine stehende Kuh kann atmen und schauen. Ein Vogel muss Flug- und Bodenzustand unterscheiden. Ein Geomancer soll eine gefährliche Aktion vor dem Treffer ankündigen. Ein Händler begrüßt bei echter Interaktion, während eine Arbeitsanimation einen tatsächlichen Arbeitszustand braucht. Dauerndes Gestikulieren ohne passenden Zustand würde wichtige Signale verdecken.

Körpergröße, Gelenklage und Textur gehören zur Bewegung. Der Renderer skaliert Quellenmodelle gleichmäßig, statt eine breite Vorlage in die alte schmale Prototypbox zu quetschen. Gemessene Ruhegrenzen enthalten tatsächliche Geometrie und Materiallayer; Sprünge, weit ausgreifende Arme und Kamerareserve sind gesondert zu betrachten.

## Kontakt, Übergang und Serverzustand

Die Gegner verwenden **15 serverseitige Aktionsarten**. Der Server legt Aktionsstart, Vorwarnziel, Freigabe, Erholung und Cooldown fest. Ein gebundener Quellclip kennt seinen wirklichen oder ausdrücklich adaptierten Kontaktzeitpunkt. Dieser wird exakt auf die Servervorbereitung abgebildet, bevor der verbleibende Clip in die Erholung übergeht. Die konkreten Zeiten und Effekte stehen in [MOBS_RUNTIME.md](MOBS_RUNTIME.md).

Bei Redstone Monstrosity sind beispielsweise Quellkontaktzeiten für SMASH und SPIT aus dem überprüften Quellverhalten abgeleitet. Eine mechanisch andere Quelle darf nicht nur wegen eines passend klingenden Clipnamens als Originalsequenz bezeichnet werden. [reference-hit-timings.json](../design/reference-hit-timings.json) dokumentiert die recherchierten Werte und benannten Anpassungen.

Nur die vom Aktionsclip belegten Kanalgruppen werden überblendet. Unbetroffene Gliedmaßen behalten aktuelle Bewegung und Blickrichtung. Jeder Frame beginnt bei der Bindepose; statische Skalen werden anschließend erneut gesetzt. Dadurch hinterlässt eine zuvor gerenderte Entity keine Pose für die nächste. Eine deklarierte Todesanimation wird in die bestehende 20-Tick-Todesfrist eingepasst und erhält keine zusätzliche Vanilla-Fallrotation.

Sprünge und Ortswechsel müssen weiterhin auf dem Server stattfinden. Ein Sprungclip kann den späteren Bodenkontakt begleiten, aber keine echte Flugbahn ersetzen. Der neue `ICY_EXPLOSION`-Zustand zündet 25 Ticks lang, verursacht Frostschaden und Verlangsamung und entfernt anschließend den eigenen Creeper. Seine Grafik darf keine tatsächlich zerstörten Blöcke oder lange Erholung nach bereits verschwundener Entity vortäuschen.

Die vier neuen Konstrukte nutzen einen zweiten, expliziten Zeitvertrag: Beim Freigabetick erzeugt der Zaubernde eine echte Entity. Deren synchronisierte Phase spielt Erscheinung, aktive Zeit, tatsächlichen Fall beziehungsweise Verschwinden. Der Eisblock muss sich in der Welt bewegen; eine reine Rigverschiebung genügt nicht. Der Bombentreffer erfolgt nach seiner sichtbaren Zündung. Bei einem zerstörten Konstrukt ersetzt die eigene Verschwindephase den generischen 20-Tick-Mob-Todpfad. Konkrete Zeiten und Sicherheitsgrenzen stehen in [CONSTRUCTS_AND_SWIMMING.md](CONSTRUCTS_AND_SWIMMING.md).

Beim Armored Trident Drowned erreicht der eigene Wurfclip nach 1,2 Quellsekunden seinen Abwurfmarker. Dieser wird auf die 24-Tick-Vorbereitung von `SOUL_BOLT` abgebildet, an der der Server einen echten `ThrownTrident` freigibt. Während dieser Vorbereitung ist der echte, synchronisierte Minecraft-Item-Use-Zustand aktiv: Dadurch wählt das native Handitem seine Wurfgeometrie mit richtig ausgerichteter Spitze. Die Freigabe beendet diesen Zustand, ohne einen zweiten Item-Wurfcallback auszuführen. Drowned-/Sunken-Schwimmpose und Unterwasserbewegung lesen den wirklichen synchronisierten Schwimmzustand. Der serverseitige Test prüft sowohl eine vertikale als auch eine horizontale Route; die grafische Beurteilung erfolgt zusätzlich anhand der tatsächlich aufgenommenen Zustandsdaten und Bilder.

## Produktionsmatrix für die zwölf Dungeons-I-Bosse

Die Modellgrundlagen sind bereits eingebunden. Die rechte Spalte nennt gezielte zusätzliche Abnahmen und Mechanikarbeit, keine pauschale Behauptung, dass die linke Seite ungeprüft vollständig sei. Für Identität und Originaleinordnung siehe [MOB_CATALOG.md](MOB_CATALOG.md).

| Boss | Aktuelle Grundlage | Nächste konkrete Abnahme / Ausbau |
| --- | --- | --- |
| Arch-Illager | Isolierter ReCrafted-CEM-Unterbaum, Blick-/Gangadaption, eigene Stab-/Orb-Ergänzung und vorhandene Serverfolge. | Die neue Stab-/Orb-Elternbindung und Handkontakte im aktuellen Clientlauf prüfen; unterscheidbare Beschwörungs-/Salvensignale, Teleportübergang und Begegnungsintro weiter ausarbeiten. |
| Corrupted Cauldron | Eigener Quellkessel mit getrennten Oberflächen und stationärem Serverprofil. | Oberfläche und Ausstoß aus realem Aktionszustand, Beschwörungsöffnung, Reaktion auf Schaden ohne unpassende Ganzkörper-Schleimverformung. |
| Heart of Ender | Passender ReCrafted-Ender-Unterbaum mit eigenen Ausdrücken. | Entfalten, Tentakel-/Bodenkontakt, Strahlursprung und Phasenübergänge; vollständige Original-Strahlmechanik bleibt eigene Arbeit. |
| Mooshroom Monstrosity | Passender Port-Rig mit Pilztextur, per-face UVs und Quellclips. | Gewicht im Schritt, Handkontakt beim Schlag, richtige Pilz-/Leuchtmaterialien und klare Erholung. |
| Nameless One | ReCrafted-Rig und aktuelle Zauber-/Teleportaktionen. | Stabhaltung, eigenständige Klonlogik, sichtbare Vorbereitung, Klon- und Niederlagesequenz. |
| Redstone Monstrosity | Vom Nutzer geforderter Goety-Rig mit dessen tatsächlichen Keyframes und Materialien. | Kontaktzeit im echten Kampf, Kiefer-/Spitbewegung, schwere Schritte, Übergänge und passende Arena-/Kernmechanik. |
| Jungle Abomination | Detaillierter ReCrafted-CEM-Körper mit Warden-Host und eigenen Ausdrücken. | Rankenursprung, Pflanzengewicht und Handkontakt; Warden-basierte Kampfclips durch gezielte eigene Bosssequenzen ergänzen. |
| Treetop Whisperer | Größere dunkel/violette Goety-Whisperer-Ableitung und echte temporäre Fesselranke. | Lesbarkeit der Blattkrone, Rankenbildung/-ende und sichtbaren Opferbezug prüfen; Giftmechanik und vollständige Originalbegleiterfolge bleiben offen. |
| Wretched Wraith | ReCrafted-CEM mit passender schwebender Gestalt. | Stoff-/Kältebewegung, gezielter Projektilursprung, Teleportübergänge und serverseitige Flugzustände. |
| Tempest Golem | ReCrafted-CEM mit Warden-Host. | Stationärer Säulen-/Schutzablauf, Aktivierung, sichtbare Verwundbarkeit und eigene Schwert-/Sturmsequenz. |
| Ancient Guardian | ReCrafted-CEM mit tatsächlicher Guardian-Hosthierarchie und aquatischer Bewegung. | Wenden unter Wasser, Blick-/Strahlursprung, etwaige Minen-/Begleiterzustände und Abstand zu Wänden. |
| Vengeful Heart of Ender | Eigenes ausgearbeitetes Modell mit eigenen Clips und bestehender Serverfolge. | Körperformen und konkrete Phasenübergänge, Angriffskontakt, Strahlführung und finales Auflösen. |

Bei Wildfire stehen die unabhängig treff- und zerstörbaren Schilde weiterhin als konkrete Mechanikarbeit aus. Bei Enderlings müssen Teleport, Landung und Angriff als Übergänge zusammenpassen. Die native Skeleton-Horseman-Komposition bewegt den Reiter mit dem Pferderücken, bleibt aber eine einzelne Gameplay-Entity. Ancient-Varianten brauchen ihre eigenen Begegnungsregeln; ein wiederverwendbarer Grundrig erledigt diese Regeln nicht.

## Friedliche Figuren und Dorfleben

Die friedliche Entityregistrierung umfasst Begleiter, Wildtiere, Schlüsselgolems, elf Dungeons-Händler und einen befreibaren `captured_blacksmith`. Bewegung und Interaktion kommen von echten `PathfinderMob`- beziehungsweise `AbstractVillager`-Entities. Ein Clientadapter liest ihre Zustände, ohne sie zu Monstern zu machen. Katzen-/Fuchshosts können den tatsächlichen Wartezustand als Sitzpose darstellen; andere Körperformen brauchen gegebenenfalls eine eigene passende Ruhepose. Beim gefangenen Schmied müssen Gefangenschaft, tatsächliche Befreiungsinteraktion und anschließendes Folgen derselben Entity nachvollziehbar bleiben; es ist keine zusätzliche Händler-Begrüßungsaufnahme.

Für weitere lebendige Weltabläufe werden weiterhin zusammenhängende Zustände gebraucht: Weiden oder Schnuppern, Aufschrecken, Flucht, Landen und Starten, Ruheplätze und Tageszeitverhalten. Ein vorhandener Flügelschlagclip allein stellt keine abgeschlossene Vogel-KI dar. Dekopartikel bleiben Ergänzungen; zuverlässige Questziele benötigen gespeicherte Entities und echte Ereignisse.

Die Händler begrüßen bei echter Interaktion oder Handel. Ihre Quellenmodelle können eigene Details wie Löffel, Werkzeuge oder Blüten über CEM-Ausdrücke bewegen. Der Feldführer zeigt eine ausdrücklich lokale Vorschaugeste; er löst weder Warenhandel noch Questerfolg aus. In der Welt laufen zeitorientierte Effekte mit der Weltzeit. Im pausierten Feldführer wird die lokale Entityalter-Uhr verwendet, damit Vorschaufiguren und Texturfolgen weiterlaufen.

Die elf Dungeons-Händler und die gewöhnlichen beziehungsweise eigenen Dorfberufe sind verschiedene Systeme. Berufsarbeit, Handel, Panik, Schlaf und Kinderzustand werden in [VILLAGERS_AND_ANIMATIONS.md](VILLAGERS_AND_ANIMATIONS.md) behandelt. Die Händlerangebote bleiben aktuelle Minecraft-Waren; originale Aufwertungs-, Geschenk- oder Towerdienste benötigen eigene Spielmechanik und passende neue Zustände.

## Materialien, Hände und Oberflächen

Mehrere Materialien besitzen denselben vollständigen Rigbaum. Vor dem Draw werden Position, Rotation, Skala und Sichtbarkeit über exakt gleiche Elternpfade kopiert. So bleiben native Grundtextur, darüberliegende Rüstung oder ein separates Schaffell auch in Bewegungen zusammen. Eine bloß ähnlich benannte Endhand reicht für ein Item nicht aus: Der Handlayer durchläuft alle Elterntransformationen bis zum tatsächlichen Anker.

Vorhandene Meshwaffen erhalten keinen zusätzlichen Itemstack. Armbrust, Axt, Bogen und Hauptmannbanner der entsprechenden Kompositionen kommen dagegen aus der tatsächlichen Ausrüstung. Im Client sind insbesondere Ausrichtung, Handseite, zu große/kleine Gegenstände und Durchdringungen beim Ausholen zu prüfen.

Leuchtmasken werden getrennt von normal beleuchteten Materialien gerendert. Transparente Hüllen benötigen einen transparenten Pass. Flame Construct verwendet die geprüfte Folge von 31 nummerierten Feuerframes; alle Frames müssen vorhanden sein und mit der richtigen Welt-/Vorschauuhr wechseln. Überlappende Außenflächen werden geometrisch korrigiert, statt Flimmern durch eine größere Texturauflösung zu kaschieren.

Tropical Slime verwendet die echte transparente Schleimhülle und den getrennten animierten Fischkern. Die 47 Fisch-PNGs sind eine zeitliche Quellenfolge, keine behaupteten 47 neuen Farbvarianten; der zusätzliche Zyklusslot folgt der dokumentierten Quellbelegung. Beim Diamond Armored Vindicator wird die jetzt ausgerüstete Whirlwind-Handwaffe im vorhandenen Elternpfad gerendert. Spielerwaffen-Sondereffekte werden dadurch nicht automatisch auf den Gegner übertragen.

## Weitere Arbeit in sinnvoller Reihenfolge

1. **Die aktuelle Quellenfassung im echten Spiel beurteilen.** Ruhe, tatsächliche Bewegung und gültige Serveraktion sowie Ausrüstung und Materialpass ansehen. Konkrete Fehler je Profil festhalten und gezielt nachprüfen.
2. **Originalmechaniken einzeln ergänzen.** Für einen Boss Arenazustand, Angriffshilfe, Treffer, Erholung und Darstellung zusammen fertigstellen. Die vorhandene gemeinsame Aktion bleibt eine Grundlage, kein Beweis identischer Originalfunktion.
3. **Eine Region zusammenhängend beleben.** Begrenzte Populationen, passende Spawns, Tagesabläufe, Händler, freiwillige Quest und Begegnung miteinander prüfen, bevor weitere Schwärme hinzugefügt werden.
4. **Offene Nicht-Sift-Profile aus dem Statusmanifest abarbeiten.** Tatsächliche Modellquelle und Registrierung unterscheiden; Rüstungsvarianten und Ancients nicht automatisch als eigenständige abgeschlossene Kreaturen zählen.
5. **Qualität erhalten.** Herkunftsmanifest, Rigprüfung, reale Weltaufnahmen und konkrete Einschränkungen bei jedem weiteren Import mitführen. Die Sift sind kein Produktionsziel dieser Fassung.

## Grafische und technische Abnahme

Die vorhandenen Unit-/Rigtests prüfen unter anderem Quellkontakt, Rückkehr zur Grundpose, Null-Längen-Skalen, Hostaliasauflösung, CEM-`this`, Elternpfade für Gegenstände, materialübergreifende Pose und lange Frameuhren. `ReferenceAssetRigTest` prüft jede Definition getrennt mit `assertAll`, damit eine fehlerhafte Quelle die übrigen Ergebnisse nicht verdeckt. Servertests prüfen echte Zustände und Wirkungen. Die tatsächlich ausgeführten Ergebnisse stehen ausschließlich im aktuellen Prüfbericht; ein Quellcodeeintrag ist kein Testnachweis.

Für die grafische Abnahme eine Figur im Stillstand, Gehen, Richtungswechsel und Angriff auf ebenem und unebenem Boden ansehen. Sichtbarer Kontakt und wirklicher Treffer müssen zusammenliegen. Zielverlust, Rückstoß, Teleport, Tod und erneutes Laden dürfen keine eingefrorene Haltung hinterlassen. Schwimmen, Sitzen und Flug sind mit tatsächlichen Weltzuständen zu prüfen. Bei Bossen gehören Modellgröße, Hitbox, Reichweite und Kameranähe gemeinsam zur Beurteilung.

Der Entwicklungs-Showcase erstellt native Minecraft-Aufnahmen aus einer ausdrücklich vorbereiteten Testwelt. Er ersetzt weder natürliche Weltgenerierung noch die abschließende gestalterische Sichtprüfung. Eine größere Aufnahmezahl ist kein automatischer Qualitätsnachweis. Für dichte Szenen werden Server-Tickzeit und Client-Frametime tatsächlich gemessen; Messwerte werden nicht aus der Anzahl von Modellen geschätzt.

Die 0.4-Aufnahmen umfassen zusätzlich eine Konstrukt-Prüfszene. Dabei sind besonders die sichtbare Pfeilerhöhe, freie Formation ohne eingeklemmte Entities, der Formationsring des Eises, der tatsächliche Fall und die Endphase zu prüfen. Die vollständige native Abnahme wird nicht allein aus dem erfolgreichen Common-Testlauf oder dem gestarteten 131-Figuren-Showcase abgeleitet. Nur tatsächlich angesehene Frames mit ihrem Aufnahmeursprung und Hash gehören in den visuellen Prüfbericht.

Eine konkrete Erstlaufkorrektur betrifft das Eis-Material: Sein Quellrenderer verwendet echte Alpha-Transparenz. Der zunächst genutzte Cutout-Pass machte die magentafarbene Formationshülle deckend, obwohl der Atlas an diesen Stellen Alpha64 enthält. Der korrigierte Pass erhält die Originalpixel und den am Entstehungspunkt sitzenden Quellring. Der [Konstrukt-Sichtbericht](../reports/v04-construct-visual-qa.json) trennt diese belegte Erstbeobachtung von der nachfolgenden tatsächlichen Sichtprüfung des korrigierten Clients.

Diese gezielte Nachprüfung ist inzwischen erfolgt: Im vollständigen Featurelauf `20261102-113549` bleibt die blaue Eisstruktur während der Formation durch die transparente Hülle sichtbar. Fall, Landungsring und natürliche Enden aller vier Konstrukte wurden in ausgewählten echten Frames geprüft; der Bericht enthält Bildhashes und die dazugehörigen Entfernungsmetadaten. Das ersetzt keine vollständige Abnahme jeder Modfigur oder jedes Animationsframes.
