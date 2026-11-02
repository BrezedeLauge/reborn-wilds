# Entenfamilien – M1 / WORLD-18

Der aktuelle Entwicklungsstand erweitert die vorhandene ID `dungeonsalive:pet_duck`. Er verwendet das bereits auditierte ReCrafted-Entenmodell und dessen ausgewählte Textur; es entsteht keine zweite Entenregistrierung. Die [verbindliche Nutzervorgabe](../MINECRAFT_2_IDEEN_FUER_CODEX.md) verlangt einen vollständigen beobachtbaren Ablauf. Dieser Text beschreibt den Code, nicht bereits eine bestandene Gesamt- oder Sichtabnahme.

## Familie und Besitz

Der begrenzte Habitatmanager kann an tatsächlich vorhandenem Oberflächenwasser in Flüssen, Sümpfen und geeigneten Teichen eine Familie aus **einem Erwachsenen und zwei bis vier Jungen** erzeugen. Eine verbleibende Kapazität von nur zwei Tieren erzeugt keine Teilfamilie; unvollständige Zulassungen werden entfernt. Die vorhandenen globalen und lokalen Habitatlimits bleiben maßgeblich. Es gibt keine nachträgliche automatische Erzeugung weiterer Jungtiere.

Der Manager verwendet `MobSpawnType.NATURAL` und gemeinsames `SpawnGroupData`. Die bestehende Provenienztrennung bleibt erhalten: manuell erzeugte, ältere oder bereits besessene Enten werden nicht durch ihre Registry-ID in eine Familie umgewandelt. Der erste erfolgreiche natürliche Packeintrag ist erwachsen; die folgenden Einträge speichern seine UUID als Elternteil. Gruppen-ID, Mitgliedsroster, gemeinsamer Anker, Alter, Eltern-ID, Aktivität und Phasenfortschritt werden gespeichert. Die Familie ist persistent; ungeladene Mitglieder werden weder gesucht noch ersetzt. Bei lokal fehlendem Elternteil können die übrigen geladenen Mitglieder mit einem stabilen lokalen Anführer weiterleben, ohne ihre gespeicherte Eltern-ID umzuschreiben.

Junge haben **55 Prozent** der normalen Körper- und Modellgröße. Die Größe kommt einmalig aus einem vorübergehenden `SCALE`-Attributmodifikator; der zusätzliche Vanilla-Altersfaktor ist für diese Spezies ausgeschaltet. Das gespeicherte Jugendalter beträgt anfangs 24.000 tatsächlich verarbeitete KI-Ticks. Ungeladene Zeit beschleunigt es nicht. Ein bestehender Besitzer behält die normalen Folgen-/Warten-Rechte. Wird eine natürliche Ente angenommen, endet ihre Familienrolle; eine vorhandene Jugendgröße kann weiter bis zum Erwachsenwerden bestehen.

## Echte Tätigkeit und Navigation

Der Erwachsene führt `SWIM → TO_SHORE → FORAGE → PREEN → REST → TO_WATER → SWIM` aus. Die Jungen folgen über wirkliche Pfade. Liegt ein geladenes Mitglied mehr als vier Blöcke zurück, wartet der Erwachsene. Uferaktivitäten schreiten erst fort, wenn die geladenen Mitglieder tatsächlich außerhalb des Wassers Bodenkontakt haben; ein noch aussteigendes Junges bleibt sichtbar in `TO_SHORE`. Die Schwimmphase wartet auf Wasserteilnahme.

`SWIM` fährt gemeinsame, tatsächlich wasserbedeckte Wegpunkte bevorzugt zwei bis fünf Blöcke entfernt an. Die Jungen steuern denselben kurzen Wasserkorridor an; der Erwachsene wartet an dessen Ende auf sie. Nach drei gemeinsam erreichten Abschnitten läuft die Mindestdauer von 120 passenden Ticks weiter. Ein kleiner Teich erlaubt kürzere Abschnitte. Fehlen erreichbare Abschnitte, gibt die Routine nach höchstens 480 aktiven Such-/Schwimmticks den normalen Uferwechsel frei, weiterhin nur mit naher Familie im Wasser. Dadurch sperrt eine unerreichbare Mindeststrecke den Landgang nicht endlos. Ein Fehlversuch setzt einen endlichen Backoff, dessen Frist während des Wartens nicht ständig verlängert wird.

Fressen dauert mindestens 80, Gefiederpflege 60 und Ruhe 140 passende Ticks. Die Animation folgt diesen serverseitigen Zuständen. Es werden dafür keine Nahrungspflanzen abgebaut, keine Items erfunden und keine Belohnungen vergeben.

Eine echte Verletzung löst den vorhandenen Gruppenalarm aus. Zwölf Ticks `ALERT` gehen in begrenztes `FLEE` über; nach Ende des Alarms kehrt die Familie je nach tatsächlichem Medium zum Schwimmen beziehungsweise zum Wasserweg zurück. Junge behalten ihre Identität. Die Phase wird nicht durch eine Quest oder eine Kamera ausgelöst.

