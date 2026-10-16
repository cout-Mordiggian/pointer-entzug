# Setup und Werkzeuge

## 1. Welches Java ist installiert?

```bash
java -version    # die Laufzeitumgebung (JVM) - fuehrt Bytecode aus
javac -version   # der Compiler - macht aus .java Bytecode (.class)
```

Beide Befehle sollten eine Versionsnummer von **21 oder höher** ausgeben.
Der Kurs verlangt mindestens Java 21, alles Neuere funktioniert ebenso, denn
Java bleibt rückwärtskompatibel. Ein *JRE* (nur Laufzeit) hätte kein `javac`,
zum Entwickeln brauchst du ein *JDK* (Java Development Kit).

Fehlt Java oder ist es zu alt, installiere ein aktuelles JDK, z. B.
**Eclipse Temurin** von <https://adoptium.net>, oder über den Paketmanager
deines Systems (`apt`, `dnf`, `pacman`, `brew`, `winget` …) bzw. mit
[SDKMAN!](https://sdkman.io).

- **Windows:** Lade auf <https://adoptium.net> den Installer (`.msi`) herunter
  und lass bei der Installation "Add to PATH" und "Set JAVA_HOME" aktiviert.
  Alternativ in PowerShell: `winget install EclipseAdoptium.Temurin.21.JDK`.
  Danach Terminal und IDE **neu starten**, sonst finden sie `java` noch nicht.
  Zeigt `java -version` danach immer noch eine alte Version (z. B. `1.8`),
  steht ein älteres Java weiter vorne im `PATH`: Windows-Suche
  "Umgebungsvariablen", unter *Path* den Eintrag des neuen JDK nach oben
  schieben (ein Eintrag `...\Oracle\Java\javapath` ist oft der Übeltäter).
- **macOS:** Den `.pkg`-Installer von <https://adoptium.net> nehmen, oder mit
  Homebrew: `brew install --cask temurin`.
- **Nur IntelliJ:** IntelliJ kann ein JDK auch selbst herunterladen, siehe
  Abschnitt 3. Für den Runner im Terminal muss `java` aber im `PATH` stehen,
  also lieber einmal richtig installieren.

| Begriff | Bedeutung |
|---------|-----------|
| **JDK** | Compiler + Werkzeuge + JRE. Das brauchst du. |
| **JRE** | Nur die Laufzeitumgebung. Reicht zum Ausführen, nicht zum Entwickeln. |
| **JVM** | Die virtuelle Maschine, die Bytecode ausführt. Teil des JRE. |
| **LTS** | Long Term Support. 8, 11, 17, 21, 25 sind LTS-Versionen. Dazwischen erscheint alle sechs Monate eine Version mit kurzer Lebensdauer (z. B. 26, 27). |

## 2. Der Kompilier-Zyklus von Hand

Damit du verstehst, was der Runner (`./lerne.sh` bzw. `.\lerne`) für dich
erledigt: Lege in einem leeren Ordner mit einem Editor die Datei `Hallo.java`
an:

```java
public class Hallo {
    public static void main(String[] args) {
        System.out.println("Hallo, Java!");
    }
}
```

Unter Windows hängt der Editor gern unbemerkt `.txt` an (`Hallo.java.txt`).
Schalte im Explorer *Ansicht > Einblenden > Dateinamenerweiterungen* ein,
dann siehst du das.

Dann im Terminal, in diesem Ordner (die Befehle sind unter Windows, macOS und
Linux gleich):

```bash
javac Hallo.java     # erzeugt Hallo.class (Bytecode)
java Hallo           # fuehrt aus - ACHTUNG: ohne .class und ohne .java!
```

Wichtige Regeln, die Anfänger oft stolpern lassen:

- Eine `public` Klasse muss **genauso heissen wie die Datei**: `Hallo` -> `Hallo.java`.
- `java Hallo` nimmt einen **Klassennamen**, keinen Dateinamen.
- `-d ziel/` sagt dem Compiler, wohin die `.class`-Dateien sollen.
- `-cp` (classpath) sagt der JVM, wo sie Klassen suchen soll.

Seit Java 11 geht auch der Schnellstart für eine einzelne Datei, praktisch zum Ausprobieren:

```bash
java Hallo.java      # kompiliert im Speicher und startet sofort
```

So startet übrigens auch der Kurs-Runner: `java Lerne.java 01`.

Seit **Java 25** darf ein kleines Programm sogar ganz ohne Klasse,
`public static` und `String[] args` auskommen (in Java 21 war das noch ein
Preview-Feature):

```java
void main() {
    IO.println("Hallo, Java!");
}
```

In diesem Kurs bleiben wir bei der klassischen Form, weil du sie in jedem
existierenden Projekt antreffen wirst, und weil sie zeigt, was wirklich
passiert (Klasse, statische Methode, Parameter).

## 3. Editor und IDE

Alles im Kurs funktioniert mit einem beliebigen Texteditor. Trotzdem lohnt sich
Werkzeugunterstützung, Java ist statisch typisiert, ein guter Editor sagt dir
Fehler *während* du tippst statt erst beim Kompilieren.

- **IntelliJ IDEA Community** (kostenlos, der De-facto-Standard in der Java-Welt)
- **VS Code** + *Extension Pack for Java* (leichtgewichtig, guter Einstieg)
- **Neovim/Helix** + `jdtls` per LSP

### Warum die IDE etwas Nachhilfe braucht

Der Kurs hat bewusst keine Build-Datei (kein Maven, kein Gradle). Die IDE weiß
deshalb nicht von selbst, welche Ordner Java-Quellcode enthalten. Außerdem
gibt es Klassen wie `Aufgaben` und `Tests` in **jedem** Kapitel, und in
`loesungen/` noch einmal. Würde die IDE alles auf einmal kompilieren, meldete
sie lauter "duplicate class".

Die Lösung: Du sagst der IDE, dass immer **genau drei Ordner** Quellcode sind:

| Ordner | Inhalt |
|--------|--------|
| `lib/` | das Prüf-Werkzeug `Pruef`, das alle Tests benutzen |
| `kapitel/NN-.../src/` | deine Aufgaben im aktuellen Kapitel |
| `kapitel/NN-.../tests/` | die Prüfungen dazu |

`loesungen/` gehört **nicht** dazu. Wechselst du zum nächsten Kapitel, tauschst
du die beiden Kapitelordner aus. Das dauert eine halbe Minute.

Ohne diese Einrichtung kannst du die IDE trotzdem als Editor benutzen und im
eingebauten Terminal den Runner starten. Dann fehlen nur Autovervollständigung,
Fehlermarkierungen und die Start- und Debug-Knöpfe.

### IntelliJ IDEA

1. **Öffnen:** *File > Open*, den Kursordner `pointer-entzug` wählen (oder
   gleich klonen, siehe README). Die Frage nach "Trust Project" mit *Trust*
   beantworten.
2. **JDK einstellen:** *File > Project Structure > Project*. Bei *SDK* ein JDK
   ab 21 wählen. Ist keins da, bietet die Liste *Download JDK* an (z. B.
   Eclipse Temurin 21). *Language level*: "SDK default". Ist das Feld
   *Compiler output* leer, dort den Ordner `out` im Kursordner eintragen.
3. **Quellordner markieren:** Im Projektbaum links auf `lib` rechtsklicken >
   *Mark Directory as > Sources Root*. Dasselbe für `src` und `tests` des
   aktuellen Kapitels. Die Ordner werden blau.
4. **Starten:** `tests/Tests.java` öffnen und auf das grüne Dreieck neben
   `main` klicken > *Run*. Das ist dasselbe wie `./lerne.sh NN`. Mit dem
   Käfer-Symbol (*Debug*) hält das Programm an deinen Breakpoints. Eigene
   Klassen mit `main` (z. B. `Spielwiese`) startest du genauso.
5. **Kapitelwechsel:** Auf die alten `src` und `tests` rechtsklicken > *Mark
   Directory as > Unmark as Sources Root*, dann die neuen markieren.

Zeigt IntelliJ oben in einer Datei "Java file is located outside of the module
source root", ist ihr Ordner nicht markiert. Meldet es "duplicate class", sind
zwei Kapitel gleichzeitig markiert (oder `loesungen/`).

Das Terminal (*View > Tool Windows > Terminal*) öffnet unter Windows eine
PowerShell, dort heißt der Runner `.\lerne NN`.

### VS Code

1. **Erweiterung installieren:** In der Ansicht *Extensions* nach
   "Extension Pack for Java" (von Microsoft) suchen und installieren.
2. **Öffnen:** *File > Open Folder*, den Kursordner `pointer-entzug` wählen.
3. **Quellordner festlegen:** Im Kursordner den Ordner `.vscode` und darin die
   Datei `settings.json` anlegen (beide sind in `.gitignore`, landen also
   nicht im Repository):

   ```json
   {
     "java.project.sourcePaths": [
       "lib",
       "kapitel/01-erste-schritte/src",
       "kapitel/01-erste-schritte/tests"
     ]
   }
   ```

   Den Kapitelnamen genau so schreiben wie den Ordner im Explorer. Schneller
   geht es oft per Rechtsklick auf einen Ordner im Explorer > *Add Folder to
   Java Source Path* (bzw. *Remove Folder from Java Source Path*), das
   schreibt dieselbe Einstellung.
4. **Starten:** `tests/Tests.java` öffnen. Über `main` stehen die Links *Run*
   und *Debug*. *Run* ist dasselbe wie `./lerne.sh NN`.
5. **Kapitelwechsel:** In `settings.json` die beiden Kapitelzeilen auf das
   neue Kapitel ändern und speichern.

Zeigt VS Code danach noch alte Fehler an, hilft *View > Command Palette*
(`Strg+Umschalt+P`) > "Java: Clean Java Language Server Workspace".

Findet die Erweiterung kein JDK, obwohl eins installiert ist: VS Code komplett
schließen und neu öffnen. Hilft das nicht, in `settings.json` zusätzlich den
JDK-Ordner eintragen, unter Windows mit normalen Schrägstrichen, z. B.
`"java.jdt.ls.java.home": "C:/Program Files/Eclipse Adoptium/jdk-21.0.8.9-hotspot"`
(den genauen Ordnernamen im Explorer nachsehen).

Das Terminal (*Terminal > New Terminal*) ist unter Windows eine PowerShell,
dort heißt der Runner `.\lerne NN`. Über den Pfeil neben dem `+` im
Terminal kannst du auch Git Bash wählen, falls installiert.

### Und die Musterlösung?

Die startest du am einfachsten im Terminal mit `./lerne.sh NN -l`
(Windows: `.\lerne NN -l`). Lesen kannst du sie in der IDE jederzeit, auch
ohne dass `loesungen/` markiert ist.

### Das Abschlussprojekt

Für das Projekt markierst du nur `projekt/src` als Quellordner (die
Kapitelordner vorher entfernen) und startest `Main`. Im Terminal geht es mit
`./starte.sh` im Ordner `projekt`, unter Windows mit `.\starte`.

Das Projekt speichert in `aufgaben.csv` im **Arbeitsverzeichnis**. Beim Start
aus der IDE ist das der Kursordner, nicht `projekt/`. Damit die Datei dort
landet, wo `starte.sh` sie sucht:

- **IntelliJ:** *Run > Edit Configurations*, bei `Main` als *Working
  directory* den Ordner `projekt` eintragen.
- **VS Code:** am einfachsten im Terminal mit `.\starte` bzw. `./starte.sh`
  starten. Wer *Run* über `main` nutzen will, legt per *Run > Add
  Configuration* eine `launch.json` an und setzt dort bei `Main`
  `"cwd": "${workspaceFolder}/projekt"`.

Welche Datei gerade benutzt wird, zeigt das Programm beim Start an.

## 4. jshell: Java zum Ausprobieren

```bash
jshell
jshell> 7 / 2
$1 ==> 3
jshell> "Hallo".substring(1, 3)
$2 ==> "al"
jshell> /exit
```

`jshell` führt einzelne Java-Ausdrücke sofort aus, ohne Klasse, ohne `main`,
ohne Kompilieren. Ideal für Fragen wie "Was liefert `Math.round(-2.5)`?".
Gewöhne es dir ab Kapitel 1 an: Jedes Code-Schnipsel aus den Kapiteln kannst
du dort in Sekunden nachprüfen. `/help` zeigt die Befehle, Tab vervollständigt.

## 5. Fehlermeldungen lesen

Der Compiler meldet Fehler in dieser Form:

```
src/Aufgaben.java:12: error: cannot find symbol
        return laenge * breit;
                        ^
  symbol:   variable breit
  location: class Aufgaben
1 error
```

Lies sie von oben nach unten:

1. **Datei und Zeile**: `src/Aufgaben.java:12`
2. **Fehlerart**: `cannot find symbol` = ein Name, den der Compiler nicht kennt
3. **Das Zirkumflex `^`** zeigt exakt auf die Stelle
4. **`symbol:`** sagt, *welcher* Name unbekannt ist

Der **erste** Fehler ist der echte. Folgefehler verschwinden oft von allein,
wenn du den ersten behebst. Nicht von "37 errors" einschüchtern lassen.

Eine Sammlung typischer Meldungen mit Erklärung:
[`spickzettel/fehlermeldungen.md`](spickzettel/fehlermeldungen.md).

## 6. Wo du nachschlägst

- **Javadoc der Standardbibliothek**: <https://docs.oracle.com/en/java/javase/21/docs/api/>
  Das ist die Referenz. Gewöhne dir an, dort statt in Blogposts nachzusehen.
- `javap -cp build/01 Aufgaben` zeigt die Signaturen einer kompilierten Klasse.
- Die offizielle Sprachspezifikation (JLS) ist präzise, aber kein Lernmaterial.
