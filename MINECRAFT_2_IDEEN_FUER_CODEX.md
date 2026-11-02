# Minecraft 2 / Dungeons Alive – Ideen, Mod-Inspiration und Arbeitsgrundlage für Codex

**Für:** LaugenBreze und den weiterarbeitenden Codex-Agenten  
**Stand:** 2. November 2026  
**Dokumenttyp:** vollständige Design- und Ideenbasis, einschließlich des bisherigen Gesprächsentwurfs und zusätzlicher Recherche.  
**Arbeitsname:** „Minecraft 2“ beschreibt hier das eigene Projekt.  
**Ziel:** normales Minecraft mit einer lebendigen, ausdrucksstark animierten Welt, Dungeons-Inhalten, freiwilligen Abenteuern und neuen kombinierbaren Baukastensystemen.

**Präzisierte Hauptpräferenz:** Die Atmosphäre soll auch beim bloßen Zuschauen tragen. Der Nutzer nennt **Crimson Desert** und **ARK: Survival Evolved** als Referenzen und wünscht ausdrücklich **Vogelschwärme am Himmel sowie gemeinsam schwimmende Entenfamilien**. Solches selbständiges Alltags- und Gruppenverhalten gehört früh in den spielbaren Kern.

**Leserouten:** Kapitel 0–2 erläutern Auftrag und Richtung. Kapitel 3–18 enthalten den vollständigen Ideenbestand mit stabilen Kennungen. Kapitel 19–21 machen daraus spielbare Abschnitte und überprüfbare Arbeit. Kapitel 22–23 enthalten die Skill-Anweisung und den kopierbaren Codex-Einstieg. Kapitel 24–25 dokumentieren Quellen und Community-Impulse.

## 0. Einstieg für Codex

Lies dieses Dokument vollständig als Produktvision. Untersuche anschließend den tatsächlichen aktuellen Projektstand. Der Nutzer hat zwischenzeitlich mit Codex weitergearbeitet; frühere Gesprächsstände und vorhandene Übergabedokumente können veraltet sein. Dieses Ideenpapier bestätigt keine bereits implementierte Funktion und ersetzt keine Prüfung des Repositories.

Arbeite am vorhandenen Mod weiter. Erhalte funktionierende Inhalte, Registrierungen, gespeicherte Daten und die bereits gewählte Plattform. Ein Minecraft-/Loader-Wechsel oder eine komplette Neuentwicklung ist kein impliziter Bestandteil dieser Ideenliste. Wenn der vorhandene Code begründete andere Architekturentscheidungen trifft, dokumentiere den Abgleich und integriere passende Ideen schrittweise.

**Der nächste sinnvolle Auftrag:** Bestand erfassen, die unten beschriebene erste spielbare Region gegen den Bestand abgleichen und den wichtigsten fehlenden zusammenhängenden Ablauf fertigstellen. Die langfristige Wunschliste ist keine Aufforderung, alle Systeme gleichzeitig anzufangen.

### Verbindliche Wünsche aus dem Gespräch

1. Normales Minecraft bleibt spielbar: gewohnte Perspektive, freies Platzieren und Abbauen, Survival, Farmen, Redstone und Kreativmodus.
2. Die Welt soll sich lebendig anfühlen: fliegende Kleintiere, Umgebungsreaktionen, aktive Bewohner, hörbare und sichtbare Tätigkeiten, Quests und besondere Begegnungen.
3. Langfristig sollen die belegten Kreaturen und Gegenstände aus Minecraft Dungeons vollständig erfasst und möglichst detailgetreu umgesetzt werden: normale Mobs, Varianten, Bosse, Minibosse, Tiere, Begleiter, Händler, Waffen, Rüstungen, Artefakte und passende weitere Inhalte.
4. Für im Gespräch als „Dungeons 2“ bezeichnete Inhalte gilt derselbe Quellenanspruch: nur konkret belegte Figuren und Gegenstände aufnehmen; Ankündigungen, Entwürfe und tatsächlich verfügbare Referenzen auseinanderhalten. Keine fehlenden Originalinhalte erfinden.
5. **Sift bleibt ausdrücklich ausgeschlossen**, wie vom Nutzer gewünscht. Die Begründung aus dem Gespräch ist keine in diesem Dokument erneut geprüfte Veröffentlichungsinformation.
6. Die Redstone-Monstrosität muss sich an der vom Nutzer gezeigten Goety-Referenz orientieren. Ein vereinfachtes, anders proportioniertes Ersatzmodell erfüllt diesen Wunsch nicht.
7. Actions & Stuff dient als gewünschte Referenz für Ausdruck, Bewegungsqualität und Atmosphäre. Dazu kommen nachvollziehbare Arbeitsanimationen für die Villager-Berufe.
8. Neues UI, bessere Inventar- und Bauplanung, neue Mechaniken in der Art von Redstone, Koop und langfristige eigene Bauziele gehören ausdrücklich zum Entwurf.
9. Codex soll passende verfügbare Skills und Werkzeuge prüfen und sinnvoll einsetzen. Konkrete Anforderungen stehen in Kapitel 22; Namen vorgeschlagener Projektskills sind keine Behauptung, dass solche Pakete bereits installiert oder öffentlich erhältlich sind.
10. Fortschritt muss überprüfbar sein: echte Laufzeitfunktion, echte Bewegungen und bei grafischen Änderungen reale Client-Aufnahmen. Ein Konzeptbild oder eine reine Registry-Zeile ist kein Nachweis einer fertigen Spielfunktion.
11. Die Welt besitzt beobachtbare eigene Abläufe: Tiere verfolgen Ziele, halten Gruppen zusammen, reagieren aufeinander und kehren nach einer Störung zu geeigneten Tätigkeiten zurück. Der Spieler kann daran Freude haben, ohne eine Quest anzunehmen oder eine Belohnung auszulösen.

### So ist das Dokument zu lesen

- **G**: Idee war bereits im Gespräch enthalten; sie wird hier verbindlich festgehalten und gegebenenfalls genauer beschrieben.
- **R**: neue Ergänzung oder Konkretisierung aus der Recherche. Die beschriebene Umsetzung ist unser eigener Entwurf; die Quelle liefert das ausdrücklich benannte Inspirationsprinzip.
- **S-…**: Primärquelle eines Mods, Werkzeugs, Ressourcenpakets oder Modpacks. Die Quellenmatrix erklärt die jeweils belegte Eigenschaft.
- **C-…**: einzelner Community-Beitrag oder Issue-Dialog. Das ist ein konkreter Nutzungshinweis, keine repräsentative Abstimmung der gesamten Community.
- **P0**: Grundlage des ersten zusammenhängenden Spielabschnitts. **P1**: dessen wichtigste Vertiefung. **P2**: spätere Erweiterung. **P3**: Experiment mit hohem Integrationsbedarf.

Prioritäten sind Designentscheidungen für dieses Projekt, keine gemessenen Aufwandsschätzungen. Alle genannten Mods sind zunächst Inspirationsquellen. Eine spätere Integration benötigt eine gesonderte Prüfung der dann tatsächlichen Minecraft-Version, des Loaders, der APIs und der verwendbaren Code-/Assetbedingungen. In dieser Recherche wurden Beschreibungen und Dokumentation ausgewertet; die Mods wurden nicht gemeinsam installiert oder im Client gegeneinander getestet.

## 1. Die Spielvision

**Du erschaffst Orte, an denen tatsächlich etwas passiert.** Ein selbst gebautes Gasthaus bekommt Gäste. Eine Brücke eröffnet eine Route. Eine Mühle bewegt sich und versorgt eine Werkstatt. Ein Dungeon liefert ein Relikt, mit dem zu Hause eine neue Konstruktion möglich wird. Bewohner und Weltbuch erinnern sich an ausgewählte Veränderungen.

Der zentrale Ablauf lautet: **entdecken → verstehen → etwas bauen oder lösen → eine sichtbare Veränderung erleben → neue Möglichkeiten erhalten**. Dieser Ablauf funktioniert für Kämpfer, Baumeister, Techniker, Entdecker und gemütliche Spieler.

### Atmosphäre als eigenständiges Spielziel

Der Nutzer möchte den Eindruck, einen bereits bewohnten Ort zu betreten: Über dem Tal zieht ein Schwarm; am Ufer schwimmt eine Familie; eine Gruppe ruht, sucht Nahrung oder reagiert auf eine Störung. Bewohner und Kreaturen haben nachvollziehbare Beziehungen zu Orten und zueinander. Diese Tätigkeit läuft in beobachteten Bereichen auch weiter, wenn der Spieler stillsteht und keinen Auftrag verfolgt.

Crimson Desert ist hier die ausdrücklich genannte Referenz für dieses Weltgefühl. Pearl Abyss beschreibt in „Das Leben in Pywel“ Alltag abseits von Quests und Kämpfen. ARK: Survival Evolved ist die zweite Nutzerreferenz; dessen offizielle Store-Beschreibung nennt unter anderem Kreaturen, Räuber-/Beutebeziehungen und Nachwuchs. Daraus folgt keine Behauptung, dass diese Spiele genau unsere geplanten Entenfamilien oder Schwarmabläufe besitzen. Die folgenden Szenen sind eigene Designvorgaben für Minecraft. [S-GAME-CRIMSON] [S-GAME-ARK]

**Neue Reihenfolge für den ersten sichtbaren Eindruck:** zuerst ein kleines überzeugendes Naturgebiet mit Vogelschwarm, Entenfamilie, Wasser-/Uferwechsel und passenden Geräuschen; daran anschließend der bewohnte Ort mit Brücke, Gasthaus und Maschine. Interaktionen zwischen Figuren, Umgebung und Gruppe sind ein eigenes Qualitätsziel. Ruhige Pausen gehören dazu.

### Designregeln

1. Wenige klare Regeln sollen viele selbst erfundene Anwendungen ermöglichen.
2. Bauen ist eine erlaubte Lösung für Abenteuer; Maschinen, Seile und Geländegestaltung dürfen helfen.
3. Neue Systeme verbinden sich über erkennbare Schnittstellen. Redstone steuert, ein Antrieb bewegt, ein Behälter speichert, ein Relikt verändert eine bestimmte Eigenschaft.
4. Grundversorgung bleibt einfach. Zusätzliche Tiefe eröffnet Möglichkeiten, ohne für jeden Alltagsschritt neue Pflichtarbeit einzuführen.
5. Der Spieler entscheidet über Veränderungen seiner Gebäude, Vorräte und ausgewiesenen Flächen.
6. Bewohner zeigen den Nutzen einer Siedlung durch Verhalten. Architektur bekommt keine pauschale Schönheitspunktzahl.
7. Neue Kreaturen erhalten erkennbare Silhouetten, Bewegungen und Begegnungsideen. Ambient-Tiere dürfen auch einfach Atmosphäre schaffen.
8. Information erscheint dort, wo sie gebraucht wird: im Rezept, am Mechanismus, am Ortseintrag oder im Gespräch.
9. Offlinezeit verursacht im Standardprofil keine stillen Rückeroberungen, zerstörten Basen oder verpflichtenden Nachholarbeiten.
10. Leistung und Speicherbarkeit gehören zur Funktion. Die Simulation wird lokal und bedarfsgerecht detailliert.
11. Erkundung bleibt frei. Hinweise erleichtern die Suche, ohne unabhängige Entdeckungen zu blockieren.
12. Ein zusammenhängender, sauber abgestimmter Ablauf hat Vorrang vor vielen voneinander getrennten halbfertigen Funktionen. Das Kuratierungsprinzip von Bliss und Crucial 2 ist hierfür eine passende Referenz. [S-BLISS] [S-CRUCIAL]

## 2. Was die Mod- und Community-Recherche zusätzlich beiträgt

Diese Punkte erweitern den bisherigen Entwurf besonders deutlich:

1. **Versuchsszenen im Weltbuch:** Eine kleine animierte Maschine erklärt Eingang, Übertragung und Wirkung. Signal, Kraft und Materialfluss können einzeln hervorgehoben werden. Inspiration: Create/Ponder. [S-CREATE] [C-PONDER]
2. **Mehrere Bauziele gemeinsam planen:** Winde, Zahnräder und Rohrleitung werden als Gesamtvorhaben berechnet; gemeinsame Zwischenprodukte erscheinen zusammen, einzelne Teilprojekte bleiben zuweisbar. Inspiration: EMI und der konkrete Multitree-Wunsch. [S-EMI] [C-EMI-PLAN]
3. **Sichtbare lokale Lieferungen:** Material wird reserviert, verarbeitet, verpackt und von einem Kurier oder Fahrzeug zugestellt. Der Status erklärt Wartezeiten. Inspiration: MineColonies. [S-MINECOLONIES]
4. **Steuerkarten:** Eine lesbare Regel verknüpft bekannte Werte: „Tank niedrig UND Ziel frei → Pumpe an“. Jede Bedingung zeigt ihren aktuellen Wert. Inspiration: Integrated Dynamics und PneumaticCraft. [S-INTEGRATED] [S-PNEUMATIC]
5. **Funktionsgärten:** Wenige besondere Pflanzen dienen als räumliche Sensoren oder Helfer. Licht, Feuchtigkeit und Resonanz sind bekannte Eingänge. Inspiration: Botania. [S-BOTANIA]
6. **Fundstück, Forschung und Suchgebiet:** Ein Artefaktfragment oder Fundbericht führt zu einer besonderen Region und deren Mechanik. Inspiration: Alex’s Caves. [S-ALEX-CAVES]
7. **Ein Material kann durch Umgang entstehen:** Beobachten, Pflegen oder eine passende Reaktion kann einen seltenen Werkstoff liefern. Inspiration: Alex’s Mobs und Ecologics. [S-ALEX-MOBS] [S-ECOLOGICS]
8. **Patina als Gestaltungswerkzeug:** Altern, säubern und konservieren werden bewusste Entscheidungen. Inspiration: Immersive Weathering. [S-WEATHERING]
9. **Eine Kamera als Spielgegenstand:** Fotografien werden Teil von Naturkunde, Chronik, Ausstellungen und gemeinsamen Erinnerungen. Inspiration: Exposure. [S-EXPOSURE]
10. **Erreichbarkeit sichtbar machen:** Ein Bewohner zeigt den ersten konkreten Engpass zwischen Arbeitsplatz und Ziel. Der freie Grundriss bleibt erhalten.
11. **Questziele präzise unterscheiden:** Besitz, Herstellung, Lieferung, Beobachtung und Weltzustand haben unterschiedliche Anerkennungsregeln. Eine vorhandene Brücke kann ein Ziel bereits erfüllen. [S-FTB] [C-QUEST]
12. **Animation aus zwei Perspektiven abnehmen:** Ego-Ansicht, Außenansicht, Blickrichtung, FOV, Rüstung und gehaltene Gegenstände werden gemeinsam geprüft. [S-NEA] [S-BETTER-COMBAT] [C-FOV]

## 3. Technik und neue kombinierbare Mechaniken

### Rollen der Systeme

| System | Eigene Aufgabe | Verbindung zu anderen Systemen |
| --- | --- | --- |
| Redstone | Bedingungen, Signale und Zeitabläufe | Schaltet Kupplungen, Ventile, Blenden und Empfänger. |
| Kraftübertragung | Drehung, Geschwindigkeit, Kraft und Belastung | Treibt Winden, Pumpen und bewegliche Baugruppen an. |
| Wasser und Druck | Flüssigkeit speichern, verteilen und bewegen | Tanks liefern Zustandswerte; Pumpen benötigen Antrieb. |
| Resonanz | Töne und kurze Muster erkennen und weitergeben | Aktiviert Empfänger, besondere Materialien und Relikte. |
| Gerichtetes Licht | Strahlen lenken, filtern und aufteilen | Bedient Sensoren, Pflanzen, Spiegelanlagen und besondere Materialien. |
| Materialzustände | Eine ausgewählte Eigenschaft gezielt verändern | Reagieren auf Wärme, Nässe, Licht oder Resonanz. |

Die bestehenden Redstone-Regeln bleiben maßgeblich. Der neue Antriebsbedarf gilt für die dafür entworfenen größeren Mechanismen; vorhandene einfache Funktionen werden nicht nachträglich unnötig verteuert.

### Ideenregister Technik