Die Amphibiennavigation prüft das Ziel und den **gesamten vanilla Pfadsuchbereich innerhalb 42 Blöcken** auf bereits geladene Chunks. Ziel und Habitat bleiben höchstens 24 Blöcke vom gespeicherten Anker entfernt. Wasserpfade bleiben an der Oberfläche; Weltgrenze, Körperkollision und gefährliche Blöcke werden geprüft. Eigene Oberflächenphysik verändert die Geschwindigkeit und lässt die Ente wirklich schwimmen und an einem erreichbaren flachen Ufer herausgehen. Sie setzt keine Position und verändert keine Blöcke. Ein gesperrter oder trockengelegter Einstieg wird neu bewertet; nach wiederholtem echten Pfadfehler wird ein anderer lokaler Uferpunkt gesucht.

Kontaktlaute entstehen sparsam beim erwachsenen Anführer, nicht bei jedem Mitglied in jedem Tick. Der eigene synthetische Klang und die Client-Posenergänzung sind getrennt von den Community-Modellquellen dokumentiert.

## Schnittstellen und Prüfung

`DungeonsCreature` bietet `duck()`, die synchronisierten Getter `duckFamily()`, `duckYoung()`, `duckActivity()` und `duckActivityTicks(partialTick)`. Clientdarstellung verwendet **`duck() && duckFamily()`**, damit die Familienposen alte/manuelle Haustiere nicht übernehmen. `DuckFamilyState.Activity` enthält `SWIM`, `TO_SHORE`, `FORAGE`, `PREEN`, `REST`, `TO_WATER`, `ALERT` und `FLEE`. Der Client entscheidet weder über Aktivitätswechsel noch Bewegung, Alter oder Zugehörigkeit.

Der vollständige Minecraft-/NeoForge-Typcheck und **148 JUnit-Fälle** sind bestanden, darunter vier `DuckFamilyStateTest`- und zwei `DuckSwimProgressTest`-Fälle. Alle **vier** nativen `DuckFamilyGameTests` bestehen im echten Serverlauf [`run-vgxtjclp`](../build/local-gametest/run-vgxtjclp/result.json) mit JAR-SHA `3b660a71b39d9ef766408b32e1a4be3f52e86fe5f8b230fe75c2397f2e77e306`. Der gesamte Lauf besteht 128 von 129 Fällen; offen ist ein Geier-Kontaktzustand. Dieser Duck-Nachweis ist keine Gesamt- oder Sichtfreigabe. Geprüfte Fälle:

- Passiver vollständiger Zyklus mit tatsächlichem Pfad, eigener horizontaler Schwimmstrecke und trockenem Bodenkontakt für alle drei Mitglieder; keine Aktivitätssetter oder Bewegung nach dem Aufbau.
- Vollständiger passiver Zyklus in einem echten **2×2-Teich**, mit eigenen Wasser-/Landpfaden und anschließender Rückkehr aller Mitglieder, ohne unmögliche Mindeststreckenquote, Teleport oder Geländeänderung.
- Akzeptierter Alarm, vollständiges Jungtier-NBT mit gleicher Identität/Elternbindung, endliche Erholung und ein während trockener Ruhe tatsächlich geänderter Wassereinstieg.
- Reale Familienzulassung mit drei Plätzen, Ablehnung einer Zweierkapazität, unveränderte manuelle Besitzerinteraktion und Ablehnung unbekannter Zielchunks ohne Nachladen.

Ein eigener echter Clientablauf soll die gesamte Bewegung bei stillstehendem Spieler zeigen. Nach einem tatsächlich erzeugten Nature-Lauf erstellt `python3 tools/build_nature_gallery.py --run <Nature-Laufordner>` eine prüfbare Galerie aus unveränderten Original-PNGs mit Tickabständen, Zuständen und Hashnachweisen. Das Werkzeug erfindet keine Bilder und macht aus einem fehlgeschlagenen Lauf keine Abnahme. Einzelbilder, Typcheck oder reine Phasentests sind dafür kein Ersatz. WORLD-18 und M1 bleiben bis zum passenden nativen und visuellen Nachweis ausdrücklich unvollständig; auch damit sind die späteren Gasthaus-, Brücken-, Besucher- und Maschinenetappen noch nicht erledigt.

Quellen: `entity/peaceful/DuckFamilyState.java`, `DuckSwimProgress.java`, `DuckFamilyGoal.java`, `DuckFamilyNavigation.java`, `DuckHabitat.java`, `DungeonsCreature.java`, `WildlifeHabitats.java` und `WildlifeEncounters.java`. Aktuelle artefaktbezogene Ergebnisse gehören nach [VALIDATION.md](VALIDATION.md), nicht aus älteren Baselines in diesen neuen Abschnitt übernommen.
