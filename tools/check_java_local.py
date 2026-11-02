#!/usr/bin/env python3
"""Type-check all Java sources and run JUnit against existing local dependencies.

Requires the genuine artifacts/classpath left by a successful NeoForge Gradle
setup. Does not run Gradle, download dependencies, create sockets, or touch an
installed client/world. All compilation uses a temporary directory. An explicit
--artifact may create a separate local test JAR only after compilation/JUnit pass.
This is not a Gradle build, NeoForge bootstrap, GameTest or visual validation.

Example:
  python3 tools/check_java_local.py --java-home /path/to/jdk21 \
      --dependency-cache /path/to/gradle/caches/modules-2/files-2.1

The saved ModDev JUnit legacyClasspath supplies the Minecraft runtime libraries;
the genuine prepared NeoForge/Minecraft JAR supplies mapped game classes. JUnit
and compile-only annotations use explicit cached coordinates, never every JAR
found in a cache. No old project classes or previously built mod JAR are used.
"""
from __future__ import annotations

import argparse
from datetime import datetime, timezone
import json
import os
from pathlib import Path
import re
import shlex
import shutil
import subprocess
import sys
import tempfile
import zipfile

sys.dont_write_bytecode = True
from run_offline_tests import DEPENDENCIES, LAUNCHER_SOURCE, cached_jar, java_tool, sha256

ROOT = Path(__file__).resolve().parents[1]
COMPILE_DEPENDENCIES = (("org.jetbrains", "annotations", "24.1.0"),)
TEST_DEPENDENCIES = (*DEPENDENCIES, ("org.junit.jupiter", "junit-jupiter-params", "5.11.4"))
PACKAGED_FILES = {
    "LICENSE": "META-INF/LICENSE-Dungeons-Alive.txt",
    "THIRD_PARTY_NOTICES.md": "META-INF/THIRD_PARTY_NOTICES.md",
    "design/model-delivery-inventory.json": "META-INF/dungeonsalive/model-provenance.json",
    "design/gear-source-inventory.json": "META-INF/dungeonsalive/gear-provenance.json",
    "design/gear-armor-models.json": "META-INF/dungeonsalive/gear-armor-provenance.json",
    **{f"third_party/{name}": f"META-INF/third_party/{name}" for name in (
        "goety/LICENSE.txt", "dungeons_mobs/LICENSE.txt", "dungeons_mobs_port/LICENSE.txt",
        "dungeons_gear/LICENSE.txt", "recrafted_mobs/LICENSE.txt",
        "recrafted_mobs/UPSTREAM-LICENSE-NOTICE.txt", "recrafted_mobs/source/CREDITS.md")},
}


def artifact_destination(path: Path) -> Path:
    candidate = (path if path.is_absolute() else ROOT / path).resolve()
    # A narrow, separate output directory avoids replacing any release, launcher
    # mod, save, or successful Gradle artifact, including through symlinks.
    if candidate.parent != ROOT / "build/local-check" or candidate.suffix != ".jar":
        raise RuntimeError("--artifact must be a .jar directly under build/local-check/; no launcher/dist/build/libs output is permitted.")
    return candidate


def expand_metadata(template: str, properties: dict[str, str]) -> str:
    def replace(match: re.Match) -> str:
        key = match.group(1)
        if key not in properties:
            raise RuntimeError(f"Unknown mod metadata placeholder: {key}")
        return properties[key]
    result = re.sub(r"\$\{([\w.]+)\}", replace, template)
    if "${" in result:
        raise RuntimeError("Unexpanded mod metadata placeholder remains.")
    return result


