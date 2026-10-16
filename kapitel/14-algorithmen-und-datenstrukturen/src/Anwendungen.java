/**
 * Kapitel 14: Anwendungen fuer DEINEN Stapel (Aufgabe 8).
 * Pruefen:  ./lerne.sh 14
 *
 * Erst Stapel.java fertig machen, diese Aufgaben bauen darauf auf.
 */
public class Anwendungen {

    /**
     * Aufgabe 8a: Sind die Klammern ( ) [ ] { } korrekt geschachtelt?
     * Alle anderen Zeichen werden ignoriert.
     *
     *   ""          -> true
     *   "a(b[c]d)e" -> true
     *   "([)]"      -> false   (Zaehlen allein reicht NICHT!)
     *   "(("        -> false
     *   ")("        -> false
     */
    public static boolean klammernKorrekt(String s) {
        // TODO: Stapel<Character> offen = new Stapel<>();
        //       Oeffnende Klammer -> push.
        //       Schliessende Klammer -> Stapel leer? Dann falsch. Sonst pop:
        //       passt die zuletzt geoeffnete Klammer zu dieser?
        //       Am Ende: Ist noch etwas offen?
        return false;
    }

    /**
     * Aufgabe 8b: Kehrt einen String mit deinem Stapel um.
     *   "abc" -> "cba",  "" -> ""
     */
    public static String umkehren(String s) {
        // TODO: Alle Zeichen pushen, dann alle poppen und an einen
        //       StringBuilder haengen.
        return "";
    }
}
