# Optionaler Wilds-Legacy-Override

Der aktuelle Mod verteilt bereits 20 ausgewählte reguläre Gegner über natürliche Biome.
Dieses ältere Pack ist **nicht mehr erforderlich**, damit Dungeons-Gegner natürlich erscheinen.

Wer bewusst die früheren sehr niedrigen Gewichte möchte, kann dieses Pack weiterverwenden.
Seine sechs Ressourcen-IDs (`woodland`, `desert`, `jungle`, `peaks`, `swamp`, `outer_end`)
sind identisch mit den eingebauten BiomeModifier-IDs. Minecraft ersetzt diese Einträge;
es addiert nicht einen zweiten Satz derselben Gegner. Das Pack behält außerdem seine
früheren engeren Biomelisten. Die neuen Familien `frozen`, `nether` und `aquatic` bleiben
von diesem Legacy-Override unberührt.

Der gemeinsame Serverschalter `[hostiles] naturalHostiles` in
`config/dungeonsalive-hostiles-common.toml` blockiert neue natürliche Dungeons-Gegner
auch bei aktiviertem Legacy-Pack. Bestehende Gegner und manuelle Begegnungen bleiben.
Bosse und Minibosse gehören weder zum eingebauten Naturbestand noch zu diesem Pack.

Die Dateien dieses Packs sind historische Konfigurationsvorlagen; sie sind kein Nachweis
für einen ausgeführten Minecraft-Spawn- oder Sichttest.
