# Die häufigsten Fehlermeldungen, und was sie wirklich bedeuten

Der Kurs-Runner (`./lerne.sh`) lässt den Compiler immer englisch melden. Beim
Kompilieren von Hand und in der IDE kommen die Meldungen auf einem deutschen
System dagegen oft auf Deutsch. Deshalb steht bei jedem Compilerfehler
die deutsche Fassung dabei (gesucht wird am besten nach einem Stück der
Meldung, ohne Datei- und Variablennamen).

## Compilerfehler (das Programm startet gar nicht)

### `cannot find symbol`
Deutsch: `Symbol nicht gefunden`
```
symbol:   variable breit
location: class Aufgaben
```
Der Compiler kennt diesen Namen nicht. Ursachen: Tippfehler,
Groß-/Kleinschreibung, Variable außerhalb ihres Blocks benutzt,
fehlender `import`, Methode existiert nicht (oder heißt anders).

### `incompatible types: String cannot be converted to int`
Deutsch: `Inkompatible Typen: String kann nicht in int konvertiert werden`

Zuweisung zwischen unpassenden Typen. Bei Zahlen hilft oft ein Cast, bei
Text `Integer.parseInt(...)`.

### `incompatible types: possible lossy conversion from double to int`
Deutsch: `Inkompatible Typen: Möglicher Verlust bei Konvertierung von double in int`

Einengende Umwandlung. Wenn du sie wirklich willst: `(int) wert`.
Zum Runden statt Abschneiden: `Math.round(...)`.

### `missing return statement`
Deutsch: `Rückgabeanweisung fehlt`

Nicht jeder Pfad durch die Methode endet mit `return`. Meist fehlt der
`else`-Zweig oder ein `return` nach einer Schleife.

### `variable x might not have been initialized`
Deutsch: `Variable x wurde möglicherweise nicht initialisiert`

Lokale Variablen haben, anders als Felder, keinen Standardwert.

### `unreported exception IOException; must be caught or declared to be thrown`
Deutsch: `Nicht gemeldete Ausnahme IOException. Muss abgefangen oder deklariert werden, um ausgelöst zu werden`

Eine checked Exception (Kapitel 7). Entweder `try`/`catch` oder
`throws IOException` an die Methode.

### `exception IOException has already been caught`
Deutsch: `Ausnahme IOException wurde bereits abgefangen`

Ein `catch` für eine Unterklasse steht **hinter** dem `catch` für ihre
Oberklasse (z. B. `catch (Exception e)` vor `catch (IOException e)`) und
kann nie greifen. Spezifische Typen zuerst.

### `non-static variable x cannot be referenced from a static context`
Deutsch: `Variable x ist nicht statisch und kann nicht aus einem statischen Kontext referenziert werden`

Du greifst aus einer `static`-Methode (oft `main`) auf ein Instanzfeld zu.
Entweder ein Objekt erzeugen oder das Feld `static` machen.

### `constructor Konto in class Konto cannot be applied to given types;`
Deutsch: `Konstruktor Konto in Klasse Konto kann nicht auf die angegebenen Typen angewendet werden.`

Die Zahl der Argumente passt zu keinem Konstruktor. Häufig nach dem
Hinzufügen eines eigenen Konstruktors: Der parameterlose Standardkonstruktor
ist damit verschwunden. Gibt es nur einen Konstruktor und stimmt bloß ein
Typ nicht, meldet javac stattdessen kürzer
`incompatible types: int cannot be converted to String` (dazu der Hinweis
`Some messages have been simplified`).

### `class X is public, should be declared in a file named X.java`
Deutsch: `Klasse X ist öffentlich und muss in einer Datei namens X.java deklariert werden`

Dateiname und Name der `public`-Klasse müssen übereinstimmen.

### `does not override or implement a method from a supertype`
Deutsch: `überschreibt oder implementiert keine Methode aus einem Supertyp`

Davor steht je nach Java-Version `method` oder der Methodenname
(`tostring() in Konto ...`). `@Override` steht über einer Methode, die
nichts überschreibt. Fast immer ein Tippfehler oder eine falsche
Parameterliste, genau dafür ist die Annotation da.

### `unreachable statement`
Deutsch: `Nicht erreichbare Anweisung`

Code direkt nach `return`, `break`, `continue` oder `throw` im selben Block.

### `local variables referenced from a lambda expression must be final or effectively final`
Deutsch: `Von Lambda-Ausdruck referenzierte lokale Variablen müssen final oder effektiv final sein`

Das Lambda benutzt eine lokale Variable, die sich später noch ändert.
Kopiere sie vorher in eine neue Variable.

