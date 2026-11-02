#!/usr/bin/env python3
"""Prepare or explicitly run the genuine NeoForge headless GameTest recipe locally.

Uses only the exact cached 21.1.265 ModDev configuration, prepared Minecraft JAR,
and a fresh local test JAR proven by check_java_local.py. No Gradle, download,
launcher IPC, account data, installed mods, or existing world is involved.
The default only prepares a new isolated build/local-gametest directory.
--run explicitly starts Java; this is a direct GameTest run, not a Gradle task.

NeoForge's own GameTest Eula implementation supports CI directly. This helper
does not write eula.txt or change an agreement flag. It uses the original
forgeserverdev target and neoforge.gameTestServer properties from the cached
official configuration. Standard Minecraft/Authlib startup may log unsuccessful
offline service checks; this script never downloads dependencies.
"""
from __future__ import annotations

import argparse
from datetime import datetime, timezone
import json
import os
from pathlib import Path
import re
import subprocess
import sys
import tempfile
import zipfile

sys.dont_write_bytecode = True
from check_java_local import ROOT, argfile, artifact_destination, read_classpath
from run_offline_tests import cached_jar, java_tool, sha256


def bootstrap_command(java: Path, vm_file: Path, minecraft: Path, libraries: list[Path], program_args: list[str]) -> list[str]:
    # Java expands its VM @file before Main. BootstrapLauncher itself does not
    # expand @programArgs after Main; pass those arguments as real argv entries.
    return [str(java), "@" + str(vm_file), "-classpath", os.pathsep.join(map(str, [minecraft, *libraries])),
            "cpw.mods.bootstraplauncher.BootstrapLauncher", *program_args]


def official_recipe(config: dict, cache: Path, legacy: Path, mod_folder: Path) -> tuple[list[str], list[str], list[Path]]:
    run = config.get("runs", {}).get("gameTestServer", {})
    if (run.get("main") != "cpw.mods.bootstraplauncher.BootstrapLauncher"
            or not run.get("server") or not run.get("gameTest") or run.get("client")
            or run.get("args", [])[:2] != ["--launchTarget", "forgeserverdev"]):
        raise RuntimeError("Cached config is not the expected genuine NeoForge GameTest server recipe.")
    properties = run.get("props", {})
    if properties.get("neoforge.enableGameTest") != "true" or properties.get("neoforge.gameTestServer") != "true":
        raise RuntimeError("Official GameTest flags are missing; refusing an ordinary dedicated-server fallback.")
    modules = [cached_jar(cache, *coordinate.split(":")) for coordinate in config["modules"]]
    replacements = {
        "{modules}": os.pathsep.join(map(str, modules)),
        "{minecraft_classpath_file}": str(legacy),
    }

    def resolve(value: str) -> str:
        for key, replacement in replacements.items():
            value = value.replace(key, replacement)
        if re.search(r"\{[^{}]+\}", value):
            raise RuntimeError(f"Unresolved official launch placeholder: {value}")
        return value

    vm_args = [resolve(value) for value in run["jvmArgs"]]
    vm_args += [f"-D{key}={resolve(value)}" for key, value in properties.items()]
    vm_args += ["-Xmx2G", "-Djava.awt.headless=true", "-Dneoforge.enabledGameTestNamespaces=dungeonsalive",
                "-Dfml.modFolders=dungeonsalive%%" + str(mod_folder)]
    return vm_args, [resolve(value) for value in run["args"]], modules


