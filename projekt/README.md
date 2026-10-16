# Abschlussprojekt: Aufgabenverwaltung

Eine Konsolen-Anwendung, die alles aus dem Kurs zusammenführt: eigene Typen,
Kapselung, Collections, Streams, Exceptions und Dateizugriff.

Anders als in den Kapiteln gibt es hier **keine Tests, die dir sagen, wann du
fertig bist**. Genau das ist der Punkt: Du entscheidest über den Entwurf.

---

## Was am Ende laufen soll

```
$ ./starte.sh

  Aufgabenverwaltung  (3 Aufgaben, 2 offen)
  -----------------------------------------
  1  Aufgaben anzeigen
  2  Aufgabe hinzufuegen
  3  Aufgabe abhaken
  4  Aufgabe loeschen
  5  Suchen
  6  Statistik
  0  Beenden

> 1

    ID  Prio     Faellig      Titel
  --------------------------------------------------
     1  HOCH     2026-09-05   [ ] Steuererklaerung
     2  MITTEL   2026-09-20   [x] Reifen wechseln
     3  NIEDRIG  -            [ ] Keller aufraeumen
```

Beim Beenden werden die Aufgaben in `aufgaben.csv` gespeichert und beim nächsten
Start wieder geladen.

---

## Meilensteine

Arbeite sie der Reihe nach ab. Nach jedem Schritt soll das Programm laufen,
nicht alles auf einmal bauen.

### M1: Datenmodell *(Kapitel 5, 10)*

- `enum Prioritaet { NIEDRIG, MITTEL, HOCH }`
- `record Aufgabe(int id, String titel, Prioritaet prioritaet, boolean erledigt, LocalDate faellig)`
  - Kompakter Konstruktor: Titel darf nicht leer sein, `id` muss positiv sein
  - `faellig` darf `null` sein (= kein Termin)
  - `Aufgabe abgehakt()`: gibt eine Kopie mit `erledigt = true` zurück
  - `boolean istUeberfaellig()`: Termin in der Vergangenheit und nicht erledigt

  *Tipp für spätere Tests (M6):* Mit `LocalDate.now()` im Inneren hängt das
  Ergebnis vom Tag ab, an dem der Test läuft. Testbarer ist
  `boolean istUeberfaellig(LocalDate heute)`: der Aufrufer übergibt das Datum,
  und `istUeberfaellig()` ruft einfach `istUeberfaellig(LocalDate.now())` auf.

`LocalDate` erklärt Kapitel 10, Abschnitt 10.7, das Wichtigste:
`LocalDate.now()`, `LocalDate.parse("2026-09-05")`, `d.isBefore(andere)`.
Nutze **niemals** `java.util.Date`, die Klasse ist seit 2014 abgelöst.

### M2: Verwaltung *(Kapitel 7, 8)*

Klasse `Aufgabenliste`:

```java
Aufgabenliste(List<Aufgabe> geladene)                            // Startbestand, z. B. aus der CSV
int hinzufuegen(String titel, Prioritaet p, LocalDate faellig)   // vergibt die ID, gibt sie zurueck
void abhaken(int id)                                             // wirft, wenn es die ID nicht gibt
void loeschen(int id)
Optional<Aufgabe> finde(int id)
List<Aufgabe> alle()                                             // unveraenderliche Sicht!
```

`Optional` (Abschnitt 9.6) kommt erst nach diesem Meilenstein. Für M2 reicht:
`finde` gibt `Optional.of(aufgabe)` zurück oder `Optional.empty()`, wenn es die
ID nicht gibt. Der Aufrufer fragt mit `isPresent()` und holt den Wert mit
`orElseThrow()` (ohne Argument). Den Rest von 9.6, mit Lambdas, brauchst du
erst später.

Eigene Exception `AufgabeNichtGefundenException`.
*Entwurfsfrage:* checked oder unchecked? Begründe deine Wahl in einem Kommentar.

Die Liste ist der einzige Ort, an dem IDs vergeben werden, und `alle()` gibt
keine veränderbare Referenz nach draußen (Kapitel 5).

