/**
 * Kapitel 06: Musterloesung. Keinen Laut von sich geben.
 */
public class Stumm implements QuakVerhalten {

    @Override
    public String quaken() {
        return "<< Stille >>";
    }
}
