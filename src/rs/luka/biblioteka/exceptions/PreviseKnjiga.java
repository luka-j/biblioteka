package rs.luka.biblioteka.exceptions;

import static java.lang.String.valueOf;

/**
 * Exception da oznaci da se kod ucenika nalazi maksimum knjiga (brKnjiga u
 * configu).
 *
 * @author luka
 * @since 18.8.'14.
 */
public class PreviseKnjiga extends Exception {

    public PreviseKnjiga(String message) {
        super(message);
    }

    /**
     * Konstruktor koji index u argumentu pretvara u String i salje kao poruku
     * Exceptiona, koja se moze dobiti putem {@link Throwable#getMessage} .
     *
     * @param index
     */
    public PreviseKnjiga(int index) {
        super(valueOf(index));
    }

    public PreviseKnjiga(int index, Throwable ex) {
        super(valueOf(index), ex);
    }
}
