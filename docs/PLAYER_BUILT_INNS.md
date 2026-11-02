# Frei gebaute Gasthäuser

Du baust das Gebäude und seine Wege selbst. Ein **Gasthauspult** markiert den Besitzer und die Funktion; ein **Gasthausplan** weist den Bereich, Zugang, Vorrat und Ankunftspunkt zu. Das System baut nichts für dich und verändert keine Landschaft. Es prüft eine konkrete Gasthausfunktion. Werkstatt, Bibliothek, Markt, Garten und Hafen aus `BUILD-01` sind damit noch nicht umgesetzt.

## Herstellen und markieren

Beide Rezepte sind ungeordnet:

| Zutaten | Ergebnis |
| --- | --- |
| Ein Lesepult + ein Smaragd | Ein Gasthauspult (`dungeonsalive:inn_ledger`) |
| Ein Papier + eine Holzkohle | Ein Gasthausplan (`dungeonsalive:inn_marker`) |

1. Baue einen trockenen Bereich mit Sitzbank, Bett, Vorratsbehälter und einem begehbaren Zugang. Platziere darin das Gasthauspult. Der tatsächliche platzierende Spieler wird sein Besitzer.
2. Benutze den Gasthausplan am eigenen Pult. Danach folgen **fünf Markierungen** in dieser Reihenfolge: erste Raumecke am Boden; gegenüberliegende Ecke auf Deckenhöhe; Eingang; Vorrat; Ankunftspunkt.
3. Für Eingang und Ankunftspunkt klicke auf die **Oberseite des Bodens**. Gewählt wird der freie Standplatz darüber. Beim Vorrat wählst du direkt die einzelne Truhe oder das Fass.
4. Kehre zum Pult zurück, prüfe die angezeigten Positionen und wähle **„Bereich bestätigen“**. Erst diese Bestätigung weist dem Gasthaus seinen tatsächlichen Vorrat zu. Das Menü zeigt anschließend Nutzbarkeit, Möbel, Nahrung, Verbindung und Gaststatus.

Die beiden Ecken begrenzen den Bereich einschließlich ihrer Blöcke, höchstens **32 × 16 × 32**. Pult, Eingang, Sitz, vollständiges Bett und Vorrat müssen darin liegen. Möbel und ihre Standplätze brauchen auch den nötigen freien Raum darüber innerhalb der Markierung. Der Ankunftspunkt liegt **außerhalb** des Bereichs, **4 bis 24 Blöcke** vom Eingang entfernt. Alle Markierungen bleiben lokal und müssen bereits geladen sein.

Solange du den Plan hältst, zeigen private Markierungspunkte die gewählten Grenzen; sie platzieren keine Blöcke. **Schleichen + Plan am Pult** beginnt eine neue Auswahl. „Auswahl verwerfen“ verwirft den aktuellen Entwurf. Die Auswahl ist an Spieler, Dimension und diese konkrete Pultplatzierung gebunden. Sie verfällt nach 24.000 Weltticks ohne weitere Markierung, beim Verlassen des Servers oder beim Ersetzen des Pults. Eine alte Menüansicht kann keine inzwischen geänderte Konfiguration überschreiben. Während ein Gast reserviert ist, muss dessen Besuch beziehungsweise Abreise vor einer Neukonfiguration enden.

## Was als benutzbar zählt

**Sitzbank:** Eine trockene **untere Holzstufe** ist die derzeit unterstützte Sitzfläche. Obere und doppelte Stufen, Treppenblöcke und beliebiger Boden sind keine Sitzplätze. Daneben muss ein erreichbarer, trockener Standplatz mit tragfähigem Boden und genug Platz für den Händlerkörper frei bleiben. Der Gast muss dort tatsächlich ankommen, bevor er sitzt. Es gibt noch keine allgemeine Spieler-Sitzinteraktion, Tischarbeit oder das Abstellen beliebiger Gegenstände.

**Bett:** Beide zusammengehörigen Betthälften müssen im Bereich liegen, darüber muss Platz frei sein. Ein erreichbarer Stand- und Ausstiegsplatz daneben ist nötig. Ein fremder Schläfer wird nicht verdrängt. Der Gast benutzt nachts ein Bett; tagsüber ruht er auf einem Sitz. Bei blockiertem sicherem Ausstieg wartet er, statt sich durch einen Block zu versetzen.

