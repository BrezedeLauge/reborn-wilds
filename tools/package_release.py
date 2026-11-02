#!/usr/bin/env python3
"""Package a validated release, or explicitly package an unverified source snapshot.

Minecraft, downloaded mods, build caches and local game worlds are intentionally
excluded. The normal release requires the project's built JAR and validation
evidence. --source-only never copies a mod JAR or rewrites validation evidence.
"""
from __future__ import annotations

import argparse
import hashlib
import json
from pathlib import Path
import shutil
import stat
import zipfile

ROOT = Path(__file__).resolve().parents[1]
VERSION = next(line.split('=', 1)[1].strip() for line in (ROOT / 'gradle.properties').read_text().splitlines()
               if line.startswith('mod_version='))
DIRECTORIES = {"src", "gradle", "docs", "tools", "profiles", "examples", "design", "schemas", "reports", "third_party", "dist"}
TOP_FILES = {"README.md", "AGENTS.md", "CODEX_START.md", "MINECRAFT_2_IDEEN_FUER_CODEX.md", "LICENSE", "THIRD_PARTY_NOTICES.md", ".gitignore",
             "build.gradle", "settings.gradle", "gradle.properties", "gradlew", "gradlew.bat"}
PROJECT_SKILLS = {"dungeons-neoforge-runtime", "dungeons-models-animation", "dungeons-native-qa"}
SOURCE_NAME = "DungeonsAlive_Source_Unverified_2026-11-02.zip"
LOCAL_TEST_NAME = f"DungeonsAlive_LocalTest_{VERSION}_2026-11-02.zip"
SOURCE_NOTICE_NAME = "UNVERIFIED_SOURCE_NOTICE.txt"
WRAPPER_BOOTSTRAP = Path("gradle/wrapper/gradle-wrapper.jar")
SOURCE_OMIT_PARTS = {"build", "dist", "target", "out", "__pycache__", "node_modules", ".gradle", ".m2",
                     ".cache", "cache", "caches", ".local-build", ".git", ".agents", ".codex", ".minecraft",
                     ".pytest_cache", ".mypy_cache", ".ruff_cache", ".idea", ".vscode", ".venv", "venv", ".tox"}
SOURCE_OMIT_SUFFIXES = {".class", ".pyc", ".pyo", ".zip", ".tar", ".gz", ".tgz", ".7z", ".mca", ".mcc"}
SOURCE_NOTICE = """DUNGEONS ALIVE — UNVERIFIED SOURCE SNAPSHOT — 2026-11-02

This archive is an unverified development working tree, not an installable mod
release. It contains source, documentation, reports and attributed asset/source
snapshots. No mod JAR, Minecraft JAR, third-party mod binary, build cache or local
game world is included. The existing official Gradle wrapper bootstrap JAR is
included solely so the project can still be started with ./gradlew.

The current Minecraft build, dedicated-server GameTests and native-client checks
have NOT been completed. Build attempts stopped while resolving the Gradle
distribution because services.gradle.org was unavailable through DNS. Passing
Minecraft-independent JUnit tests do not validate Minecraft APIs or rendering.
Reports may describe older builds or limited checks; they are historical evidence,
not proof that this complete source snapshot is compiled or playable.

Required platform: Minecraft Java 1.21.1, NeoForge 21.1.265, Java 21.
Once the official build dependencies are available, run ./gradlew build and
./gradlew runGameTestServer, then perform the native-client checks documented in
docs/VALIDATION.md. Preserve the provenance and licenses in THIRD_PARTY_NOTICES.md.

This source-only packaging mode does not update dist/, checksums.sha256 or any
validation report. The unchanged normal packaging mode still requires a built JAR
and the documented validation evidence before it can create a release archive.
"""


