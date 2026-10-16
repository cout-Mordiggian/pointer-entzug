/**
 * Kapitel 14: der Befehl "Text loeschen" (Aufgabe 10a).
 * Pruefen:  ./lerne.sh 14
 *
 *   Textpuffer t = ...;                          // "Hallo Welt"
 *   Befehl b = new LoeschenBefehl(t, 5, 5);      // 5 Zeichen ab Position 5
 *   b.ausfuehren();                              // "Hallo"
 *   b.rueckgaengig();                            // "Hallo Welt"
 *
 * Achtung: Zwischen Erzeugen und Ausfuehren kann sich der Puffer aendern.
 * rueckgaengig() muss genau das zurueckbringen, was beim AUSFUEHREN geloescht
 * wurde.
 */
public class LoeschenBefehl implements Befehl {

    // TODO: Felder

    public LoeschenBefehl(Textpuffer puffer, int von, int laenge) {
        // TODO
    }

    @Override
    public void ausfuehren() {
        // TODO: Textpuffer.loeschen gibt dir den geloeschten Text zurueck.
    }

    @Override
    public void rueckgaengig() {
        // TODO
    }
}
