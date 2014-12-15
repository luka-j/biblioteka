//126 linija, 24.8.'14.
//202 linije, 25.10.'14.
//204 linije, 29.11.'14.
package rs.luka.biblioteka.debugging;

import rs.luka.biblioteka.data.Podaci;
import rs.luka.biblioteka.exceptions.Duplikat;

/**
 * Test klasa
 *
 * @author Luka
 */
public class Test {

    public void testUnos() {
        int i = 0, j = 0;
        for (i = 0; i < 5000; i++) {
            try{Podaci.addUcenik("Pera Peric " + i, (i%8)+1, new String[]{});}
            catch(Duplikat ex) {}
        }
        for (j = 0; j < 0; j++) {
            try {Podaci.addKnjiga("Knjiga o dzungli " + j, j%100, "Imaginaran " + j%3);} 
            catch (Duplikat ex) {}
        }
    }
}
