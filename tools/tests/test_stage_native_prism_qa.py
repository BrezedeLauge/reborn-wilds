"""Filesystem-only staging regressions; no native rendering is claimed by these fixtures."""
import configparser
import importlib.util
import json
from pathlib import Path
import sys
import unittest

TOOLS = Path(__file__).resolve().parents[1]
sys.path.insert(0, str(TOOLS))
sys.dont_write_bytecode = True
spec = importlib.util.spec_from_file_location("stage_native_prism_qa", TOOLS / "stage_native_prism_qa.py")
qa = importlib.util.module_from_spec(spec)
spec.loader.exec_module(qa)


class NativePrismQaTest(unittest.TestCase):
    def setUp(self):
        from test_stage_living_prism_test import LivingPrismStageTest
        self.fixture = LivingPrismStageTest()
        self.fixture.setUp()
        self.addCleanup(self.fixture.doCleanups)
        self.root = self.fixture.root
        self.fixture.write("AGENTS.md", b"fixture project marker")
        self.fixture.write("gradle.properties", b"fixture project properties")

    def validate(self):
        f = self.fixture
        return qa.preflight(self.root, f.world, f.java, f.native)

    def test_native_profile_is_separate_and_uses_original_fresh_world_bytes_and_two_flags(self):
        playable = self.root / qa.shared.TARGET_INSTANCE
        playable.mkdir(parents=True)
        (playable / "sentinel").write_bytes(b"existing playable profile")
        original = {path: path.read_bytes() for path in self.fixture.world.iterdir()}
        self.fixture.visual.unlink()  # First capture does not require prior visual approval.
        plan = self.validate()
        qa.stage(self.root, plan, self.validate)
        target = self.root / qa.TARGET
        run = target / "minecraft"
        world = run / "saves" / qa.WORLD
        self.assertEqual(original[self.fixture.world / "level.dat"], (world / "level.dat").read_bytes())
        self.assertEqual(original, {path: path.read_bytes() for path in original})
        self.assertEqual(3, len(list(self.fixture.world.iterdir())))
        self.assertEqual(qa.TOKEN + "\n", (run / qa.RUN_MARKER).read_text())
        self.assertEqual(qa.TOKEN + "\n", (world / qa.FRESH_MARKER).read_text())
        self.assertFalse((run / "captures").exists())
        config = configparser.ConfigParser(interpolation=None)
        config.read(target / "instance.cfg")
        self.assertEqual(qa.JAVA_ARGUMENTS, config["General"]["JvmArgs"])
        self.assertEqual("true", config["General"]["OverrideJavaArgs"])
        self.assertEqual(qa.WORLD, config["General"]["JoinWorldOnLaunch"])
        self.assertNotIn("PreLaunchCommand", config["General"])
        options = (run / "options.txt").read_text()
        self.assertIn("onboardAccessibility:false\n", options)
        self.assertIn("pauseOnLostFocus:false\n", options)
        self.assertNotIn("startedCleanly", options)
        manifest = json.loads((target / "staging.json").read_text())
        self.assertEqual("not_started", manifest["capture_status"])
        self.assertFalse(manifest["visual_review_performed"])
        self.assertFalse(manifest["launcher_opened"])
        self.assertEqual(b"existing playable profile", (playable / "sentinel").read_bytes())

    def test_native_failure_old_artifact_and_played_source_fail_closed(self):
        self.fixture.native_data["status"] = "failed"
        self.fixture.reports()
        with self.assertRaisesRegex(ValueError, "Native GameTests"):
            self.validate()
        self.fixture.native_data["status"] = "passed"
        self.fixture.reports()
        before = self.fixture.jar.read_bytes()
        self.fixture.jar.write_bytes(b"older jar")
        with self.assertRaisesRegex(ValueError, "Artifact differs"):
            self.validate()
        self.fixture.jar.write_bytes(before)
        self.fixture.write(self.fixture.world / "session.lock", b"already opened")
        with self.assertRaisesRegex(ValueError, "exactly the three fresh"):
            self.validate()
        self.assertFalse((self.root / qa.TARGET).exists())

    def test_existing_qa_target_and_symlinked_parent_are_never_replaced(self):
        target = self.root / qa.TARGET
        target.mkdir(parents=True)
        with self.assertRaisesRegex(ValueError, "already exists"):
            self.validate()
        target.rmdir()
        other = self.root / "other"
        other.mkdir()
        target.symlink_to(other, target_is_directory=True)
        with self.assertRaisesRegex(ValueError, "symlink"):
            self.validate()
        self.assertEqual([], list(other.iterdir()))

    def test_changed_inputs_during_copy_clean_only_temporary_qa_directory(self):
        plan = self.validate()
        def changed():
            self.fixture.write("src/main/java/Fixture.java", b"new source after preflight")
            return self.validate()
        with self.assertRaisesRegex(ValueError, "changed after compilation"):
            qa.stage(self.root, plan, changed)
        self.assertFalse((self.root / qa.TARGET).exists())
        self.assertEqual([], list((self.root / qa.TARGET).parent.glob(".native-prism-qa-*")))


if __name__ == "__main__":
    unittest.main()
