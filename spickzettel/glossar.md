# Glossar

**Adapter**: Entwurfsmuster. Übersetzt eine vorhandene Schnittstelle in die
erwartete, z. B. `Arrays.asList`. Siehe 6.9 und 11.9.

**Annotation**: Metadaten am Code, z. B. `@Override`, `@FunctionalInterface`.
Manche prüft der Compiler, andere werten Bibliotheken zur Laufzeit aus.

**Autoboxing**: Automatische Umwandlung zwischen `int` und `Integer`.
Bequem, aber nicht gratis: In heißen Schleifen erzeugt es viele Objekte.

**Befehl (Command)**: Entwurfsmuster. Eine Aktion als Objekt, das man
speichern, später ausführen und rückgängig machen kann. Siehe 14.10.

**Beobachter (Observer)**: Entwurfsmuster. Ein Objekt benachrichtigt alle
angemeldeten Interessenten, ohne sie zu kennen. Siehe 9.9.

**Builder**: Entwurfsmuster. Ein Hilfsobjekt sammelt viele (oft freiwillige)
Angaben, `build()` erzeugt daraus das fertige Objekt. Siehe 10.9.

**Bytecode**: Das Zwischenformat in `.class`-Dateien. Plattformunabhängig,
wird von der JVM ausgeführt bzw. per JIT in Maschinencode übersetzt.

**Checked Exception**: Ausnahme, deren Behandlung der Compiler erzwingt.
Alles unter `Exception` außer `RuntimeException`. Siehe Kapitel 7.

**Classpath**: Die Liste der Orte, an denen die JVM Klassen sucht (`-cp`).

**Comparable / Comparator**: Natürliche Ordnung *in* der Klasse
(`compareTo`) gegen alternative Ordnungen *außerhalb*.

**Deadlock**: Zwei Threads warten gegenseitig auf Sperren, die der jeweils
andere hält. Beide stehen für immer.

**Dekorierer (Decorator)**: Entwurfsmuster. Packt ein Objekt ein, hat
dasselbe Interface und fügt Verhalten hinzu. `BufferedReader`. Siehe 6.9 und 11.9.

**Dependency Injection**: Ein Objekt bekommt seine Abhängigkeiten von außen
(meist per Konstruktor), statt sie sich selbst zu holen. Die Alternative zum Singleton. Siehe 13.8.

**Diamond Operator**: Das `<>` in `new ArrayList<>()`. Der Compiler liest die
Typargumente von der linken Seite ab.

**Dynamischer Typ**: Der tatsächliche Typ des Objekts zur Laufzeit; im
Gegensatz zum **statischen Typ** der Variablen. Entscheidet beim
Methodenaufruf (Polymorphie).

**Effektiv final**: Eine lokale Variable, die nach der Initialisierung nicht
mehr verändert wird. Nur solche darf ein Lambda benutzen.

**Entwurfsmuster (Design Pattern)**: Bewährte, benannte Lösung für ein
wiederkehrendes Entwurfsproblem. Kein fertiger Code, eine Idee. Siehe 5.10 und
[`entwurfsmuster.md`](entwurfsmuster.md).

**Enum**: Typ mit einer festen Menge benannter Werte (`ROT, GELB, GRUEN`).
Vollwertige Klasse: darf Felder und Methoden haben.

**Erasure (Typlöschung)**: Generics existieren nur zur Compile-Zeit; zur
Laufzeit ist `List<String>` einfach `List`.

**Fabrikmethode**: Siehe **Statische Fabrikmethode**. Das gleichnamige
GoF-Muster *Factory Method* ist etwas anderes: eine überschreibbare Methode,
mit der eine Unterklasse entscheidet, welches Objekt entsteht.

**Garbage Collector**: Gibt Objekte frei, die nicht mehr erreichbar sind.
Du gibst in Java nichts von Hand frei.

**Generics**: Typparameter wie in `List<String>`: Der Compiler prüft den
Elementtyp, Casts entfallen. Siehe Kapitel 8.

**Guard Clause**: Früher `return` für Sonderfälle am Methodenanfang, damit
der Hauptfall flach bleibt.

**Heap / Stack**: Objekte leben auf dem Heap, lokale Variablen und
Methodenaufrufe auf dem Stack. Zu tiefe Rekursion -> `StackOverflowError`.

**Idempotent**: Mehrfaches Ausführen hat dieselbe Wirkung wie einmaliges.
Wichtige Eigenschaft von `close()`.

**Immutable (unveränderlich)**: Der Zustand steht nach der Erzeugung fest.
`String`, `Integer`, `List.of(...)`, ein `record` mit unveränderlichen
Komponenten. Solche Objekte sind automatisch thread-sicher.

**Interface**: Vertrag über Fähigkeiten, ohne Zustand. Eine Klasse kann
beliebig viele implementieren.

**Invariante**: Eine Zusage, die für ein Objekt immer gilt
("das Guthaben ist nie negativ"). Konstruktoren stellen sie her, Methoden
erhalten sie.

**Iterator (Muster)**: Entwurfsmuster. Elemente einer Sammlung der Reihe
nach durchlaufen, ohne ihren inneren Aufbau zu kennen. In Java: `Iterable`
mit `iterator()`, dann klappt for-each auch für eigene Klassen. Siehe 14.9.

**JDK / JRE / JVM**: Entwicklungswerkzeuge / Laufzeitumgebung / die
virtuelle Maschine, die Bytecode ausführt.