| ID | Idee und konkreter Spielablauf | Priorität / Herkunft |
| --- | --- | --- |
| TECH-01 | **Einfacher mechanischer Einstieg:** Handkurbel, Welle und Winde heben eine kleine Plattform. Die Drehrichtung ist sichtbar; Loslassen stoppt den Antrieb. | P0 · G |
| TECH-02 | **Wasser- und Windantriebe:** Ein passend platzierter Antrieb versorgt verbundene Verbraucher. Standort und Bauweise geben Anlass für Mühlen, Türme und Kanäle. | P1 · G |
| TECH-03 | **Zahnräder, Riemen und Kupplungen:** Übersetzung tauscht Geschwindigkeit gegen Kraft; Kupplungen trennen Verbraucher. Last ist nachvollziehbar. Überlast stoppt die Anlage im Standardprofil. | P1 · G |
| TECH-04 | **Winden und bewegliche Baugruppen:** Lager, Gelenk oder Plattformanker definieren den bewegten Bereich. Türen, Vorhänge, Brücken und Kabinen können angehalten und wieder bedient werden. | P1 · G |
| TECH-05 | **Tanks, Rohre und Ventile:** Endliche Kapazität, sichtbarer Füllstand, definierte Ein-/Ausgänge. Ein geschlossener Hahn stoppt den Durchfluss; ein voller Tank erklärt seinen Zustand. | P1 · G |
| TECH-06 | **Pumpen und Druckkolben:** Mechanische Arbeit bewegt Wasser und kann eine hydraulische Plattform versorgen. Füllung und nutzbarer Druck werden getrennt angezeigt. Unsere Wasserhydraulik ist eine eigene Ableitung aus Drucksystemen. [S-PNEUMATIC] | P1 · G+R |
| TECH-07 | **Schleusen und Bewässerung:** Eine Schleuse überbrückt Wasserhöhen; ein Vorratstank versorgt Rinnen, Garten und Brunnen. Ventile verbinden dekorative Wasserbauten mit Nutzung. | P2 · G |
| TECH-08 | **Resonanzbauteile:** Glocken, Klangsteine, Leiter, Dämpfer und abgestimmte Empfänger. Erst ein Ton, später kurze Reihenfolge oder Rhythmus. Jeder Ton hat ein sichtbares Symbol. | P1 · G |
| TECH-09 | **Echo und Erinnerung:** Ein begrenzter Echospeicher wiederholt ein Muster verzögert. Höhlenstationen antworten unterschiedlich; ein Instrument kann mehrere Gebäudefunktionen bedienen. | P2 · G |
| TECH-10 | **Spiegel, Filter und Prismen:** Ein gerichteter Strahl erreicht nur sichtbare, zulässige Wege. Mechanik richtet Spiegel aus; Redstone schaltet Blenden. Anwendung: Observatorium oder unterirdischer Garten. | P2 · G |
| TECH-11 | **Besondere Materialzustände:** Wärmespeicherstein, bei Nässe quellendes Material, schaltbares Phasenglas, nachleuchtender Kristall und Resonanzmetall. Pro Material wenige lesbare Zustände statt universeller Materialphysik. | P2 · G |
| TECH-12 | **Sensoren und Anschlüsse:** Wasserstand, Drehzahl, Belastung, Wetter, Ankunft und freigegebene Lagerbestände liefern definierte Signale. Benannte Ein-/Ausgänge verbinden große Anlagen. | P1 · G |
| TECH-13 | **Messwerkzeug und Signalverlauf:** Ausgewählte Verbindungen zeigen Signalstärke und Ursache; eine kurze Aufzeichnung erklärt die Abfolge. Meldungen nennen etwa fehlenden Antrieb, unterbrochene Verbindung oder blockiertes Ziel. | P0 · G |
| TECH-14 | **Grafische Steuerkarten:** Auslöser, Bedingung und Aktion werden mit beschrifteten Werten zusammengestellt. Aktuelle Wahrheitswerte sind sichtbar. Standardanlagen funktionieren weiterhin direkt mit Redstone. [S-INTEGRATED] | P2 · R |
| TECH-15 | **Optionale Steuerpulte:** Anzeigen, benannte Schnittstellen und später begrenzte Skripte für komplexe Bahnhöfe oder Werkstätten. Programme haben Besitzer, Rechenbudget und Stopptaste. [S-CC] | P3 · R |
| TECH-16 | **Greifbare Zeitgeber:** Sanduhr, Wasseruhr und Pendel erzeugen beobachtbare Abläufe für Beleuchtung, Rätsel und Theater. Die gleichen Bauteile sind Einrichtung und Technik. [S-SUPPLEMENTARIES] | P2 · G+R |
| TECH-17 | **Versuchsszenen:** Eine sichere Miniatur erklärt einen Mechanismus im Weltbuch. Schritte lassen sich wiederholen; Eingänge und Auswirkungen werden markiert. Keine Veränderung der realen Welt. [S-CREATE] | P0 · R |
| TECH-18 | **Reliktmodule:** Ein Auslöser, ein Relikt und ein Empfänger bilden eine begrenzte Anwendung. Derselbe bekannte Effekt kann tragbar und stationär genutzt werden. [S-ARS] | P2 · G+R |
| TECH-19 | **Funktionsgärten:** Einzelne besondere Pflanzen sammeln, reichen weiter oder reagieren als Sensor. Wirkung, Reichweite und geeigneter Standort sind sichtbar. [S-BOTANIA] | P2 · R |
| TECH-20 | **Maschinen für Ausdruck:** Theater, Karussell, mechanische Figur, Hafenkran, große Uhr und wandelbarer Garten sind ausdrücklich unterstützte Spielziele. | P2 · G+R |

### Regeln, die vor der Umsetzung feststehen müssen

- Kraftnetz: ein verständliches Modell für Drehung, Übersetzung und Last; keine Simulation jedes Zahnkontakts. Geschlossene Kreise dürfen keine zusätzliche Kraft erzeugen.
- Flüssigkeit: Behälterkapazität und tatsächlich bewegte Menge werden erhalten. Pumpen benötigen ihren definierten Antrieb; ein Rücklauf erzeugt kein endloses zusätzliches Arbeitspotenzial.
- Resonanz: begrenzte Reichweite, Musterlänge und Wiederholrate; ein Empfänger reagiert deterministisch. Töne erhalten sichtbare Alternativen.
- Licht: begrenzte Strahllänge, Reflexionszahl und Intensität; ein Prisma vervielfacht keine Energie. Endlose Spiegelkreise enden kontrolliert.
- Materialzustände: nur ausdrücklich dafür eingeführte Materialien. Normale Gebäude erhalten dadurch keine zufälligen neuen Zerfallsregeln.
- Weltgrenzen: Anlagenzustände speichern; ausgeladene Bereiche nicht durch unbegrenztes Chunkladen künstlich aktiv halten. Beim Wiederladen konsistent fortsetzen.

### Drei Beispielkonstruktionen

**Bergstation:** Flussrad → Pumpe → hydraulischer Lastenaufzug. Redstone sichert die Türen, eine Glocke ruft die Plattform, Spiegel beleuchten einen Garten im Berg.

**Mechanisches Theater:** Eine Welle betreibt Vorhang, Bühnenheber und drehbare Kulisse. Übersetzungen bestimmen Geschwindigkeiten. Ein kurzer Signalablauf oder eine Melodie startet die Vorstellung.

**Sonnenobservatorium:** Bewegliche Spiegel lenken Tageslicht durch Filter. Ein Empfänger öffnet einen Raum; ein nachleuchtender Weg zeigt abends den Rückweg. Eine künstliche Lichtquelle ermöglicht die Bedienung unabhängig von Wartezeiten.

## 4. Bauen, Räume und Gestaltung

| ID | Idee und konkreter Spielablauf | Priorität / Herkunft |
| --- | --- | --- |
| BUILD-01 | **Funktionsanker für freie Räume:** Spieler markieren Gasthaus, Werkstatt, Bibliothek, Markt, Garten oder Hafen. Wenige überprüfbare Bedingungen wie Zugang, Arbeitsplatz und zugewiesener Vorrat genügen. | P0 · G |
| BUILD-02 | **Freie Architektur:** Baumhaus, Höhle, Schiff und Burg können dieselbe Funktion erfüllen. Das System erklärt fehlende Nutzbarkeit und bewertet keine vorgegebene Optik. | P0 · G |
| BUILD-03 | **Baubuch und Grundrisse:** Maße, Abstände, Linien und Ebenen vorbereiten; Projekt benennen und Teilbereiche markieren. | P1 · G |
| BUILD-04 | **Materialpaletten:** Favoriten, passende Varianten und Wechselvorschau. Ein Dach kann vor dem Umbau mit anderer Holzart betrachtet werden. [S-AXIOM] | P1 · G+R |
| BUILD-05 | **Eigene Motive und Vorlagen:** Dächer, Treppen, Fassadenteile und eigene Gebäudeteile speichern, drehen, spiegeln und wiederholen. | P1 · G |
| BUILD-06 | **Survival-Vorschau:** Bauplan zeigt Position und Materialbedarf. Material wird bei der tatsächlichen Platzierung verbraucht. Kreativwerkzeuge können zulässige größere Operationen direkt ausführen. | P1 · G |
| BUILD-07 | **Teilbare Bauprojekte:** Fundament, Dach und Brücke haben eigene Materiallisten und freiwillige Zuständigkeiten; alle bleiben am gemeinsamen Plan ausgerichtet. [S-LITEMATICA] [C-PLAN] | P1 · G+R |
| BUILD-08 | **Baufortschritt und Fehleransicht:** Benötigt, vorhanden, reserviert und verbaut sind getrennt. Falsche oder zusätzliche Blöcke werden auf Wunsch markiert. [S-LITEMATICA] | P1 · R |
| BUILD-09 | **Symmetrie und Varianten:** Im Plan spiegeln, stapeln, strecken und Varianten vergleichen. Das Werkzeug erklärt die betroffene Fläche, bevor eine echte Änderung ausgeführt wird. [S-AXIOM] | P2 · R |
| BUILD-10 | **Bewegliche Bauwerke:** Große Tore, Drehbrücken, Kabinen, Dachsegmente, Statuen und ausfahrbare Anlegestellen nutzen begrenzte bewegliche Gruppen. | P1 · G |
| BUILD-11 | **Nutzbare Einrichtung:** Sitzen, liegen, am Tisch arbeiten, Gegenstände abstellen. Sitzplätze, Schlafplätze und Arbeitsflächen sind für Spieler und NPCs verständlich erreichbar. | P0 · G |
| BUILD-12 | **Physische Projekttafeln:** Baupläne, Materialbedarf und Aufgaben erscheinen auf einer Tafel in der Welt. Dieselbe Grundlage kann Abfahrtstafel, Marktanzeige und Theaterprogramm bedienen. [S-SUPPLEMENTARIES] | P1 · R |
| BUILD-13 | **Werkstatt als räumliche Aufgabe:** Einlauf, Verarbeitung, Lager und Auslauf liegen nachvollziehbar im Gebäude. Die Form folgt der gewünschten Tätigkeit. [S-ENGINEERING] | P2 · R |
| BUILD-14 | **Baustellenhelfer nach Freigabe:** NPCs tragen zugewiesenes Material; spätere Bauhelfer arbeiten ausschließlich an bestätigtem Plan und bestätigter Fläche. Sie pausieren bei Hindernissen. | P3 · G+R |
| BUILD-15 | **Patina, Reinigung und Konservierung:** Spieler wählen Alterung für Ruinen, Häfen oder Fassaden. Der gewählte Zustand kann fixiert werden; im Standardmodus entstehen keine stillen funktionalen Schäden. [S-WEATHERING] | P2 · R |
| BUILD-16 | **Spielerdefinierte Wahrzeichen:** Ein Ort bekommt einen Namen, ein Wappen und einen Karteneintrag. Er kann Ziel für Besucher und Bezugspunkt für Geschichten werden. | P1 · G |
| BUILD-17 | **Museen, Bibliotheken und Kartenzimmer:** Funde, echte Fotografien, regionale Karten und ausgewählte Erfolge werden in selbst gebauten Räumen ausgestellt. | P2 · G |

**Abnahme für Funktionsräume:** Ein frei gebautes, nicht vorgefertigtes Haus wird erkannt; die relevante Figur erreicht Arbeitsplatz und Sitzplatz; blockierte Wege werden konkret erklärt; das Entfernen eines Arbeitsplatzes beschädigt keine anderen Räume oder gespeicherten Gegenstände.

## 5. Neues UI, Inventar, Handwerk und Weltbuch

### Gestalterische Richtung

Die Oberfläche verwendet klare Minecraft-nahe Formen, abgestufte Kanten, zurückhaltende Materialanmutung aus Stein, Holz und Papier sowie gut lesbaren Fließtext. Pixelartige Überschriften sind möglich; wesentliche Informationen bleiben auch bei kleiner Darstellung lesbar. Farbe wird durch Symbol und Text ergänzt. Kurze Animationen erklären Zustandswechsel, die Bedienpositionen bleiben stabil.

Die bisherige anklickbare Konzeptskizze zeigte vier Ansichten: **Unterwegs**, **Werkbank**, **Weltbuch**, **Technik**. Ihr Beispiel verband Herstellung, Brückenbau, Besucher und die getrennte Schaltung von Redstone-Signal und mechanischem Antrieb. Diese Beschreibung ist die portable Vorgabe; eine externe HTML-Datei ist zum Verständnis dieser Ideenliste nicht erforderlich. Die Skizze war ein UI-Konzept, kein Beleg für bereits im Mod vorhandene Mechaniken.

| ID | Idee und konkreter Spielablauf | Priorität / Herkunft |
| --- | --- | --- |
| UI-01 | **Ruhiges HUD:** stabile Hotbar mit neun Plätzen, klare Gesundheitsinformation, kurzzeitige Orts-/Wetteranzeige und höchstens ein bewusst angeheftetes Vorhaben. | P0 · G |
| UI-02 | **Kontextaktionen am angesehenen Objekt:** kurze passende Aktion; ausführliche Informationen erst beim Öffnen. Bewohnernamen und Questzeichen überfüllen die Welt nicht. | P0 · G |
| UI-03 | **Inventarsuche und Filter:** Gegenstände nach Namen, Zweck und Kategorie finden. Eine Suche nach einer Funktion darf passende bekannte Gegenstände erklären. [S-EMI] | P0 · G+R |
| UI-04 | **Favoriten und geschützte Plätze:** Werkzeug- und Baustapel können gegen Sortieren oder unbeabsichtigtes Weglegen gesperrt werden. Automatisches Nachfüllen ist optional und zeigt die Quelle. [S-IPN] | P1 · G+R |
| UI-05 | **Schnelle Transfers:** verständliche Klick-/Tastenaktionen für Stapel und passende Gegenstände. Jeder Drag-Vorgang erhält eine Alternative für Controller und Tastatur. [S-MOUSE-TWEAKS] | P1 · R |
| UI-06 | **Rezepte, Verwendungen und Zwischenprodukte:** Vom Gegenstand zu Rezepten und Einsatzmöglichkeiten wechseln. Fehlende Teilkomponenten führen zu deren Herstellung, ohne den Ausgangsplan zu verlieren. [S-EMI] | P0 · G+R |
| UI-07 | **Gemeinsamer Plan für mehrere Zielgegenstände:** Bedarf von Winde, Brücke und Rohren bündeln; gleiche Zwischenprodukte zusammenrechnen; Teilziele weiterhin sichtbar halten. Ein direkt beschrifteter Knopf macht die Funktion auffindbar. [C-EMI-PLAN] | P1 · R |
| UI-08 | **Herstellungsmenge bewusst wählen:** „Einmal herstellen“, „Zielmenge herstellen“ und „Rezept anheften“ sind unterscheidbare Aktionen. Bei Materialmangel entstehen keine halben oder doppelten Buchungen. | P0 · G |
| UI-09 | **Verbundene Vorräte:** Nur ausdrücklich verknüpfte und berechtigte Lager liefern Material. Quelle, Entfernungskonzept und Reservierung sind sichtbar. Fremde private Kisten bleiben getrennt. | P1 · G |
| UI-10 | **Praktischer Ausrüstungsvergleich:** Reichweite, Rhythmus, spezielle Wirkung und Nutzungssituation erklären. Unterschiede werden nicht auf eine einzige irreführende Gesamtpunktzahl reduziert. | P1 · G |
| UI-11 | **Weltbuch als Atlas:** erkundete Karte, Orte, Bewohner, eigene Benennungen und Notizen. Karten entwickeln sich mit Entdeckungen und ausgewählten Bauwerken. [S-ATLAS] | P0 · G+R |
| UI-12 | **Fakt, Gerücht und Plan unterscheiden:** bestätigte Orte, ungenaue Suchgebiete und selbst gesetzte Ziele erhalten verschiedene Kennzeichen. Ein Gerücht verrät nicht automatisch exakte Koordinaten. | P0 · G |
| UI-13 | **Ortseinträge:** bekannte Personen, heutige Tätigkeit, offene Projekte und funktionale Verbindungen. Ein Eintrag erklärt etwa, wo die Handelsroute am Fluss endet. | P1 · G |
| UI-14 | **Bestiarium und Naturtagebuch:** Beobachtung, Verhalten, passende Umgebung, bekannte Gefahren und eigene Fotografien sammeln. Optionaler Spoilerschutz zeigt Unbekanntes nur grob. [S-ALEX-MOBS] | P1 · G+R |
| UI-15 | **Kurze Gespräche, klare Dienste:** kleine Dialoge bleiben in der Welt verankert; Handel, Bestellung und größere Projekte haben eine eindeutige Ansicht. Eine Bitte kann zum freiwilligen Plan werden. | P0 · G |
| UI-16 | **Begleiterbedienung:** Folgen, warten, nach Hause und interagieren; Animation bestätigt den Auftrag. Der aktuelle Zustand bleibt auffindbar. | P1 · G |
| UI-17 | **Bau- und Technikansicht:** ausgewählte Bereiche, Materiallisten, Fehler und Signalwege temporär einblenden. Untersuchung einer Schaltung friert keinen gemeinsamen Server ein. | P1 · G |
| UI-18 | **Skalierung und Eingabegeräte:** skalierbarer Text und UI, vollständige Tastatur-/Controllerwege, keine nur per Hover erreichbaren Kernfunktionen, sichere Fokusführung. | P0 · G |
| UI-19 | **Ruhige Bildschirmprofile:** Zusatzanzeigen ausblendbar; gespeicherte Ansicht pro Tätigkeitskontext. Das Inventar bleibt auch ohne stets sichtbare Rezeptseiten nutzbar. [C-EMI-HIDE] | P1 · R |
| UI-20 | **Startbildschirm mit eigener Welt:** ein echtes zuletzt gespeichertes Bild oder eine zulässige Weltansicht zeigt die eigene Basis. Weltname, letzter Spielort und offene persönliche Pläne helfen beim Wiedereinstieg. | P2 · G |
| UI-21 | **Erklärungen im Spiel:** Hilfe führt zu einer kleinen Versuchsszene oder zu „Wofür brauche ich das?“. Der Spieler braucht für die Kernsysteme keine externe Wiki-Recherche. [S-CREATE] [S-BLISS] | P0 · G+R |
| UI-22 | **Hinweise zu unerfüllten Bedingungen:** Aufgaben, Raumfunktionen und Mechanismen sagen konkret, welcher Schritt oder Zustand fehlt. Besitz, Herstellung und Lieferung tragen passende Bezeichnungen. [C-QUEST] | P0 · R |

## 6. Bewohner, Berufe und Siedlungen

