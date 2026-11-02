#!/usr/bin/env python3
"""Audit or stage a NEW public source tree; never commit, upload, or alter sources.

Default: audit only. --stage additionally copies a successful, revalidated snapshot
to a new directory under build/publication/ or /private/tmp/. Reports and SHA-256
manifests are written beside the snapshot, never inside it. Findings expose only
relative file paths and categories, never matched text or credential values.
"""
from __future__ import annotations

import argparse
from collections import Counter
import ctypes
from datetime import datetime, timezone
import hashlib
import json
import os
from pathlib import Path, PurePosixPath
import re
import shutil
import stat
import sys
import tempfile
from urllib.parse import unquote, urlsplit

ROOT = Path(__file__).resolve().parents[1]
DIRECTORIES = frozenset({"src", "docs", "design", "tools", "third_party", "gradle", "profiles", "schemas", "examples"})
ROOT_FILES = frozenset({"README.md", "AGENTS.md", "CODEX_START.md", "MINECRAFT_2_IDEEN_FUER_CODEX.md", "LICENSE",
                       "THIRD_PARTY_NOTICES.md", ".gitignore", "build.gradle", "settings.gradle", "gradle.properties", "gradlew", "gradlew.bat"})
PROJECT_SKILLS = frozenset({"dungeons-neoforge-runtime", "dungeons-models-animation", "dungeons-native-qa"})
WRAPPER = "gradle/wrapper/gradle-wrapper.jar"
OMIT_PARTS = frozenset({"reports", "build", "dist", "logs", "run", "local-mods", "target", "out", "node_modules",
    "__pycache__", ".gradle", ".m2", ".cache", "cache", "caches", ".local-build", ".git", ".agents", ".codex", ".aws",
    ".azure", ".gcloud", ".minecraft", ".idea", ".vscode", ".venv", "venv", ".tox", ".pytest_cache", ".mypy_cache", ".ruff_cache",
    "playerdata"})
RUNTIME_NAMES = frozenset({"accounts.json", "launcher_accounts.json", "usercache.json", "usernamecache.json", "servers.dat",
    "options.txt", "instance.cfg", "prismlauncher.cfg", "level.dat", "level.dat_old", "session.lock", ".ds_store", "thumbs.db"})
PRIVATE_SUFFIXES = frozenset({".pem", ".key", ".ppk", ".p8", ".p12", ".pfx", ".jks", ".jceks", ".keystore"})
ARCHIVE_SUFFIXES = frozenset({".jar", ".zip", ".tar", ".gz", ".tgz", ".7z", ".rar", ".class", ".pyc", ".pyo", ".mca", ".mcc", ".log"})
TEXT_SUFFIXES = frozenset({".java", ".json", ".mcmeta", ".toml", ".md", ".txt", ".py", ".sh", ".gradle", ".properties",
    ".yaml", ".yml", ".xml", ".cfg", ".ini", ".bat", ".cmd", ".csv", ".tsv", ".html", ".css", ".js", ".svg", ".jem", ".jpm", ".bbmodel"})
MAX_FILE_BYTES = 32 * 1024 * 1024
MAX_FILES = 20_000
MAX_VISITED_PATHS = 50_000
MAX_TOTAL_BYTES = 256 * 1024 * 1024

