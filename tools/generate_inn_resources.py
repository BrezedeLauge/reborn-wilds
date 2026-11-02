#!/usr/bin/env python3
"""Own inn data and text. Native texture references; no copied game artwork."""
import json
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
OUT = ROOT / 'src/main/resources'

def write(path, value):
    target = OUT / path
    target.parent.mkdir(parents=True, exist_ok=True)
    if path.startswith('data/minecraft/tags/') and target.exists():
        existing = json.loads(target.read_text())
        value['values'] = list(dict.fromkeys(existing.get('values', []) + value['values']))
    target.write_text(json.dumps(value, ensure_ascii=False, indent=2) + '\n')

write('assets/dungeonsalive/blockstates/inn_ledger.json', {'variants': {
    'facing=' + direction: {'model': 'dungeonsalive:block/inn_ledger', 'y': rotation}
    for direction, rotation in [('north',0),('east',90),('south',180),('west',270)]}})
write('assets/dungeonsalive/models/block/inn_ledger.json', {'parent':'minecraft:block/lectern'})
write('assets/dungeonsalive/models/item/inn_ledger.json', {'parent':'dungeonsalive:block/inn_ledger'})
write('assets/dungeonsalive/models/item/inn_marker.json', {'parent':'minecraft:item/generated','textures':{'layer0':'minecraft:item/map'}})
for name, ingredients in [('inn_ledger',['minecraft:lectern','minecraft:emerald']),('inn_marker',['minecraft:paper','minecraft:charcoal'])]:
    write('data/dungeonsalive/recipe/' + name + '.json', {'type':'minecraft:crafting_shapeless','category':'misc',
        'ingredients':[{'item':item} for item in ingredients], 'result':{'id':'dungeonsalive:'+name,'count':1}})
write('data/dungeonsalive/loot_table/blocks/inn_ledger.json', {'type':'minecraft:block','pools':[{'rolls':1,
    'conditions':[{'condition':'minecraft:survives_explosion'}], 'entries':[{'type':'minecraft:item','name':'dungeonsalive:inn_ledger'}]}]})
# Merge this value with the production tag, never replace its existing entries.
write('data/minecraft/tags/block/mineable/axe.json', {'replace':False,'values':['dungeonsalive:inn_ledger']})
write('data/dungeonsalive/dungeonsalive/quests/a_welcoming_inn.json', {
    'schema_version':1,'title':'Ein Haus für Reisende',
    'description':'Baue dein eigenes Gasthaus. Markiere mit dem Gasthausplan zuerst das Pult, dann zwei gegenüberliegende Raumecken (Boden bis Decke), den freien Eingang, eine eigene Vorratskiste oder ein Fass und einen Ankunftspunkt außerhalb. Bestätige am Pult. Eine zugängliche untere Holzstufe als Sitzbank, ein Bett, einfache Nahrung und ein echter Weg ermöglichen den Besuch. Der Gast muss selbst ankommen; Zimmer und Zugang werden bei der Abholung erneut geprüft. Du entscheidest über Form, Ort und Brückenbau.',
    'icon':'dungeonsalive:inn_ledger','order':45,'prerequisites':[],
    'objectives':[
        {'id':'usable_room','type':'world_state','target':'dungeonsalive:inn_ready','amount':1,'label':'Eigenes Gasthaus mit nutzbarem Sitz, Bett und Vorrat'},
        {'id':'real_access','type':'world_state','target':'dungeonsalive:inn_connected','amount':1,'label':'Begehbarer Weg vom Ankunftspunkt zum Eingang'},
        {'id':'first_guest','type':'world_state','target':'dungeonsalive:guest_arrived','amount':1,'label':'Ein echter Gast ist angekommen'}],
    'rewards':[{'item':'minecraft:lantern','count':4},{'item':'minecraft:emerald','count':6}],'experience':40})

