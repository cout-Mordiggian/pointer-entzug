import java.util.Locale;
import java.util.Objects;

/**
 * Kapitel 06: Musterloesung. Dekorierer, der jede Zeile gross schreibt.
 */
public class Grossgeschrieben implements Protokoll {

    private final Protokoll innen;

    public Grossgeschrieben(Protokoll innen) {
        this.innen = Objects.requireNonNull(innen, "innen");
    }

    @Override
    public void schreibe(String zeile) {
        // Locale.ROOT aus demselben Grund wie in Figur.beschreibung: Das
        // Ergebnis soll nicht von der Spracheinstellung des Rechners abhaengen.
        innen.schreibe(zeile.toUpperCase(Locale.ROOT));
    }
}
