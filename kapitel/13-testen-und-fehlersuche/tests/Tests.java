import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.management.ManagementFactory;
import java.nio.charset.StandardCharsets;
import java.util.Random;

public class Tests {

    /**
     * Ein Stueck Code, das einen Wert liefert. In den Pruefungen unten steht
     * dafuer ein Lambda wie () -> Fehlerhaft.wochentag(5): "der Aufruf, den
     * wir gleich ausfuehren wollen". So kann pruefe() den Aufruf in ein
     * try/catch packen.
     */
    interface Aufruf {
        Object ausfuehren();
    }

    public static void main(String[] args) {

        // ------------------------------------------------------------ Teil A
        Rabattrechner[] kandidaten = {
            new Kandidaten.Referenz(),
            new Kandidaten.Mutant1(), new Kandidaten.Mutant2(), new Kandidaten.Mutant3(),
            new Kandidaten.Mutant4(), new Kandidaten.Mutant5(), new Kandidaten.Mutant6(),
            new Kandidaten.Mutant7(), new Kandidaten.Mutant8(), new Kandidaten.Mutant9()
        };

        // Alle Kandidaten (Index 0 ist die korrekte Implementierung) laufen
        // einmal in zufaelliger Reihenfolge, danach die korrekte noch einmal
        // ganz am Schluss. Ehrliche Tests ohne Zustand liefern dabei immer
        // dasselbe. Wer Aufrufe mitzaehlt oder sich Fehler aus frueheren
        // Aufrufen merkt, faellt spaetestens beim letzten Durchlauf auf.
        int[] reihenfolge = new int[kandidaten.length];
        for (int i = 0; i < kandidaten.length; i++) reihenfolge[i] = i;
        mischen(reihenfolge, new Random());

        Pruef.abschnitt("Teil A: Deine Tests laufen (elf Durchlaeufe)");
        System.out.println("  Deine Ausgaben erscheinen gesammelt, sobald alle Durchlaeufe fertig sind.");

        boolean[] ergebnis = new boolean[kandidaten.length];
        String[] meldungen = new String[kandidaten.length];
        for (int k : reihenfolge) {
            ByteArrayOutputStream puffer = new ByteArrayOutputStream();
            ergebnis[k] = mitgeschnitten(kandidaten[k], puffer);
            meldungen[k] = puffer.toString(StandardCharsets.UTF_8);
        }
        ByteArrayOutputStream pufferZumSchluss = new ByteArrayOutputStream();
        boolean zumSchluss = mitgeschnitten(kandidaten[0], pufferZumSchluss);
        String meldungenZumSchluss = pufferZumSchluss.toString(StandardCharsets.UTF_8);

        // Ausgabe in fester Reihenfolge, egal in welcher gerechnet wurde.
        Pruef.abschnitt("Teil A: Deine Tests gegen die korrekte Implementierung");
        // Verglichen wird der Rueckgabewert von alleBestanden: true = alles bestanden.
        System.out.print(meldungen[0]);
        Pruef.gleich(true, ergebnis[0], "Deine Tests bestehen mit der korrekten Implementierung");
        hinweisZustand(ergebnis[0], meldungen[0]);
        System.out.print(meldungenZumSchluss);
        Pruef.gleich(true, zumSchluss, "Auch zum Schluss, nach allen Mutanten, noch einmal bestanden");
        if (ergebnis[0] && !zumSchluss) {
            System.out.println("       Hinweis: Erst bestanden, nach den Mutanten nicht mehr. Deine Methode");
            System.out.println("       merkt sich etwas zwischen zwei Aufrufen, etwa ein static-Feld,");
            System.out.println("       das am Anfang von alleBestanden nicht zurueckgesetzt wird.");
        } else {
            hinweisZustand(zumSchluss, meldungenZumSchluss);
        }

        Pruef.abschnitt("Teil A: Deine Tests gegen die Mutanten");
        for (int i = 1; i < kandidaten.length; i++) {
            // Hier muss alleBestanden false liefern: Deine Tests haben den Fehler bemerkt.
            System.out.print(meldungen[i]);
            Pruef.gleich(false, ergebnis[i], "Deine Tests entlarven Mutant " + i);
        }

        // ------------------------------------------------------------ Teil B
        Pruef.abschnitt("Teil B: durchschnitt");
        pruefe(1.5, () -> Fehlerhaft.durchschnitt(new int[]{1, 2}), "{1, 2} -> 1.5");
        pruefe(4.0, () -> Fehlerhaft.durchschnitt(new int[]{2, 4, 6}), "{2, 4, 6} -> 4.0");
        pruefe(5.0 / 3, () -> Fehlerhaft.durchschnitt(new int[]{1, 2, 2}), "{1, 2, 2} -> 1.666...");
        pruefe(-2.5, () -> Fehlerhaft.durchschnitt(new int[]{-2, -3}), "negative Zahlen");

        Pruef.abschnitt("Teil B: zaehleGroesser");
        pruefe(2, () -> Fehlerhaft.zaehleGroesser(new int[]{1, 5, 3, 8}, 4), "{1, 5, 3, 8} groesser 4");
        pruefe(1, () -> Fehlerhaft.zaehleGroesser(new int[]{10, 20}, 10), "gleich der Grenze zaehlt nicht");
        pruefe(1, () -> Fehlerhaft.zaehleGroesser(new int[]{1, 2, 9}, 5), "letztes Element zaehlt mit");
        pruefe(0, () -> Fehlerhaft.zaehleGroesser(new int[]{}, 0), "leeres Array");

        Pruef.abschnitt("Teil B: istAdmin");
        pruefe(true, () -> Fehlerhaft.istAdmin("admin"), "Literal \"admin\"");
        pruefe(true, () -> Fehlerhaft.istAdmin(new String("admin")), "zur Laufzeit erzeugt: new String(\"admin\")");
        pruefe(true, () -> Fehlerhaft.istAdmin("xadmin".substring(1)), "zur Laufzeit erzeugt: substring");
        pruefe(false, () -> Fehlerhaft.istAdmin("Admin"), "Grossschreibung zaehlt");
        pruefe(false, () -> Fehlerhaft.istAdmin(null), "null -> false");

        Pruef.abschnitt("Teil B: wochentag");
        pruefe("Montag", () -> Fehlerhaft.wochentag(1), "1 -> Montag");
        pruefe("Donnerstag", () -> Fehlerhaft.wochentag(4), "4 -> Donnerstag");
        pruefe("Freitag", () -> Fehlerhaft.wochentag(5), "5 -> Freitag");
        pruefe("Samstag", () -> Fehlerhaft.wochentag(6), "6 -> Samstag");
        pruefe("Sonntag", () -> Fehlerhaft.wochentag(7), "7 -> Sonntag");
        pruefe("ungueltig", () -> Fehlerhaft.wochentag(0), "0 -> ungueltig");
        pruefe("ungueltig", () -> Fehlerhaft.wochentag(8), "8 -> ungueltig");

        Pruef.abschnitt("Teil B: normalisiere");
        pruefe("hallo welt", () -> Fehlerhaft.normalisiere("  Hallo Welt "), "Rand weg, klein");
        pruefe("abc", () -> Fehlerhaft.normalisiere("abc"), "schon normalisiert");
        pruefe("", () -> Fehlerhaft.normalisiere("   "), "nur Leerzeichen -> leer");

        Pruef.abschnitt("Teil B: hoechstens");
        pruefe(3, () -> Fehlerhaft.hoechstens(3, 10), "unter der Grenze: unveraendert");
        pruefe(10, () -> Fehlerhaft.hoechstens(10, 10), "genau an der Grenze");
        pruefe(10, () -> Fehlerhaft.hoechstens(15, 10), "ueber der Grenze: gekappt");
        pruefe(-7, () -> Fehlerhaft.hoechstens(-7, 0), "negativer Wert");

        Pruef.bericht();
    }

