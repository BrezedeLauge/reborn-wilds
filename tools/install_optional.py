#!/usr/bin/env python3
"""Install explicitly pinned optional files into one explicit Minecraft instance.

Python standard library only. No version resolution, launcher changes or execution
of downloaded code. See docs/OPTIONAL_SETUP.md for the independently pinned files.
"""

from __future__ import annotations

import argparse
import hashlib
import hmac
import json
import os
from pathlib import Path
import re
import sys
import tempfile
from urllib.error import URLError
from urllib.parse import unquote, urlsplit
from urllib.request import HTTPRedirectHandler, Request, build_opener


MANIFEST = Path(__file__).resolve().parents[1] / "profiles" / "optional-mods.json"
CHUNK_SIZE = 128 * 1024
MAX_FILE_SIZE = 64 * 1024 * 1024


class InstallError(Exception):
    """A validation or installation failure that should be shown to the user."""


class NoRedirects(HTTPRedirectHandler):
    def redirect_request(self, req, fp, code, msg, headers, newurl):
        raise InstallError("Unerwartete Download-Weiterleitung; es wurde nichts freigegeben.")


def safe_filename(value: str) -> str:
    if not isinstance(value, str) or not re.fullmatch(
        r"[A-Za-z0-9][A-Za-z0-9._+\-]*\.(jar|zip)", value
    ):
        raise InstallError("Ungültiger Dateiname im Manifest.")
    if Path(value).name != value or "/" in value or "\\" in value:
        raise InstallError("Dateinamen dürfen keine Verzeichnisse enthalten.")
    return value


def validate_url(artifact: dict) -> None:
    url = artifact.get("url", "")
    parsed = urlsplit(url)
    if (
        parsed.scheme != "https"
        or parsed.netloc != "cdn.modrinth.com"
        or parsed.query
        or parsed.fragment
    ):
        raise InstallError("Downloads sind nur direkt von https://cdn.modrinth.com erlaubt.")
    project = artifact.get("project_id", "")
    version = artifact.get("version_id", "")
    if not re.fullmatch(r"[A-Za-z0-9]+", project) or not re.fullmatch(
        r"[A-Za-z0-9]+", version
    ):
        raise InstallError("Ungültige feste Projekt- oder Versions-ID.")
    expected = f"/data/{project}/versions/{version}/{artifact['filename']}"
    if unquote(parsed.path) != expected:
        raise InstallError("Downloadpfad passt nicht zur festgelegten Datei und Version.")


def sha512_file(path: Path) -> str:
    digest = hashlib.sha512()
    with path.open("rb") as stream:
        for chunk in iter(lambda: stream.read(CHUNK_SIZE), b""):
            digest.update(chunk)
    return digest.hexdigest()


def verify_sha512(path: Path, expected: str) -> None:
    if not hmac.compare_digest(sha512_file(path), expected):
        raise InstallError(f"SHA512 stimmt nicht überein: {path.name}")


