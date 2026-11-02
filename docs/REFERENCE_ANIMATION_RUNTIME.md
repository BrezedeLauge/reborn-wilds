# Referenzmodelle und Animationslaufzeit

Technischer Stand: 2. November 2026. Ziel: **Minecraft Java 1.21.1, NeoForge 21.1.265, Java 21**.

Die Klassen unter `client/entity/reference` verbinden die geprüften Quellmodelle mit echten Minecraft-Entities. Geometrie, Texturen und vorhandene Quellclips behalten ihre eigenen Strukturen. Der Server bestimmt weiterhin Bewegung, Angriffsart, Treffer, Beschwörungen und Cooldowns. Die Darstellung löst keinen Schaden aus und erzeugt keine zusätzlichen Gameplay-Entities.

Das ist eine gemeinsame visuelle Laufzeit für mehrere lizenzierte Quellen und native Minecraft-Modelle. Ein importiertes Modell ist **kein vollständiger Port seiner ursprünglichen KI oder der Minecraft-Dungeons-Spielmechanik**. Herkunft und Lizenz der tatsächlich übernommenen Dateien stehen in [SOURCE_MODEL_AUDIT.md](SOURCE_MODEL_AUDIT.md), [THIRD_PARTY_NOTICES.md](../THIRD_PARTY_NOTICES.md) und den jeweiligen Importmanifesten. Es werden keine Actions-&-Stuff-Dateien mitgeliefert.

## 1. Unterstützte Eingaben und Zuständigkeiten

| Quelle | Verarbeitung im Projekt | Laufzeitdarstellung |
| --- | --- | --- |
| Goety-Java-Modelle | Ausgewählte `createBodyLayer`-Geometrien und `AnimationDefinition`-Klassen werden unter eigenem Namespace importiert. Inline-Animationen bleiben ausdrücklich zugeordnet. | Native `ModelPart`-Hierarchie und `KeyframeAnimations.animate`. |
| Ausgewählte Bedrock-/Gecko-JSON-Dateien aus Dungeons Mobs | `tools/convert_bedrock_models.py` konvertiert die geprüften Geometrien und Clips vor dem Build. Der Bericht hält umgesetzte Ausdrücke und Grenzen fest. | Native ModelParts; bei per-face UVs eigene `ReferenceMeshCube`-Flächen einschließlich negativer UV-Größen. |
| Ausgewählte ReCrafted-CEM-Dateien | `tools/convert_cem_models.py` erhält benannte Knoten, Hierarchie, ursprüngliche Pivots, Spiegelung, Würfelaufweitung, statische Skalen und unterstützte Ausdrücke. | Native ModelParts mit unsichtbaren Vanilla-Hostknoten, passenden Hosttreibern und streng gebundenem CEM-Ausdrucksprogramm. |
| Bereits in Minecraft enthaltene Modelle | Native Modellfabriken und installierte Textur-ResourceLocations werden benutzt. | Eigene Kompositionsrenderer für Hauptleute, Skelettreiter und beschworenen Schleim. |

Die Laufzeit ist **kein allgemeiner GeckoLib-/Molang-Interpreter** und kein beliebiger OptiFine-Ressourcenpaket-Loader. Sie verarbeitet die explizit konvertierten und registrierten Bündel. Neue Formatmerkmale müssen im Konverter oder in der Laufzeit bewusst ergänzt werden; unbekannte Ausdrücke oder Bones werden nicht unbemerkt auf null gesetzt.

Wichtige Arbeitsdateien:

- `design/reference-models.json`: Zuordnungen von Quellen, Clips, Aktionen, Skalen, Material- und Itemlayern.
- `design/reference-hit-timings.json`: recherchierte Treffer-/Freigabezeitpunkte der Quellclips und benannte Abweichungen.
- `design/cem-model-imports.json` und `design/cem-extra-model-imports.json`: konkrete CEM-Rigs, Hostaliases, Ausdrücke und geprüfte Quellen.
- `design/bedrock-model-imports.json` und `design/bedrock-conversion-report.json`: Bedrock-Importvertrag und Konvertierungsbefunde.
- `design/builtin-models.json`: die vier nativen Kompositionen einschließlich der unverändert übernommenen Schleimtextur und ihrer Prüfsumme.
- `client/entity/reference/imported/ReferenceModelIndex.java`: generierte Registrierungsbindungen; Quelle für Änderungen bleibt das zugehörige Manifest beziehungsweise der Generator.