    // ------------------------------------------------------------- Helfer

    /**
     * Ruft deine Methode alleBestanden auf. Wirft sie selbst eine Exception
     * (etwa, weil ein Mutant an einer Stelle wirft, an der du keine erwartest),
     * gilt der Durchlauf als NICHT bestanden, genau wie in JUnit, wo eine
     * unerwartete Exception einen Test rot macht.
     */
    private static boolean laeuftDurch(Rabattrechner r) {
        try {
            return MeineTests.alleBestanden(r);
        } catch (RuntimeException e) {
            System.out.println("       [deine Pruefung] unerwartete Exception: " + beschreibe(e));
            return false;
        }
    }

    /**
     * Wie laeuftDurch, schreibt aber alle Ausgaben in den Puffer statt auf
     * den Bildschirm. Nur der uebergebene Kandidat darf in dieser Zeit rechnen.
     * Endet der Aufruf nicht normal (Error, System.exit, Endlosschleife),
     * wird der Puffer trotzdem ausgegeben, damit deine Meldungen nicht fehlen.
     */
    private static boolean mitgeschnitten(Rabattrechner r, ByteArrayOutputStream puffer) {
        PrintStream original = System.out;
        // Endet das Programm mitten in alleBestanden (System.exit, Strg+C),
        // gilt das als Fehlschlag.
        Thread beendet = new Thread(() -> abbruch(original, puffer,
                "Das Programm wurde mitten in alleBestanden beendet (System.exit oder Strg+C)."));
        Runtime.getRuntime().addShutdownHook(beendet);
        Thread waechter = waechter(original, puffer);
        System.setOut(new PrintStream(puffer, true, StandardCharsets.UTF_8));
        Kandidaten.freigeben(r);
        boolean normal = false;
        try {
            boolean bestanden = laeuftDurch(r);
            normal = true;
            return bestanden;
        } finally {
            Kandidaten.freigeben(null);
            System.setOut(original);
            if (waechter != null) waechter.interrupt();
            Runtime.getRuntime().removeShutdownHook(beendet);
            if (!normal) System.out.print(puffer.toString(StandardCharsets.UTF_8));
        }
    }

