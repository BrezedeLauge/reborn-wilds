import hashlib
import importlib.util
import json
from pathlib import Path
import tempfile
import unittest

SPEC = importlib.util.spec_from_file_location('nature_gallery', Path(__file__).parents[1] / 'build_nature_gallery.py')
gallery = importlib.util.module_from_spec(SPEC)
SPEC.loader.exec_module(gallery)


class NatureGalleryIntegrityTest(unittest.TestCase):
    def fixture(self, directory):
        root = Path(directory)
        run = root / 'native'
        run.mkdir()
        image = b'\x89PNG\r\n\x1a\nfixture'
        (run / 'frame.png').write_bytes(image)
        manifest = {'status': 'failed', 'error': 'Cycle did not finish', 'cycles_complete': False,
                    'frames': [{'file': 'frame.png', 'sha256': hashlib.sha256(image).hexdigest(),
                                'server_snapshot': {'elapsed_tick': 40, 'sequence': 'ducks'}}]}
        (run / 'manifest.json').write_text(json.dumps(manifest))
        return root, run, manifest

    def test_failed_capture_stays_failed_and_original_is_unchanged(self):
        with tempfile.TemporaryDirectory() as directory:
            root, run, manifest = self.fixture(directory)
            before = (run / 'frame.png').read_bytes()
            result = gallery.payload(run, root / 'report.html')
            self.assertEqual(result['status'], 'failed')
            self.assertFalse(result['cycles_complete'])
            self.assertFalse(result['visual_review_performed'])
            self.assertEqual(result['frames'][0]['server_snapshot']['elapsed_tick'], 40)
            self.assertEqual(result['frames'][0]['image'], 'native/frame.png')
            self.assertEqual((run / 'frame.png').read_bytes(), before)

    def test_changed_original_is_rejected(self):
        with tempfile.TemporaryDirectory() as directory:
            root, run, _ = self.fixture(directory)
            (run / 'frame.png').write_bytes(b'\x89PNG\r\n\x1a\nchanged')
            with self.assertRaisesRegex(ValueError, 'hash does not match'):
                gallery.payload(run, root / 'report.html')

    def test_manifest_cannot_escape_capture_directory(self):
        with tempfile.TemporaryDirectory() as directory:
            root, run, manifest = self.fixture(directory)
            manifest['frames'][0]['file'] = '../unrelated.png'
            (run / 'manifest.json').write_text(json.dumps(manifest))
            with self.assertRaisesRegex(ValueError, 'PNG basename'):
                gallery.payload(run, root / 'report.html')

    def test_reordered_time_is_rejected(self):
        with tempfile.TemporaryDirectory() as directory:
            root, run, manifest = self.fixture(directory)
            (run / 'second.png').write_bytes((run / 'frame.png').read_bytes())
            second = {**manifest['frames'][0], 'file': 'second.png',
                      'server_snapshot': {'elapsed_tick': 20, 'sequence': 'ducks'}}
            manifest['frames'].append(second)
            (run / 'manifest.json').write_text(json.dumps(manifest))
            with self.assertRaisesRegex(ValueError, 'timing'):
                gallery.payload(run, root / 'report.html')


if __name__ == '__main__':
    unittest.main()
