package rs.luka.biblioteka.exceptions;

/**
 *
 * @author luka
 */
public class LosFormat extends Exception {

    /**
     * Creates a new instance of <code>LosFormat</code> without detail message.
     */
    public LosFormat() {
        super();
    }

    /**
     * Constructs an instance of <code>LosFormat</code> with the specified
     * detail message.
     *
     * @param msg the detail message.
     */
    public LosFormat(String msg) {
        super(msg);
    }
    
    public LosFormat(Throwable ex) {
        super(ex);
    }
    
    public LosFormat(String msg, Throwable ex) {
        super(msg, ex);
    }
}