# Narrow recognizable credential formats plus concrete credential assignments.
# No matching excerpt is ever persisted or printed.
SECRET_PATTERNS = (
    ("private_key", re.compile(rb"-----BEGIN (?:RSA |EC |DSA |OPENSSH |PGP )?PRIVATE KEY(?: BLOCK)?-----")),
    ("github_token", re.compile(rb"\b(?:gh[pousr]_[A-Za-z0-9]{30,}|github_pat_[A-Za-z0-9_]{30,})\b")),
    ("openai_token", re.compile(rb"\bsk-(?:proj-|svcacct-)?[A-Za-z0-9_-]{32,}\b")),
    ("aws_access_key", re.compile(rb"\b(?:AKIA|ASIA)[A-Z0-9]{16}\b")),
    ("slack_token", re.compile(rb"\bxox[baprs]-[A-Za-z0-9-]{15,}\b")),
    ("google_api_key", re.compile(rb"\bAIza[A-Za-z0-9_-]{35}\b")),
    ("npm_token", re.compile(rb"\bnpm_[A-Za-z0-9]{30,}\b")),
    ("jwt_token", re.compile(rb"\beyJ[A-Za-z0-9_-]{10,}\.[A-Za-z0-9_-]{10,}\.[A-Za-z0-9_-]{15,}\b")),
    ("url_credentials", re.compile(rb"https?://[^\s/:@]+:[^\s/@]{4,}@", re.I)),
    ("credential_query", re.compile(rb"[?&](?:access_token|refresh_token|api_key|token|sig|X-Amz-Signature)=([A-Za-z0-9_%+./=-]{16,})", re.I)),
)
ASSIGNMENT = re.compile(rb'''(?ix)(?:["']?)(?:api[_-]?key|access[_-]?token|refresh[_-]?token|client[_-]?secret|aws[_-]?secret[_-]?access[_-]?key|password)(?:["']?)\s*[:=]\s*["']([^\r\n"']{12,})["']''')
HOME_PATHS = (
    re.compile(rb"/(?:Users|home)/([^/\s'\"<>{}$]+)"),
    re.compile(rb"[A-Za-z]:[\\/]Users[\\/]([^\\/\s'\"<>{}$]+)", re.I),
)
LINK = re.compile(r"!?\[[^\]\n]*\]\(\s*<?([^\s)>]+)>?(?:\s+[\"'][^\n)]*[\"'])?\s*\)")


def digest(data: bytes) -> str:
    return hashlib.sha256(data).hexdigest()


def finding(path: str, kind: str) -> dict:
    return {"path": path, "kind": kind}


def private_name(name: str) -> bool:
    low = name.lower()
    return (low == ".env" or low.startswith(".env.") or low == "credentials" or low.startswith("credentials.")
            or "credentials" in low and low.endswith(".json") or "secret" in low and low.endswith(".json")
            or low in {".netrc", ".npmrc", ".pypirc", ".git-credentials", "auth.json", "token"}
            or low.startswith(("secrets.", "token.", "tokens.", "access_token", "refresh_token"))
            or low.endswith(".json") and (low.startswith(("oauth", "service-account", "service_account")) or "private" in low and "key" in low)
            or low.startswith(("id_rsa", "id_ed25519")) or Path(low).suffix in PRIVATE_SUFFIXES)


def exclusion(relative: PurePosixPath) -> str | None:
    parts = [part.lower() for part in relative.parts]
    if any(part in OMIT_PARTS or part.startswith("run-") for part in parts):
        return "runtime_or_cache"
    if private_name(relative.name):
        return "credential_filename"
    if relative.name.lower() in RUNTIME_NAMES:
        return "runtime_or_world_file"
    if relative.suffix.lower() in ARCHIVE_SUFFIXES and relative.as_posix() != WRAPPER:
        return "binary_or_archive"
    return None


def assert_no_symlink(path: Path) -> None:
    for part in (path, *path.parents):
        if part.is_symlink():
            raise ValueError("symlink_path")


def read_regular(path: Path) -> bytes:
    assert_no_symlink(path)
    flags = os.O_RDONLY | getattr(os, "O_NOFOLLOW", 0)
    fd = os.open(path, flags)
    try:
        before = os.fstat(fd)
        if not stat.S_ISREG(before.st_mode) or before.st_size > MAX_FILE_BYTES:
            raise ValueError("nonregular_or_oversized_file")
        with os.fdopen(fd, "rb", closefd=False) as stream:
            data = stream.read(MAX_FILE_BYTES + 1)
        after = os.fstat(fd)
        if len(data) > MAX_FILE_BYTES or (before.st_ino, before.st_size, before.st_mtime_ns) != (after.st_ino, after.st_size, after.st_mtime_ns):
            raise ValueError("source_changed_during_read")
        return data
    finally:
        os.close(fd)


def is_placeholder(value: bytes) -> bool:
    lower = value.lower()
    return (lower.startswith((b"${", b"{{", b"<", b"your_", b"replace_", b"example_"))
            or lower in {b"change_me_before_use", b"not-a-real-secret", b"placeholder", b"test-placeholder"})


