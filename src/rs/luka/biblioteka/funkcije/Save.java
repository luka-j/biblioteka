package rs.luka.biblioteka.funkcije;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import rs.luka.biblioteka.data.Podaci;
import static rs.luka.biblioteka.data.Podaci.getBrojKnjiga;
import static rs.luka.biblioteka.data.Podaci.getBrojUcenika;

/**
 *
 * @author luka
 * @since pocetak
 */
public class Save {
    
    private static final java.util.logging.Logger LOGGER = java.util.logging.Logger.getLogger(Save.class.getName());

    /**
     * Zove funkcije za cuvanje ucenika, zatim za cuvanje knjiga. Ako folder 
     * sa podacima ne postoji, kreira ga.
     *
     * @throws IOException
     * @see #saveUcenike() 
     * @see #saveKnjige() 
     * @since 24.9.'14.(new)
     */
    public static void save() throws IOException {
        File data = new File("Data");
        if (!data.isDirectory()) {
            data.mkdir();
        }
        saveUcenike();
        saveKnjige();
        
    }

    /**
     * Cuva listu sa ucenicima u fajl. Koristi metodu {@link rs.luka.biblioteka.data.Ucenik#getAsIOString()}
     * za format podataka. Izmedju ucenika se nalazi newline karakter ('\n')
     * 
     * @throws IOException 
     * @since 24.9.'14.(new)
     */
    public static void saveUcenike() throws IOException {
        LOGGER.log(Level.FINE, "Počinjem čuvanje učenika");
        File ucenici = new File(Utils.getWorkingDir() + "Data" + File.separator + "Ucenici.dat");
        if (getBrojUcenika() == 0) {
            LOGGER.log(Level.FINE, "Lista sa učenicima je prazna. Preskačem čuvanje podataka.");
            return;
        }
        ucenici.createNewFile();
        try (BufferedWriter fwU = new BufferedWriter(new FileWriter(ucenici))) {
            for (int i = 0; i < getBrojUcenika(); i++) {
                fwU.write(Podaci.getUcenik(i).getAsIOString());
                fwU.write('\n');
            }
        }
        LOGGER.log(Level.INFO, "Sačuvao učenike");
    }
    
    /**
     * Cuva listu sa knjigama u fajl. Koristi metodu {@link rs.luka.biblioteka.data.Knjiga#getAsIOString()}
     * za format podataka. Izmedju knjiga se nalazi newline karakter ('\n')
     * 
     * @throws IOException 
     * @since 24.9.'14.(new)
     */
    public static void saveKnjige() throws IOException {
        LOGGER.log(Level.FINE, "Počinjem čuvanje knjiga");
        File knjige = new File(Utils.getWorkingDir() + "Data" + File.separator + "Knjige.dat");
        if (getBrojKnjiga() == 0) {
            LOGGER.log(Level.FINE, "Lista sa knjigama je prazna. Preskačem čuvanje knjiga.");
            return;
        }
        knjige.createNewFile();
        try (BufferedWriter fwN = new BufferedWriter(new FileWriter(knjige))) {
            for (int i = 0; i < getBrojKnjiga(); i++) {
                fwN.write(Podaci.getKnjiga(i).getAsIOString());
                fwN.write('\n');
            }
        }
        LOGGER.log(Level.INFO, "Sačuvao knjige");
    }
}