def select_artifacts(manifest: dict, profile_name: str) -> tuple[dict, list[dict]]:
    if manifest.get("schema_version") != 1:
        raise InstallError("Unbekanntes Manifestformat.")
    if manifest.get("minecraft_version") != "1.21.1" or manifest.get("loader") != "neoforge":
        raise InstallError("Dieses Setup unterstützt ausschließlich Minecraft 1.21.1 / NeoForge.")
    profiles = manifest.get("profiles", {})
    if profile_name not in profiles:
        raise InstallError(f"Unbekanntes Profil: {profile_name}. Verfügbar: {', '.join(profiles)}")
    profile = profiles[profile_name]
    if profile.get("automatic_install") is not True:
        raise InstallError("Dieses Profil ist ausschließlich zur manuellen Installation dokumentiert.")
    keys = profile.get("artifacts", [])
    if not keys or len(set(keys)) != len(keys):
        raise InstallError("Die Profil-Dateiliste ist leer oder enthält doppelte Einträge.")
    artifacts = manifest.get("artifacts", {})
    selected = []
    for key in keys:
        if key not in artifacts:
            raise InstallError(f"Manifest enthält keine Datei für {key}.")
        artifact = dict(artifacts[key], key=key)
        filename = safe_filename(artifact.get("filename", ""))
        directory = artifact.get("directory")
        if directory not in {"mods", "resourcepacks"}:
            raise InstallError("Zielverzeichnis muss mods oder resourcepacks sein.")
        if not filename.endswith(".jar" if directory == "mods" else ".zip"):
            raise InstallError("Dateityp passt nicht zum Zielverzeichnis.")
        if "1.21.1" not in artifact.get("game_versions", []):
            raise InstallError(f"{filename} ist nicht für Minecraft 1.21.1 ausgewiesen.")
        expected_loaders = ["neoforge"] if directory == "mods" else ["minecraft"]
        if artifact.get("loaders") != expected_loaders:
            raise InstallError(f"Loader passt nicht zu diesem Profil: {filename}")
        if not re.fullmatch(r"[0-9a-f]{128}", artifact.get("sha512", "")):
            raise InstallError(f"Gültige SHA512-Prüfsumme fehlt für {filename}.")
        size = artifact.get("size_bytes")
        if type(size) is not int or not 0 < size <= MAX_FILE_SIZE:
            raise InstallError(f"Ungültige Downloadgröße: {filename}")
        validate_url(artifact)
        selected.append(artifact)

    projects = {a["project_id"]: a for a in selected}
    versions = {a["version_id"]: a for a in selected}
    if len(projects) != len(selected):
        raise InstallError("Ein Profil darf nur eine Version pro Projekt enthalten.")
    for artifact in selected:
        for needed in artifact.get("required_artifacts", []):
            if needed not in keys:
                raise InstallError(f"Nicht gepinnte Abhängigkeit {needed} für {artifact['key']}.")
        for dependency in artifact.get("api_dependencies", []):
            kind = dependency.get("dependency_type")
            project = dependency.get("project_id")
            version = dependency.get("version_id")
            match = versions.get(version) if version else projects.get(project)
            if kind == "required" and (
                match is None or (project is not None and match["project_id"] != project)
            ):
                raise InstallError(f"Erforderliche API-Abhängigkeit von {artifact['key']} fehlt.")
            if kind == "incompatible" and match is not None:
                raise InstallError(f"Unvereinbare API-Abhängigkeit bei {artifact['key']}.")
    return profile, selected


def destination_for(instance: Path, artifact: dict) -> Path:
    directory = instance / artifact["directory"]
    if directory.is_symlink():
        raise InstallError(f"Zielordner ist ein Symlink: {directory}")
    if directory.exists() and not directory.is_dir():
        raise InstallError(f"Zielordner ist keine Directory: {directory}")
    if not directory.resolve().is_relative_to(instance):
        raise InstallError("Zielpfad verlässt die ausgewählte Instanz.")
    destination = directory / artifact["filename"]
    if destination.is_symlink():
        raise InstallError(f"Zieldatei ist ein Symlink: {destination}")
    return destination


def plan_install(instance: Path, selected: list[dict]) -> list[tuple[dict, Path, bool]]:
    plan = []
    for artifact in selected:
        destination = destination_for(instance, artifact)
        exists = destination.exists()
        if exists:
            if not destination.is_file():
                raise InstallError(f"Ziel ist keine normale Datei: {destination}")
            verify_sha512(destination, artifact["sha512"])
        if artifact["directory"] == "mods" and destination.parent.exists():
            prefixes = artifact.get("conflict_prefixes", [])
            for other in destination.parent.iterdir():
                if other.name != destination.name and other.suffix == ".jar" and any(
                    other.name.startswith(prefix) for prefix in prefixes
                ):
                    raise InstallError(
                        f"Andere Version desselben Mods gefunden: {other.name}. "
                        "Bitte in einer eigenen Instanz prüfen oder die Version bewusst manuell klären."
                    )
        plan.append((artifact, destination, exists))
    return plan