def content_findings(relative: str, data: bytes, textual: bool) -> list[dict]:
    results = []
    for kind, pattern in SECRET_PATTERNS:
        if pattern.search(data):
            results.append(finding(relative, kind))
    if textual:
        if any(not is_placeholder(match.group(1)) for match in ASSIGNMENT.finditer(data)):
            results.append(finding(relative, "credential_assignment"))
        if any(any(match.group(1).strip(b".") for match in pattern.finditer(data)) for pattern in HOME_PATHS):
            results.append(finding(relative, "private_home_path"))
    return results


def file_kind(relative: PurePosixPath, data: bytes) -> str | None:
    suffix = relative.suffix.lower()
    if relative.as_posix() == WRAPPER:
        return "gradle_bootstrap" if data.startswith(b"PK\x03\x04") else None
    if suffix == ".png":
        return "image" if data.startswith(b"\x89PNG\r\n\x1a\n") else None
    if suffix == ".ogg":
        return "audio" if data.startswith(b"OggS") else None
    if suffix == ".nbt":
        # Authored data-pack structures are source assets, unlike arbitrary saved-game NBT.
        allowed = relative.parts[0] in {"src", "examples"} and "data" in relative.parts and "structure" in relative.parts
        return "authored_structure" if allowed and data.startswith((b"\x1f\x8b", b"\x0a")) else None
    if suffix in TEXT_SUFFIXES or relative.name in {"LICENSE", "NOTICE", "COPYING", "gradlew", ".gitignore"}:
        try:
            data.decode("utf-8-sig")
            return "text" if b"\0" not in data else None
        except UnicodeDecodeError:
            return None
    return None


def candidates(root: Path) -> tuple[list[Path], list[dict], list[dict]]:
    selected, excluded, errors = [], [], []
    visited = 0
    def walk(path: Path) -> None:
        nonlocal visited
        visited += 1
        relative = PurePosixPath(path.relative_to(root).as_posix())
        if visited > MAX_VISITED_PATHS or len(relative.parts) > 64:
            raise ValueError("source_path_budget_exceeded")
        reason = exclusion(relative)
        if reason:
            excluded.append(finding(relative.as_posix(), reason)); return
        if path.is_symlink():
            errors.append(finding(relative.as_posix(), "symlink")); return
        if path.is_dir():
            if any((path / marker).exists() or (path / marker).is_symlink() for marker in ("level.dat", "level.dat_old", "session.lock")):
                excluded.append(finding(relative.as_posix(), "saved_world_directory")); return
            for child in sorted(path.iterdir()):
                walk(child)
        elif path.is_file():
            selected.append(path)
            if len(selected) > MAX_FILES:
                raise ValueError("source_file_budget_exceeded")
        elif path.exists():
            errors.append(finding(relative.as_posix(), "nonregular_file"))
    for name in sorted(ROOT_FILES):
        path = root / name
        if path.exists() or path.is_symlink(): walk(path)
    for name in sorted(DIRECTORIES):
        path = root / name
        if path.exists() or path.is_symlink(): walk(path)
    skill_root = root / "skills"
    if skill_root.is_symlink(): errors.append(finding("skills", "symlink"))
    else:
        for name in sorted(PROJECT_SKILLS):
            path = skill_root / name
            if path.exists() or path.is_symlink(): walk(path)
    return sorted(selected), excluded, errors


def link_findings(contents: dict[str, bytes]) -> list[dict]:
    results = set()
    names = set(contents)
    for name, data in contents.items():
        if not name.endswith(".md"): continue
        for match in LINK.finditer(data.decode("utf-8-sig")):
            target = unquote(match.group(1))
            split = urlsplit(target)
            if split.scheme or split.netloc or not split.path or split.path.startswith("#"): continue
            joined = os.path.normpath(str(PurePosixPath(name).parent / split.path)).replace("\\", "/")
            if joined.startswith("../") or joined.startswith("/"):
                kind = "outside_public_tree"
            elif joined == "reports" or joined.startswith("reports/"):
                kind = "excluded_private_report"
            elif joined in names or any(candidate.startswith(joined.rstrip("/") + "/") for candidate in names):
                continue
            else: kind = "missing_public_target"
            results.add((name, kind, joined))
    # Even a malicious Markdown target could contain a credential or home path.
    # Keep it only while counting; public diagnostics never echo target text.
    counts = Counter((path, kind) for path, kind, _ in results)
    return [{"path": path, "kind": kind, "links": count} for (path, kind), count in sorted(counts.items())]


