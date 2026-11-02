#!/usr/bin/env python3
"""Generate original, short synthetic duck contact calls; no recordings are inputs.

The deterministic WAV oscillator bank is authored for this project. oggenc only
encodes it to Minecraft's Ogg Vorbis format. Never overwrites unrelated sounds.
"""
from __future__ import annotations

import argparse
import hashlib
import json
import math
from pathlib import Path
import random
import shutil
import struct
import subprocess
import tempfile
import wave

ROOT = Path(__file__).resolve().parents[1]
RATE = 44100


def synthesize(variant: int) -> bytes:
    noise = random.Random(20261102 + variant)
    duration = .29 + variant * .018
    phase = 0.0
    samples = []
    filtered_noise = 0.0
    for index in range(int(RATE * duration)):
        t = index / RATE
        progress = t / duration
        frequency = (225 - 70 * progress + 8 * math.sin(2 * math.pi * 28 * t)) * (1 - .035 * variant)
        phase += 2 * math.pi * frequency / RATE
        # Breathy reed-like voice, with bounded nasal formants rather than a pure beep.
        voice = 0.0
        norm = 0.0
        for harmonic in range(1, 17):
            hz = harmonic * frequency
            formant = (math.exp(-((hz - 780) / 260) ** 2)
                       + .65 * math.exp(-((hz - 1510) / 430) ** 2)
                       + .12)
            weight = formant / math.sqrt(harmonic)
            voice += math.sin(phase * harmonic) * weight
            norm += weight
        filtered_noise = .55 * filtered_noise + .45 * noise.uniform(-1, 1)
        envelope = min(1.0, t / .012) * (1 - progress) ** .8
        envelope *= .86 + .14 * math.sin(2 * math.pi * 34 * t)
        value = envelope * (.78 * voice / norm + .06 * filtered_noise)
        samples.append(max(-32767, min(32767, round(value * 32767))))
    return struct.pack('<' + 'h' * len(samples), *samples)


def main() -> None:
    parser = argparse.ArgumentParser(description=__doc__)
    installed = Path('/opt/homebrew/opt/vorbis-tools/bin/oggenc')
    parser.add_argument('--oggenc', default=shutil.which('oggenc') or (str(installed) if installed.is_file() else None))
    args = parser.parse_args()
    if not args.oggenc:
        parser.error('A local Xiph vorbis-tools oggenc installation is required for mono Ogg encoding')
    output = ROOT / 'src/main/resources/assets/dungeonsalive/sounds/wildlife'
    output.mkdir(parents=True, exist_ok=True)
    entries = []
    with tempfile.TemporaryDirectory(prefix='dungeons-duck-audio-') as temporary:
        for variant in range(3):
            source = Path(temporary) / f'contact_{variant + 1}.wav'
            with wave.open(str(source), 'wb') as wav:
                wav.setnchannels(1)
                wav.setsampwidth(2)
                wav.setframerate(RATE)
                wav.writeframes(synthesize(variant))
            target = output / f'duck_contact_{variant + 1}.ogg'
            subprocess.run([args.oggenc, '--quiet', '--quality', '4', '--serial', str(20261102 + variant),
                            '--output', str(target), str(source)], check=True)
            entries.append({'file': target.relative_to(ROOT).as_posix(),
                            'sha256': hashlib.sha256(target.read_bytes()).hexdigest(),
                            'kind': 'original_synthetic_audio', 'license': 'MIT'})
    manifest = {'author': 'LaugenBreze / Dungeons Alive project', 'license': 'MIT',
                'source': 'tools/generate_duck_sounds.py', 'external_recordings': [],
                'description': 'Original oscillator/formant contact calls; not extracted Dungeons audio.',
                'sample_rate': RATE, 'channels': 1, 'files': entries}
    (ROOT / 'design/duck-audio-provenance.json').write_text(json.dumps(manifest, indent=2) + '\n')
    assets = ROOT / 'src/main/resources/assets/dungeonsalive'
    sounds_path = assets / 'sounds.json'
    sounds = json.loads(sounds_path.read_text()) if sounds_path.exists() else {}
    sounds['entity.duck.contact'] = {
        'subtitle': 'subtitles.dungeonsalive.duck_contact',
        'sounds': [f'dungeonsalive:wildlife/duck_contact_{index}' for index in range(1, 4)]}
    sounds_path.write_text(json.dumps(sounds, ensure_ascii=False, indent=2) + '\n')
    for locale, label in [('de_de', 'Ente ruft'), ('en_us', 'Duck calls')]:
        language_path = assets / 'lang' / (locale + '.json')
        language = json.loads(language_path.read_text())
        language['subtitles.dungeonsalive.duck_contact'] = label
        language_path.write_text(json.dumps(language, ensure_ascii=False, indent=2) + '\n')
    print(f'Generated {len(entries)} original mono contact calls')


if __name__ == '__main__':
    main()