`MobRenderers` installiert den generierten Index, ergänzt die nativen Kompositionen, registriert sämtliche Referenzlayer und wählt die jeweiligen Renderer. Auch passive Referenzmodelle erhalten dort ihre Layerdefinition. Die Registrierung ihrer eigenen Entityrenderer erfolgt im passiven Clientanschluss.

## 2. Definitions- und Renderer-API

`ReferenceMobDefinition.builder(id, layerFactory, texture)` beschreibt ein einzelnes visuelles Profil. Die Texture-Strings sind vollständige ResourceLocations. `scale` ist eine **gleichmäßige** Skalierung; die Quellproportionen werden nicht einzeln auf die alte Breite und Höhe einer Prototyp-Kollisionsbox gestaucht.

| Builder-Aufruf | Vertrag |
| --- | --- |
| `.scale(float)`, `.shadow(float)` | Gleichmäßiger Modellmaßstab und optionaler Schattenradius. |
| `.bakedRoot(Supplier<ModelPart>)` | Alternative Fabrik für vollständig gebackene Geometrie, etwa per-face UVs. Die LayerFactory bleibt trotzdem für die Registrierung vorhanden. |
| `.clip(name, AnimationDefinition)` | Explizite benannte Quellanimation. Doppelte Namen werden abgelehnt. |
| `.base(name)` | Statischer oder kontinuierlicher Zusatzclip, etwa eine Quellskalierung. |
| `.idle(name)` | Ruhige Grundanimation. |
| `.walk(name, speed, amplitude)` | Bewegung über die tatsächlichen `limbSwing`-/`limbAmount`-Signale. |
| `.swim(idle, move, speed, amplitude)` | Separate Grund-/Bewegungsclips bei tatsächlichem Wasserkontakt. |
| `.action(kind, clip, sourceHitSeconds)` | Clip mit exakt auf das Serverangriffsfenster abgebildetem Quelltrefferzeitpunkt. Optional sind zwei weitere Parameter für Ein-/Ausblendticks möglich. |
| `.death(name)` | Dedizierter Todclip im bestehenden Lebenszeitfenster der Entity. |
| `.look(bone, maxYaw, maxPitch)` | Begrenzte additive Blickbewegung; Winkelgrenzen in Grad. |
| `.humanoidGait(leftLeg, rightLeg, leftArm, rightArm, speed, amplitude)` | Ausdrücklich eigene einfache Gangadaption für einen passenden Rig. |
| `.spin(bone, axis, degreesPerSecond)` | Additive kontinuierliche Drehung; etwa für einen Lüfter. |
| `.oscillate(bone, axis, amplitudeDegrees, cyclesPerSecond, phaseDegrees)` | Additive Schwingung. Achsen: `x`, `y`, `z`. |
| `.boneScale(bone, uniform)` oder `.boneScale(bone, x, y, z)` | Statische Boneskala, die nach jedem `resetPose()` wiederhergestellt wird. |
| `.hiddenBones(names...)` | Benannte Modellteile von Beginn an ausblenden. |
| `.cemDriver(host)`, `.hostAlias(sourceName, rigNode)` | Einen ausdrücklich unterstützten Vanilla-Host und dessen tatsächliche Rigknoten binden. |
| `.expression(target, expression)` | CEM-Zuweisung in originaler Auswertungsreihenfolge. |
| `.handItems(rightPath, leftPath[, palmAnchor])` | Tatsächlich ausgerüstete Gegenstände an vollständigen Elternpfaden darstellen. |
| `.materialLayer(texture, bakedRootFactory)` | Weitere Geometrie mit anderer Textur und identischer vollständiger Knochenhierarchie. |
| `.overlay(texture, fullBright)` | Statischer zusätzlicher Cutout-Pass auf der Hauptgeometrie. |
| `.emissive(texture, idleStrength, pulseAmplitude, attackBoost)` | Additiver, unbeleuchteter Leuchtpass auf der Hauptgeometrie. |
| `.animatedOverlay(prefix, frameCount, ticksPerFrame, alpha, fullBright)` | Transparente nummerierte PNG-Folge auf der Hauptgeometrie. |

