package rs.luka.biblioteka.funkcije;

import java.io.IOException;
import static java.lang.String.valueOf;
import java.util.logging.Level;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.showMessageDialog;
import rs.luka.biblioteka.data.Config;
import rs.luka.biblioteka.data.Podaci;
import rs.luka.biblioteka.exceptions.Duplikat;
import rs.luka.biblioteka.exceptions.Prazno;
import rs.luka.biblioteka.exceptions.PreviseKnjiga;
import rs.luka.biblioteka.exceptions.VrednostNePostoji;
import static rs.luka.biblioteka.grafika.Konstante.*;
import rs.luka.biblioteka.grafika.Ucenici;

/**
 *
 * @author Luka
 */
public class Unos {
    
    private static int prevLogLevel = Level.INFO.intValue();
    
    private static final java.util.logging.Logger LOGGER = 
            java.util.logging.Logger.getLogger(Unos.class.getName());
    
    /**
     * Isključuje logovanje nivoa INFO za vreme unosa
     */
    public static void initUnos() {
        if(Config.hasKey("logLevel"))
            prevLogLevel = Level.parse(Config.get("logLevel")).intValue();
        Config.set("logLevel", "WARNING");
        Logger.finalizeLogger();
        Logger.initLogger();
    }

    /**
     * Proverava da li je unos u redu i zove
     * {@link Podaci#dodajKnjigu(java.lang.String, int, java.lang.String)}
     *
     * @param nas naslov knjige
     * @param kol kolicina knjige
     * @param pisac pisac knjige
     * @throws rs.luka.biblioteka.exceptions.Prazno ako je naslov "" ili null
     * @throws rs.luka.biblioteka.exceptions.Duplikat ako naslov već postoji
     */
    public static void UnosKnj(String nas, int kol, String pisac) throws Prazno, Duplikat {
        try {
            Podaci.dodajKnjigu(nas, kol, pisac);
        }
        catch(VrednostNePostoji ex) {
            throw new Prazno(ex);
        }
    }

    /**
     * Proverava da li je unos u redu i zove
     * {@link Podaci#dodajUcenika(java.lang.String, int, java.lang.String[])}
     *
     * @param uc ime ucenika
     * @param knjige knjige koje su trenutno kod ucenika
     * @param raz razred u koji ucenik trenutno ide
     * @throws rs.luka.biblioteka.exceptions.PreviseKnjiga ako se unosi previse knjiga (prema configu)
     * @throws rs.luka.biblioteka.exceptions.Prazno ako je ucenik "" ili null
     * @throws rs.luka.biblioteka.exceptions.Duplikat ako ucenik vec postoji
     * @throws rs.luka.biblioteka.exceptions.VrednostNePostoji ako knjiga ne postoji
     */
    public static void UnosUc(String uc, String[] knjige, int raz) throws PreviseKnjiga, Prazno, Duplikat, VrednostNePostoji {
        if (uc == null || uc.isEmpty()) {
            throw new Prazno("String imena ucenika je \"\" ili null");
        }
        int brKnjiga = Config.getAsInt("brKnjiga");
        if(knjige.length > brKnjiga) {
            throw new PreviseKnjiga("Previše knjiga pri unosu");
        }
        Podaci.dodajUcenika(uc, raz, knjige);
    }

    /**
     * Zavrsava unos, vraca prethodni logLevel i ponovo ucitava grafiku.
     * 
     * @since 10.11.'13.
     */
    public static void finalizeUnos() {
        Config.set("firstRun", "false");
        LOGGER.log(Level.CONFIG, "Unos gotov. Postavljam firstRun na false.");
        Config.set("logLevel", valueOf(prevLogLevel));
        Logger.finalizeLogger();
        Logger.initLogger();
        Config.set("knjSize", valueOf(Podaci.getBrojKnjiga()));
        Config.set("ucSize", valueOf(Podaci.getBrojUcenika()));
        try {
            Save.save();
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "Greška pri čuvanju podataka nakon unosa", ex);
            showMessageDialog(null, FINALIZE_IOEX_MSG_STRING, FINALIZE_IOEX_TITLE_STRING, 
                    JOptionPane.ERROR_MESSAGE);
        }
        new Ucenici().pregledUcenika();
    }
}
