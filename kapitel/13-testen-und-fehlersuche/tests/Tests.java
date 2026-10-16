import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
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

        // Jeder Mutant laeuft einmal, die korrekte Implementierung (Index 0)
        // zweimal, und zwar in zufaelliger Reihenfolge. Ehrliche Tests ohne
        // Zustand zwischen den Aufrufen liefern trotzdem jedes Mal dasselbe
        // Ergebnis. Wer stattdessen Aufrufe mitzaehlt, faellt auf.
        int[] reihenfolge = new int[kandidaten.length + 1];
        for (int i = 0; i < kandidaten.length; i++) reihenfolge[i] = i;
        reihenfolge[kandidaten.length] = 0;
        mischen(reihenfolge, new Random());

        boolean[] ergebnis = new boolean[kandidaten.length];
        String[] meldungen = new String[kandidaten.length];
        boolean referenzSchonGelaufen = false;
        boolean referenzZweiterLauf = false;
        String meldungenZweiterLauf = "";
        for (int k : reihenfolge) {
            ByteArrayOutputStream puffer = new ByteArrayOutputStream();
            boolean bestanden = mitgeschnitten(kandidaten[k], puffer);
            String text = puffer.toString(StandardCharsets.UTF_8);
            if (k == 0 && referenzSchonGelaufen) {
                referenzZweiterLauf = bestanden;
                meldungenZweiterLauf = text;
            } else {
                ergebnis[k] = bestanden;
                meldungen[k] = text;
                if (k == 0) referenzSchonGelaufen = true;
            }
        }

        // Ausgabe in fester Reihenfolge, egal in welcher gerechnet wurde.
        Pruef.abschnitt("Teil A: Deine Tests gegen die korrekte Implementierung");
        // Verglichen wird der Rueckgabewert von alleBestanden: true = alles bestanden.
        System.out.print(meldungen[0]);
        Pruef.gleich(true, ergebnis[0], "Deine Tests bestehen mit der korrekten Implementierung");
        if (ergebnis[0] != referenzZweiterLauf) {
            System.out.print(meldungenZweiterLauf);
        }
        Pruef.gleich(ergebnis[0], referenzZweiterLauf,
                "Zweiter Durchlauf mit der korrekten Implementierung: dasselbe Ergebnis"
                + " (deine Tests merken sich nichts zwischen zwei Aufrufen)");

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
     */
    private static boolean mitgeschnitten(Rabattrechner r, ByteArrayOutputStream puffer) {
        PrintStream original = System.out;
        System.setOut(new PrintStream(puffer, true, StandardCharsets.UTF_8));
        Kandidaten.freigeben(r);
        try {
            return laeuftDurch(r);
        } finally {
            Kandidaten.freigeben(null);
            System.setOut(original);
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
