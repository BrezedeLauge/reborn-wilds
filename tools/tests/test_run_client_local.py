"""Contracts for the isolated official development-client preparation."""
import hashlib
import importlib.util
import json
from pathlib import Path
import sys
import tempfile
import unittest
from unittest.mock import patch

TOOLS = Path(__file__).resolve().parents[1]
sys.path.insert(0, str(TOOLS))
sys.dont_write_bytecode = True
spec = importlib.util.spec_from_file_location("run_client_local", TOOLS / "run_client_local.py")
runner = importlib.util.module_from_spec(spec)
spec.loader.exec_module(runner)


class LocalClientPreparationTest(unittest.TestCase):
    def test_original_recipe_keeps_client_target_and_uses_macos_thread_and_explicit_showcase(self):
        config = {"modules": ["example:bootstrap:1"], "runs": {"client": {
            "main": "cpw.mods.bootstraplauncher.BootstrapLauncher", "client": True, "server": False,
            "jvmArgs": ["-p", "{modules}"],
            "args": ["--launchTarget", "forgeclientdev", "--assetsDir", "{assets_root}", "--assetIndex", "{asset_index}", "--gameDir", "."],
            "props": {"legacyClassPath.file": "{minecraft_classpath_file}"}}}}
        with patch.object(runner, "cached_jar", return_value=Path("bootstrap.jar")):
            vm, args, _ = runner.original_client_recipe(config, Path("cache"), Path("classpath"), Path("mod"),
                                                        {"index": "17", "root": "/assets"}, True, True)
            plain_vm, plain_args, _ = runner.original_client_recipe(config, Path("cache"), Path("classpath"), Path("mod"),
                                                                     {"index": "17", "root": "/assets"}, False, False)
        self.assertEqual(["--launchTarget", "forgeclientdev"], args[:2])
        self.assertIn("-XstartOnFirstThread", vm)
        self.assertIn("-Ddungeonsalive.livingShowcase=true", vm)
        self.assertEqual(["--quickPlaySingleplayer", "living-showcase"], args[-2:])
        self.assertNotIn("--quickPlaySingleplayer", plain_args)
        self.assertFalse(any("livingShowcase" in value or "headless" in value for value in plain_vm))
        self.assertFalse(any("accessToken" in value for value in args))
        self.assertEqual("1280", args[args.index("--width") + 1])
        self.assertEqual("800", args[args.index("--height") + 1])

    def test_fresh_options_are_confined_to_marked_run_and_never_overwritten(self):
        with tempfile.TemporaryDirectory() as tmp:
            work = Path(tmp)
            (work / runner.RUN_MARKER).write_text(runner.RUN_MARKER_CONTENT)
            runner.write_fresh_options(work)
            self.assertEqual(runner.CLIENT_OPTIONS, (work / "options.txt").read_text())
            with self.assertRaises(FileExistsError):
                runner.write_fresh_options(work)
            (work / runner.RUN_MARKER).write_text("unrelated-world")
            with self.assertRaisesRegex(RuntimeError, "marked isolated"):
                runner.write_fresh_options(work)

    def test_world_copy_preserves_source_and_omits_lock(self):
        with tempfile.TemporaryDirectory() as tmp:
            root = Path(tmp); source = root / "source"; source.mkdir()
            (source / runner.WORLD_MARKER).write_text(runner.WORLD_MARKER_CONTENT + "\n")
            (source / "level.dat").write_bytes(b"fixture-level-nbt")
            (source / "session.lock").write_bytes(b"source-lock")
            (source / "region").mkdir(); (source / "region/r.0.0.mca").write_bytes(b"fixture-region")
            before = {p.relative_to(source): p.read_bytes() for p in source.rglob("*") if p.is_file()}
            destination = root / "copy"
            result = runner.copy_showcase_world(source, destination)
            self.assertEqual(before, {p.relative_to(source): p.read_bytes() for p in source.rglob("*") if p.is_file()})
            self.assertFalse((destination / "session.lock").exists())
            self.assertEqual(b"fixture-region", (destination / "region/r.0.0.mca").read_bytes())
            self.assertFalse(result["source_writes"])

    def test_unmarked_worlds_and_source_symlinks_are_rejected_before_copy(self):
        with tempfile.TemporaryDirectory() as tmp:
            root = Path(tmp); source = root / "source"; source.mkdir()
            (source / "level.dat").write_bytes(b"fixture")
            with self.assertRaisesRegex(RuntimeError, "must contain"):
                runner.copy_showcase_world(source, root / "copy")
            (source / runner.WORLD_MARKER).write_text(runner.WORLD_MARKER_CONTENT)
            (source / "linked").symlink_to(root / "outside")
            with self.assertRaisesRegex(RuntimeError, "Symlinks"):
                runner.copy_showcase_world(source, root / "copy")
            self.assertFalse((root / "copy").exists())

    def test_missing_asset_objects_fail_without_download(self):
        with tempfile.TemporaryDirectory() as tmp:
            root = Path(tmp); (root / "indexes").mkdir()
            data = b"fixture-asset"; digest = hashlib.sha1(data).hexdigest()
            (root / "indexes/17.json").write_text(json.dumps({"objects": {"test": {"hash": digest, "size": len(data)}}}))
            properties = root / "assets.properties"; properties.write_text(f"asset_index=17\nassets_root={root}\n")
            with self.assertRaisesRegex(RuntimeError, "no download"):
                runner.checked_assets(properties)
            obj = root / "objects" / digest[:2] / digest; obj.parent.mkdir(parents=True); obj.write_bytes(data)
            self.assertEqual(1, runner.checked_assets(properties)["object_count"])

    def test_clean_exit_cannot_substitute_for_twelve_verified_native_captures(self):
        with tempfile.TemporaryDirectory() as tmp:
            root = Path(tmp); captures = root / "captures"; captures.mkdir()
            manifest = captures / "manifest.json"
            manifest.write_text(json.dumps({"status": "failed", "captures": [], "error": "fixture failure"}))
            with self.assertRaisesRegex(RuntimeError, "did not complete"):
                runner.checked_captures(root, "")
            records = []
            for index in range(12):
                path = captures / f"{index}.png"; path.write_bytes(b"\x89PNG\r\n\x1a\nfixture")
                records.append({"file": path.name, "sha256": runner.sha256(path)})
            manifest.write_text(json.dumps({"status": "captured", "captures": records}))
            result = runner.checked_captures(root, "Living showcase completed: 12 native captures")
            self.assertEqual(12, result["capture_count"])
            self.assertFalse(result["visual_review_performed"])
            (captures / "0.png").write_bytes(b"changed")
            with self.assertRaisesRegex(RuntimeError, "PNG/digest"):
                runner.checked_captures(root, "Living showcase completed: 12 native captures")


if __name__ == "__main__":
    unittest.main()
