#!/usr/bin/env python3
"""Prepare, or explicitly run, the original cached NeoForge development client.

Requires a fresh passing check_java_local.py report with a separate local test
JAR. Default: prepare only. --run starts Java in a new marked build/local-client
directory; it never reads launcher accounts or changes an installed client.
An optional explicitly marked --world-source is copied read-only into this run.
This is a development launch, not a Gradle build or proof of visual correctness.
"""
from __future__ import annotations

import argparse
from datetime import datetime, timezone
import json
import os
from pathlib import Path
import re
import shutil
import subprocess
import sys
import tempfile
import zipfile

sys.dont_write_bytecode = True
from check_java_local import ROOT, argfile, read_classpath
from run_gametest_local import bootstrap_command, validate_artifact
from run_offline_tests import cached_jar, java_tool, sha256

WORLD_MARKER = "dungeonsalive-living-showcase-source.txt"
WORLD_MARKER_CONTENT = "isolated-living-showcase-world-v1"
RUN_MARKER = "dungeonsalive-local-client.txt"
RUN_MARKER_CONTENT = "isolated-direct-neoforge-client-v1"
CLIENT_OPTIONS = """version:3955
onboardAccessibility:false
tutorialStep:none
renderDistance:6
simulationDistance:5
guiScale:2
fullscreen:false
pauseOnLostFocus:false
maxFps:30
lang:de_de
soundCategory_master:0.0
renderClouds:false
"""


def write_fresh_options(work: Path) -> None:
    if (work / RUN_MARKER).read_text().strip() != RUN_MARKER_CONTENT:
        raise RuntimeError("Refusing options outside the marked isolated client run.")
    # Exclusive creation: never overwrite an existing player's options.
    with (work / "options.txt").open("x") as output:
        output.write(CLIENT_OPTIONS)


def checked_assets(properties: Path) -> dict:
    values = {}
    for line in properties.read_text().splitlines():
        if line.strip() and not line.lstrip().startswith(("#", "!")) and "=" in line:
            key, value = line.split("=", 1)
            values[key.strip()] = value.strip()
    index = values.get("asset_index", "")
    if not re.fullmatch(r"[\w.-]+", index):
        raise RuntimeError("Missing or invalid original ModDev asset index; no download attempted.")
    root = Path(values.get("assets_root", "")).expanduser().resolve()
    index_file = root / "indexes" / (index + ".json")
    if not index_file.is_file():
        raise RuntimeError(f"Missing cached asset index {index_file}; no download attempted.")
    objects = json.loads(index_file.read_text()).get("objects", {})
    if not objects:
        raise RuntimeError("Cached asset index contains no objects.")
    for value in objects.values():
        digest = value.get("hash", "")
        if not re.fullmatch(r"[0-9a-f]{40}", digest):
            raise RuntimeError("Malformed object digest in original asset index.")
        path = root / "objects" / digest[:2] / digest
        if not path.is_file() or path.stat().st_size != value.get("size"):
            raise RuntimeError(f"Missing or incomplete cached asset {digest}; no download attempted.")
    return {"index": index, "root": str(root), "index_file": str(index_file),
            "index_sha256": sha256(index_file), "object_count": len(objects),
            "validation": "All indexed object files exist with expected byte lengths; index SHA256 recorded."}


def original_client_recipe(config: dict, cache: Path, legacy: Path, mod_folder: Path,
                           assets: dict, showcase: bool, macos: bool) -> tuple[list[str], list[str], list[Path]]:
    run = config.get("runs", {}).get("client", {})
    if (run.get("main") != "cpw.mods.bootstraplauncher.BootstrapLauncher"
            or not run.get("client") or run.get("server")
            or run.get("args", [])[:2] != ["--launchTarget", "forgeclientdev"]):
        raise RuntimeError("Expected the genuine cached NeoForge forgeclientdev recipe.")
    modules = [cached_jar(cache, *coordinate.split(":")) for coordinate in config["modules"]]
    replacements = {"{modules}": os.pathsep.join(map(str, modules)),
                    "{minecraft_classpath_file}": str(legacy),
                    "{asset_index}": assets["index"], "{assets_root}": assets["root"]}

    def resolve(value: str) -> str:
        for key, replacement in replacements.items():
            value = value.replace(key, replacement)
        if re.search(r"\{[^{}]+\}", value):
            raise RuntimeError(f"Unresolved official client placeholder: {value}")
        return value

    vm = [resolve(value) for value in run["jvmArgs"]]
    vm += [f"-D{key}={resolve(value)}" for key, value in run.get("props", {}).items()]
    vm += ["-Xmx3G", "-Dfml.modFolders=dungeonsalive%%" + str(mod_folder)]
    # Exactly the extra macOS option used by ModDev 2.0.148 PrepareRunOrTest.
    if macos:
        vm.append("-XstartOnFirstThread")
    args = [resolve(value) for value in run["args"]]
    args += ["--width", "1280", "--height", "800"]
    # The original FML CommonDevLaunchHandler supplies its own offline Dev
    # profile and development token. No launcher accounts/tokens are read here.
    if showcase:
        vm.append("-Ddungeonsalive.livingShowcase=true")
        args += ["--quickPlaySingleplayer", "living-showcase"]
    return vm, args, modules


