# Lebendige Welt: aktueller Ausbau

Minecraft bleibt die frei bebaubare Sandbox. Dieser Ausbau verbindet Landschaft, Tierleben und freiwillige Dungeons-Abenteuer. Er ergänzt vorhandene Figuren und Gegenstände; er ist kein vollständiger Port aller Katalogeinträge und keine neue Minecraft-Version.

**Prüfstand, 2. November 2026:** Alle aktuellen Javaquellen kompilieren gegen die echten Minecraft-/NeoForge-Klassen; **137 JUnit-Tests und 123 erforderliche Server-GameTests bestehen ohne Fehler**. [Java-Nachweis](../reports/local-java-check.json), [echter Serverlauf](../reports/living-world-gametest.json), [Gesamtstand](../reports/living-expansion-validation.json). Beide Prüfungen verwenden dieselbe direkte Test-JAR; ein neuer Gradle-Release ist das nicht. Die grafische Abnahme bleibt offen: Der direkte macOS-Clientversuch erzeugte wegen des GUI-/XPC-Startproblems keine neue Aufnahme. Die bisherige Creative-Instanz und ihre Welt bleiben unverändert. Historische Bilder und der vorherige Build mit 97 JUnit-Tests sind kein Sichtnachweis dieses Ausbaus.

## Landschaft und Entdeckung

Der neue Welttyp **Dungeons Alive: Lebendige Welt** behält die Vanilla-Klimaverteilung, Höhlen, Dimensionen und den normalen Terrain-Generator. Er ergänzt regionale Varianten: Bernsteinhain, Nebelbruch, Glutklippen und den Verderbten Hain. Die vierte Auswahl ist im Codec optional, damit zuvor gespeicherte dreiteilige Einstellungen lesbar bleiben.

Die neuen Biome benötigen diesen Welttyp beim Erstellen einer Welt. Bestehende Spielstände behalten ihren Generator. Zusätzliche Moosstellen, Totholz, kleine Felsen und andere Dekorationen ergänzen passende gewöhnliche Biome nur bei der Erzeugung neuer Chunks. Alte Bauwerke werden nicht nachträglich mit Weltvorlagen überbaut.

Fünf baubare Materialien beziehungsweise Funktionsblöcke ergänzen die Welt: Bernsteinmoos, Leuchtmoos, uralte Steinziegel, gemeißelte uralte Steinziegel und der alte Wegstein. Die Modelle sind eigene Definitionen und referenzieren installierte Minecraft-Texturen; Minecraft-PNGs wurden nicht in den Mod kopiert.

Drei eigene natürliche Abenteuerorte enthalten je ein Siegel und begrenzte Vorräte: überwucherte Arena, verlassener Redstone-Schachtplatz und versunkenes Heiligtum. Die zugehörigen Gegner sind Corrupted Cauldron, Redstone Monstrosity und The Nameless One. Betreten und Plündern starten keinen Kampf. Die bisherigen Siegelregeln, Platzprüfung und bewusste Aktivierung bleiben erhalten.

Jeder Ort besitzt einen schmalen verwitterten Zugang und zwei Steinmale. Ein Wegstein liest beim Rechtsklick die tatsächlichen Strukturstarts bereits geladener Chunks in der Umgebung. Er kann einen vorhandenen Ort benennen und dessen Richtung anzeigen. Er führt keinen `locate`-Aufruf aus und erzeugt keine Such-Chunks; außerhalb bekannter Spuren meldet er keine erfundene Zielposition.

## Tiere, Gegner und Reisen

Alle 64 Vanilla-Biome haben eine ausdrückliche Habitatzuordnung; der technische Void bleibt leer. Die vier neuen Biome sind ebenfalls zugeordnet. Oberflächenvögel werden unterirdisch nicht als Baumkronengruppen ausgewählt. Nether, End, Wasser und Höhlen verwenden passende eigene Zusammenstellungen.

Vierzehn vorhandene Begleiterprofile können zusätzlich echte wilde Tiere sein. Nur natürliche beziehungsweise Chunk-Generierungs-Spawns erhalten diesen Status. Früher gespeicherte Haustiere, manuell gesetzte Figuren und Besitzer bleiben erhalten. Übernahme beendet den wilden Gruppenmodus. Die Gruppengröße liegt je nach Art bei zwei bis fünf; gemeinsame Routen, Pausen, Alarm und Flucht ergänzen normales Herden- und Flugverhalten. Wassergruppen benötigen tatsächlich zusammenhängenden, ausreichend großen freien Wasserraum.

