import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

/**
 * Lern-Runner fuer den Java-Kurs. Laeuft ueberall, wo ein JDK installiert ist
 * (Windows, macOS, Linux), ohne Bash:
 *
 *   java Lerne.java              Kapiteluebersicht
 *   java Lerne.java 03           Kapitel 03 kompilieren und pruefen
 *   java Lerne.java 03 -l        gegen die Musterloesung pruefen
 *   java Lerne.java alle         alle Kapitel pruefen
 *   java Lerne.java 03 -r Demo   Klasse "Demo" aus dem Kapitel ausfuehren
 *
 * Kurzformen: ./lerne.sh 03 (Bash) und .\lerne 03 (Windows) rufen diese Datei auf.
 */
public class Lerne {

    private static final int MIN_JAVA = 21;
    private static final Path BASE = basis();
    // Die Wrapper setzen den Namen, unter dem der Runner aufgerufen wurde
    private static final String AUFRUF = System.getProperty("lerne.aufruf", "java Lerne.java");
    private static final Pattern MAIN = Pattern.compile("\\bvoid\\s+main\\s*\\(");

    public static void main(String[] args) throws Exception {
        if (Runtime.version().feature() < MIN_JAVA) {
            System.err.println("Dieser Kurs braucht Java " + MIN_JAVA + " oder neuer, gefunden: Java "
                    + Runtime.version() + " (" + System.getProperty("java.home") + ").");
            System.err.println("Installiere ein aktuelles JDK oder stelle es im PATH nach vorne, siehe SETUP.md.");
            System.exit(1);
        }
        String erstes = args.length > 0 ? args[0] : "";
        switch (erstes) {
            case "", "-h", "--help", "hilfe" -> uebersicht();
            case "alle" -> {
                boolean loesung = false;
                for (int i = 1; i < args.length; i++) {
                    if (args[i].equals("-l") || args[i].equals("--loesung")) loesung = true;
                    else warne(args[i]);
                }
                boolean fehler = false;
                for (Path d : kapitel()) {
                    if (!pruefe(d, loesung, "Tests")) fehler = true;
                }
                System.out.println();
                System.out.println(fehler ? "  Mindestens ein Kapitel ist noch offen." : "  Alle Kapitel bestanden.");
                System.exit(fehler ? 1 : 0);
            }
            default -> {
                boolean loesung = false;
                String klasse = "Tests";
                for (int i = 1; i < args.length; i++) {
                    switch (args[i]) {
                        case "-l", "--loesung" -> loesung = true;
                        case "-r", "--run" -> {
                            if (i + 1 < args.length && !args[i + 1].startsWith("-")) {
                                klasse = args[++i];
                            } else {
                                System.err.println("  Hinweis: Nach -r fehlt der Klassenname, ich starte Tests."
                                        + " Beispiel: " + AUFRUF + " 01 -r Spielwiese");
                            }
                        }
                        default -> warne(args[i]);
                    }
                }
                Path dir = kapitelPfad(erstes);
                if (dir == null) {
                    System.err.println("Kein Kapitel '" + erstes + "' gefunden. '" + AUFRUF + "' zeigt alle Kapitel.");
                    System.exit(1);
                }
                System.exit(pruefe(dir, loesung, klasse) ? 0 : 1);
            }
        }
    }

    private static void warne(String option) {
        System.err.println("  Hinweis: Unbekannte Angabe '" + option + "' wird ignoriert. '"
                + AUFRUF + " -h' zeigt alle Optionen.");
    }

    private static void uebersicht() throws IOException {
        System.out.println();
        System.out.println("  Java von Grund auf: Kapiteluebersicht");
        System.out.println("  =====================================");
        for (Path d : kapitel()) {
            System.out.printf("   %s  %s%n", nummer(d), titel(d));
        }
        System.out.println();
        System.out.println("  " + AUFRUF + " 01             Kapitel 01 pruefen (auch: 1)");
        System.out.println("  " + AUFRUF + " 01 -l          gegen die Musterloesung pruefen");
        System.out.println("  " + AUFRUF + " 01 -r Klasse   eigene Klasse mit main aus dem Kapitel starten");
        System.out.println("  " + AUFRUF + " alle           alle Kapitel pruefen (alle -l: alle Loesungen)");
        System.out.println();
    }

    /** Erste README-Zeile ohne "# Kapitel NN: ". Kaputte Kodierung oder BOM fuehren nicht zum Abbruch. */
    private static String titel(Path kapitelOrdner) {
        String name = kapitelOrdner.getFileName().toString();
        try {
            // new String(...) ersetzt ungueltige Bytes, statt eine Exception zu werfen
            String text = new String(Files.readAllBytes(kapitelOrdner.resolve("README.md")), StandardCharsets.UTF_8);
            String zeile = text.lines().findFirst().orElse("")
                    .replace(String.valueOf((char) 0xFEFF), "")
                    .replaceFirst("^#\\s*", "")
                    .replaceFirst("^Kapitel [0-9]*: ", "")
                    .strip();
            return zeile.isEmpty() ? name : zeile;
        } catch (IOException e) {
            return name;
        }
    }

