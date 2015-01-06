//126 linija, 24.8.'14.
//202 linije, 25.10.'14.
//204 linije, 29.11.'14.
//186 linija, 25.12.'14. (bsh konzola)
//180 linije, 27.12.'14. (trenutno, auto, cleanup konzole, Unos)
package rs.luka.biblioteka.debugging;

import java.util.logging.Logger;
import rs.luka.biblioteka.exceptions.Duplikat;
import rs.luka.biblioteka.exceptions.Prazno;
import rs.luka.biblioteka.exceptions.PreviseKnjiga;
import rs.luka.biblioteka.exceptions.VrednostNePostoji;
import rs.luka.biblioteka.funkcije.Unos;

/**
 * Test klasa
 *
 * @author Luka
 */
public class Test {

    /**
     *
     */
    public void testUnos() {
        Unos.initUnos();
        int i = 0, j = 0;
        for (i = 0; i < 3_000; i++) {
            try{Unos.UnosUc("Pera Peric " + i,  new String[]{}, (i%8)+1);}
            catch (PreviseKnjiga | Prazno | Duplikat | VrednostNePostoji ex) {
                ex.printStackTrace();
            }
        }
        for (j = 0; j < 300; j++) {
            try {Unos.UnosKnj("Knjiga o dzungli " + j, j%100, "Imaginaran " + j%3);} 
            catch (Duplikat ex) {} catch (Prazno ex) {
                ex.printStackTrace();
            }
        }
        Unos.finalizeUnos();
    }
    private static final Logger LOG = Logger.getLogger(Test.class.getName());
}