def source_excluded(root: Path, path: Path) -> bool:
    """Exclude generated payloads while retaining Java's legitimate 'world' packages."""
    relative = path.relative_to(root)
    if any(part in SOURCE_OMIT_PARTS or part.startswith("run-") for part in relative.parts):
        return True
    if path.name == ".DS_Store" or path.suffix.lower() in SOURCE_OMIT_SUFFIXES:
        return True
    if path.suffix.lower() == ".jar" and relative != WRAPPER_BOOTSTRAP:
        return True
    # Do not mistake src/.../world or worldgen resources for a saved game. A world
    # marker identifies the complete save directory, including its datapacks/data.
    for folder in (path, *path.parents):
        if folder == root:
            break
        if folder.is_dir() and any((folder / marker).exists() for marker in ("level.dat", "level.dat_old", "session.lock")):
            return True
    return False


def select_release_files(root: Path, jar_name: str, *, source_only: bool = False) -> list[Path]:
    """Select release inputs without copying the JAR or writing an archive.

    Skill sources have their own explicit allowlist. Never traverse the general
    skills directory or local .agents configuration.
    """
    files = []
    candidates = [root / name for name in sorted(TOP_FILES)]
    skill_root = root / "skills"
    if skill_root.is_symlink():
        raise SystemExit("Refusing to package a symbolic-link directory: skills")
    selected_directories = DIRECTORIES - {"dist"} if source_only else DIRECTORIES
    directories = sorted(selected_directories) + [f"skills/{name}" for name in sorted(PROJECT_SKILLS)]
    for directory in directories:
        folder = root / directory
        if folder.is_symlink():
            raise SystemExit(f"Refusing to package a symbolic-link directory: {directory}")
        if folder.is_dir():
            candidates.extend(folder.rglob("*"))
    for path in candidates:
        relative = path.relative_to(root)
        if source_only and path.is_symlink():
            raise SystemExit(f"Refusing to package a symbolic link: {relative}")
        if source_only and source_excluded(root, path):
            continue
        selected_skill = (len(relative.parts) >= 3 and relative.parts[0] == "skills"
                          and relative.parts[1] in PROJECT_SKILLS)
        if relative.parts[0] not in DIRECTORIES and str(relative) not in TOP_FILES and not selected_skill:
            continue
        if "__pycache__" in relative.parts or path.suffix in {".pyc", ".pyo"}:
            continue
        if relative.parts[0] == "dist" and path != root / "dist" / jar_name:
            continue
        if path.is_symlink():
            raise SystemExit(f"Refusing to package a symbolic link: {relative}")
        if path.is_file():
            files.append(path)
    return sorted(files)


def write_archive(root: Path, files: list[Path], output: Path, *, source_only: bool = False) -> None:
    """Write selected files; source mode adds its warning inside the ZIP only."""
    output.parent.mkdir(parents=True, exist_ok=True)
    with zipfile.ZipFile(output, "w", compression=zipfile.ZIP_DEFLATED, compresslevel=9) as archive:
        def add(relative: str, content: bytes, mode: int = 0o644) -> None:
            entry = zipfile.ZipInfo(f"dungeons-alive/{relative}", date_time=(2026, 11, 2, 0, 0, 0))
            entry.create_system = 3
            entry.external_attr = (stat.S_IFREG | mode) << 16
            entry.compress_type = zipfile.ZIP_DEFLATED
            archive.writestr(entry, content)
        for path in sorted(files):
            add(path.relative_to(root).as_posix(), path.read_bytes(), 0o755 if path.name == "gradlew" else 0o644)
        if source_only:
            add(SOURCE_NOTICE_NAME, SOURCE_NOTICE.encode("utf-8"))


