"""Temporary-file safety tests for staging; these do not claim native Minecraft QA."""
import configparser
import gzip
import importlib.util
import json
from pathlib import Path
import sys
import tempfile
import unittest

TOOLS = Path(__file__).resolve().parents[1]
sys.path.insert(0, str(TOOLS))
sys.dont_write_bytecode = True
spec = importlib.util.spec_from_file_location("stage_living_prism_test", TOOLS / "stage_living_prism_test.py")
runner = importlib.util.module_from_spec(spec)
spec.loader.exec_module(runner)
from prepare_living_showcase import encode, preset_nbt


class LivingPrismStageTest(unittest.TestCase):
    def setUp(self):
        self.temporary = tempfile.TemporaryDirectory()
        self.addCleanup(self.temporary.cleanup)
        self.root = Path(self.temporary.name).resolve()
        self.world = self.root / "build/living-showcase-worlds/world-fixture"
        self.world.mkdir(parents=True)
        self.preset = {"dimensions": {"minecraft:overworld": {"generator": {"biome_source": {
            "type": "dungeonsalive:living_overworld", "amber_grove": "dungeonsalive:amber_grove",
            "luminous_marsh": "dungeonsalive:luminous_marsh", "ember_crags": "dungeonsalive:ember_crags",
            "blighted_thicket": "dungeonsalive:blighted_thicket"}}}}}
        self.level_data = {"DataVersion": (3, 3955), "version": (3, 19133), "GameType": (3, 1),
                           "allowCommands": (1, 1), "initialized": (1, 0), "LastPlayed": (4, 0),
                           "confirmedExperimentalSettings": (1, 1), "LevelName": (8, "Dungeons Alive Living Showcase"),
                           "WorldGenSettings": (10, {"seed": (4, 20261102), "dimensions": preset_nbt(self.preset["dimensions"])})}
        self.write_level()
        self.write(self.world / runner.SOURCE_MARKER, (runner.SOURCE_TOKEN + "\n").encode())
        self.json(self.world / "preparation.json", {"kind": "fresh_isolated_developer_world",
                  "preset": "dungeonsalive:living_world", "existing_save_read_or_modified": False,
                  "native_decode_generation_and_rendering_executed": False, "experimental_development_preset_acknowledged": True})
        preset_path = "src/main/resources/data/dungeonsalive/worldgen/world_preset/living_world.json"
        self.json(preset_path, self.preset)
        self.write("src/main/java/Fixture.java", b"// current source fixture")
        self.write("src/main/templates/META-INF/neoforge.mods.toml", b"fixture")
        self.write("build.gradle", b"fixture")
        self.jar = self.root / "build/local-check/fixture.jar"
        self.write(self.jar, b"fixture jar bytes; not a real Minecraft artifact")
        self.java = self.root / "reports/java.json"
        self.native = self.root / "reports/native.json"
        self.visual = self.root / "reports/visual.json"
        self.java_data = {"status": "passed", "junit_exit_code": 0,
                          "compile_main": {"exit_code": 0}, "compile_test": {"exit_code": 0},
                          "junit": {"counts": {"succeeded": 1, "found": 1, "failed": 0, "aborted": 0, "skipped": 0, "total_failures": 0}},
                          "artifact": {"path": str(self.jar), "sha256": runner.digest(self.jar.read_bytes()),
                                       "kind": "local_direct_javac_test_artifact_not_gradle_release"},
                          "sources": self.inventory("src/main/java/Fixture.java"), "resource_inputs": self.inventory(preset_path),
                          "packaging_inputs": self.inventory("src/main/templates/META-INF/neoforge.mods.toml"),
                          "build_configuration": self.inventory("build.gradle")}
        self.native_data = {"status": "passed", "exit_code": 0, "game_test_server_started": True,
                            "completed_test_count": 1, "required_failures": 0, "optional_failures": 0,
                            "failure_messages": [], "artifact": self.java_data["artifact"]}
        self.write("reports/terrain.png", b"\x89PNG\r\n\x1a\nimage fixture")
        self.visual_data = {"status": "passed", "scope": "isolated_living_world_client", "artifact": self.java_data["artifact"],
                            "world_preset": "dungeonsalive:living_world", "checks": {"native_world_loaded": True, "living_world_terrain_rendered": True},
                            "screenshots": [{**self.inventory("reports/terrain.png")[0], "reviewed": True}]}
        self.reports()
        self.json(runner.SOURCE_INSTANCE / "mmc-pack.json", {"formatVersion": 1, "components": [
            {"uid": key, "version": value} for key, value in {"org.lwjgl3": "3.3.3", "net.minecraft": "1.21.1", "net.neoforged": "21.1.265"}.items()]})
        self.write(runner.SOURCE_INSTANCE / "instance.cfg", b"[General]\nJavaVersion=21.0.3\nJavaPath=/fixture/java\nOverrideJavaLocation=true\nname=Old\nPreLaunchCommand=do not copy\n")
        self.write(runner.SOURCE_INSTANCE / "minecraft/mods/old.jar", b"untouched old mod")
        self.write(runner.SOURCE_INSTANCE / "minecraft/saves/old/level.dat", b"untouched old save")

    def write(self, path, data):
        path = self.root / path
        path.parent.mkdir(parents=True, exist_ok=True)
        path.write_bytes(data)

    def json(self, path, data):
        self.write(path, json.dumps(data).encode())

    def inventory(self, *paths):
        return [{"path": path, "sha256": runner.digest((self.root / path).read_bytes())} for path in paths]

    def write_level(self):
        raw = b"\x0a" + encode(8, "") + encode(10, {"Data": (10, self.level_data)})
        self.write(self.world / "level.dat", gzip.compress(raw, mtime=0))

    def reports(self):
        self.json(self.java, self.java_data)
        self.json(self.native, self.native_data)
        self.json(self.visual, self.visual_data)

    def preflight(self):
        return runner.preflight(self.root, self.world, self.java, self.native, self.visual)

    def test_stages_separate_instance_and_only_renames_generated_world(self):
        old = {p: p.read_bytes() for p in (self.root / runner.SOURCE_INSTANCE).rglob("*") if p.is_file()}
        source = {p: p.read_bytes() for p in self.world.iterdir()}
        plan = self.preflight()
        self.assertFalse((self.root / runner.TARGET_INSTANCE).exists(), "preflight wrote an instance")
        runner.stage(self.root, plan, self.preflight)
        target = self.root / runner.TARGET_INSTANCE
        world = target / "minecraft/saves" / runner.WORLD_NAME
        self.assertEqual(source[self.world / "level.dat"], (world / "level.dat.source").read_bytes())
        before = gzip.decompress(source[self.world / "level.dat"])
        after = gzip.decompress((world / "level.dat").read_bytes())
        self.assertEqual(before.replace(encode(8, "Dungeons Alive Living Showcase"), encode(8, runner.WORLD_NAME), 1), after)
        self.assertEqual({p: p.read_bytes() for p in old}, old)
        self.assertEqual({p: p.read_bytes() for p in source}, source)
        self.assertEqual(["fixture.jar"], [p.name for p in (target / "minecraft/mods").iterdir()])
        config = configparser.ConfigParser(interpolation=None)
        config.read(target / "instance.cfg")
        self.assertEqual(runner.INSTANCE_NAME, config["General"]["name"])
        self.assertEqual(runner.WORLD_NAME, config["General"]["JoinWorldOnLaunch"])
        self.assertNotIn("PreLaunchCommand", config["General"])
        self.assertEqual(runner.OPTIONS, (target / "minecraft/options.txt").read_text())

    def test_failed_native_or_wrong_artifact_never_creates_target(self):
        for mutation in ({"status": "failed"}, {"artifact": {"sha256": "0" * 64}}, {"required_failures": 1}):
            original = self.native_data.copy()
            self.native_data.update(mutation)
            self.reports()
            with self.assertRaisesRegex(ValueError, "Native GameTests"):
                self.preflight()
            self.native_data = original
        self.assertFalse((self.root / runner.TARGET_INSTANCE).exists())

    def test_changed_source_and_added_resource_invalidate_current_java_report(self):
        self.write("src/main/java/Fixture.java", b"changed")
        with self.assertRaisesRegex(ValueError, "changed after compilation"):
            self.preflight()
        self.write("src/main/java/Fixture.java", b"// current source fixture")
        self.write("src/main/resources/added.json", b"{}")
        with self.assertRaisesRegex(ValueError, "file set changed"):
            self.preflight()

    def test_played_unmarked_or_symlinked_world_is_rejected(self):
        self.write(self.world / "region/r.0.0.mca", b"played world")
        with self.assertRaisesRegex(ValueError, "unexpected files"):
            self.preflight()
        (self.world / "region/r.0.0.mca").unlink()
        (self.world / "region").rmdir()
        (self.world / runner.SOURCE_MARKER).write_text("not ours")
        with self.assertRaisesRegex(ValueError, "marker"):
            self.preflight()
        (self.world / runner.SOURCE_MARKER).unlink()
        (self.world / runner.SOURCE_MARKER).symlink_to(self.java)
        with self.assertRaisesRegex(ValueError, "symlink"):
            self.preflight()

    def test_existing_unmarked_instance_is_never_replaced_even_by_publish_race(self):
        plan = self.preflight()
        target = self.root / runner.TARGET_INSTANCE
        target.mkdir(parents=True)
        with self.assertRaisesRegex(ValueError, "Destination already exists"):
            self.preflight()
        candidate = target.parent / "candidate"
        candidate.mkdir()
        (candidate / "marker").write_text("candidate")
        with self.assertRaises(OSError):
            runner.publish_new_directory(candidate, target)
        self.assertTrue(candidate.is_dir())
        self.assertEqual([], list(target.iterdir()))

    def test_visual_scope_review_and_screenshot_hash_are_required(self):
        self.visual_data["screenshots"][0]["reviewed"] = False
        self.reports()
        with self.assertRaisesRegex(ValueError, "actually been reviewed"):
            self.preflight()
        self.visual_data["screenshots"][0]["reviewed"] = True
        self.reports()
        self.write("reports/terrain.png", b"different image")
        with self.assertRaisesRegex(ValueError, "match its hash"):
            self.preflight()

    def test_old_world_metadata_or_wrong_current_preset_is_rejected(self):
        self.level_data["initialized"] = (1, 1)
        self.write_level()
        with self.assertRaisesRegex(ValueError, "untouched"):
            self.preflight()
        self.level_data["initialized"] = (1, 0)
        self.write_level()
        with self.assertRaisesRegex(ValueError, "dimensions differ"):
            runner.renamed_fresh_level((self.world / "level.dat").read_bytes(), {"dimensions": {}})

    def test_change_during_copy_prevents_atomic_publication_and_cleans_temp_tree(self):
        plan = self.preflight()
        def mutate_and_revalidate():
            self.write("src/main/java/Fixture.java", b"changed during staging")
            return self.preflight()
        with self.assertRaisesRegex(ValueError, "changed after compilation"):
            runner.stage(self.root, plan, mutate_and_revalidate)
        self.assertFalse((self.root / runner.TARGET_INSTANCE).exists())
        self.assertEqual([], list((self.root / runner.TARGET_INSTANCE).parent.glob(".living-prism-stage-*")))

    def pending_preflight(self):
        return runner.preflight(self.root, self.world, self.java, self.native, None, visual_pending=True)

    def test_visual_pending_stages_labeled_fixture_without_visual_evidence(self):
        self.visual.unlink()
        (self.root / "reports/terrain.png").unlink()
        plan = self.pending_preflight()
        self.assertEqual("pending", plan["visual_status"])
        self.assertEqual("pending", plan["evidence"]["visual"]["status"])
        self.assertNotIn("path", plan["evidence"]["visual"])
        runner.stage(self.root, plan, self.pending_preflight)
        target = self.root / runner.TARGET_INSTANCE
        config = configparser.ConfigParser(interpolation=None)
        config.read(target / "instance.cfg")
        self.assertEqual(runner.PENDING_INSTANCE_NAME, config["General"]["name"])
        manifest = json.loads((target / "staging.json").read_text())
        self.assertEqual(runner.PENDING_INSTANCE_NAME, manifest["instance_name"])
        self.assertEqual("pending", manifest["visual_status"])
        self.assertEqual("pending", manifest["evidence"]["visual"]["status"])
        self.assertIn("visual QA is pending", manifest["scope"])
        self.assertFalse(manifest["launcher_opened"])

    def test_visual_pending_retains_native_artifact_fresh_world_and_target_guards(self):
        self.native_data["status"] = "failed"
        self.reports()
        with self.assertRaisesRegex(ValueError, "Native GameTests"):
            self.pending_preflight()
        self.native_data["status"] = "passed"
        self.reports()
        jar_bytes = self.jar.read_bytes()
        self.jar.write_bytes(b"changed artifact")
        with self.assertRaisesRegex(ValueError, "Artifact differs"):
            self.pending_preflight()
        self.jar.write_bytes(jar_bytes)
        self.level_data["initialized"] = (1, 1)
        self.write_level()
        with self.assertRaisesRegex(ValueError, "untouched"):
            self.pending_preflight()
        self.level_data["initialized"] = (1, 0)
        self.write_level()
        (self.root / runner.TARGET_INSTANCE).mkdir(parents=True)
        with self.assertRaisesRegex(ValueError, "Destination already exists"):
            self.pending_preflight()

    def test_visual_mode_must_be_explicit_and_unambiguous(self):
        with self.assertRaisesRegex(ValueError, "exactly one explicit visual mode"):
            runner.preflight(self.root, self.world, self.java, self.native, None)
        with self.assertRaisesRegex(ValueError, "exactly one explicit visual mode"):
            runner.preflight(self.root, self.world, self.java, self.native, self.visual, visual_pending=True)


if __name__ == "__main__":
    unittest.main()