Der Renderer ist generisch: `ReferenceMobRenderer<T extends Mob>`. Eine passive Figur kann deshalb eine echte `PathfinderMob`-Entity bleiben. Sie muss nicht von `Monster` erben, nur um denselben Rig verwenden zu können.

Beim Baken werden alle Animationsbones, Handpfade, Hostaliases und CEM-Eingänge geprüft. Der Renderer prüft die Existenz aller verwendeten Texturen einschließlich jedes Frames einer PNG-Folge. Ein fehlerhafter Assetvertrag führt zu einem benannten Fehler mit Mob-ID und betroffenem Bezug.

## 3. Timing zwischen Quelle und Server

`ReferenceAnimationSource<T>` liefert die aktive `AttackKind` und ihre verstrichenen Ticks. Der Standardadapter liest diese Werte aus `DungeonMob`. Für andere Mobtypen ist der kompatible Standard zunächst rein bewegungsbasiert.

Eine Aktionsbindung kennt vier Größen:

- `W`: Serverwindup in Ticks.
- `R`: Servererholung in Ticks.
- `H`: Treffer-/Freigabezeit des Quellclips in Sekunden.
- `L`: Gesamtlänge des Quellclips in Sekunden.

`ActionClipTiming.seconds` bildet die verstrichene Zeit `t` so ab:

```text
0 <= t <= W:      Quellzeit = H * t / W
W < t <= W + R:   Quellzeit = H + (L - H) * (t - W) / R
```

Die Eingangszeit wird auf das Aktionsfenster begrenzt. **Bei `t = W` liegt exakt der deklarierte Quellkontakt `H`.** Der Renderer sendet dabei kein Trefferereignis; der vorhandene Servercode entscheidet bereits über den Effekt. Bei einem Sprung bleibt ein späterer tatsächlicher Bodenkontakt zusätzlich Aufgabe der Entity-KI.

Die Standardüberblendung beträgt drei Ticks hinein und vier Ticks hinaus. Sie bleibt am Trefferpunkt vollständig aktiv und endet wieder in der aktuellen Bewegungs-/Ruhepose. Der Modellspieler blendet nur die vom Aktionsclip tatsächlich belegten Kanalgruppen Position, Rotation und Skalierung. Andere Knochen und nicht betroffene Kanalgruppen behalten die aktuelle Grundbewegung. Es werden keine zwei vollständigen Posen unkontrolliert addiert.

Quellclips mit voller Drehung behalten ihre Eulerwinkel; die Laufzeit kürzt sie nicht auf den kürzesten Winkelweg. Schleifende Aktionsclips werden für eine Aktion als einmalige Wiedergabe behandelt. Statische Quell-`SCALE`-Clips mit Länge null werden stets bei Zeit null ohne Schleifenmodulo ausgewertet. So entsteht kein Modulo-durch-null-Fehler.

Der bisherige `DungeonMob` entfernt sich nach 20 Todticks. Ein deklarierter Todclip wird in dieses bestehende Fenster eingepasst. Der Renderer schaltet dann die zusätzliche Vanilla-Todrotation ab, damit nicht zwei Fallbewegungen gleichzeitig wirken.

Jeder Frame beginnt bei der gespeicherten Bindepose; statische Boneskalen werden erneut gesetzt. Dadurch bleibt keine Haltung einer zuvor mit demselben Renderer gezeichneten Entity zurück. Poseabhängige Sichtbarkeit, etwa gefaltete oder getrennte Illagerarme, wird ausdrücklich für den jeweiligen Frame gesetzt.

## 4. CEM-Ausdrücke und Vanilla-Hosts

### Strikte Bindung

`RigExpression` ist ein begrenzter mathematischer Parser, keine Script-Engine. Er besitzt keinen Datei-, Netzwerk- oder Codeausführungszugang. Begrenzungen: 4096 Zeichen je Ausdruck, 1024 AST-Knoten und 64 Verschachtelungsebenen. Nicht endliche Ergebnisse werden abgelehnt.

