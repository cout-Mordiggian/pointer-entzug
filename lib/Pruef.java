import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Ein winziges Pruef-Framework ("Mini-JUnit") fuer diesen Kurs.
 *
 * Bewusst simpel gehalten: statische Methoden, ein globaler Zaehler.
 * Ab Kapitel 12 lernst du das echte JUnit kennen. Dann wirst du erkennen,
 * dass die Ideen dieselben sind (Assertion, Report, Exit-Code).
 */
public final class Pruef {

    private Pruef() { } // Utility-Klasse: keine Instanzen erlaubt

    private static final List<String> FEHLER = new ArrayList<>();
    private static int gesamt = 0;

    // Farben abschaltbar mit der Umgebungsvariablen NO_COLOR (siehe no-color.org)
    private static final boolean FARBE = System.getenv("NO_COLOR") == null || System.getenv("NO_COLOR").isEmpty();
    private static final String ESC   = String.valueOf((char) 27);
    private static final String GRUEN = farbe("[32m");
    private static final String ROT   = farbe("[31m");
    private static final String GRAU  = farbe("[90m");
    private static final String GELB  = farbe("[33m");
    private static final String RESET = farbe("[0m");

    static {
        // Wirft dein Code eine Exception, bricht der ganze Testlauf ab. Statt eines
        // nackten Stacktraces gibt es dann eine Erklaerung und den Zwischenstand.
        Thread.setDefaultUncaughtExceptionHandler((thread, fehler) -> {
            System.out.flush();
            System.out.println();
            System.out.println(ROT + "  ABBRUCH: Dein Code hat eine Exception geworfen, die restlichen"
                    + " Pruefungen konnten nicht laufen." + RESET);
            System.out.println(GELB + "  Lies den Stacktrace unten von oben: Die erste Zeile mit einer deiner"
                    + " Dateien (z. B. Aufgaben.java:42) ist die Stelle in deinem Code." + RESET);
            System.out.println();
            fehler.printStackTrace(System.out);
            System.out.println();
            System.out.println(ROT + "  Bis zum Abbruch: " + (gesamt - FEHLER.size()) + " von " + gesamt
                    + " Pruefungen bestanden." + RESET);
            System.exit(1);
        });
    }

    /** Vergleicht zwei Werte. Arrays werden inhaltlich verglichen. */
    public static void gleich(Object erwartet, Object tatsaechlich, String was) {
        gesamt++;
        if (tiefGleich(erwartet, tatsaechlich)) {
            bestanden(was);
        } else {
            String e = zeige(erwartet);
            String t = zeige(tatsaechlich);
            String detail;
            if (e.contains("\n") || t.contains("\n")) {
                // Mehrzeilig: untereinander, Zeilenenden als \n bzw. \r\n sichtbar
                detail = "erwartet:\n  " + e.replace("\n", "\n   ") + "\nbekommen:\n  " + t.replace("\n", "\n   ");
            } else {
                detail = "erwartet: " + e + "  |  bekommen: " + t;
            }
            if (e.equals(t) && erwartet != null && tatsaechlich != null) {
                detail += "\n" + gleicheAnzeige(erwartet, tatsaechlich);
            }
            fehlgeschlagen(was, detail);
        }
    }

    /** Fuer Kommazahlen: Vergleich mit Toleranz. */
    public static void fastGleich(double erwartet, double tatsaechlich, String was) {
        gesamt++;
        double abstand = Math.abs(erwartet - tatsaechlich);
        // Gleich (auch NaN/Infinity), absolut nah, oder bei grossen Zahlen relativ nah
        // (isFinite: sonst gaelte Inf <= 1e-12 * Inf, und 4.5 gegen Infinity waere gruen)
        if (Double.compare(erwartet, tatsaechlich) == 0 || Double.isFinite(abstand) && (abstand < 1e-6
                || abstand <= 1e-12 * Math.max(Math.abs(erwartet), Math.abs(tatsaechlich)))) {
            bestanden(was);
        } else {
            fehlgeschlagen(was, "erwartet: " + erwartet + "  |  bekommen: " + tatsaechlich);
        }
    }

    public static void wahr(boolean bedingung, String was) {
        gesamt++;
        if (bedingung) {
            bestanden(was);
        } else {
            fehlgeschlagen(was, "erwartet: true  |  bekommen: false");
        }
    }

    public static void falsch(boolean bedingung, String was) {
        gesamt++;
        if (!bedingung) {
            bestanden(was);
        } else {
            fehlgeschlagen(was, "erwartet: false  |  bekommen: true");
        }
    }

    /** Prueft, dass ein Codestueck eine bestimmte Exception wirft. */
    public static void wirft(Class<? extends Throwable> typ, Runnable code, String was) {
        gesamt++;
        try {
            code.run();
            fehlgeschlagen(was, "erwartet: " + typ.getSimpleName() + " geworfen  |  bekommen: keine Exception");
        } catch (Throwable t) {
            if (typ.isInstance(t)) {
                bestanden(was);
            } else {
                fehlgeschlagen(was, "erwartet: " + typ.getSimpleName()
                        + "  |  bekommen: " + t.getClass().getSimpleName() + " (" + t.getMessage() + ")");
            }
        }
    }

    /** Schliesst die Pruefung ab, gibt eine Zusammenfassung aus und beendet das Programm. */
    public static void bericht() {
        System.out.println();
        if (FEHLER.isEmpty()) {
            System.out.println(GRUEN + "  Alle " + gesamt + " Pruefungen bestanden. Weiter zum naechsten Kapitel!" + RESET);
            System.exit(0);
        } else {
            System.out.println(ROT + "  " + FEHLER.size() + " von " + gesamt + " Pruefungen fehlgeschlagen." + RESET);
            System.out.println(GELB + "  Tipp: Nimm dir die erste rote Pruefung vor, bearbeite das TODO in src/"
                    + " und starte erneut." + RESET);
            System.out.println(GELB + "  Haengst du fest: TIPPS.md im Kapitelordner hat gestufte Hinweise." + RESET);
            System.exit(1);
        }
    }

