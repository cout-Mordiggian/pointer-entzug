/**
 * Kapitel 09: die Schnittstelle fuer das Beobachter-Muster (schon fertig).
 *
 * Wer wissen will, wann ein Artikel knapp wird, implementiert dieses Interface
 * und meldet sich beim Lager an. Das Interface ist funktional, also genuegt ein Lambda:
 *
 *   lager.anmelden((artikel, bestand) -> System.out.println(artikel + " knapp: " + bestand));
 */
@FunctionalInterface
public interface LagerBeobachter {

    void knapp(String artikel, int bestand);
}