def validate_artifact(report: dict) -> Path:
    if report.get("status") != "passed" or report.get("junit_exit_code") != 0:
        raise RuntimeError("Local Java report must contain successful compilation and actual JUnit execution.")
    artifact = report.get("artifact", {})
    if artifact.get("kind") != "local_direct_javac_test_artifact_not_gradle_release":
        raise RuntimeError("No separately produced local test JAR is recorded in this report.")
    path = artifact_destination(Path(artifact["path"]))
    if not path.is_file() or sha256(path) != artifact.get("sha256"):
        raise RuntimeError("Local test JAR is missing or differs from its verified report.")
    for entry in report.get("sources", []) + report.get("resource_inputs", []) + report.get("packaging_inputs", []) + report.get("build_configuration", []):
        source = ROOT / entry["path"]
        if not source.is_file() or sha256(source) != entry["sha256"]:
            raise RuntimeError(f"Project input changed after the local JAR check: {entry['path']}; rerun check_java_local.py.")
    current = {p.relative_to(ROOT).as_posix() for folder in ("src/main/java", "src/test/java")
               for p in (ROOT / folder).rglob("*.java")}
    if current != {entry["path"] for entry in report.get("sources", [])}:
        raise RuntimeError("Java source set changed after the local JAR check.")
    current_resources = {p.relative_to(ROOT).as_posix() for folder in ("src/main/resources", "src/test/resources")
                         for p in (ROOT / folder).rglob("*") if p.is_file()}
    if current_resources != {entry["path"] for entry in report.get("resource_inputs", [])}:
        raise RuntimeError("Resource file set changed after the local JAR check.")
    return path


