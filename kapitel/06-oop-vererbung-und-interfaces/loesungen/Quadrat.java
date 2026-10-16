/**
 * Kapitel 06: Musterloesung Quadrat.
 */
public class Quadrat extends Rechteck {

    public Quadrat(double seite) {
        // Nutzt den protected Konstruktor der Oberklasse und gibt den
        // eigenen Namen mit. flaeche() und umfang() muessen nicht
        // ueberschrieben werden, denn die Rechteck-Formeln stimmen bereits.
        super("Quadrat", seite, seite);
    }

    public double getSeite() {
        // Kein eigenes Feld noetig: die Seite steckt schon im geerbten Zustand.
        // Ein zweites Feld waere redundant und koennte auseinanderlaufen.
        return getBreite();
    }

    /**
     * Kovarianter Rueckgabetyp: Rechteck.skaliert gibt Figur zurueck,
     * hier verengen wir auf Quadrat. Erlaubt, weil jedes Quadrat eine
     * Figur ist. Der Vertrag der Oberklasse bleibt also erfuellt.
     *
     * Der Nutzen: Aufrufer, die auf einer Quadrat-Variablen arbeiten,
     * brauchen keinen Cast.
     */
    @Override
    public Quadrat skaliert(double faktor) {
        return new Quadrat(getSeite() * faktor);
    }

    // Warum "Quadrat extends Rechteck" hier gut geht: beide sind
    // unveraenderlich. Haette Rechteck ein setBreite(), koennte man ein
    // Quadrat ueber eine Rechteck-Variable zu einem Nicht-Quadrat machen.
    // Das ist der klassische Verstoss gegen das Liskov-Prinzip ("eine
    // Unterklasse muss ueberall funktionieren, wo die Oberklasse erwartet wird").
    //
    // Hinweis zu equals: Wuerde man equals ergaenzen, muessten beide Klassen
    // dieselbe Regel benutzen. Prueft Rechteck mit instanceof Rechteck und
    // Quadrat mit instanceof Quadrat, ist rechteck.equals(quadrat) true, aber
    // quadrat.equals(rechteck) false: Die Symmetrie des equals-Vertrags ist
    // gebrochen (README 6.8).
}