def copy_showcase_world(source: Path, destination: Path) -> dict:
    if source.is_symlink():
        raise RuntimeError("World source must be a real marked directory, not a symlink.")
    source = source.expanduser().resolve()
    marker = source / WORLD_MARKER
    if not marker.is_file() or marker.read_text().strip() != WORLD_MARKER_CONTENT or not (source / "level.dat").is_file():
        raise RuntimeError(f"--world-source must contain level.dat and {WORLD_MARKER} with {WORLD_MARKER_CONTENT}.")
    paths = sorted(source.rglob("*"))
    if any(path.is_symlink() for path in paths):
        raise RuntimeError("Symlinks are not allowed inside the dedicated showcase source.")
    files = [path for path in paths if path.is_file() and path.relative_to(source).as_posix() != "session.lock"]
    before = {path.relative_to(source).as_posix(): sha256(path) for path in files}
    destination.mkdir(parents=True, exist_ok=False)
    for path in files:
        target = destination / path.relative_to(source)
        target.parent.mkdir(parents=True, exist_ok=True)
        shutil.copyfile(path, target)
        if sha256(target) != before[path.relative_to(source).as_posix()]:
            raise RuntimeError("Showcase source changed while being copied; no client started.")
    after = {path.relative_to(source).as_posix(): sha256(path) for path in source.rglob("*")
             if path.is_file() and path.relative_to(source).as_posix() != "session.lock"}
    if before != after:
        raise RuntimeError("Showcase source changed while being copied; no client started.")
    return {"source": str(source), "copied_file_count": len(before), "source_sha256": before,
            "destination": str(destination), "source_writes": False, "session_lock_copied": False}


def checked_captures(work: Path, log: str) -> dict:
    directory = work / "captures"
    manifest = directory / "manifest.json"
    if not manifest.is_file():
        raise RuntimeError("Client exited without the requested native capture manifest.")
    data = json.loads(manifest.read_text())
    captures = data.get("captures", [])
    if data.get("status") != "captured" or len(captures) != 12 or "Living showcase completed: 12 native captures" not in log:
        raise RuntimeError(f"Native showcase did not complete 12 captures: {data.get('status')}; {data.get('error', '')}")
    names = set()
    for capture in captures:
        name = capture.get("file", "")
        path = directory / name
        if not name or name in names or Path(name).name != name or path.is_symlink() or not path.is_file():
            raise RuntimeError("Unsafe, missing or duplicate native capture file.")
        if sha256(path) != capture.get("sha256") or path.read_bytes()[:8] != b"\x89PNG\r\n\x1a\n":
            raise RuntimeError(f"Native capture does not match its PNG/digest manifest: {name}")
        names.add(name)
    return {"manifest": str(manifest), "manifest_sha256": sha256(manifest), "capture_count": len(captures),
            "status": "captured", "visual_review_performed": False}