Die zusätzlichen Begegnungen beachten `doMobSpawning`, Schwierigkeit beziehungsweise Habitat, Konfiguration, geladene Chunks, ein lokales Figurenlimit und standardmäßig höchstens 96 geladene wilde Figuren dieser Systeme pro Dimension. Ein gespeicherter Regionsabstand verhindert, dass mehrere Spieler an derselben Stelle ständig Gruppen nachfüllen. Dekopartikel zählen nicht als Tiere oder Questziele.

20 ausgewählte reguläre Gegnerarten sind über neun Habitatfamilien in die natürlichen Spawnlisten eingebunden. Die sechs früheren Wilds-Modifikator-IDs bleiben erhalten, damit ein bereits installiertes Legacy-Pack sie überschreibt und die Spawngewichte nicht verdoppelt. Bosse und Minibosse erscheinen dadurch nicht zufällig. Monsterlimit, Lichtregeln und normale Entfernung bleiben wirksam.

Karawanen bestehen aus einem Dorfhändler und zwei kleinen Wollkühen. Sie bewegen sich auf kurzen geprüften Routen, halten an, reagieren auf Handel und Gefahr und speichern ihren Gruppenstatus. Je Dimension sind höchstens zwei Gruppen reserviert, auch wenn ihre Chunks entladen werden. Vorhandene Lagerhändler und gegründete Siedlungen werden nicht umfunktioniert. Begleiter lassen sich weiterhin übernehmen.

## Benutzung hinterlässt Spuren

Wiederholtes tatsächliches Überqueren von Gras, Podsol oder Myzel trägt den Boden ab: standardmäßig nach sechs Querungen grobe Erde, nach zwölf ein Trampelpfad. Stillstehen zählt nicht. Fliegen, Zuschauen, Schleichen und Reiten erzeugen keine zusätzlichen Fußspuren. Pflaster, Ackerland und gewöhnliche mit Werkzeug angelegte Wege gehören nicht zu diesem System.

Unbenutzte gespeicherte Pfade werden nach zehn Minuten Spielzeit erst wieder grobe Erde und nach weiteren zehn Minuten zum ursprünglichen Boden. Erneutes Betreten verschiebt die Erholung. Es werden ausschließlich tatsächlich vom System aufgezeichnete Pfade zurückverwandelt. Spätere Änderungen am Block werden nicht überschrieben. Neu vom Spieler gesetzter geeigneter Boden wird geschützt; vor Installation entstandene natürliche und gesetzte Grasblöcke sind rückwirkend nicht unterscheidbar. Schutzintegrationen können `TrailChangeEvent` abbrechen. Verlauf, Schutzpositionen und Arbeit pro Tick haben feste Grenzen; entladene Chunks werden nicht zum Nachwachsen geladen.

## Wasserbewegung

Ein lokales Strömungsmodell liest vorhandenes Wasser, dessen Fließrichtung, Breite, Tiefe, Gefälle und Abstand zum Ufer. Engstellen und Gefälle erhöhen die Geschwindigkeit, Uferbereiche sind ruhiger. Die Kräfte betreffen Schwimmer, lose Gegenstände und Boote. Die Serverberechnung sendet begrenzte additive Impulse an den steuernden Spieler; Clients entscheiden nicht über die Stärke. Schwimmen und Paddeln bleiben möglich, die senkrechte Bewegung wird nicht ersetzt.

Vorhandene Vanilla-Fließrichtung hat Vorrang. Flache Vanilla-Flüsse besitzen keine durchgehenden hydrologischen Daten. Für deutlich erkennbare Flusskanäle wird deshalb eine örtliche Richtung angenähert; das ist keine Simulation von Wassermenge, Einzugsgebieten oder einem vollständigen Abfluss bis zum Meer. Seen, Ozeane und symmetrische stehende Becken erhalten keine beliebige Flussströmung. Wasserblöcke, Redstone-Wasseranlagen und Terrain werden durch dieses System nicht umgebaut. Sichtbare kleine Wasserbewegungen sowie Nebel an tatsächlichen Wasserfällen ergänzen die vorhandenen Atmosphärepartikel.