    /**
     * Bricht nach 10 Sekunden ab: Deine Pruefungen brauchen Millisekunden,
     * laenger dauert nur eine Endlosschleife. Im Debugger (Breakpoints) aus.
     */
    private static Thread waechter(PrintStream original, ByteArrayOutputStream puffer) {
        if (ManagementFactory.getRuntimeMXBean().getInputArguments().toString().contains("jdwp")) {
            return null;
        }
        Thread t = new Thread(() -> {
            try {
                Thread.sleep(10_000);
            } catch (InterruptedException e) {
                return;   // rechtzeitig fertig
            }
            abbruch(original, puffer,
                    "alleBestanden laeuft seit 10 Sekunden. Vermutlich eine Endlosschleife.");
        });
        t.setDaemon(true);
        t.start();
        return t;
    }

    /** Gibt den Puffer und den Grund aus und beendet sofort mit Exit-Code 1. */
    private static void abbruch(PrintStream original, ByteArrayOutputStream puffer, String grund) {
        original.print(puffer.toString(StandardCharsets.UTF_8));
        original.println();
        original.println("  ABBRUCH: " + grund);
        original.println("  Darueber stehen deine Ausgaben bis zu diesem Moment.");
        original.flush();
        Runtime.getRuntime().halt(1);
    }

    /** Hinweis, wenn alleBestanden false liefert, ohne eine Pruefung zu melden. */
    private static void hinweisZustand(boolean bestanden, String meldungen) {
        if (!bestanden && meldungen.isBlank()) {
            System.out.println("       Hinweis: alleBestanden lieferte false, ohne eine Pruefung zu melden.");
            System.out.println("       Merkt sich deine Methode etwas zwischen zwei Aufrufen, etwa ein");
            System.out.println("       static-Feld, das am Anfang nicht zurueckgesetzt wird?");
        }
    }

    /** Bringt die Eintraege in zufaellige Reihenfolge (Fisher-Yates). */
    private static void mischen(int[] a, Random zufall) {
        for (int i = a.length - 1; i > 0; i--) {
            int j = zufall.nextInt(i + 1);
            int merker = a[i];
            a[i] = a[j];
            a[j] = merker;
        }
    }

    /**
     * Fuehrt den Aufruf aus und vergleicht das Ergebnis. Wirft der Aufruf eine
     * Exception, wird sie als FEHL gemeldet, statt den ganzen Testlauf
     * abzubrechen. So siehst du auch alle uebrigen Pruefungen.
     */
    private static void pruefe(Object erwartet, Aufruf aufruf, String was) {
        Object ergebnis;
        try {
            ergebnis = aufruf.ausfuehren();
        } catch (RuntimeException e) {
            Pruef.gleich(erwartet, beschreibe(e), was);
            return;
        }
        if (erwartet instanceof Double e && ergebnis instanceof Double t) {
            Pruef.fastGleich(e, t, was);
        } else {
            Pruef.gleich(erwartet, ergebnis, was);
        }
    }

    /** "ArrayIndexOutOfBoundsException (Index 4 ...) in Fehlerhaft.java:35" */
    private static String beschreibe(RuntimeException e) {
        String text = e.getClass().getSimpleName() + " (" + e.getMessage() + ")";
        // Die erste Stacktrace-Zeile aus deinem Code heraussuchen (Abschnitt 13.6)
        for (StackTraceElement zeile : e.getStackTrace()) {
            String datei = zeile.getFileName();
            if ("Fehlerhaft.java".equals(datei) || "MeineTests.java".equals(datei)) {
                return text + " in " + datei + ":" + zeile.getLineNumber();
            }
        }
        return text;
    }
}
