package rs.luka.biblioteka.exceptions;

/**
 *
 * @author luka
 */
@SuppressWarnings("serial")
public class ConfigException extends RuntimeException {
    
    public ConfigException() {
        super();
    }
    
    /**
     * Constructs an instance of <code>ConfigException</code> with the specified detail
     * message.
     *
     * @param msg opcija u configu koja nije dobra
     */
    public ConfigException(String msg) {
        super(msg);
    }
}
