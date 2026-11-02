#!/bin/bash
# Run manually when the agent environment cannot download dependencies or open Gradle sockets.
# Default: build + JUnit. "validate": actual core and optional-camp Minecraft servers.
# Uses the same writable dependency cache as the agent and saves every stage's log.
set -uo pipefail

mode="${1:-build}"
if [ "$#" -gt 1 ] || { [ "$mode" != build ] && [ "$mode" != validate ]; }; then
    printf '%s\n' 'Usage: bash tools/build_local.sh [build|validate]' >&2
    exit 64
fi

project_dir="$(cd "$(dirname "$0")/.." && pwd)" || exit 1
cd "$project_dir" || exit 1

export JAVA_HOME='/Applications/Android Studio.app/Contents/jbr/Contents/Home'
export GRADLE_USER_HOME='/private/tmp/dungeonsalive-gradle-setup-user'
if [ ! -x "$JAVA_HOME/bin/java" ]; then
    printf '%s\n' "Expected Java 21 was not found: $JAVA_HOME/bin/java" >&2
    exit 1
fi
mkdir -p "$GRADLE_USER_HOME" reports || exit 1

run_gradle_task() {
    local task="$1"
    local log="$2"
    printf '\n%s\n' "Starting Dungeons Alive task: $task"
    ./gradlew "$task" --console=plain 2>&1 | tee "$log"
    local gradle_result=$?
    printf '\n%s\n' "Task exit code: $gradle_result" "Log saved to: $project_dir/$log"
    return "$gradle_result"
}

if [ "$mode" = build ]; then
    run_gradle_task build reports/local-build.log
    exit "$?"
fi

run_gradle_task runGameTestServer reports/local-gametest.log || exit "$?"
python3 tools/prepare_camp_gametest.py 2>&1 | tee reports/local-camp-prepare.log || exit "$?"
run_gradle_task runCampGameTestServer reports/local-camp-gametest.log || exit "$?"
printf '\n%s\n' 'Both Minecraft server commands completed. Logs are saved in reports/. Native client review remains separate.'
