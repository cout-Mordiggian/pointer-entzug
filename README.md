# Java von Grund auf

Ein vollständiger Selbstlernkurs: **Dokumentation + Aufgaben + Musterlösungen**,
ausführbar im Terminal oder in der IDE (IntelliJ IDEA, VS Code), ohne Maven
oder Gradle. Läuft unter Windows, macOS und Linux.

Zielsprache: **Java 21 (LTS) oder neuer.** Der Kurs nutzt nur Sprachmittel, die
es seit Java 21 gibt, und läuft deshalb mit jedem neueren JDK (welches du
installiert hast, zeigt `java -version`). Wo neuere Versionen eine Regel
gelockert haben, steht ein Hinweis *"Seit Java NN"* (etwa 22 oder 25).

---

## Vorher: Java und Kurs holen

1. **JDK ab Version 21** installieren. Wie du prüfst, ob eins da ist, und wie
   du es installierst (auch unter Windows), steht in [`SETUP.md`](SETUP.md).
2. **Den Kurs holen**, eine der Varianten:
   - Im Terminal: `git clone https://github.com/cout-Mordiggian/pointer-entzug.git`
   - In **IntelliJ IDEA**: Startbildschirm > *Clone Repository* (bzw. *Get from
     VCS*), die URL oben einfügen.
   - In **VS Code**: Ansicht *Source Control* > *Clone Repository*, die URL
     einfügen.
   - Ohne Git: auf GitHub *Code > Download ZIP* und entpacken. Dann bekommst
     du allerdings keine Aktualisierungen per `git pull`.
