/**
 * Kapitel 06: Musterloesung. Ein Modell, das (noch) nicht fliegt.
 */
public class Modellente extends Ente {

    public Modellente() {
        super(new FliegtNicht(), new Quaken());
    }

    @Override
    public String anzeigen() {
        return "Ich bin eine Modellente";
    }
}
