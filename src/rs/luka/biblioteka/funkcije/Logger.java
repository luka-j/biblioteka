package rs.luka.biblioteka.funkcije;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import javax.swing.JOptionPane;
import rs.luka.biblioteka.data.Config;
import static rs.luka.biblioteka.grafika.Konstante.*;

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
    public static final java.util.logging.Logger ROOT_LOGGER = 
            java.util.logging.Logger.getLogger("rs.luka.biblioteka");
    /**
     * handler namenjen za root logger, trebalo bi da koristi UTF-8 i
     * simpleformat.
     */
    private static java.util.logging.Handler FILE_HANDLER = null;
    /**
     * Console handler za root logger. Koristiti umesto default handlera na "".
     */
    private static java.util.logging.Handler CONSOLE_HANDLER = null;
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
            if (FILE_SIZE_LIMIT > 0 && FILE_COUNT > 0) {
                File logFolder = new File(Utils.getWorkingDir() + "logs");
                if (!logFolder.exists() && !logFolder.mkdir()) {
                    throw new IOException("logFolder nije kreiran");
                }
                removeHandlers();
                FILE_HANDLER = new FileHandler(Utils.getWorkingDir() + "logs/biblioteka.log%g",
                        FILE_SIZE_LIMIT, FILE_COUNT, true);
                FILE_HANDLER.setEncoding("UTF-8");
                FILE_HANDLER.setFormatter(new BibliotekaFormatter());
                FILE_HANDLER.setLevel(LOGGING_LEVEL);
                CONSOLE_HANDLER = new ConsoleHandler();
                CONSOLE_HANDLER.setEncoding("UTF-8");
                CONSOLE_HANDLER.setFormatter(new BibliotekaFormatter());
                CONSOLE_HANDLER.setLevel(LOGGING_LEVEL);

                ROOT_LOGGER.addHandler(FILE_HANDLER);
                ROOT_LOGGER.addHandler(CONSOLE_HANDLER);
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, LOGGER_IOEX_MSG_STRING, LOGGER_IOEX_TITLE_STRING,
                    JOptionPane.ERROR_MESSAGE);
        } catch (SecurityException ex) {
            JOptionPane.showMessageDialog(null, LOGGER_SECEX_MSG_STRING, LOGGER_SECEX_TITLE_STRING, 
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Finalizuje logger, loguje izlazak i zatvara handler. Nakon zvanja ove
     * metode, svi drugi loggeri loguju direktno preko root loggera platforme
     *
     * @since 18.9.'14.
     */
    public static void finalizeLogger() {
        if (FILE_HANDLER != null) {
            ROOT_LOGGER.log(Level.INFO, "Program izlazi. Gasim logger.");
            FILE_HANDLER.close();
            CONSOLE_HANDLER.close();
            removeHandlers();
        }
    }
    
    private static void removeHandlers() {
        java.util.logging.Handler[] handlers = ROOT_LOGGER.getHandlers();
        for(java.util.logging.Handler handler : handlers) {
            ROOT_LOGGER.removeHandler(handler);
        }
        java.util.logging.Logger global = java.util.logging.Logger.getLogger("");
        handlers = global.getHandlers();
        for(java.util.logging.Handler handler : handlers) {
            global.removeHandler(handler);
        }
    }

    private Logger() {
    }
}

/**
 * Formatter koji se koristi za formatiranje izlaza Loggera.
 * @author lukatija
 * @since 15.1.'15.
 */
class BibliotekaFormatter extends Formatter {
    @Override
    public String format(LogRecord record) {
        StringBuilder format = new StringBuilder();
        format.append(Utils.getHumanReadableTime(record.getMillis())).append(' ');
        format.append(record.getSourceClassName()).append("#").append(record.getSourceMethodName());
        format.append('\n');
        format.append(record.getLevel()).append(": ");
        format.append(formatMessage(record));
        Throwable ex = record.getThrown();
        if(ex != null) {
            format.append("\n");
            format.append(ex.getClass().getCanonicalName()).append("Stack trace: \n");
            StackTraceElement[] stackTrace = ex.getStackTrace();
            for(int i=stackTrace.length-1; i>=0; i--) {
                format.append(stackTrace[i].toString()).append('\n');
            }
        }
        format.append('\n');
        return format.toString();
    }
    
    @Override
    public String formatMessage(LogRecord record) {
        Object[] params = record.getParameters();
        String msg = record.getMessage();
        if(params == null || params.length==0) {
            return msg;
        }
        if(msg.contains("{0")) 
            try{msg=MessageFormat.format(msg, params);} 
            catch(Exception ex) {ex.printStackTrace();} //formatiranje neuspelo, nastavi i vrati original
        return msg;
    }
    
    /**
     * Zamenjuje {n} sa params[n].
     * @param msg
     * @param params
     * @return 
     * @deprecated ne radi kako treba uvek
     */
    private String replaceParams(String msg, Object[] params) {
        char count = '0';
        String param;
        StringBuilder replace = new StringBuilder(msg);
        for(int i=0; i<replace.length()-2; i++) {
            if(replace.charAt(i) == '{' && 
               replace.charAt(i+1) == count &&
               replace.charAt(i+2) == '}') {
                param = params[count-'0'].toString();
                replace.delete(i, i+3);
                replace.append(param, 0, param.length());
                count++;
                i+=param.length();
            }
        }
        //System.out.println("\nFormatiran string: " + replace.toString() + "\n");
        return replace.toString();
    }
}