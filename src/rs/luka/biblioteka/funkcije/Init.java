/**
 * @lastmod 8.1.'15. 
 * LosFormat u konstruktorima Ucenika i Knjiga
 */
/**
 * @curr 
 * ...
 */
/**
 * @bugs 
 * prozor Ucenici ide preko prozora za Unos
 * Unos ucenika - oduzima knjige od biblioteke ili ne? (Config opcija)
 * Ucenici sidePan.setPreferredSize ne radi, postoji workaround koji se resetuje pri scroll-u 
 * ^^^ Samo u netbeans okruzenju, setPreferredSize radi normalno kada se pokrene posebno kao aplikacija !!! 
 * undo u kombinaciji sa prethodnim redo-om izaziva exception, ako se iz stacka izbrisu neke akcije
 * pri push(), tako da setKnjiga throwuje Duplikat (da li smem ignorisati?)
 * undoVracanje postavlja datum na trenutni, umesto datum iznajmljivanja knjige
 */
/**
 * @todo 
 * ISTESTIRATI SVE (UNIT TESTS, DEBUGGING)
 * Koristiti DA_STRING i NE_STRING
 * Pretraga na osnovu Levenshtein distance stringova
 * indexKnjige unutar UK - potreban ?
 * Smisliti nacin da ponovo iscrta prozor u showTextFieldDialog ako throwuje Exception 
 * Bugfixing, optimizacija koda, cišenje koda (starog pogotovo, videti imena)
 * Custom Logging handler, custom error messages
 * Napraviti pravu implementaciju MultiMap-e (umesto 2 arraylist-e) 
 * Izbaciti sve preostale workaround-ove
 */
/**
 * @changelog 
 * LosFormat u konstruktorima, ne dozvoljava / u imenima
 * Dodao globalne precice sa tastature, izbacio reflekciju i input/actionMap
 * Popravio pretragu knjiga, izbacio neke nepotrebne refreshove
 * Ubacio prikaz ucenika/knjiga s istim imenom/naslovom i checkUniqueness()/is*Unique
 * Izbacio knjige/ucenici S/V, ubacio customSize i win.pack()
 * Dodao Ucenici#shiftLeft i napravio da bude opciono (shiftKnjige u Config-u)
 * Popravio bagove sa kolicinom, morao da dodam Knjiga getOriginal(int i) zbog IOString konstruktora
 * Prebacio povecaj/smanjiKolicinu() u UcenikKnjiga, testirati da li je izvodljivo uopste
 * Napokon puna podrska za naslove s razlicitim imenom, a istim piscem, drugaciji format cuvanja
 * Promenio UcenikKnjiga da se sastoji od objekta Knjiga i Date
 * Ubrzao uzimanje/vracanje uvodjenjem Ucenici#refreshUcenik umesto ponovnog iscrtavanja celog prozora
 * Počistio unos, obrisao ~100ak linija, sad ubacuje direktno u memoriju. Neka ostane u posebnoj klasi za sada.
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
//7958 linije, 8.1.'15. (trenutno, config opcije&Strings, PeriodicActions, ICheckbox, setKol, UniqueList, 
//                       viseKnjiga, Knjiga u UK, displayName, optimizacija, cleanup)

//1115 linija u packageu, 24.8.'14.
//1155 linija, 24.9.'14.
//1396 linija, 25.10.'14.
//1460 linija, 18.11.'14.
//1318 linija, 25.12.'14. (Knjige/Ucenici izbaceni)
//1333 linija, 8.1.'15. (trenutno, auto, cleanup)
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
import static rs.luka.biblioteka.grafika.Konstante.*;

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
        showMessageDialog(null, HANDLER_MSG1_STRING + stackTrace + HANDLER_MSG2_STRING, HANDLER_TITLE_STRING, 
                JOptionPane.ERROR_MESSAGE);
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
     * Radi inicijalizaciju programa. Zove init*, load* i set* metode
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
            System.exit(-1);
        }
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
                } catch (Throwable ex1) {
                    ex1.printStackTrace(); //nikad?
                }
                int zatvori = showOptionDialog(null, EXIT_IOEX_MSG_STRING, EXIT_IOEX_TITLE_STRING, 
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
                } catch (Exception | Error ex2) {
                    ex2.printStackTrace(); //nikad?
                }
                int zatvori = showOptionDialog(null, EXIT_TWBL_MSG_STRING,
                        EXIT_TWBL_TITLE_STRING, JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE,
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
