/**
 * Kapitel 06: Musterloesung. Die Badewannen-Ente.
 */
public class Gummiente extends Ente {

    public Gummiente() {
        super(new FliegtNicht(), new Quietschen());
    }

    @Override
    public String anzeigen() {
        return "Ich bin eine Gummiente";
    }
}
