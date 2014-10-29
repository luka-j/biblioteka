package rs.luka.biblioteka.exceptions;

/**
 *
 * @author luka
 * @since 4.9.'14.
 */
public class Duplikat extends Exception {

    /**
     * Constructs an instance of <code>Duplikat</code> with the specified detail
     * message.
     *
     * @param msg duplikat cega
     */
    public Duplikat(String msg) {
        super(msg);
    }
    
    public Duplikat(rs.luka.biblioteka.exceptions.VrednostNePostoji.vrednost vrednost) {
        super(vrednost.name());
    }
}
