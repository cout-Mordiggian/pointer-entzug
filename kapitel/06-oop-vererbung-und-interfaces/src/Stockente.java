/**
 * Kapitel 06: Aufgabe 8c. Die ganz normale Ente.
 *
 *   Ente e = new Stockente();
 *   e.anzeigen()   // "Ich bin eine Stockente"
 *   e.fliegen()    // "Ich fliege!"
 *   e.quaken()     // "Quak"
 */
public class Stockente extends Ente {

    public Stockente() {
        // TODO: die richtigen Verhalten an Ente uebergeben.
        // Vorgegeben ist ein falscher Platzhalter, damit die Datei kompiliert.
        super(new FliegtNicht(), new Stumm());
    }

    @Override
    public String anzeigen() {
        // TODO
        return "";
    }
}
