# Optionaler Dungeons-Content-Abgleich

Prüfstand: **2. November 2026**. Dies ist ein statischer Kompatibilitäts- und ID-Abgleich eines genau benannten öffentlichen Autoren-Releases. **Es fand kein gemeinsamer Spielstart mit Dungeons Alive statt.** Der fremde Mod und seine Assets gehören nicht zum Projektpaket.

## Exakt untersuchte Datei

| Merkmal | Festgestellter Wert |
| --- | --- |
| Projekt | Dungeons Content |
| Modversion | 1.19 |
| Minecraft / Loader | Java 1.21.1 / NeoForge |
| Datei | `dungeons_1.19_mc1.21.1.jar` |
| Veröffentlichungszeit laut Autoren-API | 2025-11-11T21:43:35.990517Z |
| Modrinth-Projekt / Version | `R0ToJjk1` / `iyRGzLcx` |
| CurseForge-Projekt / Datei | `423533` / `7212076` |
| Tatsächliche Mod-ID | **`duneons`** |
| Dateigröße | 69.282.814 Bytes |
| Angegebene Lizenz | All Rights Reserved |

Quellen: [Autoren-Dateiseite](https://www.curseforge.com/minecraft/mc-mods/minecraft-dungeons-content/files/7212076), [Autoren-Projektseite](https://modrinth.com/mod/dungeons-content), [Versions-API](https://api.modrinth.com/v2/version/iyRGzLcx). Die API-Datei wurde temporär aus dem dort angegebenen [öffentlichen Download](https://cdn.modrinth.com/data/R0ToJjk1/versions/iyRGzLcx/dungeons_1.19_mc1.21.1.jar) gelesen. Ihr SHA-512 stimmt mit dem API-Wert überein:

```text
f5f79f9b95add1585ed18c91384682c0c08fc5dd54da79276635e379693192db1823a2a4a14825ab788d7a0771f2a7eecec2a08c6a8b0f1bf33d9bae853ec795
```

Die vollständigen Ergebnisse stehen in [dungeons-content-audit.json](../design/dungeons-content-audit.json). Das Projekt enthält nur selbst formulierte Prüfergebnisse und für Interoperabilität relevante IDs; weder die JAR noch fremde Texturen, Modelle, Animationen oder Implementierungscode wurden übernommen.

## GeckoLib-Angaben widersprechen sich

Der Changelog der Datei deutet einen Wegfall von GeckoLib an, während die Projektbeschreibung die Abhängigkeit weiterhin nennt. Die Versions-API liefert eine leere Abhängigkeitsliste. Deshalb wurde zusätzlich die tatsächliche **`META-INF/neoforge.mods.toml`** der Datei geprüft. Sie deklariert:

| Mod | Typ | Versionsbereich |
| --- | --- | --- |
| `neoforge` | required | `[21.1.65,)` |
| `minecraft` | required | `[1.21.1]` |
| `geckolib` | required | `[0,)` |
| `geckolib` | optional, zweimal zusätzlich vorhanden | `[0,)` |

Für genau diese Datei darf die Abhängigkeit deshalb nicht als erledigt oder sicher entfallen gelten. Ein zukünftiger Installationstest muss eine passende NeoForge-1.21.1-GeckoLib-Version pinnen und den tatsächlichen Loaderbericht auswerten. Die redundanten Einträge werden hier dokumentiert; der fremde Mod wurde nicht umgeschrieben. Eine zusätzlich enthaltene alte Forge-`mods.toml` ist kein Ersatz für die NeoForge-Metadaten.

## 250 deklarierte Entity-IDs

Die Klasse `net.mde.duneons.init.DuneonsModEntities` wurde mit dem JDK-Werkzeug `javap -p -c` statisch untersucht. Ihr Entity-`DeferredRegister` erhält den Namespace **`duneons`**. Die Hilfsmethode `register(String, EntityType.Builder)` reicht den konstanten Namensparameter an `DeferredRegister.register` weiter. Im statischen Initialisierer ergeben sich **250 verschiedene Namensargumente und 250 Registrierungsaufrufe**.

Alle IDs stehen im JSON unter `entity_declarations`. Die ebenfalls geprüfte Existenz eines Übersetzungsschlüssels ist lediglich zusätzliche Metadatenhilfe; sie ist nicht der Registrierungsnachweis. Die `minecraft_mob_category` ist die vom Autor gesetzte technische Spawnkategorie und keine verlässliche fachliche Einordnung des Originalmobs.

Einige tatsächlich deklarierte Beispiele:

| ID | Bemerkung |
| --- | --- |
| `duneons:archillager` | Registrierungsname ohne Unterstrich |
| `duneons:namelessone` | Registrierungsname ohne Unterstrich |
| `duneons:jungleabomination` | Separat von Schlaf-/Angriffshilfsformen |
| `duneons:ancientguardian` | Separat von `ancientguardiansleep` |
| `duneons:tempestgolem` | Separat von `tempestgolemsleep` |
| `duneons:heartvengeful` | Weitere Zustands- und Angriffs-IDs vorhanden |
| `duneons:leaper` | Fremde Registry-ID; Katalogspielname bleibt Leapleaf |
| `duneons:gift_wrapper` | Einzelne Namen verwenden Unterstriche |
| `duneons:firefly` | Keine Gleichsetzung mit dem eigenen Dungeons-Alive-Partikel |
| `duneons:redbow_projectile` | Beispiel eines Projektils, nicht einer zusätzlichen Kreatur |

**250 Registrierungen sind keine 250 unterschiedlichen Mobs.** Darunter befinden sich 43 IDs mit Projektilsuffix sowie Schlaf-/Todesformen, Angriffshelfer, Kisten, Portale und Steuerobjekte. Aus einem ähnlich klingenden Registrierungsnamen wird keine automatisch bestätigte Zuordnung zum Original abgeleitet. Ein echter ID-Eintrag beweist außerdem weder funktionierendes Spawning noch fertige Animation oder Questintegration.

## Was vor einer Integration noch nötig ist

Der Autorenhinweis empfiehlt die Installation auf einem Server nicht. Das ist für dieses Projekt mit eigener Serverlogik eine konkrete offene Kompatibilitätsfrage. Der Mod bleibt deshalb eine optionale Ergänzung; Dungeons Alive muss ohne ihn funktionieren. Quelle: [Autorenbeschreibung](https://modrinth.com/mod/dungeons-content).

Ein späterer Versuch sollte in einer separaten neuen Testinstanz mit gepinnten Dateien stattfinden. Zuerst müssen Loaderstart und ein Dump der **tatsächlich geladenen** Registry gelingen. Danach sind Clientdarstellung, Dedicated Server, Spawnverhalten, Speichern/Laden und optionale Questziele zu prüfen. Erst dann wird ein manuell geprüftes Mapping von Katalogeinträgen auf externe Entity-IDs verbindlich eingetragen. Dabei sind doppelte Bossbegegnungen mit den eigenen Prototypen zu vermeiden.

Die vorhandenen statischen Daten sparen bei diesem Schritt Sucharbeit. Sie ersetzen keine gemeinsame Versionsmatrix und geben keine Freigabe für fremde Assetweitergabe.