def stage_download(artifact: dict, destination: Path) -> Path:
    fd, name = tempfile.mkstemp(prefix=".dungeonsalive-", suffix=".part", dir=destination.parent)
    temporary = Path(name)
    try:
        digest = hashlib.sha512()
        count = 0
        request = Request(artifact["url"], headers={"User-Agent": "DungeonsAliveOptionalInstaller/0.1"})
        opener = build_opener(NoRedirects())
        with os.fdopen(fd, "wb") as output, opener.open(request, timeout=30) as response:
            if response.status != 200:
                raise InstallError("Der Downloadserver lieferte keine vollständige Datei.")
            while True:
                chunk = response.read(CHUNK_SIZE)
                if not chunk:
                    break
                count += len(chunk)
                if count > artifact["size_bytes"]:
                    raise InstallError(f"Download ist größer als erwartet: {destination.name}")
                output.write(chunk)
                digest.update(chunk)
            output.flush()
            os.fsync(output.fileno())
        if count != artifact["size_bytes"]:
            raise InstallError(f"Downloadgröße stimmt nicht überein: {destination.name}")
        if not hmac.compare_digest(digest.hexdigest(), artifact["sha512"]):
            raise InstallError(f"SHA512 des Downloads stimmt nicht überein: {destination.name}")
        return temporary
    except BaseException:
        temporary.unlink(missing_ok=True)
        raise


def publish_without_overwrite(temporary: Path, destination: Path, sha512: str) -> bool:
    """Atomically publish verified bytes without POSIX rename's overwrite race.

    A same-filesystem hard link is atomic and fails if the target already exists.
    The temporary name is removed by the caller. No non-atomic copy fallback.
    """
    try:
        os.link(temporary, destination)
        return True
    except FileExistsError:
        if destination.is_symlink() or not destination.is_file():
            raise InstallError(f"Ziel wurde während des Downloads verändert: {destination.name}")
        verify_sha512(destination, sha512)
        return False
    except OSError as error:
        raise InstallError(
            f"Datei konnte nicht atomisch ohne Überschreiben angelegt werden: {destination.name}. "
            "Das Ziel muss atomische Hardlinks unterstützen (z. B. APFS, NTFS oder ext4)."
        ) from error


def install(plan: list[tuple[dict, Path, bool]], instance: Path) -> None:
    staged = []
    try:
        for artifact, destination, exists in plan:
            if exists:
                continue
            destination.parent.mkdir(exist_ok=True)
            destination_for(instance, artifact)
            print(f"Lade und prüfe: {destination.name}")
            temporary = stage_download(artifact, destination)
            staged.append((artifact, destination, temporary))
        for artifact, destination, temporary in staged:
            destination_for(instance, artifact)
            changed = publish_without_overwrite(temporary, destination, artifact["sha512"])
            print(f"{'Installiert' if changed else 'Bereits identisch vorhanden'}: {destination.name}")
    finally:
        for _, _, temporary in staged:
            temporary.unlink(missing_ok=True)


def main(argv=None) -> int:
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--profile", required=True, help="animations oder enchanter")
    parser.add_argument("--instance", required=True, type=Path, help="Bestehender Spielordner einer expliziten Instanz")
    parser.add_argument("--dry-run", action="store_true", help="Nur Manifest und Ziel prüfen; kein Netzwerk und keine Änderungen")
    args = parser.parse_args(argv)
    try:
        manifest = json.loads(MANIFEST.read_text(encoding="utf-8"))
        profile, selected = select_artifacts(manifest, args.profile)
        instance = args.instance.expanduser().resolve(strict=True)
        if not instance.is_dir():
            raise InstallError("--instance muss auf einen bestehenden Spielordner zeigen.")
        plan = plan_install(instance, selected)
        print(f"Profil: {args.profile}")
        print(f"Instanz: {instance}")
        print("Vorgesehene Basis: Minecraft 1.21.1 / NeoForge 21.1.265 / Java 21")
        print("Quellen und Prüfsummen geprüft. Grafischer Spieltest bleibt offen; detaillierter Stand: docs/VALIDATION.md.")
        if profile.get("validation", {}).get("headless_integration") == "passed":
            print("Enchanter: Laden auf dem Headless-Server, echte Registry und Questintegration geprüft.")
        for artifact, destination, exists in plan:
            action = "IDENTISCH" if exists else "NEU"
            print(f"  {action}: {artifact['directory']}/{destination.name} ({artifact['size_bytes']} Bytes)")
        if args.dry_run:
            print("Dry-run abgeschlossen: keine Downloads oder Änderungen.")
            return 0
        install(plan, instance)
        print("Dateien installiert. Nächste Schritte:")
        for step in profile.get("manual_steps", []):
            print(f"  - {step}")
        return 0
    except (InstallError, OSError, ValueError, KeyError, TypeError, URLError) as error:
        print(f"Setup abgebrochen: {error}", file=sys.stderr)
        return 1


if __name__ == "__main__":
    raise SystemExit(main())