Der Konstruktor bekommt die beim Start geladenen Aufgaben (leere Liste beim
ersten Start). Die nächste freie ID ist dann *größte vorhandene ID + 1*,
nicht `size() + 1`, sonst vergibst du nach einem Löschen eine ID doppelt.
Kopiere die übergebene Liste, statt sie direkt zu speichern (Kapitel 5).

### M3: Abfragen *(Kapitel 9)*

```java
List<Aufgabe> offene()
List<Aufgabe> nachPrioritaet()                    // HOCH zuerst, dann nach Termin
List<Aufgabe> ueberfaellige()
List<Aufgabe> suche(String text)                  // Titel enthaelt text, ohne Gross/Klein
Map<Prioritaet, Long> anzahlProPrioritaet()
```

Alles mit Streams. `Comparator.comparing(...).thenComparing(...)` und
`Comparator.nullsLast(...)` für die Termine.

### M4: Persistenz *(Kapitel 11)*

Klasse `CsvSpeicher`:

```java
void speichern(Path pfad, List<Aufgabe> aufgaben) throws IOException
List<Aufgabe> laden(Path pfad) throws IOException     // fehlende Datei -> leere Liste
```

Format: eine Aufgabe pro Zeile, Trennzeichen `;`, erste Zeile ist die
Kopfzeile, Zeichensatz UTF-8 (`StandardCharsets.UTF_8` ausdrücklich angeben).
Die Felder in dieser Reihenfolge:

- `id`: ganze Zahl
- `titel`: Text; was mit einem `;` darin passiert, entscheidest du (siehe Fallen unten)
- `prioritaet`: genau der Name der Konstante, `NIEDRIG`, `MITTEL` oder `HOCH`
- `erledigt`: `true` oder `false`
- `faellig`: ISO-Format `jjjj-mm-tt`, also das, was `LocalDate.toString()`
  liefert und `LocalDate.parse(...)` liest; leer, wenn es keinen Termin gibt.
  Nicht `dd.MM.yyyy`: ISO braucht keinen eigenen Formatter, und als Text
  sortiert, steht es gleich in zeitlicher Reihenfolge.

Beispiel:

```
id;titel;prioritaet;erledigt;faellig
1;Steuererklaerung;HOCH;false;2026-09-05
3;Keller aufraeumen;NIEDRIG;false;
```

Fallen, die du lösen musst:

- Ein Titel mit `;` darin zerlegt deine Zeile. Wie gehst du damit um?
  (Ablehnen beim Anlegen ist eine völlig legitime Antwort, dokumentiere sie.)
- Leeres Datumsfeld -> `null`
- `split(";", -1)`, sonst verschwindet das leere letzte Feld
- Kaputte Zeile: Überspringen mit Warnung oder abbrechen? Entscheide und
  begründe es.
- `Prioritaet.valueOf(...)` wirft bei unbekanntem Text
- `Boolean.parseBoolean(...)` wirft dagegen **nie**: Aus `"ja"` oder `"ture"`
  wird still `false`. Willst du kaputte Zeilen erkennen, prüfe selbst auf
  `"true"` und `"false"`.

### M5: Benutzeroberfläche *(Kapitel 2, 3, 11)*

Klasse `Konsole` mit der Menüschleife. Anforderungen:

- Ungültige Eingaben dürfen das Programm **nie** abstürzen lassen
- Zahlen mit `Integer.parseInt` in `try`/`catch`, nicht `scanner.nextInt()`
- Die Tabelle sauber ausrichten, wie im Beispiel oben:
  `String.format("  %4s  %-8s %-12s %s", id, prio, termin, titel)`: `%4s`
  richtet rechtsbündig aus, `%-8s` linksbündig auf 8 Zeichen. Für `termin`
  setzt du `"-"` ein, wenn `faellig()` `null` ist (sonst steht dort `null`),
  und vor den Titel kommt `[ ]` bzw. `[x]`.
- Beim Beenden speichern
- `laden` und `speichern` werfen `IOException` (checked). `main` muss sie also
  fangen und eine verständliche Meldung ausgeben, oder `main` bekommt
  `throws IOException`. Entscheide dich; ein Stacktrace ist keine Meldung
  für Benutzer.