text = {
'block.dungeonsalive.inn_ledger':('Gasthauspult','Inn ledger'),
'item.dungeonsalive.inn_marker':('Gasthausplan','Inn plan'),
'item.dungeonsalive.inn_marker.description':('Rechtsklick: Pult → Raumecken → Eingang → Vorrat → Ankunft. Am Pult bestätigen.','Right-click: ledger → room corners → entrance → supplies → arrival. Confirm at the ledger.'),
'inn.dungeonsalive.not_owner':('Dieses Gasthauspult gehört einem anderen Spieler.','This inn ledger belongs to another player.'),
'inn.dungeonsalive.bind_first':('Markiere zuerst dein Gasthauspult mit dem Plan.','First mark your own inn ledger with the plan.'),
'inn.dungeonsalive.return_to_ledger':('Der Plan ist vollständig. Kehre zum Pult zurück und bestätige ihn.','Your plan is complete. Return to the ledger to confirm it.'),
'inn.dungeonsalive.invalid_selection':('Dieser Punkt ist nicht erreichbar oder außerhalb des erlaubten Bereichs.','This point is out of reach or outside the allowed area.'),
'inn.dungeonsalive.bounds_limit':('Der Raum darf höchstens 32 × 16 × 32 Blöcke umfassen. Markiere die zweite Ecke erneut.','The room must fit within 32 × 16 × 32 blocks. Mark the second corner again.'),
'inn.dungeonsalive.step.0':('Pult gewählt. Markiere die erste Raumecke am Boden.','Ledger selected. Mark the first room corner at floor level.'),
'inn.dungeonsalive.step.1':('Markiere die gegenüberliegende Ecke auf Deckenhöhe. Pult, Sitz, Bett und Vorrat müssen im Bereich liegen.','Mark the opposite corner at ceiling height. Include the ledger, seat, bed and supplies.'),
'inn.dungeonsalive.step.2':('Markiere den Boden direkt im Eingang: Der freie Platz darüber ist das Ziel.','Mark the floor inside the entrance: the free space above it is the target.'),
'inn.dungeonsalive.step.3':('Markiere genau eine Kiste oder ein Fass als Gastvorrat. Nur daraus wird Nahrung genommen.','Mark one chest or barrel for guest supplies. Food is taken only from this container.'),
'inn.dungeonsalive.step.4':('Markiere den Boden am Ankunftspunkt: außerhalb des Raums, 4–24 Blöcke vom Eingang entfernt.','Mark the arrival floor outside the room, 4–24 blocks from the entrance.'),
'inn.dungeonsalive.step.5':('Plan vollständig. Am Pult prüfen und bestätigen. Schleichen + Pult markiert neu.','Plan complete. Review and confirm at the ledger. Sneak-click the ledger to start again.'),
'screen.dungeonsalive.inn.save':('Bereich bestätigen','Confirm area'),
'screen.dungeonsalive.inn.reset':('Auswahl verwerfen','Discard selection'),
'screen.dungeonsalive.inn.counts':('Sitzplätze %s   ·   Betten %s   ·   Nahrung %s','Seats %s   ·   Beds %s   ·   Food %s'),
'screen.dungeonsalive.inn.connection.true':('Zugang: begehbarer Weg gefunden','Access: walkable route found'),
'screen.dungeonsalive.inn.connection.false':('Zugang: noch kein begehbarer Weg bestätigt','Access: no walkable route confirmed yet'),
'screen.dungeonsalive.inn.problem':('Prüfe den Bereich bei %s','Check the area near %s'),
'screen.dungeonsalive.inn.plan':('Deine neue Bereichsauswahl','Your new area selection'),
'screen.dungeonsalive.inn.first':('Raumecke 1: %s','Room corner 1: %s'),
'screen.dungeonsalive.inn.second':('Raumecke 2: %s','Room corner 2: %s'),
'screen.dungeonsalive.inn.entrance':('Eingang: %s','Entrance: %s'),
'screen.dungeonsalive.inn.stock':('Gastvorrat: %s','Guest supplies: %s'),
'screen.dungeonsalive.inn.arrival':('Ankunft: %s','Arrival: %s'),
'screen.dungeonsalive.inn.help':('Mit dem Gasthausplan markieren. Erst die Bestätigung gibt den gewählten Vorrat für Gäste frei.','Mark with the inn plan. Confirmation allows guests to use the selected supplies.'),
}
states = {
'unconfigured':('Noch kein Gasthaus eingetragen','No inn configured'), 'scanning':('Raum wird geprüft …','Checking the room …'),
'ready':('Bereit für Reisende','Ready for visitors'), 'unloaded':('Ein Teil des Bereichs ist nicht geladen','Part of the area is not loaded'),
'missing_seat':('Ein erreichbarer Sitzplatz fehlt','An accessible seat is missing'), 'missing_bed':('Ein nutzbares Bett fehlt','A usable bed is missing'),
'missing_stock':('Die zugewiesene Vorratskiste fehlt','The selected supply container is missing'), 'no_food':('Lege einfache Nahrung in den Gastvorrat','Add ordinary food to guest supplies'),
'blocked_route':('Ein Weg ist blockiert oder nicht begehbar','A route is blocked or not walkable'), 'not_owner':('Dieses Pult gehört dir nicht','You do not own this ledger')}
guests = {'none':('Noch kein Gast unterwegs','No guest travelling'), 'reserved':('Ein Gast bereitet die Anreise vor','A guest is preparing to visit'),
'travelling':('Ein Gast ist auf dem Weg','A guest is on the way'), 'arrived':('Ein Gast ist angekommen','A guest has arrived'),
'resting':('Ein Gast ruht sich aus','A guest is resting'), 'leaving':('Ein Gast reist ab','A guest is leaving'), 'unloaded':('Gast außerhalb der geladenen Umgebung','Guest outside the loaded area')}
results = {'ok':('Bereich gespeichert','Area saved'), 'unavailable':('Gasthausverwaltung ist noch nicht bereit.','Inn management is not ready.'),
'not_owner':('Dieses Pult gehört dir nicht.','You do not own this ledger.'), 'out_of_reach':('Kehre zum Gasthauspult zurück.','Return to the inn ledger.'),
'no_rights':('Im markierten Bereich darfst du nicht bauen.','You cannot build in the marked area.'),
'invalid_selection':('Prüfe Raumgrenzen, Eingang und Ankunftspunkt.','Check room bounds, entrance and arrival point.'),
'invalid_stock':('Der Vorrat muss eine einzelne Kiste oder ein Fass sein.','Supplies must be a single chest or barrel.'),
'unloaded':('Ein Teil des gewählten Bereichs ist nicht geladen.','Part of the selected area is not loaded.'),
'stale_revision':('Die Auswahl ist veraltet. Markiere den Bereich neu.','The selection is outdated. Mark the area again.'),
'busy':('Warte, bis der derzeitige Gast abgereist ist.','Wait until the current guest has left.'),
'world_limit':('Die Welt hat bereits 64 eingetragene Gasthäuser.','The world already has 64 registered inns.'),
'owner_limit':('Du hast bereits drei eingetragene Gasthäuser.','You already have three registered inns.'),
'overlap':('Der Bereich überschneidet ein anderes Gasthaus.','The area overlaps another inn.'),
'stock_assigned':('Dieser Vorrat gehört bereits zu einem Gasthaus.','These supplies are already assigned to an inn.'),
'data_blocked':('Die gespeicherten Gasthausdaten müssen geprüft werden.','Saved inn data needs checking.')}
for group, values in [('screen.dungeonsalive.inn.status.',states),('screen.dungeonsalive.inn.guest.',guests),('inn.dungeonsalive.result.',results)]:
    text.update({group+key:value for key,value in values.items()})
for index, language in enumerate(['de_de','en_us']):
    target=OUT/'assets/dungeonsalive/lang'/f'{language}.json'
    values=json.loads(target.read_text()) if target.exists() else {}
    values.update({key:value[index] for key,value in text.items()})
    target.parent.mkdir(parents=True,exist_ok=True)
    target.write_text(json.dumps(values,ensure_ascii=False,indent=2)+'\n')
print(json.dumps({'resources':len(list(OUT.rglob('*.json'))),'translation_keys':len(text),'source':'native minecraft: references; own code/data/text'}))