Unterstützt sind Zahlen, Konstanten `pi/true/false`, Arithmetik, Vergleiche, `&&`, `||`, `!`, ternäre Bedingungen sowie die Funktionen `sin`, `cos`, `tan`, `asin`, `acos`, `atan`, `atan2`, `torad`, `todeg`, `abs`, `floor`, `ceil`, `round`, `sqrt`, `exp`, `log`, `signum`, `frac`, `pow`, `clamp`, `between`, `min`, `max`, `in` und `if`. Bedingungen werten unbenutzte Zweige nicht aus. Die Trigonometrie arbeitet in **Radiant**, wie die übernommenen CEM-Formeln; Molang-Ausdrücke werden nicht pauschal als CEM interpretiert.

Explizite Frameeingänge:

```text
age, time, limb_swing, limb_speed, head_yaw, head_pitch,
hurt_time, death_time, health, max_health, swing_progress,
is_in_water, is_on_ground, is_alive, is_aggressive,
is_ridden, is_riding, is_sitting, is_child
```

Bonekomponenten: `tx/ty/tz`, `rx/ry/rz`, `sx/sy/sz`, `visible`, `visible_boxes`. `render.shadow_size` wird ebenfalls unterstützt. Unbekannte Eingangsvariablen, Komponenten und Bones führen zu einem Fehler; sie werden nicht als null weitergerechnet.

### Host und eigener Bone sind verschieden

Die Konverter erhalten eine unsichtbare tatsächliche Vanilla-Hosthierarchie. CEM-Wrapper hängen an ihren korrekten Eltern; die ursprünglichen Pivots und relativen Transformationen werden nicht durch geschätzte Standardwerte ersetzt.

Die Auflösung folgt einem ausdrücklich dokumentierten Vertrag:

1. Ein Bezug `EMF_<id>` bezeichnet direkt den Custombone `<id>` und umgeht die Hostaliasmap.
2. Ein unpräfixierter Name verwendet einen vorhandenen Vanilla-Hostalias zuerst.
3. Nur ohne passenden Hostalias wird der gleichnamige Custombone verwendet.

Das gilt gleichermaßen für Ausdrücke und Zuweisungsziele. Der Konverter ersetzt das lexikalische CEM-`this` pro Quellmodell durch `EMF_<eigene_id>`. Es gibt keinen globalen `this`-Alias. So verweist etwa ein eigenes `body` nicht versehentlich auf den gleichnamigen Vanilla-Körper.

Beispiele für besondere Hostabbildungen:

- Warden: CEM `body/root` entspricht Vanilla `bone`, CEM `torso` entspricht dem darunterliegenden Vanilla `body`.
- Warden-Quellclips verwenden teilweise `left_ear/right_ear`, während das installierte Modell die Kopfanhänge `left_tendril/right_tendril` nennt. Die Zuordnung erfolgt explizit auf diese echten Knoten; die Tracks werden nicht verworfen.
- Guardian: `body` entspricht dem Vanilla-Kopf, `tail1..3` entsprechen den drei tatsächlichen Schwanzabschnitten.
- Wither: `body1..3` entsprechen Schulter, Brustkorb und Schwanz; `head1..3` den drei Köpfen.

### Grundbewegungen und eigene Adaptionen

Die unterstützten Hostfamilien umfassen Warden, Guardian, Wither und Slime sowie Chicken, Cow/Pig/Sheep/Goat, Cat, Turtle, Fox, Rabbit, Wandering Trader/Villager, Illager, Skeleton/Stray, Zombie/Husk, Phantom, Allay, Squid/Glow Squid, Parrot, Silverfish und Ravager. Cat erhält die tatsächliche native Sitzpose; Turtle unterscheidet Wasserpaddeln und Bodenbewegung. `armored_pillager` und `armored_vindicator` sind ausdrückliche Varianten mit dauerhaft getrennten Armen; ein nicht vorhandener gefalteter `arms`-Bone wird dort nicht erfunden.

