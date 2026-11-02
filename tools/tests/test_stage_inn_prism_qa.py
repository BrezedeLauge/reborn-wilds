"""Filesystem and evidence guards only; temporary JAR fixtures are not runtime evidence."""
import configparser
import importlib.util
import io
import json
from pathlib import Path
import sys
import unittest
import zipfile

ROOT = next(parent for parent in Path(__file__).resolve().parents if (parent / "AGENTS.md").is_file())
sys.dont_write_bytecode = True
sys.path.insert(0, str(ROOT / "tools/tests"))
sys.path.insert(0, str(ROOT / "tools"))
spec = importlib.util.spec_from_file_location("stage_inn_prism_qa", Path(__file__).resolve().parents[1] / "stage_inn_prism_qa.py")
qa = importlib.util.module_from_spec(spec); spec.loader.exec_module(qa)


class InnPrismQaTest(unittest.TestCase):
    def setUp(self):
        from test_stage_living_prism_test import LivingPrismStageTest
        self.fixture = LivingPrismStageTest(); self.fixture.setUp(); self.addCleanup(self.fixture.doCleanups)
        self.root = self.fixture.root
        self.fixture.write("AGENTS.md", b"fixture project")
        self.fixture.write("gradle.properties", b"fixture platform")
        archive = io.BytesIO()
        with zipfile.ZipFile(archive, "w") as jar:
            for name in ("client/dev/InnShowcaseCapture", "settlement/inn/InnContent", "settlement/inn/InnService"):
                jar.writestr("de/laugenbreze/dungeonsalive/" + name + ".class", b"guard fixture, not Java bytecode")
        self.fixture.jar.write_bytes(archive.getvalue())
        self.fixture.java_data["artifact"]["sha256"] = qa.shared.digest(archive.getvalue())
        self.fixture.reports()

    def validate(self):
        f = self.fixture
        return qa.preflight(self.root, f.world, f.java, f.native)

    def test_exclusive_inn_profile_preserves_source_and_existing_instances(self):
        existing = {}
        for name in ("Dungeons-Alive-Creative", "Dungeons-Alive-Living-World", "Dungeons-Alive-Native-QA", "Dungeons-Alive-Nature-QA"):
            p = self.root / "run-prism/instances" / name / "minecraft/keep.txt"
            p.parent.mkdir(parents=True, exist_ok=True); p.write_bytes(name.encode()); existing[p] = p.read_bytes()
        source = {p: p.read_bytes() for p in self.fixture.world.iterdir()}
        plan = self.validate(); self.assertFalse(plan["target"].exists())
        qa.stage(self.root, plan, self.validate)
        target = self.root / qa.TARGET
        self.assertEqual(existing, {p: p.read_bytes() for p in existing})
        self.assertEqual(source, {p: p.read_bytes() for p in source})
        config = configparser.ConfigParser(interpolation=None); config.read(target / "instance.cfg")
        self.assertEqual("-Ddungeonsalive.innShowcase=true -Ddungeonsalive.isolatedInnQa=true", config["General"]["JvmArgs"])
        self.assertEqual(qa.WORLD, config["General"]["JoinWorldOnLaunch"])
        self.assertNotIn("PreLaunchCommand", config["General"])
        self.assertEqual(qa.TOKEN + "\n", (target / "minecraft/saves" / qa.WORLD / qa.FRESH_MARKER).read_text())
        report = json.loads((target / "staging.json").read_text())
        self.assertFalse(report["launcher_opened"]); self.assertFalse(report["visual_review_performed"])
        self.assertEqual("minecraft/inn-captures/manifest.json", report["capture_output"])

    def test_artifact_without_capture_and_failed_native_are_rejected(self):
        self.fixture.native_data["status"] = "failed"; self.fixture.reports()
        with self.assertRaisesRegex(ValueError, "Native GameTests"): self.validate()
        self.fixture.native_data["status"] = "passed"
        archive = io.BytesIO()
        with zipfile.ZipFile(archive, "w") as jar: jar.writestr("unrelated.txt", b"fixture")
        self.fixture.jar.write_bytes(archive.getvalue())
        self.fixture.java_data["artifact"]["sha256"] = qa.shared.digest(archive.getvalue()); self.fixture.reports()
        with self.assertRaisesRegex(ValueError, "lacks the actual inn"): self.validate()
        self.assertFalse((self.root / qa.TARGET).exists())

    def test_played_world_symlink_and_existing_target_are_refused(self):
        played = self.fixture.world / "session.lock"; played.write_bytes(b"played")
        with self.assertRaisesRegex(ValueError, "exactly the three fresh"): self.validate()
        played.unlink(); target = self.root / qa.TARGET; target.mkdir(parents=True)
        with self.assertRaisesRegex(ValueError, "already exists"): self.validate()
        target.rmdir(); target.symlink_to(self.fixture.world, target_is_directory=True)
        with self.assertRaisesRegex(ValueError, "symlink"): self.validate()

    def test_revalidation_failure_cleans_only_own_unpublished_directory(self):
        plan = self.validate()
        def changed():
            self.fixture.write("src/main/java/Fixture.java", b"changed after snapshot")
            return self.validate()
        with self.assertRaisesRegex(ValueError, "changed after compilation"): qa.stage(self.root, plan, changed)
        self.assertFalse((self.root / qa.TARGET).exists())
        self.assertEqual([], list((self.root / qa.TARGET).parent.glob(".inn-prism-qa-*")))


if __name__ == "__main__": unittest.main()
