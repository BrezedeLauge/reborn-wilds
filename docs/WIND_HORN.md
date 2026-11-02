# Windhorn – spielbarer Artefakt-Prototyp

Das Windhorn ist eine **eigene Nachbildung der Grundidee** eines defensiven
Dungeons-Artefakts. Code, Pixelgrafik und Parameter sind für dieses Projekt
entstanden. Es enthält keine aus Dungeons extrahierten Texturen, Modelle oder
Sounds und beansprucht keine exakte Nachbildung des Original-Items.

## Benutzen

- Item-ID: `dungeonsalive:wind_horn`
- Rechtsklick in der Haupt- oder Nebenhand löst eine Windwelle aus.
- Feindliche Mobs innerhalb einer **Kugel von 6 Blöcken Radius** werden bei freier
  Sichtlinie zurückgestoßen und erhalten **60 Spielticks Langsamkeit I**.
- Die Abklingzeit beträgt **200 Spielticks** – bei normaler Spielgeschwindigkeit
  10 Sekunden. Sie gilt für alle Exemplare dieses Item-Typs beim jeweiligen Spieler.
- Das Horn verbraucht sich nicht und verursacht keinen direkten Schaden.
- Es werden keine Blöcke zerstört oder verändert.

Der Effekt behandelt ausschließlich Lebewesen, die Minecrafts `Enemy`-Schnittstelle
implementieren. Spieler, normale passive Tiere und andere Nicht-Enemy-Entities
werden dadurch grundsätzlich ausgeschlossen. Zusätzlich werden Verbündete,
gezähmte Tiere und Entities mit Besitzer ausgeschlossen. Mods mit eigenen
Begleiter-/Fraktionssystemen müssen diese Beziehungen über die üblichen
Minecraft-APIs melden oder später einen passenden Integrationsfilter erhalten.

Vanilla-Rückstoßresistenz wird respektiert. Besonders widerstandsfähige Gegner
können deshalb wenig oder gar nicht bewegt werden. Rückstoß kann wie andere
Minecraft-Bewegungen indirekt zu Stürzen führen; der Item-Code fügt selbst keinen
Schadensereignis-Aufruf hinzu.

## Herstellen

Diese vier Zutaten lassen sich in beliebiger Anordnung kombinieren:

| Zutat | Anzahl |
| --- | --- |
| Ziegenhorn, beliebige Instrumentvariante | 1 |
| Kupferbarren | 1 |
| Feder | 1 |
| Leder | 1 |

Ergebnis: **1 Windhorn**. Das Ziegenhorn wird beim Herstellen verbraucht.

Das Rezept liegt im für 1.21.1 richtigen Singular-Ordner
`data/dungeonsalive/recipe/wind_horn.json`; das Ergebnis nutzt `id` und `count`.

## Netzwerk und Animation

Der Client gibt nur ein passendes Interaktionsergebnis zurück. Die tatsächliche
Abklingzeit, die Zielauswahl, Rückstoß und Statuswirkung werden auf dem Server
berechnet. Auch Ton und sichtbare Wind-/Wolkenpartikel werden vom Server ausgelöst.
So läuft derselbe Code im Einzelspieler und auf einem Dedicated Server.

Der Prototyp nutzt den vorhandenen Minecraft-Hornklang `SoundEvents.RAID_HORN`
mit angepasster Tonhöhe sowie die Vanilla-Partikel `GUST` und `CLOUD`. Der Klang
startet keinen Überfall. Das Item-Modell nutzt eine eigene 32×32-Pixelgrafik mit
angepasster Handhaltung. Eine Mund-/Blasanimation ist noch nicht implementiert.

## Entwicklung

Registrierung durch das Hauptprojekt:

```java
ITEMS.registerItem("wind_horn", WindHornItem::new,
        new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON));
```

Der Tooltip verwendet `item.dungeonsalive.wind_horn.tooltip`; Übersetzungen werden
in den gemeinsamen Sprachdateien gepflegt. Die Grafik lässt sich reproduzieren:

```bash
python3 tools/generate_item_textures.py
```

## Gezielte Prüfung im Spiel

Mit Cheats: `/give @s dungeonsalive:wind_horn`.

1. Zombie in 3 Blöcken Entfernung: Rückstoß und kurzzeitige Langsamkeit.
2. Zombie hinter einer geschlossenen Wand: keine Wirkung.
3. Zombie außerhalb von 6 Blöcken: keine Wirkung.
4. Spieler, Dorfbewohner und gezähmter Wolf in Reichweite: keine Wirkung.
5. Zombie im gleichen Scoreboard-Team wie der Spieler: keine Wirkung.
6. Direkt wieder benutzen: Abklingzeit verhindert einen zweiten Effekt.
7. Nach 200 Spielticks erneut benutzen: Effekt wieder verfügbar.
8. Zwei Spieler auf einem Server: Jeder hat seine eigene Abklingzeit; alle sehen
   die vom Server ausgelösten Effekte und dieselbe Mob-Bewegung.

Ein erfolgreicher Build prüft Typen und Ressourcen, ersetzt diese Verhaltens- und
Sichtprüfung aber nicht. Die nächsten Ausbauschritte wären eine Handanimation,
konfigurierbare Balancewerte und ein Integrationstest mit Dungeons-Gegnern.

## API-Quellen

- [NeoForge 1.21.1: Rezepte und Ordner](https://docs.neoforged.net/docs/1.21.1/resources/server/recipes/)
- [NeoForge 1.21.1: Shapeless-Rezeptformat](https://docs.neoforged.net/docs/1.21.1/resources/server/recipes/builtin/)
- [Offizielles Fabric-Referenzbeispiel für die Vanilla-Item-API in 1.21.1](https://github.com/FabricMC/fabric-docs/blob/main/reference/1.21.1/src/main/java/com/example/docs/item/custom/LightningStick.java)

Die letzte Quelle bestätigt nur die versionsspezifischen Vanilla-Methodensignaturen;
der Mod selbst verwendet NeoForge.
