#!/bin/bash
# Run Digital CLI testcases for this assignment.
# Usage:
#   DIGITAL_JAR="D:/path/Digital.jar" ./run-tests.sh
#   DIGITAL_JAR=/tmp/digital/Digital/Digital.jar ./run-tests.sh
set -euo pipefail
DIR="$(cd "$(dirname "$0")" && pwd)"
JAR="${DIGITAL_JAR:-}"
if [[ -z "$JAR" ]]; then
  for candidate in \
      "$DIR/Digital.jar" \
      "/tmp/digital/Digital/Digital.jar" \
      "$DIR/../Digital.jar"; do
    if [[ -f "$candidate" ]]; then
      JAR="$candidate"
      break
    fi
  done
fi
if [[ -z "${JAR:-}" || ! -f "$JAR" ]]; then
  echo "Set DIGITAL_JAR to your Digital.jar path." >&2
  exit 1
fi
echo "Using $JAR"
for f in HalfAdder.dig Successor4.dig Negation3.dig; do
  echo "=== $f ==="
  java -cp "$JAR" CLI test -circ "$DIR/$f"
done
