package rs.luka.biblioteka.exceptions;

/**
 * Exception da oznaci da je prosledjeni objekat prazan ili ne postoji.
 *
 * @author luka
 * @since 18.8.'14.
 */
public class Prazno extends Exception {

    public Prazno() {
        super();
    }

    public Prazno(String message) {
        super(message);
    }

    public Prazno(Throwable cause) {
        super(cause);
    }

    public Prazno(String message, Throwable cause) {
        super(message, cause);
    }
}