    /** Ueberschrift fuer einen Aufgabenblock. */
    public static void abschnitt(String titel) {
        System.out.println();
        System.out.println("  " + titel);
        System.out.println("  " + "-".repeat(Math.max(4, titel.length())));
    }

    // ---------------------------------------------------------------- intern

    private static String farbe(String code) {
        return FARBE ? ESC + code : "";
    }

    private static void bestanden(String was) {
        System.out.println("  " + GRUEN + "OK  " + RESET + was);
    }

    private static void fehlgeschlagen(String was, String detail) {
        FEHLER.add(was);
        System.out.println("  " + ROT + "FEHL" + RESET + " " + was);
        for (String zeile : detail.split("\n", -1)) {
            System.out.println("       " + GRAU + zeile + RESET);
        }
    }

    /** Erklaerung, wenn beide Werte gleich aussehen und trotzdem verschieden sind. */
    private static String gleicheAnzeige(Object a, Object b) {
        if (a.getClass() != b.getClass()) {
            String typen = "Typen verschieden: " + a.getClass().getSimpleName()
                    + " gegen " + b.getClass().getSimpleName() + ".";
            if (a instanceof Number && b instanceof Number) {
                typen += " Passt der Rueckgabetyp? Im Test z. B. 3L fuer long oder 2.0 fuer double schreiben.";
            }
            return typen;
        }
        // Listen und Arrays: das erste verschiedene Element genauer ansehen
        List<?> x = alsListe(a);
        List<?> y = alsListe(b);
        if (x != null && y != null && x.size() == y.size()) {
            for (int i = 0; i < x.size(); i++) {
                Object p = x.get(i);
                Object q = y.get(i);
                if (!tiefGleich(p, q)) {
                    if (p != null && q != null && zeige(p).equals(zeige(q))) {
                        return "Element " + i + ": " + gleicheAnzeige(p, q);
                    }
                    break;
                }
            }
        }
        return "Sieht gleich aus, aber equals() liefert false (equals nicht oder falsch ueberschrieben?)";
    }

    private static List<?> alsListe(Object o) {
        if (o instanceof List<?> l)  return l;
        if (o instanceof Object[] x) return Arrays.asList(x);
        return null;
    }

    private static boolean tiefGleich(Object a, Object b) {
        if (a instanceof Object[] x && b instanceof Object[] y)   return Arrays.deepEquals(x, y);
        if (a instanceof int[] x     && b instanceof int[] y)     return Arrays.equals(x, y);
        if (a instanceof long[] x    && b instanceof long[] y)    return Arrays.equals(x, y);
        if (a instanceof double[] x  && b instanceof double[] y)  return Arrays.equals(x, y);
        if (a instanceof char[] x    && b instanceof char[] y)    return Arrays.equals(x, y);
        if (a instanceof boolean[] x && b instanceof boolean[] y) return Arrays.equals(x, y);
        return Objects.deepEquals(a, b);
    }

    private static String zeige(Object o) {
        if (o == null) return "null";
        if (o instanceof Object[] x)  return folge(Arrays.asList(x), o);
        if (o instanceof int[] x)     return Arrays.toString(x);
        if (o instanceof long[] x)    return Arrays.toString(x);
        if (o instanceof double[] x)  return Arrays.toString(x);
        if (o instanceof float[] x)   return Arrays.toString(x);
        if (o instanceof short[] x)   return Arrays.toString(x);
        if (o instanceof byte[] x)    return Arrays.toString(x);
        if (o instanceof char[] x)    return Arrays.toString(x);
        if (o instanceof boolean[] x) return Arrays.toString(x);
        if (o instanceof String s)    return "\"" + sichtbar(s) + "\"";
        if (o instanceof Character c) return "'" + sichtbar(String.valueOf(c)) + "'";
        // Nur JDK-Collections: Eigene Klassen behalten ihr toString()
        if (o instanceof Collection<?> c && ausDemJdk(o)) return folge(c, o);
        if (o instanceof Map<?, ?> m && ausDemJdk(o)) {
            List<String> paare = new ArrayList<>();
            for (Map.Entry<?, ?> e : m.entrySet()) {
                paare.add(zeigeTeil(e.getKey(), o) + "=" + zeigeTeil(e.getValue(), o));
            }
            return "{" + String.join(", ", paare) + "}";
        }
        return o.toString().replace("\r", "\\r");
    }

    /** Elemente einzeln anzeigen, damit Strings darin Anfuehrungszeichen und sichtbare \r\n bekommen. */
    private static String folge(Iterable<?> elemente, Object ganzes) {
        List<String> teile = new ArrayList<>();
        for (Object e : elemente) teile.add(zeigeTeil(e, ganzes));
        return "[" + String.join(", ", teile) + "]";
    }

    private static String zeigeTeil(Object teil, Object ganzes) {
        return teil == ganzes ? "(dieselbe Sammlung)" : zeige(teil);
    }

    private static boolean ausDemJdk(Object o) {
        return o.getClass().getName().startsWith("java.");
    }

    /** Macht \r, \n und \t sichtbar. Nach \n folgt weiter ein Zeilenumbruch, damit Text lesbar bleibt. */
    private static String sichtbar(String s) {
        return s.replace("\t", "\\t").replace("\r", "\\r").replace("\n", "\\n\n");
    }
}
