# Inhaltsplan – Dungeons-Welt in einer freien Sandbox

Stand **0.4.0-alpha, 2. November 2026**. Tatsächlich ausgeführte Prüfungen: [VALIDATION.md](VALIDATION.md). Laufzeitbestand: [IMPLEMENTED_CREATURES.md](IMPLEMENTED_CREATURES.md).

## Ziel

Eine frei bebaubare Minecraft-Welt mit Dungeons-Kreaturen, lebendigen Tieren, ausdrucksstarken Bewohnern und freiwilligen Abenteuern. Quests ergänzen Erkunden und Bauen. Ruhige Gebiete, begrenzte Tiergruppen und seltene Begegnungen gehören zum Weltgefühl. Sift-Inhalte bleiben auf ausdrücklichen Nutzerwunsch ausgeschlossen.

**Ausdrücklich bestätigter Schwerpunkt:** sichtbare Schwärme, Herden/Rudel, natürliche Lebensräume und Bewohneralltag. Das Ziel geht über einzelne Spawns hinaus. [LIVING_WORLD.md](LIVING_WORLD.md) trennt die vorhandenen Grundlagen, den neuen Händlerbaustein und die noch geplanten Gruppenmechaniken.

## Erreichter Stand

| Bereich | Vorhanden | Weiteres Qualitätsziel |
| --- | --- | --- |
| Kreaturen | 131 registrierte Figuren: 78 Gegner, 53 friedliche | Verbleibende Originaltypen und exakte Varianten einzeln ergänzen |
| Bosse/Minibosse | Zwölf Bossgrundtypen, neun Minibosse; konkrete individuelle Quellen-/eigene Modelle | Originalphasen, Spezialobjekte, Arenaabhängigkeiten, ausführliche Kampfabnahme |
| Modelle | Java-/Bedrock-/CEM-Konverter, getrennte Materialien, Körper-/Handpfade, Leuchtmasken, lizenzierte Quellsnapshots | Gezielte Restfehler anhand tatsächlicher Ansichten und Originalreferenzen schließen |
| Animation | Quellclips mit Kontaktzeitabbildung, CEM-Ausdrücke/Hostbewegungen, eigene Vengeful-Sequenzen, 15 Aktionsarten | Individuelle Reaktionen, Tod, Übergänge und Eigenheiten aller Figuren |
| Friedliche Welt | 35 Begleiter, vier Wildtiere, zwei Schlüsselgolems mit eigenen Formen; gespeicherter Besitz/Warten/Folgen | Begrenzte natürliche Habitate, Flucht/Landen, Zucht, Schlüssel-Türen, besondere Begleiterfähigkeiten |
| Händler | Elf eigene Figuren mit echtem Warenhandel und Begrüßung | Upgrade-, Geschenk-, Gild-/Unique-Dienste mit sicheren Inventarregeln |
| Dorfbewohner | Sechs Berufe, sechs Arbeitsplätze, 60 Angebote; 19 Arbeitsbewegungen | Langfristiger Dorfalltag und Reaktionen im realen Mehrspielerbetrieb |
| Abenteuer | 21 Siegel, 29 Grundquests, persönlicher Fortschritt/Annahme/Belohnung | Zwei optionale eigene Lager vorhanden; weitere Orte, NPC-Dialoge und regionale Geschichten |
| Atmosphäre | Schmetterlinge, Glühwürmchen, Sporen; optionaler Spawn-Datapack für zwölf Gegnergruppen | Habitatverhalten, langfristige Spawnbalance und weitere kleine echte Tiere |
| Ausrüstung | Quellstand: zwölf Artefakte inkl. Windhorn, sechs Nahkampfwaffen, vier Fernkampfwaffen, drei Rüstungssets mit zwölf Teilen; 19 neue Items noch ungeprüft | Vollständiger verifizierter Originalkatalog, weitere Formen/Mechaniken, Dungeons-Verzauberungen/Upgrades, Survivalbalance und beidhändige Faustanimation |
| Konstrukte | Geomancer-Wand/-Bombensäule, Ice Chunk, Entangle Vine mit endlicher echter Serverwirkung | Individuelle Bossverwendung und weitere Ablaufabnahme |
| Quellen | 436-Zeilen-Audit, 47 klare Sift-Ausschlüsse, dateigenaues Lieferinventar | Offene Dungeons-II-/Ancient-Varianten verifizieren und materialisieren |

## Nächste Produktionsetappe