| ID | Idee und konkreter Spielablauf | Priorität / Herkunft |
| --- | --- | --- |
| VILL-01 | **Tagesabläufe:** Arbeit, kurze Pausen, Treffen, Heimweg und Schlafen. Abläufe reagieren auf erreichbare Orte, Tageszeit, Wetter und Gefahr. | P0 · G |
| VILL-02 | **Arbeitsanimationen mit Kontakt:** Hände greifen Werkzeug, Werkstück liegt auf der Fläche, Material wird gebracht und fertige Ware abgelegt. Animation und tatsächlicher Auftrag passen zusammen. | P0 · G |
| VILL-03 | **Bestellungen und Abholung:** Schmied oder Zimmermann erhält einen klaren Auftrag. Zustand: angenommen, Material fehlt, in Arbeit, bereit. Beschriftete Abholung gehört dem Auftraggeber. | P1 · G |
| VILL-04 | **Individuelle Identität:** Name, Beruf, wenige stabile Vorlieben und ausgewählte Erinnerungen. Klassische Villager-Gestaltung bleibt passend zum Projekt. [S-MCA] | P1 · G+R |
| VILL-05 | **Reaktion auf Leistungen:** bekannte Bewohner erkennen eine befreite Person, gebaute Brücke oder ein Wahrzeichen wieder. Erinnerungen führen zu passenden kurzen Szenen und Dialogen. | P1 · G |
| VILL-06 | **Soziale Treffpunkte:** Brunnen, Markt, Bühne und Gasthaus ermöglichen Gespräche, gemeinsames Sitzen und Musik. Nutzbare Einrichtung schafft die Gelegenheit. | P1 · G |
| VILL-07 | **Gäste und Reisende:** Besucher übernachten, schauen sich Waren an und setzen ihren Weg fort. Eine Begegnung kann interessant sein, ohne einen Auftrag anzubieten. | P0 · G |
| VILL-08 | **Handelsrouten:** freigegebene Straßen, Brücken, Häfen und Stationen verbinden bekannte Orte. Besucher und Waren nutzen die Verbindung nachvollziehbar. | P1 · G |
| VILL-09 | **Bedarfstafel:** lokale Werkstätten zeigen konkrete offene Aufträge, zugewiesene Vorräte und Überschüsse. Bedarf entsteht aus tatsächlichen Projekten. [S-MINECOLONIES] [S-LIVE-VILLAGES] | P1 · R |
| VILL-10 | **Sichtbare Kuriere:** reservierte Waren werden zugeordnet, verpackt und zugestellt. Ein blockierter Auftrag bleibt erhalten und nennt die Ursache. [S-MINECOLONIES] | P1 · R |
| VILL-11 | **Gebäudeentwicklung nach Spielerentscheidung:** ein Ort bietet neue Projekte an. Vorschau, Material und bestätigte Baufläche bestimmen die Erweiterung. Kein stiller Umbau privater Bauten. [C-VILLAGE] | P2 · G+R |
| VILL-12 | **Erreichbarkeit prüfen:** Werkzeug zeigt nutzbaren Weg und erstes Hindernis, etwa zu engen Durchgang oder unterbrochene Treppe. Bewohner bleiben bei Ausfall in einem sicheren Wartezustand. | P0 · R |
| VILL-13 | **Wetter- und Gefahrreaktionen:** Unterstellen, Türen benutzen, sichere Sammelpunkte aufsuchen. Ausgewählte Wächter können bekannte Wege patrouillieren. | P1 · G |
| VILL-14 | **Handwerk bleibt auch beim Spieler:** Bewohnerdienste liefern Komfort, Varianten oder parallele Arbeit. Essenzielle eigene Herstellung bleibt verfügbar. | P0 · G |
| VILL-15 | **Ruf und Klatsch in überschaubarer Form:** ausgewählte bedeutsame Handlungen beeinflussen passende Reaktionen; Textwiederholungen werden begrenzt. Keine tägliche Geschenkpflicht. Individuelle Bewohner sind durch MCA angeregt; Oreville beschreibt Ruf/Klatsch beim separaten Villager News, nicht bei Actions & Stuff. [S-MCA] [S-ACTIONS] | P2 · G+R |
| VILL-16 | **Regionale Kultur:** Dekoration, Gerichte, Kleidungselemente, Marktangebote und Gesprächsmotive bilden ein zusammenhängendes Ortsprofil. | P2 · R |

### Berufsszenen als konkrete Animationsaufträge

Die im tatsächlichen Zielspiel vorhandenen Berufe zuerst erfassen. Die folgende Tabelle beschreibt Arbeitsrollen für Animation und Spielgestaltung; zusätzliche Rollen werden als eigene Erweiterungen geführt.

| Arbeitsrolle | Charakteristische Szene | Funktion im Entwurf |
| --- | --- | --- |
| Bauer | prüfen, pflanzen, gießen beziehungsweise ernten, Erntekorb abstellen | Landwirtschaft und Saatgut |
| Fischer | Leine oder Netz prüfen, Fang sortieren, Ausrüstung flicken | Gewässerwissen und Fischerei |
| Schäfer | Tier beruhigen, Fell prüfen, Wolle sortieren | Tierhaltung und Textilien |
| Pfeilmacher | Schaft richten, Befiederung setzen, Bogen prüfen | Fernkampfausrüstung |
| Bibliothekar | Buch holen, blättern, Notiz schreiben, Buch einsortieren | Wissen und bekannte Hinweise |
| Kartograf | Karte ausrollen, mit Instrument prüfen, zeichnen und zusammenrollen | Regionen und Verbindungen |
| Geistlicher / Alchemist | Reagenz abmessen, mischen, Gefäß prüfen | passende Zubereitungen und Forschung |
| Rüstungsschmied | Platte formen, anpassen und Oberfläche prüfen | Rüstungsarbeiten |
| Waffenschmied | Werkstück erhitzen, mit Zange halten, hämmern und abschrecken | Waffen und besondere Komponenten |
| Werkzeugschmied | Griff einsetzen, Werkzeug prüfen, schleifen | Werkzeuge und Reparatur |
| Fleischer / Koch | Zutaten vorbereiten, schneiden, kochen und anrichten | Versorgung und Gerichte |
| Gerber | Material spannen, bearbeiten, schneiden und aufhängen | Lederwaren |
| Steinmetz | anzeichnen, meißeln, Staub abklopfen, Werkstück prüfen | Steinbauteile |
| Zimmermann | messen, sägen, Bauteil tragen, Verbindung anpassen | Einrichtung und Baukomponenten |
| Kräuterkundiger / Gärtner | Pflanzen pflegen, ernten, trocknen und bündeln | Gärten und Pflanzenwissen |
| Mechaniker | Zahnrad prüfen, Achse justieren, Anlage vorsichtig testen | Technikdienste und Hinweise |
| Archäologe | Fund abbürsten, Fragmente vergleichen, Objekt zusammensetzen | Relikte und regionale Forschung |
| Gastwirt / Bäcker | Teig oder Gericht zubereiten, servieren, Tisch abräumen | Besucher, Verpflegung und Gerüchte |

Jede Arbeitsszene braucht gültige Werkzeug-/Werkstückpositionen, einen erreichbaren Platz, einen sauberen Abbruch bei Gefahr und eine glaubwürdige Rückkehr zur Tätigkeit. Ein Beruf wird nicht durch eine dauerhaft wiederholte zufällige Armbewegung als fertig gewertet.

## 7. Natur, Wetter und eine belebte Umwelt

| ID | Idee und konkreter Spielablauf | Priorität / Herkunft |
| --- | --- | --- |
| WORLD-01 | **Fliegendes und krabbelndes Leben:** Vögel, Insekten, Motten, Fledermäuse und passende regionale Kleintiere erzeugen kurze Begegnungen; Aktivität passt zu Ort und Tageszeit. | P0 · G |
| WORLD-02 | **Habitatsystem:** Wasser, Deckung, geeignete Pflanzen, Ruhe und Plätze werden lokal erkannt. Ein gestalteter Teich oder Garten wird für passende Tiere interessant. [S-NATURALIST] | P0 · G+R |
| WORLD-03 | **Tierische Alltagsszenen:** trinken, baden, suchen, ruhen, verstecken, balzen und sich bei Nässe schütteln. Auswahl folgt dem tatsächlichen Zustand. | P0 · G |
| WORLD-04 | **Lebensraum-Werkzeug:** ein kleiner Bereich zeigt bekannte Bedingungen und konkrete Möglichkeiten, etwa erreichbares Wasser oder fehlenden Unterschlupf. | P2 · R |
| WORLD-05 | **Umweltspuren:** Fußspuren, verlassene Lager, ungewöhnliche Sporen, gestörte Tiere und entfernte Geräusche kündigen Begegnungen an. Spuren überschreiben keine wichtigen Spielerblöcke. | P1 · G |
| WORLD-06 | **Wetter als Gelegenheit:** Regen führt Bewohner unter Dächer und Tiere zu Schutzplätzen; Sturm hinterlässt geeignete Fundstellen; Nebel verändert Sicht und Orientierung. | P1 · G |
| WORLD-07 | **Wiederkehrende Naturereignisse:** leuchtende Teichnächte, Wanderungen, Blüte oder Pollenflug. Spielzyklen sind einstellbar und wiederholbar; Beobachtungspunkte haben einen Zweck. | P2 · G |
| WORLD-08 | **Zusammenhängende Regionen:** Flusstäler, Küsten, Hochflächen, Klippen und Höhlensysteme besitzen erkennbare Silhouetten und Übergänge. Landschaft lädt zu Brücken, Tunneln und Stationen ein. [S-TERRALITH] | P1 · G+R |
| WORLD-09 | **Regionale Pakete:** Gelände, Materialpalette, Klang, Wesen und eine besondere interaktive Regel bilden ein gemeinsames Thema. Beispiel: Kristalltal mit Resonanz. [S-ALEX-CAVES] | P2 · G+R |
| WORLD-10 | **Kleine ökologische Verbindungen:** Nistplatz, Pflanze und Tier reagieren sinnvoll aufeinander. Pflanzende oder sammelnde Helfertiere arbeiten nur in erlaubten Bereichen. [S-ECOLOGICS] | P2 · R |
| WORLD-11 | **Friedliche Materialgewinnung:** besondere Materialien können beim natürlichen Verhalten entstehen oder durch passenden Umgang erhältlich sein. Beobachtung eröffnet einen alternativen Weg. [S-ALEX-MOBS] | P2 · R |
| WORLD-12 | **Ruhige Flächen:** Dichte und Häufigkeit besonderer Strukturen sind steuerbar. Zwischen Abenteuern bleibt Landschaft für eigene Vorhaben und entspannte Wege. | P0 · G+R |
| WORLD-13 | **Orte ändern ihren Zustand:** bedroht, befreit, verbunden und bewohnt sind unterschiedliche sichtbare Zustände. Entwicklung passt zur dortigen Geschichte. | P1 · G |
| WORLD-14 | **Lokale Naturressourcen:** warme Quelle, Strömung, kalter Luftzug oder resonanter Kristall geben einem Standort einen eigenen Bauanlass. | P2 · G |
| WORLD-15 | **Bestandswelt respektieren:** neue Regionen und Strukturen in geeigneten neuen Bereichen oder kontrollierten Testwelten entwickeln. Bereits Gebautes bleibt durch Migrationen nachvollziehbar erhalten. | P0 · G |
| WORLD-16 | **Umweltreaktionen auf Bewegung:** Staub, Wasser, Laub und Pflanzen reagieren passend auf Größe und Bewegung einer Kreatur. [S-PARTICULAR] | P1 · R |

Für all diese Systeme gilt: Sichtbare Umgebungsszenen können detailliert sein, entfernte Lebensräume werden vereinfacht. Eine globale Nahrungskette, permanente Jagdsimulation oder verpflichtende tägliche Tierpflege ist keine Voraussetzung der lebendigen Welt.

### 7.1 Selbständiges Tierleben: Schwärme, Familien und beobachtbarer Alltag

**Ergänzter Schwerpunkt aus dem Gespräch:** Der Nutzer nennt Crimson Desert und ARK: Survival Evolved als Atmosphärenreferenzen, besonders Vogelschwärme und gemeinsam schwimmende Entenfamilien. Das Qualitätsziel: zusammenhängende Alltagsszenen, auch wenn der Spieler keine Aufgabe verfolgt und nichts auslöst. Die folgenden Abläufe sind eigene Vorschläge; sie behaupten keine bestimmten Funktionen der Referenzspiele.

Tiere sollen als zusammengehörige Lebewesen erkennbar sein: passende Abstände, Reaktionen aufeinander und länger beobachtbare Tätigkeiten. Eine Gruppe kann ankommen, landen, bleiben und später weiterziehen. **G** kennzeichnet hier den aktuellen Nutzerwunsch und seine eigene gestalterische Ausarbeitung.

| ID | Idee und konkreter Spielablauf | Priorität / Herkunft |
| --- | --- | --- |
| WORLD-17 | **Vogelschwärme mit vollständigem Ablauf:** Eine Gruppe kommt in lockerer Formation, kreist, verteilt sich auf Äste oder geeignete Dächer und ruht. Einzelne Tiere wechseln den Sitzplatz. Beim Aufbruch folgen versetzte Starts, Neuordnung und gemeinsamer Weiterflug. Form, Flughöhe und Abstände variieren innerhalb verständlicher Grenzen. | P0 · G |
| WORLD-18 | **Entenfamilien auf Wasser und am Ufer:** Erwachsene und Jungtiere bleiben als kleine Gruppe zusammen. Die Jungtiere schwimmen mit leicht versetzter Bewegung hinterher; vorausgehende Tiere warten bei größerem Abstand. Die Familie wechselt über erreichbare flache Stellen zwischen Schwimmen, Fressen, Gefiederpflege und gemeinsamer Ruhe am Ufer. | P0 · G |
| WORLD-19 | **Gruppenwege und kleine Herden:** Tiere nutzen mehrere bekannte Wasser-, Futter- und Ruheplätze eines lokalen Lebensraums. Der Ortswechsel entsteht aus ihrem Tagesablauf. Ein versperrter Weg führt zu einer erreichbaren Alternative; Gruppenmitglieder umgehen Hindernisse mit unterschiedlichen kleinen Bewegungen und sammeln sich anschließend wieder. | P1 · G |
| WORLD-20 | **Unterbrechen und sinnvoll fortsetzen:** Eine Gefahr oder ein lautes Ereignis kann die Gruppe aufmerksam machen oder zur Flucht bewegen. Nach dem Rückzug finden die Tiere wieder zusammen. Sobald es ruhig ist, setzen sie eine passende Tätigkeit fort; eine einzelne Störung hinterlässt keine dauerhaft zerstreuten oder blockierten Familienmitglieder. | P0 · G |
| WORLD-21 | **Beobachtbarer Alltag ohne Auftrag:** Gruppen beginnen Tätigkeiten auch ohne Annäherung oder Interaktion des Spielers. Wer an einem Aussichtspunkt wartet, erlebt wiederkehrende Abläufe mit kleinen Variationen. Fressen, Ausruhen, gemeinsames Aufbrechen und Wiederkommen haben einen eigenen Wert; dafür sind weder Questannahme noch Belohnung oder Sammelpflicht erforderlich. | P0 · G |
| WORLD-22 | **Tageszeit, Wetter und Wasserzustand:** Ruheplätze, Aktivität und Aufenthaltsort reagieren auf passende örtliche Bedingungen. Bei starkem Wetter sucht eine Gruppe Schutz, bei ruhigem Wasser verteilt sie sich lockerer. Eine trockengelegte Stelle wird neu bewertet. Übergänge berücksichtigen die ganze Gruppe, damit Jungtiere nicht auf einer unbrauchbaren Route zurückbleiben. | P1 · G |
| WORLD-23 | **Eigene Bauten werden Lebensraum:** Ein gestalteter Teich mit zugänglichem Ufer, ein ruhiger Garten, ein Nistplatz oder ein geschützter Dachvorsprung bietet passende Aufenthaltsorte. Die Nutzung wird sichtbar durch tatsächliche Ankunft und Verhalten. Tiere brauchen keine vorgeschriebene Gebäudeform und verändern Spielerbauten nur innerhalb ausdrücklich erlaubter Regeln. | P1 · G |
| WORLD-24 | **Klang und Bewegung erzählen den Zustand:** Versetztes Flügelschlagen, kurze Kontaktlaute, kleine Wellen und gemeinsames Aufhorchen machen Gruppenzusammenhalt erkennbar. Ein entfernter Schwarm klingt anders als Vögel auf dem eigenen Dach. Effekte bleiben maßvoll; wichtige Hinweise sind zusätzlich sichtbar und über die Komforteinstellungen regelbar. | P1 · G |
| WORLD-25 | **Stimmiger Wechsel zwischen Nähe und Ferne:** Nahe Gruppen besitzen nachvollziehbare Mitglieder und detailliertes Verhalten; in der Ferne darf eine vereinfachte Darstellung genügen. Position, Richtung, Gruppengröße und geplanter Aufenthaltsort bleiben beim Übergang konsistent. Annäherung erzeugt keine zweite Gruppe und lässt eine gerade beobachtete Familie nicht sichtbar verschwinden. | P0 · G |
| WORLD-26 | **Wiedererkennbare Gruppen:** Kleine Varianten bei Gefieder, Größe oder einer auffälligen Eigenschaft helfen, dieselbe Familie wiederzuerkennen. Gruppenidentität und Mitgliederzuordnung bleiben gespeichert. Wer später an den Teich zurückkehrt, kann die bekannte Gruppe erneut entdecken; Zuwachs oder Abgänge müssen aus einem definierten Weltvorgang entstehen. | P1 · G |

#### Beispielszene A: Abend am selbst gebauten Teich

Der Spieler sitzt am Steg und wartet. Eine erwachsene Ente führt drei Jungtiere über das Wasser. Ein Jungtier bleibt kurz an einer Wasserpflanze, die Gruppe verlangsamt sich und sammelt sich wieder. Gemeinsam erreichen sie einen flachen Uferabschnitt, putzen ihr Gefieder und ruhen unter einem Strauch. Der Spieler öffnet kein Menü. Später lässt ein Geräusch die Tiere aufblicken; nach kurzer Aufmerksamkeit setzen sie ihre Ruhe fort.

#### Beispielszene B: Vogelschwarm über der Mühle

Während das Wasserrad läuft, nähert sich ein Schwarm aus Richtung der Felder. Die Tiere kreisen einmal, dann landen mehrere auf geeigneten Dachkanten und einem Baum. Einige rufen, ein Vogel wechselt den Ast. Nach einer Weile starten die ersten Tiere, die übrigen folgen und bilden über dem Tal eine neue lockere Gruppe. Wer hinterherblickt, sieht einen zusammenhängenden Weiterflug. Dasselbe kann geschehen, während der Spieler im Garten arbeitet.

#### Konkrete Abnahme

- Dieselbe Familie bleibt anhand ihrer Mitglieder und Merkmale vor und nach Speichern/Laden erkennbar; keine doppelten Mitglieder beim Wechsel der Darstellung oder beim Laden.
- Wasser-Ufer-Wechsel und Hindernisse mit Jungtieren prüfen: kein durch fehlerhafte Wegfindung verursachtes Ertrinken, dauerhaftes Feststecken oder sichtbares Teleportieren.
- Eine kurz unterbrochene Gruppe sammelt sich und findet zu einem gültigen Ablauf zurück; blockierte Ziele werden ersetzt.
- Eine ruhige Testaufnahme zeigt mindestens einen vollständigen Tätigkeitswechsel, der ohne Annäherung, Interaktion oder Questannahme des Spielers beginnt.
- Warten und Beobachten sind ausreichend. Die Szene braucht weder eine Belohnungsanzeige noch ein ausgelöstes Ereignis am Bildschirm.
- Eine vereinfachte ferne Gruppe geht ohne sichtbaren Identitäts- oder Positionsbruch in die nahe Darstellung über. Leistung wird am tatsächlichen Testsystem gemessen.

