package rs.luka.biblioteka.funkcije;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import static java.lang.Integer.parseInt;
import static java.lang.String.valueOf;
import java.util.Scanner;
import java.util.logging.Level;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.showMessageDialog;
import rs.luka.biblioteka.data.Config;
import rs.luka.biblioteka.data.Knjiga;
import rs.luka.biblioteka.data.Podaci;
import static rs.luka.biblioteka.data.Podaci.indexOfUcenik;
import rs.luka.biblioteka.data.Ucenik;
import rs.luka.biblioteka.exceptions.Prazno;
import rs.luka.biblioteka.exceptions.PreviseKnjiga;
import rs.luka.biblioteka.grafika.Grafika;

/**
 *
 * @author Luka
 */
public class Unos {
    
    private static final java.util.logging.Logger LOGGER = java.util.logging.Logger.getLogger(Unos.class.getName());

    /**
     * Proverava da li je unos u redu i zove
     * {@link #KnjToDisk(java.lang.String, int) metodu za pisanje na disk}
     *
     * @param nas naslov knjige
     * @param kol kolicina knjige
     * @param pisac pisac knjige
     * @return 0 ako je sve OK, 1 ako naslov vec postoji
     */
    public int UnosKnj(String nas, int kol, String pisac) throws Prazno {
        if(nas==null || nas.isEmpty())
            throw new Prazno("String naslova knjige je \"\" ili null");
        if (Podaci.naslovExists(nas)) {
            LOGGER.log(Level.WARNING, "Naslov {0} već postoji", nas);
            showMessageDialog(null, "Naslov već postoji.", "Dupli unos",
                    JOptionPane.ERROR_MESSAGE);
            return 1;
        }
        KnjToDisk(nas, kol, pisac);
        return 0;
    }

    /**
     * Proverava da li je unos u redu i zove
     * {@link #UcToDisk(java.lang.String, java.lang.String, java.lang.String, java.lang.String, int, java.util.Date, java.util.Date, java.util.Date) metodu za pisanje na disk}
     *
     * @param uc ime ucenika
     * @param knjige knjige koje su trenutno kod ucenika
     * @param raz razred u koji ucenik trenutno ide
     * @return 0 ako je sve OK, 1 ako ucenik vec postoji, 2 ako je ucenik null
     * @throws rs.luka.biblioteka.exceptions.PreviseKnjiga ako se unosi previse knjiga (prema configu)
     */
    public int UnosUc(String uc, String[] knjige, int raz) throws PreviseKnjiga, Prazno {
        if (uc == null || uc.isEmpty()) {
            throw new Prazno("String imena ucenika je \"\" ili null");
        }
        if (indexOfUcenik(uc).isEmpty()) {
            int brKnjiga = parseInt(Config.get("brKnjiga"));
            if(knjige.length > brKnjiga)
                throw new PreviseKnjiga("Previše knjiga pri unosu");
            UcToDisk(uc, raz, knjige);
            return 0;
        }
        LOGGER.log(Level.WARNING, "Učenik {0} već postoji", uc);
        showMessageDialog(null, "Učenik već postoji.", "Dupli unos",
                JOptionPane.ERROR_MESSAGE);
        return 1;
    }