3. **Den Kursordner öffnen**, als Ordner bzw. Projekt in der IDE oder per `cd`
   im Terminal. Wie du IntelliJ und VS Code einrichtest, damit sie die
   Aufgaben verstehen und du sie per Klick starten und debuggen kannst, steht
   in [`SETUP.md`, Abschnitt 3](SETUP.md#3-editor-und-ide).

## Die Prüfungen starten

Geprüft wird jedes Kapitel mit dem Kurs-Runner. Er läuft in jedem Terminal,
auch im eingebauten Terminal von IntelliJ (*View > Tool Windows > Terminal*)
und VS Code (*Terminal > New Terminal*). Je nach Terminal heißt der Befehl
etwas anders:

| Terminal | Befehl |
|----------|--------|
| **Windows** (PowerShell oder cmd, auch das Terminal in IntelliJ und VS Code) | `.\lerne 01` |
| **macOS, Linux, Git Bash** | `./lerne.sh 01` |
| überall, wo `java` läuft | `java Lerne.java 01` |

Alle drei machen genau dasselbe. **Im Kurs steht immer `./lerne.sh`**; unter
Windows schreibst du stattdessen `.\lerne`, der Rest der Zeile bleibt gleich.
Aus `./lerne.sh 02 -l -r Zahlenraten` wird also `.\lerne 02 -l -r Zahlenraten`.

```bash
cd pointer-entzug    # der Ordner, in den du den Kurs geklont hast
./lerne.sh            # Kapiteluebersicht            (Windows: .\lerne)
./lerne.sh 01         # Kapitel 1 pruefen, schlaegt anfangs fehl, das ist der Plan
```

Dann:

1. `kapitel/01-erste-schritte/README.md` lesen.
2. Die `TODO`-Stellen in `kapitel/01-erste-schritte/src/Aufgaben.java` ausfüllen.
3. `./lerne.sh 01` erneut ausführen, bis alles grün ist.
4. Erst **danach** die Musterlösung ansehen: `./lerne.sh 01 -l` bzw. `kapitel/01-*/loesungen/`.
5. Die Rätsel unter **Was gibt das aus?** lösen und die Fragen unter
   **Selbstcheck** ohne Nachschlagen beantworten. Die Antworten stehen in
   `TIPPS.md` des Kapitels.

Falls es hakt:

- **`java` wird nicht gefunden:** Das JDK fehlt oder steht nicht im `PATH`.
  Nach der Installation das Terminal (bzw. die IDE) **neu starten**, siehe
  [`SETUP.md`](SETUP.md).
- **`Error: A JNI error has occurred` mit `Module jdk.compiler not in boot
  Layer`:** Installiert ist nur ein *JRE* (Laufzeit ohne Compiler). Du
  brauchst ein *JDK*, siehe [`SETUP.md`](SETUP.md).
- **`Dieser Kurs braucht Java 21 oder neuer`** (bei sehr altem Java auch
  `Could not find or load main class Lerne.java` oder Kompilierfehler in
  `Lerne.java`): Im `PATH` steht ein älteres Java vor dem neuen, siehe
  [`SETUP.md`](SETUP.md).
- **Windows: `./lerne.sh` klappt nicht** (Fehlermeldung, oder Windows fragt,
  mit welchem Programm es die Datei öffnen soll): Du bist in PowerShell oder
  cmd, nimm `.\lerne`.
- **macOS/Linux: `Permission denied`:** Es fehlt das Ausführungsrecht, etwa
  auf Cloud-Laufwerken, die keine Dateirechte speichern. Abhilfe:
  `chmod +x lerne.sh projekt/starte.sh`, oder einfach `bash lerne.sh 01`.
- **Seltsame Zeichen wie `←[32m` statt Farben:** Die alte Windows-Konsole
  kann keine Farben. Nimm das Terminal in IntelliJ oder VS Code oder das
  *Windows Terminal* (bei Windows 11 dabei). Wer ganz ohne Farben arbeiten
  will, setzt die Umgebungsvariable `NO_COLOR` vor dem Start: in PowerShell
  `$env:NO_COLOR = "1"`, in cmd `set NO_COLOR=1`, in Bash
  `export NO_COLOR=1`. Das gilt bis zum Schließen des Terminals.
- **Windows: `Batchvorgang abbrechen (J/N)?`** nach Strg+C (etwa um
  `Zahlenraten` abzubrechen): Das fragt cmd, weil `lerne.cmd` eine
  Batch-Datei ist. Mit `J` bestätigen, es geht nichts verloren.
- **Windows: Fehler beim Löschen von `build\`** oder seltsam langsame Läufe:
  Liegt der Kurs in einem OneDrive-Ordner (oft *Dokumente* oder *Desktop*),
  synchronisiert OneDrive jede erzeugte `.class`-Datei und kann sie dabei
  kurz sperren. Lege den Kurs besser außerhalb ab, etwa unter `C:\Users\<Name>\kurse\`.

> Der wichtigste Lerneffekt entsteht zwischen "es kompiliert nicht" und "es ist grün".
> Widerstehe der Versuchung, zu früh in `loesungen/` zu schauen.

---

## Aufbau des Kurses

| Nr | Kapitel | Worum es geht |
|----|---------|---------------|
| 01 | Erste Schritte | JVM, `main`, Kompilieren, Variablen, primitive Typen, Casting |
| 02 | Operatoren und Kontrollfluss | Operatoren, `if`/`switch`, Schleifen, `break`/`continue` |
| 03 | Strings und Arrays | Unveränderlichkeit, `equals` vs `==`, `StringBuilder`, 1D-/2D-Arrays |
| 04 | Methoden | Signatur, call-by-value, Überladung, Rekursion, Varargs |
| 05 | OOP I: Klassen und Objekte | Felder, Konstruktoren, Kapselung, `static`, `toString`/`equals`, Singleton, statische Fabrikmethode |
| 06 | OOP II: Vererbung, Polymorphie, Interfaces | `extends`, Polymorphie, abstrakte Klassen, Interfaces, Sichtbarkeiten, Strategie, Dekorierer, Adapter |
| 07 | Exceptions | checked/unchecked, `try`/`catch`/`finally`, try-with-resources, eigene Fehler |
| 08 | Collections und Generics | `List`, `Set`, `Map`, `Comparator`, Typparameter, Wildcards |
| 09 | Lambdas und Streams | funktionale Interfaces, Methodenreferenzen, Pipelines, `Optional`, Fabrik, Beobachter |
| 10 | Modernes Java | `var`, `record`, `enum`, `sealed`, Pattern Matching, Textblöcke, Builder |
| 11 | Dateien und IO | `Path`/`Files`, Zeichensätze, Zeilen-Streams, CSV |
| 12 | Nebenläufigkeit und Werkzeuge | Threads, `ExecutorService`, Race Conditions, Maven/Gradle, JUnit |
| 13 | Testen und Fehlersuche | eigene Tests schreiben, Grenzwerte, Mutanten entlarven, systematisch debuggen |
| 14 | Algorithmen und Datenstrukturen | Aufwand/O-Notation, Sortieren, eigene Liste und eigener Stapel, Iterator, Befehl/Rückgängig |
|    | [Abschlussprojekt](projekt/README.md) | Aufgabenverwaltung als Konsolen-Anwendung |

Zusätzlich:

- [`SETUP.md`](SETUP.md): JDK, Editor, Kompilieren von Hand, Fehlermeldungen lesen
- [`spickzettel/syntax.md`](spickzettel/syntax.md): Syntax auf einen Blick
- [`spickzettel/glossar.md`](spickzettel/glossar.md): Begriffe von "Adapter" bis "Wrapper-Klasse"
- [`spickzettel/fehlermeldungen.md`](spickzettel/fehlermeldungen.md): die häufigsten Compiler- und Laufzeitfehler und was sie bedeuten
- [`spickzettel/entwurfsmuster.md`](spickzettel/entwurfsmuster.md): die Entwurfsmuster sind auf die Kapitel verteilt; hier stehen alle auf einen Blick, mit Fundstelle im Kurs und im JDK

---

## Wie ein Kapitel aufgebaut ist

```
kapitel/05-oop-klassen-und-objekte/
├── README.md        Dokumentation: Konzepte, Beispiele, Fallstricke,
│                    Aufgaben, "Was gibt das aus?", Selbstcheck
├── TIPPS.md         gestufte Hinweise je Aufgabe + Antworten zum Selbstcheck
├── src/             DEIN Arbeitsbereich, hier stehen die TODOs
├── tests/           Pruefungen (nicht aendern, sie definieren die Aufgabe)
└── loesungen/       Musterloesung mit Kommentaren (erst danach lesen)
```

`./lerne.sh 05` kompiliert `lib/ + src/ + tests/` und startet die Prüfungen.
`./lerne.sh 05 -l` macht dasselbe mit `loesungen/` statt `src/`.

Eigenen Code ausprobieren? Lege eine Klasse mit `main`-Methode in `src/` an
und starte sie mit `./lerne.sh 05 -r MeinTest`.

Für schnelle Einzeiler ("Was ergibt eigentlich `7 / 2`?") ist **`jshell`** noch
bequemer, die interaktive Java-Konsole, siehe [`SETUP.md`](SETUP.md).

---

## Wenn du feststeckst

1. **Fehlermeldung lesen**: den *ersten* Fehler, von oben. Nachschlagen in
   [`spickzettel/fehlermeldungen.md`](spickzettel/fehlermeldungen.md).
2. **Den Test lesen.** `tests/Tests.java` zeigt genau, welche Eingabe welches
   Ergebnis erwartet. Die Tests sind die eigentliche Aufgabenstellung.
3. **Klein ausprobieren**: in `jshell` oder mit `System.out.println` in einer
   eigenen Klasse (`./lerne.sh NN -r MeinTest`).
4. **Das README-Kapitel noch einmal lesen**, gezielt den Abschnitt zum Thema.
5. **`TIPPS.md` im Kapitelordner**: pro Aufgabe bis zu drei Stufen, von "in welche
   Richtung denken" bis "Gerüst mit Lücken". Immer nur die nächste Stufe
   aufklappen, dann wieder selbst probieren.
6. Erst dann: die Musterlösung, und danach die Aufgabe **noch einmal ohne
   Vorlage** lösen. Abschreiben allein bringt nichts.

---

## Empfohlener Rhythmus

- **Ein Kapitel pro Sitzung**, 45 bis 90 Minuten. Kapitel 5, 6, 8 und 9 sind die dicksten Brocken.
- Die Kapitel 13 und 14 stehen hinten, gehören aber **dazwischen**:
  **Kapitel 13 direkt nach Kapitel 7**, **Kapitel 14 nach Kapitel 8**.
  Empfohlene Reihenfolge also: 1 bis 7, **13**, 8, **14**, 9 bis 12.
- Nach jedem Kapitel: eine Sache aus dem Kapitel **freihändig** nachbauen, ohne README.
- Nach Kapitel 8: Meilensteine M1 und M2 des Abschlussprojekts angehen.
  M1 benutzt `record`, `enum` und `LocalDate`, lies dafür vorab die
  Abschnitte 10.2, 10.3 und 10.7, sie sind kurz und setzen nur Kapitel 5 voraus.
- Nach Kapitel 11: das Projekt fertigstellen (M3 bis M5), mit Kapitel 12 erweitern (M6).
- Wiederholen lohnt sich: Die Selbstcheck-Fragen von zwei Kapiteln zuvor nach
  einer Woche noch einmal beantworten, Vergessenes jetzt aufzufrischen ist billiger als später.

Wenn du weniger Zeit hast: Kapitel 1 bis 8 und 13 sind der unverzichtbare Kern.
Kapitel 9 bis 12 machen dich vom "kann Java lesen" zum "schreibe Java, wie man es heute schreibt".

---

## Was dieser Kurs bewusst weglässt

Module (JPMS), Reflection, Annotationsprozessoren, JNI, Frameworks wie Spring.
Das sind Aufbauthemen, sie ergeben erst Sinn, wenn die Grundlagen sitzen.
Am Ende von Kapitel 12 findest du einen Ausblick, wie es danach weitergeht.