def audit(root: Path) -> tuple[dict, dict, dict[str, bytes]]:
    assert_no_symlink(root)
    files, excluded, errors = candidates(root)
    entries, contents = [], {}
    total = 0
    for path in files:
        relative = path.relative_to(root).as_posix()
        try:
            data = read_regular(path)
            kind = file_kind(PurePosixPath(relative), data)
            if kind is None:
                errors.append(finding(relative, "unexpected_file_type")); continue
            total += len(data)
            if total > MAX_TOTAL_BYTES: raise ValueError("source_byte_budget_exceeded")
            errors.extend(content_findings(relative, data, kind == "text"))
            contents[relative] = data
            mode = "0755" if relative == "gradlew" or path.stat().st_mode & 0o111 else "0644"
            entries.append({"path": relative, "bytes": len(data), "sha256": digest(data), "mode": mode, "kind": kind})
        except (OSError, ValueError) as error:
            errors.append(finding(relative, str(error) if isinstance(error, ValueError) else "file_read_failed"))
    manifest = {"schema": 1, "scope": "Public source snapshot; no private reports, runtime data or build artifacts", "files": entries}
    tree_hash = digest(json.dumps(manifest, sort_keys=True, separators=(",", ":")).encode())
    links = link_findings(contents)
    report = {"schema": 1, "status": "blocked" if errors else "audited", "source_tree_sha256": tree_hash,
              "counts": {"files": len(entries), "bytes": total, "excluded_paths": len(excluded), "blocking_findings": len(errors), "link_findings": sum(entry["links"] for entry in links)},
              "by_directory": dict(sorted(Counter(PurePosixPath(entry["path"]).parts[0] for entry in entries).items())),
              "blocking_findings": errors, "excluded": excluded, "public_link_findings": links,
              "privacy_scope": "All reports excluded. Matches are reported as relative path and category only. Pattern scanning is not a guarantee that every possible secret has been detected.",
              "license_scope": "Source notices, selected upstream snapshots and provenance manifests are preserved; mixed asset licenses are not changed."}
    return report, manifest, contents


def target_path(root: Path, value: Path) -> Path:
    path = Path(os.path.abspath(value))
    allowed = (root / "build/publication", Path("/private/tmp"))
    if not any(path != base and path.is_relative_to(base) for base in allowed):
        raise ValueError("target_must_be_beneath_build_publication_or_private_tmp")
    assert_no_symlink(path)
    if path.exists(): raise ValueError("target_already_exists")
    return path


def publish_new_directory(source: Path, target: Path) -> None:
    """Atomic exclusive rename: even an existing empty target must not be replaced."""
    libc = ctypes.CDLL(None, use_errno=True)
    if sys.platform == "darwin":
        operation = libc.renamex_np
        operation.argtypes = (ctypes.c_char_p, ctypes.c_char_p, ctypes.c_uint)
        result = operation(os.fsencode(source), os.fsencode(target), 4)
    elif sys.platform.startswith("linux") and hasattr(libc, "renameat2"):
        operation = libc.renameat2
        operation.argtypes = (ctypes.c_int, ctypes.c_char_p, ctypes.c_int, ctypes.c_char_p, ctypes.c_uint)
        result = operation(-100, os.fsencode(source), -100, os.fsencode(target), 1)
    else:
        raise ValueError("exclusive_publication_unavailable")
    if result: raise OSError(ctypes.get_errno(), "exclusive_publication_failed")


