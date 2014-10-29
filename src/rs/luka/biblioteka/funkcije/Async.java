package rs.luka.biblioteka.funkcije;

import rs.luka.biblioteka.exceptions.VrednostNePostoji;

/**
 *
 * @author Luka
 * @since 26.06.'14.
 * @deprecated
 */
public class Async implements Runnable {

    private final String metoda;
    private final String pU_knj;

    public Async(String metoda, String pU_knj) {
        this.metoda = metoda;
        this.pU_knj = pU_knj;
    }

    @Override
    public void run() {
        if (metoda.equals("Knjige.pretraziUcenike")) {
            try {
                new Knjige().pretraziUcenike(pU_knj);
            } catch (VrednostNePostoji ex) {
                ex.printStackTrace();
            }
        }
    }
}
