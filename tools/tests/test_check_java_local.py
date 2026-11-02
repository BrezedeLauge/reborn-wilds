"""Safety checks for the direct local compiler's dependency selection."""
import importlib.util
from pathlib import Path
import sys
import tempfile
import unittest
import zipfile

TOOLS = Path(__file__).resolve().parents[1]
sys.path.insert(0, str(TOOLS))
sys.dont_write_bytecode = True
spec = importlib.util.spec_from_file_location("check_java_local", TOOLS / "check_java_local.py")
runner = importlib.util.module_from_spec(spec)
spec.loader.exec_module(runner)


class LocalJavaCheckTest(unittest.TestCase):
    def test_saved_graph_preserves_order_and_does_not_scan_extra_cache_jars(self):
        with tempfile.TemporaryDirectory() as temporary:
            root = Path(temporary)
            jars = [root / name for name in ("b.jar", "a.jar", "unrelated.jar")]
            for jar in jars:
                with zipfile.ZipFile(jar, "w") as output:
                    output.writestr("example/Dependency.class", b"fixture")
            paths = root / "classpath.txt"
            paths.write_text(f"{jars[0]}\n{jars[1]}\n{jars[0]}\n")
            self.assertEqual([path.resolve() for path in jars[:2]], runner.read_classpath(paths))

    def test_missing_libraries_and_old_mod_classes_fail_closed(self):
        with tempfile.TemporaryDirectory() as temporary:
            root = Path(temporary)
            paths = root / "classpath.txt"
            paths.write_text(str(root / "missing.jar"))
            with self.assertRaisesRegex(RuntimeError, "Missing or non-JAR"):
                runner.read_classpath(paths)
            old_mod = root / "old-mod.jar"
            with zipfile.ZipFile(old_mod, "w") as output:
                output.writestr("de/laugenbreze/dungeonsalive/Old.class", b"fixture")
            paths.write_text(str(old_mod))
            with self.assertRaisesRegex(RuntimeError, "old project classes"):
                runner.read_classpath(paths)

    def test_javac_argfile_handles_spaces_quotes_and_empty_sourcepath(self):
        self.assertEqual('"-sourcepath"\n""\n"a b\\\"c\\\\d"\n',
                         runner.argfile(["-sourcepath", "", 'a b"c\\d']))
        with self.assertRaisesRegex(RuntimeError, "Newlines"):
            runner.argfile(["unsafe\nargument"])

    def test_artifact_output_cannot_replace_release_or_installed_mod(self):
        self.assertEqual(runner.ROOT / "build/local-check/check.jar",
                         runner.artifact_destination(Path("build/local-check/check.jar")))
        for path in ("dist/check.jar", "build/libs/check.jar", "run-prism/instances/Test/minecraft/mods/check.jar"):
            with self.assertRaisesRegex(RuntimeError, "build/local-check"):
                runner.artifact_destination(Path(path))

    def test_local_jar_has_fresh_main_classes_resources_and_expanded_metadata_only(self):
        with tempfile.TemporaryDirectory() as temporary:
            root = Path(temporary)
            classes = root / "classes/main"
            classes.mkdir(parents=True)
            (classes / "Actual.class").write_bytes(b"compiled-production-fixture")
            tests = root / "classes/test"
            tests.mkdir(parents=True)
            (tests / "ActualTest.class").write_bytes(b"must-not-package")
            resources = root / "src/main/resources/assets/example"
            resources.mkdir(parents=True)
            (resources / "item.json").write_text('{"parent":"item/generated"}')
            template = root / "src/main/templates/META-INF/neoforge.mods.toml"
            template.parent.mkdir(parents=True)
            template.write_text('modId="${mod_id}"\nversion="${mod_version}"\n')
            for source in runner.PACKAGED_FILES:
                path = root / source
                path.parent.mkdir(parents=True, exist_ok=True)
                path.write_text("attribution fixture")
            output = root / "local.jar"
            runner.package_local_artifact(root, classes, output,
                                          {"mod_id": "dungeonsalive", "mod_version": "0.4.0-alpha"}, {"kind": "fixture"})
            with zipfile.ZipFile(output) as jar:
                self.assertIn("Actual.class", jar.namelist())
                self.assertNotIn("ActualTest.class", jar.namelist())
                self.assertIn("assets/example/item.json", jar.namelist())
                self.assertEqual('modId="dungeonsalive"\nversion="0.4.0-alpha"\n',
                                 jar.read("META-INF/neoforge.mods.toml").decode())
                self.assertTrue(set(runner.PACKAGED_FILES.values()).issubset(jar.namelist()))
            with self.assertRaisesRegex(RuntimeError, "Unknown"):
                runner.expand_metadata("${missing}", {})


if __name__ == "__main__":
    unittest.main()