def stage(root: Path, target: Path, report: dict, manifest: dict, contents: dict[str, bytes]) -> None:
    if report["status"] != "audited": raise ValueError("blocking_findings")
    target_path(root, target)
    target.parent.mkdir(parents=True, exist_ok=True); assert_no_symlink(target.parent)
    work = Path(tempfile.mkdtemp(prefix=".public-source-", dir=target.parent))
    try:
        for entry in manifest["files"]:
            relative = entry["path"]; copied = work / relative
            copied.parent.mkdir(parents=True, exist_ok=True)
            with copied.open("xb") as stream: stream.write(contents[relative])
            copied.chmod(int(entry["mode"], 8))
        # Catch concurrent README/branding/source edits, newly introduced files or secrets.
        current, current_manifest, _ = audit(root)
        if current["status"] != "audited" or current_manifest != manifest:
            raise ValueError("source_changed_after_audit")
        for entry in manifest["files"]:
            if digest(read_regular(work / entry["path"])) != entry["sha256"]:
                raise ValueError("copied_file_hash_mismatch")
        target_path(root, target)
        publish_new_directory(work, target)
    finally:
        if work.exists(): shutil.rmtree(work)


def write_json_exclusive(path: Path, value: dict) -> None:
    assert_no_symlink(path)
    with path.open("x", encoding="utf-8") as stream:
        json.dump(value, stream, ensure_ascii=False, indent=2); stream.write("\n")


def self_test() -> dict:
    """Temporary real-file tests; no project source, private report or runtime world is touched."""
    import unittest
    class Checks(unittest.TestCase):
        def setUp(self):
            self.temporary = tempfile.TemporaryDirectory(prefix="public-selection-test-", dir="/private/tmp" if Path("/private/tmp").is_dir() else None)
            self.root = Path(self.temporary.name); (self.root / "src").mkdir(); (self.root / "tools").mkdir()
            (self.root / "README.md").write_text("Public example\n")
        def tearDown(self): self.temporary.cleanup()
        def write(self, name, data):
            path = self.root / name; path.parent.mkdir(parents=True, exist_ok=True); path.write_bytes(data); return path
        def test_allowlist_preserves_licenses_and_structures_but_omits_local_data(self):
            self.write("third_party/example/LICENSE.txt", b"Attribution retained\n")
            self.write(WRAPPER, b"PK\x03\x04official-test-bootstrap")
            self.write("src/main/resources/data/example/structure/test.nbt", b"\x0a\x00\x00\x00")
            self.write("examples/data/example/structure/test.nbt", b"\x0a\x00\x00\x00")
            self.write("src/main/java/example/world/World.java", b"class World {}\n")
            for name in ("reports/private.json", "build/private.json", "tools/.env", "profiles/accounts.json", "profiles/private.key", "tools/cache/a.json", "tools/.netrc", "profiles/token.txt", "profiles/private_key.json"):
                self.write(name, b"private content never read")
            self.write("examples/save/level.dat", b"world"); self.write("examples/save/data/a.json", b"{}")
            report, manifest, _ = audit(self.root); paths = {entry["path"] for entry in manifest["files"]}
            self.assertEqual("audited", report["status"])
            self.assertIn(WRAPPER, paths); self.assertIn("src/main/java/example/world/World.java", paths)
            self.assertIn("third_party/example/LICENSE.txt", paths); self.assertEqual(2, sum(path.endswith(".nbt") for path in paths))
            self.assertFalse(any("private" in path or "/save/" in path or "accounts" in path for path in paths))
        def test_secret_and_private_path_findings_never_echo_values(self):
            secret = b"gh" + b"p_" + b"A" * 36
            self.write("tools/leak.py", b'token = "' + secret + b'"\n')
            self.write("docs/private.md", b"/" + b"Users/fictional-person/private/file\n")
            self.write("docs/link.md", b"[hidden](missing/" + secret + b")\n")
            report, _, _ = audit(self.root)
            self.assertEqual("blocked", report["status"])
            output = json.dumps(report).encode()
            self.assertNotIn(secret, output); self.assertNotIn(b"fictional-person", output)
            self.assertEqual({"github_token", "private_home_path"}, {entry["kind"] for entry in report["blocking_findings"]})
        def test_symlink_is_not_followed(self):
            private = self.write("outside.txt", b"not selected")
            (self.root / "tools/linked.txt").symlink_to(private)
            report, _, _ = audit(self.root)
            self.assertEqual("blocked", report["status"])
            self.assertIn(finding("tools/linked.txt", "symlink"), report["blocking_findings"])
        def test_source_mutation_and_existing_destination_are_rejected(self):
            target = self.root / "build/publication/source"
            report, manifest, contents = audit(self.root)
            (self.root / "README.md").write_text("Changed after audit\n")
            with self.assertRaisesRegex(ValueError, "source_changed"):
                stage(self.root, target, report, manifest, contents)
            self.assertFalse(target.exists())
            report, manifest, contents = audit(self.root)
            target.mkdir()
            with self.assertRaisesRegex(ValueError, "target_already_exists"):
                stage(self.root, target, report, manifest, contents)
        def test_actual_publication_is_exclusive_and_hash_identical(self):
            target = self.root / "build/publication/source"
            report, manifest, contents = audit(self.root)
            stage(self.root, target, report, manifest, contents)
            self.assertEqual(contents["README.md"], (target / "README.md").read_bytes())
            self.assertFalse((target / "reports").exists())
            self.assertEqual({entry["path"] for entry in manifest["files"]}, {path.relative_to(target).as_posix() for path in target.rglob("*") if path.is_file()})
        def test_excluded_report_links_are_distinguished_from_missing_sources(self):
            self.write("docs/guide.md", b"[private](../reports/evidence.json) [missing](missing.md) [root](../README.md)\n")
            report, _, _ = audit(self.root)
            self.assertEqual({"excluded_private_report", "missing_public_target"}, {entry["kind"] for entry in report["public_link_findings"]})
    suite = unittest.defaultTestLoader.loadTestsFromTestCase(Checks)
    # Do not print assertion payloads or traceback locals: the summary has only counts.
    result = unittest.TestResult(); suite.run(result)
    summary = {"status": "passed" if result.wasSuccessful() else "failed", "tests": result.testsRun, "failures": len(result.failures), "errors": len(result.errors)}
    if not result.wasSuccessful():
        summary["failed_tests"] = [case._testMethodName for case, _ in result.failures + result.errors]
    return summary