def main() -> None:
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--source-only", action="store_true", help="Package unverified sources without a built mod JAR or report/checksum updates")
    parser.add_argument("--local-test", action="store_true", help="Package the separately verified direct-Java test JAR without changing dist or the Gradle release")
    parser.add_argument("--check-report", type=Path, default=ROOT / "reports/local-java-check.json")
    parser.add_argument("--native-report", type=Path, help="Passed actual NeoForge GameTest result for the exact local test artifact")
    parser.add_argument("--output", type=Path, help="ZIP path outside the project; source-only defaults to /private/tmp/" + SOURCE_NAME)
    args = parser.parse_args()
    if args.local_test:
        if args.source_only or args.native_report is None:
            parser.error("--local-test requires --native-report and cannot be combined with --source-only")
        from run_gametest_local import validate_artifact
        check = json.loads(args.check_report.read_text())
        artifact = validate_artifact(check)
        native = json.loads(args.native_report.read_text())
        if native.get("status") != "passed" or native.get("artifact", {}).get("sha256") != hashlib.sha256(artifact.read_bytes()).hexdigest():
            raise SystemExit("Native result must pass and refer to the same checked local test JAR")
        output = (args.output or Path("/private/tmp") / LOCAL_TEST_NAME).resolve()
        if output.is_relative_to(ROOT.resolve()) or output.name != LOCAL_TEST_NAME:
            raise SystemExit("Use the explicitly named local-test ZIP outside the project: " + LOCAL_TEST_NAME)
        files = select_release_files(ROOT, "", source_only=True)
        write_archive(ROOT, files, output)
        with zipfile.ZipFile(output, "a", compression=zipfile.ZIP_DEFLATED, compresslevel=9) as archive:
            archive.write(artifact, "dungeons-alive/local-test-mod/" + artifact.name)
            archive.writestr("dungeons-alive/LOCAL_TEST_NOTICE.txt",
                "Dungeons Alive local test build\n\n"
                "This separate JAR was compiled directly against the actual cached Minecraft/NeoForge Java 21 dependencies, "
                "passed the recorded JUnit suite and the matching native GameTest run. It is not a new Gradle release. "
                "See the included reports for precise scope and separate visual review. No existing installed JAR, "
                "game world or dist release was changed by packaging.\n"
                "Use Minecraft Java 1.21.1 / NeoForge 21.1.265 / Java 21. The optional living_world preset is needed "
                "for the four new biomes; existing worlds retain their generator.\n")
        print(f"Created separately verified LOCAL TEST {output}: {len(files) + 2} entries, {output.stat().st_size} bytes")
        return
    if args.source_only:
        output = (args.output or Path("/private/tmp") / SOURCE_NAME).resolve()
        if output.is_relative_to(ROOT.resolve()):
            raise SystemExit("Write the ZIP outside the project directory to avoid packaging the archive itself.")
        if output.name != SOURCE_NAME:
            raise SystemExit(f"Use the explicit unverified source filename: {SOURCE_NAME}")
        files = select_release_files(ROOT, "", source_only=True)
        write_archive(ROOT, files, output, source_only=True)
        print(f"Created UNVERIFIED SOURCE {output}: {len(files) + 1} entries, {output.stat().st_size} bytes")
        print("No mod JAR packaged; dist, checksums and validation reports were not changed.")
        return
    jar = ROOT / "build" / "libs" / f"dungeonsalive-{VERSION}.jar"
    if not jar.is_file():
        raise SystemExit("Build the mod first: ./gradlew build")
    for name in ("test-results.json", "server-checks.txt"):
        if not (ROOT / "reports" / name).is_file():
            raise SystemExit(f"Missing validation evidence: reports/{name}")
    dist = ROOT / "dist"
    dist.mkdir(exist_ok=True)
    shutil.copy2(jar, dist / jar.name)
    digest = hashlib.sha256(jar.read_bytes()).hexdigest()
    (ROOT / "reports" / "checksums.sha256").write_text(f"{digest}  dist/{jar.name}\n", encoding="utf-8")

    files = select_release_files(ROOT, jar.name)
    output = (args.output or ROOT.parent / f"DungeonsAlive_Codex_Starter_v{VERSION.split('-')[0]}.zip").resolve()
    if output.is_relative_to(ROOT.resolve()):
        raise SystemExit("Write the ZIP outside the project directory to avoid packaging the archive itself.")
    write_archive(ROOT, files, output)
    print(f"Created {output.name}: {len(files)} files, {output.stat().st_size} bytes")
    print(f"Mod JAR SHA256: {digest}")


if __name__ == "__main__":
    main()
