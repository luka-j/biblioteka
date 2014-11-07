package rs.luka.biblioteka.exceptions;

/**
 * Exception koji oznacava da trazena vrednost (Ucenik, Knjiga ili UcenikKnjiga)
 * ne postoji. Postoje 2 konstruktora i oba zahtevaju poruku da naznaci koja
 * vrednost ne postoji,
 *
 * @author luka
 * @since 18.8.'14.
 */
public class VrednostNePostoji extends Exception {

    public VrednostNePostoji(vrednost vrednost) {
        super(vrednost.name());
    }

    public VrednostNePostoji(vrednost vrednost, Throwable ex) {
        super(vrednost.name(), ex);
    }

    public enum vrednost {
        Ucenik, Knjiga, UcenikKnjiga, drugo
    }
}