**Vorrat:** Unterstützt sind ein **Fass oder eine einzelne normale Truhe**. Doppeltruhen, Redstone-Truhen, Shulkerkisten und unbekannte Container sind hier noch keine Vorräte. Beim Einrichten muss der Besitzer auch den tatsächlichen Vanilla-Zugang zum Behälter haben. Eine bestätigte Zuordnung wird im konkreten BlockEntity gespeichert und über normales Speichern/Laden erhalten. Wird der Behälter abgebaut und eine gleichartige private Kiste an derselben Stelle platziert, erhält sie diese Berechtigung **nicht**. Das Pult meldet fehlenden Vorrat; der neue Behälter muss ausdrücklich neu bestätigt werden. Nachbarkisten bleiben unverändert.

Als Mahlzeit zählen Brot, Apfel, Karotte, Ofenkartoffel, Rote Bete sowie gebratenes Rindfleisch, Schweinefleisch, Hammelfleisch, Hühnchen, Kaninchen, Kabeljau und Lachs. Goldnahrung, verdorbene oder giftige Nahrung, Tränke und Gerichte mit Behälterrest werden nicht verbraucht. Ein Gast nimmt nach wirklicher Sitznutzung **genau eine Portion** aus dem zugewiesenen Vorrat. Die gespeicherte Besuchsphase verhindert einen erneuten Verbrauch beim normalen Reload.

**Wege und Türen:** Der Gast braucht einen echten Bodenpfad vom Ankunftspunkt zum Eingang und von dort zu Sitz und Bett. Eine selbst gebaute Brücke kann Teil dieses Pfades sein. Die Prüfung verändert dafür keine Blöcke. Nur gewöhnliche, von Hand öffnbare **Vanilla-Holztüren mit beiden Hälften im eigenen markierten Bereich** darf der Gast öffnen und anschließend schließen. Eisentüren, Key Gates und Türen außerhalb dieses Bereichs werden nicht durch die Gasthausroutine betätigt. Eine durch Redstone offen gehaltene Tür wird nicht von ihr geschlossen. Andere bereits offene und tatsächlich begehbare Zugänge bleiben normale Minecraft-Wege.

Das Menü nennt einen konkreten Fehlerort nur, wenn er bekannt ist. Bei einem vorher bestätigten und dann unterbrochenen Brückenpfad kann das der erste ungültig gewordene Wegpunkt sein. Wenn noch nie ein Weg gefunden wurde, wird kein vermeintlich erstes Hindernis erfunden. Die Prüfung verspricht nicht jede Treppen-, Baumhaus-, Schiffs- oder Sonderkonstruktion als bereits getestet.

## Besuch, Unterbrechung und Abbau

Ist das Gasthaus funktional, sind die benötigten Chunks geladen und ist ein Spieler in der Nähe, kann am markierten Ankunftspunkt ein begrenzter echter Reisender entstehen. Er benutzt die vorhandene Händlerfigur mit einer eigenen gespeicherten Besucherrolle. Er läuft selbst zum Eingang, nimmt einen Sitz, isst, ruht und läuft zum Ankunftspunkt zurück. Dort endet dieser begrenzte Besuch. Es handelt sich nicht um einen bereits belegten weltweiten Reiseverkehr zwischen beliebigen Dörfern.

Die Sitzphase vor der Mahlzeit zählt 120 tatsächliche Nutzungsticks, die anschließende Ruhe 200. Handel und die vorhandene Schadensflucht können den Ablauf unterbrechen; dabei wird nur die eigene Möbelbindung freigegeben. Schaden startet keinen Gegenangriff. Ein blockierter Weg führt zu Warten und erneuter begrenzter Prüfung. Der Gast repariert keine Brücke und wird nicht an Hindernissen vorbeiteleportiert.

Gast-UUID, Konfigurationsrevision, Phase, Ankunft und Besuchspause werden serverseitig gespeichert. Ein Sitz speichert seine tatsächliche Gastbindung; beim Laden müssen Sitz beziehungsweise Bett und ihre Berechtigung erneut stimmen. **Eine ungeladene Gast-UUID ist kein freier Platz** und führt nicht zu einem Ersatzspawn.

Du kannst das Pult abbauen. Ein laufender Besuch wird dadurch stillgelegt und auf Abreise gesetzt, ohne den Abbau zu verbieten. Ein geladener Gast steht sicher auf und läuft hinaus; ein ungeladener Gast behält seine Reservierung bis zur Rückkehr. Seine Abreise benötigt das entfernte Pult nicht mehr. Erst sein tatsächliches Ende entfernt den stillgelegten Datensatz. Der ehemalige Raum kann wieder markiert werden; der alte Gast zählt bis zu seinem Ende weiterhin gegen die Grenzen. Sichere Möbelaufstiege/-ausstiege sind lokale Möbelbindungen, kein Ersatz für die tatsächlichen Wege.

## Freiwillige Quest