def package_local_artifact(snapshot: Path, classes: Path, output: Path,
                           properties: dict[str, str], provenance: dict) -> None:
    entries: dict[str, bytes] = {"META-INF/MANIFEST.MF": b"Manifest-Version: 1.0\r\n\r\n"}

    def add(name: str, contents: bytes) -> None:
        if name in entries:
            raise RuntimeError(f"Duplicate local JAR entry: {name}")
        entries[name] = contents

    for path in sorted(classes.rglob("*.class")):
        add(path.relative_to(classes).as_posix(), path.read_bytes())
    resources = snapshot / "src/main/resources"
    for path in sorted(resources.rglob("*")):
        if path.is_file():
            add(path.relative_to(resources).as_posix(), path.read_bytes())
    templates = snapshot / "src/main/templates"
    for path in sorted(templates.rglob("*")):
        if path.is_file():
            add(path.relative_to(templates).as_posix(), expand_metadata(path.read_text(), properties).encode("utf-8"))
    for source, destination in PACKAGED_FILES.items():
        add(destination, (snapshot / source).read_bytes())
    add("META-INF/dungeonsalive/local-check.json", (json.dumps(provenance, indent=2) + "\n").encode("utf-8"))
    with zipfile.ZipFile(output, "w", compression=zipfile.ZIP_DEFLATED) as jar:
        for name, data in sorted(entries.items()):
            entry = zipfile.ZipInfo(name, date_time=(1980, 1, 1, 0, 0, 0))
            entry.compress_type = zipfile.ZIP_DEFLATED
            entry.external_attr = 0o100644 << 16
            jar.writestr(entry, data)


def read_classpath(path: Path) -> list[Path]:
    """Read only the previously resolved JAR list, preserving Gradle's order."""
    if not path.is_file():
        raise RuntimeError(f"Missing ModDev classpath {path}; run a normal Gradle build/setup first. No download attempted.")
    entries = [Path(line.strip()).expanduser().resolve() for line in path.read_text().splitlines() if line.strip()]
    if not entries:
        raise RuntimeError(f"Empty ModDev classpath: {path}")
    for entry in entries:
        if entry.suffix != ".jar" or not entry.is_file():
            raise RuntimeError(f"Missing or non-JAR dependency in saved classpath: {entry}. Regenerate with Gradle; no fallback used.")
        if "stubs" in entry.name.lower():
            raise RuntimeError(f"Stub JAR is not accepted as a type-check dependency: {entry}")
        with zipfile.ZipFile(entry) as jar:
            if any(name.startswith("de/laugenbreze/dungeonsalive/") and name.endswith(".class") for name in jar.namelist()):
                raise RuntimeError(f"Saved classpath contains old project classes: {entry}")
    return list(dict.fromkeys(entries))


def argfile(arguments: list[str]) -> str:
    """javac argument-file escaping (not shell quoting)."""
    if any("\n" in value or "\r" in value for value in arguments):
        raise RuntimeError("Newlines are not supported in javac arguments.")
    return "\n".join('"' + value.replace("\\", "\\\\").replace('"', '\\"') + '"' for value in arguments) + "\n"


def inventory(paths: list[Path]) -> list[dict[str, str]]:
    return [{"path": path.relative_to(ROOT).as_posix(), "sha256": sha256(path)} for path in paths]


def test_class(path: Path) -> str:
    match = re.search(r"(?m)^\s*package\s+([\w.]+)\s*;", path.read_text())
    if not match:
        raise RuntimeError(f"JUnit source has no package declaration: {path}")
    return match.group(1) + "." + path.stem