    //
    //--------------------------------------------------------------------------
    //
    /**
     * Cuva 1 liniju podataka na disk, u fajl sa knjigama
     *
     * @param knj knjiga
     * @param kol kolicina
     * @param pisac pisac knjige
     * @return 0 ako je sve OK
     * @since 10.11.'13.
     */
    public int KnjToDisk(String knj, int kol, String pisac) {
        LOGGER.log(Level.FINER, "Počinjem pisanje knjige {0} ({1} komada)na disk...", 
                new Object[]{knj, kol});
        File dataFolder = new File(Utils.getWorkingDir() + "Data");
        if (!dataFolder.isDirectory()) {
            dataFolder.mkdir();
        }
        File knjige = new File(dataFolder + File.separator + "Knjige.dat");
        if (knj == null) {
            LOGGER.log(Level.WARNING, "Poslata knjiga za upis je null."); //exception?
            knj = "";
        }
        try {
            knjige.createNewFile();
            try (FileWriter fwN = new FileWriter(knjige, true)) {
                fwN.append(new Knjiga(knj, kol, pisac).getAsIOString());
                fwN.append('\n');
            }
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "I/O greška pri unosu knjiga u fajl", ex);
            showMessageDialog(null, "Doslo je do greske pri pisanju na disk.",
                    "Greska", JOptionPane.ERROR_MESSAGE);

            return 1;
        }
        LOGGER.log(Level.FINE, "Knjiga {0} ({1} komada) unesena na disk.", 
                new Object[]{knj, kol});
        return 0;
    }

    /**
     * Cuva 1 liniju podataka na disk, u fajl sa ucenicima
     *
     * @param ime ucenik
     * @param razred
     * @param knjige knjige koje se nalaze kod ucenika
     * @return 0 ako je sve OK
     * @since 10.11.'13.
     */
    public int UcToDisk(String ime, int razred, String[] knjige) {
        LOGGER.log(Level.FINER, "Počinjem pisanje učenika {0} na disk...", ime);
        File dataFolder = new File(Utils.getWorkingDir() + "Data");
        if (!dataFolder.isDirectory()) {
            dataFolder.mkdir();
        }
        File ucenici = new File(dataFolder + File.separator + "Ucenici.dat");

        try {
            ucenici.createNewFile();
            try (FileWriter fwU = new FileWriter(ucenici, true)) {
                fwU.append(new Ucenik(ime, razred, knjige).getAsIOString());
                fwU.append('\n');
            }
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "Došlo je do I/O greške pri unosu učenika na disk", ex);
            showMessageDialog(null, "Došlo je do greške pri pisanju na disk.",
                    "I/O greška pri unosu", JOptionPane.ERROR_MESSAGE);
            return 1;
        }
        LOGGER.log(Level.FINE, "Učenik {0}({1}. razred) unesen na disk", new Object[]{ime, razred});
        return 0;
    }

    /**
     *
     * @since 10.11.'13.
     */
    public void finalizeUnos() {
        Config.set("firstRun", "false");
        LOGGER.log(Level.CONFIG, "Unos gotov. Postavljam firstRun na false.");
        File knjige = new File(Utils.getWorkingDir() + "Data" + File.separator + "Knjige.dat");
        try {
            knjige.createNewFile();
            FileReader fr = new FileReader(knjige);
            Scanner scanKnj = new Scanner(fr);
            int i = 0;
            while (scanKnj.hasNextLine()) {
                i++;
                scanKnj.nextLine();
            }
            Config.set("knjSize", valueOf(i));
            LOGGER.log(Level.CONFIG, "Prebrojao knjige. postavljam knjSize na {0}", i);
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "Greška pri čitanju fajla sa knjigama za prebrojavanje", ex);
            showMessageDialog(null, "Došlo je do greške pri čitanju fajla sa podacima o knjigama",
                    "Greška pri unosu.", JOptionPane.ERROR_MESSAGE);
        }

        try {
            File ucenici = new File(Utils.getWorkingDir() + "Data" + File.separator + "Ucenici.dat");
            ucenici.createNewFile();
            Scanner scanUc = new Scanner(new FileReader(ucenici));
            int i = 0;
            while (scanUc.hasNextLine()) {
                i++;
                scanUc.nextLine();
            }
            Config.set("ucSize", valueOf(i));
            LOGGER.log(Level.CONFIG, "Prebrojao učenike. postavljam ucSize na {0}", i);

        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "Greška pri čitanju fajla sa učenicima za prebrojavanje", ex);
            showMessageDialog(null, "Doslo je do greške pri čitanju fajla sa podacima o učenicima",
                    "Greška pri unosu.", JOptionPane.ERROR_MESSAGE);

        }
        
        /*String[] options = {"Da", "Ne"};
        int ugasiti = showOptionDialog(null, "Da biste videli promene, potrebno je restartovati program. "
                + "Ugasiti sada?", "Potrebno restartovanje", JOptionPane.YES_NO_OPTION,
                JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
        if (ugasiti == 0) {
            Init.exit(false);
        }*/
        Grafika.reset();
    }
}
