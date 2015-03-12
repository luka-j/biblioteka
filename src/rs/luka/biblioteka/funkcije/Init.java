/**
 * @lastmod 11.3.'15. 
 * final Konstante
 */
/**
 * @curr 
 * XMLProperties & config
 */
/**
 * @bugs
 * prozor Ucenici ide preko prozora za Unos
 * Ucenici sidePan.setPreferredSize ne radi, postoji workaround koji se resetuje pri scroll-u 
 * ^^^ Samo u netbeans okruzenju, setPreferredSize radi normalno kada se pokrene posebno kao aplikacija !!! 
 * undo u kombinaciji sa prethodnim redo-om izaziva exception, ako se iz stacka izbrisu neke akcije
 * pri push(), tako da setKnjiga throwuje Duplikat (da li smem ignorisati?)
 * undoVracanje postavlja datum na trenutni, umesto datum iznajmljivanja knjige
 */
/**
 * @todo 
 * ISTESTIRATI SVE (UNIT TESTS, DEBUGGING)
 * proveriti sve (XMLProp), dokumentacija, optimizacija
 * parallel u XMLProps ?
 * Unos ucenika - oduzima knjige od biblioteke ili ne? (Config opcije)
 * Lock za fajlove (ne moze vise od jednog programa da pristupi)
 * Kategorije knjiga
 * Pretraga na osnovu Levenshtein distance stringova
 * indexKnjige unutar UK - potreban ?
 * Smisliti nacin da ponovo iscrta prozor u showTextFieldDialog ako throwuje Exception 
 * Bugfixing, optimizacija koda, cišenje koda (starog pogotovo, videti imena)
 * Custom error messages (dijalozi)
 * Izbaciti sve preostale workaround-ove
 */
/**
 * @changelog 
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
//6550 linija, 29.11.'14. (konstante, code (re-)organization)
//7110 linija, 25.12.'14. (dodat UVButton, izbacen Knjige i Ucenici, cleanup, bsh konzola)
//8075 linija, 23.1.'15. (config opcije&Strings&cleanup, PeriodicActions, ICheckbox, setKol, UniqueList, 
//                       viseKnjiga, Knjiga u UK, displayName, Formatter, kazne, optimizacija, cleanup)
//8786 linija, 23.2.'15. (XMLProperties)

//1115 linija u packageu, 24.8.'14.
//1155 linija, 24.9.'14.
//1396 linija, 25.10.'14.
//1460 linija, 18.11.'14.
//1318 linija, 25.12.'14. (Knjige/Ucenici izbaceni)
//1542 linija, 23.1.'15. (auto, logger, cleanup)
//1552 linija, 19.2.'15.
package rs.luka.biblioteka.funkcije;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import static java.lang.Thread.setDefaultUncaughtExceptionHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.showMessageDialog;
import static javax.swing.JOptionPane.showOptionDialog;
import rs.luka.biblioteka.data.Config;
import static rs.luka.biblioteka.data.Config.loadConfig;
import static rs.luka.biblioteka.funkcije.Datumi.proveriDatum;
import rs.luka.biblioteka.data.Podaci;
import static rs.luka.biblioteka.data.Podaci.loadData;
import static rs.luka.biblioteka.data.Ucenik.initUcenik;
import static rs.luka.biblioteka.funkcije.Logger.finalizeLogger;
import static rs.luka.biblioteka.funkcije.Logger.initLogger;
import static rs.luka.biblioteka.funkcije.Undo.initUndo;
import static rs.luka.biblioteka.funkcije.Utils.initWorkingDir;
import static rs.luka.biblioteka.funkcije.Utils.setWorkingDir;
import rs.luka.biblioteka.grafika.Dijalozi;
import rs.luka.biblioteka.grafika.Grafika;
import static rs.luka.biblioteka.grafika.Grafika.initGrafika;
import static rs.luka.biblioteka.funkcije.PeriodicActions.doPeriodicActions;
import rs.luka.biblioteka.grafika.Konstante;

/**
 *
 * @author Luka
 * @since '14.
 */
class Handler implements Thread.UncaughtExceptionHandler {

    private static final Logger LOG = Logger.getLogger(Handler.class.getName());

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        /* if(e instanceof sun.awt.X11.XException) { //desava se povremeno na Linuxu
         handleX11Ex();
         return;
         }*/
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        String stackTrace = sw.toString();

