/**
 * Kapitel 13, Teil A: die Implementierungen, gegen die deine Tests laufen.
 *
 *   +-------------------------------------------------------------------+
 *   |  BITTE ERST NACH DER AUFGABE LESEN.                               |
 *   |                                                                   |
 *   |  Hier stehen eine korrekte Implementierung und neun Mutanten,     |
 *   |  jeder mit genau einem typischen Fehler. Wenn du vorher           |
 *   |  hineinschaust, schreibst du Tests gegen Fehler, die du kennst.   |
 *   |  Die Uebung ist aber, Tests gegen Fehler zu schreiben, die du     |
 *   |  NICHT kennst, so wie im echten Leben. Arbeite nur mit der        |
 *   |  Spezifikation in Rabattrechner.java und im README.               |
 *   +-------------------------------------------------------------------+
 *
 * Diese Datei musst du nicht aendern. Sie liegt identisch in src/ und
 * loesungen/.
 */
public final class Kandidaten {

    private Kandidaten() { }

    // Rechnen darf nur der Kandidat, den tests/Tests.java gerade an
    // alleBestanden uebergibt. So kann MeineTests keinen anderen Kandidaten
    // als "Orakel" fuer die erwarteten Werte befragen.
    private static Rabattrechner freigegeben;

    static void freigeben(Rabattrechner r) {
        freigegeben = r;
    }

    private static void pruefeFreigabe(Rabattrechner r) {
        if (r != freigegeben) {
            throw new IllegalStateException("Spielregel: Rufe nur das uebergebene r auf."
                    + " Erwartete Werte rechnest du selbst aus.");
        }
    }

    /** Die korrekte Implementierung. */
    public static final class Referenz implements Rabattrechner {
        @Override
        public long endpreisCent(long preisCent, int menge) {
            pruefeFreigabe(this);
            if (preisCent < 0 || menge < 0) {
                throw new IllegalArgumentException("negativ: preis=" + preisCent + ", menge=" + menge);
            }
            long gesamt = preisCent * menge;
            int prozent;
            if (menge >= 50) {
                prozent = 10;
            } else if (menge >= 10) {
                prozent = 5;
            } else {
                prozent = 0;
            }
            long rabatt = gesamt * prozent / 100;
            return gesamt - rabatt;
        }
    }

    // Fehler: Es wird der Endpreis abgerundet statt des Rabatts. Damit wird
    // der Rabatt in Wahrheit AUFgerundet (1990 * 95 / 100 = 1890 statt 1891).
    public static final class Mutant1 implements Rabattrechner {
        @Override
        public long endpreisCent(long preisCent, int menge) {
            pruefeFreigabe(this);
            if (preisCent < 0 || menge < 0) {
                throw new IllegalArgumentException("negativ: preis=" + preisCent + ", menge=" + menge);
            }
            long gesamt = preisCent * menge;
            int prozent;
            if (menge >= 50) {
                prozent = 10;
            } else if (menge >= 10) {
                prozent = 5;
            } else {
                prozent = 0;
            }
            return gesamt * (100 - prozent) / 100;
        }
    }

    // Fehler: > statt >= an der unteren Grenze. Genau 10 Stueck bekommen
    // keinen Rabatt. Findet nur ein Test mit menge == 10.
    public static final class Mutant2 implements Rabattrechner {
        @Override
        public long endpreisCent(long preisCent, int menge) {
            pruefeFreigabe(this);
            if (preisCent < 0 || menge < 0) {
                throw new IllegalArgumentException("negativ: preis=" + preisCent + ", menge=" + menge);
            }
            long gesamt = preisCent * menge;
            int prozent;
            if (menge >= 50) {
                prozent = 10;
            } else if (menge > 10) {
                prozent = 5;
            } else {
                prozent = 0;
            }
            long rabatt = gesamt * prozent / 100;
            return gesamt - rabatt;
        }
    }

    // Fehler: Nur die Menge wird geprueft, ein negativer Preis rutscht durch.
    // Findet nur ein Test, der einen negativen PREIS uebergibt.
    public static final class Mutant3 implements Rabattrechner {
        @Override
        public long endpreisCent(long preisCent, int menge) {
            pruefeFreigabe(this);
            if (menge < 0) {
                throw new IllegalArgumentException("negativ: menge=" + menge);
            }
            long gesamt = preisCent * menge;
            int prozent;
            if (menge >= 50) {
                prozent = 10;
            } else if (menge >= 10) {
                prozent = 5;
            } else {
                prozent = 0;
            }
            long rabatt = gesamt * prozent / 100;
            return gesamt - rabatt;
        }
    }