def main() -> int:
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--stage", action="store_true", help="Copy and exclusively publish the audited NEW source tree")
    parser.add_argument("--output", type=Path, help="NEW target beneath build/publication/ or /private/tmp/ (default is timestamped)")
    parser.add_argument("--self-test", action="store_true", help="Run isolated temporary-file safety/regression tests only")
    args = parser.parse_args()
    if args.self_test:
        if args.stage or args.output: parser.error("--self-test cannot be combined with staging options")
        result = self_test(); print(json.dumps(result, indent=2)); return 0 if result["status"] == "passed" else 1
    stamp = datetime.now(timezone.utc).strftime("%Y%m%dT%H%M%S%fZ")
    try:
        target = target_path(ROOT, args.output or ROOT / "build/publication" / ("source-" + stamp))
        report, manifest, contents = audit(ROOT)
        if args.stage and report["status"] == "audited":
            stage(ROOT, target, report, manifest, contents); report["status"] = "staged"
        report["checked_at_utc"] = datetime.now(timezone.utc).isoformat()
        report["snapshot_created"] = report["status"] == "staged"
        target.parent.mkdir(parents=True, exist_ok=True); assert_no_symlink(target.parent)
        report_path = target.parent / (target.name + ".audit-" + stamp + ".json")
        manifest_path = target.parent / (target.name + ".manifest-" + stamp + ".json")
        write_json_exclusive(report_path, report); write_json_exclusive(manifest_path, manifest)
        print(json.dumps({"status": report["status"], "snapshot": str(target) if report["snapshot_created"] else None,
                          "audit": str(report_path), "manifest": str(manifest_path), "counts": report["counts"],
                          "source_tree_sha256": report["source_tree_sha256"], "blocking_findings": report["blocking_findings"]}, ensure_ascii=False, indent=2))
        return 1 if report["status"] == "blocked" else 0
    except (OSError, ValueError) as error:
        # OSError may embed credentials in an arbitrary pathname; never emit its text.
        kind = str(error) if isinstance(error, ValueError) and re.fullmatch(r"[a-z_]+", str(error)) else "filesystem_or_validation_failure"
        print(json.dumps({"status": "refused", "kind": kind})); return 1


if __name__ == "__main__":
    raise SystemExit(main())
