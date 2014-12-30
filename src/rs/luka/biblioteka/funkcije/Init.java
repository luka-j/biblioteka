/**
 * @lastmod 
 * 27.12.'14. 
 * bugfixing, sredjivanje PeriodicActions
 */
/**
 * @curr 
 * ...
 */
/**
 * @bugs 
 * Ucenici sidePan.setPreferredSize ne radi, postoji workaround koji se resetuje pri scroll-u 
 * ^^^ Samo u netbeans okruzenju, setPreferredSize radi normalno kada se pokrene posebno kao aplikacija !!! 
 * undo u kombinaciji sa prethodnim redo-om izaziva exception, ako se iz stacka izbrisu neke akcije
 * pri push(), tako da setKnjiga throwuje Duplikat (da li smem ignorisati?)
 * undoVracanje postavlja datum na trenutni, umesto datum iznajmljivanja knjige
 */
/**
 * @todo 
 * ISTESTIRATI SVE (UNIT TESTS, DEBUGGING) 
 * Smisliti nacin da ponovo iscrta prozor u showTextFieldDialog ako throwuje Exception 
 * Bugfixing, optimizacija koda, cišenje koda 
 * Ubaciti kvačice (šđžčć) 
 * Napraviti pravu implementaciju MultiMap-e (umesto 2 arraylist-e) 
 * Izbaciti sve preostale workaround-ove
 */
/**
 * @changelog 
 * Ubacio PeriodicActions (izbacio deo iz Init-a) 
 * Fodao datePeriod (period automatske provere datuma, float, u danima) u Config i Podesavanja
 * Proverava datum na 24h (ako je program ukljucen duze vreme), 
 * preimenovao autosave() u periodicActions() i dodao (razdvojio na) autosave() i checkDate()
 * ucSort - korisnik (preko configa) bira kako ce ucenici biti
 * sortirani, po imenu ili razredu bugfixing, grafika uglavnom, UVButton default
 * listener se ne aktivira ako vec postoji neki dodao font weight i popravio
 * verovatni bug u Config-u Pomerio changelog u fajl.
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
//7312 linije, 30.12.'14. (trenutno, fontovi, ucSort, PeriodicActions, ICheckbox, cleanup)

//1115 linija u packageu, 24.8.'14.
//1155 linija, 24.9.'14.
//1396 linija, 25.10.'14.
//1460 linija, 18.11.'14.
//1318 linija, 25.12.'14. (Knjige/Ucenici izbaceni)
//1438 linija, 29.12.'14. (trenutno, auto)
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
import static rs.luka.biblioteka.data.Datumi.proveriDatum;
import rs.luka.biblioteka.data.Podaci;
import static rs.luka.biblioteka.data.Podaci.loadData;
import static rs.luka.biblioteka.data.Ucenik.setValidRazred;
import static rs.luka.biblioteka.funkcije.Logger.finalizeLogger;
import static rs.luka.biblioteka.funkcije.Logger.initLogger;
import static rs.luka.biblioteka.funkcije.Undo.initUndo;
import static rs.luka.biblioteka.funkcije.Utils.initWorkingDir;
import static rs.luka.biblioteka.funkcije.Utils.setWorkingDir;
import rs.luka.biblioteka.grafika.Dijalozi;
import rs.luka.biblioteka.grafika.Grafika;
import static rs.luka.biblioteka.grafika.Grafika.initGrafika;
import static rs.luka.biblioteka.funkcije.PeriodicActions.doPeriodicActions;

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
        showMessageDialog(null, "Došlo je do neočekivane greške. Detalji:\n" + stackTrace
                + "\novi podaci su sačuvani u log.", "Nepoznata greška", JOptionPane.ERROR_MESSAGE);
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
    private static final int MAX_EXITS = 3;
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
        initGrafika();
        setValidRazred();
        loadData();
        //new Test().testUnos();
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
                int zatvori = showOptionDialog(null, "Došlo je do greške "
                        + "pri čuvanju na disk. Podaci nisu sačuvani u celosti.\n"
                        + "Pogledajte log za više informacija. Zatvoriti?",
                        "Greška", JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE,
                        null, opcije, opcije[1]);
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
                int zatvori = showOptionDialog(null, "Došlo je do nepoznate greške pri čuvanju podataka."
                        + "\n Pogledajte log za više informacija. Zatvoriti?",
                        "Greška", JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE,
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
