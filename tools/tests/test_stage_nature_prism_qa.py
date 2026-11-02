"""Isolated filesystem guards, with no launch or rendered-behavior claim."""
import configparser
import gzip
import importlib.util
import json
from pathlib import Path
import sys
import unittest

TOOLS = Path(__file__).resolve().parents[1]
sys.path.insert(0, str(TOOLS))
sys.dont_write_bytecode = True
spec = importlib.util.spec_from_file_location("stage_nature_prism_qa", TOOLS / "stage_nature_prism_qa.py")
qa = importlib.util.module_from_spec(spec)
spec.loader.exec_module(qa)


class NaturePrismQaTest(unittest.TestCase):
    def setUp(self):
        from test_stage_living_prism_test import LivingPrismStageTest
        self.fixture = LivingPrismStageTest()
        self.fixture.setUp()
        self.addCleanup(self.fixture.doCleanups)
        self.root = self.fixture.root
        self.fixture.write("AGENTS.md", b"fixture project marker")
        self.fixture.write("gradle.properties", b"fixture properties")

    def validate(self):
        f = self.fixture
        return qa.preflight(self.root, f.world, f.java, f.native)

    def test_new_nature_profile_does_not_change_either_active_profile_or_source(self):
        sentinels = {}
        for name in ("Dungeons-Alive-Living-World", "Dungeons-Alive-Native-QA"):
            path = self.root / "run-prism/instances" / name / "minecraft/session.lock"
            path.parent.mkdir(parents=True)
            path.write_bytes(b"active profile: " + name.encode())
            sentinels[path] = path.read_bytes()
        original = {path: path.read_bytes() for path in self.fixture.world.iterdir()}
        plan = self.validate()
        qa.stage(self.root, plan, self.validate)
        target = self.root / qa.TARGET
        world = target / "minecraft/saves" / qa.WORLD
        self.assertEqual(plan["staged_level"], (world / "level.dat").read_bytes())
        self.assertIn(qa.WORLD.encode(), gzip.decompress(plan["staged_level"]))
        self.assertNotIn(b"Dungeons Alive Living Showcase", gzip.decompress(plan["staged_level"]))
        self.assertEqual(original, {path: path.read_bytes() for path in original})
        self.assertEqual(sentinels, {path: path.read_bytes() for path in sentinels})
        self.assertEqual(qa.TOKEN + "\n", (world / qa.FRESH_MARKER).read_text())
        settings = configparser.ConfigParser(interpolation=None)
        settings.read(target / "instance.cfg")
        self.assertEqual(qa.JAVA_ARGUMENTS, settings["General"]["JvmArgs"])
        self.assertNotIn("livingShowcase", settings["General"]["JvmArgs"])
        self.assertEqual(qa.WORLD, settings["General"]["JoinWorldOnLaunch"])
        self.assertIn("soundCategory_master:0.35\n", (target / "minecraft/options.txt").read_text())
        manifest = json.loads((target / "staging.json").read_text())
        self.assertFalse(manifest["launcher_opened"])
        self.assertFalse(manifest["visual_review_performed"])
        self.assertEqual(.35, manifest["audio_enabled_master_volume"])
        self.assertFalse(manifest["audio_hearing_review_performed"])
        self.assertEqual("minecraft/nature-captures/manifest.json", manifest["capture_output"])

    def test_failed_native_or_changed_jar_or_played_source_refuses_staging(self):
        self.fixture.native_data["status"] = "failed"
        self.fixture.reports()
        with self.assertRaisesRegex(ValueError, "Native GameTests"):
            self.validate()
        self.fixture.native_data["status"] = "passed"
        self.fixture.reports()
        original = self.fixture.jar.read_bytes()
        self.fixture.jar.write_bytes(b"different artifact")
        with self.assertRaisesRegex(ValueError, "Artifact differs"):
            self.validate()
        self.fixture.jar.write_bytes(original)
        (self.fixture.world / "session.lock").write_bytes(b"played")
        with self.assertRaisesRegex(ValueError, "exactly the three fresh"):
            self.validate()
        self.assertFalse((self.root / qa.TARGET).exists())

    def test_existing_nature_profile_and_symlink_are_never_replaced(self):
        target = self.root / qa.TARGET
        target.mkdir(parents=True)
        with self.assertRaisesRegex(ValueError, "already exists"):
            self.validate()
        target.rmdir()
        target.symlink_to(self.fixture.world, target_is_directory=True)
        with self.assertRaisesRegex(ValueError, "symlink"):
            self.validate()

    def test_input_change_during_copy_removes_only_own_temporary_output(self):
        plan = self.validate()
        def changed():
            self.fixture.write("src/main/java/Fixture.java", b"changed during copy")
            return self.validate()
        with self.assertRaisesRegex(ValueError, "changed after compilation"):
            qa.stage(self.root, plan, changed)
        self.assertFalse((self.root / qa.TARGET).exists())
        self.assertEqual([], list((self.root / qa.TARGET).parent.glob(".nature-prism-qa-*")))


if __name__ == "__main__":
    unittest.main()
