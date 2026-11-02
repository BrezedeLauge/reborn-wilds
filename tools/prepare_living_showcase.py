#!/usr/bin/env python3
"""Create a fresh marked developer world from the project's actual living-world preset.

No existing save, launcher account, jar or installed instance is read or modified.
The native client still has to decode this metadata and generate/render the terrain.
"""
from pathlib import Path
import gzip
import json
import struct
import tempfile

ROOT = Path(__file__).resolve().parents[1]
TOKEN = 'isolated-living-showcase-world-v1'


def encode(kind, value):
    if kind == 1: return struct.pack('>b', value)
    if kind == 3: return struct.pack('>i', value)
    if kind == 4: return struct.pack('>q', value)
    if kind == 5: return struct.pack('>f', value)
    if kind == 6: return struct.pack('>d', value)
    if kind == 8:
        raw = value.encode('utf-8'); return struct.pack('>H', len(raw)) + raw
    if kind == 9:
        subtype, entries = value
        return bytes([subtype]) + struct.pack('>i', len(entries)) + b''.join(encode(subtype, item) for item in entries)
    if kind == 10:
        return b''.join(bytes([subtype]) + encode(8, key) + encode(subtype, item)
                        for key, (subtype, item) in value.items()) + b'\0'
    raise ValueError('Unsupported NBT type ' + str(kind))


def preset_nbt(value):
    if isinstance(value, dict): return (10, {key: preset_nbt(item) for key, item in value.items()})
    if isinstance(value, str): return (8, value)
    raise ValueError('Unexpected preset field type; review schema instead of guessing: ' + repr(value))


def main():
    parent = ROOT / 'build/living-showcase-worlds'
    if parent.resolve() != parent: raise SystemExit('Refusing symlinked development directory')
    parent.mkdir(parents=True, exist_ok=True)
    world = Path(tempfile.mkdtemp(prefix='world-', dir=parent))
    preset = json.loads((ROOT / 'src/main/resources/data/dungeonsalive/worldgen/world_preset/living_world.json').read_text())
    data = {
        'DataVersion': (3, 3955), 'version': (3, 19133), 'WasModded': (1, 1),
        'Version': (10, {'Name': (8, '1.21.1'), 'Id': (3, 3955), 'Snapshot': (1, 0), 'Series': (8, 'main')}),
        'LevelName': (8, 'Dungeons Alive Living Showcase'), 'GameType': (3, 1),
        'hardcore': (1, 0), 'allowCommands': (1, 1), 'initialized': (1, 0),
        # Only this freshly created disposable development preset; no existing save or EULA setting is changed.
        'confirmedExperimentalSettings': (1, 1),
        'Difficulty': (1, 2), 'DifficultyLocked': (1, 0),
        'Time': (4, 0), 'DayTime': (4, 6000), 'LastPlayed': (4, 0),
        'SpawnX': (3, 0), 'SpawnY': (3, 90), 'SpawnZ': (3, 0), 'SpawnAngle': (5, 0.0),
        'clearWeatherTime': (3, 1000000), 'raining': (1, 0), 'thundering': (1, 0),
        'rainTime': (3, 1000000), 'thunderTime': (3, 1000000),
        'GameRules': (10, {'doDaylightCycle': (8, 'false'), 'doWeatherCycle': (8, 'false'), 'doMobSpawning': (8, 'true')}),
        'DataPacks': (10, {'Enabled': (9, (8, ['vanilla', 'mod_data'])), 'Disabled': (9, (8, []))}),
        'enabled_features': (9, (8, ['minecraft:vanilla'])),
        'WorldGenSettings': (10, {'seed': (4, 20261102), 'generate_features': (1, 1), 'bonus_chest': (1, 0),
                                  'dimensions': preset_nbt(preset['dimensions'])}),
        'DragonFight': (10, {'NeedsStateScanning': (1, 1), 'DragonKilled': (1, 0), 'PreviouslyKilled': (1, 0)}),
    }
    raw = bytes([10]) + encode(8, '') + encode(10, {'Data': (10, data)})
    (world / 'level.dat').write_bytes(gzip.compress(raw, mtime=0))
    (world / 'dungeonsalive-living-showcase-source.txt').write_text(TOKEN + '\n')
    (world / 'preparation.json').write_text(json.dumps({'kind': 'fresh_isolated_developer_world',
        'preset': 'dungeonsalive:living_world', 'seed': 20261102, 'existing_save_read_or_modified': False,
        'native_decode_generation_and_rendering_executed': False,
        'experimental_development_preset_acknowledged': True}, indent=2) + '\n')
    print(world)


if __name__ == '__main__': main()
