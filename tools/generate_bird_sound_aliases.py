#!/usr/bin/env python3
"""Own bird subtitles/event assignments referencing Minecraft's installed parrot recordings."""
import json
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
ASSETS = ROOT / 'src/main/resources/assets/dungeonsalive'
CALLS = {
    'toucan': ('Tukan ruft', 'Toucan calls', .85),
    'golden_parrot': ('Goldpapagei ruft', 'Golden parrot calls', 1.0),
}


def main():
    path = ASSETS / 'sounds.json'
    sounds = json.loads(path.read_text())
    languages = {language: json.loads((ASSETS / 'lang' / (language + '.json')).read_text())
                 for language in ('de_de', 'en_us')}
    for bird, (german, english, pitch) in CALLS.items():
        subtitle = 'subtitles.dungeonsalive.' + bird + '.contact'
        sounds['entity.' + bird + '.contact'] = {
            'subtitle': subtitle,
            'sounds': [{'name': 'minecraft:entity.parrot.ambient', 'type': 'event', 'volume': .8, 'pitch': pitch}],
        }
        languages['de_de'][subtitle] = german
        languages['en_us'][subtitle] = english
    path.write_text(json.dumps(sounds, ensure_ascii=False, indent=2) + '\n')
    for language, entries in languages.items():
        (ASSETS / 'lang' / (language + '.json')).write_text(json.dumps(entries, ensure_ascii=False, indent=2) + '\n')
    (ROOT / 'design/bird-audio-provenance.json').write_text(json.dumps({
        'kind': 'own_event_aliases_to_installed_minecraft_audio',
        'minecraft_version': '1.21.1',
        'implementation_license': 'MIT for own Java/generator/event definitions only',
        'copied_game_recordings': [],
        'calls': {bird: {'source_event': 'minecraft:entity.parrot.ambient', 'pitch': data[2],
                         'original_dungeons_or_real_species_recording_claimed': False}
                  for bird, data in CALLS.items()},
        'departure_event': 'minecraft:entity.parrot.fly',
        'runtime': 'Real supported rest/actual takeoff transitions; leader-only contact voice and per-entity cooldowns',
        'limits': 'Raven and vulture gain quiet departure wings, not an invented parrot voice. Native event resolution and actual listening are separate checks.',
    }, ensure_ascii=False, indent=2) + '\n')
    print('Two bird contact aliases, DE/EN subtitles and provenance updated; no recordings copied.')


if __name__ == '__main__':
    main()
