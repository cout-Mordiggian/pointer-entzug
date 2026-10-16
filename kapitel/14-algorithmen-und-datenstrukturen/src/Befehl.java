/**
 * Kapitel 14: die Schnittstelle fuer das Befehl-Muster (Aufgabe 10, schon fertig).
 *
 * Ein Befehl ist eine Aktion als Objekt. Weil er ein Objekt ist, kann man ihn
 * speichern, in einen Stapel legen und spaeter rueckgaengig machen.
 *
 * Vertrag: rueckgaengig() wird nur nach ausfuehren() aufgerufen und stellt
 * genau den Zustand von davor wieder her.
 */
public interface Befehl {

    void ausfuehren();

    void rueckgaengig();
}