## 8. Quests, Gerüchte und Geschichten

Es gibt drei gleichwertige Einstiege: eine Entdeckung in der Welt, die Bitte einer Figur oder ein eigenes Vorhaben. Geschichten bestehen aus sorgfältig gestalteten Situationen mit nachvollziehbaren Bedingungen. Orte und Beteiligte können variieren, die Regeln der Situation bleiben verständlich.

| ID | Idee und konkreter Spielablauf | Priorität / Herkunft |
| --- | --- | --- |
| QUEST-01 | **Entdeckungsaufgaben:** Spuren, beschädigte Geräte, verschlossene Zugänge oder Fundberichte eröffnen freiwillige Ziele. | P0 · G |
| QUEST-02 | **Persönliche Bitten:** Beruf, Ort und aktuelle Situation bestimmen, welche passende Aufgabe eine Figur anbietet. [S-MCA-QUESTS] | P0 · G+R |
| QUEST-03 | **Eigene Vorhaben als Quests:** Brücke, Garten, Museum oder Bahnstrecke als benannten Plan anlegen; Material und Etappen verfolgen. | P0 · G |
| QUEST-04 | **Mehrere gültige Lösungen:** Wasserlauf freilegen, Pumpe bauen oder Versorgung umleiten können dasselbe Problem lösen. Bedingungen prüfen die Wirkung statt nur eine einzige vorgeschriebene Aktion. | P0 · G |
| QUEST-05 | **Sichtbare Folgen:** Eine gelöste Aufgabe verändert Arbeit, Besucher, Dialog oder Standortzustand. Folgen werden auch ohne geöffnetes Questfenster erkennbar. | P0 · G |
| QUEST-06 | **Gerüchte mit Suchgebieten:** Gespräch, Karte und Umwelthinweise liefern unterschiedliche Genauigkeit. Eine Aussage kann zunächst unbestätigt bleiben. [S-CRUCIAL] | P1 · G+R |
| QUEST-07 | **Lokale Anschlagbretter:** Aufgaben passen zu dortigen Berufen und tatsächlichem Bedarf. Themengebende Aushänge und wiederholbare Lieferungen ergänzen die Geschichten. [S-BOUNTIFUL] | P1 · R |
| QUEST-08 | **Explizite Zielarten:** Besitz, Herstellung, Lieferung, Beobachtung, Benutzung, Kampf und Weltzustand haben eigene Regeln. UI nennt Voraussetzung und Erledigungskriterium. [S-FTB] [C-QUEST] | P0 · R |
| QUEST-09 | **Rückwirkende Anerkennung:** vorhandene Bauwerke, bekannte Entdeckungen und bereits erfüllte Weltzustände können zählen. Ein ausdrücklich geforderter Vorführvorgang bleibt als solcher erkennbar. | P0 · G+R |
| QUEST-10 | **Kooperative Beiträge:** unterschiedliche Spieler liefern Material, erkunden oder betätigen eine Anlage. Gemeinsame Aufgaben erfassen Beiträge und verteilen die vorgesehene Anerkennung genau einmal. | P0 · G |
| QUEST-11 | **Wiederaufnahme:** Aufgabe pausieren, später fortsetzen und den letzten sinnvollen Stand sehen. Wiederkehrende Ereignisse sind erneut erreichbar. | P1 · G |
| QUEST-12 | **Fundstück → Untersuchung → Suchgebiet:** Forscher, Kartentisch oder eigene Untersuchung machen aus einem Fund einen konkreten Reiseanlass. Unabhängiges Entdecken bleibt möglich. [S-ALEX-CAVES] | P1 · R |
| QUEST-13 | **Erinnerungen statt Dauerbespaßung:** ausgewählte Folgen früherer Projekte ergeben spätere kleine Begegnungen. Dialogwiederholungen haben Abstände; jede Figur muss nicht ständig eine neue Aufgabe liefern. | P1 · G+R |

### Beispiel: die stillgelegte Mühle

Ein Bewohner bemerkt fehlende Versorgung. Vor Ort steht das Rad still; flussaufwärts lässt sich die Ursache finden. Der Spieler entscheidet zwischen Freilegen, Umleiten, vorläufigem Pumpbetrieb oder Untersuchung einer Höhle. Das Ziel heißt sinngemäß „Stelle die Versorgung wieder her“. Die gewählte Lösung verändert anschließend die Szene: Mühle arbeitet, Werkstatt produziert, Besucher kehren zurück. Bereits vorhandene brauchbare Infrastruktur zählt.

### Zielzustände technisch präzisieren

Für jede Aufgabe vor der Implementierung bestimmen: Wer besitzt sie? Was ist der genaue Zieltyp? Welche früheren Ereignisse zählen? Welche echten Weltzustände müssen bestehen? Wer erhält die Belohnung? Kann sie nach einem Neustart erneut ausgelöst werden? Was geschieht, wenn ein beteiligter Ort vorübergehend nicht geladen ist? Ein Ereignisjournal und stabile IDs ermöglichen Erklärung und verhindern doppelte Auszahlungen.

## 9. Dungeons, Ruinen und veränderliche Orte

| ID | Idee und konkreter Spielablauf | Priorität / Herkunft |
| --- | --- | --- |
| DUN-01 | **Bauen als Lösungsweg:** Brücken, Leitern, Seile, Deckung und Tunnel helfen beim Erschließen. Regelgrenzen wichtiger Mechanismen sind erkennbar und lokal. | P0 · G |
| DUN-02 | **Räumliche Identität:** Katakombe, vertikaler Förderschacht, offener Steinbruch, Spinnenhöhle und überflutete Halle spielen sich verschieden. Frühere Nutzung ist am Aufbau erkennbar. [S-YUNG] | P1 · G+R |
| DUN-03 | **Technische Hindernisse:** alte Pumpen, gerissene Antriebe, blockierte Plattformen und Lichtanlagen verwenden dieselben Regeln wie Spielerbauten. | P0 · G |
| DUN-04 | **Vorbereitung wird belohnt:** mitgebrachte Seile, Spiegel, Ventile oder ein kleiner Schaltkreis erschließen eigene Lösungen. Ein Standardweg bleibt mit verständlicher Grundausrüstung erreichbar. | P1 · G |
| DUN-05 | **Abkürzungen und Orientierung:** von innen geöffnete Wege, Aussichtspunkte und wiedererkennbare Raumfolgen erleichtern Rückkehr und Ortsverständnis. | P1 · R |
| DUN-06 | **Befreite Orte nutzbar machen:** Mine, Aussichtsturm oder Forschungsraum kann nach Klärung der Gefahr als freigegebener Außenposten dienen. | P1 · G |
| DUN-07 | **Funktionale Relikte:** Geomantenrelikt für definierte Steinsegmente, Frostrelikt für Kühlung/Eis, Sturmrelikt für begrenzten Aufwind, Resonanzrelikt für Empfänger, Monstrositätsrelikt für schwere Mechanik. | P1 · G |
| DUN-08 | **Varianten durch Raumkombination:** wenige sorgfältig ausgearbeitete Module mit klarer Spielidee bilden unterschiedliche Orte. Nicht jeder Raum benötigt einen Kampf. | P2 · R |
| DUN-09 | **Spätere Rückkehr mit neuem Werkzeug:** optionaler Seitenweg reagiert auf eine bekannte spätere Fähigkeit. Karte merkt sich den Hinweis; Hauptfortschritt bleibt ohne dauernden Rücklauf verständlich. | P2 · R |
| DUN-10 | **Gezielte Wiederholbarkeit:** ausgewählte Begegnungen oder Herausforderungen können bewusst erneut aktiviert werden. Gewonnene Ortsentwicklung wird nicht unbemerkt zurückgesetzt. | P2 · G+R |

## 10. Dungeons-Mobs, Bosse, Modelle und Originalnähe

### Inhaltlicher Umfang

Langfristig alle belegten gewünschten Dungeons-Inhalte katalogisieren. Der Katalog trennt mindestens: reguläre Gegner, biomebezogene Varianten, Eliten, Minibosse, Bosse, besondere Bossformen, Ancient-Varianten, beschworene Konstrukte, friedliche Tiere, Haustiere, Begleiter, Händler und andere NPC-Rollen. Eine Variante und eine bloß andere Ausrüstung sind ausdrücklich unterscheidbar. Bestehende Projektkataloge werden weitergeführt; Zählungen entstehen aus realen Einträgen und überprüften Bindungen.

Die Autorenbeschreibung von Dungeons Mobs nennt Dungeons-Kreaturen, konfigurierbare Spawns und Begegnungserweiterungen, beschreibt Animationen aber als unfertig und Bosse als weitere Arbeit. Die Quelle belegt daher keine komplette fertige Originalabdeckung. Cataclysm liefert eigene Bosskonzepte und Inszenierungsbeispiele, keine Ersatzreferenz für originale Dungeons-Figuren. [S-DUNGEONS-MOBS] [S-CATACLYSM]

| ID | Idee und konkreter Spielablauf | Priorität / Herkunft |
| --- | --- | --- |
| MOB-01 | **Verbindlicher Inhaltskatalog:** jede Kreatur mit stabilem Namen/ID, Kategorie, Quelle und Umsetzungsstatus führen; Sift explizit ausschließen. | P0 · G |
| MOB-02 | **Referenzpaket pro Figur:** Ansichten, Proportionen, Farb-/Materialverteilung, charakteristische Details und belegte Bewegungen sammeln. Unsichere Details als solche kennzeichnen. | P0 · G |
| MOB-03 | **Silhouette vor Detailrauschen:** Größe, Haltung, Arm-/Beinlängen, Kopf und erkennbare Formen stimmen bereits in neutraler Pose. Danach Textur, Lichtflächen und kleine Details ausarbeiten. | P0 · G |
| MOB-04 | **Eigene Bewegungsidentität:** Alltag, Fortbewegung, Aufmerksamkeit, Angriff, Trefferreaktion und Tod passen zur Figur. Schwimmende, fliegende und mehrgliedrige Wesen erhalten eigene Bewegungslogik. | P0 · G |
| MOB-05 | **Passende Lebensräume und Rollen:** Kreaturen erscheinen in nachvollziehbaren Regionen und Begegnungen. Ein Wesen kann schützen, bedrängen, Gelände verändern oder unterstützen. [S-DUNGEONS-MOBS] | P1 · G+R |
| MOB-06 | **Lesbare Absichten:** Körperhaltung, Blick, Geräusch und Effekte kündigen relevante Aktionen an. Unterschiedliche Angriffe besitzen unterscheidbare Vorbereitung. | P0 · G |
| MOB-07 | **Bosse mit veränderten Möglichkeiten:** Phasen verändern Verhalten, Verteidigung, Bewegungsräume oder nutzbare Gelegenheiten. Körper und Effekte zeigen den Zustand. [S-CATACLYSM] | P1 · G+R |
| MOB-08 | **Arena und Vorbereitung:** Deckung, vorhandene Maschinen und selbst vorbereitete Konstruktionen können sinnvoll eingesetzt werden. Ein Boss reagiert nachvollziehbar auf Position und Hindernisse. | P1 · G |
| MOB-09 | **Friedliche Begegnungen:** passende Wesen reagieren auf Musik, Futterpflanzen, neugieriges Beobachten oder bestimmte Gegenstände. Wissen kann einen Fund oder Werkstoff erschließen. [S-ALEX-MOBS] | P2 · G+R |
| MOB-10 | **Begleiter mit Nutzen:** Spur verfolgen, etwas anzeigen, kleines Paket tragen oder eine bekannte Interaktion unterstützen; zuverlässig warten, folgen und heimkehren. | P1 · G |
| MOB-11 | **Ausrüstung folgt dem Rig:** Handobjekte, Rüstung, separate Materialien und Leuchtflächen übernehmen echte Gelenktransformationen. Keine schwebenden Waffen oder stillstehenden Anbauteile. | P0 · G |
| MOB-12 | **Skalierte Umweltwirkung:** große Schritte, Wasserkontakt und Aufprall erzeugen passende begrenzte Effekte; kleine Tiere bleiben zurückhaltend. | P1 · G+R |
| MOB-13 | **Vollständigkeit pro Figur sichtbar:** Modell, Textur, Rig, Clips, Verhalten, Audio, Drops, Spawn, Speicherung und Abnahme sind einzelne Statusfelder. | P0 · G |
| MOB-14 | **Ruhige Qualitätsschau:** eine eigene Testszene zeigt Figuren aus mehreren Richtungen und in den relevanten Bewegungen. Danach folgt Abnahme in echter Spielsituation. | P0 · G |

### Die Redstone-Monstrosität als Qualitätsmaßstab

Das vom Nutzer gezeigte Goety-Bild ist die verbindliche Formreferenz für diese gewünschte Fassung. Besonders beachten: breite massive Schulter-/Rückenpartie, die Anordnung der Steinplatten und roten Bereiche, schwer wirkende Unterarme, erkennbare Hände, kompakter unterer Körper, Gesicht und Leuchtflächen. Die Quelle bleibt als Community-Referenz dokumentiert; sie wird nicht als extrahierte Originaldatei ausgegeben.

