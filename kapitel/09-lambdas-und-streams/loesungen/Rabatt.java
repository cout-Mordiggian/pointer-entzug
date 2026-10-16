/**
 * Kapitel 09: die Schnittstelle fuer das Strategie-Muster (schon fertig).
 *
 * Eine Rabatt-Strategie bekommt einen Betrag in Cent und liefert den Betrag
 * NACH Abzug des Rabatts. Weil das Interface genau eine abstrakte Methode hat,
 * ist es funktional. Jede Strategie kann auch ein Lambda sein:
 *
 *   Rabatt halbePreis = betrag -> betrag / 2;
 */
@FunctionalInterface
public interface Rabatt {

    long anwenden(long betragCent);
}
