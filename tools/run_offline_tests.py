#!/usr/bin/env python3
"""Run selected Minecraft-independent JUnit tests using cached dependencies only.

This deliberately does not replace ./gradlew build, runGameTestServer or native
client checks. It compiles with javac --release 21 and runs on the selected JDK
(21 or newer), whose exact version is recorded. No downloads or Minecraft stubs are used.

The explicit scope is QuestJournalTest, QuestDocumentParserTest, MobProfilesTest,
VillageActivityTest, MerchantRoutineTest, WildlifeGroupTest, WildlifeGroupPolicyTest,
FlockFlightPatternTest, WoolyCowBreedingStateTest, PiggyBankTreasureStateTest, ArmorSetKindTest,
WeaponFamilyTest, GearEnchantmentRulesTest and MerchantServicePolicyTest.
RigExpression and ActionClipTiming are compiled only:
their existing test classes also require Minecraft and are not selected here.
Gson defaults to cached 2.14.0; this is not a claim to resolve NeoForge's runtime
dependency graph. All dependency versions, hashes and actual results are saved.

Example: python3 tools/run_offline_tests.py --java-home /path/to/jdk
"""
from __future__ import annotations

import argparse
from datetime import datetime, timezone
import hashlib
import json
import os
from pathlib import Path
import shlex
import shutil
import subprocess
import tempfile

ROOT = Path(__file__).resolve().parents[1]
PACKAGE = "de/laugenbreze/dungeonsalive/"
TEST_CLASSES = (
    "quest.core.QuestJournalTest",
    "quest.data.QuestDocumentParserTest",
    "entity.MobProfilesTest",
    "village.VillageActivityTest",
    "entity.peaceful.MerchantRoutineTest",
    "entity.peaceful.WildlifeGroupTest",
    "entity.peaceful.WildlifeGroupPolicyTest",
    "entity.peaceful.FlockFlightPatternTest",
    "entity.peaceful.WoolyCowBreedingStateTest",
    "entity.peaceful.PiggyBankTreasureStateTest",
    "item.gear.ArmorSetKindTest",
    "item.gear.WeaponFamilyTest",
    "item.gear.enchantment.GearEnchantmentRulesTest",
    "merchant.MerchantServicePolicyTest",
)
MAIN_SOURCES = (
    "quest/core/ObjectiveType.java", "quest/core/QuestCatalog.java",
    "quest/core/QuestDefinition.java", "quest/core/QuestJournal.java",
    "quest/core/QuestObjective.java", "quest/core/QuestSnapshot.java",
    "quest/core/QuestStatus.java", "quest/data/QuestDocument.java",
    "quest/data/QuestDocumentParser.java", "entity/AttackKind.java",
    "entity/MobProfile.java", "entity/MobProfiles.java",
    "entity/peaceful/MerchantRoutine.java",
    "entity/peaceful/WildlifeGroup.java", "entity/peaceful/WildlifeGroupPolicy.java",
    "entity/peaceful/FlockFlightPattern.java",
    "entity/peaceful/WoolyCowBreedingState.java",
    "entity/peaceful/PiggyBankTreasureState.java", "entity/peaceful/PiggyBankClaims.java",
    "item/gear/ArmorSetKind.java",
    "item/gear/WeaponFamily.java", "item/gear/WeaponKind.java",
    "item/gear/enchantment/GearEnchantmentKind.java", "item/gear/enchantment/GearEnchantmentRules.java",
    "merchant/MerchantServicePolicy.java", "merchant/ServiceKind.java", "merchant/ServiceStatus.java",
    "village/ProfessionGesture.java", "village/VillageActivity.java",
    "client/entity/reference/RigExpression.java",
    "client/entity/reference/ActionClipTiming.java",
)
DEPENDENCIES = (
    ("org.junit.jupiter", "junit-jupiter-api", "5.11.4"),
    ("org.junit.jupiter", "junit-jupiter-engine", "5.11.4"),
    ("org.junit.platform", "junit-platform-commons", "1.11.4"),
    ("org.junit.platform", "junit-platform-engine", "1.11.4"),
    ("org.junit.platform", "junit-platform-launcher", "1.11.4"),
    ("org.opentest4j", "opentest4j", "1.3.0"),
    ("org.apiguardian", "apiguardian-api", "1.1.2"),
)
LAUNCHER_SOURCE = r'''
import com.google.gson.GsonBuilder;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import org.junit.platform.engine.TestExecutionResult;
import org.junit.platform.engine.discovery.DiscoverySelectors;
import org.junit.platform.engine.support.descriptor.MethodSource;
import org.junit.platform.launcher.TestExecutionListener;
import org.junit.platform.launcher.TestIdentifier;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;

public final class OfflineTestLauncher {
    public static void main(String[] args) throws Exception {
        var request = LauncherDiscoveryRequestBuilder.request();
        for (int i = 1; i < args.length; i++)
            request.selectors(DiscoverySelectors.selectClass(args[i]));
        var listener = new SummaryGeneratingListener();
        var cases = new ArrayList<Map<String, Object>>();
        var results = new TestExecutionListener() {
            private Map<String, Object> entry(TestIdentifier test) {
                var entry = new LinkedHashMap<String, Object>();
                entry.put("unique_id", test.getUniqueId());
                entry.put("display_name", test.getDisplayName());
                test.getSource().filter(MethodSource.class::isInstance)
                    .map(MethodSource.class::cast).ifPresent(source -> {
                        entry.put("class", source.getClassName());
                        entry.put("method", source.getMethodName());
                    });
                return entry;
            }
            @Override public void executionFinished(TestIdentifier test, TestExecutionResult result) {
                if (!test.isTest()) return;
                var entry = entry(test);
                entry.put("status", result.getStatus().name());
                result.getThrowable().ifPresent(error -> entry.put("error", error.toString()));
                cases.add(entry);
            }
            @Override public void executionSkipped(TestIdentifier test, String reason) {
                if (!test.isTest()) return;
                var entry = entry(test);
                entry.put("status", "SKIPPED");
                entry.put("reason", reason);
                cases.add(entry);
            }
        };
        LauncherFactory.create().execute(request.build(), listener, results);
        var summary = listener.getSummary();
        var writer = new PrintWriter(System.out, true);
        summary.printTo(writer);
        summary.printFailuresTo(writer);
        var counts = new LinkedHashMap<String, Object>();
        counts.put("found", summary.getTestsFoundCount());
        counts.put("started", summary.getTestsStartedCount());
        counts.put("succeeded", summary.getTestsSucceededCount());
        counts.put("failed", summary.getTestsFailedCount());
        counts.put("aborted", summary.getTestsAbortedCount());
        counts.put("skipped", summary.getTestsSkippedCount());
        counts.put("containers_failed", summary.getContainersFailedCount());
        counts.put("total_failures", summary.getTotalFailureCount());
        var report = new LinkedHashMap<String, Object>();
        report.put("counts", counts);
        report.put("cases", cases);
        Files.writeString(Path.of(args[0]), new GsonBuilder().setPrettyPrinting().create().toJson(report));
        if (summary.getTestsFoundCount() == 0 || summary.getTotalFailureCount() > 0
                || summary.getTestsAbortedCount() > 0) System.exit(1);
    }
}
'''