    // Fehler: falsche Reihenfolge in der if-else-Kette. Wer 50 Stueck kauft,
    // ist auch >= 10, der erste Zweig greift, 10 % werden nie erreicht.
    public static final class Mutant4 implements Rabattrechner {
        @Override
        public long endpreisCent(long preisCent, int menge) {
            pruefeFreigabe(this);
            if (preisCent < 0 || menge < 0) {
                throw new IllegalArgumentException("negativ: preis=" + preisCent + ", menge=" + menge);
            }
            long gesamt = preisCent * menge;
            int prozent;
            if (menge >= 10) {
                prozent = 5;
            } else if (menge >= 50) {
                prozent = 10;
            } else {
                prozent = 0;
            }
            long rabatt = gesamt * prozent / 100;
            return gesamt - rabatt;
        }
    }

    // Fehler: Der Gesamtpreis wird in einem int gespeichert. Ueber
    // 2.147.483.647 Cent (rund 21,5 Mio. Euro) laeuft er ueber und wird negativ.
    public static final class Mutant5 implements Rabattrechner {
        @Override
        public long endpreisCent(long preisCent, int menge) {
            pruefeFreigabe(this);
            if (preisCent < 0 || menge < 0) {
                throw new IllegalArgumentException("negativ: preis=" + preisCent + ", menge=" + menge);
            }
            int gesamt = (int) (preisCent * menge);
            int prozent;
            if (menge >= 50) {
                prozent = 10;
            } else if (menge >= 10) {
                prozent = 5;
            } else {
                prozent = 0;
            }
            long rabatt = (long) gesamt * prozent / 100;
            return gesamt - rabatt;
        }
    }

    // Fehler: menge <= 0 statt menge < 0. Die erlaubte leere Bestellung
    // (menge 0) wird mit einer Exception abgelehnt.
    public static final class Mutant6 implements Rabattrechner {
        @Override
        public long endpreisCent(long preisCent, int menge) {
            pruefeFreigabe(this);
            if (preisCent < 0 || menge <= 0) {
                throw new IllegalArgumentException("ungueltig: preis=" + preisCent + ", menge=" + menge);
            }
            long gesamt = preisCent * menge;
            int prozent;
            if (menge >= 50) {
                prozent = 10;
            } else if (menge >= 10) {
                prozent = 5;
            } else {
                prozent = 0;
            }
            long rabatt = gesamt * prozent / 100;
            return gesamt - rabatt;
        }
    }

    // Fehler: > statt >= an der oberen Grenze. Genau 50 Stueck bekommen nur
    // 5 %. Findet nur ein Test mit menge == 50 (51 oder 100 reichen nicht).
    public static final class Mutant7 implements Rabattrechner {
        @Override
        public long endpreisCent(long preisCent, int menge) {
            pruefeFreigabe(this);
            if (preisCent < 0 || menge < 0) {
                throw new IllegalArgumentException("negativ: preis=" + preisCent + ", menge=" + menge);
            }
            long gesamt = preisCent * menge;
            int prozent;
            if (menge > 50) {
                prozent = 10;
            } else if (menge >= 10) {
                prozent = 5;
            } else {
                prozent = 0;
            }
            long rabatt = gesamt * prozent / 100;
            return gesamt - rabatt;
        }
    }

    // Fehler: Nur der Preis wird geprueft, eine negative Menge rutscht durch
    // (das Spiegelbild von Mutant 3). Findet nur ein Test mit negativer MENGE.
    public static final class Mutant8 implements Rabattrechner {
        @Override
        public long endpreisCent(long preisCent, int menge) {
            pruefeFreigabe(this);
            if (preisCent < 0) {
                throw new IllegalArgumentException("negativ: preis=" + preisCent);
            }
            long gesamt = preisCent * menge;
            int prozent;
            if (menge >= 50) {
                prozent = 10;
            } else if (menge >= 10) {
                prozent = 5;
            } else {
                prozent = 0;
            }
            long rabatt = gesamt * prozent / 100;
            return gesamt - rabatt;
        }
    }

    // Fehler: preisCent <= 0 statt preisCent < 0. Der erlaubte Gratisartikel
    // (Preis 0) wird abgelehnt. Das Spiegelbild von Mutant 6.
    public static final class Mutant9 implements Rabattrechner {
        @Override
        public long endpreisCent(long preisCent, int menge) {
            pruefeFreigabe(this);
            if (preisCent <= 0 || menge < 0) {
                throw new IllegalArgumentException("ungueltig: preis=" + preisCent + ", menge=" + menge);
            }
            long gesamt = preisCent * menge;
            int prozent;
            if (menge >= 50) {
                prozent = 10;
            } else if (menge >= 10) {
                prozent = 5;
            } else {
                prozent = 0;
            }
            long rabatt = gesamt * prozent / 100;
            return gesamt - rabatt;
        }
    }
}
