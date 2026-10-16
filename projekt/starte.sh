#!/usr/bin/env bash
# Kompiliert das Projekt nach build/ und startet es.
set -euo pipefail
cd "$(dirname "${BASH_SOURCE[0]}")"
# Frisch bauen: sonst blieben .class-Dateien geloeschter/umbenannter Klassen liegen.
rm -rf build
mkdir -p build
# Compiler englisch wie im Runner und im Spickzettel, das Programm selbst mit System-Locale
javac -J-Duser.language=en -g -encoding UTF-8 -d build src/*.java
java -cp build Main "$@"