def main() -> int:
    parser = argparse.ArgumentParser(description=__doc__, formatter_class=argparse.RawDescriptionHelpFormatter)
    parser.add_argument("--java-home", type=Path, default=os.environ.get("JAVA_HOME"))
    parser.add_argument("--dependency-cache", type=Path,
                        default=Path(os.environ.get("GRADLE_USER_HOME", Path.home() / ".gradle")) / "caches/modules-2/files-2.1")
    parser.add_argument("--classpath-file", type=Path, default=ROOT / "build/moddev/junit/legacyClasspath.txt")
    parser.add_argument("--minecraft-jar", type=Path, default=ROOT / "build/moddev/artifacts/neoforge-21.1.265.jar")
    parser.add_argument("--compile-only", action="store_true", help="Compile main and test sources without executing JUnit.")
    parser.add_argument("--artifact", type=Path,
                        help="After passing compilation/JUnit, write a separate test JAR under build/local-check/ (never mods/dist/build/libs).")
    parser.add_argument("--timeout-seconds", type=int, default=180, help="Maximum duration of each compiler/JUnit process (default: 180).")
    parser.add_argument("--report", type=Path, default=ROOT / "reports/local-java-check.json")
    args = parser.parse_args()
    if args.timeout_seconds <= 0:
        parser.error("--timeout-seconds must be positive")
    if args.artifact and args.compile_only:
        parser.error("--artifact requires actual JUnit execution; it cannot be combined with --compile-only")
    report_path = args.report.expanduser().resolve()
    log_path = report_path.with_suffix(".log")
    report: dict = {
        "schema_version": 1,
        "started_at_utc": datetime.now(timezone.utc).isoformat(),
        "status": "not_run",
        "scope": "Direct javac of all main/test Java sources against genuine cached Minecraft/NeoForge classes; direct JUnit execution.",
        "platform": {"minecraft": "1.21.1", "neoforge": "21.1.265", "javac_release": 21},
        "limitations": [
            "Not a Gradle build: no dependency resolution, generator tasks, annotation processing or Gradle checks; optional local JAR packaging mirrors the current resource/template/attribution rules explicitly.",
            "JUnit runs directly without the NeoForge mod bootstrap or runtime transformations; no GameTests or client session are started.",
            "Compile success does not establish gameplay behavior, server registration, native rendering or source-model fidelity.",
            "The dependency graph comes from the previous local ModDev setup; relevant build dependency changes require refreshing it with Gradle.",
        ],
        "log": str(log_path),
        "runner_sha256": sha256(Path(__file__)),
        "junit_launcher_source_sha256": sha256(ROOT / "tools/run_offline_tests.py"),
    }
    log: list[str] = []
    exit_code = 1

    def run(command: list[str], cwd: Path) -> subprocess.CompletedProcess[str]:
        log.append("$ " + shlex.join(command) + "\n")
        try:
            result = subprocess.run(command, cwd=cwd, text=True, stdout=subprocess.PIPE,
                                    stderr=subprocess.STDOUT, timeout=args.timeout_seconds)
        except subprocess.TimeoutExpired as error:
            output = error.stdout or ""
            log.append(output.decode(errors="replace") if isinstance(output, bytes) else output)
            raise RuntimeError(f"Process exceeded {args.timeout_seconds} seconds: {command[0]}") from error
        log.append(result.stdout)
        return result

    try:
        artifact = artifact_destination(args.artifact.expanduser()) if args.artifact else None
        java_home = args.java_home.expanduser() if args.java_home else None
        java, javac = java_tool("java", java_home), java_tool("javac", java_home)
        report["java_version"] = run([str(java), "-version"], ROOT).stdout.strip()
        report["javac_version"] = run([str(javac), "-version"], ROOT).stdout.strip()
        if not re.match(r"javac 21(?:\.|\s|$)", report["javac_version"]):
            raise RuntimeError("This check requires JDK 21 exactly; supply --java-home pointing to that JDK.")
        if not re.search(r'(?:openjdk|java) version "21(?:\.|"|-)', report["java_version"]):
            raise RuntimeError("The Java runtime must also be version 21; supply --java-home pointing to JDK 21.")
        properties = dict(re.findall(r"(?m)^([\w.]+)=(.*)$", (ROOT / "gradle.properties").read_text()))
        if properties.get("minecraft_version") != "1.21.1" or properties.get("neo_version") != "21.1.265":
            raise RuntimeError("Project platform changed; refresh this runner's pinned platform/classpath deliberately.")
        minecraft = args.minecraft_jar.expanduser().resolve()
        if not minecraft.is_file() or minecraft.name != "neoforge-21.1.265.jar":
            raise RuntimeError(f"Missing genuine prepared NeoForge 21.1.265 JAR: {minecraft}; no game stub/fallback used.")
        with zipfile.ZipFile(minecraft) as jar:
            for required in ("net/minecraft/world/entity/Entity.class", "net/neoforged/neoforge/common/NeoForge.class"):
                if required not in jar.namelist():
                    raise RuntimeError(f"Prepared JAR lacks required game class {required}: {minecraft}")
        saved_classpath = args.classpath_file.expanduser().resolve()
        libraries = read_classpath(saved_classpath)
        extras = [cached_jar(args.dependency_cache.expanduser(), *item) for item in (*COMPILE_DEPENDENCIES, *TEST_DEPENDENCIES)]
        libraries = list(dict.fromkeys([minecraft, *libraries, *extras]))
        report["classpath_source"] = {"path": str(saved_classpath), "sha256": sha256(saved_classpath)}
        report["dependencies"] = [{"path": str(path), "sha256": sha256(path)} for path in libraries]
        report["build_configuration"] = inventory([ROOT / "build.gradle", ROOT / "gradle.properties"])
        sources = sorted((ROOT / "src/main/java").rglob("*.java"))
        tests = sorted((ROOT / "src/test/java").rglob("*.java"))
        if not sources or not tests:
            raise RuntimeError("No main/test Java sources were found; refusing an empty check.")
        resources = sorted(path for folder in ("src/main/resources", "src/test/resources")
                           for path in (ROOT / folder).rglob("*") if path.is_file())
        report["sources"] = inventory(sources + tests)
        report["resource_inputs"] = inventory(resources)
        packaging_files = (sorted((ROOT / "src/main/templates").rglob("*"))
                           + [ROOT / path for path in PACKAGED_FILES]) if artifact else []
        packaging_files = [path for path in packaging_files if path.is_file()]
        if artifact and any(not (ROOT / path).is_file() for path in PACKAGED_FILES):
            raise RuntimeError("A required attribution/provenance file is missing; refusing a local JAR without it.")
        if artifact and not (ROOT / "src/main/templates/META-INF/neoforge.mods.toml").is_file():
            raise RuntimeError("The native mod metadata template is missing.")
        report["packaging_inputs"] = inventory(packaging_files)
        report["main_source_count"], report["test_source_count"] = len(sources), len(tests)
        report["test_classes"] = [test_class(path) for path in tests if path.stem.endswith(("Test", "Tests"))]
        if not report["test_classes"]:
            raise RuntimeError("No *Test.java or *Tests.java JUnit classes were found.")
        with tempfile.TemporaryDirectory(prefix="dungeonsalive-java-check-") as temporary:
            work = Path(temporary)
            # A snapshot keeps compiler input stable while other agents work.
            for entry in report["sources"] + report["resource_inputs"] + report["packaging_inputs"]:
                original = ROOT / entry["path"]
                copied = work / entry["path"]
                copied.parent.mkdir(parents=True, exist_ok=True)
                shutil.copyfile(original, copied)
                if sha256(copied) != entry["sha256"]:
                    raise RuntimeError(f"Input changed while being copied; rerun after edits settle: {entry['path']}")
            main_classes, test_classes = work / "classes/main", work / "classes/test"
            main_classes.mkdir(parents=True)
            test_classes.mkdir(parents=True)
            classpath = os.pathsep.join(map(str, libraries))
            launcher = work / "LocalTestLauncher.java"
            launcher.write_text(LAUNCHER_SOURCE.replace("OfflineTestLauncher", "LocalTestLauncher"), encoding="utf-8")
            for phase, group, output, cp in (
                ("main", sources, main_classes, classpath),
                ("test", tests, test_classes, str(main_classes) + os.pathsep + classpath),
            ):
                inputs = [str(work / path.relative_to(ROOT)) for path in group]
                if phase == "test":
                    inputs.append(str(launcher))
                arguments = ["--release", "21", "-encoding", "UTF-8", "-proc:none", "-sourcepath", "",
                             "-classpath", cp, "-d", str(output), *inputs]
                argument_file = work / f"javac-{phase}.args"
                argument_file.write_text(argfile(arguments), encoding="utf-8")
                log.append(f"\nCompiling {phase}: {len(group)} project Java sources\n")
                compiled = run([str(javac), "@" + str(argument_file)], work)
                report[f"compile_{phase}"] = {"exit_code": compiled.returncode, "source_count": len(group)}
                if compiled.returncode:
                    raise RuntimeError(f"javac {phase} failed with exit {compiled.returncode}; see {log_path}.")
            if args.compile_only:
                report["junit"] = {"status": "not_run", "reason": "--compile-only"}
            else:
                result_file = work / "junit-results.json"
                runtime_cp = os.pathsep.join(map(str, [test_classes, main_classes,
                        work / "src/main/resources", work / "src/test/resources", *libraries]))
                # No ModDev bootstrap: the report explicitly distinguishes these
                # ordinary JUnit tests from transformed/server integration tests.
                executed = run([str(java), "-classpath", runtime_cp, "LocalTestLauncher", str(result_file),
                                *report["test_classes"]], work)
                report["junit_exit_code"] = executed.returncode
                if result_file.is_file():
                    report["junit"] = json.loads(result_file.read_text())
                if executed.returncode or "junit" not in report:
                    raise RuntimeError(f"Direct JUnit failed or produced no result (exit {executed.returncode}); see actual log/results.")
            staged_artifact = work / "local-test.jar"
            if artifact:
                package_local_artifact(work, main_classes, staged_artifact, properties, {
                    "kind": "local_direct_javac_test_artifact_not_gradle_release",
                    "platform": report["platform"], "junit_counts": report["junit"]["counts"],
                    "source_inputs": report["sources"],
                    "limitations": report["limitations"],
                })
            original_inputs = report["sources"] + report["resource_inputs"] + report["build_configuration"] + report["packaging_inputs"]
            changed = [entry["path"] for entry in original_inputs
                       if not (ROOT / entry["path"]).is_file() or sha256(ROOT / entry["path"]) != entry["sha256"]]
            current_sources = set((ROOT / "src/main/java").rglob("*.java")) | set((ROOT / "src/test/java").rglob("*.java"))
            changed += [path.relative_to(ROOT).as_posix() for path in current_sources - set(sources + tests)]
            current_resources = {path for folder in ("src/main/resources", "src/test/resources")
                                 for path in (ROOT / folder).rglob("*") if path.is_file()}
            changed += [path.relative_to(ROOT).as_posix() for path in current_resources - set(resources)]
            for dependency in report["dependencies"] + [report["classpath_source"]]:
                path = Path(dependency["path"])
                if not path.is_file() or sha256(path) != dependency["sha256"]:
                    changed.append(str(path))
            if changed:
                report["changed_during_check"] = sorted(set(changed))
                raise RuntimeError("Inputs changed during this check; results apply only to recorded snapshot. Rerun after edits settle.")
            if artifact:
                artifact.parent.mkdir(parents=True, exist_ok=True)
                # Stage in the output filesystem, then replace only this separate
                # local-check artifact; interrupted copies cannot leave half a JAR.
                with tempfile.NamedTemporaryFile(prefix=".local-check-", suffix=".jar", dir=artifact.parent, delete=False) as handle:
                    candidate = Path(handle.name)
                try:
                    shutil.copyfile(staged_artifact, candidate)
                    candidate.replace(artifact)
                finally:
                    candidate.unlink(missing_ok=True)
                report["artifact"] = {"path": str(artifact), "sha256": sha256(artifact),
                                      "size_bytes": artifact.stat().st_size,
                                      "kind": "local_direct_javac_test_artifact_not_gradle_release"}
        report["status"] = "passed"
        exit_code = 0
    except (OSError, RuntimeError, ValueError, zipfile.BadZipFile) as error:
        report["status"] = "failed"
        report["error"] = str(error)
        log.append("ERROR: " + str(error) + "\n")
    report["finished_at_utc"] = datetime.now(timezone.utc).isoformat()
    report_path.parent.mkdir(parents=True, exist_ok=True)
    log_path.write_text("".join(log), encoding="utf-8")
    report_path.write_text(json.dumps(report, indent=2, ensure_ascii=False) + "\n", encoding="utf-8")
    print(f"Local Java check: {report['status']}; report: {report_path}; log: {log_path}")
    if "junit" in report:
        print(json.dumps(report["junit"].get("counts", report["junit"])))
    if "error" in report:
        print(report["error"])
    return exit_code


if __name__ == "__main__":
    raise SystemExit(main())
