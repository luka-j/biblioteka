package rs.luka.biblioteka.exceptions;

/**
 * Oznacava da dati element nije jedinstven. Namenjeno prvenstveno za UniqueList,
 * jer je unchecked i ne treba da se deklarise u funkciji (ne remeti override)
 * 
 * @author luka
 * @since 3.1.'15.
 */
public class NotUnique extends RuntimeException {

    /**
     * Creates a new instance of <code>NotUnique</code> without detail message.
     */
    public NotUnique() {
        super();
    }

    /**
     * Constructs an instance of <code>NotUnique</code> with the specified
     * detail message.
     *
     * @param msg the detail message.
     */
    public NotUnique(String msg) {
        super(msg);
    }
}
