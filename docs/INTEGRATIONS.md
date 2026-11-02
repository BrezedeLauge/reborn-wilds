# Optionale Integrationen

Stand: **2. November 2026**. Die bisherigen Versionspins wurden beibehalten; der neue Dungeons-Content-Abgleich ist gesondert dokumentiert. Dieses Projekt zielt auf **Minecraft Java 1.21.1 mit NeoForge und Java 21**. Die hier genannten fremden Mods und Ressourcenpakete werden nicht mitgeliefert. Für den Animationsstack und den Enchanter gibt es ein optionales, prüfsummengesichertes Installationsskript: [OPTIONAL_SETUP.md](OPTIONAL_SETUP.md).

## Was „geprüft“ in diesem Dokument bedeutet

**Datei gelistet** bedeutet: Die Autorenseite nennt eine konkrete Datei für Minecraft 1.21.1 und den passenden Loader. **Dokumentierte Abhängigkeit** bedeutet: Die Autorenseite nennt die zusätzliche Komponente. **Headless getestet** bedeutet: Die Kombination wurde auf einem echten Minecraft-GameTest-Server geladen und die genannten Funktionen wurden dort geprüft. **Grafisch beziehungsweise im Multiplayer abgenommen** erfordert zusätzliche Clienttests.

Das Enchanter-Profil hat Laden, echte Registry und optionale Questverknüpfung im Headless-Test bestanden. Für den Animationsstack sind Downloads, SHA512 und Metadaten geprüft. Grafische Sichtprüfung, vollständiger Enchanter-Kampf und echter Client-Multiplayer bleiben offen. Details: [VALIDATION.md](VALIDATION.md).

Insbesondere ist ein Treffer in der allgemeinen Versionsliste eines Projekts noch kein Nachweis, dass jedes beliebige JAR dieses Projekts mit 1.21.1 funktioniert.

## Empfohlene Reihenfolge

1. Eigenen Dungeons-Alive-Kern allein starten und den mitgelieferten Testablauf erledigen.
2. Die eigenen 19 Berufsanimationen zunächst ohne EMF ansehen. Fresh Animations zusammen mit EMF und ETF danach in einem separaten Profil für unterstützte Vanilla-Mobs prüfen. Bei EMF tritt unser Villager-Renderer standardmäßig zurück; beide Animationssysteme werden nicht automatisch gemischt.
3. Als erste überschaubare Dungeons-Inhaltsmod Enchant With Mob einschließlich seiner Abhängigkeiten ausprobieren.
4. Eine Quest anhand der in dieser Instanz tatsächlich vorhandenen Entity-ID verbinden.
5. Den breiteren Kandidaten Dungeons Content separat evaluieren. Erst bei geklärtem Server- und Speicherverhalten in ein Mehrspielerprofil übernehmen.

## Kandidaten für 1.21.1

| Baustein | Konkreter Quellenstand | Aufgabe im Gesamtprojekt | Status |
| --- | --- | --- | --- |
| **Fresh Animations** | FA **1.10.4**, CurseForge-Datei **7670377**, 1.21.1 ausdrücklich gelistet | Lebendigere Animationen für unterstützte Vanilla-Kreaturen | Optionale Client-Ressource; Dateimetadaten geprüft |
| **Entity Model Features (EMF)** | **3.3.12-neoforge-1.21**, gelistet für 1.21/1.21.1, 15.10.2026 | Lädt die Modelle und Animationsfunktionen des Ressourcenpakets | Optionale Client-Mod; benötigt ETF |
| **Entity Texture Features (ETF)** | **7.2.6-neoforge-1.21**, gelistet für 1.21/1.21.1, 15.10.2026 | Ergänzende Textur- und Entity-Darstellung | Optionale Client-Mod |
| **Enchant With Mob** | **1.21.1-21.7.0**, CurseForge-Datei **9328931**, NeoForge, 26.10.2026 | Dungeons-Enchanter und Mob-Verzauberungen | Mit Bagus Lib: Headless-Laden, Registry und Questintegration bestanden; grafische Abnahme offen |
| **Dungeons Content** | **dungeons_1.19_mc1.21.1**, CurseForge-Datei **7212076**, NeoForge | Größerer Bestand an Dungeons-Regionen, Mobs und Ausrüstung | Optionaler Evaluationskandidat; Autor rät vom Servereinsatz ab; GeckoLib erforderlich |
| **Dungeons Weapons** | **11 / DungeonsWeaponsv3 Beta 1**, Modrinth-Version **izYP8Sfg**, NeoForge 1.21.1 | Zusätzliche Dungeons-Waffen | Frühe Beta; zusätzliche Prüfung von Balance und Multiplayer erforderlich |
| **GeckoLib** | **4.9.3** für NeoForge 1.21.1 auf der Autoren-Versionsseite | Möglicher späterer Baustein für eigene komplexe Animationen | Kein automatisches Upgrade auf GeckoLib 5; konkrete Abhängigkeit je Inhaltsmod prüfen |

