"""The local launcher must retain the official GameTest target and isolation."""
import importlib.util
from pathlib import Path
import sys
import tempfile
import unittest
from unittest.mock import patch

TOOLS = Path(__file__).resolve().parents[1]
sys.path.insert(0, str(TOOLS))
sys.dont_write_bytecode = True
spec = importlib.util.spec_from_file_location("run_gametest_local", TOOLS / "run_gametest_local.py")
runner = importlib.util.module_from_spec(spec)
spec.loader.exec_module(runner)


def config():
    return {"modules": ["example:bootstrap:1"], "runs": {"gameTestServer": {
        "main": "cpw.mods.bootstraplauncher.BootstrapLauncher", "server": True,
        "client": False, "gameTest": True,
        "jvmArgs": ["-p", "{modules}"],
        "args": ["--launchTarget", "forgeserverdev", "--gameDir", "."],
        "props": {"legacyClassPath.file": "{minecraft_classpath_file}",
                  "neoforge.enableGameTest": "true", "neoforge.gameTestServer": "true"}}}}


class DirectGameTestRecipeTest(unittest.TestCase):
    def test_program_arguments_are_real_argv_after_main_not_an_unexpanded_file(self):
        command = runner.bootstrap_command(Path("java with space"), Path("vm args.txt"), Path("minecraft.jar"),
                                            [Path("dependency.jar")], ["--launchTarget", "forgeserverdev", "--gameDir", "."])
        main = command.index("cpw.mods.bootstraplauncher.BootstrapLauncher")
        self.assertEqual(["--launchTarget", "forgeserverdev", "--gameDir", "."], command[main + 1:])
        self.assertEqual("@vm args.txt", command[1])
        self.assertEqual("java with space", command[0])

    def test_original_target_flags_and_isolated_mod_folder_are_preserved(self):
        with tempfile.TemporaryDirectory() as temporary:
            root = Path(temporary)
            with patch.object(runner, "cached_jar", return_value=root / "bootstrap.jar"):
                vm, args, modules = runner.official_recipe(config(), root, root / "classpath.txt", root / "mod-classes")
            self.assertEqual(["--launchTarget", "forgeserverdev", "--gameDir", "."], args)
            self.assertIn("-Dneoforge.gameTestServer=true", vm)
            self.assertIn("-Dfml.modFolders=dungeonsalive%%" + str(root / "mod-classes"), vm)
            self.assertIn("-DlegacyClassPath.file=" + str(root / "classpath.txt"), vm)
            self.assertEqual([root / "bootstrap.jar"], modules)
            self.assertFalse(any("eula" in value.lower() for value in vm))

    def test_ordinary_server_fallback_and_unresolved_placeholders_are_rejected(self):
        data = config()
        data["runs"]["gameTestServer"]["props"].pop("neoforge.gameTestServer")
        with self.assertRaisesRegex(RuntimeError, "ordinary dedicated-server"):
            runner.official_recipe(data, Path("cache"), Path("classpath"), Path("mods"))
        data = config()
        data["runs"]["gameTestServer"]["args"].append("{missing}")
        with patch.object(runner, "cached_jar", return_value=Path("bootstrap.jar")):
            with self.assertRaisesRegex(RuntimeError, "Unresolved"):
                runner.official_recipe(data, Path("cache"), Path("classpath"), Path("mods"))


if __name__ == "__main__":
    unittest.main()
