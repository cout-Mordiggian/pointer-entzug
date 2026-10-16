/**
 * Kapitel 06: Musterloesung. Nicht fliegen koennen.
 */
public class FliegtNicht implements FlugVerhalten {

    @Override
    public String fliegen() {
        return "Ich kann nicht fliegen.";
    }
}
