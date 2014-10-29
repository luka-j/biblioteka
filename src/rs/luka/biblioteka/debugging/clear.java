package rs.luka.biblioteka.debugging;

import java.io.File;
import java.io.IOException;

/**
 * Test klasa.
 *
 * @author Luka
 */
public class clear {

    /**
     * Brise podatke. Test metoda.
     */
    public void clear() {
        try {
            File nas = new File("Data" + File.separator + "Knjige.dat");
            nas.delete();
            nas.createNewFile();
            File kol = new File("Data" + File.separator + "Ucenici.dat");
            kol.delete();
            kol.createNewFile();
        } catch (IOException ex) {
            ex.printStackTrace(System.out);
        }
    }
}