def main() -> int:
    parser = argparse.ArgumentParser(description=__doc__, formatter_class=argparse.RawDescriptionHelpFormatter)
    parser.add_argument("--java-home", type=Path, default=os.environ.get("JAVA_HOME"))
    parser.add_argument("--dependency-cache", type=Path,
                        default=Path(os.environ.get("GRADLE_USER_HOME", Path.home() / ".gradle")) / "caches/modules-2/files-2.1")
    parser.add_argument("--check-report", type=Path, default=ROOT / "reports/local-java-check.json")
    parser.add_argument("--run", action="store_true", help="Explicitly start the prepared headless GameTest server.")
    parser.add_argument("--timeout-seconds", type=int, default=600)
    args = parser.parse_args()
    if args.timeout_seconds <= 0:
        parser.error("--timeout-seconds must be positive")
    check_path = args.check_report.expanduser().resolve()
    if not check_path.is_file():
        parser.error(f"Missing {check_path}; run check_java_local.py --artifact build/local-check/dungeonsalive-0.4.0-alpha.jar first.")
    report: dict = {"schema_version": 1, "status": "preparing", "started_at_utc": datetime.now(timezone.utc).isoformat(),
                    "method": "Direct original NeoForge GameTest Java recipe; not a Gradle task.",
                    "runner_sha256": sha256(Path(__file__)), "check_report": str(check_path)}
    work = None
    result = 1
    try:
        check = json.loads(check_path.read_text())
        artifact = validate_artifact(check)
        cache = args.dependency_cache.expanduser().resolve()
        candidates = sorted((cache / "net.neoforged/neoforge/21.1.265").glob("*/neoforge-21.1.265-moddev-config.json"))
        if not candidates or len({sha256(path) for path in candidates}) != 1:
            raise RuntimeError("Missing or ambiguous official cached NeoForge 21.1.265 ModDev config; no download attempted.")
        config_file = candidates[0]
        config = json.loads(config_file.read_text())
        minecraft = ROOT / "build/moddev/artifacts/neoforge-21.1.265.jar"
        dependencies = {entry["path"]: entry["sha256"] for entry in check["dependencies"]}
        if str(minecraft) not in dependencies or sha256(minecraft) != dependencies[str(minecraft)]:
            raise RuntimeError("Prepared Minecraft/NeoForge JAR differs from the successful type check.")
        java = java_tool("java", args.java_home.expanduser() if args.java_home else None)
        version = subprocess.run([str(java), "-version"], text=True, stdout=subprocess.PIPE,
                                 stderr=subprocess.STDOUT, timeout=15, check=True).stdout
        if not re.search(r'(?:openjdk|java) version "21(?:\.|"|-)', version):
            raise RuntimeError("GameTest requires the real Java 21 runtime.")
        classpath_file = Path(check["classpath_source"]["path"])
        if not classpath_file.is_file() or sha256(classpath_file) != check["classpath_source"]["sha256"]:
            raise RuntimeError("Saved dependency classpath changed after compilation, including its ordering.")
        libraries = read_classpath(classpath_file)
        for path in libraries:
            if dependencies.get(str(path)) != sha256(path):
                raise RuntimeError(f"Dependency changed after compilation: {path}")
        parent = ROOT / "build/local-gametest"
        if parent.resolve() != parent:
            raise RuntimeError("Isolated GameTest output directory must not be a symlink.")
        parent.mkdir(parents=True, exist_ok=True)
        work = Path(tempfile.mkdtemp(prefix="run-", dir=parent))
        (work / "dungeonsalive-local-gametest.txt").write_text("isolated-direct-neoforge-gametest-v1\n")
        mod_folder = work / "mod-classes"
        mod_folder.mkdir()
        with zipfile.ZipFile(artifact) as jar:
            for entry in jar.infolist():
                target = mod_folder / entry.filename
                if not target.resolve().is_relative_to(mod_folder):
                    raise RuntimeError("Unsafe local test JAR entry.")
            jar.extractall(mod_folder)
        legacy = work / "legacyClasspath.txt"
        legacy.write_text("\n".join(map(str, libraries)) + "\n")
        vm_args, program_args, modules = official_recipe(config, cache, legacy, mod_folder)
        for module in modules:
            if dependencies.get(str(module)) != sha256(module):
                raise RuntimeError(f"Official boot module differs from checked dependencies: {module}")
        vm_file, program_file = work / "vmArgs.txt", work / "programArgs.txt"
        vm_file.write_text(argfile(vm_args))
        program_file.write_text(argfile(program_args))
        command = bootstrap_command(java, vm_file, minecraft, libraries, program_args)
        report.update({"status": "prepared_not_run", "work_directory": str(work), "java_version": version.strip(),
                       "artifact": {"path": str(artifact), "sha256": sha256(artifact)},
                       "official_configuration": {"path": str(config_file), "sha256": sha256(config_file)},
                       "command": command, "environment": {"MOD_CLASSES": "removed if inherited; fml.modFolders selects this isolated copy"}})
        result = 0
        if args.run:
            log = work / "process.log"
            environment = os.environ.copy()
            environment.pop("MOD_CLASSES", None)
            with log.open("w") as output:
                try:
                    executed = subprocess.run(command, cwd=work, env=environment, stdout=output, stderr=subprocess.STDOUT,
                                              timeout=args.timeout_seconds)
                except subprocess.TimeoutExpired as error:
                    raise RuntimeError(f"GameTest exceeded {args.timeout_seconds} seconds; process stopped, inspect {log}.") from error
            text = log.read_text(errors="replace")
            passed = re.search(r"All (\d+) required tests passed", text)
            optional = re.search(r"(\d+) optional tests failed", text)
            complete = re.search(r"(\d+) GAME TESTS COMPLETE", text)
            required_failed = re.search(r"(\d+) required tests failed", text)
            failures = [{"test": name, "message": message} for name, message in re.findall(
                r"\[minecraft/LogTestReporter\]: (\S+) failed at [^!]+! (.+)", text)]
            report.update({"exit_code": executed.returncode, "log": str(log),
                           "game_test_server_started": "Started game test server" in text,
                           "summary_marker": passed.group(0) if passed else None,
                           "logged_total_test_count": int(passed.group(1)) if passed else None,
                           "completed_test_count": int(complete.group(1)) if complete else None,
                           "required_failures": int(required_failed.group(1)) if required_failed else 0,
                           "failure_messages": failures,
                           "optional_failures": int(optional.group(1)) if optional else 0})
            if executed.returncode or not passed or int(passed.group(1)) == 0:
                raise RuntimeError(f"GameTest did not confirm successful completion (exit {executed.returncode}); inspect {log}.")
            # Results remain tied to the recorded artifact; never silently present
            # them as validating sources that changed while the server was running.
            validate_artifact(check)
            report["status"] = "passed" if not optional else "required_passed_optional_failed"
            if optional:
                result = 1
    except (OSError, ValueError, RuntimeError, subprocess.SubprocessError, zipfile.BadZipFile) as error:
        report.update({"status": "failed", "error": str(error)})
        result = 1
    report["finished_at_utc"] = datetime.now(timezone.utc).isoformat()
    if work:
        report_path = work / "result.json"
        report_path.write_text(json.dumps(report, indent=2) + "\n")
        print(f"Direct GameTest: {report['status']}; report: {report_path}")
    else:
        print(f"Direct GameTest: {report['status']}")
    if "error" in report:
        print(report["error"])
    return result


if __name__ == "__main__":
    raise SystemExit(main())
