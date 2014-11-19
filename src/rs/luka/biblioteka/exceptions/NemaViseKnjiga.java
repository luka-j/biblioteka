//108 linija, 24.8.'14.
//142 linije, 24.9.'14.
//143 linije, 25.10.'14.
//205 linija, 18.11.'14.
package rs.luka.biblioteka.exceptions;

import rs.luka.biblioteka.data.Knjiga;

/**
 * Exception da oznaci da su sve knjige tog naslova vec iznajmljene.
 *
 * @author luka
 * @since 18.8.'14.
 */
public class NemaViseKnjiga extends Exception {

    public NemaViseKnjiga() {
        super();
    }

    public NemaViseKnjiga(String message) {
        super(message);
    }

    /**
     * Stavlja naslov date knjige u message Exceptiona
     *
     * @param knjiga knjige koje nema vise
     * @see Exception#getMessage()
     */
    public NemaViseKnjiga(Knjiga knjiga) {
        super(knjiga.getNaslov());
    }
}