### `';' expected` / `class, interface, enum, or record expected`
Deutsch: `';' erwartet` / `class, interface, enum oder record erwartet`

In neueren JDKs (z. B. Java 27) lautet die zweite Meldung
`class, interface, annotation type, enum, record, method or field expected`
(deutsch: `Klasse, Schnittstelle, Annotationstyp, Enumeration, Datensatz, Methode oder Feld erwartet`).

Ein Semikolon fehlt, der Compiler meldet das oft erst in der **nächsten**
Zeile. Bei `class ... expected` ist meist eine `}` zu viel (oder zu wenig)
und Code steht außerhalb der Klasse. Die Einrückung deines Editors verrät,
wo die Klammern nicht mehr stimmen.

### `the switch expression does not cover all possible input values`
Deutsch: `Der Switch-Ausdruck deckt nicht alle möglichen Eingabewerte ab`

Ein `switch`-Ausdruck über ein Enum oder einen `sealed`-Typ vergisst einen
Fall. Fall ergänzen, genau diese Meldung ist der Sinn von `sealed`.

## Laufzeitfehler (das Programm läuft und bricht ab)

Die Texte der Exceptions sind immer englisch.

### `NullPointerException: Cannot invoke "String.length()" because "name" is null`
Methodenaufruf auf `null`. Seit Java 15 nennt die Meldung die genaue
Referenz (in Java 14 nur mit einem Schalter), lies sie, sie sagt dir direkt,
welche Variable leer war.
(Steht dort `"<local1>"`, wurde ohne `javac -g` kompiliert.)

### `Error: Could not find or load main class Aufgaben`
Deutsch: `Fehler: Hauptklasse Aufgaben konnte nicht gefunden oder geladen werden`

`java` findet die Klasse nicht. Meist: `java Aufgaben.class` statt
`java Aufgaben`, falscher `-cp`, oder die Klasse liegt in einem Paket und
muss mit vollem Namen (`java de.firma.Start`) gestartet werden.

### `ArrayIndexOutOfBoundsException: Index 5 out of bounds for length 5`
Gültige Indizes gehen von `0` bis `length - 1`. Klassisch: `<=` statt `<`
in der Schleifenbedingung.

### `StringIndexOutOfBoundsException`
Dasselbe bei `charAt` oder `substring`. Beachte: `substring(von, bis)` hat
`bis` **exklusiv**.

### `NumberFormatException: For input string: "abc"`
`Integer.parseInt` auf etwas, das keine Zahl ist. Auch Leerzeichen zählen,
`strip()` hilft.

### `ArithmeticException: / by zero`
Nur bei **Ganzzahl**division (und `%`). `1.0 / 0` ergibt `Infinity` ohne Exception.

### `ClassCastException: class A cannot be cast to class B`
Ein Downcast auf einen Typ, den das Objekt nicht hat. Vorher mit
`instanceof` prüfen.

### `ConcurrentModificationException`
Die Collection wurde während einer `for-each`-Schleife verändert.
Nimm `removeIf` oder einen expliziten `Iterator`.

### `StackOverflowError`
Rekursion ohne (erreichbaren) Basisfall. Sieh dir die sich wiederholenden
Zeilen im Stacktrace an.

### `OutOfMemoryError: Java heap space`
Zu viele Objekte gleichzeitig im Speicher. Bei Dateien: `Files.lines` statt
`readAllLines`.

### `UnsupportedOperationException`
Änderung an einer unveränderlichen Collection (`List.of(...)`,
`stream.toList()`, `Collections.unmodifiableList(...)`), oder `add`/`remove`
auf einer `Arrays.asList(...)`-Liste (die hat feste Größe).
Kopiere sie: `new ArrayList<>(liste)`.

### `IllegalStateException: Duplicate key Anna`
`Collectors.toMap` hat einen Schlüssel zweimal gesehen. Dritter Parameter
(Merge-Funktion) angeben: `toMap(k, v, (alt, neu) -> alt)`.

### `IllegalStateException: stream has already been operated upon or closed`
Ein Stream wurde zweimal verwendet. Erzeuge einen neuen.

### `MalformedInputException: Input length = 1`
Die Datei ist nicht UTF-8 kodiert, oft `ISO-8859-1`.

## Wenn gar nichts geht

```bash
# Ausfuehrlichere Fehlermeldungen (javac schlaegt das selbst vor)
javac -Xdiags:verbose datei.java

# Welche Java-Version laeuft ueberhaupt?
java -version && javac -version

# Klasse aus dem Runner-Ordner von Hand starten
java -cp build/01 Tests
```

Und die wichtigste Regel: **Immer den ERSTEN Fehler zuerst beheben.**
Alles darunter sind oft nur Folgefehler.
