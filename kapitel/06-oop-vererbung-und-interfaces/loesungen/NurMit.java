import java.util.Objects;

/**
 * Kapitel 06: Musterloesung. Dekorierer, der nur Zeilen durchlaesst, die
 * einen bestimmten Text enthalten.
 */
public class NurMit implements Protokoll {

    private final String teil;
    private final Protokoll innen;

    public NurMit(String teil, Protokoll innen) {
        this.teil = Objects.requireNonNull(teil, "teil");
        this.innen = Objects.requireNonNull(innen, "innen");
    }

    @Override
    public void schreibe(String zeile) {
        // Ein Dekorierer muss nicht immer weiterreichen. Er darf auch
        // verschlucken. Von aussen sieht er trotzdem aus wie ein Protokoll.
        if (zeile.contains(teil)) {
            innen.schreibe(zeile);
        }
    }
}