## Entdeckte Verderbnis und Rückgewinnung

Der Verderbte Hain wird erst aktiv, wenn ein Spieler ihn betritt. Eine gespeicherte Naturfront verfärbt geeignete natürliche Grasstellen langsam auch über Biomegrenzen hinweg. Maximal acht erinnerte Regionen, 48 Blöcke Radius und 256 veränderte Stellen je Region halten den Vorgang endlich. Pro Sekunde werden insgesamt höchstens 64 Kandidaten geprüft und vier Blöcke geändert. Es werden keine entfernten Chunks dafür geladen.

Nur bei neuer Weltgenerierung reservierter unberührter Boden ist geeignet. Eine unsichtbar wie Erde dargestellte Unterbodenschicht kennzeichnet solche Naturstellen; Spielerinteraktionen und Bauänderungen entwerten den Status konservativ. Alte Spielstände werden nicht rückwirkend mit Markierungen versehen. Die Bodenmarkierung und der befallene Boden haben bewusst kein eigenes BlockItem; ihre normalen Erde-Drops erzeugen keine weiterverbreitbaren Marker.

Ein herstellbarer **Reinigungsfokus** stoppt die Front in seinem Bereich sofort. Der Server stellt gespeicherte ursprüngliche Grasblöcke danach schrittweise wieder her, soweit sie geladen und unverändert sind. Fremde Blockänderungen werden nicht überschrieben. Der Fokus wird nur bei erfolgreicher Aktivierung verbraucht; es entsteht keine automatisch angenommene Quest. Die Biome-IDs bereits erzeugter Chunks werden dabei nicht umgeschrieben: Ausbreitung und Reinigung betreffen die wirkliche Naturfront, während der ursprüngliche Landschaftstyp erhalten bleibt.

## Abenteuer, Beute und Bewohner

14 ausgewählte Vanilla-Truhentabellen können zusätzlich ein vorhandenes Ausrüstungsitem erhalten. Normale Tabellen haben 30 Prozent Chance, späte Abenteuertruhen 40 Prozent; bei letzteren sind fünf Prozent der zusätzlichen Funde einzigartig. Das entspricht zwei Prozent pro betroffener Endgame-Truhe. Vorhandener Inhalt und Item-Komponenten bleiben erhalten. Bereits erzeugte oder geleerte Truheninventare werden nicht aufgefüllt.

Ein tatsächlich vom Spieler besiegter, nicht beschworener Boss kann einen Siedlungsbrief hinterlassen. Bereits verarbeitete Boss-UUIDs werden gespeichert. Der Brief gründet nach Platz- und Rechteprüfung einen kleinen Standort mit Dorfhändler und Schmied. Der Standort benötigt freie sichere Fläche; es werden keine Gebäude automatisch über Spielerblöcke gesetzt. Der Brief wird im Survivalmodus nur bei erfolgreicher vollständiger Platzierung verbraucht. Höchstens drei Standorte je Spieler und 64 je Welt, mindestens 96 Blöcke Abstand sowie ein lokales Händlerlimit begrenzen die Gründung. Es gibt keine automatische NPC-Nachproduktion.

## Bedienung und Darstellung

Ein eigener Creative-Reiter führt alle tatsächlich registrierten Moditems einschließlich Blöcken, Siegeln, Ausrüstung und Spawn-Eiern auf. Die Einträge kommen aus der Registry und nicht aus einer zweiten handgepflegten Liste. Deutsche und englische Namen werden gegen die tatsächlichen Item-Beschreibungsschlüssel geprüft.

Journal, Figurenhandbuch, Ausrüstungsansicht, Händlerdienst-Fenster und HUD erhalten eigene dunkle Steinflächen, Messingeinfassungen und zurückhaltende Seelenfarben. Die Gestaltung verwendet eigene UI-Geometrie. Buttons behalten Tastaturbedienung, Narration und vorhandene Aktionen. Questannahme und Belohnungsabholung bleiben bewusste, serverseitige Schritte.

## Inspiration und Grenzen