def sha256(path: Path) -> str:
    return hashlib.sha256(path.read_bytes()).hexdigest()


def cached_jar(cache: Path, group: str, artifact: str, version: str) -> Path:
    folder = cache / group / artifact / version
    matches = sorted(folder.glob(f"*/{artifact}-{version}.jar"))
    if not matches:
        raise RuntimeError(f"Missing cached dependency {group}:{artifact}:{version} in {folder}; no download attempted.")
    if len({sha256(path) for path in matches}) != 1:
        raise RuntimeError(f"Conflicting cached JAR contents for {group}:{artifact}:{version}.")
    return matches[0]


def java_tool(name: str, java_home: Path | None) -> Path:
    suffix = ".exe" if os.name == "nt" else ""
    candidate = java_home / "bin" / (name + suffix) if java_home else shutil.which(name)
    if not candidate or not Path(candidate).is_file():
        raise RuntimeError(f"Missing {name}; supply --java-home pointing to a JDK 21 or newer.")
    return Path(candidate).resolve()


def main() -> int:
    parser = argparse.ArgumentParser(description=__doc__, formatter_class=argparse.RawDescriptionHelpFormatter)
    parser.add_argument("--java-home", type=Path, default=os.environ.get("JAVA_HOME"))
    parser.add_argument("--dependency-cache", type=Path,
                        default=Path.home() / ".gradle/caches/modules-2/files-2.1",
                        help="Read-only Gradle files-2.1 cache containing the exact dependency versions.")
    parser.add_argument("--gson-version", default="2.14.0", help="Exact cached Gson version; recorded in the report.")
    parser.add_argument("--report", type=Path, default=ROOT / "reports/offline-tests.json")
    args = parser.parse_args()
    report_path = args.report.resolve()
    log_path = report_path.with_suffix(".log")
    report = {
        "schema_version": 1,
        "started_at_utc": datetime.now(timezone.utc).isoformat(),
        "status": "not_run",
        "scope": "Selected Minecraft-independent JUnit test classes; no Gradle, Minecraft or NeoForge runtime.",
        "test_classes": [PACKAGE.replace("/", ".") + name for name in TEST_CLASSES],
        "compile_only": ["RigExpression", "ActionClipTiming"],
        "javac_release": 21,
        "limitations": [
            "Not a full build, dedicated-server GameTest or native visual check; exact Java runtime is recorded separately.",
            "Cached Gson version is explicit and does not establish the resolved NeoForge dependency graph.",
            "Minecraft-dependent tests, including rig and persistence tests, are not selected.",
        ],
        "junit_summary_log": str(log_path),
    }
    log: list[str] = []

    def run(command: list[str]) -> subprocess.CompletedProcess[str]:
        log.append("$ " + shlex.join(command) + "\n")
        result = subprocess.run(command, cwd=ROOT, text=True, stdout=subprocess.PIPE, stderr=subprocess.STDOUT)
        log.append(result.stdout)
        return result

    exit_code = 1
    try:
        java_home = args.java_home.expanduser() if args.java_home else None
        java = java_tool("java", java_home)
        javac = java_tool("javac", java_home)
        report["java_version"] = run([str(java), "-version"]).stdout.strip()
        report["javac_version"] = run([str(javac), "-version"]).stdout.strip()
        coordinates = (*DEPENDENCIES, ("com.google.code.gson", "gson", args.gson_version))
        jars = [cached_jar(args.dependency_cache.expanduser(), *coordinate) for coordinate in coordinates]
        report["dependencies"] = [
            {"coordinate": ":".join(coordinate), "path": str(jar), "sha256": sha256(jar)}
            for coordinate, jar in zip(coordinates, jars)
        ]
        sources = [ROOT / "src/main/java" / PACKAGE / path for path in MAIN_SOURCES]
        sources += [ROOT / "src/test/java" / PACKAGE / (name.replace(".", "/") + ".java") for name in TEST_CLASSES]
        report["sources"] = [{"path": path.relative_to(ROOT).as_posix(), "sha256": sha256(path)} for path in sources]
        with tempfile.TemporaryDirectory(prefix="dungeonsalive-offline-") as temporary:
            work = Path(temporary)
            classes = work / "classes"
            classes.mkdir()
            launcher = work / "OfflineTestLauncher.java"
            launcher.write_text(LAUNCHER_SOURCE, encoding="utf-8")
            classpath = os.pathsep.join(str(jar) for jar in jars)
            compiled = run([str(javac), "--release", "21", "-encoding", "UTF-8", "-sourcepath", "",
                            "-classpath", classpath, "-d", str(classes), *map(str, sources), str(launcher)])
            if compiled.returncode:
                raise RuntimeError(f"javac --release 21 failed with exit {compiled.returncode}; see log.")
            results_file = work / "junit-results.json"
            executed = run([str(java), "-classpath", str(classes) + os.pathsep + classpath,
                            "OfflineTestLauncher", str(results_file), *report["test_classes"]])
            if results_file.is_file():
                report["junit"] = json.loads(results_file.read_text())
            if executed.returncode:
                raise RuntimeError(f"JUnit execution failed with exit {executed.returncode}; see log and actual results.")
            if "junit" not in report:
                raise RuntimeError("JUnit exited without producing actual result data.")
            report["status"] = "passed"
            exit_code = 0
    except (OSError, RuntimeError, ValueError) as error:
        report["status"] = "failed"
        report["error"] = str(error)
        log.append("ERROR: " + str(error) + "\n")
    report["finished_at_utc"] = datetime.now(timezone.utc).isoformat()
    report_path.parent.mkdir(parents=True, exist_ok=True)
    log_path.write_text("".join(log), encoding="utf-8")
    report_path.write_text(json.dumps(report, indent=2, ensure_ascii=False) + "\n", encoding="utf-8")
    print(f"Offline JUnit: {report['status']}; report: {report_path}; log: {log_path}")
    if "junit" in report:
        print(json.dumps(report["junit"]["counts"]))
    if "error" in report:
        print(report["error"])
    return exit_code


if __name__ == "__main__":
    raise SystemExit(main())
