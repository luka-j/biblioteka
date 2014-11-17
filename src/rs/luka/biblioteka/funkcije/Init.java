/**
 * @lastmod 17.11.'14. 
 * grafika...
 */
/**
 * @curr 
 * config, cleanup
 */
/**
 * @bugs 
 * SearchBox positioning, ne centrirati panele, smanjiti scrollpanel
 * Uzimanje knjige ne radi na test podacima (?)
 * Test ubacuje ucenike sa 0 knjiga u podatke (videti konstruktor)
 * undo u kombinaciji sa prethodnim redo-om izaziva exception, ako se iz stacka izbrisu neke akcije pri push(),
 * tako da setKnjiga throwuje Duplikat (da li smem ignorisati?)
 * Pregled ucenika za velike brojeve (par hiljada)
 * undoVracanje postavlja datum na trenutni, umesto datum iznajmljivanja knjige
 */
/**
 * @todo 
 * ISTESTIRATI SVE (UNIT TESTS, DEBUGGING)
 * Smisliti nacin da ponovo iscrta prozor u showTextFieldDialog ako throwuje Exception
 * auto-restore podataka iz backupa (ako je sve unisteno)
 * Grafika, ciscenje, bugfixing
 * Pocistiti reference, listenere, izbaciti indexe gde moze, ostatak koda i organizaciju(UK -> Uc)
 * BeanShell (bsh) konzola
 * Ubaciti kvačice (šđžčć)
 * Izbaciti sve preostale workaround-ove
 * Optimizovati memoriju i vreme
 */
/**
 * @changelog
 * EmptyBorders i Insets za sve komponente, regulisani iz fieldova
 * BETA faza
 * Razlaganje metoda za grafiku
 * Grafika za knjige cleanup
 * Pocistio metode i fieldove za pregledUcenika, listeneri se generisu posebno, pregledUcenika() iz konstruktora
 * Popravio workaround za prazno search polje, sad sve radi dobro
 * Sredio grafiku za Ucenike, sada se sastoji od Ucenici (pregledUcenika) i UceniciUtils (sve ostalo)
 * Popravio bug sa brisanjem ucenika
 * Dinamicko odredjivanje velicine prozora podesavanja, LosFormat exception, misc(,) bugfixes
 * Dodao logSize i logCount u config, nimbus i motif LaF
 * Ubacio i koristio iterator ucenika i knjiga gde je moguce
 * Uradio osnovnu proveru podataka za config
 * Popravio brKnjiga, poceo config checking
 * Ubacio Color TFColor, izbacio boolean TFBoja
 * Podesavanja prema configu
 * Pomerio changelog u fajl.
 */
//2771 linija, sa svim klasama osim onih iz legacy package-a. 24.11.'13.
//2802 linije, 22.2.'14.
//2942 linije, 17.5.'14.
//3085 linija, 23.6.'14.
//3181 linija, 19.7.'14.
//3913 linija, 24.8.'14. (bez changeloga, koji ima 45)
//4434 linija, 24.9.'14. (cleanup)
//5737 linija, 25.10.'14 (cleanup, encapsulation)

//1115 linija u packageu, 24.8.'14.
//1155 linija, 24.9.'14.
//1396 linija, 25.10.'14.
package rs.luka.biblioteka.funkcije;

import java.awt.Color;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import static java.lang.Thread.setDefaultUncaughtExceptionHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.showMessageDialog;
import static javax.swing.JOptionPane.showOptionDialog;
import javax.swing.JPanel;
import rs.luka.biblioteka.data.Config;
import static rs.luka.biblioteka.data.Config.loadConfig;
import static rs.luka.biblioteka.data.Datumi.proveriDatum;
import rs.luka.biblioteka.data.Podaci;
import static rs.luka.biblioteka.data.Podaci.loadData;
import rs.luka.biblioteka.grafika.Grafika;
import static rs.luka.biblioteka.grafika.Grafika.Grafika;
import static rs.luka.biblioteka.grafika.Grafika.loadLnF;
import rs.luka.biblioteka.debugging.Test;
import static rs.luka.biblioteka.funkcije.Logger.finalizeLogger;
import static rs.luka.biblioteka.funkcije.Logger.initLogger;
import static rs.luka.biblioteka.data.Ucenik.setValidRazred;
import static rs.luka.biblioteka.funkcije.Utils.initWorkingDir;
import static rs.luka.biblioteka.funkcije.Utils.setWorkingDir;
import static rs.luka.biblioteka.funkcije.Undo.initUndo;

/**
 *
 * @author Luka
 * @since '14.
 */
class Handler implements Thread.UncaughtExceptionHandler {

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        String stackTrace = sw.toString();
        
        java.util.logging.Logger.getLogger(Handler.class.getName()).log(Level.SEVERE, "Uncaught exception", e);
        showMessageDialog(null, "Došlo je do neočekivane greške. Detalji:\n" + stackTrace
                + "\novi podaci su sačuvani u log.", "Nepoznata greška", JOptionPane.ERROR_MESSAGE);
    }

}

/**
 * Klasa koja sadrzi main, init i exit metode, pozivane na pocetku i kraju
 * izvrsavanja programa.
 *
 * @author luka
 * @since 22.8'14
 */
public class Init {