Normale Gang-, Blick- und Sitzposen folgen den Formeln der installierten Minecraft-Modelle. Ein unabhängiger `PathfinderMob` besitzt jedoch beispielsweise keinen privaten Vanilla-Chicken-Flapcounter oder Squid-Tentakelcounter. Für solche Zähler verwendet der Client begrenzte visuelle Uhren, abgeleitet aus tatsächlicher Bewegung, Wasser- und Bodenkontakt. Dadurch entstehen keine Sprünge, Flugbahnen oder Wasserzustände auf dem Server.

Warden-basierte Bosskörper können die installierten Warden-Keyframeclips als ausdrücklich eigene Kampfadaption nutzen. Diese werden auf die vorhandenen Serveraktionen abgebildet. Sie sind keine Behauptung, dass die originalen Jungle-Abomination-/Tempest-Golem-Kampfclips aus Minecraft Dungeons übernommen wurden. Das stationäre Kesselmodell erhält insbesondere keine fremde Ganzkörper-Schleimverformung.

## 5. Uhren, passive Gesten und der Feldführer

| Darstellung | Uhr |
| --- | --- |
| Java-Keyframe-Idle und additive Bewegungen | Entityalter `age`, einschließlich Teil-Tick. |
| Gehclips | Tatsächliches `limbSwing` und `limbAmount`. |
| CEM `age` | Entityalter. |
| CEM `time` in der Welt | `gameTime % 24000` plus Teil-Tick. |
| Animierte PNG-Folgen in der Welt | Volle `gameTime` plus Teil-Tick; Ganzzahlindex ohne Float-Verlust bei großen Weltzeiten. |
| Eindeutig lokale Feldführer-Vorschau | Lokales `age` auch für CEM `time` und PNG-Folgen. Dadurch läuft die Vorschau bei pausiertem Singleplayer weiter. |

Eine passive Entity liefert ihre synchronisierte Geste über einen eigenen `ReferenceAnimationSource<T>`:

```java
@Override
public Gesture gesture(MyPassiveMob mob, float partialTick) {
    return mob.hasGreeting()
        ? new Gesture("GREETING", mob.greetingTicks(partialTick), 40)
        : null;
}
```

Die Geste benennt einen vorhandenen Clip. Ihre Zeit wird auf dessen gesamte Länge abgebildet; standardmäßig blendet sie drei Ticks hinein und vier Ticks hinaus. Eine vorhandene gebundene Kampfaktion hat Vorrang. Der Adapter liest Zustand und verändert weder Inventar noch KI oder Handelsdaten.

Für den Feldführer existiert `ReferencePreviewState`:

```java
ReferencePreviewState.setGesture(previewMob, "GREETING", elapsedTicks, durationTicks);
ReferencePreviewState.clear(previewMob);
```

Die Vorschau wird nur für `level.isClientSide && level.getEntity(id) != entity` akzeptiert. Eine echte registrierte Weltentity kann darüber keine fingierte Geste erhalten. Die WeakHashMap hält geschlossene Vorschauen nicht dauerhaft fest. Der Modellspieler bevorzugt eine zulässige lokale Vorschaugeste vor dem synchronisierten Gestenadapter; Common-NBT und echte Weltzustände werden nicht verändert.

## 6. Gegenstände und mehrere Materialien

### Ausrüstung bleibt an den animierten Händen

`ModelBonePath` bindet einen **vollständigen Pfad unmittelbar unter dem tatsächlich gebackenen Root**. Ein führender Slash ist erlaubt. Ein synthetisches `root/` wird nicht automatisch entfernt, weil ein Quellmodell durchaus einen echten Bone namens `root` besitzen kann.

Beispiele vorhandener Bindungen:

```text
Native Hauptleute/Goety-Humanoide: right_arm
Port-Armored-Illager:            body/right_arm
Bedrock-Illusioner:              bedrock_source_root/bipedBody/bipedArms/bipedArmRight/bipedHandRight
```

Der Itemlayer multipliziert jede übergeordnete Position, Rotation und Skalierung, nicht bloß den letzten Handbone. Unsichtbare Eltern verbergen auch das gehaltene Item. MAINHAND/OFFHAND und dominante Hand werden vom echten Mob gelesen. Meshwaffen wie bei Royal Guard und Vanguard erhalten keine zusätzliche Itembindung und dadurch keine Doppelwaffen.