[Desire Paths](https://modrinth.com/mod/desire-paths) liefert die Idee benutzter Wege; hier wachsen auch die letzten Pfadstufen wieder zu. [Immersive Weathering](https://modrinth.com/mod/immersive-weathering) inspiriert zusammenhängende Naturveränderungen, [Particular](https://modrinth.com/mod/particular) kleine Umweltdetails und [Streams](https://www.curseforge.com/minecraft/mc-mods/streams) bewegtes Flusswasser. Dafür wurden keine fremden Moddateien oder Assets übernommen. Es handelt sich um eigene Implementierungen für den bestehenden NeoForge-Mod, nicht um Aussagen zur Kompatibilität oder Installation dieser Inspirationsmods.

Die übrigen Herkunftshinweise für bereits übernommene Kreaturen-, Animations- und Ausrüstungsassets bleiben unverändert maßgeblich. Eine erfolgreiche Kompilierung oder Serverprüfung ersetzt keine native Sichtprüfung der neuen Landschaft und Oberflächen.

## Verbindlicher nächster Ausbau: beobachtbare Natur zuerst

Die unveränderte [Nutzervorgabe](../MINECRAFT_2_IDEEN_FUER_CODEX.md) ist seit dieser Übergabe das verbindliche Produktziel. Der [Ideenindex](../design/minecraft2-vision-status.json) erhält alle **223 Ideen-IDs und Originalprioritäten**. Alle 69 P0-Ideen und die relevanten M1-/M2-Grundlagen sind einzeln zugeordnet; übrige Ideen bleiben ausdrücklich `not_assessed`. Der oben beschriebene Ausbau ist eine servergeprüfte Grundlage, keine Abnahme von M1 und kein vollständiges „Minecraft 2“.

1. **M1 – beobachtbares Naturgebiet:** `WORLD-17` benötigt den vollständigen Vogelschwarmablauf von Anflug und Kreisen über erreichbare Sitzplätze, Landen, Ruhen und Sitzwechsel bis zu versetzten Starts, Neuordnung und Weiterflug. Der bestehende Flugzustand `REST` bedeutet keine echte Sitzruhe. `WORLD-18` benötigt eine tatsächliche Entenfamilie mit Jungtieren, Nachzügler-Warten, gemeinsamem Schwimmen und sicheren Uferwechseln zum Fressen, Putzen und Ruhen. Der vorhandene Entenbegleiter, die Wassernavigation und Wollkuh-Elternfolge sind keine fertige Entenfamilie. Der Spieler muss diese Abläufe ohne Quest, Belohnung oder eigene Aktion beobachten können. Gespeicherte Identität, begrenzte Alarmflucht und Wiederaufnahme gehören dazu.
2. **M1 – bewohnter Ort:** Danach folgen ein frei gebautes nutzbares Gasthaus, eine tatsächlich begehbare Brücke oder andere Verbindung, ein ankommender Besucher und eine kleine freiwillige Aufgabe. Fehlende Erreichbarkeit muss konkret erklärt werden; eine bereits vorhandene gültige Brücke muss zählen. Gegenwärtige Siedlungsanker und Karawanen ersetzen diese durchgängige Funktion nicht.
3. **M2 – erste Maschine:** Erst anschließend verbinden Kurbel/Wasserantrieb, Welle, Winde und Redstone einen echten begrenzten Ablauf mit Stoppen, Richtung, Last, Diagnose und sicherer Versuchsszene. Lokale Wasserströmungen sind noch kein Kraft- oder Pumpnetz.

Kapitel 21 der Vorgabe bestimmt die Abnahme. Native Einzelbilder können Landschaft, Figuren und UI zeigen, aber keinen vollständigen Flug-/Landungs-/Startzyklus, keine Entenfamilie am Ufer und keine Zwei-Client-Kooperation beweisen. Laufende Entwicklung muss je Ideen-ID vorhandene Teile, Dateien, Herkunft, tatsächliche Prüfung, Lücke und nächsten Schritt festhalten.

Weitere ausdrücklich verlangte Grundlagen bleiben offen, insbesondere persistente Werkstattaufträge, exklusive Materialreservierungen, echte Warenzustellung, allgemeine Vorratsrechte und gemeinsame Questanerkennung. Sie folgen der Etappenfolge; unmittelbarer Händlerkauf oder eine gespeicherte Karawane erfüllen diese Anforderungen nicht. Frühere eigene Vorschläge zu Ökosystemfolgen, erinnernden Siedlungen und mehreren Ruinenlösungen werden nur innerhalb der passenden Ideen der verbindlichen Vorgabe weiterverfolgt.
