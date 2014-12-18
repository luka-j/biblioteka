package rs.luka.biblioteka.funkcije;

import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.SimpleFormatter;
import javax.swing.JOptionPane;
import rs.luka.biblioteka.data.Config;

/**
 * Sadrži root logger za aplikaciju i metode za inicijalizaciju i gašenje istog.
 *
 * @author luka
 * @since 18.9.2014.
 */
public class Logger {

    /**
     * Root logger aplikacije, rs.luka.biblioteka.
     */
    public static final java.util.logging.Logger ROOT_LOGGER = java.util.logging.Logger.getLogger("rs.luka.biblioteka");
    /**
     * handler namenjen za root logger, trebalo bi da koristi UTF-8 i
     * simpleformat.
     */
    private static java.util.logging.Handler handler;
    /**
     * Logging level za handler i root logger. Pri ucitavanju je INFO, a pri
     * inicijalizaciji u metodi se uzima vrednost iz configa.
     */
    private static Level LOGGING_LEVEL = Level.INFO;

    private static int FILE_SIZE_LIMIT;
    private static int FILE_COUNT;

    /**
     * Inicijalizuje logger, postavlja handlere i logging level.
     *
     * @since 18.9.'14.
     */
    public static void initLogger() {
        LOGGING_LEVEL = Level.parse(Config.get("logLevel"));
        FILE_SIZE_LIMIT = Config.getAsInt("logSizeLimit");
        FILE_COUNT = Config.getAsInt("logFileCount");
        ROOT_LOGGER.setLevel(LOGGING_LEVEL);
        try {
            File logFolder = new File(Utils.getWorkingDir() + "logs");
            if (!logFolder.exists() && !logFolder.mkdir()) {
                throw new IOException("logFolder nije kreiran");
            }
            handler = new FileHandler(Utils.getWorkingDir() + "logs/biblioteka.log%g",
                    FILE_SIZE_LIMIT, FILE_COUNT, false);
            handler.setEncoding("UTF-8");
            handler.setFormatter(new SimpleFormatter());
            handler.setLevel(LOGGING_LEVEL);

            ROOT_LOGGER.addHandler(handler);
            //za debugging:
            //for(java.util.logging.Handler handler0 : java.util.logging.Logger.getLogger("").getHandlers())
            //    if(handler0 instanceof ConsoleHandler) {
            //        handler0.setLevel(Level.FINE);
            //        System.out.println("found");}
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Greška pri inicijalizaciji loggera.\n"
                    + "Neke funkcije logovanja neće raditi", "I/O Greska",
                    JOptionPane.ERROR_MESSAGE);
        } catch (SecurityException ex) {
            JOptionPane.showMessageDialog(null, "Sigurnosna greška pri inicijalizaciji loggera.\n"
                    + "Neke funkcije logovanja neće raditi", "Sigurnosna greška", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Finalizuje logger, loguje izlazak i zatvara handler. Nakon zvanja ove
     * metode, svi drugi loggeri loguju direktno preko root loggera platforme
     *
     * @since 18.9.'14.
     */
    public static void finalizeLogger() {
        if (handler != null) {
            ROOT_LOGGER.log(Level.INFO, "Program izlazi. Gasim logger.");
            handler.close();
            ROOT_LOGGER.removeHandler(handler);
        }
    }

    private Logger() {
    }
}