**JIT**: Just-in-Time-Compiler. Übersetzt häufig durchlaufenen Bytecode
zur Laufzeit in Maschinencode. Grund dafür, dass Java "warm" schnell ist.

**jshell**: Interaktive Java-Konsole. Ausdrücke eintippen, Ergebnis sofort
sehen. Ideal zum Ausprobieren.

**Kapselung**: Interne Daten sind `private`; Änderungen laufen über
Methoden, die die Invarianten prüfen.

**Konstruktor**: Spezielle Methode ohne Rückgabetyp, die ein neues Objekt
initialisiert; heißt wie die Klasse und läuft bei `new`.

**Kovarianter Rückgabetyp**: Eine überschreibende Methode darf einen
spezielleren Typ zurückgeben als die überschriebene.

**Lambda**: Anonyme Funktion `x -> x * 2`, nutzbar überall dort, wo ein
funktionales Interface erwartet wird.

**LTS**: Long Term Support. Java 8, 11, 17, 21, 25.

**Methodenreferenz**: Kurzform eines Lambdas, das nur eine Methode aufruft:
`String::length` statt `s -> s.length()`.

**NPE**: `NullPointerException`. Methodenaufruf auf einer `null`-Referenz.

**Optional**: Rückgabetyp für "vielleicht kein Wert"; ersetzt `null` als
Rückgabe. Siehe Kapitel 9.

**Overloading (Überladen)**: Gleicher Name, andere Parameter. Der Compiler
entscheidet.

**Overriding (Überschreiben)**: Unterklasse ersetzt eine geerbte Methode.
Die Laufzeit entscheidet.

**Paket (package)**: Namensraum und Sichtbarkeitsgrenze; spiegelt die
Ordnerstruktur.

**Pattern Matching**: Typ prüfen und Variable binden in einem Schritt:
`if (o instanceof String s)`, `case Kreis(double r) ->`.

**PECS**: "Producer extends, Consumer super". Merkregel für Wildcards:
`? extends T` zum Lesen, `? super T` zum Schreiben.

**Polymorphie**: Derselbe Aufruf, unterschiedliches Verhalten je nach
tatsächlichem Objekttyp.

**Race Condition**: Das Ergebnis hängt davon ab, welcher Thread zufällig
zuerst drankommt.

**Record**: (Flach) unveränderlicher Datenträger; der Compiler erzeugt
Konstruktor, Zugriffsmethoden, `equals`, `hashCode`, `toString`.

**Referenz**: Ein Verweis auf ein Objekt. Java-Variablen von Objekttypen
enthalten Referenzen, nie das Objekt selbst.

**Schablonenmethode (Template Method)**: Entwurfsmuster. Eine Methode der
Oberklasse legt den Ablauf fest (gern `final`) und ruft für einzelne Schritte
abstrakte Methoden auf, die Unterklassen füllen. Beispiel im JDK:
`AbstractList`. Siehe 6.9.

**Sealed**: Ein Typ, der nur von aufgezählten Untertypen abgeleitet werden
darf. Ermöglicht vollständigkeitsgeprüfte `switch`-Ausdrücke.

**Signatur**: Name plus Parametertypen einer Methode. **Nicht** der
Rückgabetyp.

**Singleton**: Entwurfsmuster. Von einer Klasse gibt es genau eine Instanz.
Am einfachsten als `enum` mit einer Konstante. Globaler Zustand, sparsam einsetzen.
Siehe 5.10, 10.9 und 12.3.

**Stacktrace**: Der Aufrufpfad zum Zeitpunkt einer Exception. Von oben lesen.

**Statische Fabrikmethode**: Statische Methode, die statt `new` ein
Objekt liefert: `List.of`, `Integer.valueOf`. Darf cachen und Untertypen
liefern. Kein GoF-Muster. Siehe 5.10 und 9.8.

**Strategie (Strategy)**: Entwurfsmuster. Austauschbares Verhalten hinter
einem Interface, z. B. `Comparator` für `sort`. Oft einfach ein Lambda. Siehe 6.9 und 9.8.

**Stream**: Pipeline über Daten, faul ausgewertet, einmal benutzbar.
Keine Datenstruktur.

**String-Pool**: Zwischenspeicher für String-Literale. Grund dafür, dass
`==` bei Literalen scheinbar funktioniert, und warum man sich nie darauf
verlassen darf (bei Strings aus Eingaben oder Dateien klappt es nicht).

**Terminaloperation**: Die Operation, die eine Stream-Pipeline auslöst
(`toList`, `count`, `collect`, `forEach`).

**Thread-sicher**: Aus mehreren Threads gleichzeitig nutzbar, ohne dass
Daten kaputtgehen.

**Unboxing**: Gegenstück zum Autoboxing (`Integer` -> `int`). Ist das
`Integer` `null`, gibt es eine `NullPointerException`.

**Unchecked Exception**: `RuntimeException` und Unterklassen. Der Compiler
verlangt keine Behandlung; sie signalisieren Programmierfehler.

**Varargs**: `int... zahlen`: beliebig viele Argumente, innen ein Array.

**volatile**: Feld-Modifier. Änderungen werden sofort für alle Threads
sichtbar. Macht `x++` aber nicht atomar.

**Wildcard**: Das `?` in `List<?>` oder `List<? extends Number>`: ein
unbekanntes Typargument, unbegrenzt oder nach oben (`extends`) bzw. unten
(`super`) begrenzt. Siehe Kapitel 8 und **PECS**.

**Wrapper-Klasse**: Objektfassung eines primitiven Typs: `Integer`,
`Double`, `Boolean`, `Character`.
