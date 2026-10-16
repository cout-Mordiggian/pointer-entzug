#!/usr/bin/env bash
#
# Lern-Runner fuer den Java-Kurs. Die Logik steht in Lerne.java,
# dieses Skript ist nur die Kurzform fuer Bash.
#
#   ./lerne.sh              Kapiteluebersicht
#   ./lerne.sh 03           Kapitel 03 kompilieren und pruefen
#   ./lerne.sh 03 -l        gegen die Musterloesung pruefen
#   ./lerne.sh alle         alle Kapitel pruefen
#   ./lerne.sh 03 -r Demo   Klasse "Demo" aus dem Kapitel ausfuehren
#
exec java -Dlerne.aufruf=./lerne.sh "$(dirname "${BASH_SOURCE[0]}")/Lerne.java" "$@"