`palmAnchor=false` verwendet den üblichen Vanilla-Armanker mit Unterarmlängenversatz. `true` bezeichnet einen bereits bis zur Hand verschobenen Quellanker und lässt diesen Längenversatz aus. Die Bindung enthält keinen künstlichen Itemstack; tatsächliche Ausrüstung setzt der Common-/Servercode.

### Eigenes Material, gleiche Pose

`materialLayer(texture, factory)` ergänzt beispielsweise Fell oder eine Vanilla-Grundtextur unter einer separaten Rüstungstextur. Die Fabrik liefert dieselbe vollständige Knochenhierarchie mit nur den Würfeln dieses Materials.

`ModelPoseCopy` verknüpft exakt gleiche Elternpfade. Vor jedem Draw werden Position, Eulerrotation, Skalierung, `visible` und `skipDraw` vom Master kopiert. Auch leere Zwischenpivots werden berücksichtigt. Ein Material mit einem fehlenden Masterpfad scheitert ausdrücklich. Der Pass verwendet normale Beleuchtung, Cutout ohne Culling und den aktuellen Schadensoverlay.

### Leuchten und Framefolgen

Ein statisches `.emissive` verwendet den unbeleuchteten additiven Vanilla-`eyes`-Pass. Seine Intensität wird über RGB geregelt, weil Alpha diesen Pass nicht wie einen transparenten Materialpass abdunkelt. `.overlay(..., true)` setzt dagegen das Licht auf Vollhelligkeit; das ist kein Ersatz für einen unbeleuchteten Augenpass.

Ein animiertes Overlay ergänzt den Prefix exakt mit `0.png` bis `frameCount - 1` und verwendet `floor(clock / ticksPerFrame) % frameCount`. Beispiel:

```java
.animatedOverlay(
    "dungeonsalive:textures/entity/reference/goety/servants/flame_construct/fire/",
    31, 1, 1.0F, true)
```

`fullBright=true` verwendet `entityTranslucentEmissive` mit vollständigem Licht; das entspricht dem Shader-/Misch-/Schreibmaskentyp des geprüften Goety-Feuerpasses. Bei `false` wird `entityTranslucent` mit Weltlicht verwendet. Texturalpha und der deklarierte Layeralpha bleiben erhalten. Unsichtbare oder sterbende Entities zeichnen diese laufenden Effektfolgen nicht.

## 7. Vier native Kompositionen

| Entity | Darstellung | Konkrete Grenze |
| --- | --- | --- |
| Pillager Raid Captain | Nativer Illager-Rig, installierte Pillagertextur, tatsächliche Armbrust/Offhand und echtes ausgerüstetes Hauptmannbanner. Armbrust-Haltepose auch im Idle. | Der Server muss die Ausrüstung setzen; der Renderer erfindet keine Loot-/Inventargegenstände. |
| Vindicator Raid Captain | Nativer Illager-Rig, installierte Vindicatortextur, tatsächliche Axt und Banner, gefaltete Ruhearme und synchronisierter Schlag. | Schlaginterpolation ist eine Anpassung an die vorhandene `AttackKind`-Laufzeit. |
| Skeleton Horseman | Vollständiger nativer HorseModel-Rig mit installierter Skelettpferdtextur, eigener nativer SkeletonModel-Reiter und tatsächlich gehaltener Bogen. Der Sitz folgt dem animierten Pferderücken. | Eine gemeinsame Gameplay-Entity; Absteigen, getrennte Pferdgesundheit und Zweientitäten-Mount-KI sind nicht implementiert. |
| Conjured Slime | Nativer innerer SlimeModel-Körper plus echter transparenter `SlimeOuterLayer`. Unveränderte geprüfte MIT-Textur aus DungeonsMobs-Port. Verformung folgt Bewegung, Vertikalgeschwindigkeit, LEAP-/MELEE-Timing und Hurtzustand. | Die vorhandene Gameplay-Entity bleibt `DungeonMob`; hierdurch wird keine vollständige Vanilla-Slime-KI importiert. |

Die Hauptleute verbergen die unbenutzte native Hutbox ausdrücklich, wie es der Vanilla-IllagerModel-Konstruktor tut. Die Banner verwenden den tatsächlichen HEAD-Stack. Vanilla-PNGs werden für diese Kompositionen nicht kopiert; die ResourceLocations zeigen auf die installierten Minecraft-Ressourcen.