Quellen zu den Dateiversionen:

- [Fresh Animations 1.10.4](https://www.curseforge.com/minecraft/texture-packs/fresh-animations/files/7670377)
- [EMF, nach Minecraft 1.21.1 gefilterte Dateien](https://www.curseforge.com/minecraft/mc-mods/entity-model-features/files/all?page=1&version=1.21.1)
- [ETF, nach Minecraft 1.21.1 gefilterte Dateien](https://www.curseforge.com/minecraft/mc-mods/entity-texture-features-fabric/files/all?page=1&version=1.21.1)
- [Enchant With Mob 1.21.1-21.7.0](https://www.curseforge.com/minecraft/mc-mods/enchant-with-mob/files/9328931)
- [Enchant With Mob: Dependencies](https://www.curseforge.com/minecraft/mc-mods/enchant-with-mob/relations/dependencies)
- [Dungeons Content 1.19 für 1.21.1](https://www.curseforge.com/minecraft/mc-mods/minecraft-dungeons-content/files/7212076)
- [Dungeons Weapons 11 Beta](https://modrinth.com/mod/dungeons-weapons/version/izYP8Sfg)
- [GeckoLib-Versionen](https://modrinth.com/mod/geckolib/versions)

Die Versionsangaben dokumentieren den recherchierten Stand. Sie sind keine pauschale Anweisung, Abhängigkeiten gegen die neuesten verfügbaren Dateien auszutauschen. Ein Inhaltsmod kann einen engeren GeckoLib- oder Bagus-Lib-Bereich voraussetzen; der Launcher beziehungsweise die Metadaten der gewählten Datei müssen diesen auflösen.

## Animationen installieren und prüfen

EMF und ETF kommen als passende **NeoForge-JARs** in den Client-Modordner der Testinstanz. Fresh Animations bleibt als ZIP im Ressourcenpaketordner und wird im Spiel aktiviert. Nicht gleichzeitig OptiFine oder eine weitere CEM-Implementierung installieren: EMF nennt diese Kombinationen als inkompatibel. Quellen: [EMF-Autorenseite](https://modrinth.com/mod/entity-model-features), [Fresh Animations](https://modrinth.com/resourcepack/fresh-animations).

Die erste Sichtprüfung erfolgt an einem Villager, einem Zombie und einer Kuh: Bewegung, Augen, Schwimmen, Blickrichtung und Texturen ansehen. Danach einen eigenen Projekt-Mob und einen Mob aus der optionalen Inhaltsmod prüfen. Bei einem Fehler zunächst nur diese drei visuellen Komponenten zusammen betreiben. Ein aufwendiger Shader ist für diesen Vergleich nicht erforderlich.

Fresh Animations hat eigene Nutzungsbedingungen; EMF und ETF sind auf ihren Projektseiten als LGPL-3.0-only gekennzeichnet. Für dieses Paket genügt die getrennte Installation aus den Autorenquellen. Quellen: [Fresh Animations – Bedingungen in der Beschreibung](https://modrinth.com/resourcepack/fresh-animations), [EMF](https://modrinth.com/mod/entity-model-features), [ETF](https://modrinth.com/mod/entitytexturefeatures).

## Warum die Integration über Registry-IDs läuft

Der Kern kann Quests an registrierte Entities, Items und Biome knüpfen, ohne fremde Java-Klassen zu kopieren. Die Auswahl erfolgt über vollständige IDs der Form `namespace:name`. Eine als Gegner bezeichnete Kreatur aus Dungeons hat nicht zwangsläufig denselben internen Namen in jeder Minecraft-Mod.

Bei **Dungeons Content** wurden inzwischen 250 Entity-Deklarationen statisch aus der SHA512-geprüften Autorendatei nachvollzogen. Der tatsächliche Namespace lautet `duneons`. Die Zahl enthält auch Projektile und Zustandshelfer, also nicht 250 unterschiedliche Mobs. Der Laufzeit-Registry-Abgleich bleibt offen. Details einschließlich widersprüchlicher GeckoLib-Metadaten: [DUNGEONS_CONTENT_AUDIT.md](DUNGEONS_CONTENT_AUDIT.md).

Für **Enchant With Mob 1.21.1-21.7.0** wurden die offiziellen, SHA512-geprüften JAR-Ressourcen gelesen. Zusätzlich hat `./gradlew runIntegrationGameTest` **`enchantwithmob:enchanter` in der echten geladenen Registry und als optionales Questziel bestätigt**. Der Server lud Enchant With Mob, Bagus Lib und 25 Dungeons-Alive-Quests einschließlich des optionalen Datapacks; alle 13 Server-GameTests bestanden. Zusätzlich war das optionale `dungeons_wilds`-Datapack geladen. Version, Prüfsumme und Dateipfade sind in [OPTIONAL_SETUP.md](OPTIONAL_SETUP.md) dokumentiert. Der Headless-Test umfasst keinen vollständigen Kampf und keine grafische oder echte Client-Multiplayer-Abnahme.

### Konkreter Arbeitsauftrag für die nächste Codex-Sitzung

1. Die ausgewählte Inhaltsmod und ihre Abhängigkeiten in einer separaten Testinstanz installieren.
2. Registry-IDs mit `/summon ` beziehungsweise `/give @s ` und Tab-Vervollständigung in einer Testwelt feststellen. Bei größeren Katalogen einen schreibgeschützten Registry-Export ergänzen, über den vorhandenen `/dungeonsalive registry`-Befehl verwenden.
3. Für jede genutzte ID zusätzlich Modversion und beobachteten Anzeigenamen notieren.
4. Das Questformat der aktuellen Projektdokumentation verwenden. Keine neue JSON-Struktur erfinden, die der Loader nicht kennt.
5. Existenz von Entity, Item und gegebenenfalls Biom beim Laden prüfen. Fehlende optionale Inhalte dürfen keine stillschweigenden Ersatzbelohnungen oder unbeendbaren aktiven Quests erzeugen.
6. Je eine Annahme-, Fortschritts- und Abholsequenz mit installierter und entfernter Inhaltsmod ausführen.

Für später geplante Adapter genügt folgende Inventarstruktur. Dies ist eine **Planungstabelle**, keine bereits ausgelieferte Konfigurationsdatei:

| Feld | Beispielinhalt |
| --- | --- |
| `source_mod` | Exakt festgestellte Mod-ID |
| `source_version` | Version der getesteten Datei |
| `entity_id` | Vollständige, im Spiel bestätigte ID |
| `item_id` | Vollständige, im Spiel bestätigte ID |
| `quest_id` | Eigene stabile Quest-ID |
| `verified_in_game` | Datum und Testprotokoll |

## Dungeons II und ältere Alternativen

**Yazz's Dungeons: The Sequel** listet Forge **1.20.1**. Es ist damit kein direkt austauschbarer Baustein für diese NeoForge-1.21.1-Basis. **Dungeons Content 2** wird als **Abandoned** geführt. Beide sind für das Standardprofil zurückgestellt. Quellen: [Yazz's Dungeons: The Sequel](https://www.curseforge.com/minecraft/mc-mods/yazzs-dungeons-the-sequel), [Dungeons Content 2](https://www.curseforge.com/minecraft/mc-mods/spice-wood-content).

**Dungeons Mobs** und **Dungeons Gear** sind interessante Quellcode-Referenzen für einen späteren eigenen Port. Ihre öffentlich gelisteten Forge-Dateien bis 1.19.2 dürfen nicht als NeoForge-1.21.1-Abhängigkeiten ausgegeben werden. Quellen: [Dungeons Mobs](https://www.curseforge.com/minecraft/mc-mods/dungeons-mobs), [Dungeons Gear](https://www.curseforge.com/minecraft/mc-mods/dungeons-gear).

Ein Anspruch „alle Inhalte aus Dungeons I und II integriert“ entsteht erst nach vollständigem Inhaltsinventar und tatsächlicher Abnahme jedes Eintrags. Die verfügbaren Projektbeschreibungen belegen das für dieses Profil nicht.

## Protokoll für eine freigegebene Kombination

Für ein späteres distributierbares Modpack je Testlauf speichern:

- Minecraft-, Java-, NeoForge- und Modversionen sowie Dateiprüfsummen.
- Client allein, integrierter Server und Dedicated Server als getrennte Ergebnisse.
- Frische Welt, Weltneustart, Tod/Respawn und Dimensionswechsel.
- Questfortschritt mit zwei Spielern; kein fremder Fortschritt und keine doppelte Belohnung.
- Entfernen der optionalen Integration und anschließendes Laden derselben Testwelt.
- Animationen ohne Shader, danach mit dem tatsächlich gewählten Shader.

Das Protokoll weist Headless-Funktionstests, grafische Prüfung und echten Client-Multiplayer getrennt aus. Ein bestandener Headless-Test allein bedeutet noch keine vollständige Abnahme aller drei Bereiche.
