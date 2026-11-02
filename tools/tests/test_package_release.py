"""Exercise packaging against temporary fixtures, never the real release directory."""
import contextlib
import importlib.util
import io
from pathlib import Path
import stat
import tempfile
import unittest
from unittest.mock import patch
import zipfile

SPEC = importlib.util.spec_from_file_location("package_release", Path(__file__).resolve().parents[1] / "package_release.py")
package = importlib.util.module_from_spec(SPEC)
SPEC.loader.exec_module(package)


class SourceOnlyPackagingTest(unittest.TestCase):
    def setUp(self):
        self.temp = tempfile.TemporaryDirectory(prefix="dungeonsalive-package-check-")
        self.addCleanup(self.temp.cleanup)
        self.base = Path(self.temp.name)
        self.root = self.base / "project"
        self.root.mkdir()
        self.output = self.base / "output" / package.SOURCE_NAME

    def put(self, name, content=b"fixture\n"):
        path = self.root / name
        path.parent.mkdir(parents=True, exist_ok=True)
        path.write_bytes(content)
        return path

    def invoke(self, *arguments):
        with patch.object(package, "ROOT", self.root), patch("sys.argv", ["package_release.py", *arguments]), contextlib.redirect_stdout(io.StringIO()):
            package.main()

    def test_source_zip_preserves_inputs_and_has_exact_binary_exception_and_skill_allowlist(self):
        accepted = {"README.md", "gradlew", "build.gradle", "gradle/wrapper/gradle-wrapper.jar",
                    "gradle/wrapper/gradle-wrapper.properties", "src/main/java/example/world/KeyGate.java",
                    "src/main/resources/data/example/structure/template.nbt", "docs/current.md", "reports/current.json",
                    "reports/checksums.sha256", "third_party/example/source/model.json", "third_party/example/source/pixels.png"}
        accepted |= {f"skills/{name}/SKILL.md" for name in package.PROJECT_SKILLS}
        rejected = {"dist/old-mod.jar", "build/libs/new-mod.jar", "third_party/downloaded-mod.JAR", "docs/extra.jar",
                    "gradle/wrapper/unexpected.jar", "docs/embedded.zip", "tools/__pycache__/helper.pyc",
                    "tools/.pytest_cache/state.json", "tools/.venv/installed.py",
                    "tools/build/generated.txt", "reports/cache/cached.json", ".gradle/caches/cache.bin",
                    "run-gametest/world/level.dat", "docs/old-world/level.dat", "docs/old-world/data/state.dat",
                    "third_party/abandoned/region/r.0.0.mca", "skills/unrelated/SKILL.md", ".agents/settings.json"}
        for name in accepted | rejected:
            self.put(name)
        before = {p.relative_to(self.root): p.read_bytes() for p in self.root.rglob("*") if p.is_file()}
        self.invoke("--source-only", "--output", str(self.output))
        with zipfile.ZipFile(self.output) as archive:
            paths = {name.removeprefix("dungeons-alive/") for name in archive.namelist()}
            self.assertEqual(paths, accepted | {package.SOURCE_NOTICE_NAME})
            self.assertEqual([name for name in paths if name.lower().endswith(".jar")], ["gradle/wrapper/gradle-wrapper.jar"])
            self.assertIn("NOT been completed", archive.read("dungeons-alive/" + package.SOURCE_NOTICE_NAME).decode())
            self.assertTrue((archive.getinfo("dungeons-alive/gradlew").external_attr >> 16) & stat.S_IXUSR)
            self.assertIsNone(archive.testzip())
        after = {p.relative_to(self.root): p.read_bytes() for p in self.root.rglob("*") if p.is_file()}
        self.assertEqual(after, before, "Source mode must not alter dist, checksums or any input/report")

    def test_source_cli_needs_no_built_jar_or_validation_reports(self):
        self.put("README.md")
        self.invoke("--source-only", "--output", str(self.output))
        self.assertTrue(self.output.is_file())
        self.assertFalse((self.root / "dist").exists())
        self.assertFalse((self.root / "reports").exists())

    def test_normal_release_retains_built_jar_and_evidence_requirements(self):
        with self.assertRaisesRegex(SystemExit, "Build the mod first"):
            self.invoke("--output", str(self.base / "release.zip"))
        jar_name = f"dungeonsalive-{package.VERSION}.jar"
        self.put("build/libs/" + jar_name, b"fixture mod jar")
        with self.assertRaisesRegex(SystemExit, "Missing validation evidence"):
            self.invoke("--output", str(self.base / "release.zip"))
        self.put("reports/test-results.json")
        self.put("reports/server-checks.txt")
        self.put("dist/older.jar")
        self.put("gradle/wrapper/gradle-wrapper.jar")
        self.invoke("--output", str(self.base / "release.zip"))
        with zipfile.ZipFile(self.base / "release.zip") as archive:
            self.assertIn("dungeons-alive/dist/" + jar_name, archive.namelist())
            self.assertNotIn("dungeons-alive/dist/older.jar", archive.namelist())
            self.assertNotIn("dungeons-alive/" + package.SOURCE_NOTICE_NAME, archive.namelist())
        self.assertEqual((self.root / "dist" / jar_name).read_bytes(), b"fixture mod jar")
        self.assertTrue((self.root / "reports/checksums.sha256").is_file())

    def test_selected_source_symlinks_are_rejected_and_unlisted_skills_are_ignored(self):
        target = self.base / "outside.txt"; target.write_text("outside")
        for name in ("README.md", "gradle/wrapper/gradle-wrapper.jar", "skills/dungeons-native-qa/SKILL.md", "docs/linked"):
            link = self.root / name; link.parent.mkdir(parents=True, exist_ok=True); link.symlink_to(target)
            with self.assertRaisesRegex(SystemExit, "symbolic link"):
                package.select_release_files(self.root, "", source_only=True)
            link.unlink()
        link = self.root / "skills/unrelated/linked"; link.parent.mkdir(parents=True); link.symlink_to(target)
        self.assertEqual(package.select_release_files(self.root, "", source_only=True), [])
        for name in ("docs", "skills/dungeons-native-qa"):
            folder = self.root / name
            if folder.exists(): folder.rmdir()
            folder.symlink_to(self.base, target_is_directory=True)
            with self.assertRaisesRegex(SystemExit, "symbolic-link directory"):
                package.select_release_files(self.root, "", source_only=True)
            folder.unlink()

    def test_source_filename_and_external_output_are_required(self):
        with self.assertRaisesRegex(SystemExit, "unverified source filename"):
            self.invoke("--source-only", "--output", str(self.base / "release.zip"))
        with self.assertRaisesRegex(SystemExit, "outside the project"):
            self.invoke("--source-only", "--output", str(self.root / package.SOURCE_NAME))


if __name__ == "__main__":
    unittest.main()