## 8. Messung und Prüfungen

`ReferenceModels.bounds(id)` liefert Breite, Höhe, Tiefe und Mittelpunkt der Ruhegeometrie in Weltblöcken. Gemessen werden tatsächlich gerenderte Vertices einschließlich Cube-Aufweitung, Pivots, Rotation, Quellskalen und sichtbarer Materiallayer. Horizontale Grenzen werden um den Entityursprung symmetrisch erweitert, damit asymmetrische Geometrie nicht abgeschnitten wird.

Das sind **Ruhegrenzen ohne vollständige Aktionsreserve**. Ein Welt-Showcase muss für Sprünge, weit ausgreifende Schläge und Kameraränder zusätzliche Reserve verwenden. Für die nativen Kompositionen ist eine ausdrücklich konservative Hülle hinterlegt, die externe Banner, Reiter, Waffen beziehungsweise die äußere Schleimhülle einbezieht.

Die Tests prüfen unterschiedliche reale Risiken:

- `ReferenceAnimationTest`: Trefferzeit, echte ModelPart-Kontaktpose, Rückkehr in Bewegung/Idle, Null-Längen-Skalen und fehlende Bones.
- `RigExpressionTest`: übernommene Formeln, Radiantsemantik, Short-Circuit-Bedingungen, Aliaspräzedenz, lexikalischer EMF-Custombezug und fehlende Eingänge.
- `CemHostAnimatorTest`: diagonale Beinpaare, begrenztes Flattern, Sitzpose ohne Zustandstransfer, gefaltete/getrennte Arme und tatsächliche Reit-/Sitzflags.
- `ModelBonePathTest`: Gegenstände folgen animierten, skalierten Eltern; ungültige Pfade werden abgelehnt.
- `MaterialLayerTest`: zusätzliche Geometrie folgt Pose und Sichtbarkeit und wird in Bounds aufgenommen.
- `AnimatedOverlayTest`: Framewechsel, Schleifengrenzen und lange Weltuhren.
- `ReferenceAssetRigTest`: **alle registrierten Import- und Builtin-Rigs** werden mittels `assertAll` getrennt geprüft. Ein fehlerhafter Quellrig stoppt nicht die übrige Prüfung. Gebundene Clips und tatsächlich registrierte CEM-Profilaktionen werden an Anfang, Serverkontakt und Ende ausgewertet.

Eine grüne Headless-Prüfung belegt die strukturelle und mathematische Gültigkeit; sie beweist keine korrekte Silhouette, Itemausrichtung, Alphaoptik oder überzeugende Bewegung. Änderungen brauchen zusätzlich echte Clientaufnahmen in der Welt. Der konkrete zentrale Build-, Server- und Grafikstand gehört nach [VALIDATION.md](VALIDATION.md), nicht als ungeprüfte Vollständigkeitsbehauptung in den Modellkatalog.

## 9. Änderungen fortsetzen

1. Konkretes Assetbündel und Bedingungen im Herkunftsaudit prüfen und festhalten.
2. Die maßgeblichen Importmanifeste beziehungsweise Konverter ändern; generierte Modelldateien nicht als unabhängige zweite Quelle pflegen.
3. Quellpivots, Texturgrößen, Materialsplits und tatsächliche Bonepfade erhalten. Fehlende Tracks nur durch ausdrücklich dokumentierte Quellkorrekturen entfernen; keine allgemeine Ignore-Regel einbauen.
4. Quellkontaktzeiten prüfen und die Bindung mit dem vorhandenen Serverereignis abgleichen. Mechanisch unpassende Clips als Adaption benennen.
5. Zentralen Build und Rigtests ausführen. Bei Common-/Registrierungsänderungen zusätzlich die vorhandenen Server-GameTests ausführen.
6. Die betroffenen Figuren im echten Client ansehen: Ruhe, Bewegung, Aktion, Ausrüstung, Leucht-/Materialpass und gegebenenfalls Schwimmen/Geste.
7. Herkunft, tatsächliche Darstellung, Kampffunktionen und ausstehende Abnahme im Katalog getrennt halten.
