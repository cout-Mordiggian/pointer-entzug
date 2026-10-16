import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
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

    private static final Path BASE = basis();

    public static void main(String[] args) throws Exception {
        String erstes = args.length > 0 ? args[0] : "";
        switch (erstes) {
            case "", "-h", "--help", "hilfe" -> uebersicht();
            case "alle" -> {
                boolean loesung = args.length > 1 && (args[1].equals("-l") || args[1].equals("--loesung"));
                boolean fehler = false;
                for (Path d : kapitel()) {
                    String nr = nummer(d);
                    if (!pruefe(nr, loesung, "Tests")) fehler = true;
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
                        case "-r", "--run" -> klasse = ++i < args.length ? args[i] : "Tests";
                        default -> { }
                    }
                }
                System.exit(pruefe(erstes, loesung, klasse) ? 0 : 1);
            }
        }
    }

    private static void uebersicht() throws IOException {
        System.out.println();
        System.out.println("  Java von Grund auf - Kapiteluebersicht");
        System.out.println("  ======================================");
        for (Path d : kapitel()) {
            String name = d.getFileName().toString();
            String titel = name;
            Path readme = d.resolve("README.md");
            if (Files.exists(readme)) {
                try (Stream<String> zeilen = Files.lines(readme)) {
                    titel = zeilen.findFirst()
                            .map(z -> z.replaceFirst("^#\\s*", "").replaceFirst("^Kapitel [0-9]*: ", ""))
                            .filter(z -> !z.isBlank())
                            .orElse(name);
                }
            }
            System.out.printf("   %s  %s%n", nummer(d), titel);
        }
        System.out.println();
        System.out.println("  Start:  java Lerne.java 01        Loesung: java Lerne.java 01 -l");
        System.out.println();
    }

    private static boolean pruefe(String nr, boolean loesung, String hauptklasse) throws Exception {
        Path dir = kapitelPfad(nr);
        if (dir == null) {
            System.err.println("Kein Kapitel '" + nr + "' gefunden. 'java Lerne.java' zeigt alle Kapitel.");
            return false;
        }
        Path quelle = dir.resolve(loesung ? "loesungen" : "src");
        Path out = BASE.resolve("build").resolve(loesung ? nr + "-loesung" : nr);

        loesche(out);
        Files.createDirectories(out);
        System.out.println();
        System.out.println("  == " + dir.getFileName() + "  [" + (loesung ? "loesung" : "aufgabe") + "] ==");

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
        if (javac.run(null, null, null, javacArgs.toArray(String[]::new)) != 0) {
            System.out.println();
            System.out.println("  Kompilierfehler - lies die Meldung von oben nach unten, der erste Fehler zaehlt.");
            return false;
        }

        // Eigener Prozess: die Tests beenden sich mit System.exit, Eingaben (Scanner) gehen durch
        String java = Path.of(System.getProperty("java.home"), "bin", "java").toString();
        Process p = new ProcessBuilder(java, "-cp", out.toString(), hauptklasse).inheritIO().start();
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

    private static Path kapitelPfad(String nr) throws IOException {
        return kapitel().stream()
                .filter(d -> d.getFileName().toString().startsWith(nr + "-"))
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
            return s.filter(p -> p.toString().endsWith(".java")).map(Path::toString).toList();
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
