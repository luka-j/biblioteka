package rs.luka.biblioteka.exceptions;

/**
 *
 * @author luka
 */
public class ConfigException extends RuntimeException {
    
    public ConfigException() {
        super();
    }
    
    /**
     * Constructs an instance of <code>Duplikat</code> with the specified detail
     * message.
     *
     * @param msg duplikat cega
     */
    public ConfigException(String msg) {
        super(msg);
    }
}