        LOG.log(Level.SEVERE, "Uncaught exception", e);
        showMessageDialog(null, Init.dData.HANDLER_MSG1_STRING + stackTrace + Init.dData.HANDLER_MSG2_STRING, 
                Init.dData.HANDLER_TITLE_STRING, JOptionPane.ERROR_MESSAGE);
    }

    private void handleX11Ex() {
        new rs.luka.biblioteka.grafika.Ucenici().pregledUcenika();
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

    private static final java.util.logging.Logger LOGGER = 
            java.util.logging.Logger.getLogger(Init.class.getName());
    
    /**
     * Maksimalan broj izlazenja (ili pokusaja izlazenja).
     */
    private static final int MAX_EXITS = 5;
    /**
     * Oznacava broj izlaza do sada.
     */
    private static int exitCount = 0;
    /**
     * DisplayData. Objekat koji cuva konstante. Mora da bude u Init zbog toga što
     * se ova klasa prva inicijalizuje, iako je logičnije da stoji u Grafika. Don't ask.
     */
    public static final Konstante dData = new Konstante();

    /**
     * Postavlja default UncaughtExceptionHandler, iscrtava mali prozor koji
     * oznacava da je ucitavanje podataka u toku, zove {@link #init()} ,
     *
     * @param args
     */
    public static void main(String[] args) {
        setDefaultUncaughtExceptionHandler(new Handler());
        Dijalozi.drawInfoWindow("Učitavanje podataka...", "Učitavanje podataka...");
        init();
        Dijalozi.disposeInfoWindow();
        
        doPeriodicActions();
    }

    /**
     * Radi inicijalizaciju programa. Zove init*, load* i setConfigEntry* metode
     * odgovarajucim redosledom. Vidi see also odeljak. Ne menjati redosled,
     * osim ako neki deo ne radi (exceptioni pri inicijalizaciji).
     *
     * @see Utils#initWorkingDir
     * @see Config#loadConfig
     * @see Utils#setWorkingDir
     * @see rs.luka.biblioteka.funkcije.Logger#initLogger
     * @see Grafika#initGrafika
     * @see rs.luka.biblioteka.data.Ucenik#initUcenik
     * @see Podaci#loadData
     * @see rs.luka.biblioteka.data.Datumi#proveriDatum
     * @see Undo#initUndo
     */
    public static void init() {
        initWorkingDir();
        loadConfig(); //radi i loadStrings()
        setWorkingDir();
        initLogger();
        //loadStrings();
        initGrafika();
        initUcenik();
        loadData();
        proveriDatum();
        initUndo();
        new rs.luka.biblioteka.grafika.Ucenici().pregledUcenika();

        LOGGER.log(Level.INFO, "Inicijalizacija programa gotova.");
    }

    /**
     * Cuva podatke i zatvara aplikaciju. Sav error handling radi unutar metode.
     *
     * @param sacuvaj
     */
    public static void exit(boolean sacuvaj) {
        exitCount++;
        if (exitCount == MAX_EXITS) {
            LOGGER.log(Level.SEVERE, "Previše neuspešnih pokušaja izlaza. Forcequit");
            System.exit(-1);
        }
        String opcije[] = {dData.DA_STRING, dData.NE_STRING};
        LOGGER.log(Level.INFO, "Izlazim iz programa... Čuvam podatke: {0}", sacuvaj);
        if (sacuvaj) {
            try {
                Save.save();
                finalizeLogger();
                System.exit(0);
            } catch (IOException ex) {
                try {
                    LOGGER.log(Level.SEVERE, "I/O Greška pri čuvanju podataka.", ex);
                } catch (Throwable ex1) {
                    ex1.printStackTrace(); //nikad?
                }
                int zatvori = showOptionDialog(null, dData.EXIT_IOEX_MSG_STRING, dData.EXIT_IOEX_TITLE_STRING, 
                        JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE, null, opcije, opcije[1]);
                if (zatvori == 0) {
                    finalizeLogger();
                    System.exit(1);
                } else {
                    exit(sacuvaj);
                }
            } catch (Throwable ex) {
                try {
                    LOGGER.log(Level.SEVERE, "Nepoznata greška pri čuvanju podataka", ex);
                } catch (Throwable ex2) {
                    ex2.printStackTrace(); //nikad?
                }
                int zatvori = showOptionDialog(null, dData.EXIT_TWBL_MSG_STRING,
                        dData.EXIT_TWBL_TITLE_STRING, JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE,
                        null, opcije, opcije[1]);
                if (zatvori == 0) {
                    finalizeLogger();
                    System.exit(2);
                } else {
                    exit(sacuvaj);
                }
            }
        } else {
            finalizeLogger();
            System.exit(0);
        }
    }
}