**Referenz wiederfinden:** [Goety-Wiki zur Redstone-Monstrosität](https://goety.wiki.gg/wiki/Redstone_Monstrosity). Die Seitenadresse und indexierte Beschreibung wurden gefunden; der direkte Seitenabruf war bei dieser Recherche eingeschränkt. Die Formvorgabe oben beruht auf dem tatsächlich im Gespräch gezeigten Bild. Codex muss vor der Modellarbeit die gespeicherte Nutzerreferenz beziehungsweise die im Projekt verwendete Referenzabbildung öffnen und visuell vergleichen. Falls sie im neuen Arbeitskontext fehlt, diesen konkreten fehlenden Nachweis dokumentieren; die Figur bis zum echten Vergleich nicht als „referenzgetreu geprüft“ einstufen. Die übrige Arbeit kann weitergehen. Die offizielle Projektseite ergänzt Herkunft und Kontext von Goety. [S-GOETY]

**Bewegungsentwurf:** Das Gewicht verlagert sich vor einem großen Schritt; Fußkontakt ist klar. Schulter, Arm und Rumpf arbeiten bei einem Schlag zusammen. Vorbereitung, Wirkung und Erholung sind getrennt erkennbar. Mechanische Geräusche und Glühen können einen Zustand vermitteln. Neue Kampfphasen oder beschädigte Bauteile werden als eigene Gameplay-Adaption gekennzeichnet, soweit sie nicht aus einer konkreten Originalreferenz stammen.

**Grafische Abnahme:** Vorderseite, beide Seiten, Rücken, Dreiviertelansicht und Spielerhöhe; neutrale Beleuchtung und Spielsituation; Stillstand, Drehen, Gehen, Angriffe und Übergänge; korrekte Kameraausschnitte; keine abgeschnittenen Füße oder Hände als einziges Beweisbild. Maßstab im Vergleich zu normalen Blöcken und Spielerfigur sichtbar halten.

### Inhaltliche Wiederverwendung

Vorhandene Quellen und Mods sind nützliche Referenzen. Bei tatsächlicher Übernahme müssen konkrete Datei, Autor, Herkunft, Lizenzumfang und Bearbeitung nachvollziehbar sein. Code und Assets können unterschiedliche Bedingungen besitzen. Actions-&-Stuff-Dateien oder dekompilierte Spielarchive sind keine notwendige Grundlage dieses Entwurfs. Eigene Nachbildungen und zulässig wiederverwendete Community-Arbeit werden sauber bezeichnet.

## 11. Gegenstände, Ausrüstung und Kampf

| ID | Idee und konkreter Spielablauf | Priorität / Herkunft |
| --- | --- | --- |
| ITEM-01 | **Dungeons-Gegenstandskatalog:** Waffen, Fernkampf, Rüstungen, Artefakte, Verzauberungen, einzigartige Varianten, Begleitergegenstände und relevante Verbrauchs-/Interaktionsgegenstände mit Quellen erfassen. | P0 · G |
| ITEM-02 | **Nachvollziehbare Einbindung:** jeder Gegenstand erhält Beschaffung, mögliche Herstellung/Verbesserung, Reparatur, Einsatz und verständlichen Eintrag im Weltbuch. Passende Beuteorte und Berufsangebote verbinden Gegenstände mit der Welt. Dungeons Gear bietet dafür eine zusätzliche Ausrüstungsreferenz. [S-DUNGEONS-GEAR] | P1 · G+R |
| ITEM-03 | **Waffengattungen mit eigener Rolle:** Speer hält Abstand, schwerer Hammer hat deutlichen Ausholer, Dolche unterstützen schnelle Positionswechsel, Schild zeigt eine klare Abwehrhaltung. [S-BETTER-COMBAT] | P1 · G+R |
| ITEM-04 | **Einfache zusätzliche Aktionen:** geladener Angriff, bewusste Abwehr und optionaler Ausweichschritt werden verständlich eingeführt. Eingaben für Bauen und Abbauen behalten klare Prioritäten. | P2 · G+R |
| ITEM-05 | **Seitliche statt ausschließlich zahlenmäßige Verbesserungen:** Reichweite, Rhythmus, Anwendung und besondere Wirkung eröffnen verschiedene Spielweisen. | P1 · G |
| ITEM-06 | **Fähigkeiten auch außerhalb des Kampfes:** Rückholwurf, Seilpfeil, Schutzimpuls oder ein passendes Werkzeug können bei Erkundung und Maschinen helfen. | P2 · G |
| ITEM-07 | **Artefakte als tragbare und stationäre Werkzeuge:** eine bekannte Reliktwirkung bleibt in beiden Anwendungen erkennbar; Kosten, Reichweite und Bedienung sind klar. | P2 · G+R |
| ITEM-08 | **Seelen- oder andere vorhandene Artefaktmechaniken abgleichen:** falls im Projekt schon vorhanden, sinnvoll anschließen; Beschaffung und Ausgaben erklären, keine zweite konkurrierende Ressource für denselben Zweck hinzufügen. Goety dient als zusätzliche Referenz für Seelen, Helfer und Geräte. [S-GOETY] | P1 · G+R |
| ITEM-09 | **Ausrüstungsvorlagen:** später getrennte Auswahl für Bauen, Expedition und Kampf speichern. Wechsel zeigt fehlende Gegenstände und verbraucht keine fremd reservierten Bestände. [S-IPN] | P2 · R |
| ITEM-10 | **Persönliche Ausrüstung erhalten:** Reparatur, Name und Geschichte machen ein Lieblingsstück langfristig interessant. Verbesserungen und kosmetische Gestaltung bleiben nachvollziehbar. | P2 · G+R |
| ITEM-11 | **Timing aus einem gemeinsamen Aktionsmodell:** Vorbereitung, aktiver Treffer und Erholung sind serverseitig eindeutig; Animation zeigt denselben Ablauf. | P0 · G+R |
| ITEM-12 | **Gegnergruppen mit Rollen:** kleine Kombinationen aus Nahkampf, Distanz und Unterstützung erzeugen taktische Situationen. Gefahren werden durch Verhalten lesbar. | P1 · G |

## 12. Bewegung, Reisen und Transport

| ID | Idee und konkreter Spielablauf | Priorität / Herkunft |
| --- | --- | --- |
| MOVE-01 | **Kletterseile und Anker:** initiale Expeditionen erschließen Höhen und Schächte provisorisch. Ankerort, Reichweite und freie Verbindung sind sichtbar. | P1 · G |
| MOVE-02 | **Seilrutschen:** gesetzte Punkte verbinden bekannte Orte; Richtung, Höhe und Einstieg bleiben verständlich. | P2 · G |
| MOVE-03 | **Von Provisorium zu Infrastruktur:** Seil wird später durch Treppe, Aufzug und Station ergänzt. Jede Stufe besitzt eigenen Nutzen. | P1 · G |
| MOVE-04 | **Seilbahn und Frachtlift:** feste Stationen und begrenzte Kabinen transportieren Personen oder Material. Redstone-Ankunftssignale und klare Stopps unterstützen Anlagen. | P2 · G |
| MOVE-05 | **Ausgerüstete Boote:** Laterne, begrenzter Stauraum, Anlegepunkt und Forschungszubehör. Häfen und Kanäle werden praktische Reiseorte. | P2 · G |
| MOVE-06 | **Fähren und Bahnstationen:** bekannte Routen erhalten Ein-/Ausstieg und Ladeplätze. Reisende und Kuriere können freigegebene Verbindungen nutzen. | P2 · G |
| MOVE-07 | **Fahrpläne nach Zeit oder Bedarf:** Abfahrt nach ausreichender Ladung, zu einer Zeit oder bei freiem Ziel. UI erklärt den Wartegrund. [C-TRAIN] | P2 · R |
| MOVE-08 | **Regionale Transportanlässe:** besondere Materialien, Besucher und verteilte Werkstätten schaffen freiwilligen Nutzen für Strecken. Lokale Grundversorgung bleibt möglich. | P1 · G+R |
| MOVE-09 | **Außenpostennetz:** Lager, sichere Rast und bekannte Wege unterstützen längere Expeditionen. Aufträge an ausgeladenen Orten werden begrenzt als Zustände gespeichert. | P2 · G |
| MOVE-10 | **Lieferstatus und Reservierung:** bereit, unterwegs, angekommen oder blockiert. Bei Neustart existieren Ware und Auftrag jeweils genau einmal. | P0 · R |
| MOVE-11 | **Ankunft in der Welt anzeigen:** Glocke, Schranke, Abfahrtstafel und Beleuchtung reagieren auf eine tatsächliche Ankunft. | P1 · G |
| MOVE-12 | **Koop-Reisen:** Mitspieler können sich verabreden, einsteigen und wiederfinden. Mechaniken versetzen keine andere Person ohne deren Spielhandlung. | P1 · G |

## 13. Landwirtschaft, Küche, Tiere und gemütlicher Alltag

| ID | Idee und konkreter Spielablauf | Priorität / Herkunft |
| --- | --- | --- |
| FARM-01 | **Gestaltbare Gärten:** Rankgerüste, Sträucher, Obst, Wasserstellen und Schutzplätze verbinden Landwirtschaft mit Umgebungsgestaltung. | P1 · G |
| FARM-02 | **Bewässerung als Bauprojekt:** Rinnen, Tanks und Pumpen versorgen größere Flächen; Mangel wird am System erklärt. | P2 · G |
| FARM-03 | **Gewächshaus und bekannte Bedingungen:** kontrollierbare Licht-/Wasserbedingungen erschließen besondere Pflanzen. Kernnahrung bleibt ohne komplexe Anlage erreichbar. | P2 · G |
| FARM-04 | **Küche mit wenigen verständlichen Schritten:** Vorbereiten, Garen und Anrichten verwenden vertraute Zutaten. Arbeitsflächen und Kochgeschirr sind nutzbare Einrichtung. [S-FARMERS] | P1 · G+R |
| FARM-05 | **Gerichte mit zeitweiligem Nutzen:** klare, maßvolle Vorteile für Expedition, Handwerk oder Erholung. Vielfalt lohnt sich ohne verpflichtenden Ernährungsplan. | P2 · G |
| FARM-06 | **Regionale Rezepte und gemeinsame Mahlzeiten:** Reise bringt ein Rezept, eine Zutat oder ein servierbares Gericht mit. Gasthaus, Fest und eigener Tisch erhalten einen Anlass. | P2 · R |
| FARM-07 | **Haustieralltag:** ruhen, neugierig schauen, sich reinigen, einen geeigneten Platz wählen und auf den Besitzer reagieren. | P1 · G |
| FARM-08 | **Tierbeobachtung und kleine Hilfe:** eine seltene Verhaltensbeobachtung ergänzt das Buch; passende Tiere helfen in einem freigegebenen Garten oder beim Finden einer Spur. | P2 · G+R |
| FARM-09 | **Angelbuch:** bekannte Arten, Fundorte, Wetter-/Köderhinweise und eigene Beobachtungen sammeln. Fischer geben passende Tipps. | P2 · G |
| FARM-10 | **Pflege als freiwillige Vertiefung:** Einrichtung und bessere Bedingungen ermöglichen zusätzliche Szenen und Varianten. Grundbetrieb verursacht keine neue tägliche Pflichtliste. | P0 · G |

## 14. Animation, Effekte und Klang

Die gewünschte Qualität entsteht aus Bewegung, Reaktion und Alltag. Ausdruck folgt einem Grund: Eine Figur sieht etwas, hält inne, verändert ihre Haltung und handelt. Actions & Stuff und Fresh Animations sind dafür visuelle Referenzen; Not Enough Animations zeigt den Wert lesbarer Tätigkeiten aus der Außenansicht. [S-ACTIONS] [S-FRESH] [S-NEA]

| ID | Idee und konkreter Spielablauf | Priorität / Herkunft |
| --- | --- | --- |
| AV-01 | **Fortbewegung mit Bodenkontakt:** Gehen, Laufen, Drehen, Bremsen und passende Höhenwechsel. Schrittweite und Geschwindigkeit passen zusammen. | P0 · G |
| AV-02 | **Blick und Aufmerksamkeit:** Figuren bemerken Geräusche, Ziele und Interaktionen; Blickbewegung und Körperhaltung ergänzen sich. | P0 · G |
| AV-03 | **Übergänge und Unterbrechungen:** Arbeit, Essen, Kampf, Schwimmen und Flucht wechseln sauber. Eine abgebrochene Tätigkeit hinterlässt weder hängende Werkzeuge noch eine blockierte Pose. | P0 · G+R |
| AV-04 | **Sichtbare Spielerhandlungen:** Kartenlesen, Trinken, Rudern, Klettern, Kurbeln und Handwerk sind für Mitspieler verständlich. [S-NEA] | P1 · G+R |
| AV-05 | **Ego- und Außenansicht:** Blick nach oben/unten, verschiedene Sichtfelder und gehaltene Gegenstände gesondert prüfen. Gemeinsame Aktionszustände können unterschiedliche passende Darstellungen besitzen. [C-FOV] | P0 · R |
| AV-06 | **Kleine Alltagsszenen:** Kiste abstellen vor dem Türöffnen, Werkstück ins Licht halten, nasses Fell schütteln, neugierig am Ufer schauen. | P1 · G |
| AV-07 | **Ursachenbezogene Partikel:** Spritzer, Staub, Laub, Pollen und Nebel entsprechen Ort, Wetter, Größe und Bewegung. [S-PARTICULAR] | P1 · G+R |
| AV-08 | **Räumliche Akustik:** freies Feld, Höhle, Gasthaus und Maschinenhalle klingen unterschiedlich. Wände dämpfen; größere Räume haben passenden Nachhall. [S-SOUND] | P1 · G+R |
| AV-09 | **Hörbare Maschinenzustände:** belastete Winde, laufende Kupplung, Ventil und trockene Pumpe besitzen unterscheidbare Geräusche. Relevante Zustände bleiben zusätzlich sichtbar. | P1 · R |
| AV-10 | **Situationsbezogene Musik:** zurückhaltende Übergänge für Zuhause, Reise, Entdeckung und Gefahr. Musik lässt Raum für Umweltgeräusche. [S-BLISS] | P2 · R |
| AV-11 | **Getrennte Komfortregler:** Kamerawackeln, Bewegungsstärke, Blitze, Partikeldichte, Hall und Dialoghäufigkeit einzeln einstellen. | P0 · G |
| AV-12 | **Sichtbare Audioalternativen:** Richtung und Art wichtiger Geräusche bei Bedarf als dezenter Hinweis; Resonanzmuster zusätzlich als Zeichen. | P0 · G |
| AV-13 | **Entfernungsgerechte Qualität:** nahe wichtige Figuren detailliert, entfernte Figuren vereinfacht. Umgebungsinsekten und Effekte erhalten separate Budgets. | P0 · G+R |
| AV-14 | **Einheitliche Bildsprache:** Formen, Texturmaßstab, Leuchtflächen, Partikel und UI passen zusammen. Unterschiedliche Quellen werden gestalterisch abgestimmt. | P0 · G |

## 15. Fortschritt, Regionen und langfristige Ziele

| ID | Idee und konkreter Spielablauf | Priorität / Herkunft |
| --- | --- | --- |
| PROG-01 | **Mehrere gleichwertige Wege:** Erkunden, Bauen, Technik, Naturkunde, Bewohnerprojekte und Kämpfe erschließen jeweils interessante Möglichkeiten. | P0 · G |
| PROG-02 | **Wissen durch Tun:** erste Begegnung, beobachtetes Verhalten, gelungene Konstruktion oder untersuchter Fund ergänzt verständlich das Weltbuch. | P1 · G+R |
| PROG-03 | **Regionale Materialien:** besondere Vorkommen und Werkstoffe geben Reise und Handel einen Anlass; Kernrezepte bleiben erreichbar. | P1 · G |
| PROG-04 | **Nether als Wärmebereich:** eigene spätere Region mit besonderen Wärmequellen und Materialien für thermische Anlagen. Präzise Mechaniken erst an kleinem Prototyp prüfen. | P3 · G |
| PROG-05 | **End als Raum- und Bewegungsbereich:** begrenzte Aufwinde, verankerte schwebende Plattformen oder Geräte zur Umlenkung von Bewegung. Keine ungefragte globale Schwerkraftänderung. | P3 · G |
| PROG-06 | **Unterwasserexpeditionen:** versunkene Pumpwerke, Forschungseinrichtungen und Taucherglocken verbinden Erkundung mit Wassertechnik. | P2 · G |
| PROG-07 | **Eigene große Ziele:** Museum, Bibliothek, Hafen, Verkehrsnetz, Naturpark, Theater oder Observatorium tragen langfristig. | P1 · G |
| PROG-08 | **Weltchronik:** ausgewählte Eröffnungen, Entdeckungen, Bossbegegnungen und Bauwerke werden mit Beteiligten und Ort festgehalten. | P1 · G |
| PROG-09 | **Sammlungen mit sichtbarem Ergebnis:** Pflanzen, Fundstücke, Fotos, Karten und besondere Ausrüstung werden ausgestellt; Sammlung ergänzt die Welt statt nur Zähler zu erhöhen. | P2 · G+R |
| PROG-10 | **Wiederkehrende freiwillige Herausforderungen:** Expedition oder Begegnung erneut aktivieren, eigene Konstruktion verbessern oder besondere Beobachtung nachholen. | P2 · G |

## 16. Koop und gemeinsame Welt

| ID | Idee und konkreter Spielablauf | Priorität / Herkunft |
| --- | --- | --- |
| COOP-01 | **Gemeinsame Baupläne:** Teilabschnitte, Materiallisten und Ausrichtung teilen. Zuständigkeiten sind freiwillig und wechselbar. | P1 · G |
| COOP-02 | **Reservierungen:** dieselbe Einheit Material kann nicht zwei Aufträgen gleichzeitig zugesagt werden. Freigabe und Rückgabe sind sichtbar. | P0 · G+R |
| COOP-03 | **Persönliche und gemeinsame Karten:** eigene Notizen bleiben privat; gezielte Pings, Orte und Projekte können geteilt werden. | P1 · G |
| COOP-04 | **Rechte an Kisten und Werkstätten:** ansehen, entnehmen, bestellen, bedienen und umbauen sind nachvollziehbar zuweisbar. | P0 · G |
| COOP-05 | **Gemeinsame Questanerkennung:** Beitrag verschiedener Spieler zählt entsprechend der Aufgabe. Beitritt, Verlassen und Neustart erzeugen keine doppelten Belohnungen. | P0 · G |
| COOP-06 | **Unterschiedliche Spielweisen helfen einander:** Baumeister, Entdecker und Techniker können sinnvolle Beiträge liefern; feste Kampfklassen sind nicht Voraussetzung. | P0 · G |
| COOP-07 | **Zusammen reisen:** benannte Treffpunkte, sichtbare Mitreisende und nutzbare Stationen erleichtern Expeditionen. | P1 · G |
| COOP-08 | **Gemeinsame Erinnerung:** Chronik, Fotos, Ausstellung und Wappen machen die Welt zur Geschichte der Gruppe. | P2 · G |
| COOP-09 | **Eigene gemeinsame Aufträge:** Spieler können freiwillig Material oder Unterstützung für ein konkretes Projekt anfragen. Belohnung und Vorrat werden eindeutig hinterlegt, keine automatische Verpflichtung anderer Spieler. | P2 · R |

## 17. Kleine Funktionen mit großer Wirkung

| ID | Idee und konkreter Spielablauf | Priorität / Herkunft |
| --- | --- | --- |
| DETAIL-01 | **Beschriftbare Wegweiser:** eigene Ortsnamen passen zu Karte und ausgewählten Routen. | P1 · G |
| DETAIL-02 | **Instrumente und eigene Melodien:** allein oder gemeinsam spielen; optional Resonanzempfänger bedienen. | P2 · G |
| DETAIL-03 | **Fensterläden, Vorhänge und Markisen:** einfache Bedienung und passende Bewegungen; mechanisch oder per Signal erweiterbar. | P2 · G |
| DETAIL-04 | **Gegenstände präsentieren:** besondere Funde und Ausrüstung auf geeigneten Flächen zeigen. | P1 · G |
| DETAIL-05 | **Werkzeughalter und Lagerregale:** Werkstattinhalt sichtbar machen; Interaktion bleibt an echte Bestände gebunden. | P1 · G |
| DETAIL-06 | **Eigene Banner und Wappen:** Orte, Gruppen und ausgewählte Projekte bekommen ein wiedererkennbares Zeichen. | P2 · G |
| DETAIL-07 | **Fotomodus:** tatsächliche Weltaufnahmen mit geeigneter Kamera und frei wählbaren Komfortoptionen. Im Mehrspieler gelten klare Rechte und Weltzustände. | P2 · G |
| DETAIL-08 | **Kamera als Gegenstand:** Sucher, Aufnahmematerial und Bilder als Objekte; später einfacher Prozess von Aufnahme bis Abzug. [S-EXPOSURE] | P2 · R |
| DETAIL-09 | **Postkarten und Fotoalben:** ein eigenes Bauwerk, ein Naturereignis oder eine Expedition wird als Erinnerung teilbar und ausstellbar. | P2 · R |
| DETAIL-10 | **Besucherführung:** ausgewählte eigene Orte verbinden und Besuchern einen nachvollziehbaren Weg anbieten. | P2 · G |
| DETAIL-11 | **Kleine mechanische Dekoration:** Uhren, Figuren und bewegliche Schilder verwenden dieselben Bauteile wie größere Anlagen. | P2 · G |
| DETAIL-12 | **Rückkehr nach einer Pause:** Weltbuch fasst selbst ausgewählte offene Vorhaben und letzte bedeutsame Änderungen zusammen. | P1 · R |

## 18. Weltprofile und Entscheidungen zum Spielgefühl

| ID | Idee und konkreter Spielablauf | Priorität / Herkunft |
| --- | --- | --- |
| SETTINGS-01 | **Ruhiger Aufbau:** geringe selbständige Bedrohung, Entdeckung und Bewohneralltag im Vordergrund; Kämpfe bewusst zugänglich. [S-BLISS] | P1 · G+R |
| SETTINGS-02 | **Lebendige Abenteuer:** reguläre Gefahren, Quests und Expeditionen; nachvollziehbare angekündigte Bedrohungen. | P1 · G |
| SETTINGS-03 | **Technik und Experimente:** Kernsysteme leicht zugänglich, Erklärungen prominent, für Testwelten geeignete Optionen. | P1 · G+R |
| SETTINGS-04 | **Unabhängige Regler:** Bedrohung, Überfälle, Wetterwirkung, Bewohneraktivität, Strukturdichte, Atmosphäre und Darstellungsqualität getrennt. [S-QUARK] | P1 · G+R |
| SETTINGS-05 | **Speicherbare Profiländerungen:** Abschalten eines Systems beschädigt dessen gespeicherte Gegenstände und Weltzustände nicht; Verhalten wird vorher erklärt. | P0 · R |
| SETTINGS-06 | **Respektierte Offlinezeit:** keine standardmäßigen Strafspiralen durch Abwesenheit. Zeitabhängige Aufträge pausieren oder werden begrenzt und nachvollziehbar fortgeführt. | P0 · G |

### Bewusste Leitentscheidungen

- Die erste Fassung braucht keine neue Pflichtmechanik für Durst, Körpertemperatur oder tägliche Beziehungen.
- Große bewegliche Schiffe und beliebig große physikalische Bauwerke sind mögliche spätere Forschung, aber keine Voraussetzung des Kernkonzepts.
- Globale Teleportlager würden den Nutzen lokaler Werkstätten und Transportwege stark verändern. Für die erste Fassung lokale, ausdrücklich verbundene Vorräte bevorzugen.
- Ein eigener NPC darf Atmosphäre schaffen, ohne eine zusätzliche Währung oder zwingende Produktionskette einzuführen.
- Kämpfe sollen lesbar und variantenreich sein. Vor Einführung einer weiteren Leiste oder komplizierter Eingabefolgen ihren konkreten Nutzen zeigen.
- Mehr Strukturen, Partikel oder Figuren sind keine automatische Qualitätssteigerung. Ruhige Landschaft und klare Sicht gehören zur gewünschten Atmosphäre.

## 19. Drei zusammenhängende Spielerlebnisse

### 19.1 Dorf am Fluss und alter Steinbruch – erster vollständiger Abschnitt

1. Spieler entdeckt eine kleine Region mit einem Dorf, einem Fluss und einem stillgelegten Steinbruch.
2. Bereits ohne Quest beobachtet man einen Vogelschwarm und eine Entenfamilie beim Wechsel zwischen Wasser und Ufer. Die Familie bleibt zusammen; einzelne Tiere ruhen oder suchen Nahrung. Wenige Bewohner haben zuverlässige Arbeit, Sitzplätze und Tagesabläufe.
3. Spieler baut ein eigenes Gasthaus und erklärt den Bereich als nutzbaren Ort.
4. Eine fehlende Flussverbindung verhindert einen Besucherweg. Weltbuch beschreibt das konkrete Problem.
5. Spieler baut eine Brücke oder eine andere zulässige Verbindung. Das System erkennt die Nutzbarkeit und bereits vorhandene Lösungen.
6. Eine Handkurbel/Winde, danach ein Wasserrad, führt Kraft und Redstone verständlich ein. Eine kurze Versuchsszene erklärt die Anlage.
7. Ein Reisender erreicht den Ort und erzählt vom Steinbruch. Ein Fundbericht liefert ein Suchgebiet.
8. Der Steinbruch verlangt Orientierung, Bauen und die Nutzung eines verständlichen Mechanismus. Eine charakteristische Gegnerbegegnung bildet den Höhepunkt.
9. Ein Relikt ermöglicht eine klar begrenzte neue Anwendung zu Hause. Eine Werkstattbestellung liefert passende Bauteile.
10. Eine Figur nutzt die fertige Verbindung oder Anlage; Weltbuch und Chronik zeigen das Ergebnis. Nach Speichern/Laden bleibt der Ablauf konsistent.

**Warum dieser Abschnitt:** Er verbindet die wichtigsten Wünsche in einem realen Spielablauf. Die Dungeons-Monstrosität kann separat als Qualitätsbegegnung geprüft werden; die erste Region muss nicht auf die Fertigstellung aller Bosse warten.

### 19.2 Küste mit Hafen, Kanal und Forschung

Ein Fischer berichtet von Funden nach einem Sturm. Eine Kamera dokumentiert Spuren; ein Archäologe grenzt das Suchgebiet ein. Eine versunkene Anlage verwendet Ventile und Pumpen. Zurück im Hafen helfen Fundmaterial und Wissen beim Bau einer Schleuse. Ein Kurierboot liefert nun Waren zum oberhalb gelegenen Gasthaus. Küstenpflanzen und Tiere besiedeln einen gestalteten Ufergarten.

### 19.3 Kristalltal mit Observatorium und Musik

Ein Fundstück führt zu einem Tal mit ungewöhnlicher Akustik. Sichtbare Resonanzzeichen erklären die ersten Empfänger. Der Spieler löst einen Zugang durch Klang oder einen passenden Bauweg. Zu Hause entstehen ein Instrument, ein musikalisch bedienbares Tor und später ein Observatorium mit Spiegeln. Bewohner besuchen eine kleine Aufführung, deren Mechanik der Spieler selbst konstruiert hat.

Diese Abläufe sind eigene Synthesen. Kein einzelner genannter Mod wird als Lieferant dieses vollständigen Gesamtspiels dargestellt.

## 20. Umsetzung, Reihenfolge und technische Grenzen

### 20.1 Zuerst den vorhandenen Stand verstehen

Codex erfasst tatsächlich verwendete Minecraft-/Loader-/Java-Version, Start- und Buildwege, vorhandene Inhalte, Renderer, Animation, Datenspeicherung, Netzwerkzustände, Questmodell und UI. Existierende `AGENTS.md`, README, Übergabedokumente und Statusdateien werden gelesen und gegen Code und Laufzeit abgeglichen. Eine alte Versionsnummer in einem Text überschreibt keinen begründeten neueren Projektstand.

Ergebnis dieses Abgleichs: Für relevante Ideen-IDs **vorhanden**, **teilweise**, **fehlt**, **widersprüchlich** oder **zuerst zu prüfen** mit kurzem Nachweis. Vorhandene Systeme werden bevorzugt erweitert. Fehler und Blockaden mit Bezug zum gewählten Abschnitt zuerst beheben.

### 20.2 Etappen

| Etappe | Sichtbares Ergebnis | Benötigte Grundlagen | Noch nicht Ziel dieser Etappe |
| --- | --- | --- | --- |
| M0 – Bestand und Verlässlichkeit | Projekt startet; Inhalt und Speicherzustände sind nachvollziehbar; ein ausgewählter Mob und eine Interaktion funktionieren | Bestandsprüfung, reproduzierbarer Start, klare Statusliste | Plattformwechsel, alle Kataloginhalte gleichzeitig |
| M1 – Beobachtbare Natur und bewohnter Ort | zuerst Ufergebiet mit zusammenhängendem Vogelschwarm-/Familienverhalten; anschließend Gasthaus, Weg, Besucher und kleine Quest | lokale Habitate, Gruppenidentität, glaubwürdige Animation, Funktionsräume, Erreichbarkeit und gespeicherter Zustand | komplette Wirtschaft, globale Ökosimulation und Regionspolitik |
| M2 – Erste Maschine | Kurbel/Wasserrad, Welle, Winde, Redstone-Schaltung, Diagnose und Versuchsszene | begrenztes Kraftnetz, Bewegung, saubere Bedienung | beliebig große bewegliche Konstruktionen |
| M3 – Verbundene Versorgung | verbundene Vorräte, Werkstattauftrag, Reservierung, sichtbare Lieferung, erste Pumpanlage | Inventartransaktionen, Aufträge, Rechte, begrenzte Flüssigkeitsnetze | weltweite Vollsimulation aller Lieferanten |
| M4 – Abenteuer bringt Baufortschritt | ein ausgearbeiteter Dungeon, charakteristische Begegnung, Relikt und neue häusliche Anwendung | Ziele mit mehreren Lösungen, Kampf-/Animationszustände, Bestiarium | vollständige Sammlung aller Bosse |
| M5 – Systemtiefe | Resonanz, erweiterte Pläne, Habitate, Regionen und weitere Berufsrollen | solide Kernabläufe, Profiling, Datenmodelle | jeder experimentelle Vorschlag gleichzeitig |
| M6 – Spätere Forschung | Optik, besondere Materialien, regionale Erweiterungen, optionale Steuerpulte | gemessene Leistungsreserve und begrenzte Prototypen | unbegrenzte Simulation als pauschales Ziel |

Der Dungeons-Inhaltskatalog wächst parallel in zusammenhängenden Familien. Jede Ergänzung erreicht ihren definierten Qualitätsstand; die bloße Anzahl angelegter IDs gilt nicht als Fortschritt bei Modell- und Bewegungsqualität.

### 20.3 Zuständigkeiten der Systeme

| Bereich | Verantwortung | Wichtige Verbindung |
| --- | --- | --- |
| Inhalt und Referenzen | stabile IDs, Quellen, Modelle, Gegenstände und Status | Verhalten und Darstellung referenzieren dieselben Inhalte |
| Welt und Orte | Habitat, nutzbarer Raum, Wege, Ortszustand | Bewohner, Aufträge und Quests fragen Zustände ab |
| Bewohner | Identität, Rolle, Tagesablauf, erreichbare Tätigkeit | liefern sichtbare Arbeit zu echten Aufträgen |
| Technik | Netze, Antriebe, Behälter, Signale, begrenzte Wirkungen | klare Adapter zu Redstone und Inventaren |
| Gegenstände und Aufträge | echte Bestände, Reservierung, Verarbeitung, Lieferung | UI zeigt den autoritativen Zustand |
| Abenteuer | Voraussetzungen, Zielarten, Beiträge und Folgen | Ereignisse und Weltzustände statt verstreuter Sonderfälle |
| Darstellung | Animation, Partikel, Klang und Oberfläche | zeigt Zustände, entscheidet nicht allein über Spielwirkung |

Diese Tabelle beschreibt Verantwortung, keine vorgeschriebene Paketstruktur. Bestehende gut funktionierende Architekturen dürfen die Aufgaben anders organisieren.

### 20.4 Leistung und Speicherbarkeit

- **Server entscheidet** über Material, Treffer, Auftragszustand, Eigentümer und Fortschritt. Lokale Vorschau ist klar von echter Änderung getrennt.
- **Nahe Figuren:** vollständige relevante Bewegung und Interaktion. **Entfernte Figuren:** vereinfachte Aktivitäten. **Ausgeladene Bereiche:** gespeicherte begrenzte Aufträge und Ortszustände, kein dauerhaftes Pathfinding.
- Reine Umgebungsoptik nach Möglichkeit clientseitig und ohne unnötige Server-Entitäten; echte interaktive Tiere behalten passende Serverlogik.
- Ein entfernter Schwarm darf vereinfacht dargestellt werden. Bei Annäherung und Landung bleiben Gruppe, Richtung und Zustand konsistent. Nah beobachtete Enten und Jungtiere sind echte zusammengehörige Tiere mit gemeinsamen Zielen, erreichbaren Wegen und stabiler Identität. Darstellung und Simulation nutzen die passende Detailstufe desselben Zustands.
- Lebensräume und Raumerkennung verwenden begrenzte Prüfbereiche und reagieren auf Änderungen. Keine großflächige Komplettsuche in jedem Tick.
- Kraft-, Flüssigkeits- und Signaletze besitzen begrenzte Knoten-/Aktualisierungsbudgets. Veränderungen am Netz lösen gezielte Neubewertung aus.
- Vorschau, Foto, Diagnose und Weltbuch dürfen die eigentliche Simulation nicht unbemerkt verdoppeln.
- Aufträge und Belohnungen bleiben bei Verbindungsabbruch, Neustart und Chunkwechsel eindeutig. Inventaroperationen berücksichtigen gleichzeitige Spieleraktionen.
- Leistungsziele erst am tatsächlich verwendeten Testsystem messen. Keine erfundenen FPS-, Mobzahl- oder Serverkapazitätsversprechen.

### 20.5 Bestehenden Mod integrieren oder selbst entwickeln?

Für jeden konkreten Kandidaten eine kurze Entscheidung festhalten:

1. Welches genau benannte Problem löst er für den aktuellen Abschnitt?
2. Unterstützt eine konkret geprüfte Ausgabe die tatsächliche Projektplattform?
3. Passt Verhalten, UI und Optik zur gemeinsamen Vision?
4. Überschneidet er sich mit bestehenden Systemen für Animation, Eingabe, Kampf, Inventar oder Worldgen?
5. Reicht Konfiguration/Datenschnittstelle, ist ein Adapter sinnvoll oder ist ein kleiner eigener Baustein besser?
6. Welche Bedingungen gelten für tatsächlichen Code- oder Assetgebrauch?
7. Wie bleibt ein Spielstand erhalten, wenn die Integration geändert wird?

Als Entscheidungsnotiz genügen Nutzen, konkrete Version/Quelle, Abhängigkeiten, Überschneidungen, Anpassung und ein überprüfbarer nächster Schritt. Keine pauschale Installation aller Inspirationsprojekte.

## 21. Was als fertig gilt

### 21.1 Einheitliche Funktionsabnahme

Eine Idee ist fertig, wenn sie ihren beschriebenen Spielnutzen erfüllt, den echten Zustand verwendet, verständlich bedienbar ist und nach den relevanten Speicher-/Mehrspielersituationen korrekt bleibt. Sichtbare Prototypen, reine Datenvorlagen und vollständige Spielfunktionen werden getrennt bezeichnet.

| Bereich | Sinnvolle Abnahme |
| --- | --- |
| Gasthaus | frei gebauter Raum wird genutzt; Tür/Arbeitsplatz erreichbar; gesperrter Weg erklärt; Neu-Laden erhält Zuordnung |
| Lebendiges Ufer | Familie schwimmt und geht gemeinsam an Land; Nachzügler werden berücksichtigt; Ruhe/Nahrungssuche entstehen auch bei stillstehendem Spieler; Flucht endet wieder in geeignetem Verhalten |
| Vogelschwarm | Flug, Zusammenhalt, Landung und erneuter Start sind plausibel; Individuen bewegen sich versetzt; Nähewechsel dupliziert keine Mitglieder und lässt sie nicht sichtbar springen |
| Kraft und Tor | alle Kombinationen aus Signal und Antrieb verständlich; Stoppen, Richtung und Blockade funktionieren; Lastregel ist sichtbar |
| Tank und Pumpe | Menge bleibt erhalten; voll/leer/getrennt sauber behandelt; keine zusätzliche Menge durch Rücklauf oder Neustart |
| Projektmaterial | zwei Ziele mit gemeinsamen Zutaten, Alternative und Restprodukt korrekt berechnet; reservierter Bestand nicht doppelt ausgegeben |
| Kurierauftrag | Ware existiert einmal; Auftrag setzt nach Chunk-/Serverwechsel konsistent fort; blockiertes Ziel verursacht keinen Verlust |
| Quest | vorhandene Brücke anerkannt; falsche Voraussetzung erklärt; mehrere Lösungen geprüft; Belohnung genau einmal |
| Kampf | Ankündigung, aktiver Treffer und Erholung passen zusammen; Reichweite und Zielrichtung stimmen; Bauen/Abbauen weiter bedienbar |
| Animation | relevante Ansichten, Gelände, Ausrüstung, Übergänge und Abbrüche geprüft; keine bloße Standpose als Bewegungsnachweis |
| UI | Maus, Tastatur, Controllerweg beziehungsweise klarer aktueller Supportstatus; Skalierung, Kontrast und nicht-dragbare Alternativen |
| Koop | zwei reale Clients für konkurrierende Inventare, gemeinsame Ziele und sichtbare Aktionen; getrennt von reinem Einzelspielertest berichten |
| Weltprofile | Änderung erklärt; ausgeschaltete Systeme beschädigen keine Bestände; Speicher-/Ladeverhalten nachvollziehbar |

Automatisierte Tests auf Materialerhaltung, Reservierung, Zustandsübergänge und andere echte Fehlerrisiken konzentrieren. Grafische Qualität im realen Client prüfen. Nach ausreichendem Nachweis mit dem nächsten benötigten Schritt fortfahren; keine bedeutungslosen Tests erzeugen, die nur den Implementierungstext nachbilden.

### 21.2 Nachweise für Modelle und Animationen

- Bearbeitbare Quelldateien und Texturen erhalten, soweit sie zur eigenen oder zulässig übernommenen Arbeit gehören.
- Quelle, Anpassungen und Status je Figur dokumentieren.
- Reale Bildfolgen oder kurze Clips zeigen tatsächliche Bewegungen und Übergänge.
- Darstellung im Modellwerkzeug, unabhängiger Render, UI-Konzept und echter Minecraft-Client werden korrekt bezeichnet.
- Testaufnahme zeigt Datum/Stand, Figur und Aktion nachvollziehbar. Bekannte Abweichungen benennen.
- Einzelspielertest, Mehrspielertest und reine visuelle Prüfung werden nicht gegenseitig ersetzt.

### 21.3 Fortschrittsdatei

Codex soll vorhandene Statusdateien weiterführen oder eine passende einfache Übersicht ergänzen. Pro bearbeiteter Ideen-ID mindestens:

```text
Ideen-ID:
Ziel und sichtbarer Nutzen:
Stand: geplant / begonnen / Prototyp / integriert / geprüft
Vorhandene Implementierung und Wiederverwendung:
Geänderte Dateien:
Quellen und tatsächlicher Assetgebrauch:
Ausgeführte Prüfung und Ergebnis:
Bekannte Lücke:
Nächster konkreter Schritt:
```

## 22. Auftrag zur Auswahl und Nutzung passender Skills

Der frühere Wunsch des Nutzers, Codex passende wichtige Skills mitzugeben, bleibt Teil der Übergabe. Dieses Dokument installiert keine Skills auf dem Rechner des Nutzers.

### Benötigte Fähigkeiten

| Arbeitsbereich | Was ein passender Skill abdecken sollte |
| --- | --- |
| Minecraft-Laufzeit | tatsächlicher Loader, Registrierungen, Daten, Entitäten, Verhalten, Netzwerk, Speichern und reproduzierbare Start-/Buildwege |
| Modelle und Animation | Blockbench oder vorhandene Modellwerkzeuge, UV/Texturen, Rig, Übergänge, Ausrüstungsanker, Leuchtmaterialien und Referenzvergleich |
| Bestehende Quellen | nachvollziehbarer Import/Adapter für tatsächlich verwendete Formate, Quellenstatus und konkrete Nutzungsbedingungen |
| Native grafische Prüfung | realer Minecraft-Client, reproduzierbare Szene, Kamerawinkel, Clips/Bildfolgen, Unterscheidung von Darstellung und Spielverhalten |
| UI und UX | zustandsgebundene Oberfläche, Inventar-/Rezeptlogik, Fokus, Skalierung, Controller und Bedienung ohne Ziehen |
| Leistung und Qualität | Profiling, begrenzte Simulation, relevante Zustands-/Inventartests und gezielte Mehrspielerprüfung |

### Vorgehen für Codex

1. Verfügbare und bereits installierte Skills sowie Projektanweisungen inventarisieren. Vorhandene passende Workflows wiederverwenden.
2. Geeignete zusätzliche Skills nur aus nachvollziehbaren, tatsächlich verfügbaren Quellen auswählen. Aktuelle Installationswege aus der konkreten Umgebung verwenden; keine erfundenen Paketnamen oder veralteten Befehle übernehmen.
3. Wenn ein geeigneter allgemeiner Installer-/Skill-Creator-Workflow vorhanden ist, ihn für Installation oder lokale Projektskills nutzen. Dafür nötige Berechtigungsregeln bleiben gültig.
4. Die folgenden Namen sind **Vorschläge für projektspezifische Workflows**, keine behaupteten öffentlichen Pakete: `dungeons-neoforge-runtime`, `dungeons-models-animation`, `dungeons-native-qa`. Den ersten Namen an den tatsächlich verwendeten Loader anpassen. Optional eine eigene UI-/Projektplanungsanleitung ergänzen.
5. Inhalt der Workflows konkret auf das Repository beziehen: reale Befehle, maßgebliche Dateien, Referenzordnung, bekannte Fallstricke und Abnahme. Veraltete historische Generatoren dürfen aktuelle Modelle nicht versehentlich ersetzen.
6. Nur die für die aktuelle Aufgabe nötigen Fähigkeiten ergänzen. Eine neue Animation benötigt nicht automatisch Browser-, Deployment- oder Cloudwerkzeuge.
7. Tatsächlich installierte beziehungsweise erstellte Skills, Herkunft und Einsatzzweck knapp berichten. Wenn keine Installation stattgefunden hat, keine solche behaupten. Skillpflege entsprechend dem verwendeten Workflow versionieren.

## 23. Konkreter Einstiegsauftrag und Vollständigkeitsabgleich

### Kopierbarer Auftrag für die nächste Codex-Sitzung

```text
Arbeite im vorhandenen Minecraft-Mod-Projekt weiter. Lies zuerst
MINECRAFT_2_IDEEN_FUER_CODEX.md vollständig sowie die aktuellen Projektanweisungen.

Ziel ist normales Minecraft mit Dungeons-Kreaturen und -Gegenständen, lebendiger
Welt, hochwertigen Animationen, freiwilligen Quests, besserem UI und neuen
kombinierbaren Mechaniken. Sift bleibt ausgeschlossen. Die vom Nutzer gezeigte
Redstone-Monstrosität ist die verbindliche Formreferenz für die gewünschte Fassung.

Crimson Desert und ARK: Survival Evolved sind ausdrücklich genannte Referenzen
für das Weltgefühl. Priorisiere beobachtbare eigene Tierabläufe: Vogelschwärme,
gemeinsam schwimmende Entenfamilien, Ruhe, Nahrungssuche und nachvollziehbare
Reaktionen. Ein kleiner Naturabschnitt muss schon ohne Quest oder aktive
Spielerhandlung glaubwürdig leben. Details stehen in Kapitel 7.1.

Prüfe zuerst den tatsächlichen Code-, Build- und Laufzeitstand. Der Nutzer hat
bereits weitergearbeitet; frühere Statusangaben sind nicht automatisch aktuell.
Erhalte vorhandene funktionierende Systeme und Spielstände. Plane keinen
Loaderwechsel oder Neubau ohne konkreten Bedarf.

Ordne die für den ersten Spielabschnitt relevanten Ideen-IDs dem Bestand zu.
Wähle den wichtigsten fehlenden zusammenhängenden Ablauf aus Kapitel 19.1 und
den Etappen M0 bis M2. Schreibe konkrete Aufgaben mit sichtbarem Ergebnis,
Abhängigkeiten und Abnahmekriterien. Setze anschließend den ersten sinnvollen
Abschnitt durchgängig um, statt nur Oberflächen, Platzhalter oder Registry-IDs
anzulegen. Fahre mit bereits autorisierten und notwendigen Schritten fort.

Nutze passende vorhandene Skills. Prüfe und ergänze relevante zusätzliche
Skills oder projektspezifische Workflows gemäß Kapitel 22, ohne Paketnamen zu
erfinden oder unpassende Abhängigkeiten zu installieren.

Die verlinkten Mods sind Inspirationsquellen. Entscheide für jede echte
Integration anhand der Projektplattform, Überschneidungen, Schnittstellen und
konkreten Bedingungen. Eigene Designableitungen sind keine behaupteten Features
dieser Mods. Übernimm keine Assets ohne geklärte konkrete Herkunft und Nutzung.

Prüfe Logik an ihren echten Risiken und grafische Änderungen im realen Client.
Berichte genau, was umgesetzt und ausgeführt wurde, was nur Prototyp ist und
was noch fehlt. Halte Ideen-IDs, Quellen und Fortschritt nachvollziehbar fest.
```

### Abgleich mit allen bisherigen Themen

| Bisheriger Wunsch beziehungsweise Ideenbereich | Hier vollständig aufgegriffen |
| --- | --- |
| Minecraft-Sandbox erhalten, Dungeons-Welt und sämtliche belegten Inhalte als langfristiges Ziel | Kapitel 0–1, 9–11 |
| Alle Mobkategorien, Bosse, Minibosse, Varianten, Händler, Begleiter und Items; Sift-Ausschluss | Kapitel 0 und 10–11 |
| Detailgetreue Monstrosität anhand des Nutzerbilds | Kapitel 10 und 21 |
| Originalnähe, vorhandene Quellen, Animationen, echte Spielnachweise | Kapitel 10, 14 und 21 |
| Neue Systeme wie Redstone: Kraft, Druck, Resonanz, Licht, Materialzustände, Diagnose | Kapitel 3 |
| Theater, Aufzüge, Schleusen, Spiegelgarten und mechanische Dekoration | Kapitel 3–4, 12 und 17 |
| Funktionsräume, freie Architektur, Pläne, Materialpalette und bewegliche Bauwerke | Kapitel 4 |
| Neues HUD, Inventar, Crafting, Weltbuch, Technikansicht, Dialog und Zugänglichkeit | Kapitel 5 |
| Berufe und Arbeitsanimationen, Besucher, soziale Orte, Wege und Handel | Kapitel 6 |
| Fliegendes Leben, Habitate, Tierverhalten, Spuren, Wetter, Naturereignisse und Regionen | Kapitel 7 |
| Crimson-Desert-/ARK-Atmosphäre, Vogelschwärme, Entenfamilien und unabhängige Beobachtungsszenen | Kapitel 1, 7.1 und angepasste Etappe M1 |
| Entdeckungen, Bitten, eigene Vorhaben, alternative Lösungen und sichtbare Folgen | Kapitel 8 |
| Mit Bauen lösbare Dungeons, Vorbereitung, Außenposten und funktionale Bossrelikte | Kapitel 9 |
| Lesbarer Kampf, verschiedene Waffenrollen, Fähigkeiten auch außerhalb des Kampfes | Kapitel 10–11 |
| Seile, Klettern, Aufzüge, Seilbahnen, Boote, Fähren und Bahnhöfe | Kapitel 12 |
| Gärten, Bewässerung, Küche, Gerichte, Haustiere und Angeln | Kapitel 13 |
| Ausdrucksstarke Bewegung, Alltag, Partikel, Raumklang und Qualitätsregler | Kapitel 14 |
| Gleichwertige Fortschrittswege, Nether/End/Wasserideen, Museum und Weltchronik | Kapitel 15 |
| Gemeinsame Pläne, Vorräte, Rechte, Karten, Reisen und Geschichten | Kapitel 16 |
| Wegweiser, Instrumente, Einrichtung, Banner, Fotos und Besucherwege | Kapitel 17 |
| Gemütliche oder gefährlichere Welt, unabhängige Regler, faire Offlinezeit | Kapitel 18 |
| Erste vollständige Region mit Gasthaus, Brücke, Maschine, Abenteuer und Rückkehr | Kapitel 19–21 |
| Passende Skills und brauchbarer Codex-Einstieg | Kapitel 22–23 |
| Neu recherchierte Mod- und Community-Inspiration mit überprüfbaren Links | Kapitel 2, Quellen an den Ideen und Kapitel 24–25 |

**Erster Qualitätsmaßstab:** Der Spieler steht vor seiner selbst gebauten Mühle. Das Rad treibt eine echte Anlage an. Ein Lastenaufzug kommt an, am Ufer schwimmt eine Entenfamilie, über dem Tal zieht ein Vogelschwarm. Ein Reisender geht über die eigene Brücke und im Gasthaus entsteht ein neuer Erkundungsanlass. Die einzelnen Systeme ergeben zusammen eine verständliche, speicherbare und lebendige Minecraft-Welt.





## 24. Quellenmatrix

**Recherche und Abruf: 2. November 2026.** Die Matrix umfasst 42 verschiedene Mod-, Ressourcenpaket-, Werkzeug- und Modpack-Referenzen sowie zusätzlich zwei vom Nutzer genannte Spiele als Atmosphärenreferenzen. Ihre dokumentierten Funktionen dienen als Inspiration; die Übertragung auf dieses Projekt ist jeweils ein eigener Entwurf. Mehrere verlinkte Seiten desselben Projekts zählen nicht als weitere Projekte. Keine Zeile behauptet eine geprüfte gemeinsame Versions- oder Loaderkompatibilität, eine Installation im Nutzerprojekt oder eine Erlaubnis zur Übernahme fremder Inhalte.

### 24.1 Mechaniken, Technik und Bauen — 11 Projekte

| Referenz | Projekt und Quellentyp | Kurz belegter Ansatz | Eigene Übertragung und wichtige Grenze |
|---|---|---|---|
| [S-CREATE] | **Create** — Autoren-Repository | Animierte, kombinierbare Bauteile; Ponder erklärt Mechaniken visuell im Spiel. | Sichtbare Kraftübertragung und interaktive Miniaturversuche im Weltbuch. Eine Erklärszene verändert die echte Welt nicht. Eine mögliche Bibliotheksintegration wird separat geprüft. |
| [S-BOTANIA] | **Botania** — offizielles Lexikon | Naturmagie mit erzeugenden und funktionalen Pflanzen, Mana und Redstone-Verbindungen. | Funktionsgärten mit wenigen verständlichen Wirkungen und sichtbarer Reichweite. Normale Landwirtschaft braucht keine zusätzliche magische Währung. Das Online-Lexikon kann Entwicklungsständen voraus sein. |
| [S-ARS], [S-ARS-AUTOMATION] | **Ars Nouveau** — Spielbuch-Dokumentation | Glyphen ergeben gespeicherte Zauber; Helfer, Rituale und Geräte ermöglichen Automation. | Ein Relikteffekt funktioniert als Werkzeug und stationärer Baustein nach denselben Regeln. Wenige begrenzte Wirkungen zuerst; Navigationspunkte fremder Addons nicht als Kernfunktionen behandeln. |
| [S-PNEUMATIC] | **PneumaticCraft: Repressurized** — Autoren-Projektseite | Druckluft, druckabhängige Geräte, Sensoren und grafisch programmierbare Drohnen. | Füllstand, Druck und Blockadeursache klar anzeigen; kurze Steuerabläufe verständlich machen. Wasserhydraulik ist unsere eigene Ableitung. Überlast stoppt eine Anlage im Standardprofil nachvollziehbar. |
| [S-CC] | **CC: Tweaked** — offizielle Dokumentation | Programmierbare Computer und Turtles, Redstone, Monitore und Inventarschnittstellen. | Optionale Steuerpulte und später begrenzte Skripte; Namen für Ein- und Ausgänge. Grundmaschinen bleiben ohne Programmierung nutzbar. Rechte, Stoppschalter und Ausführungsbudget gehören zur Funktion. |
| [S-INTEGRATED] | **Integrated Dynamics** — Projektwebsite | Leser, Schreiber, Variablen und Operatoren verknüpfen Weltzustände. | Lesbare Steuerkarten mit sichtbaren Werten und Bedingungen. Die erste Ausbaustufe beschränkt sich auf wenige Regeltypen; fortgeschrittene Datenoperationen bleiben optional. |
| [S-ENGINEERING] | **Immersive Engineering** — Autoren-Projektseite | Räumliche Maschinen aus mehreren Blöcken, sichtbare Leitungen und ein Spielhandbuch. | Werkstätten mit erkennbarem Einlauf, Arbeitsbereich und Auslauf. Unsere Material- und Formensprache bleibt passend zur Fantasywelt; Montagevorschauen erklären fehlende Komponenten. |
| [S-AXIOM] | **Axiom** — Projektdokumentation | Werkzeuge für Auswahl, Verschieben, Klonen, Symmetrie, Paletten und Baupläne. | Gestaltungshilfen mit Vorschau und Materialbedarf im Survival, direkte Operationen im Kreativmodus. Funktionsinspiration bedeutet keine Übernahme des Codes oder automatische Berechtigung zu Weltänderungen. |
| [S-LITEMATICA] | **Litematica** — Autoren-Projektseite | Bauvorschau, Materialliste und Markierung abweichender Blöcke. | Gemeinsame Pläne zeigen fehlendes, vorhandenes, reserviertes und verbautes Material. Platzierung verbraucht reale Ressourcen. Eine spätere Installation verlangt eine gesonderte Prüfung der konkreten Datei. |
| [S-SUPPLEMENTARIES], [S-SUPPLEMENTARIES-FUNCTION] | **Supplementaries** — offizielles Wiki | Sanduhren liefern Zeitsignale; Tafeln zeigen Inhalte und reagieren auf Redstone. | Einrichtung wird Teil von Planung und Steuerung: Projekttafeln, Abfahrtsschilder und greifbare Zeitgeber. Viele kleine Funktionen brauchen eine gemeinsame, leicht verständliche Bedienung. |
| [S-QUARK] | **Quark** — Autoren-Projektseite | Viele kleine Ergänzungen sind einzeln schaltbar und teils weiter einstellbar. | Verständliche Weltprofile mit getrennten Einzelreglern. Beim Abschalten eines Moduls müssen gespeicherte Inhalte sinnvoll erhalten bleiben; ein ungeordneter Einstellungsbaum genügt nicht. |

### 24.2 Lebendige Welt, Bewohner und Erkundung — 12 Projekte

| Referenz | Projekt und Quellentyp | Kurz belegter Ansatz | Eigene Übertragung und wichtige Grenze |
|---|---|---|---|
| [S-MINECOLONIES] | **MineColonies** — offizielles Request-System | Lager, Handwerker und Kuriere bearbeiten Materialanforderungen. | Freie Spielergebäude erhalten nachvollziehbare Lieferaufträge mit Reservierung und sichtbaren Paketen. Private Kisten bleiben unberührt. Außerhalb geladener Bereiche genügen begrenzte gespeicherte Aufträge. |
| [S-MCA] | **MCA Reborn** — Autoren-Projektseite | Gespräche, Beziehungen, Familien und Dorfverwaltung mit individuellen NPCs. | Klassische Villager bekommen Namen, stabile Eigenschaften und ausgewählte Erinnerungen. Die menschenähnliche Darstellung und ein umfassendes Familiensystem sind keine Vorgabe für unser Projekt. |
| [S-NATURALIST] | **Naturalist** — Autoren-Projektseite | Wildtiere interagieren miteinander und mit ihrer Umgebung; einige sind zähmbar. | Lokale Habitate ermöglichen Trinken, Ruhen, Nahrungssuche und Rückzug. Keine vollständige globale Nahrungskette und keine tägliche Versorgungspflicht. Unterschiedliche Tierzahlen der Projektbeschreibung werden nicht übernommen. |
| [S-ALEX-MOBS] | **Alex’s Mobs** — Autoren-Projektseite | Kreaturen besitzen eigene Funktionen und Interaktionen; ein Tierlexikon begleitet sie. | Erinnerungswürdige Begegnungen und friedliche Wege zu besonderen Materialien. Ambient-Tiere dürfen allein Atmosphäre schaffen; nicht jede Art braucht eine verpflichtende Produktionskette. |
| [S-ALEX-CAVES] | **Alex’s Caves** — Autoren-Projektseite | Seltene Höhlenregionen verbinden eigene Kreaturen, Materialien, Mechaniken und Atmosphäre. | Fundstück, Forschung und Suchgebiet führen zu regionalen Systemen. Hinweise überbrücken Seltenheit; unabhängiges Entdecken bleibt möglich. Eine Region erhält ein stimmiges Gesamtpaket. |
| [S-ECOLOGICS] | **Ecologics** — Autoren-Projektseite | Kleine Biomerweiterungen verbinden Tiere, Pflanzen und nützliche Gegenstände. | Nistplätze, geschützter Nachwuchs und begrenzte tierische Gartenhilfe. Änderungen an Blöcken brauchen passende Naturflächen oder vom Spieler freigegebene Bereiche. |
| [S-TERRALITH] | **Terralith** — Autoren-Projektseite | Neue Landschaftsformen und Biome verwenden Vanilla-Blöcke. | Klare regionale Silhouetten und natürliche Verkehrsengstellen erzeugen Bauanlässe. Bestehende Weltbereiche werden nicht ungefragt neu generiert; größere Worldgen-Änderungen getrennt prüfen. |
| [S-YUNG] | **YUNG’s Better Dungeons** — Autoren-Projektseite | Unterschiedliche Dungeonformen, darunter Katakomben und Spinnenhöhlen. | Jeder Ort erhält eine räumliche Spielidee, erkennbare frühere Funktion und mehrere Zugänge. Bauen bleibt ein Lösungsweg. Strukturdichte muss Platz für eigene Vorhaben lassen. |
| [S-FTB] | **FTB Quests** — Autoren-Repository | Anpassbares Questsystem mit eigener Oberfläche und Unterstützung gemeinsamer Ziele. | Persönliche und gemeinsame Vorhaben mit klaren Zieltypen und Voraussetzungen. Besitz, Herstellung und Weltzustand getrennt behandeln; rückwirkende Anerkennung bewusst festlegen. |
| [S-BOUNTIFUL] | **Bountiful** — Autoren-Projektseite | Auftragsbretter und thematische Vorgaben steuern Aufgaben und Belohnungen. | Lokale Anliegen passen zu Werkstätten und Reisenden. Wiederholbare Aufträge ergänzen Geschichten mit sichtbaren Folgen; keine verpflichtende tägliche Aufgabenroutine. |
| [S-WEATHERING] | **Immersive Weathering** — Autoren-Projektseite | Umgebungsspuren wie Moos, Rost und Eiszapfen; Schutz und Pflege sind vorgesehen. | Patina, Säubern und Konservieren als bewusste Bauentscheidung. Standardmäßig keine schleichende funktionale Verschlechterung eigener Gebäude oder Arbeit wegen Offlinezeit. |
| [S-MCA-QUESTS] | **MCA: Quests** — Autoren-Projektseite | Datengetriebene Aufgaben können Beruf, Beziehungen, Umfeld und Dorfzustand berücksichtigen. | Handgestaltete Situationen werden passend zu Ort und Bewohner angeboten. Zustände und Folgen bleiben nachvollziehbar. Beschriebene Integrationen sind keine bereits ausgewählten Abhängigkeiten. |

### 24.3 Oberfläche, Animation, Atmosphäre und Kampf — 14 Projekte

| Referenz | Projekt und Quellentyp | Kurz belegter Ansatz | Eigene Übertragung und wichtige Grenze |
|---|---|---|---|
| [S-EMI] | **EMI** — Autoren-Projektseite | Rezepte, Suche, Favoriten und Rezeptbäume erklären Zutaten und Zwischenschritte. | Projektweite Materialplanung berücksichtigt Alternativen, Restprodukte und Reservierungen. Die gemeinsame Berechnung mehrerer Ziele ist unsere Ableitung aus einem Community-Wunsch, keine behauptete bestehende EMI-Kernfunktion. |
| [S-IPN] | **Inventory Profiles Next** — Projektwebsite | Sortierung, geschützte Plätze, gezieltes Einlagern und Werkzeugnachfüllen. | Weniger Handgriffe mit sichtbarer Rückmeldung und einstellbaren Komfortfunktionen. Persönliche Hotbar und fremde Vorräte werden nicht ungefragt verändert. |
| [S-MOUSE-TWEAKS] | **Mouse Tweaks** — Autoren-Projektseite | Erweiterte Ziehbedienung und Mengenverschiebung per Mausrad. | Häufige Inventaraktionen werden schnell und konsistent. Dieselben Funktionen erhalten zugängliche Alternativen für Controller, Touch und Bedienung ohne Ziehen. |
| [S-ATLAS] | **Antique Atlas** — Autoren-Repository | Kartenbuch mit Zoom, Verschieben und beschrifteten Markierungen. | Persönliches Weltbuch und Kartentisch unterscheiden Entdeckungen, Gerüchte und Notizen. Keine allwissende Anzeige ungesehener Dungeons; die Quelle belegt nicht automatisch Eigenschaften späterer Ports. |
| [S-FRESH] | **Fresh Animations** — Autoren-Projektseite eines Ressourcenpakets | Ausdrucksvollere Kreaturenbewegungen im vertrauten Minecraft-Erscheinungsbild. | Silhouette, Blick und Körperhaltung werden pro Kreatur gestaltet. Visuelle Referenz bedeutet keine Freigabe fremder Animationen und keine belegte Abdeckung aller Mod-Mobs. |
| [S-ACTIONS] | **Actions & Stuff** — Oreville-Studios-Website | Actions & Stuff verbindet Animationen, Partikel und Texturen. Das auf derselben Seite separat beschriebene **Villager News** bietet reaktive Dialoge sowie Ruf/Klatsch. | Zusammenhängende Bildsprache und passende Bewohnerreaktionen. Die beiden Produktbeschreibungen getrennt behandeln. Kein direkter Bedrock-zu-Java-Transfer der Assets. |
| [S-NEA] | **Not Enough Animations** — Autoren-Repository | Drittpersondarstellung von Aktionen wie Rudern, Karten-/Gerätebetrachtung und Klettern. | Andere Spieler erkennen Tätigkeiten am Körper. Gemeinsame Animationsprioritäten verhindern konkurrierende Posen; Ego- und Drittperson werden getrennt beurteilt. |
| [S-PARTICULAR] | **Particular** — Autoren-Projektseite | Kontextbezogene Spritzer, Wasserfalleffekte und weitere Umgebungspartikel. | Effekte reagieren auf Größe, Bewegung und Umgebung. Ambient-Schwärme benötigen nicht zwangsläufig voll simulierte Server-Entitäten; Entfernung und Budget begrenzen Darstellung. |
| [S-SOUND] | **Sound Physics Remastered** — Autoren-Repository | Schallabschwächung, Nachhall und Absorption durch Blöcke. | Räume und Maschinen erhalten akustische Identität. Wichtige Hinweise bleiben verständlich und auf Wunsch sichtbar; starke Hallwirkung ist einstellbar. |
| [S-BETTER-COMBAT] | **Better Combat** — Autoren-Repository | Waffenhaltungen, Ausholbewegungen, Animationen und datenbeschriebene Waffenattribute. | Gemeinsam abgestimmte Bewegungen, Reichweite und Trefferzeit. Bau- und Abbaubedienung bleiben nutzbar; Kampfsteuerung bekommt einen klaren Besitzer und separate Ego-Prüfung. |
| [S-DUNGEONS-MOBS] | **Dungeons Mobs** — Autoren-Projektseite | Dungeons-Kreaturen mit Spawns, Raid-Erweiterungen und passenden Begegnungsorten. | Figuren erhalten Lebensräume, Rollen und einen nachvollziehbaren Fertigstellungsstatus. Die Quelle beschreibt selbst unfertige Animationen und geplante Bosse; keine vollständige Abdeckung behaupten. |
| [S-CATACLYSM] | **L_Ender’s Cataclysm** — Autoren-Projektseite | Dungeons, besondere Bossangriffe, Phasen und Ausrüstung. | Bosszustände sind an Körper, Material und Verhalten lesbar. Die eigenen Cataclysm-Bosse sind keine Dungeons-Originalmodelle; für die Monstrosität bleibt die Nutzerreferenz maßgeblich. |
| [S-DUNGEONS-GEAR] | **Dungeons Gear** — Autoren-Projektseite | Dungeons-Ausrüstung, Artefakte und Verzauberungen mit Beute- und Handelszugängen. | Gegenstände an passende Regionen, Gegnerrollen und Berufe anbinden. Umfang, Modellqualität und tatsächliche Zielversion einzeln prüfen; die allgemeine Autorenbeschreibung ersetzt keine eigene Vollständigkeitsprüfung. |
| [S-GOETY] | **Goety – The Dark Arts** — Autoren-Projektseite | Seelen, Zauber, beschworene Helfer und magische Geräte. | Zusätzlicher Kontext für Artefakte, Begleiter und die Goety-Referenz des Nutzers. Verhalten dieser Mod und originale Dungeons-Fähigkeiten getrennt dokumentieren; konkrete Modelle und Nutzungsbedingungen einzeln prüfen. |

### 24.4 Alltag, Dokumentation und stimmige Gesamtgestaltung — 5 Projekte

| Referenz | Projekt und Quellentyp | Kurz belegter Ansatz | Eigene Übertragung und wichtige Grenze |
|---|---|---|---|
| [S-FARMERS] | **Farmer’s Delight** — Autoren-Projektseite | Landwirtschaft und Kochen werden durch zusammenhängende Tätigkeiten und Gegenstände erweitert. | Gärten, Küche und Gasthaus bilden einen sichtbaren Ablauf. Gerichte schaffen Vielfalt und freiwillige Vorteile; daraus entsteht keine neue Pflicht zur permanenten Ernährungsoptimierung. |
| [S-EXPOSURE] | **Exposure** — Autoren-Projektseite | Fotografie mit Kamera, Sucher, Film und Abzügen. | Naturtagebuch, Postkarten und Museum nutzen tatsächlich im Spiel aufgenommene Bilder. Fotos ergänzen Beobachtung und Erinnerung; sie sind als Spielaufnahmen klar von Konzeptbildern zu unterscheiden. |
| [S-BLISS] | **Bliss** — offizielle Modpack-Website | Zugängliche, ruhige Spielausrichtung; selbst gewählte Kämpfe, Anleitung im Spiel und situationsbezogene Musik. | Ein ruhiges Weltprofil lässt Aufbau, Atmosphäre und Entdeckung tragen. Kämpfe bleiben bewusst zugänglich. Übernommen wird eine Gestaltungsrichtung, nicht ungeprüft das gesamte Modpaket. |
| [S-CRUCIAL] | **Crucial 2** — Autoren-Modpackseite | Kuratierte, aufeinander abgestimmte Ergänzungen und Entdeckung über Hinweise und Gerüchte. | Eine verbindliche Gestaltungssprache und Begegnungen führen zu Möglichkeiten. Quellen werden nach ihrem Beitrag zum Gesamtspiel ausgewählt; Funktionsmenge allein entscheidet nicht. |
| [S-LIVE-VILLAGES] | **Live Villages** — Autoren-Projektseite | Der Autor beschreibt Handelsbretter, stufenweisen Bau und unterschiedliche Simulation geladener beziehungsweise entfernter Bewohner. | Ortszustände und Lieferaufträge können im Hintergrund vereinfacht fortgeführt werden. Das Projekt ist ausdrücklich in aktiver Entwicklung; die Autorenangaben wurden nicht unabhängig im Spiel getestet. |

### 24.5 Konkrete Community-Beiträge

Die folgenden Beiträge sind einzelne Wünsche, Erfahrungsberichte und Dialoge. Sie liefern Entwurfsfragen, keinen gemessenen Mehrheitskonsens. Ein geschlossener historischer Vorgang ist kein Beleg für einen aktuellen Fehler.

| Referenz | Beitrag | Belegter Anlass | Konsequenz für unseren Entwurf |
|---|---|---|---|
| [C-PLAN] | **Litematica #478:** Materiallisten für Teilbereiche | Ein Spieler wollte gemeinsam bauen, ohne Material anderer Abschnitte zu verbrauchen. Der Maintainer erklärte bereits vorhandene Bedienwege; deren Auffindbarkeit war das Problem. | Teilabschnitte, benötigte Mengen und Reservierungen direkt im Bauplan zugänglich machen. Nicht als fehlende aktuelle Litematica-Funktion darstellen. |
| [C-PONDER] | **Create #4661:** Ponder als eigene Bibliothek | Historischer Wunsch nach räumlichen Erklärungen auch außerhalb von Create; zudem Sorge vor der Verdrängung interessanter Anlagen durch Universalmaschinen. | Anschauliches Lernen von Anfang an planen und sichtbare Konstruktionen spielerisch wertvoll halten. Keine Aussage zum aktuellen Bibliotheksstatus daraus ableiten. |
| [C-TRAIN] | **r/CreateMod:** „Why should you build a train?“ | Der Ersteller suchte neben Bauanleitungen einen praktischen Nutzen für Züge und Gütertransport zwischen Orten. | Regionale Materialien, verteilte Werkstätten, Reisende und Aufträge geben Verbindungen einen Zweck. Lokale Grundversorgung bleibt möglich. |
| [C-EMI-PLAN] | **EMI #1041:** mehrere Rezeptbäume zusammenführen | Der Vorschlag verbindet gleichzeitig geplante Rezepte und gemeinsam benötigte Zwischenprodukte. | Projektweite Berechnung mehrerer Ziele mit Rezeptalternativen und Reservierungen. Nicht als bereits ausgelieferte EMI-Kernfunktion bezeichnen. |
| [C-EMI-HIDE] | **EMI #689:** Schaltflächen ausblenden oder verschieben | Nutzerwunsch nach zurückhaltender Oberfläche; der Autor ergänzte Sichtbarkeitsoptionen und schloss den Vorgang. | Hilfen und erweiterte Planung bleiben erreichbar, dürfen aber ausgeblendet werden. Kein aktuelles generelles UI-Problem behaupten. |
| [C-FOV] | **Better Combat #354:** Darstellung abhängig von Blickrichtung | Ein Nutzer beschrieb Probleme mit Arm-/Waffenansicht bei verschiedenen Blickwinkeln und höherem Sichtfeld. | Ego-Ansicht gesondert bei mehreren Sichtfeldern und Blickrichtungen prüfen. Der Beitrag ist eine konkrete Designlektion, kein pauschales Urteil über heutige Versionen. |
| [C-QUEST] | **FTB-Modpack-Issues #6765:** nicht erkannte Questgegenstände | Ein Spieler vermutete zu frühes Herstellen; die Antwort verwies auf Besitzaufgaben und mögliche fehlende Voraussetzungen. | Zieltyp, Voraussetzung und anerkannte Beiträge explizit anzeigen. Bereits erfüllte Weltzustände berücksichtigen; Belohnungen eindeutig und nur einmal vergeben. |
| [C-VILLAGE] | **Minecraft Feedback:** „A Living Village Region System“ | Community-Vorschlag für lebendige Dörfer als regional zusammenhängendes System. | Gebäude, Wege und Aktivitäten gemeinsam entwerfen. Der Vorschlag ist weder eine offizielle Ankündigung noch ein Beleg für implementierte Funktionen. |

### 24.6 Zusätzlich genannte Atmosphärenreferenzen — 2 Spiele

| Referenz | Spiel und Primärquelle | Belegter Bezug | Eigene Übertragung |
| --- | --- | --- | --- |
| [S-GAME-CRIMSON] | **Crimson Desert** — Pearl Abyss, „Das Leben in Pywel“ | Der offizielle Begleittext stellt Alltag und Aktivitäten abseits von Quests und Kämpfen vor. | Der Nutzer schätzt die lebendige Atmosphäre. Unser Minecraft bekommt beobachtbare Alltagsabläufe, kleine Begegnungen und ruhige Momente mit eigenständigem Leben. Die konkret entworfenen Tierszenen sind eigene Vorschläge. |
| [S-GAME-ARK] | **ARK: Survival Evolved** — offizielle Steam-Produktbeschreibung | Beschreibt vielfältige Kreaturen, Räuber-/Beutebeziehungen, Zähmung und Nachwuchs. | Tiere wirken durch Verhalten, Gruppenbezug, Größe und Umgebung als Teil der Welt. Für den gewünschten Minecraft-Alltag Familien- und Herdenszenen priorisieren; umfassende Zuchtverwaltung und ARKs übrige Systeme sind keine Vorgabe. |

## 25. Quellenverweise

Die Referenzen bleiben in einer einzelnen Markdown-Datei nutzbar. Vor einer tatsächlichen Integration jeweils aktuelle Projektdatei, unterstützte Plattform, Abhängigkeiten, Lizenz und vorhandene Umsetzung im Nutzer-Repository prüfen.

[S-CREATE]: https://github.com/Creators-of-Create/Create
[S-BOTANIA]: https://botaniamod.net/lexicon.html
[S-ARS]: https://ars.guide/book/getting_started/introduction_to_spellcrafting/
[S-ARS-AUTOMATION]: https://ars.guide/book/getting_started/magical_automation/
[S-PNEUMATIC]: https://www.curseforge.com/minecraft/mc-mods/pneumaticcraft-repressurized
[S-CC]: https://tweaked.cc/
[S-INTEGRATED]: https://integrateddynamics.rubensworks.net/
[S-ENGINEERING]: https://www.curseforge.com/minecraft/mc-mods/immersive-engineering
[S-AXIOM]: https://axiomdocs.moulberry.com/
[S-LITEMATICA]: https://www.curseforge.com/minecraft/mc-mods/litematica
[S-SUPPLEMENTARIES]: https://github.com/MehVahdJukaar/Supplementaries/wiki/Redstone-Blocks
[S-SUPPLEMENTARIES-FUNCTION]: https://github.com/MehVahdJukaar/Supplementaries/wiki/Functional-Blocks
[S-QUARK]: https://www.curseforge.com/minecraft/mc-mods/quark
[S-MINECOLONIES]: https://minecolonies.com/wiki/systems/request/
[S-MCA]: https://modrinth.com/mod/minecraft-comes-alive-reborn
[S-NATURALIST]: https://modrinth.com/mod/naturalist
[S-ALEX-MOBS]: https://www.curseforge.com/minecraft/mc-mods/alexs-mobs
[S-ALEX-CAVES]: https://modrinth.com/mod/alexs-caves
[S-ECOLOGICS]: https://modrinth.com/mod/Ecologics
[S-TERRALITH]: https://modrinth.com/datapack/terralith
[S-YUNG]: https://modrinth.com/mod/yungs-better-dungeons
[S-FTB]: https://github.com/FTBTeam/FTB-Quests
[S-BOUNTIFUL]: https://modrinth.com/mod/bountiful
[S-WEATHERING]: https://modrinth.com/mod/immersive-weathering
[S-MCA-QUESTS]: https://modrinth.com/mod/mca-quests
[S-EMI]: https://www.curseforge.com/minecraft/mc-mods/emi
[S-IPN]: https://inventory-profiles-next.github.io/
[S-MOUSE-TWEAKS]: https://www.curseforge.com/minecraft/mc-mods/mouse-tweaks
[S-ATLAS]: https://github.com/AntiqueAtlasTeam/AntiqueAtlas
[S-FRESH]: https://www.curseforge.com/minecraft/texture-packs/fresh-animations
[S-ACTIONS]: https://www.orevillestudios.com/
[S-NEA]: https://github.com/tr7zw/NotEnoughAnimations
[S-PARTICULAR]: https://modrinth.com/mod/particular
[S-SOUND]: https://github.com/henkelmax/sound-physics-remastered
[S-BETTER-COMBAT]: https://github.com/ZsoltMolnarrr/BetterCombat
[S-DUNGEONS-MOBS]: https://www.curseforge.com/minecraft/mc-mods/dungeons-mobs
[S-CATACLYSM]: https://www.curseforge.com/minecraft/mc-mods/lendercataclysm
[S-DUNGEONS-GEAR]: https://www.curseforge.com/minecraft/mc-mods/dungeons-gear
[S-GOETY]: https://www.curseforge.com/minecraft/mc-mods/goety
[S-FARMERS]: https://www.curseforge.com/minecraft/mc-mods/farmers-delight
[S-EXPOSURE]: https://modrinth.com/mod/exposure
[S-BLISS]: https://bliss.violetmoon.org/
[S-CRUCIAL]: https://www.curseforge.com/minecraft/modpacks/crucial-2
[S-LIVE-VILLAGES]: https://modrinth.com/mod/live-villages
[S-GAME-CRIMSON]: https://crimsondesert.pearlabyss.com/de-DE/News/Notice/Detail?_boardNo=59
[S-GAME-ARK]: https://store.steampowered.com/app/346110/ARK_Survival_Evolved/?l=english
[C-PLAN]: https://github.com/maruohon/litematica/issues/478
[C-PONDER]: https://github.com/Creators-of-Create/Create/issues/4661
[C-TRAIN]: https://www.reddit.com/r/CreateMod/comments/1c8ki3p/why_should_you_build_a_train/
[C-EMI-PLAN]: https://github.com/emilyploszaj/emi/issues/1041
[C-EMI-HIDE]: https://github.com/emilyploszaj/emi/issues/689
[C-FOV]: https://github.com/ZsoltMolnarrr/BetterCombat/issues/354
[C-QUEST]: https://github.com/FTBTeam/FTB-Modpack-Issues/issues/6765
[C-VILLAGE]: https://feedback.minecraft.net/hc/en-us/community/posts/49196137058829-A-Living-Village-Region-System
