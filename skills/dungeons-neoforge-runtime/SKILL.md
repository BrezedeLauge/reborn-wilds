---
name: dungeons-neoforge-runtime
description: Entwickle und prüfe die NeoForge-Spielmechanik von Dungeons Alive, einschließlich Entities, KI, Items, Quests, Handel, Persistenz und Serversynchronisation.
---

# Dungeons Alive: Laufzeit

Alle unten genannten Pfade beziehen sich auf den **Dungeons-Alive-Projektroot**, erkennbar an `AGENTS.md`, `CODEX_START.md` und `gradle.properties`, nicht auf den Installationsordner dieses Skills. Vor der ersten Änderung die dortigen Vorgaben und `docs/VALIDATION.md` lesen. Aktuelle Registrierungen aus `design/implementation-status.json` und Code ermitteln; ältere Mengenzahlen in Fachtexten sind kein Registry-Beleg.

## Änderung durchführen

- Version, Registrierung und betroffene Zustandsmaschine zuerst im Code verfolgen. Java 21, Minecraft 1.21.1 und NeoForge 21.1.265 bleiben ohne beauftragten Versionswechsel bestehen.
- Serverentscheidung, synchronisierte Darstellung und Speicherung getrennt nachvollziehen. Schaden, Besitz, Handel, Questfortschritt, Claims, Seelen und Cooldowns gehören auf den Server; Common-Code darf keine Client-Klassen laden. `quest/core` bleibt ohne Minecraft-Abhängigkeit.
- Registry- und Quest-IDs sowie ihre gespeicherte Bedeutung erhalten. Quests werden bewusst angenommen und Belohnungen abgeholt; ein Abschluss startet keinen Nachfolger automatisch.
- Gegner über `MobProfiles`, friedliche Figuren über `PeacefulProfiles` erweitern. Passende bestehende Ressourcen-Generatoren in `tools/` nutzen. Bei Konstrukten Besitzer/Ziel, Lebensdauer, lokale/globale Grenzen und Aufräumen verfolgen; weder Spielerbauten zerstören noch unbekannte Chunks laden.
- Bei Ausrüstung gemeinsame serverseitige Seelen verwenden. Item-Anzeigedaten sind kein Guthaben. `onStopUsing` muss den Cooldown auch beim Hotbarwechsel auslösen; Deckungs- und Besitzerprüfungen erhalten.
- Der gefangene Schmied bleibt CIVILIAN mit gespeichertem Retter und Befreiungsstatus. Rendern darf ihn nicht befreien. Originaldienste, normale Warenangebote und Sandbox-Adaptionen getrennt benennen.
- Eine Fehlerkorrektur mit dem beobachtbaren Auslöser verbinden und die relevante Regression prüfen. Aktionsvorbereitung, sichtbarer Kontakt und Servertreffer müssen dieselbe Zeitleiste verwenden.

## Fachtexte auswählen

| Änderung | Projektdateien |
| --- | --- |
| Architektur und Servergrenzen | `docs/ARCHITECTURE.md`, tatsächliche betroffene Klassen |
| Kreaturen und Aktionen | `docs/MOBS_RUNTIME.md`, `docs/REFERENCE_ANIMATION_RUNTIME.md`, `docs/SOURCE_MODEL_AUDIT.md` |
| Konstrukte oder Wasserbewegung | `docs/CONSTRUCTS_AND_SWIMMING.md` |
| Items, Seelen und Waffen | `docs/GEAR_AND_SOULS.md` |
| Quests | `docs/QUEST_FORMAT.md` |
| Berufe und Händler | `docs/VILLAGERS_AND_ANIMATIONS.md` |
| Prüfung | `docs/GAMETESTS.md`, `docs/MANUAL_TESTS.md` |

## Nachweis

Für Codeänderungen `./gradlew build` und gezielt sinnvolle JUnit-/GameTests verwenden; bei Common-/Registrierungsänderungen zusätzlich `./gradlew runGameTestServer`. Keine Laufzeittests für reine Textänderungen erfinden. Bei sichtbaren Änderungen den Ablauf aus `dungeons-native-qa` verwenden, sofern verfügbar, sonst die dokumentierten Client-Aufnahmehelfer direkt nutzen. Tatsächlich ausgeführte Prüfungen, Fehler und Umgebungsgrenzen dokumentieren; frühere erfolgreiche Läufe bleiben historische Belege.