def main() -> int:
    parser = argparse.ArgumentParser(description=__doc__, formatter_class=argparse.RawDescriptionHelpFormatter)
    parser.add_argument("--java-home", type=Path, default=os.environ.get("JAVA_HOME"))
    parser.add_argument("--dependency-cache", type=Path,
                        default=Path(os.environ.get("GRADLE_USER_HOME", Path.home() / ".gradle")) / "caches/modules-2/files-2.1")
    parser.add_argument("--check-report", type=Path, default=ROOT / "reports/local-java-check.json")
    parser.add_argument("--world-source", type=Path, help="Read-only copy of an explicitly marked prepared showcase world.")
    parser.add_argument("--run", action="store_true", help="Explicitly start this isolated development client.")
    parser.add_argument("--timeout-seconds", type=int, default=600, help="Stop only this child JVM after this duration.")
    args = parser.parse_args()
    if args.timeout_seconds <= 0:
        parser.error("--timeout-seconds must be positive")
    report = {"schema_version": 1, "status": "preparing", "started_at_utc": datetime.now(timezone.utc).isoformat(),
              "method": "Direct original NeoForge development client recipe; not a Gradle task or visual approval.",
              "runner_sha256": sha256(Path(__file__))}
    work = None
    result = 1
    try:
        check_path = args.check_report.expanduser().resolve()
        check = json.loads(check_path.read_text())
        artifact = validate_artifact(check)
        cache = args.dependency_cache.expanduser().resolve()
        configs = sorted((cache / "net.neoforged/neoforge/21.1.265").glob("*/neoforge-21.1.265-moddev-config.json"))
        if not configs or len({sha256(path) for path in configs}) != 1:
            raise RuntimeError("Missing or ambiguous original NeoForge 21.1.265 config; no download attempted.")
        config_file = configs[0]
        config = json.loads(config_file.read_text())
        java = java_tool("java", args.java_home.expanduser() if args.java_home else None)
        version = subprocess.run([str(java), "-version"], text=True, stdout=subprocess.PIPE,
                                 stderr=subprocess.STDOUT, timeout=15, check=True).stdout
        if not re.search(r'(?:openjdk|java) version "21(?:\.|"|-)', version):
            raise RuntimeError("The local development client requires real Java 21.")
        dependencies = {entry["path"]: entry["sha256"] for entry in check["dependencies"]}
        minecraft = ROOT / "build/moddev/artifacts/neoforge-21.1.265.jar"
        classpath_file = Path(check["classpath_source"]["path"])
        if not classpath_file.is_file() or sha256(classpath_file) != check["classpath_source"]["sha256"]:
            raise RuntimeError("Saved dependency classpath changed after compilation, including its ordering.")
        libraries = read_classpath(classpath_file)
        for path in [minecraft, *libraries]:
            if dependencies.get(str(path)) != sha256(path):
                raise RuntimeError(f"Runtime dependency differs from the passing compilation: {path}")
        assets = checked_assets(ROOT / "build/moddev/minecraft_assets.properties")
        parent = ROOT / "build/local-client"
        if parent.resolve() != parent:
            raise RuntimeError("Isolated client directory must not be a symlink.")
        parent.mkdir(parents=True, exist_ok=True)
        work = Path(tempfile.mkdtemp(prefix="run-", dir=parent))
        (work / RUN_MARKER).write_text(RUN_MARKER_CONTENT + "\n")
        write_fresh_options(work)
        mod_folder = work / "mod-classes"
        mod_folder.mkdir()
        with zipfile.ZipFile(artifact) as jar:
            if any(not (mod_folder / entry.filename).resolve().is_relative_to(mod_folder) for entry in jar.infolist()):
                raise RuntimeError("Unsafe local test JAR entry.")
            jar.extractall(mod_folder)
        if args.world_source:
            report["world_copy"] = copy_showcase_world(args.world_source, work / "saves/living-showcase")
        legacy = work / "legacyClasspath.txt"
        legacy.write_text("\n".join(map(str, libraries)) + "\n")
        vm, program, modules = original_client_recipe(config, cache, legacy, mod_folder, assets,
                                                       args.world_source is not None, sys.platform == "darwin")
        for path in modules:
            if dependencies.get(str(path)) != sha256(path):
                raise RuntimeError(f"Original boot module differs from checked dependencies: {path}")
        vm_file, program_file = work / "vmArgs.txt", work / "programArgs.txt"
        vm_file.write_text(argfile(vm)); program_file.write_text(argfile(program))
        command = bootstrap_command(java, vm_file, minecraft, libraries, program)
        validate_artifact(check)
        report.update({"status": "prepared_not_run", "work_directory": str(work), "check_report": str(check_path),
                       "java_version": version.strip(), "artifact": {"path": str(artifact), "sha256": sha256(artifact)},
                       "official_configuration": {"path": str(config_file), "sha256": sha256(config_file)},
                       "assets": assets, "command": command, "visual_validation": "not_performed",
                       "profile": "Original FML development profile; no Prism/account files read."})
        result = 0
        if args.run:
            log = work / "process.log"
            environment = os.environ.copy(); environment.pop("MOD_CLASSES", None)
            with log.open("w") as output:
                try:
                    executed = subprocess.run(command, cwd=work, env=environment, stdout=output, stderr=subprocess.STDOUT,
                                              timeout=args.timeout_seconds)
                except subprocess.TimeoutExpired as error:
                    raise RuntimeError(f"This client JVM exceeded {args.timeout_seconds} seconds and was stopped; inspect {log}.") from error
            report.update({"exit_code": executed.returncode, "log": str(log)})
            if executed.returncode:
                raise RuntimeError(f"Development client exited with {executed.returncode}; inspect {log}.")
            validate_artifact(check)
            if args.world_source:
                report["capture_result"] = checked_captures(work, log.read_text(errors="replace"))
                report["status"] = "captured_visual_review_required"
            else:
                report["status"] = "client_exited_visual_review_required"
    except (OSError, ValueError, RuntimeError, subprocess.SubprocessError, zipfile.BadZipFile) as error:
        report.update({"status": "failed", "error": str(error)}); result = 1
    report["finished_at_utc"] = datetime.now(timezone.utc).isoformat()
    if work:
        path = work / "result.json"; path.write_text(json.dumps(report, indent=2) + "\n")
        print(f"Local development client: {report['status']}; report: {path}")
    else:
        print(f"Local development client: {report['status']}")
    if "error" in report:
        print(report["error"])
    return result


if __name__ == "__main__":
    raise SystemExit(main())