    private static final java.util.logging.Logger LOGGER = java.util.logging.Logger.getLogger(Init.class.getName());
    /**
     * Period na koji se podaci automatski cuvaju, u milisekundama.
     */
    private static int autosave_period;

    /**
     * Postavlja default UncaughtExceptionHandler, iscrtava mali prozor koji
     * oznacava da je ucitavanje podataka u toku, zove {@link #init()} ,
     *
     * @param args
     */
    public static void main(String[] args) {
        setDefaultUncaughtExceptionHandler(new Handler());
        
        JFrame initWin = new JFrame("Učitavanje podataka...");
        initWin.setSize(250, 80);
        initWin.setLocationRelativeTo(null);
        initWin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initWin.setAlwaysOnTop(true);
        JPanel initPan = new JPanel();
        initPan.setBackground(Color.WHITE);
        initWin.setContentPane(initPan);
        JLabel initLab = new JLabel("Učitavanje podataka...");
        initPan.add(initLab);
        initWin.setVisible(true);

        init();

        initWin.dispose();
        
        autosave();
    }

    /**
     * Radi inicijalizaciju programa. Zove init*, load* i set* metode odgovarajucim redosledom. 
     * Vidi see also odeljak. 
     * Ne menjati redosled, osim ako neki deo ne radi (exceptioni pri inicijalizaciji).
     *
     * @see Utils#initWorkingDir
     * @see Config#loadConfig
     * @see Utils#setWorkingDir
     * @see Logger#initLogger
     * @see Grafika#loadLnF
     * @see Grafika#Grafika
     * @see rs.luka.biblioteka.data.Ucenik#setValidRazred
     * @see Podaci#loadData
     * @see rs.luka.biblioteka.data.Datumi#proveriDatum
     * @see Undo#initUndo
     */
    public static void init() {
        initWorkingDir();
        loadConfig();
        setWorkingDir();
        initLogger();
        loadLnF();
        Grafika(); //
        setValidRazred();
        loadData();
        //new Test().testUnos();
        proveriDatum();
        initUndo();
        
        LOGGER.log(Level.INFO, "Inicijalizacija programa gotova.");
    }

    /**
     * Cuva podatke i zatvara aplikaciju. Sav error handling radi unutar metode.
     *
     * @param sacuvaj
     */
    public static void exit(boolean sacuvaj) {
        String opcije[] = {"Da", "Ne"};
        LOGGER.log(Level.INFO, "Izlazim iz programa... Čuvam podatke: {0}", sacuvaj);
        if (sacuvaj) {
            try {
                Save.save();
                finalizeLogger();
                System.exit(0);
            } catch (IOException ex) {
                try {
                    LOGGER.log(Level.SEVERE, "I/O Greška pri čuvanju podataka.", ex);
                } catch (Exception | Error ex1) {
                    ex1.printStackTrace(); //nikad?
                }
                int zatvori = showOptionDialog(null, "Došlo je do greške "
                        + "pri čuvanju na disk. Podaci nisu sačuvani u celosti.\n"
                        + "Pogledajte log za više informacija. Zatvoriti?",
                        "Greška", JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE,
                        null, opcije, opcije[1]);
                if (zatvori == 0) {
                    finalizeLogger();
                    System.exit(1);
                }
            } catch (Exception | Error ex) {
                try {
                    LOGGER.log(Level.SEVERE, "Nepoznata greška pri čuvanju podataka", ex);
                } catch (Exception | Error ex2) {
                    ex2.printStackTrace(); //nikad?
                }
                int zatvori = showOptionDialog(null, "Došlo je do nepoznate greške pri čuvanju podataka."
                        + "\n Pogledajte log za više informacija. Zatvoriti?",
                        "Greška", JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE,
                        null, opcije, opcije[1]);
                if (zatvori == 0) {
                    finalizeLogger();
                    System.exit(2);
                }
            }
        } else {
            finalizeLogger();
            System.exit(0);
        }
    }

    /**
     * Zaustavlja trenutni Thread na odredjeni period, cuva podatke i ponavlja
     * isti proces. BLOKIRA TRENUTNI THREAD DO ZAUSTAVLJANJA PROGRAMA !!!
     */
    private static void autosave() {
        if(Config.get("savePeriod").equals("0")) {
            LOGGER.log(Level.FINE, "autosave isključen. Gasim main Thread");
            return;
        }
        try {
            autosave_period = (int)(Float.parseFloat(Config.get("savePeriod"))*60_000);
            LOGGER.log(Level.CONFIG, "autosave period: {0}", autosave_period);
        }
        catch(NumberFormatException ex) {
            LOGGER.log(Level.SEVERE, "Greška pri parsiranju autosave perioda iz configa", ex);
        }
        while (true) {
            try {
                Thread.sleep(autosave_period);
                LOGGER.log(Level.FINER, "autosaving...");
                Save.save();
            } catch (InterruptedException ex) { //ne bi trebalo da se desi; ako se desi, ignorisati
                LOGGER.log(Level.WARNING, "autosave interrupted", ex);
            } catch (IOException ex) {
                LOGGER.log(Level.SEVERE, "Greška pri automatskom čuvanju podataka", ex);
                JOptionPane.showMessageDialog(null, "Greška pri automatskom čuvanju podataka",
                        "I/O greska", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

}