- Umlaute in Eingaben: In der klassischen Windows-Konsole (cmd, PowerShell)
  kommen Zeichen wie `ä` bei `Scanner` oft kaputt an, weil Konsole und Java
  verschiedene Zeichensätze benutzen. Abhilfe: das Terminal in IntelliJ oder
  VS Code nehmen oder vor dem Start `chcp 65001` eingeben.

### M6: Ausbau (freiwillig)

- `--datei pfad` als Kommandozeilenargument (`String[] args` in `main`)
- Unteraufgaben, dann wird das Datenmodell rekursiv
- Sortierrichtung umschaltbar
- Farbige Ausgabe für überfällige Aufgaben (ANSI-Codes wie in `lib/Pruef.java`)
- Statt CSV: JSON von Hand schreiben und lesen
- JUnit-Tests für `Aufgabenliste` und `CsvSpeicher`
  (Kapitel 12.12, Vorlage `kapitel/12-nebenlaeufigkeit-und-werkzeuge/werkzeuge/pom.xml.vorlage`)
- Rückgängig: jede Änderung als Befehl-Objekt, ein Verlauf mit zwei Stapeln
  (Kapitel 14.10)
- Speicherformat austauschbar: ein Interface `Speicher` mit CSV- und
  JSON-Variante, die Auswahl trifft eine Fabrik (Kapitel 6.9, 9.8)

---

## Vorgeschlagene Struktur

```
projekt/
├── starte.sh                   (Windows: starte.cmd)
├── aufgaben.csv                (entsteht beim ersten Speichern)
└── src/
    ├── Main.java               Einstiegspunkt
    ├── Prioritaet.java         enum
    ├── Aufgabe.java            record
    ├── Aufgabenliste.java      Verwaltung
    ├── AufgabeNichtGefundenException.java
    ├── CsvSpeicher.java        Laden und Speichern
    └── Konsole.java            Menue und Ausgabe
```

Starten:

Aus dem Kursordner:

```bash
cd projekt && ./starte.sh
```

Unter Windows (PowerShell oder cmd): `cd projekt`, dann `.\starte`. In der
IDE: nur `projekt/src` als Quellordner markieren und `Main` starten, siehe
[`SETUP.md`](../SETUP.md#das-abschlussprojekt).

In `src/` liegen bereits drei Startdateien:

- `Main.java`: ein Gerüst, das schon jetzt kompiliert und startet und nur
  einen Hinweis ausgibt. Es verweist bewusst noch auf keine Klasse aus M2 bis M5
  (die stehen nur im Kommentar), damit `starte.sh` von Anfang an läuft.
  Den Kommentar ersetzt du nach und nach durch echten Code.
- `Prioritaet.java`: fertig.
- `Aufgabe.java`: das Gerüst für M1: die Komponenten stehen, der kompakte
  Konstruktor und die beiden Methoden sind noch `TODO` (die Methoden werfen
  bis dahin `UnsupportedOperationException`).

---

## Woran du merkst, dass es gut ist

- **Jede Klasse hat einen Job.** `Konsole` rechnet nicht, `Aufgabenliste` gibt
  nichts aus, `CsvSpeicher` kennt kein Menü. Wenn du in `Aufgabenliste` ein
  `System.out.println` schreibst, ist etwas verrutscht.
- **Keine `public`-Felder.** Zustand ändert sich nur über Methoden.
- **Kein `null` als Rückgabewert**: `Optional` oder eine leere Liste.
  Die eine bewusste Ausnahme ist `faellig()`: Die Record-Komponente darf `null`
  sein (= kein Termin). Wer das vermeiden will, ergänzt eine Methode
  `Optional<LocalDate> termin()` und benutzt nur noch die.
- **Fehlermeldungen nennen den Kontext.** "Aufgabe 42 existiert nicht" statt
  "Fehler".
- **Du kannst `Konsole` austauschen**, ohne den Rest anzufassen. Das ist der
  eigentliche Test für die Trennung der Zuständigkeiten.

## Wenn du steckenbleibst

Sieh nicht in eine Musterlösung (es gibt hier absichtlich keine), sondern
zurück ins passende Kapitel. Und bau kleiner: Ein Programm, das nur Aufgaben
anlegen und anzeigen kann, aber sauber, ist mehr wert als sechs halbfertige
Funktionen.
