/**
 * Kapitel 06: Musterloesung. Eine Holzente fuer Jaeger, sie fliegt nicht und ist stumm.
 */
public class Lockente extends Ente {

    public Lockente() {
        super(new FliegtNicht(), new Stumm());
    }

    @Override
    public String anzeigen() {
        return "Ich bin eine Lockente";
    }
}