**Aktuell im Quellstand umgesetzt:** gespeicherte Heimat und Tages-/Wetter-/Schadensreaktion für Lagerhändler sowie natürliche Wollkuh-Herden, Geier- und Irrlicht-Fluggruppen mit begrenzter Mitgliedschaft, Wiederanschluss, lokaler Führung und gemeinsamem Alarm. Diese Quelländerungen warten noch auf den echten Build-, Server- und Bewegungstest; sie gehören nicht zum bisherigen Auslieferungsnachweis. Weitere Arten, Fernwanderungen und besondere Interaktionen folgen als eigene Erweiterungen. Der Ausrüstungsausbau ergänzt konkrete Fernkampfzustände, Quell-Rüstungsgeometrie und serverseitige Set-/Artefaktregeln. Die 34 Ausrüstungsitems sind weiterhin nur eine Auswahl; vollständige Dungeons-Verzauberungen und Upgrade-Dienste fehlen.

### 1. Einzelne Kreaturen bis zur vollständigen Erkennbarkeit ausarbeiten

Für jede offene Zeile Quelle, Silhouette, Proportionen, Material, Gliedmaßen und charakteristische Bewegung prüfen. Ein vorhandenes Vanilla-Skelett ist kein automatisch fertiger Ancient. Ein umgefärbtes Whisperer-Rig kennzeichnet eine Treetop-Adaption, keine bewiesene identische Originalform. Die eigene Vengeful-Nachbildung bleibt anhand zusätzlicher Blickwinkel und Originalbewegungen weiter abstimmbar.

Die zwei nicht registrierten Modellvorlagen `mage` und `imperfect_heart_of_ender` zählen nicht zum Mobumfang. Der Mage ist ein Community-Mod-Kandidat; die Imperfect-Heart-Form gehört zu einer Szene. Die bisherigen vier Konstruktvorlagen sind inzwischen echte gesonderte Laufzeitentities mit Wirkung und begrenzter Lebensdauer.

### 2. Einen charakteristischen Bosskampf durchgängig ergänzen

Die Redstone-Monstrosität verwendet bereits das zur Nutzerreferenz passende Goety-Modell. Darauf die konkrete Bomben-/Arena-Interaktion ergänzen: klarer Auslöser, lesbare Vorbereitung, serverseitiger Effekt, Erholung, Tod/Abbruch und Speicherung. Die aktiven SUMMON-/SMASH-/SPIT-Clips erhalten ihre dokumentierten Kontaktpunkte. Spielerbauten und bestehende Quests bleiben erhalten.

Andere spezifische Aufgaben sind Tempest-Säulen, die Nameless-Klonsequenz, die eigentlichen Herz-Phasen und eine vollständige Unterwasserbegegnung. Mehr gemeinsame Partikelangriffe ersetzen diese Mechaniken nicht.

### 3. Tiere und Bewohner in natürliche Orte einbetten

Spawns pro Biom begrenzen, auf geladene Chunks beschränken und die Tierrollen trennen. Die jetzigen Haustiermodelle liefern bereits Interaktion und Eigentümerstatus; sie sind keine automatisch ausbalancierte Weltpopulation. Die optionalen Händler-/Gefangenenlager liefern bereits echte kleine Orte und einen befreibaren Schmied. Ihre natürliche Platzierung an Hängen und verschiedenen Weltsamen sowie eine zusammenhängende Reise benötigen weitere Arbeit.

Schmied-, Geschenk- und Verzaubererdienste sind eigene durchgängige Aufgaben. Itemkomponenten, Namen, Verzauberungen, Zahlung, Wiederholungsschutz, Abbruch und Speicherung gemeinsam behandeln. Die vorhandenen normalen Trades erfüllen diese speziellen Dienste noch nicht.

### 4. Abnahme und Quellenpflege

Rigvalidierung, tatsächliche Entity-Registrierung, visuelle Vergleichsansichten, serverseitige Wirkung und langfristige Spielbarkeit getrennt protokollieren. Kopf-/Handkontakt, Wasser, Tod, Zielwechsel, zwei echte Clients und reale Berufsarbeit bleiben wichtige Prüfsituationen. Die vorhandenen Belege nicht als vollständige Qualitätsfreigabe für ungeprüfte Situationen ausgeben.

Das aktuelle Lieferinventar und die vollständigen Lizenzen gehören zu jeder Weitergabe. ReCrafted-Kunst und Ableitungen behalten CC BY-NC-SA 4.0; eigene und ausgewählte übrige Quellen behalten ihre jeweilige MIT-Zuordnung. [THIRD_PARTY_NOTICES.md](../THIRD_PARTY_NOTICES.md) ist maßgeblich.
