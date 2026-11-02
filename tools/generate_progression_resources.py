#!/usr/bin/env python3
"""Own bilingual player-facing text for services, gear progression and living wildlife."""
import json
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
TEXT = {
    'block.dungeonsalive.key_gate': ('Key Golem Gate', 'Schlüsselgolem-Tor'),
    'block.dungeonsalive.diamond_key_gate': ('Diamond Key Golem Gate', 'Diamantschlüsselgolem-Tor'),
    'tooltip.dungeonsalive.key_gate': ('Only the placer can open this gate with their own %s within four blocks and in sight. The key is not consumed.',
                                     'Nur der Erbauer kann es mit einem eigenen %s in vier Blöcken Sichtweite öffnen. Der Schlüssel bleibt erhalten.'),
    'key_gate.dungeonsalive.unavailable': ('You cannot use this gate from here.', 'Du kannst dieses Tor von hier nicht benutzen.'),
    'key_gate.dungeonsalive.unassigned': ('This gate has no owner. Place it yourself to claim it.', 'Dieses Tor hat keinen Besitzer. Baue es selbst auf, um es zuzuordnen.'),
    'key_gate.dungeonsalive.not_owner': ('This gate belongs to another player.', 'Dieses Tor gehört einem anderen Spieler.'),
    'key_gate.dungeonsalive.requires_key_golem': ('Bring your Key Golem within four blocks, with a clear view of the gate.',
                                               'Bring deinen Schlüsselgolem auf vier Blöcke heran, mit freier Sicht auf das Tor.'),
    'key_gate.dungeonsalive.requires_diamond_key_golem': ('Bring your Diamond Key Golem within four blocks, with a clear view of the gate.',
                                                       'Bring deinen Diamantschlüsselgolem auf vier Blöcke heran, mit freier Sicht auf das Tor.'),
    'hud.dungeonsalive.souls': ('Souls %s / %s', 'Seelen %s / %s'),
    'hud.dungeonsalive.flames': ('Fire arrows %s / %s', 'Feuerpfeile %s / %s'),
    'enchantment.dungeonsalive.radiance': ('Radiance', 'Strahlen'),
    'enchantment.dungeonsalive.leeching': ('Leeching', 'Lebensraub'),
    'enchantment.dungeonsalive.committed': ('Committed', 'Hingabe'),
    'enchantment.dungeonsalive.critical_hit': ('Critical Hit', 'Kritischer Treffer'),
    'enchantment.dungeonsalive.chains': ('Chains', 'Ketten'),
    'enchantment.dungeonsalive.swirling': ('Swirling', 'Wirbel'),
    'enchantment.dungeonsalive.radiance.description': ('20% chance to heal 1/2/3 health on a hostile melee hit. One-second pause.',
                                                     '20% Chance, bei einem Gegnertreffer 1/2/3 Leben zu heilen. Eine Sekunde Pause.'),
    'enchantment.dungeonsalive.leeching.description': ('Hostile melee kills heal 3/6/9% of the defeated mob\'s maximum health, capped at 2/4/6 health.',
                                                     'Nahkampfsiege heilen 3/6/9% der maximalen Gegnergesundheit, höchstens 2/4/6 Leben.'),
    'enchantment.dungeonsalive.committed.description': ('Up to 10/20/30% extra melee damage as the hostile target loses health.',
                                                      'Bis zu 10/20/30% mehr Nahkampfschaden, je mehr Leben dem Gegner fehlt.'),
    'enchantment.dungeonsalive.critical_hit.description': ('5/10/15% chance for 1.75/2/2.25 times melee damage against hostile mobs.',
                                                         '5/10/15% Chance auf 1,75/2/2,25-fachen Nahkampfschaden gegen Gegner.'),
    'enchantment.dungeonsalive.chains.description': ('Slows up to eight visible foes within three blocks for 2/3/4 seconds. Two-second pause.',
                                                   'Bremst bis zu acht sichtbare Gegner in drei Blöcken Umkreis für 2/3/4 Sekunden. Zwei Sekunden Pause.'),
    'enchantment.dungeonsalive.swirling.description': ('Hits up to eight visible nearby additional foes for 2/3/4 magic damage. One-second pause.',
                                                     'Trifft bis zu acht sichtbare weitere Gegner in der Nähe für 2/3/4 Magieschaden. Eine Sekunde Pause.'),
    'screen.dungeonsalive.service.confirm': ('Pay & apply', 'Bezahlen'),
    'screen.dungeonsalive.service.goods': ('Goods', 'Waren'),
    'screen.dungeonsalive.service.item': ('Gear', 'Ausrüstung'),
    'screen.dungeonsalive.service.price': ('%s emeralds', '%s Smaragde'),
    'screen.dungeonsalive.service.preview': ('Preview', 'Vorschau'),
    'screen.dungeonsalive.service.blacksmith': ('Repair equipment', 'Ausrüstung reparieren'),
    'screen.dungeonsalive.service.powersmith': ('Upgrade a weapon', 'Waffe aufwerten'),
    'screen.dungeonsalive.service.enchantsmith': ('Enchant a weapon', 'Waffe verzaubern'),
    'screen.dungeonsalive.service.blacksmith.description': ('Repairs this item completely. Its name, enchantments and other properties stay with it.',
                                                          'Repariert vollständig. Name, Verzauberungen und weitere Eigenschaften bleiben erhalten.'),
    'screen.dungeonsalive.service.powersmith.description': ('Three ranks, +5% weapon damage each. Take your upgraded weapon out before another purchase.',
                                                          'Drei Ränge mit je +5% Waffenschaden. Nimm die aufgewertete Waffe nach dem Kauf heraus.'),
    'screen.dungeonsalive.service.status.empty': ('Insert your equipment.', 'Ausrüstung einlegen.'),
    'screen.dungeonsalive.service.status.unsupported': ('This service cannot alter that item.', 'Hier nicht bearbeitbar.'),
    'screen.dungeonsalive.service.status.maximum': ('Already fully repaired or upgraded.', 'Bereits vollständig verbessert.'),
    'screen.dungeonsalive.service.status.need_payment': ('Insert the shown emerald price.', 'Angezeigte Smaragde einlegen.'),
    'screen.dungeonsalive.service.status.ready': ('Ready. Review the result before paying.', 'Bereit. Ergebnis vor Kauf prüfen.'),
    'screen.dungeonsalive.service.status.collect_result': ('Take your finished item from the left slot.', 'Fertige Ausrüstung links entnehmen.'),
    'screen.dungeonsalive.service.status.unavailable': ('Service unavailable. Reopen the menu.', 'Dienst nicht verfügbar. Neu öffnen.'),
    'tooltip.dungeonsalive.gear_rank': ('Power rank %s / 3: +%s%% weapon damage', 'Kraftrang %s / 3: +%s%% Waffenschaden'),
}


def generate():
    for index, locale in enumerate(('en_us', 'de_de')):
        path = ROOT / f'src/main/resources/assets/dungeonsalive/lang/{locale}.json'
        data = json.loads(path.read_text())
        data.update({key: values[index] for key, values in TEXT.items()})
        path.write_text(json.dumps(data, ensure_ascii=False, indent=2) + '\n')
    print(f'Generated {len(TEXT)} bilingual progression entries.')


if __name__ == '__main__': generate()
