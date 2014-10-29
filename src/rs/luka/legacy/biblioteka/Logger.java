package rs.luka.legacy.biblioteka;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import static java.lang.Runtime.getRuntime;
import static java.lang.System.getProperty;
import static java.nio.file.Files.copy;
import java.nio.file.StandardCopyOption;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.NoSuchElementException;
import javax.swing.JOptionPane;
import rs.luka.biblioteka.funkcije.Utils;
import static javax.swing.JOptionPane.showMessageDialog;

/**
 *
 * @author Luka
 * @since kraj '13.
 */
public class Logger {

    private final File log = new File(Utils.getWorkingDir() + "Biblioteka.log");
    private final File bak = new File(Utils.getWorkingDir() + "log.bak");
    private String date;

    private void logInit() {
        DateFormat df = new SimpleDateFormat("dd/MMM/yyyy HH:mm:ss");
        Date date = new Date();
        try (FileWriter logger = new FileWriter(log, true);
                FileWriter backup = new FileWriter(bak, true)) {
            log.createNewFile();
            bak.createNewFile();
            if (log.length() != bak.length() && bak.exists()) {
                copy(bak.toPath(), log.toPath(), StandardCopyOption.REPLACE_EXISTING);
                logger.append(df.format(date) + "\tFajl se nije podudarao sa backup-om. Ispravljeno\n");
                backup.append(df.format(date) + "\tFajl se nije podudarao sa backup-om. Ispravljeno\n");
            }
        } catch (IOException ex) {
            log(ex);
        }
        this.date = df.format(date);
    }

    /**
     * Metoda za log uzimanja i vracanja.
     *
     * @param c case 'u': uzimanje; case 'v': vracanje;
     * @param knj knjiga
     * @param uc ucenik
     */
    public void log(char c, String knj, String uc) {
        logInit();
        try (FileWriter logger = new FileWriter(log, true);
                FileWriter backup = new FileWriter(bak, true);) {

            switch (c) {
                case 'u':
                    logger.append(date + "\t" + uc + " je uzeo/la knjigu " + knj + "\n");
                    backup.append(date + "\t" + uc + " je uzeo/la knjigu " + knj + "\n");
                    if (getProperty("os.name").startsWith("Windows")) {
                        getRuntime().exec("attrib +H " + bak.getCanonicalPath());
                    }
                    break;
                case 'v':
                    logger.append(date + "\t" + uc + " je vratio/la knjigu " + knj + "\n");
                    backup.append(date + "\t" + uc + " je vratio/la knjigu " + knj + "\n");
                    if (getProperty("os.name").startsWith("Windows")) {
                        getRuntime().exec("attrib +H " + bak.getCanonicalPath());
                    }
                    break;
                default:
                    throw new UnsupportedOperationException("Ne postoji.");
            }

        } catch (IOException ex) {
            log(ex);
        }
    }

    /**
     * Metoda za log unosa podataka.
     */
    public void log() {
        logInit();
        try (FileWriter backup = new FileWriter(bak, true);
                FileWriter logger = new FileWriter(log, true);) {

            logger.append(date + "\t" + "Uneseni podaci" + "\n");
            backup.append(date + "\t" + "Uneseni podaci" + "\n");
            if (getProperty("os.name").startsWith("Windows")) {
                getRuntime().exec("attrib +H " + bak.getCanonicalPath());
            }
        } catch (IOException ex) {
            log(ex);
        }
    }

    /**
     * Metoda za log unosa novog naslova
     *
     * @param nas naslov
     */
    public void log(String nas) {
        logInit();
        try (FileWriter backup = new FileWriter(bak, true);
                FileWriter logger = new FileWriter(log, true);) {
            logger.append(date + "\t" + "Unesen novi naslov \"" + nas + "\"\n");
            backup.append(date + "\t" + "Unesen novi naslov \"" + nas + "\"\n");
            if (getProperty("os.name").startsWith("Windows")) {
                getRuntime().exec("attrib +H " + bak.getCanonicalPath());
            }
        } catch (IOException ex) {
            log(ex);
        }
    }

    /**
     * Metoda za log unosa/brisanja ucenika
     *
     * @param add true - dodavanje ucenika, false - brisanje ucenika
     * @param uc ucenik
     */
    public void log(boolean add, String uc) {
        logInit();
        try (FileWriter backup = new FileWriter(bak, true);
                FileWriter logger = new FileWriter(log, true);) {
            if (add) {
                logger.append(date + "\t" + "Unesen novi ucenik " + uc + "\n");
                backup.append(date + "\t" + "Unesen novi ucenik " + uc + "\n");
            } else {
                logger.append(date + "\t" + "Obrisan ucenik " + uc + "\n");
                backup.append(date + "\t" + "Obrisan ucenik " + uc + "\n");
            }
            if (getProperty("os.name").startsWith("Windows")) {
                getRuntime().exec("attrib +H " + bak.getCanonicalPath());
            }
        } catch (IOException ex) {
            log(ex);
        }
    }

    /**
     * Loguje gresku(upisuje StackTrace, ako dodje do greske onda koristi
     * ex.toString() metodu)
     *
     * @param ex Exception - greska
     */
    public void log(Throwable ex) {
        logInit();
        try (FileWriter backup = new FileWriter(bak, true);
                FileWriter logger = new FileWriter(log, true);) {
            PrintWriter pw;
            String stackTrace;
            try (StringWriter sw = new StringWriter()) {
                pw = new PrintWriter(sw);
                ex.printStackTrace(pw);
                stackTrace = sw.toString();
            }
            pw.close();
            logger.append(date + "\t" + "Detalji o gresci:\n" + stackTrace + "\n");
            backup.append(date + "\t" + "Detalji o gresci:\n" + stackTrace + "\n");
            if (getProperty("os.name").startsWith("Windows")) {
                getRuntime().exec("attrib +H " + bak.getCanonicalPath());
            }
        } catch (IOException IOex) {
            String err = ex.getMessage() + "\n" + ex.getCause();
            showMessageDialog(null, "I/O greska pri logovanju greske: \n"
                    + err + "\nGreska nece biti logovana", "I/O GRESKA!!!", JOptionPane.ERROR_MESSAGE);
        } catch (NullPointerException | NoSuchElementException NullEx) {
            try (FileWriter backup = new FileWriter(bak);
                    FileWriter logger = new FileWriter(log);) {
                logger.append(ex.toString() + "\n");
                backup.append(ex.toString() + "\n");
            } catch (IOException ex1) {
                showMessageDialog(null, "Greska pri logovanju greske.", "GRESKA!!!", 0);
            }
        }
    }
}