    private static boolean pruefe(Path dir, boolean loesung, String hauptklasse) throws Exception {
        String nr = nummer(dir);
        Path quelle = dir.resolve(loesung ? "loesungen" : "src");
        Path out = BASE.resolve("build").resolve(loesung ? nr + "-loesung" : nr);

        System.out.println();
        System.out.println("  == " + dir.getFileName() + "  [" + (loesung ? "loesung" : "aufgabe") + "] ==");
        try {
            loesche(out);
            Files.createDirectories(out);
        } catch (IOException e) {
            System.out.println("  Der Ordner " + BASE.relativize(out) + " laesst sich nicht neu anlegen ("
                    + e.getClass().getSimpleName() + ": " + e.getMessage() + ").");
            System.out.println("  Vermutlich haelt ein anderes Programm eine Datei darin fest (laufendes Programm,");
            System.out.println("  Explorer, Virenscanner, OneDrive) oder der Kursordner ist schreibgeschuetzt.");
            System.out.println("  Schliesse das Programm oder loesche den Ordner build von Hand und starte erneut.");
            return false;
        }

        JavaCompiler javac = ToolProvider.getSystemJavaCompiler();
        if (javac == null) {
            System.err.println("  Kein Compiler gefunden: Du brauchst ein JDK, kein JRE (siehe SETUP.md).");
            return false;
        }
        // -g: Debug-Infos, damit NullPointerExceptions echte Variablennamen nennen
        List<String> javacArgs = new ArrayList<>(List.of(
                "-g", "-Xlint:-serial", "-encoding", "UTF-8", "-d", out.toString()));
        for (Path p : List.of(BASE.resolve("lib"), quelle, dir.resolve("tests"))) {
            javacArgs.addAll(javaDateien(p));
        }
        // Englische Meldungen wie im Spickzettel. Gilt nur hier, nicht fuer das gestartete Programm.
        Locale.setDefault(Locale.ROOT);
        if (javac.run(null, null, null, javacArgs.toArray(String[]::new)) != 0) {
            System.out.println();
            System.out.println("  Kompilierfehler: Lies die Meldung von oben nach unten, der erste Fehler zaehlt.");
            return false;
        }

        String klasse = hauptklasse.replaceFirst("\\.(java|class)$", "");
        if (!Files.isRegularFile(out.resolve(klasse.replace('.', '/') + ".class"))) {
            List<String> kandidaten = new ArrayList<>();
            for (Path p : List.of(quelle, dir.resolve("tests"))) {
                for (String datei : javaDateien(p)) {
                    if (MAIN.matcher(Files.readString(Path.of(datei), StandardCharsets.UTF_8)).find()) {
                        kandidaten.add(Path.of(datei).getFileName().toString().replaceFirst("\\.java$", ""));
                    }
                }
            }
            System.out.println("  Keine Klasse '" + klasse + "' in " + BASE.relativize(quelle) + " oder tests.");
            System.out.println("  Klassen mit main: " + (kandidaten.isEmpty() ? "keine" : String.join(", ", kandidaten)));
            return false;
        }

        // Eigener Prozess: die Tests beenden sich mit System.exit, Eingaben (Scanner) gehen durch
        String java = Path.of(System.getProperty("java.home"), "bin", "java").toString();
        Process p = new ProcessBuilder(java, "-cp", out.toString(), klasse).inheritIO().start();
        return p.waitFor() == 0;
    }

    private static Path basis() {
        try {
            // Beim Start als Quelldatei (java Lerne.java) ist die Code-Quelle die Datei selbst
            Path hier = Path.of(Lerne.class.getProtectionDomain().getCodeSource().getLocation().toURI());
            if (Files.isRegularFile(hier)) hier = hier.getParent();
            hier = hier.toAbsolutePath().normalize();
            if (Files.isDirectory(hier.resolve("kapitel"))) return hier;
        } catch (Exception e) {
            // weiter mit dem Arbeitsverzeichnis
        }
        return Path.of("").toAbsolutePath();
    }

    private static List<Path> kapitel() throws IOException {
        Path k = BASE.resolve("kapitel");
        if (!Files.isDirectory(k)) {
            System.err.println("Ordner 'kapitel' nicht gefunden. Starte den Runner im Kursordner.");
            System.exit(1);
        }
        try (Stream<Path> s = Files.list(k)) {
            return s.filter(Files::isDirectory).sorted().toList();
        }
    }

    /** Akzeptiert "03", "3", "03-strings-und-arrays" und "kapitel/03-strings-und-arrays/". */
    private static Path kapitelPfad(String angabe) throws IOException {
        String nr = angabe.replaceFirst("[/\\\\]+$", "").replaceFirst("^.*[/\\\\]", "");
        if (nr.matches("[0-9]")) nr = "0" + nr;
        String gesucht = nr;
        return kapitel().stream()
                .filter(d -> {
                    String name = d.getFileName().toString();
                    return name.equals(gesucht) || name.startsWith(gesucht + "-");
                })
                .findFirst().orElse(null);
    }

    private static String nummer(Path kapitelOrdner) {
        String name = kapitelOrdner.getFileName().toString();
        int strich = name.indexOf('-');
        return strich < 0 ? name : name.substring(0, strich);
    }

    private static List<String> javaDateien(Path ordner) throws IOException {
        if (!Files.isDirectory(ordner)) return List.of();
        try (Stream<Path> s = Files.walk(ordner)) {
            return s.filter(p -> p.toString().endsWith(".java") && Files.isRegularFile(p))
                    .map(Path::toString).toList();
        }
    }

    private static void loesche(Path ordner) throws IOException {
        if (!Files.exists(ordner)) return;
        try (Stream<Path> s = Files.walk(ordner)) {
            for (Path p : s.sorted(Comparator.reverseOrder()).toList()) {
                Files.delete(p);
            }
        }
    }
}