**„Ein Haus für Reisende“** (`dungeonsalive:a_welcoming_inn`) wird ausdrücklich im Journal angenommen. Sie prüft drei eigenständige Weltzustände: ein eigenes benutzbares Gasthaus, dessen tatsächliche Verbindung und eine echte Ankunft eines eigenen Gastes. Eine schon vorhandene passende Inn und eine bereits gespeicherte Ankunft können zählen.

Beim bewussten Abholen werden die aktuellen Raum- und Wegbedingungen erneut auf dem Server geprüft. Eine inzwischen zerstörte Brücke kann die Abholung wieder sperren. Die tatsächlich erfolgte Ankunft bleibt als historischer Nachweis erhalten, auch nach Entfernung der alten Inn. Die Belohnung beträgt **vier Laternen, sechs Smaragde und 40 Erfahrungspunkte**, einmalig. Sie startet keinen Nachfolger automatisch. Das Gasthaus funktioniert auch ohne Annahme dieser Quest.

## Grenzen und Prüfstand

Es gelten höchstens **64 Gasthausdatensätze weltweit**, drei pro Besitzer, ein reservierter Gast pro Gasthaus und acht Gäste weltweit. Ungeladene und noch abreisende stillgelegte Gäste zählen mit. Am Entstehungsort gilt zusätzlich eine lokale Grenze von sechs lebenden Händlerfiguren innerhalb des geprüften Umfelds. Nach einer normalen Abreise folgt eine gespeicherte Pause von **6.000 Weltticks**. Das historische Ankunftsregister enthält höchstens 4.096 Besitzer; volle Register werden nicht durch Löschen alter Nachweise erweitert.

Die Auswahl wird getrennt je Dimension fortgesetzt; eine andere Dimension überspringt keine hiesigen Gasthäuser. Der Service prüft höchstens zwei dortige Datensätze je 100 Ticks. Der tatsächliche Vanilla-Pfadsuchbereich von **42 Blöcken** und die Ziele müssen bereits geladen sein. Weder Gäste noch Diagnose laden unbekannte Chunks. Möbelscans sind begrenzt; „Prüfung läuft“ ist ein echter Zwischenstand. Eine Fertigmeldung entsteht nicht aus Clientdaten.

**Technischer Nachweis:** Der vollständige Java-21-/Minecraft-/NeoForge-Typcheck mit 457 Main- und 40 Testquellen sowie **170/170 JUnit-Tests** sind bestanden. Im gemeinsamen nativen Lauf [`run-mojmtf12`](../build/local-gametest/run-mojmtf12/result.json) bestanden **138/138 GameTests**, darunter **alle neun Gasthausfälle**, auf JAR-SHA `2566768d4c8e913c19c52bb08505caf995b931d251e082f869fad486ab45f2db`. Dazu gehören der tatsächliche Gesamtzyklus mit Tür, Sitz-Reproduktion aus NBT, einer Mahlzeit und Nachtbett, Rechte-/Ersatzvorrat-/Brückenbruch-/Abbaurückweg, Routenprüfung, Auswahlrechte und der echte Questadapter samt einmaliger Abholung. Auch die drei separaten Möbel-Fixtures bestehen nach der Korrektur ihrer ungenauen Weganforderung auf einen exakten Kontaktpfad und des unnötigen Posewechsels bei bereits entfernten Schläfern.

Die ältere Aquatik-Fixture wartet nun auf tatsächlich gemessenes SKY-/BLOCK-Licht 0, bevor sie ihr unverändertes natürliches Spawnprädikat genau einmal prüft; auch dieser Fall bestand. Die vorherigen fehlgeschlagenen Läufe bleiben als historische Befunde erhalten. Der aktuelle Gesamtbeleg mit ihren Verweisen liegt unter [`reports/inn-integrated-validation.json`](../reports/inn-integrated-validation.json). Dies sind echte direkte Java-/NeoForge-Prüfungen, kein erfolgreicher Gradle-Lauf oder Release-Nachweis.

Die **grafische Gasthausabnahme mit wirklicher Menübedienung und sichtbarem Gästeablauf ist weiterhin offen**. Dafür ist das getrennte, frisch markierte Profil **Dungeons Alive – Inn QA (Beobachtung)** mit exakt dieser geprüften JAR vorbereitet; bestehende Benutzerwelten bleiben unberührt. Integrationsdateien und Herkunft stehen in [`reports/inn-integration.json`](../reports/inn-integration.json).

Der Stand ist ein konkreter Teil von `BUILD-01`, `BUILD-02`, `BUILD-11`, `VILL-07`, `VILL-12`, `QUEST-08` und `QUEST-09`. Er ist kein Abschluss aller Funktionsräume, aller Einrichtungsinteraktionen oder von M1 beziehungsweise der vollständigen [verbindlichen Nutzervorgabe](../MINECRAFT_2_IDEEN_FUER_CODEX.md).
